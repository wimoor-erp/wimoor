package com.wimoor.quote.ship.controller;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.dto.QuotePriceDTO;
import com.wimoor.quote.ship.pojo.entity.QuotationPrice;
import com.wimoor.quote.ship.pojo.entity.ShipmentSupplierTranschannel;
import com.wimoor.quote.ship.pojo.entity.UserBuyer;
import com.wimoor.quote.ship.pojo.entity.UserSupplier;
import com.wimoor.quote.ship.service.IQuoteOrderService;
import com.wimoor.quote.ship.service.IShipmentSupplierTranschannelService;
import com.wimoor.quote.ship.service.IShipmentTranschannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "报价系统接口")
@RestController
@RequestMapping("/api/v1/quote/supplier")
@RequiredArgsConstructor
public class SupplierManagerController {
    final IQuoteOrderService quoteOrderService;
    final IShipmentTranschannelService iShipmentTranschannelService;
    final IShipmentSupplierTranschannelService iShipmentSupplierTranschannelService;
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

    @ApiOperation(value = "保存物流公司渠道类型")
    @PostMapping("/submitPrice/{token}/supplierTransSave")
    public Result<?> supplierTransSaveAction(@PathVariable String token, @RequestBody ShipmentSupplierTranschannel dto){
        UserSupplier supplier = quoteOrderService.getSupplierInfo(token);
        dto.setBuyerid(supplier.getBuyerid());
        dto.setOpttime(new Date());
        dto.setSupplierid(supplier.getId());
        if(StrUtil.isNotBlank(dto.getId())){
            return Result.judge(iShipmentSupplierTranschannelService.updateById(dto));
        }else{
            dto.setDisable(false);
            ShipmentSupplierTranschannel old = iShipmentSupplierTranschannelService.lambdaQuery()
                    .eq(ShipmentSupplierTranschannel::getName, dto.getName())
                    .eq(ShipmentSupplierTranschannel::getSupplierid, dto.getSupplierid())
                    .one();
            if(old != null){
                dto.setId(old.getId());
                return Result.judge(iShipmentSupplierTranschannelService.updateById(dto));
            }else{
                return Result.judge(iShipmentSupplierTranschannelService.save(dto));
            }
        }
    }

    @ApiOperation(value = "保存物流公司渠道类型")
    @PostMapping("/submitPrice/{token}/supplierTransList")
    public Result<?> supplierTransListAction(@PathVariable String token, @RequestBody ShipmentSupplierTranschannel dto){
        UserSupplier supplier = quoteOrderService.getSupplierInfo(token);
        List<ShipmentSupplierTranschannel> list = iShipmentSupplierTranschannelService.lambdaQuery()
                .eq(ShipmentSupplierTranschannel::getChannelid, dto.getChannelid())
                .eq(ShipmentSupplierTranschannel::getSupplierid, supplier.getId())
                .eq(ShipmentSupplierTranschannel::getDisable, false)
                .list();
        return Result.success(list);
    }

    @ApiOperation(value = "供应商提交报价")
    @PostMapping("/submitPrice/{token}/login")
    public Result<?> submitLoginAction(@PathVariable String token, @RequestBody Map<String,Object> param){
        UserSupplier supplier =quoteOrderService.getSupplierInfo(token);
        String operate=param.get("operate")!=null?param.get("operate").toString():null;
        if(operate==null){
            throw new BizException("参数异常");
        }
        if(operate.equals("login")){
            String mobile=param.get("mobile").toString();
            String password=param.get("password").toString();
            String md5=quoteOrderService.md5(password);
            if(md5.equals(supplier.getPassword())&&mobile.equals(supplier.getMobile())){
                return Result.success(supplier.getName());
            }else{
                throw new BizException("密码或账号错误");
            }
        }
        if(operate.equals("reset")){
            String mobile=param.get("mobile").toString();
            String password=param.get("password").toString();
            supplier.setPassword(password);
            if(!mobile.equals(supplier.getMobile())){
                throw new BizException("账号错误,此电话号码并非您得注册账号");
            }
            quoteOrderService.updatesupplier(supplier);
            return Result.success(supplier.getName());
        }
        return Result.failed();
    }
}
