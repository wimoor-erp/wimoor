<template>
	<div class="list-from" v-if="!isappend">
		<el-row>
			<el-col :span="20">
				<el-form  label-width="120px">
					<el-row>
						<el-col :span="12">
							<el-form-item label="计划编码">
								<span>{{formData.number}}</span>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="发货店铺" prop="groupid">
								<el-select v-model="formData.groupid"     placeholder="发货店铺" @change="groupChange">
									  <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
									  </el-option>
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="发货仓库" prop="warehouseid">
								<el-select v-model="formData.warehouseid"     placeholder="全部仓库" @change="warehouseChange">
								      <el-option  v-for="item in warehouseList"   :key="item.id"  :label="item.name" :value="item.id"   >
								      </el-option>
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="收货国家" prop="marketplaceid">
								<el-select   v-model="formData.marketplaceid"    placeholder="全部国家" @change="marketChange">
								      <el-option  v-for="item in marketList"   :key="item.marketplaceid"  :label="item.name" :value="item.marketplaceid"   >
								      </el-option>
								</el-select>
							</el-form-item>
						</el-col>
						 
					
                        <el-col :span="24">
							<el-form-item label="发货地址" class="myformitem" prop="region">
								<el-space wrap>
										<RadioCardGroup v-model="formData.sourceAddress"  @change="radioChange" :isspace="true">
											<template  v-for="(item,index) in addressData">
											   <RadioCard  v-if="index<3 || !showall"  :value="item.id" shadow="hover"   style="width:400px" >
													  <div class="addr-info">
															  <div class="addr-header">
																  <el-tag v-show="item.checked" style="margin-right:20px;margin-top:10px" type="danger">默认地址</el-tag>
																  <span>{{item.name}}</span>
																  <span>{{item.phone}}</span>
															  </div>
															  <el-space wrap>
																  <p style="white-space: pre-wrap;">{{item.addressline1}}</p>
																  <p style="white-space: pre-wrap;">{{item.addressline2}}</p>
																  <p style="white-space: pre-wrap;">{{item.stateorprovincecode}},{{item.city}},{{item.districtorcounty}}</p>
																  <span>{{item.countrycode}},{{item.postalcode}}</span>
															  </el-space>
															  <div class="addr-footer" style="padding-top:10px">
																  <el-link v-if="item.id==formData.sourceAddress" @click="addAddress(item)" type="primary">修改</el-link>
																  <el-link v-else ></el-link>
															  </div>
													  </div>
											   </RadioCard>
											   </template>
										</RadioCardGroup>
									</el-space>
										<el-col :span="24">
											<div class="adrr-btn" style="padding-top:20px">
												
											<el-link v-if="showall" @click="showAlladdr"  :underline="false" type="primary">显示全部</el-link>
											<el-button @click="addAddress()" v-else>使用新地址</el-button>
											
											<el-link @click="manageAlladdr"  :underline="false" type="primary">管理发货地址</el-link>
											</div>
										</el-col>
									 <!-- <el-checkbox v-model="selectFBACheck">我要使用亚马逊跨境物流服务进行运送</el-checkbox> -->
							</el-form-item>	
						</el-col>	
						<el-col :span="24">
							<el-form-item label="备注">
							  <el-input v-model="formData.remark"
								maxlength="200"
								placeholder="请输入内容"
								show-word-limit
								rows="3"
								type="textarea"  />
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="运输方式">
									<el-select v-model="formData.transtype">
									  <el-option v-for="item in transTypeAllList" :label="item.name" :value="item.id"></el-option>
									</el-select>
							</el-form-item> 
						</el-col>
						<el-col :span="12">
							<el-form-item label="单据类型">
								<el-select v-model="formData.invtype">
									<el-option :value="0" label="常规">常规(扣除本地库存且同步亚马逊卖家中心)</el-option>
									<el-option :value="1" label="本地虚拟" >本地虚拟(扣除本地库存但不同步亚马逊卖家中心，无法上传装箱信息)</el-option>
									<el-option :value="2" label="仅平台发货" >仅平台发货(仅同步亚马逊卖家中心，不扣除本地库存)</el-option>
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
			</el-col>
		</el-row>
	</div>
	<div class="divider" v-if="!isappend"> <el-divider /></div>
	<div >
	<el-row>
		<el-col :span="24">
			<div class="fro-head">
			<h4>发货商品列表({{totalproducts}})</h4>
			<div>
				<el-button @click="addProduct" type="primary" plain>添加商品</el-button>
				<el-button @click="showProductModal">导入</el-button>
				<el-button @click="printProductlabel(state)">打印当前产品标签</el-button>
				<el-button @click="clearItems(productlist)">清空列表</el-button>
			</div>
			</div>
			<div class="fro-body">
			  <el-table :data="productlist" border style="width: 100%;margin-bottom:16px;" row-key="id">
			  	<el-table-column prop="image" label="图片" width="60">
			  		<template #default="scope">
			  			<el-image :src="scope.row.image" width="40" height="40"></el-image>
			  		</template>
			  	</el-table-column>
			  	<el-table-column prop="name" label="名称/SKU"   show-overflow-tooltip>
			  		<template #default="scope">
			  			<div class='name'>{{scope.row.name}}</div>
			  			<div class='sku'>{{scope.row.sku}} </div>
			  		</template>
			  	</el-table-column>
				<!-- <el-table-column   label="包装详情" width="130"  >
					 <template #default="scope">
					 	 <el-select v-model="scope.row.areCasesRequired">
							 <el-option value="0" key="0" label="混装商品"></el-option>
							 <el-option value="1" key="1" label="原厂包装商品"></el-option>
						 </el-select>
					 </template>
				</el-table-column> -->
				
				<el-table-column prop="asin" label="ASIN" width="120"   />
				<el-table-column prop="msku" label="本地SKU" width="120"  >
					 <template #default="scope">
						 <span v-if="scope.row.msku">{{scope.row.msku}}</span>
						 <span v-else>无</span>
					  </template>
				</el-table-column>
				
				<el-table-column prop="fulfillable" label="库存" width="70" >
					<template #default="scope">
							 <div>{{scope.row.fulfillable}}</div>
							 <div v-if="scope.row.canAssembly">可组装:{{scope.row.canAssembly}}</div>
					 </template>
				</el-table-column>
				<el-table-column prop="length" label="长*宽*高" width="230"  >
					 <template #default="scope">
					 	<span >长:{{scope.row.length}}cm</span>,
						<span >宽:{{scope.row.width}}cm</span>,
						<span >高:{{scope.row.height}}cm</span>
					 </template>
				</el-table-column>
			  	<el-table-column prop="weight" label="带包装重量(kg)" width="120"   />
			  	<el-table-column prop="quantity" label="发货数量" width="150" >
			  		<template #default="scope">
			  			<el-input v-model.number="scope.row.quantity"  @input="formatQuantity(scope.row)" ></el-input>
			  		</template>
			  	</el-table-column>
				<el-table-column v-if="formData.arecase=='1'" show-overflow-tooltip prop="boxnum" label="每箱数量" width="150" >
					<template #default="scope">
						<el-space>
						<el-input   v-model.number="scope.row.boxnum"  @input="scope.row.boxnum=CheckInputInt(scope.row.boxnum)" ></el-input>
					   <div  style="min-width:50px;" v-if="scope.row.boxnum&&scope.row.boxnum!='0'&&scope.row.quantity">共{{formatFloat(scope.row.quantity/scope.row.boxnum)}}箱</div>
					</el-space>
					</template>
				</el-table-column>
				<el-table-column prop="guidance" label="亚马逊校验" width="160" >
					<template #default="scope">
						<el-tag v-if="scope.row.guidance=='success'"   type="success" :title="scope.row.msg">亚马逊校验成功</el-tag>
						<el-tag v-else-if="scope.row.guidance=='warn'" type="error"   >亚马逊未正常校验</el-tag>
						<el-tag v-else-if="scope.row.guidance=='error'" type="error"  >无效的SKU</el-tag>
						<el-tag v-else  type="warning">亚马逊校验中</el-tag>
						<el-tag v-if="scope.row.fulfillable-scope.row.quantity<0" type="warning">发货数量大于库存数</el-tag>
					    <div class="font-extraSmall" v-if="scope.row.prep&&scope.row.prep.length>0">
					    预备信息：<span v-for="item in scope.row.prep">{{item}}</span>
					    </div>
					</template>
				</el-table-column>
				<el-table-column prop="asin"  label="操作" width="120"  >
				    <template #default="scope">
						<el-button class='el-button--blue' @click="removeBind(scope.$index)">删除</el-button>
						<div>
						<el-button class='el-button--blue' @click="operateMore(scope.row)">其它维护</el-button>
						</div>
				    </template>
				</el-table-column>
			  </el-table>
			  <el-button type="primary" @click="submitPlan" :loading="submitloading" >提交</el-button>
			   <el-button @click="cancelPlan" >取消</el-button>
			</div>
		</el-col>
	</el-row>
	</div>
	 <Editdialog ref="editRef" @addressChange="loadAddress"/>
	 <MateiralDialog ref="prodiaRef" @getdata="getdata" />
	<el-dialog v-model="downloadVisible" title="导入数据" width="50%" center >
		<div>
		<el-descriptions >
			 <el-descriptions-item label="文件上传" >
			 <el-upload
			 		class="upload-demo" 
			 		action   
			 		:http-request="uploadFile" 
			 		ref="uploadRef"
			 		:limit="1" 
			 		:on-remove="handleRemove"  
			 		:file-list="fileList" 
			 		:on-exceed="handleExceed" 
			 		:before-upload="checkFile" 
			 		:show-file-list="true" 
			 		:headers="headers" 
			 		accept=".xls,.xlsx"
			 		>
			 		<!-- action="/api/file/fileUpload" -->
			 		<el-button class="btn"><i class="el-icon-paperclip"></i>选择文件</el-button>
			 		<template #tip>
			 		  <div class="el-upload__tip">
			 		    文件格式Xls/Xlsx,小于5MB.
			 		  </div>
			 		</template>
			 	</el-upload>
			 </el-descriptions-item>
				</el-descriptions>
				</div>
	  <div style="margin-top: 15px;">按照模板定义格式导入数据。没有模板？ <span style="color: #007DFF;cursor: pointer;" @click="downloadtemplate">点击下载</span></div>
	  <template #footer>
	    <span class="dialog-footer">
	      <el-button @click="downloadVisible = false">关闭</el-button>
	      <el-button type="primary" @click="submitProductList(state)"
	        >上传</el-button
	      >
	    </span>
	  </template>
	</el-dialog>
	
	<el-dialog
	title="其它信息设置"
	v-model="operatorVisiable"
	width="30%"
	
	>
	 <el-form label-width="auto"  label-position="left">
		  <el-form-item   label="预备信息处理人">
			  <el-select v-model="operatorRow.prepOwner">
				  <el-option value="NONE" key="NONE" label="NONE"></el-option> 
				 <el-option value="SELLER" key="SELLER" label="卖家"></el-option> 
				 <el-option value="AMAZON" key="AMAZON" label="亚马逊"></el-option> 
			  </el-select>
		  </el-form-item>
		  <el-form-item   label="贴标人">
		  			  <el-select v-model="operatorRow.labelOwner">
						  <el-option value="NONE" key="NONE" label="NONE"></el-option> 
						  <el-option value="SELLER" key="SELLER" label="卖家"></el-option>
						  <el-option value="AMAZON" key="AMAZON" label="亚马逊"></el-option> 
					  </el-select>
		  </el-form-item>
		  <el-form-item   label="保质期">
				<el-date-picker
					  v-model="operatorRow.expiration"
					  type="date"
					  placeholder="选择日期"
					/>
		  </el-form-item>
	 </el-form>
	<template #footer>
		<el-button @click="operatorVisiable=false" type="primary">确认</el-button>
	</template>
	</el-dialog>
	
</template>

<script setup>
import {h, ref ,watch,reactive,onMounted,inject,toRefs} from 'vue'
import { useRoute,useRouter } from 'vue-router'
import groupApi from '@/api/amazon/group/groupApi.js';
import marketApi from '@/api/amazon/market/marketApi.js'
import warehouseApi from '@/api/erp/warehouse/warehouseApi.js'
import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
import shipaddressApi from '@/api/erp/ship/shipaddressApi.js';
import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';	
import transportationApi from '@/api/erp/ship/transportationApi.js';
import materialApi from '@/api/erp/material/materialApi.js';
import Editdialog from"@/views/amazon/address/components/editdialog.vue"
import MateiralDialog from "./mateiralDialog.vue";	
import RadioCard from "@/components/Radio/RadioCard/radio_card.vue";
import RadioCardGroup from "@/components/Radio/RadioCard/radio_card_group.vue";
import {CheckInputIntLGZero,CheckInputInt,formatFloat} from '@/utils/index.js';
import {ElMessage } from 'element-plus';
import {loadInventory,validSkuList,handleLoadPlanData,submitProductList} from "@/hooks/erp/shipment/data_helper.js";
import {checkFile,printProductlabel,handleExceed,
         downloadtemplate,handleRemove} from "@/hooks/erp/shipment/file_helper.js";
import profitParamApi from '@/api/amazon/profit/profitParam.js';
let uploadRef=ref();
let editRef=ref();
let prodiaRef=ref();
const emit = defineEmits(['change','cancel']);
let state=reactive({
			operatorVisiable:false,
	        operatorRow:null,
			selectFBACheck:true,
			totalproducts:0 ,
			addrIndex:3,
			formData:{name:"",groupid:"",warehouseid:"",marketplaceid:"",
			           channel:"",remark:"",arecase:"0",transinfo:{company:"",number:""},
					   sourceAddress:"",invtype:0},
			showall:true,
			submitloading:false,
			groupList:[],
			marketList:[],
			warehouseList:[],
			tranlist:[],
			transTypeAllList:[],
			channellist:[],
			inplaceList:[],
			addressData:[],
			productlist:[],
			downloadVisible:false,
			fileList:[],
			logofile:undefined,
			queryData:{id:"",warehouseid:"",marketplaceid:"",issplit:"",groupid:"",shipmentid:""},
		})
		
		let{totalproducts ,
			addrIndex,
			formData,
			showall,
			submitloading,
			groupList,
			marketList,
			warehouseList,
			tranlist,
			channellist,
			addressData,
			productlist,
			inplaceList,
			downloadVisible,
			fileList,
			transTypeAllList,
			logofile,
			queryData,
			selectFBACheck,
			operatorRow,
			operatorVisiable
			  }=toRefs(state)

  	let props = defineProps({
							   isappend:undefined,
  	                       });
	const { isappend} = toRefs(props);
let router = useRouter();
//请求头
let headers=ref({"Content-Type": "multipart/form-data" }) 
 const emitter = inject("emitter"); // Inject `emitter`
 function closeTab(){
 			 	emitter.emit("removeTab", 100);
 			 };
function initQueryData(){
	state.queryData.planid=router.currentRoute.value.query.planid;
	state.queryData.warehouseid=router.currentRoute.value.query.warehouse;
	state.queryData.marketplaceid=router.currentRoute.value.query.marketplaceid;
	state.queryData.issplit=router.currentRoute.value.query.issplit;
	state.queryData.groupid=router.currentRoute.value.query.group;
	state.queryData.shipmentid=router.currentRoute.value.query.shipmentid;
	state.queryData.inboundplanid=router.currentRoute.value.query.inboundplanid;
	state.queryData.batchnumber=router.currentRoute.value.query.batchnumber;	
	state.queryData.formid=router.currentRoute.value.query.formid;
}
function clearItems(productitems){
	state.productlist=[];
}
onMounted(()=>{
	initQueryData();
	loadShipName();
	loadNumber();
	getGroupData("init");
	getWarehouseData();
	getTranList();
	loadInplace();
})
function loadInplace(){
	profitParamApi.getInplaceList({country:"US"}).then(res=>{
			 state.inplaceList=[];
			res.data.forEach(item=>{
				if(item.country=="US"){
					state.inplaceList.push(item);
				}
			})
	})
}
 watch(() =>router.currentRoute.value.query,(newValue,oldValue)=> {
		         if(newValue&&newValue['path']&&newValue["path"].indexOf("/invoice/addshipment")>=0){
					 initQueryData()
					 loadPlanData();
				    }
		         },{ immediate: true });
				 
 	        function loadShipName(){
 				var nowDate=new Date();
 				state.formData.name="PLN"+"("+(nowDate.getMonth()+1)+"/"+(nowDate.getDate())+"/"+nowDate.getFullYear()+" "+nowDate.getHours()+":"+nowDate.getMinutes()+")-1"
 			}
			
 			function addAddress(param){
 				    editRef.value.showModel(param,state.formData.groupid);
 			}
			
           function manageAlladdr(){
 				router.push({
 					path:'/amazon/address',
 					query:{
 						title:'发货地址',
 						path:'/amazon/address',
 					}
 				}) 
 			}
 			function showAlladdr(){
 				addrIndex.value = Infinity 
 				showall.value =false
 			}
 			function radioChange(val){
 				state.formData.sourceAddress= val
 			}
 			//获取店铺列表
 			function getGroupData(type){
 				groupApi.getAmazonGroup().then((res)=>{
 					state.groupList=res.data;
 					if(res.data&&res.data.length>0){
 							if(!state.formData.groupid || state.formData.groupid==""){
 								if(type=='init'&&state.queryData.groupid){
 									 state.formData.groupid=state.queryData.groupid;
 								}else{
 								   state.formData.groupid=res.data[0].id;
 								}
 							}
 							getMarketData(state.formData.groupid,type);
 							loadAddress();
 							loadPlanData();
 					}
 				})
 			}
 			//获取国家列表
 			function getMarketData(id,type){
 				marketApi.getMarketByGroup({'groupid':id}).then((res)=>{
 					state.marketList=res.data;
 					if(res.data&&res.data.length>0&&type!='init'){
 						state.formData.marketplaceid = res.data[0].marketplaceid;
 					}else if(res.data&&res.data.length>0&&state.formData.marketplaceid==""){
 						state.formData.marketplaceid = res.data[0].marketplaceid;
 					}
 				})
 			}
			
 			function groupChange(val,type){
 				getMarketData(val,type);
 				loadAddress();
 				cancelPlan();
 			}
 			
 			function marketChange(val){
 				 state.formData.marketplaceid=val;
 				 var skulist="";
				 loadAddress();
				 if(state.productlist){
					 state.productlist.forEach(function(items){
					 	 skulist+=(items.sku+",");
					 }); 
					 validSkuList(state,skulist);
				 }
 			}
 			//获取仓库列表
 			function getWarehouseData(){
 				warehouseApi.getWarehouseUseable().then((res)=>{
 					state.warehouseList=res.data;
 					if(res.data&&res.data.length>0){
 						state.warehouseList.forEach(function(item){
 							if(state.formData.warehouseid==""){
 								state.formData.warehouseid = item.id;
 							}
 						});
 						getReadyUseWarehouse();
 					}
 				})
 				
 			}
 			
 			function warehouseChange(val){
 				state.formData.warehouseid=val;
 			}
 			function tranChange(val){
 				state.formData.transinfo.company=val;
 				getchannelList();
 			}
 			function getchannelList(){
 				transportationApi.getChannel({"company":state.formData.transinfo.company,"marketplaceid":state.formData.marketplaceid,"transtype":""}).then((res)=>{
 					res.data.push({"id":"","channame":"无"});
 					state.channellist = res.data
 				})
 			}
 			function channelChange(val){
 				state.formData.transinfo.channel=val;
 			}
 			function getTranList(){
 				// transportationApi.getTranlist().then((res)=>{
 				// 	res.data.push({"id":"","name":"无"})
 				// 	state.tranlist = res.data;
 				// })
				transportationApi.getTransTypeAll().then((res)=>{
					state.transTypeAllList=res.data;
				});
 			}
 			function loadAddress(){
 				state.addressData=[];
 				if(state.formData.groupid){
 					shipaddressApi.getAddressByid({"addressid":'',"groupid":state.formData.groupid+","+state.formData.marketplaceid}).then((res)=>{
 						if(res.data && res.data.length>0){
 							res.data.forEach(function(item){
 								if(item.isdefault==true){
 									item.checked=true;
 								}else{
 									item.checked=false;
 								}
 							});
 							state.addressData=res.data;
 							state.formData.sourceAddress=res.data[0].id;
 							if(state.addressData.length>3){
 								state.showall = true
 							}else{
 								state.showall = false
 							}
 						}
 					})
 				}
 				
 			}
 			function loadNumber(){
 				serialnumberApi.getSerialNumber({"ftype":'SF',"isfind":"true"}).then((res)=>{
 					state.formData.number=res.data;
 				})
 			}
 			
 			function getReadyUseWarehouse(){
 				   if(state.queryData.warehouseid){
 					   warehouseApi.getSelfWarehouseById({"id":state.queryData.warehouseid}).then((res)=>{
 							if(res.data){
 							   state.formData.warehouseid=res.data;
 							   state.queryData.warehouseid=res.data;
 							}
 					   });
 				   }
 			      
 			}
 			
 			function addProduct(){
 				prodiaRef.value.dialogVisible=true;
 				var data={};
 				data.search='';
 				data.warehouseid=state.formData.warehouseid;
 				
 				data.marketplaceid=state.formData.marketplaceid;
 				data.groupid=state.formData.groupid;
				if(state.formData.invtype!=2){
					data.ftype='shipment';
					prodiaRef.value.ftype='shipment';
				}else{
					data.ftype='product';
					prodiaRef.value.ftype='product';
				}
 				prodiaRef.value.params=data;
 				prodiaRef.value.loadData();
 			}
 			 function getdata(data){
 				 if(data.length>0){
 					 var skulist="";var oldskulist="";
 					 if(state.productlist.length>0){
 						 state.productlist.forEach(function(items){
 						 	 oldskulist+=(items.sku+",");
 						 }); 
 					 }
 					 data.forEach(function(item){
 						 var haslength=oldskulist.indexOf(item.sku+",");
 						 if(haslength<0||oldskulist==""){
 							   skulist+=(item.sku+",");
 							   if(item.msku==undefined || item.msku==null || item.msku==''){
 							   	 item.msku=item.sku;
 							   }
							   item.areCasesRequired="0";
							   item.prepOwner="SELLER";
							   item.labelOwner="SELLER";
 							   state.productlist.push(item);
 						 }
 						
 					 });
 					 state.totalproducts=state.productlist.length;
					 loadInventory(state,state.productlist,state.formData.warehouseid);
 					 if(skulist!=""){
 						 validSkuList(state,skulist);
 					 }
 				 }
 				  
 			 }
 			 
 			 function removeBind(index){
 				 state.productlist.splice(index,1);
 				 state.totalproducts=productlist.length;
 			 }
			 function operateMore(row){
				 //expiration 保质期  labelOwner 贴标人  prepOwner 预备信息处理人
				state.operatorVisiable=true;
				state.operatorRow=row;
			 }
		 
 			 function submitPlan(){
 				 var itemlist=[];
 				 state.formData.groupid=state.formData.groupid;
 			     state.formData.planmarketplaceid=state.queryData.marketplaceid;
 				 state.formData.planid=state.queryData.planid;
				 state.formData.batchnumber=state.queryData.batchnumber;
 				 state.formData.issplit=state.queryData.issplit;
 				 state.productlist.forEach(function(item){
 					 var row=item;
 					 row.QuantityShipped=item.quantity;
					 if(state.formData.arecasesrequired){
					    row.boxnum=item.boxnum;
					 }
 					 row.materialid=item.id;
 					 row.sellersku=item.sku;
					 row.opttime=null;
 					 itemlist.push(row);
 				 });
 				 state.formData.planitemlist=itemlist;
 				 state.submitloading=true;
				 if(props.isappend){
					 state.submitloading=false;
					 emit("change",state);
				 }else{
					 if(!state.formData.sourceAddress){
						  ElMessage.error('地址信息未选中，请刷新重试！');
						  return;
					 }
					shipmentplanApi.saveInboundPlan(state.formData).then((res)=>{
					 					 ElMessage.success('已提交成功！');
					 					  state.submitloading=false;
					 					   // closeTab();
					 						router.push({
					 							path: '/newshipmentdetails',
					 							query: {
					 								id: res.data,
					 								title: "新货件详情",
													replace:true,
					 								path: '/newshipmentdetails',
					 							}
					 						})
					}).catch(error=>{
										  state.submitloading=false;
					}) 
				 }
 				 
 			 }
 			
 			 function cancelPlan(){
 				state.productlist=[];
 				state.totalproducts=0;
				emit("cancel");
 			 }
 			 function loadPlanData(){
				 handleLoadPlanData(state,data=>{
					  groupChange(data.groupid,'init');
				 })
 			 }
 		
 			 function showProductModal(){
 				 state.downloadVisible=true;
 			 }
 	
 			 //上传文件的事件
 			 function uploadFile(item){
 			 	//上传文件的需要formdata类型;所以要转
 			 	state.logofile=item.file;
 			 }
 		
 	
 			 function formatQuantity(row){
 				 row.quantity=CheckInputInt(row.quantity);
 			 }
			 function init(formData){
				 state.formData=formData;
			 }
			defineExpose({init});
</script>

<style >
	 
	.m-b-16{
		margin-bottom:16px;
	}
	.list-from {
		margin-top: 16px;
	}
	.addr-info{
		text-align: left;
		line-height: 1.5;
		min-height:170px;
	}
	.addr-header{
		border-bottom: 1px solid var(--el-border-color-light);
		padding-bottom:8px;
		margin-bottom:8px;
		position: relative;
	}
	.addr-header .el-tag{
		position: absolute;
		top: -13px;
		right: -21px;
	}
	.list-from .el-radio-button__original-radio:checked+.el-radio-button__inner{
		color:inherit;
		background-color:inherit;
	}
	.list-from .el-radio-button{
		display: grid;
	}
	.adrr-btn{
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.myformitem .el-form-item__content,.myformitem .el-radio-group{
		display:block;
	}
	.fro-head{
		margin:16px 24px;
		display:flex;
		align-items: center;
		justify-content: space-between;
	}
	.fro-body{
		padding:0px 24px 16px 24px;
	}
</style>
