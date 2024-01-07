package com.wimoor.amazon.orders.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.amazon.spapi.api.OrdersV0Api;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.Address;
import com.amazon.spapi.model.orders.BuyerInfo;
import com.amazon.spapi.model.orders.GetOrderAddressResponse;
import com.amazon.spapi.model.orders.GetOrderBuyerInfoResponse;
import com.amazon.spapi.model.orders.GetOrderItemsResponse;
import com.amazon.spapi.model.orders.GetOrderResponse;
import com.amazon.spapi.model.orders.Order;
import com.amazon.spapi.model.orders.Order.OrderStatusEnum;
import com.amazon.spapi.model.orders.OrderAddress;
import com.amazon.spapi.model.orders.OrderBuyerInfo;
import com.amazon.spapi.model.orders.OrderItem;
import com.amazon.spapi.model.orders.OrderItemList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.common.mapper.AmzAmountDescriptionMapper;
import com.wimoor.amazon.common.pojo.entity.AmzAmountDescription;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.feed.mapper.AmzSubmitFeedQueueMapper;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.pojo.entity.Submitfeed;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;
import com.wimoor.amazon.finances.service.IOrdersFinancialService;
import com.wimoor.amazon.orders.mapper.AmzOrderBuyerShipAddressMapper;
import com.wimoor.amazon.orders.mapper.AmzOrderItemMapper;
import com.wimoor.amazon.orders.mapper.AmzOrderMainMapper;
import com.wimoor.amazon.orders.mapper.AmzOrdersInvoiceMapper;
import com.wimoor.amazon.orders.mapper.AmzOrdersInvoiceReportMapper;
import com.wimoor.amazon.orders.mapper.AmzOrdersInvoiceVatMapper;
import com.wimoor.amazon.orders.mapper.AmzOrdersRemarkMapper;
import com.wimoor.amazon.orders.mapper.OrdersReportMapper;
import com.wimoor.amazon.orders.mapper.OrdersSummaryMapper;
import com.wimoor.amazon.orders.mapper.SummaryAllMapper;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderBuyerShipAddress;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderItem;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersInvoice;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersInvoiceReport;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersInvoiceVat;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersRemark;
import com.wimoor.amazon.orders.pojo.entity.OrdersReport;
import com.wimoor.amazon.orders.pojo.entity.OrdersSummary;
import com.wimoor.amazon.orders.pojo.entity.SummaryAll;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersDetailVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersShipVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.report.mapper.AmazonProductSalesMapper;
import com.wimoor.amazon.report.pojo.entity.AmazonProductSales;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

@Service("orderManagerService")
public class OrderManagerServiceImpl implements IOrderManagerService{
	class OrderSum{
		Integer sales;
		BigDecimal price;
		Integer orders;
		String currency;
		String shipCountry;
		public String getCurrency() {return currency;}
		public void setCurrency(String currency) {this.currency = currency;}
		public Integer getSales() {if(sales==null)return 0;return sales;}
		public void setSales(Integer sales) {this.sales = sales;}
		public BigDecimal getPrice() {if(price==null)return new BigDecimal("0");return price;}
		public void setPrice(BigDecimal price) {this.price = price;}
		public Integer getOrders() {if(orders==null)return 0;return orders;}
		public void setOrders(Integer orders) {this.orders = orders;}
		public String getShipCountry() {return shipCountry;}
		public void setShipCountry(String shipCountry) {this.shipCountry = shipCountry;}
	}
	
	@Resource
	OrdersReportMapper ordersReportMapper;
	@Resource
	AmzOrdersRemarkMapper amzOrdersRemarkMapper;
	@Resource
	AmzOrdersInvoiceMapper amzOrdersInvoiceMapper;
	@Resource
	AmzOrdersInvoiceVatMapper amzOrdersInvoiceVatMapper;
	@Resource
	ISerialNumService serialNumService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IMarketplaceService marketplaceService; 
	@Resource
	ISubmitfeedService submitfeedService;
	@Resource
	AmzOrderMainMapper amzOrderMainMapper;
	@Resource
	AmzOrderItemMapper amzOrderItemMapper;
	@Resource
	AmzOrderBuyerShipAddressMapper amzOrderBuyerShipAddressMapper;
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	OrdersSummaryMapper ordersSummaryMapper;
	@Resource
	SummaryAllMapper summaryAllMapper;
	@Resource
	AmazonProductSalesMapper amazonProductSalesMapper;
	@Resource
	IExchangeRateHandlerService exchangeRateHandlerService;
	@Resource
	IPictureService pictureService;
	@Resource
	FileUpload fileUpload;
	@Resource
	AmzSubmitFeedQueueMapper amzSubmitfeedQueueMapper;
	@Autowired
	IOrdersFinancialService ordersFinancialService;
	@Resource
	AmzAmountDescriptionMapper amzAmountDescriptionMapper;
	@Resource
	AmzOrdersInvoiceReportMapper amzOrdersInvoiceReportMapper;
	@Resource
	ProductInfoMapper productInfoMapper;
	@Resource
	AmazonGroupMapper amazonGroupMapper;

 
	
	@Override
	public IPage<AmazonOrdersVo> selectOrderList(AmazonOrdersDTO dto) {
		//一个一个auth反复查询
		IPage<AmazonOrdersVo> result=null;
		if(dto.getAmazonAuthId()==null) {
			if(dto.getGroupList()!=null) {
				if(dto.getMarketplaceid()!=null) {
					List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
					for(AmazonGroup group:dto.getGroupList()) {
						AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(group.getId(), dto.getMarketplaceid());
						dto.setAmazonAuthId(auth.getId());
						addAllQuery(mylist,dto);
					}
					result= getResult(dto,mylist);
				}else {
					//全是不限
					List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
					for(AmazonGroup group:dto.getGroupList()) {
						List<AmazonAuthority> authlist=amazonAuthorityService.selectByGroupId(group.getId());
						for(AmazonAuthority auth:authlist) {
							dto.setAmazonAuthId(auth.getId());
							addAllQuery(mylist,dto);
						}
					}
					result= getResult(dto,mylist);
				}
			}else {
				String groupid = dto.getGroupid();
				List<AmazonAuthority> authlist=amazonAuthorityService.selectByGroupId(groupid);
				List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
				for(AmazonAuthority auth:authlist) {
					dto.setAmazonAuthId(auth.getId());
					addAllQuery(mylist,dto);
				}
				result= getResult(dto,mylist);
				 
			}
		}else {
			List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
			addAllQuery(mylist,dto);
			result= getResult(dto,mylist);
		}
		return result;
	}
	
	private IPage<AmazonOrdersVo> getResult(AmazonOrdersDTO dto, List<AmazonOrdersVo> mylist) {
		// TODO Auto-generated method stub
//		if(GeneralUtil.distanceOfDay(GeneralUtil.getDatez(dto.getEndDate()), new Date())<=2) {
//			mylist.stream().filter(distinctByKey1(s->s.getKey()));
//		}
		IPage<AmazonOrdersVo> result=dto.getListPage(mylist);
		return result;
	}

	private long addAllQuery(List<AmazonOrdersVo> mylist, AmazonOrdersDTO dto) {
		Calendar c=Calendar.getInstance();
		c.add(Calendar.DATE, -3);	
		if(GeneralUtil.distanceOfDay(GeneralUtil.getDatez(dto.getEndDate()), new Date())<=3) {
			AmazonOrdersDTO newdto = new AmazonOrdersDTO();
			BeanUtil.copyProperties(dto, newdto);
			if(GeneralUtil.distanceOfDay(GeneralUtil.getDatez(dto.getStartDate()), new Date())>3) {
				newdto.setStartDate(GeneralUtil.formatDate(c.getTime()));
			}else {
				newdto.setStartDate(dto.getStartDate());
			}
			dto.setEndDate(GeneralUtil.formatDate(c.getTime()));
			List<AmazonOrdersVo> mainlist = ordersReportMapper.selectOrderMainList(newdto);
			if(mainlist!=null&&mainlist.size()>0) {
				mylist.addAll(mainlist);
			}
			dto.setEndDate(GeneralUtil.formatDate(c.getTime()));
		}
		if(StrUtil.isNotBlank(dto.getOrderid())) {
			if(mylist.size()>0) {
				return mylist.size();
			}
		}
		List<AmazonOrdersVo> orderlist = ordersReportMapper.selectOrderList(dto);
		if(orderlist!=null&&orderlist.size()>0) {
			mylist.addAll(orderlist);
		}
		return mylist.size();
	}

	static <T> Predicate<T> distinctByKey1(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	

	public List<AmazonOrdersVo>  getDownloadResult(AmazonOrdersDTO dto,List<AmazonOrdersVo> mylist){
		if(GeneralUtil.distanceOfDay(GeneralUtil.getDatez(dto.getEndDate()), new Date())<=2) {
			mylist.stream().filter(distinctByKey1(s->s.getKey()));
		}
		List<AmazonOrdersVo> result=new LinkedList<AmazonOrdersVo>();
		Collections.sort(mylist, new Comparator<AmazonOrdersVo>() {
			@Override
			public int compare(AmazonOrdersVo o1, AmazonOrdersVo o2) {
				return o2.getBuydate().compareTo(o1.getBuydate());
			}
		});
		if(mylist.size()>0) {
			result.addAll(mylist); 
		}
		return result;
	}
	
	@Override
	public List<AmazonOrdersVo> selectDownloadOrderList(AmazonOrdersDTO dto) {
		//一个一个auth反复查询
		if(dto.getAmazonAuthId()==null) {
			if(dto.getGroupList()!=null) {
				if(dto.getMarketplaceid()!=null) {
					List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
					for(AmazonGroup group:dto.getGroupList()) {
						AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(group.getId(), dto.getMarketplaceid());
						dto.setAmazonAuthId(auth.getId());
						addAllQuery(mylist,dto);
					}
					return getDownloadResult(dto,mylist);
				}else {
					//全是不限
					List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
					for(AmazonGroup group:dto.getGroupList()) {
						List<AmazonAuthority> authlist=amazonAuthorityService.selectByGroupId(group.getId());
						for(AmazonAuthority auth:authlist) {
							dto.setAmazonAuthId(auth.getId());
							addAllQuery(mylist,dto);
						}
					}
					return getDownloadResult(dto,mylist);
				}
			}else {
				String groupid = dto.getGroupid();
				List<AmazonAuthority> authlist=amazonAuthorityService.selectByGroupId(groupid);
				List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
				for(AmazonAuthority auth:authlist) {
					dto.setAmazonAuthId(auth.getId());
					addAllQuery(mylist,dto);
				}
				return getDownloadResult(dto,mylist);
				 
			}
		}else {
			List<AmazonOrdersVo> mylist=new LinkedList<AmazonOrdersVo>();
			addAllQuery(mylist,dto);
			return getDownloadResult(dto,mylist);
		}
	}
	
	@Override
	public Map<String, Object> getParamOfSummaryOrder(AmazonOrdersDTO condition) {
		//订单查询数据汇总
		return null;
	}
	
	public void setOrdersExcelBook(SXSSFWorkbook workbook, AmazonOrdersDTO dto) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("sku", "SKU");
		titlemap.put("asin", "ASIN");
		titlemap.put("groupname", "店铺");
		titlemap.put("market", "站点");
		titlemap.put("orderid", "订单号");
		titlemap.put("channel", "发货方式");
		titlemap.put("buydate", "购买日期");
		titlemap.put("itemprice", "单价");
		titlemap.put("quantity", "数量");
		titlemap.put("orderprice", "订单金额");
		titlemap.put("itemstatus", "状态");
		List<AmazonOrdersVo> list = this.selectDownloadOrderList(dto);
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
			AmazonOrdersVo map = list.get(i);
			for (int j = 0; j < titlearray.length; j++) {
				Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				Object value = null;
				if(titlearray[j].toString().equals("sku")) {
					value=map.getSku();
				}
				if(titlearray[j].toString().equals("asin")) {
					value=map.getAsin();
				}
				if(titlearray[j].toString().equals("groupname")) {
					value=map.getGroupname();
				}
				if(titlearray[j].toString().equals("market")) {
					value=map.getMarketname();
				}
				if(titlearray[j].toString().equals("orderid")) {
					value=map.getOrderid();
				}
				if(titlearray[j].toString().equals("channel")) {
					value=map.getChannel();
				}
				if(titlearray[j].toString().equals("buydate")) {
					value=map.getBuydate();
				}
				if(titlearray[j].toString().equals("itemprice")) {
					value=map.getItemprice();
				}
				if(titlearray[j].toString().equals("quantity")) {
					value=map.getQuantity();
				}
				if(titlearray[j].toString().equals("orderprice")) {
					value=map.getOrderprice();
				}
				if(titlearray[j].toString().equals("itemstatus")) {
					value=map.getItemstatus();
				}
				if (value != null) {
					cell.setCellValue(value.toString());
				}
			}
		}
	}


	@Override
	public IPage<AmazonOrdersShipVo> getOrderAddressList(Map<String, Object> paramMap, Page<AmazonOrdersShipVo> page) {
		return ordersReportMapper.getOrderAddressList(page, paramMap);
	}
	
	public List<Map<String,Object>> getOrderAddressListDownload(Map<String, Object> paramMap) {
		return ordersReportMapper.getOrderAddressListDownload(paramMap);
	}

	public String refreshAmzVatInvoinceStatus(String orderid) {
		AmzOrdersRemark orderRemark = amzOrdersRemarkMapper.selectById(orderid); 
		if(orderRemark!=null) {
			String queueid = orderRemark.getFeedQueueid();
			if(StrUtil.isNotEmpty(queueid)) {
				Submitfeed submitFeed = submitfeedService.GetFeedSubmissionRequest(queueid);
				String status = submitFeed.getFeedProcessingStatus();
				if("_DONE_".equals(status)) {
					status="已处理";
				}else if("_SUBMITTED_".equals(status)) {
					status="已提交";
				}else if("Error".equals(status)) {
					status="异常";
				}else if("_CANCELLED_".equals(status)) {
					status="已取消";
				}else if("_IN_PROGRESS_".equals(status)) {
					status="处理中";
				}else if("_UNCONFIRMED_".equals(status)) {
					status="请求等待中";
				}else {
					status="提交完成";
				}
				return status;
			}else {
				return "none";
			}
		}else {
			return "none";
		}
	}
	
	@Override
	public void setAddressExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("groupname", "店铺名称");
		titlemap.put("marketname", "站点名称");
		titlemap.put("sku", "SKU");
		titlemap.put("asin", "ASIN");
		titlemap.put("name", "产品名称");
		titlemap.put("amazon_order_id", "订单ID");
		titlemap.put("quantity", "购买数量");
		titlemap.put("item_price", "价格");
		titlemap.put("purchase_date", "购买日期");
		titlemap.put("payments_date", "付款日期");
		titlemap.put("shipment_date", "发货日期");
		titlemap.put("estimated_arrival_date", "预计到货日期");
		titlemap.put("buyer_name", "购买人");
		titlemap.put("buyer_email", "邮箱");	
		titlemap.put("recipient_name", "收件人");
		titlemap.put("ship_address_1", "地址1");
		titlemap.put("ship_address_2", "地址2");
		titlemap.put("ship_address_3", "地址3");
		titlemap.put("ship_city", "城市");
		titlemap.put("ship_state", "州");
		titlemap.put("ship_postal_code", "邮编");
		titlemap.put("ship_country", "国家编码");
		titlemap.put("tracking_number", "追踪编码");	

		List<Map<String, Object>> list = getOrderAddressListDownload(param);
		if(list!=null&&list.size()>10000) {
			throw new BizException("数据超过10000笔，无法正常导出，请缩小日期范围");
		}
		Sheet sheet = workbook.createSheet("sheet1");
		Row trow = sheet.createRow(0);
		Cell cell = null;
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
 
		if (list.size() > 0 && list != null) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				Object amazon_order_id = map.get("amazon_order_id");
				param.put("amazon_order_id", amazon_order_id);
				Map<String,Object> orderdetail=null;
				orderdetail=ordersReportMapper.getOrderReportById(param);
				if(orderdetail==null||orderdetail.get("sku")==null) {
					orderdetail=ordersReportMapper.getOrderItemById(param);
				}
				if(orderdetail!=null) {
					map.putAll(orderdetail);
				}
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
				cell = row.createCell(titlearray.length); 
			}
		} 
		
	}

	@SuppressWarnings("unused")
	@Override
	public String setAmzOrderVatHandler(UserInfo userinfo, String groupid, String country, String orderid,
			String itemstatus, String postDate, String vatlabel, String vattype,String ordertype) {

		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		Map<String, Object> map =new HashMap<String, Object>();
		Document document =new Document(PageSize.A4);
		try {
			// 创建pdf文件
			PdfWriter.getInstance(document, baos);
			map = this.setAmzOrderVatInvoicePDF(userinfo.getCompanyid(), document, orderid, null, groupid,
					vatlabel, vattype, country, postDate, itemstatus,ordertype,null);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally {
			if(document != null && document.isOpen()){
				 document.close();
	        }
			 if(baos != null){
	                try {
	                    baos.close();
	                } catch (IOException e) {
	                	e.printStackTrace();
	                }
	         }
		}
		AmzSubmitFeedQueue response = null;
		  Marketplace marketplace = marketplaceService.findMarketplaceByCountry(country);
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplace.getMarketplaceid());
		AmzOrderMain orderMain = getAmzOrderMainById(orderid,auth.getId());
		if(orderMain!=null) {
			String authid = orderMain.getAmazonauthid();
			if(auth!=null) {
				auth.setMarketPlace(marketplace);
				 Map<String, String> feedOptions = new HashMap<String, String>(); // building parameter map
				 feedOptions.put("metadata:OrderId", orderid);
				 feedOptions.put("metadata:TotalAmount", map.get("totalAmount").toString());
				 feedOptions.put("metadata:TotalVATAmount", map.get("totalVat").toString());
				 feedOptions.put("metadata:InvoiceNumber", map.get("invoiceNo").toString());
				 //invoiceNo
				 //VatNo
				 String feedoptions=feedOptions.entrySet().stream().map(e -> String.format("%s=%s", e.getKey(), e.getValue())).collect(Collectors.joining(";"));
				response = submitfeedService.SubmitFeedQueue(baos, orderid, auth, "UPLOAD_VAT_INVOICE", userinfo,feedoptions);
			}
		}
		//插入或者更新到记录表中
		if(response!=null) {
			String queueid = response.getId();
			AmzOrdersRemark order_remark = amzOrdersRemarkMapper.selectById(orderid);
			if(order_remark!=null) {
				order_remark.setFeedQueueid(queueid);
				amzOrdersRemarkMapper.updateById(order_remark);
			}else {
				AmzOrdersRemark orderRemark=new AmzOrdersRemark();
				orderRemark.setAmazonOrderId(orderid);
				orderRemark.setFeedQueueid(queueid);
				orderRemark.setOperator(userinfo.getId());
				orderRemark.setOpttime(new Date());
				orderRemark.setRemark(null);
				amzOrdersRemarkMapper.insert(orderRemark);
			}
			return queueid;
		}else {
			throw new BizException("未正常提交至亚马逊后台，请核验有无漏缺操作！");
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String, Object> setAmzOrderVatInvoicePDF(String shopid,  com.itextpdf.text.Document document, String orderid,
			String language, String groupid, String vatlabel, String vattype, String country, String postDate,
			String itemstatus,String ordertype,String invoiceno) {
		String invoiceNo=null;
		if(StrUtil.isNotEmpty(invoiceno)) {
			invoiceNo=invoiceno;
		}
		Map<String,Object> map=new HashMap<String, Object>();
		// 打印pdf发票
		document.addTitle("Invoice");
		document.addAuthor("wimoor");
		document.addSubject("This is the subject of the PDF file.");
		document.addKeywords("This is the keyword of the PDF file.");
		// step 3
		document.open();
		// 设置中文字体
		BaseFont bfChinese = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String vatvoice = "";
			String dear = "";
			String address1 = "";
			String address2 = "";
			String text4 = "Payment Due Date: ";
			String text6 = "Order ID: " + orderid;
			try {
				text4 += sdf.format(sdf.parse(postDate));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				vatvoice = serialNumService.findSerialNumber(shopid, "19");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 拿invoice表中存在的数据
			Map<String, Object> obj = this.selectVatInfo(groupid);
			AmzOrdersInvoice OrdersInvoice = (AmzOrdersInvoice) obj.get("data1");
			List<AmzOrdersInvoiceVat> VatList = null;
			if (obj.get("data2") != null)
				VatList = (List<AmzOrdersInvoiceVat>) obj.get("data2");
			Float vatRate = null;
			String vatNum = null;
			if (VatList != null) {
				for (int i = 0; i < VatList.size(); i++) {
					AmzOrdersInvoiceVat vatfee = VatList.get(i);
					if (vatfee.getCountry().equals(country)) {
						vatNum = vatfee.getVatNum();
						vatRate = vatfee.getVatRate();
					}
				}
			}
			// 拿order的数据
			AmzOrderMain orderMain = null;
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("orderid", orderid);
			paramMap.put("purchaseDate", postDate);
			paramMap.put("groupid", groupid);
			paramMap.put("shopid", shopid);
			paramMap.put("nonfin","true");
			List<AmazonOrdersDetailVo> listO =this.selectOrderDetail(paramMap);
			if (listO != null && listO.size() > 0) {
				String amazonAuthId = listO.get(0).getAmazonAuthId();
				paramMap.put("amazonAuthId", amazonAuthId);
				String marketplaceId = listO.get(0).getMarketplaceId();
				AmazonAuthority amazonAuthority = amazonAuthorityService.getById(amazonAuthId);
				Marketplace tempmarketplace = marketplaceService.getById(marketplaceId);
				amazonAuthority.setMarketPlace(tempmarketplace);
				orderMain =listO.get(0).getOrderMain();
				}
			// 处理order的数据和费用信息
			if (orderMain != null ) {
				List<AmazonOrdersDetailVo> itemlist = amzOrderItemMapper.selectOrderDetail(paramMap);// 从数据库拿amz_order_item里面的数据
				if (itemlist != null && itemlist.size() > 0) {
					listO = itemlist;
				}
				if (orderMain != null && orderMain.getBuyerAdress() != null) {
					dear +=  orderMain.getBuyerAdress().getName();
					String state = orderMain.getBuyerAdress().getStateOrRegion();
					if (state == null) {
						state = "";
					}
					String adress = orderMain.getBuyerAdress().getAddressLine1();
					if (orderMain.getBuyerAdress().getAddressLine2() != null) {
						adress =adress+","+ orderMain.getBuyerAdress().getAddressLine2();
					}
					if (orderMain.getBuyerAdress().getAddressLine3() != null) {
						adress =adress+","+ orderMain.getBuyerAdress().getAddressLine3();
					}
					address1 += adress + ", " + state;
					address2 += (orderMain.getBuyerAdress().getCity() + ", "
							+ orderMain.getBuyerAdress().getCountryCode() + " \n"
							+ orderMain.getBuyerAdress().getPostalCode());
				} else {
					dear += "--";
					address1 += "--";
					address2 += "--";
				}

			}
			String path = new ClassPathResource("font/arial.ttf").getPath();
			bfChinese = BaseFont.createFont(path, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			// 标题字体风格
			com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(bfChinese, 11, com.itextpdf.text.Font.BOLD);
			// 正文字体风格
			com.itextpdf.text.Font contextFont = new com.itextpdf.text.Font(bfChinese, 9,
					com.itextpdf.text.Font.NORMAL);
			com.itextpdf.text.Font titlefont = new com.itextpdf.text.Font(bfChinese, 14, com.itextpdf.text.Font.BOLD);
			com.itextpdf.text.Font vatfont = new com.itextpdf.text.Font(bfChinese, 18, com.itextpdf.text.Font.BOLD);
			com.itextpdf.text.Font datafont1 = new com.itextpdf.text.Font(bfChinese, 7, com.itextpdf.text.Font.BOLD);
			com.itextpdf.text.Font datafont2 = new com.itextpdf.text.Font(bfChinese, 7, com.itextpdf.text.Font.NORMAL);
			// 段落
			// Paragraph ph = new Paragraph();
			Image img = Image.getInstance(OrdersInvoice.getLogourl());
			img.setAbsolutePosition(0, 0);
			img.setAlignment(Image.ALIGN_CENTER);// 设置图片显示位置
			img.scaleAbsolute(180, 60);// 直接设定显示尺寸
			img.scalePercent(50);// 表示显示的大小为原尺寸的50%
			// img.scalePercent(25, 12);//图像高宽的显示比例
			// img.setRotation(30);//图像旋转一定角度
			// ph.add(img);
			PdfPTable Table = new PdfPTable(2);
			Table.setPaddingTop(10);
			Table.setWidthPercentage(100);
			int width2[] = { 100, 360 };
			Table.setWidths(width2);
			PdfPCell cell = new PdfPCell(img);
			cell.setPadding(15);
			// cell.setwidths(230);
			Table.addCell(cell);
			String vatText = "INVOICE";
			if(!ordertype.equals("normal")) {
				vatText="Credit Note";
			}
			String NoText = "VAT NO:";
			if (vatNum != null) {
				NoText = NoText + vatNum;
				map.put("VatNo", vatNum);
			}
			else
				NoText = NoText + "--";
			String preText="";
			if(!ordertype.equals("normal")) {
				preText="-";
			}
			String element2 = OrdersInvoice.getCompany();
			String element3 = OrdersInvoice.getAddress();
			String text1 = OrdersInvoice.getCity() + "," + OrdersInvoice.getProvince() + ","
					+ OrdersInvoice.getCountry() + "," + OrdersInvoice.getPostalcode();
			String email = OrdersInvoice.getEmail();
			if (StrUtil.isEmpty(email))
				email = "";
			String text2 = OrdersInvoice.getSign();
			String text2_1 = "Paid";
			if(!ordertype.equals("normal")) {
				text2_1="Refunded";
			}
			String text3 = "Invoice NO: " + vatvoice;
			if(!ordertype.equals("normal")) {
				text3="Credit Note: "+ vatvoice;
			}
			if(!ordertype.equals("normal")) {
				text4="Original invoice: "+ invoiceNo;
			}
			String text5 = "Invoice Issue Date: " + GeneralUtil.formatDate(new Date(), "yyyy-MM-dd");
			if(!ordertype.equals("normal")) {
				text5="Credit note date: "+ GeneralUtil.formatDate(new Date(), "yyyy-MM-dd");
			}
			map.put("invoiceNo", vatvoice);
			Paragraph ph2 = new Paragraph(element2);
			Paragraph ph3 = new Paragraph(element3);
			Paragraph phHeader = new Paragraph(vatText);
			Paragraph phNo = null;
			if (vattype.equals("Vat") && vatNum != null) {
				phNo = new Paragraph(NoText);
				phNo.setFont(contextFont);
				phNo.setAlignment(Element.ALIGN_RIGHT);
			}
			Paragraph ph5 = new Paragraph(text1);
			Paragraph ph6 = new Paragraph(text2);
			Paragraph ph7 = new Paragraph(dear);
			Paragraph ph8 = new Paragraph(address1);
			Paragraph ph9 = new Paragraph(address2);
			Paragraph ph9_1 = new Paragraph(text2_1);
			Paragraph ph10 = new Paragraph(text3);
			Paragraph ph11 = new Paragraph(text4);
			Paragraph ph12 = new Paragraph(text5);
			Paragraph ph13 = new Paragraph(text6);
			phHeader.setFont(vatfont);
			phHeader.setAlignment(Element.ALIGN_RIGHT);
			ph2.setFont(titleFont);
			ph2.setAlignment(Element.ALIGN_LEFT);
			ph3.setFont(contextFont);
			ph5.setFont(contextFont);
			ph6.setFont(contextFont);
			ph7.setFont(contextFont);
			ph8.setFont(contextFont);
			ph9.setFont(contextFont);
			ph9_1.setFont(titlefont);
			ph10.setFont(contextFont);
			ph11.setFont(contextFont);
			ph12.setFont(contextFont);
			ph13.setFont(contextFont);
			ph9_1.setAlignment(Element.ALIGN_RIGHT);
			ph10.setAlignment(Element.ALIGN_RIGHT);
			ph11.setAlignment(Element.ALIGN_RIGHT);
			ph12.setAlignment(Element.ALIGN_RIGHT);
			ph13.setAlignment(Element.ALIGN_RIGHT);
			PdfPCell cell2 = new PdfPCell();
			cell2.addElement(phHeader);
			if (phNo != null)
				cell2.addElement(phNo);
			cell2.addElement(ph2);
			if (StrUtil.isNotEmpty(text2))
				cell2.addElement(ph6);
			cell2.addElement(ph3);
			cell2.addElement(ph5);
			cell2.setPadding(10);
			Table.addCell(cell2);
			PdfPCell cell3 = new PdfPCell();
			cell3.setColspan(2);
			cell3.addElement(ph7);
			cell3.addElement(ph8);
			cell3.addElement(ph9);
			cell3.addElement(ph9_1);
			cell3.addElement(ph10);
			cell3.addElement(ph11);
			cell3.addElement(ph12);
			cell3.addElement(ph13);
			cell3.setPadding(10);
			if(!ordertype.equals("normal")) {
				String text6_1="Total payable: "+preText+orderMain.getOrderTotal().toString();
				Paragraph ph13_1= new Paragraph(text6_1);
				ph13_1.setFont(contextFont);
				ph13_1.setAlignment(Element.ALIGN_RIGHT);
				cell3.addElement(ph13_1);	 
			}
			Table.addCell(cell3);
			// 数据表格
			PdfPTable dataTable = new PdfPTable(8);
			int width3[] = null;
			if (vattype.equals("Vat") && vatNum != null) {
				dataTable = new PdfPTable(8);
				width3 = new int[] { 210, 88, 80, 167, 129, 82, 77, 113 };
			} else {
				dataTable = new PdfPTable(6);
				width3 = new int[] { 210, 88, 80, 160, 129, 89 };
			}
			// dataTable.setAutoFillEmptyCells(true); //自动填满
			dataTable.setWidths(width3);
			dataTable.setWidthPercentage(100);
			// 表头设置
			Paragraph phHeader1 = null;
			if(!ordertype.equals("normal")) {
				phHeader1=new Paragraph("Credit note details");
			}else {
				phHeader1=new Paragraph("Product Detail");
			}
			phHeader1.setAlignment(Element.ALIGN_CENTER);
			phHeader1.setFont(datafont1);
			dataTable.addCell(phHeader1);
			Paragraph phHeader2 = new Paragraph("Quantity");
			phHeader2.setAlignment(Element.ALIGN_CENTER);
			phHeader2.setFont(datafont1);
			dataTable.addCell(phHeader2);
			Paragraph phHeader3 = new Paragraph("Price");
			phHeader3.setAlignment(Element.ALIGN_CENTER);
			phHeader3.setFont(datafont1);
			dataTable.addCell(phHeader3);
			Paragraph phHeader4 = new Paragraph("Promotional\nDiscount");
			phHeader4.setAlignment(Element.ALIGN_CENTER);
			phHeader4.setFont(datafont1);
			dataTable.addCell(phHeader4);
			Paragraph phHeader5 = new Paragraph("Shipping Fee");
			phHeader5.setAlignment(Element.ALIGN_CENTER);
			phHeader5.setFont(datafont1);
			dataTable.addCell(phHeader5);
			if (vattype.equals("Vat") && vatNum != null) {
				Paragraph phHeader6 = new Paragraph("Vat Rate");
				phHeader6.setAlignment(Element.ALIGN_CENTER);
				phHeader6.setFont(datafont1);
				dataTable.addCell(phHeader6);
				Paragraph phHeader7 = new Paragraph("VAT");
				phHeader7.setAlignment(Element.ALIGN_CENTER);
				phHeader7.setFont(datafont1);
				dataTable.addCell(phHeader7);
			}
			Paragraph phHeader8 = new Paragraph("Total Price");
			phHeader8.setAlignment(Element.ALIGN_CENTER);
			phHeader8.setFont(datafont1);
			dataTable.addCell(phHeader8);
			// 数据行 循环拉取数据
			DecimalFormat df = new DecimalFormat("######0.00");
			Object currency = "";
			Float totalPrice = 0.00f, totalitemDiscount = 0.00f, totalshipPrice = 0.00f, totalVatFee = 0.00f,
					totalBalance = 0.00f;
			
			if (listO != null && listO.size() > 0) {
				for (int i = 0; i < listO.size(); i++) {
					AmazonOrdersDetailVo item = listO.get(i);
					currency = item.getCurrency();
					if ("USD".equals(currency) || "CAD".equals(currency) || "AUD".equals(currency))
						currency = "$";
					if ("JPY".equals(currency))
						currency = "¥";
					if ("EUR".equals(currency))
						currency = "€";
					if ("GBP".equals(currency))
						currency = "\u00A3";
					if ("INR".equals(currency))
						currency = "R";
					float discount = 0.00f;
					float shipfee = 0.00f;
					String vateText = "--";
					String totalTxt = "--";
					float nums = 0.00f;
					float price = 0f;
					// 不含税价格=售价/(1+vatRate)
					BigDecimal itemprice = new BigDecimal(
							item.getItemprice() == null ? "0" : item.getItemprice().toString());// 单价
					int quantity = Integer
							.parseInt(item.getQuantity() == null ? "0" : item.getQuantity().toString());
					if (itemprice != null && vattype.equalsIgnoreCase("vat") && vatRate != null) {
						nums = itemprice.floatValue();
						price = itemprice.floatValue() / (1 + (vatRate / 100));
						price = (float) (Math.round(price * 1000)) / 1000;
					} else {
						price = itemprice.floatValue();
					}
					totalPrice += price * quantity;
					String vattxt = "";
					if (item.getItemdiscount() != null) {
						discount += Float.parseFloat(item.getItemdiscount().toString());
					}
					if (item.getShipdiscount() != null) {
						discount += Float.parseFloat(item.getShipdiscount().toString());
					}
					totalitemDiscount += discount;
					if (item.getShipprice() != null) {
						shipfee += Float.parseFloat(item.getShipprice().toString());
					}
					totalshipPrice += shipfee;
					if (vatRate != null) {
						vateText = vatRate + "%";
						if (nums > 0) {
							// vat税费=售价*vatRate/(1+vatRate)
							Float rates = (vatRate / 100);
							nums = (nums * rates) / (1 + rates);
							nums = (float) (Math.round(nums * 1000)) / 1000;
							vattxt += currency.toString() + nums;
						} else {
							nums = 0.00f;
							vattxt += currency.toString() + "0.00";
						}
					} else {
						nums = 0.00f;
						vattxt += currency.toString() + "0.00";
					}
					totalVatFee += nums * quantity;
					float totalitemprice = 0.00f;
					if (itemprice != null && itemprice.compareTo(BigDecimal.ZERO) == 1) {
						totalitemprice = itemprice.floatValue() * quantity - discount + shipfee;
						totalTxt = currency + df.format(totalitemprice);
					}
					Paragraph phData1 = new Paragraph(item.getName() == null ? "--" : item.getName().toString());
					phData1.setFont(datafont2);
					dataTable.addCell(phData1);
					Paragraph phData2 = new Paragraph(
							item.getQuantity() == null ? "0" : item.getQuantity().toString());
					phData2.setFont(datafont2);
					dataTable.addCell(phData2);
					Paragraph phData3 = new Paragraph();
					if (itemprice != null) {
						phData3 = new Paragraph(preText+currency + GeneralUtil.removeLastZero(price));
					} else {
						phData3 = new Paragraph(preText+currency + "0.00");
					}
					phData3.setFont(datafont2);
					dataTable.addCell(phData3);
					Paragraph phData4 = new Paragraph();
					if (discount > 0) {
						phData4 = new Paragraph("- " + currency + discount);
					} else {
						phData4 = new Paragraph(preText+currency + "0.00");
					}
					phData4.setFont(datafont2);
					dataTable.addCell(phData4);
					Paragraph phData5 = new Paragraph();
					if (shipfee > 0) {
						phData5 = new Paragraph(preText+currency.toString() + shipfee);
					} else {
						phData5 = new Paragraph(preText+currency + "0.00");
					}
					phData5.setFont(datafont2);
					dataTable.addCell(phData5);
					if (vattype.equals("Vat") && vatNum != null) {
						Paragraph phData6 = new Paragraph(vateText);
						phData6.setFont(datafont2);
						dataTable.addCell(phData6);
						Paragraph phData7 = new Paragraph(preText+currency + GeneralUtil.removeLastZero(nums));
						phData7.setFont(datafont2);
						dataTable.addCell(phData7);
					}
					Paragraph phData8 = new Paragraph(preText+totalTxt);
					phData8.setFont(datafont2);
					dataTable.addCell(phData8);
					dataTable.setPaddingTop(10);
				}
				Paragraph phDataTable = new Paragraph();
				phDataTable.add(dataTable);
				PdfPCell cell4 = new PdfPCell();
				cell4.addElement(phDataTable);
				cell4.setColspan(2);
				Table.addCell(cell4);
			} else {
				// 暂未拉取到数据
				Paragraph nodataPh = new Paragraph("订单详情暂未获取到！请稍后重试！");
				nodataPh.setFont(datafont1);
				PdfPCell cell4 = new PdfPCell(nodataPh);
				cell4.setColspan(2);
				Table.addCell(cell4);
			}
			if (StrUtil.isEmpty(vatNum) || vattype.equals("normal")) {
				totalVatFee = 0f;
			}
			totalPrice = (float) (Math.round(totalPrice * 1000)) / 1000;// 保留3位小数
			totalBalance = totalPrice - totalitemDiscount + totalshipPrice + totalVatFee;
			PdfPCell cell5 = new PdfPCell();
			Paragraph phfooter1 = new Paragraph("Sub Total:   " +preText+ currency + GeneralUtil.removeLastZero(totalPrice));
			phfooter1.setFont(contextFont);
			phfooter1.setAlignment(Element.ALIGN_RIGHT);
			Paragraph phfooter2 = new Paragraph("Shipping Fee:   " +preText+ currency + totalshipPrice);
			phfooter2.setFont(contextFont);
			phfooter2.setAlignment(Element.ALIGN_RIGHT);
			Paragraph phfooter3 = new Paragraph("Promotional Discount:   -" + currency + totalitemDiscount);
			phfooter3.setFont(contextFont);
			phfooter3.setAlignment(Element.ALIGN_RIGHT);
			Paragraph phfooter4 = new Paragraph("VAT Total:   " +preText+ currency + GeneralUtil.removeLastZero(totalVatFee));
			phfooter4.setFont(contextFont);
			phfooter4.setAlignment(Element.ALIGN_RIGHT);
			Paragraph phfooter5 = new Paragraph("Total Amount:   " +preText+ currency + df.format(totalBalance));
			map.put("totalAmount", df.format(totalBalance));
			map.put("totalVat", GeneralUtil.removeLastZero(totalVatFee));
			phfooter5.setFont(contextFont);
			phfooter5.setAlignment(Element.ALIGN_RIGHT);
			cell5.addElement(phfooter1);
			if (totalshipPrice > 0)
				cell5.addElement(phfooter2);
			if (totalitemDiscount > 0)
				cell5.addElement(phfooter3);
			if (vattype.equals("Vat") && vatNum != null)
				cell5.addElement(phfooter4);
			cell5.addElement(phfooter5);
			cell5.setPadding(10);
			cell5.setColspan(2);
			Table.addCell(cell5);
			document.add(Table);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serialNumService.readSerialNumber(shopid, "22");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public Map<String, Object> selectVatInfo(String groupid) {
		Map<String, Object> maps = new HashMap<String, Object>();
		QueryWrapper<AmzOrdersInvoice> queryWrapper=new QueryWrapper<AmzOrdersInvoice>();
		queryWrapper.eq("groupid", groupid);
		List<AmzOrdersInvoice> list1 = amzOrdersInvoiceMapper.selectList(queryWrapper);
		if (list1 != null && list1.size() > 0) {
			maps.put("data1", list1.get(0));
		}
		
		QueryWrapper<AmzOrdersInvoiceVat> vatqueryWrapper=new QueryWrapper<AmzOrdersInvoiceVat>();
		vatqueryWrapper.eq("groupid", groupid);
		List<AmzOrdersInvoiceVat> list2 = amzOrdersInvoiceVatMapper.selectList(vatqueryWrapper);
		List<String> countryArr=new ArrayList<String>();
		countryArr.add("uk");countryArr.add("de");
		countryArr.add("fr");countryArr.add("es");
		countryArr.add("it");
		if (list2 != null && list2.size() > 0) {
			String hascountrys="";
			for(int j=0;j<list2.size();j++) {
				hascountrys+=(list2.get(j).getCountry()+",");
			}
			for(int i=0;i<countryArr.size();i++) {
				String nowcountry=countryArr.get(i);
					if(hascountrys.contains(nowcountry)) {
						continue;
					}
					AmzOrdersInvoiceVat nowVat=new AmzOrdersInvoiceVat();
					nowVat.setCountry(nowcountry);
					nowVat.setGroupid(groupid);
					nowVat.setVatNum("");
					nowVat.setVatRate(null);
					list2.add(nowVat);
			}
			maps.put("data2", list2);
		}else {
			for(int i=0;i<countryArr.size();i++) {
				String nowcountry=countryArr.get(i);
				AmzOrdersInvoiceVat nowVat=new AmzOrdersInvoiceVat();
				nowVat.setCountry(nowcountry);
				nowVat.setGroupid(groupid);
				nowVat.setVatNum("");
				nowVat.setVatRate(null);
				list2.add(nowVat);
			}
			maps.put("data2", list2);
		}
		return maps;
	}
	
    
	@SuppressWarnings("unused")
	public AmzOrderMain saveOrderDetail(String orderid, AmazonAuthority amazonAuthority, String itemstatus, boolean nonaddress) {
		if (itemstatus == null || itemstatus.equals("")) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		boolean needRequest = true;
		// 先查询数据，是否存在order信息
		AmzOrderMain orderMain =this.getAmzOrderMainById(orderid, amazonAuthority.getId());
		QueryWrapper<AmzOrderItem> querywrapper=new QueryWrapper<AmzOrderItem>();
		querywrapper.eq("amazon_order_id", orderid);
		List<AmzOrderItem> orderItemList = amzOrderItemMapper.selectList(querywrapper);
		//&& orderMain.getBuyerShippingAddressId() != null)
		if (orderMain != null && "Shipped".equals(orderMain.getOrderStatus())) {// 数据库存在order,并且订单状态一致
			AmzOrderBuyerShipAddress buyerAdress = amzOrderBuyerShipAddressMapper
					.selectById(orderMain.getAmazonOrderId());
			if(buyerAdress!=null&&buyerAdress.getName()!=null) {
				needRequest = false;
			}
			if(orderItemList==null|| !itemstatus.equals(orderMain.getOrderStatus())){
				orderMain.setHasItem(true);
			}
			orderMain.setBuyerAdress(buyerAdress);
		}
		if (needRequest) {
			// getOrder并保存到数据库
			orderMain = this.requestOrderMain(orderid, amazonAuthority,nonaddress);
		}

		if(orderMain.isHasItem()==true
				||orderItemList.size()==0
				||orderItemList.get(0).getItemPrice()==null
				||orderItemList.get(0).getItemPrice().compareTo(new BigDecimal("0"))==0
				) {
			orderItemList = requestOrderItemByAmzOrderId(orderMain, amazonAuthority);
		}
		if (orderMain != null && orderItemList != null && orderItemList.size() > 0
				&& !itemstatus.equals(orderMain.getOrderStatus())) {
			// 如果更新之后状态仍然不一致，更新order_report状态
			OrdersReport orderreport = new OrdersReport();
			orderreport.setAmazonOrderId(orderMain.getAmazonOrderId());
			orderreport.setPurchaseDate(orderMain.getPurchaseDate());
			orderreport.setSku(orderItemList.get(0).getSku());
			if (orderItemList.get(0).getQuantityordered() != null) {
				orderreport.setQuantity(orderItemList.get(0).getQuantityordered());
			}
			if (orderItemList.get(0).getItemPrice() != null) {
				orderreport.setItemPrice(orderItemList.get(0).getItemPrice());
			}
			orderreport.setItemStatus(orderMain.getOrderStatus());
			orderreport.setOrderStatus(orderMain.getOrderStatus());
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("purchaseDate", orderreport.getPurchaseDate());
			map.put("amazonauthid", orderreport.getAmazonauthid());
			map.put("sku", orderreport.getSku());
			map.put("salesChannel", orderreport.getSalesChannel());
			map.put("amazonOrderId", orderreport.getAmazonOrderId());
			orderMain.setOrderItemList(orderItemList);
			List<OrdersReport> oldrecordlist = ordersReportMapper.selectByOrderIDSKU(map);
			if (oldrecordlist != null && oldrecordlist.size() > 0) {
				this.addOrder(oldrecordlist.get(0), orderreport);
				this.addOrder2(oldrecordlist.get(0), orderreport);
				this.addOrder3(oldrecordlist.get(0), orderreport);
				ordersReportMapper.updateById(orderreport);
			}
		}
		
	 
		 
		AmzOrderBuyerShipAddress addressObj = orderMain.getBuyerAdress();
		if(addressObj!=null ) {
			Date nowdate = new Date();
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(nowdate);
		    calendar.add(Calendar.DATE,-30);
		    nowdate=calendar.getTime(); 
			if(addressObj.getOpttime().before(nowdate)) {
				orderMain.setBuyerAdress(null);
			}
		}else if(orderMain!=null){
			orderMain.setBuyerAdress(null);
		}
	    orderMain.setOrderItemList(orderItemList);
		return orderMain;
	}
	
	public AmzOrderMain requestOrderMain(String orderid, AmazonAuthority amazonAuthority, boolean nonaddress) {
		    AmzOrderMain orderMain =this.getAmzOrderMainById(orderid, amazonAuthority.getId());
		     Boolean isnew=false;
		     if(orderMain==null) {
		    	 isnew=true;
		     }
		     if(orderMain==null||!orderMain.getOrderStatus().equals(OrderStatusEnum.SHIPPED.getValue())) {
		    	 if(orderMain==null||GeneralUtil.distanceOfHour(orderMain.getPurchaseDate(), new Date())>10) {
		    		  Order order = this.requestOrderByAmzOrderId(orderid, amazonAuthority);
		 		      Marketplace marketplace = marketplaceService.selectByPKey(order.getMarketplaceId());
		 				String market = null;
		 				if (marketplace != null) {
		 					market = marketplace.getMarket();
		 				}
		 			 orderMain = new AmzOrderMain(order, market);
		    	 }
		     } 
			 if(orderMain==null||orderMain.getAmazonOrderId()==null) {
				   throw new BizException("抓取订单信息失败，请刷新后重试");
				}
			 AmzOrderBuyerShipAddress oldaddress = amzOrderBuyerShipAddressMapper.selectById(orderMain.getAmazonOrderId());
			try {
				QueryWrapper<AmzOrdersInvoiceReport> queryWrapper=new  QueryWrapper<AmzOrdersInvoiceReport>() ;
				queryWrapper.eq("order_id", orderid);
				List<AmzOrdersInvoiceReport> invoicelist = amzOrdersInvoiceReportMapper.selectList(queryWrapper);
				if(invoicelist!=null&&invoicelist.size()>0) {
					AmzOrderBuyerShipAddress address=oldaddress;
					if(address==null) {
						address =new AmzOrderBuyerShipAddress();
					}
					AmzOrdersInvoiceReport inv=invoicelist.get(0);
					address.setAddressLine1(inv.getShipAddress1());
					orderMain.setBuyerName(inv.getBuyerName());
					orderMain.setBuyerEmail(inv.getBuyerEmail());
					address.setAddressLine2(inv.getShipAddress2());
					address.setAddressLine3(inv.getShipAddress3());
					address.setAmazonauthid(inv.getAmazonAuthId().toString());
					address.setAmazonOrderid(inv.getOrderId());
					address.setCity(inv.getShipCity());
					address.setCountryCode(inv.getShipCountry());
					address.setCounty(inv.getShipCountry());
					address.setPostalCode(inv.getShipPostalCode());
					address.setName(inv.getRecipientName());
					address.setOpttime(new Date());
					address.setPhone(inv.getBuyerPhoneNumber());
					if(oldaddress!=null) {
						amzOrderBuyerShipAddressMapper.updateById(address);
					}else {
						amzOrderBuyerShipAddressMapper.insert(address);
					}
					orderMain.setBuyerAdress(address);
				}
			 if(nonaddress==false) {
			    final String resourcePath = "/orders/v0/orders/"+orderid;
			    final List<String> dataElements = Arrays.asList("buyerInfo","shippingAddress");
				JsonObject responseBodyJson =apiBuildService.getRestrictedData(amazonAuthority, resourcePath, dataElements);
				if (responseBodyJson!=null) {
					JsonObject payload = responseBodyJson.getAsJsonObject("payload");
					if(payload!=null) {
						JsonObject buyerInfo=payload.getAsJsonObject("BuyerInfo");
						Gson gson = new Gson();
						BuyerInfo buyerinfo=gson.fromJson(buyerInfo, BuyerInfo.class);
						if(buyerInfo!=null) {
							orderMain.setBuyerEmail(buyerinfo.getBuyerEmail());
							orderMain.setBuyerName(buyerinfo.getBuyerName());
						}
						JsonObject shippingAddress=payload.getAsJsonObject("ShippingAddress");
						Address address=gson.fromJson(shippingAddress, Address.class);
						AmzOrderBuyerShipAddress buyerAdress = orderMain.getBuyerAdress();
						if (shippingAddress != null) {
							if(buyerAdress==null) {
								buyerAdress=new AmzOrderBuyerShipAddress();
							}
							buyerAdress.setShipAddress(address);
							buyerAdress.setAmazonauthid(amazonAuthority.getId());
							buyerAdress.setAmazonOrderid(orderid);
							buyerAdress.setOpttime(new Date());
							if(oldaddress!=null) {
								amzOrderBuyerShipAddressMapper.updateById(buyerAdress);
							}else {
								amzOrderBuyerShipAddressMapper.insert(buyerAdress);
							}
							orderMain.setBuyerAdress(buyerAdress);
						}
					}
				}
			 }
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		orderMain.setAmazonauthid(amazonAuthority.getId());
		if(isnew==true) {
			amzOrderMainMapper.insert(orderMain);
		}else {
			this.updateAmzOrderMain(orderMain);
		}
		return orderMain;
	}
    public void updateAmzOrderMain(AmzOrderMain order) {
    	LambdaQueryWrapper<AmzOrderMain> query=new LambdaQueryWrapper<AmzOrderMain>();
		query.eq(AmzOrderMain::getAmazonOrderId,order.getAmazonOrderId());
		query.eq(AmzOrderMain::getAmazonauthid,order.getAmazonauthid());
    	amzOrderMainMapper.update(order,query);
    }
    public AmzOrderMain getAmzOrderMainById(String orderid,String amazonauthid) {
    	LambdaQueryWrapper<AmzOrderMain> query=new LambdaQueryWrapper<AmzOrderMain>();
		query.eq(AmzOrderMain::getAmazonOrderId,orderid);
		query.eq(AmzOrderMain::getAmazonauthid,amazonauthid);
		AmzOrderMain orderMain = amzOrderMainMapper.selectOne(query);
		return orderMain;
    }
	@SuppressWarnings("unused")
	private OrderBuyerInfo requestOrderInfoByAmzOrderId(String orderid, AmazonAuthority amazonAuthority) {
		OrdersV0Api ordersApi = apiBuildService.getOrdersV0Api(amazonAuthority);
		try {
			GetOrderBuyerInfoResponse infoRes = ordersApi.getOrderBuyerInfo(orderid);
			OrderBuyerInfo info = infoRes.getPayload();
			if(info!=null) {
				return info;
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Order requestOrderByAmzOrderId(String orderid, AmazonAuthority amazonAuthority) {
		OrdersV0Api ordersApi = apiBuildService.getOrdersV0Api(amazonAuthority);
		try {
			//请求OrderMain
			GetOrderResponse orderRes = ordersApi.getOrder(orderid);
			Order orders = orderRes.getPayload();
			if(orders!=null) {
				return orders;
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private OrderAddress requestOrderAddressByAmzOrderId(String orderid, AmazonAuthority amazonAuthority) {
		OrdersV0Api ordersApi = apiBuildService.getOrdersV0Api(amazonAuthority);
		try {
			//请求OrderMain
		   GetOrderAddressResponse orderAdds = ordersApi.getOrderAddress(orderid);
			  OrderAddress orderAddress = orderAdds.getPayload();
			if(orderAddress!=null) {
				return orderAddress;
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<AmzOrderItem> requestOrderItemByAmzOrderId(AmzOrderMain orderMain, AmazonAuthority amazonAuthority) {
		//请求OrderItem
		if (orderMain == null || orderMain.getAmazonOrderId() == null || amazonAuthority == null) {
			return null;
		}
 		// 查询旧数据
		QueryWrapper<AmzOrderItem> querywrapper=new QueryWrapper<AmzOrderItem>();
		querywrapper.eq("amazon_order_id", orderMain.getAmazonOrderId());
		List<AmzOrderItem> temp = amzOrderItemMapper.selectList(querywrapper);
		if (temp != null && temp.size() > 0) {
			if ("Shipped".equalsIgnoreCase(orderMain.getOrderStatus())) {
				boolean isok = true;
				for (AmzOrderItem item : temp) {
					if (item.getCurrency() == null) {
						isok = false;
						break;
					}
				}
				if (isok) {
					if (!orderMain.isHasItem()) {
						orderMain.setHasItem(true);
						updateAmzOrderMain(orderMain);
					}
					return temp;
				}
			}
			// 清理旧数据
			QueryWrapper<AmzOrderItem> deletequeryWrapper=new QueryWrapper<AmzOrderItem>();
			deletequeryWrapper.eq("amazon_order_id", orderMain.getAmazonOrderId());
			amzOrderItemMapper.delete(deletequeryWrapper);  
		}
		String marketplaceid = orderMain.getMarketplaceid();
		Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
		if (marketplace == null) {
			return null;
		}
		List<AmzOrderItem> result = null;
 		OrdersV0Api ordersApi = apiBuildService.getOrdersV0Api(amazonAuthority);
		GetOrderItemsResponse response=null;
		String amazonOrderId = orderMain.getAmazonOrderId();
		try {
			response = ordersApi.getOrderItems(orderMain.getAmazonOrderId(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (response != null && response.getPayload() != null) {
			OrderItemList itemList = response.getPayload().getOrderItems();
			if (itemList != null && itemList.size() > 0) {
				result = new ArrayList<AmzOrderItem>();
				boolean flag = false;
				for (int i = 0; i < itemList.size(); i++) {
					OrderItem orderitem = itemList.get(i);
					AmzOrderItem item = new AmzOrderItem(orderitem, marketplace.getMarket());
					if (item != null) {
						item.setPurchaseDate(orderMain.getPurchaseDate());
						item.setAmazonOrderId(amazonOrderId);
						item.setMarketplaceid(marketplaceid);
						item.setAmazonauthid(amazonAuthority.getId());
						result.add(item);
						///// 保存到数据库///////////
						querywrapper.eq("orderItemId",item.getOrderitemid());
						AmzOrderItem oldone = amzOrderItemMapper.selectOne(querywrapper);
						if(oldone!=null) {
							amzOrderItemMapper.update(item, querywrapper);
						}else {
							amzOrderItemMapper.insert(item);
						}
						flag = true;
					}
				}
				// 如果orderMain没有Item,将它置为hasItem=true
				if (flag && !orderMain.isHasItem()) {
					orderMain.setHasItem(true);
					updateAmzOrderMain(orderMain);
				}
			}
			// 翻页
			String next = response.getPayload().getNextToken();
			while (next != null) {
				try {
					Thread.sleep(2000);// 恢复速率为2秒一个请求
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				GetOrderItemsResponse response2=null;
				try {
					response2 = ordersApi.getOrderItems(orderMain.getAmazonOrderId(), next);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (response2 != null && response2.getPayload() != null) {
					itemList = response2.getPayload().getOrderItems();
					if (itemList != null && itemList.size() > 0) {
						for (int i = 0; i < itemList.size(); i++) {
							OrderItem orderitem = itemList.get(i);
							AmzOrderItem item = new AmzOrderItem(orderitem, marketplace.getMarket());
							if (item != null) {
								item.setPurchaseDate(orderMain.getPurchaseDate());
								item.setAmazonOrderId(amazonOrderId);
								item.setMarketplaceid(marketplaceid);
								item.setAmazonauthid(amazonAuthority.getId());
								if (result == null) {
									result = new ArrayList<AmzOrderItem>();
								}
								result.add(item);
								///// 保存到数据库///////////
								querywrapper.eq("orderItemId",item.getOrderitemid());
								AmzOrderItem oldone = amzOrderItemMapper.selectOne(querywrapper);
								if(oldone!=null) {
									amzOrderItemMapper.update(item, querywrapper);
								}else {
									amzOrderItemMapper.insert(item);
								}
							}
						}
					}
					next = response2.getPayload().getNextToken();
				}
			}
		}
		return result;
	}

	
	public void addOrder(OrdersReport oldone,OrdersReport newone){
		   Boolean oldNeed=needAdd(oldone);
		   Boolean newNeed=needAdd(newone);
		   OrderSum order =  new OrderSum();
		   if(oldNeed==false&&newNeed==false){
			   return;
		   }
		   else if(oldNeed==false&&newNeed==true){
			   order.setCurrency(newone.getCurrency());
			   order.setOrders(1);
			   order.setPrice(newone.getItemPrice());
			   order.setSales(newone.getQuantity());
			   order.setShipCountry(newone.getShipCountry());
		   }else if(oldNeed==true&&newNeed==false){
			   order.setCurrency(oldone.getCurrency());
			   order.setOrders(-1);
			   order.setPrice(oldone.getItemPrice().multiply(new BigDecimal("-1")));
			   order.setSales(oldone.getQuantity()*-1);
			   order.setShipCountry(oldone.getShipCountry());
		   }else{
			   order.setCurrency(newone.getCurrency());
			   order.setOrders(0);
			   order.setPrice(newone.getItemPrice().subtract(oldone.getItemPrice()));
	           order.setSales(newone.getQuantity()-oldone.getQuantity());
			   order.setShipCountry(newone.getShipCountry());
			   if(order.getSales()==0)return;
		   }
		   String amazonAuthid=newone.getAmazonauthid();
		   String day = GeneralUtil.getStrDate(newone.getPurchaseDate());
		   String marketid=null;
		   if(newone.getSalesChannel()!=null) {
				Map<String, Marketplace> marketmap=marketplaceService.findMapByPoint();
				if (marketmap.get(newone.getSalesChannel()) != null) {
					marketid = marketmap.get(newone.getSalesChannel()).getMarketplaceid();
				}
				if (marketid == null){
					marketid = newone.getSalesChannel();
				}
		   }
		   if(marketid==null){
				marketid = oldone.getSalesChannel();
		   }
		   String sku = newone.getSku();
		   String asin=newone.getAsin();
		   String marketplaceId=marketid;
		   OrdersSummary orderssum = new OrdersSummary();
					     orderssum.setPurchaseDate(GeneralUtil.getDatez(day));
					     orderssum.setSku(sku);
					     orderssum.setAsin(asin);
					     orderssum.setMarketplaceid(marketplaceId);
					     orderssum.setAmazonauthid(amazonAuthid);
	 	   OrdersSummary oldorderssum = ordersSummaryMapper.selectByUniqueKey(orderssum);
			if (oldorderssum == null) {
				orderssum.setOrdersum(order.getOrders());
				orderssum.setQuantity(order.getSales());
				orderssum.setOrderprice(order.getPrice());
				ordersSummaryMapper.insert(orderssum);
			} else {
				orderssum.setId(oldorderssum.getId());
				orderssum.setOrdersum(order.getOrders() + oldorderssum.getOrdersum());
				orderssum.setQuantity(order.getSales() + oldorderssum.getQuantity());
				orderssum.setOrderprice(order.getPrice().add(oldorderssum.getOrderprice()));
				ordersSummaryMapper.updateById(orderssum);
			}

		}
	public Boolean needAdd(OrdersReport record) {
		if (record == null)
			return false;
		if (record.getOrderStatus().equals("Cancelled")) {
			return false;
		}
		return true;
	}

	public void addOrder2(OrdersReport oldone, OrdersReport newone) {
		updateRecord(oldone, "old");
		updateRecord(newone, "new");
	}

	private void updateRecord(OrdersReport one, String type) {
		 if(one!=null){
			   String purchase_date = GeneralUtil.getStrDate(one.getPurchaseDate());
			   String amazonauthid=one.getAmazonauthid();
			   String sales_channel=one.getSalesChannel();
			   String order_status=one.getOrderStatus();
			   String fulfillchannel=one.getFulfillmentChannel();
			   String hasdiscount="1";
			   if(one.getItemPromotionDiscount()!=null){
				   if(one.getItemPromotionDiscount().floatValue()>0){  hasdiscount="0";  }
			   }
			   String is_business_order="false";
			   if(one.getIsBusinessOrder()!=null){  is_business_order=one.getIsBusinessOrder(); }
			 //  String sku=one.getSku();
			  // String asin=one.getAsin();
			   String discount="0";
			   if(one.getItemPromotionDiscount()!=null&&one.getItemPrice()!=null){
				   discount=one.getItemPromotionDiscount().divide(one.getItemPrice(),2,RoundingMode.DOWN).toString();
			   }
			   SummaryAll sumall=new SummaryAll();
				  sumall.setAmazonauthid(amazonauthid);
				  sumall.setPurchaseDate(GeneralUtil.getDatez(purchase_date));
				  sumall.setFulfillChannel(fulfillchannel);
				  sumall.setSalesChannel(sales_channel);
				  if(StrUtil.isNotEmpty(order_status))
				  sumall.setOrderStatus(order_status);
				  if(StrUtil.isNotEmpty(is_business_order))
				  sumall.setIsBusinessOrder(is_business_order);
				  if(StrUtil.isNotEmpty(hasdiscount))
				  sumall.setDiscount(hasdiscount);
				  if(StrUtil.isNotEmpty(discount))
				  sumall.setDiscountAmount(new BigDecimal(discount));
				  QueryWrapper<SummaryAll> queryWrapper=new QueryWrapper<SummaryAll>();
				  queryWrapper.eq("amazonauthid", amazonauthid);
				  queryWrapper.eq("purchase_date", GeneralUtil.getDatez(purchase_date));
				  queryWrapper.eq("fulfillChannel", fulfillchannel);
				  queryWrapper.eq("sales_channel", sales_channel);
				  if(StrUtil.isNotEmpty(order_status))queryWrapper.eq("order_status", sumall.getOrderStatus());
				  if(StrUtil.isNotEmpty(is_business_order))queryWrapper.eq("is_business_order", sumall.getIsBusinessOrder());
				  if(StrUtil.isNotEmpty(hasdiscount))queryWrapper.eq("discount", sumall.getDiscount());
				  if(StrUtil.isNotEmpty(discount))queryWrapper.eq("discount_amount", sumall.getDiscountAmount());
				  List<SummaryAll> oldorderssumlist = summaryAllMapper.selectList(queryWrapper);
				  SummaryAll oldorderssum=null;
				  if(oldorderssumlist.size()==1){ 
					  oldorderssum= oldorderssumlist.get(0);
			      }else if(oldorderssumlist.size()>1){
			    	  for(int i=0;i<oldorderssumlist.size();i++){
			    		  summaryAllMapper.deleteById(oldorderssumlist.get(i));
			    	  }
			      }
				  if(oldorderssum!=null){
					  if("old".equals(type)){
						  sumall.setQuantity(new BigDecimal(oldorderssum.getQuantity()).subtract(new BigDecimal(one.getQuantity())).intValue());
						  sumall.setOrdernumber(oldorderssum.getOrdernumber()-1);
						  sumall.setPrice(oldorderssum.getPrice().subtract(one.getItemPrice()));
						  summaryAllMapper.updateById(sumall);
					  }
					  if("new".equals(type)){
						  sumall.setQuantity(new BigDecimal(oldorderssum.getQuantity()).add(new BigDecimal(one.getQuantity())).intValue());
						  sumall.setOrdernumber(oldorderssum.getOrdernumber()+1);
						  sumall.setPrice(oldorderssum.getPrice().add(one.getItemPrice()));
						  summaryAllMapper.updateById(sumall);
					  }
				  }else{
					  sumall.setQuantity(one.getQuantity());
					  sumall.setOrdernumber(1);
					  sumall.setPrice(one.getItemPrice());
					  summaryAllMapper.insert(sumall);
				  }
		   }
	}
	
	public void addOrder3(OrdersReport oldone, OrdersReport newone) {
		updateRecord3(oldone, "old");
		updateRecord3(newone, "new");
	}

	private void updateRecord3(OrdersReport one, String type) {

		if (one != null) {
			String purchase_date = GeneralUtil.getStrDate(one.getPurchaseDate());
			String amazonauthid = one.getAmazonauthid();
			String sales_channel = one.getSalesChannel();
			String sku = one.getSku();
			String asin = one.getAsin();
			String orderStatus = one.getOrderStatus();
			AmazonProductSales amazonProductSales = new AmazonProductSales();
			amazonProductSales.setAmazonAuthId(amazonauthid);
			amazonProductSales.setAsin(asin);
			amazonProductSales.setSku(sku);
			amazonProductSales.setPurchaseDate(GeneralUtil.getDatez(purchase_date));
			amazonProductSales.setOrderStatus(orderStatus);
			amazonProductSales.setSalesChannel(sales_channel);
			QueryWrapper<AmazonProductSales> queryWrapper=new QueryWrapper<AmazonProductSales>();
			queryWrapper.eq("amazonAuthId", amazonProductSales.getAmazonAuthId());
			queryWrapper.eq("asin", amazonProductSales.getAsin());
			queryWrapper.eq("sku", amazonProductSales.getSku());
			queryWrapper.eq("purchase_date", amazonProductSales.getPurchaseDate());
			queryWrapper.eq("order_status", amazonProductSales.getOrderStatus());
			queryWrapper.eq("sales_channel", amazonProductSales.getSalesChannel());
			AmazonProductSales oldsales = amazonProductSalesMapper.selectOne(queryWrapper);
			if (oldsales != null) {
				if ("old".equals(type)) {
					oldsales.setPrice(oldsales.getPrice().subtract(one.getItemPrice()));
					oldsales.setQuantity(oldsales.getQuantity() - one.getQuantity());
					oldsales.setOrdersum(oldsales.getOrdersum() - 1);
					oldsales.setPricermb(getExchangeRate(oldsales.getSalesChannel(), oldsales.getPrice()));
					amazonProductSalesMapper.updateById(oldsales);
				} else if ("new".equals(type)) {
					oldsales.setPrice(oldsales.getPrice().add(one.getItemPrice()));
					oldsales.setQuantity(oldsales.getQuantity() + one.getQuantity());
					oldsales.setOrdersum(oldsales.getOrdersum() + 1);
					oldsales.setPricermb(getExchangeRate(oldsales.getSalesChannel(), oldsales.getPrice()));
					amazonProductSalesMapper.updateById(oldsales);
				}
			} else {
				amazonProductSales.setPrice(one.getItemPrice());
				amazonProductSales.setQuantity(one.getQuantity());
				amazonProductSales.setOrdersum(1);
				amazonProductSales.setPricermb(getExchangeRate(amazonProductSales.getSalesChannel(), amazonProductSales.getPrice()));
				amazonProductSalesMapper.insert(amazonProductSales);
			}
		}
	}
	
	private BigDecimal getExchangeRate(String point, BigDecimal money) {
		Map<String, Marketplace> map = marketplaceService.findMapByPoint();
		Marketplace market = map.get(point);
		if (market == null) {
			return null;
		}
		return exchangeRateHandlerService.changeCurrencyByLocal(market.getCurrency(), "CNY", money);
	}
	
	public List<AmazonOrdersDetailVo>  selectOrderItemDetail(Map<String, Object> paramMap) {
		return amzOrderItemMapper.selectOrderDetail(paramMap);
	}

	@Override
	public List<AmazonOrdersDetailVo> selectOrderDetail(Map<String, Object> paramMap) {
		boolean isdeep=paramMap.get("isdeep")==null?false:paramMap.get("isdeep").toString().equals("true");
		paramMap.put("isarchive", "false");
		List<AmazonOrdersDetailVo> list = ordersReportMapper.selectOrderDetail(paramMap);
		if(list==null||list.isEmpty()) {
			 paramMap.put("isarchive", "false");
			 list = ordersReportMapper.selectOrderItemDetail(paramMap);
		}
		if(list==null||list.isEmpty()) {
			 paramMap.put("isarchive", "true");
			 list = ordersReportMapper.selectOrderItemDetail(paramMap);
		}
		if(list==null||list.isEmpty()) {
			 paramMap.put("isarchive", "true");
			 list = ordersReportMapper.selectOrderDetail(paramMap);
		}
		if (list != null && list.size() > 0) {
			String amazonAuthId = list.get(0).getAmazonAuthId();
			String marketplaceId = list.get(0).getMarketplaceId();
			AmazonAuthority amazonAuthority = amazonAuthorityService.getById(amazonAuthId);
			Marketplace marketplace = marketplaceService.getById(marketplaceId);
			amazonAuthority.setMarketPlace(marketplace);
			String orderid = paramMap.get("orderid").toString();
			String itemstatus = list.get(0).getItemstatus();
			boolean nonaddress=false;
			 if(paramMap.get("nonaddress")!=null&&paramMap.get("nonaddress").toString().equals("true")) {
				 nonaddress=true;
			 }
			 if(isdeep) {
				 this.amzOrderMainMapper.deleteById(orderid);
				 LambdaQueryWrapper<AmzOrderItem> queryWrapper=new LambdaQueryWrapper<AmzOrderItem>();
				 queryWrapper.eq(AmzOrderItem::getAmazonOrderId, orderid);
				this.amzOrderItemMapper.delete(queryWrapper);
			 }
			 AmzOrderMain orderMain = this.saveOrderDetail(orderid, amazonAuthority, itemstatus,nonaddress);
				if (orderMain != null && orderMain.getOrderItemList() != null) {
					paramMap.put("amazonAuthId", amazonAuthId);
					List<AmazonOrdersDetailVo> list_item = amzOrderItemMapper.selectOrderDetail(paramMap);// 从数据库拿amz_order_item里面的数据
					if (list_item != null && list_item.size() > 0) {
						list = list_item;
					}
		    }
			for (AmazonOrdersDetailVo map : list) {
				String itemtax = map.getItemtax().toString();
				String region = map.getRegion();
				Object totalprice = map.getTotalprice();
				if (("EU".equals(region)||"UK".equals(region))&& Float.valueOf(itemtax) == 0.0 && totalprice!=null) {
					if(map.getVatRate()!=null) {
						String vat_rate = map.getVatRate().toString();
						float rate = Float.valueOf(vat_rate) / 100;
						float number = (Float.valueOf(totalprice.toString()) * rate) / (1 + rate);
						map.setItemtax(new BigDecimal(GeneralUtil.formatterNum(number)).setScale(2, RoundingMode.HALF_UP));
					}
					
				}
			}
			if (orderMain != null) {
				list.get(0).setOrderprice(orderMain.getOrderTotal());
				list.get(0).setOrderstatus(orderMain.getOrderStatus());
				if (orderMain.getBuyerAdress() != null) {
					list.get(0).setAddressName(orderMain.getBuyerAdress().getName());
					String adress = orderMain.getBuyerAdress().getAddressLine1();
					if (adress == null) {
						adress = orderMain.getBuyerAdress().getAddressLine2();
					}
					if (adress == null) {
						adress = orderMain.getBuyerAdress().getAddressLine3();
					}
					list.get(0).setBuyername(orderMain.getBuyerAdress().getName());
					list.get(0).setBuyeremail(orderMain.getBuyerAdress().getName());
					list.get(0).setAddressLine(adress);
					list.get(0).setAddressState(orderMain.getBuyerAdress().getStateOrRegion());
					list.get(0).setAddressCity(orderMain.getBuyerAdress().getCity());
					list.get(0).setAddressCountry(orderMain.getBuyerAdress().getCountryCode());
					list.get(0).setAddressPostal(orderMain.getBuyerAdress().getPostalCode());
					list.get(0).setHasAddress("has");
				}else {
					list.get(0).setHasAddress("nohas");
				}
				list.get(0).setOrderMain(orderMain);
				boolean nonfin=false;
				 if(paramMap.get("nonfin")!=null&&paramMap.get("nonfin").toString().equals("true")) {
					 nonfin=true;
				 }
				if(nonfin==false) {
					List<OrdersFinancial> financialList = ordersFinancialService.getOrdersFinancialList(amazonAuthority,orderid);
					if(financialList!=null) {
						BigDecimal financialfee=new BigDecimal("0");
						for(int i=0;i<financialList.size();i++) {
							OrdersFinancial fin = financialList.get(i);
							String ename = fin.getFtype();
							QueryWrapper<AmzAmountDescription> queryWrapper=new QueryWrapper<AmzAmountDescription>();
							queryWrapper.eq("ename", ename);
							AmzAmountDescription description = amzAmountDescriptionMapper.selectOne(queryWrapper);
							if(description!=null) {
								fin.setMarketplaceId(description.getCname());
							}else {
								fin.setMarketplaceId(ename);
							}
							financialfee=financialfee.add(fin.getAmount());
						}
						list.get(0).setFinancialList(financialList);
						list.get(0).setFinancialfee(financialfee);
					}
				}
		
			}
			return list;
		} else {
			return null;
		}
	
	}

	@Override
	public int saveAmazonVat(String shopid, String groupid, String vatcompany, String vatcountry, String vatprovince,
			String vatcity, String vataddress, String vatphone, String vatpostal, String vatemail, String vatsign,
			String image, Map<String, Object> vatfeeMap,InputStream stream,String filename) {

		AmzOrdersInvoice invoice = new AmzOrdersInvoice();
		Boolean isSave = true;
		QueryWrapper<AmzOrdersInvoice> queryWrapper=new QueryWrapper<AmzOrdersInvoice>();
		queryWrapper.eq("groupid", groupid);
		List<AmzOrdersInvoice> invoicelist = amzOrdersInvoiceMapper.selectList(queryWrapper);
		if (invoicelist.size() > 0 && invoicelist != null) {
			invoice = invoicelist.get(0);
			isSave = false;
		}
		Picture picture =null;
		try {
			if(StrUtil.isNotEmpty(filename)&&stream!=null) {
				String filePath = PictureServiceImpl.logoImgPath + shopid;
				
				int len = filename.lastIndexOf(".");
				String filenames = filename.substring(0, len);
				String imgtype=filename.substring(len, filename.length());
				filename=filenames+System.currentTimeMillis()+imgtype;
				picture = pictureService.uploadPicture(stream, null, filePath, filename, invoice.getImage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		invoice.setAddress(vataddress);
		invoice.setCity(vatcity);
		invoice.setCompany(vatcompany);
		invoice.setCountry(vatcountry);
		invoice.setEmail(vatemail);
		if (isSave == true)
			invoice.setGroupid(groupid);
		invoice.setPhone(vatphone);
		invoice.setPostalcode(vatpostal);
		invoice.setProvince(vatprovince);
		if(picture!=null) {
			invoice.setImage(picture.getId());
			invoice.setLogourl(fileUpload.getPictureImage(picture.getLocation()));
		}
		if (StrUtil.isNotEmpty(vatsign))
			invoice.setSign(vatsign);
		int result = 0;
		if (isSave == true) {
			// 保存
			result = amzOrdersInvoiceMapper.insert(invoice);
		} else {
			// 更新
			result = amzOrdersInvoiceMapper.updateById(invoice);
		}
		// vat表的操作
		if (!vatfeeMap.isEmpty()) {
			for (String str : vatfeeMap.keySet()) {
				AmzOrdersInvoiceVat vat = amzOrdersInvoiceVatMapper.selectbyGroupAndCountry(groupid,
						str.substring(0, 2));
				if(vatfeeMap.get(str)==null||StrUtil.isBlankOrUndefined(vatfeeMap.get(str).toString()))continue;
				if (vat == null) {
					// 保存
					vat = new AmzOrdersInvoiceVat();
					vat.setCountry(str.substring(0, 2));
					vat.setGroupid(groupid);
					if (str.indexOf("vat") >= 0)
						vat.setVatNum(vatfeeMap.get(str).toString());
					if (str.indexOf("num") >= 0)
						vat.setVatRate(Float.parseFloat(vatfeeMap.get(str).toString()));
					amzOrdersInvoiceVatMapper.insert(vat);
				} else {
					// 更新
					vat.setCountry(str.substring(0, 2));
					if (str.indexOf("vat") >= 0)
						vat.setVatNum(vatfeeMap.get(str).toString());
					if (str.indexOf("num") >= 0)
						vat.setVatRate(Float.parseFloat(vatfeeMap.get(str).toString()));
					amzOrdersInvoiceVatMapper.updateById(vat);
				}
			}
		}
		return result;
	
	}

	public List<OrdersFinancial> lastShippedOrderFin(AmazonAuthority auth, ProductInfo info) {
		// TODO Auto-generated method stub
		String marketpalceid=info.getMarketplaceid();
	 
		Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketpalceid);
		String amazonOrderId=ordersReportMapper.getLastShippedOrder(auth.getId(),market.getPointName(),info.getSku());
		
		Marketplace marketplace = marketplaceService.getById(info.getMarketplaceid());
		auth.setMarketPlace(marketplace);
		if(StrUtil.isEmpty(amazonOrderId)) {
			return null;
		}
		List<OrdersFinancial> financialList = ordersFinancialService.getOrdersFinancialList(auth,amazonOrderId);
        return financialList;
	}


	 
	
	


	
	
}
