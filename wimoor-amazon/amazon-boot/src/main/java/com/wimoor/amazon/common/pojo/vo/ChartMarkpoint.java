package com.wimoor.amazon.common.pojo.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChartMarkpoint  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4540630412100021744L;
	String name;
	String value;
	String xAxis;
	Object yAxis;
}
