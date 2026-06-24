	<template>
	  <el-dialog v-model="visable" top="1vh"   width="80%" title="采购单列表">
		<div > 
		<div class="con-header">
			<el-tabs v-model="activeName"  @tab-change="handleChange">
			  <el-tab-pane v-for="item in orderState"  :name="item.name" :key="item.name">
				  <template #label>
					  {{item.label}}
					  <span class="text-orange">{{item.length}}</span>
				  </template>
			  </el-tab-pane>
			  </el-tabs>
				    <el-row>
				   <el-space>
				   <Warehouse  ref="warehouseRef" @changeware="changeWarehouse" />
				   <Supplier  ref="supplierRef"   @change="changeSupplier" />
		           <el-select style="width:110px;"  v-if="activeName=='4'" v-model="arrivalType"  @change="handleQuery" placeholder="收货状态">
						<el-option v-for="item in arrivalOptions" :label="item.label" :value="item.value" ></el-option>
					</el-select>  
				   <el-select  style="width:110px;"  v-if="activeName=='6'"  v-model="audistatusType" @change="handleQuery">
					   <el-option  label="全部完成" value="" ></el-option>
					   <el-option  label="付款完成" value="11" ></el-option>
					   <el-option  label="入库完成" value="12" ></el-option>
				   </el-select>
					  
				   <el-select  style="width:110px;"  v-if="activeName=='5'" v-model="payarrivalType"  @change="handleQuery" placeholder="收货状态">
				   	<el-option v-for="item in paymentOptions" :label="item.label" :value="item.value" ></el-option>
				   </el-select>
				   <div class="date-picker-group" >
				   	<el-select style="width:120px;" v-model="dateType" @change="handleQuery">
				   		<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
				   	</el-select>
				   	<Datepicker style="width: 240px;" longtime="ok" ref="datepickersRef" @changedate="changedate" />
				   </div>
				     <el-input  v-model="searchKeywords" placeholder="请输入" clearable @input="handleQuery"  style="min-width:240px;" >
				        <template #prepend>
				          <el-select v-model="searchtype" @change='searchTypeChange' placeholder="SKU"  style="width:90px">
				            <el-option label="SKU" value="sku"></el-option>
				            <el-option label="订单号" value="order"></el-option>
				            <el-option label="运单号" value="logistics"></el-option>
				          </el-select>
				        </template>
				        <template #append>
				          <el-button @click="handleQuery">
				             <el-icon style="font-size: 16px;align-itmes:center">
				              <search />
				           </el-icon>
				          </el-button>
				        </template>
				      </el-input>
				     <el-popover v-if="ftype=='SKU'"  :teleported="true"  :width="500" trigger="click">
				           <template #reference>
				             <el-button  title='高级筛选'  class='ic-btn'>
				                 <FilterIcon></FilterIcon>
				             </el-button>
				           </template>
				  		  <el-form :model="form" label-width="100px">
				  		  <el-form-item label="产品标签">
				  		       <Tags @change="changeTags" ref="tagsRef"></Tags>
				  		     </el-form-item>
							 <el-form-item label="产品负责人">
							      <Owner @owner="changeOwner" ref="ownerRef" ></Owner>
							    </el-form-item>
							  <el-form-item label="产品名称">
								     <el-input   v-model="queryParam.name"></el-input>
							   </el-form-item>
							  <el-form-item label="产品分类">
							 <Category @change="changeCategory" ref="categoryRef"></Category>
							  </el-form-item>
							  <el-form-item label="类型">
									 <el-radio-group v-model="productType">
										  <el-radio-button label="全部产品" />
										  <el-radio-button label="单独产品" />
										  <el-radio-button label="组合产品" />
										  <el-radio-button label="待组装产品" />
										</el-radio-group>
							</el-form-item>	
							<el-form-item label="备注">
							  <el-input
							      v-model="remarks"
							      :rows="2"
							      type="textarea"
							      placeholder="请输入"
							    />
							 </el-form-item>
				  			  <el-form-item>
				  			       <el-button type="primary" @click.stop="handleQuery()">搜索</el-button>
				  			     </el-form-item>
				  			</el-form>
				         </el-popover>
				      <el-button @click.stop="clearSearch">重置</el-button>
				  		
				    </el-space>
				    </el-row>
			  </div>
			<SkuTable   ref="skutableRef" @change="changeRow"    :tableState = "activeName" />
		</div>
		   <template #footer>
		     <div class="dialog-footer">
		       <el-button @click="visable = false">取消</el-button>
		       <el-button type="primary" @click="handleSubmit">  确定 </el-button>
		     </div>
		   </template>
		</el-dialog>
	</template>
	<script>
	    export default{ name:"采购单对话框" };
	</script>
	<script setup>
		import { ref,reactive,onMounted,toRefs,inject} from 'vue'
		import Table from"./components/table.vue";
		import SkuTable from"./components/sku_table_inner_dailog.vue"
		import Warehouse from '@/components/header/warehouse.vue';
		import GroupSelect from '@/components/header/group_select.vue';
		import Supplier from '@/components/header/supplier.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import UploadDialog from '@/components/Upload/uploadDialog.vue';
		import {Search,ArrowDown,} from '@element-plus/icons-vue'
		import {useRouter } from 'vue-router'
		import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
		import { ElMessage, ElMessageBox } from 'element-plus';
		import FilterIcon from "@/components/icon/filtericon.vue";
		import Owner from '@/components/header/owner.vue';
		import Tags from '@/components/header/tags.vue';
		import Category from '@/components/header/category.vue';
		import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
		import AutoPayment from "./components/auto_price_dialog.vue";
		const emitter = inject("emitter");
		const router = useRouter()
		const autoPaymentRef=ref();
		const emit = defineEmits(['change',]);
		let tableRef=ref();
		let skutableRef=ref();
		let datepickersRef=ref();
		let tagsRef=ref();
		let warehouseRef=ref();
		let groupRef=ref();
		let supplierRef=ref();
		let categoryRef=ref();
		let uploadDialogRef=ref();
		let state=reactive({
			ftype:'SKU',
			remarks:'',
			selectRow:{},
			productType:'全部产品',
			visable:false,
			searchtype:'sku',
			activeName:'0',
			searchKeywords:'',
			isload:true,
			queryParam:{},
			passinware:0,
			nopass:0,
			passnopay:0,
			passpay:0,
			orderState:[
				{label:'全部订单',name:'0',},
				// {label:'待提交',name:'1',length:0,},
				{label:'待审核',name:'2',length:0},
				// {label:'待下单',name:'3',length:0,},
				{label:'待付款',name:'5',length:0},
				{label:'待收货',name:'4',length:0},
				{label:'已完成',name:'6',},
				// {label:'已作废',name:'8',},
			],
			arrivalType:'',
			payarrivalType:'',
			paymentType:false,
			audistatusType:'12',
			dateType:'0',
			dateOptions:[
				{label:'创建日期',value:'0',},
				{label:'预计到货日期',value:'1',},
				{label:'审核日期',value:'2',},
			],
			arrivalOptions:[
				{label:'全部状态',value:'',},
				{label:'待付款',value:'notpay',},
				{label:'已付款',value:'pay',},
				{label:'已签待入',value:'needrec',},
				{label:'收货超期',value:'outdate',},
			],
			paymentOptions:[
				{label:'全部状态',value:'',},
				{label:'待收货',value:'notin',},
				{label:'已收货',value:'in',},
				{label:'收货超期',value:'outdate',},
			],
			skurows:[],
		})
		const{
			ftype,visable,selectRow,
			activeName,orderState,arrivalType,
		    paymentType,dateOptions,dateType,queryParam,
			isload,searchKeywords,searchtype,remarks,productType,passinware,nopass,
			passnopay,passpay,arrivalOptions,paymentOptions,payarrivalType,audistatusType,skurows,
		}=toRefs(state)
		function handleAdd(){
			emitter.emit("removeCache", "添加采购单");
			router.push({
				path:"/e/p/o",
				query:{
					title:'添加采购单',
					path:"/e/p/o",
				},
			})
		}
		function handleSubmit(){
			emit("change",state.selectRow);
			state.visable=false;
		}
		function handleChange(val){
			state.activeName = val;
			if(state.activeName=="2" || state.activeName=="3" || state.activeName=="4" || state.activeName=="5"){
				state.queryParam.fromDate=null;
				state.queryParam.toDate=null;
				datepickersRef.value.setBlank();
			}else{
				datepickersRef.value.reset();
				var dates=datepickersRef.value.getValue();
				state.queryParam.fromDate=dates.start;
				state.queryParam.toDate=dates.end;
			}
			handleQuery();
		}
		function changeGroup(val){
			state.queryParam.groupid=val;
			if(state.isload==false){
				handleQuery();
			} 
		}
		function changeSupplier(val){
			state.queryParam.supplierid=val;
			handleQuery();
		}
		function changeWarehouse(val){
			state.queryParam.warehouseid=val;
			if(state.isload==false){
				handleQuery();
			}
		}
		function openUploadPayment(){ 
			uploadDialogRef.value.show({'template':"PurchasePayment",'title':"采购付款导入"});
		}
		function handleUpload(formDatas){
			purchaselistApi.uploadPaymentFile(formDatas).then(res=>{
				handleQuery();
				uploadDialogRef.value.hide();
			});
		}
		//日期改变
		function changedate(dates){
			state.queryParam.fromDate=dates.start;
			state.queryParam.toDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		}
		function handleQuery(){
			loadStatusNums();
			if(state.searchKeywords.length==14&&state.searchKeywords.indexOf("PO")==0){
				state.searchtype="order";
			}
			state.queryParam.search=state.searchKeywords;
			state.queryParam.ftype=state.searchtype;
			if(state.activeName=="0"){
				state.queryParam.auditstatus="all";
			}
			if(state.activeName=="2"){
				state.queryParam.auditstatus="1";
			}
			// if(state.activeName=="3"){
			// 	state.queryParam.auditstatus="alls";
			// }
			if(state.activeName=="4"){
				state.queryParam.auditstatus="4";
			}
			if(state.activeName=="5"){
				state.queryParam.auditstatus="6";
			}
			if(state.activeName=="6"){
				state.queryParam.auditstatus="3";
			}
			if(state.activeName=="7"){
				state.queryParam.auditstatus="0";
			}
			if(state.dateType=="0"){
				state.queryParam.datetype="createdate";
			}
			if(state.dateType=="1"){
				state.queryParam.datetype="deliverydate";
			}
			if(state.dateType=="2"){
				state.queryParam.datetype="approvaldate";
			}
			if(!state.queryParam.warehouseid){
				state.queryParam.warehouseid="";
			}
			if(state.activeName=='4'){
				 state.queryParam.auditstatusparam=state.arrivalType;
			}
			if(state.activeName=='5'){
				 state.queryParam.auditstatusparam=state.payarrivalType;
			}
			if(state.productType=="全部产品"){
				state.queryParam.issfg="";
			}else if(state.productType=="单独产品"){
				state.queryParam.issfg="0";
			}else if(state.productType=="组合产品"){
				state.queryParam.issfg="1";
			}else if(state.productType=="待组装产品"){
				state.queryParam.issfg="2";
			}
			if(state.activeName=='5'){
				if(state.paymentType==false){
					state.queryParam.auditstatus="6";
				}else{
					state.queryParam.auditstatus="5";
				}
			}
			if(state.activeName=='6'){
				if(state.audistatusType=="11"){
					state.queryParam.auditstatus="11";
				}else if(state.audistatusType=="12"){
					state.queryParam.auditstatus="12";
				}else{
					state.queryParam.auditstatus="3";
				}
			}
			if(state.ftype=="SKU"){
				skutableRef.value.load(state.queryParam);
			}else{
				tableRef.value.load(state.queryParam);
			}
			if(state.isload==true){
				state.isload=false;
			}
		}
		function downExcelReports(){
			//下载表格数据
			purchaselistApi.downExcelReports(state.queryParam);
		}
	 
		function getskurows(rows){
			state.skurows=rows;
		}
		function changeRow(row){
			state.selectRow=row;
			
		}
		//批量审核
		function approveAll(){
			if(state.skurows && state.skurows.length>0){
				var ids="";
				state.skurows.forEach(function(item){
					ids=ids+(item.id+",");
				});
				purchaselistApi.approval({"ids":ids}).then((res)=>{
					if(res.data){
						ElMessage.success('批量审核成功!');
						handleQuery();
					}
				});
			}else{
				ElMessage.error('至少选择一行数据!');
			}
		}
		//批量驳回
		function returnAll(){
			if(state.skurows && state.skurows.length>0){
				var ids="";
				state.skurows.forEach(function(item){
					ids=ids+(item.id+",");
				});
				purchaselistApi.purchaseReturn({"ids":ids,"remark":null}).then((res)=>{
					if(res.data){
						ElMessage.success('批量驳回成功!');
						handleQuery();
					}
				});
			}else{
				ElMessage.error('至少选择一行数据!');
			}
		}
		function autoPayments(){
			if(state.skurows && state.skurows.length>0){
				var idslist=[];
				state.skurows.forEach(function(item){
					if(item.auditstatus==2 && item.paystatus==0){
						idslist.push(item.id);
					}
				});
				if(idslist.length>0){
					autoPaymentRef.value.show(idslist);
				}else{
					ElMessage.error('选定的暂无付款项！');
				}
			}else{
				ElMessage.error('至少选择一行数据!');
			}
		}
		function autoReceives(row){
			if(state.skurows && state.skurows.length>0){
				var idslist=[];
				state.skurows.forEach(function(item){
					if(item.auditstatus==2 && item.inwhstatus==0){
						idslist.push(item.id);
					}
				});
				if(idslist.length>0){
					purchaselistApi.autorec(idslist).then((res)=>{
						if(res.data){
							ElMessage.success('自动收货成功！');
							 handleQuery();
						}
					});
				}else{
					ElMessage.error('所选定的暂无收货项！');
				}
			}else{
				    ElMessage.error('至少选择一行数据!');
			}
		}
		function searchTypeChange(){
			handleQuery();
		}
		function loadStatusNums(){
			//加载各个状态下的num
			var ftypes="";
			if(state.ftype=="SKU"){
				ftypes="sku";
			}else{
				ftypes="orders";
			}
			purchaselistApi.getPurchaseNumAllStatus({"ftype":ftypes}).then((res)=>{
				if(res.data){
					if(ftypes=="orders"){
						state.orderState[1].length=res.data.nopass;
						//state.orderState[2].length=0;
						state.orderState[3].length=res.data.passinware;
						state.orderState[2].length=res.data.passnopay+res.data.passpay;
					}else{
						state.orderState[1].length=res.data.nopassentry;
						//state.orderState[2].length=0;
						state.orderState[3].length=res.data.passinwareentry;
						state.orderState[2].length=res.data.passnopayentry+res.data.passpayentry;
					}
					
				}
					
			});
		}
		function changeTags(tags){
			 state.queryParam.taglist=tags;
		}
		function changeCategory(value){
			state.queryParam.categoryid=value;
		}
		function changeOwner(id){
			state.queryParam.owner=id;
		}
		function clearSearch(){
			warehouseRef.value.warehouseid="";
			state.queryParam.warehouseid="";
			supplierRef.value.reset();
			state.queryParam.supplierid="";
			state.searchtype="sku";
			state.searchKeywords="";
			state.queryParam.search="";
			state.queryParam.name="";
			if(state.ftype=="SKU"){
				categoryRef.value.reset();
				state.queryParam.categoryid="";
				state.productType="全部产品";
				state.queryParam.issfg="";
				state.remarks="";
				state.queryParam.remark="";
				state.queryParam.taglist=[];
				tagsRef.value.state.tagsValue="";
			}
			if(state.activeName=='4'){
				 state.arrivalType='';
			}
			if(state.activeName=='5'){
				 state.payarrivalType='';
			}
			handleQuery();
		}
		function load(){
			loadStatusNums();
		}
		function show(){
			state.visable=true;
		}
		defineExpose({
		   show,
		})
		onMounted(()=>{
			load();
		});
		
	</script>
	
	<style scoped="scoped">
		.text-orange{
			color:var(--el-color-primary);
			font-size:12px;
			font-weight: 700;
		}
	</style>
	



