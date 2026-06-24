<template>
  <div class="main-sty">
    <div class="con-header">
      <el-tabs v-model="queryParams.xmlType" class="demo-tabs" @tabChange="handleQuery">
        <el-tab-pane label="电商订单" name="CEB303"></el-tab-pane>
        <el-tab-pane label="电商出口清单" name="CEB603"></el-tab-pane>
        <el-tab-pane label="出口报关单" name="DEC001"></el-tab-pane>
      </el-tabs>
      <el-row :gutter="24">
        <el-col :span="8">
          <el-card header="申报信息" shadow="hover">
            <div class="flex-between">
              <div v-if="summaryData.total_price">
                <span class="font-extraSmall">金额:</span>
                <span class="font-bold text-orange"> {{getCurrencyMark(market.currency)}} {{getValue(summaryData.total_price)}}</span>
              </div>
              <div v-if="summaryData.total_quantity">
                <span class="font-extraSmall">数量:</span>{{getValue(summaryData.total_quantity)}}
              </div>
              <div v-if="summaryData.net_weight">
                <span class="font-extraSmall">净重:</span>{{getValue(summaryData.net_weight)}}
              </div>
              <div v-if="summaryData.gross_weight" >
                <span class="font-extraSmall">毛重:</span>{{getValue(summaryData.gross_weight)}}
              </div>
            </div>
          </el-card>
        </el-col>
         <el-col :span="8">
          <el-card  header="运输信息"  shadow="hover">
            <div class="flex-between">
              <div v-if="summaryData.transweight">
                <span class="font-extraSmall">物流重量:</span>{{getValue(summaryData.transweight)}}
              </div>
               <div v-if="summaryData.boxweight">
                <span class="font-extraSmall">装箱重量:</span>{{getValue(summaryData.boxweight)}}
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card   header="数量统计"  shadow="hover">
            <div class="flex-between">  
              <div v-if="summaryData.num">
                <span class="font-extraSmall">单据个数:</span>{{getValue(summaryData.num)}}
              </div>
               <div v-if="summaryData.boxnum">
                <span class="font-extraSmall">箱子个数:</span>{{getValue(summaryData.boxnum)}}
              </div>
               <div v-if="summaryData.shipmentnum">
                <span class="font-extraSmall">货件个数:</span>{{getValue(summaryData.shipmentnum)}}
              </div>
            </div>
          </el-card>
        </el-col>
       
      </el-row>
      <div class="flex-between" style="padding-bottom:20px">
        <el-space>
          <el-radio-group v-model="queryParams.type" @change="handleQuery" >
            <el-radio-button label="申报单"   value="declare" />
            <el-radio-button label="SKU统计"  value="sku" />
          </el-radio-group>
          <Group  @change="groupChange" defaultValue="onlyMarket" isproduct="market"></Group>
          <div class="select-group">
          <el-select v-model="queryParams.dateType" @change="handleQuery" style="width: 120px;" class="select-group-left" placeholder="日期类型">
            <el-option label="申报时间" value="app_time"></el-option>
            <el-option label="操作时间" value="opttime"></el-option>
          </el-select>
            <Datepicker longtime="ok" ref="datepickers" class="select-group-right" @changedate="changedate" />
          </div>

          <el-input  v-model="queryParams.search" @input="handleQuery" clearable placeholder="请输入SKU" style="width: 250px;" class="input-with-select" >
            <template #append>
              <el-button @click="handleQuery" >
                <el-icon class="ic-cen font-medium">
                  <search/>
                </el-icon>
              </el-button>
            </template>
          </el-input>

          <el-button type="primary" @click.stop="downloadExcel">导出</el-button>
          <el-button type="primary" @click.stop="showImportDialog">导入发送状态</el-button>

        </el-space>
      </div>
    </div>
    <div class="con-body" v-if="queryParams.type=='declare'">

<!--      show-summary-->
<!--      :summary-method="getSummaries"-->
      <GlobalTable ref="globalTable"
                   :tableData="tableData"  height="calc(100vh - 350px)" @selectionChange='handleSelect'
                      @loadTable="loadTableData" :stripe="true"
                   style="width: 100%;margin-bottom:16px;">
        <template #field>
          <el-table-column label="申报状态/类型" width="130" prop="app_status" sortable="custom">
            <template #default="scope">
              {{scope.row.app_status==1?"暂存":scope.row.app_status==2?"申报":"处理中"}}/
              {{scope.row.app_type==1?"新增":scope.row.app_type==2?"变更":"删除"}}
            </template>
          </el-table-column>
          <el-table-column label="申报类型" width="150" prop="xml_type" show-overflow-tooltip sortable="custom">
            <template #default="scope">
              {{scope.row.xml_type=="CEB303"?"电商订单":scope.row.xml_type=="CEB603"?"电商出口清单":"出口报关单"}}
              <div>
               申报时间: {{scope.row.app_time}}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="申报金额" prop="total_price" sortable="custom">
            <template #default="scope">
              {{getValue(scope.row.total_price)}}
            </template>
          </el-table-column>
          <el-table-column label="申报数量" prop="total_quantity" sortable="custom">
            <template #default="scope">
              {{getValue(scope.row.total_quantity)}}
            </template>
          </el-table-column>
          <el-table-column label="重量" prop="gross_weight" sortable="custom">
            <template #default="scope">
              <div>毛重 {{getValue(scope.row.gross_weight)}}</div>
              <div>净重 {{getValue(scope.row.net_weight)}}</div>
            </template>
          </el-table-column>
          <el-table-column label="申报货件" prop="number" sortable="custom" width="350" :show-overflow-tooltip="true"></el-table-column>
           <el-table-column label="装箱重量" prop="boxweight" sortable="custom">
            <template #default="scope">
              <div>装箱重量 {{getValue(scope.row.boxweight)}}</div>
              <div>箱数 {{getValue(scope.row.boxnum)}}</div>
            </template>
          </el-table-column>
              <el-table-column label="物流重量" prop="transweight" sortable="custom">
            <template #default="scope">
              {{getValue(scope.row.transweight)}}
            </template>
          </el-table-column>
          <el-table-column label="返回信息" prop="return_status" show-overflow-tooltip width="150" sortable="custom">
            <template #default="scope">
             <div>
<!--               操作结果（1 电子口岸已暂存/2 电子口岸申报中/3 发送海关成功/4 发送海关失败/100 海关退单/120 海关入库/300 人工审核/399 海关审结/800 放行/899 结关等,若小于 0
               数字表示处理异常回执-->
               <el-tag type="danger" v-if="scope.row.return_status<=0">处理异常</el-tag>
               <el-tag type="success" v-if="scope.row.return_status==1">电子口岸已暂存</el-tag>
               <el-tag type="success" v-if="scope.row.return_status==2">电子口岸申报中</el-tag>
               <el-tag type="success" v-if="scope.row.return_status==3">发送海关成功</el-tag>
               <el-tag type="danger" v-if="scope.row.return_status==4">发送海关失败</el-tag>
               <el-tag type="danger" v-if="scope.row.return_status==100">海关退单</el-tag>
               <el-tag type="success" v-if="scope.row.return_status==120">海关入库</el-tag>
               <el-tag type="warning" v-if="scope.row.return_status==300">人工审核</el-tag>
               <el-tag type="success" v-if="scope.row.return_status==399">海关审结</el-tag>
               <el-tag type="success" v-if="scope.row.return_status==800">放行</el-tag>
               <el-tag type="success" v-if="scope.row.return_status==899">结关</el-tag>
             </div>
             <div>返回时间:{{scope.row.return_time}}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作时间" prop="opttime" sortable="custom"></el-table-column>
          <el-table-column label="操作" prop="file_path" sortable="custom">
            <template #default="scope">
              <el-button link type="primary" size="mini" @click="showFile(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </template>
      </GlobalTable>
    </div>
    <div class="con-body" v-if="queryParams.type=='sku'">
      <GlobalTable ref="globalTableSkuRef"
                   :tableData="tableData"  
                    height="calc(100vh - 350px)" 
                    @selectionChange='handleSelect'
                    @loadTable="loadTableData" 
                    :stripe="true"
                   style="width: 100%;margin-bottom:16px;">
        <template #field>
          <el-table-column label="SKU" prop="sku" sortable="custom"></el-table-column>
          <el-table-column label="商品名称" prop="name" sortable="custom"></el-table-column>
          <el-table-column label="申报单价" prop="price" sortable="custom">
            <template #default="scope">
              {{getValue(scope.row.price)}}
            </template>
          </el-table-column>
          <el-table-column label="申报数量" prop="quantity" sortable="custom">
            <template #default="scope">
              {{getValue(scope.row.quantity)}}
            </template>
          </el-table-column>
               <el-table-column label="申报金额" prop="total_price" sortable="custom">
            <template #default="scope">
              {{getValue(scope.row.total_price)}}
            </template>
          </el-table-column>
        </template>
      </GlobalTable>
    </div>
  </div>
  <Customs_inventory_xml ref="inventoryXmlRef"></Customs_inventory_xml>
  <Customs_order_xml ref="orderXmlRef"></Customs_order_xml>
  <Customs_declare_xml ref="declareXmlRef"></Customs_declare_xml>
  <el-dialog v-model="importDialogVisible" title="导入发送状态" width="800px" :close-on-click-modal="false">
    <el-form :model="importForm">
      <el-form-item label="选择文件">
        <el-upload
          ref="uploadRef"
          :auto-upload="false"
          multiple
          accept=".xml"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :file-list="importForm.fileList"
        >
          <el-button type="primary">选择XML文件</el-button>
          <template #tip>
            <div class="el-upload__tip">
              支持多选 .xml 文件
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="importDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleImportSubmit" :loading="importLoading">上传文件</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref,reactive,toRefs,onMounted}from"vue"
import {MoreOne} from '@icon-park/vue-next';
import Group from '@/components/header/group.vue';
import { ElMessage, ElMessageBox,ElForm } from 'element-plus';
import shipmentCustomsApi from "@/api/erp/shipv2/shipmentCustomsApi.js";
import {Search} from '@element-plus/icons-vue';
import Datepicker from '@/components/header/datepicker.vue';
import Customs_inventory_xml  from "@/views/erp/shipv2/shipment_handing/shipstep/components/customs_inventory_xml.vue";
import Customs_order_xml from "@/views/erp/shipv2/shipment_handing/shipstep/components/customs_order_xml.vue";
import Customs_declare_xml from "@/views/erp/shipv2/shipment_handing/shipstep/components/customs_declaration_xml.vue";
import {getValue,getCurrencyMark} from '@/utils/index.js';

const inventoryXmlRef =ref(Customs_inventory_xml);
const orderXmlRef =ref(Customs_order_xml);
const declareXmlRef =ref(Customs_declare_xml);
let globalTable = ref();
let globalTableSkuRef = ref();

const dataFormRef = ref(ElForm);
const props = {
  expandTrigger: 'hover',
  value:'id',
  label:'name',
}
let state = reactive({
  tableData: {records:[],total:0},
  queryParams:{
    search:"",
    xmlType:"CEB303",
    marketplaceid:null,
    dateType:"app_time",
    type:"declare",
  },
  summary:{},
  isload:true,
  summaryData: {},
  market: {}
})
let {
  tableData,queryParams,tableDataEU,
  summary,isload,summaryData,market
} =toRefs(state);

function groupChange(obj){
  state.queryParams.groupid=obj.groupid;
  state.queryParams.marketplaceid = obj.marketplaceid;
  state.market = obj.market;
  handleQuery();
}


function downloadExcel(){
 shipmentCustomsApi.downExcelCustomsXml(state.queryParams);
}

function handleQuery(){
  state.isload=false;
  state.queryParams.warehouseid = state.warehouseCheck;
  if(state.queryParams.type=="declare"){
    globalTable.value.loadTable(state.queryParams);
  }
  if(state.queryParams.type=="sku"){
    globalTableSkuRef.value.loadTable(state.queryParams);
  }
}

function loadSummary(){
  shipmentCustomsApi.summaryCustomsXml(state.queryParams).then(res=>{
    state.summaryData=res.data;
  })
}

function loadTableData(params) {
  if(state.queryParams.type=="declare"){
    shipmentCustomsApi.listCustomsXml(params).then(res=>{
      state.tableData.records=res.data.records;
      state.tableData.total=res.data.total;
  })
  }
  if(state.queryParams.type=="sku"){
    shipmentCustomsApi.listCustomsXmlItem(params).then(res=>{
      state.tableData.records=res.data.records;
      state.tableData.total=res.data.total;
  })
  }
  
  loadSummary();
}

function showFile(row){
  if(state.queryParams.xmlType=="CEB303"){
    orderXmlRef.value.show(row.groupid,null,row.guid);
  }
  if(state.queryParams.xmlType=="CEB603"){
    inventoryXmlRef.value.show(row.groupid,null,row.guid);
  }
  if(state.queryParams.xmlType=="DEC001"){
    declareXmlRef.value.show(row.groupid,null,row.guid);
  }
}
//日期改变

function changedate(dates){
  state.queryParams.fromDate=dates.start;
  state.queryParams.toDate=dates.end;
  if(state.isload==false){
    handleQuery();
  }
}

// 导入发送状态相关
let importDialogVisible = ref(false);
let importLoading = ref(false);
let uploadRef = ref(null);
let importForm = reactive({
  fileList: []
});
let importFiles = ref([]);

function showImportDialog() {
  importDialogVisible.value = true;
  importForm.fileList = [];
  importFiles.value = [];
}

function handleFileChange(file) {
  importFiles.value.push(file.raw);
}

function handleFileRemove(file) {
  importFiles.value = importFiles.value.filter(f => f.name !== file.name && f.size !== file.size);
}

function handleImportSubmit() {
  if (importFiles.value.length === 0) {
    ElMessage.warning('请先选择XML文件');
    return;
  }
  importLoading.value = true;
  let formData = new FormData();
  importFiles.value.forEach(file => {
    formData.append('files', file);
  });
  shipmentCustomsApi.importReturnStatus(formData).then(res => {
    importLoading.value = false;
    if (res.data && res.data.success) {
      let msg = res.data.msg || '导入完成';
      ElMessage.success(msg);
      handleQuery();
    } else {
      ElMessage.warning(res.data ? (res.data.msg || '导入失败') : '导入失败');
    }
    importDialogVisible.value = false;
  }).catch(() => {
    importLoading.value = false;
    ElMessage.error('导入请求失败');
  });
}

/* 合计行数据 */
function getSummaries(){
  var arr = ["合计"]
  arr[2]=state.summary.afnWarehouseQuantity;
  arr[3]=state.summary.afnFulfillableQuantity;
  arr[4]=state.summary.afnReservedQuantity;
  arr[5]=state.summary.afnInboundWorkingQuantity;
  arr[6]=state.summary.afnInboundShippedQuantity;
  arr[7]=state.summary.afnInboundReceivingQuantity;
  arr[8]=state.summary.afnUnsellableQuantity
  arr[9]=state.summary.afnResearchingQuantity;
  return  arr
}

onMounted(()=>{
})
</script>

<style scoped>
.img-40{width: 40px;
  height: 40px;flex: none;
  margin-right: 8px;}
</style>
<style>

.select-group .select-group-left .el-select__wrapper{
  border-right: none;
  border-top-right-radius: 0px !important;
  border-bottom-right-radius: 0px !important;

}
.select-group .select-group-right {
  border-top-left-radius:0px !important;
  border-bottom-left-radius:0px !important;
  border-left: none;

}
</style>