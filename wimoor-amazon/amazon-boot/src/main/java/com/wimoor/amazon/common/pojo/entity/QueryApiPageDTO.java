package com.wimoor.amazon.common.pojo.entity;

import lombok.Data;

@Data
public class QueryApiPageDTO {
	String groupid;
	String marketplaceid;
	Integer	pageSize;
	String	paginationToken;
    String sortBy;
	String sortOrder;
}
