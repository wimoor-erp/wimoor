<template>
	<div class="main-sty">
	<!--  头部搜索区域 -->
	<div class="con-header" >
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
	       <el-input v-model="form.title"></el-input>
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
	import TinyMCEEditor from '@/components/TinyMCE/TinyMCEEditor.vue';
	const state = reactive({
		form:{title:"",content:"",action:"timely",target:"AL",}
	})
	const {
		form
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
	  console.log('保存内容:', state.notepad.content)
	  // 这里可以添加保存逻辑
	  alert('内容已保存（查看控制台）')
	}
	
	const clearContent = () => {
	  state.notepad.content = '<p></p>'
	} 
	function onSubmit(){
		notify.addAnnounce(state.form).then(res=>{
			ElMessage.success('提交成功!');
		})
	}
</script>

<style>
</style>