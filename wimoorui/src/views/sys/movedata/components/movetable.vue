<template>
		
		<div class="con-body">
			<el-row :gutter="20">
				<el-col :span="12">
					<div class="con-header">
						<el-row>
							<el-space>
							<div>数据库</div>
							<el-select v-model="database" @change="handleChange">
								<el-option v-for="item in databaseOptions" :key="item.Database" :value="item.Database" :label="item.Database"></el-option>
							</el-select>
							<el-input v-model="tableSearchName" @input="handleChange(database)" placeholder="请输入表名称" clearable></el-input>
								<el-button   :icon="Refresh" @click="handleChange(database)">刷新列表</el-button>
							<el-button @click="handleShowAdd" :loading="loading">添加到迁移任务</el-button>
							</el-space>
					    </el-row>
					</div>
					<div class="con-body">
						<el-table :data="tableOptions" 
				            height="calc(100vh - 200px)"  
							:default-sort="{ prop: 'data_length_mb', order: 'descending' }"
							 @selection-change="handleSelectionChange">
							<el-table-column type="selection" width="55" />
							<el-table-column property="name" label="名称" width="240" sortable/>
							<el-table-column property="rows" label="行数" width="120" sortable/>
					 
							<el-table-column property="tablecomment" label="备注" sortable/>
							<el-table-column property="data_length_mb" label="数据大小(M)" width="120" sortable/>
							<el-table-column property="data_length_gb" label="数据大小(G)" width="120" sortable />
							<el-table-column label="查看字段" prop="name" width="120">
							 <template #default="scope">
								<el-button type="primary"  link @click.stop="showTableColumn(scope.row)">查看</el-button>
							 </template>
						</el-table-column>
				  </el-table>
					</div>
				</el-col>
				<el-col :span="12">
					<div class="con-header">
						<el-row>
							<div style="padding-bottom:15px">
							<el-space>
							 <el-button type="success" @click="handleQueryTask">刷新任务进度</el-button>
							 <el-button type="primary"  @click="startTask">重启任务</el-button>
							 	<div class="flex-between">
								   <div></div>
									<el-button type="danger" @click="handleClear">清除</el-button>
							</div>
							</el-space>
						
						  
						</div>
						 <el-table :data="tasklist"  height="calc(100vh - 200px)" 	:default-sort="{ prop: 'table_size', order: 'descending' }">
								<el-table-column property="table_name" label="表名称" width="220" sortable>
									<template #default="scope">
										<div>{{scope.row.table_name}}</div>
										<div  ><span class="font-extraSmall">{{scope.row.database_name}}</span></div>
									</template>
								</el-table-column>
								<el-table-column property="table_size" label="行数" width="100" sortable/>
								<el-table-column property="download_size" label="迁移行" width="100" sortable />
								<el-table-column property="savedays" label="保留天数" width="100" sortable />
								<el-table-column property="datefield" label="迁移关键字">
										<template #default="scope">
											<div><span class="font-extraSmall">日期关键字:	{{scope.row.datefield}}</span></div>
											<div v-if="scope.row.authfield"><span class="font-extraSmall">授权关键字:	{{scope.row.authfield}}</span></div>
											<div v-if="scope.row.marketfield"><span class="font-extraSmall">市场关键字:	{{scope.row.marketfield}}</span></div>
											<div v-if="scope.row.countryfield"><span class="font-extraSmall">国家关键字:	{{scope.row.countryfield}}</span></div>
											<div v-if="scope.row.sellerfield"><span class="font-extraSmall">卖家关键字:	{{scope.row.sellerfield}}</span></div>
										</template>
								</el-table-column>
								<el-table-column  property="startdate"  label="开始时间" sortable />
								<el-table-column  property="enddate"  label="结束时间" sortable  />
								</el-table>
					    </el-row>
					</div>
				</el-col>
		  </el-row>
		</div>

	
	<el-dialog
    v-model="fieldDialogVisible"
    title="字段详情"
    width="50%"
    :before-close="handleClose"
  >
   <el-table :data="field" >
							<el-table-column prop="COLUMN_NAME" label="字段名"></el-table-column>
							<el-table-column prop="COLUMN_TYPE" label="数据类型"></el-table-column>
							<el-table-column prop="ORDINAL_POSITION" label="字段顺序"></el-table-column>
							<el-table-column prop="COLUMN_KEY" label="主键"></el-table-column>
						 </el-table>
						  <el-table :data="findex" >
							<el-table-column prop="INDEX_NAME" label="索引名"></el-table-column>
							<el-table-column prop="fieldname" label="索引字段"></el-table-column>
						 </el-table>
  </el-dialog>
		 <el-dialog
    v-model="dialogFormVisible"
    title="任务详情"
    width="50%"
    :before-close="handleClose"
  >
 
<el-form>
	<el-form-item label="选中数据表" prop="datefield">
		<el-table :data="selectItems" 
				        height="300"  
							:default-sort="{ prop: 'data_length_mb', order: 'descending' }"
						 >
							<el-table-column property="name" label="名称" width="240" sortable/>
							<el-table-column property="rows" label="行数" width="120" sortable/>
							<el-table-column property="tablecomment" label="备注" sortable/>
							<el-table-column property="data_length_mb" label="数据大小(M)" width="120" sortable/>
							<el-table-column property="data_length_gb" label="数据大小(G)" width="120" sortable />
							<el-table-column label="查看字段" prop="name" width="120">
							 <template #default="scope">
								<el-button type="primary"  link @click="showTableColumn(scope.row)">查看</el-button>
							 </template>
						</el-table-column>
				  </el-table>
					共{{selectItems.length}}条数据
	</el-form-item>
	<el-form-item label="日期字段" prop="datefield">
		<el-input v-model="formdata.datefield" placeholder="请输入日期字段"></el-input>
	</el-form-item>
	<el-form-item label="保留天数" prop="savedays">
		<el-input v-model="formdata.savedays" placeholder="请输入保留天数"></el-input>
	</el-form-item>
	<el-form-item label="授权字段" prop="authfield">
		<el-input v-model="formdata.authfield" placeholder="请输入授权字段"></el-input>
	</el-form-item>
	<el-form-item label="市场字段" prop="marketfield">
		<el-input v-model="formdata.marketfield" placeholder="请输入市场字段"></el-input>
	</el-form-item>
	<el-form-item label="国家字段" prop="countryfield">
		<el-input v-model="formdata.countryfield" placeholder="请输入国家字段"></el-input>
	</el-form-item>
	<el-form-item label="卖家字段" prop="sellerfield">
		<el-input v-model="formdata.sellerfield" placeholder="请输入卖家字段"></el-input>
	</el-form-item>
</el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="loading">
          提交并执行
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import {h, ref ,watch,reactive,onMounted,toRefs,nextTick} from 'vue'
import {Search,ArrowDown,Refresh} from '@element-plus/icons-vue';
import { ElMessage ,ElMessageBox} from 'element-plus';
import dataMoveApi from "@/api/sys/tool/dataMoveApi.js";
const state = reactive({
	loading: false, databaseOptions: [],
	findex: [], field: [],
	dialogFormVisible:false,
	database: "", tableOptions: "",
	tableName: "", tableList: [],
	tasklist: [], selectItems: [],
	fieldDialogVisible: false, tableSearchName: "",
	formdata:{ datefield:"",savedays:"",authfield:"",marketfield:"",countryfield:"",sellerfield:"" }
});
 
const { databaseOptions, database, tableName,
	field, findex,fieldDialogVisible,dialogFormVisible,
	tableOptions, tableList, tasklist, selectItems, loading,tableSearchName ,	formdata} = toRefs(state);
function handleChange(){
	dataMoveApi.showTables({"database":state.database,"tableSearchName":state.tableSearchName}).then(res=>{	
		state.tableOptions=res.data;
		nextTick(()=>{
			if(res.data&&res.data.length>0){
				state.tableName=res.data[0].name;
			}
		})
		handleQueryTask();
	})

}
function handleQueryTask(){
	dataMoveApi.showMoveTask({"database":state.database}).then(res=>{
		state.tasklist=res.data;
	})
}
function showTableColumn(row) {
	state.fieldDialogVisible = true;
			nextTick(()=>{
				dataMoveApi.showTableColumn({ "database": state.database, "tableName": row.name }).then(res => {
					state.field = res.data.field;
					state.findex = res.data.index;
				})
			})
}
function handleShowAdd(){
	state.dialogFormVisible=true;
}
function handleSelectionChange(val){
	state.selectItems=val;
}
function handleAdd(){
	var data=[];
	state.selectItems.forEach(item=>{
		var itemData={};
		itemData.database_name=state.database;
		itemData.table_name=item.name;
		itemData.table_size = item.rows;
		itemData.datefield=state.formdata.datefield;
		itemData.savedays = state.formdata.savedays;
		itemData.authfield = state.formdata.authfield;
		itemData.marketfield = state.formdata.marketfield;
		itemData.countryfield = state.formdata.countryfield;
		itemData.sellerfield = state.formdata.sellerfield;
		itemData.download_size=0;
		data.push(itemData);
	})
	state.loading=true;
	dataMoveApi.taskMoveAdd(state.database,data).then(res=>{
		ElMessage.success("操作成功");
		state.dialogFormVisible = false;
		handleQueryTask();
		state.loading=false;
	}).catch(e=>{
		state.loading=false;
	});
}
function handleClear(){
	dataMoveApi.clearMove({"database":state.database}).then(res=>{
		ElMessage.success("操作成功");
		handleQueryTask();
	})
}
function startTask(){
	dataMoveApi.startMoveTask({"database":state.database}).then(res=>{
		ElMessage.success("操作成功");
		handleQueryTask();
	})
}
function loadDatabase(){
	dataMoveApi.showDataBase().then(res=>{
		 state.databaseOptions=res.data;
		 if(state.databaseOptions&&state.databaseOptions.length>0){
			 for(var i =0;i<state.databaseOptions.length;i++){
				 if(state.databaseOptions[i].Database.indexOf("db_")>=0){
					  state.database=state.databaseOptions[i].Database;
					   handleChange(state.database);
					   break;
				 }
			 }
		    
		 }
	})
}
onMounted(()=>{
	loadDatabase();
})

 
</script>

<style>
</style>