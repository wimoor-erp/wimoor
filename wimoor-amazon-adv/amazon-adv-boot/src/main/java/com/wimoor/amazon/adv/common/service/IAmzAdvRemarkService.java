package com.wimoor.amazon.adv.common.service;

import java.util.List;

import com.wimoor.amazon.adv.common.pojo.AmzAdvRemark;
 

public interface IAmzAdvRemarkService extends IService<AmzAdvRemark>{
	int addRemark(List<AmzAdvRemark> list);
	int updateRemark(List<AmzAdvRemark> list);
}
