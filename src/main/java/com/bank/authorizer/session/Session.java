package com.bank.authorizer.session;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Violation;
import com.bank.authorizer.rules.engine.RulesEngine;
import com.bank.authorizer.transaction.Transaction;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.bank.authorizer.config.Constants.*;
import static com.bank.authorizer.rules.Violation.ACCOUNT_ALREADY_INITIALIZED;
import static com.bank.authorizer.transaction.Transaction.Status.AUTHORIZED;
import static com.bank.authorizer.transaction.Transaction.Status.DECLINED;
import static java.util.UUID.randomUUID;

@Data
@Log4j2
public class Session implements Processor<UUID> {

    private final UUID id = randomUUID();
    private Account account;

    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private final List<Transaction> allTransactions = new LinkedList<>();

    // List of transactions in the last seconds defined in HIGH_FREQUENCY_INTERVAL constant, by default,
    // transactions of the last 2 minutes
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final CopyOnWriteArrayList<Transaction> transactionsInHighFrequencyInterval = new CopyOnWriteArrayList<>();

    public Session() {
        log.info("Session started, id={}", id);
        scheduleTimerForTransactionsCleanUp();
    }

    public Result setAccount(final Account account) {
        final Result result = new Result(account);
        final LinkedHashSet<Violation> violations = RulesEngine.validateAccountForCreation(this.account, account);
        result.addViolations(violations);

        if (violations.isEmpty()) {
            log.info("Setting session's account. {}", account);
            this.account = account;
        } else if(violations.contains(ACCOUNT_ALREADY_INITIALIZED)) {
            result.setAccount(this.account);
        }

        log.info("{} violations registering {}: {}", violations.size(), account, violations);
        return result;
    }

    @Override
    public Result process(final Transaction transaction) {
        log.info("Processing {}", transaction);
        final Result result = new Result();
        final LinkedHashSet<Violation> violations = RulesEngine.validateTransactionToBeProcessed(transaction, account, transactionsInHighFrequencyInterval);
        result.addViolations(violations);

        if (violations.isEmpty()) {
            transaction.setStatus(AUTHORIZED);
            transaction.start(account);
        } else {
            transaction.setStatus(DECLINED);
        }

        registerTransaction(transaction);
        result.setAccount(account);
        log.info("Transaction with id={} processed with final status '{}' and violations: {}",
                 transaction.getId(), transaction.getStatus(), violations);

        return result;
    }

    /* ************************************************************************************
     * Some auxiliary stuff
     * ************************************************************************************/

    private void registerTransaction(final Transaction transaction) {
        allTransactions.add(transaction);
        transactionsInHighFrequencyInterval.add(transaction);
    }

    private void scheduleTimerForTransactionsCleanUp() {
        log.info("Scheduling task to track high frequency transactions");
        final Timer timer = new Timer();
        timer.schedule(new Task(), NO_DELAY, TIMER_FREQUENCY);
    }

    private final class Task extends TimerTask {
        public void run() {
            // By default, two minutes ago time
            final LocalDateTime nowMinusIntervalTime = LocalDateTime.now().minusSeconds(HIGH_FREQUENCY_INTERVAL);
            for (final Transaction transaction : transactionsInHighFrequencyInterval) {
                if (transaction.getCreationTime().isBefore(nowMinusIntervalTime)) {
                    log.info("Removing outdated transaction: {}", transaction);
                    transactionsInHighFrequencyInterval.remove(FIRST_TRANSACTION);
                } else {
                    break;
                }
            }
        }
    }
}
