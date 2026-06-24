<template>
	<el-row>
	  <GlobalTable ref="globalTable" 
	  :tableData="tableData"  
	  @loadTable="loadtableData" 
	  :defaultSort="{ prop: 'createdate', order: 'descending' }" 
	  :stripe="true" 
	   @selection-change = "selectChange"
	   height="calc(100vh - 300px)" 
	   style="width: 100%;margin-bottom:16px;">
	    <template #field>
		   <el-table-column type="selection" width="38" />
		   <el-table-column prop="orderNum" label="货件编码/货件名称" width='180'>
		      <template #default="scope">
			     <span class='sku' style="padding-right:3px;" @click="toFormApprove(scope.row)">{{scope.row.shipmentid}}</span>
				 <el-tooltip v-if="scope.row.referenceid"  >
					  <template #content>
					  <div >内部编码(Reference ID):{{scope.row.referenceid}}</div>
					  </template>
					 <el-icon @click="showReferenceidDailog(scope.row)" :size="15"class="pointer font-extraSmall text-green" style="vertical-align: middle;"><HelpFilled></HelpFilled></el-icon>
				 </el-tooltip>
				 
				 <el-tooltip v-else content="请填写内部编码:referenceid">
				 					 <el-icon @click="showReferenceidDailog(scope.row)" :size="15"class="pointer font-extraSmall text-gray" style="vertical-align: middle;"><HelpFilled></HelpFilled></el-icon>
				 </el-tooltip>
				  
				 <el-tooltip v-if="scope.row.tracknumber"  >
					 <template #content>
					       <div >跟踪编码:{{scope.row.tracknumber}}</div>
						   <div v-if="scope.row.traceupstatus==0">物流跟踪编码待处理</div>
						   <div v-if="scope.row.traceupstatus==1">物流跟踪编码已处理</div>
						   <div v-if="scope.row.traceupstatus==2">物流跟踪编码处理失败</div>
					  </template>
				 	<el-icon v-if="scope.row.traceupstatus==0"   :size="15"
					             class="pointer font-extraSmall text-blue" 
					             style="vertical-align: middle;margin-left:5px;"><Van></Van></el-icon>
					<el-icon v-if="scope.row.traceupstatus==1"   :size="15"
								 class="pointer font-extraSmall text-orange" 
								 style="vertical-align: middle;margin-left:5px;">
								 <Van></Van></el-icon>
					<el-icon v-if="scope.row.traceupstatus==2"   :size="15"
													 class="pointer font-extraSmall text-red" 
													 style="vertical-align: middle;margin-left:5px;">
													 <Van></Van></el-icon>
					<el-icon v-else title="物流跟踪编码待处理"  :size="15"
						 class="pointer font-extraSmall text-orange" 
						 style="vertical-align: middle;margin-left:5px;">
						 <Van></Van></el-icon>
				 </el-tooltip>
				 <el-tooltip v-else content="请填写追踪编码">
				 	 <el-icon  @click="shipmentfollow(scope.row)"  :size="15"class="pointer font-extraSmall text-gray" style="vertical-align: middle;margin-left:5px;"><Van></Van></el-icon>
				 </el-tooltip>
				  
				  <p>{{scope.row.name}}</p>
			  </template>
		    </el-table-column>
	    <el-table-column prop="plan" label="店铺/发货仓库" width='180'>
		<template #default="scope">
		     <span >{{scope.row.groupname}}</span>
			  <p class="text-gray" v-if="scope.row.syncinv!=1">{{scope.row.warehouse}}</p>
			  <div style="margin-left:-5px" v-else>
				  <el-button key="primary" @click="showSyncDailog(scope.row)" type="primary" link >本地出库 <span class="font-extraSmall">（同步货件）</span></el-button>
				 
			  </div>
		  </template>
		</el-table-column>
		 <el-table-column prop="address" label="配送中心" width="170" >
			 <template #default="scope">
		 
					  <div class="pointer" style="padding-top:5px" @click="handleCountryChange(scope.row.marketplaceid)">{{scope.row.country}}
							 <span v-if="scope.row.arrivalTime">
							 <span class="text-gray pointer" @click="handleCenterChange(scope.row.centerId)" >
							 {{scope.row.centerId}}({{scope.row.countryCode}})  
							 </span>
							 <el-tooltip  content="待出库货件在此配送中心个数">
								 <el-tag size="small"	round  effect="dark" type="success"  v-if="(scope.row.status<=5&&scope.row.status>=2)&&scope.row.centerNumber&&scope.row.centerNumber>'1'"
								    >{{scope.row.centerNumber}}</el-tag>
							 </el-tooltip>
						    </span>
					  </div>
			 
					 <p class="font-extraSmall" v-if="scope.row.arrivalTime">到货日期:
					 		 						 <span>{{dateFormat(scope.row.arrivalTime)}} </span>
					 		 					  <span v-if="scope.row.delayDays>0 && scope.row.status==5" style="padding-left:2px;">
					 		 					  <el-tooltip  placement="top">
					 		 					  					   <template #content>
					 		 					  					         <div >超期：{{scope.row.delayDays}} 天</div>
					 		 					  					    </template>
					 		 					      <el-icon  color="#F56C6C"><WarningFilled /></el-icon>
					 		 					  </el-tooltip>
					 		 					  </span>
					 </p>
					 <div v-else>
					  <span class="text-gray pointer" @click="handleCenterChange(scope.row.centerId)" >
					  {{scope.row.centerId}}({{scope.row.countryCode}})  
					  </span>
					  <el-tooltip  content="待出库货件在此配送中心个数">
					 	 <el-tag size="small"	round  effect="dark" type="success"  v-if="(scope.row.status<=5&&scope.row.status>=2)&&scope.row.centerNumber&&scope.row.centerNumber>'1'"
					 	    >{{scope.row.centerNumber}}</el-tag>
					  </el-tooltip>
					 </div>
			   </template>
		 </el-table-column>
		 <el-table-column prop="createdate" label="创建日期" width="175"    sortable="custom">
			 <template #header>
				 <span>创建日期</span>
				 <el-button link size="small" type="info"  @click.stop="handleDateQuery">超期</el-button>
			  </template>
		  <template #default="scope">
		       <span >{{dateTimesFormat(scope.row.createdate)}}</span>
		 	        <p v-if="scope.row.status5date" class="font-extraSmall">
		 				   发货日期:  {{dateFormat(scope.row.status5date)}}
		 			</p>			  
					  <el-tooltip   placement="top"  >
					  		  <template #content>
					  		  <div >运输方式:{{scope.row.transtypename}},{{scope.row.transday}}天。</div>
					  		  </template>
					  		 <p style="font-size: 12px;" v-if="scope.row.daytype=='red'" class="text-red" >{{handleTransNoShip(scope.row)}}</p>
							 <p style="font-size: 12px;" v-else   >{{handleTransNoShip(scope.row)}}</p>
					  </el-tooltip>
					 
					  
		    </template>
		 </el-table-column> 
		 
		<!--  <el-table-column prop="createdate" label="创建日期" width="175"  sortable="custom">
		  <template #default="scope">
		       <span >{{dateTimesFormat(scope.row.createdate)}}</span>
			  <p class="font-extraSmall" v-if="scope.row.arrivalTime">到货日期:
					  <el-tooltip  placement="top">
						   <template #content>
								 <div >发货日期:{{dateFormat(scope.row.status5date)}}
								 </div>
							</template>
						 <span>{{dateFormat(scope.row.arrivalTime)}} </span>
					  </el-tooltip>
					  <span v-if="scope.row.delayDays>0&&scope.row.status==5" style="padding-left:2px;">
					  <el-tooltip  placement="top">
					  					   <template #content>
					  					         <div >超期：{{scope.row.delayDays}} 天</div>
					  					    </template>
					      <el-icon  color="#F56C6C"><WarningFilled /></el-icon>
					  </el-tooltip>
					  </span>
			  </p>
		    </template>
		 </el-table-column> -->
	    <el-table-column prop="skuamount" label="SKU个数" width="100"    sortable="custom">
					<template #default="scope">  
					 <div>{{scope.row.skuamount}} </div>
					 <div v-if="scope.row.areCasesRequired"><el-tag size="small">原装</el-tag></div>
					 </template>
		</el-table-column>
		 <el-table-column prop="sumQuantity" label="实际配货数量" width="125"  sortable="custom">
			<template #default="scope">
			     <span >{{scope.row.sumshipped}}</span>
				  <p class="font-extraSmall">拟发货数量:{{scope.row.sumQuantity}}</p>
			  </template> 
		 </el-table-column> 
		  <el-table-column  label="发货渠道" width="270">
			   <template #default="scope">
			      <span >{{scope.row.company}}</span>
			 	  <p class="font-extraSmall">{{scope.row.channame}}
				  <el-button v-if="scope.row.apiSystem" 
				             @click="showTransInfoDailog(scope.row)" 
							 key="primary" 
							 type="primary" 
							 link >查看物流</el-button>
				  </p>
				  
			   </template> 
		   </el-table-column> 
		   <el-table-column prop="satus" label="状态" width="110" align="center"  >
			   <template #default="scope">
				    <el-tooltip  placement="top">
					   <template #content>
					         <div v-if="scope.row.status0date">取消日期：{{dateTimesFormat(scope.row.status0date)}}</div>
							 <div v-if="scope.row.status1date">创建日期：{{dateTimesFormat(scope.row.status1date)}}</div>
							 <div v-if="scope.row.status2date">审核日期：{{dateTimesFormat(scope.row.status2date)}}</div>
							 <div v-if="scope.row.status3date">配货日期：{{dateTimesFormat(scope.row.status3date)}}</div>
							 <div v-if="scope.row.status4date">装箱日期：{{dateTimesFormat(scope.row.status4date)}}</div>
							 <div v-if="scope.row.shipedDate"> 出库日期：{{dateTimesFormat(scope.row.shipedDate)}}</div>
							 <div v-if="scope.row.status5date">物流发货：{{dateTimesFormat(scope.row.status5date)}}</div>
							 <div v-if="scope.row.receivedate">接收日期：{{dateTimesFormat(scope.row.receivedate)}}</div>
							 <div v-if="scope.row.status6date">结束日期：{{dateTimesFormat(scope.row.status6date)}}</div>
					    </template>
			            <el-tag :type="tranStatusType(scope.row)" v-if="scope.row.status">{{tranStatus(scope.row)}}</el-tag>
				    </el-tooltip>
				    <span  
					 @click="refreshShipmentRec(scope.row.shipmentid)"
					 style="padding-left:2px;cursor:pointer;color:#67C23A"> 
					 <el-icon><Refresh /></el-icon>
					 </span>
					 <span v-if="(scope.row.sumrec!=scope.row.sumshipped
					             &&scope.row.status==6
								 &&!scope.row.ignorerec)||(scope.row.shipmentstatus=='状态异常')"
					 @click="refreshShipmentDialog(scope.row.shipmentid)"
					 style="padding-left:2px;cursor:pointer;color:#F56C6C"> 
					<el-icon><CircleClose /></el-icon>
					</span>
				    <div v-if="scope.row.status>=5"><span class="font-extraSmall"> 已收货:</span>
				   			 <span class="font-extraSmall" v-if="scope.row.sumrec==scope.row.sumshipped||scope.row.ignorerec">{{scope.row.sumrec}}</span>
				   			 <span style="color:#F56C6C" v-else>{{scope.row.sumrec}}</span>
				   	</div>
					<div v-if="scope.row.status>0&&scope.row.status<5&&scope.row.checkinv&&scope.row.checkinv.length>5" class="font-extraSmall text-green">
						 <el-button :type="scope.row.tagtype" :title="'已下架，下架ID:'+scope.row.checkinv" @click="handleSearchCheckInv(scope.row)" link>可拣货</el-button>
					</div>
			   </template> 
			</el-table-column>    
		   <el-table-column prop="remark" label="备注"  />
	    <el-table-column prop="operate" label="操作"  width="120"  fixed="right" >
	        <template #default="scope">
	          <el-space>
	            <el-button type="primary"  @click="shipmentfollow(scope.row)">跟踪发货</el-button>
	          </el-space>
	        </template>
	    </el-table-column>
		</template>
	  </GlobalTable>
	  
	</el-row>
	<el-dialog v-model="dialogVisible" title="确认信息" width="50%" center >
	     <div style="width:100%;text-align:center">请选择忽略此异常或是重新同步</div>
	  <template #footer>
	    <span class="dialog-footer">
	      <el-button @click="ignoreShipmentWarn()">忽略异常</el-button>
	      <el-button type="primary" @click="refreshShipmentRec()" >重新同步</el-button >
	    </span>
	  </template>
	</el-dialog>
	<el-dialog v-model="referenceidVisible" :title="editRow.shipmentid+' 提交后台内部编码(reference ID)'" width="50%" center >
	    <el-input v-model="editRow.referenceidEdit"></el-input>
	  <template #footer>
	    <span class="dialog-footer">
	      <el-button @click="referenceidVisible=false">关闭</el-button>
	      <el-button type="primary" @click="submitShipmentReferenceid()" >提交</el-button >
	    </span>
	  </template>
	</el-dialog>
	<TransInfoDailog ref="transInfoDailog"></TransInfoDailog>
	<AsyncDailog ref="asyncDailog" @change="loadTable"></AsyncDailog>
	
</template>

<script>
	import { ref,reactive,onMounted ,inject} from 'vue'
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import shipmentplanApi from '@/api/erp/ship/shipmentplanApi.js';
	import shipmentApi from '@/api/amazon/inbound/shipmentApi.js';
	import {ElMessage } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router'
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import {Refresh,CircleClose,WarningFilled,Ship,HelpFilled,Van} from '@element-plus/icons-vue'
	import TransInfoDailog from "./transinfo.vue"
	import AsyncDailog from './asyncdailog.vue'
	import {tranStatus,tranStatusType} from "@/hooks/erp/shipment/shipment_status.js"
	export default{
		name:'Table',
		components:{
			GlobalTable,Refresh,ElMessage,CircleClose,TransInfoDailog,WarningFilled,AsyncDailog,Ship,HelpFilled,Van
		},
		emits:["change"],
		setup(props,context){
			const emitter = inject("emitter");
			let router = useRouter()
			let tableData=reactive({records:[],total:0})
			let orderStatus =ref("")
			let parmashead = ref({})
			let editRow=ref({});
			let dialogVisible=ref(false);
			let referenceidVisible=ref(false);
			let transInfoDailog=ref(TransInfoDailog);
			let asyncDailog=ref(AsyncDailog);
			let globalTable=ref(GlobalTable);
			let optShipmentid="";
			function refreshShipmentDialog(shipmentid){
				optShipmentid=shipmentid;
				dialogVisible.value=true;
			}
			function handleSearchCheckInv(row){
				  parmashead.value.checkinv=row.checkinv;
				  loadTable()
			}
			function showSyncDailog(row){
				let params={};
				params.shipmentid=row.shipmentid;
				params.groupid=row.groupid;
				params.marketplaceid=row.marketplaceid;
				asyncDailog.value.showDailog(params);
			}
			function ignoreShipmentWarn(){
				shipmentApi.ignoreShipment({'shipmentid':optShipmentid}).then(res=>{
					 ElMessage.success('忽略成功！');
					 if(dialogVisible.value){
						 dialogVisible.value=false;
					 }
					 globalTable.value.loadTable();
				})
			}
			var oldcheckinv={};
			
			function getshipmentData(params){
				parmashead.value=params;
				parmashead.value.checkinv="";
				oldcheckinv={};
				globalTable.value.loadTable();
			}
			function showTransInfoDailog(row){ 
				transInfoDailog.value.loadTransDetialInfo(row.companyid,row.shipmentid,row.ordernum);
			}
			function loadTable(){
				globalTable.value.loadTable();
			}
			function handleCenterChange(centerid){
				parmashead.value.fbacenter=centerid;
				loadTable();
			}
			function handleCountryChange(marketplaceid){
				parmashead.value.country=marketplaceid;
				loadTable();
			}
			function loadtableData(params){
				params.groupid = parmashead.value.store;
				params.marketplaceid =parmashead.value.country;
				params.warehouseid =parmashead.value.warehouse;
				params.fbacenter=parmashead.value.fbacenter;
				if(parmashead.value.start!==undefined){
					params.fromdate = parmashead.value.start;
					params.enddate =parmashead.value.end;
				}else{
					const end = new Date()
					const start = new Date()
					start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
					params.fromdate =dateFormat(start);
					params.enddate =dateFormat(end);
				}
				params.auditstatus = parmashead.value.orderStatus;
				if(parmashead.value.seachtype!==undefined){
					params.searchtype =parmashead.value.seachtype;
				}else{
					params.searchtype ="sku";
				}
				params.search = parmashead.value.searchwords;
				params.company =parmashead.value.company;
				params.channel= parmashead.value.channel;
				params.transtype =parmashead.value.transtype;
				params.checkdown=parmashead.value.checkdown;
				params.checkinv=parmashead.value.checkinv;
				params.areCasesRequired=parmashead.value.areCasesRequired;
				params.hasexceptionnum=parmashead.value.hasexceptionnum;
				params.hasreferenceid=parmashead.value.hasreferenceid;
				var tagtypes=["primary","success","info","warning","danger"];
				shipmenthandlingApi.getshipList(params).then((res)=>{
					var indexv=0;
					res.data.records.forEach(itemv=>{
						if(oldcheckinv[itemv.checkinv]==undefined){
							indexv=(indexv+1)%5;
							itemv.tagtype=tagtypes[indexv];
							oldcheckinv[itemv.checkinv]=indexv;
						}else{
							itemv.tagtype=tagtypes[oldcheckinv[itemv.checkinv]];
						}
					});
					tableData.records = res.data.records;
					tableData.total =res.data.total;
				})
			}
			function submitShipmentReferenceid(){
				shipmentplanApi.updateShipmentReferenceid({"shipmentid":editRow.value.shipmentid,"referenceid":editRow.value.referenceidEdit}).then((res)=>{
					 if(res.data && res.data=="ok"){
						ElMessage.success( '修改成功！');
						editRow.value.referenceid=editRow.value.referenceidEdit;
						referenceidVisible.value=false;
					 }else{
						 ElMessage.error('修改失败！');
					 }
				});
			}
			function showReferenceidDailog(row){
				referenceidVisible.value=true;
				editRow.value=row;
				editRow.value.referenceidEdit=editRow.value.referenceid;
			}
	        function refreshShipmentRec(shipmentid){
				if(!shipmentid&&optShipmentid){
					shipmentid=optShipmentid;
				}
				shipmentApi.refreshShipmentRec({'shipmentid':shipmentid}).then(res=>{
					ElMessage.success('同步成功！');
					 if(dialogVisible.value){
						 dialogVisible.value=false;
					 }
					 globalTable.value.loadTable();
				})
			}
			//跟踪货件
			function shipmentfollow(row){
				emitter.emit("removeCache", "发货流程"+row.shipmentid);
				router.push({
					path:'/shipment_handing/shipstep',
					query:{
					  shipmentid:row.shipmentid,
					  title:"发货流程"+row.shipmentid,
					  path:'/shipment_handing/shipstep',
					}
				})
			}
			function toFormApprove(row){
				router.push({
					path: '/shipmentdetails',
					query: {
						id: row.planid,
						title: "货件详情",
						path: '/shipmentdetails',
					}
				})
			}
		   function selectChange(rows){
			   context.emit("change",rows);
		   }
		   
		   function handleTransNoShip(row){
			   if(row.transtypename){
			   	// 红单/空运，2个工作日
			   	// 卡航  6个工作日
			   	// 美森限时达/美森正班船   7个工作日
			   	// 普船/铁路  8个工作日
			   	var transday=8;
			   	if(row.transday){
			   		transday=row.transday;
			   	}
			   	if(row.days>0){
					row.daytype='red';
			   		return "发货超期"+row.days+"天";
			   	}else {
			   		//row.overday=transday-times;
			   		return "发货剩余"+(row.days*-1)+"天";
			   	} 
			   }else{
			   	return "";
			   }
		   }
		   
		   function handleTransType(row){
		   	if(row.transtypename){
		   		var transday=8;
		   		if(row.transday){
		   			transday=row.transday;
		   		}
		   		if(row.days>=0){
		   			return "超期";
		   		}else{
		   			return "正常";
		   		}
		   	}else{
		   		return "";
		   	}
		   }
		   
		   function handleDateQuery(){
			    globalTable.value.tableSort({"prop":"days","order":"desc"});
		   }
			return{
				tableData,shipmentfollow,getshipmentData,dateFormat,globalTable,
				tranStatus,orderStatus,parmashead,loadtableData,
				tranStatusType,refreshShipmentRec,dialogVisible,refreshShipmentDialog,
				ignoreShipmentWarn,showTransInfoDailog,transInfoDailog,toFormApprove,
				showSyncDailog,asyncDailog,loadTable,dateTimesFormat,selectChange,referenceidVisible,
				handleSearchCheckInv,handleCenterChange,handleCountryChange,showReferenceidDailog,editRow,
				submitShipmentReferenceid,handleTransType,handleTransNoShip,handleDateQuery,
			}
		}
	}
</script>

<style>
	.centerIditem {
	   margin-top: 6px;
	   margin-right: 3px;
	   
	}
	.sku{
		cursor:pointer;
	}
	.text-gray{
		color:#999;
	}
</style>
