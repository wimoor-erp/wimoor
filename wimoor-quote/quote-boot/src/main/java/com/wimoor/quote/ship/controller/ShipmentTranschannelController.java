package com.wimoor.quote.ship.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.wimoor.common.result.Result;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.entity.UserBuyer;
import com.wimoor.quote.ship.pojo.dto.ShipmentSupplierTranschannelDTO;
import com.wimoor.quote.ship.pojo.entity.ShipmentSupplierTranschannel;
import com.wimoor.quote.ship.pojo.entity.ShipmentTranschannel;
import com.wimoor.quote.ship.service.IQuoteOrderService;
import com.wimoor.quote.ship.service.IShipmentSupplierTranschannelService;
import com.wimoor.quote.ship.service.IShipmentTranschannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "报价系统渠道")
@RestController
@RequestMapping("/api/v1/transchannel")
@RequiredArgsConstructor
public class ShipmentTranschannelController {
    final IShipmentTranschannelService iShipmentTranschannelService;
    final IQuoteOrderService quoteOrderService;
    final IShipmentSupplierTranschannelService iShipmentSupplierTranschannelService;
    //获取货件 生成订单
    @ApiOperation(value = "保存渠道类型")
    @PostMapping("/save/{token}")
    public Result<?> saveAction(@PathVariable String token, @RequestBody ShipmentTranschannel dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        dto.setBuyerid(buyer.getId());
        dto.setDisable(false);
        dto.setOpttime(new Date());
        ShipmentTranschannel old = iShipmentTranschannelService.lambdaQuery().eq(ShipmentTranschannel::getName, dto.getName()).eq(ShipmentTranschannel::getBuyerid, buyer.getId()).one();
        if(old != null){
            dto.setId(old.getId());
            return Result.judge(iShipmentTranschannelService.updateById(dto));
        }else{
            return Result.judge(iShipmentTranschannelService.save(dto));
        }
    }

    @ApiOperation(value = "更新渠道类型")
    @PostMapping("/update/{token}")
    public Result<?> updateAction(@PathVariable String token, @RequestBody ShipmentTranschannel dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        dto.setBuyerid(buyer.getId());
        dto.setOpttime(new Date());
        return Result.judge(iShipmentTranschannelService.updateById(dto));
    }

    @ApiOperation(value = "查询渠道类型")
    @PostMapping("/list/{token}")
    public Result<?> listAction(@PathVariable String token, @RequestBody ShipmentSupplierTranschannelDTO dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        dto.setBuyerid(buyer.getId());
        if(StrUtil.isNotBlank(dto.getName())){
            dto.setName("%"+StrUtil.trim(dto.getName())+"%");
        }else{
            dto.setName(null);
        }
        return Result.success(iShipmentTranschannelService.listPage(dto)) ;
    }

    @ApiOperation(value = "查询全部渠道类型")
    @PostMapping("/listall/{token}")
    public Result<?> listAction(@PathVariable String token){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        return Result.success(iShipmentTranschannelService.lambdaQuery().eq(ShipmentTranschannel::getBuyerid, buyer.getId()).list());
    }


    @ApiOperation(value = "查看物流公司渠道类型")
    @PostMapping("/tlist/{token}")
    public Result<?> tlistAction(@PathVariable String token, @RequestBody ShipmentSupplierTranschannelDTO dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        dto.setBuyerid(buyer.getId());
        if(StrUtil.isNotBlank(dto.getName())){
            dto.setName("%"+StrUtil.trim(dto.getName())+"%");
        }else{
            dto.setName(null);
        }
        if(StrUtil.isNotBlank(dto.getSupplierid())){
            dto.setSupplierid(StrUtil.trim(dto.getSupplierid()));
        }else{
            dto.setSupplierid(null);
        }
        return Result.success(iShipmentSupplierTranschannelService.listPage(dto)) ;
    }

    @ApiOperation(value = "保存物流公司渠道类型")
    @PostMapping("/tsave/{token}")
    public Result<?> tsaveAction(@PathVariable String token, @RequestBody ShipmentSupplierTranschannel dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        dto.setBuyerid(buyer.getId());
        dto.setDisable(false);
        dto.setOpttime(new Date());
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

    @ApiOperation(value = "更新物流公司渠道类型")
    @PostMapping("/tupdate/{token}")
    public Result<?> tupdateAction(@PathVariable String token, @RequestBody ShipmentSupplierTranschannel dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        dto.setBuyerid(buyer.getId());
        dto.setOpttime(new Date());
        return Result.judge(iShipmentSupplierTranschannelService.updateById(dto));
    }
}
