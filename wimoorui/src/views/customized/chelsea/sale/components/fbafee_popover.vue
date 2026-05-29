<template>
	  <el-popover
	    ref="popoverRef"
		placement="top"
	    :virtual-ref="buttonRef"
	    trigger="click"
	    virtual-triggering
		width="450px"
	  >
	   <div>
		  <p>预计费用超收</p>
		   <p class="fbafee-text"  >{{checkFBA()}}</p>
	   </div>
	   <el-row>
		   <el-col :span="12">
			   <el-space v-if="localRow.param" diretion="vertical" fill>
				   <span class="size-title">自测参数</span>
				   <span><span class="light-font">长x宽x高(cm): </span>
				   <span  v-if="localRow.param.mskuDim">{{localRow.param.mskuDim.length}} x {{localRow.param.mskuDim.width}} x {{localRow.param.mskuDim.height}}</span></span>
				   <span v-if="localRow.param.mskuDim"><span class="light-font">重量(kg): </span><span>{{localRow.param.mskuDim.weight}}</span></span>
				   <span><span class="light-font">尺寸分段: </span>
				   <span v-if="localRow.param.costDetailMap">{{localRow.param.costDetailMap.productTier}}</span></span>
				   <el-divider class="line-margin"></el-divider>
				   <span><span class="light-font">FBA费用: </span>
				   <span v-if="localRow.param.costDetailMap" class="font-bold" >{{localRow.param.costDetailMap.fba}}</span></span>
			   </el-space>
		   </el-col>
		   <el-col  :span="12">
			   <el-space  v-if="localRow.fba" diretion="vertical" fill>
				   <span class="size-title">FBA测参数</span>
				   <span><span class="light-font">长x宽x高(cm): </span><span>{{localRow.fba.length}} x {{localRow.fba.width}} x {{localRow.fba.height}}</span></span>
				   <span><span class="light-font">重量(kg): </span><span v-if="localRow.fba.weight">{{localRow.fba.weight}} </span></span>
				   <span><span class="light-font">尺寸分段: </span><span v-if="localRow.fba.product_size_tier">{{localRow.fba.product_size_tier}}</span></span>
				   <el-divider class="line-margin"></el-divider>
				   <span><span class="light-font">FBA费用: </span>
					 <span class="font-bold" v-if="localRow.fba.estimated_fee_total!=localRow.fba.estimated_referral_fee_per_unit" >{{formatFloat(localRow.fba.estimated_fee_total-localRow.fba.estimated_referral_fee_per_unit)}}</span>
					<span class="font-bold" v-else-if="localRow.fba.expected_efn_fulfilment_fee_per_unit_uk" >{{formatFloat(localRow.fba.expected_efn_fulfilment_fee_per_unit_uk)}}</span>
					<span class="font-bold" v-else-if="localRow.fba.expected_efn_fulfilment_fee_per_unit_de" >{{formatFloat(localRow.fba.expected_efn_fulfilment_fee_per_unit_de)}}</span>
					<span class="font-bold" v-else-if="localRow.fba.expected_efn_fulfilment_fee_per_unit_it" >{{formatFloat(localRow.fba.expected_efn_fulfilment_fee_per_unit_it)}}</span>
					</span>
			   </el-space>
		   </el-col>
		   <el-col :span="12"></el-col>
	   </el-row>
	  </el-popover>
</template>

<script setup>
import { ref,reactive,onMounted,toRefs, computed, nextTick, watch,} from 'vue';
import {formatFloat,formatPercent,formatInteger,decodeText,getCurrencyMark} from '@/utils/index.js';
const buttonRef = ref();
const popoverRef = ref();
 const state = reactive({
		localRow:{}
	})
    const {
		localRow,
	} = toRefs(state)

function checkFBA(){
	var diff=0;
	if(state.localRow.fba){
	        diff=formatFloat(state.localRow.fba.estimated_fee_total-state.localRow.fba.estimated_referral_fee_per_unit);
	  }
	if(state.localRow.param&&state.localRow.param.costDetailMap&&state.localRow.param.costDetailMap.fba){
		diff=diff-parseFloat(state.localRow.param.costDetailMap.fba) ;
	}
	if(state.localRow.ordersales){
		return diff*state.localRow.ordersales;
	}else{
		return 0;
	}
}
function show(row){
	state.localRow=row;
	if(row.costDetail){
		state.localRow.costDetail=JSON.parse(JSON.stringify(row.costDetail));
	}
}

   function getId(){
	   return state.localRow.id;
   }
   
defineExpose({
	 show,buttonRef,getId
})
   
</script>

<style scoped>
	.fbafee-text{
		font-size: 20px;
		font-weight: 700;
		color:var(--el-color-primary)
	}
	.line-margin{
		margin-top: 8px;
		margin-bottom: 8px;
	}
	.size-title{
		margin-top: 16px;
		margin-bottom: 8px;
	}
</style>