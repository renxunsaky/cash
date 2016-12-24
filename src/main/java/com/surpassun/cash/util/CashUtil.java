package com.surpassun.cash.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CashUtil.class);
	
	public static String getCurrentMacAddress() {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			
			byte[] mac = network.getHardwareAddress();
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			String result = sb.toString();
			log.debug("Current mac address : {}", result);
			
			return result;
		} catch (UnknownHostException | SocketException e) {
			log.error("Error while getting mac address", e);
			return null;
		}
	}
	
	public static void makeFadeOutAnimation(int duration, Node target) {
		FadeTransition ft = new FadeTransition(Duration.millis(duration), target);
		ft.setFromValue(1.0);
		ft.setToValue(0);
		ft.play();
	}
	
	public static void makeFadeInAnimation(int duration, Node target) {
		FadeTransition ft = new FadeTransition(Duration.millis(duration), target);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
	}
	
	/**
	 * create a popup dialog and wait for user's choice
	 * 
	 * @param title
	 * @param headerText
	 * @param contentText
	 * @return true if the user hit "OK", else return false
	 */
	public static boolean createConfirmPopup(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	public static String createInputPopup(String title, String headerText, String contentText) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		return result.isPresent() ? result.get() : null;
	}
	
	public static void createWarningPopup(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.show();
	}
}
