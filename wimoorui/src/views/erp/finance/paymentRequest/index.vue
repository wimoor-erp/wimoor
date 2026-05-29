	<template>
		<div class="main-sty con-header"> 
			<el-tabs v-model="activeName"  @tab-change="handleQuery">
			  <el-tab-pane v-for="item in orderState"  :label="item.label" :name="item.name" :key="item.name">
				  <template #label>
					  {{item.label}}
					  <span class="text-orange">{{item.length}}</span>
				  </template>
			  </el-tab-pane>
			</el-tabs>
			<el-row>
			 <el-space>
			 <Supplier ref="supplierRef" @change="changeSupplier"  />
			 <el-select class="width120" v-model="queryParam.paymethod" placeholder="支付方式" @change="handleQuery">
				 <el-option  v-for="item in payMethodList"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
			 </el-select>
			<!-- <el-select placeholder="请款类型">
					 <el-option label="采购货款"></el-option>
					 <el-option label="运费"></el-option>
			 </el-select> -->
			 <div class="date-picker-group">
			 	<el-select v-model="dateType">
			 		<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
			 	</el-select>
			 	<Datepicker ref="datepickers" :shortIndex="3" @changedate="changedate" />
			 </div>
			   <el-input  v-model="searchKeywords" placeholder="请输入" @input="handleQuery" class="input-with-select" >
			      <template #prepend>
			        <el-select v-model="searchtype" @change="handleQuery" style="width:100px">
			          <el-option label="请款单号" value="order"></el-option>
			          <el-option label="关联单号" value="purchase"></el-option>
			        </el-select>
			      </template>
			      <template #append>
			        <el-button @click="handleQuery">
			           <el-icon style="font-size: 16px;align-itmes:center">
			            <search />
			         </el-icon>
			        </el-button>
			      </template>
			    </el-input>
			   <el-popover   v-model:visible="moreSearchVisible" :width="400" trigger="click">
			         <template #reference>
			           <el-button  :type="filterBtn" plain title='高级筛选'  class='ic-btn'>
			           <i>
			           <svg width="16" height="16" fill="none" viewBox="0 0 48 48"  xmlns="http://www.w3.org/2000/svg">
			           	<path d="M6 9L20.4 25.8178V38.4444L27.6 42V25.8178L42 9H6Z" fill="none" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>
			            </i>
			           </el-button>
			         </template>
					  <el-form :model="filterForm"  ref="formRef" label-width="80px">
					  <el-form-item label="备注" prop="remarks">
			                <el-input type="textarea" 
											  v-model="filterForm.remark"
											  placeholder="请输入备注..."
											  ></el-input>
					     </el-form-item>
						  <el-form-item>
						       <el-button type="primary" @click="handleQuery">搜索</el-button>
						       <el-button @click="cancelFilter">取消</el-button>
						     </el-form-item>
						</el-form>
			       </el-popover>
			    <el-button @click.stop="clearSearch">重置</el-button>
			  </el-space>
			  </el-row>
			   <!--功能区域 -->
			  <el-row>
			   <el-space >
				 <el-button v-if="activeName==0 && activeName!=''" type="primary" class="im-but-one" @click="approveAll">
				   <span>审核通过</span>
				 </el-button>
				 <el-button v-if="activeName==0 && activeName!=''" type="primary" class="im-but-one" @click="approveReturnAll">
				   <span>审核驳回</span>
				 </el-button>
				 <el-button v-if="activeName==1 && activeName!=''" type="primary" class="im-but-one" @click="paymentAll">
				   <span>付款</span>
				 </el-button>
				 <el-button v-if="activeName==1 && activeName!=''" type="primary" class="im-but-one" @click="payreturnAll">
				   <span>作废</span>
				 </el-button>
				  <el-button v-if="(activeName==1 || activeName==0)&& activeName!=''" @click="BatchPayType">修改付款方式</el-button>
			   </el-space>
			   <div class='rt-btn-group'>
				   <el-button class='ic-btn'  title='列配置'>
					  <setting-two theme="outline" size="16"  :strokeWidth="3"/>
				   </el-button>
					<el-button   class='ic-btn' title='帮助文档'>
					 <help theme="outline" size="16" :strokeWidth="3"/>
				   </el-button>
			   </div>
			</el-row>
			<Table ref="tableRef" @getCheckRow="getCheckRow"/>
		</div>
		 <el-dialog
		    v-model="dialogVisible"
		    title="批量修改付款方式"
		    width="360px"
		    :before-close="handleClose"
		  >
		  <el-form>
		  <el-form-item label="支付方式">
		  	<el-select placeholder="支付方式" v-model="paytypeAll" @change="loadPaymentAccount(paytypeAll)">
		  					<el-option  v-for="item in payMethodAllList"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
		  	</el-select>
		  </el-form-item>
		  <el-form-item label="支付账号" v-if="payAccList&&payAccList.length>1">
		  	<el-select  v-model="payacc">
		  		 <el-option  v-for="item in payAccList"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
		  	</el-select>
		  </el-form-item>
		  </el-form>
		    <template #footer>
		        <el-button @click="dialogVisible = false">取消</el-button>
		        <el-button type="primary" @click="updatePay">
		          确认
		        </el-button>
		    </template>
		  </el-dialog>
		  
		  <el-dialog
		     v-model="paydialogVisible"
		     title="付款详情"
		     width="1200px"
		     :before-close="handleClose"
		   >
		    
				<el-table :data="tableData" show-summary style="width: 100%" max-height="500">
					 <el-table-column prop="number" label="图片">
						  <template #default="scope">
						 <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
						 <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
						  </template>
					 </el-table-column>
					 <el-table-column prop="number" label="产品名称"  show-overflow-tooltip >
						 <template #default="scope">
								   <div>{{scope.row.mname}}</div>
								   <p class="sku">{{scope.row.sku}}</p>
						 </template>
					 </el-table-column> 
					 <el-table-column prop="number" label="请款单号"   />
					 <el-table-column prop="purnumber" label="采购单号"   />
					 <el-table-column prop="creatorname" label="请款人"   />
					<el-table-column prop="number" label="请款时间"  >
						<template #default="scope">
								{{dateTimesFormat(scope.row.createtime)}}				 
						</template>
					</el-table-column> 
					 <el-table-column prop="number" label="费用项"  >
						 <template #default="scope">
						 		{{scope.row.project}}				 
						 </template>
					 </el-table-column> 
					  <el-table-column prop="payprice" label="金额"  />
					  <el-table-column prop="paymethod" label="支付方式"  />
				</el-table>
		     
		     <template #footer>
		         <el-button @click="paydialogVisible = false">取消</el-button>
		         <el-button type="primary" @click="confirmPay">
		           确认
		         </el-button>
		     </template>
		   </el-dialog>
	</template>
	<script>
	    export default{ name:"请款单" };
	</script>
	<script setup>
		import { ref,reactive,onMounted,toRefs} from 'vue'
		import Table from"./components/table.vue"
		import Supplier from '@/components/header/supplier.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import {Search,ArrowDown,} from '@element-plus/icons-vue'
		import NullTransform from"@/utils/null-transform";
		import {useRouter } from 'vue-router';
		import {dateFormat,dateTimesFormat} from '@/utils/index.js';
		import { ElMessage, ElMessageBox} from 'element-plus'
		import {Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
		import faccountApi from '@/api/erp/finances/faccountApi.js';
		import purchaseFinlistApi from '@/api/erp/purchase/finance/listApi.js';
		const supplierRef=ref();
		const router = useRouter();
		const ponumber = router.currentRoute.value.query.number;
		const tableRef=ref();
		const formRef=ref()
		let state=reactive({
			dialogVisible:false,
			paydialogVisible:false,
			moreSearchVisible:false,
			activeName:'',
			filterBtn:'',
			filterForm:{
				remark:'',
			},
			searchtype:'order',
			searchKeywords:'',
			queryParam:{},
			orderState:[
				{label:'全部订单',name:'',},
				{label:'待审核',name:'0',},
				{label:'待付款',name:'1',},
				{label:'已完成',name:'2',},
				{label:'已驳回',name:'3',},
			],
			dateType:'0',
			dateOptions:[
				{label:'申请日期',value:'0',},
				{label:'预计付款日期',value:'1',},
				{label:'实际付款日期',value:'2',},
			],
			selectrows:[],
			isload:true,
			payMethodList:[],
			paytypeAll:null,
			payMethodAllList:[],
			tableData:[],
			payAccList:[],
			payacc:'',
		})
		const{
			filterBtn,
			filterForm,
			moreSearchVisible,
			activeName,orderState,
		    dateOptions,dateType,
			dialogVisible,
			selectrows,
			isload,
			queryParam,
			searchtype,
			searchKeywords,
			payMethodList,
			paytypeAll,
			payMethodAllList,
			paydialogVisible,
			tableData,
			payAccList,
			payacc,
		}=toRefs(state)
		state.searchKeywords=ponumber;
		state.searchtype="purchase";
		function cancelFilter(){
			state.moreSearchVisible = false
			/* 重置表单 */
			formRef.value.resetFields()
		}
		/* 批量修改付款方式 */
		const getCheckRow = function(selection){
			state.selectrows = selection;
		}
		function approveAll(){
			if(state.selectrows && state.selectrows.length>0){
				var idlist=[];
				state.selectrows.forEach(function(item){
					idlist.push(item.id); 
				});
				purchaseFinlistApi.approve(idlist).then((res)=>{
					if(res.data){
						ElMessage.success('批量审核通过成功！');
						handleQuery();
					}
				});
				
			}else{
					ElMessage.error('请至少选择一个请款单据！')
			}
		}
		function approveReturnAll(){
			if(state.selectrows && state.selectrows.length>0){
				var idlist=[];
				state.selectrows.forEach(function(item){
					idlist.push(item.id); 
				});
				purchaseFinlistApi.approveReturn(idlist).then((res)=>{
					if(res.data){
						ElMessage.success('批量审核驳回成功！');
						handleQuery();
					}
				});
				
			}else{
					ElMessage.error('请至少选择一个请款单据！')
			}
		}
		
		function BatchPayType(){
			if(state.selectrows.length>0){
				state.dialogVisible = true;
				faccountApi.getPaymentMethod().then((res)=>{
					if(res.data && res.data.length>0){
						state.payMethodAllList=res.data;
						state.paytypeAll=state.payMethodAllList[0].id;
						loadPaymentAccount(state.paytypeAll);
					}
				});
			}else{
					ElMessage.error('请至少选择一个请款单据！');
			}
		}
		function loadPaymentAccount(paymethod){
			faccountApi.getPaymentAccount({"paymethod":paymethod}).then((res)=>{
				if(res.data && res.data.length>0){
					state.payAccList=res.data;
					var defaultid="";
					state.payAccList.forEach(item=>{
						if(item.isdefault){
							defaultid=item.id;
						}
					});
					state.payacc=defaultid;
				}else{
					state.payAccList=[];
					state.payacc="";
				}
			});
		}
		function changeSupplier(val){
			state.queryParam.supplierid=val;
			if(state.isload==true){
			   loadPaymentMethod();
			}
			handleQuery();
		}
		function clearSearch(){
			supplierRef.value.reset();
			state.queryParam.supplierid="";
			state.searchKeywords="";
			state.queryParam.paymethod="";
			handleQuery();
		}
		function updatePay(){
			if(state.paytypeAll){
				var idlist=[];
				var data={};
					data.paymethod=state.paytypeAll;
					data.payacc=state.payacc;
				state.selectrows.forEach(function(item){
					idlist.push(item.id); 
				});
				data.entryList=idlist;
				purchaseFinlistApi.updatePay(data).then((res)=>{
					if(res.data){
						state.dialogVisible = false;
						ElMessage.success('批量修改成功！');
						handleQuery();
					}
				});
			}else{
				ElMessage.error('支付方式不能为空！');
			}
		}
		//日期改变
		function changedate(dates){
			state.queryParam.fromDate=dates.start;
			state.queryParam.toDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		}
		function handleQuery(){
			if(state.filterForm.remark){
				state.queryParam.remark=state.filterForm.remark;
			}
			state.queryParam.status=state.activeName;
			state.queryParam.search=state.searchKeywords;
			state.queryParam.searchtype=state.searchtype;
			tableRef.value.load(state.queryParam);
			if(state.isload==true){
				state.isload=false;
			}
		}
		function loadPaymentMethod(){
			faccountApi.getPaymentMethod().then((res)=>{
				if(res.data && res.data.length>0){
					res.data.push({"id":"","name":"全部"});
					state.payMethodList=res.data;
					state.queryParam.paymethod="";
				}
			});
		}
		function paymentAll(){
			if(state.selectrows && state.selectrows.length>0){
				state.paydialogVisible=true;
				//ajax 请求数据
				var idlist=[];
				state.selectrows.forEach(function(item){
					idlist.push(item.id); 
				});
				purchaseFinlistApi.getData(idlist).then((res)=>{
					if(res.data){
						state.tableData=res.data;
					}
				});
			}else{
				ElMessage.error('请至少选择一个请款单据！');
			}
		}
		function payreturnAll(){
			if(state.selectrows && state.selectrows.length>0){
				//ajax 请求数据
				ElMessageBox.confirm(
					'请确认是否作废单据？',
					{
					  confirmButtonText: '确认',
					  cancelButtonText: '取消',
					  type: 'warning',
					  callback:(action)=>{
						 if(action=="confirm"){
							  var idlist=[];
							  state.selectrows.forEach(function(item){
							  	idlist.push(item.id); 
							  });
							  purchaseFinlistApi.approveReturn(idlist).then((res)=>{
							  	if(res.data){
							  		ElMessage.success('批量作废成功！');
							  		handleQuery();
							  	}
							  });
						 }
					  }
					}
				  )
			}else{
				ElMessage.error('请至少选择一个请款单据！')
			}
		}
		function confirmPay(){
			//确认付款
			var idlist=[];
			state.selectrows.forEach(function(item){
				idlist.push(item.id); 
			});
			purchaseFinlistApi.paymentForm(idlist).then((res)=>{
				if(res.data){
					state.paydialogVisible=false;
					ElMessage.success('批量付款成功！');
					handleQuery();
				}
			});
		}
	</script>
	
	<style scoped="scoped">
		.width120{
			width:120px;
		}
		.text-orange{
			color:var(--el-color-primary);
			font-size:12px;
			font-weight: 700;
		}
		.img-40{width: 40px;
		height: 40px;flex: none;
		margin-right: 8px;}
	</style>
	



