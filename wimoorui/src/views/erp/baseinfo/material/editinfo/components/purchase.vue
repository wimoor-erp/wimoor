<template>
	<h3 id="purchase" class="tab-scroll">采购信息</h3>
	<!-- <el-form-item label="采购员">
		<el-select class="in-wi-24" v-model="dataForms.buyer" placeholder="采购员">
				<el-option v-for="(item,index) in buyerList" :label="item.name" :value="item.id" />
		</el-select>
	</el-form-item> -->
	<el-row>
		<el-col :span="8">
			<el-form-item label="采购成本">
				<el-input v-model="dataBaseForms.price" type="text" 
				          @input="dataBaseForms.price=CheckInputFloat(dataBaseForms.price)" 
				          placeholder="请输入采购成本">
				 <template #append v-if="forms.assemblyforms&&forms.assemblyforms.list&&forms.assemblyforms.list.length>0">
					<el-tooltip content="组装列表采购成本汇总">
						<el-icon @click="handleRefreshPrice(forms.assemblyforms.list,dataBaseForms)" class="pointer">
							<el-button :icon="RefreshLeft"></el-button>
						</el-icon>
					</el-tooltip>
				 </template>
				</el-input>
			</el-form-item>
		</el-col>
		<el-col :span="8">
			<el-form-item label="其它成本">
				<el-input v-model="dataBaseForms.otherCost"  @input="dataBaseForms.otherCost=CheckInputFloat(dataBaseForms.otherCost)"  type="text"  ></el-input>
			</el-form-item>
		</el-col>
		<el-col :span="8">
			<el-form-item label="退税率" v-if="type=='product'"  width="100">
				<el-input v-model="dataBaseForms.vatrate" @input="dataBaseForms.vatrate=CheckInputFloat(dataBaseForms.vatrate)"  placeholder="为空不含税">
					  <template #append>%</template>
				</el-input>
			</el-form-item>
		</el-col>
	</el-row>
	 
	<el-form-item label="供应商" class="grid-row">
		<el-table border :data="dataForms"  >
			<el-table-column width="50" type="index">
						 <template #header >
							   <el-link :underline="false" type="primary" @click="addSupplier">
							   <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
							   </el-link>
						 </template>
			</el-table-column>
			<el-table-column label="供应商" >
				<template #default="scope">
					<div class="inlinePurchaseForm">
					<el-form label-width="70px">
						<el-form-item label="供应商">
							<el-space>
								<el-input  readonly @click="selectSupplier(scope.row)" v-model="scope.row.name" placeholder="选择供应商" ></el-input>
							    <el-switch
							        v-model="scope.row.isdefault"
							    	@change="changeIsdefault(scope.row)"
							        inline-prompt
									 width="50px"
							    	:disabled="scope.row.isdefault"
							        active-text="默认"
							        inactive-text="备选"
							      />
								 <el-button   type="info"  link  @click="jumpsupplier">
										<el-icon class="ic-cen ">
											<Edit />
										</el-icon>
							 	 </el-button>
								 </el-space>
						</el-form-item>
						 
						<el-form-item label="供货周期">
						<el-input  v-model.number="scope.row.deliverycycle"  @input="scope.row.deliverycycle=CheckInputInt(scope.row.deliverycycle)" placeholder="默认为0">
							  <template #append>天</template>
						</el-input>
						</el-form-item>
						<el-form-item label="采购链接">
							<el-input   style="margin-top: 3px;" v-model="scope.row.purchaseUrl"   type="textarea" :rows="2" placeholder="采购链接"></el-input>
						</el-form-item>
					</el-form>
					</div>
				</template>
			</el-table-column>
			 
			<el-table-column label="阶梯价" width="350" >
				<template #default="scope">
					<div>
					<el-table border :data="scope.row.stepList" >
						<el-table-column label="采购量">
							<template #default="scope">
								<el-space>
									<el-input v-model="scope.row.amount" @input="scope.row.amount=CheckInputInt(scope.row.amount)" placeholder="起订量" ></el-input>
								</el-space>
							</template>	
						</el-table-column>
						<el-table-column label="采购单价">
							<template #default="scopesub">
								<el-input v-if="0==scopesub.$index"  
								v-model="scopesub.row.price" 
								@input="scopesub.row.price=CheckInputFloat(scopesub.row.price)"
								@change="checkPriceInputFloat(dataBaseForms,scopesub.row.price)"></el-input>
								<el-input v-else v-model="scopesub.row.price" @input="scopesub.row.price=CheckInputFloat(scopesub.row.price)"></el-input>
							</template>	
						</el-table-column>
						<el-table-column label="操作">
							<template #default="scopesub">
								<el-space>
									<el-link v-if="scope.row.stepList.length-1==scopesub.$index" title="添加" :underline="false" type="warning" @click="addladder(scope.row.stepList,scope.$index)">
									  <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
									 </el-link>
									<el-link title="删除" v-if="scopesub.$index>0&&scope.row.stepList.length-1==scopesub.$index" type="warning" :underline="false" @click="removePrice(scope.row.stepList)">
										<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
									</el-link>
								</el-space>
							</template>	
						</el-table-column>
					</el-table>
					</div>
				</template>
			</el-table-column>
			
			<el-table-column fixed="right" label="其它信息" width="250">
				<template #default="scope">
					<div class="inlinePurchaseForm">
					<el-form label-width="100px">
						<el-form-item label="不良率">
						<el-input   style="margin-top: 3px;" v-model="scope.row.badrate" @input="scope.row.badrate=CheckInputFloat(scope.row.badrate)"  placeholder="不良率">
							  <template #append>%</template>
						</el-input>
						</el-form-item>
						<el-form-item label="1688代码">
							<el-input    v-model="scope.row.productCode"   type="text"  placeholder="1688代码"></el-input>
						</el-form-item>
						<el-form-item label="备注">
							<el-input    v-model="scope.row.remark"   type="text"  placeholder="下单备注"></el-input>
						</el-form-item>
					</el-form>
					</div>
				</template>
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="60">
				<template #default="scope">
					<el-link title="删除"  type="primary" :underline="false" @click="removeSupplier(scope.$index)">
						<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
					</el-link>
				</template>
			</el-table-column>
		</el-table>
	</el-form-item>
	<SupplierDialog ref='supDiaRef' @getdata="getSupplierRows" />
	<!-- 采购价编辑弹窗 -->
	<PriceDialog ref ="priDiaRef" @getprice="getprice"/>
</template>

<script setup>
	import {ArrowDown,Edit,RefreshLeft} from '@element-plus/icons-vue'
	import {Plus,Minus} from '@icon-park/vue-next';
	import { ElMessage } from 'element-plus'
	import { ref,reactive,onMounted,watch,toRefs } from 'vue'
	import SupplierDialog from "@/views/erp/baseinfo/supplier/supplier_dialog.vue"
	import PriceDialog from"./purchase_price_dialog.vue"
	import {useRouter } from 'vue-router';
	import {CheckInputFloat,CheckInputInt,formatFloat} from '@/utils/index.js';
	let router = useRouter();
	const supDiaRef =ref()
	const priDiaRef =ref()
	const type=router.currentRoute.value.query.type;

	let props = defineProps({
	  dataForms:Object,
	  dataBaseForms:Object,
	  forms:Object,
	})
	let state=reactive({
		nowRows:{},
	});
	 let {dataForms,dataBaseForms,forms} =toRefs(props);
	 let { nowRows } =toRefs(state);
	 onMounted(()=>{
		 var timer=setTimeout(function(){
			 watch(props.forms.assemblyforms,(val)=>{
			 			if(val&&val.list.length>0){
			 				var price=0;
			 				val.list.forEach(item=>{
			 					price=price+parseFloat(item.subprice)*parseInt(item.subnumber);
			 				})
			 				props.dataBaseForms.price=price;
			 			}
			 });
			 clearTimeout(timer);
		 },800)
	 })

 
	function checkPriceInputFloat(row,price){
			row.price=price;
	}
	function jumpsupplier(){
		router.push({
			path:"/erp/baseinfo/supplier",
			query:{
				title:'供应商',
				path:'/erp/baseinfo/supplier',
			}
		})
	}
	function selectSupplier(row){
		state.nowRows=row;
		supDiaRef.value.show();
	}
	function addladder(list,index){
		list.push({
			amount:'',
			currency:'0',
			price:'',
		})
		//putval(row,index)
	}
	function removePrice(list){
		list.pop();
	}
	function handleRefreshPrice(list,baseForm){
		var price=0.0;
		list.forEach(item=>{
		     price=price+parseInt(item.subnumber)*parseFloat(item.subprice);
		});
		baseForm.price=formatFloat(price);
	}
 
	
 
	function addSupplier(){
		var row={};
		if(props.dataForms.length>0){
			row.isdefault=false;
		}else{
			row.isdefault=true;
		}
		row.stepList=[{amount:1,price:""}];
		row.name='';
		if(!props.dataForms || props.dataForms==null || props.dataForms==undefined || props.dataForms==undefined || props.dataForms=="null"){
			props.dataForms=[];
		}
		props.dataForms.push(row);
		// cusTableData.push({
		// 	name:"义乌市烈火包装制品有限公司",
		// 	currency:["CNY","USD"],
		// 	price:"",
		// 	check:false,
		// })
	}
	function editPrice(row){
		priDiaRef.value.priceVisable =true;
		state.nowRows=row;
		if(row.stepList && row.stepList.length>0){
			//不做处理
		}else{
			row.stepList=[{
				amount:'',
				currency:'0',
				price:'',
			}];
		}
		priDiaRef.value.tableData=row.stepList;
	}
	function getprice(data){
		state.nowRows.stepList=data;
	}
	function removeSupplier(index){
		props.dataForms.splice(index,1);
	}
	function getSupplierRows(row){
		 var name=row.name;
		 var ispush=true;
		 props.dataForms.forEach(function(item){
			 if(name==item.name){
				 ispush=false;
			 }
		 });
		 if(ispush==true){
			state.nowRows.name=row.name;
			state.nowRows.supplierid=row.id; 
		 }else{
			 ElMessage.error('已有重复的供应商！')
		 }
		
	}
	function changeIsdefault(row){
		var nowisdefault=row.isdefault;
		props.dataForms.forEach(function(item){
			item.isdefault=false;
		});
		if(nowisdefault==true){
			row.isdefault=true;
		}
	}
</script>

<style scoped>
	.position-edit{
		position: relative;
		border: 1px dashed #a4d8ff;
		border-color: transparent;
		cursor: pointer;
	}
	.position-edit .el-icon{
		position: absolute;
		left:0;
		top:0;
		z-index: 2;
		color:#007dff;
		font-size: 14px;
		opacity: 0;
	}
	.position-edit:hover{
		border-color:#a4d8ff;
	}
	.pull-right{
		padding-left:10px;
	}
	.inlinePurchaseForm .el-form-item{
		padding-top:3px;
		padding-bottom:3px;
	}
	.position-edit:hover .el-icon{
		opacity: 1;
	}
</style>
