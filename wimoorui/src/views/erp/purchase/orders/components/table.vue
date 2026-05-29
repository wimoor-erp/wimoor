<template>
	<div v-loading="loading" background="#ffff">
	   <table class="sd-table m-b-16 " style="border-left:none"  border="0" cellpadding="0" cellspacing="0">
			<thead  >
							   <tr>
								   <th width="40%">订单信息</th>
								   <th width="30%">产品信息</th>
								   <th width="30%">供应商信息</th>
								   <th width="15%">采购信息</th>
								   <th v-if="tableState=='4' || tableState=='5' || tableState=='6'" width="150px">到货数</th>
								   <th v-if="tableState=='4' || tableState=='5' || tableState=='6'" width="150px">付款金额</th>
								   <th  width="10%">备注</th>
								   <th  width="100px">
									   <el-space>
										   <div>操作</div>
										     <div>
												 <el-tooltip  placement="top"  content="上一页">
													 <span style="margin-right:3px;" v-if="parseInt(state.queryParams.currentpage)>1"  @click.stop="pageUp()"><el-icon class="pointer" ><ArrowLeft/></el-icon> </span>
													 <span   v-else  style="color:#dedede;margin-right:3px;"><el-icon ><ArrowLeft/></el-icon> </span>
												 </el-tooltip>
											 
												  <el-tooltip   placement="top" content="下一页">
													  
												 <span v-if="parseInt(state.queryParams.currentpage)<(parseInt(state.tableData.total)/parseInt(state.queryParams.pagesize))" 
												  @click.stop="pageDown()"><el-icon class="pointer" ><ArrowRight /></el-icon></span>
											     <span v-else  style="color:#dedede"><el-icon ><ArrowRight/></el-icon> </span>
												 </el-tooltip>
											 </div>
										    
									   </el-space>
								   

								  </th>
							   </tr>
			</thead>
		   <tbody style="border:none" v-if="tableData.records && tableData.records.length>0" v-for="item in tableData.records" >
			  <!-- 空格间距-->
			    <tr class="sep-row"><td :colspan="tableState=='4' || tableState=='5' || tableState=='6'? 10:8"></td></tr>
				   <tr v-for="(sub,index) in item.itemlist">
					   <td  class="border-right-color" >
						   <div v-if="index=='0'">
						  <div class="p-order-title ">
							<div> 
							    <el-space><h4>{{item.number}} </h4> 
							     <el-checkbox v-model="item.ischeck"></el-checkbox>
							     </el-space> 
							 </div>
							  <el-space class="font-small" :spacer="spacer">
								  <div class="flex-center text-gray">
								  <el-icon class="font-base "><Clock /></el-icon>
								  <span class="m-t-r-2-4"> {{dateFormat(item.createdate)}}</span>
								 </div>
								  <span>
									<div class="flex-center text-gray">  
									<el-icon class="font-base "><User /></el-icon>
								  <span class="m-t-r-2-4">  {{item.usrname}}</span>
								  </div>
								  </span>
							  </el-space>
						  </div>
						  <el-row  :gutter="24">
							  <el-col  :sm="24" :md="24" :lg="16">
								  <p class="font-small"><span class="font-extraSmall">入库仓: </span>{{item.warehouse}}
								  <el-icon v-if="item.hasStatus1==item.itemlist.length" class="font-base" @click="handleWarehouseShow(item.id)"><Edit/></el-icon>
								  </p>
							  </el-col>
							  <el-col :sm="24" :md="24" :lg="8">
								  <p class="font-small" ><span class="font-extraSmall">总金额: </span><span class="word-break">￥{{item.orderprice}}</span></p>
							  </el-col>
						  </el-row>
						  
						  <div class="table-edit-flex font-small"
						  @click="e=>getRemarks(e,item,'form')">
						  				<span v-if="item.remark!=''&&item.remark!=undefined">
						  				<span class='font-extraSmall'>备注:</span>
						  				{{item.remark}}</span>
						  				<span v-else ><span class='font-extraSmall'>备注:</span>-</span>
						   <el-icon><Edit /></el-icon>
						  	</div>
						 
						 <el-space class="m-t-8">
							 <el-button size="small" link class="text-gray"   @click="downloadExcel(item)">
								<install theme="filled" size="16"  :strokeWidth="3"/> 
							 	&nbsp;导出
							 </el-button>
							 <el-dropdown v-if="item.hasStatus1>0" trigger="click">
							    <el-button size="small"   link class="text-gray" >
									<audit theme="filled" size="16"  :strokeWidth="3"/>
							    	&nbsp;批量审核
							    </el-button>
							      <template #dropdown>
							        <el-dropdown-menu>
							          <el-dropdown-item @click="approveAll(item)">通过</el-dropdown-item>
							          <el-dropdown-item @click="approveReturnAll(item)">驳回 </el-dropdown-item>
							        </el-dropdown-menu>
							      </template>
							    </el-dropdown>
								<el-popconfirm title="自动付完货物费用,确认全部付款吗?"
								  width="220"
								:icon="InfoFilled"  @confirm="autoPaymentAll(item)" v-if="item.hasStatus2>0">
								    <template #reference>
								      <el-button size="small" link class="text-gray">
										<pay-code-one theme="filled" size="16"  :strokeWidth="3"/>  
										&nbsp;自动付款
								      </el-button>
								    </template>
								  </el-popconfirm>
								  <el-popconfirm title="确认全部自动收货吗?" width="160" :icon="InfoFilled"  @confirm="autoReceiveAll(item)" v-if="item.hasStatus2>0" >
								      <template #reference>
								        <el-button  size="small" link class="text-gray">
											<receive theme="filled" size="17"  :strokeWidth="3"/>
								        	&nbsp;自动收货
								        </el-button>
								      </template>
								    </el-popconfirm>
									  <el-button @click="showAutoShipfeeAll(item)" size="small" link class="text-gray">
										<pay-code-one theme="filled" size="16"  :strokeWidth="3"/>  
										&nbsp;自动付运费
									  </el-button>
									  <el-button @click="cancelAllOrder(item)" size="small" link class="text-gray">
										  <el-icon size="16"><Unlock /></el-icon>
											&nbsp;整单撤回
									  </el-button>
									 
						 </el-space>
						 </div>
					   </td>
					   <td>
						   <div class="flex-center">
							   <div  @click.stop="handleToMaterial(sub)">
								   <el-image v-if="sub.image" :src="sub.image" class="img-80 pointer"  ></el-image>
								   <el-image  v-else :src="$require('empty/noimage40.png')"  class="img-80 pointer"   ></el-image>
							   </div>
							   <div >
								   <div class="text-omit-2" >{{sub.mname}}</div>
								   <p  ><span class="sku">{{sub.sku}} 
								    <copy  @click.stop="CopyText(sub.sku)" title='复制SKU' style="padding:3px;" theme="outline" size="14" fill="#666" :strokeWidth="3"/>
								   </span>
								   <el-tag  size="small" v-if="sub.issfg=='2'" effect="plain" type="warning" @click.stop="showAssembly(sub)">组装</el-tag>
								   <span  class='tag-mr' v-if='sub.TagNameList'  v-for='(s,index) in sub.TagNameList' :key='index'>
								       <el-tag size="small"  effect="plain" :title="s.name" :type="s.color">  {{s.name}}</el-tag>
								   </span>
								   </p>
								   <div class="m-t-8 flex-center">
									   <el-tag size="small" v-show="sub.auditstatus==3" type="info">已完成</el-tag>
									   <el-tag size="small" v-show="sub.auditstatus==1" type="warning">待审核
									   <span v-if="sub.ischange==1" style="color: #ff0f47;">
									        <el-icon @click="handlePurchase(sub)"><Warning /></el-icon>
									   </span>
									   </el-tag>
									   <el-tag size="small" v-show="sub.auditstatus==0" type="danger">已作废</el-tag>
									   <el-tag size="small" v-show="sub.auditstatus==2 && sub.paystatus==0&&sub.inwhstatus==1" type="blue">待付款</el-tag>
									   <el-tag size="small" v-show="sub.auditstatus==2 && sub.paystatus==0&&sub.inwhstatus==0" type="blue">待付款</el-tag>
									   <el-tag size="small" v-show="sub.auditstatus==2 && sub.inwhstatus==0&&sub.paystatus==1" type="blue">待收货</el-tag>
									   <el-tag size="small" v-show="sub.paystatus==3" type="warning">已请款</el-tag>
									   <div v-if="sub.orderstatus">
										  <span v-if="orderStatus[sub.orderstatus]" class="tag-group"><span class="before">1688</span>
										  <span :class="orderStatus[sub.orderstatus].color" style="padding-left:2px">  {{orderStatus[sub.orderstatus].simple}} </span> <a href=""></a>
										  </span> 
										  <span v-else class="tag-group"><span class="before">1688</span>待收货</span> 
									   </div>
									   
									   <el-tag type="danger" v-if="sub.changenumber&&sub.changeauditstatus==1" class="pointer" style="margin-left:10px;" 
									   :title="'换货单编号:'+sub.changenumber"  size="small" 
									   @click="handleChangeForm(sub.changenumber)" effect="dark">
									   换货中(采购待确认)</el-tag>
									   <el-tag type="danger" v-if="sub.changenumber&&sub.changeauditstatus==2" class="pointer" style="margin-left:10px;"
									   :title="'换货单编号:'+sub.changenumber"  size="small" 
									   @click="handleChangeForm(sub.changenumber)" effect="dark">
									   换货中(仓库待入库)</el-tag>
									   <el-tag type="success" v-if="sub.changenumber&&sub.changeauditstatus==3" class="pointer" style="margin-left:10px;"
									   :title="'换货单编号:'+sub.changenumber"  size="small" 
									   @click="handleChangeForm(sub.changenumber)" effect="dark">
									   换货完成</el-tag>
									   <el-tag type="primary" v-if="sub.changenumber&&sub.changeauditstatus==0" class="pointer" style="margin-left:10px;"
									   :title="'换货单编号:'+sub.changenumber"  size="small" 
									   @click="handleChangeForm(sub.changenumber)" effect="dark">
									   换货取消</el-tag>
								   </div>
							   </div>
						   </div>
						  <!-- <div v-if="sub.auditstatus==2 && sub.paystatus==3"><span class="font-extraSmall">付款方式:</span>
						   <el-tag  type="warning" size="small" effect="plain" round>申请付款</el-tag>
						   </div> -->
						 </td>
					   <td>
						  <el-space direction="vertical" alignment="left">
							  <p><span class="text-gray">供应商:</span ><el-link :underline="false"  @click.stop="handleToSupplier(sub.purchaseUrl)">{{sub.suppliername}}</el-link></p>
							  <el-space><span class="text-gray">供货周期:</span>
							  <el-popover
							    v-if="sub.auditstatus==1"
							    placement="top-start"
							    title="供货周期"
							    :width="200"
							    trigger="click"
							  	@show="showDatePopover(sub)"
							  >
							  	<el-input
							  	    v-model="sub.delivery_cycle2"
							  	    :rows="2"
							  	    type="textarea"
							  	    placeholder="请输入"
							  	  />
							  		<el-button type="primary" class='m-t-8' @click="updateCycleDate(sub.materialid,sub.delivery_cycle2,sub)">确定</el-button>
							    <template #reference>
							  		<span class="table-edit-flex">
							      <span v-if="sub.delivery_cycle!=''&&sub.delivery_cycle!=undefined">{{sub.delivery_cycle}}</span>
							  							   <span v-else >-</span>
							      <el-icon><Edit /></el-icon>
							  		</span>
							    </template>
							  </el-popover>
							  <span v-else >
								  <span class="table-edit-flex">
								  <span v-if="sub.delivery_cycle!=''&&sub.delivery_cycle!=undefined">{{sub.delivery_cycle}}</span>
								  <span v-else >-</span>
								 </span>
							  </span>
							  </el-space>
							  <p><span class="text-gray">起订量:</span>{{sub.moq}}</p>
						  </el-space>
					   </td>
					   <td >
						   <el-space direction="vertical" alignment="left">
							   <div>
						    <span class="text-gray">单价:</span><span v-if="sub.withoutEdit">￥{{sub.itemprice}}
							<div class="table-edit-flex pull-right">
							 <el-icon v-if=" sub.auditstatus==1" @click="handleChanges(sub)"><Edit /></el-icon>
							</div>
							</span>
						    <el-input v-model="sub.edit_itemprice" @input="changeItemPrice(sub)" v-else></el-input>
							</div>
							<div>
							<span class="text-gray">数量:</span>
						   <span v-if="sub.withoutEdit">{{sub.amount}}
						   <div class="table-edit-flex pull-right">
						    <el-icon v-if=" sub.auditstatus==1" @click="handleChanges(sub)"><Edit /></el-icon>
						   </div>
						   </span>
						   <el-input v-model="sub.edit_amount"   @input="changeItemAmount(sub)" v-else></el-input> 
						   </div>
						   <div>
							<span class="text-gray">金额:</span>
						   <span v-if="sub.withoutEdit">￥{{sub.orderprice}}
						     <div class="table-edit-flex pull-right">
						      <el-icon v-if=" sub.auditstatus==1" @click="handleChanges(sub)"><Edit /></el-icon>
						   	 </div>
						   </span>
						   
						   <el-input  v-model="sub.edit_orderprice"  @input="changeItemOrderprice(sub)" v-else></el-input>
						   </div>
						  
						   </el-space>
						     
						   
						</td>
					   <!-- 收货进度 -->
					   <td v-if="tableState=='4' || tableState=='5' || tableState=='6'">
						   <div>{{sub.totalin}}
							<el-progress :percentage="(sub.totalin/sub.amount)*100"  :status="sub.inwhstatus==0?'':'success'">
							     <el-tag v-if="sub.inwhstatus==0&&sub.totalin>0" effect="plain" type="primary"  round size="small">入库中</el-tag>
						         <el-tag v-else-if="sub.inwhstatus==0" effect="plain" type="warning"  round size="small">未入库</el-tag>
							     <el-tag v-else effect="plain" type="success"  round size="small">已入库</el-tag>
							</el-progress>
						   </div>
						    <p>  <span class="font-extraSmall">预计到货:</span>
						          <DeliveryDate :sub="sub"></DeliveryDate></p>
					   </td >
						<td v-if="tableState=='4' || tableState=='5' || tableState=='6'">
						     <div :id="sub.id">￥{{sub.totalpay}} 
					    </div>
						  <div>
						  	  <el-progress :percentage="(sub.totalpay/sub.orderprice)*100"  :status="sub.paystatus==0?'':'success'">
						  		<el-tag v-if="sub && sub.paystatus==1" effect="plain" type="success"  round size="small">已结清</el-tag>
						  		<el-tag v-else effect="plain" type="warning"  round size="small">未结清</el-tag> 
						  	  </el-progress>
						  </div>
						   <p> <span class="font-extraSmall">审核日期:</span>{{dateFormat(sub.audittime)}}</p>
						 </td>
					   <td >
			   <div class="table-edit-flex font-small" 
			   @click="e=>getRemarks(e,sub,'entry')">
				<span v-if="sub.remark!=''&&sub.remark!=undefined">
				<span class='font-extraSmall word-break'>备注:</span>
				{{sub.remark}}</span>
				<span v-else >-</span>
			    <el-icon><Edit /></el-icon>
			   	</div>
					   </td>
					   <td >
						   <el-space direction="vertical" alignment="left">
					<template v-if="sub.auditstatus==1 || sub.auditstatus==0">
						    <el-button  v-if="sub.auditstatus==1 && sub.withoutEdit==true" link type="primary" @click="handlePass(sub)">通过</el-button>
							<el-button  v-if="sub.auditstatus==1 && sub.withoutEdit==true" link type="primary"  @click="handleReturn(sub)">驳回</el-button>
						<span  v-else-if="sub.auditstatus==0"></span>
						<el-space v-else>
							    <el-button link  type="primary" @click="updateItem(sub)">保存</el-button>
								<el-button link  type="primary" @click="cancelChangesub(sub)">取消</el-button>
						</el-space>
						<el-dropdown :hide-on-click="false">
						  <span class="el-dropdown-link">
							  <el-button type="primary" link>更多
							  <el-icon class="ic-cen"><ArrowDown /></el-icon>
							  </el-button>
						  </span>
						   <template #dropdown>
						    <el-dropdown-menu>
						      <el-dropdown-item @click="handleRecords(sub)">采购记录</el-dropdown-item>
							  <el-dropdown-item v-if="sub.auditstatus==0" @click="reSubmit(sub)">重新提交</el-dropdown-item>
						    </el-dropdown-menu>
						</template>
						</el-dropdown>
						
					</template>
					<el-space v-else direction="vertical" alignment="left">
						<el-button v-if="sub.inwhstatus==0&&tableState=='4' " link type="primary" @click="handleReceipt(sub,'rec')"><span style="padding-right:15px;">收货</span></el-button>
						<el-button v-if="sub.paystatus==0&&tableState=='5'" link type="primary" @click="handlePay(sub,'pay')"><span style="padding-right:15px;">付款</span> </el-button>
						<el-button v-if="sub.paystatus==3" link type="primary" @click.stop="gotoApprovePay(sub)" ><span style="padding-right:10px;">审核</span> </el-button>
						<el-button v-if="sub.auditstatus==3||(sub.inwhstatus==1&&sub.paystatus==1)" link type="primary" @click="handleDetails(sub)">详情</el-button>
						<el-dropdown :hide-on-click="false" >
						  <span class="el-dropdown-link">
						   <el-button type="primary" link>更多
						   <el-icon class="ic-cen"><ArrowDown /></el-icon>
						   </el-button>
						  </span>
						   <template #dropdown>
						    <el-dropdown-menu>
							  <el-dropdown-item v-if="sub.inwhstatus==0&&sub.auditstatus==2&&tableState!='4'"   @click="handleReceipt(sub,'rec')">收货</el-dropdown-item>
						      <el-dropdown-item v-if="sub.auditstatus>=2"    :class="tableState=='4'?'text-orange':''"   @click="handleReceipt(sub,'ret')">退货</el-dropdown-item>
							  <el-dropdown-item v-if="sub.paystatus==0&&sub.auditstatus==2&&tableState!='5'"    @click="handlePay(sub,'pay')">付款</el-dropdown-item>
							  <el-dropdown-item v-if="sub.auditstatus>=2"   :class="tableState=='5'?'text-orange':''"  @click="handlePay(sub,'refund')">退款</el-dropdown-item>
							  <el-dropdown-item v-if="sub.paystatus==0 && sub.totalin==0 && (sub.totalpay==0 || sub.totalpay==0.0) && sub.withoutEdit==true" @click="handleChangesub(sub)">撤回</el-dropdown-item>
							  <el-dropdown-item v-if="sub.totalin==0 && (sub.totalpay==0 || sub.totalpay==0.0)" @click="handleDelete(sub)">作废</el-dropdown-item>
							   <el-dropdown-item v-if="sub.auditstatus>=2"    :class="tableState=='4'?'text-orange':''"   @click="gotoChange(sub)">换货</el-dropdown-item>
							  <el-dropdown-item v-if="sub.auditstatus==2" @click="handleDetails(sub)">详情</el-dropdown-item>
						    </el-dropdown-menu>
						</template>
						</el-dropdown>
					</el-space>
					 
						</el-space>
						</td>
				   </tr>
		   </tbody>
		   <tbody v-else>
			   <tr>
				   
				   <td v-if="tableState=='4' || tableState=='5' || tableState=='6'" colspan="10" style="text-align: center;">暂无匹配数据!</td>
				   <td v-else colspan="8" class="text-center-p font-extraSmall" >暂无匹配数据!</td>
			   </tr>
		   </tbody>
	   </table>
		<!--   </el-scrollbar> -->
	    <pagination
		    background 
	         v-if="tableData.total > 0"
	         :total="tableData.total"
	         v-model:page="queryParams.currentpage"
	         v-model:limit="queryParams.pagesize"
	         @pagination="handleQuery"
			  layout="total,  prev, pager, next, jumper"
			  :page-sizes="[10, 20, 50, 100]"
	       />
		</div>  
	<Records ref="RecordsRef"/>
	<ChangeRecords ref="changeRecordsRef"/>
	<WarehouseDialog :visible="warehouseDailog.visible"  :single="true" @dataChange="dataChange" @handleClose="warehouseDailog.visible=false"/>
	<Receipt ref="ReceiptRef" @change="changeRec" />
	<Payment ref="PaymentRef" @change="changePay"/>
	<Details ref="detailsRef"/>
	<Assembly ref="AssemblyRef"/>
	<AutoPayment ref="autoPaymentRef" @change="handleAutoPriceChange"></AutoPayment>
	<!-- SKU备注popover -->
	<el-popover
	  placement="top-start"
	  title="备注"
		:width="300"
		virtual-triggering
	  trigger="click"
		:virtual-ref="remarkRef"
		:visible="remarkRows.visible"
		@show="showRemarkPopover(remarkRows)"
	>
		<el-input
		    v-model="remarkRows.remark2"
		    :rows="2"
		    type="textarea"
		    placeholder="请输入"
		  />
		  <div class="m-t-8">
		<el-button type="primary"  v-if="remarktype=='entry'"
		 @click="updatenotice(remarkRows.id,remarkRows.remark2,remarkRows,'popover-'+remarkRows.id,proxy,'');remarkRows.visible=false">确定</el-button>
		 <el-button type="primary"  v-if="remarktype=='form'"
		  @click="updateNoticeForm(remarkRows.id,remarkRows.remark2,remarkRows,'popover-'+remarkRows.id,proxy,'');remarkRows.visible=false">确定</el-button>
	  <el-button type="success"  v-if="remarktype=='form'"
	   @click="updateNoticeForm(remarkRows.id,remarkRows.remark2,remarkRows,'popover-'+remarkRows.id,proxy,'all');remarkRows.visible=false">修改SKU备注</el-button>
	  <el-button type="default" plain @click="remarkRows.visible=false">关闭</el-button>
		</div>
	</el-popover>
	
	
	<ShipfeeDialog ref="shipfeeRef" @change="handleQuery"></ShipfeeDialog>
</template>

<script setup>
	import '@/assets/css/packbox_table.css'
	import {h,ref,reactive,toRefs,getCurrentInstance,inject	} from 'vue'
	import {Download,Edit,DeleteFilled,EditPen,TakeawayBox,InfoFilled,Warning ,ArrowDown,Clock,User,ArrowLeft,ArrowRight,Unlock } from '@element-plus/icons-vue';
	import {MoreOne,PayCodeOne,Receive,Install,Audit,Copy} from '@icon-park/vue-next';
	import CopyText from"@/utils/copy_text.js";
	import {ElDivider} from 'element-plus';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import Records from "../records.vue"; 
	import ChangeRecords from "./change_records.vue"; 
	import Assembly from "./assembly.vue"; 
	import Receipt from "./receipt.vue"; 
	import Payment from "./payment.vue"; 
	import Details from "./details.vue"; 
	import ShipfeeDialog from "./shipfee_dialog.vue"; 
	import DeliveryDate from "./deliverydate.vue"; 
	import {useRouter } from 'vue-router'
	import orderStatus from '@/model/erp/purchase/open1688/order_status.json';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import NullTransform from"@/utils/null-transform";
	import WarehouseDialog from "@/views/erp/warehouse/base/warehouseDialog.vue";
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import purchaseExtApi from '@/api/erp/purchase/form/extApi.js';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
	import AutoPayment from "./auto_price_dialog.vue";
	import {updateItem,handleChangesub,changeItemPrice,changeItemOrderprice,
	        changeItemAmount,handleChanges,updatenotice,updateNoticeForm,
	        handleDelete,updateCycleDate,reSubmit} from  '@/hooks/erp/purchase/form.js';	
	const router = useRouter()
	const spacer = h(ElDivider, { direction: 'vertical' })
	const   proxy  = getCurrentInstance();
	const changeRecordsRef=ref();
	const RecordsRef = ref()
	const remarkRef = ref()
	const ReceiptRef =ref()
	const PaymentRef =ref()
	const detailsRef =ref()
	const warehouseRef = ref()
	const AssemblyRef= ref()
	const autoPaymentRef=ref();
	const shipfeeRef=ref();
	let globalTable=ref();
	var isrefresh=router;
	const emitter = inject("emitter");
	const emit = defineEmits(['selectrow','changepay',]);
	const props = defineProps({
	             tableState:String,
	        })
	const {
		tableState,
	} =toRefs(props);		
	const state = reactive({
		warehouseentryid:"",
		warehouseDailog:{visible: false},
		queryParams:{
			currentpage:1,
			pagesize:10,
		},
		loading:false,
		tableData:{records:[],total:0},
		selectrows:[],
		remarkRows:[],
		remarktype:'entry',
	})
	const {
		tableData,loading,
		warehouseDailog,
		queryParams,
		selectrows,
		remarkRows,
		remarktype,
	} = toRefs(state)
 function getRemarks(e,row,remarktypename){
 	const evt = e || window.e || window.event;
 	state.remarkRows= row;
 	remarkRef.value = evt.currentTarget;
	state.remarktype=remarktypename;
 	state.remarkRows.visible = true;		
 }
	function cancelChangesub(row){
		row.withoutEdit=true;
	}
	function showDeliveryModal(sub){
		sub.deliveryDateRef.value.focus();
	}
	//采购记录
	function handleRecords(row){
		RecordsRef.value.show(row.id);
	}
	//采购变更记录
	function handlePurchase(row){
		changeRecordsRef.value.show(row.id);
	}
	//展示组装信息
	function showAssembly(sub){
		AssemblyRef.value.show(sub,"ass");
	}
	function handleWarehouseShow(id){
		state.formid=id;
		state.warehouseDailog.visible=true
	}
	function showRemarkPopover(sub){
	    sub.remark2=sub.remark;
	}
	function showDatePopover(sub){
	    sub.delivery_cycle2=sub.delivery_cycle;
	}	
	function dataChange(warehouse){
		purchaselistApi.updateWarehouse({'id':state.formid,'warehouseid':warehouse.id}).then(res=>{
			ElMessage.success('更换仓库成功');
			state.tableData.records.forEach(item=>{
				if(item.id==state.formid){
					item.warehouse=warehouse.name;
				}
			})
		})
		state.warehouseDailog.visible=false;
	}
	//审核通过
	function handlePass(sub){
		purchaselistApi.approval({"ids":sub.id}).then((res)=>{
			if(res.data){
				sub.auditstatus=2;
				sub.paystatus=0;
				sub.inwhstatus=0;
				ElMessage.success('审核通过 ');
			}
		});
	}
	function handleReturn(sub){
		AssemblyRef.value.show(sub,"return");
	}
	 
	function handleChangeForm(changenumber){
		emitter.emit("removeCache", "换货单");
		router.push({
			path:"/erp/purchase/change",
			query:{
				title:'换货单',
				path:"/erp/purchase/change",
				number:changenumber,
			},
		})
	}
	//下单
	function handleBuy(sub){
		ElMessageBox.confirm(
		   '确定下单吗？',
		   '采购下单',
		   {
		     confirmButtonText: '确认',
		     cancelButtonText: '取消',
		     type: 'warning',
		   }
		 )
		   .then(() => {
		     ElMessage.success('下单成功');
			 sub.state.label = "待收货"
			 sub.state.type = "success"
		   })
		   .catch(() => {
		     ElMessage.info('取消操作');
		   })
	}
	//收货
	function handleReceipt(sub,type){
		ReceiptRef.value.show(type,sub)
	}
	//换货
	function gotoChange(row){
		router.push({
			path:"/e/p/c/c",
			query:{
				title:'添加换货单',
				path:"/e/p/c/c",
				purchaseentryid:row.id,
			},
		})
	}
	
	//付款
	function handlePay(sub,type){
		PaymentRef.value.show(type,sub)
	}
	//采购单详情
	function handleDetails(sub){
		detailsRef.value.show(sub)
	}
	
	function changeselect(row) {
		 if(row && row.isselect==true){
			 state.selectrows.push(row);
		 }else{
			 state.selectrows.forEach(function(item,index){
				 if(item.id==row.id){
				 	state.selectrows.splice(index,1)
				 }
			 });
		 }
		 emit('selectrow',state.selectrows);
	}
	function handleToMaterial(row){
		var type=null;
		if(row.mtype==1 || row.mtype=="1"){
			type='consumable';
		}
		if(row.mtype==2 || row.mtype=="2"){
			type='package';
		}
		if(row.mtype==0 || row.mtype=="0"){
			type='product';
		}
		 router.push({
			path:'/material/details',
			query:{
			  title:"产品详情",
			  path:'/material/details',
			  details:row.materialid,
			  type:type
			}
		 })
	}
	function handleToSupplier(link){
		 window.open(link,"_blank");
	}
	function changePay(){
		emit("changepay");
	}
	function changeRec(){
		emit("changepay");
	}


 function gotoApprovePay(row){
		emitter.emit("removeCache", "请款单");
		router.push({
			path:"/finance/paymentRequest",
			query:{
				title:'请款单',
				path:"/finance/paymentRequest",
				number:row.number,
			},
		})
	}
	function approveAll(row){
		var idslist="";
		row.itemlist.forEach(function(item){
			if(item.auditstatus==1){
				idslist+=((item.id)+",");
			}
		});
		if(idslist!=""){
			purchaselistApi.approval({"ids":idslist}).then((res)=>{
				if(res.data){
					ElMessage.success('订单审核通过成功！');
					row.itemlist.forEach(function(item){
						if(item.auditstatus==1){
							item.auditstatus=2;
							item.paystatus=0;
							item.inwhstatus=0;
						}
					});
				}
			});
		}else{
			ElMessage.error('单据暂无待审核项！');
		}
	}
	function approveReturnAll(row){
		var idslist="";
		row.itemlist.forEach(function(item){
			if(item.auditstatus==1){
				idslist+=((item.id)+",");
			}
		});
		if(idslist!=""){
			purchaselistApi.purchaseReturn({"ids":idslist}).then((res)=>{
				if(res.data){
					ElMessage.success( '订单审核驳回成功！');
					row.itemlist.forEach(function(item){
						if(item.auditstatus==1){
							item.auditstatus=0;
							item.paystatus=0;
							item.inwhstatus=0;
						}
					});
				}
			});
		}else{
			ElMessage.error( '单据暂无待审核项！');
		}
	}
	function autoPaymentAll(row){
		var idslist=[];
		row.itemlist.forEach(function(item){
			if(item.auditstatus==2 && item.paystatus==0){
				idslist.push(item.id);
			}
		});
		if(idslist.length>0){
			autoPaymentRef.value.show(idslist,row);
		}else{
			ElMessage.error('单据暂无付款项！');
		}
	}
	function handleAutoPriceChange(idslist,data,row){
			if(data){
				ElMessage.success('订单付款成功！');
				row.itemlist.forEach(function(item){
					if(idslist.includes(item.id)){
						item.paystatus=1;
						if(item.inwhstatus==1){
							item.auditstatus=3;
						}
						item.totalpay=item.orderprice;
					}
				});
			}
	}
	function autoReceiveAll(row){
		
		var idslist=[];
		row.itemlist.forEach(function(item){
			if(item.auditstatus==2 && item.inwhstatus==0){
				idslist.push(item.id);
			}
		});
		if(idslist.length>0){
			purchaselistApi.autorec(idslist).then((res)=>{
				if(res.data){
					ElMessage.success('订单收货成功！');
					row.itemlist.forEach(function(item){
						if(idslist.includes(item.id)){
							item.inwhstatus=1;
							if(item.paystatus==1){
								item.auditstatus=3;
							}
							item.totalin=item.amount;
						}
					});
				}
			});
		}else{
			ElMessage.error('单据暂无收货项！');
		}
	}
	
	function showAutoShipfeeAll(row){
		shipfeeRef.value.show(row);
	}
	// function autoShipfeeAll(){
	// 	//分摊
	// 	var data={};
	// 	data.entrylist=state.shipList;
	// 	data.shipfee=state.shipfee;
	// 	purchaseExtApi.shareFee(data).then((res)=>{
	// 		if(res.data && res.data.length>0){
	// 			ElMessage.success("分摊成功!");
	// 			state.shipList=res.data;
	// 		}
	// 	});
	// }
	// function submitShipfeeAll(){
	// 	//提交费用
	// 	var amount=0;
	// 	state.shipList.forEach(item=>{
	// 		amount=formatFloat(amount)+formatFloat(item.itemprice);
	// 	})
	// 	if(state.shipfee==amount){
	// 		purchaseExtApi.autoShipPay(state.feedRow.paymethod,state.feedRow.payacc,state.shipList).then((res)=>{
	// 			ElMessage.success("自动付款运费成功!");
	// 			state.shipfeeVisable=false;
	// 			handleQuery();
	// 		});
	// 	}else{
	// 		ElMessage.error("自动付款的总运费和分摊费用总和不相同!");
	// 	}
		
	// }
	function downloadExcel(row){
		var data={};
		data.number=row.number;
		data.warehouseid=row.warehouseid;
		data.supplierid=row.itemlist[0].supplier;
		data.buyerName=row.usrname;
		data.buyerDate=dateFormat(row.createdate);
		data.totalprice=row.orderprice;
		data.remark=row.remark;
		data.creator=row.creator;
		data.formid=row.id;
		purchaselistApi.downloadPurchaseInfo(data);
	}
	function load(params){
		state.queryParams=params;
		state.queryParams.currentpage=1;
		state.queryParams.pagesize=10;
		// state.queryParams.fromDate="2022-01-01";
		// state.queryParams.toDate="2023-02-20";
		//加载表格数据
		handleQuery();
	}
	function handleQuery(){
		state.loading=true;
		purchaselistApi.getListByOrder(state.queryParams).then((res)=>{
			state.loading=false;
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			emit('selectrow',res.data.records);
		});
	}
	function pageUp(){
		if(parseInt(state.queryParams.currentpage)>1){
			state.queryParams.currentpage=parseInt(state.queryParams.currentpage)-1;
			handleQuery();
		}
	}
	function pageDown(){
		if(parseInt(state.queryParams.currentpage)<(parseInt(state.tableData.total)/parseInt(state.queryParams.pagesize)+1)){
			state.queryParams.currentpage=parseInt(state.queryParams.currentpage)+1;
			handleQuery();
		}
	}
	function handleSizeChange(){
	}
	function cancelAllOrder(row){
		if(row.itemlist && row.itemlist.length>0){
			ElMessageBox.confirm(
				   '确定要撤回该采购单吗？',
				   '单据撤回',
				   {
				     confirmButtonText: '确认',
				     cancelButtonText: '取消',
				     type: 'warning',
				   }
				 ).then(() => {
					  row.itemlist.forEach(entry=>{
						  if(entry.auditstatus>1){
							  purchaselistApi.recallEntry({"id":entry.id}).then((res)=>{
									if(res.data){
										entry.auditstatus=1;
										entry.ischange=1;
										entry.withoutEdit=true;
										count=count+1;
									}
							  });
						  }
					  });
					  ElMessage.success('采购单撤回成功！');
					   
				   })
		}
	}
	defineExpose({
	   load,
	})

</script>

<style scoped>
.tag-mr{margin-left:4px;margin-bottom:4px;display:inline-block}
.m-b-16{
	margin-bottom:16px;
}


.sep-row td{
	border-right: 0!important;
	border-left: 0!important;
	background-color: #f1f1f1;
	padding:4px 0;
}
.dark .sep-row td{
	background-color: #545454;
}
.sep-row:hover{
	background-color:#fff!important;
}
.border-right-color{
	border-right:2px solid #f1f1f1 !important;
}
.dark .border-right-color{
	border-right:2px solid #1b1b1d !important
}
.img-80{width:80px;
height:80px;flex: none;
margin-right: 8px;}
tr:hover  .table-edit-flex .el-icon{
	opacity: 1;
}
.font-12{
	font-size: 12px;
}
.text-center-p{
	text-align: center!important;
	padding-top: 32px!important;
}
.p-order-title{
	margin-bottom: 8px;
}
.word-break{
	white-space:nowrap
}
.m-t-r-2-4{
	margin-top: 3px;
	margin-left:4px;
}
.sd-table th, .sd-table td{
	border-right:0px
}
.text-gray{
	color: #999999;
}
.pull-right{
	float:right;
	margin-top:3px;
}
.sku .i-icon-copy:hover, td:hover .i-icon-copy{
    cursor: pointer;
    opacity: 1 !important;
}
.tag-group{
	font-size: 12px;
	border:1px solid #999;
	color: #999;
	padding-right: 2px;
	padding-top: 2px;
	padding-bottom: 2px;
	border-radius: 2px;
	margin-left: 8px;
}
.tag-group .before{
	color: #fff;
	background-color: #999;
	padding: 2px;
}
 .text-omit-3{
	 overflow: hidden;
	 text-overflow: ellipsis;
	 display: -webkit-box;
	 -webkit-line-clamp:3;
	 line-clamp:3;
	 -webkit-box-orient: vertical;
 }
</style>
