package com.wimoor.amazon.auth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;

 
@Mapper
public interface MarketplaceMapper extends BaseMapper<Marketplace> {

	/**
	 * 根据用户绑定的账号所对应的记录ID获取该账号对应的marketplace
	 * @param id
	 * @return
	 */
	List<Marketplace> findbyauth(String id);
	
	/**
	 * 根据区域查询marketplace
	 * @param region
	 * @return
	 */
	List<Marketplace> findbyRegion(String region);
	
	/**
	 * 查找 店铺下面对应role的marketplace，当roleid为空，查询该店铺下面所有的marketplace
	 *
	 * @param roleid 当该参数为空的时候系统自动不采用该参数，只使用shopid进行查询。
	 * @param shopid
	 * @return
	 */
	List<Marketplace> findByRole(@Param("roleid")String roleid,@Param("shopid")String shopid);
	
	/**
	 * 查询该用户所对应所有角色的marketplace。
	 * @param userid
	 * @return
	 */
	List<Marketplace> findByUser(@Param("userid")String userid,@Param("shopid")String shopid);
	
	List<Marketplace> findByGroup(@Param("groupid")String groupid);

	List<Marketplace> selectMarketByGroupid(@Param("shopid")String shopid,@Param("groupid")String groupid);
	
	List<Marketplace> selectMarketBySellerid(Map<String, Object> param);
	
	List<Marketplace> selectMarketSupportBind();
	
	List<Marketplace> selectGroupByRegion();
	
	List<Marketplace> findByShopid(@Param("shopid")String shopid);

	List<Map<String, Object>> findEUPriorityByGroup(String groupid);

	List<Map<String, Object>> findCommMarketpaceByShopid(String shopid);


	List<Marketplace> getMarketPointBySkuGroup(@Param("sku")String sku,@Param("groupid") String groupid);
	List<Marketplace> getMarketPointByMSku(@Param("sku")String sku,@Param("groupid") String groupid,@Param("shopid") String shopid);
}