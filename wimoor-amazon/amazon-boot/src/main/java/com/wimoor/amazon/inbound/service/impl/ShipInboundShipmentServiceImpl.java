package com.wimoor.amazon.inbound.service.impl;

import java.awt.Font;
import java.awt.FontMetrics;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazon.spapi.model.fulfillmentinbound.Address;
import com.amazon.spapi.model.fulfillmentinbound.GetTransportDetailsResponse;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentItem;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentList;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredLtlDataOutput;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredSmallParcelDataOutput;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredSmallParcelPackageOutput;
import com.amazon.spapi.model.fulfillmentinbound.NonPartneredSmallParcelPackageOutputList;
import com.amazon.spapi.model.fulfillmentinbound.ShipmentStatus;
import com.amazon.spapi.model.fulfillmentinbound.TransportContent;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.feed.service.impl.FeedFileInboundFBAExcel;
import com.wimoor.amazon.feed.service.impl.FeedFileInboundFBAXML;
import com.wimoor.amazon.inbound.mapper.ShipInboundBoxMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundCaseMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundShipmentMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransHisMapper;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.inbound.mapper.ShipTransDetailMapper;
import com.wimoor.amazon.inbound.pojo.dto.ShipCartShipmentDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundItemDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmenSummaryDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTransHis;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundshipmentTraceupload;
import com.wimoor.amazon.inbound.pojo.entity.ShipTransDetail;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipInboundBoxService;
import com.wimoor.amazon.inbound.service.IShipInboundCaseService;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inbound.service.IShipInboundshipmentTraceuploadService;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.service.IInventorySupplyService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.inventory.pojo.dto.EnumByInventory;
import com.wimoor.erp.inventory.pojo.dto.InventoryParameter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import feign.FeignException;
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
	
	final IInventorySupplyService iInventorySupplyService;
	
	final IShipInboundCaseService iShipInboundCaseService;
	
	final IShipInboundBoxService iShipInboundBoxService;
	
	final ISubmitfeedService submitfeedService;
	
	final IProductInfoService iProductInfoService;
	
	final ShipInboundTransMapper shipInboundTransMapper;
	
	final ShipInboundItemMapper shipInboundItemMapper;
	
	final IShipAddressService shipAddressService;
	
	final ShipInboundBoxMapper shipInboundBoxMapper;
	
	final ShipInboundCaseMapper shipInboundCaseMapper;
	
	final ShipTransDetailMapper shipTransDetailMapper; 
	
	final IShipInboundTransService shipInboundTransService;
	
	final ShipInboundTransHisMapper shipInboundTransHisMapper;
	final IShipInboundshipmentTraceuploadService iShipInboundshipmentTraceuploadService;
	@Resource
	private InventoryReportMapper inventoryReportMapper;
	
	@Autowired
	ErpClientOneFeignManager erpClientOneFeign;
	
	public IPage<ShipInboundShipmenSummarytVo> findByTraceCondition(ShipInboundShipmenSummaryDTO dto) {
		// TODO Auto-generated method stub
		Page<ShipInboundShipmenSummarytVo> page=dto.getPage();
   	    if(StrUtil.isNotBlank(dto.getSearchtype())&&StrUtil.isNotBlank(dto.getSearch())) {
   	    	 if(dto.getSearchtype().equals("remark")) {
   	    		 dto.setSearch("%"+dto.getSearch().trim()+"%");
   	    	 }else if(dto.getSearchtype().equals("name")) {
    	    	 dto.setSearch("%"+dto.getSearch().trim()+"%");
	    	 }else {
	    		 dto.setSearch("%"+dto.getSearch().trim()+"%");
	    	 }
   	    }else {
   	    	     dto.setSearch(null);
   	    }

		IPage<ShipInboundShipmenSummarytVo> list = this.baseMapper.findByTraceCondition(page,dto);
	   	 for(ShipInboundShipmenSummarytVo item:list.getRecords()) {
				String company = item.getCompanyid();
				if(company!=null) {
					Result<Object> stcompany = erpClientOneFeign.getTransCompanyAPI(company);
						if(Result.isSuccess(stcompany)&&stcompany.getData()!=null) {
							Map<String, Object> map = BeanUtil.beanToMap(stcompany.getData());
							item.setApiSystem(map.get("system").toString());
						}
					}
			    if(item.getArrivalTime()!=null) {
			    	if(item.getArrivalTime().after(new Date())) {
			    		Integer delayDays=(Double.valueOf(GeneralUtil.distanceOfDay(item.getArrivalTime(), new Date())).intValue()*-1);
			    		item.setDelayDays(delayDays);
			    	}else {
			    		Integer delayDays=Double.valueOf(GeneralUtil.distanceOfDay(item.getArrivalTime(), new Date())).intValue();
			    		item.setDelayDays(delayDays);
			    	}
			    } 
			}
	   	 return list;
	} 
	 
@Override
@Transactional
public String createInboundShipment(ShipInboundShipment shipment) {
	// TODO Auto-generated method stub
    ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
    AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
    Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
	String mshipmentid=iFulfillmentInboundService.createInboundShipment(auth, market, inplan, shipment);
	if(mshipmentid.equals(shipment.getShipmentid())) {
		this.updateById(shipment);
		inplan.setAuditstatus(3);
		inplan.setOpttime(new Date());
		shipInboundPlanService.updateById(inplan);
		try {
			shipInboundShipmentRecordService.saveRecord(shipment);
			for(ShipInboundItem item:shipment.getItemList()) {
				changeAmazonInventoryReport(inplan,item.getSellersku(),item.getQuantityshipped());
				LambdaQueryWrapper<ShipInboundItem> query=new LambdaQueryWrapper<ShipInboundItem>();
				query.eq(ShipInboundItem::getShipmentid, item.getShipmentid());
				query.eq(ShipInboundItem::getSellersku, item.getSellersku());
				this.shipInboundItemService.update(item,query);
			}
		}catch(Exception e) {
			e.printStackTrace();
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
			changeAmazonInventoryReport(auth.getId(),market,item.getSellersku());
		}
	}
}

@Override
@Transactional
public String updateInboundShipment(ShipInboundShipment shipment, String actiontype) {
	// TODO Auto-generated method stub
    ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
    AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
    Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
    String shipmentid =null;
    
	if(StrUtil.isBlank(actiontype)||!"updateself".equals(actiontype)) {
		shipmentid = iFulfillmentInboundService.updateInboundShipment(auth, market, inplan, shipment);
	}else {
		shipmentid=shipment.getShipmentid();
	}
	ShipInboundShipment oldshipment = this.getById(shipment.getShipmentid());
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
	return shipmentid;
}
 
private void changeAmazonInventoryReport(ShipInboundPlan plan, String sku, Integer amount){
	String market = plan.getMarketplaceid();
	Marketplace marketobj = marketplaceService.findMapByMarketplaceId().get(market);
	LambdaQueryWrapper<InventoryReport> query = new LambdaQueryWrapper<InventoryReport>();
	query.eq(InventoryReport::getSku, sku);
	if(marketobj.getRegion().equals("EU")) {
		query.eq(InventoryReport::getMarketplaceid, "EU");
	}else {
		query.eq(InventoryReport::getMarketplaceid, market);
	}
	query.eq(InventoryReport::getIsnewest, true);
	String groupid = plan.getAmazongroupid();
	String marketplaceid = plan.getMarketplaceid();
	AmazonAuthority amazonAuthority = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	query.eq(InventoryReport::getAmazonAuthId, amazonAuthority.getId());
	List<InventoryReport> invlist = inventoryReportMapper.selectList(query);
	if(invlist==null||invlist.size()==0) {
		InventoryReport inv = new InventoryReport();
		inv.setByday(new Date());
		inv.setAmazonAuthId(amazonAuthority.getId());
		inv.setSku(sku);
		if(marketobj.getRegion().equals("EU")) {
			inv.setMarketplaceid("EU");
		}else {
			inv.setMarketplaceid(market);
		}
		inv.setAfnInboundWorkingQuantity(amount);
		inv.setIsnewest(true);
		inv.setPcondition("New");
		inv.setByday(new Date());
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
			inv.setByday(new Date());
			inventoryReportMapper.update(inv,query);
		}
	}
}

private void changeAmazonInventoryReport(String amazonauthid,Marketplace market ,String sku) throws BizException {
	 AmazonAuthority auth = amazonAuthorityService.getById(amazonauthid);
	 auth.setMarketPlace(market);
	 try {
		 new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iInventorySupplyService.syncInventorySupply(auth, Arrays.asList(sku));	
			}
			 
		 }).start();
		 
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
}


@Override
public ShipInboundShipment saveCartShipment(UserInfo user,ShipCartShipmentDTO dto) {
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
	shipment.setTranstyle(dto.getTranstyle());
	shipment.setBoxnum(dto.getBoxnum());
	
	if(dto.getOpttype()!=null&&"submit".equals(dto.getOpttype())) {
		ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
		Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
		if(market!=null)auth.setMarketPlace(market);
		shipment.setCarrier(dto.getCarrier());
		List<ShipInboundItemVo> itemlist = shipInboundItemService.listByShipmentid(dto.getShipmentid());
		iFulfillmentInboundService.putTransportDetailsRequest(auth, market, inplan, shipment);
		    ByteArrayOutputStream stream = FeedFileInboundFBAExcel.getFile(inplan, shipment, itemlist, dto.getBoxListDetail(), dto.getCaseListDetail(), auth.getSellerid());
		    //判断状态，上一条请求正在处理中，请稍后再提交;如果请求已经完成，可再次提交。
			AmzSubmitFeedQueue submitfeed = submitfeedService.HandlerFeedQueue(stream, shipment.getShipmentid(), auth, FeedFileInboundFBAExcel.type, user);
			if(submitfeed!=null) {
				shipment.setSubmissionidExcel(submitfeed.getId());
				shipment.setFeedstatus("waiting");
			}
		    ByteArrayOutputStream stream2 = FeedFileInboundFBAXML.getFile(shipment, auth.getSellerid(),dto.getCaseListDetail());
			AmzSubmitFeedQueue submitfeedXML = submitfeedService.HandlerFeedQueue(stream2, shipment.getShipmentid(), auth, FeedFileInboundFBAXML.type, user);
			if(submitfeedXML!=null) {
				shipment.setSubmissionid(submitfeedXML.getId());
				shipment.setFeedstatus("waiting");
			}
	  }
	this.updateById(shipment);
	return shipment;
}


@Override
public String getLabelUrl(String shipmentid,String pagetype,String labelType,String pannum) {
	// TODO Auto-generated method stub
	ShipInboundShipment shipment=this.getById(shipmentid); 
	ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
	AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
	if(shipment.getStatus()>=2 &&shipment.getStatus()<=4) {
		if(shipment.getSubmissionid()==null) {
			throw new BizException("您还没有提交箱子信息，无法操作");
		}
		shipment.setStatus(4);
		shipment.setStatus4date(new Date());
		this.updateById(shipment);
	}
	return iFulfillmentInboundService.getLabelDownloadUR(auth, shipment, pagetype,labelType,pannum);
}

public List<ShipInboundShipmenSummarytVo> getShipmentSyncList(Map<String, Object> param) {
	//String shopid = param.get("shopid").toString();
	String search = param.get("search").toString();
	String marketplaceid = param.get("marketplaceid").toString();
	String groupid = param.get("groupid").toString();
	String fromdate = param.get("fromdate").toString();
	String enddate = param.get("enddate").toString();
	Marketplace marketPlace=marketplaceService.findMapByMarketplaceId().get(marketplaceid);
	AmazonGroup group = iAmazonGroupService.getById(groupid);
	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	List<ShipInboundShipmenSummarytVo> result=new ArrayList<ShipInboundShipmenSummarytVo>();
	if(auth!=null) {
		auth.setMarketPlace(marketPlace);
		//当有search的时候   就没有fromdate和enddate 具体查一个
		InboundShipmentList inboundShipmentList =null;
		if(StrUtil.isBlank(search)) {
			  inboundShipmentList = iFulfillmentInboundService.listShipment(auth,marketPlace,fromdate,enddate);
		}else {
			  inboundShipmentList = iFulfillmentInboundService.listShipment(auth,marketPlace,Arrays.asList(search));
		}
		if(inboundShipmentList!=null) {
			for(InboundShipmentInfo item:inboundShipmentList) {
				ShipInboundShipmenSummarytVo shipmentvo=new ShipInboundShipmenSummarytVo();
				shipmentvo.setGroupname(group.getName());
				shipmentvo.setGroupid(groupid);
				shipmentvo.setShipmentid(item.getShipmentId());
				shipmentvo.setCenter(enddate);
				Map<String, Object> tocountry =  findToCountry(item.getDestinationFulfillmentCenterId(),null);
				if(tocountry!=null&&tocountry.get("marketplaceId")!=null) {
					marketplaceid=tocountry.get("marketplaceId").toString()	;
				}
				Marketplace mmarket = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
				shipmentvo.setMarketplaceid(marketplaceid);
				shipmentvo.setCountry(mmarket.getName());
				shipmentvo.setCountryCode(mmarket.getMarket());
				shipmentvo.setCenterId(item.getDestinationFulfillmentCenterId());
				shipmentvo.setName(item.getShipmentName());
				if(item.getShipmentStatus()!=null) {
					shipmentvo.setShipmentstatus(item.getShipmentStatus().getValue());
				}
				shipmentvo.setAreCasesRequired(item.isAreCasesRequired());
				ShipInboundShipment ship = this.baseMapper.selectById(item.getShipmentId());
				shipmentvo.setSyncinv(-1);
				if(ship!=null) {
					shipmentvo.setSyncinv(ship.getSyncInv());
				}
				String address=item.getShipFromAddress().getAddressLine1();
				if(StrUtil.isNotBlank(address)) {
					address=address+","+item.getShipFromAddress().getAddressLine2();
				}else {
					address=item.getShipFromAddress().getAddressLine2();
				}
				address=address+","+item.getShipFromAddress().getCity();
				address=address+","+item.getShipFromAddress().getCountryCode();
				address=address+","+item.getShipFromAddress().getPostalCode();
				address=address+","+item.getShipFromAddress().getName();
				shipmentvo.setRemark(address);
				result.add(shipmentvo);
			}
			
		}
	}
	return result; 
}


@Override
public Map<String, Object> findToCountry(String destinationFulfillmentCenterId, String region) {
	// TODO Auto-generated method stub
	return this.baseMapper.findToCountry(destinationFulfillmentCenterId, region);
}

@Override
public ShipInboundShipmentDTO syncShipment(String groupid, String marketplaceid, String shipmentid,String warehouseid) {
	// TODO Auto-generated method stub
	Marketplace marketPlace=marketplaceService.findMapByMarketplaceId().get(marketplaceid);
	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	InboundShipmentList inboundShipmentList = iFulfillmentInboundService.listShipment(auth,marketPlace,Arrays.asList(shipmentid));
	if(inboundShipmentList.size()>0) {
		InboundShipmentInfo ship = inboundShipmentList.get(0);
		ShipInboundShipment shipment = shipInboundPlanService.createShipment(auth, marketPlace, ship, warehouseid, null);
		List<ShipInboundItem> itemlist = iFulfillmentInboundService.syncItemsByShipmentId(auth, marketPlace, shipment);
		ShipInboundPlan plan = shipment.getInboundplan();
		plan.setSkunum(itemlist.size());
		shipInboundPlanService.updateById(plan);
		ShipInboundShipmentDTO shipmentdto=new ShipInboundShipmentDTO();
		ShipInboundPlanDTO plandto=new ShipInboundPlanDTO();
	    BeanUtil.copyProperties(shipment, shipmentdto);
	    BeanUtil.copyProperties(plan, plandto);
		shipmentdto.setInboundplan(plandto);
		List<ShipInboundItemDTO> itemlistDto=new LinkedList<ShipInboundItemDTO>();
		itemlist.stream().forEach(item->{
			  ShipInboundItemDTO dto=new ShipInboundItemDTO();
			  BeanUtil.copyProperties(item, dto);
			  itemlistDto.add(dto);
		  });
		  shipmentdto.setItemList(itemlistDto);
		  return shipmentdto;
	}
	return null;
}


@Override
public ShipInboundShipmenSummarytVo getUnSyncShipment(String groupid, String marketplaceid, String shipmentid, String warehouseid) {
	// TODO Auto-generated method stub
	Marketplace marketPlace=marketplaceService.findMapByMarketplaceId().get(marketplaceid);
	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	AmazonGroup group = iAmazonGroupService.getById(groupid);
	InboundShipmentList inboundShipmentList = iFulfillmentInboundService.listShipment(auth,marketPlace,Arrays.asList(shipmentid));
	if(inboundShipmentList.size()>0) {
		InboundShipmentInfo ship = inboundShipmentList.get(0);
		ShipInboundShipment shipment = shipInboundPlanService.convertShipmentPojo(auth, marketPlace, ship, warehouseid, null);
		ShipInboundShipment oldshipment = this.getById(shipment.getShipmentid());
		ShipInboundPlan plan = shipment.getInboundplan();
	    if(oldshipment==null) {
	    	shipment.setInboundplanid(plan.getId());
	    	this.save(shipment);
	    	shipInboundPlanService.save(plan);
	    }else {
	    	if(oldshipment.getInboundplanid()!=null) {
	    		ShipInboundPlan oldplan = shipInboundPlanService.getById(oldshipment.getInboundplanid());
	    		if(oldplan!=null) {
	    			shipment.setInboundplanid(oldplan.getId());
	    		}else {
	    			shipInboundPlanService.save(plan);
	    			shipment.setInboundplanid(plan.getId());
	    		}
	    	}
	    	this.updateById(shipment);
	    }
		shipInboundPlanService.updateById(plan);
		List<ShipInboundItem> itemlist = iFulfillmentInboundService.getUnSyncItemsByShipmentId(auth, marketPlace, shipment);
		plan.setSkunum(itemlist.size());
		ShipInboundShipmenSummarytVo shipmentdvo=new ShipInboundShipmenSummarytVo();
	    BeanUtil.copyProperties(shipment, shipmentdvo);
	    shipmentdvo.setGroupid(auth.getGroupid());
	    shipmentdvo.setGroupname(group.getName());
	    Marketplace market = marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
	    shipmentdvo.setCountry(market.getName());
	    shipmentdvo.setCountryCode(market.getMarket());
	    shipmentdvo.setName(shipment.getName());
	    shipmentdvo.setWarehouseid(warehouseid);
	    shipmentdvo.setSkuamount(Integer.valueOf(itemlist.size()).longValue());
		List<ShipInboundItemVo> itemlistDto=new LinkedList<ShipInboundItemVo>();
		itemlist.stream().forEach(item->{
				ShipInboundItemVo vo=new ShipInboundItemVo();
				  BeanUtil.copyProperties(item, vo);
				  String msku= iProductInfoService.getMSKU(auth.getId(), market.getMarketplaceid(), item.getSellersku());
				  Map<String, Object> map = iProductInfoService.findNameAndPicture(item.getSellersku(), market.getMarketplaceid(), groupid);
				  vo.setFNSKU(item.getFulfillmentnetworksku());
				  if(msku!=null) {
					  vo.setMsku(msku);
				  }else {
					  vo.setMsku(item.getSellersku());
				  }
				  vo.setSku(item.getSellersku());
				  if(map!=null) {
					  if(map.get("name")!=null) {
						  vo.setName(map.get("name").toString());
					  }
					  if(map.get("image")!=null) {
						  vo.setImage(map.get("image").toString());
					  }
				  }
				  
				  vo.setQuantityShipped(item.getQuantityshipped());
				  vo.setQuantityReceived(item.getQuantityreceived());
				  itemlistDto.add(vo);
			  });
		shipmentdvo.setItemList(itemlistDto);
		return shipmentdvo;
	}
	return null;
}
	public void setExcelBookShipmentLabel(String shopid, SXSSFWorkbook workbook, String shipmentid,List<Map<String,Object>> paralist) {
		Sheet sheet = workbook.createSheet("sheet1");
		Map<String, Integer> titlemap = new HashMap<String, Integer>();
		titlemap.put("No.", 0);
		titlemap.put("sku", 1);
		titlemap.put("fnsku", 2);
		titlemap.put("country", 3);
		titlemap.put("conditions", 4);
		titlemap.put("quantity", 5);
		titlemap.put("title", 6);
		titlemap.put("code", 7);
		// 在索引0的位置创建行（最顶端的行）
		Row row = sheet.createRow(0);
		for (String key : titlemap.keySet()) {
			Cell cell = row.createCell(titlemap.get(key)); // 在索引0的位置创建单元格(左上端)
			if (!"No.".equals(key)) {
				cell.setCellValue(key.toUpperCase());
			} else {
				cell.setCellValue(key);
			}
		}
		List<Map<String, Object>> skulist=new ArrayList<Map<String,Object>>();
		if(StrUtil.isNotEmpty(shipmentid)) {
			skulist = this.baseMapper.findLabelByshipmentid(shopid, shipmentid);
		}else{
			skulist=paralist;
		}
		Font f = new Font("宋体", Font.PLAIN, 12);
		@SuppressWarnings("restriction")
		FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
		for (int i = 0; i < skulist.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			Map<String, Object> skumap = skulist.get(i);
			for (String key : skumap.keySet()) {
				Cell cell = skurow.createCell(titlemap.get(key));
				if ("title".equals(key)) {
					String title = skumap.get(key).toString();
					char[] array = title.toCharArray();
					// cell.setCellValue();
					String pre = "";
					int begin = 0;
					for (; begin < array.length; begin++) {
						pre = pre + array[begin];
						if (fm.stringWidth(pre) >= 114)
							break;
					}
					String rear = "";
					for (int end = array.length - 1; end > begin; end--) {
						rear = array[end] + rear;
						if (fm.stringWidth(rear) >= 114)
							break;
					}
					cell.setCellValue(pre + "..." + rear);
				} else {
					if("code".equals(key)) {
						cell.setCellValue(skumap.get(key).toString().replace("FBA", "")); 
					}else {
						cell.setCellValue(skumap.get(key).toString());
					}
				}
			}
			Cell cell = skurow.createCell(titlemap.get("No.")); // 在索引0的位置创建单元格(左上端)
			cell.setCellValue(i + 1);
			Cell cell2 = skurow.createCell(titlemap.get("conditions")); // 在索引0的位置创建单元格(左上端)
			cell2.setCellValue("New");
		}
	}
	
	public void setPDFDocLabel(String shopid, Document document, String shipmentid, PdfWriter writer) {
		List<Map<String, Object>> skulist = this.baseMapper.findLabelByshipmentid(shopid, shipmentid);
		setPDFDocLabel(document,skulist,writer);
	}
	
	public void setPDFDocLabel(Document document, List<Map<String,Object>> skulist, PdfWriter writer){
		document.addTitle("产品标签");
		document.addAuthor("wimoor");
		try {
		// step 3
		document.open();
		// step 4
		BaseFont baseFont = null;
		String path = new ClassPathResource("font/ARIALN.TTF").getPath();
		baseFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 8);
		// 方法一：使用Windows系统字体(TrueType)
		String path2 = new ClassPathResource("font/SIMYOU.TTF").getPath();
		baseFont = BaseFont.createFont(path2, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		com.itextpdf.text.Font font_zh = new com.itextpdf.text.Font(baseFont, 8);
		for (int i = 0; i < skulist.size(); i++) {
			if (skulist.get(i).get("fnsku") == null || skulist.get(i).get("quantity") == null) {
				Paragraph msg = new Paragraph(skulist.get(i).get("sku").toString() + "数据异常", font_zh);
				document.add(msg);
			} else {
				String fnsku = skulist.get(i).get("fnsku").toString().trim();// "X001IWP8ZH"
				Barcode128 code128 = new Barcode128();
				code128.setCode(fnsku);
				code128.setCodeType(Barcode128.CODE128);
				PdfContentByte cb = writer.getDirectContent();
				Image code128Image = code128.createImageWithBarcode(cb, null, null);
				code128Image.scalePercent(100);
				String title = skulist.get(i).get("title").toString();
				title = GeneralUtil.formatterName(title);
				Paragraph p = new Paragraph(title, font);
				Paragraph p2 = new Paragraph("新品", font_zh);
				p2.setSpacingAfter(10f);
				String quantity = skulist.get(i).get("quantity") == null ? "0" : skulist.get(i).get("quantity").toString();
				for (int j = 0; j < Integer.parseInt(quantity); j++) {
					document.add(code128Image);
					document.add(p);
					document.add(p2);
				}
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


	
	public void validateShipment(String shipmentid) {
		ShipInboundShipment shipment = this.baseMapper.selectById(shipmentid);
		ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getInboundplanid());
		//拉取当前货件的信息
		Marketplace marketPlace=marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getAmazongroupid(), plan.getMarketplaceid());
		if(auth!=null) {
			auth.setMarketPlace(marketPlace);
			List<InboundShipmentItem> itemlist = iFulfillmentInboundService.requestInboundShipmentsItem(auth, shipmentid,marketPlace);
			InboundShipmentInfo shipmentReponse = iFulfillmentInboundService.requestInboundShipments(auth, shipmentid,marketPlace);
			Address amzaddress = shipmentReponse.getShipFromAddress();
			ShipAddress sysaddress = shipAddressService.getById(plan.getShipfromaddressid());
			if(shipmentReponse!=null&&shipmentReponse.getShipmentStatus()!=null) {
				if(!shipmentReponse.getShipmentStatus().getValue()
						.equals(shipment.getShipmentstatus())
					) {
					throw new BizException("状态："+shipment.getShipmentstatus()+"与亚马逊货件状态："+shipmentReponse.getShipmentStatus()+"不匹配！");
				}
				if(!shipmentReponse.getShipmentStatus().equals(ShipmentStatus.CANCELLED)
						&&(shipment.getStatus()<=0)) {
					throw new BizException("本地货件取消货驳回与亚马逊货件状态："+shipmentReponse.getShipmentStatus()+"不匹配！");
				}
			}
			if(amzaddress.getAddressLine1()!=null
					&&StrUtil.isNotBlank(amzaddress.getAddressLine1().trim())
					&&!amzaddress.getAddressLine1().equals(sysaddress.getAddressline1())){
				throw new BizException("地址："+sysaddress.getAddressline1()+"与亚马逊货件地址："+amzaddress.getAddressLine1()+"不匹配！");
			}
			if(amzaddress.getAddressLine2()!=null
					&&StrUtil.isNotBlank(amzaddress.getAddressLine2().trim())
					&&!amzaddress.getAddressLine2().equals(sysaddress.getAddressline2().replace("\r", "").replace("\n", ""))){
				throw new BizException("地址："+sysaddress.getAddressline2()+"与亚马逊货件地址："+amzaddress.getAddressLine2()+"不匹配！");
			}
			if(amzaddress.getCity()!=null
					&&StrUtil.isNotBlank(amzaddress.getCity().trim())
					&&!amzaddress.getCity().equals(sysaddress.getCity())){
				throw new BizException("地址："+sysaddress.getCity()+"与亚马逊货件地址："+amzaddress.getCity()+"不匹配！");
			}
			if(amzaddress.getCountryCode()!=null
					&&StrUtil.isNotBlank(amzaddress.getCountryCode().trim())
					&&!amzaddress.getCountryCode().equals(sysaddress.getCountrycode())){
				throw new BizException("地址："+sysaddress.getCountrycode()+"与亚马逊货件地址："+amzaddress.getCountryCode()+"不匹配！");
			}
			if(amzaddress.getDistrictOrCounty()!=null
					&&StrUtil.isNotBlank(amzaddress.getDistrictOrCounty().trim())
					&&!amzaddress.getDistrictOrCounty().equals(sysaddress.getDistrictorcounty())){
				throw new BizException("地址："+sysaddress.getDistrictorcounty()+"与亚马逊货件地址："+amzaddress.getDistrictOrCounty()+"不匹配！");
			}
			if(amzaddress.getPostalCode()!=null
					&&StrUtil.isNotBlank(amzaddress.getPostalCode().trim())
					&&!amzaddress.getPostalCode().equals(sysaddress.getPostalcode())){
				throw new BizException("地址："+sysaddress.getPostalcode()+"与亚马逊货件地址："+amzaddress.getPostalCode()+"不匹配！");
			}
			if(amzaddress.getStateOrProvinceCode()!=null
					&&StrUtil.isNotBlank(amzaddress.getStateOrProvinceCode().trim())
					&&!amzaddress.getStateOrProvinceCode().equals(sysaddress.getStateorprovincecode())){
				throw new BizException("地址："+sysaddress.getStateorprovincecode()+"与亚马逊货件地址："+amzaddress.getStateOrProvinceCode()+"不匹配！");
			}
			Map<String,InboundShipmentItem> skuItem=new HashMap<String,InboundShipmentItem>();
			for(int j=0;j<itemlist.size();j++) {
				InboundShipmentItem sysitem = itemlist.get(j);
				skuItem.put(sysitem.getSellerSKU(), sysitem);
			}
			if(itemlist!=null && itemlist.size()>0) {
				QueryWrapper<ShipInboundItem> queryWrapper=new QueryWrapper<ShipInboundItem>();
				queryWrapper.eq("ShipmentId", shipmentid);
				List<ShipInboundItem> sysitemlist = shipInboundItemMapper.selectList(queryWrapper);
				String notinSku="";
				String notinventorySku="";
				for(int i=0;i<sysitemlist.size();i++) {
					ShipInboundItem item = sysitemlist.get(i);
					if(item!=null && item.getQuantityshipped()>0) {
						String sku = item.getSellersku();
						boolean isshow=false;
						boolean isnumnoteq=false;
						int num=item.getQuantityshipped();
						InboundShipmentItem sysitem=skuItem.get(sku);
						if(sysitem!=null&&sysitem.getSellerSKU().equals(sku) && sysitem.getQuantityShipped()>0 && num>0) {
							isshow=true;
							if(num!=sysitem.getQuantityShipped()) {
								isnumnoteq=true;
							}
						}
						if(isshow==false) {
							notinSku+=sku+",";
						}
						if(isnumnoteq==true) {
							notinventorySku+=sku+",";
						}
					}
				}
				if(!notinSku.equals("")) {
					notinSku=notinSku.substring(0,notinSku.length()-1);
					throw new BizException("此SKU："+notinSku+"不存在于卖家中心的发货列表中！");
				}
				if(!notinventorySku.equals("")) {
					notinventorySku=notinventorySku.substring(0,notinventorySku.length()-1);
					throw new BizException("此SKU："+notinventorySku+"的发货数量与卖家中心发货数量不匹配！");
				}
				
			}
		}
	}


	@Override
	public ShipInboundTrans getSelfTransData(String shipmentid) {
		QueryWrapper<ShipInboundTrans> queryWrapper=new QueryWrapper<ShipInboundTrans>();
		queryWrapper.eq("shipmentid", shipmentid);
		List<ShipInboundTrans> list = shipInboundTransMapper.selectList(queryWrapper);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}


	@Override
	public List<Map<String, Object>> findInboundItemByShipmentId(String shipmentid) {
		return shipInboundItemMapper.selectByShipmentid(shipmentid);
	}

	@Override
	@Cacheable(value = "PkgPaperCache")
	public Map<String, String> getPkgPaper(String transtyle) {
		Map<String, String> map = new TreeMap<String, String>();
		if ("LTL".equals(transtyle)) {
			map.put("PackageLabel_Plain_Paper", "1-1/1\"×4\"美国信纸");
			map.put("PackageLabel_Letter_6", "3-3/1\"×4\"美国信纸");
			map.put("PackageLabel_A4_4", "A4纸可显示4个标签");
		} else {
			map.put("PackageLabel_Plain_Paper", "1-1/1\"×4\"美国信纸");
			map.put("PackageLabel_Letter_2", "2-2/1\"×4\"美国信纸");
			map.put("PackageLabel_Letter_6", "3-3/1\"×4\"美国信纸");
			map.put("PackageLabel_A4_2", "A4纸可显示2个标签");
			map.put("PackageLabel_A4_4", "A4纸可显示4个标签");
		}
		return map;
	}


	@Override
	public List<ShipInboundBox> findShipInboundBoxByShipment(String shipmentid) {
		QueryWrapper<ShipInboundBox> queryWrapper=new QueryWrapper<ShipInboundBox>();
		queryWrapper.eq("shipmentid", shipmentid);
		queryWrapper.orderByAsc("boxnum");
		List<ShipInboundBox> list = shipInboundBoxMapper.selectList(queryWrapper);
		return list;
	}


	@Override
	public List<ShipInboundCase> findShipInboundCaseByShipment(String shipmentid) {
		QueryWrapper<ShipInboundCase> queryWrapper=new QueryWrapper<ShipInboundCase>();
		queryWrapper.eq("shipmentid", shipmentid);
		return shipInboundCaseMapper.selectList(queryWrapper);
	}


	@Override
	public List<Map<String, Object>> findShipInboundBox(String shipmentid) {
		return shipInboundBoxMapper.findShipInboundBox(shipmentid);
	}


	@Override
	public Map<String, Object> getSelfTransDataView(String shipmentid) {
		return shipInboundTransMapper.getSelfTransDataView(shipmentid);
	}


	@Override
	public ShipTransDetail getTransChannelInfo(String id) {
		return shipTransDetailMapper.selectById(id);
	}


	@Override
	public List<String> getCarrierInfo(String country, String transtyle) {
		QueryWrapper<Marketplace> queryWrapper=new QueryWrapper<Marketplace>();
		queryWrapper.eq("market", country);
		String countrycode=null; 
		List<Marketplace> mlist = marketplaceService.list(queryWrapper);
		if(mlist!=null) {
			countrycode=mlist.get(0).getMarket();
		}
		List<String> list = this.baseMapper.findCarrierByshipmentid(countrycode, transtyle);
		if (list.size() == 0) {
			list.add("OTHER");
		}
		return list;
	}
	
    public Integer refreshShipmentRec(String shipmentid) {
    	ShipInboundShipment shipment=this.baseMapper.selectById(shipmentid);
    	ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getInboundplanid());
    	AmazonAuthority amazonAuthority=amazonAuthorityService.selectByGroupAndMarket(plan.getAmazongroupid(), plan.getMarketplaceid());
    	Marketplace market=marketplaceService.getById(plan.getMarketplaceid());
    	shipment.setInboundplan(plan);
    	List<ShipInboundItem> response=iFulfillmentInboundService.syncItemsByShipmentId(amazonAuthority,market,shipment);
        Integer result=0;
        for(ShipInboundItem item:response) {
        	Integer rec=item.getQuantityreceived();
        	if(rec==null) {
        		rec=0;
        	}
        	result=result+rec;
        }
        return result;
    }

	@Override
	public void setExcelBoxDetail(UserInfo user, SXSSFWorkbook workbook, String shipmentid) {
		Sheet sheet = workbook.createSheet("sheet1");
		List<LinkedHashMap<String, Object>> boxlist = this.baseMapper.findBoxDetailByShipmentId(shipmentid);
		Row row = sheet.createRow(0);
		int index = 0;
		if (boxlist == null || boxlist.size() == 0) {
			return;
		}
		Cell cell = row.createCell(0);
		cell.setCellValue("SKU");
		cell = row.createCell(1);
		cell.setCellValue("装箱数量");
		cell = row.createCell(2);
		cell.setCellValue("箱号");
		cell = row.createCell(3);
		cell.setCellValue("长度(cm)");
		cell = row.createCell(4);
		cell.setCellValue("宽度(cm)");
		cell = row.createCell(5);
		cell.setCellValue("高度(cm)");
		cell = row.createCell(6);
		cell.setCellValue("重量(Kg)");
		for (int i = 0; i < boxlist.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			Map<String, Object> skumap = boxlist.get(i);
			index = 0;
			for (String key : skumap.keySet()) {
				cell = skurow.createCell(index++);
				cell.setCellValue(skumap.get(key).toString());
			}
		}
	}
	
	@Transactional
	public void saveTransTrance(UserInfo user,ShipInboundShipment shipment,String carrier,String transtyle,List<Map<String, Object>> boxinfo){
		if(StrUtil.isNotBlank(transtyle)) {
			shipment.setTranstyle(transtyle);
		}
		if(StrUtil.isNotEmpty(carrier)) {
			shipment.setCarrier(carrier);
			this.updateById(shipment);
		}
		List<ShipInboundBox> boxlist = this.findShipInboundBoxByShipment(shipment.getShipmentid());
		Map<Integer, Object> mymap = new HashMap<Integer, Object>();
		if (boxinfo != null) {
			for (Map<String, Object> myitem : boxinfo) {
				if(myitem.get("value")!=null && StrUtil.isNotEmpty(myitem.get("value").toString())) {
					mymap.put(Integer.parseInt(myitem.get("boxnum").toString()), myitem.get("value"));
				}
			}
		}
		for (ShipInboundBox item : boxlist) {
			if (mymap.containsKey(item.getBoxnum())) {
				item.setTrackingId(mymap.get(item.getBoxnum()).toString());
			} else {
				item.setTrackingId("");
			}
			item.setOpttime(new Date());
			item.setOperator(user.getId());
			shipInboundBoxMapper.updateById(item);
		}
		 try {
			 ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
			 AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
			 Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
			 auth.setMarketPlace(market);
			 iFulfillmentInboundService.putTransportDetailsRequest(auth, market, inplan, shipment);
			 String qid= shipment.getSubmissionidExcel();
			 if(StrUtil.isNotBlank(qid)) {
				 AmzSubmitFeedQueue que = submitfeedService.selectByPrimaryKey(qid);
				 if(que!=null) {
					 submitfeedService.callSubmitFeed(auth, market, que);
				 }
			 }else {
				    List<ShipInboundItemVo> volist = shipInboundItemService.listByShipmentid(shipment.getShipmentid());
				    List<ShipInboundCase> caselist = this.findShipInboundCaseByShipment(shipment.getShipmentid());
				    ByteArrayOutputStream stream = FeedFileInboundFBAExcel.getFile(inplan, shipment, volist, boxlist, caselist, auth.getSellerid());
					AmzSubmitFeedQueue submitfeed = submitfeedService.HandlerFeedQueue(stream, shipment.getShipmentid(), auth, FeedFileInboundFBAExcel.type, user);
					if(submitfeed!=null) {
						shipment.setSubmissionidExcel(submitfeed.getId());
						shipment.setFeedstatus("waiting");
						this.updateById(shipment);
					}
						
			 }
			 iShipInboundshipmentTraceuploadService.saveStatus(user,shipment.getShipmentid());
		}catch(BizException e) {
			throw e;
		}catch(Exception e) {
				this.updateById(shipment);
				throw new BizException("物流商信息提交失败");
		}
    }

	public void saveTransTrance(){
		List<ShipInboundshipmentTraceupload> list = iShipInboundshipmentTraceuploadService.listByAuth();
		for(ShipInboundshipmentTraceupload item:list) {
			List<ShipInboundBox> boxlist = this.findShipInboundBoxByShipment(item.getShipmentid());
			ShipInboundShipment shipment = this.baseMapper.selectById(item.getShipmentid());
			 try {
				 ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
				 UserInfo user=new UserInfo();
				 user.setId(item.getCreator());
				 user.setCompanyid(inplan.getShopid());
				 AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
				 Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
				 auth.setMarketPlace(market);
				 iFulfillmentInboundService.putTransportDetailsRequest(auth, market, inplan, shipment);
				 String qid= shipment.getSubmissionidExcel();
				 if(StrUtil.isNotBlank(qid)) {
					 AmzSubmitFeedQueue que = submitfeedService.selectByPrimaryKey(qid);
					 if(que!=null) {
						 submitfeedService.callSubmitFeed(auth, market, que);
					 }
				 }else {
					    List<ShipInboundItemVo> volist = shipInboundItemService.listByShipmentid(shipment.getShipmentid());
					    List<ShipInboundCase> caselist = this.findShipInboundCaseByShipment(shipment.getShipmentid());
					    ByteArrayOutputStream stream = FeedFileInboundFBAExcel.getFile(inplan, shipment, volist, boxlist, caselist, auth.getSellerid());
						AmzSubmitFeedQueue submitfeed = submitfeedService.HandlerFeedQueue(stream, shipment.getShipmentid(), auth, FeedFileInboundFBAExcel.type, user);
						if(submitfeed!=null) {
							shipment.setSubmissionidExcel(submitfeed.getId());
							shipment.setFeedstatus("waiting");
							this.updateById(shipment);
						}
							
				 }
				 item.setStatus(1);
			}catch(BizException e) {
				item.setStatus(0);
				item.setErrormsg(e.getMessage());
			}catch(Exception e) {
				item.setStatus(0);
				item.setErrormsg(e.getMessage());
			}
			 item.setOpttime(new Date());
			 iShipInboundshipmentTraceuploadService.updateById(item);
		}
	 
		
    }
	
	@Transactional
	public void confirmTransTrance(UserInfo user,ShipInboundShipment shipment){
		 try {
			 ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
			 AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
			 Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
			 auth.setMarketPlace(market);
			 String status =iFulfillmentInboundService.confirmTransportDetailsRequest(auth, market, inplan, shipment);
			 shipment.setTransportStatus(status);
			 this.updateById(shipment);
		}catch(BizException e) {
			throw e;
		}catch(Exception e) {
				this.updateById(shipment);
				throw new BizException("物流商信息提交失败");
		}
    }
	
	@Transactional
	public void estimateTransTrance(UserInfo user,ShipInboundShipment shipment){
		 try {
			 ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
			 AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
			 Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
			 auth.setMarketPlace(market);
			 String status = iFulfillmentInboundService.estimateTransportDetailsRequest(auth, market, inplan, shipment);
			 shipment.setTransportStatus(status);
			 this.updateById(shipment);
		}catch(BizException e) {
			throw e;
		}catch(Exception e) {
				this.updateById(shipment);
				throw new BizException("物流商信息提交失败");
		}
    }
	@Override
	@Transactional
	public void saveSelfTransData(UserInfo user, ShipInboundTrans ship, String operate, List<Map<String, Object>> boxinfo,
			String proNumber, Date shipsdate, String carrier) {
		QueryWrapper<ShipInboundTrans> query=new QueryWrapper<ShipInboundTrans>();
		query.eq("shipmentid", ship.getShipmentid());
		List<ShipInboundTrans> list = shipInboundTransMapper.selectList(query);
		ShipInboundShipment shipment = this.getById(ship.getShipmentid());
		if ("LTL".equals(shipment.getTranstyle())) {
			shipment.setProNumber(proNumber);
		}
		if(StrUtil.isBlank(ship.getCompany())) {
			ship.setCompany(null);
		}
		if(StrUtil.isBlank(ship.getChannel())) {
			ship.setChannel(null);
		}
		if (list.size() > 0) {
			ShipInboundTrans item = list.get(0);
			ship.setId(item.getId());
			shipInboundTransService.updateById(ship);
			shipInboundTransHisMapper.insert(new ShipInboundTransHis(ship));
		} else {
			shipInboundTransService.save(ship);
			shipInboundTransHisMapper.insert(new ShipInboundTransHis(ship));
		}
		if(shipment!=null) {
			shipment.setStatus5date(shipsdate);
		}	
		this.updateById(shipment);
		//trans费用分摊
		this.shipInboundItemService.saveShipmentsFee(shipment,user);
		//cost费用更新
		this.shipInboundItemService.saveCostFee(shipment,user);
	}
	
	
	@Transactional
	public String saveMarkshiped(UserInfo user, String shipmentid,String operateType)   {
		ShipInboundShipment shipment = this.getById(shipmentid);
		if (shipment.getStatus() ==5||shipment.getStatus() ==6) {
			throw new BizException("错误：系统检测到货件状态为已发货。如有疑问请联系管理员");
		}
		if (shipment.getStatus() != 4&&!"updateself".equals(operateType)) {
			throw new BizException("错误：系统检测到装箱环节没有处理完成，请确认您当前已经成功下载装箱信息。");
		}
		shipment.setStatus(5);
		if(shipment.getStatus5date()==null) {
			shipment.setStatus5date(new Date());
		}
		shipment.setShipmentstatus(ShipInboundShipment.ShipmentStatus_SHIPPED);
		shipment.setOperator(user.getId());
		shipment.setOpttime(new Date());
		shipment.setShipedDate(new Date());
		ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getInboundplanid());
		List<ShipInboundItem> itemlist = shipInboundItemService.getItemByShipment(shipmentid);
		List<InventoryParameter> list=new ArrayList<InventoryParameter>();
     	AmazonAuthority auth=null;
		Marketplace market=null;
		String groupid=plan.getAmazongroupid();
		String marketplaceid=plan.getMarketplaceid();
		auth=amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
		market=marketplaceService.getById(marketplaceid);
        try {
        	   InboundShipmentList shipmentlist = this.iFulfillmentInboundService.listShipment(auth, market, Arrays.asList(shipmentid));
        	   if(shipmentlist!=null&&shipmentlist.size()>0) {
        		   ShipmentStatus status = shipmentlist.get(0).getShipmentStatus();
        		   if(status!=null) {
        			   if(status.equals(ShipmentStatus.SHIPPED)
        					   ||status.equals(ShipmentStatus.IN_TRANSIT)
        					   ||status.equals(ShipmentStatus.RECEIVING)
        					   ||status.equals(ShipmentStatus.CHECKED_IN)
        					   ||status.equals(ShipmentStatus.CLOSED)) {
        				   operateType="updateself";
            		   }
        		   }
        	   }
        		if(auth!=null && market!=null && plan!=null&&!"updateself".equals(operateType)) {
        			iFulfillmentInboundService.updateInboundShipment(auth, market, plan, shipment);
        		}
        		boolean isupdate = this.baseMapper.updateById(shipment)>0;
        		if (isupdate==true) {
        			shipInboundShipmentRecordService.saveRecord(shipment);
        		}
        		if(auth!=null && market!=null && plan!=null&&!"updateself".equals(operateType)) {
        			saveTransTrance(user,shipment,null,null,null);
        		}
    		try {
    			for (int i = 0; i < itemlist.size(); i++) {
    				ShipInboundItem item = itemlist.get(i);
    				InventoryParameter para = new InventoryParameter();
    				Integer shiped = item.getQuantity();
    				if (item.getQuantityshipped() != null) {
    					shiped =item.getQuantityshipped();
    				}
    				para.setAmount(shiped);
    				para.setShopid(plan.getShopid());
    				para.setNumber(shipmentid);
    				para.setFormid(plan.getId());
    				EnumByInventory statusinv = EnumByInventory.Ready;
    				para.setStatus(statusinv);
    				para.setOperator(shipment.getOperator());
    				para.setOpttime(new Date());
    				para.setFormid(plan.getId());
    				para.setMaterial(item.getMaterialid());
    				para.setSku(item.getMsku());
    				para.setOperator(user.getId());
    				para.setOpttime(new Date());
    				para.setWarehouse(plan.getWarehouseid());
    				para.setFormtype("outstockform");
    				list.add(para);
    			}
    		     erpClientOneFeign.outbound(list);
    		 }catch(FeignException e) {
    			 throw new BizException(BizException.getMessage(e, "扣除库存失败"));
    		 }catch(Exception e) {
    			 throw new BizException("提交失败" +e.getMessage());
    		 }
        }catch (Exception e) {
			// TODO: handle exception
        	//erpClientOneFeign.undoOutbound(list);
            throw new BizException(e.getMessage());
		}
		return "success";
	}


	@Override
	public List<Map<String, Object>> getSelfTransHis(String shopid, String shipmentid) {
		return shipInboundTransHisMapper.getSelfTransHis(shopid,  shipmentid);
	}
	
	@Override
	public int saveTransData(UserInfo user,String shipmentid, String company, String channel,String transtyle) {
		int result=0;
		ShipInboundShipment shipment = this.baseMapper.selectById(shipmentid);
		if(shipment.getStatus()>1) {
			throw new BizException("订单已经审核，请在货件跟踪模块修改物流信息");
		}
		if(StrUtil.isBlank(company)) {
			company=null;
		}
		if(StrUtil.isBlank(channel)) {
			channel=null;
		}
		if(StrUtil.isNotBlank(transtyle)) {
			shipment.setTranstyle(transtyle);
			result+=this.baseMapper.updateById(shipment);
		} 
		QueryWrapper<ShipInboundTrans> queryWrapper=new QueryWrapper<ShipInboundTrans>();
		queryWrapper.eq("shipmentid", shipmentid);
		List<ShipInboundTrans> list = shipInboundTransMapper.selectList(queryWrapper);
		if (list.size() > 0) {
			ShipInboundTrans item = list.get(0);
			item.setChannel(channel);
			item.setCompany(company);
			item.setOperator(user.getId());
			item.setOpttime(new Date());
			shipInboundTransService.updateById(item);
			result+=shipInboundTransHisMapper.insert(new ShipInboundTransHis(item));
		} else {
			ShipInboundTrans ship=new ShipInboundTrans();
			ship.setShipmentid(shipmentid);
			ship.setCompany(company);
			ship.setChannel(channel);
			ship.setOperator(user.getId());
			ship.setOpttime(new Date());
			shipInboundTransService.save(ship);
			result+=shipInboundTransHisMapper.insert(new ShipInboundTransHis(ship));
		}
		return result;
	}

	@Override
	public void ignoreShipment(UserInfo user, String shipmentid) {
		// TODO Auto-generated method stub
		ShipInboundShipment shipment = this.baseMapper.selectById(shipmentid);
		shipment.setIgnorerec(true);
		this.updateById(shipment);
	}

	@Override
	@Cacheable(value = "ShipmentStatusCache")
	public String getShipmentStatusName(String shipmentstatus) {
		// TODO Auto-generated method stub
		return this.baseMapper.getShipmentStatusName(shipmentstatus);
	}

	@Override
	public void refreshTransportDetails(String shipmentid) {
		// TODO Auto-generated method stub
		ShipInboundShipment shipment = this.baseMapper.selectById(shipmentid);
		if(shipment==null||shipment.getTranstyle()==null)return;
		ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
		List<ShipInboundBox> boxlist = this.findShipInboundBoxByShipment(shipmentid);
		boolean hasTrans=false;
		for(ShipInboundBox box:boxlist) {
			if(StrUtil.isNotBlank(box.getTrackingId())) {
				hasTrans=true;
			}
		}
		if(hasTrans==false) {
			return;
		}
		GetTransportDetailsResponse  response = this.iFulfillmentInboundService.getTransportDetails(auth,shipmentid);
	    if(response==null||response.getPayload()==null) {return ;}
		TransportContent trans = response.getPayload().getTransportContent();
		if(trans==null) {    return ;}	
		String status = trans.getTransportResult().getTransportStatus().getValue();
		shipment.setTransportStatus(status);
		this.updateById(shipment);
		if(shipment.getTranstyle().equals("SP")) {
			if(trans.getTransportDetails()!=null&&trans.getTransportDetails().getNonPartneredSmallParcelData()!=null) {
				NonPartneredSmallParcelDataOutput splist=trans.getTransportDetails().getNonPartneredSmallParcelData();
				NonPartneredSmallParcelPackageOutputList spboxlist=null;
				if(splist!=null) {
					  spboxlist = splist.getPackageList();
				}
				if(splist!=null) {
					 for(int i=0;i<boxlist.size();i++) {
						 ShipInboundBox box = boxlist.get(i);
		    			   if(spboxlist!=null&&spboxlist.size()>i){
		    				   NonPartneredSmallParcelPackageOutput spstatus = spboxlist.get(i);
		    				   box.setPackageStatus(spstatus.getPackageStatus().getValue());
		    				   if(spstatus.getTrackingId()!=null&&spstatus.getTrackingId().indexOf("FBA")!=0&&!spstatus.getTrackingId().equals("123456")) {
		    					   box.setTrackingId(spstatus.getTrackingId());
		    				   }else {
		    					   box.setPackageStatus("");
		    				   }
		    			   }else {
		    				   box.setPackageStatus(status);
		    			   }
		    			   this.shipInboundBoxMapper.updateById(box);
		    			}
				 }
			}
		} else {
			if(trans.getTransportDetails()!=null&&trans.getTransportDetails().getNonPartneredLtlData()!=null) {
				NonPartneredLtlDataOutput splist = trans.getTransportDetails().getNonPartneredLtlData();
				for(int i=0;i<boxlist.size();i++) {
					 ShipInboundBox box = boxlist.get(i);
	    			   box.setPackageStatus(status);
	    			   if(splist!=null&&splist.getProNumber()!=null) {
	    				   if(splist.getProNumber().indexOf("FBA")!=0&&!splist.getProNumber().equals("123456")) {
	    					   box.setTrackingId(splist.getProNumber());
	    				   }else {
	    					   box.setPackageStatus("");
	    				   }
	    			   }
	    			   this.shipInboundBoxMapper.updateById(box);
	    	   }
			}
		}
	 }

	@Override
	public List<Map<String, Object>> findInboundItemByInboundplanId(String inboundplanid) {
		// TODO Auto-generated method stub
		return  shipInboundItemMapper.selectByInboundplanid(inboundplanid);
	}
}




