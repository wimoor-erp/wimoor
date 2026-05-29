<template>
	<el-col :span="24">
		<GlobalTable ref="globalTable" :tableData="tableData"  @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		  <template #field>
		    <el-table-column type="selection" width="38" />
		    <el-table-column prop="name"  label="名称" sortable='custom' >
				<template #default="scope">
								<span>{{scope.row.name}}</span>
								&nbsp;&nbsp;&nbsp; <el-tag class="ml-2" v-if="scope.row.isdefault" type="info">默认</el-tag>  
				</template>
				
			</el-table-column>
		    <el-table-column prop="city"  label="所在发货地"  sortable='custom' >
				<template #default="scope">
					<div>{{scope.row.city}}</div>
					<div  ><span class="font-extraSmall">店铺：</span>{{scope.row.groupname}}<span v-if="scope.row.marketname"><span class="font-extraSmall">站点：</span>{{scope.row.marketname}}</span></div>
				  </template>
				</el-table-column>
			<el-table-column prop="addressline1" label="街道/详细地址" sortable='custom' >
			   <template #default="scope">
				<div>{{scope.row.addressline1}}</div>
				<div v-if="scope.row.addressline2">,{{scope.row.addressline2}}</div>
			  </template>
			</el-table-column>
		    <el-table-column prop="postalcode"  label="邮政编码"  sortable='custom' />
			<el-table-column prop="phone"  label="电话"  sortable='custom' />
			<el-table-column prop="operator"  label="操作人"  sortable='custom' />
		    <el-table-column prop="operate"  label="操作" width="140"  >
		        <template #default="scope">
		            <el-button class='el-button--blue' @click="editAddress(scope.$index, scope.row)">编辑</el-button>
		            <el-popconfirm
		                confirm-button-text="是"
		                cancel-button-text="否"
		                :icon="InfoFilled"
		                icon-color="#626AEF"
		                title="您确认要删除此地址吗?"
		                @confirm="deleteAddress(scope.$index, scope.row)"
		                @cancel="cancelEvent"
		              >
		                <template #reference>
		                  <el-button class='el-button--blue'>删除</el-button>
		                </template>
		              </el-popconfirm>
				 
		        </template>
		    </el-table-column>
	    </template>
	</GlobalTable>
	</el-col>
</template>

<script>
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted } from 'vue'
	import {Search,ArrowDown,InfoFilled} from '@element-plus/icons-vue'
	import {ElMessage,ElLoading} from 'element-plus';
	import addressApi from '@/api/amazon/inbound/addressApi.js';
		export default {
			name: 'list',
			components: { GlobalTable},
			setup(prop, context) {
				let globalTable=ref(GlobalTable);
				let tableData=reactive({records:[],total:0})
				let myparams={};
				function refreshTable(params){
					myparams=params;
					globalTable.value.loadTable();
				}
				function loadTableData(params){
					if(!params){
						params={isdisable:""};
					}
					params.isdisable=myparams.isdisable;
					params.groupid=myparams.groupid;
					addressApi.getAddress(params).then((res)=>{
						tableData.records = res.data.records
						tableData.total =res.data.total
					})
				}
				function deleteAddress(index,row){
					    addressApi.deleteAddress({ids:row.id}).then((res)=>{
							ElMessage.success('操作成功！');
							globalTable.value.loadTable();
						});
						
				}
				function editAddress(index,row){
					 context.emit("editAddress",row) ;
				}
				return {loadTableData,tableData,globalTable,refreshTable,deleteAddress,editAddress};
			}
		}
</script>

<style>
	.el-table{width:100%}
</style>
