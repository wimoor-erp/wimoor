<template>
    <div class="main-sty">
        <div class="con-header">
			<el-row>
				<el-space>
					<AdGroup  :border="true"/>
				    <el-select v-model="campagin" placeholder="全部广告活动" clearable filterable >
						<el-option v-for="item in campaginsData" :label="item.name" :value="item.value"></el-option>
					</el-select>
				    <el-select v-model="campagin" placeholder="全部广告组" clearable filterable >
						<el-option v-for="item in campaginsData" :label="item.name" :value="item.value"></el-option>
					</el-select>
				    <el-select v-model="campagin" placeholder="创建人" clearable filterable >
						<el-option v-for="item in campaginsData" :label="item.name" :value="item.value"></el-option>
					</el-select>
					<el-input  v-model="searchKeywords" @input="loadData" placeholder="请输入SKU或关键词" clearable="true" class="input-with-select" >
					    <template #suffix>
					                <el-icon><search/></el-icon>
					    </template>
					</el-input>
					<el-button>重置</el-button>
				</el-space>
			</el-row>
            <el-row>
                <el-space >
                    <el-button @click="handdleAdd" type="primary" class="im-but-one">
                        <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
                        <span>添加提醒</span>
                    </el-button>
                </el-space>
                <div class='rt-btn-group'>
                    <el-button   class='ic-btn' title='帮助文档'>
                        <help theme="outline" size="16" :strokeWidth="3"/>
                    </el-button>
                </div>
            </el-row>
        </div>
        <!--表单-->
        <el-row>
            <GlobalTable ref="globalTable" :tableData="tableData" @selectionChange='handleSelect' :defaultSort="{ prop: 'opttime', order: 'descending' }"  @loadTable="loadTableData" :stripe="true" 
			height="calc(100vh - 246px)"
			>
            	<template #field>
                <el-table-column prop="object"  label="提醒对象" width="100" />
                <el-table-column prop="market"  label="店铺站点"  />
				<el-table-column prop="name"  label="名称"   />
                <el-table-column prop="cycle"  label="计算周期"  />
                <el-table-column prop="term"  label="触发条件"  />
                <el-table-column prop="opttime"  label="创建日期"  />
                <el-table-column prop="state"  label="状态"  >
				<template #default="scope">
					  <el-tag type="info">未触发</el-tag>
				  </template>
				</el-table-column>
                <el-table-column prop="operate"  label="创建人"  />
                <el-table-column  label="操作" width="140" sortable >
                    <template #default="scope">
                        <el-button class='el-button--blue' @click="Remove(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
				</template>
            </GlobalTable>
        </el-row>

    </div>
	<Create ref="createRef"/>

</template>
<script setup>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import {ref,reactive,toRefs,onMounted}from"vue";
	import AdGroup from '@/components/header/ad_group.vue';
	import Create from"./components/create.vue"
	const createRef = ref()
	const state = reactive({
		     tableData:{records:[
				 {
					 object:"广告活动",
					 name:"A2012TKJJA Spider Web Decoration AU",
					 cycle:"近七天",
					 term:'花费超过 $1',
					 opttime:'2022-22-02',
					 state:'active',
				 }
			 ],total:0},
		})
	const{
		tableData,
	}=toRefs(state)	
	function handdleAdd(){
		createRef.value.show()
	}
</script>
<style>
</style>