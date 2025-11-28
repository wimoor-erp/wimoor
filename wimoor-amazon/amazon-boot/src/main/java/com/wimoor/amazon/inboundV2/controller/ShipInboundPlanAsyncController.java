package com.wimoor.amazon.inboundV2.controller;

import com.amazon.spapi.model.fulfillmentinboundV20240320.InboundPlan;
import com.amazon.spapi.model.fulfillmentinboundV20240320.Item;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListInboundPlanItemsResponse;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.service.IInboundApiHandlerService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "发货单同步")
@RestController
@RequestMapping("/api/v2/shipInboundPlan/async")
@SystemControllerLog("发货单同步")
@RequiredArgsConstructor
public class ShipInboundPlanAsyncController {

   final IInboundApiHandlerService inboundApiHandlerService;
   final IAmazonAuthorityService iAmazonAuthorityService;
   final IProductInfoService iProductInfoService;
   final ErpClientOneFeignManager erpClientOneFeignManager;
   final IShipInboundPlanService shipInboundPlanService;
    @SystemControllerLog("查看将同步的货件的详细内容")
    @GetMapping("/syncPlanItem")
    public Result<List<ShipInboundItemVo>> syncPlanItemAction(
            @ApiParam("店铺ID") @RequestParam String groupid,
            @ApiParam("站点ID") @RequestParam String marketplaceid,
            @ApiParam("货件ID") @RequestParam String planid,
            @ApiParam("仓库ID") @RequestParam String warehouseid ) {
        UserInfo user= UserInfoContext.get();

        AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
        ListInboundPlanItemsResponse itemResult = inboundApiHandlerService.listInboundPlanItems(auth, planid, 1000, null);

        List<ShipInboundItemVo> result =new ArrayList<ShipInboundItemVo>();
        PlanDTO dto = new PlanDTO();
        dto.setShopid(user.getCompanyid());

        List<String> mskulist=new ArrayList<String>();
        for(Item item:itemResult.getItems()){
            ShipInboundItemVo one=new ShipInboundItemVo();
            one.setSku(item.getMsku());
            one.setFnsku(item.getFnsku());
            one.setQuantity(item.getQuantity());
            one.setConfirmQuantity(item.getQuantity());
            String msku = iProductInfoService.findMSKUBySKUMarket(item.getMsku(), marketplaceid, auth.getId());
            one.setMsku(msku);
            if(item.getPrepInstructions()!=null&&item.getPrepInstructions().size()>0){
                one.setPrepOwner(item.getPrepInstructions().get(0).getPrepOwner());
            }
            one.setLabelOwner(item.getLabelOwner());
            mskulist.add(msku);
            result.add(one);

        }
        dto.setMskulist( mskulist);
        dto.setPlantype("ship");

        Result res= erpClientOneFeignManager.getMskuInventory(dto);
        if(Result.isSuccess(res)&&res.getData()!=null){
            List<Map<String, Object>> list=(List<Map<String, Object>>)res.getData();
            Map<String,Map<String,Object>> skuinfo= new java.util.HashMap<String,Map<String,Object>>();
             for(Map<String, Object> one:list){
                 skuinfo.put(one.get("sku").toString(), one);
             }
            for(ShipInboundItemVo item:result){
                Map<String, Object> info = skuinfo.get(item.getMsku());
                if(info!=null){
                    item.setImage(info.get("image").toString());
                    item.setName(info.get("name").toString());
                    if(info.get("fulfillable")!=null){
                        item.setInvquantity(Long.parseLong(info.get("fulfillable").toString()));
                    }else{
                        item.setInvquantity(0L);
                    }
                    if(info.get("outbound")!=null){
                        item.setOutbound(Long.parseLong(info.get("outbound").toString()));
                    }else{
                        item.setOutbound(0L);
                    }
                }
            }

        }
        return Result.success(result);
    }


    @SystemControllerLog("查看将同步的货件保存")
    @PostMapping("/savePlanItemSync")
    @Transactional
    public Result<Boolean> savePlanItemSyncAction(@RequestBody ShipPlanListDTO dto) {
        UserInfo user = UserInfoContext.get();
        dto.setShopid(user.getCompanyid());
        AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
        InboundPlan planResult = inboundApiHandlerService.getInboundPlan(auth, dto.getPlanid());
        if(planResult!=null){
            shipInboundPlanService.savePlanItemSync(planResult,dto,user,auth);
        }
        return Result.success(true);
    }

}
