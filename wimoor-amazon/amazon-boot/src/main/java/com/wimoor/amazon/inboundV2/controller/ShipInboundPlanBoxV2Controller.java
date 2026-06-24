package com.wimoor.amazon.inboundV2.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.poi.excel.ExcelWriter;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxAnalysisDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.*;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inboundV2.service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;
import com.wimoor.amazon.inboundV2.pojo.dto.PackingDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCartDTO;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "发货单")
@RestController
@RequestMapping("/api/v2/shipInboundPlan/box")
@SystemControllerLog("发货单装修")
@RequiredArgsConstructor
public class ShipInboundPlanBoxV2Controller {
    final IShipInboundPlanService shipInboundPlanV2Service;
    final IShipInboundItemService iShipInboundItemService;
    final ISerialNumService serialNumService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IAmzProductSalesPlanShipItemService iAmzProductSalesPlanShipItemService;
	final IShipInboundBoxService shipInboundBoxV2Service;
	final IShipInboundCaseService shipInboundCaseV2Service;
	final IFulfillmentInboundService iFulfillmentInboundService;
	final IShipInboundOperationService  iShipInboundOperationService;
	final IShipInboundBoxAnalysisService shipInboundBoxAnalysis1Service;
	final IShipInboundBoxAnalysisService shipInboundBoxAnalysis2Service;
	final IShipInboundBoxAnalysisService shipInboundBoxAnalysis3Service;
	@ApiOperation(value = "获取箱子信息")
	@PostMapping("/getBoxDetail")
	public Result<Map<String, Object>> getBoxDetailAction(@RequestBody PackingDTO dto) {
		if(dto.getArecase()!=null&&dto.getArecase().equals("true")){
			if(dto.getShipmentid()!=null) {
				return Result.success(shipInboundBoxV2Service.getShipmentBoxDetialCase(dto));
			}else {
				return Result.success(shipInboundBoxV2Service.getBoxDetialCase(dto));
			}
		}else{
			if(dto.getShipmentid()!=null) {
				return Result.success(shipInboundBoxV2Service.getShipmentBoxDetial(dto));
			}else {
				return Result.success(shipInboundBoxV2Service.getBoxDetial(dto));
			}
		}

	}
	
	@ApiOperation(value = "获取箱子信息")
	@GetMapping("/getAllBoxDim")
	public Result<List<Map<String, Object>>> getAllBoxDimAction(String formid) {
	    return Result.success(shipInboundBoxV2Service.findAllBoxDim(formid));
	}
	
	@ApiOperation(value = "查询当前箱子分组是否填写装箱信息")
	@GetMapping("/selectPackgroupInfo")
	public  Result<?> selectPackgroupInfoAction(String packingGroupId){
		Map<String,Object> boxMap=new HashMap<String, Object>();
		LambdaQueryWrapper<ShipInboundBox> queryWrapper=new LambdaQueryWrapper<ShipInboundBox>();
		queryWrapper.eq(ShipInboundBox::getPackingGroupId, packingGroupId);
		List<ShipInboundBox> list = shipInboundBoxV2Service.list(queryWrapper);
		if(list!=null && list.size()>0) {
			boxMap.put("msg", "success");
			boxMap.put("num", list.size());
			return Result.success(boxMap);
		}else {
			boxMap.put("msg", "none");
			return Result.success(boxMap);
		}
	}
	 

	@ApiOperation(value = "提交箱子信息")
	@PostMapping("/savePackingInformation")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<?> savePackingInformationAction(@ApiParam("发货计划")@RequestBody ShipCartDTO dto){
		     UserInfo user=UserInfoContext.get();
			 try {
				shipInboundBoxV2Service.savePackingInformation(dto,user);
				return Result.success();
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
		
	@ApiOperation(value = "提交箱子信息")
	@GetMapping("/submitPackingInformation")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<ShipInboundOperation> submitPackingInformationAction(String formid){
		     UserInfo user=UserInfoContext.get();
			 try {
				 ShipInboundPlan plan = shipInboundPlanV2Service.getById(formid);
				List<ShipInboundItem> itemlist = iShipInboundItemService.lambdaQuery().eq(ShipInboundItem::getFormid, formid).list();
				return Result.success(shipInboundBoxV2Service.setPackingInformation(plan,user,itemlist));
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
	
	@ApiOperation(value = "完成装箱")
	@GetMapping("/donePlanBox")
	@SystemControllerLog("完成装箱")    
	@Transactional
	public  Result<Boolean> donePackingAction(String formid,String type){
		     UserInfo user=UserInfoContext.get();
			 try {
				return Result.success(shipInboundPlanV2Service.donePacking(user,formid,type));
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}
		
	@ApiOperation(value = "生成箱子分组")
	@GetMapping("/generatePackingOptions")
	@Transactional
	public  Result<ShipInboundOperation> generatePackingOptionsAction(String formid){
		 try {
			return Result.success(shipInboundBoxV2Service.generatePackingOptions(formid));
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}
		
	@ApiOperation(value = "查看分组信息")
	@PostMapping("/listPackingOptions")
	@Transactional
	public  Result<?> listPackingOptionsAction(@RequestBody PackingDTO dto){
		 try {
			Map<String, Object> map = shipInboundBoxV2Service.listPackingOptions(dto);
			return Result.success(map);
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}


	@ApiOperation(value = "查看分组Group详细信息")
	@PostMapping("/listPackingGroupItems")
	@Transactional
	public  Result<?> listPackingGroupItemsAction(@RequestBody PackingDTO dto){
		 try {
			  Object result = shipInboundBoxV2Service.listPackingGroupItems(dto);
			return Result.success(result);
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}
	
	@ApiOperation(value = "查看分组Group详细信息")
	@PostMapping("/confirmPackingOption")
	@Transactional
	public  Result<ShipInboundOperation> confirmPackingOptionAction(@RequestBody PackingDTO dto){
		 try {
			 ShipInboundOperation result = shipInboundBoxV2Service.confirmPackingOption(dto);
			return Result.success(result);
		 }catch(FeignException e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("生成失败" +e.getMessage());
		 }
	}


	@ApiOperation(value = "分析箱子")
	@PostMapping("/boxAnalysis")
	public Result<?> boxAnalysisAction(@RequestBody BoxAnalysisDTO dto) {
		UserInfo user=UserInfoContext.get();
		if(dto.getType().equals("v1")){
			return Result.success( shipInboundBoxAnalysis1Service.boxAnalysis(user,dto));
		}else if( dto.getType().equals("v2")){
			return Result.success( shipInboundBoxAnalysis2Service.boxAnalysis(user,dto));
		}else  {
			return Result.success( shipInboundBoxAnalysis3Service.boxAnalysis(user,dto));
		}
	}
	@PostMapping("/uploadExcelBoxDetail")
	public Result<?> uploadExcelBoxDetailAction(@RequestParam("file") MultipartFile file, String formid, String packingGroupId){
		try {
			UserInfo user=UserInfoContext.get();
			ShipCartDTO cart = shipInboundBoxV2Service.getDetailFromExcel(file, formid);
			cart.setFormid(formid);
			cart.setPackingGroupId(packingGroupId);
			cart.setShipmentid(null);
			for(ShipInboundBox box:cart.getBoxListDetail()){
				box.setPackingGroupId(packingGroupId);
				box.setShipmentid(null);
			}
			cart.setBoxnum(cart.getBoxListDetail().size());
			shipInboundBoxV2Service.savePackingInformation(cart,user);
			return Result.success();
		}catch(FeignException e) {
			 throw new BizException("提交失败" +e.getMessage());
		 }catch(Exception e) {
			 throw new BizException("提交失败" +e.getMessage());
		 }

	}
	@PostMapping("/uploadShipmentExcelBoxDetail")
	public Result<?> uploadShipmentExcelBoxDetailAction(@RequestParam("file") MultipartFile file, String formid, String shipmentid){
		try {
			UserInfo user=UserInfoContext.get();
			ShipCartDTO cart = shipInboundBoxV2Service.getDetailFromExcel(file, formid);
			cart.setFormid(formid);
			cart.setPackingGroupId(null);
			cart.setShipmentid(shipmentid);
			for(ShipInboundBox box:cart.getBoxListDetail()){
				box.setPackingGroupId(null);
				box.setShipmentid(shipmentid);
			}
			cart.setBoxnum(cart.getBoxListDetail().size());
			shipInboundBoxV2Service.savePackingInformation(cart,user);
			return Result.success();
		}catch(FeignException e) {
			throw new BizException("提交失败" +e.getMessage());
		}catch(Exception e) {
			throw new BizException("提交失败" +e.getMessage());
		}

	}
	@GetMapping("/downExcelBoxDetail")
	public void downExcelBoxDetailAction(String formid,
										 HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=boxDetail.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			UserInfo user=UserInfoContext.get();

			shipInboundBoxV2Service.setExcelBoxDetail(user, workbook, formid);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/downExcelBoxCaseTemp")
	public Result<?> downExcelBoxCaseTempAction(@RequestBody ShipPlanVo shipPlanVo,
										 HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=boxDetail.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			UserInfo user = UserInfoContext.get();

			// 1. 从resources读取模板文件
			InputStream templateStream = ExcelWriter.class
					.getClassLoader()
					.getResourceAsStream("template/package.xlsx");

			if (templateStream == null) {
				throw new BizException("模板文件未找到：template/package.xlsx");
			}


			Workbook workbook = WorkbookFactory.create(templateStream);
			// 4. 获取第一个工作表（可根据需要调整）
			Sheet sheet = workbook.getSheetAt(0);
			sheet.getRow(1).getCell(7).setCellValue(shipPlanVo.getCountryname());
			sheet.getRow(1).getCell(14).setCellValue(shipPlanVo.getGroupname());
			Row oldsheetRow = sheet.getRow(5);
			sheet.shiftRows(5, sheet.getLastRowNum(), shipPlanVo.getItemlist().size()-1,true,false);
			
			for(int rowaddnum=0;rowaddnum<shipPlanVo.getItemlist().size()-1;rowaddnum++) {
				Row row = sheet.createRow(5+rowaddnum);
				row.setRowStyle(oldsheetRow.getRowStyle());
				for (int cellnum = 0; cellnum <= oldsheetRow.getLastCellNum(); cellnum++) {
					Cell oldcell = oldsheetRow.getCell(cellnum);
					Cell mycell = row.createCell(cellnum);
					if(oldcell!=null) {
						mycell.setCellValue(oldcell.getRichStringCellValue());
						mycell.setCellStyle(oldcell.getCellStyle());
					}else {
						mycell.setCellValue("");
					}

				}
			}
			for(int i=0;i<shipPlanVo.getItemlist().size();i++){
				ShipInboundItemVo item=shipPlanVo.getItemlist().get(i);
				Row row=sheet.getRow(5+i);
				row.getCell(0).setCellValue(item.getSku());
				row.getCell(1).setCellValue(item.getQuantity());
			}
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();

		} catch (IOException e) {
            throw new RuntimeException(e);
        }
		return Result.success();
    }

}


