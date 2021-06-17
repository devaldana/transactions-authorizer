package com.bank.authorizer;

import com.bank.authorizer.operation.Operation;
import com.bank.authorizer.session.Result;
import com.bank.authorizer.session.Session;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.powermock.reflect.Whitebox.getMethod;

public class ApplicationTest {

    @Test
    public void runOperation() throws InvocationTargetException, IllegalAccessException {
        Session session = new Session();
        String jsonLine = "{\"account\": {\"active-card\": true, \"available-limit\": 0}}";
        Operation operation = Operation.parseString(jsonLine);
        Method runOperation = getMethod(Application.class, "runOperation", Session.class, Operation.class);
        Result result = (Result) runOperation.invoke(null, session, operation);
        assertNotNull(result);
        assertFalse(result.getViolations().isEmpty());
        assertEquals(1, result.getViolations().size());
    }

}