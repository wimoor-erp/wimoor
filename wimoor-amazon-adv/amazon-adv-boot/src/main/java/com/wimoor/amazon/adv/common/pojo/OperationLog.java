package com.wimoor.amazon.adv.common.pojo;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name="t_sys_operationlog")
@Data
public class OperationLog   extends BaseObject{
 
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