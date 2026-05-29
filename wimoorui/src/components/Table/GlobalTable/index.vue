<template>
	  <el-table v-if="tableData"
	    :data="tableData.records" 
		:id="uuid"
		ref="globalTableRef"
	    v-loading="loading" 
		element-loading-text="加载中..."
		:default-sort="defaultSort"
	    :stripe="stripe" 
		:border="border"
		:height="height"
		:show-header="showHeader"
		:show-summary="showSummary"
		:summary-method="summaryMethod"
		:span-method="spanMethod"
	    :row-class-name="rowClassName"
		:cell-class-name="cellClassName"
		:scrollbar-always-on="scrollbarAlwaysOn"
		:lazy="lazy"
		:load="load"
		:tree-props="treeProps"
		:row-key="rowKey"
		:highlight-current-row="highlightCurrentRow"
		:default-expand-all="defaultExpandAll"
		@cell-click="cellClick"
		@row-click="rowClick"
	    @sort-change="tableSort" 
		@expand-change="expandChange"
		@selection-change='selectChange'  
		@current-change="currentChange"
	     style="width: 100%;margin-bottom:16px;">
		 		 <template #empty>
		 			 <div class="empty-wrapper">
		 			 <el-image :src="$require('nodata.png')"></el-image>
		 			 <div class="emptytext">暂无数据！</div>
		 			 </div>
		 		 </template>
		  <slot  name="field" ref="slotRef" :isshow="isShow"  :list="whiteList"></slot>
	  </el-table>
	  
	  <el-table v-else
	         :id="uuid"
	         :data="mTableData.records" 
	  		 ref="globalTableRef"
	         v-loading="loading" 
			 element-loading-text="加载中..."
	  		:default-sort="defaultSort"
	        :stripe="stripe" 
	  		:border="border"
	  		:height="height"
			:lazy="lazy"
			:load="load"
			:show-header="showHeader"
			:tree-props="treeProps"
	  		:show-summary="showSummary"
	  		:summary-method="summaryMethod"
			:span-method="spanMethod"
			:row-key="rowKey"
			:row-class-name="rowClassName"
			:cell-class-name="cellClassName"
			:highlight-current-row="highlightCurrentRow"
			:default-expand-all="defaultExpandAll"
			@row-click="rowClick"
	        @sort-change="tableSort" 
			@expand-change="expandChange"
	  		@selection-change='selectChange'  
			@current-change="currentChange"
	         style="width: 100%;margin-bottom:16px;">
	  		<slot  name="field"  ref="slotRef" :isshow="isShow" :list="whiteList"></slot>
	  </el-table>
	  <el-pagination background v-if="inDialog&&tableData&&!nopage"  layout="total, sizes, prev, next, jumper"   :total="tableData.total"
	  :page-sizes="pagesizes&&pagesizes.length>0?pagesizes:[10, 20, 50, 100]"  :page-size="pagesize" style='margin-left:auto' 
	  :current-page="currentPage"  @size-change="handleSizeChange"   @current-change="handleCurrentChange">
	  </el-pagination>
	  <el-pagination background v-else-if="tableData&&!nopage"  layout="total, sizes, prev, pager, next, jumper"   :total="tableData.total"
	  :page-sizes="pagesizes&&pagesizes.length>0?pagesizes:[10, 20, 50, 100]"  :page-size="pagesize" style='margin-left:auto' 
	  :current-page="currentPage"  @size-change="handleSizeChange"   @current-change="handleCurrentChange">
	  </el-pagination>
	  <el-pagination background v-else-if="inDialog&&!nopage"   layout="total, sizes, prev, next, jumper"   :total="mTableData.total"
	  :page-sizes="pagesizes&&pagesizes.length>0?pagesizes:[10, 20, 50, 100]"  :page-size="pagesize" style='margin-left:auto' 
	  :current-page="currentPage"  @size-change="handleSizeChange"   @current-change="handleCurrentChange">
	  </el-pagination>
	  <el-pagination background v-else-if="!nopage"  layout="total, sizes, prev, pager, next, jumper"   :total="mTableData.total"
	  :page-sizes="pagesizes&&pagesizes.length>0?pagesizes:[10, 20, 50, 100]"  :page-size="pagesize" style='margin-left:auto' 
	  :current-page="currentPage"  @size-change="handleSizeChange"   @current-change="handleCurrentChange">
	  </el-pagination>
</template>
<script setup>
  import { reactive,ref,watch,onMounted,toRefs,nextTick} from "vue";
  import { ElMessage,ElLoading } from 'element-plus';
  import { useRoute,useRouter } from 'vue-router';
  import queryFieldApi from '@/api/sys/tool/queryFieldApi.js';
  import {tableHeaderFloat,screenToTop,checkVisiable} from '@/utils/jquery/table/float-header.js';
  import {guid} from '@/utils/index.js';
  	let props = defineProps({
  	 	                       tableData:{records:[],total:""},
							   queryParams:{},
							   pagesizes:[],
		                       inDialog:"",
							   inTable:"",
							   defaultSort:{}, 
							   stripe:"",
							   border:"",
							   size:"",
							   fieldType:"",
							   lazy:undefined,
							   nopage:undefined,
							   height:undefined,
							   load:undefined,
							   treeProps:undefined,
							   showSummary:"",
							   showHeader:undefined,
							   summaryMethod:"",
							   spanMethod:"",
							   rowKey:undefined,
							   rowClassName:undefined,
							   cellClassName:undefined,
							   highlightCurrentRow:undefined,
							   defaultExpandAll:undefined,
							   scrollbarAlwaysOn:{
								   type:Boolean,
								   default:false,
							   },
  	                       });
	const { tableData,inDialog,inTable,nopage,defaultSort,size,queryParams,lazy,load,treeProps,showHeader,fieldType,scrollbarAlwaysOn,pagesizes,
	        stripe,border,height,showSummary,summaryMethod,spanMethod,rowKey,rowClassName,cellClassName,highlightCurrentRow,defaultExpandAll} = toRefs(props);
	const emit = defineEmits(['loadTable',"selectionChange","row-click","expandChange","currentChange"]);
	let globalTableRef=ref();
	const state = reactive({
		              whiteList:[],
					  pagesize:10  ,
					  currentPage: 1 ,
					  mTableData:{records:[],total:""},
					  mQueryParams: {pagesize:10,currentpage:1} ,
					  loading: false ,
					  uuid:guid(),
					});
	const {
	  whiteList,
	  uuid,
	  pagesize,
	  currentPage,
	  mTableData,
	  mQueryParams,
	  loading,
	} = toRefs(state);
    const slotRef=ref();
	 let route = useRoute();
	 let router = useRouter();
  		function handleSizeChange(size){
			state.pagesize=size;
			state.mQueryParams.pagesize=size;
			state.mQueryParams.currentpage=1;
			state.currentPage=1;
			refreshTable();
  		}
 
	   function tableSort(option){
			  state.mQueryParams.sort=option.prop;
			  var order=option.order.replace("ascending","asc");
			  order=order.replace("descending","desc");
			  state.mQueryParams.order=order;
			  state.mQueryParams.currentpage=1;
			  state.currentPage=1;
			  refreshTable();
		}
  		function handleCurrentChange(page){
			state.currentPage=page;
			state.mQueryParams.currentpage=page;
			refreshTable();
  		}
		function currentChange(currentrow,oldrow){
			 emit("currentChange",currentrow,oldrow ) ;
		}
		function setCurrentRow(row){
			globalTableRef.value.setCurrentRow(row);
		}
		const callback =function(res){
			     if(props.tableData){
					 if(res&&res["records"]&&res["total"]){
							   props.tableData.records=res.records;
							   props.tableData.total=res.total;
					 }else if(res&&res["data"]&&res["data"]["records"]&&res["data"]["total"]){
							  props.tableData.records=res.data.records;
							  props.tableData.total=res.data.total;
					 }else{
							  props.tableData.records=[];
							  props.tableData.total=0; 
					 }
				 }else{
					 if(res&&res["records"]&&res["total"]){
							   state.mTableData.records=res.records;
							   state.mTableData.total=res.total;
					 }else if(res&&res["data"]&&res["data"]["records"]&&res["data"]["total"]){
							  state.mTableData.records=res.data.records;
							  state.mTableData.total=res.data.total;
					 }else{
							  state.mTableData.records=[];
							  state.mTableData.total=0; 
					 }
				 }
				 state.loading=false;
		}
		function refreshTable(){
			 if(!state.loading){
			      state.loading=true;
			 }
			 state.mQueryParams.loadType="refreshTable";
			 if(!props.defaultExpandAll&&globalTableRef.value.toggleRowSelection){
				 if(props.tableData&&props.tableData.records){
					 props.tableData.records.forEach(row=>{
						 globalTableRef.value.toggleRowSelection(row,false);
					 })
				 }
				 if(state.mTableData&&state.mTableData.records){
					 state.mTableData.records.forEach(row=>{
					 	 globalTableRef.value.toggleRowSelection(row,false);
					 })
				 }
			 }
			 emit("loadTable", state.mQueryParams,callback) ;
			 if(!props.height){
			     screenToTop();
			   }
		}
		
		function loadTable(param){
			 if(!state.loading){
			      state.loading=true;
			 }
			 state.mQueryParams.currentpage=1;
			 state.currentPage=1;
			 if(param){
			    state.mQueryParams=Object.assign(state.mQueryParams, param);
			 }
			 state.mQueryParams.loadType="loadTable";
			 emit("loadTable",state.mQueryParams,callback ) ;
			 if(!props.height){
			     screenToTop();
			   }
		
		}
		function changeSize(size){
			if(size){
				 state.pagesize=size;
				 state.mQueryParams.pagesize=size;
			}
		}
		function toggleRowSelection(row, selected){
			globalTableRef.value.toggleRowSelection(row,selected);
		}
		function selectChange(selection){
			  emit("selectionChange",selection) ;
			  emit("selection-change",selection) ;
		}
		function toggleRowExpansion(row,isexpend){
			globalTableRef.value.toggleRowExpansion(row,isexpend);
		}
		function getSelectionRows(){
			return globalTableRef.value.getSelectionRows();
		}
		function expandChange(row, expandedRows){
			 emit("expandChange",row, expandedRows) ;
		}
		function rowClick(row, column, event){
			  emit("row-click",row, column, event) ;
		}
		function refreshField(){
				if(props.fieldType){
					   queryFieldApi.getVersionFieldByUserQueryName({"queryname":props.fieldType}).then(res=>{
						if(res.data && res.data.length>0){
							res.data.sort(function(a,b){
								if(a.findex<b.findex){  return -1; }
								if(a.findex>b.findex){  return 1;  }
								return 0;
							});
							var selectfield=[];
							var allfield=[];
							res.data.forEach(item=>{
								var fielddata={prop:item.ffield,label:item.title,width:item.width,tooltip:item.titleTooltip};
								 if(item.sortable){
									 fielddata.sortable="custom";
								 }else{
									 fielddata.sortable=false;
								 }
								 if(item.isShow){
										selectfield.push(fielddata);
								 }
								 allfield.push(fielddata);
							});
							
							if(selectfield.length==0){
								state.whiteList=allfield;
							}else{
								state.whiteList=selectfield;
							}
						}else{
							 state.whiteList=[];
						}
						doLayout();
					});
				}
		}
		const isShow=function(fieldname){
			if(state.whiteList&&state.whiteList.length>0){
				if(state.whiteList.includes(fieldname)){
					return true;
				}else{
					return false;
				}
			}else{
				return true;
			}
		}
	    refreshField();
		function doLayout(){
			globalTableRef.value.doLayout();
		}
		
		function setScrollLeft(val){
			globalTableRef.value.setScrollLeft(val);
		}
		function setScrollTop(val){
			globalTableRef.value.setScrollTop(val);
		}
		 onMounted(async () => {
			 if(props.size){
				 state.pagesize=props.size;
				 state.mQueryParams.pagesize=props.size;
			 }
			 if(props.defaultSort){
			 	state.mQueryParams.sort=props.defaultSort.prop;
			 	state.mQueryParams.order=props.defaultSort.order=="ascending"?"asc":"desc";
			 }
			 if(props.inDialog){
			     refreshTable();
			 }
		 })
		 defineExpose({ loadTable,refreshTable,refreshField,handleCurrentChange,
		 toggleRowExpansion,toggleRowSelection,setCurrentRow,getSelectionRows,
		 doLayout,changeSize,tableSort,setScrollLeft,setScrollTop })
onMounted(()=>{
 watch(props.queryParams,(val)=>{
			loadTable(props.queryParams);
		 });
		 watch(route,()=>{ 
			 if(!props.height&&!props.inTable){
			 	 tableHeaderFloat(state.uuid);
			  }
		 })
		 watch(() =>router.currentRoute.value.query,(newValue,oldValue)=> {
		       if(newValue&&newValue['refresh']){
							 setTimeout(()=>{
								 if(checkVisiable(state.uuid)){
							 	         loadTable(props.queryParams);
								 }
							 },100);
		 			  }
		       },{ immediate: true });
		 watch(props.tableData,(val)=>{
			       if(state.loading){
		 		        state.loading = false;
				   }
				  if(!props.height&&!props.inTable){
					  tableHeaderFloat(state.uuid);
		           }
			});
})
		
</script>

<style>
	.floathead{
		position:fixed;
		z-index:100;
		top:34px;
	}
	.floatfooter {
		position:fixed;
		z-index:100;
		bottom:5px;
	}
		.emptytext{
			font-size: 14px;
			color: rgba(0, 0, 0, 0.35);
			margin-top: 8px;
		}
		.empty-wrapper{
			padding-top:16px;
			padding-bottom:16px;
			line-height: normal;
		}
</style>
