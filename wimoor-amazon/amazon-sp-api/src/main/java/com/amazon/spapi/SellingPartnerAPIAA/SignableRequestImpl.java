package com.amazon.spapi.SellingPartnerAPIAA;

import com.amazonaws.ReadLimitInfo;
import com.amazonaws.SignableRequest;
import com.amazonaws.http.HttpMethodName;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import okio.Buffer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SignableRequestImpl implements SignableRequest<Request> {
    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private Request originalRequest;
    private Request.Builder signableRequestBuilder;

    SignableRequestImpl(Request originalRequest) {
        this.originalRequest = originalRequest;
        signableRequestBuilder = originalRequest.newBuilder();
    }

    @Override
    public void addHeader(String name, String value) {
        signableRequestBuilder.addHeader(name, value);
    }

    @Override
    public void addParameter(String name, String value) {
        HttpUrl newUrl = signableRequestBuilder.build()
                .httpUrl()
                .newBuilder()
                .addEncodedQueryParameter(name, value)
                .build();

        signableRequestBuilder.url(newUrl);
    }

    @Override
    public void setContent(InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();

        Request requestSnapshot = signableRequestBuilder.build();
        requestSnapshot.headers()
                .names()
                .forEach(headerName -> headers.put(headerName, requestSnapshot.header(headerName)));

        if (requestSnapshot.body() != null) {
            MediaType contentType = requestSnapshot.body().contentType();
            if (contentType != null) {
                headers.put(CONTENT_TYPE_HEADER_NAME, contentType.toString());
            }
        }

        return headers;
    }

    @Override
    public String getResourcePath() {
        return originalRequest.url()
                .getPath();
    }

    @Override
    public Map<String, List<String>> getParameters() {
        Map<String, List<String>> parameters = new HashMap<>();
        try {
            List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(originalRequest.url().toURI(),
                    StandardCharsets.UTF_8);
            nameValuePairs.forEach(nameValuePair -> parameters.put(nameValuePair.getName(),
                    Collections.singletonList(nameValuePair.getValue())));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return parameters;
    }

    @Override
    public URI getEndpoint() {
        URI uri = null;
        try {
            uri = originalRequest.url().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return URI.create(String.format("%s://%s", uri.getScheme(), uri.getHost()));
    }

    @Override
    public HttpMethodName getHttpMethod() {
        return HttpMethodName.fromValue(originalRequest.method().toUpperCase());
    }

    @Override
    public int getTimeOffset() {
        return 0;
    }

    @Override
    public InputStream getContent() {
        ByteArrayInputStream inputStream = null;

        if (originalRequest.body() != null) {
            try {
                Buffer buffer = new Buffer();
                originalRequest.body().writeTo(buffer);
                inputStream = new ByteArrayInputStream(buffer.readByteArray());
            } catch (IOException e) {
                throw new RuntimeException("Unable to buffer request body", e);
            }
        }

        return inputStream;
    }

    @Override
    public InputStream getContentUnwrapped() {
        return getContent();
    }

    @Override
    public ReadLimitInfo getReadLimitInfo() {
        return null;
    }

    @Override
    public Object getOriginalRequestObject() {
        return signableRequestBuilder.build();
    }
}
