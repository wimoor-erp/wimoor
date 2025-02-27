package com.amazon.spapi.documents;

import com.amazon.spapi.documents.impl.AESCryptoStreamFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class UploadHelperTest {
    private static String KEY = "sxx/wImF6BFndqSAz56O6vfiAh8iD9P297DHfFgujec=";
    private static String VECTOR = "7S2tn363v0wfCfo1IX2Q1A==";

    class FileCapture {
        volatile boolean existed = false;
        volatile File file = null;
    }

    private Answer getAnswer(FileCapture fileCapture, Exception e) {
        return new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                File tmpFile = (File)invocation.getArgument(2);
                fileCapture.existed = tmpFile.exists();
                fileCapture.file = tmpFile;

                if (e != null) {
                    throw e;
                }

                return null;
            }
        };
    }

    private Answer getAnswer(FileCapture fileCapture) {
        return getAnswer(fileCapture, null);
    }

    @Test
    public void testSuccess() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "SPAPI.*\\.tmp$";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);

        FileCapture fileCapture = new FileCapture();
        Mockito.doAnswer(getAnswer(fileCapture)).when(httpTransferClient)
                .upload(Mockito.eq(url), Mockito.eq(contentType), Mockito.any());

        UploadHelper helper = new UploadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .build();

        UploadSpecification spec = new UploadSpecification.Builder(contentType, cryptoStreamFactory, source, url)
                .build();
        helper.upload(spec);

        assertTrue(fileCapture.file.getName().matches(expectedFileRegex));
        assertTrue(fileCapture.existed);
        assertFalse(fileCapture.file.exists());

        Mockito.verify(httpTransferClient).upload(Mockito.eq(url), Mockito.eq(contentType), Mockito.any());
    }

    @Test
    public void testSuccessWithCustomTmpFileParams() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = "http://www.abc.com/123";
        String expectedFileRegex = "NOTSPAPI.*\\.NOTtmp$";
        String expectedFilePath = "spapitmp";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);

        FileCapture fileCapture = new FileCapture();
        Mockito.doAnswer(getAnswer(fileCapture)).when(httpTransferClient)
                .upload(Mockito.eq(url), Mockito.eq(contentType), Mockito.any());

        File tmpDir = new File(Paths.get(System.getProperty("java.io.tmpdir"), expectedFilePath)
                .toAbsolutePath().toString());
        tmpDir.mkdirs();

        UploadHelper helper = new UploadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .withTmpFilePrefix("NOTSPAPI")
                .withTmpFileSuffix(".NOTtmp")
                .withTmpFileDirectory(tmpDir)
                .build();

        UploadSpecification spec = new UploadSpecification.Builder(contentType, cryptoStreamFactory, source, url)
                .build();
        helper.upload(spec);

        assertTrue(fileCapture.file.getName().matches(expectedFileRegex));
        assertEquals(expectedFilePath, fileCapture.file.getParentFile().getName());
        assertTrue(fileCapture.existed);
        assertFalse(fileCapture.file.exists());

        Mockito.verify(httpTransferClient).upload(Mockito.eq(url), Mockito.eq(contentType), Mockito.any());
    }

    @Test
    public void testFileDeletedOnFailure() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = "http://www.abc.com/123";

        HttpTransferClient httpTransferClient = Mockito.mock(HttpTransferClient.class);

        FileCapture fileCapture = new FileCapture();
        Mockito.doAnswer(getAnswer(fileCapture, new IllegalArgumentException())).when(httpTransferClient)
                .upload(Mockito.eq(url), Mockito.eq(contentType), Mockito.any());

        UploadHelper helper = new UploadHelper.Builder()
                .withHttpTransferClient(httpTransferClient)
                .build();

        UploadSpecification spec = new UploadSpecification.Builder(contentType, cryptoStreamFactory, source, url)
                .build();
        assertThrows(IllegalArgumentException.class, () -> helper.upload(spec));

        assertTrue(fileCapture.existed);
        assertFalse(fileCapture.file.exists());

        Mockito.verify(httpTransferClient).upload(Mockito.eq(url), Mockito.eq(contentType), Mockito.any());
    }

    @Test
    public void testBadUrlFailedUpload() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        CryptoStreamFactory cryptoStreamFactory = new AESCryptoStreamFactory.Builder(KEY, VECTOR).build();
        InputStream source = new ByteArrayInputStream(new byte[0]);
        String url = "sdfkajsdfiefi";

        Path path = Files.createTempDirectory("SPAPI");
        try {
            UploadHelper helper = new UploadHelper.Builder()
                    .withTmpFileDirectory(path.toFile())
                    .build();

            UploadSpecification spec = new UploadSpecification.Builder(contentType, cryptoStreamFactory, source, url)
                    .build();
            assertThrows(IllegalArgumentException.class, () -> helper.upload(spec));
        } finally {
            // This will throw if the directory is not empty, indicating that the temp file was not removed
            Files.delete(path);
        }
    }

    @Test
    public void testInvalidTmpFilePrefix() {
        UploadHelper.Builder builder = new UploadHelper.Builder();

        assertThrows(IllegalArgumentException.class, () -> builder
                .withTmpFilePrefix("A"));
    }
}