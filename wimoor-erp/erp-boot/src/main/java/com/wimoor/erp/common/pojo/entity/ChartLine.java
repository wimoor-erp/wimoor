package com.wimoor.erp.common.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class ChartLine  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5835784630292609966L;
	List<ChartPoint> markpoint;
	List<ChartPoint> markpoint2;
	List<Object> data;
	List<Object> data2;
	List<Object> data3;
	List<Object> data4;
	Object summary;
	String name;
}
