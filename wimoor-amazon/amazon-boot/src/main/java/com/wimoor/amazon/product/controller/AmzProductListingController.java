package com.wimoor.amazon.product.controller;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.spapi.model.productpricing.GetOffersResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.feed.service.impl.FeedFileInventoryXML;
import com.wimoor.amazon.product.pojo.dto.ProductListingItemDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductPriceRecord;
import com.wimoor.amazon.product.pojo.entity.ProductInAutoprice;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IAmzProductPriceRecordService;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCatalogItemService;
import com.wimoor.amazon.product.service.IProductInAutopriceService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.product.service.IProductListingsItemService;
import com.wimoor.amazon.product.service.IProductProductPriceService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/listing")
public class AmzProductListingController {

	final IAmzProductRefreshService iAmzProductRefreshService;
	final IProductListingsItemService iProductListingsItemService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IProductCatalogItemService iProductCatalogItemService;
	final IProductProductPriceService iProductProductPriceService;
	final IMarketplaceService marketplaceService;
	final IProductInfoService iProductInfoService;
	final ISubmitfeedService submitfeedService;
	final IProductInAutopriceService iProductInAutopriceService;
	final IAmzProductPriceRecordService iAmzProductPriceRecordService;
    @ApiOperation(value = "创建产品对象（必须含ASIN和SKU）")
    @PostMapping("/pushAsin")
    @CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
    public Result<?> pushAsinAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
        ProductInfo info = iProductListingsItemService.pushAsin(dto);
    	if(StrUtil.isNotBlank(dto.getLowestprice())) {
			if(info!=null) {
				ProductInAutoprice entity=iProductInAutopriceService.getById(info.getId());
				if(entity!=null) {
					entity.setPid(info.getId());
					entity.setLowestprice(new BigDecimal(dto.getLowestprice()));
					iProductInAutopriceService.updateById(entity);
				}else {
					entity=new ProductInAutoprice();
					entity.setPid(info.getId());
					entity.setLowestprice(new BigDecimal(dto.getLowestprice()));
					iProductInAutopriceService.save(entity);
				}
				if(info!=null&&dto.getPrice()!=null) {
					AmzProductPriceRecord record=new AmzProductPriceRecord();
					record.setPid(info.getId());
					record.setRemark("上传产品或刷新售价");
					record.setPrice(new BigDecimal(dto.getPrice()));
					record.setLowestprice(entity.getLowestprice());
					record.setRefprice(info.getPrice());
					record.setShipprice(new BigDecimal("0"));
					record.setOldshipprice(new BigDecimal("0"));
					record.setOldprice(info.getPrice());
					record.setOpttime(new Date());
					iAmzProductPriceRecordService.save(record);
				}
			}
		}
        return Result.success(info);
    }
	private void setAuthority(ProductListingItemDTO dto) {
		if( !StrUtil.isBlank(dto.getPid())) {
			ProductInfo info = iProductInfoService.getById(dto.getPid());
			AmazonAuthority auth = amazonAuthorityService.getById(info.getAmazonAuthId());
			dto.setAmazonauthid(auth.getId());
			dto.setAsin(info.getAsin());
			dto.setSku(info.getSku());
			dto.setMarketplaceids(Arrays.asList(info.getMarketplaceid()));
			dto.setGroupid(auth.getGroupid());
		}
		else if(StrUtil.isNotBlank(dto.getSellerid())) {
			AmazonAuthority auth = amazonAuthorityService.selectBySellerId(dto.getSellerid());
			dto.setAmazonauthid(auth.getId());
		}
		else if(StrUtil.isBlank(dto.getAmazonauthid())) {
    		if(dto.getGroupid()!=null) {
    			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceids().get(0));
    			if(auth!=null) {
    				dto.setAmazonauthid(auth.getId());
    			}else {
        			throw new BizException("无法定为授权");
        		}
    		}else {
    			throw new BizException("无法定为授权");
    		}
    	}
	}
    @ApiOperation(value = "刷新产品")
    @GetMapping("/refreshInfoBySKU")
    public Result<?> refreshInfoBySKUAction(String authid,String marketplaceid,String sku) {
    	AmazonAuthority auth = amazonAuthorityService.getById(authid);
		List<String> includedData=new ArrayList<String>();
		includedData.add("summaries");
		includedData.add("attributes");
		includedData.add("fulfillmentAvailability");
    	com.amazon.spapi.model.listings.Item item = iProductListingsItemService.captureListMatchingProduct(auth, sku, Arrays.asList(marketplaceid),null,includedData);
    	iProductListingsItemService.handlerResult(item, auth,null);
    	return Result.success(item);
    }
    
    @ApiOperation(value = "刷新产品")
    @PostMapping("/refreshItemInfo")
    public Result<?> refreshItemInfoAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
    	AmazonAuthority auth = amazonAuthorityService.getById(dto.getAmazonauthid());
		List<String> includedData=new ArrayList<String>();
		includedData.add("summaries");
		includedData.add("attributes");
		includedData.add("fulfillmentAvailability");
    	com.amazon.spapi.model.listings.Item item = iProductListingsItemService.captureListMatchingProduct(auth, dto.getSku(), dto.getMarketplaceids(),null,includedData);
    	iProductListingsItemService.handlerResult(item, auth,null);
    	return Result.success(item);
    }
    
    @ApiOperation(value = "创建产品对象（必须含ASIN和SKU）")
    @PostMapping("/saveAsin")
    public Result<?> saveAsinAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
    	ProductInfo info = iProductListingsItemService.saveAsin(dto);
		if(StrUtil.isNotBlank(dto.getLowestprice())) {
			if(info!=null) {
				ProductInAutoprice entity=iProductInAutopriceService.getById(info.getId());
				if(entity!=null) {
					entity.setPid(info.getId());
					entity.setLowestprice(new BigDecimal(dto.getLowestprice()));
					iProductInAutopriceService.updateById(entity);
				}else {
					entity=new ProductInAutoprice();
					entity.setPid(info.getId());
					entity.setLowestprice(new BigDecimal(dto.getLowestprice()));
					iProductInAutopriceService.save(entity);
				}
			}
		}
        return Result.success(info);
    }
    
    @ApiOperation(value = "创建产品对象（必须含ASIN和SKU）")
    @PostMapping("/changePrice")
    public Result<?> changePriceAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
    	UserInfo user = UserInfoContext.get();
    	dto.setUserid(user.getId());
        return Result.success(iProductListingsItemService.changePriceSku(dto));
    }
    
    @ApiOperation(value = "创建产品对象（必须含ASIN和SKU）")
    @PostMapping("/findAsin")
    public Result<?> findAsinAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
    	AmazonAuthority auth = amazonAuthorityService.getById(dto.getAmazonauthid());
        return Result.success(iProductCatalogItemService.captureCatalogProductChildren(auth,dto.getAsin(),dto.getMarketplaceids()));
    }
    
    @ApiOperation(value = "创建产品对象（必须含ASIN和SKU）")
    @PostMapping("/findAsinInfo")
    public Result<?> findAsinInfoAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
    	AmazonAuthority auth = amazonAuthorityService.getById(dto.getAmazonauthid());
     
        return Result.success(iProductCatalogItemService.captureCatalogProduct(auth,dto.getAsins(),dto.getMarketplaceids()));
    }
    
    @ApiOperation(value = "删除产品对象（必须含SKU）")
    @PostMapping("/deleteSku")
    public Result<?> deleteSkuAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
        return Result.success(iProductListingsItemService.deleteAsin(dto));
    }
	
    
    @ApiOperation(value = "读取产品对象")
    @PostMapping("/sku")
    public Result<?> skuAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
    	AmazonAuthority amazonAuthority = amazonAuthorityService.getById(dto.getAmazonauthid());
    	List<String> includedData=new ArrayList<String>();
		includedData.add("attributes");
		includedData.add("summaries");
		com.amazon.spapi.model.listings.Item reponse1 = iProductListingsItemService.captureListMatchingProduct(amazonAuthority, dto.getSku(), dto.getMarketplaceids(),null,includedData);
        return Result.success(reponse1);
    }
    
    @ApiOperation(value = "更新仓库库存")
    @PostMapping("/recordFollowList")
    @CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
    public Result<?> recordFollowListAction(@RequestBody ProductListingItemDTO dto) {
    	    setAuthority(dto);
    	    AmazonAuthority amazonAuthority = amazonAuthorityService.getById(dto.getAmazonauthid());
        	Marketplace marketplace = marketplaceService.getById(dto.getMarketplaceids().get(0));
	    	GetOffersResponse response = iProductProductPriceService.getItemOffers(amazonAuthority,dto.getAsin(),marketplace.getMarketplaceid());
	    	iProductProductPriceService.handleResultItemOffers(amazonAuthority, dto.getAsin(), marketplace.getMarketplaceid(), response);
	        return Result.success(response);
    }
    
    @ApiOperation(value = "更新仓库库存")
    @PostMapping("/updateInventory")
    public Result<?> updateInventoryAction(@RequestBody ProductListingItemDTO dto) {
    	setAuthority(dto);
    	AmazonAuthority amazonAuthority = amazonAuthorityService.getById(dto.getAmazonauthid());
    	Marketplace marketplace = marketplaceService.getById(dto.getMarketplaceids().get(0));
    	amazonAuthority.setMarketPlace(marketplace);
    	ByteArrayOutputStream inventoryXml=null;
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("sku", dto.getSku());
			map.put("qty","1");
			  inventoryXml = FeedFileInventoryXML.getFile(amazonAuthority ,map);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			throw new BizException("XML格式生成失败!");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("XML格式生成失败!");
		}
		AmzSubmitFeedQueue response = null;
		UserInfo user = new UserInfo();
		user.setId("1");
		if (inventoryXml != null && amazonAuthority != null && user != null) {
			response = submitfeedService.SubmitFeedQueue(inventoryXml, "Inventory"+dto.getSku(), amazonAuthority, FeedFileInventoryXML.type, user,null);
		} else {
			throw new BizException("系统改库存出现异常，未能正常提交至亚马逊!");
		}
        return Result.success(response);
    }
}

