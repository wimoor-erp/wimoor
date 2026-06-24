<template>
 <div class="el-white-bg">
	 <Header ref="myHeaderRef" @change = "handleQuery" @expend="handlerExpend"/>
	 <div class=" ship-expand-table"  style="padding-bottom:16px;">
		 <GlobalTable ref="globalTableRef"
		 :tableData="tableData"  
		 :size="50"
		 @loadTable="loadTableData" 
		 :defaultSort="{ prop: 'marketneedship', order: 'descending' }"  
		 rowKey="id"
		 :defaultExpandAll="expendall"
		 @row-click="tableRowClick"
		 :rowClassName="handleRowClassName"
		 @expandChange="handleExpandChange"
		 >
		<template #field>
		<el-table-column type="expand" width="48">
			 <template #header>
				  <el-button v-if="isExpendAll&&expendall==false" @click="expendAll()" key="true"   type="info"  link
				       > <el-icon><ArrowDownBold /></el-icon></el-button>
				  <el-button v-else-if="expendall==false" @click="expendAll()"  key="false" type="info"  link
				     > <el-icon><ArrowRightBold /></el-icon></el-button>
			 </template>
			 <template #default="props">
				<!-- 展开的table -->
				<Expandtable :tableData="props.row.expendData"  
				             :row="props.row"
							  ref = "expendTableRef"
							 :isOversea="queryParams.isOversea"
							 :transtypeOptions="transtypeOptions"
							 :overseaOptions="overseaOptions"
							 :allOverseaOptions="allOverseaOptions"
							 @reload="loadDetail"
							 @show-sales-adjust="handleShowSalesAdjust"
							 @show-product-detail="handleShowProductDetail"
							 @show-ship-record="handleShowShipRecord"
							 @show-eudata="handleShowEUData"
							 @show-split-row="showSplitRow"
							 @show-stock-cycle = "handleShowStockCycle"
							 @show-inventory-details = "handleShowInventory"
							 @show-invplan-details = "handleShowInvPlan"
							 @show-fba-warning="handleShowFBAWarning"
							 @edit-row="handleAdd"
							 @save-row="submitForm"
							 @add-sub="handlAddSub"
							 :status="props.row.rowstatue"/> 
			 </template>
		</el-table-column>
		<el-table-column prop="img" label="图片" width="65" >
		   <template #default="scope">
		  <img v-if="scope.row.image" :src="scope.row.image" class="product-img"/>
		  <img v-else :src="$require('empty/noimage40.png')" class="product-img"/>
		  </template>
		</el-table-column>
		<el-table-column prop="sku" label="名称/SKU" :sort-orders="sortOrders" sortable='custom'  >
		   <template #default="scope">
		      <div   class="font-sku  pointer"  :underline="false" >
						<span  @click.stop="productDetails(scope.row)" >{{scope.row.sku}} </span>
			    <copy  @click.stop="CopyText(scope.row.sku)" title='复制SKU' theme="outline" style="padding-left:3px;" size="14" fill="#666" :strokeWidth="3"/>
			    <el-icon v-if="scope.row.notepad" @click.stop="showNotepad(scope.row.notepad)"  style="padding-left:20px;color: var(--el-text-color-placeholder)"><Link></Link></el-icon> 
				</div>
			   <el-tooltip :content="scope.row.name" :show-after="200">
				   <div class='font-name text-omit-1'>{{scope.row.name}}</div>
			   </el-tooltip>
			   <div>
			  <div>
			 <el-space wrap>
			<span v-if="scope.row.issfg=='1'"   @click.stop="handleEmtpy">
				<el-tag
				 title="组合产品"
				 @click.stop="e=>handleAssemblyShow(e,scope.row)"
				 @mouseenter="e=>handleAssemblyShow(e,scope.row)"
				 type="warning" class="pointer" v-if="scope.row.issfg=='1'"
				 size="small" 
				>组合</el-tag>
			</span>
			<template v-if="scope.row.tagNameList">
				<el-tag 
								effect="plain" 
								:type="item.color"  
								v-for="item in scope.row.tagNameList" 
								:key="item.id"
								size="small" >
			                {{item.name}}
		            </el-tag>
			</template>
			  
			  </el-space>
			  </div>
			  </div>
		  </template>
		</el-table-column>
		<el-table-column label="报表" width="80">
			<template #default="scope">
				<el-space :size="3" direction="vertical">
					   <el-button text :underline="false" @click.stop="handlesaleChart(scope.row)">
						   <el-tooltip content="销量报表" placement="right" :hide-after="0" :show-after="200">
					   <chart-histogram class="ic-cen" theme="outline" size="16" fill="#ff6700" :strokeWidth="4"/>
					   </el-tooltip>
					   </el-button>   
				</el-space>
					<el-button text :underline="false"  @click.stop="handlarrivalChart(scope.row)">
											   <el-tooltip content="预计到货报表" placement="right" :hide-after="0" :show-after="200">
					<chart-line class="ic-cen" theme="filled" size="15" fill="#529b2e"/>
					 </el-tooltip>
					 </el-button>
			</template>
		</el-table-column>
		
		<el-table-column label="发货需求量" 
		:sort-orders="sortOrders" prop="marketneedship"  
		sortable='custom'  width="125">
		<template #default="scope">
			<div v-if="queryParams.plansimple">
				<div   v-if="queryParams.isOversea">{{scope.row.needshipfba}}</div>
				<div   v-else>{{scope.row.marketneedship}}</div>
			</div>
			<div v-else>
				<div   v-if="queryParams.isOversea">{{scope.row.needshipfba}}</div>
				<div   v-else>{{scope.row.needship}}</div>
			</div>
		</template>
		</el-table-column>
		<el-table-column label="实际发货总量" sortable prop="amount" width="125">
			<template #default="scope">
				 <div v-if="queryParams.plansimple">
					<div class="font-bold" v-if="scope.row.marketamount">{{scope.row.marketamount}}</div>
					<div class="font-bold" v-else>0</div>
				</div>
				<div v-else>
				<div class="font-bold" v-if="scope.row.amount">{{scope.row.amount}}</div>
				<div class="font-bold" v-else>0</div>
				</div> 
			</template>
		</el-table-column>
		<el-table-column label="可用库存" :sort-orders="sortOrders" sortable='custom' prop="allquantity" width="100">
			<template #default="scope">
				<div
				@click.stop="e=>getInventory(e,scope.row)"
				@mouseenter="e=>getInventory(e,scope.row)"
				 >
			         <div class="text-center pointer" >
							<span class="font-bold" v-if="scope.row.quantity">{{scope.row.quantity}}</span>
					        <span class="font-bold"  v-else >0</span>
							 <el-tooltip v-if="scope.row.quantityShelf" :content="'上架数量：'+scope.row.quantityShelf" >
							    <document-folder v-if="scope.row.quantityShelf==scope.row.quantity" theme="outline" size="12" fill="#67C23A"/>
							    <document-folder v-else theme="outline" size="12" fill="#E6A23C"/>
							  </el-tooltip>
					 </div>
					</div>
				 
					<div v-if="scope.row.canAssembly">
					  
					   <el-tooltip v-if="scope.row.canAssemblyShelf" :content="'上架子产品可组装数量：'+scope.row.canAssemblyShelf"  >
						     <span v-if="scope.row.canAssemblyShelf==scope.row.canAssembly" class="font-extraSmall text-green" >可组装:{{scope.row.canAssembly}} </span>
						     <span v-else class="font-extraSmall text-orange" >可组装:{{scope.row.canAssembly}} </span>
					  </el-tooltip>
					  <span v-else class="font-extraSmall" >可组装:{{scope.row.canAssembly}} </span>
					</div>
					 
					<div  v-if="scope.row.otheramount" title="已经在其他计划被使用的数量">
					 <span class="font-extraSmall" >已使用:</span>
					 <span class="text-red font-small">{{scope.row.otheramount}}</span>
					</div>
			</template>
			
		</el-table-column>
		<el-table-column label="公告" >
			<template #default="scope">
					<span class="table-edit-flex" >
						<span class="font-small" v-html="scope.row.htmlnotice"></span>
						<el-icon @click.stop="editRemarks(scope.row)"><Edit/></el-icon>
					</span>
			</template>
		</el-table-column>
		<el-table-column label="操作" width="160">
			<template #default="scope">
				<div class="flex-center-bew" @click.stop="handleEmtpy" >
				<el-link @click.stop="handleEmtpy"  :underline="false" type="primary" class="font-small">
					<div v-if="scope.row.rowstatue.isEdit"  class="opt-td-div">
						<span  @click.stop="submitForm(scope.row)">保存</span>
						<span  @click.stop="handleCancel(scope.row)" style="margin-left:20">取消</span> 
					</div>
					<div v-else class="opt-td-div">
						<span   @click.stop="handleAdd(scope.row)" >编辑</span>
						
						<span v-if="scope.row.rowstatue.isplan" 
						       @click.stop="handleDelete(scope.row)" >  移除</span> 
						
					</div>
				</el-link>
				<span
				 @click.stop="e=>handleProductShowHide(e,scope.row)"
				  @mouseenter="e=>handleProductShowHide(e,scope.row)"
				 >
				  <more-one class="ic-cen pointer" theme="outline" size="20" fill="#333" :strokeWidth="3"/>
				</span>
				</div>
			</template>
		</el-table-column>
     </template>
 </GlobalTable>
	</div>
	<SalechartDialog ref="salechartRef"/>
	<ArrivalDialog ref="arrivalchartRef"/>
	<RemarksDialog ref="remarksRef" @confirm="remarkConfirm"/>
    <SaleAdjustDialog ref="saleAdjustDialogRef" @confirm="loadDetail"></SaleAdjustDialog>
	<GoodsDeatils ref="goodsDeatilsRef"  />
	<ShipRecordDialog ref="shipRecordDialogRef"></ShipRecordDialog>
	<SplitPlanDialog ref="splitPlanDialogRef" 
	                 :transtypeOptions="transtypeOptions"
	                 @change="handleSplitRow">
	</SplitPlanDialog>
	</div>
	<!-- 备货周期 -->
	<el-popover
	   @hide="expenditem.stockVisible=false"
	   placement="top-start"
	   title="备货周期"
	   :width="300"
	  :visible="expenditem.stockVisible" 
	  :virtual-ref="stockCycleRef"
	  virtual-triggering
	  trigger="click"
	 >
	 <p class="font-extraSmall m-b-18" v-if="expenditem.cycle">
						  备货周期:{{expenditem.shipday}}天<br>=安全库存周期{{expenditem.cycle.stockingCycle}}天
						  +头程周期{{expenditem.cycle.firstLegDays}}天
						  +上架周期{{expenditem.cycle.putOnDays}}天
	 </p>
	  <el-row  >
	     <el-col :span="24" style="margin-top:10px"> 
						  安全库存周期
		</el-col>
	     <el-col :span="24"  > 
			 <el-input type="number" class="m-b-18" v-model="expenditem.setstockingcycles" clearable >  </el-input>
		</el-col>
	   </el-row>
	 <el-row  >
	    <el-col :span="24" style="margin-top:10px"> 
	 		   运输方式
	 	 </el-col>
	    <el-col :span="24"> 
	 		<el-select v-model="expenditem.settranstype" >
				<el-option  key="" value="" label="系统默认" >
								   系统默认
				</el-option>
	 		    <el-option
	 		      v-for="item in transtypeOptions"
	 		      :key="item.id"
	 		      :label="item.name"
	 		      :value="item.id"
	 		    > {{item.name}}
					<span class="font-extraSmall" v-if="expenditem.defaultTranstype==item.id">(默认)</span>
				</el-option>
	 		  </el-select>	 
	 	 </el-col>
	  </el-row>
	  <div style="padding-top:10px;text-align:right">
		    <el-button  @click.stop="expenditem.stockVisible = false">关闭</el-button>
	       <el-button type="primary" @click.stop="handlePSaveCycle(expenditem)">确定</el-button>
	  </div>
	 </el-popover>
	 <!-- 库存详情 -->
	 <el-popover  
	:virtual-ref="InventoryDetalisRef"
	virtual-triggering
	  trigger="click"  placement="top" width="240px" >
	  <ul class="border-line">
	 	 <li class="flex-center-between "><span class="pointer" @click="refreshInventory(expenditem)" title="点击可更新为实时库存">可用库存<el-icon v-loading="expenditem.loading" class="font-extraSmall"><RefreshRight /></el-icon></span><el-tag type="success">{{getZeroValue(expenditem.afn_fulfillable_quantity)}}</el-tag></li>
	 	 <li class="flex-center-between"><span class="font-extraSmall">预留库存<span  >(汇总)</span></span><el-tag type="info">{{getZeroValue(expenditem.reserved_qty)}}</el-tag></li>
	 	 <li class="flex-center-between"><span class="font-extraSmall">预留库存<span > (买家订单)</span></span><el-tag type="info">{{getZeroValue(expenditem.reserved_customerorders)}}</el-tag></li>
	 	 <li class="flex-center-between"><span>预留库存<span class="font-extraSmall"> (运营中心转运)</span></span><el-tag type="warning">{{getZeroValue(expenditem.reserved_fc_transfers)}}</el-tag></li>
	 	 <li class="flex-center-between"><span>预留库存<span class="font-extraSmall"> (运营中心处理中)</span></span><el-tag type="warning">{{getZeroValue(expenditem.reserved_fc_processing)}}</el-tag></li>
	 	 <li class="flex-center-between"><span>待入库库存<span class="font-extraSmall"> (正在发货)</span></span><el-tag >{{getZeroValue(expenditem.afn_inbound_working_quantity)}}</el-tag></li>
	 	 <li class="flex-center-between"><span>待入库库存<span class="font-extraSmall"> (待接收)</span></span><el-tag >{{getZeroValue(expenditem.afn_inbound_shipped_quantity)}}</el-tag></li>
	 	 <li class="flex-center-between"><span>待入库库存<span class="font-extraSmall"> (正在接收)</span></span><el-tag >{{getZeroValue(expenditem.afn_inbound_receiving_quantity)}}</el-tag></li>
	 	 <li class="flex-center-between" v-if="expenditem.overseaqty"><span>海外仓库存<span class="font-extraSmall">  </span></span><el-tag >{{getZeroValue(expenditem.overseaqty)}}</el-tag></li>
		 <li class="flex-center-between" v-if="expenditem.formqty"><span>发货单在途<span class="font-extraSmall">  </span></span><el-tag >{{getZeroValue(expenditem.formqty)}}</el-tag></li>
	  </ul>
	 </el-popover>
	 
	 <!-- 库存库龄详情 -->
	  <el-popover  
	 :virtual-ref="InvPlanDetalisRef"
	 virtual-triggering
	   trigger="click"  placement="top" width="640px" >
	   
	    <el-descriptions
	       class="margin-top"
	       :column="3"
	       :size="size"
		   v-if="expenditem.invplandata"
	       border
	     >
	       <el-descriptions-item label="30天库龄"> {{expenditem.invplandata.invAge0To30Days}} </el-descriptions-item>
		   <el-descriptions-item label="60天库龄"> {{expenditem.invplandata.invAge31To60Days}} </el-descriptions-item>
		   <el-descriptions-item label="90天库龄"> {{expenditem.invplandata.invAge61To90Days}} </el-descriptions-item>
		   <el-descriptions-item label="180库龄"> {{expenditem.invplandata.invAge91To180Days}} </el-descriptions-item>
		   <el-descriptions-item label="270库龄"> {{expenditem.invplandata.invAge181To270Days}} </el-descriptions-item>
		   <el-descriptions-item label="330天库龄"> {{expenditem.invplandata.invAge181To330Days}} </el-descriptions-item>
		   <el-descriptions-item label="7天发货量"> {{expenditem.invplandata.unitsShippedT7}} </el-descriptions-item>
		   <el-descriptions-item label="30天发货量"> {{expenditem.invplandata.unitsShippedT30}} </el-descriptions-item>
		   <el-descriptions-item label="60天发货量"> {{expenditem.invplandata.unitsShippedT60}} </el-descriptions-item>
		   <el-descriptions-item label="90天发货量"> {{expenditem.invplandata.unitsShippedT90}} </el-descriptions-item>
		   
		   <el-descriptions-item label="空间占用"> {{formatFloat(expenditem.invplandata.storageVolume)}}/{{formatFloat(expenditem.invplandata.itemVolume)}} </el-descriptions-item>
		   <el-descriptions-item label="建议">
		   <span class="text-orange" v-if="RecommendedAction[expenditem.invplandata.recommendedAction]">
			   {{RecommendedAction[expenditem.invplandata.recommendedAction]}}
		   </span>
		   <span class="text-orange" v-else>{{expenditem.invplandata.recommendedAction}}</span>
		   <span v-if="expenditem.invplandata.alert" class="text-red">/
			   <span v-if="expenditem.invplandata.alert=='Low conversion'">转化低</span>
			   <span v-else-if="expenditem.invplandata.alert=='Low traffic'">流量低</span>
			   <span v-else></span>
		   </span>
		   </el-descriptions-item>
		   
		   <el-descriptions-item label="库存健康状态"> {{expenditem.invplandata.fbaInventoryLevelHealthStatus}} </el-descriptions-item>
		   <el-descriptions-item label="低库存水平量"> {{expenditem.invplandata.fbaMinimumInventoryLevel}} </el-descriptions-item>
		   <el-descriptions-item label="持续供货天数"> {{expenditem.invplandata.daysOfSupply}} </el-descriptions-item>
		   <el-descriptions-item label="当前库存" v-if="expenditem.afn_fulfillable_quantity"> {{getZeroValue(expenditem.afn_fulfillable_quantity)+getZeroValue(expenditem.afn_reserved_quantity)-getZeroValue(expenditem.reserved_customerorders)}} </el-descriptions-item>
	     </el-descriptions>
	  </el-popover>
	 
	 
	 <!-- 可用库存 -->
	 <el-popover 
	   :virtual-ref="InventoryRef"
	   virtual-triggering
	   trigger="click" 
	   @show="handleQueryInventoryQty(editRow)" 
	   placement="top" 
	   width="240"  >
	  <ul class="border-line">
	 	 <li class="flex-center-between"><span>待入库库存</span>
	 	                                  <el-tag v-if="editRow.inbound"> {{editRow.inbound}}</el-tag>
	 									  <el-tag v-else> 0</el-tag>
	 	 </li>
	 	 <li class="flex-center-between"><span>可用库存</span>
	 	                                <el-tag type="success" v-if="editRow.fulfillable">{{editRow.fulfillable}}</el-tag>
	 									<el-tag type="success" v-else>0</el-tag>
	 	 </li>
	 	 <li class="flex-center-between"><span>待出库库存</span>
	 	                                <el-tag type="warning"  v-if="editRow.outbound">{{editRow.outbound}}</el-tag>
	 									<el-tag type="warning"  v-else>0</el-tag>
	 	 </li>
	 	 <li class="flex-center-between" v-if="editRow.canAssembly"><span>可组装库存</span><el-tag type="info">{{editRow.canAssembly}}</el-tag></li>
	 	 <li class="flex-center-between" v-if="editRow.canconsumable"><span>辅料齐套数</span><el-tag type="info">{{editRow.canconsumable}}</el-tag></li>
	  </ul>
	 </el-popover>
	 <!--隐藏菜单-->
	 <el-popover
	 placement="bottom"
	 popper-class="dropdown-popover"
	 :virtual-ref="productShowHideRef"
	 virtual-triggering
	 trigger="click"
	 >
	 <ul class='el-dropdown-menu'>
	 	<li class="el-dropdown-menu__item">
	 		<span v-if="editRow.ishide=='1'" @click.stop="showProduct(editRow)">显示产品</span>
	 		<span v-else  @click.stop="hideProduct(editRow)">隐藏产品</span>
	 	</li>
	 </ul>
	 </el-popover>
	 <el-dialog title="FBA库存差异"
	            v-model="fbaInvWarning.visible"
	            :append-to-body="true" 
				width="80%"
	 		   >
			   <el-tooltip
			   effect="dark"
			   placement="top"
			   :raw-content="true"
			   content="总库存 = FBA可用库存 + 货件待接收数量;<br>
			   FBA总库存 = FBA可用库存 + FBA待入库库存;<br>
			   待入库差异 = FBA待入库库存 - 货件待接收数量;
			   "
			   >
			<div id='invenchart' style='height:240px;width:100%'>
			 
			</div>
			</el-tooltip>
	 		 <template #footer>
	 		 	<span class="dialog-footer">
	 		 		<el-button @click="fbaInvWarning.visible = false">关闭</el-button>
	 		 	</span>
	 		 </template>
	 	</el-dialog>
	 <Confirm ref="confirmRef"></Confirm>
	 <!-- 组装产品 -->
	 <AssemblyDialog :assbRef="assbRef" @change="getAssembliyList(editRow)" :loading="editRow.assloading"
	  :assemblyList="editRow.assemblyList"/>
		<ContentDialog ref="notepadDialogRef"></ContentDialog>
</template>
<script>
export default {
  name: "FBA发货规划"
}
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,toRefs,nextTick} from 'vue'
	import Expandtable from "./components/expand_table.vue";
	import SalechartDialog from "@/views/amazon/listing/common/salechart.vue"
	import ArrivalDialog from"@/views/amazon/listing/common/arrival_dialog.vue";
	import ShipRecordDialog from "./components/ship_record.vue"
	import RemarksDialog from "./components/remarks_dialog.vue"
	import SaleAdjustDialog from "@/views/amazon/sales/forecast/components/sale_adjust_dialog.vue";
	import AssemblyDialog from "@/views/erp/components/assembly_dialog.vue";
	import SplitPlanDialog from "./components/split_plan_dialog.vue"
	import GoodsDeatils from "@/views/amazon/listing/common/goods_deatils.vue"
  import Header from "./components/header.vue";
  import Confirm from "@/components/dialog/confirm.vue";
	import ContentDialog from "@/views/sys/notepad/content_dialog.vue";   
	// API依赖
	import * as echarts from 'echarts';
	import planApi from '@/api/erp/ship/planApi.js';
	import inventoryApi from '@/api/erp/inventory/inventoryApi.js';
	import RecommendedAction from '@/model/amazon/ship/RecommendedAction.json';
	import {Edit,ArrowRightBold,ArrowDownBold,RefreshRight} from '@element-plus/icons-vue';
	import { ElMessage ,ElMessageBox} from 'element-plus'
	import {decodeText,formatFloat} from '@/utils/index.js';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import markApi from '@/api/erp/material/markApi.js';
	import {Help,MoreOne,ChartHistogram,ChartLine,Copy,DocumentFolder,Link} from '@icon-park/vue-next';
	import {sublit} from "@/api/erp/assembly/assemblyApi.js";
	import { useRouter } from 'vue-router';
	import CopyText from"@/utils/copy_text.js";
  import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
const router = useRouter()
const notepadDialogRef = ref();
   const expendTableRef = ref()
   const globalTableRef=ref();
   const salechartRef =ref();
   const confirmRef=ref();
   const arrivalchartRef =ref()
   const saleAdjustDialogRef=ref();
   const goodsDeatilsRef=ref();
   const shipRecordDialogRef=ref();
   const splitPlanDialogRef=ref();
   const assbRef =ref();
   const myHeaderRef =ref();
   const remarksRef =ref()
   const stockCycleRef = ref()
   const InventoryRef = ref()
   const InventoryDetalisRef = ref()
   const InvPlanDetalisRef = ref()
   const productShowHideRef = ref()
   const extendTableRefs=[];
   const state = reactive({
	 fbaInvWarning:{row:{},visible:false},
	 editRow:{},
	 expendall:false,
	 expenditem:{stockVisible:false},
     // 表格树数据
     tableData: {records:[],total:0}  ,
     // 弹窗属性
     dialog: { visible: false }  ,
	 sortOrders:[ 'descending','ascending', null],
     // 查询参数
     queryParams: {} ,
	 expendRows:[],
	 isExpendAll:false,
	 closeloading:false,
	 transtypeOptions:[],
	 overseaOptions:[],
	 allOverseaOptions:[],
     // 表单数据
     formData: { } ,
     // 表单参数校验
   });
   const {
	 fbaInvWarning,
     tableData,
     queryParams,
	 transtypeOptions,
	 overseaOptions,
	 allOverseaOptions,
	 sortOrders,
	 closeloading,
	 isExpendAll,
	 expendall,
     formData,
	 expenditem,
	 editRow,
   } = toRefs(state);
  function FBAinventoryChart(row){
	 var myChart = echarts.init(document.getElementById('invenchart'));
	 var data = [
		 {name:'总库存',value:row.quantity,},
		 {name:'待入库差异',value:row.inbounddiff,},
		 {name:'货件待接收数量',value:row.localinbound,},
		 {name:'FBA待入库库存',value:row.fbainbound,},
		 {name:'FBA可用库存',value:row.localquantity,},
		 {name:'FBA总库存',value:row.oldquantity,},
		 ]
	var names = data.reduce((pre,cur)=>pre.concat(cur.name),[])	;
	var values =[];
	 data.forEach((item)=>{
		 if(item.value==0||item.value=='0'){
			 values.push({
				           value:0,
				           itemStyle: {
				             color: '#f2f2f2',
				           }
			 })
		 }else{
			 values.push(item.value)
		 }
	 })
	 var option = {
		   grid: {
		     left: '0',
		     right: '0',
		     bottom: '0',
		     containLabel: true
		   },
		     xAxis: {
		      type: 'category',
		      data:names,
		     },
		     yAxis: {
		        show: false,
			    type: 'value'
		     },
			  series:[
				  {
				  type: 'bar',
				  color:['#ff7315'],
                  barMinHeight:20,
				  label: {
				        show: true
				      },
				  data: values,
				  },
			  ],
	 }
	 myChart.setOption(option,true);
	 window.addEventListener('resize',()=>{
	 myChart.resize();
	 })
  }	
   function handleEmtpy(){
	   
   }
   function handleAssemblyShow(e,row){
   		 const evt = e || window.e || window.event;
   		 assbRef.value = evt.currentTarget
   		 state.editRow = row
   		 state.editRow.oldwarehouseid=state.editRow.warehouseid;
   	}
	function addInventoryQty(value){
		if(value){
			state.expenditem.quantity=state.expenditem.quantity+parseInt(value);
		}
}
function showNotepad(id){
	  notepadDialogRef.value.show(id);
	}
	function refreshInventory(rows){
				 //刷新fba库存
				 state.expenditem.loading=true;
				 inventoryRptApi.syncInventorySupply({"skus":rows.sku,"groupid":rows.groupid,"marketplaceid":rows.marketplaceid}).then((res)=>{
					 state.expenditem.loading=false;
					 if(res.data){
						 ElMessage.success('更新成功！');
						 state.expenditem.afn_fulfillable_quantity=res.data.afnFulfillableQuantity;
						 state.expenditem.reserved_qty=res.data.afnReservedQuantity;
						 if(res.data.resdetail){
							  state.expenditem.reserved_customerorders=res.data.resdetail.reservedCustomerorders;
							  state.expenditem.reserved_fc_transfers=res.data.resdetail.reservedFcTransfers;
							  state.expenditem.reserved_fc_processing=res.data.resdetail.reservedFcProcessing;
						 }
						 state.expenditem.afn_inbound_working_quantity=res.data.afnInboundWorkingQuantity;
						 state.expenditem.afn_inbound_shipped_quantity=res.data.afnInboundShippedQuantity;
						 state.expenditem.afn_inbound_receiving_quantity=res.data.afnInboundReceivingQuantity;
						 state.expenditem.reserved_qty=res.data.afnReservedQuantity;
						 state.expenditem.quantity=0;
						 addInventoryQty(state.expenditem.afn_fulfillable_quantity);
						 addInventoryQty(state.expenditem.reserved_fc_transfers);
						 addInventoryQty(state.expenditem.reserved_fc_processing);
						 addInventoryQty(state.expenditem.afn_inbound_working_quantity);
						 addInventoryQty(state.expenditem.afn_inbound_shipped_quantity);
						 addInventoryQty(state.expenditem.afn_inbound_receiving_quantity);
					 }else{
						 ElMessage.error('更新失败！');
					 }
				 })
				 
	}
   function handleShowFBAWarning(row){
	   state.fbaInvWarning.row=row;
	   state.fbaInvWarning.visible=true;
	   nextTick(()=>{
		   FBAinventoryChart(row)
	   })
   }
   function remarkConfirm(formdata){
	   state.tableData.records.forEach(row=>{
		   if(row.id==formdata.materialid){
			   row.notice=formdata.mark;
			   row.htmlnotice=decodeText(row.notice);
		   }
	   })
   }
    function getAssembliyList(row){
		if(!row["assemblyList"]){
			row.assloading=true;
			sublit({materialid:row.id,warehouseid:state.queryParams.warehouseid}).then(res=>{
				row.assemblyList=res.data;
				row.assloading=false;
			});
		}
	}
	function handleQuery(params){
		state.queryParams=params;
		 warehouseApi.getOversaWarehouse({"ftype":"oversea_usable",
		                                       "groupid":state.queryParams.groupid,
											   "country":null}).then((res)=>{
				res.data.push({"id":"0","name":"FBA仓"})
			    state.allOverseaOptions=res.data;
		 });
		state.tableData.records.forEach(item=>{
			if(state.expendRows.includes(item.id)){
				if(item.rowstatue){
				   item.rowstatue.isexpends=false;
				} 
				globalTableRef.value.toggleRowExpansion(item,false);
			}
		});
		globalTableRef.value.loadTable(params);
	}
	function handleQueryInventoryQty(row){
		if(!row["hasInvData"]){
			inventoryApi.getInventory({warehouseid:state.queryParams.warehouseid,materialid:row.id}).then(res=>{
				row.canconsumable=res.data.canconsumable;
				row.hasInvData=true;
			})
		}
	}
	function handlerExpend(value){
		if(value){
			state.expendall=value;
			state.queryParams.expendall=true;
			handleQuery(state.queryParams);
		}else{
			state.queryParams.expendall=false;
			state.expendall=false;
			state.isExpendAll=false;
			state.tableData.records.forEach((row,index)=>{
				  if(state.expendRows.includes(row.id)){
					  globalTableRef.value.toggleRowExpansion(row,false);
				  }
			});
			state.expendRows=[];
			handleQuery(state.queryParams);
		}
		
	}
	function expendAll(){
		if(state.isExpendAll==true){
			state.closeloading=true;
			state.isExpendAll=false;
			state.tableData.records.forEach((row,index)=>{
				   if(state.expendRows.includes(row.id)){
				      globalTableRef.value.toggleRowExpansion(row,false);
				   }
			});
			state.expendRows=[];
		}else{
		   state.isExpendAll=true;
		   state.tableData.records.forEach((row,index)=>{
			     if(!state.expendRows.includes(row.id)){
					  state.expendRows.push(row.id);
			          globalTableRef.value.toggleRowExpansion(row,true);
				 }
		   })
		}
	}
   /* 销量报表 */
   function handlesaleChart(row){
	   var groupid=row.groupid;
	   var marketplaceid="";
	   var amazonAuthId="";
	   var sku="";
	   var msku=row.sku;
	   salechartRef.value.show(groupid,marketplaceid,amazonAuthId,sku,msku);
   }
	function handleShipHistory(row){
		 
	}
    function handleShowEUData(itemrow,parentrow){
		    var row =parentrow;
			var subrow=[];
			 if(!row["expendEuData"]||(row["expendEuSKU"]!=itemrow.sku)){
				var param={"groupid":row.groupid,
				           "sku":itemrow.sku,
						   "msku":parentrow.sku,
						   "warehouseid":state.queryParams.warehouseid,
						   "plantype":"ship",
						   "plansimple":state.queryParams.plansimple,
						   "marketplaceids":state.queryParams.marketplaceids,
						   "iseu":true,
						   "amount":0};
				row.rowstatue.loading=true;
				 planApi.getExpandCountryData(param).then(res=>{
								row.rowstatue.loading=false;
								if(res.data){
									var findex=row.expendData.length-1;
									if(row.expendData){
										row.expendData = row.expendData.filter(item => !item.iseu);
										row.expendData.forEach((item,index)=>{
											if(item.sku==itemrow.sku&&item.marketplaceid=='EU'){
												findex=index;
											}
										})
									}
									res.data.forEach(item=>{
										if(item.reallyamount&&parseInt(item.reallyamount)){
											item.amount=item.reallyamount;
										}
										item.visible=false;
										item.iseu=true;
										row.expendData.splice(findex+1,0,item);
									});
									row.expendData.forEach(item=>{
										item.key=item.groupid+item.marketplaceid+item.sku;
										if(row.rowstatue.isEdit==true){
											   if(item.subList&&item.subList.length>0){
												   row[item.key]=item.subList;
												   row.rowstatue.subPlanEdit=true;
											   }
										 }
									})
									row.expendEuData=res.data;
								}
								row.expendEuSKU=itemrow.sku;
							    row.rowstatue.showeu=true;
				})
			 } else{ 
				 row.expendEuSKU=itemrow.sku;
				 if(row.rowstatue.showeu==true){
				 	row.rowstatue.showeu=false;
				 }else{
				 	row.rowstatue.showeu=true;
				 }
			 }
	}
	function showSplitRow(itemrow,parentrow){
		splitPlanDialogRef.value.show(itemrow,parentrow,state.transtypeOptions);
	}
	function handleShowProductDetail(itemrow,parentrow){
		goodsDeatilsRef.value.show(itemrow);
	}
	function handleShowSalesAdjust(itemrow,parentrow){
		saleAdjustDialogRef.value.show(itemrow,parentrow);
	}
	function handleShowShipRecord(itemrow,parentrow){
		shipRecordDialogRef.value.show(itemrow,parentrow);
	}
	function handleSplitRow(splitRows,itemrow,parentrow){
		  if(splitRows.length>0){
			   itemrow.subnum=2;
			   var sumamount=0;
			    splitRows.forEach(row=>{
			   	    sumamount+=parseInt(row.amount);
			    })
			   itemrow.reallyamount=sumamount;
			   itemrow.splitRows=splitRows;
		  }else{
			    itemrow.subnum=0;
				
		  }
		
		 
	}
	function showShipRecord(row){
	   
	}
   /* 预计到货报表 */
   function handlarrivalChart(row){
	   var groupid=row.groupid;
	   var marketplaceid="";
	   var amazonAuthId="";
	   var sku="";
	   var msku=row.sku;
	   arrivalchartRef.value.show(groupid,marketplaceid,amazonAuthId,sku,msku)
   }
 
   function loadTableData(params,callback){
	if(!state.expendall&&state.isExpendAll==true){
	      expendAll();
	   }
	   state.expendRows=[];
	   state.closeloading=false;
	   params.plantype="ship";
	   planApi.getPlanList(params).then(res=>{
		   state.tableData.records=res.data.records;
		   state.tableData.total=res.data.total;
		   if(state.tableData.records){
		   			   state.tableData.records.forEach(item=>{
		   				   if(item.notice){
		   					   item.htmlnotice=decodeText(item.notice);
		   				   }
						  
		   				   item.rowstatue={
		   					   isplan:false,
		   					   isexpends:false,
		   					   isEdit:false,
		   					   loading:false,
		   					   showeu:false,
							   subPlanEdit:null,
		   				   };
		   				   if(item.amount&&parseInt(item.amount)){
		   						item.rowstatue.isplan=true;
		   				   }
						   if(item.subList&&item.subList.length>0){
							   item.rowstatus.subPlanEdit=true;
						   }
		   				   if(state.queryParams.expendall){
		   						handleDetail(item,item.expendData);
								state.expendRows.push(item.id);
		   				   }else{
							    item.expendData=null;
						   }
		   			   });
		   			    if(state.queryParams.expendall){
		   			 		 state.isExpendAll=true
		   			    }
		   }
		 
		 
	   }); 
   }
   function expendRow(row){
	  globalTableRef.value.toggleRowExpansion(row)
   }
  function handleAdd(row){
	  row.rowstatue.isEdit=true;
	   if(!state.expendRows.includes(row.id)){
			globalTableRef.value.toggleRowExpansion(row);
	   }else if(row.expendData&&row.expendData.length>0){
		   row.expendData.forEach(item=>{
			   if(item.subList&&item.subList.length>0){
			   		 row[item.key]=item.subList;
			   		 row.rowstatue.subPlanEdit=true;
			   }
		   })
	   }
	  
  }
  async function handlAddSub(row,item,type){
	row.rowstatue.subPlanEdit=true;
	if(!row[item.key]){
		row[item.key]=[];
	}
	if(type=="append"){
		var data={"amount":"",
		          "groupid":item.groupid,
		          "marketplaceid":item.marketplaceid,
				  "sku":item.sku,
				  "country":item.country,
				  "msku":item.msku,
				  "amazonauthid":item.amazonauthid,
				  "aftersalesday":item.aftersalesday};
		if(data.overseaid==null ||data.overseaid==undefined ||data.overseaid=="" ||data.overseaid=="null"){
			data.overseaid="0";
		}
		if(!data.transtype){
				if(state.transtypeOptions&&state.transtypeOptions.length>0){
					data.transtype=state.transtypeOptions[0].id;
				}else{
					data.transtype="";
				}
		  }
		row[item.key].push(data);
		return ;
	}
    await planApi.subsplit(item).then(res=>{
  	    if(res.data.length>0){
  		    res.data.forEach(splititem=>{
  			    if(!splititem.transtype){
  						if(state.transtypeOptions&&state.transtypeOptions.length>0){
  							splititem.transtype=state.transtypeOptions[0].id;
  						}else{
  							splititem.transtype="";
  						}
  			      }
				if(splititem.overseaid==null
				||splititem.overseaid==undefined
				||splititem.overseaid==""
				||splititem.overseaid=="null"){
					splititem.overseaid="0";
				}
			})
  		    row[item.key]=res.data;
	   }else{
		    row[item.key]=[];
			var data={"amount":"",
			          "groupid":item.groupid,
			          "marketplaceid":item.marketplaceid,
					  "sku":item.sku,
					  "country":item.country,
					  "msku":item.msku,
					  "amazonauthid":item.amazonauthid,
					  "aftersalesday":item.aftersalesday};
			if(data.overseaid==null ||data.overseaid==undefined ||data.overseaid=="" ||data.overseaid=="null"){
				data.overseaid="0";
			}
			if(!data.transtype){
					if(state.transtypeOptions&&state.transtypeOptions.length>0){
						data.transtype=state.transtypeOptions[0].id;
					}else{
						data.transtype="";
					}
			  }
			row[item.key].push(data);
	   }
     });
  }
  
  function handleCancel(row){
	  row.rowstatue.isEdit=false;
	  loadDetail(row);
  }
  function handleDelete(row){
	     if(row.rowstatue.isplan==true){
			 planApi.remove({warehouseid:row.warehouseid,msku:row.msku,groupid:row.groupid}).then(res=>{
			 	row.rowstatue.isplan=false;
			 	row.rowstatue.isEdit=false;
			 	ElMessage.success("删除成功");
				loadDetail(row);
				myHeaderRef.value.handleQuerySummary();
			 });
		 }
  }
   function savePlanItem(planData,row){
	   planApi.save(planData).then(res=>{
		   if(res.data>0){
			   row.rowstatue.isplan=true;
			   row.rowstatue.isEdit=false;
			   ElMessage.success("加入计划并保存");
		    }
	   		loadDetail(row);
	   		myHeaderRef.value.handleQuerySummary();
	   	});
   }
 
   function submitForm(row){
	   var planData=[];
	   var tableData=row.expendData;
	   var quantity=0;
	   if(row.quantity){
		   quantity=row.quantity;
	   }
	   if(row["canAssembly"]){
		  quantity=quantity+parseInt(row.canAssembly); 
	   }
	   var error="";
	   if(tableData&&tableData.length>0){
		   var haseu=false;
		   var boxerror=false;
		   tableData.forEach(item=>{
		   		   if(item.amount>0&&item.iseu==true){
		   			   haseu=true;
		   		   }
		   });
		   tableData.forEach(item=>{
			      if(row[item.key]&&row[item.key].length>0){
					  var subqty=0;
						 item.subList=row[item.key];
						 item.subList.forEach(subitem=>{
							 if(item.boxnum>0&&subitem.amount%item.boxnum>0){
													   boxerror=true;
							   }
							 quantity=quantity-subitem.amount;
							 subqty=subqty+parseInt(subitem.amount);
							 subitem.warehouseid=state.queryParams.warehouseid;
						 })
						 if(subqty>0){
							item.subnum=item.subList.length;
							item.amount=subqty;
						    planData.push(item); 
						 }
				  } else if(item.amount>0){
					   if(haseu==true){
						   if(item.marketplaceid=='EU'){
							 item.amount=0;
							 item.splitRows=[];
						   }
					   } 
					   if(row.boxnum>0&&item.amount%row.boxnum>0){
						   boxerror=true;
					   }
					 quantity=quantity-item.amount;
					 item.warehouseid=state.queryParams.warehouseid;
					 planData.push(item); 
		   		}
				   
		   });
		   var type=null;
		   if(quantity<0){
			   error=error+"库存不足，";
			   type="planinventory";
		   }
		   if(boxerror==true){
		   	  error=error+"不符合箱规【"+row.boxnum+"】，";
			  if(type==null){
				  type="boxmessageship";
			  }
		   }
		   if(planData.length==0){
			   ElMessage.warning('没有可以保存的发货计划');
			   return ;
		   }
		   if(error!=""){
			   confirmRef.value.show("warning_"+type,'加入计划', error+'您确定要加入发货计划吗?',()=>{
			   				   savePlanItem(planData,row);
			   })
		   }else{
			   savePlanItem(planData,row);
		   }
	   }else{
		 row.rowstatue.isplan=false;
		 row.rowstatue.isEdit=false;  
	   }
	
   }
   function handleRowClassName({row,rowindex}){ 
	   if(row&&row.rowstatue&&row.rowstatue.isplan){
	   		   return 'selectedtr';
	   } else{
		   return '';
	   }
	  
   }
   function handleConfirm(status,row){
	    row.rowstatue.isEdit=status.isEdit;
		row.rowstatue.isplan=status.isplan;
		if(row.expendData&&row.expendData.length>0){
			row.expendData.forEach(item=>{
				if(item.subList&&item.subList.length>0){
					row.rowstatus.subPlanEdit=true;
					item.key=item.groupid+item.marketplaceid;
				}
			})
		}
   }
   /* 隐藏产品 */
   function hideProduct(row){
	   if(row.rowstatue.isplan){
		   ElMessage.error( '当前产品已加入计划，无法隐藏！');
		   return ;
	   }
	   ElMessageBox.confirm('您确定要隐藏此产品吗?', '隐藏产品',
	   	    {
	   	      confirmButtonText: '确定',
	   	      cancelButtonText: '取消',
	   	      type: 'warning',
	   	    }
	   	  ).then( () => {
	   		  markApi.hide({materialid:row.id}).then(res=>{
	   		  		    row.displayState = true;
	   		  				ElMessage.success('产品已隐藏！');
	   		  				handleQuery(state.queryParams);
	   		  })
	   	  })
	  
   }
   function handleProductShowHide(e,row){
   	   state.editRow = row
   	   const evt = e || window.e || window.event;
   	   productShowHideRef.value = evt.currentTarget
   }
   function showProduct(row){
   	   markApi.show({materialid:row.id}).then(res=>{
   		    row.displayState = true;
   			ElMessage.success('产品显示成功！');
   			handleQuery(state.queryParams);
   	   })
   }
   function productDetails(row){
   			  router.push({
   			  	path:'/material/details',
   			  	query:{
   			  	  title:"产品详情",
   			  	  path:'/material/details',
   				  details:row.id,
				  type:"product"
   			  	}
   			  })
   }
   
   function tableRowClick(row){
	   globalTableRef.value.toggleRowExpansion(row);
   }
   function freezeItem(item){
	   Object.freeze(item.asin);
	   Object.freeze(item.openDate);
	   Object.freeze(item.mincycle);
	   Object.freeze(item.needpurchase);
	   Object.freeze(item.asin);
	   Object.freeze(item.shopid);
	   Object.freeze(item.sumweek);
	   Object.freeze(item.url);
	   Object.freeze(item.groupname);
	   Object.freeze(item.groupid);
	   Object.freeze(item.statuscolor);
	   Object.freeze(item.salesday);
	   Object.freeze(item.sum15);
	   Object.freeze(item.shipday);
	   Object.freeze(item.summonth);
	   Object.freeze(item.country);
	   Object.freeze(item.sumseven);
	   Object.freeze(item.needshipfba);
	   Object.freeze(item.cycle);    
	   Object.freeze(item.marketname);           
	   Object.freeze(item.msku);  
	   Object.freeze(item.marketplaceid);  
	   Object.freeze(item.sku);  
	   Object.freeze(item.warehouseid);  
	   Object.freeze(item.amazonauthid); 
	   Object.freeze(item.afn_reserved_future_supply); 
	   Object.freeze(item.afn_inbound_shipped_quantity); 
	   Object.freeze(item.afn_reserved_quantity); 
	   Object.freeze(item.afn_researching_quantity); 
	   Object.freeze(item.afn_fulfillable_quantity); 
	   Object.freeze(item.afn_unsellable_quantity); 
   }
   function handleDetail(row,expendData){
	   var subrow=[];
	    var quantity=row.quantity?parseInt(row.quantity):0;
	    if(row["canAssembly"]){
	    		  quantity=quantity+parseInt(row.canAssembly); 
	    }
	    row.rowstatue.showeu=false;
	    row.expendEuData=null;
	   var reallyamount=0;
	   var needship=0;
	   if(expendData){
		   expendData.forEach(item=>{
		    item.key=item.groupid+item.marketplaceid+item.sku;
			if(row.rowstatue.isEdit==true){
				   if(item.subList&&item.subList.length>0){
					   row[item.key]=item.subList;
					   row.rowstatue.subPlanEdit=true;
				   }
			 }
		   	if(!parseInt(item.amount)){
		   		item.amount=0;
		   	}
		   	if(item.needship&&parseInt(item.needship)){
		   		needship=needship+parseInt(item.needship);
		   	}
		   	if(item.reallyamount&&parseInt(item.reallyamount)){
		   		item.amount=parseInt(item.reallyamount);
		   		quantity=quantity-parseInt(item.amount);
		   		reallyamount+=parseInt(item.reallyamount);
		   	}else if(quantity-parseInt(item.amount)>0){
		   		item.amount=parseInt(item.amount);
		   		quantity=quantity-parseInt(item.amount);
		   	}else if(parseInt(quantity)>0){
		   		item.amount=quantity;
		   		quantity=0;
		   	}else{
		   		item.amount=0;
		   		quantity=0;
		   	}
		   	item.setstockingcycles=item.cycle.stockingCycle;
		   	item.iseu=false;
		   	item.visible=false;
		   	if(item.marketplaceid=='EU'&&item.subnum>1){
		   		handleShowEUData(item,row);
		   	}
		   		freezeItem(item);
		   	subrow.push(item);
		   });
		   row.amount=reallyamount;
		   row.needship=needship;
		   row.expendData=  subrow;
		  
	   }
   }
   function loadDetail(row){
			   row.rowstatue.loading=true;
			   var param={"groupid":row.groupid,
			              "msku":row.sku,
			              "warehouseid":state.queryParams.warehouseid,
			   						    "plantype":"ship",
			   							"plansimple":state.queryParams.plansimple,
			   							"marketplaceids":state.queryParams.marketplaceids,
			   							"iseu":false,
			   							"amount":0};
			   planApi.getExpandCountryData(param).then(res=>{
			      row.rowstatue.loading=false;
			   			 		if(res.data){
										handleDetail(row,res.data);
			   			 		}
			   })
			  
	    }
   function handleExpandChange(row,expandedRows){
		 expandedRows.forEach(item=>{
			 state.expendRows.push(item.id);
		 });
	    if(expandedRows.length==state.tableData.records.length){
		}
		if(expandedRows.length==0){
			state.closeloading=false;
		}
		if(state.expendRows.includes(row.id)){
			if(row.expendData==null||row.expendData.length==0){
			    loadDetail(row);
		     }
		}
		
   }

   function loadTransTypeAllList(){
   		transportationApi.getTransTypeAll().then((res)=>{
			if(res.data){
			    state.transtypeOptions=res.data;
			}
   		});
   }
   onMounted(()=>{
	   loadTransTypeAllList();
   })
   /* 编辑公告 */
   function editRemarks(row){
	   remarksRef.value.show(row.id);
   }
  function handlePSaveCycle(row){
	  expendTableRef.value.handleSaveCycle(row)
  }
   function handleShowStockCycle(e,row){
   	state.expenditem = row
   	state.expenditem.stockVisible = true
   	const evt = e || window.e || window.event;
   	stockCycleRef.value = evt.currentTarget
   }
   function handleShowInventory(e,row){
   	state.expenditem = row
   	const evt = e || window.e || window.event;
   	InventoryDetalisRef.value = evt.currentTarget
   }
   
   function handleShowInvPlan(e,row){
   	state.expenditem = row;
	
   	const evt = e || window.e || window.event;
   	InvPlanDetalisRef.value = evt.currentTarget
   }
   function getZeroValue(value){
   	if(value){
   		return value;
   	}else{
   		return 0;
   	}
   }
	function getInventory(e,row){
		const evt = e || window.e || window.event;
		InventoryRef.value = evt.currentTarget
		state.editRow = row
	}
</script>

<style>
	.hide{
		display:none;
	}
	 .ship-expand-table .editfield.el-button.is-link {
	     color: var(--el-table-header-text-color) !important;
	 }
	.ship-expand-table .editfield.el-button.is-link:focus, .editfield.el-button.is-link:hover {
	     color: var(--el-color-blue-light-2)  !important;
	 }
	.ship-expand-table .el-table__expanded-cell{
		padding:0px;
	}
	 .ship-expand-table .el-table__expanded-cell .el-table__cell{
		background-color:var(--el-fill-color-lighter);
	}
	.ship-expand-table .el-table__expanded-cell .table-edit-flex{
		text-align:center;
		display: block;
	}
	 .ship-expand-table .el-table__expanded-cell .el-table__inner-wrapper::before{background-color:var(--el-fill-color-lighter)}
	.m-b-8{
		margin-bottom:8px;
	}
	.ship-expand-table .expend-bor{
		border-top:2px solid var(--el-color-info-light-9);
		border-bottom:2px solid var(--el-color-info-light-9);
	}
	.ship-expand-table .r_active{
		background-color: var(--el-dropdown-menuItem-hover-fill);
	    color: var(--el-dropdown-menuItem-hover-color);
		}
	.ship-expand-table{
		padding-left:16px;
		padding-right:16px;
	}
	.flex-center-bew{
		display: flex;
		align-items: center;
	}
	.flex-center-bew .el-link{
		margin-right:8px;
	}
	.border-line li{
		line-height:40px;
     }
	 .opt-td-div span{
		 padding-left:4px;
		 padding-right:4px;
		 margin-right:4px;
		 margin-left:4px;
	 }
	 .selectedtr td:nth-child(n+1){
		  background-color: #fff3ec;
	 }
	 .selectedtr td:first-child{
		  background-image: url('@/assets/image/pages/shipplan/ship_plan_checked.png');
	      background-color: #fff3ec;
		  background-repeat: no-repeat;
		  background-size: 42px 42px;
	 }
	 .dark .selectedtr td:nth-child(n+1){
			  background-color: #302d2c;
	 }
	 .dark .selectedtr td:first-child{
			  background-image: url('@/assets/image/pages/shipplan/ship_plan_checked.png');
		  background-color: #302d2c;
			  background-repeat: no-repeat;
			  background-size: 42px 42px;
	 }
	 .waring-bg{background-color: var(--el-color-primary-light-9)!important;}
</style>
<style scoped="scoped">
	.font-sku{
		font-weight: 700!important;
		font-size: 16px;
	}
	.font-name{
		color:#999;
	}
</style>