package com.wimoor.admin.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class NotifyDTO extends BasePageQuery {
String targetType;
}
