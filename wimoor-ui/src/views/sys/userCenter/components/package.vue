<template>
	<div>
		<el-descriptions :column="1">
			<el-descriptions-item>
				<div class="quota-card">
					<div class="quota-header">
						<div class="quota-title">订单处理额度</div>
						<div class="quota-status" :class="getQuotaStatus()">
							{{ getQuotaStatusText() }}
						</div>
					</div>
					<div class="quota-expiration" v-if="packageData.expirationTime">
						<span class="expiration-label">失效日期</span>
						<span class="expiration-value">{{ dateFormat(packageData.expirationTime) }}</span>
					</div>
					<div class="quota-numbers">
						<div class="quota-number-item">
							<div class="quota-label">已用额度</div>
							<div class="quota-value">{{ packageData.existOrderCount || 0 }}</div>
						</div>
						<div class="quota-divider"></div>
						<div class="quota-number-item">
							<div class="quota-label">总额度</div>
							<div class="quota-value">{{ calcMaxNum(packageData.maxOrderCount) }}</div>
						</div>
						<div class="quota-divider"></div>
						<div class="quota-number-item">
							<div class="quota-label">剩余额度</div>
							<div class="quota-value">{{ getRemainingQuota() }}</div>
						</div>
					</div>
					<div class="quota-progress">
						<div class="progress-info">
							<span class="progress-label">使用进度</span>
							<span class="progress-percent">{{ getUsagePercent().toFixed(2) }}%</span>
						</div>
						<el-progress 
							:percentage="getUsagePercent()" 
							:color="getProgressColor()"
							:stroke-width="12"
							:show-text="false"
						></el-progress>
					</div>
				</div>
			</el-descriptions-item>
		</el-descriptions>

		<!-- 订单列表 -->
		<div class="order-section">
			<div class="section-header">
				<h3 class="section-title">我的订单</h3>
				<span class="section-subtitle">默认显示最近一年的订单</span>
			</div>
			<el-table :data="orderList" :stripe="true" style="width: 100%;">
				<el-table-column prop="outTradeNo" label="订单号" width="180">
				</el-table-column>
				<el-table-column prop="subject" label="订单名称" min-width="200">
				</el-table-column>
				<el-table-column prop="packageName" label="套餐名称" width="120">
				</el-table-column>
				<el-table-column prop="totalAmount" label="金额" width="100">
					<template #default="scope">
						¥{{ scope.row.totalAmount }}
					</template>
				</el-table-column>
				<el-table-column prop="paystatus" label="支付状态" width="100">
					<template #default="scope">
						<span :class="getPayStatusClass(scope.row.paystatus)">
							{{ getPayStatusText(scope.row.paystatus) }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="paytime" label="支付时间" width="140">
					<template #default="scope">
						{{ scope.row.paytime ? dateFormat(scope.row.paytime) : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="ivcstatus" label="开票状态" width="100">
					<template #default="scope">
						<span :class="getInvoiceStatusClass(scope.row.ivcstatus)">
							{{ getInvoiceStatusText(scope.row.ivcstatus) }}
						</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template #default="scope">
						<el-button 
							v-if="!scope.row.hasInvoiceRequest && scope.row.paystatus === 'success'" 
							@click="handleInvoiceApply(scope.row)" 
							type="text" 
							:disabled="scope.row.paystatus !== 'success'"
						>
							申请开票
						</el-button>
						<template v-else-if="scope.row.hasInvoiceRequest">
							<el-button 
								@click="viewInvoiceInfo(scope.row)" 
								type="text" 
								class="text-blue"
							>
								查看发票信息
							</el-button>
							<el-button 
								v-if="scope.row.invoiceUrl" 
								@click="downloadInvoice(scope.row)" 
								type="text" 
								class="text-green"
							>
								下载发票
							</el-button>
						</template>
						<span v-else class="text-gray">-</span>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 发票申请弹窗 -->
		<el-dialog v-model="invoiceApplyVisible" title="发票申请" width="500px">
			<el-form :model="invoiceForm" label-width="120px">
				<el-form-item label="公司名称" :required="true">
					<el-input v-model="invoiceForm.companyName" placeholder="请输入公司名称" />
				</el-form-item>
				<el-form-item label="纳税人识别号" :required="true">
					<el-input v-model="invoiceForm.taxId" placeholder="请输入纳税人识别号" />
				</el-form-item>
				<el-form-item label="注册地址" :required="true">
					<el-input v-model="invoiceForm.address" placeholder="请输入注册地址" />
				</el-form-item>
				<el-form-item label="联系电话" :required="true">
					<el-input v-model="invoiceForm.phone" placeholder="请输入联系电话" />
				</el-form-item>
				<el-form-item label="开户行" :required="true">
					<el-input v-model="invoiceForm.bankName" placeholder="请输入开户行" />
				</el-form-item>
				<el-form-item label="银行账号" :required="true">
					<el-input v-model="invoiceForm.bankAccount" placeholder="请输入银行账号" />
				</el-form-item>
				<el-form-item label="备注">
					<el-input type="textarea" v-model="invoiceForm.remark" placeholder="请输入备注（选填）" :rows="3" />
				</el-form-item>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
					<el-button @click="invoiceApplyVisible = false">取消</el-button>
					<el-button type="primary" @click="submitInvoiceApply">提交申请</el-button>
				</div>
			</template>
		</el-dialog>

		<!-- 查看发票信息弹窗 -->
		<el-dialog v-model="invoiceInfoVisible" title="发票信息" width="500px">
			<el-form :model="viewInvoiceForm" label-width="120px">
				<el-form-item label="公司名称">
					<el-input v-if="isEditing" v-model="viewInvoiceForm.companyname" />
					<span v-else>{{ viewInvoiceForm.companyname || '-' }}</span>
				</el-form-item>
				<el-form-item label="纳税人识别号">
					<el-input v-if="isEditing" v-model="viewInvoiceForm.taxid" />
					<span v-else>{{ viewInvoiceForm.taxid || '-' }}</span>
				</el-form-item>
				<el-form-item label="注册地址">
					<el-input v-if="isEditing" v-model="viewInvoiceForm.address" type="textarea" :rows="2" />
					<span v-else>{{ viewInvoiceForm.address || '-' }}</span>
				</el-form-item>
				<el-form-item label="联系电话">
					<el-input v-if="isEditing" v-model="viewInvoiceForm.phone" />
					<span v-else>{{ viewInvoiceForm.phone || '-' }}</span>
				</el-form-item>
				<el-form-item label="开户行">
					<el-input v-if="isEditing" v-model="viewInvoiceForm.bankname" />
					<span v-else>{{ viewInvoiceForm.bankname || '-' }}</span>
				</el-form-item>
				<el-form-item label="银行账号">
					<el-input v-if="isEditing" v-model="viewInvoiceForm.bankaccount" />
					<span v-else>{{ viewInvoiceForm.bankaccount || '-' }}</span>
				</el-form-item>
				<el-form-item label="备注">
					<el-input v-if="isEditing" v-model="viewInvoiceForm.remark" type="textarea" :rows="2" />
					<span v-else>{{ viewInvoiceForm.remark || '-' }}</span>
				</el-form-item>
				<el-form-item v-if="viewInvoiceForm.location" label="发票文件">
					<el-list style="max-height: 200px; overflow-y: auto;">
						<el-list-item v-for="(url, index) in viewInvoiceForm.location.split(';')" :key="index">
							<a :href="url" target="_blank">{{ url.substring(url.lastIndexOf('/') + 1) }}</a>
						</el-list-item>
					</el-list>
				</el-form-item>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
					<el-button v-if="isEditing" @click="cancelEdit">取消</el-button>
					<el-button @click="invoiceInfoVisible = false" v-if="!isEditing">关闭</el-button>
					<el-button 
						v-if="isEditing" 
						type="primary" 
						@click="saveEditInvoice"
					>
						保存
					</el-button>
					<el-button 
						v-if="!isEditing && !viewInvoiceForm.location" 
						type="primary" 
						@click="startEditInvoice"
					>
						编辑
					</el-button>
					<el-button 
						v-if="!isEditing && viewInvoiceForm.location" 
						type="primary" 
						@click="downloadInvoiceFromView"
					>
						下载发票
					</el-button>
				</div>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	import {reactive,toRefs,ref} from"vue";
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import {Monitor,User,Document,House,Iphone,DataBoard} from '@element-plus/icons-vue'
	import limitApi from '@/api/sys/admin/limitApi.js';
	import Card from "./card.vue";
	import customerOrderApi from '@/api/sys/admin/customerOrderApi.js';
	import invoiceApi from '@/api/sys/admin/invoiceApi.js';
	import userApi from '@/api/sys/admin/userApi.js';
	import summaryChartApi from '@/api/amazon/order/summaryChartApi.js';
	
	const state = reactive({
		packageData:{},
		viptag:"svip.png",
		orderList: [],
	})
	
	const invoiceApplyVisible = ref(false);
	const invoiceInfoVisible = ref(false);
	const currentOrder = ref(null);
	const currentUser = ref({});
	const invoiceForm = reactive({
		companyName: '',
		taxId: '',
		address: '',
		phone: '',
		bankName: '',
		bankAccount: '',
		remark: '',
	});
	const viewInvoiceForm = reactive({
		companyname: '',
		taxid: '',
		address: '',
		phone: '',
		bankname: '',
		bankaccount: '',
		remark: '',
		location: '',
	});
	const isEditing = ref(false);
	const editingOrderId = ref(null);
	
	const {
		packageData,
		viptag,
		orderList,
	}=toRefs(state)
	
	function calcMaxNum(value){
		if(value>9999999){
			return '∞';
		}else{
			return value;
		}
	}
	
	function getRemainingQuota(){
		const max = packageData.value.maxOrderCount;
		const used = packageData.value.existOrderCount || 0;
		if(max>9999999){
			return '∞';
		}
		const remaining = max - used;
		return remaining > 0 ? remaining : 0;
	}
	
	function getUsagePercent(){
		const max = packageData.value.maxOrderCount;
		const used = packageData.value.existOrderCount || 0;
		if(max>9999999){
			return 10;
		}
		if(max <= 0) return 0;
		const percent = (used / max) * 100;
		return Math.min(Math.max(percent, 0), 100);
	}
	
	function getProgressColor(){
		const percent = getUsagePercent();
		if(percent >= 90) return '#f56c6c';
		if(percent >= 70) return '#e6a23c';
		if(percent >= 50) return '#409eff';
		return '#67c23a';
	}
	
	function getQuotaStatus(){
		const percent = getUsagePercent();
		if(percent >= 90) return 'status-critical';
		if(percent >= 70) return 'status-warning';
		if(percent >= 50) return 'status-normal';
		return 'status-good';
	}
	
	function getQuotaStatusText(){
		const max = packageData.value.maxOrderCount;
		if(max>9999999){
			return '无限额度';
		}
		const percent = getUsagePercent();
		if(percent >= 90) return '额度紧张';
		if(percent >= 70) return '额度预警';
		if(percent >= 50) return '使用正常';
		return '额度充足';
	}
	
	function show(){
		limitApi.getMyManagerLimitmeal().then((res)=>{
			if(res.data){
				state.packageData=res.data;
				fetchOrderTotal();
			}
			if(res.data.tariffpackage===4){
				state.viptag = "svip.png";
			}else if(res.data.tariffpackage===3){
				state.viptag = "tvip.png";
			}else if(res.data.tariffpackage===2||res.data.tariffpackage===1){
				state.viptag = "bvip.png";
			}else{
				state.viptag = "free.png";
			}
		});
		userApi.getInfo().then(res => {
			if (res.data) {
				currentUser.value = res.data;
			}
		});
		loadOrderList();
	}
	
	function fetchOrderTotal() {
		let startDate = packageData.value.effectiveTime;
		if (!startDate) {
			const oneYearAgo = new Date();
			oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);
			startDate = formatDate(oneYearAgo);
		}
		summaryChartApi.getMyOrderTotal({
			startdate: startDate
		}).then(res => {
			if (res.data && res.data.ordersum !== undefined) {
				state.packageData.existOrderCount = parseInt(res.data.ordersum);
			}
		}).catch(() => {
			console.error('获取订单总数失败');
		});
	}
	
	function loadOrderList() {
		const oneYearAgo = new Date();
		oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);
		const startTime = formatDate(oneYearAgo) + ' 00:00:00';
		const endTime = formatDate(new Date()) + ' 23:59:59';
		
		customerOrderApi.list({
			pageNum: 1,
			pageSize: 50,
			startTime: startTime,
			endTime: endTime,
		}).then(res => {
			if (res.data && res.data.records) {
				state.orderList = res.data.records.map(order => {
					return {
						...order,
						invoiceUrl: null,
						hasInvoiceRequest: false,
					};
				});
				fetchInvoiceInfo();
			}
		});
	}
	
	function formatDate(date) {
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${year}-${month}-${day}`;
	}
	
	function fetchInvoiceInfo() {
		state.orderList.forEach(order => {
			invoiceApi.detail({ orderid: order.id }).then(res => {
				if (res.data) {
					order.hasInvoiceRequest = true;
					order.invoiceUrl = res.data.location;
				}
			});
		});
	}
	
	function getPayStatusClass(status) {
		switch (status) {
			case 'success': return 'status-success';
			case 'pending': return 'status-warning';
			case 'failed': return 'status-error';
			default: return 'status-default';
		}
	}
	
	function getPayStatusText(status) {
		switch (status) {
			case 'success': return '已支付';
			case 'pending': return '待支付';
			case 'failed': return '支付失败';
			default: return status || '-';
		}
	}
	
	function getInvoiceStatusClass(status) {
		switch (status) {
			case 'invoiced': return 'status-success';
			case 'pending': return 'status-warning';
			default: return 'status-default';
		}
	}
	
	function getInvoiceStatusText(status) {
		switch (status) {
			case 'invoiced': return '已开票';
			case 'pending': return '待开票';
			default: return '-';
		}
	}
	
	function handleInvoiceApply(order) {
		currentOrder.value = order;
		invoiceForm.companyName = '';
		invoiceForm.taxId = '';
		invoiceForm.address = '';
		invoiceForm.phone = '';
		invoiceForm.bankName = '';
		invoiceForm.bankAccount = '';
		invoiceForm.remark = '';
		invoiceApplyVisible.value = true;
	}
	
	function submitInvoiceApply() {
		if (!invoiceForm.companyName || !invoiceForm.taxId || !invoiceForm.address || 
			!invoiceForm.phone || !invoiceForm.bankName || !invoiceForm.bankAccount) {
			ElMessage.error('请填写完整的发票信息');
			return;
		}
		
		invoiceApi.save({
			orderid: currentOrder.value.id,
			shopid: currentUser.value.shopid || currentUser.value.id,
			company: invoiceForm.companyName,
			invoice: invoiceForm.taxId,
			address: invoiceForm.address,
			phone: invoiceForm.phone,
			bank: invoiceForm.bankName,
			cardNo: invoiceForm.bankAccount,
			remark: invoiceForm.remark,
		}).then(res => {
			ElMessage.success('发票申请提交成功');
			invoiceApplyVisible.value = false;
			loadOrderList();
		}).catch(() => {
			ElMessage.error('提交失败');
		});
	}
	
	function downloadInvoice(order) {
		if (!order.invoiceUrl) {
			ElMessage.error('暂无发票文件');
			return;
		}
		window.open(order.invoiceUrl, '_blank');
	}

	function viewInvoiceInfo(order) {
		editingOrderId.value = order.id;
		invoiceApi.detail({ orderid: order.id }).then(res => {
			if (res.data) {
				viewInvoiceForm.companyname = res.data.companyname || res.data.company || '';
				viewInvoiceForm.taxid = res.data.taxid || res.data.invoice || '';
				viewInvoiceForm.address = res.data.address || '';
				viewInvoiceForm.phone = res.data.phone || '';
				viewInvoiceForm.bankname = res.data.bankname || res.data.bank || '';
				viewInvoiceForm.bankaccount = res.data.card_no || res.data.bankaccount || res.data.cardNo || res.data.cardno || '';
				viewInvoiceForm.remark = res.data.remark || '';
				viewInvoiceForm.location = res.data.location || '';
			} else {
				viewInvoiceForm.companyname = '';
				viewInvoiceForm.taxid = '';
				viewInvoiceForm.address = '';
				viewInvoiceForm.phone = '';
				viewInvoiceForm.bankname = '';
				viewInvoiceForm.bankaccount = '';
				viewInvoiceForm.remark = '';
				viewInvoiceForm.location = '';
			}
			isEditing.value = false;
			invoiceInfoVisible.value = true;
		});
	}
	
	function startEditInvoice() {
		isEditing.value = true;
	}
	
	function saveEditInvoice() {
		if (!viewInvoiceForm.companyname || !viewInvoiceForm.taxid || !viewInvoiceForm.address || 
			!viewInvoiceForm.phone || !viewInvoiceForm.bankname || !viewInvoiceForm.bankaccount) {
			ElMessage.error('请填写完整的发票信息');
			return;
		}
		
		invoiceApi.save({
			orderid: editingOrderId.value,
			company: viewInvoiceForm.companyname,
			invoice: viewInvoiceForm.taxid,
			address: viewInvoiceForm.address,
			phone: viewInvoiceForm.phone,
			bank: viewInvoiceForm.bankname,
			cardNo: viewInvoiceForm.bankaccount,
			remark: viewInvoiceForm.remark,
		}).then(res => {
			ElMessage.success('发票信息修改成功');
			isEditing.value = false;
			loadOrderList();
		}).catch(() => {
			ElMessage.error('修改失败');
		});
	}
	
	function cancelEdit() {
		isEditing.value = false;
		viewInvoiceInfo({ id: editingOrderId.value });
	}

	function downloadInvoiceFromView() {
		if (!viewInvoiceForm.location) {
			ElMessage.error('暂无发票文件');
			return;
		}
		window.open(viewInvoiceForm.location, '_blank');
	}
	
	defineExpose({
		show,
		viptag,
	})
</script>

<style scoped>
	.fontbig{
		font-size:24px;
	}
	.text-gray{
		color:#999;
	}
	.icon-wrap .el-icon{
		background-color: #fff9f4;
		width:32px;
		height: 32px;
		border-radius: 16px;
		font-size: 16px;
		color:#ff7315;
		font-weight: 700;
		margin-bottom:8px;
		margin-top:16px;
	}
	
	.quota-card {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 16px;
		padding: 24px;
		box-shadow: 0 8px 24px rgba(102, 126, 234, 0.25);
		margin-top: 16px;
	}
	
	.quota-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12px;
	}
	
	.quota-expiration {
		display: flex;
		justify-content: flex-end;
		align-items: center;
		gap: 8px;
		margin-bottom: 20px;
	}
	
	.expiration-label {
		font-size: 12px;
		color: rgba(255, 255, 255, 0.7);
	}
	
	.expiration-value {
		font-size: 14px;
		font-weight: 500;
		color: #ffffff;
		background: rgba(255, 255, 255, 0.15);
		padding: 4px 12px;
		border-radius: 12px;
	}
	
	.quota-title {
		font-size: 18px;
		font-weight: 600;
		color: #ffffff;
	}
	
	.quota-status {
		padding: 6px 12px;
		border-radius: 20px;
		font-size: 12px;
		font-weight: 500;
		background: rgba(255, 255, 255, 0.2);
		color: #ffffff;
		backdrop-filter: blur(10px);
	}
	
	.quota-numbers {
		display: flex;
		justify-content: space-around;
		margin-bottom: 24px;
	}
	
	.quota-number-item {
		text-align: center;
		flex: 1;
	}
	
	.quota-label {
		font-size: 12px;
		color: rgba(255, 255, 255, 0.7);
		margin-bottom: 8px;
	}
	
	.quota-value {
		font-size: 24px;
		font-weight: 700;
		color: #ffffff;
	}
	
	.quota-divider {
		width: 1px;
		background: rgba(255, 255, 255, 0.2);
		margin: 0 16px;
	}
	
	.quota-progress {
		background: rgba(255, 255, 255, 0.1);
		border-radius: 12px;
		padding: 16px;
	}
	
	.progress-info {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12px;
	}
	
	.progress-label {
		font-size: 12px;
		color: rgba(255, 255, 255, 0.8);
	}
	
	.progress-percent {
		font-size: 14px;
		font-weight: 600;
		color: #ffffff;
	}
	
	:deep(.el-progress-bar__outer) {
		background: rgba(255, 255, 255, 0.2);
		border-radius: 6px;
	}
	
	:deep(.el-progress-bar__inner) {
		border-radius: 6px;
		transition: all 0.3s ease;
	}
	
	.order-section {
		margin-top: 24px;
		border-radius: 12px;
		padding: 20px;
		box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
	}
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16px;
	}
	
	.section-title {
		font-size: 16px;
		font-weight: 600;
		color: #333;
		margin: 0;
	}
	
	.section-subtitle {
		font-size: 12px;
		color: #999;
	}
	
	.status-success {
		color: #67c23a;
		background: #f0f9eb;
		padding: 4px 12px;
		border-radius: 12px;
		font-size: 12px;
	}
	
	.status-warning {
		color: #e6a23c;
		background: #fdf6ec;
		padding: 4px 12px;
		border-radius: 12px;
		font-size: 12px;
	}
	
	.status-error {
		color: #f56c6c;
		background: #fef0f0;
		padding: 4px 12px;
		border-radius: 12px;
		font-size: 12px;
	}
	
	.status-default {
		color: #999;
		background: #f5f5f5;
		padding: 4px 12px;
		border-radius: 12px;
		font-size: 12px;
	}
	
	.text-orange {
		color: #e6a23c;
		font-size: 12px;
	}
	
	.text-green {
		color: #67c23a;
	}

	.text-blue {
		color: #409eff;
	}
</style>