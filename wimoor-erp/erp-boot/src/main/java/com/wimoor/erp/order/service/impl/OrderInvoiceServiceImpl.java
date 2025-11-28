package com.wimoor.erp.order.service.impl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.erp.order.mapper.OrderInvoiceMapper;
import com.wimoor.erp.order.pojo.entity.OrderInvoice;
import com.wimoor.erp.order.service.IOrderInvoiceService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrderInvoiceServiceImpl extends ServiceImpl<OrderInvoiceMapper, OrderInvoice> implements IOrderInvoiceService {
    @Autowired
    FileUpload fileUpload;
    public void treatShipmentTemplate(Workbook workbook, Map<String,Object> param, List<Map<String,Object>> list)  {
        for(int sheetindex=0;sheetindex<workbook.getNumberOfSheets();sheetindex++) {
            int pRowIndex = 0;
            Sheet sheet = workbook.getSheetAt(sheetindex);
            //第一步：列表填充，将列表行模板复制
            boolean hasPlistRow = false;
            Row oldsheetRow=null;
            for (int i=0;i<=sheet.getLastRowNum();i++){
                    Row sheetRow = sheet.getRow(i);
                    boolean mhasPlistRow = false;
                    if (sheetRow != null) {
                        for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
                            Cell cell = sheetRow.getCell(cellnum);
                            if(cell==null)continue;
                            String cellValue =null;
                            try{
                                cellValue = cell.getStringCellValue().trim();
                            }catch(Exception e){
                                cellValue= String.valueOf(cell.getNumericCellValue());
                            }
                            if(cellValue.contains("列表")){
                                mhasPlistRow=true;
                                break;
                            }
                        }
                    }

                    if(mhasPlistRow==true) {
                        hasPlistRow=true;oldsheetRow=sheetRow;continue;
                    }else if(hasPlistRow==true) {
                        if(i<sheet.getLastRowNum()) {
                            if(list.size()>1) {
                                sheet.shiftRows(i, sheet.getLastRowNum(), list.size()-1,true,false);
                            }
                        }
                        for(int rowaddnum=0;rowaddnum<list.size()-1;rowaddnum++) {
                            Row row = sheet.createRow(i+rowaddnum);
                            row.setRowStyle(oldsheetRow.getRowStyle());
                            for (int cellnum = 0; cellnum <= oldsheetRow.getLastCellNum(); cellnum++) {
                                Cell oldcell = oldsheetRow.getCell(cellnum);
                                Cell mycell = row.createCell(cellnum);
                                if(oldcell!=null) {
                                    try{
                                        mycell.setCellValue(oldcell.getRichStringCellValue());
                                        mycell.setCellStyle(oldcell.getCellStyle());
                                    }catch(Exception e){
                                        try{
                                            mycell.setCellValue(oldcell.getNumericCellValue());
                                            mycell.setCellStyle(oldcell.getCellStyle());
                                        }catch(Exception e1){
                                            mycell.setCellValue("");
                                            e1.printStackTrace();
                                        }
                                    }
                                } else {
                                    mycell.setCellValue("");
                                }
                            }
                        }
                        i=i+list.size()-1;
                        hasPlistRow=false;
                    }
                }
            //第二步：字段填充
            for (int i=0;i<=sheet.getLastRowNum();i++){
                hasPlistRow = false;
                Row sheetRow = sheet.getRow(i);
                if (sheetRow == null) {
                    continue;
                }
                for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
                    Cell cell = sheetRow.getCell(cellnum);
                    if (cell == null) {
                        continue;
                    }
                    String cellValue =null;
                    try{
                        cellValue = cell.getStringCellValue().trim();
                    }catch(Exception e){
                        cellValue= String.valueOf(cell.getNumericCellValue());
                    }
                    if(StrUtil.isNotEmpty(cellValue)) {
                        cell.setCellType(CellType.STRING);
                        if(param.get(cellValue)!=null) {
                            setCellValue(workbook,sheet,cell,param,cellValue);
                        }else {
                            if(cellValue.contains("列表")){
                                Map<String, Object> pmap = list.get(pRowIndex);
                                if(setCellValue(workbook,sheet,cell,pmap,cellValue)) {
                                    hasPlistRow=true;
                                }
                            } else if(cellValue.contains("{")) {
                                setCellValue(workbook,sheet,cell,param,cellValue);
                            }
                        }
                    }
                }
                if(hasPlistRow) {
                    if(pRowIndex+1<list.size()) {
                        pRowIndex++;
                    }else {
                        pRowIndex=0;
                    }
                }

            }
        }
    }
    boolean setCellValue(Workbook workbook,Sheet sheet,Cell cell,Map<String, Object> map,String cellValue){
        Object value =map.get(cellValue);
        if(value!=null) {
            if(cellValue.contains("图片")) {
                Row imgrow = cell.getRow();
                float nums=50;
                imgrow.setHeightInPoints(nums);
                String image =value!=null?fileUpload.getPictureImage(value):"";
                Drawing<?> draw = sheet.createDrawingPatriarch();
                ClientAnchor anchor =null;
                if(cellValue.contains("是否已付费")){
                      anchor = draw.createAnchor(300000, 30000, 300000, 300000, cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex()+4, cell.getRowIndex()+3);
                }else{
                      anchor = draw.createAnchor(0, 0, 0, 0, cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex()+3, cell.getRowIndex()+2);
                }
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                BufferedImage bufferImg;
                try {
                    if(value.toString().contains(".jpg")||value.toString().contains(".jpeg")){
                        bufferImg = ImageIO.read(new URL(image));
                        ImageIO.write(bufferImg, "JPEG", byteArrayOut);
                    }else if(value.toString().contains(".png")){
                        bufferImg = ImageIO.read(new URL(image));
                        ImageIO.write(bufferImg, "PNG", byteArrayOut);
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                org.apache.poi.ss.usermodel.Picture picexcel = draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG));
                cell.setCellValue("");
            }else {
                cell.setCellValue(value.toString());
            }
            return true;
        }else {
            List<String> cellvalueArr = splitValue(cellValue);
            boolean hasdata=false;
            for(int k=0;k<cellvalueArr.size();k++) {
                String paramkey = cellvalueArr.get(k);
                if(StrUtil.isNotEmpty(paramkey)) {
                    if(map.get(paramkey)!=null) {
                        String paramvalue = map.get(paramkey).toString();
                        cellValue=cellValue.replace( paramkey , paramvalue);
                        cell.setCellValue(cellValue);
                        hasdata=true;
                    }else {
                        String paramvalue = "";
                        cellValue=cellValue.replace( paramkey , paramvalue);
                        cell.setCellValue(cellValue);
                    }
                }
            }
            return hasdata;
        }

    }
    private List<String> splitValue(String value) {
        List<String> ls=new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(value);
        while(matcher.find()) {
            ls.add(matcher.group());
        }
        return ls;
    }
}
