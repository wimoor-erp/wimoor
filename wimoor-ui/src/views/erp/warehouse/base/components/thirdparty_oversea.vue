<template>
	<div class="gird-line-left">
		 <!-- 搜索表单 -->
		 			  <el-space>
					  	<el-icon class="font-extraSmall pointer" @click="handleRouteToApi"><Setting/></el-icon>
						  <el-select  style="width:150px"  v-model="apiid" @change="handChangeApi">
							  <template #append>Setting</template>
							<el-option v-for="item in apilist" :label="item.name" :value="item.id" />
						  </el-select>
		 			  <div class="flex-center">
		 				  <el-button @click="handleSyncWarehouse">同步仓库</el-button>
		 			  </div>
					  </el-space>
		 		 
		    <!-- 数据表格 -->
		 		<el-scrollbar style="height: calc(100vh - 180px)">
		 	  <el-menu
		 	        :default-active="defaultActive"
		 	         class="m-t-8"
		 	      >
		 	        <el-menu-item  v-for="(item,index) in warehouselist" :index="item.findex"   @click="handleRowClick(item)" >
		 	           <div style="width:140px">{{item.code}}-{{item.name}}<span v-if="item.country">-{{item.country}}</span>
					   <el-icon class="text-green bg-green" v-if="item.isbind==true" style="font-size:14px;color:#67C23A" title="已绑定"><InfoFilled /></el-icon>
					   </div>
		 			  <el-tooltip  trigger="click" class="text-right" @visible-change="visibleChange(item)" v-if="item.channel&&item.channel instanceof Array &&item.channel.length>0">
		 			     <div class="font-extraSmall"> 查看渠道</div>
		 			     <template #content>
							  <p v-for="chitem in item.channel" class="text item">{{chitem.channelName}}</p>
		 			     </template>
						  
		 			   </el-tooltip>
					    <el-dropdown class="text-right" @visible-change="visibleChange(item)">
					       <el-icon class="more-icon" :class="{'isvisable':item.isvisable}"><MoreFilled /></el-icon>
					       <template #dropdown>
					         <el-dropdown-menu>
					    			<el-dropdown-item  @click.stop="showBindDialog(item)">绑定本地仓库</el-dropdown-item>
					         </el-dropdown-menu>
					       </template>
					     </el-dropdown>
		 	        </el-menu-item>
		 	      </el-menu>
		 </el-scrollbar>
	</div>
	<div class="gird-line-right">
       <InventoryK5  v-if="system=='K5'"  ref="inventoryK5Ref"></InventoryK5>
	   <InventoryOps v-else-if="system=='OPS'" ref="inventoryOpsRef"></InventoryOps>
	   <InventoryXL v-else-if="system=='XL'" ref="inventoryXLRef"></InventoryXL>
	   <InventoryYC v-else-if="system=='YC'" ref="inventoryYCRef"></InventoryYC>
	   <ThirdpartyBindDialog ref="thirdpartyBindDialogRef"></ThirdpartyBindDialog>
	</div>
	
	
</template>

<script setup>
	import { onMounted, reactive, ref, toRefs,nextTick } from 'vue';
	import {Copy} from '@icon-park/vue-next';
	import { Plus,Search, Edit, Refresh, Delete,MoreFilled,Setting ,InfoFilled} from '@element-plus/icons-vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import CopyText from"@/utils/copy_text.js";
    import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import thirdpartyWarehouseApi from "@/api/erp/thirdparty/thirdpartyWarehouseApi.js";
	import { useRoute,useRouter } from 'vue-router';
	import InventoryK5  from "./thirdparty/k5.vue";
	import InventoryOps from "./thirdparty/ops.vue";
	import InventoryXL  from "./thirdparty/xl.vue";
	import InventoryYC  from "./thirdparty/yc.vue";
	import ThirdpartyBindDialog from "./thirdparty_bind_dialog.vue";
	let router = useRouter();
	const state = reactive({apilist:[],apiid:"",refreshtime:"无",warehouselist:[],defaultActive:1,queryParam:{},invlist:[],system:""});
	const { apilist,apiid,warehouselist,defaultActive,queryParam,invlist,refreshtime,system} = toRefs(state);
	const inventoryK5Ref=ref();
	const inventoryOpsRef=ref();
	const inventoryXLRef=ref();
	const inventoryYCRef=ref();
	const thirdpartyBindDialogRef=ref();
	function handleSyncWarehouse(){
		thirdpartyWarehouseApi.saveStock({"apiid":state.apiid,"action":"sync"}).then(res=>{
			state.warehouselist=res.data;
			handleExt();
			ElMessage.success("同步成功")
		})
	}
 function handleRouteToApi(){
 	router.push({
 		path:'/erp/thirdparty',
 		query:{
 		  title:"物流绑定",
 		  path:'/erp/thirdparty',
 		}
 	})
 }
 
 function showBindDialog(item){
	 thirdpartyBindDialogRef.value.show(item);
 }
	function handChangeApi(){
		state.queryParam.apiid=state.apiid;
		state.queryParam.api=state.apiid;
		state.apilist.forEach(item=>{
			if(item.id==state.apiid){
				state.system=item.system;
			}
		})
		
		thirdpartyWarehouseApi.list({"apiid":state.apiid}).then(res=>{
			state.warehouselist=res.data;
			handleExt();
		})
	}
	function handleRowClick(warehouse){
		state.queryParam.houseid=warehouse.code;
		 handleQueryInv();
	}
	function handleQueryInv(){
		if(state.system=="K5"){
			inventoryK5Ref.value.query(state.queryParam);
		}
		if(state.system=="OPS"){
			inventoryOpsRef.value.query(state.queryParam);
		}
		if(state.system=="XL"){
			inventoryXLRef.value.query(state.queryParam);
		}
		if(state.system=="YC"){
			inventoryYCRef.value.query(state.queryParam);
		}
	}

	function isJSON(str) {
	  try {
	    JSON.parse(str);
	    return true;
	  } catch (error) {
	    return false;
	  }
	}
	function handleExt(){
		if(state.warehouselist){
			for(var i=0;i<state.warehouselist.length;i++){
				var item=state.warehouselist[i];
				if(item.ext&&isJSON(item.ext)){
				item.channel=JSON.parse(item.ext);
				item.isvisable=true;
				}else if(item.ext){
					item.channel=item.ext;
					item.isvisable=true;
				}
				 if(!item.findex){
				 	 item.findex=i;
				 }
				 if(i==0){
				 	state.defaultActive=item.findex;
					handleRowClick(item)
				 }
			}
		}
	}
	function visibleChange(item){
		item.isvisable = !item.isvisable
	}
	 onMounted(()=>{
		thirdpartyApi.getSupportApi({"support":"warehouse"}).then(res=>{
			if(res&&res.data&&res.data.length>0){
				state.apilist=res.data;
				state.apiid=res.data[0].id
				state.queryParam.apiid=res.data[0].id;
				state.queryParam.api=state.apiid;
				state.system=res.data[0].system;
			}else{
				state.apilist=[];
				state.apiid="";
				state.queryParam.apiid="";
				state.system="";
				state.queryParam.api=state.apiid;
			}
			handChangeApi();
		})
	 });
</script>
<style>
	
</style>
<style scoped>
	.more-icon{
		transform:rotate(90deg);
		font-size:14px!important;
		margin-right: -8px!important;
		opacity: 0;
	}
	.thirdparty-main-sty{
		padding:20px;
	}
	.el-menu-item.is-active{
		background-color: var(--el-color-info-light-9);
	}
	.el-menu{
		border-right:none;
	}
	.el-menu-item{
		display: flex;
		justify-content: space-between;
	}
	.ch-tag{
		float:"left";
		padding:10px;
	}
	.flex{
		display: flex;
	}
		.flex .gird-line-left{
			width: 310px;
		}
		.flex .gird-line-right{
			flex:1;
		}
		.local{
			height: calc(100vh - 66px);
		}
		
		.localoversea{
			height: calc(100vh - 120px);
		}
		.el-menu-item.is-active{
			background-color: var(--el-color-info-light-9);
		}
		.el-menu-item .isvisable{
			opacity: 1;
		}
		.el-menu-item:hover .more-icon{
			opacity: 1;
		}
</style>