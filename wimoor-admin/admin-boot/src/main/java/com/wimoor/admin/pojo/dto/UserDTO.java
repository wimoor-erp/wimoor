package com.wimoor.admin.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
@Data
public class UserDTO extends BasePageQuery{
    String name;
    String account;
    String roleid;
    String status;
    String shopid;
}
