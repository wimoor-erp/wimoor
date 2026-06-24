<template>
 <el-col :span='12'>
		  <el-space style='margin-bottom:8px;'>
		    <span class='pag-title'>商品销量排名</span><span class='pag-small-Extra'>{{chartTitle}}</span>
		  </el-space>
		  <div class='pag-radius-bor'  style="min-height: 392px;">
       <el-table :data="tableData.records" border style="width: 100%;">
			<el-table-column  type="index" label="排名"  width="50" />
			 <el-table-column prop="image" label="图片" width="60" >
			 <template #default="scope">
			  <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
			</template>
		    </el-table-column>
			 <el-table-column  label="sku信息"  show-overflow-tooltip>
				  <template #default="scope" >
					  <div class='name'>{{scope.row.name}}</div>
					  <span class="sku">{{scope.row.sku}} </span>
					  <span class="groupname"> ({{scope.row.groupname}}/{{scope.row.marketname}}) </span>
				  </template>
		    </el-table-column>
			<el-table-column prop="catename" label="品类"  width="100" />
			<el-table-column prop="quantity" label="销量" width='80'/>
			<el-table-column prop="orderprice" label="销售额(站点币种)"  width="120" />
			<el-table-column prop="fulfillable" label="FBA可售" width='80'/>
      </el-table>
		  </div>
	 </el-col>
</template>
<script>
import { ref,reactive,onMounted,watch } from 'vue'
import summaryDataApi from '@/api/amazon/summary/summaryDataApi.js'
export default {
  name: "Goodsrank",
  components: {},
  props:["parameter"],
  setup(prop,context){
    //返回数据
	let chartTitle =ref();
	let tableData =reactive({records:[]}) ;
	watch(prop.parameter,(val)=>{
		  chartTitle.value=prop.parameter.beginDate+ " ~ "+prop.parameter.endDate.substring(0,10);
            summaryDataApi.top5(prop.parameter).then((res)=>{
				tableData.records=res.data;
			});
    });
  
    //返回数据
    return {
      tableData,
	  chartTitle
    };
  }
};
</script>
<style>
	.groupname{
		padding-left:4px;
		color:#c1c1c1;
	}
	
</style>