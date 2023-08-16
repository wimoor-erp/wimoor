package com.wimoor.amazon.report.service;

import com.wimoor.amazon.report.pojo.vo.PerformanceVo;

public interface IReportAmzPerformanceService {
	   public PerformanceVo getPerformance(String shopid,String groupid);
}
