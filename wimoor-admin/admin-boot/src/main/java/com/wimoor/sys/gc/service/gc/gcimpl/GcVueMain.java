package com.wimoor.sys.gc.service.gc.gcimpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.constant.Base;
import com.wimoor.sys.gc.constant.TpParamConstant;
import com.wimoor.sys.gc.model.po.DbFieldPO;
import com.wimoor.sys.gc.service.gc.GcSevice;
import com.wimoor.sys.gc.template.VueMainTemplate;
import com.wimoor.sys.gc.util.GcDataUtil;
import com.wimoor.sys.gc.util.GcFileUtil;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("all")
@Component
@Slf4j
public class GcVueMain extends BaseGcImpl implements GcSevice {

    /**
     * 模板key
     */
    public static final String KEY_NAME = "X-Vue";


    /**
     * 生成Html-main 主页
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
        // 数据表格字段
        StringBuffer vueInfoColumns = new StringBuffer(" ");
        StringBuffer vueInfoColumnSlots = new StringBuffer(" ");
        List<DbFieldPO> dbFields = gcConfig.getDbFields();
        for (DbFieldPO dbFieldPO : dbFields) {
            // 未勾选的字段过滤
            if (!isChecked(dbFieldPO)) {
                continue;
            }
            Object vueFieldType = dbFieldPO.getVueFieldType();

            // 指定类型字段不生成到列表中，同时排除list接口查询
            List<Integer> vueFieldTypeArray = JSON.parseObject(gcConfig.getDefaultTemplateParam(TpParamConstant.VUE_FIELD_TYPES_ARRAY), List.class);
            if (vueFieldTypeArray.contains(vueFieldType + "")) {
                continue;
            }

            String name = GcDataUtil.getFieldName(gcConfig, dbFieldPO.getName());
            String newDesc = super.getDesc(dbFieldPO.getDesc(), dbFieldPO.getName());
            // 判断是否需要生成查询
            boolean isSearch = dbFieldPO.getIsSearch() == null ? false : dbFieldPO.getIsSearch();

            Integer vueFieldTypeInt = (vueFieldType != null) ? Integer.parseInt(vueFieldType.toString()) : -1;
            if (vueFieldTypeInt.equals(Base.VueFieldType.V4.getValue())
                    || vueFieldTypeInt.equals(Base.VueFieldType.V6.getValue())
                    || vueFieldTypeInt.equals(Base.VueFieldType.V7.getValue())
                    || vueFieldTypeInt.equals(Base.VueFieldType.V9.getValue())) {
                // 字典 - 单选
                vueInfoColumns.append(VueMainTemplate.TEXT_DICT
                        .replaceAll("\\{label}", newDesc)
                        .replace("{prop}", name)
                        .replace("{search}", isSearch + "")
                        .replace("{dictCode}", getDictCode(dbFieldPO.getDictCode()))
                );
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V5.getValue())
                    || vueFieldTypeInt.equals(Base.VueFieldType.V8.getValue())) {
                // 字典 - 多选
                vueInfoColumns.append(VueMainTemplate.TEXT_DICT_CHECKBOX
                        .replaceAll("\\{label}", newDesc).replace("{prop}", name)
                        .replace("{search}", isSearch + "").replace("{dictCode}", getDictCode(dbFieldPO.getDictCode())));
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V11.getValue())) {
                // 时间
                vueInfoColumns.append(VueMainTemplate.TEXT.replaceAll("\\{label}", newDesc).replace("{prop}", name)
                        .replace("{searchSpan}", "7").replace("{search}", isSearch + ""));
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V12.getValue())) {
                // 时间-小时选择
                vueInfoColumns.append(VueMainTemplate.TIME.replaceAll("\\{label}", newDesc)
                        .replace("{prop}", name).replace("{search}", isSearch + ""));
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V13.getValue()) || vueFieldTypeInt.equals(Base.VueFieldType.V14.getValue())) {
                // 图片
                vueInfoColumns.append(VueMainTemplate.IMG.replaceAll("\\{label}", newDesc).replace("{prop}", name));
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V20.getValue())) {
                // 级联选择器
                vueInfoColumns.append(VueMainTemplate.CASCADER.replace("{label}", newDesc).replace("{prop}", name)
                        .replace("{search}", isSearch + "")
                );
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V22.getValue())) {
                // 图标选择器
                vueInfoColumns.append(VueMainTemplate.ICON.replace("{label}", newDesc).replaceAll("\\{prop}", name));
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V23.getValue())) {
                // 颜色选择器
                vueInfoColumns.append(VueMainTemplate.COLOR.replace("{label}", newDesc).replace("{prop}", name));
            } else if (vueFieldTypeInt.equals(Base.VueFieldType.V24.getValue())) {
                // 地图选择器
                vueInfoColumns.append(VueMainTemplate.MAP.replace("{label}", newDesc).replaceAll("\\{prop}", name));
            } else {
                // 默认普通文本
                vueInfoColumns.append(VueMainTemplate.TEXT.replaceAll("\\{label}", newDesc).replace("{prop}", name)
                        .replace("{searchSpan}", "5").replace("{search}", isSearch + ""));
            }
            // 插槽字段处理(搜索或特殊展示)
            vueInfoColumnSlots.append(super.jxVueInfoColumnsSlot(vueFieldTypeInt, name, isSearch));
        }

        // 数据保存
        gcConfig.setTemplateParam("vueInfoColumns", vueInfoColumns.toString());
        gcConfig.setTemplateParam("vueInfoColumnSlots", vueInfoColumnSlots.toString());
        // 开始生成文件并进行数据替换
        GcFileUtil.replacBrBwWritee(gcConfig, GcFileUtil.getBrBwPath(gcConfig, KEY_NAME));
    }
}
