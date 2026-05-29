<template>
 
        <div class="con-header">
        <el-row>
            <el-space >
				<el-cascader
				      v-model="warehouseCheck"
				      :options="warehouseData.list"
					  :value = "id"
					  :label="name"
				      :props="props"
				      @change="warehouseChange"
				    />
				<el-date-picker
				        v-model="dateValue"
						@change = "dateChange"
				        type="daterange"
						:clearable="false"
				        range-separator="至"
				        start-placeholder="开始日期"
				        end-placeholder="结束日期"
				        :shortcuts="shortcuts"
				      />
                <el-input  v-model="searchKeywords" @clear="clearSearch" placeholder="请输入" class="input-with-select" clearable>
                    <template #prepend>
                        <el-select v-model="selectlabel" @change='searchTypeChange'  placeholder="SKU" style="width: 110px">
                            <el-option label="SKU" value="sku"></el-option>
                            <el-option label="订单编号" value="number"></el-option>
                        </el-select>
                    </template>
                    <template #append>
                        <el-button @click="searchList">
                            <el-icon style="font-size: 16px;align-itmes:center">
                                <search />
                            </el-icon>
                        </el-button>
                    </template>
                </el-input>
            </el-space>
            <div class='rt-btn-group'>
				<el-space :size="16">
 
               </el-space>
				<el-button   class='ic-btn' title='帮助文档'>
                    <help theme="outline" size="16" :strokeWidth="3"/>
                </el-button>
            </div>
        </el-row>
		<el-row>
			 	<PrePlan type="rec" :warehouseid="warehouseid" @change="loadRunid" :saveData="addConsumablePlan" @query="showPlanItem"></PrePlan>
		</el-row>
        </div>
 
        <el-row>
			<StorageDetailTable isConsumable="ok"  @getSelection="getSelections" ref ="StorageDetailTableRef" @getTotalmount = "gettolmount"  />
        </el-row>

	
 
</template>
<script>
    export default{ name:"采购包装" };
</script>
<script setup>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import { ref,reactive,onMounted,watch } from 'vue'
    import {Search,ArrowDown,Close} from '@element-plus/icons-vue'
	import storagedetailApi from '@/api/erp/purchase/receive/storagedetailApi.js';
	import consumableApi from '@/api/erp/purchase/plan/consumableApi.js';
	import preprocessApi from '@/api/erp/purchase/plan/preprocessApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import { ElMessage ,ElMessageBox} from 'element-plus';
	import { useRoute,useRouter } from 'vue-router';  
	import StorageDetailTable from "@/views/erp/purchase/report/receive/components/storageDetail_table.vue";
	import PrePlan from "@/views/erp/purchase/components/preplan.vue";

	  		const router = useRouter();
			let totalamount = ref()
			let StorageDetailTableRef = ref()
			let warehouseCheck =ref()
			let warehouseid=ref("");
			let warehouseData =reactive({list:[]})
			let tableData=reactive({records:[],total:0})
			let dateValue =ref()
			let searchKeywords =ref()
			let selectlabel = ref()
			let selectRows=ref();
			let editDatas=ref({});
			let runid=ref();
			let planid=ref();
			const props = {
			  value:'id',
			  label:'name',
			}
			function searchList(){
				StorageDetailTableRef.value.search = searchKeywords.value;
				StorageDetailTableRef.value.loadtableData() 
			}
			function clearSearch(){
				searchList()
			}
			const shortcuts = [
			  {
			    text: '近7天',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
			      return [start, end]
			    },
			  },
			  {
			    text: '近一个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
			      return [start, end]
			    },
			  },
			  {
			    text: '近3个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
			      return [start, end]
			    },
			  },
			]
			onMounted(()=>{
			dateValue.value = shortcuts[0].value();
			getWarehouseData();
			})
			const getWarehouseData = function(){
				warehouseApi.getWarehouseList().then(function(res){
					warehouseData.list = res.data
					if(res.data && res.data.length>0){
						warehouseCheck.value=res.data[0].children[0].id;
						warehouseid.value=res.data[0].children[0].id;
						StorageDetailTableRef.value.getWarehouseid(warehouseCheck.value)
					}
				})
			}
			function warehouseChange(val){
				let mywarehouseid =''
				if(val){
					warehouseData.list.forEach((item)=>{
						item.children.forEach((sub)=>{
							if(sub.id == val[1]){
								mywarehouseid =sub.id
							}
						})
					})
				}else{
					mywarehouseid =''
				}
				warehouseid.value=mywarehouseid;
				StorageDetailTableRef.value.getWarehouseid(mywarehouseid);
			}
			function gettolmount(data){
				totalamount.value=data
			}
			function dateChange(val){
				StorageDetailTableRef.value.getdate(val)
			}
			function searchTypeChange(){
				StorageDetailTableRef.value.searchtype = selectlabel.value
			}
			function exportData(){
				StorageDetailTableRef.value.downLoadExcel()
			}
			function getSelections(tablerow,rows,consformid){
				selectRows.value=rows;
				editConsumablePlan(tablerow,consformid);
			}
			
			
			function editConsumablePlan(tablerow,consformid){
				var datas={};
				var skulists=[];
				var tablelist=[];
				if(selectRows.value && selectRows.value.length>0){
					selectRows.value.forEach(item=>{
						skulists.push(item.recid);
					});
				}
				if(tablerow && tablerow.length>0){
					tablerow.forEach(items=>{
						tablelist.push(items.recid);
					});
				}
				
				datas.selectList=skulists;
				datas.tableList=tablelist;
				datas.runid=consformid;
				editDatas.value=datas;
				 
			}
			
			function addConsumablePlan(){
			  return	editDatas.value;
			}
			
			

			
			function getWarehouseid(){
				var warehouseid=null;
				if(warehouseCheck.value && warehouseCheck.value.length==2){
					warehouseid=warehouseCheck.value[1];
				}else{
					warehouseid=warehouseCheck.value;
				}
				return warehouseid;
			}
			

			
			function showPlanItem(row){
				runid.value=row.id;
				StorageDetailTableRef.value.runid=row.id;
				StorageDetailTableRef.value.preprocessingid=row.id;
				StorageDetailTableRef.value.loadtableData(); 
			}
			
			function loadRunid(preprocessingForm){
					planid.value=preprocessingForm.id;
					StorageDetailTableRef.value.runid=planid.value;
					StorageDetailTableRef.value.preprocessingid="";
					StorageDetailTableRef.value.loadtableData(); 
			  }  
</script>
<style>
</style>