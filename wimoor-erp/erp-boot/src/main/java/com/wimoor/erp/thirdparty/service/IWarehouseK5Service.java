package com.wimoor.erp.thirdparty.service;

import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import org.apache.http.HttpException;

import java.util.List;
import java.util.Map;

public interface IWarehouseK5Service {
    public List<Map<String, Object>> searchStock(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) throws HttpException;
    public List<ThirdPartyWarehouse> searchStartHouse(ThirdPartyAPI api) throws HttpException;
    List<ThirdPartyWarehouse> listByApi(ThirdPartyAPI api);
}
