package com.wimoor.sys.tool.pojo.dto;

import java.io.Serializable;
import java.util.List;

import com.wimoor.sys.tool.pojo.entity.SysQueryVersionFeild;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("全部系统标签对象")
public class SysQueryVersionDTO  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6680544072580402154L;
	List<SysQueryVersionFeild> fieldlist;
    String queryname;
    String versionname;
}
