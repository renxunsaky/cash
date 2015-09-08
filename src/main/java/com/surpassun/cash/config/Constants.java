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
	public static final String FXML_DESIGN_CONFIG = FXML_DESIGN_PREFIX + "configuration/configuration" + FXML_DESIGN_SUFIX;
	public static final String LANG_BASE_NAME = "i18n/Language";
	public static final String TERMINAL_ID = "TERMINAL_ID";
	public static final String BARCODE_PREFIX_MEMBER_CARD = "88168";
	public static final String BARCODE_PREFIX_GIFT_CARD = "1688";
	public static final String PAYMENT_MODE_CASH = "paymentCash";
	public static final String PAYMENT_MODE_BANK_CARD = "paymentBankCard";
	public static final String PAYMENT_MODE_GIFT_CARD = "paymentGiftCard";
	public static final String SHORTCUT_PRODUCTS = "SHORTCUT_PRODUCTS";
	public static final String SHORTCUT_PRICES = "SHORTCUT_PRICES";
	public static final String CLICKED = "clicked";
	public static final String STRICK_REDUCTION_ACTIVE = "STRICK_REDUCTION_ACTIVE";
	public static final String STRICK_REDUCTION_VALUE = "STRICK_REDUCTION_VALUE";
	public static final String LOCALE = "LOCALE";
	public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
	public static final String OPERATION_TYPE_DECREASE_QUANTITY = "DECREASE";
	public static final String OPERATION_TYPE_DELETE = "DELETE";

}
