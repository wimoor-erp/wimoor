package com.amazon.spapi.SellingPartnerAPIAA;

public interface RateLimitConfiguration {

    Double getRateLimitPermit();

    Long getTimeOut();

    String proxyHost();

    Integer proxyPort();
}
