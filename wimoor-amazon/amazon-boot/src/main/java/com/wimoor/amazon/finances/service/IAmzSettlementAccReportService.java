package com.wimoor.amazon.finances.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;

public interface IAmzSettlementAccReportService  extends IService<AmzSettlementAccReport>{

	IPage<Map<String, Object>> findSettlementAcc(Page<Object> page, Map<String, Object> map);
	public List<Map<String, Object>> findSettlementAcc(Map<String, Object> map);
	public HashMap<String, HashMap<String, Object>> loadSummayAccMaps(Map<String, Object> param);
	Map<String, Object> findSettlementAccSum(Map<String, Object> map);
 	public AmzSettlementAccReport findSettlementByKey(String settlement_id, String amazonauthid, String marketplace_name);
	List<Map<String, Object>> findDateByAuth(Map<String, Object> map);

}
