	<template>
		<el-dialog v-model="costVisable" title="其他成本" destroy-on-close='true' width="800px"  >
				<el-row>
					<el-space >
			         <el-button type="primary" @click="addCost">添加成本</el-button>
					 <Datepicker  @changedate="changedate" />
					 <el-button> 费用类型管理</el-button>
					</el-space>
				</el-row>
				<el-table border class="m-t-16" :data="tableData">
					<el-table-column prop="itemname" label="费用类型">
					</el-table-column>
					<el-table-column label="分摊日期" width="250">
						<template #default="scope">
						<el-date-picker
						        v-model="scope.row.date"
						        type="date"
						        placeholder="请选择"
						      />
						</template>
					</el-table-column>
					<el-table-column label="金额" >
						<template #default="scope">
							<el-input v-model="scope.row.price">
								<template #append>
									<el-select v-model="scope.row.currenty">
										<el-option label="USD" :value="1"></el-option>
									</el-select>
								</template>
							</el-input>
						</template>
					</el-table-column>
					<el-table-column label="操作" width="100">
						<template #default="scope">
							<el-button v-if="true" type="primary" link>编辑</el-button>
							<el-button v-else type="primary" link>保存</el-button>
							<el-button type="primary" link>删除</el-button>
						</template>
					</el-table-column>
				</el-table>
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="costVisable = false"> 关闭</el-button>
		  	</span>
		  </template>
		</el-dialog>
	</template>
	
	<script setup>
		import {ref,reactive,onMounted,toRefs } from "vue"
		import Datepicker from '@/components/header/datepicker.vue';
		import productinoptApi from '@/api/amazon/product/productinoptApi.js';
		const state=reactive({
			costVisable:false,
			tableData:{records:[],total:0},
			queryParam:{},
		})
		 const {
		   costVisable,
		   tableData,
		   queryParam,
		 } = toRefs(state);
     	defineExpose({
			loadData,
		})
	function addCost(){
		tableData.push({
			costType:'',
			data:'',
			price:'',
		})
	}
	function changedate(val){
		state.queryParam.fromDate=val.start;
		state.queryParam.endDate=val.end;
	}
	
	function loadData(row){
		state.costVisable=true;
		var data={};
		data.itemid='';
		data.sku=row.sku;
		data.osku='';
		data.marketplaceid=row.marketplaceid;
		data.groupid=row.groupid;
		if(state.queryParam.fromDate){
			data.fromDate=state.queryParam.fromDate;
			data.endDate=state.queryParam.endDate;
		}else{
			var start = new Date();
			var end=new Date();
			data.fromDate=start.format("yyyy-MM-dd");
			end.setTime(end.getTime() - 3600 * 1000 * 24 * 7);
			data.endDate=end.format("yyyy-MM-dd")+" 23:59:59";
		}
		productinoptApi.getFinDataList(data).then((res)=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		});
	}
	</script>
	
	<style>
	</style>
	
