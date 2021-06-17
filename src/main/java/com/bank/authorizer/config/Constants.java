package com.bank.authorizer.config;

public final class Constants {

    // As a utility class, no instances are required
    private Constants() {}

    // Next units must be in milliseconds
    public static final long NO_DELAY = 0;
    public static final long TIMER_FREQUENCY = 1000;

    // Frequency must be set in seconds
    public static final long HIGH_FREQUENCY_INTERVAL = 120;
    public static final String ACCOUNT_MEMBER = "account";
    public static final String TRANSACTION_MEMBER = "transaction";
    public static final String ACTIVE_CARD_MEMBER = "active-card";
    public static final String AVAILABLE_LIMIT_MEMBER = "available-limit";
    public static final String MERCHANT_MEMBER = "merchant";
    public static final String AMOUNT_MEMBER = "amount";
    public static final String TIME_MEMBER = "time";
    public static final int MAX_TRANSACTIONS_IN_HIGH_FREQUENCY_INTERVAL = 3;
    public static final int FIRST_TRANSACTION = 0;
    public static final String INPUT_ACTION_TEXT = "Register the operation: ";
}
