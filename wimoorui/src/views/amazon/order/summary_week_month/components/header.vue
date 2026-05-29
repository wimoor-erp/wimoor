<template>
	<div class="con-header" >
	  <el-row>
	    <el-space >
	  <el-select v-model="store"  @change="changeGroup"  placeholder="全部店铺">
	        <el-option  v-for="item in storeList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	  <el-select v-model="country" @change="refreshTable" placeholder="全部国家">
	        <el-option  v-for="item in countryList"   :key="item.pointName"  :label="item.name" :value="item.pointName"   >
	        </el-option>
	  </el-select>
	  <el-select v-model="orderType" @change="refreshTable" placeholder="全部状态" clearable >
	        <el-option  v-for="item in orderTypeList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	    <el-select v-model="customerType" @change="refreshTable" placeholder="客户类型" clearable >
	        <el-option  v-for="item in customerTypeList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	  <el-select v-model="shipType" @change="refreshTable" placeholder="发货方式" clearable >
	        <el-option  v-for="item in shipTypeList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	   <el-input  v-model="searchKeywords" placeholder="请输入" class="input-with-select" >
	      <template #prepend>
	        <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="ASIN" value="asin"></el-option>
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
	    <el-button>重置</el-button>
	  </el-space>
	  <div class='rt-btn-group'>
	  <el-button class='ic-btn'  title='列配置'>
	     <setting-two theme="outline" size="16"  :strokeWidth="3"/>
	  </el-button>
	   <el-button   class='ic-btn' title='帮助文档'>
	    <help theme="outline" size="16" :strokeWidth="3"/>
	  </el-button>
	  </div>
	  </el-row>
	</div>
</template>

<script>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {h, ref ,watch,reactive,onMounted} from 'vue'
	export default{
		name:'Header',
		components:{
			MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,Search,ArrowDown,
			ChartPie,
		},
		setup(){
			return{
				
			}
		}
		}
</script>

<style>
	
</style>
