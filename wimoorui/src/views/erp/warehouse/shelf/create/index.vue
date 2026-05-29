<template>
 <el-row class="el-white-bg">
    <el-col :span="4" >
		<div class="grid-content" >
			    <div class='gird-head'>
				<el-button type="primary" v-hasPerm="'erp:wh:shelf:add'" 
				           @click="showCreateDialog(nowNode.data)">添加库位</el-button>	
				<div class='rt-btn-group'>
				 <el-button   class='ic-btn' title='帮助文档'>
				  <help theme="outline" size="16" :strokeWidth="3"/>
				</el-button>
				</div>
				</div>
				<div class='gird-body' style="height:calc(100vh - 110px);">
				<ShelfTree ref="leftSearchTree"   @node-click="treeSeletecd"/>
		       </div>
	    </div>
	</el-col> 
    <el-col :span="20" >
		<div class="grid-content" >
		  <ShelfCard :breadname = 'breadname' ref="selfCardRef" @add-new="showCreateDialog"  @card-click="cardClickToTree" @modify-tree="modifyTree"/>
		  <ShelfDialog @submit-done="reloadSearchTree" ref="selfDialogRef"/>
	    </div> 
	</el-col>

  </el-row>

</template>

<script>
import { ref ,watch,reactive,onMounted} from 'vue'
import ShelfTree from "@/views/erp/warehouse/shelf/base/shelf_tree.vue";
import ShelfCard from "./components/shelf_card.vue";
import ShelfDialog from "./components/shelf_dialog.vue";
/* 清除缓存 */
import {menuApi} from "@/api/sys/admin/menuApi.js";
import {Help} from '@icon-park/vue-next';

export default {
  name: '库位列表',
  components: {ref,ShelfTree,ShelfCard,ShelfDialog,Help},
  setup(){
		const value = ref([])
		const breadname = ref()
		const leftSearchTree=ref();
		const selfCardRef=ref();
		var nowNode=reactive({data:{}});
		const props = {
		  expandTrigger: 'hover',
		}
	  
		const selfDialogRef = ref();
		const handleChange = (value) => {
			
		};
		const  reloadSearchTree=(id)=>{
			return  leftSearchTree.value.reloadTree(id);
		}
		const cardClickToTree=(id)=>{
			return  leftSearchTree.value.selectNode(id);
		}
		const modifyTree=(parentid)=>{
			   leftSearchTree.value.reloadTree(parentid);
			   selfDialogRef.value.reloadDailogTree(parentid);
		}
	    function showCreateDialog(data){  
		        	/* 清除缓存 */
				  menuApi.cleanUserCache();
		 selfDialogRef.value.showDailog(data);
	    }
		function treeSeletecd(data){
			nowNode.data=data;
			if(selfCardRef.value){
			   selfCardRef.value.loadData(data);
			}
		  breadname.value = leftSearchTree.value.getAllTreeName(data);
		}
		function serachTree(){
			leftSearchTree.value.filterNode(value,data)
		}
		return {
			handleChange,nowNode,breadname,
			props,value,reloadSearchTree,
			leftSearchTree,treeSeletecd,showCreateDialog,cardClickToTree,
			selfDialogRef,selfCardRef,modifyTree}
	  },
}

</script>
<style>
	.gird-head{display:flex;
	 padding:16px;
	 border-bottom:1px solid var(--el-border-color-light)
	}
	.grid-content{border-right:1px solid var(--el-border-color-light);
	height:100%
	}
</style>