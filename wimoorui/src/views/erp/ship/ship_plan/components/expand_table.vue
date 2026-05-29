<template>
	<div class="erp-ship-plan expend-bor">
	<el-table  
	:row-class-name="handleRowClassName" 
	:data="tableData" 
	:empty-text="status.loading?'加载中...':'平台SKU皆已失效或隐藏 , 请在计划中忽略或者隐藏此本地SKU'"
	 size="small">
		<el-table-column label="FBA仓库" prop="marketname"   header-align="center" align="center" >
			<template #default="scope">
				<el-link v-if="scope.row.url" :href="scope.row.url" target="_blank">{{scope.row.marketname}}</el-link>
				<el-link v-else-if="scope.row.marketplaceid=='EU'" @click="handleShowEuData(scope.row)" class="pointer">{{scope.row.marketname}}</el-link>
			    <div v-else>{{scope.row.marketname}}</div>
			</template>
		</el-table-column>
		<el-table-column label="亚马逊sku"  prop="sku" header-align="center" align="center" min-width="160">
			<template #default="scope">
			 <div >{{scope.row.sku}} </div>
			  <el-tag size="small" effect="plain" :type="scope.row.statuscolor" v-if="scope.row.statusname">{{scope.row.statusname}}</el-tag>
			</template>
			
		</el-table-column>
		<el-table-column label="销量(7天|30天)" header-align="center" align="center" min-width="100"  prop="sales" >
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
					@click="e=>editStockCycle(e,scope.row)">
				     	<span>{{scope.row.shipday}}
						| <span class="font-extraSmall"> {{getTransName(scope.row.transtype,transtypeOptions)}}</span>
						</span>
				     	<el-icon ><Edit/></el-icon>
				     </div>
			</template>
		</el-table-column>
		<el-table-column label="FBA库存" min-width="90" header-align="center" align="center" >
			<template #default="scope">
				<el-space>
				<div v-if="scope.row.iseu">
					<span v-if="scope.row.formqty" class="text-warning">{{parseInt(scope.row.quantity)+parseInt(scope.row.formqty)}}</span>
					<span v-else>{{scope.row.quantity}} </span>
				</div>
				 <div v-else
				  @click="e=>InventoryDetails(e,scope.row)"
				  @mouseenter="e=>InventoryDetails(e,scope.row)"
				  class="text-center pointer">
				  <span v-if="scope.row.formqty" class="text-warning">{{parseInt(scope.row.quantity)+parseInt(scope.row.formqty)}}</span>
				  <span v-else>{{scope.row.quantity}} </span>
				   <span class="font-extraSmall" v-if="scope.row.overseaqty">+{{getZeroValue(scope.row.overseaqty)}}</span>  
				 </div>
				 <div class="font-extraSmall pointer" v-if="scope.row.inbounddiff" @click="showWarningDialog(scope.row)">
				     <span v-if="scope.row.inbounddiff>0" >+{{scope.row.inbounddiff}} </span>  
									    <span v-else >{{scope.row.inbounddiff}} </span>  
				</div>  
				<div v-if="scope.row.invage"><span class="font-extraSmall"  title="90天以上库龄">滞销:</span>
				<span class="text-red">{{scope.row.invage}}</span></div>
				</el-space>
			</template>
		</el-table-column>
		<el-table-column label="建议发货量" header-align="center" align="center" >
			<template #default="scope">
				 <span v-if="scope.row.iseu">  </span>
				 <div v-else>
				<span >
				<el-tooltip  placement="top">
									   <template #content>
									         <div >含发货频率 {{scope.row.mincycle}} 天内销量：{{scope.row.ship_min_cycle_sale}}  </div>
											 <div>更新时间:{{scope.row.opttime}}<el-button type="success" @click="handleRefresh(scope.row)" text size="small">重算</el-button></div>
									    </template>
				  <span v-if="isOversea==true">{{scope.row.needshipfba}}</span> 
				  <span v-else>{{scope.row.needship}}</span> 
				</el-tooltip>
				</span>
				<!-- <span v-else>0</span> -->
				</div>
			</template>
		</el-table-column>
		<el-table-column label="实际发货量"  :min-width="status.subPlanEdit?200:110" header-align="center" align="center" >
			<template #header>
				<el-button size="small" v-if="status.isEdit"   type="success" link @click="handleSave()"  title="点击列名,可以保存编辑的内容">
					     实际发货量 <el-icon class="el-icon--right"><SuccessFilled /></el-icon>
				</el-button>
				<el-button size="small" v-else @click="handleEdit()"  type="default" link class="editfield" title="点击列名,可以将内容改为编辑状态" >
						 实际发货量 <el-icon class="el-icon--right"><Edit /></el-icon>
				</el-button>
			</template>
			<template #default="scope">
				 <div v-if="status.isEdit&&row[scope.row.key]&&row[scope.row.key].length>0"> 
					  <table>
						  <tr v-for="(subitem,index) in row[scope.row.key]" :key="index">
							  <td>
								  <el-input
								  style="width:200px;" 
								  :controls="false"  
								  size="small"
								  v-model="subitem.amount"
								  @input="subitem.amount=CheckInputInt(subitem.amount)"
								  >
									   <template #prepend>
										 <el-select style="width:70px"
													v-model="subitem.transtype" 
												   size="small"
												   placeholder="运输方式">
															<el-option  v-for="item in transtypeOptions"   
																		:key="item.id"  
																		:label="item.name" 
																		:value="item.id"    />
										 </el-select>
									   </template>
									   <template #append>
										<el-select size="small"
												   style="width: 70px"
												   v-model="subitem.overseaid" 
												   placeholder="发货仓库">
											<el-option  
																	 v-for="item in allOverseaOptions"   
																	:key="item.id"  
																	:label="item.name" 
																	:value="item.id"    />
										</el-select>
									   </template>
									 </el-input>
							  </td>
							  <td style="font-size:16px;padding-top:6px">
								  <el-icon class="el-icon--right  text-red pointer"  @click="row[scope.row.key].splice(index, 1);">
										   <RemoveFilled />
										   </el-icon>
							  </td>
						  </tr>
						  <tr>
							  <td colspan="3">
								  <el-button size="small"  type="warning" plain style="width:200px;float:left" @click="handleSplitRow(scope.row,'append')" :icon="CirclePlusFilled" ></el-button>
							  </td>
						  </tr>
					  </table>
				 </div>
				 
				 <div v-else-if="scope.row.subList&&scope.row.subList.length>0">
				 	     <div v-for="(subitem,index) in scope.row.subList" :key="index">
							  <div :class="index>0?'topborder':''">
								  {{subitem.amount}}
								  <span v-for="item in transtypeOptions" class="font-extraSmall" :key="item.id" >
									<span v-if="subitem.transtype==item.id" >
										{{item.name}}
									</span>  
								  </span>
								  <span v-if="subitem.overseaid&&subitem.overseaid!='0'&&allOverseaOptions"  class="font-extraSmall"  >
									  (
									  <span v-for="item in allOverseaOptions" :key="item.id" >
										  <span v-if="subitem.overseaid==item.id" >
										  		  {{item.name}}
										  </span>
									  </span>
									  )
								  </span>
								  <span v-else  class="font-extraSmall">(FBA)</span>
							  </div>
				 		  </div>
						  <el-icon v-if="status.isEdit&&!(scope.row.subnum>1&&scope.row.marketplaceid=='EU')"
						    class="font-extraSmall" 
						    @click="handleSplitRow(scope.row,'init')"><EditPen />
						  </el-icon>
				 </div>
				<div v-else>
					<el-space>
					<el-input-number   
					v-if="status.isEdit&&(scope.row.subnum<=1||!scope.row.subnum)" 
					style="width:80px;" :controls="false" size="small" 
					v-model="scope.row.amount"
					@blur.stop="e=>handleBlur(e,scope.row)"
					:inputmode ="scope.row.amount"
					@input="scope.row.amount=CheckInputInt(scope.row.amount)"></el-input-number>
					<span v-else>
					   <span v-if="scope.row.reallyamount">{{scope.row.reallyamount}}</span>
					   <span v-else>0</span>
					   <span v-if="scope.row.subnum>1" class="font-extraSmall"> (汇总) </span>
					</span>
					 <el-icon v-if="status.isEdit&&!(scope.row.subnum>1&&scope.row.marketplaceid=='EU')" 
					   class="font-extraSmall" 
					   @click="handleSplitRow(scope.row,'init')"><EditPen />
					 </el-icon>
					</el-space>
				</div>
			</template>
		</el-table-column>
		<el-table-column label="历史供货天数" header-align="center" align="center" >
			<template #default="scope">
				<div  
				 @click="e=>InvPlanDetails(e,scope.row)"
				 @mouseenter="e=>InvPlanDetails(e,scope.row)"
				 class="text-center pointer"> 
				 <span  v-if="scope.row.historicaldaysofsupply">
				 <span v-if="scope.row.fbainventorylevelhealthstatus=='Healthy'" class='text-green'> {{scope.row.historicaldaysofsupply}}</span>
				  <span v-else-if="scope.row.fbainventorylevelhealthstatus=='Low stock'" class='text-orange'> {{scope.row.historicaldaysofsupply}}</span>
				  <span v-else-if="scope.row.fbainventorylevelhealthstatus=='Out of stock'" class='text-red'> {{scope.row.historicaldaysofsupply}}</span>
				 	<span v-else> {{scope.row.historicaldaysofsupply}}</span>
				 </span>
				  <span  v-else>--</span>
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
		<el-table-column label="发货后可销售天数" min-width="115" header-align="center" align="center" >
			<template #default="scope">
				<span v-if="scope.row.aftersalesday>=0&&scope.row.aftersalesday<180">{{scope.row.aftersalesday}}</span>
				<span v-else>180+</span>
			</template>
		</el-table-column>
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
				<el-tag  type="danger" v-if="scope.row.status=='1'" round>{{scope.row.shipRecordStatusName}}</el-tag>
				 <el-tooltip  effect="dark"  v-else-if="scope.row.status=='5'" placement="top" >
				  <template #content>
					<span>到货时间:{{dateFormat(scope.row.shipRecordArrivalday)}}</span>
				  </template>
				  <el-tag size="small" type="success"  round> {{scope.row.shipRecordStatusName}}</el-tag>
				 </el-tooltip>
				<el-tag size="small"  v-else round>{{scope.row.shipRecordStatusName}}</el-tag>
				</el-space>
				<div v-else> 无</div>
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
	import {Calendar,Edit,View,CaretBottom,EditPen,WarningFilled,SuccessFilled,CirclePlusFilled,RemoveFilled} from '@element-plus/icons-vue';
 	import planApi from '@/api/erp/ship/planApi.js';
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
					transtypeOptions:{},
					overseaOptions:[],
					allOverseaOptions:[],
					editSub:{}
	})
	const {
	   tableData,status,transtypeOptions,isOversea,overseaOptions,editSub,allOverseaOptions
	} = toRefs(props);
 
	defineExpose({
		handleSaveCycle,
	})
	const emit = defineEmits(['show-product-detail',
							  'show-sales-adjust',
							  'show-ship-record',
							  'show-split-row',
							  'show-eudata',
							  'show-stock-cycle',
							  'show-inventory-details',
							  'show-invplan-details',
							  'reload',
							  'edit-row',
							  'add-sub',
							  'show-fba-warning']);
	function getZeroValue(value){
		if(value){
			return value;
		}else{
			return 0;
		}
	}
	function showWarningDialog(row){
	  emit('show-fba-warning',row);
	}
	function handleBlur(e,row){
		const evt = e || window.e || window.event;
		var input=$(evt.currentTarget).find("input");
		if(input){
			input.val(row.amount);
		}
	}
	function handleSaveCycle(row){
		if(row.setstockingcycles<0){
			ElMessage.error("备货周期必须大于等于0");
			return;
		}
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
	function handleSplitRow(item,type){
		  emit("add-sub",props.row,item,type);
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

<style scoped>
.topborder{
	border-top:1px solid #dedede;
}
</style>
