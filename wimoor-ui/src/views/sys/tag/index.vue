<template>
  <div class="flex el-white-bg" >
		<div class="gird-line-left">
			 <parent-list @change="handleClick" />
		</div>
		<div class="gird-line-right">
			<el-scrollbar>
          <!-- 字典项组件 -->
          <children-list  ref="childrenListRef"   />
		  </el-scrollbar>
		</div>
  </div>
</template>
<script>
    export default{ name:"标签管理" };
</script>
<script setup>
import ParentList from './components/sysTagGroups.vue';
import ChildrenList from './components/sysTags.vue';
import { reactive, toRefs,ref } from 'vue'; 
 
	  const state = reactive({
	    typeCode: '',
	  }); 
	  const childrenListRef=ref();
	  
	  const { typeCode,  } = toRefs(state);
	  
	  const handleClick = (row) => {
		childrenListRef.value.handleQuery(row.id);
	  };
 
</script>
<style scoped="scoped">
.flex{
	display: flex;
}
	.flex .gird-line-left{
		width: 400px;
	}
	.flex .gird-line-right{
		flex:1;
	}
	.el-scrollbar{
		height: calc(100vh - 66px);
	}
</style>
