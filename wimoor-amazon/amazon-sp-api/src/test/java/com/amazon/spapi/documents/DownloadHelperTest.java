package com.amazon.spapi.documents;

import com.amazon.spapi.documents.exception.MissingCharsetException;
import com.amazon.spapi.documents.impl.AESCryptoStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class DownloadHelperTest {
    private static String KEY = "sxx/wImF6BFndqSAz56O6vfiAh8iD9P297DHfFgujec=";
    private static String VECTOR = "7S2tn363v0wfCfo1IX2Q1A==";

    private Answer getTransferAnswer(String contentType, CryptoStreamFactory cryptoStreamFactory, String fileContents,
                                     boolean isGZipped) {
        return new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                File file = invocation.getArgument(1);

                byte[] dataToWrite = fileContents.getBytes(StandardCharsets.UTF_8);

                if (isGZipped) {
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToWrite.length);
                    GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);
                    zipStream.write(dataToWrite);
                    zipStream.close();
                    dataToWrite = byteStream.toByteArray();
                }

                // Write encrypted contents to file to mimic downloading an encrypted file
                InputStream source = new ByteArrayInputStream(dataToWrite);
                try (InputStream inputStream = cryptoStreamFactory.newEncryptStream(source)) {
                    FileUtils.copyInputStreamToFile(inputStream, file);
                }

                return contentType;
            }
        };
    }

    @Test
    public void testSuccess() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "SPAPI.*\\.tmp$";
        String expectContents = "Hello World";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);
        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);
        Mockito.doReturn(contentType).when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        Mockito.doAnswer(getTransferAnswer(contentType, cryptoStreamFactory, expectContents, false))
                .when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        DownloadHelper helper = new DownloadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .build();

        DownloadSpecification spec = new DownloadSpecification.Builder(cryptoStreamFactory, url).build();

        File file;
        try (DownloadBundle bundle = helper.download(spec)) {
            assertEquals(contentType, bundle.getContentType());

            file = fileArg.getValue();
            assertTrue(file.exists());

            assertTrue(file.getName().matches(expectedFileRegex));

            try (InputStream contents = bundle.newInputStream()) {
                assertTrue(IOUtils.contentEquals(contents, new ByteArrayInputStream(expectContents.getBytes(
                        StandardCharsets.UTF_8))));
            }
        }

        assertFalse(file.exists());

        Mockito.verify(httpTransferClient).download(Mockito.eq(url), Mockito.any());
    }

    @Test
    public void testSuccessWithCompression() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "SPAPI.*\\.tmp$";
        String expectContents = "Hello World";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);
        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);
        Mockito.doReturn(contentType).when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        Mockito.doAnswer(getTransferAnswer(contentType, cryptoStreamFactory, expectContents, true))
                .when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        DownloadHelper helper = new DownloadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .build();

        DownloadSpecification spec = new DownloadSpecification.Builder(cryptoStreamFactory, url)
                .withCompressionAlgorithm(CompressionAlgorithm.GZIP)
                .build();

        File file;
        try (DownloadBundle bundle = helper.download(spec)) {
            assertEquals(contentType, bundle.getContentType());

            file = fileArg.getValue();
            assertTrue(file.exists());

            assertTrue(file.getName().matches(expectedFileRegex));

            try (InputStream contents = bundle.newInputStream()) {
                assertTrue(IOUtils.contentEquals(contents, new ByteArrayInputStream(expectContents.getBytes(
                        StandardCharsets.UTF_8))));
            }

            try (BufferedReader contents = bundle.newBufferedReader()) {
                assertTrue(IOUtils.contentEquals(contents, new StringReader(expectContents)));
            }

            assertTrue(file.exists());
        }

        assertFalse(file.exists());

        Mockito.verify(httpTransferClient).download(Mockito.eq(url), Mockito.any());
    }

    @Test
    public void testSuccessWithCustomTempFileParams() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "NOTSPAPI.*\\.NOTtmp$";
        String expectContents = "Hello World";
        String expectedFilePath = "spapitmp";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);
        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);
        Mockito.doReturn(contentType).when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        Mockito.doAnswer(getTransferAnswer(contentType, cryptoStreamFactory, expectContents, false))
                .when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        File tmpDir = new File(Paths.get(System.getProperty("java.io.tmpdir"), expectedFilePath)
                .toAbsolutePath().toString());
        tmpDir.mkdirs();
        tmpDir.deleteOnExit();

        DownloadHelper helper = new DownloadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .withTmpFilePrefix("NOTSPAPI")
                .withTmpFileSuffix(".NOTtmp")
                .withTmpFileDirectory(tmpDir)
                .build();

        DownloadSpecification spec = new DownloadSpecification.Builder(cryptoStreamFactory, url).build();

        File file;
        try (DownloadBundle bundle = helper.download(spec)) {
            assertEquals(contentType, bundle.getContentType());

            file = fileArg.getValue();
            assertTrue(file.exists());
            assertEquals(expectedFilePath, file.getParentFile().getName());

            assertTrue(file.getName().matches(expectedFileRegex));

            try (BufferedReader contents = bundle.newBufferedReader()) {
                assertTrue(IOUtils.contentEquals(contents, new StringReader(expectContents)));
            }

            try (InputStream contents = bundle.newInputStream()) {
                assertTrue(IOUtils.contentEquals(contents, new ByteArrayInputStream(expectContents.getBytes(
                        StandardCharsets.UTF_8))));
            }

            assertTrue(file.exists());
        }

        assertFalse(file.exists());

        Mockito.verify(httpTransferClient).download(Mockito.eq(url), Mockito.any());
    }

    @Test
    public void testReaderBadCharset() throws Exception {
        String contentType = "text/xml; charset=NOTSUPPORTED";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "NOTSPAPI.*\\.NOTtmp$";
        String expectContents = "Hello World";
        String expectedFilePath = "spapitmp";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);
        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);
        Mockito.doReturn(contentType).when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        Mockito.doAnswer(getTransferAnswer(contentType, cryptoStreamFactory, expectContents, false))
                .when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        File tmpDir = new File(Paths.get(System.getProperty("java.io.tmpdir"), expectedFilePath)
                .toAbsolutePath().toString());
        tmpDir.mkdirs();
        tmpDir.deleteOnExit();

        DownloadHelper helper = new DownloadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .withTmpFilePrefix("NOTSPAPI")
                .withTmpFileSuffix(".NOTtmp")
                .withTmpFileDirectory(tmpDir)
                .build();

        DownloadSpecification spec = new DownloadSpecification.Builder(cryptoStreamFactory, url).build();

        File file;
        try (DownloadBundle bundle = helper.download(spec)) {
            assertEquals(contentType, bundle.getContentType());

            file = fileArg.getValue();
            assertTrue(file.exists());
            assertEquals(expectedFilePath, file.getParentFile().getName());

            assertTrue(file.getName().matches(expectedFileRegex));

            assertThrows(UnsupportedCharsetException.class, () -> bundle.newBufferedReader());

            try (InputStream contents = bundle.newInputStream()) {
                assertTrue(IOUtils.contentEquals(contents, new ByteArrayInputStream(expectContents.getBytes(
                        StandardCharsets.UTF_8))));
            }

            assertTrue(file.exists());
        }

        assertFalse(file.exists());

        Mockito.verify(httpTransferClient).download(Mockito.eq(url), Mockito.any());
    }

    @Test
    public void testMissingCharsetNoDefault() throws Exception {
        String contentType = "text/xml";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "NOTSPAPI.*\\.NOTtmp$";
        String expectContents = "Hello World";
        String expectedFilePath = "spapitmp";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);
        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);
        Mockito.doReturn(contentType).when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        Mockito.doAnswer(getTransferAnswer(contentType, cryptoStreamFactory, expectContents, false))
                .when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        File tmpDir = new File(Paths.get(System.getProperty("java.io.tmpdir"), expectedFilePath)
                .toAbsolutePath().toString());
        tmpDir.mkdirs();
        tmpDir.deleteOnExit();

        DownloadHelper helper = new DownloadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .withTmpFilePrefix("NOTSPAPI")
                .withTmpFileSuffix(".NOTtmp")
                .withTmpFileDirectory(tmpDir)
                .build();

        DownloadSpecification spec = new DownloadSpecification.Builder(cryptoStreamFactory, url).build();

        File file;
        try (DownloadBundle bundle = helper.download(spec)) {
            assertEquals(contentType, bundle.getContentType());

            file = fileArg.getValue();
            assertTrue(file.exists());
            assertEquals(expectedFilePath, file.getParentFile().getName());

            assertTrue(file.getName().matches(expectedFileRegex));

            assertThrows(MissingCharsetException.class, () -> bundle.newBufferedReader());

            try (InputStream contents = bundle.newInputStream()) {
                assertTrue(IOUtils.contentEquals(contents, new ByteArrayInputStream(expectContents.getBytes(
                        StandardCharsets.UTF_8))));
            }

            assertTrue(file.exists());
        }

        assertFalse(file.exists());

        Mockito.verify(httpTransferClient).download(Mockito.eq(url), Mockito.any());
    }

    @Test
    public void testSuccessMissingBadCharsetDefaultProvided() throws Exception {
        String contentType = "text/xml";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "NOTSPAPI.*\\.NOTtmp$";
        String expectContents = "Hello World";
        String expectedFilePath = "spapitmp";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);
        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);
        Mockito.doReturn(contentType).when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        Mockito.doAnswer(getTransferAnswer(contentType, cryptoStreamFactory, expectContents, false))
                .when(httpTransferClient).download(Mockito.eq(url), fileArg.capture());

        File tmpDir = new File(Paths.get(System.getProperty("java.io.tmpdir"), expectedFilePath)
                .toAbsolutePath().toString());
        tmpDir.mkdirs();
        tmpDir.deleteOnExit();

        DownloadHelper helper = new DownloadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .withTmpFilePrefix("NOTSPAPI")
                .withTmpFileSuffix(".NOTtmp")
                .withTmpFileDirectory(tmpDir)
                .build();

        DownloadSpecification spec = new DownloadSpecification.Builder(cryptoStreamFactory, url).build();

        File file;
        try (DownloadBundle bundle = helper.download(spec)) {
            assertEquals(contentType, bundle.getContentType());

            file = fileArg.getValue();
            assertTrue(file.exists());
            assertEquals(expectedFilePath, file.getParentFile().getName());

            assertTrue(file.getName().matches(expectedFileRegex));

            try (InputStream contents = bundle.newInputStream()) {
                assertTrue(IOUtils.contentEquals(contents, new ByteArrayInputStream(expectContents.getBytes(
                        StandardCharsets.UTF_8))));
            }

            try (BufferedReader contents = bundle.newBufferedReader(StandardCharsets.UTF_8)) {
                assertTrue(IOUtils.contentEquals(contents, new StringReader(expectContents)));
            }

            assertTrue(file.exists());
        }

        assertFalse(file.exists());

        Mockito.verify(httpTransferClient).download(Mockito.eq(url), Mockito.any());
    }

    @Test
    public void testBadUrlDownload() throws Exception {
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        String url = "sdfkajsdfiefi";

        Path path = Files.createTempDirectory("SPAPI");

        DownloadHelper helper = new DownloadHelper.Builder()
                .withTmpFileDirectory(path.toFile())
                .build();

        DownloadSpecification spec = new DownloadSpecification.Builder(cryptoStreamFactory, url)
                .build();

        try {
            assertThrows(IllegalArgumentException.class, () -> helper.download(spec));
        } finally {
            // This will throw if the directory is not empty, indicating that the temp file was not removed
            Files.delete(path);
        }
    }

    @Test
    public void testInvalidTmpFilePrefix() {
        DownloadHelper.Builder builder = new DownloadHelper.Builder();

        assertThrows(IllegalArgumentException.class, () -> builder
                .withTmpFilePrefix("A"));
    }
}