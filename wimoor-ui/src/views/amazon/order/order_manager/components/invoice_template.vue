<template>
	<!-- 编辑发票信息 -->
	<el-dialog v-model="editvatVisible" title="编辑发票详情" top="2vh"  width="60%" @close='closeDialog'>
		<el-descriptions>
			<el-descriptions-item label="公司LOGO">
				<el-image :src="invoiceData1.logourl"   width="180" height="60"></el-image>
			</el-descriptions-item>
		</el-descriptions>
		<el-descriptions >
			<el-descriptions-item label="LOGO上传" >
			<el-upload
					class="upload-demo" 
					action   
					:http-request="uploadFile" 
					ref="upload"
					:limit="1" 
					:on-remove="handleRemove"  
					:file-list="fileList" 
					:on-exceed="handleExceed" 
					:before-upload="beforeUpload" 
					:show-file-list="true" 
					:headers="headers" 
					accept=".jpeg,.jpg,.png"
					>
					<!-- action="/api/file/fileUpload" -->
					<el-button class="btn"><i class="el-icon-paperclip"></i>上传图片</el-button>
					<template #tip>
					  <div class="el-upload__tip">
					    图片格式JPG/JPEG,分辨率180*60,小于50KB.
					  </div>
					</template>
				</el-upload>
			 
	 
			</el-descriptions-item>
		    <el-descriptions-item label="公司名称">
				<el-input  style="width: 80%;"    v-model="invoiceData1.company"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="发票署名">
				<el-input   style="width: 80%;" v-model="invoiceData1.sign"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="国家">
				<el-input   style="width: 80%;"  v-model="invoiceData1.country"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="州/省">
				<el-input   style="width: 80%;"  v-model="invoiceData1.province"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="城市">
				<el-input  style="width: 80%;" v-model="invoiceData1.city"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="地址">
				<el-input   style="width: 80%;" v-model="invoiceData1.address"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="电话">
				<el-input  style="width: 80%;"  v-model="invoiceData1.phone"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="邮编">
				<el-input   style="width: 80%;" v-model="invoiceData1.postalcode"></el-input>
			</el-descriptions-item>
			<el-descriptions-item label="邮箱"  >
				<el-input   style="width: 25%;"  v-model="invoiceData1.email"></el-input>
			</el-descriptions-item>
		</el-descriptions>
		<el-divider/>
		<div>Vat税号税率设置(欧洲5国需要缴Vat税,未缴可不填)</div>
		<el-table :data="invoiceData2.list" border class="od-table">
			<el-table-column prop="country" label="国家" width="60"> 
				<template #default="scope">
					<div>{{countryFunc(scope.row.country)}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="vatNum" label="Vat税号"  >
				<template #default="scope">
					<el-input v-model="scope.row.vatNum" ></el-input>
				</template>
			</el-table-column>
			<el-table-column prop="vatRate" label="税率(%)"  >
				<template #default="scope">
					<el-input v-model="scope.row.vatRate" ></el-input>
				</template>
			</el-table-column>
		</el-table>
	 
		  <template #footer>
		  	<span class="dialog-footer">
				<el-button @click="editvatVisible = false">关闭</el-button>
		  		<el-button @click="submitVatInfo" type="primary">提交</el-button>
		  	</span>
		  </template>
		
	</el-dialog>	
</template>

<script setup>
	import {ref ,watch,reactive,onMounted,onUpdated} from 'vue';
	import orderListApi from '@/api/amazon/order/orderListApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import {formatFloat,dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {ElMessage,ElLoading} from 'element-plus';
	import {View,Refresh} from '@element-plus/icons-vue'
	import myUtil from "@/hooks/amazon/order/financial.js";
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import FeedStatus from "@/components/feedstatus/index.vue";
		const {statusFunc,countryFunc,currencyFunc,totalRowFunc}=myUtil();
	//上传后的文件列表
	let invoiceModel={logourl:"",
	                      company:"",
	                      country:"",
						  province:"",
						  city:"",
	                      address:"",
		                  phone:"",postalcode:"",email:"",
	                     }
	let orderData=ref(invoiceModel);
	let fileList=ref([])
	let vatNo=ref("");
	 let vatRates=ref(0);
	// 运行上传文件大小，单位 M
	let fileSize=ref(2)
	let invoiceData1=ref({});
	let invoiceData2=reactive({list:[]})
	let editvatVisible=ref(false);
	var vattype=null;
	//请求头
	let headers=ref({"Content-Type": "multipart/form-data" }) 
	let logofile=ref()
	function show(row,type){
		vattype=type;
		editvatVisible.value=true;
		orderData.value=JSON.parse(JSON.stringify(row));
		loadOrderInfo(row);
	}
	defineExpose({show})
	function submitVatInfo(){
		var vatData=handlerVatRate();
		let FormDatas = new FormData()
		if(logofile.value){
		    FormDatas.append('file',logofile.value);
		}else{
			FormDatas.append('file',{});
		}
		FormDatas.append('groupid',orderData.value.groupid);
		FormDatas.append("Vatphone",invoiceData1.value.phone);
		FormDatas.append("Vatcompany",invoiceData1.value.company);
		FormDatas.append("Vatpostal",invoiceData1.value.postalcode);
		FormDatas.append("Vatcountry",invoiceData1.value.country);
		FormDatas.append("Vatemail",invoiceData1.value.email);
		FormDatas.append("Vatprovince",invoiceData1.value.province);
		FormDatas.append("Vatsign",invoiceData1.value.sign);
		FormDatas.append("Vatcity",invoiceData1.value.city);
		FormDatas.append("image",invoiceData1.value.logourl);
		FormDatas.append("Vataddress",invoiceData1.value.address);
		FormDatas.append("ukvat",vatData.ukvat);
		FormDatas.append("uknum",vatData.uknum);
		FormDatas.append("devat",vatData.devat);
		FormDatas.append("denum",vatData.denum);
		FormDatas.append("frvat",vatData.frvat);
		FormDatas.append("frnum",vatData.frnum);
		FormDatas.append("esvat",vatData.esvat);
		FormDatas.append("esnum",vatData.esnum);
		FormDatas.append("itvat",vatData.itvat);
		FormDatas.append("itnum",vatData.itnum);
		orderListApi.saveOrderVat(FormDatas).then(function(res){
			if(res.data){
				if(res.data.isSuccess=="true"){
					ElMessage.success('操作成功！');
					editvatVisible.value=false;
				}else{
					ElMessage.error( '操作失败！');
					editvatVisible.value=true;
				}
			}
		})
	}
	function handlerVatRate(){
		var vatData={};
		invoiceData2.list.forEach(function(item,index){
			if(item.country=="uk"){
				vatData.ukvat=item.vatNum;
				vatData.uknum=item.vatRate;
			}
			if(item.country=="de"){
				vatData.devat=item.vatNum;
				vatData.denum=item.vatRate;
			}
			if(item.country=="fr"){
				vatData.frvat=item.vatNum;
				vatData.frnum=item.vatRate;
			}
			if(item.country=="it"){
				vatData.itvat=item.vatNum;
				vatData.itnum=item.vatRate;
			}
			if(item.country=="es"){
				vatData.esvat=item.vatNum;
				vatData.esnum=item.vatRate;
			}
		});
		return vatData;
	}
	//文件上传之前
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 50; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error('上传文件大小不能超过 50KB!!');
				return false;
			}
			else {
			   return true;
			}
		}
	}
	 
	function handlerMarket(market){
		if (market == "Amazon.fr"){
			market = "FR";
		}
		if (market == "Amazon.co.uk"){
			market = "UK";
		}
		if (market == "Amazon.de"){
			market = "DE";
		}
		if (market == "Amazon.es"){
			market = "ES";
		}
		if (market == "Amazon.it"){
			market = "IT";
		}
		if (market == "Amazon.nl"){
			market = "NL";
		}
		if (market == "Amazon.se"){
			market = "SE";
		}
		if (market == "Amazon.com.be"){
			market = "BE";
		}
		if (market == "Amazon.pl"){
			market = "PL";
		}
		return market;
	}
			
      function loadOrderInfo(row){
				orderListApi.selectVatInfoByGroup({
					"groupid":orderData.value.groupid
				}).then(function(res){
					if(res.data){
						if(res.data.data1){
						   invoiceData1.value=res.data.data1;
						}else{
							invoiceData1.value=invoiceModel;
						}
						invoiceData2.list=res.data.data2;
						vatNo.value=res.data.vatNo;
						if(vattype!="Vat"){
							vatRates.value=0;
						}else{
							var market=handlerMarket(orderData.value.market);
							invoiceData2.list.forEach(function(item,index){
								if(item.country==market){
									vatRates.value=item.vatRate;
								}
							});
						}
					}
				})
			}
	//上传了的文件给移除的事件，由于我没有用到默认的展示，所以没有用到
	function handleRemove(){
	}
	//超出文件个数的回调
	function handleExceed(){
		 ElMessage.error('超出最大上传文件数量的限制！');
		 return;
	}
	//上传文件的事件
	function uploadFile(item){
		//上传文件的需要formdata类型;所以要转
		logofile.value=item.file;
	}
	//上传成功后的回调
	function handleSuccess(){
		
	}
</script>

<style>
</style>