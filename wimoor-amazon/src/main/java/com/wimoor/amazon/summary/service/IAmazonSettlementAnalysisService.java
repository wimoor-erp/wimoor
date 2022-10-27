package com.wimoor.amazon.summary.service;

import com.wimoor.amazon.report.pojo.entity.AmzSettlementAccReport;

public interface IAmazonSettlementAnalysisService {
    public void confirm(AmzSettlementAccReport item);
	public void refreshAll();
	public void checkSummaryData();
 
}
