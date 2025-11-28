package com.wimoor.quote.ship.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.user.UserInfo;
import com.wimoor.quote.ship.pojo.dto.ShipmentSupplierTranschannelDTO;
import com.wimoor.quote.ship.pojo.entity.ShipmentSupplierTranschannel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.quote.ship.pojo.entity.UserBuyer;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_shipment_supplier_transchannel】的数据库操作Service
* @createDate 2025-07-22 18:06:30
*/
public interface IShipmentSupplierTranschannelService extends IService<ShipmentSupplierTranschannel> {

    IPage<Map<String,Object>> listPage(ShipmentSupplierTranschannelDTO dto);

    String uploadExcel(Sheet sheet,  UserBuyer buyer);

    void getTempExcelReport(SXSSFWorkbook workbook);
}
