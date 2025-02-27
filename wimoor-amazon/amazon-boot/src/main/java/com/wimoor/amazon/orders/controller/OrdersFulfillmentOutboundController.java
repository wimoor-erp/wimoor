package com.wimoor.amazon.orders.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.orders.pojo.dto.OrdersFulfillmentDTO;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrder;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersRemoveVo;
import com.wimoor.amazon.orders.service.IFulfillmentOutboundOrdersService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "其他渠道订单")
@RestController
@Slf4j
@RequestMapping("/api/v0/orders/fulfillment")
public class OrdersFulfillmentOutboundController {
    @Autowired
    IFulfillmentOutboundOrdersService iFulfillmentOutboundOrdersService;
    @Autowired
    IAmazonAuthorityService amazonAuthorityService;
    @ApiOperation(value = "抓取产品订单")
    @GetMapping("/refreshOrderByAuth")
    public Result<String> refreshOrderByAuthAction(String authid) {
        iFulfillmentOutboundOrdersService.getFulfillmentOrders(authid);
        return Result.success();
    }

    @ApiOperation(value = "抓取")
    @GetMapping("/refreshOrder")
    public Result<String> refreshOrder() {
        amazonAuthorityService.executTask(iFulfillmentOutboundOrdersService);
        return Result.success();
    }

    @ApiOperation(value = "获取订单列表")
    @PostMapping("/list")
    public Result<?> getListAction(@RequestBody OrdersFulfillmentDTO dto) {
        UserInfo userInfo = UserInfoContext.get();
        dto.setShopid(userInfo.getCompanyid());
        AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
        if(auth!=null){
            dto.setAmazonAuthId(auth.getId());
        }
        IPage<FulfillmentOutboundOrder> list=iFulfillmentOutboundOrdersService.getList(dto);
        return Result.success(list);
    }

}
