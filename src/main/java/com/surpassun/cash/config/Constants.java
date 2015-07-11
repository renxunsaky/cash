package com.surpassun.cash.config;

/**
 * Application constants.
 */
public interface Constants {

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SYSTEM_ACCOUNT = "system";
    
    public static final String FXML_DESIGN_PREFIX = "design/";
    public static final String FXML_DESIGN_SUFIX = ".fxml";
    
    public static final String FXML_DESIGN_LOGIN = FXML_DESIGN_PREFIX + "Login" + FXML_DESIGN_SUFIX;
	public static final String FXML_DESIGN_MAIN = FXML_DESIGN_PREFIX + "Main" + FXML_DESIGN_SUFIX;
	public static final String FXML_DESIGN_CHECKOUT = FXML_DESIGN_PREFIX + "checkout/Checkout" + FXML_DESIGN_SUFIX;
	public static final String LANG_BASE_NAME = "i18n/Language";
	public static final String TERMINAL_ID = "TERMINAL_ID";
	public static final String BARCODE_PREFIX_MEMBER_CARD = "99";
	public static final String BARCODE_PREFIX_GIFT_CARD = "98";
	public static final String PAYMENT_MODE_CASH = "paymentCash";
	public static final String PAYMENT_MODE_BANK_CARD = "paymentBankCard";
	public static final String PAYMENT_MODE_GIFT_CARD = "paymentGiftCard";
	public static final String SHORTCUT_PRICES = "SHORTCUT_PRICES";
	public static final String CLICKED = "clicked";

}
