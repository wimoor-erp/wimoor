<template >
	<div class="main-sty">
		<div class="con-header">
			<el-row>
				<el-space>
					<el-button @click="handleDownload" :loading="downloading">导出</el-button>
					<el-date-picker
					        v-model="byday"
					        type="date"
							:editable="false"
							:clearable="false"
							@change="changeDate"
					        placeholder="汇率日期"
					      />
				</el-space>
			</el-row>
		</div>
       <el-table :data="tableData">
		<el-table-column label="货币"  prop="symbol" />
		<el-table-column label="货币代码"  prop="name" />
		<el-table-column label="兑换币" >
				<template #default="scope">
					CNY(100元)
					</template>
		</el-table-column>
		<el-table-column label="官方汇率"  prop="price"></el-table-column>
		<el-table-column label="我的当前汇率" prop="myprice" >
			<template #default="scope">
				 <el-popover placement="left" :visible="scope.row.visible" :width="160"  trigger="click">
				      <template #reference>
						  <span class="table-edit-flex">
						  <span v-if="scope.row.showmyprice">{{scope.row.showmyprice}}</span>
						  <el-icon @click="scope.row.visible=true"><Edit/></el-icon>
						  </span>
				      </template>
					  
				      <el-input type="number" v-model="scope.row.myprice"></el-input>
					  <div class="m-t-8">
						  <el-button @click="scope.row.visible=false">取消</el-button>
						  <el-button type="primary" @click="saveMyCurrencyRate(scope.row)">确认</el-button>
					  </div>
				    </el-popover>
			</template>
		</el-table-column>
		<el-table-column label="生效时间" prop="utctime">	</el-table-column>
        </el-table>
	</div>
</template>
<script>
    export default{ name:"汇率管理" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import {Edit} from '@element-plus/icons-vue'
	import exchangeRateAPI from '@/api/amazon/finances/exchangeRateAPI.js';
	import {ElMessage} from 'element-plus';
	const state = reactive({
			tableData:[],
			byday:new Date(),
			downloading:false,
	})
	const{
		tableData,byday,downloading
	}=toRefs(state)
	function changeDate(value){
		loadData();
	}
	function loadData(){
		exchangeRateAPI.getMyCurrencyRate({"byday":state.byday.format("yyyy-MM-dd")}).then(res=>{
			if(res.data&&res.data.length>0){
				res.data.forEach(item=>{
					item.showmyprice=item.myprice;
				})
				state.tableData=res.data;
			}else{
				state.tableData=[];
				ElMessage.info( '未找到汇率，系统会自动使用前一天的汇率' );
			}
			
			
		})
	}
	function handleDownload(){
		state.downloading=true;
		exchangeRateAPI.downFinRate({"byday":state.byday.format("yyyy-MM-dd")},()=>{
			state.downloading=false;
		})
	}
	function saveMyCurrencyRate(row){
		var data=JSON.parse(JSON.stringify(row));
		data.utctime=new Date(data.utctime);
		data.price=data.myprice;
		exchangeRateAPI.saveMyCurrencyRate(data).then(res=>{
			ElMessage.success( '保存成功' );
			row.visible=false;
			row.showmyprice=data.myprice;
		})
	}
	onMounted(()=>{
		loadData();
	})
</script>

<style >
</style>
 