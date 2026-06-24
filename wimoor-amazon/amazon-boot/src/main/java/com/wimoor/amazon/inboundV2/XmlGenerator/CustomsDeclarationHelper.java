package com.wimoor.amazon.inboundV2.XmlGenerator;

import com.wimoor.amazon.inboundV2.XmlPojo.*;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报关单XML生成助手类（V4.0版本）
 */
public class CustomsDeclarationHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSS");

    private Document doc;
    private Element root;

    /**
     * 创建报关单XML文档
     */
    public CustomsDeclarationHelper() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.newDocument();

        // 创建根节点
        root = doc.createElement("DecMessage");
        root.setAttribute("xmlns", "http://www.chinaport.gov.cn/dec");
        doc.appendChild(root);
    }

    /**
     * 创建元素并设置文本内容
     */
    private Element createElement(String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        if (textContent != null) {
            element.setTextContent(textContent);
        }
        return element;
    }

    /**
     * 添加子元素
     */
    private void addElement(Element parent, String tagName, String textContent) {
        if (textContent != null) {
            Element child = createElement(tagName, textContent);
            parent.appendChild(child);
        }
    }

    /**
     * 添加数值类型子元素
     */
    private void addNumberElement(Element parent, String tagName, Number number) {
        if (number != null) {
            addElement(parent, tagName, number.toString());
        }
    }

    /**
     * 格式化XML输出
     */
    public String toXmlString() throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        String xmlString = writer.toString();
        if (!xmlString.startsWith("<?xml")) {
            xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlString;
        }
        return xmlString;
    }

    /**
     * 生成GUID
     */
    public static String generateGuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 生成当前时间戳（yyyyMMddHHmmssmm）
     */
    public static String getCurrentTimestamp() {
        return TIMESTAMP_FORMAT.format(new Date());
    }

    // ====================== 报关单表头 ======================


    /**
     * 设置报关单表头
     */
    public void setDeclarationHeader(DeclarationHeader header) {
        Element decHead = doc.createElement("DecHead");
        root.appendChild(decHead);

        // 基本信息
        addElement(decHead, "SeqNo", header.getSeqNo());
        addElement(decHead, "IEFlag", header.getIeFlag());
        addElement(decHead, "Type", header.getType());
        addElement(decHead, "AgentCode", header.getAgentCode());
        addElement(decHead, "AgentName", header.getAgentName());
        addElement(decHead, "ApprNo", header.getApprNo());
        addElement(decHead, "BillNo", header.getBillNo());
        addElement(decHead, "ContrNo", header.getContrNo());
        addElement(decHead, "CustomMaster", header.getCustomMaster());
        addElement(decHead, "CutMode", header.getCutMode());

        // 企业信息
        addElement(decHead, "CopCode", header.getCopCode());
        addElement(decHead, "CopName", header.getCopName());
        addElement(decHead, "DistinatePort", header.getDistinatePort());

        // 费用信息
        addElement(decHead, "FeeCurr", header.getFeeCurr());
        addElement(decHead, "FeeMark", header.getFeeMark());
        addNumberElement(decHead, "FeeRate", header.getFeeRate());
        addNumberElement(decHead, "GrossWet", header.getGrossWet());

        // 日期
        addElement(decHead, "IEDate", header.getIeDate());
        addElement(decHead, "IEPort", header.getIePort());

        // 保险费
        addElement(decHead, "InsurCurr", header.getInsurCurr());
        addElement(decHead, "InsurMark", header.getInsurMark());
        addNumberElement(decHead, "InsurRate", header.getInsurRate());

        // 许可和备案
        addElement(decHead, "LicenseNo", header.getLicenseNo());
        addElement(decHead, "ManualNo", header.getManualNo());

        // 货物信息
        addNumberElement(decHead, "NetWt", header.getNetWt());
        addElement(decHead, "NoteS", header.getNoteS());

        // 杂费
        addElement(decHead, "OtherCurr", header.getOtherCurr());
        addElement(decHead, "OtherMark", header.getOtherMark());
        addNumberElement(decHead, "OtherRate", header.getOtherRate());

        // 收发货人
        addElement(decHead, "OwnerCode", header.getOwnerCode());
        addElement(decHead, "OwnerName", header.getOwnerName());
        addNumberElement(decHead, "PackNo", header.getPackNo());

        // 贸易信息
        addElement(decHead, "TradeCountry", header.getTradeCountry());
        addElement(decHead, "TradeMode", header.getTradeMode());
        addElement(decHead, "TrafMode", header.getTrafMode());
        addElement(decHead, "TrafName", header.getTrafName());
        addElement(decHead, "TransMode", header.getTransMode());
        addElement(decHead, "WrapType", header.getWrapType());

        // 海关编号
        addElement(decHead, "EntryId", header.getEntryId());
        addElement(decHead, "PreEntryId", header.getPreEntryId());
        addElement(decHead, "EdiId", header.getEdiId());
        addElement(decHead, "Risk", header.getRisk());
        addElement(decHead, "EntryType", header.getEntryType());
        addElement(decHead, "PDate", header.getPDate());
        addElement(decHead, "TypistNo", header.getTypistNo());
        addElement(decHead, "InputerName", header.getInputerName());
        addElement(decHead, "PartenerID", header.getPartenerId());

        // 跨境电商新增字段
        addElement(decHead, "TradeAreaCode", header.getTradeAreaCode());
        addElement(decHead, "MarkNo", header.getMarkNo());
        addElement(decHead, "DespPortCode", header.getDespPortCode());
        addElement(decHead, "EntyPortCode", header.getEntyPortCode());
        addElement(decHead, "GoodsPlace", header.getGoodsPlace());
        addElement(decHead, "BLNo", header.getBlNo());
        addElement(decHead, "DeclareName", header.getDeclareName());
        addElement(decHead, "NoOtherPack", header.getNoOtherPack());
        addElement(decHead, "OrgCode", header.getOrgCode());

        // 境外企业信息
        addElement(decHead, "OverseasConsignorCode", header.getOverseasConsignorCode());
        addElement(decHead, "OverseasConsignorCname", header.getOverseasConsignorCname());
        addElement(decHead, "OverseasConsignorEname", header.getOverseasConsignorEname());
        addElement(decHead, "OverseasConsignorAddr", header.getOverseasConsignorAddr());
        addElement(decHead, "OverseasConsigneeCode", header.getOverseasConsigneeCode());
        addElement(decHead, "OverseasConsigneeEname", header.getOverseasConsigneeEname());
        addElement(decHead, "DomesticConsigneeEname", header.getDomesticConsigneeEname());

        // 检验检疫编码
        addElement(decHead, "TradeCiqCode", header.getTradeCiqCode());
        addElement(decHead, "OwnerCiqCode", header.getOwnerCiqCode());
        addElement(decHead, "DeclCiqCode", header.getDeclCiqCode());
    }

    // ====================== 报关单表体 ======================


    /**
     * 设置报关单表体
     */
    public void setDeclarationItems(List<DeclarationItem> items) {
        if (items == null || items.isEmpty()) return;

        Element decLists = doc.createElement("DecLists");
        root.appendChild(decLists);

        for (DeclarationItem item : items) {
            Element decList = doc.createElement("DecList");
            decLists.appendChild(decList);

            // 基本信息
            addElement(decList, "ClassMark", item.getClassMark());
            addElement(decList, "CodeTS", item.getCodeTS());
            addElement(decList, "ContrItem", item.getContrItem());
            addNumberElement(decList, "DeclPrice", item.getDeclPrice());
            addElement(decList, "DutyMode", item.getDutyMode());
            addNumberElement(decList, "Factor", item.getFactor());
            addElement(decList, "GModel", item.getGModel());
            addElement(decList, "GName", item.getGName());
            addNumberElement(decList, "GNo", item.getGNo());
            addElement(decList, "OriginCountry", item.getOriginCountry());
            addElement(decList, "TradeCurr", item.getTradeCurr());
            addNumberElement(decList, "DeclTotal", item.getDeclTotal());
            addNumberElement(decList, "GQty", item.getGQty());
            addNumberElement(decList, "FirstQty", item.getFirstQty());
            addNumberElement(decList, "SecondQty", item.getSecondQty());
            addElement(decList, "GUnit", item.getGUnit());
            addElement(decList, "FirstUnit", item.getFirstUnit());
            addElement(decList, "SecondUnit", item.getSecondUnit());
            addElement(decList, "UseTo", item.getUseTo());
            addNumberElement(decList, "WorkUsd", item.getWorkUsd());
            addElement(decList, "ExgNo", item.getExgNo());
            addNumberElement(decList, "ExgVersion", item.getExgVersion());
            addElement(decList, "DestinationCountry", item.getDestinationCountry());

            // 跨境电商新增字段
            addElement(decList, "CiqCode", item.getCiqCode());
            addElement(decList, "DeclGoodsEname", item.getDeclGoodsEname());
            addElement(decList, "OrigPlaceCode", item.getOrigPlaceCode());
            addElement(decList, "Purpose", item.getPurpose());
            addElement(decList, "ProdValidDt", item.getProdValidDt());
            addElement(decList, "ProdQgp", item.getProdQgp());
            addElement(decList, "GoodsAttr", item.getGoodsAttr());
            addElement(decList, "Stuff", item.getStuff());
            addElement(decList, "Uncode", item.getUncode());
            addElement(decList, "DangName", item.getDangName());
            addElement(decList, "DangPackType", item.getDangPackType());
            addElement(decList, "DangPackSpec", item.getDangPackSpec());
            addElement(decList, "EngManEntCnm", item.getEngManEntCnm());
            addElement(decList, "NoDangFlag", item.getNoDangFlag());
            addElement(decList, "DestCode", item.getDestCode());
            addElement(decList, "GoodsSpec", item.getGoodsSpec());
            addElement(decList, "GoodsModel", item.getGoodsModel());
            addElement(decList, "GoodsBrand", item.getGoodsBrand());
            addElement(decList, "ProduceDate", item.getProduceDate());
            addElement(decList, "ProdBatchNo", item.getProdBatchNo());
            addElement(decList, "DistrictCode", item.getDistrictCode());

            // 许可证信息
            if (item.getGoodsLimits() != null && !item.getGoodsLimits().isEmpty()) {
                Element decGoodsLimits = doc.createElement("DecGoodsLimits");
                decList.appendChild(decGoodsLimits);

                for (GoodsLimit limit : item.getGoodsLimits()) {
                    Element decGoodsLimit = doc.createElement("DecGoodsLimit");
                    decGoodsLimits.appendChild(decGoodsLimit);

                    addNumberElement(decGoodsLimit, "GoodsNo", limit.getGoodsNo());
                    addElement(decGoodsLimit, "LicTypeCode", limit.getLicTypeCode());
                    addElement(decGoodsLimit, "LicenceNo", limit.getLicenceNo());
                    addElement(decGoodsLimit, "LicWrtofDetailNo", limit.getLicWrtofDetailNo());
                    addNumberElement(decGoodsLimit, "LicWrtofQty", limit.getLicWrtofQty());
                    addElement(decGoodsLimit, "LicWrtofQtyUnit", limit.getLicWrtofQtyUnit());

                    // VIN信息
                    if (limit.getGoodsLimitVin() != null) {
                        GoodsLimitVin vin = limit.getGoodsLimitVin();
                        Element decGoodsLimitVin = doc.createElement("DecGoodsLimitVin");
                        decGoodsLimit.appendChild(decGoodsLimitVin);

                        addElement(decGoodsLimitVin, "LicenceNo", vin.getLicenceNo());
                        addElement(decGoodsLimitVin, "LicTypeCode", vin.getLicTypeCode());
                        addElement(decGoodsLimitVin, "VinNo", vin.getVinNo());
                        addElement(decGoodsLimitVin, "BillLadDate", vin.getBillLadDate());
                        addElement(decGoodsLimitVin, "QualityQgp", vin.getQualityQgp());
                        addElement(decGoodsLimitVin, "MotorNo", vin.getMotorNo());
                        addElement(decGoodsLimitVin, "VinCode", vin.getVinCode());
                        addElement(decGoodsLimitVin, "ChassisNo", vin.getChassisNo());
                        addNumberElement(decGoodsLimitVin, "InvoiceNum", vin.getInvoiceNum());
                        addElement(decGoodsLimitVin, "ProdCnnm", vin.getProdCnnm());
                        addElement(decGoodsLimitVin, "ProdEnnm", vin.getProdEnnm());
                        addElement(decGoodsLimitVin, "ModelEn", vin.getModelEn());
                        addNumberElement(decGoodsLimitVin, "PricePerUnit", vin.getPricePerUnit());
                        addElement(decGoodsLimitVin, "InvoiceNo", vin.getInvoiceNo());
                    }
                }
            }
        }
    }

    // ====================== 集装箱信息 ======================

    /**
     * 设置集装箱信息
     */
    public void setContainers(List<ContainerInfo> containers) {
        if (containers == null || containers.isEmpty()) return;

        Element decContainers = doc.createElement("DecContainers");
        root.appendChild(decContainers);

        for (ContainerInfo container : containers) {
            Element containerElement = doc.createElement("Container");
            decContainers.appendChild(containerElement);

            addElement(containerElement, "ContainerId", container.getContainerId());
            addElement(containerElement, "ContainerMd", container.getContainerMd());
            addElement(containerElement, "GoodsNo", container.getGoodsNo());
            addElement(containerElement, "LclFlag", container.getLclFlag());
            addNumberElement(containerElement, "GoodsContaWt", container.getGoodsContaWt());
            addNumberElement(containerElement, "ContainerWt", container.getContainerWt());
        }
    }

    // ====================== 随附单证 ======================


    /**
     * 设置随附单证信息
     */
    public void setLicenseDocus(List<LicenseDocu> docus) {
        if (docus == null || docus.isEmpty()) return;

        Element decLicenseDocus = doc.createElement("DecLicenseDocus");
        root.appendChild(decLicenseDocus);

        for (LicenseDocu docu : docus) {
            Element licenseDocu = doc.createElement("LicenseDocu");
            decLicenseDocus.appendChild(licenseDocu);

            addElement(licenseDocu, "DocuCode", docu.getDocuCode());
            addElement(licenseDocu, "CertCode", docu.getCertCode());
        }
    }

    // ====================== 申请单证信息 ======================


    /**
     * 设置申请单证信息
     */
    public void setRequestCerts(List<RequestCert> certs) {
        if (certs == null || certs.isEmpty()) return;

        Element decRequestCerts = doc.createElement("DecRequestCerts");
        root.appendChild(decRequestCerts);

        for (RequestCert cert : certs) {
            Element decRequestCert = doc.createElement("DecRequestCert");
            decRequestCerts.appendChild(decRequestCert);

            addElement(decRequestCert, "AppCertCode", cert.getAppCertCode());
            addElement(decRequestCert, "ApplOri", cert.getApplOri());
            addElement(decRequestCert, "ApplCopyQuan", cert.getApplCopyQuan());
        }
    }

    // ====================== 其他包装信息 ======================


    /**
     * 设置其他包装信息
     */
    public void setOtherPacks(List<OtherPack> packs) {
        if (packs == null || packs.isEmpty()) return;

        Element decOtherPacks = doc.createElement("DecOtherPacks");
        root.appendChild(decOtherPacks);

        for (OtherPack pack : packs) {
            Element decOtherPack = doc.createElement("DecOtherPack");
            decOtherPacks.appendChild(decOtherPack);

            addNumberElement(decOtherPack, "PackQty", pack.getPackQty());
            addElement(decOtherPack, "PackType", pack.getPackType());
        }
    }

    // ====================== 使用人信息 ======================

    /**
     * 设置使用人信息
     */
    public void setUsers(List<UserInfo> users) {
        if (users == null || users.isEmpty()) return;

        Element decUsers = doc.createElement("DecUsers");
        root.appendChild(decUsers);

        for (UserInfo user : users) {
            Element decUser = doc.createElement("DecUser");
            decUsers.appendChild(decUser);

            addElement(decUser, "UseOrgPersonCode", user.getUseOrgPersonCode());
            addElement(decUser, "UseOrgPersonTel", user.getUseOrgPersonTel());
        }
    }

    // ====================== 企业资质信息 ======================


    /**
     * 设置企业资质信息
     */
    public void setCopLimits(List<CopLimit> limits) {
        if (limits == null || limits.isEmpty()) return;

        Element decCopLimits = doc.createElement("DecCopLimits");
        root.appendChild(decCopLimits);

        for (CopLimit limit : limits) {
            Element decCopLimit = doc.createElement("DecCopLimit");
            decCopLimits.appendChild(decCopLimit);

            addElement(decCopLimit, "EntQualifNo", limit.getEntQualifNo());
            addElement(decCopLimit, "EntQualifTypeCode", limit.getEntQualifTypeCode());
        }
    }

    // ====================== 自由文本信息 ======================


    /**
     * 设置自由文本信息
     */
    public void setFreeText(FreeText freeText) {
        Element decFreeTxt = doc.createElement("DecFreeTxt");
        root.appendChild(decFreeTxt);

        addElement(decFreeTxt, "RelId", freeText.getRelId());
        addElement(decFreeTxt, "RelManNo", freeText.getRelManNo());
        addElement(decFreeTxt, "BonNo", freeText.getBonNo());
        addElement(decFreeTxt, "VoyNo", freeText.getVoyNo());
        addElement(decFreeTxt, "DecBpNo", freeText.getDecBpNo());
        addElement(decFreeTxt, "CusFie", freeText.getCusFie());
        addElement(decFreeTxt, "DecNo", freeText.getDecNo());
    }

    // ====================== 签名信息 ======================


    /**
     * 设置签名信息
     */
    public void setSignInfo(SignInfo signInfo) {
        Element decSign = doc.createElement("DecSign");
        root.appendChild(decSign);

        addElement(decSign, "OperType", signInfo.getOperType());
        addElement(decSign, "ICCode", signInfo.getIcCode());
        addElement(decSign, "CopCode", signInfo.getCopCode());
        addElement(decSign, "OperName", signInfo.getOperName());
        addElement(decSign, "ClientSeqNo", signInfo.getClientSeqNo());
        addElement(decSign, "Sign", signInfo.getSign());
        addElement(decSign, "SignDate", signInfo.getSignDate());
        addElement(decSign, "Certificate", signInfo.getCertificate());
        addElement(decSign, "HostId", signInfo.getHostId());
        addElement(decSign, "DomainId", signInfo.getDomainId());
        addElement(decSign, "Note", signInfo.getNote());
        addElement(decSign, "BillSeqNo", signInfo.getBillSeqNo());
    }

    // ====================== 电子随附单据关联关系 ======================


    /**
     * 设置电子随附单据关联关系
     */
    public void setEdocRelation(EdocRelation relation) {
        Element edocRealation = doc.createElement("EdocRealation");
        root.appendChild(edocRealation);

        addElement(edocRealation, "EdocID", relation.getEdocId());
        addElement(edocRealation, "EdocCode", relation.getEdocCode());
        addElement(edocRealation, "EdocFomatType", relation.getEdocFomatType());
        addElement(edocRealation, "OpNote", relation.getOpNote());
        addElement(edocRealation, "EdocCopId", relation.getEdocCopId());
        addElement(edocRealation, "EdocOwnerCode", relation.getEdocOwnerCode());
        addElement(edocRealation, "SignUnit", relation.getSignUnit());
        addElement(edocRealation, "SignTime", relation.getSignTime());
        addElement(edocRealation, "EdocOwnerName", relation.getEdocOwnerName());
        addElement(edocRealation, "EdocSize", relation.getEdocSize());
    }
}