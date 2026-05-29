<template>
<el-dialog
  title="添加库位"
  v-model="dialogVisible"
  width="80%"
  class="shelfdialog"
  :before-close="handleClose"
>

  <el-row style="border-top:solid 1px #dcdde0;">
     <el-col :span="4" >
	   <div class=" bg-purple-light gird-body" style="min-height:350px;height:100%; border-right:solid 1px #dcdde0">
        <ShelfTree ref="lefttree" @node-click="setNodeValue"></ShelfTree>
	   </div>
     </el-col>
    <el-col :span="20" style='padding-top:20px;'>
	       <el-form
	       :model="formdata"
	       :rules="rules"
	       ref="ruleForm"
	       label-width="100px"
	       class="demo-ruleForm"
	       >
		    <el-row>
		    <el-col :span="12">
	       <el-form-item label="父级:" >
	         {{formdata.parentname}}
	       </el-form-item>
	       <el-form-item label="规格:">
			   <div v-if="formdata.parentLength||formdata.parentWidth||formdata.parentHeight">
				   <span v-if="formdata.parentLength">
					  <span >长({{formdata.parentLengthUnit}}):</span><span style="padding-right:10px;">{{formdata.parentLength}} </span>   
				   </span>
				   <span v-if="formdata.parentWidth">
					  <span> 宽({{formdata.parentWidthUnit}}):</span><span style="padding-right:10px;">{{formdata.parentWidth}} </span> 
				   </span>
				   <span v-if="formdata.parentHeight">
					  <span> 高({{formdata.parentHeightUnit}}):</span><span style="padding-right:10px;">{{formdata.parentHeight}}</span> 
				   </span>
			   </div>
			 <div v-else>
				 无
			 </div>
	       </el-form-item>
	       <el-form-item label="库位名称"  prop="name"  >
	          <el-input v-model="formdata.name"  placeholder="[排,行,列,格],[区,架,层,盒]"></el-input>
	       </el-form-item>
	       <el-form-item label="起始编码"   prop="number"  >
	          <el-input v-model="formdata.number"></el-input>
	       </el-form-item>
	   
			<el-form-item label="规格" label-position="top" prop="specifications">
				<div class="el-input-group-thir">
					<el-input v-model="formdata.length" placeholder="长"></el-input>
					<el-input v-model="formdata.width" placeholder="宽"></el-input>
					<el-input v-model="formdata.height" placeholder="高">
						<template #append>cm</template>
					</el-input>
				</div>
			</el-form-item>
	       
		    <el-form-item  label="库位数量" prop="quantity">
		         <el-col :span="22"><el-input v-model="formdata.quantity"></el-input></el-col> 
		     </el-form-item>
		   </el-col>
		   </el-row>
		      </el-form>
			<el-divider style="margin:0px" />
		  
		   <el-form class="itemlist"  ref="itemForm"     :rules="rules" label-width="100px" :model="listitem" label-position="top">
			<el-space :size="16" class="shelf-title">
				<h4>库位列表</h4>
				<span class="font-extraSmall">生成的库位名称，规格只做参考，可根据实际情况修改</span>
			</el-space>
			<el-row  :gutter="20" v-if="listitem&&listitem.list&&listitem.list.length >=1">
			   <el-col :span="8" v-for="item in listitem.list">
				   <el-card class="box-card">
					   <el-form-item label="库位名称" >
						   <el-row  :gutter="0">
								<el-col :span="11">
								   <el-form-item  label-position="top"   >
									   <el-input v-model="item.number" ></el-input>
								   </el-form-item>
								</el-col>
								<el-col class="text-center" :span="2">
								   <span class="text-gray-500">-</span>
								</el-col>
								<el-col :span="11">
								  <el-form-item    >
									   <el-input v-model="item.name"  ></el-input> 
								   </el-form-item>
							   </el-col>
						   </el-row>
						</el-form-item>
					    <el-form-item label="规格" label-position="top" prop="specifications" >
							<div class="el-input-group-thir">
								<el-input v-model="item.length" placeholder="长"></el-input>
								<el-input v-model="item.width" placeholder="宽"></el-input>
								<el-input v-model="item.height" placeholder="高">
									<template #append>cm</template>
								</el-input>
							</div>
						</el-form-item>
				   		 
				   </el-card>
			   </el-col>
		   </el-row>
			<el-row v-else>
				 <el-col :span="5" >
					   <el-card class="box-card">
						 <div   class="text item"> 暂无库位生成</div>
					   </el-card>
			     </el-col>
		   </el-row>
		   </el-form>
	  
		
     </el-col>
  </el-row>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
	    <el-button   @click="dialogSubmit(false)" >继续添加</el-button>
        <el-button  type="primary"  @click="dialogSubmit(true)">完 成</el-button>
    </span>
  </template>
</el-dialog>
</template>

<script>
   import ShelfTree from "@/views/erp/warehouse/shelf/base/shelf_tree.vue";
   import myform from "@/hooks/erp/warehouse/form";
   import {reactive,onBeforeMount,ref,onUpdated,watch } from 'vue';
   export default {
	  name: 'shelf_dialog',
	  components: {ShelfTree},
      setup(props,{attrs,slots,emit}){
		     const dialogVisible=ref(false);
		     const hasDialogSelectNode=ref(false);
			 const lefttree=ref();
			 const {ruleForm,itemForm,rules,submitForm,resetForm,formdata,listitem}=myform();
		     const showDailog = (data) => {
					dialogVisible.value=true;
					if(data){
						setTimeout(()=>{
							if(data.id){
								lefttree.value.selectNode(data.id);
							}
						},100);
					}
		        }
			 const reloadDailogTree=(parentid)=>{ 
				 if(lefttree.value){
				   lefttree.value.reloadTree(parentid);
				 }
			 }
			 const dialogSubmit=(needhide)=>{
				 if(submitForm()!=false){
					 if(needhide){
						  lefttree.value.reloadTree(formdata.parentid);
						  setTimeout(()=>{
							   emit("submit-done",formdata.parentid);
						  },300);
				          dialogVisible.value = false;
					 }else{
						  lefttree.value.reloadTree(formdata.parentid);
						  setTimeout(()=>{
						       emit("submit-done",formdata.parentid);
						  },300);
					 }
				 }
			 }
			const setNodeValue=(node)=>{
				   formdata.parentname=node.treepath?node.number+'-'+node.name:node.name;
				   formdata.parentid=node.id;
				   if(node.addressid){
						formdata.addressid=node.addressid
				   }else{
					    formdata.addressid=node.id
				   }
				   if(node.width){
						  formdata.parentLength=parseFloat(node.length) ;
					      formdata.parentWidth= parseFloat(node.width);
					      formdata.parentHeight= parseFloat(node.height);
				   } else{
					    formdata.parentLength=0;
					    formdata.parentHeight=0;
					    formdata.parentWidth=0;
				   }
				   formdata.parentLengthUnit="cm";
				   formdata.parentWidthUnit="cm";
				   formdata.parentHeightUnit="cm";
				   formdata.treepath=node.treepath;
				};
		     function handleClose(done) {  done();  };
			return {showDailog,dialogVisible,ruleForm,itemForm,rules,
			        handleClose,submitForm,resetForm,formdata,
					listitem,lefttree,setNodeValue,dialogSubmit,reloadDailogTree};
		 } 
	  }

</script>
<style>
	.shelf-title{
		margin-bottom:16px;
	}
	.input-mid input{
		border-radius:0em;
	}
	.width30p .el-input-group__append{
		padding:0 10px !important;
		margin-top:-1px;
	}
	.itemlist{
		padding-top:15px;padding-left:15px;
	}
    .itemlist .el-form--label-top .el-form-item__label{
		padding: 0 0 5px 0;
	}
	.itemlist .widthhalf{
		width:40%;
	}
	.itemlist .box-card {
	      width: 100%;  
		  padding-bottom:10px;
		  margin-bottom:10px;
	}
	.width60{
		width:23%;
	}
	.width30p{
		width:37%;
	}
	.shelfdialog .el-dialog__body {
		padding-top:0px !important;
		}
</style>