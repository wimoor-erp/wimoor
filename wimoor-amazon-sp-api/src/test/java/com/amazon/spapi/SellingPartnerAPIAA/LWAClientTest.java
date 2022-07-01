package com.amazon.spapi.SellingPartnerAPIAA;

 
import com.amazon.spapi.SellingPartnerAPIAA.LWAAccessTokenCacheImpl;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAccessTokenRequestMeta;
import com.amazon.spapi.SellingPartnerAPIAA.LWAClient;
import com.amazon.spapi.SellingPartnerAPIAA.LWAClientScopes;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
 
import okio.Buffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.amazon.spapi.SellingPartnerAPIAA.ScopeConstants.SCOPE_MIGRATION_API;
import static com.amazon.spapi.SellingPartnerAPIAA.ScopeConstants.SCOPE_NOTIFICATIONS_API;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LWAClientTest {
    private static final String TEST_ENDPOINT = "https://www.amazon.com/api";
    private static final MediaType EXPECTED_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static LWAAccessTokenRequestMeta lwaAccessTokenRequestMetaSeller;
    private static LWAAccessTokenRequestMeta lwaAccessTokenRequestMetaSellerless;

    private static final String TEST_SCOPE_1 = SCOPE_NOTIFICATIONS_API;
    private static final String TEST_SCOPE_2 = SCOPE_MIGRATION_API;

    private static final Set<String> scopesTestSellerless = new HashSet<String>(Arrays.asList(TEST_SCOPE_1,
            TEST_SCOPE_2));

    private static final String SELLER_TYPE_SELLER = "seller";
    private static final String SELLER_TYPE_SELLERLESS = "sellerless";
    
    @Mock
    private OkHttpClient mockOkHttpClient;

    @Mock
    private Call mockCall;
    
    private LWAClient underTest;

    static {
        lwaAccessTokenRequestMetaSeller = LWAAccessTokenRequestMeta.builder()
                .refreshToken("rToken")
                .clientId("cId")
                .clientSecret("cSecret")
                .grantType("rToken")
                .build();

        lwaAccessTokenRequestMetaSellerless = LWAAccessTokenRequestMeta.builder()
                .clientId("cId")
                .clientSecret("cSecret")
                .grantType("cCredentials")
                .scopes(new LWAClientScopes(scopesTestSellerless))
                .build(); 
    }

    @Before @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        underTest = new LWAClient(TEST_ENDPOINT);
        underTest.setOkHttpClient(mockOkHttpClient);
    }

    public static Stream<Arguments> lwaClient(){

        return Stream.of(
                Arguments.of(SELLER_TYPE_SELLER, lwaAccessTokenRequestMetaSeller),
                Arguments.of(SELLER_TYPE_SELLERLESS, lwaAccessTokenRequestMetaSellerless)
        );
    }

    @Test
    public void initializeEndpoint() {
        assertEquals(TEST_ENDPOINT, underTest.getEndpoint());
    }

    @ParameterizedTest
    @MethodSource("lwaClient")
    public void makeRequestFromMeta (String sellerType, LWAAccessTokenRequestMeta testLwaAccessTokenRequestMeta) throws IOException {
        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(mockOkHttpClient.newCall(requestArgumentCaptor.capture()))
                .thenReturn(mockCall);
        when(mockCall.execute())
                .thenReturn(buildResponse(200, "foo"));

        underTest.getAccessToken(testLwaAccessTokenRequestMeta);

        Request actualRequest = requestArgumentCaptor.getValue();
        assertEquals(TEST_ENDPOINT, actualRequest.url().toString());
        assertEquals("POST", actualRequest.method().toUpperCase());

        Buffer bodyBuffer = new Buffer();
        actualRequest.body().writeTo(bodyBuffer);
        JsonObject bodyJson = new JsonParser()
                .parse(new InputStreamReader(bodyBuffer.inputStream()))
                .getAsJsonObject();

        if (sellerType == SELLER_TYPE_SELLER) {
            assertEquals("rToken", bodyJson.get("refresh_token")
                    .getAsString());
        }
        else if (sellerType == SELLER_TYPE_SELLERLESS){
            assertNotNull(bodyJson.get("scope"));
        }
        assertEquals("cId", bodyJson.get("client_id").getAsString());
        assertEquals("cSecret", bodyJson.get("client_secret").getAsString());
        assertEquals(EXPECTED_MEDIA_TYPE, actualRequest.body().contentType());
    }

    @ParameterizedTest
    @MethodSource("lwaClient")
    public void returnAccessTokenFromResponse(String sellerType, LWAAccessTokenRequestMeta testLwaAccessTokenRequestMeta) throws IOException {
        when(mockOkHttpClient.newCall(any(Request.class)))
                .thenReturn(mockCall);
        when(mockCall.execute())
                .thenReturn(buildResponse(200, "Azta|foo"));

        assertEquals("Azta|foo", underTest.getAccessToken(testLwaAccessTokenRequestMeta));
    }

    @ParameterizedTest
    @MethodSource("lwaClient")
    public void unsuccessfulPostThrowsException(String sellerType, LWAAccessTokenRequestMeta testLwaAccessTokenRequestMeta) throws IOException {
        when(mockOkHttpClient.newCall(any(Request.class)))
                .thenReturn(mockCall);
        when(mockCall.execute())
                .thenReturn(buildResponse(400, "Azta|foo"));

        Assertions.assertThrows(RuntimeException.class, () -> {
            underTest.getAccessToken(testLwaAccessTokenRequestMeta);
        });
    }

    @ParameterizedTest
    @MethodSource("lwaClient")
    public void missingAccessTokenInResponseThrowsException(String sellerType, LWAAccessTokenRequestMeta testLwaAccessTokenRequestMeta) throws IOException {
        when(mockOkHttpClient.newCall(any(Request.class)))
                .thenReturn(mockCall);
        when(mockCall.execute())
                .thenReturn(buildResponse(200, ""));

        Assertions.assertThrows(RuntimeException.class, () -> {
            underTest.getAccessToken(testLwaAccessTokenRequestMeta);
                });
    }
    
    //Test for Access Token getting from cache
    @Test
    public void returnAccessTokenFromCache() throws IOException, InterruptedException {
        
        when(mockOkHttpClient.newCall(any(Request.class)))
        .thenReturn(mockCall);
        when(mockCall.execute())     
        .thenReturn(buildResponse(200, "Azta|foo", "120"))
        .thenThrow(IllegalStateException.class);
        underTest.setLWAAccessTokenCache(new LWAAccessTokenCacheImpl());
        
        //First call should get from Endpoint
        assertEquals("Azta|foo", underTest.getAccessToken(lwaAccessTokenRequestMetaSeller));
        //Second call when the cache is still valid, if it goes to end point it will return IllegalStateException.
        assertEquals("Azta|foo", underTest.getAccessToken(lwaAccessTokenRequestMetaSeller));
    }
    
    @Test
    public void returnAccessTokenFromCacheWithExpiry() throws IOException, InterruptedException {
        LWAClient client = new LWAClient(TEST_ENDPOINT);
        client.setOkHttpClient(mockOkHttpClient);
        when(mockOkHttpClient.newCall(any(Request.class)))
        .thenReturn(mockCall);
        when(mockCall.execute())     
        .thenReturn(buildResponse(200, "Azta|foo", "1"))
        .thenReturn(buildResponse(200, "Azta|foo1", "1"));
       
        //First call should get from Endpoint
        assertEquals("Azta|foo", client.getAccessToken(lwaAccessTokenRequestMetaSeller));
        //Second call should again go to the endpoint because accesstoken is expired after expiry adjustment.
        assertEquals("Azta|foo1", client.getAccessToken(lwaAccessTokenRequestMetaSeller));
    }
    
    private static Response buildResponse(int code, String accessToken, String expiryInSeconds) {
        ResponseBody responseBody = ResponseBody.create(EXPECTED_MEDIA_TYPE,
                String.format("{%s:%s,%s:%s}", "access_token", accessToken, "expires_in", expiryInSeconds));

        return new Response.Builder()
                .request(new Request.Builder().url(TEST_ENDPOINT).build())
                .code(code)
                .body(responseBody)
                .protocol(Protocol.HTTP_1_1)
                .message("OK")
                .build();
    }
    
    private static Response buildResponse(int code, String accessToken) {
        return buildResponse(code, accessToken, "3600");
    }
    
 
}
