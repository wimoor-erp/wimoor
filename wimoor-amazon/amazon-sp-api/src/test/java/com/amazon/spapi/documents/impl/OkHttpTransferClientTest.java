package com.amazon.spapi.documents.impl;

import com.amazon.spapi.documents.exception.HttpResponseException;
import com.squareup.okhttp.*;
import okio.Buffer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class OkHttpTransferClientTest {

    @Test
    public void testDefaultBuilder() throws Exception {
        OkHttpTransferClient helper = new OkHttpTransferClient.Builder().build();
        OkHttpClient client = (OkHttpClient)ReflectionUtils.tryToReadFieldValue(OkHttpTransferClient.class,
                "client", helper).get();
    }

    @Test
    public void testDownloadSuccess() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        String url = "https://www.s3-amazon.com/123";
        String content = "Hello world!";

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);

        ArgumentCaptor<Request> requestArg = ArgumentCaptor.forClass(Request.class);
        Mockito.doReturn(call).when(client).newCall(requestArg.capture());

        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(response).when(call).execute();

        Mockito.doReturn(contentType).when(response).header("Content-Type");

        Mockito.doReturn(true).when(response).isSuccessful();

        ResponseBody responseBody = Mockito.mock(ResponseBody.class);
        Mockito.doReturn(responseBody).when(response).body();

        Mockito.doReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)))
                .when(responseBody).byteStream();

        OkHttpTransferClient helper = new OkHttpTransferClient.Builder()
                .withClient(client)
                .build();

        File tmpFile = File.createTempFile("foo", null);
        try {
            assertEquals(contentType, helper.download(url, tmpFile));
            assertTrue(IOUtils.contentEquals(new FileInputStream(tmpFile),
                    new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))));
        } finally {
            tmpFile.delete();
        }

        assertEquals(url, requestArg.getValue().urlString());
        assertEquals("GET", requestArg.getValue().method());
        Mockito.verify(responseBody).close();
    }

    @Test
    public void testDownloadIOException() throws Exception {
        String url = "https://www.s3-amazon.com/123";

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);

        ArgumentCaptor<Request> requestArg = ArgumentCaptor.forClass(Request.class);
        Mockito.doReturn(call).when(client).newCall(requestArg.capture());

        Mockito.doThrow(new IOException()).when(call).execute();

        OkHttpTransferClient helper = new OkHttpTransferClient.Builder()
                .withClient(client)
                .build();

        File tmpFile = File.createTempFile("foo", null);
        try {
            Assertions.assertThrows(IOException.class, () -> helper.download(url, tmpFile));
        } finally {
            tmpFile.delete();
        }
    }

    private void failureResponseMocks(Response response, ResponseBody responseBody,
                                      int httpStatusCode, String message, String bodyContent) throws Exception {
        Mockito.doReturn(httpStatusCode).when(response).code();
        Mockito.doReturn(message).when(response).message();

        Mockito.doReturn(new StringReader(bodyContent)).when(responseBody)
                .charStream();
    }

    @Test
    public void testDownloadFailure() throws Exception {
        String url = "https://www.s3-amazon.com/123";
        String responseBodyText = "This is the response body";
        int maxErrorBodyLen = 5;
        int httpStatusCode = 403;
        String message = "Forbidden";

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);

        ArgumentCaptor<Request> requestArg = ArgumentCaptor.forClass(Request.class);
        Mockito.doReturn(call).when(client).newCall(requestArg.capture());

        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(response).when(call).execute();

        ResponseBody responseBody = Mockito.mock(ResponseBody.class);
        Mockito.doReturn(responseBody).when(response).body();
        Mockito.doNothing().when(responseBody).close();

        failureResponseMocks(response, responseBody, httpStatusCode, message, responseBodyText);

        Mockito.doReturn(false).when(response).isSuccessful();

        OkHttpTransferClient helper = new OkHttpTransferClient.Builder()
                .withClient(client)
                .withMaxErrorBodyLen(maxErrorBodyLen)
                .build();

        File tmpFile = File.createTempFile("foo", null);
        try {
            helper.download(url, tmpFile);
            fail("Expected exception");
        } catch(HttpResponseException e) {
            assertEquals(httpStatusCode, e.getCode());
            assertEquals(message, e.getMessage());
            assertEquals("This ", e.getBody());
        } finally {
            tmpFile.delete();
        }

        Mockito.verify(responseBody).close();
    }

    @Test
    public void testFailureInBodyExtraction() throws Exception {
        String url = "https://www.s3-amazon.com/123";
        int maxErrorBodyLen = 5;
        int httpStatusCode = 403;
        String message = "Forbidden";

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);

        ArgumentCaptor<Request> requestArg = ArgumentCaptor.forClass(Request.class);
        Mockito.doReturn(call).when(client).newCall(requestArg.capture());

        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(response).when(call).execute();

        ResponseBody responseBody = Mockito.mock(ResponseBody.class);
        Mockito.doReturn(responseBody).when(response).body();
        Mockito.doNothing().when(responseBody).close();

        Mockito.doReturn(httpStatusCode).when(response).code();
        Mockito.doReturn(message).when(response).message();

        Mockito.doThrow(new IOException()).when(responseBody).charStream();

        Mockito.doReturn(false).when(response).isSuccessful();

        OkHttpTransferClient helper = new OkHttpTransferClient.Builder()
                .withClient(client)
                .withMaxErrorBodyLen(maxErrorBodyLen)
                .build();

        File tmpFile = File.createTempFile("foo", null);
        try {
            helper.download(url, tmpFile);
            fail("Expected exception");
        } catch(HttpResponseException e) {
            assertEquals(httpStatusCode, e.getCode());
            assertEquals(message, e.getMessage());
            assertEquals("", e.getBody());
        } finally {
            tmpFile.delete();
        }

        Mockito.verify(responseBody).close();
    }

    @Test
    public void testUploadSuccess() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        String url = "https://www.s3-amazon.com/123";
        String content = "Hello world!";

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);

        ArgumentCaptor<Request> requestArg = ArgumentCaptor.forClass(Request.class);
        Mockito.doReturn(call).when(client).newCall(requestArg.capture());

        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(response).when(call).execute();

        Mockito.doReturn(true).when(response).isSuccessful();

        ResponseBody responseBody = Mockito.mock(ResponseBody.class);
        Mockito.doReturn(responseBody).when(response).body();
        Mockito.doNothing().when(responseBody).close();

        OkHttpTransferClient helper = new OkHttpTransferClient.Builder()
                .withClient(client)
                .build();

        File tmpFile = File.createTempFile("foo", null);
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(tmpFile)) {
                fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            }

            helper.upload(url, contentType, tmpFile);

            Buffer buffer = new Buffer();
            requestArg.getValue().body().writeTo(buffer);
            assertEquals(content, buffer.readString(StandardCharsets.UTF_8));

            assertEquals(url, requestArg.getValue().urlString());
            assertEquals("PUT", requestArg.getValue().method());
            assertEquals(contentType, requestArg.getValue().body().contentType().toString());
        } finally {
            tmpFile.delete();
        }

        Mockito.verify(responseBody).close();
    }

    @Test
    public void testUploadIOException() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        String url = "https://www.s3-amazon.com/123";
        String content = "Hello world!";

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);

        ArgumentCaptor<Request> requestArg = ArgumentCaptor.forClass(Request.class);
        Mockito.doReturn(call).when(client).newCall(requestArg.capture());

        Mockito.doThrow(new IOException()).when(call).execute();

        OkHttpTransferClient helper = new OkHttpTransferClient.Builder()
                .withClient(client)
                .build();

        File tmpFile = File.createTempFile("foo", null);
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(tmpFile)) {
                fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            }

            Assertions.assertThrows(IOException.class, () -> helper.upload(url, contentType, tmpFile));
        } finally {
            tmpFile.delete();
        }
    }

    @Test
    public void testUploadFailure() throws Exception {
        String contentType = "text/xml; charset=UTF-8";
        String url = "https://www.s3-amazon.com/123";
        String content = "Hello world!";

        String responseBodyText = "This is the response body";
        int maxErrorBodyLen = 5;
        int httpStatusCode = 403;
        String message = "Forbidden";

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);

        ArgumentCaptor<Request> requestArg = ArgumentCaptor.forClass(Request.class);
        Mockito.doReturn(call).when(client).newCall(requestArg.capture());

        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(response).when(call).execute();

        Mockito.doReturn(false).when(response).isSuccessful();

        ResponseBody responseBody = Mockito.mock(ResponseBody.class);
        Mockito.doReturn(responseBody).when(response).body();
        Mockito.doNothing().when(responseBody).close();
        failureResponseMocks(response, responseBody, httpStatusCode, message, responseBodyText);

        OkHttpTransferClient helper = new OkHttpTransferClient.Builder()
                .withClient(client)
                .withMaxErrorBodyLen(maxErrorBodyLen)
                .build();

        File tmpFile = File.createTempFile("foo", null);
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(tmpFile)) {
                fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            }

            helper.upload(url, contentType, tmpFile);
            fail("Expected exception");
        } catch(HttpResponseException e) {
            assertEquals(httpStatusCode, e.getCode());
            assertEquals(message, e.getMessage());
            assertEquals("This ", e.getBody());
        } finally {
            tmpFile.delete();
        }

        Mockito.verify(responseBody).close();
    }
}