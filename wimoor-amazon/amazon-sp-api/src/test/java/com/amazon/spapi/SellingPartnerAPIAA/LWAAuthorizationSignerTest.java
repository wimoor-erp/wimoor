package com.amazon.spapi.SellingPartnerAPIAA;

import com.amazon.spapi.SellingPartnerAPIAA.LWAAccessTokenCache;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAccessTokenCacheImpl;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAccessTokenRequestMeta;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAuthorizationSigner;
import com.amazon.spapi.SellingPartnerAPIAA.LWAClient;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static com.amazon.spapi.SellingPartnerAPIAA.ScopeConstants.SCOPE_MIGRATION_API;
import static com.amazon.spapi.SellingPartnerAPIAA.ScopeConstants.SCOPE_NOTIFICATIONS_API;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LWAAuthorizationSignerTest {
    private static final String TEST_REFRESH_TOKEN = "rToken";
    private static final String TEST_CLIENT_SECRET = "cSecret";
    private static final String TEST_CLIENT_ID = "cId";
    private static final String TEST_ENDPOINT = "https://www.amazon.com/lwa";
    private static final String TEST_SCOPE_1 = SCOPE_NOTIFICATIONS_API;
    private static final String TEST_SCOPE_2 = SCOPE_MIGRATION_API;
    private static final String SELLER_TYPE_SELLER = "seller";
    private static final String SELLER_TYPE_SELLERLESS = "sellerless";
    private static final MediaType EXPECTED_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");


    private Request request;
    private static LWAAuthorizationSigner underTestSeller;
    private static LWAAuthorizationSigner underTestSellerless;
    
    @Mock
    private OkHttpClient mockOkHttpClient;

    @Mock
    private Call mockCall;

    static {

        underTestSeller = new LWAAuthorizationSigner(LWAAuthorizationCredentials.builder()
                .clientId(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .refreshToken(TEST_REFRESH_TOKEN)
                .endpoint(TEST_ENDPOINT)
                .build());

        underTestSellerless = new LWAAuthorizationSigner(LWAAuthorizationCredentials.builder()
                .clientId(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .withScopes(TEST_SCOPE_1, TEST_SCOPE_2)
                .endpoint(TEST_ENDPOINT)
                .build());
    }

    @Before @BeforeEach
    public void init() {
        request = new Request.Builder()
                .url("https://www.amazon.com/api")
                .build();
        MockitoAnnotations.initMocks(this);

    }

    public static Stream<Arguments> lwaAuthSigner(){

        return Stream.of(
                Arguments.of(SELLER_TYPE_SELLER, underTestSeller),
                Arguments.of(SELLER_TYPE_SELLERLESS, underTestSellerless)
        );
    }

    @ParameterizedTest
    @MethodSource("lwaAuthSigner")
    public void initializeLWAClientWithConfiguredEndpoint(String sellerType, LWAAuthorizationSigner testAuthSigner) {
        assertEquals(TEST_ENDPOINT, testAuthSigner.getLwaClient().getEndpoint());
    }

    @ParameterizedTest
    @MethodSource("lwaAuthSigner")
    public void requestLWAAccessTokenFromConfiguration(String sellerType, LWAAuthorizationSigner testAuthSigner) {
        LWAClient mockLWAClient = mock(LWAClient.class);
        ArgumentCaptor<LWAAccessTokenRequestMeta> lwaAccessTokenRequestMetaArgumentCaptor = ArgumentCaptor.forClass(LWAAccessTokenRequestMeta.class);

        when(mockLWAClient.getAccessToken(lwaAccessTokenRequestMetaArgumentCaptor.capture()))
                .thenReturn("foo");

        testAuthSigner.setLwaClient(mockLWAClient);
        testAuthSigner.sign(request);

        LWAAccessTokenRequestMeta actualLWAAccessTokenRequestMeta = lwaAccessTokenRequestMetaArgumentCaptor.getValue();
        assertEquals(TEST_REFRESH_TOKEN, actualLWAAccessTokenRequestMeta.getRefreshToken());
        assertEquals(TEST_CLIENT_SECRET, actualLWAAccessTokenRequestMeta.getClientSecret());
        assertEquals(TEST_CLIENT_ID, actualLWAAccessTokenRequestMeta.getClientId());

        if(sellerType.equals(SELLER_TYPE_SELLER)){
            Assert.assertTrue(actualLWAAccessTokenRequestMeta.getScopes().getScopes().isEmpty());
            assertEquals("refresh_token", actualLWAAccessTokenRequestMeta.getGrantType());
        }
        else if (sellerType.equals(SELLER_TYPE_SELLERLESS)){
            assertEquals(new HashSet<String>(Arrays.asList(TEST_SCOPE_1, TEST_SCOPE_2)), actualLWAAccessTokenRequestMeta.getScopes().getScopes());
            assertEquals("client_credentials", actualLWAAccessTokenRequestMeta.getGrantType());
        }

    }

    @ParameterizedTest
    @MethodSource("lwaAuthSigner")
    public void returnSignedRequestWithAccessTokenFromLWAClient(String sellerType, LWAAuthorizationSigner testAuthSigner) {
        LWAClient mockLWAClient = mock(LWAClient.class);

        when(mockLWAClient.getAccessToken(any(LWAAccessTokenRequestMeta.class)))
                .thenReturn("Azta|Foo");

        testAuthSigner.setLwaClient(mockLWAClient);
        Request actualSignedRequest = testAuthSigner.sign(request);

        assertEquals("Azta|Foo", actualSignedRequest.header("x-amz-access-token"));
    }

    @ParameterizedTest
    @MethodSource("lwaAuthSigner")
    public void originalRequestIsImmutable(String sellerType, LWAAuthorizationSigner testAuthSigner) {
        LWAClient mockLWAClient = mock(LWAClient.class);

        when(mockLWAClient.getAccessToken(any(LWAAccessTokenRequestMeta.class)))
                .thenReturn("Azta|Foo");

        testAuthSigner.setLwaClient(mockLWAClient);
        assertNotSame(request, testAuthSigner.sign(request));
    }
    
    @Test
    public void returnSignedRequestWithAccessTokenFromLWACache() throws IOException { 
        LWAClient testLWAClient = new LWAClient(TEST_ENDPOINT);
        testLWAClient.setOkHttpClient(mockOkHttpClient);
        
        when(mockOkHttpClient.newCall(any(Request.class)))
        .thenReturn(mockCall);
        when(mockCall.execute())     
        .thenReturn(buildResponse(200, "Azta|foo", "120"))
        .thenReturn(buildResponse(200, "Azta|foo1", "1"));

        LWAAccessTokenCache testLWACache = new LWAAccessTokenCacheImpl();        
        LWAAuthorizationSigner testlwaSigner = new LWAAuthorizationSigner(LWAAuthorizationCredentials.builder()
                .clientId(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .refreshToken(TEST_REFRESH_TOKEN)
                .endpoint(TEST_ENDPOINT)
                .build() , testLWACache );  
        
        testlwaSigner.setLwaClient(testLWAClient);
        testLWAClient.setLWAAccessTokenCache(testLWACache);
        Request actualSignedRequest = testlwaSigner.sign(request);
        Request actualSignedSecondRequest = testlwaSigner.sign(request);

        assertEquals("Azta|foo", actualSignedSecondRequest.header("x-amz-access-token"));
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
}
