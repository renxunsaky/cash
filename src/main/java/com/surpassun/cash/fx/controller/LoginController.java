package com.surpassun.cash.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;

@Component
public class LoginController extends SimpleController {
	
	@Inject
	private AuthenticationManager authenticationManager;
	
	@FXML
	TextField username;
	@FXML
	TextField password;
	
	public void show(Stage stage) {
		super.show(this, stage, Constants.FXML_DESIGN_LOGIN);
	}

	@FXML
	public void login() {
		try {
			Authentication auth = new UsernamePasswordAuthenticationToken(username.getText(), password.getText());
			SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
		} catch (AuthenticationException e) {
			return;
		}
		
		screenManager.showMainScreen();
	}
}
