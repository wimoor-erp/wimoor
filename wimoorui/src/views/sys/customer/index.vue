<template>
	<div class="main-sty con-header"> 
		<el-row style="text-align: center;">
		<el-col :span="2"></el-col>
		<el-col style="padding: 10px;" :span="3" v-for="item in pkglist">
			
			<div  class="font-extraLarge">  <span class="pointer" @click.stop="handlePkgQuery(item.id)">{{item.qty}}</span> </div>
			<div class="text-orange">
			   <span class="pointer  " @click.stop="handlePkgQuery(item.id)">{{item.NAME}}</span>
			 </div>
		</el-col>
	  </el-row>
	  <el-row>
	   <el-space>
	  	 <el-select v-model="queryParams.pkgid" @change="handleQuery" placeholder="选择套餐" clearable>
	  	 	<el-option v-for="item in pkglistAll" :label="item.name" :value="item.id" :key="item.id"></el-option>
	  	 </el-select>
		 <el-select v-model="queryParams.roleid" @change="handleQuery" placeholder="选择角色" clearable>
		 	<el-option v-for="item in rolelist" :label="item.name" :value="item.id" :key="item.id"></el-option>
		 </el-select>
	  	 <el-input class='ic-btn' v-model="queryParams.name" @input="handleQuery" placeholder="搜索公司名称,账号,手机号,邮箱,备注" clearable></el-input>
	  	 <el-button type="primary" @click="handleQuery">查询</el-button>
	    </el-space>
	    </el-row>
	  <GlobalTable
	   :tableData="tableData"
	   height="calc(100vh - 220px)"  @loadTable="loadTableData" 
	   :defaultSort="{ prop: 'createDate', order: 'descending' }"
	   ref="globalTable"
	   >
	   <template #field>
			<el-table-column label="客户信息"         prop="userName"  sortable="custom">
				<template #default="scope">
					<div style="font-weight: bolder;">{{scope.row.userName}}</div>
					<div>{{scope.row.account}}</div>
				</template> 
			</el-table-column>
			<el-table-column label="公司名称"     prop="shopName"  sortable="custom">
				<template #default="scope">
					<div>{{scope.row.shopName}}</div>
					<div class="text-red">活跃度:{{scope.row.logincount}}</div>
				</template> 
			</el-table-column>
			<el-table-column label="联系信息"     prop="telephone"  sortable="custom">
				<template #default="scope">
					<div><span class="font-extraSmall">Tel:</span>{{scope.row.telephone}}</div>
					<div><span class="font-extraSmall">Email:</span>{{scope.row.email}}</div>
				</template> 
			</el-table-column>
			<el-table-column label="注册时间"     prop="createDate"  sortable="custom">
				<template #default="scope">
					<div>{{dateTimesFormat(scope.row.createDate)}}</div>
					 
				</template> 
			</el-table-column>
			<el-table-column label="最后登录时间"     prop="lastlogintime"  sortable="custom">
				<template #default="scope">
					<div>{{scope.row.lastlogintime}}</div>
				</template> 
			</el-table-column>
			<el-table-column label="套餐类型"     prop="tariffpackage"  sortable="custom">
				<template #default="scope">
					 <el-tag v-if="scope.row.tariffpackage=='标准版'">{{scope.row.tariffpackage}}</el-tag>
					 <el-tag v-if="scope.row.tariffpackage=='免费版'" type="info">{{scope.row.tariffpackage}}</el-tag>
					 <el-tag v-if="scope.row.tariffpackage=='专业版'" type="warning">{{scope.row.tariffpackage}}</el-tag>
					 <el-tag v-if="scope.row.tariffpackage=='入门版'" type="success">{{scope.row.tariffpackage}}</el-tag>
					 <el-tag v-if="scope.row.tariffpackage=='旗舰版'" type="danger">{{scope.row.tariffpackage}}</el-tag>
					 <div class="font-extraSmall pointer" @click="showPkgDialog(scope.row.shopid)">套餐使用详情</div>
				</template> 
			</el-table-column>
			<el-table-column label="套餐备注"     prop="remark"  sortable="custom">
				<template #default="scope">
					<div>{{scope.row.remark}}</div>
				</template> 
			</el-table-column>
			<el-table-column label="操作"   >
				<template #default="scope">
					<!-- <div>
					<el-button link type="primary">编辑</el-button>
					<el-button link type="primary">删除</el-button>
					<el-button link type="primary">暂停</el-button>
					</div> -->
					<div>
					<el-button link type="primary" @click="showTaskDialog(scope.row.shopid)">报表申请</el-button>
					<!-- <el-button link type="primary">自定义套餐</el-button> -->
					</div>
				</template> 
			</el-table-column>
	  	 </template>
	  </GlobalTable> 
	</div>
	
	<el-dialog
	title="套餐详情"
	v-model="visible"
	width="70%"
	>
	 <el-table :data="pkgTable">
		 <el-table-column prop="" label="已/可绑定店铺数量">
			 <template #default="scope">
			 	 {{scope.row.existShopCount}}/{{handleQty(scope.row.maxShopCount)}}
			 </template>
		 </el-table-column>
		 <el-table-column prop="" label="已/可绑定站点数量">
		 			 <template #default="scope">
		 			 	 {{scope.row.existMarketCount}}/{{handleQty(scope.row.maxMarketCount)}}
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="" label="已/可创建子用户数量">
		 			 <template #default="scope">
		 			 	 {{scope.row.existMember}}/{{handleQty(scope.row.maxMember)}}
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="" label="已/可创建商品数量">
		 			 <template #default="scope">
		 			 	 {{scope.row.existProductCount}}/{{handleQty(scope.row.maxProductCount)}}
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="" label="已/可处理订单数量">
		 			 <template #default="scope">
		 			 	 {{scope.row.existOrderCount}}/{{handleQty(scope.row.maxOrderCount)}}
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="" label="已/可开启广告组数量">
		 			 <template #default="scope">
		 			 	 {{scope.row.existdayOpenAdvCount}}/{{handleQty(scope.row.maxdayOpenAdvCount)}}
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="" label="已/可创建利润方案数量">
		 			 <template #default="scope">
		 			 	 {{scope.row.existProfitPlanCount}}/{{handleQty(scope.row.maxProfitPlanCount)}}
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="" label="失效时间">
		 			 <template #default="scope">
		 			 	 {{dateFormat(scope.row.losingEffect)}}
		 			 </template>
		 </el-table-column>
	 </el-table>
	<template #footer>
		<el-button @click="visible=false">关闭</el-button>
	</template>
	</el-dialog>
	
	<el-dialog
	title="客户申请报表"
	v-model="rptvisible"
	width="50%"
	>
	 <el-space>
		 <el-select v-model="reportType" placeholder="请选择报表类型">
			 <el-option v-for="item in reportTypeList" :key="item.code" :label="item.cname" :value="item.code">{{item.cname}}({{item.code}})</el-option>
		 </el-select>
		 <el-select v-model="groupid" placeholder="请选择店铺">
		 	 <el-option v-for="item in groupList" :key="item.id" :value="item.id" :label="item.name"></el-option>
		 </el-select>
		 <Datepicker ref="datepickers" @changedate="changedate" />
	 </el-space>
	<template #footer>
		<el-button @click="rptvisible=false">关闭</el-button>
		<el-button @click="submitForm" type="primary">提交</el-button>
	</template>
	</el-dialog>
</template>
<script>
    export default{ name:"客户管理" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
    import '@/assets/css/packbox_table.css';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import { listRole} from "@/api/sys/admin/roles"
	import limitApi from '@/api/sys/admin/limitApi.js';
	const globalTable=ref();
	const ownerRef=ref();
	const accDialogRef=ref();
	const calcDialogRef=ref();
	const  state=reactive({
		isload:true,
		tableData: {records:[],total:0}  ,
		queryParams:{name:'',},
		pkglist:[],
		pkglistAll:[],
		rolelist:[],
		visible:false,
		pkgTable:[],
		rptvisible:false,
		reportTypeList:[],
		reportType:null,
		groupid:null,
		groupList:[],
		fromDate:null,
		endDate:null,
	})
	const{
		isload,
		tableData,
		queryParams,
		pkglist,
		rolelist,
		pkglistAll,
		visible,
		pkgTable,
		rptvisible,
		reportTypeList,
		reportType,
		groupid,
		groupList,
		fromDate,
		endDate,
	}=toRefs(state)
	function handleQty(value){
		if(value > 9999999){
			return '不限'
		}else{
			return value;
		}
	}
	//日期改变
	function changedate(dates){
		state.fromDate=dates.start;
		state.endDate=dates.end;
	}
	function submitForm(){
		//String groupid,String reportType,String start,String end,String ignore
		if(!state.groupid){
			ElMessage.error("店铺不能为空");
			return;
		}
		if(!state.reportType){
			ElMessage.error("报表类型不能为空");
			return;
		}
		if(!state.fromDate || !state.endDate){
			ElMessage.error("开始结束日期不能为空");
			return;
		}
		limitApi.requestGroupReport({"groupid":state.groupid,
		                             "reportType":state.reportType,
		                             "start":state.fromDate,
									 "end":state.endDate,
									 "ignore":"true"}).then((res)=>{
			    ElMessage.success("申请成功");
		});
	}
	
	function loadRptData(shopid){
		limitApi.selectGroupList({"shopid":shopid}).then((res)=>{
			state.groupList=res.data;
		});
	}
	function showTaskDialog(shopid){
		state.rptvisible=true;
		loadRptData(shopid);
	}
	function showPkgDialog(shopid){
		state.pkgTable=[];
		state.visible=true;
		limitApi.detail({"shopid":shopid}).then((res)=>{
			state.pkgTable.push(res.data);
		});
	}
	 function handleQuery(){
	 	globalTable.value.loadTable(state.queryParams);
		state.isload=false;
	 }
	 function loadTableData(params){
	 	 limitApi.queryByShopName(params).then(res=>{
			 state.tableData.records=res.data.records;
			 state.tableData.total=res.data.total;
	 	 })
	 }
	 
	 
	 
 
	function loadData(){
		limitApi.summary().then((res)=>{
			state.pkglist=res.data;
		});
		limitApi.pkglist().then((res)=>{
			state.pkglistAll=res.data;
		});
		listRole().then(res=>{
			 state.rolelist=res.data;
		})
		handleQuery();
		limitApi.reportTypeList().then((res)=>{
			state.reportTypeList=res.data;
		});
	}
	function handlePkgQuery(id){
		state.queryParams.pkgid=id;
		handleQuery();
	}
	
	onMounted(()=>{
		loadData();
	})
	 
</script>

<style scoped >
	 
</style>
 