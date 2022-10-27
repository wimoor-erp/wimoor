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
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersRemoveDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersRemoveVo;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.orders.service.IAmzOrderSolicitationsService;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.amazon.report.service.IReportAmzOrderInvoiceService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.OSSApiService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "订单移除接口")
@RestController
@Slf4j
@RequestMapping("/api/v0/orders/remove")
public class OrdersRemoveController{
	 
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
    @Resource
    IReportAmzOrderInvoiceService iReportAmzOrderInvoiceService;

	@ApiOperation("订单移除列表")
	@PostMapping("/removelist")
	public Result<IPage<AmazonOrdersRemoveVo>> getRemovelistAction(@RequestBody AmazonOrdersRemoveDTO condition) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		String region =condition.getRegion();
		String groupid = condition.getGroupid();
		String startDate =condition.getStartDate();
		String endDate = condition.getEndDate();
		String search = condition.getSearch().trim();
		String searchtype = condition.getSearchtype();
		if(StrUtil.isEmpty(search)) {
			paramMap.put("sku", null);
		}else {
			paramMap.put("sku", search);
		}
		if (StrUtil.isNotEmpty(groupid)) {
			if("all".equals(groupid)) {
				List<AmazonGroup> groupList = amazonGroupService.getGroupByUser(userinfo);
				paramMap.put("groupList", groupList);
			}else {
				paramMap.put("groupid", groupid);
			}
		}
		paramMap.put("searchtype", searchtype);
		if("all".equals(region) || StrUtil.isEmpty(region)) {
			paramMap.put("region", null);
		}else {
			paramMap.put("region", region);
		}
		paramMap.put("startDate", startDate.trim());
		paramMap.put("endDate", endDate.trim());
		paramMap.put("shopid", shopid);
		
		Page<AmazonOrdersRemoveVo> page = condition.getPage();
		IPage<AmazonOrdersRemoveVo> list = orderManagerService.selectRemoveList(paramMap,page);
		return Result.success(list);
	}
	

}

