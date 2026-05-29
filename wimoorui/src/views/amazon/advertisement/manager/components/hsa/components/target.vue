<template>
	<el-form-item label="定向策略" prop="target">
		<div style="width:90%;" >
			<el-radio-group v-model="targetType" @change="handleTargetType" class="have-ass-text-radio">
				<div>
					<div>
						<el-radio label="keyword">
							关键词投放
							<p>选择关键词可以帮助您的商品出现在买家的搜索词中。</p>
						</el-radio>
					</div>
					<el-radio label="target">
						商品投放
						<p>选择特定的产品，类别，品牌，或其他产品的特征来投放您的广告。</p>
					</el-radio>
				</div>
			</el-radio-group>

			<div class="flex-center"   v-if="targetType=='keyword'">
				   <TargetKeyword ref="targetKeywordRef" @change="emitChange"></TargetKeyword>
			</div>

			<div class="flex-center" v-if="targetType=='target'">
				  <TargetProduct ref="targetProductRef"  @change="emitChange"></TargetProduct>
			</div>
			
		</div>
	</el-form-item>
	 
</template>

<script setup>
	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	
	import TargetKeyword from './target_keyword.vue' ;
	import TargetProduct from './target_product.vue' ;
	import {
		ref,
		reactive,
		onMounted,
		toRefs,
		watch,
		nextTick
	} from 'vue'
	import {
		Search,
	} from '@element-plus/icons-vue'
	import {
		ArrowCircleRight,
	} from '@icon-park/vue-next';
	//展示用
	const targetProductRef=ref();
	const targetKeywordRef=ref();
	const star = ref([2, 4])
	 
	const emit = defineEmits(['change','changetype']);
	const state = reactive({
		targetType: "keyword",
		queryParams:{},
	})
	const {
		targetType,
		queryParams,
	} = toRefs(state)
  
    function handleTargetType(){
		if(state.targetType=="target"){
			nextTick(()=>{
				targetProductRef.value.show(state.queryParams);
			});
		}else{
			nextTick(()=>{
			    targetKeywordRef.value.show(state.queryParams);
			});
		}
		emit("changetype",state.targetType);
	}
	function emitChange(data){
		emit("change",data);
	}
	function show(params) {
		state.queryParams = params;
		state.targetingType="keyword";
		if(params.adtype=="adtarget"){
			state.targetType="target";
		 }else{
			 state.targetType="keyword";
		 }
		handleTargetType();
	}
	defineExpose({
		show,
	})
	 
</script>

<style scoped>
	.star-box {
		width: 100%;
		margin: 0 16px;
		margin-bottom: 16px;
	}

	.class-header {
		display: flex;
		justify-content: space-between;
		color: #666;
	}

	.class-list {
		display: flex;
		justify-content: space-between;
		color: #333;
		border-bottom: 1px solid #eee;
	}

	.have-ass-text-radio .el-radio {
		height: inherit !important;
		line-height: 24px;
		white-space: inherit;
		margin-bottom: 16px;
		align-items: start;
	}

	.have-ass-text-radio p {
		font-size: 12px;
		color: #acb0b9;
	}

	.p-l {
		width: 50%;
		height: 500px;
	}

	.p-r {
		width: 50%;
		height: 500px;
		background-color: var(--el-color-info-light-7);
	}

	.m-l-8 {
		margin-left: 8px;
	}
</style>
<style>
	.have-ass-text-radio .el-radio__input {
		padding-top: 6px;
	}

	.el-tree-node__label {
		flex-grow: 1;
	}
</style>
