package com.bank.authorizer.rules.business;

import com.bank.authorizer.rules.Rule;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.models.EvaluationModel;
import com.bank.authorizer.transaction.Transaction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.bank.authorizer.TestsHelper.createAccount;
import static com.bank.authorizer.TestsHelper.createPayment;
import static org.junit.Assert.*;

public class AllRulesTest {

    @Test
    public void accountAlreadyInitializedRule() {
        Rule<EvaluationModel> rule = new AccountAlreadyInitializedRule();
        assertEquals(Violation.ACCOUNT_ALREADY_INITIALIZED, rule.getViolation());
        EvaluationModel model = new EvaluationModel(createAccount(0));
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void accountInvalidInitialBalanceRule() {
        Rule<EvaluationModel> rule = new AccountInvalidInitialBalanceRule();
        assertEquals(Violation.INVALID_INITIAL_BALANCE, rule.getViolation());

        // Initial balance equals to zero (0)
        EvaluationModel model = new EvaluationModel(createAccount(0));
        assertTrue(rule.evaluate(model));

        // Negative initial balance
        model = new EvaluationModel(createAccount(-10));
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void accountNotInitializedRule() {
        Rule<EvaluationModel> rule = new AccountNotInitializedRule();
        assertEquals(Violation.UNINITIALIZED_ACCOUNT, rule.getViolation());
        EvaluationModel model = new EvaluationModel();
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void inactiveCardRule() {
        Rule<EvaluationModel> rule = new InactiveCardRule();
        assertEquals(Violation.INACTIVE_CARD, rule.getViolation());

        // Initial account status is 'INACTIVE' which means card is also inactive by default
        EvaluationModel model = new EvaluationModel(createAccount(0));
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void insufficientFundsRule() {
        Rule<EvaluationModel> rule = new InsufficientFundsRule();
        assertEquals(Violation.INSUFFICIENT_FUNDS, rule.getViolation());
        EvaluationModel model = new EvaluationModel();
        model.setAccount(createAccount(100));
        model.setTransaction(createPayment("O'REILLY", 185));
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void invalidTransactionAmountRule() {
        Rule<EvaluationModel> rule = new InvalidTransactionAmountRule();
        assertEquals(Violation.INVALID_TRANSACTION_AMOUNT, rule.getViolation());
        EvaluationModel model = new EvaluationModel();
        model.setAccount(createAccount(100));
        model.setTransaction(createPayment("O'REILLY", 0));
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void numberOfTransactionsLimitExceededRule() {
        Rule<EvaluationModel> rule = new NumberOfTransactionsLimitExceededRule();
        assertEquals(Violation.TOO_MANY_TRANSACTIONS, rule.getViolation());
        EvaluationModel model = new EvaluationModel();
        List<Transaction> transactions = new ArrayList<>();
        model.setTransactions(transactions);
        transactions.add(createPayment("Apple", -5));
        transactions.add(createPayment("O'REILLY", 23));
        assertFalse(rule.evaluate(model));
        transactions.add(createPayment("Microsoft", 0));
        assertTrue(rule.evaluate(model));
        transactions.add(createPayment("Manning", 87));
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void similarTransactionInListRule() {
        Rule<EvaluationModel> rule = new SimilarTransactionInListRule();
        assertEquals(Violation.DUPLICATE_TRANSACTION, rule.getViolation());
        EvaluationModel model = new EvaluationModel(createPayment("Microsoft", 3));
        List<Transaction> transactions = new ArrayList<>();
        model.setTransactions(transactions);
        transactions.add(createPayment("Apple", -5));
        transactions.add(createPayment("O'REILLY", 23));
        assertFalse(rule.evaluate(model));
        transactions.add(createPayment("Microsoft", 3));
        assertTrue(rule.evaluate(model));
    }

    @Test
    public void validTransactionAmountRule() {
        Rule<EvaluationModel> rule = new ValidTransactionAmountRule();
        assertEquals(Violation.INVALID_TRANSACTION_AMOUNT, rule.getViolation());
        EvaluationModel model = new EvaluationModel(createPayment("Zara", 0));
        assertFalse(rule.evaluate(model));
        model = new EvaluationModel(createPayment("Donella", -21));
        assertFalse(rule.evaluate(model));
        model = new EvaluationModel(createPayment("Evans", 43));
        assertTrue(rule.evaluate(model));
    }
}