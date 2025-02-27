package com.wimoor.erp.ship.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransDetail;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface ShipTransCompanyMapper extends BaseMapper<ShipTransCompany> {
	IPage<ShipTransCompany> findByCondition(Page<?> page,@Param("shopid") String shopid, @Param("search") String search,@Param("isdelete")String isdelete);

	List<ShipTransDetail> findByCompanyId(@Param("comid") String comid);
	List<ShipTransDetail> findDisabledByCompanyId(@Param("comid") String comid);
	
	List<Map<String, Object>> selectBycom(@Param("company") String comid, @Param("shopid") String shopid,
			@Param("marketplaceid") String marketplaceid, @Param("transtype")String transtype);

	List<Map<String, Object>> selectBychannel(@Param("company") String company, @Param("channel") String channel,
			@Param("marketplaceid") String marketplaceid, @Param("transtype")String transtype);

	List<ShipTransCompany> findBylimit(@Param("shopid") String shopid);

	List<Map<String, Object>> findAllByCondition(@Param("shopid") String shopid, @Param("mydate") String date,@Param("search") String search);

	List<ShipTransDetail> findListItem(Map<String, Object> map);

	List<ShipTransCompany> getCompanyTranstypeList(Map<String, Object> maps);
}