package com.wimoor.amazon.product.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ProductPricingApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.productpricing.GetOffersResponse;
import com.amazon.spapi.model.productpricing.GetOffersResult;
import com.amazon.spapi.model.productpricing.GetPricingResponse;
import com.amazon.spapi.model.productpricing.OfferCustomerType;
import com.amazon.spapi.model.productpricing.OfferDetailList;
import com.amazon.spapi.model.productpricing.OfferType;
import com.amazon.spapi.model.productpricing.OffersList;
import com.amazon.spapi.model.productpricing.Price;
import com.amazon.spapi.model.productpricing.PriceList;
import com.amazon.spapi.model.productpricing.PriceType;
import com.amazon.spapi.model.productpricing.Product;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.squareup.okhttp.Call;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.mapper.ProductPriceMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.pojo.entity.ProductPrice;
import com.wimoor.amazon.product.pojo.entity.ProductPriceType;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCatalogItemService;
import com.wimoor.amazon.product.service.IProductFollowHandlerService;
import com.wimoor.amazon.product.service.IProductProductPriceService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.IPictureService;

@Service
public class ProductProductPriceServiceImpl implements IProductProductPriceService {
	@Resource
	private IProductInfoService iProductInfoService;
	@Resource
	private ProductInOptMapper productInOptMapper;
	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	IAmzProductRefreshService iAmzProductRefreshService;
	@Autowired
	IPictureService pictureService;
	@Autowired
	IProductCatalogItemService iProductCaptureCatalogItemService;
    @Autowired
    IMarketplaceService marketplaceService;
    @Autowired
	private ProductPriceMapper productPriceMapper;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IProductFollowHandlerService iProductFollowHandlerService;
	@Override
	public GetPricingResponse captureProductPrice(AmazonAuthority amazonAuthority, String sku, String  marketplaceid) {
		// TODO Auto-generated method stub
		  ProductPricingApi api = apiBuildService.getProductPricingApi(amazonAuthority);
		try {
			 GetPricingResponse response = api.getPricing(marketplaceid,"Sku", null, Arrays.asList(sku), null,null);
			return response;
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("接口调用错误："+e.getMessage());
		}
	}
	  
	  
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<AmzProductRefresh> skuRefreshlist =iAmzProductRefreshService.findForPriceRefresh(amazonAuthority.getId());
		if(skuRefreshlist!=null&&skuRefreshlist.size()>0){
			AmzProductRefresh skuRefresh=skuRefreshlist.get(0);
			String[] skuarray = skuRefresh.getSku().split(",");
			List<String> skulist = Arrays.asList(skuarray);
			captureProductPriceSync(amazonAuthority,skuRefresh,skulist);
		}
	}		
	
	@Override
	public Call captureProductPriceSync(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh,List<String> skulist) {
		// TODO Auto-generated method stub
		  amazonAuthority.setUseApi("getPricing");
		  ProductPricingApi api = apiBuildService.getProductPricingApi(amazonAuthority);
		try {
			if(amazonAuthority.apiNotRateLimit()) {
				ApiCallback<GetPricingResponse> callback =new  ApiCallbackGetPricing(this,amazonAuthority,amzProductRefresh);
				Call item = api.getPricingAsync(amzProductRefresh.getMarketplaceid(),"Sku", null, skulist, null,null, callback);
				return item;
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			amazonAuthority.setApiRateLimit(null, e);
		}
		return null;
	}

 
public GetOffersResponse getItemOffers(AmazonAuthority amazonAuthority, String asin, String  marketplaceid) {
		ProductPricingApi api = apiBuildService.getProductPricingApi(amazonAuthority);
		try {
			   GetOffersResponse response = api.getItemOffers(marketplaceid,"New", asin,null);
			return response;
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("接口调用错误："+e.getMessage());
		}
}
	 
public void handleResultItemOffers(AmazonAuthority amazonAuthority, String asin, String  marketplaceid, GetOffersResponse response) {
	  GetOffersResult result = response.getPayload();
	  OfferDetailList offers = result.getOffers();
	  iProductFollowHandlerService.recordFollowOfferChange(offers, asin, marketplaceid);
}
 


		@Override
		public void handlerResult(GetPricingResponse result, AmazonAuthority amazonAuthority,
				String marketplaceid) {
			// TODO Auto-generated method stub
			PriceList priceList = result.getPayload();
			for(Price productPrice:priceList) {
				String sku = productPrice.getSellerSKU();
				String amazonauthid=amazonAuthority.getId();
				List<ProductInfo> infolist = iProductInfoService.selectBySku(sku, marketplaceid, amazonauthid);
				if(infolist!=null&&infolist.size()>0) {
					ProductInfo info=infolist.get(0);
					Product product = productPrice.getProduct();
					AmzProductRefresh refresh = iAmzProductRefreshService.getById(info.getId());
					if(product==null) {
						refresh.setPriceRefreshTime(LocalDateTime.now());
					    refresh.setNotfound(false);
						iAmzProductRefreshService.updateById(refresh);		
						continue; 
					}
					OffersList offerlist = product.getOffers();
					if(offerlist==null) {
						refresh.setPriceRefreshTime(LocalDateTime.now());
					    refresh.setNotfound(false);
						iAmzProductRefreshService.updateById(refresh);		
						continue;
					}
					for(OfferType offer:offerlist) {
						offer.getBuyingPrice().getLandedPrice();
						ProductPrice price = new ProductPrice();
						price.setAsin(info.getAsin());
						price.setMarketplaceid(marketplaceid);
						price.setByday(new Date());
						price.setIsnewest(true);
						price.setFulfillmentchannel(offer.getFulfillmentChannel());
						price.setItemcondition(offer.getItemCondition());
						price.setItemsubcondition(offer.getItemSubCondition());
						price.setPtype(ProductPriceType.BuyPrice);
						PriceType buyPrice = offer.getBuyingPrice();
						if (buyPrice == null||
								(!offer.getItemCondition().equals("New"))||
								(offer.getOfferType()!=null&&offer.getOfferType().equals(OfferCustomerType.B2B))) {
							continue;
						}
						price.setListingAmount(buyPrice.getListingPrice().getAmount());
						price.setListingCurrency(buyPrice.getListingPrice().getCurrencyCode());
						price.setLandedAmount(buyPrice.getLandedPrice().getAmount());
						price.setLandedCurrency(buyPrice.getLandedPrice().getCurrencyCode());
						price.setShippingAmount(buyPrice.getShipping().getAmount());
						price.setShippingCurrency(buyPrice.getShipping().getCurrencyCode());
						price.setSellersku(offer.getSellerSKU());
						price.setSellerid(amazonAuthority.getSellerid());
						productPriceMapper.insert(price);
						ProductInOpt productInOpt = productInOptMapper.selectById(info.getId());
						info.setPrice(buyPrice.getLandedPrice().getAmount());
						iProductInfoService.updateById(info);
							if (productInOpt == null) {
								productInOpt = new ProductInOpt();
								productInOpt.setPid(new BigInteger(info.getId()));
								productInOpt.setLastupdate(new Date());
								productInOpt.setBuyprice(buyPrice.getLandedPrice().getAmount());
								productInOptMapper.insert(productInOpt);
							} else {
								productInOpt.setBuyprice(buyPrice.getLandedPrice().getAmount());
								productInOpt.setLastupdate(new Date());
								productInOptMapper.updateById(productInOpt);
							}
		      }
		    if(refresh!=null) {
		    	refresh.setPriceRefreshTime(LocalDateTime.now());
				iAmzProductRefreshService.updateById(refresh);		
		    }
			}
		}
	
		}
	
		@Override
		public void handlerFailure(AmazonAuthority auth, AmzProductRefresh skuRefresh, ApiException e) {
			// TODO Auto-generated method stub
			String[] skuarray = skuRefresh.getSku().split(",");
			List<String> skulist = Arrays.asList(skuarray);
		    if(e.getMessage().contains("Not Found")) {
		    	String responsebody=e.getResponseBody();
		    	for(String sku:skulist) {
		    		if(responsebody.contains(sku)&&responsebody.contains(skuRefresh.getMarketplaceid())) {
		    			AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
								.eq(AmzProductRefresh::getMarketplaceid, skuRefresh.getMarketplaceid())
								.eq(AmzProductRefresh::getSku,sku)
								.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
								);
		    			if(refresh!=null) {
		    				refresh.setNotfound(true);
			    			refresh.setPriceRefreshTime(LocalDateTime.now());
			    		 	iAmzProductRefreshService.updateById(refresh);
			    		 	ProductInfo info = iProductInfoService.getById(refresh.getPid());
			    		 	info.setInvalid(true);
			    		 	iProductInfoService.updateById(info);
		    			}
		    			
		    		}
		    	}
		    }else {
		    	for(String sku:skulist) {
		    			AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
								.eq(AmzProductRefresh::getMarketplaceid, skuRefresh.getMarketplaceid())
								.eq(AmzProductRefresh::getSku,sku)
								.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
								);
		    			if(refresh!=null) {
		    				refresh.setPriceRefreshTime(LocalDateTime.now());
		    				iAmzProductRefreshService.updateById(refresh);
		    			}
		    	}
		    }
		
		}

        boolean isrun=true;
		@Override
		public void runTask() {
			// TODO Auto-generated method stub
			ProductProductPriceServiceImpl self=this;
		    new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(isrun) {
						try {
							amazonAuthorityService.executTask(self);
							Thread.sleep(20000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		    }).start();
			
		}


		@Override
		public void stopTask() {
			// TODO Auto-generated method stub
			isrun=false;
		}
	 
}
