package com.wimoor.amazon.inboundV2.XmlGenerator;


import com.wimoor.amazon.inboundV2.XmlPojo.ReceiptData;
import com.wimoor.amazon.inboundV2.util.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 收款单 (CEB403) XML生成器
 */
public class ReceiptXmlGenerator {



    /**
     * 生成收款单XML
     */
    public static String generateReceiptXml(ReceiptData receiptData) throws Exception {
        Document doc = XmlHelper.createDocument();

        // 创建根节点
        Element root = doc.createElement("CEB403Message");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns", "http://www.chinaport.gov.cn/ceb");
        root.setAttribute("version", "1.0");
        root.setAttribute("guid", receiptData.getGuid());
        doc.appendChild(root);

        // 创建Receipts节点
        Element receipts = doc.createElement("Receipts");
        root.appendChild(receipts);

        // 添加字段
        XmlHelper.addElement(doc, receipts, "guid", receiptData.getGuid());
        XmlHelper.addElement(doc, receipts, "appType", receiptData.getAppType());
        XmlHelper.addElement(doc, receipts, "appTime", receiptData.getAppTime());
        XmlHelper.addElement(doc, receipts, "appStatus", receiptData.getAppStatus());
        XmlHelper.addElement(doc, receipts, "ebpCode", receiptData.getEbpCode());
        XmlHelper.addElement(doc, receipts, "ebpName", receiptData.getEbpName());
        XmlHelper.addElement(doc, receipts, "ebcCode", receiptData.getEbcCode());
        XmlHelper.addElement(doc, receipts, "ebcName", receiptData.getEbcName());
        XmlHelper.addElement(doc, receipts, "orderNo", receiptData.getOrderNo());
        XmlHelper.addElement(doc, receipts, "payCode", receiptData.getPayCode());
        XmlHelper.addElement(doc, receipts, "payName", receiptData.getPayName());
        XmlHelper.addElement(doc, receipts, "payNo", receiptData.getPayNo());
        XmlHelper.addNumberElement(doc, receipts, "charge", receiptData.getCharge());
        XmlHelper.addElement(doc, receipts, "currency", receiptData.getCurrency());
        XmlHelper.addElement(doc, receipts, "accountingDate", receiptData.getAccountingDate());
        XmlHelper.addElement(doc, receipts, "note", receiptData.getNote());

        // 添加BaseTransfer节点
        Element baseTransfer = XmlHelper.createBaseTransfer(
                doc, receiptData.getCopCode(), receiptData.getCopName(),
                receiptData.getDxpId(), receiptData.getBaseNote()
        );
        root.appendChild(baseTransfer);

        return XmlHelper.documentToString(doc);
    }
}