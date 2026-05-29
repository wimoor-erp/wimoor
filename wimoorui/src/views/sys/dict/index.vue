<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="12" :xs="24">
        <el-card  >
          <template #header>
            <svg-icon icon-class="dict" />
            字典类型
          </template>
          <dict-type @dictClick="handleDictTypeClick" />
        </el-card>
      </el-col>

      <el-col :span="12" :xs="24">
        <el-card  >
          <template #header>
            <svg-icon icon-class="dict_item" />
            <span style="margin: 0 5px">字典数据项</span>
            <el-tag type="success" v-if="typeCode" size="small">{{
              typeName
            }}-{{typeCode}}</el-tag>
            <el-tag type="warning" v-else size="small">未选择字典</el-tag>
          </template>
          <!-- 字典项组件 -->
          <dict-item ref="dictItem" :typeName="typeName" :typeCode="typeCode" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script >
import DictType from './components/DictType.vue';
import DictItem from './components/DictItem.vue';
import { reactive, toRefs,ref } from 'vue';

export default {
  name: '数据字典',
  components:{DictType,DictItem},
  setup(){
	  const state = reactive({
	    typeCode: '',
	    typeName: '',
	  }); 
	  const dictItem=ref();
	  const { typeCode, typeName } = toRefs(state);
	  
	  const handleDictTypeClick = (row) => {
	    if (row) {
	      state.typeName = row.name;
	      state.typeCode = row.code;
	    } else {
	      state.typeName = '';
	      state.typeCode = '';
	    }
		dictItem.value.handleQuery(typeCode.value);
	  };
	  return{
		  state,typeCode,typeName,//value
		  handleDictTypeClick,//function
		  dictItem,//ref
	  }
  }
};
</script>
<style>
	.app-container{
		margin:20px;
	}
	
</style>
