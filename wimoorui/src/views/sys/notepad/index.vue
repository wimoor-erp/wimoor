<template>
  <el-row   >
	  <el-col :span="4">
		 <!-- 数据表格 -->
		<div style="padding-top:10px;padding-bottom:3px">
			<el-input v-model="queryParams.search">
		 <template #prepend>
		        <el-select v-model="queryParams.searchtype" placeholder="Select" style="width: 80px">
		          <el-option label="标题" value="title" />
		          <el-option label="SKU" value="sku" />
		          <el-option label="内容" value="content" />
		        </el-select>
		      </template>
		      <template #append>
		        <el-button :icon="Search" @click="handleQuery"/>
		      </template>
		</el-input></div> 
		 <el-table
		   :data="tableData.records"
		   ref="mytableRef"
		    v-loading="loading"
			:show-header="false"
			:highlight-current-row="true"
			 height="calc(100vh - 150px)"
			 @row-click="handleTableRowClick"
		   :stripe="true" 
		 >
		 <template #empty>
		 		 			 <div class="empty-wrapper">
		 		 			 <el-image :src="$require('nodata.png')"></el-image>
		 		 			 <div class="emptytext">暂无数据！</div>
		 		 			 </div>
		 </template>
		   <el-table-column  prop="title" align="center" show-overflow-tooltip="">
		     <template #default="scope">
		       {{scope.row.title}}
		     </template>
		   </el-table-column>
			 <el-table-column  prop=sku align="center" width="60" >
				<template #default="scope">
					<el-dropdown>
						<el-button link :icon="MoreFilled"></el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item @click="handleBindSku(scope.row)">绑定SKU</el-dropdown-item>
								<el-dropdown-item @click="handleDelete(scope.row)">删除</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				
				</template>
			</el-table-column>
			
		 </el-table>
		 <div style="background-color: #ffffff;">
		 <pagination
		   :total="tableData.total"
			 layout="total,  prev, next"
		   v-model:page="queryParams.currentpage"
		   v-model:limit="queryParams.pagesize"
		   @pagination="handleQuery()"
		 />
		 </div>
	  </el-col>
	 <el-col :span="20">
  
	 
	<!--  头部搜索区域 -->
	<el-card class="editor-header" style="">
			<div class="flex-between">
				<div  class="font-extraLarge"  style="margin-right:20px;width:60px;padding-top:5px">标题</div>
			    <el-input v-model="notepad.title" size="large" style="padding-right:10px">
				 </el-input> 
			 <el-button type="primary" size="large" @click="handleSave" :loading="saveloading">保存</el-button>
						
			</div> 
	 </el-card>	  
	 
	   <div class="editor-container"  style="height: calc(100vh - 200px);padding-right:10px;padding-left:10px">
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

  </el-col>
</el-row  >
<MaterialDialog  ref = "MaterialRef"       @getdata="getRows" />
</template>
 
<script setup>
	
import {  onMounted,ref ,nextTick,reactive,toRefs,onUnmounted,watch} from 'vue';
import { Search, Plus, Edit, Refresh, Delete,MoreFilled } from '@element-plus/icons-vue';
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
	saveloading: false,
	bindNotepad: {},
});
const { notepad,editorOption,tableData,queryParams,saveloading } = toRefs(state);



const editorConfig = ref({
  height: 'calc(100vh - 113px)',
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
  table_toolbar: 'tableprops tabledelete | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol'
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
	notepadApi.list(state.queryParams).then(res=>{
			 state.tableData.records=res.data.records;
			 state.tableData.total=res.data.total;
			 if(type=='init'&&state.tableData&&state.tableData.records&&state.tableData.records.length>0){
				
				 nextTick(()=>{
					 state.notepad=JSON.parse(JSON.stringify(state.tableData.records[0]));
					  mytableRef.value.setCurrentRow(state.tableData.records[0])
				 })
				
			 }
	})
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

<style>
	
	.editor-header .el-card__body{
		 padding-top:10px;
		 padding-bottom:8px;
	}
</style>
<style scoped>
.editor-header{
	margin-top:10px;
	margin-left:14px;
	margin-right:14px;
	margin-bottom:1px;
}
.notepad-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.editor-wrapper {
  margin: 20px 0;
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  overflow: hidden;
}

.actions {
  margin: 20px 0;
  display: flex;
  gap: 10px;
}

.save-btn, .clear-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.save-btn {
  background: #1890ff;
  color: white;
}

.save-btn:hover {
  background: #40a9ff;
}

.clear-btn {
  background: #f5f5f5;
  color: #333;
  border: 1px solid #d9d9d9;
}

.clear-btn:hover {
  background: #e6f7ff;
  border-color: #1890ff;
}

</style>