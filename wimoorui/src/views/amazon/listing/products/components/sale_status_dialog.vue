<template>
	<div class="salestatusDailog">
	<el-dialog v-model="saleStatusVisable" title="销售状态管理" :destroy-on-close='true' width="600px"  >
	<h4 class="m-b-8">默认销售状态</h4>
	<el-space class="m-b-32">
		<el-tag  v-for="item in statusSysList" :type="item.color"    effect="dark">
			{{item.name}}
		</el-tag>
	  </el-space>
	 
	  <h4 class="m-b-8">其他状态</h4>
	  <el-row>
	  <el-space wrap>
	   <el-tag
	      v-for="item in statusList"
	      :key="item.id"
	      closable
		  :type="item.color"
		  effect="dark"
	      :disable-transitions="false"
	      @close="handleClose(item.id)"
	    >
		<el-dropdown @command="command" trigger="click">
		    <span class="font-s-w pointer" > {{ item.name }}</span>
		    <template #dropdown>
		      <el-dropdown-menu>
		        <el-dropdown-item   v-for="sub in colorList" :command="{tag:item.name,color:sub.value,id:item.id}">
					<div class="color-select">
					  <span >{{ sub.label }}</span>
					  <span :class="'cilcle-'+colorTran(sub.value)"></span  >
					</div> 
				</el-dropdown-item>
		      </el-dropdown-menu>
		    </template>
		  </el-dropdown>
	    </el-tag>
		
	    <el-input
	      v-if="inputVisible"
	      ref="InputRef"
	      v-model="inputValue"
	    />
		<el-button   v-if="inputVisible" type="primary" @click="submitStatus" >提交</el-button>
		<el-button   v-if="inputVisible" type="info" @click="handleInputCancel" >取消</el-button>
	    <el-button v-else type="info" plain size="small" @click="showInput">
	      + 添加状态
	    </el-button>
		</el-space>
		</el-row>
	  <template #footer>
	  	<span class="dialog-footer">
	  		<el-button @click="saleStatusVisable = false">关闭</el-button>
	  	</span>
	  </template>
	</el-dialog>
	</div>
</template>

<script>
	import {ref,reactive,onMounted,nextTick} from "vue";
	import productstatusaApi from '@/api/amazon/product/productstatusApi.js';
	import {ElMessage,ElMessageBox} from 'element-plus';
	export default{
		setup(){
			let saleStatusVisable =ref(false)
			let inputValue = ref('')
			let colorVal =ref("")
			let inputVisible = ref(false)
			let InputRef =ref()
			let statusList=ref([]);
			let statusSysList=ref([]);
			let colorList = [
				{label:"蓝色",value:""},
				{label:"红色",value:"danger"},
				{label:"绿色",value:"success"},
				{label:"黄色",value:"warning"},
				{label:"灰色",value:"info"},
				];
			let statuscolor=ref("blue");
			let remark=ref("");
			onMounted(()=>{
			   loadData();
			})
			function handleClose(id)  {
				ElMessageBox.confirm(
					'请确认是否删除？',
					{
					  confirmButtonText: '确认',
					  cancelButtonText: '取消',
					  type: 'warning',
					  callback:(action)=>{
						 if(action=="confirm"){
							  deleteItem(id);
						 }
					  }
					}
				  )
			}
			
			const showInput = () => {
			  inputVisible.value = true
			  nextTick(() => {
			    InputRef.value.input.focus()
			  })
			}
			
			const handleInputConfirm = () => {
			  if (inputValue.value) {
			    statusList.value.push({
					name:inputValue.value,
					color:"",
				})
			  }
			  inputVisible.value = false
			  inputValue.value = ''
			}
			function handleInputCancel(){
				inputVisible.value = false
				inputValue.value = ''
			}
			function command(obj){
				colorVal.value=obj.color;
				// if(obj.color=="blue"){
				// 	colorVal.value = ""
				// }else if(obj.color=="red"){
				// 	colorVal.value = "danger"
				// }else if(obj.color=="green"){
				// 	colorVal.value = "success"
				// }else if(obj.color=="yellow" || obj.color=="orange" || obj.color=="purple"){
				// 	colorVal.value = "warning"
				// }else if(obj.color=="gray" || obj.color=="black"){
				// 	colorVal.value = "info"
				// }else{
				// 	colorVal.value = "info";
				// }
				changeColor(obj);
			}
			function changeColor(obj){
				//修改颜色 更新颜色
				statusList.value.forEach((item)=>{
					if(item.name==obj.tag){
						item.color = colorVal.value
					}
				})
				updateItem(obj.id,obj.color);
			}
			function loadData(){
				productstatusaApi.getProStatusByShop().then((res)=>{
					var list1=[];
					var list2=[];
					res.data.forEach(function(item){
						if(item.issystem==true){
							list1.push(item);
						}else{
							list2.push(item);
						}
					});
					statusSysList.value=list1;
					statusList.value=list2;
				});
			}
			function deleteItem(id){
				productstatusaApi.deleteProductInfoStatus({"id":id}).then((res)=>{
					if(res.data.isOK=="true"){
						ElMessage.success('删除成功！');
						loadData();
					}else{
						ElMessage.error('操作失败');
					}
				});
			}
			function updateItem(id,color){
				//修改status
				productstatusaApi.updateProductInfoStatus({"id":id,"name":inputValue.value,"remark":remark.value,"color":color}).then((res)=>{
					if(res.data.isOK=="true"){
						ElMessage.success('更新成功！');
					}else{
						ElMessage.error('操作失败');
					}
				});
			}
			function submitStatus(){
				//handleInputConfirm();
				//新增status
				productstatusaApi.updateProductInfoStatus({"id":'',"name":inputValue.value,"remark":remark.value,"color":'blue'}).then((res)=>{
					if(res.data.isOK=="true"){
						ElMessage.success('新增成功！');
						loadData();
						handleInputCancel();
					}else{
						ElMessage.error('操作失败');
					}
					
				});
			}
			function colorTran(color){
				    if(color==""){
						color = "blue"
					}else if(color=="danger"){
						color = "red"
					}else if(color=="success"){
						color = "green"
					}else if(color=="warning"){
						color = "yellow"
					}else if(color=="info"){
						color = "gray"
					}else{
						color = "gray";
					}
					return color;
			}
			return{
				//value
				saleStatusVisable,inputValue,colorList,inputVisible,
				colorVal,statusList,statusSysList,statuscolor,remark,
				//ref
				InputRef,
				//function
				submitStatus,loadData,changeColor,deleteItem,updateItem,command,
				handleClose,showInput,handleInputConfirm,handleInputCancel,colorTran,
			}
		}	
	}
</script>

<style>
	.m-b-8{
		margin-bottom: 8px;
	}
	.m-b-32{
		margin-bottom:32px;
	}
	.font-s-w{
		font-size: 12px;
		color: #fff;
	}
	.cilcle-red{
		width:12px;
		height:12px;
		background-color:#f56c6c;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-yellow{
		width:12px;
		height:12px;
		background-color:#e6a23c;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-green{
		width:12px;
		height:12px;
		background-color:#67c23a;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-blue{
		width:12px;
		height:12px;
		background-color:#00aaff;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-gray{
		width:12px;
		height:12px;
		background-color:#606266;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-black{
		width:12px;
		height:12px;
		background-color:#000000;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	.cilcle-orange{
		width:12px;
		height:12px;
		background-color:#ffa400;
		display: inline-block;
		border-radius: 8px;
		margin-left: 8px;
	}
	
	.color-select{
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
</style>
