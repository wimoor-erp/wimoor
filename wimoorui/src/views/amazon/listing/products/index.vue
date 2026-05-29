<template>
	<div class="main-sty" >
		<div class="con-header">
			<el-row>
				<Group @change="getGroup" isproduct="ok"  ref="groupRef" />
				<el-space>
					<Status @status="getStatus" ref="statusRef" />
					<Owner @owner="getOwner" ref="ownerRef" />
					<el-select v-model="disabletype" @change='disabletypeChange' placeholder="全部显示状态"
						style="width: 110px">
						<el-option label="未隐藏" value="false">未隐藏</el-option>
						<el-option label="隐藏" value="true">隐藏</el-option>
						<el-option label="失效" value="invalid">失效</el-option>
						<el-option label="不限" value="all">不限</el-option>
					</el-select>
					<div class="radioWrapper">
					 <el-radio-group v-model="asinType" @change="asinTypeChange">
					      <el-radio-button label="asin" >ASIN</el-radio-button>
					      <el-radio-button label="parentasin" >父ASIN</el-radio-button>
					    </el-radio-group>
						</div>
					<el-input v-model="searchKeywords" clearable @input="changeKeywords"  placeholder="请输入" class="input-with-select">
						<template #prepend>
							<el-select v-model="searchtype" @change='searchTypeChange' placeholder="SKU"
								style="width: 110px">
								<el-option label="SKU" value="sku">SKU</el-option>
								<el-option label="同级SKU" value="childsku">同级SKU</el-option>
								<el-option label="本地SKU" value="msku">本地SKU</el-option>
								<el-option label="ASIN" value="asin">ASIN</el-option>
								<el-option label="同级ASIN" value="childasin">同级ASIN</el-option>
								<el-option label="父ASIN" value="parentasin">父ASIN</el-option>
							</el-select>
						</template>
						<template #append>
							<el-button @click="searchConfirm">
								<el-icon style="font-size: 16px;align-itmes:center">
									<search />
								</el-icon>
				  	</el-button>
						</template>
					</el-input>
					<el-popover   :popper-append-to-body="false" v-model:visible="moreSearchVisible" :width="400" trigger="click">
						<template #reference>
						
							      <el-button :color="filterBtnColor" plain class='ic-btn'>
							      	<i>
							      	<svg width="16" height="16" fill="none" viewBox="0 0 48 48"  xmlns="http://www.w3.org/2000/svg">
							      		<path d="M6 9L20.4 25.8178V38.4444L27.6 42V25.8178L42 9H6Z" fill="none" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>
							         </i>
							      </el-button>
						</template>
						 <el-form :model="form"  label-width="auto"  label-position="left">
							<el-form-item label="产品标签">
							     <Tags ref="tagsRef" @change="changeTag"  />
							   </el-form-item>
						    <el-form-item label="产品分类">
								 <Category ref="cateRef" @change="getCategory" />
							 </el-form-item>
							<el-form-item label="销量">
							     <el-select  v-model="salerangecheck"  :teleported="false" placeholder="请选择" >
							    <el-option
							      v-for="item in salerange"
							      :key="item.value"
							      :label="item.label"
							      :value="item.value"
							    />
							     </el-select>
							   </el-form-item>
							   <el-form-item label="配送方式">
							        <el-select  v-model="isfba"  :teleported="false" placeholder="请选择" @change="trantypeChange">
							       <el-option
							         v-for="item in saleslist"
							         :key="item.value"
							         :label="item.label"
							         :value="item.value"
							       />
							        </el-select>
							      </el-form-item>
								  <el-form-item label="产品名称">
								    <el-input v-model="proname" clearable="true"></el-input>
								    </el-form-item>
								  <el-form-item label="备注">
									  <el-input v-model="remark" clearable="true"></el-input>
								    </el-form-item>
									<el-form-item label="发现差评">
										<el-checkbox v-model="isbadreview" label="" size="large" />
									 </el-form-item>
									
								    <el-form-item>
								  <el-button type="primary" @click="submitSearch(formRef)">搜索</el-button>
								  <el-button @click="resetSearch(formRef)">取消</el-button>
								    </el-form-item>
								
						</el-form>
						
					  </el-popover>
					
					<el-popover    v-model:visible="dataSearchVisible" :width="400" trigger="click">
						<template #reference>
							<el-button>指数过滤</el-button>
						</template>
						 <el-form :model="form" label-width="auto"   >
							<el-form-item label="数据字段">
							     <el-select  v-model="dataColumn"  :teleported="false" placeholder="请选择" @change="columnChange">
									<el-option
									  v-for="item in columnList"
									  :key="item.value"
									  :label="item.label"
									  :value="item.value"
									/>
							     </el-select>
							   </el-form-item>
							   <el-form-item label="符号">
							       <el-radio-group v-model="mark">
							           <el-radio :label="3">大于</el-radio>
							           <el-radio :label="6">小于</el-radio>
							           <el-radio :label="9">等于</el-radio>
							         </el-radio-group>
							      </el-form-item>
								  <el-form-item label="数值">
									  <el-input v-model="dataValue" type="number"  placeholder="用%时请输入小数"></el-input>
									  <el-button style="margin-top: 3px;" @click="addColumn" type="success">添加条件</el-button>
									   <el-button style="margin-top: 3px;" @click="clearColumn" type="success">清空条件</el-button>
								    </el-form-item>
									<el-form-item label="筛选条件">
										{{columntext}}
									</el-form-item>
								    <el-form-item>
								  <el-button type="primary" @click="submitDataForm(formRef)">搜索</el-button>
								  <el-button @click="resetDataForm(formRef)">取消</el-button>
								    </el-form-item>
						</el-form>
					  </el-popover>
					<el-button @click.stop="clearSearch" >重置</el-button>
				</el-space>
			</el-row>
			<!--功能区域 -->
			<el-row>
				<el-space>
					<el-button type="primary" class="im-but-one" @click.stop="showRefreshDialog">
					    <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
					    <span>同步商品</span>
					</el-button>
					<el-button @click.stop="refreshFBAInv">批量同步FBA库存</el-button>
					<el-button @click.stop="showOwnerDialog">批量修改负责人</el-button>
					<el-button @click.stop="showPriceDialog">批量修改商品价格</el-button>
					<el-button @click.stop="showStatusDialog">批量修改销售状态</el-button>
          <el-button @click.stop="showSyncDialog">批量同步商品</el-button>
				</el-space>
				<div class='rt-btn-group'>
					<el-space>
					<el-dropdown :hide-on-click="false" trigger="click"  @command="handleCommand">
					    <el-button class='ic-btn'  title='排序'>
					       <sort-one theme="outline" size="16"  :strokeWidth="3"/>
					    </el-button>
					    <template #dropdown>
						<ul class="sortdropdowntitle">
							  <li class="font-extraSmall">排序依据</li>
						 </ul>
					    <div  style="height:300px;overflow-y:scroll; ">
						
					      <ul class="sortdropdown">
					        <li v-for="(item,index) in rankData"  @click="rankChange(item.value)"
							 :class="{r_active:currentRank==item.value}" 
							>{{item.name}}</li>
					    </ul>
						    </div>
							<ul class="sortdropdowntitle" >
							<li divided  class="font-extraSmall">排序循序</li>
							</ul>
							 <ul class="sortdropdown">
							<li v-for="item in soltData" @click="soltChange(item.value)" 
							:class="{r_active:currentSolt==item.value}">{{item.name}}</li>
							</ul>
					    </template>
					  </el-dropdown>
					  <el-button class='ic-btn' title="日均销量公式" @click="EditSaleFormula">
						  <formula theme="outline" size="14"  :strokewidth="3"/>
					  </el-button>
					  <el-button class='ic-btn' title="店外商品搜索" @click="handleToOutSearch">
					  			 <el-icon style="font-size: 16px;align-itmes:center">
					  			 	<search />
					  			 </el-icon>
					  </el-button>
					</el-space>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
					 
				</div>
			</el-row>
			
			<!-- <el-row>
			筛选器
				<el-tag  closable>库存 > 10</el-tag>
			</el-row> -->
		</div>
		
		<div class="con-body">
			<Table  @checkRow="checkRows"  @changeOwner="showOwner"  ref="tableRef" @searchsku="handleSearchSku" />
		</div>
	</div>
	<el-dialog
     top="3vh"
	 width="95%"
	 class="skusearchdialog"
	 v-model="visable" 
	 title="商品信息" 
	 destroy-on-close='true' >
		 <Table @checkRow="checkRows" indialog="true"   ref="tableSkuRef"   />
	</el-dialog>
	<BatchModifypriceDialog ref="priceDialogRef"/>
	<!-- 日均销量公式 -->
	<SaleFormula ref = "SaleFormulaRef"/>
	<RefreshProDialog ref="refreshRef" />
	<el-dialog
	  v-model="ownerVisable"
	  title="批量修改运营负责人"
	  width="30%"
	>
	<el-tabs v-model="activeOwner">
		<el-tab-pane   name="manual"  label="运营负责人">
			<OwnerAll @owner="changeAllOwner" notAll="isNotAll" :defaultValue="ownerId" class="in-wi-24"  ref="ownerOptRef" :isInForm="true"  />
		</el-tab-pane>
		<el-tab-pane name="autolocal"  label="本地产品负责人">
			<span>使用当前商品对应的本地产品负责人覆盖运营负责人。</span>
		</el-tab-pane>
	</el-tabs>
	 <el-space>
			 
			
	 </el-space>
	  <template #footer>
	      <el-button @click="ownerVisable=false">取消</el-button>
	      <el-button type="primary" @click="refreshOwner">确认</el-button>
	  </template>
	</el-dialog>

  <el-dialog
      v-model="syncVisable"
      title="批量同步商品信息"
      width="30%"
  >
    <el-space>
      <span class="font-extraSmall">同步商品信息提交后，系统将会在2个小时内更新数据。</span>
    </el-space>
    <el-space>
      <el-form :model="syncForm"  ref="syncFormRef"  >
        <el-form-item label="更新内容" prop="sku">
          <el-checkbox-group v-model="syncForm.types" >
            <el-checkbox label="商品信息" value="info" />
            <el-checkbox label="商品价格" value="price" />
            <el-checkbox label="商品排名" value="rank" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="店铺名称" prop="groupid">
            <span>{{syncForm.selectgroup}}</span>
        </el-form-item>
        <el-form-item label="站点名称" prop="marketplaceid">
          <span>{{syncForm.selectmarket}}</span>
        </el-form-item>
        <el-form-item label="生效范围" prop="range">
            <el-radio-group v-model="syncForm.range">
                <el-radio label="all">所有{{syncForm.selectgroup}}-{{syncForm.selectmarket}}商品</el-radio>
                <el-radio label="local">当前选中商品生效</el-radio>
            </el-radio-group>
        </el-form-item>
      </el-form>
    </el-space>
    <template #footer>
      <el-button @click="syncVisable=false">取消</el-button>
      <el-button type="primary" @click="refreshSync">提交</el-button>
    </template>
  </el-dialog>
	<!-- 调价弹窗 -->
	<ModifyPriceDialog ref="modifypriceRef" @change="refreshTable"/>
	<!-- 销售状态 -->
	 <el-dialog
	   v-model="statusVisable"
	   title="批量修改商品销售状态"
	   width="30%"
	 >
	  <el-select v-model="statusid"    placeholder="产品销售状态"  >
	        <el-option  v-for="item in statusList"    :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	  
	   <template #footer>
	       <el-button @click="statusVisable=false">取消</el-button>
	       <el-button type="primary" @click="submitStatus">确认</el-button>
	   </template>
	 </el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h, nextTick} from 'vue';
	import {Help,Plus,MenuUnfold,SettingTwo,SortOne,Formula} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,Check} from '@element-plus/icons-vue';
	import Table from"./components/table.vue"
	import BatchModifypriceDialog from"./components/batchmodifypriceDialog.vue"
	import SaleFormula from"./components/sale_formula.vue"
	import Group from '@/views/amazon/listing/common/group.vue';
	import {useRouter } from 'vue-router'
	import Owner from '@/components/header/owner.vue';
	import Tags from '@/components/header/tags.vue';
	import Category from '@/components/header/category.vue';
	import ModifyPriceDialog from "./components/modifypriceDialog.vue"
	import Status from './components/prostatus.vue';
	import RefreshProDialog from './components/refreshProduct.vue';
	import OwnerAll from '@/components/header/ownerAll.vue';
	import productRefreshApi from '@/api/amazon/product/productRefreshApi.js';
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import productinoptApi from '@/api/amazon/product/productinoptApi.js';
	import {percentInput,CheckInputFloat} from '@/utils/index.js';
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
  import {initProductTask} from "@/api/amazon/listing/listingApi.js";
 
			onMounted(()=>{
				
			})
			const filterBtnColor = ref()
			const modifypriceRef=ref();
			const ownerOptRef=ref();
			const asinType = ref('asin')
			let activeOwner=ref('manual');
			let localPid=null;
			let ownerId=ref();
			let cateRef=ref();
			let tagsRef=ref();
			let visable=ref(false);
			let ownerVisable=ref(false);
      let syncVisable=ref(false);
			let statusVisable=ref(false);
			let feeVisable=ref(false);
			let groupRef=ref();
			let tableSkuRef=ref();
			let ownerRef=ref();
			let statusRef=ref();
			let refreshRef=ref();
			let priceDialogRef =ref()
			let radio2 = ref('ASIN')
			let salerangecheck =ref("")
			let trantype=ref()
			let currentRank =ref('')
			let currentSolt =ref("desc")
			let isload=ref(true)
			let searchtype=ref("sku")
			let disabletype=ref("false")
			let isfba=ref("")
			let tableRef=ref();
			let searchKeywords=ref();
			let remark=ref("");
			let proname=ref("");
			let SaleFormulaRef =ref()
			let moreSearchVisible=ref(false);
			let dataSearchVisible=ref(false);
			let isbadreview=ref(false); 
			let mark=ref(3);
			let dataValue=ref();
			let columntext=ref("");
			let columnval=ref("");
			let tags=ref();
			let statusid=ref("");
			let statusList=ref([]);
			let saleslist=reactive([{"value":"","label":"不限"},{"value":"AMAZON","label":"亚马逊配送"},{"value":"DEFAULT","label":"卖家自配送"}])
			let columnList=reactive([{"value":"v.advacos","label":"ACOS"},
			{"value":"v.advctr","label":"CTR"},
			{"value":"v.acoas","label":"ACOAS"},{"value":"v.advspc","label":"CR"},
			{"value":"v.sessionrate","label":"七日转化率"},
			{"value":"v.proprate","label":"净利润率"},
			{"value":"v.dayfulfilla","label":"可售天数"},
			{"value":"v.afn_fulfillable_quantity","label":"库存"},
			{"value":"v.profitrate","label":"产品利润率"}]);
			let dataColumn=ref("v.advacos");
	    let props = { multiple: true }
		let soltData=reactive([{name:'从高到低',value:'desc'},{name:'从低到高',value:"asc"}])
		var queryParam=reactive({});
		let router=useRouter();
    let syncForm=reactive({
      types:["info"],
      range:"all",
    })
		let rankData =reactive([
			{
				name:'SKU',
				value:'sku',
			},
		{
			name:'日均销量',
			value:'averageSalesDay',
		},
		
		{
			name:'库存',
			value:'afn_fulfillable_quantity',
		},{
			name:'跟卖数',
			value:'notread,followcount',
		},
		{
			name:'可售天数',
			value:'dayfulfilla',
		},
		{
			name:'利润',
			value:'profits',
		},
		{
			name:'价格',
			value:'landed_amount',
		},
		{
			name:'访问量',
			value:'sessions',
		},
		{
			name:'点击量',
			value:'advclick',
		},
		{
			name:'广告花费',
			value:'advspend',
		},
		{
			name:'净利润',
			value:'profitall',
		},
		{
			name:'产品利润率',
			value:'newprorate',
		},
		{
			name:'上架日期',
			value:'opendate',
		},
		{
			name:'Review数量',
			value:'feedback_count',
		},
		{
			name:'Review评分',
			value:'positive_feedback_rating',
		},
		{
			name:'父ASIN',
			value:'parentAsin',
		},
		])
		let salerange =reactive([
			{label:'不限',value:""},{label:'销量小幅下降',value:"-3"},
			{label:'销量大幅下降',value:"-25"},{label:'销量小幅上升',value:"3"},
			{label:'销量大幅上升',value:"25"}
		])
		let options =ref([]);
		let selection = ref([])
		function asinTypeChange(e){
			if(e=='parentasin'){
			     queryParam.isparent='parentasin';
				 tableRef.value.changeSize(50);
				 refreshTable();
			}else{
				queryParam.isparent='';
				tableRef.value.changeSize(20);
				refreshTable();
			}
		}
		function changeAllOwner(id){
			ownerId.value=id;
		}
		
		function checkRows(rows){
			selection.value =  rows;
		}
		function modifyPrice(){
			if(selection.value.length>0){
				priceDialogRef.value.priceVisable =true
			}else{
				ElMessage.error('请选择商品');
			}
			
		}
		//排序
		function rankChange(val){
			currentRank.value= val;
			if(val=='sku'){
				currentSolt.value="asc";
			}
			queryParam.sort=val;
			queryParam.order=currentSolt.value;
			refreshTable();
		}
        function handleSearchSku(sku){
			visable.value=true;
		     var param=JSON.parse(JSON.stringify(queryParam));
		     param.searchtype="sku";
			 param.search=sku;
			 param.groupid="";
			 param.isparent="";
			 param.marketplaceid="";
			 var timeout= setTimeout(function(){
				 tableSkuRef.value.loadData(param);
				 clearTimeout(timeout);
			 },300);
			  
		}
		function soltChange(val){
			currentSolt.value =val;
			queryParam.sort=currentRank.value;
			queryParam.order=val;
			refreshTable();
		}
		function getGroup(obj){
			queryParam.groupid=obj.groupid;
			queryParam.marketplaceid=obj.marketplaceid;
			queryParam.searchtype=searchtype.value;
			queryParam.isfba=isfba.value;
			if(remark.value && remark.value!=""){
				queryParam.remark=remark.value;
			}
			if(radio2.value!="ASIN"){
				queryParam.isparent="isparent";
			}
			queryParam.disable=disabletype.value;
			queryParam.name=proname.value;
      syncForm.selectmarket=obj.market.name;
      syncForm.selectgroup=obj.group.name;
      syncForm.groupid=obj.groupid;
      syncForm.marketplaceid=obj.marketplaceid;
			refreshTable();
			isload=false;
		}
		function getOwner(id){
			queryParam.owner=id;
			if(isload==false){
				refreshTable();
			}
		}
		function getStatus(status){
			queryParam.salestatus=status;
			if(isload==false){
				refreshTable();
			}
		}
		function searchTypeChange(val){
			searchtype.value=val;
			queryParam.searchtype=val;
			if(isload==false){
				refreshTable();
			}
		}
		function disabletypeChange(val){
			disabletype.value=val;
			queryParam.disable=val;
			if(isload==false){
				refreshTable();
			}
		}
		function changeKeywords(val){
			searchKeywords.value=val;
			queryParam.search=val;
			if(isload==false){
				refreshTable();
			}
		}
		function searchConfirm(){
			refreshTable();
		}
		function getCategory(category){
			queryParam.category=category;
			moreSearchVisible.value=true;
		}
		function changeTag(tags){
			queryParam.taglist=tags;
		}
		function submitSearch(){
			queryParam.changeRate=salerangecheck.value;
			queryParam.isfba=isfba.value;
		    queryParam.remark=remark.value;
			queryParam.isbadreview=isbadreview.value;
			queryParam.name=proname.value;
			if(queryParam.changeRate!=""||
			   queryParam.isfba!=""||
			   queryParam.name!=""||
			   queryParam.remark!=""
			){
				filterBtnColor.value="#ff6700"
			}else if(queryParam.category!=""&&queryParam.category!=undefined){
				filterBtnColor.value="#ff6700"
			}else if(queryParam.taglist!=''&&queryParam.taglist!=undefined){
				filterBtnColor.value="#ff6700"
			}else{
				filterBtnColor.value=""
			}
			refreshTable();
			moreSearchVisible.value=false;
		}
		function resetSearch(){
			moreSearchVisible.value=false;
		}
		function refreshTable(){
			tableRef.value.loadData(queryParam);
		}
		function EditSaleFormula(){
			SaleFormulaRef.value.show();
		}
	 
		function submitDataForm(){
			queryParam.paralist=columnval.value;
			searchConfirm();
			dataSearchVisible.value=false;
		}
		function resetDataForm(){
			dataSearchVisible.value=false;
		}
		function addColumn(){
			var data1="";var data2="";var data3=dataValue.value;
			if(data3=="" || data3==undefined ||data3==null || data3=="undefined"){
				ElMessage.error('请输入正确的值！');
				return;
			}
			if(dataColumn.value=="v.advacos"){
				data1="ACOS";
			}
			if(dataColumn.value=="v.advctr"){
				data1="CTR";
			}
			if(dataColumn.value=="v.acoas"){
				data1="ACOAS";
			}
			if(dataColumn.value=="v.advspc"){
				data1="CR";
			}
			if(dataColumn.value=="v.sessionrate"){
				data1="七日转化率";
			}
			if(dataColumn.value=="v.proprate"){
				data1="净利润率";
			}
			if(dataColumn.value=="v.dayfulfilla"){
				data1="可售天数";
			}
			if(dataColumn.value=="v.afn_fulfillable_quantity"){
				data1="库存";
			}
			if(dataColumn.value=="v.profitrate"){
				data1="产品利润率";
			}
			if(mark.value==3){
				data2=">";
			}
			if(mark.value==9){
				data2="=";
			}
			if(mark.value==6){
				data2="<";
			}
			columntext.value=columntext.value+(data1+data2+data3+";");
			columnval.value=columnval.value+(dataColumn.value+data2+data3+",");
		}
		function clearColumn(){
			columntext.value="";
			columnval.value="";
		}
		function showRefreshDialog(){
			if(selection.value&&selection.value.length>0){
				selection.value.forEach(function(item){
					 tableRef.value.refreshProduct(item);
				});
			}else{
				refreshRef.value.show(queryParam.groupid,queryParam.marketplaceid);
			}
		}
    function showSyncDialog(){
      if(selection.value&&selection.value.length>0){
        syncVisable.value=true;
      }else{
        ElMessage.error('至少选择一行数据！');
      }
    }

		function showOwnerDialog(){
			if(selection.value&&selection.value.length>0){
				 ownerVisable.value=true;
				 localPid=null;
				 ownerId.value=null;
				 nextTick(()=>{
				 		ownerOptRef.value.initDefaultValue();
				 })
			}else{
				 ElMessage.error('至少选择一行数据！');
			}
		}
		function submitStatus(){
			var pids="";
			selection.value.forEach(function(item){
				 pids=pids+item.id+",";
			});
			productinoptApi.updateOptStatus({"pid":pids,"status":statusid.value}).then((res)=>{
				if(res.data=="ok"){
				    ElMessage.success('操作成功！');
					statusVisable.value=false;
					searchConfirm();
				}else{
					ElMessage.error("操作失败！");
				}
			});
		}
		
		function showStatusDialog(){
			if(selection.value&&selection.value.length>0){
				 statusVisable.value=true;
				 productinfoApi.getProStatusList().then((res)=>{
				 	if(res.data&&res.data.length>0){
				 		statusList.value=res.data;
						statusid.value=res.data[0].id;
				 	}
				 })
			}else{
				 ElMessage.error('至少选择一行数据！');
			}
		}
		
		function showPriceDialog(){
			if(selection.value&&selection.value.length>0){
				 modifypriceRef.value.ismore=true;
				 modifypriceRef.value.tableData=selection.value;
				 modifypriceRef.value.morePrice=selection.value[0].landedAmount;
				 modifypriceRef.value.modifyPriceVisable = true
			}else{
				 ElMessage.error('至少选择一行数据！');
			}
		}
		
		function showFeeDialog(){
			feeVisable.value=true;
		}
		
		function showOwner(pid,ownerid){
			ownerVisable.value=true;
			ownerId.value=ownerid;
			localPid=pid;
			nextTick(()=>{
					ownerOptRef.value.ownerid=ownerid;
			})
	
		}

    function refreshSync(){
      var pidss=[];
      selection.value.forEach(function(item){
        pidss.push(item.id);
      });
      syncForm.pidlist=pidss;
      console.log(syncForm);
      initProductTask(syncForm).then((res)=>{
        if(res.data){
          ElMessage.success('操作成功！');
          syncVisable.value=false;
        }else{
          ElMessage.error('操作失败！');
        }
      })

    }
		
		function refreshOwner(){
			if(localPid==null){
				var list=[];
				selection.value.forEach(function(item){
					list.push(item.id);
				});
				productinoptApi.updateOptOwner(ownerId.value,activeOwner.value,list).then((res)=>{
					 if(res.data){
						 refreshTable();
						 ElMessage.success('更新成功！');
						 ownerVisable.value=false;
					 }else{
						 ElMessage.error('更新失败！');
					 }
				})
			}else{
				var lists=[localPid];
				productinoptApi.updateOptOwner(ownerId.value,activeOwner.value,lists).then((res)=>{
					 if(res.data){
						 refreshTable();
						 ElMessage.success('更新成功！');
						 ownerVisable.value=false;
					 }else{
						 ElMessage.error('更新失败！');
					 }
				})
			}
			
		}
		
		function refreshFBAInv(){
			//批量刷新fba库存 要锁定一个店铺 一个站点。。
			if(queryParam.groupid=="all" || queryParam.groupid=="" || queryParam.marketplaceid=="all" || queryParam.marketplaceid==""){
				ElMessage.error('批量刷新FBA库存需确定一个店铺下的某个站点！')
				return;
			}
			if(selection.value&&selection.value.length>0){
				var skus="";
				selection.value.forEach(function(item){
					skus=skus+item.sku+",";
				});
				skus=skus.substring(0,skus.length-1);
				inventoryRptApi.syncInventorySupply({"skus":skus,"groupid":queryParam.groupid,"marketplaceid":queryParam.marketplaceid}).then((res)=>{
					 if(res.data){
						 //rows.afnFulfillableQuantity=res.data.afnFulfillableQuantity;
						 refreshTable();
						 ElMessage.success('更新成功！');
						//rows.itemshow=false;
					 }else{
						 ElMessage.error('更新失败！');
					 }
				})
			}else{
				 ElMessage.error('至少选择一行数据！');
			}
			
		}
		function handleToOutSearch(){
			router.push({
				path:'/amazon/listing/catalog',
				query:{
				  title:"商品搜索",
				  path:'/amazon/listing/catalog',
				}
			})
		}
		function clearSearch(){
			disabletype.value="false";
			salerangecheck.value="";
			isfba.value="";
			proname.value="";
			remark.value="";
			searchtype.value="sku";
			searchKeywords.value="";
			statusRef.value.reset();
			ownerRef.value.reset();
			tagsRef.value.reset();
			cateRef.value.reset();
			isbadreview.value=false;
			groupRef.value.getGroupData();
			queryParam.paralist=null;
			queryParam.searchtype="sku";
			queryParam.isfba="";
			queryParam.remark="";
			queryParam.isparent=null;
			queryParam.disable="false";
			queryParam.name=null;
			queryParam.search="";
			queryParam.taglist=null;
			queryParam.category=null;
			queryParam.ownerid=null;
			queryParam.owner=null;
			submitSearch();
		}
		
 
</script>

<style>
	.skusearchdialog .el-dialog__body{
		padding:0 0 10px 0;
	}
	.skusearchdialog {
			 margin: var(--el-dialog-margin-top,15vh) auto 10px !important;
	}
	.r_active{
		background-color: var(--el-dropdown-menuItem-hover-fill);
	    color: var(--el-dropdown-menuItem-hover-color);
		}
	.sortdropdowntitle li{
		padding:5px;
		border-bottom:solid 1px #dedede;
	}
	.sortdropdown li{
		padding:10px;
		cursor: pointer;
	}
	.sortdropdown li:hover{
		color: var(--el-dropdown-menuItem-hover-color);
	}
	.radioWrapper .el-radio-group{
		flex-wrap: nowrap;
	}
</style>
