package com.wimoor.sys.gc.service;


import java.util.Map;

import com.wimoor.sys.gc.model.dto.GenerateDto;


/**
 *  代码生成调用层service
 * @version 1.0.0
 */
public interface GenerationSevice {


    /**
     * 生成预览（生成 服务端+layui+vue代码预览）
     *
     * @param generateDto generateDto
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @version 1.0.0
     */
    Map<String, String> preview(GenerateDto generateDto);


    /**
     * 生成代码(服务端代码，直接生成到对应目录,注意覆盖问题)
     *
     * @param generateDto generateDto
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @version 1.0.0
     */
    Map<String, String> generateCode(GenerateDto generateDto);


    /**
     * 生成 vue 代码(直接下载)
     *
     * @param generateDto generateDto
     * @return void
     * @version 1.0.0
     */
    void generateCodeVue(GenerateDto generateDto);


    /**
     * 生成 java + vue 代码(直接下载)
     *
     * @param generateDto
     */
    void generateCodeJavaAndVue(GenerateDto generateDto);

    /**
     * 获取生成路径（服务端+前端）
     *
     * @param dataSourceId 数据源配置id
     * @param tableName tableName
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @version 1.0.0
     */
    Map<String, String> getPath(String tableName, String dataSourceId);

}
