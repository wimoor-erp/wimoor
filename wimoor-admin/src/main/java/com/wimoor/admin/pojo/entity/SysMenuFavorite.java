package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单收藏
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_menu_favorite")
@ApiModel(value="SysMenuFavorite对象", description="菜单收藏")
public class SysMenuFavorite implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;

    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "用户ID")
    private BigInteger userid;

    @ApiModelProperty(value = "菜单ID")
    private BigInteger menuid;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;


}
