package com.bank.authorizer.rules.engine;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.evaluators.CurrentAccountEvaluator;
import com.bank.authorizer.rules.evaluators.NewAccountEvaluator;
import com.bank.authorizer.rules.evaluators.NewTransactionEvaluator;
import com.bank.authorizer.rules.models.EvaluationModel;
import com.bank.authorizer.transaction.Transaction;

import java.util.LinkedHashSet;
import java.util.List;

public final class RulesEngine {

    // As a utility class, no instances are required
    private RulesEngine() {}

    public static LinkedHashSet<Violation> validateAccountForCreation(final Account currentAccount,
                                                                      final Account newAccount) {

        // Evaluating that the current account meet the defined rules
        final EvaluationModel evaluationModelForCurrentAccount = new EvaluationModel(currentAccount);
        final CurrentAccountEvaluator currentAccountEvaluator = new CurrentAccountEvaluator(evaluationModelForCurrentAccount);
        final LinkedHashSet<Violation> currentAccountViolations = (LinkedHashSet<Violation>) currentAccountEvaluator.evaluate();

        // Evaluating that the new account meet the defined rules
        final EvaluationModel evaluationModelForNewAccount = new EvaluationModel(newAccount);
        final NewAccountEvaluator newAccountEvaluator = new NewAccountEvaluator(evaluationModelForNewAccount);
        final LinkedHashSet<Violation> newAccountViolations = (LinkedHashSet<Violation>) newAccountEvaluator.evaluate();

        // Concatenating and returning all rules violations
        currentAccountViolations.addAll(newAccountViolations);
        return currentAccountViolations;
    }

    public static LinkedHashSet<Violation> validateTransactionToBeProcessed(final Transaction transaction,
                                                                            final Account currentAccount,
                                                                            final List<Transaction> transactionsInHighFrequencyInterval) {

        final NewTransactionEvaluator newTransactionEvaluator;
        final EvaluationModel evaluationModel =  new EvaluationModel();
        evaluationModel.setAccount(currentAccount);
        evaluationModel.setTransaction(transaction);
        evaluationModel.setTransactions(transactionsInHighFrequencyInterval);
        newTransactionEvaluator = new NewTransactionEvaluator(evaluationModel);
        return (LinkedHashSet<Violation>) newTransactionEvaluator.evaluate();
    }
}
