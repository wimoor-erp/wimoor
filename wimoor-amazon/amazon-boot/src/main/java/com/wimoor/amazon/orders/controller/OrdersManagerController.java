package com.wimoor.amazon.orders.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersDTO;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersShipDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersDetailVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.orders.service.IAmzOrderSolicitationsService;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.OSSApiService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "订单管理接口")
@RestController
@RequestMapping("/api/v0/orders/manager")
public class OrdersManagerController{
	 
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
	/**
	 * @param condition
	 * @return
	 */
	@ApiOperation(value = "获取订单列表")
	@PostMapping("/list")
	public Result<IPage<AmazonOrdersVo>> getOrderlistAction(@RequestBody AmazonOrdersDTO condition) {
		UserInfo userinfo = UserInfoContext.get();
		condition.setOrderid(null);
		if ("sku".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
      		{condition.setSku(condition.getSearch());}
		if ("asin".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
  		{condition.setAsin(condition.getSearch());}
		if ("number".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
  		{condition.setOrderid(condition.getSearch());}else {
  			condition.setOrderid(null);
  		}
		if(StrUtil.isNotEmpty(condition.getSearch())&&condition.getSearch().trim().length()==19) {
			String orderid=condition.getSearch();
			String[] orderidarray= orderid.split("-");
			if(orderidarray.length==3&&orderidarray[0].length()==3&&orderidarray[1].length()==7&&orderidarray[2].length()==7) {
				condition.setOrderid(condition.getSearch().trim());
				condition.setStartDate("1997-01-01");
				condition.setSku(null);
				condition.setAsin(null);
				condition.setEndDate(GeneralUtil.fmtDate(new Date()));
				condition.setShopid(userinfo.getCompanyid());
			}
		}
		
		if (StrUtil.isNotEmpty(condition.getGroupid())) {
			if("all".equals(condition.getGroupid())) {
				List<AmazonGroup> groupList =amazonGroupService.getGroupByUser(userinfo);
				condition.setGroupList(groupList);
				condition.setGroupid(null);
			} 
		}else {
			throw new BizException("店铺ID不能为空！");
		}
		if(StrUtil.isNotEmpty(condition.getPointname()) &&!"all".equals(condition.getPointname())) {
			String pointname=condition.getPointname();
			Map<String, Marketplace> mamap = marketplaceService.findMapByPoint();
			Marketplace market = mamap.get(pointname);
			condition.setMarketplaceid(market.getMarketplaceid());
			if(market!=null) {
				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(condition.getGroupid(), market.getMarketplaceid());
				condition.setAmazonAuthId(auth.getId());
			}
		}else {
			condition.setPointname(null);
		}
 
		condition.setEndDate(condition.getEndDate().trim()+" 23:59:59");
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			condition.setOwner(userinfo.getId());
		} 
		AmazonOrdersDTO archiveCondition = new AmazonOrdersDTO();
		BeanUtil.copyProperties(condition, archiveCondition);
		IPage<AmazonOrdersVo> list=orderManagerService.selectOrderList(condition);
		if(condition.getOrderid()!=null&&list.getRecords().size()==0) {
			archiveCondition.setIsarchive("true");
			list=orderManagerService.selectOrderList(archiveCondition);
		}
		return Result.success(list);
	}
	
	@ApiOperation(value ="下载订单列表")
	@GetMapping("/downloadOrderList")
	public void getDownloadOrderListAction(
			@ApiParam("search内容")@RequestParam String search,
			@ApiParam("店铺ID")@RequestParam String groupid,
			@ApiParam("查询类型")@RequestParam String searchtype,
			@ApiParam("渠道")@RequestParam String channel,
			@ApiParam("开始日期")@RequestParam String startDate,
			@ApiParam("结束日期")@RequestParam String endDate,
			@ApiParam("站点")@RequestParam String pointname,
			@ApiParam("订单状态")@RequestParam String status,
			@ApiParam("产品颜色")@RequestParam String color,
			@ApiParam("是否是企业买家")@RequestParam String isbusiness,
		    HttpServletResponse response) {
		    AmazonOrdersDTO condition=new AmazonOrdersDTO();
		    condition.setSearch(search);
		    condition.setGroupid(groupid);
		    condition.setSearchtype(searchtype);
		    condition.setChannel(channel);
		    condition.setStartDate(startDate);
		    condition.setEndDate(endDate);
		    condition.setPointname(pointname);
		    condition.setStatus(status);
		    condition.setColor(color);
		    condition.setIsbusiness(isbusiness);
		
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			// 将数据写入Excel
			UserInfo userinfo = UserInfoContext.get();
			if(userinfo.isLimit(UserLimitDataType.operations)) {
				condition.setOwner(userinfo.getId());
			} 
			condition.setOrderid(null);
			if ("sku".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
	      		{condition.setSku(condition.getSearch());}
			if ("asin".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
	  		{condition.setAsin(condition.getSearch());}
			if ("number".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
	  		{condition.setOrderid(condition.getSearch());}else {
	  			condition.setOrderid(null);
	  		}
			if(StrUtil.isNotEmpty(condition.getSearch())&&condition.getSearch().trim().length()==19) {
				String orderid=condition.getSearch();
				String[] orderidarray= orderid.split("-");
				if(orderidarray.length==3&&orderidarray[0].length()==3&&orderidarray[1].length()==7&&orderidarray[2].length()==7) {
					condition.setOrderid(condition.getSearch().trim());
					condition.setStartDate("1997-01-01");
					condition.setSku(null);
					condition.setAsin(null);
					condition.setEndDate(GeneralUtil.fmtDate(new Date()));
					condition.setShopid(userinfo.getCompanyid());
				}
			}
			if (StrUtil.isNotEmpty(condition.getGroupid())) {
				if("all".equals(condition.getGroupid())) {
					List<AmazonGroup> groupList =amazonGroupService.getGroupByUser(userinfo);
					condition.setGroupList(groupList);
					condition.setGroupid(null);
				} 
			}
			if(StrUtil.isNotEmpty(condition.getPointname()) ) {
				Marketplace market = marketplaceService.findMapByPoint().get(condition.getPointname());
				condition.setMarketplaceid(market.getMarketplaceid());
				if(market!=null) {
					AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(condition.getGroupid(), market.getMarketplaceid());
					condition.setAmazonAuthId(auth.getId());
				}
			}
			condition.setEndDate(condition.getEndDate().trim()+" 23:59:59");
			try {
				orderManagerService.setOrdersExcelBook(workbook, condition);
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=orderList" + System.currentTimeMillis() + ".xlsx");// 设置文件名
				ServletOutputStream fOut = response.getOutputStream();
				workbook.write(fOut);
				workbook.close();
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	@ApiOperation(value = "获取订单列表汇总数据")
	@PostMapping("/getParamOfSummaryOrder")
	public Result<Map<String,Object>> getParamOfSummaryOrderAction(@RequestBody AmazonOrdersDTO condition) {
		UserInfo userinfo = UserInfoContext.get();
		condition.setOrderid(null);
		if ("sku".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
      		{condition.setSku(condition.getSearch());}
		if ("asin".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
  		{condition.setAsin(condition.getSearch());}
		if ("number".equals(condition.getSearchtype()) && StrUtil.isNotEmpty(condition.getSearch()))
  		{condition.setOrderid(condition.getSearch());}else {
  			condition.setOrderid(null);
  		}
	 
		if (StrUtil.isNotEmpty(condition.getGroupid())) {
			if("all".equals(condition.getGroupid())) {
				List<AmazonGroup> groupList =amazonGroupService.getGroupByUser(userinfo);
				condition.setGroupList(groupList);
				condition.setGroupid(null);
			} 
		}else {
			throw new BizException("店铺ID不能为空！");
		}
		if(StrUtil.isNotEmpty(condition.getPointname()) ) {
			Marketplace market = marketplaceService.findMapByPoint().get(condition.getPointname());
			condition.setMarketplaceid(market.getMarketplaceid());
			if(market!=null) {
				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(condition.getGroupid(), market.getMarketplaceid());
				condition.setAmazonAuthId(auth.getId());
			}
		}
		condition.setEndDate(condition.getEndDate().trim()+" 23:59:59");
		Map<String, Object> maps=orderManagerService.getParamOfSummaryOrder(condition);
		return Result.success(maps);
	}
	
  	
  	

	
	
	@ApiOperation("下载订单发货跟踪列表")
	@PostMapping("/downloadOrderAddressList")
	public void getOrderAddressListAction(
			@RequestBody AmazonOrdersShipDTO condition,HttpServletResponse response)  throws BizException {
		        // 创建新的Excel工作薄
				SXSSFWorkbook workbook = new SXSSFWorkbook();
				// 将数据写入Excel
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
						List<AmazonGroup> groupList = amazonGroupService.getGroupByUser(userinfo);
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
				try {
					orderManagerService.setAddressExcelBook(workbook, paramMap);
					response.setContentType("application/force-download");// 设置强制下载不打开
					response.addHeader("Content-Disposition", "attachment;fileName=orderAddress" + System.currentTimeMillis() + ".xlsx");// 设置文件名
					ServletOutputStream fOut = response.getOutputStream();
					workbook.write(fOut);
					workbook.close();
					fOut.flush();
					fOut.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	 
	
	@ApiOperation("发送review邀请给买家")
	@GetMapping("/sendReviewMessage")
	public Result<Map<String,Object>> sendReviewMessageAction(
			@ApiParam("订单ID")@RequestParam String orderid,
			@ApiParam("站点")@RequestParam String marketplaceid,
			@ApiParam("授权ID")@RequestParam String amazonauthid 
			){
	    AmazonAuthority auth = amazonAuthorityService.getById(amazonauthid);
	    UserInfo userinfo = UserInfoContext.get();
		iAmzOrderSolicitationsService.createProductReviewAndSellerFeedbackSolicitation(userinfo,auth, orderid, marketplaceid);
		return Result.success();
	}
	
	@ApiOperation("查询订单详情")
	@GetMapping("/showOrderDetail")
	public Result<List<AmazonOrdersDetailVo>> showOrderDetailAction(@ApiParam("店铺ID")@RequestParam String groupid,
			@ApiParam("AmazonOrderID")@RequestParam String orderid,
			@ApiParam("订单购买日期")@RequestParam String purchaseDate,
			@ApiParam("是否不加载地址")@RequestParam String nonaddress,
			@ApiParam("是否不财务")@RequestParam String nonfin,
			@ApiParam("深度搜索")@RequestParam String isdeep) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		paramMap.put("purchaseDate", purchaseDate);
		paramMap.put("orderid", orderid);
		paramMap.put("groupid", groupid);
		paramMap.put("shopid", shopid);
		paramMap.put("nonaddress", nonaddress);
		paramMap.put("nonfin", nonfin);
		paramMap.put("isdeep", isdeep);
		List<AmazonOrdersDetailVo> list = orderManagerService.selectOrderDetail(paramMap);
		return Result.success(list);
	}
	
	

}

