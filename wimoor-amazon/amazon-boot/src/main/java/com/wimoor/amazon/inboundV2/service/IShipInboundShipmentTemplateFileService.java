package com.wimoor.amazon.inboundV2.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentTemplateFile;
import com.wimoor.common.user.UserInfo;


public interface IShipInboundShipmentTemplateFileService extends IService<ShipInboundShipmentTemplateFile>{

	List<Map<String, Object>> getShipmentCustomsRecord(String shopid);
	
	public Map<String,Object> uploadShipmentcustomsFile(UserInfo user, InputStream inputStream, String filename);

	public Map<String, Object> deleteCustomsFile(String uploadid);
}
