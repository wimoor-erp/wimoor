package com.wimoor.erp.assembly.mapper;

import java.util.List;
import java.util.Map;

import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.assembly.pojo.entity.AssemblyFormEntry;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssemblyFormEntryMapper extends BaseMapper<AssemblyFormEntry> {
	List<Map<String, Object>> selectByFormid(String formid);

	List<AssemblyFormEntry> selectEntityByFormid(String formid);

	List<Map<String, Object>> selectAssemblyFormByPurchaseEntryId(String entryid);
	Integer selectNeedProcess(@Param("materialid")String materialid,@Param("warehouseid") String warehouseid,@Param("shopid") String shopid);
	List<ShipInboundItemVo> findItemListByFormids(@Param("list")List<String> list);
}
