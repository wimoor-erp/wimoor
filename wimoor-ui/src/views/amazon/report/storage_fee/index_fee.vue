<template>

			<el-row>
				<el-space>
					   <Group  @change="groupChange"    ></Group>
						<el-date-picker
						          v-model="queryParams.searchDate"
						          type="month"
						          placeholder="选择月份"
								  @change="changedate"
						        />
						 <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" placeholder="搜索ASIN或平台SKU"></el-input>
		        </el-space>
				
				<div class='rt-btn-group' style="margin-bottom:10px;">
				<el-button style="float:right;"  @click="downloadList" :loading="downloading">导出</el-button>
				<el-button style="float:right;"  @click="uploadRptFile"  >导入</el-button>
				</div>
			 </el-row>
			 <GlobalTable ref="globalTable"
			  :tableData="tableData"  height="calc(100vh - 270px)" 
			  :defaultSort="{ prop: 'sku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
			  <el-table-column label="平台商品信息" prop="psku"    sortable="custom" show-overflow-tooltip >
			  	<template #default="scope">
			  	<div class="flex-center">
			  		   <img v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  /> 
			  		   <img v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  />
			  		   <div >
			  			   <div  >{{scope.row.name}}</div>
			  			   <p class="sku">{{scope.row.sku}} 
			  			  	  <span class="font-extraSmall"> ASIN:{{scope.row.asin}}</span>
							  <span class="font-extraSmall" style="padding-left:5px;"> 币种：</span>{{scope.row.currency}}
			  			   </p>
						    <div ><span class="font-extraSmall"> 月份:</span>{{scope.row.month}} <span style="padding-left:20px;"><span class="font-extraSmall"> 收货中心:</span>{{scope.row.fulfillment_center}}</span></div>
			  		   </div>
			  	</div>
			  	</template>
			  </el-table-column>
				<el-table-column label="尺寸" prop="longest_side"  width="180"  sortable="custom"  >
							<template #default="scope">
								<div>{{scope.row.longest_side}}*{{scope.row.median_side}}*{{scope.row.shortest_side}}
								<span class="font-extraSmall">{{scope.row.measurement_units}}</span></div>
								<div v-if="scope.row.product_size_tier" class="font-extraSmall">{{scope.row.product_size_tier}}</div>
								<div>  <span class="font-extraSmall"> 重:</span>{{scope.row.weight}}
								 <span v-if="scope.row.weight_units=='inches'" class="font-extraSmall">
									 磅
								 </span>
								 <span v-else class="font-extraSmall">
								 	千克							 
								 </span>
								 </div>
							</template>
					</el-table-column>
					<el-table-column label="日均" 
					prop="average_quantity_on_hand" 
					width="160"  
					sortable="custom"  >
					<template #header>
					<el-space :size="4">
					  <span>日均</span>
					   <el-tooltip
					          effect="dark"
					          content=""
					          placement="top-start"
					        >
							 <template #content> <div>日均储存:商品在亚马逊运营中心的日均储存数量。该值等于过去一个月的库存总体积除以该月包含的天数。</div>
							  <div>日均购买：买家已订购但尚未从运营中心发货的商品的日均数量。</div>
							    <div>日均移除：平均每天请求从亚马逊运营中心移除的商品数量。</div>
							  </template>
							
							
					          <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
					        </el-tooltip>
					</el-space>
					</template>
					<template #default="scope">
						<div><span class="font-extraSmall">日均存储：</span>{{scope.row.average_quantity_on_hand}}  </div>
						<div><span class="font-extraSmall">日均购买：</span>{{scope.row.average_quantity_customer_orders}}  </div>
						<div><span class="font-extraSmall">日均移除：</span>{{scope.row.average_quantity_pending_removal}}  </div>
					</template>
					
					</el-table-column>
					<el-table-column label="仓储利用率" prop="storage_utilization_ratio" width="160"   sortable="custom"  >
						<template #header>
						<el-space :size="4">
						  <span>仓储利用率</span>
						   <el-tooltip
								  effect="dark"
								  placement="top-start"
								>
								<template #content> <div>仓储利用率:商品尺寸分段在当月最后一天的仓储利用率。仓储利用率的计算方法是过去 13 周您的库存平均每日占用体积（以立方英尺为单位）除以平均每日发货体积（以立方英尺为单位）。</div>
								 </template>
								  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
								</el-tooltip>
						</el-space>
						</template>
						<template #default="scope">
							<div> {{scope.row.storage_utilization_ratio}} {{scope.row.storage_utilization_ratio_units}} </div>
						</template>
					</el-table-column>
					<el-table-column label="仓储费" prop="monthly_storage_fee" width="120"   sortable="custom"  >
						<template #header>
						<el-space :size="4">
						  <span>仓储费</span>
						   <el-tooltip
								  effect="dark"
								  placement="top-start"
								>
								<template #content>  
								 <div>仓储费：每个 ASIN 的预计月度仓储费，计算方法为基本费率和使用附加费率的总和乘以商品的总体积。</div>
								 <div>基本费率:SKU 应该申请的仓储费</div>
								 <div>总体积：现有库存的平均数量减去待移除商品的平均数量，所得的值乘以商品体积"</div>
								 </template>
								  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
								</el-tooltip>
						</el-space>
						</template>
						<template #default="scope">
							<div> {{scope.row.monthly_storage_fee}}  </div>
							<div class="font-extraSmall">基本费率:{{scope.row.base_rate}} </div>
							<div class="font-extraSmall">总体积:{{scope.row.estimated_total_item_volume}} </div>
						</template>
					</el-table-column>
					<el-table-column label="仓储费奖励" prop="total_incentive_fee_amount" width="130"   sortable="custom"  >
						<template #header>
						<el-space :size="4">
						  <span>仓储费奖励</span>
						   <el-tooltip
								  effect="dark"
								  placement="top-start"
								>
								<template #content>  
								 <div>仓储费奖励：适用于此 ASIN 的月度仓储费奖励金额。</div>
								 <div>类型:适用于此 ASIN 的奖励类型的描述。</div>
								 </template>
								  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
								</el-tooltip>
						</el-space>
						</template>
						<template #default="scope">
							<div> {{scope.row.total_incentive_fee_amount}}  </div>
							<div class="font-extraSmall">类型:{{scope.row.breakdown_incentive_fee_amount}} </div>
						</template>
					</el-table-column>
					<el-table-column label="体积" prop="item_volume" width="130"   sortable="custom"  >
						<template #header>
						<el-space :size="4">
						  <span>体积</span>
						   <el-tooltip
								  effect="dark"
								  placement="top-start"
								>
								<template #content>  
								 <div>库存仓储费按体积收取。体积计算方法为“最长边 x 次长边 x 最短边”，单位为立方英尺。这些边通常对应商品的长、宽和高</div>
								 </template>
								  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
								</el-tooltip>
						</el-space>
						</template>
						<template #default="scope">
							<div> {{scope.row.item_volume}}  <br>{{scope.row.volume_units}} </div>
						</template>
					</el-table-column>
					<el-table-column label="附加费金额" prop="utilization_surcharge_rate" width="130"   sortable="custom"  >
						<template #header>
						<el-space :size="4">
						  <span>附加费金额</span>
						   <el-tooltip
								  effect="dark"
								  placement="top-start"
								>
								<template #content>  
								 <div>适用于此 ASIN 的仓储利用率附加费金额。此费率取决于商品尺寸分段和仓储利用率。</div>
								 </template>
								  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
								</el-tooltip>
						</el-space>
						</template>
						<template #default="scope">
							<div> {{scope.row.utilization_surcharge_rate}}   </div>
						</template>
					</el-table-column>
					 
			</template>
			</GlobalTable>
	<UploadRpt ref="uploadRptRef"></UploadRpt>
	</template>
	
	<script setup>
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import Group from '@/components/header/group.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import UploadRpt from '@/components/Upload/uploadRpt.vue';
		import { ElMessage, ElMessageBox} from 'element-plus';
		import storageFeeApi from '@/api/amazon/finances/storageFeeApi.js';
		import {InfoFilled} from '@element-plus/icons-vue';
		const globalTable=ref();
		var start = new Date();
		start.setTime(start.getTime() - 3600 * 1000 * 24 * (31));
		let state=reactive({
			queryParams: {search:"",searchDate:start.format("yyyy-MM")},
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
		const uploadRptRef=ref();
		function uploadRptFile(){
			uploadRptRef.value.show("GET_FBA_STORAGE_FEE_CHARGES_DATA");
		}
		//日期改变
		function changedate(dates){
			state.queryParams.searchDate=dates.format("yyyy-MM");
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
			  storageFeeApi.storageList(params).then(res=>{
					 state.tableData.records=res.data.records;
					 state.tableData.total=res.data.total;
					 state.isload=false;
			  })
		}
		
		function downloadList(){
			state.downloading=true;
			storageFeeApi.downloadList(state.queryParams,()=>{
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
	



