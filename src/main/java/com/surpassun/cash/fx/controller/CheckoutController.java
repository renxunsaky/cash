package com.surpassun.cash.fx.controller;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.domain.Client;
import com.surpassun.cash.domain.Config;
import com.surpassun.cash.domain.Product;
import com.surpassun.cash.fx.dto.ArticleDto;
import com.surpassun.cash.repository.ClientRepository;
import com.surpassun.cash.repository.ConfigRepository;
import com.surpassun.cash.service.ProductService;
import com.surpassun.cash.util.CashUtil;
import com.surpassun.cash.util.StringPool;

@Component
public class CheckoutController extends SimpleController {
	
	private final Logger log = LoggerFactory.getLogger(CheckoutController.class);
	
	@Inject
	private ProductService productService;
	
	/******* Properties for header *********/
	@FXML
	Label terminalInfo;
	@FXML
	Label clientInfo;
	@FXML
	Label timer;
	@Inject
	private ConfigRepository configRepository;
	@Inject
	private ClientRepository clientRepository;

	/******* Properties for calculator *********/
	@FXML
	TextField calculatorResult;
	private Float currentNumber;
	private Float savedResult;
	private String currentOperation;
	
	/******* Properties for article list *********/
	@FXML
	TableView<ArticleDto> articleList;
	@FXML
	TableColumn<ArticleDto, String> articleOtherInfo;
	@FXML
	TableColumn<ArticleDto, String> articleName;
	@FXML
	TableColumn<ArticleDto, String> articlePrice;
	@FXML
	Label totalPriceInfo;
	
	private float totalPrice;
	
	
	/******* Properties for bar code scanner usage *********/
	private StringBuilder currentBarcode;
	private Map<String, Integer> barcodeList;
	
	public void show(Stage stage) {
		super.show(this, stage, Constants.FXML_DESIGN_CHECKOUT);
		init();
	}
	
	/**
	 * initialize top labels like client name, address, terminal number etc
	 */
	private void init() {
		//initialize general properties
		currentBarcode = new StringBuilder();
		barcodeList = new HashMap<String, Integer>();
		
		//initialize terminal information
		String employeeName = SecurityContextHolder.getContext().getAuthentication().getName();
		Config terminalConfig = configRepository.findByName(Constants.TERMINAL_ID + StringPool.COLON + CashUtil.getCurrentMacAddress());
		String terminalId = terminalConfig != null ? terminalConfig.getValue() : null;
		StringBuilder sb = new StringBuilder(terminalId).append(", ").append(employeeName);
		terminalInfo.setText(sb.toString());
		final DateFormat format = DateFormat.getInstance();
		final Timeline timeLine = new Timeline(new KeyFrame(Duration.minutes(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Calendar cal = Calendar.getInstance();
				timer.setText(format.format(cal.getTime()));
			}
		}));
		timeLine.setCycleCount(Animation.INDEFINITE);
		timeLine.play();
		
		//initialize article list
		articleName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
		articleOtherInfo.setCellValueFactory(new PropertyValueFactory<>("quantityInfo"));
		articlePrice.setCellValueFactory(new PropertyValueFactory<>("priceInfo"));
	}

	private String trim(String text) {
		float result = Float.parseFloat(text);
		return trim(result);
	}
	
	private String trim(Float text) {
		if (text == null) {
			return "0";
		}
		int intPart = text.intValue();
		if (text - intPart > 0) {
			return Float.toString(text);
		} else {
			return Integer.toString(intPart);
		}
	}
	
	@FXML
	public void handleDigitalKeys(ActionEvent event) {
		String number = ((Button)event.getSource()).getText();
		if (currentNumber != null && currentOperation != null) {
			savedResult = doOperation(currentOperation, savedResult != null ? savedResult : currentNumber, Float.parseFloat(number));
		}
		calculatorResult.setText(trim(calculatorResult.getText() + number));
	}

	@FXML
	public void handleOperationKeys(ActionEvent event) {
		String text = calculatorResult.getText();
		currentNumber = Float.parseFloat(text);
		currentOperation = ((Button)event.getSource()).getText();
		if (savedResult != null) {
			calculatorResult.setText(trim(savedResult));
			savedResult = null;
		} else {
			calculatorResult.setText("");
		}
	}
	
	@FXML
	public void handleEqual() {
		String text = calculatorResult.getText();
		float newNumber = Float.parseFloat(text);
		currentNumber = doOperation(currentOperation, currentNumber, newNumber);
		currentOperation = null;
		savedResult = null;
		calculatorResult.setText(trim(currentNumber));
	}
	
	private float doOperation(String operation, float firstNumber, float secondNumber) {
		float result = 0F;
		switch (currentOperation) {
			case "+" : result = firstNumber + secondNumber; break;
			case "-" : result = firstNumber - secondNumber; break;
			case "*" : result = firstNumber * secondNumber; break;
			case "/" : result = firstNumber / secondNumber; break;
			default: break;
		}
		return result;
	}
	
	@FXML
	public void handleSpecialKeys(ActionEvent event) {
		String text = calculatorResult.getText();
		float newNumber = text != null ? Float.parseFloat(text) : 0F;
		String operation = ((Button)event.getSource()).getText();
		switch (operation) {
			case "C" : 
				currentNumber = null;
				currentOperation = null;
				savedResult = null;
				break;
			case "+/-" : 
				currentNumber = -newNumber;
				break;
			case "%" :
				currentNumber = newNumber/100;
				break;
			case "<=" : 
				currentNumber = 0F; 
				break;
			default: break;
		}
		calculatorResult.setText(trim(currentNumber));
	}
	
	@FXML
	public void handleScannerInput(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			String barcode = currentBarcode.toString();
			if (barcode.startsWith(Constants.BARCODE_PREFIX_MEMBER_CARD)) {
				Client client = clientRepository.findByCode(barcode);
				if (client != null) {
					StringBuilder clientInfoBuilder = new StringBuilder();
					clientInfoBuilder.append(client.getCode()).append(", ").append(client.getFirstname()).append(" ").append(client.getLastname())
						.append(", ").append(client.getAdress()).append(" ").append(client.getPostcode()).append(" ").append(client.getCity())
						.append(", ").append(client.getPhone()).append(", ").append(client.getMembershipLevel().getName());
					clientInfo.setText(clientInfoBuilder.toString());
				} else {
					log.warn("Cannot find client with code : {}", barcode);
				}
			} else if (barcode.startsWith(Constants.BARCODE_PREFIX_GIFT_CARD)) {
				
			} else {
				//search barcode in database and display it on the screen
				Product product = productService.getProductByBarcode(barcode);
				if (product != null) {
					Integer quantity = 1;
					if (barcodeList.containsKey(barcode)) {
						quantity = barcodeList.get(barcode) + 1;
					}
					barcodeList.put(barcode, quantity);
					ArticleDto articleDto = new ArticleDto(product, quantity);
					//the equqls method of ArticleDto is overrided, so it is secure to do like this
					int index = articleList.getItems().indexOf(articleDto);
					if (index != -1) {
						articleList.getItems().set(index, articleDto);
					} else {
						articleList.getItems().add(articleDto);
					}
					
					updateTotalPrice(articleList.getItems());
				} else {
					log.warn("Cannot find product with code : {}", barcode);
				}
			}
			
			//clear barcode buffer
			currentBarcode = new StringBuilder();
		} else if (event.getCode().isDigitKey()) {
			currentBarcode.append(event.getText());
		}
	}

	@FXML
	public void increaseQuantity() {
		changeQuantity(true);
	}
	
	@FXML
	public void decreaseQuantity() {
		changeQuantity(false);
	}
	
	private void changeQuantity(boolean isIncrease) {
		ArticleDto articleDto = articleList.getSelectionModel().getSelectedItem();
		if (articleDto != null) {
			int quantity = barcodeList.get(articleDto.getCode());
			quantity = isIncrease ? quantity + 1 : quantity -1;
			if (quantity > 0) {
				articleDto.correctInfo(quantity);
				int index = articleList.getSelectionModel().getSelectedIndex();
				articleList.getItems().set(index, articleDto);
				updateTotalPrice(articleList.getItems());
				barcodeList.put(articleDto.getCode(), quantity);
				articleList.getSelectionModel().select(index);
			} else {
				deleteCurrentSelection();
			}
		}
	}
	
	@FXML
	public void deleteCurrentSelection() {
		ArticleDto articleDto = articleList.getSelectionModel().getSelectedItem();
		if (articleDto != null) {
			articleList.getItems().remove(articleDto);
			updateTotalPrice(articleList.getItems());
			barcodeList.remove(articleDto.getCode());
		}
	}
	
	private void updateTotalPrice(ObservableList<ArticleDto> items) {
		totalPrice = 0;
		for (ArticleDto article : items) {
			totalPrice += article.getRealPrice();
		}
		String text = StringUtils.substringBefore(totalPriceInfo.getText(), StringPool.COLON);
		StringBuilder sb = new StringBuilder(text).append(StringPool.COLON);
		sb.append(StringPool.SPACE).append(totalPrice).append(StringPool.SPACE).append(StringPool.EURO);
		totalPriceInfo.setText(sb.toString());
	}
	
}
