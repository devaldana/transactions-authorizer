package com.bank.authorizer.operation;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.transaction.Payment;
import com.bank.authorizer.transaction.Transaction;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;

import static com.bank.authorizer.account.Account.Status.ACTIVE;
import static com.bank.authorizer.account.Account.Status.INACTIVE;
import static com.bank.authorizer.config.Constants.*;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

final class OperationConverter {

    // As a utility class, no instances are required
    private OperationConverter() {}

    static Account convertJsonObjectToAccount(final JsonObject object) {
        final Account account = new Account();
        final boolean activeCard = object.getAsJsonPrimitive(ACTIVE_CARD_MEMBER).getAsBoolean();
        final long availableLimit = object.getAsJsonPrimitive(AVAILABLE_LIMIT_MEMBER).getAsLong();
        account.credit(availableLimit);
        account.setStatus(activeCard ? ACTIVE : INACTIVE);
        return account;
    }

    static Transaction convertJsonObjectToTransaction(final JsonObject object) {
        final Payment payment = new Payment();
        final String merchant = object.getAsJsonPrimitive(MERCHANT_MEMBER).getAsString();
        final long amount = object.getAsJsonPrimitive(AMOUNT_MEMBER).getAsLong();
        final String time = object.getAsJsonPrimitive(TIME_MEMBER).getAsString();
        payment.setMerchant(merchant);
        payment.setAmount(amount);
        payment.setDate(LocalDateTime.parse(time, ISO_OFFSET_DATE_TIME));
        return payment;
    }
}