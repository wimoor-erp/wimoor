package com.wimoor.erp.common.service;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.common.pojo.entity.SummaryData;

public interface ISummaryDataService extends IService<SummaryData> {
	public void refreshProNum() ;
	public void refreshProRate();
	List<SummaryData> findMainReport(String shopid,String ftype);
	public void summary(Set<String> shopset);
}
