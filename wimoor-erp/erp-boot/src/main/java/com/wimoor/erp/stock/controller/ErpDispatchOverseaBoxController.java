package com.wimoor.erp.stock.controller;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.stock.pojo.dto.PackingDTO;
import com.wimoor.erp.stock.pojo.dto.ShipCartDTO;
import com.wimoor.erp.stock.service.IErpDispatchOverseaBoxService;
import com.wimoor.erp.stock.service.IErpDispatchOverseaCaseService;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "发货单")
@RestController
@RequestMapping("/api/v1/inventory/dispatch/oversea/box")
@SystemControllerLog("发货单装修")
@RequiredArgsConstructor
public class ErpDispatchOverseaBoxController {
    final ISerialNumService serialNumService;
	final IErpDispatchOverseaBoxService erpDispatchOverseaBoxService;
	final IErpDispatchOverseaCaseService erpDispatchOverseaCaseService;
	@ApiOperation(value = "获取箱子信息")
	@PostMapping("/getBoxDetail")
	public Result<Map<String, Object>> getBoxDetailAction(@RequestBody PackingDTO dto) {
			return Result.success(erpDispatchOverseaBoxService.getBoxDetial(dto));
	}
	
	@ApiOperation(value = "获取箱子信息")
	@GetMapping("/getAllBoxDim")
	public Result<List<Map<String, Object>>> getAllBoxDimAction(String formid) {
	    return Result.success(erpDispatchOverseaBoxService.findAllBoxDim(formid));
	}
	
	@ApiOperation(value = "提交箱子信息")
	@PostMapping("/savePackingInformation")
	@SystemControllerLog("新增")    
	@Transactional
	public  Result<?> savePackingInformationAction(@ApiParam("发货计划")@RequestBody ShipCartDTO dto){
		     UserInfo user=UserInfoContext.get();
			 try {
				erpDispatchOverseaBoxService.savePackingInformation(dto,user);
				return Result.success();
			 }catch(FeignException e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }catch(Exception e) {
				 throw new BizException("提交失败" +e.getMessage());
			 }
	}


		
}


