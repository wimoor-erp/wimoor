<template>
	<div class="el-white-bg" style="padding-bottom:20px">
		
						 <div class="gird-line-head">
						 <h3>海外仓备货单</h3>
						 <div>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 </div>
						 <div class="fill-from-body"  >
							 <el-steps :active="handlerStep(warehouseform.auditstatus)" finish-status="success" align-center class="m-b-t-24">
							   <el-step title="创建" />
							   <el-step title="审核"  />
							   <el-step title="配货中"  />
							   <el-step title="已发货"  />
							   <el-step title="完成"  />
							 </el-steps>
							 <div style="padding-left:20px;padding-right:20px;">
							 <el-col  :span="22" :offset="2"  >
							<el-descriptions :column="2">
							    <el-descriptions-item label="单据编码">{{warehouseform.number}}</el-descriptions-item>
							    <el-descriptions-item label="发货仓库">{{warehouseform.fromwarehouse}}</el-descriptions-item>
							    <el-descriptions-item label="预计到货日期">
									<span v-if="warehouseform.arrivalTime">{{dateFormat(warehouseform.arrivalTime)}}</span>
									<span v-else>无</span>
								</el-descriptions-item>
								<el-descriptions-item label="海外仓库">{{warehouseform.towarehouse}}</el-descriptions-item>
								<el-descriptions-item label="申请人">{{warehouseform.creator}}</el-descriptions-item>
								<el-descriptions-item label="店铺" v-if="warehouseform.groupname">{{warehouseform.groupname}}</el-descriptions-item>
								<el-descriptions-item label="国家"  >{{warehouseform.country}}</el-descriptions-item>
								<el-descriptions-item label="状态">
									<el-tag v-if="warehouseform.auditstatus==0" type="warning" >未提交</el-tag>
									<el-tag v-if="warehouseform.auditstatus==1" type="info" >待审核</el-tag>
									<el-tag v-if="warehouseform.auditstatus==2" >配货中</el-tag>
									<el-tag v-if="warehouseform.auditstatus==3" >已发货</el-tag>
									<el-tag v-if="warehouseform.auditstatus==4" type="success" >已完成</el-tag>
									<el-tag v-if="warehouseform.auditstatus==5" type="danger" >已驳回</el-tag>
								</el-descriptions-item>
								<el-descriptions-item label="调库时间">{{dateFormat(warehouseform.createdate)}}</el-descriptions-item>
								<el-descriptions-item label="备注">
									<el-popover v-model:visible="warehouseform.revisable" placement="top" :width="240" trigger="click">
										<el-input 	
										    rows="3"  
											maxlength="100"
											type="textarea" 
											v-model="warehouseform.remark2"  
											class='inp-box' 
											placeholder="货件备注" />
										<div style="text-align: right; margin-top: 20px">
											<el-button 
											     size="small" 
												 @click="warehouseform.revisable = false">
												 取消
										    </el-button>
											<el-button 
											    size="small" 
											    type="primary"
												@click="handleUpdateRemark">
												确认
												</el-button>
										</div>
										<template #reference>
											<el-link title="编辑备注" class="icon-text-center"  :underline="false" >
												 {{warehouseform.remark}} <el-icon  style="font-size: 16px;margin-left:10px;"><Edit /> </el-icon>&nbsp;备注
											</el-link>
										</template>
									</el-popover>
								
								</el-descriptions-item>
							  </el-descriptions>
							  </el-col>
						<!-- <el-divider></el-divider> -->
						<div style="padding-top:20px;">
						 <el-tabs v-model="activeName" type="card" class="demo-tabs" @tab-click="handleClick">
						     <el-tab-pane label="调库商品 " name="调库商品">
								 <el-row>
										<el-col :span="2"  >
											<div style="padding-left:10px">
											  <el-link title="复制新增"  class="icon-text-center" :underline="false" @click="handleCopy(warehouseform.id)">
												 <el-icon  style="font-size: 16px;"  class="text-green"><CopyDocument /> </el-icon>&nbsp;复制
											  </el-link>
											  </div>
										</el-col>
										<el-col :span="2">
											  <el-link title="拆分新增"  class="icon-text-center" :underline="false" @click="splitHandel()">
												 <el-icon style="font-size: 16px;" class="text-blue"><Crop /> </el-icon>&nbsp;拆分
											  </el-link>
										</el-col>
										<el-col :span="2">
											  <el-link title="打印配货单"  class="icon-text-center" :underline="false" @click="handlePrint()">
													<Printer theme="outline" size="16" class="text-red" :strokeWidth="2"/>&nbsp;打印配货单
											  </el-link>
										</el-col>
										<el-col :span="2">
											  <el-link title="打印标签"  class="icon-text-center" :underline="false" @click="handleLabelPrint()">
													<Printer theme="outline" size="16"  class="text-orange"  :strokeWidth="2"/>&nbsp;打印标签
											  </el-link>
										</el-col>
								   </el-row>
								  <div class="mark-re">
								 <el-table border :data="dispatchFormEntryList">
								 							 <el-table-column  prop="image" label="图片"  width="80">
								 							    <template #default="scope" >
								 							     <el-image v-if="scope.row.image" :src="scope.row.image"   class="product-img"  ></el-image>
								 							 	<el-image v-else :src="$require('empty/noimage40.png')"   class="product-img"   ></el-image>
								 							   </template>
								 							 </el-table-column>
								 							 <el-table-column  prop="product" label="名称"   show-overflow-tooltip >
								 							    <template #default="scope">
								 							       <div class='name'>{{scope.row.name}}</div>
								 							   </template>
								 							 </el-table-column>
								 							 <el-table-column  prop="sku" label="本地产品SKU"  width="110"></el-table-column>
								 							 <el-table-column  prop="sellersku" label="平台商品SKU"  width="110">
								 								 <template #default="scope">
								 								     <div class='name'>{{scope.row.sellersku}}</div>
								 								     <div class='font-extraSmall'>{{scope.row.fnsku}}</div>
								 								 </template>
								 							 </el-table-column>
								 							 <el-table-column prop="amount" label="发货数量" width="80"/>
								 							 <el-table-column label="长*宽*高(cm)" prop="length" width="180">
								 							 									<template #default="scope">
								 							 										<span >长:{{scope.row.length}}cm</span>,
								 							 										<span >宽:{{scope.row.width}}cm</span>,
								 							 										<span >高:{{scope.row.height}}cm</span>
								 							 									</template>
								 							 </el-table-column>
								 							 <el-table-column label="带包装重量(kg)" prop="weight" width="110"/>
								    <el-table-column   label="调出仓库">
								  	     <el-table-column prop="from_warehouse_inbound" label="待入库" width="70"/>
								 							     <el-table-column prop="from_warehouse_fulfillable" label="可用库存" width="90"/>
								 							     <el-table-column prop="from_warehouse_outbound" label="待出库" width="70"/>
								 							 </el-table-column>
								 							 <el-table-column   label="海外仓库">
								 							      <el-table-column prop="to_warehouse_inbound" label="待入库" width="70"/>
								 							      <el-table-column prop="to_warehouse_fulfillable" label="可用库存" width="90"/>
								 							      <el-table-column prop="to_warehouse_outbound" label="待出库" width="70"/>
								 							  </el-table-column>
								 </el-table>
								  </div>
							 </el-tab-pane>
						     <el-tab-pane label="物流信息" name="物流信息">
								 <logistics :marketplaceid="warehouseform.marketplaceid"
								            :formid="warehouseform.id"
											:country="warehouseform.country">
								 </logistics>
							 </el-tab-pane>
							 <el-tab-pane label="装箱" name="装箱">
								 <div style="padding-bottom:20px">
											   <div class="box-product-wrap" style="padding-bottom:20px">
											   <el-space :size="12" wrap>
											   <div   v-for="item in  dispatchFormEntryList" class="box-product-img" >
													 <img :src="item.image" style="width:40px;height:40px"/>
													 <p class="font-base">x{{item.amount}}</p>
											   </div>
											   </el-space>
											   </div>
										   <div v-if="!warehouseform.hasBox" >
												<div class="confirmBoxNum-wrap">
													 <p class="mar-bot" style="font-weight: bolder;">确认包装箱数量</p> 
													<div style="margin-bottom:16px;margin-top:10px">
														需要&nbsp;
														<el-input-number  style="width: 120px;" :min="1" :max="10000" v-model="warehouseform.boxnum"  @change="handleChange" /> 
														&nbsp;个箱子包装 
													</div>
													<el-button  type="primary" @click.stop="openPackList(warehouseform)">打开装箱表单</el-button>
												</div>
										   </div>
										   <div v-else>
										   		<el-link  @click.stop="openPackList(warehouseform)"  type="primary">
										   			<ad-product class="ic-cen" theme="filled" size="16"/>&nbsp;
										   			<div class="font-base">装箱表单（共{{warehouseform.boxnum}}箱）</div>
										   		</el-link>
										   </div>
									</div>
								 </el-tab-pane>
						   </el-tabs>
							
						  </div>
						 </div>
						 <div>
							 
						 </div>
						</div>
						<div class='text-center mar-top-16' style="margin-bottom:20px">
							<el-button type="" @click="closePage">关闭</el-button>
							<el-button type="primary" v-if="warehouseform.auditstatus==3 || warehouseform.auditstatus==4" @click.stop="showConsDialog">辅料出库</el-button>
							<span style="padding-left:30px;"></span>
							<el-button v-if="warehouseform.auditstatus!=5 && warehouseform.auditstatus!=0"  @click.stop="reSubmitForm(warehouseform.auditstatus,'back')">
								<span v-if="warehouseform.auditstatus==1">驳回</span>
								<span v-if="warehouseform.auditstatus==2||warehouseform.auditstatus==3">取消</span>
								<span v-if="warehouseform.auditstatus==4">撤销</span>
							</el-button>
							
							<el-button v-if="warehouseform.auditstatus==0" type="" @click="gotoeditForm">编辑</el-button>
									
							<el-button v-if="warehouseform.auditstatus<=3" type="primary" @click="reSubmitForm(warehouseform.auditstatus,'submit')">
								<span v-if="warehouseform.auditstatus==0">提交</span>
								<span v-if="warehouseform.auditstatus==1">通过</span>
								<span v-if="warehouseform.auditstatus==2">发货</span>
								<span v-if="warehouseform.auditstatus==3">收货</span>
							</el-button>
						</div>
	 <SplitDialog ref="splitDialogRef" />
	 <Consumable ref="consRef"></Consumable>
	 </div>
	 <BoxTable  ref="boxTableRef" @change="getBoxData"></BoxTable>
	 
	 <el-dialog v-model="visible" class="invheader" title="标签打印" top="8vh" width="80%">
	 	<template #title>
	 		标签打印 
	 	</template>
	        <el-table :data="labellist" >
	 		   <el-table-column label="图片" prop="image" width="70">
	 		   	<template #default="scope">
	 		   		<el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
	 		   	</template>
	 		   </el-table-column>
	 		  <el-table-column label="SKU" prop="sku" show-overflow-tooltip>
	 		          <template #default="scope">
	 		  			<el-input v-model="scope.row.sku" clearable></el-input>
	 		  		</template>
	 		  </el-table-column>
			  <el-table-column label="名称" prop="ename" show-overflow-tooltip>
			          <template #default="scope">
			  			<el-input v-model="scope.row.ename" clearable></el-input>
			  		</template>
			  </el-table-column>
	 		  <el-table-column label="发货数量" prop="amount" show-overflow-tooltip>
	 		          <template #default="scope">
	 		  			<el-input v-model="scope.row.amount" clearable></el-input>
	 		  		</template>
	 		  </el-table-column>
			  <el-table-column label="备注" prop="remark" show-overflow-tooltip>
			          <template #default="scope">
			  			<el-input v-model="scope.row.remark" clearable></el-input>
			  		</template>
			  </el-table-column>
	 		  <el-table-column label="操作" prop="price" show-overflow-tooltip>
	 		          <template #default="scope">
	 		  			<el-button link type="primary"   @click="handleDelete(scope.$index)" >删除</el-button>
	 		  		</template>
	 		  </el-table-column>
	 	   </el-table>
	 	   <template #footer>
	 	   	<span class="dialog-footer">
	 	   		<el-button @click="visible=false">关闭</el-button>
	 	   		<el-button type="primary" @click="submitFormLabel">下载</el-button>
	 	   	</span>
	 	   </template>
	 </el-dialog>
</template>
<script setup>
	import {Plus,Minus,Help,Printer,AddPrint,InboxOut} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router';
	import {redirectToList} from '@/utils/page_helper.js';
	import {dateFormat,dateTimesFormat,formatFloat,getMarketplaceId} from '@/utils/index.js';
	import dispatchApi from '@/api/erp/inventory/dispatchOverseaApi.js';
	import {loadInventory} from "@/hooks/erp/shipment/data_helper";
	import {CopyDocument,Crop,Edit,Warning,ArrowDown} from '@element-plus/icons-vue'
	import SplitDialog from "./split_dialog.vue"
	import logistics from "./logistics.vue";
	import BoxTable from "./box_table.vue";
	import Consumable from '@/views/erp/baseinfo/material/consumableDialog.vue';
	const MaterialRef = ref();
	const splitDialogRef =ref();
	const boxTableRef=ref();
	const router = useRouter();
	const consRef=ref();
	const id = router.currentRoute.value.query.id;
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	const state = reactive({
		warehouseform:{},
		activeName:"调库商品",
		dispatchFormEntryList:[],
		visible:false,
		labellist:[]
	})
	const {
		warehouseform,activeName,labellist,visible,
		dispatchFormEntryList
	}=toRefs(state)
	
	function load(){
		dispatchApi.getData({"id":id}).then((res)=>{
			if(res.data){
				state.warehouseform=res.data.warehouseform;
				state.warehouseform.marketplaceid=getMarketplaceId(state.warehouseform.country);
				state.warehouseform.remark2=state.warehouseform.remark;
				state.dispatchFormEntryList=res.data.dispatchFormEntryList;
				state.dispatchFormEntryList.forEach(item=>{
					item.msku=item.sku;
				})
				state.warehouseform.hasBox=state.warehouseform.boxnum;
				if(state.dispatchFormEntryList&&state.dispatchFormEntryList.length>0){
					loadInventory(state,state.dispatchFormEntryList,state.warehouseform.from_warehouseid);
				}
			}
		});
	}
	function handleCopy(id){
		router.push({
			path:"/e/w/os/s",
			query:{
				title:'海外仓补货单',
				path:"/e/w/os/s",
				copyid:id
			},
		})
	}
	function handlePrint(){
		dispatchApi.downPDFShipForm({id:state.warehouseform.id});
	}
	function splitHandel(){
		state.dispatchFormEntryList.forEach(row=>{
			if(!row.length){ row.length=0; }
			if(!row.width) { row.width=0;  }
			if(!row.height){ row.height=0; }
			row.volume=formatFloat(row.length*row.width*row.height/1000000,4);
			row.dimweight=formatFloat(row.length*row.width*row.weight/5000,4);
		})
		splitDialogRef.value.show(state.dispatchFormEntryList,state.warehouseform);
	}
	function handlerStep(status){
		if(status==5){
			status=-1;
		}
		if(status==4){
			status=5;
		}
		return status;
	}
	function handleUpdateRemark(){
		dispatchApi.updateRemark({"formid":state.warehouseform.id,"remark":state.warehouseform.remark2}).then(res=>{
			state.warehouseform.remark=state.warehouseform.remark2;
			state.warehouseform.revisable=false;
			ElMessage.success( "修改成功");
		})
	}
	function closePage(){
		redirectToList(router,"/erp/warehouse/overseas/stock",'海外仓备货单');
	}
	function reSubmitForm(status,ftype){
		//状态的回滚和提交操作
		var titleStr="";
		var title="";
		if(ftype=="back"){
			if(status==1){
				titleStr="您确认要驳回该调库单吗？";
				title="驳回调库单";
				status=5;
			}else if(status==2){
				titleStr="您确认要取消该调库单吗？";
				title="取消调库单";
				status=5;
			}else if(status==3){
				titleStr="您确认要取消该调库单吗？";
				title="取消调库单";
				status=5;
			}else if(status==4){
				titleStr="您确认要撤销该调库单吗？";
				title="撤销调库单";
				status=5;
			} 
		}
		if(ftype=="submit"){
			if(status==0){
				titleStr="您确认要提交该调库单吗？";
				title="提交调库单";
				status=1;
			}else if(status==1){
				titleStr="您确认要通过该调库单吗？";
				title="通过调库单";
				status=2;
			}else if(status==2){
				titleStr="您确认要发货出库吗？";
				title="发货调库单";
				status=3;
			}else if(status==3){
				titleStr="请确认您已收到所有货物？";
				title="收货调库单";
				status=4;
			} 
		}
		if(ftype=="done"){
			titleStr="您确定要直接完成发货单吗？";
			title="完成调库单";
			status=4;
		}
		 ElMessageBox.confirm(
			titleStr,
			title,
			{
			  confirmButtonText: '确定',
			  cancelButtonText: '取消',
			  type: 'warning',
			}
		  )
			.then(() => {
					dispatchApi.examine({"ids":id,"status":status}).then((res)=>{
						  if(res.data){
							  ElMessage.success(res.data);
							  load();
						  }else{
							  ElMessage.error( "操作失败");
						  }
					  });
			})
			.catch(() => {
			  ElMessage.info('取消操作');
			})
		
	}
	function gotoeditForm(){
			router.push({
				path:"/e/w/t",
				query:{
					title:'编辑调库单',
					path:"/e/w/t",
					id:id,
					fwid:state.warehouseform.from_warehouseid,
					twid:state.warehouseform.to_warehouseid,
				    replace:true
				},
			})
	}
	function showConsDialog(){
		   var data={};
		   var skulist=[];
		   data.warehouseid=state.warehouseform.from_warehouseid;
		   data.number=state.warehouseform.number+"-sea";
		   state.dispatchFormEntryList.forEach(item=>{
			   if(item.sku){
				   var row={};
				   row.qty=item.amount;
				   row.sku=item.sku;
				   skulist.push(row);
			   }
		   });
		   data.skulist=skulist;
		   consRef.value.show(data);
	}
	function handleLabelPrint(){
		dispatchApi.getPrintLabel({"id":state.warehouseform.id}).then(res=>{
			state.visible=true;
			state.labellist=res.data;
		})
	}
	function handleDelete(index){
		state.labellist.splice(index,1)
	}
	function submitFormLabel(){
		var data=[];
			state.labellist.forEach(item=>{
				var dataitem={};
				dataitem.sku=item.sku;
				dataitem.ename=item.ename;
				dataitem.quantity=item.amount;
				dataitem.materialid=item.materialid;
				dataitem.country=item.country;
				dataitem.remark=item.remark;
				data.push(dataitem);
			})		
		dispatchApi.downExcelLabel(data)
	}
	function openPackList(warehouseform){
		if(!(warehouseform.boxnum && warehouseform.boxnum>1)){
			warehouseform.boxnum=1;
		}
		warehouseform.boxnum=parseInt(warehouseform.boxnum);
		state.operatorInfo=warehouseform;
		state.dispatchFormEntryList.forEach(item=>{
			item.quantity=item.amount;
		})
		boxTableRef.value.show({boxnum:warehouseform.boxnum,"items":state.dispatchFormEntryList},warehouseform);
	}
	onMounted(()=>{
		load();
	});
</script>

<style scoped>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 150px);
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
		.mark-re{
			margin-top: 16px;
			margin-bottom:16px;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	.mark-re h5::before{
		content: "";
		display: inline-block;
		height: 13px;
		width: 4px;
		margin-right:8px;
		background-color: #FF6700;
	}
	.fill-from-body{
		margin:16px 24px;
	}
	.m-b-t-24{
		margin-bottom:24px;
		margin-top:24px;
	}
</style>
