<template>
	<el-select v-model="plateformid"   placeholder="全部平台" @change="handleChange">
		 <template #prefix>
			<el-button size="small" @click.stop="showAddDailog"   type="info"  text bg>
			   <el-icon><House /></el-icon>
			</el-button>
		 </template>
		  <el-option label="全部平台"  value=""  > </el-option>
	      <el-option v-for="item in options"   :label="item.name" :value="item.id"   >
	      </el-option>
	</el-select>
	<el-dialog v-model="visible" title="平台管理" :destroy-on-close='true' width="600px"  >
		   <div style="padding-bottom:20px;"> 
		   <el-space >
		      <el-tag v-for="item in options" :key="item.name" closable type="warning" @close="handleDisable(item)">
		         {{ item.name }}
		       </el-tag>
			  </el-space>
		   </div>
		    <el-input  v-model="name"   style="width:300px"  placeholder="请输入平台名称"  >
		       <template #append  >  <el-button :icon="Plus"  @click="handleAdd"  />   </template>
		    </el-input>
	  <template #footer>
	  	<span class="dialog-footer">
	  		<el-button @click="visible = false">关闭</el-button>
	  	</span>
	  </template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,UploadFilled,House} from '@element-plus/icons-vue';
	import orderApi from "@/api/erp/order/orderApi.js";
	const emit = defineEmits(['change']);
	const state =reactive({
		visible:false,
		plateformid:"",
		options:[],
		name:"",
	})
	const {
	 visible,plateformid,options,name,
	}=toRefs(state)
	function handleChange(){
		emit('change',plateformid);
	}
	onMounted(()=>{
		loadData();
	})
	function handleAdd(){
		var data={name:state.name};
		orderApi.savePlatform(data).then(res=>{
			loadData();
			state.name="";
			ElMessage.success("操作成功");
		})
	}
	function loadData(){
		orderApi.listPlatform().then(res=>{
			state.options=res.data;
		})
	}
	function handleDisable(item){
		orderApi.removePlatform(item).then(res=>{
			loadData();
			ElMessage.success("操作成功");
		})
	}
	function showAddDailog(){
		state.visible=true;
	}
</script>

<style>
</style>