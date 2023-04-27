package com.wimoor.amazon.summary.service;

import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;

public interface IAmazonSettlementAnalysisService {
    public void confirm(AmzSettlementAccReport item);
	public void refreshAll();
	public void checkSummaryData();
 
}
