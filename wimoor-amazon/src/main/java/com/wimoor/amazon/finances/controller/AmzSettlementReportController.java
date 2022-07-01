package com.wimoor.amazon.finances.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.finances.service.IAmzSettlementReportService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "亚马逊账期接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settlement")
public class AmzSettlementReportController {
	   final IAmzSettlementReportService iAmzSettlementReportService;
}
