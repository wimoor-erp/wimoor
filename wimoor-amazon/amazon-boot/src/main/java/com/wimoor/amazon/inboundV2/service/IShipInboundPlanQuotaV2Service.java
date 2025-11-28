package com.wimoor.amazon.inboundV2.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipLabelDto;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.dto.QuotaInfoDTO;

public interface IShipInboundPlanQuotaV2Service {

	void setPDFDocShipFormDetail(PdfWriter writer, UserInfo user, Document document, QuotaInfoDTO quotaInfoDto,List<ShipPlanVo> volist);

	void setPDFDocShipForm(PdfWriter writer, UserInfo user, Document document, QuotaInfoDTO quotaInfoDto, List<ShipPlanVo> volist);

	void setPDFDocLabel(Document document, List<Map<String,Object>> skulist, PdfWriter writer);
	void setExcelBookShipmentLabel(String shopid, SXSSFWorkbook workbook,List<Map<String,Object>> paralist) ;
	public List<Map<String, Object>> getLabelValue(List<ShipLabelDto> dto) ;
}
