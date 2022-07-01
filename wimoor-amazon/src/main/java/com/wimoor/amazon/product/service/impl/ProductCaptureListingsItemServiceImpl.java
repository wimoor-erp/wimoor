package com.wimoor.amazon.product.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ListingsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.listings.Item;
import com.amazon.spapi.model.listings.ItemSummaryByMarketplace;
import com.amazon.spapi.model.listings.ItemSummaryByMarketplace.StatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.squareup.okhttp.Call;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCaptureCatalogItemService;
import com.wimoor.amazon.product.service.IProductCaptureListingsItemService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;

@Service("productCaptureService")
public class ProductCaptureListingsItemServiceImpl implements IProductCaptureListingsItemService {
	@Resource
	private ProductInfoMapper productMapper;
	@Resource
	private ProductInOptMapper productInOptMapper;
	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	IAmzProductRefreshService iAmzProductRefreshService;
	@Autowired
	IPictureService pictureService;
	@Autowired
	IProductCaptureCatalogItemService iProductCaptureCatalogItemService;
	@Override
	public Item captureListMatchingProduct(AmazonAuthority amazonAuthority, String sku, List<String> marketplaces) {
		// TODO Auto-generated method stub
		
		ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
		try {
			Item item = api.getListingsItem(amazonAuthority.getSellerid(), sku, marketplaces, null, null);
			return item;
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("接口调用错误："+e.getMessage());
		}
	}
	  
	  
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<AmzProductRefresh> skulist =iAmzProductRefreshService.findForDetailRefresh(amazonAuthority.getId());
		for(AmzProductRefresh skuRefresh:skulist) {
			String[] marketarray = skuRefresh.getMarketplaceid().split(",");
			List<String> markets = Arrays.asList(marketarray);
			captureListMatchingProductSync(amazonAuthority,skuRefresh,markets);
			iProductCaptureCatalogItemService.captureCatalogProductSync(amazonAuthority, skuRefresh, markets);
		}
	}		
	
	@Override
	public Call captureListMatchingProductSync(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, List<String> marketplaces) {
		// TODO Auto-generated method stub
		ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
		try {
			if(amazonAuthority.apiNotRateLimit("getListingsItem")) {
				ApiCallback<Item> callback=new ApiCallbackItem(this,amazonAuthority,amzProductRefresh);
				List<String> includedData=new ArrayList<String>();
				includedData.add("summaries");
				includedData.add("attributes");
				includedData.add("offers");
				Call item = api.getListingsItemAsync(amazonAuthority.getSellerid(), amzProductRefresh.getSku(), marketplaces, null, includedData, callback);
				return item;
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			amazonAuthority.setApiRateLimit("getListingsItem", null, e);
		}
		return null;
	}

 
 
	 
		@Override
		public void handlerItem(Item result, AmazonAuthority amazonAuthority) {
			// TODO Auto-generated method stub
			for(ItemSummaryByMarketplace summary:result.getSummaries()) {
				AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
						.eq(AmzProductRefresh::getMarketplaceid, summary.getMarketplaceId())
						.eq(AmzProductRefresh::getSku,result.getSku())
						.eq(AmzProductRefresh::getAmazonauthid, amazonAuthority.getId())
						);
				refresh.setSku(result.getSku());
			    refresh.setAsin(summary.getAsin());
			    refresh.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
			    refresh.setDetailRefreshTime(LocalDateTime.now());
				refresh.setDetailRefreshTime(LocalDateTime.now());
				iAmzProductRefreshService.updateById(refresh);
				ProductInfo product = productMapper.selectById(refresh.getPid());
				ProductInOpt opt = productInOptMapper.selectById(product.getId());
			    String mystatus=null;
			    boolean hasbuyable=false;
			    if(summary.getStatus()!=null&&summary.getStatus().size()>0) {
					for(StatusEnum status:summary.getStatus()){
						if(status.getValue().equals(StatusEnum.BUYABLE.getValue())) {
							hasbuyable=true;
						}
				    	if(mystatus==null) {
				    		mystatus=status.getValue();
				    	}else {
				    		mystatus=mystatus+","+status.getValue();
				    	}
				    };
			    }
				Picture picture=null;
			    try {
			    	if(summary.getMainImage()!=null&&summary.getMainImage().getLink()!=null) {
			    		if(product.getImage()!=null) {
				    		picture=pictureService.getById(product.getImage());
				    		if(picture==null||picture.getUrl()==null||!picture.getUrl().equals(summary.getMainImage().getLink())) {
				    			picture=pictureService.downloadPicture(summary.getMainImage().getLink(), PictureServiceImpl.productImgPath, product.getImage().toString());
				    		}
				    	}else {
				    		picture=pictureService.downloadPicture(summary.getMainImage().getLink(), PictureServiceImpl.productImgPath,null);
				    	}
			    	}
			    	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    boolean isparent=false;
			    if(summary.getFnSku()==null&&hasbuyable==false) {
			    	isparent=true;
	  			}
	            if( (product.getAsin()==null&&summary.getAsin()!=null)
	            		||(product.getIsparent()==null&&isparent==true)
	            		||(product.getName()==null&&summary.getItemName()!=null)
	            		||(product.getFnsku()==null&&summary.getFnSku()!=null)
	            		||(product.getImage()==null&&picture!=null)
	            		||(product.getPcondition()==null&&summary.getConditionType().getValue()!=null)
	            		||(product.getLastupdate()==null&&summary.getLastUpdatedDate()!=null)
	              		||(product.getCreatedate()==null&&summary.getCreatedDate()!=null)
	            		||(product.getStatus()==null&&mystatus!=null)
	            		||!product.getAsin().equals(summary.getAsin())
	            		||!product.getName().equals(summary.getItemName())
	            		||!product.getFnsku().equals(summary.getFnSku())
	            		||!product.getPcondition().equals(summary.getConditionType().getValue())
	             		||!product.getLastupdate().equals(AmzDateUtils.getDate(summary.getLastUpdatedDate()))
	             		||!product.getCreatedate().equals(AmzDateUtils.getDate(summary.getCreatedDate()))
	            		||!product.getStatus().equals(mystatus)
	            		||!product.getIsparent().equals(isparent)
	            		||(product.getImage()!=null&&product.getImage().toString().equals(picture.getId()))
	            		) {
	            	product.setAsin(summary.getAsin());
	      			product.setName(summary.getItemName());
	      			product.setFnsku(summary.getFnSku());
	      			product.setIsparent(isparent);
	      		    product.setPcondition(summary.getConditionType().getValue());
	      		    product.setLastupdate(AmzDateUtils.getDate(summary.getLastUpdatedDate()) );
	      		    product.setCreatedate(AmzDateUtils.getDate(summary.getCreatedDate()) );
	      		    product.setTypename(summary.getProductType());
		      		if(picture!=null) {
		      		    product.setImage(new BigInteger(picture.getId()));
		  		    }
	      		    product.setStatus(mystatus);
	      		    productMapper.updateById(product);
	            }
			    if(opt!=null &&(opt.getFnsku()==null ||!opt.getFnsku().equals(product.getFnsku()))) {
			    	opt.setFnsku(product.getFnsku());
			    	productInOptMapper.updateById(opt);
			    }
		 

			}
			
			
		}
	
 
	
}
