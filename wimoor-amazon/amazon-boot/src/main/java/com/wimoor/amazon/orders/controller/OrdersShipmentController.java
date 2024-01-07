package com.wimoor.amazon.orders.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersShipDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersShipVo;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.orders.service.IAmzOrderSolicitationsService;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.OSSApiService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "订单发货接口")
@RestController
@RequestMapping("/api/v0/orders/ship")
public class OrdersShipmentController{
	 
	@Autowired
	IAmzOrderMainService amzOrderMainService;
	@Autowired
	IAmzOrderItemService amzOrderItemService;
	@Autowired
	IMarketplaceService marketplaceService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IOrderManagerService orderManagerService;
	@Resource
	IAmazonGroupService amazonGroupService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	IPictureService  pictureService;
	@Resource
	OSSApiService ossApiService;
	@Resource
	IAmzOrderSolicitationsService iAmzOrderSolicitationsService;
 
  	

	@ApiOperation("订单发货跟踪列表")
	@PostMapping("/shiplist")
	public Result<IPage<AmazonOrdersShipVo>> getShipListAction(@RequestBody AmazonOrdersShipDTO condition) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		String color = condition.getColor();
		String marketplaceid = condition.getMarketplaceid();
		String groupid = condition.getGroupid();
		String startDate = condition.getStartDate();
		String endDate = condition.getEndDate();
		String searchtype = condition.getSearchtype();
		String search = condition.getSearch().trim();
		if (StrUtil.isNotEmpty(color))
			paramMap.put("color", color);
		if (StrUtil.isNotEmpty(marketplaceid))
			paramMap.put("marketplaceid", marketplaceid);
		if (searchtype.equals("sku") && StrUtil.isNotEmpty(search))
			paramMap.put("sku", search);
		if (searchtype.equals("asin") && StrUtil.isNotEmpty(search))
			paramMap.put("asin", search);
		if (searchtype.equals("number") && StrUtil.isNotEmpty(search))
			paramMap.put("orderid", search);
		if (StrUtil.isNotEmpty(groupid)) {
			if("all".equals(groupid)) {
				List<AmazonGroup> groupList =amazonGroupService.getGroupByUser(userinfo);
				paramMap.put("groupList", groupList);
			}else {
				paramMap.put("groupid", groupid);
			}
		}
		if(StrUtil.isNotEmpty(marketplaceid)&&StrUtil.isNotEmpty(groupid)) {
              	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid,marketplaceid);
				paramMap.put("amazonauthid", auth.getId());
		}
		paramMap.put("startDate", startDate.trim());
		paramMap.put("endDate", endDate.trim()+" 23:59:59");
		paramMap.put("shopid", shopid);
		Page<AmazonOrdersShipVo> page = condition.getPage();
		IPage<AmazonOrdersShipVo> list = orderManagerService.getOrderAddressList(paramMap,page);
		return Result.success(list);
	}
	
	 

}

