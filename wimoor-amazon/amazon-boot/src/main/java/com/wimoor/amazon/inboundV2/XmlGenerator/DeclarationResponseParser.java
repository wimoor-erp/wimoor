package com.wimoor.amazon.inboundV2.XmlGenerator;

import com.wimoor.amazon.inboundV2.XmlPojo.DeclarationResult;
import com.wimoor.amazon.inboundV2.XmlPojo.ImportResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;

/**
 * 报关单回执解析器
 */
public class DeclarationResponseParser {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmssmm");

    /**
     * 解析报关单回执
     */
    public static DeclarationResult parseDeclarationResponse(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

        DeclarationResult result = new DeclarationResult();

        // 查找DEC_RESULT节点
        NodeList decResultList = doc.getElementsByTagName("DEC_RESULT");
        if (decResultList.getLength() > 0) {
            Element decResult = (Element) decResultList.item(0);

            result.setSeqNo(getElementText(decResult, "SEQ_NO"));
            result.setEntryId(getElementText(decResult, "ENTRY_ID"));

            // 解析日期
            String noticeDateStr = getElementText(decResult, "NOTICE_DATE");
            if (noticeDateStr != null && !noticeDateStr.isEmpty()) {
                try {
                    result.setNoticeDate(DATE_FORMAT.parse(noticeDateStr));
                } catch (Exception e) {
                    // 日期解析失败，继续处理其他字段
                }
            }

            result.setChannel(getElementText(decResult, "CHANNEL"));
            result.setNote(getElementText(decResult, "NOTE"));
            result.setDeclPort(getElementText(decResult, "DECL_PORT"));
            result.setAgentName(getElementText(decResult, "AGENT_NAME"));
            result.setDeclareNo(getElementText(decResult, "DECLARE_NO"));
            result.setTradeCo(getElementText(decResult, "TRADE_CO"));
            result.setCustomsField(getElementText(decResult, "CUSTOMS_FIELD"));
            result.setBondedNo(getElementText(decResult, "BONDED_NO"));

            // 解析其他日期
            String ieDateStr = getElementText(decResult, "I_E_DATE");
            if (ieDateStr != null && !ieDateStr.isEmpty()) {
                try {
                    result.setIEDate(DATE_FORMAT.parse(ieDateStr));
                } catch (Exception e) {
                    // 忽略解析错误
                }
            }

            // 解析数值
            String packNoStr = getElementText(decResult, "PACK_NO");
            if (packNoStr != null && !packNoStr.isEmpty()) {
                try {
                    result.setPackNo(Integer.parseInt(packNoStr));
                } catch (NumberFormatException e) {
                    // 忽略解析错误
                }
            }

            result.setBillNo(getElementText(decResult, "BILL_NO"));
            result.setTrafMode(getElementText(decResult, "TRAF_MODE"));
            result.setVoyageNo(getElementText(decResult, "VOYAGE_NO"));

            // 解析重量
            String netWtStr = getElementText(decResult, "NET_WT");
            if (netWtStr != null && !netWtStr.isEmpty()) {
                try {
                    result.setNetWt(Double.parseDouble(netWtStr));
                } catch (NumberFormatException e) {
                    // 忽略解析错误
                }
            }

            String grossWtStr = getElementText(decResult, "GROSS_WT");
            if (grossWtStr != null && !grossWtStr.isEmpty()) {
                try {
                    result.setGrossWt(Double.parseDouble(grossWtStr));
                } catch (NumberFormatException e) {
                    // 忽略解析错误
                }
            }

            String dDateStr = getElementText(decResult, "D_DATE");
            if (dDateStr != null && !dDateStr.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    result.setDDate(dateFormat.parse(dDateStr));
                } catch (Exception e) {
                    // 忽略解析错误
                }
            }
        }

        // 解析处理结果文字信息
        NodeList resultInfoList = doc.getElementsByTagName("RESULT_INFO");
        if (resultInfoList.getLength() > 0) {
            Element resultInfo = (Element) resultInfoList.item(0);
            result.setResultInfo(resultInfo.getTextContent());
        }

        return result;
    }

    /**
     * 解析导入系统响应
     */
    public static ImportResponse parseImportResponse(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

        ImportResponse response = new ImportResponse();
        Element root = doc.getDocumentElement();

        response.setResponseCode(getElementText(root, "ResponseCode"));
        response.setErrorMessage(getElementText(root, "ErrorMessage"));
        response.setClientSeqNo(getElementText(root, "ClientSeqNo"));
        response.setSeqNo(getElementText(root, "SeqNo"));
        response.setTrnPreId(getElementText(root, "TrnPreId"));

        return response;
    }


    private static String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Element element = (Element) nodeList.item(0);
            String text = element.getTextContent();
            return text != null ? text.trim() : null;
        }
        return null;
    }
}