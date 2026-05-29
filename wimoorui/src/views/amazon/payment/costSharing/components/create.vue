<template>
	<div >
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>添加分摊费用</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							
						 <el-form :model="form"
						   :rules="rules"
						   label-width="120px">
							 <el-form-item label="单据编码"   >
							 	<el-input class="in-wi-24" disabled  v-model="form.number" placeholder="请输入"> </el-input>
							 </el-form-item>
								 <el-form-item label="费用类型" prop="feeType">
									 <el-select v-model="form.feeType">
										 <el-option v-for="(item,index) in form.feeOptions" :label="item.label" :value="item.value"></el-option>
									 </el-select>
								 </el-form-item>
							 <el-form-item label="金额"  prop="amount">
							 	<el-input class="in-wi-24" type="number" v-model="form.amount" placeholder="请输入金额">
									<template #prefix>￥</template>
								</el-input>
							 </el-form-item>
							<el-form-item label="备注"  >
							 	<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="请输入备注"> </el-input>
							 </el-form-item>
						 </el-form>
						
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >分摊商品 </h5>
						   </div>
						  <!-- 需要添加平台SKU -->
						   <el-button @click="handleAdd" type="primary">添加商品</el-button>
						 </div>
						 <el-table border :data="tableData"
						 show-summary
						 >
							 <el-table-column label="序号" type="index" width="80"/> 
							 <el-table-column label="店铺"  width="120" prop="group"/> 
							 <el-table-column label="国家"  width="120" prop="market"/> 
							 <el-table-column label="分摊产品"  prop="market" show-overflow-tooltip>
							 	<template #default="scope">
							 		<div class="flex-center">
							 			<el-image class="  product-img" :src="scope.row.image"></el-image>
							 			<div class="m-l-8">
							 				<div class="name">{{scope.row.name}}</div>
							 				<div class="sku">{{scope.row.sku}}</div>
							 			</div>
							 		</div>
							 	</template>
							 </el-table-column>
							 <el-table-column label="分摊日期">
								  <template #default="scope">
									  <el-date-picker
									          v-model="scope.row.date"
									          type="date"
									          placeholder="请选择日期"
									        />
								  </template>  
							 </el-table-column>
							 <el-table-column label="分摊金额">
								 <template #header>
									 <el-space>
									<span>分摊金额</span>
									<el-button 
									@click="handleSharRules"
									link type="primary">按规则分摊</el-button>
									</el-space>
								 </template>
								  <template #default="scope">
									  <el-input type='number'  v-model="scope.row.amount" >
										  <template #prefix>￥</template>
									  </el-input>
								  </template>  
							 </el-table-column>
							 <el-table-column label="备注">
								  <template #default="scope">
									  <el-input v-model="scope.row.remarks">
									  </el-input>
								  </template>  
							 </el-table-column>
							 <el-table-column label="操作" width="80">
								  <template #default="scope">
								    <el-button @click="handleDelete(scope.$index)" link type="primary" >删除</el-button>
					        	  </template>
							 </el-table-column>
						 </el-table>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;">
								<el-space>
									<el-button type="" @click="closeTab">关闭</el-button>
									<el-button type="primary" @click="submitForm">提交</el-button>
								</el-space>
							</div>
						</div>
	</div>
	<Listings ref="ListingsRef" />
</template>

<script setup>
	import {Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue';
	import {ElMessage } from 'element-plus';
	import Listings from"@/views/amazon/listing/common/listings.vue"
	import $require from '@/utils/system/require.js';
	const emitter = inject("emitter"); // Inject `emitter`
	const ListingsRef = ref()
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	const state = reactive({
		form:{
			number:'',
			remarks:'',
			amount:'',
		},
		rules: {
			feeType: [{ required: true, message: '费用类型不能为空', trigger: 'blur' }],
			amount: [{ required: true, message: '金额不能为空', trigger: 'blur' },
			],
		},
		tableData:[
			{
				image:$require('testpic.png'),
				group:'akhd',
				market:'美国',
				name:'Confetti Balloons 60 Pack Pastel Yellow White Party Balloon for Sunflower',
				sku:'kq005',
				amount:'',
				currency:'￥',
				remarks:'-',
			},
			{
				image:$require('testpic.png'),
				group:'akhd',
				market:'美国',
				name:'Confetti Balloons 60 Pack Pastel Yellow White Party Balloon for Sunflower',
				sku:'kq005',
				amount:'',
				currency:'￥',
				remarks:'-',
			}
		],
	})
	const {
		form,
		rules,
		tableData,
	}=toRefs(state)

  function handleAdd(){
	  ListingsRef.value.show()
  }
  
  function handleSharRules(){
	  /* 0.5为举例的规则系数 */
	  state.tableData.forEach((item)=>{
		  item.amount = state.form.amount*0.5
	  })
  }
  
  function handleDelete(index){
	   state.tableData.splice(index,1)
  }
</script>

<style>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 150px);
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
		.mark-re{
			margin-top: 16px;
			margin-bottom:16px;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	.mark-re h5::before{
		content: "";
		display: inline-block;
		height: 13px;
		width: 4px;
		margin-right:8px;
		background-color: #FF6700;
	}
	.fill-from-body{
		margin:16px 24px;
	}
</style>
