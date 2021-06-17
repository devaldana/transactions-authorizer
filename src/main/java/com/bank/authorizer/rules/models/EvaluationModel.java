package com.bank.authorizer.rules.models;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class EvaluationModel {
    
    private Account account;
    private Transaction transaction;
    private List<Transaction> transactions;

    public EvaluationModel() {}

    public EvaluationModel(final Account account) {
        this.account = account;
    }

    public EvaluationModel(final Transaction transaction) {
        this.transaction = transaction;
    }
}
