<template>
	<el-dialog title="发货记录(请注意:[确认]后需要[保存]才能生效)" 
	           v-model="dialog.visible"
	           :append-to-body="true" 
			   >
			   <el-row :gutter="20">
			       <el-col :span="12">
					   <el-space>
					   <el-image :src="parentrow.image"  style="height:60px;width:60px;"></el-image>
				  	    <div>
							<div class="p-name padding">{{parentrow.name}}</div>
							<div class="bottom-font"><span class="text-blue">{{itemrow.sku}}</span>({{itemrow.marketname}})</div>
						</div>
					  </el-space> 
				   </el-col>
			       <el-col :span="6" style="float:right">
					   <div class="padding">{{itemrow.needship}}</div>
				       <div class="font-extraSmall bottom-font">建议发货量</div>
				   </el-col>
			        <el-col :span="6" style="float:right">
			            <div class="padding">{{itemrow.amount}}</div>
			            <div class="font-extraSmall bottom-font">建议发货量</div>
			        </el-col>
			     </el-row>
		 
				 <table class="split-table">
					 <thead>
						 <tr>
							 <th>发货渠道</th>
							 <th>发货数量</th>
							 <th>发货仓库</th>
							 <th>操作</th>
						 </tr>
					</thead>
					<tbody>
						<tr v-for="(row,index) in tableData">
															 <td>
																 <el-select class="in-wi-24" 
																            v-model="row.transtype" 
																			placeholder="请选择">
																 	<el-option  v-for="item in transtypeOptions"   
																	:key="item.id"  
																	:label="item.name" 
																	:value="item.id"    />
																 </el-select>
															 </td>
															 <td>
																 <el-input-number  
																 style="width:80px;" 
																 :controls="false"  
																 v-model="row.amount"
																 @input="row.amount=CheckInputInt(row.amount)"
																 ></el-input-number>
															 </td>
															 <td>
																 <el-select class="in-wi-24" 
																            v-model="row.overseaid" 
																            placeholder="请选择">
																 	<el-option  v-for="item in overseaOptions"   
																	            :key="item.id"  
																				:label="item.name" 
																				:value="item.id"    />
																 </el-select>
															 </td>
															 <td>
															    <el-button @click="handleDelete(index)"
																      type="primary" link > 
																   删除</el-button>
															 </td>
						</tr>
					</tbody>
					<tfoot class="table-footer">
						<tr><td colspan="4">
						<div class="pointer"  @click="handleAdd"><el-icon><Plus /></el-icon> 添加</div>
						</td></tr>
					</tfoot>
				 </table>
			 <template #footer>
			 	<span class="dialog-footer">
			 		<el-button @click="dialog.visible = false">取消</el-button>
			 		<el-button type="primary" @click="submitFunc">确认</el-button>
			 	</span>
			 </template>
		  
			
		</el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted}from"vue"
	import {Close,Plus} from '@element-plus/icons-vue';
	import * as echarts from 'echarts';
	import shipmentApi from "@/api/amazon/inbound/shipmentApi.js";
	import {dateFormat,getDateValue,CheckInputInt} from "@/utils/index.js";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import planApi from '@/api/erp/ship/planApi.js';
	const state=reactive({
		    dialog:{visible:false},tableData:[],loading:false,parentrow:[],
			transtypeOptions:[],overseaOptions:[],itemrow:{},
	})
	const {
	        dialog,tableData,loading,parentrow,transtypeOptions,overseaOptions,itemrow,
	} = toRefs(state);
	const emit = defineEmits(['change']);
	function handleAdd(){
		if(state.tableData.length>0){
		   var row= JSON.parse(JSON.stringify(state.tableData[state.tableData.length-1])); 
			state.tableData.push(row);
		}else{
			state.tableData.push(state.itemrow);
		}
		
	}
	function handleDelete(index){
		state.tableData.splice(index,1);
	}
	function submitFunc(){
		var set=[];
		var hasreapt=false;
		state.tableData.forEach(row=>{
		   var key=row.transtype+row.overseaid;
		   if(!row.transtype){
			    hasreapt=true;
				return;
		   }
		   if(set.includes(key)){
			   hasreapt=true;
		   }else{
			  set.push(key); 
		   }
		});
		if(hasreapt){
			ElMessage.warning('确认失败，请保证运输放方式不能相同且不能为空！');
			return ;
		}
		state. dialog.visible=false;
		emit("change",state.tableData,state.itemrow,state.parentrow);
	}
	function show(itemrow,parentrow,transtypeOptions){
		state.dialog.visible=true;
		state.itemrow=itemrow;
		var row=JSON.parse(JSON.stringify(itemrow));
		planApi.subsplit(row).then(res=>{
			if(res.data.length>0){
				res.data.forEach(splititem=>{
					if(!splititem.transtype){
						splititem.transtype="";
					}
					if(splititem.overseaid==null
					||splititem.overseaid==undefined
					||splititem.overseaid==""
					||splititem.overseaid=="null"){
						splititem.overseaid="";
					}
				})
				state.tableData=res.data;
			}else{
				if(!row.transtype){
					row.transtype="";
				}
				if(row.overseaid==null
				||row.overseaid==undefined
				||row.overseaid==""
				||row.overseaid=="null"){
					row.overseaid="";
				}
				state.tableData=[];
				state.tableData.push(row);
			}
		})
		state.parentrow=parentrow;
		state.transtypeOptions=JSON.parse(JSON.stringify(transtypeOptions));
		 loadOverSeaWarehouse(itemrow);
	}
	
	function loadOverSeaWarehouse(itemrow){
		   warehouseApi.getOversaWarehouse({ftype:"oversea_usable",groupid:itemrow.groupid,country:itemrow.country}).then((res)=>{
		   		res.data.push({"id":"","name":"FBA仓"})
		   	    state.overseaOptions=res.data;
			});
	}
	defineExpose({show});
</script>

<style scoped="scoped">
	.badShip{
		 background-color: #fff3ec;
	}
	.p-name{
		width:260px;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
	.padding{
		font-size:20px;
		padding-bottom:10px;
	}
	.bottom-font{
		font-size:14px;
		
	}
	.split-table{
		margin-top:20px;
		width:100%;
	    border:solid 1px #dedede;
		border-color:#dedede;
		border-collapse: collapse;
		min-height:300px;
	}
	.split-table thead th{
		background-color:#f5f7fa;
		border:solid 1px #dedede;
		padding-top:10px;
		padding-bottom:10px;
		border-color:#dedede;
	}
	.split-table td{
		 padding-top:5px;
		 padding-bottom:5px;
		 text-align: center;
	}
	 .table-footer div{
		 margin:15px;
		 padding:15px;
		 border:solid 1px #dedede;
	 }
</style>