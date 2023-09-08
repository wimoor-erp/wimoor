package com.wimoor.amazon.product.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ListingsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.catalogitems.ItemImage;
import com.amazon.spapi.model.catalogitems.ItemImage.VariantEnum;
import com.amazon.spapi.model.catalogitems.ItemImagesByMarketplace;
import com.amazon.spapi.model.listings.FulfillmentAvailability;
import com.amazon.spapi.model.listings.Issue;
import com.amazon.spapi.model.listings.Item;
import com.amazon.spapi.model.listings.ItemSummaryByMarketplace;
import com.amazon.spapi.model.listings.ItemSummaryByMarketplace.StatusEnum;
import com.amazon.spapi.model.listings.ListingsItemPatchRequest;
import com.amazon.spapi.model.listings.ListingsItemPutRequest;
import com.amazon.spapi.model.listings.ListingsItemPutRequest.RequirementsEnum;
import com.amazon.spapi.model.listings.ListingsItemSubmissionResponse;
import com.amazon.spapi.model.listings.Money;
import com.amazon.spapi.model.listings.PatchOperation;
import com.amazon.spapi.model.listings.PatchOperation.OpEnum;
import com.amazon.spapi.model.listings.Issue.SeverityEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.squareup.okhttp.Call;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.feed.service.impl.FeedFileInventoryXML;
import com.wimoor.amazon.feed.service.impl.FeedFileProductXML;
import com.wimoor.amazon.product.mapper.AmzProductPriceRecordMapper;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.pojo.dto.ItemAttributesDTO;
import com.wimoor.amazon.product.pojo.dto.ProductListingItemDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductPriceRecord;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCatalogItemService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.product.service.IProductListingsItemService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.amazon.util.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;

@Service
public class ProductListingsItemServiceImpl implements IProductListingsItemService {
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
	IProductCatalogItemService iProductCatalogItemService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IMarketplaceService iMarketplaceService;
	@Autowired
	AmzProductPriceRecordMapper amzProductPriceRecordMapper;
	@Autowired
	ISubmitfeedService submitfeedService;
	@Override
	public Item captureListMatchingProduct(AmazonAuthority amazonAuthority, String sku, List<String> marketplaces,String issueLocale, List<String> includedData ) {
		// TODO Auto-generated method stub
		ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
		try {
			Item item = api.getListingsItem(amazonAuthority.getSellerid(), sku, marketplaces, issueLocale, includedData);
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
				List<ProductInfo> info = iProductInfoService.selectBySku(sku,marketplaceid, amazonauthid);
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
				}
			}
			ProductInfo product=null;
			if(refresh!=null) {
				 product=iProductInfoService.getById(refresh.getPid());
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
				Integer fulfillment=0;
				if(result.getFulfillmentAvailability()!=null&&result.getFulfillmentAvailability().size()>0) {
					for(FulfillmentAvailability item:result.getFulfillmentAvailability()) {
						if(item.getQuantity()!=null) {
							fulfillment=fulfillment+item.getQuantity();
						}
					}
				}

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
		            		||(!product.getSku().equals(result.getSku()))
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
		            		||!(product.getTypename()!=null&&product.getTypename().equals(summary.getProductType()))
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
			      		if(!product.getSku().equals(result.getSku())) {
			      			product.setSku(result.getSku());
			      		}
		      		    product.setStatus(mystatus);
		      		    product.setRefreshtime(LocalDateTime.now());
		      		    if(product.idIsNULL()) {
		      		    	iProductInfoService.save(product);
		      		    	AmzProductRefresh refresh=new AmzProductRefresh();
							refresh.setPid(new BigInteger(product.getId()));
							refresh.setSku(result.getSku());
						    refresh.setAsin(summary.getAsin());
						    refresh.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
						    refresh.setDetailRefreshTime(LocalDateTime.now());
						    refresh.setNotfound(false);
							iAmzProductRefreshService.save(refresh);
		      		    }else {
		      		    	iProductInfoService.updateById(product);
		      		    }
		            }
		           // &&(opt.getFnsku()==null ||!opt.getFnsku().equals(product.getFnsku()))
				    if(opt!=null) {
				    	opt.setFnsku(product.getFnsku());
				    	opt.setLastupdate(new Date());
				    	opt.setFulfillmentAvailability(fulfillment);
				    	productInOptMapper.updateById(opt);
				    }else {
				    	opt=new ProductInOpt();
				    	opt.setPid(new BigInteger(product.getId()));
				    	opt.setDisable(false);
				    	opt.setFulfillmentAvailability(fulfillment);
				    	opt.setFnsku(product.getFnsku());
				    	ProductInOpt one = productInOptMapper.selectById(product.getId());
				    	if(one!=null) {
				    		productInOptMapper.updateById(opt);
				    	}else {				    		
				   		   productInOptMapper.insert(opt);
				    	}
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
		    		 	ProductInfo info = iProductInfoService.getById(refresh.getPid());
		    		 	info.setInvalid(true);
		    		 	iProductInfoService.updateById(info);
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
		ProductListingsItemServiceImpl self = this;
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


		@Override
		public ProductInfo pushAsin(ProductListingItemDTO dto) {
			// TODO Auto-generated method stub
			AmazonAuthority amazonAuthority = amazonAuthorityService.getById(dto.getAmazonauthid());
			Marketplace market = iMarketplaceService.getById(dto.getMarketplaceids().get(0));
			List<ProductInfo> info = iProductInfoService.selectBySku(dto.getSku(), market.getMarketplaceid(), amazonAuthority.getId());
			if(info==null||info.size()==0) {
				throw new BizException("未找到对应分类");
			}
			ProductInfo infoone = info.get(0);
			ProductInOpt opt = productInOptMapper.selectById(infoone.getId());
			ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
			ListingsItemPutRequest body=new ListingsItemPutRequest();
			body.setProductType("PRODUCT");
			ItemAttributesDTO itemAttribute=new ItemAttributesDTO();
			itemAttribute.setCondition_type("new_new");
			itemAttribute.setFulfillment_availability(dto.getFilfullable());
			itemAttribute.setMax_order_quantity(dto.getMaxOrderQuantity());
			if(dto.getMerchantShippingGroup()!=null ) {
				itemAttribute.setMerchant_shipping_group(dto.getMerchantShippingGroup());
			}else {
				itemAttribute.setMerchant_shipping_group(opt.getMerchantShippingGroup());
			}
			itemAttribute.setFulfillment_channel_code("DEFAULT");
			if(dto.getPrice()!=null) {
				Money price=new Money();
				BigDecimal myprice = new BigDecimal(dto.getPrice());
				myprice=myprice.setScale(2);
				price.setAmount(myprice.toString());
				price.setCurrencyCode(market.getCurrency());
				itemAttribute.setList_price(price);
				itemAttribute.setPurchasable_price(price);
			}else if(opt!=null&&opt.getBuyprice()!=null) {
				Money price=new Money(); 
				price.setAmount(opt.getBuyprice().toString());
				price.setCurrencyCode(market.getCurrency());
				itemAttribute.setList_price(price);
				itemAttribute.setPurchasable_price(price);
			}
			if(dto.getLeaddays()!=null) {
				itemAttribute.setLead_time_to_ship_max_days(dto.getLeaddays());
			}
			RequirementsEnum req=RequirementsEnum.LISTING_OFFER_ONLY;
			body.setRequirements(req);
			body.setAttributes(itemAttribute.getJson(dto.getMarketplaceids(), dto.getAsin()));
			ListingsItemSubmissionResponse resp =null;
			try {
				 resp = api.putListingsItem(amazonAuthority.getSellerid(), dto.getSku(), dto.getMarketplaceids(), body, null);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean haserror=false;
			String error=null;
			if(resp!=null&&!resp.getIssues().isEmpty()&&resp.getIssues().size()>0) {
				for(Issue issues:resp.getIssues()) {
					if(issues.getSeverity().equals(SeverityEnum.ERROR)) {
						haserror=true;
						error=issues.getMessage();
					}
				}
			}
			if(resp!=null&&haserror==false) {
				infoone.setStatus(resp.getStatus().getValue());
				infoone.setInvalid(false);
				if(StrUtil.isNotBlank(dto.getFilfullable())&& Integer.parseInt(dto.getFilfullable())>0) {
					infoone.setDisable(false);
				}else {
					infoone.setDisable(true);
				}
				if(dto.getPrice()!=null) {
					infoone.setPrice(new BigDecimal(dto.getPrice()));
				}
				this.iProductInfoService.updateById(infoone);
				
				if(opt!=null) {
					if(StrUtil.isNotBlank(dto.getFilfullable())) {
						opt.setFulfillmentAvailability(Integer.parseInt(dto.getFilfullable()));
					}
					if(StrUtil.isNotBlank(dto.getPrice())) {
						opt.setBuyprice(new BigDecimal(dto.getPrice()));
					}
					if(StrUtil.isNotBlank(dto.getMerchantShippingGroup())) {
						opt.setMerchantShippingGroup(dto.getMerchantShippingGroup());
					}
					productInOptMapper.updateById(opt);
				}else {
					ProductInOpt productInOpt = new ProductInOpt();
					productInOpt.setPid(new BigInteger(infoone.getId()));
					productInOpt.setLastupdate(new Date());
					if(dto.getFilfullable()!=null) {
						productInOpt.setFulfillmentAvailability(Integer.parseInt(dto.getFilfullable()));
					}
					if(dto.getPrice()!=null) {
						productInOpt.setBuyprice(new BigDecimal(dto.getPrice()));
					}
					if(StrUtil.isNotBlank(dto.getMerchantShippingGroup())) {
						productInOpt.setMerchantShippingGroup(dto.getMerchantShippingGroup());
					}
					productInOptMapper.insert(productInOpt);
				}
			}else {
				throw new BizException(error);
			}
			return infoone;
		}


		@Override
		public Object deleteAsin(ProductListingItemDTO dto) {
			// TODO Auto-generated method stub
			AmazonAuthority amazonAuthority = amazonAuthorityService.getById(dto.getAmazonauthid());
			List<ProductInfo> oldinfo = iProductInfoService.selectBySku(dto.getSku(), dto.getMarketplaceids().get(0), amazonAuthority.getId());
			ProductInfo infoone =null;
			if(oldinfo!=null&&oldinfo.size()>0) {
				infoone=oldinfo.get(0);
			}
			if(infoone!=null&&infoone.getInvalid()==true) {
				return null;
			}
			ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
			ListingsItemSubmissionResponse response = null;
			try {
				  response = api.deleteListingsItem(amazonAuthority.getSellerid(), dto.getSku(), dto.getMarketplaceids(), null);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean haserror=false;
			if(!response.getIssues().isEmpty()&&response.getIssues().size()>0) {
				for(Issue issues:response.getIssues()) {
					if(issues.getSeverity().equals(SeverityEnum.ERROR)) {
						haserror=true;
					}
				}
			}
			if(haserror==false&&infoone!=null) {
				infoone.setStatus(response.getStatus().getValue());
				infoone.setInvalid(true);
				this.iProductInfoService.updateById(infoone);
			}
			return response;
		}


		@Override
		public ProductInfo saveAsin(ProductListingItemDTO dto) {
			// TODO Auto-generated method stub
			AmazonAuthority amazonAuthority = amazonAuthorityService.getById(dto.getAmazonauthid());
			if(amazonAuthority==null) {
				throw new BizException("未找到对应授权");
			}
			if(dto.getMarketplaceids()==null||dto.getMarketplaceids().size()==0) {
				throw new BizException("未找到对应站点");
			}
			Marketplace market = iMarketplaceService.getById(dto.getMarketplaceids().get(0));
			if(market==null) {
				throw new BizException("未找到对应站点");
			}
			ProductInfo info=null;
			List<ProductInfo> oldinfo = iProductInfoService.selectBySku(dto.getSku(), market.getMarketplaceid(), amazonAuthority.getId());
			if(oldinfo!=null&&oldinfo.size()>0) {
				 info=oldinfo.get(0);
				 ProductInOpt productInOpt = productInOptMapper.selectById(info.getId());
				 productInOpt.setLastupdate(new Date());
				 if(dto.getPrice()!=null) {
					 productInOpt.setBuyprice(new BigDecimal(dto.getPrice()));
				 }
				 if(StrUtil.isNotBlank(dto.getMerchantShippingGroup())) {
						productInOpt.setMerchantShippingGroup(dto.getMerchantShippingGroup());
				 }
				 Picture picture=null;
			    	if(dto.getImage()!=null) {
			    		String path=PictureServiceImpl.productImgPath+amazonAuthority.getShopId()+"/"+amazonAuthority.getId()+"/"+info.getMarketplaceid()+"/";
			    		try {
							picture=pictureService.downloadPicture(dto.getImage(), path,null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		 if(picture!=null) {
			    		       	info.setImage(new BigInteger(picture.getId()));
			    		     	iProductInfoService.updateById(info);
			    		  }
				   } 
				 productInOptMapper.updateById(productInOpt);
			}else {
				info=new ProductInfo();
				info.setSku(dto.getSku());
				info.setAsin(dto.getAsin());
				info.setAmazonAuthId(new BigInteger(dto.getAmazonauthid()));
				if(info.getMarketplaceid()==null) {
					info.setMarketplaceid(market.getMarketplaceid());
				}
				info.setFulfillChannel("DEFAULT");
			    info.setInvalid(true);
			    info.setDisable(true);
			    info.setPrice(new BigDecimal(dto.getPrice()));
			    Picture picture=null;
		    	if(dto.getImage()!=null) {
		    		String path=PictureServiceImpl.productImgPath+amazonAuthority.getShopId()+"/"+amazonAuthority.getId()+"/"+info.getMarketplaceid()+"/";
		    		try {
						picture=pictureService.downloadPicture(dto.getImage(), path,null);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		 if(picture!=null) {
		    		       	info.setImage(new BigInteger(picture.getId()));
		    		     	iProductInfoService.updateById(info);
		    		  }
			    } 
			    iProductInfoService.save(info);
			    ProductInOpt productInOpt = new ProductInOpt();
				productInOpt.setPid(new BigInteger(info.getId()));
				productInOpt.setLastupdate(new Date());
				if(dto.getFilfullable()!=null) {
					productInOpt.setFulfillmentAvailability(Integer.parseInt(dto.getFilfullable()));
				}
				if(StrUtil.isNotBlank(dto.getMerchantShippingGroup())) {
					productInOpt.setMerchantShippingGroup(dto.getMerchantShippingGroup());
				}
				productInOpt.setBuyprice(new BigDecimal(dto.getPrice()));
				productInOptMapper.insert(productInOpt);
			 
				AmzProductRefresh refresh=new AmzProductRefresh();
				refresh.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
				refresh.setAsin(dto.getAsin());
				refresh.setSku(dto.getSku());
				refresh.setMarketplaceid(info.getMarketplaceid());
				refresh.setPid(new BigInteger(info.getId()));
				refresh.setNotfound(false);
				refresh.setIsparent(false);
				iAmzProductRefreshService.save(refresh);
				iProductCatalogItemService.captureCatalogProductSync(amazonAuthority,refresh, dto.getMarketplaceids());
			}
			return info;
		}

 
		@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
		public  ListingsItemSubmissionResponse changePriceSku(ProductListingItemDTO dto) {
				AmazonAuthority amazonAuthority = amazonAuthorityService.getById(dto.getAmazonauthid());
				Marketplace market = iMarketplaceService.getById(dto.getMarketplaceids().get(0));
				List<ProductInfo> info = iProductInfoService.selectBySku(dto.getSku(), market.getMarketplaceid(), amazonAuthority.getId());
				if(info==null||info.size()==0||info.get(0).getTypename()==null) {
					throw new BizException("未找到对应分类");
				}
				ProductInfo info_one = info.get(0);
				ProductInOpt opt = productInOptMapper.selectById(info_one.getId());
				ListingsItemSubmissionResponse resp = changePriceSku(dto.getUserid(),amazonAuthority,market,info_one,opt,dto.getPrice(),dto.getFromDate(),dto.getToDate());
				boolean haserror=false;
				if(resp!=null&&!resp.getIssues().isEmpty()&&resp.getIssues().size()>0) {
					for(Issue issues:resp.getIssues()) {
						if(issues.getSeverity().equals(SeverityEnum.ERROR)) {
							haserror=true;
						}
					}
				}
				if(resp!=null&&haserror==false) {
					opt.setBuyprice(new BigDecimal(dto.getPrice()));
					info_one.setPrice(new BigDecimal(dto.getPrice()));
					productInOptMapper.updateById(opt);
					iProductInfoService.updateById(info_one);
				}
				return resp;
		}
		
	 
		@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
		public  ListingsItemSubmissionResponse changePriceSku(String userid,AmazonAuthority amazonAuthority,Marketplace market,ProductInfo info,ProductInOpt opt,String newprice,String fromDate,String toDate) {
			    // TODO Auto-generated method stub
				ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
				ListingsItemSubmissionResponse resp=null;
				try {
					ListingsItemPatchRequest body=new ListingsItemPatchRequest();
					body.setProductType("PRODUCT");
					List<PatchOperation> patches=new ArrayList<PatchOperation>();
					PatchOperation e=new PatchOperation();
					e.setOp(OpEnum.REPLACE);
					String path = "/attributes/purchasable_offer";
					e.setPath(path);
					ItemAttributesDTO itemAttribute=new ItemAttributesDTO();
					Integer ftype=1;
					if(StrUtil.isNotEmpty(fromDate)) {
						Money price2=new Money();
						price2.setAmount(newprice);
						ftype=2;
						price2.setCurrencyCode(market.getCurrency());
						itemAttribute.setDiscounted_price(price2);
						itemAttribute.setDiscounted_price_start_at(fromDate);
						itemAttribute.setDiscounted_price_end_at(toDate);
					}else {
						Money price=new Money();
						price.setAmount(newprice);
						price.setCurrencyCode(market.getCurrency());
						itemAttribute.setPurchasable_price(price);
					}
					e.addValueItem(itemAttribute.getJsonPurchasableOffer(info.getMarketplaceid()));
					patches.add(e);
					body.setPatches(patches);
					resp = api.patchListingsItem(amazonAuthority.getSellerid(),info.getSku(),Arrays.asList(market.getMarketplaceid()), body, null);
					if(resp!=null && "ACCEPTED".equals(resp.getStatus().getValue())) {
						//改价成功
						AmzProductPriceRecord entity=new AmzProductPriceRecord();
						if(StrUtil.isNotEmpty(fromDate)) {
							entity.setFtype(2);
							entity.setStartdate(GeneralUtil.getDatez(fromDate));
							entity.setEnddate(GeneralUtil.getDatez(toDate));
						}else {
							entity.setFtype(1);
						}
						entity.setPrice(new BigDecimal(newprice));
						entity.setOpttime(new Date());
						entity.setOperator(userid);
						entity.setPid(info.getId());
						entity.setRemark("操作立即调价");
						entity.setOldprice(info.getPrice());
						entity.setFtype(ftype);
						amzProductPriceRecordMapper.insert(entity);
					}
				} catch (ApiException e) {
					e.printStackTrace();
				}
				return resp;
		}

		@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
		public  ListingsItemSubmissionResponse changeShipTemplate(String userid,AmazonAuthority amazonAuthority,Marketplace market,ProductInfo info,ProductInOpt opt) {
			    // TODO Auto-generated method stub
				ListingsApi api = apiBuildService.getProductApi(amazonAuthority);
				ListingsItemSubmissionResponse resp=null;
				try {
					ListingsItemPatchRequest body=new ListingsItemPatchRequest();
					body.setProductType("PRODUCT");
					List<PatchOperation> patches=new ArrayList<PatchOperation>();
					PatchOperation e=new PatchOperation();
					e.setOp(OpEnum.REPLACE);
					String path = "/attributes/merchant_shipping_group";
					e.setPath(path);
					ItemAttributesDTO itemAttribute=new ItemAttributesDTO();
					itemAttribute.setMerchant_shipping_group(opt.getMerchantShippingGroup());
					e.addValueItem(itemAttribute.getJsonMerchantShippingGroup(info.getMarketplaceid()));
					patches.add(e);
					body.setPatches(patches);
					resp = api.patchListingsItem(amazonAuthority.getSellerid(),info.getSku(),Arrays.asList(market.getMarketplaceid()), body, null);
					if(resp!=null && "ACCEPTED".equals(resp.getStatus().getValue())) {
						//改价成功
						this.productInOptMapper.updateById(opt); 
					}
				} catch (ApiException e) {
					e.printStackTrace();
				}
				return resp;
		} 
		
		
		public Result<?> updateShipGroupName(AmazonAuthority amazonAuthority,ProductInfo info,Marketplace marketplace,String shipGroupName) {
	    	amazonAuthority.setMarketPlace(marketplace);
	    	ByteArrayOutputStream productXml=null;
			try {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("sku", info.getSku());
				map.put("shipGroupName",shipGroupName);
				  productXml = FeedFileProductXML.getFile(amazonAuthority ,map);
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
			if (productXml != null && amazonAuthority != null && user != null) {
				response = submitfeedService.SubmitFeedQueue(productXml, "Product"+info.getSku(), amazonAuthority, FeedFileInventoryXML.type, user,null);
			} else {
				throw new BizException("系统改运费模板!");
			}
	        return Result.success(response);
	    }
}
