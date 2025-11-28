package com.wimoor.common.pojo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.bean.BeanUtil;
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
    private int pagesize = 1000;
    
    @ApiModelProperty(value = "排序字段", example = "10")
    private String  sort = "";
    
    @ApiModelProperty(value = "排序类型(desc降序，asc升序)", example = "10")
    private String  order = "";
    
    @ApiModelProperty(value = "追加参数", example = "param.paramother.mykey=xxxx")
    private Map<String,String> paramother=new HashMap<String,String>();
    
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
    
    
	int compareTo(Object o1,Object o2){
	    if(o1==null&&o2==null)return 0;
		if(o1==null )return -1;
	    if(o2==null) return 1;
	    if(o1 instanceof BigDecimal) {
	    	BigDecimal v1=(BigDecimal)o1;
	    	BigDecimal v2=null;
	    	if(o2 instanceof Long) {
	    		v2=new BigDecimal(o2.toString());
	    	}else {
	    		v2=(BigDecimal)o2;
	    	}
	    	return v1.compareTo(v2);
	    }
	    else if(o1 instanceof String) {
	    	String v1=(String)o1;
	    	String v2=(String)o2;
	    	return v1.compareTo(v2);
	    }
        else if(o1 instanceof Date) {
    	   Date v1=(Date)o1;
    	   Date v2=(Date)o2;
	    	return v1.compareTo(v2);
	    }else if(o1 instanceof LocalDateTime) {
	    	LocalDateTime v1=(LocalDateTime)o1;
	    	LocalDateTime v2=(LocalDateTime)o2;
		    	return v1.compareTo(v2);
		 }
        else if(o1 instanceof Integer) {
        	Integer v1=(Integer)o1;
        	Integer v2=(Integer)o2;
 	    	return v1.compareTo(v2);
 	    }
        else if(o1 instanceof Long) {
        	Long v1=(Long)o1;
        	Long v2=null;
        	if(o2 instanceof BigDecimal) {
	    		v2= new BigDecimal(o2.toString()).longValue();
	    	}else {
	    		v2=(Long)o2;
	    	}
 	    	return v1.compareTo(v2);
 	    }
        else if(o1 instanceof Float) {
        	Float v1=(Float)o1;
        	Float v2=(Float)o2;
 	    	return v1.compareTo(v2);
 	    }else {
 	    	throw new BizException("排序字段类型无法匹配，请联系管理员");
 	    }
	}
	
	public <T> void sort(List<T> mylist){
		Collections.sort(mylist, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				 Map<String, Object> m1 = BeanUtil.beanToMap(o1);
				 Map<String, Object> m2 = BeanUtil.beanToMap(o2);
				 if(order!=null&&order.equals("desc")) {
					 return compareTo(m2.get(sort),m1.get(sort));
				 }else  {
					 return compareTo(m1.get(sort),m2.get(sort));
				 }
			}
		});
	}
	
	public <T> IPage<T>  getListPage(List<T> mylist){
		 if(StrUtil.isNotBlank(sort)) {
	        	if(StrUtil.isBlank(order)) {order="asc";} 
	        	sort(mylist);
		 }
		IPage<T> result=new Page<T>(currentpage,pagesize,mylist.size());
		if(mylist.size()>0) {
			result.setRecords(mylist.subList((currentpage-1)*pagesize, mylist.size()>currentpage*pagesize?currentpage*pagesize:mylist.size()));
		}
		return result;
	}
}
