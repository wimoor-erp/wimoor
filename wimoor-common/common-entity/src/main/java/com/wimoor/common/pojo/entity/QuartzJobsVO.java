package com.wimoor.common.pojo.entity;

import lombok.Data;

@Data
public class QuartzJobsVO {
    private String jobDetailName;
    private String jobCronExpression;
    private String timeZone;
    private String groupName;
    private String status;    
}