package com.wimoor.amazon.orders.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersInvoice;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersInvoiceVat;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersDetailVo;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.service.ISerialNumService;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
@Data
public class OrderWordHandler {
    ISerialNumService serialNumService;
    IOrderManagerService orderManagerService;
    IAmazonAuthorityService amazonAuthorityService;
    IMarketplaceService marketplaceService;
    
	@SuppressWarnings("unchecked")
	public void setAmzOrderVatInvoiceWord(String shopid, Document document, String orderid, String language,
			String groupid, String vatlabel, String vattype, String country, String postDate, String itemstatus,String ordertype, String invoiceno) {
		String invoiceNo=null;
		if(StrUtil.isNotEmpty(invoiceno)) {
			invoiceNo=invoiceno;
		}
		String preText="";
		if(!ordertype.equals("normal")) {
			preText="-";
		}
		//打印word文档发票
		document.addTitle("Invoice");
		document.addAuthor("wimoor");
		document.addSubject("This is the subject of the Word file.");
		document.addKeywords("This is the keyword of the Word file.");
		// step 3
		document.open();
		// 设置中文字体
		BaseFont bfChinese = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			String vatvoice="";
			String dear="";
			String address1="";
			String address2="";
			String text4="Payment Due Date: ";
			String text6="Order ID: "+orderid;
			if(!ordertype.equals("normal")) {
				text4="Original invoice: "+ invoiceNo;
			}
			try {
				text4+=sdf.format(sdf.parse(postDate));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}	
			try {
				vatvoice = serialNumService.findSerialNumber(shopid, "19");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//拿invoice表中存在的数据
			Map<String,Object> obj=orderManagerService.selectVatInfo(groupid);
			AmzOrdersInvoice OrdersInvoice=(AmzOrdersInvoice) obj.get("data1");
			List<AmzOrdersInvoiceVat> VatList=null;
			if(obj.get("data2")!=null)VatList= (List<AmzOrdersInvoiceVat>) obj.get("data2");
			Float vatRate=null;
			String vatNum=null;
			if(VatList!=null) {
				for(int i=0;i<VatList.size();i++) {
					AmzOrdersInvoiceVat vatfee = VatList.get(i);
					if(vatfee.getCountry().equals(country)) {
						vatNum=vatfee.getVatNum();
						vatRate=vatfee.getVatRate();
					}
				}
			}
			//拿order的数据
			AmzOrderMain orderMain = null;
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("orderid", orderid);
			paramMap.put("purchaseDate", postDate);
			paramMap.put("groupid", groupid);
			paramMap.put("shopid", shopid);
			paramMap.put("nonfin","true");
			List<AmazonOrdersDetailVo> listO = orderManagerService.selectOrderDetail(paramMap);
			if (listO!=null && listO.size()>0) {
				String amazonAuthId= listO.get(0).getAmazonAuthId();
				paramMap.put("amazonAuthId", amazonAuthId);
				String marketplaceId= listO.get(0).getMarketplaceId();
				AmazonAuthority amazonAuthority = amazonAuthorityService.getById(amazonAuthId);
				Marketplace tempmarketplace = marketplaceService.getById(marketplaceId);
				amazonAuthority.setMarketPlace(tempmarketplace);
				orderMain = listO.get(0).getOrderMain();
			}
			//处理order的数据和费用信息
			if(orderMain!=null ) {
				List<AmazonOrdersDetailVo>  itemlist = orderManagerService.selectOrderItemDetail(paramMap);// 从数据库拿amz_order_item里面的数据
				if (itemlist != null && itemlist.size() > 0) {
					listO = itemlist;
				}
				if(orderMain!=null && orderMain.getBuyerAdress()!=null) {
					dear+= orderMain.getBuyerAdress().getName();
					String state=orderMain.getBuyerAdress().getStateOrRegion();
					if(state==null){
						state="";
					}
					String adress = orderMain.getBuyerAdress().getAddressLine1();
					if (orderMain.getBuyerAdress().getAddressLine2() != null) {
						adress =adress+","+ orderMain.getBuyerAdress().getAddressLine2();
					}
					if (orderMain.getBuyerAdress().getAddressLine3() != null) {
						adress =adress+"," +orderMain.getBuyerAdress().getAddressLine3();
					}
					address1+=adress+", "+state;
					address2+=(orderMain.getBuyerAdress().getCity()+
							", "+orderMain.getBuyerAdress().getCountryCode()+" \n"+orderMain.getBuyerAdress().getPostalCode());
				}else {
					dear+="--";address1+="--";address2+="--";
				}
				
			}
			String path = new ClassPathResource("font/arial.ttf").getPath();
			bfChinese = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// 标题字体风格
			Font titleFont = new Font(bfChinese, 10, Font.BOLD);
			Font titleFont2 = new Font(bfChinese, 14, Font.BOLD);
			// 正文字体风格
			Font contextFont = new Font(bfChinese, 9, Font.NORMAL);
			Font vatfont=new Font(bfChinese, 18, Font.BOLD);
			Font datafont1=new Font(bfChinese, 7, Font.BOLD);
			Font datafont2=new Font(bfChinese, 7, Font.NORMAL);
			//段落
			Paragraph ph = new Paragraph();
			Image img=Image.getInstance(OrdersInvoice.getLogourl());
			img.setAbsolutePosition(0, 0);
			img.setAlignment(Image.ALIGN_CENTER);//设置图片显示位置
			img.scaleAbsolute(180,60);//直接设定显示尺寸
			img.scalePercent(50);//表示显示的大小为原尺寸的50% 
			//img.scalePercent(25, 12);//图像高宽的显示比例
			//img.setRotation(30);//图像旋转一定角度
			ph.add(img);
			Table Table = new Table(2,2);
			Table.setPadding(10);
			int width2[] = {100,360};
			Table.setWidths(width2); 
			Table.setAutoFillEmptyCells(true); //自动填满
			Table.setBorder(1);
			Cell cell=new Cell(ph);
			cell.setWidth(230);
			Table.addCell(cell);
			String vatText="INVOICE";
			if(!ordertype.equals("normal")) {
				vatText="Credit Note";
			}
			String NoText="VAT NO:";
			if(vatNum!=null)NoText=NoText+vatNum;
			else NoText=NoText+"--";
			String element2=OrdersInvoice.getCompany();
			String element3= OrdersInvoice.getAddress();
			String text1=OrdersInvoice.getCity()+","+OrdersInvoice.getProvince()+","+OrdersInvoice.getCountry()+
					","+OrdersInvoice.getPostalcode();
			String email=OrdersInvoice.getEmail();
			if(StrUtil.isEmpty(email))email="";
			String text2=OrdersInvoice.getSign();
			String text2_1 = "Paid";
			if(!ordertype.equals("normal")) {
				text2_1="Refunded";
			}
			String text3="Invoice NO: "+vatvoice;
			if(!ordertype.equals("normal")) {
				text3="Credit Note: "+vatvoice;
			}
			String text5="Invoice Issue Date: "+GeneralUtil.formatDate(new Date(), "yyyy-MM-dd");
			if(!ordertype.equals("normal")) {
				text5="Credit note date: "+ GeneralUtil.formatDate(new Date(), "yyyy-MM-dd");
			}
			Paragraph ph2 = new Paragraph(element2);
			Paragraph ph3 = new Paragraph(element3);
			Paragraph phHeader = new Paragraph(vatText);
			Paragraph phNo=null;
			if(vattype.equals("Vat") && vatNum!=null) {
				phNo= new Paragraph(NoText);
				phNo.setFont(contextFont);
				phNo.setAlignment(Element.ALIGN_RIGHT);
			} 
			Paragraph ph5 = new Paragraph(text1);
			Paragraph ph6 = new Paragraph(text2);
			Paragraph ph7 = new Paragraph(dear);
			Paragraph ph8 = new Paragraph(address1);
			Paragraph ph9 = new Paragraph(address2);
			Paragraph ph9_1 = new Paragraph(text2_1);
			Paragraph ph10 = new Paragraph(text3);
			Paragraph ph11 = new Paragraph(text4);
			Paragraph ph12 = new Paragraph(text5);
			Paragraph ph13 = new Paragraph(text6);
			phHeader.setFont(vatfont);
			phHeader.setAlignment(Element.ALIGN_RIGHT);
			ph2.setFont(titleFont);
			ph2.setAlignment(Element.ALIGN_LEFT);
			ph3.setFont(contextFont);
			ph5.setFont(contextFont);
			ph6.setFont(contextFont);
			ph7.setFont(contextFont);
			ph8.setFont(contextFont);
			ph9.setFont(contextFont);
			ph9_1.setFont(titleFont2);
			ph10.setFont(contextFont);
			ph11.setFont(contextFont);
			ph12.setFont(contextFont);
			ph13.setFont(contextFont);
			ph9_1.setAlignment(Element.ALIGN_RIGHT);
			ph10.setAlignment(Element.ALIGN_RIGHT);
			ph11.setAlignment(Element.ALIGN_RIGHT);
			ph12.setAlignment(Element.ALIGN_RIGHT);
			ph13.setAlignment(Element.ALIGN_RIGHT);
			Cell cell2=new Cell();
			cell2.add(phHeader);
			if(phNo!=null)cell2.add(phNo);
			cell2.add(ph2);
			if(StrUtil.isNotEmpty(text2))cell2.add(ph6);
			cell2.add(ph3);
			cell2.add(ph5);
			Table.addCell(cell2);
			Cell cell3=new Cell();
			cell3.setColspan(2);
			cell3.add(ph7);
			cell3.add(ph8);
			cell3.add(ph9);
			cell3.add(ph9_1);
			cell3.add(ph10);
			cell3.add(ph11);
			cell3.add(ph12);
			cell3.add(ph13);
			if(!ordertype.equals("normal")) {
				String text6_1="Total payable: "+preText+orderMain.getOrderTotal().toString();
				Paragraph ph13_1= new Paragraph(text6_1);
				ph13_1.setFont(contextFont);
				ph13_1.setAlignment(Element.ALIGN_RIGHT);
				cell3.addElement(ph13_1);	 
			}
			Table.addCell(cell3);
			//数据表格
			Table dataTable=new Table(6);
			int width3[] =null;
			if(vattype.equals("Vat") && vatNum!=null) {
				dataTable= new Table(8,2);	
				width3=new int[]{211,88,80,167,129,82,77,114};
			}else{
				 dataTable= new Table(6,2);
				 width3=new int[]{211,88,80,167,129,83};
			}
			dataTable.setAutoFillEmptyCells(true); //自动填满
			dataTable.setWidths(width3);
			dataTable.setWidth(100);
			//表头设置
			Paragraph phHeader1=null;
			if(!ordertype.equals("normal")) {
				phHeader1=new Paragraph("Credit note details");
			}else {
				phHeader1=new Paragraph("Product Detail");
			}
			phHeader1.setAlignment(Element.ALIGN_CENTER);
			phHeader1.setFont(datafont1);
			dataTable.addCell(phHeader1);
			Paragraph phHeader2=new Paragraph("Quantity");
			phHeader2.setAlignment(Element.ALIGN_CENTER);
			phHeader2.setFont(datafont1);
			dataTable.addCell(phHeader2);
			Paragraph phHeader3=new Paragraph("Price");
			phHeader3.setAlignment(Element.ALIGN_CENTER);
			phHeader3.setFont(datafont1);
			dataTable.addCell(phHeader3);
			Paragraph phHeader4=new Paragraph("Promotional\nDiscount");
			phHeader4.setAlignment(Element.ALIGN_CENTER);
			phHeader4.setFont(datafont1);
			dataTable.addCell(phHeader4);
			Paragraph phHeader5=new Paragraph("Shipping Fee");
			phHeader5.setAlignment(Element.ALIGN_CENTER);
			phHeader5.setFont(datafont1);
			dataTable.addCell(phHeader5);
			if(vattype.equals("Vat") && vatNum!=null) {
				Paragraph phHeader6=new Paragraph("Vat Rate");
				phHeader6.setAlignment(Element.ALIGN_CENTER);
				phHeader6.setFont(datafont1);
				dataTable.addCell(phHeader6);
				Paragraph phHeader7=new Paragraph("VAT");
				phHeader7.setAlignment(Element.ALIGN_CENTER);
				phHeader7.setFont(datafont1);
				dataTable.addCell(phHeader7);
			}
			Paragraph phHeader8=new Paragraph("Total Price");
			phHeader8.setAlignment(Element.ALIGN_CENTER);
			phHeader8.setFont(datafont1);
			dataTable.addCell(phHeader8);
			//数据行 循环拉取数据
			DecimalFormat    df   = new DecimalFormat("######0.00");
			Object currency="";
			float totalPrice=0.00f,totalitemDiscount=0.00f,totalshipPrice=0.00f,totalVatFee=0.00f,totalBalance=0.00f;
			if(listO!=null && listO.size()>0) {
				for(int i=0;i<listO.size();i++) {
					AmazonOrdersDetailVo item = listO.get(i);
					currency=item.getCurrency();
					if("USD".equals(currency) || "CAD".equals(currency) || "AUD".equals(currency))currency="$";
					if("JPY".equals(currency))currency="¥";
					if("EUR".equals(currency))currency="€";
					if("GBP".equals(currency))currency="￡";
					if("INR".equals(currency))currency="R";
					float discount=0.00f;
					float shipfee=0.00f;
					String vateText="--";
					String totalTxt="--";
					float nums =0.00f;
					float price=0f;
					//不含税价格=售价/(1+vatRate)
					BigDecimal itemprice = new BigDecimal(item.getItemprice()==null?"0":item.getItemprice().toString());
					int quantity = Integer.parseInt(item.getQuantity()==null?"0":item.getQuantity().toString());
					if(itemprice!=null &&  vattype.equalsIgnoreCase("vat") && vatRate!=null) {
						nums= itemprice.floatValue();
						price=itemprice.floatValue()/(1+(vatRate/100));
						price=(float)(Math.round(price*1000))/1000;//保留3位小数
					} else {
						price = itemprice.floatValue();
					}
					totalPrice+=price*quantity;
					if (item.getItemdiscount()!=null) {
						discount += Float.parseFloat(item.getItemdiscount().toString());
					} 
					if (item.getShipdiscount()!=null) {
						discount += Float.parseFloat(item.getShipdiscount().toString());
					} 
					totalitemDiscount+=discount;
					if (item.getShipprice()!=null) {
						shipfee += Float.parseFloat(item.getShipprice().toString());
					} 
					totalshipPrice+=shipfee;
					if(vatRate!=null) {
						vateText=vatRate+"%";
						 if(nums>0){
							 //vat税费=售价*vatRate/(1+vatRate)
							 Float rates = (vatRate/100);
							 nums=(nums*rates)/(1+rates);
							 nums=  (float)(Math.round(nums*1000))/1000;
						 }else{
							 nums=0.00f;
						 }
					}else {
						 nums=0.00f;
					}
					totalVatFee+=nums*quantity;
					float totalitemprice=0.00f;
					if(itemprice!=null&&itemprice.compareTo(BigDecimal.ZERO)==1) {
						totalitemprice= itemprice.floatValue()*quantity-discount+shipfee;
						totalTxt=currency+df.format(totalitemprice);
					}
					Paragraph phData1=new Paragraph(item.getName()==null?"--":item.getName().toString());
							phData1.setFont(datafont2);
							dataTable.addCell(phData1);
							Paragraph phData2=new Paragraph(item.getQuantity()==null?"0":item.getQuantity().toString());
							phData2.setFont(datafont2);
							dataTable.addCell(phData2);
							Paragraph phData3=new Paragraph(preText+currency+GeneralUtil.removeLastZero(price));
							phData3.setFont(datafont2);
							dataTable.addCell(phData3);
							Paragraph phData4=new Paragraph();
							if(discount>0){
								phData4=new Paragraph("- "+currency+discount);
							} else {
								phData4=new Paragraph(preText+currency+"0.00");
							}
							phData4.setFont(datafont2);
							dataTable.addCell(phData4);
							Paragraph phData5=new Paragraph();
							if(shipfee>0){
								phData5=new Paragraph(preText+currency.toString()+shipfee);
							} else {
								phData5=new Paragraph(preText+currency+"0.00");
							}
							phData5.setFont(datafont2);
							dataTable.addCell(phData5);
							if(vattype.equals("Vat") && vatNum!=null) {
								Paragraph phData6=new Paragraph(vateText);
								phData6.setFont(datafont2);
								dataTable.addCell(phData6);
								Paragraph phData7=new Paragraph(preText+currency+GeneralUtil.removeLastZero(nums));
								phData7.setFont(datafont2);
								dataTable.addCell(phData7);
							}
							Paragraph phData8=new Paragraph(preText+totalTxt);
							phData8.setFont(datafont2);
							dataTable.addCell(phData8);
							dataTable.setPadding(10);
				}
				Paragraph phDataTable = new Paragraph();
				phDataTable.add(dataTable);
				Cell cell4=new Cell();
				cell4.add(phDataTable);
				cell4.setColspan(2);
				Table.addCell(cell4);
			}else {
				//暂未拉取到数据
				Paragraph nodataPh=new Paragraph("订单详情暂未获取到！请稍后重试！");
				nodataPh.setFont(datafont1);
				Cell cell4=new Cell(nodataPh);
				cell4.setColspan(2);
				Table.addCell(cell4);
			}
			//底部数据
			if(StrUtil.isEmpty(vatNum) || vattype.equals("normal")) totalVatFee=0f;
			totalPrice=(float)(Math.round(totalPrice*1000))/1000;//保留3位小数
			totalBalance=totalPrice-totalitemDiscount+totalshipPrice+totalVatFee;
			Cell cell5=new Cell();
			Paragraph phfooter1=new Paragraph("Sub Total:   "+preText+currency+GeneralUtil.removeLastZero(totalPrice));
			phfooter1.setFont(contextFont);
			phfooter1.setAlignment(Element.ALIGN_RIGHT);
			Paragraph phfooter2=new Paragraph("Shipping Fee:   "+preText+currency+totalshipPrice);
			phfooter2.setFont(contextFont);
			phfooter2.setAlignment(Element.ALIGN_RIGHT);	
			Paragraph phfooter3=new Paragraph("Promotional Discount:   - "+currency+totalitemDiscount);
			phfooter3.setFont(contextFont);
			phfooter3.setAlignment(Element.ALIGN_RIGHT);
			Paragraph phfooter4=new Paragraph("VAT Total:   "+preText+currency+GeneralUtil.removeLastZero(totalVatFee));
			phfooter4.setFont(contextFont);
			phfooter4.setAlignment(Element.ALIGN_RIGHT);
			Paragraph phfooter5=new Paragraph("Total Amount:   "+preText+currency+df.format(totalBalance));
			phfooter5.setFont(contextFont);
			phfooter5.setAlignment(Element.ALIGN_RIGHT);
			cell5.add(phfooter1);
			if(totalshipPrice>0)cell5.add(phfooter2);
			if(totalitemDiscount>0)cell5.add(phfooter3);
			if(vattype.equals("Vat") && vatNum!=null) {
				cell5.add(phfooter4);
			}
			cell5.add(phfooter5);
			cell5.setColspan(2);
			Table.addCell(cell5);
			Table.setWidth(100);
			document.add(Table);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				serialNumService.readSerialNumber(shopid, "22");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
	
}
