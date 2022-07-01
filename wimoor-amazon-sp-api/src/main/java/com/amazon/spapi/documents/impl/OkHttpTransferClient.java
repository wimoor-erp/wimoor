package com.amazon.spapi.documents.impl;

import com.amazon.spapi.documents.HttpTransferClient;
import com.amazon.spapi.documents.exception.HttpResponseException;
import com.squareup.okhttp.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * HTTP transfer client utilizing OkHttp.
 */
public class OkHttpTransferClient implements HttpTransferClient {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private final OkHttpClient client;
    private final int maxErrorBodyLen;

    private OkHttpTransferClient(OkHttpClient client, int maxErrorBodyLen) {
        this.client = client;
        this.maxErrorBodyLen = maxErrorBodyLen;
    }

    private HttpResponseException createResponseException(Response response) {
        String body = "";
        if (maxErrorBodyLen > 0) {
            try (Reader bodyReader = response.body().charStream()) {
                char[] buf = new char[maxErrorBodyLen];
                int charsRead = IOUtils.read(bodyReader, buf);
                if (charsRead > 0) {
                    body = new String(buf, 0, charsRead);
                }
            } catch (Exception e) {
                // Ignore any failures reading the body so that the original failure is not lost
            }
        }

        return new HttpResponseException(response.message(), body, response.code());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String download(String url, File destination) throws HttpResponseException, IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        try {
            if (!response.isSuccessful()) {
                throw createResponseException(response);
            }

            FileUtils.copyInputStreamToFile(response.body().byteStream(), destination);
        } finally {
            IOUtils.closeQuietly(response.body());
        }

        return response.header(CONTENT_TYPE_HEADER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upload(String url, String contentType, File source) throws HttpResponseException, IOException {
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.parse(contentType), source))
                .build();

        Response response = client.newCall(request).execute();
        try {
            if (!response.isSuccessful()) {
                throw createResponseException(response);
            }
        } finally {
            IOUtils.closeQuietly(response.body());
        }
    }

    /**
     * Use this to create an instance of an {@link OkHttpTransferClient}.
     */
    public static class Builder {
        private OkHttpClient client = null;
        private int maxErrorBodyLen = 4096;

        /**
         * The {@link OkHttpClient}. If not specified, a new instance of {@link OkHttpClient} will be created using the
         * no-arg constructor.
         *
         * @param client The client
         * @return this
         */
        public Builder withClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        /**
         * When an HTTP response indicates failure, the maximum number of characters for
         * {@link HttpResponseException#getBody()}. Default <code>4096</code>.
         *
         * @param maxErrorBodyLen The maximum number of characters to extract from the response body on failure
         * @return this
         */
        public Builder withMaxErrorBodyLen(int maxErrorBodyLen) {
            this.maxErrorBodyLen = maxErrorBodyLen;
            return this;
        }

        /**
         * Create the client.
         *
         * @return The client
         */
        public OkHttpTransferClient build() {
            if (client == null) {
                client = new OkHttpClient();
            }

            return new OkHttpTransferClient(client, maxErrorBodyLen);
        }
    }
}
