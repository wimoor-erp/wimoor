<template>
    <div :class="inForm?'onclass':'main-sty'">
        <div class="con-header">
        <el-row>
            <el-space >
				 <Warehouse    @changeware="wareChange" defaultValue="all" />
				  <Supplier  ref="supplierRef"   @change="changeSupplier" />
				  <div class="date-picker-group" v-if="inForm===undefined ||inForm==false" >
				  	<el-select style="width:100px;" v-model="queryParams.datetype" @change="handleQuery">
				  		<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
				  	</el-select>
				  	<Datepicker  longtime="ok"
				  	ref="datepickers"
				  	 @changedate="changedate" />
				  </div>
					<el-select  v-if="inForm===undefined ||inForm==false" v-model="queryParams.paymethod" placeholder="支付方式" @change="handleQuery">
						 <el-option  v-for="item in payMethodList"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
					</el-select>
					<el-select v-model="queryParams.projectid"  @change="handleQuery" placeholder="费用类型" clearable>
						<el-option  v-for="item in finlist"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
					</el-select>
                <el-input  v-model="queryParams.search" @clear="handleQuery" placeholder="请输入" class="input-with-select" clearable>
                    <template #prepend>
                        <el-select v-model="queryParams.searchtype" @change='handleQuery'  placeholder="SKU" style="width: 110px">
                            <el-option label="SKU" value="sku"></el-option>
                            <el-option label="订单编号" value="number"></el-option>
							<el-option v-if="queryParams.settlementid" label="1688订单" value="orderid"></el-option>
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
            </el-space>
        </el-row>
		<el-row>
			<el-button @click="downLoadExcel">
			    <span>导出</span>
			</el-button>
			<div class='rt-btn-group'>
				<el-space :size="16">
				<span class="font-base"><span class="font-base-nine">付款金额总计:</span>￥{{totalamount}}</span>
			   </el-space>
				<el-button   class='ic-btn' title='帮助文档'>
			        <help theme="outline" size="16" :strokeWidth="3"/>
			    </el-button>
			</div>
		</el-row>
        </div>
        <!--表单-->
        <el-row>
			<GlobalTable ref="globalTable"
			 :tableData="tableData"  
			 height="calc(100vh - 250px)" 
			 :defaultSort="{ prop: 'opttime', order: 'descending' }"  
			 @loadTable="loadTableData" 
			 :stripe="true"  
			 style="width: 100%;margin-bottom:16px;">
				<template #field>
			    <el-table-column prop="number"  label="订单编号" width="220" sortable="custom" show-overflow-tooltip>
					<template #default="scope">
					   <div >{{scope.row.number}}  
					   <el-tag v-if="scope.row.paystatus==1" type="success">已付款</el-tag>
					   <el-tag v-if="scope.row.paystatus==0" type="info">未付款</el-tag></div>
					   <div class='font-extraSmall'>{{scope.row.wname}}</div>
					 </template>
					</el-table-column>
				<el-table-column prop="image" label="图片" width="65" >
				   <template #default="scope">
				    <el-image :src="scope.row.image"   style="width:40px;height:40px;"  ></el-image>
				  </template>
				</el-table-column>
			    <el-table-column prop="sku" label="名称/SKU"  sortable="custom" show-overflow-tooltip>
			       <template #default="scope">
			          <div class='mname'>{{scope.row.mname}}</div>
			          <div class='sku'>{{scope.row.sku}}  </div>
			      </template>
			    </el-table-column>
				 <el-table-column prop="payment_method"  width="220" label="供应商" sortable="custom" >
				 <template #default="scope">
						<div >{{scope.row.cname}}</div>
						<div class='font-extraSmall' v-if="scope.row.payment_method">付款方式:{{scope.row.payment_method}}</div>
					</template>
				 </el-table-column>
				<el-table-column prop="purchases" width="120" label="采购数量"  sortable="custom" >
					<template #default="scope">
					    <div >{{scope.row.purchases}}</div>
					    <div class='font-extraSmall' v-if="scope.row.totalin">入库:{{scope.row.totalin}}  </div>
					</template>
				</el-table-column>
				 <el-table-column prop="orderprice"  width="120" label="采购金额" sortable="custom" >
				<template #default="scope">
					    <div >{{scope.row.orderprice}}</div>
					    <div class='font-extraSmall' v-if="scope.row.totalpay">已付:{{scope.row.totalpay}}  </div>
					</template>
				</el-table-column>
			<el-table-column prop="fee_type"  width="180" label="费用类型" sortable="custom" >
			<template #default="scope">
				    <div >{{scope.row.fee_type}}</div>
					    <div class='font-extraSmall'><span >付款日期:{{dateFormat(scope.row.opttime)}}</span></div>
				</template>
			</el-table-column>
					 <el-table-column prop="orderprice"  width="150" label="付款金额" sortable="custom" >
					 <template #default="scope">
					 	    <div v-if="scope.row.payprice<0" class="text-red">{{scope.row.payprice}}:退款</div>
							<div v-else>￥{{scope.row.payprice}}</div>
					 	   <div class='font-extraSmall'>操作人:{{scope.row.name}}</div>
					 	</template>
					 </el-table-column>
				
				
				   <el-table-column prop="remark"  label="备注"  sortable="custom" />
			</template>
			</GlobalTable>
        </el-row>

    </div>
 
</template>
<script>
    export default{ name:"采购付款明细" };
</script>
<script setup>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import { ref,reactive,onMounted,toRefs} from 'vue'
    import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import listApi from '@/api/erp/purchase/form/listApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import { ElMessageBox,ElMessage } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import UploadDialog from '@/components/Upload/uploadDialog.vue';
	import {dateFormat} from '@/utils/index.js';
	import Supplier from '@/components/header/supplier.vue';
    import faccountApi from '@/api/erp/finances/faccountApi.js';
	 
	 let globalTable=ref();

	 const state = reactive({
	 		tableData:{records:[],total:0},
	 		queryParams:{
	 			searchtype:"sku",
	 			search:"",
				datetype:"paydate",
	 		},
			
			dateOptions:[{label:"付款时间",value:"paydate"}
			            ,{label:"确认收货时间",value:"recdate"}
						,{label:"账单日",value:"countdate"}],
			payMethodList:[],
			finlist:[],
			totalamount:0,
	 });
	 const { queryParams,tableData,totalamount,payMethodList,finlist,dateOptions, } = toRefs(state);
	 let props = defineProps({
	  	                      inForm:undefined,
	                        });
	 const { inForm} = toRefs(props);
	  function handleQuery(){
		  if(state.queryParams.settlementid||state.queryParams.fromDate){
	 	    globalTable.value.loadTable(state.queryParams);
		  }
	  }
	  function show(param){
		  state.queryParams.settlementid=param.settlementid;
		  state.queryParams.search=param.search;
		  state.queryParams.searchtype=param.searchtype;
		   handleQuery();
	  }
	   defineExpose({ show});
	  function loadTableData(params){
	  		  listApi.getPaymentReport(params).then(res=>{
	  				 state.tableData.records=res.data.records;
	  				 state.tableData.total=res.data.total;
					 if(params.currentpage==1){
							 if(res.data.total>0){
								  state.totalamount=res.data.records[0].totalpayprice;
							 }else{
								 state.totalamount=0;
							 }
					 }
					 
	  		  })
	  }
	 function downLoadExcel(){
	 	listApi.getPaymentReportExcel(state.queryParams) 
	 }
	 function changeSupplier(value,type){
		 state.queryParams.supplierid=value;
         if(type!="load"){
		    handleQuery();
		 }
	 }
	 function loadPaymentMethod(){
	 	faccountApi.getPaymentMethod().then((res)=>{
	 		if(res.data && res.data.length>0){
	 			res.data.push({"id":"","name":"全部支付方式"});
	 			state.payMethodList=res.data;
	 			state.queryParams.paymethod="";
	 		}else{
				state.payMethodList=[];
				state.queryParams.paymethod="";
			}
	 	});
	 }
	 function loadFacProject(){
	 	faccountApi.getProject().then((res)=>{
		    res.data.push({"id":"","name":"全部费用类型"});
	 		if(res.data && res.data.length>0){
	 			state.finlist=res.data;
	 		}else{
				state.finlist=[];
				state.queryParam.projectid="";
			}
	 	});
	 }
	 function wareChange(val){
		 state.queryParams.warehouseid=val;
		 handleQuery();
	 }
	 //日期改变
	 function changedate(datestr,timedate,type){
	 	state.queryParams.fromDate=datestr.start;
	 	state.queryParams.toDate=datestr.end;
		if(type!="load"){
	 	 handleQuery();
		}
	 }
	 
	 onMounted(()=>{
		 loadPaymentMethod();
		 loadFacProject();
	 })
	 
</script>
<style>
</style>