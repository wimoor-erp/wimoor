package com.wimoor.erp.thirdparty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseBind;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IThirdPartyWarehouseBindService extends IService<ThirdPartyWarehouseBind> {
    List<Map<String,Object>> findByConditon( String shopid,ThirdPartyWarehouseBind dto);
    boolean saveBind(ThirdPartyWarehouseBind dto);
    boolean deleteBind(ThirdPartyWarehouseBind dto);
    boolean asyncLocalInventory(String shopid,String warehouseid,Map<String,Integer> qty);
    void runTask();
}
