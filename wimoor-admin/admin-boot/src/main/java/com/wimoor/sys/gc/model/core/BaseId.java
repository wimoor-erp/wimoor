package com.wimoor.sys.gc.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *  通用请求对象
 *
 *  <P>
 *      适应场景
 *      1、根据 id 进行数据操作，如：发送消息,修改数据指定内容 （此时传递id即可）
 *  </P>
 *
 * @author 王松
 * @WX-QQ 1720696548
 * @date 2019/10/31 21:12
 * spring
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseId extends Convert {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4442995388287844012L;
	/**
     * id
     */
    private String id;

}
