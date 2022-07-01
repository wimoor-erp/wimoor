package com.amazon.spapi.documents.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class CryptoExceptionTest {
    @Test
    public void testConstructor() {
        Throwable throwable = new RuntimeException();
        CryptoException exception = new CryptoException(throwable);

        assertSame(throwable, exception.getCause());
    }
}