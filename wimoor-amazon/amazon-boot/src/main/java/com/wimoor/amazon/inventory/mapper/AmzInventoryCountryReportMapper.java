package com.wimoor.amazon.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryCountryReport;

/**
 * <p>
 * 用于存储欧洲各个国家的库存 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-08
 */
@Mapper
public interface AmzInventoryCountryReportMapper extends BaseMapper<AmzInventoryCountryReport> {
	
	IPage<Map<String,Object>> findFBACountry(Page<?> page,@Param("param")Map<String,Object> param);

	void insertBatch(List<AmzInventoryCountryReport> list);
}
