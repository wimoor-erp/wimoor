package com.wimoor.erp.stock.service.impl;

import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaBox;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaCase;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaForm;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class BoxFileExcel {
    public static Workbook getFile(ErpDispatchOverseaForm shipment, List<Map<String,Object>> itemlist, List<ErpDispatchOverseaBox> boxlist) {
        try {
            InputStream is = new ClassPathResource("template/CartonConfigurationTemplate.xlsx").getInputStream();
            Workbook workbook =  WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Integer skuunit=0;
            for(Map<String,Object>  item:itemlist) {
                skuunit=skuunit+Integer.parseInt(item.get("amount").toString());
            }
            boolean expendrow=false;
            for (int i=0;i<=sheet.getLastRowNum();i++){
                Row sheetRow = sheet.getRow(i);
                //遍历列cell
                if(i==1) {
                    org.apache.poi.ss.usermodel.Cell cell = sheetRow.getCell(3);
                    cell.setCellValue(shipment.getNumber());
                }else if(i==2) {
                    org.apache.poi.ss.usermodel.Cell cell = sheetRow.getCell(0);
                    cell.setCellValue(" Total Goods: "+itemlist.size()+", ("+skuunit+" units)");
                    org.apache.poi.ss.usermodel.Cell cell1 = sheetRow.getCell(10);
                    cell1.setCellValue(boxlist.size());
                }else if(i==4) {
                    for(int j=0;j<boxlist.size();j++) {
                        Cell cell = sheetRow.createCell(10+j);
                        cell.setCellValue("C000"+boxlist.get(j).getBoxnum());
                    }
                }else if(i==5&&itemlist.size()-1>0&&expendrow==false) {
                    sheet.shiftRows(i, sheet.getLastRowNum(), itemlist.size()-1,true,false);
                    i--;expendrow=true;continue;
                }else if(i>4&&i<=(4+itemlist.size())){
                    if(sheetRow==null) {
                        sheetRow=sheet.createRow(i);
                    }
                     Map<String,Object> skuinfo = itemlist.get(i-5);
                    Cell cell = sheetRow.createCell(0);
                    cell.setCellValue("");
                    cell = sheetRow.createCell(1);
                    cell.setCellValue(skuinfo.get("name")!=null?skuinfo.get("name").toString():"");
                    cell = sheetRow.createCell(6);
                    cell.setCellValue(skuinfo.get("sku")!=null?skuinfo.get("sku").toString():"");
                    cell = sheetRow.createCell(7);
                    cell.setCellValue(skuinfo.get("upc")!=null?skuinfo.get("upc").toString():"");
                    cell = sheetRow.createCell(8);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(skuinfo.get("amount")!=null?Integer.parseInt(skuinfo.get("amount").toString()):0);
                    cell = sheetRow.createCell(9);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(skuinfo.get("amount")!=null?Integer.parseInt(skuinfo.get("amount").toString()):0);
                    for(int j=0;j<boxlist.size();j++) {
                        cell = sheetRow.createCell(10+j);
                        boolean hasvalue=false;
                        ErpDispatchOverseaBox box = boxlist.get(j);
                        List<ErpDispatchOverseaCase> boxCase = box.getCaseListDetail();
                        for(int z=0;z<boxCase.size();z++) {
                            if(boxCase.get(z).getSku().equals(skuinfo.get("sku").toString()) && box.getId().equals(boxCase.get(z).getBoxid())) {
                                    if(boxCase.get(z).getQuantity()==null||boxCase.get(z).getQuantity()==0) {
                                        cell.setCellType(CellType.NUMERIC);
                                        cell.setCellValue(0);hasvalue=true;break;
                                    }else {
                                        cell.setCellType(CellType.NUMERIC);
                                        cell.setCellValue(boxCase.get(z).getQuantity());hasvalue=true;break;
                                    }

                            }
                        }
                        if(hasvalue==false) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(0);
                        }
                    }
                }else  if(i==(3+itemlist.size()+3)){
                    for(int j=0;j<boxlist.size();j++) {
                        Cell cell = sheetRow.createCell(10+j);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(boxlist.get(j).getWeight().doubleValue()*1000);
                    }
                }else   if(i==(3+itemlist.size()+4)){
                    for(int j=0;j<boxlist.size();j++) {
                        Cell cell = sheetRow.createCell(10+j);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(Double.valueOf(boxlist.get(j).getLength().doubleValue()));
                    }
                }else   if(i==(3+itemlist.size()+5)){
                    for(int j=0;j<boxlist.size();j++) {
                        Cell cell = sheetRow.createCell(10+j);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(Double.valueOf(boxlist.get(j).getWidth().doubleValue()));
                    }
                }else   if(i==(3+itemlist.size()+6)){
                    for(int j=0;j<boxlist.size();j++) {
                        Cell cell = sheetRow.createCell(10+j);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(  Double.valueOf(boxlist.get(j).getHeight().doubleValue()));
                    }
                }

            }
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
