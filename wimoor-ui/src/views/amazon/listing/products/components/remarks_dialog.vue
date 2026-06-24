	<template>
		<el-dialog v-model="remarksVisable" title="产品备注" :destroy-on-close='true' width="800px"  >
				<el-row :gutter="32">
					<el-col :span="12" class="border-right">
						<el-input v-model="dataRow.remark" placeholder="输入备注" :rows="8"  type="textarea"  />
						<el-button class="m-t-8" type="primary" @click.stop="submitRemark" >提交</el-button>
					</el-col>
					<el-col :span="12">
						<el-scrollbar class="remark-history">
						<h4>备注日志</h4>
						 <el-timeline>
						    <el-timeline-item
						      v-for="(activity, index) in remarkHis"
						      :key="index"
						      :timestamp="activity.timestamp"
							  :hollow="true"
							   type='success'
						    >
						      {{ activity.content }}
						    </el-timeline-item>
						  </el-timeline>
						  </el-scrollbar>
					</el-col>
				</el-row>
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="remarksVisable = false"> 关闭</el-button>
		  	</span>
		  </template>
		</el-dialog>
	</template>
	
	<script>
		import {ref,reactive,onMounted} from "vue";
		import productinoptApi from '@/api/amazon/product/productinoptApi.js';
		import productinfoApi from '@/api/amazon/product/productinfoApi.js';
		import {ElMessage,ElDivider} from 'element-plus';
		import {decodeText} from '@/utils/index.js';
		export default{
			emits:["change"],
			setup(props,context){
				let remarksVisable =ref(false);
				let dataRow=ref({});
				let remarkHis = ref([]);
				function submitRemark(){
					//更新备注
					productinfoApi.updateRemark({"id":dataRow.value.id,"remark":dataRow.value.remark,"ftype":"pro"}).then((res)=>{
						if(res.data && res.data.message=="success"){
							ElMessage.success('操作成功！')
							dataRow.value.htmlremark=decodeText(dataRow.value.remark);
							remarksVisable.value = false;
							//loadData(dataRow.value);
						}else{
							ElMessage.error( "操作失败！")
						}
					});
				}
				function loadData(row){
					remarksVisable.value=true;
					dataRow.value=row;
					productinoptApi.getProRemarkHis({"pid":row.id,"ftype":"pro"}).then((res)=>{
						var arrs=[];
						if(res.data && res.data.length>0){
							res.data.forEach(function(item){
								var row={};
								row.content=item.remark;
								row.timestamp=item.opttime+" | 操作人："+item.optname;
								arrs.push(row);
							});
							remarkHis.value=arrs;
						}
						
					});
				}
				return{
					remarksVisable,remarkHis,submitRemark,dataRow,loadData
				}
			}	
		}
	</script>
	
	<style>
		.border-right{
			border-right:1px solid #e4e7ed;
		}
		.remark-history h4{
			margin-bottom:16px;
		}
		.remark-history {
			height: calc(60vh);
		}
		.el-timeline-item__node--normal{
			width: 10px;
			height:10px;
			left: 0px;
		}
		.el-timeline-item__timestamp{
			color: #bcbec3;
		}
	</style>
	
