package com.amazon.spapi.SellingPartnerAPIAA;

import com.amazon.spapi.SellingPartnerAPIAA.SignableRequestImpl;
import com.amazonaws.http.HttpMethodName;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SignableRequestImplTest {
    private Request testRequest;
    private SignableRequestImpl underTest;

    @Before
    public void init() {
        testRequest = new Request.Builder()
                .url("http://www.amazon.com/request/library?test=true&sky=blue&right=右")
                .get()
                .build();

        underTest = new SignableRequestImpl(testRequest);
    }

    @Test
    public void getHttpMethod() {
        assertEquals(HttpMethodName.GET, underTest.getHttpMethod());

        underTest = new SignableRequestImpl(new Request.Builder()
                .url("https://www.amazon.com")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"foo\": \"bar\"}"))
                .build());

        assertEquals(HttpMethodName.POST, underTest.getHttpMethod());
    }

    @Test
    public void getOriginalRequestObject() {
        Request actualRequest = (Request)underTest.getOriginalRequestObject();

        assertNotSame(testRequest, actualRequest);
        assertEquals(testRequest.method(), actualRequest.method());
        assertEquals(testRequest.url(), actualRequest.url());
        assertEquals(testRequest.headers().toMultimap(), actualRequest.headers().toMultimap());
        assertEquals(testRequest.body(), actualRequest.body());
    }

    @Test
    public void getReadLimitInfo() {
        assertNull(underTest.getReadLimitInfo());
    }

    @Test
    public void getResourcePath() {
        assertEquals("/request/library", underTest.getResourcePath());
    }

    @Test
    public void noTimeOffset() {
        assertEquals(0, underTest.getTimeOffset());
    }

    @Test
    public void getEndpoint() {
        assertEquals(URI.create("http://www.amazon.com"), underTest.getEndpoint());
    }

    @Test
    public void headers() {
        Map<String, String> expectedHeaders = new HashMap<>();

        assertTrue(underTest.getHeaders().isEmpty());

        underTest.addHeader("foo", "bar");
        expectedHeaders.put("foo", "bar");
        assertEquals(expectedHeaders, underTest.getHeaders());

        underTest.addHeader("ban", "bop");
        expectedHeaders.put("ban", "bop");
        assertEquals(expectedHeaders, underTest.getHeaders());
    }

    @Test
    public void getParameters() {
        Map<String, List<String>> expectedParamters = new HashMap<>();
        expectedParamters.put("test", Collections.singletonList("true"));
        expectedParamters.put("sky", Collections.singletonList("blue"));
        expectedParamters.put("right", Collections.singletonList("右"));

        assertEquals(expectedParamters, underTest.getParameters());
    }

    @Test
    public void getContent() {
        String expectedContent = "{\"foo\":\"bar\"}";
        StringBuilder actualContent = new StringBuilder();

        underTest = new SignableRequestImpl(new Request.Builder()
                .url("https://www.amazon.com")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), expectedContent))
                .build());

        try(Scanner scanner = new Scanner(underTest.getContent())){
            while(scanner.hasNext()) {
                actualContent.append(scanner.next());
            }
        }

        assertEquals(expectedContent, actualContent.toString());
    }

    @Test
    public void getUnwrappedContent() {
        String expectedContent = "{\"ban\":\"bop\"}";
        StringBuilder actualContent = new StringBuilder();

        underTest = new SignableRequestImpl(new Request.Builder()
                .url("https://www.amazon.com")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), expectedContent))
                .build());

        try(Scanner scanner = new Scanner(underTest.getContentUnwrapped())){
            while(scanner.hasNext()) {
                actualContent.append(scanner.next());
            }
        }

        assertEquals(expectedContent, actualContent.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setContentNotSupported() {
        underTest.setContent(new ByteArrayInputStream("abc".getBytes()));
    }

    @Test
    public void addParameter() {
        underTest.addParameter("left", "左");

        HttpUrl actualHttpUrl = ((Request) underTest.getOriginalRequestObject())
                .httpUrl();

        assertEquals(Collections.singletonList("true"), actualHttpUrl.queryParameterValues("test"));
        assertEquals(Collections.singletonList("blue"), actualHttpUrl.queryParameterValues("sky"));
        assertEquals(Collections.singletonList("右"), actualHttpUrl.queryParameterValues("right"));
        assertEquals(Collections.singletonList("左"), actualHttpUrl.queryParameterValues("left"));
    }

    @Test
    public void gracefulBlankParametersParse() {
        testRequest = new Request.Builder()
                .url("http://www.amazon.com/request/library?  ")
                .get()
                .build();

        underTest = new SignableRequestImpl(testRequest);

        assertTrue(underTest.getParameters().isEmpty());
    }

    @Test
    public void gracefulIncompleteParameterPairsParse() {
        testRequest = new Request.Builder()
                .url("http://www.amazon.com/request/library?isSigned& =false")
                .get()
                .build();

        Map<String, List<String>> expected = new HashMap<>();
        expected.put("isSigned", Collections.singletonList(null));
        expected.put(" ", Collections.singletonList("false"));

        underTest = new SignableRequestImpl(testRequest);

        assertEquals(expected, underTest.getParameters());
    }

    @Test
    public void getHeadersIncludesContentTypeFromRequestBody() {
        String expected = "application/json; charset=utf-8";
        RequestBody requestBody = RequestBody.create(MediaType.parse(expected),
                "{\"foo\":\"bar\"}");

        testRequest = new Request.Builder()
                .url("http://www.amazon.com")
                .post(requestBody)
                .header("Content-Type", "THIS SHOULD BE OVERRIDDEN WITH REQUEST BODY CONTENT TYPE")
                .build();

        underTest = new SignableRequestImpl(testRequest);

        assertEquals(expected, underTest.getHeaders().get("Content-Type"));
    }

    @Test
    public void missingRequestBodyDoesNotOverwriteExistingContentTypeHeader() {
        String expected = "testContentType";

        testRequest = new Request.Builder()
                .url("http://www.amazon.com")
                .get()
                .header("Content-Type", expected)
                .build();

        underTest = new SignableRequestImpl(testRequest);

        assertEquals(expected, underTest.getHeaders().get("Content-Type"));
    }

    @Test
    public void missingRequestBodyContentTypeDoesNotOverwriteExistingContentTypeHeader() {
        String expected = "testContentType";

        testRequest = new Request.Builder()
                .url("http://www.amazon.com")
                .post(RequestBody.create(null, "foo"))
                .header("Content-Type", expected)
                .build();

        underTest = new SignableRequestImpl(testRequest);

        assertEquals(expected, underTest.getHeaders().get("Content-Type"));
    }
}
