<template>
	<div class='main-sty'>
		<!--  头部搜索区域 -->
		<div class="con-header">
			<el-tabs  v-model="queryParams.status"class="demo-tabs" @tab-change="handleQuery">
			    <el-tab-pane  v-for="item in statusOptions"   :label="item.name" :value="item.value"> </el-tab-pane>
			  </el-tabs>
			<el-form ref="queryFormRef" :model="queryParams" :inline="true">
			<el-row>
				<el-space>
					<el-form-item>
						<el-select v-model="queryParams.roleid" @change="handleQuery" placeholder="角色权限">
							<el-option v-for="item in ruleOptions" :key="item.id" :label="item.name" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-input v-model="queryParams.searchKeywords"  @change="handleQuery" placeholder="请输入" class="input-with-select">
							<template #prepend>
								<el-select v-model="queryParams.searchType"  @change="handleQuery" placeholder="用户名" style="width: 110px">
									<el-option label="用户名" value="1"></el-option>
									<el-option label="登录账号" value="2"></el-option>
								</el-select>
							</template>
							<template #append>
								<el-button  @click="handleQuery">
									<el-icon style="font-size: 16px;align-itmes:center">
										<search />
									</el-icon>
								</el-button>
							</template>
						</el-input>
					</el-form-item>
					<el-form-item>
					  <el-button @click="resetQuery">重置</el-button>
					</el-form-item>
				</el-space>
			</el-row>
			<!--功能区域 -->
			<el-row>
				<el-form-item>
				<el-space>
					<el-button type="primary" class="im-but-one" @click="handleAdd">
						<plus theme="outline" size="18" fill="#fff" :strokeWidth="4" />
						<span>添加用户</span>
					</el-button>
					<el-button :disabled="multiple"  @click="handleEnable">启用</el-button>
					<el-button :disabled="multiple"  @click="handleDisable">停用</el-button>
					<el-button :disabled="multiple"  @click="handleDelete" >删除</el-button>
				</el-space>
				</el-form-item>
				<div class='rt-btn-group'>
					<el-button class='ic-btn' title='列配置'>
						<setting-two theme="outline" size="16" :strokeWidth="3" />
					</el-button>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
				</div>
			</el-row>
		  </el-form>
		</div>
		<!-- 表单 -->
		<GlobalTable ref="globalTable"  height="calc(100vh - 340px)" :tableData="tableData"  
		 @loadTable="loadTableData" border style="width: 100%;margin-bottom:16px;"
		 @selectionChange = "handleSelectionChange">
		  <template #field>
		   <el-table-column type="selection" width="38" />
		    <el-table-column prop="name" label="用户名" width="160" sortable="custom"/>
		  <el-table-column prop="account" label="登录账号" width="160"  />
		 <el-table-column prop="status" label="状态" width="80"   >
		     <template #default="scope">
		         <el-tag  :type="scope.row.status==0?'success':'info'" effect="plain">{{scope.row.statusName}}</el-tag>
		     </template>
		 </el-table-column>
		  <el-table-column prop="deptname" label="部门" width="110"  />
		 <el-table-column prop="mark" label="角色权限"   >
		    <template #default="scope">
		      <span class='tag-mr' v-show='item'  v-for='(item,index) in scope.row.roles' :key='index'>
		          <el-tag  type="info">{{item}}</el-tag>
		      </span>
		    </template>
		 </el-table-column>
		 <el-table-column prop="store" label="店铺权限"   >
		    <template #default="scope">
		      <span class='tag-mr' v-show='item'  v-for='(item,index) in scope.row.groups' :key='index'>
		          <el-tag  type="info">{{item}}</el-tag>
		      </span>
		    </template>
		 </el-table-column>
		 <el-table-column prop="user" label="数据权限"   >
		    <template #default="scope">
		      <span class='tag-mr' v-show='item'  v-for='(item,index) in scope.row.perms' :key='index'>
		          <el-tag  type="info">{{item}}</el-tag>
		      </span>
		    </template>
		 </el-table-column>
		  <el-table-column prop="createDate" label="创建时间"  width="120"  sortable="custom">
		    <template #default="scope">
				{{dateFormat(scope.row.createDate)}}
			</template>
		 </el-table-column>
		  <el-table-column prop="losingEffect" label="到期时间"   width="120" sortable="custom">
		  <template #default="scope">
		  				{{dateFormat(scope.row.losingEffect)}}
		  			</template>
		  </el-table-column>
		  <el-table-column prop="operate" label="操作"  width="120"   sortable>
		      <template #default="scope">
		          <el-button class='el-button--blue' @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
		      </template>
		  </el-table-column>
		  </template>
		</GlobalTable>
	</div>
	<el-dialog v-model="dialog.visible" title="添加用户"   destroy-on-close='true' width="600px"  >
		<el-form ref="dataFormRef" :inline="true" :model="formData" :rules="rules" class="form-width-fill" label-width="auto" >

			<el-form-item label="登录账号"  prop="account" >
				<el-input v-model="formData.account" placeholder="请输入(手机,邮箱)..."></el-input>
			</el-form-item>
			<el-form-item label="登录密码"  prop="password" >
				<el-input v-if="formData.id" v-model="formData.password" type="password" show-password placeholder="******"></el-input>
				<el-input v-else v-model="formData.password" type="password" show-password :placeholder="'默认密码 '+formData.account+'wimoor!0'"></el-input>
			</el-form-item>
			<el-form-item label="用户名"    prop="name">
				<el-input v-model="formData.name" placeholder="请输入..."></el-input>
			</el-form-item>
			<el-form-item label="账号期限"   prop="losingEffect">
				 <el-date-picker
				        v-model="formData.losingEffect"
				        type="date"
				        placeholder="请选择..."
				        :size="size"
				      />
			</el-form-item>
			<el-form-item label="状态(是否停用)"  prop="disable">
				<el-switch
				    v-model="formData.disable"
				  />
			</el-form-item>
			<el-form-item label="所在部门"  prop="disable">
				<el-tree-select
				  v-model="formData.deptid"
				  placeholder="无"
				  node-key="value"
				  :data="deptOptions"
				  filterable
				  ref="deptTreeRef"
				/>
			</el-form-item>
			
			<el-form-item label="权限设置" >
				<el-tabs v-model="activeName"  type="border-card" class="demo-tabs" @tab-click="handleClick">
				  <el-tab-pane  label="角色">
					  <el-checkbox-group v-model="formData.roles" label="myname"  >
						    <el-checkbox  v-for="(item) in ruleFormOptions" :label="item.id" size="large" >
							   {{item.name}}
							</el-checkbox >
					   </el-checkbox-group>
				  </el-tab-pane>
				  <el-tab-pane  label="店铺">
					<el-checkbox-group v-model="formData.groups" @change="groupChange">
					<el-checkbox  v-for="(item) in groupOptions" :disabled="item.disable"  :label="item.id" size="large" >
					   {{item.name}}
					</el-checkbox >
					</el-checkbox-group>
				  </el-tab-pane>
				  <el-tab-pane  label="数据权限">
					  <el-checkbox-group v-model="formData.datalimits" >
				  			<el-checkbox  v-for="(item) in dataLimitTypeOptions"   :label="item.value" size="large" >
				  			   {{item.name}}
				  			</el-checkbox >
					  </el-checkbox-group>	
				  </el-tab-pane>
				</el-tabs>
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="cancel">取消</el-button>
				<el-button type="primary" @click="submitForm">提交</el-button>
			</span>
		</template>
	</el-dialog>
</template>
<script>
    export default{ name:"账号管理" };
</script>
<script setup>
	import { MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne } from '@icon-park/vue-next';
	import { Search,ArrowDown, } from '@element-plus/icons-vue'
	import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
	import { onMounted, reactive, ref, toRefs, watch, getCurrentInstance } from 'vue';
    import { listUserPages,deleteUsers,disableUsers,enableUsers,addUser,updateUser,getUserFormData} from "@/api/sys/admin/user.js"
	import { listRole} from "@/api/sys/admin/roles.js"
	import groupApi from "@/api/amazon/group/groupApi.js"
	import {listSelectDepartments} from '@/api/sys/admin/dept.js';
	import {dateFormat} from '@/utils/index.js';
 
			const queryFormRef = ref(ElForm);
			const dataFormRef = ref(ElForm);
			const globalTable = ref();
			const deptTreeRef=ref();
			const state = reactive({
			  // 选中ID数组
			  ids: []  ,
			  single:false,
			  multiple:true,
			  selected:false,
			  // 非单个禁用
			  loading: true,
			  // 表格树数据
			  tableData: {records:[],total:0} ,
			  // 弹窗属性
			  dialog: { visible: false }  ,
			  // 查询参数
			  queryParams: {status:"0",name:'',roleid:"",searchType:"1",searchKeywords:""} ,
			  // 表单数据
			  formData: {
			    name:'',
			    account:"",
				password:'',
			    state:true,
				losingEffect:'',
				disable:false,
				deptid:'',
				groups:[],
				roles:[],
				datalimits:[],
			  } ,
			  // 表单参数校验
			  rules: {
			    name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
			    account: [{ required: true, message: '账号不能为空', trigger: 'blur' }],
				losingEffect: [{ required: true, message: '失效日期不能为空', trigger: 'blur' }],
			  },
			  ruleOptions : [],
			  groupOptions : [],
			  ruleFormOptions : [],
			  dataLimitTypeOptions:[],
			  statusOptions : [],
			  deptOptions:[],
			});
			
			const {
			  ids,
			  single,
			  multiple,
			  loading,
			  statusOptions,
			  ruleOptions,
			  groupOptions,
			  deptOptions,
			  dataLimitTypeOptions,
			  queryParams,
			  tableData,
			  formData,
			  rules,
			  dialog,
			  ruleFormOptions,
			} = toRefs(state);
			
	        const { proxy } = getCurrentInstance();
            function handleQuery(){
				if(state.queryParams.searchType=="1"){
					state.queryParams.name=state.queryParams.searchKeywords;
					state.queryParams.account="";
				}else{
					state.queryParams.account=state.queryParams.searchKeywords;
					state.queryParams.name="";
				}
				globalTable.value.loadTable(state.queryParams);
			}
		
			function cancel(){
				resetForm();
				state.dialog.visible = false;
			}
			function resetForm() {
				dataFormRef.value.resetFields();
				state.formData= {
					name:'',
					account:"",
					password:'',
					state:true,
					losingEffect:'',
					disable:false,
					deptid:'',
					groups:[],
					roles:[],
					datalimits:[],
					
				 };
				var date=new Date();
				 date.setTime(date.getTime() + 3600 * 1000 * 24 * (3000));
				 state.formData.losingEffect=date; 
				 state.formData.groups=[];
				 initGroup();
			}
			function initGroup(){
				state.groupOptions.forEach(item=>{
				   state.formData.groups.push(item.id);
					if(item.id!="0"){
					  item.disable=true;
					}
				})
			}
			// 手机号正则表达式（中国大陆手机号）
			const phoneRegex = /^1[3-9]\d{9}$/;
			 // 邮箱正则表达式
			const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
			
			// 验证函数
			function validateUsername(username) {
				if (phoneRegex.test(username)) {
					return {
						isValid: true,
						type: 'phone',
						message: '✓ 手机号格式正确'
					};
				} else if (emailRegex.test(username)) {
					return {
						isValid: true,
						type: 'email',
						message: '✓ 邮箱格式正确'
					};
				} else {
					return {
						isValid: false,
						type: 'unknown',
						message: '✗ 请输入有效的手机号或邮箱地址'
					};
				}
			}
			function submitForm(){
				if(state.formData.roles.length==0){
					ElMessage.error('请至少选择一个角色');
					return;
				}
				var uservalide=validateUsername(state.formData.account);
				if(uservalide.isValid==false){
					ElMessage.error(uservalide.message);
					return;
				}
				if(!state.formData.password){
					state.formData.password=state.formData.account.trim()+"wimoor!0";
				}
				if(validatePasswordComplexity(state.formData.password)==false){
					ElMessage.error('确保密码长度至少为8，且需包含字母、数字和特殊字符');
					return;
				}
				dataFormRef.value.validate((isValid) => {
							if (isValid) {
								if(state.formData.groups.includes("0")){
									state.formData.groups=[];
									}
								if(state.formData.id){
									updateUser(state.formData.id,state.formData).then(res=>{
										ElMessage.success('修改成功');
										state.dialog.visible = false;
										handleQuery();
								    });
								}else{
									addUser(state.formData).then(res=>{
										ElMessage.success('保存成功');
										state.dialog.visible = false;
										handleQuery();
									})
								 }
									
								}
						});
			}
			function resetQuery(){
				queryFormRef.value.resetFields();
				state.queryParams = {
				  status:"0",name:'',roleid:"",searchType:"1",searchKeywords:""
				};
				handleQuery()
			}
			function handleUpdate(index,row){
				getUserFormData(row.id).then(res=>{
					state.formData.id=res.data.id;
					state.formData.name=res.data.userinfo.name;
					state.formData.account=res.data.account;
					state.formData.state=res.data.state;
					state.formData.disable=res.data.disable;
					state.formData.losingEffect=new Date(res.data.losingeffect);
					state.formData.deptid=res.data.deptid;
					state.formData.groups=res.data.groups;
					state.formData.roles=res.data.roles;
					state.formData.datalimits=res.data.datalimits; 
					state.dialog.visible = true;
					if(state.formData.groups.length==0){ 
						initGroup();
					}else{
						state.groupOptions.forEach(item=>{item.disable=false;})
					}
				});
			}
			function handleSelectionChange(selection) {
			  state.ids = selection.map((item) => item.id);
			  state.single = selection.length !== 1;
			  state.multiple = !selection.length;
			}
			function loadTableData(param){
				listUserPages(param).then(res=>{
					state.tableData.records = res.data.records
					state.tableData.total =res.data.total
				})
			}
			
			function handleEnable(){
				var myids=state.ids.join(",");
				enableUsers(myids).then(res=>{
					 ElMessage.success('启用成功');
					 handleQuery();
				});
			}
			
			function handleDisable(){
				var myids=state.ids.join(",");
				disableUsers(myids).then(res=>{
					ElMessage.success('禁用成功');
					handleQuery();
				})
			}
			
			function handleDelete(){
				var myids=state.ids.join(",");
				deleteUsers(myids).then(res=>{
					ElMessage.success('删除成功');
					handleQuery();
				})
			}
			//方法
			function handleAdd() {
				state.formData= {
					name:'',
					account:"",
					password:'',
					state:true,
					losingEffect:'',
					disable:false,
					deptid:'',
					groups:[],
					roles:[],
					datalimits:[],
					
				 };
				var date=new Date();
				 date.setTime(date.getTime() + 3600 * 1000 * 24 * (3000));
				 state.formData.losingEffect=date.format("yyyy-MM-dd"); ;
                 state.dialog.visible=true
			}
		 
			function groupChange(){
				if(state.formData.groups.includes("0")){
					state.groupOptions.forEach(item=>{
						if(!state.formData.groups.includes(item.id)){
							state.formData.groups.push(item.id);
						}
						if(item.id!="0"){
						  item.disable=true;
						}
					})
				}else{
					state.groupOptions.forEach(item=>{
						if(item.disable&&item.id!="0"){
							var index=state.formData.groups.indexOf(item.id);
							state.formData.groups=state.formData.groups.slice(index+1,1);
						}
						item.disable=false;
					})	
				}
			}
		   function loadDeptData() {
			  const deptOptions = [];
			  listSelectDepartments().then((response) => {
			    const rootDeptOption = {
					  value: '0',
					  label: '顶级部门',
					  children: response.data,
			    };
			    deptOptions.push(rootDeptOption);
			    state.deptOptions = deptOptions;
			  });
			}
			function hasMinLength(password) {
			    return password.length >= 8;
			}
			 
			function hasChar(password) {
			    return /[a-z]/.test(password)||/[A-Z]/.test(password);
			}
			 
			function hasNumber(password) {
			    return /\d/.test(password);
			}
			 
			function hasSpecialChar(password) {
			    return /[@$!%*?&]/.test(password);
			}
			function  validatePasswordComplexity(password) {
						return hasMinLength(password) && 
						           hasChar(password) && 
						           hasNumber(password) && 
						           hasSpecialChar(password);
					}

			onMounted(()=>{
				proxy.listDictsByCode("user_status").then(res=>{
					state.statusOptions=res.data;
					state.queryParams.status="0";
					listRole().then(res=>{
						state.ruleOptions=res.data;
						state.ruleFormOptions=res.data;
						handleQuery();
					})
				});
				proxy.listDictsByCode("limit_data_type").then(res=>{
					state.dataLimitTypeOptions=res.data;
				});
				
				groupApi.getAmazongroupList().then(res=>{
					var group=[{id:'0',name:'不限制',disable:false}];
					state.groupOptions=group.concat(res.data);
					initGroup(); 
				});
				loadDeptData() ;
			})
		 
 
</script>
<style scoped="scoped">
	.tag-mr {
		margin-right: 4px;
		margin-bottom: 4px;
		display: inline-block
	}

 .con-header .el-form--inline .el-form-item{
	 margin-right: 0px;
 }
  .con-header .el-form-item--default{
	 margin-bottom: 0;
 }
</style>
