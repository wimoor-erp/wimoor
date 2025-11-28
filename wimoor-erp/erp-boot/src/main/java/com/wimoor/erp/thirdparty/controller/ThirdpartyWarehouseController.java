package com.wimoor.erp.thirdparty.controller;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseBind;
import com.wimoor.erp.thirdparty.service.*;
import com.wimoor.erp.thirdparty.service.impl.OverseasWarehouseKeyManager;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "第三方API K5")
@RestController
@RequestMapping("/api/v1/thirdparty/warehouse")
@RequiredArgsConstructor
public class ThirdpartyWarehouseController {
    final IThirdPartyWarehouseService warehouseK5Service;
    final IThirdPartyWarehouseService warehouseOPSService;
    final IThirdPartyWarehouseService warehouseXLSService;
    final IThirdPartyWarehouseService warehouseYCService;
    final IThirdPartyAPIService thirdPartyApiService;
    @Autowired
    IThirdPartyWarehouseBindService thirdPartyWarehouseBindService;
    @GetMapping("/saveStock")
    public Result<?> saveStockAction(String apiid){
        UserInfo user = UserInfoContext.get();
        try {
            ThirdPartyAPI api = thirdPartyApiService.getById(apiid);
            if(api.getSystem().equals("K5")){
                return Result.success(warehouseK5Service.searchStartHouse(api));
            }else if(api.getSystem().equals("OPS")){
                return Result.success(warehouseOPSService.searchStartHouse(api));
            }else if(api.getSystem().equals("XL")){
                return Result.success(warehouseXLSService.searchStartHouse(api));
            }else if(api.getSystem().equals("YC")){
                return Result.success(warehouseYCService.searchStartHouse(api));
            }else{
                return Result.success();
            }
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/searchStock")
    public Result<?> searchStockAction(@RequestBody ThirdpartyWarehouseInvDTO dto){
        UserInfo user = UserInfoContext.get();
        try {
            ThirdPartyAPI api = thirdPartyApiService.getById(dto.getApi());
            dto.setShopid(user.getCompanyid());
            if(api.getSystem().equals("K5")){
                return Result.success(warehouseK5Service.searchStock(api,dto));
            }else if(api.getSystem().equals("OPS")){
                return Result.success(warehouseOPSService.searchStock(api,dto));
            }else if(api.getSystem().equals("XL")){
                return Result.success(warehouseXLSService.searchStock(api,dto));
            }else if(api.getSystem().equals("YC")){
                return Result.success(warehouseYCService.searchStock(api,dto));
            }else{
                return Result.success();
            }
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/order")
    public Result<?> orderAction(@RequestBody ThirdpartyWarehouseInvDTO dto){
        ThirdPartyAPI api = thirdPartyApiService.getById(dto.getApi());
        if(api.getSystem().equals("K5")){
            return Result.success(warehouseK5Service.getOrder(api,dto));
        }else  if(api.getSystem().equals("OPS")){
            return Result.success(warehouseOPSService.getOrder(api,dto));
        }else  if(api.getSystem().equals("XL")){
            return Result.success(warehouseXLSService.getOrder(api,dto));
        }else if(api.getSystem().equals("YC")){
            return Result.success(warehouseYCService.getOrder(api,dto));
        }
        return Result.success();
    }
    @GetMapping("/list")
    public Result<?> listAction(String apiid){
            ThirdPartyAPI api = thirdPartyApiService.getById(apiid);
            List<ThirdPartyWarehouse> result = null;
            if(api.getSystem()!=null&&api.getSystem().equals("K5")){
                result=warehouseK5Service.listByApi(api);
            }else  if(api.getSystem()!=null&&api.getSystem().equals("OPS")){
                result=warehouseOPSService.listByApi(api);
            }else  if(api.getSystem()!=null&&api.getSystem().equals("XL")){
                result=warehouseXLSService.listByApi(api);
            }else  if(api.getSystem()!=null&&api.getSystem().equals("YC")){
                result=warehouseYCService.listByApi(api);
            }
            if(result!=null){
                List<String> ids = result.stream().map(ThirdPartyWarehouse::getId).collect(Collectors.toList());
                if(ids!=null&&ids.size()>0){
                    List<ThirdPartyWarehouseBind> bindlist = thirdPartyWarehouseBindService.lambdaQuery().in(ThirdPartyWarehouseBind::getThirdpartyWarehouseId,ids).list();
                    if(bindlist!=null&&bindlist.size()>0){
                        List<String> bindids = bindlist.stream().map(ThirdPartyWarehouseBind::getThirdpartyWarehouseId).collect(Collectors.toList());
                        for(ThirdPartyWarehouse item:result){
                            if(bindids.contains(item.getId())){
                                item.setIsbind(true);
                            }else{
                                item.setIsbind(false);
                            }
                        }
                    }
                }
            }
        return Result.success(result);
    }
    @PostMapping("/bindlist")
    public Result<?> bindlistAction(@RequestBody ThirdPartyWarehouseBind dto){
            UserInfo user = UserInfoContext.get();
            return Result.success(thirdPartyWarehouseBindService.findByConditon(user.getCompanyid(),dto));
    }

    @PostMapping("/savebind")
    public Result<?> saveBindAction(@RequestBody ThirdPartyWarehouseBind dto){
        UserInfo user = UserInfoContext.get();
        dto.setOperator(user.getId());
        dto.setOpttime(new Date());
        return Result.success(thirdPartyWarehouseBindService.saveBind(dto));
    }

    @PostMapping("/deletebind")
    public Result<?> deleteBindAction(@RequestBody ThirdPartyWarehouseBind dto){
        return Result.success(thirdPartyWarehouseBindService.deleteBind(dto));
    }

    @GetMapping("/getMyKeyPair")
    public Result<?> getMyKeyPair(){
        if(!OverseasWarehouseKeyManager.keysExist()){
            try {
                OverseasWarehouseKeyManager.generateAndSaveKeyPair();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("publicKey",OverseasWarehouseKeyManager.getPublicKeyBase64());
            result.put("privateKey",OverseasWarehouseKeyManager.getPrivateKeyBase64());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success(result);
    }

}
