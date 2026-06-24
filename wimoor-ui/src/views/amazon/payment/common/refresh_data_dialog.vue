<template>
	<el-dialog
	title="重算"
	v-model="dialog.visable"
	width="600px"
	>
	<el-form >
		<el-form-item label="店铺国家">
			<Group ref="groups" @change="getData"  />
		</el-form-item>
		<el-form-item label="重算区间">
			
			<div v-if="needdeep" class="date-picker-group">
				<el-select v-model="queryParam.ftype" placeholder="结算日期" style="width:100px"  >
					<el-option v-for="item in dateOptions"  :label="item.label" :value="item.value"></el-option>
				</el-select>
				<Datepicker   ref="datepickers" @changedate="changedate" />
			</div>
			<Datepicker v-else ref="datepickers"  @changedate="changedate" />
		</el-form-item>
		<el-form-item label="深层计算">
			 <el-checkbox v-if="needdeep" v-model="needdeep"   label="深层计算" disabled="true" size="large" /> 
			 <el-checkbox v-else v-model="isdeep" label="深层计算" size="large" /> 
			 <div class="font-extraSmall" style="padding-left:20px;">深层计算：计算本地成本，报表重新汇总。 基础计算：报表数据重新计算</div>
		</el-form-item>
		
		
	</el-form>
	<template #footer>
		<el-button @click="dialog.visable=false">取消</el-button>
		<el-button type="primary" :loading="loading" @click="getRefreshData">确定</el-button>
		
		<div style="padding-top:10px;">
	       <Progress ref="progressRef" :options="progressOptions"></Progress>
		</div>
	</template>
	</el-dialog>
</template>


<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import Progress from '@/components/progress/index.vue';
	import Group from '@/components/header/group.vue';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import settlementMonthSummaryApi from '@/api/amazon/finances/settlementMonthSummaryApi.js'
    import '@/assets/css/packbox_table.css';
	import {dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	const globalTable=ref();
	const ownerRef=ref();
	const accDialogRef=ref();
	const calcDialogRef=ref();
	const progressRef=ref();
	const emit = defineEmits(['change']);
	const  state=reactive({dialog:{visable:false} ,
	                       queryParam:{ftype:""},
						   isdeep:false,
						   key:"",
						   progressOptions:{key:"settlement_refresh_data",progress:-1} ,
						   loading:false,
						   dateOptions:[{label:'结算日期',value:''},{label:'转账日期',value:'acc'}],})
	const{ dialog,queryParam,progressOptions,isdeep,loading,dateOptions}=toRefs(state)
	let props = defineProps({   needdeep:"" });
	const {   needdeep} = toRefs(props);
	function getData(obj){
		state.queryParam.groupid=obj.groupid;
		state.queryParam.marketplaceid=obj.marketplaceid;
	}
	//日期改变
	function changedate(dates){
		state.queryParam.fromDate=dates.start;
		state.queryParam.endDate=dates.end;
	}
	function getRefreshData(){
		if(state.isdeep){
			state.queryParam.isdeep="true";
		}else{
			state.queryParam.isdeep="false";
		}
		if(props.needdeep){
			state.queryParam.isdeep="true";
		}
		state.loading=true;
		state.progressOptions.progress=0;
		settlementMonthSummaryApi.getRefreshData(state.queryParam).then(res=>{
			ElMessage.success('操作成功！');
			state.progressOptions.key=res.data;
			state.loading=false;
			progressRef.value.show();
			emit("change");
		}).catch(error=>{
		 
		}); 
	}
		
	function show(){
		state.dialog.visable=true;
		setTimeout(function(){
			state.progressOptions.progress=0;
			progressRef.value.show();
		},100); 
		
	}
	defineExpose({ show})
</script>

<style scoped >
	 .img80{
		 width:80px;
		 length:80px;
	 }
	 .width60{
		 width:60px;
	 }
	 .width50{
	 		 width:50px;
	 }
	 .pull-left{
		 float:left;
	 }
</style>
 