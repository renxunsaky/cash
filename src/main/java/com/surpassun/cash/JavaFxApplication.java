package com.surpassun.cash;

import javafx.application.Application;
import javafx.stage.Stage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JavaFxApplication extends Application {
	
	private ConfigurableApplicationContext applicationContext;
	private static String[] args;

	@Override
	public void init() throws Exception {
		super.init();
		applicationContext = SpringApplication.run(getClass(), JavaFxApplication.args);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
	}
	
	@Override
	public void start(Stage arg0) throws Exception {
		
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		applicationContext.close();
	}
	
	public static void main(String[] args) {
		JavaFxApplication.args = args;
		Application.launch(JavaFxApplication.class, args);
	}

}
