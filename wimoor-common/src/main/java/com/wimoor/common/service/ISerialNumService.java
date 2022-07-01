package com.wimoor.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.pojo.entity.SerialNum;

public interface ISerialNumService extends IService<SerialNum> {
	public String readSerialNumber(String shopid, String profix) throws Exception ;
	public String findSerialNumber(String shopid, String profix) throws Exception ;
	public String getNum(String profix,Integer num);
}
