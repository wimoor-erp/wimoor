package com.wimoor.sys.gc.service.gc.pattern;

 
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.service.gc.GcSevice;
import com.wimoor.sys.gc.service.gc.gcimpl.GcController;
import com.wimoor.sys.gc.service.gc.gcimpl.GcDTO;
import com.wimoor.sys.gc.service.gc.gcimpl.GcEntity;
import com.wimoor.sys.gc.service.gc.gcimpl.GcIService;
import com.wimoor.sys.gc.service.gc.gcimpl.GcIServiceImpl;
import com.wimoor.sys.gc.service.gc.gcimpl.GcMapper;
import com.wimoor.sys.gc.service.gc.gcimpl.GcMapperXml;
import com.wimoor.sys.gc.service.gc.gcimpl.GcQuery;
import com.wimoor.sys.gc.service.gc.gcimpl.GcVO;
import com.wimoor.sys.gc.service.gc.gcimpl.GcVueAdd;
import com.wimoor.sys.gc.service.gc.gcimpl.GcVueMain;
import com.wimoor.sys.gc.service.gc.gcimpl.GcVuePid;
import com.wimoor.sys.gc.service.gc.gcimpl.GcVueUpd;

/**
 * 执行代码生成，并依次生成文件
 */
@Service
public class GcIteratorPattern {

    /**
     * 每个模板的代码生成实现 bean
     */
    Map<String, GcSevice> beans = null;

    /**
     * 初始化代码生成实现类到容器中
     *
     * @return void
     * @author wangsong
     * @date 2022/8/9 15:32
     */
    public void init() {
        if (beans == null) {
            beans = new LinkedHashMap<>(16);
            beans.put(GcEntity.KEY_NAME, new GcEntity());
            beans.put(GcDTO.KEY_NAME, new GcDTO());
            beans.put(GcVO.KEY_NAME, new GcVO());
            beans.put(GcQuery.KEY_NAME, new GcQuery());
            beans.put(GcController.KEY_NAME, new GcController());
            beans.put(GcIService.KEY_NAME, new GcIService());
            beans.put(GcIServiceImpl.KEY_NAME, new GcIServiceImpl());
            beans.put(GcMapper.KEY_NAME, new GcMapper());
            beans.put(GcMapperXml.KEY_NAME, new GcMapperXml());
            beans.put(GcVueMain.KEY_NAME, new GcVueMain());
            beans.put(GcVueAdd.KEY_NAME, new GcVueAdd());
            beans.put(GcVueUpd.KEY_NAME, new GcVueUpd());
            beans.put(GcVuePid.KEY_NAME, new GcVuePid());
        }

    }


    /**
     * 依次执行代码生成
     */
    public void run(GcConfig gcConfig) {
        this.init();
        for (GcSevice xjGcSevice : beans.values()) {
            xjGcSevice.run(gcConfig);
        }
    }
}
