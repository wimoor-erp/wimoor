package com.wimoor.sys.gc.model.core;


import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *  通用Entity
 *
 * @author 王松
 * @WX-QQ 1720696548
 * @date 2019/10/31 21:12
 * spring
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseDto extends Convert {/**
	 * 
	 */
	private static final long serialVersionUID = -7187158313206747071L;

//    @ApiModelProperty(value = "id--> 添加不传，修改必传")
//    private String id;
}
