package com.wimoor.admin.pojo.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

 
@Data
@ApiModel("权限视图对象")
public class PermissionVO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8204968918004411341L;

	@ApiModelProperty("权限ID")
    private Long id;

    @ApiModelProperty("菜单ID")
    private Long menu_id;
    
    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("URL权限标识-服务名称")
    private String serviceName;

    @ApiModelProperty("URL权限标识-请求标识")
    private String requestMethod;

    @ApiModelProperty("URL权限标识-请求方式")
    private String requestPath;

    @ApiModelProperty("按钮权限标识")
    private String btnPerm;

}
