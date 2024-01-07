package com.wimoor.amazon.adv.common.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.Marketplace;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvAuthMapper extends BaseMapper<AmzAdvAuth>{
	
	List<Map<String, Object>> findViewList(String shopid);

	List<Map<String, Object>> selectProfileByGroup(String groupid);
	
	Map<String, Object> getProfileByGroupAndmarkplace(@Param("groupid")String groupid, @Param("marketplaceid")String marketplaceid);

	List<Marketplace> getRegionByAdvgroup(@Param("shopid")String shopid, @Param("advgroupid")String advgroupid);

	List<AmzAdvProfile> getProfileIdByGroup(@Param("shopid")String shopid, @Param("advgroupid")String advgroupid);
	
	Integer findBindAdvCount(@Param("shopid")String shopid);
														
	List<Map<String, Object>> findAmzAdvAuthWithDisable();
	
	void deleteAdventDateForProfileId(@Param("profileid")BigInteger profileid);
	
	void deleteAdventDateForSellerId(@Param("sellerid")String sellerid);
	
	int updateAdvAuthDisable(Map<String, Object> map);
	
	List<Map<String, Object>> findgroupBySellerId();

	List<AmzAdvAuth> selectNotDisableList();

}