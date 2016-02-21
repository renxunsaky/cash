package com.surpassun.cash.config;

import javafx.stage.Stage;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;

import com.surpassun.cash.fx.controller.CheckoutController;
import com.surpassun.cash.fx.controller.ConfigController;
import com.surpassun.cash.fx.controller.LoginController;
import com.surpassun.cash.fx.controller.MainController;
import com.surpassun.cash.fx.controller.ProductController;

@Configuration
public class ScreenManager {
	
    @Inject
    private LoginController loginController;
    @Inject
    private MainController mainController;
    @Inject
    private CheckoutController checkoutController;
    @Inject
    private ConfigController configController;
    @Inject
    private ProductController productController;
    
    private Stage stage;
    
    public void setPrimaryStage(Stage stage) {
    	this.stage = stage;
    }

	public void showLogin() {
		loginController.show(stage);
	}

	public void showMainScreen() {
		mainController.show(stage);
	}

	public void showCheckout() {
		checkoutController.show(stage);
	}
	
	public void showConfiguration() {
		configController.show(stage);
	}
	
	public void showProducts() {
		productController.show(stage);
	}
}
