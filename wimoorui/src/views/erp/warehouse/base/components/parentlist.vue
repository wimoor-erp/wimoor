 <template>
    <!-- 搜索表单 -->
		  <div class="flex-center-between m-b-16">
			  <h5>仓库</h5>
			  <div class="flex-center">
				  <el-input
				    v-if="SearchShow==false"
				    v-model="queryParams.search"
				    placeholder="仓库名称"
				    clearable
					@clear = "SearchShow=true"
				    @input="handleQuery"
				  />
				  <el-button @click="handleAdd" link class="im-but-one " >
				   <el-icon class="font-base"><Plus /></el-icon>
				    <span>添加</span>
				  </el-button>
				  <el-dropdown v-if="SearchShow">
					  <el-button  link class="im-but-one " >
						  <el-icon class="font-base"><MoreFilled /></el-icon>
					    <span>更多</span>
					  </el-button>
				      <template #dropdown>
				        <el-dropdown-menu>
				          <el-dropdown-item v-if="SearchShow" clear  @click="SearchShow=false">搜索</el-dropdown-item>
				          <el-dropdown-item @click="dailogRankShow">排序</el-dropdown-item>
						  <el-dropdown-item v-if="queryParams.showhide!='true'" @click="showHide"><el-icon><Hide /></el-icon>已删除</el-dropdown-item>
						   <el-dropdown-item v-else @click="showNormal"><el-icon><View /></el-icon>未删除</el-dropdown-item>
				        </el-dropdown-menu>
				      </template>
				    </el-dropdown>
			  </div>
		  </div>
    <!-- 数据表格 -->
		<el-scrollbar :style="ftype!='oversea'?'height: calc(100vh - 120px);':'height: calc(100vh - 180px);'">
	  <el-menu
	        :default-active="defaultActive"
	         class="m-t-8"
	      >
	        <el-menu-item  v-for="(item,index) in list" :index="item.findex"   @click="handleRowClick(item.id)" >
	          <span>{{item.name}}<el-icon class="text-green bg-green" v-if="item.isbind" style="font-size:14px;color:#67C23A" title="已绑定"><InfoFilled /></el-icon></span>
			   <el-dropdown class="text-right" @visible-change="visibleChange(item)">
			      <el-icon class="more-icon" :class="{'isvisable':item.isvisable}"><MoreFilled /></el-icon>
			      <template #dropdown>
			        <el-dropdown-menu>
						<el-dropdown-item v-if="item.disabled==true"  @click.stop="handleRecover(item)">恢复</el-dropdown-item>
						<el-dropdown-item v-if="item.disabled!=true" @click.stop="handleUpdate(item)">编辑</el-dropdown-item>
			          <el-dropdown-item v-if="item.disabled!=true" @click.stop="handleDelete(item)">删除</el-dropdown-item>
			        </el-dropdown-menu>
			      </template>
			    </el-dropdown>
	        </el-menu-item>
	      </el-menu>
 </el-scrollbar>

    <!-- 弹窗表单 -->
    <el-dialog
      :title="dialog.title"
      v-model="dialog.visible"
      width="500px"
      @close="cancel"
    >
      <el-form
        ref="dataFormRef"
        :model="formData"
        :rules="rules"
        label-width="95px"
      >
        <el-form-item label="仓库名称" prop="name">
          <el-input v-model="formData.name" placeholder="仓库名称" />
        </el-form-item>
		<el-form-item label="次序" prop="findex">
		  <el-input v-model="formData.findex" placeholder="仓库次序" />
		</el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input  v-model="formData.remark" type="textarea"  placeholder="请输入内容" :autosize="{ minRows: 2, maxRows: 4 }" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
		  <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

<el-dialog
	   v-model="dialogRank.visible"
	   title="显示次序设置"
	   width="400px"
	 >
	 <div class="rank-list-title">
	 	<span>仓库名称</span>
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
	 				<span>{{element.findex}}</span>
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

<script  setup>
	//需要安装依赖 npm i -S vuedraggable@next
	//使用文档地址https://www.itxst.com/vue-draggable-next/tutorial.html
import draggable from "vuedraggable";
import { onMounted, reactive, ref, toRefs,} from 'vue';
import { Plus,Search, Edit, Refresh, Delete,MoreFilled,InfoFilled,View,Hide } from '@element-plus/icons-vue';
import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
import warehouseApi from "@/api/erp/warehouse/warehouseApi"
	const queryFormRef = ref(ElForm);
	const dataFormRef = ref(ElForm);
	const emit = defineEmits(['selectedItem']);
	let props = defineProps({  ftype:"" })
	const state = reactive({
	  loading: true,
	  // 选中ID数组
	  ids: "",
	  // 非单个禁用
	  single: true,
	  // 非多个禁用
	  multiple: true,
	  queryParams: {
		search:'',
		ftype:"self",
		pagesize:10000,
		currentpage:1,
	  } ,
	  SearchShow:true,
	  list: [],
	  total: 0,
	  defaultActive:1,
	  dialog: { visible: false } ,
	  dialogRank:{visiable:false},
	  formData: {
		status: 1,
	  } ,
	  rules: {
		name: [{ required: true, message: '请输入标签组名称', trigger: 'blur' }],
	  },
	});

	const { total, dialog, loading,defaultActive, list, formData, rules, queryParams,SearchShow,dialogRank} =
	  toRefs(state);
	
	function handleQuery() {
	  state.loading = true;
	  state.queryParams.ftype=props.ftype;
	  warehouseApi.getWarehouseListPage(state.queryParams).then(res=>{
		  var listindex=[];
		  res.data.records.forEach((row,index)=>{
			  if(!row.findex){
				  row.findex=index;
			  }
			  while(listindex.includes(row.findex)){
			  					  row.findex=row.findex+1;
			  }
			  listindex.push(row.findex);
			  if(index==0){
				  state.defaultActive=row.findex;
			  }
		  })
		  state.list = res.data.records;
		  state.total = res.data.total;
		  state.loading = false;
		  if(state.list&&state.list.length>0){
			  handleRowClick(state.list[0].id);
		  }
	  })
	}

	function handleAdd() {
	  state.dialog = {
		title: '仓库添加',
		visible: true,
	  };
	}

	function handleUpdate(item) {
	  state.dialog = {
		title: '编辑仓库',
		visible: true,
	  };
	  const id = item.id || state.ids;
	  warehouseApi.detail(id).then(({data})=>{
		  state.formData=data;
	  });
	}

	function submitForm() {
	  dataFormRef.value.validate((isValid) => {
		if (isValid) {
			state.formData.ftype=props.ftype;
		  if (state.formData.id) {
			warehouseApi.updateData(state.formData.id, state.formData).then(() => {
			  ElMessage.success('修改成功');
			  cancel();
			  handleQuery();
			});
		  } else {
			warehouseApi.saveData(state.formData).then(() => {
			  ElMessage.success('新增成功');
			  cancel();
			  handleQuery();
			});
		  }
		}
	  });
	}

	function cancel() {
	  state.formData.id = undefined;
	  dataFormRef.value.resetFields();
	  state.dialog.visible = false;
	}

   function checkWarehouse(ids){
	   
   }

	function handleDelete(item) {
	  const ids = [item.id || state.ids].join(',');
	  
	  warehouseApi.checkDeleteWarehouse(ids).then((res)=>{
	  	if(res.data){
	  		ElMessageBox.confirm(res.data, '警告', {
	  				confirmButtonText: '继续删除',
	  				cancelButtonText: '取消',
	  				type: 'warning',
	  		})
	  				.then(() => {
	  				  warehouseApi.deleteInfo(ids).then(() => {
	  					ElMessage.success('删除成功');
	  					handleQuery();
	  				  });
	  				})
	  				.catch(() => ElMessage.info('已取消删除'));
	  	}else{
			warehouseApi.deleteInfo(ids).then(() => {
					ElMessage.success('删除成功');
					handleQuery();
			});
		}
	  });
	  
	}

	function handleRowClick(id) {
	    emit('selectedItem', id);
	}
	
	function showHide(){
		state.queryParams.showhide='true';
		handleQuery();
	}
	function showNormal(){
		state.queryParams.showhide=null;
		handleQuery();
	}
	function visibleChange(item){
		item.isvisable = !item.isvisable
	}
	function dailogRankShow(){
		state.dialogRank.visible = true
	}
	function dragEnd(){
		state.list.forEach((item,index)=>{
			item.findex = index+1
		})
	}
	function submitFormIndex(){
		warehouseApi.updateIndex(state.list).then(res=>{
			ElMessage.success('排序成功');
			state.dialogRank.visible=false
			handleQuery();
		});
	}
	function handleRecover(row){
		warehouseApi.detail(row.id).then(({data})=>{
			if(data){
				data.disabled=false;
				warehouseApi.updateData(row.id,data).then((res)=>{
					  ElMessage.success('操作成功');
					  handleQuery();
				});
			}else{
				 ElMessage.error('未查到此仓库!');
			}
		});
		
	}
	onMounted(() => {
	  handleQuery();
	});


</script>
<style scoped="scoped">
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
</style>