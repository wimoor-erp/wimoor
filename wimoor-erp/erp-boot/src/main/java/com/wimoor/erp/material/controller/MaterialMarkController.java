package com.wimoor.erp.material.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.entity.MaterialMark;
import com.wimoor.erp.material.service.IMaterialMarkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "本地产品公告与隐藏")
@RestController
@RequestMapping("/api/v1/material/mark")
@SystemControllerLog("本地产品公告与隐藏")
@RequiredArgsConstructor
public class MaterialMarkController {
  final IMaterialMarkService iMaterialMarkService;
  
  @ApiOperation(value = "根据产品ID,修改公告")
  @SystemControllerLog("修改产品公告")
  @PostMapping("/saveNotice")
  public Result<String> saveNoticeAction(@RequestBody MaterialMark mk) {
	  UserInfo userinfo = UserInfoContext.get();
	  iMaterialMarkService.saveNotice(userinfo, mk.getMaterialid(), mk.getMark());
	  return Result.success();
  }
  
  @ApiOperation(value = "根据产品ID,查看公告")
  @GetMapping("/showNotice")
  public Result<MaterialMark> saveNoticeAction(@RequestParam String materialid) {
	  return Result.success(iMaterialMarkService.showNotice( materialid));
  }
  
  @ApiOperation(value = "根据产品ID,显示")
  @SystemControllerLog("显示产品")
  @PostMapping("/show")
  public Result<String> showAction(@RequestBody MaterialMark mk) {
	  UserInfo userinfo = UserInfoContext.get();
	  iMaterialMarkService.show(userinfo, mk.getMaterialid());
	  return Result.success();
  }
  
  @ApiOperation(value = "根据产品ID,隐藏")
  @SystemControllerLog("隐藏产品")
  @PostMapping("/hide")
  public Result<String> hideAction(@RequestBody MaterialMark mk) {
	  UserInfo userinfo = UserInfoContext.get();
	  iMaterialMarkService.hide(userinfo, mk.getMaterialid());
	  return Result.success();
  }
}
