package com.wimoor.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 树形下拉视图对象
 */
@Data
@NoArgsConstructor
public class TreeSelectVO {

    public TreeSelectVO(String id, String label) {
        this.id = id;
        this.label = label;
    }

    private String id;

    private String label;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TreeSelectVO> children;

}
