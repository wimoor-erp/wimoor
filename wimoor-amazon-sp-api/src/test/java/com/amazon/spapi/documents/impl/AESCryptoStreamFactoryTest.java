package com.amazon.spapi.documents.impl;

import com.amazon.spapi.documents.exception.CryptoException;
import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AESCryptoStreamFactoryTest {
    private static String KEY = "sxx/wImF6BFndqSAz56O6vfiAh8iD9P297DHfFgujec=";
    private static String VECTOR = "7S2tn363v0wfCfo1IX2Q1A==";

    @Test
    public void testBuilderConstructorMissingInitializationVector() {
        String key = "DEF";
        String initializationVector = null;

        assertThrows(IllegalArgumentException.class, () ->
                new AESCryptoStreamFactory.Builder(key, initializationVector));
    }

    @Test
    public void testBuilderConstructorMissingKey() {
        String key = null;
        String initializationVector = "ABC";

        assertThrows(IllegalArgumentException.class, () ->
                new AESCryptoStreamFactory.Builder(key, initializationVector));
    }

    @Test
    public void testBadKey() throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(KEY);
        String encodedKey = Base64.getEncoder().encodeToString(
                Arrays.copyOfRange(decodedKey, 2, decodedKey.length-1));

        AESCryptoStreamFactory aesCryptoStreamFactory = new AESCryptoStreamFactory.Builder(encodedKey, VECTOR).build();
        try (InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes(StandardCharsets.UTF_8))) {
            assertThrows(CryptoException.class, () -> aesCryptoStreamFactory.newDecryptStream(inputStream));
        }
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        String stringContent = "Hello World!";
        byte[] byteContent = stringContent.getBytes(StandardCharsets.UTF_8);

        AESCryptoStreamFactory aesCryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();

        try (InputStream encryptStream =
                     aesCryptoStreamFactory.newEncryptStream(new ByteArrayInputStream(byteContent))) {
            byte[] encryptedContent = ByteStreams.toByteArray(encryptStream);

            try (InputStream decryptStream =
                         aesCryptoStreamFactory.newDecryptStream(new ByteArrayInputStream(encryptedContent))) {
                byte[] decryptedContent = ByteStreams.toByteArray(decryptStream);

                assertArrayEquals(decryptedContent, byteContent);
                assertFalse(Arrays.equals(encryptedContent, decryptedContent));
            }
        }
    }
}