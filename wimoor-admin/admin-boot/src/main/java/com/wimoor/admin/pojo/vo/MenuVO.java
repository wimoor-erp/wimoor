package com.wimoor.admin.pojo.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.admin.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = true)
public class MenuVO extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2893461561219143113L;

    private String parentId;

    private String name;

    private String icon;

    private String routeName;

    private String routePath;

    private String component;

    private Integer sort;

    private Integer visible;

    private String redirect;
    
    private List<String> permissions;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<MenuVO> children;

}
