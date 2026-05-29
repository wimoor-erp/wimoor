<template>
	<div class="expend-bor ">
	<el-table 
	 :row-class-name="handleRowClassName" 
	 :data="tableData"  
	 :empty-text="status.loading?'加载中...':'平台SKU皆已失效或隐藏 , 请在计划中忽略或者隐藏此本地SKU'"
	 size="small">
		<el-table-column label="FBA仓库" prop="marketname"   header-align="center" align="center" >
			<template #default="scope">
				<el-link v-if="scope.row.url" :href="scope.row.url" class="font-12" target="_blank">{{scope.row.groupname}}-{{scope.row.marketname}}</el-link>
				<el-link v-else-if="scope.row.marketplaceid=='EU'" class="font-12" @click="handleShowEuData(scope.row)"  >
				{{scope.row.groupname}}-{{scope.row.marketname}}</el-link>
				<div v-else class="font-12">{{scope.row.groupname}}-{{scope.row.marketname}}</div>
			</template>
		</el-table-column>
		<el-table-column label="亚马逊sku"  prop="sku" header-align="center" align="center" min-width="120">
			<template #default="scope">
			   {{scope.row.sku}} 
			  <el-tag size="small" effect="plain" :type="scope.row.statuscolor" v-if="scope.row.statusname">{{scope.row.statusname}}</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="销量(7天|30天)" header-align="center" align="center" min-width="120"  prop="sales" >
			<template #default="scope">
				<span v-if="scope.row.sumseven"> {{scope.row.sumseven}} </span>
				<span v-else>0</span>
			   <span class="font-extraSmall">|</span> 
			   <span v-if="scope.row.summonth">{{scope.row.summonth}}  </span>
			   <span v-else>0</span>
			</template>
		</el-table-column>
		<el-table-column label="日均销量" min-width="90" header-align="center" align="center" >
			<template #default="scope">
				<div class="table-edit-flex" @click="handleAdjustSale(scope.row)">	
				<span v-if="scope.row.avgsales">{{scope.row.avgsales}}
				  <span v-if="scope.row.sysavgsales!=scope.row.avgsales" class="font-extraSmall"> ({{scope.row.sysavgsales}})</span>
				 </span>
				<span v-else-if="scope.row.sysavgsales">{{scope.row.sysavgsales}}</span>
				<span v-else>0</span>
				<el-icon ><Calendar/></el-icon>
				</div>
			</template>
		</el-table-column>
		<el-table-column label="备货周期" header-align="center" align="center" >
			<template #default="scope">
				<span v-if="scope.row.iseu">  </span>
				     <div class="table-edit-flex"
					 @click="e=>editStockCycle(e,scope.row)"
					 >
				     	<span>{{scope.row.shipday}}|
						<span class="font-extraSmall">{{getTransName(scope.row.transtype,transtypeOptions)}}</span>
						</span>
				     	<el-icon ><Edit/></el-icon>
				     </div>
			</template>
		</el-table-column>
		<el-table-column label="FBA库存" header-align="center" align="center" >
			<template #default="scope">
				<el-space>
				<span v-if="scope.row.iseu">
					<span v-if="scope.row.formqty" class="text-warning">{{parseInt(scope.row.quantity)+parseInt(scope.row.formqty)}}</span>
					<span v-else>{{scope.row.quantity}} </span>
				</span>
				 <div v-else
				 @click="e=>InventoryDetails(e,scope.row)"
				 @mouseenter="e=>InventoryDetails(e,scope.row)"
				  class="text-center pointer">
				  <span v-if="scope.row.formqty" class="text-warning">{{parseInt(scope.row.quantity)+parseInt(scope.row.formqty)}}</span>
				  <span v-else>{{scope.row.quantity}} </span></div>
				  <div v-if="scope.row.invage"><span class="font-extraSmall"  title="90天以上库龄">滞销:</span>
				  <span class="text-red">{{scope.row.invage}}</span></div>
				  </el-space>
			</template>
		</el-table-column>
		<el-table-column label="建议采购量" header-align="center" align="center" >
			<template #default="scope">
				 <span v-if="scope.row.iseu">  </span>
				 <div v-else>
				<span v-if="scope.row.needpurchase">
				<el-tooltip  placement="top">
				   <template #content>
					         <div >含供货周期内销量</div>
					   		 <div>更新时间:{{scope.row.opttime}}<el-button type="success" @click="handleRefresh(scope.row)" text size="small">重算</el-button></div>
					</template>
				   {{scope.row.needpurchase}}
				</el-tooltip>
				</span>
				<span v-else>0</span>
				</div>
			</template>
		</el-table-column>
		 
		<el-table-column label="可销售天数" header-align="center" align="center" >
			<template #default="scope">
				<span v-if="scope.row.iseu">  </span>
				<div v-else>
				<el-tooltip v-if="scope.row.salesday"
				        effect="dark"
				        placement="top"
				      >
					    <template #content>
					        <span>断货时间:{{dateFormat(scope.row.short_time)}}</span>
					      </template>
				      <span v-if="scope.row.salesday>=0&&scope.row.salesday<180">{{scope.row.salesday}}</span>
					  <span v-else>180+</span>
				      </el-tooltip>
				<span v-else>0</span>
				</div>
			</template>
		</el-table-column>
		<el-table-column label="采购后可销售天数" min-width="115" header-align="center" align="center" >
			<template #default="scope">
				<span v-if="scope.row.aftersalesday>=0&&scope.row.aftersalesday<180">{{scope.row.aftersalesday}}</span>
				<span v-else>180+</span>
			</template>
		</el-table-column>
		<el-table-column label="商品详情" header-align="center" align="center" >
			<template #default="scope">
				<el-icon v-if="scope.row.marketplaceid=='EU'">
					<CaretBottom @click="handleShowEuData(scope.row)" class="pointer"/> 
				</el-icon>
				<el-icon v-else @click="handleProductDeatils(scope.row)" class="font-medium ">
					<View />
				</el-icon>
			</template>
		</el-table-column>
	</el-table>
	</div>
</template>
<script setup>
	import { ref ,reactive,onMounted,toRefs,watch} from 'vue'
	import {Calendar,Edit,View,CaretBottom,EditPen} from '@element-plus/icons-vue';
 	import planApi from '@/api/erp/ship/planApi.js';
	import fbacycleApi from '@/api/amazon/inbound/fbacycleApi.js';
	import {dateFormat} from "@/utils/index";
	import { ElMessage, ElMessageBox } from 'element-plus'
	const props = defineProps({
					row:{
						default:[],
					},
					tableData:[],
					status:{},
					transtypeOptions:{}
	})
	const {
	   tableData,status,transtypeOptions,
	} = toRefs(props);
	defineExpose({
		handleSaveCycle,
	})
	const emit = defineEmits(['show-product-detail',
							  'show-sales-adjust',
							  'show-ship-record',
							  'show-split-row',
							  'show-eudata',
							  'show-inventory-details',
							  'show-stock-cycle',
							  'reload']);

 
	function handleSaveCycle(row){
		var stockingCycle=row.setstockingcycles;
		var transtype=row.settranstype;
		var formData={sku:row.sku,marketplaceid:row.marketplaceid,groupid:row.groupid}
		    formData.transtype=transtype;
		    formData.stockingcycle=stockingCycle;
		    fbacycleApi.updateStockCycleTranstype(formData).then(res=>{
			       ElMessage.success("保存成功");
			       emit("reload",props.row);
		    })
	}
	function handleRefresh(row){
		var formData={sku:row.sku,marketplaceid:row.marketplaceid,groupid:row.groupid};
		   planApi.refreshDataBySKU(formData).then(res=>{
			   emit("reload",props.row);
		   });
	}
 function handleRowClassName({row,rowindex}){
	   if(row["hide"]){
		    return 'hide';
	   }else if(row.iseu==true){
		   if(props.status.showeu==false){
	   		   return 'hide';
		   }else{
			 return '';
		   }
	   } else{
		   return '';
	   }
   }
	function getTransName(transtype,transtypeOptions){
		  if(transtypeOptions){
			  var name="";
			  transtypeOptions.forEach(item=>{
				  if(name==""&&(transtype+"")==item.id){
					  name =item.name;
				  }else{
					  return ;
				  }
			  })
			  return name;
		  }else{
			  return "";
		  }
	}
	function handleSplitRow(row){
		emit("show-split-row",row,props.row);
	}
	function handleShowEuData(row){
		emit("show-eudata",row,props.row);
	}
	function handleShipRecord(row){
		emit("show-ship-record",row,props.row);
	}
	function handleProductDeatils(row){
		emit("show-product-detail",row,props.row);
	}
	function handleAdjustSale(row){
		emit("show-sales-adjust",row,props.row);
	}
	function InventoryDetails(e,row){
		emit("show-inventory-details",e,row)
	}
    function editStockCycle(e,row){
		emit("show-stock-cycle",e,row)
	}
</script>

<style>
	.hide{
		display:none;
	}
	.expend-bor{
		border-top:2px solid var(--el-color-info-light-9);
		border-bottom:2px solid var(--el-color-info-light-9);
	}
	.purchase-plan-amz .expand-table .el-table__expanded-cell{
		padding:0px;
	}
	.purchase-plan-amz .expand-table .el-table__expanded-cell .el-table__cell{
		background-color:var(--el-fill-color-lighter);
	}
	.purchase-plan-amz .el-table__expanded-cell .table-edit-flex{
		text-align:center;
		display: block;
	}
	.purchase-plan-amz .expand-table .el-table__expanded-cell .el-table__inner-wrapper::before{background-color:var(--el-fill-color-lighter)}
	.purchase-plan-amz .m-b-8{
		margin-bottom:8px;
	}
	.purchase-plan-amz .font-12{
		font-size:12px;
	}
</style>
