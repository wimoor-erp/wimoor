package com.wimoor.amazon.adv.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvProfileMapper extends BaseMapper<AmzAdvProfile>{
	String getUUID();
	List<Map<String,Object>> getSelleridBygroup(@Param("groupid")String groupid);
	
	List<Map<String,Object>> getProfileForSellerid(@Param("sellerid")String sellerid, @Param("region")String region);
	
	List<Map<String,Object>> getAdvDateStatus(@Param("sellerid")String sellerid, @Param("status")String status);
	
	List<Map<String, Object>> getProfileBySellerid(@Param("sellerid")String sellerid);
	
	List<Map<String, Object>> findBkp1(@Param("profileid")String profileid, @Param("dates")String dates);
	
	List<Map<String, Object>> findBkp2(@Param("profileid")String profileid, @Param("dates")String dates);
	
	List<Map<String, Object>> findBkp3(@Param("profileid")String profileid, @Param("dates")String dates);
	
	List<Map<String, Object>> findBkp4(@Param("profileid")String profileid, @Param("dates")String dates);
	
	List<Map<String, Object>> findBkp5(@Param("profileid")String profileid, @Param("dates")String dates);
	
	List<Map<String, Object>> findBkp6(@Param("profileid")String profileid, @Param("dates")String dates);
	
	List<Map<String, Object>> findBkp7(@Param("profileid")String profileid, @Param("dates")String dates);
	
}