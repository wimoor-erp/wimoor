package com.amazon.spapi.SellingPartnerAPIAA;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RateLimitConfigurationOnRequests implements RateLimitConfiguration {

    /**
     * RateLimiter Permit
     */
    private Double rateLimitPermit;

    /**
     * Timeout for RateLimiter
     */
    private Long waitTimeOutInMilliSeconds;

    @Override
    public Long getTimeOut() {
        return waitTimeOutInMilliSeconds;
    }

    @Override
    public Double getRateLimitPermit() {
        return rateLimitPermit;
    }

}
