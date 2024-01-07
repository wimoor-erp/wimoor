package com.wimoor.admin.pojo.dto;

import lombok.Data;

@Data
public class SysEmailDTO {
String toCompany;
String toUserid;
String ccCompany;
String ccUserid;
String toEmail;
String subject;
String content;
}
