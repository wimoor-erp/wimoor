<template>
	<div class="card-page screen-height">
	<Header @change="handleQuery" @deletes="deleteItems" @pushs="pushItems" :btnloading="btnloading"   @times="changeTime" @prices="changePrice" @cycles="changeCycle" />
	<div class="pag-radius-bor">
		<!-- :defaultSort="{ prop: 'opttime', order: 'descending' }"  -->
		<GlobalTable ref="globalTable" 
		:tableData="tableData"
		@selectionChange='handleSelect' 
		:defaultSort="defaultSort"
		rowKey="pid"
		@loadTable="loadTableData" :stripe="true"
		>
			<template #field>
			<el-table-column fixed type="selection" width="40"/>
			
			<el-table-column fixed label="图片" width="120" >
				
				<template #default="scope">
					
					<el-image :src="scope.row.location"  ></el-image>
					
					
				</template>
				
			</el-table-column>
			 
			<el-table-column  label="ASIN/标题/SKU/店铺" width="240" >
				<template #default="scope">
					<p><b>{{scope.row.asin}}<el-link :href="scope.row.href" target="_blank">
					<el-icon><Link /></el-icon>
					</el-link>&nbsp;</b>
					<el-tag type="info" effect="plain" size="small" v-if="scope.row.status_upload=='NOUPLOAD'">未发布</el-tag>
					<el-tag type="danger" effect="plain" size="small" v-if="scope.row.status_upload=='INVALID'">无效产品</el-tag>
					
					<el-tag type="success" effect="plain" size="small" v-if="scope.row.status_upload=='ISUPLOAD'">已发布</el-tag>
					<el-tag type="success" effect="plain" size="small" v-if="scope.row.status_upload=='DISCOVERABLE'">已发布</el-tag>
					<el-tag type="info" effect="plain" size="small" v-if="scope.row.status_upload=='ISOFFLINE'">待上架</el-tag>
					<el-tag type="danger" effect="plain" size="small" v-if="scope.row.status_upload=='ISDELETE'">已删除</el-tag>
					<el-tag type="success" effect="plain" size="small" v-if="scope.row.status_upload=='ISONLINE'">在售中</el-tag>
					
					<el-tag type="success" effect="plain" size="small" v-if="scope.row.status_upload=='NEEDONLINE'">上架中</el-tag>
					<el-tag type="info" effect="plain" size="small" v-if="scope.row.status_upload=='NEEDOFFLINE'">下架中</el-tag>
					<el-tag type="success" effect="plain" size="small" v-if="scope.row.status_upload=='NEEDDISCOVERABLE'">发布中</el-tag>
					<el-tag type="danger" effect="plain" size="small" v-if="scope.row.status_upload=='NEEDDELETE'">删除中</el-tag>
						
						
					<el-tag type="error" effect="plain" size="small" :title="scope.row.errormsg" v-if="scope.row.status_upload=='ONLINEFAIL'">上架失败</el-tag>
					<el-tag type="error" effect="plain" size="small" :title="scope.row.errormsg" v-if="scope.row.status_upload=='UPLOADFAIL'">发布失败</el-tag>
					<el-tag type="error" effect="plain" size="small" :title="scope.row.errormsg" v-if="scope.row.status_upload=='OFFLINEFAIL'">下架失败</el-tag>
					<el-tag type="error" effect="plain" size="small" :title="scope.row.errormsg" v-if="scope.row.status_upload=='DELETEFAIL'">删除失败</el-tag>
					</p>
					
					<p class="text-omit-2 text-m-c">
					<el-tooltip
					 placement="top"
					 :content="scope.row.pname"> 
					
					<span class="font-extraSmall">{{scope.row.pname}}</span>
					</el-tooltip>	
					</p>
					<p class=" font-extraSmall"><span class="font-extraSmall">SKU: </span>{{scope.row.sku}} </p>
					<p class="font-extraSmall">店铺：{{scope.row.gname}}-{{scope.row.marketname}}</p>
				</template>
			</el-table-column>
			<el-table-column  label="品牌备案" prop="gname" width="140" >
				<template #default="scope">
				 <!-- <p>{{scope.row.gname}}-{{scope.row.marketname}}</p> -->
				 
				 <!-- <p>{{scope.row.pbrand}}</p> -->
				 <a 
				 style="color: black;text-decoration-line: none" 
				 :href="'https://branddb.wipo.int/zh/quicksearch/results?by=brandName&v='+scope.row.pbrand"
				 target="_blank"
				 >
				 
				 <i class="el-icon-menu"></i>{{scope.row.pbrand}}</a>
				 
				 
				</template>
			</el-table-column>
			<el-table-column  label="定时任务" prop="timename" width="120" >
				<template #default="scope">
					<div>{{scope.row.timename}}</div>
					<div>{{scope.row.timerange}}</div>
				</template>
				</el-table-column>
			
			
			<el-table-column  label="定价信息"  width="250" >
			  <template #default="scope">
				  <div class="font-extraSmall" v-if="scope.row.minoffer">
				  	 <span class="font-extraSmall">列表最低：
				  		 {{scope.row.minoffer.listing_price_amount}}+{{scope.row.minoffer.shiping_amount}}={{formatFloat(scope.row.minoffer.listing_price_amount+scope.row.minoffer.shiping_amount)}}
				  	 </span>
				  	 <el-tag style="margin-left:5px;"  type="warning" @click="handleFollowListRecord(scope.row)" title="抢最低价" class="pointer" size="small">抢</el-tag> 
				    </div>
				   <div class="font-extraSmall" v-else>暂无记录</div>
				   <p class="font-extraSmall">
						<span v-if="scope.row.winoffer">购物车价：
							{{scope.row.winoffer.listing_price_amount}}+{{scope.row.winoffer.shiping_amount}}={{formatFloat(scope.row.winoffer.listing_price_amount+scope.row.winoffer.shiping_amount)}}
						</span>
				   </p>
			       <span class="font-extraSmall">我的出价：
				   {{scope.row.buyprice}}+
				   <span class="font-extraSmall" v-if="scope.row.buyshipprice">
				   {{scope.row.buyshipprice}}={{formatFloat(scope.row.buyprice+scope.row.buyshipprice)}}
				   </span>
				   <span class="font-extraSmall" v-else>0.00</span>
				   <el-icon @click="changeFBMPrice(scope.row)"><Edit/></el-icon>
				   <!-- <el-tag style="margin-left:5px;"  type="success" title="我的出价" class="pointer" size="small">我</el-tag> --> 
				  <!-- <el-icon class="text-green"><CircleCheckFilled /></el-icon> -->
				  </span>
				  <p class="font-extraSmall">我的定价：
				  {{scope.row.assumeprice}} - {{scope.row.price}}</p>
				   <p class="font-extraSmall">
					   运费金额：
					   <span v-if="scope.row.buyshipprice">{{scope.row.buyshipprice}}</span>
					    <span v-else>0.00</span>
				   <el-popover
				       placement="right"
				       title="调价记录"
				       :width="430"
				       trigger="click"
				   	@before-enter="loadPriceRecord(scope.row)"
				     >
				     <span class="font-extraSmall">注：调价记录只显示近七天！</span>
				       <template #reference>
					 <el-tag style="margin-left:5px;"  type="success" title="调价记录" class="pointer" size="small">Log</el-tag>
				       </template>
				   	<el-scrollbar style="height:calc(50vh)">
				   	<div v-if="scope.row.pricelist && scope.row.pricelist.length>0" class="list-item" v-for="item in scope.row.pricelist">
				   	<el-space  direction="vertical">
				   		<p>
				   		<h5><el-icon class="font-base"><EditPen /></el-icon>{{item.remark}}</h5>
				   		<p class="font-extraSmall">{{dateTimesFormat(item.opttime)}}</p>
				   		</p>
				   	</el-space>
				   	<el-space :size="32">
				   		<div>
				   		<b>{{item.lowestprice}}</b>
				   		<p class="font-extraSmall">当前承受价</p>
				   		</div>
				   		<div>
				   		<div>	
				   			<b>{{item.refprice}}
				   			</b>
				   		</div>
				   		<p class="font-extraSmall">参考价格</p>
				   		</div>
				   		<div>
				   			<el-icon :size="24"><Right ></Right></el-icon>
				   		</div>
				   		<div>
				   		<b>{{item.price}}+{{item.shipprice}}={{formatFloat(item.price+item.shipprice)}}</b>
				   		<p class="font-extraSmall">调整价格</p>
				   		</div>
				   	</el-space>
				   	</div>
				   	<div v-else>
				   		暂无记录！
				   	</div>
				   	</el-scrollbar>
				     </el-popover>
				   </p>
				   
				   
				   
			  </template>
			</el-table-column>
			 
		 
<!-- 			<el-table-column   label="调价时间/调价记录"  width="155"     >
				<template #default="scope">
					<p> {{dateTimesFormat(scope.row.pricetime)}}</p> 
						<el-popover
						    placement="right"
						    title="调价记录"
						    :width="400"
						    trigger="click"
							@before-enter="loadPriceRecord(scope.row)"
						  >
						  <span class="font-extraSmall">注：调价记录只保留近七天！</span>
						    <template #reference>
						     <el-button type="primary"  size="small" plain>
								 调价记录
							 
							 </el-button>
						    </template>
							<el-scrollbar style="height:calc(50vh)">
							<div v-if="scope.row.pricelist && scope.row.pricelist.length>0" class="list-item" v-for="item in scope.row.pricelist">
							<el-space  direction="vertical">
								<p>
								<h5><el-icon class="font-base"><EditPen /></el-icon>{{item.remark}}</h5>
								<p class="font-extraSmall">{{dateTimesFormat(item.opttime)}}</p>
								</p>
							</el-space>
							<el-space :size="32">
								<div>
								<b>{{item.lowestprice}}</b>
								<p class="font-extraSmall">当前承受价</p>
								</div>
								<div>
								<div>	
									<b>{{item.refprice}}
									</b>
								</div>
								<p class="font-extraSmall">参考价格</p>
								</div>
								<div>
									<el-icon :size="24"><Right ></Right></el-icon>
								</div>
								<div>
								<b>{{item.price}}+{{item.shipprice}}={{formatFloat(item.price+item.shipprice)}}</b>
								<p class="font-extraSmall">调整价格</p>
								</div>
							</el-space>
							</div>
							<div v-else>
								暂无记录！
							</div>
							</el-scrollbar>
						  </el-popover>
				</template>
			</el-table-column> -->
			<el-table-column   label="跟卖者"  width="250"  >
				<template #default="scope">
                    <p v-if="scope.row.listoffers && scope.row.listoffers.length>0">
					     <div  v-for="item in scope.row.listoffers" @mouseenter="e=>handleOffers(e,scope.row.offers)" @click="e=>handleOffers(e,scope.row.offers)" class="pointer">
								<el-space>
								<span style="width:130px" v-if="item.isself" class="text-orange">{{item.sellerid}}</span>
								
								<el-tag style="margin-left:0px;" v-if="item.isself" type="success" title="我自己" size="small" effect="dark">我</el-tag>
								
								<span style="width:130px" v-else  >{{item.sellerid}}</span>
								
								<el-tag style="margin-left:0px;" v-if="item.is_buy_box_winner==true && !item.losttime" type="danger" title="购物车拥有人" size="small" effect="dark">车</el-tag>
								
								<!-- <el-link :href="item.href" target="_blank">
								<el-icon><Link /></el-icon>
								</el-link> -->
								</el-space>
					      </div>
					</p>
					<p v-else>暂无记录</p>
				</template>
			</el-table-column>
			
			<el-table-column  label="库存/发货天数" prop="delivery_cycle" width="120" >
			  <template #default="scope">
				{{scope.row.quantity}} / {{scope.row.delivery_cycle}}天
			  </template>
			</el-table-column>
			
			<el-table-column  label="评论/国家/备货" prop="createopt" width="150" >
			  <template #default="scope">
				<p v-if="scope.row.listoffers && scope.row.listoffers.length>0" v-for="(item,index) in scope.row.listoffers">
				 <div v-if="item.is_buy_box_winner || item.isself">
				{{getValue(item.feedback)}} / {{item.ships_from_country}} / {{item.minimum_hours/24}}-{{item.maximum_hours/24}}
				 </div>
				</p>
				<p v-else>暂无记录</p>
			  </template>
			</el-table-column>
			<el-table-column  label="售价(含运费)" prop="createopt" width="138" >
			  <template #default="scope">
				  
				<p v-if="scope.row.listoffers && scope.row.listoffers.length>0" v-for="(item,index) in scope.row.listoffers">
				 <div v-if="item.is_buy_box_winner || item.isself">
				{{item.listing_price_amount}}+{{item.shiping_amount}}={{formatFloat(item.listing_price_amount+item.shiping_amount)}}
				 </div>
				</p>
				<p v-else>暂无记录</p>
			  </template>
			</el-table-column>
			
			<!-- <el-table-column   label="排名" prop="rank" width="80"   /> -->
<!-- 			<el-table-column   label="购物车价格"  width="140" >
			  <template #default="scope">
				  <span v-if="scope.row.winoffer">
					  {{scope.row.winoffer.listing_price_amount}}+{{scope.row.winoffer.shiping_amount}}=
					  {{formatFloat(scope.row.winoffer.listing_price_amount+scope.row.winoffer.shiping_amount)}}
				  </span>
				  <span v-else>暂无记录</span>
			  </template>
			</el-table-column> -->
			
			<el-table-column  label="累计出单/出单时间" prop="orders_sum" width="160" sortable="custom">
			<template #default="scope">
				<div>{{scope.row.orders_sum}}</div>
				<div>{{scope.row.last_order_time}}</div>
				<div>
			   <el-space>
				   <div>
			    <span class="font-extraSmall">当前库存:</span>
				<span class="font-extraSmall" v-if="scope.row.fulfillment_availability">{{scope.row.fulfillment_availability}}</span>
				<span class="font-extraSmall" v-else>未同步/暂无</span>
				</div>
				<el-button link :loading="scope.row.invloading" @click="loadInventory(scope.row)">
					<el-icon class="font-extraSmall pointer" title="刷新库存"  ><Refresh /></el-icon>
				</el-button>
				<el-button link :loading="scope.row.fillloading"  @click="fillInventory(scope.row)">
					<el-icon class="font-extraSmall pointer" title="根据水位线补齐库存" ><WalletFilled /></el-icon>
				</el-button>
			
			 </el-space>
				</div>
			</template>
			</el-table-column>
			<el-table-column   label="备注" prop="remark" width="100"   >
				<template #default="scope">
					<p class="text-omit-2 text-m-c">
					<el-tooltip
					 placement="top"
					 :content="scope.row.remark"> 
					{{scope.row.remark}}
					</el-tooltip>	
					</p>
				</template>
				</el-table-column>
				
				<el-table-column  label="更新时间" prop="createopt" width="120" >
				  <template #default="scope">
					<p v-if="scope.row.listoffers && scope.row.listoffers.length>0" v-for="(item,index) in scope.row.listoffers">
					 <div v-if="item.is_buy_box_winner || item.isself">
					{{dateTimesFormat(item.opttime)}}
					</div>
					</p>
					<p v-else>暂无记录</p>
				  </template>
				</el-table-column>
				
				<el-table-column  label="创建时间" prop="createtime" width="105" sortable="custom">
					<template #default="scope">
						<!-- <p>{{scope.row.creatorname}}</p> -->
						{{dateTimesFormat(scope.row.createtime) }}
					</template>
				</el-table-column>
				
		    <el-table-column  fixed="right" label="操作" width="80"  >
		        <template #default="scope">
					<el-space direction="vertical" >
		            <el-button  @click="handleEdit(scope.row)" link type="primary">编辑</el-button>
		            <el-button  type="danger" @click="Remove(scope.row)" link>删除</el-button>
		            <el-button  type="primary" @click="asinLog(scope.row.pid)" link>日志</el-button>
					</el-space>
		        </template>
		    </el-table-column>
			
			</template>
		</GlobalTable>
	</div>
	</div>
	<FollowEdit @change="refreshTable" ref="editRef"/>
	<AsinLog ref="asinLogRef"/>
	
	<el-dialog
	title="批量修改跟卖时间段"
	v-model="timevisable"
	width="400px"
	>
	 <el-select v-model="changeTimeid"   style="width:100%;">
	 	<el-option v-for="item in changetimelist" :key="item.id" :label="item.fullname"   :value="item.id">
	 	</el-option>
	 </el-select>
	<template #footer>
		<el-button @click="timevisable=false">关闭</el-button>
		<el-button @click="submitChangeTime" type="primary">提交</el-button>
	</template>
	</el-dialog>
	<el-dialog
	title="修改全部产品发货天数"
	v-model="cyclevisable"
	width="400px"
	>
	 	<el-input v-model="allCycle" @input="allCycle=CheckInputInt(allCycle)"></el-input>
	<template #footer>
		<el-button @click="cyclevisable=false">关闭</el-button>
		<el-button @click="submitAllCycle" type="primary">提交</el-button>
	</template>
	</el-dialog>
	<BatchEditDialog @change="refreshTable" ref="batchEditDialogRef"/>
	
	
	<el-popover
	    placement="right"
	    title="卖家列表"
	    :width="400"
	    trigger="click"
		:virtual-ref="offerPopverRef"
		virtual-triggering
	  >
	    <template #reference>
			 
	    </template>
		<el-scrollbar style="height:calc(50vh)">
		<div v-for="(item,index) in offers">
		<div v-if="!item.losttime" class="list-item" >
		<el-space >
			
			<span v-if="item.isself" class="text-orange">{{item.sellerid}}</span>
			<el-tag style="margin-left:5px;" v-if="item.isself" type="success" title="我自己" size="small" effect="dark">我</el-tag>
			
			<span v-else  >{{item.sellerid}}</span>
			<el-tag style="margin-left:5px;" v-if="item.is_buy_box_winner==true && !item.losttime" type="danger" title="拥有购物车" size="small" effect="dark">购物车</el-tag>
			<el-link :href="item.href" target="_blank">
			<el-icon><Link /></el-icon>
			</el-link>
		</el-space>
		<el-space :size="32">
			<div>评论：{{getValue(item.feedback)}} </div> 
			<div>国家：{{item.ships_from_country}} </div> 
			<div v-if="item.minimum_hours">备货： {{item.minimum_hours/24}}-{{item.maximum_hours/24}} 天</div> 
		</el-space>
		<el-space :size="32">
			<div>售价(含运费)：{{item.listing_price_amount}}+{{item.shiping_amount}}={{formatFloat(item.listing_price_amount+item.shiping_amount)}}</div> 
		</el-space>
		<el-space :size="32">
			<div>更新时间：{{dateTimesWithoutYearFormat(item.opttime)}} </div> 
		</el-space>
		</div>
		</div>
		</el-scrollbar>
	  </el-popover>
 
	
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted, h}from"vue";
	import { ElDivider } from 'element-plus'
	import FollowEdit from './components/followEdit.vue'
	import BatchEditDialog from './components/batchEditDialog.vue'
	import AsinLog from './components/asinLog.vue'
	import {Location,CollectionTag,CircleCheckFilled,EditPen,Right,Link,Refresh,WalletFilled} from '@element-plus/icons-vue'
	import {} from '@element-plus/icons-vue';
	import Header from "./components/header.vue";
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import followTimeApi from '@/views/customized/amazon_follow_ui/js/followTimeApi.js';
	import {dateFormat,dateTimesFormat,getValue,dateTimesWithoutYearFormat,formatFloat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	import listingApi from '@/api/amazon/listing/listingApi.js';
	const spacer = h(ElDivider, { direction: 'vertical' })
	const editRef = ref()
	const asinLogRef = ref()
	const globalTable=ref();
	const offerPopverRef=ref();
	const batchEditDialogRef=ref();
	const state=reactive({
		 tableData:{records:[],total:0},
		 checkrows:[],
		 queryParams:{},
		 btnloading:{upload:false,sales:false,unsales:false,delete:false},
		 timevisable:false,
		 cyclevisable:false,
		 defaultSort:{"prop": 'createtime', "order": 'descending' },
		 offers:[],
		 changeTimeid:null,
		 changetimelist:[],
		 allCycle:null,
		formData: {
		}, 
	})
	const{
	tableData,
	formData,
	defaultSort,
	checkrows,
	btnloading,
	queryParams,
	timevisable,
	offers,
	cyclevisable,
	changeTimeid,
	changetimelist,
	allCycle,
	}=toRefs(state)
	function Remove(row){
		var pid=row.pid;
		ElMessageBox.confirm(
			'请确认是否删除？',
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
					var list=[];
					list.push(pid);
					followListApi.deleteProfuctFollow(list).then((res)=>{
						  ElMessage({
							type: 'success',
							message: '删除成功',
						  });
						  refreshTable();
					}); 
				 }
			  }
			}
		  )
	}
	
	function handleOffers(e,offers){
		offerPopverRef.value=e.currentTarget;
		state.offers=offers;
	}
	function loadInventory(row){
		var data={"authid":row.amazonauthid,"marketplaceid":row.marketplaceid,"sku":row.sku};
		row.invloading=true;
		listingApi.refreshInfoBySKU(data).then((res)=>{
				row.invloading=false;
				 row.fulfillment_availability=0;
				 if(res.data&&res.data.fulfillmentAvailability&&res.data.fulfillmentAvailability.length>0){
					 res.data.fulfillmentAvailability.forEach(item=>{
						 if(item.fulfillmentChannelCode=="DEFAULT"){
							 row.fulfillment_availability=item.quantity;
							 ElMessage({
								type: 'success',
								message: '操作完成',
							 });
						 }
					 })
				 }else{
					 ElMessage({
						type: 'success',
						message: '操作完成,未找到库存',
					 });
				 }
		}).catch(e=>{
			row.invloading=false;
		}); 
	}
	function fillInventory(row){
		row.fillloading=true;
		followListApi.refreshFulfillableProfuctFollow({"id":row.pid}).then((res)=>{
				  row.fillloading=false;
				  row.fulfillment_availability=0;
				  if(res.data&&res.data.length>0){
				  	   res.data.forEach(fulfill=>{
							fulfill.fulfillmentAvailability.forEach(item=>{
									 if(item.fulfillmentChannelCode=="DEFAULT"){
										 row.fulfillment_availability=item.quantity;
										 ElMessage({
											type: 'success',
											message: '操作完成',
										 });
									 }
							})
					    })
				  }else{
						 ElMessage({
							type: 'success',
							message: '操作完成,未找到库存',
						 });
				  }
		}).catch(e=>{
			row.fillloading=false;
		}); 
	}
	function handleEdit(row){
		editRef.value.show(row)
	}
	function asinLog(pid){
		asinLogRef.value.show(pid)
	}
	function handleQuery(params){
		state.queryParams=params;
		globalTable.value.loadTable(params);
	}
	function refreshTable(){
		handleQuery(state.queryParams);
	}
	function loadTableData(params){
		var groupidstr=params.groupid;
		var groupid=null;
		var marketplaceid=null;
		if(groupidstr.indexOf("-")>=0){
			groupid=groupidstr.split("-")[1];
			marketplaceid=groupidstr.split("-")[0];
			params.groupid=groupid;
			params.marketplaceid=marketplaceid;
		}
		followListApi.getProductFollowList(params).then((res)=>{
			if(res&&res.data&&res.data.total>0){
				res.data.records.forEach(item=>{
				 item.listoffers=[];
				 item.href="https://"+item.pointname+"/dp/"+item.asin+"?th=1&psc=1";
				 if(item.offers&&item.offers.length>0){
					 item.offers.forEach(oitem=>{
					 	  oitem.href="https://"+item.pointname+"/sp?seller="+oitem.sellerid;
						  if(oitem.losttime){
							   console.log(oitem);
						  }else{
							  if(oitem.is_buy_box_winner || oitem.isself){
							  		item.listoffers.push(oitem);
							  }
						  }
						  
					 });
				 }
				 if(item.listoffers.length==0&&item.minoffer){
					 item.listoffers.push(item.minoffer);
				 }
				});
			}
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		});
	}
	function handleSelect(rows){
		state.checkrows=rows;
	}
	function loadPriceRecord(row){
		var nowDate=new Date();
		nowDate.setTime(nowDate.getTime() - 3600 * 1000 * 24 * 7);
		var datestr=dateFormat(nowDate);
		listingApi.getPriceRecord({"pid":row.pid,"byday":datestr}).then((res)=>{
			row.pricelist=res.data;
		});
	}
	function handleFollowListRecord(row){
		listingApi.recordFollowList({"pid":row.pid}).then(res=>{
			ElMessage({
					type: 'success',
					message: '操作成功'
			});
			refreshTable();
		});
	}
	function pushItems(ftype){
		var title="";
		if(ftype=="upload"){
			title="请确认是否批量发布";
		}
		if(ftype=="sales"){
			title="请确认是否批量设置在售";
		}
		if(ftype=="unsales"){
			title="请确认是否批量设置缺货";
		}
		if(ftype=="price"){
			title="请确认是否批量重新调价";
		}
		if(state.checkrows.length==0){
			ElMessage({
					type: 'error',
					message: '请至少选择一行数据'
			})
			return;
		}
		ElMessageBox.confirm(
			title,
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
					 var list=[];
					state.checkrows.forEach(item=>{
						list.push(item.pid);
					});
					if(ftype=="upload"){
						state.btnloading.upload=true;
						followListApi.pushProfuctFollow(list).then((res)=>{
								state.btnloading.upload=false;
								  ElMessage({
									type: 'success',
									message: '已完成操作',
								  });
								  refreshTable();
						}); 
					}
					if(ftype=="sales"){
						state.btnloading.sales=true;
						followListApi.salesProfuctFollow(list).then((res)=>{
							state.btnloading.sales=false;
								  ElMessage({
									type: 'success',
									message: '已完成操作',
								  });
								  refreshTable();
						}); 
					}
					if(ftype=="unsales"){
						state.btnloading.unsales=true;
						followListApi.unsalesProfuctFollow(list).then((res)=>{
								state.btnloading.unsales=false;
								  ElMessage({
									type: 'success',
									message: '已完成操作',
								  });
								  refreshTable();
								  
						}); 
					}
					if(ftype=="price"){
						state.btnloading.price=true;
						followListApi.priceProfuctFollow(list).then((res)=>{
								state.btnloading.unsales=false;
								  ElMessage({
									type: 'success',
									message: '已完成操作',
								  });
								  refreshTable();
								  
						}); 
					}
				 }
			  }
			}
		  )
	}
	function deleteItems(){
		if(state.checkrows.length==0){
			ElMessage({
					type: 'error',
					message: '请至少选择一行数据'
			})
			return;
		}
		 
		ElMessageBox.confirm(
			'请确认是否批量删除？',
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
					 var list=[];
					state.checkrows.forEach(item=>{
						list.push(item.pid);
					});
					state.btnloading.delete=true;
					followListApi.deleteProfuctFollow(list).then((res)=>{
						state.btnloading.delete=false;
							  ElMessage({
								type: 'success',
								message: '已完成操作',
							  });
							  refreshTable();
					}); 
					  
				 }
			  }
			}
		  )
	}
	function changeCycle(){
		state.cyclevisable=true;
	}
	function changePrice(){
		if(state.checkrows&&state.checkrows.length>0){
			batchEditDialogRef.value.show(state.checkrows);
		}else{
			ElMessage({
					type: 'error',
					message: '请至少选择一行数据'
			})
		}
	}
	function changeTime(){
		state.timevisable=true;
		followTimeApi.list().then((res)=>{
			if(res.data){
				res.data.forEach(item=>{
					if(!item.endtime){
						item.fullname=item.name+" ("+item.starttime+"--"+")";
					}else{
						item.fullname=item.name+" ("+item.starttime+"-"+item.endtime+")";
					}
				});
				state.changeTimeid=res.data[0].id;
				state.changetimelist=res.data;
			}
		});
	}
	function submitAllCycle(){
		followListApi.changeAllCycle({"cycle":state.allCycle.toString()}).then((res)=>{
			ElMessage({
					type: 'success',
					message: '操作成功'
			})
			state.cyclevisable=false;
			refreshTable();
		});
	}
	function submitChangeTime(){
		if(state.checkrows && state.checkrows.length>0){
			var pidlist=[];
			pidlist.push(state.changeTimeid.toString());
			state.checkrows.forEach(item=>{
				pidlist.push(item.pid);
			});
			followListApi.changeProfuctFollowTime(pidlist).then((res)=>{
				ElMessage({
						type: 'success',
						message: '操作成功'
				})
				state.timevisable=false;
				refreshTable();
			});
		}else{
			ElMessage({
					type: 'error',
					message: '请至少选择一行数据'
			})
		}
	}
	
</script>

<style scoped>
	.m-b-16{
		margin-bottom:16px;
	}
	.card-page{
		margin:16px;
	}
	.text-m-c{
		color:#666;
	}
	.screen-height{
	  min-height:calc(100vh - 66px)	
	}
	.flex-t{
		display: flex;
	}
	.font-light{
		color:#aaa;
	}
	.list-item{
		margin-bottom:16px;
		padding-bottom:16px;
		border-bottom:1px solid #ddd;
	}
</style>