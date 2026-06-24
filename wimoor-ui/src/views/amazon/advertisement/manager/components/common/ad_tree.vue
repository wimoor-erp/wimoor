<template>
	<div class="tree-content" :class="{'isHidden':!leftAside}">
	<div v-show="leftAside" >
	<div class="ad-tree el-white-bg" >
		<div class="ad-head">
			<AdGroup :border="false" @change="changeGroup"/>
		</div>
	  <div class="ad-body">
		  <el-tabs v-model="adtype">
		      <el-tab-pane   @click="handleChangeAdtype(1)" :name="0">
				  <template #label>
					  广告活动
				  </template>
			  </el-tab-pane>
		      <el-tab-pane  @click="handleChangeAdtype(0)" :name="1">
				  <template #label>
				    广告组合 
				  </template>
				 </el-tab-pane>
		    </el-tabs>
			 <el-button
			 class="m-t-8"
			     @click.stop="handlePortfoliosAdd" 
			      v-show="adtype===1"
			      type="primary"
			      link
			      >
				  <el-icon title="添加广告组合"
				  style="margin-right:8px;" 
				  class="font-bold "><Plus /></el-icon>
				  添加广告组合
				  </el-button>
			   <el-radio-group 
				v-show="adtype===0"
				v-model="adverttype" @change="handleType" size="small">
			         <el-radio-button label="商品推广" />
			         <el-radio-button label="品牌推广" />
			         <el-radio-button label="展示推广" />
			    </el-radio-group>
	
		  <div class="m-16">
				 <el-input
				   v-show="adtype===0"
				   v-model="queryParams.name" clearable @change="loadTreeData" placeholder="搜索广告活动..." >
					 <template #suffix>
					  <el-icon @click.stop="loadTreeData"><Search /></el-icon>
					 </template>
					  <template  #prepend>
					         <el-select   v-model="queryParams.campaignStatus" @change="loadTreeData" placeholder="状态" style="width:75px">
					           <el-option label="开启" value="enabled" />
					           <el-option label="暂停" value="paused" />
					           <el-option label="归档" value="archived" />
							   <el-option label="全部" value="all" />
					         </el-select>
					 </template>
				 </el-input>
				 <el-input 
				   v-show="adtype===1"
				  v-model="filterText" clearable  placeholder="搜索广告组合..." >
					 <template #suffix>
					  <el-icon ><Search /></el-icon>
					 </template>
				 </el-input>
		 </div>
		 <el-scrollbar height="calc(100vh - 302px)">
		<!-- 广告活动	 -->
		 <el-tree
		  empty-text="无数据或加载中"
		  v-if="adtype==0"
		  :data="treeData"
		   node-key="id"
		   :default-expanded-keys="[1]"
		  @node-click="changeData" 
		  @node-expand="handleNodeClick">
		  <template #default="scope">
			  <div class="item-node ">
				  <i v-if="scope.node.level>0" class="dot-state" 
				  :class="scope.data.state==='enabled'||scope.data.state==='ENABLED'?'dot-green':'dot-red'"
				  ></i>
				    <el-tooltip :content="scope.node.label" placement="top" :show-after="500">
				      <span class="text-omit-1 whiteS">{{scope.node.label}}</span>
				    </el-tooltip>
			  </div>
		  </template>
		  </el-tree>
		 
		 <!-- 广告组合 -->
		 <el-tree
		  v-else
		  ref="adPRef"
		  :data="adGroupsData"
		   node-key="id"
		   :default-expand-all="true"
		   :indent="0"
		   :filter-node-method="filterNode"
		   :default-expanded-keys="[1]"
		  @node-click="handlePoloClick" >
		  <template #default="scope">
		 			  <div class="item-node flex-center-between">
						 <i v-if="scope.node.level>1" class="dot-state"
						 :class="scope.data.state==='enabled'||scope.data.state==='ENABLED'?'dot-green':'dot-red'"
						 ></i> 
		 				 <el-tooltip :content="scope.node.label" placement="top" :show-after="500">
		 				   <span class="text-omit-1 whiteS">{{scope.node.label}}</span>
		 				 </el-tooltip>
		 			  </div>
		  </template>
		  </el-tree>
		  </el-scrollbar>
		  <div class="pag-wrap">
		  <pagination
		    style="padding:0px;"
		    v-if="queryParams.total > 0&&adtype==0"
		    :total="queryParams.total"
		  			layout=" prev, next"
		    v-model:page="queryParams.currentpage"
		    v-model:limit="queryParams.pagesize"
		    @pagination="loadTreeData"
		  />
		  </div>
	  </div>
	  </div>
	  </div>
	  <div class="expend-btn-tree" >
		 <el-icon
		 @click="leftAside=!leftAside"
		 ><ArrowLeftBold /></el-icon>
	  </div>
	  </div>
	  
	  <el-dialog
	  v-model="visable"
	  title="创建广告组合"
	  width="600px"
	  >
	  <el-form v-model="formData" label-position="top">
		  <el-form-item label="广告组合名称">
			  <el-input style="width:40%" v-model="formData.name"></el-input>
		  </el-form-item>
		  <el-form-item label="预算类型">
			    <el-radio-group v-model="formData.inbudget" >
			        <el-radio  :label="true">无预算上限</el-radio>
			        <el-radio  :label="false">日期范围预算上限</el-radio>
			      </el-radio-group>
		  </el-form-item>
		  <el-form-item v-if="formData.inbudget==false" label="预算上限">
		  			  <el-input style="width:40%"  v-model="formData.amount" @input="formData.amount=CheckInputFloat(formData.amount)"></el-input>
		  </el-form-item>
		  <el-form-item v-if="formData.inbudget==false" label="日期类型">
		  			    <el-radio-group v-model="formData.policy" >
		  			        <el-radio label="dateRange" >日期区间</el-radio>
		  			        <el-radio label="monthlyRecurring">每月</el-radio>
		  			      </el-radio-group>
		  </el-form-item>
		  <el-form-item v-if="formData.inbudget==false&&formData.policy=='dateRange'" label="开始时间">
		  			 <el-date-picker
		  			           v-model="formData.startdate"
		  			           type="date"
							   :editable="false"
		  			           placeholder="开始时间"
		  			         />
		  </el-form-item>
		  <el-form-item v-if="formData.inbudget==false&&formData.policy=='dateRange'" label="结束时间">
		  			 <el-date-picker
		  			           v-model="formData.enddate"
		  			           type="date"
							   :editable="false"
		  			           placeholder="结束时间"
		  			         />
		  </el-form-item>
	  </el-form>
	 <div class="text-right pdbm20" >
		 <el-button @click="visable=false">取消</el-button>
		 <el-button type="primary" v-if="formData.id" @click="submitPortfolios">保存</el-button>
		 <el-button type="primary" v-else @click="submitPortfolios">新增</el-button>
	 </div>
		   <el-table :data="state.adGroupsData[0].children">
			   <el-table-column label="名称" prop="name"></el-table-column>
			   <el-table-column label="状态" prop="state">
				   <template #default="scope">
				       <el-tag v-if="scope.row.state=='enabled'" type="success">开启</el-tag>
					   <el-tag v-if="scope.row.state=='paused'" type="warning">暂停</el-tag>
					   <el-tag v-if="scope.row.state=='archived'" type="danger">归档</el-tag>
				   	</template>
			   </el-table-column>
			   <el-table-column label="操作" prop="state">
				   <template #default="scope">
				       <el-button link type="primary" @click="handlePortfolios(scope.row)">编辑</el-button>
					  <!--software developer test place delete
					   <el-button link type="danger"  @click="handlePortfolios(scope.row,'archived')">归档</el-button>
					   <el-button link type="warning" @click="handlePortfolios(scope.row,'paused')">暂停</el-button> -->
				   	</template>
			   </el-table-column>
		   </el-table>
	  </el-dialog>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowLeftBold,Plus} from '@element-plus/icons-vue'
	import AdGroup from '@/components/header/ad_group.vue';
	import '@/assets/css/switch_button.css';
	import {  ElMessage,ElMessageBox,  } from 'element-plus';
	import {CheckInputFloat,CheckInputInt,formatFloat} from '@/utils/index.js';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import advAdgroupApi from '@/api/amazon/advertisement/report/advAdgroupApi.js';
	import portfoliosApi from '@/api/amazon/advertisement/portfolios/portfoliosApi.js'; 
	const emit = defineEmits(['change',]);
	const filterText = ref('')
	const adPRef = ref()
	const state = reactive({
		adtype:0,
		leftAside:true,
		visable:false,
		queryParams:{
			name:'',
			total:0,
			pagesize:18,
			campaignStatus:'enabled',
			campaignType:"SP",
		},
		formData:{
			name:'',
			inbudget:true,
			state:"enabled",
			policy:"dateRange",
		},
 
		adGroupsData:[
		],
		treeData:[
		],
		adverttype:'商品推广',
	})
	
	const{
		btnData,
		adtype,
		treeData,
		leftAside,
		adGroupsData,
		visable,
		formData,
		queryParams,
		adverttype,
	}=toRefs(state)
	
	watch(filterText, (val) => {
	  adPRef.value.filter(val)
	})
	
	const filterNode=(value,data)=>{
		if(!value) return true;
		return data.label.includes(value)
	}
	function handlePoloClick(row){
		state.queryParams.campaignid=null;
		state.queryParams.adgroupid=null;
		state.queryParams.portfoliosid=row.id;
		state.queryParams.poloname=row.name;
		emit("change",state.queryParams);
	}
	function handleType(){
		var ftype="SP";
		if(state.adverttype=="商品推广"){
			ftype="SP";
		}else if(state.adverttype=="品牌推广"){
			ftype="SB";
		}else{
			ftype="SD";
		}
		state.queryParams.campaignType=ftype;
		state.queryParams.adgroupid=null;
		state.queryParams.adgroupname=null;
		state.queryParams.campaignid=null;
		state.queryParams.camname=null;
		loadTreeData();
		emit("change",state.queryParams);
	}
	function changeData(row){
		state.queryParams.portfoliosid=null;
		if(row.level==1){
			state.queryParams.adgroupid=null;
			state.queryParams.adgroupname=null;
			state.queryParams.campaignid=row.campaignid;
			state.queryParams.camname=row.name;
		}
		if(row.level==2){
			state.queryParams.campaignid=row.campaignid;
			state.queryParams.camname=row.camname;
			state.queryParams.adgroupid=row.adgroupid;
			state.queryParams.adgroupname=row.name;
		}
		emit("change",state.queryParams);
	}
	function handlePortfoliosAdd(){
			state.formData={
				name:'',
				inbudget:true,
				state:"enabled",
				policy:"dateRange",
			}
			state.visable=true;
	}
	function handlePortfolios(row,status){
       if(status){
		   ElMessageBox.confirm('确认'+status+'此记录?', '警告', {
		   		confirmButtonText: '确定',
		   		cancelButtonText: '取消',
		   		type: 'warning',
		   }).then(() => {
		   	  var data=JSON.parse(JSON.stringify(row));
		   	  data.state=status;
		   	  portfoliosApi.updatePortfolios(data).then(res=>{
		   		loadPofoData();
		   		ElMessage.success('操作成功');
		   	  })
		   	}).catch(() => ElMessage.info('已取消'));
	   }else{
		state.formData=JSON.parse(JSON.stringify(row));
	   }
	}
 
	function handleNodeClick(row){
		advAdgroupApi.loadAdGroup({"profileid":row.profileid,"campaignsid":row.campaignid,"campaignType":state.queryParams.campaignType}).then((res)=>{
			if(res.data && res.data.length>0){
				res.data.forEach(item=>{
					item.level=2;
					item.label=item.name;
					item.id=item.adgroupid;
				});
				row.children=res.data;
			}
		});
	}
	
	function handleChangeAdtype(index){
		state.adtype = index;
	}
	function changeGroup(data){
		state.queryParams.groupid=data.groupid;
		state.queryParams.profileid=data.profileid;
		state.queryParams.adgroupid=null;
		state.queryParams.adgroupname=null;
		state.queryParams.campaignid=null;
		state.queryParams.camname=null;
		state.queryParams.ftime=data.profile.ftime;
		state.queryParams.marketname=data.profile.name;
		loadTreeData();
		loadPofoData();
		emit("change",state.queryParams);
	}
	
	function loadTreeData(){
		state.treeData=[];
		advertApi.loadCampaignsNotArchived(state.queryParams).then((res)=>{
			if(res.data && res.data.records){
				res.data.records.forEach(item=>{
					item.level=1;
					item.children=[{"label":"加载中..."}];
					item.label=item.name;
					item.id=item.campaignid;
				});
				/* console.log(res.data.records) */
				state.treeData=res.data.records;
				state.queryParams.total=res.data.total;
				//emit("change",state.queryParams);
			}
		});
	}
	function loadPofoData(){
		state.adGroupsData.push({label:'广告组合',level:1,children:[]})
		portfoliosApi.findPortfolios({"groupid":state.queryParams.groupid,"profileid":state.queryParams.profileid}).then((res)=>{
			if(res.data && res.data.length>0){
				res.data.forEach(item=>{
					item.level=2;
					item.label=item.name;
				});
				state.adGroupsData[0].children = res.data;
			}
		});
		
	}
	function submitPortfolios(){
		state.formData.profileid=state.queryParams.profileid;
		if(state.formData.id){
			portfoliosApi.updatePortfolios(state.formData).then(res=>{
				loadPofoData();
				ElMessage.success('编辑成功');
			})
		}else{
			portfoliosApi.createPortfolios(state.formData).then(res=>{
				loadPofoData();
				ElMessage.success('保存成功');
			})
		}
		
	}
</script>

<style>
	.whiteS{
		white-space:normal;
	}
	.pdbm20{
		padding-bottom:20px;
	}
	.el-tree-node__label{
		flex-grow:1;
	}
	.tree-content{
		position:relative;
		height:calc(100vh - 66px);
		width:264px;
	}
	.ad-tree{
		width:248px;
		box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
		margin-left:16px;
		position: fixed;
	}
	.expend-btn-tree{
		position:fixed;
		left: 328px;
		top:49%;
		z-index:3;
		width:24px;
		height:24px;
	}
	.isHidden .expend-btn-tree{
		left:64px;
	}
	.isHidden{
		width:inherit;
	}
	.expend-btn-tree .el-icon{
		background: #fff;
		    height: 40px;
		    width: 21px;
		    border: 2px solid #eee;
		    border-left: none;
		    padding-right: 4px;
			font-size: 12px;
			color: #333;
			font-weight:700;
		    border-bottom-right-radius: 32px;
		    border-top-right-radius: 32px;
			cursor:pointer;
	}
	.expend-btn-tree .el-icon:hover{
		color: #ff6700;
	}
	.pag-wrap{
		height:32px;
	}
	.ad-head{
		padding:8px;
		border-bottom:1px solid #eee;
	}
	.ad-body{
		padding-left:16px;
		padding-right:16px;
		padding-bottom:8px;
	}
    .m-16{
		margin-top:16px;
		margin-bottom:16px;
	}
	.dot-state{
		width:8px;
		height:8px;
		display: inline-block;
		border-radius:4px;
		margin-right:8px;
		flex:none;
	}
	.dot-green{
		background:#69d632;
	}
	.dot-red{
		background:#ff5f59;
	}
	.item-node{
		display:flex;
		align-items:center;
	}
	.ad-body .el-tree-node{
		padding-top:8px;
		padding--bottom:8px;
	}
	
 @media(max-width:1360px) {
	.ad-tree{
		position: absolute;
	}
	.expend-btn-tree{
		position: absolute;
		left:264px;
	}
	.isHidden .expend-btn-tree{
		left:0px;
	}
}	
</style>