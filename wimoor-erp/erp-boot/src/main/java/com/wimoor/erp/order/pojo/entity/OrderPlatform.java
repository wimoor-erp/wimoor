package com.wimoor.erp.order.pojo.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
@Data
@ApiModel(value="OrderPlatform对象", description="订单平台")
@TableName("t_erp_order_platform")
public class OrderPlatform extends ErpBaseEntity {
    private String name;
    private String shopid;
    private Boolean disabled;
}
