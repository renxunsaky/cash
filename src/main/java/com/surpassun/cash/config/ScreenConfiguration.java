package com.surpassun.cash.config;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

import com.surpassun.cash.fx.controller.FXMLDialog;
import com.surpassun.cash.fx.controller.LoginController;

@Configuration
@Lazy
public class ScreenConfiguration {

    private Stage primaryStage;
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 777, 500));
        primaryStage.show();
    }
    
    @Bean
    @Scope("prototype")
    public FXMLDialog loginDialog() {
    	return new FXMLDialog(loginController(), new ClassPathResource("design/Login.fxml"), primaryStage, StageStyle.UNDECORATED);
    }

    @Bean
    @Scope("prototype")
    public LoginController loginController() {
        return new LoginController(this);
    }
}
