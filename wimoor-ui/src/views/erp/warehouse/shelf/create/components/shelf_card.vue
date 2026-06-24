<template>
	<div class='sc-list'  >
		<div class='sc-head'>
			  <el-breadcrumb :separator-icon="ArrowRight" >
			    <el-breadcrumb-item v-for="item in breadname" class='pointer' @click="cardClick(item.id)">{{item.name}}</el-breadcrumb-item>
			  </el-breadcrumb>
			<!-- <span class='title '  >{{warehose}} 
			<span v-if="subnum>0" class='text-gray'> (下级{{subnum}})</span></span> -->
			<div class='rt-btn-group'>
			<el-dropdown>
			    <span class="el-dropdown-link">
			      批量操作
			      <el-icon class="el-icon--right">
			        <arrow-down />
			      </el-icon>
			    </span>
			    <template #dropdown>
			      <el-dropdown-menu>
			        <el-dropdown-item @click="deleteShelf(shelfDatas)">全部删除</el-dropdown-item>
					<el-dropdown-item  @click="printImage('',parentNode.data.id)">二维码下载</el-dropdown-item>
			      </el-dropdown-menu>
			    </template>
			  </el-dropdown>
			</div>
		</div>
		<div class='list-card'>
		 <el-row :gutter="16" >
			 <el-col :span="cardsize">
			       <el-card shadow="hover" @click="addShelf(parentNode)"  class='addcard'>
					   <plus theme="outline" size="32" fill="#666" :strokeWidth="5"/>
					   <span style='color:var(--el-text-color-secondary)' >点击添加库位</span>
				   </el-card>
			     </el-col>
			 <el-col :span="cardsize" v-for="shelfData in shelfDatas.list">
			       <el-card shadow="hover" v-if="shelfData.id">
					   <div class='card-head '>
						   <div class='l-text pointer'  @click="cardClick(shelfData)">
						     <h3 >{{shelfData.number}}-{{shelfData.name}}</h3>
						     <span class='font-extraSmall'>{{shelfData.num}}</span>
						   </div>
						   <div class='r-icon label pointer text-gray'>
							 <el-dropdown>
							     <span class="el-dropdown-link">
							       <el-icon color="var(--el-text-color-placeholder)"><more-filled /></el-icon>
							     </span>
							     <template #dropdown>
							       <el-dropdown-menu>
							         <el-dropdown-item  @click="deleteShelf(shelfData)" >删除</el-dropdown-item>
							         <el-dropdown-item @click="shelfDetail(shelfData)">详情</el-dropdown-item>
							       </el-dropdown-menu>
							     </template>
							   </el-dropdown>
						   </div>
					   </div>
					   <div class='card-body'> 
					        <el-row>
						 
							<el-col :span="12">
							  <span ><span class='label'>已用容量</span><span :title="sizePercent(shelfData)">{{shelfData.usesize}}%</span></span>
							</el-col>
							<el-col :span="12">
							  <div> <span class='label'>SKU数量</span>{{shelfData.summary.skunum}}</div>
							</el-col>
							 <el-col :span="24">
							 <span ><span  class='label'>规格</span>
							   <span v-if="shelfData.length">{{shelfData.length}}×{{shelfData.width}}×{{shelfData.height}}
							 	</span>
							  <span v-else>无</span>
							  </span>
						   </el-col>
					     	<el-col :span="12">
					     	 <div @click="downloadQRCode(shelfData)" class="pointer" title="点击下载二维码" >
					     	 	<PayCodeOne theme="outline" size="41" fill="#f5a623" :strokeWidth="3"/>			   
					     	 </div>
					     	</el-col> 
							   <el-col :span="12">
							  <div class='card-footer'>
							  	<el-button @click="editShelf(shelfData)">编辑</el-button>
							  </div>
							  </el-col> 
							</el-row>
					   </div>
				   </el-card>
			</el-col>
		 </el-row>
		 </div>
</div>
<el-dialog
  title="修改库位"
  v-model="dialogModifyVisible"
  width="40%"
  @close= 'closeselfDialog'
>
 <el-form
	       :model="formdata.data"
	       :rules="rules"
	       ref="ruleForm"
	       label-width="100px"
	       class="demo-ruleForm"
	       >
		    <el-row>
		    <el-col :span="20">
	       <el-form-item label="库位名称"  prop="name"  >
	          <el-input v-model="formdata.data.name"  placeholder="[排,行,列,格],[区,架,层,盒]"></el-input>
	       </el-form-item>
	       <el-form-item label="编码"   prop="number"  >
	          <el-input v-model="formdata.data.number"></el-input>
	       </el-form-item>
			<el-form-item label="规格" label-position="top" prop="specifications">
			 <el-row  :gutter="0">
				 <el-col :span="7">
					 <el-form-item    prop="length" >
					   <el-input v-model="formdata.data.length" class='input-left pull-left '    placeholder="长"></el-input>
					 </el-form-item>
				 </el-col>
				 <el-col :span="7">
					 <el-form-item    prop="width" >
					  <el-input v-model="formdata.data.width"  class='input-mid pull-left  '      placeholder="宽"></el-input>
					 </el-form-item>
				</el-col>
			      <el-col :span="10">
					 <el-form-item    prop="height" > 
					   <el-input v-model="formdata.data.height" class='input-right pull-left  '   placeholder="高" > <template #append >cm</template></el-input> 
					</el-form-item>
				</el-col>
			 </el-row>
			</el-form-item>
		   </el-col>
		   </el-row>
		 </el-form>
		 <template #footer>
		   <span class="dialog-footer">
		       <el-button @click="dialogModifyVisible = false">取 消</el-button>
		       <el-button  type="primary"  @click="modifySubmit(formdata.data)">完 成</el-button>
		   </span>
		 </template>
</el-dialog>
<el-dialog
  title="库位二维码"
  v-model="dialogImageVisible"
  width="40%"
>
         <div class="subcenter">
	 
				 <h2 class="text-center" >{{showname}}</h2>
				 <el-image :src="imageUrl"></el-image>
			 
		 </div>
		 <template #footer>
		   <span class="dialog-footer">
		       <el-button @click="dialogImageVisible = false">取 消</el-button>
		       <el-button  type="primary"  @click="printImage(qrcodeShelfId,'')">打印</el-button>
		   </span>
		 </template>
</el-dialog>
<el-dialog
  title="库位详情"
  v-model="dialogShelfdetailVisable"
  width="40%"
>
<el-row class='card-body'>
	<el-col :span="24">
	  <span ><span class='label'>库位名称：</span><span >{{shelfDetaildata.number}}-{{shelfDetaildata.name}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>已用容量：</span><span >{{shelfDetaildata.usesize}}%</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>SKU数量：</span><span >{{shelfDetaildata.summary.skunum}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>库存数量：</span><span >{{shelfDetaildata.summary.quantity}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>SKU异常数量：</span><span >{{shelfDetaildata.summary.expnumber}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>规格(cm)：</span><span >{{shelfDetaildata.length}}×{{shelfDetaildata.width}}×{{shelfDetaildata.height}} </span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>容量(m³)：</span><span >{{capacityFormat(shelfDetaildata.capacity)}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>操作人：</span><span >{{shelfDetaildata.summary.operator}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>操作时间：</span><span >{{parseTime(shelfDetaildata.summary.opttime,'{y}-{m}-{d}')}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>创建人：</span><span >{{shelfDetaildata.summary.creator}}</span></span>
	</el-col>
	<el-col :span="12">
	  <span ><span class='label'>创建时间：</span><span >{{parseTime(shelfDetaildata.summary.creattime,'{y}-{m}-{d}')}}</span></span>
	</el-col>
</el-row>
</el-dialog>
</template>
<script>
import { ArrowDown ,MoreFilled, ArrowRight,} from '@element-plus/icons-vue'	
import {Plus,PayCodeOne,DeleteOne} from '@icon-park/vue-next';
import { ref ,onMounted,watch,reactive} from 'vue';
import shelfApi from '@/api/erp/warehouse/shelf.js';
import { ElMessageBox,ElMessage, ElLoading } from 'element-plus';
import myform from "@/hooks/erp/warehouse/form";
import {formatFloat,parseTime,deepCopy} from '@/utils/index.js';
export default{
	name:'shelfCard',
	components: {ArrowDown,Plus,MoreFilled,PayCodeOne,DeleteOne,ArrowRight},
	props:['breadname'],
	setup(props,{attrs,slots,emit}){ 
		const parentNode=reactive({data:{}});
		const subnum =ref(0);
		const cardsize = ref(6)
		const ruleForm =ref();
	    const imageUrl =ref('');
		const showname=ref('');
		const loading=ref(false);
		const dialogModifyVisible =ref(false);
		const dialogImageVisible =ref(false);
		const dialogShelfdetailVisable=ref(false);
		const qrcodeShelfId=ref("");
		const screenwidth = ref(document.body.offsetWidth)
		const shelfDatas =reactive({list:[]});
		const formdata =reactive({data:{}});
		//关闭时调用
		function closeselfDialog(){
			ruleForm.value.resetFields();
		}

	    const loadData =(data)=>{
			    parentNode.data=data;
				if(data.children){
					subnum.value=data.children.length;
				}else{
					subnum.value=0;
				}
			    var loadingInstance= ElLoading.service({ target:".sc-list"});
			 	shelfApi.detailWarehouseShelf(data.id).then(response=>{ 
					loadingInstance.close();
					if(response&&response.data){
					   shelfDatas.list=response.data;
					}else{
						shelfDatas.list=[];
					}
				});
		 }
		//方法
		 window.onresize =()=>{
			 screenwidth.value= document.body.offsetWidth
		 }
		 const addShelf=(parentNode)=>{
			 emit("add-new",parentNode.data);
		 }
		 const capacityFormat=(value)=>{
			return formatFloat(parseFloat(value)/1000000) ;
		 }
		 const sizePercent=(data)=>{
			 let summary=data.summary;
			 if(summary&&summary.size){
		       return  '占用：'+formatFloat(parseFloat(summary.size)/1000000)+'m³,容量：'+formatFloat(parseFloat(data.capacity)/1000000)+"m³";
			 }else{
				 return  '没有占用空间或无法求出';
			 }
		 }
		 const computePercent=(value)=>{
			 if(value&&parentNode.data.capacity){
				 return formatFloat(parseFloat(value)/parseFloat(parentNode.data.capacity))+"%";
			 }else{
				 return "0%";
			 }
		 }
		 const cardClick=(id)=>{
			 emit("card-click",id);
		 };
		 const downloadQRCode=(shelfData)=>{
			 qrcodeShelfId.value=shelfData.id;
			  shelfApi.getQRCode(shelfData.id).then(res => {
							 const blob = new Blob([res]);
						     let url = window.URL.createObjectURL(blob);
							 showname.value=shelfData.number+"-"+shelfData.name;
							 imageUrl.value=url;
							 dialogImageVisible.value=true;
            });
		 }
		 const printImage=(qrcodeShelfId,parnetid)=>{
			         if(qrcodeShelfId==''&&parnetid==''){
						ElMessage.error("无法获取到库位ID");
						 return ;
					   }
					   var name=showname.value;
					  if(parnetid!=''){
						  name=parentNode.data.name;
					  }
				 
		 			  shelfApi.getQRCodePdf(qrcodeShelfId,parnetid).then(res => {
		 							 const blob = new Blob([res]);
		 						     if(window.navigator["msSaveOrOpenBlob"]&&window.navigator.msSaveOrOpenBlob()){
										 navigator.msSaveBlob(blob,showname.value+".pdf")
									 }else{
										 var link=document.createElement("a");
										 link.href=window.URL.createObjectURL(blob);
										 link.download=name+".pdf";
										 link.click();
										 window.URL.revokeObjectURL(link.href);
									 }
		 							 dialogImageVisible.value=false;
		              });
		 }
		 
		 const deleteShelf=(shelfData)=>{
			 var deleteids="";
			 if(shelfData["list"]){
				 var datalist=shelfData.list
				 for(let i =0;i<datalist.length;i++){
					 var item=datalist[i];
					 deleteids+=item.id+",";
				 }
			 }else{
				 deleteids=shelfData.id;
			 }
			 ElMessageBox.confirm('确认要删除吗？',"提示",{confirmButtonText: '确定',
			                                             cancelButtonText: '取消',
			 										     type: 'warning',})
			 			  .then((_) => {
			 						 shelfApi.deleteWarehouseShelf(deleteids).then(response=>{
										     if(response.code===200){
											     ElMessage.success('删除成功');
											 }
			 						         emit("modify-tree",parentNode.data.id);
			 						         loadData(parentNode.data);
			 	             	     });
			 		              })
			 			   .catch((_) => {})
											  
			 
		 };
		 const modifySubmit=(shelfdata)=>{
			 ruleForm.value.validate((valid) => {
			           if (valid) {
						   if(shelfdata.length&&shelfdata.width&&shelfdata.height){
							     shelfdata.capacity=parseFloat(shelfdata.length)*parseFloat(shelfdata.width)*parseFloat(shelfdata.height);
						   }
						   shelfdata.treepath=(parentNode.data.treepath?parentNode.data.treepath:"root")+"!"+shelfdata.number;
						   shelfApi.modifyWarehouseShelf(shelfdata).then(response=>{
											  ElMessage.success('保存成功');
											  emit("modify-tree",parentNode.data.id);
											  dialogModifyVisible.value=false;
							});
			           } else {
						   ElMessage.error('校验出错，请修改后重试');
					     ruleForm.value.resetFields();
			             return false
			           }
			         })
					 
			
		 }
	
 
		 const editShelf=(shelfdata)=>{
			 formdata.data=deepCopy(shelfdata);
			 dialogModifyVisible.value=true;
		 }
		 const contentform=myform();
		 const shelfDetaildata = ref()
		 function shelfDetail(shelfData){
			 dialogShelfdetailVisable.value = true
			 shelfDetaildata.value = shelfData
		 }
		watch(screenwidth,(newvalue,oldvalue)=>{
			if(newvalue<1600){
				cardsize.value=8
			}else{
				cardsize.value=6
			}
		});
		var rules=contentform.rules;
		return{
			parentNode,subnum,parentNode,shelfDatas,closeselfDialog,
			cardsize,screenwidth,addShelf,loadData,cardClick,rules,
			deleteShelf,editShelf,dialogModifyVisible,formdata,
			modifySubmit,downloadQRCode,sizePercent,computePercent,
			imageUrl,dialogImageVisible,showname,printImage,qrcodeShelfId,
			parseTime,ruleForm,dialogShelfdetailVisable,shelfDetail,shelfDetaildata,capacityFormat
		}
		
	}
}
</script>
<style>
  .sc-head{display:flex;
   font-size:var(--el-font-size-base);
   padding:16px 0px;
   margin:0px 16px;
   border-bottom:1px solid var(--el-border-color-light)
  }
  .list-card{
	  margin:0px 16px;
  }
  .text-gray{
	 color: var(--el-text-color-secondary);
  }
  .text-center{
	  text-align: center;
  }
 .list-card  .el-card{margin-top:16px;height:202px}
  .addcard{
	   display:flex;
	   align-items: center;
	   justify-content: center;
  }
 .list-card .el-card .card-head{margin-bottom:16px;display: flex;}
  .card-head .r-icon{margin-left:auto;font-size:12px}
  .addcard .el-card__body{
	  display:flex;
	  flex-direction: column ;
	  align-items: center;
  }
  .card-body{font-size:var(--el-font-size-base)}
  .card-body  .label{color:var(--el-text-color-secondary);
   margin-right:8px;
  }
 .card-body .el-col{
	 margin-bottom:8px;
 }
 .subcenter{
	        text-align: center;
	 		display: table-cell;
	 		vertical-align: middle;
	 		width:800px;
	 		height: 100%;
	 		 
 }
 .subcenter .el-image, .subcenter img{
		max-width: 100%;
		max-height: 100%;
	}

 .card-footer{display:flex;justify-content: flex-end;margin-top:40px;}
</style>