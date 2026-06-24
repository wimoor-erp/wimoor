<template>
	<el-dialog v-model="statusSelectVisable" title="销售状态" destroy-on-close='true' width="600px"  :modal="false">
		<el-radio-group v-model="statusid" class="warp-radio-group">
		      <el-radio  border 
			   v-for="item in statusList"    :label="item.id"   :value="item.id"
			    >{{item.name}}</el-radio>
		    </el-radio-group>
	  <template #footer>
	  	<span class="dialog-footer">
			<el-button @click.stop="statusSelectVisable=false">关闭</el-button>
	  		<el-button @click.stop="cancelStatus">删除状态</el-button>
	  		<el-button type="primary" @click.stop="submitStatus">提交</el-button>
	  	</span>
	  </template>
	</el-dialog>
</template>

<script>
	import {ref,reactive,onMounted} from "vue";
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import productinoptApi from '@/api/amazon/product/productinoptApi.js';
	import {ElMessage,ElDivider} from 'element-plus';
	export default{
		setup(){
			let statusSelectVisable =ref(false);
			let statusList =ref([]);
			let statusid=ref("0");
			let pid=ref();
			function loadData(row){
				statusSelectVisable.value=true;
				pid.value=row.id;
				productinfoApi.getProStatusList().then((res)=>{
					if(res.data&&res.data.length>0){
						statusList.value=res.data;
						productinoptApi.getOptStatusById({"pid":row.id}).then((res)=>{
							if(res.data){
								 statusid.value=res.data.toString(); 
							}
						});
					}
				})
			}
			function submitStatus(){
				productinoptApi.updateOptStatus({"pid":pid.value,"status":statusid.value}).then((res)=>{
					if(res.data=="ok"){
					    ElMessage.success('操作成功！');
						statusSelectVisable.value=false;
					}else{
						ElMessage.error("操作失败！");
					}
				});
			}
			function cancelStatus(){
				productinoptApi.updateOptStatus({"pid":pid.value,"status":"delete"}).then((res)=>{
					if(res.data=="ok"){
						ElMessage.success("删除状态成功！");
						statusid.value="0";
						statusSelectVisable.value=false;
					}else{
						ElMessage.error("操作失败！");
					}
				});
			}
			return{
				statusSelectVisable,statusList,statusid,pid,
			    submitStatus,loadData,cancelStatus
			}
		}	
	}
</script>

<style>
	.warp-radio-group label{
		margin-bottom:16px;
	}
	
	.warp-radio-group .el-radio{
		margin-right:16px;
	}
</style>
