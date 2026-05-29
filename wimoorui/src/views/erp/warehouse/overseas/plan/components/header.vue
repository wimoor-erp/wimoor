<template>
	<div class="sys-headr-top">
		<el-space :size="16" class="font-base">
			<div class="flex-center">
			<span>仓库（发货/采购）：</span>
		     <Warehouse @changeware="getWarehouse"   defaultValue="only"   />
			</div>
			<div>
				<span >国家：</span>
				<el-select v-model="queryParams.country"  @change="handleQuery" >
					<el-option   v-for="(item,index) in countryOptions" :key="index" :label="item" :value="item" />
				</el-select>
				</div>
		</el-space>
		
		<el-space>
		<el-button @click="refreshPlan" :loading="refreshPlanLoading" type="primary">计算</el-button>
		<el-button @click="clearPlan">清空计划</el-button>
	</el-space>
	</div>		
 
	<div class=" con-header"  style="padding:0px 16px 0px 16px;">

			<div class="flex-between"  style="padding:16px 16px 16px 0px;">
				<el-space>
				 
				   <Owner  ref="ownerRef" @owner="getOwner" />
				   <div class="mytagsgroup group el-input-group">
						 <Tags   ref="tagsRef" @change="getTags"/>
						 <el-checkbox-button v-model="queryParams.notag" @change="handleQuery" label="排除" size="large" />
				   </div>
				   <el-input  
				   v-model="queryParams.search" 
				   clearable 
				   @input="handleQuery"  
				   @clear="handleQuery" 
				   placeholder="请输入SKU" 
				   class="input-with-select" >
				   <template #append>
				     <el-button >
				        <el-icon style="font-size: 16px;align-itmes:center">
				         <search @click="handleQuery" />
				      </el-icon>
				     </el-button>
				   </template>
				 </el-input>
				 <el-popover    
				    v-model:visible="moreSearchVisible" 
				    :width="400" 
				    trigger="click">
				 	<template #reference>
				 	<el-button  title='高级筛选'  class='ic-btn'>
				 	<i>
				 	<svg width="16" height="16" fill="none" viewBox="0 0 48 48"  xmlns="http://www.w3.org/2000/svg">
				 		<path d="M6 9L20.4 25.8178V38.4444L27.6 42V25.8178L42 9H6Z" fill="none" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>
				 	 </i>
				 	</el-button>
				 	</template>
				 	 <el-form   label-width="80px">
				 		 <el-form-item label="类型">
				 			   <el-radio-group v-model="queryParams.issfg">
				 			      <el-radio-button label="" >全部产品</el-radio-button>
				 			      <el-radio-button label="0" >普通产品</el-radio-button>
				 				  <el-radio-button label="1" >组合产品</el-radio-button>
				 			    </el-radio-group>
				 			 </el-form-item>	
				 		<el-form-item label="产品品类">
				 		 <Category @change="getCategory"></Category>
				 		</el-form-item>
				 		<el-form-item label="平台SKU">
				 		  <el-input
				 		      v-model="queryParams.skuarray"
				 		      :rows="2"
				 		      type="textarea"
				 		      placeholder="逗号分隔批量搜索..."
				 		    />
				 		 </el-form-item>
						 <el-form-item label="产品名称">
						   <el-input
							   v-model="queryParams.name"
							   placeholder="搜索名称"
							 />
						  </el-form-item>
							 <el-form-item label="备注">
							   <el-input
								   v-model="queryParams.remark"
								   :rows="2"
								   type="textarea"
								   placeholder="请输入备注"
								 />
							  </el-form-item>
							 <el-form-item label="产品状态">
							   <el-radio-group v-model="queryParams.status2">
								   <el-radio label="">不限</el-radio>	 
								   <el-radio label="shownormal">未隐藏</el-radio>
								   <el-radio label="showhid">隐藏</el-radio>
								 </el-radio-group>
							 </el-form-item>
				 		 <el-form-item >
				 			 <el-button type="primary" @click="handleQuery();moreSearchVisible=false">搜索</el-button>
				 			 <el-button @click="resetForm()">取消</el-button>	
				 		</el-form-item>
				 		 </el-form>
				 	  </el-popover>
					  <el-popover    v-model:visible="dataSearchVisible" :width="400" trigger="click">
					  	<template #reference>
					  		<el-button>指数过滤</el-button>
					  	</template>
					  	 <el-form :model="form" label-width="auto"   >
					  		<el-form-item label="数据字段">
					  		     <el-select  v-model="dataColumn"  :teleported="false" placeholder="请选择" @change="columnChange">
					  				<el-option
					  				  v-for="item in columnList"
					  				  :key="item.value"
					  				  :label="item.label"
					  				  :value="item.value"
					  				/>
					  		     </el-select>
					  		   </el-form-item>
					  		   <el-form-item label="符号">
					  		       <el-radio-group v-model="mark">
					  		           <el-radio :label="3">大于</el-radio>
					  		           <el-radio :label="6">小于</el-radio>
					  		           <el-radio :label="9">等于</el-radio>
					  		         </el-radio-group>
					  		      </el-form-item>
					  			  <el-form-item label="数值">
					  				  <el-input v-model="dataValue" type="number"  placeholder="用%时请输入小数"></el-input>
					  				  <el-button style="margin-top: 3px;" @click="addColumn" type="success">添加条件</el-button>
					  				   <el-button style="margin-top: 3px;" @click="clearColumn" type="success">清空条件</el-button>
					  			    </el-form-item>
					  				<el-form-item label="筛选条件">
					  					{{columntext}}
					  				</el-form-item>
					  			    <el-form-item>
					  			  <el-button type="primary" @click="submitDataForm(formRef)">搜索</el-button>
					  			  <el-button @click="resetDataForm(formRef)">取消</el-button>
					  			    </el-form-item>
					  	</el-form>
					    </el-popover>
					  
				 	 <el-button @click="resetQuery">重置</el-button>
				</el-space>
		 
				</div>
	 
		<el-row>
				<el-button type="primary" class="im-but-one" @click="submitPlan">
				  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
				  <span>采购</span>
				</el-button>
				<el-button type="primary" class="im-but-one" @click="submitShip">
				  <ship theme="outline" size="18" fill="#fff"  :strokeWidth="4"/>
				  <span>发货</span>
				</el-button>
				<!-- <el-button @click="gotoSalePre" > 销售计划</el-button> -->
				<el-button @click="showFbaDeliveryDialog">发货配置</el-button>
				<div class='rt-btn-group'>
					<div class="flex-center font-small m-r-16">
						<!-- <el-button v-if="queryParams.selected==true" @click.stop="downloadCheckList" :loading="downloading" size="small" style="margin-right:10px;">导出已选</el-button> -->
						<el-checkbox style="margin-right:0px;" v-model="queryParams.selected" label="显示已选"  @change="handleQuery"/>
	                  	<el-divider direction="vertical" />
						<el-checkbox v-model="queryParams.expendall" label="默认展开"  @change="handleExpendAll"/>
						<el-divider direction="vertical" />
						<el-checkbox  v-if="queryParams.marketplaceids&&queryParams.marketplaceids.length>0" v-model="queryParams.plansimple" label="简约计划"  @change="handleQuery"/>
					     <el-divider v-if="queryParams.marketplaceids&&queryParams.marketplaceids.length>0" direction="vertical" />
						<hoc-el-affix
						  :offset-top="80"
						  @change="e=>queryParams.isAffix=e"
						  :class="queryParams.isAffix?'isAffix':''"
						>
					     <div class="affix-s">
							<span @click="handleCheckShow">已选 <span class="text-orange font-bold"> {{summary.qty}} </span> 个SKU</span>
							<el-divider direction="vertical" />
							<span>采购总数 <span class="text-orange font-bold">
							<span v-if="summary.purchaseqty">{{summary.purchaseqty}}</span>
							<span v-else>0</span>
							</span></span>
							<el-divider direction="vertical" />
							<span>发货总数 <span class="text-orange font-bold"> 
							<span v-if="summary.shipqty">{{summary.shipqty}}</span>
							<span v-else>0</span>
							</span></span>
							</div>
						</hoc-el-affix>
					</div>
					<el-space v-if="queryParams.marketplaceids&&queryParams.marketplaceids.length>0" >
					<el-dropdown :hide-on-click="false" @command="handleCommand"  trigger="click">
					    <el-button class='ic-btn'  title='排序'>
					       <sort-one theme="outline" size="16"  :strokeWidth="3"/>
					    </el-button>
					    <template #dropdown>
					      <el-dropdown-menu >
								<el-dropdown-item disabled>排序依据</el-dropdown-item>
										<el-dropdown-item v-for="(item,index) in rankData" 
										                  :key="index" 
																			@click="rankChange(item.value)"
								                      :class="{r_active:queryParams.currentRank==item.value}" >
																			{{item.name}}
										</el-dropdown-item>
					        <el-dropdown-item divided  disabled>排序循序</el-dropdown-item>
					          <el-dropdown-item v-for="(item,index) in soltData" 
									        :key="index" 
													@click="soltChange(item.value)" 
							            :class="{r_active:queryParams.currentSolt==item.value}">
													{{item.name}}
										</el-dropdown-item>
					      </el-dropdown-menu>
					    </template>
					  </el-dropdown>
					  <el-button class='ic-btn m-l-0' title='帮助文档' >
					  	<help theme="outline" size="16" :strokeWidth="3" />
					  </el-button>
					</el-space>
				</div>	
		</el-row>
	</div>
	<DeliveryDialog ref="deliverRef" />
    <PurchaseDialog ref="purchaseRef" @change="handleQuery();handleQuerySummary()"></PurchaseDialog>
	 <ShipDialog ref="shipRef" @change="handleQuery();handleQuerySummary()"></ShipDialog>
	
</template>

<script setup>
	import {Search,ArrowDown} from '@element-plus/icons-vue'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,SortOne,Ship} from '@icon-park/vue-next';
	import { ref ,reactive,onMounted,toRefs,inject} from 'vue'
	import Warehouse from "@/components/header/warehouse.vue"
	import Owner from '@/components/header/owner.vue';
	import Tags from '@/components/header/tags.vue';
	import Category from '@/components/header/category.vue';
	import {downExcelSales} from "@/api/amazon/listing/preSalesApi.js";
	import filtericon from "@/components/icon/filtericon.vue";
	import { ElMessage, ElMessageBox } from 'element-plus'
	import PurchaseDialog from "./PurchaseDialog.vue";
	import ShipDialog from "./ShipDialog.vue";
	// API依赖
	import orderPlanApi from '@/api/erp/order/orderPlanApi.js';
	import { useRouter } from 'vue-router';
	import editApi from '@/api/erp/purchase/form/editApi.js';
  import materialApi from '@/api/erp/material/materialApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js'
	import DeliveryDialog from "./delivery_dialog.vue";
	let deliverRef = ref()
	let marketRef=ref();
	const purchaseRef=ref();
	const shipRef=ref();
	let ownerRef=ref();
	let tagsRef=ref();
	let uploadDialogRef=ref();
	const checkDataDialogRef=ref();
	 const emitter = inject("emitter");
	let router = useRouter();
	let columntext=ref("");
	let columnval=ref("");
	let mark=ref(3);
	const state = reactive({
	  dataSearchVisible:false,
	  dataColumn:"v3.sales7",
	  columnList:[{"value":"v3.sales7","label":"7日销量"},
		{"value":"v3.sales30","label":"30日销量"},
		{"value":"v3.overseaqty","label":"海外仓可用库存"},
		{"value":"v3.localqty","label":"本地可用库存"},
		{"value":"v3.salesday","label":"可销售天数"},
		{"value":"v3.shipqty","label":"建议发货量"}
	  ],
	  dataValue:null,
	  queryParams: {planid:'',
	                searchtype:'sku',selected:false,small:"",currentRank:"",status2:"shownormal",notag:false,
					hasAddFee:"",issfg:"",currentSolt:"desc",defoutwarehouseid:""
					} ,
		loading: false,
		countryOptions:null,
	  refreshPlanLoading:false,
	  moreSearchVisible:false,
	  summary:{skunum:0,amount:0},
	  progress:0,
	  downloading:false,
	});
	const {
	  queryParams,loading,progress, summary,downloading,notag,refreshPlanLoading,moreSearchVisible,countryOptions,dataSearchVisible,
	  dataColumn,dataValue,columnList,
	} = toRefs(state);
	
	let FbaInventorysChecked = ref([]);
	let localinventoryChecked=ref(1);
  
		let rankData =reactive([
			{name:'可售库存',value:'afn_fulfillable_quantity'},
			{name:'预估日均销量',value:'avgsales'},
			{name:'7日销量',value:'sales_seven'},
			{name:'30日销量',value:'sales_month'},
			{name:'实际发货量',value:'marketamount'},
			{name:'建议发货量',value:'marketneedship'},
			{name:'可售天数',value:"marketsalesday"},
			{name:'发货后可售天数',value:"marketaftersalesday"},
		])
		let soltData = reactive([
			{name:'从高到低',value:"desc"},
			{name:'从低到高',value:'asc'},
		]) 
		let form = reactive({
					type:'全部产品',
					small:3,
					extraprice:3,
		})
		
		function submitDataForm(){
			state.queryParams.paralist=columnval.value;
			handleQuery();
			state.dataSearchVisible=false;
		}
		function resetDataForm(){
			state.dataSearchVisible=false;
		}
		function addColumn(){
			var data1="";var data2="";var data3=dataValue.value;
			if(data3=="" || data3==undefined ||data3==null || data3=="undefined"){
				ElMessage.error('请输入正确的值！');
				return;
			}
			if(dataColumn.value=="v3.sales7"){
				data1="7日销量";
			}
			if(dataColumn.value=="v3.sales30"){
				data1="30日销量";
			}
			if(dataColumn.value=="v3.overseaqty"){
				data1="海外仓可用库存";
			}
			if(dataColumn.value=="v3.localqty"){
				data1="本地可用库存";
			}
			if(dataColumn.value=="v3.salesday"){
				data1="可销售天数";
			}
			if(dataColumn.value=="v3.shipqty"){
				data1="建议发货量";
			}
			if(mark.value==3){
				data2=">";
			}
			if(mark.value==9){
				data2="=";
			}
			if(mark.value==6){
				data2="<";
			}
			columntext.value=columntext.value+(data1+data2+data3+";");
			columnval.value=columnval.value+(dataColumn.value+data2+data3+",");
		}
		
		function clearColumn(){
			columntext.value="";
			columnval.value="";
		}
		
	   function clearPlan(){
					ElMessageBox.prompt(
					   '<h2 class="text-red">注意，此操作不可逆</h2><div style="margin-top:10px;margin-bottom:10px"><strong >系统将清空所有已经加入的计划不区分店铺，您确定要清空发货计划吗?</strong></div>请在下方输入框填写:<strong>我已知晓</strong>',
					    '清空计划',
					    {
					      confirmButtonText: '确定',
					      cancelButtonText: '取消',
					      type: 'warning',
						  dangerouslyUseHTMLString: true,
						  inputErrorMessage: '请在框中输入文字',	
					    }
					  )
					    .then((value) => {
							if(value && value.value=="我已知晓"){ 
								if(state.queryParams.warehouseid){
									orderPlanApi.clear({"warehouseid":state.queryParams.warehouseid}).then(res=>{
										 ElMessage.success('清除成功！');
										 handleQuerySummary();
										 emits("change",state.queryParams);
									});
								}
							}else{
								ElMessage.info('未收到正确口令，本次操作无效！');
							}
					    }).catch(() => {
					      ElMessage.info('取消清除！');
					    })
				}
				
			let  emits = defineEmits(['change'])	
			function handleCheckShow(){
				state.queryParams.selected=true;
				//state.queryParams.ischeck="all";
				handleQuery();
			}
function handleQuery(type) {
				if (state.countryOptions !=null) { 
						if (state.queryParams.country) { 
							localStorage.setItem("overseacountry",state.queryParams.country);
						}
						emits("change",state.queryParams);
					} else { 
						console.log("getWarehouse.........");
						warehouseApi.getOversaCountry().then(res=>{
									if(res.data&&res.data.length>0){
										state.countryOptions = res.data;
										state.queryParams.country = state.countryOptions[0];
										var overseacountry = localStorage.getItem("overseacountry");
										if(overseacountry){
											state.countryOptions.forEach(item=>{
												if(item==overseacountry){
													state.queryParams.country=item;
												}
											})
										}
									} else {
										state.countryOptions = [];
							}
							emits("change",state.queryParams);
								})
					}
				}
			function handleQuerySummary(){
				 orderPlanApi.summary().then(res=>{
					 state.summary=res.data;
				 });
			}
				
			function showFbaDeliveryDialog(){deliverRef.value.show()}
			function getWarehouse(wid){
				state.queryParams.warehouseid=wid;
				handleQuery();
				handleQuerySummary();
			}
	var timer=null;
onMounted(() => {
		emitter.on("openTab", (value) => { // 监听事件
		  if(value=="/erp/warehouse/overseas/plan"){
			  if(timer==null){
				  timer=setInterval(function(){
				  	handleQuerySummary();
				  },60000);
			  }
		  }else if(timer){
		  	clearInterval(timer);
			timer=null;
		  }
		});
	
		timer=setInterval(function(){
			handleQuerySummary();
		},60000);
	});
			 defineExpose({ handleQuerySummary });
            function refreshPlan(){
				state.refreshPlanLoading=true;
				orderPlanApi.refreshData(state.queryParams).then(res=>{
					state.refreshPlanLoading=false;
					ElMessage.success("计算成功!")
					handleQuery();
				})
			}
 
			function handleExpendAll(value){
				      state.queryParams.expendall=value;
					  emits("expend",value);
			}
			function getOwner(id){
				state.queryParams.owner=id;
				handleQuery();
			}
			function getTags(tags){
				state.queryParams.tags=tags;
				handleQuery();
			}
			function getCategory(value){
				state.queryParams.categoryid=value;
			}
			function resetQuery(){
				state.queryParams.searchtype="sku";
				state.queryParams.selected=false;
				state.queryParams.categoryid="";
				state.queryParams.shortdays=false;
				state.queryParams.status2="shownormal";
				state.queryParams.owner="";
				state.queryParams.search="";
				state.queryParams.name="";
				state.queryParams.remark="";
				state.queryParams.tags=[];
				state.queryParams.marketplaceids=[];
				state.queryParams.status='';
				state.queryParams.hasAddFee="";
				state.queryParams.issfg="";
				state.queryParams.different="";
				state.queryParams.hasAssembly="";
				state.queryParams.currentSolt="desc";
				state.queryParams.currentRank="";
				state.queryParams.skuarray="";
				state.queryParams.defoutwarehouseid="";
				ownerRef.value.reset();
				tagsRef.value.reset();
				handleQuery();
			}
			function resetForm(){
				state.moreSearchVisible =false
			}	
			function rankChange(value){
				state.queryParams.currentRank=value;
				state.queryParams.sort=value;
				state.queryParams.order=state.queryParams.currentSolt;
				handleQuery("opt");
			}
		 
			function soltChange(value){
				state.queryParams.currentSolt=value;
				handleQuery("opt");
			}
			function handleCalu(){
				    state.loading=true;
				    state.progress=0;
					orderPlanApi.calPlanModel(state.queryParams).then(res=>{
						state.progress=100;
						state.loading=false;
						ElMessage.success('计算成功！');
						handleQuery();
					})
			}
		 
			function gotoSalePre(){
				router.push({
					path:"/amazon/sales/forecast",
					query:{
						title:'销售计划',
						path:"/amazon/sales/forecast",
					}
				})
			}
		
			/* 采购提交 */
			function submitPlan(){
				orderPlanApi.getPurchase({"warehouseid":state.queryParams.warehouseid}).then(res=>{
					state.queryParams.list=res.data;
					 purchaseRef.value.show(state.queryParams);
				})
			  
			}
			/* 发货提交 */
			function submitShip(){
				orderPlanApi.getShip().then(res=>{
					 state.queryParams.shiplist=res.data;
					 shipRef.value.show(state.queryParams);
				})
			}
			
			function downloadCheckList(){
				state.downloading=true;
				editApi.downloadItemList({"planid":state.queryParams.planid},()=>{
					state.downloading=false;
				});
			}
			function goToPlanConfirm(queryPrarams,summary){
				   console.log(queryPrarams);
				   var params=JSON.parse(JSON.stringify(state.queryParams));
				       params.plantype="purchase";
					   params.pagesize=1000000;
					   params.currentpage=1;
					   var itemlist=[];
					   
					   orderPlanApi.getPurchase({"warehouseid":state.queryParams.warehouseid}).then(res=>{
						   console.log(res.data);
					   });
					   
					 
				
			}
</script>

<style>
	.mytagsgroup .el-input__wrapper{
		border-top-right-radius:0em;
		border-bottom-right-radius:0em;
	}
	.sys-headr-top{
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding:16px 24px;
		border-bottom:1px solid var(--el-border-color);
		background-color:var(--el-bg-color);
	}
	.no-flex-warp{
		flex-wrap:inherit;
	}
	.m-r-16{
		margin-right:16px;
	}
	.m-l-0{
		margin-right:-8px;
	}

	.p-2{
		bottom:40px;
	}
	.m-b-4{
		margin-bottom:4px;
	}
	.isAffix .affix-s{
		background-color:rgba(0,0,0,0.8);
		border-radius: 4px;
		padding:4px 8px;
		height:24px!important;
		white-space: nowrap;
		color:#fff;
	}
	.affix-s{
		margin-right:8px;
		margin-left:8px;
	}
</style>
