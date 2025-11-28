package com.wimoor.amazon.feed.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;

public class FeedFileProductPriceXML {
	public static final String type="POST_PRODUCT_PRICING_DATA";
	// 生成productprice的xml文件
		public static ByteArrayOutputStream getFile(Map<String, Object> map, String ftype) throws TransformerException, ParserConfigurationException {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			ByteArrayOutputStream bos = null;
				AmazonAuthority auth = null;
				String sku = map.get("sku").toString();
				String currency = map.get("currency").toString();
				String standardPrice = map.get("standardPrice").toString();
				if (map.get("auth") != null) {
					auth = (AmazonAuthority) map.get("auth");
				}
				if (auth == null) {
					return null;
				}
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.newDocument();
				document.setXmlStandalone(true);
				Element AmazonEnvelope = document.createElement("AmazonEnvelope");
				AmazonEnvelope.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
				AmazonEnvelope.setAttribute("xsi:noNamespaceSchemaLocation", "amznenvelope.xsd");
				document.appendChild(AmazonEnvelope);
				Element Header = document.createElement("Header");
				Element DocumentVersion = document.createElement("DocumentVersion");
				DocumentVersion.setTextContent("1.01");
				Element MerchantIdentifier = document.createElement("MerchantIdentifier");
				MerchantIdentifier.setTextContent(auth.getSellerid());
				Header.appendChild(DocumentVersion);
				Header.appendChild(MerchantIdentifier);
				AmazonEnvelope.appendChild(Header);
				Element MessageType = document.createElement("MessageType");
				MessageType.setTextContent("Price");
				AmazonEnvelope.appendChild(MessageType);
				Element Message = document.createElement("Message");
				AmazonEnvelope.appendChild(Message);
				Element MessageID = document.createElement("MessageID");
				MessageID.setTextContent("1");
				Message.appendChild(MessageID);
				Element Price = document.createElement("Price");
				Element SKU = document.createElement("SKU");
				SKU.setTextContent(sku);
				Price.appendChild(SKU);
				Element StandardPrice = document.createElement("StandardPrice");
				StandardPrice.setAttribute("currency", currency);
				StandardPrice.setTextContent(standardPrice);
				Price.appendChild(StandardPrice);
			   if("isBusiness".equals(ftype)){
					String businessprice =map.get("businessprice")==null?null:map.get("businessprice").toString();
					Element BusinessPrice = document.createElement("BusinessPrice");
					BusinessPrice.setTextContent(businessprice);
					Price.appendChild(BusinessPrice);
					if(map.get("submittype")!=null&&"deletebusiness".equals(map.get("submittype").toString())) {
						Element PricingAction = document.createElement("PricingAction");
						PricingAction.setTextContent("delete business_price");
						Price.appendChild(PricingAction);
					}else {
						String businesstype=map.get("businesstype")==null?null:map.get("businesstype").toString();
						List<Map<String, Object>> businessLst = null;
						if(map.get("businesslist")!=null) {
						    businessLst=GeneralUtil.jsonStringToMapList( map.get("businesslist").toString());
						    if(businessLst.size()>0&&(businessLst.size()==1&&!StrUtil.isEmpty(businessLst.get(0).get("price").toString()))) {
						    	Set<String> quantityset=new HashSet<String>();
								Element QuantityPriceType = document.createElement("QuantityPriceType");
								QuantityPriceType.setTextContent(businesstype);
								Price.appendChild(QuantityPriceType);
									Element QuantityPrice = document.createElement("QuantityPrice");
									for(int i =0;i<businessLst.size();i++) {
										Element QuantityPriceitem = document.createElement("QuantityPrice"+(i+1));
										QuantityPriceitem.setTextContent(businessLst.get(i).get("price").toString());
										Element QuantityLowerBound = document.createElement("QuantityLowerBound"+(i+1));
										String quantity=businessLst.get(i).get("quantity").toString();
										QuantityLowerBound.setTextContent(quantity);
										if(quantityset.contains(quantity)) {
											throw new BizException("Business Price 数量重复");
										}else {
											quantityset.add(quantity);
										}
										QuantityPrice.appendChild(QuantityPriceitem);
										QuantityPrice.appendChild(QuantityLowerBound);
									}
								Price.appendChild(QuantityPrice);
						    }
						}
					}

				}
	          if("isFortime".equals(ftype)) {
					String startDate = map.get("startDate").toString();
					String endtDate = map.get("endDate").toString();
					if (StrUtil.isNotEmpty(startDate)) {
						startDate += "T00:00:00Z";
					} else {
						startDate = "";
					}
					if (StrUtil.isNotEmpty(endtDate)) {
						endtDate += "T23:59:59Z";
					} else {
						endtDate = "";
					}
					String salesPrice = map.get("salesPrice").toString();
					StandardPrice.setAttribute("currency", currency);
					StandardPrice.setTextContent(standardPrice);
					Price.appendChild(StandardPrice);
					Element Sale = document.createElement("Sale");
					Element StartDate = document.createElement("StartDate");
					StartDate.setTextContent(startDate);
					Sale.appendChild(StartDate);
					Element EndDate = document.createElement("EndDate");
					EndDate.setTextContent(endtDate);
					Sale.appendChild(EndDate);
					Element SalePrice = document.createElement("SalePrice");
					SalePrice.setAttribute("currency", currency);
					SalePrice.setTextContent(salesPrice);
					Sale.appendChild(SalePrice);
					Price.appendChild(Sale);
				}
	      	    Message.appendChild(Price);
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
				DOMSource domSource = new DOMSource(document);
				// xml transform String
				bos = new ByteArrayOutputStream();
				transformer.transform(domSource, new StreamResult(bos));
				return bos;
		}
}
