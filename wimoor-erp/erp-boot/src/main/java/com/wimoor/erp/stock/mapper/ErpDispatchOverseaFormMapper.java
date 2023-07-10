package com.wimoor.erp.stock.mapper;

import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaForm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Mapper
public interface ErpDispatchOverseaFormMapper extends BaseMapper<ErpDispatchOverseaForm> {

	IPage<Map<String, Object>> findByCondition(Page<?>  page,@Param("params")Map<String,Object> map);

	Map<String, Object> findById(@Param("id")String id);
	List<Map<String, Object>> getShipArrivalTimeRecord(
														@Param("shopid") String shopid,
														@Param("country") String country, 
														@Param("sku") String sku, 
														@Param("groupid") String groupid
													  );
}
