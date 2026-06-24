<template>
	<el-dialog title="采购详情" v-model="dialogVisable" width="60%">
		<div  class="product-box">
			 <el-image v-if="entry.image" :src="entry.image" class="img-40"  width="40" height="40"  ></el-image>
			 <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
			<div>
				<p class="name">{{entry.mname}}</p>
				<p class="sku">{{entry.sku}}</p>
			</div>
		</div>
		 <el-tabs
		    v-model="activeName"
		    type="card"
		    @tab-change="handleClick"
		  >
		    <el-tab-pane label="操作记录" name="1">
				<!-- 到货记录 -->
				<el-row :gutter="40">
					<el-col :span="11"  class="record-box">
					<ArrivalRecord ref="arrivalRef" @change="loadDetail()"/>
					</el-col>
					<el-col :span="2" class="record-box">
						<el-divider direction="vertical" style="height:100%"/>
						</el-col>
					
					<el-col :span="11" class="record-box">
					 <PayRecord ref="payRef" @change="loadDetail()"/>
					</el-col>
				</el-row>
			</el-tab-pane>
		    <el-tab-pane label="物流信息" name="2">
			    <!-- 物流信息 -->
			    <LogisticsRecord ref="logisRef" />
			</el-tab-pane>
			<el-tab-pane label="交易信息" name="4">
				<!-- 交易订单记录 -->
				<OrderRecord ref="orderRef" />
			</el-tab-pane>
		  </el-tabs>
		  <template #footer>
			  <el-button @click="dialogVisable=false">关闭</el-button>
		  </template>
	</el-dialog>
</template>

<script setup>
	import {h, ref,reactive,onMounted,watch,inject,toRefs,nextTick} from 'vue'
    import {Close,Plus,Edit} from '@element-plus/icons-vue';
	import ArrivalRecord from "./arrival_record.vue"
	import LogisticsRecord from "./logistics_record.vue"
	import PayRecord from "./pay_record.vue"
	import OrderRecord from "./order_record.vue";
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	const arrivalRef=ref();
	const logisRef=ref();
	const payRef=ref();
	const orderRef=ref();
	const state = reactive({
		dialogVisable:false,
		activeName:'1',
		detail:{},
		entry:{},
	})
	const {
		activeName,
		dialogVisable,
		detail,
		entry,
	}=toRefs(state)

	function show(entrys){
		state.activeName="1";
		state.entry=entrys;
		state.dialogVisable = true;
		if(state.entry){
		    loadDetail();
		}
	}
	function loadDetail(){
		purchaselistApi.getRecdetail({"id":state.entry.id,"ftype":"all","actiontype":"all"}).then((res)=>{
					 state.detail=res.data;
					  handleClick(state.activeName);
		});
	}
	function handleClick(val){
		if(val=="1"){
			nextTick(()=>{
			 arrivalRef.value.show(state.entry,state.detail.receivelist);
			 payRef.value.show(state.detail.paylist);
			 })
		}else if(val=="2"){
			nextTick(()=>{
			logisRef.value.show(state.entry.id);
			})
		} else{
			nextTick(()=>{
			orderRef.value.show(state.entry);
			})
		}
	}
	defineExpose({
		show,
	})
</script>

<style scoped>

.product-box{
	display: flex;
	margin-bottom: 24px;
}	
.product-box .el-image{
	margin-right: 16px;
}
.product-box .name{
font-size: 12px;
margin-bottom:8px;
}
.product-box .sku{
font-size: 12px;
color:var(--el-color-blue)
}
.m-t-32{
	margin-top: 32px;
}
.record-box{
		padding:12px;
	}
.img-40{width: 40px;
height: 40px;flex: none;
margin-right: 8px;}
</style>
