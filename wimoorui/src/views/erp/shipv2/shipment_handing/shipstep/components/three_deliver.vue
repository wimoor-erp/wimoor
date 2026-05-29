<template>
	<div class="transportation-wrap">
		<el-row :gutter="16">
			<el-col :span="12" >
				<div class="shi-left">
					<el-card shadow="never">
						<el-form :model="form" v-if="form.isEdit" label-width="120px" >
							<el-form-item label="运输方式" prop="transtype" >
									 <el-space>
										<el-select v-model="form.transtype"  style="width:400px" placeholder="请选择" fit-input-width="true" @change="changeTransType">
											 <el-option
												  v-for="item in tranlist.list"
												  :key="item.id"
												  :label="item.name"
												  :value="item.id"
												/>
										</el-select>
										<el-button
										      key="warning"
										      type="warning"
										      link
										      >
											  <el-icon @click="compareVisible=true"
											  	style="font-size:20px">
											  	<balance-two theme="outline" fill="#FF6700" :strokeWidth="2" />
											  </el-icon>
											  </el-button >
								 </el-space>
							</el-form-item>
							<el-form-item label="物流承运商" prop="company">
								 <el-space>
										<el-select v-model="form.company" style="width:400px" placeholder="请选择" @change="changeCompany">
											<el-option
												 v-for="item in companylist.list"
												 :key="item.id"
												 :label="item.name"
												 :value="item.id"
											   />
										</el-select>
								    
										<el-link type="info" @click="infoVisiable=true"  :underline="false">
											<el-icon>
												<View />
											</el-icon>&nbsp;查看报价
										</el-link>
								 </el-space>
							</el-form-item>
							<el-form-item label="物流渠道" prop="channel">
								<el-select v-model="form.channel" @change="setFormData" style="width:400px" filterable  placeholder="请选择">
									<el-option
									     v-for="item in channellist.list"
									     :key="item.id"
									     :label="item.channame"
									     :value="item.id"
									   />
								</el-select>
							</el-form-item>
							<el-form-item label="物流系统编号">
							      <el-input v-model="form.ordernumber" />
							    </el-form-item>
								<el-form-item label="物流发货日期">
								    <el-date-picker
								            v-model="form.shipdate"
								            type="date"
								            placeholder="选择日期"
								          />
								</el-form-item>
								<el-form-item label="预计到货日期">
								    <el-date-picker
								            v-model="form.arrdate"
								            type="date"
								            placeholder="选择日期"
								          />
							    </el-form-item>
											
								<el-form-item label="实际结算">
									<div class="mt-4">
									    <el-input
									      v-model="form.rweight"
										  @input="calculatefee" 
										  clearable
									      placeholder="请输入"
									      class="input-with-select"
									    >
									      <template #prepend>
									        <el-select v-model="form.wtype"   style="width: 115px">
									          <el-option label="重量" value="0"  />
									          <el-option label="体积" value="1" />
									        </el-select>
									      </template>
									      <template #append>
									       <el-select v-model="form.wunit"   style="width: 115px">
									                 <el-option label="kg" value="kg" />
									                 <el-option label="cbm" value="cbm" />
									        </el-select>
									      </template>
									    </el-input>
									  </div>
								</el-form-item>
								<el-form-item label="单价">
								     <el-input v-model="form.singleprice" clearable v-dataType:float @input="calculatefee"  placeholder="请输入单价">
								           <template #append>￥</template>
								        </el-input>
								</el-form-item>
								<el-form-item label="关税等其它费用">
								     <el-input v-model="form.otherfee" clearable @input="calculatefee" placeholder="请输入关税等其它费用">
								           <template #append>￥</template>
								        </el-input>
								</el-form-item>
								<el-form-item label="货件出港日期">
								    <el-date-picker
								            v-model="form.outtime"
								            type="date"
								            placeholder="选择日期"
								          />
								</el-form-item>
								<el-form-item label="货件到港日期">
								    <el-date-picker
								            v-model="form.intime"
								            type="date"
								            placeholder="选择日期"
								          />
								</el-form-item>
								<el-form-item label="备注">
									 <el-input v-model="form.remark" type="textarea" />
								</el-form-item>
								<el-form-item>
								      <el-button type="primary"  @click="saveSelfTrans()">保存</el-button>
								      <el-button @click="openHistory">修改记录</el-button>
									   <!-- <el-button @click="openCosUpload">上传/下载报关资料</el-button> -->
									  <el-button @click="showFeeDialog">修改费用</el-button>
								    </el-form-item>
						</el-form>
						
						<el-form   v-else label-width="120px"  class="ship-three-deliver">
							<el-form-item label="运输方式" prop="transtype" >
									 <el-space>
										 <span>{{transData.transtyle}}</span>
										<el-button
										      key="warning"
										      type="warning"
										      link
										      >
											  <el-icon @click="compareVisible=true"
											  	style="font-size:20px">
											  	<balance-two theme="outline" fill="#FF6700" :strokeWidth="2" />
											  </el-icon>
											  </el-button >
								 </el-space>
							</el-form-item>
							<el-form-item label="物流承运商" prop="company">
								 <el-space>
									 {{transData.company}}
										<el-link type="info" @click="infoVisiable=true"  :underline="false">
											<el-icon>
												<View />
											</el-icon>&nbsp;查看报价
										</el-link>
								 </el-space>
							</el-form-item>
							<el-form-item label="物流渠道" prop="channel">
								{{transData.channame}}
							</el-form-item>
							<el-form-item label="渠道类型" prop="channel">
								{{transData.channeltype}}
							</el-form-item>
							<el-form-item label="区域" v-if="transData.subarea" prop="channel">
								{{transData.subarea}}
							</el-form-item>
							<el-form-item label="物流系统编号">
								{{transData.ordernum}}
								
							    </el-form-item>
								<el-form-item label="物流发货日期">
									{{dateFormat(form.shipdate)}}
								    
								</el-form-item>
								<el-form-item label="预计到货日期">
									{{dateFormat(transData.arrivalTime)}}
							    </el-form-item>
					
								<el-form-item label="实际结算">
									<div class="mt-4">
										{{transData.transweight}}{{transData.wunit}}
									  </div>
								</el-form-item>
								<el-form-item prop="wtype" label="收费方式" width="100" >
										<span v-if="transData.wtype==0">
											重量(kg)
										</span>
										<span v-else>
											体积(cbm)
										</span>
								</el-form-item>
								<el-form-item label="单价">
									{{transData.singleprice}}
								</el-form-item>
								<el-form-item label="关税等其它费用">
									{{transData.otherfee}}
								</el-form-item>
								<el-form-item label="货件出港日期">
									{{dateFormat(transData.outarrtime)}}
								</el-form-item>
								<el-form-item label="货件到港日期">
									{{dateFormat(transData.inarrtime)}}
								</el-form-item>
								<el-form-item label="备注">
									{{transData.remark}}
								</el-form-item>
								<el-form-item>
								      <el-button type="primary"  @click="form.isEdit=true;">编辑</el-button>
								      <el-button @click="openHistory">修改记录</el-button>
									   <!-- <el-button @click="openCosUpload">上传/下载报关资料</el-button> -->
									  <el-button @click="showFeeDialog">修改费用</el-button>
								    </el-form-item>
						</el-form>
					</el-card>
				</div>
			</el-col>
			<el-col :span="12"  >
				<div>
				 
					<el-table :data="boxinfolist.list" border  v-load="pkgLoading"  height="calc(100vh - 260px)" show-summary="true" >
						<el-table-column prop="id" label="箱号" width="135"   />
						<el-table-column prop="" label="追踪编号" width="210">
							<template #header>
								<span>追踪编号</span>
								<el-link type="primary" @click.stop="refreshBoxInfo" style="font-size: 12px;margin-left: 10px;">拉取</el-link>
							</template>
							<template #default="scope">
							
								<el-input v-if="scope.$index==0"  v-model="scope.row.tracking_id">
								<template #suffix>
								          <el-icon  @click="trackingChange(scope.row,true)" class="el-input__icon pointer"><DCaret /></el-icon>
								        </template>
								</el-input>
								<el-input v-else  v-model="scope.row.tracking_id"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="qty" label="装箱数量" width="80"   />
						<el-table-column prop="weight" label="重量(kg)" width="80"   />
						<el-table-column prop="volume" label="尺寸(cm)材积"  >
							<template #default="scope">
								<span>{{scope.row.length}}</span>*
								<span>{{scope.row.width}}</span>*
								<span>{{scope.row.height}}</span>
								({{scope.row.volume}})
							</template>
						</el-table-column>
					</el-table>
			 
					<div style="padding-top:20px;padding-bottom: 10px;">
						<el-row>
							<el-col :span="16"><Operation ref="operationRef" @change="handleOperationChange"></Operation></el-col>
						    <el-col :span="8" class="text-right"><el-button  :loading="saveTransLoading"  type="primary"  @click="saveTransTrace('Tracking')"> 提交追踪编号(标发货件) </el-button></el-col>
						</el-row>
					</div>
				</div>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="24">
				<div class="shi-sum-data">
					<el-space :size="32">
						<div>运输费用: <span >￥{{shipfee}}</span></div>
						<div>物流费用总计: <span >￥{{formatFloat(totalfee)}}</span></div>
					</el-space>
				</div>
			</el-col>
		</el-row>
		<el-dialog v-model="historyDialogVisible" title="历史信息" width="80%"  top="5vh" center>
			<el-table :data="historylist.list" height="calc(100vh - 350px)">
				<el-table-column prop="optname" label="操作人" width="100"   />
				<el-table-column prop="opttime" label="操作时间" width="100">
					<template  #default="scope">
						{{dateFormat(scope.row.opttime)}}
					</template>
				</el-table-column>
				<el-table-column prop="transtypename" label="运输方式" width="100"   />
				<el-table-column prop="companyname" label="物流承运商" width="100"   />
				<el-table-column prop="channame" label="物流渠道" width="100"   />
				<el-table-column prop="arrivalTime" label="预计到货日期" width="100" >
					<template  #default="scope">
							{{getValue(dateFormat(scope.row.arrivalTime))}}
					</template>
				</el-table-column>
				<el-table-column prop="wtype" label="收费方式" width="100" >
						<template  #default="scope">
							<span v-if="scope.row.wtype=='0'">
								重量(kg)
							</span>
							<span v-else>
								体积(cbm)
							</span>
						</template>
				</el-table-column>
				<el-table-column prop="transweight" label="计价单位" width="100"  >
					<template  #default="scope">
							<span>{{getValue(scope.row.transweight)}}</span>
							({{getValue(scope.row.wunit)}})
					</template>
				</el-table-column>	
				<el-table-column prop="singleprice" label="单价" width="50"  />
				<el-table-column prop="otherfee" label="关税等其它费用" width="100"  />
				<el-table-column prop="outarrtime" label="货件出港日期" width="100" >
					<template  #default="scope">
							{{getValue(dateFormat(scope.row.outarrtime))}}
						</template>
				</el-table-column>
				<el-table-column prop="inarrtime" label="货件到港日期" width="100" >
					<template  #default="scope">
							{{getValue(dateFormat(scope.row.inarrtime))}}
						</template>
				</el-table-column>
				<el-table-column prop="remark" label="备注" width="100"  />
				<el-table-column prop="singleprice" label="运输费用"   >
					<template  #default="scope">
						{{getValue(feefunc(scope.row.singleprice,scope.row.transweight))}}
						</template>
				</el-table-column>
				<el-table-column prop="singleprice" label="物流费用总计"   >
					<template  #default="scope">
						  {{getValue(feefunc(scope.row.singleprice,scope.row.transweight)+scope.row.otherfee)}}  
						</template>
				</el-table-column>
			</el-table>
			<template #footer>
			  <span class="dialog-footer">
			    <el-button @click="historyDialogVisible = false">关闭</el-button>
			  </span>
			</template>
		</el-dialog>
		
		<el-dialog v-model="cosDialogVisible" title="上传/下载报关资料" width="60%" center>
			<div>
				<el-row :gutter="16">
					<el-col :span="12" style="border-right: 1px solid #dedede;">
						<!-- <el-button style="margin-bottom: 20px;" type="primary" @click="downloadCustoms" >批量下载</el-button> -->
						<div style="margin-bottom: 20px;">历史上传记录 </div>
						<el-table   :data="customslist.list"  >
							<!-- <el-table-column type="selection" width="50" /> -->
							<el-table-column prop="filename" label="文件名"    />
							<el-table-column prop="username" label="操作人" width="100"   />
							<el-table-column prop="opttime" label="操作时间" width="150"  >
								<template  #default="scope">
									  {{dateTimesFormat(scope.row.opttime)}}  
								</template>
						    </el-table-column>
							<el-table-column prop="username" label="操作" width="100" >
								<template  #default="scope">
									 <span style="color: #0055ff;cursor: pointer;margin-right: 5px;" @click="downloadCustoms(scope.row.id)">下载</span>  
									 <span style="color: #0055ff;cursor: pointer;" @click="deleteFtpFile(scope.row.id)">删除</span>  
								</template>
							</el-table-column>
							
						</el-table>
					</el-col>
					<el-col :span="12">
						<el-upload
								class="upload-demo" 
								action   
								:http-request="uploadFile" 
								ref="upload"
								:limit="1" 
								:file-list="fileList" 
								:on-exceed="handleExceed" 
								:before-upload="beforeUpload" 
								:show-file-list="true" 
								:headers="headers" 
								accept=".docx,.doc,.xlsx,.xls,.txt"
								>
								<!-- action="/api/file/fileUpload" -->
								<el-button class="btn"><i class="el-icon-paperclip"></i>选择文件</el-button>
								<template #tip>
								  <div class="el-upload__tip">
								    文件后缀名:docx/doc/xlsx/xls/txt,小于50MB.
								  </div>
								</template>
							</el-upload>
							
							<el-button @click="uploadCusFile" type="primary">上传</el-button>
						<div style="margin-top: 5px;">按照模板定义格式导入数据。没有模板？<span style="color: #0055ff;cursor: pointer;" @click="downloadDataTemplate">点击下载</span></div>
					</el-col>
				</el-row>
			</div>
			
		</el-dialog>
		
	 
		
				<el-dialog v-model="compareVisible" title="物流比价" top="1vh" width="70%" >
					<CompareList ref="comareRef" :isdialog="true" :shipform="form" :shipdata="shipdata"></CompareList>
					<template #footer>
						<span class="dialog-footer">
							<el-button @click="compareVisible = false">关闭</el-button>
							<el-button type="primary" @click="selectTrans()">确认</el-button>
						</span>
					</template>
				</el-dialog>
				
			<el-dialog class="excelinfo" v-model="infoVisiable" width="860px" title="详情">
				<div v-if="pricemessage!=''">{{pricemessage}}</div>
				<iframe :src="getFrameUrl(form.company)" width="100%" height='423px' style="padding:0;margin:0"
				 frameborder='0'>未上传报价单 </iframe>
				<template #footer>
					<span class="dialog-footer">
						<el-button type="primary" @click="infoVisiable=false">关闭</el-button>
					</span>
				</template>
			</el-dialog>
	</div>
	<FeeEdit ref="editFeeRef"  @change="reloadPage"></FeeEdit>
</template>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import ShipmentOpt from"./shipment_operator.vue"
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import shipmenthandlingV2Api from '@/api/erp/shipV2/shipmentPlacementApi.js';
    import transportationApi from '@/api/erp/ship/transportationApi.js';
	import { useRoute,useRouter } from 'vue-router'
	import {formatFloat,dateFormat,dateTimesFormat,CheckInputInt,CheckInputFloat,CheckALLFloat} from '@/utils/index.js';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import { ElTable } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	import FeeEdit from '@/views/amazon/payment/jobCosting/components/edit.vue';
	import CompareList from "@/views/erp/ship/transportation/components/compare.vue"
	import { View ,DCaret} from '@element-plus/icons-vue'
	import {BalanceTwo} from '@icon-park/vue-next';
	import {getValue} from '@/utils/index.js';
	import PackageStatus from '@/model/amazon/ship/PackageStatus.json';
	import TransportStatus from '@/model/amazon/ship/TransportStatus.json';
	import Operation from "@/views/erp/shipv2/shipment_add/shipstep/components/operation.vue";
	import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
	import materialApi from '@/api/erp/material/materialApi.js';
	const emit = defineEmits(['stepdata',"change"]);
	let props = defineProps({onlyTrans:null });
	const { onlyTrans } = toRefs(props);
	const operationRef=ref();
	let editFeeRef=ref();
	let router = useRouter()
	let shipdata = ref({})
	let shipfee = ref(0)
	let totalfee = ref(0)
	let historyDialogVisible = ref(false)
	let withTransSubmit=ref(true);
	let cosDialogVisible = ref(false)
	let isPartnered=ref(false);
	let materialDialogVisible = ref(false)
	let submitloading=ref(false);
	let compareVisible = ref(false);
	let saveTransLoading=ref(false);
	let comareRef = ref(CompareList);
	let pkgLoading=ref(false);
	let myshowtype=ref("");
	//上传后的文件列表
	let fileList = ref([])
	let infoVisiable = ref(false);
	let transportStatus=ref("");
	// 运行上传文件大小，单位 M
	let fileSize = ref(2);
	let transData=ref({});
	//请求头
	let headers = ref({
		"Content-Type": "multipart/form-data"
	})
	let cusfile = ref()
	let shipmentstatus=ref();
	let amzshipmentstatus=ref();
	let form = reactive({
		carrier: '',
		arrdate: '',
		shipdate: '',
		remark: '',
		transtype: '',
		tranType:"SP",
		company: '',
		channel: '',
		wtype: '0',
		wunit: 'kg',
		rweight: 0,
		singleprice: 0,
		otherfee: 0,
		isEdit:true,
		outtime: '',
		intime: '',
		ordernumber: '',
		consWarehouseid:null,
		boxWarehouseid:null,
	})
	let tranlist = reactive({
		list: []
	})
	let companylist = reactive({
		list: []
	})
	let channellist = reactive({
		list: []
	})
	let carrlist = reactive({
		list: []
	})
	let boxinfolist = reactive({
		list: []
	})
	let historylist = reactive({
		list: []
	})
	let materiallist = reactive({
		list: []
	})
	let customslist = reactive({
		list: []
	})
	let boxListData = reactive({
		list: []
	})
	let packageSkuListData = reactive({
		list: []
	})
	let shipInfoAll={};
	let pricemessage=ref("");
	var param={sumBoxWeight:0,sumBoxVolume:0};
			function trackingChange(row,allfill){
				boxinfolist.list.forEach(item=>{
					   if(allfill){
						  item.tracking_id=row.tracking_id;
					   }else if(!item.tracking_id){
						   item.tracking_id=row.tracking_id;
					   }
				})
			}
			async function getTransTypeAll() {
				let res = await transportationApi.getTransTypeAll();
				if (res.data) {
					tranlist.list = res.data;
					if (form.transtype == "") {
						form.transtype = res.data[0].id;
					}
				}
			}
			function handleOptChange(){
				context.emit("change");
			}
			function changeTransType(val, type) {
				if (val&&val.id) {
					getCompanyTranstypeList(val.id, type);
				} else {
					getCompanyTranstypeList(val, type);
				}
			}

		function showFeeDialog(){
			//查询itemlist 然后show
			shipInfoAll.itemlist.forEach(item=>{
				item.QuantityShipped=item.quantity;
				item.groupname=shipdata.value.groupname;
				item.marketname=shipdata.value.country;
				item.shipmentid=item.shipmentId;
			});
			editFeeRef.value.show(shipInfoAll.itemlist,'isV2');
		}	
		function estimateTransTrace(){
			saveTransLoading.value=true;
			shipmenthandlingApi.estimateTransTrace({"shipmentid":shipInfoAll.shipmentid}).then(res=>{
				saveTransLoading.value=false;
				loadCartInfo();
			}).catch(e=>{
				 saveTransLoading.value=false;
			})
		}
		function confirmTransTrace(){
			saveTransLoading.value=true;
			shipmenthandlingApi.confirmTransTrace({"shipmentid":shipInfoAll.shipmentid}).then(res=>{
				saveTransLoading.value=false;
				loadCartInfo();
			}).catch(e=>{
				 saveTransLoading.value=false;
			})
		}
		function loadCarrier(){
			  var tranType=form.tranType;
			  if(shipdata.value.market!=null){
				  shipmenthandlingApi.getCarrier({
					"country":shipdata.value.market,
					"transtyle":tranType
				  }).then(res=>{
					   carrierData.list=res.data;
				  })
			  }
		}
			function getFrameUrl(company) {
				let url = "";
				companylist.list.forEach(item=>{
					if (item.id ==company ) {
						url = item.uploadpath;
					}
				});
				if(url&&url!=""){
					if(url.indexOf("http")>=0){
						pricemessage.value="";
					  return "https://view.officeapps.live.com/op/embed.aspx?src=" + url;
					}else{
						pricemessage.value="暂未报价"
						return "";
					}
				}else{
					pricemessage.value="暂未报价"
					return "";
				}
			}
			
			function changeCompany(val, type) {
				getChannel(type) 
			}

			function getCompanyTranstypeList(val, type) {
				transportationApi.getCompanyTranstypeList({
					"marketplaceid": shipdata.value.marketplaceid,
					"transtype": val
				}).then(res => {
					if (res.data && res.data.length > 0) {
						companylist.list = res.data;
						if (type != 'init') {
							form.company = res.data[0].id;
						}
						getChannel(type);
					} else {
						companylist.list = [];
						form.company = null;
						channellist.list = [];
						form.channel = null;
					}
				})
			}
			function setFormData(type){
				var channel=null;
				 channellist.list.forEach(item=>{
					 if(item.id==form.channel){  channel=item;  }
				 })
				if(type=="init"){
					if(channel&&channel.pretime>0&&!form.arrdate){
						form.arrdate=new Date(new Date().getTime() + 3600 * 1000 * 24 * (channel.pretime));
					}
					if(!form.shipdate){
						form.shipdate=new Date();
					}
					if(!form.singleprice){
						form.singleprice=channel.price;
					}
					if(!form.singleprice){
						form.singleprice=channel.price;
					}
					 
					loadCartInfo();
					calculatefee();
				}else{
					if(channel&&channel.pretime>0){
						form.arrdate=new Date(new Date().getTime() + 3600 * 1000 * 24 * (channel.pretime));
					}else{
						form.arrdate=new Date(new Date().getTime() + 3600 * 1000 * 24 * 30);
					}
					if(!form.shipdate){
						form.shipdate=new Date();
					}
					 form.singleprice=channel.price;
				   loadCartInfo();
				   calculatefee(); 
				}
			
				
				 
			}
			function getChannel(type) {
				transportationApi.getChannel({
					"marketplaceid": shipdata.value.marketplaceid,
					"transtype": form.transtype,
					"company": form.company
				}).then(res => {
					if (res.data) {
						channellist.list = res.data;
						var defaultchannel=null;
						if (type != 'init') {
							form.channel = res.data[0].id;
						}
					   setFormData(type);
					}
				})
			}
 

			function calculatefee() {
					if(form.otherfee){
						form.otherfee=CheckALLFloat(form.otherfee);
					}
					form.rweight=CheckInputFloat(form.rweight);
					shipfee.value = formatFloat(form.singleprice * form.rweight);
					totalfee.value = formatFloat((form.singleprice * form.rweight) + parseFloat(form.otherfee));
			}

			function selectTrans() {
				let selection = comareRef.value.getSelection();
				if (selection && selection.length > 1) {
					ElMessage.warning('请勿选择多行！');
				} else if (selection && selection.length == 0) {
					ElMessage.warning( '请选择一行记录！');
				} else if (selection) {
					form.company = selection[0].company;
					form.channel = selection[0].id;
					changeTransType(form.transtype, 'init');
					compareVisible.value = false;
				}

			}

			function getShipAmazonInfo(res) {
					var data = res.data;
					shipmentstatus.value=res.data.shipment.status;
					amzshipmentstatus.value=res.data.shipmentAll.shipmentstatus;
					if (data.listbox && data.listbox.length > 0) {
						boxinfolist.list = data.listbox;
						data.listbox.forEach(item=>{
							if(item.package_status){
								 item.packagestatus=PackageStatus[item.package_status];
							} 
							param.sumBoxWeight=param.sumBoxWeight+item.weight;
							param.sumBoxVolume=param.sumBoxVolume+item.volume;
						})
					 param.sumBoxWeight=formatFloat(param.sumBoxWeight);
					 param.sumBoxVolume=formatFloat(param.sumBoxVolume);
					}
					form.shipdate = data.shipment.shipedDate;
					form.tranType=data.shipment.transtyle;
					if(!form.tranType){
						form.tranType="SP";
					}
					
					if (data.transinfo) {
						transData.value=data.transinfo;
						form.singleprice = data.transinfo.singleprice;
						if(data.transinfo.transweight>0){
							form.rweight  = data.transinfo.transweight;
						}else{
							var weight =  data.sumweight>data.volume?data.sumweight:data.volume;
							form.rweight = Math.ceil(weight);
							
						}
						if (data.transinfo.wtype == "0") {
							form.wtype = '0';
						} else {
							form.wtype = '1';
						}
					   if(data.transinfo.wunit){
						  form.wunit = data.transinfo.wunit;
					   }
						if (data.transinfo.arrivalTime) {
							form.arrdate = data.transinfo.arrivalTime;
						}
						if (data.transinfo.outarrtime) {
							form.outtime = data.transinfo.outarrtime;
						}
						if (data.transinfo.inarrtime) {
							form.intime = data.transinfo.inarrtime;
						}
						if (data.transinfo.ordernum) {
							form.ordernumber = data.transinfo.ordernum;
						}
						if (data.transinfo.otherfee) {
							form.otherfee = data.transinfo.otherfee;
						}
						if (data.transinfo.remark) {
							form.remark = data.transinfo.remark;
						}
					}else{
						transData.value={};
					}
					if (data.transchannel) {
						form.transtype = data.transchannel.transtype;
						form.company = data.transchannel.company;
						form.channel = data.transchannel.id;
						if(!form.singleprice){
							form.singleprice=data.transchannel.price;
							
						}
						if(data.transchannel.pretime>0&&!form.arrdate){
							form.arrdate=new Date(new Date().getTime() + 3600 * 1000 * 24 * (data.transchannel.pretime));
						}
						if(!form.shipdate){
							form.shipdate=new Date();
						}
						
						changeTransType(form.transtype, 'init');
					} else {
						changeTransType(form.transtype);
					}
					if (data.shipment) {
						if (data.shipment.carrier) {
							form.carrier = data.shipment.carrier;
						}
					}
					transportStatus.value=res.data.shipment.transportStatus;
					if(data.shipment&&TransportStatus[data.shipment.transportStatus]){
						 transportStatus.value=TransportStatus[data.shipment.transportStatus];
					}
					loadCartInfo();
					loadCarrier();
			}
			
			function loadCartInfo(){
				pkgLoading.value=true;
				var hastrack=false;
				shipmenthandlingV2Api.getShipCart({"shipmentid": shipInfoAll.shipmentid,"channelid":form.channel}).then(res => {
					pkgLoading.value=false;
					if(res.data){
					 boxinfolist.list = res.data.cart;
					 if(res.data.cart){
						 res.data.cart.forEach(item=>{
							 if(item.tracking_id&&item.tracking_id!="123456"&&item.tracking_id!="a123"&&item.tracking_id!="a123456"&&item.tracking_id!=item.id){
								 hastrack=true;
							 }
						 })
					 }
					 param.sumBoxWeight=0.0;
					 param.sumBoxVolume= 0.0;
					  boxinfolist.list.forEach(item=>{
					 							param.sumBoxWeight=formatFloat(param.sumBoxWeight)+formatFloat(item.weight);
					 							param.sumBoxVolume=formatFloat(param.sumBoxVolume)+formatFloat(item.volume);
					 						})
					
					 var channel=null;
					  channellist.list.forEach(item=>{
					 	 if(item.id==form.channel){  channel=item;  }
					  })
					 if(!transData.value||!transData.value.transweight||!form.rweight){
					     form.rweight=param.sumBoxWeight>param.sumBoxVolume?param.sumBoxWeight:param.sumBoxVolume;
					 }
					 transportStatus.value=res.data.shipment.transportStatus;
					 if(hastrack){
					    operationRef.value.show(shipInfoAll.shipment.formid,"updateShipmentTrackingDetails");
					 }
					}
				})
						
			}
			function getDateValue(value){
				if(value){
					if (('' + value).length === 10) {
					   value = parseInt(value) * 1000;
					} else if(('' +value).indexOf("-")>0||('' +value).indexOf("/")>0){
					   value= new Date(value);;
					}
				}
				return value;
			}
			function saveSelfTrans(type,callback) {
				var data = {};
				data.shipmentid = shipInfoAll.shipmentid;
				data.company = form.company;
				data.channel = form.channel;
				data.unit = form.wunit;
				data.wtype = form.wtype;
				data.transtype = form.transtype;
				data.ordernum = form.ordernumber;
				data.remark = form.remark;
				data.rweight = form.rweight;
				data.singleprice = form.singleprice;
				data.otherfee = form.otherfee;
				data.arrive = form.arrdate;
				data.arrive=getDateValue(data.arrive);
				data.shipdate = form.shipdate;
				data.shipdate=getDateValue(data.shipdate);
				data.outarrivaldate = form.outtime;
				data.outarrivaldate=getDateValue(data.outarrivaldate);
				data.inarrivaldate = form.intime;
				data.inarrivaldate=getDateValue(data.inarrivaldate);
				shipmentTransportationApi.saveSelfTrans(data).then(res => {
					if(type=="submitbatch"){
						if(callback){
							callback();
						}
					}else{
						ElMessage.success('操作成功！');
						reloadPage();
					}
					
				}) 
			}
			function changeWarehouse(id){
				form.consWarehouseid=id;  
			}
			function changeBoxWarehouse(id){
				form.boxWarehouseid=id;  
			}
			function reloadPage(){
				var status= parseInt(shipdata.value.status);
				status=status-2;
				status=status>3?3:status;
				status=status<0?0:status;
				emit("stepdata", status,shipdata.value.shipmentid);
			}
 
			//保存物流跟踪
			function saveTransTrace(type){
				var data={};
				var boxinfos = [];
				saveTransLoading.value=true;
				boxinfolist.list.forEach(function(item, index) {
					var trackinginfo = {};
					trackinginfo.boxnum = item.boxnum;
					trackinginfo.id=item.id;
					trackinginfo.value = item.tracking_id;
					boxinfos.push(trackinginfo);
				})
				data.boxinfo = boxinfos;
				data.carrier = form.carrier;
				data.shipmentid = shipInfoAll.shipmentid;
				data.transtype=form.tranType;
				data.actiontype=type;
				shipmenthandlingV2Api.saveTransTrace(data).then(res => {
					ElMessage.success('操作成功！');
					saveTransLoading.value=false;
					pkgLoading.value=true;
					if(res.data && res.data.operationid){
						operationRef.value.show(res.data.operationid,0);
					}
				}).catch(e=>{
					saveTransLoading.value=false;
				})
				
			}
			function handleOperationChange(data){
				//跳转路由至第二步
				if(data && data.operationStatus=="SUCCESS"  ){
					var param={};
					param.shipmentid = shipInfoAll.shipmentid;
					if(parseInt(shipInfoAll.shipment.status)<6){
						shipmenthandlingV2Api.feedbackTransTrace(param).then(res=>{
							 ElMessage.success('操作成功！');
						})
					}
				    
				}
				
			}
			function openHistory() {
				historyDialogVisible.value = true;
				shipmentTransportationApi.getSelfTransHis({
					"shipmentid": shipInfoAll.shipmentid
				}).then(res => {
					if (res.data && res.data.length > 0) {
						historylist.list = res.data;
					}
				})

			}

			function openCosUpload() {
				cosDialogVisible.value = true;
				getShipmentCustoms();
			}

			function getShipmentCustoms() {
				shipmenthandlingApi.getShipmentCustomsRecord().then(res => {
					if (res.data && res.data.length > 0) {
						customslist.list = res.data;
					}
				})
			}
		 
			
		 

			function feefunc(value1, value2) {
				return (parseFloat(value1)) * (parseFloat(value2));
			}

			//删除海关上传记录
			function deleteFtpFile(id) {
				shipmenthandlingApi.deleteCustomsFile({
					"uploadid": id
				}).then(res => {
					if (res.data && res.data.code == "ok") {
						getShipmentCustoms();
						ElMessage.success('操作成功！');
					}
				})
			}
			//下载模板
			function downloadDataTemplate() {

			}
			//超出文件个数的回调
			function handleExceed() {
				ElMessage.error('超出最大上传文件数量的限制！');
				return
			}
			//上传文件的事件
			function uploadFile(item) {
				//上传文件的需要formdata类型;所以要转
				cusfile.value = item.file;
			}
			//文件上传之前
			function beforeUpload(file) {
				if (file.type != "" || file.type != null || file.type != undefined) {
					//截取文件的后缀，判断文件类型
					const FileExt = file.name.replace(/.+\./, "").toLowerCase();
					//计算文件的大小
					const isLt5M = file.size / 1024 / 1024 < 50; //这里做文件大小限制
					//如果大于50M
					if (!isLt5M) {
						ElMessage.error('上传文件大小不能超过 50MB!!');
						return false;
					} else {
						return true;
					}
				}
			}

			function uploadCusFile() {
				let FormDatas = new FormData()
				FormDatas.append('file', cusfile.value);
				shipmenthandlingApi.uploadCustomsFile(FormDatas).then(function(res) {
					if (res.data) {
						if (res.data.code == "ok") {
							getShipmentCustoms();
							ElMessage.success( "上传成功！");
						} else {
							ElMessage.error("上传失败！");
						}
					}
				})
			}
function getBoxDetial(data){
		if(data){
			 boxListData.list=[];
			 data.listbox.forEach((item)=>{
					let isold=false;
				   if(boxListData.list&&boxListData.list.length>0){
						 boxListData.list.forEach((boxitem)=>{
							 if(boxitem.boxlength==item.length
								 &&boxitem.boxwidth==item.width
								 &&boxitem.boxheight==item.height){
									 boxitem.number=boxitem.number+1;
									 isold=true;
								 }
						 });
					 } 
					if(isold==false){
						let obj = {boxlength:item.length,boxwidth:item.width,boxheight:item.height,number:1,editnumber:0,materialid:""};
						boxListData.list.push(obj)
					} 
			 });
		 
		}
}
			async function loadOptData(datas) {
				shipInfoAll=datas;
				shipdata.value = datas.shipmentAll;
				await getTransTypeAll();
				if(!datas.shipmentAll.transtyle){
					datas.shipmentAll.transtyle="SP";
				}
				shipmenthandlingApi.getCarrier({
					"country":datas.shipmentAll.countryCode,
					"transtyle":datas.shipmentAll.transtyle
				}).then(res=>{  
					 carrlist.list=res.data;
				})
				if(datas.transinfo && datas.transinfo.singleprice){
					form.isEdit=false;
				}
				getShipAmazonInfo({"data":datas});
				getBoxDetial(datas);
			}

			function stepclick(step) {
				emit("stepdata", step);
			}
			function loadZhApiDetail(data,companyid,shipmentid){
				if(data&&data.data && data.data.shipment && data.data.shipment.parcels){
					//渲染trackID
					boxinfolist.list.forEach(item=>{
						data.data.shipment.parcels.forEach(parcel=>{
							if(parcel.ext_number){
								var number=parcel.ext_number ;
								if( number==item.id){
									item.tracking_id=parcel.tracking_number?parcel.tracking_number:parcel.number;
								}
							}
							
						});
					})
					
				}else{
					ElMessage.error(data.info);
				}
			}
			  
			  function loadZmApiDetail(data){
			  		if(data&&data.message&&data.message=="请求成功"){
			  			//zmData.value=data;
			  		} 
			     }
			//拉取箱子追踪信息
			function refreshBoxInfo(){
				if(boxinfolist.list && boxinfolist.list.length>0){
					var shipmentid=shipInfoAll.shipment.shipmentConfirmationId;
					transportationApi.shipTransDetial({"companyid": form.company,"shipmentid":shipmentid,"ordernum":form.ordernumber}).then(res=>{
						if(res && res.data.ftype=="ZH"){
							loadZhApiDetail(res.data,form.company,shipmentid);
						}
						if(res && res.data.ftype=="ZM"){
							loadZmApiDetail(res.data);
						}
					})
				}else{
					ElMessage.error('请维护物流追踪信息');
				}
				
			}
				
		   defineExpose({loadOptData})
</script>

<style>
	.transportation-wrap{
		margin-left: 16px;
		margin-right: 16px;
	}
	.shi-left {
		margin-bottom:8px;
	}
	.shi-right .el-table{
		margin-top:16px;
		margin-bottom: 16px;
	}
	.shi-sum-data{
		background-color: var(--el-bg-color);
		padding:16px 24px;
		 font-size: 14px;
		 margin-bottom: 16px;
	}
	.shi-sum-data span{
		    font-weight: 600;
		    font-family: "Helvetica Neue";
			color: var(--el-color-primary);
	}
	.ship-three-deliver .el-form-item--default,.ship-three-deliver .el-form-item {
		margin-bottom: 8px
	}
	
</style>
