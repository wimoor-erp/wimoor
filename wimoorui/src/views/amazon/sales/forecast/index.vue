<template>
	<div class="main-sty">
		<div class="">
	    	<el-row>
	    		<el-space class="m-b-16">
	    			<Group ref="groups" @change="changeGroup"  defaultValue="onlyEU"/>
	    			<Owner   @owner="changeOwner" />
					        <el-date-picker
					                v-model="dateValue"
					        		@change = "dateChange"
					                type="daterange"
					        		:clearable="false"
					                range-separator="至"
					                start-placeholder="开始上架日期"
					                end-placeholder="结束上架日期"
					                :shortcuts="shortcuts"
					              />
	    			<el-input v-model="localParams.searchKeywords"  clearable  @input="handleQuery"  placeholder="请输入" class="input-with-select">
	    				<template #prepend>
	    					<el-select v-model="localParams.selectlabel" @change="handleQuery"  placeholder="SKU"
	    						style="width: 110px">
	    						<el-option label="SKU" value="sku"></el-option>
	    						<el-option label="MSKU" value="msku"></el-option>
	    						<el-option label="ASIN" value="asin"></el-option>
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
					
	    			<!-- <el-button>重置</el-button> -->
	    		</el-space>
	    		<div class='rt-btn-group m-b-16'>
					<div style="padding-right:10px;"><el-checkbox  v-model="queryParams.needplan" @change="handleQuery" label="发货规划中未出现" /></div>
					<el-button  :loading="downLoading" @click="handleDownloadSales"><el-icon><Download /></el-icon>导出</el-button>
					<el-button :loading="upLoading"  @click="handleUploadSales"><el-icon><Upload /></el-icon>导入</el-button>
	    			<el-button class='ic-btn' title='帮助文档'>
	    				<help theme="outline" size="16" :strokeWidth="3" />
	    			</el-button>
	    		</div>
	    	</el-row>
		</div>
		<div class="con-body">
			<GlobalTable
					height="calc(100vh - 250px)" 
					ref="gloablTableRef"
					:tableData="tableData"
					:size="10"
					:stripe="true" 
					:defaultSort="{ prop: 'openDate', order: 'descending' }"  
					@loadTable="loadtableData" >
				<template #field>
				<el-table-column label="图片" prop="img" width="65">
					<template #default="scope">
					<el-image v-if="scope.row.image" :src="scope.row.image"  class="img-40"  ></el-image>
					<el-image v-else :src="$require('empty/noimage40.png')"   class="img-40"   ></el-image>
					</template>
				</el-table-column>
				<el-table-column label="名称/SKU" prop="sku" sortable="custom" show-overflow-tooltip="">
					<template #default="scope">
						<div class="name">{{scope.row.name}}</div>
						<div  >
						<span class=" sku pointer" @click="showMore(scope.row)">{{scope.row.psku}} </span><span class="font-extraSmall"> [{{scope.row.asin}}] </span>
						<el-tag size="small" effect="dark" :type="scope.row.statuscolor" v-if="scope.row.statusname">{{scope.row.statusname}}</el-tag>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="店铺/国家" width="120" prop="groupname"  sortable="custom" >
					<template #default="scope">
						<div >{{scope.row.groupname}}</div>
						<div class="font-extraSmall">{{scope.row.marketname}}</div>
					</template>
				</el-table-column>
				<el-table-column label="本地库存" width="120" prop="fulfillable"  >
					<template #default="scope">
						<div >{{scope.row.fulfillable}}</div>
						<div class="font-extraSmall">待入库:{{scope.row.inbound}}</div>
					</template>
				</el-table-column>
				<el-table-column label="销量" prop="state" >
					<el-table-column label="日均" prop="avgsales" width="80" :sort-orders="sortOrders" sortable="custom" >
						<template #default="scope">
							<span v-if="scope.row.avgsales">{{scope.row.avgsales}}</span>
							<span v-else>0</span>
							<el-link @click="handleSales(scope.row)"  
							type="warning" class="m-l-8 ">
							<el-icon class="ic-cen"><Histogram /></el-icon>
							</el-link>
						</template>
					</el-table-column>
					<el-table-column label="7天" prop="sumseven" :sort-orders="sortOrders" width="80" sortable="custom" >
						<template #default="scope">
							<span v-if="scope.row.sumseven">{{scope.row.sumseven}}</span>
							<span v-else>0</span>
						</template>
					</el-table-column>
					<el-table-column label="30天" prop="summonth" :sort-orders="sortOrders" width="80" sortable="custom" >
						<template #default="scope">
							<span v-if="scope.row.summonth">{{scope.row.summonth}}</span>
							<span v-else>0</span>
						</template>
					</el-table-column> 
				</el-table-column>
				<el-table-column label="上架时间" prop="openDate" :sort-orders="sortOrders" sortable="custom" width="110">
					<template #default="scope">
						<div>{{dateFormat(scope.row.openDate)}}</div>
						<div v-if="scope.row.multplus" class="font-extraSmall">复合增长率{{scope.row.multplus}}%</div>
					</template>
				</el-table-column>
				<el-table-column label="销量预测"  prop="operator">
					<el-table-column :label="header.month1+'月'" width="100">
						<template #default="scope">
							<el-input v-model="scope.row.month1sales" 
							   @input="scope.row.month1sales=CheckInputInt(scope.row.month1sales)"></el-input>
						</template>
					</el-table-column>
					<el-table-column :label="header.month2+'月'" width="100">
						<template #default="scope">
							<el-input v-model="scope.row.month2sales"
							 @input="scope.row.month2sales=CheckInputInt(scope.row.month2sales)"
							></el-input>
						</template>
					</el-table-column>
					<el-table-column :label="header.month3+'月'" width="100">
						<template #default="scope">
							<el-input v-model="scope.row.month3sales"
							@input="scope.row.month3sales=CheckInputInt(scope.row.month3sales)"
							></el-input>
						</template>
					</el-table-column>
				</el-table-column>
				
				<el-table-column label="操作" width="140">
					 <template #header>
							操作 <el-button  style="margin-top:-3px;"  @click="submitFormBatch()" type="primary" link>批量保存</el-button>
					 </template>
					<template #default='scope'>
					 <el-space>
						<el-button :loading="scope.row.saveLoading"  @click="submitForm(scope.row)" type="primary" link>保存</el-button>
						<el-button @click="handleShowSalesAdjust(scope.row)" type="primary" link>详细计划</el-button>
					  </el-space>	
					</template>
				</el-table-column>
				</template>
			</GlobalTable>
		</div>
	</div>
	<SalechartDialog ref="salechartRef"/>
	<SaleAdjustDialog ref="saleAdjustDialogRef"></SaleAdjustDialog>
	<SaleAdjustUploadDialog ref ="saleAdjustUploadDialogRef"></SaleAdjustUploadDialog>
	<GoodsDeatils ref="goodsDeatilsRef" :productInfoData="productInfoData"/>
</template>
<script>
    export default{ name:"销售计划" };
</script>
<script setup>
	import {h,ref,reactive,toRefs,onMounted} from "vue"
	import {MoreOne,Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {Search,ArrowDown,Histogram,Download,Upload} from '@element-plus/icons-vue';
	import Group from '@/components/header/group.vue';
	import Owner from '@/components/header/owner.vue';
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus';
	import {useRouter } from 'vue-router';
	import SalechartDialog from "@/views/amazon/listing/common/salechart.vue";
	import {getProductMonthSales} from "@/api/amazon/listing/preSalesApi.js";
	import {getSales,save,clear} from "@/api/amazon/listing/preSalesApi.js";
	import GoodsDeatils from "@/views/amazon/listing/common/goods_deatils.vue"
	import {dateFormat,CheckInputInt} from "@/utils/index.js";
	import SaleAdjustDialog from "./components/sale_adjust_dialog.vue";
	import SaleAdjustUploadDialog from "./components/sale_adjust_upload_dialog.vue";
	import {downExcelSales} from "@/api/amazon/listing/preSalesApi.js";
	const spacer = h(ElDivider, { direction: 'vertical' });
	const salechartRef =ref();
	const saleAdjustDialogRef=ref();
	const saleAdjustUploadDialogRef=ref();
	const gloablTableRef=ref();
	let goodsDeatilsRef=ref();
	const router = useRouter();
	const state = reactive({
		queryParams:{needplan:false},
		downLoading:false,
		upLoading:false,
		dateValue:[],
		sortOrders:[ 'descending','ascending', null],
		tableData:{records:[],total:0},
		header:{month1:"",month2:"",month3:""},
		localParams:{selectlabel:"sku"},
	})
	const { queryParams ,dateValue,sortOrders,localParams,header,downLoading,upLoading,tableData}=toRefs(state);
	const shortcuts = [
	  {
	    text: '近一个月上架',
	    value: () => {
	      const end = new Date()
	      const start = new Date()
	      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
	      return [start, end]
	    },
	  },
	  {
	    text: '近3个月上架',
	    value: () => {
	      const end = new Date()
	      const start = new Date()
	      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
	      return [start, end]
	    },
	  },
	  {
	    text: '近1年上架',
	    value: () => {
	      const end = new Date()
	      const start = new Date()
	      start.setTime(start.getTime() - 3600 * 1000 * 24 * 365)
	      return [start, end]
	    },
	  },
	  {
	    text: '近3年上架',
	    value: () => {
	      const end = new Date()
	      const start = new Date()
	      start.setTime(start.getTime() - 3600 * 1000 * 24 * 365*3)
	      return [start, end]
	    },
	  },
	];
	
	function loadtableData(params){
		getProductMonthSales(params).then(res=>{
			if(res&&res.data&&res.data.total>0){
				state.tableData.records=res.data.records;
				state.tableData.total=res.data.total;
				state.header.month1= res.data.records[0].month1;
				state.header.month2=res.data.records[0].month2;
				state.header.month3=res.data.records[0].month3;
			}else{
				state.tableData.records=[];
				state.tableData.total=0;
			}
		});
	}
	function dateChange(date){
			if(date[1]){
			  state.queryParams.fromDate = date[0];
			  state.queryParams.toDate = date[1];
			  handleQuery();
			}
	}
	function showMore(row){
		goodsDeatilsRef.value.show(row);
	}
	function handleQuery(){
		if(state.localParams.searchKeywords){
			if(state.localParams.selectlabel=="sku"){
				state.queryParams.asin="";
				state.queryParams.msku="";
				state.queryParams.sku=state.localParams.searchKeywords;
			}
			if(state.localParams.selectlabel=="asin"){
				state.queryParams.sku="";
				state.queryParams.msku="";
				state.queryParams.asin=state.localParams.searchKeywords;
			}
			if(state.localParams.selectlabel=="msku"){
				state.queryParams.sku="";
				state.queryParams.asin="";
				state.queryParams.msku=state.localParams.searchKeywords;
			}
		}else {
				state.queryParams.sku="";
				state.queryParams.asin="";
				state.queryParams.msku="";
		}
		 gloablTableRef.value.loadTable(state.queryParams);
	}
	function handleUploadSales(){
		saleAdjustUploadDialogRef.value.show();
	}
	function handleDownloadSales(){
		state.downLoading=true;
		downExcelSales(state.queryParams,item=>{
			state.downLoading=false;
		});
	}
	function changeGroup(value){
	    state.queryParams.groupid=value.groupid;
		state.queryParams.marketplaceid=value.marketplaceid;
		handleQuery()
	}
	function changeOwner(value){
		state.queryParams.owner=value;
		if(state.queryParams.groupid){
		   handleQuery()
		}
	}
	function handleShowSalesAdjust(row){
		row.sysavgsales=row.avgsales;
		saleAdjustDialogRef.value.show(row,row);
	}
	function monthDate(monthstr){
		var monthstr=monthstr.split("-");
		var year=parseInt(monthstr[0]);
		var month=parseInt(monthstr[1])-1;
		var date=new Date();
		date.setYear(year);
		//date=new Date(date);
		date.setMonth(month);
		//date=new Date(date);
		date.setDate(1);
		//date=new Date(date); 
		return date;
	}
	async function submitFormBatch(){
		for(var i=0;i<state.tableData.records.length;i++){
			var row=state.tableData.records[i];
			if(i==state.tableData.records.length-1){
			   await submitForm(row,false);
			}else{
			   await submitForm(row,true);
			}
		}
		 
	}
	async function submitForm(row,isbatch){
			 var monthdata={};
			 var preSaleList=[];
			 if(  (row.month1sales&&row.month1sales!="0")
			    ||(row.month2sales&&row.month2sales!="0")
			    ||(row.month3sales&&row.month3sales!="0")
			 ){
			  var keys=[row.month1+1,row.month2+1,row.month3+1];
		      monthdata[row.month1+1]={amount:row.month1sales};
			  monthdata[row.month2+1]={amount:row.month2sales};
			  monthdata[row.month3+1]={amount:row.month3sales};
			  monthdata[row.month1+1].date=monthDate(row.month1);
			  monthdata[row.month2+1].date=monthDate(row.month2);
			  monthdata[row.month3+1].date=monthDate(row.month3);
			 for(var j=0;j<keys.length;j++){
				   var i=keys[j];
			 		   var amount=monthdata[i].amount;
			 			 if(amount!=""){
			 				 var start= monthdata[i].date.clone();
			 				 var end = start.clone();
			 				 end=end.setMonth(end.getMonth() +1);
			 				 end=new Date(end); 
			 				 end=end.setDate(1);
			 				 end=new Date(end); 
							 var daynum=0;
			 				 while(start<end){
								  start=start.clone();
								  start=start.setDate(start.getDate()+1);
								  start=new Date(start);
			 					 daynum++;	  
			 				 }
							monthdata[i].daynum=daynum; 
			 			 }
			 }
		
		 for(var j=0;j<keys.length;j++){
		 			 var i=keys[j];
					 var amount=monthdata[i].amount;
					 if(amount!=""){
						 var sub=amount%monthdata[i].daynum;
						 var myamount=parseInt(amount/monthdata[i].daynum);
						 var start= monthdata[i].date.clone();
						 var end = start.clone();
						 end=end.setMonth(end.getMonth() +1);
						 end=new Date(end); 
						 end=end.setDate(1);
						 end=new Date(end); 
						 while(start<end){
									  var param={};
									  param.sku=row.psku;
									  param.groupid=row.groupid;
									  param.amazonauthid=row.amazonauthid;
									  param.marketplaceid=row.marketplaceid;
									  param.quantity=myamount;
									  if(sub>0){
										   param.quantity=param.quantity+1;
										   sub=sub-1;
									  } 
									  param.date=start;
									  if(param.quantity>0){
										   preSaleList.push(param);
									  }
									  start=start.clone();
									  start=start.setDate(start.getDate()+1);
									  start=new Date(start);
						 }
					 }
			 }
			row.saveLoading=true;
			await save(preSaleList).then(res=>{
				 row.saveLoading=false;
				 ElMessage.success("保存成功");
				 if(!isbatch){
				    handleQuery()
				 }
			}).catch(error=>{
				 row.saveLoading=false;
			})
	    }
	}
	function handleSales(row){
		salechartRef.value.show(row.groupid,row.marketplaceid,row.amazonAuthId,row.psku);
	}
	onMounted(() => {
	state.dateValue= "";
	state.queryParams.fromDate = null;
	state.queryParams.toDate = null;
	});
</script>

<style scoped>
	.m-l-8{
		margin-left:8px;
	}
	.m-b-16{
		margin-bottom:16px;
	}
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
