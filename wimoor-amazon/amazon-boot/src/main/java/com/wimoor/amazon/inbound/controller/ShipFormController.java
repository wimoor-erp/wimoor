package com.wimoor.amazon.inbound.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazon.spapi.model.fulfillmentinbound.GetInboundGuidanceResult;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;
import com.amazon.spapi.model.fulfillmentinbound.PrepInstruction;
import com.amazon.spapi.model.fulfillmentinbound.PrepOwner;
import com.amazon.spapi.model.fulfillmentinbound.SKUPrepInstructions;
import com.amazon.spapi.model.fulfillmentinbound.ShipmentStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.dto.SalesChartDTO;
import com.wimoor.amazon.common.pojo.vo.Chart;
import com.wimoor.amazon.common.pojo.vo.ChartLine;
import com.wimoor.amazon.inbound.pojo.dto.ProductLabelDownloadDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipCartShipmentDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundItemDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmenSummaryDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundTransDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipTransDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.amazon.util.ChartPoint;
import com.wimoor.amazon.util.ChartPoint.SumType;
import com.wimoor.amazon.util.HttpDownloadUtil;
import com.wimoor.amazon.util.LockCheckUtils;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货订单接口")
@RestController
@RequestMapping("/api/v1/shipForm")
@SystemControllerLog("发货订单")
@RequiredArgsConstructor
public class ShipFormController {
	
	final IShipInboundItemService shipInboundItemService;
	final IShipInboundShipmentService shipInboundShipmentService;
	final ISerialNumService serialNumService;
    final IShipInboundPlanService shipInboundPlanService;
    final IShipInboundTransService shipInboundTransService;
    final IProductInfoService productInfoService;
    final IMarketplaceService marketplaceService;
	final IFulfillmentInboundService iFulfillmentInboundService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IShipAddressToService shipAddressToService;
	final IShipAddressService shipAddressService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IProductInOptService productInOptService;
	final IAmazonGroupService iAmazonGroupService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	@ApiOperation(value = "获取所有本地货件列表")
	@PostMapping("/list")
	public Result<IPage<ShipInboundShipmenSummarytVo>> findByTraceConditionAction(@ApiParam("配货单列表申请")@RequestBody ShipInboundShipmenSummaryDTO dto) {
		    UserInfo user=UserInfoContext.get();
		    dto.setAuditstatus(StrUtil.isBlank(dto.getAuditstatus())?null:dto.getAuditstatus());
		    dto.setShipmentid( StrUtil.isBlank(dto.getShipmentid())?null:dto.getShipmentid());
		    dto.setMarketplaceid(StrUtil.isBlank(dto.getMarketplaceid())?null:dto.getMarketplaceid());
		    dto.setGroupid( StrUtil.isBlank(dto.getGroupid())?null:dto.getGroupid());
		    dto.setWarehouseid( StrUtil.isBlank(dto.getWarehouseid())?null:dto.getWarehouseid());
		    dto.setCompany( StrUtil.isBlank(dto.getCompany())?null:dto.getCompany());
		    dto.setChannel(StrUtil.isBlank(dto.getChannel())?null:dto.getChannel());
		    dto.setTranstype( StrUtil.isBlank(dto.getTranstype())?null:dto.getTranstype());
		    dto.setSearchtype(StrUtil.isBlank(dto.getSearchtype())?null:dto.getSearchtype());
		    dto.setSearch( StrUtil.isBlank(dto.getSearch())?null:dto.getSearch());
		    dto.setFromdate( StrUtil.isBlank(dto.getFromdate())?null:dto.getFromdate()+" 00:00:00.000");
		    dto.setEnddate( StrUtil.isBlank(dto.getEnddate())?null:dto.getEnddate()+" 23:59:59.999");
		    dto.setHasexceptionnum(StrUtil.isBlank(dto.getHasexceptionnum())?null:dto.getHasexceptionnum());
		    dto.setShopid( user.getCompanyid());
		    if(StrUtil.isNotEmpty(dto.getSearch()) && dto.getSearch().indexOf("FBA")==0) {
		    	dto.setSearch(dto.getSearch());
		    	dto.setSearchtype("number");
		    	dto.setMarketplaceid(null);
		    	dto.setGroupid(null);
		    	dto.setWarehouseid(null);
		    	dto.setFromdate(null);
		    	dto.setEnddate(null);
		    }
		    if(dto.getGroupid()==null) {
		    	if(user.getGroups()!=null&&user.getGroups().size()>0) {
		    		dto.setGroupList(user.getGroups());
		    	}
		    }
		    IPage<ShipInboundShipmenSummarytVo> shiplist=shipInboundShipmentService.findByTraceCondition(dto);
	        return Result.success(shiplist);
	    }
	
	@ApiOperation(value = "货件忽略异常")
	@GetMapping("/ignoreShipment")
	public Result<String> ignoreShipmentAction(String shipmentid) {
		UserInfo user=UserInfoContext.get();
		shipInboundShipmentService.ignoreShipment(user,shipmentid);
		return Result.success();
	}
	
	@ApiOperation(value = "校验货件SKU")
	@GetMapping("/guidance")
	public Result<Map<String,Object>> getInboundGuidanceAction(@ApiParam("店铺ID") @RequestParam String groupid, 
			                                            @ApiParam("站点ID") @RequestParam String marketplaceid, 
			                                            @ApiParam("skulist") @RequestParam String skulist){
		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
		Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
		Map<String,Object> result =new HashMap<String,Object>();
		if(auth!=null) {
			String[] skuArr = skulist.split(",");
			if(skuArr.length>0) {
				List<String> skuArrlist=new ArrayList<String>();
				for(int i=0;i<skuArr.length;i++) {
					skuArrlist.add(skuArr[i]);
				}
				GetInboundGuidanceResult guidance = iFulfillmentInboundService.getInboundGuidance(auth, marketplaceid, skuArrlist);
				Map<String, SKUPrepInstructions> prepInstructions = iFulfillmentInboundService.getPrepInstructions(auth, market, skuArrlist);
				result.put("guidance", guidance);
				result.put("prepInstructions", prepInstructions);
			}
		}
		return Result.success(result);
	}
	
	
	
 
 

	@ApiOperation(value = "获取货件信息")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/info/{shipmentid}")
	public Result<ShipInboundShipmenSummarytVo> infoAction(@PathVariable("shipmentid") String shipmentid) {
		    ShipInboundShipmenSummarytVo data = shipInboundItemService.summaryShipmentItem(shipmentid);
		    ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		    BeanUtil.copyProperties(shipment, data,"itemList");
		    ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getInboundplanid());
		    if(plan!=null) {
		    	data.setMarketplaceid(plan.getMarketplaceid());
		    	data.setTranstyle(shipment.getTranstyle());
		    	data.setCountryCode(marketplaceService.getById(plan.getMarketplaceid()).getMarket());
		    }
	        return Result.success(data);
	}
	
	@ApiOperation(value = "获取URL下载箱子标签")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/getPkgLabelUrl")
	public void getPkgLabelUrlAction(
			@RequestParam("shipmentid") String shipmentid
			,@RequestParam("pagetype") String pagetype
			,@RequestParam("labeltype") String labeltype,
			@RequestParam("pannum") String pannum,
			HttpServletResponse response
			 ) {
		    String data = shipInboundShipmentService.getLabelUrl(shipmentid,pagetype,labeltype,pannum);
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=shipment.pdf");// 设置文件名
			try {
				ServletOutputStream fOut = response.getOutputStream();
				HttpDownloadUtil.download(data, fOut);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@ApiOperation(value = "获取货件信息【ERP内部调用,也可以用于显示货件】")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/getShipment/{shipmentid}")
    @Transactional
	public Result<ShipInboundShipmentDTO> getShipmentidAction(@PathVariable("shipmentid") String shipmentid) {
		      ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		      ShipInboundPlan inplanv = shipInboundPlanService.getById(shipment.getInboundplanid());
		      ShipInboundShipmentDTO shipmentdto=new ShipInboundShipmentDTO();
		      ShipInboundPlanDTO plandto=new ShipInboundPlanDTO();
			  BeanUtil.copyProperties(shipment, shipmentdto);
			  BeanUtil.copyProperties(inplanv, plandto);
			  shipmentdto.setInboundplan(plandto);
			  List<ShipInboundItem> list = shipInboundItemService.getItemByShipment(shipmentid);
			  List<ShipInboundItemDTO> itemlist=new LinkedList<ShipInboundItemDTO>();
			  list.stream().forEach(item->{
				  ShipInboundItemDTO dto=new ShipInboundItemDTO();
				  BeanUtil.copyProperties(item, dto);
				  itemlist.add(dto);
			  });
			  shipmentdto.setItemList(itemlist);
	          return Result.success(shipmentdto);
	}
	
	private ShipInboundShipment transShipmentDTO2Entity(ShipInboundShipmentDTO dto) {
		ShipInboundPlan inplanparam = new ShipInboundPlan();
		BeanUtil.copyProperties(dto.getInboundplan(), inplanparam);
		ShipInboundShipment shipment=new ShipInboundShipment();
		BeanUtil.copyProperties(dto, shipment);
		List<ShipInboundItemDTO> list = dto.getItemList();
		List<ShipInboundItem> itemlist = new ArrayList<ShipInboundItem>();
		for(ShipInboundItemDTO itemdto:list) {
			ShipInboundItem item=new ShipInboundItem();
			BeanUtil.copyProperties(itemdto, item);
			itemlist.add(item);
		}
		shipment.setItemList(itemlist);
		shipment.setInboundplan(inplanparam);
	    return shipment;
	}
	
	@ApiOperation(value = "创建货件【ERP内部调用】")
	@PostMapping("/create")
	@Transactional
	public Result<String> createShipmentAction(@ApiParam("货件信息")@RequestBody ShipInboundShipmentDTO dto) {
		        String result=shipInboundShipmentService.createInboundShipment(transShipmentDTO2Entity(dto));
		        return Result.success(result);
	}
	
	@ApiOperation(value = "更新货件【ERP内部调用】")
	@PostMapping("/update")
	public Result<String> updateShipmentAction(@ApiParam("货件信息")@RequestBody ShipInboundShipmentDTO dto) {
		ShipInboundShipment shipment = transShipmentDTO2Entity(dto);
	    ShipInboundPlan inplan = shipInboundPlanService.getById(shipment.getInboundplanid());
	    AmazonAuthority auth=amazonAuthorityService.selectByGroupAndMarket(inplan.getAmazongroupid(), inplan.getMarketplaceid());
		String result=shipInboundShipmentService.updateInboundShipment(shipment,dto.getActiontype());
		if(StrUtil.isBlank(dto.getActiontype())||!"updateself".equals(dto.getActiontype())) {
		    if(shipment.getStatus()==0) {//校驗取消失敗的情況
			   	  iFulfillmentInboundService.checkCancel(auth,inplan,shipment);
			   	  shipInboundShipmentService.updateById(shipment);
		    }
		}
	    return Result.success(result);
	}
	
	@ApiOperation(value = "提交箱子信息")
	@PostMapping("/cart")
	public Result<ShipInboundShipment> saveCartShipmentAction(@ApiParam("货件信息")@RequestBody ShipCartShipmentDTO dto) {
		    UserInfo user=UserInfoContext.get();
		    ShipInboundShipment result=shipInboundShipmentService.saveCartShipment(user,dto);
	        return Result.success(result);
	}
	
	@ApiOperation(value = "提交物流信息")
	@PostMapping("/trans")
	public Result<String> saveTransAction(@ApiParam("货件信息")@RequestBody ShipInboundTransDTO dto) {
		    ShipInboundTrans trans = new ShipInboundTrans();
			BeanUtil.copyProperties(dto, trans);
			ShipInboundTrans oldtrans = shipInboundTransService.getOne(new QueryWrapper<ShipInboundTrans>().eq("shipmentid",dto.getShipmentid()));
			boolean flog = false;
			if(StrUtil.isAllBlank(dto.getCompany())) {
				dto.setCompany(null);
			}
			if(StrUtil.isAllBlank(dto.getChannel())) {
				dto.setChannel(null);
			}
			if(oldtrans!=null) {
				trans.setId(oldtrans.getId());
				flog=shipInboundTransService.updateById(trans);
			}else {
				flog=shipInboundTransService.save(trans);
			}
	        return Result.judge(flog);
	}
	
	@SystemControllerLog(  "更新物流")
	@PostMapping("/updateTransInfo")
	public Result<?> updateTransInfoAction(@ApiParam("货件信息")@RequestBody ShipInboundTransDTO dto) {
		UserInfo user=UserInfoContext.get();
		 boolean flog =false;
			if(StrUtil.isAllBlank(dto.getCompany())) {
				dto.setCompany(null);
			}
			if(StrUtil.isAllBlank(dto.getChannel())) {
				dto.setChannel(null);
			}
		ShipInboundTrans oldtrans = shipInboundTransService.getOne(new QueryWrapper<ShipInboundTrans>().eq("shipmentid",dto.getShipmentid()));
		 if(oldtrans!=null) {
			 oldtrans.setInarrtime(dto.getInarrtime());
			 oldtrans.setOutarrtime(dto.getOutarrtime());
			 oldtrans.setArrivalTime(dto.getArrivalTime());
			 oldtrans.setOperator(user.getId());
			 oldtrans.setOpttime(new Date());
			 flog = shipInboundTransService.updateById(oldtrans);
		 }else {
			 ShipInboundTrans trans=new ShipInboundTrans();
			 trans.setInarrtime(dto.getInarrtime());
			 trans.setOutarrtime(dto.getOutarrtime());
			 trans.setArrivalTime(dto.getArrivalTime());
			 trans.setOperator(user.getId());
			 trans.setOpttime(new Date());
			 trans.setShipmentid(dto.getShipmentid());
			 flog = shipInboundTransService.save(trans);
		 }
		 return Result.judge(flog);
	}
	

	
	@ApiOperation(value = "下载产品标签-Excel")
	@PostMapping("/downExcelLabel")
	public void downExcelLabelAction(
			@RequestBody ProductLabelDownloadDTO dto,
			HttpServletResponse response) {
		UserInfo user=UserInfoContext.get();
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=labeltemplate.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			if("1".equals(dto.getFtype())) {
				List<Map<String, Object>> skuinfolist = dto.getSkuinfo();
				List<Map<String, Object>>  paralist=new ArrayList<Map<String, Object>>();
			    if(skuinfolist!=null&&skuinfolist.size()>0) {
			    	for(int i=0;i<skuinfolist.size();i++) {
						Map<String,Object> item = skuinfolist.get(i);
						if(item.get("pid")!=null) {
							String pid =  item.get("pid").toString();
							String marketplaceid= item.get("marketplaceid").toString();
							ProductInfo pdt = productInfoService.getById(pid);
							if(pdt!=null) {
								Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
								Map<String, Object> maps=new HashMap<String, Object>();
								maps.put("title", pdt.getName());
								maps.put("quantity", item.get("quantity"));
								maps.put("fnsku", item.get("fnsku"));
								maps.put("sku",pdt.getSku());
								if(marketplace!=null) {
									maps.put("country", marketplace.getMarket());
								}
								paralist.add(maps);
							}
						}
					}
			    	shipInboundShipmentService.setExcelBookShipmentLabel(user.getCompanyid(), workbook, null,paralist);
			    }
				
			}else {
				shipInboundShipmentService.setExcelBookShipmentLabel(user.getCompanyid(), workbook, dto.getShipmentid(),null);
			}
			
			
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ApiOperation(value = "下载产品标签-Pdf")
	@PostMapping("/downPDFLabel")
	public void downPDFLabelAction(
			@RequestBody ProductLabelDownloadDTO dto,
			HttpServletResponse response) {
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=PDFFBALabel.pdf");// 设置文件名
		Document document = new Document(PageSize.A4);
		UserInfo user=UserInfoContext.get();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			if("1".equals(dto.getFtype())) {
				List<Map<String, Object>> skuinfolist = dto.getSkuinfo();
				List<Map<String,Object>> paralist=null;
			    if(skuinfolist!=null&&skuinfolist.size()>0) {
			    	paralist=new ArrayList<Map<String,Object>>();
			    	for(int i=0;i<skuinfolist.size();i++) {
						Map<String,Object> item = skuinfolist.get(i);
						if(item.get("pid")!=null) {
							String pid = (String) item.get("pid");
							ProductInfo pdt = productInfoService.getById(pid);
							if(pdt!=null) {
								Map<String, Object> maps=new HashMap<String, Object>();
								maps.put("title", pdt.getName());
								maps.put("quantity", item.get("quantity"));
								maps.put("fnsku", item.get("fnsku"));
								maps.put("sku", item.get("sku"));
								paralist.add(maps);
							}
						}
					}
			       shipInboundShipmentService.setPDFDocLabel(document, paralist, writer);
			    }
			}else {
			       shipInboundShipmentService.setPDFDocLabel(user.getCompanyid(),document, dto.getShipmentid(), writer);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(document!=null&&document.isOpen()) {
				document.close();
			}
		}
	}
	
	@SuppressWarnings({ "resource", "deprecation" })
	@ApiOperation(value = "下载产品标签模板")
	@GetMapping("/downExcelTemp")	
	public void downExcelTempAction(HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=ship-template.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			Sheet sheet = workbook.createSheet("模板格式");
			sheet.setColumnWidth(0, 40 * 256);
			sheet.setColumnWidth(1, 40 * 256);
			// 第一行
			CellStyle cstitle = workbook.createCellStyle();
			
			cstitle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cstitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			
			Row titleRow = sheet.createRow(0);
			titleRow.setHeight((short) (25 * 15));
			Cell title1 = titleRow.createCell(0);
			title1.setCellStyle(cstitle);
			title1.setCellValue("亚马逊SKU");
			Cell title2 = titleRow.createCell(1);
			title2.setCellStyle(cstitle);
			title2.setCellValue("发货数量");
			
			Sheet sheet2 = workbook.createSheet("模板示例");
			sheet2.setColumnWidth(0, 40 * 256);
			sheet2.setColumnWidth(1, 40 * 256);
			
			// 第一行
			Row etitleRow = sheet2.createRow(0);
			etitleRow.setHeight((short) (25 * 15));
			Cell etitle1 = etitleRow.createCell(0);
			etitle1.setCellStyle(cstitle);
			etitle1.setCellValue("亚马逊SKU");
			Cell etitle2 = etitleRow.createCell(1);
			etitle2.setCellStyle(cstitle);
			etitle2.setCellValue("发货数量");
			
			//内容
			Row ecotentRow = sheet2.createRow(1);
			Cell ece1 = ecotentRow.createCell(0);
			ece1.setCellValue("SKU001");
			Cell ece3 = ecotentRow.createCell(1);
			ece3.setCellValue("20");
			Row ecotentRow2 = sheet2.createRow(2);
			Cell eceSKU002 = ecotentRow2.createCell(0);
			eceSKU002.setCellValue("SKU002");
			Cell ecell3 = ecotentRow2.createCell(1);
			ecell3.setCellValue("15");
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ApiOperation(value = "上传产品标签")
	@PostMapping(value="/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<Map<String,Object>> uploadExcelAction(String warehouseid,String groupid,
			String marketplaceid,
			@RequestParam("file")MultipartFile file){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		if (StrUtil.isNotEmpty(groupid) && StrUtil.isNotEmpty(marketplaceid)) {
			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
			if (auth != null) {
				map.put("amazonauthid", auth.getId());
			}
		}
		map.put("shopid", shopid);
		map.put("groupid", groupid);
		map.put("marketplaceid", marketplaceid);
		map.put("warehouseid", warehouseid);
		
		if (file != null) {
			try {
				InputStream inputStream = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(inputStream);
				Sheet sheet = workbook.getSheetAt(0);
				return Result.success(shipInboundPlanService.uploadShipListByExcel(sheet, map));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.put("msg", "上传失败");
		return Result.success(map);
	}
	
	
	@ApiOperation(value = "删除货件[获取货件状态]")
	@GetMapping("/requestInboundShipment")
	public Result<String> requestInboundShipmentAction(@ApiParam("货件ID")@RequestParam String shipmentid){
		ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		ShipInboundPlan inboundplan = shipInboundPlanService.getById(shipment.getInboundplanid());
		AmazonAuthority amazonAuthority = amazonAuthorityService.selectByGroupAndMarket(inboundplan.getAmazongroupid(), inboundplan.getMarketplaceid());
		Marketplace tempmarketplace = marketplaceService.getById(inboundplan.getMarketplaceid());
		amazonAuthority.setMarketPlace(tempmarketplace);
		InboundShipmentInfo shipmentInfo = iFulfillmentInboundService.requestInboundShipments(amazonAuthority, shipmentid,tempmarketplace);
		if (shipmentInfo != null) {
			return Result.success(shipmentInfo.getShipmentStatus().getValue());
		}
		return Result.success("fail");
	}
	
	@ApiOperation(value = "验证货件是否正确")
	@GetMapping("/validateShipment")
	public Result<String> validateShipmentAction(@ApiParam("货件ID")@RequestParam String shipmentid) {
		shipInboundShipmentService.validateShipment(shipmentid);
		return Result.success("success");
	}
	
	
	@GetMapping("/getBaseInfo")
	public Result<Map<String, Object>> getBaseInfoAction(@ApiParam("货件ID")@RequestParam String shipmentid){
		ShipInboundShipmenSummarytVo data = shipInboundItemService.summaryShipmentItemWithoutItem(shipmentid);
	    ShipInboundShipment ship = shipInboundShipmentService.getById(shipmentid);
	    BeanUtil.copyProperties(ship, data,"itemList");
	    ShipInboundPlan plan = shipInboundPlanService.getById(ship.getInboundplanid());
	    if(plan!=null) {
	    	data.setMarketplaceid(plan.getMarketplaceid());
	    	data.setTranstyle(ship.getTranstyle());
	    	data.setCountryCode(marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid()).getMarket());
	    }
		Map<String, Object> map = getItemPriceAction(shipmentid);
		List<SummaryShipmentVo> detail = shipInboundPlanService.showPlanListByPlanid(null, shipmentid);
		if (detail.size() > 0) {
			map.put("detail", detail.get(0));
		}
		ship.setShipmentstatus(shipInboundShipmentService.getShipmentStatusName(ship.getShipmentstatus()));
		map.put("shipment", ship);
		map.put("shipmentid", ship.getShipmentid());
		ShipAddress fromAddress = shipAddressService.getById(plan.getShipfromaddressid());
		ShipAddressTo toAddress = shipAddressToService.getToAddress(ship);
		map.put("toAddress", toAddress);
		map.put("fromAddress", fromAddress);
		map.put("shipmentAll",data);
		getBoxDetialAction(map,ship,plan,shipmentid);
		getShipAmazonInfoAction(map,shipmentid);
		return Result.success(map);
	}
	
	
	 
	public Map<String, Object> getItemPriceAction(String shipmentid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> itemlist = shipInboundShipmentService.findInboundItemByShipmentId(shipmentid);
		Integer sumquantity = 0;
		Integer sumquantityshiped = 0;
		Integer sumquantityReceived = 0;
		boolean sumistotal=false;
		for (int i = 0; i < itemlist.size(); i++) {
			Map<String, Object> tempmap = itemlist.get(i);
			Integer quantity = 0;
			if (tempmap.get("Quantity") != null) {
				quantity = Integer.parseInt(tempmap.get("Quantity").toString());
			}
			Integer quantityShipped = 0;
			if (tempmap.get("QuantityShipped") != null) {
				quantityShipped = Integer.parseInt(tempmap.get("QuantityShipped").toString());
			}
			int quantityReceived = 0;
			if (tempmap.get("QuantityReceived") != null) {
				quantityReceived = Integer.parseInt(tempmap.get("QuantityReceived").toString());
			}
			if (tempmap.get("QuantityInCase") != null) {
				int quantityInCase = Integer.parseInt(tempmap.get("QuantityInCase").toString());
				if(quantityInCase!=0) {
					tempmap.put("caseamount", quantityShipped/quantityInCase);
					tempmap.put("skuinbox", quantityShipped);
					sumistotal=true;
				}else {
					tempmap.put("caseamount", 0);
					tempmap.put("skuinbox", 0);
				}
			}else {
				tempmap.put("caseamount", 0);
				tempmap.put("skuinbox", 0);
				tempmap.put("QuantityInCase", 0);
			}
			sumquantityReceived += quantityReceived;
			sumquantity += quantity;
			sumquantityshiped += quantityShipped;
		}
		map.put("skunum", itemlist.size());
		map.put("sumquantity", sumquantity);
		map.put("sumquantityshiped", sumquantityshiped);
		map.put("sumquantityReceived", sumquantityReceived);
		map.put("itemlist", itemlist);
		if(sumistotal) {
			map.put("sumtotal", sumquantityshiped);
		} 
		return map;
	}
	
 
	@SuppressWarnings("unchecked")
	public Result<Map<String, Object>> getBoxDetialAction(Map<String, Object> map,ShipInboundShipment shipment ,ShipInboundPlan plan, String shipmentid)  {
		Map<String, String> pkgpaper = shipInboundShipmentService.getPkgPaper("SP");
		map.put("pkgpaper", pkgpaper);
		Map<String, String> pkgpaperltl = shipInboundShipmentService.getPkgPaper("LTL");
		map.put("pkgpaperltl", pkgpaperltl);
		List<ShipInboundBox> listbox = shipInboundShipmentService.findShipInboundBoxByShipment(shipmentid);
		List<ShipInboundCase> listsku = shipInboundShipmentService.findShipInboundCaseByShipment(shipmentid);
		List<Map<String, Object>> itemlist = (List<Map<String, Object>>) map.get("itemlist");
		TreeMap<Integer, Integer> boxsum = new TreeMap<Integer, Integer>();
		int sumtotal = 0;
		for (int i = 0; i < itemlist.size(); i++) {
			Map<String, Object> skumap = itemlist.get(i);
			String sku = (String) skumap.get("sellersku");
			TreeMap<Integer, ShipInboundCase> boxsku = new TreeMap<Integer, ShipInboundCase>();
			int skuinbox = 0;
			for (int j = 0; j < listsku.size(); j++) {
				if (sku.equals(listsku.get(j).getMerchantsku())) {
					skuinbox = skuinbox + listsku.get(j).getQuantity();
					boxsku.put(listsku.get(j).getNumberofcase(), listsku.get(j));
				}
			}
			sumtotal = sumtotal + skuinbox;
			skumap.put("skuinbox", skuinbox);
			skumap.put("boxsku", boxsku);
		}

		for (int j = 0; j < listsku.size(); j++) {
			if (boxsum.get(listsku.get(j).getNumberofcase()) == null) {
				boxsum.put(listsku.get(j).getNumberofcase(), listsku.get(j).getQuantity());
			} else {
				boxsum.put(listsku.get(j).getNumberofcase(),
						boxsum.get(listsku.get(j).getNumberofcase()) + listsku.get(j).getQuantity());
			}
		}
	
		BigDecimal totalweight = new BigDecimal("0");
		Map<BigDecimal, Object> dem = new HashMap<BigDecimal, Object>();
		for (int i = 0; i < listbox.size(); i++) {
			ShipInboundBox item = listbox.get(i);
			item.setSumsku(boxsum.get(item.getBoxnum()));
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
			totalBoxNum=totalBoxNum+new Integer(value.get("boxsum").toString());
			totalBoxSize=totalBoxSize.add(entry.getKey().multiply(new BigDecimal(value.get("boxsum").toString())).divide(new BigDecimal("1000000"), 2,BigDecimal.ROUND_HALF_UP));
		}
		dem.remove(new BigDecimal("0"));
		map.put("dem", dem);
		map.put("demsize", dem.size());
		map.put("totalweight", totalweight);
		map.put("listbox", listbox);
		map.put("sumtotal", sumtotal);
		map.put("arecasesrequired", plan.getArecasesrequired());
		map.put("shipment", shipment);
		map.put("totalBoxNum", totalBoxNum) ;
		map.put("totalBoxSize", totalBoxSize);
		if(plan!=null && plan.getMarketplaceid()!=null) {
			map.put("market",marketplaceService.getById(plan.getMarketplaceid()).getMarket());
		}
		return Result.success(map);
	}
	
 
	public Result<Map<String, Object>> getShipAmazonInfoAction(Map<String, Object> map,String shipmentid) {
		List<Map<String, Object>> cartlist = shipInboundShipmentService.findShipInboundBox(shipmentid);
		Map<String,Object> transinfo = shipInboundShipmentService.getSelfTransDataView(shipmentid);
		BigDecimal sumweight = new BigDecimal("0");
		BigDecimal volume = new BigDecimal("0");
		BigDecimal sumqty = new BigDecimal("0");
		for (Map<String, Object> entry : cartlist) {
			sumweight = sumweight.add(entry.get("weight")!=null?(BigDecimal) entry.get("weight"):new BigDecimal("0"));
			sumqty = sumqty.add(entry.get("qty")!=null?(BigDecimal) entry.get("qty"):new BigDecimal("0"));
			if (entry.get("length") != null) {
				BigDecimal length = (BigDecimal) entry.get("length");
				BigDecimal width = entry.get("width")!=null?(BigDecimal) entry.get("width"):new BigDecimal("0.0001");
				BigDecimal height =entry.get("height")!= null? (BigDecimal) entry.get("height"):new BigDecimal("0.0001");
				InputDimensions dim = new InputDimensions(length, width, height, InputDimensions.unit_cm);
				BigDecimal baseDim = new BigDecimal("5000");
				if(transinfo!=null && transinfo.get("drate")!=null) {
					baseDim = new BigDecimal(transinfo.get("drate").toString());
				}
				ItemMeasure singlevolume = dim.getDimensionalWeight(baseDim);
				volume = volume.add(singlevolume.getValue().setScale(2, BigDecimal.ROUND_HALF_UP));
				entry.put("volume", singlevolume.getValue().setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		}
		map.put("sumqty", sumqty);
		map.put("volume", volume.setScale(2, BigDecimal.ROUND_HALF_UP));
		map.put("sumweight", sumweight);
		map.put("cart", cartlist);
		if(transinfo!=null) {
			map.put("transinfo", transinfo);
			if(transinfo.get("channel")!=null) {
				map.put("transchannel", shipInboundShipmentService.getTransChannelInfo(transinfo.get("channel").toString()));
			}
		}
		return Result.success(map);
	}
	
	@GetMapping("/getShipCart")
	public Result<Map<String, Object>> getShipCartAction(String shipmentid) {
		Map<String, Object> map = new HashMap<String,Object>();
		shipInboundShipmentService.refreshTransportDetails(shipmentid);
		List<Map<String, Object>> cartlist = shipInboundShipmentService.findShipInboundBox(shipmentid);
		ShipInboundShipment ship = shipInboundShipmentService.getById(shipmentid);
		BigDecimal sumweight = new BigDecimal("0");
		BigDecimal volume = new BigDecimal("0");
		BigDecimal sumqty = new BigDecimal("0");
		Map<String,Object> transinfo = shipInboundShipmentService.getSelfTransDataView(shipmentid);
		for (Map<String, Object> entry : cartlist) {
			sumweight = sumweight.add((BigDecimal) entry.get("weight"));
			sumqty = sumqty.add((BigDecimal) entry.get("qty"));
			if (entry.get("length") != null) {
				BigDecimal length = (BigDecimal) entry.get("length");
				BigDecimal width = (BigDecimal) entry.get("width");
				BigDecimal height = (BigDecimal) entry.get("height");
				InputDimensions dim = new InputDimensions(length, width, height, InputDimensions.unit_cm);
				BigDecimal baseDim = new BigDecimal("5000");
				if(transinfo!=null && transinfo.get("drate")!=null) {
					baseDim = new BigDecimal(transinfo.get("drate").toString());
				}
				ItemMeasure singlevolume = dim.getDimensionalWeight(baseDim);
				volume = volume.add(singlevolume.getValue().setScale(2, BigDecimal.ROUND_HALF_UP));
				entry.put("volume", singlevolume.getValue().setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		}
		map.put("cart", cartlist);
		map.put("shipment", ship);
		return Result.success(map);
	}
	
	@GetMapping("/getCarrier")
	public Result<List<String>> getCarrierAction(String country,String transtyle) {
		List<String> list = shipInboundShipmentService.getCarrierInfo(country, transtyle);
		return Result.success(list);
	}
	
	@GetMapping("/downExcelBoxDetail")
	public void downExcelBoxDetailAction(String shipmentid,
			HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=boxDetail.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			UserInfo user=UserInfoContext.get();
			shipInboundShipmentService.setExcelBoxDetail(user, workbook, shipmentid);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 @ApiOperation(value = "保存物流跟踪信息")
	 @PostMapping("/saveTransTrace")
	 @Transactional
	 public	Result<String> saveTransTraceAction(@ApiParam("货件物流信息") @RequestBody ShipTransDTO dto){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundShipment ship = shipInboundShipmentService.getById(dto.getShipmentid());
		 shipInboundShipmentService.saveTransTrance(user,ship, dto.getCarrier(),dto.getTranstype(), dto.getBoxinfo());
		 return Result.success("ok");
	 }
	
	 @ApiOperation(value = "保存物流跟踪信息")
	 @GetMapping("/estimateTransTrace")
	 @Transactional
	 public	Result<String> estimateTransTraceAction(@ApiParam("货件ID") String shipmentid){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundShipment ship = shipInboundShipmentService.getById(shipmentid);
		 shipInboundShipmentService.estimateTransTrance(user,ship);
		 return Result.success("ok");
	 }
	
	 @ApiOperation(value = "保存物流跟踪信息")
	 @GetMapping("/confirmTransTrace")
	 @Transactional
	 public	Result<String> saveTransTraceAction(@ApiParam("货件ID") String shipmentid){
		 UserInfo user=UserInfoContext.get();
		 ShipInboundShipment ship = shipInboundShipmentService.getById(shipmentid);
		 shipInboundShipmentService.confirmTransTrance(user,ship );
		 return Result.success("ok");
	 }
	
	 
	 @ApiOperation(value = "保存物流信息")
	 @PostMapping("/saveSelfTrans")
	 public	Result<String> saveSelfTransAction(@ApiParam("货件物流信息") @RequestBody ShipTransDTO dto){
	    	UserInfo user=UserInfoContext.get();
			ShipInboundTrans ship = new ShipInboundTrans();
			ship.setCompany(dto.getCompany());
			if(StrUtil.isNotBlank(dto.getTranstype())) {
				ship.setTranstype(new BigInteger(dto.getTranstype()));
			}
			ship.setChannel(dto.getChannel());
			ship.setOrdernum(dto.getOrdernum());
			ship.setShipmentid(dto.getShipmentid());
			ship.setWunit(dto.getUnit());
			ship.setOperator(user.getId());
			ship.setOpttime(new Date());
			if(StrUtil.isNotBlank(dto.getWtype())) {
				ship.setWtype(new Integer(dto.getWtype()));
			}else {
				ship.setWtype(0);
			}
			ship.setArrivalTime(dto.getArrive());
			ship.setOutarrtime(dto.getOutarrivaldate());
			ship.setInarrtime(dto.getInarrivaldate());
			ship.setRemark(dto.getRemark());
			ship.setOtherfee(dto.getOtherfee());
			ship.setSingleprice(dto.getSingleprice());
			ship.setTransweight(dto.getRweight());
			LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"Markshiped"+user.getCompanyid());
			try {
				shipInboundShipmentService.saveSelfTransData(user, ship, dto.getOperate(), null, dto.getProNumber(),dto.getShipdate(),"");
			}finally {
				lock.clear();
			}
			return Result.success("success");
		}
	 
	 @ApiOperation(value = "获取物流保存历史记录")
	 @GetMapping("/getSelfTransHis")
	 public Result<List<Map<String, Object>>> getSelfTransHisAction(@ApiParam("货件ID")@RequestParam String shipmentid) {
		     UserInfo user=UserInfoContext.get();
		     List<Map<String, Object>> list = shipInboundShipmentService.getSelfTransHis(user.getCompanyid(),shipmentid);
	    	 return Result.success(list);
	 }
	 
	@ApiOperation(value = "更新货件")
	@GetMapping("/marketShipped")
	@SystemControllerLog("本地出库")
    @Transactional
	public  Result<Boolean> marketShippedAction(@RequestParam String shipmentid){
		    UserInfo user=UserInfoContext.get();
		    shipInboundShipmentService.saveMarkshiped(user,shipmentid,"updateself");
		    return Result.judge(true);
	}
	
	@ApiOperation(value = "更新货件")
	@GetMapping("/marketAmazonShipped")
	@SystemControllerLog("本地出库")
    @Transactional
	public  Result<Boolean> marketAmazonShippedAction(@RequestParam String shipmentid){
		    UserInfo user=UserInfoContext.get();
			LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"saveMarkshiped"+user.getCompanyid());
			 try {
		          shipInboundShipmentService.saveMarkshiped(user, shipmentid,"updateamz");
				}finally {
					lock.clear();
				}
			return Result.judge(true);
	}
	
		
	 @ApiOperation(value = "获取货件计划列表")
	 @PostMapping("/getPlanList")
	 public Result<IPage<ShipPlanVo>> getPlanListAction(@ApiParam("货件计划DTO") @RequestBody ShipPlanListDTO dto) {
		 UserInfo user=UserInfoContext.get();
	     return Result.success(shipInboundPlanService.getPlanList(dto,user));
	 }
	 
	 @ApiOperation(value = "获取货件计划")
	 @GetMapping("/getPlanInfo")
	 public Result<ShipPlanVo> getPlanListAction(String inboundplanid) {
		 UserInfo user=UserInfoContext.get();
		 try {
			 if(StrUtil.isEmptyIfStr(inboundplanid)) {
				 throw new BizException("计划ID不能为空");
			 }
			 ShipPlanVo vo = shipInboundPlanService.getPlanBaseInfo(inboundplanid,user);
			 return Result.success(vo);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		return Result.success(null);
	 }
	 
	 
	 @ApiOperation(value = "获取货件计划列表根据planid")
	 @GetMapping("/findPlanSubDetail")
	 public Result<List<Map<String, Object>>> findPlanSubDetailAction(@ApiParam("店铺ID")@RequestParam String groupid,@ApiParam("仓库ID")@RequestParam String warehouseid,
			 @ApiParam("planID")@RequestParam String planid,@ApiParam("站点ID")@RequestParam String marketplaceid,String issplit) {
		 Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
		 if (!"true".equals(issplit) && marketplace!=null) {
				if ("EU".equals(marketplace.getRegion())) {
					marketplaceid = "EU";
				}
		 }
		 Result<List<Map<String, Object>>> listdata = erpClientOneFeign.findPlanSubDetailAction(groupid,planid, warehouseid, marketplaceid, issplit);
		 List<Map<String, Object>> list = listdata.getData();
		 if(list!=null && list.size()>0) {
			 AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
			 if(auth!=null) {
				 for(Map<String, Object> item:list) {
					 String psku=item.get("psku").toString();
					 QueryWrapper<ProductInfo> queryWrapper=new QueryWrapper<ProductInfo>();
					 queryWrapper.eq("marketplaceid", marketplaceid);
					 queryWrapper.eq("amazonAuthId",auth.getId());
					 queryWrapper.eq("sku", psku);
					 ProductInfo productinfo = productInfoService.getOne(queryWrapper);
					 if(productinfo!=null) {
						 item.put("asin", productinfo.getAsin());
						 ProductInOpt opt = productInOptService.getById(productinfo.getId());
						 if(opt==null || opt.getMsku()==null) {
							 item.put("msku", psku);
						 }else {
							 item.put("msku", opt.getMsku());
						 }
					 }
				 }
			 }
			
		 }
	     return Result.success(list);
	 }
	 
	 @ApiOperation(value = "更新plan的备注")
	 @GetMapping("/updatePlanRemark")
	 public Result<String> updatePlanRemarkAction(String inboundplanid,String remark) {
		 ShipInboundPlan plan = shipInboundPlanService.getById(inboundplanid);
		 if(plan!=null) {
			 plan.setRemark(remark);
			 shipInboundPlanService.updateById(plan);
			 return Result.success("ok");
		 }else {
			 return Result.success(null);
		 }
	 }
	 
	 @ApiOperation(value = "更新shipment的备注,名称")
	 @GetMapping("/updateShipmentRemark")
	 public Result<String> updateShipmentRemarkAction(String shipmentid,String remark,String name) {
		 ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		 if(shipment!=null) {
			 if(StrUtil.isNotEmpty(name)) {
				 shipment.setName(name);
			 }
			 if(StrUtil.isNotEmpty(remark)) {
				 shipment.setRemark(remark);
			 }
			 shipInboundShipmentService.updateById(shipment);
			 return Result.success("ok");
		 }else {
			 return Result.success(null);
		 }
	 }
	 
	 @ApiOperation(value = "根据shipmentid获取itemlist")
	 @GetMapping("/getItemlistByShipmentId")
	 public Result<Map<String, Object>> getItemlistByShipmentIdAction(String shipmentid) {
		 ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		 if(shipment!=null) {
			 Map<String, Object> map=new HashMap<String, Object>();
			 List<Map<String, Object>> list = shipInboundShipmentService.findInboundItemByShipmentId(shipmentid);
			 ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getInboundplanid());
			 String addressid = plan.getShipfromaddressid();
			 map.put("itemlist", list);
			 map.put("addressid", addressid);
			 map.put("groupid", plan.getAmazongroupid());
			 map.put("warehouseid", plan.getWarehouseid());
			 map.put("marketplaceid", plan.getMarketplaceid());
			 return Result.success(map);
		 }else {
			 return Result.success(null);
		 }
	 }
	 @ApiOperation(value = "根据shipmentid获取itemlist")
	 @GetMapping("/getItemlistByInboundPlanId")
	 public Result<Map<String, Object>> getItemlistByInboundPlanIdAction(String inboundplanid) {
		 ShipInboundPlan plan = shipInboundPlanService.getById(inboundplanid);
		 if(plan!=null) {
			 Map<String, Object> map=new HashMap<String, Object>();
			 List<Map<String, Object>> list = shipInboundShipmentService.findInboundItemByInboundplanId(inboundplanid);
			 String addressid = plan.getShipfromaddressid();
			 map.put("itemlist", list);
			 map.put("addressid", addressid);
			 map.put("groupid", plan.getAmazongroupid());
			 map.put("warehouseid", plan.getWarehouseid());
			 map.put("marketplaceid", plan.getMarketplaceid());
			 return Result.success(map);
		 }else {
			 return Result.success(null);
		 }
	 }
	 
	 @ApiOperation(value = "保存货件上的物流信息")
	 @GetMapping("/saveTrans")
	 public	Result<String> saveShipmentTransAction(
			 @RequestParam String shipmentid,
			 @RequestParam String company,
			 @RequestParam String channel,
			 @RequestParam String transtyle){
		 UserInfo user=UserInfoContext.get();
		 int result=shipInboundShipmentService.saveTransData(user,shipmentid,company,channel,transtyle);
		 if(result>0) {
			 return Result.success("success");
		 }else {
			 return Result.success("fail");
		 }
	 }

	 
	 
     @ApiOperation(value = "查看货件prep")
	 @GetMapping("/loadShipmentPrep")
	 public Result<?> loadShipmentPrep(String shipmentid) {
		 ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		 ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getInboundplanid());
		 AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getAmazongroupid(), plan.getMarketplaceid());
		 Marketplace market = this.marketplaceService.findMapByMarketplaceId().get(plan.getMarketplaceid());
		 List<ShipInboundItem> list = shipInboundItemService.list(new LambdaQueryWrapper<ShipInboundItem>().eq(ShipInboundItem::getShipmentid, shipmentid));
		 ArrayList<String> mlist = new ArrayList<String>();
		 for(ShipInboundItem item:list) {
			 mlist.add(item.getSellersku());
		 }
		 Map<String, SKUPrepInstructions> res = iFulfillmentInboundService.getPrepInstructions(auth, market, mlist);
		 for(ShipInboundItem localSkuItem:list) {
			 SKUPrepInstructions shipitem = res.get(localSkuItem.getSellersku());
				if(shipitem!=null&&shipitem.getPrepInstructionList().size()>0) {
					String preOwner=null;
					String preInstruction=null;
					for(PrepInstruction pre:shipitem.getPrepInstructionList()) {
						if(pre==null||pre.getValue()==null) {
							continue;
						}
						String owner=PrepOwner.SELLER.getValue();
						if(preOwner==null) {
							preOwner=owner;
						}else {
							preOwner=preOwner+","+owner;
						}
						String instruction=pre.getValue();
						if(preInstruction==null) {
							preInstruction=instruction;
						}else {
							preInstruction=preInstruction+","+instruction;
						}
					}
					if(preOwner!=null) {
						localSkuItem.setPrepOwner(preOwner);
						localSkuItem.setPrepInstruction(preInstruction);
					}
				}
				shipInboundItemService.updateById(localSkuItem);
			}
		 return Result.success(res);
	 }
 
     @ApiOperation(value = "驳回虚拟货件")
	 @GetMapping("/cancelShipment")
	 public Result<Boolean> cancelShipmentAction(String shipmentid) {
		 ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
		 if(shipment!=null) {
			 shipment.setStatus(-1);
			 ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getInboundplanid());
			 if(plan.getAuditstatus()==1) {
				 plan.setAuditstatus(2);
				 shipInboundPlanService.updateById(plan);
			 }
			 shipment.setShipmentstatus(ShipmentStatus.CANCELLED.getValue());
			 boolean isupdate = shipInboundShipmentService.updateById(shipment);
			 if(isupdate) {
				 AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(plan.getAmazongroupid(), plan.getMarketplaceid());
				 iFulfillmentInboundService.checkCancel(auth,plan,shipment);
			 }
			 return Result.judge(isupdate);
		 }else {
			 throw new BizException("找不到对应虚拟货件！");
		 }
	 }
	 
	 @ApiOperation(value = "审核后更新货件状态")
	 @GetMapping("/updatePlan")
	 public Result<String> updatePlanAction(String planid) {
		 ShipInboundPlan plan = shipInboundPlanService.getById(planid);
		 if(plan!=null) {
			 boolean isupdate = false;
			 QueryWrapper<ShipInboundShipment> queryWrapper=new QueryWrapper<ShipInboundShipment>();
			 queryWrapper.eq("inboundplanid", planid);
			 List<ShipInboundShipment> shiplist = shipInboundShipmentService.list(queryWrapper);
			 if(shiplist!=null) {
				 int cancelnum=0;
				 for(int i=0;i<shiplist.size();i++) {
					 if(shiplist.get(i).getStatus()==-1) {
						 cancelnum++;
					 }
				 }
				 if(cancelnum==shiplist.size()) {
					 plan.setAuditstatus(2);
				 }else {
					 plan.setAuditstatus(3);
				 }
				 isupdate = shipInboundPlanService.updateById(plan);
			 }
			 if(isupdate==true) {
				 return Result.success("ok");
			 }else {
				 return Result.success("fail");
			 }
		 }else {
			 throw new BizException("找不到对应计划！");
		 }
	 }
	 
	
	 @ApiOperation(value = "审核后更新货件状态")
	 @GetMapping("/refreshShipment")
	 public Result<String> refreshShipmentAction() {
		  amazonAuthorityService.executTask(iFulfillmentInboundService);
		 return Result.success();
	 }
	  


		@PostMapping("/shipLine")
		public Result<Chart> getSalesLineAction(@RequestBody SalesChartDTO dto) {
			UserInfo userinfo = UserInfoContext.get();
			Chart chart=new Chart();
			List<ChartLine> lines =new ArrayList<ChartLine>();
			List<String> legends=new ArrayList<String>();
			if(StrUtil.isBlank(dto.getGroupid())) {
				dto.setGroupid(null);
			}
			if(StrUtil.isBlank(dto.getSku())) {
			   List<ProductInfo> groupList = productInfoService.selectByMSku(dto.getMsku(),dto.getMarketplaceid(),dto.getGroupid(),userinfo.getCompanyid());
		       for(ProductInfo item:groupList) {
		    	   AmazonAuthority auth = amazonAuthorityService.getById(item.getAmazonAuthId());
		    	   AmazonGroup amzstore = iAmazonGroupService.getById(auth.getGroupid());
		    	   ChartLine line= shipInboundPlanService.shipArrivalTimeChart(auth.getGroupid(),item.getAmazonAuthId().toString(), item.getSku(), dto.getMarketplaceid(), dto.getDaysize(), userinfo);
					lines.add(line);
					String legend = dto.getSku();
                    if(StrUtil.isBlank(dto.getGroupid())) {
                    	legend= dto.getSku()+"-"+amzstore.getName();
					}
					legends.add(legend);
		       }
				Calendar c = Calendar.getInstance();
				Calendar end=Calendar.getInstance();
				end.add(Calendar.DATE, dto.getDaysize()-1);
				chart.setLabels(ChartPoint.getLabels(SumType.Daily, c.getTime(), end.getTime()));
				chart.setLines(lines);
				chart.setLegends(legends);
				return Result.success(chart);
			}else {
				ChartLine line= shipInboundPlanService.shipArrivalTimeChart(dto.getGroupid(),dto.getAmazonAuthId(), dto.getSku(), dto.getMarketplaceid(), dto.getDaysize(), userinfo);
				lines.add(line);
				chart.setLines(lines);
				Calendar c = Calendar.getInstance();
				Calendar end=Calendar.getInstance();
				end.add(Calendar.DATE, dto.getDaysize()-1);
				chart.setLabels(ChartPoint.getLabels(SumType.Daily, c.getTime(), end.getTime()));
				chart.setLegends(Arrays.asList(dto.getSku()));
			}
			
			return Result.success(chart);
		}
		
	 
}
