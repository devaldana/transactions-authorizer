package com.bank.authorizer.operation;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.transaction.Transaction;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Optional;

import static com.bank.authorizer.config.Constants.ACCOUNT_MEMBER;
import static com.bank.authorizer.config.Constants.TRANSACTION_MEMBER;
import static com.bank.authorizer.operation.OperationConverter.convertJsonObjectToAccount;
import static com.bank.authorizer.operation.OperationConverter.convertJsonObjectToTransaction;

public final class Operation {

    private final Type type;
    private final JsonObject object;

    private Operation(final JsonObject object) {
        this.object = object;
        this.type = getType(this.object);
    }

    public static Operation parseString(final String json) {
        final JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        return new Operation(object);
    }

    public Optional<Account> getAccount() {
        if (isAccountCreation()) {
            final JsonObject jsonObject = getObjectMember(ACCOUNT_MEMBER);
            final Account account = convertJsonObjectToAccount(jsonObject);
            return Optional.of(account);
        }
        return Optional.empty();
    }

    public Optional<Transaction> getTransaction() {
        if (isTransaction()) {
            final JsonObject jsonObject = getObjectMember(TRANSACTION_MEMBER);
            final Transaction transaction = convertJsonObjectToTransaction(jsonObject);
            return Optional.of(transaction);
        }
        return Optional.empty();
    }

    /* ************************************************************************************
     * Some auxiliary stuff
     * ************************************************************************************/

    private Type getType(final JsonObject object) {
        if (object.has(ACCOUNT_MEMBER)) {
            return Type.ACCOUNT_CREATION;
        } else if (object.has(TRANSACTION_MEMBER)) {
            return Type.TRANSACTION;
        } else {
            throw new NotSupportedOperation();
        }
    }

    public boolean isAccountCreation() {
        return Type.ACCOUNT_CREATION.equals(type);
    }

    public boolean isTransaction() {
        return Type.TRANSACTION.equals(type);
    }

    private JsonObject getObjectMember(final String memberName) {
        return object.getAsJsonObject(memberName);
    }

    public enum Type {
        TRANSACTION,
        ACCOUNT_CREATION
    }
}
