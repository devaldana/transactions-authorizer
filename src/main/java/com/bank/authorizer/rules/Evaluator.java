package com.bank.authorizer.rules;

import com.bank.authorizer.rules.exceptions.NotPreparedEvaluator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

public abstract class Evaluator<E> {

    protected List<Rule<E>> rules;
    private E entity;

    public Evaluator(E entity) {
        this.entity = entity;
        setRules();
    }

    // This methods iterates over the rules defined in the concrete implementation of this class.
    // For each rules that evaluates to true, a violation is added to a list that is returned.
    public Set<Violation> evaluate() {
        if (isPrepared()) {
            final Set<Violation> violations = new LinkedHashSet<>();
            rules.forEach(rule -> {
                if(rule.evaluate(entity)) violations.add(rule.getViolation());
            });
            return violations;
        } else {
            throw new NotPreparedEvaluator();
        }
    }

    private boolean isPrepared() {
        return nonNull(entity) && nonNull(rules);
    }

    public abstract void setRules();
}