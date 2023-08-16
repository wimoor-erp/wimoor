package com.amazon.spapi.SellingPartnerAPIAA;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * AWSAuthenticationCredentials
 */
@Data
@Builder
public class AWSAuthenticationCredentials {
    /**
     * AWS IAM User Access Key Id
     */
    @NonNull
    private String accessKeyId;

    /**
     * AWS IAM User Secret Key
     */
    @NonNull
    private String secretKey;

    /**
     * AWS Region
     */
    @NonNull
    private String region;
}
