<template>
	<div class="main-sty">
		<el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleClick">
		  <el-tab-pane  v-for="(item,index) in editableTabs" :label="item.title" :name="item.name" :key="item.name" >
		  </el-tab-pane>
		</el-tabs>
		  <Header @getdata="getdata" ref="headerRef"  />
		  <Table  ref="tableRef"  @change="handleTableChange"/>
	</div>
</template>

<script>
	import Table from "./components/table.vue"
	import Header from "./components/header.vue";
	import { ref,reactive,onMounted } from 'vue'
	export default{
		name:'货件处理',
		components:{Header,Table},
		setup(){
			let tableRef=ref()
			let headerRef= ref()
			let obj = reactive({orderStatus:7})
			let activeName = ref("1")
			let editableTabs =ref([{
				title:'全部货件',
				name:"0"
			},{
				title:'处理中',
				name:"1"
			},{
				title:'已发货',
				name:"2"
			},{
				title:'正在接收',
				name:"3"
			},{
				title:'已完成', 
				name:"4"
			},{
				title:'已取消',
				name:"5"
			},{
				title:'异常单据',
				name:"6"
			},
			])
			onMounted(()=>{
			
			})
			function handleClick(){
				if(activeName.value=="0"){
					obj.orderStatus = ""
					obj.hasexceptionnum=null;
				}else if(activeName.value=="1"){
					obj.orderStatus = 7
					obj.hasexceptionnum=null;
				}else if(activeName.value=="2"){
					obj.orderStatus = 5
					obj.hasexceptionnum=null;
				}else if(activeName.value=="3"){
					obj.orderStatus = 55
					obj.hasexceptionnum=null;
				}else if(activeName.value=="4"){
					obj.orderStatus = 6
					obj.hasexceptionnum=null;
				}else if(activeName.value=="5"){
					obj.orderStatus = 0
					obj.hasexceptionnum=null;
				}else if(activeName.value=="6"){
					obj.hasexceptionnum='ok';
					obj.orderStatus = 6
				}
				tableRef.value.getshipmentData(obj);
				headerRef.value.statusChange(obj);
			}
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
				if(data.searchwords&&data.searchwords.indexOf("FBA")==0){
					activeName.value="0";
					obj.orderStatus="";
				}
				tableRef.value.getshipmentData(obj);
			}
			return{
				activeName,handleClick,editableTabs,tableRef,getdata,
				obj,headerRef,handleTableChange
			}
		}
	}
</script>

<style>
</style>
