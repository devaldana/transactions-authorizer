package com.bank.authorizer;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.transaction.Payment;
import com.bank.authorizer.transaction.Transaction;

import java.time.LocalDateTime;

public class TestsHelper {
    public static Account createAccount(long initialBalance) {
        Account account = new Account();
        account.credit(initialBalance);
        return account;
    }

    public static Transaction createPayment(String merchant, long amount) {
        Payment payment = new Payment();
        payment.setMerchant(merchant);
        payment.setAmount(amount);
        payment.setDate(LocalDateTime.now());
        return payment;
    }
}
