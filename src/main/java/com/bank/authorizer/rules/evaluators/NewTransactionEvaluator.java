package com.bank.authorizer.rules.evaluators;

import com.bank.authorizer.rules.Evaluator;
import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.business.*;
import com.bank.authorizer.rules.models.EvaluationModel;

import java.util.Arrays;
import java.util.List;

public final class NewTransactionEvaluator extends Evaluator<EvaluationModel> {

    private static final List<Rule<EvaluationModel>> rules;

    static {
        rules = Arrays.asList(
                // List here the rules for a new transaction
                // The order here will be maintained in the attribute 'violations' of Result
                new AccountNotInitializedRule(),
                new InactiveCardRule(),
                new InvalidTransactionAmountRule(),
                new InsufficientFundsRule(),
                new NumberOfTransactionsLimitExceededRule(),
                new SimilarTransactionInListRule()
        );
    }

    public NewTransactionEvaluator(final EvaluationModel evaluationModel) {
        super(evaluationModel);
    }

    @Override
    public void setRules() {
        super.rules = rules;
    }
}
