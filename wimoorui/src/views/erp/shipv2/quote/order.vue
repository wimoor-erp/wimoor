<template>
	<div class="main-sty">
		<div class="con-body">
			 <el-row >
				<el-col :span="24"  >
					<el-tabs v-model="queryOrderParam.status" @tab-change="handleOrderQuery">
						<el-tab-pane label="全部状态" :name="0"   ></el-tab-pane>
						<el-tab-pane label="待询价" :name="1"   ></el-tab-pane>
						<el-tab-pane label="待报价" :name="3"   ></el-tab-pane>
						<el-tab-pane label="已有报价" :name="4"   ></el-tab-pane>
						<el-tab-pane label="已结束" :name="5"   ></el-tab-pane>
					    <el-tab-pane label="已废弃" :name="6"   ></el-tab-pane>
					</el-tabs>	
					<div style="margin-bottom: 10px;">
						<el-space>
							<el-button  type="primary" @click="showSupplierDialog"   >批量询价</el-button>
							<Datepicker ref="datepickers" @changedate="changedate" />
							<el-input  v-model="queryOrderParam.search" style="width:600px" clearable   @clear="handleOrderQuery" placeholder="请输入询价单号,货件号,SKU,供应商,地址,国家,店铺,备注" class="input-with-select" >
							   <template #append>
							     <el-button @click.stop="handleOrderQuery">
							        <el-icon class="ic-cen font-medium">
							         <search />
							      </el-icon>
							     </el-button>
							   </template>
							 </el-input>
						</el-space>
						<div style="float:right; ">
							<el-button-group class="ml-4">
							  <el-button type="primary" 
							  size="small" round 
							  :plain="queryOrderParam.displayType=='number'?true:false"   
							  @click="queryOrderParam.displayType='center';handleOrderQuery()"  >地址</el-button>
							  <el-button type="primary" 
							  size="small" round 
							  :plain="queryOrderParam.displayType=='center'?true:false"   
							  @click="queryOrderParam.displayType='number';handleOrderQuery()">批次</el-button>
							</el-button-group>
						</div>
					</div>
					<GlobalTable 
					       ref="orderGlobalTable"
						   style="width: 100%;margin-bottom:16px;"
						   @loadTable="loadOrderTableData" 
						   :tableData="orderTableData"  
						   :defaultSort="{ prop: 'number', order: 'descending' }"  
						   :stripe="true"  
						   :currentPage="1"
						   :size="1"
						   height="calc(100vh - 250px)"
						   :pagesizes="[1,10,20]"
						   :defaultExpandAll="true"
						   :show-header="false"
						  >
						  <template #field>
								<el-table-column label="操作" prop="remark" >
									<template #default="scope">
										 <el-card shadow="never"
										 class="sh-card"
										 >
										 <template #header>
										 	<div class="flex-center-between font-base">
										 			<el-space :size="24">
										 				<div class="flex-center">
										 					<el-checkbox v-model="scope.row.check" size="large" />
										 					<span class="light-font"  style="margin-left:8px">订单号：</span>
										 					<span style="margin-right:8px">{{scope.row.number}}</span>
										 						<el-tag  effect="dark" v-if="scope.row.status==1" type="primary" >待询价</el-tag>
										 						<el-tag  effect="dark" v-if="scope.row.status==3" type="warning" >待报价</el-tag>
										 						<el-tag  effect="dark" v-if="scope.row.status==4" type="success" >已有报价</el-tag>
										 						<el-tag  effect="dark" v-if="scope.row.status==5" type="info" >已结束</el-tag>
																<el-tag  effect="dark" v-if="scope.row.status==6" type="info" >已废弃</el-tag>
										 				</div>
										 				<div>
										 					<span class="light-font">创建时间：</span>
										 					<span>{{dateTimesFormat(scope.row.createtime,"yyyy-MM-dd hh:mm")}} </span>
										 				</div>
													<!-- 	<div>
															<span class="light-font">是否竞价：</span>
															<span v-if="scope.row.isbidding" class="text-blue">是 </span>
															<span v-else class="text-success">否 </span>
														</div> -->
														<div>
															<span class="light-font">材积基数：</span>
															<span >{{scope.row.base}} </span>
														</div>
														<div v-if="scope.row.closetime">
															<span class="light-font">预计结束时间：</span>
															<span>{{dateTimesFormat(scope.row.closetime,"yyyy-MM-dd hh:mm")}}</span>
														</div>
														<div v-if="scope.row.closetime">
															<span class="light-font">渠道类型：</span>
															<span v-if="scope.row.shipmentTranschannel">{{scope.row.shipmentTranschannel.name}}</span>
														</div>
														<div v-if="scope.row.closetime">
															<span class="light-font">时效：</span>
															<span>{{scope.row.days}} 天</span>
														</div>
										 				<p>
										 				<span class="light-font font-base">备注：</span>
										 				<span>{{scope.row.remark||'-'}}</span>
										 				</p>
														<el-button @click="showRemark(scope.row)" :icon="Edit" size="small" title="编辑" ></el-button>  
														<el-button @click="downloadOrder(scope.row)" :icon="Download" size="small" title="导出报价单" ></el-button>
														
										 			</el-space>
													  <el-button @click="disableOrder(scope.row)"  :icon="Delete" title="废弃报价单" size="small"></el-button>
												
										 	</div>
										 	<div>
										 	</div>
										 </template>
										 <div class="quot-card-shipment font-base">
										 	<div class="ship-list">
										 		<p class="m-b-8 font-bold" >
										 			货件列表 <el-icon @click="showAddList(scope.row)" class="pointer"><DocumentAdd /></el-icon>
										 		</p>
										 		
										 		<table class="sd-table" border="0" cellpadding="0" cellspacing="0">
										 			<thead>
										 				<tr>
										 				<th>货件名称</th>
										 				<th>店铺/站点</th>
										 				<th>发货仓库</th>
										 				<th>收货地址</th>
										 				<th width="100px">地址编码</th>
										 				<th width="120px">计算重量(kg)</th>
										 				<th width="120px">体积(cm³)</th>
										 				<th >报价</th>
										 				<th>备注</th>
										 				<th width="120px">操作</th>
										 				</tr>
										 			</thead>
										 			<tbody 
										 			:class="sub.shipmentList.length>1?'trCell':''"
										 			v-for="sub in scope.row.addressList" :key="sub.name">
										 				<tr class="adress-tr" :size="32" v-if="sub.shipmentList.length>1">
										 					<td colspan="10" style="border-top:2px solid  #ebeef5;">
										 					<el-space :size="32" >
										 						<p>
										 							<span >合计：</span>
										 						</p>
										 						<p class="font-small">
										 							<span class="font-extraSmall">编码：</span>
										 							<span v-if="sub.name">{{sub.name}}</span>
																	<span v-else>{{sub.code}}</span>
										 						</p>
																<div v-if="sub&&sub.shipmentList">
																	<span class="light-font">件数：</span>
																	<span>{{sub.shipmentList.length}} </span>
																</div>
										 						<p class="font-small">
										 							<span class="font-extraSmall">重量：</span>
										 							<span class="font-bold">{{sub.weight}} kg</span>
										 						</p>
										 						<p class="font-small">
										 							<span class="font-extraSmall">体积：</span>
																	<span class="font-bold">{{formatFloat(sub.volume/1000000.0)}} m³</span>
										 						</p>
										 					</el-space>
										 					</td>
										 				</tr>
														<tr class="adress-tr" v-else>
															<td colspan="10" style="height:1px;border-bottom:2px solid  #ebeef5;padding:0px">
															 
															</td>
														</tr>
										 				<tr v-for="sh in sub.shipmentList" :key="sh.buyerid" >
										 					<td><div>{{sh.shipmentid}} </div>
															<div class="font-extraSmall">{{sh.number}}</div>
															</td>
										 					<td>
										 						<p>{{sh.groupname}}</p>
										 						<span class='font-extraSmall'>
										 							{{sh.country}}<span v-if="sh.area">({{sh.area}}) </span>
																	<el-tag v-if="sh.isfar" size="small" plain round type="danger">偏远</el-tag>
										 						</span>
										 					</td>
										 					<td>{{sh.warehousename}}</td>
										 					<td >
										 						<div v-if="sh.address"> {{sh.address.addressLine1}}<span v-if="sh.address.addressLine2">,{{sh.address.addressLine2}}</span>,
																{{sh.address.postalCode}}<span v-if="sh.address.phoneNumber">,{{sh.address.phoneNumber}}</span>
																</div>
										 					</td>
										 					<td class="ad-td">{{sh.destination}}</td>
										 					<td>{{sh.calweight}}
															<div class="font-extraSmall">重量：{{sh.weight}}</div>
															</td>
										 					<td>{{formatFloat(sh.volume/1000000.0)}} m³
																<div class="font-extraSmall">材积：{{sh.dimweight}}</div>
															</td>
										 					<td>
										 						<el-popover
										 						 trigger="hover"
										 						 width="700px"
										 						 placement="left"
										 						 :show-after="100"
										 						>
										 						<el-table :data="sh.quotationPriceList" >
										 							<el-table-column label="承运商"    >
										 								<template #default="scope">
																			<div class="text-omit-2">
										 									{{scope.row.supplier.name}}
																			</div>
										 								</template>
										 							</el-table-column>
										 							<el-table-column label="计价方式" width="130">
										 								<template #default="scope">
										 									<p>
										 										<span>材积重：</span>
										 										<span>{{formatFloat(scope.row.weight,1)}}</span>
										 									</p>
										 									<span class="font-extraSmall">材积系数：{{scope.row.base}}</span>
										 								</template>
										 							</el-table-column>
										 							<el-table-column label="单价(￥)" width="70" align="right"  prop="unitprice">
										 							  	<template #default="scope">
																			<div v-if="!scope.row.disabled">{{scope.row.unitprice?.toFixed(2)}}</div>
																			<div v-else class="font-extraSmall">放弃</div>
										 							  	</template>
										 							  </el-table-column>
										 							<el-table-column label="其它/附加费用(￥)" width="120"  align="right" prop="otherfee" >
										 								<template #default="scope">
																			<div v-if="!scope.row.disabled">
																				其它费:{{scope.row.otherfee?.toFixed(2)||0.00}}
																				<div>
																					附加费:{{scope.row.tax?.toFixed(2)||0.00}}
																				</div>
																			
																			</div>
										 									<div v-else class="font-extraSmall">放弃报价</div>
										 								</template>
										 							</el-table-column>
										 							<el-table-column label="总报价(￥)"  align="right" prop="totalfee" >
										 							   <template #default="scope">
																		   <div v-if="!scope.row.disabled">{{scope.row.totalfee?.toFixed(2)}}</div>
																		   <div v-else class="font-extraSmall">放弃报价</div>
										 									
										 							   </template>
										 							</el-table-column>
																	<el-table-column label="备注"   prop="remark" >
																	   
																	</el-table-column>
										 						</el-table>
										 						<template #reference>
										 							<el-button size="small" v-show="sh.quotationPriceList&&sh.quotationPriceList.length>0">查看</el-button>
										 						</template>
										 					  </el-popover>
										 					</td>
										 					<td>{{sh.remark||'-'}}</td>
										 					<td>
										 						<el-button type="primary" link @click="showShipmentInfo(sh)">详情</el-button>
																<el-button type="danger" link @click="handleDelete(sh,scope.row)">删除</el-button>
										 					</td>
										 				</tr>
													 
										 			</tbody>
										 		</table>
										 	</div>
										 	<div class="price-wrap">
										 		<p class="m-b-8 font-bold" >承运商
												<div v-if="scope.row.quotationPriceList&&scope.row.quotationPriceList.length>0" style="float:right">
												<el-button @click="showOverview(scope.row)" size="small" plain round type="success"><el-icon><Search /></el-icon>概览</el-button>
												</div>
												</p>
												
												<div v-if="scope.row.quotationPriceList&&scope.row.quotationPriceList.length>0">
										 		<div class="cus-list" v-for="sub in scope.row.quotationPriceList" :key="sub.id">
										 			<div class="flex-row-top">
										 				<el-space class="cus-name">
										 					<el-icon :class="sub.confirm?'user text-green':'user'"  ><UserFilled /></el-icon>
																<div v-if="isowner">
																<el-tooltip  style="float:left;"   :content="sub.supplier.name"><div  class="text-omit-2">{{sub.supplier.name}}</div></el-tooltip>
																<copy   @click.stop="CopyText(urlFormat(sub.supplier))" title='复制报价链接' theme="outline" size="14" fill="#666"/>
															    </div>
										 					<div v-else>{{sub.supplier.name}}</div>
										 				</el-space>
										 				<div class="flex-center">
										 					<el-space direction="vertical" :size="0" class="pointer" @click="showPriceDetail(scope.row,sub)">
										 						<p style="width:120px"><span class="font-extraSmall">附加费用:
										 						<span v-if="sub.tax">{{sub.tax.toFixed(2)||0.00}}</span>
																<span v-else>0.00</span>
										 						</span>
										 						</p>
										 					 
																<p style="width:120px"><span class="font-extraSmall">总价:</span>
										 						<span class="font-large font-bold">
										 						<span class="font-extraSmall">￥</span>
										 						<el-tooltip content="注意:总价不包含附加费">{{sub.totalfee||'-'}}</el-tooltip></span>
																<div v-if="sub.disabled" class="text-red">
																	<el-button type="danger" link :icon="WarningFilled">
																		放弃报价</el-button>
																	</div>
																</p>
										 						 
										 					</el-space>
										 					<div class="text-right btn-isshow" style="padding-left:5px">
															<el-button
															v-if="!sub.totalfee"
															@click="cancelQuote(sub)"
															 >取消</el-button>
										 					<el-button 
															v-else-if="!sub.confirm"
															:disabled="!sub.confirm&&scope.row.status===5"
															type="primary"
															 @click="confirmPrice(sub)"
															 plain>接受</el-button>
															 <div v-else>
																  <el-button   type="success">已中标</el-button>
																  <el-button link  @click.stop="refreshShipment(sub)" type="primary" >同步</el-button>
															</div>
															
															 </div>
										 				</div>
										 			</div>
										 		</div>
										 	 </div>
											 <div v-else class="text-center" style="padding-top: 32px;">
												 <el-icon :size="32"><ChatLineSquare /></el-icon>
												 <p style="margin-bottom: 24px;">还未发送给承运商询价！</p>
												 <el-button @click="handleShowAddToQuote(scope.row)" type="primary">询价</el-button>
											 </div>
											</div>
										 </div>
										 </el-card>
									</template>
								</el-table-column>
						  </template>
					</GlobalTable>
				</el-col>
			 </el-row>
		</div>
	</div>
	
	<el-dialog v-model="quoteVisiable" title="询价订单信息" width="600px">
			<el-form-item label="报价类型"> 
				<el-select v-model="quoteOrder.ftype">
					<el-option label="批量报价" value="2"  key="2"></el-option>
					<el-option label="地址报价" value="3" key="3"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="结束时间">
					 <el-date-picker v-model="quoteOrder.closetime"
						type="date"
						placeholder="选择日期"
						:size="size">
					 </el-date-picker>
			</el-form-item>
			<el-form-item label="订单备注">
				<el-input   v-model="quoteOrder.remark">
					 
				</el-input>
			</el-form-item>
		  <template #footer>
		  		<el-button @click="saveOrder"   type="primary">确认生成</el-button>
		  	<el-button @click="quoteVisiable=false"  >关闭</el-button>
		  </template>
	</el-dialog>
	
	<el-dialog v-model="shipmentVisiable" title="货件信息" width="60%">
		<div gutter="20" v-if="isowner">
			<div style="display: flex;"  >
				<div style="flex: 1;margin-right:10px" >
				<el-table :data="shipment.boxList">
					<el-table-column label="箱子ID" prop="boxid" show-overflow-tooltip>
								<template #default="scope">
									<div> {{scope.row.boxid}}</div> 
								</template>
					 </el-table-column>
					 <el-table-column label="长(cm)" prop="length"   width="70"    >
					 			<template #default="scope">
									<div>{{scope.row.length}}</div>
					 			</template>
					  </el-table-column>
					  <el-table-column label="宽(cm)" prop="width"   width="70"     >
					  			<template #default="scope">
					  				<div> {{scope.row.width}}</div> 
					  			</template>
					   </el-table-column>
					  <el-table-column label="高(cm)" prop="height"   width="70"     >
					  			<template #default="scope">
					  				<div>{{scope.row.height}}</div> 
					  			</template>
					   </el-table-column>
					 <el-table-column label="重(kg)" prop="weight"   width="70"     >
					 			<template #default="scope">
					 				<div> {{scope.row.weight}}</div> 
					 			</template>
					  </el-table-column>
				</el-table>
			</div>
			<div style="flex: 1;"  v-hasPerm="'erp:pi:list:item'"   >
				<el-table :data="shipment.itemList">
					<el-table-column label="图片" prop="image" width="70">
								<template #default="scope">
									<el-image style="width:40px;height:40px" :src="scope.row.image"> </el-image> 
								</template>
					 </el-table-column>
					 <el-table-column label="名称" prop="name"     show-overflow-tooltip  >
					 			<template #default="scope">
									<div>{{scope.row.name}}</div>
									<div class="font-extraSmall">{{scope.row.ename}}</div>
					 			</template>
					  </el-table-column>
					  <el-table-column label="SKU" prop="sku"    show-overflow-tooltip   >
					  			<template #default="scope">
					  				<div> {{scope.row.sku}}</div> 
									<div class="font-extraSmall"> 材质：{{scope.row.material}}</div> 
					  			</template>
					   </el-table-column>
					  <el-table-column label="数量" prop="quantity"    width="65"   >
					  			<template #default="scope">
					  				<div>{{scope.row.quantity}}</div> 
					  			</template>
					   </el-table-column>
					
				</el-table>
			</div>
			</div>
		</div>
		<div v-else>
			<el-table :data="shipment.boxList">
				<el-table-column label="箱子ID" prop="boxid" show-overflow-tooltip>
							<template #default="scope">
								<div> {{scope.row.boxid}}</div> 
							</template>
				 </el-table-column>
				 <el-table-column label="长(cm)" prop="length"   width="70"    >
				 			<template #default="scope">
								<div>{{scope.row.length}}</div>
				 			</template>
				  </el-table-column>
				  <el-table-column label="宽(cm)" prop="width"   width="70"     >
				  			<template #default="scope">
				  				<div> {{scope.row.width}}</div> 
				  			</template>
				   </el-table-column>
				  <el-table-column label="高(cm)" prop="height"   width="70"     >
				  			<template #default="scope">
				  				<div>{{scope.row.height}}</div> 
				  			</template>
				   </el-table-column>
				 <el-table-column label="重(kg)" prop="weight"   width="70"     >
				 			<template #default="scope">
				 				<div> {{scope.row.weight}}</div> 
				 			</template>
				  </el-table-column>
			</el-table>
		</div>
		<template #footer>
			<el-button @click="shipmentVisiable=false"  >关闭</el-button>
		</template>
	</el-dialog>
	<el-dialog v-model="remarksVisable" title="订单备注" :destroy-on-close='true' width="400px"  >
		<el-form label-width="auto">
					<el-form-item label="渠道"  >
						  <el-select v-model="selectRow.transchannel">
						  	<el-option v-for="item in channelOptions" 
										:label="item.name" 
										:value="item.id"  
										:key="item.id">
							</el-option>
						  </el-select>
					 </el-form-item> 
					<el-form-item label="结束时间">
							 <el-date-picker 
							    v-model="selectRow.closetime"
								placeholder="选择日期"
								type="datetime"
								format="YYYY-MM-DD hh:mm"
								value-format="x"
								:size="size">
							 </el-date-picker>
					</el-form-item>
					 <el-form-item label="到货时效(天)"   >
						  <el-input-number v-model="selectRow.days" />
					  </el-form-item> 
					<el-form-item label="订单备注">
						<el-input   v-model="selectRow.remark"  placeholder="输入备注" :rows="8"  type="textarea" ></el-input>
					</el-form-item>
					</el-form>
	  <template #footer>
	  	<span class="dialog-footer">
			<el-button   type="primary" @click.stop="submitRemark" >提交</el-button>
	  		<el-button @click="remarksVisable = false"> 关闭</el-button>
	  	</span>
	  </template>
	</el-dialog>
	<Overview ref="overviewRef" ></Overview>
	<Supplier ref="supplierRef" @change="handleOrderQuery"></Supplier>
	<Quote ref="quoteRef" :indialog="true" @change="handleAddShipment"></Quote>
	<PriceDialog ref="priceDialogRef"></PriceDialog>
</template>

<script setup>
	import {Search,ArrowDown,Edit,Delete,UserFilled,ChatLineSquare,Link,DocumentAdd,Download,WarningFilled} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne, Form} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import orderApi from '@/api/quote/orderApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
    import {ElMessage,ElMessageBox } from 'element-plus';
	import CopyText from"@/utils/copy_text.js";
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import Supplier from './supplier.vue';
	import Quote from './quote.vue';
	import Overview from './overview.vue';
	import PriceDialog from './components/price_dialog.vue';
	import supplierApi from '@/api/quote/supplierApi.js';
	import transchannelApi from '@/api/quote/transchannelApi.js';
	import '@/assets/css/packbox_table.css';
	const globalTable=ref();
	const orderGlobalTable=ref();
	const overviewRef=ref();
	const supplierRef=ref();
	const buyerRef=ref();
	const quoteRef=ref();
	const priceDialogRef=ref();
	const state = reactive({
		orderTableData:{records:[],total:0},
	    remarksVisable:false,
		queryOrderParam:{
			 status:0,
			 token:null,
			 displayType:"center",
		},
		channelOptions:[],
		isowner:false,
		selectRows:null,
		selectOrderRows:null,
		quoteVisiable:false,
		shipmentVisiable:false,
		quoteOrder:{
			ftype:"2",
			closetime:null,
			remark:null,
			shipmentids:null,
		},
		editRow:null,
		shipment:{},
		selectRow:null,
		
	})
	const{
		 selectRows,quoteVisiable,quoteOrder,orderTableData,queryOrderParam,selectOrderRows,shipmentVisiable,editRow,
		 shipment,remarksVisable,selectRow,isowner,channelOptions
	}=toRefs(state)
	
	function submitRemark(){
		if(state.selectRow.days){
			state.selectRow.days=parseInt(state.selectRow.days);
		}
		orderApi.updateOrderRemark(state.selectRow).then(res=>{
			state.remarksVisable=false;
			handleOrderQuery();
		})
	}
	function showOverview(order){
		overviewRef.value.show(order);
	}
	function showRemark(row){
		state.selectRow={}
		var data=JSON.parse(JSON.stringify(row));
		state.selectRow.remark=data.remark;
		state.selectRow.transchannel=data.transchannel;
		if(data.days){
			state.selectRow.days=parseInt(data.days);
		}
		state.selectRow.closetime=data.closetime;
		state.selectRow.id=data.id;
		state.remarksVisable=true;
		transchannelApi.listall(state.queryOrderParam.token).then(res=>{
			state.channelOptions=res.data;
		})
	}
	function urlFormat(row){
		var url=location.origin+"/quote?token="+row.token+"&title="+row.name;
		return url;
	}
	function showAddList(row){
		state.editRow=row;
		quoteRef.value.show();
	}
	function handleAddShipment(selectShipment){
		var param=[];
		selectShipment.forEach(item=>{
			var data={"orderid":state.editRow.id,"shipmentid":item.shipmentid};
			param.push(data);
		})
		orderApi.addOrderShipment(param).then(res=>{
			 handleOrderQuery();
		})
	}
	function showShipmentInfo(row){
		state.shipment=row;
		state.shipmentVisiable=true;
	}
	function showPriceDetail(row,price){
		priceDialogRef.value.show(row.id,price.supplierid);
	}
 //日期改变
 function changedate(dates,value,type){
 	state.queryOrderParam.fromDate=dates.start;
 	state.queryOrderParam.toDate=dates.end;
 	if(type!="load"){
 	    handleOrderQuery();
 	}
 }
	function handleOrderQuery(){
		 orderGlobalTable.value.loadTable(state.queryOrderParam);
	}
	function loadOrderTableData(params){

		  orderApi.listOrder(params).then(res=>{
			  		state.orderTableData.records=[];
					nextTick(()=>{
						state.orderTableData.records=res.data.records;
						state.orderTableData.total=res.data.total;
					});
				
		  }).catch(e=>{
			  state.orderTableData.records=[];
			  state.orderTableData.total=0;
		  })
	}
    function handleDelete(shipment,order){
		var param={};
		param.shipmentid=shipment.shipmentid;
		param.orderid=order.id;
		 ElMessageBox.confirm(
		 	     '您确定要从订单中删除此货件?',
		 	     '注意',
		 	    {
		 	      confirmButtonText: '确定',
		 	      cancelButtonText: '取消',
		 	      type: 'warning',
		 	    }
		 	  ).then( () => { 
				  orderApi.deleteOrderShipment(param).then(res=>{
					   ElMessage.success("删除成功!");
					   handleOrderQuery();
				  });
			  })
	}
	function selectChange(selection) {
		 state.selectRows=selection;
	}
	function selectOrderChange(selection){
		 state.selectOrderRows=selection;
	}

	
	function showOrderDialog(){
		if(state.selectRows&& state.selectRows.length>0){
			state.quoteVisiable=true;
		}else{
			 ElMessage.error("至少选择一行数据!");
		}
	}
	function getSelectOrderRows(){
		var selectOrderRows=[];
		state.orderTableData.records.forEach(item=>{
			if(item.check){
				selectOrderRows.push(item);
			}
		})
		return selectOrderRows;
	}
	function handleShowAddToQuote(order){
		var selectOrderRows=[];
		selectOrderRows.push(order);
		supplierRef.value.show(state.queryOrderParam.token,selectOrderRows,"list");
	}
	function showSupplierDialog(){
		var selectOrderRows=getSelectOrderRows()
		if(selectOrderRows&& selectOrderRows.length>0){
			supplierRef.value.show(state.queryOrderParam.token,selectOrderRows,"list");
		}else{
			 ElMessage.error("至少选择一行数据!");
		}
	}
	function confirmPrice(row){
		ElMessageBox.confirm(
			     '您确定接受 “'+row.supplier.name+'” 的报价吗?',
			    '接受报价',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  orderApi.confirmPrice({"priceid":row.id}).then((res)=>{
				  	 ElMessage.success("确定接受报价成功!");
				  	 	handleOrderQuery();
				  });
			  })
	}
	function cancelQuote(row){
		ElMessageBox.confirm(
			     '您确定要取消发送此订单询价给这个供应商?',
			    '取消询价',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  supplierApi.deleteSupplierPrice({"supplierid":row.supplierid,"orderid":row.orderid}).then((res)=>{
				  	 ElMessage.success("取消询价成功!");
				  	 	handleOrderQuery();
				  });
			  })
	}
	function disableOrder(order){
		ElMessageBox.confirm(
			     '您确定要取消此订单询价?',
			    '取消询价',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  orderApi.updateOrderStatus({"orderid":order.id,"status":"6"}).then((res)=>{
				  	 ElMessage.success("取消询价成功!");
				  	 	handleOrderQuery();
				  });
			  })
	}
	function downloadOrder(row){
		orderApi.downloadOrderExport({"orderid":row.id}).then(res=>{
			  ElMessage.success("导出成功!"); 
		})
	}
	
	  function loadToken(){
		  thirdpartyApi.getQuoteToken().then((res)=>{
			state.queryOrderParam.token=res.data.buyertoken;
			state.isowner=res.data.isowner;
			if( state.queryOrderParam.token){
				handleOrderQuery();
			}else{
				state.tableData={records:[],total:0};
				state.orderTableData={records:[],total:0};
			}
			});
	}
	
	 
	function refreshShipment(row){
		//showDetail
		var lists=null;
		orderApi.showDetail({"orderid":row.orderid,"supplierid":row.supplierid}).then(res=>{
			lists=res.data;
			if(lists && lists.length>0){
				orderApi.refreshShipment(lists).then(ress=>{
					 ElMessage.success("操作成功!");
				})
			}else{
				ElMessage.error("没有匹配到物流信息!");
			}
		});
		
	}
	onMounted(()=>{
		      loadToken();
	});
	
</script>

<style scoped>
	.sh-card{
		margin-bottom: 12px;
		
	}
	.sd-table th,.sd-table td{
		border-right: 0px;

	}
	.sd-table{
		border-left: 0px;
	} 
	.quot-card-shipment{
		display: flex;
		padding-left:20px;
		padding-right:20px;
	}
	.m-b-8{
		margin-bottom:8px;
	}
	.price-wrap{
		width: 400px;
		padding-top: 20px;
		padding-bottom: 20px;
		padding-left: 20px;
	}
	.ship-list{
		flex:1;
		border-right:1px solid var(--el-card-border-color);
		padding-top: 20px; 
		padding-bottom: 20px;
		padding-right: 20px;
	}
	.cus-name .user{
	  	border:1px solid var(--el-card-border-color);
		background-color:var(--el-fill-color-blank);
		width: 40px;
		height: 40px;
		border-radius: 20px;
		color:#666;
	}
	.cus-name .text-green{
		color:#67C23A;
		border:1px solid var(--el-color-success-light-5);
	}
	.flex-row-top{
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.adress-tr td{
		background-color: var(--el-bg-color);
		padding-top: 4px;
		padding-bottom:4px;
	}
	.trCell tr .ad-td{
		background-color:var(--el-bg-color);
	}
	.btn-isshow .isqu{
		display: none;
	}
	.btn-isshow:hover .succ{
		display: none;
	}
	.btn-isshow:hover .isqu{
		display: inherit;
	}
</style>
<style>
	.sh-card .el-card__body{
		padding-left: 0px;
		padding-right: 0px;
		padding-top: 0px;
		padding-bottom: 0px;
	}
	.sh-card .el-card__header{
		
	}
	.cus-list .flex-row-top{
		margin-top: 16px;
		background-color:var(--el-fill-color-lighter);
		padding:16px;
		border-radius:8px;
	}
</style>