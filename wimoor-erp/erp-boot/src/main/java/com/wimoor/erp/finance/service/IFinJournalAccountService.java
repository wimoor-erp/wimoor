package com.wimoor.erp.finance.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;

public interface IFinJournalAccountService extends IService<FinJournalAccount> {

	public IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> param);
	
	public Map<String, Object> findSumByCondition(Map<String, Object> param);
	
	public List<Map<String, Object>> findDetialByCondition(Map<String, Object> param);

	List<Map<String, Object>> paymentSumByCondition(Map<String, Object> param);

	List<Map<String, Object>> paymentSum(String shopid);
	
	public boolean saveRecord(FinJournalAccount faccount, FinJournalAccount old, UserInfo currUser) throws Exception;

	public boolean saveData(FinJournalAccount faccount, UserInfo currUser);

	void setExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	
	public List<Map<String, Object>> findAllAcountByType(UserInfo user,String acc, String year, String month);
	
	public Map<Integer, Map<String, Object>> findLineDataByYear(UserInfo user,String acc, String year, String month);

	public void cancelData(String id, UserInfo userinfo);

}
