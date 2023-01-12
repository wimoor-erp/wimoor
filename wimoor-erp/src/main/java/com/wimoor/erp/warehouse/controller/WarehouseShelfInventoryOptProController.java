package com.wimoor.erp.warehouse.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 预操作，此表内不存储任何记录。当预操作结束后自动删除 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Api(tags = "库位库存操作接口")
@RestController
@RequestMapping("/api/v1/warehouse/shelfInventoryOptPro")
@RequiredArgsConstructor
public class WarehouseShelfInventoryOptProController {

}

