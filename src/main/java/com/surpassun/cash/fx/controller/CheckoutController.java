package com.surpassun.cash.fx.controller;

import javafx.stage.Stage;

import com.surpassun.cash.config.Constants;

public class CheckoutController extends SimpleController {

	public void show(Stage stage) {
		super.show(this, stage, Constants.FXML_DESIGN_CHECKOUT);
	}
}
