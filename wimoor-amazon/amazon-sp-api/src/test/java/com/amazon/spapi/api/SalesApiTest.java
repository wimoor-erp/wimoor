/*
 * Selling Partner API for Sales
 * The Selling Partner API for Sales provides APIs related to sales performance.
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amazon.spapi.api;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.sales.GetOrderMetricsResponse;

/**
* API tests for SalesApi
*/
@Ignore
public class SalesApiTest {

private final SalesApi api = new SalesApi();


/**
* 
*
* Returns aggregated order metrics for given interval, broken down by granularity, for given buyer type.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | .5 | 15 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
*
* @throws ApiException if the Api call fails
* @throws LWAException If calls to fetch LWA access token fails
*/
@Test
public void getOrderMetricsTest() throws ApiException, LWAException {
    List<String> marketplaceIds = null;
    String interval = null;
    String granularity = null;
    String granularityTimeZone = null;
    String buyerType = null;
    String fulfillmentNetwork = null;
    String firstDayOfWeek = null;
    String asin = null;
    String sku = null;
GetOrderMetricsResponse response = api.getOrderMetrics(marketplaceIds, interval, granularity, granularityTimeZone, buyerType, fulfillmentNetwork, firstDayOfWeek, asin, sku);

// TODO: test validations
}

}