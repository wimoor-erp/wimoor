<template>
	<div class="main-sty">
		<div class="con-header">
			<el-row>
				<el-space>
					<Group ref="groups" @change="changeGroup" />
					 <Datepicker ref="datepickers" @changedate="changedate" />
					<Owner   @owner="ownerchange" />
					<el-input v-model="searchKeywords" clearable @input="searchConfirm" placeholder="请输入" class="input-with-select">
						<template #prepend>
							<el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU"
								style="width: 110px">
								<el-option label="SKU" value="sku"></el-option>
								<el-option label="FNSKU" value="fnsku"></el-option>
								<el-option label="ASIN" value="asin"></el-option>
							</el-select>
						</template>
						<template #append>
							<el-button @click="searchConfirm">
								<el-icon style="font-size: 16px;align-itmes:center">
									<search />
								</el-icon>
				  	</el-button>
						</template>
					</el-input>
					<!-- <el-button>重置</el-button> -->
				</el-space>
				<div class='rt-btn-group'>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
				</div>
			</el-row>
			<!--功能区域 -->
		</div>
		<div class="con-body">
			<Table  ref="tableRef"  />
		</div>
	</div>
</template>

<script>
	import {ref,reactive,onMounted,watch,h} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown} from '@element-plus/icons-vue';
	import Group from '@/components/header/group.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import Owner from '@/components/header/owner.vue';
	import Table from"./components/table.vue";
	export default {
		name:'调价队列',
		components: {
			Help,ArrowDown,
			Search,MenuUnfold,
			Plus, SettingTwo,Table,
			Group,Datepicker,Owner
		},
		setup(props,context) {
			let tableRef=ref();
			let isload=true;
			let selectlabel=ref("sku");
			let searchKeywords=ref("");
			var queryParam={};
			function changeGroup(obj){
				queryParam.groupid=obj.groupid;
				queryParam.marketplaceid=obj.marketplaceid;
				queryParam.searchtype=selectlabel.value;
				queryParam.search=searchKeywords.value;
				tableRef.value.loadData(queryParam);
				isload=false;
			}
			//日期改变
			function changedate(dates){
				queryParam.startDate=dates.start;
				queryParam.endDate=dates.end;
				if(isload==false){
					tableRef.value.loadData(queryParam);
				}
			}
			function ownerchange(val){
				queryParam.operator=val;
				if(isload==false){
					tableRef.value.loadData(queryParam);
				}
			}
			function searchTypeChange(){
				queryParam.searchtype=selectlabel.value;
				if(isload==false){
					tableRef.value.loadData(queryParam);
				}
			}
			function searchConfirm(){
				queryParam.search=searchKeywords.value;
				if(isload==false){
					tableRef.value.loadData(queryParam);
				}
			}
			function stausChange(val){
				
			}
			return {
				//function
				changeGroup,changedate,ownerchange,searchTypeChange,searchConfirm,stausChange,
				
				//value
				selectlabel,searchKeywords,
				//ref
				tableRef
				
			}
		},
	}
</script>

<style>
</style>
