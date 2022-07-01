package com.wimoor.common.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_serial_num")
public class SerialNum extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1480236316318230896L;

	@TableField(value= "ftype")
    private String ftype;

    @TableField(value= "seqno")
    private Integer seqno;

    @TableField(value= "prefix_date")
    private Date prefixDate;

    @TableField(value= "opttime")
    private Date opttime;

    @TableField(value= "shopid")
    private String shopid;
     
}