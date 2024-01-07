package com.wimoor.erp.purchase.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.cache.annotation.CacheEvict;
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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseMaterial;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.util.DownloadExcelUtil;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "采购计划item接口")
@RestController
@SystemControllerLog( "采购计划item接口")
@RequestMapping("/api/v1/purchase/plan/item")
@RequiredArgsConstructor
public class PlanItemController {
	final IPurchasePlanItemService iPurchasePlanItemService;
	final protected ISerialNumService serialNumService;
	final IPurchaseWareHouseMaterialService iPurchaseWareHouseMaterialService;
	final IWarehouseService iWarehouseService;
	@PostMapping("/save")
    @Transactional
	@CacheEvict(value = { "inventoryByMskuCache"  }, allEntries = true)
	public Result<PurchasePlanItem> savePlanItemAction(@RequestBody PurchasePlanItem item) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		item.setShopid(shopid);
		item.setOpttime(new Date());
		item.setOperator(userinfo.getId());
		if(StrUtil.isBlankOrUndefined(item.getWarehouseid())){
			throw new BizException("入库仓库不能为空");
		}
		if(StrUtil.isBlankOrUndefined(item.getGroupid())){
			item.setGroupid(null);
		}
		LambdaQueryWrapper<PurchasePlanItem> query=new LambdaQueryWrapper<PurchasePlanItem>();
		query.eq(PurchasePlanItem::getMaterialid, item.getMaterialid());
		query.eq(PurchasePlanItem::getPlanid, item.getPlanid());
		if(StrUtil.isAllNotBlank(item.getGroupid())) {
			query.eq(PurchasePlanItem::getGroupid, item.getGroupid());
		}else {
			item.setGroupid(null);
			query.isNull(PurchasePlanItem::getGroupid);
		}
		PurchasePlanItem old = iPurchasePlanItemService.getOne(query);
		if(old!=null) {
			item.setId(old.getId());
			iPurchasePlanItemService.updateById(item);
		}else {
			iPurchasePlanItemService.save(item);
		}
		return Result.success(item);
	}
	
	@GetMapping("/delete")
    @Transactional
	@CacheEvict(value = { "inventoryByMskuCache"  }, allEntries = true)
	public Result<?> deletePlanItemAction(String planid,String materialid,String groupid) throws ERPBizException {
		LambdaQueryWrapper<PurchasePlanItem> query=new LambdaQueryWrapper<PurchasePlanItem>();
		query.eq(PurchasePlanItem::getMaterialid, materialid);
		query.eq(PurchasePlanItem::getPlanid, planid);
		if(StrUtil.isAllNotBlank(groupid)) {
			query.eq(PurchasePlanItem::getGroupid,groupid);
		}else {
			query.isNull(PurchasePlanItem::getGroupid);
		}
		iPurchasePlanItemService.remove(query);
		return Result.success();
	}
	
	@PostMapping("/changewh")
    @Transactional
	@CacheEvict(value = { "inventoryByMskuCache"  }, allEntries = true)
	public Result<?> deletePlanItemAction(@RequestBody PurchaseWareHouseMaterial pwm) throws ERPBizException {
		 iPurchaseWareHouseMaterialService.savePurchaseWareHouseMaterial(pwm);
		 Warehouse warehouse = iWarehouseService.getById(pwm.getWarehouseid());
		 return Result.success(warehouse);
	}
	
    @PostMapping(value = "/uploadWarhouseSKUFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<String> uploadWarhouseSKUFileAction(@RequestParam("file")MultipartFile file,@RequestParam("planid")String planid)  {
	       UserInfo user=UserInfoContext.get();
			if (file != null) {
				try {
					InputStream inputStream = file.getInputStream();
					Workbook workbook = WorkbookFactory.create(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					for (int i = 1; i <= sheet.getLastRowNum(); i++) {
						Row info=sheet.getRow(i);
						if(info==null || info.getCell(0)==null) {
							continue;
						}
						iPurchaseWareHouseMaterialService.uploadWarhouseSKUFile(user, info,planid);
					}
					workbook.close();
					return Result.success();
				} catch (IOException e) {
					e.printStackTrace();
					return Result.failed();
				} catch (EncryptedDocumentException e) {
					e.printStackTrace();
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return Result.success("ok");
	}
	
	@ApiOperation(value = "计划清除")
	@GetMapping("/clear")
	@CacheEvict(value = { "inventoryByMskuCache"  }, allEntries = true)
	public Result<?> clearShipPlanItem(String planid) {
		UserInfo user = UserInfoContext.get();
			LambdaQueryWrapper<PurchasePlanItem> query=new LambdaQueryWrapper<PurchasePlanItem>();
			query.eq(PurchasePlanItem::getShopid, user.getCompanyid());
			query.eq(PurchasePlanItem::getPlanid,planid);
			iPurchasePlanItemService.remove(query);
		return Result.success();
	}

	@ApiOperation(value = "计划刷新")
	@GetMapping("/getSummary")
	public Result<?> getSummary(String planid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getSummary(user.getCompanyid(),planid));
	}

	@ApiOperation(value = "计划列表")
	@PostMapping("/list/{planid}")
	public Result<?> list(@PathVariable String planid,@RequestBody List<String> materialidList) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getList(user.getCompanyid(),planid,materialidList));
	}
	
	@ApiOperation(value = "计划列表")
	@GetMapping("/listHis")
	public Result<?> listHis(String warehouseid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getHisList(user.getCompanyid(),warehouseid));
	}
	
	@ApiOperation(value = "计划刷新")
	@GetMapping("/getBatchList")
	public Result<?> getBatch(String planid,String batchnumber) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getList(user.getCompanyid(),planid,batchnumber));
	}

	@ApiOperation(value = "计划导出")
	@GetMapping("/downloadItemList")
	public Result<?> downloadItemList(String planid,HttpServletResponse response) {
		UserInfo user = UserInfoContext.get();
		try {
			List<Map<String, Object>> list = iPurchasePlanItemService.listItem(user.getCompanyid(), planid);
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("sku", "sku");
			titlemap.put("inwarehouse", "入库仓库");
			titlemap.put("amount", "计划数量");
			titlemap.put("name", "操作人");
			titlemap.put("opttime", "操作时间");
			DownloadExcelUtil.setWorkbook(response, titlemap, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success();
	}

	@ApiOperation(value = "计划打包")
	@PostMapping("/batch")
	@Transactional
	public Result<String> batchShipPlanItem(@RequestBody List<PurchasePlanItem> list) {
		UserInfo user = UserInfoContext.get();
		String number=null;
		 try {
			 number=serialNumService.readSerialNumber(user.getCompanyid(), "DP");
			} catch (Exception e) {
				e.printStackTrace();
				try {
					 number=serialNumService.readSerialNumber(user.getCompanyid(), "DP");
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
	    for(PurchasePlanItem item:list) {
	    	if(StrUtil.isAllNotBlank(item.getGroupid())) {
	    		iPurchasePlanItemService.lambdaUpdate()
				.set(PurchasePlanItem::getBatchnumber, number)
				.eq(PurchasePlanItem::getMaterialid, item.getMaterialid())
				.eq(PurchasePlanItem::getWarehouseid, item.getWarehouseid())
				.eq(PurchasePlanItem::getGroupid, item.getGroupid())
				.eq(PurchasePlanItem::getPlanid, item.getPlanid()).update();
			}else {
				iPurchasePlanItemService.lambdaUpdate()
				.set(PurchasePlanItem::getBatchnumber, number)
				.eq(PurchasePlanItem::getMaterialid, item.getMaterialid())
				.eq(PurchasePlanItem::getWarehouseid, item.getWarehouseid())
				.isNull(PurchasePlanItem::getGroupid)
				.eq(PurchasePlanItem::getPlanid, item.getPlanid()).update();
			}
			
	    }
		return Result.success(number);
	}

    @ApiOperation(value = "计划打包")
    @GetMapping("/removeBatch")
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache"  }, allEntries = true)
    public Result<?> moveBatchShipPlanItem(String batchnumber) {
    	UserInfo user = UserInfoContext.get();
    	iPurchasePlanItemService.moveBatch(user.getCompanyid(),batchnumber);
    	return Result.success();
    }

 
}
