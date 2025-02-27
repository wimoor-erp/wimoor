package com.wimoor.sys.gc.service.gc.gcimpl;

 
import org.springframework.stereotype.Component;

import com.wimoor.common.service.impl.BaseServiceImpl;
import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.service.gc.GcSevice;
import com.wimoor.sys.gc.util.GcFileUtil;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("all")
@Component
@Slf4j
public class GcIService  implements GcSevice {

    /**
     * 模板key
     */
    public static final String KEY_NAME = "X-Service";

    /**
     * 生成Service层
     *
     * @param data            数据
     * @param this.TABLE_NAME 数据库表名
     * @param path            生成代码路径
     * @return void
     * @date 2019/11/20 19:18
     */
    @Override
    public void run(GcConfig gcConfig){
          log.info("开始生成: {}", KEY_NAME);
        // 开始生成文件并进行数据替换
        GcFileUtil.replacBrBwWritee(gcConfig, GcFileUtil.getBrBwPath(gcConfig, KEY_NAME));
    }
}
