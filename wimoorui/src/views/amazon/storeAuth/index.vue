<template>
  <div class="main-sty">
    <el-row gutter="16">
      <el-col :span="6">
        <el-card shadow="never" >
          <el-scrollbar style="height: calc(100vh - 112px);">
            <div class="el-card-head">
              <div><h3>店铺账号列表</h3>
                <p class="font-extraSmall">用于区分店铺的名称</p></div>
              <div>
                <div class="text-right" style="padding-right:20px">
                  <el-space>
                    <el-link type="primary" class="flex-center"  @click="addStorename" :underline="false">
                      <span>新增</span>
                      <plus class="ic-cen" theme="outline" size="16" :strokeWidth="5"/>
                    </el-link>
                    <el-link type="info" linked @click="dialogRank.visible=true" :underline="false">
                      <span>排序</span>
                      <el-icon :size="16" ><Sort /></el-icon>
                    </el-link>
                  </el-space>
                </div>
              </div>
            </div>
            <div class="el-card-body">
              <el-card shadow="hover" v-if="storeList.list.length>0" v-for="item in storeList.list">
                <div class="flex-center-between">
                  <div><h4>{{item.name}}</h4>
                    <p class="font-extraSmall m-t-8">创建于：{{dateFormat(item.createtime)}}</p>
                  </div>
                  <el-space>
                    <el-link @click="updataStorename(item)" type="info" :underline="false">
                      <el-icon  class="font-medium"> <Edit /> </el-icon>
                    </el-link>
                    <el-link @click="delectStore(item)" type="info" :underline="false">
                      <el-icon  class="font-medium">
                        <Delete/>
                      </el-icon>
                    </el-link>
                  </el-space>
                </div>
              </el-card>
              <el-empty
                  v-else
                  :image-size="119"
                  :image="$require('store.png')" description="请添加要授权的店铺!" />
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="18">
        <Table :storelist="storeList" />
      </el-col>
    </el-row>
  </div>
  <el-dialog
      v-model="dialogVisible"
      :title=edittitle
      width="30%"
      @close ="closedialog"
  >
    <el-form label-width="auto">
      <el-form-item label="店铺名称" >
        <el-input v-model="form.name" placeholder="请输入店铺在系统上的名称" class="input-with-select" />
      </el-form-item>
      <el-form-item label="是否财务账套" >
        <el-switch
            v-model="form.isfinance"
            inline-prompt
            v-if="form.id"
            active-text="是"
            inactive-text="否"
        />
        <span v-else class="text-red">请先保存店铺信息，再初始化财务账套！</span>
      </el-form-item>
      <el-form-item label="公司名称" v-if="form.isfinance">
        <el-input v-model="form.company" placeholder="请输入店铺对应公司主体名称" class="input-with-select" />
      </el-form-item>
      <el-form-item label="税号" v-if="form.isfinance">
        <el-input v-model="form.taxNumber" placeholder="请输入店铺对应公司主体税号" class="input-with-select" />
      </el-form-item>
    </el-form>

    <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="dialogVisible = false">取消</el-button>
	        <el-button type="primary" @click="saveStore" :loading="formloading"  >确认</el-button >
	      </span>
    </template>
  </el-dialog>
  <el-dialog
      v-model="dialogRank.visible"
      title="显示次序设置"
      width="400px"
  >
    <div class="rank-list-title">
      <span>店铺名称</span>
      <span>显示次序</span>
    </div>
    <draggable
        class="draggable"
        :list="list"
        animation="300"
        @start="dragStart"
        @end="dragEnd"
    >
      <template #item="{ element }">
        <div class="item" >
          <el-card shadow="hover">
            <el-space class="list-title">
              <drag class="ic-cen" theme="outline" size="16" fill="#9a9a9a" :strokeWidth="2"/>
              <span>{{element.name}}
	 	 			</span>
            </el-space>
            <div class="font-extraSmall" style="float:right">{{element.findex}}</div>
          </el-card>
        </div>
      </template>
    </draggable>
    <template #footer>
      <el-button @click="dialogRank.visible=false">取消</el-button>
      <el-button type="primary" @click="submitFormIndex">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import Table from"./components/table.vue"
import {ref,reactive,onMounted,toRefs} from "vue"
import {Edit,Delete,Sort} from '@element-plus/icons-vue';
import {Plus} from '@icon-park/vue-next';
import {dateFormat} from '@/utils/index.js';
import { ElDivider,ElMessageBox ,ElMessage } from 'element-plus';
import draggable from "vuedraggable";
import groupApi from '@/api/amazon/group/groupApi.js';
import {initFinAccountingSubjects} from "@/api/finance/subjects"
// 响应式数据
const dialogVisible = ref(false);
const edittitle = ref("添加店铺");
const storeList = reactive({list:[]});
const dialogRank = reactive({visible:false});
const state = reactive({list:[],form:{},formloading:false});
const {list,form,formloading} = toRefs(state);



// 生命周期
onMounted(() => {
  loadStoreList();
});

// 方法
const addStorename = () => {
  dialogVisible.value = true;
  state.form={};
  edittitle.value = "添加店铺";
};

const dragEnd = () => {
  state.list.forEach((item,index) => {
    item.findex = index + 1;
  });
};

const saveStore = () => {

  if(!state.form.name){
    ElMessage.error('店铺名称不能为空！');
    return;
  }
  if(state.form.isfinance){
    if(!state.form.company){
      ElMessage.error('公司名称不能为空！');
      return;
    }
    if(!state.form.taxNumber){
      ElMessage.error('税号不能为空！');
      return;
    }
    state.formloading=true;
    initFinAccountingSubjects(state.form.id).then((res) => {
        ElMessage.success('初始化财务账套成功！');
        groupApi.AmazonGroupSave(state.form).then((res) => {
            ElMessage.success('提交成功！');
            dialogVisible.value = false;
            state.formloading=false;
            loadStoreList();
        }).catch(e=>{state.formloading=false;});
    }).catch(e=>{state.formloading=false;});
  }else{
    groupApi.AmazonGroupSave(state.form).then((res) => {
      ElMessage.success('提交成功！');
      dialogVisible.value = false;
      state.formloading=false;
      loadStoreList();
    }).catch(e=>{state.formloading=false;});
  }

};

const loadStoreList = () => {
  groupApi.getAmazongroupList().then((res) => {
    storeList.list = res.data;
    state.list = JSON.parse(JSON.stringify(res.data));
  });
};

const submitFormIndex = () => {
  groupApi.updateBatch(state.list).then(res => {
    ElMessage.success('排序成功');
    state.dialogRank.visible = false;
    loadStoreList();
  });
};

const updataStorename = (item) => {
  groupApi.getAmazonGroupById(item.id).then(res=>{
    state.form=res.data;
    edittitle.value = "编辑店铺";
    dialogVisible.value = true;
  })
};

const closedialog = () => {
  state.form ={};
};

const delectStore = (item) => {
  let id = item.id;
  ElMessageBox.confirm(
      '你确定要删除该店铺吗?',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        callback:(action) => {
          if(action == "confirm"){
            groupApi.deleteAmazongroup(id).then(() => {
              ElMessage.success('删除成功');
              loadStoreList();
            });
          }
        }
      }
  );
};
</script>

<style scoped="scoped">
.el-card-head{
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.el-card-body .el-card__body{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding:16px 24px;
}
.el-card-body .el-card{
  margin-bottom:8px;
}

.draggable .item{
  margin-bottom:5px;
}
.m-b-16{
  margin-bottom:16px;
}

.m-b-16 h5{
  line-height: 32px;
}
.m-b-16 .el-input{
  margin-right:8px;
}
.el-menu{
  border-right:none;
}
.more-icon{
  transform:rotate(90deg);
  font-size:14px!important;
  margin-right: -8px!important;
  opacity: 0;
}
.el-menu-item{
  display: flex;
  justify-content: space-between;
}
.el-menu-item.is-active{
  background-color: var(--el-color-info-light-9);
}
.el-menu-item .isvisable{
  opacity: 1;
}
.el-menu-item:hover .more-icon{
  opacity: 1;
}
.rank-list-title{
  display: flex;
  justify-content: space-between;
  margin:0 16px;
  font-size: 12px;
  color: #999;
}
</style>