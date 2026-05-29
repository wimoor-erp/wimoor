<template >
     <div class="el-white-bg">
	 <Header ref="myHeaderRef" @change = "handleQuery" @confirm="goToPlanConfirm" @expend="handlerExpend"/>
	 	 
	 <div class=" purchase-plan-amz  m-b-p-16">
		 <GlobalTable ref="globalTableRef"
		 :tableData="tableData"  
		 :size="50"
		 @loadTable="loadTableData" 
		 :defaultSort="{ prop: 'marketneedpurchase', order: 'descending' }"  
		 rowKey="id"
		:defaultExpandAll="expendall"
		 @row-click="tableRowClick"
		 :rowClassName="handleRowClassName"
		 @expandChange="handleExpandChange"
		 >
		<template #field>
		<el-table-column type="expand" width="45">
			 <template #header>
				  <el-button v-if="isExpendAll" @click="expendAll()" key="true"   type="info"  link
				       > <el-icon><ArrowDownBold /></el-icon></el-button>
				  <el-button v-else @click="expendAll()"  key="false" type="info"  link
				     > <el-icon><ArrowRightBold /></el-icon></el-button>
			 </template>
			 <template #default="scope">
				<!-- 展开的table -->
				 <Expandtable :tableData="scope.row.expendData"  
				             :row="scope.row"  
							 ref = "expandTableRef"
							 :transtypeOptions="transtypeOptions"
							 @reload="loadDetail"
							 @show-sales-adjust="handleShowSalesAdjust"
							 @show-product-detail="handleShowProductDetail"
							 @show-ship-record="handleShowShipRecord"
							 @show-eudata="handleShowEUData"
							 @show-split-row="showSplitRow"
							 @show-inventory-details = "handleShowInventory"
							 @show-stock-cycle = "handleShowStockCycle"
							 :status="scope.row.rowstatue"/>  
			 </template>
		</el-table-column>
		<el-table-column prop="img" label="图片" width="65" >
		   <template #default="scope">
		    <img v-if="scope.row.image" :src="scope.row.image" class="product-img"/>
			<img v-else :src="$require('empty/noimage40.png')" class="product-img"/>
		  </template>
		</el-table-column>
		<el-table-column prop="sku" label="名称/SKU" :sort-orders="sortOrders" sortable='custom' min-width="200" >
		   <template #default="scope">
			    <div @mouseenter.stop="handleEmtpy">
			   <el-space  wrap>
				   <div  @click.stop="productDetails(scope.row)"  class="font-sku  pointer" >
				         {{scope.row.sku}}
						 <copy  @click.stop="CopyText(scope.row.sku)" title='复制SKU' theme="outline" style="padding-left:3px;" size="14" fill="#666" :strokeWidth="3"/>
				   </div>
			 
			   				<span v-if="scope.row.issfg=='1'"   @click.stop="handleEmtpy">
			   					<el-tag
			   					 title="组合产品"
			   					 @click.stop="e=>handleAssemblyShow(e,scope.row)"
			   					 @mouseenter="e=>handleAssemblyShow(e,scope.row)"
			   					 type="warning" class="pointer" v-if="scope.row.issfg=='1'"
			   					 size="small" 
			   					>组合</el-tag>
			   				</span>
			   				<el-tag v-if="scope.row.tagNameList" effect="plain" :type="item.color"  v-for="item in scope.row.tagNameList" size="small" >
			   					{{item.name}}
			   				</el-tag>
			   			 
			   </el-space>
			   </div>
			  <el-tooltip  placement="top" :content="scope.row.name" :show-after="200">
		      <div class='font-name text-omit-1 m-b-8'>{{scope.row.name}}</div>
			  </el-tooltip>
			  <div @mouseenter.stop="handleEmtpy">
			  <el-row >
			  <el-space  class="font-extraSmall"  :size="4" :spacer="spacer">
				  <div @click.stop="handleEmtpy" >
					       <span
						   @click.stop="e=>handleWisePriceShow(e,scope.row)"
						    @mouseenter="e=>handleWisePriceShow(e,scope.row)"
							v-if="scope.row.price" class="pointer " >￥{{scope.row.price}}</span> 
					  		 <span v-else >-</span> 
				  	</div>
			   <span 
			   v-if="scope.row.warehousename"
			   @click.stop="e=>handleShowWareHouse(e,scope.row)"
			   class="pointer">
				   {{scope.row.warehousename}}
			   </span>
			   <span v-else
			   @click.stop="e=>handleShowWareHouse(e,scope.row)"
			   class="pointer text-red">请添加入库仓库</span>
			  </el-space>
			  </el-row>
			  </div>
		  </template>
		</el-table-column>
		<el-table-column label="报表" width="80">
			<template #default="scope">
				<el-space  :size="3" direction="vertical">
				<el-button  text @click.stop="handlesaleChart(scope.row)">
				 <el-tooltip content="销量报表" placement="right" :hide-after="0" :show-after="200">
				 <chart-histogram class="ic-cen" theme="outline" size="16" fill="#ff6700" :strokeWidth="4"/>
				 </el-tooltip>
				 </el-button>   
				 </el-space>
				 <el-button text @click.stop="handlarrivalChart(scope.row)">
					 <el-tooltip content="预计到货报表" placement="right" :hide-after="0" :show-after="200">
				<chart-line class="ic-cen" theme="filled" size="15" fill="#529b2e"/>
				 </el-tooltip>
				 </el-button>
			</template>
		</el-table-column>
		<el-table-column label="公告"  min-width="220" >
			<template #default="scope">
					<span class="table-edit-flex" >
					    <span class="font-small " v-html="scope.row.htmlnotice"></span>
						<el-icon @click.stop="editRemarks(scope.row)"><Edit/></el-icon>
					</span>
			</template>
		</el-table-column>
		<el-table-column  label="采购记录" width="100">
			<template  #default="scope">
				<div  @click.stop="handleEmtpy">
						  <div
						   @click.stop="e=>getPurHistory(e,scope.row,'click')"
						   @mouseenter="e=>getPurHistory(e,scope.row,'hover')"
						   class="font-small pointer" v-if="scope.row.last!=''" style="width:67px;" v-html="scope.row.last"></div>
						   <span v-else>-</span>
				  </div>
			  </template>
		</el-table-column>	
		
		<el-table-column label="可用库存" prop="quantity"  :sort-orders="sortOrders" sortable='custom'  width="110">
			<template #default="scope">
					<div @click.stop="handleEmtpy">
			       <div>
					   <div 
					   @click.stop="e=>getInventory(e,scope.row)"
					   @mouseenter="e=>getInventory(e,scope.row)"
					   class=" pointer" >
							<span class="font-bold" v-if="scope.row.quantity">
							<span v-if="scope.row.issfg=='2'" class='text-orange'>{{scope.row.quantity}}</span>
							<span v-else>{{scope.row.quantity}}</span>
							</span>
					        <span  class="font-bold" v-else >0</span>
							<span v-if="scope.row.wshqty" title="90天以上库龄">&nbsp;&nbsp;&nbsp;<span class="font-extraSmall">滞销:</span>
							<span class="text-red">{{scope.row.wshqty}}</span>
							</span>
					 </div>
					  <div class="font-extraSmall" >供货周期 {{scope.row.delivery_cycle}} 天</div>
				  </div>
				  </div>
				</template>
		</el-table-column>
       
	<el-table-column label="建议采购"
		prop="marketneedpurchase"  
		:sort-orders="sortOrders" sortable='custom' 
		 width="110">
	 	<template #default="scope">
			<div class="font-bold font-gray">
				 <div  v-if="scope.row?.rowstatue?.isplan==false&&scope.row.prereallyamount&&parseInt(scope.row.marketneedpurchase)!=parseInt(scope.row.prereallyamount)"
				 > {{scope.row.prereallyamount}}</div>
				<div v-else >{{scope.row.needpurchase}}</div>
			 </div>
			<div class="font-extraSmall">总需求量:{{scope.row.marketneedpurchase}}</div>
	 	</template>
	 </el-table-column>
 
		<el-table-column label="实际采购量"   :sort-orders="sortOrders" sortable='custom' prop="amount" width="110">
			<template #default="scope">
				<div v-if="scope.row?.rowstatue?.isPEdit" @click.stop="handleEmtpy">
					<el-input v-model.number="scope.row.reallyamount"
					 @input="scope.row.reallyamount=CheckInputInt(scope.row.reallyamount)"
					  ></el-input>
				</div>
				<div class="font-bold" v-else-if="scope.row?.rowstatue?.isplan">{{scope.row.amount}}
				</div>
				<div v-else><span  class="font-bold">0 </span> </div>
			</template>
		</el-table-column>

		
	 
		<el-table-column label="操作" width="150">
			<template #default="scope">
				<div class="flex-center-bew" @click.stop="handleEmtpy" >
				<el-link @click.stop="handleEmtpy"  :underline="false" type="primary" class="font-small">
					<div v-if="scope.row.rowstatue.isPEdit"  class="opt-td-div">
						<span  @click.stop="submitForm(scope.row)">保存</span>
						<span  @click.stop="handleCancel(scope.row)" style="margin-left:20">取消</span> 
					</div>
					<div v-else class="opt-td-div">
						<span   @click.stop="handleAdd(scope.row)" >编辑</span>
						<span v-if="scope.row.rowstatue.isplan" 
						       @click.stop="handleDelete(scope.row)" > 移除</span> 
					</div>
				</el-link>
				  <span 
				   @click.stop="e=>handleProductShowHide(e,scope.row)"
				    @mouseenter="e=>handleProductShowHide(e,scope.row)"
				   >
				    <more-one class="ic-cen" theme="outline" size="20" fill="#333" :strokeWidth="3"/>
				  </span>
				</div>
			</template>
		</el-table-column>
     </template>
 </GlobalTable>
	</div>
	<ShipRecordDialog ref="shipRecordDialogRef"></ShipRecordDialog>
	<SalechartDialog ref="salechartRef"/>
	<ArrivalDialog ref="arrivalchartRef"/>
	<RemarksDialog ref="remarksRef" @confirm="remarkConfirm"/>
    <SaleAdjustDialog ref="saleAdjustDialogRef" @confirm="loadDetail"></SaleAdjustDialog>
	<GoodsDeatils ref="goodsDeatilsRef"  />
	</div>
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
	<!-- 修改仓库 -->
	<el-popover
	:visible="editRow.warehouseVisable"
	placement="top"
	title="选择入库仓"
	:width="400"
	:virtual-ref="warehouseSelectRef"
	virtual-triggering
	trigger="click"
	>
	<el-icon class="pointer" style="float:right;margin-top:-30px;" @click="editRow.warehouseVisable=false" ><Close /></el-icon>
	 <el-radio-group class="m-b-p-16" v-model="editRow.warehouseid" @change="handleChangeWarehouse(editRow),editRow.warehouseVisable=false">
	    <el-radio v-for="item in plan.warehouseList" :key="item.warehouseid" :label="item.warehouseid">{{item.name}}</el-radio>
	</el-radio-group>
	<el-button style="float:right" size="small" @click="editRow.warehouseVisable=false"  > 关闭</el-button>
	 
	</el-popover>
	<!-- 组装产品 -->
	<AssemblyDialog :assbRef="assbRef" @change="getAssembliyList(editRow)" :loading="editRow.assloading"
	 :assemblyList="editRow.assemblyList"/>
	<!-- 阶梯采购价 -->
	<el-popover
	    placement="top"
	    title="阶梯采购价"
		:virtual-ref="WisePriceRef"
		 virtual-triggering
	    :width="200"
		trigger="click"
		  @show="getWisePriceList(editRow)"
	  >
		<el-table :data="pricelist" v-loading="loading">
			<el-table-column label="起订量" prop="amount"></el-table-column>
			<el-table-column label="单价" prop="price"></el-table-column>
		</el-table>
	  </el-popover>
	<!-- 补货记录 -->
	<el-popover
	    placement="top"
	    title="补货记录"
		:virtual-ref="purHistoryRef"
		virtual-triggering
		:visible="lastRecordVisible"
	    :width="520"
		trigger="click"
		@show="getRecordList(editRow)"
	  >
		<el-table :data="recordlist" v-loading="lastRecordLoading">
			<el-table-column label="订单" prop="createdate" width="140">
								<template #default="scope">
									<el-link class="pointer" type="primary" @click="handleGotoPage(editRow.issfg,scope.row.number,scope.row.id)">{{scope.row.number}}</el-link>
								    <div class="font-extraSmall">{{dateTimesFormat(scope.row.createdate)}}</div>
								</template>
							</el-table-column>
					   <el-table-column label="数量" prop="amount" width="100">
					   	<template #default="scope">
					   	   {{scope.row.amount}}
						   <div  v-if="editRow.issfg!='1'" class="font-extraSmall">已入库:{{scope.row.totalin}}</div>
					   	   <div v-else-if="scope.row.hashandle" class="font-extraSmall">待处理:{{scope.row.hashandle}}</div>
					   	</template>
					   </el-table-column>
							<el-table-column v-if="editRow.issfg=='1'"  label="状态" prop="auditstatusName">
							</el-table-column>
							<el-table-column v-else label="状态"  prop="auditstatusname">
							</el-table-column>
							<el-table-column label="已付款" v-if="editRow.issfg!='1'"  prop="totalpay"></el-table-column>
							<el-table-column label="类型" >
								 <template #default="scope">
								  <div v-if="editRow.issfg=='1'" >组装单</div>  
								  <div v-else>采购单</div>  
								 </template>
							</el-table-column>
							
		</el-table>
	  </el-popover>
	  <!-- 库存 -->
	  <el-popover 
	  trigger="click"
	  :virtual-ref="InventoryRef"
	  virtual-triggering
	   @show="handleQueryInventoryQty(editRow)" placement="top" width="240"  >
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
	     <li class="flex-center-between" v-if="editRow.issfg=='2'"><span>待组装消耗</span>
		    <el-tag type="info" v-if="editRow.invneed">{{editRow.invneed}}</el-tag>
			<el-tag type="info" v-else>0</el-tag>
		 </li>
	   </ul>
	  </el-popover>
	  
	 <!-- 展开后的table -->
	  <el-popover 
		:virtual-ref="InventoryDetalisRef"
		virtual-triggering
		trigger="click"  placement="top" width="240px"  >
	   <ul class="border-line">
	  	<li class="flex-center-between "><span class="pointer" @click="refreshInventory(expenditem)" title="点击可更新为实时库存">可用库存<el-icon v-loading="expenditem.loading"  class="font-extraSmall"><RefreshRight /></el-icon></span><el-tag type="success">{{getZeroValue(expenditem.afn_fulfillable_quantity)}}</el-tag></li>
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
	 <!-- 备货周期 -->
	 <el-popover
	   @hide="expenditem.visible=false"
	    placement="top-start"
	    title="备货周期"
	    :width="300"
		:visible="expenditem.visible"
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
	 		<el-button  @click.stop="expenditem.visible = false">关闭</el-button>
	        <el-button type="primary" @click.stop="handlePSaveCycle(expenditem)">确定</el-button>
	   </div>
	  </el-popover>
	  <Confirm ref="confirmRef"></Confirm>
</template>
<script>
    export default{ name:"采购规划" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,toRefs,h} from 'vue'
	import Expandtable from "./components/expand_table.vue";
	import SalechartDialog from "@/views/amazon/listing/common/salechart.vue"
	import ArrivalDialog from"@/views/amazon/listing/common/arrival_dialog.vue";
	import RemarksDialog from "@/views/erp/ship/ship_plan/components/remarks_dialog.vue"
	import SaleAdjustDialog from "@/views/amazon/sales/forecast/components/sale_adjust_dialog.vue";
	import AssemblyDialog from "@/views/erp/components/assembly_dialog.vue";
	import ShipRecordDialog from "@/views/erp/ship/ship_plan/components/ship_record.vue"
	import GoodsDeatils from "@/views/amazon/listing/common/goods_deatils.vue";
	import Confirm from "@/components/dialog/confirm.vue";
    import Header from "./components/header.vue";
	import CopyText from"@/utils/copy_text.js";
	// API依赖
	import planApi from '@/api/erp/ship/planApi.js';
	import inventoryApi from '@/api/erp/inventory/inventoryApi.js';
	import {Edit,ArrowRightBold,ArrowDownBold,Close,RefreshRight} from '@element-plus/icons-vue';
	import { ElMessage ,ElMessageBox,ElDivider} from 'element-plus'
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js'
	import markApi from '@/api/erp/material/markApi.js';
	import {Help,MoreOne,ChartHistogram,ChartLine,Copy} from '@icon-park/vue-next';
	import {sublit} from "@/api/erp/assembly/assemblyApi.js";
	import {changeWarehouse,saveItem,deleteItem,getLast3} from '@/api/erp/purchase/plan/planApi.js';
	import { useRouter } from 'vue-router'
	import {CheckInputInt,dateFormat,dateTimesFormat,decodeText} from "@/utils/index.js";
	import materialApi from '@/api/erp/material/materialApi.js';
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import {assemblyFormNeedInv } from '@/api/erp/assembly/assemblyApi.js'
	const spacer = h(ElDivider, { direction: 'vertical' })
   const router = useRouter();
   const globalTableRef=ref();
   const expandTableRef = ref();
   const salechartRef =ref()
   const arrivalchartRef =ref()
   const saleAdjustDialogRef=ref();
   const goodsDeatilsRef=ref();
   const shipRecordDialogRef=ref();
   const splitPlanDialogRef=ref();
   const myHeaderRef =ref();
   const remarksRef =ref()
   const extendTableRefs=[];
   const assbRef =ref()
   const WisePriceRef = ref()
   const purHistoryRef = ref()
   const InventoryRef = ref()
   const InventoryDetalisRef =ref();
   const confirmRef=ref();
   const stockCycleRef =ref()
   const warehouseSelectRef = ref()
   const productShowHideRef = ref()
   const state = reactive({
	   loading:true,
     // 表格树数据
     tableData: {records:[],total:0}  ,
	 lastRecordVisible:false,
     // 弹窗属性
     dialog: { visible: false }  ,
     // 查询参数
     queryParams: {plansimple:false} ,
	 editRow:{warehouseVisable:false,},
	 expendRows:[],
	 expenditem:{visible:false},
	 isExpendAll:false,
	 expendall:false,
	 closeloading:false,
	 lastRecordLoading:false,
	 transtypeOptions:[],
	 sortOrders:[ 'descending','ascending', null],
     // 表单数据
     formData: { } ,
	 overseaOptions:[],
	 pricelist:[],
	 recordlist:[],
	 plan:{},
     // 表单参数校验
   });
   const {
     tableData,
     queryParams,
	 transtypeOptions,
	 closeloading,
	 lastRecordVisible,
	 loading,
	 plan,
	 lastRecordLoading,
	 recordlist,
	 sortOrders,
	 isExpendAll,
	 expendall,
	 pricelist,
     formData,
	 editRow,
	 expenditem,
   } = toRefs(state);
   function handleEmtpy(){
	   
   }
   function handlePSaveCycle(row){
	   expandTableRef.value.handleSaveCycle(row)
   }
   function addInventoryQty(value){
   	if(value){
   		state.expenditem.quantity=state.expenditem.quantity+parseInt(value);
   	}
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
					 state.expenditem.reserved_qty=res.data.afnReservedQuantity;
   					 state.expenditem.afn_inbound_working_quantity=res.data.afnInboundWorkingQuantity;
   					 state.expenditem.afn_inbound_shipped_quantity=res.data.afnInboundShippedQuantity;
   					 state.expenditem.afn_inbound_receiving_quantity=res.data.afnInboundReceivingQuantity;
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
   function statusFormatter(value) {
   	if (value == 0) {
   		return "未提交";
   	}
   	if (value == 1) {
   		return "待组装";
   	}
   	if (value == 2) {
   		return "组装中";
   	}
   	if (value == 3) {
   		return "已完成";
   	}
   	if (value == 4) {
   		return "已终止";
   	}
   	if (value == 5) {
   		return "已撤销";
   	}
   	return "";
   }
   function handleGotoPage(issfg,number,id){
	   if(issfg=='1'){
		   router.push({
		   	path:'/e/p/p/d',
		   	query:{
		   	  title:"加工单详情",
		   	  path:'/e/p/p/d',
		   	  id:id }
		   })
	   }else{
		   router.push({
		   	path:"/erp/purchase/orders",
		   	query:{
		   		title:'采购单',
		   		path:"/erp/purchase/orders",
				number:number,
				refresh:true,
		   	},
		   })
	   }
	   
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
			sublit({materialid:row.id,warehouseid:""}).then(res=>{
				row.assemblyList=res.data;
				row.assloading=false;
			});
		}
	}
	function getWisePriceList(row){
		state.loading= true
		materialApi.getWisePriceList({"mid":row.id}).then((res)=>{
			state.pricelist=res.data;
			state.loading= false
		});
	}
	function getRecordList(row){
		state.lastRecordLoading=true;
		getLast3({"id":row.id}).then((res)=>{
			state.recordlist=res.data.list;
			state.lastRecordLoading=false;
		});
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
	function handleChangeWarehouse(row){
		changeWarehouse({"planid": state.plan.id,"materialid":row.id,"warehouseid":row.warehouseid}).then(res=>{
			ElMessage.success("修改成功");
			row.warehousename=res.data.name;
		}).catch(e=>{
			row.warehouseid=row.oldwarehouseid;
		});
	}
	
	function handleQuery(params){
		state.queryParams=params;
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
				row.hasInvData=true;
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
	   salechartRef.value.show("",marketplaceid,amazonAuthId,sku,msku);
   }
   function handleAssemblyShow(e,row){
   		 const evt = e || window.e || window.event;
   		 assbRef.value = evt.currentTarget
   		 state.editRow = row
		 state.editRow.oldwarehouseid=state.editRow.warehouseid;
   	}
	function getPurHistory(e,row,type){
		const evt = e || window.e || window.event;
		purHistoryRef.value = evt.currentTarget
		state.editRow = row;
		if(type=="hover"){
			state.lastRecordVisible=false;
		}
		if(type=="click"){
			state.lastRecordVisible=!state.lastRecordVisible;
		}
		
	}
	function getInventory(e,row){
		const evt = e || window.e || window.event;
		InventoryRef.value = evt.currentTarget;
		state.editRow = row;
		var param={warehouseid:row.warehouseid,materialid:row.id};
		param.warehouseid="";
		 state.queryParams.plan.warehouseList.forEach(item=>{
			 param.warehouseid=param.warehouseid+item.warehouseid+",";
		 })
		assemblyFormNeedInv(param).then(res=>{
			state.editRow.invneed=res.data;
		});
	}
	function handleWisePriceShow(e,row){
		const evt = e || window.e || window.event;
		WisePriceRef.value = evt.currentTarget
		 state.editRow = row
	}
	function handleShipHistory(row){
		 
	}
    function handleShowEUData(itemrow,parentrow){
		var row =parentrow;
		var subrow=[];
		 if(!row["expendEuData"]||(row["expendEuSKU"]!=itemrow.sku)){
			var param={"groupid":itemrow.groupid,
			           "sku":itemrow.sku,
					   "warehouseid":"",
					   "plantype":"purchase",
					   "marketplaceids":state.queryParams.marketplaceids,
					   "plansimple":state.queryParams.plansimple,
					   "iseu":true,
					   "amount":0
					   };
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
		splitPlanDialogRef.value.show(itemrow,parentrow,state.transtypeOptions,state.overseaOptions);
	}
	function handleShowInventory(e,row){
		state.expenditem = row
		const evt = e || window.e || window.event;
		InventoryDetalisRef.value = evt.currentTarget
	}
	function handleShowStockCycle(e,row){
		state.expenditem = row
		state.expenditem.visible = true
		const evt = e || window.e || window.event;
		stockCycleRef.value = evt.currentTarget
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
		   row.expendData=  Object.freeze(subrow);
	   }
 }
	function showShipRecord(row){
	   
	}
   /* 预计到货报表 */
   function handlarrivalChart(row){
	   var groupid="";
	   var marketplaceid="";
	   var amazonAuthId="";
	   var sku="";
	   var msku=row.sku;
	   arrivalchartRef.value.show(groupid,marketplaceid,amazonAuthId,sku,msku)
   }
 var rownum=0;
   function loadTableData(params,callback){
	if(!state.expendall&&state.isExpendAll==true){
	      expendAll();
	   }
	   params.plantype="purchase";
	   state.plan=params.plan;
	   planApi.getPlanList(params).then(res=>{
		   if(res.data){
			   if(res.data.records){
			   			   rownum=0;
			   			   res.data.records.forEach(async item=>{
			   				   item.rowstatue={
			   					   isplan:false,
			   					   isexpends:false,
			   					   isPEdit:false,
			   					   loading:false,
			   					   showeu:false,
			   				   };
							   if(item.notice){
							       item.htmlnotice=decodeText(item.notice);
							   }
			   					if(!item.amount||parseInt(item.amount)<=0){
			   						       item.reallyamount=parseInt(item.needpurchase)-parseInt(item.quantity);
										   if(item.reallyamount<0){
											   item.reallyamount=0;
										   }
			   						   }
			   				  if(state.queryParams.expendall){
									handleDetail(item,item.expendData);
									state.expendRows.push(item.id);
			   				  }else{
									item.expendData=null;
			   				  }
			   				   if(item.amount&&parseInt(item.amount)>0){
			   						item.rowstatue.isplan=true;
			   				   }
							   item.prereallyamount=item.reallyamount;
			   			   });
			   }
			   state.tableData.records=res.data.records;
			   state.tableData.total=res.data.total;
			   if(state.queryParams.expendall){
			   	 state.isExpendAll=true
			   }
		   }else{
			   state.tableData.records=[];
			   state.tableData.total=0;
		   }
		   
	   }); 
   }
   function expendRow(row){
	  globalTableRef.value.toggleRowExpansion(row)
   }
  function handleAdd(row){
	  row.rowstatue.isPEdit=true;
	   if(!state.expendRows.includes(row.id)){
			globalTableRef.value.toggleRowExpansion(row);
	   }
  }
  function handleCancel(row){
	  row.rowstatue.isPEdit=false;
	  loadDetail(row);
  }
  function handleDelete(row){
	     if(row.rowstatue.isplan==true){
			  deleteItem({"planid":state.plan.id,"materialid":row.id,"groupid":""}).then(res=>{
			 	row.rowstatue.isplan=false;
			 	row.rowstatue.isPEdit=false;
				row.amount=0;
			 	ElMessage.success("删除成功");
				loadDetail(row);
				myHeaderRef.value.handleQuerySummary();
			 });
		 }
  }
 function savePlanItem(plan,row){
	       if(parseInt(row.reallyamount)<=0){
			   ElMessage.error( "计划采购量不能为0 ");
			   return ;
		   }
	       saveItem({"planid":plan.id,
	                 "materialid":row.id,
					 "warehouseid":row.warehouseid,
					 "groupid":"",
					 "amount":row.reallyamount}).then(res=>{
					row.rowstatue.isplan=true;
					row.rowstatue.isPEdit=false;
					row.amount=row.reallyamount;
					ElMessage.success("加入计划并保存");
					loadDetail(row);
					myHeaderRef.value.handleQuerySummary();
	   	});
   }
 
   function submitForm(row){
	   var error="";
	   if(row.reallyamount){
		   if(row.boxnum&&row.reallyamount%row.boxnum>0){
		   	  error=error+"不符合箱规【"+row.boxnum+"】，";
			  
		   }
		   if(!row.warehouseid){
			   ElMessage.error('入库仓库不能为空，请在左侧SKU下方添加入库仓库');
			   return ;
		   }
		   if(!state.plan){
			   ElMessage.warning('没有可以保存的发货计划');
			   return ;
		   }
		   if(error!=""){
			   confirmRef.value.show("warning_boxmessage",'加入计划', error+'您确定要加入发货计划吗?',()=>{
				   savePlanItem(state.plan,row);
			   })
		   }else{
			   savePlanItem(state.plan,row);
		   }
	   }else{
		 row.rowstatue.isplan=false;
		 row.rowstatue.isPEdit=false;  
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
	   if(row.rowstatue){
		   row.rowstatue.isPEdit=status.isPEdit;
		   row.rowstatue.isplan=status.isplan;
	   }else{
		   row.rowstatue={};
		   row.rowstatue.isPEdit=status.isPEdit;
		   row.rowstatue.isplan=status.isplan;
	   }
	    
   }
/* 隐藏产品 */
   function hideProduct(row){
	   if(row.rowstatue&&row.rowstatue.isplan){
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
   function showProduct(row){
   	   markApi.show({materialid:row.id}).then(res=>{
   		    row.displayState = true;
   			ElMessage.success('产品显示成功！');
   			handleQuery(state.queryParams);
   	   })
   }
   function tableRowClick(row){
	   if(window.getSelection().toString() === ''){
		   globalTableRef.value.toggleRowExpansion(row);
	   }
   }
   function goToPlanConfirm(queryPrarams,summary){
	   var params=JSON.parse(JSON.stringify(state.queryParams));
	       params.plantype="purchase";
		   params.pagesize=1000000;
		   params.currentpage=1;
		   var itemlist=[];
		   if(state.queryParams.selected&&summary.skunum!=state.tableData.total){
			   ElMessageBox.confirm(
			   	     '您确定部分提交采购计划吗?（查看已选搜索会自动触发部分采购，将只会提交这一次查看的计划内容，其他内容保留）',
			   	    '提交', 
			   	    {
			   	      confirmButtonText: '确定',
			   	      cancelButtonText: '取消',
			   	      type: 'warning',
			   	    }
			   	  ).then( () => { 
					  planApi.getPlanList(params).then(res=>{
						  if(res.data&&res.data.records&&res.data.records.length>0){
							  res.data.records.forEach(item=>{
							  	itemlist.push(item.id);
							  });
							  router.push({
							  	path:"/e/p/p/s",
							  	query:{
							  		title:'提交采购计划',
							  		path:"/e/p/p/s",
							  		planid:state.queryParams.planid,
									list:itemlist
							  	},
							  })
						  }else{
							  ElMessage.error('未找到对应记录！');
						  }
					  		
					  	})
				  })
		   }else{
			   router.push({
			   	path:"/e/p/p/s",
			   	query:{
			   		title:'提交采购计划',
			   		path:"/e/p/p/s",
			   		planid:state.queryParams.planid,
					list:itemlist
			   	},
			   })
		   }
	    
   	
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
   function loadDetail(row){
	            if(!row.rowstatue){
					row.rowstatue={};
				}
			 	row.rowstatue.loading=true;
				var subrow=[];
				 row.rowstatue.showeu=false;
				 row.expendEuData=null;
			 	 var param={"groupid":"",
				            "warehouseid":"",
							"msku":row.sku,
							"plantype":"purchase",
							"marketplaceids":state.queryParams.marketplaceids,
							"plansimple":state.queryParams.plansimple,
							"iseu":false,
							"amount":row.amount};
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
   function getZeroValue(value){
   	if(value){
   		return value;
   	}else{
   		return 0;
   	}
   }
   
   function handleShowWareHouse(e,row){
	   state.editRow = row
	   state.editRow.warehouseVisable = true
	   const evt = e || window.e || window.event;
	   warehouseSelectRef.value = evt.currentTarget
   }
   function handleProductShowHide(e,row){
	   state.editRow = row
	   const evt = e || window.e || window.event;
	   productShowHideRef.value = evt.currentTarget
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
	.purchase-plan-amz  .el-table__expanded-cell{
		padding:0px;
	}
	.purchase-plan-amz  .el-table__expanded-cell .el-table__cell{
		background-color:var(--el-fill-color-lighter);
	}
	.purchase-plan-amz .el-table__expanded-cell .table-edit-flex{
		text-align:center;
		display: block;
	}
	.purchase-plan-amz  .el-table__expanded-cell .el-table__inner-wrapper::before{background-color:var(--el-fill-color-lighter)}
	.purchase-plan-amz .m-b-8{
		margin-bottom:8px;
	}
	.purchase-plan-amz .font-12{
		font-size:12px;
	}
	.r_active{
		background-color: var(--el-dropdown-menuItem-hover-fill);
	    color: var(--el-dropdown-menuItem-hover-color);
		}
	.purchase-plan-amz{
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
		  background-image: url('@/assets/image/pages/puchaseplan/shipment_plan_checked.png');
	      background-color: #fff3ec;
		  background-repeat: no-repeat;
		  background-size: 42px 42px;
	 }
	 .dark .selectedtr td:nth-child(n+1){
	 		  background-color: #302d2c;
	 }
	 .dark .selectedtr td:first-child{
	 		  background-image: url('@/assets/image/pages/puchaseplan/shipment_plan_checked.png');
	      background-color: #302d2c;
	 		  background-repeat: no-repeat;
	 		  background-size: 42px 42px;
	 }
     .local-price{
		     font-size: 14px;
		     font-weight: 600;
		     color: #f5a20c;
	 }
	 .waring-bg{background-color: var(--el-color-primary-light-9)!important;}
	 .m-b-p-16{
		 padding-bottom:16px;
	 }
</style>
<style scoped="scoped">
	.font-sku{
		font-weight: 700!important;
		font-size: 16px;
	}
	.font-name{
		color: #666;
	}
	.font-gray{
		color: #999;
	}
</style>