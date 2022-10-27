package com.wimoor.amazon.auth.service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonSellerMarket;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.model.sellers.GetMarketplaceParticipationsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 授权对应区域客户所有绑定的站点 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-25
 */
public interface IAmazonSellerMarketService extends IService<AmazonSellerMarket>,IRunAmazonService {
	 public List<AmazonSellerMarket> handler(AmazonAuthority amazonAuthority, GetMarketplaceParticipationsResponse response) ;
	 public void refreshMarketByAuth(AmazonAuthority amazonAuthority);
	 public	int deleteByLogic(AmazonSellerMarket amazonSellerMarketPlace);
	 public	int selectBySellerIdLogic(String sellerid);
	 public	int getCurrentMarketCountByShopId(String shopId);
	 public	List<Map<String,Object>> selectByGroup(String groupid);
	 public	List<AmazonSellerMarket> selectAllBySellerId(String sellerid);
}
