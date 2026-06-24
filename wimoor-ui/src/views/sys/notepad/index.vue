<template>
  <div class="notepad-app">
    <el-row :gutter="0" class="notepad-row">
      <el-col :span="5" class="notepad-sidebar">
        <div class="sidebar-header">
          <div class="search-box">
            <div class="search-row">
              <el-select v-model="queryParams.searchtype" class="search-type-select">
                <el-option label="标题" value="title" />
                <el-option label="SKU" value="sku" />
                <el-option label="内容" value="content" />
              </el-select>
              <el-input
                v-model="queryParams.search"
                placeholder="搜索笔记..."
                :prefix-icon="Search"
                @keyup.enter="handleQuery"
                class="search-input"
              />
              <el-button :icon="Search" circle @click="handleQuery" class="search-btn" />
            </div>
          </div>
          <el-button type="primary" class="new-note-btn" @click="handleNew">
            <el-icon><Plus /></el-icon>
            <span>新建笔记</span>
          </el-button>
        </div>

        <div class="notes-list" v-loading="loading">
          <div v-if="tableData.records.length === 0" class="empty-state">
            <div class="empty-icon">📝</div>
            <div class="empty-text">暂无笔记</div>
            <div class="empty-subtitle">点击上方按钮创建新笔记</div>
          </div>
          
          <div
            v-for="(item, index) in tableData.records"
            :key="item.id"
            class="note-item"
            :class="{ 'active': notepad.id === item.id }"
            @click="handleTableRowClick(item)"
          >
            <div class="note-item-content">
              <div class="note-title">{{ item.title || '无标题笔记' }}</div>
              <div class="note-preview">{{ getPreviewText(item.content) }}</div>
            </div>
            <div class="note-actions">
              <el-dropdown trigger="click">
                <el-button link class="action-btn">
                  <el-icon><MoreFilled /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleBindSku(item)">
                      <el-icon><Link /></el-icon>
                      绑定SKU
                    </el-dropdown-item>
                    <el-dropdown-item @click="handleDelete(item)" divided>
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>

        <div class="sidebar-footer">
          <pagination
            :total="tableData.total"
            layout="total, prev, next"
            v-model:page="queryParams.currentpage"
            v-model:limit="queryParams.pagesize"
            @pagination="handleQuery()"
            :pager-count="3"
            small
          />
        </div>
      </el-col>

      <el-col :span="19" class="notepad-main">
        <div class="editor-header">
          <div class="header-content">
            <div class="title-input-wrapper">
              <el-input
                v-model="notepad.title"
                placeholder="输入笔记标题..."
                class="title-input"
                size="large"
              />
            </div>
            <div class="header-actions">
              <el-button 
                type="primary" 
                size="large" 
                @click="handleSave" 
                :loading="saveloading"
                class="save-btn"
              >
                <el-icon><Check /></el-icon>
                保存笔记
              </el-button>
            </div>
          </div>
        </div>

        <div class="editor-wrapper">
          <div class="paper-effect">
            <div class="editor-container">
              <TinyMCEEditor
                ref="tinyMceEditorRef"
                v-model="notepad.content"
                editorKey="editorElement20251011"
                :config="editorConfig"
                @init="onEditorInit"
                @change="onContentChange"
                @mceNewDocument="handleNew"
                :debug-mode="true"
              />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <MaterialDialog ref="MaterialRef" @getdata="getRows" />
</template>
 
<script setup>
	
import {  onMounted,ref ,nextTick,reactive,toRefs,onUnmounted,watch} from 'vue';
import { Search, Plus, Edit, Refresh, Delete,MoreFilled,Link,Check } from '@element-plus/icons-vue';
import 'highlight.js/styles/atom-one-dark.css' // 引入highlight.js的样式，这里选择atom-one-dark样式，你可以选择其他样式
import notepadApi from '@/api/sys/tool/notepadApi';
import { ElMessage, ElMessageBox } from 'element-plus';
// 引入highlight.js
import hljs from 'highlight.js';
import TinyMCEEditor from '@/components/TinyMCE/TinyMCEEditor.vue';
import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
import markApi from "@/api/erp/material/markApi";
const mytableRef = ref();
const MaterialRef = ref();
const tinyMceEditorRef = ref(null); // 添加编辑器引用
const state = reactive({
  notepad:{ content: '<p></p>', title:""},
  queryParams:{currentpage:1,pagesize:20,searchtype:"title",search:''},
  tableData:{records:[],total:0},
	loading: false,
	saveloading: false,
	bindNotepad: {},
});
const { notepad,editorOption,tableData,queryParams,saveloading } = toRefs(state);



const editorConfig = ref({
  height: 'calc(100vh - 160px)',
  language: 'zh_CN',
  language_url: '/tinymce/langs/zh_CN.js',
  menubar: 'file edit view insert format tools',
  plugins: [
    'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview', 'anchor',
    'searchreplace', 'visualblocks', 'code','codesample', 'fullscreen',
    'insertdatetime', 'media', 'table', 'help', 'wordcount', 'quickbars'
  ],
  toolbar:[
  'undo redo',
  'blocks',
  'bold italic underline strikethrough',
  'forecolor backcolor',
  'removeformat',
  'alignleft aligncenter alignright alignjustify',
  'bullist numlist outdent indent',
  'link quickimage table  ',
  'codesample help'
  ].join(' | '),
  quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote ',
  quickbars_insert_toolbar: 'quickimage quicktable',
  table_toolbar: 'tableprops tabledelete | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol',
  content_style: `
    body {
      font-family: 'Georgia', 'Times New Roman', 'PingFang SC', 'Microsoft YaHei', serif;
      font-size: 16px;
      line-height: 1.8;
      color: #303133;
      padding: 20px;
      background: transparent;
    }
    p {
      margin-bottom: 16px;
    }
    h1, h2, h3, h4, h5, h6 {
      color: #1a1a1a;
      margin-top: 24px;
      margin-bottom: 16px;
    }
    a {
      color: #667eea;
      text-decoration: none;
    }
    a:hover {
      text-decoration: underline;
    }
    blockquote {
      border-left: 4px solid #667eea;
      padding-left: 16px;
      margin-left: 0;
      color: #606266;
      font-style: italic;
    }
    pre {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 16px;
      overflow-x: auto;
    }
    code {
      background: #f5f7fa;
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 14px;
    }
  `
})

const onEditorInit = (editor) => {
  console.log('编辑器初始化完成', editor)
}

const onContentChange = (content) => {
  console.log('内容发生变化', content.length)
}

const saveContent = () => {
  console.log('保存内容:', state.notepad.content)
  // 这里可以添加保存逻辑
  alert('内容已保存（查看控制台）')
}

const clearContent = () => {
  state.notepad.content = '<p></p>'
} 
function getRows(rows) { 
	let materialid = "";
	if(rows && rows.length>0){
		rows.forEach(function(item){
				if(materialid.indexOf((item.id+","))<0){
					 materialid+=item.id+",";
				}
		});
		var data = { mark: state.bindNotepad.id, materialid: materialid, ftype: "notepad" };
		markApi.notepad(data).then(res=>{
				ElMessage.success("绑定成功");
		})
	}
}
function handleBindSku(row) {
	 state.bindNotepad=row;
	 MaterialRef.value.show( );
 }
   function onEditorReady(quill) {
      // 编辑器准备就绪后，可以获取quill实例进行一些操作
      console.log('Editor is ready!', quill)
    }
 function handleQuery(type){
	state.loading = true;
	notepadApi.list(state.queryParams).then(res=>{
			 state.tableData.records=res.data.records;
			 state.tableData.total=res.data.total;
			 if(type=='init'&&state.tableData&&state.tableData.records&&state.tableData.records.length>0){
				
				 nextTick(()=>{
					 state.notepad=JSON.parse(JSON.stringify(state.tableData.records[0]));
					  mytableRef.value.setCurrentRow(state.tableData.records[0])
				 })
				
			 }
	}).finally(() => {
		state.loading = false;
	});
 }

 // 获取内容预览文本
 function getPreviewText(content) {
   if (!content) return '空笔记';
   // 移除HTML标签并截取前50个字符
   const text = content.replace(/<[^>]+>/g, '').replace(/\s+/g, ' ').trim();
   return text.length > 50 ? text.substring(0, 50) + '...' : text || '空笔记';
 }
 onMounted(()=>{
	 handleQuery('init')
 });
 function handleTableRowClick(row){	
	console.log('切换笔记，内容长度:', row.content.length);
	state.notepad=JSON.parse(JSON.stringify(row));
	
	// 当切换笔记时，确保编辑器内容同步更新
	nextTick(() => {
	  if (tinyMceEditorRef.value) {
	    const editor = tinyMceEditorRef.value.getEditor();
	    if (editor) {
	      console.log('编辑器实例存在，当前内容长度:', editor.getContent().length);
	      if (editor.getContent() !== row.content) {
	        console.log('内容不匹配，更新编辑器内容');
	        editor.setContent(row.content);
	        
	        // 额外检查确保内容已正确设置
	        setTimeout(() => {
	          if (editor.getContent() !== row.content) {
	            console.warn('内容更新失败，再次尝试');
	            editor.setContent(row.content);
	          } else {
	            console.log('内容更新成功');
	          }
	        }, 200);
	      } else {
	        console.log('内容已经匹配，无需更新');
	      }
	    } else {
	      console.log('编辑器实例不存在，调用刷新方法');
	      // 编辑器实例获取失败，尝试调用刷新方法
	      tinyMceEditorRef.value.refreshEditor().then(success => {
	        console.log('刷新编辑器结果:', success);
	        
	        // 刷新后再次尝试设置内容
	        setTimeout(() => {
	          const refreshedEditor = tinyMceEditorRef.value.getEditor();
	          if (refreshedEditor) {
	            refreshedEditor.setContent(row.content);
	          }
	        }, 300);
	      });
	    }
	  } else {
	    console.warn('编辑器引用未定义');
	  }
	});
}
 function handleNew(){
	 state.notepad={ content: '', title:""};
	 // 由于我们添加了watch监听器，编辑器内容会自动同步，不需要手动设置
 }
function handleDelete(row) {
	 ElMessageBox.confirm('确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        notepadApi.removeNote(row.id).then(res => {
          ElMessage.success('删除成功');
          handleQuery();
        });
      }).catch(() => {
        ElMessage.info('已取消删除');
      });
}
  
 function handleSave(){
	 state.saveloading=true;
	 notepadApi.save(state.notepad).then(res=>{
		 ElMessage.success("保存成功");
		 state.saveloading=false;
		 handleQuery();
	 }).catch(e=>{
		 state.saveloading=false;
	 })
 }

// 监听notepad内容变化，确保编辑器内容同步
watch(() => state.notepad.content, (newContent) => {
  nextTick(() => {
    if (tinyMceEditorRef.value) {
      const editor = tinyMceEditorRef.value.getEditor();
      if (editor && editor.getContent() !== newContent) {
        console.log('内容变化，更新编辑器');
        editor.setContent(newContent);
      } else if (!editor) {
        console.log('编辑器实例获取失败，尝试刷新');
        // 编辑器实例获取失败，尝试调用刷新方法
        tinyMceEditorRef.value.refreshEditor();
      }
    }
  });
});

// 添加页面可见性变化监听
const handleVisibilityChange = () => {
  if (!document.hidden && tinyMceEditorRef.value) {
    console.log('页面从隐藏变为可见，刷新编辑器');
    // 当页面从隐藏变为可见时，刷新编辑器确保内容正确显示
    setTimeout(() => {
      tinyMceEditorRef.value.refreshEditor();
    }, 100);
  }
};

document.addEventListener('visibilitychange', handleVisibilityChange);

// 组件卸载时清理事件监听
onUnmounted(() => {
  document.removeEventListener('visibilitychange', handleVisibilityChange);
});
</script>

<style scoped>
/* 记事本应用整体样式 */
.notepad-app {
  height: calc(100vh - 40px);
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
  font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

 

/* 左侧边栏样式 */
.notepad-sidebar {
  background: #ffffff;
  border-right: 1px solid #e8eaec;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 60px);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #f0f2f5;
  background: linear-gradient(180deg, #fafbfc 0%, #ffffff 100%);
}

.search-box {
  margin-bottom: 12px;
}

.search-row {
  display: flex;
  align-items: center;
  gap: 6px;
}

.search-type-select {
  width: 78px;
  flex-shrink: 0;
}

.search-type-select :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.search-type-select :deep(.el-input__inner) {
  font-size: 12px;
}

.search-input {
  flex: 1;
  min-width: 0;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.search-btn {
  flex-shrink: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: #fff;
}

.search-btn:hover {
  opacity: 0.85;
}

.new-note-btn {
  width: 100%;
  height: 40px;
  border-radius: 8px;
  font-weight: 500;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.new-note-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

/* 笔记列表样式 */
.notes-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.notes-list::-webkit-scrollbar {
  width: 6px;
}

.notes-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.notes-list::-webkit-scrollbar-track {
  background: transparent;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #909399;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.6;
}

.empty-text {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
}

.empty-subtitle {
  font-size: 12px;
  color: #b0b3b8;
}

.note-item {
  display: flex;
  align-items: center;
  padding: 12px 14px;
  margin-bottom: 6px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #ffffff;
  border: 1px solid transparent;
  position: relative;
}

.note-item:hover {
  background: #f8f9fb;
  border-color: #e8eaec;
  transform: translateX(2px);
}

.note-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  border-color: transparent;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.note-item.active .note-title {
  color: #ffffff;
  font-weight: 600;
}

.note-item.active .note-preview {
  color: rgba(255, 255, 255, 0.85);
}

.note-item.active .action-btn {
  color: rgba(255, 255, 255, 0.9);
}

.note-item-content {
  flex: 1;
  min-width: 0;
}

.note-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.2s;
}

.note-preview {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.2s;
}

.note-actions {
  margin-left: 8px;
  opacity: 0;
  transition: opacity 0.2s;
}

.note-item:hover .note-actions,
.note-item.active .note-actions {
  opacity: 1;
}

.action-btn {
  padding: 4px;
  color: #909399;
}

.action-btn:hover {
  color: #409eff;
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 12px 16px;
  border-top: 1px solid #f0f2f5;
  background: #fafbfc;
}

.sidebar-footer :deep(.el-pagination) {
  justify-content: center;
}

/* 右侧主编辑区域 */
.notepad-main {
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

/* 编辑器头部 */
.editor-header {
  padding: 16px 24px;
  background: #ffffff;
  border-bottom: 1px solid #e8eaec;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.title-input-wrapper {
  flex: 1;
}

.title-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  font-size: 16px;
  font-weight: 500;
  padding: 8px 16px;
}

.title-input :deep(.el-input__inner) {
  color: #303133;
}

.save-btn {
  height: 44px;
  padding: 0 24px;
  border-radius: 8px;
  font-weight: 500;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  border: none;
  color: #303133;
  transition: all 0.3s ease;
}

.save-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(67, 233, 123, 0.4);
}

/* 编辑器包装器 */
.editor-wrapper {
  flex: 1;
  padding: 16px;
  overflow: hidden;
}

.paper-effect {
  background: #ffffff;
  border-radius: 12px;
  box-shadow:
    0 4px 20px rgba(0, 0, 0, 0.08),
    0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  position: relative;
  display: flex;
  flex-direction: column;
}

/* 纸张纹理效果 */
.paper-effect::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.1) 50%, transparent 100%);
  pointer-events: none;
  z-index: 1;
}

/* 模拟纸张横线效果 */
.paper-effect::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: repeating-linear-gradient(
    transparent,
    transparent 31px,
    rgba(0, 0, 0, 0.02) 31px,
    rgba(0, 0, 0, 0.02) 32px
  );
  pointer-events: none;
  z-index: 1;
}

.editor-container {
  flex: 1;
  padding: 0;
  position: relative;
  z-index: 2;
  overflow: hidden;
}

.editor-container :deep(.tox-tinymce) {
  border: none !important;
  border-radius: 0 !important;
}

.editor-container :deep(.tox .tox-menubar),
.editor-container :deep(.tox .tox-toolbar-overlord),
.editor-container :deep(.tox .tox-statusbar) {
  background: #fafbfc !important;
  border-color: #e8eaec !important;
}

.editor-container :deep(.tox .tox-edit-area) {
  border: none !important;
}

.editor-container :deep(.tox .tox-edit-area iframe) {
  background: transparent !important;
}

/* 编辑器内容区域样式 */
.editor-container :deep(.mce-content-body) {
  font-family: 'Georgia', 'Times New Roman', 'PingFang SC', 'Microsoft YaHei', serif;
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
  padding: 20px;
}

/* 滚动条样式 */
.notepad-main :deep(.tox .tox-edit-area) {
  overflow-y: auto;
}

.notepad-main :deep(.tox .tox-edit-area::-webkit-scrollbar) {
  width: 8px;
}

.notepad-main :deep(.tox .tox-edit-area::-webkit-scrollbar-thumb) {
  background: #c1c1c1;
  border-radius: 4px;
}

.notepad-main :deep(.tox .tox-edit-area::-webkit-scrollbar-track) {
  background: transparent;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.note-item {
  animation: fadeIn 0.3s ease forwards;
}

.note-item:nth-child(1) { animation-delay: 0.05s; }
.note-item:nth-child(2) { animation-delay: 0.1s; }
.note-item:nth-child(3) { animation-delay: 0.15s; }
.note-item:nth-child(4) { animation-delay: 0.2s; }
.note-item:nth-child(5) { animation-delay: 0.25s; }
.note-item:nth-child(6) { animation-delay: 0.3s; }
.note-item:nth-child(7) { animation-delay: 0.35s; }
.note-item:nth-child(8) { animation-delay: 0.4s; }
.note-item:nth-child(9) { animation-delay: 0.45s; }
.note-item:nth-child(10) { animation-delay: 0.5s; }

/* 响应式调整 */
@media (max-width: 1200px) {
  .notepad-sidebar {
    flex: 0 0 280px;
  }
  
  .notepad-main {
    flex: 1;
  }
}

/* 加载状态优化 */
.notes-list :deep(.el-loading-mask) {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
}

.notes-list :deep(.el-loading-spinner) {
  color: #667eea;
}

</style>