package com.wimoor.erp.stock.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.wimoor.common.result.Result;
import com.wimoor.erp.common.pojo.entity.ChartLine;
import com.wimoor.erp.common.pojo.entity.ChartPoint;
import com.wimoor.erp.order.mapper.OrderPlanMapper;
import com.wimoor.erp.order.service.impl.OrderSkuPresaleServiceImpl;
import feign.FeignException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.stock.mapper.ErpDispatchOverseaFormMapper;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaForm;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaFormEntry;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormEntryService;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Service
@RequiredArgsConstructor
public class ErpDispatchOverseaFormServiceImpl extends ServiceImpl<ErpDispatchOverseaFormMapper, ErpDispatchOverseaForm> implements IErpDispatchOverseaFormService {
	 
	final IErpDispatchOverseaFormEntryService iErpDispatchOverseaFormEntryService;
	 
	final IInventoryFormAgentService inventoryFormAgentService;
	 
	final IMaterialService materialService;
	 
	final IWarehouseService warehouseService;
	 
	final ISerialNumService serialNumService;
	 
	final IInventoryService inventoryService;
	final OrderPlanMapper orderPlanMapper;
	@Override
	public int submitAction(UserInfo user,String ids,String status ) {
		int result = 0;
		String[] idlist = ids.split(",");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			if (GeneralUtil.isNotEmpty(id)) {
				ErpDispatchOverseaForm dispatchForm = this.getById(id);
				if ("1".equals(status)||"0".equals(status)) {
				    //提交后不可编辑
				}
				if ("2".equals(status)) {
					if(dispatchForm.getAuditstatus()==1) {
					   changeInventory(user,dispatchForm);
					}
				}else if("3".equals(status)) {
					if(dispatchForm.getAuditstatus()==1) {
						changeInventory(user,dispatchForm);
						outBoundOut(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==2) {
						outBoundOut(user,dispatchForm);
					}
				}else if("4".equals(status)) {
					if(dispatchForm.getAuditstatus()==1||dispatchForm.getAuditstatus()==0) {
						changeInventory(user,dispatchForm);
						outBoundOut(user,dispatchForm);
						inBoundToFulfillable(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==2) {
						outBoundOut(user,dispatchForm);
						inBoundToFulfillable(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==3) {
						inBoundToFulfillable(user,dispatchForm);
					}
				}else if("5".equals(status)) {
					if(dispatchForm.getAuditstatus()==2) {
						cancelInventory(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==3) {
						cancelOut(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==4) {
						cancelIn(user,dispatchForm);
					}
					if(dispatchForm.getAuditstatus()==0) {
						this.baseMapper.deleteById(dispatchForm);
					}
				}
				if(!"0".equals(status)) {
					dispatchForm.setAudittime(new Date());
					dispatchForm.setAuditstatus(Integer.parseInt(status));
					result = this.baseMapper.updateById(dispatchForm);
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> saveAction(UserInfo user, ErpDispatchOverseaForm dispatchForm, String skumapstr) {
		// TODO Auto-generated method stub
		if (GeneralUtil.isNotEmpty(skumapstr)) {
			JSONArray skulist = GeneralUtil.getJsonArray(skumapstr);
			dispatchForm.setId(null);
			return saveForm(user,dispatchForm, skulist);
		}
		return null;
	}

	@Override
	public Map<String, Object> findById(String id) {
		// TODO Auto-generated method stub
		return this.baseMapper.findById(id);
	}

	@Override
	public String uploadDispatchStockByExcel(Sheet sheet, UserInfo user) {
		// TODO Auto-generated method stub
		Row whrow = sheet.getRow(0);
		Cell whnamecell = whrow.getCell(1);
		String whname_out = whnamecell.getStringCellValue();
		Row whrow2 = sheet.getRow(1);
		Cell whnamecell2 = whrow2.getCell(1);
		String whname_in = whnamecell2.getStringCellValue();
		Row whrow3 = sheet.getRow(2);
		Cell remarkcell = whrow3.getCell(1);
		remarkcell.setCellType(CellType.STRING);
		String remark = remarkcell.getStringCellValue();
		if (GeneralUtil.isEmpty(whname_out)||GeneralUtil.isEmpty(whname_in)) {
			return "仓库名称不能为空";
		}
		String shopid = user.getCompanyid();
		Warehouse wh_out = warehouseService.getWarehouseByName(shopid,whname_out);
		if (wh_out == null || !wh_out.getFtype().contains("self_")) {
			return "调出仓库无法匹配";
		}
		Warehouse wh_in = warehouseService.getWarehouseByName(shopid,whname_in);
		if (wh_in == null || !wh_in.getFtype().contains("self_")) {
			return "调入仓库无法匹配";
		}
		ErpDispatchOverseaForm dispatchForm = new ErpDispatchOverseaForm();
		dispatchForm.setId(warehouseService.getUUID());
		dispatchForm.setShopid(shopid);
		try {
			dispatchForm.setNumber(serialNumService.readSerialNumber(shopid, "PD"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				dispatchForm.setNumber(serialNumService.readSerialNumber(shopid, "PD"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
		}
		dispatchForm.setFromWarehouseid(new BigInteger(wh_out.getId()));
		dispatchForm.setToWarehouseid(new BigInteger(wh_in.getId()));
		dispatchForm.setRemark(remark);
		dispatchForm.setAuditor(user.getId());
		dispatchForm.setCreator(user.getId());
		dispatchForm.setCreatedate(new Date());
		dispatchForm.setOperator(user.getId());
		dispatchForm.setAuditstatus(1);
		dispatchForm.setAudittime(new Date());
		dispatchForm.setOpttime(new Date());
		List<String> skuList = new ArrayList<String>();
		List<String> invList = new ArrayList<String>();
		boolean isok = true;
		for (int i = 4; i <= sheet.getLastRowNum(); i++) {
			Row skuRow = sheet.getRow(i);
			Cell cell = skuRow.getCell(0);
			cell.setCellType(CellType.STRING);
			String sku = cell.getStringCellValue();
			if(GeneralUtil.isEmpty(sku)) {
				continue;
			}
			Material material = materialService.selectBySKU(shopid,sku);
			if (material==null) {
				isok = false;
				skuList.add(sku);
			} else {
				Double qty = skuRow.getCell(1).getNumericCellValue();
				//判断库存是否充足
				Inventory inv = inventoryService.selectNowInv(wh_out.getId(), material.getId(), shopid, Status.fulfillable);
				if(inv!=null && inv.getQuantity()>=qty){
					//skuMap.put(material.getId(),(int)Math.floor(qty));
				} else {
					isok = false;
					invList.add(sku);
				}
			}
		}
		if (isok) {
			//this.saveForm(user,dispatchForm, skuMap);
			return "上传成功";
		} else {
			String msg = "上传失败,";
			if(skuList.size()>0){
				msg += "SKU:";
				for (int i = 0; i < skuList.size(); i++) {
					msg += skuList.get(i) + " ";
				}
				msg += "不匹配,请先创建产品。<br/>";
			}
			if(invList.size()>0){
				msg += "SKU:";
				for (int i = 0; i < invList.size(); i++) {
					msg += invList.get(i) + " ";
				}
				msg += "在"+wh_out.getName()+"库存不足。";
			}
			return msg;
		}
	}

	@Override
	public IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByCondition(page,map);
	}

	public Map<String, Object> saveForm(UserInfo user,ErpDispatchOverseaForm dispatchForm, JSONArray skuMap) {
		Integer status = dispatchForm.getAuditstatus();
		dispatchForm.setAuditstatus(0);
		int result = 0;
		String msg = "";
		if (skuMap.size() > 0) {
			ErpDispatchOverseaForm oldFormEntity = getById(dispatchForm.getId());
			if (oldFormEntity != null) {
				result = this.baseMapper.updateById(dispatchForm);
				msg = "更新";
			} else {
				result = this.baseMapper.insert(dispatchForm);
				msg = "添加";
			}
			List<Map<String, Object>> oldentrylist = iErpDispatchOverseaFormEntryService.selectByFormid(dispatchForm.getId());
			if (oldentrylist != null && oldentrylist.size() > 0) {
				iErpDispatchOverseaFormEntryService.deleteByFormid(dispatchForm.getId());
			}
			for (int i=0;i<skuMap.size();i++) {
				JSONObject item = skuMap.getJSONObject(i);
				ErpDispatchOverseaFormEntry dispatchFormEntry = new ErpDispatchOverseaFormEntry();
				dispatchFormEntry.setFormid(new BigInteger(dispatchForm.getId()));
				dispatchFormEntry.setMaterialid(new BigInteger(item.getString("id")));
				dispatchFormEntry.setAmount(item.getInteger("amount"));
				dispatchFormEntry.setSellersku(item.getString("psku"));
				dispatchFormEntry.setFnsku(item.getString("fnsku"));
				iErpDispatchOverseaFormEntryService.save(dispatchFormEntry);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(result>0) {
			submitAction(user,dispatchForm.getId(),status.toString());
		}
		if (result > 0) {
			msg += "成功！";
		} else {
			msg += "失败！";
		}
		map.put("msg", msg);
		map.put("id", dispatchForm.getId());
		return map;
	}

	public void inBoundToFulfillable(UserInfo user ,ErpDispatchOverseaForm dispatchForm){
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.alReady);
		List<Map<String, Object>> entrylist = iErpDispatchOverseaFormEntryService.selectByFormid(dispatchForm.getId());
		if (entrylist != null && entrylist.size() > 0) {
			for (int j = 0; j < entrylist.size(); j++) {
				parameter_in.setMaterial(entrylist.get(j).get("materialid").toString());
				parameter_in.setAmount(Integer.parseInt(entrylist.get(j).get("amount").toString()));
				inventoryFormAgentService.inStockByRead(parameter_in);
			}
		}
	}
	public void outBoundOut(UserInfo user ,ErpDispatchOverseaForm dispatchForm) {
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.alReady);
		List<Map<String, Object>> entrylist = iErpDispatchOverseaFormEntryService.selectByFormid(dispatchForm.getId());
		if (entrylist != null && entrylist.size() > 0) {
			for (int j = 0; j < entrylist.size(); j++) {
				parameter_out.setMaterial(entrylist.get(j).get("materialid").toString());
				parameter_out.setAmount(Integer.parseInt(entrylist.get(j).get("amount").toString()));
				inventoryFormAgentService.outStockByRead(parameter_out);
			}
		}
	}
	 

    public void changeInventory(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.Ready);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.Ready);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockByRead(parameter_out);
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockByRead(parameter_in);
		}
    }
   
    public void cancelOut(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.alReady);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.Ready);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockReadyCancel(parameter_out);
		 
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockReadyCancel(parameter_in);
		}
    }
    public void cancelIn(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.alReady);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.alReady);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockReadyCancel(parameter_out);
		 
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockReadyCancel(parameter_in);
		}
    }
    
    public void cancelInventory(UserInfo user,ErpDispatchOverseaForm dispatchForm) {
		List<ErpDispatchOverseaFormEntry> entrylist = iErpDispatchOverseaFormEntryService.findByFormid(dispatchForm.getId());
		InventoryParameter parameter_out = new InventoryParameter();
		parameter_out.setShopid(dispatchForm.getShopid());
		parameter_out.setFormid(dispatchForm.getId());
		parameter_out.setNumber(dispatchForm.getNumber());
		parameter_out.setFormtype("overseaship");
		parameter_out.setWarehouse(dispatchForm.getFromWarehouseid().toString());
		parameter_out.setOperator(dispatchForm.getOperator());
		parameter_out.setStatus(EnumByInventory.Ready);
		
		InventoryParameter parameter_in = new InventoryParameter();
		parameter_in.setShopid(dispatchForm.getShopid());
		parameter_in.setFormid(dispatchForm.getId());
		parameter_in.setFormtype("overseaship");
		parameter_in.setWarehouse(dispatchForm.getToWarehouseid().toString());
		parameter_in.setOperator(dispatchForm.getOperator());
		parameter_in.setNumber(dispatchForm.getNumber());
		parameter_in.setStatus(EnumByInventory.Ready);
		
		for(int i=0;i<entrylist.size();i++) {
			ErpDispatchOverseaFormEntry entry = entrylist.get(i);
			String materialid = entry.getMaterialid().toString();
			parameter_out.setMaterial(materialid);
			parameter_out.setAmount(entry.getAmount());
			inventoryFormAgentService.outStockReadyCancel(parameter_out);
		 
			parameter_in.setMaterial(materialid);
			parameter_in.setAmount(entry.getAmount());
			inventoryFormAgentService.inStockReadyCancel(parameter_in);
		}
    }
	String getStatusName(Integer  auditstatus){
		if(auditstatus==1) return "待审核";
		if(auditstatus==2) return "配货中";
		if(auditstatus==3) return "已发货";
		if(auditstatus==4) return "已完成";
		if(auditstatus==5) return "已驳回";
		return "";
	}
	public List<Map<String, Object>> getShipArrivalTimeRecord(UserInfo user,String warehouseid, String sku ){
		List<Map<String, Object>> list = this.baseMapper.getShipArrivalTimeRecord(user.getCompanyid(), warehouseid, null, sku, null);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++) {
				Map<String, Object> map = list.get(i);
				map.put("statusName", getStatusName(Integer.parseInt(map.get("auditstatus").toString())));
			}
		}
		return list;
	}
	public List<Map<String, Object>> getShipArrivalTimeRecordByCountry(UserInfo user,String country, String sku ){
		List<Map<String, Object>> list = this.baseMapper.getShipArrivalTimeRecord(user.getCompanyid(), null, country, sku, null);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++) {
				Map<String, Object> map = list.get(i);
				map.put("statusName", getStatusName(Integer.parseInt(map.get("auditstatus").toString())));
			}
		}
		return list;
	}


	@Override
	public ChartLine shipArrivalTimeChart(String country, String sku, Integer daysize, UserInfo userinfo) {
		SimpleDateFormat FMT_YMD = new SimpleDateFormat("MM-dd");
		String shopid=userinfo.getCompanyid();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shipRecord = getShipArrivalTimeRecordByCountry(userinfo, country, sku);
		List<ChartPoint> markpointList =new LinkedList<ChartPoint>();
		List<ChartPoint> markpointList2 =new LinkedList<ChartPoint>();
		ChartLine line=new ChartLine();
		if (shipRecord != null && shipRecord.size() > 0) {
			List<Map<String, Object>> newShipRecoed = new ArrayList<Map<String, Object>>();
			Map<String, Object> prevmaps = null;
			for (int k = 0; k < shipRecord.size(); k++) {
				Map<String, Object> maps = shipRecord.get(k);
				BigDecimal unreceivedquantity = new BigDecimal(maps.get("shipQuantity").toString());
				BigDecimal inbondquantity = new BigDecimal(maps.get("Quantity").toString());
				if (unreceivedquantity.floatValue() <= 3 || (inbondquantity.floatValue() != 0 &&
						unreceivedquantity.divide(inbondquantity, 4, RoundingMode.HALF_EVEN).floatValue() < 0.06)) {
					continue;
				}
				Date arrivalTime = GeneralUtil.getDate(maps.get("arrivalTime"));
				if (arrivalTime != null) {
					LocalDate DateNow = ZonedDateTime.ofInstant(arrivalTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
					String shipid = (String) maps.get("shipmentid");
					Date createdate = GeneralUtil.getDate(maps.get("createdate"));
					String channame = (String) maps.get("channame");
					String tooltipname = "<table style='color:white;margin:8px'><tr><td>货物编码：" + shipid + "</td></tr><tr><td>"
							+ "创建日期：" + GeneralUtil.formatDate(createdate) + "</td></tr><tr><td>" + "货物渠道：" + channame + "</td></tr><tr><td>"
							+ "预计到货日期：" + GeneralUtil.formatDate(arrivalTime, FMT_YMD) + "</td></tr><tr><td>"
							+ "预计到货数量：" + unreceivedquantity+ "</td></tr></table>";
					String tooltipvalue = "";
					maps.put("tooltipName", tooltipname);
					maps.put("tooltipValue", tooltipvalue);
					if (prevmaps == null) {
						prevmaps = maps;
						newShipRecoed.add(prevmaps);
					} else {
						Date prevArrivalTime =GeneralUtil.getDate(prevmaps.get("arrivalTime"));
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
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", shopid);
		param.put("country", country);
		param.put("sku",sku);
		List<Map<String, Object>> result = this.orderPlanMapper.getExpandCountryData(param);
		if (result != null && result.size() > 0) {
			for (Map<String, Object> skuMap : result) {
				//如果修改过预估销量，使用用户干预的日均销量
				if(skuMap.get("msalesavg")!=null) {
					skuMap.put("salesavg", skuMap.get("msalesavg"));
				}
				//每日预估销量
				Date today = new Date();
				List<String> label = new ArrayList<String>();
				List<Object> data = new ArrayList<Object>();
				List<Object> inputquantity = new ArrayList<Object>();
				List<Object> salfQuantityList = new ArrayList<Object>();
				BigDecimal sales = skuMap.get("salesavg") == null ? new BigDecimal("0") : new BigDecimal(skuMap.get("salesavg").toString());
				BigDecimal quantity = skuMap.get("fulfillable") == null ? new BigDecimal("0") : new BigDecimal(skuMap.get("fulfillable").toString());
				//安全库存周期
				BigDecimal stocking_cycle = skuMap.get("stocking_cycle") == null ? new BigDecimal("0") : new BigDecimal(skuMap.get("stocking_cycle").toString());
				String Mysku = skuMap.get("sku").toString();
				ChartPoint markpoint = new ChartPoint();
				BigDecimal willaddquantity = new BigDecimal("0");
				if (shipRecord != null && shipRecord.size() > 0) {
					for (int i = 0; i < shipRecord.size(); i++) {
						Map<String, Object> map = shipRecord.get(i);
						if (map.get("arrivalTime") == null || !Mysku.equals(map.get("sku"))) {
							continue;
						}
						Date arrivalTime = GeneralUtil.getDate( map.get("arrivalTime"));
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
							tooltipName += "</table>";
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
						daysales = sales;
						quantity = quantity.subtract(daysales);
					}
					isfirst = false;
					if (shipRecord != null && shipRecord.size() > 0) {
						for (int i = 0; i < shipRecord.size(); i++) {
							Map<String, Object> map = shipRecord.get(i);
							if (map.get("tooltipName") == null || map.get("arrivalTime") == null || map.get("sku") == null) {
								continue;
							}
							Date arrivalTime = GeneralUtil.getDate( map.get("arrivalTime"));
							String shipSku = (String) map.get("sku");
							if (!Mysku.equals(shipSku)) {
								continue;
							}
							LocalDate Date1 = ZonedDateTime.ofInstant(today.toInstant(), ZoneId.systemDefault()).toLocalDate();
							LocalDate Date2 = ZonedDateTime.ofInstant(arrivalTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
							if (Date2.equals(Date1)) {
								inbondquantity = new BigDecimal(map.get("shipQuantity").toString());
								suminbondquantity = inbondquantity.add(suminbondquantity);
								quantity = quantity.add(inbondquantity);
								if (inbondquantity.compareTo(new BigDecimal("0")) == 1) {
									ChartPoint markpointTemp=new ChartPoint();
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
				ChartPoint markpoint = new ChartPoint();
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


	public Date getSomeDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

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
}
