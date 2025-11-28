package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
@Data
@TableName("t_sys_local_holiday")
public class SysLocalHoliday {
    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String shopid;
    /** 年 */
    private Integer year;

    /** 月 */
    private Integer month;

    /** 日 */
    private Integer day;

    /** 节日名称 */
    private String name;

    /** 展示内容 */
    private String showName;

    /** 星期 */
    private String weekName;

    /** 0工作日 1周末 2节日 3调休 */
    private Integer type;

    private String typeStr;

    /** 用于同步API节假日的key */
    private String keyDate;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date curDate;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /** 农历日 */
    private String lunars;

    /** 农历日期 */
    private String lunarsDate;

    /** 是否置灰 0不置灰 1置灰 */
    private Integer isGray;

    String creator;

    Date createtime;

    String operator;

    Date opttime;
}
