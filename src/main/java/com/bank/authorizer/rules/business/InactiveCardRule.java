package com.bank.authorizer.rules.business;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;

import static com.bank.authorizer.account.Account.Status.INACTIVE;
import static com.bank.authorizer.rules.Violation.INACTIVE_CARD;

public final class InactiveCardRule implements Rule<EvaluationModel> {

    @Override
    public boolean evaluate(EvaluationModel evaluationModel) {
        final Account account = evaluationModel.getAccount();
        final boolean isAccountAlreadyInitialized = new AccountAlreadyInitializedRule().evaluate(evaluationModel);
        return isAccountAlreadyInitialized && INACTIVE.equals(account.getStatus());
    }

    @Override
    public Violation getViolation() {
        return INACTIVE_CARD;
    }
}
