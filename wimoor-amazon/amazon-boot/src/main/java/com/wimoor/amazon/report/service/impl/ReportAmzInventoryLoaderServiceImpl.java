package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;


import org.springframework.stereotype.Service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryCountryReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IAmzInventoryCountryReportService;

import lombok.extern.slf4j.Slf4j;
 
 
@Slf4j
@Service("reportAmzInventoryLoaderService")
public class ReportAmzInventoryLoaderServiceImpl extends ReportServiceImpl  implements IAmzInventoryCountryReportService {
 
 
	/**
	 * afn-researching-quantity	afn-reserved-future-supply	afn-future-supply-buyable
	 */
	@SuppressWarnings("unused")
	public synchronized String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String mlog="";
		int lineNumber = 0;
		AmzInventoryCountryReport record = null;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
                System.out.println(line);				 
				lineNumber++;
			}
	
			  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("ReportAmzInventoryService:"+e.getMessage());
			e.printStackTrace();
		}  finally {
				if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
	}
      return mlog;
	}
	

  

	@Override
	public String myReportType() {
		return  ReportType.InventoryLoader;
	}

 
	
}
