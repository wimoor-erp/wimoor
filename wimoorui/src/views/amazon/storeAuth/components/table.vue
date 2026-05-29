<template>
	<div>
		<div class="con-header">
			<el-row>
				<el-button type="primary" @click="storeAuth">店铺授权</el-button>
				<div class='rt-btn-group'>
					<el-button type="primary" @click="advAuth">广告授权</el-button>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
				</div>
			</el-row>
			<el-row>
				<el-col :span="24">
				<el-table  class="only-table-head" border>
					<el-table-column label="店铺名称"></el-table-column>
					<el-table-column label="卖家ID" ></el-table-column>
					<el-table-column label="区域" width="90"></el-table-column>
					<el-table-column label="国家"></el-table-column>
					<el-table-column label="店铺授权" ></el-table-column>
					<el-table-column label="广告授权" ></el-table-column>
					<el-table-column label="授权日期" ></el-table-column>
					<el-table-column label="操作" width="200" ></el-table-column>
				</el-table>
				<el-row v-if="tableData.list.length<=0" class="storegroup">
				<el-col>
				    <el-card shadow="never" ><div class="font-extraSmall text-center ">暂无数据</div></el-card>
				</el-col>
				</el-row>
				<div class="storegroup" v-for="item in tableData.list">
					<el-table :show-header="false" :data="item.regionslist" border  :span-method="rowmerge">
						<el-table-column  >{{item.gname}}</el-table-column>
						<el-table-column prop="sellerid" ></el-table-column>
						<el-table-column prop="regionname"  width="90" ></el-table-column>
						<el-table-column  prop="countrysList">
							<template #default="scope">
								 <div class="country" v-for="sub in scope.row.countrysList">
									  <el-space>
									 <div class="country">
										 <el-image :src="imageFormat(sub)"></el-image>
										 <span> {{sub.countryname}}</span>
									</div>
									 <el-icon class="font-extraSmall pointer" size="large" @click.stop="updateStatus(scope.row.sellerid,sub.marketplaceid,'false')" v-if="sub.disable"><Hide /></el-icon>
									 <el-icon class="font-extraSmall pointer" size="large" @click.stop="updateStatus(scope.row.sellerid,sub.marketplaceid,'true')" v-else><View /></el-icon>
									</el-space>
								 </div>
							</template>
						</el-table-column>
						<el-table-column  prop="sellerauth">
							<template #default="scope">
								<el-space>
								<div>
								<el-tag v-if="scope.row.sellerauth=='2'"  type="info">未授权</el-tag>
								<el-tag v-else-if="scope.row.sellerauth=='0'"  title="请重新授权" type="danger">授权异常</el-tag>
								<el-tag v-else  type="success">已授权</el-tag>
								</div>
								<div>
								    <el-button  title="刷新站点" v-if="scope.row.authid" link type="primary" size="small" @click="refreshAuth(scope.row.authid)">刷新</el-button>
									<div style="margin-top:10px;">
									<el-button link type="primary"  @click="showTaskDialog(scope.row)">报表进度</el-button>
									</div>
								 </div>
								 </el-space>
							</template>	
						</el-table-column>
						<el-table-column  prop="adauth">
							<template #default="scope">
								<el-space>
								<div>
								<el-tag v-if="scope.row.adauth=='2'"  type="info">未授权</el-tag>
								<el-tag v-else-if="scope.row.adauth=='0'" title="请重新授权" type="danger">授权异常</el-tag>
								<el-tag v-else  type="success">已授权</el-tag>
								</div>
								<div style='margin-left:10px;'>
								<div>
									<el-button   v-if="scope.row.advid"  title="刷新站点" link type="primary" size="small" @click="refreshAdv(scope.row.advid)">刷新</el-button>
								</div>	
								<div style="margin-top:10px;">
									<el-button  
									v-if="scope.row.advid"  
									title="申请报告" link 
									type="primary" 
									size="small" 
									@click="showAdvReport(scope.row.advid)">
									   申请报告
									</el-button>
								</div>
								</div>
								</el-space>
							</template>	
						</el-table-column>
						<el-table-column  prop="time" >
							<template #default="scope">
								{{dateFormat(scope.row.time)}}
							</template>	
						</el-table-column>
						<el-table-column   width="200" >
							<template #default="scope">
								<div style="padding-left:20px">
								<div style="padding-bottom:20px">
									<el-button v-if="scope.row.sellerauth!='2'" @click="deleteByLogic(scope.row.sellerid)" type="danger">停用店铺授权</el-button>
								</div>
								<el-button v-if="scope.row.advid"  @click="disableAuth(scope.row.advid)" type="danger">停用广告授权</el-button>
								</div>
							</template>	
						</el-table-column>
					</el-table>
				</div>
				</el-col>
			</el-row> 
		</div> 
	</div>
	<AuthStore ref="authStoreRef" :storelist="storelist"></AuthStore>
	<AuthAdv ref="authAdvRef" :storelist="storelist"></AuthAdv>
	<TaskData ref="taskDataRef" ></TaskData>
	<AdvReport ref="advReportRef"></AdvReport>
</template>

<script setup>
import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
import {ref,reactive,onMounted,toRefs} from "vue";
import {Edit,ArrowDown,Delete,View,Hide} from '@element-plus/icons-vue';
import { ElDivider,ElMessageBox ,ElMessage } from 'element-plus'
import authApi from '@/api/amazon/auth/authApi.js';
import authAdvApi from '@/api/amazon/advertisement/auth/authApi.js';
import {dateFormat} from '@/utils/index.js';
import AuthStore from "./authstore.vue"
import AdvReport from "./advReport.vue"
import AuthAdv from "./authadv.vue"
import $require from '@/utils/system/require.js';
import TaskData from "./taskData.vue"
			let props = defineProps({storelist:[]});
			const { storelist,} = toRefs(props);
			const advReportRef=ref();
			const imageFormat=function(country){
				return $require('country/'+country.countrycode+'-flag-small.jpg');
			}
			const authStoreRef=ref();
			const authAdvRef=ref();
			const taskDataRef=ref();
			let tableData =reactive({list:[]})
			onMounted(()=>{
				loadauthData()
			})
			function showTaskDialog(row){
				 taskDataRef.value.show(row);
			}
			function storeAdvAuth(){
				
			}
			function updateStatus(sellerid,marketplaceid,status){
				authApi.statusChange({"sellerid":sellerid,"marketplaceid":marketplaceid,"status":status}).then((res)=>{
					ElMessage.success("操作成功！");
					loadauthData();
				});
			}
			function showAdvReport(advauthid){
				advReportRef.value.show(advauthid)
			}
			function loadauthData(){
				authApi.getBindSeller().then((res)=>{
					 tableData.list = res.data 
				})
			}
			function rowmerge({row,column,rowIndex,columnIndex}){
				if(columnIndex === 0){
					if(rowIndex % 20 === 0){
						return [20,1]
						}else{
                   return [0, 0]
                   }
				}	
			}
			function storeAuth(){
				authStoreRef.value.show();
			}
			function advAuth(){
				authAdvRef.value.show();
			}
			function refreshAdv(id){
				authAdvApi.captureProfileById({"id":id}).then((res)=>{
					ElMessage.success("刷新成功！");
				});
			}
			function refreshAuth(id){
				authApi.refreshMarketByAuth({"authid":id}).then((res)=>{
					ElMessage.success("刷新成功！");
				});
			}
			function disableAuth(id){
				ElMessageBox.confirm(
									    '是否解除绑定，解除后会造成店铺数据缺失！',
									    {
									      confirmButtonText: '确定',
									      cancelButtonText: '取消',
									      type: 'warning',
										  callback:(action)=>{
											 if(action=="confirm"){
												authAdvApi.disableAuth({"id":id}).then((res)=>{
													if(res.data&&res.data.code===200){
														    ElMessage.success(res.data.msg);
													}else{
														if(res.data&&res.data.msg){
															ElMessage.error(res.msg);
														}else{
															ElMessage.error("广告解除绑定操作失败，请联系管理员");
														}
													}
													loadauthData();
												})
											 }
										  }
									    }
									  )
			}
			function deleteByLogic(sellerid){
				ElMessageBox.confirm(
									    '是否解除绑定，解除后会造成店铺数据缺失！',
									    {
									      confirmButtonText: '确定',
									      cancelButtonText: '取消',
									      type: 'warning',
										  callback:(action)=>{
											 if(action=="confirm"){
												authApi.deleteByLogic({"sellerid":sellerid}).then((res)=>{
													if(res.data.indexOf('成功')>=0){
														ElMessage.success(res.data);
													}else{
														ElMessage.error(res.data);
													}
													loadauthData();
												})
											 }
										  }
									    }
									  )
				
			}
 
</script>

<style>
	.only-table-head .el-scrollbar{
		display: none;
	}
	.storegroup{
		margin-top:8px;
		width:100%;
	}
	.font-400{
		font-weight:400;
	}
	.country{
		display: flex;
		align-items: center;
	}
	.country .el-image{
		margin-right:8px;
	}
   .form-width-fill{margin-top:16px;}
	.storegroup .el-table tbody tr:hover > td {
		background-color: transparent !important
	}
</style>
