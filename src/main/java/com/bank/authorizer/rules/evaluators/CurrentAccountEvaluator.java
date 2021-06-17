package com.bank.authorizer.rules.evaluators;

import com.bank.authorizer.rules.Evaluator;
import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.business.AccountAlreadyInitializedRule;
import com.bank.authorizer.rules.models.EvaluationModel;

import java.util.Arrays;
import java.util.List;

public final class CurrentAccountEvaluator extends Evaluator<EvaluationModel> {

    private static final List<Rule<EvaluationModel>> rules;

    static {
        rules = Arrays.asList(
                // List here the rules for the current account
                // The order here will be maintained in the attribute 'violations' of Result
                new AccountAlreadyInitializedRule()
        );
    }

    public CurrentAccountEvaluator(final EvaluationModel evaluationModel) {
        super(evaluationModel);
    }

    @Override
    public void setRules() {
        super.rules = rules;
    }
}
