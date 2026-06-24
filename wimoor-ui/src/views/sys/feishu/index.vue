<template>
  <div style="height:calc(100vh - 40px)">
    <el-row style="padding-top:20px; height: 100%;">
      <el-col :span="10" style="padding: 0 10px; height: 100%;">
        <div style="display: flex; justify-content: center; align-items: flex-start; height: 100%;">
          <el-col :span="18">
            <el-card  >
              <template #header>
                <div class="card-header">
                  <span>飞书绑定</span>
                </div>
              </template>
              <el-form v-if="edit" :model="form" label-width="auto"  style="width: 100%;" >
                <el-form-item label="飞书应用ID"   >
                  <el-input placeholder="App ID" v-model="form.appId" ></el-input>
                </el-form-item>
                <el-form-item label="飞书应用密钥"   >
                  <el-input placeholder="App Secret"  v-model="form.appSecret" ></el-input>
                </el-form-item>
                <el-form-item label="加密密钥"   >
                  <el-input placeholder="Encrypt Key"  v-model="form.encryptKey" ></el-input>
                </el-form-item>
                <el-form-item label="验证令牌"   >
                  <el-input placeholder="Verification Token"  v-model="form.verificationToken" ></el-input>
                </el-form-item>
                <el-form-item label="API文档连接">
                  <el-input placeholder="API链接" disabled v-model="url" ></el-input>
                </el-form-item>
              </el-form>
              <el-form v-else :model="form" label-width="auto"  style="width: 100%;" >
                <el-form-item label="飞书应用ID"   >
                  {{form.appId}}
                </el-form-item>
                <el-form-item label="飞书应用密钥"   >
                  {{form.appSecret}}
                </el-form-item>
                <el-form-item label="加密密钥"   >
                  {{form.encryptKey}}
                </el-form-item>
                <el-form-item label="验证令牌"   >
                  {{form.verificationToken}}
                </el-form-item>
                <el-form-item label="API文档连接">
                  {{url}}
                </el-form-item>
              </el-form>
              <template #footer>
                <div class="flex-between">
                  <div> </div>
                  <div>
                    <el-button v-if="edit"  type="primary" @click="addApi">
                      保存
                    </el-button>
                    <el-button v-else type="primary" @click="edit=true">
                      修改
                    </el-button>
                  </div>
                </div>
                <el-card header="网页应用配置" v-if="desktopUrl" shadow="never" class="font-extraSmall" style="margin-top:20px;white-space: pre-wrap;">
                  <div style="padding-bottom: 20px;word-wrap: break-word;overflow-wrap: break-word;">
                    桌面端主页:<br>{{desktopUrl}}
                  </div>
                  <div style="padding-bottom: 20px;word-wrap: break-word;overflow-wrap: break-word;">
                    移动端主页:<br>{{mobileUrl}}
                  </div>

                </el-card>

              </template>

            </el-card>
          </el-col>
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script setup>
import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
import { ElMessage ,ElMessageBox} from 'element-plus';
import feishuApi from '@/api/sys/tool/feishuApi.js';
import TableData from '../../finance/periods/components/template/feishu_table_bind.vue';
let state=reactive({
  downLoading:false,
  tableData:{records:[],total:0},
  edit:false,
  url:"https://open.feishu.cn/?lang=zh-CN",
  form:{
    "appId":null,
    "appSecret":null,
    "encryptKey":null,
    "verificationToken":null,
  },
  desktopUrl:"",
  mobileUrl:"",
});
let {
  tableData,
  downLoading,
  edit,
  form,
  url,
  desktopUrl,
  mobileUrl
} =toRefs(state);
function createDocx(){
  feishuApi.createAppAndTables({title:"test"}).then((res)=>{
    ElMessage.success("创建成功");
  });
}


function addApi(){
  if(state.form.appId){
    feishuApi.update(state.form).then((res)=>{
      ElMessage.success("更新成功");
      showBindInfo();
    });
  }else{
    feishuApi.save(state.form).then((res)=>{
      ElMessage.success("添加成功");
      showBindInfo();
    });
  }

}

function showBindInfo(){
  feishuApi.getBindInfo().then((res)=>{
    if(res.data){
      state.edit=false;
      state.form=res.data;
      state.desktopUrl="https://open.feishu.cn/open-apis/authen/v1/authorize?app_id="
          +state.form.appId+"&redirect_uri=https%3A%2F%2Fapp.wimoor.com%2Fssologin&scope=offline_access&state=feishu";
      state.mobileUrl="https://open.feishu.cn/open-apis/authen/v1/authorize?app_id="
          +state.form.appId+"&redirect_uri=https%3A%2F%2Fh5.wimoor.com%2Fpages%2Fsys%2Flogin%2Findex%3Fscope%3Doffline_access%26state%3Dfeishu";
    }else{
      state.edit=true;
    }
  });
}


onMounted(()=>{
  showBindInfo();
})

</script>

<style>
</style>
