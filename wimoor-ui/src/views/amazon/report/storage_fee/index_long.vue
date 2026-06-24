<template>
 
			<el-row>
				<el-space>
					   <Group  @change="groupChange"    ></Group>
						<Datepicker ref="datepickers" :shortIndex="2" @changedate="changedate" />
						 <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" placeholder="搜索ASIN或平台SKU"></el-input>
		        </el-space>
				<div class='rt-btn-group' style="margin-bottom:10px;">
				 <el-button style="float:right;"  @click="downloadList" :loading="downloading">导出</el-button>
				</div>
			 </el-row>
			 <GlobalTable ref="globalTable"
			  :tableData="tableData"  height="calc(100vh - 270px)" 
			  :defaultSort="{ prop: 'sku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
			   <el-table-column label="平台商品信息" prop="sku"    sortable="custom" show-overflow-tooltip >
			   	<template #default="scope">
			   	<div class="flex-center">
			   		   <img v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  /> 
			   		   <img v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  />
			   		   <div >
			   			   <div  >{{scope.row.name}}</div>
			   			   <p class="sku">{{scope.row.sku}} 
			 				  <span class="font-extraSmall"> ASIN:{{scope.row.asin}}</span>
			   			   </p>
			   		   </div>
			   	</div>
			   	</template>
			   </el-table-column>
			    <el-table-column label="费用时间" prop="snapshot_date"   sortable="custom"  >
						<template #default="scope">
							{{dateTimesFormat(scope.row.snapshot_date)}}
						</template>
				</el-table-column>
				<el-table-column label="数量" prop="qty_charged"   sortable="custom"  >
				</el-table-column>
				<el-table-column label="扣费" prop="amount_charged"   sortable="custom"  >
				</el-table-column>
				<el-table-column label="每单位体积" prop="per_unit_volume"   sortable="custom"  >
				</el-table-column>
				<el-table-column label="费率附加费" prop="rate_surcharge"   sortable="custom"  >
				</el-table-column>
				<el-table-column label="附加费天数" prop="surcharge_age_tier"   sortable="custom"  >
				</el-table-column>
				<el-table-column label="币种" prop="currency"     >
				</el-table-column>
			</template>
			</GlobalTable>
	 
	</template>
	
	<script setup>
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import Group from '@/components/header/group.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import {dateFormat,dateTimesFormat} from '@/utils/index.js';
		import { ElMessage, ElMessageBox} from 'element-plus';
		import storageFeeApi from '@/api/amazon/finances/storageFeeApi.js';
		const globalTable=ref();
		let state=reactive({
			queryParams: {search:""},
			tableData:{records:[],total:0},
			isload:true,
			downloading:false,
		})
		const{
			dateOptions,
			queryParams,
			tableData,
			isload,
			downloading,
		}=toRefs(state)
		
		//日期改变
		function changedate(dates){
			state.queryParams.fromDate=dates.start;
			state.queryParams.toDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		}
		function groupChange(obj){
			state.queryParams.groupid=obj.groupid;
			state.queryParams.marketplaceid=obj.marketplaceid;
			handleQuery();
		}
		
		function handleQuery(){
			 globalTable.value.loadTable(state.queryParams);
		}
		function loadTableData(params){
			  storageFeeApi.list(params).then(res=>{
					 state.tableData.records=res.data.records;
					 state.tableData.total=res.data.total;
					 state.isload=false;
			  })
		}
		
		function downloadList(){
			state.downloading=true;
			storageFeeApi.downloadLongList(state.queryParams,()=>{
				state.downloading=false;
			})
		}
		onMounted(()=>{
			 
		});
	</script>
	
	<style scoped="scoped">
		.app-container{
			padding:20px;
		}
		.img-40{width: 40px;
		height: 40px;flex: none;
		margin-right: 8px;}
	</style>
	



