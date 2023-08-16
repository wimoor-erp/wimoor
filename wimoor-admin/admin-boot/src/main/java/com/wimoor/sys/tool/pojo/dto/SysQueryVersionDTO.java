package com.wimoor.sys.tool.pojo.dto;

import java.util.List;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("全部系统标签对象")
public class SysQueryVersionDTO {
    List<String> fieldlist;
    String queryname;
    String versionname;
}
