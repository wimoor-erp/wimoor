<template>
	<div class="main-sty">
	    <Header @getdata="getdata" ref="headerRef"  />
	    <Table :needTab="true" ref="tableRef"  @change="handleTableChange"/>
	</div>
</template>

<script setup>
	import Table from "./components/table.vue"
	import Header from "./components/header.vue";
	import { ref,reactive,onMounted } from 'vue'
	let tableRef=ref();
	let headerRef= ref();
	let obj = reactive({orderStatus:7});
	onMounted(()=>{
 
	})
	function handleTableChange(rows){
		headerRef.value.handleTableSelectChange(rows);
	}
	function getdata(data){
		obj.store = data.store;
		obj.country =data.country;
		obj.warehouse = data.warehouse;
		obj.start =data.start;
		obj.end = data.end;
		obj.seachtype =data.seachtype;
		obj.searchwords = data.searchwords;
		obj.company =data.company;
		obj.channel =data.channel;
		obj.transtype =data.transtype;
		obj.checkdown=data.checkdown;
		obj.fbacenter=data.fbacenter;
		obj.areCasesRequired=data.areCasesRequired;
		obj.hasreferenceid=data.hasreferenceid;
		obj.auditstatus=data.auditstatus;
		if(data.searchwords&&data.searchwords.indexOf("FBA")==0){
			obj.orderStatus="";
		}
		tableRef.value.getshipmentData(obj);
	}
</script>

<style>
</style>
