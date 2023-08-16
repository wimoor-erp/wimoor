package com.wimoor.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.pojo.entity.SerialNum;

public interface ISerialNumService extends IService<SerialNum> {
	/**
	 * 当需要使用编码时要通过readSerialNumber 将编码读取出来，读取出来的编码将会被消耗掉
	 * @param shopid
	 * @param profix
	 * @return
	 * @throws Exception
	 */
	public String readSerialNumber(String shopid, String profix) throws Exception ;
	
	/**
	 * 查看当前可以使用的编码，但是不会消耗此编码，多次读取将会返回同一个编码
	 * @param shopid
	 * @param profix
	 * @return
	 * @throws Exception
	 */
	public String findSerialNumber(String shopid, String profix) throws Exception ;
	
	/**
	 * 根据前缀和序号生成编码，生成的编码不在系统中保留
	 * @param profix
	 * @param num
	 * @return
	 */
	public String getNum(String profix,Integer num);
}
