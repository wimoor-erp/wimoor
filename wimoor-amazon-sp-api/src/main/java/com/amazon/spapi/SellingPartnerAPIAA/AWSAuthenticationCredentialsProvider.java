package com.amazon.spapi.SellingPartnerAPIAA;

import lombok.Builder;
import lombok.Data;

/**
 * AWSAuthenticationCredentialsProvider
 */
@Data
@Builder
public class AWSAuthenticationCredentialsProvider {
    /**
     * AWS IAM Role ARN
     */
    private String roleArn;

    /**
     * AWS IAM Role Session Name
     */
    private String roleSessionName;


}
