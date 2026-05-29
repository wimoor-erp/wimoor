<template>
	<div class="main-sty"> 
		<el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleClick">
		  <el-tab-pane label="全部订单" name="0" :key="0">
		  </el-tab-pane>
		  <el-tab-pane label="未审核" name="1" :key="1">
			  
		  </el-tab-pane>
		  <el-tab-pane label="已审核" name="2" :key="2">
			  
		  </el-tab-pane>
		  <el-tab-pane label="未通过" name="3" :key="3">
			  
		  </el-tab-pane>
		</el-tabs>
		<Header @getdata="changedata" ref="headerRef"  />
		 <Table ref="tableRef" />
	</div>
</template>

<script>
	import Table from "./components/table.vue"
	import Header from "./components/header.vue";
	import { ref,reactive,onMounted } from 'vue'
	export default{
		name:'发货单',
		components:{Header,Table},
		setup(){
			let tableRef=ref(Table)
			let headerRef= ref()
			let obj = reactive({auditstatus:1})
			let activeName = ref("1")
			function handleClick(){
				if(activeName.value=="0"){
					obj.auditstatus = ""
				}else if(activeName.value=="1"){
					obj.auditstatus = 1
				}else if(activeName.value=="2"){
					obj.auditstatus = 3
				}else if(activeName.value=="3"){
					obj.auditstatus = 2
				}
				tableRef.value.getshipplanData(obj);
			}
			function changedata(data){
				obj.groupid = data.groupid;
				obj.marketplaceid =data.marketplaceid;
				obj.warehouseid = data.warehouseid;
				obj.start =data.start;
				obj.end = data.end;
				if(data.seachtype){
					obj.seachtype =data.seachtype;
				}else{
					obj.seachtype ="sku";
				}
				if(data.searchwords){
					obj.searchwords = data.searchwords;
				}else{
					obj.searchwords ="";
				}
				if(data.color){
					obj.color=data.color;
				}else{
					obj.color="";
				}
				tableRef.value.getshipplanData(obj)
			}
			return{
				activeName,tableRef,obj,headerRef,handleClick,changedata
			}
		}
	}
</script>

<style>
</style>
