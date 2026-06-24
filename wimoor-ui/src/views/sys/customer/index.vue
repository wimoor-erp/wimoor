<template>
	<div class="main-sty con-header"> 
			<el-col :span="24">
			<el-tabs v-model="activeTab" @tab-change="handleTabChange">
				<el-tab-pane label="全部" name="all">
					<template #label>
						<span>全部</span>
					</template>
				</el-tab-pane>
				<el-tab-pane 
					v-for="item in pkglist" 
					:key="item.id" 
					:label="item.NAME || item.name" 
					:name="item.id"
				>
					<template #label>
						<span>{{item.NAME || item.name}}</span>
						<el-badge v-if="getSummaryByPkgId(item.id)" :value="getSummaryByPkgId(item.id)" class="badge" />
					</template>
				</el-tab-pane>
			</el-tabs>
			</el-col>	
	  <el-row>
	   <el-space>
		 <el-select v-model="queryParams.roleid" @change="handleQuery" @clear="queryParams.roleid=null;handleQuery()" placeholder="选择角色" clearable>
		 	<el-option v-for="item in rolelist" :label="item.name" :value="item.id" :key="item.id"></el-option>
		 </el-select>
	  	 <el-input class='ic-btn' v-model="queryParams.name" @input="handleQuery" placeholder="搜索公司名称,账号,手机号,邮箱,备注" clearable></el-input>
	  	 <el-button type="primary" @click="handleQuery">查询</el-button>
	    </el-space>
	    </el-row>
	  <GlobalTable
	   :tableData="tableData"
	   height="calc(100vh - 260px)"  @loadTable="loadTableData" 
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
			<el-table-column label="注册时间"     prop="createDate"  sortable="custom">
				<template #default="scope">
          <div >{{dateTimesFormat(scope.row.createDate)}}</div>
					<div class="font-extraSmall"><span >Tel:</span>{{scope.row.telephone}}</div>
					<div class="font-extraSmall"><span >Email:</span>{{scope.row.email}}</div>
				</template> 
			</el-table-column>
	 
			<el-table-column label="最后登录时间"     prop="lastlogintime"  sortable="custom">
				<template #default="scope">
					<div>{{scope.row.lastlogintime}}</div>
					<div class="font-extraSmall">注册时间：{{dateTimesFormat(scope.row.createDate)}}</div>
					<div class="font-extraSmall">过期时间：{{dateFormat(scope.row.expirationTime)}}</div>
				</template> 
			</el-table-column>
			<el-table-column label="套餐类型"     prop="tariffpackage"  sortable="custom">
				<template #default="scope">
					 <el-tag :type="getPackageTagType(scope.row.tariffpackage)">{{getPackageName(scope.row.tariffpackage)}}</el-tag>
					 <div class="font-extraSmall pointer" @click="showPkgDialog(scope.row.shopid)">套餐使用详情</div>
				</template> 
			</el-table-column>
			<el-table-column label="套餐备注"  width="450"   prop="remark"  sortable="custom">
				<template #default="scope">
					<div class="remark-cell">
						<span class="remark-text">{{scope.row.remark || '-'}}</span>
						<el-button 
							type="text" 
							class="remark-edit-btn"
							@click="openRemarkDialog(scope.row)"
							icon="Edit"
						>编辑</el-button>
					</div>
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
					<el-button link type="primary" @click="showCustomPkgDialog(scope.row.shopid, scope.row)">自定义套餐</el-button>
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
	 <div v-if="pkgTable.length > 0" class="pkg-detail-container">
		<div class="pkg-section">
			<div class="section-header">
				<el-icon class="section-icon"><component :is="icons.Box" /></el-icon>
				<span class="section-title">额度使用情况</span>
			</div>
			<div class="pkg-grid">
				<div class="pkg-card">
					<div class="card-icon orders-icon">
						<el-icon><component :is="icons.ShoppingCart" /></el-icon>
					</div>
					<div class="card-content">
						<span class="card-label">处理订单</span>
						<div class="card-value-row">
							<span class="card-value used">{{pkgTable[0].existOrderCount || 0}}</span>
							<span class="card-divider">/</span>
							<span class="card-value total">{{handleQty(pkgTable[0].maxOrderCount)}}</span>
						</div>
						<div class="card-progress">
							<el-progress :percentage="getUsagePercent(pkgTable[0].existOrderCount, pkgTable[0].maxOrderCount)" :show-text="false" stroke-width="6" />
						</div>
					</div>
				</div>
				
			</div>
		</div>
		
		<el-collapse v-model="collapseActiveNames" class="collapse-section">
			<el-collapse-item name="other-limits">
				<template #title>
					<span class="collapse-title">查看其他额度详情</span>
				</template>
				<div class="pkg-grid secondary-grid">
					<div class="pkg-card secondary-card">
						<div class="card-icon shops-icon">
							<el-icon><component :is="icons.Building" /></el-icon>
						</div>
						<div class="card-content">
							<span class="card-label">绑定店铺</span>
							<div class="card-value-row">
								<span class="card-value used">{{pkgTable[0].existShopCount || 0}}</span>
								<span class="card-divider">/</span>
								<span class="card-value total">{{handleQty(pkgTable[0].maxShopCount)}}</span>
							</div>
							<div class="card-progress">
								<el-progress :percentage="getUsagePercent(pkgTable[0].existShopCount, pkgTable[0].maxShopCount)" :show-text="false" stroke-width="4" />
							</div>
						</div>
					</div>
					
					<div class="pkg-card secondary-card">
						<div class="card-icon markets-icon">
							<el-icon><component :is="icons.Globe" /></el-icon>
						</div>
						<div class="card-content">
							<span class="card-label">绑定站点</span>
							<div class="card-value-row">
								<span class="card-value used">{{pkgTable[0].existMarketCount || 0}}</span>
								<span class="card-divider">/</span>
								<span class="card-value total">{{handleQty(pkgTable[0].maxMarketCount)}}</span>
							</div>
							<div class="card-progress">
								<el-progress :percentage="getUsagePercent(pkgTable[0].existMarketCount, pkgTable[0].maxMarketCount)" :show-text="false" stroke-width="4" />
							</div>
						</div>
					</div>
					
					<div class="pkg-card secondary-card">
						<div class="card-icon users-icon">
							<el-icon><component :is="icons.User" /></el-icon>
						</div>
						<div class="card-content">
							<span class="card-label">子用户</span>
							<div class="card-value-row">
								<span class="card-value used">{{pkgTable[0].existMember || 0}}</span>
								<span class="card-divider">/</span>
								<span class="card-value total">{{handleQty(pkgTable[0].maxMember)}}</span>
							</div>
							<div class="card-progress">
								<el-progress :percentage="getUsagePercent(pkgTable[0].existMember, pkgTable[0].maxMember)" :show-text="false" stroke-width="4" />
							</div>
						</div>
					</div>
					
					<div class="pkg-card secondary-card">
						<div class="card-icon products-icon">
							<el-icon><component :is="icons.Package" /></el-icon>
						</div>
						<div class="card-content">
							<span class="card-label">商品数量</span>
							<div class="card-value-row">
								<span class="card-value used">{{pkgTable[0].existProductCount || 0}}</span>
								<span class="card-divider">/</span>
								<span class="card-value total">{{handleQty(pkgTable[0].maxProductCount)}}</span>
							</div>
							<div class="card-progress">
								<el-progress :percentage="getUsagePercent(pkgTable[0].existProductCount, pkgTable[0].maxProductCount)" :show-text="false" stroke-width="4" />
							</div>
						</div>
					</div>
					
					<div class="pkg-card secondary-card">
						<div class="card-icon ads-icon">
							<el-icon><component :is="icons.Reading" /></el-icon>
						</div>
						<div class="card-content">
							<span class="card-label">广告组</span>
							<div class="card-value-row">
								<span class="card-value used">{{pkgTable[0].existdayOpenAdvCount || 0}}</span>
								<span class="card-divider">/</span>
								<span class="card-value total">{{handleQty(pkgTable[0].maxdayOpenAdvCount)}}</span>
							</div>
							<div class="card-progress">
								<el-progress :percentage="getUsagePercent(pkgTable[0].existdayOpenAdvCount, pkgTable[0].maxdayOpenAdvCount)" :show-text="false" stroke-width="4" />
							</div>
						</div>
					</div>
					
					<div class="pkg-card secondary-card">
						<div class="card-icon plans-icon">
							<el-icon><component :is="icons.BarChart" /></el-icon>
						</div>
						<div class="card-content">
							<span class="card-label">利润方案</span>
							<div class="card-value-row">
								<span class="card-value used">{{pkgTable[0].existProfitPlanCount || 0}}</span>
								<span class="card-divider">/</span>
								<span class="card-value total">{{handleQty(pkgTable[0].maxProfitPlanCount)}}</span>
							</div>
							<div class="card-progress">
								<el-progress :percentage="getUsagePercent(pkgTable[0].existProfitPlanCount, pkgTable[0].maxProfitPlanCount)" :show-text="false" stroke-width="4" />
							</div>
						</div>
					</div>
				</div>
			</el-collapse-item>
		</el-collapse>
		
		<div class="pkg-section">
			<div class="section-header">
				<el-icon class="section-icon"><component :is="icons.Clock" /></el-icon>
				<span class="section-title">有效期</span>
			</div>
			<div class="date-card">
				<div class="date-item">
					<el-icon class="date-icon"><component :is="icons.CircleCheck" /></el-icon>
					<div>
						<span class="date-label">生效时间</span>
						<span class="date-value">{{dateFormat(pkgTable[0].effectiveTime) || '-'}}</span>
					</div>
				</div>
				<div class="date-divider"></div>
				<div class="date-item">
					<el-icon class="date-icon"><component :is="icons.CircleClose" /></el-icon>
					<div>
						<span class="date-label">失效时间</span>
						<span class="date-value">{{dateFormat(pkgTable[0].expirationTime) || '-'}}</span>
					</div>
				</div>
			</div>
		</div>
		
		<div v-if="pkgTable[0].orderSummary" class="pkg-section">
			<div class="section-header">
				<el-icon class="section-icon"><component :is="icons.DataBoard" /></el-icon>
				<span class="section-title">订单统计</span>
			</div>
			<div class="order-summary-card">
				<div class="summary-item">
					<span class="summary-label">订单总数</span>
					<span class="summary-value">{{pkgTable[0].existOrderCount || '-'}}</span>
				</div>
			</div>
			<div class="order-detail-section">
				<h5 class="detail-section-title">每日订单明细</h5>
				<div class="order-detail-grid">
					<div v-for="(value, date) in pkgTable[0].orderSummary" :key="date" 
						 v-show="date !== 'ordersum' && date !== 'orderpricesum'"
						 class="detail-item">
						<span class="detail-date">{{date}}</span>
						<span class="detail-info">订单数: {{value.ordersum || 0}}</span>
					</div>
				</div>
			</div>
		</div>
	</div>
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
	
	<el-dialog
	title="自定义套餐"
	v-model="customPkgVisible"
	width="70%"
	>
		<el-form :model="customPkgForm" label-width="150px">
			<el-form-item label="公司名称">
				<el-input v-model="customPkgForm.shopName" disabled />
			</el-form-item>
			<el-form-item label="选择套餐" required>
				<el-select 
					v-model="customPkgForm.tariffpackage" 
					placeholder="请选择套餐" 
					filterable
					@change="handlePackageChange"
				>
					<el-option v-for="item in pkglistAll" :key="item.id" :value="item.id" :label="item.name || item.NAME"></el-option>
				</el-select>
			</el-form-item>
      <el-form-item label="生效时间" required>
        <el-date-picker v-model="customPkgForm.effectiveTime" type="date" value-format="YYYY-MM-DD" />
      </el-form-item>
			<el-form-item label="失效时间" required>
				<el-date-picker v-model="customPkgForm.expirationTime" type="date" value-format="YYYY-MM-DD" />
			</el-form-item>
			<el-form-item label="订单数量限制">
				<el-input v-model="customPkgForm.maxOrderCount" type="number" />
			</el-form-item>
			  <el-divider />
			<el-form-item label="店铺数量限制">
				<el-input v-model="customPkgForm.maxShopCount" type="number" />
			</el-form-item>
			<el-form-item label="站点数量限制">
				<el-input v-model="customPkgForm.maxMarketCount" type="number" />
			</el-form-item>
			<el-form-item label="子用户数量限制">
				<el-input v-model="customPkgForm.maxMember" type="number" />
			</el-form-item>
			<el-form-item label="商品数量限制">
				<el-input v-model="customPkgForm.maxProductCount" type="number" />
			</el-form-item>
			
			<el-form-item label="广告限制">
				<el-input v-model="customPkgForm.maxdayOpenAdvCount" type="number" />
			</el-form-item>
			<el-form-item label="利润方案数量限制">
				<el-input v-model="customPkgForm.maxProfitPlanCount" type="number" />
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="customPkgVisible=false">关闭</el-button>
			<el-button @click="saveCustomPkg" type="primary">保存</el-button>
		</template>
	</el-dialog>
	
	<el-dialog
		title="编辑套餐备注"
		v-model="remarkDialogVisible"
		width="500px"
	>
		<el-form :model="remarkForm" label-width="100px">
			<el-form-item label="公司名称">
				<el-input v-model="remarkForm.shopName" disabled />
			</el-form-item>
			<el-form-item label="备注内容">
				<el-input 
					v-model="remarkForm.remark" 
					type="textarea" 
					:rows="6" 
					placeholder="请输入套餐备注"
				/>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="remarkDialogVisible=false">关闭</el-button>
			<el-button @click="saveRemarkDialog" type="primary">保存</el-button>
		</template>
	</el-dialog>
</template>
<script>
    export default{ name:"客户管理" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled,Box,ShoppingCart,OfficeBuilding,User,Reading,Histogram,Clock,DataBoard,CircleCheckFilled,CircleCloseFilled} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
    import '@/assets/css/packbox_table.css';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import { listRole} from "@/api/sys/admin/roles"
	import limitApi from '@/api/sys/admin/limitApi.js';
    import summaryChartApi from '@/api/amazon/order/summaryChartApi.js';
	const globalTable=ref();
	const ownerRef=ref();
	const accDialogRef=ref();
	const calcDialogRef=ref();
	const icons = {
	Box,
	ShoppingCart,
	OfficeBuilding,
	User,
	Reading,
	Histogram,
	Clock,
	DataBoard,
	CircleCheckFilled,
	CircleCloseFilled
};

function getUsagePercent(used, total) {
	if (!used || !total || total <= 0) {
		return 0;
	}
	return Math.round((used / total) * 100);
}

const collapseActiveNames = ref([]);

const  state=reactive({
		isload:true,
		tableData: {records:[],total:0}  ,
		queryParams:{name:'',pkgid:null,roleid:null},
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
		summaryData: {},
		customPkgVisible:false,
		customPkgForm:{
			shopid: null,
			shopName: '',
			packageId: null,
			effectiveTime: '',
			expirationTime: '',
			maxShopCount: 0,
			maxMarketCount: 0,
			maxMember: 0,
			maxProductCount: 0,
			maxOrderCount: 0,
			maxdayOpenAdvCount: 0,
			maxProfitPlanCount: 0,
		},
	})
	const activeTab = ref('all');
	const remarkDialogVisible = ref(false);
	const remarkForm = reactive({
		shopid: null,
		shopName: '',
		remark: '',
	});
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
		customPkgVisible,
		customPkgForm,
		reportTypeList,
		reportType,
		groupid,
		groupList,
		fromDate,
		endDate,
		summaryData,
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
			const data = res.data;
			state.pkgTable.push(data);
			let startDate = data.effectiveTime;
			if (!startDate) {
				const oneYearAgo = new Date();
				oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);
				startDate = formatDate(oneYearAgo);
			}
			summaryChartApi.getOrderTotal({
				shopid: shopid,
				startdate: startDate
			}).then(res => {
				if (res.data) {
					if (res.data.ordersum !== undefined) {
						state.pkgTable[0].existOrderCount = parseInt(res.data.ordersum);
					}
					if (res.data.orderpricesum !== undefined) {
						state.pkgTable[0].orderpricesum = res.data.orderpricesum;
					}
					state.pkgTable[0].orderSummary = res.data;
				}
			}).catch(() => {
				console.error('获取订单总数失败');
			});
		});
	}
	
	function getOrderDetailList() {
		if (!state.pkgTable[0]?.orderSummary) {
			return [];
		}
		const result = [];
		const summary = state.pkgTable[0].orderSummary;
		for (const key in summary) {
			if (key !== 'ordersum' && key !== 'orderpricesum') {
				result.push({
					date: key,
					ordersum: summary[key]?.ordersum || 0,
					orderprice: summary[key]?.orderprice || 0
				});
			}
		}
		return result.sort((a, b) => new Date(b.date) - new Date(a.date));
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
		limitApi.pkglist().then((res)=>{
			state.pkglist=res.data;
			state.pkglistAll=res.data;
		});
		listRole().then(res=>{
			 state.rolelist=res.data;
		})
		handleQuery();
		limitApi.reportTypeList().then((res)=>{
			state.reportTypeList=res.data;
		});
		limitApi.summary().then((res)=>{
			const summaryArray = res.data;
			const summaryMap = {};
			summaryArray.forEach((item, index) => {
				if (index === 0) {
					summaryMap.total = item.qty;
				} else if (item.id !== undefined) {
					summaryMap[item.id] = item.qty;
				}
			});
			state.summaryData = summaryMap;
		});
	}
	function getPackageName(tariffpackage) {
		return tariffpackage || '未知';
	}
	
	function getPackageTagType(tariffpackage) {
		const typeMap = {
			'免费版': 'info',
			'入门版': 'success',
			'标准版': '',
			'专业版': 'warning',
			'旗舰版': 'danger',
		};
		return typeMap[tariffpackage] || '';
	}
	
	function handlePkgQuery(id){
		state.queryParams.pkgid=id;
		activeTab.value = id;
		handleQuery();
	}
	
	function handleTabChange(tabName) {
		if (tabName === 'all') {
			state.queryParams.pkgid = null;
		} else {
			state.queryParams.pkgid = tabName;
		}
		handleQuery();
	}
	
	function getSummaryByPkgId(pkgId) {
		return state.summaryData[pkgId] || 0;
	}
	
	function showCustomPkgDialog(shopid, row) {
		customPkgVisible.value = true;
		customPkgForm.value = {
			shopId: shopid,
			shopName: row.shopName || '',
			packageId: null,
      expirationTime: '',
			maxShopCount: 0,
			maxMarketCount: 0,
			maxMember: 0,
			maxProductCount: 0,
			maxOrderCount: 0,
			maxdayOpenAdvCount: 0,
			maxProfitPlanCount: 0,
		};
		limitApi.detail({shopid: shopid}).then(res => {
			if (res.data) {
				const data = res.data;
				customPkgForm.value.tariffpackage = data.tariffpackage !== undefined && data.tariffpackage !== null ? Number(data.tariffpackage) : null;
				customPkgForm.value.expirationTime = data.expirationTime || '';
				customPkgForm.value.maxShopCount = data.maxShopCount || 0;
				customPkgForm.value.maxMarketCount = data.maxMarketCount || 0;
				customPkgForm.value.maxMember = data.maxMember || 0;
				customPkgForm.value.maxProductCount = data.maxProductCount || 0;
				customPkgForm.value.maxOrderCount = data.maxOrderCount || 0;
				customPkgForm.value.maxdayOpenAdvCount = data.maxdayOpenAdvCount || 0;
				customPkgForm.value.maxProfitPlanCount = data.maxProfitPlanCount || 0;
				customPkgForm.value.remark = data.remark || '';
				customPkgForm.value.shopId = shopid;
        //如果生效时间为空，默认当前时间
				if (!data.effectiveTime) {
					const now = new Date();
					customPkgForm.value.effectiveTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
				} else {
					customPkgForm.value.effectiveTime = data.effectiveTime;
				}
			}
		}).catch(() => {
			ElMessage.error("获取套餐信息失败");
		});
	}
	
	function handlePackageChange(tariffpackage) {
		if (tariffpackage === null || tariffpackage === undefined) return;
		const pkg = state.pkglistAll.find(item => item.id === tariffpackage);
		if (pkg) {
			customPkgForm.value.maxShopCount = pkg.maxShopCount || 0;
			customPkgForm.value.maxMarketCount = pkg.maxMarketCount || 0;
			customPkgForm.value.maxMember = pkg.maxMember || 0;
			customPkgForm.value.maxProductCount = pkg.maxProductCount || 0;
			customPkgForm.value.maxOrderCount = pkg.maxOrderCount || 0;
			customPkgForm.value.maxdayOpenAdvCount = pkg.dayOpenAdvCount || 0;
			customPkgForm.value.maxProfitPlanCount = pkg.maxProfitPlanCount || 0;
		}
	}
	
	function saveCustomPkg() {
		if (customPkgForm.value.tariffpackage === null || customPkgForm.value.tariffpackage === undefined) {
			ElMessage.error("请选择套餐");
			return;
		}
		if (!customPkgForm.value.expirationTime) {
			ElMessage.error("请选择失效时间");
			return;
		}
		if (!customPkgForm.value.effectiveTime) {
      customPkgForm.value.effectiveTime = new Date().toISOString();
    }

		limitApi.updateLimit(customPkgForm.value).then(res => {
			ElMessage.success("保存成功");
			customPkgVisible.value = false;
			handleQuery();
		}).catch(() => {
			ElMessage.error("保存失败");
		});
	}
	
	function openRemarkDialog(row) {
		remarkForm.shopid = row.shopid;
		remarkForm.shopName = row.shopName || '';
		remarkForm.remark = row.remark || '';
		remarkDialogVisible.value = true;
	}
	
	function saveRemarkDialog() {
		limitApi.updateRemark({
			shopId: remarkForm.shopid,
			remark: remarkForm.remark
		}).then(res => {
			ElMessage.success("备注修改成功");
			remarkDialogVisible.value = false;
			handleQuery();
		}).catch(() => {
			ElMessage.error("修改失败");
		});
	}
	
	onMounted(()=>{
		loadData();
	})
	 
</script>

<style scoped >
	.badge {
		margin-left: 4px;
	}
	.remark-cell {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 10px;
	}
	.remark-text {
		flex: 1;
		white-space: pre-wrap;
		word-break: break-all;
		line-height: 1.5;
	}
	.remark-edit-btn {
		flex-shrink: 0;
		padding: 2px 8px;
		font-size: 12px;
	}
	
	.pkg-detail-container {
		padding: 20px;
	}
	
	.pkg-section {
		margin-bottom: 24px;
	}
	
	.section-header {
		display: flex;
		align-items: center;
		gap: 8px;
		margin-bottom: 16px;
		padding-bottom: 12px;
		border-bottom: 2px solid #f0f0f0;
	}
	
	.section-icon {
		font-size: 18px;
		color: #667eea;
	}
	
	.section-title {
		font-size: 16px;
		font-weight: 600;
		color: #333;
	}
	
	.pkg-grid {
		display: grid;
		grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
		gap: 16px;
	}
	
	.pkg-card {
		display: flex;
		gap: 12px;
		padding: 16px;
		background: #fff;
		border-radius: 12px;
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
		transition: all 0.3s ease;
	}
	
	.pkg-card:hover {
		box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
		transform: translateY(-2px);
	}
	
	.card-icon {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 48px;
		height: 48px;
		border-radius: 12px;
		flex-shrink: 0;
	}
	
	.card-icon el-icon {
		font-size: 24px;
		color: #fff;
	}
	
	.orders-icon {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	}
	
	.shops-icon {
		background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
	}
	
	.markets-icon {
		background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
	}
	
	.users-icon {
		background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
	}
	
	.products-icon {
		background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
	}
	
	.ads-icon {
		background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
	}
	
	.plans-icon {
		background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
	}
	
	.card-content {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 4px;
		min-width: 0;
	}
	
	.card-label {
		font-size: 12px;
		color: #999;
	}
	
	.card-value-row {
		display: flex;
		align-items: baseline;
		gap: 4px;
	}
	
	.card-value {
		font-size: 20px;
		font-weight: 600;
	}
	
	.card-value.used {
		color: #667eea;
	}
	
	.card-value.total {
		font-size: 14px;
		color: #999;
		font-weight: 400;
	}
	
	.card-divider {
		color: #ddd;
		margin: 0 2px;
	}
	
	.card-progress {
		margin-top: 4px;
	}
	
	.date-card {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 30px;
		padding: 20px;
		background: #f8f9fa;
		border-radius: 12px;
	}
	
	.date-item {
		display: flex;
		align-items: center;
		gap: 12px;
	}
	
	.date-icon {
		font-size: 24px;
		color: #667eea;
	}
	
	.date-item div {
		display: flex;
		flex-direction: column;
		gap: 4px;
	}
	
	.date-label {
		font-size: 12px;
		color: #999;
	}
	
	.date-value {
		font-size: 16px;
		font-weight: 600;
		color: #333;
	}
	
	.date-divider {
		width: 1px;
		height: 40px;
		background: #ddd;
	}
	
	.order-summary-card {
		display: flex;
		gap: 20px;
		padding: 16px;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 12px;
		margin-bottom: 16px;
	}
	
	.summary-item {
		display: flex;
		flex-direction: column;
		gap: 4px;
	}
	
	.summary-label {
		font-size: 12px;
		color: rgba(255, 255, 255, 0.7);
	}
	
	.summary-value {
		font-size: 24px;
		font-weight: 600;
		color: #fff;
	}
	
	.detail-section-title {
		font-size: 14px;
		font-weight: 500;
		color: #666;
		margin: 0 0 12px 0;
	}
	
	.order-detail-grid {
		display: grid;
		grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
		gap: 12px;
	}
	
	.detail-item {
		display: flex;
		flex-direction: column;
		gap: 4px;
		padding: 12px;
		background: #fff;
		border-radius: 8px;
		border: 1px solid #eee;
	}
	
	.detail-date {
		font-size: 14px;
		font-weight: 600;
		color: #333;
	}
	
	.detail-info {
		font-size: 12px;
		color: #666;
	}
	
	.collapse-section {
		margin-bottom: 24px;
		border-radius: 12px;
		overflow: hidden;
	}
	
	.collapse-title {
		font-size: 13px;
		color: #888;
	}
	
	.secondary-grid {
		padding-top: 12px;
	}
	
	.secondary-card {
		padding: 12px;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
	}
	
	.secondary-card .card-icon {
		width: 40px;
		height: 40px;
	}
	
	.secondary-card .card-icon el-icon {
		font-size: 20px;
	}
	
	.secondary-card .card-value {
		font-size: 16px;
	}
	
	.secondary-card .card-value.total {
		font-size: 12px;
	}
</style>
 