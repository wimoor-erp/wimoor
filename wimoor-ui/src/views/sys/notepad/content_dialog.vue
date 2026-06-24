<template>
  <el-dialog
  v-model="visible"
  :title="item.title"
  width="60%"
  top="3vh"
  :before-close="handleClose">
  <div style="padding:20px">
   <MarkdownRenderer :content="item.content"  :showCodeOnly="true"></MarkdownRenderer>
  </div>
</el-dialog>
</template>
<script setup>
import {  onMounted,ref ,nextTick,reactive,toRefs,onUnmounted,watch} from 'vue';
import MarkdownRenderer from '@/views/sys/deepseek/components/MarkdownRenderer.vue';
import notepadApi from '@/api/sys/tool/notepadApi';
const state=reactive({
		visible:false,
    id: "",
    item:{},
	});
const {visible, id, item } = toRefs(state);
function show(id) {
    notepadApi.view({id:id}).then(res=>{
        state.item = res.data;
        state.visible = true;
    })
  }
defineExpose({
	  show,
	})
</script>
