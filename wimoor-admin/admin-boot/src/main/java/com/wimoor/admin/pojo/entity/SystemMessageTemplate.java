package com.wimoor.admin.pojo.entity;
import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
@TableName("t_sys_message_template")
public class SystemMessageTemplate {
	
	@TableId(value= "id")
    private Integer id;

	@TableField(value= "ftype")
    private String ftype;

	@TableField(value= "content")
    private String content;

	@TableField(value= "operator")
    private BigInteger operator;

	@TableField(value= "opttime")
    private Date opttime;
 
    
}