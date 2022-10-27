package com.wimoor.amazon.report.mapper;

import com.wimoor.amazon.report.pojo.entity.AmzInventoryCountryReport;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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

}
