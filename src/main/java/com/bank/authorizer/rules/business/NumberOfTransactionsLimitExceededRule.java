package com.bank.authorizer.rules.business;

import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;
import com.bank.authorizer.transaction.Transaction;

import java.util.List;

import static com.bank.authorizer.config.Constants.MAX_TRANSACTIONS_IN_HIGH_FREQUENCY_INTERVAL;
import static com.bank.authorizer.rules.Violation.TOO_MANY_TRANSACTIONS;

public final class NumberOfTransactionsLimitExceededRule implements Rule<EvaluationModel> {

    @Override
    public boolean evaluate(final EvaluationModel evaluationModel) {
        final List<Transaction> transactions = evaluationModel.getTransactions();
        return transactions.size() >= MAX_TRANSACTIONS_IN_HIGH_FREQUENCY_INTERVAL;
    }

    @Override
    public Violation getViolation() {
        return TOO_MANY_TRANSACTIONS;
    }
}
