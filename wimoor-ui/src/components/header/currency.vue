<template>
	<el-select v-model="currency"  @change="changeData"  >
	      <el-option  
			  v-for="item in currencyList"   
			  :key="item.id"  
			  :label="item.name" 
			  :value="item.id" >
	      </el-option>
	</el-select>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,} from 'vue'
	const emit = defineEmits(['change']);
	
	const props = defineProps({
		defaultValue:'',
	})
	const{
		defaultValue,
	}=toRefs(props)
	const state =reactive({
		currencyList:[
			{name:'CNY',id:'CNY'},
			{name:'USD',id:'USD'},
			{name:'站点币种',id:'MARKET'},
		],
		currency:"CNY",
	})
	const{ currencyList,currency }=toRefs(state)
	onMounted(()=>{
		loadcurrencyList();
	});
	function loadcurrencyList(){
		if(props.defaultValue){
			state.currency=props.defaultValue;
		}
	}
	function changeData(value){
		emit("change",value);
	}
	function reset(value){
		if(value=="CNY"){
			state.currency="CNY";
		}else if(value=="USD"){
			state.currency="USD";
		}else if(value=="MARKET"){
			state.currency="MARKET";
		}else{
			state.currency="CNY";
		}
	}
	defineExpose({
	  state,reset
	})
</script>

<style>
</style>