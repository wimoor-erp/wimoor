<template>
	<div class="main-sty">
		<div class="con-header">
		<el-row>
			<el-space>
		 <Group @change="groupChange"></Group>
		 <Owner @owner="getOwner"  />
		<Datepicker :shortIndex="2" @changedate="changedate" />
		<el-select v-model="queryParam.status" @change="handleQuery" placeholder="状态" clearable>
			<el-option v-for="item in statusOptions" :label="item.label" :value="item.value"></el-option>
		</el-select>
		<el-input  v-model="queryParam.search" @input="handleQuery" placeholder="请输入" class="input-with-select" >
		  <template #prepend>
		    <el-select v-model="queryParam.searchType" @change='handleQuery' style="width: 110px">
		      <el-option label="平台SKU" value="sku"></el-option>
		      <el-option label="单据号" value="number"></el-option>
		      <el-option label="货件号" value="shipmentid"></el-option>
		    </el-select>
		  </template>
		  <template #append>
		    <el-button @click="handleQuery">
		       <el-icon class="ic-cen font-medium">
		        <search />
		     </el-icon>
		    </el-button>
		  </template>
		</el-input>
<!-- 		<el-button @click="clearSearch">重置</el-button> -->
		</el-space>
		</el-row>	
		<el-row>
			<el-space>
				<el-button @click="handleEdit" type="primary">重置成本</el-button>
				<el-button @click="downloadList">导出</el-button>
				<el-button @click="showDateDialog" type="primary">修改接收日期</el-button>
				</el-space>
			<div class="rt-btn-group">
				<el-button   class='ic-btn' title='帮助文档'>
					<help theme="outline" size="16" :strokeWidth="3"/>
				</el-button>
			</div>
		</el-row>
		</div>
		<div class="con-body">
			<Table ref="tableRef"/>
		</div>
	</div>
	<EditInfo ref="editRef" @change="handleQuery" ></EditInfo>
	
	<!-- 接收日期修改 -->
	 <el-dialog v-model="dateVisible" title="接收日期修改" :destroy-on-close='true' width="560px" >
	 			 <el-form :inline="true" :model="dateForm" class="form-width-fill" label-width="auto">
	 			    <el-form-item label="选择类型"   >
	 			 	   <el-select v-model="dateForm.ftype">
						   <el-option key="normal" value="normal" label="具体日期" ></el-option>
						   <el-option key="day" value="day" label="增加天数(根据当前日期)" ></el-option>
					   </el-select>
	 			    </el-form-item>
	 			 	<el-form-item label="选择日期"  v-if="dateForm.ftype=='normal'" >
	 			 		 <el-date-picker
	 			 		         v-model="dateForm.date"
	 			 		         type="date"
	 			 		         placeholder="选择日期"
	 			 		       />
	 			 	</el-form-item>
					<el-form-item label="增加天数"  v-else >
						 <el-input type="number" v-model="dateForm.day"></el-input>
					</el-form-item>
	 			  </el-form>
	 	<template #footer>
	 		<span class="dialog-footer">
	 			<el-button @click="dateVisible = false">取消</el-button>
	 			<el-button type="primary" @click="submitDate" >确认</el-button>
	 		</span>
	 	</template>
	 </el-dialog>
</template>
<script>
	export default{ name:"批次成本" };
</script>
<script setup>
	import Datepicker from '@/components/header/datepicker.vue';
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import {Help,} from '@icon-park/vue-next';
	import Table from"./components/table.vue";
	import Group from '@/components/header/group.vue';
	import Owner from '@/components/header/owner.vue';
	import EditInfo from"./components/edit.vue"
	import {useRouter } from 'vue-router';
	import inboundItemApi from '@/api/amazon/inbound/inboundItemApi.js';
	const tableRef = ref()
	const editRef=ref();
	const router = useRouter()
	const state=reactive({
		dateVisible:false,
		dateForm:{
			ftype:'normal',
			date:null,
			day:7,
		},
		queryParam:{
			search:'',
			dateType:'1',
			status:'',
			orderType:'',
			searchType:'sku',
		},
		statusOptions:[
			{label:'全部状态',value:'',},
			{label:'未开始',value:'1',},
			{label:'扣减中',value:'2',},
			{label:'已扣完',value:'3',},
		],
		isload:true,
	})
	const {
		queryParam,dateForm,
		statusOptions,isload,dateVisible,
	}=toRefs(state)
	
	function handleEdit(){
		if(tableRef.value.state.selectRow && tableRef.value.state.selectRow.length>0){
			editRef.value.show(tableRef.value.state.selectRow);
		}else{
			ElMessage.error('请选择数据行！');
		}
		
	}
	
	function groupChange(obj){
		state.queryParam.groupid=obj.groupid;
		state.queryParam.marketplaceid=obj.marketplaceid;
		handleQuery();
	}
	//日期改变
	function changedate(dates){
		state.queryParam.fromDate=dates.start;
		state.queryParam.toDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
		 
	}
	function getOwner(ownerid){
		state.queryParam.owner=ownerid;
		if(state.isload==false){
			handleQuery();
		}
	}
	function downloadList(){
		inboundItemApi.downloadList(state.queryParam);
	}
	
	function showDateDialog(){
		if(tableRef.value.state.selectRow && tableRef.value.state.selectRow.length>0){
			state.dateVisible=true;
		}else{
			ElMessage.error('请选择数据行！');
		}
	}
	
	function submitDate(){
		var date=null;
		var days=null;
		if(state.dateForm.ftype=="normal"){
			date=state.dateForm.date;
			days=null;
		}else{
			days=parseInt(state.dateForm.day);
			date=null;
		}
		var itemlist=[];
		tableRef.value.state.selectRow.forEach(row=>{
			itemlist.push(row);
		});
		inboundItemApi.updateReceiveDate({"itemlist":itemlist ,
		"date":date,"day":days}).then((res)=>{
			ElMessage.success('操作成功！');
			handleQuery();
			state.dateVisible=false;
		});
		
		
	}
	
	
	function handleQuery(){
		tableRef.value.show(state.queryParam);
		state.isload=false;
	}
	
</script>

<style>
	.font-32{
		font-size:32px;
	}
</style>
