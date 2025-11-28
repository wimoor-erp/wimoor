package com.wimoor.amazon.performance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.performance.pojo.dto.PerformanceReportListDTO;
import com.wimoor.amazon.performance.pojo.entity.AmzOrderFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface AmzOrderFeedbackMapper extends BaseMapper<AmzOrderFeedback> {
    IPage<Map<String, Object>> findByCondition(Page<Object> page, @Param("param") PerformanceReportListDTO dto);
}
