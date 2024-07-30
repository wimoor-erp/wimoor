package com.wimoor.admin.pojo.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class UserInsertDTO {
	String id;
    String account;
    String password;
    String name;
    String company;
    Date losingEffect;
    Boolean disable;
    String deptid;
    List<String> roles;
    List<String> groups;
    List<String> datalimits;
    
}
