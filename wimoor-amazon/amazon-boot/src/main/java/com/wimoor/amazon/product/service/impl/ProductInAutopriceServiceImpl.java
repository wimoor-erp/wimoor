package com.wimoor.amazon.product.service.impl;

import com.wimoor.amazon.product.pojo.dto.ProductListingItemDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductPriceRecord;
import com.wimoor.amazon.product.pojo.entity.FollowOfferChange;
import com.wimoor.amazon.product.pojo.entity.ProductInAutoprice;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.product.mapper.ProductInAutopriceMapper;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.service.IAmzProductPriceRecordService;
import com.wimoor.amazon.product.service.IProductInAutopriceService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.product.service.IProductListingsItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-20
 */
@Service
public class ProductInAutopriceServiceImpl extends ServiceImpl<ProductInAutopriceMapper, ProductInAutoprice> implements IProductInAutopriceService {
	@Autowired
    IProductListingsItemService iProductListingsItemService;
	@Autowired
	IAmzProductPriceRecordService iAmzProductPriceRecordService;
	@Resource
	private IProductInfoService iProductInfoService;
	@Resource
	private ProductInOptMapper productInOptMapper;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IMarketplaceService iMarketplaceService;
	
	@Override
	public void changeTaskPriceSku(String asin, String marketplaceid, Map<String, FollowOfferChange> mapseller) {
		LambdaQueryWrapper<ProductInfo> query=new LambdaQueryWrapper<ProductInfo>();
		query.eq(ProductInfo::getAsin, asin);
		query.eq(ProductInfo::getMarketplaceid, marketplaceid);
		query.eq(ProductInfo::getInvalid, false);
		List<ProductInfo> list = iProductInfoService.list(query);
		if(list==null||list.size()<=0) {
			return;
		}
		FollowOfferChange lowestseller=null;
		FollowOfferChange buyboxseller=null;
		BigDecimal lowestprice =null;
		for(ProductInfo info:list) {
			if(info==null) {
				continue;
			}
			ProductInOpt opt = this.productInOptMapper.selectById(info.getId());
			if((info.getInvalid()!=null&&info.getInvalid()==true)||(opt!=null&&opt.getDisable()!=null&&opt.getDisable()==true)) {
				continue;
			}
			ProductInAutoprice auto = this.getById(info.getId());
			boolean disable=((auto==null||auto.getDisable()==null)?false:auto.getDisable());
			if(auto==null||disable==true||auto.getLowestprice()==null||auto.getLowestprice().floatValue()<0.00001) {
				continue;
			}
			BigDecimal step = auto.getDownStepPrice()==null?new BigDecimal("0.01"):auto.getDownStepPrice();
			BigDecimal up=auto.getUpStepPrice()==null?new BigDecimal("0.1"):auto.getUpStepPrice();
			Integer type=auto.getFtype()==null?2:auto.getFtype();
			AmazonAuthority amazonAuthority = amazonAuthorityService.getById(info.getAmazonAuthId());
			FollowOfferChange myseller = mapseller.get(amazonAuthority.getSellerid());
		    if(lowestprice==null) {
		    	 for(Entry<String, FollowOfferChange> entry:mapseller.entrySet()) {
		    		  FollowOfferChange value=entry.getValue();
		    		  BigDecimal price=value.getListingPriceAmount()!=null?value.getListingPriceAmount():new BigDecimal("0");
		    		  price=price.add(value.getShipingAmount()!=null?value.getShipingAmount():new BigDecimal("0"));
		    		 
			    		  if(lowestprice==null||lowestprice.compareTo(price)>0) {
								lowestprice=price;
								lowestseller=value;
							}
			    		  if(value.getIsBuyBoxWinner()) {
								buyboxseller=value;
							}
		    		  
		    	  }
		    }
		 
		    if(type==2) {
		    	if(buyboxseller!=null) {
		    		 BigDecimal price=buyboxseller.getListingPriceAmount()!=null?buyboxseller.getListingPriceAmount():new BigDecimal("0");
	    			 price=price.add(buyboxseller.getShipingAmount()!=null?buyboxseller.getShipingAmount():new BigDecimal("0"));
			    	 lowestprice=price;
		    	}
		    }
			String fulfillchannel="FBM";
			BigDecimal myprice = new BigDecimal("10000000.00");
			if(myseller!=null) {
				if(myseller.getIsFulfilledByAmazon()) {
					fulfillchannel="FBA";
				}
				FollowOfferChange asinseller = myseller;
				BigDecimal lprice = asinseller.getListingPriceAmount()!=null?asinseller.getListingPriceAmount():new BigDecimal("0");
				BigDecimal ship=asinseller.getShipingAmount()!=null?asinseller.getShipingAmount():new BigDecimal("0");
				myprice=lprice.add(ship);
			}
			if(myprice==null) {
				if(opt!=null) {
					myprice=opt.getBuyprice();
				}else {
					myprice=info.getPrice();
				}
			}
			String remark="抢"+fulfillchannel+(type==1?"最低价":"购物车")+"，固定降价";
			if(amazonAuthority.getSellerid().equals(lowestseller.getSellerid())) {
				if(myseller!=null) {
				    if(myseller.getIsBuyBoxWinner()) {
				    	  BigDecimal secondprice=null;
				    	  for(Entry<String, FollowOfferChange> entry:mapseller.entrySet()) {
				    		  FollowOfferChange value=entry.getValue();
				    		  if(!value.getSellerid().equals(amazonAuthority.getSellerid())) {
				    			    BigDecimal price=value.getListingPriceAmount()!=null?value.getListingPriceAmount():new BigDecimal("0");
				    				price=price.add(value.getShipingAmount()!=null?value.getShipingAmount():new BigDecimal("0"));
				    				if(secondprice==null||secondprice.compareTo(price)>0) {
				    					secondprice=price;
				    				}
				    		  }
				    	  }
				    	  if(secondprice!=null&&secondprice.subtract(lowestprice).compareTo(up)>=0) {
				    		  remark="拥有购物车，匹配"+fulfillchannel+"竞争对手价格";
				    		  lowestprice=secondprice;
				    	  }else {
				    		  continue;
				    	  }
				    	
				    }else {
				    	continue;
				    }
				}else {
					continue;
				}
			}
			BigDecimal referPrice=new BigDecimal(lowestprice.toString());
			lowestprice=lowestprice.subtract(step);
			if(auto.getLowestprice().compareTo(lowestprice)>=0) {
				if(auto.getLowestprice().compareTo(myprice)<0) {
					lowestprice=auto.getLowestprice();
				}else {
					continue;
				}
			}
			if(myprice.subtract(lowestprice).floatValue()<0.0001) {
				if(myseller!=null) {
					if(myseller.getIsBuyBoxWinner()!=null&&!myseller.getIsBuyBoxWinner()) {
						continue; 
					}
				}else {
					continue; 
				}
			}
			if(myseller!=null&&myseller.getListingPriceAmount()!=null) {
				lowestprice=lowestprice.subtract(myseller.getShipingAmount());
			}
			    ProductListingItemDTO dto=new ProductListingItemDTO();
			    dto.setAmazonauthid(info.getAmazonAuthId().toString());
			    dto.setAsin(info.getAsin());
			    dto.setSku(info.getSku());
			    dto.setPrice(lowestprice.toString());
			    dto.setMarketplaceids(Arrays.asList(marketplaceid));
				ProductInfo info2 = iProductListingsItemService.pushAsin(dto);
			if(info2!=null) {
				AmzProductPriceRecord entity=new AmzProductPriceRecord();
				entity.setPid(info.getId());
				entity.setRemark(remark);
				entity.setPrice(lowestprice);
				entity.setRefprice(referPrice);
				if(myseller!=null&&myseller.getListingPriceAmount()!=null) {
					entity.setShipprice(myseller.getShipingAmount());
					entity.setOldshipprice(myseller.getShipingAmount());
					entity.setOldprice(myseller.getListingPriceAmount());
				}else {
					entity.setOldprice(myprice);
				}
				Calendar c=Calendar.getInstance();
				entity.setOpttime(c.getTime());
				entity.setLowestprice(auto.getLowestprice());
				try {
					iAmzProductPriceRecordService.save(entity);
				}catch(Exception e) {
					LambdaQueryWrapper<AmzProductPriceRecord> updateWrapper=new LambdaQueryWrapper<AmzProductPriceRecord>();
					updateWrapper.eq(AmzProductPriceRecord::getPid,entity.getPid());
					updateWrapper.eq(AmzProductPriceRecord::getOpttime, entity.getOpttime());
					iAmzProductPriceRecordService.update(entity, updateWrapper);
				}
			
				 
			}
		}
		
		
	}
}
