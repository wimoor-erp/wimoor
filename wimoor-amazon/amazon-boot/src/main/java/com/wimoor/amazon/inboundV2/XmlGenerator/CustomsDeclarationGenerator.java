package com.wimoor.amazon.inboundV2.XmlGenerator;


import com.wimoor.amazon.inboundV2.XmlPojo.*;
import java.util.List;

/**
 * 报关单XML生成器
 * 封装CustomsDeclarationHelper，提供更简洁的API
 */
public class CustomsDeclarationGenerator {

    /**
     * 生成报关单XML
     * @param declaration 统一报关单数据
     * @return XML字符串
     * @throws Exception 生成异常
     */
    public static String generateDeclarationXml(CustomsDeclaration declaration) throws Exception {
        // 验证数据
        CustomsDeclaration.ValidationResult validation = declaration.validate();
        if (!validation.isValid()) {
            throw new IllegalArgumentException("报关单数据验证失败: " + validation.getErrorString());
        }

        // 创建Helper实例
        CustomsDeclarationHelper helper = new CustomsDeclarationHelper();

        // 设置表头
        helper.setDeclarationHeader(declaration.getHeader());

        // 设置商品项
        helper.setDeclarationItems(declaration.getItems());

        // 设置集装箱信息（如果有）
        List<ContainerInfo> containers = declaration.getContainers();
        if (containers != null && !containers.isEmpty()) {
            helper.setContainers(containers);
        }

        // 设置随附单证（如果有）
        List<LicenseDocu> licenseDocus = declaration.getLicenseDocus();
        if (licenseDocus != null && !licenseDocus.isEmpty()) {
            helper.setLicenseDocus(licenseDocus);
        }

        // 设置申请单证（如果有）
        List<RequestCert> requestCerts = declaration.getRequestCerts();
        if (requestCerts != null && !requestCerts.isEmpty()) {
            helper.setRequestCerts(requestCerts);
        }

        // 设置其他包装（如果有）
        List<OtherPack> otherPacks = declaration.getOtherPacks();
        if (otherPacks != null && !otherPacks.isEmpty()) {
            helper.setOtherPacks(otherPacks);
        }

        // 设置使用人信息（如果有）
        List<UserInfo> users = declaration.getUsers();
        if (users != null && !users.isEmpty()) {
            helper.setUsers(users);
        }

        // 设置企业资质（如果有）
        List<CopLimit> copLimits = declaration.getCopLimits();
        if (copLimits != null && !copLimits.isEmpty()) {
            helper.setCopLimits(copLimits);
        }

        // 设置自由文本（如果有）
        FreeText freeText = declaration.getFreeText();
        if (freeText != null) {
            helper.setFreeText(freeText);
        }

        // 设置签名信息（如果有）
        SignInfo signInfo = declaration.getSignInfo();
        if (signInfo != null) {
            helper.setSignInfo(signInfo);
        }

        // 设置电子随附单据（如果有）
        EdocRelation edocRelation = declaration.getEdocRelation();
        if (edocRelation != null) {
            helper.setEdocRelation(edocRelation);
        }

        // 生成XML
        return helper.toXmlString();
    }

    /**
     * 生成简单的报关单XML（只有表头和商品项）
     */
    public static String generateSimpleDeclarationXml(DeclarationHeader header,
                                                      List<DeclarationItem> items) throws Exception {
        CustomsDeclaration declaration = CustomsDeclaration.createSimpleDeclaration(header, items);
        return generateDeclarationXml(declaration);
    }

    /**
     * 生成完整的报关单XML
     */
    public static String generateFullDeclarationXml(DeclarationHeader header,
                                                    List<DeclarationItem> items,
                                                    List<ContainerInfo> containers,
                                                    List<LicenseDocu> licenseDocus,
                                                    List<CopLimit> copLimits) throws Exception {
        CustomsDeclaration declaration = CustomsDeclaration.createFullDeclaration(
                header, items, containers, licenseDocus, copLimits);
        return generateDeclarationXml(declaration);
    }

    /**
     * 生成报关单文件内容（包含文件名和XML内容）
     */
    public static DeclarationFile generateDeclarationFile(CustomsDeclaration declaration,
                                                          String senderId) throws Exception {
        String xmlContent = generateDeclarationXml(declaration);
        String fileName = generateFileName(declaration, senderId);

        DeclarationFile file = new DeclarationFile();
        file.setFileName(fileName);
        file.setXmlContent(xmlContent);
        file.setBusinessNo(declaration.getBusinessNo());
        file.setTotalValue(declaration.getTotalDeclValue());
        file.setItemCount(declaration.getItems() != null ? declaration.getItems().size() : 0);

        return file;
    }

    /**
     * 生成报关单文件名
     */
    public static String generateFileName(CustomsDeclaration declaration, String senderId) {
        String timestamp = CustomsDeclarationHelper.getCurrentTimestamp();
        String sequence = "001"; // 可以根据业务生成序列号

        DeclarationHeader header = declaration.getHeader();
        String type = "I".equals(header.getIeFlag()) ? "IMPORT" : "EXPORT";

        return String.format("DEC_%s_%s_%s_%s_%s.xml",
                type,
                header.getSeqNo(),
                senderId,
                timestamp,
                sequence);
    }

    /**
     * 报关单文件信息
     */
    public static class DeclarationFile {
        private String fileName;
        private String xmlContent;
        private String businessNo;
        private double totalValue;
        private int itemCount;

        // getter/setter
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getXmlContent() { return xmlContent; }
        public void setXmlContent(String xmlContent) { this.xmlContent = xmlContent; }

        public String getBusinessNo() { return businessNo; }
        public void setBusinessNo(String businessNo) { this.businessNo = businessNo; }

        public double getTotalValue() { return totalValue; }
        public void setTotalValue(double totalValue) { this.totalValue = totalValue; }

        public int getItemCount() { return itemCount; }
        public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    }
}