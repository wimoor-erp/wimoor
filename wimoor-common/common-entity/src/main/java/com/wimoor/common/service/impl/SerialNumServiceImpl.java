package com.wimoor.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mapper.SerialNumMapper;
import com.wimoor.common.pojo.entity.SerialNum;
import com.wimoor.common.service.ISerialNumService;
 

@Service("serialNumService")
public class SerialNumServiceImpl extends  ServiceImpl<SerialNumMapper,SerialNum> implements ISerialNumService {

	
	public String readSerialNumber(String shopid, String profix) throws Exception {
	profix=profix.toUpperCase();
 
	QueryWrapper<SerialNum> queryWrapper = new QueryWrapper<SerialNum>();
    SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
    String today=matter.format(new Date());
    queryWrapper.eq("ftype", profix);
    queryWrapper.eq("shopid", shopid);
    queryWrapper.eq("prefix_date",today);
	List<SerialNum> snlist= this.list(queryWrapper);
    if(snlist!=null&&snlist.size()>0){
    	SerialNum serialNum = snlist.get(0);
    	serialNum.setSeqno(serialNum.getSeqno()+1);
    	super.updateById(serialNum);
        profix=getNum(profix,serialNum.getSeqno());
    }
    else {
    	SerialNum serialNum = new SerialNum();
    	serialNum.setFtype(profix);
    	serialNum.setPrefixDate(GeneralUtil.getDatez(today));
    	serialNum.setSeqno(1);
    	serialNum.setShopid(shopid);
    	serialNum.setOpttime(new Date());
    	super.save(serialNum);
    	profix=getNum(profix,serialNum.getSeqno());
    }
	return profix;
	
}	
	public String findSerialNumber(String shopid, String profix) throws Exception {
		profix=profix.toUpperCase();
 
		QueryWrapper<SerialNum> queryWrapper = new QueryWrapper<SerialNum>();
	    SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
	    Date today=GeneralUtil.getDatez(matter.format(new Date()));
	    queryWrapper.eq("ftype", profix);
	    queryWrapper.eq("shopid", shopid);
	    queryWrapper.eq("prefix_date",today);
		List<SerialNum> snlist= this.list(queryWrapper);
	    if(snlist!=null&&snlist.size()>0){
	    	SerialNum serialNum = snlist.get(0);
	    	serialNum.setSeqno(serialNum.getSeqno()+1);
	        profix=getNum(profix,serialNum.getSeqno());
	    }
	    else {
	    	SerialNum serialNum = new SerialNum();
	    	serialNum.setFtype(profix);
	    	serialNum.setPrefixDate(today);
	    	serialNum.setSeqno(1);
	    	serialNum.setShopid(shopid);
	    	serialNum.setOpttime(new Date());
	    	profix=getNum(profix,serialNum.getSeqno());
	    }
		return profix;
	}	
public   String getNum(String profix,Integer num){
		  String strDate="";//流水号时间
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		  Date date = new Date();
		  //格式化当前时间，并按字符'-'分割
		  String[] sd=sdf.format(date).split("-");
		  //截取编号中的日期
		  for(int i=0;i<sd.length;i++){
		   strDate+=sd[i];
		  }
		  String s2="";
		  Integer lg=num;
		  //对流水号结尾的四位数字进行判断，以便增加
		  if(lg>0&&lg<9){
		   s2="000"+(lg);
		  }else if(lg>=9&&lg<99){
		   s2="00"+(lg);
		  }else if(lg>=99&&lg<999){
		   s2="0"+(lg);
		  }else if(lg>=999&&lg<9999){
		   s2=""+(lg);
		  }else {
		   s2=""+(lg);
		  }
		  //返回自动生成后的流水号
		  return profix+=strDate+s2;
		 }

 	 
}
