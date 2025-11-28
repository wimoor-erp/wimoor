package com.wimoor.admin.pojo.entity;

 
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_dict")
public class SysDict extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 487346522891840745L;

    private String code;

    private String name;

    private Integer status;

    private  String  remark;

}
