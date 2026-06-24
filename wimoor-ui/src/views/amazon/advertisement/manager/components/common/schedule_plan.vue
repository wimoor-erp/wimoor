<template>
	<el-dialog
	title="分时策略"
	v-model="visible"
	width="80%"
	>
	
	<el-form v-model="from" >
		<el-form-item label="策略名称" >
			<el-space :size="32">
			<el-switch
			    v-model="from.useTem"
				size="small"
			    active-text="使用模板"
			  />
			  <el-input v-if="!from.useTem" v-model="from.name" placeholder="策略名称"></el-input>
			 <el-select v-else v-model="from.value" @change="loadSelectRule" placeholder="请选择分时策略" >
			    <el-option
			      v-for="item in planOptions"
			      :key="item.ruleId"
			      :label="item.name"
			      :value="item.ruleId"
			    />
			  </el-select>
			  </el-space>
		</el-form-item>
		<el-form-item v-if='nowRow.ftype!="add"' label="在用策略" >
			<div  v-loading="rulesloading">
			    <el-checkbox   v-for="item in useRuleList" @change="changeUseRuleItem(item)"  :checked="item.isuse"  v-model="item.isuse"    >{{item.name}}</el-checkbox>
			</div>
		</el-form-item>
		<el-form-item label="策略状态" >
			<el-switch
			    size="small"
			    v-model="from.state"
				active-text="开启"
				inactive-text="暂停"
				inline-prompt
			  />
		</el-form-item>
		<el-form-item v-if="from.useTem" label="策略名称" >
			 <el-input style="width:200px;" v-model="from.rulename" ></el-input>
		</el-form-item>
		<el-form-item label="起止时间" >
			<el-row>
			<el-date-picker
			        v-model="from.date"
			        type="daterange"
			        range-separator="-"
			        start-placeholder="开始时间"
			        end-placeholder="结束时间"
			      />
				  &nbsp;&nbsp;<span class="font-extraSmall">非必填</span>
			</el-row>	  
		</el-form-item>
		<el-form-item >
			<div class="flex1">
			<el-tabs
			    v-model="from.tab"
			    type="card"
			    class="demo-tabs"
			    @tab-click="handleClick"
			  >
			   <!-- <el-tab-pane label="分时竞价" disabled name="1"></el-tab-pane> -->
			    <el-tab-pane label="分时预算" name="2"></el-tab-pane>
				</el-tabs>
				<el-space direction="vertical" class="widthfill" :fill="true" :size="16">
			 <el-alert class="clear-lineheight"  type="info" :closable="false"> 
			 <p>1.分时竞价执行中，手动修改竞价或者亚马逊后台修改竞价，将以后者修改的竞价为原有竞价。</p>
			 <p>2.指定日期的分时竞价规则会比重复周期的规则优先执行</p>
			 </el-alert>		
			<el-table :data="tableData">
				<el-table-column label="触发类型" width="140">
				  <template #default="scope">
					<el-select  v-model="scope.row.trigger"  >
					   <el-option
					     v-for="item in scope.row.triggerOptions"
					     :key="item.value"
					     :label="item.name"
					     :value="item.value"
					   />
					 </el-select>
				  </template>
				</el-table-column>
				<el-table-column label="执行日期" width="280px">
					<template #default="scope">
						<el-select v-if="scope.row.trigger=='1'"  v-model="scope.row.date"  placeholder="选择日期区间"  multiple clearable>
						   <el-option
							 v-for="item in scope.row.dateOptions"
							 :key="item.value"
							 :label="item.name"
							 :value="item.value"
						   />
						 </el-select>
						 <el-date-picker
						 v-else
						         :disabled="true"
						         v-model="scope.row.daterange"
						         type="date"
						         placeholder="选择日期区间"
						       />
					</template>
				</el-table-column>
				<el-table-column label="时间段" width="300px">
					<template #default="scope">
						<el-space  >
						<el-time-select
						      v-model="scope.row.startTime"
						      :max-time="scope.row.endTime"
						      placeholder="开始时间"
						      start="1:00"
						      step="1:00"
						      end="24:00"
						    />
						    <el-time-select
						      v-model="scope.row.endTime"
						      :min-time="scope.row.startTime"
						      placeholder="结束时间"
						      start="1:00"
						      step="1:00"
						      end="24:00"
						    />
							</el-space>
					</template>
				</el-table-column>
				<!-- <el-table-column label="竞价规则">
				  <template #default="scope">
					<el-select  v-model="scope.row.rule"  >
					   <el-option
					     v-for="item in scope.row.ruleOptions"
					     :key="item.value"
					     :label="item.name"
					     :value="item.value"
					   />
					 </el-select>
				  </template>
				</el-table-column> -->
				<el-table-column label="更新预算规则-预算的值">
					<template #default="scope">
						<!-- <el-input v-if="scope.row.rule=='1'" v-model="scope.row.bidding">
							  <template #prepend>$</template>
						</el-input> -->
						<el-input  v-model="scope.row.rate"  @input="scope.row.rate=CheckInputFloat(scope.row.rate)">
							  <template #append>%</template>
						</el-input>
					</template>
				</el-table-column>
				<!-- <el-table-column label="操作" width="80px">
				  <template #default="scope">
				    <el-link v-if="scope.$index==0" @click="handleAdd" :underline="false" type="primary" class="font-extraLarge">
						<el-icon><CirclePlus /></el-icon>
					</el-link>
					<el-link v-else  @click="handleDelete(scope.$index)" :underline="false" type="primary" class="font-extraLarge">
						<el-icon><Minus /></el-icon>
					</el-link>
				  </template>
				</el-table-column> -->
			</el-table>
			</el-space>
			</div>
		</el-form-item>
	</el-form>
	<template #footer>
		<el-button @click="visible=false">取消</el-button>
		<el-button v-if="!from.useTem" @click="saveBudgetRule('save')">保存为模板</el-button>
		<el-button type="primary" v-if="!from.useTem && nowRow.ftype!='add'" @click="saveBudgetRule('use')">保存并应用</el-button>
		<el-button  v-if="from.useTem" @click="updateBudgetRule">更新模板</el-button>
		<el-button type="primary" v-if="from.useTem && nowRow.ftype!='add'" @click="useBudgetRule">应用模板</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs} from"vue";
	import {CirclePlus,Minus} from '@element-plus/icons-vue';
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import {ElDivider,ElMessageBox,ElMessage} from 'element-plus';
	import advBudgetRuleApi from '@/api/amazon/advertisement/report/advBudgetRuleApi.js';
	
	
	const state = reactive({
		useRuleList:[],
		visible:false,
		planOptions:[],
		rulesloading:true,
		from:{
			name:'',
			value:null,
			useTem:false,
			state:true,
			date:'',
			tab:'2',
		},
		tableData:[
			{
			 trigger:'2', 
			 triggerOptions:[{name:'重复周期',value:'1'},{name:'每日重复',value:'2'}],
			 date:['MONDAY'],
			 daterange:null,
			 dateOptions:[
				 {name:'周一',value:'MONDAY'},
				 {name:'周二',value:'TUESDAY'},
				 {name:'周三',value:'WEDNESDAY'},
				 {name:'周四',value:'THURSDAY'},
				 {name:'周五',value:'FRIDAY'},
				 {name:'周六',value:'SATURDAY'},
				 {name:'周日',value:'SUNDAY'},
				 ],
			 ruleOptions:[
				 {name:'固定竞价',value:'1'},
				 {name:'增加%',value:'2'},
				 {name:'减少%',value:'3'},
				 ],
				 rule:'1',
			},
		],
		nowRow:null,
		
	})
	const{
		visible,
		from,
		planOptions,
		tableData,
		nowRow,
		useRuleList,
		rulesloading,
	}=toRefs(state)
	defineExpose({
		show,
	})
	
	function loadUseRuleList(){
		state.rulesloading=true;
		var params={"campaignId":state.nowRow.campaignId};
		if(state.nowRow.profileid){
			advBudgetRuleApi.amzGetBudgetRulesByCampaign(state.nowRow.profileid,state.nowRow.campaignType,params).then((res)=>{
				res.data.forEach(item=>{
					item.isuse=true;
				});
				state.useRuleList=res.data;
				state.rulesloading=false;
			});
		}else{
			state.rulesloading=false;
		}
		
	}
	function show(row){
		state.visible = true;
		state.nowRow=row;
		if(row.profileid){
			advBudgetRuleApi.getAdvBudgetRuleByProfileid(row.profileid,row.campaignType).then((res)=>{
				state.planOptions=res.data;
			});
		}
		if(row.ftype!="add"){
			loadUseRuleList();
		}
	}
	
	function changeUseRuleItem(item){
		//做判断 是use还是delete
		if(item.isuse==false){
			//delete
			advBudgetRuleApi.amzDeleteCampaignBudgetRule(item.profileid,state.nowRow.campaignType,
			{"campaignId":state.nowRow.campaignId,"budgetRuleId":item.ruleId}).then((res)=>{
				ElMessage.success("当前广告活动已解除此规则！");
				loadUseRuleList();
			});
		}else{
			//应用
			advBudgetRuleApi.amzPostBudgetRulesByCampaign(item.profileid,state.nowRow.campaignType,
			{"campaignId":state.nowRow.campaignId,"ruleid":item.ruleId}).then((res)=>{
				ElMessage.success("当前广告活动应用此规则成功！");
				loadUseRuleList();
			});
		}
	}
	function handleDelete(index){
		state.tableData.splice(index,1);
	}
	
	function handleAdd(){
		state.tableData.push({
			trigger:'2',
			triggerOptions:[{name:'重复周期',value:'1'},{name:'每日重复',value:'2'}], 
			date:['MONDAY'],
			daterange:null,
			dateOptions:[
							 {name:'周一',value:'MONDAY'},
							 {name:'周二',value:'TUESDAY'},
							 {name:'周三',value:'WEDNESDAY'},
							 {name:'周四',value:'THURSDAY'},
							 {name:'周五',value:'FRIDAY'},
							 {name:'周六',value:'SATURDAY'},
							 {name:'周日',value:'SUNDAY'},
							 ],
			ruleOptions:[
							 {name:'固定竞价',value:'1'},
							 {name:'增加%',value:'2'},
							 {name:'减少%',value:'3'},
							 ],
							 rule:'1',
		})
	}
	//保存预算rules
	function saveBudgetRule(ftype){
		var startTime=state.from.date[0];
		var endTime=state.from.date[1];
		var startStr= dateFormat(startTime).replaceAll("-","");
		var endStr= dateFormat(endTime).replaceAll("-","");
		var profileid=state.nowRow.profileid;
		var type=state.nowRow.campaignType;
		var budgetRulesDetails=[];
		if(state.tableData && state.tableData.length>0){
			state.tableData.forEach(item=>{
				if(item.rate && state.from.name){
					var datas={
						"ruleType":"SCHEDULE",
						"budgetIncreaseBy":{
						   "type":"PERCENT",
						   "value": formatFloat(item.rate)
						},
						"name":state.from.name,
						// "performanceMeasureCondition": {
						// 	"metricName": "ACOS",
						// 	"comparisonOperator": "GREATER_THAN",
						// 	"threshold": 0.1
						// }
					};
					if(startStr && startStr.length>3){
						datas.duration={
							"dateRangeTypeRuleDuration":{
								"startDate":startStr,
								"endDate":endStr
							}
						};
					}
					if(item.trigger=='1'){
						 if(item.date && item.date.length>0){
							 datas.recurrence={
							 	"daysOfWeek":item.date
							 };
							if(item.startTime && item.startTime.length>3){
								datas.recurrence.intraDaySchedule=[{
									 "startTime":item.startTime+":00",
									 "endTime":item.endTime+":00",
								}];
							}
						 }
					}else{
						datas.recurrence={
							"type":"DAILY"
						};
						if(item.startTime && item.startTime.length>3){
							datas.recurrence.intraDaySchedule=[{
								 "startTime":item.startTime+":00",
								 "endTime":item.endTime+":00",
							}];
						}
					}
					budgetRulesDetails.push(datas);
				}else{
					ElMessage.error("请输入预算的百分比值或模板名称！");
				}
				
			});
		}
		
		var params={"budgetRulesDetails":budgetRulesDetails};
		advBudgetRuleApi.amzCreateBudgetRules(profileid,type,params).then((res)=>{
			if(res.data && res.data.length>0){
				ElMessage.success("保存预算模板成功！");
				if(ftype=="use"){
					var ruleid=res.data[0].ruleId;
					advBudgetRuleApi.amzPostBudgetRulesByCampaign(profileid,type,{"campaignId":state.nowRow.id,"ruleid":ruleid}).then((res)=>{
						ElMessage.success("预算规则应用成功！");
					});
				}
			}else{
				ElMessage.error("保存预算模板失败！");
			}
		});
	}
	 
	//应用模板
	function useBudgetRule(){
		var ruleid= state.from.value;
		advBudgetRuleApi.amzPostBudgetRulesByCampaign(state.nowRow.profileid,state.nowRow.campaignType,
		{"campaignId":state.nowRow.campaignId,"ruleid":ruleid}).then((res)=>{
			ElMessage.success("当前广告活动应用此规则成功！");
			loadUseRuleList();
		});
	}
	//更新模板
	function updateBudgetRule(){
		var startTime=state.from.date[0];
		var endTime=state.from.date[1];
		var startStr= dateFormat(startTime).replaceAll("-","");
		var endStr= dateFormat(endTime).replaceAll("-","");
		var profileid=state.nowRow.profileid;
		var type=state.nowRow.campaignType;
		var budgetRulesDetails=[];
		if(state.tableData && state.tableData.length>0){
			state.tableData.forEach(item=>{
				if(item.rate && state.from.rulename){
					var states="ACTIVE";
					if(state.from.state==false){
						states="PAUSED";
					}
					var datas={
						"ruleType":"SCHEDULE",
						"budgetIncreaseBy":{
						   "type":"PERCENT",
						   "value": formatFloat(item.rate)
						},
						"name":state.from.rulename,
						// "performanceMeasureCondition": {
						// 	"metricName": "ACOS",
						// 	"comparisonOperator": "GREATER_THAN",
						// 	"threshold": 0.1
						// }
					};
					if(startStr && startStr.length>3){
						datas.duration={
							"dateRangeTypeRuleDuration":{
								"startDate":startStr,
								"endDate":endStr
							}
						};
					}
					if(item.trigger=='1'){
						 if(item.date && item.date.length>0){
							 datas.recurrence={
							 	"daysOfWeek":item.date,
								"type":"DAILY"
							 };
							if(item.startTime && item.startTime.length>3){
								datas.recurrence.intraDaySchedule=[{
									 "startTime":item.startTime+":00",
									 "endTime":item.endTime+":00",
								}];
							}
						 }
					}else{
						datas.recurrence={
							"type":"DAILY"
						};
						if(item.startTime && item.startTime.length>3){
							datas.recurrence.intraDaySchedule=[{
								 "startTime":item.startTime+":00",
								 "endTime":item.endTime+":00",
							}];
						}
					}
					var details={
						"ruleId":state.from.value,
						"ruleState":states,
						"ruleDetails":datas
						};
					budgetRulesDetails.push(details);
				}else{
					ElMessage.error("请输入预算的百分比值或模板名称！");
				}
				
			});
		}
		var params={"budgetRulesDetails":budgetRulesDetails};
		advBudgetRuleApi.amzUpdateBudgetRules(state.nowRow.profileid,state.nowRow.campaignType,params).then((res)=>{
			if(res.data && res.data.length>0){
				ElMessage.success("预算规则更新成功！");
			}else{
				ElMessage.erroe("预算规则更新失败！");
			}
		});
	}
	//加载数据
	function loadSelectRule(){
		var ruleid= state.from.value;
		var ruleRow=null;
		state.planOptions.forEach(item=>{
			if(item.ruleId==ruleid){
				ruleRow=item;
			}
		});
		if(ruleRow){
			var rates=JSON.parse(ruleRow.budgetIncreaseBy);
			var rence= ruleRow.recurrence;
			var duration=JSON.parse(ruleRow.duration);
			if(rence.indexOf("DAILY")){
				state.tableData[0].trigger="2";
			}else{
				state.tableData[0].trigger="1";
			}
			state.tableData[0].rate=rates.value;
			if(duration){
				var ruleDuration=duration.dateRangeTypeRuleDuration;
				var startDate=ruleDuration.startDate;
				var endDate=ruleDuration.endDate;
				var startDate1=startDate.substring(0,4);
				var startDate2=startDate.substring(4,6);
				var startDate3=startDate.substring(6,8);
				var endDate1=endDate.substring(0,4);
				var endDate2=endDate.substring(4,6);
				var endDate3=endDate.substring(6,8);
				state.from.date=[];
				state.from.date.push(new Date(startDate1+"-"+startDate2+"-"+startDate3).getTime());
				state.from.date.push(new Date(endDate1+"-"+endDate2+"-"+endDate3).getTime());
			}
			if(ruleRow.ruleState=="ACTIVE" || ruleRow.ruleState=="active"){
				state.from.state=true;
			}else{
				state.from.state=false;
			}
			state.from.rulename=ruleRow.name;
			// if(rence.intraDaySchedule){
			// 	var Schedule=rence.intraDaySchedule[0];
			// }
			
			
		}
		
	}
	
</script>

<style>
	.flex1{
		flex:1;
	}
	.widthfill{
		width:100%;
	}
	.clear-lineheight{
		line-height:inherit;
	}
</style>