package com.wimoor.feishu.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_feishu_leave_calendar")
@ApiModel(value="LeaveCalendar对象", description="")
public class LeaveCalendar implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId
    private String timeoffEventId;
    
    private String uuid;
    
    private String appid;
    
    private String eventContentType;

    private Date startDate;

    private Date endDate;
    
    private String userid;
    
    private Boolean isdelete;

    private String logs;

    private Date opttime;


}
