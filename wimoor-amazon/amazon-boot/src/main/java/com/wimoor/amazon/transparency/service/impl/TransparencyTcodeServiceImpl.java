package com.wimoor.amazon.transparency.service.impl;

import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.transparency.mapper.TransparencyTcodeMapper;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTcode;
import com.wimoor.amazon.transparency.service.ITransparencyTcodeService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_tcode】的数据库操作Service实现
* @createDate 2025-08-08 11:31:32
*/
@Service
public class TransparencyTcodeServiceImpl extends ServiceImpl<TransparencyTcodeMapper, TransparencyTcode>
    implements ITransparencyTcodeService {
    @Autowired
    IProductInfoService iProductInfoService;
    @Resource
    QrConfig qrconfig;
    @Override
    public IPage<Map<String, Object>> listTCode(UserInfo userinfo, TransparencyDTO dto) {
        dto.setShopid(userinfo.getCompanyid());
        IPage<Map<String, Object>> page = this.baseMapper.listTcode(dto.getPage(), dto);
        if(page!=null&&page.getRecords()!=null){
            for(Map<String, Object> map : page.getRecords()){
                String sku=map.get("sku").toString();
                String groupid=map.get("groupid").toString();
                Map<String, Object> item = iProductInfoService.findNameAndPicture(sku, null, groupid);
                if(item!=null){
                    map.putAll(item);
                }
            }
        }
        return page;
    }

    @Override
    public TransparencyTcode saveTCode(UserInfo userinfo, TransparencyTcode dto) {
        dto.setOperator(userinfo.getId());
        LambdaQueryWrapper<TransparencyTcode> query=new LambdaQueryWrapper<TransparencyTcode>();
        query.eq(TransparencyTcode::getTaskid,dto.getTaskid());
        query.eq(TransparencyTcode::getTcode,dto.getTcode());
        TransparencyTcode one = this.baseMapper.selectOne(query);
        if(one!=null){
            one.setUsetime(dto.getUsetime());
            this.baseMapper.update(one,query);
        }else{
            this.baseMapper.insert(dto);
        }
        return one;
    }

    @Override
    public void setExcelBook(SXSSFWorkbook workbook, TransparencyDTO dto, UserInfo userinfo) {
        dto.setShopid(userinfo.getCompanyid());
        List<Map<String, Object>> list = this.baseMapper.listTcode(dto);
        SXSSFSheet sheet = workbook.createSheet();
        int rowindex=0;
        SXSSFRow titleRow = sheet.createRow(rowindex++);
        SXSSFCell titlecell = titleRow.createCell(0);
        titlecell.setCellValue("透明计划账号");
        titlecell = titleRow.createCell(1);
        titlecell.setCellValue("ASIN");
        titlecell = titleRow.createCell(2);
        titlecell.setCellValue("SKU");
        titlecell = titleRow.createCell(3);
        titlecell.setCellValue("T-Code");
        for(Map<String, Object> map : list){
            SXSSFRow row = sheet.createRow(rowindex++);
            SXSSFCell cell = row.createCell(0);
            cell.setCellValue(map.get("client_name").toString());
            cell = row.createCell(1);
            cell.setCellValue(map.get("asin").toString());
            cell = row.createCell(2);
            cell.setCellValue(map.get("sku").toString());
            cell = row.createCell(3);
            cell.setCellValue(map.get("tcode").toString());
        }
    }

    @Override
    public void setPdfBook(Document document, PdfWriter writer, TransparencyDTO dto, UserInfo userinfo) {
        dto.setShopid(userinfo.getCompanyid());
        List<Map<String, Object>> list = this.baseMapper.listTcode(dto);
        if(dto.getFtype().equals("H1")){
            setPdfBookH1(document,writer, list);
        }

    }
    private Image getImage(String imgpath) {
        if(imgpath==null)return null;
        Image img = null;
        try {
            ClassPathResource classPathRes = new ClassPathResource("template/" + imgpath);
            img = Image.getInstance(classPathRes.getURL());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(img!=null) {
            img.setScaleToFitHeight(true);
        }
        return img;
    }

    private void setPdfBookH1(Document document, PdfWriter writer, List<Map<String, Object>> list) {
        document.addTitle("产品标签");
        document.addAuthor("wimoor");
        BaseFont baseFont = null;
        try {
            document.open();
            String path = new ClassPathResource("font/ARIALN.TTF").getPath();
            baseFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            // 获取页面尺寸，用于确定文本放置位置
            Rectangle pageSize = document.getPageSize();

            for (int i = 0; i < list.size(); i++) {
                document.newPage();
                PdfContentByte content = writer.getDirectContent();
                Map<String, Object> map = list.get(i);
                String gtin = map.get("gtin").toString().trim();
                String tcode = map.get("tcode").toString().trim();
                String sku=map.get("sku").toString().trim();
                String gs1Data =tcode;


                Image img = getImage("ih.png");
                img.scaleAbsolute(18,18);
                img.setAbsolutePosition(8, 58);
                document.add(img);

                // 绘制文本 - 调整位置和大小
                content.beginText();
                // 增加字体大小到更易读的8pt
                content.setFontAndSize(baseFont, 5);
                // 将文本放在页面顶部附近，确保在可见区域
                // 页面坐标原点在左下角，所以Y值应接近页面高度
                content.setTextMatrix(33, pageSize.getHeight() - 12);

                // 处理换行：iText中需要手动计算换行位置
                String text = "Scan with the ";
                content.showText(text);

                // 如果需要换行，需要重新设置文本矩阵
                 content.setTextMatrix(33, pageSize.getHeight() - 17);
                 content.showText("Transparency app");

                content.endText();

                try {
                    // 二维码生成代码保持不变...
                    int width = 50;
                    int height = 50;
                    Map<EncodeHintType, Object> hints = new HashMap<>();
                    hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
                    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    hints.put(EncodeHintType.MARGIN, 1);
                    hints.put(EncodeHintType.GS1_FORMAT, true);

                    BitMatrix bitMatrix = new MultiFormatWriter().encode(
                            gs1Data,
                            BarcodeFormat.DATA_MATRIX,
                            width,
                            height,
                            hints
                    );
                    MatrixToImageConfig config = new MatrixToImageConfig();
                    BufferedImage imagebuffer = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ImageIO.write(imagebuffer, "jpg", out);
                    img = Image.getInstance(out.toByteArray());
                    img.setScaleToFitHeight(true);
                    img.setAbsolutePosition(12, 5);
                    document.add(img);



                    content.beginText();
                    content.setFontAndSize(baseFont, 6);  // 增大字体
//                    content.setTextMatrix(0, 1, -1, 0, 65, 5);
                   // content.setTextMatrix(30, pageSize.getHeight() - 18);  // 调整位置
                    // 处理换行
                    List<String>lines=new ArrayList<String>();
                    if(sku!=null &&  sku.length()>15){
                        lines.add(sku.substring(0,15));
                        lines.add(sku.substring(15));
                    }else{
                        lines.add(sku);
                    }
                    float yPos = pageSize.getHeight() - 10;
                    int x=67;
                    for (String line : lines) {
                        content.setTextMatrix(0, 1, -1, 0, x, 6);
                        x=x+7;
                        //content.setCharacterSpacing(0.7f);
                       // content.setTextMatrix(40, yPos);
                        content.showText(line);
                        yPos -= 5;  // 行间距
                    }
                    content.endText();
                    content.beginText();
                    content.setFontAndSize(baseFont, 6);  // 增大字体
                    content.setTextMatrix(2, 2);
                    content.showText((i+1)+"");
                    content.endText();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new BizException("PDF创建失败"+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("PDF创建失败"+e.getMessage());
        }
    }
}




