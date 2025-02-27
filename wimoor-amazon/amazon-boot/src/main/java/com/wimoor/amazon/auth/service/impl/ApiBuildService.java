package com.wimoor.amazon.auth.service.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazon.spapi.api.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazon.spapi.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.SellingPartnerAPIAA.ScopeConstants;
import com.amazon.spapi.client.ApiClient;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.tokens.CreateRestrictedDataTokenRequest;
import com.amazon.spapi.model.tokens.CreateRestrictedDataTokenResponse;
import com.amazon.spapi.model.tokens.RestrictedResource;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import cn.hutool.core.lang.Assert;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "auth")
public class ApiBuildService implements InitializingBean {

	@Setter
	private String appid;
	
    @Setter
    private String accessKeyId;
    
    @Setter
    private String secretKey;
    
    @Setter
    private String clientId;
    
    @Setter
    private String clientSecret;

    @Setter
    private String sandbox;
    
    @Setter
    private String needproxy;
    
    @Setter
    private String redirecturl;
    
    @Override
    public void afterPropertiesSet() {

        Assert.notBlank(clientId, "LWAAuthorizationCredentials clientId为空");
        Assert.notBlank(clientSecret, "LWAAuthorizationCredentials clientSecret为空");
 
    }
    
	public String getAccessKeyId() {
		return accessKeyId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getAppid() {
		return appid;
	}

	public String getSandbox() {
		return sandbox;
	}
	
	public String getRedirecturl() {
		return redirecturl;
	}
	
	public final static String endpoint="https://api.amazon.com/auth/o2/token";
 

 

	public LWAAuthorizationCredentials getLWAAuthorizationCredentials(AmazonAuthority auth) {
		   if(auth.getClientId()!=null) {
			   LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
			             //申请app后LWA中的clientId
			             .clientId(auth.getClientId().trim())
			             //申请app后LWA中的clientSecret
			             .clientSecret(auth.getClientSecret().trim())
			             //店铺授权时产生的refreshToken或者app自授权生成的
			             .refreshToken(auth.getRefreshToken())
			             .endpoint(endpoint)
			             .build();
			       return lwaAuthorizationCredentials;
		   }else {
			   LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
			             //申请app后LWA中的clientId
			             .clientId(clientId)
			             //申请app后LWA中的clientSecret
			             .clientSecret(clientSecret)
			             //店铺授权时产生的refreshToken或者app自授权生成的
			             .refreshToken(auth.getRefreshToken())
			             .endpoint(endpoint)
			             .build();
			       return lwaAuthorizationCredentials;
		   }
	      
	}
	
	public LWAAuthorizationCredentials getLWAAuthorizationCredentialsWithScope(AmazonAuthority auth,String scope) {
		 if(auth.getClientId()!=null) {
			 LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
		             //申请app后LWA中的clientId
		             .clientId(auth.getClientId())
		             //申请app后LWA中的clientSecret
		             .clientSecret(auth.getClientSecret())
		             .withScope(scope)
		             //店铺授权时产生的refreshToken或者app自授权生成的
		             .endpoint(endpoint)
		             .build();
		       return lwaAuthorizationCredentials;
		 }else {
			 LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
		             //申请app后LWA中的clientId
		             .clientId(clientId)
		             //申请app后LWA中的clientSecret
		             .clientSecret(clientSecret)
		             .withScope(scope)
		             //店铺授权时产生的refreshToken或者app自授权生成的
		             .endpoint(endpoint)
		             .build();
		       return lwaAuthorizationCredentials;
		 }
	       
	}
	
	private String getEndPoint(String region) {
		String endpoint="";
		sandbox="false";
		if(sandbox!=null&&"true".equals(sandbox)) {
			if("us-east-1".equals(region)) {
				endpoint="https://sandbox.sellingpartnerapi-na.amazon.com";
			}else if("eu-west-1".equals(region)) {
				endpoint="https://sandbox.sellingpartnerapi-eu.amazon.com";
			}else if("us-west-2".equals(region)){
				endpoint="https://sandbox.sellingpartnerapi-fe.amazon.com";
			}
		}else {
			if("us-east-1".equals(region)) {
				endpoint="https://sellingpartnerapi-na.amazon.com";
			}else if("eu-west-1".equals(region)) {
				endpoint="https://sellingpartnerapi-eu.amazon.com";
			}else if("us-west-2".equals(region)){
				endpoint="https://sellingpartnerapi-fe.amazon.com";
			}
		}
		
		return endpoint;
	}
	
	public String getRegion(String awsregion) {
		String region="";
			if("us-east-1".equals(awsregion)) {
				region="NA";
			}else if("eu-west-1".equals(awsregion)) {
				region="EU";
			}else if("us-west-2".equals(awsregion)){
				region="FE";
			}
		return region;
	}
	
	public ReportsApi  getReportsApi(AmazonAuthority auth) {
	                    ReportsApi api = new ReportsApi.Builder()
			                 .lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
			                 .endpoint(getEndPoint(auth.getAWSRegion()))
			                 .rateLimitConfigurationOnRequests(auth)
			                 .build();
	                     return api;
	     }
	
	public ListingsApi  getProductApi(AmazonAuthority auth) {
        ListingsApi api = new ListingsApi.Builder()
	            .lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
	            .endpoint(getEndPoint(auth.getAWSRegion()))
	            .rateLimitConfigurationOnRequests(auth)
	            .build();
		return api;
    }
	
	public OrdersV0Api  getOrdersV0Api(AmazonAuthority auth) {
		OrdersV0Api api = new OrdersV0Api.Builder()
            .lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
            .endpoint(getEndPoint(auth.getAWSRegion()))
            .rateLimitConfigurationOnRequests(auth)
            .build();
		    initClient(api.getApiClient());
        return api;
	}
    
	public FeedsApi  getFeedApi(AmazonAuthority auth) {
		  FeedsApi api = new FeedsApi.Builder()
            .lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
            .endpoint(getEndPoint(auth.getAWSRegion()))
            .rateLimitConfigurationOnRequests(auth)
            .build();
			initClient(api.getApiClient());
        return api;
	}
	
	public FbaInboundApi  getInboundApi(AmazonAuthority auth) {
		 FbaInboundApi api = new FbaInboundApi.Builder()
           .lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
           .endpoint(getEndPoint(auth.getAWSRegion()))
           .rateLimitConfigurationOnRequests(auth)
           .build();
		   initClient(api.getApiClient());
          return api;
	}
	public FbaOutboundApi  getOutboundApi(AmazonAuthority auth) {
		FbaOutboundApi api = new FbaOutboundApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
				.endpoint(getEndPoint(auth.getAWSRegion()))
				.rateLimitConfigurationOnRequests(auth)
				.build();
		initClient(api.getApiClient());
		return api;
	}

	public AplusContentApi  getAplusContentApi(AmazonAuthority auth) {
		AplusContentApi api = new AplusContentApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
				.endpoint(getEndPoint(auth.getAWSRegion()))
				.rateLimitConfigurationOnRequests(auth)
				.build();
		initClient(api.getApiClient());
		return api;
	}


	public FbaInboundV20240320Api  getInboundV20240320Api(AmazonAuthority auth) {
		FbaInboundV20240320Api api = new FbaInboundV20240320Api.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
				.endpoint(getEndPoint(auth.getAWSRegion()))
				.rateLimitConfigurationOnRequests(auth)
				.build();
		   initClient(api.getApiClient());
         return api;
	}
	
	public FbaInventoryApi  getFbaInventoryApi(AmazonAuthority auth) {
		FbaInventoryApi api = new FbaInventoryApi.Builder()
          .lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
          .endpoint(getEndPoint(auth.getAWSRegion()))
          .rateLimitConfigurationOnRequests(auth)
          .build();
         return api;
	}
	
	public AuthorizationApi  getAuthorizationApiGrantless(AmazonAuthority auth) {
		/*********************Grantless operations**************
		 *  createDestination	POST /notifications/v1/destinations
		 *	deleteDestination	DELETE /notifications/v1/destinations/{destinationId}
		 *	deleteSubscriptionById	DELETE /notifications/v2/subscriptions/{notificationType}/{subscriptionId}
		 *	getDestination	GET /notifications/v1/destinations/{destinationId}
		 *	getDestinations	GET /notifications/v1/destinations
		 *	getSubscriptionById	GET /notifications/v1/subscriptions/{notificationType}/{subscriptionId}
		 *	getAuthorizationCode
		 */
		AuthorizationApi api=new AuthorizationApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentialsWithScope(auth,ScopeConstants.SCOPE_MIGRATION_API))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
        return api;
	}
	
	public NotificationsApi getNotificationsApiGrantless(AmazonAuthority auth) {
	       NotificationsApi api=new NotificationsApi.Builder()
			.lwaAuthorizationCredentials(getLWAAuthorizationCredentialsWithScope(auth,ScopeConstants.SCOPE_NOTIFICATIONS_API))
	        .endpoint(getEndPoint(auth.getAWSRegion()))
	        .rateLimitConfigurationOnRequests(auth)
			.build();
           return api;
   }
	
	public DefinitionsApi GetDefinitionsApi(AmazonAuthority auth) {
		DefinitionsApi api = new DefinitionsApi.Builder()
			.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
	        .endpoint(getEndPoint(auth.getAWSRegion()))
	        .rateLimitConfigurationOnRequests(auth)
			.build();
		 return api;
	}

	public CatalogApi  getCatalogApi(AmazonAuthority auth) {
		CatalogApi api=new CatalogApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
        return api;
	}
	
	public NotificationsApi getNotificationsApi(AmazonAuthority auth) {
		NotificationsApi api=new NotificationsApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
		return api;
	}

	public FinancesApi getFinancesApi(AmazonAuthority auth) {
			FinancesApi api=new FinancesApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
			    initClient(api.getApiClient());
		  return api;
	 }
	
	public SmallAndLightApi getSmallAndLightApi(AmazonAuthority auth) {
		SmallAndLightApi api=new SmallAndLightApi.Builder()
			.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
	        .endpoint(getEndPoint(auth.getAWSRegion()))
	        .rateLimitConfigurationOnRequests(auth)
			.build();
		    initClient(api.getApiClient());
	  return api;
 }
	
	public TokensApi getTokensApi(AmazonAuthority auth) {
		TokensApi api=new TokensApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
		        initClient(api.getApiClient());
        return api;
	}
	

	public SellersApi getSellersApi(AmazonAuthority auth){
		SellersApi api=new SellersApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
        return api;
	}

	public ProductPricingApi getProductPricingApi(AmazonAuthority auth){
		ProductPricingApi api=new ProductPricingApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
        return api;
	}

	public MessagingApi getMessageApi(AmazonAuthority auth){
		MessagingApi api=new MessagingApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
				.endpoint(getEndPoint(auth.getAWSRegion()))
				.rateLimitConfigurationOnRequests(auth)
				.build();
		return api;
	}

	public UploadsApi getUploadsApi(AmazonAuthority auth){
		UploadsApi api=new UploadsApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
				.endpoint(getEndPoint(auth.getAWSRegion()))
				.rateLimitConfigurationOnRequests(auth)
				.build();
		return api;
	}

	public SolicitationsApi getSolicitationsApi(AmazonAuthority auth){
		SolicitationsApi api=new SolicitationsApi.Builder()
				.lwaAuthorizationCredentials(getLWAAuthorizationCredentials(auth))
		        .endpoint(getEndPoint(auth.getAWSRegion()))
		        .rateLimitConfigurationOnRequests(auth)
				.build();
		        initClient(api.getApiClient());
        return api;
	}

	private void initClient(ApiClient client) {
		client.setConnectTimeout(120000);
        client.setReadTimeout(120000);
        if(needproxy!=null&&needproxy.equals("true")) {
        	SocketAddress address=new InetSocketAddress("127.0.0.1",1080);
            Proxy proxy= new Proxy(Proxy.Type.SOCKS,address);
    		client.getHttpClient().setProxy(proxy);
        }
	}
	public static void initClient(OkHttpClient okHttpClient) {
		     okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
		     okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
	}
 
	 public   RestrictedResource buildRestrictedResource(RestrictedResource.MethodEnum method, String path, List<String> dataElements){
	     RestrictedResource resource = buildRestrictedResource(method,path);
	     resource.dataElements(dataElements);
	     return resource;
	 }
	 public   RestrictedResource buildRestrictedResource(RestrictedResource.MethodEnum method, String path){
	     RestrictedResource resource = new RestrictedResource();
	     resource.setMethod(method);
	     resource.setPath(path);
	     return resource;
	 }
	 
	 public   JsonObject getRestrictedData(AmazonAuthority amazonAuthority,String resourcePath,List<String> dataElements) throws ApiException, IOException  {
	          // Initialize a CreateRestrictedDataTokenRequest object that represents the Restricted Data Token request body.
		      RestrictedResource resource = buildRestrictedResource(RestrictedResource.MethodEnum.GET, resourcePath, dataElements);
		     // Make a list of the RestrictedResource objects that will be included in the request to create the RDT.
		     List<RestrictedResource> resourceList = Arrays.asList(resource);
		     // Get an RDT for the list of restricted resources.
		     String restrictedDataToken = getRestrictedDataToken(amazonAuthority,resourceList);
			 Response restrictedRequestResponse  = buildAndExecuteRestrictedRequest(amazonAuthority,resource, restrictedDataToken, null);
		     @SuppressWarnings("deprecation")
		     JsonObject responseBodyJson = new JsonParser().parse(restrictedRequestResponse.body().string()).getAsJsonObject();
			 return responseBodyJson;
	 }
	 
	 public   String getRestrictedDataToken(AmazonAuthority auth,List<RestrictedResource> resourceList) throws ApiException {
	     // Initialize a CreateRestrictedDataTokenRequest object that represents the Restricted Data Token request body.
	     CreateRestrictedDataTokenRequest restrictedDataTokenRequest = new CreateRestrictedDataTokenRequest();
	     // Add a resource list to the CreateRestrictedDataTokenRequest object.
	     restrictedDataTokenRequest.setRestrictedResources(resourceList);

	     try {
	         CreateRestrictedDataTokenResponse response =this.getTokensApi(auth).createRestrictedDataToken(restrictedDataTokenRequest);
	         String restrictedDataToken = response.getRestrictedDataToken();
	         return restrictedDataToken;
	     } catch (ApiException e) {
	         System.out.println(e.getResponseHeaders());  // Capture the response headers when a exception is thrown.
	         throw e;
	     } catch (LWAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return null;
	 }
	 
	 public   Response buildAndExecuteRestrictedRequest(AmazonAuthority auth,RestrictedResource resource, String restrictedDataToken, RequestBody requestBody) throws IOException {
	     // Construct a request with the specified RestrictedResource, RDT, and RequestBody.
	     Request signedRequest = new Request.Builder()
	             .url(getEndPoint(auth.getAWSRegion()) + resource.getPath())  // Define the URL for the request, based on the endpoint and restricted resource path.
	             .method(resource.getMethod().getValue(), requestBody)  // Define the restricted resource method value, and requestBody, if required by the restricted operation.
	             .addHeader("x-amz-access-token", restrictedDataToken) // Sign the request with the RDT by adding it to the "x-amz-access-token" header.
	             .build(); // Build the request.

	     // Execute the signed request.
	     OkHttpClient okHttpClient = new OkHttpClient();
	     Response response = okHttpClient.newCall(signedRequest).execute();
	     initClient(okHttpClient);
	     return response;
	 }

	
}
