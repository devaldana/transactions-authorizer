package com.bank.authorizer.account;

import com.bank.authorizer.transaction.Transaction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Data
public class Account implements Monetary<UUID> {

    @Setter(AccessLevel.NONE)
    private final UUID id = randomUUID();
    private Status status = Status.INACTIVE;

    @Setter(AccessLevel.NONE)
    private long balance;

    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private List<Transaction> transactions = new LinkedList<>();

    @Override
    public void saveTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public void credit(final long amount) {
        balance += amount;
    }

    @Override
    public void debit(final long amount) {
        balance -= amount;
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
