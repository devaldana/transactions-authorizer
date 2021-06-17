package com.bank.authorizer.rules.business;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;
import com.bank.authorizer.transaction.Transaction;

import static com.bank.authorizer.rules.Violation.INSUFFICIENT_FUNDS;

public final class InsufficientFundsRule implements Rule<EvaluationModel> {

    @Override
    public boolean evaluate(final EvaluationModel evaluationModel) {
        final Account account = evaluationModel.getAccount();
        final Transaction transaction = evaluationModel.getTransaction();
        final boolean isAccountAlreadyInitialized = new AccountAlreadyInitializedRule().evaluate(evaluationModel);
        final boolean isValidTransactionAmount = new ValidTransactionAmountRule().evaluate(evaluationModel);
        final boolean isNegativeBalanceAfterTransaction = isAccountAlreadyInitialized && (account.getBalance() - transaction.getAmount()) < 0;
        return isAccountAlreadyInitialized && isValidTransactionAmount && isNegativeBalanceAfterTransaction;
    }

    @Override
    public Violation getViolation() {
        return INSUFFICIENT_FUNDS;
    }
}
