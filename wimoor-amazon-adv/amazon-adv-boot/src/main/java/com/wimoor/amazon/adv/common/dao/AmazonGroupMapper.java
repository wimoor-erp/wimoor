package com.wimoor.amazon.adv.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.common.pojo.AmazonGroup;
import com.wimoor.amazon.base.BaseMapper;


@Mapper
public interface AmazonGroupMapper extends BaseMapper<AmazonGroup> {
	List<AmazonGroup> findShopNameByUser(String shopid);
	
	List<AmazonGroup> findAdvShopNameByUser(String shopid);
	
	List<AmazonGroup> selectUsedByShop(String shopid);

	List<AmazonGroup> selectUsedByShopSKU(@Param("shopid") String shopid, @Param("sku") String sku);

//	int insertAmazonGroup(@Param("shopname") String shopname, @Param("shopid") String shopid, @Param("profitcfgid") String profitcfgid);

	int deleteAmazonGroup(@Param("id") String id, @Param("shopid") String shopid);

//	int updateAmazonGroup(@Param("sname") String sname, @Param("id") String id);

	int insertInitGroup(@Param("shopid") String shopid);

	int updateInitAuthGroupId(@Param("shopid") String shopid);

	int getShopCountByShopId(String shopid);
	
	List<Map<String, Object>> findAdvGroup(@Param("groupid") String groupid, @Param("shopid") String shopid);
	
	List<Map<String, Object>> findAmazonGroup(@Param("groupid") String groupid, @Param("shopid") String shopid);
}