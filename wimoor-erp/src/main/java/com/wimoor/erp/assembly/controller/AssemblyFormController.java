package com.wimoor.erp.assembly.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.api.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.inventory.service.IInventoryService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "组装接口")
@RestController
@RequestMapping("/api/v1/assembly")
@RequiredArgsConstructor
public class AssemblyFormController {
   final IAssemblyService assemblyService;
   final IInventoryService inventoryService;
	@GetMapping("/sublit")
	public Result<List<AssemblyVO>> getSubList(String materialid,String warehouseid){
		UserInfo userinfo = UserInfoContext.get();
		List<AssemblyVO> assemblyList = assemblyService.selectByMainmid(materialid);
		if(StrUtil.isBlankOrUndefined(warehouseid)) {
			warehouseid=null;
		}
		if(assemblyList!=null&&assemblyList.size()>0) {
			for(AssemblyVO items:assemblyList) {
	              Map<String, Object> submap = inventoryService.findInvByWarehouseId(items.getSubmid(),warehouseid, userinfo.getCompanyid());
	              if(submap!=null&&submap.size()>0) {
	            	  if(submap.get("inbound")!=null) {
	            		  items.setInbound(Integer.parseInt(submap.get("inbound").toString()));
	            	  }
	            	  if(submap.get("outbound")!=null) {
	            		  items.setOutbound(Integer.parseInt(submap.get("outbound").toString()));
	            	  }
	            	  if(submap.get("fulfillable")!=null) {
	            		  items.setFulfillable(Integer.parseInt(submap.get("fulfillable").toString()));
	            	  }
	              }
			}
		}
		return Result.success(assemblyList);
	}
}
