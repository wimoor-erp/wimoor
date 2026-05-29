<template>
	<div>
		<div style="margin-bottom: 20px;">
				<Card :managerLimit="packageData" ></Card>
         </div>
		   <el-descriptions  :column="3"  >
			  <el-descriptions-item >
				        <template #label>
							<div class="icon-wrap">
							<el-icon><Monitor /></el-icon>
							</div>
				            站点数量
				        </template>
			   <el-space>
				<span v-if="calcMaxNum(packageData.maxMarketCount)==='∞'" 
				class="fontbig">{{calcMaxNum(packageData.maxMarketCount)}}</span>
				<span v-else>{{calcMaxNum(packageData.maxMarketCount)}}</span>
			   </el-space>
			   <div>
				   <el-space  class="text-gray">
				   <span>已用</span>
				   <span>{{packageData.existMarketCount}}</span>
				   </el-space>
			   </div>
			  </el-descriptions-item>
			  <el-descriptions-item >
				  <template #label>
				  	<div class="icon-wrap">
				  	<el-icon><User /></el-icon>
				  	</div>
				      子账户量
				  </template>
			   <el-space>
				   <span v-if="calcMaxNum(packageData.maxMember)==='∞'"
				   class="fontbig">{{calcMaxNum(packageData.maxMember)}}</span>
				   <span v-else>{{calcMaxNum(packageData.maxMember)}}</span>
			   </el-space>
			   <div>
				   <el-space  class="text-gray">
				   <span>已用</span>
				   <span>{{packageData.existMember}}</span>
				   </el-space>
			   </div>
			  </el-descriptions-item>
			  <el-descriptions-item>
				  <template #label>
				  	<div class="icon-wrap">
				  	<el-icon><Document /></el-icon>
				  	</div>
				      订单数量
				  </template>
			   <el-space>
				   <span v-if="calcMaxNum(packageData.maxOrderCount)==='∞'"
				   class="fontbig">{{calcMaxNum(packageData.maxOrderCount)}}</span>
				   <span v-else>{{calcMaxNum(packageData.maxOrderCount)}}</span>
			   </el-space>
			   <div>
				   <el-space  class="text-gray">
				   <span>已用</span>
				   <span>{{packageData.existOrderCount}}</span>
				   </el-space>
			   </div>
			  </el-descriptions-item>
			  <el-descriptions-item >
				  <template #label>
				  	<div class="icon-wrap">
				  	<el-icon><House /></el-icon>
				  	</div>
				      店铺数量
				  </template>
			   <el-space>
				   <span v-if="calcMaxNum(packageData.maxShopCount)==='∞'"
				   class="fontbig">{{calcMaxNum(packageData.maxShopCount)}}</span>
				   <span v-else>{{calcMaxNum(packageData.maxShopCount)}}</span>
			   </el-space>
			   <div>
				   <el-space  class="text-gray">
				   <span>已用</span>
				   <span>{{packageData.existShopCount}}</span>
				   </el-space>
			   </div>
			  </el-descriptions-item>
			  <el-descriptions-item>
				  <template #label>
				  	<div class="icon-wrap">
				  	<el-icon><Iphone /></el-icon>
				  	</div>
				      计算方案量
				  </template>
			   <el-space>
				   <span v-if="calcMaxNum(packageData.maxProfitPlanCount)==='∞'"
				   class="fontbig">{{calcMaxNum(packageData.maxProfitPlanCount)}}</span>
				   <span v-else>{{calcMaxNum(packageData.maxProfitPlanCount)}}</span>
			   </el-space>
			   <div>
				   <el-space  class="text-gray">
				   <span>已用</span>
				   <span>{{packageData.existProfitPlanCount}}</span>
				   </el-space>
			   </div>
			  </el-descriptions-item>
			  <el-descriptions-item >
				  <template #label>
				  	<div class="icon-wrap">
				  <el-icon><DataBoard /></el-icon>
				  	</div>
				      广告组数量
				  </template>
			   <el-space>
				   <span v-if="calcMaxNum(packageData.maxdayOpenAdvCount)==='∞'"
				   class="fontbig">{{calcMaxNum(packageData.maxdayOpenAdvCount)}}</span>
				   <span v-else>{{calcMaxNum(packageData.maxdayOpenAdvCount)}}</span>
			   </el-space>
			   <div>
				   <el-space class="text-gray">
				   <span>已用</span>
				   <span>{{packageData.existdayOpenAdvCount}}</span>
				   </el-space>
			   </div>
			  </el-descriptions-item>
			</el-descriptions>
	</div>
</template>

<script setup>
	import {reactive,toRefs,ref} from"vue";
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import {Monitor,User,Document,House,Iphone,DataBoard} from '@element-plus/icons-vue'
	import limitApi from '@/api/sys/admin/limitApi.js';
	import Card from "./card.vue";
	const state = reactive({
		packageData:{},
		viptag:"svip.png",
	})
	const {
		packageData,
		viptag,
	}=toRefs(state)
	
	function calcMaxNum(value){
		if(value>9999999){
			return '∞';
		}else{
			return value;
		}
	}
	
	function show(){
		limitApi.getMyManagerLimitmeal().then((res)=>{
			if(res.data){
				state.packageData=res.data;
			}
			if(res.data.tariffpackage===4){
				state.viptag = "svip.png";
			}else if(res.data.tariffpackage===3){
				state.viptag = "tvip.png";
			}else if(res.data.tariffpackage===2||res.data.tariffpackage===1){
				state.viptag = "bvip.png";
			}else{
				state.viptag = "free.png";
			}
		});
	}
	defineExpose({
		show,
		viptag,
	})
</script>

<style scoped>
	.fontbig{
		font-size:24px;
	}
	.text-gray{
		color:#999;
	}
	.icon-wrap .el-icon{
		background-color: #fff9f4;
		width:32px;
		height: 32px;
		border-radius: 16px;
		font-size: 16px;
		color:#ff7315;
		font-weight: 700;
		margin-bottom:8px;
		margin-top:16px;
	}
</style>