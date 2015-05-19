package com.surpassun.cash;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.config.ScreenConfiguration;

@SpringBootApplication
public class JavaFxApplication extends Application {
	
	private ConfigurableApplicationContext applicationContext;
	private static String[] args;

	@Value("${app.ui.title:Example App}")
	private String windowTitle;
	
	@Inject
	ScreenConfiguration screenConfiguration;
	
	@Override
	public void init() throws Exception {
		super.init();
		
		SpringApplication app = new SpringApplication(getClass());
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);

        // Check if the selected profile has been set as argument.
        // if not the development profile will be added
        addDefaultProfile(app, source);
        
		applicationContext = app.run(JavaFxApplication.args);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
	}
	
	/**
     * Set a default profile if it has not been set
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active")) {
            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle(windowTitle);
		stage.setResizable(true);
		stage.setFullScreen(true);
		stage.centerOnScreen();
		screenConfiguration.setPrimaryStage(stage);
		stage.setScene(screenConfiguration.loginDialog().getScene());
		stage.show();
//		screenConfiguration.loginDialog().show();
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
