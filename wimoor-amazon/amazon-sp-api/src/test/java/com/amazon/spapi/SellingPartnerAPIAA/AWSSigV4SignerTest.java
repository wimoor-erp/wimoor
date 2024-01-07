package com.amazon.spapi.SellingPartnerAPIAA;

import com.amazon.spapi.SellingPartnerAPIAA.AWSAuthenticationCredentials;
import com.amazon.spapi.SellingPartnerAPIAA.AWSAuthenticationCredentialsProvider;
import com.amazon.spapi.SellingPartnerAPIAA.AWSSigV4Signer;
import com.amazonaws.SignableRequest;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.squareup.okhttp.Request;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AWSSigV4SignerTest {
    private static final String TEST_ACCESS_KEY_ID = "aKey";
    private static final String TEST_SECRET_KEY = "sKey";
    private static final String TEST_REGION = "us-east";
    private static final String TEST_ROLE_ARN = "arnrole";
    private static final String TEST_ROLESESSION_NAME = "test";

    @Mock
    private AWS4Signer mockAWS4Signer;
    @Mock
    private AWSCredentialsProvider mockAWSCredentialsProvider;
    @Mock
    private AWSCredentials mockAWSCredentials;

    private AWSSigV4Signer underTest;
    private AWSSigV4Signer underTestCredentialsProvider;

    @Before
    public void init() {
        underTest = new AWSSigV4Signer(AWSAuthenticationCredentials.builder()
                .accessKeyId(TEST_ACCESS_KEY_ID)
                .secretKey(TEST_SECRET_KEY)
                .region(TEST_REGION)
                .build()
        );
    }

    @Test
    public void signRequestUsingProvidedCredentials() {
        ArgumentCaptor<AWSCredentials> awsCredentialsArgumentCaptor = ArgumentCaptor.forClass(AWSCredentials.class);
        underTest.setAws4Signer(mockAWS4Signer);

        doNothing()
                .when(mockAWS4Signer)
                .sign(any(SignableRequest.class), awsCredentialsArgumentCaptor.capture());

        underTest.sign(new Request.Builder().url("https://api.amazon.com").build());

        AWSCredentials actualAWSCredentials = awsCredentialsArgumentCaptor.getValue();

        assertEquals(TEST_ACCESS_KEY_ID, actualAWSCredentials.getAWSAccessKeyId());
        assertEquals(TEST_SECRET_KEY, actualAWSCredentials.getAWSSecretKey());
    }

    @Test
    public void setSignerRegion() {
        assertEquals(TEST_REGION, underTest.getAws4Signer().getRegionName());
    }

    @Test
    public void setSignerServiceName() {
        assertEquals("execute-api", underTest.getAws4Signer().getServiceName());
    }

    @Test
    public void returnSignedRequest() {
        ArgumentCaptor<SignableRequest> signableRequestArgumentCaptor = ArgumentCaptor.forClass(SignableRequest.class);
        underTest.setAws4Signer(mockAWS4Signer);

        Request actualSignedRequest = underTest.sign(new Request.Builder()
                .url("http://api.amazon.com")
                .build());

        verify(mockAWS4Signer)
                .sign(signableRequestArgumentCaptor.capture(), any(AWSCredentials.class));

        SignableRequest actualSignableRequest = signableRequestArgumentCaptor.getValue();

        assertEquals(((Request)actualSignableRequest.getOriginalRequestObject()).url(), actualSignedRequest.url());
    }
    
    @Test
    public void returnSignedRequestWithCredentialProvider() {
        ArgumentCaptor<SignableRequest> signableRequestArgumentCaptor = ArgumentCaptor.forClass(SignableRequest.class);
        
        Mockito.when(mockAWSCredentialsProvider.getCredentials()).thenReturn(mockAWSCredentials);
        
        underTestCredentialsProvider = new AWSSigV4Signer(AWSAuthenticationCredentials.builder()
                .accessKeyId(TEST_ACCESS_KEY_ID)
                .secretKey(TEST_SECRET_KEY)
                .region(TEST_REGION)
                .build(), AWSAuthenticationCredentialsProvider.builder()
                .roleArn(TEST_ROLE_ARN)
                .roleSessionName(TEST_ROLESESSION_NAME)
                .build()
        );
        underTestCredentialsProvider.setAws4Signer(mockAWS4Signer);
        underTestCredentialsProvider.setAwsCredentialsProvider(mockAWSCredentialsProvider);
        
        Request actualSignedRequest = underTestCredentialsProvider.sign(new Request.Builder()
                .url("http://api.amazon.com")
                .build());

        verify(mockAWS4Signer)
                .sign(signableRequestArgumentCaptor.capture(), any(AWSCredentials.class));

        SignableRequest actualSignableRequest = signableRequestArgumentCaptor.getValue();

        assertEquals(((Request)actualSignableRequest.getOriginalRequestObject()).url(), actualSignedRequest.url());
    }
    
   
}
