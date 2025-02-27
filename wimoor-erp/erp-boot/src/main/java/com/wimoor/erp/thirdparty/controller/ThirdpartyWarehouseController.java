package com.wimoor.erp.thirdparty.controller;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.service.IThirdPartyAPIService;
import com.wimoor.erp.thirdparty.service.IWarehouseK5Service;
import com.wimoor.erp.thirdparty.service.IWarehouseOPSService;
import com.wimoor.erp.thirdparty.service.IWarehouseXLSService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.web.bind.annotation.*;

@Api(tags = "第三方API K5")
@RestController
@RequestMapping("/api/v1/thirdparty/warehouse")
@RequiredArgsConstructor
public class ThirdpartyWarehouseController {
    final IWarehouseK5Service warehouseK5Service;
    final IWarehouseOPSService warehouseOPSService;
    final IWarehouseXLSService warehouseXLSService;
    final IThirdPartyAPIService thirdPartyApiService;
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
            }else{
                return Result.success();
            }
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/list")
    public Result<?> listAction(String apiid){
            ThirdPartyAPI api = thirdPartyApiService.getById(apiid);
            if(api.getSystem().equals("K5")){
                return Result.success(warehouseK5Service.listByApi(api));
            }else  if(api.getSystem().equals("OPS")){
                return Result.success(warehouseOPSService.listByApi(api));
            }else  if(api.getSystem().equals("XL")){
                return Result.success(warehouseXLSService.listByApi(api));
            }else{
                return Result.success();
            }
    }
}
