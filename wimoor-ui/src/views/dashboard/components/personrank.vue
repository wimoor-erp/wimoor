<template>
  <el-col :span="6">
    <el-space style="margin-bottom:8px;">
      <span class="pag-title">销售业绩排名</span>
      <span class="pag-small-Extra">{{refreshtime}} </span>
    </el-space>
    <div class="pag-radius-bor " style='height:394px;'>
      <el-table :data="tableData.records" border style="width: 100%;">
        <el-table-column type="index" label="排名"  width="50" />
        <el-table-column prop="name" label="负责人"    show-overflow-tooltip/>
        <el-table-column prop="quantity" label="销量"   />
		<el-table-column prop="increase" label="环比涨幅" >
			 <template #default="scope" >
				  <span class='name'>{{scope.row.increase}}</span>
				   
                  <el-icon v-if="scope.row.isup" style='margin-left:4px;color:var(--el-color-success)'><caret-top /></el-icon>
				  <el-icon v-else style='margin-left:4px;color:var(--el-color-danger)'><caret-bottom /></el-icon>
			</template>
		</el-table-column>
      </el-table>
    </div>
  </el-col>
</template>
<script>
import {CaretBottom,CaretTop} from '@element-plus/icons-vue'
import { ref,reactive,onMounted,watch } from 'vue'
import summaryDataApi from '@/api/amazon/summary/summaryDataApi.js'
import {formatFloat} from '@/utils/index.js';
export default {
  name: "Personrank",
  components: {CaretBottom,CaretTop},
  setup() {
	  let pagesize =8;
	  let currentPage =1;
	  let tableData =reactive({records:[]});
	  let refreshtime=ref("");
	  onMounted(() => {
		    let param={"daytype":7,"pagesize":pagesize,"currentpage":currentPage};
	  		summaryDataApi.querySales(param).then((res)=>{
				if(res&&res.data&&res.data.records){
					res.data.records.forEach((item)=>{
						if(refreshtime.value==""){
							refreshtime.value= new Date(item.createdate).format("yyyy-MM-dd hh:mm:ss"); 
						}
						if(parseFloat(item.orderprice)-parseFloat(item.oldorderprice)>0){
					     	item.increase=formatFloat((parseFloat(item.orderprice)-parseFloat(item.oldorderprice))/parseFloat(item.oldorderprice)*100);
							item.isup=true;
						}else{
							item.increase=formatFloat((parseFloat(item.oldorderprice)-parseFloat(item.orderprice))/parseFloat(item.oldorderprice)*100);
							item.isup=false;
						}
					    
					})
					
				}
				tableData.records=res.data.records;
			})
	  		})
	  
    
    //返回数据
    return {
      tableData,refreshtime
    };
  }
};
</script>
<style>
.st-list {
  display: flex;
  padding: 7px 16px;
  border: 1px solid var(--el-border-color-base);
  margin: 8px 0;
  border-radius: var(--el-border-radius-base);
  align-items: center;
}


.st-list .st-label .title {
  font-size: 14px;
  color: var(--el-text-color-primary);
}
.st-list .pag-data-num {
  margin-left: auto;
  margin-bottom: 0;
}
</style>
