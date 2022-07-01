package com.wimoor.amazon.inbound.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.inbound.mapper.ShipInboundShipmentMapper;
import com.wimoor.amazon.inbound.pojo.dto.ShipCartShipmentDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipInboundBoxService;
import com.wimoor.amazon.inbound.service.IShipInboundCaseService;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.amazon.report.mapper.InventoryReportMapper;
import com.wimoor.amazon.report.pojo.entity.InventoryReport;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 

@Service("shipInboundShipmentService")
@RequiredArgsConstructor
public class ShipInboundShipmentServiceImpl extends  ServiceImpl<ShipInboundShipmentMapper,ShipInboundShipment> implements IShipInboundShipmentService { 
	 
	final IFulfillmentInboundService iFulfillmentInboundService;
	
	final IAmazonGroupService iAmazonGroupService;
	
	final IAmazonAuthorityService amazonAuthorityService;
	
	final IMarketplaceService marketplaceService;
	@Autowired
	IShipInboundShipmentRecordService shipInboundShipmentRecordService;
	@Resource
	IShipInboundPlanService shipInboundPlanService;
	@Resource
	private IShipInboundItemService shipInboundItemService;
	
	final InventoryReportMapper inventoryReportMapper;
	
	final IShipInboundCaseService iShipInboundCaseService;
	
	final IShipInboundBoxService iShipInboundBoxService;
	
	final ISubmitfeedService submitfeedService;
	
	public IPage<ShipInboundShipmenSummarytVo> findByTraceCondition(Page<Object> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByTraceCondition(page, param);
	} 


  public ByteArrayOutputStream createInboundFBAExcel(ShipInboundPlan plan,ShipInboundShipment shipment,List<ShipInboundItemVo> itemlist,List<ShipInboundBox> boxlist,List<ShipInboundCase> boxcase , String sellerid) {
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	File file = null;
	try {
		 InputStream is = new ClassPathResource("template/shipmentid.xlsx").getInputStream();
		 Workbook workbook =  WorkbookFactory.create(is);
		 Sheet sheet = workbook.getSheetAt(0);
		 Integer skuunit=0;
		 for(ShipInboundItemVo  item:itemlist) {
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
		    		cell.setCellValue(new Double(boxlist.get(j).getLength().doubleValue()));
		    	}
		    }else   if(i==(3+itemlist.size()+5)){
		    	for(int j=0;j<boxlist.size();j++) {
		    		Cell cell = sheetRow.createCell(11+j);
		    	  	cell.setCellType(CellType.NUMERIC);
		    		cell.setCellValue(new Double(boxlist.get(j).getWidth().doubleValue()));
		    	}
		    }else   if(i==(3+itemlist.size()+6)){
		    	for(int j=0;j<boxlist.size();j++) {
		    		Cell cell = sheetRow.createCell(11+j);
		    	  	cell.setCellType(CellType.NUMERIC);
		    		cell.setCellValue(new Double(boxlist.get(j).getHeight().doubleValue()));
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
	}finally {
        if(file!=null) {
        	file.deleteOnExit();
        }
	}
	return null;
}

@Override
public String createInboundShipment(ShipInboundShipment shipment) {
	// TODO Auto-generated method stub
    ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
    AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
    Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
	String mshipmentid= iFulfillmentInboundService.createInboundShipment(auth, market, inplan, shipment);
	if(mshipmentid.equals(shipment.getShipmentid())) {
		this.updateById(shipment);
		shipInboundShipmentRecordService.saveRecord(shipment);
		for(ShipInboundItem item:shipment.getItemList()) {
			changeAmazonInventoryReport(auth.getId(),market,item.getSellersku(),item.getQuantityshipped());
		}
	}else {
		throw new BizException("未正常确认货件");
	}
	return mshipmentid;
}
private void updateItemList(AmazonAuthority auth,Marketplace market,List<ShipInboundItem> itemlist) {
	for(ShipInboundItem item:itemlist) {
		LambdaQueryWrapper<ShipInboundItem> query = new LambdaQueryWrapper<ShipInboundItem>()
													        .eq(ShipInboundItem::getSellersku, item.getSellersku())
															.eq(ShipInboundItem::getShipmentid, item.getShipmentid());
		ShipInboundItem olditem = shipInboundItemService.getOne(query);
		if(olditem.getQuantityshipped()!=item.getQuantityshipped()) {
			shipInboundItemService.update(item, query);
			changeAmazonInventoryReport(auth.getId(),market,item.getSellersku(),item.getQuantityshipped()-olditem.getQuantityshipped());
		}
	}
}
@Override
public String updateInboundShipment(ShipInboundShipment shipment) {
	// TODO Auto-generated method stub
    ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
    AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
    Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
	String shipmentid = iFulfillmentInboundService.updateInboundShipment(auth, market, inplan, shipment);
	if(StrUtil.isNotBlank(shipmentid)&&shipmentid.equals(shipment.getShipmentid())) {
		ShipInboundShipment oldshipment = this.getById(shipmentid);
		if(oldshipment.getStatus()!=shipment.getStatus()) {
			shipInboundShipmentRecordService.saveRecord(shipment);
			switch(shipment.getStatus()) {
				case 0:shipment.setStatus0date(new Date());break;
				case 1:	shipment.setStatus1date(new Date());break;
				case 2:shipment.setStatus2date(new Date());break;
				case 3:shipment.setStatus3date(new Date());break;
				case 4:shipment.setStatus4date(new Date());break;
				case 5:shipment.setShipedDate(new Date());break;
			}
		}
		this.updateById(shipment);
		updateItemList(auth,market,shipment.getItemList());
	}
	return shipmentid;
}
 
private void changeAmazonInventoryReport(String amazonauthid,Marketplace market ,String sku,Integer amount ) throws BizException {
	String marketplaceid=market.getMarketplaceid();
	if(market.getRegion().equals("EU")) {
		marketplaceid= "EU";
	}
		List<InventoryReport> invlist=inventoryReportMapper.selectList(new LambdaQueryWrapper<InventoryReport>()
				                                                            .eq(InventoryReport::getAmazonAuthId,amazonauthid)
				                                                            .eq(InventoryReport::getSku, sku)
				                                                            .eq(InventoryReport::getIsnewest, true)
				                                                            .eq(InventoryReport::getMarketplaceid, marketplaceid));
		if(invlist==null||invlist.size()==0) {
			InventoryReport inv = new InventoryReport();
			inv.setByday(new Date());
			inv.setAmazonAuthId(amazonauthid);
			inv.setSku(sku);
			if(market.getRegion().equals("EU")) {
				inv.setMarketplaceid("EU");
			}else {
				inv.setMarketplaceid(marketplaceid);
			}
			inv.setAfnInboundWorkingQuantity(amount);
			inv.setIsnewest(true);
			inv.setPcondition("New");
			inventoryReportMapper.insert(inv);
		}else {
			for (int i = 0; i < invlist.size(); i++) {
				InventoryReport inv = invlist.get(i);
				inv.setByday(new Date());
				if (inv.getAfnInboundWorkingQuantity() == null) {
					inv.setAfnInboundWorkingQuantity(0);
				}
				if(inv.getAfnInboundReceivingQuantity() ==null) {
					inv.setAfnInboundReceivingQuantity(0);
				}
				if (amount < 0) {
					if (inv.getAfnInboundWorkingQuantity() + amount < 0) {
						amount = inv.getAfnInboundWorkingQuantity() + amount;
						inv.setAfnInboundWorkingQuantity(0);
					} else {
						inv.setAfnInboundWorkingQuantity(amount + inv.getAfnInboundWorkingQuantity());
						amount = 0;
					}
					if (inv.getAfnInboundReceivingQuantity() + amount < 0) {
						amount = inv.getAfnInboundReceivingQuantity() + amount;
						inv.setAfnInboundReceivingQuantity(0);
					} else if (amount != 0) {
						inv.setAfnInboundReceivingQuantity(amount + inv.getAfnInboundReceivingQuantity());
						amount = 0;
					}
				} else {
					inv.setAfnInboundWorkingQuantity(amount + inv.getAfnInboundWorkingQuantity());
				}
				inventoryReportMapper.updateById(inv);
			}
		}
	 
	

}


@Override
public String saveCartShipment(UserInfo user,ShipCartShipmentDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundShipment shipment=this.getById(dto.getShipmentid()); 
	iShipInboundBoxService.remove(new LambdaQueryWrapper<ShipInboundBox>()
			                        .eq(ShipInboundBox::getShipmentid, dto.getShipmentid()));
	iShipInboundCaseService.remove(new LambdaQueryWrapper<ShipInboundCase>()
			                        .eq(ShipInboundCase::getShipmentid, dto.getShipmentid()));
	for(ShipInboundBox box:dto.getBoxListDetail()) {
		iShipInboundBoxService.save(box);
	}
	for(ShipInboundCase casebox:dto.getCaseListDetail()) {
		iShipInboundCaseService.save(casebox);
	}
	shipment.setOldboxnum(shipment.getBoxnum());
	shipment.setBoxnum(dto.getBoxnum());
	
	if(dto.getOpttype()!=null&&"submit".equals(dto.getOpttype())) {
		ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
		Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
		List<ShipInboundItemVo> itemlist = shipInboundItemService.listByShipmentid(dto.getShipmentid());
		 try {
			iFulfillmentInboundService.putTransportDetailsRequest(auth, market, inplan, shipment);
		 }catch(Exception e) {
			this.updateById(shipment);
			throw new BizException("物流商信息提交失败");
		 }
		  ByteArrayOutputStream stream = createInboundFBAExcel(inplan, shipment, itemlist, dto.getBoxListDetail(), dto.getCaseListDetail(), auth.getSellerid());
		  Map<String,Object> map = submitfeedService.selectByFeedTypeAndFileName(auth.getId(), inplan.getMarketplaceid(),"_POST_FLAT_FILE_FROM_EXCEL_FBA_CREATE_CARTON_INFO_",shipment.getShipmentid());
		  //判断状态，上一条请求正在处理中，请稍后再提交;如果请求已经完成，可再次提交。
		  if(map==null ||map.get("feedstatus")==null|| "Error".equals(map.get("feedstatus").toString())||
				"_CANCELLED_".equals(map.get("feedstatus").toString())||"_DONE_".equals(map.get("feedstatus").toString())){
			AmzSubmitFeedQueue submitfeed = submitfeedService.HandlerFeedQueue(stream, shipment.getShipmentid(), auth, "_POST_FLAT_FILE_FROM_EXCEL_FBA_CREATE_CARTON_INFO_", user);
			shipment.setSubmissionid(submitfeed.getId());
			shipment.setFeedstatus("waiting");
		  } else {
			throw new BizException("请求正在处理中，请稍后再试！");
		  }
	}
	this.updateById(shipment);
	return dto.getShipmentid();
}


@Override
public String getPkgLabelUrl(String shipmentid,String pagetype) {
	// TODO Auto-generated method stub
	ShipInboundShipment shipment=this.getById(shipmentid); 
	ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
	if(shipment.getStatus()>2 &&shipment.getStatus()<=4) {
		shipment.setStatus(4);
		shipment.setStatus4date(new Date());
		this.updateById(shipment);
	}
	return iFulfillmentInboundService.getLabelDownloadUR(auth, shipment, pagetype);
}



}
