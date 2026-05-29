 <template>
    <!-- 搜索表单 -->
		  <div class="flex-center-between m-b-16">
			   <el-select size="small" 
			              v-model="queryParams.groupid"  
						  :size="size"   
						  placeholder="全部店铺" 
						  @change="handleQuery">
			         <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
			         </el-option>
			   </el-select>
			   <el-select size="small" 
			              v-model="queryParams.profileid" 
						  placeholder="全部站点" 
			              style="margin-left: 4px;" 
						  @change="handleQuery">
				   <el-option v-for="item in profileList" :key="item.id" :label="item.name" :value="item.id"></el-option>
			   </el-select>
		  </div>
		  <div class="flex-center-between m-b-16">
			  <div class="flex-center">
			  				  <el-button @click="handleAdd" link class="im-but-one " >
			  				   <el-icon class="font-base"><Plus /></el-icon>
			  				    <span>添加</span>
			  				   </el-button>
							  <el-input
							    v-if="SearchShow==false"
							    v-model="queryParams.search"
							    placeholder="Portfolio"
							    clearable
								size="small"
							    @clear = "SearchShow=true"
							    @input="handleQuery"
							  />
			  				  <el-button v-if="SearchShow" 
							             size="small"  
							             link 
										 class="im-but-one "  
							             @click="SearchShow=false" >
									  <el-icon  class="font-base"><Search /></el-icon>
			  					      <span>搜索</span>
			  				  </el-button>
			  </div>
		  </div>
    <!-- 数据表格 -->
	<el-scrollbar style="height: calc(100vh - 160px);">
	    <el-menu :default-active="0"   class="m-t-8" >
	        <el-menu-item  v-for="(item,index) in list" :index="index"   @click="handleRowClick(item.id)" >
				  <el-tooltip placement="top" v-if="item.name.length>14">
				    <template #content>{{item.name}} </template>
				    <div style="width:220px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;">
					    {{item.name}}
					</div>
				  </el-tooltip>
				  <div v-else>
					  {{item.name}}
				  </div>
			   <el-dropdown v-if="item.id" class="text-right" @visible-change="visibleChange(item)">
			      <el-icon class="more-icon" :class="{'isvisable':item.isvisable}"><MoreFilled /></el-icon>
			      <template #dropdown>
			        <el-dropdown-menu>
						<el-dropdown-item  @click.stop="handleUpdate(item)">编辑</el-dropdown-item>
			          <el-dropdown-item  @click.stop="handleDelete(item)">删除</el-dropdown-item>
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
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
</template>

<script  setup>
	//需要安装依赖 npm i -S vuedraggable@next
	//使用文档地址https://www.itxst.com/vue-draggable-next/tutorial.html
import draggable from "vuedraggable";
import { onMounted, reactive, ref, toRefs,} from 'vue';
import { Plus,Search, Edit, Refresh, Delete,MoreFilled } from '@element-plus/icons-vue';
import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
import authApi from "@/api/amazon/advertisement/auth/authApi.js"
import portfoliosApi from "@/api/amazon/advertisement/portfolios/portfoliosApi.js"
	const queryFormRef = ref(ElForm);
	const dataFormRef = ref(ElForm);
	const emit = defineEmits(['change']);
	let props = defineProps({  ftype:"" })
	let state = reactive({
	  loading: true,
	  // 选中ID数组
	  ids: "",
	  // 非单个禁用
	  single: true,
	  groupList:[],
	  // 非多个禁用
	  multiple: true,
	  profileList:[],
	  queryParams: {
		search:'',
		groupid:"",
		profileid:"",
		portfolios:""
	  } ,
	  SearchShow:true,
	  list: [],
	  total: 0,
	  dialog: { visible: false } ,
	  formData: {
		status: 1,
	  } ,
	});

	const { total, dialog, groupList,profileList, loading, list, formData, queryParams,SearchShow} =
	  toRefs(state);
	
	function handleQuery() {
	   state.loading = true;
	   state.queryParams.ftype=props.ftype;
	   portfoliosApi.findPortfolios({"groupid":state.queryParams.groupid,
	                                 "profileid":state.queryParams.profileid}).then(res=>{
			if(res.data){
				state.list=[];
				state.list.push({"id":"","name":"全部",isvisable:true});
				res.data.forEach(item=>{
					item.isvisable=false;
					state.list.push(item);
				})
				state.queryParams.portfolios="";
				emit("change",state.queryParams);
			}else{
				state.list=[];
				state.queryParams.portfolios="";
				emit("change",state.queryParams);
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

	function handleDelete(item) {
	  const ids = [item.id || state.ids].join(',');
	  ElMessageBox.confirm('确认删除已选中的数据项?', '警告', {
		confirmButtonText: '确定',
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
	}

	function handleRowClick(id) {
	     state.queryParams.portfolios=id;
		 emit("change",state.queryParams);
	}
	function visibleChange(item){
		item.isvisable = !item.isvisable
	}
	function dailogRankShow(){
		state.dialogRank.visible = true
	}
	function loadProfile(){
		authApi.loadProfile({"groupid":state.queryParams.groupid}).then(res=>{
			if(res.data){
			   state.profileList=res.data;
			   state.queryParams.profileid=res.data[0].id;
			}else{
			   state.queryParams.profileid="";
			}
			 handleQuery();
		})
	}
	onMounted(async () => {
		await authApi.getGroup().then(res=>{
			if(res.data){
				state.groupList=res.data
				state.queryParams.groupid=res.data[0].id;
				loadProfile();
			}else{
				state.groupList=[]
				state.queryParams.groupid="";
				state.profileList=[];
				state.queryParams.profileid="";
				handleQuery();
			}
			
		});
		 
	   
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