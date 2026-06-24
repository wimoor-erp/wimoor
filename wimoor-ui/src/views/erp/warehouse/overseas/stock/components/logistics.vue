<template>
	<el-form :model="form"  v-if="form.transedit" :load="submitloading"  label-width="120px" >
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
			 </el-space>
		</el-form-item>
		<el-form-item label="物流渠道" prop="channel">
			<el-select v-model="form.channel" @change="setFormData"  placeholder="请选择">
				<el-option
				     v-for="item in channellist.list"
				     :key="item.id"
				     :label="item.channame"
				     :value="item.id"
				   />
			</el-select>
		</el-form-item>
		<el-form-item label="跟踪编号">
		      <el-input v-model="form.ordernumber" />
		    </el-form-item>
			<el-form-item label="预计到货日期">
			    <el-date-picker
			            v-model="form.arrivalTime"
			            type="date"
			            placeholder="选择日期"
			          />
		    </el-form-item>
						
			<el-form-item label="实际结算">
				<div class="mt-4">
				    <el-input
				      v-model="form.transweight"
					  @input="calculatefee"
					  type="number"
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
			     <el-input v-model="form.singleprice" @input="calculatefee" type="number" placeholder="请输入单价">
			           <template #append>￥</template>
			        </el-input>
			</el-form-item>
			<el-form-item label="关税等其它费用">
			     <el-input v-model="form.otherfee" type="number" @input="calculatefee" placeholder="请输入关税等其它费用">
			           <template #append>￥</template>
			        </el-input>
			</el-form-item>
			<el-form-item label="货件出港日期">
			    <el-date-picker
			            v-model="form.outarrtime"
			            type="date"
			            placeholder="选择日期"
			          />
			</el-form-item>
			<el-form-item label="货件到港日期">
			    <el-date-picker
			            v-model="form.inarrtime"
			            type="date"
			            placeholder="选择日期"
			          />
			</el-form-item>
			<el-form-item label="备注">
				 <el-input v-model="form.remark" type="textarea" />
			</el-form-item>
			
			<el-form-item>
			
			     
				  <el-space :size="32">
					   <el-button type="primary"  @click="saveSelfTrans('save')">保存</el-button>
				  	<div>运输费用: <span >￥{{shipfee}}</span></div>
				  	<div>物流费用总计: <span >￥{{formatFloat(totalfee)}}</span></div>
				  </el-space>
			       <!--  <el-button @click="openHistory">修改记录</el-button> -->
				   <!-- <el-button @click="openCosUpload">上传/下载报关资料</el-button> -->
			    </el-form-item>
	</el-form>
	<el-form :model="form"  v-else :load="submitloading"  label-width="120px" >
		<el-form-item label="运输方式" prop="transtype" >
				 <el-space>
					 {{transInfoData.transtypename}}
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
					 {{transInfoData.companyname}}
			 </el-space>
		</el-form-item>
		<el-form-item label="物流渠道" prop="channel">
			 {{transInfoData.channame}}（{{transInfoData.channeltype}}）
		</el-form-item>
		<el-form-item label="跟踪编号">
		     {{transInfoData.ordernumber}}
		    </el-form-item>
			<el-form-item label="预计到货日期">
			   {{transInfoData.arrivalTime}}
		    </el-form-item>
						
			<el-form-item label="实际结算">
				<div class="mt-4">
					 {{transInfoData.transweight}}
					   <span v-if="transInfoData.wtype=='0'" >重量</span>
					   <span v-if="transInfoData.wtype=='1'" >体积</span>
						<span v-if="transInfoData.wunit=='kg'" >kg</span>
						<span v-if="transInfoData.wunit=='cbm'" >cbm</span>
				  </div>
			</el-form-item>
			<el-form-item label="单价">
				{{transInfoData.singleprice}}￥
			</el-form-item>
			<el-form-item label="关税等其它费用">
				{{transInfoData.otherfee}}￥
			</el-form-item>
			<el-form-item label="货件出港日期">
				{{transInfoData.outarrtime}}
			</el-form-item>
			<el-form-item label="货件到港日期">
				{{transInfoData.inarrtime}}
			</el-form-item>
			<el-form-item label="备注">
				{{transInfoData.remark}}
			</el-form-item>
			
			<el-form-item>
			     
				  <el-space :size="32">
					   <el-button type="primary"   @click="form.transedit=true">编辑</el-button>
				  	<div>运输费用: <span >￥{{shipfee}}</span></div>
				  	<div>物流费用总计: <span >￥{{formatFloat(totalfee)}}</span></div>
				  </el-space>
			       <!--  <el-button @click="openHistory">修改记录</el-button> -->
				   <!-- <el-button @click="openCosUpload">上传/下载报关资料</el-button> -->
			    </el-form-item>
	</el-form>
	<el-dialog v-model="compareVisible" title="物流比价" top="1vh" width="70%" center>
		<CompareList ref="comareRef" :isdialog="true" :shipform="form" :shipdata="shipdata"></CompareList>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="compareVisible = false">关闭</el-button>
				<el-button type="primary" @click="selectTrans()">确认</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import { useRoute,useRouter } from 'vue-router'
	import {formatFloat,dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import { ElTable } from 'element-plus'
	import CompareList from "@/views/erp/ship/transportation/components/compare.vue"
	import { View } from '@element-plus/icons-vue'
	import {BalanceTwo} from '@icon-park/vue-next';
	import {getValue} from '@/utils/index.js';
	import dispatchApi from '@/api/erp/inventory/dispatchOverseaApi.js';
	let router = useRouter()
	let shipdata = ref({})
	let shipfee = ref(0)
	let totalfee = ref(0)
	let historyDialogVisible = ref(false)
	let cosDialogVisible = ref(false)
	let materialDialogVisible = ref(false)
	let submitloading=ref(false);
	let optRef = ref();
	let compareVisible = ref(false);
	let saveTransLoading=ref(false);
	let comareRef = ref(CompareList);
	//上传后的文件列表
	let fileList = ref([])
	let infoVisiable = ref(false);
	// 运行上传文件大小，单位 M
	let fileSize = ref(2)
	let cusfile = ref();
	let transInfoData=ref({});
	let props = defineProps({marketplaceid:"",formid:""});
	const {marketplaceid,formid} = toRefs(props);
	let form = reactive({
		pronumber: '',
		carrier: '',
		arrivalTime: '',
		shipdate: '',
		remark: '',
		transtype: '',
		transedit:true,
		company: '',
		channel: '',
		wtype: '0',
		wunit: 'kg',
		transweight: 0,
		singleprice: 0,
		otherfee: 0,
		outarrtime:'',
		inarrtime: '',
		ordernumber: ''
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
	let historylist = reactive({
		list: []
	})
	let materiallist = reactive({
		list: []
	})
	let customslist = reactive({
		list: []
	})
	let pricemessage=ref("");
	var param={sumBoxWeight:0,sumBoxVolume:0};
	function trackingChange(row){
		boxinfolist.list.forEach(item=>{
			if(!item['tracking_id']){
				item.tracking_id=row.tracking_id;
			}
		})
	}
	async function getTransTypeAll(type) {
		let res = await transportationApi.getTransTypeAll();
		if (res.data) {
			tranlist.list = res.data;
			form.transtype = res.data[0].id;
			changeTransType(form.transtype,type)
		}
	}
	function handleOptChange(){
		emit("change");
	}
	function changeTransType(val, type) {
		if (val.id) {
			getCompanyTranstypeList(val.id, type);
		} else {
			getCompanyTranstypeList(val, type);
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
			"marketplaceid": props.marketplaceid,
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
			if(channel&&channel.pretime>0&&!form.arrivalTime){
				form.arrivalTime=new Date(new Date().getTime() + 3600 * 1000 * 24 * (channel.pretime));
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
			if(!form.transweight){
				if(channel.priceunits=="weight"){
					form.transweight=param.sumBoxWeight>param.sumBoxVolume?param.sumBoxWeight:param.sumBoxVolume;
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
			if(channel.pretime>0){
				form.arrivalTime=new Date(new Date().getTime() + 3600 * 1000 * 24 * (channel.pretime));
			}else{
				form.arrivalTime=new Date(new Date().getTime() + 3600 * 1000 * 24 * 30);
			}
			if(!form.shipdate){
				form.shipdate=new Date();
			}
			 form.singleprice=channel.price;
			if(channel.priceunits=="weight"){
				form.transweight=param.sumBoxWeight>param.sumBoxVolume?param.sumBoxWeight:param.sumBoxVolume;
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
		if(form.company){
			transportationApi.getChannel({
				"marketplaceid": props.marketplaceid,
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
	}


	function calculatefee() {
			shipfee.value = formatFloat(form.singleprice * form.transweight);
			totalfee.value = formatFloat((form.singleprice * form.transweight) + parseFloat(form.otherfee));
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
	function saveSelfTrans(ftype) {
		form.operate = ftype;
		form.outarrtime = new Date(form.outarrtime);
		form.inarrtime = new Date(form.inarrtime);
		form.arrivalTime = new Date(form.arrivalTime);
		form.formid=props.formid;
		form.proNumber = form.pronumber;
		
		submitloading.value=true;
		dispatchApi.saveInfo(form).then(res => {
			 ElMessage.success('操作成功！');
			 submitloading.value=false;
		}).catch(e=>{
			  submitloading.value=false;
		})
	}
	
	function openHistory() {
		historyDialogVisible.value = true;
		dispatchApi.getSelfTransHis({
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
		dispatchApi.getShipmentCustomsRecord().then(res => {
			if (res.data && res.data.length > 0) {
				customslist.list = res.data;
			}
		})
	}

	function feefunc(value1, value2) {
		return (parseFloat(value1)) * (parseFloat(value2));
	}

	//下载海关记录
	function downloadCustoms() {

	}
	 
function getInfo() {
					dispatchApi.getInfo({"id": props.formid}).then(res => {
						var transinfo = res.data;
						transInfoData.value=transinfo;
						if (transinfo) {
							form.transedit=false;
							form.singleprice = transinfo.singleprice;
							form.transweight =  transinfo.transweight;
							if (transinfo.wtype == "0") {
								form.wtype = '0';
							} else {
								form.wtype = '1';
							}
	                        if(transinfo.wunit){
								form.wunit =  transinfo.wunit;
							}
							if (transinfo.arrivalTime) {
								form.arrivalTime = transinfo.arrivalTime;
							}
							if (transinfo.outarrtime) {
								form.outarrtime = transinfo.outarrtime;
							}
							if (transinfo.inarrtime) {
								form.inarrtime = transinfo.inarrtime;
							}
							if (transinfo.ordernum) {
								form.ordernumber = transinfo.ordernum;
							}
							if (transinfo.otherfee) {
								form.otherfee = transinfo.otherfee;
							}
							if (transinfo.remark) {
								form.remark = transinfo.remark;
							}
						}
						if (transinfo) {
							form.transtype = transinfo.transtype;
							form.company = transinfo.company;
							form.channel = transinfo.channel;
							form.singleprice=transinfo.singleprice;
							changeTransType(form.transtype, 'init');
						} else {
							changeTransType(form.transtype);
						}
						console.log(res.data);
					})
				}
 
	onMounted(()=>{
		setTimeout(function(){
		   shipdata.value.marketplaceid=props.marketplaceid;
		   getTransTypeAll("init");
		   if(props.formid){
			   getInfo();
		   }
		},1000);
	})
</script>

<style>
</style>