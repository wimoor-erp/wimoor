package com.wimoor.erp.ship.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundItemDTO;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AmazonClientOneFeign;
import com.wimoor.erp.ship.pojo.dto.ShipAmazonShipmentDTO;
import com.wimoor.erp.ship.service.IShipAmazonFormService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import com.wimoor.util.QRCodeUtil;

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
public class ShipAmazonFormController {
	
	final IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	final AmazonClientOneFeign amazonClientOneFeign;
	final IShipAmazonFormService iShipFormService;
	final ISerialNumService serialNumService;
	
	@ApiOperation(value = "获取配货单")
	@ApiImplicitParam(name = "shipmentid", value = "货件ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/quotainfo/{shipmentid}")
	public Result<ShipInboundShipmenSummarytVo> quotainfoAction(@PathVariable("shipmentid") String shipmentid) {
		    Result<ShipInboundShipmenSummarytVo> result = amazonClientOneFeign.infoAction(shipmentid);
		    ShipInboundShipmenSummarytVo itemsum=result.getData();
		    ShipInboundShipmenSummarytVo data = iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum);
	        return Result.success(data);
	    }
	
	
	
	
	@ApiOperation(value = "提交发货计划")
	@PostMapping("/saveInboundPlan")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<Boolean> saveInboundPlanAction(@ApiParam("发货计划")@RequestBody ShipInboundPlanDTO inplan){
		 UserInfo user=UserInfoContext.get();
			inplan.setCreatedate(new Date());
			inplan.setCreator(user.getId());
			inplan.setLabelpreptype("SELLER_LABEL");
			inplan.setOperator(user.getId());
			inplan.setOpttime(new Date());
			inplan.setShopid(user.getCompanyid());
			inplan.setAuditstatus(1);
		 if(StrUtil.isEmpty(inplan.getNumber())) {
			 try {
					inplan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
				} catch (Exception e) {
					e.printStackTrace();
					try {
						inplan.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SF"));
					} catch (Exception e1) {
						e1.printStackTrace();
						throw new BizException("编码获取失败,请联系管理员");
					}
				}
		 }
		Result<String> result = amazonClientOneFeign.saveInboundPlanAction(inplan);
		if(Result.isSuccess(result)) {
			if (inplan.getPlansubid() != null) {
				//iShipFormService.afterShipInboundPlanSave(inplan, issplit,skulist);
			}
			return Result.judge(true);
		}else {
			return Result.judge(false);
		}
	}
	
    @ApiOperation(value = "确认货件")
    @SystemControllerLog("确认货件")
    @GetMapping("/createShipment")
    @Transactional
    public Result<Boolean> createShipmentAction(String shipmentid) {
    	Result<ShipInboundShipmentDTO> result = amazonClientOneFeign.getShipmentidAction(shipmentid);
    	ShipInboundShipmentDTO shipmentobj = result.getData();
   	    UserInfo user=UserInfoContext.get();
   	    shipmentobj.setLabelpreptype("SELLER_LABEL");
    	shipmentobj.setOperator(user.getId());
   	    shipmentobj.setOpttime(new Date());
   	    shipmentobj.setStatus(2);
   	    iShipFormService.fulfillableToOutbound(user,shipmentobj);
    	Result<String> result2 = amazonClientOneFeign.createShipmentAction(shipmentid);
        if(!Result.isSuccess(result2)) {
        	throw new BizException(result2.getMsg());
        }
    	if(!result2.getData().equals(shipmentid)) {
    		throw new BizException("货件创建失败");
    	} 
    	return Result.judge(true);
    }
    
	 
	@ApiOperation(value = "删除货件")
	@SystemControllerLog("删除货件")
	@GetMapping(value = "disableShipment")
	String disableShipmentAction(String shipmentid,String shipmentStatus,String disableShipment) throws BizException {
		boolean isDelAmz = true;
		if (ShipInboundShipmentDTO.ShipmentStatus_CANCELLED.equalsIgnoreCase(shipmentStatus)
				|| ShipInboundShipmentDTO.ShipmentStatus_CLOSED.equalsIgnoreCase(shipmentStatus)
				|| ShipInboundShipmentDTO.ShipmentStatus_DELETED.equalsIgnoreCase(shipmentStatus) || "".equalsIgnoreCase(shipmentStatus)) {
			isDelAmz = false;
		}
	    UserInfo user=UserInfoContext.get();
		return iShipFormService.updateDisable(user, shipmentid, isDelAmz,disableShipment);
	}
	
	@ApiOperation(value = "更新货件")
	@PostMapping("/updateShipment")
	@SystemControllerLog("配货")
    @Transactional
	public  Result<Boolean> updateShipmentAction(@ApiParam("货件ITEM")@RequestBody ShipAmazonShipmentDTO shipment){
		    UserInfo user=UserInfoContext.get();
		    iShipFormService.updateItemQty(user,shipment);
		    return Result.judge(false);
	}
    
	@ApiOperation(value = "更新货件")
	@PostMapping("/marketShipped")
	@SystemControllerLog("配货")
    @Transactional
	public  Result<Boolean> marketShippedAction(String shipmentid){
		    UserInfo user=UserInfoContext.get();
		    iShipFormService.marketShipped(user,shipmentid);
		    return Result.judge(false);
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
			BufferedImage imagebuffer = QRCodeUtil.generateQRCodeCommon(content, StrUtil.isNotBlank(size)?Integer.parseInt(size):20);
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
    
}
