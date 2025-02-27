package com.wimoor.sys.gc.service.gc.gcimpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.constant.TpParamConstant;
import com.wimoor.sys.gc.model.po.DbFieldPO;
import com.wimoor.sys.gc.service.gc.GcSevice;
import com.wimoor.sys.gc.util.GcFileUtil;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("all")
@Component
@Slf4j
public class GcVO extends BaseGcImpl implements GcSevice {

    /**
     * 模板key
     */
    public static final String KEY_NAME = "X-VO";


    @Override
    public void run(GcConfig gcConfig){
          log.info("开始生成: {}", KEY_NAME);
        List<DbFieldPO> dbFields = gcConfig.getDbFields();
        //数据拼接(所有字段)
        this.generateParameters(gcConfig,dbFields);
        // 开始生成文件并进行数据替换
        GcFileUtil.replacBrBwWritee(gcConfig, GcFileUtil.getBrBwPath(gcConfig, KEY_NAME));

    }


    private void generateParameters(GcConfig gcConfig,List<DbFieldPO> data) {
        //数据拼接(所有字段)
        StringBuffer fields = new StringBuffer();
        int position = 0;
        for (DbFieldPO fieldMap : data) {
            // 未勾选的字段过滤
            Object checked = fieldMap.getChecked();      // 兼容layui
            Object isChecked = fieldMap.getIsChecked();  // 兼容vue
            if (checked !=null && !Boolean.parseBoolean(checked.toString())) {
                continue;
            }
            if (isChecked !=null && !Boolean.parseBoolean(isChecked.toString())) {
                continue;
            }
            String type = fieldMap.getType();
            String desc = fieldMap.getDesc();
            String fieldName =fieldMap.getName();
            desc = super.removeDescTheNewlineCharacter(desc,fieldName);
            String typeDetail = fieldMap.getTypeDetail();
            // 1、生成注释
            Boolean entitySwagger = Boolean.valueOf(gcConfig.getDefaultTemplateParam(TpParamConstant.ENTITY_SWAGGER));
            if (entitySwagger) {
                // 字段注释信息-->  Swagger2 模式
                fields.append("\r\n    @ApiModelProperty(value = \"" + desc + "\" ,position = " + position++ + ")");
            } else {
                // 字段注释信息-->  doc 注释
                fields.append("\r\n    /** \r\n     * " + desc + " \r\n     */");
            }
            // 3、生成字段
            fields.append("\r\n    " + super.jxModel( gcConfig,fieldName, type,false) + "\r\n");
        }

        gcConfig.setTemplateParam("entitys",fields.toString());
        gcConfig.setTemplateParam("serialVersionUID", IdUtil.getSnowflakeNextIdStr());
    }
}
