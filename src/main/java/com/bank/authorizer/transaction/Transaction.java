package com.bank.authorizer.transaction;

import com.bank.authorizer.account.Monetary;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Data
public abstract class Transaction implements Identifiable<UUID> {

    @Setter(AccessLevel.NONE)
    protected final UUID id = randomUUID();
    @Setter(AccessLevel.NONE)
    protected final LocalDateTime creationTime = LocalDateTime.now();
    protected long amount;
    protected LocalDateTime date;
    protected Status status = Status.SUBMITTED;
    public abstract void start(Monetary account);
    public abstract boolean isSimilar(Transaction transaction);

    public enum Status {
        SUBMITTED,
        AUTHORIZED,
        DECLINED
    }
}
