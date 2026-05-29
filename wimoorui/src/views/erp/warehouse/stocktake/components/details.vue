<template>
<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>盘点单详情</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							
						 <el-form :model="form"
						   :rules="rules"
						   label-width="120px">
							  <el-row>
							 <el-col :span="12">
							 <el-form-item label="单据编码"   >
							   {{form.number}}
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="备注"  >
								  {{form.remark}}
							 </el-form-item>
						  </el-col>
							 <el-col :span="12">
								 <el-form-item label="类型" prop="warehouse">
									 <div v-if="form.ftype==1">仓位</div>
									 <div v-if="form.ftype==2">库位</div>
								 </el-form-item>
							 </el-col>
							 <el-col :span="12" >
							  <el-form-item label="状态"  >
							     <el-tag v-if="form.isworking==true" type="warning" effect="plain" >进行中</el-tag>
								 <el-tag v-else type="success" effect="plain" >盘点完成</el-tag>
								 <el-button v-if="form.isworking==true" style="margin-left: 10px;" size="small" type="primary" @click.stop="continueHandle">继续盘点</el-button>
							  </el-form-item>
							  </el-col>
							  </el-row>
						 </el-form>
						 <el-divider></el-divider>
						 <el-tabs v-model="activeWarehouse"  class="table-tabs" @tab-change="loadItemList(activeWarehouse)">
						     <el-tab-pane v-for="item in warehouseList" :label="item.name" :name="item.id" > </el-tab-pane>
						   </el-tabs>
						 <div class="mark-re">
						   <h5 >产品列表</h5>
						 </div>
						 <el-table border :data="tableData">
							 <el-table-column  prop="image" label="图片" width="70px" >
							    <template #default="scope">
							     <el-image v-if="scope.row.image" :src="scope.row.image"  class="product-img"></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"    class="product-img"  ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column  prop="product" label="名称/SKU"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.name}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column label="成本价(¥)" prop="price"/>
							 <el-table-column label="可用库存" prop="fulfillable"/>
							 <el-table-column label="待出库" prop="outbound"/>
							 <el-table-column label="盘点数量"  prop="amount">
							 </el-table-column>
							  <el-table-column label="账面数量" sortable  prop="amount">
								  <template #default="scope">
								  <span v-if="scope.row.isworking==true">
									  {{scope.row.fulfillable+scope.row.outbound}}
								  </span>
								   <span v-else>
									  {{scope.row.amount-scope.row.overamount+scope.row.lossamount}}
								   </span>
								  </template>
							  </el-table-column>
							  <el-table-column label="盘盈数量" prop="overamount"/>
							  <el-table-column label="盘亏数量" prop="lossamount"/>
							  <el-table-column label="库位库存" prop="lossamount" >
							  								   <template #default="scope">
							  								  <el-link type="primary" @click.stop="handleShowShelf(scope.row)">库位操作</el-link> 
							  								  </template>
							  </el-table-column>
						 </el-table>
						 <el-row :gutter="16" class="m-t-16  ">
							 <el-col :span="24" >
								 <h5 class="flex-right">合计</h5>
							 </el-col>
							 <el-col :span="24" >
							  <div class="m-t-8 flex-right">
							 <income-one class="m-r-8 " theme="outline" size="16" fill="#529b2e"/>
							 <span class="font-small m-r-16 text-height">盘盈金额:</span>
							 <span class="font-24 font-bold "><span class="font-12">¥</span>{{form.overprice}}</span>
							</div>
							<div class="m-t-8 flex-right">
							 <expenses-one class="m-r-8 " theme="outline" size="16" fill="#f56c6c"/>
							 <span class="font-small m-r-16 text-height">盘亏金额:</span>
							 <span class="font-24 font-bold "><span class="font-12">¥</span>{{form.lossprice}}</span>
							</div>
						 </el-col>
						 </el-row>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;">
								<el-space>
									<el-button type="" @click="closeTab">关闭</el-button>
								</el-space>
							</div>
						</div>
	</div>
	<SkuInventory ref="skuInventoryRef" :disable='true'  @change="saveItem"></SkuInventory>
</template>

<script setup>
	import {ArrowDown,Edit,Remove,CirclePlus} from '@element-plus/icons-vue'
	import {Plus,ExpensesOne,IncomeOne,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage } from 'element-plus'
	import SkuInventory from './sku_inventory.vue';
	import { useRoute,useRouter } from 'vue-router';
	import {redirectToList} from '@/utils/page_helper.js';
	import shelfproductApi from '@/api/erp/warehouse/shelfproductApi.js';
	import stocktakingApi from '@/api/erp/inventory/stocktakingApi.js';
	const emitter = inject("emitter"); // Inject `emitter`
	const skuInventoryRef=ref();
	function closeTab(){
		 redirectToList(router,"/erp/warehouse/stocktake",'库存盘点');
	};
	const router = useRouter()
	const id = router.currentRoute.value.query.id;
	let state = reactive({
		orderState:true,
		tableData:[],
		form:{
		},
		warehouseList:[],
		activeWarehouse:"",
		rules: {
			warehouse: [{ required: true, message: '出库仓库不能为空', trigger: 'blur' }],
		},
		summary:{},
	})
	let {
		orderState,
		form,
		warehouseList,
		activeWarehouse,
		tableData,
		rules,
		summary,
	}=toRefs(state)
	
	function continueHandle(){
		//跳到编辑页面 create.vue
		router.push({
			path:"/e/w/s",
			query:{
				title:'编辑盘点单',
				path:"/e/w/s",
				id:id,
				replace:true
			},
		})
	}
	function load(){
		stocktakingApi.view({"id":id}).then((res)=>{
			if(res.data){
				state.tableData=res.data.itemList;
				state.summary=res.data.sum_map;
				state.form=res.data;
				var checkid=[];
				if(state.form.ftype==2&&res.data.shelflist&&res.data.shelflist.length>0){
						 res.data.shelflist.forEach(item=>{
							checkid.push(item.shelfid) ;
						 })
				}
				if(state.form.ftype==1&&res.data.warehouselist&&res.data.warehouselist.length>0){
						 res.data.warehouselist.forEach(item=>{
							checkid.push(item.warehouseid) ;
						 })
				}
				loadTabsData(checkid,state.form.ftype);
			}
		});
	}
	function loadTabsData(tabsid,ftype){
		if(tabsid!=[] && tabsid!=null && tabsid!=""){
			if(ftype==1){
				stocktakingApi.stocktakingListWarehouse(tabsid).then(res=>{
					state.warehouseList=res.data;
					state.activeWarehouse=state.warehouseList[0].id;
					loadItemList(state.warehouseList[0].id);
				})
			}
			else{
				stocktakingApi.stocktakingListShelf(tabsid).then(res=>{
					state.warehouseList=res.data;
					state.activeWarehouse=state.warehouseList[0].id;
					loadItemList(state.warehouseList[0].id);
				})
			}
	    }
	}
	function loadItemList(itemid){
		var params={selected:"true",isstocking:"true"};
		params.stocktakingid=id;
		if(state.form.ftype==1){
			params.warehouseid=itemid;
			stocktakingApi.searchCondition(params).then((res)=>{
				state.tableData = res.data;
			});
		}else{
			params.shelfid=itemid;
			shelfproductApi.getShelfInventoryStockingList(params).then(function(res) {
					state.tableData = res.data;
			})
		}
	
	}
	function handleShowShelf(row){
		row.stocktakingid=id;
		row.warehouseid=state.activeWarehouse;
		row.stocktype=state.form.ftype;
		skuInventoryRef.value.show(row);
	}
	onMounted(()=>{
		load();
	});
</script>

<style>
	.icon-font{
		font-size: 24px;
	}
	.text-green{
		color: var(--el-color-success-dark-2);
	}
	.text-red{
		color: var(--el-color-danger-dark-2);
	}
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 176px);
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
		.mark-re{
			margin-top: 16px;
			margin-bottom:16px;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	.mark-re h5::before{
		content: "";
		display: inline-block;
		height: 13px;
		width: 4px;
		margin-right:8px;
		background-color: #FF6700;
	}
	.fill-from-body{
		margin:16px 24px;
	}
	.m-t-16{margin-top: 16px;}
	.m-r-16{
		margin-right: 16px;
	}
	.m-r-8{
		margin-right:8px;
	}
	.font-24{
		font-size: 24px;
	}
	.flex-right{
		display: flex;
		justify-content: flex-end;
		align-items: flex-end;
	}
	.text-height{
		line-height: 24px;
		display: inline-block;
	}
</style>
