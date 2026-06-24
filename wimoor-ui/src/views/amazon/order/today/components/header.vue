<template>
	 
	  <el-row>
	    <el-space >
	  <Group @change="groupChange" defaultValue="only"/>
	  <el-select v-model="orderType" @change="refreshType" placeholder="全部状态" clearable >
	        <el-option  v-for="item in orderTypeList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	    <el-select v-model="customer" @change="refreshCustomer" placeholder="客户类型" clearable >
	        <el-option  v-for="item in customerList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	  <el-select v-model="channel" @change="refreshChannel" placeholder="发货方式" clearable >
	        <el-option  v-for="item in channelList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	   <el-input  v-model="searchKeywords" clearable placeholder="请输入" class="input-with-select" >
	      <template #prepend>
	        <el-select v-model="searchtype" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="ASIN" value="asin"></el-option>
	          <el-option label="订单号" value="number"></el-option>
	        </el-select>
	      </template>
	      <template #append>
	        <el-button @click="searchConfirm">
	           <el-icon style="font-size: 16px;align-itmes:center">
	            <search />
	         </el-icon>
	        </el-button>
	      </template>
	    </el-input>
	   <el-popover   :popper-append-to-body="false" v-model:visible="moreSearchVisible" :width="400" trigger="click">
	         <template #reference>
	           <el-button  title='高级筛选'  class='ic-btn'>
	           <menu-unfold theme="outline" size="16"  :strokeWidth="3"/>
	           </el-button>
	         </template>
			  <el-form :model="form" label-width="80px">
			  <el-form-item label="颜色">
			       <el-select v-model="color"  :popper-append-to-body="false" placeholder="颜色选择" @change="colorChange">
			       <el-option
			             v-for="item in colorList"
			             :key="item.value"
			             :label="item.label"
			             :value="item.value"
			           >
					   <div class="color-select">
			             <span >{{ item.label }}</span>
			             <span :class="'cilcle-'+item.value"></span  >
						</div> 
			           </el-option>
			       </el-select>
			     </el-form-item>
				  <el-form-item>
				       <el-button type="primary" @click.stop="submitForm(formRef)">搜索</el-button>
				       <el-button @click="moreSearchVisible=false">取消</el-button>
				     </el-form-item>
				</el-form>
	       </el-popover>
	    <!-- <el-button>重置</el-button> -->
	  </el-space>
	  </el-row>
	   <!--功能区域 -->
	 
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue';
	import Group from '@/components/header/group.vue';
	 const emit = defineEmits(['getdata']);
	 let state=reactive({
		queryParams:{},
		orderType:"",
	 	orderTypeList:[{"id":"","name":"全部订单状态"},{"id":"Unshipped","name":"Unshipped"},{"id":"Shipped","name":"Shipped"},
		{"id":"Canceled","name":"Canceled"},{"id":"Pending","name":"Pending"}],
		customer:"",
		customerList:[{"id":"","name":"全部客户类型"},{"id":"0","name":"个人"},{"id":"1","name":"企业"}],
		channel:"",
		channelList:[{"id":"","name":"全部发货方式"},{"id":"Amazon","name":"亚马逊发货"},{"id":"Merchant","name":"卖家自发货"}],
		color:"",
		colorList:[
			{label:"蓝色",value:"blue"},
			{label:"红色",value:"red"},
			{label:"绿色",value:"green"},
			{label:"黄色",value:"yellow"},
			{label:"灰色",value:"gray"},
			{label:"黑色",value:"black"},
			{label:"橙色",value:"orange"},
		],
		searchtype:"sku",
		searchKeywords:"",
		timeData:{},
		moreSearchVisible:false,
	 });
	
	 // const props = defineProps({
	 //   msg: String,
	 // });
	
	 const {
		 orderType,
		 orderTypeList,
		 customer,
		 customerList,
		 channel,
		 channelList,
		 color,
		 colorList,
		 queryParams,
		 searchKeywords,
		 searchtype,
		 moreSearchVisible,
		 timeData,
	 } = toRefs(state);
     function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		state.queryParams.status="";
		state.queryParams.isbusiness="";
		state.queryParams.channel="";
		state.queryParams.color="";
		state.queryParams.search="";
		state.queryParams.searchtype="sku";
		state.queryParams.isorder="isproduct";
		changeData();
	 }
	 function refreshType(val){
		 state.queryParams.status=val;
		 changeData();
	 }
	 function refreshCustomer(val){
		 state.queryParams.isbusiness=val;
		 changeData();
	 }
	 function refreshChannel(val){
		 state.queryParams.channel=val;
		 changeData();
	 }
	 function colorChange(val){
		 state.queryParams.color=val;
		 changeData();
	 }
	 function submitForm(){
		 changeData();
	 }
	 function searchConfirm(){
		 changeData();
	 }
	 function searchTypeChange(val){
		 state.queryParams.searchtype=val;
		 changeData();
	 }
	 function changeData(){
		 state.queryParams.search=state.searchKeywords;
		 emit('getdata',state.queryParams);
	 }
</script>

<style>
	.cilcle-red{
		width:12px;
		height:12px;
		background-color:#f56c6c;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-yellow{
		width:12px;
		height:12px;
		background-color:#e6a23c;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-green{
		width:12px;
		height:12px;
		background-color:#67c23a;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-blue{
		width:12px;
		height:12px;
		background-color:#00aaff;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-gray{
		width:12px;
		height:12px;
		background-color:#606266;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-black{
		width:12px;
		height:12px;
		background-color:#000000;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-orange{
		width:12px;
		height:12px;
		background-color:#ffa400;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.pad2{
		margin-left: 5px;
	}
</style>
