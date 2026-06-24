<!-- RadioButton -->
<template> 
 	<el-button v-for="item in list"  @click="handleClick(item)" 
	:class="{'active':item.isActive}" link>   {{getLabel(item)}} </el-button>
</template>
 
<script setup>
	import {ref,reactive,watch,onMounted,computed,toRefs} from 'vue'
	let props = defineProps({
							  defaultValue:"",
							  list:[],
							  keyName:"",
							  labelName:"",
							});
	const emit = defineEmits(['change']);
	const { list } = toRefs(props);
	function selectValue(value){
		props.list.forEach((item)=>{
			     var val="";
				  if(props["keyName"]){
				  		  val= item[props["keyName"]]
				  }else{
				  		  val= item["id"] ;
				  }
				  if(val==value){
					  item.isActive = true 
				  }else{
					  item.isActive=false
				  }
		});
		
	}
	function getLabel(row){
		var key="name"
		if(props["labelName"]){
			 key=props["labelName"];
		}
		return row[key] ;
	}
   function handleClick(row){
	   var val="";
	   var key="id"
	   if(props["keyName"]){
		   key=props["keyName"];
	   }
	   val= row[key] ;
	   selectValue(val);
	   emit("change", val);
	   return val;
   }
   function setValue(value){
	  selectValue(value);
   }
  defineExpose({ setValue })
</script>

<style scoped="scoped">
	.el-button.is-link{
		padding: 8px 15px;
		margin-bottom:16px;
	}
	.el-button.active{
		color:var(--el-color-primary);
		background-color:var(--el-color-primary-light-8)!important;
	}
	.el-button.is-link:focus, .el-button.is-link:hover {
		color:var(--el-color-primary)
	}
	.el-button+.el-button{
		margin-left: 0px;
		margin-right:12px;
	}
</style>
