package com.wimoor.erp.common.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ImportRecord;

 

public interface IImportRecordService extends IService<ImportRecord>{

	List<ImportRecord> getImportRecord(String shopid, String importtype);

	List<Map<String, Object>> getShipmentCustomsRecord(String shopid);
	
	public Map<String,Object> uploadShipmentcustomsFile(UserInfo user, InputStream inputStream, String filename);

	public Map<String, Object> deleteCustomsFile(String uploadid);
}
