<template>
  <el-col :span="span">
    <el-space style="margin-bottom:8px;">
      <span class="pag-title">公告</span>
      <span class="pag-small-Extra"> </span>
    </el-space>
    <div class="pag-radius-bor " style='height:392px;'>
		<div class='mes-list' v-for='(m,index) in messageData.list' :key="index">
           <el-tag :type='m.state' size="small" class='mes-tag'>{{titleFormat(m.title)}}</el-tag>
		   <el-popover
		      placement="top-start"
		      :title="m.title"
		      :width="600"
			  :show-after="500"
		      trigger="hover"
		    >
			<div v-html="m.content"></div> 
		      <template #reference>
		       <div class='mes-text'>{{contentFormat(m.title)}}</div>
		       <div class='mes-time'>{{dateFormat(m.createdat)}}</div>
		      </template>
		    </el-popover>
		 
		</div>
    </div>
  </el-col>
</template>
<script>
import { ref,reactive,onMounted,watch } from 'vue'
import notifyApi from '@/api/sys/admin/notify.js'
export default {
  name: "Notice",
  components: {},
  props:["span"],
  setup() {
	  let messageData =reactive({list: []});
	  onMounted(()=>{
		   notifyApi.getMsgLimit().then((res)=>{
			   messageData.list=res.data;
		   })
	  });
	 function dateFormat(date){
		 return new Date(date).format("yyyy-MM-dd"); 
	 }
    function contentFormat(value){
		if(value.indexOf("】")){
			return value.substring(value.indexOf("】")+1,value.length);
		}else{
			return value;
		}
	}
	function titleFormat(value){
		if(value.indexOf("】")){
			return value.substring(1,value.indexOf("】"));
		}else{
			return "通知";
		}
	}
    //返回数据
    return {
      messageData,dateFormat,titleFormat,contentFormat
    };
  }
};
</script>
<style>
.mes-list{
  display: flex;
  margin: 16px 0;
  align-items: center;
  font-size:14px;
  color:var(--el-text-color-regular)
}
.mes-list .mes-tag{
	margin-right:8px;
}
.mes-list .mes-text{
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
	margin-right:24px;
	}
.mes-list .mes-time{
	color:var(--el-text-color-secondary);
	margin-left:auto;
	white-space: nowrap;
}	
</style>
