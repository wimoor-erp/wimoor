package com.wimoor.amazon.adv.common.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRemind;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvRemindMapper  extends BaseMapper<AmzAdvRemind>{

	PageList<Map<String, Object>> selectByCondition(Map<String, Object> param, PageBounds pagebounds);
	
	List<Map<String, Object>> selectByprofileid(Map<String, Object> param);
	
	void updateRemindIswarn(Map<String,Object> map);
	
	void updateRemindIsnotwarn(Map<String,Object> map);
	
	List<Map<String,Object>> getCreatorList(String shopid);
	
	void deleteReminForProfileId(@Param("profileid")BigInteger profileid); 
	
	List<Map<String, Object>> selectAllRemind();
}