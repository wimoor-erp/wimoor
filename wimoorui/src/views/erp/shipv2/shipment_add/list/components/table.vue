<template>
	<el-row>
		<GlobalTable ref="globalTable" :tableData="tableData" :stripe="true" 
		   @selection-change = "selectChange"
		 @loadTable="loadtableData"  height="calc(100vh - 300px)" 
		 :defaultSort="{ prop: 'createtime', order: 'descending' }"  
			style="width: 100%;margin-bottom:16px;">
			<template #field>
			    <el-table-column type="selection" width="38" />
				<el-table-column prop="groupname" label="店铺" width="120" />
				<el-table-column prop="number" label="计划编码" width='180' sortable="custom">
					<template #default="scope">
						<div>{{scope.row.number}}  <span 
					
					 style="padding-left:2px;cursor:pointer;color:#67C23A"> 
					 <el-icon  v-loading="scope.row.refreshloading"   @click="refreshPlan(scope.row)"><Refresh /></el-icon>
					 </span></div>
						<div class="font-extraSmall">{{scope.row.name}}</div>
						</template>
						</el-table-column>
				<el-table-column prop="skunum" label="SKU个数" width="100" sortable="custom" >
				<template #default="scope">
					<div>{{scope.row.skunum}}</div>
					<div v-if="scope.row.auditstatus>1&&scope.row.auditstatus<7&&scope.row.checkInv&&scope.row.checkInv.length>5" class="font-extraSmall text-green">
						 <el-tag  size="small" :title="'已下架，下架ID:'+scope.row.checkInv" type="success"   effect="plain" round >可拣货</el-tag>
					</div>
				</template>
				</el-table-column>
				<el-table-column prop="sumquantity" label="发货数量" width="100" sortable="custom" />
				<el-table-column prop="warename" label="发货仓库" width="170" />
				<el-table-column prop="country" label="收货站点" width="120" >
					<template #default="scope">
					     <span >{{scope.row.countryname}}</span>
						 <div class="font-extraSmall"> {{scope.row.country}}</div>
					 </template>
				</el-table-column>
				<el-table-column prop="createtime" label="申请日期" width="175"    sortable="custom">
					<template #default="scope">
					     <span >{{dateFormat(scope.row.createtime)}}</span>
						 <div class="font-extraSmall">运输方式:{{scope.row.transtypename}}</div>
					 </template>
					 </el-table-column>
				<el-table-column prop="auditime" label="申请日期" width="175"    sortable="custom">
				 <template #header>
					 <span>审核日期</span>
					 <el-button link size="small" type="info"  @click.stop="handleDateQuery">超期</el-button>
				  </template>
				 <template #default="scope">
				      <span >{{dateFormat(scope.row.auditime)}}</span>
					        <p v-if="scope.row.shippingDate" class="font-extraSmall">
								   发货日期:  {{dateFormat(scope.row.shippingDate)}}
							</p>			  
									  <el-tooltip   placement="top"  >
									  		  <template #content>
									  		  <div >运输方式:{{scope.row.transtypename}},{{scope.row.transday}}天</div>
											    <div >审核日期:{{dateFormat(scope.row.auditime)}}</div>
												<div >发货日期:{{dateFormat(scope.row.shippingDate)}}</div>
									  		  </template>
									  		 <p style="font-size: 12px;" v-if="scope.row.daytype=='red'" class="text-red" >{{handleTransNoShip(scope.row)}}</p>
											 <p style="font-size: 12px;" v-else   >{{handleTransNoShip(scope.row)}}</p>
									  </el-tooltip>
									 
									  
				   </template>
				</el-table-column>
				<el-table-column v-if="auditstatus==null" prop="auditstatus" label="状态" width="130"   >
					<template #default="scope">
						 <el-tag v-if="scope.row.auditstatus==1" type="warning">待审核</el-tag>
						 <el-tag v-if="scope.row.auditstatus==2" type="warning">仓库配货</el-tag>
						 <el-tag v-if="scope.row.auditstatus==3" type="warning">计划装箱</el-tag>
						 <el-tag v-if="scope.row.auditstatus==4" type="warning">配送方案确认</el-tag>
						 <el-tag v-if="scope.row.auditstatus==5"  >货件装箱</el-tag>
						 <el-tag v-if="scope.row.auditstatus==6"  >物流确认</el-tag>
						 <el-tag v-if="scope.row.auditstatus==7" >发货出库</el-tag>
						 <el-tag v-if="scope.row.auditstatus==8" type="success">货件跟踪</el-tag>
						 <el-tag v-if="scope.row.auditstatus==12" type="danger">已取消</el-tag>
						 <el-tag v-if="scope.row.auditstatus==11" type="danger">已驳回</el-tag>
						 <div class="font-extraSmall" v-if="scope.row.invtype==2">
							 手动同步<span v-if="scope.row.invstatus==0">(未扣库存)</span>
						 </div>
					</template>
				</el-table-column>
				<el-table-column prop="remark" label="备注" />
				<el-table-column prop="remark" label="操作" width="130"  fixed="right" >
					<template #default="scope">
						<el-space>
							<el-button v-if="scope.row.auditstatus==2" class='el-button--blue' @click="gotoNewInbound(scope.row)">下架配货</el-button>
							<el-button v-else-if="scope.row.auditstatus==3" class='el-button--blue' @click="gotoBox(scope.row)">提交装箱</el-button>
							<el-button v-else-if="scope.row.auditstatus==4" class='el-button--blue' @click="gotoPlacement(scope.row)">配送方案</el-button>
							<el-button v-else-if="scope.row.auditstatus==5" class='el-button--blue' @click="gotoShip(scope.row)">打标出库</el-button>
							<el-button v-else-if="scope.row.auditstatus==6" class='el-button--blue' @click="gotoShip(scope.row)">物流确认</el-button>
							<el-button v-else-if="scope.row.auditstatus==7" class='el-button--blue' @click="gotoShip(scope.row)">发货出库</el-button>
							<el-button v-else-if="scope.row.auditstatus==8" class='el-button--blue' @click="gotoOldInbound(scope.row)">跟踪发货</el-button>
							<el-button v-else-if="scope.row.auditstatus>8" class='el-button--blue' @click="gotoNewInbound(scope.row)">跟踪发货</el-button>
							<el-button v-if="scope.row.auditstatus==1" class='el-button--blue' @click="shipmentDetails(scope.row)">审核</el-button>
							<el-button v-else class='el-button--blue' @click="shipmentDetails(scope.row)">详情</el-button>
						</el-space>
					</template>
				</el-table-column>
			</template>
		</GlobalTable>
	</el-row>
</template>

<script setup>
	import {
		ref,
		reactive,
		onMounted
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import {dateFormat,dateTimesFormat,} from '@/utils/index.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
  	import {ElMessage } from 'element-plus';
  import {Refresh,} from '@element-plus/icons-vue'
		const emit = defineEmits(["change"]);
			let globalTable = ref(GlobalTable);
			let router = useRouter()
			let parmashead = ref({})
			let tableData = reactive({
				records: [],
				total: 0
			});
			let auditstatus = ref("")
		function selectChange(rows){
				emit("change",rows);
		}
	   function handleDateQuery(){
			    globalTable.value.tableSort({"prop":"days","order":"desc"});
		   }
			function getshipplanData(params) {
				parmashead.value = params;
				globalTable.value.loadTable();
			}
			function gotoPlacement(row){
				router.push({
					path:'/e/s/s/placement',
					query:{
					  id:row.id,
					  title:"发货处理_配置",
					  path:'/e/s/s/placement',
					}
				})
			}
			function gotoShip(row){
				router.push({
					path:'/e/s/s/three',
					query:{
					  id:row.id,
					  title:"发货处理_发货",
					  path:'/e/s/s/three',
					}
				})
			}
			
			function gotoBox(row){
				router.push({
					path:'/e/s/s/two',
					query:{
					  id:row.id,
					  title:"发货处理_装箱",
					  path:'/e/s/s/two',
					}
				})
			}
			
			function gotoNewInbound(row){
				router.push({
					path:'/e/s/s/one',
					query:{
					  id:row.id,
					  title:"发货处理_配货",
					  path:'/e/s/s/one',
					}
				})
			}
		
			function gotoOldInbound(row){
				router.push({
					path:'/e/s/s/end',
					query:{
					  id:row.id,
					  title:"发货处理_结束",
					  path:'/e/s/s/end',
					}
				})	
			}
			function shipmentDetails(row) {
				router.push({
					path: '/newshipmentdetails',
					query: {
						id: row.id,
						title: "新货件详情",
						path: '/newshipmentdetails',
					}
				})
			}
			function getDaysBetweenDates(date2) {
			    // 将日期字符串转换为Date对象
				if(date2){
					var d1 = new Date();
					var d2 = new Date(date2);
					// 计算两个日期的时间差（毫秒）
					var timeDiff = Math.abs(d2.getTime() - d1.getTime());
					// 计算天数
					var days = Math.ceil(timeDiff / (1000 * 3600 * 24));
					return days;
				}else{
					return 0;
				}
				
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
			var tagtypes=["primary","success","info","warning","danger"];
			function loadtableData(params) {
				params.groupid = parmashead.value.groupid;
				params.marketplaceid = parmashead.value.marketplaceid;
				params.warehouseid = parmashead.value.warehouseid;
				if (parmashead.value.start !== undefined) {
					params.fromDate = (parmashead.value.start);
					params.toDate = (parmashead.value.end + " 23:59:59");
				} else {
					const end = new Date()
					const start = new Date()
					start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
					params.fromDate = dateFormat(start);
					params.toDate = dateFormat(end) + " 23:59:59";
				}
				params.auditstatus = parmashead.value.auditstatus;
				if (parmashead.value.seachtype !== undefined) {
					params.searchtype = parmashead.value.seachtype;
				} else {
					params.searchtype = "sku";
				}
				auditstatus.value=params.auditstatus;
				params.search = parmashead.value.searchwords;
				shipmentplanApi.getPlanList(params).then((res) => {
					tableData.records = res.data.records
					tableData.total = res.data.total
				})
			}
	function refreshPlan(row){
		row.refreshloading=true;
		var data={"inboundPlanId":row.planid,
		          "amazonauthid":row.amazonauthid}
		shipmentplanApi.refreshPlan(data).then(res=>{
			row.refreshloading=false;
			ElMessage.success("操作成功!");
			globalTable.value.loadTable();
		})
	}
    defineExpose({ getshipplanData })
		 
	 
</script>

<style>
</style>
