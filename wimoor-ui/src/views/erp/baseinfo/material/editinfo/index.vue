<template>
	<div id="materialeditbaseinfo" class=" gary-bg" style="padding:16px 0px; ">
		<el-row >
			<el-col :xl="4" :lg="2" class="ri-tabs">
				<el-tabs tab-position="left"  v-model="activeName">
				    <el-tab-pane label="基本信息" name="1">
						<template #label>
							 <el-link  :underline="false"  href="#base">基本信息</el-link>
						</template>
					</el-tab-pane>
					<!-- v-if="router.currentRoute.value.query.isAssemable" -->
					<el-tab-pane  label="组合信息" v-if="type=='product'" name="2"  >
						<template #label>
							 <el-link :underline="false" href="#assemble">组合信息</el-link>
						</template>
					</el-tab-pane>
				    <el-tab-pane  label="采购信息" name="3">
						<template #label>
							 <el-link :underline="false" href="#purchase">采购信息</el-link>
						</template>
					</el-tab-pane>
					<el-tab-pane label="规格信息" name="4">
						<template #label>
							 <el-link :underline="false" href="#specs">规格信息</el-link>
						</template>
					</el-tab-pane>
					<el-tab-pane label="辅料关联" v-if="type=='product'"  name="5">
						<template #label>
							 <el-link :underline="false" href="#consumables">辅料关联</el-link>
						</template>
					</el-tab-pane>
				    <el-tab-pane label="物流信息" v-if="type=='product'"  name="6">
						<template #label>
							 <el-link :underline="false" href="#logistics">物流信息</el-link>
						</template>
					</el-tab-pane>
				  </el-tabs>
			</el-col>
			<el-col :xl="16" :lg="21">
				<el-card class="fr-con">
					 <el-scrollbar class="he-scr-car"  ref="scorollbarRef" @scroll="scroll">
					 <el-form ref="globalFormRef" :model="forms"  label-width="100px">
						 <!-- 基础信息 -->
						 <Base  ref="baseRef" :tagsValue="tagsValue"  :dataForms="forms.baseforms"   />
						 
					    <el-divider />
						
						<!-- 组装信息 -->
						<Assemble v-if='forms.baseforms.issfg!="2"&&type=="product"' :dataForms="forms.assemblyforms"   />
						<!--是本成品的时候 展示父亲列表-->
						<Parents v-else-if="type=='product'" :dataForms="forms.parentList" />
						
						 <el-divider />
						 
						 <!-- 采购信息 --> 
						<Purchase :dataForms="forms.supplierforms"   :dataBaseForms="forms.baseforms"  :forms="forms" />
						 
						   <el-divider />
						   
						  <!-- 规格信息 -->
						  <Specs :dataForms="forms.specsforms" />
						  
						  <el-divider />
						  
						  <!-- 辅料关联 -->
						  <Consumables v-if="type=='product'" :dataForms="forms.consforms" />
						  
						   <el-divider />
						   
						    <!--物流信息 -->
							<Logistics v-if="type=='product'" @clear="forms.logisforms=[]"  :dataForms="forms.baseforms" :custom="forms.custom" :listForms="forms.logisforms" />
					 </el-form>
						</el-scrollbar>
						<div class='text-center mar-top-16'>
							<el-space>
								<el-button @click="closeTab">取消</el-button>
								<el-button v-if="forms.baseforms.delete!=true" @click.stop="submitForm" type="primary">提交</el-button>
							</el-space>
						</div>
						
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>
<script>
    export default{ name:"编辑产品" };
</script>
<script setup>
import { ref,reactive,onMounted,watch,onUnmounted,toRefs,inject,nextTick} from 'vue'
import Base from"./components/base.vue"
import Purchase from"./components/purchase.vue"
import Logistics from"./components/logistics.vue"
import Consumables from"./components/consumables.vue"
import Specs from"./components/specs.vue"
import Assemble from"./components/assemble_info.vue"
import Parents from "./components/parent_info.vue"
import tabScroll from"@/utils/tab_scroll"
import {useRouter } from 'vue-router'
import {checkVisiable} from '@/utils/jquery/table/float-header';
import { ElMessage } from 'element-plus'
import materialApi from '@/api/erp/material/materialApi.js';
      const scorollbarRef=ref();
      const emitter = inject("emitter"); // Inject `emitter`
	  let router = useRouter()
	  const mid=router.currentRoute.value.query.details;
	  const iscopy=router.currentRoute.value.query.iscopy;
	  const type=router.currentRoute.value.query.type;
	  const scrolltopobj=router.currentRoute.value.query.scrolltopobj;
	  const baseRef=ref({});
	  const globalFormRef=ref();
	  onMounted(()=>{
	  	loadData();
	  })
		let state =reactive({
			activeName:"1",
			forms:{
				baseforms:{sku:'',name:'',imgfile:{},pkgimgfile:{},
				taglist:"",effectivedate:new Date(),issfg:"0",isDelete:false},
				supplierforms:[],
				specsforms:{pkgdim:{},itemdim:{},boxdim:{},boxnum:0},
				consforms:[],
        custom:{},
				logisforms:[{cname:'',ename:'',price:0}],
				assemblyforms:{list:[],assemblyTime:0,issemi:0},
				logislist:[],
				parentList:[],
			},
			tagsValue:[],
			
		})
		let {
			activeName,forms,tagsValue
		}=toRefs(state)
		function closeTab(){
			emitter.emit("removeTab", 100);
		};
		function scroll(obj){
			state.activeName= tabScroll(obj,"tab-scroll")
		}
		watch(() =>router.currentRoute.value.query,(newValue,oldValue)=> {
		      if(newValue&&newValue['refresh']){
					  setTimeout(()=>{
						    if(checkVisiable("materialeditbaseinfo")){
					  								state.forms={
					  										baseforms:{sku:'',name:'',imgfile:{},pkgimgfile:{},
															   taglist:"",effectivedate:new Date(),issfg:"0",isDelete:false},
					  										supplierforms:[],
					  										specsforms:{pkgdim:{},itemdim:{},boxdim:{},boxnum:0},
					  										consforms:[],
                                custom:{},
					  										logisforms:[],
					  										assemblyforms:{list:[],assemblyTime:0,issemi:0},
					  										logislist:[],
					  										parentList:[],
					  									}
							  }
					  },100);
			     }
		      },{ immediate: true });
		function loadData(){
			materialApi.getMaterialInfo({"id":mid}).then((res)=>{
				baseRef.value.loadBrandCateList();
				if(res.data && res.data.material){
					 if(iscopy=="ok"){
						res.data.material.id="";
						res.data.material.sku="";
					 }
					 if(res.data.parentList && res.data.parentList.length>0){
					 	state.forms.parentList=res.data.parentList;
					 }
					 state.forms.baseforms=res.data.material;
					 if(res.data.tagNameList){
						state.forms.baseforms.tagNameList=res.data.tagNameList;
					 }
					 if(res.data.supplierList && res.data.supplierList.length>0){
						state.forms.supplierforms=res.data.supplierList;
					 }
					 var specsformsData={};
					 var pkgdim={"length":0,"width":0,"height":0,"weight":0};
					 var itemdim={"length":0,"width":0,"height":0,"weight":0};
					 var boxdim={"length":0,"width":0,"height":0,"weight":0};
					 if(res.data.pkgDim!=null && res.data.pkgDim!=undefined && res.data.pkgDim!=""){
						  specsformsData.pkgdim=res.data.pkgDim;
					 }else{
						  specsformsData.pkgdim=pkgdim;
					 }
					 if(res.data.itemDim!=null && res.data.itemDim!=undefined && res.data.itemDim!=""){
						  specsformsData.itemdim=res.data.itemDim;
					 }else{
					 	  specsformsData.itemdim=itemdim;
					 }
					 if(res.data.boxDim!=null && res.data.boxDim!=undefined && res.data.boxDim!=""){
					 	  specsformsData.boxdim=res.data.boxDim;
					 }else{
					 	  specsformsData.boxdim=boxdim;
					 }
					 specsformsData.boxnum=res.data.material.boxnum;
					 state.forms.specsforms=specsformsData;
					 state.forms.consforms=res.data.consumableList;
					 state.forms.baseforms.taglist=res.data.taglist;
					 if(res.data.customs){
						 state.forms.logisforms=res.data.customs;
					 }
          if(res.data.custom){
            state.forms.custom=res.data.custom;
          }
					 if(res.data.assemblyList && res.data.assemblyList.length>0){
						 state.forms.assemblyforms.list=res.data.assemblyList;
						 state.forms.assemblyforms.assemblyTime=state.forms.baseforms.assemblyTime;
             state.forms.assemblyforms.isAssPrice=state.forms.baseforms.isAssPrice;
					 }
				}
				var arrs=[];
				materialApi.findMaterialTags({"mid":mid}).then((ress)=>{
					  if(ress.data && ress.data!=""){
						  var strs=ress.data.toString();
						  var list=strs.split(",");
						  list.forEach(function(item){
							  arrs.push(item);
						  });
						   state.tagsValue=arrs;
					  }else{
						   state.tagsValue=[];
					  }
					  if(scrolltopobj){
						  nextTick(()=>{
							  var top=parseInt(scrolltopobj);
							  if(top-30>0){
								  top=top-30;
							  }
						  	 scorollbarRef.value.setScrollTop(top);
						  })
					  }
				});
			});
		}
		function submitForm(){
			//提交表单数据 saveData
			globalFormRef.value.validate((isValid) => {
				if (isValid) {
					let FormDatas = new FormData()
					var datas={};
					var asstime=state.forms.assemblyforms.assemblyTime;
					var pricelist=[];
					state.forms.baseforms.assemblyTime=asstime;
					if(state.forms.specsforms.boxnum && state.forms.specsforms.boxnum!=""){
						state.forms.baseforms.boxnum=parseInt(state.forms.specsforms.boxnum);
					}
					var skustr=state.forms.baseforms.sku.toString();
					datas.material=state.forms.baseforms;
					if(type=="product"){
						datas.material.mtype=0;
					}
					if(type=="consumable"){
						datas.material.mtype=1;
					}
					if(type=="package"){
						datas.material.mtype=2;
					}
					datas.material.sku=skustr;
					datas.itemDim= state.forms.specsforms.itemdim;
					datas.boxDim=state.forms.specsforms.boxdim;
					datas.pkgDim=state.forms.specsforms.pkgdim;
					if(state.forms.baseforms.taglist!=null){
						datas.taglist=state.forms.baseforms.taglist;
					}
					datas.customs=state.forms.logisforms;
          datas.custom=state.forms.custom;
					if(datas.customs && datas.customs.length>0){
						var ispass=true;
						var nowcountry="";
						datas.customs.forEach(function(cust){
							if(nowcountry==cust.country){
								ispass=false;
							}else{
								nowcountry=cust.country;
							}
						});
						if(ispass==false){
							ElMessage.error('海关信息不能添加相同的国家!');
							return;
						}
					}
					datas.supplierList=state.forms.supplierforms;
					datas.consumableList=state.forms.consforms;
					datas.assemblyList=state.forms.assemblyforms.list;
          datas.material.isAssPrice=state.forms.assemblyforms.isAssPrice;
          datas.material.groupid=state.forms.baseforms.groupid;
					if(datas.supplierList && datas.supplierList.length>0){
						var issupplier=false;
						datas.supplierList.forEach(function(item){
							 if(item.isdefault==true){
								 issupplier=true;
							 }
						});
						if(issupplier==false){
							ElMessage.error('供应商列表必须有一个默认!');
							return;
						}
					}
					if(state.forms.assemblyforms.list && state.forms.assemblyforms.list.length>0){
						var isasspass=true;
						 state.forms.assemblyforms.list.forEach(function(item){
							 if(item.subnumber<=0){
								 isasspass=false;
							 }
						 });
						 if(isasspass==false){
						 	ElMessage.error('组装信息单位数量不能为0!');
						 	return;
						 }
						 datas.material.issfg="1";
					}else{
						datas.material.issfg="0";
					}
					if(iscopy=="ok"){
						 datas.material.id="";
						 datas.iscopy="ok";
					}
					FormDatas.append("infostr",JSON.stringify(datas));
					if(state.forms.baseforms.imgfile){
						FormDatas.append("file",state.forms.baseforms.imgfile);
					}else{
						FormDatas.append("file",{});
					}
					if(state.forms.baseforms.pkgimgfile){
						FormDatas.append("pkgfile",state.forms.baseforms.pkgimgfile);
					}else{
						FormDatas.append("pkgfile",{});
					}
					var stepisok=true;
					var priceok=true;
					if(datas.supplierList && datas.supplierList.length>0){
						datas.supplierList.forEach(item=>{
							if(item.stepList && item.stepList.length>0){
								//判断阶梯价 是否输入正确
								let amounts = item.stepList.map(items => parseInt(items["amount"]));
								let amountSet = new Set(amounts);//去重
								 if (amountSet.size == amounts.length) {
								   stepisok=true;
								} else {
								   stepisok=false;
								}
								item.stepList.forEach(step=>{
									if(step.amount==null || step.amount==undefined || step.amount=="" || step.price==null || step.price==undefined || step.price=="" || parseInt(step.amount)<=0){
										priceok=false;
									}
								});
							}
						});
					}
					if(!stepisok){
						ElMessage.error( '阶梯价不能出现重复的采购量！');
						return;
					}
					if(!priceok){
						ElMessage.error( '请正确输入采购供应商的采购单价和采购量！');
						return;
					}

					materialApi.saveData(FormDatas).then((res)=>{
						 if(res.data.id){
							ElMessage.success('操作成功！');
							 var title="产品详情";
							 if(type!="product"){
								 title="辅料详情";
							 }
							 router.push({
								path:'/material/details',
								query:{
								  title:title,
								  path:'/material/details',
								  details:res.data.id,
								  replace:true,
								  type:type
								}
							});
						 }
					});
				}})
		
		}
		
			
</script>

<style scoped>
.viewscroll{
	 box-shadow: inset 2px 2px 4px rgba(0, 0, 0, 0.2);
}
.dark .small-tab  th.el-table__cell{
		background-color: #000000;
	}	
.small-tab  th.el-table__cell{
	background-color: #fff;
}	
	.wi-60{
		width:60px;
	}
.mar-top-16{
	margin-top: 16px;
}
.fr-con .el-divider{
	padding-bottom:8px!important;
}
   .fr-con .tab-scroll {
   	margin-bottom:16px ;
   }
	.fr-con .el-card__body{
		height:calc(100vh - 90px);
		min-height:600px;
	}
	.ri-tabs{
		display: flex;
		justify-content: flex-end;
	}
	.ri-tabs .el-tabs--left .el-tabs__nav-wrap.is-left::after{
		height:0px;
	}
	.in-wi-24{
		width: 400px;
	}
	.grid-row .is-link{
		margin-left: 8px;
		opacity: 0;
	}
	.grid-row:hover .is-link{
		opacity: 1;
	}
	.fo-we-400{
		font-weight: 400;
	}
	.he-scr-car{
		height:calc(100vh - 156px);
	}

</style>
<style>
	.he-scr-car h3{
		margin-bottom: 24px;
	}
	.right-align-list .el-descriptions__label{
		width:100px;
		text-align: right;
		padding-right:16px;
	}
</style>