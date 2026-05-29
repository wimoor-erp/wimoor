<template>
	<div class="box-margin">
		<div class="pag-radius-bor mar-bot" >
			<div class=" two-box" > 
			  <Header ref="headerRef" ></Header>
			  <el-space>
			  <div class="boxgroup-subtext">以下装箱组中的SKU会一同配送，打包时请为包装箱编号，便于识别，装箱组中可包含多个包装箱。  </div>
			  <el-button @click="generatePackingOptions" :icon="Refresh" size="small"> {{packagetitle}}</el-button>
			    <el-switch
			      v-model="planData.areCasesRequired"
			      class="ml-2"
			      inline-prompt
				  @change="handleAreCasesRequired"
			      style="--el-switch-on-color: #68cea4; --el-switch-off-color: #ffbb6c"
			      active-text="单箱"
			      inactive-text="混装"
			    />
			</el-space>
			  <div  v-loading="optionloading"  element-loading-text="加载装箱方案..." style="min-height:320px;">
				  	<div  v-if="!options||options.length==0" class="font-extraSmall text-center" style="padding-top:150px;">
						<el-space>
							<el-icon :size="16"><WarningFilled /></el-icon>
							无法加载到装箱方案
						</el-space>
					</div>
			  <div   v-else v-for="(optitem,index) in options"> 
			      <el-divider v-if="index>0" />
				  <div class="flex-between boxgroup-wrap"   >
						  <el-tag size="large"  effect="dark"   round>装箱方案 {{index+1}}</el-tag>
						  <div>
							  <el-button v-if="optitem.status=='OFFERED'"  @click="confirmPackingOption(optitem)"  :loading="optitem.loading">选择此方案</el-button>
						      <el-button v-else-if="optitem.status=='ACCEPTED'"  @click="confirmPackingOption(optitem)" type="success" :loading="optitem.loading">已选择此方案</el-button>
							  <el-button v-else   @click="confirmPackingOption(optitem)" type="info" :loading="optitem.loading">已失效</el-button>
						  </div>
					</div>
				            <div  >
				 			  <el-card style="margin-bottom:20px" v-for="(groupInfo,index) in optitem.listPackingGroups" :key="index" shadow="false">
				 					  <template #header>
				 						<el-row>
											<el-col :span="12">
				 						  <span class="font-bold font-base">装箱组 {{index+1}}</span>
										  <div class="font-extraSmall m-t-8">{{groupInfo.skunum}}个SKU (共{{groupInfo.skuqty}}件商品)</div>
										  </el-col>
										  <el-col :span="12">
											  <div class="text-right"><el-link type="primary" @click="showGroupInfo(groupInfo)">查看商品详情</el-link></div>
										  </el-col>
				 						</el-row>
				 					  </template>
				 					   <el-row>
				 						   <el-col :span="groupInfo.hasBox==false?16:24">
											   <div class="box-product-wrap">
											   <el-space :size="12" wrap>
				 							   <div   v-for="item in  groupInfo.items" class="box-product-img" >
				 									 <img :src="item.skuinfo.image" />
				 									 <p class="font-base">x{{item.quantity}}</p>
				 							   </div>
											   </el-space>
											   </div>
				 						   </el-col>
				 						   <el-col v-if="groupInfo.hasBox==false" :span="4" >
				 								<div class="confirmBoxNum-wrap">
				 									 <p class="mar-bot" style="font-weight: bolder;"  v-if="!planData.areCasesRequired">确认包装箱数量</p>
				 									<div style="margin-bottom:16px;" v-if="!planData.areCasesRequired">
				 										需要&nbsp;
														<el-input-number  style="width: 120px;" :min="1" :max="10000" v-model="groupInfo.boxnum"  @change="handleChange" /> 
														&nbsp;个箱子包装 
				 									</div>
				 									<el-button  type="primary" @click.stop="openPackList(groupInfo)">打开装箱表单</el-button>
				 								</div>
				 						   </el-col>
				 						</el-row>
										<template v-if="groupInfo.hasBox==true"   #footer>
														 						<el-link  @click.stop="openPackList(groupInfo)"  type="primary">
																					<ad-product class="ic-cen" theme="filled" size="16"/>&nbsp;
														 						    <div class="font-base">装箱表单（共{{groupInfo.boxnum}}箱）</div>
														 						</el-link>
										</template>
				 					</el-card>
							</div>
						 <el-row >
							     <el-col :span="10">
							     <!-- <span class="font-extraSmall">原厂包装发货的SKU数量为0</span> -->
							 	 </el-col>
							      <el-col :span="10">
									  
								   </el-col>
										<el-col :span="4">
											 <el-button style="float:right" v-if="planData.auditstatus>3"  @click="toNext()"  >下一步 </el-button>
											<el-button style="float:right;margin-right: 10px;" @click="submitBox('submit')" type="primary" :loading="btnloading">确认装箱信息</el-button>
										</el-col>						   
						</el-row>
				</div>
			
				<el-row style="padding-top:10px;">
				    <el-col :span="10">
				        <Operation ref="operationRef" :titles="['提交箱子信息','生成发货报告']"  @change="handleOperationChange"></Operation>
					    <Operation ref="operationRef2" :titles="确认箱子分组"  @change="handleOperationChange"></Operation>
			     	</el-col>
				 </el-row>
			</div>
			</div>
		</div>
		<Footer  ref="footerRef"></Footer>
	</div>
	 
	 
	<BoxTable  ref="boxTableRef" @change="getBoxData"></BoxTable>
	<BoxTableCase  ref="boxTableCaseRef" @change="getBoxData"></BoxTableCase>
    <Table ref="tableRef" ></Table>
	
	
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs, nextTick } from 'vue';
	import { useRoute,useRouter } from 'vue-router';
	import {Location,Van,ShoppingCart,Printer,Document,Refresh,Management,WarningFilled} from '@element-plus/icons-vue';
	import {Plus,AdProduct} from '@icon-park/vue-next';
	import BoxTable from "./components/box_table.vue";
	import BoxTableCase from "./components/box_table_case.vue";
	import Table from "./components/table.vue";
	import Footer from "./components/footer.vue";
	import Header from "./components/header.vue";
	import Operation from "./components/operation.vue";
	import {pointKeyChnage} from '@/utils/jquery/key/point-key';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import {dateFormat,formatFloat,getValue,formatPercent,CheckInputIntLGZero,CheckInputFloat} from '@/utils/index.js';
	import addressApi from '@/api/amazon/inbound/addressApi.js';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	let router = useRouter();
	const planid=router.currentRoute.value.query.id;
	const footerRef=ref();
	const boxTableRef=ref();
	const boxTableCaseRef=ref();
	const headerRef=ref();
	const globalTable=ref();
	const operationRef=ref();
	const operationRef2=ref();
	const tableRef=ref();
	let state =reactive({
		packagetitle:'重新分组',
		planData:{}, 
		tableData:{records:[],total:0},
		step:0,
		itemcount:0,
		formsubmitdisable:true,
		operatorInfo:null,
		optionloading:false,
		btnStatus:{
			"submitForm":false,
			"submitStep":false,
			"number":1,
		},
		options:[],
		boxDisable:false,
		btnloading:false,
	})
	let{planData,step,formsubmitdisable,itemcount,tableData,operatorInfo,btnStatus,options,boxDisable,optionloading,btnloading,
	packagetitle}=toRefs(state);
	
	function stepChange(val){
		state.step=val;
		//loading.value=true;
		//shipmentRef.value.getBaseInfo(shipmentid);
	}
	function handleAreCasesRequired(){
		shipmentplanApi.updateAreCasesRequired({"formid":planid,"areCasesRequired":state.planData.areCasesRequired}).then((res)=>{
			 ElMessage.success("修改成功");
		});
	}
	function showGroupInfo(groupInfo){
		tableRef.value.show(state.planData,groupInfo);
	}

	function loadData(){
		shipmentplanApi.getPlanInfo({"formid":planid}).then((res)=>{
			state.planData=res.data;
			state.itemcount=state.planData.itemlist.length;
			headerRef.value.show(state.planData,1);
			footerRef.value.show(state.planData);
			//获取箱子分组信息
			listPackingOptions();
		});
	}
	function toNext(){
		router.push({
			path:'/e/s/s/placement',
			query:{
			  id:planid,
			  title:"发货处理_配置",
			  path:'/e/s/s/placement',
			  replace:true
			}
		})		 
	}
	function handleDonePacking(){
		shipmentBoxApi.donePlanBox({"formid":planid,"ftype":"submit"}).then((res)=>{
			 generatePlacementOptions();
		}).catch(e=>{
			state.btnloading=false;
		});
	}
	function generatePackingOptions(){
		state.packagetitle="分组中....";
		shipmentBoxApi.generatePackingOptions({"formid":planid}).then((res)=>{
			if(res.data && res.data.operationid){
				operationRef.value.show(res.data.operationid,1);
				state.packagetitle="重新分组";
			}
		}).catch(()=>{
			state.btnloading=false;
		});
	}
	
	function listPackingOptions(nextToken){
		var param={"formid":planid};
		 param.pageSize=20;
		 if(nextToken){
			  param.paginationToken=nextToken; 
		 }else{
			 param.paginationToken=null;
		 }
		 state.optionloading=true;
		shipmentBoxApi.listPackingOptions(param).then((res)=>{
			if(res.data&&res.data.options&&res.data.options.length>0){
				state.options=res.data.options;
				getOptionsItem();
				if(state.options&&state.options.length==1&&state.options[0].status=="OFFERED"){
					confirmPackingOption(state.options[0]);
				}
			}else if(state.planData.auditstatus==3){
				generatePackingOptions();
			}else {
				state.options=[];
				state.optionloading=false;
			}
		}).catch(e=>{
			state.optionloading=false;
		});
	
	}
	var sortBy = function (filed, rev, primer) {
	    rev = (rev) ? -1 : 1;
	    return function (a, b) {
	        a = a[filed];
	        b = b[filed];
	        if (typeof (primer) != 'undefined') {
	            a = primer(a);
	            b = primer(b);
	        }
	        if (a < b) { return rev * -1; }
	        if (a > b) { return rev * 1; }
	        return 1;
	    }
	};
	async function listPackingGroupItems(packingOptionId,packingGroupId,nextToken,optionitem){
		var param={};
		param.formid=planid;
		param.packingOptionId=packingOptionId;
		param.packingGroupId=packingGroupId;
		param.pageSize=100;
		if(nextToken){
			param.paginationToken=nextToken; 
		}else{
			param.paginationToken=null;
		}
		
		await shipmentBoxApi.listPackingGroupItems(param).then((res)=>{
			 var groupInfo=res.data;
			 groupInfo.packstyle=2;
			 groupInfo.packSetType=1;
			 groupInfo.hasBox=false;
			 var skuMap={};
			 state.planData.itemlist.forEach(item=>{
				 skuMap[item.sku]=item;
			 })
			 groupInfo.packingOptionId=param.packingOptionId;
			 groupInfo.packingGroupId=param.packingGroupId;
			 var skunum=0;
			 var skuqty=0;
			 groupInfo.items.forEach(item=>{
				  item.skuinfo= skuMap[item.msku];
				  skuqty+=item.quantity;
				  item.skuitemid= skuMap[item.msku].id;
				  skunum++;
			 });
			 groupInfo.skunum=skunum;
			 groupInfo.skuqty=skuqty;
			 groupInfo.items.sort(sortBy('msku', false, String));
			 state.optionloading=false;
			 optionitem.listPackingGroups.push(groupInfo);
		});
		//如果箱子组已经提交了装箱 直接加载装箱信息 查询
		optionitem.listPackingGroups.forEach(item=>{
			selectPackgroupInfo(item);
		});
	}
	function checkAllSubmit(){
		var formsubmitdisable=false;
			state.options.forEach(vitem=>{
				vitem.listPackingGroups.forEach(mitem=>{
					 if(mitem.hasBox==false){
						 formsubmitdisable=true;
					 }
				});	
			})
		  state.formsubmitdisable=formsubmitdisable;
	}
		function selectPackgroupInfo(item){
			shipmentBoxApi.selectPackgroupInfo({"packingGroupId":item.packingGroupId}).then((ress)=>{
				if(ress.data.msg=="success"){
					//已经填写过装箱信息
					item.hasBox=true;
					item.boxnum=parseInt(ress.data.num);
				    checkAllSubmit()
					
				}else{
					item.hasBox=false;
				}
			});
		}
		
	function getLocalOptionsItem(){
		 var groupInfo={};
		 state.options=[{listPackingGroups:[]}];
		 groupInfo.packstyle=2;
		 groupInfo.packSetType=1;
		 groupInfo.packageCount=null;
		 groupInfo.hasBox=false;
		 groupInfo.packingOptionId=state.planData.id;
		 groupInfo.packingGroupId=state.planData.id;
		 groupInfo.items=state.planData.itemlist;
		 groupInfo.items.forEach(item=>{
			             item.packingGroupId=state.planData.id;
		 				 item.skuinfo= item;
						
		 });
		 state.options[0].listPackingGroups.push(groupInfo);
		 selectPackgroupInfo( state.options[0].listPackingGroups[0]);
		
		 
	}
	async function getOptionsItem(){
		for(var j=0;j<state.options.length;j++){
			var item=state.options[j];
			var packingOptionId=item.packingOptionId;
			item.listPackingGroups=[];
			for(var i=0;i<item.packingGroups.length;i++){
				await listPackingGroupItems(packingOptionId,item.packingGroups[i],null,item);
			}
		}
	}
	
	function generatePlacementOptions(){
		shipmentPlacementApi.generatePlacementOptions({"formid":state.planData.id}).then(res=>{
			if(res.data.operationid){
				 operationRef.value.show(res.data.operationid,1);
			}
		})
	}
	
	function handleOperationChange(data){
		//跳转路由至第三步
		if(data && data.operationStatus=="SUCCESS" &&data.operation=="setPackingInformation"){
		   handleDonePacking();
		}
		if(data && data.operationStatus=="SUCCESS" &&data.operation=="generatePackingOptions"){
			 listPackingOptions();
		}
		if(data && data.operationStatus=="SUCCESS" &&data.operation=="confirmPackingOption"){
			 listPackingOptions();
		}
		if(data && data.operationStatus=="SUCCESS" &&data.operation=="generatePlacementOptions"){
			var timer1=setTimeout(function(){
					router.push({
						path:'/e/s/s/placement',
						query:{
						  id:planid,
						  title:"发货处理_配置",
						  path:'/e/s/s/placement',
						  replace:true
						}
					})		 
					clearTimeout(timer1);
			},500);
		}
	}
	
	function confirmPackingOption(groupInfo){
		var param={};
		param.formid=planid;
		param.packingOptionId=groupInfo.packingOptionId;
		groupInfo.loading=true;
		if(state.planData.invtype!=1&&state.planData.invtype!="1"){
			shipmentBoxApi.confirmPackingOption(param).then((res)=>{
				groupInfo.loading=false;
			   if(res.data && res.data.operationid){
			   	operationRef2.value.show(res.data.operationid,1);
			   	state.packagetitle="确认分组";
			   }
			});
		}else{
			groupInfo.loading=false;
		}
		
	}
	
	function confirmPackStyle(groupInfo){
		if(groupInfo.packstyle==1 || groupInfo.packstyle=='1'){
			groupInfo.packageCount='one';
		}else{
			groupInfo.packageCount='more';
		}
		confirmPackingOption(groupInfo);
	}
	function gotoPackInfo(groupInfo){
		groupInfo.packageCount=null;
	}
	
	
	function openPackList(groupInfo){
		if(!(groupInfo.boxnum && groupInfo.boxnum>1)){
			groupInfo.boxnum=1;
		}
		groupInfo.boxnum=parseInt(groupInfo.boxnum);
		state.operatorInfo=groupInfo;
		if(state.planData.areCasesRequired){
			boxTableCaseRef.value.show(groupInfo,state.planData);
		}else{
			boxTableRef.value.show(groupInfo,state.planData);
		}
	}
	
 
	function getBoxData(data){
		selectPackgroupInfo(data);
	}
	
	function packNumchange(row,index){
		var boxindex='boxNum'+index;
		if(row[boxindex]&&row[boxindex]!="0"){
		   row[boxindex]=CheckInputIntLGZero(row[boxindex]);
		}
		packNumSum(row);
	}
	
	//装箱数量校验
	function packNumSum(row){
		let sum = 0
		for(var i=0;i<state.operatorInfo.boxnum ;i++){
		    if(row['boxNum'+i]){
			sum +=parseInt(row['boxNum'+i]);
			}
		}
		row.sum=sum;
	}
	function submitBox(){
		state.btnloading=true;
		shipmentBoxApi.submitPackingInformation({"formid":planid}).then((res)=>{
			if(res.data &&res.data.operationid){
				ElMessage.warning("箱子信息已提交...");
				operationRef.value.show(res.data.operationid,0);
			}
		}).catch(()=>{
			state.btnloading=false;
		});
	}
	
	
	
	onMounted(()=>{
		loadData();
	})
</script>
<style>
	.boxgroup-wrap{
		margin-bottom: 16px;
	}
	.boxgroup-subtext{
		font-size: 14px;
		color:#999;
	}
	.box-product-img img{
		height:48px;
		width: 48px;
		border:1px solid #eee;
		border-radius: 4px;
	}
	.box-product-wrap{
	  padding: 12px 16px;
	}
	.two-box .el-card__header {
	    padding: 12px 16px;
	    border-bottom: 1px solid var(--el-card-border-color);
	    box-sizing: border-box;
	    background-color: #F8F8F8;
	}
	.dark .two-box .el-card__header {
	    padding: 12px 16px;
	    border-bottom: 1px solid var(--el-card-border-color);
	    box-sizing: border-box;
	    background-color: #040404;
	}
	.two-box .el-card__body {
	    padding:0px;
	}
	.two-box .el-card__footer {
	    padding: 8px 16px;
	    box-sizing: border-box;
	    background-color: #F8F8F8;
	}
	.dark .two-box .el-card__footer {
	    padding: 8px 16px;
	    box-sizing: border-box;
	    background-color: #040404;
	}
	.confirmBoxNum-wrap{
		border-left: 1px solid #dedede;
		padding:12px 16px;
		font-size: 14px;
	}
	.dark .confirmBoxNum-wrap{
		border-left: 1px solid #040404;
		padding:12px 16px;
		font-size: 14px;
	}

	.mar-bot{
		margin-bottom:16px;
	}
	.mar-bot10{
		margin-bottom:10px;
	}
	.box-margin{
		padding:16px;
		min-height:calc(100vh - 36px)
	}
</style>