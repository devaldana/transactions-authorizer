package com.bank.authorizer.rules.business;

import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;
import com.bank.authorizer.transaction.Transaction;

import static com.bank.authorizer.rules.Violation.INVALID_TRANSACTION_AMOUNT;

public final class InvalidTransactionAmountRule implements Rule<EvaluationModel> {

    @Override
    public boolean evaluate(final EvaluationModel evaluationModel) {
        final Transaction transaction = evaluationModel.getTransaction();
        return transaction.getAmount() <= 0;
    }

    @Override
    public Violation getViolation() {
        return INVALID_TRANSACTION_AMOUNT;
    }
}
