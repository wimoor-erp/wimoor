<template>
<div>
	<el-button @click="copyshipment" >复制货件</el-button>
	<el-dialog
	  v-model="dialogVisible"
	  title="操作提示"
	  width="30%"
	>
	<div v-loading="statusLoading"> <span>{{canceltitle}}</span></div>
	 
	  <template #footer>
	    <span class="dialog-footer">
		  <el-button @click="confirmDelete('local')" :style="{ display: visibleBtn }">仅删除本地</el-button>
	      <el-button @click="dialogVisible = false">取消</el-button>
	      <el-button type="primary" @click="confirmDelete('')" :loading="confirmCancelLoading"
	        >确认</el-button
	      >
	    </span>
	  </template>
	</el-dialog>
</div>
</template>

<script setup>
	
	import {TakeOff,TransactionOrder,Local} from '@icon-park/vue-next';
	import { ref,reactive,onMounted } from 'vue';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import shipmentApi from '@/api/amazon/inbound/shipmentApi.js';
	import { useRoute,useRouter } from 'vue-router'
	import { ElMessageBox ,ElMessage } from 'element-plus'
	const emit = defineEmits(['change']);
	let router = useRouter()
	const shipmentid = router.currentRoute.value.query.shipmentid;
	let canceltitle=ref("亚马逊后台货件状态为,请确认是否同步删除亚马逊货件？")
	let shipstatus=ref("")
	let dialogVisible = ref(false)
	let confirmCancelLoading=ref(false);
	let visibleBtn=ref("none")
	let statusLoading=ref(false);
	let shipDatas=ref({})
	        function loadOptData(shipmentAll){
				shipDatas.value=shipmentAll;
			}
			function deleteShipment(){
					//先弹窗打开modal 获取最新的货件状态
					dialogVisible.value=true;
					statusLoading.value=true;
					var status=shipDatas.value.shipmentstatus;
					shipmenthandlingApi.requestInboundShipment({
						"shipmentid":shipmentid
					}).then(res=>{
						if(res.data!="fail"){
							status=res.data;
							shipstatus.value=res.data;
							if(status!="CANCELLED"){
								visibleBtn.value="";
								canceltitle.value="亚马逊后台货件状态为"+status+",请确认是否同步删除亚马逊货件？";
							}else{
								canceltitle.value="亚马逊后台货件状态为"+status+",请确认是否删除本地货件？";
							}
						}else{
							visibleBtn.value="";
							canceltitle.value="亚马逊后台货件状态无法判断,请选择仅删除本地货件。";
						}
					    statusLoading.value=false;
						emit("change");
					})
					
					
				}	
			function confirmDelete(ftype){
				var nowstatus="";
				if(ftype=="local"){
					nowstatus="DELETED";
				}else{
					nowstatus=shipstatus.value;
				}
				confirmCancelLoading.value=true;
				shipmenthandlingApi.disableShipment({
					"shipmentid":shipmentid,
					"shipmentStatus":nowstatus,
					"disableShipment":"1"
				}).then(res=>{
					  ElMessage.success('操作成功！');
					  confirmCancelLoading.value=false;
					  dialogVisible.value=false;
					  context.emit("change");
				}).catch(error=>{
					 confirmCancelLoading.value=false;
				})
			}
			function copyshipment(){
							router.push({
								path:'/invoice/addshipment',
								query:{
									title:'添加货件',
									path:'/invoice/addshipment',
									shipmentid:shipmentid
								}
							}) 
						}
			function localDoneShipmentFunc(){
				ElMessageBox.confirm(
					'该操作会对库存产生影响，请确认是否要执行本地已完成？',
					{
					  confirmButtonText: '确认',
					  cancelButtonText: '取消',
					  type: 'warning',
					  callback:(action)=>{
						 if(action=="confirm"){
							  //shipmentApi.refreshShipmentRec({'shipmentid':shipmentid}).then(res=>{
									 shipmenthandlingApi.localDoneShipment({
									 	  "shipmentid":shipmentid
									  }).then(res=>{
										  ElMessage.success('操作成功！');
										  context.emit("change");
								 })
							// })
						 }
					  }
					}
				  )
				
			}	
		   defineExpose({loadOptData})
</script>

<style>
</style>
