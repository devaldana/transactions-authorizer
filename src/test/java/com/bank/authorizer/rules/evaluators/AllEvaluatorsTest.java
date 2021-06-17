package com.bank.authorizer.rules.evaluators;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Evaluator;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.exceptions.NotPreparedEvaluator;
import com.bank.authorizer.rules.models.EvaluationModel;
import com.bank.authorizer.transaction.Transaction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.bank.authorizer.TestsHelper.createAccount;
import static com.bank.authorizer.TestsHelper.createPayment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllEvaluatorsTest {

    @Test(expected = NotPreparedEvaluator.class)
    public void notPreparedEvaluator() {
        Evaluator evaluator = new NewTransactionEvaluator(null);
        evaluator.evaluate();
    }

    @Test
    public void currentAccountEvaluator() {
        EvaluationModel model = new EvaluationModel();
        Evaluator<EvaluationModel>  evaluator = new CurrentAccountEvaluator(model);
        Set<Violation> violations = evaluator.evaluate();
        assertEquals(0, violations.size());
        model.setAccount(createAccount(0));
        violations = evaluator.evaluate();
        assertEquals(1, violations.size());
        assertTrue(violations.contains(Violation.ACCOUNT_ALREADY_INITIALIZED));
    }

    @Test
    public void newAccountEvaluator() {
        EvaluationModel model = new EvaluationModel(createAccount(100));
        Evaluator<EvaluationModel>  evaluator = new NewAccountEvaluator(model);
        Set<Violation> violations = evaluator.evaluate();
        assertEquals(0, violations.size());
        model.setAccount(createAccount(0));
        violations = evaluator.evaluate();
        assertEquals(1, violations.size());
        assertTrue(violations.contains(Violation.INVALID_INITIAL_BALANCE));
        model.setAccount(createAccount(-39));
        violations = evaluator.evaluate();
        assertEquals(1, violations.size());
        assertTrue(violations.contains(Violation.INVALID_INITIAL_BALANCE));
    }

    @Test
    public void newTransactionEvaluator() {
        List<Transaction> transactions = new ArrayList<>();
        EvaluationModel model = new EvaluationModel();
        Evaluator<EvaluationModel> evaluator = new NewTransactionEvaluator(model);
        Transaction transaction = createPayment("Samsung", 137);
        model.setTransaction(transaction);
        model.setTransactions(transactions);

        Set<Violation> violations = evaluator.evaluate();
        transactions.add(transaction);
        assertTrue(violations.contains(Violation.UNINITIALIZED_ACCOUNT));

        model.setAccount(createAccount(50));
        violations = evaluator.evaluate();
        assertTrue(violations.contains(Violation.INACTIVE_CARD));
        assertTrue(violations.contains(Violation.INSUFFICIENT_FUNDS));
        assertTrue(violations.contains(Violation.DUPLICATE_TRANSACTION));

        transaction = createPayment("Lenovo", 137);
        model.setTransaction(transaction);
        violations = evaluator.evaluate();
        transactions.add(transaction);
        assertTrue(violations.contains(Violation.INACTIVE_CARD));
        assertTrue(violations.contains(Violation.INSUFFICIENT_FUNDS));

        transaction = createPayment("Lenovo", 130);
        model.setTransaction(transaction);
        Account account =  new Account();
        account.credit(130);
        account.setStatus(Account.Status.ACTIVE);
        model.setAccount(account);
        violations = evaluator.evaluate();
        transactions.add(transaction);
        assertTrue(violations.isEmpty());

        transaction = createPayment("Lenovo", 137);
        model.setTransaction(transaction);
        violations = evaluator.evaluate();
        transactions.add(transaction);
        assertTrue(violations.contains(Violation.INSUFFICIENT_FUNDS));
        assertTrue(violations.contains(Violation.TOO_MANY_TRANSACTIONS));
        assertTrue(violations.contains(Violation.DUPLICATE_TRANSACTION));

        transaction = createPayment("LG", 0);
        model.setTransaction(transaction);
        violations = evaluator.evaluate();
        transactions.add(transaction);
        assertTrue(violations.contains(Violation.INVALID_TRANSACTION_AMOUNT));
        assertTrue(violations.contains(Violation.TOO_MANY_TRANSACTIONS));
    }
}