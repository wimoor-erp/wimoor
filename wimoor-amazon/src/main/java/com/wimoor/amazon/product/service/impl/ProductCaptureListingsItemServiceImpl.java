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
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
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
import com.wimoor.amazon.util.EmojiFilterUtils;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;

@Service
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
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
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
		AmzProductRefresh skuRefresh=iAmzProductRefreshService.findForDetailRefresh(amazonAuthority.getId());
        List<String> markets = Arrays.asList(skuRefresh.getMarketplaceid());
	    captureListMatchingProductSync(amazonAuthority,skuRefresh,markets);
	}		
	
	@Override
	public Call captureListMatchingProductSync(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, List<String> marketplaces) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getListingsItem");
		ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
		try {
			if(amazonAuthority.apiNotRateLimit()) {
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
			amazonAuthority.setApiRateLimit(null, e);
		}
		return null;
	}

 
 
	 
		@Override
		public void handlerResult(Item result, AmazonAuthority amazonAuthority) {
			// TODO Auto-generated method stub
			for(ItemSummaryByMarketplace summary:result.getSummaries()) {
				AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
						.eq(AmzProductRefresh::getMarketplaceid, summary.getMarketplaceId())
						.eq(AmzProductRefresh::getSku,result.getSku())
						.eq(AmzProductRefresh::getAmazonauthid, amazonAuthority.getId())
						);
				if(refresh!=null) {
					refresh.setSku(result.getSku());
				    refresh.setAsin(summary.getAsin());
				    refresh.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
				    refresh.setDetailRefreshTime(LocalDateTime.now());
				    refresh.setNotfound(false);
					iAmzProductRefreshService.updateById(refresh);
				}else {
					List<ProductInfo> info = productMapper.selectBySku(result.getSku(), summary.getMarketplaceId(), amazonAuthority.getId());
					if(info!=null&&info.size()>0) {
						refresh=new AmzProductRefresh();
						refresh.setPid(new BigInteger(info.get(0).getId()));
						refresh.setSku(result.getSku());
					    refresh.setAsin(summary.getAsin());
					    refresh.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
					    refresh.setDetailRefreshTime(LocalDateTime.now());
					    refresh.setNotfound(false);
						iAmzProductRefreshService.save(refresh);
					} 
				}
				 
				ProductInfo product = null;
				if(refresh!=null) {
					product=productMapper.selectById(refresh.getPid());
				}else {
					 product =new ProductInfo();
					 product.setSku(result.getSku());
					 product.setMarketplaceid(summary.getMarketplaceId());
					 product.setAmazonAuthId(new BigInteger(amazonAuthority.getId()));
				}
				
				ProductInOpt opt =null;
				if(product.idIsNULL()==false) {
					opt=productInOptMapper.selectById(product.getId());
				}
			    String mystatus=null;
			    if(summary.getStatus()!=null&&summary.getStatus().size()>0) {
					for(StatusEnum status:summary.getStatus()){
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
				    		if(picture==null||picture.getUrl()==null||picture.getLocation()==null||!picture.getUrl().equals(summary.getMainImage().getLink())) {
				    			String path=PictureServiceImpl.productImgPath+amazonAuthority.getShopId()+"/"+amazonAuthority.getId()+"/"+summary.getMarketplaceId()+"/";
				    			picture=pictureService.downloadPicture(summary.getMainImage().getLink(),path, product.getImage().toString());
				    		}
				    	}else {
				    		String path=PictureServiceImpl.productImgPath+amazonAuthority.getShopId()+"/"+amazonAuthority.getId()+"/"+summary.getMarketplaceId()+"/";
				    		picture=pictureService.downloadPicture(summary.getMainImage().getLink(), path,null);
				    	}
			    	}
			    	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            if( (product.getAsin()==null&&summary.getAsin()!=null)
	            		||(product.getName()==null&&summary.getItemName()!=null)
	            		||(product.getFnsku()==null&&summary.getFnSku()!=null)
	            		||(product.getImage()==null&&picture!=null)
	            		||(product.getPcondition()==null&&summary.getConditionType()!=null&&summary.getConditionType().getValue()!=null)
	            		||(product.getLastupdate()==null&&summary.getLastUpdatedDate()!=null)
	              		||(product.getCreatedate()==null&&summary.getCreatedDate()!=null)
	              		||(product.getFnsku()==null&&summary.getFnSku()!=null)
	            		||(product.getStatus()==null&&mystatus!=null)
	            		||!product.getAsin().equals(summary.getAsin())
	            		||!(product.getName()!=null&&product.getName().equals(EmojiFilterUtils.filterEmoji(summary.getItemName())))
	            		||!(product.getFnsku()!=null&&product.getFnsku().equals(summary.getFnSku()))
	            		||!(product.getPcondition()!=null&&summary.getConditionType()!=null&&product.getPcondition().equals(summary.getConditionType().getValue()))
	             		||!product.getLastupdate().equals(AmzDateUtils.getDate(summary.getLastUpdatedDate()))
	             		||!product.getCreatedate().equals(AmzDateUtils.getDate(summary.getCreatedDate()))
	            		||!product.getStatus().equals(mystatus)
	            		||(product.getImage()!=null&&product.getImage().toString().equals(picture.getId()))
	            		) {
	            	product.setAsin(summary.getAsin());
	      			product.setName(EmojiFilterUtils.filterEmoji(summary.getItemName()));
	      			product.setFnsku(summary.getFnSku());
	      
	      			if(summary.getConditionType()!=null) {
	      				product.setPcondition(summary.getConditionType().getValue());
	      			}
	      		    product.setLastupdate(AmzDateUtils.getDate(summary.getLastUpdatedDate()) );
	      		    product.setCreatedate(AmzDateUtils.getDate(summary.getCreatedDate()) );
	      		    product.setTypename(summary.getProductType());
		      		if(picture!=null) {
		      		    product.setImage(new BigInteger(picture.getId()));
		  		    }
		      		product.setInvalid(false);
	      		    product.setStatus(mystatus);
	      		    if(product.idIsNULL()) {
	      		    	productMapper.insert(product);
	      		    	refresh=new AmzProductRefresh();
						refresh.setPid(new BigInteger(product.getId()));
						refresh.setSku(result.getSku());
					    refresh.setAsin(summary.getAsin());
					    refresh.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
					    refresh.setDetailRefreshTime(LocalDateTime.now());
					    refresh.setNotfound(false);
						iAmzProductRefreshService.save(refresh);
	      		    }else {
	      		    	productMapper.updateById(product);
	      		    }
	            }
	           // &&(opt.getFnsku()==null ||!opt.getFnsku().equals(product.getFnsku()))
			    if(opt!=null) {
			    	opt.setFnsku(product.getFnsku());
			    	opt.setLastupdate(LocalDateTime.now());
			    	productInOptMapper.updateById(opt);
			    }else {
			    	opt=new ProductInOpt();
			    	opt.setPid(new BigInteger(product.getId()));
			    	opt.setDisable(false);
			    	opt.setFnsku(product.getFnsku());
			    	productInOptMapper.insert(opt);
			    }
		 

			}
			
			
		}
	
		@Override
		public void handlerFailure(AmazonAuthority auth, AmzProductRefresh skuRefresh, ApiException e) {
			// TODO Auto-generated method stub
		    if(e.getMessage().contains("Not Found")) {
		    	      AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
								.eq(AmzProductRefresh::getMarketplaceid, skuRefresh.getMarketplaceid())
								.eq(AmzProductRefresh::getSku,skuRefresh.getSku())
								.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
								);
		    			refresh.setNotfound(true);
		    			refresh.setDetailRefreshTime(LocalDateTime.now());
		    		 	iAmzProductRefreshService.updateById(refresh);
		    		 	ProductInfo info = productMapper.selectById(refresh.getPid());
		    		 	info.setInvalid(true);
		    		 	productMapper.updateById(info);
		    }else {
		        AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
						.eq(AmzProductRefresh::getMarketplaceid, skuRefresh.getMarketplaceid())
						.eq(AmzProductRefresh::getSku,skuRefresh.getSku())
						.eq(AmzProductRefresh::getAmazonauthid, auth.getId())
						);
    			refresh.setDetailRefreshTime(LocalDateTime.now());
    		 	iAmzProductRefreshService.updateById(refresh);
		    }
		
		}
        boolean isrun=true;

		@Override
		public void stopTask() {
			// TODO Auto-generated method stub
		ProductCaptureListingsItemServiceImpl self = this;
		   new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(isrun) {
					try {
						amazonAuthorityService.executTask(self);
						Thread.sleep(10000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		   }).start();
			
		}


		@Override
		public void runTask() {
			// TODO Auto-generated method stub
			isrun=false;
		}
	 
	
}
