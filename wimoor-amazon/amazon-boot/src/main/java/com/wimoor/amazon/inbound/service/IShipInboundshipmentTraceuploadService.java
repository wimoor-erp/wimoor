package com.wimoor.amazon.inbound.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundshipmentTraceupload;
import com.wimoor.common.user.UserInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */
public interface IShipInboundshipmentTraceuploadService extends IService<ShipInboundshipmentTraceupload> {

	List<ShipInboundshipmentTraceupload> listByAuth();

	void getTempExcelReport(SXSSFWorkbook workbook);

	void uploadTraceuploadFile(UserInfo user, Row info);

	void saveStatus(UserInfo user, String shipmentid);

	int saveItem(UserInfo user, String shipmentid);

}
