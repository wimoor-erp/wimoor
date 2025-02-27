package com.wimoor.erp.ship.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.*;
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

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.common.service.IImportRecordService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.pojo.dto.ConsumableOutFormDTO;
import com.wimoor.erp.ship.pojo.dto.QuotaInfoDTO;
import com.wimoor.erp.ship.pojo.dto.ShipFormDTO;
import com.wimoor.erp.ship.service.IShipFormService;
import com.wimoor.erp.util.LockCheckUtils;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货订单接口")
@RestController
@RequestMapping("/api/v2/shipForm")
@SystemControllerLog("发货订单V2")
@RequiredArgsConstructor
public class ShipFormController {
	
	final IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	final AmazonClientOneFeignManager amazonClientOneFeign;
	final IShipFormService iShipFormService;
	final ISerialNumService serialNumService;
	final IMaterialService materialService;
	final IWarehouseService warehouseService;
	final IInventoryService inventoryService;
	final IImportRecordService importRecordService;
	final IPictureService iPictureService;
	final FileUpload fileUpload;
    @Autowired
    final IMaterialConsumableService iMaterialConsumableService;
	@Resource
    QrConfig qrconig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
	@ApiOperation(value = "获取配货单")
	@ApiImplicitParam(name = "dto", value = "本地仓库与SKU列表以及对于操作数量amount和extendinfo", required = true, paramType = "query", dataType = "String")
	@PostMapping("/quotainfo")
	public Result<QuotaInfoDTO> quotainfoAction(@RequestBody  QuotaInfoDTO dto) {
		    UserInfo user=UserInfoContext.get();
		    List<MaterialVO> resultvo = iWarehouseShelfInventoryService.formInvAssemblyShelf(user,dto);
		    dto.setList(resultvo);
		    if(StrUtil.isBlank(dto.getFormid())||dto.getFormid().equals("0")) {
		    	dto.setFormid(warehouseService.getUUID());
		    }
	        return Result.success(dto);
	    }
	
 
	   
	
    @ApiOperation("获取配货单二维码")
    @PostMapping("/getQRCode/{formid}/{size}") 
    public  void getQRCode(@ApiParam("货件ID") @PathVariable("formid") String formid,
    		@ApiParam("二维码大小") @PathVariable("size") String size,
    		HttpServletResponse resp) {
    	String content = "wxsc/"+formid;//内容信息
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
    
    @ApiOperation(value = "保存库存耗材")
    @PostMapping("/saveInventoryConsumable")
    @Transactional
	public Result<?> saveInventoryConsumableAction(@RequestBody ConsumableOutFormDTO dto) {
    	UserInfo user=UserInfoContext.get();
    	iMaterialConsumableService.saveInventoryConsumable(user,dto);
		return Result.success();
	}

    @ApiOperation(value = "发货库存锁定")
    @SystemControllerLog("发货库存锁定")
    @PostMapping("/outBoundShipInventory")
    @Transactional
    public Result<Boolean> outBoundShipInventory(@RequestBody ShipFormDTO dto) {
   	    UserInfo user=UserInfoContext.get();
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"createShipForm"+user.getCompanyid());
		try {
		     iShipFormService.fulfillableToOutbound(user,dto);
		}finally {
			lock.clear();
		}
    	return Result.success(true);
    }
    
    @ApiOperation(value = "发货出库")
    @SystemControllerLog("发货出库")
    @PostMapping("/outShipInventory")
    @Transactional
    public Result<Boolean> outShipInventory(@RequestBody ShipFormDTO dto) {
   	    UserInfo user=UserInfoContext.get();
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"createShipForm"+user.getCompanyid());
		try {
		     iShipFormService.subOutbound(user,dto);
		}finally {
			lock.clear();
		}
    	return Result.success(true);
    }
    
    @ApiOperation(value = "发货出库")
    @SystemControllerLog("发货出库")
    @PostMapping("/subFulfillable")
    @Transactional
    public Result<Boolean> subFulfillable(@RequestBody ShipFormDTO dto) {
   	    UserInfo user=UserInfoContext.get();
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"createShipForm"+user.getCompanyid());
		try {
		     iShipFormService.fulfillableOut(user,dto);
		}finally {
			lock.clear();
		}
    	return Result.success(true);
    }
    
    @ApiOperation(value = "修改出库数量")
    @SystemControllerLog("修改出库数量")
    @PostMapping("/updateItemQty")
    @Transactional
    public Result<Boolean> updateItemQty(@RequestBody ShipFormDTO dto) {
   	    UserInfo user=UserInfoContext.get();
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"changeShipForm"+user.getCompanyid());
		try {
		     iShipFormService.updateItemQty(user,dto);
		}finally {
			lock.clear();
		}
    	return Result.success(true);
    }
    
    @ApiOperation(value = "取消出库")
    @SystemControllerLog("取消出库")
    @PostMapping("/updateDisable")
    @Transactional
    public Result<Boolean> updateDisable(@RequestBody ShipFormDTO dto) {
   	    UserInfo user=UserInfoContext.get();
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"cancelShipForm"+user.getCompanyid());
		try {
		     iShipFormService.updateDisable(user,dto);
		}finally {
			lock.clear();
		}
    	return Result.success(true);
    }




    
}
