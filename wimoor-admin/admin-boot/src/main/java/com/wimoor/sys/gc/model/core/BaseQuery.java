package com.wimoor.sys.gc.model.core;


import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 通用查询字段
 * @author wangsong
 * @mail 1720696548@qq.com
 * @date 2021/8/25 0025 11:53
 * @version 1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseQuery extends Convert {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5353401420704266122L;

	/**
     * 页数(小于等于0查询所有,不传默认1)
     */
    private Long current = 1L;

    /**
     * 每页数量(不传默认10)
     */
    private Long size = 10L;

    /**
     * 排序字段
     */
    private String sort = "create_time";

    /**
     * 排序规则 (asc | desc)
     */
    private String order = "desc";

}
