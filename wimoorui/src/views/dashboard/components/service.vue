<template>
  <el-col :span="span" >
   <div style="color:#000">
    <el-space style="margin-bottom:8px;">
      <span class="pag-title" style="color:#303133">开发支持</span>
      <span class="pag-small-Extra"> </span>
    </el-space>
    <div class="pag-radius-bor text-sty setbgS" style='margin-bottom:16px;' >
    <div>
		<h4 style="color:#303133">全面开源的跨境电商系统</h4>
		<div class='subtext'>Wimoor完全免费</div>
    </div>
    </div>
	<div class="pag-radius-bor text-sty setbgC"  >
      <div>
		<h4 style="color:#303133">软件定制服务</h4>
		<div class='subtext'>企业数字化专家</div>
        <el-button color="#1e61ff" @click="open" >联系我们</el-button>
     </div>
    </div>
	</div>
  </el-col>
</template>
<script>
import { ref } from 'vue'
import {CaretBottom,} from '@element-plus/icons-vue'
import {  ElMessageBox } from 'element-plus';
import { useDark, useToggle } from "@vueuse/core";
export default {
  name: "Service",
  props:["span"],
  components: {CaretBottom},  
  setup() {
	  const isDark=useDark();
  function open(){
       ElMessageBox.alert('135 5483 3402', '联系电话', {
         confirmButtonText: '确定',
       })
    }
    return {
		open,isDark
    };
  }
};
</script>
<style>
.text-sty{display: flex;align-items: center;height:188px;}
.text-sty h4{font-weight:600;color:var(--el-text-color-primary);margin-bottom:8px;font-size: 16px;}
.text-sty .subtext{font-size:14px;color:var(--el-text-color-secondary);}
.text-sty button {margin-top:16px;}
</style>
<style scope>
 .setbgS{
	 background:url(@/assets/image/ser-source.jpg) no-repeat center/cover;
 }
 .dark .setbgS{
 	 background:url(@/assets/image/ser-source.jpg) no-repeat center/cover;
	 filter: brightness(0.8);
 }
 .setbgC{
 	 background:url(@/assets/image/company-service.png) no-repeat center/cover;
 }
 .dark .setbgC{
	 background:url(@/assets/image/company-service.png) no-repeat center/cover;
	 filter: brightness(0.8);
 }
</style>
