package com.wimoor.erp.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaBox;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface ErpDispatchOverseaBoxMapper extends BaseMapper<ErpDispatchOverseaBox>{

	List<Map<String, Object>> findShipInboundBox(String shipmentid);
	List<Map<String, Object>> findAllBox(String id);
}