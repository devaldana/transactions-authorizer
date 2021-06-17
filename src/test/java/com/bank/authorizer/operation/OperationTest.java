package com.bank.authorizer.operation;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.transaction.Payment;
import com.bank.authorizer.transaction.Transaction;
import com.google.gson.JsonSyntaxException;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class OperationTest {

    @Test
    public void parsingActiveAccount() {
        String jsonLine = "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";
        Operation operation = Operation.parseString(jsonLine);
        assertTrue(operation.isAccountCreation());
        assertFalse(operation.isTransaction());
        assertTrue(operation.getAccount().isPresent());
        assertFalse(operation.getTransaction().isPresent());
        assertEquals(100, operation.getAccount().get().getBalance());
        assertEquals(Account.Status.ACTIVE, operation.getAccount().get().getStatus());
        assertNotEquals(Account.Status.INACTIVE, operation.getAccount().get().getStatus());
    }

    @Test
    public void parsingInactiveAccount() {
        String jsonLine = "{\"account\": {\"active-card\": false, \"available-limit\": 0}}";
        Operation operation = Operation.parseString(jsonLine);
        assertTrue(operation.isAccountCreation());
        assertFalse(operation.isTransaction());
        assertTrue(operation.getAccount().isPresent());
        assertFalse(operation.getTransaction().isPresent());
        assertEquals(0, operation.getAccount().get().getBalance());
        assertEquals(Account.Status.INACTIVE, operation.getAccount().get().getStatus());
        assertNotEquals(Account.Status.ACTIVE, operation.getAccount().get().getStatus());
    }

    @Test
    public void parsingTransaction() {
        String jsonLine = "{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\"}}";
        Operation operation = Operation.parseString(jsonLine);
        assertFalse(operation.isAccountCreation());
        assertTrue(operation.isTransaction());
        assertFalse(operation.getAccount().isPresent());
        assertTrue(operation.getTransaction().isPresent());
        Transaction transaction = operation.getTransaction().get();
        assertTrue(transaction instanceof Payment);
        Payment payment = (Payment) transaction;
        assertEquals("Burger King", payment.getMerchant());
        assertEquals(20, payment.getAmount());
        // Default status for a Transaction is SUBMITTED
        assertEquals(Transaction.Status.SUBMITTED, payment.getStatus());
        assertNotNull(payment.getCreationTime());
        assertNotNull(payment.getId());
        assertNotNull(payment.getDate());
        LocalDateTime date = LocalDateTime.of(2019, 2, 13, 10, 0);
        assertEquals(date, payment.getDate());
    }

    @Test(expected = JsonSyntaxException.class)
    public void parsingInvalidJson() {
        String jsonLine = "{\"account\": {\"active-card\": false, \"available-limit\": 0}";
        Operation operation = Operation.parseString(jsonLine);
    }

    @Test(expected = NotSupportedOperation.class)
    public void notSupportedOperation() {
        String jsonLine = "{\"card\": {\"exp\": 2025, \"code\": 123}}";
        Operation operation = Operation.parseString(jsonLine);
    }
}