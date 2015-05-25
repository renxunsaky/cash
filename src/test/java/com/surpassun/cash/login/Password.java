package com.surpassun.cash.login;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class Password {

	public static void main(String[] args) {
		PasswordEncoder encoder = new StandardPasswordEncoder();
		System.out.println(encoder.encode("test"));
		System.out.println(encoder.encode("admin"));
	}
}
