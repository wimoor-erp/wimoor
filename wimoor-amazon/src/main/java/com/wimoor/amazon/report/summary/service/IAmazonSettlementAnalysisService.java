package com.wimoor.amazon.report.summary.service;

import java.util.Map;
import java.util.Set;

public interface IAmazonSettlementAnalysisService {
    public void confirm(Map<String, Object> param, Set<String> marketnameset);
	public void refreshAll();
 
}
