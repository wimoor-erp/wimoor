package com.amazon.spapi.documents;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class UploadSpecificationTest {
    @Test
    public void testBuilderConstructorMissingContentType() throws Exception {
        String contentType = null;
        CryptoStreamFactory cryptoStreamFactory = Mockito.mock(CryptoStreamFactory.class);
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = "https://www.amazon.com";

        assertThrows(IllegalArgumentException.class, () -> new UploadSpecification.Builder(
                contentType, cryptoStreamFactory, source, url));
    }

    @Test
    public void testBuilderConstructorMissingCryptoStreamFactory() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = null;
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = "https://www.amazon.com";

        assertThrows(IllegalArgumentException.class, () -> new UploadSpecification.Builder(
                contentType, cryptoStreamFactory, source, url));
    }

    @Test
    public void testBuilderConstructorMissingSource() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = Mockito.mock(CryptoStreamFactory.class);
        InputStream source = null;
        String url = "https://www.amazon.com";

        assertThrows(IllegalArgumentException.class, () -> new UploadSpecification.Builder(
                contentType, cryptoStreamFactory, source, url));
    }

    @Test
    public void testBuilderConstructorMissingUrl() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = Mockito.mock(CryptoStreamFactory.class);
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = null;

        assertThrows(IllegalArgumentException.class, () -> new UploadSpecification.Builder(
                contentType, cryptoStreamFactory, source, url));
    }

    @Test
    public void testSuccess() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = Mockito.mock(CryptoStreamFactory.class);
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = "http://abc.com/123";

        UploadSpecification spec = new UploadSpecification.Builder(contentType, cryptoStreamFactory, source, url)
                .build();

        assertEquals(contentType, spec.getContentType());
        assertSame(cryptoStreamFactory, spec.getCryptoStreamFactory());
        assertSame(source, spec.getSource());
        assertEquals(url, spec.getUrl());
    }
}