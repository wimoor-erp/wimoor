package com.wimoor.amazon.auth.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.SellersApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.sellers.GetMarketplaceParticipationsResponse;
import com.amazon.spapi.model.sellers.Marketplace;
import com.amazon.spapi.model.sellers.MarketplaceParticipation;
import com.amazon.spapi.model.sellers.MarketplaceParticipationList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.AmazonSellerMarketMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonSellerMarket;
import com.wimoor.amazon.auth.service.IAmazonSellerMarketService;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 授权对应区域客户所有绑定的站点 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-25
 */
@Service
public class AmazonSellerMarketServiceImpl extends ServiceImpl<AmazonSellerMarketMapper, AmazonSellerMarket> implements IAmazonSellerMarketService {
	@Autowired
	ApiBuildService apiBuildService;
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getMarketplaceParticipations");
		SellersApi api = apiBuildService.getSellersApi(amazonAuthority);
		try {
			if(amazonAuthority.apiNotRateLimit()) {
				ApiCallback<GetMarketplaceParticipationsResponse> callback=new ApiCallbackGetMarketplaceParticipations(this,amazonAuthority);
			    api.getMarketplaceParticipationsAsync(callback);
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			amazonAuthority.setApiRateLimit(null, e);
		}
	}
	
	public void refreshMarketByAuth(AmazonAuthority amazonAuthority) {
		SellersApi api = apiBuildService.getSellersApi(amazonAuthority);
		try {
			 GetMarketplaceParticipationsResponse response = api.getMarketplaceParticipations();
			 handler(amazonAuthority,response);
		   } catch (ApiException e) {
			    // TODO Auto-generated catch block
			     e.printStackTrace();
			     throw new BizException("API调用错误："+e.getMessage());
		}
	 
	}
	
	public List<AmazonSellerMarket> handler(AmazonAuthority amazonAuthority, GetMarketplaceParticipationsResponse response) {
		// TODO Auto-generated method stub
		List<AmazonSellerMarket> result=new ArrayList<AmazonSellerMarket>();
		MarketplaceParticipationList list = response.getPayload();
		 for(MarketplaceParticipation marketplaceParticipation:list) {
			 Marketplace market = marketplaceParticipation.getMarketplace();
			 LambdaQueryWrapper<AmazonSellerMarket> query=new LambdaQueryWrapper<AmazonSellerMarket>()
		              .eq(AmazonSellerMarket::getSellerid, amazonAuthority.getSellerid())
		              .eq(AmazonSellerMarket::getMarketplaceId,market.getId());
		      AmazonSellerMarket sellerMarket = this.baseMapper.selectOne(query);
		      if(sellerMarket!=null) {
		    		  sellerMarket.setName(market.getName());
			  		  sellerMarket.setCountry(market.getCountryCode());
			  		  sellerMarket.setCurrency(market.getDefaultCurrencyCode());
			  		  sellerMarket.setLanguage(market.getDefaultLanguageCode());
			  		  sellerMarket.setDomain(market.getDomainName());
			  		  if(StrUtil.isNotBlank(amazonAuthority.getId())) {
		  				sellerMarket.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
		  			  }
		  			  sellerMarket.setDisable(false);
		  			  sellerMarket.setOpttime(LocalDateTime.now());
		  			  sellerMarket.setSellerid(amazonAuthority.getSellerid());
		  			  sellerMarket.setMarketplaceId(market.getId());
		    		  result.add(sellerMarket);
		    		  this.baseMapper.update(sellerMarket, query);
		      }else {
		    	    sellerMarket=new AmazonSellerMarket();
		  			sellerMarket.setName(market.getName());
		  			sellerMarket.setCountry(market.getCountryCode());
		  			sellerMarket.setCurrency(market.getDefaultCurrencyCode());
		  			sellerMarket.setLanguage(market.getDefaultLanguageCode());
		  			sellerMarket.setDomain(market.getDomainName());
		  			if(StrUtil.isNotBlank(amazonAuthority.getId())) {
		  				sellerMarket.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
		  			}
		  			sellerMarket.setDisable(false);
		  			sellerMarket.setOpttime(LocalDateTime.now());
		  			sellerMarket.setSellerid(amazonAuthority.getSellerid());
		  			sellerMarket.setMarketplaceId(market.getId());
		  			result.add(sellerMarket);
		  		    this.baseMapper.insert(sellerMarket);
		      }
		 }
		 return result;
	}

	@SuppressWarnings("unused")
	private boolean campare(AmazonAuthority amazonAuthority,AmazonSellerMarket sellerMarket, Marketplace market) {
		// TODO Auto-generated method stub
		boolean needupdate=false;
		if(sellerMarket.getName()==null||!sellerMarket.getName().equals(market.getName())) {
			sellerMarket.setName(market.getName());
			needupdate=true;
		}
		if(sellerMarket.getCountry()==null||!sellerMarket.getCountry().equals(market.getCountryCode())) {
			sellerMarket.setCountry(market.getCountryCode());
			needupdate=true;
		}
		if(sellerMarket.getCurrency()==null||!sellerMarket.getCurrency().equals(market.getDefaultCurrencyCode())) {
			sellerMarket.setCurrency(market.getDefaultCurrencyCode());
			needupdate=true;
		}
		if(sellerMarket.getLanguage()==null||!sellerMarket.getLanguage().equals(market.getDefaultLanguageCode())) {
			sellerMarket.setLanguage(market.getDefaultLanguageCode());
			needupdate=true;
		}
		if(sellerMarket.getDomain()==null||!sellerMarket.getDomain().equals(market.getDomainName())) {
			sellerMarket.setDomain(market.getDomainName());
			needupdate=true;
		}
		if(sellerMarket.getAmazonauthid()==null||!sellerMarket.getAmazonauthid().toString().equals(amazonAuthority.getId())) {
			if(StrUtil.isNotBlank(amazonAuthority.getId())) {
  				sellerMarket.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
  				needupdate=true;
  			}
		}
		if(needupdate==true) {
			sellerMarket.setOpttime(LocalDateTime.now());
		}
		return needupdate;
	}
	
	public int deleteByLogic(AmazonSellerMarket amazonSellerMarketPlace) {
	 return this.baseMapper.deleteByLogic(amazonSellerMarketPlace);	
	}
	
	public int selectBySellerIdLogic(String sellerid) {
		return this.baseMapper.selectBySellerIdLogic(sellerid);
	}
	
	public int getCurrentMarketCountByShopId(String shopId) {
		return this.getCurrentMarketCountByShopId(shopId);
	}
	
	public List<Map<String,Object>> selectByGroup(String groupid){
		return this.baseMapper.selectByGroup(groupid);
	}
	
	public List<AmazonSellerMarket> selectAllBySellerId(String sellerid){
		return this.baseMapper.selectAllBySellerId(sellerid);
	}
}
