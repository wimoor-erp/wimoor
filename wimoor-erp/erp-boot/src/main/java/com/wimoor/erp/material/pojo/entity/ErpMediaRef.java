package com.wimoor.erp.material.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 媒体-商品关联表（对标 PDM sku_pic_pack）
 * 通过 ref_type 区分 SPU 池（0）/ SKU 展示图（1）
 * 通过 platform + marketplace_id + slot_position 表达多平台 Listing 绑定
 *
 * @author wimoor
 */
@Data
@ApiModel(value = "ErpMediaRef对象", description = "媒体-商品关联")
@TableName("t_erp_media_ref")
public class ErpMediaRef {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键(Snowflake)")
    private String id;

    @ApiModelProperty(value = "FK → t_erp_media.id")
    @TableField("media_id")
    private String mediaId;

    @ApiModelProperty(value = "FK → t_erp_material.id")
    @TableField("material_id")
    private String materialId;

    @ApiModelProperty(value = "企业ID")
    @TableField("shopid")
    private String shopid;

    @ApiModelProperty(value = "关联类型: 0=SPU级图片池 1=SKU级展示图")
    @TableField("ref_type")
    private Integer refType;

    @ApiModelProperty(value = "分配角色: 1=成品图 2=橱窗图 3=公共图 4=说明图 5=场景图")
    @TableField("pic_class")
    private Integer picClass;

    @ApiModelProperty(value = "排序(越小越靠前)")
    @TableField("sort_order")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否主图 0=否 1=是")
    @TableField("is_main")
    private Integer isMain;

    @ApiModelProperty(value = "平台标识(amazon/tiktok/ebay) NULL=通用")
    @TableField("platform")
    private String platform;

    @ApiModelProperty(value = "站点ID(用于多站点差异图)")
    @TableField("marketplace_id")
    private String marketplaceId;

    @ApiModelProperty(value = "图片位(MAIN/PT01~PT08) 用于Listing绑定")
    @TableField("slot_position")
    private String slotPosition;

    @ApiModelProperty(value = "创建人")
    @TableField("creator")
    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    // ==== ref_type 常量 ====
    public static final int REF_TYPE_SPU_POOL = 0;
    public static final int REF_TYPE_SKU_DISPLAY = 1;

    // ==== pic_class 常量 ====
    public static final int PIC_CLASS_FINISHED = 1;
    public static final int PIC_CLASS_SHOWCASE = 2;
    public static final int PIC_CLASS_PUBLIC = 3;
    public static final int PIC_CLASS_INSTRUCTION = 4;
    public static final int PIC_CLASS_SCENE = 5;

    // ==== 主图标记 ====
    public static final int MAIN_YES = 1;
    public static final int MAIN_NO = 0;

    // ==== Amazon slot 常量 ====
    public static final String SLOT_MAIN = "MAIN";
    public static final String PLATFORM_AMAZON = "amazon";
}
