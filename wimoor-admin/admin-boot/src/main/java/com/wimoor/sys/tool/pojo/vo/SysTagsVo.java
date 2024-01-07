package com.wimoor.sys.tool.pojo.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("全部系统标签对象")
public class SysTagsVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3195900481740535439L;

	String value;
	
	String label;
	
	List<SysTagsChildrenVo> children;
}
