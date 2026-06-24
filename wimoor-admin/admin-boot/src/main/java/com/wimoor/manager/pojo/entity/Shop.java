package com.wimoor.manager.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop")
@ApiModel(value="Shop对象", description="店铺表")
public class Shop extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "邀请码")
    private String invitecode;

    @ApiModelProperty(value = "受邀请码")
    private String fromcode;

    @ApiModelProperty(value = "oldid")
    private String oldid;

    @ApiModelProperty(value = "老板邮箱")
    private String bossEmail;
}