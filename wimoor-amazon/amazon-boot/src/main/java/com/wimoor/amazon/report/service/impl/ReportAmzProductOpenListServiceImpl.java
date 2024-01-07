package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.report.pojo.entity.ReportType;
 

@Service("reportAmzProductOpenListService")
public class ReportAmzProductOpenListServiceImpl extends ReportServiceImpl{
	@Resource
	private ProductInfoMapper productMapper;
	
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br )  {
		// TODO Auto-generated method stub
				int lineNumber = 0;
				String line;
				String asin = null;
				String sku = null;
				try {
					while ((line = br.readLine()) != null) {
						String[] info = line.split("\t");
						if (lineNumber != 0) {
							sku = info[0];
							asin = info[1];
							List<ProductInfo> productInfoList = productMapper.selectBySku(sku, amazonAuthority.getMarketPlace().getMarketplaceid(),amazonAuthority.getId());
							 ProductInfo productInfo=null;
							 if(productInfoList.size()>1){
								 for(ProductInfo productitem:productInfoList){
									   if(!productitem.getAsin().equals(asin)){
										   productMapper.deleteById(productitem);
									   }else{
										   productInfo  =productitem;
									   }
								   }
							 }else if(productInfoList.size()==1){
								 productInfo=productInfoList.get(0);
							 }else {
								 productInfo=new ProductInfo();
								 productInfo.setId(amazonAuthorityService.getUUID());
								 productInfo.setAmazonAuthId(new BigInteger(amazonAuthority.getId()));
								 productInfo.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
								 productInfo.setSku(sku);
								 productInfo.setAsin(asin);
								 productMapper.insert(productInfo);
							 }
							if (productInfo != null && !productInfo.getAsin().equals(asin)) {
								productInfo.setAsin(asin);
								productMapper.updateById(productInfo);
							}
							info=null;
							productInfoList.clear();
							productInfoList=null;
							productInfo=null;
						}
						lineNumber++;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
			 
						if(br!=null) {
						    try {
								br.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				 
			}
				return null;

	}

	@Override
	public String myReportType() {
		// TODO Auto-generated method stub
		return ReportType.ProductOpenListings;
	}
	
	
}
