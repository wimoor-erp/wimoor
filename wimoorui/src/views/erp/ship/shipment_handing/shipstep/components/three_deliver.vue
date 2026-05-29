<template>
	<div>
		<el-row :gutter="16">
			<el-col :span="24">
				<div class="shi-left">
					<el-card shadow="never">
						<el-form :model="form" v-if="form.isEdit" label-width="120px" >
							<el-form-item label="运输方式" prop="transtype" >
									 <el-space>
										<el-select v-model="form.transtype"  placeholder="请选择" fit-input-width="true" @change="changeTransType">
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
										<el-select v-model="form.company" placeholder="请选择" @change="changeCompany">
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
								<el-select filterable v-model="form.channel" @change="setFormData"  placeholder="请选择">
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
		<el-row class="mar-bot">
				<ShipmentOpt ref="optRef"  @change="handleOptChange"/>
				<div class="rt-btn-group">
					<el-button type="success"   v-if="shipmentstatus>4 "  @click="openMaterialCon('isshow')">查看辅料出库</el-button>
					<el-button type="primary" @click="stepclick(3)" plain>下一步</el-button>
					<el-button type="primary"  v-hasPerm="'amz:ship:conship'" v-if="shipmentstatus<=4 && shipmentstatus>=2"  @click="openMaterialCon('isopt')">确认出库(含辅料)</el-button>
					<el-button type="primary"  v-hasPerm="'amz:ship:shipdone'" v-if="(shipmentstatus<=4 && shipmentstatus>=2)||amzshipmentstatus=='WORKING'"  :loading="submitloading" @click="amazonDoneShipment()">确认出库</el-button>
				  </div>
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
		
		<el-dialog v-model="materialDialogVisible" width="50%" >
			  <template #header>
				  <span v-if="myshowtype!='isshow'">确认出库</span>
				  <span v-else>辅料与箱子出库详情</span>
			    </template>
			<el-card  >
				 <template #header>
				      <div class="card-header">
				        <span>辅料列表</span>
						<Warehouse style="margin-left:10px;" size="small" defaultValue="only"
						 :defaultid="form.consWarehouseid"  @change="changeWarehouse"  />
				      </div>
				</template>
			<el-table :data="materiallist.list" border >
				<el-table-column prop="image" label="图片" width="100" >
					<template  #default="scope">
						<el-image :src="scope.row.image"  style="width: 40px;height: 40px;"></el-image>
					</template>
				</el-table-column>
				<el-table-column prop="sku" label="SKU" width="100"   />
				<el-table-column prop="mname" label="产品名称"     />
				<el-table-column prop="inventoryqty" label="产品库存" width="100"   />
				<el-table-column prop="needamount" label="扣减数量" width="100"  >
					<template  #default="scope">
					    <div v-if="scope.row.out">
							<span class="font-extraSmall">已出库：</span>
							<span class="text-success">{{scope.row.out}}</span>
						</div>
						<el-input v-else    v-model="scope.row.needamount" @input="scope.row.needamount=CheckInputInt(scope.row.needamount)" ></el-input>
					</template>
				</el-table-column>
				<el-table-column prop="residue" label="剩余待扣" width="100"  >
					<template  #default="scope">
						<el-input    v-model="scope.row.residue" @input="scope.row.residue=CheckInputFloat(scope.row.residue)" ></el-input>
					</template>
				</el-table-column>
			</el-table>
			</el-card>
			 <el-card style="margin-top:20px;">
				 <template #header>
				       <div class="card-header">
				         <span>箱子列表</span>
				         <Warehouse style="margin-left:10px;" size="small"  defaultValue="only"
						 :defaultid="form.boxWarehouseid"  @change="changeBoxWarehouse"  />
				       </div>
				 </template>
			<el-table :data="boxListData.list" border >
				<el-table-column prop="boxlength" label="长x宽x高(cm)"  >
				<template #default="scope">
					 {{scope.row.boxlength}}x{{scope.row.boxwidth}}x{{scope.row.boxheight}}
				</template>
				</el-table-column>
				<el-table-column prop="number" label="箱子数量"  >
				</el-table-column>
				<el-table-column prop="materialid" label="对应箱子SKU"  width="200" >
					<template  #default="scope">
						 <el-select v-model="scope.row.materialid" @change="handlePackageSkuChange(scope.row)">
							 <el-option v-for="item in packageSkuListData.list" :value="item.id" :key="item.id" :label="item.sku">{{item.sku}}</el-option>
						 </el-select>
					</template>
				</el-table-column>
				<el-table-column prop="number" label="库存"  >
					<template  #default="scope">
						 <div v-if="scope.row.packagesku">{{scope.row.packagesku.fulfillable}}</div>
					      <div v-else>0</div>
					</template>
					
				</el-table-column>
				<el-table-column prop="number" label="出库数量"   >
					<template  #default="scope">
						<div v-if="scope.row.packagesku&&scope.row.packagesku.out">
							<span class="font-extraSmall">已出库：</span>
							<span class="text-success">{{scope.row.packagesku.out}}</span>
						</div>
						<el-input v-else    v-model="scope.row.editnumber" @input="scope.row.editnumber=CheckInputInt(scope.row.editnumber)" ></el-input>
					</template>
				</el-table-column>
			</el-table>
			 	</el-card>
			<template #footer>
			 <div style="float:left;">
				 <el-checkbox v-model="withTransSubmit">
					同步保存物流信息 
				 </el-checkbox>
			 </div>
			  <span class="dialog-footer">
			    <el-button @click="materialDialogVisible = false">关闭</el-button>
				<el-button type="primary" v-if="myshowtype!='isshow'" :loading="submitloading" @click="submitMaterialCon()">提交出库</el-button>
			  </span>
			</template>
		</el-dialog>
		
				<el-dialog v-model="compareVisible" title="物流比价" width="70%" >
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
	<FeeEdit ref="editFeeRef" @change="reloadPage"></FeeEdit>
</template>
<script setup>
	import {h, ref ,watch,reactive,onMounted} from 'vue'
	import ShipmentOpt from"./shipment_operator.vue"
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
    import transportationApi from '@/api/erp/ship/transportationApi.js';
	import shipmenthandlingV2Api from '@/api/erp/shipV2/shipmentPlacementApi.js';
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
	import materialApi from '@/api/erp/material/materialApi.js';
	const emit = defineEmits(['stepdata',"change"]);
		let editFeeRef=ref();
		let router = useRouter()
		let shipdata = ref({})
		let transData= ref({})
		let shipfee = ref(0)
		let totalfee = ref(0)
		let historyDialogVisible = ref(false)
		let withTransSubmit=ref(true);
		let cosDialogVisible = ref(false)
		let isPartnered=ref(false);
		let materialDialogVisible = ref(false)
		let submitloading=ref(false);
		let optRef = ref();
		let compareVisible = ref(false);
		let saveTransLoading=ref(false);
		let comareRef = ref(CompareList);
		let pkgLoading=ref();
		let myshowtype=ref("");
		const shipmentid = router.currentRoute.value.query.shipmentid;
		//上传后的文件列表
		let fileList = ref([])
		let infoVisiable = ref(false);
		let transportStatus=ref("");
		// 运行上传文件大小，单位 M
		let fileSize = ref(2)
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
			outtime: '',
			intime: '',
			isEdit:true,
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
					item.QuantityShipped=item.quantityShipped;
					item.groupname=shipdata.value.groupname;
					item.marketname=shipdata.value.country;
					item.shipmentid=item.ShipmentId;
				});
				console.log(shipInfoAll);
				editFeeRef.value.show(shipInfoAll.itemlist);
			}	
			function estimateTransTrace(){
				saveTransLoading.value=true;
				shipmenthandlingApi.estimateTransTrace({"shipmentid":shipmentid}).then(res=>{
				    saveTransLoading.value=false;
					loadCartInfo();
				}).catch(e=>{
					 saveTransLoading.value=false;
				})
			}
			function confirmTransTrace(){
				saveTransLoading.value=true;
				shipmenthandlingApi.confirmTransTrace({"shipmentid":shipmentid}).then(res=>{
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
						if(!form.rweight){
							if(channel&&channel.priceunits=="weight"){
								form.rweight=param.sumBoxWeight>param.sumBoxVolume?param.sumBoxWeight:param.sumBoxVolume;
								if(!form.wtype){
									form.wtype="0";
								}
							}else{
								if(!form.wtype){
									form.wtype="1";
								}
							}
						}
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
					     form.singleprice=channel?channel.price:0;
						if(channel&&channel.priceunits=="weight"){
							form.rweight=param.sumBoxWeight>param.sumBoxVolume?param.sumBoxWeight:param.sumBoxVolume;
							if(!form.wtype){
								form.wtype="0";
							}
						}else{
							if(!form.wtype){
								form.wtype="1";
							}
						}
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
						var hastransCart=false;
						shipmentstatus.value=res.data.shipment.status;
						amzshipmentstatus.value=res.data.shipmentAll.shipmentstatus;
						if (data.cart && data.cart.length > 0) {
							boxinfolist.list = data.cart;
							data.cart.forEach(item=>{
								if(item.tracking_id&&(item.tracking_id.indexOf("FBA")==0||item.tracking_id=="123456")){
									 item.package_status=undefined;
									 item.tracking_id="";
								 }else{
									hastransCart=true; 
								 }
								if(item.package_status){
									 item.packagestatus=PackageStatus[item.package_status];
									 if(item.package_status!='CLOSED'){
										 hastransCart=true;
									 }
								} 
								param.sumBoxWeight=param.sumBoxWeight+item.weight;
								param.sumBoxVolume=param.sumBoxVolume+item.volume;
							})
						 param.sumBoxWeight=formatFloat(param.sumBoxWeight);
						 param.sumBoxVolume=formatFloat(param.sumBoxVolume);
						}
						if (data.shipment.status5date&&!form.shipdate) {
							form.shipdate = data.shipment.status5date;
						}
						form.tranType=data.shipment.transtyle;
						if(!form.tranType){
							form.tranType="SP";
						}
						if (data.transinfo) {
							transData.value=data.transinfo;
							form.singleprice = data.transinfo.singleprice;
							form.rweight = data.transinfo.transweight;
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
						}
						if (data.transchannel) {
							form.transtype = data.transchannel.transtype;
							form.company = data.transchannel.company;
							form.channel = data.transchannel.id;
							form.isEdit=false;
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
						
						if(hastransCart==true){
							 loadCartInfo();
						}
						loadCarrier();
				}
				
				function loadCartInfo(){
					pkgLoading.value=true;
					shipmenthandlingApi.getShipCart({"shipmentid": shipmentid}).then(res => {
						pkgLoading.value=false;
						if(res.data){
							var haspkg=false;
						 if(res.data.cart){
							 res.data.cart.forEach(box=>{
								 if(box&&box.tracking_id&&(box.tracking_id.indexOf("FBA")==0||box.tracking_id=="123456")){
									 box.package_status=undefined;
									 box.tracking_id="";
								 }else{
									 haspkg=true;
								 }
								if(box.package_status){
									 box.packagestatus=PackageStatus[box.package_status];
								}
							 })
						 }
						 if(haspkg==true){
							  boxinfolist.list = res.data.cart;
						 }
						 transportStatus.value=res.data.shipment.transportStatus;
						 if(res.data.shipment&&TransportStatus[res.data.shipment.transportStatus]){
							 	transportStatus.value=TransportStatus[res.data.shipment.transportStatus];
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
					data.shipmentid = shipmentid;
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
					shipmenthandlingApi.saveSelfTrans(data).then(res => {
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
					emit("stepdata", status);
				}
				function amazonDoneShipment(){
					if(submitloading.value==true){
						return;
					}
					submitloading.value=true;
					shipmenthandlingApi.amazonDoneShipment({"shipmentid":shipmentid}).then(res => {
						ElMessage.success('操作成功！');
						 submitloading.value=false;
						 emit("change");
					}).catch(e=>{
						  submitloading.value=false;
					})
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
					shipmenthandlingApi.getSelfTransHis({
						"shipmentid": shipmentid
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
	         function handlePackageSkuChange(row){
				 var packagesku="";
				  packageSkuListData.list.forEach(item=>{
					  if(item.id==row.materialid){
						  packagesku=item;
					  }
				  })
				 row.packagesku=packagesku;
				 setEditNumber(row);
			 }
			 function setEditNumber(row){
				 if(row.packagesku&&row.number>row.packagesku.fulfillable){
				      row.editnumber=row.packagesku.fulfillable;
				 }else{
					   row.editnumber=row.number;
				 }
				 if(row.packagesku&&(row.editnumber+row.packagesku.out)>row.number){
					 row.editnumber=row.number-row.packagesku.out;
					 if( row.editnumber<0){
						 row.editnumber=0;
					 }
				 }
			 }
			    
				async function openMaterialCon(showtype) {
					materialDialogVisible.value = true;
					 myshowtype.value=showtype;
					 form.consWarehouseid=shipdata.value.warehouseid;
					 form.boxWarehouseid=shipdata.value.warehouseid;
					//查询material的箱子列表
					await materialApi.packageListAll({'warehouseid':shipdata.value.warehouseid,'search':shipmentid}).then((res)=>{
						 packageSkuListData.list=res.data;
						 boxListData.list.forEach(row=>{
							 packageSkuListData.list.forEach(item=>{
							 					if(row.boxlength==item.length
							 					   &&row.boxwidth==item.width
							 					   &&row.boxheight==item.height){	
													   row.materialid=item.id;
													   row.packagesku=item;
													   setEditNumber(row);
											      }
							 })
							 if(row.materialid==""){
								 packageSkuListData.list.forEach(item=>{
								 					if(row.boxlength<=item.length+2&&row.boxlength>=item.length+1
													   &&row.boxwidth<=item.width+2&&row.boxwidth>=item.width+1
													   &&row.boxheight<=item.height+2&&row.boxheight>=item.height+1
								 					   ){	
								 						   row.materialid=item.id;
														   row.packagesku=item;
														   setEditNumber(row);
								       }
								 })
							 }
						 })
						 
					});
					shipmenthandlingApi.getConsumableList({
						"shipmentid": shipmentid
					}).then(res => {
						if (res.data && res.data.length > 0) {
							materiallist.list = res.data;
							if(materiallist.list&&materiallist.list.length>0){
								materiallist.list.forEach(item=>{
									item.residue=item.needamount-parseInt(item.needamount);
									if(item.out){
									    item.needamount=parseInt(item.needamount)-parseInt(item.out);
									}else{
										item.needamount=parseInt(item.needamount);
									}
									if(item.needamount<0){
										item.needamount=0;
									}
								});
								
							}
						}
					})
				}
				
				function loadZhApiDetail(data,companyid,shipmentid){
					if(data&&data.data && data.data.shipment && data.data.shipment.parcels){
						//渲染trackID
						boxinfolist.list.forEach(item=>{
							data.data.shipment.parcels.forEach(parcel=>{
								if(parcel.ext_number){
									var number=parcel.ext_number ;
									if( number==item.boxid){
										item.tracking_id=parcel.tracking_number;
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
				
		 
	
				function feefunc(value1, value2) {
					return (parseFloat(value1)) * (parseFloat(value2));
				}
	
				function submitMaterialCon() {
					var conswarehouseid = form.consWarehouseid;
					var boxwarehouseid = form.boxWarehouseid;
					var skulist = [];
					var pkglist=[];
					var isok=true;
					var isout=false;
					materiallist.list.forEach(function(item) {
						var row = {}
						row.sku = item.sku;
						if(item.needamount){
							row.amount = parseInt(item.needamount);
						}else{
							row.amount =0;
						}
						if(item.out&&parseInt(item.out)>0){
							isout=true;
						}
						if(item.residue){
							row.residue = parseFloat(item.residue);
						}else{
							row.residue =0;
						}
						if(row.amount<0){
							isok=false;
						}
						if(item.inventoryqty<0){
							isok=false;
						}
						if(row.residue>=1){
							isok=false;
						}
						 
						if(!item.out&&parseInt(item.needamount)>item.inventoryqty){
							isok=false;
						}
						if(!item.out){
						    skulist.push(row);
						}
					});
					if(isok==false){
						ElMessage.error('剩余库存必须小于1或者辅料库存不足');
						return;
					}
					
					boxListData.list.forEach(function(items) {
						 if(items.editnumber&&items.materialid&&items.packagesku&&parseInt(items.editnumber)>0){
							 var row = {};
							 row.amount=items.editnumber;
							 row.warehouseid=boxwarehouseid;
							  row.materialid=items.materialid;
							  row.sku=items.packagesku.sku;
							  if(!items.packagesku.out){
								     pkglist.push(row);
							  }
							
						 }
						
						
					});
					if(withTransSubmit.value){
						saveSelfTrans("submitbatch");
					}
					if(isout){
						 ElMessageBox.confirm(
						    '已经完成辅料出库，确认将只执行货件出库操作?',
						    '注意',
						    {
						      confirmButtonText: '确认',
						      cancelButtonText: '取消',
						      type: 'warning',
						    }
						  )
						.then(() => {
						  amazonDoneShipment();
						})
					}else{
						shipmenthandlingApi.saveInventoryConsumable({
							"shipmentid": shipmentid,
							"warehouseid": conswarehouseid,
							"skulist":  skulist,
							"pkglist":pkglist,
						}).then(res => {
								materialDialogVisible.value = false;
						        amazonDoneShipment();
						})
					}
					
				}
				//下载海关记录
				function downloadCustoms() {
	
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
					optRef.value.loadOptData(datas.shipmentAll);
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
					getShipAmazonInfo({"data":datas});
					getBoxDetial(datas);
				}
	
				function stepclick(step) {
					emit("stepdata", step);
				}
				
			   defineExpose({loadOptData})
</script>

<style>
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
