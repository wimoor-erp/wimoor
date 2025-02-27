package com.wimoor.erp.purchase.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ConsumablePlanDTO对象", description="采购耗材")
public class PreprocessFormListDTO extends BasePageQuery {
    List<String> selectList;
    List<String> tableList;
    String runid;
    String shopid;
    String warehouseid;
    String remark;
    Integer ftype;
}
