<template>
  <el-dialog
      v-model="visible"
      title="大附件上传"
      width="400"
      :before-close="handleClose"
  >
    <FileUpload ref="fileUpload" v-model="fileList"  :type="type" :fileType="fileType"></FileUpload>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleAdd" > 确认 </el-button>
      </div>
    </template>
  </el-dialog>

</template>
<script setup>
import {ref, reactive, onMounted, watch, toRefs} from 'vue';
import {UploadFilled} from '@element-plus/icons-vue';
import FileUpload from "@/components/FileUpload/upload.vue";
const props = defineProps({
  type: {
    type: String,
    default: 'notepad'
  },
  fileType: {
    type: Array,
    default: () => []
  }
});
const {type,fileType} = toRefs(props);
const emit = defineEmits(['change']);
// let props = defineProps({multiple:false });
// const { multiple} = toRefs(props);
const state = reactive({
  visible: false,
  fileList:[]
});
const {visible,fileList} = toRefs(state);

function show(files=[]) {
  state.visible = true;
  state.fileList=[];
  files.forEach(file=>{
    let item={}
    item.name=file.fileName;
    item.url=file.filePath;
    item.type=file.fileType;
    state.fileList.push(item)
  })
}
// 确认添加文件
function handleAdd() {
  // 校检文件是否为空
  if (fileList.value.length === 0) {
    proxy.$modal.msgError('请上传文件!')
    return
  }
  let files=[]
  fileList.value.forEach(item=>{
    let file={}
    file.fileName=item.name;
    file.filePath=item.url;
    file.fileType=item.type;
    files.push(file)
  })
  emit("change", files)
}
// 关闭弹窗
function hide() {
  state.visible = false;
  state.fileList = [];
}
defineExpose({show,hide})
</script>
<style scoped lang="scss">

</style>