package com.surpassun.cash.fx.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.config.ScreenManager;

@Component
public class SimpleController implements FxController {
	
	private final Logger log = LoggerFactory.getLogger(SimpleController.class);
	
	@Inject
	protected ScreenManager screenManager;
	
	@Value("${locale.language}")
	private String language;

	@Value("${locale.country}")
	private String country;

	@Override
	public void show(FxController controller, Stage primaryStage, String fxml) {
		FXMLLoader loader;
		try {
			loader = new FXMLLoader(new ClassPathResource(fxml).getURL());
			loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> arg0) {
					return controller;
				}
			});
			loader.setResources(ResourceBundle.getBundle(
					Constants.LANG_BASE_NAME, new Locale(language, country)));
			Scene scene = new Scene((Parent) loader.load());
			primaryStage.setScene(scene);
		} catch (IOException e) {
			log.error("Error while show scene : {}", fxml, e);
		}
	}
	
	@FXML
	public void showMainScreen() {
		screenManager.showMainScreen();
	}
}
