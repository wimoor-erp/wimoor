<template>
	<el-form-item label="自动投放" prop="target" required>
		<el-radio-group  v-model="forms.biddingType" class="have-ass-text-radio">
			<div>
			<div>
		     <el-radio label="1" >
			 默认竞价
			  <p >作用于广告组下所有的关键词，除特定设置过不同竞价的关键词</p>
			  <el-input v-show="forms.biddingType=='1'" v-model="forms.bidding" placeholder="请输入" style="width:214px">
			  	<template #prepend>$</template>
			  </el-input>
			  </el-radio>
			  </div>
			<el-radio label="2" >
			  投放组竞价
			  <p>使用多种竞价策略，将您的广告与寻找你产品的购物者进行匹配</p>
			  <el-table  :data="forms.biddingTable" v-show="forms.biddingType=='2'">
				  <el-table-column label="投放组" width="180">
					  <template #default="scope">
						  <el-space>
					  		<el-switch
					  		   v-model="scope.row.status"
					  		   size="small"
					  		 />
	 						<span>{{scope.row.name}}</span> 
							</el-space>
					  </template>
				  </el-table-column>
				  <el-table-column label="建议竞价" prop="sugBid">
					  <template #default="scope">
					  				<div>
										{{scope.row.sugBid.bid}}
									</div>
								   <div class="font-extraSmall">
									   	{{scope.row.sugBid.start}}-{{scope.row.sugBid.end}}
								   </div>
					  	   </template>
					  </el-table-column>
				  <el-table-column label="竞价" width="180" prop="sugBid">
					  <template #default="scope">
						 <el-input v-model="scope.row.sugBid.bidding" placeholder="请输入" >
						 	<template #prepend>$</template>
						 </el-input>
					  </template>
				  </el-table-column>
			  </el-table>
			  </el-radio>
			    </div>
		</el-radio-group>
	</el-form-item>
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs,watch} from 'vue'
    const props=defineProps({
		forms:{},
	})
	const {
	    forms,
	} = toRefs(props);
</script>

<style scoped>
	.have-ass-text-radio .el-radio{
		height:inherit!important;
		line-height:24px;
		white-space: inherit;
		margin-bottom:16px;
		align-items:start;
	}
	
	.have-ass-text-radio p{
		font-size:12px;
		color:#acb0b9;
	}
</style>
<style>
	.have-ass-text-radio  .el-radio__input{
		  padding-top:6px;
	}
</style>