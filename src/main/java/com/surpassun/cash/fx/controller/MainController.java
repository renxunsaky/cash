package com.surpassun.cash.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;

@Component
public class MainController extends SimpleController {

	@FXML
	Label username;
	
	public void show(Stage stage) {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user != null) {
			super.show(this, stage, Constants.FXML_DESIGN_MAIN);
			username.setText(user.getUsername());
			username.setTextAlignment(TextAlignment.RIGHT);
		}
	}
	
	@FXML
	public void showCheckout() {
		
	}
	
	@FXML
	public void showReporting() {
		
	}
	
	@FXML
	public void showManageClients() {
		
	}
	
	@FXML
	public void showConfiguration() {
		
	}
	
	@FXML
	public void showManageProducts() {
		
	}
	
	@FXML
	public void showManageTransactions() {
		
	}
}
