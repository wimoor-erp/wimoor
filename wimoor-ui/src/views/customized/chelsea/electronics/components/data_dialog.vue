<template>
	<el-button @click="show">提取EEE和FC销量</el-button>
   <el-dialog v-model="visible" title="从已经上传的报告中提取EEE销量和FC销量"    >
	   <el-row>
		   <el-col :span="12">
	   <el-descriptions column="1">
		   <el-descriptions-item label="店铺">
	         <GroupSelect @change="handleGroup" defaultValue='only'></GroupSelect>
		   </el-descriptions-item>
		   <el-descriptions-item label="区域">
	         <Region ref="regionRef" @change="handleRegion"></Region>
		   </el-descriptions-item>
		   <el-descriptions-item label="起始日期">
		      <el-date-picker v-model="fromDate" type="date"></el-date-picker >
		   </el-descriptions-item>
	   </el-descriptions>
	   </el-col>
	   <el-col :span="12">
		   <div style="padding-bottom:20px" class="text-center">
			       <el-button :loading="eeeloading" type="primary" style="padding-top:30px;padding-bottom:30px;width:100px" @click="handleEprEEE">提取EEE</el-button>
		   </div>
		   <div  class="text-center">
			     <el-button :loading="fcloading" type="success" style="padding-top:30px;padding-bottom:30px;width:100px" @click="handleFc">提取FC</el-button>
		   </div>
	   </el-col>
	   </el-row>
	 <template #footer>
			 <el-button @click="visible = false">关闭</el-button>
	 </template>
	  </el-dialog>
</template>
	
<script setup>
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import { ElMessage,ElLoading } from 'element-plus';
		import GroupSelect from "@/components/header/group_select.vue";
		import Region from "@/components/header/region.vue";
		import authApi from '@/api/amazon/auth/authApi.js';
		import Datepicker from '@/components/header/datepicker.vue';
		import electronicsApi from "@/views/customized/chelsea/api/electronics/electronics.js";
		let state=reactive({visible:false,fromDate:null,
		fcloading:false,amazonauthid:"", 
		callback:null,eeeloading:false,action:undefined});
		const{visible,fromDate,fcloading,eeeloading,amazonauthid }=toRefs(state);
		 const emit = defineEmits(['change']);
		 const regionRef=ref();
		function show(ftype){
			state.visible=true;
			state.fromDate = new Date()
			state.fromDate.setTime(state.fromDate - 3600 * 1000 * 24 * 60)
		}
		function hide(){
				state.visible=false;
				state.eeeloading=false;
				state.fcloading=false;
		}
		function handleGroup(groupid){
			regionRef.value.getData(groupid);
		}
		function handleRegion(amazonauthid){
			state.amazonauthid=amazonauthid;
		}
		function handleFc(){
			var param={"amazonAuthid":state.amazonauthid,
			           "fromDate":state.fromDate};
			electronicsApi.vatFc(param).then(res=>{
				ElMessage.success("提取成功");
				state.fcloading=false;
				emit("change");
			}).catch(e=>{
				state.fcloading=false;
			})
		}
		function handleEprEEE(){
			state.eeeloading=true;
			var param={"amazonAuthid":state.amazonauthid,
			           "fromDate":state.fromDate};
			electronicsApi.eprEEE(param).then(res=>{
				ElMessage.success("提取成功");
				state.eeeloading=false;
				emit("change");
			}).catch(e=>{
				state.eeeloading=false;
			})
		}
		 
		function loading(){
			state.btnloading=true;
		}
		defineExpose({
			show,hide,loading
		})
</script>
	
<style scoped="scoped">
	.text-orange{
		color:var(--el-color-primary);
		font-size:12px;
		font-weight: 700;
	}
</style>