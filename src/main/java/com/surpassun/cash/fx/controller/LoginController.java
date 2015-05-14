package com.surpassun.cash.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.surpassun.cash.config.ScreensConfiguration;

public class LoginController implements DialogController {

	@Autowired
	private AuthenticationManager authenticationManager;
	private ScreensConfiguration screens;
	private FXMLDialog dialog;

	@Override
	public void setDialog(FXMLDialog dialog) {
		this.dialog = dialog;
	}

	public LoginController(ScreensConfiguration screens) {
		this.screens = screens;
	}

	@FXML
	Label header;
	@FXML
	TextField username;
	@FXML
	TextField password;

	@FXML
	public void login() {
		Authentication authToken = new UsernamePasswordAuthenticationToken(
				username.getText(), password.getText());
		try {
			authToken = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authToken);
		} catch (AuthenticationException e) {
			header.setText("Login failure, please try again:");
			header.setTextFill(Color.DARKRED);
			return;
		}
		dialog.close();
		//TODO : show next screen
		//screens.showScreen(screens.customerDataScreen());
	}
}
