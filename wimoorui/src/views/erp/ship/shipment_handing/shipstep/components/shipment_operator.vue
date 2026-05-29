<template>
<div>
 
	<el-button v-if="shipDatas.status=='6' ||shipDatas.status=='5' || shipDatas.status=='-1' || shipDatas.status=='0'" disabled="disabled" v-hasPerm="'amz:ship:localdone'">本地完成</el-button>
	<el-button v-else  @click="localDoneShipmentFunc" v-hasPerm="'amz:ship:localdone'">本地完成</el-button>
	<el-button v-if="shipDatas.status>='2'"  @click="deleteShipment"  v-hasPerm="'amz:ship:delete'">删除货件</el-button>
	<el-dialog
	  v-model="dialogVisible"
	  title="操作提示"
	  width="30%"
	>
	<div v-loading="statusLoading"> <span>{{canceltitle}}</span></div>
	  <template #footer>
	    <span class="dialog-footer">
		  <el-button @click="confirmDelete('local')" type="primary"  >仅删除本地</el-button>
	      <el-button @click="dialogVisible = false">取消</el-button>
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
	let canceltitle=ref("此货件仅支持本地删除,还原本地库存。")
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
