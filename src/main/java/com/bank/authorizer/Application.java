package com.bank.authorizer;

import com.bank.authorizer.account.Account;
import com.bank.authorizer.operation.NotSupportedOperation;
import com.bank.authorizer.operation.Operation;
import com.bank.authorizer.session.Result;
import com.bank.authorizer.session.Session;
import com.bank.authorizer.transaction.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

import static com.bank.authorizer.config.Constants.INPUT_ACTION_TEXT;
import static java.util.Objects.isNull;

@Log4j2
public class Application {

    private static final Gson gson = new GsonBuilder().serializeNulls().create();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final Session session = new Session();

        while (true) {
            try {
                print(INPUT_ACTION_TEXT);
                final String jsonLine = scanner.nextLine();
                final Operation operation = Operation.parseString(jsonLine);
                final Result result = runOperation(session, operation);
                printResult(gson.toJson(result));
            } catch (Exception exception) {
                printException(exception);
            }
        }
    }

    private static Result runOperation(final Session session, final Operation operation) {
        Result result;
        if (operation.isAccountCreation()) {
            final Account account = operation.getAccount().get();
            result = session.setAccount(account);
        } else if (operation.isTransaction()) {
            final Transaction transaction = operation.getTransaction().get();
            result = session.process(transaction);
        } else {
            throw new NotSupportedOperation();
        }
        return result;
    }

    private static void print(final String message) {
        System.out.print(message);
    }

    private static void printResult(String message) {
        print("====> Operation result: " + message + "\n");
    }

    private static void printException(Exception exception) {
        final String message = isNull(exception.getMessage())? "unknown error, contact support.": exception.getMessage();
        print("======> Internal error: " + message + "\n");
    }
}