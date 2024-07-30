package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.dto.ProductInAftersaleDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;

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
 * @since 2022-11-28
 */
@Mapper
public interface ProductInAftersaleMapper extends BaseMapper<ProductInAftersale> {

	void insertBatch(List<ProductInAftersale> salesafter);
	
	List<Map<String,Object>> getSummary(@Param("shopid")String shopid,@Param("groupid")String groupid);

	IPage<Map<String, Object>> findList(Page<?> page, @Param("param")ProductInAftersaleDTO dto);
}
