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
    public String proxyHost() {
        return "";
    }

    @Override
    public Integer proxyPort() {
        return null;
    }

    @Override
    public Double getRateLimitPermit() {
        return null;
    }

}
