package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class PackingDTO extends BasePageQuery {
	String formid;
}
