<template>
	<el-dialog title="评论分析" 
	           class="feedbackdialog" 
			   v-model="dialog.visible" 
			   @close="handleClose" 
			   @opened = "handleOpen" 
			   width="80%"
			   :append-to-body="true" >
	         <div class="con-header">
	         	<el-row  :gutter="32">
	         		<el-col :span="24">
	         			<div class="flex-center">
	         				   <img 
	         				   :src="row.image"   
	         				   style="width:80px;margin-right:10px"/>
	         				<el-space direction="vertical" alignment="left">
	         				 <div class="font-bold">{{row.sku}}</div>
	         				 <div class="font-small">{{row.name}}</div>
	         				 <el-space class="font-extraSmall">
	         					 <span>ASIN：{{row.asin}}</span>
	         					 <span>站点：{{row.marketplacename}}</span>
								
	         				 </el-space>
						
	         				 </el-space>
	         			 </div>
	         			 </el-col>
						 <el-col  :span="24" >
						<el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleClick">
						    <el-tab-pane label="评论" name="getItemReviewTopics"> </el-tab-pane>
							<el-tab-pane label="评论历史" name="getItemReviewTrends"></el-tab-pane>
							<el-tab-pane label="分类" name="getBrowseNodeReviewTopics"></el-tab-pane>
						</el-tabs>
						<div v-loading="loading">
							<div v-if="'getItemReviewTopics'==activeName">
							<div class="flex-between" style="margin-bottom: 10px;">
								
								  <el-radio-group  v-model="topics" size="small">
								      <el-radio-button v-for="item in topicsOptions" :label="item.value"   >{{item.label}}</el-radio-button>
								    </el-radio-group>
									<el-select size="small" v-model="sortby" @change="handleQuery">
												 <el-option label="提及" value="MENTIONS">
													提及-<span class="font-extraSmall">根据 ASIN 客户评论中提及次数计算得出的热门正面和负面话题。</span>
												 </el-option>
												 <el-option label="星级评价影响" value="STAR_RATING_IMPACT">
													星级评价影响-<span class="font-extraSmall">根据对 ASIN 星级评定的影响来计算最正面和最负面的话题。</span>
												 </el-option>
									</el-select>
								 
								<el-space v-if="itemReviewTopics&&itemReviewTopics.dateRange">
									<div>
								        <span class="font-extraSmall">开始日期：</span> {{itemReviewTopics.dateRange.startDate.year}}-{{itemReviewTopics.dateRange.startDate.monthValue}}-{{itemReviewTopics.dateRange.startDate.dayOfMonth}}		
									</div>
									<div>
										<span class="font-extraSmall">结束日期：</span>  {{itemReviewTopics.dateRange.endDate.year}}-{{itemReviewTopics.dateRange.endDate.monthValue}}-{{itemReviewTopics.dateRange.endDate.dayOfMonth}}
									</div>
								</el-space>
							</div>
								<el-table :default-expand-all="false"
									v-if="itemReviewTopics&&itemReviewTopics.topics"
									ref="tableRef"
									@row-click="rowClick"
									:data="topics=='negativeTopics'?itemReviewTopics.topics.negativeTopics:itemReviewTopics.topics.positiveTopics">
									<el-table-column type="expand">
									   <template #default="props">
									       <el-table :data="props.row.subtopics"  >
									         <el-table-column label="子评论点"prop="subtopic" />
									         <el-table-column label="表现" prop="metrics" >
																<template #default="scope">
																	 <div>评论人数:{{scope.row.metrics.numberOfMentions}}</div>
																	 <div>发生率：{{scope.row.metrics.occurrencePercentage}}</div>
																	 </template>
																</el-table-column>
									         <el-table-column label="评论片段" prop="reviewSnippets"  >
												<template #default="scope">
													 <div>
														<div style="border-bottom:1px dashed #dedede"
														 v-for="subitem in scope.row.reviewSnippets">
														 {{subitem}}</div>
													 </div>
												</template>
									          </el-table-column>
									       </el-table>
									   </template>
									 </el-table-column>
									<el-table-column prop="topic" width="140" label="评论点"></el-table-column>
									<el-table-column  label="表现" width="140">
										 <template #default="scope">
										<div v-if="scope.row.asinMetrics">
											<div>评论人数:{{scope.row.asinMetrics.numberOfMentions}}</div>
											<div>发生率：{{scope.row.asinMetrics.occurrencePercentage}}</div>
											<div>星级影响：{{scope.row.asinMetrics.starRatingImpact}}</div>
										</div>
										 <div v-else>--</div>
										 </template>
									</el-table-column>
									<el-table-column prop="browseNodeMetrics" width="140" label="分类">
										<template #default="scope">
											<div v-if="scope.row.browseNodeMetrics&&scope.row.browseNodeMetrics.occurrencePercentage">发生率:{{scope.row.browseNodeMetrics.occurrencePercentage.allProducts}}</div>
											<div v-if="scope.row.browseNodeMetrics&&scope.row.browseNodeMetrics.starRatingImpact">星级影响:{{scope.row.browseNodeMetrics.starRatingImpact.allProducts}}</div>
										</template>
									</el-table-column>
									<el-table-column prop="childAsinMetrics"  width="200" label="子ASIN">
										<template #default="scope">
											<div>最少提及</div>
											  	<div> ASIN:{{scope.row.childAsinMetrics.leastMentions.asin}}
												<span class="font-extraSmall">{{scope.row.childAsinMetrics.leastMentions.numberOfMentions}}人</span></div>
											<div>最多提及</div>
											  	<div>ASIN:{{scope.row.childAsinMetrics.mostMentions.asin}}
												<span class="font-extraSmall">{{scope.row.childAsinMetrics.mostMentions.numberOfMentions}}人</span></div>
										</template>
									</el-table-column>
									<el-table-column prop="parentAsinMetrics" width="140" label="父ASIN">
										<template #default="scope">
										<div>评论人数:{{scope.row.parentAsinMetrics.numberOfMentions}}</div>
										<div v-if="scope.row.parentAsinMetrics.occurrencePercentage"> 
										 	发生率：{{scope.row.parentAsinMetrics.occurrencePercentage}}</div>
										<div>星级影响：{{scope.row.parentAsinMetrics.starRatingImpact}}</div>
										</template>
									</el-table-column>
									<el-table-column prop="reviewSnippets" label="评论片段">
										<template #default="scope">
										<div style="border-bottom:1px dashed #dedede" v-for="item in scope.row.reviewSnippets">{{item}}</div>
										</template>
									</el-table-column>
									   
								</el-table>
					          </div>
							<div v-if="'getItemReviewTrends'==activeName">
							<div class="flex-between">
								<el-radio-group  v-model="topics" size="small">
								    <el-radio-button v-for="item in topicsOptions" :label="item.value"   >{{item.label}}</el-radio-button>
								  </el-radio-group>
								<el-space v-if="itemReviewTrends&&itemReviewTrends.dateRange">
									<div>
								        <span class="font-extraSmall">开始日期：</span> {{itemReviewTrends.dateRange.startDate.year}}-{{itemReviewTrends.dateRange.startDate.monthValue}}-{{itemReviewTrends.dateRange.startDate.dayOfMonth}}		
									</div>
									<div>
										<span class="font-extraSmall">结束日期：</span>  {{itemReviewTrends.dateRange.endDate.year}}-{{itemReviewTrends.dateRange.endDate.monthValue}}-{{itemReviewTrends.dateRange.endDate.dayOfMonth}}
									</div>
								</el-space>
							</div>
							<div v-if="itemReviewTrends&&itemReviewTrends.reviewTrends" v-for="item in topics=='negativeTopics'?itemReviewTrends.reviewTrends.negativeTopics:itemReviewTrends.reviewTrends.positiveTopics">
								<div style="padding:5px"> <el-text class="mx-1" size="large">{{item.topic}}</el-text></div>
								
								<el-table  
									ref="tableRef"
									:data="item.trendMetrics">
									<el-table-column  label="日期" >
										 <template #default="scope">
											 <el-space>
											 <div>
											     <span class="font-extraSmall">开始日期：</span> {{scope.row.dateRange.startDate.year}}-{{scope.row.dateRange.startDate.monthValue}}-{{scope.row.dateRange.startDate.dayOfMonth}}		
											 </div>
											 <div>
											 	<span class="font-extraSmall">结束日期：</span>  {{scope.row.dateRange.endDate.year}}-{{scope.row.dateRange.endDate.monthValue}}-{{scope.row.dateRange.endDate.dayOfMonth}}
											 </div>
											 </el-space>
										 </template>
									</el-table-column>
									<el-table-column  label="ASIN表现发生率"  >
										 <template #default="scope">
										 <div>{{scope.row.asinMetrics.occurrencePercentage}}</div>
										 </template>
									</el-table-column>
									<el-table-column prop="browseNodeMetrics"   label="分类发生率">
										<template #default="scope">
											<div>{{scope.row.browseNodeMetrics.occurrencePercentage.allProducts}}</div>
										</template>
									</el-table-column>
									<el-table-column prop="parentAsinMetrics"   label="父ASIN发生率">
										<template #default="scope">
											<div v-if="scope.row.parentAsinMetrics.occurrencePercentage"> 
												 {{scope.row.parentAsinMetrics.occurrencePercentage}}</div>
											 <div v-else> 	发生率：0</div>
										</template>
									</el-table-column>
								</el-table>
							   </div>
							  </div>
							  <div v-if="'getBrowseNodeReviewTopics'==activeName">
								 <div style="margin-bottom:10px"> <el-tag size="large">{{itemBrowseNode.displayName}}</el-tag></div>
								  <el-tabs tab-position="left"  v-model="activeBrowseNodeName" @tab-change="loadActiveBrowseNode" type="border-card"   class="demo-tabs">
								      <el-tab-pane label="分类评论"  name="getBrowseNodeReviewTopics"></el-tab-pane>
								      <el-tab-pane label="评论历史"  name="getBrowseNodeReviewTrends"></el-tab-pane>
								      <el-tab-pane label="分类退货"  name="getBrowseNodeReturnTopics"></el-tab-pane>
								      <el-tab-pane label="退货历史"  name="getBrowseNodeReturnTrends"></el-tab-pane>
									  <div v-loading="subloading">
										  <div v-if="activeBrowseNodeName=='getBrowseNodeReviewTopics'">
											<div class="flex-between" style="margin-bottom: 10px;">
												  <el-radio-group  v-model="topics" size="small">
												      <el-radio-button v-for="item in topicsOptions" :label="item.value"   >{{item.label}}</el-radio-button>
												    </el-radio-group>
													<el-select size="small" v-model="sortby" @change="handleQuery">
																 <el-option label="提及" value="MENTIONS">
																	提及-<span class="font-extraSmall">根据 ASIN 客户评论中提及次数计算得出的热门正面和负面话题。</span>
																 </el-option>
																 <el-option label="星级评价影响" value="STAR_RATING_IMPACT">
																	星级评价影响-<span class="font-extraSmall">根据对 ASIN 星级评定的影响来计算最正面和最负面的话题。</span>
																 </el-option>
													</el-select>
												 
												<el-space v-if="browseNodeReviewTopics&&browseNodeReviewTopics.dateRange">
													<div>
												        <span class="font-extraSmall">开始日期：</span> {{browseNodeReviewTopics.dateRange.startDate.year}}-{{browseNodeReviewTopics.dateRange.startDate.monthValue}}-{{browseNodeReviewTopics.dateRange.startDate.dayOfMonth}}		
													</div>
													<div>
														<span class="font-extraSmall">结束日期：</span>  {{browseNodeReviewTopics.dateRange.endDate.year}}-{{browseNodeReviewTopics.dateRange.endDate.monthValue}}-{{browseNodeReviewTopics.dateRange.endDate.dayOfMonth}}
													</div>
												</el-space>
											</div>
												<el-table :default-expand-all="false"
													v-if="browseNodeReviewTopics&&browseNodeReviewTopics.topics"
													ref="tableRef"
													@row-click="rowClick"
													:data="topics=='negativeTopics'?browseNodeReviewTopics.topics.negativeTopics:browseNodeReviewTopics.topics.positiveTopics">
													<el-table-column type="expand">
													   <template #default="props">
													       <el-table :data="props.row.subtopics"  >
													         <el-table-column label="子评论点"prop="subtopic" />
													         <el-table-column label="表现" prop="metrics" >
																				<template #default="scope">
																					 <div>评论人数:{{scope.row.metrics.numberOfMentions}}</div>
																					 <div>发生率：{{scope.row.metrics.occurrencePercentage}}</div>
																					 </template>
																				</el-table-column>
													         <el-table-column label="评论片段" prop="reviewSnippets"  >
																<template #default="scope">
																	 <div>
																		<div style="border-bottom:1px dashed #dedede"
																		 v-for="subitem in scope.row.reviewSnippets">
																		 {{subitem}}</div>
																	 </div>
																</template>
													          </el-table-column>
													       </el-table>
													   </template>
													 </el-table-column>
													<el-table-column prop="topic" width="140" label="评论点"></el-table-column>
								 
													<el-table-column prop="browseNodeMetrics" width="140" label="分类">
														<template #default="scope">
															<div v-if="scope.row.browseNodeMetrics&&scope.row.browseNodeMetrics.occurrencePercentage">发生率:{{scope.row.browseNodeMetrics.occurrencePercentage.allProducts}}</div>
															<div v-if="scope.row.browseNodeMetrics&&scope.row.browseNodeMetrics.starRatingImpact">星级影响:{{scope.row.browseNodeMetrics.starRatingImpact.allProducts}}</div>
														</template>
													</el-table-column>
											 
													<el-table-column prop="reviewSnippets" label="评论片段">
														<template #default="scope">
														<div style="border-bottom:1px dashed #dedede" v-for="item in scope.row.reviewSnippets">{{item}}</div>
														</template>
													</el-table-column>
													   
												</el-table>
											  
											  </div>
										  <div v-if="activeBrowseNodeName=='getBrowseNodeReviewTrends'">
											  <div class="flex-between" style="margin-bottom: 10px;">
											  	<el-radio-group  v-model="topics" size="small">
											  	    <el-radio-button v-for="item in topicsOptions" :label="item.value"   >{{item.label}}</el-radio-button>
											  	  </el-radio-group>
											  	<el-space v-if="browseNodeReviewTrends&&browseNodeReviewTrends.dateRange">
											  		<div>
											  	        <span class="font-extraSmall">开始日期：</span> {{browseNodeReviewTrends.dateRange.startDate.year}}-{{browseNodeReviewTrends.dateRange.startDate.monthValue}}-{{browseNodeReviewTrends.dateRange.startDate.dayOfMonth}}		
											  		</div>
											  		<div>
											  			<span class="font-extraSmall">结束日期：</span>  {{browseNodeReviewTrends.dateRange.endDate.year}}-{{browseNodeReviewTrends.dateRange.endDate.monthValue}}-{{browseNodeReviewTrends.dateRange.endDate.dayOfMonth}}
											  		</div>
											  	</el-space>
											  </div>
											  <div v-if="browseNodeReviewTrends&&browseNodeReviewTrends.reviewTrends" v-for="item in topics=='negativeTopics'?browseNodeReviewTrends.reviewTrends.negativeTopics:browseNodeReviewTrends.reviewTrends.positiveTopics">
											  	<div style="padding:5px"> <el-text class="mx-1" size="large">{{item.topic}}</el-text></div>
											  	
											  	<el-table  
											  		ref="tableRef"
											  		:data="item.trendMetrics">
											  		<el-table-column  label="日期" >
											  			 <template #default="scope">
											  				 <el-space>
											  				 <div>
											  				     <span class="font-extraSmall">开始日期：</span> {{scope.row.dateRange.startDate.year}}-{{scope.row.dateRange.startDate.monthValue}}-{{scope.row.dateRange.startDate.dayOfMonth}}		
											  				 </div>
											  				 <div>
											  				 	<span class="font-extraSmall">结束日期：</span>  {{scope.row.dateRange.endDate.year}}-{{scope.row.dateRange.endDate.monthValue}}-{{scope.row.dateRange.endDate.dayOfMonth}}
											  				 </div>
											  				 </el-space>
											  			 </template>
											  		</el-table-column>
											  		 
											  		<el-table-column prop="browseNodeMetrics"   label="分类发生率">
											  			<template #default="scope">
											  				<div>{{scope.row.browseNodeMetrics.occurrencePercentage.allProducts}}</div>
											  			</template>
											  		</el-table-column>
											   
											  	</el-table>
											   </div>
											  
											  
											  </div>
										  <div v-if="activeBrowseNodeName=='getBrowseNodeReturnTopics'">
											  <div class="flex-between">
											  	<el-space v-if="browseNodeReturnTopics&&browseNodeReturnTopics.dateRange">
											  		<div>
											  	        <span class="font-extraSmall">开始日期：</span> {{browseNodeReturnTopics.dateRange.startDate.year}}-{{browseNodeReturnTopics.dateRange.startDate.monthValue}}-{{browseNodeReturnTopics.dateRange.startDate.dayOfMonth}}		
											  		</div>
											  		<div>
											  			<span class="font-extraSmall">结束日期：</span>  {{browseNodeReturnTopics.dateRange.endDate.year}}-{{browseNodeReturnTopics.dateRange.endDate.monthValue}}-{{browseNodeReturnTopics.dateRange.endDate.dayOfMonth}}
											  		</div>
											  	</el-space>
											  </div>
											  	<el-table  
											  		v-if="browseNodeReturnTopics&&browseNodeReturnTopics.topics"
											  		:data="browseNodeReturnTopics.topics">
											  	 
											  		<el-table-column prop="topic"   label="评论点"></el-table-column>
											  								 
											  		<el-table-column prop="browseNodeMetrics"   label="分类发生率">
											  			<template #default="scope">
											  				<div v-if="scope.row.browseNodeMetrics&&scope.row.browseNodeMetrics.occurrencePercentage">{{scope.row.browseNodeMetrics.occurrencePercentage.allProducts}}</div>
											  			</template>
											  		</el-table-column>
											    
											  	</el-table>
											  </div>
										  <div v-if="activeBrowseNodeName=='getBrowseNodeReturnTrends'">
											  <div class="flex-between">
											   
											  	<el-space v-if="browseNodeReturnTrends&&browseNodeReturnTrends.dateRange">
											  		<div>
											  	        <span class="font-extraSmall">开始日期：</span> {{browseNodeReturnTrends.dateRange.startDate.year}}-{{browseNodeReturnTrends.dateRange.startDate.monthValue}}-{{browseNodeReturnTrends.dateRange.startDate.dayOfMonth}}		
											  		</div>
											  		<div>
											  			<span class="font-extraSmall">结束日期：</span>  {{browseNodeReturnTrends.dateRange.endDate.year}}-{{browseNodeReturnTrends.dateRange.endDate.monthValue}}-{{browseNodeReturnTrends.dateRange.endDate.dayOfMonth}}
											  		</div>
											  	</el-space>
											  </div>
											  <div v-if="browseNodeReturnTrends&&browseNodeReturnTrends.returnTrends" v-for="item in browseNodeReturnTrends.returnTrends">
											  	<div style="padding:5px"> <el-text class="mx-1" size="large">{{item.topic}}</el-text></div>
											  	
											  	<el-table  
											  		ref="tableRef"
											  		:data="item.trendMetrics">
											  		<el-table-column  label="日期" >
											  			 <template #default="scope">
											  				 <el-space>
											  				 <div>
											  				     <span class="font-extraSmall">开始日期：</span> {{scope.row.dateRange.startDate.year}}-{{scope.row.dateRange.startDate.monthValue}}-{{scope.row.dateRange.startDate.dayOfMonth}}		
											  				 </div>
											  				 <div>
											  				 	<span class="font-extraSmall">结束日期：</span>  {{scope.row.dateRange.endDate.year}}-{{scope.row.dateRange.endDate.monthValue}}-{{scope.row.dateRange.endDate.dayOfMonth}}
											  				 </div>
											  				 </el-space>
											  			 </template>
											  		</el-table-column>
											  		 
											  		<el-table-column prop="browseNodeMetrics"   label="分类发生率">
											  			<template #default="scope">
											  				<div>{{scope.row.browseNodeMetrics.occurrencePercentage.allProducts}}</div>
											  			</template>
											  		</el-table-column>
											   
											  	</el-table>
											   </div>
											  
											  </div>
									  </div>
								    </el-tabs>
							  </div>
							</div>
						 </el-col>
	         	</el-row>
	         	</div>
	</el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted,nextTick}from"vue"
	import {Close} from '@element-plus/icons-vue';
	import * as echarts from 'echarts';
	import listingApi from '@/api/amazon/listing/listingApi.js';
	import {CheckInputInt,dateFormat,dateFormatMMdd} from "@/utils/index.js";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import { useDark, useToggle } from "@vueuse/core";
    const isDark = useDark();
	const preinputRef=ref()
	const tableRef=ref();
	const emit = defineEmits(['confirm']);
	const state=reactive({
		  dialog:{visible:false},
		  loading:false,
		  subloading:false,
		  itemReviewTopics:{},
		  itemReviewTrends:{},
		  itemBrowseNode:{},
		  browseNodeReviewTopics:{},
		  browseNodeReviewTrends:{},
		  browseNodeReturnTopics:{},
		  browseNodeReturnTrends:{},
		  activeBrowseNodeName:"getBrowseNodeReviewTopics",
		  sortby:"STAR_RATING_IMPACT",
		  row:{},
		  activeName:"getItemReviewTopics",
		  topicsOptions:[ { label: '否定评论', value: 'negativeTopics' },
                         { label:'肯定评论', value: 'positiveTopics' },],
		  queryParam:{groupid:"",warehouse:"",sku:"",fromdate:"",enddate:""},
		  topics:"negativeTopics",
	})
	const { dialog,loading,subloading,queryParam,row,
			itemReviewTrends,itemBrowseNode,sortby,itemReviewTopics,
			browseNodeReviewTopics,browseNodeReviewTrends,browseNodeReturnTopics,browseNodeReturnTrends,
			topicsOptions,topics,activeName,activeBrowseNodeName} = toRefs(state);
	var myChart =null ;
	function show(row){
		state.dialog.visible=true;
		state.row=row; 
		handleQuery();
	}
	onMounted(()=>{
	
	})
	
	defineExpose({show});
    function handleClose(){
		state.dialog.visible=false;
	}
	function rowClick(data){
		tableRef.value.toggleRowExpansion(data);
	}
	function getItemBrowseNode(){
		state.loading=true;
		listingApi.getItemBrowseNode(
		{"amazonauthid":state.row.amazonAuthId,
		"asin":state.row.asin,
		"marketplaceid":state.row.marketplaceid}).then(res=>{
					state.itemBrowseNode=res.data;
					loadActiveBrowseNode();
					state.loading=false;
		})
	}
	function loadActiveBrowseNode(){
		if(state.activeBrowseNodeName=="getBrowseNodeReviewTopics"){
			state.subloading=true;
			listingApi.getBrowseNodeReviewTopics(
			{"amazonauthid":state.row.amazonAuthId,
			"browsenode":state.itemBrowseNode.browseNodeId,
			"marketplaceid":state.row.marketplaceid,
			"sortby":state.sortby
			}).then(res=>{
						state.browseNodeReviewTopics=res.data;
						state.subloading=false;
			})
		}
		if(state.activeBrowseNodeName=="getBrowseNodeReviewTrends"){
			state.subloading=true;
			listingApi.getBrowseNodeReviewTrends(
			{"amazonauthid":state.row.amazonAuthId,
			"browsenode":state.itemBrowseNode.browseNodeId,
			"marketplaceid":state.row.marketplaceid
			}).then(res=>{
						state.browseNodeReviewTrends=res.data;
						state.subloading=false;
			})
		}
		if(state.activeBrowseNodeName=="getBrowseNodeReturnTopics"){
			state.subloading=true;
			listingApi.getBrowseNodeReturnTopics(
			{"amazonauthid":state.row.amazonAuthId,
			"browsenode":state.itemBrowseNode.browseNodeId,
			"marketplaceid":state.row.marketplaceid
			}).then(res=>{
						state.browseNodeReturnTopics=res.data;
						state.subloading=false;
			})
		}
		if(state.activeBrowseNodeName=="getBrowseNodeReturnTrends"){
			state.subloading=true;
			listingApi.getBrowseNodeReturnTrends(
			{"amazonauthid":state.row.amazonAuthId,
			"browsenode":state.itemBrowseNode.browseNodeId,
			"marketplaceid":state.row.marketplaceid
			}).then(res=>{
						state.browseNodeReturnTrends=res.data;
						state.subloading=false;
			})
		}
		
	}
	function handleClick(value){
		if(value=="getBrowseNodeReviewTopics"){
			getItemBrowseNode()
		}
		handleQuery();
	}
	 function handleQuery(){
		 state.loading=true;
		 if("getItemReviewTopics"==state.activeName){
			 listingApi.getItemReviewTopics(
			 {"amazonauthid":state.row.amazonAuthId,
			 "asin":state.row.asin,
			 "marketplaceid":state.row.marketplaceid,
			 "sortby":state.sortby}).then(res=>{
			 			state.itemReviewTopics=res.data;
			 			state.loading=false;
			 })
		 }
		 if("getItemReviewTrends"==state.activeName){
		 			 listingApi.getItemReviewTrends(
		 			 {"amazonauthid":state.row.amazonAuthId,
		 			 "asin":state.row.asin,
		 			 "marketplaceid":state.row.marketplaceid
		 		      }).then(res=>{
							state.itemReviewTrends=res.data;
							state.loading=false;
		 			 })
		 }
	}
   
  var yearMyChart=null;
   
</script>

<style>
	 .product-dialog-img{
		 margin-top:-5px;
		 margin-right:10px;
	 }
</style>
