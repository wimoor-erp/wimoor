<template>
	<div class="con-header" >
		<el-tabs v-model="activeName" class="demo-tabs" @tab-change="searchConfirm">
		  <el-tab-pane label="全部订单" name=""   key="">
		  </el-tab-pane>
		  <el-tab-pane label="待装箱"   name="3" :key="3">
		  </el-tab-pane>
		  <el-tab-pane label="待打标"   name="4" :key="4">
		  </el-tab-pane>
		  <el-tab-pane label="物流跟踪" name="5" :key="5">
		  </el-tab-pane>
		  <el-tab-pane label="海关"     name="6" :key="6">
		  </el-tab-pane>
		  <el-tab-pane label="待接收"   name="7" :key="7">
		  </el-tab-pane>
		  <el-tab-pane label="已完成"   name="8" :key="8">
		  </el-tab-pane>
		 <el-tab-pane label="异常单据"   name="9" :key="9">
		 </el-tab-pane>
		  <el-tab-pane label="已取消"   name="0" :key="0">
		  </el-tab-pane>
		</el-tabs>
	  <el-row>
	    <el-space >
			<Group ref="groups" @change="getData" defaultValue="all"/>
			<Warehouse ref="warehouses" @changeware="getWarehouse" />
			<Datepicker ref="datepickers" @changedate="changedate" :shortIndex="1" />
	        <el-input  clearable v-model="searchKeywords" @input="searchConfirm" placeholder="请输入" class="input-with-select" >
	      <template #prepend>
	        <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="货件编码" value="number"></el-option>
	          <el-option label="计划编码" value="formnumber"></el-option>
			  <el-option label="备注" value="remark"></el-option>
			  <el-option label="配送中心" value="centerid"></el-option>
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
	   <el-popover    v-model:visible="moreSearchVisible" :width="400" trigger="click">
	         <template #reference>
	           <el-button  title='高级筛选'  class='ic-btn'>
	           <menu-unfold theme="outline" size="16"  :strokeWidth="3"/>
	           </el-button>
	         </template>
			  <el-form  :model="form" label-width="120px">
			  <el-form-item label="物流商">
			       <el-select  v-model="tran"  :teleported="false" placeholder="请选择" @change="tranChange">
					   <el-option  v-for="item in tranlist"  :key="item.id"  :label="item.name"  :value="item.id"  >
					   </el-option>
			       </el-select>
			     </el-form-item>
				 <el-form-item label="物流渠道">
				      <el-select v-model="channel" :teleported="false" placeholder="请选择" @change="channelChange">
				           <el-option  v-for="item in channellist"  :key="item.id"  :label="item.channame" :value="item.id" >
				           </el-option>
				      </el-select>
				    </el-form-item>
					<el-form-item label="运输方式">
					     <el-select  v-model="trantype"  :teleported="false" placeholder="请选择" @change="trantypeChange">
					     <el-option value="" label="全部" ></el-option>
						 <el-option value="1" label="海运" ></el-option>
						 <el-option value="2" label="空运" ></el-option>
						 <el-option value="3" label="铁路" ></el-option>
						 <el-option v-for="item in transtypelist" :value="item.id" :label="item.name" ></el-option>
					     </el-select>
					   </el-form-item>
					   <el-form-item label="FBA仓库">
						   <el-select-v2  
								v-model="fbacenter"   
							   :options="fbacenterlist" 
								filterable 
								clearable  
								:teleported="false"
								placeholder="请选择" 
						       @change="handleCenterChange">
						           <template #default="{ item }">
						                <div :title="item.addressName+'-'+item.city">{{item.country}}
										<span v-if="item.code">-{{ item.code }}</span>
										<span v-if="item.area">-{{ item.area }}</span></div>
						           </template>
						   </el-select-v2>
					      </el-form-item>
						  <el-form-item label="是否原装">
						       <el-select  v-model="queryParam.areCasesRequired"  :teleported="false" placeholder="请选择" >
						       <el-option :value="null" label="全部" ></el-option>
						  	 <el-option :value="true" label="是" ></el-option>
						  	 <el-option :value="false" label="否" ></el-option>
						       </el-select>
						     </el-form-item>
						<el-form-item label="含内部编码">
						     <el-select  v-model="queryParam.hasreferenceid"  :teleported="false" placeholder="请选择" >
						     <el-option :value="null" label="全部" ></el-option>
							 <el-option :value="true" label="是" ></el-option>
							 <el-option :value="false" label="否" ></el-option>
						     </el-select>
						   </el-form-item>
				  <el-form-item >
				       <el-button type="primary" @click="submitForm(formRef)">搜索</el-button>
				       <el-button @click="resetForm(formRef)">取消</el-button>
				     </el-form-item>
				</el-form>
	       </el-popover>
	    <el-button @click="resetQuery">重置</el-button>
	  </el-space>
	 <!-- <div class='rt-btn-group'>
	  		  <el-checkbox-group v-model="queryParam.checkdown" size="small"  @change="handleCheckDown" v-if="queryParam.orderStatus==7" >
	  		        <el-checkbox-button  label="notdown" value="notdown"> 未下架</el-checkbox-button>
					<el-checkbox-button  label="hasdown" value="hasdown"> 可拣货</el-checkbox-button>
	  		 </el-checkbox-group>
	  </div> -->
	  </el-row>
	   <!--功能区域 -->
	  <el-row>
	   <el-space >
		     <el-dropdown  v-if="printButtonShow">
		       <el-button type="primary">
		         打印配货单<el-icon class="el-icon--right"><arrow-down /></el-icon>
		       </el-button>
		       <template #dropdown>
		         <el-dropdown-menu>
		           <el-dropdown-item @click="printLabel('simple')">简单版</el-dropdown-item>
		           <el-dropdown-item @click="printLabel('detail')">详细版</el-dropdown-item>
		         </el-dropdown-menu>
		       </template>
		     </el-dropdown>
		 <el-button @click="handleShelf" v-if="printButtonShow">批量下架</el-button>
		<el-button  @click="showQuoteDialog"  type="success" v-if="printButtonShow">物流报价</el-button>
	   </el-space>
	   <el-button type="primary" @click="asyncShipment">
	        同步计划
	   </el-button>
	   <div class='rt-btn-group'>
		   <el-button   @click="showNumberDialog" >导入追踪号</el-button>
	   <el-button class='ic-btn'  title='列配置'>
	      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
	   </el-button>
	    <el-button   class='ic-btn' title='帮助文档'>
	     <help theme="outline" size="16" :strokeWidth="3"/>
	   </el-button>
	   </div>
	</el-row>
	</div>
	<TransNumber ref="transNumberRef"></TransNumber>
	<AsyncPlan ref="asyncPlanRef"></AsyncPlan>
	<QuoteDialog ref="quoteRef" @change="searchConfirm"></QuoteDialog>
</template>   

<script setup> 
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { useRoute,useRouter } from 'vue-router'
	import groupApi from '@/api/amazon/group/groupApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	import { ref,reactive,onMounted,watch,toRefs } from 'vue'
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import Group from '@/components/header/group.vue';
	import {ElMessage } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import TransNumber from './transnumber.vue';
	import QuoteDialog from './quoteDialog.vue';
	import AsyncPlan from '@/views/erp/shipv2/shipment_add/list/components/async.vue';
    const emit = defineEmits(["getdata"]);
	const asyncPlanRef =ref();
	const state = reactive({
		shipmark:"全部",
		activeName:"",
	})
	const{
		shipmark,activeName,
		}=toRefs(state)
	let tranlist=ref([])
	let channellist=ref([])
	let searchKeywords =ref()
	let queryParam=reactive({orderStatus:7,areCasesRequired:null,hasreferenceid:null});
	let router = useRouter();
	let selectlabel = ref("sku");
	let trantype=ref("")
	let tran=ref("")
	let rfgroups=ref(Group);
	let rfdatepickers=ref(Datepicker);
	let channel=ref("")
	let moreSearchVisible=ref(false)
	let isload=true;
	let printButtonShow =ref(false);
	let groups=ref();
	let transtypelist=ref();
	let warehouses=ref();
	let datepickers=ref();
	let dialogVisible=ref(false);
	const transNumberRef=ref();
	const quoteRef=ref();
	let fbacenter =ref("");
	let fbacenterlist=ref([]);
	onMounted(()=>{
		getTranList();
		loadFbaCenter();
	})
	function asyncShipment(){
		asyncPlanRef.value.show();
	}
	function loadFbaCenter(){
		fbacenterlist.value=[];
		shipmenthandlingApi.getFbaCenter().then(res=>{
			fbacenterlist.value.push({value:"",label:"全部",options:[{value:"",label:"全部仓库",country:"全部仓库",area:""}]});
			res.data.forEach(item=>{
				item.value=item.code;
				item.label="";
				if(item.country){
					item.label=item.label+item.country;
				}
				if(item.code){
					item.label=item.label+"-"+item.code;
				}
				var hasitem=false;
				fbacenterlist.value.forEach(node=>{
					if(node.value==item.country&&hasitem==false){
						hasitem=true;
						node.options.push(item);
					}
				});
				if(hasitem==false) {
					var node={label:item.country,value:item.country,options:[]};
					node.options.push(item);
					fbacenterlist.value.push(node);
				}
			});
		})
	}
	function showNumberDialog(){
		transNumberRef.value.show();
	}
	function refreshShipment(){
		let groupid=rfgroups.value.groupid;
		let marketplaceid=rfgroups.value.marketplaceid;
		let datas=rfdatepickers.value.dateValue;
		let start=datas[0].format("yyyy-MM-dd");
		let end=datas[1].format("yyyy-MM-dd")+" 23:59:59";
	}

	function searchTypeChange(){
		queryParam.seachtype=selectlabel.value;
		if(isload==false){
			emit("getdata",queryParam);
		}
	}
	function searchConfirm(){
		if(searchKeywords.value&&searchKeywords.value.indexOf("FBA")==0){
			selectlabel.value="number";
		}
		queryParam.searchwords=searchKeywords.value;
		queryParam.auditstatus=state.activeName;
		if(isload==false){
			emit("getdata",queryParam);
		}
	}
	function getTranList(){
		transportationApi.getTranlist().then((res)=>{
			res.data.push({"id":"","name":"全部"})
			tranlist.value = res.data;
		})
		transportationApi.getTransType().then((res)=>{
			transtypelist.value=res.data;
		});
		
	}
	function trantypeChange(val){
		trantype.value=val;
	}
	function tranChange(val){
		tran.value=val;
		getchannelList();
	}
	function channelChange(val){
		channel.value=val;
	}
	function getchannelList(){
		var tranid=tran.value;
		var market="";
		if(queryParam.country==undefined){
			market="";	
		}else{
			market=queryParam.country;
		}
		transportationApi.getChannel({"company":tranid,"marketplaceid":market,"transtype":""}).then((res)=>{
			res.data.push({"id":"","channame":"全部"});
			channellist.value = res.data
		})
	}
	function submitForm(){
		queryParam.company=tran.value;
		queryParam.channel=channel.value;
		queryParam.transtype=trantype.value;
		if(isload==false){
			emit("getdata",queryParam);
		}
		moreSearchVisible.value=false;
	}
	function resetForm(){
		moreSearchVisible.value=false;
	}
	function getData(obj){
		queryParam.store=obj.groupid;
		queryParam.country=obj.marketplaceid;
		emit("getdata",queryParam)
		isload=false;
	}
	function getWarehouse(wid){
		queryParam.warehouse=wid;
		if(isload==false){
			emit("getdata",queryParam);
		}
	}
	function handleCheckDown(){
		if(queryParam.checkdown.length>=2){
			queryParam.checkdown=[queryParam.checkdown[1]];
		}
		 emit("getdata",queryParam);
	}
	function changedate(dates){
		queryParam.start=dates.start;
		queryParam.end=dates.end;
		if(isload==false){
			emit("getdata",queryParam);
		}
	}
	function handleShelf(){
		router.push({
			path:'/erp/ship/quota',
			query:{
			  shipments:selects,
			  title:"配货单下架",
			  path:'/erp/ship/quota',
			}
		})
	}
	function showQuoteDialog(){
		quoteRef.value.show(selects);
	}
	function resetQuery(){
		tran.value="";
		channel.value=null;
		channellist.value=[];
		trantype.value="";
		selectlabel.value='sku';
		groups.value.groupid="";
		groups.value.marketplaceid="";
		warehouses.value.warehouseid="";
		queryParam.store="";
		queryParam.country="";
		queryParam.warehouse="";
		queryParam.areCasesRequired=null;
		queryParam.hasreferenceid=null;
		queryParam.auditstatus=state.activeName;
		searchKeywords.value="";
		const end = new Date()
		const start = new Date()
		start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
		datepickers.value.dateValue=[start, end];
		queryParam.start=start.format("yyyy-MM-dd");
		queryParam.end=end.format("yyyy-MM-dd")+" 23:59:59";
		queryParam.fbacenter="";
		queryParam.checkinv="";
		searchConfirm();
	}
	let selects=[];
	let selectrows=[];
	function handleTableSelectChange(rows){
		selects=[];
		selectrows=rows;
		if(rows&&rows.length>0){
			rows.forEach(item=>{
				selects.push(item.shipmentid);
			})
		}
		if(rows&&rows.length>0){
			printButtonShow.value=true;
		}else{
			printButtonShow.value=false;
		}
	}
	function statusChange(obj){
		queryParam.orderStatus=obj.orderStatus;
	}
	function printLabel(type){
		//下载pdf
		shipmenthandlingApi.downPDFShipForms(type, {shipments:selects});
	}
	function handleCenterChange(value){
		queryParam.fbacenter=value;
	}
	 defineExpose({handleTableSelectChange,statusChange});
</script>

<style>
</style>
