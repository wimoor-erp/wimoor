package com.wimoor.amazon.report.pojo.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 

@Data
@TableName("t_amz_adv_browsenode")  
@ApiModel(value="AmzBrowseNode对象", description="产品分类")
public class AmzBrowseNode {
    @ApiModelProperty(value = "ID")
	@TableId(value= "id")
    private BigInteger id;

    @ApiModelProperty(value = "名称")
	@TableField(value= "name")
    private String name;

    @ApiModelProperty(value = "父分类ID")
	@TableField(value= "parentid")
    private BigInteger parentid;

    @ApiModelProperty(value = "国家")
	@TableField(value= "country")
    private String country;
    
    @ApiModelProperty(value = "是否顶级分类")
	@TableField(value= "is_category_root")
    private Boolean isCategoryRoot;

    @ApiModelProperty(value = "更新时间")
	@TableField(value= "refreshtime")
    private Date refreshtime;
	
    @ApiModelProperty(value = "等级")
	@TableField(value= "level")
    private Integer level;
	
    @TableField(exist = false)
    Map<BigInteger,AmzBrowseNode> childrenMap;

  
    public Map<BigInteger, AmzBrowseNode> getChildrenMap() {
		return childrenMap;
	}

	public void addChildrenMap(AmzBrowseNode child) {
		if(childrenMap==null) {
			childrenMap=new HashMap<BigInteger,AmzBrowseNode>();
		}
		 childrenMap.put(child.getId(), child);
	}

	public void addAllChildrenMap(Map<BigInteger,AmzBrowseNode> allchild) {
		if(childrenMap==null) {
			childrenMap=new HashMap<BigInteger,AmzBrowseNode>();
		}
		 childrenMap.putAll(allchild);;
	}
 
 
 
}