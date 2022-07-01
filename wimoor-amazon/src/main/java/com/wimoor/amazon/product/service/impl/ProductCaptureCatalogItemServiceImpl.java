package com.wimoor.amazon.product.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.CatalogApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.catalogitems.Item;
import com.amazon.spapi.model.catalogitems.ItemRelationship;
import com.amazon.spapi.model.catalogitems.ItemRelationships;
import com.amazon.spapi.model.catalogitems.ItemRelationshipsByMarketplace;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCaptureCatalogItemService;
import com.wimoor.amazon.product.service.IProductInfoService;
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

	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<AmzProductRefresh> skulist =iAmzProductRefreshService.findForDetailRefresh(amazonAuthority.getId());
		for(AmzProductRefresh skuRefresh:skulist) {
			String[] marketarray = skuRefresh.getMarketplaceid().split(",");
			captureCatalogProductSync(amazonAuthority,skuRefresh,Arrays.asList(marketarray));
		}
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
		CatalogApi api = apiBuildService.getCatalogApi(auth);
		try {
			if(auth.apiNotRateLimit("getCatalogItem")) {
				ApiCallbackGetCatalogItem callback = new ApiCallbackGetCatalogItem(this,auth,skuRefresh);
				List<String> includedData=new ArrayList<String>();
				includedData.add("relationships");
				api.getCatalogItemAsync(skuRefresh.getAsin(),market, includedData,null,callback);
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			auth.setApiRateLimit("getCatalogItem", null, e);
		}
	}

	@Override
	public void handlerCatalogItem(AmazonAuthority auth, AmzProductRefresh skuRefresh,Item result) {
		// TODO Auto-generated method stub
       ItemRelationships relist = result.getRelationships();
       for(ItemRelationshipsByMarketplace relations:relist) {
    	  String marketplaceid= relations.getMarketplaceId();
    	    for(ItemRelationship rela:relations.getRelationships()) {
    	    	List<String> parentAsins = rela.getParentAsins();
    	    	List<String> children = rela.getChildAsins();
    	    	ProductInfo info = iProductInfoService.productOnlyone(auth.getId(),skuRefresh.getSku(),  marketplaceid);
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
    	    		if(isparent) {
    	    			if(info.getIsparent()==null||!info.getIsparent()) {
    	    				iProductInfoService.updateById(info);
    	    			}
    	    		}
    	    	}
    	    	
    	    }
       }
	   
	}
 
	
}
