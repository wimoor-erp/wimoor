package com.wimoor.sys.gc.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 *  通用Vo,获取反序列类生成UUID
 *
 * @author 王松
 * @WX-QQ 1720696548
 * @date 2019/10/31 21:12
 * spring
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseVo extends Convert {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5966597609324546448L;

	/**
     * id
     */
    private String id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
