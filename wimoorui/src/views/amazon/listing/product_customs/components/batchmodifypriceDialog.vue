<template>
	<el-dialog v-model="priceVisable" title="批量改价" destroy-on-close='true' width="800px"  >
	  <el-table :data="tableData" border>
		  <el-table-column label="图片" width="60">
			  <template #default = "scope">
			  <el-image :src="scope.row.image"></el-image>
			  </template>
		  </el-table-column>
		  <el-table-column label="名称/SKU">
			  <template #default = "scope">
			    <div>{{scope.row.name}}</div>
				<div class="sku">{{scope.row.sku}}</div>
			  </template>
		  </el-table-column>
		  <el-table-column label="售价" width="100">
			  <template #default="scope">
				  <el-input v-model="scope.row.price"></el-input>
			  </template>
		  </el-table-column>
	  </el-table>
	  <template #footer>
	  	<span class="dialog-footer">
	  		<el-button @click="priceVisable = false">取消</el-button>
	  		<el-button type="primary" @click="">提交</el-button>
	  	</span>
	  </template>
	</el-dialog>
</template>

<script>
	import {ref,reactive,onMounted} from "vue"
	export default{
		setup(){
			let priceVisable =ref(false)
			return{
				priceVisable,
			}
		}	
	}
</script>

<style>
</style>
