<template>

	<div class="mysql">
		<div class="con-header">
			<el-tabs v-model="queryParams.ftype"  class="demo-tabs" @tab-change="handleQuery">
			  <el-tab-pane label="正品仓" :name="props.ftype+'_usable'"></el-tab-pane>
			  <el-tab-pane label="测试仓" v-if="props.ftype!='oversea'" :name="props.ftype+'_test'" ></el-tab-pane>
			  <el-tab-pane label="废品仓" v-if="props.ftype!='oversea'" :name="props.ftype+'_unusable'" ></el-tab-pane>
			</el-tabs>
		    <el-row>
				<el-space>
					<el-button type="primary" class="im-but-one" @click.stop="handleAdd">
					  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
					  <span>添加仓位</span>
					</el-button>
					<el-button @click.stop="handleDelete"  :disabled="deleteDisable">删除</el-button>
					<el-switch @change="showHandle"  v-model="withouthide"   inline-prompt  active-text="未删除"
						inactive-text="已删除" />
					<el-input  v-model="queryParams.search" @input="handleQuery" placeholder="仓位名称" style="width: 250px;" class="input-with-select" >
						<template #suffix>
						  <el-icon style="font-size:16px;" class="el-input__icon" @click="handleQuery"><search/></el-icon>
					   </template> 
					</el-input>
				</el-space>
		    </el-row>
			
		</div>

		<div class="con-body">
			<global-table ref="globalTableRef"
						  :tableData="tableData"  
						  :height="props.ftype!='oversea'?'calc(100vh - 240px)':'calc(100vh - 310px)'" 
						  @loadTable="loadtableData" 
						  :defaultSort="{ prop: 'findex', order: 'ascending' }"  
						  @selectionChange="handleSelectionChange"
						  :stripe="true"  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
			        <el-table-column type="selection" width="55" align="center" />
			        <el-table-column prop="number" label="仓位编码" width="145"> 
					   <template #default="scope">
						   <div> {{scope.row.number}}</div>
						   <div class="font-extraSmall">   排序号:{{scope.row.findex}}</div>
					   </template> 
					</el-table-column>
					<el-table-column  prop="name" label="仓位名称"  width="345">
					    <template #default="scope">
							<el-space>
					    <span>{{scope.row.name}}</span>   <el-icon v-if="scope.row.isbind==true" style="font-size:14px;color:#67C23A" title="已绑定"><InfoFilled /></el-icon>
						    	<el-tag
								 v-if="scope.row.isdefault"
								type="warning" effect="plain">默认仓位</el-tag>
					           <el-tag v-else type="info" @click="handleDefault(scope.row)" class="row-tag-hover pointer" >设为默认</el-tag>
							   </el-space>
							   <div v-if="scope.row.country">
								   <span class="font-extraSmall">国家：</span>{{scope.row.country}}
							   <span v-if="scope.row.groupname" class="font-extraSmall">店铺：{{scope.row.groupname}}</span>
							   </div>
					   </template>
					   </el-table-column>
			 	    <el-table-column   label="支持负数" width="80">
			 	     <template #default="scope">
					  <span  v-if="scope.row.ishungry" >是</span>
					  <span  v-else  >否</span>
			 	   </template>
			 	   </el-table-column>
				    <el-table-column prop="address" label="仓库地址" show-overflow-tooltip >
					<template #default="scope">
								 <div>{{scope.row.addressid}}</div>
					</template>
					</el-table-column>
			       <el-table-column prop="remark" label="备注" show-overflow-tooltip  />
			 	   <el-table-column prop="operator" label="操作人"  width="100"/>
			 	   <el-table-column prop="opttime" label="操作时间"   width="100"/>
			       <el-table-column label="操作" align="center" width="140">
			        <template #default="scope"> 
							<el-space>
					  <div> <el-button type="primary" v-if="scope.row.disabled!=true"   link @click="handleUpdate(scope.row)">编辑</el-button>
					  </div>
			          <div><el-button  type="primary" v-if="scope.row.disabled!=true"  link @click="handleDelete(scope.row)">删除</el-button>
					
					  </div>
					  <div><el-button  type="primary" v-if="scope.row.disabled==true"  link @click="handleRecover(scope.row)">恢复</el-button>
					  </div>
					   </el-space>
			        </template>
			      </el-table-column>
			     </template>
			 </global-table>
		 </div>
	</div>
	<Warehouse ref="warehouseRef" 
	    :marketList="marketList" 
		:groupList="groupList" 
	    :ftype="props.ftype" 
		@confirm="handleQuery"/>
</template>

<script setup>
	//需要安装依赖 npm i -S vuedraggable@next
	//使用文档地址https://www.itxst.com/vue-draggable-next/tutorial.html
	import draggable from "vuedraggable";
	import '@/assets/css/draggable.css';
	import {Search,ArrowDown,UploadFilled,ArrowUpBold,ArrowDownBold,InfoFilled} from '@element-plus/icons-vue'
	import {Plus,Drag} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import Warehouse from "./edit.vue"
	import { ElMessage, ElMessageBox } from 'element-plus'
	import GlobalTable from  "@/components/Table/GlobalTable/index.vue";
	import warehouseApi from "@/api/erp/warehouse/warehouseApi.js"
	import groupApi from '@/api/amazon/group/groupApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
    const warehouseRef = ref();
	const globalTableRef=ref(GlobalTable);
	let props = defineProps({  ftype:"" });
	const state = reactive({
		queryParams:{search:""},
        tableData: {records:[],total:0}  ,
		groupList:[],
		marketList:[],
		deleteDisable:true,
		ids:"",
		withouthide:true,
	});
	state.queryParams.ftype=props.ftype+"_usable";
	const {
		rankVisible,
		list,
		ids,
		queryParams,
		deleteDisable,
		marketList,
		groupList,
		tableData,
		withouthide,
	} = toRefs(state)
	function handleAdd(){
		warehouseRef.value.show(state.queryParams.parentid, state.queryParams.ftype);
	}
	function handleQuery(){
		globalTableRef.value.loadTable(state.queryParams);  
	}
	function show(parentid){
			state.queryParams.parentid=parentid;
			handleQuery();
	}
	function handleRecover(row){
		warehouseApi.detail(row.id).then(({data})=>{
			if(data){
				data.disabled=false;
				warehouseApi.updateData(row.id,data).then((res)=>{
					  ElMessage.success('操作成功');
					  handleQuery();
				});
			}else{
				 ElMessage.error('未查到此仓库!');
			}
		});
		
	}
	
	function handleDelete(row){
		 const ids = [row.id || state.ids].join(',');
		 warehouseApi.checkDeleteWarehouse(ids).then((res)=>{
		 	if(res.data){
		 		ElMessageBox.confirm(res.data, '警告', {
		 				confirmButtonText: '继续删除',
		 				cancelButtonText: '取消',
		 				type: 'warning',
		 		})
		 				.then(() => {
		 				  warehouseApi.deleteInfo(ids).then(() => {
		 					ElMessage.success('删除成功');
		 					handleQuery();
		 				  });
		 				})
		 				.catch(() => ElMessage.info('已取消删除'));
		 	}else{
		 			warehouseApi.deleteInfo(ids).then(() => {
		 					ElMessage.success('删除成功');
		 					handleQuery();
		 			});
		 		}
		 });
	}
	function loadtableData(param){
		warehouseApi.getWarehouseListPage(param).then(res=>{
		    state.tableData.records=res.data.records;
			if(state.tableData.records){
				state.tableData.records.forEach(row=>{
					if(state.marketList&&row.country){
					   state.marketList.forEach(item=>{
					       if(item.market==row.country){
							   row.countryname=item.name;
						   }
					   })
					}
					if(state.groupList&&row.groupid){
					   state.groupList.forEach(item=>{
					       if(item.id==row.groupid){
							   row.groupname=item.name;
						   }
					   })
					}
				});
			}
			state.tableData.total=res.data.total;
		});
	}
	function handleSelectionChange(selection){
			  state.ids = selection.map((item) => item.id);
			  if(selection.length>0){
				  state.deleteDisable=false;
			  }else{
				  state.deleteDisable=true;
			  }
	}
    function handleUpdate(row){
			warehouseRef.value.show(state.queryParams.parentid,state.queryParams.ftype,row.id);
	}
 
	function handleDefault(row){
		 warehouseApi.updateDefault(row.id).then(res=>{
			 ElMessage.success('设置成功');
			 handleQuery();
		 })
	}
	async function loadGroup(){
			await  groupApi.getAmazonGroup().then((res)=>{
				    if(res.data){
						res.data.push({"id":"","name":"全部"})
						state.groupList=res.data;
					}
			  })
	}
	async function loadCountry(){
			 await marketApi.getMarketByGroup({'groupid':""}).then((res)=>{
				 if(res.data){
					 res.data.push({"id":"","name":"全部","marketplaceid":""})
					 state.marketList=res.data;
				 }
			 })
	}
	function showHandle(val){
		if(val==false){
			state.queryParams.showhide='true';
		}else{
			state.queryParams.showhide=null;
		}
		handleQuery();
	}
	
	onMounted(()=>{
		 if(props.ftype=="oversea"){
		 	loadGroup();
		 	loadCountry();
		 }
	});
	
	defineExpose({ show })
</script>

<style scoped="scoped">
.rank-list-title{
		display: flex;
		justify-content: space-between;
		margin:0 16px;
		font-size: 12px;
		color: #999;
	}
	.demo-tabs{
		padding-top:0px;
	}
.row-tag-hover{
	opacity: 0;
}
tr:hover .row-tag-hover{
	opacity:1;
}
</style>
