package com.amazon.spapi.documents;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompressionAlgorithmTest {
    public enum MyEnum {
        GZIP, NOT_GZIP
    }

    @Test
    public void testFromEquivalent() {
        assertEquals(CompressionAlgorithm.GZIP, CompressionAlgorithm.fromEquivalent(MyEnum.GZIP));
    }

    @Test
    public void testFromEquivalentNull() {
        MyEnum myEnum = null;
        assertNull(CompressionAlgorithm.fromEquivalent(myEnum));
    }

    @Test
    public void testNotEquivalent() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> CompressionAlgorithm.fromEquivalent(MyEnum.NOT_GZIP));
    }

    @Test
    public void testFromString() {
        assertEquals(CompressionAlgorithm.GZIP, CompressionAlgorithm.fromEquivalent("GZIP"));
    }

    @Test
    public void testFromStringNull() {
        String val = null;
        assertNull(CompressionAlgorithm.fromEquivalent(val));
    }

    @Test
    public void testFromStringUnsupportedValue() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> CompressionAlgorithm.fromEquivalent("NOT_GZIP"));
    }
}