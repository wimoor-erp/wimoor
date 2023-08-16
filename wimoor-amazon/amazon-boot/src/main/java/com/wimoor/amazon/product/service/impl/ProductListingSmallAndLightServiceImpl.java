package com.wimoor.amazon.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.SmallAndLightApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.fbasmallandlight.SmallAndLightEnrollment;
import com.amazon.spapi.model.fbasmallandlight.SmallAndLightEnrollmentStatus;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.product.service.IProductListingSmallAndLightService;
@Service
public class ProductListingSmallAndLightServiceImpl  implements IProductListingSmallAndLightService {
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	private IProductInfoService iProductInfoService;
	@Override
	public void refreshSmallAndLight(AmazonAuthority auth, String sku, List<String> marketplaces) {
		// TODO Auto-generated method stub
		SmallAndLightApi api = apiBuildService.getSmallAndLightApi(auth);
		try {
			SmallAndLightEnrollment response = api.getSmallAndLightEnrollmentBySellerSKU(sku, marketplaces);
		   if(response!=null) {
			   String marketplaceid=response.getMarketplaceId();
			   String amazonauthid=auth.getId();
			   List<ProductInfo> infos = iProductInfoService.selectBySku(sku, marketplaceid, amazonauthid);
		       for(ProductInfo info:infos) {
		    	   if(response.getStatus().equals(SmallAndLightEnrollmentStatus.ENROLLED)) {
		    		   if(info.getInSnl()==null||info.getInSnl()==false) {
		    			   info.setInSnl(true);
			    		   iProductInfoService.updateById(info);
		    		   }
		    	   }else {
		    		   if(info.getInSnl()==null||info.getInSnl()==true) {
		    			   info.setInSnl(false);
			    		   iProductInfoService.updateById(info);
		    		   }
		    	   }
		       }
		   }
			
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 

}
