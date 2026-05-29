<template>
	<el-dialog top="5vh" class="transdialog" v-model="dialog.visible" title="FBA发货配置" @open="handleOpen" width='1100px'>
		 <el-tabs v-model="queryParams.transtype" class="demo-tabs" @tab-change="handleClick">
		    <el-tab-pane v-for="item in transtypeOptions" :label="item.name" :name="item.id"> </el-tab-pane>
			<el-tab-pane  disabled  class="pull-right"> 
			  <template #label>
			         <div  @click.stop="showTransTypeDailog" class="custom-tabs-label">
			           <el-icon> <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/></el-icon>
			           </div>
			       </template> 
			</el-tab-pane>
		  </el-tabs>
		  
		<el-table border :data="formData" height="calc(50vh)" >
			<el-table-column label="国家" width="110">
				<template #default="scope">
					<el-space>
						<el-image  :src="scope.row.image"   width="40" height="40" ></el-image>
						<span>{{scope.row.name}}</span>
					</el-space>
				</template>
				
			</el-table-column>
			<el-table-column label="上架周期(天)" width="130">
				<template #default="scope">
					<el-space>
						<el-input v-model="scope.row.putOnDays"></el-input>
					</el-space>
				</template>
			</el-table-column>
			<el-table-column label="安全库存周期(天)"  width="140">
				<template #default="scope">
					<el-space>
						<el-input v-model="scope.row.stockingCycle"></el-input>
					</el-space>
				</template>
			</el-table-column>
			<el-table-column label="发货频率(天)"  width="130">
				<template #default="scope">
					<el-space>
						<el-input v-model="scope.row.minCycle"></el-input>
					</el-space>
				</template>
			</el-table-column>
			<el-table-column label="头程周期(天)" width="130">
				<template #default="scope">
					<el-space>
						<el-input v-model="scope.row.firstLegDays"></el-input>
					</el-space>
				</template>
			</el-table-column>
			<el-table-column label="默认" width="80">
				<template #default="scope">
					<el-space>
						  <el-tooltip content="每个国家只能有一个默认设置,计算优先产品自身设置">
						   <el-switch
						      v-model="scope.row.isdefault"
						      inline-prompt
						      active-text="是" inactive-text="否" /> 
						  </el-tooltip>
						 
					</el-space>
				</template>
			</el-table-column>
			<el-table-column label="操作人" prop="operatorname" ></el-table-column>
			<el-table-column label="操作时间" prop="opttime"  >
				<template #default="scope">
				   {{dateFormat(scope.row.opttime)}}
				</template>
			</el-table-column>
		</el-table>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="dialog.visible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</span>
		</template>
	</el-dialog>
		<TransTypeDailog ref="transTypeDailogRef" @confirm="loadTransTypeAllList"></TransTypeDailog>
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs} from 'vue'
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import marketApi from '@/api/amazon/market/marketApi.js'
	import fbacycleApi from '@/api/amazon/inbound/fbacycleApi.js'
	import {ElMessage } from 'element-plus'
	import TransTypeDailog from "@/views/erp/ship/transportation/components/trans_type_dialog.vue";
	import userApi from '@/api/sys/admin/userApi.js';
	import {dateFormat} from '@/utils/index.js'
	import $require from '@/utils/system/require.js';
	const transTypeDailogRef=ref();
	const emit = defineEmits(['change']);
	const state=reactive({
		      queryParams:{transtype:""},
			  dialog:{visible:false},
			  formData:[],
			  countryOptions:[],
			  transtypeOptions:[],
			  userinfo:{},
	    });
		const {
		  queryParams,dialog,formData,userinfo,countryOptions,transtypeOptions
		} = toRefs(state);
   function showTransTypeDailog(){
	 transTypeDailogRef.value.show();
   }
   function handleQuery(){
	   fbacycleApi.list(state.queryParams).then(res=>{
		  var countrydata={};
		   if(res&&res.data){
			   res.data.forEach(item=>{
				   countrydata[item.marketplaceid]=item;
			   });
		   }
		   state.formData=[];
		   state.countryOptions.forEach(item=>{
			   if(item.region=="EU"&&item.market!="DE"){
				   return ;
			   }else if(item.market=="DE"){
				   item.name='欧洲';
				   item.marketplaceid="EU";
			   }
			   if(countrydata[item.marketplaceid]){
				   var image=$require('country/'+item.market+'-flag-small.jpg') ;
				   countrydata[item.marketplaceid].image=image;
				   countrydata[item.marketplaceid].name=item.name;
				   state.formData.push(countrydata[item.marketplaceid]);
			   }else{
				   var isdefault=false;
				   if(state.queryParams.transtype==1&&res.data.length<=0){
					  isdefault=true;
				   }
				    var image=$require('country/'+item.market+'-flag-small.jpg') ;
				   state.formData.push({
				   					   'marketplaceid':item.marketplaceid,
				   					   'name':item.name,
				   					   "stockingCycle":3,
				   					   "image":image,
				   					   "minCycle":7,
				   					   "putOnDays":2,
				   					   "transtype":state.queryParams.transtype,
				   					   "firstLegDays":30,
				   					   'isdefault':isdefault,
				   					   "operatorname":state.userinfo.name,
				   					   "opttime":new Date(),
				   });
			   }
		   });
	   })
   }
   function loadTransTypeAllList(){
		transportationApi.getTransTypeAll().then((res)=>{
			state.transtypeOptions=res.data;
			if(state.transtypeOptions&&state.transtypeOptions.length>0){
				state.queryParams.transtype=state.transtypeOptions[0].id;
			}
			handleQuery();
		});
   }
   function submitForm(){
	   state.formData.forEach(item=>{
		   item.opttime=null;
	   })
	   fbacycleApi.save(state.formData).then(res=>{
		   ElMessage.success( '已提交成功！');
			handleQuery();
			emit("change");
	   })
   }
   function handleClick(){
	   handleQuery();
   }
   function loadUserInfo(){
	   userApi.getInfo().then((res)=>{
	   	 state.userinfo=res.data;
	   })
   }
   function show(){
   	state.dialog.visible=true;
   }
   defineExpose({ show });
   async function  loadMarketList(){
   	  await	marketApi.getMarketAll().then((res)=>{
   			state.countryOptions=res.data;
   		});
   }
   function handleOpen(){
	     loadMarketList();
		 loadTransTypeAllList();
		 loadUserInfo();
	 }
</script>

<style>
	.custom-tabs-label{
		cursor: pointer;
	}
 
	.transdialog .el-tabs__nav .is-disabled {
    float:right;
    } 
</style>
