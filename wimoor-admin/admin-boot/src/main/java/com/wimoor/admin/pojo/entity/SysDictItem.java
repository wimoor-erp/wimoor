package com.wimoor.admin.pojo.entity;
 
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_dict_item")
public class SysDictItem extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3984144199012663754L;

    private String name;

    private String value;

    private String dictCode;

    private String sort;

    private Integer status;

    private Integer defaulted;

    private String remark;

}
