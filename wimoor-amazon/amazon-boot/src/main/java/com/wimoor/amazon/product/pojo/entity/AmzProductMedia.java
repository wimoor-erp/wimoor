package com.wimoor.amazon.product.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Amazon SP-API 商品图片缓存表。
 * 同一 SKU + variant（MAIN/PT01..PT08/SWCH）唯一。
 *
 * @author wimoor team
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_product_media")
@ApiModel(value = "AmzProductMedia对象", description = "Amazon商品媒体缓存")
public class AmzProductMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键(Snowflake)")
    private String id;

    @ApiModelProperty(value = "企业ID")
    @TableField("shopid")
    private String shopid;

    @ApiModelProperty(value = "授权ID")
    @TableField("authority_id")
    private String authorityId;

    @ApiModelProperty(value = "站点ID")
    @TableField("marketplace_id")
    private String marketplaceId;

    @ApiModelProperty(value = "SKU")
    @TableField("sku")
    private String sku;

    @ApiModelProperty(value = "ASIN")
    @TableField("asin")
    private String asin;

    @ApiModelProperty(value = "媒体类型: 0=图片 1=视频")
    @TableField("media_type")
    private Integer mediaType;

    @ApiModelProperty(value = "图片位: MAIN/PT01~PT08/SWCH")
    @TableField("variant")
    private String variant;

    @ApiModelProperty(value = "排序")
    @TableField("sort_order")
    private Integer sortOrder;

    @ApiModelProperty(value = "Amazon CDN URL")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "本地落地路径(可选)")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "宽度px")
    @TableField("width")
    private Integer width;

    @ApiModelProperty(value = "高度px")
    @TableField("height")
    private Integer height;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("sync_time")
    private Date syncTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    // 常量
    public static final String VARIANT_MAIN = "MAIN";
}
