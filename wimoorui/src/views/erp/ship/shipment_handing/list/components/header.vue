<template>
	<div class="con-header" >
	  <el-row>
	    <el-space >
			<Group ref="groups" @change="getData" defaultValue="all"/>
			<Warehouse ref="warehouses" @changeware="getWarehouse" />
			<Datepicker ref="datepickers" @changedate="changedate" />
	        <el-input  v-model="searchKeywords" @input="searchConfirm" placeholder="请输入" class="input-with-select" >
	      <template #prepend>
	        <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="货件编码" value="number"></el-option>
	          <el-option label="货件名称" value="formnumber"></el-option>
			  <el-option label="备注" value="remark"></el-option>
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
	  <div class='rt-btn-group'>
	  		  <el-checkbox-group v-model="queryParam.checkdown" size="small"  @change="handleCheckDown" v-if="queryParam.orderStatus==7" >
	  		        <el-checkbox-button  label="notdown" value="notdown"> 未下架</el-checkbox-button>
					<el-checkbox-button  label="hasdown" value="hasdown"> 可拣货</el-checkbox-button>
	  		 </el-checkbox-group>
	  </div>
	  </el-row>
	   <!--功能区域 -->
	  <el-row>
	   <el-space >
		 <el-button type="primary" @click="asyncShipment">
		      同步货件
		 </el-button>
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
	   </el-space>
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
</template>   

<script> 
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { useRoute,useRouter } from 'vue-router'
	import groupApi from '@/api/amazon/group/groupApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	import { ref,reactive,onMounted,watch } from 'vue'
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import Group from '@/components/header/group.vue';
	import {ElMessage } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import TransNumber from './transnumber.vue';
	export default{
		name:"Header",
		components:{MenuUnfold,Search,ArrowDown,SettingTwo,Help,Copy,MoreOne,Group,Warehouse,Datepicker,TransNumber},
		emits:["getdata"],
		setup(props,context){
			let tranlist=ref([])
			let channellist=ref([])
			let searchKeywords =ref()
			let queryParam=reactive({orderStatus:7,areCasesRequired:null,hasreferenceid:null});
			let router = useRouter()
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
			let transNumberRef=ref();
			let fbacenter =ref("");
			let fbacenterlist=ref([]);
			onMounted(()=>{
				getTranList();
				loadFbaCenter();
			})
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
			function asyncShipment(){
				router.push({
						path:'/e/s/a',
						query:{
						  name:'shiptongbu',
						  title:"货件同步",
						  path:'/e/s/a',
						}
					})
			}
			function searchTypeChange(){
				queryParam.seachtype=selectlabel.value;
				if(isload==false){
					context.emit("getdata",queryParam);
				}
			}
			function searchConfirm(){
				if(searchKeywords.value&&searchKeywords.value.indexOf("FBA")==0){
					selectlabel.value="number";
				}
				queryParam.searchwords=searchKeywords.value;
				if(isload==false){
					context.emit("getdata",queryParam);
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
					context.emit("getdata",queryParam);
				}
				moreSearchVisible.value=false;
			}
			function resetForm(){
				moreSearchVisible.value=false;
			}
			function getData(obj){
				queryParam.store=obj.groupid;
				queryParam.country=obj.marketplaceid;
				context.emit("getdata",queryParam)
				isload=false;
			}
			function getWarehouse(wid){
				queryParam.warehouse=wid;
				if(isload==false){
					context.emit("getdata",queryParam);
				}
			}
			function handleCheckDown(){
				if(queryParam.checkdown.length>=2){
					queryParam.checkdown=[queryParam.checkdown[1]];
				}
				 context.emit("getdata",queryParam);
			}
			function changedate(dates){
				queryParam.start=dates.start;
				queryParam.end=dates.end;
				if(isload==false){
					context.emit("getdata",queryParam);
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
			function handleTableSelectChange(rows){
				selects=[];
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
			return{
				refreshShipment,selectlabel,searchKeywords,searchConfirm,searchTypeChange,
				tranlist,channellist,getTranList,trantypeChange,trantype,statusChange,queryParam,
				tranChange,tran,channelChange,channel,getchannelList,handleShelf,handleCheckDown,
				submitForm,resetForm,getData,getWarehouse,handleTableSelectChange,printButtonShow,
				moreSearchVisible,changedate,resetQuery,groups,warehouses,datepickers,printLabel,
				dialogVisible,rfgroups,rfdatepickers,asyncShipment,transNumberRef,showNumberDialog,
				handleCenterChange,fbacenter,transtypelist,fbacenterlist
			}
		}
	}
</script>

<style>
</style>
