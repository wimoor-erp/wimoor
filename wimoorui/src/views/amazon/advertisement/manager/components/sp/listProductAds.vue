<template>
	<el-row>
		<el-space>
			<el-select v-model="adstate" placeholder="广告状态" @change="handleQuery"  >
				<el-option v-for="item in adStatesOptions" :label="item.name" :value="item.value"></el-option>
			</el-select>
          <DateSelect @setDate="setDate" @dateTypeSwitch="handleQuery"/>
			<el-input v-model="queryParams.search" clearable @input="handleQuery" placeholder="搜索SKU">
					 <template #suffix>
					  <el-icon @click.stop="handleQuery"><Search /></el-icon>
					 </template>
			</el-input>	
			<el-button @click="clearSearch">重置</el-button>
			<el-popover   :teleported="true"  :width="500" trigger="click">
				<template #reference>
				<el-button  title='高级筛选'  class='ic-btn'>
				  <filtericon></filtericon>
				</el-button>
				</template>
				 <el-form  label-width="auto"  label-position="left">
					 <el-form-item   label="商品广告">
						<el-select v-model="queryParams.changeRate" @change="handleQuery">
							<el-option value="" key="" label="全部"></el-option>
							<el-option value="-3" key="-3" label="销量小幅下降"></el-option>
							<el-option value="-25" key="-25" label="销量大幅下降"></el-option>
							<el-option value="3" key="3" label="销量小幅上升"></el-option>
							<el-option value="25" key="25" label="销量大幅上升"></el-option>
						</el-select>	
						 </el-form-item>
					   <el-form-item label="产品SKU">
					     <el-input
					         v-model="queryParams.skuStr"
					         :rows="2"
					         type="textarea"
					         placeholder="请输入产品SKU,以英文的逗号分割"
					       />
					    </el-form-item>
						<!-- <el-form-item label="产品分类">
						   <Category ref="categoryRef" @change="changeCategory"></Category>
						 </el-form-item> -->
						 <el-form-item label="产品备注">
						   <el-input
						       v-model="queryParams.remark"
						       :rows="2"
						       type="textarea"
						       placeholder="请输入产品备注"
						     />
						  </el-form-item>
					 
					 <el-form-item>
						 <el-button type="primary" @click.stop="handleQuery" plain>确认</el-button>
					  </el-form-item>
					 </el-form>
				  </el-popover>	
			<DataFilter :activeName="activeName"  @change="changeDataFilter"/>
		</el-space>
	</el-row>
	<el-row class="flex-center-between">
		<el-space>
		 
		<el-button   :disabled="!queryParams.adGroupid" title="请先锁定广告组再添加"  type="primary"  class="im-but-one" @click="handleAdd()">
		  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		  <span>添加广告商品</span>
		</el-button>
		<el-dropdown trigger="click">
		  <el-button :disabled="disabled">
		             批量操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
		  </el-button>
		  <template #dropdown>
		    <el-dropdown-menu >
			  <el-dropdown-item  @click="handleUpdate(globalTable,state,'status','ENABLED')">启用</el-dropdown-item>
			  <el-dropdown-item  @click="handleUpdate(globalTable,state,'status','PAUSED')">暂停</el-dropdown-item>
			  <el-dropdown-item  @click="handleValue('remark','调整备注')">备注</el-dropdown-item>
		    </el-dropdown-menu>
		  </template>
		</el-dropdown>
		<el-space v-show="batch.input">
			<el-button @click="batch.input=false">取消</el-button>
			<el-text class="font-base">{{batch.text}}</el-text>
			<el-input v-if="batch.type=='remark'" v-model="batch.value"></el-input>
			 
			<el-button type="primary" @click.stop="handleUpdate(globalTable,state,batch.type,batch.value)">确认</el-button>
		</el-space>
		</el-space>
		<div class='icon-group'>
			<el-space>
			<el-dropdown  >
			    <el-button class='ic-btn'  title='其他'>
			      <el-icon><Menu /></el-icon>
			    </el-button>
			    <template #dropdown>
			      <el-dropdown-menu>
					<el-dropdown-item @click="handleExpendAdContent('sumproducts')">商品汇总</el-dropdown-item>
			        <el-dropdown-item   @click="handleExpendAdContent('days')">广告成交周期</el-dropdown-item>
					<el-dropdown-item   @click="handleExpendAdContent('otherproducts')">广告贡献-其它商品</el-dropdown-item>
			      </el-dropdown-menu>
			    </template>
			  </el-dropdown>
			  <el-button   @click="showAdChart(adchartRef,state)" class='ic-btn'  title='展示图表'>
			   <el-icon><Histogram /></el-icon>
			  </el-button>
			  <el-tooltip content="自定义列"   placement="top">
			  	<el-button   @click="showColumnSet(columnSetRef,'advadproduct')" class='ic-btn'>
			  <el-icon><Tools /></el-icon>
			  </el-button>
			  </el-tooltip>
			</el-space>
		</div>
	</el-row>
 <div class="summary-top expend-table" v-loading="selectionloding">
	 <GlobalTable  ref="globalTable" :tableData="tableData"
	   @loadTable="loadTableData" :stripe="false" 
	   @selectionChange = "selectChange"
	   :defaultSort="defaultSort"
	   show-summary
	   :lazy="false"
	   :load="handleExpend"
	   :treeProps="{ children: 'children', hasChildren: 'hasChildren' }"
	   :summary-method="getSummaries"
	    rowKey="id"
	   :defaultExpandAll="true"
	   fieldType="advadproduct"
	   >
		  <template v-slot:field='columns'>
		 <el-table-column  fixed type="selection" width="60" />
		 <el-table-column fixed   label="图片"  width="80" >
		 				 <template #default="scope">
		 					<div  v-if="!scope.row.statustype && scope.row.status!='none'">
		 						<el-image v-if="scope.row.image" :src="scope.row.image" style="height:60px;width:60px;"></el-image>
		 						<el-image v-else :src="$require('empty/noimage40.png')" style="height:60px;width:60px;"></el-image>
		 					</div>	
		 					<div v-if="scope.row.status=='none'">
		 						
		 					</div>
		 				 </template>
		 			</el-table-column>
		 <el-table-column fixed    label="商品信息"  width="200" show-overflow-tooltip>
		 				   <template #default="scope">
		 					   <div v-if="scope.row.statustype=='product'">
		 						   售价:{{scope.row.landed_amount}}
		 					   </div>
		 					   <div v-else>
		 						   <div v-if="scope.row.status!='none'">
									   <div class='sku'>{{scope.row.sku}} </div>
		 							<div  class='font-extraSmall'>{{scope.row.name}}</div>
									<div><span class="font-small" style="padding-right: 3px;">{{scope.row.asin}} </span><span class="font-extraSmall"> {{scope.row.groupname}}-{{scope.row.market}}</span></div>
		 						  </div>
		 						  <div v-else>
		 							  	<div  class='name'>{{scope.row.name}}</div>
		 						  </div>
		 						</div>
		 				   </template>
		  </el-table-column>
		   <el-table-column    label="状态"  >
			   <template #default="scope">
				   <div >
				    <el-switch   v-if="scope.row.status?.toUpperCase()=='ARCHIVED'" disabled
				       v-model="scope.row.checkstatus"
					    inline-prompt
						width="45px"
					   active-text="开启"
					   inactive-text="归档"
				       size="small"
				     />
					 <el-switch :loading="scope.row.statusloading" v-else-if="scope.row.status?.toUpperCase()=='ENABLED' || scope.row.status?.toUpperCase()=='PAUSED'"  
					    v-model="scope.row.checkstatus"
						 inline-prompt
						 width="45px"
						active-text="开启"
						inactive-text="暂停"
					    size="small"
						 @change="handleSubmitUpdate(state,'status',scope.row)"
					  />
					  <span v-else></span>
					  </div>
					   <el-button link  size="small" v-if="scope.row.servingStatus&&scope.row.servingStatus=='loading'" :loading="true"></el-button>
					   <div v-else>
						    <span  style="font-size:10px;" v-if="scope.row?.servingStatus&&scope.row?.servingStatus?.show" :class="'text-'+scope.row?.servingStatus?.color" :title="scope.row?.servingStatus?.title">{{scope.row?.servingStatus?.name}}</span>
					   </div>
					   
			   </template>
			</el-table-column>
		 
		 <template v-if="columns.list" v-for="column in columns.list">
		 	<el-table-column   :label="column.label" :width="column.width " :sortable="column.sortable?'custom':false" :prop="column.prop">
		 		<template #default="scope">
		 			   <div v-if="column.prop=='campaignName'">
		 				  <span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
		 				    <span  ></span>
		 				  </span>
		 				   <span  v-else-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
		 				  				 7/30日销量图表:
		 				  		 <el-icon class="pointer" @click.stop="showOrderby(scope.row)"><Histogram /></el-icon>
		 				  	 </span>
							 <div v-else>
							   <el-link  :underline="false" type="primary">
							   <span @click.stop="bindAdvCams(scope.row.campaignId,scope.row.campaignName)" v-if="scope.row.campaignName">
								   {{scope.row.campaignName}}
							   </span>
							   <span v-else>
								   <span >{{scope.row.name}}</span>
							   </span>
							   </el-link>
							  <copy class="" @click.stop="CopyText(scope.row.campaignName)" title='复制广告活动' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
							  </div>
		 			   </div>
					    <div v-else-if="column.prop=='adGroupName'">
							<span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
									  <span  ></span>
								   </span>
								   <span  v-else-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
									7日转化率:
								  <span v-if="scope.row.advctr"> {{formatPercent(scope.row.advctr)}}</span>
								   <span v-else> 0</span>
								   </span>
							  <div v-else>
							   <el-link  :underline="false" type="primary">
							   <span v-if="scope.row.adGroupName"  
										   @click.stop="bindAdvGpds(scope.row.adGroupId,scope.row.adGroupName,scope.row.campaignId,scope.row.campaignName)">
								   {{scope.row.adGroupName}}
							   </span>
							   </el-link>
								<copy class="" @click.stop="CopyText(scope.row.adGroupName)" title='复制广告组' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
								</div>
					     </div>
		 				<div v-else-if="column.prop=='impressions'">
		 				   <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
		 				   	亚马逊库存:
		 				   <span v-if="scope.row.afn_fulfillable_quantity"> {{ scope.row.afn_fulfillable_quantity}}</span>
		 				   </div>
		 				   <span v-else > {{scope.row.impressions}}</span>
		 				</div>
		 				 <div v-else-if="column.prop=='clicks'">
		 				    <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
		 				    	广告总花费:
		 				    <span v-if="scope.row.advspend"> {{ scope.row.advspend}}</span>
		 				    </div>
		 				    <span v-else > {{scope.row.clicks}}</span>
		 				 </div>
						 <div v-else-if="column.prop=='CTR'">
						    <span v-if="scope.row.status!='none'">{{ scope.row.CTR }}</span>
						    <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						    	其它成本:
						    <span v-if="scope.row.othercost"> {{ scope.row.othercost}}</span>
						    </div>
						 </div>
						 <div v-else-if="column.prop=='avgcost'">
						   <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						   	利润率:
						   <span v-if="scope.row.prorate"> {{ formatFloat(scope.row.prorate/100)}}%</span>
						   </div>
						   <span v-else>{{formatFloat(scope.row.avgcost)}}</span>
						 </div>
						 <div v-else-if="column.prop=='cost'">
						    <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						    	利润:
						    <span v-if="scope.row.profits"> {{ formatFloat(scope.row.profits)}}</span>
						    </div>
						    <span v-else > {{scope.row.cost}}</span>
						 </div>
						 <div v-else-if="column.prop=='sumSales'">
						    <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						    	净利润率:
						    <span v-if="scope.row.proprate"> {{ formatFloat(scope.row.proprate)}}</span>
						    </div>
						    <span v-else > {{scope.row.sumSales}}</span>
						 </div>
						 <div v-else-if="column.prop=='sumUnits'">
						    <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						    	净利润:
						    <span v-if="scope.row.profitall"> {{ formatFloat(scope.row.profitall)}}</span>
						    </div>
						    <span v-else > {{scope.row.sumUnits}}</span>
						 </div>
						 <div v-else-if="column.prop=='attributedUnitsOrdered'">
							 <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
								ACoAS:
							 <span v-if="scope.row.acoas"> {{ formatFloat(scope.row.acoas)}}</span>
							 </div>
							 <span v-else > {{scope.row.attributedUnitsOrdered}}</span>
						  </div>
						  <div v-else-if="column.prop=='ACOS'">
						     <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						     	<span>购物车比例:<span v-if="scope.row.buybox"></span>{{scope.row.buybox}}%</span>
						      </div>
						     <span v-else>{{ scope.row.ACOS}}</span>
						  </div>
						  <div v-else-if="column.prop=='ROAS'">
						    <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						     </div>
						    <span v-else>{{scope.row.ROAS}}</span>
						  </div>
						  <div v-else-if="column.prop=='CSRT'">
						    <div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						     </div>
						    <span v-else>{{ scope.row.CSRT}}</span>
						  </div>
		 				<div v-else>
		 					  {{scope.row[column.prop]}}
		 				</div>
		 		  </template>
		 	</el-table-column>
		 </template>
		 
		 
			<!-- <el-table-column    label="广告活动"  width="200" >
				   <template #default="scope">
					   <span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
					     <span  ></span>
					   </span>
					    <span  v-else-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
					   				 7/30日销量图表:
					   		 <el-icon class="pointer" @click.stop="showOrderby(scope.row)"><Histogram /></el-icon>
					   	 </span>
						 <div v-else>
						   <el-link  :underline="false" type="primary">
						   <span @click.stop="bindAdvCams(scope.row.campaignId,scope.row.campaignName)" v-if="scope.row.campaignName">
							   {{scope.row.campaignName}}
						   </span>
						   <span v-else>
							   <span >{{scope.row.name}}</span>
						   </span>
						   </el-link>
						  <copy class="" @click.stop="CopyText(scope.row.campaignName)" title='复制广告活动' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
						  </div>
				   </template>
			 </el-table-column>
			 <el-table-column     label="广告组"  width="200" >
			 	   <template #default="scope">
			 		   <span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
			 						  <span  ></span>
			 					   </span>
			 					   <span  v-else-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
			 					   	7日转化率:
			 					  <span v-if="scope.row.advctr"> {{formatPercent(scope.row.advctr)}}</span>
								   <span v-else> 0</span>
			 					   </span>
							  <div v-else>
			 				   <el-link  :underline="false" type="primary">
			 				   <span v-if="scope.row.adGroupName"  
			 							   @click.stop="bindAdvGpds(scope.row.adGroupId,scope.row.adGroupName,scope.row.campaignId,scope.row.campaignName)">
			 					   {{scope.row.adGroupName}}
			 				   </span>
			 				   </el-link>
							    <copy class="" @click.stop="CopyText(scope.row.adGroupName)" title='复制广告组' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
								</div>
			 	   </template>
			 				   
			  </el-table-column> -->
			<!-- <el-table-column   label="曝光量" width="140 " sortable="custom"     prop="impressions">
					 <template #default="scope">
					<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						亚马逊库存:
					<span v-if="scope.row.afn_fulfillable_quantity"> {{ scope.row.afn_fulfillable_quantity}}</span>
					</div>
					<span v-else > {{scope.row.impressions}}</span>
					</template>
			 </el-table-column>
			<el-table-column   label="点击次数" width="130 " sortable="custom"     prop="clicks">
					<template #default="scope">
					<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						广告总花费:
					<span v-if="scope.row.advspend"> {{ scope.row.advspend}}</span>
					</div>
					<span v-else > {{scope.row.clicks}}</span>
					</template>
			 </el-table-column>
			<el-table-column   label="点击率" width="130 " sortable="custom"   prop="CTR" >
					<template #default="scope">
						<span v-if="scope.row.status!='none'">{{ scope.row.CTR }}</span>
						<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
							其它成本:
						<span v-if="scope.row.othercost"> {{ scope.row.othercost}}</span>
						</div>
					</template>
			</el-table-column>
			<el-table-column   label="每次点击费用" width="130" sortable="custom"   prop="avgcost">
					<template #default="scope">
						<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
							利润率:
						<span v-if="scope.row.prorate"> {{ formatFloat(scope.row.prorate/100)}}%</span>
						</div>
						<span v-else>{{formatFloat(scope.row.avgcost)}}</span>
						
					</template>
			</el-table-column>
			<el-table-column   label="广告费"  width="130 " sortable="custom"    prop="cost">
					<template #default="scope">
					<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
						利润:
					<span v-if="scope.row.profits"> {{ formatFloat(scope.row.profits)}}</span>
					</div>
					<span v-else > {{scope.row.cost}}</span>
					</template>
			 </el-table-column>
			<el-table-column   label="广告销售额" width="130 " sortable="custom"   prop="sumSales">
					<template #default="scope">
							<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
								净利润率:
							<span v-if="scope.row.proprate"> {{ formatFloat(scope.row.proprate)}}</span>
							</div>
							<span v-else > {{scope.row.sumSales}}</span>
					</template>
			</el-table-column>
			<el-table-column   label="销量" width="130 " sortable="custom"   prop="sumUnits" >
					<template #default="scope">
								<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
									净利润:
								<span v-if="scope.row.profitall"> {{ formatFloat(scope.row.profitall)}}</span>
								</div>
								<span v-else > {{scope.row.sumUnits}}</span>
						</template>
			</el-table-column>
			<el-table-column   label="订单量" width="130 " sortable="custom"  prop="attributedUnitsOrdered" >
					<template #default="scope">
								<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
									ACoAS:
								<span v-if="scope.row.acoas"> {{ formatFloat(scope.row.acoas)}}</span>
								</div>
								<span v-else > {{scope.row.attributedUnitsOrdered}}</span>
						</template>
			</el-table-column>
			<el-table-column   label="Acos" width="140 " sortable="custom"    prop="ACOS">
					<template #default="scope">
						<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
							<span>购物车比例:<span v-if="scope.row.buybox"></span>{{scope.row.buybox}}%</span>
						 </div>
						<span v-else>{{ scope.row.ACOS}}</span>
					</template>
			</el-table-column>
			<el-table-column   label="RoAS" width="120 " sortable="custom"  prop="ROAS">
				<template #default="scope">
					<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
					 </div>
					<span v-else>{{scope.row.ROAS}}</span>
				</template>
			</el-table-column>
			<el-table-column   label="转化率" width="120 "   prop="CSRT">
				<template #default="scope">
					<div  v-if="scope.row.status=='none' && scope.row.statustype=='product'"    >
					 </div>
					<span v-else>{{ scope.row.CSRT}}</span>
					</template>
			</el-table-column>
			<el-table-column   label="目标商品-销售额" sortable="custom"  width="180 "  prop="attributedSalesSameSKU"/>
			<el-table-column   label="其它商品-销售额" sortable="custom"  width="180 "  prop="attributedSales"/>
			<el-table-column   label="目标商品-转化量" sortable="custom"  width="160"   prop="attributedConversionsSameSKU"/>
			<el-table-column   label="其它商品-转化量" sortable="custom"  width="160"   prop="attributedConversions"/> -->
	 </template>
	 </GlobalTable>
 </div>
	<columnSet ref="columnSetRef"  @change="getFieldData" />
	<AdChart ref="adchartRef" :summary="summary" />
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs,computed} from 'vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,Menu,Edit,Histogram,WarningFilled,Close,Tools} from '@element-plus/icons-vue';
	import {Copy} from '@icon-park/vue-next';
	import CopyText from"@/utils/copy_text.js";
	import columnSet from '@/components/Table/column_set.vue';
	import AdOrderCycle from '@/views/amazon/advertisement/manager/components/common/ad_order_cycle.vue';
    import DateSelect from '../../components/common/date_select.vue'
	import filtericon from "@/components/icon/filtericon.vue";
	import Category from '@/components/header/category.vue';
	import AdChart from '@/views/amazon/advertisement/manager/components/common/ad_chart.vue';
	import DataFilter from '@/views/amazon/advertisement/manager/components/common/data_filter.vue';
	import {handleSubmitUpdate,loadList,handleUpdate,handleExpandChange,} from '../javascript/productAds.js';
	import {showAdChart,handleAdvDaysData,showColumnSet} from '../javascript/common.js';
	import {adgroupsuggestFormatter,} from '../javascript/formatter.js';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	let router = useRouter();
	const columnSetRef = ref();
	const endtimeRef = ref()
	const budgetRefRef = ref()
	const biddingRef = ref()
	const globalTable =ref()
	const categoryRef=ref();
	const adchartRef=ref();
	let props = defineProps({ activeName:"" })
	const { activeName } = toRefs(props);
	const state = reactive({
		editRow:{endDate:'',budget:''},
		tableData:{records:[
		],total:0},
		summary:{},
		expandType:"",
		queryParams:{},
		batch:{text:'',input:false,value:'',},
		emits:null,
		defaultSort:{ prop: 'sku', order: 'ascending' },
		bindParams:{},
		adStatesOptions:[
				{name:'全部广告状态',value:'all',},
				{name:'已启动',value:'enabled',},
				{name:'已暂停',value:'paused',},
				{name:'已归档',value:'archived',},
				// {name:'运行中',value:'runing',},
				// {name:'停止中',value:'stoping',},
			],
		adstate:'all',
		selectionloding:false,
	})
	const{
		tableData,
		editRow,
		batch,
		summary,
		expandType,
		queryParams,
		defaultSort,
		bindParams,
		adStatesOptions,
		adstate,
		selectionloding,
	}=toRefs(state)
	const emits = defineEmits(['selectRow','change','bindData']);
	state.emits=emits;
	
	function showOrderby(row){
		//展示图表
	}
	function showBidModal(row){
		//加载建议竞价数据
		//loadAgroupSuggestBid(row);
		state.bidVisible=true;
		state.editRow=row;
		state.editRow.defaultbid = row.defaultBid;
	}
    function setDate(dates,dateType,isload,tabName){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(isload!="load"){
			if(tabName===''||tabName===null||tabName===undefined||tabName==='ProductAds'){
				handleQuery();
			}
		}
	}
	function changeDataFilter(paralist){
		if(paralist!="" && paralist!=undefined && paralist!=null){
			state.queryParams.paralist=paralist;
			handleQuery();
		}else{
			state.queryParams.paralist=null;
			handleQuery();
		}
	}
	function clearSearch(){
		state.adstate='all';
		state.adType='all';
		state.queryParams.search='';
		state.bindParams={};
		state.queryParams.changeRate='';
		state.queryParams.skuStr=null;
		if(categoryRef.value){
			state.queryParams.categoryid='';
			categoryRef.value.reset();
		}
		handleQuery();
	}

	onMounted(()=>{
		//加载汇总数值
	
	});
	function handleAdd(){
		router.push({
			path:"/a/a/sp",
			query:{
				title:'添加广告商品',
				path:"/a/a/sp",
				type:state.activeName,
				campaignid:state.queryParams.campaignid,
				adGroupid:state.queryParams.adGroupid
			},
		})
	}
	function bindAdvCams(id,name){
		state.bindParams.campaignid=id;
		state.bindParams.camname=name;
		state.bindParams.adgroupid=null;
		state.bindParams.adgroupname=null;
		emits("bindData",state.bindParams);
	}
	function bindAdvGpds(adid,adname,camid,camname){
		state.bindParams.campaignid=camid;
		state.bindParams.camname=camname;
		state.bindParams.adgroupid=adid;
		state.bindParams.adgroupname=adname;
		emits("bindData",state.bindParams);
	}
	
	 
	function selectChange(seletion){
		emits('selectRow',seletion)
	}
	 
	function handleExpend(row,treeNode,resolve){
		handleExpandChange(state,row,treeNode,resolve)
	}
	 
	function handleExpendAdContent(ftype){
		if(ftype=="days"){
			//广告成交周期展开
			state.expandType="days";
		}else if(ftype=="sumproducts"){
			//广告商品展开
			state.expandType="sumproducts";
		}else if(ftype=="otherproducts"){
			//广告贡献-其它商品展开
			state.expandType="otherproducts";
		}
		var records=JSON.parse(JSON.stringify(state.tableData.records)); 
		records.forEach(item=>{
			handleExpandChange(state,item);
		});
		state.tableData.records=[];
		var time=setTimeout(function(){
			state.tableData.records=records;
			clearTimeout(time);
		},1000);
		
	 
	}
	
	function getFieldData(fdata){
		loadField(fdata);
	}
	function loadField(id){
		globalTable.value.refreshField();
	}
	
	function loadTableData(params){
		if(params&&params.profileid){
			state.queryParams=params;
			loadList(state);
		}
	}
	function handleQuery(){
		if(state.queryParams.profileid){
			if(state.adstate=='all'){
				state.queryParams.state=null;
			}else{
				state.queryParams.state=state.adstate;
			}
			state.queryParams.targetingType=state.adType;
		    state.queryParams.sku=state.queryParams.search;
			globalTable.value.loadTable(state.queryParams);
		}
	    
	}
	function getSummaries({columns,data}){
		var arr = ["合计"];
				columns.forEach((item,index)=>{
					if(index>=6){
						arr[index]=state.summary[item.property];
					}
				})
		return  arr
	}
	 function show(params){
		 if(state.queryParams.profileid!=params.profileid
		  ||state.queryParams.campaignid!=params.campaignid
		  ||state.queryParams.adGroupid!=params.adGroupid){
		 state.queryParams=Object.assign(state.queryParams, params);
		 state.activeName=props.activeName;
			var timer=setTimeout(function(){
						 handleQuery();
						 loadField();
						 clearTimeout(timer);
			},100);
			}
	 }
	 function changeCategory(val){
	 	state.queryParams.categoryid=val;
	 }
	 function handleValue(type,name){
	 	state.batch.input = true;
	 	state.batch.text = name;
		state.batch.type=type;
	 }
	defineExpose({
		handleQuery,show,
	})
</script>

<style>
.popover-footer{
	margin-top:16px;
}
 .summary-top .el-table__body-wrapper {
  order: 1;
}
</style>