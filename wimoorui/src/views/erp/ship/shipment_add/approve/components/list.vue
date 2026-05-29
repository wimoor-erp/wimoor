<template>
	<div>
		<div class='font-base list-from-title' >单据信息</div>
		<el-space direction="vertical" alignment="left" :size="16">
			<div class="item-list-from" >
				<div class="label">状态</div>
				<div class="value">
					<el-tag v-if="planData.auditstatus==1" type="warning">待审核</el-tag>
					<el-tag v-if="planData.auditstatus==2" type="info">已退回</el-tag>
					<div v-if="planData.auditstatus==3">
						<el-tag v-if="planData.iserror" type="danger">审核失败</el-tag>
						<el-tag v-else type="success">已确认</el-tag>
					</div>
					
				</div>
			</div>
			<div class="item-list-from" >
				<div class="label">店铺</div>
				<div class="value">{{planData.groupname}}</div>
			</div>
			<div class="item-list-from" >
				<div class="label">申请人</div>
				<div class="value">{{planData.creatorname}}</div>
			</div>
			<div class="item-list-from group-item-last" >
				<div class="label">申请日期</div>
				<div class="value">{{planData.createdate2}}</div>
			</div>
			<div class="item-list-from" >
				<div class="label">发货仓库</div>
				<div class="value">{{planData.warename}}</div>
			</div>
			<div class="item-list-from" >
				<div class="label">发货地址</div>
				<div class="value">
					<span v-if="planData['address']">
						 {{planData.address.addressline1}},{{planData.address.addressline2}}
						 ,{{planData.address.countrycode}}
					</span>
					<span v-else  class="text-red">
						暂无地址！
					</span>
				</div>
			</div>
			<div class="item-list-from" >
				<div class="label">收货站点</div>
				<div class="value">{{planData.destination}}</div>
			</div>
			<div class="item-list-from group-item-last" >
				<div class="label">包装类型</div>
				<div class="value">
					<span v-if="planData.areCasesRequired==false">混装发货</span>
					<span v-else><el-tag type="success">原厂包装发货</el-tag></span>
				</div>
			</div>
			<div class="item-list-from group-item-last" >
				<div class="label">备注</div>
				<div class="value">
					 {{remark}}
					 <el-link  class="icon-text-center" :underline="false" @click="editRemark">
						 <el-icon ><Edit/></el-icon>修改
					 </el-link>
				</div>
			</div>
		</el-space>
	</div>
	<el-dialog v-model="remarkVisible" title="备注" destroy-on-close='true' width="560px" @close='closeDialog'>
		 <div class="from-body">
			<el-input
			    v-model="remark"
			    maxlength="100"
			    placeholder="请输入内容"
			    show-word-limit
				rows="3"
			    type="textarea"
			  />
		</div>	
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="remarkVisible = false">取消</el-button>
				<el-button type="primary" @click="updateRemark" >提交</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted } from 'vue'
	import {} from'@icon-park/vue-next';
	import {Edit} from '@element-plus/icons-vue'
	import shipmentplanApi from '@/api/erp/ship/shipmentplanApi.js';
	import { useRoute,useRouter } from 'vue-router'
	import {ElMessage } from 'element-plus'
 
			let router = useRouter();
			let remarkVisible=ref(false);
			let planData=ref({});
			const planid=router.currentRoute.value.query.id;
			let remark=ref();
			function editRemark(){
				remarkVisible.value = true;
			}
			function updateRemark(){
				shipmentplanApi.updatePlanRemark({"inboundplanid":planData.value.id,"remark":remark.value}).then((res)=>{
					if(res.data && res.data=="ok"){
						ElMessage.success('修改成功！');
						 remarkVisible.value = false;
					}else{
						ElMessage.error( '修改失败！' );
					}
				});
			}
			 defineExpose({editRemark,remarkVisible,planData,remark,updateRemark });
 
</script>

<style>
	.group-item-last{
		margin-bottom:24px;
	}
</style>
