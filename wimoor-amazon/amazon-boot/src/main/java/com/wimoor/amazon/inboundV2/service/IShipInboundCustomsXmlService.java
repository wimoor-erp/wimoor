package com.wimoor.amazon.inboundV2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCustomXmlDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXml;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
/**
* @author liufei
* @description 针对表【t_erp_ship_v2_inboundshipment_customs_xml】的数据库操作Service
* @createDate 2025-12-03 13:35:14
*/
public interface IShipInboundCustomsXmlService extends IService<ShipInboundCustomsXml> {

    String getNextSequenceNumber(ShipInboundCustomsXml xmlInfo);

    IPage<Map<String, Object>> findList(ShipCustomXmlDTO dto);

    Map<String, Object> summaryList(ShipCustomXmlDTO dto);

    void setExcelBook(SXSSFWorkbook workbook, ShipCustomXmlDTO dto);

    void setCustomsExcelBook(Sheet sheet, ShipCustomXmlDTO dto);

    Map<String, Object> importReturnStatus(UserInfo user, MultipartFile[] files);
}
