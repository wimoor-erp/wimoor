package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_user_bind")
@ApiModel(value="SysUserBind对象", description="")
public class SysUserBind implements Serializable {

    private static final long serialVersionUID=1L;
    
    @TableId(value= "userid")
    private BigInteger userid;

    private BigInteger bindid;


}
