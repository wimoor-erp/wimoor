package com.wimoor.erp.ship.controller;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.common.service.IImportRecordService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.pojo.dto.ConsumableOutFormDTO;
import com.wimoor.erp.ship.pojo.dto.ShipAmazonShipmentDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.erp.ship.service.IShipAmazonFormService;
import com.wimoor.erp.util.LockCheckUtils;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import feign.FeignException;
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
public class ShipAmazonFormController {
	
	final IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	final AmazonClientOneFeignManager amazonClientOneFeign;
	final IShipAmazonFormService iShipFormService;
	final ISerialNumService serialNumService;
	final IMaterialService materialService;
	final IWarehouseService warehouseService;
	final IInventoryService inventoryService;
	final IImportRecordService importRecordService;
	final IPictureService iPictureService;
	final FileUpload fileUpload;
	final IMaterialConsumableService iMaterialConsumableService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	@Resource
    QrConfig qrconig;
	@ApiOperation(value = "获取配货单")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/quotainfo/{shipmentid}")
	public Result<ShipInboundShipmenSummarytVo> quotainfoAction(@PathVariable("shipmentid") String shipmentid) {
		    Result<ShipInboundShipmenSummarytVo> result = amazonClientOneFeign.infoAction(shipmentid);
		    ShipInboundShipmenSummarytVo itemsum=result.getData();
		    ShipInboundShipmenSummarytVo data = iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum);
	        return Result.success(data);
	    }
	
 
	
	@SystemControllerLog("查看将同步的货件的详细内容")
	@GetMapping("/getUnSyncShipmentDetial")
	public Result<ShipInboundShipmenSummarytVo> getUnSyncShipmentDetialAction(
			@ApiParam("店铺ID") @RequestParam String groupid,
            @ApiParam("站点ID") @RequestParam String marketplaceid,
            @ApiParam("货件ID") @RequestParam String shipmentid,
            @ApiParam("仓库ID") @RequestParam String warehouseid ) {
		 UserInfo user=UserInfoContext.get();
		 Result<ShipInboundShipmenSummarytVo> shipmentResult =null;
		 try {
			 shipmentResult = amazonClientOneFeign.getUnSyncShipmentAction(groupid,marketplaceid,shipmentid,warehouseid);
		 }catch(FeignException  e) {
	 			 throw new BizException(BizException.getMessage(e, "同步货件失败"));
	 	}catch(Exception e) {
			 e.printStackTrace();
			 throw new BizException(e.getMessage());
		 }
		if(shipmentResult!=null&&shipmentResult.getData()!=null&&StrUtil.isNotBlank(warehouseid)) {
			  ShipInboundShipmenSummarytVo shipment = shipmentResult.getData();
			  Warehouse warehouse = warehouseService.getById(warehouseid);
			  shipment.setWarehouse(warehouse.getName());
			  for(ShipInboundItemVo item:shipment.getItemList()) {
				  Material material =null;
				  if(item.getMaterialid()!=null) {
					    material = materialService.getById(item.getMaterialid());
				  }else {
					    material =materialService.findBySKU(item.getSku(), user.getCompanyid());
					    if(material!=null) {
					    	item.setMaterialid(material.getId());
					    }else {
					    	continue;
					    }
				  }
				  item.setInvquantity(0L);
				  item.setOutbound(0L);
			     if(material!=null) {
						  if(material.getName()!=null) {
							  item.setName(material.getName());
						  }
						  if(material.getImage()!=null) {
							  Picture picture = iPictureService.getById(material.getId());
							  if(picture!=null&&picture.getLocation()!=null) {
								  item.setImage(picture.getLocation());
							  }
						  }
						  if(material.getSku()!=null) {
							  item.setSku(material.getSku());
					      } 
						  Map<String, Object> invdetail = inventoryService.getSelfInvBySKU(warehouseid,item.getMaterialid() );
						   if(invdetail!=null&&invdetail.get("fulfillable")!=null) {
							   item.setInvquantity(Long.parseLong(invdetail.get("fulfillable").toString()));
						   }else {
							   item.setInvquantity(0L);
						   }
					  }else {
						  item.setInvquantity(0L);
						  item.setOutbound(0L);
					  }
				  if(item.getImage()!=null) {
					  item.setImage( fileUpload.getPictureImage(item.getImage()) );	
			       }
			  }
		}
        return shipmentResult;
	}
	
	@ApiOperation(value = "下载配货单")
	@GetMapping("/downPDFShipForm")
	public void downPDFShipFormAction(
			@ApiParam("货件ID")@RequestParam String shipmentid,
			@ApiParam("类型")@RequestParam String ftype,
			HttpServletResponse response) {
		UserInfo user=UserInfoContext.get();
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=shipForm" + System.currentTimeMillis() + ".pdf");// 设置文件名
		Document document = new Document(PageSize.A4);
		// step 2
		try {
		    Result<ShipInboundShipmenSummarytVo> result = amazonClientOneFeign.infoAction(shipmentid);
		    ShipInboundShipmenSummarytVo itemsum=result.getData();
		    ShipInboundShipmenSummarytVo data = iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum);
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			if("detail".equals(ftype)) {
				iShipFormService.setPDFDocShipFormDetail(writer,user, document,  data);
			}else {
				iShipFormService.setPDFDocShipForm(writer,user,document, data);
			}
		} catch(FeignException  e) {
			 throw new BizException(BizException.getMessage(e, "获取货件失败"));
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
	}
	
	

    @ApiOperation(value = "确认货件")
    @SystemControllerLog("确认货件")
    @GetMapping("/createShipment")
    @Transactional
    public Result<Boolean> createShipmentAction(String shipmentid) {
    	Result<ShipInboundShipmentDTO> result = amazonClientOneFeign.getShipmentidAction(shipmentid);
    	ShipInboundShipmentDTO shipmentobj = result.getData();
    	if(shipmentobj.getStatus()!=1) {
    		throw new BizException("当前状态已经无法审核，请刷新后确认状态是否正常。");
    	}
   	    UserInfo user=UserInfoContext.get();
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"createShipment"+user.getCompanyid());
		try {
			    shipmentobj.setLabelpreptype("SELLER_LABEL");
		    	shipmentobj.setOperator(user.getId());
		   	    shipmentobj.setOpttime(new Date());
		   	    shipmentobj.setStatus(2);
		   	 for (int i = 0; i < shipmentobj.getItemList().size(); i++) {
				 ShipInboundItemDTO item = shipmentobj.getItemList().get(i);
				 Material m = materialService.getBySku(user.getCompanyid(),item.getMsku());
				 if(m==null) {
						throw new BizException("平台SKU【"+item.getSellersku()+"】对应本地SKU【"+item.getMsku()+"】无法找到对应信息");
				  }
				 item.setMaterialid(m.getId());
				 BigDecimal price = m.getPrice()!=null?m.getPrice():new BigDecimal("0");
				 price =m.getPriceWavg()!=null?m.getPriceWavg():price;
				 BigDecimal shipfee =m.getPriceShipWavg()!=null?m.getPriceShipWavg(): new BigDecimal("0");
			     item.setUnitcost(price.add(shipfee));
			    }
		   	    iShipFormService.fulfillableToOutbound(user,shipmentobj);
		   	 	Result<String> result2 =null;
		   	 	try{
		   	 	    result2=amazonClientOneFeign.createShipmentAction(shipmentobj);
		   	 	}catch(FeignException  e) {
		   	 			 throw new BizException(BizException.getMessage(e, "货件创建失败"));
		   	 	}catch(Exception e) {
		    	 	   throw new BizException(e.getMessage());
		    	}
		    	if(!Result.isSuccess(result2)) {
		    		if(result2!=null) {
		    			JSONObject errorJson = GeneralUtil.getJsonObject(result2.getMsg());
		        		JSONArray errors = errorJson.getJSONArray("errors");
		        		JSONObject errorObj = errors.getJSONObject(0);
		            	throw new BizException(errorObj.getString("message"));
		    		}else {
		    			throw new BizException("货件创建失败");
		    		}
		        }
		    	if(!result2.getData().equals(shipmentid)) {
		    		throw new BizException("货件创建失败");
		    	}  
		}finally {
			lock.clear();
		}
    	return Result.judge(true);
    }
    
	 
	@ApiOperation(value = "删除货件")
	@SystemControllerLog("删除货件")
	@GetMapping(value = "disableShipment")
    @Transactional
	Result<String> disableShipmentAction(String shipmentid,String shipmentStatus,String disableShipment) throws BizException {
		boolean isDelAmz = true;
		if(StrUtil.isBlank(shipmentStatus)) {
			throw new BizException("货件状态不能为空");
		}
		if (ShipInboundShipmentDTO.ShipmentStatus_CANCELLED.equalsIgnoreCase(shipmentStatus)
				|| ShipInboundShipmentDTO.ShipmentStatus_CLOSED.equalsIgnoreCase(shipmentStatus)
				|| ShipInboundShipmentDTO.ShipmentStatus_DELETED.equalsIgnoreCase(shipmentStatus)) {
			isDelAmz = false;
		}
	    UserInfo user=UserInfoContext.get();
		return Result.success(iShipFormService.updateDisable(user, shipmentid, isDelAmz,disableShipment)) ;
	}
	
	@ApiOperation(value = "更新货件")
	@PostMapping("/updateShipment")
	@SystemControllerLog("配货")
    @Transactional
	public  Result<Boolean> updateShipmentAction(@ApiParam("货件ITEM")@RequestBody ShipAmazonShipmentDTO shipment){
		    UserInfo user=UserInfoContext.get();
		    iShipFormService.updateItemQty(user,shipment);
		    return Result.success();
	}
    
	
    @ApiOperation("获取配货单二维码")
    @PostMapping("/getQRCode/{shipmentid}/{size}") 
    public  void getQRCode(@ApiParam("货件ID") @PathVariable("shipmentid") String shipmentid,
    		@ApiParam("二维码大小") @PathVariable("size") String size,
    		HttpServletResponse resp) {
    	String content = "https://www.wimoor.com/wxsc/"+shipmentid;//内容信息
         ServletOutputStream os = null;
		try { 
			resp.reset();
			resp.setHeader("Content-Disposition","attachment;fileName=qrcode.jpg");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Expose-Headers", "filename");
			resp.setContentType("octet-stream");// image/jpeg
			resp.setHeader("filename","二维码.jpg");
			resp.setContentType("application/force-download");// 设置强制下载不打开
			os = resp.getOutputStream();
			qrconig.setWidth(70);
			qrconig.setHeight(70);
			BufferedImage imagebuffer =	QrCodeUtil.generate(content, qrconig);
			imagebuffer.flush();
		    ImageIO.write(imagebuffer, "jpg", os);
		    imagebuffer.flush();
		    os.flush();
		    os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("文件读取错误");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("文件读取错误");
		}
	 
    }
    
    @ApiOperation(value = "获取库存耗材列表")
    @GetMapping("/getConsumableList")
	public Result<List<Map<String, Object>>> getConsumableListAction(@ApiParam("货件ID")@RequestParam String shipmentid) {
		UserInfo user=UserInfoContext.get();
		List<Map<String, Object>> list = iMaterialConsumableService.findConsumableDetailByShipment(user.getCompanyid(), shipmentid);
		return Result.success(list);
	}
    
    @ApiOperation(value = "保存库存耗材")
    @PostMapping("/saveInventoryConsumable")
    @Transactional
	public Result<?> saveInventoryConsumableAction(@RequestBody ConsumableOutFormDTO dto) {
    	UserInfo user=UserInfoContext.get();
    	iMaterialConsumableService.saveInventoryConsumable(user,dto);
		return Result.success();
	}
    
    @ApiOperation(value = "获取报关信息历史上传记录")
	@GetMapping("/getShipmentCustomsRecord")
		public Result<List<Map<String,Object>>> getShipmentCustomsRecordAction(){
		    UserInfo user=UserInfoContext.get();
	 	    List<Map<String,Object>> recoirdlist=importRecordService.getShipmentCustomsRecord(user.getCompanyid());
			return Result.success(recoirdlist);
	 }
    
    @ApiOperation(value = "上传海关文件")
    @PostMapping(value="/uploadCustomsFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   		public Result<Map<String, Object>> uploadCustomsFileAction(
   				@RequestParam("file")MultipartFile file
   				){
   		    UserInfo user=UserInfoContext.get();
   	 	    Map<String, Object> maps=null;
			try {
				maps = importRecordService.uploadShipmentcustomsFile(user,file.getInputStream(),file.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
   			return Result.success(maps);
   	 }
    
    @ApiOperation(value = "删除海关文件")
    @GetMapping("/deleteCustomsFile")
    public Result<Map<String, Object>> deleteCustomsFileAction(@ApiParam("上传海关记录ID")@RequestParam String uploadid){
    	Map<String, Object> maps=importRecordService.deleteCustomsFile(uploadid);
    	return Result.success(maps);
    }
    
    @ApiOperation(value = "获取shipplan信息")
    @GetMapping("/findPlanSubDetail")
    public Result<List<Map<String, Object>>> findPlanSubDetailAction(@ApiParam("groupid")@RequestParam String groupid,@ApiParam("planid")@RequestParam String planid,
    		@ApiParam("warehouseid")@RequestParam String warehouseid,@ApiParam("marketplaceid")@RequestParam String marketplaceid,String issplit){
    	List<Map<String, Object>> result=null;
    	if(!"true".equals(issplit)) {
    		result= iShipFormService.findPlanSubDetail(planid,marketplaceid,warehouseid);
    	}else {
    		result= iShipFormService.findPlanEuSubDetail(planid,marketplaceid,warehouseid);
    	}
    	return Result.success(result);
    }
    
    @GetMapping("/handlerExpShipment")
    public Result<String> handlerExceptionShipAction(String shipmentid){
    	   Result<ShipInboundShipmentDTO> result = amazonClientOneFeign.getShipmentidAction(shipmentid);
    	   ShipInboundShipmentDTO shipmentobj = result.getData();
    	   UserInfo user=UserInfoContext.get();
    	   shipmentobj.setLabelpreptype("SELLER_LABEL");
    	   shipmentobj.setOperator(user.getId());
    	   shipmentobj.setOpttime(new Date());
    	   shipmentobj.setStatus(2);
    	   iShipFormService.fulfillableToOutbound(user,shipmentobj);
    	   return Result.success("ok");
    }
 
    
    
   
    
    
    
    
    
    
}
