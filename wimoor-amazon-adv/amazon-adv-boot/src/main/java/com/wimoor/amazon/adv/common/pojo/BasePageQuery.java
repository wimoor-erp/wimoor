package com.wimoor.amazon.adv.common.pojo;

import java.util.ArrayList;
import java.util.List;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.Order.Direction;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.util.StringUtil;

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
    
    public   PageBounds getPageBounds() { 
		int page = currentpage; //  页号 
		Integer pageSize = pagesize; // 每页数据条数
		PageBounds pageBounds = null;
	 
		if(StringUtil.isNotEmpty(sort)) {
			String[] sortarray = sort.split(",");
			String[] orderarray =null;
			if(StringUtil.isNotEmpty(sort)) {
				orderarray=order.split(",");
			}
		    List<Order> orderlist=new ArrayList<Order>();
			for(int i=0;i<sortarray.length;i++) {
				Direction orderdirect = Order.Direction.DESC;
				if (orderarray!=null&&orderarray.length>i&&StringUtil.isNotEmpty(orderarray[i])&& "asc".contains(orderarray[i])) {
					orderdirect = Order.Direction.ASC;
				} 
				Order orderObject = new Order(sortarray[i], orderdirect, "? ");
				orderlist.add(orderObject);
			}
			pageBounds = new PageBounds(page, pageSize, orderlist, true);
		}  else {
			pageBounds = new PageBounds(page, pageSize, null, true);
		}
		return pageBounds;
	}
    
    
}
