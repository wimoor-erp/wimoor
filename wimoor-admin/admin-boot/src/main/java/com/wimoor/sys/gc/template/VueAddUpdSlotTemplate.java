package com.wimoor.sys.gc.template;

/**
 * vue 插槽组件定义
 * @author wangsong
 * @mail 1720696548@qq.com
 * @date 2022/5/14 0014 22:32
 * @version 1.0.0
 */
public interface VueAddUpdSlotTemplate {

    /**
     * 富文本  TinymceEditor（vue自定义组件）
     * {prop} 字段
     */
    String TINYMCE_EDITOR = "            <template slot-scope=\"{row}\" slot=\"{prop}\">\n" +
            "                <TinymceEditor v-if=\"initSuccess\" :content.sync=\"obj.{prop}\"/>\n" +
            "            </template>\n";


    /**
     * md 编辑器  TinymceEditor（vue自定义组件）
     * {prop} 字段
     */
    String MD_EDITOR = "            <template slot-scope=\"{row}\" slot=\"{prop}\">\n" +
            "                <MdEditor v-if=\"initSuccess\" :content.sync=\"obj.{prop}\"/>\n" +
            "            </template>\n";


}
