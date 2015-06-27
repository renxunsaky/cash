package com.surpassun.cash.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

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

}
