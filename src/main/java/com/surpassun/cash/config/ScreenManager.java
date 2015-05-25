package com.surpassun.cash.config;

import javafx.stage.Stage;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;

import com.surpassun.cash.fx.controller.LoginController;
import com.surpassun.cash.fx.controller.MainController;

@Configuration
public class ScreenManager {
	
    @Inject
    private LoginController loginController;
    @Inject
    private MainController mainController;
    
    private Stage stage;

	public void showLogin() {
		loginController.show(stage);
	}

	public void showMainScreen() {
		mainController.show(stage);
	}

	public void setPrimaryStage(Stage stage) {
		this.stage = stage;
	}
}
