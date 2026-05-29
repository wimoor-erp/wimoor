<template>
	<el-tabs v-model="activeName"  @tab-change="tabChange">
	  <el-tab-pane v-for="(item,index) in orderState"  :name="item.name" :key="item.name">
		  <template #label>
			  <span >{{item.label}}</span>
			  <span  v-if="item.num">( <span class="text-orange">{{item.num}}</span> )</span>
		   </template>
			</el-tab-pane>
			</el-tabs>
			<el-row>
				<el-space >
					    <el-radio-group v-if="queryParams.auditstatus=='2'" v-model="queryParams.operate" @change="handleQuery">
					      <el-radio-button label=""  value=""   >全部</el-radio-button>
					      <el-radio-button label="true" value="true" >可处理</el-radio-button>
					      <el-radio-button label="false" value="false" >无库存</el-radio-button>
					    </el-radio-group>
					<el-select placeholder="单据类型" v-model="queryParams.ftype" @change="handleQuery" clearable>
						<el-option label="组装单"  value="ass"></el-option>
						<el-option label="拆分单"  value="dis"></el-option>
					</el-select>
					<Warehouse @changeware="getWarehouse"  :defaultValue="isConsumable=='ok'?'only':''"/>
					<Datepicker v-if="queryParams.auditstatus!='1'&&queryParams.auditstatus!='2'" ref="datepickers" @changedate="changeDate" />
					<el-input v-model="queryParams.search" @input="handleInputQuery"
					 clearable 
					 @clear="handleInputQuery"
					 placeholder="请输入" class="input-with-select">
						<template #prepend>
							<el-select v-model="queryParams.searchtype" @change='handleQuery' placeholder="SKU"
								style="width: 110px">
								<el-option label="单据编码" value="number"></el-option>
								<el-option label="SKU" value="sku"></el-option>
								<el-option label="货件编码" value="shipment"></el-option>
							</el-select>
						</template>
						<template #append>
							<el-button @click="handleQuery">
								<el-icon style="font-size: 16px;align-itmes:center">
									<search />
								</el-icon>
				  	</el-button>
						</template>
					</el-input>
					</el-space>
			</el-row>
			<el-row v-if="isConsumable=='ok'">
					<PrePlan type="ass" :warehouseid="state.queryParams.warehouseid" @change="loadRunid" :saveData="addTableData" @query="loadQueryid"></PrePlan>
			</el-row>
			<el-row v-else>
				<el-space  >
					<el-button v-hasPerm="'erp:po:ass:add'"  type="primary" class="im-but-one" @click="handleAdd">
					  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
					  <span>添加加工单</span>
					</el-button>
					<el-button @click="upload">导入</el-button>
					    <el-radio-group v-if="queryParams.auditstatus=='2'" v-model="queryParams.operate" @change="handleQuery">
					      <el-radio-button label=""  value=""   >全部</el-radio-button>
					      <el-radio-button label="true" value="true" >可处理</el-radio-button>
					      <el-radio-button label="false" value="false" >无库存</el-radio-button>
					    </el-radio-group>
					<el-button v-if="queryParams.auditstatus=='2'" @click="handleDelete">作废</el-button>
					<!-- <el-button>重置</el-button> -->
					    <el-dropdown>
					      <el-button type="primary">
					        导出<el-icon class="el-icon--right"><arrow-down /></el-icon>
					      </el-button>
					      <template #dropdown>
					        <el-dropdown-menu>
					          <el-dropdown-item @click="downloadDetailExcelHandle">详情导出</el-dropdown-item>
					          <el-dropdown-item @click="downloadFormExcelHandle">表单导出</el-dropdown-item>
					        </el-dropdown-menu>
					      </template>
					    </el-dropdown>
				</el-space>
				<div class='rt-btn-group'>
					<el-button    @click="gotoShelf"            type="primary"  plain>下架</el-button>
					<el-button    @click="downloadAssPDFHandle" type="warning"  plain>导出配货单</el-button>
					<el-button    @click="goDispatch"           type="success"  plain>调库</el-button> 
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
				</div>
			</el-row>

			<el-dialog
			   v-model="uploadVisible"
			   title="导入加工单"
			   width="400px"
			 >
			 <el-upload
			     :drag="true"
			     action
				 :http-request="uploadFiles"
				 :limit="1"
				 :before-upload="beforeUpload" 
				 :show-file-list="true" 
				 :headers="headers" 
				 accept=".xls,.xlsx,.zip"
			     multiple
			   >
			     <el-icon class="font-48"><upload-filled /></el-icon>
			     <div class="el-upload__text">
			      拖拽文件到此处或 <em>点击上传</em>
			     </div>
			   </el-upload>
			 <template #footer>
			   <span class="dialog-footer">
				   <div class="flex-center-between">
				 <el-button type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
				 <div>
			     <el-button @click="uploadVisible = false">取消</el-button>
			     <el-button type="primary" @click.stop="handleUploadExcel">
			       上传文件
			     </el-button></div></div>
			   </span>
			 </template>
			  </el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import { useRouter } from 'vue-router';
	import PrePlan from "@/views/erp/purchase/components/preplan.vue";
	import {getCountNum,downloadDetailExcel,downloadFormExcel,downExcelTemp,uploadExcel } from '@/api/erp/assembly/assemblyApi.js';
	import dispatchApi from '@/api/erp/inventory/dispatchApi.js';
	let props = defineProps({ isConsumable:"" })
	const { isConsumable } = toRefs(props);
	const router = useRouter();
	const shipmentid = router.currentRoute.value.query.shipmentid;
	const mainsku = router.currentRoute.value.query.mainsku;
	const emits  =defineEmits(
		['change','delete','downloadPDF']
	)
    const state =reactive({
		orderState:[
			{label:'全部',name:''},
			{label:'处理中',name:'2',num:'5'},
			{label:'已完成',name:'3'},
			{label:'已终止',name:'4'},
			{label:'已作废',name:'5'},
		],
		activeName:'',
		radio:'全部',
		planname:'查看计划',
		queryParams:{searchtype:"sku",operate:""},
		editDatas:{},
		uploadVisible:false,
		
	})
	const {
		orderState,
		activeName,
		queryParams,
		planname,
		editDatas,
		uploadVisible,
		radio,
	}=toRefs(state)
	defineExpose({
		state,show,setSelectRows
	});
	function gotoShelf(){
		emits('goshelf');
	}
	
	function goDispatch(){
		emits('dispatch');
	}
	
	function handleAdd(){
		router.push({
			path:"/e/p/p/c",
			query:{
				title:'添加加工单',
				path:"/e/p/p/c",
			},
		})
	}
	function downloadDetailExcelHandle(){
		downloadDetailExcel(state.queryParams);
	}
	function downloadFormExcelHandle(){
		downloadFormExcel(state.queryParams);
	}
	//导入
	function upload(){
		state.uploadVisible = true;
	}
	function getWarehouse(value,load){
		state.queryParams.warehouseid=value;
	}
	function loadRunid(preprocessingForm){
			state.queryParams.runid=preprocessingForm.id;
			state.queryParams.preprocessingid="";
			emits('change',state.queryParams);
	  }
	function loadQueryid(preprocessingForm){
		state.queryParams.runid=preprocessingForm.id;
		state.queryParams.preprocessingid=preprocessingForm.id;
		emits('change',state.queryParams);
	}
	function handleQuery(){
		emits('change',state.queryParams);
	}
	
	function setSelectRows(table,selected){
		var datas={};
		var skulists=[];
		var tablelist=[];
		if(selected && selected.length>0){
			selected.forEach(item=>{
				if(item.id){
				  skulists.push(item.id);
				}
			});
		}
		if(table && table.length>0){
			table.forEach(items=>{
				tablelist.push(items.id);
			});
		}
		datas.selectList=skulists;
		datas.tableList=tablelist;
		state.editDatas=datas;
	}
	
	function addTableData(){
		return state.editDatas;
	}
	function changeDate(strdate,timedate){
		state.queryParams.fromDate=timedate.start;
		state.queryParams.toDate=timedate.end;
		emits('change',state.queryParams);
	}
	function tabChange(val){
		state.queryParams.auditstatus=val;
		emits('change',state.queryParams);
	}
	function handleInputQuery(){
		if(state.queryParams.search){
			if(state.queryParams.search.indexOf("AS")==0||state.queryParams.search.indexOf("MO")==0){
				state.queryParams.searchtype="number";
			}
			else if(state.queryParams.search.indexOf("FBA")==0){
				state.queryParams.searchtype="shipment";
			}
		}
		
		handleQuery();
	}
	function handleDelete(){
		emits('delete');
	}
	function show(){
		getCountNum().then(res=>{
			if(res.data){
				state.orderState[1].num=res.data.inProgressNum;
				if(res.data.pendingNointerNum){
				   state.orderState[1].num=state.orderState[1].num+res.data.pendingNointerNum;
				}
				if(res.data.pendingNum){
					state.orderState[1].num=state.orderState[1].num+res.data.pendingNum;
				}
			}
		})
	}
	function downloadTemp(){
		downExcelTemp();
	}
	
	function downloadAssPDFHandle(){
		emits('downloadPDF');
	}
	function handleUploadExcel(){
		let FormDatas = new FormData();
		FormDatas.append('file',state.myfile);
		uploadExcel(FormDatas).then(function(res){
			if(res.data!="上传成功"){
				ElMessage.error(res.data);
			}else{
				ElMessage.success('上传成功');
				state.uploadVisible = false;
				handleQuery();
			}
		})
	}
	//文件上传之前
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error(  '上传文件大小不能超过 5MB!!');
				return false;
			}
			else {
			   return true;
			}
		}
	}
	function uploadFiles(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	onMounted(()=>{
		state.orderState[1].num=0;
		if(shipmentid){
			state.queryParams.searchtype="shipment";
			state.queryParams.search=shipmentid;
			emits('change',state.queryParams);
		}
		if(mainsku){
			state.activeName="2";
			state.queryParams.auditstatus="2";
			state.queryParams.searchtype="sku";
			state.queryParams.search=mainsku;
			emits('change',state.queryParams);
		}
		if(props.isConsumable=='ok'){
		    state.queryParams.isplan=true;
			state.queryParams.ftype="ass";
			emits('change',state.queryParams);
		} else{
			state.queryParams.isplan=false;
			emits('change',state.queryParams);
		}
		show();
	})  
</script>

<style scoped="scoped">
	.text-orange{
		font-weight: 700;
		color:var(--el-color-primary);
		font-size: 12px;
	}
</style>
