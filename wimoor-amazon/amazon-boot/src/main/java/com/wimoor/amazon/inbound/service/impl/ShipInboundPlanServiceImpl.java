package com.wimoor.amazon.inbound.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.amazon.spapi.model.fulfillmentinbound.Address;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;
import com.wimoor.amazon.common.pojo.vo.ChartLine;
import com.wimoor.amazon.common.pojo.vo.ChartMarkpoint;
import com.wimoor.amazon.common.service.IDaysalesFormulaService;
import com.wimoor.amazon.common.service.impl.DaysalesFormulaServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundPlanMapper;
import com.wimoor.amazon.inbound.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.vo.AmazonShipmentVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInPresaleService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.amazon.util.UUIDUtil;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service("shipInboundPlanService")
@RequiredArgsConstructor
public class ShipInboundPlanServiceImpl extends ServiceImpl<ShipInboundPlanMapper,ShipInboundPlan> implements IShipInboundPlanService { 
	final IAmazonGroupService iAmazonGroupService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IMarketplaceService marketplaceService;
	@Autowired
	@Lazy
    IShipInboundShipmentService shipInboundShipmentService;
	@Autowired
	@Lazy
	IShipInboundItemService iShipInboundItemService;
	
    final IShipInboundShipmentRecordService shipInboundShipmentRecordService;
	final ISerialNumService serialNumService;
	final IShipAddressService shipAddressService;
	final IShipAddressToService shipAddressToService;
	final ProductInfoMapper productInfoMapper;
	final IProductInPresaleService productInPresaleService;
	final IDaysalesFormulaService daysalesFormulaService;
	final ErpClientOneFeignManager erpClientOneFeign;
	public void saveShipInboundPlan(ShipInboundPlan inplan) {
		// TODO Auto-generated method stub
	       AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
	       Marketplace market = marketplaceService.selectByPKey(inplan.getMarketplaceid());
	       IFulfillmentInboundService iFulfillmentInboundService =SpringUtil.getBean(FulfillmentInboundServiceImpl.class);
	       List<ShipInboundShipment> shipmentList = iFulfillmentInboundService.createInboundShipmentPlan(auth,market,inplan);
	       if(shipmentList!=null&&shipmentList.size()>0) {
	    	   for(ShipInboundShipment localShipment:shipmentList) {
		    	   shipInboundShipmentService.save(localShipment);
				   shipInboundShipmentRecordService.saveRecord(localShipment);
		       }
	    	   save(inplan);
	       }else {
	    	   throw new BizException("计划提交失败,请重新尝试或联系客服");
	       }
	       
			
		  
	}
	 
	
	
	List<AmazonShipmentVo> listShipment(String groupid,String marketplaceid,String begin,String end){
	    AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	    Marketplace market = marketplaceService.selectByPKey(marketplaceid);
		AmazonGroup group = iAmazonGroupService.getById(groupid);
	    IFulfillmentInboundService iFulfillmentInboundService =SpringUtil.getBean(FulfillmentInboundServiceImpl.class);
		InboundShipmentList list = iFulfillmentInboundService.listShipment(auth, market, begin, end);
		List<AmazonShipmentVo> result=new LinkedList<AmazonShipmentVo>();
		for(InboundShipmentInfo item:list) {
			AmazonShipmentVo resultitem=new AmazonShipmentVo();
			resultitem.setInfo(item);
			resultitem.setGroupid(groupid);
			resultitem.setGroupname(group.getName());
			resultitem.setMarketname(market.getName());
			resultitem.setMarketplaceid(market.getMarketplaceid());
			result.add(resultitem);
		}
		return result;
	}

 
    
    private ShipInboundShipment getShipment(AmazonAuthority auth, Marketplace market, InboundShipmentInfo item){
                Date nowdate = new Date();
                if(item==null) {
                	return null;
                }
                ShipInboundShipment shipment = new ShipInboundShipment();
    			shipment.setShipmentid(item.getShipmentId());
    			String shipmentstatus="SHIPPED";
    			if(item.getShipmentStatus()!=null) {
    				shipmentstatus=item.getShipmentStatus().getValue();
    			}
    			shipment.setCreatedate(nowdate);
    			shipment.setCreator("1");
    			shipment.setCurrency(market.getCurrency());
    			shipment.setDestinationfulfillmentcenterid(item.getDestinationFulfillmentCenterId());
    			shipment.setLabelpreptype(item.getLabelPrepType().getValue());
    			if(item.getEstimatedBoxContentsFee()!=null) {
    				if(item.getEstimatedBoxContentsFee().getFeePerUnit()!=null) {
    					shipment.setFeeperunit(item.getEstimatedBoxContentsFee().getFeePerUnit().getValue());
    				}
    				if(item.getEstimatedBoxContentsFee().getTotalFee()!=null) {
    					shipment.setTotalfee(item.getEstimatedBoxContentsFee().getTotalFee().getValue());
    				}
    				shipment.setTotalunits(item.getEstimatedBoxContentsFee().getTotalUnits());
    			}
    			shipment.setIntendedBoxContentsSource("NONE");
    			shipment.setName(item.getShipmentName());
    			shipment.setOperator("1");
    			shipment.setOpttime(nowdate);
    			shipment.setShipmentstatus(shipmentstatus);
    			shipment.setShipedDate(nowdate);
    			shipment.setStatus1date(nowdate);
    			shipment.setStatus2date(nowdate);
    		    shipment.setSyncInv(1);
    			if ("SHIPPED".equalsIgnoreCase(shipmentstatus)) {
    				shipment.setStatus5date(nowdate);
    				shipment.setStatus(5);
    			} else if("CLOSED".equalsIgnoreCase(shipmentstatus)) {
    				shipment.setStatus(6);
    				shipment.setStatus5date(nowdate);
    				shipment.setStatus6date(new Date());
    			} else if("WORKING".equalsIgnoreCase(shipmentstatus)){
    				shipment.setStatus5date(nowdate);
    				shipment.setStatus(4);
    			} else {
    				shipment.setStatus5date(nowdate);
    				shipment.setStatus(5);
    			}
		return shipment;
    }
    
   private ShipAddress getAddress(AmazonAuthority auth,InboundShipmentInfo item){
    	Address address = item.getShipFromAddress();
		String shipaddrssid =  UUIDUtil.getUUIDshort();
		ShipAddress shipAddress=new ShipAddress();
		shipAddress.setId(shipaddrssid);
		shipAddress.setAddressline1(address.getAddressLine1());
		shipAddress.setAddressline2(address.getAddressLine2());
		shipAddress.setCity(address.getCity());
		shipAddress.setCountrycode(address.getCountryCode());
		shipAddress.setDistrictorcounty(address.getDistrictOrCounty());
		shipAddress.setGroupid(auth.getGroupid());
		shipAddress.setIsdefault(false);
		shipAddress.setIsfrom(true);
		shipAddress.setName(address.getName());
		shipAddress.setOperator("1");
		shipAddress.setOpttime(new Date());
		shipAddress.setPostalcode(address.getPostalCode());
		shipAddress.setShopid(auth.getShopId());
		shipAddress.setStateorprovincecode(address.getStateOrProvinceCode());
		return shipAddress;
    }
   
   private ShipInboundPlan createPlan(AmazonAuthority auth, Marketplace market, InboundShipmentInfo item,String warehouseid,Integer skunum) {
	    Date nowdate = new Date();
		ShipInboundPlan inplan = new ShipInboundPlan();
		String marketplaceid = market.getMarketplaceid();
		String groupid = auth.getGroupid();
		String shipname = item.getShipmentName();
		String inboundplanid = UUIDUtil.getUUIDshort();
		inplan.setId(inboundplanid);
		inplan.setAmazongroupid(groupid);
		inplan.setArecasesrequired(item.isAreCasesRequired());
		inplan.setAuditstatus(3);
		inplan.setCreatedate(nowdate);
		inplan.setOpttime(nowdate);
		inplan.setCreator("1");
		inplan.setLabelpreptype(item.getLabelPrepType().getValue());
		 
		Map<String, Object> tocountry = shipInboundShipmentService.findToCountry(item.getDestinationFulfillmentCenterId(),null);
		if(tocountry!=null&&tocountry.get("marketplaceId")!=null) {
			marketplaceid=tocountry.get("marketplaceId").toString()	;
		}
		inplan.setMarketplaceid(marketplaceid);
		inplan.setName(shipname);
		String number = null;
		try {
			number = serialNumService.readSerialNumber(auth.getShopId(), "SF");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inplan.setNumber(number);
		inplan.setOperator("1");
		inplan.setShopid(auth.getShopId());
		if(StrUtil.isNotEmpty(warehouseid)) {
			inplan.setWarehouseid(warehouseid);
		}
		inplan.setSkunum(skunum);
		if(item.getShipFromAddress()!=null&&item.getShipFromAddress().getName()!=null) {
			QueryWrapper<ShipAddress> query=new QueryWrapper<ShipAddress>();
			query.eq("groupid",auth.getGroupid());
			query.eq("name",item.getShipFromAddress().getName());
			List<ShipAddress> shipAddresslist = shipAddressService.list(query);
			if(shipAddresslist!=null && shipAddresslist.size()>0) {
				inplan.setShipfromaddressid(shipAddresslist.get(0).getId());
			}else {
				//插入shipaddress表   并给inbound表addressid
				ShipAddress shipAddress=getAddress(auth,item);
				boolean saveShipResult=shipAddressService.save(shipAddress);
				if(saveShipResult) {
					inplan.setShipfromaddressid(shipAddress.getId());
				}
			}
		}
		this.save(inplan);
		return inplan;
   }
	@Override
	public ShipInboundShipment createShipment(AmazonAuthority auth, Marketplace market, InboundShipmentInfo item,String warehouseid,Integer skunum) {
		// TODO Auto-generated method stub
			ShipInboundShipment shipmentold = shipInboundShipmentService.getById(item.getShipmentId());
			if(shipmentold==null) {
				ShipInboundPlan inplan=createPlan( auth,  market,  item, warehouseid, skunum);
				ShipInboundShipment shipment = getShipment(auth,market,item);
				shipment.setInboundplanid(inplan.getId());
				shipInboundShipmentService.save(shipment);
				shipInboundShipmentRecordService.saveRecord(shipment);
				shipment.setInboundplan(inplan);
				return shipment;
			}else {
				 ShipInboundPlan plan = this.getById(shipmentold.getInboundplanid());
				 if(plan!=null) {
					 if(StrUtil.isNotEmpty(warehouseid)) {
						 plan.setWarehouseid(warehouseid);
					 }
				 }else {
					 plan=createPlan( auth,  market,  item, warehouseid, skunum);
					 shipmentold.setInboundplan(plan);
					 shipmentold.setInboundplanid(plan.getId());
				 }
				 if(item.getShipmentStatus()!=null) {
					 shipmentold.setShipmentstatus(item.getShipmentStatus().getValue());
				 }
				 shipInboundShipmentService.updateById(shipmentold);
				 shipmentold.setInboundplan(plan);
				 return shipmentold;
			}
	} 
	
	@Override
	public ShipInboundShipment convertShipmentPojo(AmazonAuthority auth, Marketplace market, InboundShipmentInfo item,String warehouseid,Integer skunum) {
		// TODO Auto-generated method stub
				Date nowdate = new Date();
				ShipInboundPlan inplan = new ShipInboundPlan();
				String marketplaceid = market.getMarketplaceid();
				String groupid = auth.getGroupid();
				String shipname = item.getShipmentName();
				String inboundplanid = UUIDUtil.getUUIDshort();
				inplan.setId(inboundplanid);
				inplan.setAmazongroupid(groupid);
				inplan.setArecasesrequired(item.isAreCasesRequired());
				inplan.setAuditstatus(3);
				inplan.setCreatedate(nowdate);
				inplan.setOpttime(nowdate);
				inplan.setCreator("1");
				inplan.setLabelpreptype(item.getLabelPrepType().getValue());
				 
				Map<String, Object> tocountry = shipInboundShipmentService.findToCountry(item.getDestinationFulfillmentCenterId(),null);
				if(tocountry!=null&&tocountry.get("marketplaceId")!=null) {
					marketplaceid=tocountry.get("marketplaceId").toString()	;
				}
				inplan.setMarketplaceid(marketplaceid);
				inplan.setName(shipname);
				String number = null;
				try {
					number = serialNumService.readSerialNumber(auth.getShopId(), "SF");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inplan.setNumber(number);
				inplan.setOperator("1");
				inplan.setShopid(auth.getShopId());
				if(StrUtil.isNotBlank(warehouseid)) {
					inplan.setWarehouseid(warehouseid);
				}
				inplan.setSkunum(skunum);
				if(item.getShipFromAddress()!=null&&item.getShipFromAddress().getName()!=null) {
					QueryWrapper<ShipAddress> query=new QueryWrapper<ShipAddress>();
					query.eq("groupid",auth.getGroupid());
					query.eq("name",item.getShipFromAddress().getName());
					List<ShipAddress> shipAddresslist = shipAddressService.list(query);
					if(shipAddresslist!=null && shipAddresslist.size()>0) {
						inplan.setShipfromaddressid(shipAddresslist.get(0).getId());
					}else {
						//插入shipaddress表   并给inbound表addressid
						ShipAddress shipAddress=getAddress(auth,item);
						boolean saveShipResult=shipAddressService.save(shipAddress);
						if(saveShipResult) {
							inplan.setShipfromaddressid(shipAddress.getId());
						}
					}
				}
			 ShipInboundShipment shipment = getShipment(auth,market,item);
		     shipment.setInboundplan(inplan);
		return shipment;
	}



	@Override
	public List<SummaryShipmentVo> showPlanListByPlanid(String planid, String shipmentid) {
		return this.baseMapper.selectitemListByPlanid(planid, shipmentid);
	}



	@Override
	public IPage<ShipPlanVo> getPlanList(ShipPlanListDTO dto,UserInfo user) {
		dto.setShopid(user.getCompanyid());
		if(StrUtil.isNotEmpty(dto.getSearchtype())&&StrUtil.isNotEmpty(dto.getSearch())) {
			if(dto.getSearchtype().equals("remark")||dto.getSearchtype().equals("sku")) {
				dto.setSearch("%"+dto.getSearch().trim()+"%");
			}
		}
		IPage<ShipPlanVo> list = this.baseMapper.findByCondition(dto.getPage(),dto);
		return list;
	}



	@Override
	public ShipPlanVo getPlanBaseInfo(String inboundplanid, UserInfo user) {
		ShipPlanListDTO dto=new ShipPlanListDTO();
		dto.setPlanid(inboundplanid);
		IPage<ShipPlanVo> list = this.getPlanList(dto, user);
		if(list!=null&&list.getTotal()>0) {
			ShipPlanVo vo=list.getRecords().get(0);
			ShipAddress address = shipAddressService.getById(vo.getAddressid());
			vo.setAddress(address);
			List<SummaryShipmentVo> listshipment = showPlanListByPlanid(inboundplanid,null);
			 for(SummaryShipmentVo item:listshipment) {
				 List<ShipInboundItemVo> itemlist = iShipInboundItemService.listByShipmentid(item.getShipmentId());
				 item.setItemlist(itemlist);
				 ShipAddressTo addressto = shipAddressToService.getById(item.getShipToAddressID());
				 item.setAddressTo(addressto);
			 }
			 vo.setShipmentList(listshipment);
            return vo;
		}
		return null;
	}
	
   


	@Override
	public Map<String, Object> uploadShipListByExcel(Sheet sheet, Map<String, Object> map) {
		Map<String, Object> skuMap = new HashMap<String, Object>();
		List<String> skuList = new ArrayList<String>();
		boolean isok = true;
		for(int i = 1; i <= sheet.getLastRowNum(); i++){
			Row crow = sheet.getRow(i);
			Cell cell = crow.getCell(0);
			cell.setCellType(CellType.STRING);
			String sku = cell.getStringCellValue();
			if (StrUtil.isNotEmpty(sku)) {
				List<ProductInfo> templist = productInfoMapper.selectBySku(sku.trim(), map.get("marketplaceid").toString(),
						map.get("amazonauthid").toString());
				if (templist==null || templist.size()==0) {
					isok = false;
					skuList.add(sku);
				} else {
					Double qty = crow.getCell(1).getNumericCellValue();
					if(map.get("warehouseid")==null){//不用本地库存
						if(qty>0){
							skuMap.put(sku, (int)Math.floor(qty));
						}
					} else {
						if(qty>0){
							//拿msku
							String msku= productInfoMapper.findMSKUBySKUMarket(sku, map.get("marketplaceid").toString(),
									map.get("amazonauthid").toString());
							if(msku==null){
								isok = false;
								skuList.add(sku);
							} else {
								skuMap.put(sku, (int)Math.floor(qty));
							}
						}
					}
				}
			}
		}
		map.put("searchsku", skuMap.keySet());
		if (isok) {
			if(skuMap!=null&&skuMap.keySet().size()>0) {
				List<Map<String, Object>> result = productInfoMapper.findAllByCondition(map);
				for(Map<String, Object> rmap : result){
					rmap.put("quantity", skuMap.get(rmap.get("sku")));
				}
				map.put("result", result);
			}
			map.put("msg", "上传成功");
			return map;
		} else {
			String msg = "导入失败,SKU:";
			for (int i = 0; i < skuList.size(); i++) {
				msg += skuList.get(i) + " ";
			}
			msg += "店铺站点不匹配。";
			map.put("msg", msg);
			return map;
		}
	}


	@SuppressWarnings("unchecked")
	public ChartLine shipArrivalTimeChart(String groupid,String amazonAuthId, String sku,String marketplaceid, int daysize,UserInfo userinfo) {
		SimpleDateFormat FMT_YMD = new SimpleDateFormat("MM-dd");
		String shopid=userinfo.getCompanyid();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> result = iShipInboundItemService.getshotTime(userinfo.getCompanyid(),groupid,marketplaceid,sku);
		List<Map<String, Object>> shipRecord = this.baseMapper.getShipArrivalTimeRecord(userinfo.getCompanyid(),marketplaceid,sku);
		List<ChartMarkpoint> markpointList =new LinkedList<ChartMarkpoint>();
		List<ChartMarkpoint> markpointList2 =new LinkedList<ChartMarkpoint>();
		ChartLine line=new ChartLine();
		try {
			String country="EU";
			if(!marketplaceid.equals("EU")) {
				Marketplace mk = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
				country=mk.getMarket();
			}
			  Result<List<Map<String, Object>>> oversea = erpClientOneFeign.getShipArrivalTimeRecord(userinfo.getCompanyid(),country,groupid,sku);
			if(Result.isSuccess(oversea)&&oversea.getData()!=null) {
				 List<Map<String, Object>> overseaList = oversea.getData();
				 shipRecord.addAll(overseaList);
			}
		}catch(FeignException e) {
			BizException.getMessage(e, "获取海外仓失败");
		}
		
		if (shipRecord != null && shipRecord.size() > 0) {
			List<Map<String, Object>> newShipRecoed = new ArrayList<Map<String, Object>>();
			Map<String, Object> prevmaps = null;
			for (int k = 0; k < shipRecord.size(); k++) {
				Map<String, Object> maps = shipRecord.get(k);
				BigDecimal unreceivedquantity = new BigDecimal(maps.get("shipQuantity").toString()); 
				BigDecimal inbondquantity = new BigDecimal(maps.get("Quantity").toString());
				if (unreceivedquantity.floatValue() <= 5 || (inbondquantity.floatValue() != 0 && 
						unreceivedquantity.divide(inbondquantity, 4, RoundingMode.HALF_EVEN).floatValue() < 0.06)) {
					continue;
				}
				Date arrivalTime = AmzDateUtils.getDate(maps.get("arrivalTime"));
				if (arrivalTime != null) {
					LocalDate DateNow = ZonedDateTime.ofInstant(arrivalTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
					String shipid = (String) maps.get("shipmentid");
					Date createdate = AmzDateUtils.getDate(maps.get("createdate"));
					String channame = (String) maps.get("channame");
					String tooltipname = "<table style='color:white;margin:8px'><tr><td>货物编码：" + shipid + "</td></tr><tr><td>"
							+ "创建日期：" + GeneralUtil.formatDate(createdate) + "</td></tr><tr><td>" + "货物渠道：" + channame + "</td></tr><tr><td>"
							+ "预计到货日期：" + GeneralUtil.formatDate(arrivalTime, FMT_YMD) + "</td></tr><tr><td>"
							+ "预计到货数量：" + inbondquantity + "</td></tr></table>";
					String tooltipvalue = "";
					maps.put("tooltipName", tooltipname);
					maps.put("tooltipValue", tooltipvalue);
					if (prevmaps == null) {
						prevmaps = maps;
						newShipRecoed.add(prevmaps);
					} else {
						Date prevArrivalTime =AmzDateUtils.getDate(prevmaps.get("arrivalTime"));
						LocalDate prevDateNow = ZonedDateTime.ofInstant(prevArrivalTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
						if (prevDateNow.equals(DateNow)) {
							newShipRecoed.remove(prevmaps);
							int nowquantity = Integer.parseInt(maps.get("Quantity").toString());
							int prevquantity = Integer.parseInt(prevmaps.get("Quantity").toString());
							int nowshipQuantity = Integer.parseInt(maps.get("shipQuantity").toString());
							int prevshipQuantity = Integer.parseInt(prevmaps.get("shipQuantity").toString());
							maps.put("Quantity", nowquantity + prevquantity);
							maps.put("shipQuantity", nowshipQuantity + prevshipQuantity);
							maps.put("tooltipName", prevmaps.get("tooltipName") + tooltipname);
							maps.put("tooltipValue", "");
						}
						prevmaps = maps;
						newShipRecoed.add(maps);
					}
				} else {
					continue;
				}
			}
			if (newShipRecoed != null && newShipRecoed.size() > 0)
				shipRecord = newShipRecoed;
		    }
			 if (result != null && result.size() > 0) {
					for (Map<String, Object> skuMap : result) {
						if(skuMap.get("marketplaceId")!=null && skuMap.get("marketplaceId").toString().equals("EU")){
							int summonth = skuMap.get("salesmonth") == null ? 0 : Integer.parseInt(skuMap.get("salesmonth").toString());
							int sumseven = skuMap.get("salesweek") == null ? 0 : Integer.parseInt(skuMap.get("salesweek").toString());
							int sum15 = skuMap.get("salesfifteen") == null ? 0 : Integer.parseInt(skuMap.get("salesfifteen").toString());
							String openDate = skuMap.get("openDate") == null ? null : skuMap.get("openDate").toString();
							DaysalesFormula formula = daysalesFormulaService.selectByShopid(shopid);
							int qty = DaysalesFormulaServiceImpl.getAvgSales(formula, summonth, sumseven, sum15, GeneralUtil.parseDate(openDate));
							skuMap.put("sales", qty);
						} 
						//如果修改过预估销量，使用用户干预的日均销量
						if(skuMap.get("presales")!=null) {
							skuMap.put("sales", skuMap.get("presales"));
						}
						//每日预估销量
						Map<String,Integer> presaleMap = productInPresaleService.selectBySKUMarket(sku,marketplaceid, groupid);
						Date today = new Date();
						List<String> label = new ArrayList<String>();
						List<Object> data = new ArrayList<Object>();
						List<Object> inputquantity = new ArrayList<Object>();
						List<Object> salfQuantityList = new ArrayList<Object>();
						BigDecimal sales = skuMap.get("sales") == null ? new BigDecimal("0") : new BigDecimal(skuMap.get("sales").toString());
						BigDecimal quantity = skuMap.get("quantity") == null ? new BigDecimal("0") : new BigDecimal(skuMap.get("quantity").toString());
						//安全库存周期
						BigDecimal stocking_cycle = skuMap.get("stocking_cycle") == null ? new BigDecimal("0") : new BigDecimal(skuMap.get("stocking_cycle").toString());
						String Mysku = skuMap.get("sku").toString();
						ChartMarkpoint markpoint = new ChartMarkpoint();
					    BigDecimal willaddquantity = new BigDecimal("0");
			            if (shipRecord != null && shipRecord.size() > 0) {
								for (int i = 0; i < shipRecord.size(); i++) {
									Map<String, Object> map = shipRecord.get(i);
									if (map.get("arrivalTime") == null || !Mysku.equals(map.get("sku"))) {
										continue;
									}
									Date arrivalTime = AmzDateUtils.getDate( map.get("arrivalTime"));
									int days = comparePastDate(new Date(), arrivalTime);
									BigDecimal inbondquantity = new BigDecimal(map.get("Quantity").toString());
									BigDecimal arrivalquantity = new BigDecimal(map.get("shipQuantity").toString());
									if (days < 0) {// 预计到达日期在今天之前,且收货数量与发货数量相差5%
										if (arrivalquantity.floatValue() <= 5 || (inbondquantity.floatValue() != 0
												&& arrivalquantity.divide(inbondquantity, 4, RoundingMode.HALF_EVEN).floatValue() < 0.06)) {
											continue;
										}
										willaddquantity = willaddquantity.add(arrivalquantity);
										String tooltipName = map.get("tooltipName").toString();
										tooltipName = tooltipName.replace("</table>", "");
										tooltipName += "<tr><td class='text-red'>比预计少收数量：" + arrivalquantity + "</td></tr></table>";
										if (markpoint.getName() != null) {
											markpoint.setName(markpoint.getName()+tooltipName);
											markpoint.setValue(markpoint.getValue()+ map.get("tooltipValue").toString());
										} else {
											markpoint.setName(tooltipName);
											markpoint.setValue( map.get("tooltipValue").toString());
										}
									}
								}
							}
						BigDecimal inbondquantity = new BigDecimal("0");
						BigDecimal suminbondquantity;
						boolean isfirst = true;
						int daynum=0;
						int day=daysize;
						while (day > 0) {
							BigDecimal daysales = sales;
							suminbondquantity = new BigDecimal("0");
							if(daynum==2) {
								if (markpoint.getName() != null) {
									markpoint.setXAxis( GeneralUtil.formatDate(today, FMT_YMD).replace("-", "."));
									markpoint.setYAxis( quantity);
									markpointList2.add(markpoint);
								}
								quantity = quantity.add(willaddquantity);
							}
							if (!isfirst) {
								String dateStr = GeneralUtil.formatDate(today,"yyyy-MM-dd");
								if(presaleMap!=null && presaleMap.get(dateStr)!=null){
									daysales = new BigDecimal(presaleMap.get(dateStr)+"");
								} else {
									daysales = sales;
								}
								quantity = quantity.subtract(daysales);
							}
							isfirst = false;
							if (shipRecord != null && shipRecord.size() > 0) {
								for (int i = 0; i < shipRecord.size(); i++) {
									Map<String, Object> map = shipRecord.get(i);
									if (map.get("tooltipName") == null || map.get("arrivalTime") == null || map.get("sku") == null) {
										continue;
									}
									Date arrivalTime = AmzDateUtils.getDate( map.get("arrivalTime"));
									String shipSku = (String) map.get("sku");
									if (!Mysku.equals(shipSku)) {
										continue;
									}
									LocalDate Date1 = ZonedDateTime.ofInstant(today.toInstant(), ZoneId.systemDefault()).toLocalDate();
									LocalDate Date2 = ZonedDateTime.ofInstant(arrivalTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
									if (Date2.equals(Date1)) {
										inbondquantity = new BigDecimal(map.get("Quantity").toString());
										suminbondquantity = inbondquantity.add(suminbondquantity);
										quantity = quantity.add(inbondquantity);
										if (inbondquantity.compareTo(new BigDecimal("0")) == 1) {
											ChartMarkpoint markpointTemp=new ChartMarkpoint();
											markpointTemp.setName(map.get("tooltipName").toString());
											markpointTemp.setValue( map.get("tooltipValue").toString());
											markpointTemp.setXAxis(GeneralUtil.formatDate(today, FMT_YMD).replace("-", "."));
											markpointTemp.setYAxis(quantity);
											markpointList2.add(markpointTemp);
										}
									}
								}
							}
							if (quantity.compareTo(new BigDecimal("0")) == -1) {
								quantity = new BigDecimal("0");
							}
							data.add(quantity);
							inputquantity.add(suminbondquantity);
							//安全库存 = 安全库存周期 * 销量
							BigDecimal salfQuantity = daysales.multiply(stocking_cycle);
							salfQuantityList.add(salfQuantity);
							inbondquantity = new BigDecimal("0");
							label.add(GeneralUtil.formatDate(today, FMT_YMD));
							today = getSomeDay(today, 1);
							day--;
							daynum++;
						}
						day = daysize;
					    line.setMarkpoint2(markpointList2);
						line.setData(data);
						line.setData2(salfQuantityList);
						line.setData3(inputquantity);
						line.setName(Mysku);
					 
					}
			 }
					for (int j = 0; j < resultList.size(); j++) {
						Map<String, Object> maps = resultList.get(j);
						List<BigDecimal> quantyList = (List<BigDecimal>) maps.get("data");
						List<String> resultLabels = (List<String>) maps.get("labels");
						for (int i = 0; i < quantyList.size(); i++) {
						     ChartMarkpoint markpoint = new ChartMarkpoint();
							if (quantyList.get(i).compareTo(new BigDecimal("0")) == 0) {
								markpoint.setName(resultLabels.get(i) + "库存数量 ：");
								markpoint.setValue("0");
								markpoint.setXAxis(resultLabels.get(i));
								markpoint.setYAxis(quantyList.get(i));
								markpointList.add(markpoint);
								break;
							}
						}
						line.setMarkpoint(markpointList);
					}
				 
			return line;
	}
	// 几天后的时间
		public Date getSomeDay(Date date, int day) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, day);
			return calendar.getTime();
		}
		// 计算两个日期的天数差别
		public int comparePastDate(Date date1, Date date2) {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			int day1 = cal1.get(Calendar.DAY_OF_YEAR);
			int day2 = cal2.get(Calendar.DAY_OF_YEAR);
			int year1 = cal1.get(Calendar.YEAR);
			int year2 = cal2.get(Calendar.YEAR);
			if (year1 != year2) {
				int timeDistance = 0;
				for (int i = year1; i < year2; i++) {
					if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
						timeDistance += 366;
					} else {
						timeDistance += 365;
					}
				}
				return timeDistance + (day2 - day1);
			} else {
				return day2 - day1;
			}
		}

		
		public List<Map<String, Object>> getShipRecordByMarket(String marketplaceid, String groupid) {
			// TODO Auto-generated method stub
			return this.baseMapper.getShipRecordByMarket( marketplaceid, groupid);
		}
		
		public List<Map<String, Object>> getShipRecord(String shopid,String groupid, String marketplaceid, String sku) {
			// TODO Auto-generated method stub
			return this.baseMapper.getShipRecord(groupid, marketplaceid, sku,shopid);
		}

		@Override
		public List<Map<String, Object>> getShipBadRecord(String shopid, String marketplaceid, String sku) {
			// TODO Auto-generated method stub
			return this.baseMapper.getShipBadRecord(marketplaceid, sku, shopid);
		}

		@Override
		public IPage<Map<String, Object>> getShipmentReport(Page<?> page, Map<String, Object> param) {
			// TODO Auto-generated method stub
			return this.baseMapper.getShipmentReport(page,param);
		}
		
		public void setExcelBookByType(SXSSFWorkbook workbook, Map<String, Object> params) {
			String type = params.get("ftype").toString();
			// 货件发货明细
			if ("shipment".equals(type)) {
				Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
				titlemap.put("ShipmentId", "货件编码");
				titlemap.put("groupname", "发货店铺");
				titlemap.put("market", "收货站点");
				titlemap.put("center", "配送中心");
				titlemap.put("warehouse", "发货仓库");
				titlemap.put("createdate", "创建日期");
				titlemap.put("shiped_date", "发货日期");
				titlemap.put("arrivalTime", "预计到货日期");
				titlemap.put("start_receive_date", "开始接受日期");
				titlemap.put("status6date", "完成日期");
				titlemap.put("company", "承运商");
				titlemap.put("transtype", "运输方式");
				titlemap.put("channame", "发货渠道");
				
				titlemap.put("qtyshipped", "发货数量");
				titlemap.put("qtyreceived", "到货数量");
				titlemap.put("price", "运输费用");
				titlemap.put("otherfee", "其它费用");
				titlemap.put("totalprice", "物流费用统计");
				titlemap.put("transweight", "实际计费重量");
				titlemap.put("weightkg", "预估发货重量");
				titlemap.put("boxweight", "装箱实际重量(kg)");
				titlemap.put("boxvolume", "装箱材积重量");
				titlemap.put("wunit", "重量单位");
				titlemap.put("shipmentstatus", "状态");
				List<Map<String, Object>> list = this.baseMapper.getShipmentReport(params);
				Sheet sheet = workbook.createSheet("sheet1");
				// 在索引0的位置创建行（最顶端的行）
				Row trow = sheet.createRow(0);
				Object[] titlearray = titlemap.keySet().toArray();
				for (int i = 0; i < titlearray.length; i++) {
					Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
					Object value = titlemap.get(titlearray[i].toString());
					cell.setCellValue(value.toString());
				}
				for (int i = 0; i < list.size(); i++) {
					Row row = sheet.createRow(i + 1);
					Map<String, Object> map = list.get(i);
					for (int j = 0; j < titlearray.length; j++) {
						Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
						Object value = map.get(titlearray[j].toString());
						if (value != null) {
							if ("totalprice".equals(titlearray[j].toString())) {
								if (StrUtil.isEmpty(value.toString()) || value == null) {
									value = "";
								}
							}
							cell.setCellValue(value.toString());
						}
					}
				}
			} else if("mainlist".equals(type)){
				Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
				titlemap.put("sku", "SKU");
				titlemap.put("ShipmentId", "货件编码");
				titlemap.put("groupname", "发货店铺");
				titlemap.put("market", "收货站点");
				titlemap.put("center", "配送中心");
				titlemap.put("warehouse", "发货仓库");
				titlemap.put("shiped_date", "发货日期");
				titlemap.put("arrivalTime", "预计到货日期");
				titlemap.put("channame", "发货渠道");
				titlemap.put("QuantityShipped", "发货数量");
				titlemap.put("QuantityReceived", "到货数量");
				titlemap.put("shipmentstatus", "状态");
				titlemap.put("createdate", "创建日期");
				List<Map<String, Object>> list = this.baseMapper.getShipmentDetailReport(params);
				Sheet sheet = workbook.createSheet("sheet1");
				// 在索引0的位置创建行（最顶端的行）
				Row trow = sheet.createRow(0);
				Object[] titlearray = titlemap.keySet().toArray();
				for (int i = 0; i < titlearray.length; i++) {
					Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
					Object value = titlemap.get(titlearray[i].toString());
					cell.setCellValue(value.toString());
				}
				for (int i = 0; i < list.size(); i++) {
					Row row = sheet.createRow(i + 1);
					Map<String, Object> map = list.get(i);
					for (int j = 0; j < titlearray.length; j++) {
						Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
						Object value = map.get(titlearray[j].toString());
						if (value != null) {
							cell.setCellValue(value.toString());
						}
					}
				}
			}else {
				Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
				titlemap.put("mainsku", "本地SKU");
				titlemap.put("subsku", "本地子SKU");
				titlemap.put("name", "产品名称");
				titlemap.put("subnumber", "子SKU单位量");
				titlemap.put("subskuout", "子SKU消耗量");
				titlemap.put("QuantityShipped", "发货数量");
				titlemap.put("sku", "平台SKU");
				titlemap.put("ShipmentId", "货件编码");
				titlemap.put("groupname", "发货店铺");
				titlemap.put("market", "收货站点");
				titlemap.put("center", "配送中心");
				titlemap.put("warehouse", "发货仓库");
				titlemap.put("shiped_date", "发货日期");
				titlemap.put("arrivalTime", "预计到货日期");
				titlemap.put("channame", "发货渠道");
				titlemap.put("QuantityReceived", "到货数量");
				titlemap.put("shipmentstatus", "状态");
				titlemap.put("createdate", "创建日期");
				List<Map<String, Object>> list = this.baseMapper.getShipmentDetailAssReport(params);
				Sheet sheet = workbook.createSheet("sheet1");
				// 在索引0的位置创建行（最顶端的行）
				Row trow = sheet.createRow(0);
				Object[] titlearray = titlemap.keySet().toArray();
				for (int i = 0; i < titlearray.length; i++) {
					Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
					Object value = titlemap.get(titlearray[i].toString());
					cell.setCellValue(value.toString());
				}
				for (int i = 0; i < list.size(); i++) {
					Row row = sheet.createRow(i + 1);
					Map<String, Object> map = list.get(i);
					for (int j = 0; j < titlearray.length; j++) {
						Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
						Object value = map.get(titlearray[j].toString());
						if (value != null) {
							cell.setCellValue(value.toString());
						}
					}
				}
			}
		}



		@Override
		public IPage<Map<String, Object>> getShipmentDetailReport(Page<Object> page, Map<String, Object> param) {
			// TODO Auto-generated method stub
			IPage<Map<String, Object>> result = this.baseMapper.getShipmentDetailReport(page,param);
			if (result != null && result.getRecords().size() > 0&&page.getCurrent()==1) {
				Map<String, Object> summary = this.baseMapper.getShipmentDetailReportTotal(param);
				Map<String, Object> map = result.getRecords().get(0);
				map.put("summary", summary);
			}
			return result;
		}
}
