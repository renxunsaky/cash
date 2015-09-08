package com.surpassun.cash.fx.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.domain.Config;
import com.surpassun.cash.domain.User;
import com.surpassun.cash.repository.ConfigRepository;
import com.surpassun.cash.repository.UserRepository;
import com.surpassun.cash.util.CashUtil;
import com.surpassun.cash.util.LanguageUtil;
import com.surpassun.cash.util.StringPool;

@Component
public class ConfigController extends SimpleController {
	
	private final Logger log = LoggerFactory.getLogger(ConfigController.class);
	private static StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
	
	@Inject
	private UserRepository userRepository;

	/*User configuration*/
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
	
	/*Discount configuration*/
	@Inject
	private ConfigRepository configRepository;
	@FXML
	CheckBox discountActive;
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
	
	private User selectedUser;
	private int selectedIndex;
	
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
				//password.setText(selectedUser.getPassword());
				userActive.setSelected(selectedUser.getActivated());
			}
		});
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
		//add user to the database
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
			if (!(NumberUtils.isNumber(firstProductDiscount.getText()) 
					&& NumberUtils.isNumber(secondProductDiscount.getText())
					&& NumberUtils.isNumber(thirdProductDiscount.getText()))) {
						log.warn("product discount value is not valid");
						discountValueWarnInfo.setText(LanguageUtil.getMessage("ui.label.warn.discount.value.invalid"));
						CashUtil.makeFadeOutAnimation(5000, discountValueWarnInfo);
						return;
					}
		}
		float firstDiscount = Float.parseFloat(firstProductDiscount.getText());
		float secondDiscount = Float.parseFloat(secondProductDiscount.getText());
		float thirdDiscount = Float.parseFloat(thirdProductDiscount.getText());
		Config discountActiveConfig = configRepository.findByName(Constants.STRICK_REDUCTION_ACTIVE);
		if (discountActiveConfig == null) {
			discountActiveConfig = new Config(Constants.STRICK_REDUCTION_ACTIVE, Boolean.toString(isDiscountActive), null);
		} else {
			discountActiveConfig.setValue(Boolean.toString(isDiscountActive));
		}
		configRepository.save(discountActiveConfig);
		log.info("Discount active config value set to : {}", isDiscountActive);
		
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
		
		saveDiscountSuccessInfo.setText(LanguageUtil.getMessage("ui.label.success.save.discount.config"));
		CashUtil.makeFadeOutAnimation(5000, saveDiscountSuccessInfo);
	}
}
