<template>
	<el-row class="el-white-bg">
		<el-col :span="4" >
			  <el-scrollbar height="calc(100vh - 56px)" style="border-right:1px solid var(--el-border-color-light);">
	      <div class='left-content'>
			  <div class='gird-body'>
	         <ShelfTree ref="leftSearchTree"  isleaf="true" @node-click="treeSeletecd"/>
			</div>
	      </div>
		  </el-scrollbar>
	   </el-col>
	   <el-col :span ='20'>
		     <ShelfProduct ref='ShelfProductRef' />
	   </el-col>
	</el-row>
</template>

<script>
import { ref ,watch,reactive,onMounted} from 'vue'
import ShelfTree from "@/views/erp/warehouse/shelf/base/shelf_tree.vue";
import ShelfProduct from "./components/shelf_product.vue";
export default{
	name: '库位库存',
	components: {ShelfTree,ShelfProduct},
	setup(){
	const ShelfProductRef =ref()
	//方法
	//选中树节点
	function treeSeletecd(data){
		ShelfProductRef.value.loadProduct(data)
	}
	return{
		treeSeletecd,
		ShelfProductRef,
	}
	}
}
</script>

<style>
	.left-content{
		height:100%;
	}
</style>
