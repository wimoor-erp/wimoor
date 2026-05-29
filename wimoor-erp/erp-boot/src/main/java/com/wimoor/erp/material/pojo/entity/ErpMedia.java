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
 * 商品媒体资源元数据表（对标 PDM sku_pic + sku_video）
 * 双表分离架构核心：本表只存媒体本身，关联关系在 {@link ErpMediaRef}
 *
 * @author wimoor
 */
@Data
@ApiModel(value = "ErpMedia对象", description = "商品媒体资源元数据")
@TableName("t_erp_media")
public class ErpMedia {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键(Snowflake)")
    private String id;

    @ApiModelProperty(value = "企业ID(租户隔离)")
    @TableField("shopid")
    private String shopid;

    @ApiModelProperty(value = "媒体类型: 0=图片 1=视频")
    @TableField("media_type")
    private Integer mediaType;

    @ApiModelProperty(value = "用途类型: 10=参考图 30=原图 40=成品图 60=橱窗图 70=公共图 90=说明图 100=场景图")
    @TableField("usage_type")
    private Integer usageType;

    @ApiModelProperty(value = "来源: 1=引用图 2=自拍图 3=白底图 4=Amazon同步 5=批量导入 6=AI生成")
    @TableField("source")
    private Integer source;

    @ApiModelProperty(value = "外部原始URL(如Amazon CDN)")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "对象存储相对路径")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "缩略图/视频封面存储路径")
    @TableField("thumb_location")
    private String thumbLocation;

    @ApiModelProperty(value = "原始文件名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "宽度px")
    @TableField("width")
    private Integer width;

    @ApiModelProperty(value = "高度px")
    @TableField("height")
    private Integer height;

    @ApiModelProperty(value = "文件大小(bytes)")
    @TableField("file_size")
    private Long fileSize;

    @ApiModelProperty(value = "视频时长(秒,仅视频)")
    @TableField("duration")
    private Integer duration;

    @ApiModelProperty(value = "MIME类型")
    @TableField("content_type")
    private String contentType;

    @ApiModelProperty(value = "文件MD5(用于去重)")
    @TableField("md5")
    private String md5;

    @ApiModelProperty(value = "AI处理状态: 0=无需处理 1=处理中 2=处理完成 3=处理失败")
    @TableField("process_status")
    private Integer processStatus;

    @ApiModelProperty(value = "AI处理任务ID(用于回调关联)")
    @TableField("process_task_id")
    private String processTaskId;

    @ApiModelProperty(value = "上传人")
    @TableField("creator")
    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    // ==== 媒体类型常量 ====
    public static final int MEDIA_TYPE_IMAGE = 0;
    public static final int MEDIA_TYPE_VIDEO = 1;

    // ==== 用途类型常量 ====
    public static final int USAGE_REFERENCE = 10;
    public static final int USAGE_ORIGINAL = 30;
    public static final int USAGE_FINISHED = 40;
    public static final int USAGE_SHOWCASE = 60;
    public static final int USAGE_PUBLIC = 70;
    public static final int USAGE_INSTRUCTION = 90;
    public static final int USAGE_SCENE = 100;

    // ==== 来源常量 ====
    public static final int SOURCE_REFERENCE = 1;
    public static final int SOURCE_SELF_SHOT = 2;
    public static final int SOURCE_WHITE_BG = 3;
    public static final int SOURCE_AMAZON_SYNC = 4;
    public static final int SOURCE_BATCH_IMPORT = 5;
    public static final int SOURCE_AI_GENERATED = 6;

    // ==== AI处理状态常量 ====
    public static final int PROCESS_NONE = 0;
    public static final int PROCESS_RUNNING = 1;
    public static final int PROCESS_COMPLETED = 2;
    public static final int PROCESS_FAILED = 3;
}
