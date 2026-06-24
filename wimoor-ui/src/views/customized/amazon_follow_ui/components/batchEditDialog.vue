<template>
	<el-dialog
	title="批量修改价格和备货周期"
	v-model="visable"
	width="600px"
	>
     <el-tabs v-model="activeName" type="card"  >
	   <el-tab-pane label="固定改价" name="1">
		  <el-form>
			  <el-form-item label="当前价格">
				  <el-space>
				  <el-input placeholder="请输入"   v-model="price"  @input="price=CheckInputFloat(price)"></el-input>
				  <el-button type="primary" @click="changeFollow('price')">修改</el-button>
				  </el-space>
			  </el-form-item>
			  <el-form-item label="承受价　">
			  				  <el-space>
			  				  <el-input placeholder="为0则不调价"  v-model="lowprice" @input="lowprice=CheckInputFloat(lowprice)"></el-input>
			  				  <el-button type="primary" @click="changeFollow('lowprice')">修改</el-button>
			  				  </el-space>
			  </el-form-item>
		  </el-form>
	   </el-tab-pane>
	   <el-tab-pane label="比例改价" name="2">
		   <el-form>
		   <el-form-item label="当前价格">
		   				  <el-space>
							  <el-input type="number" placeholder="比例系数(例如:1.2 0.9)" v-model="rateprice" @input="rateprice=CheckInputFloat(rateprice)"></el-input>
							  <span class="text-info"> *当前价格</span>
		   				  <el-button type="primary" @click="changeFollow('rateprice')">修改</el-button>
		   				  </el-space>
		   </el-form-item>
		   <el-form-item label="承受价格">
		   				  <el-space>
		   				  <el-input  type="number" placeholder="比例系数(例如:1.2 0.9)" v-model="ratelowprice" @input="ratelowprice=CheckInputFloat(ratelowprice)"></el-input>
						   <span class="text-info"> *当前承受价格</span>
		   				  <el-button type="primary" @click="changeFollow('ratelowprice')">修改</el-button>
		   				  </el-space>
		   </el-form-item>
		   </el-form>
	   </el-tab-pane>
	   <el-tab-pane label="库存/发货天数" name="3">
		   <el-form>
		   			  <el-form-item label="发货天数">
		   				  <el-space>
		   				  <el-input placeholder="ASIN发货天数" v-model="cycle"  @input="cycle=CheckInputInt(cycle)"></el-input>
		   				  <el-button type="primary" @click="changeFollow('cycle')">修改</el-button>
		   				  </el-space>
		   			  </el-form-item>
		   			  <el-form-item label="起始库存">
							  <el-space>
							  <el-input placeholder="库存数量" v-model="quantity" @input="quantity=CheckInputInt(quantity)"></el-input>
							  <el-button type="primary" @click="changeFollow('qty')">修改</el-button>
							  </el-space>
		   			  </el-form-item>
					  <el-form-item label="库存水位">
							  <el-space>
							  <el-input placeholder="库存数量" v-model="lowestquantity" @input="lowestquantity=CheckInputInt(lowestquantity)"></el-input>
							  <el-button type="primary" @click="changeFollow('lowestqty')">修改</el-button>
							  </el-space>
					  </el-form-item>
					  
		   </el-form>
	   </el-tab-pane>
	 </el-tabs>
	<template #footer>
		<el-button @click="visable=false">关闭</el-button>
	</template>
	</el-dialog>
</template>


<script setup>
	import {ref,reactive,toRefs,onMounted,defineEmits,}from"vue";
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	const emit = defineEmits(['change']);
	const state=reactive({
		visable:false,
		activeName:"1",
		checkorws:null,
		price:null,
		lowprice:null,
		rateprice:null,
		ratelowprice:null,
		cycle:null,
		quantity:null,
		lowestquantity:null,
	})
	const {
		activeName,
		visable,
		checkorws,
		price,
		lowprice,
		rateprice,
		ratelowprice,
		cycle,
		quantity,
		lowestquantity
	}=toRefs(state)
	
	function changeFollow(ftype){
		var list=[];
		if(ftype=="price"){
			if(!state.price || state.price==""){
				ElMessage({
					type: 'error',
					message: '价格填写不正确！'
				});
				return;
			}
			list.push(state.price.toString());
		}
		if(ftype=="lowprice"){
			if(!state.lowprice || state.lowprice==""){
				ElMessage({
					type: 'error',
					message: '承受价格填写不正确！'
				});
				return;
			}
			list.push(state.lowprice.toString());
		}
		if(ftype=="rateprice"){
			if(!state.rateprice || state.rateprice==""){
				ElMessage({
					type: 'error',
					message: '比例价格填写不正确！'
				});
				return;
			}
			list.push(state.rateprice.toString());
		}
		if(ftype=="ratelowprice"){
			if(!state.ratelowprice || state.ratelowprice==""){
				ElMessage({
					type: 'error',
					message: '比例承受价格填写不正确！'
				});
				return;
			}
			list.push(state.ratelowprice.toString());
		}
		if(ftype=="cycle"){
			if(!state.cycle || state.cycle==""){
				ElMessage({
					type: 'error',
					message: '发货天数填写不正确！'
				});
				return;
			}
			list.push(state.cycle.toString());
		}
		if(ftype=="qty"){
			if(!state.quantity || state.quantity==""){
				ElMessage({
					type: 'error',
					message: '库存填写不正确！'
				});
				return;
			}
			list.push(state.quantity.toString());
		}
		if(ftype=="lowestqty"){
			if(!state.lowestquantity || state.lowestquantity==""){
				ElMessage({
					type: 'error',
					message: '库存水位填写不正确！'
				});
				return;
			}
			list.push(state.lowestquantity.toString());
		}
		state.checkorws.forEach(item=>{
			list.push(item.pid);
		});
		followListApi.changeFollow(list,ftype).then((res)=>{
			ElMessage({
				type: 'success',
				message: '操作成功'
			});
			state.visable=false;
			handleQuery();
		});
	} 
	function handleQuery(){
		emit("change");
	}
	function show(rows){
		state.visable = true;
		state.checkorws=rows;
		console.log(rows);
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>