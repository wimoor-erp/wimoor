package com.wimoor.admin.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeptVO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5253368756977859991L;

	private String id;

    private String name;

    private Integer parentId;

    private String treePath;

    private Integer sort;

    private Integer status;

    private String leader;

    private String mobile;

    private String email;


    private List<DeptVO> children;

}
