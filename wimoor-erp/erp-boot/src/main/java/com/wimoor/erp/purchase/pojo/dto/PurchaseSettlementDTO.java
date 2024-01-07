package com.wimoor.erp.purchase.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PurchaseFormDTO对象", description="获取采购列表")
public class PurchaseSettlementDTO extends BasePageQuery{
 
    @ApiModelProperty( value = "开始日期")
    String fromDate;
   
    @ApiModelProperty(value = "结束日期" )
    String toDate;
    
    @ApiModelProperty(value = "账期ID" )
    String settlementid;
    
    @ApiModelProperty(value = "搜索内容" )
    String search;
    
    @ApiModelProperty(value = "银行卡ID" )
    String acct;
    
    @ApiModelProperty(value = "检查异常单据" )
    Boolean ischeck;
}
