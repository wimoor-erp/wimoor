package com.wimoor.amazon.notifications.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 亚马逊Destination 亚马逊消息接受对象
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_notifications_destination")
@ApiModel(value="AmzNotificationsDestination对象", description="亚马逊Destination 亚马逊消息接受对象")
public class AmzNotificationsDestination implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "接受消息对象的ID")
    @TableId(value="destinationid")
    private String destinationid;

    @ApiModelProperty(value = "接受消息对象的名称")
    @TableField(value="name")
    private String name;

    @ApiModelProperty(value = "消息队列arn")
    @TableField(value="resource_sqs_arn")
    private String resourceSqsArn;

    @ApiModelProperty(value = "本地bridge区域")
    @TableField(value="resource_event_bridge_region")
    private String resourceEventBridgeRegion;

    @ApiModelProperty(value = "本地bridge账号")
    @TableField(value="resource_event_bridge_accountid")
    private String resourceEventBridgeAccountid;

    @ApiModelProperty(value = "本地bridge名称")
    @TableField(value="resource_event_bridge_name")
    private String resourceEventBridgeName;
    
    @ApiModelProperty(value = "刷新时间")
    @TableField(value="refreshtime")
    private Date refreshtime;
    
    @ApiModelProperty(value = "授权ID")
    @TableField(value="awsregion")
    private String awsregion;
}
