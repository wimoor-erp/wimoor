<template>
	<div class="con-header">
	<el-row>
		<el-space>
			<div>
				<Category @change="handleCategory"></Category>
				<Owner style="margin-left: 10px;" ref="ownerRef" @owner="getOwner" />
			</div>
			<Warehouse
			@changeware="gettoWarehouse" 
			 warehouseType="oversea_usable"
			 defaultValue="only" 
			:isform="true" />
			<Datepicker ref="datepickers"   @changedate="changedate" />
			<el-input  v-model="searchKeywords" placeholder="输入订单编码,SKU或名称" @input="handleQuery" clearable class="input-with-select" >
			   <template #append>
			     <el-button @click="handleQuery">
			        <el-icon style="font-size: 16px;align-itmes:center">
			         <search />
			      </el-icon>
			     </el-button>
			   </template>
			 </el-input>
		</el-space>
		<div class='rt-btn-group'>
			<el-button @click="handleHisShow"> 库存历史 </el-button>
			<el-button @click="handleDownload"> 导出 </el-button>
		</div>
	</el-row>
			<el-row>
				<el-space>
					<el-button type="primary" class="im-but-one" @click="handleAdd">
					  <span>添加SKU入库</span>
					</el-button>
					 <el-button @click="handleOutbound">出库</el-button> 
					 <el-button @click="handeStocktake">库存矫正</el-button> 
					 <el-button @click="handePlan">备货</el-button> 
					 <el-button @click="handleClear">清除</el-button> 
				</el-space>
				<div class='rt-btn-group'>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
				</div>
			</el-row>
 </div>
    
	<CreateOrder     ref="createOrderRef"></CreateOrder>
	<InboundCreate   ref="inboundCreateRef"   @change="handleQuery"></InboundCreate>
    <OutboundCreate  ref="outboundCreateRef"  @change="handleQuery"></OutboundCreate>
	<StocktakeCreate ref="stocktakeCreateRef" @change="handleQuery"></StocktakeCreate>
	<PlanCreate      ref="planCreateRef"      @change="handleQuery"></PlanCreate>
	<InvHisDialog    ref="invHisDialogRef"></InvHisDialog>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,UploadFilled,House} from '@element-plus/icons-vue';
    import Warehouse from '@/components/header/warehouse.vue';
	import Category  from '@/components/header/category.vue';
	import Owner from '@/components/header/owner.vue';
	import CreateOrder from './create.vue';
	import InboundCreate from '@/views/erp/warehouse/inbound/components/create_dialog.vue';
	import OutboundCreate from "@/views/erp/warehouse/outbound/components/create_dialog.vue";
	import StocktakeCreate from "@/views/erp/warehouse/stocktake/components/create_dialog.vue";
	import PlanCreate from "./plan_create_dialog.vue";
	import InvHisDialog from "./inv_his_dialog.vue";
	import orderApi from "@/api/erp/order/orderApi.js";
	const emit = defineEmits(['getdata']);
	const createOrderRef=ref();
	const inboundCreateRef=ref();
	const outboundCreateRef=ref();
	const stocktakeCreateRef=ref();
	const planCreateRef=ref();
	const invHisDialogRef=ref();
	let props = defineProps({tableData:[]});
	const {tableData} = toRefs(props);
    const state =reactive({
		inboundVisible:false,
		outboundVisible:false,
		uploadVisible:false,
		warehouse:{},
		queryParam:{
			search:'',
			auditstatus:'',
			platformid:''
		},
		myfile:null,
		skulist:{},
		searchKeywords:'',
	})
	const {
		warehouse,
		outboundVisible,
		inboundVisible,
		orderStateList,
		uploadVisible,
		queryParam,
		skulist,
		activeStatus,
		myfile,
		searchKeywords
	}=toRefs(state)
	function handleAdd(){
		var param=JSON.parse(JSON.stringify(state.queryParam));
		param.ischeck=true;
		orderApi.findMaterialBySelect(param).then(res=>{
		     inboundCreateRef.value.show(res.data,state.queryParam);
		});
	}
	function handleHisShow(){
		invHisDialogRef.value.show(state.queryParam.warehouseid);
	}
	function handleDownload(){
		orderApi.downloadOrderPlanForm(state.queryParam);
	}
	function handleOutbound(){
		state.queryParam.ftype="3";
		var param=JSON.parse(JSON.stringify(state.queryParam));
		param.ischeck=true;
		orderApi.findMaterialBySelect(param).then(res=>{
		   outboundCreateRef.value.show(res.data,state.queryParam);
		});
		
	}
	function handeStocktake(){
		var param=JSON.parse(JSON.stringify(state.queryParam));
		param.ischeck=true;
		orderApi.findMaterialBySelect(param).then(res=>{
		     stocktakeCreateRef.value.show(res.data,state.queryParam);
		});
	}
	function handePlan(){
		var param=JSON.parse(JSON.stringify(state.queryParam));
		param.ischeck=true;
		orderApi.findMaterialBySelect(param).then(res=>{
			planCreateRef.value.show(res.data,state.queryParam);
		});
		
	}
	//导入
	function upload(){
		state.uploadVisible = true;
	}
	//日期改变
	function changedate(dates){
		state.queryParam.fromDate=dates.start;
		state.queryParam.toDate=dates.end;
		handleQuery();
	}
	function gettoWarehouse(warehouseid,type,warehouse){
		state.queryParam.warehouseid=warehouseid;
		state.queryParam.warehousename=warehouse.name;
		state.warehouse=warehouse;
		handleQuery();
	}
	function handlePlatform(value){
		state.queryParam.platformid=value;
		emit('getdata',state.queryParam);
	}
	function handleQuery(){
		state.queryParam.sku=state.searchKeywords;
		emit('getdata',state.queryParam);
	}
	function handleCategory(categoryid){
		state.queryParam.categoryid=categoryid;
		emit('getdata',state.queryParam);
	}
	function getOwner(id){
		state.queryParam.myself=id;
		emit('getdata',state.queryParam);
	}
	function handleClear(){
		 orderApi.clear().then(res=>{
			 handleQuery();
		 });
	}
	
	//文件上传之前
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error(  '上传文件大小不能超过 5MB!!');
				return false;
			}
			else {
			   return true;
			}
		}
	}
	function uploadFiles(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	function uploadExcel(){
		let FormDatas = new FormData();
		FormDatas.append('file',state.myfile);
		dispatchApi.uploadExcel(FormDatas).then(function(res){
		 
		})
	}
	function downloadTemp(){
		dispatchApi.downExcelTemp();
	}
	function setRow(selectedData){
		state.tableData=selectedData;
	}

	onMounted(()=>{
			// var timer=setTimeout(function(){
			// 	handleQuery();
			// 	clearTimeout(timer);
			// },300)
	});
	
	
</script>
 
<style scoped="scoped">

	.text-orange{
		font-weight: 700;
		color:var(--el-color-primary);
		font-size: 12px;
	}
	.font-48{
		font-size: 48px;
		    color: #999;
	}
</style>
