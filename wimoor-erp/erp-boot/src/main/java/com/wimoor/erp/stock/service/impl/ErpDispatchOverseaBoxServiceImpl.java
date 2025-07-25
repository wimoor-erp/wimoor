package com.wimoor.erp.stock.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.mapper.ErpDispatchOverseaBoxMapper;
import com.wimoor.erp.stock.pojo.dto.PackingDTO;
import com.wimoor.erp.stock.pojo.dto.ShipCartDTO;
import com.wimoor.erp.stock.pojo.entity.DispatchForm;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaBox;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaCase;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaForm;
import com.wimoor.erp.stock.service.IDispatchFormService;
import com.wimoor.erp.stock.service.IErpDispatchOverseaBoxService;
import com.wimoor.erp.stock.service.IErpDispatchOverseaCaseService;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.Map.Entry;

@Service("shipInboundBoxV2Service")
public class ErpDispatchOverseaBoxServiceImpl extends  ServiceImpl<ErpDispatchOverseaBoxMapper, ErpDispatchOverseaBox> implements IErpDispatchOverseaBoxService {
	 @Lazy
	 @Autowired
	 IErpDispatchOverseaCaseService shipInboundCaseV2Service;
	@Lazy
	@Autowired
	IErpDispatchOverseaFormService erpDispatchOverseaFormService;
public List<ErpDispatchOverseaBox> findListByPackageGroupid(String formid) {
	QueryWrapper<ErpDispatchOverseaBox> queryWrapper = new QueryWrapper<ErpDispatchOverseaBox>();
	queryWrapper.eq("formid", formid);
	queryWrapper.orderByAsc("boxnum");
	return this.baseMapper.selectList(queryWrapper);
}

public List<ErpDispatchOverseaBox> findListByShipmentid(String formid) {
	QueryWrapper<ErpDispatchOverseaBox> queryWrapper = new QueryWrapper<ErpDispatchOverseaBox>();
	queryWrapper.eq("formid", formid);
	queryWrapper.orderByAsc("boxnum");
	return this.baseMapper.selectList(queryWrapper);
}

public List<Map<String, Object>> findShipInboundBox(String inboundPlanId) {
	return this.baseMapper.findShipInboundBox(inboundPlanId);
}


 
@SuppressWarnings("unchecked")
public Map<String, Object> getBoxDetial(PackingDTO dto)  {
	Map<String, Object> map = new HashMap<String,Object>();
	List<ErpDispatchOverseaBox> listbox = this.findListByPackageGroupid(dto.getFormid());
	TreeMap<String, Integer> boxsum = new TreeMap<String, Integer>();
	int sumtotal = 0;
	for(ErpDispatchOverseaBox box:listbox) {
		box.setCaseListDetail(shipInboundCaseV2Service.findShipInboundCaseByBoxid(box.getId()));
		for (ErpDispatchOverseaCase boxcase:box.getCaseListDetail()) {
			String key=box.getFormid()!=null?box.getFormid():"";
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
		ErpDispatchOverseaBox item = listbox.get(i);
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
	List<ErpDispatchOverseaBox> listbox = this.findListByShipment(dto.getFormid());
	TreeMap<String, Integer> boxsum = new TreeMap<String, Integer>();
	int sumtotal = 0;
	for(ErpDispatchOverseaBox box:listbox) {
		box.setCaseListDetail(findShipInboundCaseByBoxid(box.getId()));
		for (ErpDispatchOverseaCase boxcase:box.getCaseListDetail()) {
			String key=box.getFormid()!=null?box.getFormid():"";
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
		ErpDispatchOverseaBox item = listbox.get(i);
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


private List<ErpDispatchOverseaCase> findShipInboundCaseByBoxid(String id) {
	// TODO Auto-generated method stub
	LambdaQueryWrapper<ErpDispatchOverseaCase> query=new LambdaQueryWrapper<ErpDispatchOverseaCase>();
	query.eq(ErpDispatchOverseaCase::getBoxid, id);
	return this.shipInboundCaseV2Service.list(query);
}

private List<ErpDispatchOverseaBox> findListByShipment(String formid) {
	// TODO Auto-generated method stub
	LambdaQueryWrapper<ErpDispatchOverseaBox> query=new LambdaQueryWrapper<ErpDispatchOverseaBox>();
	query.eq(ErpDispatchOverseaBox::getFormid, formid);
	return this.baseMapper.selectList(query);
}


@Override
public  void  savePackingInformation(ShipCartDTO dto, UserInfo user) {
	LambdaQueryWrapper<ErpDispatchOverseaBox> query= new LambdaQueryWrapper<ErpDispatchOverseaBox>();
	query.eq(ErpDispatchOverseaBox::getFormid, dto.getFormid());
	List<ErpDispatchOverseaBox> list = this.baseMapper.selectList(query);
	for(ErpDispatchOverseaBox box:list) {
		shipInboundCaseV2Service.remove(new LambdaQueryWrapper<ErpDispatchOverseaCase>().eq(ErpDispatchOverseaCase::getBoxid,box.getId()));
		this.baseMapper.deleteById(box.getId());
	}
	Map<String,Integer> skuincase=new HashMap<String,Integer>();
	for(ErpDispatchOverseaBox box:dto.getBoxListDetail()) {
		box.setFormid(dto.getFormid());
		this.save(box);
		for(ErpDispatchOverseaCase casebox:box.getCaseListDetail()) {
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
	if(dto!=null&&dto.getBoxListDetail()!=null){
		ErpDispatchOverseaForm form = erpDispatchOverseaFormService.getById(dto.getFormid());
		form.setBoxnum(dto.getBoxListDetail().size());
		erpDispatchOverseaFormService.updateById(form);
	}
   return ;
}





@Override
public List<Map<String, Object>> findAllBoxDim(String formid) {
	// TODO Auto-generated method stub
	return this.baseMapper.findAllBox(formid);
}

}
