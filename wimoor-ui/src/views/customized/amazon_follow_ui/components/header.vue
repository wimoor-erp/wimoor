<template>
	<el-tabs v-model="queryParams.activeName"   @tab-change="handleQuery">
	   <el-tab-pane label="全部" name="1"></el-tab-pane>
	   <el-tab-pane label="" name="3">
	   		   <template #label>
	   			  <span class="font-bold">在线</span> 
	   		   </template>
	   </el-tab-pane>
	   <el-tab-pane label="未发布" name="2"></el-tab-pane>
	   
	   <el-tab-pane label="待上架" name="4"></el-tab-pane>
	   <el-tab-pane label="上架中" name="11"></el-tab-pane>
	   <el-tab-pane label="下架中" name="12"></el-tab-pane>
	   <el-tab-pane label="发布中" name="13"></el-tab-pane>
	   <el-tab-pane label="调价中" name="15"></el-tab-pane>
	   <el-tab-pane label="删除中" name="14"></el-tab-pane>
	   <el-tab-pane label="已删除" name="7"> </el-tab-pane>
	   <el-tab-pane label="发布失败" name="9"></el-tab-pane>
	   <el-tab-pane label="下架失败" name="10"></el-tab-pane>
	   <el-tab-pane label="调价失败" name="16"></el-tab-pane>
	   <el-tab-pane label="删除失败" name="8"></el-tab-pane>
	 </el-tabs>
	<div class="pag-radius-bor">
		<el-form v-model="form">
			<el-row :gutter="32">
				<el-col :span="7">
			<el-form-item label="产品查询">
				<el-input v-model="queryParams.search" clearable  @input="handleQuery" placeholder="ASIN、SKU、标题、品牌"></el-input>
			</el-form-item>
			</el-col>
			<el-col :span="6">
			<el-form-item label="创建时间">
				<el-date-picker
				        v-model="queryParams.createtime"
				        type="daterange"
				        range-separator="-"
				        start-placeholder="开始日期"
				        end-placeholder="结束日期"
				        :size="size"
				      />
			</el-form-item>
			</el-col>
			<el-col :span="6">
			<el-form-item label="跟卖时间">
				<el-space class="flex-center">
				<el-select v-model="queryParams.followDateId" @change="handleQuery" style="width:100%;">
					<el-option v-for="item in timelist" :key="item.id" :label="item.fullname"   :value="item.id">
					</el-option>
				</el-select>
				<el-button @click="handleSettingTime">
					<el-icon><Setting /></el-icon>
				</el-button>
				</el-space>
			</el-form-item>
			</el-col>
			<el-col :span="5">
			<el-form-item label="价　格">
				<el-space size='8'>
				<el-input type="number" v-model="queryParams.lowprice" placeholder="起"></el-input>
				<span class="font-extraSmall">至</span>
				<el-input type="number" v-model="queryParams.maxprice"  placeholder="止"></el-input>
				</el-space>
			</el-form-item>
			</el-col>
			</el-row>
			<el-row :gutter="32">
				<el-col :span="7">
			<el-row :gutter="32">
				<el-col :span="24">
			<el-form-item label="店　　铺">
				<el-select style="width:100%;" v-model="queryParams.groupid" @change="handleQuery">
					<el-option v-for="item in grouplist" :key="item.value"  :label="item.label" :value="item.value"></el-option>
				</el-select>
			</el-form-item>
			</el-col>
			<!-- <el-col :span="12">
			<el-form-item label="国家">
				<el-select></el-select>
			</el-form-item>
			</el-col> -->
			</el-row>
			</el-col>
			<el-col :span="6">
				<el-form-item label="出单时间">
					<el-date-picker
					        v-model="queryParams.ordertime"
					        type="daterange"
					        range-separator="-"
					        start-placeholder="开始日期"
					        end-placeholder="结束日期"
					        :size="size"
					      />
				</el-form-item>
			</el-col>
			<el-col :span="6">
			<el-form-item label="业务员　">
				<OwnerAll @owner="changeOwner" ref="ownerRef"  ></OwnerAll>
			</el-form-item>
			</el-col>
			<el-col :span="5">
			<el-form-item label="出单量">
				<el-space size='8'>
				<el-input type="number" v-model="queryParams.loworders" placeholder="起"></el-input>
				<span class="font-extraSmall">至</span>
				<el-input type="number" v-model="queryParams.maxorders" placeholder="止"></el-input>
				</el-space>
			</el-form-item>
			</el-col>
			</el-row>
			<el-row :gutter="32" class="el-no-ma">
				<el-col :span="7">
               <el-form-item label="承受价　">
				   <el-radio-group v-model="queryParams.isoverprice" >
				         <el-radio label="" >全部</el-radio>
				         <el-radio label="1" >达到承受价</el-radio>
				         <el-radio label="0" >未设置调价</el-radio>
				    </el-radio-group>
               </el-form-item>    
			   </el-col>
			   <el-col :span="6">
			   <el-form-item label="购物车所属">
			   				   <el-radio-group v-model="queryParams.isshopowner" >
			   				         <el-radio label="" >全部</el-radio>
			   				         <el-radio label="1" >自己</el-radio>
			   				         <el-radio label="0" >他人</el-radio>
			   				    </el-radio-group>
			   </el-form-item>    
			   </el-col>
			   <el-col :span="6">
			   <el-form-item label="购物车资格">
			   				   <el-radio-group v-model="queryParams.isshopcart" >
								     <el-radio label="" >全部</el-radio>
			   				         <el-radio label="1">有</el-radio>
			   				         <el-radio label="0" >无</el-radio>
			   				    </el-radio-group>
			   </el-form-item>    
			   </el-col>
			   <el-col :span="5">
			   <el-form-item >
				   <el-button type="primary" @click="handleQuery">查询</el-button>
				   <el-button type="" @click="clearQuery">重置</el-button>
			   </el-form-item>    
			   </el-col>
			</el-row>
		</el-form>
	</div>
	<el-row class="m-t-b-16">
		<el-space size="8">
		<el-button type="primary" @click="handleAdd">
			<plus theme="outline" size="18" fill="#fff" :strokeWidth="3"/>
			添加跟卖</el-button>
		
		
		<el-button 
		v-if="
		queryParams.activeName == 1 || 
		queryParams.activeName == 2 || 
		queryParams.activeName == 88"
		type="success"
		@click="pushItems('upload')"
		:loading="btnloading.upload"
		title="新添加跟卖的ASIN从此上架">
		批量发布
		</el-button>
		
		<el-button
		v-if="
		queryParams.activeName == 7 || 
		queryParams.activeName == 9"
		type="success"
		@click="pushItems('upload')"
		:loading="btnloading.upload"
		title="新添加跟卖的ASIN从此上架">
		重新发布
		</el-button>
		
		
		<el-button
		v-if="
		queryParams.activeName == 1 || 
		queryParams.activeName == 4 || 
		queryParams.activeName == 88"
		type="success"
		@click="pushItems('sales')"
		:loading="btnloading.sales"
		title="将商品立即设置在售状态,请勿在任务执行的前后十分钟进行此操作">
		批量上架
		</el-button>
		
		
		
		<el-button
		v-if="
		queryParams.activeName == 1 || 
		queryParams.activeName == 10 || 
		queryParams.activeName == 3"
		type="danger"
		@click="pushItems('unsales')"
		:loading="btnloading.unsales"
		title="将商品立即设置缺货状态,请勿在任务执行的前后十分钟进行此操作">
		批量下架
		</el-button>
		
		<el-button 
		v-if="
		queryParams.activeName == 16 || 
		queryParams.activeName == 3"
		title="ASIN将以最大价格重新抢购物车" 
		@click="pushItems('price')">
		重新调价
		</el-button>
		
		
		<el-button @click="syncProductInfo()" :loading="productloading" title="从商品分析内同步">同步产品</el-button>
		
		</el-space>
		
		<el-dropdown trigger="click">
			
		  <el-button>
		           批量操作<el-icon ><arrow-down /></el-icon>
		  </el-button>
		  
		  <template #dropdown>
		    <el-dropdown-menu >
		      <el-dropdown-item @click="deleteItems" >删除产品</el-dropdown-item>
		      <el-dropdown-item  @click="editStockAndPrice" >备货&价格</el-dropdown-item>
		      <el-dropdown-item  @click="editStockCycle">全部备货周期</el-dropdown-item>
		      <el-dropdown-item  @click="showTimDialog">跟卖时间</el-dropdown-item>
		    </el-dropdown-menu>
		  </template>
		  
		</el-dropdown>
		
		<el-tag style="font-size: 20px margin-left: 50px">步骤：1.添加跟卖ASIN → 2.未发布-批量发布 → 3.待上架-批量上架 👉完结👈 </el-tag>
		
		
		<div class='rt-btn-group'>
			<el-space size="8">
			<el-button @click="handleBatchAdd">
				<el-icon><Plus /></el-icon>
				导入
			</el-button>
			<el-button @click="showDownload">
				<el-icon><Download /></el-icon>
			    导出
			</el-button>
			
			
			
			<el-button @click="handleWarning">警告 ({{warningnumber}})</el-button>
			</el-space>
		</div>
	</el-row>
	<FollowTime ref="followTimeRef"/>
	<FollowAdd @change="handleQuery" ref="addFollowRef"/>
	<BatchFollow ref="batchFollowRef"/>
	<Warning ref="warningRef" @change="loadWarningNumber" /> 
	<DownloadItem @change="downloadList" ref="downloadItemRef"/>
</template>

<script setup>
	import {Plus,} from '@icon-park/vue-next';
	import {ref,reactive,toRefs,onMounted,defineProps}from"vue";
	import {ArrowDown,Download,Setting} from '@element-plus/icons-vue'
	import FollowTime from './followTime.vue'
	import BatchFollow from './batchFollow.vue'
	import FollowAdd from './followAdd.vue'
	import Warning from './warning.vue'
	import DownloadItem from './downloadItem.vue'
	import OwnerAll from '@/components/header/ownerAll.vue';
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import followTimeApi from '@/views/customized/amazon_follow_ui/js/followTimeApi.js';
	import listingApi from '@/api/amazon/listing/listingApi.js';
	import authApi from '@/api/amazon/auth/authApi.js';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	const emit = defineEmits(['change','deletes','pushs','times','prices','cycles']);
	const followTimeRef=ref()
	const addFollowRef=ref()
	const batchFollowRef=ref()
	const warningRef=ref()
	const uploadLogRef=ref()
	const downloadItemRef=ref()
	const ownerRef=ref();
    const state=reactive({
		form:{
			car:'1',
			price:'1',
			getcar:'1',
		},
		timelist:[],
		warningnumber:0,
		productloading:false,
		grouplist:[],
		grouplistWithAll:[],
		queryParams:{
			activeName:"3",
			createtime:null,
			ordertime:null,
			groupid:"",
			sort:"createtime",
			order:"desc",
			followDateId:"",
			search:"",
			isoverprice:"",
			isshopowner:"",
			isshopcart:"",
			loworders:null,
			maxorders:null,
			lowprice:null,
			maxprice:null,
		}
	})
	const {
		form,
		timelist,
		grouplist,
		productloading,
		grouplistWithAll,
		warningnumber,
		queryParams,
	}=toRefs(state)
	let props = defineProps({ btnloading:{upload:false,sales:false,unsales:false,delete:false} });
	const { btnloading } = toRefs(props);
	function showTimDialog(){
		emit("times");
	}
	function handleSettingTime(){
		followTimeRef.value.show()
	}
	function handleAdd(){
		addFollowRef.value.show()
	}
	function handleBatchAdd(){
		batchFollowRef.value.show();
	}
	function loadWarningNumber(){
	followListApi.findWarningListNum().then(res=>{
		state.warningnumber=res.data;
	})
	}
	function handleWarning(){
		warningRef.value.show(state.grouplistWithAll)
	}
	function uploadLog(){
		uploadLogRef.value.show()
	}
	function editStockCycle(){
		emit("cycles");
	} 
	function editStockAndPrice(){
		emit("prices");
	}
	function changeOwner(id){
		 state.queryParams.ownerid=id;
	}
	function load(){
		followTimeApi.list().then((res)=>{
			if(res.data){
				res.data.forEach(item=>{
					item.fullname=item.name+"("+item.starttime.substring(0,5);
					if(item.endtime){
						item.fullname=item.fullname+"-"+item.endtime.substring(0,5);
					}else{
						item.fullname=item.fullname+"-全天";
					}
					item.fullname=item.fullname+")";
				});
				res.data.push({"fullname":"全部","id":""});
			}
			state.timelist=res.data;
		});
		authApi.getAuthMaketplace().then((res)=>{
		   state.grouplistWithAll=[];
		   state.grouplist=[];
			if(res.data){
				state.grouplistWithAll=res.data;
				res.data.push({"label":"全部","value":""});
				state.grouplist=res.data;
				if(res.data.length>0){
			     	state.queryParams.groupid=res.data[0].value;
				}
			}
			
		});
		handleQuery();
	}
	function handleQuery(){
		if(state.queryParams.groupid.indexOf("-")>=0){
			state.queryParams.amazonauthid=state.queryParams.groupid.split("-")[1];
		}
		if(state.queryParams.createtime){
			state.queryParams.fromDate=state.queryParams.createtime[0].format("yyyy-MM-dd");
			state.queryParams.toDate=state.queryParams.createtime[1].format("yyyy-MM-dd")+" 23:59:59";
		}
		if(state.queryParams.ordertime){
			state.queryParams.startDate=state.queryParams.ordertime[0].format("yyyy-MM-dd");
			state.queryParams.endDate=state.queryParams.ordertime[1].format("yyyy-MM-dd")+" 23:59:59";
		}
		var status=state.queryParams.activeName;
		state.queryParams.status=status;
		emit("change",state.queryParams)
		
		
		
		
		
	}
	function deleteItems(){
		emit("deletes");
	}
	function pushItems(ftype){
		emit("pushs",ftype);
	}
	function showDownload(){
		downloadItemRef.value.show();
	}
	function downloadList(groupid){
		var marketplaceid=groupid.split("-")[0];
		var amazonauthid=groupid.split("-")[1];
		followListApi.downloadDetailExport({"authid":amazonauthid,"marketplaceid":marketplaceid});
	}
	function syncProductInfo(){
		state.productloading=true;
		followListApi.syncProductInfo().then(res=>{
			state.productloading=false;
			 ElMessage({
			 		type: 'success',
			 		message: '操作成功'
			 });
		}).catch(error=>{
			
		})
	}
	function clearQuery(){
		state.queryParams={
			activeName:"1",
			createtime:null,
			ordertime:null,
			groupid:"",
			followDateId:"",
			search:"",
			isoverprice:"",
			isshopowner:"",
			isshopcart:"",
			loworders:null,
			maxorders:null,
			lowprice:null,
			maxprice:null,
		};
		state.queryParams.ownerid="";
		ownerRef.value.reset();
		handleQuery();
	}
	onMounted(() => {
		 load();
		 loadWarningNumber();
	});



</script>

<style>

	.el-no-ma .el-form-item--default{
		margin-bottom:0px;
	}
	.m-t-b-16{
		margin-top:16px;
		margin-bottom:16px;
	}
</style>