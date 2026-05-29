<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="12" :xs="24">
        <el-card   shadow="always">
			<el-row>
			<el-space>
				 <Group  @change="groupChange" defaultValue="only"  ></Group>
				 <el-button type="primary" @click.stop="handleAdd()">批量添加</el-button>
				 <el-button   @click.stop="handleUpload">导入添加</el-button>
		  </el-space>
		  <div class='rt-btn-group'>
		   <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" placeholder="搜索ASIN或SKU"></el-input>
		  </div>
		  </el-row>
		  
		   <GlobalTable ref="globalTable"
		    :tableData="tableData"  height="calc(100vh - 220px)" 
			@selectionChange='selectChange'
			@row-click="rowClick" 
		    :defaultSort="{ prop: 'psku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
		    style="width: 100%;margin-bottom:16px;">
		   	<template #field>
			 <el-table-column type="selection" :reserve-selection="false" width="38" />
		     <el-table-column label="产品信息" prop="psku"    sortable="custom" show-overflow-tooltip >
		     	<template #default="scope">
		     	<div class="flex-center">
		     		   <el-image v-if="scope.row.pimage" :src="scope.row.pimage" class="img-40"  width="40" height="40"  ></el-image>
					   <el-image v-else-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
		     		   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
		     		   <div >
						   <div v-if="scope.row.name">{{scope.row.name}}</div>
		     			   <div  v-else >{{scope.row.pname}}</div>
		     			   <p class="sku">{{scope.row.psku}} 
						   <span class="font-extraSmall"> ASIN:{{scope.row.asin}}</span>
		     			   </p>
		     		   </div>
		     	</div>
		     	</template>
		     </el-table-column>
			 <el-table-column label="FBA库存" prop="quantity"   width="120" sortable="custom" >
				 <template #default="scope">
				        <div v-if="scope.row.fbaquantity">{{scope.row.fbaquantity}}</div>
				 	    <div v-else>0</div>
				 </template>
			 </el-table-column>
			 <el-table-column label="本地库存" prop="fulfillable"  width="120"    >
				 	<template #default="scope">
				           <div v-if="scope.row.fulfillable">{{scope.row.fulfillable}}</div>
						    <div v-else>0</div>
				    </template>
			 </el-table-column>
			 <el-table-column fixed="right" prop="sku" label="操作"  width="125" >
			     <template #default="scope">
					 <el-button v-if="pidset.includes(scope.row.pid)" link type="info">已添加</el-button>
			         <el-button v-else type="primary" link @click="handleAdd(scope.row)">添加</el-button>
			     </template>
			 </el-table-column>
			</template>
			</GlobalTable>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card   shadow="always">
			SKU{{selectTableData.length}}个
			<el-button style="float: right;" link type="info" @click.stop="clearAll()">全部清空</el-button>
			<el-table style="margin-top:5px;" :data="selectTableData" border height="calc(100vh - 190px)" >
				<el-table-column  prop="pname" label="产品信息" >
					<template #default="scope">
					<div class="flex-center">
						   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
						   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
						   <div >
							   <div v-if="scope.row.name">{{scope.row.name}}</div>
							   <div  v-else >{{scope.row.pname}}</div>
							   <p class="sku"><span class="font-extraSmall">FNSKU:</span>{{scope.row.fnsku}} 
							   </p>
						   </div>
					</div>
					</template>
				</el-table-column>	
				<el-table-column prop="pname" label="打印数量" width="100" >
					<template #default="scope">
							<el-input  type="text"  v-model="scope.row.printnum" @input="scope.row.printnum=CheckInputInt(scope.row.printnum)"></el-input>
					</template>
				</el-table-column>	
				<el-table-column prop="pname" label="操作" width="100" >
					<template #default="scope">
					    <el-button type="primary"  link @click="handleDelete(scope.$index)">删除</el-button>
					</template>
				</el-table-column>	
			</el-table>
			<div style="margin-top:10px;">
			<el-button type="info" @click.stop="handleDownloadLabel('pdf')">打印产品标签-PDF</el-button>
			<el-button type="info" @click.stop="handleDownloadLabel('excel')">打印产品标签-Excel</el-button>
			</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
  
  <el-dialog
     v-model="uploadVisible"
     title="导入产品标签"
     width="400px"
   >
   <el-upload
      :drag="true"
      action
      :http-request="uploadFiles"
      :limit="1"
      :before-upload="beforeUpload" 
      :show-file-list="true" 
      :headers="headers" 
      accept=".xls,.xlsx"
      multiple
     >
       <el-icon class="font-large"><upload-filled /></el-icon>
       <div class="el-upload__text">
        拖拽文件到此处或 <em>点击上传</em>
       </div>
     </el-upload>
   <template #footer>
     <span class="dialog-footer">
  	   <div class="flex-center-between">
  	 <el-button type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
  	 <div>
       <el-button @click="uploadVisible = false">取消</el-button>
       <el-button type="primary" @click.stop="uploadExcel" :loading="btnLoading">
         上传文件
       </el-button></div></div>
     </span>
   </template>
    </el-dialog>
</template>
<script>
	export default{ name:"标签打印" };
</script>
<script setup>
 
import { reactive, toRefs ,ref} from 'vue';
import { WarningFilled } from '@element-plus/icons-vue';
import Group from '@/components/header/group.vue';
import { ElMessage, ElMessageBox } from 'element-plus'
import listApi from '@/api/amazon/listing/listingApi.js';
import {downloadLabel} from '@/hooks/amazon/listing/label.js';
import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
      const globalTable=ref();
	  const state = reactive({
		uploadVisible:false,
	    queryParams: {search:""}, 
		ids:[],
		pidset:[],
		selectTableData:[],
		tableData:{records:[],total:0},
		myfile:null,
		btnLoading:false,
	  });
	  const { queryParams,tableData,selectTableData,ids,pidset,uploadVisible,myfile,btnLoading } = toRefs(state);
	  function handleDownloadLabel(ftype){
		  if(state.selectTableData && state.selectTableData.length>0){
			  var data=[];
			  state.selectTableData.forEach(item=>{
				  var nowitem={};
				   //nowitem.title=item.pname;
				   nowitem.quantity=item.printnum;
				   nowitem.pid=item.pid;
				   //nowitem.marketplaceid=item.marketplaceid;
				   //nowitem.fnsku=item.fnsku;
				   //nowitem.sku=item.sku;
				   data.push(nowitem);
			  });
			  downloadLabel('',ftype,data);
		  }
	  }
	  function groupChange(obj){
	  			state.queryParams.groupid=obj.groupid;
	  			state.queryParams.marketplaceid=obj.marketplaceid;
	  			handleQuery();
	  }
	  function handleQuery(){
		   globalTable.value.loadTable(state.queryParams);
	  }
	  function loadTableData(params){
		  listApi.getProductInfoWithFnSKU(params).then(res=>{
				 state.tableData.records=res.data.records;
				 state.tableData.total=res.data.total;
		  })
	  }
	  function selectChange(selection) {
	  	     state.ids = selection;
	  }
	  function rowClick(row){
	  		  globalTable.value.toggleRowSelection(row,true);
	  }
     function handleAdd(row){
		 if(row){
			 row.printnum=1;
			 if(state.pidset.includes(row.pid)){
				 ElMessage.error("SKU:["+row.psku+"]已存在！");
				 return;
			 }else if(!row.fnsku){
				 ElMessage.error("SKU:["+row.psku+"]无FNSKU！");
				 return;
			 }else{
				 state.pidset.push(row.pid);
				 state.selectTableData.push(row);
			 }
			
		 }else{
			 state.ids.forEach(item=>{
				 item.printnum=1;
				 if(state.pidset.includes(item.pid)){
						 ElMessage.error("SKU:['"+item.psku+"']已存在！");
						 return;
				 }else if(!item.fnsku){
				 ElMessage.error( "SKU:["+item.psku+"]无FNSKU！");
				 return;
			 }else{
					 state.pidset.push(item.pid);
					 state.selectTableData.push(item);
				 }
			 })
		 }
	 }
	 function handleDelete(index){
	 	state.selectTableData.splice(index,1);
		state.pidset.splice(index,1);
	 }
	 function clearAll(){
		 state.selectTableData=[];
		 state.pidset=[];
	 }
	 
	 //文件上传之前
	 function beforeUpload(file){
	 	if (file.type != "" || file.type != null || file.type != undefined){
	 	    //截取文件的后缀，判断文件类型
	 		const FileExt = file.name.replace(/.+\./, "").toLowerCase();
	 		//计算文件的大小
	 		const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
	 		//如果大于50M
	 		if (!isLt5M) {
	 			ElMessage.error('上传文件大小不能超过 5MB!!');
	 			return false;
	 		}
	 		else {
	 		   return true;
	 		}
	 	}
	 }
	 function uploadExcel(){
		 state.btnLoading=true;
	 	let FormDatas = new FormData();
	 	FormDatas.append('file',state.myfile);
	 	listApi.uploadExcel(FormDatas).then(function(res){
			if(res.data && res.data.length>0){
				state.uploadVisible=false;
				ElMessage.success( '导入标签成功');
				addExcel(res.data);
			}else{
				ElMessage.error( '导入标签失败');
			}
			 state.btnLoading=false;
	 	})
	 }
	 
	 function uploadFiles(item){
	 	//上传文件的需要formdata类型;所以要转
	 	state.myfile=item.file;
	 }
	 
	 function handleUpload(){
		 state.uploadVisible=true;
	 }
	 
	 function downloadTemp(){
	 	listApi.downExcelTemp();
	 }
	 
	 function addExcel(list){
		 state.selectTableData=[];
		 list.forEach(item=>{
			 item.printnum=item.totalOfferCount;
			 item.pname=item.name;
			 item.pid=item.id;
			 state.pidset.push(item.id);
		 });
		 state.selectTableData=list;
	 }
 
</script>

<style scoped>
	.app-container{
		padding:20px;
	}
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
