<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="24" :xs="24">
        <el-card   shadow="always">
			<el-row>
			<el-space>
				 <Group  @change="groupChange"  defaultValue="only"  ></Group>
				  <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" placeholder="请输入SKU"></el-input>
				  <Datepicker ref="datepickers" shortIndex="1" @changedate="changedate" />
		  </el-space>
		  <div class='rt-btn-group'>
			  <el-space>
				  <DataDialog ref="dataDialogRef" @change="handleQuery"></DataDialog>
		    <div>
				<el-dropdown>
				  <span class="el-dropdown-link">
				     <el-button >
				           导入报告<el-icon class="el-icon--right"><arrow-down /></el-icon>
				         </el-button>
				  </span>
				  <template #dropdown>
				    <el-dropdown-menu>
				      <el-dropdown-item  @click.stop="showupload('GET_EPR_MONTHLY_REPORTS')">导入EPR报告</el-dropdown-item>
				      <el-dropdown-item  @click.stop="showupload('GET_VAT_TRANSACTION_DATA')">导入Vat报告</el-dropdown-item>
					  <el-dropdown-item  @click.stop="showSalesUpload('saleseee')">导入EEE销量</el-dropdown-item>
					  <el-dropdown-item  @click.stop="showSalesUpload('salesfc')">导入FC销量</el-dropdown-item>
					  <el-dropdown-item  @click.stop="showWeightUpload()">导入SKU重量</el-dropdown-item>
				    </el-dropdown-menu>
				  </template>
				</el-dropdown>
			</div>
			<el-button type="primary" @click.stop="downloadList">导出</el-button>
			</el-space>
		  </div>
		  </el-row>
		  <el-row style="margin-bottom: 10px;">
			<!-- <el-button type="primary" @click.stop="calcEleAmount">计算用量</el-button> -->
		   </el-row>
		   <GlobalTable ref="globalTable"
		    :tableData="tableData"  height="calc(100vh - 250px)" 
		    :defaultSort="{ prop: 'msku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
		    style="width: 100%;margin-bottom:16px;">
		   	<template #field>
			 <el-table-column label="店铺/站点" prop="groupname"  width="150"   fixed="left" >
			 		<template #default="scope">
			 			<span>{{scope.row.groupname}}-
						<span v-if="scope.row.country=='CZ'">捷克共和国</span>
						<span v-else>{{scope.row.marketname}}</span>
						</span>
			 		</template>
			 </el-table-column>
			<el-table-column label="产品信息" prop="psku"  width="250"   fixed="left" show-overflow-tooltip >
				<template #default="scope">
				<div class="flex-center">
					   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
					   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
					   <div >
						   <div  >{{scope.row.pname}}</div>
						   <p class="sku">
							   <span class="font-extraSmall">SKU:</span>
							   <span v-if="scope.row.psku">{{scope.row.psku}}</span>
							   <span v-else>{{scope.row.sku}}</span>
						   </p>
					   </div>
				</div>
				</template>
			</el-table-column>
			<el-table-column label="销量" prop="sales"  sortable="custom"  width="100"></el-table-column>
			<el-table-column label="Transfer" prop="transfer" sortable="custom" width="100"></el-table-column>
			<el-table-column  label="重量(kg)">
			<el-table-column label="电子电器" width="100">
				<template #default="scope">
					<el-input v-model="scope.row.ele_weight" clearable @input="scope.row.ele_weight=CheckInputFloat(scope.row.ele_weight)" ></el-input>
				</template>
			</el-table-column>
			<el-table-column label="电池" width="100">
				<template #default="scope">
					<el-input v-model="scope.row.power_weight" clearable @input="scope.row.power_weight=CheckInputFloat(scope.row.power_weight)" ></el-input>
				</template>
			</el-table-column>
			<el-table-column label="包装wrapper" width="100">
				<template #default="scope">
					<el-input v-model="scope.row.pkg_weight" clearable @input="scope.row.pkg_weight=CheckInputFloat(scope.row.pkg_weight)" ></el-input>
				</template>
			</el-table-column>
			<el-table-column label="plastic" width="100">
				<template #default="scope">
					<el-input v-model="scope.row.plastic_weight" clearable @input="scope.row.plastic_weight=CheckInputFloat(scope.row.plastic_weight)" ></el-input>
				</template>
			</el-table-column>
			</el-table-column>
			<el-table-column label="操作" width="80" >
				<template #default="scope">
					 <el-button link type="primary" @click="saveItem(scope.row)">保存</el-button>
				</template>
			</el-table-column>
			<el-table-column label="电子电器用量">
			<el-table-column label="Sales用量" prop="weee_sales">
				<template #default="scope">
					 <span >{{scope.row.weee_sales}}</span>
				</template>
			</el-table-column>
			<el-table-column label="FC用量" prop="weee_fc">
				<template #default="scope">
					 <span  >{{scope.row.weee_fc}}</span>
				</template>
			</el-table-column>
			</el-table-column>
				<el-table-column label="电池用量">
			<el-table-column label="Sales用量" prop="ele_sales">
				<template #default="scope">
					 <span >{{scope.row.ele_sales}}</span>
				</template>
			</el-table-column>
			<el-table-column label="FC用量"  prop="ele_fc">
				<template #default="scope">
					 <span  >{{scope.row.ele_fc}}</span>
					 
				</template>
			</el-table-column>
			</el-table-column>
			<el-table-column label="包装wrapper">
			<el-table-column label="Sales用量" prop="pkg_sales">
				<template #default="scope">
					 <span >{{scope.row.pkg_sales}}</span>
				</template>
			</el-table-column>
			<el-table-column label="FC用量"  prop="pkg_fc">
				<template #default="scope">
					 <span  >{{scope.row.pkg_fc}}</span>
					 
				</template>
			</el-table-column>
			</el-table-column>
			
			<el-table-column label="plastic用量">
			<el-table-column label="Sales用量" prop="plastic_sales">
				<template #default="scope">
					 <span >{{scope.row.plastic_sales}}</span>
				</template>
			</el-table-column>
			<el-table-column label="FC用量"  prop="plastic_fc">
				<template #default="scope">
					 <span  >{{scope.row.plastic_fc}}</span>
					 
				</template>
			</el-table-column>
			</el-table-column>
			 
			</template>
			</GlobalTable>
        </el-card>
      </el-col>
     
    </el-row>
  </div>
  <!-- 上传文件弹框 -->
  <UploadRpt ref="uploadRptRef"></UploadRpt>
  <UploadSalesDialog ref="uploadSalesDialogRef" @upload="handleUploadSales"></UploadSalesDialog>
  <uploadDialog ref="uploadDialogRef" @upload="handleUploadWeight"></uploadDialog>
</template>
<script>
	export default{ name:"电子电器用量" };
</script>
<script setup>
import { reactive, toRefs ,ref} from 'vue';
import { WarningFilled ,ArrowDown} from '@element-plus/icons-vue';
import Group from '@/components/header/group.vue';
import Datepicker from '@/components/header/datepicker.vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import UploadRpt from '@/components/Upload/uploadRpt.vue';
import DataDialog from "./components/data_dialog.vue";
import UploadSalesDialog from "./components/uploadSalesDialog.vue";
import uploadDialog from '@/components/Upload/uploadDialog.vue';
import {CheckInputFloat,CheckInputInt,getValue,formatFloat} from '@/utils/index.js';
import electronicsApi from "@/views/customized/chelsea/api/electronics/electronics.js";
      const globalTable=ref();
	  const uploadRptRef=ref();
	  const dataDialogRef=ref();
	  let materialDailogRef=ref();
	  const uploadSalesDialogRef=ref();
	  const uploadDialogRef=ref();
	  const state = reactive({
	    queryParams: {search:""}, 
		tableData:{records:[],total:0},
		nowrow:{},
		downParams:{},
		isload:false,
	  });
	  const { queryParams,tableData,nowrow,downParams,isload,} = toRefs(state);
	  function groupChange(obj){
			state.queryParams.groupid=obj.groupid;
			state.queryParams.marketplaceid=obj.marketplaceid;
			handleQuery();
	  }
	  function saveItem(row){
		  var data={};
		  data.sku=row.sku;
		  data.powerWeight=row.power_weight;
		  data.pkgWeight=row.pkg_weight;
		  data.eleWeight=row.ele_weight;
		  data.plasticWeight=row.plastic_weight;
		  electronicsApi.saveItem(data).then((res)=>{
			  row.weee_sales=formatFloat(row.power_weight*row.sales);
			  row.weee_fc=formatFloat(row.power_weight*row.transfer);
			  row.ele_sales=formatFloat(row.ele_weight*row.sales);
			  row.ele_fc=formatFloat(row.ele_weight*row.transfer);
			  row.pkg_sales=formatFloat(row.pkg_weight*row.sales);
			  row.pkg_fc=formatFloat(row.pkg_weight*row.transfer);
			  row.plastic_sales=formatFloat(row.plastic_weight*row.sales);
			  row.plastic_fc=formatFloat(row.plastic_weight*row.transfer);
			  ElMessage.success("操作成功!");
		  });
	  }
	  
	  function handleQuery(){
		   globalTable.value.loadTable(state.queryParams);
	  }
	  function loadTableData(params){
		  electronicsApi.getProductInfoList(params).then(res=>{
				 state.tableData.records=res.data.records;
				 state.tableData.total=res.data.total;
				 if(state.isload==false){
					state.isload=true;
				 }
		  })
	  }
	  
	  //日期改变
	  function changedate(dates){
	  	state.queryParams.fromDate=dates.start;
	  	state.queryParams.toDate=dates.end;
		if(state.isload==true){
			handleQuery();
		}
	  }
	 
	  function downloadList(){
		  electronicsApi.downloadElectronicsExcel(state.queryParams);
	  }
	 
	 function showupload(ftype){
		 uploadRptRef.value.show(ftype);
	 }
	 function showSalesUpload(type){
		 if(type=="saleseee"){
			 var param={"template":type,"type":type,title:"上传EEE销量",action:"/chelsea/api/v1/chelsea/ship/downExcelTemp"};
			 uploadSalesDialogRef.value.show(param)
		 }else{
			var param={"template":type,"type":type,title:"上传FC销量",action:"/chelsea/api/v1/chelsea/ship/downExcelTemp"};
			uploadSalesDialogRef.value.show(param) 
		 }
		
	 }
	 function showWeightUpload(){
		 var param={"template":"electronics",title:"上传重量",action:"/chelsea/api/v1/chelsea/ship/downExcelTemp"};
		 uploadDialogRef.value.show(param);
	 }
	 function handleUploadSales(formData){
		 electronicsApi.uploadSalesExcel(formData).then(res=>{
		 				ElMessage.success("操作成功!");
		 				handleQuery();
		 })
	 }
	 function handleUploadWeight(formData){
			 electronicsApi.uploadElectronicsExcel(formData).then(res=>{
				ElMessage.success("操作成功!");
				handleQuery();
			 })
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
