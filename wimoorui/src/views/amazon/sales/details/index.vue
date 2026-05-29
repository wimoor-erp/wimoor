<template>
	<div class="main-sty">
		<div class="con-header">
			<el-row >
				<el-space>
					<el-button @click.stop="downloadSalesDetail">导出</el-button>
					<el-select v-model="queryParam.summaryType" @change="searchConfirm">
						<el-option label="按日汇总" key="day" value="day"></el-option>
						<el-option label="按周汇总" key="week" value="week"></el-option>
						<el-option label="按月汇总" key="month" value="month"></el-option>
					</el-select>
					<Group @change="getGroup" isproduct="ok" />
					<Owner @owner="getOwner" />
					<Datepicker ref="datepickers" :days="1"  @changedate="changeDate" />
					<el-input v-model="queryParam.search" clearable  placeholder="请输入SKU" class="input-with-select">
						<template #append>
							<el-button @click.stop="searchConfirm">
								<el-icon style="font-size: 16px;align-itmes:center">
									<search />
								</el-icon>
				  	</el-button>
						</template>
					</el-input>
				</el-space>
				<div class='rt-btn-group'>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
				</div>
			</el-row>
		</div>
		<div class="con-body">
		     <GlobalTable  ref="globalTable" :tableData="tableData" :showSummary="true" :summaryMethod="getSummaries" @loadTable="loadtableData"
				:stripe="true" height="calc(100vh - 198px)"
				:defaultSort="defaultSort"    >
				<template #field>
				  <el-table-column fixed  prop="img" label="图片" width="60" >
				   <template #default="scope">
					  <el-popover
					    placement="right"
					    title="商品详情"
					    :width="400"
					    >
					    <template #reference>
					      <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
					    </template>
						<div class="pro-message">
						<el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
						</div>
						<div class="font-small">
							<div>{{scope.row.name}}</div>
							<div class="sku">SKU:<span class="text-primary">{{scope.row.amz_sku}}</span></div>
						</div>
						<el-space class="font-extraSmall" :size="4">
							<span >{{scope.row.ownername}}</span>
						</el-space>
					  </el-popover> 
				  </template>
				</el-table-column>
				<el-table-column fixed  prop="sku" label="名称/MSKU" width="240" show-overflow-tooltip>
				   <template #default="scope">
				      <div class='name'>{{scope.row.name}}</div>
				      <div class='sku'>{{scope.row.sku}}
				      </div>
				  </template>
				</el-table-column>
				<el-table-column label="上架日期" fixed width="100" prop="openDate" sortable="custom"></el-table-column>
			    <el-table-column  label="汇总" fixed  prop="vsum"  sortable="custom"/>
				<el-table-column v-for="item in fieldOptions" max-width="250"  min-width="110" :prop="item.label" sortable="custom">
					<template #header>
						  {{item.name}} 
					</template>
				    <template #default="scope">
						  {{scope.row[item.label]}}
				    </template>
				</el-table-column> 
				</template>
			</GlobalTable>
		</div>
	</div>
</template>
<script>
    export default{ name:"销量详情" };
</script>
<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs,} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,ArrowUp} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import Group from '@/components/header/group.vue';
	import Owner from '@/components/header/owner.vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import salessumApi from "@/api/amazon/summary/salessumApi.js"
	const globalTable=ref(GlobalTable);
	let state = reactive({queryParam:{search:"",summaryType:"day"},fieldOptions:[],
	tableData:{records:[],total:0},
	           defaultSort:{"prop": 'vsum', "order": 'descending' }});
	const {queryParam,fieldOptions,tableData,defaultSort} = toRefs(state);
	let displayVisable =ref(false);
	function searchConfirm(){
		 salessumApi.getOrderSumField(state.queryParam).then(res=>{
			 if(res.data){
				 state.fieldOptions=res.data.reverse();
				 globalTable.value.loadTable(state.queryParam);
			 }
		 });
	}
	function downloadSalesDetail(){
		salessumApi.downloadSalesDetail(state.queryParam);
	}
	function loadtableData(params){
		salessumApi.getOrderData(params).then(res=>{
				 state.tableData.records = res.data.records
				 state.tableData.total =res.data.total
		})
	}
	function getGroup(value){
		state.queryParam.groupid=value.groupid;
		state.queryParam.marketplace=value.marketplaceid;
		searchConfirm();
	}
	function getOwner(value){
		state.queryParam.owner=value;
	}
	function changeDate(value){
		state.queryParam.enddatestr=value.end;
		state.queryParam.fromdatestr=value.start;
	}
	function moreFilter(){
		displayVisable.value =!displayVisable.value
	}
	const getSummaries = (param) => {
	  const { columns, data } = param
	  const sums = []
	  columns.forEach((column, index) => {
	    if (index === 0) {
	        sums[index] = '汇总';
	    }else if(index===1||index===2){
			sums[index] = '';
		}else{
			if(data&&data[0]&&data[0]["sumData"]){
				if(index===3){
					sums[index]=data[0].sumData['汇总'];
				}else{
					sums[index]=data[0].sumData[columns[index].property.replace("v","")];
				}
				
			}else{
				sums[index]=0;
			}
			
		}
      });	
	  return sums
	}
	
</script>

<style>
	.m-b-0{
		margin-bottom:0px!important;
	}
	.v-enter-from{
	    transform: translateY(-20px);
	}
	.v-enter-active{
		 transition: transform 0.5s ease;
	}
	.v-enter-to{
		 transform: translateY(0px);
	}
	.v-leave-from{
	    transform: translateY(0px);
	}
	.v-leave-to{
		 transform: translateY(-10px);
	}
	.v-leave-active{
		 transition: transform 0.3s ease-out;
	}
</style>
