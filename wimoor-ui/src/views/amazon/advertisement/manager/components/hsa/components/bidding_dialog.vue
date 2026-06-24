<template>
	<el-dialog
	title="竞价策略设置"
	v-model="visible"
	width="60%"
	
	>
	<div v-loading="loading">
	<el-form v-model="from" label-width="80px" label-position="left">
	    <BiddingTacticsSP  v-if="form.campaignType.toUpperCase()=='SP'" :forms="form"/>
	</el-form>
	</div>
	<template #footer>
		<el-button @click="visible=false">取消</el-button>
		<el-button @click="handleSubmit" type="primary">确认</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs} from"vue"
	import BiddingTacticsSP from './bidding_tactics_sp.vue';
	import {updateCampaings} from '../../javascript/campaigns.js';
	const emits = defineEmits(['change']);
	const state = reactive({
		visible:false,
		loading:false,
		form:{radio:'1'},
	})
	const{
		visible,
		form,
		loading,
	}=toRefs(state)
	defineExpose({
		show,
	})
	function handleSubmit(){
		var data=[];
		state.form.bidding=state.form.dynamicBidding;
		var placementBidding=[];
		state.form.campaignType='SP';
		state.form.type='SP';
		if(state.form.bidding&&state.form.bidding.placementBidding){
			state.form.bidding.placementBidding.forEach(item=>{
				if(item.percentage){
					item.percentage=parseInt(item.percentage);
					placementBidding.push(item);
				}
			});
			state.form.bidding.placementBidding=placementBidding;
		}
		data.push(state.form);
		state.loading=true;
		 updateCampaings(data,()=>{
			 emits("change");
			 state.loading=false;
			 state.visible=false
		 })
		
	}
	function show(form,isshow){
		state.loading=!isshow;
		state.form=form;
		state.visible = true
	}
</script>

<style>
	.have-ass-text-radio .el-radio{
		height:inherit!important;
		line-height:24px;
		white-space: inherit;
	}
	.have-ass-text-radio p{
		font-size:12px;
		color:#acb0b9;
	}
</style>