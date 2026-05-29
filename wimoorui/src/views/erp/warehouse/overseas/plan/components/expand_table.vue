<template>
	<div class="erp-ship-plan expend-bor">
	<el-table  
	:row-class-name="handleRowClassName" 
	:data="tableData" 
	:empty-text="status.loading?'加载中...':'平台SKU皆已失效或隐藏 , 请在计划中忽略或者隐藏此本地SKU'"
	 size="small">
		<el-table-column label="海外仓库" prop="localwarehousename"  show-overflow-tooltip="" min-width="200" header-align="center" align="center" >
		</el-table-column>
		<el-table-column label="平台sku"  prop="sku" header-align="center" align="center" min-width="160">
			<template #default="scope">
			 <div >{{scope.row.sku}} </div>
			  <el-tag size="small" effect="plain" :type="scope.row.statuscolor" v-if="scope.row.statusname">{{scope.row.statusname}}</el-tag>
			</template>
			
		</el-table-column>
		<el-table-column label="销量(7天|30天)" header-align="center" align="center" min-width="100"  prop="sales" >
			<template #default="scope">
				<span v-if="scope.row.sales7"> {{scope.row.sales7}} </span>
				<span v-else>0</span>
			   <span class="font-extraSmall">|</span> 
			   <span v-if="scope.row.sales30">{{scope.row.sales30}}  </span>
			   <span v-else>0</span>
			</template>
		</el-table-column>
		<el-table-column label="日均销量" min-width="90" header-align="center" align="center" >
			<template #default="scope">
				 <div class="table-edit-flex"   >
				  <span v-if="scope.row.msalesavg">{{scope.row.msalesavg}}
				 						 <span v-if="scope.row.salesavg!=scope.row.msalesavg && scope.row.salesavg" class="font-extraSmall"> ({{scope.row.salesavg}})</span>
				 						</span>
				  <span v-else-if="scope.row.salesavg">{{scope.row.salesavg}}</span>
				  <span v-else>0</span>
				  <el-icon @click="showSales(scope.row)" ><Edit/></el-icon>
				  </div>
			</template>
		</el-table-column>
		<el-table-column label="备货周期" header-align="center" align="center" >
			<template #default="scope">
				     <div class="table-edit-flex"
					@click="e=>editStockCycle(e,scope.row)">
				     	<span>{{scope.row.stocking_cycle}}
						| <span class="font-extraSmall"> {{ scope.row.transtypename }}</span>
						</span>
				     	<el-icon ><Edit/></el-icon>
				     </div>
			</template>
		</el-table-column>
		<el-table-column label="待入库" prop="inbound"   width="80" header-align="center" align="center">
			<template #default="scope">
					 {{scope.row.inbound}}
				</template>
		</el-table-column>
		<el-table-column label="海外仓可用库存"  width="120" prop="fulfillable" header-align="center" align="center" >
			<template #default="scope">
				 <span v-if="scope.row.fulfillable">{{scope.row.fulfillable}}</span>
				  <span v-else>0</span> 
			</template>
		</el-table-column>
		<el-table-column label="建议发货量" header-align="center"  width="110"  prop="shipqty" align="center" >
			<template #default="scope">
				 <span v-if="scope.row.shipqty">{{scope.row.shipqty}}</span> 
				  <span v-else>0</span> 
			</template>
		</el-table-column>
		<el-table-column label="实际发货量"   width="115" header-align="left" align="left" >
			<template #header>
				<el-button size="small" v-if="status.isPEdit"   type="success" link @click="handleSave()"  title="点击列名,可以保存编辑的内容">
					     实际发货量 <el-icon class="el-icon--right"><SuccessFilled /></el-icon>
				</el-button>
				<el-button size="small" v-else @click="handleEdit()"  type="default" link class="editfield" title="点击列名,可以将内容改为编辑状态" >
						 实际发货量 <el-icon class="el-icon--right"><Edit /></el-icon>
				</el-button>
			</template>
			<template #default="scope">
				<el-input-number 
				v-if="status.isPEdit" 
				style="width:80px;" :controls="false" size="small" 
				v-model="scope.row.amount"
				@blur.stop="e=>handleBlur(e,scope.row)"
				@input="scope.row.amount=CheckInputInt(scope.row.amount)"></el-input-number>
				<span v-else>
				   <span v-if="scope.row.reallyamount">{{scope.row.reallyamount}}</span>
				   <span v-else>0</span>
				  
				</span>
				 <el-icon v-if="status.isPEdit&&scope.$index==tableData.length-1" 
				   class="font-extraSmall" style="float:right;padding-top:10px" 
				   @click="handleSplitRow(scope.row)"><CirclePlusFilled />
				 </el-icon>
			</template>
		</el-table-column>
		
		<el-table-column label="可销售天数" header-align="center" align="center" >
			<template #default="scope">
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
			</template>
		</el-table-column>
		<!-- <el-table-column label="发货后可销售天数" min-width="115" header-align="center" align="center" >
			<template #default="scope">
				<span v-if="scope.row.aftersalesday>=0&&scope.row.aftersalesday<180">{{scope.row.aftersalesday}}</span>
				<span v-else>180+</span>
			</template>
		</el-table-column> -->
		<el-table-column label="近期发货记录" min-width="180" header-align="center" align="center" >
			<template #default="scope">
				<el-space v-if="scope.row.shipRecordQuantity>0" @click="handleShipRecord(scope.row)">
				<span class="pointer font-extraSmall" v-if="scope.row.shipRecordDay=='今日'" >
				<el-tag size="small" type="danger">{{scope.row.shipRecordDay}}</el-tag>
				</span>
				<span class="pointer font-extraSmall" v-else >
				{{scope.row.shipRecordDay}}
				</span>
				<span class="pointer " style="padding-left:5px;">{{scope.row.shipRecordQuantity}}</span>
				<el-tag v-if="scope.row.shipRecordStatus==1"  size="small" effect="plain" round type="info" >待审核</el-tag>
				<el-tag v-if="scope.row.shipRecordStatus==2"  size="small" effect="plain" round type="primary">配货中</el-tag>
				<el-tag v-if="scope.row.shipRecordStatus==3"  size="small" effect="plain" round type="success">已发货</el-tag>
				<el-tag v-if="scope.row.shipRecordStatus==4"  size="small" type="success" round effect="plain">已完成</el-tag>
				<el-tag v-if="scope.row.shipRecordStatus==5"  size="small" type="danger" round effect="plain">已驳回</el-tag>
				 <el-tooltip  effect="dark"  v-else-if="scope.row.status=='5'" placement="top" >
				  <template #content>
					<span>到货时间:{{dateFormat(scope.row.shipRecordArrivalday)}}</span>
				  </template>
				 </el-tooltip>
				</el-space>
				<div v-else> 无</div>
			</template>
		</el-table-column>
	</el-table>
	</div>
	
	<el-dialog :title="titles"
	 :append-to-body="true" 
	           v-model="saleVisible"
					width="25%"
			   >
			<el-input  v-model="localRow.presales" ></el-input>	   
			 <template #footer>
			 	<span class="dialog-footer">
			 		<el-button @click="saleVisible = false">关闭</el-button>
					<el-button type="primary" @click="saveSales">提交</el-button>
			 	</span>
			 </template>
		</el-dialog>
</template>
<script setup>
	import { ref ,reactive,onMounted,toRefs,watch} from 'vue'
	import {Calendar,Edit,View,CaretBottom,EditPen,WarningFilled,SuccessFilled,CirclePlusFilled} from '@element-plus/icons-vue';
 	import planApi from '@/api/erp/ship/planApi.js';
	import orderPlanApi from '@/api/erp/order/orderPlanApi.js';
	import fbacycleApi from '@/api/amazon/inbound/fbacycleApi.js';
	import {dateFormat,CheckInputInt} from "@/utils/index";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import $ from 'jquery';
	const props = defineProps({
					row:{
						default:[],
					},
					tableData:[],
					status:{},
					isOversea:false,
					transtypeOptions:{}
	})
	const {
	   tableData,status,transtypeOptions,isOversea,
	} = toRefs(props);
	
	const state = reactive({
		saleVisible:false,
		titles:'日均销量设置',
		localRow:null,
	});
	
	const {
	  saleVisible,
	  titles,
	  localRow,
	} = toRefs(state);
	
	defineExpose({
		handleSaveCycle,
	})
	const emit = defineEmits([
							  'show-sales-adjust',
							  'show-ship-record',
							  'show-split-row',
							  'show-stock-cycle',
							  'show-inventory-details',
							  'show-invplan-details',
							  'reload',
							  'edit-row',
							  'show-fba-warning']);
	function getZeroValue(value){
		if(value){
			return value;
		}else{
			return 0;
		}
	}
	function handleBlur(e,row){
		const evt = e || window.e || window.event;
		var input=$(evt.currentTarget).find("input");
		if(input){
			input.val(row.amount);
		}
	}
	function showWarningDialog(row){
	  emit('show-fba-warning',row);
	}
	function handleSaveCycle(row){
		if(row.stocking_cycle<0){
			ElMessage.error("备货周期必须大于等于0");
			return;
		}
		var stockingCycle=row.stocking_cycle;
		var transtype=row.settranstype;
		var formData={sku:row.sku,warehouseid:row.localwarehouseid}
		    formData.transtype=transtype;
		    formData.stockingcycle=stockingCycle;
		    orderPlanApi.updateStockCycleTranstype(formData).then(res=>{
			       ElMessage.success("保存成功");
			       emit("reload",props.row);
		    })
	}
	function showSales(row){
		state.localRow=row;
		state.titles="日均销量设置："+row.sku;
		state.saleVisible=true;
	}
	
	function saveSales(){
		var row=state.localRow;
		var data={};
		data.sku=row.sku;
		data.warehouseid=row.localwarehouseid;
		data.country=row.country;
		data.msalesavg=row.presales;
		orderPlanApi.saveSales(data).then((res)=>{
			 ElMessage.success("保存成功");
			 state.saleVisible=false;
			 emit("reload",props.row);
		});
	}
	
 function handleRowClassName({row,rowindex}){
	   if(row["hide"]){
		    return 'hide';
	   }else{
		   return '';
	   }
   }
   function handleRefresh(row){
	   var formData={sku:row.sku,marketplaceid:row.marketplaceid,groupid:row.groupid};
	   planApi.refreshDataBySKU(formData).then(res=>{
		   emit("reload",props.row);
	   });
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
	function handleEdit(){
		emit("edit-row",props.row);
	}
	function handleSave(){
		emit("save-row",props.row);
	}
function handleSplitRow(row) {
		emit("show-split-row",row,props.row);
	}
	function handleShipRecord(row){
		emit("show-ship-record",row,props.row);
	}
	function handleAdjustSale(row){
		emit("show-sales-adjust",row,props.row);
	}
    function editStockCycle(e,row){
 	   emit("show-stock-cycle",e,row)
     }
	 function InventoryDetails(e,row){
	 	emit("show-inventory-details",e,row)
	 }
	 function InvPlanDetails(e,row){
	 	emit("show-invplan-details",e,row)
	 }
</script>

<style>

</style>
