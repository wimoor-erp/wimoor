<template>
	<div   class="main-sty">
		  <el-row>
			  	<div class="con-header" style="margin-bottom:20px;">
					<el-space>
					   <Group @change="groupChange" defaultValue="only"/>
					   <el-input  style="width:500px;" v-model="queryParam.search" clearable  placeholder="请输入用;分割" class="input-with-select" >
					   <template #prepend>
					   	<el-select v-model="queryParam.locale" placeholder="站点语言" style="width: 95px">
					   		<el-option label="站点语言" value=""></el-option>
					   		<el-option label="英文" value="en_US"></el-option>
					   		<el-option label="中文" value="zh_CN"></el-option>
					   	</el-select>
					   </template>
					   </el-input>
					   <el-button @click.stop="handleQuery()" :loading="searchLoading">搜索</el-button>
					   <el-button @click.stop="handleDownload()"> 导出</el-button>
				   </el-space>
		   	    </div>
				<div class='rt-btn-group'  style="padding-top:5px;">
					<div  class ="text-blue pointer" style="font-size:12px" @click.stop="handleShowDailog">总计：{{tableData.numberOfResults}} 条记录</div>
				</div>
		   </el-row>
		<el-scrollbar style="height: calc(100vh - 158px);">
      	<el-row v-if="tableData&&tableData.numberOfResults">
			<el-col
				  v-for="(item,index) in tableData.items"
			      :key="item.asin"
			      :span="4"
			      
			    >
			      <el-card :body-style="{ padding: '0px',height:'450px'}" style="margin:5px;">
							  <el-image
							        style="width:240px; height:240px"
							        :src="item.mainimage"
							        :zoom-rate="1.2"
							        :preview-src-list="item.subimages"
							        :initial-index="4"
							        fit="cover"
							      />
					<div style="padding: 14px">
			        <div v-if="item.summaries" v-for="summarie in item.summaries" >
			          <el-tooltip :content="summarie.itemName">
						   <div class="text-omit">{{summarie.itemName}}</div>
					  </el-tooltip>
			          <div class="bottom">
			            <div class="font-extraSmall">品牌:{{summarie.brand}}</div>
			             <div class="font-extraSmall"
						 v-if="summarie.browseClassification&&summarie.browseClassification.displayName"
						 >分类:{{summarie.browseClassification.displayName}}</div>
			          
					  </div>
			        </div>
					<div  v-if="item.salesRanks" v-for="salesRank in item.salesRanks">
						<div class="font-extraSmall" >排名：</div>
						<div  class="font-extraSmall" v-for="rank in salesRank.classificationRanks"> 
							 <el-link  :href="rank.link" target="_blank">{{rank.title}}</el-link>
							:{{rank.rank}}位
						</div>
						<div v-for="rank in salesRank.displayGroupRanks" class="font-extraSmall"> 
					       <el-link  :href="rank.link" target="_blank">{{rank.title}}</el-link>
							:{{rank.rank}}位
						</div>
					</div>
					<div style="padding-top:10px" >ASIN:{{item.asin}}</div>
					</div>			  
			      </el-card>
			    </el-col>
	    </el-row>
		 <el-empty v-else description="暂无数据" />
		</el-scrollbar>
		<el-row>
		 
				<div class="font-extraSmall"  style="padding-top:5px;">总计：{{tableData.numberOfResults}} 条记录</div>
					<div class='rt-btn-group'  style="padding-top:5px;">
				 <el-button-group>
								<el-button type="primary" @click.stop="handleQuery(queryParam.previousToken)" :icon="ArrowLeft" :disabled="!queryParam.previousToken">上一页</el-button>
								<el-button type="primary" @click.stop="handleQuery(queryParam.nextToken)"  :disabled="!queryParam.nextToken">
								  下一页<el-icon class="el-icon--right" ><ArrowRight /></el-icon>
								</el-button>
				</el-button-group>
				</div>
		 
			  
		</el-row>
 </div>
  <el-dialog
     v-model="dialogVisible"
     title="在结果中查找"
     width="30%"
     :before-close="handleClose"
   >
   <el-form :model="form" label-width="120px">
       <el-form-item label="品牌" v-if="tableData.refinements&&tableData.refinements.brands">
         <el-checkbox-group v-model="queryParam.brandNames">
             <el-checkbox v-for="brand in tableData.refinements.brands" :label="brand.brandName" >
         	   {{brand.brandName}}<span class="font-extraSmall">({{brand.numberOfResults}}条)</span>
         	</el-checkbox>
           </el-checkbox-group>
       </el-form-item>
       <el-form-item label="分类" v-if="tableData.refinements&&tableData.refinements.classifications">
         <el-checkbox-group v-model="queryParam.classificationIds">
             <el-checkbox v-for="classification in tableData.refinements.classifications" :label="classification.classificationId">
         		   {{classification.displayName}}<span class="font-extraSmall">({{classification.numberOfResults}}条)</span>
         		   </el-checkbox>
           </el-checkbox-group>
       </el-form-item>
     </el-form>
  
	 
     <template #footer>
       <span class="dialog-footer">
         <el-button @click="confirmQuery(0)">清除</el-button>
         <el-button type="primary" @click="confirmQuery(1)"> 确认  </el-button>
       </span>
     </template>
   </el-dialog>
</template>
<script>
    export default{ name:"商品搜索" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowLeft,ArrowRight} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import reportApi from '@/api/amazon/inbound/reportApi.js';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Datepicker from '@/components/header/datepicker.vue';
	import Group from '@/components/header/group.vue';
	import listingApi from '@/api/amazon/listing/listingApi.js';
	import transform from '@/hooks/amazon/listing/download.js';
	let state=reactive({
		 queryParam:{
			 search:"",
			 marketplaceid:"",
			 groupid:"",
		 },
		 dialogVisible:false,
		 searchLoading:false,
		 tableData:{numberOfResults:0},
	});
	let {
	   queryParam,
	   tableData,
	   dialogVisible,
	   searchLoading,
	} =toRefs(state);
	 function groupChange(data){
		state.queryParam.groupid=data.groupid;
		state.queryParam.marketplaceid=data.marketplaceid;
	 }
	 function handleShowDailog(){
		 state.dialogVisible=true;
	 }
	 function confirmQuery(value){
		 state.dialogVisible = false;
		 if(value==0){
			 state.queryParam.brandNames=[];
			 state.queryParam.classificationIds=[];
		 }
		 handleQuery();
	 }
	 function handleDownload(){
		 transform(state.tableData,"asinReport",()=>{
			 ElMessage.success('导出成功');
		 })
	 }
	 function handleQuery(token){
		 state.queryParam.marketplaceIds=[state.queryParam.marketplaceid];
		 if(!state.queryParam.search){
			 ElMessage.error('请输入搜索关键词');
			 return;
		 }
		 if(state.queryParam.locate){
		 	   state.queryParam.keywordsLocale=state.queryParam.search;
		 }else{
			  var mykeys=state.queryParam.search.split(";");
			  state.queryParam.keywords=mykeys;
		 }
		
		 state.queryParam.pageSize=18;
		 if(token){
			 state.queryParam.pageToken=token;
		 }
		  state.searchLoading=true;
		 listingApi.searchCatalogProducts(state.queryParam).then(res=>{
			 state.searchLoading=false;
			 if(res.data){
				 res.data.items.forEach(item=>{
					if(item.images&&item.images.length>0){
						var marketImages=item.images[0];
						item.subimages=[];
						marketImages.images.forEach(image=>{
							if(image.variant=='MAIN'&&image.height==500){
								item.mainimage=image.link;
							}
							if(!item.mainimage){
								item.mainimage=image.link;
							}
							if(image.height==2000){
								item.subimages.push(image.link);
							}
						})
					}
				 })
			 }
			 state.tableData=res.data;
			 state.queryParam.nextToken=null;
			 state.queryParam.previousToken=null;
			 if(res&&res.data&&res.data.pagination&&res.data.pagination.nextToken){
				 state.queryParam.nextToken=res.data.pagination.nextToken;
			 }
			 if(res&&res.data&&res.data.pagination&&res.data.pagination.previousToken){
				 state.queryParam.previousToken=res.data.pagination.previousToken;
			 }
		 })
	 }
  
	 
</script>

<style>
	.text-omit{
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp:3;
		line-clamp:3;
		font-size:12px;
		-webkit-box-orient: vertical;
    }
</style>
