package com.wimoor.sys.gc.config;



import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wimoor.sys.gc.config.model.GcFilePath;
import com.wimoor.sys.gc.model.po.DbFieldPO;
import com.wimoor.sys.gc.model.vo.DatasourceVO;


/**
 * 所有配置的集合
 */
@Data
public class GcConfig {

    /**
     * 是否生成 数结构 页面/代码
     */
    private Boolean isTree;

    /**
     * 表字段的相关数据(前端传递)
     */
    private List<DbFieldPO> dbFields;

    /**
     * 选择的数据源 (为空表示未当前服务连接的数据源)
     */
    private DatasourceVO dbDatasource;
    /**
     * 默认模板参数
     */
    private Map<String, String> defaultTemplateParam = new ConcurrentHashMap<>();
    /**
     * 动态模板参数（各个生成的实现对象动态加入的参数）
     */
    private Map<String, String> templateParam = new ConcurrentHashMap<>();
    /**
     * 模板生成位置
     */
    private Map<String, GcFilePath> templatePathMap = new HashMap<>();
    /**
     * 模板生成后的访问链接（代码生成后，自动填充该数据）
     */
    private Map<String, String> visitPathMap = new LinkedHashMap<>();


    /**
     * 添加文件生成 模板配置
     *
     * @param name
     * @param templatePath
     * @param path
     * @return
     */
    public GcConfig addTemplate(String name, String templatePath, String path) {
        GcFilePath gcFilePath = new GcFilePath();
        gcFilePath.setName(name);
        gcFilePath.setTemplatePath(templatePath);
        gcFilePath.setPath(path);
        templatePathMap.put(name, gcFilePath);
        return this;
    }

    /**
     * 模板生成后的 访问链接
     *
     * @param name key名
     * @param path 访问地址
     * @return
     */
    public void addVisitPath(String name, String path) {
        visitPathMap.put(name, path);
    }


    /**
     * 添加默认模板参数
     *
     * @param key key名
     * @param key 访问地址
     * @return
     */
    public void setDefaultTemplateParam(String key, String value) {
        value = value == null ? "" : value;
        defaultTemplateParam.put("{" + key + "}", value);
    }

    /**
     * 添加模板参数
     *
     * @param key
     */
    public String getDefaultTemplateParam(String key) {
        return defaultTemplateParam.get("{" + key + "}");
    }

    /**
     * 添加模板参数
     *
     * @param key
     * @param value
     */
    public void setTemplateParam(String key, String value) {
        value = value == null ? "" : value;
        templateParam.put("{" + key + "}", value);
    }


}
