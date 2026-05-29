<template>
	<div class="flex-auto">
			<el-select v-model="statusid"    placeholder="产品销售状态" @change="statusChange">
				 <template #prefix>
					<el-button size="small" @click.stop="editSaleStatus" type="info"  text  bg>
						<el-icon class="font-base font-bold"><Setting /></el-icon>
					</el-button>
				 </template>
			      <el-option  v-for="item in statusList"    :label="item.name" :value="item.id"   >
			      </el-option>
			</el-select>
			</div>
		   <SaleStatusDialog ref="SaleStatusRef"/>
</template>

<script>
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import { ref,reactive,onMounted,watch } from 'vue'
	import SaleStatusDialog from"./sale_status_dialog.vue"
	import {Setting} from '@element-plus/icons-vue';
	export default{
		name:"status",
		components:{ Setting,SaleStatusDialog},
		emits:["status"],
		setup(props,context){
			let statusList =ref([])
			let statusid=ref()
			let SaleStatusRef =ref()
			onMounted(()=>{
				getStatusData()
			})
		 
			//获取负责人列表
			function getStatusData(){
				productinfoApi.getProStatusList().then((res)=>{
					if(res.data&&res.data.length>0){
						res.data.push({"id":"","name":"全部销售状态"})
						statusList.value=res.data;
						statusList.value.forEach((item,index)=>{
							if(!item){
								statusList.value.splice(index,1)
							}
						})
						statusid.value ="";
						context.emit("status",statusid.value);
					}
				})
			}
			//改变负责人
			function statusChange(id){
				 context.emit("status",id);
			}
			 function editSaleStatus(){
				SaleStatusRef.value.saleStatusVisable =true
			 }
			 function reset(){
				 statusid.value ="";
			 }
			return{
				 statusid,statusList,statusChange,getStatusData,
				 editSaleStatus,SaleStatusRef,reset,
			}
		}
	}
</script>

<style>
	 .flex-auto{
		 flex: auto;
	 }
</style>
