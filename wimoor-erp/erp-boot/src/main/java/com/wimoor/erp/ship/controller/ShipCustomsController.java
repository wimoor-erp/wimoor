package com.wimoor.erp.ship.controller;


import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.erp.ship.service.IShipCustomsService;
import com.wimoor.erp.ship.service.IShipTransChannelService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "货件海关信息")
@RestController
@SystemControllerLog("海关信息")
@RequestMapping("/api/v1/shipCustoms")
@RequiredArgsConstructor
public class ShipCustomsController {

    @Resource
    IShipCustomsService shipCustomsService;



    @PostMapping("/getShipCustoms")
    //获取货件信息 填充到Excel文件中
    public void getShipCustoms(String shipmentid){



    }




}
