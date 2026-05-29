<template>
	<div class="main-sty">
	<!--  头部搜索区域 -->
	<div class="con-header" >
	     <Header @getdata="loadData" ref="headerRef"  type="product"/>
	</div>
	<el-row>
	   <GlobalTable ref="globalTable" 
	   :defaultSort="defaultSort"
		@selectionChange='selectChange' 
		:tableData="tableData"   
		 :rowClassName="handleRowClassName"
		 @loadTable="loadTableData" 
		 @rowClick="handleRowClick"
		  height="calc(100vh - 250px)"  
		 :stripe="true"  style="width: 100%;margin-bottom:16px;">
		   <template #field>
	     <el-table-column fixed type="selection" width="38" />
	      <el-table-column fixed prop="image" label="图片" width="64" >
	       <template #default="scope">
	       
			<el-popover
			    placement="top-start"
			    :width="200"
			    trigger="hover"
				:persistent="false"
				v-if="scope.row.image"
			  >
			    <template #reference>
			      <img :src="scope.row.image"  style="width:40px;height:40px;padding:0px;"   />
			    </template>
				<el-image  :src="scope.row.image"    ></el-image>
			  </el-popover>

			<el-image v-else :src="$require('empty/noimage40.png')"   width="40" height="40"  ></el-image>
	      </template>
	    </el-table-column>
	    <el-table-column fixed prop="sku" label="名称/SKU" sortable="custom" min-width="240" show-overflow-tooltip>
	       <template #default="scope">
	          <div class='name'>{{scope.row.name}}  
			  </div>
	          <div class='sku'>{{scope.row.sku}}
	            <copy class="" @click.stop="CopyText(scope.row.sku)" title='复制SKU' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
	          </div>
	      </template>
	    </el-table-column>
		<el-table-column prop="createdate"  label="生效日期"  width="135" sortable="custom" >
					 <template #default="scope">
						<div>{{dateFormat(scope.row.createdate)}}</div>
						<div v-if="scope.row.upc" class="font-extraSmall">条码:{{scope.row.upc}}</div>
					 </template>
				</el-table-column>
	   <el-table-column prop="category"  label="品类" width="135" sortable="custom" >
			 <template #default="scope">
				<div>{{NullTransform(scope.row.category)}}</div>
				<div v-if="scope.row.specification" class="font-extraSmall">规格:{{scope.row.specification}}</div>
			 </template>
		</el-table-column>
	    <el-table-column prop="weight" label="重量(kg)"  width="120" sortable="custom" >
			<template #default="scope">
				<div v-if="scope.row.weight">{{NullTransform(scope.row.weight)}}</div>
				<div v-else>-</div>
				<div class="font-extraSmall" v-if="scope.row.length">{{scope.row.length}}*{{scope.row.width}}*{{scope.row.height}} cm</div>
			 </template>
		</el-table-column>
	    <el-table-column prop="price" label="采购成本" width='100'  sortable="custom">
		  <template #default="scope">
			  <el-popover
			      placement="top"
			      title="阶梯采购价"
			      :width="200"
				  trigger="click"
				  @before-enter="getWisePriceList(scope.row)"
			    >
			      <template #reference>
			       <span v-if="scope.row.price" class="pointer">{{scope.row.price}}</span> 
				   <span v-else >-</span> 
			      </template>
			  	<el-table :data="pricelist" v-loading="loading">
			  		<el-table-column label="起订量" prop="amount"></el-table-column>
			  		<el-table-column label="单价" prop="price"></el-table-column>
			  	</el-table>
			    </el-popover>
		  </template>
	    </el-table-column>
	    <el-table-column prop="supplier" label="供应商" min-width='200'  sortable="custom">
			<template #default="scope">
				<el-link v-if="scope.row.supplier" @click.stop="openPurchase(scope.row.purchaseUrl)" >{{scope.row.supplier}}</el-link>
				<el-link v-else>-</el-link>
			    <div ><span class="font-extraSmall">供货周期：</span>{{NullTransform(scope.row.delivery_cycle)}}</div>
			</template>	
		</el-table-column>	
	    <el-table-column prop="fulfillable" label="可用库存" width="100"   sortable="custom">
		<template #default="scope">
			<el-popover
			    placement="top"
			    title="产品库存"
			    :width="250"
			    trigger="click"
				@before-enter="loadInventory(scope.row)"
			  >
			    <template #reference>
			     <span class="pointer">{{scope.row.fulfillable}}</span> 
			    </template>
				 <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
				    <el-tab-pane label="本地仓" name="local">
						<el-table :data="inventoryList" >
							<el-table-column label="仓库名称" width="140" prop="name"></el-table-column>
							<el-table-column label="库存" prop="quantity"></el-table-column>
						</el-table>
					</el-tab-pane>
				    <el-tab-pane label="海外仓" name="oversea">
						<el-table :data="overseainventoryList" >
							<el-table-column label="仓库名称" width="140" prop="name"></el-table-column>
							<el-table-column label="库存" prop="quantity"></el-table-column>
						</el-table>
					</el-tab-pane>
				  </el-tabs>
				
			  </el-popover>
		</template>
		 </el-table-column>
	    <el-table-column prop="name" class-name="editable-cell" label="标签" width="150" >
	       <template #default="scope">
	         <span class='tag-mr' v-if='scope.row.TagNameList'  v-for='(s,index) in scope.row.TagNameList' :key='index'>
	             <el-tag size="small"  effect="plain" :title="s.name" :type="s.color">  {{s.name}}</el-tag>
	         </span>
			 <span v-else>-</span>
			<span class="edit-text" @click.stop="editTags(scope.row)" :class="scope.row.active">修改</span>
	       </template>
	    </el-table-column>
	    <el-table-column prop="remark" label="备注"  show-overflow-tooltip sortable="custom" >
			 <template #default="scope">
				 {{NullTransform(scope.row.remark)}}
			</template>
		</el-table-column>
	    <el-table-column fixed="right" prop="operate" label="操作"  width="135" >
	        <template #default="scope">
	          <el-space>
	            <el-button type="primary" link @click.stop="productDetails(scope.row)">详情</el-button>
	            <el-button type="primary" v-if="!scope.row.isDelete" link @click.stop="productEdit(scope.row)">编辑</el-button>
	            <el-dropdown v-if="!scope.row.isDelete"  :hide-on-click="false">
	              <span class="el-dropdown-link">
	                <more-one class="ic-cen" theme="outline" size="16" fill="#333" :strokeWidth="3"/>
	              </span>
	               <template #dropdown>
	                <el-dropdown-menu>
	                  <el-dropdown-item @click.stop="deleteInfo(scope.row)">停用</el-dropdown-item>
	                </el-dropdown-menu>
	            </template>
	            </el-dropdown>
		             <el-button type="primary" v-else link @click.stop="recoverRows(scope.row)">启用</el-button>
	          </el-space>
	        </template>
	    </el-table-column>
	  </template>
	  </GlobalTable>
	</el-row>
	</div>
	
	<el-dialog v-model="markVisable" title="编辑标签" width="600px">
		  <el-space>
		  <span>标签</span>
		  <el-cascader v-model="tagsValue" placeholder="请选择标签" :options="tagsList"  @change="changeTags" :popper-append-to-body="false" :props="markprops" clearable />
		 </el-space>
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="markVisable = false">取消</el-button>
				<el-button @click.stop="Addtag">添加标签</el-button>
		  		<el-button type="primary" @click.stop="submitTag">提交</el-button>
		  	</span>
		  </template>
	</el-dialog>
	
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,inject,nextTick} from "vue"
	import {Copy,MenuUnfold,Plus,SettingTwo,Help,MoreOne} from '@icon-park/vue-next';
	import Header from "./components/header.vue"
	import {useRouter } from 'vue-router'
	import { ElMessage, ElMessageBox } from 'element-plus'
	import CopyText from"@/utils/copy_text.js";
	import NullTransform from"@/utils/null-transform";
	import materialApi from '@/api/erp/material/materialApi.js';
	import {getAllTags} from '@/api/sys/admin/tag.js';
	import {dateFormat} from "@/utils/index";
	import inventoryApi from '@/api/erp/inventory/inventoryApi.js';
	const emitter = inject("emitter");
	const headerRef=ref();
	let router = useRouter()
	const globalTable=ref();
	let state = reactive({
		activeName:'local',
		loading:true,
		tagsList:[],
		tableData:{records:[],total:0},
		markprops:{ multiple: true },
		tagsValue:[],
		checkTags:'',
		nowTagProRow:{},
		markVisable:false,
		inventoryList:[],
		overseainventoryList:[],
		pricelist:[],
		queryParams:{supplier:""},
		defaultSort:{"prop": 'opttime', "order": 'descending' }
		})
		let {loading,tableData,tagsList,markprops,tagsValue,checkTags,nowTagProRow,markVisable,queryParams,defaultSort,
		inventoryList,pricelist,activeName,overseainventoryList}=toRefs(state)
		  function handleRowClick(row,field){
			  if(field.property=="image"||field.property=="sku"
			  ||field.property=="category"||field.property=="createdate"
			  ||field.property=="supplier"||field.property=="remark"){
				  router.push({
				  	path:'/material/details',
				  	query:{
				  	  title:"产品详情",
				  	  path:'/material/details',
					  details:row.id,
					  type:"product"
				  	}
				  });
			  }
		  }
		  function productDetails(row){
			  router.push({
			  	path:'/material/details',
			  	query:{
			  	  title:"产品详情",
			  	  path:'/material/details',
				  details:row.id,
				  type:"product"
			  	}
			  })
		  }
		  function productEdit(row){
			     emitter.emit("removeCache", "编辑产品"+row.sku);
				 router.push({
				 	path:'/localproduct/editinfo',
				 	query:{
				 	  title:"编辑产品"+row.sku,
				 	  path:'/localproduct/editinfo',
				 	  details:row.id,
					  type:"product"
				 	}
				 }) 
		  }
		  function handleClick(){
			  
		  }
function submitTag(row){
	var mid=state.nowTagProRow.id;;
	var ids=state.checkTags;
	materialApi.saveMaterialTags({"mid":mid,"ids":ids}).then((res)=>{
			ElMessage.success('操作成功！');
			state.markVisable = false;
			headerRef.value.submitQuery();
	});
	
}
function Addtag(){
	router.push({
		path:'/sys/tags',
		query:{
		  title:"产品标签",
		  path:'/sys/tags',
		}
	})
}

function loadData(params){
	state.queryParams=params;
	state.queryParams.sort=state.defaultSort.prop;
	state.queryParams.order=state.defaultSort.order;
	state.queryParams.mtype="product";
	nextTick(()=>{
	globalTable.value.loadTable(params);
	});
	
	
}
function loadTableData(params){
	materialApi.getMaterialList(params).then((res)=>{
		state.tableData.records = res.data.records
		state.tableData.total =res.data.total
	})
}
function editTags(rows){
	state.nowTagProRow=rows;
	getAllTags().then(res => {
		state.tagsList=res.data;
		//在通过id去找 选了哪些
		var arrs=[];
		materialApi.findMaterialTags({"mid":rows.id}).then((ress)=>{
			  if(ress.data && ress.data!=""){
				  var strs=ress.data.toString();
				  var list=strs.split(",");
				  list.forEach(function(item){
					  arrs.push(item);
				  });
				   state.tagsValue=arrs;
			  }else{
				   state.tagsValue=[];
			  }
		});
	});
	state.markVisable =true;
}
function changeTags(tags){
		 if(tags){
			 var items="";
			  tags.forEach(function(item){
				  items+=(item[1]+",");
			  });
			  state.checkTags=items;
		 }else{
			 state.checkTags="";
		 }
}
function loadInventory(row){
	state.loading =true;
	inventoryApi.getInventoryByMaterial({"mid":row.id}).then((res)=>{
		var lsit1=[];
		var lsit2=[];
		if(res.data && res.data.length>0){
			res.data.forEach(item=>{
				if(item.ftype=='oversea_usable'){
					lsit2.push(item);
				}else{
					lsit1.push(item);
				}
			});
			state.overseainventoryList=lsit2;
			state.inventoryList=lsit1;
		}
		state.loading =false;
	});
}
function getWisePriceList(row){
	state.loading =true;
	materialApi.getWisePriceList({"mid":row.id}).then((res)=>{
		state.pricelist=res.data;
			state.loading =false
	});
}
function openPurchase(url){
	 window.open(url, '_blank');
}
function deleteInfo(row){
	materialApi.deleteData({"ids":row.id}).then((res)=>{
		if(res.data){
			ElMessage.success('删除成功！');
			loadData(state.queryParams);
		}
	})
}
   function handleRowClassName({row,rowindex}){
	   if(row.issfg=="0"){
	   		   return 'dandu';
	   } else if(row.issfg=="1"){
		      return 'zuhe';
	   }else if(row.issfg=="2"){
		   return 'daizu';
	   }
	  
   }
function recoverRows(row){
		if(row.isDelete==true || row.isDelete==1){
			materialApi.recoverData({"id":row.id,"sku":row.sku}).then((res)=>{
				if(res.data){
					ElMessage.success(res.data);
					loadData(state.queryParams);
				}
			})
		}
}
function selectChange(selection) {
	 headerRef.value.selectRows=selection;
}

</script>
<style >
	.el-dropdown-link .i-icon svg{vertical-align:text-top;}
	.tag-mr{margin-right:4px;margin-bottom:4px;display:inline-block}
	.sku .i-icon-copy{cursor:pointer;opacity: 0;}
	.el-table  tr:hover>td.el-table__cell .sku .i-icon-copy{opacity: 1;transition: opacity .4s; }
	.scoped-form .el-form-item--default{
		margin-bottom:0px;
	}
	.scoped-form .el-form-item__label{
		color: #999;
	}
	.footer-right{ 
		margin-top:16px;
		text-align: right;
	}
	.font-12{
		font-size:12px;
	}
	.el-table__row.daizu  td:last-child{
		background: url(@/assets/image/pages/material/chanpin_daizu.png) no-repeat top right !important;
		background-color: #fff!important;
	}
	
	.el-table__row.dandu  td:last-child{
		background: url(@/assets/image/pages/material/chanpin_dandu.png) no-repeat top right !important;
		background-color: #fff!important;
	}
	
	.el-table__row.zuhe td:last-child{
		background: url(@/assets/image/pages/material/chanpin_zuhe.png) no-repeat top right !important;
		background-color: #fff!important;
	}
	.dark .el-table__row.daizu  td:last-child{
		background: url(@/assets/image/pages/material/chanpin_daizu.png) no-repeat top right !important;
		background-color: #000!important;
	}
	
	.dark .el-table__row.dandu  td:last-child{
		background: url(@/assets/image/pages/material/chanpin_dandu.png) no-repeat top right !important;
		background-color: #000!important;
	}
	
	.dark .el-table__row.zuhe td:last-child{
		background: url(@/assets/image/pages/material/chanpin_zuhe.png) no-repeat top right !important;
		background-color: #000!important;
	}
	.el-table__row.el-table__row--striped.zuhe td:last-child {
		background: url(@/assets/image/pages/material/chanpin_zuhe.png) no-repeat top right !important;
	    background-color: var(--el-fill-color-lighter) !important;
	}
	.el-table__row.el-table__row--striped.dandu  td:last-child{
		background: url(@/assets/image/pages/material/chanpin_dandu.png) no-repeat top right !important;
		background-color: var(--el-fill-color-lighter) !important;
	}
	.el-table__row.el-table__row--striped.daizu  td:last-child{
		background: url(@/assets/image/pages/material/chanpin_daizu.png) no-repeat top right !important;
		background-color: var(--el-fill-color-lighter) !important;
	}
	.el-table__row.el-table__row--striped.hover-row.zuhe td:last-child, 
	.el-table__row.hover-row.zuhe td:last-child{
		background: url(@/assets/image/pages/material/chanpin_zuhe.png) no-repeat top right !important;
	    background-color: var(--el-table-row-hover-bg-color)!important;
	}
	.el-table__row.el-table__row--striped.hover-row.dandu  td:last-child,
	.el-table__row.hover-row.dandu  td:last-child
	{
		background: url(@/assets/image/pages/material/chanpin_dandu.png) no-repeat top right !important;
		background-color: var(--el-table-row-hover-bg-color)!important;
	}
	.el-table__row.el-table__row--striped.hover-row.daizu  td:last-child,
	.el-table__row.hover-row.daizu  td:last-child
	{
		background: url(@/assets/image/pages/material/chanpin_daizu.png) no-repeat top right !important;
		background-color: var(--el-table-row-hover-bg-color)!important;
	}
	
 
</style>

 
