<template>
     <span class="pointer" :style="dateFormat(sub.deliverydate)<dateFormat(new Date())?'color: #ff0f47;':''" @click="showpicker">{{dateFormat(sub.deliverydate)}}</span> 
	 <div class="hidepicker" >
	 <el-date-picker
	        v-model="sub.deliverydate"
	        type="date"
	 		ref="deliveryDateRef"
	        placeholder="选择日期"
			@change="updatedevDate"
	      > 
		  </el-date-picker>
	   </div>
</template>


<script setup>
import {reactive,toRefs,ref}from"vue";
import {dateFormat,dateTimesFormat} from '@/utils/index.js';
import {changeDeliveryDate} from  '@/hooks/erp/purchase/form.js';
 const props = defineProps({ sub:{}  })
 const { sub, } =toRefs(props);
 let deliveryDateRef=ref();
 function showpicker(){
	 deliveryDateRef.value.handleOpen();
 }
 function updatedevDate(val){
	 var times=dateFormat(val);
	 var entry=props.sub;
	 changeDeliveryDate(entry.id,times);
	 
 }
</script>

<style>
	.img-40{
		width: 60px;
		height: 60px;flex: none;
		margin-right: 8px;
	}
	.hidepicker .el-input__wrapper{
		display:none;
	}
	.hidepicker,.hidepicker .el-input{
		height:1px;
		margin-top:-3px;
	}
</style>