package com.wimoor.amazon.inboundV2.util;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 跨境电商XML文档生成助手类
 */
public class XmlHelper {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat TIMESTAMP_MS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 生成GUID（36位大写UUID）
     */
    public static String generateGuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 获取当前时间（YYYYMMDDHHmmss格式）
     */
    public static String getCurrentAppTime() {
        return TIMESTAMP_FORMAT.format(new Date());
    }

    /**
     * 获取当前时间（YYYYMMDDHHmmssSSS格式）
     */
    public static String getCurrentTimeWithMs() {
        return TIMESTAMP_MS_FORMAT.format(new Date());
    }

    /**
     * 创建XML文档
     */
    public static Document createDocument() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    /**
     * 创建元素并设置文本内容
     */
    public static Element createElement(Document doc, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        if (textContent != null) {
            element.setTextContent(textContent);
        }
        return element;
    }

    /**
     * 添加子元素
     */
    public static void addElement(Document doc, Element parent, String tagName, String textContent) {
        if (textContent != null) {
            Element child = createElement(doc, tagName, textContent);
            parent.appendChild(child);
        }
    }

    /**
     * 添加子元素（数值类型）
     */
    public static void addNumberElement(Document doc, Element parent, String tagName, Number number) {
        if (number != null) {
            addElement(doc, parent, tagName, number.toString());
        }
    }

    /**
     * 格式化XML输出
     */
    public static String documentToString(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        // 添加XML声明
        String xmlString = writer.toString();
        if (!xmlString.startsWith("<?xml")) {
            xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlString;
        }
        return xmlString;
    }

    /**
     * 生成报文文件名
     * @param messageType 报文类型，如：CEB303, CEB603
     * @param senderId 发送方ID（企业代码）
     * @param sequence 流水号（4位）
     * @return 完整的文件名
     */
    public static String generateFileNameExpid(String messageType, String senderId, String sequence) {
        String timestamp = TIMESTAMP_MS_FORMAT.format(new Date());
        return String.format("CEB_%s_%s_EPORT_%s_%s.xml",
                messageType, senderId, timestamp, sequence);
    }

    /**
     * 生成报文文件名
     * @param messageType 报文类型，如：CEB303, CEB603
     * @param senderId 发送方ID（企业代码）
     * @param sequence 流水号（4位）
     * @return 完整的文件名
     */
    public static String generateFileNameEdiCode(String messageType, String senderId, String sequence) {
        String timestamp = TIMESTAMP_FORMAT.format(new Date());
        return String.format("SZCPORT%sMessage_%s_E002_%s%s.xml",
                messageType, senderId, timestamp, sequence);
    }

    /**
     * 创建BaseTransfer节点
     */
    public static Element createBaseTransfer(Document doc, String copCode, String copName,
                                             String dxpId, String note) {
        Element baseTransfer = doc.createElement("BaseTransfer");

        addElement(doc, baseTransfer, "copCode", copCode);
        addElement(doc, baseTransfer, "copName", copName);
        addElement(doc, baseTransfer, "dxpMode", "DXP");
        addElement(doc, baseTransfer, "dxpId", dxpId);
        addElement(doc, baseTransfer, "note", note);

        return baseTransfer;
    }
}