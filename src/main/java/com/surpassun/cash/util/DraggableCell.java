package com.surpassun.cash.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

public class DraggableCell<T> extends ListCell<T> {
	
	private final Logger log = LoggerFactory.getLogger(DraggableCell.class);
	
	public DraggableCell(DataFormat dataFormat) {
        ListCell<T> thisCell = this;

        setContentDisplay(ContentDisplay.TEXT_ONLY);
        setAlignment(Pos.CENTER_LEFT);

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(dataFormat, getItem());
            content.putString(getItem().toString());
            dragboard.setContent(content);
            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell &&
                   event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<T> items = getListView().getItems();
                int sourceIdx = items.indexOf(db.getContent(dataFormat));
                T source = items.get(sourceIdx);
                int targetIdx = items.indexOf(getItem());
                
                if (sourceIdx < targetIdx) {
                	for (int i = sourceIdx; i < targetIdx; i++) {
                		items.set(i, items.get(i+1));
                	}
                } else {
                	for (int i = sourceIdx; i > targetIdx; i--) {
                		items.set(i, items.get(i-1));
                	}
                }
                items.set(targetIdx, source);
                List<T> itemscopy = new ArrayList<>(items);
                getListView().getItems().setAll(itemscopy);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }

    @Override
	protected void updateItem(T obj, boolean empty) {
		if (obj != null) {
			super.updateItem(obj, empty);
			setText(obj.toString());
			Method method = ReflectionUtils.findMethod(obj.getClass(), "isShortcutButtonEnabled");
			if (method != null) {
				try {
					Boolean result = (Boolean)method.invoke(obj, null);
					if (result) {
						setStyle("-fx-text-fill: #000000;");
					} else {
						setStyle("-fx-text-fill: #FF241C;");
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Error while invoking method 'isShortcutButtonEnabled'", e);
				}
			}
		} else {
			setText(null);
		}
	}
}
