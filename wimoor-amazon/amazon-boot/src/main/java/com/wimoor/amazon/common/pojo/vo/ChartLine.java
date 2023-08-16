package com.wimoor.amazon.common.pojo.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class ChartLine  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5835784630292609966L;
	List<ChartMarkpoint> markpoint;
	List<ChartMarkpoint> markpoint2;
	List<Object> data;
	List<Object> data2;
	List<Object> data3;
	List<Object> data4;
	Object summary;
	String name;
}
