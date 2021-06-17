package com.bank.authorizer.session;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.rules.Violation;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static com.bank.authorizer.account.Account.Status.ACTIVE;
import static com.bank.authorizer.config.Constants.ACTIVE_CARD_MEMBER;
import static com.bank.authorizer.config.Constants.AVAILABLE_LIMIT_MEMBER;
import static java.util.Objects.nonNull;

@Data
public class Result {

    private AccountModel account;
    private final List<String> violations = new LinkedList<>();

    public Result() {}

    public Result(Account account) {
        setAccount(account);
    }

    public void setAccount(final Account account) {
        if (nonNull(account)) this.account = new AccountModel(account);
    }

    public void addViolation(final Violation violation) {
        violations.add(violation.getDescription());
    }

    public void addViolations(final LinkedHashSet<Violation> violations) {
        violations.forEach(this::addViolation);
    }

    private static final class AccountModel {
        @SerializedName(ACTIVE_CARD_MEMBER)
        private boolean activeCard;
        @SerializedName(AVAILABLE_LIMIT_MEMBER)
        private long availableLimit;

        AccountModel(Account account) {
            activeCard = ACTIVE.equals(account.getStatus());
            availableLimit = account.getBalance();
        }
    }
}