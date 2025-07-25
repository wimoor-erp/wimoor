package com.wimoor.amazon.auth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
 
@Mapper
public interface AmazonGroupMapper extends BaseMapper<AmazonGroup> {

	List<AmazonGroup> findShopNameByCompany(String shopid);
	
	List<AmazonGroup> findAdvShopNameByCompany(String shopid);
	
	List<AmazonGroup> selectUsedByShop(String shopid);

	List<AmazonGroup> selectUsedByShopSKU(@Param("shopid") String shopid, @Param("sku") String sku);

	int insertInitGroup(@Param("shopid") String shopid);

	int updateInitAuthGroupId(@Param("shopid") String shopid);

	int getShopCountByShopId(String shopid);
	
	List<Map<String, Object>> findAdvGroup(@Param("groupid") String groupid, @Param("shopid") String shopid);
	
	List<Map<String, Object>> findAmazonGroup(@Param("groupid") String groupid, @Param("shopid") String shopid);
	
	List<AmazonGroup> getUserGroupList(@Param("userid")String userid);
	
}