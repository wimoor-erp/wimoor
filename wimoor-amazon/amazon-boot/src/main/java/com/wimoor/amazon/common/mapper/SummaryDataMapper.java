package com.wimoor.amazon.common.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.common.pojo.entity.SummaryData;
@Mapper
public interface SummaryDataMapper extends BaseMapper<SummaryData> {
	Integer findProNum(@Param("shopid")String shopid);
	Integer returnOrders(@Param("shopid")String shopid,@Param("oneday")Date oneday);

	List<SummaryData> findMainReport(@Param("shopid")String shopid,@Param("ftype")String ftype);
	Map<String,Object> monthSalesOrders(@Param("shopid")String shopid,@Param("oneday")Date oneday);
	void refreshProRate();
	Integer deleteAllMainRpt(String shopid);
	void refreshProNum();	
}