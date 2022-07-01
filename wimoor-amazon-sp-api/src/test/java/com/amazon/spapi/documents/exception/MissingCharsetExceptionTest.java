package com.amazon.spapi.documents.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MissingCharsetExceptionTest {
    @Test
    public void testConstructor() {
        String message = "This is the message";
        MissingCharsetException exception = new MissingCharsetException(message);

        assertEquals(message, exception.getMessage());
    }
}