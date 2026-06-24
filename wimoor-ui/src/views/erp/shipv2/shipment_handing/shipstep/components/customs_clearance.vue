<template>
  <div class="customs-wrap">
    <el-table :data="productData.list" id="customs-clearance-table"  border style="width: 100%;" >
      <el-table-column prop="image" label="图片" width="70">
        <template #default="scope">
          <img :src="scope.row.image" @click.stop="handleToMaterial(scope.row)" style="width:40px;height:40px" />
        </template>
      </el-table-column>
      <el-table-column label="名称/SKU"  show-overflow-tooltip>
        <template #default="scope">
          <div class='name text-omit-1'>{{scope.row.name}}</div>
          <div class='sku'>{{scope.row.sellersku}} <el-tag v-if="scope.row.boxnum&&shipInfo.arecasesrequired">原装单箱：{{scope.row.boxnum}}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="quantityshipped" label="申报数量" width="120"   >
        <template #default="scope">
          <div>{{scope.row.quantityshipped}}  <span class="font-extraSmall">{{scope.row.unit}}</span> <span  class="font-extraSmall">（{{formatFloat(scope.row.quantityshipped*scope.row.weight)}}kg）</span> </div>
          <div ><span class="font-extraSmall">售价：</span><span class="text-orange">{{getCurrencyMark(scope.row.currency)+formatFloat(scope.row.infoprice)}}</span></div>
        </template>
      </el-table-column>

      <el-table-column prop="unit" label="单位" width="80"   >
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.unit}}</div>
          <el-autocomplete
              v-else v-model="scope.row.unit"
              :fetch-suggestions="querySearch"
              style="width: 67px;"
              clearable
              class="in-wi-24"
              placeholder="单位"
          />
        </template>
      </el-table-column>

      <el-table-column  label="申报单价" width="120">
        <template #header>
          <el-icon @click="showRateDialog"><Setting /></el-icon>
          <span>申报单价</span>
          <el-button style="display:inline" :disabled="!isEdit" link @click="calcRate"><el-icon ><Refresh /></el-icon></el-button>
        </template>
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.price}}</div>
          <el-input  v-else v-model="scope.row.price"></el-input>
        </template>
      </el-table-column>
      <el-table-column  label="海关编码" width="130">
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.code}}</div>
          <el-input v-else v-model="scope.row.code"></el-input>
        </template>
      </el-table-column>
      <el-table-column  label="税率" width="100">
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.rate}}</div>
          <el-input v-else v-model="scope.row.rate"></el-input>
        </template>
      </el-table-column>
      <el-table-column  label="产品材质" width="100" show-overflow-tooltip>
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.material}}</div>
          <el-input  v-else v-model="scope.row.material"></el-input>
        </template>
      </el-table-column>
      <el-table-column  label="产品材质(中文)" width="120">
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.materialcn}}</div>
          <el-input v-else v-model="scope.row.materialcn"></el-input>
        </template>
      </el-table-column>
      <el-table-column  label="产品用途" width="100">
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.application}}</div>
          <el-input v-else v-model="scope.row.application"></el-input>
        </template>
      </el-table-column>
      <el-table-column   label="链接/品牌" width="90">
        <template #default="scope">
          <el-popover
              placement="top"
              title="编辑链接"
              :width="250"
              trigger="hover"
          >
            <template #reference>
              <el-link :href="scope.row.infourl" target="_blank"><el-icon  ><Link/></el-icon></el-link>
            </template>
              {{scope.row.infourl}}
          </el-popover>
          <div v-if="scope.row.brand" class="font-extraSmall">{{scope.row.brand}}</div>
        </template>
      </el-table-column>
      <el-table-column label="海关要素" show-overflow-tooltip width="180">
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.elements}}</div>
          <el-input type="textarea" rows="2" v-else v-model="scope.row.elements"></el-input>
          <div v-if="!isEdit && scope.row.elements" class="i-icon i-icon-copy">
            <el-icon class="copy-icon" @click="copyElements(scope.row.elements)">
              <CopyDocument />
            </el-icon>
          </div>
        </template>
      </el-table-column>
      <el-table-column  label="英文名" width="180">
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.ename}}</div>
          <el-input v-else v-model="scope.row.ename"></el-input>
        </template>
      </el-table-column>
      <el-table-column  label="中文名" width="120">
        <template #default="scope">
          <div v-if="!isEdit">{{scope.row.cname}}</div>
          <el-input v-else v-model="scope.row.cname"></el-input>
        </template>
      </el-table-column>
    </el-table>
    <el-row >
      <el-space>

      </el-space>
      <div class="rt-btn-group m-t-8">
        <el-space>
        <el-button v-if="!isEdit"  :loading="confirmloading" @click.stop="isEdit=true">编辑海关信息</el-button>
        <el-space  v-else >
        <el-button  :loading="confirmloading" @click.stop="loadCustoms(shipInfo.shipmentid,true)">重新载入</el-button>
        <el-button  :loading="confirmloading" @click.stop="saveCustoms">保存海关信息</el-button>
        </el-space>
        <el-button v-if="shipInfo.shipment" :disabled="isEdit"  type="primary"   @click.stop="visible=true">下载发票</el-button>
        </el-space>
      </div>
    </el-row>
  </div>
  <el-dialog
      v-model="visible"
      title="下载发票"
      top="8vh"
      :destroy-on-close='true'
  >
    <div style="margin-top:-10px;padding-left:10px">{{templatetitle}}</div>
    <el-row>
      <el-table :data="filterTableData"  style="margin-top: 15px;" height="calc(100vh - 400px)">
        <el-table-column prop="filename" label="模板上传名称"  sortable   />
        <el-table-column prop="createdate" label="创建时间"   sortable  width="100" />
        <el-table-column prop="username" label="创建者"  sortable   width="100" />
        <el-table-column prop="opttime" label="操作时间"  sortable width="100" />
        <el-table-column prop="opttime" label="操作" width="240">
          <template #header>
            <el-input size="small" v-model="search" clearable placeholder="输入海关模版名称"></el-input>
          </template>
          <template #default="scope">
            <el-space>
              <el-link type="success" @click="downloadCustoms(scope.row.id)">发票下载</el-link>
              <el-link style="margin-left:20px" type="info" @click="downloadCustomsTemp(scope.row.id,scope.row.filename)">模板下载</el-link>
              <el-link v-if="scope.row.shopid" style="margin-left: 10px;" @click="deleteCustoms(scope.row.id)" type="danger">删除</el-link>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <template #footer>
		       <span class="dialog-footer">
				    <div>
						  <el-button  type="primary" class="pull-left" @click="showUploadTemplate">上传模板</el-button>
		          <el-button @click="visible = false">关闭</el-button>
					 </div>
		       </span>
    </template>
  </el-dialog>

  <!-- 上传文件弹框 -->
  <el-dialog
      v-model="uploadVisible"
      :title="uploadTitle"
      width="400px"
      style="padding: 5px;"
  >
    <el-upload
        :drag="true"
        action
        ref="uploadRef"
        :http-request="uploadFiles"
        :limit="1"
        :on-exceed="handleExceed"
        :before-upload="beforeUpload"
        :show-file-list="true"
        :headers="headers"
        accept=".xlsx"
    >
      <el-icon class="font-large"><upload-filled /></el-icon>
      <div class="el-upload__text">
        拖拽文件到此处或 <em>点击上传</em>
      </div>
    </el-upload>
    <template #footer>
		      <span class="dialog-footer">
		   	   <div class="flex-center-between">
		   	 <el-button   type="success" @click.stop="downloadTemplate" plain>下载模板</el-button>

		   	 <div>
		        <el-button @click="uploadVisible = false">取消</el-button>
		        <el-button type="primary" v-loading="uploadloading" @click.stop="uploadExcel">
		          上传文件
		        </el-button></div></div>
		      </span>
    </template>
  </el-dialog>

  <el-dialog v-model="rateVisible"
             title="设置申报海关单价比例"
             width="550px"
             style="padding: 5px;"
  >
    <el-table :data="rateTableData" border>
      <el-table-column label="国家" prop="name"></el-table-column>
      <el-table-column label="参考价格" prop="region_name">
        <template #default="scope">
          <el-select v-model="scope.row.price_type" style="width:100px;">
            <el-option label="采购成本" value="cost" key="cost"   ></el-option>
            <el-option label="销售价格" value="price" key="price"   ></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="设置比例(填写小数)" prop="marketplaceId" >
        <template #default="scope">
          <el-input v-model="scope.row.rate"  placeholder="请输入比例,例如0.1"></el-input>
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
			<span class="dialog-footer">
				<el-button @click="rateVisible = false">关闭</el-button>
				<el-button type="primary" @click="submitRate">提交</el-button>
			</span>
    </template>
  </el-dialog>
</template>

<script setup>
import {Search,ArrowDown,WarningFilled,Setting,Refresh,Link,CopyDocument} from '@element-plus/icons-vue'
import { ref,reactive,onMounted,toRefs, computed } from 'vue'
import { ElMessage,ElMessageBox } from 'element-plus';
import { useRoute,useRouter } from 'vue-router'
import {downloadShipmentLabel} from '@/hooks/amazon/listing/label.js';
import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
import materialApi from '@/api/erp/material/materialApi.js';
import shipmentCustomsApi from '@/api/erp/shipv2/shipmentCustomsApi.js';
import downloadTemplateFileApi from '@/api/erp/common/downloadTemplateFileApi.js';
import customssetApi from "@/api/amazon/inbound/customssetApi.js";
import {formatFloat,getCurrencyMark} from '@/utils/index.js';
const emit = defineEmits(['stepdata']);
const uploadRef=ref();
let router = useRouter()
let productData=reactive({list:[]})
let shipInfo=ref({})
let validateLoading=ref(false);
let hassubmit=ref(true);
let boxcontents=ref("FEED")
let piceDisable=ref(false);
let confirmloading=ref(false);
const customsXmlRef=ref(null);
let boxconName=ref("系统提交装箱");
let visible=ref(false);
let loading=reactive({v1:false,v2:false,v3:false})
let state =reactive({
  xmlVisible:false,
  uploadVisible:false,
  rateVisible:false,
  uploadloading:false,
  isEdit:true,
  uploadTitle:'导入海关模板',
  myfile:null,
  templatetitle:"",
  tableData:[],
  search:'',
  rateTableData:[],
  countryList:[],
  unitOptions:[{label:'个',value:'个'},{label:'件',value:'件'},{label:'套',value:'套'},{label:'台',value:'台'},{label:'箱',value:'箱'},{label:'包',value:'包'},{label:'千克',value:'千克'},{label:'克',value:'克'},{label:'米',value:'米'},{label:'厘米',value:'厘米'}],

});
let{ uploadVisible,uploadloading,uploadTitle,myfile,tableData,search,templatetitle,isEdit,
  rateVisible,rateTableData,countryList,unitOptions,
}=toRefs(state);

function downloadLabel(ftype){
  downloadShipmentLabel(shipInfo.value.shipmentid,ftype,null);
}
function handleNoticeSuccess(){
  ElMessage.success( '修改链接成功！');
}
function downloadCustoms(id){
  var shipmentid=shipInfo.value.shipmentid;
  //loading[versionval]=true;
  var data=shipInfo.value;
  var title="";
  if(data&&data.transinfo){
    if(data.transinfo.company){
      title=title+data.transinfo.company+" - ";
    }
    if(data.transinfo.channame){
      title=title+data.transinfo.channame+" - ";
    }
  }
  if(data&&data.shipmentAll){
    if(data.shipmentAll.referenceid){
      title=title+"["+data.shipmentAll.referenceid+"]";
    }
  }
  shipmentCustomsApi.downloadShipTemplateFile({"templateid":id,"shipmentid":shipmentid},()=>{
    //loading[versionval]=false;
  },title+"("+shipInfo.value.shipment.shipmentConfirmationId+"-"+data.shipmentAll.country+"-"+data.shipmentAll.center+"-"+data.totalBoxNum+'箱-海关发票).xlsx');
}
function downloadCustomsTemp(id,name){
  var shipmentid='#';
  //loading[versionval]=true;
  shipmentCustomsApi.downloadShipTemplateFile({"templateid":id,"shipmentid":shipmentid},()=>{
    //loading[versionval]=false;
  },name);
}
const filterTableData=computed(()=> state.tableData.filter((data)=> !state.search||data.filename.includes(state.search)));
function saveCustoms(){
  confirmloading.value=true;
  productData.list.forEach(item=>{
    item.itemid=item.id;
    item.country=shipInfo.value.market;
    item.opttime=null;
    item.operator=null;
    item.createtime=null;
    item.creator=null;
  })
  hassubmit.value=true;
  shipmentTransportationApi.saveCustomsList(productData.list).then(res=>{
    materialApi.saveCustoms(productData.list).then(res=>{
      confirmloading.value=false;
      ElMessage.success( '保存成功！');
      shipInfo.value.shipment.status= shipInfo.value.shipment.status+1;
      hassubmit.value=false;
      loadCustoms(shipInfo.value.shipmentid)
    }).catch(e=>{
      confirmloading.value=false;
    });
  }).catch(e=>{
    confirmloading.value=false;
  })
}
async function loadCustoms(shipmentid,isreload){
  await shipmentTransportationApi.getCustomsList({"shipmentid":shipmentid}).then(res=>{
    if(res.data){
      state.isEdit=false;
      productData.list.forEach(async item=>{
        if(res.data[item.id]){
          item=Object.assign(item,res.data[item.id]);
          item.isinship=true;
          if(item.price){
            item.price=formatFloat(item.price);
          }
            item.url=item.infourl;
        }
      })
    }
  });

  for(var i=0;productData&&productData.list&&i<productData.list.length;i++){
    var item=productData.list[i];
    if(!item.isinship||isreload){
      await materialApi.getCustoms({"msku":item.msku,"country":shipInfo.value.market}).then(res=>{
        item=Object.assign(item,res.data);
        if(item.price){
          item.price=formatFloat(item.price);
        }
        item.url=item.infourl;
      })
    }
  }
  if(isreload){
    state.isEdit=true;
  }
}
const querySearch = (queryString, cb) => {
  const results = queryString
      ? unitOptions.value.filter(createFilter(queryString))
      : unitOptions.value
  // call callback function to return suggestions
  cb(results)
}
function loadOptData(data){
  data.shipmentid=data.shipment.shipmentid;
  shipInfo.value=data;

  if(data&&data.shipmentAll){
    state.templatetitle=data.shipmentAll.country+"("+data.shipmentAll.center+")";
    if(data.shipmentAll.referenceid){
      state.templatetitle=state.templatetitle+"["+data.shipmentAll.referenceid+"]";
    }
  }
  if(data&&data.transinfo){
    if(data.transinfo.company){
      state.templatetitle=state.templatetitle+" - "+data.transinfo.company;
    }
    if(data.transinfo.channame){
      state.templatetitle=state.templatetitle+" - "+data.transinfo.channame;
    }
  }
  productData.list=data.itemlist;
  loadCustoms(data.shipmentid,false);
  handleQuery();
  loadCountry();
}
 

function handleToMaterial(row){
  router.push({
    path:'/material/details',
    query:{
      title:"产品详情",
      path:'/material/details',
      details:row.mid,
    }
  })
}

//文件上传之前
function beforeUpload(file){
  if (file.type != "" || file.type != null || file.type != undefined){
    //截取文件的后缀，判断文件类型
    const FileExt = file.name.replace(/.+\./, "").toLowerCase();
    //计算文件的大小
    const isLt5M = file.size / 1024  < 50000; //这里做文件大小限制
    //如果大于50M
    if (!isLt5M) {
      ElMessage.error('上传文件大小不能超过 50MB!!');
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

function handleExceed(files){
  uploadRef.value.clearFiles();
  const myfile = files[0]  ;
  myfile.uid = genFileId();
  uploadRef.value.handleStart(myfile);
}

function uploadExcel(){
  let FormDatas = new FormData()
  state.uploadloading=true;
  FormDatas.append('file',state.myfile);
  shipmentCustomsApi.uploadCustomsFile(FormDatas).then(function(res){
    ElMessage.success('上传海关模板成功');
    state.uploadloading=false;
    state.uploadVisible = false;
    handleQuery();
  })

}

function downloadTemplate(){
  downloadTemplateFileApi.downExcelTemp({"ftype":'ShipmentCustoms'});
}
function showUploadTemplate(){
  state.uploadVisible=true;
  state.uploadloading = false;

}


function deleteCustoms(id){
  ElMessageBox.confirm(
      '您确认要删除吗?',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        shipmentCustomsApi.deleteCustomsFile({"uploadid":id}).then((res)=>{
          ElMessage.success('删除海关模板成功');
          handleQuery();
        });
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消操作',
        })
      })

}

function handleQuery(){
  shipmentCustomsApi.getShipmentCustomsRecord().then((res)=>{
    state.tableData=res.data;
  });
}
function showRateDialog(){
  state.rateVisible=true;
}

function loadCountry(){
  customssetApi.getRate().then((res)=>{
    state.rateTableData=res.data;
  });
}
function calcRate(){
  var nowRate=null;
  var mymarket= shipInfo.value.market;
  var mycurrency=productData.list[0].currency;
  state.rateTableData.forEach(market=>{
    if(mymarket==market.market){
      nowRate=market;
    }else {
      if(mycurrency=="EUR" ){
        nowRate=market;
      }else if(mycurrency=="SEK" ){
        nowRate=market;
      }else if(mycurrency=="TRY" ){
        nowRate=market;
      }else if(mycurrency=="PLN"){
        nowRate=market;
      }else if(mycurrency=="SAR"){
        nowRate=market;
      }
    }
  })
  productData.list.forEach(item=>{
    var infoprice=item.infoprice;
    var localprice=item.new_unitcost;
    if(nowRate.price_type=="cost"){
      item.price=formatFloat(localprice*nowRate.rate);
    }else{
      item.price=formatFloat(infoprice*nowRate.rate);
    }
  })

}

// 复制海关要素内容
function copyElements(elements) {
  if (!elements) return;

  // 创建临时textarea元素
  const textarea = document.createElement('textarea');
  textarea.value = elements;
  document.body.appendChild(textarea);

  // 选择并复制内容
  textarea.select();
  document.execCommand('copy');

  // 移除临时元素
  document.body.removeChild(textarea);

  // 显示复制成功提示
  ElMessage.success('海关要素内容已复制到剪贴板');
}
function submitRate(){
  var lists=[];
  state.rateTableData.forEach(item=>{
    if(item.rate && parseFloat(item.rate)>0){
      lists.push({"marketplaceid":item.marketplaceId,"priceType":item.price_type,"rate":parseFloat(item.rate)});
    }
  });
  customssetApi.saveRate(lists).then((res)=>{
    ElMessage.success('操作成功');
    state.rateVisible=false;
  });
}

defineExpose({loadOptData})
</script>

<style scoped>
.icon-prin{
  display: flex;
  align-items: center;
}
.bg-red{
  background-color: var(--el-button-hover-bg-color);
}
.icon-prin .el-dropdown-link{
  margin-left: 8px;
  color: var(--el-color-primary);
}
.m-t-16{
  margin-top:16px;
}

.pull-left{
  float:left;
}
.customs-wrap{
  margin-left:16px;
  margin-right:16px;
}
/* 复制图标的样式 */
.copy-icon {
  cursor: pointer;
  color: var(--el-color-primary);
  font-size: 16px;
  transition: color 0.3s;
}

.copy-icon:hover {
  color: var(--el-color-primary-dark-2);
}
</style>

