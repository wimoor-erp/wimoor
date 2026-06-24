<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="24" :xs="24">
        <el-card   shadow="always">
			<el-row>
			<el-space>
				 <Group  @change="groupChange" defaultValue="only"  ></Group>
				  <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" placeholder="搜索ASIN或平台SKU"></el-input>
		  </el-space>
		  <div class='rt-btn-group'>
			<el-button type="info" @click.stop="openUpload">导入</el-button>
			<el-button type="primary" @click.stop="downloadVisable=true">导出</el-button>
		  </div>
		  </el-row>
		  
		   <GlobalTable ref="globalTable"
		    :tableData="tableData"  height="calc(100vh - 213px)" 
		    :defaultSort="{ prop: 'psku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
		    style="width: 100%;margin-bottom:16px;">
		   	<template #field>
		     <el-table-column label="平台商品信息" prop="psku"    sortable="custom" show-overflow-tooltip >
		     	<template #default="scope">
		     	<div class="flex-center">
		     		   <el-image v-if="scope.row.pimage" :src="scope.row.pimage" class="img-40"  width="40" height="40"  ></el-image>
		     		   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
		     		   <div >
		     			   <div  >{{scope.row.pname}}</div>
		     			   <p class="sku">{{scope.row.psku}} 
						   <span class="font-extraSmall"> ASIN:{{scope.row.asin}}</span>
		     			   </p>
		     		   </div>
		     	</div>
		     	</template>
		     </el-table-column>
			 <el-table-column label="FBA库存" prop="quantity"  width="120"    >
			 		<template #default="scope">
			 			   <div v-if="scope.row.fbaquantity">{{scope.row.fbaquantity}}</div>
			 				<div v-else>0</div>
			 		</template>
			 </el-table-column>
			 <el-table-column label="店铺/站点" prop="groupname"  width="150"    >
			 		<template #default="scope">
			 			<span>{{scope.row.groupname}}-{{scope.row.marketname}}</span>
			 		</template>
			 </el-table-column>
			<el-table-column label="本地产品信息" prop="psku"   show-overflow-tooltip >
				<template #default="scope">
				<div class="flex-center">
					   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
					   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
					   <div >
						   <div  >{{scope.row.name}}</div>
						   <p class="sku"><span class="font-extraSmall">SKU:</span>{{scope.row.sku}} 
						   <el-tag v-if="scope.row.issfg=='1'" type="success">组合</el-tag>
						   </p>
					   </div>
				</div>
				</template>
			</el-table-column>

			 <el-table-column label="本地库存" prop="fulfillable"  width="120"    >
				 	<template #default="scope">
				           <div v-if="scope.row.fulfillable">{{scope.row.fulfillable}}</div>
						    <div v-else>0</div>
				    </template>
			 </el-table-column>
			 <el-table-column label="采购价格" prop="price"  width="100"    />
			  <el-table-column label="重量(kg)" prop="pkgweight"  width="100"    />
			 <el-table-column fixed="right" prop="sku" label="操作"  width="140" >
			     <template #default="scope">
			         <el-button   type="primary" link @click="handleBind(scope.row)">重新绑定</el-button>
					 <div v-if="!scope.row.omsku" class="font-extraSmall">
						 系统自动绑定MSKU
					 </div>
			     </template>
			 </el-table-column>
			</template>
			</GlobalTable>
        </el-card>
      </el-col>
     
    </el-row>
  </div>
  <!-- 子产品选择弹窗 -->
   <MaterialDialog ref="materialDailogRef" @getdata="getRows" type="product" ></MaterialDialog>
   <el-dialog title="平台SKU与本地SKU关系导出" v-model="downloadVisable" width="60%">
	   <Group  @change="downGroupChange" defaultValue=""  ></Group>
	   
	   <template #footer>
	   		<el-button type="info" @click="downloadVisable=false">关闭</el-button>
			<el-button type="primary" @click="downloadData">下载</el-button>
	   </template>
   </el-dialog>
   <UploadDialog ref="uploadDialogRef" @upload="handleUpload"></UploadDialog>
</template>
<script>
	export default{ name:"SKU配对" };
</script>
<script setup>
 
import { reactive, toRefs ,ref} from 'vue';
import { WarningFilled } from '@element-plus/icons-vue';
import Group from '@/components/header/group.vue';
import { ElMessage, ElMessageBox } from 'element-plus'
import listApi from '@/api/amazon/listing/listingApi.js';
import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
import UploadDialog from '@/components/Upload/uploadDialog.vue';
import ProductInOptApi from "@/api/amazon/product/productinoptApi.js";
      const globalTable=ref();
	  let materialDailogRef=ref();
	  let uploadDialogRef=ref();
	  const state = reactive({
	    queryParams: {search:""}, 
		tableData:{records:[],total:0},
		nowrow:{},
		downloadVisable:false,
		downParams:{},
	  });
	  const { queryParams,tableData,nowrow,downloadVisable,downParams } = toRefs(state);
	  function groupChange(obj){
	  			state.queryParams.groupid=obj.groupid;
	  			state.queryParams.marketplaceid=obj.marketplaceid;
	  			handleQuery();
	  }
	  function downGroupChange(obj){
		  state.downParams.groupid=obj.groupid;
		  state.downParams.marketplaceid=obj.marketplaceid;
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
	  function handleBind(row){
		  state.nowrow=row;
		  materialDailogRef.value.show();
	  }
	  function getRows(rows){
		  if(rows&&rows.length==1){
			  var sku=rows[0].sku;
			  ProductInOptApi.updateOptMsku({"pid":state.nowrow.pid,"msku":sku}).then((res)=>{
				  if(res.data=="isok"){
					  state.nowrow.sku=rows[0].sku;
					  state.nowrow.issfg=rows[0].issfg;
					  state.nowrow.price=rows[0].price;
					  state.nowrow.pkgweight=rows[0].weight;
					  state.nowrow.image=rows[0].image;
					  state.nowrow.name=rows[0].name;
					  state.nowrow.fulfillable=rows[0].fulfillable;
					  ElMessage.success("绑定成功！");
				  }else{
					  ElMessage.error( "绑定失败！");
				  }
			  });
			  
		  }else{
			  ElMessage.error("只能选择一个SKU进行绑定！");
		  }
	  }
	  function downloadData(){
		  var groupid=state.downParams.groupid;
		  var marketplaceid=state.downParams.marketplaceid;
		  if(state.downParams.groupid==""){
			  groupid="all";
		  }
		  if(state.downParams.marketplaceid==""){
		  	  marketplaceid=null;
		  }
		  ProductInOptApi.downExcelMSKUData({"groupid":groupid,"marketplaceid":marketplaceid});
	  }
	 function openUpload(){
	 	uploadDialogRef.value.show({action:"/amazon/api/v1/report/product/productInOpt/downMskuExcelFeeTemp",'template':"mskuTemplate",'title':"本地SKU导入"});
	 }
	 function handleUpload(formDatas){
	 	ProductInOptApi.uploadMskuFile(formDatas).then(res=>{
			ElMessage.success("上传成功！");
	 		handleQuery();
	 		uploadDialogRef.value.hide();
	 	});
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
