package com.wimoor.amazon.inboundV2.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.amazon.spapi.model.fulfillmentinboundV20240320.BoxContentInformationSource;
import com.amazon.spapi.model.fulfillmentinboundV20240320.BoxInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ConfirmPackingOptionResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Currency;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Dimensions;
import com.amazon.spapi.model.fulfillmentinboundV20240320.GeneratePackingOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Incentive;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Item;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ItemInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.LabelOwner;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListPackingGroupItemsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListPackingOptionsResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PackageGroupingInput;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PackingOption;
import com.amazon.spapi.model.fulfillmentinboundV20240320.PrepOwner;
import com.amazon.spapi.model.fulfillmentinboundV20240320.SetPackingInformationRequest;
import com.amazon.spapi.model.fulfillmentinboundV20240320.SetPackingInformationResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UnitOfMeasurement;
import com.amazon.spapi.model.fulfillmentinboundV20240320.UnitOfWeight;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Weight;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundBoxV2Mapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundPlanPackingGroupItemMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundPlanPackingOptionsMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxItemMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxMapper;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCartDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.PackingDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlanPackingGroupItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlanPackingOptions;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBox;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBoxItem;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxService;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.amazon.inboundV2.service.IShipInboundOperationService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service("shipInboundBoxV2Service")
public class ShipInboundBoxServiceImpl extends  ServiceImpl<ShipInboundBoxV2Mapper,ShipInboundBox> implements IShipInboundBoxService {
	 @Lazy
	 @Autowired
	 IShipInboundCaseService shipInboundCaseV2Service;
	 @Autowired
	 IInboundApiHandlerService iInboundApiHandlerService;
	 @Autowired
	 @Lazy
     IShipInboundShipmentService shipInboundShipmentService;
	 @Autowired
	 @Lazy
	 IShipInboundItemService iShipInboundItemService;
	 @Autowired
	 @Lazy
	 IShipInboundPlanService iShipInboundPlanService;

	 @Autowired
	 IAmazonAuthorityService amazonAuthorityService;

	 @Autowired
	 IShipInboundOperationService iShipInboundOperationService;
	 @Autowired
	 ShipInboundPlanPackingOptionsMapper shipInboundPlanPackingOptionsMapper;
	 @Autowired
	 ShipInboundPlanPackingGroupItemMapper ShipInboundPlanPackingGroupItemMapper;
	 @Autowired
	 ShipInboundShipmentBoxMapper shipInboundShipmentBoxMapper;
	 @Autowired
	 ShipInboundShipmentBoxItemMapper shipInboundShipmentBoxItemMapper;
	 @Autowired
	 @Lazy
	 IShipInboundCaseService iShipInboundCaseService;
public List<ShipInboundBox> findListByPackageGroupid(String formid,String packageGroupid) {
	QueryWrapper<ShipInboundBox> queryWrapper = new QueryWrapper<ShipInboundBox>();
	queryWrapper.eq("formid", formid);
	if(packageGroupid!=null) {
		queryWrapper.eq("packing_group_id", packageGroupid);
	}
	queryWrapper.orderByAsc("boxnum");
	return this.baseMapper.selectList(queryWrapper);
}
public List<ShipInboundBox> findListByShipmentid(String formid,String shipmentid) {
	QueryWrapper<ShipInboundBox> queryWrapper = new QueryWrapper<ShipInboundBox>();
	queryWrapper.eq("formid", formid);
	if(shipmentid!=null) {
		queryWrapper.eq("shipmentid", shipmentid);
		queryWrapper.orderByAsc("boxid");
	}else{
		queryWrapper.orderByAsc("boxnum");
	}
	return this.baseMapper.selectList(queryWrapper);
}

public List<Map<String, Object>> findShipInboundBox(String inboundPlanId) {
	return this.baseMapper.findShipInboundBox(inboundPlanId);
}

public Boolean hasSubmitPackage(ShipInboundPlan inplan) {
	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(inplan.getGroupid(), inplan.getMarketplaceid());
	ShipInboundOperation operation = iShipInboundOperationService.getOperation(auth, inplan.getId(), "setPackingInformation");
	return operation!=null&&(operation.getOperationStatus().equals("SUCCESS"));
}
 
@SuppressWarnings("unchecked")
public Map<String, Object> getBoxDetial(PackingDTO dto)  {
	Map<String, Object> map = new HashMap<String,Object>();
	List<ShipInboundBox> listbox = this.findListByPackageGroupid(dto.getFormid(),dto.getPackingGroupId());
	TreeMap<String, Integer> boxsum = new TreeMap<String, Integer>();
	int sumtotal = 0;
	for(ShipInboundBox box:listbox) {
		box.setCaseListDetail(shipInboundCaseV2Service.findShipInboundCaseByBoxid(box.getId()));
		for (ShipInboundCase boxcase:box.getCaseListDetail()) {
			String key=box.getPackingGroupId()!=null?box.getPackingGroupId():"";
			key=key+box.getBoxnum();
			if (boxsum.get(key) == null) {
				boxsum.put(key, boxcase.getQuantity());
			} else {
				boxsum.put(key,
						boxsum.get(key) + boxcase.getQuantity());
			}
		}
	}


	BigDecimal totalweight = new BigDecimal("0");
	Map<BigDecimal, Object> dem = new HashMap<BigDecimal, Object>();
	for (int i = 0; i < listbox.size(); i++) {
		ShipInboundBox item = listbox.get(i);
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
	map.put("totalBoxNum", totalBoxNum) ;
	map.put("totalBoxSize", totalBoxSize);
	return map;
}

@SuppressWarnings("unchecked")
public Map<String, Object> getShipmentBoxDetial(PackingDTO dto)  {
	Map<String, Object> map = new HashMap<String,Object>();
	List<ShipInboundShipmentBox> listbox = this.findListByShipment(dto.getFormid(),dto.getShipmentid());
	TreeMap<String, Integer> boxsum = new TreeMap<String, Integer>();
	int sumtotal = 0;
	for(ShipInboundShipmentBox box:listbox) {
		box.setCaseListDetail(findShipInboundCaseByBoxid(box.getId()));
		for (ShipInboundShipmentBoxItem boxcase:box.getCaseListDetail()) {
			String key=box.getPackingGroupId()!=null?box.getPackingGroupId():"";
			key=key+box.getBoxnum();
			if (boxsum.get(key) == null) {
				boxsum.put(key, boxcase.getQuantity());
			} else {
				boxsum.put(key,
						boxsum.get(key) + boxcase.getQuantity());
			}
		}
	}
	BigDecimal totalweight = new BigDecimal("0");
	Map<BigDecimal, Object> dem = new HashMap<BigDecimal, Object>();
	for (int i = 0; i < listbox.size(); i++) {
		ShipInboundShipmentBox item = listbox.get(i);
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
	map.put("totalBoxNum",  listbox.size()) ;
	map.put("totalBoxSize", totalBoxSize);
	return map;
}


private List<ShipInboundShipmentBoxItem> findShipInboundCaseByBoxid(String id) {
	// TODO Auto-generated method stub
	LambdaQueryWrapper<ShipInboundShipmentBoxItem> query=new LambdaQueryWrapper<ShipInboundShipmentBoxItem>();
	query.eq(ShipInboundShipmentBoxItem::getBoxid, id);
	return shipInboundShipmentBoxItemMapper.selectList(query);
}

private List<ShipInboundShipmentBox> findListByShipment(String formid, String shipmentid) {
	// TODO Auto-generated method stub
	LambdaQueryWrapper<ShipInboundShipmentBox> query=new LambdaQueryWrapper<ShipInboundShipmentBox>();
	query.eq(ShipInboundShipmentBox::getShipmentid, shipmentid);
	return this.shipInboundShipmentBoxMapper.selectList(query);
}

@Override
public ShipInboundOperation generatePackingOptions(String formid) {
	ShipInboundPlan plan =iShipInboundPlanService.getById(formid);
	if(plan!=null) {
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		GeneratePackingOptionsResponse response = iInboundApiHandlerService.generatePackingOptions(auth, plan.getInboundPlanId());
		if(response!=null) {
			return iShipInboundOperationService.setOperationID(auth,formid,response.getOperationId());
		}else {
			return null;
		}
	}else {
		return null;
	}
}


@Override
public  void  savePackingInformation(ShipCartDTO dto, UserInfo user) {
	LambdaQueryWrapper<ShipInboundBox> query= new LambdaQueryWrapper<ShipInboundBox>();
	query.eq(ShipInboundBox::getFormid, dto.getFormid());
	if(StrUtil.isNotBlank(dto.getShipmentid())) {
		query.eq(ShipInboundBox::getShipmentid, dto.getShipmentid());
		ShipInboundShipment shipment = shipInboundShipmentService.getById(dto.getShipmentid());
		shipment.setBoxtime(new Date());
		shipInboundShipmentService.updateById(shipment);
	}else {
		query.eq(ShipInboundBox::getPackingGroupId, dto.getPackingGroupId());
	}
	List<ShipInboundBox> list = this.baseMapper.selectList(query);
	for(ShipInboundBox box:list) {
		shipInboundCaseV2Service.remove(new LambdaQueryWrapper<ShipInboundCase>().eq(ShipInboundCase::getBoxid,box.getId()));
		this.baseMapper.deleteById(box.getId());
	}
	Map<String,Integer> skuincase=new HashMap<String,Integer>();
	for(ShipInboundBox box:dto.getBoxListDetail()) {
		box.setFormid(dto.getFormid());
		if(StrUtil.isNotBlank(dto.getShipmentid())) {
			box.setShipmentid(dto.getShipmentid());
		}else {
			box.setPackingGroupId(dto.getPackingGroupId());
		}
		box.setOperator(user.getId());
		this.save(box);
		for(ShipInboundCase casebox:box.getCaseListDetail()) {
			casebox.setBoxid(box.getId());
			shipInboundCaseV2Service.save(casebox);
			Integer i=skuincase.get(casebox.getSku());
			if(i==null) {
				i=0;
			}
			i=i+casebox.getQuantity();
			skuincase.put(casebox.getSku(), i);
		}
	}
	

	if(dto.getBoxnum()!=dto.getBoxListDetail().size()) {
		throw new BizException("箱子数量与保存的箱子记录不匹配，请重试或联系管理员");
	}
   return ;
}

 
private void saveOptions(ShipInboundPlan plan,List<PackingOption> options) {
	for(PackingOption option:options) {
		LambdaQueryWrapper<ShipInboundPlanPackingOptions> query=new LambdaQueryWrapper<ShipInboundPlanPackingOptions>();
		if(plan.getInboundPlanId()!=null) {
			query.eq(ShipInboundPlanPackingOptions::getInboundPlanId, plan.getInboundPlanId());
		}else {
			query.eq(ShipInboundPlanPackingOptions::getInboundPlanId, plan.getId());
		}
		query.eq(ShipInboundPlanPackingOptions::getPackingOptionId, option.getPackingOptionId());
		query.eq(ShipInboundPlanPackingOptions::getFormid, plan.getId());
		ShipInboundPlanPackingOptions old = shipInboundPlanPackingOptionsMapper.selectOne(query);
		if(old!=null) {
			old.setFormid(plan.getId());
			old.setContents(JSONUtil.toJsonStr(option));
			shipInboundPlanPackingOptionsMapper.update(old, query);
		}else {
			ShipInboundPlanPackingOptions one=new ShipInboundPlanPackingOptions();
			if(plan.getInboundPlanId()!=null) {
				one.setInboundPlanId( plan.getInboundPlanId());
			}else {
				one.setInboundPlanId(  plan.getId());  
			}
			one.setFormid(plan.getId());
			one.setPackingOptionId(option.getPackingOptionId());
			one.setContents(JSONUtil.toJsonStr(option));
			shipInboundPlanPackingOptionsMapper.insert(one);
		}
		
	}
}
@Override
public Map<String,Object> listPackingOptions(PackingDTO dto) {
	ShipInboundPlan plan = iShipInboundPlanService.getById(dto.getFormid());
	if(plan!=null&&StrUtil.isNotBlank(plan.getInboundPlanId())) {
		if(plan.getAuditstatus()>3) {
			Map<String,Object> map=new HashMap<String,Object>();
			LambdaQueryWrapper<ShipInboundPlanPackingOptions> query=new LambdaQueryWrapper<ShipInboundPlanPackingOptions>();
			if(plan.getInboundPlanId()!=null) {
				query.eq(ShipInboundPlanPackingOptions::getInboundPlanId, plan.getInboundPlanId());
			}else {
				query.eq(ShipInboundPlanPackingOptions::getInboundPlanId, plan.getId());
			}
			query.eq(ShipInboundPlanPackingOptions::getFormid, plan.getId());
			List<ShipInboundPlanPackingOptions> list = shipInboundPlanPackingOptionsMapper.selectList(query);
			List<PackingOption> options = new ArrayList<PackingOption>();
			for(ShipInboundPlanPackingOptions optionitem:list) {
				JSONObject json = JSONUtil.parseObj(optionitem.getContents());
				PackingOption option=JSONUtil.toBean(json, PackingOption.class, false);
				options.add(option);
			}
			saveOptions(plan, options);
			map.put("options", options);
			map.put("pagination", null);
			return map;
		}
		if(plan.getInvtype()==1) {
			Map<String,Object> map=new HashMap<String,Object>();
			List<PackingOption> options = new ArrayList<PackingOption>();
			PackingOption	option=new PackingOption();
			option.setPackingGroups(Arrays.asList(plan.getId()));
			option.setStatus("OFFERED");
			Incentive inv = new Incentive();
			inv.setType("");
			Currency currency=new Currency();
			currency.setAmount(new BigDecimal("0"));
			currency.setCode("RMB");
			inv.setValue(currency);
			List<Incentive> fee=Arrays.asList(inv);
			option.setFees(fee);
			option.setPackingOptionId(plan.getId());
			options.add(option);
			saveOptions(plan, options);
			map.put("options", options);
			map.put("pagination", null);
			return map;
		}
		if(dto.getPageSize()==null) {
		   dto.setPageSize(20);
		}
		AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(plan.getGroupid(), plan.getMarketplaceid());
		ListPackingOptionsResponse response = iInboundApiHandlerService.listPackingOptions(auth, plan.getInboundPlanId(), dto.getPageSize(), dto.getPaginationToken());
		if(response!=null) {
			Map<String,Object> map=new HashMap<String,Object>();
			List<PackingOption> options = response.getPackingOptions();
			saveOptions(plan, options);
			map.put("options", options);
			map.put("pagination", response.getPagination());
			return map;
		}else {
			return null;
		}
	}else {
		return null;
	}
}

public ShipInboundOperation setPackingInformation(ShipInboundPlan plan, UserInfo user, List<ShipInboundItem> itemlist) {
	// TODO Auto-generated method stub
    AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
    Map<String,List<ShipInboundBox>> map=new HashMap<String,List<ShipInboundBox>>();
    Map<String,ShipInboundItem> skumap=new HashMap<String,ShipInboundItem>();
    for(ShipInboundItem item:itemlist) {
    		skumap.put(item.getSku(), item);
    }
    Boolean isshipment=false;
    List<ShipInboundBox> list = this.lambdaQuery().eq(ShipInboundBox::getFormid, plan.getId()).list();
    for(ShipInboundBox item:list) {
    	String key="#";
    	 if(StrUtil.isNotBlank(item.getShipmentid())) {
    		    isshipment=true;
    		    key=item.getShipmentid();
    	    }else if(StrUtil.isNotBlank(item.getPackingGroupId())){
    	    	key=item.getPackingGroupId();
    	    }
    	    List<ShipInboundBox> mlist = map.get(key);
	    	if(mlist==null) {
	    		mlist=new ArrayList<ShipInboundBox>();
	    		map.put(key, mlist);
	    	}
	    	mlist.add(item);
    }
    SetPackingInformationRequest body=new SetPackingInformationRequest();
    List<PackageGroupingInput> pkgroups=new ArrayList<PackageGroupingInput>();
    if(isshipment) {
    	List<ShipInboundShipment> shipments = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getFormid, plan.getId()).list();
    	for(ShipInboundShipment shipment:shipments) {
    		if(!map.containsKey(shipment.getShipmentid())) {
    			throw new BizException("请确保所有货件都保存箱子信息后再提交");
    		}
    	}
    }
    for(Entry<String, List<ShipInboundBox>> entry:map.entrySet()) {
    	 String key=entry.getKey();
    	 PackageGroupingInput e=new PackageGroupingInput();
    	 List<ShipInboundBox>  boxlist=entry.getValue();
    	 if(isshipment) {
    	    	 e.setShipmentId(key);
    	    }else {
    	    	e.setPackingGroupId(key);
    	    }
    	  List<BoxInput> boxes=new LinkedList<BoxInput>();
    	  for(ShipInboundBox box:boxlist) {
    		  BoxInput boxitem=new BoxInput();
    		  Dimensions dim=new Dimensions();
    		  Weight weight=new Weight();
    		  weight.setUnit(UnitOfWeight.fromValue(box.getWunit().toUpperCase()));
    		  weight.setValue(box.getWeight());
			  boxitem.setWeight(weight);
    		  dim.setHeight(box.getHeight());
    		  dim.setLength(box.getLength());
    		  dim.setWidth(box.getWidth());
    		  dim.setUnitOfMeasurement(UnitOfMeasurement.fromValue(box.getUnit().toUpperCase()));
			  boxitem.setDimensions(dim);
    		  boxitem.setQuantity(1);
			  boxitem.setContentInformationSource(BoxContentInformationSource.BOX_CONTENT_PROVIDED);
			  List<ItemInput> items=new LinkedList<ItemInput>();
			  List<ShipInboundCase> caselist=iShipInboundCaseService.lambdaQuery().eq(ShipInboundCase::getBoxid,box.getId()).list();
			  for(ShipInboundCase itemcase:caselist) {
				ItemInput myitem=new ItemInput();
				myitem.setMsku(itemcase.getSku());
				myitem.setQuantity(itemcase.getQuantity());
				ShipInboundItem skuitem = skumap.get(itemcase.getSku());
				myitem.setPrepOwner(PrepOwner.valueOf(skuitem.getPrepOwner()));
				myitem.setLabelOwner(LabelOwner.valueOf(skuitem.getLabelOwner()));
				myitem.setExpiration(GeneralUtil.formatDate(skuitem.getExpiration()));
				myitem.setManufacturingLotCode(null);
				items.add(myitem);
			  }
			  boxitem.setItems(items);
			  boxes.add(boxitem);
    	  }
		  e.setBoxes(boxes);
		  pkgroups.add(e);
    }
	body.setPackageGroupings(pkgroups);
	SetPackingInformationResponse response = iInboundApiHandlerService.setPackingInformation(auth, plan.getInboundPlanId(), body);
	if(response!=null) {
		return iShipInboundOperationService.setOperationID(auth,plan.getId(),response.getOperationId());
	}else {
		return null;
	}
}

 
private  void savePackingGroupItems(PackingDTO dto,ShipInboundPlan plan ,List<Item> items) {
	LambdaQueryWrapper<ShipInboundPlanPackingGroupItem> query=new LambdaQueryWrapper<ShipInboundPlanPackingGroupItem>();
	query.eq(ShipInboundPlanPackingGroupItem::getPackingGroupId, dto.getPackingGroupId());
	query.eq(ShipInboundPlanPackingGroupItem::getPackingOptionId, dto.getPackingOptionId());
	if(plan.getInboundPlanId()!=null) {
		query.eq(ShipInboundPlanPackingGroupItem::getInboundPlanId, plan.getInboundPlanId());
	}else {
		query.eq(ShipInboundPlanPackingGroupItem::getInboundPlanId, plan.getId());
	}
	query.eq(ShipInboundPlanPackingGroupItem::getFormid, plan.getId());
	ShipInboundPlanPackingGroupItemMapper.delete(query);
	for(Item item:items) {
		ShipInboundPlanPackingGroupItem one=new ShipInboundPlanPackingGroupItem();
		one.setFormid(plan.getId());
		if(plan.getInboundPlanId()!=null) {
			one.setInboundPlanId(plan.getInboundPlanId());
		}else {
			one.setInboundPlanId( plan.getId());
		}
	   one.setPackingOptionId(dto.getPackingOptionId());
	   one.setPackingGroupId(dto.getPackingGroupId());
	   one.setSku(item.getMsku());
	   one.setQuantity(item.getQuantity());
	   ShipInboundPlanPackingGroupItemMapper.insert(one);
	}
}
@Override
public Object listPackingGroupItems(PackingDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundPlan plan = iShipInboundPlanService.getById(dto.getFormid());
	if(plan==null) {
		throw new BizException("未找到对应计划");
	}
	if(plan.getAuditstatus()>3 ) {
		LambdaQueryWrapper<ShipInboundPlanPackingGroupItem> query=new LambdaQueryWrapper<ShipInboundPlanPackingGroupItem>();
		query.eq(ShipInboundPlanPackingGroupItem::getPackingGroupId, dto.getPackingGroupId());
		query.eq(ShipInboundPlanPackingGroupItem::getPackingOptionId, dto.getPackingOptionId());
		if(plan.getInboundPlanId()!=null) {
			query.eq(ShipInboundPlanPackingGroupItem::getInboundPlanId, plan.getInboundPlanId());
		}else {
			query.eq(ShipInboundPlanPackingGroupItem::getInboundPlanId, plan.getId());
		}
		query.eq(ShipInboundPlanPackingGroupItem::getFormid, plan.getId());
		List<ShipInboundPlanPackingGroupItem> groupitems = ShipInboundPlanPackingGroupItemMapper.selectList(query);
		if(groupitems!=null&&groupitems.size()>0) {
			ListPackingGroupItemsResponse response =new ListPackingGroupItemsResponse();
			List<Item> items = new ArrayList<Item>();
			for(ShipInboundPlanPackingGroupItem groupitem:groupitems) {
				Item item=new Item();
				item.setMsku(groupitem.getSku());
				item.setQuantity(groupitem.getQuantity());
				items.add(item);
			}
			response.setItems(items);
			return response;
		} 
		
	}
	if(plan.getInvtype()==1) {
		ListPackingGroupItemsResponse response =new ListPackingGroupItemsResponse();
		List<Item> items = new ArrayList<Item>();
		List<ShipInboundItemVo> planitems = iShipInboundItemService.listByFormid(dto.getFormid());
		for(ShipInboundItemVo planitem:planitems) {
			Item item=new Item();
			item.setMsku(planitem.getMsku());
			item.setQuantity(planitem.getConfirmQuantity());
			items.add(item);
		}
		response.setItems(items);
    	savePackingGroupItems( dto, plan ,items);
    	return response;
	}
    AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
    ListPackingGroupItemsResponse response = iInboundApiHandlerService.listPackingGroupItems(auth, 
    		plan.getInboundPlanId(), 
    		dto.getPackingOptionId(), 
    		dto.getPackingGroupId(), 
    		dto.getPageSize(), 
    		dto.getPaginationToken());
    if(response!=null&&response.getItems().size()>0) {
    	List<Item> items = response.getItems();
    	savePackingGroupItems( dto, plan ,items);
    }
    return response;
}

@Override
public ShipInboundOperation confirmPackingOption(PackingDTO dto) {
	// TODO Auto-generated method stub
	ShipInboundPlan plan = iShipInboundPlanService.getById(dto.getFormid());
    AmazonAuthority auth = amazonAuthorityService.getById(plan.getAmazonauthid());
    if(plan.getInvtype()==1) {
    	LambdaQueryWrapper<ShipInboundPlanPackingOptions> query=new LambdaQueryWrapper<ShipInboundPlanPackingOptions>();
		if(plan.getInboundPlanId()!=null) {
			query.eq(ShipInboundPlanPackingOptions::getInboundPlanId, plan.getInboundPlanId());
		}else {
			query.eq(ShipInboundPlanPackingOptions::getInboundPlanId, plan.getId());
		}
		query.eq(ShipInboundPlanPackingOptions::getPackingOptionId, dto.getPackingOptionId());
		query.eq(ShipInboundPlanPackingOptions::getFormid, plan.getId());
		ShipInboundPlanPackingOptions old = shipInboundPlanPackingOptionsMapper.selectOne(query);
		old.setStatus("ACCEPTED");
		shipInboundPlanPackingOptionsMapper.update(old, query);
		ShipInboundOperation result=new ShipInboundOperation();
		result.setOperationStatus("SUCCESS");
		result.setFormid(plan.getId());
    	return result;
    }
	ConfirmPackingOptionResponse response = iInboundApiHandlerService.confirmPackingOption(auth, plan.getInboundPlanId(), dto.getPackingOptionId());
	if(response!=null) {
		return iShipInboundOperationService.setOperationID(auth,dto.getFormid(),response.getOperationId());
	}else {
		return null;
	}
}

@Override
public List<Map<String, Object>> findAllBoxDim(String formid) {
	// TODO Auto-generated method stub
	return this.baseMapper.findAllBox(formid);
}

}
