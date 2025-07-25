package com.wimoor.amazon.auth.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.common.service.IAmazonMoveDataService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "迁移数据接口")
@RestController
@RequestMapping("/api/v1/data")
@Component("moveDataController")
//@Slf4j
@RequiredArgsConstructor
public class MoveDataController {
    private final IAmazonMoveDataService amazonMoveDataService;
    
    @ApiOperation(value = "迁移settlement")
    @GetMapping("/moveSettlementReport")
    public Result<String> moveSettlementReportAction() {
    	  amazonMoveDataService.moveSettlementReport();
        return Result.success();
    }
    
    @ApiOperation(value = "迁移settlement")
    @GetMapping("/moveSettlementReportArchive")
    public Result<String> moveSettlementReportArchiveAction() {
    	  new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amazonMoveDataService.moveSettlementReportArchive();
			}
    	  }).start();
        return Result.success();
    }
    
    @ApiOperation(value = "迁移inventoryhis")
    @GetMapping("/moveInventoryHisArchive")
    public Result<String> moveInventoryHisArchiveAction() {
    	  new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amazonMoveDataService.moveInventoryHisArchive();
			}
    	  }).start();
        return Result.success();
    }
    
    @ApiOperation(value = "迁移Data")
    @GetMapping("/moveData")
    public Result<String> moveDataAction() {
    	  new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amazonMoveDataService.moveSettlementReportArchive();
				amazonMoveDataService.moveInventoryHisArchive();
			}
    	  }).start();
        return Result.success();
    }
}
