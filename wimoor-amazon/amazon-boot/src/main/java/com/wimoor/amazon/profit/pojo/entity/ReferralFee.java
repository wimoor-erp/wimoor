package com.wimoor.amazon.profit.pojo.entity;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_referralfee")  
@ApiModel(value="ReferralFee对象", description="佣金")
public class ReferralFee implements Serializable{
	/**
	 * 
	 */
	@TableField(exist=false)
	private static final long serialVersionUID = -3486567050320560116L;

	@TableId(value= "id")
    private Integer id;

	@NotNull(message="类型不能为空")
	@Size(max=50,message="类型的长度不能超过50个字符")
	@TableField(value= "type")
    private String type;

	@TableField(value= "isMedia")
    private String ismedia;

    @TableField(value= "name")
    private String name;

    @TableField(value= "loweast")
    private BigDecimal loweast;

    @TableField(value= "top1")
    private BigDecimal top1;

    @TableField(value= "top2")
    private BigDecimal top2;

    @TableField(value= "top3")
    private BigDecimal top3;

    @TableField(value= "percent1")
    private BigDecimal percent1;

    @TableField(value= "percent2")
    private BigDecimal percent2;

    @TableField(value= "percent3")
    private BigDecimal percent3;
    
    @NotNull(message="国家不能为空")
	@TableField(value= "country")
    private String country;
    
    @TableField(value= "parent_id")
    private Integer parentId;
 
    public BigDecimal getTop(int index){
    	switch(index){
    	case 1:return top1;
    	case 2:return top2;
    	case 3:return top3;
    	}
    	return null;
    }
    
    public BigDecimal getPercent(int index){
    	switch(index){
    	case 1:return percent1;
    	case 2:return percent2;
    	case 3:return percent3;
    	}
    	return null;
    }

}