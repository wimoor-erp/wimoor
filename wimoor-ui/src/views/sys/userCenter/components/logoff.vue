<template>
	<div class="logoff-container">
		<!-- 外部警示卡片 -->
		<div class="outer-warning-card">
			<div class="warning-header">
				<el-icon class="icon-warning-big"><Warning /></el-icon>
				<div class="warning-info">
					<h3 class="warning-title">⚠️ 账号注销</h3>
					<p class="warning-desc">此操作将永久删除您的账号及所有相关数据</p>
				</div>
			</div>
			
			<div class="info-cards">
				<!-- 紧急联系 -->
				<div class="info-card emergency">
					<el-icon class="info-icon"><Phone /></el-icon>
					<div class="info-content">
						<span class="info-title">紧急联系</span>
						<span class="info-text">如遇意外操作，请立即联系管理员</span>
					</div>
				</div>
				
				<!-- 数据安全 -->
				<div class="info-card security">
					<el-icon class="info-icon"><Help /></el-icon>
					<div class="info-content">
						<span class="info-title">数据安全保障</span>
						<span class="info-text">解绑后数据将被系统自动清洗，确保不会泄露</span>
					</div>
				</div>
			</div>
			
			<!-- 大号注销按钮 -->
			<div class="button-container">
				<el-button 
					type="danger" 
					size="large" 
					class="logoff-btn"
					@click="logout"
				>
					<el-icon class="btn-icon"><Delete /></el-icon>
					确认注销账号
				</el-button>
			</div>
		</div>
		
		<!-- 注销确认对话框 -->
		<el-dialog 
			v-model="bindVisible" 
			title="确认注销账号" 
			destroy-on-close='true' 
			width="550px"
			:before-close="handleClose"
			class="logoff-dialog"
		>
			<div class="dialog-warning">
				<el-icon class="dialog-warning-icon"><Warning /></el-icon>
				<h3 class="dialog-title">此操作将永久删除您的账号</h3>
				<p class="dialog-desc">
					<span style="color:#f56c6c;font-weight:bold;">请注意：</span>
					一旦确认注销，您的账号及所有相关数据将被<span style="color:#f56c6c;font-weight:bold;">永久删除</span>，且<span style="color:#f56c6c;font-weight:bold;">无法恢复</span>。
				</p>
			</div>
			
			<!-- 删除内容清单 -->
			<div class="delete-list">
				<h4>注销后将删除以下内容：</h4>
				<ul>
					<li><el-icon class="icon-item"><User /></el-icon> 您的账号信息及所有个人资料</li>
					<li><el-icon class="icon-item"><Shop /></el-icon> 已绑定的店铺信息</li>
					<li><el-icon class="icon-item"><Document /></el-icon> 所有订单数据及交易记录</li>
					<li><el-icon class="icon-item"><Setting /></el-icon> 系统配置及自定义设置</li>
					<li><el-icon class="icon-item"><DataBoard /></el-icon> 所有与账号关联的数据</li>
				</ul>
			</div>
			
			<!-- 确认复选框 -->
			<div class="confirm-box">
				<el-checkbox v-model="confirmDelete" disabled>
					我已阅读并理解以上内容，确认注销账号（此操作无法撤销）
				</el-checkbox>
				<el-button 
					type="text" 
					@click="confirmDelete = !confirmDelete"
					class="enable-checkbox"
				>点击此处确认已阅读</el-button>
			</div>
			
			<template #footer>
				<span class="dialog-footer">
					<el-button size="large" @click="bindVisible = false">取消</el-button>
					<el-button 
						type="danger" 
						size="large"
						@click="agreelogout"
						:disabled="!confirmDelete"
						class="confirm-btn"
					>
						<el-icon><Delete /></el-icon>
						确认注销
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script>
	import userApi from '@/api/sys/admin/userApi.js';
	import { ref,reactive,onMounted ,h} from 'vue';
	import { ElMessage } from 'element-plus';
	import { 
		Warning, 
		User, 
		Shop, 
		Document, 
		Setting, 
		DataBoard, 
		Phone, 
		Help, 
		Delete 
	} from '@element-plus/icons-vue';
	
	export default{
		setup(){
			let bindVisible = ref(false);
			let confirmDelete = ref(false);
			
			function handleClose(done) {
				if (confirmDelete.value) {
					ElMessage.warning('请确认是否真的要取消注销操作');
				}
				done();
			}
			
			function agreelogout(){
				userApi.unbindAccount().then((res)=>{
					ElMessage.success('注销成功！感谢您使用我们的服务。');
					localStorage.removeItem("jsessionid");
					localStorage.removeItem("logintype");
					let authserver = localStorage.getItem("authserver");
					location = authserver + "/logout"; 
				})
			}
			
			function logout(){
				bindVisible.value = true;
				confirmDelete.value = false;
			}
			
			return{
				bindVisible,
				confirmDelete,
				agreelogout,
				logout,
				Warning,
				User,
				Shop,
				Document,
				Setting,
				DataBoard,
				Phone,
				Help,
				Delete
			}
		}
	}
</script>

<style scoped>
.logoff-container {
	padding: 24px;
}

/* 外部警示卡片 */
.outer-warning-card {
	background: linear-gradient(135deg, #fff5f5 0%, #fff7e6 100%);
	border-radius: 16px;
	padding: 24px;
	border: 1px solid #ffe5cc;
	box-shadow: 0 4px 20px rgba(245, 108, 108, 0.1);
}

.warning-header {
	display: flex;
	align-items: center;
	margin-bottom: 20px;
	padding-bottom: 20px;
	border-bottom: 1px dashed #ffe5cc;
}

.icon-warning-big {
	font-size: 40px;
	color: #f56c6c;
	margin-right: 16px;
	animation: pulse 2s infinite;
}

@keyframes pulse {
	0%, 100% {
		transform: scale(1);
		opacity: 1;
	}
	50% {
		transform: scale(1.05);
		opacity: 0.8;
	}
}

.warning-info {
	flex: 1;
}

.warning-title {
	font-size: 18px;
	color: #f56c6c;
	margin: 0 0 8px 0;
	font-weight: 600;
}

.warning-desc {
	font-size: 14px;
	color: #666;
	margin: 0;
}

/* 信息卡片 */
.info-cards {
	display: flex;
	gap: 16px;
	margin-bottom: 24px;
}

.info-card {
	flex: 1;
	display: flex;
	align-items: center;
	padding: 16px;
	border-radius: 12px;
}

.info-card.emergency {
	background: linear-gradient(135deg, #fff7e6 0%, #fff5f5 100%);
	border: 1px solid #ffe5cc;
}

.info-card.security {
	background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%);
	border: 1px solid #b5f5ec;
}

.info-icon {
	font-size: 24px;
	margin-right: 12px;
}

.info-card.emergency .info-icon {
	color: #e6a23c;
}

.info-card.security .info-icon {
	color: #10b981;
}

.info-content {
	display: flex;
	flex-direction: column;
}

.info-title {
	font-size: 14px;
	font-weight: 600;
	margin-bottom: 4px;
}

.info-card.emergency .info-title {
	color: #e6a23c;
}

.info-card.security .info-title {
	color: #10b981;
}

.info-text {
	font-size: 12px;
	color: #666;
}

/* 按钮容器 */
.button-container {
	display: flex;
	justify-content: center;
}

.logoff-btn {
	width: 240px;
	height: 52px;
	font-size: 16px;
	font-weight: 600;
	background: linear-gradient(135deg, #f56c6c 0%, #ee4d4d 100%);
	border: none;
	border-radius: 26px;
	box-shadow: 0 4px 12px rgba(245, 108, 108, 0.3);
	transition: all 0.3s ease;
}

.logoff-btn:hover {
	transform: translateY(-2px);
	box-shadow: 0 6px 16px rgba(245, 108, 108, 0.4);
}

.btn-icon {
	font-size: 18px;
	margin-right: 8px;
}

/* 对话框样式 */
.logoff-dialog :deep(.el-dialog__header) {
	border-bottom: 1px solid #f0f0f0;
}

.logoff-dialog :deep(.el-dialog__title) {
	font-size: 18px;
	font-weight: 600;
	color: #333;
}

.dialog-warning {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20px;
	background: linear-gradient(135deg, rgba(245, 108, 108, 0.1) 0%, rgba(230, 162, 60, 0.1) 100%);
	border-radius: 12px;
	margin-bottom: 20px;
	text-align: center;
}

.dialog-warning-icon {
	font-size: 36px;
	color: #f56c6c;
	margin-bottom: 12px;
}

.dialog-title {
	font-size: 16px;
	color: #f56c6c;
	margin: 0 0 8px 0;
	font-weight: 600;
}

.dialog-desc {
	font-size: 14px;
	color: #666;
	line-height: 1.8;
	margin: 0;
}

/* 删除清单 */
.delete-list {
	margin-bottom: 20px;
	padding: 16px;
	background: #fafafa;
	border-radius: 8px;
}

.delete-list h4 {
	font-size: 14px;
	color: #333;
	margin: 0 0 12px 0;
	font-weight: 600;
}

.delete-list ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

.delete-list li {
	display: flex;
	align-items: center;
	padding: 8px 0;
	font-size: 13px;
	color: #666;
	border-bottom: 1px dashed #eee;
}

.delete-list li:last-child {
	border-bottom: none;
}

.icon-item {
	margin-right: 12px;
	color: #f56c6c;
	font-size: 14px;
}

/* 确认复选框 */
.confirm-box {
	padding: 16px;
	background: #fefefe;
	border: 2px dashed #f56c6c;
	border-radius: 8px;
	text-align: center;
}

.confirm-box :deep(.el-checkbox__input.is-disabled .el-checkbox__inner) {
	background: #fff;
	border-color: #d9d9d9;
}

.confirm-box :deep(.el-checkbox__input.is-disabled + .el-checkbox__label) {
	color: #999;
}

.enable-checkbox {
	display: block;
	margin-top: 12px;
	font-size: 13px;
	color: #f56c6c;
	text-decoration: underline;
}

.enable-checkbox:hover {
	color: #f78989;
}

/* 底部按钮区域 */
.dialog-footer {
	display: flex;
	justify-content: flex-end;
	gap: 16px;
}

.confirm-btn {
	background: linear-gradient(135deg, #f56c6c 0%, #ee4d4d 100%);
	border: none;
}

.confirm-btn:disabled {
	background: #ccc !important;
	cursor: not-allowed;
}
</style>