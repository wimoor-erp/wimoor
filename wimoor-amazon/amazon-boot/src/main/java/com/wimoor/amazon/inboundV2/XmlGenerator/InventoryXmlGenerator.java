package com.wimoor.amazon.inboundV2.XmlGenerator;

import com.wimoor.amazon.inboundV2.XmlPojo.InventoryData;
import com.wimoor.amazon.inboundV2.XmlPojo.InventoryItem;
import com.wimoor.amazon.inboundV2.util.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 出口清单 (CEB603) XML生成器
 */
public class InventoryXmlGenerator {



    /**
     * 生成出口清单XML
     */
    public static String generateInventoryXml(InventoryData inventoryData) throws Exception {
        Document doc = XmlHelper.createDocument();

        // 创建根节点
        Element root = doc.createElement("CEB603Message");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns", "http://www.chinaport.gov.cn/ceb");
        root.setAttribute("version", "1.0");
        root.setAttribute("guid", inventoryData.getGuid());
        doc.appendChild(root);

        // 创建Inventory节点
        Element inventory = doc.createElement("Inventory");
        root.appendChild(inventory);

        // 创建InventoryHead
        Element inventoryHead = doc.createElement("InventoryHead");
        inventory.appendChild(inventoryHead);

        // 添加表头字段
        XmlHelper.addElement(doc, inventoryHead, "guid", inventoryData.getGuid());
        XmlHelper.addElement(doc, inventoryHead, "appType", inventoryData.getAppType());
        XmlHelper.addElement(doc, inventoryHead, "appTime", inventoryData.getAppTime());
        XmlHelper.addElement(doc, inventoryHead, "appStatus", inventoryData.getAppStatus());
        XmlHelper.addElement(doc, inventoryHead, "customsCode", inventoryData.getCustomsCode());
        XmlHelper.addElement(doc, inventoryHead, "ebpCode", inventoryData.getEbpCode());
        XmlHelper.addElement(doc, inventoryHead, "ebpName", inventoryData.getEbpName());
        XmlHelper.addElement(doc, inventoryHead, "orderNo", inventoryData.getOrderNo());
        XmlHelper.addElement(doc, inventoryHead, "logisticsCode", inventoryData.getLogisticsCode());
        XmlHelper.addElement(doc, inventoryHead, "logisticsName", inventoryData.getLogisticsName());
        XmlHelper.addElement(doc, inventoryHead, "logisticsNo", inventoryData.getLogisticsNo());
        XmlHelper.addElement(doc, inventoryHead, "copNo", inventoryData.getCopNo());
        XmlHelper.addElement(doc, inventoryHead, "invtNo", inventoryData.getInvtNo());
        XmlHelper.addElement(doc, inventoryHead, "ieFlag", inventoryData.getIeFlag());
        XmlHelper.addElement(doc, inventoryHead, "portCode", inventoryData.getPortCode());
        XmlHelper.addElement(doc, inventoryHead, "ieDate", inventoryData.getIeDate());
        XmlHelper.addElement(doc, inventoryHead, "statisticsFlag", inventoryData.getStatisticsFlag());
        XmlHelper.addElement(doc, inventoryHead, "agentCode", inventoryData.getAgentCode());
        XmlHelper.addElement(doc, inventoryHead, "agentName", inventoryData.getAgentName());
        XmlHelper.addElement(doc, inventoryHead, "ebcCode", inventoryData.getEbcCode());
        XmlHelper.addElement(doc, inventoryHead, "ebcName", inventoryData.getEbcName());
        XmlHelper.addElement(doc, inventoryHead, "ownerCode", inventoryData.getOwnerCode());
        XmlHelper.addElement(doc, inventoryHead, "ownerName", inventoryData.getOwnerName());
        XmlHelper.addElement(doc, inventoryHead, "iacCode", inventoryData.getIacCode());
        XmlHelper.addElement(doc, inventoryHead, "iacName", inventoryData.getIacName());
        XmlHelper.addElement(doc, inventoryHead, "emsNo", inventoryData.getEmsNo());
        XmlHelper.addElement(doc, inventoryHead, "tradeMode", inventoryData.getTradeMode());
        XmlHelper.addElement(doc, inventoryHead, "trafMode", inventoryData.getTrafMode());
        XmlHelper.addElement(doc, inventoryHead, "trafName", inventoryData.getTrafName());
        XmlHelper.addElement(doc, inventoryHead, "voyageNo", inventoryData.getVoyageNo());
        XmlHelper.addElement(doc, inventoryHead, "billNo", inventoryData.getBillNo());
        XmlHelper.addElement(doc, inventoryHead, "totalPackageNo", inventoryData.getTotalPackageNo());
        XmlHelper.addElement(doc, inventoryHead, "loctNo", inventoryData.getLoctNo());
        XmlHelper.addElement(doc, inventoryHead, "licenseNo", inventoryData.getLicenseNo());
        XmlHelper.addElement(doc, inventoryHead, "country", inventoryData.getCountry());
        XmlHelper.addElement(doc, inventoryHead, "POD", inventoryData.getPod());
        XmlHelper.addNumberElement(doc, inventoryHead, "freight", inventoryData.getFreight());
        XmlHelper.addElement(doc, inventoryHead, "fCurrency", inventoryData.getFCurrency());
        XmlHelper.addElement(doc, inventoryHead, "fFlag", inventoryData.getFFlag());
        XmlHelper.addNumberElement(doc, inventoryHead, "insuredFee", inventoryData.getInsuredFee());
        XmlHelper.addElement(doc, inventoryHead, "iCurrency", inventoryData.getICurrency());
        XmlHelper.addElement(doc, inventoryHead, "iFlag", inventoryData.getIFlag());
        XmlHelper.addElement(doc, inventoryHead, "wrapType", inventoryData.getWrapType());
        XmlHelper.addNumberElement(doc, inventoryHead, "packNo", inventoryData.getPackNo());
        XmlHelper.addNumberElement(doc, inventoryHead, "grossWeight", inventoryData.getGrossWeight());
        XmlHelper.addNumberElement(doc, inventoryHead, "netWeight", inventoryData.getNetWeight());
        XmlHelper.addElement(doc, inventoryHead, "note", inventoryData.getNote());

        // 创建商品列表
        if (inventoryData.getInventoryItems() != null && !inventoryData.getInventoryItems().isEmpty()) {
            for (InventoryItem item : inventoryData.getInventoryItems()) {
                Element inventoryList = doc.createElement("InventoryList");
                inventory.appendChild(inventoryList);

                XmlHelper.addNumberElement(doc, inventoryList, "gnum", item.getGnum());
                XmlHelper.addElement(doc, inventoryList, "itemNo", item.getItemNo());
                XmlHelper.addElement(doc, inventoryList, "itemRecordNo", item.getItemRecordNo());
                XmlHelper.addElement(doc, inventoryList, "itemName", item.getItemName());
                XmlHelper.addElement(doc, inventoryList, "gcode", item.getGcode());
                XmlHelper.addElement(doc, inventoryList, "gname", item.getGname());
                XmlHelper.addElement(doc, inventoryList, "gmodel", item.getGmodel());
                XmlHelper.addElement(doc, inventoryList, "barCode", item.getBarCode());
                XmlHelper.addElement(doc, inventoryList, "country", item.getCountry());
                XmlHelper.addElement(doc, inventoryList, "currency", item.getCurrency());
                XmlHelper.addNumberElement(doc, inventoryList, "qty", item.getQty());
                XmlHelper.addNumberElement(doc, inventoryList, "qty1", item.getQty1());
                XmlHelper.addNumberElement(doc, inventoryList, "qty2", item.getQty2());
                XmlHelper.addElement(doc, inventoryList, "unit", item.getUnit());
                XmlHelper.addElement(doc, inventoryList, "unit1", item.getUnit1());
                XmlHelper.addElement(doc, inventoryList, "unit2", item.getUnit2());
                XmlHelper.addNumberElement(doc, inventoryList, "price", item.getPrice());
                XmlHelper.addNumberElement(doc, inventoryList, "totalPrice", item.getTotalPrice());
                XmlHelper.addElement(doc, inventoryList, "note", item.getNote());
            }
        }

        // 添加BaseTransfer节点
        Element baseTransfer = XmlHelper.createBaseTransfer(
                doc, inventoryData.getCopCode(), inventoryData.getCopName(),
                inventoryData.getDxpId(), inventoryData.getBaseNote()
        );
        root.appendChild(baseTransfer);

        return XmlHelper.documentToString(doc);
    }
}