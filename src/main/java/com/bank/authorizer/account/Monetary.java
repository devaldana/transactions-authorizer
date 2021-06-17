package com.bank.authorizer.account;

import com.bank.authorizer.transaction.Identifiable;
import com.bank.authorizer.transaction.Transaction;

public interface Monetary<T> extends Identifiable<T> {
    void saveTransaction(Transaction transaction);
    void credit(long amount);
    void debit(long amount);
    long getBalance();
}
