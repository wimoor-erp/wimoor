package com.wimoor.sys.email.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_mail_template")
@ApiModel(value="SysMailTemplate对象", description="")
public class SysMailTemplate implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String mailSubject;

    @ApiModelProperty(value = "1、系统通知邮件")
    private Integer ftype;

    private BigInteger shopid;

    private String content;


}
