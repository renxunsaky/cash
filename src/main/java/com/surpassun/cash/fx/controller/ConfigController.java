package com.surpassun.cash.fx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.domain.Category;
import com.surpassun.cash.domain.Client;
import com.surpassun.cash.domain.Config;
import com.surpassun.cash.domain.Product;
import com.surpassun.cash.domain.User;
import com.surpassun.cash.repository.CategoryRepository;
import com.surpassun.cash.repository.ClientRepository;
import com.surpassun.cash.repository.ConfigRepository;
import com.surpassun.cash.repository.ProductRepository;
import com.surpassun.cash.repository.UserRepository;
import com.surpassun.cash.service.ConfigService;
import com.surpassun.cash.util.CashUtil;
import com.surpassun.cash.util.DraggableCell;
import com.surpassun.cash.util.ExcelExportUtil;
import com.surpassun.cash.util.ExcelImportUtil;
import com.surpassun.cash.util.LanguageUtil;
import com.surpassun.cash.util.StringPool;

@Component
public class ConfigController extends SimpleController {

	private final Logger log = LoggerFactory.getLogger(ConfigController.class);
	private static StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
	private static final DataFormat categoryFormat = new DataFormat("com.surpassun.cash.domain.Category");
	private static final DataFormat productFormat = new DataFormat("com.surpassun.cash.domain.Product");

	@Inject
	private UserRepository userRepository;
	@FXML
	TabPane configTabPane;

	/* User configuration */
	@FXML
	TextField username;
	@FXML
	PasswordField password;
	@FXML
	CheckBox userActive;
	@FXML
	ListView<User> userList;
	@FXML
	Label userExistsWarnInfo;

	/* Discount configuration */
	@Inject
	private ConfigRepository configRepository;
	@Inject
	ConfigService configService;
	@FXML
	CheckBox discountActive;
	@FXML
	CheckBox discountCouponForAll;
	@FXML
	TextField firstProductDiscount;
	@FXML
	TextField secondProductDiscount;
	@FXML
	TextField thirdProductDiscount;
	@FXML
	Label discountValueWarnInfo;
	@FXML
	Label saveDiscountSuccessInfo;

	/* Import configuration */
	@Inject
	private CategoryRepository categoryRepository;
	@Inject
	private ProductRepository productRepository;
	@Inject
	private ClientRepository clientRepository;
	@FXML
	Label importProductStatusInfo;
	@FXML
	Label importClientStatusInfo;

	private User selectedUser;
	private int selectedIndex;

	/* Shortcut prices configuration */
	@FXML
	private ListView<Category> categoryList;
	@FXML
	private ListView<Product> productList;
	@FXML
	private ListView<String> priceList;
	private List<Category> modifiedCategories;
	private List<Product> modifiedProducts;

	public void show(Stage stage) {
		super.show(this, stage, Constants.FXML_DESIGN_CONFIG);
		init();
	}

	private void init() {
		List<User> users = userRepository.findAll();
		userList.getItems().addAll(users);
		userList.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			@Override
			public ListCell<User> call(ListView<User> list) {
				return new UserListCell();
			}
		});

		userList.setOnMouseClicked((MouseEvent event) -> {
			selectedIndex = userList.getSelectionModel().getSelectedIndex();
			selectedUser = userList.getSelectionModel().getSelectedItem();
			if (selectedUser != null) {
				username.setText(selectedUser.getLogin());
				// password.setText(selectedUser.getPassword());
				userActive.setSelected(selectedUser.getActivated());
			}
		});

		// initShortcutPane();
	}

	@FXML
	public void initShortcutPane() {
		if (configTabPane.getSelectionModel().getSelectedIndex() == 2) {
			List<Category> categories = categoryRepository.findAll();

			if (categories != null && categories.size() > 0) {
				categoryList.getItems().addAll(categories);
				categoryList.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {
					@Override
					public ListCell<Category> call(ListView<Category> list) {
						return new DraggableCell(categoryFormat);
					}
				});
				// categoryList.getSelectionModel().selectedItemProperty().addListener(new
				// ChangeListener<Category>() {
				// @Override
				// public void changed(ObservableValue<? extends Category>
				// observable, Category oldValue, Category newValue) {
				// initProductList(newValue);
				// }
				// });
				categoryList.getSelectionModel().select(0);
				initProductList(categories.get(0));
			}
		}
	}

	private void initProductList(Category category) {
		productList.getItems().clear();
		priceList.getItems().clear();
		List<Product> products = productRepository.findByCategory(category);
		if (products != null && products.size() > 0) {
			productList.getItems().addAll(products);
			productList.setCellFactory(new Callback<ListView<Product>, ListCell<Product>>() {
				@Override
				public ListCell<Product> call(ListView<Product> list) {
					return new DraggableCell(productFormat);
				}
			});
			// productList.getSelectionModel().selectedItemProperty().addListener(new
			// ChangeListener<Product>() {
			// @Override
			// public void changed(ObservableValue<? extends Product>
			// observable, Product oldValue, Product newValue) {
			// initPriceList( newValue.getCode());
			// }
			// });
			productList.getSelectionModel().select(0);
			initPriceList(products.get(0).getCode());
		}
	}

	private void initPriceList(String productCode) {
		priceList.getItems().clear();
		String[] prices = configService.findListByName(Constants.SHORTCUT_PRICES + StringPool.COLON + productCode);
		String[] emptyPrices = new String[16 - prices.length];
		priceList.getItems().addAll(prices);
		priceList.getItems().addAll(emptyPrices);
		priceList.setCellFactory(TextFieldListCell.forListView());
	}

	@FXML
	public void addUser() {
		String creator = SecurityContextHolder.getContext().getAuthentication().getName();
		String login = username.getText();
		if (StringUtils.isBlank(login)) {
			log.warn("User's login cannot be empty");
			userExistsWarnInfo.setText(LanguageUtil.getMessage("ui.label.warn.user.login.empty"));
			CashUtil.makeFadeOutAnimation(5000, userExistsWarnInfo);
			return;
		}
		if (StringUtils.isBlank(password.getText())) {
			log.warn("User's password cannot be empty");
			userExistsWarnInfo.setText(LanguageUtil.getMessage("ui.label.warn.user.password.empty"));
			CashUtil.makeFadeOutAnimation(5000, userExistsWarnInfo);
			return;
		}
		if (userRepository.exists(login)) {
			log.warn("User {} already exists", login);
			userExistsWarnInfo.setText(LanguageUtil.getMessage("ui.label.warn.user.login.exists", login));
			CashUtil.makeFadeOutAnimation(5000, userExistsWarnInfo);
			return;
		}

		User user = new User(login, passwordEncoder.encode(password.getText()), userActive.isSelected(), creator, creator);
		// add user to the database
		userRepository.save(user);
		log.info("User {} added to database", login);
		userList.getItems().add(user);
		username.clear();
		password.clear();
		userActive.setSelected(false);
	}

	private static class UserListCell extends ListCell<User> {
		@Override
		protected void updateItem(User user, boolean empty) {
			if (user != null) {
				super.updateItem(user, empty);
				setText(user.getLogin());
				if (user.getActivated()) {
					setStyle("-fx-text-fill: #76C525;");
				} else {
					setStyle("-fx-text-fill: #FF241C;");
				}
			} else {
				setText(null);
			}
		}
	}

	@FXML
	public void modifyUser() {
		if (selectedUser != null && StringUtils.isNotBlank(username.getText())) {
			selectedUser.setLogin(username.getText());
			if (StringUtils.isNotBlank(password.getText())) {
				selectedUser.setPassword(passwordEncoder.encode(password.getText()));
			}
			selectedUser.setActivated(userActive.isSelected());
			userRepository.save(selectedUser);

			userList.getItems().set(selectedIndex, selectedUser);

			username.clear();
			password.clear();
			userActive.setSelected(false);
		}
	}

	@FXML
	public void deleteUser() {
		int selectedIndex = userList.getSelectionModel().getSelectedIndex();
		User selectedUser = userList.getSelectionModel().getSelectedItem();
		userList.getItems().remove(selectedIndex);
		userRepository.delete(selectedUser);
		log.info("User {} deleted from database", selectedUser.getLogin());
	}

	@FXML
	public void saveDiscountConfig() {
		boolean isDiscountActive = discountActive.isSelected();
		if (isDiscountActive) {
			if (!(NumberUtils.isNumber(firstProductDiscount.getText()) && NumberUtils.isNumber(secondProductDiscount.getText()) && NumberUtils.isNumber(thirdProductDiscount.getText()))) {
				log.warn("product discount value is not valid");
				discountValueWarnInfo.setText(LanguageUtil.getMessage("ui.label.warn.discount.value.invalid"));
				CashUtil.makeFadeOutAnimation(5000, discountValueWarnInfo);
				return;
			}
		}
		Config discountActiveConfig = configRepository.findByName(Constants.STRICK_REDUCTION_ACTIVE);
		if (discountActiveConfig == null) {
			discountActiveConfig = new Config(Constants.STRICK_REDUCTION_ACTIVE, Boolean.toString(isDiscountActive), null);
		} else {
			discountActiveConfig.setValue(Boolean.toString(isDiscountActive));
		}
		configRepository.save(discountActiveConfig);
		log.info("Discount active config value set to : {}", isDiscountActive);

		if (isDiscountActive) {
			float firstDiscount = Float.parseFloat(firstProductDiscount.getText());
			float secondDiscount = Float.parseFloat(secondProductDiscount.getText());
			float thirdDiscount = Float.parseFloat(thirdProductDiscount.getText());
			StringBuilder sb = new StringBuilder();
			sb.append(firstDiscount).append(StringPool.SEMICOLON).append(secondDiscount).append(StringPool.SEMICOLON).append(thirdDiscount);
			Config discountValueConfig = configRepository.findByName(Constants.STRICK_REDUCTION_VALUE);
			if (discountValueConfig == null) {
				discountValueConfig = new Config(Constants.STRICK_REDUCTION_VALUE, sb.toString(), null);
			} else {
				discountValueConfig.setValue(sb.toString());
			}
			configRepository.save(discountValueConfig);
			log.info("Discount config value set to : {}", sb.toString());
		}

		boolean isDiscountCouponForAll = discountCouponForAll.isSelected();
		Config discountCouponForAllConfig = configRepository.findByName(Constants.DISCOUNT_COUPON_FOR_ALL);
		if (discountCouponForAllConfig == null) {
			discountCouponForAllConfig = new Config(Constants.DISCOUNT_COUPON_FOR_ALL, Boolean.toString(isDiscountCouponForAll), null);
		}
		discountCouponForAllConfig.setValue(Boolean.toString(isDiscountCouponForAll));
		configRepository.save(discountCouponForAllConfig);
		log.info("Discount coupon is usable for all products value set to : {}", isDiscountCouponForAll);

		saveDiscountSuccessInfo.setText(LanguageUtil.getMessage("ui.label.success.save.discount.config"));
		CashUtil.makeFadeOutAnimation(5000, saveDiscountSuccessInfo);
	}

	@FXML
	public void chooseImportFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Choose file to import");
		File file = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());
		if (file != null) {
			log.info("file name {}", file.getAbsolutePath());
			InputStream is = null;
			HSSFWorkbook workbook = null;
			try {
				is = new FileInputStream(file);
				workbook = new HSSFWorkbook(is);
				if (workbook != null) {
					HSSFSheet sheet = workbook.getSheetAt(0);
					List<Integer> errorLines = new ArrayList<Integer>();
					Set<Product> products = ExcelImportUtil.workbookToProducts(sheet, categoryRepository, errorLines);
					if (errorLines.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (Integer errorLineNumber : errorLines) {
							sb.append(errorLineNumber).append(StringPool.COMMA_AND_SPACE);
						}
						boolean confirm = CashUtil.createConfirmPopup(LanguageUtil.getMessage("ui.title.common.confirmation"), LanguageUtil.getMessage("ui.popup.import.header"),
								LanguageUtil.getMessage("ui.popup.import.content", sb.toString()));
						if (confirm) {
							importProducts(products);
						}
					} else {
						importProducts(products);
					}
				}
			} catch (FileNotFoundException e) {
				log.error("Error finding file : {}", file.getAbsoluteFile());
			} catch (IOException e) {
				log.error("Error reading file {}", file.getAbsoluteFile());
			} catch (Exception e) {
				log.error("some other error happened while processing import", e);
			} finally {
				try {
					if (workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					log.error("Error close workbook");
				}
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					log.error("Error close input stream");
				}
			}
		}
	}

	private void importProducts(Set<Product> products) {
		if (products != null) {
			importProductStatusInfo.setText(LanguageUtil.getMessage("ui.label.info.import.starts"));
			CashUtil.makeFadeOutAnimation(9000, importProductStatusInfo);
			try {
				productRepository.save(products);
				configService.updateShortcutPrices(products);
				importProductStatusInfo.setText(LanguageUtil.getMessage("ui.label.info.import.ends.successfully"));
				CashUtil.makeFadeOutAnimation(5000, importProductStatusInfo);
			} catch (Exception e) {
				log.error("Error while saving products into database", e);
				importProductStatusInfo.setText(LanguageUtil.getMessage("ui.label.info.import.ends.error"));
				importProductStatusInfo.setStyle("-fx-text-fill: #FF241C;");
				CashUtil.makeFadeOutAnimation(5000, importProductStatusInfo);
			}
		}
	}

	@FXML
	public void chooseClientImportFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Choose file to import");
		File file = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());
		if (file != null) {
			log.info("file name {}", file.getAbsolutePath());
			InputStream is = null;
			HSSFWorkbook workbook = null;
			try {
				is = new FileInputStream(file);
				workbook = new HSSFWorkbook(is);
				if (workbook != null) {
					HSSFSheet sheet = workbook.getSheetAt(0);
					List<Integer> errorLines = new ArrayList<Integer>();
					Set<Client> clients = ExcelImportUtil.workbookToClients(sheet, errorLines);
					if (errorLines.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (Integer errorLineNumber : errorLines) {
							sb.append(errorLineNumber).append(StringPool.COMMA_AND_SPACE);
						}
						boolean confirm = CashUtil.createConfirmPopup(LanguageUtil.getMessage("ui.title.common.confirmation"),
								LanguageUtil.getMessage("ui.popup.import.header"), LanguageUtil.getMessage("ui.popup.import.content", sb.toString()));
						if (confirm) {
							importClients(clients);
						}
					} else {
						importClients(clients);
					}
				}
			} catch (FileNotFoundException e) {
				log.error("Error finding file : {}", file.getAbsoluteFile());
			} catch (IOException e) {
				log.error("Error reading file {}", file.getAbsoluteFile());
			} catch (Exception e) {
				log.error("some other error happened while processing import", e);
			} finally {
				try {
					if (workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					log.error("Error close workbook");
				}
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					log.error("Error close input stream");
				}
			}
		}
	}

	private void importClients(Set<Client> clients) {
		if (clients != null) {
			importClientStatusInfo.setText(LanguageUtil.getMessage("ui.label.info.import.starts"));
			CashUtil.makeFadeOutAnimation(9000, importClientStatusInfo);
			try {
				clientRepository.save(clients);
				importClientStatusInfo.setText(LanguageUtil.getMessage("ui.label.info.import.ends.successfully"));
				CashUtil.makeFadeOutAnimation(5000, importClientStatusInfo);
			} catch (Exception e) {
				log.error("Error while saving products into database", e);
				importClientStatusInfo.setText(LanguageUtil.getMessage("ui.label.info.import.ends.error"));
				importClientStatusInfo.setStyle("-fx-text-fill: #FF241C;");
				CashUtil.makeFadeOutAnimation(5000, importClientStatusInfo);
			}
		}
	}

	@FXML
	public void exportProductToFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Choose file to import");
		File file = fileChooser.showSaveDialog(((Button) event.getSource()).getScene().getWindow());
		if(file != null){
            ExcelExportUtil.exportProducts(productRepository, file);
        }
	}
	
	@FXML
	public void exportClientToFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Choose file to import");
		File file = fileChooser.showSaveDialog(((Button) event.getSource()).getScene().getWindow());
		if(file != null){
			ExcelExportUtil.exportClients(clientRepository, file);
		}
	}

	/********* Configure shortcut prices ********/
	@FXML
	public void activateCategory(ActionEvent event) {
		int selectedIndex = categoryList.getSelectionModel().getSelectedIndex();
		Category category = categoryList.getSelectionModel().getSelectedItem();
		category.setShortcutButtonEnabled(!category.isShortcutButtonEnabled());
		
		modifiedCategories.add(category);
		//categoryRepository.save(category);

		categoryList.getItems().set(selectedIndex, category);
	}

	@FXML
	public void activateProduct(ActionEvent event) {
		int selectedIndex = productList.getSelectionModel().getSelectedIndex();
		Product product = productList.getSelectionModel().getSelectedItem();
		product.setShortcutButtonEnabled(!product.isShortcutButtonEnabled());
		
		modifiedProducts.add(product);
		//productRepository.save(product);

		productList.getItems().set(selectedIndex, product);
	}

	@FXML
	public void updateShortcutPrice(EditEvent<String> event) {
		if (NumberUtils.isNumber(event.getNewValue())) {
			priceList.getItems().set(event.getIndex(), event.getNewValue());
		}
	}

	@FXML
	public void refreshProductList(Event event) {
		Category category = categoryList.getSelectionModel().getSelectedItem();
		if (category != null) {
			initProductList(category);
		} else {
			productList.getItems().clear();
		}
	}

	@FXML
	public void refreshPriceList(Event event) {
		Product product = productList.getSelectionModel().getSelectedItem();
		if (product != null) {
			initPriceList(product.getCode());
		} else {
			priceList.getItems().clear();
		}
	}

	@FXML
	public void saveShortcutPrices(ActionEvent event) {
		Product product = productList.getSelectionModel().getSelectedItem();
		Config confExisted = configRepository.findByName(Constants.SHORTCUT_PRICES + StringPool.COLON + product.getCode());
		String newValue = StringUtils.join(priceList.getItems(), StringPool.SEMICOLON);
		confExisted.setValue(newValue);
		configRepository.save(confExisted);
	}
	
	@FXML
	public void saveCategories(ActionEvent event) {
		//save configuration order
		int totalCategories = categoryList.getItems().size();
		List<Category> categories = new ArrayList<Category>(totalCategories);
		//save configuration order
		for (int i = 0; i < totalCategories; i++) {
			Category category = categoryList.getItems().get(i);
			category.setDisplayOrder(i);
			categories.add(category);
		}
		categoryRepository.save(categories);
	}
	
	@FXML
	public void saveProducts(ActionEvent event) {
		int totalProducts = productList.getItems().size();
		List<Product> products = new ArrayList<Product>(totalProducts);
		//save configuration order
		for (int i = 0; i < totalProducts; i++) {
			Product product = productList.getItems().get(i);
			product.setDisplayOrder(i);
			products.add(product);
		}
		productRepository.save(products);
	}
}
