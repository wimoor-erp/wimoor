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

import com.amazon.spapi.api.CatalogApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.catalogitems.Item;
import com.amazon.spapi.model.catalogitems.ItemClassificationSalesRank;
import com.amazon.spapi.model.catalogitems.ItemDisplayGroupSalesRank;
import com.amazon.spapi.model.catalogitems.ItemImage;
import com.amazon.spapi.model.catalogitems.ItemImagesByMarketplace;
import com.amazon.spapi.model.catalogitems.ItemRelationship;
import com.amazon.spapi.model.catalogitems.ItemRelationships;
import com.amazon.spapi.model.catalogitems.ItemRelationshipsByMarketplace;
import com.amazon.spapi.model.catalogitems.ItemSalesRanks;
import com.amazon.spapi.model.catalogitems.ItemSalesRanksByMarketplace;
import com.amazon.spapi.model.catalogitems.ItemSearchResults;
import com.amazon.spapi.model.catalogitems.ItemSummaries;
import com.amazon.spapi.model.catalogitems.ItemSummaryByMarketplace;
import com.amazon.spapi.model.catalogitems.ItemImage.VariantEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.pojo.dto.ProductCatalogItemsDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.pojo.entity.ProductRank;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCatalogItemService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.product.service.IProductRankService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;

import cn.hutool.core.util.StrUtil;

@Service 
public class ProductCatalogItemServiceImpl implements IProductCatalogItemService {
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
	@Autowired
	IMarketplaceService iMarketplaceService;
	public ItemSearchResults searchCatalogProducts(AmazonAuthority auth,ProductCatalogItemsDTO dto) {
		// TODO Auto-generated method stub
		auth.setUseApi("searchCatalogItems");
		try {
			if(auth.apiNotRateLimit()) {
				List<String> marketplaceIds=dto.getMarketplaceIds();
				List<String> identifiers=dto.getIdentifiers();
				String identifiersType=dto.getIdentifiersType();
				List<String> includedData=dto.getIncludedData()!=null?dto.getIncludedData():new ArrayList<String>();
				String locale=dto.getLocale();
				String sellerId=dto.getSellerId();
				List<String> keywords=dto.getKeywords();
				List<String> brandNames=dto.getBrandNames();
				List<String> classificationIds=dto.getClassificationIds();
				Integer pageSize=dto.getPageSize();
				String pageToken=dto.getPageToken();
				String keywordsLocale=dto.getKeywordsLocale();
				includedData.add("salesRanks");
				includedData.add("images");
				includedData.add("attributes");
				includedData.add("summaries");
				CatalogApi api = apiBuildService.getCatalogApi(auth);
				ItemSearchResults response = api.searchCatalogItems(marketplaceIds,identifiers,identifiersType,includedData,locale,sellerId,
							keywords,brandNames,classificationIds,pageSize,pageToken,keywordsLocale);
			    return response;
			}else {
				throw new BizException("API申请频繁，请稍后重试");
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			auth.setApiRateLimit( null, e);
		}
		return null;
	}

	
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
		List<String> includedData=new ArrayList<String>();
		includedData.add("dimensions");
		includedData.add("attributes");
		return captureCatalogProduct( auth, asin, market, includedData) ;
	}

	@Override
	public Item captureCatalogProduct(AmazonAuthority auth,String asin, List<String> market, List<String> includedData) {
		// TODO Auto-generated method stub
		CatalogApi api = apiBuildService.getCatalogApi(auth);
		Item response =null;
		try {
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
			includedData.add("summaries");
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
				includedData.add("summaries");
				if(skuRefresh.getCatalogRefreshTime()==null) {
					includedData.add("images");
				}	
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
            	    			List<ProductInfo> asinParent = iProductInfoService.selectByAsin(auth.getId(), parentAsin ,marketplaceid);
            	    			for(ProductInfo parent:asinParent) {
            	    				parent.setIsparent(true);
            	    				iProductInfoService.updateById(parent);
            	    			}
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
    	    				info.setIsparent(true);
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
    	    	   productRank.setByday(new Date());
    	    	   productRank.setCategoryid(categoryId);
    	    	   productRank.setIsmain(false);
    	    	   productRank.setRank(rank);
    	    	   productRank.setTitle(itemClassificationSalesRank.getTitle());
    	    	   productRank.setLink(itemClassificationSalesRank.getLink());
    	    	   productRank.setProductId(info.getId());
    	    	   iProductRankService.insert(productRank);
    	       }
    	       for(ItemDisplayGroupSalesRank itemDisplayGroupSalesRank:displayGroupRanks) {
    	    	   //String  link=itemDisplayGroupSalesRank.getLink();
    	    	   Integer rank = itemDisplayGroupSalesRank.getRank();
    	    	   //String title=itemDisplayGroupSalesRank.getTitle();
    	    	   String categoryId=itemDisplayGroupSalesRank.getWebsiteDisplayGroup();
    	    	   ProductRank productRank=new ProductRank();
    	    	   productRank.setByday(new Date());
    	    	   productRank.setCategoryid(categoryId);
    	    	   productRank.setIsmain(true);
    	    	   productRank.setRank(rank);
    	    	   productRank.setTitle(itemDisplayGroupSalesRank.getTitle());
    	    	   productRank.setLink(itemDisplayGroupSalesRank.getLink());
    	    	   productRank.setProductId(info.getId());
    	    	   iProductRankService.insert(productRank);
    	       }
           }
       }
       ItemSummaries summary = result.getSummaries();
       String imageurl = null;
	if(summary!=null&&summary.size()>0) {
    	   for(ItemSummaryByMarketplace item:summary) {
    		   String brand=null;
    		   String manufacturer=null;
    		   if(item.getBrand()!=null) {
    			   brand=item.getBrand();
    		   }
    		   if(item.getManufacturer()!=null) {
    			   manufacturer=item.getManufacturer();
    		   }
			   ProductInfo info = iProductInfoService.productOnlyone(auth.getId(),skuRefresh.getSku(),  item.getMarketplaceId());
			   if(info.getName()==null) {
				   info.setName(item.getItemName());
			   }
		       info.setBrand(brand);
		       info.setTypename(result.getProductTypes().get(0).getProductType());
		       info.setManufacturer(manufacturer);
		       if(result.getImages()!=null&&result.getImages().size()>0) {
					ItemImagesByMarketplace image = result.getImages().get(0);
					List<ItemImage> images = image.getImages();
					if(images!=null&&images.size()>0) {
						for(ItemImage itemimage:images) {
							if(itemimage.getVariant().compareTo(VariantEnum.MAIN)==0&&itemimage.getWidth()==75) {
								imageurl=itemimage.getLink();
								break;
							}
						}
					}
					Picture picture=null;
			    	if(imageurl!=null) {
			    		String path=PictureServiceImpl.productImgPath+auth.getShopId()+"/"+auth.getId()+"/"+info.getMarketplaceid()+"/";
			    		try {
							picture=pictureService.downloadPicture(imageurl, path,null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		 if(picture!=null) {
			    		       	info.setImage(new BigInteger(picture.getId()));
			    		  }
				   } 
				}
		       iProductInfoService.updateById(info);
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
		ProductCatalogItemServiceImpl self = this;
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
 
	
	@Override
	public List<String> captureCatalogProductChildren(AmazonAuthority auth, String asin, List<String> market) {
		// TODO Auto-generated method stub
		List<String> includedData=new ArrayList<String>();
		includedData.add("relationships");
		Item res = this.captureCatalogProduct(auth, asin, market,includedData);
		if(res!=null&&res.getRelationships()!=null) {
			ItemRelationshipsByMarketplace rela = res.getRelationships().get(0);
			if(rela!=null&&rela.getRelationships().size()>0) {
				 List<String> parent = rela.getRelationships().get(0).getParentAsins();
				 if(parent!=null&&parent.size()>0) {
					   res = this.captureCatalogProduct(auth, parent.get(0), market,includedData);
				 }
			}
		}
		List<String> asins=new ArrayList<String>();
		for(ItemRelationshipsByMarketplace mkrelation: res.getRelationships()) {
		  for(ItemRelationship rela:mkrelation.getRelationships()) {
  	    	List<String> children = rela.getChildAsins();
  	    	for(String childasin:children) {
  	    		asins.add(childasin);
  	    	}
		  }
		}
		if(asins.size()==0) {
			asins.add(asin);
		}
		return asins;
	}
	
	public List<Item> captureCatalogProduct( AmazonAuthority auth, List<String> asins, List<String> market ){
		List<String> includedDataAttr=new ArrayList<String>();
    	includedDataAttr.add("summaries");
    	includedDataAttr.add("images");
		List<Item> result=new ArrayList<Item>();
		 if(asins.size()>0) {
 			try {
 				List<List<String>> listasin = GeneralUtil.getListByPageSize(asins, 20);
 				for(List<String> itemasinlist:listasin) {
 					ProductCatalogItemsDTO dto=new ProductCatalogItemsDTO();
	    				dto.setIdentifiersType("ASIN");
	    				dto.setIdentifiers(itemasinlist);
	    				dto.setIncludedData(includedDataAttr);
	    				dto.setMarketplaceIds(market);
	    				dto.setSellerId(auth.getSellerid());
	    				dto.setPageSize(20);
						ItemSearchResults itemres = this.searchCatalogProducts(auth, dto);
						for(Item res2:itemres.getItems()) {
							if(res2!=null&&res2.getSummaries()!=null) {
		  	    				for(ItemSummaryByMarketplace sitem:res2.getSummaries()) {
		  	    					String marketplaceid=sitem.getMarketplaceId();
		  	    					Marketplace marketobj = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
		  	    					sitem.setWebsiteDisplayGroup("https://www."+marketobj.getPointName()+"/dp/"+res2.getAsin());
		  	    				}
		  	    			}
		  	    			result.add(res2);
	    				 }
 				}
 			}catch(Exception e) {
 				e.printStackTrace();
 			}
 	 
 	    } 
		return result;
	}
}
