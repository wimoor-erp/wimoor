package com.amazon.spapi.documents.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class HttpResponseExceptionTest {
    @Test
    public void testConstructor() {
        String message = "This is the message";
        String body = "This is the body";
        int code = 403;
        String expectToString = "com.amazon.spapi.documents.exception.HttpResponseException: " +
                "This is the message {code=403, body=This is the body}";

        HttpResponseException exception = new HttpResponseException(message, body, code);

        assertEquals(message, exception.getMessage());
        assertEquals(body, exception.getBody());
        assertEquals(code, exception.getCode());
        assertEquals(expectToString, exception.toString());
    }

    @Test
    public void testConstructorNullBody() {
        String message = "This is the message";
        String body = null;
        int code = 403;
        String expectToString = "com.amazon.spapi.documents.exception.HttpResponseException: " +
                "This is the message {code=403, body=null}";

        HttpResponseException exception = new HttpResponseException(message, body, code);

        assertEquals(message, exception.getMessage());
        assertEquals(body, exception.getBody());
        assertEquals(code, exception.getCode());
        assertEquals(expectToString, exception.toString());
    }

    @Test
    public void testConstructorCause() {
        String message = "This is the message";
        Throwable cause = new RuntimeException();
        String body = "This is the body";
        int code = 403;

        HttpResponseException exception = new HttpResponseException(message, cause, body, code);

        assertEquals(message, exception.getMessage());
        assertSame(cause, exception.getCause());
        assertEquals(body, exception.getBody());
        assertEquals(code, exception.getCode());
    }
}