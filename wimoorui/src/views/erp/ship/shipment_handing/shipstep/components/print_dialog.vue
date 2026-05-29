<template>
	<el-dialog
	    v-model="visible"
	    title="打印单详情"
	    width="50%"
	    :before-close="handleClose"
	  >
	 
			 <div id="printPage">
				 <div class="text-center" style="font-size:30px;font-weight: bolder;color: black;">
				 	 <span v-if="planData.number">{{planData.number}}	</span>		 
					 <span v-else>{{planData.shipmentid}}</span>
				 </div>
				 
					  <div class="text-center" style="font-size: 56px;font-weight: bolder;color: black;padding-top:40px">
					    <span v-if="localRow.msku.length<=15">{{localRow.msku}}</span>
						<div v-else>
							<div>{{localRow.msku.substring(0,15)}}</div>
							<div>{{localRow.msku.substring(15,localRow.msku.length)}}</div>
						</div>
					  </div>
					  <div v-if="edit" class="text-center" style="font-size: 20px;font-weight: bolder;color: black;margin-top:20px ;"  >
					  发货数量: 
					  <el-space>
					   <el-input-number v-model="localRow.editQuantityShipped"   />
					   <el-button type="primary" @click="localRow.QuantityShipped=localRow.editQuantityShipped;edit=false">确认</el-button>
					   <el-button @click="edit=false">取消</el-button>
					   </el-space>
					  </div>
					  <div v-else @click="localRow.editQuantityShipped=localRow.QuantityShipped;edit=true;" class="text-center pointer" style="font-size: 30px;font-weight: bolder;color: black;margin-top:20px ;">
						  发货数量:{{localRow.QuantityShipped}}
					  </div>
					  <div class="text-center" style="font-size:30px;font-weight: bolder;color: black;margin-top:40px ;">
						 <span v-if="planData.createtime"> {{dateFormat(planData.createtime)}}</span>
						 <span v-else> {{dateFormat(planData.createdate)}}</span>
					  </div>
			 </div>
			 <template #footer>
			   <span class="dialog-footer">
			     <el-button @click="visible= false">关闭</el-button>
				 <el-button type="primary" v-print="content">
				   打印
				 </el-button>
			   </span>
			 </template>
	  </el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs } from 'vue';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	const emit = defineEmits(['change']);
	const state = reactive({
		localRow:null,
		planData:{},
		visible:false,
		edit:false,
		content:{id:"printPage",popTitle:"",},
	})
	const {
		localRow,
		visible,
		edit,
		content,
		planData,
	}=toRefs(state)
	
	function show(row,planData){
		 
		 state.planData=planData;
		 state.visible=true;
		 state.localRow=row;
		 state.content.popTitle="";
	}
	defineExpose({show})
	
</script>

<style>
</style>