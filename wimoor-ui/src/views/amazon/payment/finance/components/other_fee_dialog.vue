	<template>
		<el-dialog 	v-model="visible"
	       title="其他费用录入" 
	       destroy-on-close='true' 
		   width="80%"  >
			<el-tabs v-model="activeName"  @tab-change="handleChange">
			  <el-tab-pane v-for="item in menus"  :name="item.name" :key="item.name">
				  <template #label>
					  {{item.label}}
				  </template>
			  </el-tab-pane>
			  </el-tabs>
			  <div v-if="activeName==='0'">
				<el-row :gutter="24">
						  <el-col :span="8">
							  <div v-if="!product">
								  <el-space>
									  <Group  @change="groupChange" defaultValue="only"  ></Group>
									  <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" clearable placeholder="搜索ASIN或SKU"></el-input>
								  </el-space>
						         <GlobalTable ref="globalTable"
									   :tableData="tableData"  height="calc(100vh - 470px)" 
									   :inDialog="true"
									   :highlightCurrentRow="true"
									    @currentChange="handleCurrentChange"
									   :defaultSort="{ prop: 'psku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
									    style="width: 100%;margin-bottom:16px;">
										<template #field>
										<el-table-column label="平台商品信息" prop="psku"    sortable="custom" show-overflow-tooltip >
											<template #default="scope">
											<div class="flex-center">
												   <el-image v-if="scope.row.pimage" :src="scope.row.pimage" class="img-40"  width="40" height="40"  ></el-image>
												   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
												   <div >
													   <div  >{{scope.row.pname}}</div>
													   <p class="sku">{{scope.row.psku}} 
															   <span class="font-extraSmall"> ASIN:{{scope.row.asin}}</span>
													   </p>
												   </div>
											</div>
											</template>
						        </el-table-column>
							</template>
							</GlobalTable>
							</div>
							<div v-else>
								<div class="flex-center">
									   <el-image v-if="product.image" :src="product.image" class="img-100"  ></el-image>
									   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-100"    ></el-image>
									   <div >
										   <div class="font-extraSmall" >{{product.name}}</div>
										   <p  class="font-large">{{product.sku}}
										   																					   <span class="font-extraSmall"> ASIN:{{product.asin}}</span>
										   									</p>
										   <p  class="font-extraSmall">
										   	  <span style="padding-right:10px">店铺：{{product.groupname}}</span>   国家：{{product.marketplacename}}
										   </p>
										 
									   </div>
									 
								</div>
							 <p  class="font-extraSmall" style="padding-top:20px;">
							 	   <div>备注：</div> 
									 <div v-html="product.remark"></div>
							 </p>
							</div>
						  </el-col>
						  <el-col :span="16">
							  <div style="padding-top:5px;padding-bottom:8px;float:left">
								   <el-space>
								   <el-button type="primary" @click="addItem">
									      <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
									   添加费用</el-button>
								  </el-space>
							  </div>
							  <div style="padding-top:5px;padding-bottom:8px;float:right">
								   <el-space>
								     <Datepicker ref="datepickersRef" :shortIndex="3" @changedate="changedate" />
								  </el-space>
							  </div>
							  <el-table :data="feetableData">
								   <el-table-column prop="itemname" label="费用名称"  >
									   <template #default="scope">
									   <span v-if="scope.row.isNew==false">{{scope.row.itemname}}</span>
									   <el-select v-else v-model="scope.row.itemid">
										   <el-option
										      v-for="item in finItemList"
										     :key="item.id"
										     :label="item.name"
										     :value="item.id"
										   > </el-option>
									   </el-select>
									   </template>
								  </el-table-column>
								  <el-table-column label="日期" prop="byday" width="250">
								  	<template #default="scope">
								  	<span v-if="scope.row.isNew==false">{{dateFormat(scope.row.byday)}}</span>
									<el-date-picker v-else
									        v-model="scope.row.byday"
									        type="date"
									        placeholder="请选择日期"
									      />
								  	</template>
								  </el-table-column>
								   <el-table-column prop="currency" label="货币"  >
									      <template #default="scope">
									   <span v-if="scope.row.isNew==false">{{scope.row.currency}}</span>
									   <el-select v-else v-model="scope.row.currency">
											   <el-option
												  v-for="item in currencyList"
												 :key="item.name"
												 :label="item.symbol"
												 :value="item.name"
											   > </el-option>
									   </el-select>
									    </template>
									</el-table-column>
								   <el-table-column prop="amount" label="金额"  >
										   <template #default="scope">
											    <span v-if="scope.row.isNew==false">{{scope.row.amount}}</span>
												<el-input v-else v-model="scope.row.amount" @input="scope.row.amount=CheckInputFloat(scope.row.amount)"></el-input>
											 </template>   
									</el-table-column>   
								   <el-table-column prop="itemname" label="操作"  >
									   <template #default="scope">
										<el-button type="primary" v-if="scope.row.isNew==true" link @click="saveItem(scope.row)">保存</el-button>
										<div v-else>
											<el-button type="primary"   link @click="scope.row.isNew=true">编辑</el-button>
											<el-button type="danger"   link @click="deleteItem(scope.row)">删除</el-button>
										</div>
										
									   </template>
								   </el-table-column>
							  </el-table>
						  </el-col>
				</el-row>
				</div>
				<div v-else>
						  <Record ref="recordRef"></Record>
				</div>
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="visible = false"> 关闭</el-button>
		  	</span>
		  </template>
		</el-dialog>
	</template>
	
	<script setup>
		import {ref,reactive,onMounted,toRefs ,nextTick} from "vue";
		import {Plus} from '@icon-park/vue-next';
		import Datepicker from '@/components/header/datepicker.vue';
		import financesApi from '@/api/amazon/finances/financesApi.js';
		import {CheckInputFloat,CheckInputInt,dateFormat,dateTimesFormat} from '@/utils/index.js';
		import {ElMessage} from 'element-plus';
		import listApi from '@/api/amazon/listing/listingApi.js';
		import Group from '@/components/header/group.vue';
		import Record from "./record.vue"
		import financesItemDataApi from '@/api/amazon/finances/financesItemDataApi.js';
		import exchangeRateAPI from '@/api/amazon/finances/exchangeRateAPI.js';
		import financesItemApi from '@/api/amazon/finances/financesItemApi.js';
		const emit = defineEmits(['change']);
		const globalTable=ref();
		const recordRef=ref();
		const state=reactive({
			tableData:{records:[],total:0},
			feetableData:[],
			queryParams:{},
			queryFinParams:{},
			visible:false,
			activeName:'0',
			formData:{},
			feeList:[],
			product:null,
			menus:[
				{label:'费用录入',name:'0',},
				{label:'操作日志',name:'1',},
			],
			feed:"",
			title:'',
			market:{},
			amount:0,
			currencyList:[],
			currency:'',
			finItemList:[],
		})
		 const {
		   visible,
		   tableData,
		   product,
		   feetableData,
		   queryParams,
		   queryFinParams,
		   activeName,
		   menus,
		   formData,
		   feed,
		   feeList,
		   title,
		   amount,
		   currencyList,
		   currency,
		   finItemList
		 } = toRefs(state);
     	
	function submitCost(){
		 var data={};
		 data.sku=state.formData.sku;
		 data.marketplaceid=state.formData.marketplaceid;
		 data.groupid=state.formData.groupid;
		 var times = new Date();
		 data.byday=times.format("yyyy-MM-dd");
		 data.currency=state.currency;
		 data.itemid=state.feed;
		 data.amount=state.amount;
		 financesApi.saveFinItemData(data).then((res)=>{
		 	if(res.data.status=="success"){
				if(res.data){
					 ElMessage.success( '添加成功！');
					 state.costVisable=true;
				}else{
					 ElMessage.error('添加失败！');
				}
			}
		 });
	}
	function addItem(){
		var data={"id":"","isNew":true,byday:new Date(),currency:"RMB"};
		data.marketplaceid=state.queryFinParams.marketplaceid;
		data.groupid=state.queryFinParams.groupid;
		data.sku=state.queryFinParams.sku;
		data.currency=state.market.currency;
		data.itemid= state.finItemList&&state.finItemList.length>0?state.finItemList[0].id:"";
		state.feetableData.unshift(data);
	}
	function handleChange(value){
		if(value==0){
			show();
		}else{
			nextTick(()=>{
				recordRef.value.show(state.queryParams);
			})
		}
	}
	function handleSelectRow(row){
		 globalTable.value.setCurrentRow(row)
	}
	function handleCurrentChange(row){
		if(row){
			state.queryFinParams.groupid=row.groupid;
			state.queryFinParams.marketplaceid=row.marketplaceid;
			state.queryFinParams.sku=row.psku;
			loadfeeData();
		}
	}
	function saveItem(row){
		if(!row.itemid){
			ElMessage.error('请选择费用项！');
			return;
		}
		if(!row.amount||parseFloat(row.amount)<0.00001){
			ElMessage.error('费用金额必须大于0！');
			return;
		}
		if(!row.byday){
			ElMessage.error( '请选择日期！');
			return;
		}
		if(!row.currency){
			ElMessage.error( '请选择币别！');
			return;
		}
		if(!row.sku){
			ElMessage.error('请选择SKU！');
			return;
		}
		financesItemDataApi.saveFinItemData(row).then(res=>{
			ElMessage.success('添加成功！');
			loadfeeData();
			emit("change");
		});
	}
	function deleteItem(row){
		financesItemDataApi.deleteFinItemData({id:row.id}).then(res=>{
			ElMessage.success('删除成功！');
			loadfeeData();
			emit("change");
		});
	}
	function loadfeeData(){
		financesItemDataApi.getFinDataList(state.queryFinParams).then(res=>{
			if(res.data && res.data.length>0){
				 res.data.forEach(item=>{
					 item.isNew=false;
				 });
			}
			 state.feetableData=res.data;
			 addItem();
		});
	}
	//日期改变
	function changedate(dates,load){
		state.queryFinParams.fromDate=dates.start;
		state.queryFinParams.endDate=dates.end;
		if(load=="load")return;
		if(state.queryFinParams.groupid){
			loadfeeData();
		}
	}
	
	
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		state.market=obj.market;
		handleQuery();
	}
	function handleQuery(){
			       globalTable.value.loadTable(state.queryParams);
	}
	function loadTableData(params){
		      params.ftype="simple";
			  if(params.groupid){
			  listApi.getProductInfoWithFnSKU(params).then(res=>{
					 state.tableData.records=res.data.records;
					 state.tableData.total=res.data.total;
					 if( state.tableData.total>0){
						 setTimeout(function(){
							  handleSelectRow(state.tableData.records[0]);
						 },200);
						
					 }
			  });
			   }
	}
	function loadFinListWithoutPage(){
		financesItemApi.getFinListWithoutPage().then((res)=>{
			state.finItemList=res.data;
		});
	}
	function loadCurrencyRate(){
		exchangeRateAPI.getMyCurrencyRate({byday:new Date().format("yyyy-MM-dd")}).then(res=>{
			state.currencyList=res.data;
		})
	}
	function show(){
		state.visible=true;
		state.product=null;
		financesApi.findFinItemByUser().then((res)=>{
			if(res.data && res.data.length>0){
				state.feeList=res.data;
				state.feed=res.data[0].id;
			}
		});
		loadFinListWithoutPage();
		loadCurrencyRate();
	}
	defineExpose({
		show,
	})
	</script>
	
	<style scoped>
		.img-40{width: 40px;
		height: 40px;flex: none;
		margin-right: 8px;}
		.img-100{width: 100px;
		height: 100px;flex: none;
		margin-right: 8px;}
	</style>
	
 