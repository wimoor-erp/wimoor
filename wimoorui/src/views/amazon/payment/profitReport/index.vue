	<template>
		<div class="main-sty con-header"> 
			<el-tabs v-model="queryParams.charttype"  @tab-change="handleChange">
			  <el-tab-pane v-for="item in ViewData"  :label="item.label" :name="item.name" :key="item.name">
			  </el-tab-pane>
			</el-tabs>
			<el-row>
				<Group @change="getGroup"    ref="groupRef" />
			 <el-space>
			<!-- 	 <Group ref="groups" @change="getData" defaultValue="all"/> -->
				 <el-select style="width:100px;" v-model="queryParams.currency" @change="handleQuery">
				 	<el-option label="人民币" value="CNY"></el-option>
				 	<el-option label="美元" value="USD"></el-option>
				 	<el-option label="站点币种" value="MARKET"></el-option>
				 </el-select>
				 <Category  style="width:200px;" @change="changeCategory" ref="categoryRef"></Category>
				 <Owner style="width:120px;"   @owner="getOwner" ref="ownerRef" ></Owner>
				 <Datepicker type="monthrange" :shortIndex="1" ref="datepickers"   unlink-panels @changedate="changedate" />
			    <el-input 
				    v-model="queryParams.search" 
					placeholder="请输入" 
					@input="searchConfirm" 
					class="input-with-select" >
			      <template #prepend>
			        <el-select v-model="queryParams.fatype" @change='searchTypeChange'  style="width:100px">
			          <el-option label="MSKU" value="msku"></el-option>
			          <el-option label="ASIN" value="asin"></el-option>
			          <el-option label="父ASIN" value="parentasin"></el-option>
			          <el-option label="SKU" value="sku"></el-option>
			          <el-option label="产品名称" value="name"></el-option>
			        </el-select>
			      </template>
			      <template #append>
			        <el-button @click="handleQuery">
			           <el-icon style="font-size: 16px;align-itmes:center">
			            <search />
			         </el-icon>
			        </el-button>
			      </template>
			    </el-input>
			   <el-popover   v-model:visible="moreSearchVisible" :width="400" trigger="click">
			         <template #reference>
			           <el-button  :type="filterBtn" plain title='高级筛选'  class='ic-btn'>
			           <i>
			           <svg width="16" height="16" fill="none" viewBox="0 0 48 48"  xmlns="http://www.w3.org/2000/svg">
			           	<path d="M6 9L20.4 25.8178V38.4444L27.6 42V25.8178L42 9H6Z" fill="none" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>
			            </i>
			           </el-button>
			         </template>
					  <el-form   ref="formRef" label-width="80px">
					  <el-form-item label="产品标签" prop="remarks">
			                <Tags ref="tagsRef" @change="changeTag"  />
					     </el-form-item>
						  <el-form-item>
						       <el-button type="primary" @click="">搜索</el-button>
						       <el-button @click="cancelFilter">取消</el-button>
						     </el-form-item>
						</el-form>
			       </el-popover>
			    <el-button>重置</el-button>
			  </el-space>
			  <div class='rt-btn-group'>
							 <el-button @click="downloadList" :loading="loading">导出</el-button>
			  				   <el-button
			  				    @click="recalculation"
			  				    > 重算 </el-button>
			  				   <el-button class='ic-btn' @click="formulaConfig" title='毛利润计算公式'>
			  					  <formula theme="outline" size="14"  :strokeWidth="3"/>
			  				   </el-button>
			  					<el-button   class='ic-btn' title='帮助文档'>
			  					 <help theme="outline" size="16" :strokeWidth="3"/>
			  				   </el-button>
			  </div>
			  </el-row>
			   <!--功能区域 -->
			  
 
		
	 
			<TableHorizontal v-if="tabletype=='horizontal'" :activeName="queryParams.charttype" @changeTable="handleChnageTable" ref="tableRef"/>
			<TableVertical v-else :activeName="queryParams.charttype" @changeTable="handleChnageTable" ref="tableRef"/>
		</div>
		<FormulaConing ref="FormulaRef"/>
		<AccCalcDialog   ref="calcDialogRef"></AccCalcDialog>
		<RefreshDataDialog ref="refreshDataDialogRef"></RefreshDataDialog>
	</template>
	<script>
	    export default{ name:"利润报表" };
	</script>
	<script setup>
		import { ref,reactive,onMounted,toRefs,nextTick} from 'vue'
		import Datepicker from '@/components/header/datepicker.vue';
		import Group from '@/views/amazon/listing/common/group.vue';
		import RefreshDataDialog from '@/views/amazon/payment/common/refresh_data_dialog.vue';
		import Owner from '@/components/header/owner.vue';
		import Category from '@/components/header/category.vue';
		import Tags from '@/components/header/tags.vue';
		import TableHorizontal from"./table/table.vue";
		import TableVertical from"./table/table_vertical.vue";
		import AccCalcDialog from './components/accCalc_dialog.vue';
		import FormulaConing from "./components/formula.vue"
		import {Search,ArrowDown,} from '@element-plus/icons-vue'
		import {useRouter } from 'vue-router'
		import { ElMessage, ElMessageBox} from 'element-plus'
		import {Plus,Formula,Help,Copy,MoreOne} from '@icon-park/vue-next';
		import settlementMonthSummaryApi from "@/api/amazon/finances/settlementMonthSummaryApi.js";
		const router = useRouter();
		const FormulaRef =ref();
		const tableRef =ref();
		const refreshDataDialogRef=ref();
		const calcDialogRef=ref();
		let state=reactive({
			visable:false,
			dialogVisible:false,
			moreSearchVisible:false,
			tabletype:"horizontal",
			filterBtn:'',
			ViewData:[
				{label:'SKU',name:'sku',},
				{label:'MSKU',name:'msku',},
				{label:'ASIN',name:'asin',},
				{label:'父ASIN',name:'parentasin',},
				{label:'品类',name:'categoryid',},
				{label:'店铺',name:'groupid',},
			],
			selectrows:[],
			queryParams:{currency:"CNY",charttype:"sku",fatype:"sku",search:""},
			loading:false,
		})
		const{
			filterBtn,
			moreSearchVisible,
			activeName,ViewData,
			dialogVisible,
			tabletype,
			selectrows,
			visable,
			queryParams,
			loading,
		}=toRefs(state)
		function downloadList(){
			state.loading=true;
			settlementMonthSummaryApi.downloadList(state.queryParams).then((res)=>{
				state.loading=false;
			}).catch(e=>{
				state.loading=false;
 			});
		}
		function handleChnageTable(value){
			state.tabletype=value;
			nextTick(()=>{
			handleQuery();
			})
		}
		function formulaConfig(){
			//FormulaRef.value.show()
			calcDialogRef.value.show();
		}
		
		function recalculation(){
			refreshDataDialogRef.value.show();
		}
		
		function handleChange(val){
			setTimeout(function(){handleQuery();},100);
			 
		}
		 function getOwner(id){
			state.queryParams.ownerid=id;
			 handleQuery();
		 }
		//日期改变
		function changedate(dates){
			state.queryParams.fromDate=dates.start;
			state.queryParams.endDate=dates.end; 
			handleQuery();
		}
		function getGroup(obj){
			state.queryParams.groupid=obj.groupid;
			state.queryParams.marketplaceid=obj.marketplaceid;
			handleQuery();
		}
		function handleQuery(){
			 
			 tableRef.value.show(state.queryParams);
		}
		
	</script>
	
	<style scoped="scoped">
	</style>
	



