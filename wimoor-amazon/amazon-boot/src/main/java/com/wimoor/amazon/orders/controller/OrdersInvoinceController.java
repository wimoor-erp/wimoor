package com.wimoor.amazon.orders.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.orders.service.IAmzOrderSolicitationsService;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.amazon.orders.service.impl.OrderWordHandler;
import com.wimoor.amazon.report.service.IReportAmzOrderInvoiceService;
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
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "订单发票接口")
@RestController
@Slf4j
@RequestMapping("/api/v0/orders/invoince")
public class OrdersInvoinceController{
	 
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
		IPage<AmazonOrdersVo> list=iReportAmzOrderInvoiceService.selectOrderList(condition);
		return Result.success(list);
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
		String feedid=orderManagerService.setAmzOrderVatHandler(userinfo,groupid,country,orderid,itemstatus,postDate,vatlabel,vattype,"normal");
		log.debug(feedid);
		if(StrUtil.isNotBlank(feedid)) {
			map.put("isOk", "true");
			map.put("msg", "提交成功!");
			map.put("feedid",feedid);
		}else {
			map.put("isOk", "fasle");
			map.put("msg", "提交失败!");
			
		}
		return Result.success(map);
	}
	
	
	
	@ApiOperation("查询店铺发票基础信息")
	@GetMapping("/selectVatInfoByGroup")
	public Result<Map<String, Object>> selectVatInfoByGroupAction(@ApiParam("店铺ID")@RequestParam String groupid) {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		Map<String, Object> obj = orderManagerService.selectVatInfo(groupid);
		String vatvoice = null;
		try {
			Calendar c=Calendar.getInstance();
			String year=c.get(Calendar.YEAR)+"";
			vatvoice = serialNumService.findSerialNumber(shopid, year.substring(0,2));
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
			@RequestParam(value="file",required=false)MultipartFile file
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
			if(file!=null) {
				result = orderManagerService.saveAmazonVat(shopid, groupid, Vatcompany, Vatcountry, Vatprovince, Vatcity,
						Vataddress, Vatphone, Vatpostal, Vatemail, Vatsign, image, vatfeeMap,file.getInputStream(),file.getOriginalFilename());
			}else {
				result = orderManagerService.saveAmazonVat(shopid, groupid, Vatcompany, Vatcountry, Vatprovince, Vatcity,
						Vataddress, Vatphone, Vatpostal, Vatemail, Vatsign, image, vatfeeMap,null,null);
			}
			
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
	@GetMapping(value = "/refreshAmzVatInvoinceStatus")
	public Object getAmzVatInvoinceStatusAction(@ApiParam("orderID")@RequestParam String orderid){
		if(StrUtil.isNotEmpty(orderid)) {
			String status = orderManagerService.refreshAmzVatInvoinceStatus(orderid);
			return status;
		}
		return "none";
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
			@ApiParam("退款还是购买")@RequestParam String ordertype,
			@ApiParam("生成的no")@RequestParam String invoiceno,
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
				orderManagerService.setAmzOrderVatInvoicePDF(shopid, document, orderid, language, groupid,
						vatlabel, vattype, country, postDate, itemstatus,ordertype,invoiceno);
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
						vatlabel, vattype, country, postDate, itemstatus,ordertype,invoiceno);
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

