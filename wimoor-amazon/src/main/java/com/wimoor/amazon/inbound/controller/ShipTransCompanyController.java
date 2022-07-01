package com.wimoor.amazon.inbound.controller;

import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.common.service.impl.SystemControllerLog;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
 
 

@Api(tags = "物流渠道接口")
@SystemControllerLog("物流渠道")
@RestController("/api/v1/shipTransCompany")
@RequiredArgsConstructor
public class ShipTransCompanyController  {
	final IShipInboundTransService shipInboundTransService;
}
