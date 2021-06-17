package com.bank.authorizer.rules.business;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;

import static com.bank.authorizer.rules.Violation.ACCOUNT_ALREADY_INITIALIZED;
import static java.util.Objects.nonNull;

public final class AccountAlreadyInitializedRule implements Rule<EvaluationModel> {

    @Override
    public boolean evaluate(final EvaluationModel evaluationModel) {
        final Account account = evaluationModel.getAccount();
        return nonNull(account);
    }

    @Override
    public Violation getViolation() {
        return ACCOUNT_ALREADY_INITIALIZED;
    }
}
