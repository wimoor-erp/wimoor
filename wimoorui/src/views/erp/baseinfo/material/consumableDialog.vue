<template>
 <el-dialog v-model="dialog.visible" title="辅料出库"  width="80%" top="6vh" :before-close="handleClose">
	<el-table :data="tableData" border >
		<el-table-column prop="image" label="图片" width="100" >
			<template  #default="scope">
				<el-image :src="scope.row.image"  style="width: 40px;height: 40px;"></el-image>
			</template>
		</el-table-column>
		<el-table-column prop="sku" label="SKU" width="100"   />
		<el-table-column prop="mname" label="产品名称"     />
		<el-table-column prop="inventoryqty" label="产品库存" width="100"   />
		<el-table-column prop="need" label="扣减数量" width="100"  >
			<template  #default="scope">
			    <div v-if="scope.row.out">
					<span class="font-extraSmall">已出库：</span>
					<span class="text-success">{{scope.row.out}}</span>
				</div>
				<el-input v-else type="number"   v-model="scope.row.need" @input="scope.row.need=CheckInputInt(scope.row.need)" ></el-input>
			</template>
		</el-table-column>
		<el-table-column prop="residue" label="剩余待扣" width="100"  >
			<template  #default="scope">
				<el-input type="number"  v-model="scope.row.residue" @input="scope.row.residue=CheckInputFloat(scope.row.residue)" ></el-input>
			</template>
		</el-table-column>
	</el-table>
	<template #footer>
			<span class="dialog-footer">
				<el-button @click="dialog.visible = false">关闭</el-button>
				<el-button type="primary" @click="submitFunc" v-if="tableData.length>0">确认</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup>
	import { ref, reactive, onMounted,toRefs ,watch} from 'vue'
	import { Search, ArrowDown, } from '@element-plus/icons-vue'
	import { ElDivider, ElMessageBox, ElMessage } from 'element-plus'
	import consumablesApi from '@/api/erp/material/consumablesApi.js';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import {formatFloat, CheckInputInt,CheckInputFloat} from '@/utils/index.js';
	const state=reactive({
		tableData:[],
		dialog:{visible:false},
		number:"",
		warehouseid:"",
		skulist:[],
	});
	const {  tableData,dialog,number,warehouseid,skulist  } = toRefs(state);
	function handleQuery(){
		consumablesApi.getConsumableList({
			"number": state.number,
			"skulist":state.skulist,
			"warehouseid":state.warehouseid
		}).then(res => {
			if (res.data && res.data.length > 0) {
				state.tableData = res.data;
				if(state.tableData&&state.tableData.length>0){
					state.tableData.forEach(item=>{
						item.residue= formatFloat(item.residue);
						if(item.out){
						    item.needamount=parseInt(item.needamount)-parseInt(item.out);
						}else{
							item.needamount=parseInt(item.needamount);
						}
						if(item.needamount<0){
							item.needamount=0;
						}
					});
					
				}
			}
		})
	}
	function submitFunc(){
		var skulist=[];
		var isok=true;
		var isout=false;
		state.tableData.forEach(function(item) {
			var row = {}
			row.sku = item.sku;
			if(item.out&&parseInt(item.out)>0){
				isout=true;
			}
			if(item.need){
				row.amount = parseInt(item.need);
			}else{
				row.amount =0;
			}
			if(item.residue){
				row.residue = parseFloat(item.residue);
			}else{
				row.residue =0;
			}
			if(row.amount<0){
				isok=false;
			}
			if(item.inventoryqty<=0){
				isok=false;
			}
			if(row.residue>=1){
				isok=false;
			}
			if(parseInt(item.need)>item.inventoryqty){
				isok=false;
			}
			skulist.push(row);
		});
		if(isok==false){
			ElMessage.error('剩余库存必须小于1或者辅料库存不足');
			return;
		}
		if(isout){
			ElMessage.error('已经辅料出库，无法再操作。');
			return;
		}
		if(skulist && skulist.length>0){
			consumablesApi.saveConsumable({
				"number": state.number,
				"warehouseid": state.warehouseid,
				"skulist":  skulist,
				"pkglist":null,
			}).then(res => {
				if(res.data>0){
					ElMessage.success('出库成功');	
					state.dialog.visible = false;
				}else{
					ElMessage.error( '出库失败,请检查是否录入正确');
				}
			})
		}else{
			ElMessage.error('出库产品数量必须大于0或已经出库');
		}
		
		
	}
	function show(row){
		 state.dialog.visible=true;
		 state.number=row.number;
		 state.warehouseid=row.warehouseid;
		 state.skulist=row.skulist;
		 handleQuery();
	}
	defineExpose({
	  show,
	})
</script>

<style>
</style>