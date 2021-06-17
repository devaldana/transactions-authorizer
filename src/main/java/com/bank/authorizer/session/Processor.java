package com.bank.authorizer.session;

import com.bank.authorizer.transaction.Identifiable;
import com.bank.authorizer.transaction.Transaction;

interface Processor<T> extends Identifiable<T> {
    Result process(Transaction transaction);
}
