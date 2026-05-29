<template>
	<div class="main-sty" >
		<div class="con-header">
			<el-row>
				<Group @change="getGroup"  ref="groupRef" />
				<el-space>
					<div class="date-picker-group">
						<el-select v-model="queryParams.datetype" style="width:100px" placeholder="结算日期" @change="handleQuery">
							<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
						</el-select>
						<Datepicker ref="datepickers"  :shortIndex="1"  @changedate="changedate" />
					</div>
					<el-select v-model="queryParams.currency"  @change="handleQuery">
						<el-option label="人民币" value="CNY"></el-option>
						<el-option label="美元" value="USD"></el-option>
						<el-option label="站点币种" value="market"></el-option>
					</el-select>
		        </el-space>
				
				<div class='rt-btn-group' >
				 <el-button   @click="downloadList">导出</el-button>
				</div>
				
				
			 </el-row>
			 <el-table :data="tableData" 
			 :row-class-name="rowclassname"
			 v-loading="loading" >
				 <el-table-column prop="name" label="店铺"   >
					 <template #default="scope">
						 <el-space>
						<el-icon v-show="scope.row.pid=='0'" class="ic-cen font-small"><ArrowDownBold /></el-icon> 
						{{scope.row.name}}
						</el-space>
					 </template>
				 </el-table-column >
				 <el-table-column prop="tocurrency" label="币种"  />
				 <el-table-column prop="principal" label="销售额" align="right">
				 </el-table-column>
				 <el-table-column prop="commission" label="销售佣金" align="right">
				 </el-table-column>
				 <el-table-column prop="fbafee" label="FBA费用" align="right">
				 </el-table-column>
				 <el-table-column prop="refund" label="退款金额" align="right">
				 </el-table-column>
				 <el-table-column prop="storagefee" label="仓储费" align="right">
				 </el-table-column>
				 <el-table-column prop="advfee" label="广告费" align="right">
				 </el-table-column>
				 <el-table-column prop="other" label="其它" align="right">
				 </el-table-column>
				 <el-table-column prop="setin" label="结算收入" align="right">
				 </el-table-column>
				 <el-table-column prop="price" label="采购成本" align="right">
				 </el-table-column>
				 <el-table-column prop="profit" label="利润" align="right">
				 </el-table-column>
			 </el-table>
		</div>
		</div>
	</template>
	
	<script setup>
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import Group from '@/views/amazon/listing/common/group.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import { ElMessage, ElMessageBox} from 'element-plus';
		import settlementAccRptApi from '@/api/amazon/finances/settlementAccRptApi.js';
		import { outputmoney} from '@/utils/index.js';
		import {ArrowDownBold} from '@element-plus/icons-vue'
		let state=reactive({
			dateOptions:[{label:'结算日期',value:''},{label:'转账日期',value:'acc'}],
			queryParams:{datetype:"",currency:"CNY"},
			tableData:[],
			loading:false,
			isload:true,
		})
		const{
			dateOptions,
			queryParams,
			tableData,
			loading,
			isload,
		}=toRefs(state)
		
		//日期改变
		function changedate(dates){
			state.queryParams.fromDate=dates.start;
			state.queryParams.endDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		}
		function getGroup(obj){
			state.queryParams.groupid=obj.groupid;
			state.queryParams.marketplaceid=obj.marketplaceid;
			handleQuery();
			state.isload=false;
		}
		
		function handleQuery(){
			//查询表格数据
			state.loading=true;
			settlementAccRptApi.getSettlementOverAll(state.queryParams).then((res)=>{
				state.loading=false;
				if(res.data){
					res.data.forEach(item=>{
						item.principal=item.principal?outputmoney(item.principal):item.principal;
						item.commission=item.commission?outputmoney(item.commission):item.commission;
						item.fbafee=item.fbafee?outputmoney(item.fbafee):item.fbafee;
						item.refund=item.refund?outputmoney(item.refund):item.refund;
						item.storagefee=item.storagefee?outputmoney(item.storagefee):item.storagefee;
						item.advfee=item.advfee?outputmoney(item.advfee):item.advfee;
						item.other=item.other?outputmoney(item.other):item.other;
						item.setin=item.setin?outputmoney(item.setin):item.setin;
						item.price=item.price?outputmoney(item.price):item.price;
						item.profit=item.profit?outputmoney(item.profit):item.profit;
					})
				}
				state.tableData=res.data;
			}).catch(e=>{state.loading=false;});
		}
		
		function downloadList(){
			settlementAccRptApi.downloadSettlementOverAll(state.queryParams);
		}
		function rowclassname({row}){
			if(row.pid=='0'){
				return 'sumrowclass'
			}
		}
		onMounted(()=>{
			 
		});
	</script>
	
	<style >
		.sumrowclass{
			background:#f1f1f1!important;
			font-weight:700;
		}
		.dark .sumrowclass{
			background:#080808!important;
			font-weight:700;
		}
	</style>
	



