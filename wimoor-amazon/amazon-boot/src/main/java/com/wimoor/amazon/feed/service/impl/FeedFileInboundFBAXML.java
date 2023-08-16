package com.wimoor.amazon.feed.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.wimoor.amazon.inbound.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;

public class FeedFileInboundFBAXML {
    public static final String type="POST_FBA_INBOUND_CARTON_CONTENTS";
	/**不同提交方式获取箱标的方式不同
	 * POST_FLAT_FILE_FROM_EXCEL_FBA_CREATE_CARTON_INFO  ：SELLER_LABEL
       POST_FBA_INBOUND_CARTON_CONTENTS：UNIQUE
	 * @param shipment
	 * @param sellerid
	 * @return
	 */
	public static ByteArrayOutputStream getFile(ShipInboundShipment shipment, String sellerid,List<ShipInboundCase> boxcase) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			ByteArrayOutputStream bos = null;
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				org.w3c.dom.Document document = builder.newDocument();
				document.setXmlStandalone(true);

				org.w3c.dom.Element AmazonEnvelope = document.createElement("AmazonEnvelope");
				AmazonEnvelope.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
				AmazonEnvelope.setAttribute("xsi:noNamespaceSchemaLocation", "CartonContentsRequest.xsd");
				document.appendChild(AmazonEnvelope);

				org.w3c.dom.Element Header = document.createElement("Header");
				org.w3c.dom.Element DocumentVersion = document.createElement("DocumentVersion");
				DocumentVersion.setTextContent("1.01");
				org.w3c.dom.Element MerchantIdentifier = document.createElement("MerchantIdentifier");
				MerchantIdentifier.setTextContent(sellerid);
				Header.appendChild(DocumentVersion);
				Header.appendChild(MerchantIdentifier);
				AmazonEnvelope.appendChild(Header);

				org.w3c.dom.Element MessageType = document.createElement("MessageType");
				MessageType.setTextContent("CartonContentsRequest");
				AmazonEnvelope.appendChild(MessageType);

				org.w3c.dom.Element Message = document.createElement("Message");
				AmazonEnvelope.appendChild(Message);

				org.w3c.dom.Element MessageID = document.createElement("MessageID");
				MessageID.setTextContent("1");
				Message.appendChild(MessageID);
				org.w3c.dom.Element CartonContents = document.createElement("CartonContentsRequest");
				Message.appendChild(CartonContents);

				org.w3c.dom.Element ShipmentId = document.createElement("ShipmentId");
				ShipmentId.setTextContent(shipment.getShipmentid());
				CartonContents.appendChild(ShipmentId);

				org.w3c.dom.Element NumCartons = document.createElement("NumCartons");
				NumCartons.setTextContent(shipment.getBoxnum().toString());
				CartonContents.appendChild(NumCartons);

				for (int i = 0; i < shipment.getBoxnum(); i++) {
					org.w3c.dom.Element Carton = document.createElement("Carton");
					CartonContents.appendChild(Carton);
					org.w3c.dom.Element id = document.createElement("CartonId");
					id.setTextContent((i + 1) + "");
					Carton.appendChild(id);
					for (int j = 0; j < boxcase.size(); j++) {
						ShipInboundCase caseinfo = boxcase.get(j);
						if (caseinfo.getNumberofcase() == (i + 1) && caseinfo.getUnitspercase() > 0) {
							org.w3c.dom.Element Item = document.createElement("Item");
							Carton.appendChild(Item);
							org.w3c.dom.Element sku = document.createElement("SKU");
							sku.setTextContent(caseinfo.getMerchantsku());
							Item.appendChild(sku);
							org.w3c.dom.Element qty = document.createElement("QuantityShipped");
							qty.setTextContent(caseinfo.getQuantity().toString());
							Item.appendChild(qty);
							org.w3c.dom.Element qin = document.createElement("QuantityInCase");
							qin.setTextContent(caseinfo.getUnitspercase().toString());
							Item.appendChild(qin);
						}
					}
				}
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
				DOMSource domSource = new DOMSource(document);
				bos = new ByteArrayOutputStream();
				transformer.transform(domSource, new StreamResult(bos));
				return bos;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
}
