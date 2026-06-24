<template>
	<el-dialog
	title="Asin警告"
	v-model="visable"
	width="70%"
	top="5vh"
	>
	<el-alert title="卖家如果更改相应的品牌，标题，图片会做出报警" show-icon type="info" />
	<div class="flex-center-between m-t-8" style="padding-bottom:10px;">
	<el-space>
			<el-select  style="width:100%;"    v-model="groupid"  >
				<el-option v-for="item in grouplist" :key="item.value"  :label="item.label" :value="item.value"></el-option>
			</el-select>
		<el-button type="primary" @click="handleQuery">查询</el-button>
		<!-- <el-button type="">重置</el-button> -->
	</el-space>
	<div>
		<el-space>
			<el-button type="" @click="stopWarningAll">忽略全部</el-button>
		</el-space>
	</div>
	</div>
		<GlobalTable ref="globalTable" :tableData="tableData"
		@selectionChange='handleSelect' 
		 height="calc(50vh)" 
		@loadTable="loadTableData" :stripe="true"
		>
			<template #field>
				<el-table-column  label="ASIN" prop="asin"     />
			<el-table-column  label="新数据 "     >
		<el-table-column fixed label="图片" width="75" >
			<template #default="scope">
				<el-image :src="scope.row.image"
				 class="product-img"></el-image> 
				 <el-tag v-if="scope.row.image!=scope.row.location" type="error">警告</el-tag>
			</template>
		</el-table-column>
		<el-table-column  label="标题"     >
			<template #default="scope">
			<div v-html="scope.row.diffname"></div> 
					 <el-tag v-if="scope.row.name!=scope.row.pname" type="error">警告</el-tag>
			</template>
		</el-table-column>
		<el-table-column  label="品牌" prop="brand" width="100">
			<template #default="scope">
				{{scope.row.brand}}
				 <el-tag v-if="scope.row.brand!=scope.row.brand2" type="error">警告</el-tag>
		   </template>
		   </el-table-column>
		<el-table-column  label="创建时间" prop="createtime" width="105" sortable >
			<template #default="scope">
				{{dateTimesFormat(scope.row.createtime) }}
			   </template>
		</el-table-column>
		<el-table-column  label="创建人" prop="creatorname" width="80" />
		
		</el-table-column>
		<el-table-column label="旧数据">
		<el-table-column   label="图片" width="75" >
			<template #default="scope">
				<el-image :src="scope.row.location" class="product-img"></el-image>
			</template>
		</el-table-column>
		
		<el-table-column  label="标题"     >
			<template #default="scope">
				{{scope.row.pname}}
			</template>
		</el-table-column>
		
		<el-table-column  label="品牌" prop="brand2" width="100" />
		</el-table-column>
		<el-table-column label="操作" width="80">
			<template #default="scope">
				<el-link  @click="stopWarning(scope.row.pid)">忽略</el-link>
			</template>
		</el-table-column>
	</template>
	</GlobalTable>
	<template #footer>
		<el-button @click="visable=false">关闭</el-button>
	</template>
	</el-dialog>
</template>
 
<script setup>
	import {ref,reactive,toRefs,onMounted,}from"vue";
	import {dateFormat,dateTimesFormat,} from '@/utils/index.js';
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	import authApi from '@/api/amazon/auth/authApi.js';
    const  JsDiff = import('@/utils/diff.js') ;
	const emit = defineEmits(['change']);
	const globalTable=ref();
	const state=reactive({
		visable:false,
		tableData:{records:[],total:0},
		grouplist:[],
		groupid:null,
	})
	const {
		visable,
		tableData,
		grouplist,
		groupid,
	}=toRefs(state)
	
	function stopWarning(pid){
		var list=[];
		list.push(pid);
		stopWarnings(list);
	}
	function stopWarningAll(){
		var authid=state.groupid.split("-")[1];
		var marketplaceid=state.groupid.split("-")[0];
		var params={"authid":authid,"marketplaceid":marketplaceid};
		followListApi.ignoreWarningListAll(params).then((res)=>{
			ElMessage({
			  type: 'success',
			  message: '操作成功',
			})
			emit("change");
			handleQuery();
		});
	}
	function stopWarnings(list){
		followListApi.ignoreWarningList(list).then((res)=>{
			ElMessage({
			  type: 'success',
			  message: '操作成功',
			})
			emit("change");
			handleQuery();
		});
	}
	function handleQuery(){
		var authid=state.groupid.split("-")[1];
		var marketplaceid=state.groupid.split("-")[0];
		var params={"authid":authid,"marketplaceid":marketplaceid};
	    globalTable.value.loadTable(params);
	}
	function getDiff(oldContent,content1){
		var diff = JsDiff.diffChars(oldContent, content1);
		var arr = new Array();
		            for (var i = 0; i < diff.length; i++) {
		                if (diff[i].added && diff[i + 1] && diff[i + 1].removed) {
		                    var swap = diff[i];
		                    diff[i] = diff[i + 1];
		                    diff[i + 1] = swap;
		                }
		                console.log(diff[i]);
		                var diffObj = diff[i];
		                var content = diffObj.value;
		                //可以考虑启用，特别是后台清理HTML标签后的文本
		                if (content.indexOf("\n") >= 0) {
		                    var reg = new RegExp('\n', 'g');
		                    content = content.replace(reg, '<br/>');
		                }
		                if (diffObj.removed) {
		                    arr.push('<del title="删除的部分">' + content + '</del>');
		                } else if (diffObj.added) {
		                    arr.push('<ins title="新增的部分">' + content + '</ins>');
		                } else {
		                    //没有改动的部分
		                    arr.push('<span title="没有改动的部分">' + content + '</span>');
		                }
		            }
		            var html = arr.join('');
		          return  html;
	}
	function loadTableData(params){
		followListApi.findWarningList(params).then((res)=>{
			res.data.records.forEach(item=>{
				item.diffname=getDiff(item.name,item.pname);
			})
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		});
	}
	function show(glist){
		state.visable = true;
			state.grouplist=glist;
			if(glist && glist.length>0){
				state.groupid="";
				setTimeout(function(){handleQuery();},200);
			}
		
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>