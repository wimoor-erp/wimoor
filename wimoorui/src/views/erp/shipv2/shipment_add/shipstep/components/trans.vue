<template>
	<el-dialog
	title="物流信息修改"
	v-model="visible"
	width="30%"
	
	>
 <el-descriptions class="margin-top"  :column="1"  border  >
	 <el-descriptions-item>
	   <template #label>
	     <div class="cell-item">
	       运输方式
	     </div>
	   </template>
		<el-select v-model="transtype" :disabled="needDisabled" placeholder="请选择" fit-input-width="true" @change="changeTransType">
			 <el-option
				  v-for="item in tranlist"
				  :key="item.id"
				  :label="item.name"
				  :value="item.id"
				/>
		</el-select>
	 </el-descriptions-item>
	<el-descriptions-item>
	  <template #label>
	    <div class="cell-item">
	      物流公司
	    </div>
	  </template>
	<el-select  style="margin-left: 5px;" filterable v-model="companyid" :disabled="needDisabled" @change="companyChange()">
		<el-option  v-for="company in companylist"   :key="company.id"  :label="company.name" :value="company.id"   >
		</el-option>
	</el-select>
	</el-descriptions-item>
	<el-descriptions-item>
	  <template #label>
	    <div class="cell-item">
	      物流渠道
	    </div>
	  </template>
	<el-select  v-model="channelid" filterable  :disabled="needDisabled" style="margin-left: 5px;">
		<el-option  v-for="chan in channellist"   :key="chan.id"  :label="chan.channame" :value="chan.id"   >
		</el-option>
	</el-select>
	</el-descriptions-item>
	<el-descriptions-item v-if="!isadd">
	  <template #label>
	    <div class="cell-item">
	      操作
	    </div>
	  </template>
	<el-button :disabled="needDisabled"  @click="saveTrans" type="primary">保存</el-button>
	</el-descriptions-item>
	<el-descriptions-item v-else>
	  <template #label>
	    <div class="cell-item">
	      应用全部
	    </div>
	  </template>
	  <el-checkbox v-model="allcheck" ></el-checkbox>
	</el-descriptions-item>
	</el-descriptions>
	<template #footer>
		<el-button v-if='isadd' @click="setData">确认</el-button>
		<el-button v-else  @click="visible=false">关闭</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
		import { ref,reactive,onMounted,toRefs,nextTick } from 'vue';
		import { ElMessage,ElMessageBox } from 'element-plus';
		import transportationApi from '@/api/erp/ship/transportationApi.js';
		import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
	 const emit = defineEmits(["change"]);
	let state =reactive({
		visible:false,
		planData:{},
		transtype:"",
		companyid:"",
		channelid:"",
		allcheck:false,
		shipment:"",
		channellist:[],
		companylist:[],
		tranlist:[],
		needDisabled:false,
		 
	})
	let{companyid,channelid,channellist,allcheck,companylist,planData,needDisabled,visible,transtype,tranlist,shipment,}=toRefs(state);
	let props = defineProps({isadd:undefined });
	const {isadd} = toRefs(props);
	function companyChange(type){
				transportationApi.getChannel({"company":state.companyid,"marketplaceid":state.planData.marketplaceid,"transtype":state.transtype}).then((res)=>{
					state.channelid="";
					if(res.data&&res.data.length>0){
						 state.channellist=res.data;
						 state.channelid=res.data[0].id;
						 if(state.planData.channelid && type=="init"){
							  state.channelid=state.planData.channelid;
						 }
					}else{
						state.channelid=null;
						state.channellist=[];
					}
				})
	}
	function saveTrans(){
			shipmentTransportationApi.saveTransCompany({
			"transtype":state.transtype,
			"company":state.companyid,
			"channel":state.channelid,
			"shipmentid":state.planData.reallyShipmentid}).then(res=>{
					 ElMessage.success('保存成功！');
					 emit("change");
			});
	}
	
	function setData(){
		 state.shipment.channelitem={};
		 state.channellist.forEach(item=>{
			 if( state.channelid==item.id){
				 state.shipment.channelitem=item;
			 }
		 })
		 state.shipment.transtypeItem={};
		 state.tranlist.forEach(item=>{
			 if(item.id==state.transtype){
				 state.shipment.transtypeItem=item;
			 }
		 });
		 state.shipment.companyItem={}; 
		 state.companylist.forEach(item=>{
			 if(state.companyid==item.id){
				 state.shipment.companyItem=item;
			 }
		 })
		 state.shipment.allcheck=state.allcheck;
		emit("change",state.shipment,state.planData);
		state.visible=false;
	}
	function getCompanyTranstypeList(val, type) {
		transportationApi.getCompanyTranstypeList({
			"marketplaceid": state.planData.marketplaceid,
			"transtype": val
		}).then(res => {
			if (res.data && res.data.length > 0) {
				state.companylist = res.data;
				state.companyid = res.data[0].id;
				companyChange(type);
			} else {
				state.companylist = [];
				state.companyid = null;
				state.channellist = [];
				state.channelid = null;
			}
		})
	}
	
	function changeTransType(val, type) {
		if (val&&val.id) {
			getCompanyTranstypeList(val.id, type);
		} else {
			getCompanyTranstypeList(val, type);
		}
	}
	
	async function getTransTypeAll() {
		let res = await transportationApi.getTransTypeAll();
		if (res.data) {
			state.tranlist = res.data;
			if (state.transtype == "" || state.transtype==undefined || state.transtype==null) {
				state.transtype = res.data[0].id;
			}
			transportationApi.getTranlist().then(ress=>{
				 if(ress.data&&ress.data.length>0){
					 state.companylist=ress.data;
					 state.companyid="";
					 state.channelid="";
					 if(state.planData.companyid){
						 state.companyid=state.planData.companyid;
						 companyChange("init");
					 }
				 }else{
					state.companylist=[];
				 }
			});
		}
	}
	
	function show(row,shipment){
		state.transtype=row.transtype;
		state.visible=true;
		state.planData=row;
		state.shipment=shipment;
		if(parseInt(state.planData.status)>7){
			state.needDisabled=true;
		}else{
			state.needDisabled=false;
		}
		getTransTypeAll();
	}
	
	
	defineExpose({
		show
	})
</script>

<style>
</style>