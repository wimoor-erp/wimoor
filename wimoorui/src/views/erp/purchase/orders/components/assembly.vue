<template>
	<el-dialog v-model="dialogVisable"
	:title="asstitle"
	>
	<div v-if="ftype=='return'">
			备注:
			<el-input v-model="nowrow.remark"  :rows="2"
		type="textarea"></el-input>
	</div>
		 <el-tabs v-model="activeName"  @tab-change="handleChange">
		  <el-tab-pane v-for="item in orderState"  :name="item.name" :key="item.name">
				  <template #label>
					  {{item.label}}
				  </template>
		  </el-tab-pane>
		 </el-tabs>
		 <div v-if="activeName=='0'">
			<el-table  :data="tableData" style="width: 100%" :border="false">
				<el-table-column  type="expand">
				      <template #default="props">
						  {{ props.row.state }}
				          <el-table :data="props.row.entrylist" :border="true">
				            <el-table-column label="图片" prop="sku" >
								<template #default="scope">
									<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40" class="img-40"   ></el-image>
									<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40" class="img-40"  ></el-image>
								</template>
							</el-table-column>
				            <el-table-column label="SKU" prop="sku" />
				            <el-table-column label="产品名称" prop="mname" />
				            <el-table-column label="需求数量" prop="amount" />
				          </el-table>
				      </template>
				</el-table-column>
				<el-table-column label="主SKU" prop="mainsku" />
				<el-table-column label="订单编号" prop="number" />
				<el-table-column label="订单状态" prop="auditstatus" >
					<template #default="scope">
						<el-tag v-if="scope.row.auditstatus==0" type="info" >未提交</el-tag>
						<el-tag v-if="scope.row.auditstatus==1" type="info" >待组装</el-tag>
						<el-tag v-if="scope.row.auditstatus==2" type="warning" >组装中</el-tag>
						<el-tag v-if="scope.row.auditstatus==3" type="success" >已完成</el-tag>
						<el-tag v-if="scope.row.auditstatus==4" type="danger" >已终止</el-tag>
						<el-tag v-if="scope.row.auditstatus==5" type="danger" >已作废</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="组装数量" prop="amount"/>
				<el-table-column label="仓库" prop="warehousename"/>
				<el-table-column label="操作"   >
					<template #default="scope">
						<el-button v-if="scope.row.auditstatus==1 || scope.row.auditstatus==2" type="primary" @click.stop="cancelAss(scope.row.formid)">作废</el-button>
						<el-button v-else type="primary" disabled>作废</el-button>
					</template>
				</el-table-column>
			</el-table>
		 </div>
		 <div v-else-if="activeName=='1'">
			 <el-table  :data="skutableData" style="width: 100%" :border="false">
				<el-table-column type="expand">
					  <template #default="props">
						  {{ props.row.state }}
						  <el-table :data="props.row.sublist" :border="true">
							<el-table-column label="图片" prop="sku" >
								<template #default="scope">
									<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"  class="img-40"   ></el-image>
									<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"  class="img-40"  ></el-image>
								</template>
							</el-table-column>
							<el-table-column label="SKU" prop="sku" />
							<el-table-column label="子产品名称" prop="mname" />
							<el-table-column label="单位数量" prop="subnumber" />
						  </el-table>
					  </template>
				</el-table-column>
				<el-table-column label="图片" prop="image" >
					 <template #default="scope">
						<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40" class="img-40"   ></el-image>
						<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40" class="img-40"  ></el-image>
					 </template>
				</el-table-column>
				<el-table-column label="SKU" prop="sku" >
					 <template #default="scope">
						 {{scope.row.sku}}  <el-tag type="success" >主SKU</el-tag>
					 </template>
				</el-table-column>	
				<el-table-column label="产品名称" prop="name" />
			 </el-table>
		 </div>
		<div v-else>
			<el-table  :data="localSKUlist" style="width: 100%" :border="false">
				<el-table-column label="图片" prop="sku" >
					<template #default="scope">
						<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40" class="img-40"   ></el-image>
						<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40" class="img-40"  ></el-image>
					</template>
				</el-table-column>
				<el-table-column label="SKU" prop="sku" />
				<el-table-column label="订单编号" prop="number" />
				<el-table-column label="产品名称" prop="mname" />
				<el-table-column label="需求数量" prop="amount" />
			</el-table>
		 </div>
		<template #footer>
			<el-button @click="dialogVisable=false">关闭</el-button>
			<el-button v-if="ftype=='return'" type="primary" @click="handleReturn">驳回</el-button>
		</template>
	
	</el-dialog>
</template>

<script setup>
	import {reactive,toRefs}from"vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import {deleteFrom } from '@/api/erp/assembly/assemblyApi.js'
	const state = reactive({
		dialogVisable:false,
		tableData:[],
		skutableData:[],
		activeName:'0',
		asstitle:"组装信息",
		ftype:"ass",
		remark:"",
		orderState:[
			{label:'采购对应组装订单',name:'0',},
			{label:'关联的主SKU',name:'1',},
			{label:'本SKU组装单占比',name:'2',},
		],
		nowrow:null,
		localSKUlist:[],
	})
	const {
		dialogVisable,
		tableData,
		activeName,
		orderState,
		skutableData,
		asstitle,
		ftype,
		remark,
		nowrow,
		localSKUlist
	}=toRefs(state)
	function show(row,ftype){
		state.nowrow=row;
		if(ftype=="return"){
			state.asstitle="驳回订单";
			state.ftype="return";
		}
		state.dialogVisable =true;
		loadData();
	}
	function loadData(){
		state.tableData=[];
		purchaselistApi.getAssemblyInfo({"id":state.nowrow.id}).then((res)=>{
			if(res.data){
				state.tableData=res.data.assemblyformlist;
				state.skutableData=res.data.assemblysublist;
			}
		});
	}
	function handleReturn(){
		purchaselistApi.purchaseReturn({"ids":state.nowrow.id,"remark":state.nowrow.remark}).then((res)=>{
			if(res.data){
				state.nowrow.auditstatus=0;
				state.nowrow.paystatus=0;
				state.nowrow.inwhstatus=0;
				state.dialogVisable=false;
				ElMessage.success('驳回通过 ');
			}
		});
	}
	function cancelAss(id){
		deleteFrom({"ids":id}).then((res)=>{
			if(res.data.type=="info"){
				ElMessage.success( res.data.msg);
				loadData();
			} 
		});
	}
	function handleChange(val){
		state.activeName = val;
		if(val=='2'){
			state.localSKUlist=[];
			state.tableData.forEach(item=>{
				item.entrylist.forEach(entry=>{
					if(entry.sku==state.nowrow.sku){
						entry.number=item.number;
						state.localSKUlist.push(entry);
					}
				})
			});
		}
		//handleQuery();
	}
	defineExpose({
		show,
	})
</script>

<style scoped="scoped">
	.img-40{
		width: 60px;
		height: 60px;flex: none;
		margin-right: 8px;
	}
</style>