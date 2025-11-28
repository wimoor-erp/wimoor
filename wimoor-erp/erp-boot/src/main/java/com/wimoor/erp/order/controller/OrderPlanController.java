package com.wimoor.erp.order.controller;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.erp.order.pojo.entity.OrderListing;
import com.wimoor.erp.order.pojo.entity.OrderPlan;
import com.wimoor.erp.order.service.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/v1/order/plan")
@RequiredArgsConstructor
public class OrderPlanController {
    private final IOrderService orderService;
    private final IOrderPlatformService orderPlatformService;
    private final ISerialNumService iSerialNumService;
    private final IOrderPlanService orderPlanService;
    private final IOrderListingService iOrderListingService;
    @PostMapping("/getPlan")
    @Transactional
    public Result<?> savePlanFromAction(@RequestBody PlanDTO dto)   {
        UserInfo user = UserInfoContext.get();
        dto.setShopid(user.getCompanyid());
        if(user.isLimit(UserLimitDataType.owner)) {
            dto.setOwner(user.getId());
        }
        if(StrUtil.isAllBlank(dto.getSearch())) {
            dto.setSearch(null);
        }else {
            dto.setSearch("%"+dto.getSearch().trim()+"%");
        }
        if(StrUtil.isBlank(dto.getOwner())) {
            dto.setOwner(null);
        }
        if(StrUtil.isAllBlank(dto.getName())) {
            dto.setName(null);
        }else {
            dto.setName("%"+dto.getName().trim()+"%");
        }
        if(dto.getSelected()==null||dto.getSelected()==false) {
            dto.setSelected(false);
        }
        if(StrUtil.isAllBlank(dto.getRemark())) {
            dto.setRemark(null);
        }else {
            dto.setRemark("%"+dto.getRemark().trim()+"%");
        }
        if(StrUtil.isAllBlank(dto.getGroupid())) {
            dto.setGroupid(null);
        }else {
            dto.setGroupid(dto.getGroupid().trim());
        }
        if(StrUtil.isAllBlank(dto.getStatus())) {
            dto.setStatus(null);
        }
        if(StrUtil.isBlank(dto.getCategoryid())) {
            dto.setCategoryid(null);
        }

        if(StrUtil.isBlank(dto.getHasAddFee())) {
            dto.setHasAddFee(null);
        }
        if(StrUtil.isBlank(dto.getStatus2())) {
            dto.setStatus2(null);
        }
        if(StrUtil.isAllBlank(dto.getCurrentRank())) {
            dto.setCurrentRank(null);
        }
        if(StrUtil.isAllBlank(dto.getShortdays())) {
            dto.setShortdays(null);
        }
        if(dto.getIscheck()!=null&&dto.getIscheck().equals("true")) {
            dto.setSelected(true);
            dto.setIscheck("all");
        }else if(dto.getIscheck()!=null&&dto.getIscheck().equals("all")){
            dto.setSelected(true);
            dto.setIscheck("all");
        }else {
            dto.setIscheck(null);
        }
        if(StrUtil.isAllBlank(dto.getSkuarray())) {
            dto.setSkulist(null);
        }else {
            dto.setSkulist(Arrays.asList(dto.getSkuarray().split(",")));
        }
        if(StrUtil.isAllBlank(dto.getGroupid())) {
            dto.setGroupid(null);
        }
        if(StrUtil.isBlank(dto.getSmall())) {
            dto.setSmall(null);
        }
        String paralist =dto.getParalist();
        if (StrUtil.isNotEmpty(paralist)) {
            String para = paralist.substring(0, paralist.length() - 1).replace(",", " and ");
            dto.setParalist(para);
        } else {
            dto.setParalist(null);
        }
        return Result.success(orderPlanService.findByCondition(dto));
    }
    @PostMapping("/getExpandCountryData")
    @Transactional
    public Result<?> getExpandCountryDataAction(@RequestBody Map<String,Object> param)   {
        UserInfo user = UserInfoContext.get();
        param.put("shopid",user.getCompanyid());
        return Result.success(orderPlanService.getExpandCountryData(param));
    }
    @PostMapping("/refreshData")
    @Transactional
    public Result<?> refreshDataAction(@RequestBody Map<String,Object> param)   {
        UserInfo user = UserInfoContext.get();
        param.put("shopid",user.getCompanyid());
        return Result.success(orderPlanService.refreshData(param));
    }
    @PostMapping("/clear")
    @Transactional
    public Result<?> clearAction(@RequestBody Map<String,Object> param)   {
        UserInfo user = UserInfoContext.get();
        param.put("shopid",user.getCompanyid());
        return Result.success(orderPlanService.clear(param));
    }

    @PostMapping("/saveItem")
    @Transactional
    public Result<?> saveAction(@RequestBody List<OrderPlan> list)   {
          UserInfo user = UserInfoContext.get();
    if(list!=null&&list.size()>0){
        // 创建要删除的条件集合
        java.util.Set<String> skuSet = new java.util.HashSet<>();
        java.util.Set<String> warehouseIdSet = new java.util.HashSet<>();
        for(OrderPlan dto : list){
            skuSet.add(dto.getSku());
            warehouseIdSet.add(dto.getWarehouseid());
            dto.setShopid(user.getCompanyid());
            dto.setOpttime(new Date());
            dto.setOperator(user.getId());
        }
        
        // 删除所有匹配的记录
        orderPlanService.lambdaUpdate()
                .eq(OrderPlan::getShopid, user.getCompanyid())
                .in(OrderPlan::getSku, skuSet)
                .remove();
        
        // 批量保存
        orderPlanService.saveBatch(list);
    }
    return Result.success();
    }

    @PostMapping("/saveSales")
    @Transactional
    public Result<?> saveSalesAction(@RequestBody OrderListing ol)   {
        UserInfo user = UserInfoContext.get();
        OrderListing old=iOrderListingService.lambdaQuery()
                .eq(OrderListing::getSku,ol.getSku())
                .eq(OrderListing::getWarehouseid,ol.getWarehouseid())
                .eq(OrderListing::getShopid,user.getCompanyid())
                .eq(OrderListing::getCountry,ol.getCountry())
                .one();
        if(old!=null){
            old.setMsalesavg(ol.getMsalesavg());
            iOrderListingService.updateById(old);
        }else{
            iOrderListingService.save(ol);
        }
        return Result.success();
    }
    @PostMapping("/removeItem")
    @Transactional
    public Result<?> removeAction(@RequestBody OrderPlan dto)   {
        UserInfo user = UserInfoContext.get();
        orderPlanService.lambdaUpdate().eq(OrderPlan::getSku,dto.getSku())
                .eq(OrderPlan::getShopid,user.getCompanyid())
                .remove();
        return Result.success();
    }
    @PostMapping("/removeWarehouseItem")
    @Transactional
    public Result<?> removeWarehouseItemAction(@RequestBody List<OrderPlan> list)   {
        UserInfo user = UserInfoContext.get();
        for(OrderPlan dto:list){
            orderPlanService.lambdaUpdate().eq(OrderPlan::getSku,dto.getSku())
                    .eq(OrderPlan::getWarehouseid,dto.getWarehouseid())
                    .eq(OrderPlan::getShopid,user.getCompanyid())
                    .remove();
        }
        return Result.success();
    }


    @PostMapping("/bindMsku")
    @Transactional
    public Result<?> bindMskuAction(@RequestBody Map<String,Object> param)   {
        UserInfo user = UserInfoContext.get();
        param.put("shopid",user.getCompanyid());
        return Result.success(orderPlanService.bindMSku(param));
    }
    @GetMapping("/getPurchase")
    @Transactional
    public Result<?> getPurchaseAction(String warehouseid)   {
        UserInfo user = UserInfoContext.get();
        return Result.success(orderPlanService.getPurchase(user,warehouseid));
    }

    @GetMapping("/getShip")
    @Transactional
    public Result<?> getShipAction()   {
        UserInfo user = UserInfoContext.get();
        return Result.success(orderPlanService.getShip(user.getCompanyid()));
    }

    @GetMapping("/summary")
    @Transactional
    public Result<?> summaryAction()   {
        UserInfo user = UserInfoContext.get();
        return Result.success(iOrderListingService.summary(user.getCompanyid()));
    }
}
