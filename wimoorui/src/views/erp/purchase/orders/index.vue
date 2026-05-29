	<template>
		<div class="main-sty "> 
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
					<GroupSelect style="width:120px;" ref="groupRef"  @change="changeGroup" defaultValue="all" />
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
				  		  <el-form label-width="100px">
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
				     <!--功能区域 -->
				    <el-row>
				     <el-space >
						 <el-button type="primary" class="im-but-one" @click="handleAdd">
						   <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
						   <span>添加采购单</span>
						 </el-button>
						 <el-radio-group v-model="ftype" @change="handleQuery">
						     <el-radio-button label="单据" />
						     <el-radio-button label="SKU" />
						   </el-radio-group>
					 <!-- <el-button >物流单识别</el-button> -->
					   <el-dropdown v-if="(activeName==0||activeName==2) && ftype=='SKU'">
					         <el-button >批量审核 
							 <el-icon class="ic-cen"><arrow-down /></el-icon></el-button>
					        <template #dropdown>
					          <el-dropdown-menu>
					            <el-dropdown-item @click.stop="approveAll">通过</el-dropdown-item>
					            <el-dropdown-item @click.stop="returnAll">驳回</el-dropdown-item>
					          </el-dropdown-menu>
					        </template>
					      </el-dropdown>
						  <el-button v-if="activeName==3">批量下单</el-button>
						  <el-popconfirm title="自动付完货物费用,是否确认对选中项全部付款?"   @confirm="autoPayments" v-if="(activeName==4 || activeName==5) && ftype=='SKU'">
						      <template #reference>
						       <el-button >自动付款</el-button>
						      </template>
						    </el-popconfirm>
						 <el-popconfirm title="是否确认对选中项全部收货?"   @confirm="autoReceives" v-if="(activeName==4 || activeName==5) && ftype=='SKU'">
						     <template #reference>
						      <el-button >自动收货</el-button>
						     </template>
						   </el-popconfirm>
						  <el-button @click.stop="downExcelReports">导出SKU列表</el-button>
						  <el-button v-if="activeName=='5'" @click.stop="openUploadPayment">导入付款</el-button>
						 <el-button   @click.stop="showAutoShipfeeAll">自动付运费</el-button>
						  <el-button   @click.stop="showEditRemarkAll">批量修改产品备注</el-button>
				     </el-space>
				     <div class='rt-btn-group'>
						 <el-space>
						  <el-switch
						     v-if="activeName=='5'" 
						      v-model="paymentType"
						      class="ml-2"
							   width="60px"
							   @change="handleQuery"
						      inline-prompt
						      style="--el-switch-on-color:#3eba5b; --el-switch-off-color:  #b8b8b8;"
						      active-text="已请款"
						      inactive-text="未请款"
						    />
				  	  <!-- <el-button class='ic-btn'  title='列配置'>
				  		  <setting-two theme="outline" size="16"  :strokeWidth="3"/>
				  	   </el-button> -->
					   </el-space>
					   <el-button   class='ic-btn' :icon="Printer" @click="handlePrinterShow()" title='打印设置'>
					   </el-button>
				  		<el-button   class='ic-btn' title='帮助文档'>
				  		    <help theme="outline" size="16" :strokeWidth="3"/>
				  	   </el-button>
				     </div>
				  </el-row>
			  </div>
			  <div v-if="ftype=='单据'">
			<Table  ref="tableRef"  @selectrow="getrows" @changepay="loadStatusNums"  :tableState = "activeName" />
			</div>
			<div  v-else>
			<SkuTable  ref="skutableRef" @changepay="loadStatusNums"  @selectrow="getskurows"  :tableState = "activeName" />
			</div>
		</div>
		<UploadDialog ref="uploadDialogRef" @upload="handleUpload"></UploadDialog>
		<AutoPayment ref="autoPaymentRef"  @change="handleQuery()"></AutoPayment>
		<Printip ref="printipRef"></Printip>
		<ShipfeeDialog ref="shipfeeRef" @change="handleQuery()"></ShipfeeDialog>
		<RemarkDialog ref="remarkRef" @change="handleQuery()"></RemarkDialog>
	</template>
	<script>
	    export default{ name:"采购单" };
	</script>
	<script setup>
		import { ref,reactive,onMounted,toRefs,watch,inject} from 'vue'
		import Table from"./components/table.vue";
		import SkuTable from"./components/skuTable.vue"
		import Warehouse from '@/components/header/warehouse.vue';
		import GroupSelect from '@/components/header/group_select.vue';
		import Supplier from '@/components/header/supplier.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import UploadDialog from '@/components/Upload/uploadDialog.vue';
		import {Search,ArrowDown,Printer} from '@element-plus/icons-vue'
		import {useRouter } from 'vue-router'
		import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
		import { ElMessage, ElMessageBox } from 'element-plus';
		import FilterIcon from "@/components/icon/filtericon.vue";
		import Owner from '@/components/header/owner.vue';
		import Tags from '@/components/header/tags.vue';
		import Category from '@/components/header/category.vue';
		import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
		import AutoPayment from "./components/auto_price_dialog.vue";
		import Printip from "./components/printip.vue";
		import ShipfeeDialog from "./components/shipfee_dialog.vue"; 
		import RemarkDialog from "./components/remark_dialog.vue"; 
 
		const emitter = inject("emitter");
		const router = useRouter();
		const printipRef=ref();
		const autoPaymentRef=ref();
		const shipfeeRef=ref();
		let tableRef=ref();
		let skutableRef=ref();
		let datepickersRef=ref();
		let tagsRef=ref();
		let warehouseRef=ref();
		let groupRef=ref();
		let supplierRef=ref();
		let categoryRef=ref();
		let uploadDialogRef=ref();
		let remarkRef=ref();
		let state=reactive({
			ftype:'单据',
			remarks:'',
			productType:'全部产品',
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
				{label:'已作废',name:'7',},
				{label:'换货中',name:'8',},
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
			tableRows:[],
		})
		const{
			ftype,
			activeName,orderState,arrivalType,
		    paymentType,dateOptions,dateType,queryParam,
			isload,searchKeywords,searchtype,remarks,productType,passinware,nopass,
			passnopay,passpay,arrivalOptions,paymentOptions,payarrivalType,audistatusType,skurows,tableRows,
		}=toRefs(state);
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
		function handlePrinterShow(){
			printipRef.value.show();
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
		function resetDate(){
			const end = new Date();
			const start = new Date();
			end.setTime(end.getTime() - 3600 * 1000 * 24 * (0+1));
			start.setTime(start.getTime() - 3600 * 1000 * 24 * (365+0));
			var mydata={start:"",end:""};
			mydata.start=start.format("yyyy-MM-dd");
			mydata.end=end.format("yyyy-MM-dd")+" 23:59:59";
			state.queryParam.fromDate=mydata.start;
			state.queryParam.toDate=mydata.end;
		}
		function changedate(dates){
			state.queryParam.fromDate=dates.start;
			state.queryParam.toDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}else{
				if(router.currentRoute.value.query.number){
					  resetDate();
				}
			}
		}
		function handleQuery(){
			loadStatusNums();
			if(state.searchKeywords&&state.searchKeywords.length==14&&state.searchKeywords.indexOf("PO")==0){
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
			if(state.activeName=="8"){
				state.queryParam.auditstatus="18";
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
		function getrows(rows){
			state.tableRows=rows;
		}
		
		function showEditRemarkAll(){
			var entryids="";
			if(state.ftype=="SKU"){
				if(state.skurows && state.skurows.length>0){
					state.skurows.forEach(item=>{ 
						entryids=entryids+item.id+",";
					});
				}
			}else{
				if(state.tableRows && state.tableRows.length>0){
					state.tableRows.forEach(rows=>{
						if(rows.ischeck&&rows.itemlist && rows.itemlist.length>0){
							rows.itemlist.forEach(entry=>{
								entryids=entryids+entry.id+",";
							});
						}
					});
				}
			}
			remarkRef.value.show(entryids);
		}
		
		function showAutoShipfeeAll(){
			var itemlist=[];
			if(state.ftype=="SKU"){
				if(state.skurows && state.skurows.length>0){
					state.skurows.forEach(item=>{ itemlist.push(item); });
				}
			}else{
				if(state.tableRows && state.tableRows.length>0){
					state.tableRows.forEach(rows=>{
						if(rows.ischeck&&rows.itemlist && rows.itemlist.length>0){
							rows.itemlist.forEach(item=>{
								itemlist.push(item);
							});
						}
					});
				}
			}
			var row={};
			row.itemlist=itemlist;
			shipfeeRef.value.show(row);
		}
		function getskurows(rows){
			state.skurows=rows;
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
		onMounted(()=>{
			var timer=setTimeout(function(){
				if(router.currentRoute.value.query.number){
						datepickersRef.value.reset(3);
				}
				clearTimeout(timer);
			},200)
			load();
		});
		watch(() =>router.currentRoute.value.query,(newValue,oldValue)=> {
		  if(newValue&&newValue['refresh']){
			      state.searchKeywords=router.currentRoute.value.query.number;
			      delete router.currentRoute.value.query.number;
						 setTimeout(()=>{
							 if(state.searchKeywords){
								 datepickersRef.value.reset(3);
								 resetDate();
							 }
							 
							handleQuery();
						 },100);
					 }
		  },{ immediate: true });
	</script>
	
	<style scoped="scoped">
		.text-orange{
			color:var(--el-color-primary);
			font-size:12px;
			font-weight: 700;
		}
	</style>
	



