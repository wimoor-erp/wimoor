package com.wimoor.erp.common.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;

public interface IExcelDownLoadService {
	
	void uploadMaterialFile(UserInfo user, InputStream inputStream, Row info);
	
	void uploadCustomerFile(UserInfo user, Row info);
	
	void uploadCategoryFile(UserInfo user, InputStream inputStream, Row info);
	
	public SXSSFWorkbook downloadExcel(List<Map<String,Object>> list);
	
	SXSSFWorkbook downloaderrorExcel(List<String> list);

	void uploadMskuFile(UserInfo user, InputStream inputStream, Row info,String marketplaceid,String groupid);

	void uploadOtherfeeFile(UserInfo user, InputStream inputStream, Row info, String marketplaceid, String groupid,String itemid);

	public void uploadFinItemDataFile(UserInfo user, InputStream inputStream, Row info, String marketplaceid, String groupid) ;

	void getExcelStockAllInfoReport(SXSSFWorkbook workbook, Map<String, Object> map);

	void uploadStockTakingFile(UserInfo user, Workbook workbook, String stockid);

	void uploadShipmentTransFile(UserInfo user, InputStream inputStream, String companyid,String fileName);

	void uploadMaterialcustomsFile(UserInfo user, InputStream inputStream, String stockid, String fileName);

	void uploadShipmentcustomsFile(UserInfo user, InputStream inputStream, String fileName);

	void uploadMaterialBaseInfoFile(UserInfo user, Row info,String mtype, Map<String, String> ownermap);

	void uploadMaterialSupplierFile(UserInfo user, Row info);

	MaterialConsumableVO uploadMaterialConsumableFile(UserInfo user, Row info);

	void uploadMaterialCustomsFile(UserInfo user, Row info);

	void uploadMaterialAssemblyFile(UserInfo user, Row info);
}
