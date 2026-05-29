<template>
	<div class="main-sty">
		<div class="con-header" >
		  <el-row>
		    <el-space >
				<Datepicker ref="datepickersRef" :days="1"  @changedate="changedate" />
				 <Warehouse @changeware="getWarehouse"   />
		   <el-input  v-model="searchKeywords" clearable @input="handleQuery" placeholder="请输入" class="input-with-select" >
		      <template #prepend> 
		        <el-select v-model="selectlabel"  placeholder="SKU" style="width: 110px">
		          <el-option label="SKU" value="sku"></el-option>
		        </el-select>
		      </template>
		      <template #append>
		        <el-button @click="handleQuery">
		          <el-icon style="font-size: 16px;align-itmes:center"><Search /></el-icon>
		        </el-button>
		      </template>
		    </el-input>
		   </el-space>
		   <div class='rt-btn-group'>
			   <el-button @click.stop="downloadList" :loading="downLoading"  type="primary">导出</el-button>  
		   </div>
		</el-row>
		</div>
	<el-row>
	<GlobalTable ref="globalTable" 
	height="calc(100vh - 200px)" 
	:tableData="tableData"   
	:defaultSort="{'prop': 'inwaretime', 'order': 'descending' }"
	style="width: 100%;margin-bottom:16px;" 
	 @loadTable="loadTableData"   >
		 <template #field>
			<el-table-column prop="name" fixed label="商品信息"    show-overflow-tooltip   >
			   <template #default="scope">
				   <el-space>
					   <div>  <el-image :src="scope.row.image" style="width:40px; height:40px"></el-image></div>
					   <div>
					   <div class='name' v-if="scope.row.name" >{{scope.row.name}}</div>
					   <div class='sku'>{{scope.row.sku}} 
					   <el-tag v-if="scope.row.issfg=='2'" type="success" size="small" >半成品</el-tag> 
					   </div>
					   </div>
				   </el-space>
			  </template>
			</el-table-column>
	        <el-table-column prop="number"   label="订单号"  align="center" sortable="custom"   >
				<template #default="scope">
					{{scope.row.number}}
					<div class="font-extraSmall">仓库名称:{{scope.row.wname}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="number"   label="时效"  align="center" sortable="custom"   >
				<template #default="scope">
					<el-tooltip effect="light">
						 <template #content> 
						       <el-descriptions  direction="vertical"   :column="2"   style="padding:10px;">
									 <el-descriptions-item label="下单时间" >{{dateFormat(scope.row.createdate)}}</el-descriptions-item>
									 <el-descriptions-item label="审核时间" >{{dateFormat(scope.row.audittime)}}</el-descriptions-item>
									 <el-descriptions-item label="最近入库" >{{dateFormat(scope.row.inwaretime)}}</el-descriptions-item>
									 <el-descriptions-item label="最近组装" >{{dateFormat(scope.row.asstime)}}</el-descriptions-item>
									 <el-descriptions-item label="最近发货" >{{dateFormat(scope.row.shiptime)}}</el-descriptions-item>
						       </el-descriptions>
						 </template>
								<p v-if="scope.row.days">{{scope.row.days}} 天</p>
								<p v-else-if="scope.row.days==0">1 天</p>
								<p v-else>暂无</p>
					</el-tooltip>
				</template>
			</el-table-column>
				<el-table-column prop="recqty"   label="收货" align="center"   sortable="custom">
					<template #default="scope">
						<el-tooltip effect="light">
							 <template #content> 
							       <el-descriptions    direction="vertical" style="padding:10px;"    :column="1"     >
							         <el-descriptions-item label="采购">{{scope.row.amount}}</el-descriptions-item>
							         <el-descriptions-item label="收货">{{scope.row.recqty}}</el-descriptions-item>
							       </el-descriptions>
							 </template>
							{{scope.row.recqty}}
						</el-tooltip>
					</template>
				</el-table-column>
				<el-table-column prop="changeAmount"   label="换货" align="center"   sortable="custom">
					<template #default="scope">
						<el-tooltip  >
							<template #content> 
								<div v-html="scope.row.changehtml"></div> 
							</template>
							<div>{{scope.row.changeAmount}}</div>
						</el-tooltip>
						<div v-if="scope.row.changeInv">
							 <span class="font-extraSmall">换货后库存:</span>
						     <span class="font-extraSmall" v-html="scope.row.changeInv"></span>  
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="changeAmount"   label="换货出库" align="center" >
					<template #default="scope">
						<el-tooltip v-if="scope.row.changeship">
							 <template #content> 
							 <div  v-html="scope.row.changeship.skushipqty"> </div>
							  </template>
							<div>
							    {{scope.row.changeship.shipqty}}
							</div>
						</el-tooltip>
					
					</template>
				</el-table-column>
				<el-table-column prop="lossamount"   label="盘点出库" align="center"   sortable="custom">
					<template #default="scope">
						<div v-if="scope.row.lossamount">{{scope.row.lossamount}}</div>
						<div v-else></div>
					</template>
				</el-table-column>
				<el-table-column prop="outqty"  label="出库"  align="center"   sortable="custom">
					<template #default="scope">
						<el-tooltip effect="light"  placement="left">
							 <template #content> 
						            <table class="table zu-zhuang-detail "  cellspacing="0" cellpadding="0" border="0">
										<tr class="el-table__row"><td>主SKU组装</td><td  width="120px">{{scope.row.assqty}} </td><td width="60px">代料</td><td width="120px"><span v-if="scope.row.changeAmount">{{scope.row.changeAmount}}</span><span v-else>0</span></td></tr>
										<tr class="el-table__row"><td>组装消耗</td><td  width="120px">{{scope.row.subqty}}</td><td width="60px">发货</td><td width="120px"><div style="width:120px;">{{scope.row.shipqty}}</div></td></tr>
										<tr class="el-table__row"><td>发货消耗</td><td  ><div  >{{scope.row.subshipqty}}</div></td><td width="60px">盘点</td><td width="120px"><div style="width:120px;">{{scope.row.lossamount}}</div></td></tr>
										<tr class="el-table__row"><td>发货消耗详情</td><td colspan="3" >  
										 <el-scrollbar max-height="200px">
										<div  v-if="scope.row.skushipqty"   class="font-extraSmall" v-html="scope.row.skushipqty"> </div>
										   </el-scrollbar></td></tr>
										<tr class="el-table__row"><td>主SKU组装详情</td><td colspan="3">
										<el-scrollbar  max-height="200px">
											<div v-if="scope.row.skuassqty" class="font-extraSmall" v-html="scope.row.skuassqty"> </div>
											</el-scrollbar></td></tr>
									</table>
							 </template>
							{{scope.row.outqty}}
						</el-tooltip>
					</template>
				</el-table-column>
				<el-table-column prop="fulfillable"   label="可用库存" align="center"   sortable="custom">
					<template #default="scope">
						<el-tooltip effect="light"  placement="left">
							 <template #content> 
								<table  class="table zu-zhuang-detail " style="width:100%" cellspacing="0" cellpadding="0" border="0"   >
									<tr>
										<td>待入库</td><td>{{scope.row.inbound}}</td>
										<td>可用库存</td><td>{{scope.row.fulfillable}}</td>
										<td>待出库</td><td>{{scope.row.outbound}}</td>
									</tr>
								 </table>
									<table v-if="scope.row.mainsku"  class="table zu-zhuang-detail " style="width:100%" cellspacing="0" cellpadding="0" border="0"  >
										 <tr>
											 <td>主SKU图片</td><td colspan="2">主SKU名称</td>
										 </tr>
										 <tr>
										 <td> <el-image :src="scope.row.mainimage" style="width:40px; height:40px"></el-image></td>
										 <td colspan="2">
											 <div  style="width:300px" class='text-omit-2 font-extraSmall'  v-if="scope.row.mainname" >{{scope.row.mainname}}</div>
											  <el-tag  type="warning" size="small" >组装成品</el-tag> 
										 </td>
										 </tr>
									<tr><td>主SKU列表</td><td colspan="2">
										 	 <el-scrollbar max-height="50px">
												 <div  style="width:220px;" class='  font-extraSmall' v-html="scope.row.mainsku">   </div>
											 </el-scrollbar>
									</td></tr>
									<tr><td>主SKU待入库库存</td>
										<td>主SKU可用库存</td>
										<td>主SKU待出库库存</td>
										 </tr>
										 <tr>
											 <td>{{scope.row.maininbound}}</td>
											 <td>{{scope.row.mainfulfillable}}</td>
											 <td>{{scope.row.mainoutbound}}</td>
										 </tr>
										 <tr v-if="scope.row.mainskuinbound"><td colspan="3">
										 主SKU待入库库存详情<br/>
										 	 	 <el-scrollbar max-height="50px">
										 			  <div    class="font-extraSmall" v-html="scope.row.mainskuinbound"></div>
										 		 </el-scrollbar>
										 </td></tr>
										 <tr  v-if="scope.row.mainskufulfillable">
											 <td colspan="3">
										 主SKU可用库存详情<br/>
										 	 	 <el-scrollbar max-height="50px">
										 			 <div  style="width:220px;" class='sku font-extraSmall'  v-html="scope.row.mainskufulfillable">   </div>
										 		 </el-scrollbar>
										 </td></tr>
										 <tr v-if="scope.row.mainskuoutbound"><td colspan="3">
										 主SKU待出库库存详情<br/>
										 	 	 <el-scrollbar max-height="50px">
										 			   <div    class="font-extraSmall" v-html="scope.row.mainskuoutbound"></div>
										 		 </el-scrollbar>
										 </td></tr>
									</table> 
							  </template>
							{{scope.row.fulfillable}}
						</el-tooltip>
					</template>
				</el-table-column>
	     </template>
	 </GlobalTable>
	</el-row>
		</div>
		<DownloadDialog ftype="purchase_time" ref="downloadDialogRef" @change="generateReport()"></DownloadDialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted,inject}from"vue";
	import {Search,ArrowDown,} from '@element-plus/icons-vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat} from "@/utils/index.js";
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import DownloadDialog from "@/views/erp/components/download_dialog.vue";
	let state=reactive({
		 downLoading:false,
		 tableData:{records:[],total:0},
		 searchKeywords:"",
		 selectlabel:'sku',
		 editRow:{warehouseVisable:false,},
		 queryParam:{},
		 isload:true,
	});
	let {
	   tableData,
	   downLoading,
	   editRow,
	   searchKeywords,
	   selectlabel,
	   queryParam,
	   isload,
	} =toRefs(state);
	const globalTable=ref();
	const downloadDialogRef=ref();
	function handleQuery(){
		if(state.selectlabel=='sku'){
			state.queryParam.sku=state.searchKeywords;
		}else{
			state.queryParam.msku=state.searchKeywords;
		}
		globalTable.value.loadTable(state.queryParam);
		state.isload=false;
	}
	function getTableHtml(value){
		if(value){
			value=value.replaceAll(",","<br/>");
			return value;
		}else{
			return value;
		}
		
	}
	function loadTableData(params){
		 purchaselistApi.getPurchaseTimeList(params).then((res)=>{
			 if(res&&res.data&&res.data.records){
				 res.data.records.forEach(row=>{
					 if(row.mainsku){
						row.mainsku="<div>"+row.mainsku.replaceAll(",","</div><div>")+"</div>";
					 }
					 if(row.changestr){
						row.changehtml="<el-space><div>"+row.changestr.replaceAll(";","</div><div>")+"</div></el-space>";
					 }
					 if(row.changeInv){
						 row.changeInv="<div>"+row.changeInv.replaceAll(";","</div><div>")+"</div>";
					 }
					 row.skuassqty=getTableHtml(row.skuassqty);
					 row.skushipqty=getTableHtml(row.skushipqty);
					 if(row.changeship){
				        row.changeship.skushipqty=getTableHtml(row.changeship.skushipqty);
					 }
					 row.mainskuinbound=getTableHtml(row.mainskuinbound);
					 row.mainskufulfillable=getTableHtml( row.mainskufulfillable);
					 row.mainskuoutbound=getTableHtml( row.mainskuoutbound);
					 row.skusubnumber=getTableHtml( row.skusubnumber);
				 })
			 }
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		});
	}
	
	//日期改变
	function changedate(dates){
		state.queryParam.startDate=dates.start;
		state.queryParam.endDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
	}
	
	function getWarehouse(val){
		state.queryParam.warehouseid=val;
		if(state.isload==false){
			handleQuery();
		}
	}
	function generateReport(){
		state.downLoading=true;
		if(state.selectlabel=='sku'){
			state.queryParam.sku=state.searchKeywords;
		 }
		 purchaselistApi.downloadTimeList({"sku":state.queryParam.sku,"startDate":state.queryParam.startDate,
		 "endDate":state.queryParam.endDate,"warehouseid":state.queryParam.warehouseid}).then(res=>{
			state.downLoading=false;
			ElMessage.success("已提交报表，等待生成");
			downloadDialogRef.value.show();
		});
	}
	function downloadList(){
		downloadDialogRef.value.show();
	}
	onMounted(()=>{
		 handleQuery();
	})
</script>

<style scoped>
	.zu-zhuang-detail td{
		border-bottom:1px solid #dedede;
		padding:8px;
	}
	.zu-zhuang-detail .name{
		
	}
</style>