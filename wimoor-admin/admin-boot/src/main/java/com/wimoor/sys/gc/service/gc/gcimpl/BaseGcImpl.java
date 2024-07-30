package com.wimoor.sys.gc.service.gc.gcimpl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.constant.Base;
import com.wimoor.sys.gc.constant.BracketConstant;
import com.wimoor.sys.gc.constant.FieldTypeConstant;
import com.wimoor.sys.gc.model.po.DbFieldPO;
import com.wimoor.sys.gc.template.VueAddUpdSlotTemplate;
import com.wimoor.sys.gc.template.VueAddUpdTemplate;
import com.wimoor.sys.gc.template.VueMainSlotTemplate;
import com.wimoor.sys.gc.util.GcDataUtil;

/**
 * 通用信息处理
 */

public class BaseGcImpl  {

    private final static String NO = "NO";

    /**
     * 数据库类型对应字段生成
     * <p>
     * 用于 entity+dto+vo+query
     * </P>
     *
     * @param gcConfig  配置信息
     * @param fieldName 字段名
     * @param type      字段类型
     * @param isTimeStr 时间是否使用字符串,只处理 datetime 字段类型（特殊处理, 主要处理满足 query 范围查询的代码自动生成）
     */
    protected String jxModel(GcConfig gcConfig, String fieldName, String type, Boolean isTimeStr) {

        isTimeStr = isTimeStr != null && isTimeStr;
        // 转驼峰模式
        fieldName = GcDataUtil.getFieldName(gcConfig, fieldName);
        String field = "";
        // 字段
        if (type.equals(FieldTypeConstant.MEDIUMINT) || type.equals(FieldTypeConstant.SMALLINT) || type.equals(FieldTypeConstant.INT) || type.equals(FieldTypeConstant.TINYINT)) {
            // 整数int
            field = "private Integer " + fieldName + ";";
        } else if (type.equals(FieldTypeConstant.BIGINT)) {
            // 整数Long
            field = "private Long " + fieldName + ";";
        } else if (type.equals(FieldTypeConstant.VARCHAR) || type.equals(FieldTypeConstant.CHAR)) {
            // 字符串
            field = "private String " + fieldName + ";";
        } else if (type.equals(FieldTypeConstant.BINARY) || type.equals(FieldTypeConstant.VARBINARY)) {
            // 字符串
            field = "private String " + fieldName + ";";
        } else if (type.equals(FieldTypeConstant.TEXT) || type.equals(FieldTypeConstant.LONG_TEXT) || type.equals(FieldTypeConstant.JSON)) {
            // 大文本、超大文本、json
            field = "private String " + fieldName + ";";
        } else if (type.equals(FieldTypeConstant.DATETIME) || type.equals(FieldTypeConstant.TIME)
                || type.equals(FieldTypeConstant.TIMESTAMP) || type.equals(FieldTypeConstant.DATE)) {
            // 时间
            if ((type.equals(FieldTypeConstant.DATETIME) || type.equals(FieldTypeConstant.DATE))
                    && isTimeStr) {
                field = "private String " + fieldName + ";";
            } else {
                field = "private LocalDateTime " + fieldName + ";";
            }
        } else if (type.equals(FieldTypeConstant.DOUBLE)) {
            // 双精度小数 Double
            field = "private Double " + fieldName + ";";
        } else if (type.equals(FieldTypeConstant.FLOAT)) {
            // 单精度小数 Float
            field = "private Float " + fieldName + ";";
        } else if (type.equals(FieldTypeConstant.DECIMAL)) {
            // 小数 decimal
            field = "private BigDecimal " + fieldName + ";";
        }
        //   else if (type.equals(FieldTypeConstant.TINYINT)) {
        //       // 布尔 tinyint
        //       field = "private Boolean " + fieldName + ";";
        //   }
        return field;
    }


    /**
     * 必填字段获取 jsr303 验证注解，
     *
     * @param isNull     NO 代表必填, YES 非必填
     * @param type       字段类型
     * @param typeDetail 字段类型长度,  如: int(11) 在 mysql8.0.16+ 版本后, 删除了 int /bigint 的长度支持 (自动使用最大值)
     * @param desc       字段备注
     * @param isRequired 是否判断必填 true 是 false 否, query 查询中不需要验证必传
     */
    protected String jsrModel(String isNull, String type, String typeDetail, String desc, boolean isRequired) {
        String jsr = "";
        if ((NO).equals(isNull) && isRequired) {
            if (type.equals(FieldTypeConstant.VARCHAR) || type.equals(FieldTypeConstant.CHAR)
                    || type.equals(FieldTypeConstant.TEXT) || type.equals(FieldTypeConstant.LONG_TEXT)
                    || type.equals(FieldTypeConstant.BINARY) || type.equals(FieldTypeConstant.VARBINARY)
            ) {
                jsr = "    @NotBlank(message = \"{DESC} 不能为空\")";
            } else {
                jsr = "    @NotNull(message = \"{DESC} 不能为空\")";
            }
            jsr = jsr.replace("\\{DESC}", desc);
            jsr += "\n";
        }
        // 获取数据库注释,去除括号后的内容
        desc = desc.contains(BracketConstant.LEFT_BRACKET) ? desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET)) : desc;
        desc = desc.contains(BracketConstant.LEFT_BRACKET_TWO) ? desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_TWO)) : desc;
        desc = desc.contains(BracketConstant.LEFT_BRACKET_THREE) ? desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_THREE)) : desc;
        desc = desc.contains(BracketConstant.LEFT_BRACKET_FOUR) ? desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_FOUR)) : desc;
        desc = desc.contains(BracketConstant.LEFT_BRACKET_FIVE) ? desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_FIVE)) : desc;
        desc = desc.contains(BracketConstant.LEFT_BRACKET_SIX) ? desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_SIX)) : desc;

        //字段
        String maxlength = "";
        if (type.equals(FieldTypeConstant.INT) || type.equals(FieldTypeConstant.BIGINT) || type.equals(FieldTypeConstant.TINYINT)
                || type.equals(FieldTypeConstant.SMALLINT) || type.equals(FieldTypeConstant.MEDIUMINT)
        ) {
            // 整数
            // int(11), 判断是否有长度,存在长度获取指定长度的最大值, 转为long添加到注解中
            if (typeDetail.contains(BracketConstant.LEFT_BRACKET)) {
                int len = Integer.parseInt(typeDetail.substring(typeDetail.indexOf("(") + 1, typeDetail.indexOf(")")));
                maxlength = len >= 19 ? Long.MAX_VALUE + "" : this.numberGenerator(9, len, 0);
            } else {
                maxlength = this.getDefaultMaxlength(type);
            }
            jsr += "    @Range(min=0L, max={MAX}L,message = \"{DESC} 必须>=0 和 <={MAX}\")";
            jsr = jsr.replaceAll("\\{MAX}", maxlength + "").replace("{DESC}", desc);
        } else if (type.equals(FieldTypeConstant.DOUBLE) || type.equals(FieldTypeConstant.FLOAT) || type.equals(FieldTypeConstant.DECIMAL)) {
            // 小数 判断是否有长度,存在长度获取指定长度的最大值, 转为long添加到注解中 decimal(10,2)，取10, 2不处理
            if (typeDetail.contains(BracketConstant.LEFT_BRACKET)) {
                String typeDetailStr = typeDetail.substring(typeDetail.indexOf(BracketConstant.LEFT_BRACKET) + 1, typeDetail.indexOf(BracketConstant.RIGHT_BRACKET));
                String[] typeDetailStrArray = typeDetailStr.split(",");
                if (typeDetailStrArray.length > 1) {
                    maxlength = this.numberGenerator(9, Integer.parseInt(typeDetailStrArray[0]), Integer.parseInt(typeDetailStrArray[1]));
                } else {
                    maxlength = this.numberGenerator(9, Integer.parseInt(typeDetailStrArray[0]), 0);
                }
            } else {
                maxlength = this.getDefaultMaxlength(type);
            }
            jsr += "    @DecimalMin(value=\"0\",message=\"{DESC} 必须 >= 0\")";
            jsr += "\n    @DecimalMax(value=\"{MAX}\",message=\"{DESC} 必须 <= {MAX}\")";
            jsr = jsr.replaceAll("\\{MAX}", maxlength).replace("{DESC}", desc);
        } else if (type.equals(FieldTypeConstant.VARCHAR) || type.equals(FieldTypeConstant.CHAR) || type.equals(FieldTypeConstant.TEXT) || type.equals(FieldTypeConstant.LONG_TEXT)) {
            // 字符串
            if (typeDetail.contains(BracketConstant.LEFT_BRACKET)) {
                maxlength = typeDetail.substring(typeDetail.indexOf(BracketConstant.LEFT_BRACKET) + 1, typeDetail.indexOf(BracketConstant.RIGHT_BRACKET));
            } else {
                maxlength = this.getDefaultMaxlength(type);
            }
            jsr += "    @Length(min=0, max={MAX},message = \"{DESC} 必须>=0 和 <={MAX}位\")";
            jsr = jsr.replaceAll("\\{MAX}", maxlength).replace("{DESC}", desc);
        } else if (type.equals(FieldTypeConstant.DATETIME) || type.equals(FieldTypeConstant.TIME) || type.equals(FieldTypeConstant.TIMESTAMP)) {
            // 时间暂无
        }
        return jsr;
    }


    /**
     * 判断当前字段是否勾选
     *
     * @param fieldMap
     * @return boolean true 表示勾选，false 表示为勾选
     * @author wangsong
     * @date 2021/11/4 0004 7:04
     * @version 1.0.0
     */
    protected boolean isChecked(DbFieldPO fieldMap) {
        // 兼容layui
        boolean checked = false;
        if (fieldMap.getChecked() != null) {
            checked = fieldMap.getChecked();
        }
        // 兼容vue
        boolean isChecked = false;
        if (fieldMap.getIsChecked() != null) {
            isChecked = fieldMap.getIsChecked();
        }
        return checked || isChecked;
    }

    /**
     * 获取 desc 字段描叙 去掉 () 内后的数据
     *
     * @param desc
     * @return String
     */
    protected String getDesc(String desc, String fieldName) {
        if (desc != null) {
            if (desc.contains(BracketConstant.LEFT_BRACKET)) {
                desc = desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET));
            }
            if (desc.contains(BracketConstant.LEFT_BRACKET_TWO)) {
                desc = desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_TWO));
            }
            if (desc.contains(BracketConstant.LEFT_BRACKET_THREE)) {
                desc = desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_THREE));
            }
            if (desc.contains(BracketConstant.LEFT_BRACKET_FOUR)) {
                desc = desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_FOUR));
            }
            if (desc.contains(BracketConstant.LEFT_BRACKET_FIVE)) {
                desc = desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_FIVE));
            }
            if (desc.contains(BracketConstant.LEFT_BRACKET_SIX)) {
                desc = desc.substring(0, desc.indexOf(BracketConstant.LEFT_BRACKET_SIX));
            }
            desc = removeDescTheNewlineCharacter(desc, fieldName);
        } else {
            desc = fieldName;
        }
        return desc;
    }

    /**
     * 去除换行符号 替换为空格
     *
     * @param desc
     * @return
     */
    protected String removeDescTheNewlineCharacter(String desc, String fieldName) {
        if (StringUtils.isNotBlank(desc)) {
            desc = desc.replaceAll("\r", " ");
            desc = desc.replaceAll("\n", " ");
        } else {
            desc = fieldName;
        }
        return desc;
    }


    /**
     * 获取vue 添加或编辑页的 表单数据
     *
     * @param gcConfig     代码生成配置信息
     * @param name         字段名
     * @param type         字段类型
     * @param typeDetail   类型+长度
     * @param newDesc      字段中文描述
     * @param vueFieldType vue表单字段类型
     * @param isNull       是否必填 NO 代表必填, YES 非必填
     * @return
     */
    protected String jxVueColumns(GcConfig gcConfig, String name, String type, String typeDetail, String newDesc, Integer vueFieldType, List<String> dictCode, String isNull) {
        boolean required = isNull.equals(NO);
        // 生成表单时获取数据库的字段的长度来控制输入
        String maxlength = "0";
        // 小数位
        int precision = 0;
        if (type.equals(FieldTypeConstant.INT) || type.equals(FieldTypeConstant.BIGINT) || type.equals(FieldTypeConstant.TINYINT)) {
            // int(11), 判断是否有长度,存在长度获取指定长度的最大值, 转为long添加到注解中
            if (typeDetail.contains(BracketConstant.LEFT_BRACKET)) {
                int len = Integer.parseInt(typeDetail.substring(typeDetail.indexOf(BracketConstant.LEFT_BRACKET) + 1, typeDetail.indexOf(BracketConstant.RIGHT_BRACKET)));
                maxlength = this.numberGenerator(9, len, 0);
            } else {
                // 默认大小
                maxlength = this.getDefaultMaxlength(type);
            }
        } else if (type.equals(FieldTypeConstant.DOUBLE) || type.equals(FieldTypeConstant.FLOAT) || type.equals(FieldTypeConstant.DECIMAL)) {
            //  小数
            //  判断是否有长度,存在长度获取指定长度的最大值, 转为long添加到注解中 decimal(10,2)，取10, 2不处理
            if (typeDetail.contains(BracketConstant.LEFT_BRACKET)) {
                String typeDetailStr = typeDetail.substring(typeDetail.indexOf(BracketConstant.LEFT_BRACKET) + 1, typeDetail.indexOf(BracketConstant.RIGHT_BRACKET));
                String[] typeDetailStrArray = typeDetailStr.split(",");
                if (typeDetailStrArray.length > 1) {
                    maxlength = this.numberGenerator(9, Integer.parseInt(typeDetailStrArray[0]), Integer.parseInt(typeDetailStrArray[1]));
                } else {
                    maxlength = this.numberGenerator(9, Integer.parseInt(typeDetailStrArray[0]), 0);
                }
            } else {
                // 默认大小
                maxlength = this.getDefaultMaxlength(type);
            }

        } else if (type.equals(FieldTypeConstant.VARCHAR) || type.equals(FieldTypeConstant.CHAR) || type.equals(FieldTypeConstant.TEXT) || type.equals(FieldTypeConstant.LONG_TEXT)) {
            // 字符串
            if (typeDetail.contains(BracketConstant.LEFT_BRACKET)) {
                maxlength = typeDetail.substring(typeDetail.indexOf(BracketConstant.LEFT_BRACKET) + 1, typeDetail.indexOf(BracketConstant.RIGHT_BRACKET));
            }
        } else if (type.equals(FieldTypeConstant.DATETIME) || type.equals(FieldTypeConstant.TIME) || type.equals(FieldTypeConstant.TIMESTAMP)) {
        }
        // 处理字段
        String columnStr = "";
        name = GcDataUtil.getFieldName(gcConfig, name);
        if (Base.VueFieldType.V1.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.INPUT.replace("{maxlength}", maxlength + "");
        } else if (Base.VueFieldType.V2.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.NUMBER.replace("{precision}", precision + "").replace("{maxRows}", maxlength);
        } else if (Base.VueFieldType.V3.getValue().equals(vueFieldType)) {
        } else if (Base.VueFieldType.V4.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.RADIO.replace("{dictCode}", getDictCode(dictCode));
        } else if (Base.VueFieldType.V5.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.CHECKBOX.replace("{dictCode}", getDictCode(dictCode));
        } else if (Base.VueFieldType.V6.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.SELECT.replace("{dictCode}", getDictCode(dictCode));
        } else if (Base.VueFieldType.V7.getValue().equals(vueFieldType)) {
        } else if (Base.VueFieldType.V8.getValue().equals(vueFieldType)) {
        } else if (Base.VueFieldType.V9.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.SWITCH.replace("{dictCode}", getDictCode(dictCode));
        } else if (Base.VueFieldType.V10.getValue().equals(vueFieldType)) {
        } else if (Base.VueFieldType.V11.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.DATETIME.replaceAll("\\{label}", newDesc).replace("{prop}", name);
        } else if (Base.VueFieldType.V12.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.TIME.replaceAll("\\{label}", newDesc).replace("{prop}", name);
        } else if (Base.VueFieldType.V13.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.UPLOAD.replace("{listType}", "picture-img").replace("{limit}", "1").replace("{fileType}", "img").replace("{accept}", "'image/png, image/jpeg, image/jpg, image/gif'").replace("{tip}", "只能上传 jpg/png/gif 格式的图片");
        } else if (Base.VueFieldType.V14.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.UPLOAD.replace("{listType}", "picture-card").replace("{limit}", "10").replace("{fileType}", "img").replace("{accept}", "'image/png, image/jpeg, image/jpg, image/gif'").replace("{tip}", "只能上传10张 jpg/png/gif 格式的图片");
        } else if (Base.VueFieldType.V15.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.UPLOAD.replace("{listType}", "picture-img").replace("{limit}", "1").replace("{fileType}", "video").replace("{accept}", "'video/mp4'").replace("{tip}", "只能上传mp4格式的视频");
        } else if (Base.VueFieldType.V16.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.UPLOAD.replace("{listType}", "").replace("{limit}", "10").replace("{fileType}", "all").replace("{accept}", "null").replace("{tip}", "");
        } else if (Base.VueFieldType.V17.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.TEXTAREA.replace("{maxlength}", maxlength + "");
        } else if (Base.VueFieldType.V18.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.INPUT.replace("{maxlength}", maxlength + "");
        } else if (Base.VueFieldType.V19.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.INPUT.replace("{maxlength}", maxlength + "");
        } else if (Base.VueFieldType.V20.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.CASCADER;
        } else if (Base.VueFieldType.V21.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.ARRAY;
        } else if (Base.VueFieldType.V22.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.ICON;
        } else if (Base.VueFieldType.V23.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.COLOR;
        } else if (Base.VueFieldType.V24.getValue().equals(vueFieldType)) {
            columnStr = VueAddUpdTemplate.MAP;
        } else {
            // 没有默认 input
            columnStr = VueAddUpdTemplate.INPUT.replace("{maxlength}", maxlength + "");
        }
        // 基础必填字段
        columnStr = columnStr.replaceAll("\\{label}", newDesc)
                .replaceAll("\\{prop}", name)
                .replace("{required}", required + "");
        return columnStr;
    }


    /**
     * vue 字段需要定义为插槽的处理 (添加和编辑页)
     *
     * @param vueFieldType vue表单字段类型
     * @param name         字段名
     * @author wangsong
     * @mail 1720696548@qq.com
     * @date 2022/5/14 0014 22:41
     * @version 1.0.0
     */
    protected String jxVueColumnsSlot(Integer vueFieldType, String name) {
        String vueAddUpdSlot = "";
        if (Base.VueFieldType.V18.getValue().equals(vueFieldType)) {
            // 追加定义富文本插槽
            vueAddUpdSlot = VueAddUpdSlotTemplate.TINYMCE_EDITOR.replace("{prop}", name);
        }
        if (Base.VueFieldType.V19.getValue().equals(vueFieldType)) {
            // 追加定义md编辑器插槽
            vueAddUpdSlot = VueAddUpdSlotTemplate.MD_EDITOR.replace("{prop}", name);
        }
        return vueAddUpdSlot;
    }


    /**
     * vue 字段需要定义为插槽的处理 (列表页，搜索插槽 或 特殊字段展示的插槽)
     *
     * @param vueFieldType vue表单字段类型
     * @param name         字段名
     * @param isSearch     是否搜索
     * @author wangsong
     * @mail 1720696548@qq.com
     * @date 2022/5/14 0014 22:41
     * @version 1.0.0
     */
    protected String jxVueInfoColumnsSlot(Integer vueFieldType, String name, boolean isSearch) {
        String vueAddUpdSlot = "";
        if (Base.VueFieldType.V11.getValue().equals(vueFieldType)) {
            // 时间范围搜索插槽
            vueAddUpdSlot = VueMainSlotTemplate.DATE_PICKER.replace("{prop}", name);
        }
        return vueAddUpdSlot;
    }


    /**
     * 获取字典替换值
     *
     * @param dictCode
     * @return
     */
    protected String getDictCode(List<String> dictCode) {
        String dictCodeStr = "this.website.Dict.Base.Default";
        if (dictCode != null && dictCode.size() > 0) {
            dictCodeStr = dictCode.get(dictCode.size() - 1);
            dictCodeStr = "'" + dictCodeStr + "'";
        }
        return dictCodeStr;
    }

    /**
     * 数字生成器 小数位 digital
     *
     * @param num          数字
     * @param len          长度
     * @param decimalPlace 小数位
     * @return {@link String}
     */
    private String numberGenerator(Integer num, Integer len, Integer decimalPlace) {
        StringBuffer numStr = new StringBuffer();
        for (int i = 0; i < len; i++) {
            numStr.append(num);
        }
        if (decimalPlace != null && decimalPlace > 0) {
            numStr.append(".");
            for (int i = 0; i < decimalPlace; i++) {
                numStr.append(num);
            }
        }
        return numStr.toString();
    }


    /**
     * 得到整数大小
     *
     * @param type 类型
     * @return {@link String}
     */
    private String getDefaultMaxlength(String type) {
        String maxlength = "";
        if (type.equals(FieldTypeConstant.TINYINT)) {
            maxlength = this.numberGenerator(9, 1, 0);
        } else if (type.equals(FieldTypeConstant.INT)) {
            maxlength = this.numberGenerator(9, 9, 0);
        } else if (type.equals(FieldTypeConstant.BIGINT)) {
            maxlength = this.numberGenerator(9, 18, 0);
        } else if (type.equals(FieldTypeConstant.FLOAT)) {
            maxlength = this.numberGenerator(9, 9, 2);
        } else if (type.equals(FieldTypeConstant.DOUBLE)) {
            maxlength = this.numberGenerator(9, 18, 2);
        } else if (type.equals(FieldTypeConstant.DECIMAL)) {
            maxlength = this.numberGenerator(9, 18, 2);
        } else if (type.equals(FieldTypeConstant.CHAR)) {
            maxlength = "1";
        } else if (type.equals(FieldTypeConstant.VARCHAR)) {
            maxlength = "256";
        } else if (type.equals(FieldTypeConstant.TEXT)) {
            maxlength = "999";
        } else if (type.equals(FieldTypeConstant.LONG_TEXT)) {
            maxlength = "9999";
        }
        return maxlength;
    }
}
