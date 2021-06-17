package com.bank.authorizer.operation;

public class NotSupportedOperation extends RuntimeException {
    public NotSupportedOperation() {
        super("Invalid input, please validate that the JSON has the format " +
              "and the properties defined in the contract.");
    }
}
