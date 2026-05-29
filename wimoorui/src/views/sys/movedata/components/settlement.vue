<template>
		
		<div class="con-body">
			<el-row :gutter="20">
				<el-col :span="12">
					<div class="con-header">
						<el-row>
							<el-space>
							<div>数据保留天数（天）</div>
							<el-input v-model="savedays" @input="handleChange" placeholder="保留天数" clearable></el-input>
							<div>
                    <span class="font-extraSmall">已经迁移账期数： </span>
										<span  >{{ beforeSummary.hasmove}}</span>
							</div> 
							<div>
                    <span class="font-extraSmall">本次需要迁移账期数： </span>
										<span  >{{ beforeSummary.needmove }}</span>
							</div> 
							<el-button @click="handleAdd" :loading="btnloading"  >开始迁移队列</el-button>
							</el-space>
					    </el-row>
					</div>
					
				  <el-table :data="needMove"  v-loading="loading"
				            height="calc(100vh - 200px)"  
						 >
					<el-table-column property="settlement_id" label="账期ID" />
					<el-table-column property="deposit_date" label="转账日期"  />
						<el-table-column property="marketplace_name" label="站点"  />
					<el-table-column property="settlement_start_date" label="账期开始日期"  />
					<el-table-column property="settlement_end_date" label="账期结束日期"  />
					<el-table-column property="capturetime" label="抓取时间时间"  />

						<el-table-column property="deposit_date" label="迁移状态"   >
							<template #default="scope">
								 <el-tag v-if="scope.row.status == 'DONE'"  type="success">
									 已迁移
								</el-tag>
								<el-tag v-else-if="scope.row.status == 'DONING'"  type="danger">
									 迁移中
								</el-tag>
								<el-tag v-else type="info">
									 未迁移
								</el-tag>
							</template>
						</el-table-column>
				  </el-table>
				</el-col>
				<el-col :span="12">
					<div class="con-header">
						<el-row>
							<div style="padding-bottom:15px">
							<el-space>
								 <el-button type="success" @click="loadSettlement">刷新进度</el-button>
								<el-button type="primary" @click="startTask">重启队列</el-button>
							 	<div>
                    <span class="font-extraSmall">已经迁移账期数： </span>
										<span  >{{ treatSummary.hastreat }}</span>
							</div> 
							<div>
                    <span class="font-extraSmall">本次需要迁移账期数： </span>
										<span  >{{ treatSummary.needtreat }}</span>
							</div> 
								<el-button type="danger" @click="handleClear">清除</el-button>
							</el-space>
						
						  
						</div>
						 <el-table :data="treatlist" v-loading="loading" height="calc(100vh - 200px)" 	:default-sort="{ prop: 'table_size', order: 'descending' }">
									<el-table-column property="settlementid" label="账期ID" />
					       <el-table-column property="deposit_date" label="转账日期"  />
								<el-table-column property="status" label="迁移状态" width="100" >
							<template #default="scope">
								 <el-tag v-if="scope.row.status == 2"  type="success">
									 已迁移
								</el-tag>
								<el-tag v-else-if="scope.row.status == 1"  type="danger">
									 迁移中
								</el-tag>
								<el-tag v-else type="info">
									 未迁移
								</el-tag>
							</template>
						</el-table-column>
								<el-table-column  property="startdate"  label="开始时间" sortable />
								<el-table-column  property="enddate"  label="结束时间" sortable  />
								</el-table>
					    </el-row>
					</div>
				</el-col>
			 
		  </el-row>
		</div>
</template>

<script setup>
import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
import {Search,ArrowDown,} from '@element-plus/icons-vue';
import { ElMessage ,ElMessageBox} from 'element-plus';
import dataMoveApi from "@/api/sys/tool/dataMoveApi.js";
const state = reactive({ loading: false,btnloading:false, savedays: "365", needMove: [], beforeSummary: {}, treatSummary: {},treatlist:[]});
const { savedays,needMove,beforeSummary,loading,treatSummary,treatlist,btnloading } = toRefs(state);
function handleChange(){
		loadSettlement();
}
function handleAdd() { 
	state.btnloading=true;
	dataMoveApi.moveSettlement({ "savedays": state.savedays }).then(res => {
		  state.btnloading=false;
			ElMessage.success("插入队列成功");
			loadSettlement();
	 
	});
}
function handleClear(){
	dataMoveApi.clearSettlement().then(res => {
			ElMessage.success("清除成功");
			loadSettlement();
	});
}
function startTask(){
	dataMoveApi.startmoveSettlement().then(res => {
			ElMessage.success("重启成功");
			loadSettlement();
	});
}
function loadSettlement() {
	state.loading=true;
	dataMoveApi.querySettlement({ "savedays": state.savedays }).then(res => {
		state.beforeSummary	 = res.data.beforeSummary;
		state.needMove = res.data.needMove;
		state.treatSummary = res.data.treatSummary;
		state.treatlist = res.data.treatlist;
		state.loading=false;
	});
}
onMounted(()=>{
	loadSettlement();
})

 
</script>

<style>
</style>