package com.wimoor.erp.ship.controller;

import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.erp.inventory.service.IStockCycleService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.ship.service.IShipPlanModelService;
import com.wimoor.erp.ship.service.IShipPlanService;
import com.wimoor.erp.warehouse.service.IWareHouseFBAService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "FBA发货规划接口")
@RestController("/api/v1/shipment_plan")
@SystemControllerLog("FBA发货规划")
@RequiredArgsConstructor
public class ShipPlanController   {
	final IShipPlanService shipPlanService;
	final ISerialNumService serialNumService;
	final IWarehouseService warehouseService;
	final IMaterialService materialService;
	final IStepWisePriceService stepWisePriceService;
	final IStockCycleService stockCycleService;
	final IShipPlanModelService shipPlanModelService;
	final IWareHouseFBAService wareHouseFBAService;
  
	
}
