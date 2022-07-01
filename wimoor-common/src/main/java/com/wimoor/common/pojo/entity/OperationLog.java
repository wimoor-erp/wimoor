package com.wimoor.common.pojo.entity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_operationlog")
public class OperationLog   extends BaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 5224375654057826320L;

    private Date time;

    private String ip;

    private String userid;

    private String username;

    private String logtype;

    private String method;

    private String param;
    
    private String exceptiondetail;
    
    private String description;
    
 
	public void setParam(String param) {
		if(param.length()>=3990) {
			param=param.substring(0, 3990);
		}
		this.param = param;
	}
 

    
}