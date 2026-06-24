<template> 
<el-dialog
      v-model="visible"
      title="模板上传下载"
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
            <el-input size="small" v-model="search" clearable placeholder="输入模版名称"></el-input>
          </template>
          <template #default="scope">
            <el-space>
              <el-link type="success" @click="downloadData(scope.row.id)">数据下载</el-link>
              <el-link style="margin-left:20px" type="info" :href="scope.row.filepath" target="_blank">模板下载</el-link>
              <el-link v-if="scope.row.shopid" style="margin-left: 10px;" @click="deleteTemplate(scope.row.id)" type="danger">删除</el-link>
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
  </template>
  <script  setup>
	import { ref,reactive,onMounted,watch ,toRefs,computed} from 'vue'
	import { ElMessage,ElMessageBox } from 'element-plus';
	import {CheckInputFloat,CheckInputInt,spaceCharInput} from '@/utils/index.js';
  import { uploadTemplateFile, downloadTemplateFile, getTemplateRecord, deleteTemplateFile } from '@/api/sys/tool/templateApi.js';
  import downloadTemplateFileApi from "@/api/erp/common/downloadTemplateFileApi.js";
  import shipmentCustomsApi from "@/api/erp/shipv2/shipmentCustomsApi.js";
  const emit = defineEmits(['download']);
  const uploadRef=ref();
let state =reactive({
  xmlVisible:false,
  uploadVisible:false,
  rateVisible:false,
  uploadloading:false,
  isEdit:true,
  uploadTitle:'导入模板',
  myfile:null,
  templatetitle:"",
  tableData:[],
  search:'',
  rateTableData:[],
  countryList:[],
  filterTableData:[],
  visible:false,
  ftype:null,
  rowdata:{},
});
let{ uploadVisible,uploadloading,uploadTitle,myfile,tableData,search,templatetitle,isEdit,
  rateVisible,rateTableData,countryList,filterTableData,visible,ftype,rowdata
}=toRefs(state);
	 


function showUploadTemplate(){
  state.uploadVisible=true;
  state.uploadloading = false;
}
function deleteTemplate(id){
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
        deleteTemplateFile({"uploadid":id}).then((res)=>{
          ElMessage.success('删除模板成功');
          loadData();
        });
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消操作',
        })
      })

}
function loadData(){
  getTemplateRecord().then((res)=>{
    state.filterTableData=res.data;
  })
}
function downloadTemp(id,name){
  downloadTemplateFileByType(id,state.ftype,name);
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
    FormDatas.append('ftype',state.ftype);
    uploadTemplateFile(FormDatas).then(function(res){
      ElMessage.success('上传模板成功');
      state.uploadloading=false;
      state.uploadVisible = false;
      loadData();
    })

  }
  function downloadTemplate(){
  let ftype="";
    if(state.ftype=="purchase"){
      ftype="PurchaseFormContractTemplate";
    }
    if(state.ftype=="customs"){
      ftype="ShipmentCustoms";
    }
    downloadTemplateFileApi.downExcelTemp({"ftype":ftype});
  }

function downloadData(id){
   emit('download',id,state.param);
}
function download(templateid,params){
  //访问后台ajax 获取Excel文件
  params.templateid=templateid;
  params.ftype=state.ftype;
  downloadTemplateFile(params);
}
function show(ftype,rowdata){
    state.visible=true;
    state.ftype=ftype;
    state.param=rowdata;
    loadData();
}
defineExpose({
  show,
  download,
})


</script>

<style scoped>
	 
</style>