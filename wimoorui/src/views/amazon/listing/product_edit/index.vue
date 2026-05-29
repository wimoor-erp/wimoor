<template>
	<div class="main-sty" >
		<div class="con-header" style="">
			<el-space>
        <el-select v-model="queryParams.locale" placeholder="站点语言" style="width: 95px">
          <el-option label="站点语言" value=""></el-option>
          <el-option label="中文" value="zh_CN"></el-option>
          <el-option label="英文" value="en_US"></el-option>
        </el-select>
			 <Group ref="groupRef"  @change="groupChange" defaultValue="only" ></Group>
			 </el-space>
		</div>
		<div v-loading="loading" class="con-body" style="padding-top:20px; padding-right:60px">
		 <el-form label-width="auto" style="max-width: 600px">
				 <el-form-item label="产品类型" >
					<el-select
					  v-model="productType"
					  filterable
					  remote
					  reserve-keyword
					  @change="handleGetType"
					  :remote-method="remoteMethod"
					  :loading="loading"
					  placeholder="类型"
					  style="width: 240px"
					>
					  <el-option
					    v-for="item in productTypes"
					    :key="item.name"
					    :label="item.displayName"
					    :value="item.name"
					  />
					</el-select>
				 </el-form-item>
				 <el-form-item label="SKU" >
					 <el-input v-model="sku"  need="language_tag" abort=""placeholder="SKU"></el-input>
				 </el-form-item>
				<el-form-item label="标题" >
				    <el-input v-model="formData.brand"  need="language_tag" abort=""placeholder="商品的品牌或制造商"></el-input>
				</el-form-item>
				<el-form-item label="品牌" >
				    <el-input v-model="formData.item_name" need="language_tag" placeholder="提供可能面向顾客的产品标题"></el-input>
				</el-form-item>
				<el-form-item label="描述" >
					<el-input v-model="formData.brand" need="language_tag" placeholder="商品的文本描述"></el-input>
				</el-form-item>
				<el-form-item label="商品特性" >
					<el-input v-model="formData.bullet_point" need="language_tag" placeholder="简短的描述性文本，从某几个具体的角度，通过列举商品特性对商品进行说明"></el-input>
				</el-form-item>
				<el-form-item label="描述" >
					<el-input v-model="formData.product_description" need="language_tag" placeholder="商品的文本描述"></el-input>
				</el-form-item>
				<el-form-item label="提供条件类型" >
					<el-select v-model="formData.condition_type"    placeholder="提供产品的实际条件类型">
					  <el-option value="new_new" label="新"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item v-if="schema&&schema.properties" label="危险商品规管" >
					<el-select v-model="formData.supplier_declared_dg_hz_regulation"  placeholder="如果产品是受运输、储存和/或废物管制的危险商品或危险材料、物质或废物，请从有效值表中进行选择">
						<el-option value="new_new" v-for="(item,index) in schema.properties.supplier_declared_dg_hz_regulation.items.properties.value.enum" 
						:label="schema.properties.supplier_declared_dg_hz_regulation.items.properties.value.enumNames[index]"
						:value="item"></el-option>
					</el-select>
				</el-form-item>
				<div class="flex-between">
					<div></div>
				<el-button type="primary" @click="handlerSubmit(formData)">提交</el-button>
				</div>
			</el-form>
	    </div>
	</div>

</template>

<script setup>
	import {ref,reactive,onMounted,toRefs, nextTick } from "vue";
	import {DocumentAdd} from '@element-plus/icons-vue';
	import {ElMessage} from 'element-plus';
	import { useRoute,useRouter } from 'vue-router';
	import Group from '@/components/header/group.vue';
	import productTypeApi from '@/api/amazon/listing/productTypeApi.js';
	import listingApi from '@/api/amazon/listing/listingApi.js';
	import {schemaTran} from "@/hooks/view/schema.js";
    const vueFormRef=ref();
	let state=reactive({
				queryParams:{"locale":"zh_CN"},
				data:{},
				formData:{},
				activeName:"Offer",
				productType:"",
				productTypes:[],
				productTypeDefinition:{},
				loading:false,
				dialogVisible:false,
				json:{},
				sku:"",
				schema:{},
				uiSchema:{},
				formSchema:{},
	});
	const {
	  queryParams,productType,productTypes,productTypeDefinition,json,dialogVisible,
	  data,loading,schema,formData,activeName,formSchema,uiSchema,sku
	} = toRefs(state);
	const formFooter ={
		    show: true, // 是否显示默认底部
		    okBtn: '保存', // 确认按钮文字
		    okBtnProps: { type: 'primary' }, // 传递确认按钮的 props，例如配置按钮 loading 状态 okBtnProps: { loading: true }
		    cancelBtn: '取消', // 取消按钮文字
	}
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		loadProductTypes();
	}
  function handleClick(){
    getSchema();
  }
	function remoteMethod(value){
		state.queryParams.keywords=[];
		state.queryParams.keywords.push(value);
		loadProductTypes();
	}
	function loadProductTypes(){
		state.loading=true;
        state.queryParams.searchLocale=state.queryParams.locale;
		productTypeApi.searchDefinitionsProductTypes(state.queryParams).then(res=>{
			state.loading=false;
	        state.productTypes=res.data.productTypes;
		});
	}
  function getSchema(){
      
  }
	function getSchemaJson(url){
		state.loading=true;
		productTypeApi.getSchemaJson(url).then(res=>{
		    state.schema=JSON.parse(res.data);
			nextTick(()=>{
			state.loading=false;
			})
            state.activeName="offer";
            getSchema();
		});
	}
	function handleGetType(){
		  var param={"groupid":state.queryParams.groupid,"marketplaceid":state.queryParams.marketplaceid,"locale":"zh_CN"};
		  param.itemName=state.productType;
		  productTypeApi.getProductDefinition(param).then(res=>{
		  state.productTypeDefinition=res.data;
          getSchemaJson(state.productTypeDefinition.schema.link.resource)
	    });
	}
   function handlerSubmit(formData){
	   var country_of_origin={'value':"CN",'marketplace_id':"",}
		var param={"groupid":state.queryParams.groupid,
		           "marketplaceids":[state.queryParams.marketplaceid],
				   "sku":state.sku,
				   "productType":state.productType,
				   "attributes":state.formData};
			listingApi.putListingsItem(param).then(res=>{
				 console.log(res);
			});
   }
	onMounted(()=>{
	 
	})
</script>

<style>
	.formFooter_item .el-form-item__content{
		text-align: center;
	}
	.dark .genFromComponent .appendCombining_box {
		background-color: #2f2f2f;
	}
</style>