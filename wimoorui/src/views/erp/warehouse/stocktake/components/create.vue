<template>
	<div class="el-white-bg">
			 
						 <div class="gird-line-head">
						 <h3 v-if="iswork==false">创建盘点单</h3>
						 <h3 v-else>编辑盘点单</h3>
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
							 	<el-input class="in-wi-24" disabled  v-model="form.number" placeholder="请输入"> </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="备注"  >
							 	<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="备注"> </el-input>
							 </el-form-item>
						  </el-col>
						  <el-col :span="12">
						  		 <el-form-item label="类型"   prop="warehouse">
						  			<el-radio-group v-model="ftype" class="ml-4" @change="changeFtype" :disabled="iswork">
						  				<el-radio :label="1" size="small">盘点产品</el-radio>
						  				<el-radio :label="2" size="small">盘点库位</el-radio>
										<el-radio :label="3" size="small">盘点产品(海外仓)</el-radio>
						  			  </el-radio-group> 
						  		 </el-form-item>
						   </el-col>
							 <el-col v-if="ftype==1||ftype==3" :span="8">
								 <el-form-item label="仓位"   prop="warehouse">
									 <el-cascader
									       style="width:100%"
									       v-model="warehouseCheck"
									       :options="warehouseData"
									 	   :value = "name"
									 	   :label="name"
									       :props="props"
									       @change="warehouseChange"
									 	  placeholder="全部仓库"
										  :disabled="iswork"
									 	  clearable
									     />
								 </el-form-item>
							 </el-col>
							 <el-col v-if="ftype==1 && iswork" :span="2">
									 <el-form-item     prop="warehouse">
										  <el-button   type="primary" @click="showUploadModal">导入产品</el-button>
									 </el-form-item>
							 </el-col>
							 
							 <el-col v-if="ftype==2" :span="12">
								 <el-form-item label="库位"   prop="warehouse">
									 <el-cascader
									      style="width:100%"
										   v-model="warehouseCheck"
										   :options="warehouseData"
										   :value = "name"
										   :label="name"
										   :props="propsShelf"
										   @change="warehouseChange"
										  placeholder="全部库位"
										  :disabled="iswork"
										  clearable
										 />
								 </el-form-item>
							 </el-col>
							
							 	 
							 <el-col :span="12"  v-if="iswork">
							  <el-form-item label="状态"  >
							     <el-tag type="success" effect="dark" >盘点中</el-tag>
							  </el-form-item>
							  </el-col>
							  </el-row>
						 </el-form>
						
						<!-- <el-divider></el-divider> -->
						<el-tabs v-model="activeWarehouse"  class="table-tabs" @tab-change="loadItemList()">
						    <el-tab-pane v-for="item in warehouseList" :label="item.name" :name="item.id" > </el-tab-pane>
						  </el-tabs>
						 <div class="mark-re">
						   <h5 v-if="ftype==1">产品列表
                                     <span style="padding-left:20px;" class="font-extraSmall">盘盈金额：</span>
									 <span class="text-green">￥{{form.overprice}}</span>  
									 <span style="padding-left:20px;" class="font-extraSmall">盘亏金额：</span>
									 <span class="text-red">￥{{form.lossprice}}</span>
							</h5>
                              <h5 v-else>产品列表 
							   <el-button type="primary" style="margin-left:10px" size="small" v-if="ftype==2" @click="downloadList" >导出产品</el-button>
							  </h5>
							 
							 <div class="mark-re">
								 <div style="float:left;margin-right: 10px;">
								 							   <el-radio-group v-model="hasinv" @change="loadItemList()"  >
								 							     <el-radio label="1" >库存为零参与盘点 </el-radio>
								 							     <el-radio label="2" >库存为零不参与盘点</el-radio>
								 							   </el-radio-group>
								 							</div>
															 <div style="float:left;margin-right: 10px;">
							    <el-input v-model="searchKeywords" @input="loadItemList()"  placeholder="搜索SKU" clearable />
								</div>
						    </div>
						 </div>
						 
						 <GlobalTable   ref="globalTable" :tableData="tableData" 
						   height="calc(100vh - 440px)" 
							border @loadTable="loadTableData" >
						 	<template #field>
							 <el-table-column  prop="image" label="图片"  width="70px">
							    <template #default="scope">
							     <el-image v-if="scope.row.image" :src="scope.row.image"  class="product-img"></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"    class="product-img"  ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column  prop="name" label="名称/SKU" sortable="custom"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.name}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column label="成本单价(￥)" prop="price" sortable="custom"/>
							 <el-table-column  v-if="ftype==1"  label="可用库存" prop="fulfillable" sortable="custom"/>
							 <el-table-column  v-if="ftype==2"  label="当前库位库存" prop="quantity" sortable="custom">
								   <template #default="scope">
									   <div>{{scope.row.quantity}}</div>
									   <div class="font-extraSmall">{{scope.row.addressname}}:{{scope.row.warehousename}}</div>
								 </template>
								 </el-table-column>
							 <el-table-column  v-if="ftype==1"  label="待出库" prop="outbound" sortable="custom"/>
							 <el-table-column label="盘点数量">
								  <template #default="scope">
									  <el-input :disabled="!iswork" clearable  @blur="saveItem(scope.row)" @clear="removeItem(scope.row)"  @input="scope.row.amount=CheckInputInt(scope.row.amount)"  v-model="scope.row.amount">
									  </el-input>
								  </template>  
							 </el-table-column>
							  <el-table-column v-if="ftype==1" label="账面数量"  >
								  <template #default="scope">
									  {{handleNums(scope.row)}}
								   </template>
							 </el-table-column>  
							  <el-table-column label="盘盈数量" prop="overamount" />
							  <el-table-column label="盘亏数量" prop="lossamount" />
							  <el-table-column label="库位库存" v-if="ftype==1" prop="lossamount" >
								   <template #default="scope">
								  <el-link type="primary" @click.stop="handleShowShelf(scope.row)">库位操作</el-link> 
								  </template>
							  </el-table-column>
							  <el-table-column label="仓库库存" v-else prop="lossamount" >
								   <template #default="scope">
									  <el-link type="primary" @click.stop="handleShowShelf(scope.row)">仓库操作</el-link> 
								   </template>
							  </el-table-column>
						 </template>
						 </GlobalTable>
						</div>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;padding-bottom:20px">
								<el-space>
									<el-button type="" @click="closeTab">关闭</el-button>
									<el-button type="primary" v-if="iswork==false&&!id" @click="startForm">开始盘点</el-button>
									<el-button type="primary" v-if="iswork==true" @click="cancelForm">取消盘点</el-button>
									<el-button type="primary" v-if="iswork==true" @click="endForm">盘点完成</el-button>
								</el-space>
							</div>
						</div>
	</div>
	
	<SkuInventory ref="skuInventoryRef"   @change="saveItem" />
	<UploadDialog ref="uploadDialogRef"   @upload="uploadItem" /> 
</template>


<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router';
	import stocktakingApi from '@/api/erp/inventory/stocktakingApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import SkuInventory from './sku_inventory.vue';
	import UploadDialog from './uploadDialog.vue';
	import shelfproductApi from '@/api/erp/warehouse/shelfproductApi.js';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import shelfApi from '@/api/erp/warehouse/shelf.js';
    const skuInventoryRef=ref();
	const uploadDialogRef=ref();
	const emitter = inject("emitter"); // Inject `emitter`
	const props = {
	  expandTrigger: 'hover',
	  value:'id',
	  label:'name',
	  multiple: true 
	}
	const propsShelf = {
	  value:'id',
	  label:'numbername',
	  multiple: true 
	}
	let globalTable=ref();
	
	function closeTab(){
		router.push({
			path:"/erp/warehouse/stocktake",
			query:{
				title:'库存盘点',
				path:"/erp/warehouse/stocktake",
			    replace:true,
				refresh:true
			},
		})
	};
	window.onbeforeunload = function () {
	    return '确认要关闭当前窗口？';
	}
	const router = useRouter()
	var id = router.currentRoute.value.query.id;
	const state = reactive({
		warehouseCheck:null,
		iswork:false,
		
		tableData:{records:[],total:0},
		form:{
			overprice:0,
			lossprice:0,
		},
		rules: {
			warehouse: [{ required: true, message: '出库仓库不能为空', trigger: 'blur' }],
		},
		hasinv:'2',
		warehouseData:[],
		searchKeywords:'',
		ftype:1,
		shelfVisible:false,
		warehouseList:[],
		activeWarehouse:'',
		queryParams:{},
	})
	const {
		iswork,
		form,
		tableData,
		rules,
		warehouseData,
		warehouseCheck,
		searchKeywords,
		hasinv,
		ftype,
		shelfVisible,
		warehouseList,
		activeWarehouse,
		queryParams,
	}=toRefs(state)
	function downloadList(){
		shelfproductApi.downloadList(state.queryParams);
	}
	
	function showUploadModal(){
		uploadDialogRef.value.show(id);
	}
	function uploadItem(formData){  
		stocktakingApi.uploadBaseInfoFile(formData).then((res)=>{
			ElMessage.success('操作成功！');
			uploadDialogRef.value.hide();
			loadItemList();
		});
	}
	
	function changeFtype(){
			state.warehouseCheck=null;
			state.tableData.records=[];
			state.tableData.total=0;
			state.warehouseList=[];
			loadWarehouse();
	}
	function handleShowShelf(row){
		if(state.iswork==true){
			row.stocktakingid=id;
			row.warehouseid=state.activeWarehouse;
			row.stocktype=state.ftype;
			skuInventoryRef.value.show(row);
		}else{
			ElMessage.error("请开始盘点才能操作！");
		}
	}
 function saveItem(row){
	 if(state.ftype==1){
		 saveWarehouseItem(row);
	 } else{
		 saveShelfItem(row);
	 }
 }
	function saveWarehouseItem(row){
		if(row!=""&& parseInt(row.amount)>=0){
			var data={};
			data.id=id;
			data.warehouseid=state.activeWarehouse;
			data.materialid=row.materialid;
			data.amount=row.amount;
			if(row.itemid){
				data.itemid=row.itemid;
			}else{
				data.itemid="";
			}
			var fulfillable=0;
			var outbound=0;
			if(row["fulfillable"]) {
				fulfillable=row.fulfillable;
			}
			if(row["outbound"]) {
				outbound=row.outbound;
			}
			var overamount =parseInt(row.amount)- parseInt(fulfillable+outbound);
			data.overamount=overamount;
			stocktakingApi.saveItem(data).then((res)=>{
				if(res.data.isok==true){
					ElMessage.success(  "添加成功！");
					state.form.overprice=res.data.overprice;
					state.form.lossprice=res.data.lossprice;
					row.itemid=res.data.item.id;
					row.overamount=res.data.item.overamount;
					row.lossamount=res.data.item.lossamount;
				}
			});
		}
	}
	function saveShelfItem(row){
		var data={};
		data.stocktakingid=id;
		data.materialid=row.materialid;
		data.shelfid=state.activeWarehouse;
		data.warehouseid=row.warehouseid;
		data.amount=parseInt(row.amount);
		var quantity=parseInt(row.quantity);
		if(data.amount>quantity){
			data.overamount=row.amount -quantity;
			data.lossamount=0;
		}else{
			data.overamount=0;
			data.lossamount=quantity-row.amount;
		}
		if(row.id){
			data.id=row.id;
			stocktakingApi.updateShelfStockItem(data).then((res)=>{
				ElMessage.success('操作成功！');
				if(!row.amount){
					row.amount=0;
				}else{
					if(!row.quantity){
						row.quantity=0; 
					}
					 row.lossamount=parseInt(row.quantity)-parseInt(row.amount);
					 row.overamount=parseInt(row.amount)-parseInt(row.quantity);
					 row.overamount= row.overamount>0? row.overamount:0;
					 row.lossamount= row.lossamount>0? row.lossamount:0;
				}
				
			})
		}else{
			stocktakingApi.saveShelfStockItem(data).then((res)=>{
				ElMessage.success('操作成功！');
				row.id=res.data.id;
				if(!row.amount){
					row.amount=0;
				}else{
					 if(!row.quantity){
						row.quantity=0; 
					 }
					 row.lossamount=parseInt(row.quantity)-parseInt(row.amount);
					 row.overamount=parseInt(row.amount)-parseInt(row.quantity);
					 row.overamount= row.overamount>0? row.overamount:0;
					 row.lossamount= row.lossamount>0? row.lossamount:0;
				}
			})
		}
		
		
		
	}
	function removeItem(row){
		if(state.ftype==1){
			removeWarehouseItem(row);
		} else{
			removeShelfItem(row);
		}
	}
	function removeWarehouseItem(row){
		if(row.itemid){
			var data={};
			data.id=id;
			if(row.itemid){
				data.itemid=row.itemid;
			}else{
				data.itemid="";
			}
			stocktakingApi.removeItem(data).then((res)=>{
				if(res.data.isok==true){
					ElMessage.success("删除成功！");
					state.overprice=res.data.overprice;
					state.lossprice=res.data.lossprice;
					row.itemid="";
					row.overamount="";
					row.lossamount="";
				}
			});
		}
	}
	function removeShelfItem(row){
		if(row.id){
			stocktakingApi.deleteShelfStockItem(row).then((res)=>{
				if(res.data.isok==true){
					ElMessage.success("删除成功！");
					row.itemid="";
					row.overamount="";
					row.lossamount="";
				}
			});
		}
	}
    function toEditPage(id){
		router.push({
			path:"/e/w/s",
			query:{
				title:'编辑盘点单',
				path:"/e/w/s",
				id:id,
			},
		})
		load();
	}
	function startForm(){
		if(state.warehouseCheck && state.warehouseCheck.length>0){
			var param={"id":0,"ftype":state.ftype};
			if(state.ftype==1){
				var data=[];
				state.warehouseList.forEach(item=>{
					data.push({stocktakingid:id,warehouseid:item.id});
				});
				param.warehouselist=data;
			}else{
				var data=[];
				state.warehouseList.forEach(item=>{
					data.push({stocktakingid:id,shelfid:item.id});
				})
			    param.shelflist=data;
			}
			stocktakingApi.startAction(param).then((res)=>{
				 if(res.data && res.data.isok==true){
					 id=res.data.id;
					 toEditPage(res.data.id);
				 }
			});
		}else{
			ElMessage.error("请选择仓库或者库位！");
		}
		
	}
	function endForm(){
		var data={};
		data.id=id;
		data.remark=state.form.remark;
		stocktakingApi.endAction(data).then((res)=>{
			if(res.data.isok==true){
				ElMessage.success(  "盘点完成！");
				router.push({
					path:"/e/w/s/d",
					query:{
						title:'盘点单详情',
						path:"/e/w/s/d",
						id:id,
						replace:true
					},
				})
			}
		});
	}
	function cancelForm(){
		ElMessageBox.confirm('确认取消盘点吗?', '警告', {
		 				confirmButtonText: '确定',
		 				cancelButtonText: '取消',
		 				type: 'warning',
		 })
		.then(() => {
		   stocktakingApi.cancelAction({"id":id}).then((res)=>{
		   	 if(res.data && res.data.isok==true){
		   		ElMessage.success(res.data.msg);
		   			router.push({
		   				path:"/erp/warehouse/stocktake",
		   				query:{
		   					title:'库存盘点',
		   					path:"/erp/warehouse/stocktake",
		   				    replace:true,
							refresh:true
						},
		   			})
		   	 }else{
		   		 ElMessage.error(  res.data.msg);
		   	 }
		   });
		})
		.catch(() => ElMessage.info('已取消操作'));
	}
	function loadTabsData(){
		var tabsid=state.warehouseCheck;
		var ftype=state.ftype;
		if(tabsid!=[] && tabsid!=null && tabsid!=""){
			if(ftype==1){
				stocktakingApi.stocktakingListWarehouse(tabsid).then(res=>{
					state.warehouseList=res.data;
					state.activeWarehouse=state.warehouseList[0].id;
					loadItemList();
				})
			}
			else{
				stocktakingApi.stocktakingListShelf(tabsid).then(res=>{
					state.warehouseList=res.data;
					state.activeWarehouse=state.warehouseList[0].id;
					loadItemList();
				})
			}
	    }
	}
	function warehouseChange(val){
		var warehouseid =[];
		if(val){
			if(state.ftype==1){//仓库
				val.forEach((values)=>{ warehouseid.push(values[1]); });
			}else{//库位
				val.forEach((values)=>{
					if(values.length>1){  warehouseid.push(values[(values.length)-1]); }
				});
			}
			state.warehouseCheck=warehouseid;
			loadTabsData();
		}else{
			warehouseid =[];
		}
	
	}
	function loadItemList(){
		var data={};
		data.search=state.searchKeywords;
		if(state.activeWarehouse){
			data.warehouseid=state.activeWarehouse;
		}else{
			data.warehouseid="";
		}
		if(state.hasinv=="2"){
			data.hasInv=true;
		}else{
			data.hasInv=false;
		}
		if(id){
			data.id=id;
		}else{
			data.id="";
		}
		globalTable.value.loadTable(data);
	} 
	function loadTableData(params){
		params.stocktakingid=id;
		if(state.ftype==1){
			stocktakingApi.searchCondition(params).then((res)=>{
				state.tableData.records = res.data.records;
				state.tableData.total =res.data.total;
			});
		}else{
			params.shelfid=params.warehouseid;
			params.isstocking="true";
			state.queryParams=params;
			shelfproductApi.getShelfInventoryStockingList(params).then(function(res) {
					state.tableData.records = res.data.records;
					state.tableData.total = res.data.total;
			})
		}
	
	}
	function handleNums(row){
		var amount=0,overamount=0,lossamount=0,fulfillable=0,outbound=0;
		if(row.amount2) {
			amount=row.amount2;
		}
		if(row.overamount) {
			overamount=row.overamount;
		}
		if(row.lossamount) {
			lossamount=row.lossamount;
		}
		if(row.fulfillable) {
			fulfillable=row.fulfillable;
		}
		if(row.outbound) {
			outbound=row.outbound;
		}
		if(row.amount2!=undefined) {
			return amount-overamount+lossamount;
		}else {
			return fulfillable+outbound;
		}
	}
	function loadWarehouse(){
		if(state.ftype==1){
			warehouseApi.getWarehouseList().then(function(res){
				state.warehouseData = res.data;
			})
		}else{
			shelfApi.getWarehouseShelf().then(function(res){
				state.warehouseData = res.data;
			});
		}
	}
	function load(){
			stocktakingApi.edit({"id":id}).then((res)=>{
				if(res.data){
					 state.form=res.data;
					 state.ftype=res.data.ftype;
					 if(res.data.isworking){
					 	 state.iswork=res.data.isworking;
					 }
					 state.hasinv='2';
					 if(res.data.id){
						 id=res.data.id;
					 }
					 var checkid=[];
					 if(state.ftype==2&&res.data.shelflist&&res.data.shelflist.length>0){
						 res.data.shelflist.forEach(item=>{
							checkid.push(item.shelfid) ;
						 })
					 }
					 if(state.ftype==1&&res.data.warehouselist&&res.data.warehouselist.length>0){
						 res.data.warehouselist.forEach(item=>{
							checkid.push(item.warehouseid) ;
						 })
					 }
					 state.warehouseCheck=checkid;
					 loadTabsData();
					 loadWarehouse();
				}
			});
	}
	onMounted(()=>{
		load();
	});
</script>

<style scoped>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 176px);
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
		.mark-re{
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
</style>
