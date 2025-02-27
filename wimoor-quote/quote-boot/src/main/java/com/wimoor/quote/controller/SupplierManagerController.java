package com.wimoor.quote.controller;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.dto.QuotePriceDTO;
import com.wimoor.quote.api.entity.QuotationPrice;
import com.wimoor.quote.api.entity.UserBuyer;
import com.wimoor.quote.api.entity.UserSupplier;
import com.wimoor.quote.service.IQuoteOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "报价系统接口")
@RestController
@RequestMapping("/api/v1/quote/supplier")
@RequiredArgsConstructor
public class SupplierManagerController {
    final IQuoteOrderService quoteOrderService;

    @ApiOperation(value = "获取订单信息")
    @PostMapping("/listSupplierOrder")
    public Result<?> listSupplierOrderAction(@RequestBody QuoteDTO dto){
        if(StrUtil.isBlank(dto.getToken())){
            throw new BizException("未绑定询价商，无法查询");
        }
        UserSupplier supplier =quoteOrderService.getSupplierInfo(dto.getToken());
        if(supplier==null){
            throw new BizException("未找到对应询价商");
        }
        return Result.success( quoteOrderService.listSupplierOrder(supplier,dto));
    }

    @ApiOperation(value = "查看供应商")
    @PostMapping("/listSupplier")
    public Result<?> listSupplierAction(@RequestBody UserSupplier dto){
        if(StrUtil.isBlank(dto.getToken())){
            throw new BizException("未绑定询价商，无法查询");
        }
        UserBuyer buyer =quoteOrderService.getBuyerInfo(dto.getToken());
        return Result.success( quoteOrderService.listSupplier(buyer.getId()));
    }

    @ApiOperation(value = "添加供应商")
    @PostMapping("/addSupplier/{token}")
    public Result<?> addSupplierAction(@PathVariable String token, @RequestBody UserSupplier dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        return Result.success( quoteOrderService.addSupplier(buyer,dto));
    }

    @ApiOperation(value = "修改供应商")
    @PostMapping("/updateSupplier")
    public Result<?> updateSupplierAction(@RequestBody UserSupplier dto){
        return Result.success( quoteOrderService.updatesupplier(dto));
    }
    @ApiOperation(value = "修改供应商")
    @GetMapping("/deleteSupplier")
    public Result<?> deleteSupplierAction(@RequestParam String id){
        return Result.success( quoteOrderService.deleteSupplier(id));
    }


    @ApiOperation(value = "发送订单给供应商报价")
    @PostMapping("/sendOrderToSupplier")
    public Result<?> sendOrderToSupplierAction( @RequestBody QuotePriceDTO dto){
        return Result.success( quoteOrderService.sendOrderToSupplier(dto));
    }

    @ApiOperation(value = "撤销给服务商发送的订单")
    @GetMapping("/deleteSupplierPrice")
    public Result<?> deleteSupplierPriceAction( @RequestParam String orderid,@RequestParam String supplierid){
        return Result.success( quoteOrderService.deleteSupplierPrice(orderid,supplierid));
    }

    @ApiOperation(value = "设置材积基数")
    @GetMapping("/updateBase")
    public Result<?> updateBaseAction(String token,String orderid,String base){
        UserSupplier supplier =quoteOrderService.getSupplierInfo(token);
        return Result.success( quoteOrderService.updateOrderSupplierBase(supplier,orderid,base));
    }

    @ApiOperation(value = "供应商提交报价")
    @PostMapping("/submitPrice/{token}")
    public Result<?> submitPriceAction(@PathVariable String token, @RequestBody List<QuotationPrice> dto){
        UserSupplier supplier =quoteOrderService.getSupplierInfo(token);
        for(QuotationPrice item:dto){
            item.setSupplierid(supplier.getId());
            item.setOpttime(new Date());
        }
        return Result.success( quoteOrderService.submitPrice(dto));
    }
}
