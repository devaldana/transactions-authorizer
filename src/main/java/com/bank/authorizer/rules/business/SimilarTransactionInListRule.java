package com.bank.authorizer.rules.business;

import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;
import com.bank.authorizer.transaction.Transaction;

import java.util.List;

import static com.bank.authorizer.rules.Violation.DUPLICATE_TRANSACTION;

public final class SimilarTransactionInListRule implements Rule<EvaluationModel> {

    @Override
    public boolean evaluate(final EvaluationModel evaluationModel) {
        final Transaction transaction = evaluationModel.getTransaction();
        final List<Transaction> transactions = evaluationModel.getTransactions();
        return transactions.stream().anyMatch(transaction::isSimilar);
    }

    @Override
    public Violation getViolation() {
        return DUPLICATE_TRANSACTION;
    }
}
