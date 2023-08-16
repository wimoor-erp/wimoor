package com.wimoor.sys.tool.pojo.entity;

import java.io.Serializable;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
@Data
@TableName("t_sys_query_version_feild")
@ApiModel(value="SysQueryVersionFeild对象", description="")
public class SysQueryVersionFeild implements Serializable {

    private static final long serialVersionUID=1L;

    private BigInteger fversionid;

    private String ffield;

    private Integer findex;


}
