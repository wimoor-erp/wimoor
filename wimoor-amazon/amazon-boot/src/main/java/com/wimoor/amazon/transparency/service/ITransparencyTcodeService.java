package com.wimoor.amazon.transparency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTcode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_tcode】的数据库操作Service
* @createDate 2025-08-08 11:31:32
*/
public interface ITransparencyTcodeService extends IService<TransparencyTcode> {
    IPage<Map<String, Object>> listTCode(UserInfo userinfo, TransparencyDTO dto);

    TransparencyTcode saveTCode(UserInfo userinfo, TransparencyTcode dto);

    void setExcelBook(SXSSFWorkbook workbook, TransparencyDTO dto, UserInfo userinfo);

    void setPdfBook(Document document, PdfWriter writer, TransparencyDTO dto, UserInfo userinfo);
}
