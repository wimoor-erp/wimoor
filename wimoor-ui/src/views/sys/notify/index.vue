<template>
	<div class="main-sty">
	<!--  头部搜索区域 -->
	<el-row :gutter="20">
	  <el-col :span="15">
		<div   >
		<el-form :model="form" label-width="120px">
		    <el-form-item label="消息类型：">
		     <el-select v-model="form.action" placeholder="消息类型">
		       <el-option label="常规消息" value="like" />
		       <el-option label="即时消息" value="timely" />
		     </el-select>
		    </el-form-item>
		    <el-form-item label="用户类型">
		      <el-select v-model="form.target" placeholder="用户类型">
		        <el-option label="全部用户" value="AL" />
		        <el-option label="付费用户" value="PA" />
			<el-option label="免费用户" value="FR" />
		      </el-select>
		    </el-form-item>
		    <el-form-item label="标题:">
				<el-space>
			  <el-select v-model="form.titlePre" placeholder="标题前缀">
		       <el-option label="【重要公告】" value="【重要公告】" />
		       <el-option label="【系统公告】" value="【系统公告】" />
			   <el-option label="【重要通知】" value="【重要通知】" />
			   <el-option label="" value="" />
		     </el-select>
		       <el-input style="width:400px" v-model="form.title"></el-input>
			   </el-space>
		    </el-form-item>
		    
		    <el-form-item label="内容">
			<TinyMCEEditor
			  ref="tinyMceEditorRef"
			  v-model="form.content"
			  editorKey="editorElement20251020"
			  :config="editorConfig"
			  @init="onEditorInit"
			  @change="onContentChange"
			  @mceNewDocument="handleNew"
			  :debug-mode="true"
			/>
		    </el-form-item>
		    <el-form-item>
		      <el-button type="primary" @click="onSubmit">提交</el-button>
		      <el-button>取消</el-button>
		    </el-form-item>
		  </el-form>
		  </div>
	  </el-col>
	  <el-col :span="9">
		<div class="announcement-section">
		  <h3>系统公告</h3>
		  <GlobalTable 
		    :inDialog="false" 
		    :height="'calc(100vh - 250px)'" 
		    ref="globalTableRef" 
		    :tableData="tableData"   
		    @loadTable="loadTableData"
		    :defaultSort="{ prop: 'createdAt', order: 'descending' }"  
		    :showHeader="false"
		    :stripe="false"
		    rowKey="id"
		  >
		    <template #field>
		      <el-table-column label="title"  prop="title" >
		        <template #default="scope">
		          <div class="message-wrapper">
		            <div class="mWimageB" v-if="scope.row.icon">
		              <el-icon color="#fff"><Comment /></el-icon>
		            </div>
		            <div class="mWimage" v-else>
		              <el-icon color="#fff"><BellFilled /></el-icon>
		            </div>
		            <div class="mWtext">
		              <div class="mWcon">
		                <el-tooltip
		                  effect="dark"
		                  :content="scope.row.ntitle"
		                  placement="top-start"
		                >
		                  <div class="font24 text-omit-1" v-html="scope.row.ntitle"></div>
		                </el-tooltip>
		                <div class="font-extraSmall ">
		                  <span style="padding-right:16px;" v-if="scope.row.systype">{{scope.row.systype}}</span> 
						   <span class="font-extraSmall" style="white-space: nowrap;">{{scope.row.createdAt}}</span>
		                </div>
		              </div>
		             
		            </div>
		          </div>
	          <div class="message-body message-content" v-html="scope.row.content"></div>
	        </template>
	      </el-table-column>
	    </template>
	  </GlobalTable>
	</div>
	</el-col>
	</el-row>
	</div>
</template>
<script>
export default {
	name: "公告发布"
}
</script>
<script setup>
	import {ref,reactive,onMounted,watch,inject,toRefs,nextTick} from 'vue';
	import notify from "@/api/sys/admin/notify.js";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import { BellFilled,Comment } from '@element-plus/icons-vue';
	import TinyMCEEditor from '@/components/TinyMCE/TinyMCEEditor.vue';
	import GlobalTable from '@/components/Table/GlobalTable/index.vue';
	const globalTableRef=ref();
	const state = reactive({
		form:{title:"",content:"",action:"timely",target:"AL",},
		tableData:{records:[],total:0},
		queryParams:{}
	})
	const {
		form,tableData,queryParams
	}=toRefs(state);
	
	
	const editorConfig = ref({
	  language: 'zh_CN',
	  language_url: '/tinymce/langs/zh_CN.js',
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
	  table_toolbar: 'tableprops tabledelete | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol'
	})
	
	const onEditorInit = (editor) => {
	  console.log('编辑器初始化完成', editor)
	}
	
	const onContentChange = (content) => {
	  console.log('内容发生变化', content.length)
	}
	
	const saveContent = () => {
  console.log('保存内容:', state.form.content)
  // 这里可以添加保存逻辑
  alert('内容已保存（查看控制台）')
}
	
	const clearContent = () => {
  state.form.content = '<p></p>'
} 
	function onSubmit(){
		var data=JSON.parse(JSON.stringify(state.form));
		data.title = data.titlePre + data.title;
		notify.addAnnounce(data).then(res=>{
			ElMessage.success('提交成功!');
			// 提交成功后重新加载公告列表
			globalTableRef.value.loadTable(state.queryParams);
		})
	}

	// 加载公告列表
	function loadTableData(param){
		notify.getAnnounce(param).then(res=>{
			if(res.data && res.data.records){
				res.data.records.forEach(item=>{
					item.ntitle = item.title.slice(6);
					item.systype = item.title.slice(1,5);
					if(item.systype==='系统公告'){
						item.icon = 'sysgg';
					}
				});
				state.tableData.records = res.data.records;
				state.tableData.total = res.data.total;
			}
		});
	}

	// 组件挂载时加载公告列表
	onMounted(()=>{
		globalTableRef.value.loadTable(state.queryParams);
	});
</script>

<style scoped>
.announcement-section {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  height: 100%;
  overflow-y: auto;
}

.announcement-section h3 {
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.message-wrapper{
  display: flex;
}

.mWtext{
  display: flex;
  justify-content: space-between;
  flex-grow:1;
  margin-bottom: 16px;
}

.font24{
  font-size:18px;
  font-weight:700;
  margin-bottom:4px;
  margin-right:24px;
}

.message-body{
  color:var(--el-text-color-regular);
}

.dark .message-body{
  color:var(--el-text-color-regular);
}

.mWimage{
  background-color: var(--el-color-primary-light-1);
  width:32px;
  height:32px;
  border-radius:16px;
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  margin-right:12px;
}

.mWimageB{
  background-color: var(--el-color-blue-light-1);
  width:32px;
  height:32px;
  border-radius:16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right:12px;
}

.message-content{
  margin-left:42px
}

.con-header {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.main-sty {
  padding: 20px;
}

.font-extraSmall {
  font-size: 12px;
  color: #909399;
}

.text-omit-1 {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>