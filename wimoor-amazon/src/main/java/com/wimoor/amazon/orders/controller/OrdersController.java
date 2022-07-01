package com.wimoor.amazon.orders.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersDTO;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersRemoveDTO;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersShipDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersDetailVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersRemoveVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersShipVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.amazon.orders.service.impl.OrderWordHandler;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.OSSApiService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

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
@Api(tags = "报表接口")
@RestController
@RequestMapping("/api/v0/orders")
public class OrdersController{
	 
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
	
	@ApiOperation(value = "抓取产品订单")
	@GetMapping("/refreshOrder")
	public Result<String> requestReportAction() {
		amazonAuthorityService.executTask(amzOrderMainService);
		return Result.success();
	}
	 
	@GetMapping("/refreshOrdersItem")
	public Result<?> ordersItemAction() {
		amazonAuthorityService.executTask(amzOrderItemService);
		return Result.success();
	}
	
	
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
		IPage<AmazonOrdersVo> list=orderManagerService.selectOrderList(condition);
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
	
  	@ApiOperation("订单退货列表")
  	@PostMapping("/returnlist")
    public  Result<IPage<AmazonOrdersReturnVo>> getReceiveReportExcel(
    		@RequestBody AmazonOrdersReturnDTO condition){
  		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String shopid =userinfo.getCompanyid();
		String color=condition.getColor();
		String marketplaceid=condition.getMarketplaceid();
		String search=condition.getSearch();
		String searchtype=condition.getSearchtype();
		String groupid=condition.getGroupid();
		String startDate=condition.getStartDate();
		String endDate=condition.getEndDate();
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
		paramMap.put("startDate", startDate.trim());
		paramMap.put("endDate", endDate.trim());
		paramMap.put("shopid", shopid);
		Page<AmazonOrdersReturnVo> page=condition.getPage();
		IPage<AmazonOrdersReturnVo> list=orderManagerService.selectReturnsList(paramMap, page);
		return Result.success(list);
	}
  	
  	

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
	
	@ApiOperation("发票发送至亚马逊")
	@GetMapping("/sendAmzVatInvoince")
	public Result<Map<String,Object>> setOrderVatSourceAction(
			@ApiParam("店铺ID")@RequestParam String groupid,
			@ApiParam("国家编码[de,fr,es,it,uk]")@RequestParam String country,
			@ApiParam("订单ID")@RequestParam String orderid,
			@ApiParam("订单Item状态[itemstatus]")@RequestParam String itemstatus,
			@ApiParam("下单时间")@RequestParam String postDate,
			@ApiParam("发票格式[Word,PDF]")@RequestParam String vatlabel,
			@ApiParam("发票类型[Vat,normal]")@RequestParam String vattype
			){
		UserInfo userinfo = UserInfoContext.get();
		Map<String,Object> map=new HashMap<String, Object>();
		int res=orderManagerService.setAmzOrderVatHandler(userinfo,groupid,country,orderid,itemstatus,postDate,vatlabel,vattype);
		if(res>0) {
			map.put("isOk", "true");
			map.put("msg", "提交成功!");
		}else {
			map.put("isOk", "fasle");
			map.put("msg", "提交失败!");
			
		}
		return Result.success(map);
	}
	
	@ApiOperation("查询订单详情")
	@GetMapping("/showOrderDetail")
	public Result<List<AmazonOrdersDetailVo>> showOrderDetailAction(@ApiParam("店铺ID")@RequestParam String groupid,
			@ApiParam("AmazonOrderID")@RequestParam String orderid,
			@ApiParam("订单购买日期")@RequestParam String purchaseDate) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		paramMap.put("purchaseDate", purchaseDate);
		paramMap.put("orderid", orderid);
		paramMap.put("groupid", groupid);
		paramMap.put("shopid", shopid);
		List<AmazonOrdersDetailVo> list = orderManagerService.selectOrderDetail(paramMap);
		return Result.success(list);
	}
	
	@ApiOperation("查询店铺发票基础信息")
	@GetMapping("/selectVatInfoByGroup")
	public Result<Map<String, Object>> selectVatInfoByGroupAction(@ApiParam("店铺ID")@RequestParam String groupid) {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		Map<String, Object> obj = orderManagerService.selectVatInfo(groupid);
		String vatvoice = null;
		try {
			vatvoice = serialNumService.findSerialNumber(shopid, "22");
			obj.put("vatNo", vatvoice);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!obj.isEmpty()) {
			return Result.success(obj);
		} else {
			return null;
		}
	}
	
	@ApiOperation("编辑发票信息")
	@PostMapping(value="/saveOrderVat",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<Map<String, Object>> saveOrderVatAction(@ApiParam("店铺ID")@RequestParam String groupid,
			@ApiParam("电话")@RequestParam String Vatphone,@ApiParam("公司")@RequestParam String Vatcompany,
			@ApiParam("邮编")@RequestParam String Vatpostal,@ApiParam("国家")@RequestParam String Vatcountry,
			@ApiParam("邮箱")@RequestParam String Vatemail,@ApiParam("省份")@RequestParam String Vatprovince,
			@ApiParam("署名")@RequestParam String Vatsign,@ApiParam("城市")@RequestParam String Vatcity,
			@ApiParam("公司logo")@RequestParam String image,@ApiParam("公司地址")@RequestParam String Vataddress,
			@ApiParam("ukvat") String ukvat,@ApiParam("uknum") String uknum,@ApiParam("devat") String devat,
			@ApiParam("denum") String denum,@ApiParam("frvat") String frvat,@ApiParam("frnum") String frnum,
			@ApiParam("itvat") String itvat,@ApiParam("itnum") String itnum,@ApiParam("esvat") String esvat,
			@ApiParam("esnum") String esnum,
			@RequestParam("file")MultipartFile file
			) throws FileNotFoundException {
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> maps = new HashMap<String, Object>();
		String shopid = userinfo.getCompanyid();
		Map<String, Object> vatfeeMap = new HashMap<String, Object>();
		if (StrUtil.isNotEmpty(ukvat))
			vatfeeMap.put("ukvat", ukvat);
		if (StrUtil.isNotEmpty(uknum))
			vatfeeMap.put("uknum", uknum);
		if (StrUtil.isNotEmpty(devat))
			vatfeeMap.put("devat", devat);
		if (StrUtil.isNotEmpty(denum))
			vatfeeMap.put("denum", denum);
		if (StrUtil.isNotEmpty(frvat))
			vatfeeMap.put("frvat", frvat);
		if (StrUtil.isNotEmpty(frnum))
			vatfeeMap.put("frnum", frnum);
		if (StrUtil.isNotEmpty(esvat))
			vatfeeMap.put("esvat", esvat);
		if (StrUtil.isNotEmpty(esnum))
			vatfeeMap.put("esnum", esnum);
		if (StrUtil.isNotEmpty(itvat))
			vatfeeMap.put("itvat", itvat);
		if (StrUtil.isNotEmpty(itnum))
			vatfeeMap.put("itnum", itnum);
		int result=0;
		try {
			result = orderManagerService.saveAmazonVat(shopid, groupid, Vatcompany, Vatcountry, Vatprovince, Vatcity,
					Vataddress, Vatphone, Vatpostal, Vatemail, Vatsign, image, vatfeeMap,file.getInputStream(),file.getOriginalFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result > 0) {
			maps.put("msg", "操作成功！");
			maps.put("isSuccess", "true");
		} else {
			maps.put("msg", "操作失败！");
			maps.put("isSuccess", "fail");
		}
		return Result.success(maps);
	}
	
	
	@ApiOperation("下载发票信息")
	@GetMapping("/downloadOrderVatInvoice")
	public void downloadOrderVatInvoiceAction(
			@ApiParam("orderID")@RequestParam String orderid,
			@ApiParam("店铺ID")@RequestParam String groupid,
			@ApiParam("选择是pdf还是word文档")@RequestParam String vatlabel,
			@ApiParam("是vat还是普通发票")@RequestParam String vattype,
			@ApiParam("国家")@RequestParam String country,
			@ApiParam("订单状态")@RequestParam String itemstatus,
			@ApiParam("购买日期")@RequestParam String postDate,
			HttpServletResponse response) {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		String language="en";
		response.setContentType("application/force-download");// 设置强制下载不打开
		if ("PDF".equals(vatlabel)) {
			response.addHeader("Content-Disposition", "attachment;fileName=AmzOrderVatInvoicePDF" + System.currentTimeMillis() + ".pdf");// 设置文件名
		} else {
			response.addHeader("Content-Disposition", "attachment;fileName=AmzOrderVatInvoiceWord" + System.currentTimeMillis() + ".doc");// 设置文件名
		}
		if ("PDF".equals(vatlabel)) {
			Document document = new Document(PageSize.A4);
			try {
				// 创建pdf文件
				PdfWriter.getInstance(document, response.getOutputStream());
				Map<String, Object> maps = orderManagerService.setAmzOrderVatInvoicePDF(shopid, document, orderid, language, groupid,
						vatlabel, vattype, country, postDate, itemstatus);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (document != null && document.isOpen()) {
					document.close();
				}
			}
		} else {
			com.lowagie.text.Document document = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
			// 创建word文件
			try {
				RtfWriter2.getInstance(document, response.getOutputStream());
				OrderWordHandler handler=new OrderWordHandler();
				handler.setSerialNumService(this.serialNumService);
				handler.setAmazonAuthorityService(this.amazonAuthorityService);
				handler.setOrderManagerService(this.orderManagerService);
				handler.setMarketplaceService(this.marketplaceService);
				handler.setAmzOrderVatInvoiceWord(shopid, document, orderid, language, groupid,
						vatlabel, vattype, country, postDate, itemstatus);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (document != null && document.isOpen()) {
					document.close();
				}
			}
		}
	}

}

