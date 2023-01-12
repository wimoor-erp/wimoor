package com.wimoor.amazon.product.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.CatalogApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.catalogitems.Item;
import com.amazon.spapi.model.catalogitems.ItemClassificationSalesRank;
import com.amazon.spapi.model.catalogitems.ItemDisplayGroupSalesRank;
import com.amazon.spapi.model.catalogitems.ItemRelationship;
import com.amazon.spapi.model.catalogitems.ItemRelationships;
import com.amazon.spapi.model.catalogitems.ItemRelationshipsByMarketplace;
import com.amazon.spapi.model.catalogitems.ItemSalesRanks;
import com.amazon.spapi.model.catalogitems.ItemSalesRanksByMarketplace;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.pojo.entity.ProductRank;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCaptureCatalogItemService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.product.service.IProductRankService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.IPictureService;

import cn.hutool.core.util.StrUtil;

@Service 
public class ProductCaptureCatalogItemServiceImpl implements IProductCaptureCatalogItemService {
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
    IProductRankService iProductRankService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<AmzProductRefresh> skulist =iAmzProductRefreshService.findForCatalogRefresh(amazonAuthority.getId());
		if(skulist!=null&&skulist.size()>0) {
			AmzProductRefresh skuRefresh=skulist.get(0);
			String[] marketarray = skuRefresh.getMarketplaceid().split(",");
			captureCatalogProductSync(amazonAuthority,skuRefresh,Arrays.asList(marketarray));
		}
	}		
	@Override
	public Item captureCatalogProductDim(AmazonAuthority auth,String asin, List<String> market) {
		// TODO Auto-generated method stub
		CatalogApi api = apiBuildService.getCatalogApi(auth);
		Item response =null;
		try {
			List<String> includedData=new ArrayList<String>();
			includedData.add("dimensions");
			includedData.add("attributes");
			response = api.getCatalogItem(asin,market, includedData,null);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("接口调用错误："+e.getMessage());
		}
		return response;
	}

	@Override
	public Item captureCatalogProduct(AmazonAuthority auth, AmzProductRefresh skuRefresh, List<String> market) {
		// TODO Auto-generated method stub
		CatalogApi api = apiBuildService.getCatalogApi(auth);
		Item response =null;
		try {
			List<String> includedData=new ArrayList<String>();
			includedData.add("relationships");
			response = api.getCatalogItem(skuRefresh.getAsin(),market, includedData,null);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("接口调用错误："+e.getMessage());
		}
		return response;
	}

	@Override
	public void captureCatalogProductSync(AmazonAuthority auth, AmzProductRefresh skuRefresh, List<String> market) {
		// TODO Auto-generated method stub
		auth.setUseApi("getCatalogItem");
		try {
			if(auth.apiNotRateLimit()) {
				ApiCallbackGetCatalogItem callback = new ApiCallbackGetCatalogItem(this,auth,skuRefresh);
				List<String> includedData=new ArrayList<String>();
				includedData.add("relationships");
				includedData.add("salesRanks");
				if(skuRefresh!=null&&skuRefresh.getAsin()!=null&&StrUtil.isNotBlank(skuRefresh.getAsin())) {
					CatalogApi api = apiBuildService.getCatalogApi(auth);
					api.getCatalogItemAsync(skuRefresh.getAsin(),market, includedData,null,callback);
				}else {
					String[] marketarray = skuRefresh.getMarketplaceid().split(",");
					for(String marketstr:marketarray) {
			    			AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
									.eq(AmzProductRefresh::getMarketplaceid, marketstr)
									.eq(AmzProductRefresh::getSku,skuRefresh.getSku())
									.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
									);
			    			if(refresh!=null) {
			    				refresh.setCatalogRefreshTime(LocalDateTime.now());
			    				iAmzProductRefreshService.updateById(refresh);
			    			}
			    	}
					System.out.println("存在为空的ASIN："+skuRefresh.getSku());
				}
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			auth.setApiRateLimit( null, e);
		}
	}

	@Override
	public void handlerResult(AmazonAuthority auth, AmzProductRefresh skuRefresh,Item result) {
		// TODO Auto-generated method stub
	   if(result==null)return;
       ItemRelationships relist = result.getRelationships();
       for(ItemRelationshipsByMarketplace relations:relist) {
    	  String marketplaceid= relations.getMarketplaceId();
	      AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
																.eq(AmzProductRefresh::getMarketplaceid, marketplaceid)
																.eq(AmzProductRefresh::getSku,skuRefresh.getSku())
																.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
																);
	    	    if(refresh!=null) {
	    	    	refresh.setSku(skuRefresh.getSku());
	    	    	refresh.setAsin(skuRefresh.getAsin());
	    	    	refresh.setMarketplaceid(marketplaceid);
	    	    	refresh.setAmazonauthid(new BigInteger(auth.getId()));
	    	    	refresh.setCatalogRefreshTime(LocalDateTime.now());
	    	    	refresh.setNotfound(false);
	    	    	iAmzProductRefreshService.updateById(refresh);
	    	    }
    	    for(ItemRelationship rela:relations.getRelationships()) {
    	    	List<String> parentAsins = rela.getParentAsins();
    	    	List<String> children = rela.getChildAsins();
    	    	ProductInfo info = iProductInfoService.productOnlyone(auth.getId(),skuRefresh.getSku(),  marketplaceid);
    	    	if(info==null) {continue;}
    	    	if(parentAsins!=null&&parentAsins.size()>0) {
    	    		for(String parentAsin:parentAsins) {
        	    		if(StrUtil.isNotBlank(parentAsin)) {
            	    		if(info.getParentAsin()==null||!info.getParentAsin().equals(parentAsin)) {
            	    			info.setParentAsin(parentAsin);
            	    			info.setParentMarketplace(marketplaceid);
            	    			info.setIsparent(false);
            	    			iProductInfoService.updateById(info);
            	    		}
            	    	}
        	    	}
    	    	}
    	    	if(children!=null&&children.size()>0) {
    	    		boolean isparent = false;
    	    		for(String child:children) {
        	    		if(StrUtil.isNotBlank(child)) {
        	    			  List<ProductInfo> asinChildren = iProductInfoService.selectByAsin(auth.getId(), child ,marketplaceid);
            	    		  if(asinChildren!=null && asinChildren.size()>0) {
            	    			  for(ProductInfo childInfo:asinChildren) {
                	    			  if(childInfo.getParentAsin()==null||!childInfo.getParentAsin().equals(info.getAsin())) {
                	    				  childInfo.setParentAsin(info.getAsin());
                	    				  childInfo.setParentMarketplace(marketplaceid);
                	    				  childInfo.setIsparent(false);
                      	    			  iProductInfoService.updateById(childInfo);
                      	    		  }
                	    			  isparent=true;
                	    		  }
            	    		  }
            	    	}
        	    	}
    	    		if(isparent) {
    	    			if(info.getIsparent()==null||!info.getIsparent()) {
    	    				iProductInfoService.updateById(info);
    	    			}
    	    		}
    	    	}
    	    	
    	    }
       }
       ItemSalesRanks salesRanks = result.getSalesRanks();
       if(salesRanks!=null) {
           for(ItemSalesRanksByMarketplace itemSalesRanksByMarketplace:salesRanks) {
        	   String marketplaceid= itemSalesRanksByMarketplace.getMarketplaceId();
    	       ProductInfo info = iProductInfoService.productOnlyone(auth.getId(),skuRefresh.getSku(),  marketplaceid);
    	       if(info==null) {continue;}
    	       List<ItemClassificationSalesRank> classificationRanks = itemSalesRanksByMarketplace.getClassificationRanks();
    	       List<ItemDisplayGroupSalesRank> displayGroupRanks = itemSalesRanksByMarketplace.getDisplayGroupRanks();
    	       for(ItemClassificationSalesRank itemClassificationSalesRank:classificationRanks) {
    	    	   Integer rank = itemClassificationSalesRank.getRank();
    	    	   //String link=itemClassificationSalesRank.getLink();
    	    	   String categoryId=itemClassificationSalesRank.getClassificationId();
    	    	   //String title=itemClassificationSalesRank.getTitle();
    	    	   ProductRank productRank=new ProductRank();
    	    	   productRank.setByday(LocalDateTime.now());
    	    	   productRank.setCategoryid(categoryId);
    	    	   productRank.setIsmain(false);
    	    	   productRank.setRank(rank);
    	    	   productRank.setProductId(info.getId());
    	    	   iProductRankService.insert(productRank);
    	       }
    	       for(ItemDisplayGroupSalesRank itemDisplayGroupSalesRank:displayGroupRanks) {
    	    	   //String  link=itemDisplayGroupSalesRank.getLink();
    	    	   Integer rank = itemDisplayGroupSalesRank.getRank();
    	    	   //String title=itemDisplayGroupSalesRank.getTitle();
    	    	   String categoryId=itemDisplayGroupSalesRank.getWebsiteDisplayGroup();
    	    	   ProductRank productRank=new ProductRank();
    	    	   productRank.setByday(LocalDateTime.now());
    	    	   productRank.setCategoryid(categoryId);
    	    	   productRank.setIsmain(true);
    	    	   productRank.setRank(rank);
    	    	   productRank.setProductId(info.getId());
    	    	   iProductRankService.insert(productRank);
    	       }
           }
       }

	}


	@Override
	public void handlerFailure(AmazonAuthority auth, AmzProductRefresh skuRefresh, ApiException e) {
		// TODO Auto-generated method stub
		String[] marketarray = skuRefresh.getMarketplaceid().split(",");
	    if(e.getMessage().contains("Not Found")) {
	    	String responsebody=e.getResponseBody();
	    	for(String market:marketarray) {
	    		if(responsebody.contains(skuRefresh.getAsin())&&responsebody.contains(market)) {
	    			AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
							.eq(AmzProductRefresh::getMarketplaceid, market)
							.eq(AmzProductRefresh::getSku,skuRefresh.getSku())
							.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
							);
	    			if(refresh!=null) {
	    				refresh.setNotfound(true);
	    				refresh.setCatalogRefreshTime(LocalDateTime.now());
	    				iAmzProductRefreshService.updateById(refresh);
	    				ProductInfo info = iProductInfoService.getById(refresh.getPid());
	    				info.setInvalid(true);
	    				iProductInfoService.updateById(info);
	    			}
	    		}
	    	}
	    }else {
	    	for(String market:marketarray) {
	    			AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
							.eq(AmzProductRefresh::getMarketplaceid, market)
							.eq(AmzProductRefresh::getSku,skuRefresh.getSku())
							.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
							);
	    			if(refresh!=null) {
	    				refresh.setCatalogRefreshTime(LocalDateTime.now());
	    				iAmzProductRefreshService.updateById(refresh);
	    			}
	    	}
	    }
	
	}

    boolean isrun=true;
	@Override
	public void stopTask() {
		// TODO Auto-generated method stub
		isrun=false;
	}


	@Override
	public void runTask() {
		// TODO Auto-generated method stub
		ProductCaptureCatalogItemServiceImpl self = this;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(isrun) {
					try {
						amazonAuthorityService.executTask(self);
						Thread.sleep(5000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}
 
	
}
