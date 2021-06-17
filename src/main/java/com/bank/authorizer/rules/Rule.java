package com.bank.authorizer.rules;

public interface Rule<T> {
    boolean evaluate(T type);
    Violation getViolation();
}
