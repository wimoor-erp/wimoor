<template>
    <div class="main-sty">
		 <div class="con-body">
		 <el-row :gutter="10" class="mb8">
			<el-form :model="queryParams" :inline="true" label-width="80px">
                	<el-form-item label="支付时间">
						<el-date-picker
							v-model="queryParams.dateRange"
							type="daterange"
							range-separator="至"
							start-placeholder="开始日期"
							end-placeholder="结束日期"
						/>
					</el-form-item>
				<el-form-item label="搜索">
					<el-input
						v-model="queryParams.search"
						placeholder="请输入订单号、订单名称、公司名称或套餐名称"
						clearable
						style="width: 250px"
					/>
				</el-form-item>
				<el-form-item label="公司名称">
					<el-select 
						v-model="queryParams.shopid"
						placeholder="请选择公司" 
						clearable 
						filterable
						remote
						:remote-method="searchShop"
						:loading="shopSearchLoading"
						:keyword="shopSearchKeyword"
					>
						<el-option v-for="shop in shopList" :key="shop.id" :value="shop.id" :label="shop.name"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="套餐名称">
					<el-select style="width: 150px" v-model="queryParams.tariffpackage" @clear="queryParams.tariffpackage=null" placeholder="请选择套餐" clearable filterable>
						<el-option v-for="pkg in packageList" :key="pkg.id" :value="pkg.id" :label="pkg.name"></el-option>
					</el-select>
				</el-form-item>
				<template v-if="showAdvancedSearch">
					<el-form-item label="订单类型">
						<el-select v-model="queryParams.ftype" placeholder="请选择" clearable>
							<el-option value="package" label="套餐"></el-option>
							<el-option value="append" label="附加包"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="付款状态">
						<el-select v-model="queryParams.paystatus" placeholder="请选择" clearable>
							<el-option value="success" label="已支付"></el-option>
							<el-option value="pending" label="待支付"></el-option>
							<el-option value="failed" label="支付失败"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="开票状态">
						<el-select v-model="queryParams.ivcstatus" placeholder="请选择" clearable>
							<el-option value="invoiced" label="已开票"></el-option>
							<el-option value="pending" label="待开票"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="开票请求">
						<el-select v-model="queryParams.hasInvoiceRequest" placeholder="请选择" clearable>
							<el-option :value="true" label="已申请"></el-option>
							<el-option :value="false" label="未申请"></el-option>
						</el-select>
					</el-form-item>
				
				</template>
				<el-form-item>
					<el-button type="primary" @click="handleQuery">搜索</el-button>
					<el-button @click="resetQuery">重置</el-button>
					<el-button @click="toggleAdvancedSearch">
						{{ showAdvancedSearch ? '收起筛选' : '更多筛选' }}
					</el-button>
				</el-form-item>
			</el-form>
		</el-row>
		
		<el-row style="margin-bottom: 16px;">
			<el-button type="primary" @click="handleAdd">添加新订单</el-button>
		</el-row>
	       <el-row>
			 <GlobalTable ref="globalTable" :tableData="tableData" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
			   <template #field>
			 	 <el-table-column prop="outTradeNo" label="商户订单号"  sortable='custom'   >
					 <template #default="scope">
					 						   <div>{{scope.row.outTradeNo}}</div>
					 </template>
			 	  </el-table-column>
				  <el-table-column prop="shopName" label="公司名称"  sortable='custom'  >
				  </el-table-column>
				  <el-table-column prop="subject" label="订单名称"  sortable='custom'  >
				  </el-table-column>
				  <el-table-column prop="packageName" label="套餐名称"  sortable='custom'  >
				  </el-table-column>
				  <el-table-column prop="ftype" label="订单类型"  sortable='custom'  width="100">
				     <template #default="scope">
						<span v-if="scope.row.ftype === 'package'">套餐</span>
						<span v-else-if="scope.row.ftype === 'append'">附加包</span>
						<span v-else>{{scope.row.ftype}}</span>
					 </template>
				  </el-table-column>
				  <el-table-column prop="totalAmount" label="付款金额"  sortable='custom'  width="120">
				  </el-table-column>
				  <el-table-column prop="paytype" label="支付方式"  sortable='custom'  width="100">
				  </el-table-column>
				  <el-table-column prop="paystatus" label="付款状态"  sortable='custom'  width="100">
				     <template #default="scope">
						<span v-if="scope.row.paystatus === 'success'" style="color: green;">已支付</span>
						<span v-else-if="scope.row.paystatus === 'pending'" style="color: orange;">待支付</span>
						<span v-else-if="scope.row.paystatus === 'failed'" style="color: red;">支付失败</span>
						<span v-else>{{scope.row.paystatus}}</span>
					 </template>
				  </el-table-column>
				  <el-table-column prop="ivcstatus" label="开票状态"  sortable='custom'  width="150">
				     <template #default="scope">
						<template v-if="scope.row.ivcstatus === 'invoiced'">
							<span style="color: green;">已开票</span>
							<el-button 
								v-if="scope.row.location && scope.row.location.length > 0"
								@click="downloadInvoiceFromList(scope.row)"
								link 
								type="primary"
								size="small"
								icon="download"
							>
								下载
							</el-button>
						</template>
						<span v-else-if="scope.row.ivcstatus === 'pending'" style="color: orange;">待开票</span>
						<span v-else>{{scope.row.ivcstatus}}</span>
					 </template>
				  </el-table-column>
				  <el-table-column prop="hasInvoiceRequest" label="开票请求"  sortable='custom'  width="100">
				     <template #default="scope">
						<span v-if="scope.row.hasInvoiceRequest" style="color: green;">已申请</span>
						<span v-else style="color: gray;">未申请</span>
					 </template>
				  </el-table-column>
				  <el-table-column prop="paytime" label="支付时间"  sortable='custom'  width="180">
				     <template #default="scope">
						{{scope.row.paytime ? dateFormat(scope.row.paytime) : '-'}}
					 </template>
				  </el-table-column>
			 	 <el-table-column label="操作"    width="200"  >
			 		 <template #default="scope">
						  <el-button   @click="handleEdit(scope.row)"   link type="primary">编辑</el-button>
			 			  <el-button   @click="handleDisable(scope.row)"   link type="danger">禁用</el-button>
			 			  <el-button   @click="handleUploadInvoice(scope.row)"   link type="success">上传发票</el-button>
			 		 </template>
			 	 </el-table-column>
			 	 </template>
			  </GlobalTable>
  </el-row>
  </div>
  
   <!-- 订单编辑对话框 -->
   <el-dialog v-model="dialogFormVisible" title="订单信息" width="60%">
      <el-form :model="editForm"  label-width="auto">
        <el-form-item label="订单类型" >
		  <el-select v-model="editForm.ftype">
			  <el-option value="package" label="套餐"></el-option>
			  <el-option value="append" label="附加包"></el-option>
		  </el-select>
        </el-form-item>
		<el-form-item label="公司名称" >
		  <el-select 
			v-model="editForm.shopid" 
			placeholder="请选择公司" 
			filterable
			remote
			:remote-method="searchShop"
			:loading="shopSearchLoading"
			:keyword="shopSearchKeyword"
		  >
			  <el-option v-for="shop in shopList" :key="shop.id" :value="shop.id" :label="shop.name"></el-option>
		  </el-select>
        </el-form-item>
		<el-form-item label="商户订单号" >
          <el-input v-model="editForm.outTradeNo"  />
        </el-form-item>
		<el-form-item label="订单名称" >
          <el-input v-model="editForm.subject"  />
        </el-form-item>
		<el-form-item label="套餐名称" >
		  <el-select v-model="editForm.tariffpackage" placeholder="请选择套餐" filterable>
			  <el-option v-for="pkg in packageList" :key="pkg.id" :value="pkg.id" :label="pkg.name"></el-option>
		  </el-select>
        </el-form-item>
		<el-form-item label="交易号" >
          <el-input v-model="editForm.tradeNo"  />
        </el-form-item>
		<el-form-item label="付款金额" >
          <el-input v-model="editForm.totalAmount" type="number" />
        </el-form-item>
		<el-form-item label="支付方式" >
          <el-input v-model="editForm.paytype"  />
        </el-form-item>
		<el-form-item label="付款状态" >
		  <el-select v-model="editForm.paystatus">
			  <el-option value="success" label="已支付"></el-option>
			  <el-option value="pending" label="待支付"></el-option>
			  <el-option value="failed" label="支付失败"></el-option>
		  </el-select>
        </el-form-item>
		<el-form-item label="开票状态" >
		  <el-select v-model="editForm.ivcstatus">
			  <el-option value="invoiced" label="已开票"></el-option>
			  <el-option value="pending" label="待开票"></el-option>
		  </el-select>
        </el-form-item>
		<el-form-item label="支付时间" >
          <el-date-picker v-model="editForm.paytime" type="datetime" />
        </el-form-item>
		<el-form-item label="购买月份" >
          <el-input v-model="editForm.months" type="number" />
        </el-form-item>
		<el-form-item label="购买数量" >
          <el-input v-model="editForm.pcs" type="number" />
        </el-form-item>
		<el-form-item label="折扣码" >
          <el-input v-model="editForm.discountnumber"  />
        </el-form-item>
		<el-form-item label="是否禁用" >
		  <el-switch v-model="editForm.disable" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
	
	<!-- 发票上传对话框 -->
	<el-dialog v-model="uploadInvoiceVisible" title="上传发票" width="600px">
		<el-form :model="invoiceForm" label-width="120px">
			<el-divider content-position="left">发票抬头信息</el-divider>
			<el-form-item label="公司名称">
				<el-input v-model="invoiceForm.companyname" placeholder="请输入公司名称" />
			</el-form-item>
			<el-form-item label="纳税人识别号">
				<el-input v-model="invoiceForm.taxid" placeholder="请输入纳税人识别号" />
			</el-form-item>
			<el-form-item label="注册地址">
				<el-input v-model="invoiceForm.address" placeholder="请输入注册地址" />
			</el-form-item>
			<el-form-item label="联系电话">
				<el-input v-model="invoiceForm.phone" placeholder="请输入联系电话" />
			</el-form-item>
			<el-form-item label="开户行">
				<el-input v-model="invoiceForm.bankname" placeholder="请输入开户行" />
			</el-form-item>
			<el-form-item label="银行账号">
				<el-input v-model="invoiceForm.bankaccount" placeholder="请输入银行账号" />
			</el-form-item>
			<el-form-item label="备注">
				<el-input v-model="invoiceForm.remark" type="textarea" placeholder="请输入备注" />
			</el-form-item>
			<el-divider content-position="left">上传发票</el-divider>
			<el-form-item label="发票文件">
				<el-upload
					class="upload-demo"
					action
					:http-request="uploadInvoiceFile"
					:file-list="invoiceFileList"
					:on-success="handleInvoiceUploadSuccess"
					:on-error="handleInvoiceUploadError"
					:before-upload="beforeInvoiceUpload"
					accept=".pdf,.jpg,.jpeg,.png"
					multiple
					:disabled="uploadingInvoice"
				>
					<el-button type="primary" :loading="uploadingInvoice">点击上传</el-button>
					<div class="el-upload__tip">只能上传 pdf/jpg/jpeg/png 文件</div>
				</el-upload>
			</el-form-item>
			<el-form-item label="已上传文件">
				<el-list v-if="invoiceForm.location" style="max-height: 200px; overflow-y: auto;">
					<el-list-item v-for="(url, index) in invoiceForm.location.split(';')" :key="index">
						<a :href="url" target="_blank">{{ url.substring(url.lastIndexOf('/') + 1) }}</a>
					</el-list-item>
				</el-list>
				<span v-else style="color: #999;">暂无上传文件</span>
			</el-form-item>
		</el-form>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="uploadInvoiceVisible = false" :disabled="saveInvoiceLoading || uploadingInvoice">取消</el-button>
				<el-button type="primary" @click="saveInvoice" :loading="saveInvoiceLoading || uploadingInvoice">保存</el-button>
			</div>
		</template>
	</el-dialog>
	</div>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
    import { ElMessage ,ElMessageBox} from 'element-plus';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import MarketCountry from '@/components/header/market_country.vue';
	import customerOrderApi from '@/api/sys/admin/customerOrderApi.js';
	import shopApi from '@/api/sys/admin/shopApi.js';
	import packageApi from '@/api/sys/admin/packageApi.js';
	import invoiceApi from '@/api/sys/admin/invoiceApi.js';
	const globalTable=ref();
	const formLabelWidth = '140px'
	const marketCountryRef =ref();
	const showAdvancedSearch = ref(false);
	const uploadInvoiceVisible = ref(false);
	const invoiceFileList = ref([]);
	const saveInvoiceLoading = ref(false);
	const uploadingInvoice = ref(false);
	const shopSearchLoading = ref(false);
	const shopSearchKeyword = ref('');
	
	let state=reactive({
		 downLoading:false,
		 dialogFormVisible:false,
		 queryParams:{
			 pageNum: 1,
			 pageSize: 10,
			 search:"",
			 ftype:"",
			 paystatus:"",
			 ivcstatus:"",
			 dateRange: [],
			 shopid: null,
			 tariffpackage: null,
			 hasInvoiceRequest: null,
		 },
		 editForm:{},
		 invoiceForm:{
			 orderid: null,
			 location: '',
		 },
		 isload:true,
		 tableData:{records:[],total:0},
		 shopList: [],
		 packageList: [],
	});
	let {
	   queryParams,
	   isload,
	   editForm,
	   dialogFormVisible,
	   invoiceForm,
	   tableData,
	   downLoading,
	   shopList,
	   packageList,
	} =toRefs(state);

	function toggleAdvancedSearch() {
		showAdvancedSearch.value = !showAdvancedSearch.value;
	}
  
	function handleQuery(){
	     globalTable.value.loadTable(state.queryParams);
	}
	
	function loadTableData(param){
		const params = { ...param };
		if (params.dateRange && params.dateRange.length === 2) {
			const formatDate = (date) => {
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0');
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`;
			};
			params.startTime = formatDate(params.dateRange[0]) + ' 00:00:00';
			params.endTime = formatDate(params.dateRange[1]) + ' 23:59:59';
			delete params.dateRange;
		}
		customerOrderApi.listAll(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total = res.data.total;
			// 为每个订单获取发票信息
			state.tableData.records.forEach(order => {
				invoiceApi.detail({ orderid: order.id }).then(res => {
					if (res.data && res.data.location) {
						// 解析发票文件列表
						const locations = res.data.location.split(';').filter(l => l.trim());
						order.invoiceFiles = locations.map(location => ({
							url: location,
							name: location.substring(location.lastIndexOf('/') + 1)
						}));
					} else {
						order.invoiceFiles = [];
					}
				}).catch(() => {
					order.invoiceFiles = [];
				});
			});
		}); 
	}
	
	function resetQuery(){
		state.queryParams = {
			pageNum: 1,
			pageSize: 10,
			search: "",
			ftype: "",
			paystatus: "",
			ivcstatus: "",
			dateRange: [],
			shopid: null,
			tariffpackage: null,
			hasInvoiceRequest: null,
		};
		showAdvancedSearch.value = false;
		handleQuery();
	}
	
	function handleEdit(row){
		customerOrderApi.detail({"id":row.id}).then(res=>{
			state.editForm=res.data;
			state.dialogFormVisible=true;
		})
		
	}
	function handleAdd(){
		state.editForm={};
		state.dialogFormVisible=true;
	}
	function handleSubmit(){
		if(state.editForm&&state.editForm.id){
			customerOrderApi.updateItem(state.editForm).then(res=>{
				ElMessage.success("操作成功");
				handleQuery();
				state.dialogFormVisible = false;
			})
		}else{
			customerOrderApi.save(state.editForm).then(res=>{
				ElMessage.success("操作成功");
				handleQuery();
				state.dialogFormVisible = false;
			})
		}
		
	}
    function handleDisable(row){
		ElMessageBox.confirm(`确认禁用该订单?`, '警告', {
		  confirmButtonText: '确定',
		  cancelButtonText: '取消',
		  type: 'warning',
		})
		  .then(() => {
		    customerOrderApi.updateItem({ id: row.id, disable: true })
		      .then(() => {
		        handleQuery();
		        ElMessage.success('禁用成功');
		      })
		      .catch(() => {
		        ElMessage.error('禁用失败');
		      });
		  })
		  .catch(() => ElMessage.info('已取消禁用'));
	}
	
	function handleUploadInvoice(row){
		state.invoiceForm.orderid = row.id;
		invoiceApi.detail({orderid: row.id}).then(res => {
			if (res.data) {
				state.invoiceForm.location = res.data.location || '';
				state.invoiceForm.companyname = res.data.companyname || res.data.company || '';
				state.invoiceForm.taxid = res.data.taxid || res.data.invoice || '';
				state.invoiceForm.address = res.data.address || '';
				state.invoiceForm.phone = res.data.phone || '';
				state.invoiceForm.bankname = res.data.bankname || res.data.bank || '';
				state.invoiceForm.bankaccount = res.data.bankaccount || res.data.card_no || res.data.cardNo || '';
				state.invoiceForm.remark = res.data.remark || '';
			} else {
				state.invoiceForm.location = '';
				state.invoiceForm.companyname = '';
				state.invoiceForm.taxid = '';
				state.invoiceForm.address = '';
				state.invoiceForm.phone = '';
				state.invoiceForm.bankname = '';
				state.invoiceForm.bankaccount = '';
				state.invoiceForm.remark = '';
			}
		}).finally(() => {
			invoiceFileList.value = [];
			uploadInvoiceVisible.value = true;
		});
	}
	
	function beforeInvoiceUpload(file) {
		const fileType = file.type;
		const allowedTypes = ['application/pdf', 'image/jpeg', 'image/jpg', 'image/png'];
		if (!allowedTypes.includes(fileType)) {
			ElMessage.error('只能上传 pdf/jpg/jpeg/png 文件');
			return false;
		}
		return true;
	}
	
	function uploadInvoiceFile(item) {
		uploadingInvoice.value = true;
		const formData = new FormData();
		formData.append('file', item.file);
		formData.append('orderid', state.invoiceForm.orderid);
		
		invoiceApi.upload(formData).then(res => {
			if (res.data && (res.data.invoiceFile || res.data.location)) {
				handleInvoiceUploadSuccess({ code: 200, data: res.data }, item.file, []);
			} else {
				handleInvoiceUploadError(new Error('上传失败'), item.file, []);
			}
		}).catch(err => {
			handleInvoiceUploadError(err, item.file, []);
		}).finally(() => {
			uploadingInvoice.value = false;
		});
	}
	
	function handleInvoiceUploadSuccess(response, file, fileList) {
		if (response.code === 200) {
			ElMessage.success('上传成功');
			state.invoiceForm.invoiceFile = response.data.invoiceFile;
      state.invoiceForm.location = response.data.location;
			invoiceFileList.value = fileList;
		} else {
			ElMessage.error('上传失败');
		}
	}
	
	function handleInvoiceUploadError(err, file, fileList) {
		ElMessage.error('上传失败');
	}
	
	function downloadInvoiceFromList(order) {
		if (order.location && order.location.length > 0) {
			// 下载第一个发票文件
			window.open(order.location, '_blank');
		} else {
			ElMessage.error('暂无发票文件');
		}
	}
	
	function previewInvoice(url) {
		window.open(url, '_blank');
	}
	
	function saveInvoice() {
		saveInvoiceLoading.value = true;
		invoiceApi.save({
			orderid: state.invoiceForm.orderid,
			company: state.invoiceForm.companyname,
			invoice: state.invoiceForm.taxid,
			address: state.invoiceForm.address,
			phone: state.invoiceForm.phone,
			bank: state.invoiceForm.bankname,
			cardNo: state.invoiceForm.bankaccount,
			remark: state.invoiceForm.remark,
			location: state.invoiceForm.location,
		}).then(res => {
			ElMessage.success('保存成功');
			uploadInvoiceVisible.value = false;
			handleQuery();
		}).catch(() => {
			ElMessage.error('保存失败');
		}).finally(() => {
			saveInvoiceLoading.value = false;
		});
	}
	
	function searchShop(keyword) {
		if (!keyword || keyword.length < 1) {
			state.shopList = [];
			return;
		}
		shopSearchLoading.value = true;
		shopApi.list({ search: keyword, pageSize: 20 }).then(res => {
			state.shopList = res.data;
			shopSearchLoading.value = false;
		}).catch(() => {
			shopSearchLoading.value = false;
		});
	}
	
	function loadPackageList() {
		packageApi.list({}).then(res => {
			state.packageList = res.data;
		});
	}
	 
	 onMounted(()=>{
		 loadPackageList();
		 handleQuery();
	 })
	 
	 
</script>

<style>
</style>