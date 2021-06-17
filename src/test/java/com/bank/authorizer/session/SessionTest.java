package com.bank.authorizer.session;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.transaction.Transaction;
import org.junit.Test;

import static com.bank.authorizer.TestsHelper.createAccount;
import static com.bank.authorizer.TestsHelper.createPayment;
import static org.junit.Assert.*;

public class SessionTest {

    @Test
    public void setAccount() {
        Session session = new Session();
        assertNotNull(session.getId());
        assertNotNull(session.getAllTransactions());
        assertTrue(session.getAllTransactions().isEmpty());
        Result result = session.setAccount(createAccount(0));
        assertNotNull(result);
        assertEquals(1, result.getViolations().size());
    }

    @Test
    public void processTransaction() {
        Session session = new Session();
        Account account = createAccount(15);
        Transaction transaction = createPayment("Burger King", 10);
        Result result = session.process(transaction);
        assertNotNull(result);
        assertEquals(1, result.getViolations().size());

        transaction = createPayment("Burger King", 10);
        session.setAccount(account);
        result = session.process(transaction);
        assertNotNull(result);
        assertEquals(2, result.getViolations().size());

        transaction = createPayment("Hilton", 15);
        account.setStatus(Account.Status.ACTIVE);
        result = session.process(transaction);
        assertNotNull(result);
        assertEquals(0, result.getViolations().size());

        transaction = createPayment("Levis", 15);
        result = session.process(transaction);
        assertNotNull(result);
        System.out.println(result.getViolations());
    }
}