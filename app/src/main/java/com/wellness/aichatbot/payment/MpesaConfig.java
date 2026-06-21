package com.wellness.aichatbot.payment;

/**
 * M-Pesa Daraja API Configuration
 * Using provided Sandbox/Production credentials.
 */
public final class MpesaConfig {
    public static final String CUSTOMER_KEY = "PyjouYdJgWjSHI9AGlgAMBxIegWHXmpYULZABBtb6KxdkG6u";
    public static final String CUSTOMER_SECRET = "irNu8WFUX8yzum4J623G2floTm4rWSnVfeBPW77dHGrne3WaKITlfwBKChL56mPn";
    public static final String BUSINESS_SHORTCODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CALLBACK_URL = "https://mydomain.com/mpesa-express-simulate/";

    private MpesaConfig() {
    }
}
