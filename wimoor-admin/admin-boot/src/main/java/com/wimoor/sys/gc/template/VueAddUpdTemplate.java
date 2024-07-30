package com.wimoor.sys.gc.template;

/**
 * @author wangsong
 * 复选Html 模板配置
 */
public interface VueAddUpdTemplate {

    /**
     * 文本 {label}  {prop} {required}
     */
    String INPUT = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            labelWidth: 110,\n" +
            "                            maxlength: {maxlength},\n" +
            "                            showWordLimit: true,\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请输入 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";

    /**
     * 大文本 {label}  {prop} {required}
     */
    String TEXTAREA = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'textarea',\n" +
            "                            labelWidth: 110,\n" +
            "                            maxlength: {maxlength},\n" +
            "                            showWordLimit: true,\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请输入 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";


    /**
     * 数字 {label}  {prop}  {precision}  {maxRows} {required}
     */
    String NUMBER = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'number',\n" +
            "                            labelWidth: 110,\n" +
            "                            precision: {precision},  //保留小数位,\n" +
            "                            minRows: 0,\n" +
            "                            maxRows: {maxRows},\n" +
            "                            row: true,\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请输入 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";

    /**
     * 单选 {label}  {prop} {dictCode} {required}
     */
    String RADIO = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'radio',\n" +
            "                            labelWidth: 110,\n" +
            "                            dicData: this.dict.get({dictCode}),\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";

    /**
     * 多选 {label}  {prop} {dictCode} {required}
     */
    String CHECKBOX = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'checkbox',\n" +
            "                            labelWidth: 110,\n" +
            "                            dataType: 'string', // 字符串模式\n" +
            "                            dicData: this.dict.get({dictCode}),\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";

    /**
     * 下拉选 {label}  {prop} {dictCode} {required}
     */
    String SELECT = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'select',\n" +
            "                            labelWidth: 110,\n" +
            "                            filterable:true,\n" +
            "                            dicData: this.dict.get({dictCode}),\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";

    /**
     * 开关 {label}  {prop} {dictCode} {required}
     */
    String SWITCH = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'switch',\n" +
            "                            labelWidth: 110,\n" +
            "                            dicData: this.dict.get({dictCode}),\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";


    /**
     * 时间日期 {label}  {prop} {required}
     */
    String DATETIME = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'datetime',\n" +
            "                            labelWidth: 110,\n" +
            "                            format: 'yyyy-MM-dd HH:mm:ss',\n" +
            "                            valueFormat: 'yyyy-MM-dd HH:mm:ss',\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";

    /**
     * 时间-小时选择器 {label}  {prop} {required}
     * 09:00 | 09:30 | 10:00
     */
    String TIME = "                        {\n" +
            "                            label: \"{label}\",\n" +
            "                            prop: \"{prop}\",\n" +
            "                            type: \"time\",\n" +
            "                            labelWidth: 110,\n" +
            "                            span: 20,\n" +
            "                            pickerOptions: {\n" +
            "                                start: '06:00',\n" +
            "                                step: '00:30',\n" +
            "                                end: '23:00'\n" +
            "                            },\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请输入 {label} \",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";
    /**
     * 数组框 ， 多内容添加，如标签
     * {label}  {prop} {required}
     */
    String ARRAY = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'array',\n" +
            "                            labelWidth: 110,\n" +
            "                            dataType: 'string',\n" +
            "                            limit: 10,\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请添加 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";
    /**
     * 图标 选择器
     * {label}  {prop} {required}
     */
    String ICON = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'icon',\n" +
            "                            labelWidth: 110,\n" +
            "                            iconList: this.icon.iconList,\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label}\",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }],\n" +
            "                        },\n";

    /**
     * 颜色 选择器
     * {label}  {prop} {required}
     */
    String COLOR = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: 'color',\n" +
            "                            labelWidth: 110,\n" +
            "                            colorFormat: \"hex\",\n" +
            "                            showAlpha: false,\n" +
            "                            predefine: [\"#FF8C00\", \"#FFD700\", \"#90EE90\", \"#00CED1\", \"#1E90FF\",\n" +
            "                                \"#C71585\", \"#FF4500\", \"#FF7800\", \"#FAD400\", \"#00BABD\"],\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label} \",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }],\n" +
            "                        },\n";


    /**
     * 级联选择器 {label}  {prop} {required}
     */
    String CASCADER = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            type: \"cascader\",\n" +
            "                            labelWidth: 110,\n" +
            "                            dataType: 'string',\n" +
            "                            filterable: true, \n" +
            "                            // 自行替换字典数据 \n" +
            "                            dicData: this.defaultdata.dicData,\n" +
            "                            span: 20,\n" +
            "                            props: {\n" +
            "                                value: \"id\",\n" +
            "                                label: \"name\",\n" +
            "                                children: \"children\"\n" +
            "                            },\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label} \",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }]\n" +
            "                        },\n";

    /**
     * 地图 选择器 {label}  {prop} {required}
     */
    String MAP = "                        {\n" +
            "                            // 需先配置高德js 详见: https://avuejs.com/form/form-input-map/#\n" +
            "                            // 新增可给定默认地址: 104.06601585298779,30.656922000443107,四川省成都市青羊区西御河街道天府广场今站购物中心\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            labelWidth: 110,\n" +
            "                            type: 'map',\n" +
            "                            dataType: 'string',\n" +
            "                            span: 20,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请选择 {label} \",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }],\n" +
            "                        },\n";


    /**
     * 文件上传
     * {label}
     * {prop}
     * {listType} ==> 单图/视频-[picture-img] |  多图/视频-[picture-card] |  缩略图-[picture] | 任意文件空
     * {limit} ==> 上传数量
     * {accept} ==> 文件上传格式 -> 图片默认: image/png, image/jpeg, image/jpg, image/gif  | 视频默认: video/mp4 | 任意文件不限制
     * {tip} ==> 文件上传提示  -> 【图片: 只能上传 jpg/png/gif 格式的图片】 【视频：只能上传mp4格式的视频】【任意文件：无】
     * {fileType} ==> 文件类型, 分类图片/视频/任意文件的存储路径
     * {required}
     */
    String UPLOAD = "                        {\n" +
            "                            label: '{label}',\n" +
            "                            prop: '{prop}',\n" +
            "                            dataType: 'string', \n" +
            "                            labelWidth: 110,\n" +
            "                            rules: [{\n" +
            "                                required: {required},\n" +
            "                                message: \"请上传 {label} \",\n" +
            "                                trigger: \"blur\"\n" +
            "                            }],\n" +
            "                            span: 24,\n" +
            "                            accept: {accept},  \n" +
            "                            type: 'upload',\n" +
            "                            listType: '{listType}', \n" +
            "                            action: uploadPath + 'file/gc/{fileType}/',   // 上传地址 + 文件保存上传地址(详见接口描叙)\n" +
            "                            multiple: true,          // 文件多选\n" +
            "                            drag: true,              // 拖拽排序\n" +
            "                            limit: {limit},                // 上传数量 1 个\n" +
            "                            //fileSize: 500,         // 上传大小 500 kb内\n" +
            "                            tip: '{tip}',\n" +
            "                            loadText: '上传中...',   \n" +
            "                            propsHttp: {\n" +
            "                                res: 'data'\n" +
            "                            },\n" +
            "                            uploadBefore: (file, done) => {\n" +
            "                                // 文件上传前处理\n" +
            "                                done(file)\n" +
            "                            },\n" +
            "                            uploadAfter: (res, done) => {\n" +
            "                                this.$message.success('上传成功');\n" +
            "                                done()\n" +
            "                            },\n" +
            "                            uploadError(error, column) {\n" +
            "                                // 上传失败\n" +
            "                                this.$message.error(error);\n" +
            "                            },\n" +
            "                            uploadExceed(limit, files, fileList, column){\n" +
            "                                // 文件数量验证\n" +
            "                                this.$message.warning(`当前限制文件数量为 ${limit}, 当前共 ${files.length + fileList.length} `);\n" +
            "                            },\n" +
            "                        },\n";
}
