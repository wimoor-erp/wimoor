package com.wimoor.sys.gc.model.core;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;


/**
 * 分页对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BasePage<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7623770876620142680L;
	@XjSecret(isNext = true)
    private List<T> records;
    private long total;
    private long size;
    private long current;
    private List<OrderItem> orders;
    private boolean optimizeCountSql;
    private boolean searchCount;
    private boolean optimizeJoinOfCountSql;
    private Long maxLimit;
    private String countId;
}
