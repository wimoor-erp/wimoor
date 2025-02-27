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

/**
 * mapperXml 生成
 * @author wangsong
 * @mail 1720696548@qq.com
 * @date 2021/7/13 0013 11:52
 * @version 1.0.1
 */
@SuppressWarnings("all")
@Component
@Slf4j
public class GcMapperXml  implements GcSevice {

    /**
     * 模板key
     */
    public static final String KEY_NAME = "X-MapperXml";

    /**
     * 生成查询时将下方定义的字段名修改为使用 eq 查询的字段 (只针对字符串类型的字段, 原 likeRight -> eq)
     */
    public static final List<String> EQ_FIELD = CollUtil.newArrayList("id", "pid");

    /**
     * 生成Dao 对应的xml
     *
     * @param data    数据
     * @param GenerateConfig 数据
     * @param path    生成代码路径
     * @return void
     * @date 2019/11/20 19:18
     */
    @Override
    public void run(GcConfig gcConfig) {
          log.info("开始生成: {}", KEY_NAME);
        List<DbFieldPO> dbFields = gcConfig.getDbFields();
        gcConfig.setTemplateParam("resultMap", resultXml(gcConfig, dbFields));
        gcConfig.setTemplateParam("columnList", columnXml(dbFields));
        gcConfig.setTemplateParam("selectSearchList", selectSearchXml(gcConfig, dbFields));
        gcConfig.setTemplateParam("xmlInsert", insertXml(gcConfig, dbFields));
        gcConfig.setTemplateParam("xmlUpd", updateXml(gcConfig, dbFields));
        // 开始生成文件并进行数据替换
        GcFileUtil.replacBrBwWritee(gcConfig, GcFileUtil.getBrBwPath(gcConfig, KEY_NAME));
    }


    /**
     * 生成动态搜索条件的 sql
     *
     * @return java.lang.String
     * @author wangsong
     * @date 2021/7/13 0013 11:26
     * @version 1.0.1
     */
    private String selectSearchXml(GcConfig gcConfig, List<DbFieldPO> dbFields) {
        StringBuffer selectSearcListSb = new StringBuffer("");
        // 拼接字段 key
        for (int i = 0; i < dbFields.size(); i++) {
            DbFieldPO fieldMap = dbFields.get(i);
            String type = fieldMap.getType();
            Boolean isSearch = fieldMap.getIsSearch() == null ? false : fieldMap.getIsSearch();
            if (!isSearch) {
                continue;
            }

            // 数据库字段
            String fieldName = fieldMap.getName();
            // 字段名驼峰
            String fieldNameHump = GcDataUtil.getFieldName(gcConfig, fieldName);
            List<String> keywordArray = JSON.parseObject(gcConfig.getDefaultTemplateParam("keywordArray"), List.class);
            // mysql关键字处理
            if (keywordArray.contains(fieldName)) {
                fieldName = "`" + fieldName + "`";
            }

            // 拼接--动态添加sql
            // 字段判空
            if (type.equals(FieldTypeConstant.VARCHAR) || type.equals(FieldTypeConstant.TEXT)
                    || type.equals(FieldTypeConstant.CHAR) || type.equals(FieldTypeConstant.LONG_TEXT
            )) {
                // 字符串默认右模糊
                selectSearcListSb.append("\r\n        <if test=\"query." + fieldNameHump + " != null and query." + fieldNameHump + "  != ''\">");
                if (EQ_FIELD.contains(fieldMap.getName())) {
                    selectSearcListSb.append("\r\n            and t." + fieldName + " = #{query." + fieldNameHump + "}");
                }else{
                    selectSearcListSb.append("\r\n            and t." + fieldName + " like concat(#{query." + fieldNameHump + "},'%')");
                }
                selectSearcListSb.append("\r\n        </if>");
            } else if (type.equals(FieldTypeConstant.DATETIME)) {
                // 时间默认时间范围搜索(传入字符串分割的开始时间+结束时间)
                selectSearcListSb.append("\r\n        <if test=\"query." + fieldNameHump + " != null and query." + fieldNameHump + " != ''\">" +
                        "\r\n            <foreach item=\"" + fieldNameHump + "\" collection=\"query." + fieldNameHump + ".split(',')\" open=\"and t." + fieldName + " >= \" separator=\" and \" close=\" >= t." + fieldName + "\">" +
                        "\r\n                #{" + fieldNameHump + "}" +
                        "\r\n            </foreach>" +
                        "\r\n        </if>");
            } else {
                // 其他默认eq
                selectSearcListSb.append("\r\n        <if test=\"query." + fieldNameHump + " != null\">");
                selectSearcListSb.append("\r\n            and t." + fieldName + " = #{query." + fieldNameHump + "}");
                selectSearcListSb.append("\r\n        </if>");
            }
        }
        return selectSearcListSb.toString();
    }


    /**
     * 生成动态添加sql
     * @author wangsong
     * @date 2021/7/13 0013 11:26
     * @return java.lang.String
     * @version 1.0.1
     */
    private String insertXml(GcConfig gcConfig, List<DbFieldPO> data) {

        String tableName = gcConfig.getDefaultTemplateParam("tableName");

        StringBuffer insertList = new StringBuffer("            insert into " + tableName + "(");
        insertList.append("\r\n            <trim suffixOverrides=\",\">");
        // 拼接字段 key
        for (int i = 0; i < data.size(); i++) {
            DbFieldPO fieldMap = data.get(i);
            String type = fieldMap.getType();                                               // 字段类型
            String fieldNameHump = GcDataUtil.getFieldName(gcConfig, fieldMap.getName());  // 字段名驼峰
            // 拼接--动态添加sql
            if (type.equals("varchar") || type.equals("text")) {
                insertList.append("\r\n                <if test=\"" + fieldNameHump + " != null and " + fieldNameHump + "  != ''\">");
            } else {
                insertList.append("\r\n                <if test=\"" + fieldNameHump + " != null\">");
            }
            // 字段名(第一个不拼接逗号)
            insertList.append("\r\n                    `" + fieldMap.getName() + "`,");
            insertList.append("\r\n               </if>");
        }
        insertList.append("\r\n            </trim>");

        // 拼接字段值
        insertList.append("\r\n            )values(");
        insertList.append("\r\n            <trim suffixOverrides=\",\">");
        for (int i = 0; i < data.size(); i++) {
            DbFieldPO fieldMap = data.get(i);
            // 字段类型日
            String type = fieldMap.getType();
            String fieldNameHump = GcDataUtil.getFieldName(gcConfig, fieldMap.getName());
            // 拼接--动态添加sql
            if (type.equals("varchar") || type.equals("text")) {
                insertList.append("\r\n                <if test=\"" + fieldNameHump + " != null and " + fieldNameHump + " != ''\">");
            } else {
                insertList.append("\r\n                <if test=\"" + fieldNameHump + " != null\">");
            }
            // 字段值(第一个不拼接逗号)
            insertList.append("\r\n                    #{" + fieldNameHump + "},");
            insertList.append("\r\n                </if>");
        }
        insertList.append("\r\n           </trim>");
        // 加最后的括号
        insertList.append("\r\n           )");
        return insertList.toString();
    }


    /**
     * 生成编辑sql
     * @author wangsong
     * @date 2021/7/13 0013 11:26
     * @return java.lang.String
     * @version 1.0.1
     */
    private String updateXml(GcConfig gcConfig, List<DbFieldPO> data) {
        String tableName = gcConfig.getDefaultTemplateParam("tableName");
        StringBuffer updateList = new StringBuffer("            update " + tableName);
        updateList.append("\r\n            <set>");
        for (int i = 0; i < data.size(); i++) {
            DbFieldPO fieldMap = data.get(i);
            // 字段名
            String fieldName = fieldMap.getName();
            // 驼峰字段名
            String fieldNameHump = GcDataUtil.getFieldName(gcConfig, fieldName);
            // 字段类型日
            String type = fieldMap.getType();
            if (!fieldName.toLowerCase().equals("id")) {
                // 拼接--动态编辑sql
                if (type.equals("varchar") || type.equals("text")) {
                    updateList.append("\r\n                <if test=\"" + fieldNameHump + " != null and " + fieldNameHump + " != ''\">");
                } else {
                    updateList.append("\r\n                <if test=\"" + fieldNameHump + " != null\">");
                }
                // 字段值( <set> 可以不处理最后的逗号 )
                updateList.append("\r\n                    `" + fieldName + "` =  #{" + fieldNameHump + "},");
                updateList.append("\r\n                </if>");
            }
        }
        updateList.append("\r\n            </set>");
        updateList.append("\r\n            where id = #{id}");
        return updateList.toString();
    }


    /**
     * 生成 xml和实体类 映射关系数据
     * @author wangsong
     * @date 2021/7/13 0013 11:26
     * @return java.lang.String
     * @version 1.0.1
     */
    private String resultXml(GcConfig gcConfig, List<DbFieldPO> data) {
        String tableName = gcConfig.getDefaultTemplateParam("tableName");

        StringBuffer resultMap = new StringBuffer();
        for (DbFieldPO fieldMap : data) {
            //字段名
            String fieldName = fieldMap.getName();
            //驼峰字段名
            String fieldNameHump = GcDataUtil.getFieldName(gcConfig, fieldName);
            if ("id".equals(fieldName)) {
                resultMap.append("\r\n        <id column=\"" + fieldName + "\" property=\"" + fieldNameHump + "\"/>");
            } else {
                resultMap.append("\r\n        <result column=\"" + fieldName + "\" property=\"" + fieldNameHump + "\"/>");
            }
        }
        return resultMap.toString();
    }


    /**
     * 生成通用返回数据
     * @author wangsong
     * @date 2021/7/13 0013 11:26
     * @return java.lang.String
     * @version 1.0.1
     */
    private String columnXml(List<DbFieldPO> data) {
        StringBuffer columnList = new StringBuffer();
        for (DbFieldPO fieldMap : data) {
            // 字段名
            String fieldName = fieldMap.getName();
            columnList.append("\r\n        t." + fieldName + ",");
        }
        return columnList.toString().substring(0, columnList.toString().length() - 1);
    }
}
