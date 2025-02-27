package com.wimoor.erp.change.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PurchaseFormEntryChangeDTO对象", description="换货单dto")
public class PurchaseFormEntryChangeDTO extends BasePageQuery{

	String entryid;
	
	String  supplierid;
	
	String id;
	
	String warehouseid;
	
	String remark;
	
	String skumapstr;

	Map<String,Object> skumap;

	private String shopid;

	private String number;

	private String logistics;

	private Integer amount;

	@ApiModelProperty(value = "1:进行中，0：已完成")
	private Integer auditstatus;

	private Integer totalin;

	private String materialid;
	private Boolean withoutInv;

	private String mainid;

	private String assemblyFormId;

	private String disassembleFormId;

	private String operator;

	private String creator;

	private Date opttime;

	private Date createtime;
}
