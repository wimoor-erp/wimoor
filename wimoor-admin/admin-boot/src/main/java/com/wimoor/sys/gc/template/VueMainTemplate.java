package com.wimoor.sys.gc.template;

/**
 * @author wangsong
 * 复选Html 模板配置
 */
public interface VueMainTemplate {


    /**
     * 文本  {label}  {prop} {search} {searchSpan}
     */
    String TEXT = "                {\n" +
            "                    label: '{label}',\n" +
            "                    prop: '{prop}',\n" +
            "                    search: {search},\n" +
            "                    searchSpan: {searchSpan},\n" +
            "                    overHidden: true,\n" +
            "                },\n";

    /**
     * 字典 (单选) {label}  {prop} {search}
     */
    String TEXT_DICT = "                {\n" +
            "                    label: '{label}',\n" +
            "                    prop: '{prop}',\n" +
            "                    type: 'select',\n" +
            "                    search: {search},\n" +
            "                    searchSpan: 5,\n" +
            "                    filterable:true,\n" +
            "                    overHidden: true,\n" +
            "                    dicData: this.dict.get({dictCode}),\n" +
            "                },\n";


    /**
     * 字典（多选） {label}  {prop} {search}
     */
    String TEXT_DICT_CHECKBOX = "                {\n" +
            "                    label: '{label}',\n" +
            "                    prop: '{prop}',\n" +
            "                    type: 'select',\n" +
            "                    search: {search},\n" +
            "                    searchSpan: 5,\n" +
            "                    overHidden: true,\n" +
            "                    dataType: 'string',\n" +
            "                    dicData: this.dict.get({dictCode}),\n" +
            "                },\n";


    /**
     * 时间-小时选择 {label}  {prop} {search}
     */
    String TIME = "                {\n" +
            "                    label: '{label}',\n" +
            "                    prop: '{prop}',\n" +
            "                    search: {search},\n" +
            "                    searchSpan: 5,\n" +
            "                    overHidden: true,\n" +
            "                    type: \"time\",\n" +
            "                    pickerOptions:{\n" +
            "                        start: '06:00',\n" +
            "                        step: '00:30',\n" +
            "                        end: '23:00'\n" +
            "                    }\n" +
            "                },\n";


    /**
     * 图片,支持多图/单图） {label}  {prop}
     */
    String IMG = "                {\n" +
            "                    label: '{label} ',\n" +
            "                    prop: '{prop}',\n" +
            "                    search: false,\n" +
            "                    overHidden: true,\n" +
            "                    html: true,\n" +
            "                    formatter: (val) => {\n" +
            "                        if(val.{prop} == null || val.{prop} == ''){\n" +
            "                            return \"\";\n" +
            "                        }else{\n" +
            "                            let imgs = val.{prop}.split(\",\");\n" +
            "                            let html = \"\"; \n" +
            "                            imgs.forEach(item => html += \"<img src='\" + item + \"'  style='border-radius: 40px;height: 40px;width: 40px;margin-top: 10px'>\")\n" +
            "                            return html;\n" +
            "                        }\n" +
            "                    }\n" +
            "                },\n";


    /**
     * 图标
     * {label}  {prop}
     */
    String ICON = "                {\n" +
            "                    label: '{label} ',\n" +
            "                    prop: '{prop}',\n" +
            "                    html: true,\n" +
            "                    formatter: (val) => {\n" +
            "                        return '<i class=' + val.{prop} + '></i>'\n" +
            "                    }\n" +
            "                },\n";

    /**
     * 颜色
     * {label}  {prop}
     */
    String COLOR = "                {\n" +
            "                    label: '{label}',\n" +
            "                    prop: '{prop}',\n" +
            "                    type: 'color',\n" +
            "                    overHidden: true,\n" +
            "                    colorFormat: \"hex\",\n" +
            "                    showAlpha: false\n" +
            "                },\n";

    /**
     * 地图
     * {label}  {prop}
     */
    String MAP = "                {\n" +
            "                    label: '{label}',\n" +
            "                    prop: '{prop}',\n" +
            "                    overHidden: true,\n" +
            "                    html: true,\n" +
            "                    formatter: (val) => {\n" +
            "                        if (val.{prop} != null && val.{prop}.split(',').length >= 3) {\n" +
            "                            return val.{prop}.split(',')[2];\n" +
            "                        }\n" +
            "                        return ''\n" +
            "                    }\n" +
            "                },\n";


    /**
     * 级联选择器的搜索   {label}  {prop} {search}
     */
    String CASCADER = "                {\n" +
            "                    label: '{label}',\n" +
            "                    prop: '{prop}',\n" +
            "                    search: {search},\n" +
            "                    searchSpan: 5,\n" +
            "                    type: 'cascader',\n" +
            "                    dataType: 'string',\n" +
            "                    overHidden: true,\n" +
            "                    filterable: true, \n" +
            "                    // 在 mounted 事件使用 let res = await this.crud.get() 同步获取数据替换字典数据 \n" +
            "                    dicData: this.defaultdata.dicData, \n" +
            "                    props: {\n" +
            "                        value: \"id\",\n" +
            "                        label: \"name\",\n" +
            "                        children: \"children\"\n" +
            "                    }\n" +
            "                },\n";
}
