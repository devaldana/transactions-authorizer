package com.bank.authorizer.transaction;

import com.bank.authorizer.account.Account;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.bank.authorizer.TestsHelper.createAccount;
import static com.bank.authorizer.TestsHelper.createPayment;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class TransactionTest {

    private static long ONE_MILLISECOND = 1;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void similarTransaction() throws InterruptedException {
        String merchant = "Microsoft";
        long amount = 17;
        Transaction firstTransaction = createPayment(merchant, amount);
        sleep(ONE_MILLISECOND); // Wait 1 millisecond to get different creation time
        Transaction secondTransaction = createPayment(merchant, amount);
        assertNotNull(firstTransaction.getId());
        assertNotNull(secondTransaction.getId());
        assertNotNull(firstTransaction.getCreationTime());
        assertNotNull(secondTransaction.getCreationTime());
        assertNotEquals(firstTransaction.getId(), secondTransaction.getId());
        assertNotEquals(firstTransaction.getCreationTime(), secondTransaction.getCreationTime());
        assertTrue(firstTransaction.isSimilar(secondTransaction));
    }

    @Test
    public void startAuthorizedTransaction() {
        Transaction transaction = createPayment("Apple", 35);
        Account account = createAccount(100);
        transaction.setStatus(Transaction.Status.AUTHORIZED);
        transaction.start(account);
        assertEquals(65, account.getBalance());
        assertNotNull(account.getTransactions());
        assertFalse(account.getTransactions().isEmpty());
        assertEquals(1, account.getTransactions().size());
    }

    @Test
    public void startNotAuthorizedTransaction() {
        Transaction transaction = createPayment("Apple", 35);
        Account account = createAccount(100);
        // By default, the status of a new transaction is 'SUBMITTED'
        assertEquals(Transaction.Status.SUBMITTED, transaction.getStatus());

        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("Unauthorized transaction, id=" + transaction.getId());
        transaction.start(account);
    }
}