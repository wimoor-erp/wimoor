 
<template>
  <div class="main-sty el-white-bg">
    <el-form ref="queryFormRef" :model="queryParams" :inline="true">
      <el-form-item>
		 <el-button type="primary" class="im-but-one" @click="handleAdd">
				    <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
				    <span>添加地址</span>
		 </el-button>
        <el-button
          :disabled="single"
          @click="handleDelete"
          >删除
        </el-button>
      </el-form-item>

      <el-form-item prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="名称"
        />
      </el-form-item>

      <el-form-item prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="状态"
          clearable
        >
          <el-option :value="1" label="正常" />
          <el-option :value="0" label="删除" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button  @click="resetQuery"> 重置 </el-button>
      </el-form-item>
    </el-form>

 <GlobalTable  
 height="calc(100vh - 220px)" 
 :queryParams="queryParams"
 @loadTable="loadtableData" 
 :defaultSort="{ prop: 'opttime', order: 'descending' }"  
 @selectionChange="handleSelectionChange"
 :stripe="true"  style="width: 100%;margin-bottom:16px;">
 	<template #field>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="name" label="地址名称" width="200"/>
      <el-table-column prop="number" label="地址编码" width="100"/>
      <el-table-column prop="detail" label="地址详情"    show-overflow-tooltip  width="120"/>
	  <el-table-column label="对应仓库"   >
		  <template #default="scope">
		    <span class='tag-mr' v-show='scope.row.boundWarehouseList'  v-for='(item,index) in scope.row.boundWarehouseList' :key='index'>
		        <el-tag closable size="small" @close="unboundAddress(scope.row.id,item.id)" type="info">{{item.name}}</el-tag>
		    </span>
		  </template>
	  </el-table-column>
      <el-table-column prop="postcode" label="邮编" width="80" />
	  <el-table-column prop="phone" label="业主电话" width="120" />
	  <el-table-column prop="landlord" label="业主姓名" width="140" />
	  <el-table-column prop="remark" label="备注" width="120" show-overflow-tooltip   />
	  <el-table-column prop="lostEffectDate" label="到期时间" width="150" />
      <el-table-column label="操作" align="center" width="150">
        <template #default="scope">
          <el-button
            type="primary"
            link
			v-if="scope.row.disabled==false"
            @click.stop="handleUpdate(scope.row)"
          >编辑
          </el-button>
          <el-button
            type="primary"
            link
			v-if="scope.row.disabled==false"
            @click.stop="handleBound(scope.row)"
          >绑定
          </el-button>

          <el-button
            type="primary"
            link
			v-if="scope.row.disabled==false"
            @click.stop="handleDelete(scope.row)"
          >删除
          </el-button>
		  <el-button
		    type="primary"
		    link
		    v-if="scope.row.disabled"
		    @click.stop="handleRefound(scope.row)"
		  >还原
		  </el-button>
        </template>
      </el-table-column>
     </template>
 </GlobalTable>
   <WarehouseDialog :visible="warehouseDailog.visible" @dataChange="dataChange"  @handleClose="handleClose"></WarehouseDialog>
    <!-- 添加或修改部门对话框 -->
    <el-dialog
      :title="dialog.title"
      v-model="dialog.visible"
      width="600px"
      @closed="cancel"
    >
      <el-form
        ref="dataFormRef"
        :model="formData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="地址名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入地址名称" />
        </el-form-item>
		<el-form-item label="地址编码" prop="number">
		  <el-input v-model="formData.number" placeholder="001(注意:请勿使用中文)" />
		</el-form-item>
         <el-form-item label="地址详情" prop="detail">
           <el-input v-model="formData.detail"   
		   :rows="2"
            type="textarea" placeholder="请输入地址详情如:xx省xx市龙xx区xx街道xxx社区xxx栋xxx" />
         </el-form-item>
		 <el-form-item label="邮编" prop="postcode">
		   <el-input v-model="formData.postcode" placeholder="邮编" />
		 </el-form-item>
         <el-form-item label="业主电话" prop="phone">
           <el-input v-model="formData.phone" placeholder="地址所拥有者的电话" />
         </el-form-item>
		 <el-form-item label="业主姓名" prop="landlord">
		   <el-input v-model="formData.landlord" placeholder="王小二" />
		 </el-form-item>
		 <el-form-item label="备注" prop="remark">
		   <el-input v-model="formData.remark" placeholder="备注" />
		 </el-form-item>
		 <el-form-item label="到期时间" prop="lostEffectDate">
			 <el-date-picker
			        v-model="formData.lostEffectDate"
			        type="date"
			        placeholder="请选择使用地址的到期时间"
			      />
		 </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm"> 确 定 </el-button>
          <el-button @click="cancel"> 取 消 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script>
    export default{ name:"仓库地址" };
</script>
<script setup>
// Vue依赖
import { onMounted, reactive, ref, toRefs } from 'vue';
// API依赖
import warehouseAddressApi from '@/api/erp/warehouse/warehouseAddressApi.js';
// 组件依赖
import { Search,  Edit, Refresh, Delete } from '@element-plus/icons-vue';
	import {Plus,Help} from '@icon-park/vue-next';
import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
import GlobalTable from "@/components/Table/GlobalTable/index.vue";
import WarehouseDialog from "@/views/erp/warehouse/base/warehouseDialog.vue"
	  const queryFormRef = ref(ElForm);
	  const dataFormRef = ref(ElForm);
	  const state = reactive({
	    // 选中ID数组
	    ids: []  ,
	    // 非单个禁用
	    single: true,
		handlerid:"",
	    // 表格树数据
	    tableData: {records:[],total:0}  ,
	    // 弹窗属性
	    dialog: { visible: false }  ,
		warehouseDailog:{visible: false},
	    // 查询参数
	    queryParams: {status:1,name:''} ,
	    // 表单数据
	    formData: { } ,
	    // 表单参数校验
	    rules: {
	      name: [{ required: true, message: '地址名称不能为空', trigger: 'blur' }],
		number: [{ required: true, message: '地址编码不能为空', trigger: 'blur' },
		         { min: 3, max: 3, message: '地址编码必须是三个英文字符或数字', trigger: 'blur' },],
	    },
	  });
	  
	  const {
	    single,
	    tableData,
	    queryParams,
	    formData,
	    rules,
	    dialog,
		warehouseDailog,
	  } = toRefs(state);
	  
	  /**
	   * 查询
	   */
	  function handleQuery() {
	     state.queryParams.searchSession=new Date();  
	  }
	  
	  function handleClose(){
		  state.warehouseDailog.visible=false;
	  }
	  
	  function handleBound(row){
		  state.warehouseDailog.visible=true;
		  state.handlerid=row.id;
	  }
	  function unboundAddress(addressid,warehouseid){
		  warehouseAddressApi.unboundWarehouseAddress(addressid,{"warehouseids":warehouseid}).then(res=>{
		  			   ElMessage.success('解绑成功');
		  			   handleQuery();
		  })
	  }
	  function dataChange(ids){
		   state.warehouseDailog.visible=false;
		   var myids="";
		   ids.forEach(item=>{
			   if(myids==""){
				   myids=item;
			   }else{
				   myids=myids+","+item;
			   }
		   })
		   warehouseAddressApi.bindWarehouseAddress(state.handlerid,{"warehouseids":myids}).then(res=>{
			   ElMessage.success('绑定成功');
			   handleQuery();
		   })
	  }
	  
	  function loadtableData(params,callback){
	  		  warehouseAddressApi.listWarehouseAddress(params).then(callback)
	  }
	  /**
	   * 重置查询
	   */
	  function resetQuery() {
	    queryFormRef.value.resetFields();
	    handleQuery();
	  }
	  
	  
	  function handleSelectionChange(selection) {
	    state.ids = selection.map((item) => item.id);
	    state.single = selection.length === 0;
	  }
	  
	  /**
	   * 添加
	   */
	  function handleAdd(row) {
	    state.formData.id = undefined;
	    state.dialog = {
	      title: '添加地址',
	      visible: true,
	    };
	  }
	  
	  /**
	   * 修改
	   */
	  async function handleUpdate(row) {
	    const id = row.id || state.ids;
	    state.dialog = {
	      title: '修改地址',
	      visible: true,
	    };
	    warehouseAddressApi.detailWarehouseAddress(id).then((response) => {
	      state.formData = response.data;
	    });
	  }
	  
	  /**
	   * 部门表单提交
	   */
	  function submitForm() {
	    dataFormRef.value.validate((valid) => {
	      if (valid) {
	        if (state.formData.id) {
	          warehouseAddressApi.updateWarehouseAddress(state.formData.id, state.formData).then(() => {
	            ElMessage.success('修改成功');
	            cancel();
	            handleQuery();
	          });
	        } else {
	          warehouseAddressApi.addWarehouseAddress(state.formData).then(() => {
	            ElMessage.success('新增成功');
	            cancel();
	            handleQuery();
	          });
	        }
	      }
	    });
	  }
	  
	  /**
	   * 删除部门
	   *
	   * @param row
	   */
	  function handleDelete(row) {
	    const ids = [row.id || state.ids].join(',');
	    ElMessageBox.confirm(`确认删除已选中的数据项?`, '警告', {
	      confirmButtonText: '确定',
	      cancelButtonText: '取消',
	      type: 'warning',
	    })
	      .then(() => {
	       warehouseAddressApi.disableWarehouseAddress(ids,{'disable':'true'})
	          .then(() => {
	            handleQuery();
	          })
	          .catch(() => {
	            console.log(`删除失败`);
	          });
	      })
	      .catch(() => ElMessage.info('已取消删除'));
	  }
	  /**
	   * 删除部门
	   *
	   * @param row
	   */
	  function handleRefound(row) {
	    const ids = [row.id || state.ids].join(',');
	    warehouseAddressApi.disableWarehouseAddress(ids,{'disable':'false'})
	       .then(() => {
	         handleQuery();
	         ElMessage.success('还原成功');
	       })
	       .catch(() => {
	         console.log(`还原失败`);
	       });
	  }
	  /**
	   * 取消/关闭弹窗
	   **/
	  function cancel() {
	    dialog.value.visible = false;
	    formData.value.id = undefined;
	    dataFormRef.value.resetFields();
	    dataFormRef.value.clearValidate();
	  }
	  
	  onMounted(() => {
	    handleQuery();
	  });
	  
  
</script>
<style scoped="scoped">
.el-form--inline .el-form-item{
	margin-right:8px;
}
</style>
