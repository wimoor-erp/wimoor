package com.wimoor.amazon.report.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.report.service.IHandlerReportService;
import com.wimoor.amazon.report.summary.service.ISummaryOrderReportService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags = "报表接口")
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
	@Autowired
	IHandlerReportService handlerReportService;
	@Resource
	ISummaryOrderReportService summaryOrderReportService;
	
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "根据id 获取产品信息")
    @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    @GetMapping("/requestReport/{type}")
    public void requestReportAction(@PathVariable String type) {
    	handlerReportService.requestReport(type,false); 
    }
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "根据id 获取产品信息")
    @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    @GetMapping("/requestReportIgnore/{type}")
    public void requestReportIgnoreAction(@PathVariable String type) {
    	handlerReportService.requestReport(type,true); 
    }
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "更新报表")
    @ApiImplicitParams({
    	 @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/refreshReportType/{type}")
    public void refreshReportAction(@PathVariable String type) {
        handlerReportService.refreshProcessReportList(type);
    }
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "更新报表")
    @GetMapping("/refreshReport")
    public void refreshReportAction() {
        handlerReportService.refreshProcessReportList(null);
    }
    
    @ApiOperation(value = "汇总orders报表数据")
    @GetMapping("/summaryOrderReport")
    public void summaryOrderReportAction() {
    	summaryOrderReportService.summaryOrderReport();
    }
    
    
  
}
