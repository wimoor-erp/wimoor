package com.wimoor.quote.purchase.controller;
import cn.hutool.core.util.StrUtil;
import com.wimoor.common.result.Result;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.ship.pojo.entity.UserBuyer;
import com.wimoor.quote.purchase.pojo.dto.PurchaseQuoteDTO;
import com.wimoor.quote.purchase.service.IPurchaseQuoteManagerService;
import com.wimoor.quote.ship.service.IQuoteOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Api(tags = "采购报价系统接口")
@RestController
@RequestMapping("/api/v1/quote/purchase")
@RequiredArgsConstructor
public class PurchaseQuoteManagerController {

    final IPurchaseQuoteManagerService iPurchaseQuoteManagerService;


    final IQuoteOrderService quoteOrderService;

    //获取货件 生成订单
    @ApiOperation(value = "保存订单信息")
    @PostMapping("/save")
    @Transactional
    public Result<?> saveAction(@RequestBody PurchaseQuoteDTO  dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(dto.getToken());
        return Result.judge( iPurchaseQuoteManagerService.savePurchase(buyer,dto));
    }

    @ApiOperation(value = "订单信息")
    @PostMapping("/list")
    @Transactional
    public Result<?> listAction(@RequestBody  QuoteDTO dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(dto.getToken());
        if(StrUtil.isNotBlank(dto.getSearch())){
            dto.setSearch("%"+dto.getSearch()+"%");
        }
        return Result.success(iPurchaseQuoteManagerService.listPurchase(buyer,dto));

    }


    @ApiOperation(value = "订单item信息")
    @GetMapping("/getItem")
    public Result<?> getItemAction(String orderid){
        return Result.success(iPurchaseQuoteManagerService.getEntryList(orderid));
    }

}
