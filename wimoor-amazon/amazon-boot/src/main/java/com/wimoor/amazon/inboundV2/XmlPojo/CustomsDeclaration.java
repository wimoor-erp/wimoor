package com.wimoor.amazon.inboundV2.XmlPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一报关单数据实体
 * 封装所有报关单相关数据，用于简化XML生成调用
 */
public class CustomsDeclaration {

    // 报关单表头
    private DeclarationHeader header;

    // 报关单商品项列表
    private List<DeclarationItem> items;

    // 集装箱信息列表
    private List<ContainerInfo> containers;

    // 随附单证列表
    private List<LicenseDocu> licenseDocus;

    // 申请单证列表
    private List<RequestCert> requestCerts;

    // 其他包装信息列表
    private List<OtherPack> otherPacks;

    // 使用人信息列表
    private List<UserInfo> users;

    // 企业资质信息列表
    private List<CopLimit> copLimits;

    // 自由文本信息
    private FreeText freeText;

    // 签名信息
    private SignInfo signInfo;

    // 电子随附单据关联关系
    private EdocRelation edocRelation;

    // 商品限制信息（内部使用）
    private List<GoodsLimit> goodsLimits;

    // 其他业务字段
    private String note;
    private String businessNo;  // 业务流水号
    private String version = "V4.0";  // 报文版本

    // ==================== 构造函数 ====================

    public CustomsDeclaration() {
        // 初始化列表
        this.items = new ArrayList<>();
        this.containers = new ArrayList<>();
        this.licenseDocus = new ArrayList<>();
        this.requestCerts = new ArrayList<>();
        this.otherPacks = new ArrayList<>();
        this.users = new ArrayList<>();
        this.copLimits = new ArrayList<>();
        this.goodsLimits = new ArrayList<>();
    }

    /**
     * 创建简化报关单（只有表头和商品项）
     */
    public static CustomsDeclaration createSimpleDeclaration(DeclarationHeader header,
                                                                    List<DeclarationItem> items) {
        CustomsDeclaration declaration = new CustomsDeclaration();
        declaration.setHeader(header);
        if (items != null) {
            declaration.setItems(items);
        }
        return declaration;
    }

    /**
     * 创建完整报关单
     */
    public static CustomsDeclaration createFullDeclaration(DeclarationHeader header,
                                                                  List<DeclarationItem> items,
                                                                  List<ContainerInfo> containers,
                                                                  List<LicenseDocu> licenseDocus,
                                                                  List<CopLimit> copLimits) {
        CustomsDeclaration declaration = new CustomsDeclaration();
        declaration.setHeader(header);
        if (items != null) {
            declaration.setItems(items);
        }
        if (containers != null) {
            declaration.setContainers(containers);
        }
        if (licenseDocus != null) {
            declaration.setLicenseDocus(licenseDocus);
        }
        if (copLimits != null) {
            declaration.setCopLimits(copLimits);
        }
        return declaration;
    }

    // ==================== 便捷方法 ====================

    /**
     * 添加商品项
     */
    public void addItem(DeclarationItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    /**
     * 添加集装箱信息
     */
    public void addContainer(ContainerInfo container) {
        if (this.containers == null) {
            this.containers = new ArrayList<>();
        }
        this.containers.add(container);
    }

    /**
     * 添加随附单证
     */
    public void addLicenseDocu(LicenseDocu docu) {
        if (this.licenseDocus == null) {
            this.licenseDocus = new ArrayList<>();
        }
        this.licenseDocus.add(docu);
    }

    /**
     * 添加企业资质
     */
    public void addCopLimit(CopLimit limit) {
        if (this.copLimits == null) {
            this.copLimits = new ArrayList<>();
        }
        this.copLimits.add(limit);
    }

    // ==================== 验证方法 ====================

    /**
     * 验证报关单数据是否完整
     */
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();

        if (header == null) {
            result.addError("报关单表头不能为空");
            return result;
        }

        // 验证必填字段
        if (isEmpty(header.getSeqNo())) {
            result.addError("报关单序号不能为空");
        }

        if (isEmpty(header.getIeFlag())) {
            result.addError("进出口标志不能为空");
        }

        if (isEmpty(header.getCopCode())) {
            result.addError("经营单位代码不能为空");
        }

        if (isEmpty(header.getCopName())) {
            result.addError("经营单位名称不能为空");
        }

        if (isEmpty(header.getTradeMode())) {
            result.addError("贸易方式不能为空");
        }

        if (items == null || items.isEmpty()) {
            result.addError("报关单商品项不能为空");
        } else {
            // 验证商品项
            for (int i = 0; i < items.size(); i++) {
                DeclarationItem item = items.get(i);
                if (item == null) {
                    result.addError("第" + (i+1) + "个商品项为空");
                    continue;
                }

                if (isEmpty(item.getGName())) {
                    result.addError("第" + (i+1) + "个商品项名称不能为空");
                }

                if (item.getGQty() == null || item.getGQty().doubleValue() <= 0) {
                    result.addError("第" + (i+1) + "个商品项数量必须大于0");
                }

                if (item.getDeclPrice() == null || item.getDeclPrice().doubleValue() <= 0) {
                    result.addError("第" + (i+1) + "个商品项申报单价必须大于0");
                }
            }
        }

        return result;
    }

    /**
     * 获取商品总金额
     */
    public double getTotalDeclValue() {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (DeclarationItem item : items) {
            if (item.getDeclTotal() != null) {
                total += item.getDeclTotal().doubleValue();
            }
        }
        return total;
    }

    /**
     * 获取商品总数量
     */
    public double getTotalQuantity() {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (DeclarationItem item : items) {
            if (item.getGQty() != null) {
                total += item.getGQty().doubleValue();
            }
        }
        return total;
    }

    /**
     * 获取集装箱总数
     */
    public int getContainerCount() {
        return containers != null ? containers.size() : 0;
    }

    /**
     * 获取随附单证数量
     */
    public int getLicenseDocCount() {
        return licenseDocus != null ? licenseDocus.size() : 0;
    }

    // ==================== 验证结果内部类 ====================

    public static class ValidationResult {
        private boolean valid = true;
        private List<String> errors = new ArrayList<>();

        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorString() {
            return String.join("; ", errors);
        }
    }

    // ==================== getter和setter方法 ====================

    public DeclarationHeader getHeader() {
        return header;
    }

    public void setHeader(DeclarationHeader header) {
        this.header = header;
    }

    public List<DeclarationItem> getItems() {
        return items;
    }

    public void setItems(List<DeclarationItem> items) {
        this.items = items;
    }

    public List<ContainerInfo> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerInfo> containers) {
        this.containers = containers;
    }

    public List<LicenseDocu> getLicenseDocus() {
        return licenseDocus;
    }

    public void setLicenseDocus(List<LicenseDocu> licenseDocus) {
        this.licenseDocus = licenseDocus;
    }

    public List<RequestCert> getRequestCerts() {
        return requestCerts;
    }

    public void setRequestCerts(List<RequestCert> requestCerts) {
        this.requestCerts = requestCerts;
    }

    public List<OtherPack> getOtherPacks() {
        return otherPacks;
    }

    public void setOtherPacks(List<OtherPack> otherPacks) {
        this.otherPacks = otherPacks;
    }

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    public List<CopLimit> getCopLimits() {
        return copLimits;
    }

    public void setCopLimits(List<CopLimit> copLimits) {
        this.copLimits = copLimits;
    }

    public FreeText getFreeText() {
        return freeText;
    }

    public void setFreeText(FreeText freeText) {
        this.freeText = freeText;
    }

    public SignInfo getSignInfo() {
        return signInfo;
    }

    public void setSignInfo(SignInfo signInfo) {
        this.signInfo = signInfo;
    }

    public EdocRelation getEdocRelation() {
        return edocRelation;
    }

    public void setEdocRelation(EdocRelation edocRelation) {
        this.edocRelation = edocRelation;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // ==================== 私有方法 ====================

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}