package com.wimoor.amazon.finances.service;


public interface IAmzFinEmailService {
	public void sendWeekEmailDetailTask();
	public void sendMonthEmailDetailTask() ;
	public void sendWeekEmailDetail();
	public void sendMonthEmailDetail() ;
	public void sendWeekEmailDetail(String shopid);
	public void sendMonthEmailDetail(String shopid);
}
