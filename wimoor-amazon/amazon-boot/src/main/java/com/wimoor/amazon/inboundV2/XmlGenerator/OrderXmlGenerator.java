package com.wimoor.amazon.inboundV2.XmlGenerator;


import com.wimoor.amazon.inboundV2.XmlPojo.OrderData;
import com.wimoor.amazon.inboundV2.XmlPojo.OrderItem;
import com.wimoor.amazon.inboundV2.util.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 电子订单 (CEB303) XML生成器
 */
public class OrderXmlGenerator {



    /**
     * 生成电子订单XML
     */
    public static String generateOrderXml(OrderData orderData) throws Exception {
        Document doc = XmlHelper.createDocument();

        // 创建根节点
        Element root = doc.createElement("CEB303Message");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns", "http://www.chinaport.gov.cn/ceb");
        root.setAttribute("version", "1.0");
        root.setAttribute("guid", orderData.getGuid());
        doc.appendChild(root);

        // 创建Order节点
        Element order = doc.createElement("Order");
        root.appendChild(order);

        // 创建OrderHead
        Element orderHead = doc.createElement("OrderHead");
        order.appendChild(orderHead);

        // 添加表头字段
        XmlHelper.addElement(doc, orderHead, "guid", orderData.getGuid());
        XmlHelper.addElement(doc, orderHead, "appType", orderData.getAppType());
        XmlHelper.addElement(doc, orderHead, "appTime", orderData.getAppTime());
        XmlHelper.addElement(doc, orderHead, "appStatus", orderData.getAppStatus());
        XmlHelper.addElement(doc, orderHead, "orderType", orderData.getOrderType());
        XmlHelper.addElement(doc, orderHead, "orderNo", orderData.getOrderNo());
        XmlHelper.addElement(doc, orderHead, "ebpCode", orderData.getEbpCode());
        XmlHelper.addElement(doc, orderHead, "ebpName", orderData.getEbpName());
        XmlHelper.addElement(doc, orderHead, "ebcCode", orderData.getEbcCode());
        XmlHelper.addElement(doc, orderHead, "ebcName", orderData.getEbcName());
        XmlHelper.addNumberElement(doc, orderHead, "goodsValue", orderData.getGoodsValue());
        XmlHelper.addNumberElement(doc, orderHead, "freight", orderData.getFreight());
        XmlHelper.addElement(doc, orderHead, "currency", orderData.getCurrency());
        XmlHelper.addElement(doc, orderHead, "note", orderData.getNote());

        // 创建商品列表
        if (orderData.getOrderItems() != null && !orderData.getOrderItems().isEmpty()) {
            for (OrderItem item : orderData.getOrderItems()) {
                Element orderList = doc.createElement("OrderList");
                order.appendChild(orderList);

                XmlHelper.addNumberElement(doc, orderList, "gnum", item.getGnum());
                XmlHelper.addElement(doc, orderList, "itemNo", item.getItemNo());
                XmlHelper.addElement(doc, orderList, "itemName", item.getItemName());
                XmlHelper.addElement(doc, orderList, "itemDescribe", item.getItemDescribe());
                XmlHelper.addElement(doc, orderList, "barCode", item.getBarCode());
                XmlHelper.addElement(doc, orderList, "unit", item.getUnit());
                XmlHelper.addElement(doc, orderList, "currency", item.getCurrency());
                XmlHelper.addNumberElement(doc, orderList, "qty", item.getQty());
                XmlHelper.addNumberElement(doc, orderList, "price", item.getPrice());
                XmlHelper.addNumberElement(doc, orderList, "totalPrice", item.getTotalPrice());
                XmlHelper.addElement(doc, orderList, "note", item.getNote());
            }
        }

        // 添加BaseTransfer节点
        Element baseTransfer = XmlHelper.createBaseTransfer(
                doc, orderData.getCopCode(), orderData.getCopName(),
                orderData.getDxpId(), orderData.getBaseNote()
        );
        root.appendChild(baseTransfer);

        return XmlHelper.documentToString(doc);
    }
}