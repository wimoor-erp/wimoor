package com.wimoor.amazon.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.performance.pojo.dto.PerformanceReportListDTO;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsPerformanceReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface AmzPromotionsPerformanceReportMapper extends BaseMapper<AmzPromotionsPerformanceReport> {
    IPage<Map<String, Object>> findByCondition(Page<Object> page,@Param("param") PerformanceReportListDTO dto);
}
