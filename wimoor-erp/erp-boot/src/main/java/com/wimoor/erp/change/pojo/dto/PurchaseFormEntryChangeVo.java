package com.wimoor.erp.change.pojo.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PurchaseFormEntryChangeVo对象", description="换货Vo")
public class PurchaseFormEntryChangeVo {

	    private String id;
	    
	    private String shopid;
	    
	    private String number;

	    private String entryid;

	    private String supplierid;

	    private String logistics;

	    private Integer amount;

	    @ApiModelProperty(value = "1:进行中，0：已完成")
	    private Integer auditstatus;

	    private Integer totalin;

	    private String materialid;

	    private String warehouseid;

	    private String remark;

	    private String operator;

	    private String creator;

	    private Date opttime;

	    private Date createtime;
}
