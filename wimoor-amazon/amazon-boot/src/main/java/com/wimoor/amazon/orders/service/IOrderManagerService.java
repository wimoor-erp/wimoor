package com.wimoor.amazon.orders.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersDetailVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersShipVo;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.common.user.UserInfo;

public interface IOrderManagerService{

	IPage<AmazonOrdersVo> selectOrderList(AmazonOrdersDTO dto);
 
	IPage<AmazonOrdersShipVo> getOrderAddressList(Map<String, Object> paramMap, Page<AmazonOrdersShipVo> page);

	void setAddressExcelBook(SXSSFWorkbook workbook, Map<String, Object> paramMap);

	String setAmzOrderVatHandler(UserInfo userinfo, String groupid, String country, String orderid, String itemstatus,
			String postDate, String vatlabel, String vattype,String ordertype);

	List<AmazonOrdersDetailVo> selectOrderDetail(Map<String, Object> paramMap);

	Map<String, Object> selectVatInfo(String groupid);

	int saveAmazonVat(String shopid, String groupid, String vatcompany, String vatcountry, String vatprovince,
			String vatcity, String vataddress, String vatphone, String vatpostal, String vatemail, String vatsign,
			String image, Map<String, Object> vatfeeMap,InputStream stream,String filename);

	public Map<String,Object> setAmzOrderVatInvoicePDF(String shopid, com.itextpdf.text.Document document, String orderid,
			String language, String groupid, String vatlabel, String vattype, String country, String postDate,
			String itemstatus,String ordertype,String invoiceno); 
	
	public AmzOrderMain saveOrderDetail(String orderid, AmazonAuthority amazonAuthority, String itemstatus,boolean nonAddress);
	
	public List<AmazonOrdersDetailVo> selectOrderItemDetail(Map<String, Object> paramMap);

	Map<String, Object> getParamOfSummaryOrder(AmazonOrdersDTO condition);

	public List<AmazonOrdersVo> selectDownloadOrderList(AmazonOrdersDTO dto);
	
	public void setOrdersExcelBook(SXSSFWorkbook workbook, AmazonOrdersDTO dto);
	
	public String refreshAmzVatInvoinceStatus(String orderid);

	List<OrdersFinancial> lastShippedOrderFin(AmazonAuthority auth, ProductInfo info);

}
