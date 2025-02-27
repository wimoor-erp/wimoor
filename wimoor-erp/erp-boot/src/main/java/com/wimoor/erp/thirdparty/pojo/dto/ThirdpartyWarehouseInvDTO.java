package com.wimoor.erp.thirdparty.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import lombok.Data;

@Data
public class ThirdpartyWarehouseInvDTO extends BasePageQuery {
    String api;
    String action;
    String shopid;
    String houseid;
    String sku;
}
