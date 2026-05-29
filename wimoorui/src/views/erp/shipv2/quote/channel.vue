<template>
    <div style="padding:20px;">
	    <div >
			<el-row gutter="20">
				<el-col :span="6">
					<ChannelType ref="channelTypeRef" @change="handleChannelTypeChange"></ChannelType>				 
			    </el-col>	
			<el-col  :span="18">
				<el-card title="" body-style="padding:0px">
					 <template #header>
						  <div class="card-header flex-between">
							  	<el-space>
								<div>物流商渠道</div>
								<el-select v-model="supplierid">
									<el-option label="全部" value=""></el-option>
									<el-option v-for="item in suppliers" 
									   :key="item.id"
									   :label="item.name"
									   :value="item.id">
									</el-option>
								</el-select>
								<el-input v-model="name" clearable placeholder="请输入名称">
									<template #append>
									        <el-button :icon="Search" @click="handleQuery"/>
									      </template>
								</el-input>
								</el-space>
								<el-space>
									<el-button @click="handleDownload">上传</el-button>
									<el-button @click="handleShow" type="primary">新增</el-button>
								</el-space>
								
						  </div>
						</template>
						<div style="padding-bottom:10px">
						<GlobalTable
						 ref="globalTableRef"
						   @loadTable="loadTableData" 
						   :tableData="tableData"  
						   :stripe="true"  
							height="calc(100vh - 203px)"
						  >
							  <template #field>
									 <el-table-column label="名称" prop="name"  sortable="custom" >
									 	<template #default="scope">
									 		{{scope.row.name}}
									 	</template>
									 </el-table-column>
									 <el-table-column label="渠道类型名称" prop="channeltype" sortable="custom"  >
									 	<template #default="scope">
									 		{{scope.row.channeltype}}
									 	</template>
									 </el-table-column>
									 <el-table-column label="物流公司" prop="company"  sortable="custom" >
									 	<template #default="scope">
									 		{{scope.row.company}}
									 	</template>
									 </el-table-column>
									 <el-table-column label="操作时间" prop="opttime" sortable="custom"  >
									 	<template #default="scope">
									 		{{scope.row.opttime}}
									 	</template>
									 </el-table-column>
									 <el-table-column label=""  width="120"  sortable="custom" >
										<template #default="scope">
											<el-space>
											<el-button @click="delSupplier(scope.row)" link type="danger">删除</el-button>
											<el-button @click="editSupplier(scope.row)" link type="primary">更新</el-button>
											</el-space>
										</template>
									 </el-table-column>
							  </template>
						</GlobalTable>
						</div>
						</el-card>			
				   </el-col>
				   </el-row>
					</div>
	</div>
 <el-dialog
     v-model="visible"
     title="渠道类型"
     width="500"
     :before-close="handleClose"
   >
     <el-form :model="form" label-width="auto" style="max-width: 600px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
		<el-form-item label="物流公司">
			<el-select v-model="form.supplierid">
				<el-option v-for="item in suppliers" 
				   :key="item.id"
				   :label="item.name"
				   :value="item.id">
				</el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="渠道类型">
			<el-select v-model="form.channelid">
				<el-option v-for="item in channelOptions" 
				   :key="item.id"
				   :label="item.name"
				   :value="item.id">
				</el-option>
			</el-select>
		</el-form-item>
      </el-form>
     <template #footer>
       <div class="dialog-footer">
         <el-button @click="visible = false">取消</el-button>
         <el-button type="primary" @click="handleSubmit"> 确认 </el-button>
       </div>
     </template>
   </el-dialog>
   <UploadDialog ref="uploadDialogRef" @upload="handleUpload"></UploadDialog>
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
	import transchannelApi from '@/api/quote/transchannelApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import ChannelType  from "./components/channel_type.vue";
	import UploadDialog from "@/components/Upload/uploadDialog.vue";
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import {ElMessage,ElMessageBox } from 'element-plus';
	const emit = defineEmits(['change']);
	const globalTableRef=ref();
	const channelTypeRef=ref();
	const uploadDialogRef=ref();
	const state = reactive({
		edittoken:"",
		editname:"",
		suppliers:[],
		tableData:{},
		token:"",
		tokenname:"",
		form:{},
		name:"",
		supplierid:"",
		title:'新增',
		channelOptions:[],
		isowner:false,
		visible:false,
		buyer:{'edit':true},
	})
	const{
		token,buyer,edittoken,editname,suppliers,tableData,title,isowner,tokenname,name,supplierid,visible,form,channelOptions
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
	function handleChannelTypeChange(channels){
		
	}
	function handleDownload(){
		uploadDialogRef.value.show({"action":"/quote/api/v1/transchannel/downloadTemp","template":"downloadTemp.docx"});
	}
	function handleUpload(data){
		transchannelApi.uploadFile(state.token,data).then(res=>{
			handleQuery();
			uploadDialogRef.value.hide();
			if(res.data){
				ElMessage.error(res.data);
			}
		});
	}
	function handleShow(){
		state.visible=true;
	}
	function handleQuery(){
		globalTableRef.value.loadTable({"name":state.name,"supplierid":state.supplierid});
	}
	function loadTableData(param){
		transchannelApi.tlist(state.token,param).then(res=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		})
	}
	function handleSubmit(){
		state.form.opttime=null;
		if(state.form.id){
			state.form.opttime=null;
			transchannelApi.tupdate(state.token,state.form).then(res=>{
					 ElMessage.success("操作成功!");
					 state.visible=false;
					 handleQuery();
			})
		}else{
			transchannelApi.tsave(state.token,state.form).then(res=>{
				 ElMessage.success("操作成功!");
				 state.visible=false;
				 handleQuery();
			})
		}
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
				 state.suppliers=res.data;
			 });
	}
	function delSupplier(row){
		ElMessageBox.confirm(
			    '您确定要删除此记录?',
			    '删除报价供应商渠道',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  row.disable=true;
				  row.opttime=null;
				  transchannelApi.tupdate(state.token,row).then((res)=>{
				  	 ElMessage.success("删除供应商渠道成功!");
				  	 handleQuery();
				  });
			  })
		
	}
	function editSupplier(row){
		state.form=row;
		state.visible=true;
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
				channelTypeRef.value.show(res.data);
			    handleQuery();
			    loadSupplier();
				transchannelApi.listall(state.token).then(res=>{
					state.channelOptions=res.data;
				})
			}
	    });
	}
	onMounted(()=>{
		      loadToken();
	});
</script>

<style>
</style>