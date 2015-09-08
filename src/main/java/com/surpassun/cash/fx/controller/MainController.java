package com.surpassun.cash.fx.controller;

import java.util.Locale;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.service.ConfigService;
import com.surpassun.cash.util.CacheUtil;

@Component
public class MainController extends SimpleController {
	
	private final Logger log = LoggerFactory.getLogger(MainController.class);
	@Inject
	private ConfigService configService;

	@FXML
	Label username;
	
	public void show(Stage stage) {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user != null) {
			super.show(this, stage, Constants.FXML_DESIGN_MAIN);
			username.setText(user.getUsername());
			username.setTextAlignment(TextAlignment.RIGHT);
		}
		init();
	}
	
	private void init() {
		Locale locale = new Locale(configService.findByName(Constants.LOCALE));
		CacheUtil.putCache(Constants.LOCALE, locale);
	}

	@FXML
	public void showCheckout() {
		log.debug("showCheckout clicked");
		screenManager.showCheckout();
	}
	
	@FXML
	public void showReporting() {
		log.debug("showReporting clicked");
	}
	
	@FXML
	public void showManageClients() {
		log.debug("showManageClients clicked");
	}
	
	@FXML
	public void showConfiguration() {
		log.debug("showConfiguration clicked");
		screenManager.showConfiguration();
	}
	
	@FXML
	public void showManageProducts() {
		log.debug("showManageProducts clicked");
	}
	
	@FXML
	public void showManageTransactions() {
		log.debug("showManageTransactions clicked");
	}
}
