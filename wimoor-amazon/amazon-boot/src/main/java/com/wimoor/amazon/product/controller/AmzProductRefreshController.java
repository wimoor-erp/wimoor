package com.wimoor.amazon.product.controller;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.spapi.model.catalogitems.ItemSearchResults;
import com.amazon.spapi.model.listings.Item;
import com.amazon.spapi.model.productpricing.GetPricingResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.pojo.dto.ProductCatalogItemsDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCatalogItemService;
import com.wimoor.amazon.product.service.IProductListingSmallAndLightService;
import com.wimoor.amazon.product.service.IProductListingsItemService;
import com.wimoor.amazon.product.service.IProductProductPriceService;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Api(tags = "产品任务接口")
@RestController
@Component("amzProductRefreshController")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/amzProductRefresh")
public class AmzProductRefreshController {

	final IAmzProductRefreshService iAmzProductRefreshService;
	final IProductListingsItemService iProductListingsItemService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IProductCatalogItemService iProductCatalogItemService;
	final IProductProductPriceService iProductProductPriceService;
	final IProductListingSmallAndLightService iProductListingSmallAndLightService;
    @ApiOperation(value = "创建对象")
    @GetMapping("/insert")
    public Result<Boolean> insertAction() {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				iAmzProductRefreshService.insert();
			}
    	}).start();
        return Result.judge(true);
    }
	
    //
    @ApiOperation(value = "更新商品对象")
    @GetMapping("/refresh")
    public Result<Boolean> refreshAction() {
    	log.info("refreshProductAction-----"+new Date());
    	amazonAuthorityService.executTask(iProductListingsItemService);
     
        return Result.judge(true);
    }
    
    @ApiOperation(value = "更新商品对象")
    @GetMapping("/refreshByAuth")
    public Result<Boolean> refreshAction(String authid) {
    	AmazonAuthority auth = amazonAuthorityService.getById(authid);
    	iProductListingsItemService.runApi(auth);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "更新")
    @GetMapping("/refreshCatalog")
    public Result<Boolean> refreshCatalogAction() {
    	log.info("refreshCatalogAction-----"+new Date());
    	amazonAuthorityService.executTask(iProductCatalogItemService);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "更新")
    @GetMapping("/refreshPrice")
    public Result<Boolean> refreshPriceAction() {
    	log.info("refreshPriceAction-----"+new Date());
    	amazonAuthorityService.executTask(iProductProductPriceService);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "刷新产品价格")
    @GetMapping("/refreshPriceBySKU")
    public Result<GetPricingResponse> refreshPriceBySKUAction(String groupid,String marketplaceid,String sku) {
    	log.info("refreshPriceAction-----"+new Date());
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
    	GetPricingResponse reponse = iProductProductPriceService.captureProductPrice(auth, sku,marketplaceid);
    	iProductProductPriceService.handlerResult(reponse, auth, marketplaceid);
        return Result.success(reponse);
    }
    
    
    @ApiOperation(value = "刷新产品")
    @GetMapping("/refreshItemBySKU")
    @CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
    public Result<?> refreshItemBySKUAction(String groupid,String marketplaceid,String sku) {
    	log.info("refreshPriceAction-----"+new Date());
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
		List<String> includedData=new ArrayList<String>();
		includedData.add("summaries");
		includedData.add("attributes");
		includedData.add("fulfillmentAvailability");
    	Item item = iProductListingsItemService.captureListMatchingProduct(auth, sku, Arrays.asList(marketplaceid),null,includedData);
    	iProductListingsItemService.handlerResult(item, auth,null);
    	this.refreshPriceBySKUAction(groupid, marketplaceid, sku);
    	if(item!=null&&item.getSummaries()!=null&&item.getSummaries().size()>0) {
    		String asin=item.getSummaries().get(0).getAsin();
    		if(StrUtil.isNotBlank(asin)) {
    			this.refreshCatalogBySKUAction(groupid, marketplaceid, asin, sku);
    		}
    		iProductListingSmallAndLightService.refreshSmallAndLight(auth,sku,Arrays.asList(marketplaceid));
    	}
    	
    	return Result.success(item);
    }
    
    
    @ApiOperation(value = "刷新产品关系")
    @GetMapping("/refreshCatalogBySKU")
    public Result<?> refreshCatalogBySKUAction(String groupid,String marketplaceid,String asin,String sku) {
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
    	AmzProductRefresh skuRefresh=new AmzProductRefresh();
    	skuRefresh.setAmazonauthid(new BigInteger(auth.getId()));
    	skuRefresh.setSku(sku);
    	skuRefresh.setAsin(asin);
		com.amazon.spapi.model.catalogitems.Item item = iProductCatalogItemService.captureCatalogProduct(auth, skuRefresh, Arrays.asList(marketplaceid));
		iProductCatalogItemService.handlerResult(auth, skuRefresh, item); 
        return Result.success(item);
    }  
    
    @ApiOperation(value = "刷新产品关系")
    @PostMapping("/searchCatalogProducts")
    public Result<?> searchCatalogProductsAction(@RequestBody ProductCatalogItemsDTO dto) {
    	AmazonAuthority auth = null;
    	if(dto.getSellerId()!=null) {
    		auth=amazonAuthorityService.selectBySellerId(dto.getSellerId());
    	}else {
    		auth=amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
    	}
    	 
    	 ItemSearchResults response = iProductCatalogItemService.searchCatalogProducts(auth,dto) ;
        return Result.success(response);
    }  

}

