package com.wimoor.amazon.feed.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;

import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundItemVo;

import cn.hutool.core.util.StrUtil;

public class FeedFileInboundFBAExcel {
      public static final String type="POST_FLAT_FILE_FROM_EXCEL_FBA_CREATE_CARTON_INFO";
	  public static ByteArrayOutputStream getFile(ShipInboundPlan plan,ShipInboundShipment shipment,List<ShipInboundItemVo> itemlist,List<ShipInboundBox> boxlist,List<ShipInboundCase> boxcase , String sellerid) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			 InputStream is = new ClassPathResource("template/shipmentid.xlsx").getInputStream();
			 Workbook workbook =  WorkbookFactory.create(is);
			 Sheet sheet = workbook.getSheetAt(0);
			 Integer skuunit=0;
			for(ShipInboundItemVo item:itemlist) {
				 skuunit=skuunit+item.getQuantityShipped();
			 }
		     boolean expendrow=false;
			 for (int i=0;i<=sheet.getLastRowNum();i++){
				 Row sheetRow = sheet.getRow(i);
				//遍历列cell
				 if(i==0) {
						org.apache.poi.ss.usermodel.Cell cell = sheetRow.getCell(1);
						cell.setCellValue(shipment.getShipmentid());
				 }else if(i==1) {
					    org.apache.poi.ss.usermodel.Cell cell = sheetRow.getCell(0);
						cell.setCellValue("Name: "+shipment.getName());
				 }else if(i==2) {
				    	continue;
				 }else if(i==3) {
				    	for(int j=0;j<boxlist.size();j++) {
				    		Cell cell = sheetRow.createCell(11+j);
				    		cell.setCellValue("Box "+boxlist.get(j).getBoxnum()+" - QTY");
				    	}
				 }else if(i==4&&itemlist.size()-1>0&&expendrow==false) {
					 sheet.shiftRows(i, sheet.getLastRowNum(), itemlist.size()-1,true,false);
					 i--;expendrow=true;continue;
				 }else if(i>3&&i<=(3+itemlist.size())){
					 if(sheetRow==null) {
						sheetRow=sheet.createRow(i);
					 }
					ShipInboundItemVo skuinfo = itemlist.get(i-4);
			    	Cell cell = sheetRow.createCell(0);
			    	cell.setCellValue(skuinfo.getSellersku());
			    	cell = sheetRow.createCell(1);
			    	cell.setCellValue(skuinfo.getAsin());
			      	cell = sheetRow.createCell(2);
			    	cell.setCellValue(skuinfo.getPname());
			      	cell = sheetRow.createCell(3);
			    	cell.setCellValue(skuinfo.getFNSKU());
			      	cell = sheetRow.createCell(4);
					cell.setCellValue("--");
					cell = sheetRow.createCell(5);
					cell.setCellValue("Merchant");
					cell = sheetRow.createCell(6);
					cell.setCellValue("--");
					cell = sheetRow.createCell(7);
					cell.setCellValue("Merchant");
			      	cell = sheetRow.createCell(8);
			      	cell.setCellType(CellType.NUMERIC);
			    	cell.setCellValue(skuinfo.getQuantityShipped());
			      	cell = sheetRow.createCell(9);
			      	cell.setCellType(CellType.NUMERIC);
			    	cell.setCellValue(skuinfo.getQuantityShipped());
			    	for(int j=0;j<boxlist.size();j++) {
			    		  cell = sheetRow.createCell(11+j);
			    		  boolean hasvalue=false;
			    	  	for(int z=0;z<boxcase.size();z++) {
				    		if(boxcase.get(z).getMerchantsku().equals(skuinfo.getSellersku()) &&boxlist.get(j).getBoxnum()==boxcase.get(z).getNumberofcase()) {
				    		  	if(plan.getArecasesrequired()==false) {
				    		  		if(boxcase.get(z).getQuantity()==null||boxcase.get(z).getQuantity()==0) {
					        		  	cell.setCellType(CellType.NUMERIC);
					    		  		cell.setCellValue(0);hasvalue=true;break;
					    		  	}else {
					        		  	cell.setCellType(CellType.NUMERIC);
					    		  		cell.setCellValue(boxcase.get(z).getQuantity());hasvalue=true;break;
					    		  	}
				    		  	}else {
				    		  		if(boxcase.get(z).getNumberofcase()==null||boxcase.get(z).getNumberofcase()==0) {
					        		  	cell.setCellType(CellType.NUMERIC);
					    		  		cell.setCellValue(0);hasvalue=true;break;
					    		  	}else {
					        		  	cell.setCellType(CellType.NUMERIC);
					    		  		cell.setCellValue(boxcase.get(z).getQuantity());hasvalue=true;break;
					    		  	}
				    		  	}
				    		
				    		}
				    	}
			    	  	if(hasvalue==false) {
			    		  	cell.setCellType(CellType.NUMERIC);
			    	  		cell.setCellValue(0);
			    	  	}
			    	}
			    }else   if(i==(3+itemlist.size()+2)){
			    	for(int j=0;j<boxlist.size();j++) {
			    		Cell cell = sheetRow.createCell(11+j);
			    	  	cell.setCellType(CellType.STRING);
			    		cell.setCellValue("Box "+boxlist.get(j).getBoxnum());
			    	}
			    }else   if(i==(3+itemlist.size()+3)){
			    	for(int j=0;j<boxlist.size();j++) {
			    		Cell cell = sheetRow.createCell(11+j);
			    	  	cell.setCellType(CellType.NUMERIC);
			    		cell.setCellValue(boxlist.get(j).getWeight().doubleValue());
			    	}
			    }else   if(i==(3+itemlist.size()+4)){
			    	for(int j=0;j<boxlist.size();j++) {
			    		Cell cell = sheetRow.createCell(11+j);
			    	  	cell.setCellType(CellType.NUMERIC);
			    		cell.setCellValue(Double.valueOf(boxlist.get(j).getLength().doubleValue()));
			    	}
			    }else   if(i==(3+itemlist.size()+5)){
			    	for(int j=0;j<boxlist.size();j++) {
			    		Cell cell = sheetRow.createCell(11+j);
			    	  	cell.setCellType(CellType.NUMERIC);
			    		cell.setCellValue(Double.valueOf(boxlist.get(j).getWidth().doubleValue()));
			    	}
			    }else   if(i==(3+itemlist.size()+6)){
			    	for(int j=0;j<boxlist.size();j++) {
			    		Cell cell = sheetRow.createCell(11+j);
			    	  	cell.setCellType(CellType.NUMERIC);
			    		cell.setCellValue(  Double.valueOf(boxlist.get(j).getHeight().doubleValue()));
			    	}
			    }
				if(i>(3+itemlist.size()+2)) {
						org.apache.poi.ss.usermodel.Cell cell = sheetRow.getCell(0);
						if(cell==null)continue;
						String cellValue = cell.getStringCellValue().trim();
						if(StrUtil.isNotEmpty(cellValue)) {
							if(cellValue.contains("${center}")) {
								cell.setCellValue(cellValue.replace("${center}",shipment.getDestinationfulfillmentcenterid()));
							}
							if(cellValue.contains("${skunum}")) {
							  	cell.setCellType(CellType.NUMERIC);
								cell.setCellValue(cellValue.replace("${skunum}",itemlist.size()+""));
							}
							if(cellValue.contains("${skuunits}")) {
							  	cell.setCellType(CellType.NUMERIC);
								cell.setCellValue(cellValue.replace("${skuunits}",skuunit+""));
							}
						}
					}
			 }
			 workbook.write(bos);
			return bos;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
