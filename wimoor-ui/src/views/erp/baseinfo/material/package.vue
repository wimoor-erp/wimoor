<template>
	<div class="main-sty">
	<!--  头部搜索区域 -->
	<div class="con-header" >
	     <Header @getdata="loadData" ref="headerRef"  type="package"/>
	</div>
	<el-row>
	   <GlobalTable ref="globalTable" 
	   :rowClassName="handleRowClassName" 
	   :defaultSort="defaultSort" 
	   @selectionChange='selectChange' 
	   :tableData="tableData" 
	   height="calc(100vh - 250px)"  
	   @loadTable="loadTableData" 
	   :stripe="true"  
	   style="width: 100%;margin-bottom:16px;">
		   <template #field>
	     <el-table-column   type="selection" width="38" />
	    <el-table-column   prop="sku" min-width="200" label="名称/SKU" sortable="custom" show-overflow-tooltip>
	       <template #default="scope">
	          <div class='name'>{{scope.row.name}}</div>
	          <div class='sku'>{{scope.row.sku}}
	            <copy class="" @click="CopyText(scope.row.sku)" title='复制名称' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
	         <el-popover
	             placement="top"
	             title="阶梯采购价"
	             :width="200"
	         				  trigger="click"
	         				  @before-enter="getWisePriceList(scope.row)"
	           >
	             <template #reference>
	              <span v-if="scope.row.price" class="pointer text-orange"> ￥{{scope.row.price}}</span> 
	         				   <span v-else  class="pointer text-orange"> -</span> 
	             </template>
	         	<el-table :data="pricelist" v-loading="loading">
	         		<el-table-column label="起订量" prop="amount"></el-table-column>
	         		<el-table-column label="单价" prop="price"></el-table-column>
	         	</el-table>
	           </el-popover>
			  </div>
	      </template>
	    </el-table-column>
	    <el-table-column prop="category"  label="品类"  sortable="custom" show-overflow-tooltip>
			 <template #default="scope">
				{{NullTransform(scope.row.category)}}
				<div class="font-extraSmall" >备注:{{NullTransform(scope.row.remark)}}</div>
			 </template>
		</el-table-column>
	    <el-table-column prop="weight" label="重量(kg)"  width="100" sortable="custom" >
			<template #default="scope">
				<div v-if="scope.row.weight">{{NullTransform(scope.row.weight)}}</div>
				<div v-else>-</div>
				<div class="font-extraSmall" v-if="scope.row.length">{{scope.row.length}}*{{scope.row.width}}*{{scope.row.height}} cm</div>
			 </template>
		</el-table-column>
	    <el-table-column prop="supplier" label="供应商" width='200'  sortable="custom">
			<template #default="scope">
				<el-link v-if="scope.row.supplier" @click.stop="openPurchase(scope.row.purchaseUrl)" class="font-12">{{scope.row.supplier}}</el-link>
				<el-link v-else>-</el-link>
			    <div ><span class="font-extraSmall">供货周期：</span>{{NullTransform(scope.row.delivery_cycle)}}</div>
			</template>	
		</el-table-column>	
	 
		<el-table-column prop="fulfillable" label="可用库存" width="100"   sortable="custom">
		<template #default="scope">
			<el-popover
			    placement="top"
			    title="产品库存"
			    :width="250"
			    trigger="click"
				@before-enter="loadInventory(scope.row)"
			  >
			    <template #reference>
			     <span class="pointer">{{scope.row.fulfillable}}</span> 
			    </template>
				<el-table :data="inventoryList" >
					<el-table-column label="仓库名称" width="140" prop="name"></el-table-column>
					<el-table-column label="库存" prop="quantity"></el-table-column>
				</el-table>
			  </el-popover>
		</template>
		 </el-table-column>
		<el-table-column prop="inbound" label="在途库存" width="120"    sortable="custom">
			<template #default="scope">
				<span v-if="scope.row.inbound">{{scope.row.inbound}}</span>	 
				<span v-else>0</span>	 
			</template>	
		</el-table-column>
	    <el-table-column fixed="right" prop="operate" label="操作"  width="190" >
	        <template #default="scope">
	          <el-space>
	            <el-button type="primary" link @click="productDetails(scope.row)">详情</el-button>
	            <el-button type="primary" link @click="productEdit(scope.row)">编辑</el-button>
	            <el-dropdown :hide-on-click="false">
	              <span class="el-dropdown-link">
	                <more-one class="ic-cen" theme="outline" size="16" fill="#333" :strokeWidth="3"/>
	              </span>
	               <template #dropdown>
	                <el-dropdown-menu>
	                  <el-dropdown-item @click.stop="deleteInfo(scope.row)">停用</el-dropdown-item>
					  <el-dropdown-item @click.stop="recoverRows(scope.row)">启用</el-dropdown-item>
	                 <!-- <el-dropdown-item >打印条码</el-dropdown-item> -->
	                </el-dropdown-menu>
	            </template>
	            </el-dropdown>
	          </el-space>
	        </template>
	    </el-table-column>
	  </template>
	  </GlobalTable>
	</el-row>
	</div>
	
</template>
<script>
    export default{ name:"箱子管理" };
</script>
<script setup>
	import {ref,reactive,toRefs,onMounted,inject} from "vue"
	import {Copy,MenuUnfold,Plus,SettingTwo,Help,MoreOne} from '@icon-park/vue-next';
	import {Edit } from '@element-plus/icons-vue';
	import Header from "./components/header.vue"
	import {useRouter } from 'vue-router'
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import CopyText from"@/utils/copy_text.js";
	import NullTransform from"@/utils/null-transform";
	import materialApi from '@/api/erp/material/materialApi.js';
	import Record from "./components/record.vue"
	import {getAllTags} from '@/api/sys/admin/tag.js';
	import inventoryApi from '@/api/erp/inventory/inventoryApi.js';
	import consumableApi from '@/api/erp/purchase/plan/consumableApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import {getLast} from '@/api/erp/purchase/plan/planApi.js';
	const emitter = inject("emitter");
	const headerRef=ref();
	let router = useRouter()
	const globalTable=ref();
	let state = reactive({
		loading:true,
		tagsList:[],
		tableData:{records:[],total:0},
		markprops:{ multiple: true },
		tagsValue:[],
		checkTags:'',
		nowTagProRow:{},
		markVisable:false,
		inventoryList:[],
		pricelist:[],
		queryParams:{},
		defaultSort:{"prop": 'opttime', "order": 'descending' }
		})
		let {loading,tableData,tagsList,markprops,tagsValue,checkTags,nowTagProRow,markVisable,queryParams,defaultSort,
		inventoryList,pricelist,}=toRefs(state)
		  function productDetails(row){
			  router.push({
			  	path:'/material/details',
			  	query:{
			  	  title:"包材详情",
			  	  path:'/material/details',
				  details:row.id,
				  type:'package',
			  	}
			  })
		  }
		  function productEdit(row){
			     emitter.emit("removeCache", "编辑包材"+row.sku);
				 router.push({
				 	path:'/localproduct/editinfo',
				 	query:{
				 	  title:"编辑包材"+row.sku,
				 	  path:'/localproduct/editinfo',
				 	  details:row.id,
					  type:'package',
				 	}
				 }) 
		  }
  
function loadData(params){
	state.queryParams=params;
	state.queryParams.sort=state.defaultSort.prop;
	state.queryParams.order=state.defaultSort.order;
	globalTable.value.loadTable(params);
}
function loadTableData(params){
	materialApi.packageList(params).then((res)=>{
		state.tableData.records = res.data.records;
		state.tableData.total =res.data.total;
		if(state.tableData.records){
			state.tableData.records.forEach(item=>{
				item.amount=item.planamount;
				item.scVisible=false;
				getLast({id:item.id}).then(res=>{
					item.last=res.data;
				})
			})
		}
	})
}
  
function loadInventory(row){
	state.loading =true;
	inventoryApi.getInventoryByMaterial({"mid":row.id}).then((res)=>{
		state.inventoryList=res.data;
		state.loading =false
	});
}
function getWisePriceList(row){
	state.loading =true;
	materialApi.getWisePriceList({"mid":row.id}).then((res)=>{
		state.pricelist=res.data;
			state.loading =false
	});
}
function openPurchase(url){
	 window.open(url, '_blank');
}
function deleteInfo(row){
	materialApi.deleteData({"ids":row.id}).then((res)=>{
		if(res.data){
			ElMessage.success( '删除成功！');
			loadData(state.queryParams);
		}
	})
}
function recoverRows(row){
		if(row.isDelete==true || row.isDelete==1){
			materialApi.recoverData({"id":row.id,"sku":row.sku}).then((res)=>{
				if(res.data){
					ElMessage.success(res.data);
					loadData(state.queryParams);
				}
			})
		}
}
function selectChange(selection) {
	 headerRef.value.selectRows=selection;
}
   
	onMounted(()=>{
		
	})
</script>

<style >
	.el-dropdown-link .i-icon svg{vertical-align:text-top;}
	.tag-mr{margin-right:4px;margin-bottom:4px;display:inline-block}
	.sku .i-icon-copy{cursor:pointer;opacity: 0;}
	.el-table  tr:hover>td.el-table__cell .sku .i-icon-copy{opacity: 1;transition: opacity .4s; }
	.scoped-form .el-form-item--default{
		margin-bottom:0px;
	}
	.scoped-form .el-form-item__label{
		color: #999;
	}
	.footer-right{ 
		margin-top:16px;
		text-align: right;
	}
	.font-12{
		font-size:12px;
	}
	 
</style>
