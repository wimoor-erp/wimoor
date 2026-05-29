<template>
	<div class="main-sty">
		<div class="con-header">
		<el-row>
			<el-space>
		<Group ref="groupRef" @change="groupChange" defaultValue="only"  />
		<!-- <Currency ref="currencyRef" @change="getData" /> -->
		<el-select v-model="queryParams.itemid"   filterable  placeholder="请选择其它费用项" @change="handleQuery">
			<el-option
			   v-for="item in finItemList"
			  :key="item.id"
			  :label="item.name"
			  :value="item.id"
			> </el-option>
		  </el-select>	
		<Datepicker ref="datepickersRef" @changedate="changedate" />
		<el-input  v-model="queryParams.search" @input="handleQuery" placeholder="请输入" class="input-with-select" >
		  <template #prepend>
		    <el-select v-model="queryParams.searchtype" @change='handleQuery' style="width: 110px">
		     <!-- <el-option label="单据号" value="number"></el-option> -->
		      <el-option label="SKU" value="sku"></el-option>
		     <!-- <el-option label="创建人" value="owner"></el-option> -->
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
		<el-button @click="clearSearch">重置</el-button>
		</el-space>
		</el-row>	
		<el-row>
			<el-space>
				<el-button type="primary" @click="handleAdd">添加费用</el-button>
				<el-button @click="FeeTypeEdit">费用类型管理</el-button>
			<!-- 	<el-button  @click="shareRules">分摊规则</el-button> -->
				<el-button @click="openUpload">导入分摊费</el-button>
				 <el-dropdown>
				      <el-button type="primary">
				        导出<el-icon class="el-icon--right"><arrow-down /></el-icon>
				      </el-button>
				      <template #dropdown>
				        <el-dropdown-menu>
				          <el-dropdown-item @click="downloadExcel('normal')">每天详细</el-dropdown-item>
				          <el-dropdown-item @click="downloadExcel('month')">按月汇总</el-dropdown-item>
				        </el-dropdown-menu>
				      </template>
				    </el-dropdown>
			</el-space>
			<div class="rt-btn-group">
				<el-button   class='ic-btn' title='帮助文档'>
					<help theme="outline" size="16" :strokeWidth="3"/>
				</el-button>
			</div>
		</el-row>
		</div>
		<div class="con-body">
			<Table ref="tableRef" />
		</div>
	</div>
	<OtherFee ref="otherFeeRef"/>
	<ShareRules ref="ShareRulesRef"/>
	<Listings ref="ListingsRef" />
	    <UploadDialog ref="uploadDialogRef" @upload="handleUpload"></UploadDialog>
	  <CostDialog ref="costRef" @change="handleQuery"></CostDialog>
</template>
<script>
	export default{ name:"其它费用" };
</script>
<script setup>
	import Group from '@/components/header/group.vue';
	import Currency from '@/components/header/currency.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import { ref,reactive,onMounted,toRefs} from 'vue';
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue';
	import {Help,} from '@icon-park/vue-next';
	import Table from"./components/table.vue";
	import OtherFee from"./components/otherFee.vue";
	import ShareRules from"./components/shareRules.vue";
	import Listings from"@/views/amazon/listing/common/listings.vue";
	import CostDialog from"./cost_dialog.vue";
	import {useRouter } from 'vue-router';
	import UploadDialog from '@/components/Upload/uploadDialog.vue';
	import financesItemApi from '@/api/amazon/finances/financesItemApi.js';
	import financesItemDataApi from '@/api/amazon/finances/financesItemDataApi.js';

	const router = useRouter();
	let uploadDialogRef=ref();
	const currencyRef=ref();
	const otherFeeRef = ref();
	const ShareRulesRef = ref();
	const ListingsRef = ref();
	const tableRef=ref();
	const groupRef=ref();
	const datepickersRef=ref();
	const costRef=ref();
	const state=reactive({
		selectlabel:'code',
		searchKeywords:'',
		uploadVisible:false,
		queryParams:{
			search:'',
			itemid:'',
			searchtype:'sku'
		},
		isload:true,
		finItemList:[],
	})
	const {
		selectlabel,
		searchKeywords,
		uploadVisible,
		queryParams,
		isload,
		finItemList,
	}=toRefs(state)
	function openUpload(){
		uploadDialogRef.value.show({'templateCallback':function(){
			financesItemDataApi.downFinItemDataTemp();
		},'title':"产品其它费用导入"});
	}
	function handleUpload(formDatas){
		financesItemDataApi.uploadFile(formDatas).then(res=>{
			ElMessage.success("上传成功！");
			handleQuery();
			uploadDialogRef.value.hide();
		});
	}
	function clearSearch(){
		currencyRef.value.reset()
	}

	function handleAdd(){
		 costRef.value.show();
	}

	function FeeTypeEdit(){
		otherFeeRef.value.show()
	}
	function shareRules(){
		ShareRulesRef.value.show()
	}

	function upload(){
		state.uploadVisible = true
	}
	//日期改变
	function changedate(dates){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
	}
	//店铺站点改变
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
	    state.queryParams.marketplaceid=obj.marketplaceid;
		handleQuery();
	}
	function handleQuery(){
		state.queryParams.sku=state.queryParams.search;
		tableRef.value.show(state.queryParams);
		state.isload=false;
	}
	function loadFinListWithoutPage(){
		financesItemApi.getFinListWithoutPage().then((res)=>{
			if(res.data && res.data.length>0){
				res.data.push({"id":"",'name':"全部费用项"});
			}
			state.finItemList=res.data;
		});
	}
	function downloadExcel(ftype){
		state.queryParams.ftype=ftype;
		financesItemDataApi.downOtherFee(state.queryParams);
	}
	function downloadTemp(){
		financesItemDataApi.downFinItemDataTemp();
	}
	onMounted(()=>{
		loadFinListWithoutPage();
	})
</script>

<style>
	.font-32{
		font-size:32px;
	}
</style>