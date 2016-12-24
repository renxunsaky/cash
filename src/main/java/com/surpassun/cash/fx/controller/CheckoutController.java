package com.surpassun.cash.fx.controller;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.domain.Category;
import com.surpassun.cash.domain.Client;
import com.surpassun.cash.domain.GiftCard;
import com.surpassun.cash.domain.Product;
import com.surpassun.cash.fx.dto.ArticleDto;
import com.surpassun.cash.repository.CategoryRepository;
import com.surpassun.cash.repository.ClientRepository;
import com.surpassun.cash.repository.GiftCardRepository;
import com.surpassun.cash.service.ConfigService;
import com.surpassun.cash.service.OperationAuditService;
import com.surpassun.cash.service.ProductService;
import com.surpassun.cash.util.CacheUtil;
import com.surpassun.cash.util.CashUtil;
import com.surpassun.cash.util.LanguageUtil;
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
	private ConfigService configService;
	@Inject
	private ClientRepository clientRepository;
	@Inject
	private CategoryRepository categoryRepository;
	@Inject
	private OperationAuditService auditService;

	/******* Properties for calculator *********/
	@FXML
	TextField calculatorResult;
	private Float currentNumber;
	private Float savedResult;
	private String currentOperation;
	private KeyCodeCombination showCalculator = new KeyCodeCombination(KeyCode.UP, KeyCombination.META_DOWN);
	private KeyCodeCombination hideCalculator = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.META_DOWN);
	@FXML
	AnchorPane calculatorAnchorPane;

	/******* Properties for payment method *********/
	@FXML
	Button paymentGiftCardButton;
	@FXML
	Button paymentBankCardButton;
	@FXML
	Button paymentCashButton;
	private boolean paymentInProcess;

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
	@FXML
	Label payableByCouponInfo;
	@FXML
	Label warnInfo;

	private float totalPrice;
	private float payableByCoupon;
	private boolean strickModeOn;
	private boolean discountCouponForAll;
	private Float[] strickReductions;

	/******* Properties for bar code scanner usage *********/
	private StringBuilder currentBarcode;
	private Map<String, Integer> barcodeList;
	private GiftCardRepository giftCardRepository;

	/******* Properties for payment *********/
	@FXML
	Label toPay;
	@FXML
	Label received;
	@FXML
	Label toReturn;
	private String currentPaymentMode;
	private float receivedCash;
	private float receivedBankCard;
	private float receivedGiftCard;

	/******* Properties for shortcut buttons *********/
	@FXML
	GridPane categoryGrid;
	@FXML
	GridPane productGrid;
	@FXML
	GridPane priceGrid;
	private Map<String, String> currentCategory = new HashMap<String, String>();
	private Map<String, String> currentProduct = new HashMap<String, String>();

	public void show(Stage stage) {
		super.show(this, stage, Constants.FXML_DESIGN_CHECKOUT);
		init();
	}

	/**
	 * initialize top labels like client name, address, terminal number etc
	 */
	private void init() {
		// initialize general properties
		currentBarcode = new StringBuilder();
		barcodeList = new HashMap<String, Integer>();

		// initialize terminal information
		String employeeName = SecurityContextHolder.getContext().getAuthentication().getName();
		String terminalId = configService.findByName(Constants.TERMINAL_ID + StringPool.COLON + CashUtil.getCurrentMacAddress());
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

		// initialize article list
		articleName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
		articleOtherInfo.setCellValueFactory(new PropertyValueFactory<>("quantityInfo"));
		articlePrice.setCellValueFactory(new PropertyValueFactory<>("priceInfo"));

		// initialize configuration
		discountCouponForAll = configService.findBoolean(Constants.DISCOUNT_COUPON_FOR_ALL);
		strickModeOn = configService.findBoolean(Constants.STRICK_REDUCTION_ACTIVE);
		strickReductions = configService.findFloatListByName(Constants.STRICK_REDUCTION_VALUE);
		warnInfo.setText("");

		String adminPassword = configService.findByName(Constants.ADMIN_PASSWORD);
		CacheUtil.putCache(Constants.ADMIN_PASSWORD, adminPassword);

		// initialize shortcut buttons
		List<Node> children = categoryGrid.getChildren();
		List<Category> categories = categoryRepository.findByShortcutButtonEnabled(true);
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (children.size() > i) {
				Button node = (Button) children.get(i);
				node.setText(category.getName());
				node.setId(category.getCode());
				// set the first button as the default selected one
				if (i == 0) {
					currentCategory.put(category.getCode(), category.getName());
					node.getStyleClass().add(Constants.CLICKED);

					initializeShortcutButtons(category.getCode());
				}
			}
		}// end for
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
		String number = ((Button) event.getSource()).getText();
		if (currentNumber != null && currentOperation != null) {
			savedResult = doOperation(currentOperation, savedResult != null ? savedResult : currentNumber, Float.parseFloat(number));
		}
		calculatorResult.setText(trim(calculatorResult.getText() + number));
	}

	@FXML
	public void handleOperationKeys(ActionEvent event) {
		String text = calculatorResult.getText();
		currentNumber = Float.parseFloat(text);
		currentOperation = ((Button) event.getSource()).getText();
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
		case "+":
			result = firstNumber + secondNumber;
			break;
		case "-":
			result = firstNumber - secondNumber;
			break;
		case "*":
			result = firstNumber * secondNumber;
			break;
		case "/":
			result = firstNumber / secondNumber;
			break;
		default:
			break;
		}
		return result;
	}

	@FXML
	public void handleSpecialKeys(ActionEvent event) {
		String text = calculatorResult.getText();
		float newNumber = text != null ? Float.parseFloat(text) : 0F;
		String operation = ((Button) event.getSource()).getText();
		switch (operation) {
		case "C":
			currentNumber = null;
			currentOperation = null;
			savedResult = null;
			break;
		case "+/-":
			currentNumber = -newNumber;
			break;
		case "%":
			currentNumber = newNumber / 100;
			break;
		case "<=":
			currentNumber = 0F;
			break;
		default:
			break;
		}
		calculatorResult.setText(trim(currentNumber));
	}
	
	@FXML
	public void animateCalculator(KeyEvent event) {
		if (showCalculator.match(event)) {
			/**
			Animation collapsePanel = new Transition() {
				{
					setCycleDuration(Duration.millis(500));
				}

				@Override
				protected void interpolate(double fraction) {
					calculatorStackPane.setPrefWidth(480 * fraction);
				}
			};
			
			collapsePanel.setOnFinished(e -> {
				calculatorStackPane.getChildren().forEach(node -> {node.setVisible(true);});
				calculatorStackPane.getParent().setVisible(true);
			});
			collapsePanel.play();
			**/
			calculatorAnchorPane.setVisible(true);
			CashUtil.makeFadeInAnimation(500, calculatorAnchorPane);
		} else if (hideCalculator.match(event)) {
			/**
			Animation collapsePanel = new Transition() {
				{
					setCycleDuration(Duration.millis(300));
				}

				@Override
				protected void interpolate(double fraction) {
					calculatorStackPane.setPrefWidth(480 * (1.0 - fraction));
				}
			};
			collapsePanel.setOnFinished(e -> {
				calculatorStackPane.getChildren().forEach(node -> {node.setVisible(false);});
				calculatorStackPane.getParent().setVisible(false);
			});
			collapsePanel.play();
			**/
			CashUtil.makeFadeOutAnimation(500, calculatorAnchorPane);
			//calculatorAnchorPane.setVisible(false);
		}
	}

	@FXML
	public void handleScannerInput(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			String barcode = currentBarcode.toString();
			if (barcode.startsWith(Constants.BARCODE_PREFIX_MEMBER_CARD)) {
				Client client = clientRepository.findByCode(barcode);
				if (client != null) {
					StringBuilder clientInfoBuilder = new StringBuilder();
					clientInfoBuilder.append(client.getCode()).append(", ").append(client.getFirstname()).append(" ").append(client.getLastname()).append(", ").append(client.getAddress()).append(" ")
							.append(client.getPostcode()).append(" ").append(client.getCity()).append(", ").append(client.getPhone()).append(", ").append(client.getMembershipLevel().getName());
					clientInfo.setText(clientInfoBuilder.toString());
				} else {
					log.warn("Cannot find client with code : {}", barcode);
					warnInfo.setText(LanguageUtil.getMessage("ui.label.warn.client.not.found", barcode));
					CashUtil.makeFadeOutAnimation(5000, warnInfo);
				}
			} else if (barcode.startsWith(Constants.BARCODE_PREFIX_GIFT_CARD)) {
				if (Constants.PAYMENT_MODE_GIFT_CARD.equals(currentPaymentMode)) {
					GiftCard giftCard = giftCardRepository.findByCode(barcode);
					float giftCardBalance = giftCard.getBalance();
					float toBePayed = totalPrice - receivedGiftCard - receivedBankCard - receivedCash;
					boolean confirm = false;
					if (giftCardBalance - toBePayed >= 0) {
						confirm = CashUtil.createConfirmPopup(LanguageUtil.getMessage("ui.title.common.confirmation"), LanguageUtil.getMessage("ui.popup.giftcard.header", toBePayed),
								LanguageUtil.getMessage("ui.popup.confirmation.content"));
						if (confirm) {
							receivedGiftCard += toBePayed;
							giftCardBalance -= toBePayed;
							toBePayed = 0;
						}
					} else {
						confirm = CashUtil.createConfirmPopup(LanguageUtil.getMessage("ui.title.common.confirmation"),
								LanguageUtil.getMessage("ui.popup.giftcard.header.balance.not.enough", toBePayed), LanguageUtil.getMessage("ui.popup.confirmation.content.use.left", giftCardBalance));
						if (confirm) {
							receivedGiftCard += giftCardBalance;
							toBePayed -= giftCardBalance;
							giftCardBalance = 0;
						}
					}
					if (confirm) {
						StringBuilder sb = new StringBuilder().append(String.format("%.2f", toBePayed)).append(StringPool.SPACE).append(StringPool.EURO);
						updateLabel(toPay, sb);

						StringBuilder sb1 = new StringBuilder().append(String.format("%.2f", totalPrice - toBePayed)).append(StringPool.SPACE).append(StringPool.EURO);
						updateLabel(received, sb1);
					}
				}
			} else if (StringUtils.isNotBlank(barcode)) {
				if (!paymentInProcess) {
					// search barcode in database and display it on the screen
					Product product = productService.getProductByBarcode(barcode);
					if (product != null) {
						addProduct(barcode, product);
					} else {
						log.warn("Cannot find product with code : {}", barcode);
						warnInfo.setText(LanguageUtil.getMessage("ui.label.warn.product.not.found", barcode));
						CashUtil.makeFadeOutAnimation(5000, warnInfo);
					}
				}
			}

			// clear barcode buffer
			currentBarcode = new StringBuilder();
		} else if (event.getCode().isDigitKey()) {
			currentBarcode.append(event.getText());
		}
	}

	@FXML
	public void increaseQuantity() {
		ArticleDto articleDto = articleList.getSelectionModel().getSelectedItem();
		if (articleDto != null && !paymentInProcess) {
			int scannedNumber = articleList.getItems().size();
			Float strickDiscount = null;
			if (strickModeOn && strickReductions.length > scannedNumber) {
				strickDiscount = strickReductions[scannedNumber];
			}
			if (strickDiscount != null) {
				ArticleDto selectedItem = articleList.getSelectionModel().getSelectedItem();
				Product product = new Product();
				product.setId(selectedItem.getId());
				product.setPrice(selectedItem.getUnitPrice());
				product.setCode(selectedItem.getCode());
				product.setName(selectedItem.getDisplayName());
				product.setDiscount(selectedItem.getDiscount());
				addProduct(selectedItem.getCode(), product);
			} else {
				changeQuantity(true);
			}
		}
	}

	@FXML
	public void decreaseQuantity() {
		ArticleDto articleDto = articleList.getSelectionModel().getSelectedItem();
		if (articleDto != null) {
			int quantity = barcodeList.get(articleDto.getCode());
			if (!paymentInProcess && quantity > 1) {
				String password = CashUtil.createInputPopup(LanguageUtil.getMessage("ui.title.common.confirmation"), LanguageUtil.getMessage("ui.popup.require.password"),
						LanguageUtil.getMessage("ui.popup.enter.password"));
				if (CacheUtil.getCache(Constants.ADMIN_PASSWORD).equals(password)) {
					auditService.addAudit(StringUtils.substringBefore(terminalInfo.getText(), StringPool.COMMA), StringUtils.substringAfter(terminalInfo.getText(), StringPool.COMMA), articleDto,
							Constants.OPERATION_TYPE_DECREASE_QUANTITY);
					changeQuantity(false);
				} else {
					warnInfo.setText(LanguageUtil.getMessage("ui.label.warn.admin.password.incorrect"));
					CashUtil.makeFadeOutAnimation(5000, warnInfo);
				}
			}
		}
	}

	private void changeQuantity(boolean isIncrease) {
		ArticleDto articleDto = articleList.getSelectionModel().getSelectedItem();
		if (articleDto != null) {
			int quantity = barcodeList.get(articleDto.getCode());
			quantity = isIncrease ? quantity + 1 : quantity - 1;
			if (quantity > 0) {
				articleDto.correctInfo(quantity, strickModeOn);
				int index = articleList.getSelectionModel().getSelectedIndex();
				articleList.getItems().set(index, articleDto);
				updateTotalPrice(articleList.getItems());
				barcodeList.put(articleDto.getCode(), quantity);
				articleList.getSelectionModel().select(index);
			}
		}
	}

	@FXML
	public void deleteCurrentSelection() {
		if (!paymentInProcess) {
			String password = CashUtil.createInputPopup(LanguageUtil.getMessage("ui.title.common.confirmation"), LanguageUtil.getMessage("ui.popup.require.password"),
					LanguageUtil.getMessage("ui.popup.enter.password"));
			if (CacheUtil.getCache(Constants.ADMIN_PASSWORD).equals(password)) {
				ArticleDto articleDto = articleList.getSelectionModel().getSelectedItem();
				if (articleDto != null) {
					auditService.addAudit(StringUtils.substringBefore(terminalInfo.getText(), StringPool.COMMA), StringUtils.substringAfter(terminalInfo.getText(), StringPool.COMMA), articleDto,
							Constants.OPERATION_TYPE_DELETE);

					articleList.getItems().remove(articleDto);
					updateTotalPrice(articleList.getItems());
					barcodeList.remove(articleDto.getCode());
				}
			} else {
				warnInfo.setText(LanguageUtil.getMessage("ui.label.warn.admin.password.incorrect"));
				CashUtil.makeFadeOutAnimation(5000, warnInfo);
			}
		}
	}

	private void updateTotalPrice(ObservableList<ArticleDto> items) {
		totalPrice = 0F;
		payableByCoupon = 0F;
		for (ArticleDto article : items) {
			totalPrice += article.getRealPrice();
			if (article.getDiscount() == null || article.getDiscount().equals(0F)) {
				payableByCoupon += article.getRealPrice();
			}
		}
		if (discountCouponForAll) {
			payableByCoupon = totalPrice;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%.2f", totalPrice)).append(StringPool.SPACE).append(StringPool.EURO);
		updateLabel(totalPriceInfo, sb);
	}

	@FXML
	public void beginPayment() {
		paymentInProcess = true;
		// display total to pay, received information
		StringBuilder sb = new StringBuilder().append(String.format("%.2f", totalPrice)).append(StringPool.SPACE).append(StringPool.EURO);
		updateLabel(toPay, sb);

		updateLabel(received, null);
		
		//display the amount payable by discount coupon
		StringBuilder sb2 = new StringBuilder().append(String.format("%.2f", payableByCoupon)).append(StringPool.SPACE).append(StringPool.EURO);
		updateLabel(payableByCouponInfo, sb2);
	}

	@FXML
	public void cancelPayment() {
		paymentInProcess = false;
		// reset payment information
		receivedBankCard = 0F;
		receivedCash = 0F;
		receivedGiftCard = 0F;
		payableByCoupon = 0F;

		updateLabel(toPay, null);
		updateLabel(received, null);
		updateLabel(toReturn, null);
		updateLabel(payableByCouponInfo, null);

		currentPaymentMode = null;
		paymentGiftCardButton.getStyleClass().remove(Constants.CLICKED);
		paymentBankCardButton.getStyleClass().remove(Constants.CLICKED);
		paymentCashButton.getStyleClass().remove(Constants.CLICKED);
	}

	@FXML
	public void setPaymentMethod(ActionEvent event) {
		if (paymentInProcess) {
			paymentGiftCardButton.getStyleClass().remove(Constants.CLICKED);
			paymentBankCardButton.getStyleClass().remove(Constants.CLICKED);
			paymentCashButton.getStyleClass().remove(Constants.CLICKED);

			Button clickedButton = (Button) event.getSource();
			clickedButton.getStyleClass().add(Constants.CLICKED);

			currentPaymentMode = clickedButton.getId();
			if (Constants.PAYMENT_MODE_BANK_CARD.equals(currentPaymentMode)) {
				// TODO: propose bank card payment terminal
			} else {
				calculatorResult.clear();
			}
		}
	}

	@FXML
	public void handlePayment() {
		if (paymentInProcess) {
			if (!Constants.PAYMENT_MODE_BANK_CARD.equals(currentPaymentMode)) {
				if (Constants.PAYMENT_MODE_CASH.equals(currentPaymentMode)) {
					receivedCash = Float.parseFloat(calculatorResult.getText());
				}
				float totalReceived = receivedBankCard + receivedCash + receivedGiftCard;
				StringBuilder sb = new StringBuilder();
				sb.append(totalReceived).append(StringPool.SPACE).append(StringPool.EURO);
				updateLabel(received, sb);

				StringBuilder toReturn = new StringBuilder();
				toReturn.append(String.format("%.2f", totalReceived - totalPrice)).append(StringPool.SPACE).append(StringPool.EURO);
				updateLabel(this.toReturn, toReturn);
				currentPaymentMode = null;
			}
		}
	}

	@FXML
	public void printTicket() {
		// TODO: print ticket
	}

	@FXML
	public void handleDiscount(ActionEvent event) {
		if (!paymentInProcess) {
			Button clickedButton = (Button) event.getSource();
			String text = clickedButton.getText();
			String discount = StringUtils.substringBefore(text, StringPool.PERCENT);

			ArticleDto articleDto = articleList.getSelectionModel().getSelectedItem();
			if (articleDto != null) {
				articleDto.correctInfo(discount);
				int index = articleList.getSelectionModel().getSelectedIndex();
				articleList.getItems().set(index, articleDto);
				updateTotalPrice(articleList.getItems());
				articleList.getSelectionModel().select(index);
			}
		}
	}

	@FXML
	public void handleCategoryChange(ActionEvent event) {
		List<Node> children = categoryGrid.getChildren();
		for (Node node : children) {
			node.getStyleClass().removeAll(Constants.CLICKED);
		}
		currentCategory.clear();
		Button clickedButton = (Button) event.getSource();
		clickedButton.getStyleClass().add(Constants.CLICKED);
		String categoryCode = clickedButton.getId();
		currentCategory.put(categoryCode, clickedButton.getText());

		initializeShortcutButtons(categoryCode);
	}

	@FXML
	public void handleProductChange(ActionEvent event) {
		List<Node> children = productGrid.getChildren();
		for (Node node : children) {
			node.getStyleClass().removeAll(Constants.CLICKED);
		}
		currentProduct.clear();
		Button clickedButton = (Button) event.getSource();
		clickedButton.getStyleClass().add(Constants.CLICKED);
		String productCode = clickedButton.getId();
		currentProduct.put(productCode, clickedButton.getText());

		initializePriceButtons(productCode);
	}

	private void initializeShortcutButtons(String categoryCode) {
		String[] products = configService.findListByName(Constants.SHORTCUT_PRODUCTS + StringPool.COLON + categoryCode);

		List<Node> productButtons = productGrid.getChildren();
		int counter = 0;
		for (Node productButton : productButtons) {
			Button button = (Button) productButton;
			if (products != null && products.length > counter) {
				Product product = productService.getProductByBarcode(products[counter]);
				if (product != null && product.isShortcutButtonEnabled()) {
					button.setText(product.getName());
					button.setId(product.getCode());
					button.getStyleClass().removeAll(Constants.CLICKED);
					counter++;
				} else {
					clearButton(button);
				}

				// set the first button to default one
				if (counter == 1) {
					button.getStyleClass().add(Constants.CLICKED);
					currentProduct.put(product.getCode(), product.getName());
				}
			} else {
				clearButton(button);
			}
		}// end for

		initializePriceButtons(products != null ? products[0] : null);
	}
	
	private void clearButton(Button button) {
		button.setText("");
		button.setId("");
		button.getStyleClass().removeAll(Constants.CLICKED);
	}

	private void initializePriceButtons(String productCode) {
		String[] prices = configService.findListByName(Constants.SHORTCUT_PRICES + StringPool.COLON + productCode);
		List<Node> priceButtons = priceGrid.getChildren();
		int counter = 0;
		for (Node priceButton : priceButtons) {
			Button button = (Button) priceButton;
			clearButton(button);
			if (prices != null && prices.length > counter) {
				if (StringUtils.isNotBlank(prices[counter])) {
					button.setText(prices[counter]);
				}
			}
			counter++;
		}// end for
	}

	@FXML
	public void addProductManually(ActionEvent event) {
		if (!paymentInProcess) {
			Button priceButton = (Button) event.getSource();
			if (StringUtils.isNotBlank(priceButton.getText()) && NumberUtils.isNumber(priceButton.getText())) {
				String categoryCode = currentCategory.keySet().iterator().next();
				String barcode = currentProduct.keySet().iterator().next();
				
				Product product = new Product(currentCategory.get(categoryCode), barcode, priceButton.getText());
				addProduct(barcode, product);
			}
		}
	}

	private void addProduct(String barcode, Product product) {
		int scannedNumber = articleList.getItems().size();
		Float strickDiscount = null;
		if (strickModeOn && strickReductions.length > scannedNumber) {
			strickDiscount = strickReductions[scannedNumber];
		}

		Integer quantity = 1;
		if (barcodeList.containsKey(barcode)) {
			quantity = barcodeList.get(barcode) + 1;
		}
		// If strick mode is on, and they are the first three products, do not
		// add them to the map
		if (strickDiscount == null) {
			barcodeList.put(barcode, quantity);
		} else {
			barcodeList.put(barcode + ":" + strickDiscount, quantity);
		}
		ArticleDto articleDto = new ArticleDto(product, quantity, strickDiscount);
		// the equals method of ArticleDto is overridden, so it is secure to do
		// like this
		int index = articleList.getItems().indexOf(articleDto);
		if (index != -1 && strickDiscount == null) {
			articleList.getItems().set(index, articleDto);
		} else {
			articleList.getItems().add(articleDto);
		}

		updateTotalPrice(articleList.getItems());
	}

	private void updateLabel(Label label, StringBuilder value) {
		String text = StringUtils.substringBefore(label.getText(), StringPool.COLON);
		StringBuilder sb = new StringBuilder(text).append(StringPool.COLON).append(StringPool.SPACE).append(value != null ? value : "");
		label.setText(sb.toString());
	}
}
