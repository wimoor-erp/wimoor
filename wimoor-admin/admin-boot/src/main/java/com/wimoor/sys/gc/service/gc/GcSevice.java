package com.wimoor.sys.gc.service.gc;

import com.wimoor.sys.gc.config.GcConfig;

/**
  * 代码生成执行器
  */
@SuppressWarnings("all")
public interface GcSevice {

    /***
     * 执行方法
     * @param data  数据
     * @param path  生成代码路径 模板位置
     * @param path  生成代码路径
     */
    public void run(GcConfig gcConfig);

}
