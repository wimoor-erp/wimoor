<template>
	<div :class="isConsumable=='ok'?'':'main-sty'">
		<div class="con-header">
	    	<Header ref="headerRef" @change="handleQuery" @delete="handleDelete" :isConsumable="isConsumable"
			 @downloadPDF="handleDownloadPDF" @goshelf="gotoShelf" @dispatch="gotoDispatch" />
		</div>
		<div class="con-body">
			<GlobalTable  :height="isConsumable=='ok'?'calc(100vh - 343px)':'calc(100vh - 300px)'" 
					     :queryParams="queryParams"
					     @loadTable="loadtableData" 
						 ref="tableRef"
						 :defaultSort="{ prop: 'opttime', order: 'descending' }"  
					     @selectionChange="handleSelectionChange" >
				<template #field>
					<el-table-column type="selection" width="40"/>
				<el-table-column label="加工单编码" prop="number" width="150">
					<template #default="scope">
						<div>{{scope.row.number}}</div>
						<div v-if="!queryParams.auditstatus">
							<el-tag type="primary"  v-if="scope.row.auditstatus=='2' || scope.row.auditstatus=='1'"
							effect="plain"> 处理中</el-tag>
							<el-tag type="success"  v-if="scope.row.auditstatus=='3'"
							effect="plain"> 已完成</el-tag>
							<el-tag type="danger"  v-if="scope.row.auditstatus=='4'"
							effect="plain"> 已终止</el-tag>
							<el-tag type="info"  v-if="scope.row.auditstatus=='5'"
							effect="plain"> 已作废</el-tag>
						</div>
					</template>
				  </el-table-column>
					
				 <el-table-column  prop="image" label="图片" width="65" >
					<template #default="scope">
					<el-image v-if="scope.row.image" :src="scope.row.image" class="product-img" ></el-image>
					<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
				   </template>
				 </el-table-column>
				 <el-table-column label="名称/SKU"  >
					<template #default="scope">
						<el-tooltip :show-after="100" placement="top" :content="scope.row.name">
							 <div class='name text-omit-1'>{{scope.row.name}}</div>
						</el-tooltip>
					  
					   <div class='sku'>{{scope.row.sku}}</div>
				   </template>
				 </el-table-column>
				<el-table-column label="操作仓库" prop="wname"/>
				<el-table-column label="单据类型" width="100" prop="puramount" sortable="custom">
					<template #default="scope">
						<el-tag :type="success" v-if="scope.row.ftype=='ass'" effect="plain">
						组装单</el-tag>
						<el-tag :type="primary" v-else effect="plain">
						拆分单</el-tag>
						<el-icon style="margin-left: 5px;" title="不存在于采购规划" v-if="scope.row.puramount<0"><WarningFilled /></el-icon>
					</template>
				</el-table-column>
				<el-table-column label="加工数量" prop="amount" width="140" sortable="custom">
					<template #default="scope">
						 <div>{{scope.row.amount}}</div>
						 <div v-if="scope.row.amount==scope.row.amount_handle"><el-tag effect="plain" round size="small" type="success">已完成</el-tag></div>
						 <div v-else-if="scope.row.amount_handle==0"><el-tag  effect="plain"  round size="small" type="danger">未开始</el-tag></div>
						  <div v-else><span class="font-extraSmall">待加工：</span>{{scope.row.amount-scope.row.amount_handle}}</div>
					</template>
					</el-table-column>
			
				<el-table-column  label="采购需求量" 
								  v-if="queryParams.auditstatus=='2'&&queryParams.operate=='false'"
								  prop="needin" 
								  width="110" 
								  sortable="custom">
					<template #default="scope">
						<div>
						<span v-if="scope.row.needin">{{scope.row.needin}}</span>
						<span v-else>0</span>
						</div>
						 <el-tag type="danger"  size="small" v-if="scope.row.needin>0">需要采购</el-tag>
					</template>
				</el-table-column>
				<el-table-column v-else label="可处理数量" prop="hasqty" width="110" sortable="custom">
					<template #default="scope">
						<div>
						<span v-if="scope.row.hasqty">{{scope.row.hasqty}}</span>
						<span v-else>0</span>
						</div>
						 <el-tag type="danger"  size="small" v-if="scope.row.needin>0">需要采购</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="创建人" prop="creator" width="90" sortable="custom"/>
				<el-table-column label="操作时间" prop="opttime"  width="100" sortable="custom">
					<template #default="scope">
						<span>{{dateFormat(scope.row.opttime)}}</span>
					</template>
				</el-table-column>
				<el-table-column label="关联货件" prop="shipment">
				<template #default="scope">
					<span>{{scope.row.shipmentids}}</span>
					<div v-if="scope.row.shipmentids">
						<el-tag type="danger" size="small" effect="dark">优先组装</el-tag>
					</div>
				</template>
				</el-table-column>
				<el-table-column label="备注" prop="remark"/>
				<el-table-column  fixed="right"  label="操作" width="120">
					<template #default='scope'>
					<el-space>
						<el-button @click="handleDownload(scope.row)" type="primary" link>下载</el-button>
						<el-button @click="handleDetail(scope.row)" type="primary" link>详情</el-button>
					  </el-space>	
					</template>
				</el-table-column>
				</template>
			</GlobalTable>
		</div>
	</div>
</template>
<script>
    export default{ name:"加工单" };
</script>
<script setup>
	import Header from"./components/header.vue"
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram,WarningFilled} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'
	import {useRouter } from 'vue-router'
	import {listForm,downloadInfoWord,deleteFrom } from '@/api/erp/assembly/assemblyApi.js'
	import {dateFormat,CheckInputInt} from "@/utils/index.js";
	import preprocessApi from '@/api/erp/purchase/plan/preprocessApi.js';
	const router = useRouter()
	const headerRef= ref()
	const state = reactive({
		queryParams:{},
		isShow:false,
		tableData:[],
		selectRows:[],
	})
	const {
		queryParams,
		isShow,
		tableData,
		selectRows
	}=toRefs(state)
	let props = defineProps({ isConsumable:"" })
    const { isConsumable } = toRefs(props);
	const tableRef=ref();
	function gotoShelf(){
		if(state.selectRows && state.selectRows.length>0){
			var assemblyids=[];
			var isok=true;
			if(state.queryParams.warehouseid==undefined || state.queryParams.warehouseid==null || state.queryParams.warehouseid==""
				|| state.queryParams.warehouseid=="null"){
				isok=false;
			} 
			state.selectRows.forEach(item=>{
				assemblyids.push(item.id+"");
				if(item.auditstatus!=2){
					isok=false;
				}
			});
			if(isok){
				router.push({
					path:'/erp/ship/quota',
					query:{
					  assemblys:assemblyids,
					  title:"加工单下架",
					  path:'/erp/ship/quota',
					}
				})
			}else{
				ElMessage.error("请选择具体仓库和处理中的数据行！");
			}
			
		}else{
			ElMessage.error("请选择数据行");
		}
	}
	
	function gotoDispatch(){
		if(state.selectRows && state.selectRows.length>0){
			var assemblyids=[];
			var isok=true;
			if(state.queryParams.warehouseid==undefined || state.queryParams.warehouseid==null || state.queryParams.warehouseid==""
				|| state.queryParams.warehouseid=="null"){
				isok=false;
			} 
			state.selectRows.forEach(item=>{
				assemblyids.push(item.id+"");
				if(item.auditstatus!=2 && item.auditstatus!=3){
					isok=false;
				}
			});
			if(isok){
				router.push({
					path:"/e/w/t",
					query:{
						title:'编辑调库单',
						path:"/e/w/t",
						assemblyids:assemblyids
					},
				})
			}else{
				ElMessage.error("请选择具体仓库和处理中/已完成的数据行！");
			}
			
		}else{
			ElMessage.error("请选择数据行");
		}
	}
	
	function loadtableData(params,callback){
		
		listForm(params).then(res=>{
			var selectRows=[];
			state.tableData=res.data.records;
			if(res.data.records){
				res.data.records.forEach(row=>{
					if(row.hasqty){
						row.hasqty=parseInt(row.hasqty);
					}
					if(row.check_inv&&row.check_inv==params.runid){
						selectRows.push(row);
					}
				})
			}
			callback(res.data);
			state.selectRows=selectRows;
			headerRef.value.setSelectRows(state.tableData,state.selectRows);
			var mytimer=setTimeout(function(){
				if(selectRows&&selectRows.length>0){
					var tableselect=tableRef.value.getSelectionRows();
					var selectids=[];
					tableselect.forEach(item=>{
						selectids.push(item.id);
					})
					selectRows.forEach(item=>{
						if(selectids&&selectids.length>0&&!selectids.contains(item.id)){
						   tableRef.value.toggleRowSelection(item,true);
						}else{
							tableRef.value.toggleRowSelection(item,true);
						}
					})
				}
				clearTimeout(mytimer);
			},500);
		})
	}
	function handleQuery(params){
		if(params){
			state.queryParams.warehouseid=params.warehouseid;
			state.queryParams.auditstatus=params.auditstatus;
			state.queryParams.search=params.search;
			state.queryParams.searchtype=params.searchtype;
			state.queryParams.ftype=params.ftype;
			state.queryParams.runid=params.runid;
			state.queryParams.preprocessingid=params.preprocessingid;
			state.queryParams.isplan=params.isplan;
			if(params.auditstatus=="1" || params.auditstatus=="2"){
				state.queryParams.operate=params.operate;
			}else{
				state.queryParams.operate=undefined;
			}
			if(params.auditstatus=="1"||params.auditstatus=="2"){
					state.queryParams.fromDate=undefined;
					state.queryParams.toDate=undefined;
				}else{
					state.queryParams.fromDate=params.fromDate;
					state.queryParams.toDate=params.toDate;
				}
			
		}
		state.queryParams.searchSession=new Date();
	}
	function handleDownload(row){
		downloadInfoWord({formid:row.id});
	}
	function handleDetail(row){
		router.push({
			path:"/e/p/p/d",
			query:{
				title:'加工单详情',
				path:"/e/p/p/d",
				id:row.id
			},
		})
	}
	function getState(val){
		if(val=='可处理'){
			state.isShow = true
		}else{
			state.isShow = false
		}
	}
	function tabState(val){
		if(val=='2'){
			state.isShow = true
		}else if(val=='1'){
			if(headerRef.value.state.radio=='可处理'){
				state.isShow = true
			}else{
				state.isShow = false
			}
		}else{
			state.isShow = false
		}
	}
	function handleSelectionChange(rows){
		state.selectRows=rows;
		headerRef.value.setSelectRows(state.tableData,state.selectRows);
	}
	function handleDelete(){
		if(state.selectRows && state.selectRows.length>0){
			ElMessageBox.confirm('确认作废单据吗?', '警告', {
			 				confirmButtonText: '确定',
			 				cancelButtonText: '取消',
			 				type: 'warning',
			 })
			.then(() => {
			   var ids="";
			   state.selectRows.forEach(function(item){
			   	ids+=(item.id+",");
			   });
			   deleteFrom({"ids":ids}).then((res)=>{
			   	if(res.data.type=="info"){
			   		ElMessage.success(res.data.msg);
					handleQuery();
					headerRef.value.show();
			   	} 
			   });
			})
			.catch(() => ElMessage.info('已取消操作'));
			
		}
	}
	
	function handleDownloadPDF(){
		if(state.selectRows && state.selectRows.length>0){
			var ids=[];
			state.selectRows.forEach(item=>{
				ids.push(item.id);
			});
			preprocessApi.downPDFAssemblyForm(ids);
		}else{
			ElMessage.error("请选择数据行");
		}
		
		
	}
	onMounted(() => {
		handleQuery();
	});
</script>

<style>
</style>
