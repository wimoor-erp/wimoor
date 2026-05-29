<template>
	<div class="con-header">
	<el-row>
		<el-space>
		<el-select v-if="!isdialog" v-model="marketplaceid" @change='refreshTable' placeholder="全部区域">
			 <el-option v-for="item in marketList.list" :label="item.name" :value="item.marketplaceid"></el-option>
		</el-select>
		<el-select v-if="!isdialog" v-model="transType"  @change='refreshTable' placeholder="全部运输方式" >
			<el-option v-for="item in transTypeAllList.list" :label="item.name" :value="item.id"></el-option>
		</el-select>
		<el-select v-model="priceunits"  @change='refreshTable' placeholder="计价方式">
		  <el-option v-for="item in weightList.list" :label="item.label" :value="item.value"></el-option>
		</el-select>
		<el-select v-model="company"  @change='refreshTable' placeholder="物流公司" >
			<el-option v-for="item in companyList.list" :label="item.name" :value="item.id"></el-option>
		</el-select>
		<el-input  v-model="searchKeywords" @change='refreshTable' placeholder="请输入" class="input-with-select" >
		    <template #prepend>
		        <el-select v-model="selectlabel"  style="width: 110px">
		            <el-option label="渠道名称" value="1"></el-option>
		            <el-option label="备注" value="2"></el-option>
		        </el-select>
		    </template>
		    <template #append>
		        <el-button  @click='refreshTable'>
		            <el-icon style="font-size: 16px;align-itmes:center">
		                <search />
		            </el-icon>
		        </el-button>
		    </template>
		</el-input>
		</el-space>
	</el-row>
	<el-row>
	   
		<GlobalTable ref="globalTable" :tableData="tableData" height="calc(100vh - 268px)"  @loadTable="loadTableData"  @selectionChange = "selectionChange" border >
			  <template #field>
			 <el-table-column v-if="isdialog"  type="selection" width="38" />
			 <el-table-column v-if="!isdialog"  label="区域" prop="market" width="80"/>
			 <el-table-column label="分区" prop="subarea" width="80" />
			 <el-table-column label="物流公司" prop="cname" width="120"  />
			 <el-table-column label="渠道类型"  prop="channeltype" width="120"/>
			 <el-table-column v-if="!isdialog" label="运输方式"  prop="tname" width="100"/>
			 <el-table-column label="渠道名称"  prop="channame"/>
			 <el-table-column label="渠道时效(天)" width="120" prop="pretime" sortable='custom'/>
			 <el-table-column prop="priceunits"  label="计价单位"  width="100" sortable='custom' >
			   <template #default="scope">
			 	<span v-if="scope.row.priceunits=='weight'">  <el-tag class="ml-2" type="success">kg</el-tag> </span>
			 	<span v-else><el-tag class="ml-2" type="info">cbm</el-tag></span>
			   </template>
			 </el-table-column>
			 <el-table-column label="价格(元)" width="100" prop="price" sortable='custom'/>
			 <el-table-column label="材积基数(cbcm)" width="120" prop="drate"/>
			 <el-table-column label="材积基数(cbm)" width="110" prop="cbmrate"/>
			 <el-table-column label="备注" prop="remark"/>
			 <el-table-column v-if="!isdialog" label="操作人" width="120" prop="optname"/>
			 <el-table-column v-if="!isdialog" label="操作时间" width="110" prop="opttime"/>
	      </template>
	   </GlobalTable>
	</el-row>
	</div>
</template>

<script>
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import {Edit,Delete,Search} from '@element-plus/icons-vue'
	import { ref,reactive,onMounted,watch } from 'vue'
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	export default{
	    name: 'CompareList',
	    components:{ Search,GlobalTable},
	    props:["shipdata","shipform","isdialog"],
	    setup(prop,context){
			 let selectlabel =ref("1");
			 let marketplaceid=ref("");
			 let transType=ref("");
			 let priceunits=ref("");
			 let company=ref("");
			 let searchKeywords=ref("");
			 let globalTable=ref(GlobalTable);
			 let tableData=reactive({records:[],total:0})
			 let marketList=reactive({list:[]});
			 let companyList=reactive({list:[]});
			 let transTypeAllList=reactive({list:[]});
			 let weightList=reactive({list:[
				        {label:'全部计价方式',value:''},
			 			{label:'重量(kg)',value:'weight'},
			 			{label:"体积(cbm)",value:'volume'},
			 		]})
			 function loadMarketList(){
			 	marketApi.getMarketAll().then((res)=>{
			 		marketList.list = res.data;
					marketList.list.push({marketplaceid:'',name:"全部区域"})
			 	});
			 }
			 
			 function loadTransTypeAllList(){
			 	transportationApi.getTransTypeAll().then((res)=>{
			 		transTypeAllList.list=res.data;
					transTypeAllList.list.push({id:'',name:"全部运输方式"})
			 	});
			 }
			 let selectRows = ref([])
			 function selectionChange(val){
			 	selectRows.value =val
			 }
			 function getSelection(){
				 return selectRows.value;
			 }
			 function loadCompanyList(){
				 	transportationApi.getCompanyList().then(res=>{
						companyList.list=res.data;
						companyList.list.push({id:'',name:"全部物流公司"});
						refreshTable() ;
					});
			 }
			 function refreshTable(){
				 var param={'marketplaceid':marketplaceid.value,
				            'transtype':transType.value,
							'priceunits':priceunits.value,
							'company':company.value,
							'search':searchKeywords.value
							};
				 globalTable.value.loadTable(param);
			 }
			 function loadTableData(param){
				transportationApi.listitem(param).then(res=>{
					tableData.records=res.data.records;
					tableData.total=res.data.total;
				});
			 }
			
			 onMounted(()=>{
				 if(prop.shipform){
					 transType.value = prop.shipform.transtype;
					 marketplaceid.value=prop.shipdata.marketplaceid;
				 }
			    loadMarketList();
			    loadTransTypeAllList();
			    loadCompanyList();
				watch(prop.shipform,(val)=>{
						  transType.value = prop.shipform.transtype;
									  marketplaceid.value=prop.shipdata.marketplaceid;
									  refreshTable();
				})
			 });
	        //方法
	        //数据接收
	        return{ 
				selectlabel,marketplaceid,transType,priceunits,tableData,company,searchKeywords,
				marketList,transTypeAllList,weightList,companyList,
				globalTable,
				loadTableData,refreshTable,selectionChange,getSelection,
			}
	    }
	
	}
</script>

<style>
</style>