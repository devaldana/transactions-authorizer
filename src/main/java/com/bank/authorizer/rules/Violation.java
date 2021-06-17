package com.bank.authorizer.rules;

public enum Violation {

    ACCOUNT_ALREADY_INITIALIZED("account-already-initialized"),
    UNINITIALIZED_ACCOUNT("account-not-initialized"),
    INACTIVE_CARD("card-not-active"),
    INSUFFICIENT_FUNDS("insufficient-limit"),
    TOO_MANY_TRANSACTIONS("high-frequency-small-interval"),
    DUPLICATE_TRANSACTION("doubled-transaction"),
    INVALID_INITIAL_BALANCE("invalid-initial-balance"),
    INVALID_TRANSACTION_AMOUNT("invalid-transaction-amount");

    private String description;

    Violation(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
