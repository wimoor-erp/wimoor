package com.wimoor.sys.gc.service.gc.gcimpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wimoor.common.service.impl.BaseServiceImpl;
import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.constant.FieldTypeConstant;
import com.wimoor.sys.gc.model.po.DbFieldPO;
import com.wimoor.sys.gc.service.gc.GcSevice;
import com.wimoor.sys.gc.util.GcDataUtil;
import com.wimoor.sys.gc.util.GcFileUtil;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("all")
@Component
@Slf4j
public class GcIServiceImpl implements GcSevice {

    /**
     * 模板key
     */
    public static final String KEY_NAME = "X-ServiceImpl";
    /**
     * 生成查询时将下方定义的字段名修改为使用 eq 查询的字段 (只针对字符串类型的字段, 原 likeRight -> eq)
     */
    public static final List<String> EQ_FIELD = CollUtil.newArrayList("id", "pid");

    /**
     * 生成ServiceImpl
     *
     * @param data           数据
     * @param GenerateConfig 数据
     * @param path           生成代码路径
     * @return void
     * @date 2019/11/20 19:18
     */
    @Override
    public void run(GcConfig gcConfig) {
        log.info("开始生成: {}", KEY_NAME);
        List<DbFieldPO> dbFields = gcConfig.getDbFields();
        this.generateParameters(gcConfig, dbFields);
        // 开始生成文件并进行数据替换
        GcFileUtil.replacBrBwWritee(gcConfig, GcFileUtil.getBrBwPath(gcConfig, KEY_NAME));
    }


    /**
     * mybatis-plus 搜索条件处理
     *
     * @author wangsong
     * @email 1720696548@qq.com
     * @date 2022/6/30 9:50
     */
    private void generateParameters(GcConfig gcConfig, List<DbFieldPO> data) {
        // MybatisPlus搜索条件数据拼接
        StringBuffer findPageMybatisPlus = new StringBuffer("");

        // 富文本字段 查询时排除该字段
        StringBuffer excludeReturn = new StringBuffer("");
        // 处理参数
        for (DbFieldPO fieldMap : data) {
            String fieldName = fieldMap.getName();
            String type = fieldMap.getType();
            String desc = fieldMap.getDesc();
            Boolean isSearch = fieldMap.getIsSearch() == null ? false : fieldMap.getIsSearch();
            Integer vueFieldType = fieldMap.getVueFieldType();
            String tableNameUp = gcConfig.getDefaultTemplateParam("tableNameUp");

            // 指定类型字段不生成到列表中，同时排除list接口查询
            List<String> vueFieldTypeArray = JSON.parseObject(gcConfig.getDefaultTemplateParam("vueFieldTypesArray"), List.class);
            if (vueFieldTypeArray.contains(vueFieldType + "")) {
                if (excludeReturn.toString().equals("")) {
                    excludeReturn.append("        ");
                    excludeReturn.append("queryWrapper.select(" + tableNameUp + ".class, info -> !\"" + fieldName + "\".equals(info.getColumn())");
                } else {
                    excludeReturn.append("\r\n");
                    excludeReturn.append("                 ");
                    excludeReturn.append(" && !\"" + fieldName + "\".equals(info.getColumn())");
                }
            }

            if (!isSearch) {
                continue;
            }
            // 字段映射成驼峰模式,在首字母大写
            fieldName = GcDataUtil.getFieldName(gcConfig, fieldName);
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);


            //字段  // !=null StringUtils.isNotBlank(account)
            findPageMybatisPlus.append("        ");
            if (type.equals(FieldTypeConstant.INT) || type.equals(FieldTypeConstant.BIGINT)
                    || type.equals(FieldTypeConstant.TINYINT)) {
                /**
                 * 整数 int/long/tinyint
                 */
                findPageMybatisPlus.append("queryWrapper.eq(query.get" + fieldName + "() != null, " + tableNameUp + "::get" + fieldName + ", query.get" + fieldName + "());");
            } else if (type.equals(FieldTypeConstant.DOUBLE) || type.equals(FieldTypeConstant.FLOAT)
                    || type.equals(FieldTypeConstant.DECIMAL)) {
                /**
                 * 单精度小数 Float / 双精度小数 Double / decimal等
                 */
                findPageMybatisPlus.append("queryWrapper.eq(query.get" + fieldName + "() != null, " + tableNameUp + "::get" + fieldName + ", query.get" + fieldName + "());");
            } else if (type.equals(FieldTypeConstant.VARCHAR) || type.equals(FieldTypeConstant.TEXT)
                    || type.equals(FieldTypeConstant.CHAR) || type.equals(FieldTypeConstant.LONG_TEXT
            )) {
                /**
                 * 字符串 / 大文本、超大文本
                 */
                if (EQ_FIELD.contains(fieldMap.getName())) {
                    findPageMybatisPlus.append("queryWrapper.eq(StringUtils.isNotBlank(query.get" + fieldName + "()), " + tableNameUp + "::get" + fieldName + ", query.get" + fieldName + "());");
                } else {
                    findPageMybatisPlus.append("queryWrapper.likeRight(StringUtils.isNotBlank(query.get" + fieldName + "()), " + tableNameUp + "::get" + fieldName + ", query.get" + fieldName + "());");
                }
            } else if (type.equals(FieldTypeConstant.DATETIME) || type.equals(FieldTypeConstant.TIME)
                    || type.equals(FieldTypeConstant.TIMESTAMP) || type.equals(FieldTypeConstant.DATE)) {
                /**
                 * 时间
                 */
                if (type.equals(FieldTypeConstant.DATETIME) || type.equals(FieldTypeConstant.DATE)) {
                    String hql = "if (StringUtils.isNotBlank(query.get{prop}()) && query.get{prop}().split(SymbolConst.COMMA).length >= 1) {\n" +
                            "            queryWrapper.between({tableNameUp}::get{prop}, query.get{prop}().split(\",\")[0], query.get{prop}().split(\",\")[1]);\n" +
                            "        }";
                    findPageMybatisPlus.append(hql.replaceAll("\\{prop}", fieldName).replaceAll("\\{tableNameUp}", tableNameUp));
                } else {
                    findPageMybatisPlus.append("queryWrapper.eq(query.get" + fieldName + "() != null, " + tableNameUp + "::get" + fieldName + ", query.get" + fieldName + "());");
                }
            }
            findPageMybatisPlus.append("\r\n");
        }
        // 增加排除字段的最后一个括号，添加到填充内容中
        excludeReturn = excludeReturn.toString().equals("") ? excludeReturn : excludeReturn.append(");");
        String excludeReturnStr = excludeReturn.toString().equals("") ? "" : excludeReturn.toString() + "\r\n";
        // 去掉搜索条件的最后一个 \r\n
        String findPageMybatisPlusStr = findPageMybatisPlus.toString().equals("") ?
                "" : findPageMybatisPlus.toString().substring(0, findPageMybatisPlus.toString().length() - 2);

        gcConfig.setTemplateParam("findPageMybatisPlus", excludeReturnStr + findPageMybatisPlusStr);
    }
}
