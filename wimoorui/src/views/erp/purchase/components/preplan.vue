<template>
	<template  v-if="type=='rec'">
		<el-space>
		 <el-button :disabled="!preprocessingForm.isrun"  @click="save" type="primary"  >保存</el-button>
		 <el-button :disabled="!preprocessingForm.isrun" type="success" plain @click="showRunPlan" >查看已选</el-button> 
		 <el-button v-if="queryid" type="warning" plain @click="clearQueryPlan">
		 	{{planname}}
		 	<el-icon><Close></Close></el-icon>
		 </el-button>
		 <el-button v-else type="warning" plain @click="showPlan">
		 	{{planname}}
		 </el-button>	
		 <el-button :disabled="!preprocessingForm.isrun" type="danger" plain @click="close" >结束</el-button>
		 <el-button  :disabled="!preprocessingForm.isrun" @click="clearPlan" type="danger"  plain>清空</el-button>
		<div class="font-extraSmall">勾选行之后必须点击“保存当前页勾选”才能加入计划</div>
		</el-space>
		<div class='rt-btn-group'>
			<div   style="padding-right:10px;" class="font-extraSmall">
				 <div>当前计划: {{preprocessingForm.number}} </div>
			     <div style="padding-top:7px">{{preprocessingForm.createtimeformat}}</div>
			</div>
			<el-button tag="div" size="large" @click="gotoShelf" type="primary" plain>
				<div><div>下架</div> 
			<span class="font-extraSmall" v-if="preprocessingForm.isCheckInvTime" :title="dateTimesFormat(preprocessingForm.isCheckInvTime)">已下架</span>
			<span class="font-extraSmall" v-else >未处理</span>
			</div>
			</el-button>		
			<el-button   size="large" @click="downloadPlan" type="warning"  plain>
				<div><div>导出配货单</div>
				<span class="font-extraSmall" v-if="preprocessingForm.isDownTime" :title="dateTimesFormat(preprocessingForm.isDownTime)">已下载</span>
				<span class="font-extraSmall" v-else >未下载</span>
				</div>
				</el-button>
			<el-button  size="large"   @click="showConsumablePlan"  type="primary" plain>
				<div><div>辅料出库</div>
				<span class="font-extraSmall" v-if="preprocessingForm.isOutConsumableTime" :title="dateTimesFormat(preprocessingForm.isOutConsumableTime)">已出库</span>
				<span class="font-extraSmall" v-else >未出库</span>
				</div>
				</el-button>
			<el-button size="large" @click="gotoDispatchForm" type="success" plain>
				<div><div>调库</div>
				<span class="font-extraSmall" v-if="preprocessingForm.isDispatchTime" :title="dateTimesFormat(preprocessingForm.isDispatchTime)">已调库</span>
				<span class="font-extraSmall" v-else >未调库</span>
				</div>
				</el-button>
		</div>
	</template>
	<template v-else>
		<el-space >
			<el-button   :disabled="!preprocessingForm.isrun" @click="save" type="primary"  >保存</el-button>
			 <el-button   :disabled="!preprocessingForm.isrun" type="success" plain @click="showRunPlan" >查看已选</el-button> 
			 <el-button   v-if="queryid" type="warning" plain @click="clearQueryPlan">
			 	{{planname}}
			 	<el-icon><Close></Close></el-icon>
			 </el-button>
			 <el-button    v-else type="warning" plain @click="showPlan">
			 	{{planname}}
			 </el-button>	
			 <el-button  :disabled="!preprocessingForm.isrun" type="danger" plain @click="close" >结束</el-button>
			 <el-button  :disabled="!preprocessingForm.isrun" @click="clearPlan" type="danger"  plain>清空</el-button>
			<div class="font-extraSmall">勾选行之后必须点击“保存当前页勾选”才能加入计划</div>
			</el-space>
			<div class='rt-btn-group'>
		 
				<div   style="padding-right:10px;" class="font-extraSmall">
					 <div>当前计划: {{preprocessingForm.number}} </div>
				     <div style="padding-top:7px">{{preprocessingForm.createtimeformat}}</div>
				</div>
				<el-button tag="div" size="large" @click="gotoShelf" type="primary" plain>
					<div><div>下架</div> 
					<span class="font-extraSmall" v-if="preprocessingForm.isCheckInvTime" :title="dateTimesFormat(preprocessingForm.isCheckInvTime)">已下架</span>
					<span class="font-extraSmall" v-else >未处理</span>
					</div>
				</el-button>		
				<el-button   size="large" @click="downloadPlan" type="warning"  plain>
					<div><div>导出配货单</div>
					<span class="font-extraSmall" v-if="preprocessingForm.isDownTime" :title="dateTimesFormat(preprocessingForm.isDownTime)">已下载</span>
					<span class="font-extraSmall" v-else >未下载</span>
					</div>
					</el-button>
				<el-button  size="large"   @click="showConsumablePlan"  type="primary" plain>
					<div><div>辅料出库</div>
					<span class="font-extraSmall" v-if="preprocessingForm.isOutConsumableTime" :title="dateTimesFormat(preprocessingForm.isOutConsumableTime)">已出库</span>
					<span class="font-extraSmall" v-else >未出库</span>
					</div>
					</el-button>
				<el-button size="large" @click="gotoDispatchForm" type="success" plain>
					<div><div>调库</div>
					<span class="font-extraSmall" v-if="preprocessingForm.isDispatchTime" :title="dateTimesFormat(preprocessingForm.isDispatchTime)">已调库</span>
					<span class="font-extraSmall" v-else >未调库</span>
					</div>
					</el-button>
				<el-button    size="large"  @click="doneAllAssemblyForm" type="primary" plain>
					<div><div>批量组装</div>
					<span class="font-extraSmall" v-if="preprocessingForm.isAssemblyTime" :title="dateTimesFormat(preprocessingForm.isAssemblyTime)">已操作</span>
					<span class="font-extraSmall" v-else >未操作</span>
					</div>
					</el-button>
				<el-button class='ic-btn' title='帮助文档'>
					<help theme="outline" size="16" :strokeWidth="3" />
				</el-button>
			</div>
			
		 
	</template>
	<el-dialog   v-model="dialogVisible" title="查看已有计划" >
			<GlobalTable  ref="globalTableRef"   :defaultSort="defaultSort"  :tableData="tableData"    @loadTable="loadTableData" :stripe="false" >
					  <template #field>
						     <el-table-column prop="number" label="编号"  width="200" >
								 <template #default="scope">
									   <div>{{scope.row.number}}
									   <el-tag v-if="scope.row.isrun" round  effect="light" type="warning">进行中</el-tag>
									   <el-tag v-else round  effect="light" type="success">已完成</el-tag>
									   </div>
									   <div class="font-extraSmall">创建人：{{scope.row.creatorname}}</div>
								 </template>
							 </el-table-column>
							 <el-table-column prop="warehousename" label="仓库"  >
								 <template #default="scope">
									   <div>{{scope.row.warehousename}}</div>
									   <div class="font-extraSmall">创建时间：{{scope.row.createtime}}</div>
								 </template>
							 </el-table-column>
						     <el-table-column prop="operatorname" label="操作人" width="200">
								 <template #default="scope">
										   <div>{{scope.row.operatorname}}</div>
										   <div class="font-extraSmall">操作时间：{{scope.row.opttime}}</div>
								 </template>
							 </el-table-column>
							 <el-table-column prop="operatorname" label="状态"  width="220">
									 <template #default="scope">
										 <el-space    wrap>
											   <el-tooltip :content="scope.row.is_check_inv_time">
												   <el-tag type="warning" plain v-if="scope.row.is_check_inv_time">已下架</el-tag>
												   <el-tag type="warning" v-else>未下架</el-tag>
											   </el-tooltip>
											 <el-tooltip :content="scope.row.is_out_consumable_time">
												   <el-tag type="info" v-if="scope.row.is_out_consumable_time">辅料出库</el-tag>
												   <el-tag  type="info" v-else>辅料未操作</el-tag>
											 </el-tooltip>
											 <el-tooltip :content="scope.row.is_dispatch_time">
												   <el-tag  type="danger" v-if="scope.row.is_dispatch_time">已调库</el-tag>
												   <el-tag  type="danger" v-else>未调库</el-tag>
											 </el-tooltip>
											 <el-tooltip :content="scope.row.is_down_time">
												   <el-tag v-if="scope.row.is_down_time" type="success">已配货</el-tag>
												   <el-tag v-else type="success">未配货</el-tag>
											 </el-tooltip>
											 <el-tooltip v-if="scope.row.ftype==1" :content="scope.row.is_assembly_time">
													   <el-tag v-if="scope.row.is_assembly_time">已组装</el-tag>
													   <el-tag v-else>未组装</el-tag>
											 </el-tooltip>
									    </el-space>
									 </template>
							 </el-table-column>
						     <el-table-column label="操作" width="60" >
								 <template #default="scope">
										 <el-button link type="primary" @click="showPlanItem(scope.row)">查看</el-button>
								 </template>
						  </el-table-column>
						</template>
				</GlobalTable>								
				<template #footer>
					<span class="dialog-footer">
						<el-button @click="dialogVisible = false">关闭</el-button>
					</span>
				</template>
		</el-dialog>
		<Consumable ref="consRef" @change="submitConsumablePlan" :isassembly="true"></Consumable>  
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider,ElMessageBox} from 'element-plus';
	import {Search,ArrowDown,UploadFilled,Close} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import { useRouter } from 'vue-router';
	import preprocessApi from '@/api/erp/purchase/plan/preprocessApi.js';
	import {getCountNum,downloadDetailExcel,downloadFormExcel,downExcelTemp,uploadExcel } from '@/api/erp/assembly/assemblyApi.js';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import dispatchApi from '@/api/erp/inventory/dispatchApi.js';
	import Consumable from '@/views/erp/shipv2/shipment_add/shipstep/components/consumable.vue';
		const router = useRouter();
	const emits  =defineEmits(
		['change','delete','save']
	)
	let props = defineProps({warehouseid:"",type:"",saveData:null})
	const { warehouseid,type,saveData } = toRefs(props);
	const globalTableRef=ref();
	const consRef=ref();
	const state =reactive({
		planname:'查看计划',
		preprocessingForm:{},
		isrunPreprocessingForm:{},
		dialogVisible:false,
		tableData:{records:[],total:0},
		queryid:"",
		defaultSort:{"prop": 'opttime', "order": 'descending' },
	})
	const {
		planname,
		preprocessingForm,
		dialogVisible,
		tableData,
		queryid,
		defaultSort
	}=toRefs(state)
	watch(props,()=>{
		loadRunid(props.warehouseid);
	})
	function save(){
		if(state.preprocessingForm.isrun){
			if(props.saveData){
				var data=props.saveData();
				data.runid=state.preprocessingForm.id;
				preprocessApi.saveItem(data).then((res)=>{
					ElMessage.success("操作成功!");
					emits("save",state.preprocessingForm.id);
				})
			}else{
				    emits("save",state.preprocessingForm.id);
			}
		}else{
			ElMessageBox.confirm('确认要操作已经结束的计划?', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning',
			}).then(() => {
				if(props.saveData){
					var data=props.saveData();
					data.runid=state.preprocessingForm.id;
					preprocessApi.saveItem(data).then((res)=>{
						ElMessage.success("操作成功!");
						emits("save",state.preprocessingForm.id);
					})
				}else{
					    emits("save",state.preprocessingForm.id);
				}
			 }).catch(() => ElMessage.info('已取消'));
		}
		
	}
	function showPlan(){
		dialogVisible.value=true;
		var timer=setTimeout(function(){
			 handleQuery();
			 clearTimeout(timer);
		},300);
	}
	
	function handleQuery(){
		var params={};
		params.warehouseid=props.warehouseid;
		if(props.type=="ass"){
			params.ftype=1;
		}else{
			params.ftype=0;
		}
		globalTableRef.value.loadTable(params);  
	}
	
	function loadTableData(params){
		preprocessApi.getFormList(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		});
	}
 
 function downloadPlan(){
        if(props.type=="rec"){
			preprocessApi.downRecPDFForm({"runid":state.preprocessingForm.id},()=>{
				preprocessApi.update({"formid":state.preprocessingForm.id,"donetype":"down"}).then((res)=>{
					ElMessage.success("操作成功!");
					loadRunid(props.warehouseid);
				});
			}); 
		}else{
			preprocessApi.downPDFAssemblyFormId({"formid":state.preprocessingForm.id},()=>{
				preprocessApi.update({"formid":state.preprocessingForm.id,"donetype":"down"}).then((res)=>{
					ElMessage.success("操作成功!");
					loadRunid(props.warehouseid);
				});
			}); 
		}
 	
 }
 function showConsumablePlan(){
	 var data={};
	 data.warehouseid=state.preprocessingForm.warehouseid;
	 data.number=state.preprocessingForm.number;
	 data.id=state.preprocessingForm.id;
	 data.itemlist=[];
	 var gotoid=state.preprocessingForm.id;
	  data.id=gotoid;
	preprocessApi.getItemsFormConsumable({"formid":gotoid}).then((res)=>{
		 if(res.data && res.data.length>0){
			 res.data.forEach(item=>{
			 	data.itemlist.push({"msku":item.sku,"confirmQuantity":item.amount});
			 })
			 consRef.value.show(null,data);
		 }else{
			 ElMessage.error("没有可提交的内容!");
		 }
	});
 	
 	
 }
	function gotoShelf(){
		var gotoid= state.preprocessingForm.id;
		if(gotoid){
			if(props.type=="rec"){
				router.push({
					path:'/erp/ship/quota',
					query:{
					  recpreprocessingid:gotoid,
					  title:"配货下架",
					  path:'/erp/ship/quota',
					}
				})
			}else{
				router.push({
					path:'/erp/ship/quota',
					query:{
					  asspreprocessingid:gotoid,
					  title:"配货下架",
					  path:'/erp/ship/quota',
					}
				})
			}

			
		}else{
			ElMessage.error("当前没有已选计划进行下架，请选择行并保存或者选择一个计划");
		}
	}
	
	function showPlanItem(row){
		state.queryid=row.id;
		row.createtimeformat=dateTimesFormat(row.createtime);
		row.isAssemblyTime=row.is_assembly_time;
		row.isCheckInvTime=row.is_check_inv_time;
		row.isDispatchTime=row.is_dispatch_time;
		row.isDownTime=row.is_down_time;
		row.isOutConsumableTime=row.is_out_consumable_time;
        row.isAssemblyTime=row.is_assembly_time;
		state.preprocessingForm=row;
	    emits("query",row);
		planname.value=row.createtime+'计划';
		dialogVisible.value=false;
	}
	
	function close(){
		preprocessApi.update({"formid":state.preprocessingForm.id,"donetype":"close"}).then((res)=>{
			ElMessage.success("操作成功!");
		 	emits("change",state.preprocessingForm);
		});
	}
	function submitConsumablePlan(){
		//更新此次list的consumable_form_id
		preprocessApi.update({"formid":state.preprocessingForm.id,"donetype":"consumable"}).then((res)=>{
		    loadRunid(props.warehouseid);
		});
	}
	function clearQueryPlan(){
		state.queryid="";
		state.planname='查看计划';
		state.preprocessingForm=state.isrunPreprocessingForm;
		emits("change",state.preprocessingForm);
	}
	function loadRunid(warehouseid){
		preprocessApi.getRunsForm({"warehouseid":warehouseid,"type":props.type}).then((res)=>{
			if(res.data){
				state.preprocessingForm=res.data;
				state.preprocessingForm.createtimeformat=dateTimesFormat(res.data.createtime);
				state.isrunPreprocessingForm=state.preprocessingForm;
				state.queryid="";
				emits("change",state.preprocessingForm);
			}
		});
	}
	function clearPlan(){
		ElMessageBox.confirm('确认是否清空计划?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
		}).then(() => {
			  preprocessApi.clearRunsForm({"formid":state.preprocessingForm.id}).then((res)=>{
			  	ElMessage.success("清空成功!");
				state.queryid="";
			    emits("change",state.preprocessingForm);
				state.planname='查看计划';
			  });
			}).catch(() => ElMessage.info('已取消'));
	}
	
	function showRunPlan(){
		state.queryid=state.preprocessingForm.id;
		emits("query",state.preprocessingForm);
		planname.value=dateTimesFormat(state.preprocessingForm.createtime)+'计划';
		dialogVisible.value=false;
	}
	
	function gotoDispatchForm(){
		var gotoid= state.preprocessingForm.id;
		router.push({
			path:"/e/w/t",
			query:{
				title:'编辑调库单',
				path:"/e/w/t",
				consformid:gotoid
			},
		})
	}
	
	function doneAllAssemblyForm(){
		var gotoid =state.preprocessingForm.id;
		ElMessageBox.confirm('请确认是否批量组装计划中得的所有组装单?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
		}).then(() => {
			 preprocessApi.doneAssemblyForm({"formid":gotoid}).then(res=>{
			 	preprocessApi.update({"formid":gotoid,"donetype":"assembly"}).then((res)=>{
			 		ElMessage.success("操作成功!");
			 			emits("change",state.preprocessingForm);
			 	});
			 })
			}).catch(() => ElMessage.info('已取消'));
		
		
	}
	function show(){
		
	}
	defineExpose({
		show
	});
</script>

<style>
</style>