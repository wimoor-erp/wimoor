<template>
	<el-dialog v-model="visable" title="分摊规则设置">
	    <el-table :data="tableData">
	      <el-table-column  label="费用名称"  prop="name" />
		  <el-table-column  label="分摊规则"  >
		  <template #default="scope">
		  		<el-select v-model="scope.row.shareType">
					<el-option v-for="(item,index) in scope.row.rulesData" :label="item.label" :value="item.value"></el-option>
				</el-select>
		  </template>
		  </el-table-column>
		  <el-table-column label="操作人"  prop="operate"/>
		  <el-table-column label="操作时间"  prop="date"/>
	    </el-table>
		 <template #footer>
		        <el-button @click="visable = false">取消</el-button>
		        <el-button type="primary" @click="handleConfirm">
		          保存
		        </el-button>
		    </template>
	  </el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	const state=reactive({
	  visable:false,
	  tableData:[{
		  name:'早期评论人计划',
		  shareType:'1',
		  rulesData:[
			 {label:'均分',value:'1'},
			 {label:'按销量占比',value:'2'},
			 {label:'按销售额占比',value:'3'},
		  ],
		  operate:'张三',
		  date:'2022-02-20',
	  }]
	})
	const {
		visable,
		tableData,
	}=toRefs(state)
	
	function show(){
		state.visable = true
	}
	
	defineExpose({
		show,
	})
</script>

<style>
</style>
