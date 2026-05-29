<template>
	<div class="gird-line-head">
		<el-space class="font-small" :size="24">
			<span><span class="text-gray">发货店铺:  </span>Ruikeji</span>
			<span ><span class="text-gray">发货仓库:  </span>清湖仓</span>
		</el-space>
		<el-button   class='ic-btn' title='帮助文档'>
					 <help theme="outline" size="16" :strokeWidth="3"/>
		</el-button>
	</div>
	<div class="grid-content">
		<!-- 比例分配 -->
		<el-table :data="ratioData" :span-method="colSpan" :show-header="false" border>
			<el-table-column >
				<div class="flex-center-between">
				<span>EU站点发货比例设置, 只能填写0-10的数字  </span>
				 <el-switch
				    v-model="value"
					size="small"
				    active-text="优先按30天销量比例计算"
				  />
				  </div>
			</el-table-column>
			<el-table-column width="110"  v-for="item in marketsData">
				<el-input-number
				    min="1"
					max="10" 
					step="1"
					step-strictly
					style="width: 100%;" v-model="item.ratio" :controls="false"></el-input-number>
			</el-table-column>
			<el-table-column width="60"></el-table-column>
		</el-table>
		<!-- 数量计算 -->
		<el-table border :data="tableData">
			<el-table-column label="产品信息" show-overflow-tooltip>
				<template #default="scope">
					<div class="flex-center">
						<div>
						<el-image class="image-product" :src="scope.row.img"></el-image>
						</div>
						<div>
							<div class="name">{{scope.row.name}}</div>
							<div class="sku">{{scope.row.sku}}</div>
						</div>
					</div>
					<el-space > 
						<span><p class="font-bold">255</p>亚马逊库存</span>
						<span><p class="font-bold">19</p>可售天数</span>
						<span><p class="font-bold">150</p>可用库存</span>
					</el-space>
					<div>
						<span class="font-extraSmall">本地SKU:A4373FPFZB-S</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="计划发货数量" prop="num"  >
				<template #default="scope">
					<div>{{scope.row.num}}</div>
					<el-link type="warning" class="font-medium" title="销量报表">
					<el-icon><Histogram /></el-icon>
					</el-link>
				</template>	
			</el-table-column>
			<el-table-column v-for="item in marketsData" width="110" >
				<template #header="scope">
					 <el-checkbox  v-model="item.value"  :label="item.label"  border />
				</template>
				<template #default="scope">
					<el-input v-model="scope.row['shipnum'+item.market]"></el-input>
					<div class="font-extraSmall m-t-16">月销:{{scope.row['sale'+item.market]}}</div>
				</template>	
			</el-table-column>
			<el-table-column label="合计" prop="sum" width="60">
				<template #default="scope">
					<div>{{scope.row.sum}}</div>
					<div class="font-extraSmall" style="margin-top:24px">600</div>
				</template>	
			</el-table-column>
			<!--合计结果计算 -->
			<template #append>
				<el-table :show-header="false"  :data="SumTableData">
					<el-table-column>
						<template #default="scope">
						  <span>{{scope.row.title}}</span>
						</template>	
					</el-table-column>
					<el-table-column width="100">
					</el-table-column>
					<el-table-column v-for="item in  sumMarkets"  width="110">
						<template #default="scope">{{item.value}}</template>
					</el-table-column>
					<el-table-column width="60"></el-table-column>
				</el-table>
			</template>
		</el-table>
		<el-row class="m-t-16 text-right">
			<el-button >取消</el-button>
			<el-button type="primary">确认继续下一步</el-button>
		</el-row>
	</div>
</template>

<script setup>
	import {Help} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue';
	import { ref  ,reactive,onMounted,computed} from 'vue'
	let ratioData = reactive([{}])
    let marketsData = reactive([
		{label:'德国',market:'de',value:true,ratio:1},
		{label:'法国',market:'fr',value:true,ratio:1},
		{label:'西班牙',market:'es',value:true,ratio:1},
		{label:'意大利',market:'it',value:true,ratio:1},
		{label:'土耳其',market:'tu',value:true,ratio:1},
		{label:'荷兰',market:'nl',value:true,ratio:1},
		{label:'波兰',market:'pl',value:true,ratio:1},
		{label:'瑞典',market:'se',value:true,ratio:1},
	])
	let tableData = reactive([
		{
			img:'https://photo.wimoor.com/materialImg/26138972975530085/1601265426213_after.jpeg',
			name:'外包成品）1920盖茨比舞会连衣裙套装；盒子：BXA040；组合：1条黑色蓝砖发带',
			sku:'A4373FPFZB-S',
			num:150,
			sum:150,
			salede:300,
		}
	])
	let SumTableData = reactive([
		{title:"发货数量	"},
		{title:"发货重量"},
		{title:"预估运输重量"},
		{title:"预估箱子体积"},
	])
	let sumMarkets= reactive([
		{label:'德国',value:12,},
		{label:'法国',value:21,},
		{label:'西班牙',value:12,},
		{label:'意大利',value:12,},
		{label:'土耳其',value:12,},
		{label:'荷兰',value:12,},
		{label:'波兰',value:12,},
		{label:'瑞典',value:12,},
	])
	/* 计算发货数量 */
	let ratioSum = marketsData.reduce((c, R)=> c + R.ratio, 0)
	let shipnumResault = computed(()=>{
	/* 
		marketsData.forEach() */
			
	})
	function colSpan({ 
		row,
        column,
        rowIndex,
		columnIndex,}
  ){
	  if(columnIndex==0){
		  return [1, 1]
	  }
	}
</script>

<style scoped="scoped">
	.text-gray{
		color:var(--el-text-color-secondary)
	}
	.grid-content{
		padding:16px 24px;
	}
	.image-product{
		width: 40px;
		height: 40px;
		margin-right: 8px;
	}
	.m-t-16{
		margin-top: 16px;
	}
	.text-right{
		justify-content: flex-end;
	}
</style>
