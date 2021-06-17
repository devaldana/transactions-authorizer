package com.bank.authorizer.transaction;

import com.bank.authorizer.account.Monetary;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import static com.bank.authorizer.transaction.Transaction.Status.AUTHORIZED;
import static java.util.Objects.isNull;

@Log4j2
public class Payment extends Transaction {

    @Setter @Getter
    private String merchant;

    @Override
    public void start(final Monetary account) {
        if (AUTHORIZED.equals(status)) {
            log.info("Trying to debit {} from account with id={}", amount, account.getId());
            account.debit(amount);
            account.saveTransaction(this);
            log.info("{} successfully debited from account with id={}", amount, account.getId());
        } else {
            throw new IllegalStateException("Unauthorized transaction, id=" + id);
        }
    }

    @Override
    public boolean isSimilar(final Transaction transaction) {
        if(isNull(transaction)) return false;
        if(!(transaction instanceof Payment)) return false;
        final Payment payment = (Payment) transaction;
        return this.amount == payment.amount && this.merchant.equals(payment.merchant);
    }

    public String toString() {
        return String.format("Payment(id=%s, status=%s, creationTime=%s, amount=%s, merchant=%s, date=%s)",
                              id, status, creationTime, amount, merchant,  date);
    }
}