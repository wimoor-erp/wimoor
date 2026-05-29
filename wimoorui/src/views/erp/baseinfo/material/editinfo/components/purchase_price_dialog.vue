<template>
	<el-dialog v-model="priceVisable" title="采购阶梯价格编辑">
		<el-table border :data="tableData" >
			<el-table-column type="index" label="序号" width="50">
				<template #header>
					<span>序号</span>
				</template>
			</el-table-column>
			<el-table-column label="采购量">
				<template #default="scope">
					<el-space>
						<el-input v-model="scope.row.amount" @input="scope.row.amount=CheckInputInt(scope.row.amount)" placeholder="起订量" ></el-input>
						<!-- <span>-</span> :disabled="scope.$index!==0"
						<el-input v-model="scope.row.endnum" @change="putval(scope.row,scope.$index)"></el-input> -->
					</el-space>
				</template>	
			</el-table-column>
			<el-table-column label="币种">
				<template #default="scope">
					<el-select  v-model="scope.row.currency" placeholder="币别" >
					  <el-option  label="CNY" value="0" />
					 <!-- <el-option  label="USD" value="1" /> -->
					 </el-select>
				</template>
			</el-table-column>
			<el-table-column label="采购单价">
				<template #default="scope">
					<el-input v-model="scope.row.price" @input="scope.row.price=CheckInputFloat(scope.row.price)"></el-input>
				</template>	
			</el-table-column>
			<el-table-column label="操作">
				<template #default="scope">
					<el-space>
						<el-link v-if="tableData.length-1==scope.$index" title="添加" :underline="false" type="primary" @click="addladder(scope.row,scope.$index)">
						  <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
						 </el-link>
						<el-link title="删除" v-if="scope.$index>0&&tableData.length-1==scope.$index" type="primary" :underline="false" @click="removePrice">
							<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
						</el-link>
					</el-space>
				</template>	
			</el-table-column>
		</el-table>
		<template #footer>
			<el-button @click="priceVisable=false">取消</el-button>
			<el-button type="primary" @click="submitprice" >提交</el-button>
		</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted} from 'vue'
	import {Plus,Minus} from '@icon-park/vue-next';
	import {ElMessage} from 'element-plus';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	const emit = defineEmits(['getprice']);
	 
	let state=reactive({
		priceVisable:false,
		tableData:[],
		
	});
	let { priceVisable,tableData } =toRefs(state);
	// let tableData =reactive([
	// 	{
	// 		starnum:'',
	// 		endnum:'',
	// 		currenty:'USD',
	// 		price:'',
	// 	},
	// ])
	function addladder(row,index){
		state.tableData.push({
			amount:'',
			currency:'0',
			price:'',
		})
		//putval(row,index)
	}
	function removePrice(){
		state.tableData.pop()
	}
	function putval(row,index){
		if(parseInt(row.endnum)<=parseInt(row.starnum)){
			ElMessage.error('需大于起订量');
			row.endnum = ""
		}
		if(state.tableData[index+1]){
			if(row.endnum==""){
				state.tableData[index+1].endnum = ''
			}else{
				state.tableData[index+1].starnum = parseInt(row.endnum)+1
			}
			
		}
	}
	
	function submitprice(){
		var isok=true;
		var localamount=0;
		state.tableData.forEach((item)=>{
			if(item.amount==""|| item.price==""){
				isok=false;  
			}else{
				if(localamount==0){
					localamount=item.amount;
				}else if(localamount<item.amount){
					localamount=item.amount;
				}else if(localamount>=item.amount){
					isok=false;
				}
			}
		})
		if(isok==true){
			state.priceVisable =false;
			emit("getprice",state.tableData);
		}else{
			ElMessage.error('数据行不能为空或者采购量填入有误，请重新输入！');
			emit("getprice",[]);
		}
		
	}
		
	defineExpose({
	  tableData,priceVisable
	}); 
		 
</script>

<style>
</style>
