package com.surpassun.cash.fx.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;

import org.springframework.core.io.Resource;

public class FXMLDialog extends Stage {

	public FXMLDialog(DialogController controller, Resource fxml, Window owner) {
		this(controller, fxml, owner, StageStyle.DECORATED);
	}

	public FXMLDialog(final DialogController controller, Resource fxml, Window owner, StageStyle style) {
		super(style);
		initOwner(owner);
		initModality(Modality.WINDOW_MODAL);
		try {
			FXMLLoader loader = new FXMLLoader(fxml.getURL());
			loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> aClass) {
					return controller;
				}
			});
			controller.setDialog(this);
			setScene(new Scene((Parent) loader.load()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
