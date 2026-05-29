<template>
	<div class="ama-rec">
		<el-table :data="productData.list" border  show-summary >
			<el-table-column prop="image" label="图片" width="70">
				<template #default="scope">
					<img :src="scope.row.image" style="width:40px;height:40px"  />
				</template>
			</el-table-column>
			<el-table-column prop="sku" label="名称/SKU" min-width="200"  show-overflow-tooltip>
				<template #default="scope">
					<div class='name'>{{scope.row.pname}}</div>
					<div class='sku'>{{scope.row.sellersku}} </div>
				</template>
			</el-table-column>
		  <el-table-column prop="quantityshipped" label="已发出" width="100"  />
		  <el-table-column prop="quantityreceived" label="已接收" width="100"  />
		  <el-table-column prop="sellersku" label="状态"  width="120"   >
			<template #default="scope">
						<div v-if="scope.row.quantityreceived>0 && scope.row.quantityshipped==scope.row.quantityreceived">
								  <el-tag type="success">完成</el-tag>
						</div>
				  <div v-else>
				  	    <el-tag type="warning">待接收</el-tag> 
				  </div>
		      </template>
		  </el-table-column>
		</el-table>
	</div>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,computed} from 'vue'
			let productData=reactive({list:[]})
			function loadOptData(datas){
				productData.list=datas;
			}
	 	    defineExpose({loadOptData})
</script>

<style>
	.ama-rec .el-table{
		width:100%;
	}
</style>
