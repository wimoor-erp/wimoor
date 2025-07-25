package com.wimoor.sys.gc.config.model;

import lombok.Data;

/**
 * 代码生成位置/ 模板位置存储对象
 */
@Data
public class GcFilePath {
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板位置
     */
    private String templatePath;
    /**
     * 生成文件位置
     */
    private String path;
}