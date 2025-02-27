package com.wimoor.amazon.inboundV2.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxMapper;
import com.wimoor.amazon.inboundV2.pojo.dto.*;
import com.wimoor.amazon.inboundV2.service.*;
import com.wimoor.amazon.util.LockCheckUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentBoxesResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentItemsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Shipment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmenSummaryDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipTransDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundDestinationAddress;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBox;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBoxItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentItem;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.amazon.util.HttpDownloadUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货单")
@RestController
@RequestMapping("/api/v2/shipInboundPlan/shipment")
@SystemControllerLog("发货货件")
@RequiredArgsConstructor
public class ShipInboundPlanPlacementV2Controller {
	
	final IShipInboundPlanService shipInboundPlanV2Service;
	final IShipInboundShipmentService shipInboundShipmentV2Service;
	final IMarketplaceService marketplaceService;
	final IShipAddressService shipAddressService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IShipInboundBoxService shipInboundBoxV2Service;
	final IShipInboundCaseService shipInboundCaseV2Service;
	final IShipInboundShipmentRecordV2Service shipInboundV2ShipmentRecordService;
	final FileUpload fileUpload;
	final ShipInboundShipmentBoxMapper shipInboundShipmentBoxMapper;
	final IShipInboundTransportationService iShipInboundTransportationService;
	final AdminClientOneFeignManager adminClientOneFeign;
	@ApiOperation(value = "不同收货地址数方案列表")
	@PostMapping("/listPlacementOptions")
	@Transactional
	public  Result<?> listPlacementOptionsAction(@RequestBody PlacementDTO dto){
		 try {
			Map<String,Object> maps = shipInboundShipmentV2Service.listPlacementOptions(dto);
			return Result.success(maps); 
		 }catch(FeignException e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }
	}

	@ApiOperation(value = "获取所有本地货件列表")
	@PostMapping("/list")
	public Result<IPage<ShipInboundShipmenSummarytVo>> findByTraceConditionAction(@ApiParam("配货单列表申请")@RequestBody ShipInboundShipmenSummaryDTO dto) {
		    UserInfo user=UserInfoContext.get();
		    dto.setShopid(user.getCompanyid());
		    dto.setAuditstatus(StrUtil.isBlank(dto.getAuditstatus())?null:dto.getAuditstatus());
		    dto.setShipmentid( StrUtil.isBlank(dto.getShipmentid())?null:dto.getShipmentid());
		    dto.setMarketplaceid(StrUtil.isBlank(dto.getMarketplaceid())?null:dto.getMarketplaceid());
		    dto.setGroupid( StrUtil.isBlank(dto.getGroupid())?null:dto.getGroupid());
		    dto.setWarehouseid( StrUtil.isBlank(dto.getWarehouseid())?null:dto.getWarehouseid());
		    dto.setCompany( StrUtil.isBlank(dto.getCompany())?null:dto.getCompany());
		    dto.setChannel(StrUtil.isBlank(dto.getChannel())?null:dto.getChannel());
		    dto.setTranstype( StrUtil.isBlank(dto.getTranstype())?null:dto.getTranstype());
		    dto.setSearchtype(StrUtil.isBlank(dto.getSearchtype())?null:dto.getSearchtype());
		    dto.setSearch( StrUtil.isBlank(dto.getSearch())?null:dto.getSearch());
		    dto.setFromdate( StrUtil.isBlank(dto.getFromdate())?null:dto.getFromdate()+" 00:00:00.000");
		    dto.setEnddate( StrUtil.isBlank(dto.getEnddate())?null:dto.getEnddate()+" 23:59:59.999");
		    dto.setHasexceptionnum(StrUtil.isBlank(dto.getHasexceptionnum())?null:dto.getHasexceptionnum());
		    if(StrUtil.isNotEmpty(dto.getSearch()) && dto.getSearch().indexOf("FBA")==0) {
		    	dto.setSearch(dto.getSearch());
		    	dto.setSearchtype("number");
		    	dto.setMarketplaceid(null);
		    	dto.setGroupid(null);
		    	dto.setWarehouseid(null);
		    	dto.setFromdate(null);
		    	dto.setEnddate(null);
		    }
		    if(StrUtil.isBlank(dto.getFbacenter())) {
		    	dto.setFbacenter(null);
		    }
		    if(StrUtil.isBlank(dto.getCheckinv())) {
		    	dto.setCheckinv(null);
		    }
		    if(dto.getGroupid()==null) {
		    	if(user.getGroups()!=null&&user.getGroups().size()>0) {
		    		dto.setGroupList(user.getGroups());
		    	}
		    }
		    if(StrUtil.isNotEmpty(dto.getFormid())) {
		    	dto.setMarketplaceid(null);
		    	dto.setGroupid(null);
		    	dto.setWarehouseid(null);
		    	dto.setFromdate(null);
		    	dto.setEnddate(null);
		    }
			if(dto.getAuditstatus()!=null&&dto.getAuditstatus().equals("8")){
					dto.setAuditstatus(null);
					dto.setShipmentstatus("CLOSED");
			}
			if(dto.getAuditstatus()!=null&&dto.getAuditstatus().equals("9")){
					dto.setAuditstatus(null);
					dto.setShipmentstatus(null);
					dto.setHasexceptionnum("true");
			}
		    IPage<ShipInboundShipmenSummarytVo> shiplist=shipInboundShipmentV2Service.findByTraceCondition(dto);
	        return Result.success(shiplist);
	    }
	
	@GetMapping("/getPaper")
	public Result<?> getPaperAction(){
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String, String> pkgpaper = shipInboundShipmentV2Service.getPkgPaper("SP");
		map.put("pkgpaper", pkgpaper);
		Map<String, String> pkgpaperltl = shipInboundShipmentV2Service.getPkgPaper("LTL");
		map.put("pkgpaperltl", pkgpaperltl);
		return Result.success(map);
	}
	@GetMapping("/getBaseInfo")
	public Result<Map<String, Object>> getBaseInfoAction(@ApiParam("货件ID")@RequestParam String shipmentid){
		ShipInboundShipment ship =null;
		if(shipmentid.contains("FBA")){
			ship=shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentid).one();
		}else{
			ship=shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getShipmentid,shipmentid).one();
		}
		ShipInboundShipmenSummarytVo data = shipInboundShipmentV2Service.summaryShipmentItemWithoutItem(ship.getShipmentid());
	    BeanUtil.copyProperties(ship, data,"itemList");
	    ShipInboundPlan plan = shipInboundPlanV2Service.getById(ship.getFormid());
	    if(plan!=null) {
	    	data.setMarketplaceid(plan.getMarketplaceid());
	    	data.setTranstyle(ship.getTranstyle());
	    	data.setCountryCode(marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid()).getMarket());
	    }
		List<ShipInboundShipment> shipments = shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getFormid, ship.getFormid()).list();
		List<String> shipmentids=new LinkedList<String>();
		for(ShipInboundShipment shipment:shipments) {
			shipmentids.add(shipment.getShipmentid());
		}
		plan.setShipmentids(shipmentids);
		Map<String, Object> map = getItemPriceAction(ship.getShipmentid());
		SummaryShipmentVo detail = shipInboundShipmentV2Service.showPlanListByPlanid( ship.getShipmentid());
		map.put("detail", detail);
		map.put("plan", plan);
		ship.setShipmentstatus(shipInboundShipmentV2Service.getShipmentStatusName(ship.getShipmentstatus()));
		map.put("shipment", ship);
		map.put("shipmentid", ship.getShipmentid());
		ShipAddress fromAddress = shipAddressService.getById(plan.getSourceAddress());
		ShipInboundDestinationAddress toAddress = shipInboundShipmentV2Service.getToAddress(ship.getDestination());
		map.put("toAddress", toAddress);
		map.put("fromAddress", fromAddress);
		map.put("shipmentAll",data);
		if((plan.getSubmitbox()==null||plan.getSubmitbox()==false)&&ship.getStatus()==3) {
			this.getEditBoxDetialAction(map, ship, plan);
		}else {
			getBoxDetialAction(map,ship,plan);
		}
		getShipAmazonInfoAction(map,ship);
		return Result.success(map);
	}

	@GetMapping("/getBaseInfosByPlan")
	public Result<List<Map<String,Object>>> getBaseInfosByPlanAction(@ApiParam("货件ID")@RequestParam String planid){
		List<ShipInboundShipment> ships = shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getFormid, planid).list();
		List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
		for(ShipInboundShipment ship:ships){
			ShipInboundShipmenSummarytVo data = shipInboundShipmentV2Service.summaryShipmentItemWithoutItem(ship.getShipmentid());
			BeanUtil.copyProperties(ship, data,"itemList");
			ShipInboundPlan plan = shipInboundPlanV2Service.getById(ship.getFormid());
			if(plan!=null) {
				data.setMarketplaceid(plan.getMarketplaceid());
				data.setTranstyle(ship.getTranstyle());
				data.setCountryCode(marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid()).getMarket());
			}
			List<ShipInboundShipment> shipments = shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getFormid, ship.getFormid()).list();
			List<String> shipmentids=new LinkedList<String>();
			for(ShipInboundShipment shipment:shipments) {
				shipmentids.add(shipment.getShipmentid());
			}
			plan.setShipmentids(shipmentids);
			Map<String, Object> map = getItemPriceAction(ship.getShipmentid());
			SummaryShipmentVo detail = shipInboundShipmentV2Service.showPlanListByPlanid( ship.getShipmentid());
			map.put("detail", detail);
			map.put("plan", plan);
			ship.setShipmentstatus(shipInboundShipmentV2Service.getShipmentStatusName(ship.getShipmentstatus()));
			map.put("shipment", ship);
			map.put("shipmentid", ship.getShipmentid());
			ShipAddress fromAddress = shipAddressService.getById(plan.getSourceAddress());
			ShipInboundDestinationAddress toAddress = shipInboundShipmentV2Service.getToAddress(ship.getDestination());
			map.put("toAddress", toAddress);
			map.put("fromAddress", fromAddress);
			map.put("shipmentAll",data);
			if((plan.getSubmitbox()==null||plan.getSubmitbox()==false)&&ship.getStatus()==3) {
				this.getEditBoxDetialAction(map, ship, plan);
			}else {
				getBoxDetialAction(map,ship,plan);
			}
			getShipAmazonInfoAction(map,ship);
			result.add(map);
		}

		return Result.success(result);
	}
	public Result<Map<String, Object>> getShipAmazonInfoAction(Map<String, Object> map,ShipInboundShipment ship) {
		List<Map<String, Object>> cartlist = shipInboundShipmentV2Service.findShipInboundBox(ship.getShipmentid());
		Map<String,Object> transinfo = shipInboundShipmentV2Service.getSelfTransDataView(ship.getShipmentConfirmationId());
		BigDecimal sumweight = new BigDecimal("0");
		BigDecimal volume = new BigDecimal("0");
		BigDecimal sumqty = new BigDecimal("0");
		for (Map<String, Object> entry : cartlist) {
			sumweight = sumweight.add(entry.get("weight")!=null?(BigDecimal) entry.get("weight"):new BigDecimal("0"));
			sumqty = sumqty.add(entry.get("qty")!=null?(BigDecimal) entry.get("qty"):new BigDecimal("0"));
			if (entry.get("length") != null) {
				BigDecimal length = (BigDecimal) entry.get("length");
				BigDecimal width = entry.get("width")!=null?(BigDecimal) entry.get("width"):new BigDecimal("0.0001");
				BigDecimal height =entry.get("height")!= null? (BigDecimal) entry.get("height"):new BigDecimal("0.0001");
				InputDimensions dim = new InputDimensions(length, width, height, InputDimensions.unit_cm);
				BigDecimal baseDim = new BigDecimal("5000");
				if(transinfo!=null && transinfo.get("drate")!=null) {
					baseDim = new BigDecimal(transinfo.get("drate").toString());
				}
				ItemMeasure singlevolume = dim.getDimensionalWeight(baseDim);
				volume = volume.add(singlevolume.getValue().setScale(2, RoundingMode.HALF_UP));
				entry.put("volume", singlevolume.getValue().setScale(2, RoundingMode.HALF_UP));
			}
		}
		map.put("sumqty", sumqty);
		map.put("volume", volume.setScale(2, RoundingMode.HALF_UP));
		map.put("sumweight", sumweight);
		map.put("cart", cartlist);
		if(transinfo!=null) {
			map.put("transinfo", transinfo);
			if(transinfo.get("channel")!=null) {
				map.put("transchannel", shipInboundShipmentV2Service.getTransChannelInfo(transinfo.get("channel").toString()));
			}
		}
		return Result.success(map);
	}
	
	@SuppressWarnings("unchecked")
	public Result<Map<String, Object>> getEditBoxDetialAction(Map<String, Object> map,ShipInboundShipment shipment ,ShipInboundPlan plan)  {
		Map<String, String> pkgpaper = shipInboundShipmentV2Service.getPkgPaper("SP");
		map.put("pkgpaper", pkgpaper);
		Map<String, String> pkgpaperltl = shipInboundShipmentV2Service.getPkgPaper("LTL");
		map.put("pkgpaperltl", pkgpaperltl);
		List<ShipInboundBox> listbox = shipInboundBoxV2Service.findListByShipmentid(plan.getId(),shipment.getShipmentid());
		
		List<Map<String, Object>> itemlist = (List<Map<String, Object>>) map.get("itemlist");
		TreeMap<Integer, Integer> boxsum = new TreeMap<Integer, Integer>();
		int sumtotal = 0;
		for(ShipInboundBox box:listbox) {
			List<ShipInboundCase> listsku = shipInboundCaseV2Service.findShipInboundCaseByBoxid(box.getId());
			box.setCaseListDetail(listsku);
			for (int i = 0; i < itemlist.size(); i++) {
				Map<String, Object> skumap = itemlist.get(i);
				String sku = (String) skumap.get("msku");
				TreeMap<Integer, ShipInboundCase> boxsku = skumap.get("boxsku")!=null?(TreeMap<Integer, ShipInboundCase>) skumap.get("boxsku"):
					new TreeMap<Integer, ShipInboundCase>();
				int skuinbox =skumap.get("skuinbox")!=null?(int) skumap.get("skuinbox"): 0;
				for (int j = 0; j < listsku.size(); j++) {
					ShipInboundCase casebox=listsku.get(j);
					if (sku.equals(casebox.getSku())) {
						skuinbox = skuinbox + casebox.getQuantity();
						boxsku.put(box.getBoxnum(), casebox);
					}
				}
				sumtotal = sumtotal + skuinbox;
				skumap.put("skuinbox", skuinbox);
				skumap.put("boxsku", boxsku);
			}
			for (int j = 0; j < listsku.size(); j++) {
				ShipInboundCase casebox=listsku.get(j);
				if (boxsum.get(box.getBoxnum()) == null) {
					boxsum.put(box.getBoxnum(), casebox.getQuantity());
				} else {
					boxsum.put(box.getBoxnum(),
							boxsum.get(box.getBoxnum()) + casebox.getQuantity());
				}
			}
		
		}
		

		
		BigDecimal totalweight = new BigDecimal("0");
		Map<BigDecimal, Object> dem = new HashMap<BigDecimal, Object>();
		for (int i = 0; i < listbox.size(); i++) {
			ShipInboundBox item = listbox.get(i);
			item.setSumsku(boxsum.get(item.getBoxnum()));
			if (item.getWeight() != null) {
				totalweight = totalweight.add(item.getWeight());
			}

			BigDecimal len = new BigDecimal("0");
			BigDecimal width = new BigDecimal("0");
			BigDecimal height = new BigDecimal("0");
			BigDecimal di = new BigDecimal("0");
			if (item.getLength() != null) {
				len = item.getLength();
			}
			if (item.getWidth() != null) {
				width = item.getWidth();
			}
			if (item.getHeight() != null) {
				height = item.getHeight();
			}
			if (!len.equals(new BigDecimal("0")) && !height.equals(new BigDecimal("0"))
					&& !width.equals(new BigDecimal("0"))) {
				di = len.multiply(width).multiply(height);
			} else if (!len.equals(new BigDecimal("0")) && !width.equals(new BigDecimal("0"))) {
				di = len.multiply(width);
			} else if (!len.equals(new BigDecimal("0")) && !height.equals(new BigDecimal("0"))) {
				di = len.multiply(height);
			} else if (!width.equals(new BigDecimal("0")) && !height.equals(new BigDecimal("0"))) {
				di = width.multiply(width);
			}
			if (dem.get(di) == null) {
				HashMap<String, Object> mymap = new HashMap<String, Object>();
				mymap.put("demitem", item);
				ArrayList<Object> mylist = new ArrayList<Object>();
				mylist.add(item);
				mymap.put("demlist", mylist);
				mymap.put("boxsum", mylist.size());
				dem.put(di, mymap);
			} else {
				HashMap<String, Object> mymap = (HashMap<String, Object>) dem.get(di);
				ArrayList<Object> mylist = (ArrayList<Object>) (mymap).get("demlist");
				mylist.add(item);
				mymap.put("boxsum", mylist.size());
			}
		}
		Integer totalBoxNum=0;
		BigDecimal totalBoxSize=new BigDecimal("0");
		for(Entry<BigDecimal, Object> entry:dem.entrySet()) {
			HashMap<String, Object> value = (HashMap<String, Object>)entry.getValue();
			totalBoxNum=totalBoxNum+  Integer.parseInt(value.get("boxsum").toString());
			totalBoxSize=totalBoxSize.add(entry.getKey().multiply(new BigDecimal(value.get("boxsum").toString())).divide(new BigDecimal("1000000"), 2,RoundingMode.HALF_UP));
		}
		dem.remove(new BigDecimal("0"));
		map.put("dem", dem);
		map.put("demsize", dem.size());
		map.put("totalweight", totalweight);
		map.put("listbox", listbox);
		map.put("sumtotal", sumtotal);
		map.put("arecasesrequired", false);
		map.put("shipment", shipment);
		map.put("totalBoxNum", listbox.size()) ;
		map.put("totalBoxSize", totalBoxSize);
		if(plan!=null && plan.getMarketplaceid()!=null) {
			map.put("market",marketplaceService.getById(plan.getMarketplaceid()).getMarket());
		}
		return Result.success(map);
	}
	
	@SuppressWarnings("unchecked")
	public Result<Map<String, Object>> getBoxDetialAction(Map<String, Object> map,ShipInboundShipment shipment ,ShipInboundPlan plan)  {
		Map<String, String> pkgpaper = shipInboundShipmentV2Service.getPkgPaper("SP");
		map.put("pkgpaper", pkgpaper);
		Map<String, String> pkgpaperltl = shipInboundShipmentV2Service.getPkgPaper("LTL");
		map.put("pkgpaperltl", pkgpaperltl);
		List<ShipInboundShipmentBox> listbox = shipInboundShipmentV2Service.findShipInboundBoxByShipment(shipment.getShipmentid());
		List<ShipInboundShipmentBoxItem> listsku = shipInboundShipmentV2Service.findShipInboundCaseByShipment(shipment.getShipmentid());
		List<Map<String, Object>> itemlist = (List<Map<String, Object>>) map.get("itemlist");
		TreeMap<Integer, Integer> boxsum = new TreeMap<Integer, Integer>();
		int sumtotal = 0;
		Map<String,Integer> indexBoxMap=new HashMap<String,Integer>();
		for(int i = 0; i < listbox.size(); i++){
			indexBoxMap.put(listbox.get(i).getId(), i+1);
		}
		for (int i = 0; i < itemlist.size(); i++) {
			Map<String, Object> skumap = itemlist.get(i);
			String sku = (String) skumap.get("sellersku");
			TreeMap<Integer, ShipInboundShipmentBoxItem> boxsku = new TreeMap<Integer, ShipInboundShipmentBoxItem>();
			int skuinbox = 0;
			for (int j = 0; j < listsku.size(); j++) {
				if (sku.equals(listsku.get(j).getSku())) {
					skuinbox = skuinbox + listsku.get(j).getQuantity();
					String boxId = listsku.get(j).getBoxid();
					boxsku.put(indexBoxMap.get(boxId), listsku.get(j));
				}
			}
			sumtotal = sumtotal + skuinbox;
			skumap.put("skuinbox", skuinbox);
			skumap.put("boxsku", boxsku);
		}

		for (int j = 0; j < listsku.size(); j++) {
			if (boxsum.get(listsku.get(j).getQuantity()) == null) {
				boxsum.put(listsku.get(j).getQuantity(), listsku.get(j).getQuantity());
			} else {
				boxsum.put(listsku.get(j).getQuantity(),
						boxsum.get(listsku.get(j).getQuantity()) + listsku.get(j).getQuantity());
			}
		}
	
		BigDecimal totalweight = new BigDecimal("0");
		Map<BigDecimal, Object> dem = new HashMap<BigDecimal, Object>();
		for (int i = 0; i < listbox.size(); i++) {
			ShipInboundShipmentBox item = listbox.get(i);
			item.setSumsku(boxsum.get(item.getBoxnum()));
			if (item.getWeight() != null) {
				totalweight = totalweight.add(item.getWeight());
			}

			BigDecimal len = new BigDecimal("0");
			BigDecimal width = new BigDecimal("0");
			BigDecimal height = new BigDecimal("0");
			BigDecimal di = new BigDecimal("0");
			if (item.getLength() != null) {
				len = item.getLength();
			}
			if (item.getWidth() != null) {
				width = item.getWidth();
			}
			if (item.getHeight() != null) {
				height = item.getHeight();
			}
			if (!len.equals(new BigDecimal("0")) && !height.equals(new BigDecimal("0"))
					&& !width.equals(new BigDecimal("0"))) {
				di = len.multiply(width).multiply(height);
			} else if (!len.equals(new BigDecimal("0")) && !width.equals(new BigDecimal("0"))) {
				di = len.multiply(width);
			} else if (!len.equals(new BigDecimal("0")) && !height.equals(new BigDecimal("0"))) {
				di = len.multiply(height);
			} else if (!width.equals(new BigDecimal("0")) && !height.equals(new BigDecimal("0"))) {
				di = width.multiply(width);
			}
			if (dem.get(di) == null) {
				HashMap<String, Object> mymap = new HashMap<String, Object>();
				mymap.put("demitem", item);
				ArrayList<Object> mylist = new ArrayList<Object>();
				mylist.add(item);
				mymap.put("demlist", mylist);
				mymap.put("boxsum", mylist.size());
				dem.put(di, mymap);
			} else {
				HashMap<String, Object> mymap = (HashMap<String, Object>) dem.get(di);
				ArrayList<Object> mylist = (ArrayList<Object>) (mymap).get("demlist");
				mylist.add(item);
				mymap.put("boxsum", mylist.size());
			}
		}
		Integer totalBoxNum=0;
		BigDecimal totalBoxSize=new BigDecimal("0");
		for(Entry<BigDecimal, Object> entry:dem.entrySet()) {
			HashMap<String, Object> value = (HashMap<String, Object>)entry.getValue();
			totalBoxNum=totalBoxNum+  Integer.parseInt(value.get("boxsum").toString());
			totalBoxSize=totalBoxSize.add(entry.getKey().multiply(new BigDecimal(value.get("boxsum").toString())).divide(new BigDecimal("1000000"), 2,RoundingMode.HALF_UP));
		}
		dem.remove(new BigDecimal("0"));
		map.put("dem", dem);
		map.put("demsize", dem.size());
		map.put("totalweight", totalweight);
		map.put("listbox", listbox);
		map.put("sumtotal", sumtotal);
		map.put("arecasesrequired", false);
		map.put("shipment", shipment);
		map.put("totalBoxNum", listbox.size()) ;
		map.put("totalBoxSize", totalBoxSize);
		if(plan!=null && plan.getMarketplaceid()!=null) {
			map.put("market",marketplaceService.getById(plan.getMarketplaceid()).getMarket());
		}
		return Result.success(map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getItemPriceAction(String shipmentid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> itemlist = shipInboundShipmentV2Service.findInboundItemByShipmentId(shipmentid);
		Integer sumquantity = 0;
		Integer sumquantityshiped = 0;
		Integer sumquantityReceived = 0;
		boolean sumistotal=false;
		List<String> mskulist=new LinkedList<String>();
		for (int i = 0; i < itemlist.size(); i++) {
			Map<String, Object> tempmap = itemlist.get(i);
			mskulist.add(tempmap.get("msku").toString());
		}
		Result<Map<String, Object>> mskuInfoResult = erpClientOneFeign.findMaterialMapBySku(mskulist);
		Map<String,Object> mskuInfo=null;
		if(Result.isSuccess(mskuInfoResult)&&mskuInfoResult.getData()!=null) {
			mskuInfo=mskuInfoResult.getData();
		}
		//erpClientOneFeign
		for (int i = 0; i < itemlist.size(); i++) {
			Map<String, Object> tempmap = itemlist.get(i);
			Integer quantity = 0;
			String msku=tempmap.get("msku").toString();
			if(mskuInfo!=null&&mskuInfo.get(msku)!=null) {
				Map<String, Object> mskuInfoItem = (Map<String,Object>)mskuInfo.get(msku);
				if(mskuInfoItem.get("image")!=null) {
					tempmap.put("image",fileUpload.getPictureImage(mskuInfoItem.get("image")) );
				}
			}
			if (tempmap.get("Quantity") != null) {
				quantity = Integer.parseInt(tempmap.get("Quantity").toString());
			}
			Integer quantityShipped = 0;
			if (tempmap.get("QuantityShipped") != null) {
				quantityShipped = Integer.parseInt(tempmap.get("QuantityShipped").toString());
			}
			int quantityReceived = 0;
			if (tempmap.get("QuantityReceived") != null) {
				quantityReceived = Integer.parseInt(tempmap.get("QuantityReceived").toString());
			}
			if (tempmap.get("QuantityInCase") != null) {
				int quantityInCase = Integer.parseInt(tempmap.get("QuantityInCase").toString());
				if(quantityInCase!=0) {
					tempmap.put("caseamount", quantityShipped/quantityInCase);
					tempmap.put("skuinbox", quantityShipped);
					sumistotal=true;
				}else {
					tempmap.put("caseamount", 0);
					tempmap.put("skuinbox", 0);
				}
			}else {
				tempmap.put("caseamount", 0);
				tempmap.put("skuinbox", 0);
				tempmap.put("QuantityInCase", 0);
			}
			sumquantityReceived += quantityReceived;
			sumquantity += quantity;
			sumquantityshiped += quantityShipped;
		}
		map.put("skunum", itemlist.size());
		map.put("sumquantity", sumquantity);
		map.put("sumquantityshiped", sumquantityshiped);
		map.put("sumquantityReceived", sumquantityReceived);
		map.put("itemlist", itemlist);
		if(sumistotal) {
			map.put("sumtotal", sumquantityshiped);
		} 
		return map;
	}
	
	
	@ApiOperation(value = "确认箱子分组策略")
	@GetMapping("/confirmPlacementOption")
	@Transactional
	public  Result<ShipInboundOperation> confirmPlacementOptionAction(String planid,String placementOptionId){
		 try {
			 UserInfo user=UserInfoContext.get();
			 return Result.success(shipInboundShipmentV2Service.confirmPlacementOption(user,planid,placementOptionId));
		 }catch(FeignException e) {
			 throw new BizException("确认箱子分组策略失败" +e.getMessage());
		 }
	}

	@ApiOperation(value = "生成不同收货地址数对应方案")
	@GetMapping("/generatePlacementOptions")
	@Transactional
	public  Result<ShipInboundOperation> generatePlacementOptionsAction(String formid){
		 try {
			return Result.success(shipInboundShipmentV2Service.generatePlacementOptions(formid));
		 }catch(FeignException e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }
	}
	
	
	@ApiOperation(value = "生成不同收货地址数对应方案")
	@GetMapping("/generateDeliveryWindowOptions/ignoreRepeat")
	@Transactional
	public  Result<ShipInboundOperation> generateDeliveryWindowOptionsAction(String formid,String shipmentId){
		 try {
			return Result.success(shipInboundShipmentV2Service.generateDeliveryWindowOptions(formid,shipmentId));
		 }catch(FeignException e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "不同收货地址数方案列表")
	@PostMapping("/listDeliveryWindowOptions/ignoreRepeat")
	@Transactional
	public  Result<?> listDeliveryWindowOptionsAction(@RequestBody DeliveryWindowDTO dto){
		 try {
			return Result.success(shipInboundShipmentV2Service.listDeliveryWindowOptions(dto)); 
		 }catch(FeignException e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "不同收货地址数方案列表")
	@PostMapping("/confirmDeliveryWindowOptions/ignoreRepeat")
	@Transactional
	public  Result<?> confirmDeliveryWindowOptionsAction(@RequestBody DeliveryWindowDTO dto){
		 try {
			return Result.success(shipInboundShipmentV2Service.confirmDeliveryWindowOptions(dto)); 
		 }catch(FeignException e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成方案失败" +e.getMessage());
		 }
	}
	
	
	@ApiOperation(value = "货件的完整详细信息")
	@GetMapping("/getShipment/ignoreRepeat")
	public  Result<?> getShipmentAction(String planid,String shipmentId){
		 try {
			 AmzShipmentDTO shipment = shipInboundShipmentV2Service.getShipment(planid,shipmentId);
			 return Result.success(shipment);
		 }catch(FeignException e) {
			 throw new BizException("获取货件的完整详细信息失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("获取货件的完整详细信息失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "确认发货")
	@PostMapping("/shippedInboundPlan/{id}")
	@SystemControllerLog("确认发货")    
	@Transactional
	public  Result<String> shippedInboundPlan(@PathVariable("id")  String id,@RequestBody List<ShipmentDTO> dto ){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundPlan plan=shipInboundPlanV2Service.getById(id);
			try {
				 List<String> shipmentIds=new ArrayList<String>();
				 if(dto!=null&& !dto.isEmpty()){
					 for (ShipmentDTO item:dto){
						 shipmentIds.add(item.getShipmentId());
					 }
				 }
				 String placementOptionId=shipInboundShipmentV2Service.saveShipment(plan,shipmentIds);
				 if(plan.getAuditstatus()==4&&shipInboundBoxV2Service.hasSubmitPackage(plan)) {
					 plan.setAuditstatus(6);
				 }else  if(plan.getAuditstatus()==4) {
					 plan.setAuditstatus(5);
				 }else {
					 throw new BizException("计划状态不匹配，请操作其他步骤后发货");
				 }
				if(dto!=null&& !dto.isEmpty()){
					for (ShipmentDTO item:dto){
						ShipInboundShipment shipment = shipInboundShipmentV2Service.getById(item.getShipmentId());
						if(shipment!=null){
							ShipInboundTrans ship = new ShipInboundTrans();
							ship.setCompany(item.getCompanyid());
							if(StrUtil.isNotBlank(item.getTranstype())) {
								ship.setTranstype(new BigInteger(item.getTranstype()));
							}
							ship.setChannel(item.getChannelid());
							ship.setShipmentid(shipment.getShipmentConfirmationId());
							ship.setOperator(user.getId());
							ship.setOpttime(new Date());
						    iShipInboundTransportationService.saveSelfTransData(user, ship,shipment,null);
						}
					}
				}
                 if(StrUtil.isNotBlank(placementOptionId)){
					 plan.setPlacementOptionId(placementOptionId);
				 }
				 plan.setOpttime(new Date());
				 plan.setOperator(user.getId());
				 shipInboundPlanV2Service.updateById(plan);
				 shipInboundV2ShipmentRecordService.saveRecord(plan);
				 return Result.success(plan.getId());
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	
	@ApiOperation(value = "完成装箱")
	@PostMapping("/doneShipmentBox/{formid}")
	@SystemControllerLog("完成装箱")    
	@Transactional
	public  Result<String> doneShipmentBox(@PathVariable("formid")String formid,@RequestBody List<String> shipmentids){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundPlan inplan=shipInboundPlanV2Service.getById(formid);
			try {
				 shipInboundShipmentV2Service.saveShipment(inplan,shipmentids);
				 for(String shipmentid:shipmentids) {
					 ShipInboundShipment shipment = shipInboundShipmentV2Service.getById(shipmentid);
					 if(shipment!=null&&shipment.getStatus()==3) {
							shipment.setStatus(4);
							shipment.setOperator(user.getId());
							shipment.setOpttime(new Date());
							shipInboundShipmentV2Service.updateById(shipment);
							shipInboundV2ShipmentRecordService.saveRecord(shipment);
						}
				 }
				if(inplan.getAuditstatus()==5){
					inplan.setAuditstatus(6);
					inplan.setOperator(user.getId());
					inplan.setOpttime(new Date());
					shipInboundPlanV2Service.updateById(inplan);
					shipInboundV2ShipmentRecordService.saveRecord(inplan);
				}
				 return Result.success(inplan.getId());
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	
	@ApiOperation(value = "货件的完整详细信息")
	@PostMapping("/getShipmentItems/ignoreRepeat")
	public  Result<?> getShipmentItemsAction(@RequestBody ShipmentItemsDTO dto){
		 try {
			 ListShipmentItemsResponse shipmentitems = shipInboundShipmentV2Service.getshipmentItems(dto);
			 return Result.success(shipmentitems);
		 }catch(FeignException e) {
			 throw new BizException("获取货件的SKU列表失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("获取货件的SKU列表失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "货件的完整详细信息")
	@PostMapping("/listShipmentBoxes/ignoreRepeat")
	public  Result<?> listShipmentBoxesAction(@RequestBody ShipmentItemsDTO dto){
		 try {
			   ListShipmentBoxesResponse shipmentitems = shipInboundShipmentV2Service.listShipmentBoxes(dto);
			 return Result.success(shipmentitems);
		 }catch(FeignException e) {
			 throw new BizException("获取货件的箱子信息失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("获取货件的箱子信息失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "获取URL下载箱子标签")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/getPkgLabelUrl")
	public void getPkgLabelUrlAction(
			@RequestParam("shipmentid") String shipmentid
			,@RequestParam("pagetype") String pagetype
			,@RequestParam("labeltype") String labeltype,
			@RequestParam("pannum") String pannum,
			HttpServletResponse response
			 ) {
		    UserInfo user=UserInfoContext.get();
		    String data = shipInboundShipmentV2Service.getLabelUrl(user,shipmentid,pagetype,labeltype,pannum);
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=shipment.pdf");// 设置文件名
			try {
				ServletOutputStream fOut = response.getOutputStream();
				HttpDownloadUtil.download(data, fOut);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@GetMapping("/getShipCart")
	public Result<Map<String, Object>> getShipCartAction(String shipmentid,String channelid) {
		Map<String, Object> map = new HashMap<String,Object>();
		ShipInboundShipment ship = shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getShipmentid,shipmentid).one();
		List<Map<String, Object>> cartlist = shipInboundShipmentV2Service.findShipInboundBox(ship.getShipmentid());
		BigDecimal sumweight = new BigDecimal("0");
		BigDecimal volume = new BigDecimal("0");
		BigDecimal sumqty = new BigDecimal("0");
		Map<String,Object> transinfo = shipInboundShipmentV2Service.getSelfTransDataView(ship.getShipmentConfirmationId());
		for (Map<String, Object> entry : cartlist) {
			sumweight = sumweight.add((BigDecimal) entry.get("weight"));
			sumqty = sumqty.add((BigDecimal) entry.get("qty"));
			if (entry.get("length") != null) {
				BigDecimal length = (BigDecimal) entry.get("length");
				BigDecimal width = (BigDecimal) entry.get("width");
				BigDecimal height = (BigDecimal) entry.get("height");
				InputDimensions dim = new InputDimensions(length, width, height, InputDimensions.unit_cm);
				BigDecimal baseDim = new BigDecimal("5000");
				if(transinfo!=null && transinfo.get("drate")!=null) {
					baseDim = new BigDecimal(transinfo.get("drate").toString());
				}
				if(StrUtil.isNotBlank(channelid)){
					transinfo = erpClientOneFeign.getShipTransChannelDetial(channelid);
					 if(transinfo!=null){
						 baseDim = new BigDecimal(transinfo.get("drate").toString());
					 }
				}
				ItemMeasure singlevolume = dim.getDimensionalWeight(baseDim);
				volume = volume.add(singlevolume.getValue().setScale(2, RoundingMode.HALF_UP));
				entry.put("volume", singlevolume.getValue().setScale(2, RoundingMode.HALF_UP));
			}
		}
		map.put("cart", cartlist);
		map.put("shipment", ship);
		return Result.success(map);
	}
	
	 @ApiOperation(value = "保存物流跟踪信息")
	 @PostMapping("/saveTransTrace")
	 @SystemControllerLog("保存物流跟踪编号信息")
	 @Transactional
	 public	Result<ShipInboundOperation> saveTransTraceAction(@ApiParam("货件物流信息") @RequestBody ShipTransDTO dto){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundShipment  ship= shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getShipmentid,dto.getShipmentid()).one();
		 if(dto.getActiontype()!=null){
			 ship.setTransportStatus(dto.getActiontype());
			 shipInboundShipmentV2Service.updateById(ship);
		 }
		 return Result.success(shipInboundShipmentV2Service.saveTransTrance(user,ship, dto.getBoxinfo()));
	 }

	@ApiOperation(value = "保存物流跟踪信息")
	@PostMapping("/feedbackTransTrace")
	@SystemControllerLog("保存物流跟踪编号信息")
	@Transactional
	public	Result<ShipInboundShipment> feedbackTransTraceAction(@ApiParam("货件物流信息") @RequestBody ShipTransDTO dto){
		UserInfo user=UserInfoContext.get();
		Boolean edit=false;
		ShipInboundShipment ship = shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getShipmentid,dto.getShipmentid()).one();
		if(ship.getStatus()<6) {
			ship.setStatus(6);
			ship.setOperator(user.getId());
			ship.setOpttime(new Date());
			edit=true;
			shipInboundV2ShipmentRecordService.saveRecord(ship);
		}
		if(ship.getTransportStatus()!=null){
			edit=true;
			if(ship.getTransportStatus().equals("Shipped")){
				ship.setTransportStatus("Waiting");
			}
			if(ship.getTransportStatus().equals("Tracking")){
				ship.setTransportStatus("WORKING");
			}
		}
		if(edit){
			shipInboundShipmentV2Service.updateById(ship);
		}
		return Result.success(ship);
	}
	 
		@PostMapping(value = "updateFeeByShipment")
		@Transactional
		public Result<?> updateFeeByShipment(@RequestBody List<ShipInboundShipmentItem> list)   {
			UserInfo user=UserInfoContext.get();
			for(ShipInboundShipmentItem item:list) {
				item.setOperator(user.getId());
				item.setOpttime(new Date());
			}
			shipInboundShipmentV2Service.updateFeeByShipment(list);
			return Result.success();
		}
		
		
		@GetMapping(value = "updateRemarkByShipment")
		@Transactional
		public Result<?> updateRemarkByShipmentAction(String shipmentid,String remark)   {
			 ShipInboundShipment shipment = shipInboundShipmentV2Service.getById(shipmentid);
			 shipment.setRemark(remark);
			 shipInboundShipmentV2Service.updateById(shipment);
			return Result.success();
		}
		
	@GetMapping("/downExcelBoxDetail")
	public void downExcelBoxDetailAction(String shipmentid,
			HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=boxDetail.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			UserInfo user=UserInfoContext.get();
			ShipInboundShipment shipment=null;
			if(shipmentid.contains("FBA")){
				shipment=shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentid).one();
			}else{
				shipment=shipInboundShipmentV2Service.getById(shipmentid);
			}
			shipInboundShipmentV2Service.setExcelBoxDetail(user, workbook, shipment.getShipmentid());
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/downExcelBox")
	public void downExcelBoxAction(String shipmentid,  HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=boxDetail.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			UserInfo user=UserInfoContext.get();
			ShipInboundShipment shipment=null;
			if(shipmentid.contains("FBA")){
				shipment=shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentid).one();
			}else{
				shipment=shipInboundShipmentV2Service.getById(shipmentid);
			}
			shipInboundShipmentV2Service.setExcelBox(user, workbook, shipment.getShipmentid());
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "货件")
	@GetMapping("/saveshipment")
	@Transactional
	public  Result<String> saveshipmentAction(String shipid){
		UserInfo user=UserInfoContext.get();
		ShipInboundShipment shipment = shipInboundShipmentV2Service.getById(shipid);
		ShipInboundPlan inplan=shipInboundPlanV2Service.getById(shipment.getFormid());
		try {
			shipInboundShipmentV2Service.saveShipment(inplan, Arrays.asList(shipid));
			return Result.success(inplan.getId());
		}catch(FeignException e) {
			throw new BizException("提交失败" +e.getMessage());
		}
	}


	@ApiOperation(value = "货件")
	@PostMapping("/saveshipments/{formid}")
	@Transactional
	public  Result<String> saveshipmentsAction(@PathVariable String formid,@RequestBody List<String> shipmentids){
		UserInfo user=UserInfoContext.get();
		ShipInboundPlan inplan=shipInboundPlanV2Service.getById(formid);
		try {
			shipInboundShipmentV2Service.saveShipment(inplan, shipmentids);
			return Result.success(inplan.getId());
		}catch(FeignException e) {
			throw new BizException("提交失败" +e.getMessage());
		}
	}


	@ApiOperation(value = "转入报价")
	@PostMapping("/toQuote/{token}")
	@SystemControllerLog("转入报价")
	@Transactional
	public  Result<String> toQuoteAction(@PathVariable String token,@RequestBody List<String> shipmentids){
		UserInfo user=UserInfoContext.get();
		Result<UserInfo> info = adminClientOneFeign.getUserByUserId(user.getId());
		for(String shipmentId:shipmentids) {
			shipInboundShipmentV2Service.toQuote(info.getData(),token,shipmentId);
		}
		return Result.success("success");
	}

}
