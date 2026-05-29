<template>
	<div class="con-header" >
	<el-row>
	 <el-space>
	 <Group @change="getData" defaultValue="all" />
	 <Warehouse @changeware="getWarehouse" />
	 <Datepicker @changedate="changedate"  :shortIndex="1" />
	   <el-input  v-model="searchKeywords" placeholder="请输入" @input="searchConfirm" clearable class="input-with-select" >
	      <template #prepend>
	        <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="计划名称" value="plan"></el-option>
	          <el-option label="计划编码" value="number"></el-option>
			  <el-option label="备注" value="remark"></el-option>
	        </el-select>
	      </template>
	      <template #append>
	        <el-button @click="searchConfirm">
	           <el-icon style="font-size: 16px;align-itmes:center">
	            <search />
	         </el-icon>
	        </el-button>
	      </template>
	    </el-input>
	    <el-button>重置</el-button>
			
	  </el-space>
	  </el-row>
	   <!--功能区域 -->
	  <el-row>
	   <el-space >
	    <el-button type="primary" class="im-but-one" @click="addShipment">
	      <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
	      <span>添加新版货件</span>
	    </el-button>
		<el-button type="primary"  @click="asyncShipment">
		     同步计划
		</el-button>
		 <el-dropdown  v-if="printButtonShow">
		   <el-button type="primary">
			 打印配货单<el-icon class="el-icon--right"><arrow-down /></el-icon>
		   </el-button>
		   <template #dropdown>
			 <el-dropdown-menu>
			   <el-dropdown-item @click="printLabel('simple')">简单版</el-dropdown-item>
			   <el-dropdown-item @click="printLabel('detail')">详细版</el-dropdown-item>
			 </el-dropdown-menu>
		   </template>
		 </el-dropdown>
	 <el-button @click="handleShelf" v-if="printButtonShow">批量下架</el-button>
	   </el-space>
	   <div class='rt-btn-group'>
		   <el-space>
		   <el-button class='ic-btn'  title='列配置'>
			  <setting-two theme="outline" size="16"  :strokeWidth="3"/>
		   </el-button>
			<el-button   class='ic-btn' title='帮助文档'>
			 <help theme="outline" size="16" :strokeWidth="3"/>
		   </el-button>
		   </el-space>
	   </div>
	</el-row>
	</div>
	<AsyncPlan ref="asyncPlanRef"></AsyncPlan>
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { useRoute,useRouter } from 'vue-router'
	import { ref,reactive,onMounted,watch ,inject} from 'vue'
	import groupApi from '@/api/amazon/group/groupApi.js';
	import shipmentQuotaApi from '@/api/erp/shipv2/shipmentQuotaApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import Group from '@/components/header/group.vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import AsyncPlan from './async.vue';
	const emits = defineEmits(["getdata"]);
	const emitter = inject("emitter");
	let router = useRouter()
	let queryParam={}
	let selectlabel=ref('sku')
	let searchKeywords=ref("")
	let moreSearchVisible=ref(false)
	let color=ref("");
	let checkdown=ref("");
	let printButtonShow=ref(false);
	const asyncPlanRef =ref();
	let isload=true;
	function addShipment(){
		emitter.emit("removeCache", "添加新版货件");
		router.push({
			path:'/invoice/new/addshipment',
			query:{
			  title:"添加新版货件",
			  path:'/invoice/new/addshipment',
			}
		})
	}
	var selects=[];
	var selectrows=[];
	function handleTableSelectChange(rows){
		selects=[];
		selectrows=rows;
		if(rows&&rows.length>0){
			rows.forEach(item=>{
				selects.push(item.id);
			})
		}
		if(rows&&rows.length>0){
			printButtonShow.value=true;
		}else{
			printButtonShow.value=false;
		}
	}
	function handleCheckDown(value){
		queryParam.checkdown=value;
		emits("getdata",queryParam)
	}
	function getData(obj){
		queryParam.groupid=obj.groupid;
		queryParam.marketplaceid=obj.marketplaceid;
		emits("getdata",queryParam)
		isload=false;
	}
	function getWarehouse(wid){
		queryParam.warehouseid=wid;
		if(isload==false){
			emits("getdata",queryParam)
		}
	}
	function changedate(dates){
		queryParam.start=dates.start;
		queryParam.end=dates.end;
		if(isload==false){
			emits("getdata",queryParam)
		}
	}
	function searchTypeChange(){
		queryParam.seachtype=selectlabel.value;
		if(isload==false){
			emits("getdata",queryParam)
		}
	}
	function searchConfirm(){
		queryParam.searchwords=searchKeywords.value;
		if(queryParam.searchwords&&queryParam.searchwords.indexOf("SF")==0&&queryParam.searchwords.length==14){
			selectlabel.value="number";
			queryParam.seachtype=selectlabel.value;
		}
		if(isload==false){
			emits("getdata",queryParam)
		}
	}
	function handleShelf(){
		router.push({
			path:'/erp/ship/quota',
			query:{
			  planids:selects,
			  title:"配货单下架",
			  path:'/erp/ship/quota',
			}
		})
	}
	function asyncShipment(){
		asyncPlanRef.value.show();
	}
	function submitForm(){
		queryParam.color=color.value;
		if(isload==false){
			emits("getdata",queryParam)
		}
		moreSearchVisible.value=false;
	}
	function resetForm(){
		moreSearchVisible.value=false;
	}
	function printLabel(type){
		//下载pdf
		shipmentQuotaApi.downPDFShipForm(type, selects,'planLabel');
	}
	defineExpose({ handleTableSelectChange })

 
</script>

<style>
</style>
