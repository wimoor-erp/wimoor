<template>
	<div class="con-header" >
		<el-tabs v-model="orderType"  @tab-change="refreshTable">
		  <el-tab-pane  label="全部"   name=""> </el-tab-pane>
		  <el-tab-pane v-for="item in orderTypeList" :label="item.name"   :name="item.id"> </el-tab-pane>
		</el-tabs>
	  <el-row>
	    <el-space >
	  <el-select v-model="store"  @change="changeGroup"  placeholder="全部店铺">
	        <el-option  v-for="item in storeList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	  <el-select v-model="country" @change="refreshTable" placeholder="全部国家">
	        <el-option  v-for="item in countryList"   :key="item.pointName"  :label="item.name" :value="item.pointName"   >
	        </el-option>
	  </el-select>
	    <el-select v-model="customerType" @change="refreshTable" placeholder="客户类型" clearable >
	        <el-option  v-for="item in customerTypeList"   :key="item.id"  :value="item.id" :label="item.name"   >
	        </el-option>
	  </el-select>
	  <el-date-picker
	          v-model="dateValue"
	  		  @change = "dateChange"
	          type="daterange"
	  		  :clearable="false"
	          range-separator="至"
	          start-placeholder="开始日期"
	          end-placeholder="结束日期"
	          :shortcuts="shortcuts"
	        />
	   <el-input  v-model="searchKeywords" clearable placeholder="请输入" class="input-with-120" >
	      <template #prepend>
	        <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width:80px">
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
	   <el-popover   :teleported="false" v-model:visible="moreSearchVisible" :width="400" trigger="click">
	         <template #reference>
	           <el-button  title='高级筛选'  class='ic-btn'>
	           <menu-unfold theme="outline" size="16"  :strokeWidth="3"/>
	           </el-button>
	         </template>
			  <el-form :model="form" label-width="80px">
			  	 <el-form-item label="发票上传">
					 <el-select v-model="invoceType" placeholder="上传状态" clearable  :teleported="false">
					       <el-option  v-for="item in invoceTypeList"   :key="item.id"  :label="item.name" :value="item.id"   >
					       </el-option>
					 </el-select>
				 </el-form-item>	 
				 <el-form-item label="发货方式">
					 <el-select v-model="shipType" placeholder="发货方式" clearable  :teleported="false">
					       <el-option  v-for="item in shipTypeList"   :key="item.id"  :label="item.name" :value="item.id"   >
					       </el-option>
					 </el-select>
				 </el-form-item>
				  <el-form-item>
				       <el-button type="primary" @click="submitForm(formRef)">搜索</el-button>
				       <el-button @click="resetForm(formRef)">取消</el-button>
				     </el-form-item>
				</el-form>
	       </el-popover>
	    <el-button>重置</el-button>
	  </el-space>
	  </el-row>
	   <!--功能区域 -->
	  <el-row>
	   <el-space >
	   <!-- <el-button type="primary" class="im-but-one">
	      <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
	      <span>添加多渠道订单</span>
	    </el-button> -->
	   <el-button @click="handleCallbackReview">邀请评论</el-button>
<!-- 		 <el-button @click="invoiceManage">VAT发票管理</el-button> -->
		  <el-button @click="downloadOrderlist">导出</el-button>
		   <UploadRpt ref="uploadRptRef" type="GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_GENERAL"></UploadRpt>
	   <!-- <el-dropdown trigger="click">
	      <el-button>
	                 订单标记<el-icon class="el-icon--right"><arrow-down /></el-icon>
	      </el-button>
	      <template #dropdown>
	        <el-dropdown-menu >
	          <el-dropdown-item>标记促销</el-dropdown-item>
	          <el-dropdown-item>取消标记促销</el-dropdown-item>
	          <el-dropdown-item>标记退款</el-dropdown-item>
	          <el-dropdown-item>取消标记退款</el-dropdown-item>
	        </el-dropdown-menu>
	      </template>
	    </el-dropdown> -->
	   </el-space>
	   <div class='rt-btn-group'>
		<!-- <el-button @click="sumSale"  class='ic-btn'  title='统计'>
		   <chart-pie theme="outline" size="16"  :strokeWidth="3"/>
		</el-button> -->   
	   <el-button class='ic-btn'  title='列配置'>
	      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
	   </el-button>
	    <el-button   class='ic-btn' title='帮助文档'>
	     <help theme="outline" size="16" :strokeWidth="3"/>
	   </el-button>
	   </div>
	</el-row>
	</div>
	<!-- <el-row v-if="sumdataShow">
		 <el-space :size="32" :spacer="spacer">
			 <div class="sum-data"  v-for="item in 4"> 
				 <h3>9265</h3>
				 <div class="font-small">订单数量</div>
			 </div>
		 </el-space>
	</el-row> -->
	
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider,ElMessage } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	import {useRouter } from 'vue-router';
	import UploadRpt from '@/components/Upload/uploadRpt.vue';
   const emit = defineEmits(['refreshTable',"downloadOrderlist" ]);
			let router = useRouter()
			let dateValue= ref();
			let storeList = ref()
			let store=ref("")
			let rows={};
			let dataType=ref("1")
			let countryList = ref()
			let country = ref("")
			let orderType = ref("Shipped")
			let orderTypeList=[{id:"Shipped",name:"已发货"},{id:"Unshipped",name:"未发货"},{id:"Pending",name:"进行中"},{id:"Cancelled",name:"已取消"}]
			let customerType = ref("")
			let customerTypeList=[{id:"false",name:"个人"},{id:"true",name:"企业"}]
			let shipType = ref("")
			let shipTypeList=[{id:"Amazon",name:"亚马逊发货"},{id:"Merchant",name:"卖家自发货"}]
			let searchKeywords =ref()
			let selectlabel = ref("sku")
			let moreSearchVisible =  ref()
			let sumdataShow = ref(false)
			let spacer = h(ElDivider, { direction: 'vertical' })
			let color=ref("")
			let colorList = [
				{label:"全部",value:""},
				{label:"红色",value:"red"},
				{label:"橙色",value:"orange"},
				{label:"蓝色",value:"blue"},
				{label:"黑色",value:"black"},
				{label:"绿色",value:"green"},
				{label:"黄色",value:"yellow"},
				{label:"紫色",value:"purple"}
				]
			let shortcuts = [
				{
				  text: '昨日',
				  value: () => {
				    const end = new Date()
				    const start = new Date()
				    start.setTime(start.getTime() - 3600 * 1000 * 24 * 1)
				    return [start, end]
				  },
				},
			  {
			    text: '近7天',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
			      return [start, end]
			    },
			  },
			  {
			    text: '近1个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
			      return [start, end]
			    },
			  },
			  {
			    text: '近2个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 60)
			      return [start, end]
			    },
			  },
			  {
			    text: '近3个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
			      return [start, end]
			    },
			  },
			]
			
			onMounted(()=>{
				dateValue.value = shortcuts[0].value()
				getstoreData();
			})
			function refreshTable(){
				rows.groupid=store.value
				rows.pointname=country.value
				rows.orderstatus=orderType.value
				rows.saleschannel=shipType.value
				rows.isbusiness=customerType.value
				rows.color=color.value
				rows.search=searchKeywords.value
				rows.searchtype=selectlabel.value
				rows.dates=dateValue.value
				rows.datatype=dataType.value;
				if(rows.search!="" && rows.search!=undefined){
					if(distinctDay(rows.dates[0],rows.dates[1])<=365){
						emit("refreshTable",rows)
					}else{
						//提示
						ElMessage.error('含搜索条件时日期不能超过一年！')
					}
				}else{
					if(distinctDay(rows.dates[0],rows.dates[1])<=30){
							emit("refreshTable",rows);
					}else{
						//提示
						ElMessage.error('不含搜索条件时日期不能超过30天！')
					}
					
				}
			}
			function distinctDay(date1,date2){
				return (date2-date1)/1000/3600/24;
			}
			function changeGroup(val){
				getCountryData()
			}
			//获取店铺列表
			function getstoreData(){
				groupApi.getAmazonGroup().then(function(res){
					if(res.data && res.data.length>0){
						res.data.push({"id":"all","name":"全部"})
						storeList.value=res.data;
						store.value=res.data[0].id;
						getCountryData();
					}
				});
			}
			//获取国家列表
			function getCountryData(){
				marketApi.getMarketByGroup({"groupid":store.value}).then(function(res){
					if(res.data && res.data.length>0){
						res.data.push({"pointName":"all","name":"全部"})
						countryList.value=res.data;
						country.value=res.data[0].pointName;
						refreshTable()
					}
				});
			}
			function handleCallbackReview(){
					emit("callbackReview",rows);
			}
			//获取订单状态
			function getOrderType(){
				
			}
			function downloadOrderlist(){
				emit("downloadOrderlist",rows)
			}
			//日期改变
			function dateChange(){
				refreshTable()
			}
			//搜索内容
			function searchConfirm(){
				refreshTable()
			}
			//搜索类型
			function searchTypeChange(){
				refreshTable()
			}
			function submitForm(){
				refreshTable()
				
				moreSearchVisible.value = false
			}
			function resetForm(){
				moreSearchVisible.value = false
			}
			function colorChange(val){
				color.value=val;
			}
			function sumSale(){
				if(sumdataShow.value){
					sumdataShow.value = false
				}else{
					sumdataShow.value = true
				}
			}
			//发票管理
			function invoiceManage(row){
				  router.push({
				  	path:'/service/VatInvioce',
				  	query:{
				  	  title:"VAT发票",
				  	  path:'/service/VatInvioce',
				  	}
				  })
			}
</script>

<style>

.color-select{
	display: flex;
	align-items: center;
	justify-content: space-between;
}	
.sum-data{margin-bottom:16px;}
</style>
