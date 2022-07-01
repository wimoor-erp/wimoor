package com.wimoor.amazon.auth.mapper;
 
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
 
@Mapper
public interface AmazonAuthorityMapper extends BaseMapper<AmazonAuthority> {
	
	AmazonAuthority selectBySellerId(String sellerid);

	List<AmazonAuthority> selectByshopid(String shopid);

	List<AmazonAuthority> selectByShopAndMarket(@Param("shopid") String shopid, @Param("marketplaceid") String marketplaceid);

	List<AmazonAuthority> selectByMarket(@Param("marketplaceid") String marketplaceid);

	List<Map<String, Object>> findShopList(@Param("shopId") String shopId, @Param("groupname") String groupname);

	AmazonAuthority selectBySellerIdAndShopId(AmazonAuthority shop);

	Long getCurrentShopCountByShopId(String shopId);

	AmazonAuthority selectAllBySellerId(AmazonAuthority shop);

	Integer selectMarketPlaceById(String amazonAuthId);

	AmazonAuthority selectByGroupAndMarket(@Param("groupid") String groupid, @Param("marketplaceid") String marketplaceid);

	List<AmazonAuthority> selectByGroupAndMarket(@Param("marketplaceid") String marketplaceid);

	List<Map<String, Object>> selectFinByShopid(@Param("shopid") String shopid, @Param("groupid") String groupid);

	List<AmazonAuthority> getNeedDelete();

	List<AmazonAuthority> selectByRegion(String region);
	
    String uuid();
}
