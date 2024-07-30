package com.wimoor.sys.gc.service.gc.gcimpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.model.po.DbFieldPO;
import com.wimoor.sys.gc.service.gc.GcSevice;
import com.wimoor.sys.gc.util.GcDataUtil;
import com.wimoor.sys.gc.util.GcFileUtil;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("all")
@Component
@Slf4j
public class GcVueUpd extends BaseGcImpl implements GcSevice {

    /**
     * 模板key
     */
    public static final String KEY_NAME = "X-VueUpd";


    /**
     * 生成Html-Upd 修改页
     *
     * @param data    数据
     * @param GenerateConfig 数据
     * @param path    生成代码路径
     * @return void
     * @date 2019/11/20 19:18
     */
    @Override
    public void run(GcConfig gcConfig) {
        log.info("开始生成:{}", KEY_NAME);
        StringBuffer vueUpdColumns = new StringBuffer("");
        StringBuffer vueAddColumnSlots = new StringBuffer("");
        List<DbFieldPO> dbFields = gcConfig.getDbFields();
        for (DbFieldPO dbField : dbFields) {
            // 未勾选的字段过滤
            if (!isChecked(dbField)) {
                continue;
            }
            // 生成字段
            vueUpdColumns.append(super.jxVueColumns(
                    gcConfig,
                    dbField.getName(),
                    dbField.getType(),
                    dbField.getTypeDetail(),
                    getDesc(dbField.getDesc(), dbField.getName()),
                    dbField.getVueFieldType(),
                    dbField.getDictCode(),
                    dbField.getIsNull()
            ));

            // 生成字段 插槽
            String name = GcDataUtil.getFieldName(gcConfig, dbField.getName());
            vueAddColumnSlots.append(super.jxVueColumnsSlot( dbField.getVueFieldType(), name));
        }
        // 数据保存
        gcConfig.setTemplateParam("vueUpdColumns", vueUpdColumns.toString());
        gcConfig.setTemplateParam("vueUpdColumnSlots", vueAddColumnSlots.toString());
        // 开始生成文件并进行数据替换
        GcFileUtil.replacBrBwWritee(gcConfig, GcFileUtil.getBrBwPath(gcConfig, KEY_NAME));
    }
}
