package com.wimoor.common.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 字段自动填充
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增填充创建时间
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtCreate", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictUpdateFill(metaObject, "gmtModified", () -> LocalDateTime.now(), LocalDateTime.class);
        
        this.strictInsertFill(metaObject, "createtime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "createdate", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "opttime", () -> new Date(),Date.class);
    }

    /**
     * 更新填充更新时间
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "gmtModified", () -> LocalDateTime.now(), LocalDateTime.class);
        
        this.strictUpdateFill(metaObject, "opttime", () ->  new Date(),Date.class);
    }

}
