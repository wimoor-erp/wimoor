import { createApp } from 'vue';
import 'element-plus/dist/index.css';
import 'element-plus/theme-chalk/dark/css-vars.css';
import App from './App.vue';
import zhCn from 'element-plus/es/locale/lang/zh-cn';
import '@/assets/css/global.css';
import '@/assets/css/css-vars.css';
import router from './router';
import store from './store';
import ElementPlus from 'element-plus';
import { ElMessage } from 'element-plus';
import { hasPerm, hasPermi, dataType } from '@/directive/permission.js';
import require from '@/utils/system/require.js';
import print from 'vue3-print-nb';
import plugins from './plugins' // plugins
import HocElementAffix from '@hoc-element/affix'
import * as filters from './filters'; // global filters
import { listDictsByCode } from "@/api/sys/admin/dict.js";
import { useDict } from '@/utils/dict'
import Pagination from '@/components/Pagination/index.vue';
import GlobalTable from "@/components/Table/GlobalTable/index.vue";
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from '@/utils/wimoor'
import { download } from '@/utils/request.js'
// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar'
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload/upload.vue"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
import mitt from 'mitt';
const emitter = mitt();
const app = createApp(App)
app.use(store)
app.use(HocElementAffix)
app.use(ElementPlus, {
	locale: zhCn,
	size: 'default',
})
app.config.globalProperties.$require = require;
app.config.globalProperties.listDictsByCode = listDictsByCode;

app.config.globalProperties.$message.success = (msg) => {
	return ElMessage({
		offset: 48,
		message: msg,
		type: 'success',
		showClose: true,
	})
}
app.config.globalProperties.$message.error = (msg) => {
	return ElMessage({
		offset: 48,
		message: msg,
		type: 'error',
		showClose: true,
	})
}
app.config.globalProperties.$message.warning = (msg) => {
	return ElMessage({
		offset: 48,
		message: msg,
		type: 'warning',
		showClose: true,
	})
}
// 全局方法挂载
app.config.globalProperties.useDict = useDict
app.config.globalProperties.download = download
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels
app.use(router)
app.use(hasPerm)
app.use(hasPermi)
app.use(dataType)
app.use(print)
app.use(plugins)
// 全局组件挂载
app.use(elementIcons)
app.component('svg-icon', SvgIcon)
app.component('DictTag', DictTag)
app.component('FileUpload', FileUpload)
app.component('ImageUpload', ImageUpload)
app.component('ImagePreview', ImagePreview)
app.component('RightToolbar', RightToolbar)
app.component('Editor', Editor)
app.component('Pagination', Pagination)
app.component('GlobalTable', GlobalTable)
app.provide('emitter', emitter); // 注入provider
app.mount('#app')


