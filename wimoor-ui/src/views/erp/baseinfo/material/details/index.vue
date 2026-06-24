<template>
	<div class=" gary-bg" style="padding:16px 0">
		<el-row>
			 <el-col :xl="4" :lg="2"  class="ri-tabs">
				<el-tabs v-if="data.assemblyList"  tab-position="left"  v-model="activeName">
				    <el-tab-pane label="基本信息" name="1">
						<template #label>
							 <el-link :underline="false"  href="#base">基本信息</el-link>
						</template>
					</el-tab-pane>
					<el-tab-pane  v-if="(data.assemblyList&&data.assemblyList.length>0)||(data.parentList&&data.parentList.length>0)&&type=='product'" label="组合信息" name="2">
						<template #label>
							 <el-link v-if="issfg=='1'" :underline="false"  href="#children">组合信息</el-link>
							 <el-link v-else-if="issfg=='2'" :underline="false"  href="#parent">组合信息</el-link>
							 <el-link v-else :underline="false"  href="#normal">组合信息</el-link>
						</template>
					</el-tab-pane>
				    <el-tab-pane label="采购信息" name="3">
						<template #label>
							 <el-link :underline="false" href="#purchase">采购信息</el-link>
						</template>
					</el-tab-pane>
					<el-tab-pane label="规格信息" name="4">
						<template #label>
							 <el-link :underline="false" href="#specs">规格信息</el-link>
						</template>
					</el-tab-pane>
					<el-tab-pane v-if="data.consumableList&&data.consumableList.length>0&&type=='product'" label="辅料关联" name="5">
						<template #label>
							 <el-link :underline="false" href="#consumables">辅料关联</el-link>
						</template>
					</el-tab-pane>
					<el-tab-pane v-if="data.consumablePList&&data.consumablePList.length>0&&type=='consumable'" label="辅料关联" name="5">
						<template #label>
							 <el-link :underline="false" href="#consumablePs">辅料关联</el-link>
						</template>
					</el-tab-pane>
				    <el-tab-pane v-if="data.customs&&type=='product'" label="物流信息" name="6">
						<template #label>
							 <el-link :underline="false" href="#logistics">物流信息</el-link>
						</template>
					</el-tab-pane>
					<el-tab-pane label="库存信息" name="7">
						<template #label>
							 <el-link :underline="false" href="#inv">库存信息</el-link>
						</template>
					</el-tab-pane>
				  </el-tabs>
			</el-col>
			<el-col  :xl="16" :lg="21">
				<el-card class="fr-con">
					 <el-scrollbar class="he-scr-car" ref="scrollbarRef" @scroll="scroll" always>
						 <Listinfo ref="infoRef" />
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							<el-space>
								<el-button @click="closeTab" >关闭</el-button>
								<el-button @click.stop="copyinfo"  v-if="type" type="primary">复制</el-button>
								<el-button v-if="isdelete=='false'&&type"   @click.stop="editinfo" type="primary">编辑</el-button>
								<el-button v-if="isdelete!='false'&&type"  @click.stop="recoverRows(data)" type="primary">启用</el-button>
							</el-space>
						</div>
						
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>
<script setup>
import { ref,reactive,onMounted,watch,onUnmounted,inject,onUpdated} from 'vue'
import tabScroll from"@/utils/tab_scroll"
import Listinfo from"./components/listinfo.vue"
import {useRouter } from 'vue-router'
import {redirectToList} from '@/utils/page_helper.js';
import { ElMessage } from 'element-plus'
import materialApi from '@/api/erp/material/materialApi.js';
	    const emitter = inject("emitter"); // Inject `emitter`
		let activeName =ref('1');
		const infoRef=ref({});
		const scrollbarRef = ref()
		let router = useRouter()
		let data=ref({});
		let issfg=ref("0");
		let isdelete=ref("false");
		const mid=router.currentRoute.value.query.details;
		const type=router.currentRoute.value.query.type;
		var scrolltopobj=null;
		function scroll(obj){
			scrolltopobj=obj.scrollTop;
			activeName.value = tabScroll(obj,"tab-scroll")
		}
		 function closeTab(){
			  if(type=="consumable"){
				     redirectToList(router,"/erp/material/consumable",'辅料管理');
				  }else if(type=="package"){
					 redirectToList(router,"/erp/material/package",'包材管理');
				  }else{
				     redirectToList(router,"/erp/product/localproduct",'产品管理');
				 }
		 	//emitter.emit("removeTab", 100);
		 }
		 function loadData(){
			 console.log(mid);
			if(mid){
				materialApi.getMaterialInfo({"id":mid}).then((res)=>{
					if(res.data){
						infoRef.value.loadData(res.data);
						data.value=res.data;
						issfg.value=res.data.material.issfg;
						if(res.data.material.isDelete==true){
							isdelete.value="true";
						}else{
							isdelete.value="false";
						}
					}
				})
			}else{
				ElMessage.error("未找到对应本地产品");
				emitter.emit("removeTab", 100); 
			}
		 	
		}
		function editinfo(){
	        emitter.emit("removeCache", "编辑产品");
			router.push({
				path:'/localproduct/editinfo',
				query:{
				  title:"编辑产品",
				  path:'/localproduct/editinfo',
				  details:mid,
				  replace:true,
				  type:type,
				  scrolltopobj:scrolltopobj
				}
			}) 
		}
		function copyinfo(){
			emitter.emit("removeCache", "编辑产品");
			router.push({
				path:'/localproduct/editinfo',
				query:{
				  title:"编辑产品",
				  path:'/localproduct/editinfo',
				  details:mid,
				  iscopy:'ok',
				  type:type,
				  scrolltopobj:scrolltopobj
				}
			}) 
		}
		function recoverRows(row){
			materialApi.recoverData({"id":mid,"sku":row.material.sku}).then((res)=>{
				if(res.data){
					ElMessage.success(res.data);
					isdelete.value="false";
					loadData();
				}
			})
				 
		}
		onMounted(()=>{
		   loadData();
		   var timer=setTimeout(()=>{
			   if(scrollbarRef&&scrollbarRef.value&&scrollbarRef.value.update){
			      scrollbarRef.value.update();
			   }
			   clearTimeout(timer);
		   },2000)
		})
</script>

<style>
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
   .fr-con h3{
   	margin-bottom:16px ;
   }
	.fr-con .el-card__body{
		height:calc(100vh - 68px);
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
	.flex-center .el-link--inner{
		display: flex;
		align-items: center;
		margin-left: 8px;
		opacity: 0;
	}
	.grid-row:hover .flex-center .el-link--inner{
		opacity: 1;
	}
	.fo-we-400{
		font-weight: 400;
	}
	.he-scr-car{
		height:calc(100vh - 176px)
	}
</style>
