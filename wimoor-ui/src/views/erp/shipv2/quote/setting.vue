<template>
    <div style="padding:20px;">
	    <div >
			<el-row gutter="20">
				<el-col :span="6">
					<el-card title="">
						<template #header>
						     <div class="card-header">
						       <span>询价商管理</span>
						     </div>
						   </template>
					<div v-if="token" style="padding-bottom:20px">
						  <el-descriptions :column="1"  border  >
						    <el-descriptions-item label="状态"><el-tag type="success">已绑定</el-tag> </el-descriptions-item>
						    <el-descriptions-item label="令牌">  <span v-if="tokenname">({{tokenname}})</span>	 {{token}} 	 
						                  <copy class="" @click.stop="CopyText(token)" title='复制SKU' theme="outline" size="14" fill="#666" :strokeWidth="3"/>	 </el-descriptions-item>
						    <el-descriptions-item label="操作"> 	<el-button @click="unbindToken" link type="primary" >解绑</el-button></el-descriptions-item>
						  </el-descriptions>
					</div>
					<div v-else style="padding-bottom:20px">
						<el-descriptions :column="1"  border  >
						  <el-descriptions-item label="状态"> <el-tag type="danger">未绑定</el-tag> </el-descriptions-item>
						  <el-descriptions-item label="令牌"> <el-input v-model="edittoken" placeholder="填写询价商Token"></el-input> </el-descriptions-item>
						  <el-descriptions-item label="别名"> <el-input v-model="editname" placeholder="填写别名"></el-input> </el-descriptions-item>
						  <el-descriptions-item label="操作"> <el-button  @click="bindToken"   type="primary" >绑定</el-button></el-descriptions-item>
						</el-descriptions>
					</div>
						<el-collapse v-if="token"  v-model="activeNames" @change="handleChange">
										    <el-collapse-item title="高级" name="1">
										     <el-form-item label="名称">
										     	<el-input v-model="buyer.name" :disabled="!buyer.edit" placeholder="填写供应商名称"></el-input>
										     </el-form-item>
										     <el-form-item label="地址">
										     	 <el-input v-model="buyer.address" :disabled="!buyer.edit" placeholder="填写地址信息"></el-input>
										     </el-form-item>
										     <el-form-item label="手机号">
										     	 <el-input v-model="buyer.mobile" :disabled="!buyer.edit" placeholder="填写手机号"></el-input>
										     </el-form-item>
										     <el-form-item label="联系人">
										     	 <el-input v-model="buyer.contact" :disabled="!buyer.edit" placeholder="填写联系人"></el-input>
										     </el-form-item>
										     <div style=" margin-bottom:20px">
										     				  <div v-if="token" >
										     					    <el-button  v-if="!buyer.edit" @click="buyer.edit=true"   type="primary">修改</el-button>
										     					     <el-button v-else  @click="addBuyer"   type="primary">保存</el-button>
										     				  </div>
										     				<div v-else>
										     					 <el-button    @click="addBuyer"    type="primary">新增</el-button>
										     				</div>
										     				
										     </div>
										    </el-collapse-item>
						</el-collapse>
			 				  
				</el-card>						 
			</el-col>	
			<el-col  :span="18">
				<el-card title="" body-style="padding:0px">
					 <template #header>
						  <div class="card-header flex-between">
									<el-button @click="handleShow">新增</el-button>
							<div>物流供应商管理</div>
						  </div>
						</template>
						<el-table  :data="tableData" height="calc(100vh - 145px)" >
							  <el-table-column prop="name" label="名称"     >
								 <template #default="scope">
								   <div>{{scope.row.name}}</div>
								   <div class="font-extraSmall">{{scope.row.address}}</div>
							  </template>
							   </el-table-column>
							  <el-table-column prop="contact" label="联系人"    width="230"  >
								 <template #default="scope">
								   <div>{{scope.row.contact}}</div>
								   <div>{{scope.row.mobile}}</div>
							  </template>
							   </el-table-column>
							  <el-table-column prop="createtime" label="链接"  width="240"  v-if="isowner" v-hasPerm="'erp:pi:supplier:link'"   >
								 <template #default="scope">
									   <el-link type="success" :href="urlFormat(scope.row)" target="_blank" > <el-icon><Link /></el-icon> 供应商页面</el-link>
									   <copy style="padding-left:10px" @click.stop="CopyText(urlFormat(scope.row))" title='复制SKU' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
								 </template>
							  </el-table-column>
							<el-table-column prop="createtime" label="创建时间"  width="200" >
								 <template #default="scope">
									  {{dateTimesFormat(scope.row.createtime)}} 
								 </template>
							</el-table-column>
							<el-table-column prop="createtime" label="操作"  width="180" >
								 <template #default="scope">
									<el-button  @click="editSupplier(scope.row)">编辑</el-button>
									<el-button type="danger" @click="delSupplier(scope.row.id)">删除</el-button>
								 </template>
							</el-table-column>
						</el-table>
						</el-card>			
				   </el-col>
				   </el-row>
					</div>
	</div>
 <CreateDialog ref="createDialogRef" @change="loadSupplier()"></CreateDialog>
</template>

<script setup>
	import {Search,ArrowDown,Link} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import CopyText from"@/utils/copy_text.js";
	import orderApi from '@/api/quote/orderApi.js';
	import supplierApi from '@/api/quote/supplierApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import CreateDialog from './components/create_dialog.vue';
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import {ElMessage,ElMessageBox } from 'element-plus';
	const emit = defineEmits(['change']);
	const createDialogRef=ref();
	const state = reactive({
		edittoken:"",
		editname:"",
		supplier:{},
		tableData:[],
		token:"",
		tokenname:"",
		title:'新增',
		isowner:false,
		buyer:{'edit':true},
	})
	const{
		token,buyer,edittoken,editname,supplier,tableData,title,isowner,tokenname,
		}=toRefs(state);
	function addBuyer(){
		orderApi.addBuyer(state.buyer).then(res=>{
			if(res.data){
				state.edittoken=res.data;
			    bindToken("isowner");
			}else{
				ElMessage.error("操作失败");
				state.tableData=[];
			}
		})
	}
	function bindToken(isowner){
		var owner=false;
		if(isowner=="isowner"){
			owner=true;
		}
		orderApi.getBuyer({"token":state.edittoken}).then(res=>{
			if(res.data){
				state.buyer=res.data;
				state.buyer.edit=false;
				thirdpartyApi.saveQuoteToken({"buyertoken":state.edittoken,"isowner":owner,"name":state.editname}).then(res=>{
					state.token=state.edittoken;
					state.tokenname=state.editname;
					loadSupplier();
					ElMessage.success("绑定成功");
					emit("change");
				})
			}else{
				ElMessage.error("绑定失败，未找到询价商");
				state.tableData=[];
			}
		})
		
	}
	function unbindToken(){
		ElMessageBox.confirm(
			     '您确定要删除此Token，这样您将失去对此询价商的所有信息?',
			    '解除此询价商绑定关系',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				thirdpartyApi.unbindQuoteToken().then(res=>{
					state.buyer={'edit':true};
					state.token='';
					state.tableData=[];
					ElMessage.success("解除绑定成功");
					emit("change");
				})
			  })
	}
	 function loadBuyer(){
		 orderApi.getBuyer({"token":state.token}).then(res=>{
		 	state.buyer=res.data;
			if(state.buyer){
		 	    state.buyer.edit=false;
			}else{
				state.buyer={edit:true};
			}
		 })
	 }
	 

	function loadSupplier(){
			 var datas={};
			 datas.token=state.token;
			 supplierApi.listsupplier(datas).then((res)=>{
				 state.tableData=res.data;
			 });
	}
	function delSupplier(id){
		ElMessageBox.confirm(
			     '您确定要删除此记录?',
			    '删除报价供应商',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  supplierApi.deletesupplier({"id":id}).then((res)=>{
				  	 ElMessage.success("删除供应商成功!");
					 state.supplier={};
					  state.title="新增";
				  	 loadSupplier();
				  });
			  })
		
	}
	function editSupplier(row){
		state.supplier=row;
		state.title='保存';
		createDialogRef.value.show(state.token,state.supplier,state.title);
	}
	function handleShow(){
		createDialogRef.value.show(state.token,{},state.title);
	}
	function urlFormat(row){
		var url=location.origin+"/quote?token="+row.token+"&title="+row.name;
		return url;
	}
	  function loadToken(){
		  thirdpartyApi.getQuoteToken().then((res)=>{
			state.token=res.data.buyertoken;
			state.tokenname=res.data.name;
			state.edittoken=res.data.buyertoken;
			state.editname=res.data.name;
			state.isowner=res.data.isowner;
			if(state.token){
			  loadBuyer();
			  loadSupplier();
			}
			
			});
	}
	onMounted(()=>{
		      loadToken();
	});
</script>

<style>
</style>