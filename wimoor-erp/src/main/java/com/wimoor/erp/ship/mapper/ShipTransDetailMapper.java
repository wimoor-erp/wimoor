package com.wimoor.erp.ship.mapper;

 
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.ship.pojo.entity.ShipTransDetail;
@Mapper
public interface ShipTransDetailMapper extends BaseMapper<ShipTransDetail> {
    @Select("<script>" +
            "select count(0) from t_erp_ship_transdetail t "+
    		"left join t_erp_ship_transcompany u on u.id=t.company "+
            "where u.shopid=#{shopid} and  t.transtype=#{transtype} "+
            "</script>")
	int usedTransType(String shopid, String transtype);
  
}