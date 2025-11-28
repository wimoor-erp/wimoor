package com.wimoor.admin.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

 
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteVO  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8579026999253086213L;

	private String path;

    private String component;

    private String redirect;

    /**
     * 如果设置为 true
     */
    private Boolean alwaysShow;

    private String name;

    private Boolean hidden;

    private Meta meta;
    
    private Integer sort;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta implements Serializable{
        /**
		 * 
		 */
		private static final long serialVersionUID = 8039809086202742006L;
		private String title;
        private String icon;
        private List<String> permissions;
    }
    private List<RouteVO> children;
}
