package com.wimoor.amazon.notifications.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订阅消息对象
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_notifications_subscriptions")
@ApiModel(value="AmzNotificationsSubscriptions对象", description="订阅消息对象")
public class AmzNotificationsSubscriptions implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订阅ID")
   @TableId("subscriptionId")
    private String subscriptionId;

    @ApiModelProperty(value = "订阅类型")
    @TableField("eventFilterType")
    private String eventFilterType;

    @ApiModelProperty(value = "版本")
    @TableField("payloadVersion")
    private String payloadVersion;

    @ApiModelProperty(value = "接受信息的目标")
    @TableField("destinationId")
    private String destinationId;

    @ApiModelProperty(value = "聚合时间")
    @TableField("aggregationSettings")
    private String aggregationSettings;

    @ApiModelProperty(value = "站点")
    @TableField("marketplaceIds")
    private String marketplaceIds;

    @ApiModelProperty(value = "刷新时间")
    private LocalDateTime refreshtime;

    @ApiModelProperty(value = "授权ID")
    private BigInteger amazonauthid;


}
