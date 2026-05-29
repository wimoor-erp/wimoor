<template>
    <div class="main-sty">
        <div class="con-header">
        <el-row>
            <el-space >
                <el-button @click="exportData">
                    <span>导出</span>
                </el-button>
				<el-cascader
				      v-model="warehouseCheck"
				      :options="warehouseData.list"
					   :value = "name"
					   :label="name"
				      :props="props"
				      @change="warehouseChange"
					  placeholder="全部仓库"
					  clearable
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
                            <el-option label="SKU" value="SKU"></el-option>
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
				<span class="font-base"><span class="font-base-nine">入库数量总计:</span>{{totalamount}}</span>
               </el-space>
				<el-button   class='ic-btn' title='帮助文档'>
                    <help theme="outline" size="16" :strokeWidth="3"/>
                </el-button>
            </div>
        </el-row>
        </div>
        <!--表单-->
        <el-row>
			<StorageDetailTable ref ="StorageDetailTableRef" @getTotalmount = "gettolmount"/>
        </el-row>

    </div>
</template>
<script>
    export default{ name:"采购入库明细" };
</script>
<script setup>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import { ref,reactive,onMounted,watch } from 'vue'
    import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import StorageDetailTable from "./components/storageDetail_table.vue";
	import storagedetailApi from '@/api/erp/purchase/receive/storagedetailApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
    
	let totalamount = ref()
	let StorageDetailTableRef = ref()
	let warehouseCheck =ref()
	let warehouseData =reactive({list:[]})
	let dateValue =ref()
	let searchKeywords =ref()
	let selectlabel = ref()
			const props = {
			  value:'id',
			  label:'name',
			}
			function searchList(){
				StorageDetailTableRef.value.search = searchKeywords.value
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
			getWarehouseData()
			})
			const getWarehouseData = function(){
			
				warehouseApi.getWarehouseList().then(function(res){
					warehouseData.list = res.data;
					StorageDetailTableRef.value.getWarehouseid("");
				})
			}
			function warehouseChange(val){
				let warehouseid =''
				if(val){
					warehouseData.list.forEach((item)=>{
						item.children.forEach((sub)=>{
							if(sub.id == val[1]){
								warehouseid =sub.id;
							}
						})
					})
				}else{
					warehouseid =''
				}
			 
				StorageDetailTableRef.value.getWarehouseid(warehouseid)
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
            
</script>
<style>
</style>