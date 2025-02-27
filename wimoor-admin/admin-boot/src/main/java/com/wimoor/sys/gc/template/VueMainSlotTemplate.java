package com.wimoor.sys.gc.template;

/**
 *  @author wangsong
 * 复选Html 模板配置
 */
public interface VueMainSlotTemplate {

    /**
     * 开关插槽 (默认 0-开  1-关)
     *
     *
     */
//    String SWITCH = "            <template slot-scope=\"{row,index,type,size}\" slot=\"{field}\">\n" +
//            "                <el-switch v-model=\"row.{field}\" @change=\"updDisable(row)\"\n" +
//            "                           active-color=\"#13ce66\" inactive-color=\"#ff4949\"\n" +
//            "                           :active-value=0 :inactive-value=1\n" +
//            "                           active-text=\"\" inactive-text=\"\">\n" +
//            "                </el-switch>\n" +
//            "            </template>";


    /**
     * 时间范围选择
     *  {prop}
     * @author wangsong
     * @mail 1720696548@qq.com
     * @date 2022/9/9 0009 0:23
     * @version 1.0.0
     */
    String DATE_PICKER = "           <template slot-scope=\"{row,index,type,size}\" slot=\"{prop}Search\">\n" +
            "                <div class=\"block\">\n" +
            "                    <el-date-picker\n" +
            "                            v-model=\"search.{prop}\"\n" +
            "                            value-format=\"yyyy-MM-dd HH:mm:ss\"\n" +
            "                            type=\"datetimerange\"\n" +
            "                            align=\"right\"\n" +
            "                            unlink-panels\n" +
            "                            range-separator=\"至\"\n" +
            "                            start-placeholder=\"开始日期\"\n" +
            "                            end-placeholder=\"结束日期\"\n" +
            "                            :picker-options=\"defaultdata.timeOptions\">\n" +
            "                    </el-date-picker>\n" +
            "                </div>\n" +
            "            </template>";


}
