package com.wimoor.common.pojo.entity;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @desc 基础分页请求对象
 */
@Data
public class BasePageQuery {

    @ApiModelProperty(value = "当前页", example = "1")
    private int currentpage = 1;

    @ApiModelProperty(value = "每页记录数", example = "10")
    private int pagesize = 10;
    
    @ApiModelProperty(value = "排序字段", example = "10")
    private String  sort = "";
    
    @ApiModelProperty(value = "排序类型(desc降序，asc升序)", example = "10")
    private String  order = "";
    
    
    public <T> Page<T> getPage(){
    	Page<T> page=new Page<T>(currentpage,pagesize);
        if(StrUtil.isNotBlank(sort)) {
        	if(StrUtil.isBlank(order)) {order="asc";} 
        		List<OrderItem> orderlist=new ArrayList<OrderItem>();
        		String[] sortarray = sort.split(",");
        		String[] orderarray = order.split(",");
        		for(int i=0;i<sortarray.length;i++) {
        			String msort=sortarray[i];
        			String morder="asc";
        			if(i<orderarray.length) {
        				morder=orderarray[i];
        			}
        			orderlist.add(new OrderItem(msort,"asc".equals(morder.toLowerCase())));
        		}
        		page.addOrder(orderlist);
        }
    	return page;
    }
}
