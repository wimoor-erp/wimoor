package com.wimoor.amazon.product.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
			throw new BizException("接口调用错误："+e.getResponseBody());
		}
	}
	  
	  
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		AmzProductRefresh skuRefresh=iAmzProductRefreshService.findForDetailRefresh(amazonAuthority.getId());
		if(skuRefresh!=null) {
			List<String> markets = Arrays.asList(skuRefresh.getMarketplaceid());
			captureListMatchingProductSync(amazonAuthority,skuRefresh,markets);
		}
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

 
 
	ProductInfo getProductInfo(String marketplaceid,String sku,String amazonauthid,String asin){
	    	AmzProductRefresh refresh = iAmzProductRefreshService.getOne(new LambdaQueryWrapper<AmzProductRefresh>()
					.eq(AmzProductRefresh::getMarketplaceid, marketplaceid)
					.eq(AmzProductRefresh::getSku,sku)
					.eq(AmzProductRefresh::getAmazonauthid, amazonauthid)
					);
			if(refresh!=null) {
				refresh.setSku(sku);
			    refresh.setAsin(asin);
			    refresh.setAmazonauthid(new BigInteger(amazonauthid));
			    refresh.setDetailRefreshTime(LocalDateTime.now());
			    refresh.setNotfound(false);
				iAmzProductRefreshService.updateById(refresh);
			}else {
				List<ProductInfo> info = productMapper.selectBySku(sku,marketplaceid, amazonauthid);
				if(info!=null&&info.size()>0) {
					refresh=new AmzProductRefresh();
					refresh.setPid(new BigInteger(info.get(0).getId()));
					refresh.setSku(sku);
				    refresh.setAsin(asin);
				    refresh.setAmazonauthid(new BigInteger(amazonauthid));
				    refresh.setDetailRefreshTime(LocalDateTime.now());
				    refresh.setMarketplaceid(marketplaceid);
				    refresh.setNotfound(false);
					iAmzProductRefreshService.save(refresh);
				}else {
					
				}
			}
			ProductInfo product=null;
			if(refresh!=null) {
				 product=productMapper.selectById(refresh.getPid());
			}else {
				 product =new ProductInfo();
				 product.setSku(sku);
				 product.setMarketplaceid(marketplaceid);
				 product.setAmazonAuthId(new BigInteger(amazonauthid));
			}
			return product;
	    }
		@Override
		public void handlerResult(Item result, AmazonAuthority amazonAuthority,AmzProductRefresh mrefresh) {
			// TODO Auto-generated method stub
			if(result!=null&&result.getSummaries()!=null&&result.getSummaries().size()>0) {
				for(ItemSummaryByMarketplace summary:result.getSummaries()) {
					ProductInfo product = getProductInfo(summary.getMarketplaceId(),result.getSku(),amazonAuthority.getId(),summary.getAsin());
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
					    		if(picture==null
					    				||picture.getUrl()==null
					    				||picture.getLocation()==null
					    				||!picture.getUrl().equals(summary.getMainImage().getLink())
					    				||(product.getLastupdate()!=null&&!(product.getLastupdate().compareTo(AmzDateUtils.getDate(summary.getLastUpdatedDate()))==0))
					    				) {
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
		      		    	AmzProductRefresh refresh=new AmzProductRefresh();
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
				    	opt.setLastupdate(new Date());
				    	productInOptMapper.updateById(opt);
				    }else {
				    	opt=new ProductInOpt();
				    	opt.setPid(new BigInteger(product.getId()));
				    	opt.setDisable(false);
				    	opt.setFnsku(product.getFnsku());
				    	productInOptMapper.insert(opt);
				    }
			 

				}
			}else if(mrefresh!=null){
				 getProductInfo(mrefresh.getMarketplaceid() ,result.getSku(),amazonAuthority.getId(),mrefresh.getAsin());
			}
			
			
			
		}
	
		@Override
		public void handlerFailure(AmazonAuthority auth, AmzProductRefresh skuRefresh, ApiException e) {
			// TODO Auto-generated method stub
			if(skuRefresh!=null&&skuRefresh.getMarketplaceid()==null) {
				iAmzProductRefreshService.removeById(skuRefresh.getPid());
			}
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
		        if(refresh==null) {
		        	iAmzProductRefreshService.removeById(skuRefresh.getPid());
		        }else {
		        	refresh.setDetailRefreshTime(LocalDateTime.now());
	    		 	iAmzProductRefreshService.updateById(refresh);
		        }
    			
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
