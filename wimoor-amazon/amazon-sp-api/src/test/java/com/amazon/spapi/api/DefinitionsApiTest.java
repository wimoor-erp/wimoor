
package com.amazon.spapi.api;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.amazon.spapi.model.definitions.ProductTypeList;

/**
* API tests for DefinitionsApi
*/
@Ignore
public class DefinitionsApiTest {

private final DefinitionsApi api = new DefinitionsApi();


/**
* 
*
* Retrieve an Amazon product type definition.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that wer ApiException if the A

Selling Partner API](doc and Rate Limits in the

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.amazon.spapi.model.definitions.ProductTypeList;ils
*/
@Test
public void getDefinitionsProductTypeTest() throws ApiException, LWAException {
    String productType = null;
    List<String> marketplaceIds = null;
    String sellerId = null;
    String productTypeVersion = null;
    String requirements = null;
    String requirementsEnforced = null;
    String locale = null;
ProductTypeDefinition response = api.getDefinitionsProductType(productType, marketplaceIds, sellerId, productTypeVersion, requirements, requirementsEnforced, locale);

// TODO: test validations
}

/**
* 
*
* Search for and return a list of Amazon product types that have definitions available.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
*
* @throws ApiException if the Api call fails
* @throws LWAException If calls to fetch LWA access token fails
*/
@Test
public void searchDefinitionsProductTypesTest() throws ApiException, LWAException {
    List<String> marketplaceIds = null;
    List<String> keywords = null;
    String itemName = null;
    String locale = null;
    String searchLocale = null;
ProductTypeList response = api.searchDefinitionsProductTypes(marketplaceIds, keywords, itemName, locale, searchLocale);

// TODO: test validations
}

}