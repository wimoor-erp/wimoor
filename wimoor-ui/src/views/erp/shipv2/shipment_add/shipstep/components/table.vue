<template>
 <el-dialog
 title="商品详情"
 v-model="boxVisiable"
 >
		<div class="con-body"  >
		  <el-table :data="tableData" stripe>
			  <el-table-column label="图片" width="65"  >
				  <template #default="scope">
					  <el-image :src="scope.row.skuinfo.image" class="product-img"></el-image>
				  </template>
			  </el-table-column>
			  <el-table-column label="名称/SKU" >
				  <template #default="scope">
					 <div class='name  text-omit-1'>{{scope.row.skuinfo.pname}}</div>
					 <div class='sku'>{{scope.row.skuinfo.sku}}</div>
				  </template>
			  </el-table-column>
			  <el-table-column label="ASIN / FNSKU" >
				  <template #default="scope">
					 {{scope.row.skuinfo.asin}}
					 <div class="font-extraSmall">
						 {{scope.row.skuinfo.fnsku}}
					 </div>
				  </template>
			  </el-table-column>
			  <el-table-column label="数量" >
				  <template #default="scope">
					  {{scope.row.quantity}}
				  </template>
			  </el-table-column>
		  </el-table>
		</div>
	<template #footer>
		<el-button @click="boxVisiable=false" >关闭</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,nextTick,computed } from 'vue';
	import '@/assets/css/packbox_table.css'
	import { ElMessage,ElMessageBox } from 'element-plus';
	import { Search,ArrowDown,Close} from '@element-plus/icons-vue';
	import { useRoute,useRouter } from 'vue-router';
	import { pointKeyChnage} from '@/utils/jquery/key/point-key';
	import { formatFloat,CheckInputIntLGZero,CheckInputFloat,getValue} from '@/utils/index.js';
	import Operation from "./operation.vue";
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	const emit = defineEmits(['change']);
	const operationRef=ref();
	let router = useRouter();
	const formid=router.currentRoute.value.query.id;
	const wstepRef=ref();
	let props = defineProps({isdiv:undefined });
	const {isdiv} = toRefs(props);
	let state =reactive({
		boxVisiable:false,
		operatorInfo:{}, 
		step:0,
		title:['箱子信息提交','生成配置方案'],
		boxDisable:false,
		tableData:[],
		inputboxNum:1,
		btnloading:false,
		boxWeightData:[],
		boxListData :{list:[ {boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]}, ]},
		boxDetail:{},
		planData:{},
	})
	let{operatorInfo,boxDisable,tableData,btnloading,inputboxNum,boxWeightData,boxListData,boxVisiable,boxDetail,planData}=toRefs(state);
	
	 
	function show(planData,groupInfo){
		state.planData=planData;
		state.boxVisiable=true;
		state.tableData=groupInfo.items;
	}
	
	 defineExpose({ show })
</script>

<style scoped="scoped">
	.box-ship .el-form-item--small .el-form-item__content{
		line-height: 20px;
	}
	.font-extraSmall{
		font-weight: 400;
	}
	.box-ship .con-header{
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top:16px;
		margin-bottom:16px;
	}
	.box-ship .el-input-number--small{
		width: 100%;
	}
	.sd-table{
		margin-bottom:16px;
	}
	.box-size{
		display: flex;
		align-items: center;
		justify-content: flex-end;
	}
	.box-ship .el-input-number{width:70px !important;}
	.box-size span{
		margin-left: 8px;
		margin-right:8px;
	}
	.icon-c{
		font-size: 16px;
		margin-right:8px;
		line-height: 0px;
	}
	.box-ship .el-radio-group{
		line-height:20px;
	}
</style>