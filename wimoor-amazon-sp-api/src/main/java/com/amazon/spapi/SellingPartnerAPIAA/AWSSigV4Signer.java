package com.amazon.spapi.SellingPartnerAPIAA;

import com.amazonaws.SignableRequest;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.squareup.okhttp.Request;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;

/**
 * AWS Signature Version 4 Signer
 */
public class AWSSigV4Signer {
    private static final String SERVICE_NAME = "execute-api";

    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private AWS4Signer aws4Signer;

    private AWSCredentials awsCredentials;

    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private AWSCredentialsProvider awsCredentialsProvider;

    /**
     *
     * @param awsAuthenticationCredentials AWS Developer Account Credentials
     */
    public AWSSigV4Signer(AWSAuthenticationCredentials awsAuthenticationCredentials) {
        aws4Signer = new AWS4Signer();
        aws4Signer.setServiceName(SERVICE_NAME);
        aws4Signer.setRegionName(awsAuthenticationCredentials.getRegion());
        awsCredentials = new BasicAWSCredentials(awsAuthenticationCredentials.getAccessKeyId(),
                awsAuthenticationCredentials.getSecretKey());
    }

    /**
    *
    * @param awsAuthenticationCredentials and awsAuthenticationCredentialsProvider AWS Developer Account Credentials
    */
   public AWSSigV4Signer(AWSAuthenticationCredentials awsAuthenticationCredentials,
            AWSAuthenticationCredentialsProvider awsAuthenticationCredentialsProvider) {
       aws4Signer = new AWS4Signer();
       aws4Signer.setServiceName(SERVICE_NAME);
       aws4Signer.setRegionName(awsAuthenticationCredentials.getRegion());
       BasicAWSCredentials awsBasicCredentials = new BasicAWSCredentials(awsAuthenticationCredentials.getAccessKeyId(),
               awsAuthenticationCredentials.getSecretKey());
       awsCredentialsProvider = new STSAssumeRoleSessionCredentialsProvider.Builder(
               awsAuthenticationCredentialsProvider.getRoleArn(),
               awsAuthenticationCredentialsProvider.getRoleSessionName())
                       .withStsClient(AWSSecurityTokenServiceClientBuilder.standard()
                       .withRegion(awsAuthenticationCredentials.getRegion())
                       .withCredentials(new AWSStaticCredentialsProvider(awsBasicCredentials)).build())
                       .build();
   }

    /**
     *  Signs a Request with AWS Signature Version 4
     *
     * @param originalRequest Request to sign (treated as immutable)
     * @return Copy of originalRequest with AWS Signature
     */
    public Request sign(Request originalRequest) {
        SignableRequest<Request> signableRequest = new SignableRequestImpl(originalRequest);
        if (awsCredentialsProvider != null) {
            aws4Signer.sign(signableRequest, awsCredentialsProvider.getCredentials());
        } else {
            aws4Signer.sign(signableRequest, awsCredentials);
        }
        return (Request) signableRequest.getOriginalRequestObject();
    }
    }
