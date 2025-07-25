package com.wimoor.amazon.report.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.report.pojo.entity.AmazonEprMonthlyReport;
import com.wimoor.amazon.report.pojo.entity.AmazonProductSales;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzEprMonthlyReportMapper extends BaseMapper<AmazonEprMonthlyReport> {
	 
}