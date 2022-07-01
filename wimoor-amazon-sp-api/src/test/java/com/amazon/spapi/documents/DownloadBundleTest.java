package com.amazon.spapi.documents;

import com.amazon.spapi.documents.impl.AESCryptoStreamFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DownloadBundleTest {
    private static String KEY = "sxx/wImF6BFndqSAz56O6vfiAh8iD9P297DHfFgujec=";
    private static String VECTOR = "7S2tn363v0wfCfo1IX2Q1A==";

    private DownloadBundle createBadFileBundle() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();

        File file = File.createTempFile("foo", null, null);
        file.delete();

        return new DownloadBundle(null, contentType, cryptoStreamFactory, file);
    }

    @Test
    public void testNewInputStreamBadFile() throws Exception {
        DownloadBundle downloadBundle = createBadFileBundle();
        assertThrows(FileNotFoundException.class, () -> downloadBundle.newInputStream());
    }

    @Test
    public void testNewReaderBadFile() throws Exception {
        DownloadBundle downloadBundle = createBadFileBundle();
        assertThrows(FileNotFoundException.class, () -> downloadBundle.newBufferedReader());
    }
}