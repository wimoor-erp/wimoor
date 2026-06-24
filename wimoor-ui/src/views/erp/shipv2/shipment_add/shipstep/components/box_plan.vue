<template>
	<el-dialog
	title="装箱"
	v-model="visiable"
	width="80%"
	top="1vh"
	>
	<div class="con-body"  >
		<table class="mytable">
			<thead>
				<tr class="el-table__header">
					<th>图片</th>
					<th>名称/SKU</th>
					<th>长</th>
					<th>宽</th>
					<th>高</th>
					<th>重量(kg)</th>
					<th>发货数量</th>
					<th>预计箱数</th>
					<th>每箱个数</th>
					<th>推荐使用箱子</th>
				</tr>
			</thead>
			<tbody class="el-table__body">
				<tr v-for="row in planData.itemlist">
					<td><img :src="row.image" @click.stop="handleToMaterial(row)" style="width:40px;height:40px" /></td>
					<td style="width:200px">
						<div class='name text-omit-1'>{{row.name}}</div>
						<div class='sku pointer ellipsis'  @click.stop="showPrintModal(row)" >
						{{row.msku}} <el-tag v-if="row.boxnum">单箱：{{row.boxnum}}</el-tag>
						</div>
					</td>
					<td>
						<div>{{row.pkglength}}</div>
					</td>
					<td>
						<div>{{row.pkgwidth}}</div>
					</td>
					<td>
						<div>{{row.pkgheight}}</div>
					</td>
					<td>
						<div>{{row.pkgweight}}</div>
					</td>
					<td>
						<div>{{row.confirmQuantity}}</div>
					</td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tbody>
		</table>
	</div>
	<template #footer>
		<el-button @click="visiable=false" >关闭</el-button>
	</template>
	</el-dialog>
	  
	  
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,nextTick,computed } from 'vue';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import { Search,ArrowDown,Close} from '@element-plus/icons-vue';
	import { formatFloat,CheckInputIntLGZero,CheckInputFloat,getValue} from '@/utils/index.js';
	const emit = defineEmits(['change']);
	let props = defineProps({isdiv:undefined });
	const {isdiv} = toRefs(props);
	let state =reactive({
		visiable:false,
		planData:{},
	})
	let{visiable, planData}=toRefs(state);
	function show(plans){
		state.planData=plans;
		state.visiable=true;
	}
	defineExpose({ show })
</script>

<style scoped="scoped">
	 .ellipsis {
	   white-space: nowrap; /* 确保文本在一行内显示 */
	   overflow: hidden; /* 隐藏溢出的内容 */
	   text-overflow: ellipsis; /* 使用省略号表示溢出的文本 */
	 }
	 .mytable {
	   color: #fff; /* 设置表格文字颜色 */
	   text-align: center; /* 设置表格文字居中 */
	   line-height: 40px; /* 设置表格行高 */
	 }
</style>