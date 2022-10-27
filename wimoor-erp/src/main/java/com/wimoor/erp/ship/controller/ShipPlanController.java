package com.wimoor.erp.ship.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.inventory.service.IStockCycleService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.ship.pojo.dto.ShipPlanDTO;
import com.wimoor.erp.ship.service.IShipPlanModelService;
import com.wimoor.erp.ship.service.IShipPlanService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "FBA发货规划接口")
@RestController("/api/v1/shipment_plan")
@SystemControllerLog("FBA发货规划")
@RequiredArgsConstructor
public class ShipPlanController   {
	final IShipPlanService shipPlanService;
	final ISerialNumService serialNumService;
	final IWarehouseService warehouseService;
	final IMaterialService materialService;
	final IStepWisePriceService stepWisePriceService;
	final IStockCycleService stockCycleService;
	final IShipPlanModelService shipPlanModelService;
 
	@PostMapping("list")
	public Result<Object> getListData(@RequestBody ShipPlanDTO dto) {
		 UserInfo user=UserInfoContext.get();
		String groupid = dto.getGroupid();
		String warehouseid = dto.getWarehouseid();
		String planid = dto.getPlanid();
		String marketplaceid = dto.getMarketplaceid();
		String color = dto.getColor();
		String search =dto.getSearch();
		String selected =dto.getSelected();
		String status = dto.getStatus();
		String status2 = dto.getStatus2();
		String owner = dto.getOwner();
		String categoryid = dto.getCategoryid();
		String shopid =user.getCompanyid();
		String orderparam = dto.getOrderparam();
		String sortparam =dto.getSortparam();
		String ordercountry = dto.getOrdercountry();
		String issfg = dto.getIssfg();
		String skulist = dto.getSkulist();
		String proname = dto.getProname();
		String remark = dto.getRemark();
		String defoutwarehouseid =dto.getDefoutwarehouseid();
		String samesearch=dto.getSamesearch();
		String searchtype=dto.getSearchtype();
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> skusearch = new ArrayList<String>();
		if (StrUtil.isEmpty(defoutwarehouseid)) {
			defoutwarehouseid = null;
		}
		if(StrUtil.isEmpty(samesearch)) {
			searchtype=null;samesearch=null;
		}
		if (StrUtil.isEmpty(orderparam)) {
			orderparam = null;
		}
		if (StrUtil.isEmpty(ordercountry)) {
			ordercountry = null;
		}
		if (StrUtil.isEmpty(sortparam)) {
			sortparam = null;
		}
		if (StrUtil.isEmpty(owner)) {
			owner = null;
		}
		if (StrUtil.isEmpty(categoryid)) {
			categoryid = null;
		}
		if (StrUtil.isEmpty(issfg)) {
			issfg = null;
		}
		if (StrUtil.isNotEmpty(proname)) {
			proname = "%" + proname + "%";
		} else {
			proname = null;
		}
		if (skulist instanceof String) {
			if (StrUtil.isEmpty(skulist)) {
				skusearch = null;
			} else {
				String subskulist = skulist;
				String[] searchlist = subskulist.split(",");
				if (searchlist != null) {
					for (int i = 0; i < searchlist.length; i++) {
						if (StrUtil.isNotEmpty(searchlist[i])) {
							skusearch.add("%" + searchlist[i] + "%");
						}
					}
				} else {
					skusearch = null;
				}
			}
		} else {
			skulist = null;
		}
		if (StrUtil.isNotEmpty(remark)) {
			remark = "%" + remark + "%";
		} else {
			remark = null;
		}
	 
		if (user.isLimit(UserLimitDataType.owner)) {
			map.put("myself", user.getId());
		}
		map.put("groupid", groupid);
		map.put("shopid", shopid);
		map.put("warehouseid", warehouseid);
		map.put("planid", planid);
		map.put("categoryid", categoryid);
		map.put("color", color);
		map.put("search", search);
		map.put("selected", selected);
		map.put("status", status);
		map.put("status2", status2);
		map.put("orderparam", orderparam);
		map.put("sortparam", sortparam);
		map.put("ordercountry", ordercountry);
		map.put("issfg", issfg);
		map.put("owner", owner);
		map.put("skulist", skusearch);
		map.put("pname", proname);
		map.put("remark", remark);
		map.put("defoutwarehouseid", defoutwarehouseid);
		map.put("userid", user.getId());
		map.put("searchtype", searchtype);
		map.put("samesearch", samesearch);
		if (StrUtil.isEmpty(search))
			map.put("search", null);
		else {
			search = search.trim();
			map.put("search", "%" + search + "%");
		}
		if (StrUtil.isEmpty(color) || "gray".equals(color)) {
			map.put("color", null);
		}
		if (StrUtil.isEmpty(categoryid) || "all".equals(categoryid)) {
			map.put("categoryid", null);
		}
		ArrayList<String> marketplaceList = null;
		if (StrUtil.isNotEmpty(marketplaceid) && !"all".equals(marketplaceid)) {
			marketplaceList = new ArrayList<String>();
			String[] arraymarket = marketplaceid.split(",");
			for (String marketplace : arraymarket) {
				if (StrUtil.isNotEmpty(marketplace))
					marketplaceList.add(marketplace);
			}
			map.put("marketplaceList", marketplaceList);
		}
		ArrayList<String> groupList = null;
		if (StrUtil.isNotEmpty(groupid) && !"all".equals(groupid)) {
			groupList = new ArrayList<String>();
			String[] arraygroup = groupid.split(",");
			for (String group : arraygroup) {
				if (StrUtil.isNotEmpty(group))
					groupList.add(group);
			}
			map.put("groupList", groupList);
		}
		IPage<Map<String, Object>> list = shipPlanService.getPlan(dto.getPage(),map);
		return Result.success(list);
	}
	
}
