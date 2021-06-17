package com.bank.authorizer.rules.business;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;

import static com.bank.authorizer.rules.Violation.UNINITIALIZED_ACCOUNT;
import static java.util.Objects.isNull;

public final class AccountNotInitializedRule implements Rule<EvaluationModel> {

    @Override
    public boolean evaluate(final EvaluationModel evaluationModel) {
        final Account account = evaluationModel.getAccount();
        return isNull(account);
    }

    @Override
    public Violation getViolation() {
        return UNINITIALIZED_ACCOUNT;
    }
}
