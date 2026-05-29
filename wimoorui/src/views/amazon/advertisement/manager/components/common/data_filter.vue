<template>
	<el-popover
	     :visible="visible"
	    placement="left"
	    title="指标筛选"
	    :width="800"
		@hide="visible=false"
	    trigger="click"
	  >
	   <el-row class="filter-box"> 
		   <el-col :span="12" class="search-item">
			   <el-scrollbar height="480px">
				   <div class="vertical-flex-space">
				   <div class="filter-item">
				   <div class="list-item m-b-8" v-for="(item,index) in dataList" :key="index">
					   <el-space >
						   <el-select v-model="item.dataType" @change="e=>handleChangeOptios(e,item)">
							   <el-option v-for="option in optionsList" :label="option.name" :value="option.value"></el-option>
						   </el-select>	
						   <el-select v-model="item.symbolType" >
							   <el-option v-for="option in symbolList" :label="option.name" :value="option.value"></el-option>
						   </el-select>
						   <el-input
							type="number"
							v-model="item.dataVal">
							   <template #suffix v-if="item.chartype">
							    %
							   </template>
						   </el-input>
						   <el-button
							@click="handleDelect(index)"
							:disabled="index==0" link><el-icon><CloseBold /></el-icon></el-button>
					   </el-space>
				   </div>
				   <el-button type="primary"
						@click="handleAdd"
						link>
						   <el-icon class="font-medium"><Plus /></el-icon>
						   添加筛选条件</el-button>
					</div>
				   <div class="flex-center-between">
					   <el-button @click="removeAll" link type="info"><el-icon><Delete /></el-icon>清空所有</el-button>
					   <el-button  @click="handleSave" link type="info">保存这次搜索</el-button>
				   </div>
					</div>
			   </el-scrollbar>
		   </el-col >
		   <el-col :span="12" class="search-history">
			   <h5>我的搜索条件</h5>
			   <el-table :data="tableData" class="m-t-8">
				   <el-table-column label="搜索条件" prop="fcondition">
					    <template #default="scope">
							<span v-if="scope.row.isactive" class="text-orange">{{scope.row.fcondition}}</span>
							<span v-else  >{{scope.row.fcondition}}</span>
						</template>
				   </el-table-column>
				   <el-table-column label="操作"   width="110">
					   <template #default="scope">
						   <el-button link type="primary"  @click.stop="handleThisQuery(scope.row)">筛选</el-button>
						   <el-button link    @click.stop="handleRemove(scope.row.id)">删除</el-button>
					   </template>
				   </el-table-column>
			   </el-table>
		   </el-col>
	   </el-row>
	   <div class="footer-popover">
		   <el-button @click="visible=false">关闭</el-button>
		   <el-button @click.stop="clearSearch">清除</el-button>
		   <el-button type="primary" @click.stop="handleQuery">筛选</el-button>
	   </div>
	    <template #reference>
	     <el-button @click="visible=true">筛选器</el-button>
	    </template>
	  </el-popover>
	
</template>

<script setup>
	import {ref ,reactive,onMounted,toRefs,} from 'vue';
	import {Plus,CloseBold,Delete} from '@element-plus/icons-vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	const emit = defineEmits(['change']);
	let props = defineProps({ activeName:"" })
	const { activeName } = toRefs(props);
	const state = reactive({
		visible:false,
		dataList:[
			{dataType:'CSRT',symbolType:'>',dataVal:'',chartype:true,},
		],
		ftype:"",
		optionsList:[
			{name:'转化率',value:'CSRT',chartype:true,},
			{name:'曝光量',value:'impressions',chartype:false,},
			{name:'ACOS',value:'ACOS',chartype:true,},
			{name:'ROAS',value:'ROAS',chartype:true,},
			{name:'花费',value:'cost',chartype:false,},
			{name:'点击率',value:'CTR',chartype:true,},
		],
		symbolList:[
			{name:'大于',value:'>',},
			{name:'等于',value:'=',},
			{name:'小于',value:'<',},
		],
		tableData:[],
	})
	
	const{
		visible,
		dataList,
		optionsList,
		symbolList,
		tableData,
		ftype,
	}=toRefs(state)
	
	function handleAdd(){
		state.dataList.push(
		   {dataType:'CSRT',symbolType:'>',dataVal:'',chartype:true,}
		)
	}
	function clearSearch(){
		clearActive();
		emit("change","");	
	}
	function handleChangeOptios(e,item){
		if(e=='impressions' || e=='cost'){
			item.chartype = false
		}else{
			item.chartype = true
		}
	}
	function handleDelect(index){
		state.dataList.splice(index,1)
	}
	
	function removeAll(){
		state.dataList = [ {dataType:'CSRT',symbolType:'>',dataVal:'',chartype:true,}];
		clearSearch();
	}
	function handleRemove(id){
		advertApi.deleteSerchHistory({"id":id}).then((res)=>{
			if(res.data=="SUCCESS"){
				ElMessage.success("删除成功！");
				loadSearchHistory();
			}else{
				ElMessage.error("删除失败！");
			}
		});
	}
	function handleSave(){ 
		var paralist="";
		if(state.dataList && state.dataList.length>0){
			state.dataList.forEach(item=>{
				if(item.dataVal && item.dataVal!="" && item.dataVal!=undefined){
					var value=item.dataVal;
					if(item.chartype==true){
						value=Number(value.toString()).toPrecision(5);
						value= value/100;
					}
					paralist+=(item.dataType+item.symbolType+value+",");
				}
			});
		}
		if(paralist!=""){
			paralist=paralist.substring(0,paralist.length-1);
			paralist = paralist.replaceAll("CSRT","转化率").replaceAll("bid","默认竞价").replaceAll("ACOS","ACOS").replaceAll("ROAS","ROAS")
						 .replaceAll("cost","花费").replaceAll("impressions","曝光量").replaceAll("sumUnits","销售数量");
			advertApi.addSerchHistory({"condition":paralist,"ftype":state.ftype}).then((res)=>{
				if(res.data=="SUCCESS"){
					ElMessage.success("操作成功！");
					loadSearchHistory();
				}else{
					ElMessage.error("操作失败！");
				}
			});			 
					 
		}else{
			ElMessage.error("请正确输入筛选条件！");
		}
	}
	function clearActive(){
		state.tableData.forEach(item=>{
			item.isactive=false;
		});
	}
	function handleThisQuery(row){
		var paralist=row.fcondition;
		clearActive();
		if(row.fcondition){
			if(paralist!=""){
				paralist = paralist.replaceAll("转化率","CSRT").replaceAll("默认竞价","bid").replaceAll("ACOS","ACOS").replaceAll("ROAS","ROAS")
							 .replaceAll("花费","cost").replaceAll("曝光量","impressions").replaceAll("销售数量","sumUnits").replaceAll(","," and ");
				row.isactive=true;
				emit("change",paralist);		 
			}else{
				ElMessage.error("请查看筛选条件是否正确！");
			}
		}else{
			ElMessage.error("请查看筛选条件是否正确！");
		}
	}
	function handleQuery(){
		var paralist="";
		clearActive();
		if(state.dataList && state.dataList.length>0){
			state.dataList.forEach(item=>{
				if(item.dataVal && item.dataVal!="" && item.dataVal!=undefined){
					var value=item.dataVal;
					if(item.chartype==true){
						value=Number(value.toString()).toPrecision(5);
						value= value/100;
					}
					paralist+=(item.dataType+item.symbolType+value+",");
				}
			});
		}
		if(paralist!=""){
			paralist=paralist.substring(0,paralist.length-1);
			paralist = paralist.replaceAll("转化率","CSRT").replaceAll("默认竞价","bid").replaceAll("ACOS","ACOS").replaceAll("ROAS","ROAS")
						 .replaceAll("花费","cost").replaceAll("曝光量","impressions").replaceAll("销售数量","sumUnits").replaceAll(","," and ");
			emit("change",paralist);		 
		}else{
			ElMessage.error("请正确输入筛选条件！");
		}
	}
	 
	function loadSearchHistory(){
		var ftype="advcams";
		if(props.activeName==1){
			ftype="advcams";
		}else if(props.activeName==2){
			ftype="advadgs";
		}else if(props.activeName==3){
			ftype="advpros";
		}else if(props.activeName==4){
			ftype="advkeys";
		}else if(props.activeName==6){
			ftype="advtarget";
		}else if(props.activeName==5 || props.activeName==7){
			ftype="advsehs";
		}else if(props.activeName==8){
			ftype="advsehstarget";
		}
		state.ftype=ftype;
		advertApi.getSerchHistory({"ftype":ftype}).then((res)=>{
			state.tableData=res.data;
		});
	}
	onMounted(()=>{
		loadSearchHistory();
	});

</script>

<style>
	.filter-box{
		border:1px solid #eee;
		display:flex;
		border-radius:4px;
	}
	.search-item{
		background-color:#f5f5f5;
		padding:16px;
	}
	.search-history{
		padding:16px;
	}
	.footer-popover{
		margin-top:12px;
	}
	.m-b-8{
		margin-bottom:8px;
	}
	.vertical-flex-space{
		height:100%;
		display: flex;
		flex-direction:column;
		justify-content: space-between;
	}
	.filter-item{
		flex-grow:1;
	}
</style>