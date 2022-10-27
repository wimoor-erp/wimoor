package com.wimoor.manager.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_manager_limit_append")
@ApiModel(value="ManagerLimitAppend对象", description="")
public class ManagerLimitAppend extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;


    private BigInteger shopid;

    private Integer tariffpackage;

    private Integer tariffpackageAppendId;

    private String ftype;

    private Integer num;

    private LocalDate effecttime;

    private LocalDate losingeffect;

    private Boolean isclose;

    private LocalDateTime opttime;


}
