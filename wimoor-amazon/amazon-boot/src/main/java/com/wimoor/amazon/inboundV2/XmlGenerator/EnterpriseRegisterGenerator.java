package com.wimoor.amazon.inboundV2.XmlGenerator;


import com.wimoor.amazon.inboundV2.XmlPojo.EnterpriseInfo;
import com.wimoor.amazon.inboundV2.util.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;

/**
 * 关联企业注册 (REG001) XML生成器
 */
public class EnterpriseRegisterGenerator {



    /**
     * 生成关联企业注册XML
     */
    public static String generateEnterpriseRegisterXml(List<EnterpriseInfo> enterpriseList)
            throws Exception {
        Document doc = XmlHelper.createDocument();

        // 创建根节点
        Element root = doc.createElement("REG001Message");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns", "http://www.chinaport.gov.cn/ceb");
        root.setAttribute("version", "1.0");
        root.setAttribute("guid", XmlHelper.generateGuid());
        doc.appendChild(root);

        // 创建Enterprise节点
        Element enterprise = doc.createElement("Enterprise");
        root.appendChild(enterprise);

        // 添加企业信息
        for (EnterpriseInfo info : enterpriseList) {
            Element enterpriseInfo = doc.createElement("EnterpriseInfo");
            enterprise.appendChild(enterpriseInfo);

            XmlHelper.addElement(doc, enterpriseInfo, "guid", info.getGuid());
            XmlHelper.addElement(doc, enterpriseInfo, "orgCode", info.getOrgCode());
            XmlHelper.addElement(doc, enterpriseInfo, "orgName", info.getOrgName());
            XmlHelper.addElement(doc, enterpriseInfo, "cusCode", info.getCusCode());
            XmlHelper.addElement(doc, enterpriseInfo, "bizCategories", info.getBizCategories());
            XmlHelper.addElement(doc, enterpriseInfo, "contactPerson", info.getContactPerson());
            XmlHelper.addElement(doc, enterpriseInfo, "contactTelephone", info.getContactTelephone());
        }

        return XmlHelper.documentToString(doc);
    }
}
