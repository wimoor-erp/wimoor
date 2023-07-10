package com.wimoor.amazon.common.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.common.pojo.entity.UserSalesRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
@Mapper
public interface UserSalesRankMapper extends BaseMapper<UserSalesRank> {
	List<Map<String,Object>> summaryAllUserSales(Map<String,Object> map);
	IPage<Map<String,Object>> findRankByShop(Page<?> page,@Param("shopid")String shopid,@Param("daytype")String daytype);
	void deleteAll(String shopid);
	Date summaryLastDate();
}