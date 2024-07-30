package com.wimoor.erp.stock.mapper;

import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaFormEntry;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Mapper
public interface ErpDispatchOverseaFormEntryMapper extends BaseMapper<ErpDispatchOverseaFormEntry> {

	List<Map<String, Object>> selectByFormid(@Param("formid")String formid);

	List<Map<String, Object>> findFormDetailByFormid(@Param("formid")String formid, @Param("warehouseid")String warehouseid,
			@Param("warehouseid2")String warehouseid2,@Param("shopid")String shopid);
}
