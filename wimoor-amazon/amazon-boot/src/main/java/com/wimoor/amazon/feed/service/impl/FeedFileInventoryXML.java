package com.wimoor.amazon.feed.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.Map;
 

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
 

public class FeedFileInventoryXML {
	public static final String type="POST_INVENTORY_AVAILABILITY_DATA";
	// 生成productprice的xml文件
		public static ByteArrayOutputStream getFile(AmazonAuthority auth,Map<String, Object> map) throws TransformerException, ParserConfigurationException {
			    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    ByteArrayOutputStream bos = null;
				String sku = map.get("sku").toString();
				String qty = map.get("qty").toString();
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
				MessageType.setTextContent("Inventory");
				AmazonEnvelope.appendChild(MessageType);
				Element Message = document.createElement("Message");
				AmazonEnvelope.appendChild(Message);
				Element MessageID = document.createElement("MessageID");
				MessageID.setTextContent("1");
				Message.appendChild(MessageID);
				
				Element OperationType = document.createElement("OperationType");
				OperationType.setTextContent("Update");
				Message.appendChild(OperationType);
				
				Element Inventory = document.createElement("Inventory");
				Element SKU = document.createElement("SKU");
				SKU.setTextContent(sku);
				Inventory.appendChild(SKU);
				
				Element FulfillmentCenterID = document.createElement("FulfillmentCenterID");
				FulfillmentCenterID.setTextContent("DEFAULT");
				Inventory.appendChild(FulfillmentCenterID);
				
				Element Quantity = document.createElement("Quantity");
				Quantity.setTextContent(qty);
				Inventory.appendChild(Quantity);
				
				//Element SwitchFulfillmentTo = document.createElement("SwitchFulfillmentTo");
				//SwitchFulfillmentTo.setTextContent("MFN");
				//Inventory.appendChild(SwitchFulfillmentTo);
				
				
	      	    Message.appendChild(Inventory);
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
