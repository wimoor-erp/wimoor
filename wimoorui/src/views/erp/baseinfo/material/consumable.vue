<template>
	<div class="main-sty">
	<!--  头部搜索区域 -->
	<div class="con-header" >
	     <Header @getdata="loadData" ref="headerRef"  type="consumable"/>
	</div>
	<el-row>
	   <GlobalTable ref="globalTable" 
	   :rowClassName="handleRowClassName" 
	   :defaultSort="defaultSort" 
	   @selectionChange='selectChange' 
	   :tableData="tableData" 
	   height="calc(100vh - 246px)"  
	   @loadTable="loadTableData" 
	   :stripe="true"  
	   style="width: 100%;margin-bottom:16px;">
		   <template #field>
	     <el-table-column   type="selection" width="38" />
	      <el-table-column   prop="image" label="图片" width="60" >
	       <template #default="scope">
	       
			<el-popover
			    placement="top-start"
			    :width="200"
			    trigger="hover"
				v-if="scope.row.image"
			  >
			    <template #reference>
			      <el-image  :src="scope.row.image"   width="40" height="40"  ></el-image>
			    </template>
				<el-image  :src="scope.row.image"    ></el-image>
			  </el-popover>

			<el-image v-else :src="$require('empty/noimage40.png')"   width="40" height="40"  ></el-image>
	      </template>
	    </el-table-column>
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
	    <el-table-column prop="weight" label="重量(kg)"  width="110" sortable="custom" >
			<template #default="scope">
				<div v-if="scope.row.weight">{{NullTransform(scope.row.weight)}}</div>
				<div v-else>-</div>
				<div class="font-extraSmall" v-if="scope.row.length">{{scope.row.length}}*{{scope.row.width}}*{{scope.row.height}} cm</div>
			 </template>
		</el-table-column>
	    <el-table-column prop="supplier" label="供应商" width='200'  sortable="custom" show-overflow-tooltip>
			<template #default="scope">
				<el-link v-if="scope.row.supplier" @click.stop="openPurchase(scope.row.purchaseUrl)" >{{scope.row.supplier}}</el-link>
				<el-link v-else>-</el-link>
			    <div ><span class="font-extraSmall">供货周期：</span>{{NullTransform(scope.row.delivery_cycle)}}</div>
			</template>	
		</el-table-column>	
	    <el-table-column prop="needamount" sortable="custom" 
		 width="120"   label="需求量" >
			 <template #default="scope"> 
				<span title="待入库库存" v-if="scope.row.needinboundamount" class="text-green">{{scope.row.needinboundamount}} </span> 
				<span title="待入库库存" v-else class="font-extraSmall">0 </span> 
				<span v-if="scope.row.needamount" > + </span> 
				<span v-if="scope.row.needamount" class="text-orange"  title="实际库存"> {{scope.row.needamount}}</span>
				 <el-popover
				     title="主SKU列表"
				     :width="630"
					  trigger="click"
					  @before-enter="getPConsumableList(scope.row)"
				   >
				     <template #reference>
				     <div class="font-extraSmall pointer">
				     				  <span v-if="scope.row.mainsubnum"> {{scope.row.mainsubnum}}</span>
				     				  <span v-else> 0</span>
				     个SKU使用</div>
				     </template>
					<el-table border :data="pconsumableList" v-loading="loading"  height="calc(40vh)" >
							 <el-table-column width="80" label="图片">
								<template #default="scope">
									<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
									<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
								</template>
							 </el-table-column>
							 <el-table-column label="产品名称/主SKU" show-overflow-tooltip  >
									 <template #default="scope">
										<div class='name'>{{scope.row.name}}</div>
										<div class='sku'>{{scope.row.sku}} </div>
									 </template>
							 </el-table-column>
							 <el-table-column label="单价" prop="price" width="100">
								 <template #default="scope">
									<div > {{scope.row.price}}</div>
									<div ><span class="font-extraSmall">消耗量:</span>{{scope.row.amount}} </div>
								 </template>
							 </el-table-column>
							 <el-table-column label="库存" prop="fulfillable"  width="100">
								 <template #default="scope">
									<div v-if="scope.row.fulfillable"> {{scope.row.fulfillable}}</div>
									<div v-else>0</div>
									<div v-if="scope.row.outbound"><span class="font-extraSmall">待出库:</span>{{scope.row.outbound}} </div>
								 </template>
							 </el-table-column>
							 <el-table-column label="待入库" prop="inbound"  width="70">
							 </el-table-column>
							 <el-table-column label="辅料需求量" prop="needPlan"  width="100">
							 </el-table-column>
					</el-table>
				   </el-popover>
				 
			 </template>	
	    </el-table-column>
		<el-table-column prop="fulfillable" label="可用库存" width="145"   sortable="custom">
			<template #default="scope">
							 {{scope.row.fulfillable}}
			 <div class="font-extraSmall">
				 <el-popover
				     placement="top"
				     title="修改安全库存"
				     :width="200"
					 :visible="scope.row.scVisible"
					 @show="showCyclePopover(scope.row)"
				   >
				     <template #reference>
				     <div class="pointer  table-edit-flex" @click="scope.row.scVisible=!scope.row.scVisible">
						 <span class="text-red" v-if="scope.row.fulfillable<scope.row.stockingCycle">
							 低于安全库存:{{scope.row.stockingCycle}}
						 </span>
						 <span v-else>
						 	 安全库存:{{scope.row.stockingCycle}}
						 </span>
					 <span >
						<el-icon ><Edit /></el-icon>
					</span>
					 </div>
					   
				     </template>
				 	 <el-input v-model="scope.row.oldstockingCycle" @input="scope.row.oldstockingCycle=CheckInputInt(scope.row.oldstockingCycle)"></el-input>
					 <el-button style="margin-top:5px;"   type="primary" @click.stop="updateCycle(scope.row)">确认</el-button>
				   </el-popover>
				
				 </div>
			</template>	
		</el-table-column>
		<el-table-column prop="suggest" label="建议采购" width="120"    sortable="custom">
			<template #default="scope">
				{{scope.row.suggest}} 
				<span v-if="scope.row.needsuggest" class="font-small">
				<span class="font-extraSmall" >(</span>
				<span class="text-red" :title="'缺货'+scope.row.needsuggest+'个'">{{scope.row.needsuggest}}</span>
				<span class="font-extraSmall">)</span>
				</span>
				<div class="font-extraSmall"><span >在途：</span>
				<span v-if="scope.row.inbound">{{scope.row.inbound}}</span>
				<span v-else>0</span>	 
				</div>
				
			</template>	
		</el-table-column>
		<el-table-column prop="inbound" label="采购数量" width="120"    sortable="custom">
			<template #default="scope">
				 <el-input v-model="scope.row.amount" @input="scope.row.amount=CheckInputInt(scope.row.amount)" ></el-input>
			     
			</template>	
		</el-table-column>
		 <el-table-column prop="inbound" label="采购记录" width="130"   >
			<template #default="scope">
					 <Record :row="scope.row" ></Record>
			</template>	
		 </el-table-column>
	    <el-table-column fixed="right" prop="operate" label="操作"  width="190" >
	        <template #default="scope">
	          <el-space>
				<el-button v-if="scope.row.isplan==true" type="primary" link @click="delItem(scope.row)">移除</el-button>
				<el-button v-if="scope.row.isplan==true" type="primary" link @click="addItem(scope.row)">保存</el-button>
				<el-button v-else type="primary" link @click="addItem(scope.row)">加入</el-button>
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
	import consumablesApi from '@/api/erp/material/consumablesApi.js';
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
		pconsumableList:[],
		queryParams:{},
		defaultSort:{"prop": 'suggest', "order": 'descending' }
		})
		let {loading,tableData,tagsList,markprops,tagsValue,checkTags,nowTagProRow,markVisable,queryParams,defaultSort,
		inventoryList,pricelist,pconsumableList,}=toRefs(state)
		  function productDetails(row){
			  router.push({
			  	path:'/material/details',
			  	query:{
			  	  title:"辅料详情",
			  	  path:'/material/details',
				  details:row.id,
				  type:'consumable',
			  	}
			  })
		  }
		  function productEdit(row){
			     emitter.emit("removeCache", "编辑辅料"+row.sku);
				 router.push({
				 	path:'/localproduct/editinfo',
				 	query:{
				 	  title:"编辑辅料"+row.sku,
				 	  path:'/localproduct/editinfo',
				 	  details:row.id,
					  type:'consumable',
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
	materialApi.consumableList(params).then((res)=>{
		state.tableData.records = res.data.records;
		state.tableData.total =res.data.total;
		if(state.tableData.records){
			state.tableData.records.forEach(item=>{
				item.amount=item.planamount;
				item.scVisible=false;
				item.last="--";
				if(item.needsuggest){
					if(parseInt(item.needsuggest)<0){
						item.needsuggest=0;
					}
				}
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
function getPConsumableList(row){
	state.loading =true;
	consumablesApi.getConsumablePList({"materialid":row.id,"warehouseid":row.warehouseid}).then((res)=>{
		state.pconsumableList=res.data;
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
					ElMessage.success( res.data);
					loadData(state.queryParams);
				}
			})
		}
}
function selectChange(selection) {
	 headerRef.value.selectRows=selection;
}
   function handleRowClassName({row,rowindex}){
	   if(row.isplan&&row.isplan!="0"){
	   		   return 'selectedtr';
	   } else{
		   return '';
	   }
   }
   function addItem(row){
	   var data={};
	   data.warehouseid=state.queryParams.warehouseid;
	   data.amount=parseInt(row.amount);
	   data.materialid=row.id;
	   if(data.amount>0){
		   consumableApi.save(data).then((res)=>{
		   		   if(res.data){
		   			   ElMessage.success("加入成功");
					   row.isplan=true;
					   row.planamount=row.amount;
		   			   headerRef.value.getSummary();
		   		   }
		   });
	   }else{
		   ElMessage.error( "数量必须大于0");
		   row.amount=row.planamount;
	   }
	   
   }
   function delItem(row){
   	   var data={};
   	   data.warehouseid=state.queryParams.warehouseid;
   	   data.materialid=row.id;
   	   consumableApi.deleteItem(data).then((res)=>{
			    ElMessage.success("移除成功");
			    row.isplan=false;
				row.amount="";
			    headerRef.value.getSummary();
   	   });
   }
   function showCyclePopover(row){
	   if(row.stockingCycle&&row.stockingCycle!="0"){
	      row.oldstockingCycle=row.stockingCycle;
	   }else{
		   row.oldstockingCycle="";
	   }
   }
   function updateCycle(row){
	   warehouseApi.updateStockByChange({"materialid":row.id,"warehouseid":state.queryParams.warehouseid,
	   "stockingcycle":parseInt(row.oldstockingCycle)}).then((res)=>{
			if(res.data==true){
				ElMessage.success("操作成功");
				row.stockingCycle=row.oldstockingCycle;
				row.scVisible=false;
			}
	   });
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
	 .selectedtr {
	
	         }
	 .selectedtr td:nth-child(n+1)
	 {
			  background-color: #fff3ec !important;
	 }
 
	 .selectedtr td:first-child {
			  background-image: url('@/assets/image/pages/puchaseplan/shipment_plan_checked.png')!important;
		      background-color: #fff3ec!important;
			  background-repeat: no-repeat!important;
			  background-size: 42px 42px!important;
	 }
	 
	 .dark .selectedtr td:nth-child(n+1){
					   background-color: #302d2c !important;
	 }
	 .dark .selectedtr td:first-child{
					   background-image: url('@/assets/image/pages/puchaseplan/shipment_plan_checked.png');
		               background-color: #302d2c !important;
					   background-repeat: no-repeat;
					   background-size: 42px 42px;
	 }
</style>
