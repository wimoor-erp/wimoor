<template>
	<el-dialog
	title="装箱分析详情"
	v-model="visiable"
    width="80%"
	top="1vh"
	>
	
			<div class="con-body" style="min-height:600px" 	v-loading="loading" >
				<el-tabs v-model="version" class="demo-tabs" @tab-change="show(planData)">
				    <el-tab-pane label="重量匹配" name="v1"></el-tab-pane>
				    <el-tab-pane label="体积匹配" name="v2"></el-tab-pane>
				    <el-tab-pane label="质量均匀" name="v3"></el-tab-pane>
				  </el-tabs>
		 
			     <table class="sd-table">
					 <thead>
						 <tr>
							 <td>SKU</td>
							 <td style="width:200px">名称</td>
							 <td>发货数量</td>
							 <td>预计箱数</td>
							 <td>每箱个数</td>
							 <td>产品重量</td>
							 <td>产品长度</td>
							 <td>产品宽度</td>
							 <td>产品高度</td>
							 <td  style="width:140px">体积(立方厘米)</td>
							 <td>箱内重量</td>
							 <td>推荐箱子尺寸</td>
						 </tr>
					 </thead>
					 <tbody v-if="boxgroup&&boxgroup.length>0" v-for="group in boxgroup">
						 <tr v-if="group.itemlist&&group.itemlist.length>0"  v-for="(row,rindex) in group.itemlist">
							 <td class="sku">{{row.sku}}</td>
							 <td><div  class="text-omit-2" style="width:200px">{{row.name}}</div></td>
							 <td>{{row.quantity}}</td>
							 <td>{{row.boxnumber}}</td>
							 <td>{{row.boxquantity}}</td>
							 <td>{{row.weight}}</td>
							 <td>{{row.length}}</td>
							 <td>{{row.width}}</td>
							 <td>{{row.height}}</td>
							 <td>{{formatFloat(row.boxquantity*row.length*row.width*row.height )}}</td>
							 <td>{{formatFloat(row.boxquantity*row.weight)}}</td>
							 <td v-if="rindex==0"  :rowspan="(group.itemlist.length+1)+''">
							 <div v-if="group.isok">{{group.length}}*{{group.width}}*{{group.height}}</div>
							 <div v-else>无法匹配箱子</div></td>
						 </tr>
						 <tr v-if="group"  >
						 							 <td colspan="9"  > </td>
						 							 <td>{{formatFloat(group.skuTotalVolumn )}}  </td>
						 							 <td>{{group.skuTotalWeight}} </td>
						 							
						 </tr>
					 </tbody>
				 </table>
			</div>
		<template #footer>
			<el-button @click="submit" v-loading="submitloading" type="primary">确认</el-button>
			<el-button @click="visiable=false" >关闭</el-button>
		</template>
		</el-dialog>
</template>

<script setup>
		import '@/assets/css/packbox_table.css';
	import { ref,reactive,onMounted,toRefs,nextTick,computed } from 'vue';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import { formatFloat,CheckInputIntLGZero,CheckInputFloat,getValue} from '@/utils/index.js';
	let state =reactive({
		visiable:false,
		planData:{},
		boxgroup:[],
		loading:false,
		version:"v1",
	})
	let{ visiable,planData,loading,boxgroup,version}=toRefs(state);
	
	 
	async function show(data){
		state.visiable=true;
		state.planData=data;
		state.loading=true;
		var params={};
		var lists=[];
		var boxlist=[];
		await shipmenthandlingApi.getBoxUse().then(res=>{
			 if(res.data && res.data.length>0){
					 boxlist=res.data;
			 }
		});
		
		state.planData.itemlist.forEach(item=>{
			var data={};
			data.sku=item.sku;
			data.name=item.name;
			data.quantity=item.confirmQuantity;
			data.length=item.pkglength;
			data.width=item.pkgwidth;
			data.height=item.pkgheight;
			data.weight=item.pkgweight;
			if(item.confirmQuantity>0){
				lists.push(data);
			}
		});
		params.itemlist=lists;
		params.boxlist=boxlist;
		params.type=state.version;
		shipmentplanApi.boxAnalysis(params).then(ress=>{
		     state.loading=false;
			 state.boxgroup=ress.data;
		});
	}
	function submit(){
		
		
		
	}
	
	 defineExpose({ show })
</script>

<style>
	.disable-row{
		  filter: grayscale(100%);
		--el-table-tr-bg-color: var(--el-color-warning-light-9);
	}
</style>