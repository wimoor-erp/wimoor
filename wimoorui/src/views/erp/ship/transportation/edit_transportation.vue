<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>物流渠道</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
						 <el-form :model="form"  label-width="120px">
							 <el-form-item label="物流承运公司"   >
							 	<el-input class="in-wi-24" v-model="form.name" placeholder="请输入"> </el-input>
							 </el-form-item>
						 </el-form>
						 <el-form :model="form"  label-width="120px">
							 <el-form-item label="公司简称"  >
							 	<el-input class="in-wi-24" v-model="form.simplename" placeholder="请输入"> </el-input>
							 </el-form-item>
						 </el-form>
						 <el-form :model="form"  label-width="120px">
							 <el-form-item label="物流接口"  >
								 <el-space>
							 	<el-select class="in-wi-24" v-model="form.api" placeholder="请选择">
							 		<el-option  v-for="item in apiList.list"   :key="item.id"  :label="item.name" :value="item.id"    />
							 	</el-select>
								<el-icon class="font-extraSmall pointer" @click="handleRouteToApi"><Setting/></el-icon>
								</el-space>
							 </el-form-item>
						 </el-form>
						 <!-- <el-form :model="form"  label-width="120px">
							 <el-form-item label="授权码"  >
							 	<el-input class="in-wi-24" v-model="form.accessToken" placeholder="Access Token"> </el-input>
							 </el-form-item>
						 </el-form> -->
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >渠道列表 </h5>
						   </div>
						   <el-button @click="addTransportation" type="primary">添加</el-button>
						 </div>
						 <el-table border :data="tranTableData.list">
							 <el-table-column label="区域" width="120"> 
							   <template #default="scope">
								   <el-select style="width:80px" v-model="scope.row.marketplaceid" :disabled="scope.row.id?true:false">
									   <el-option v-for="item in marketList.list" :label="item.name" :value="item.marketplaceid"></el-option>
								   </el-select>
							   </template>
							 </el-table-column>
							 <el-table-column label="分区"  width="120" :prop="subarea">
								 <template #default="scope">
									 <el-input v-model="scope.row.subarea"></el-input>
								 </template>
							 </el-table-column>
							 <el-table-column label="运输方式" width="160">
								 <template #header>
									 <el-link :underline="false" @click="editModeoftransport">
										  <span class="font-12">运输方式 </span>
										<el-icon><Edit></Edit></el-icon>
									 </el-link>
								 </template>
								  <template #default="scope">
									  <el-select style="width:120px" v-model="scope.row.transtype">
								        <el-option v-for="item in transTypeAllList.list" :label="item.name" :value="item.id"></el-option>
									  </el-select>
								  </template>  
							 </el-table-column>
							 <el-table-column label="渠道类型" width="210">
								 <template #header>
									 <el-link :underline="false" @click="editChannel">
										  <span class="font-12">渠道类型</span>
										<el-icon><Edit></Edit></el-icon>
									 </el-link>
								 </template>
								  <template #default="scope">
									  <el-select style="width:180px"  v-model="scope.row.channel">
								        <el-option v-for="item in channelAllList.list" :label="item.name" :value="item.id"></el-option>
									  </el-select>
								  </template>  
							 </el-table-column>
							 <el-table-column label="供应商渠道名称" width="240">
								 <template #default="scope">
								 	<el-input v-model="scope.row.channame"></el-input>
								 </template>
							 </el-table-column>
							 <el-table-column label="渠道时效(天)" width="100">
								 <template #default="scope">
								 	<el-input v-model="scope.row.pretime" @input="scope.row.pretime=CheckInputInt(scope.row.pretime)"></el-input>
								 </template>
							 </el-table-column>
							 <el-table-column label="计价单位" width="160">
								<template #default="scope">
								 <el-select v-model="scope.row.priceunits" style="width:120px">
								   <el-option v-for="item in weightList.list" :label="item.label" :value="item.value"></el-option>
								 </el-select>
								</template>
							 </el-table-column>
							 <el-table-column label="价格(元)" width="80">
								<template #default="scope">
									<el-input v-model="scope.row.price" @input="scope.row.price=CheckInputFloat(scope.row.price)"></el-input>
								</template> 
							 </el-table-column>
							 <el-table-column label="材积基数(cbcm)" width="120">
								 <template #default="scope">
								 	<el-input v-model="scope.row.drate" @change="changeCBM(scope.row)" @input="scope.row.drate=CheckInputFloat(scope.row.drate)"></el-input>
								 </template>
							 </el-table-column>
							 <el-table-column label="材积基数(cbm)" width="110">
								 <template #default="scope">
								 	<el-input v-model="scope.row.cbmrate"   @input="scope.row.cbmrate=CheckInputFloat(scope.row.cbmrate)"></el-input>
								 </template>
							 </el-table-column>
							 <el-table-column label="备注">
								 <template #default="scope">
								 	<el-input v-model="scope.row.remark"></el-input>
								 </template>
							 </el-table-column>
							 <el-table-column label="操作" width="80">
								  <template #default="scope">
								    <el-link @click="removeTransportation(scope.$index)" :underline="false" type="primary" class="font-small">删除</el-link>
					        	  </template>
							 </el-table-column>
						 </el-table>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;">
								<el-space>
									
									<el-button type="" @click="closeTab">关闭</el-button>
									<el-button type="primary" @click="submitCompanyDetail">提交</el-button>
								</el-space>
							</div>
						</div>
	</div>
	<TransTypeDailog ref="transTypeDailogRef" @confirm="loadTransTypeAllList"></TransTypeDailog>
	<!-- 渠道类型 -->
	<el-dialog v-model="Channelvisable"   title="编辑渠道类型">
		<el-table border :data="channelList.list">
			<el-table-column type="index" width="60">
				<template #header >
				   <el-link :underline="false" type="primary" @click="addChannelName">
				   <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
				   </el-link>
				</template>
			</el-table-column>
			<el-table-column label="渠道类型">
				<template #default="scope">
				<el-input v-model="scope.row.name"></el-input>
				</template>
			</el-table-column>
			<el-table-column label="常用国家">
				<template #default="scope">
				<el-select v-model="scope.row.country">
					<el-option label="US" value="US"></el-option>
					<el-option label="CA" value="CA" ></el-option>
					<el-option label="EU" value="EU" ></el-option>
					<el-option label="UK" value="UK" ></el-option>
					<el-option label="全部" value="" ></el-option>
				</el-select>
				</template>
			</el-table-column>
			<el-table-column label="操作">
				<template #default="scope">
					<el-link title="删除"  type="primary" :underline="false" @click="removeChannelName(scope.$index)">
						  删除
					</el-link>
				</template>
			</el-table-column>
		</el-table>
		<template #footer>
		
		      <span class="dialog-footer">
		        <el-button @click="Channelvisable = false">取消</el-button>
		        <el-button type="primary" @click="handleChannel"
		          >保存</el-button >
		      </span>
		    </template>
	</el-dialog>
</template>

<script>
	import {ArrowDown,Edit,Setting} from '@element-plus/icons-vue'
	import {Plus,Minus,Help } from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject } from 'vue'
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	import TransTypeDailog from "./components/trans_type_dialog.vue";
	import {ElMessage } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router';
	import {CheckInputFloat,CheckInputInt,formatFloat} from '@/utils/index.js';
	export default{
		components: {
			Plus,Edit,Minus,Help,TransTypeDailog,Setting
		},
		setup(){
			let Channelvisable =ref(false)
			let transTypeDailogRef=ref();
			let apiList =reactive({list:[]});
			let marketList=reactive({list:[]});
			let weightList=reactive({list:[
						{label:'重量(kg)',value:'weight'},
						{label:"体积(cbm)",value:'volume'},
					]})
		    let transTypeAllList=reactive({list:[]});	
			let channelAllList=reactive({list:[]});
			let transTypeList=reactive({list:[]});
			let channelList=reactive({list:[]});
			let tranTableData =reactive({list:[
				{
					marketplaceid:'',
					subarea:'',
					transtype:'',
					channel:"",
					price:'',
					priceunits:'weight',
					pretime:'',
					cbmrate:'',
					drate:'',
					channame:'',
					remarks:'',
				}
			]})
			let form = reactive({
				name:'',
				simplename:'',
				api:"",
				accessToken:'',
			});
		    let router = useRouter()
			function showCompanyDetail(){
				let id = router.currentRoute.value.query.id;
				if(id){
					transportationApi.showCompanyDetail({'id':id}).then((res)=>{
						if(res&&res.data){
						   form.name=res.data.name;
						   form.simplename=res.data.simplename;
						   if(res.data.api){
						      form.api=res.data.api;
						   }else{
							   form.api="";
						   }
						   form.accessToken=res.data.accessToken;
						   form.id=res.data.id;
						   tranTableData.list=res.data.detaillist;
						}
					});
				}
				
			}
		const emitter = inject("emitter"); // Inject `emitter`
		function closeTab(){
			emitter.emit("removeTab", 100);
		};
		 
		function editModeoftransport(){
			transTypeDailogRef.value.show();
		}
			function showTranInfo(id){
				router.push({
					path:'/transportation/details',
					query:{
					  id: id,
					  replace:true,
					  title:"物流详情",
					  path:'/transportation/details',
					}
				})
			}
			function addTransportation(){
				tranTableData.list.push({
					marketplaceid:'',
					subarea:'',
					transtype:'',
					channel:"",
					price:'',
					priceunits:'weight',
					pretime:'',
					drate:'',
					cbmrate:"",
					channame:'',
					remarks:'',
				})
			}
			function removeTransportation(index){
				tranTableData.list.splice(index,1)
			}
			
			
			
			function editChannel(){
				Channelvisable.value = true
			}
			function addChannelName(){
				channelList.list.push({
					label:'',
				})
			}
			function removeChannelName(index){
				channelList.list.splice(index,1)
			}
		    function loadApiList(){
				transportationApi.loadApiCompany().then((res)=>{
					apiList.list=res.data;
					apiList.list.push({id:"",name:"无"});
				});
			}
			function loadMarketList(){
				marketApi.getMarketAll().then((res)=>{
					marketList.list = res.data
				});
			}
			function loadTransTypeAllList(){
				transportationApi.getTransTypeAll().then((res)=>{
					transTypeAllList.list=res.data;
				});
			}
	
			function loadChannelAllList(){
				transportationApi.channelList().then((res)=>{
						channelAllList.list=res.data;
						channelList.list=[];
						res.data.forEach((item)=>{
							if(item.shopid){
							  channelList.list.push(item);
							}
						})
						
				});
			}
			function handleChannel(){
				 Channelvisable.value = false;
				   transportationApi.saveChannel(channelList.list).then(res=>{
					   ElMessage.success("操作成功！");
					   loadChannelAllList();
				   })
			}
			function submitCompanyDetail(){
				form.detaillist=tranTableData.list;
				let invaild=false;
				let msg="";
				tranTableData.list.forEach((item)=>{
					if(!item.marketplaceid){
						invaild=true;
						msg="站点不能为空";
				        return ;
					}
					if(!item.channame){
						invaild=true;
						msg="渠道名称不能为空";
					    return ;
					}
				});
				if(!form.name){
					invaild=true;
					msg="物流公司名称不能为空";
				}
				if(!form.simplename){
					invaild=true;
					msg="物流公司简称不能为空";
				}
				if(tranTableData.list.length<1){
					invaild=true;
					msg="至少需要一笔渠道才能提交";
				}
				
				
				if(invaild){
					ElMessage.error(msg);
					 return ;
				}
				transportationApi.saveCompanyDetail(form).then((res)=>{
					 ElMessage.success( '已提交成功！');
					 showTranInfo(res.data);
				});
				
			}
			function changeCBM(row){
				var cbcm=row.drate;
				if(row.drate){
					row.cbmrate=formatFloat(1000000/cbcm);
				}
			}
			function handleRouteToApi(){
				router.push({
					path:'/erp/thirdparty',
					query:{
					  title:"物流绑定",
					  path:'/erp/thirdparty',
					}
				})
			}
			onMounted(()=>{
			   loadApiList();
			   loadMarketList();
			   loadTransTypeAllList();
			   loadChannelAllList();
			   showCompanyDetail();
			});
			return{
				form,
				tranTableData,
				addTransportation,
				removeTransportation,
				editModeoftransport,handleChannel,
				Channelvisable,editChannel,addChannelName,removeChannelName,
				apiList,marketList,weightList,transTypeAllList,channelAllList,channelList,
				submitCompanyDetail,
				closeTab,transTypeDailogRef,loadTransTypeAllList,
				CheckInputFloat,CheckInputInt,changeCBM,formatFloat,
				handleRouteToApi
			}
		}
		}
</script>

<style>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 176px);
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
</style>
