package com.bank.authorizer.rules.exceptions;

public class NotPreparedEvaluator extends RuntimeException {
    public NotPreparedEvaluator() {
        super("Not prepared evaluator: review that entity and rules are set");
    }
}
