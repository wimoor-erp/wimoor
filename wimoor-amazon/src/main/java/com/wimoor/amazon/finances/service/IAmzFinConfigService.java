package com.wimoor.amazon.finances.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.finances.pojo.entity.AmzFinSettlementFormula;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItemData;
import com.wimoor.common.user.UserInfo;


 

public interface IAmzFinConfigService extends IService<AmzFinUserItem>{
	public IPage<Map<String, Object>> findFinListByShopid(Page<?>page,String shopid);

	public void saveFinItemData(List<AmzFinUserItemData> amzFinUserItemDataList) ;

	public IPage<Map<String, Object>> getFinDataList(Page<?>page,UserInfo user, Map<String, Object> params);
	
	public List<Map<String, Object>> getFinDataLastWeek(String ekey,UserInfo user, Map<String, Object> params);

	public List<Map<String, Object>> findFinListByShopid(String shopid);
	
	public Map<String,String> findFinMapByShopid(String shopid) ;
	
	public AmzFinSettlementFormula getAmzFinSettlementFormula(String shopid);
	
	public Map<String, Object> getAmzFinSettlementFormulaConvert(String shopid);
	
	public String saveformulaData(UserInfo user, String formuladata, String pricetype);

	public Map<String, String> getformulaTitle(String shopid);

	public List<Map<String, Object>> findFinListByShopidNoPage(String shopid);

	public void setExcelBook(SXSSFWorkbook workbook, List<Map<String, Object>> list);
	
	public void setExcelBookByOtherFee(SXSSFWorkbook workbook, List<Map<String, Object>> list, String ftype);
	
	public List<Map<String, Object>> getFinDataForSku(Map<String, Object> params);
	
	public int saveFinItemData(Map<String, Object> params);
	
	public int deleteFinItemData(String id);
	
	public List<Map<String, Object>> getFinDataList(UserInfo user, Map<String, Object> params);
	public void setExcelBookOverall(SXSSFWorkbook workbook, List<Map<String, Object>> list) ;
}
