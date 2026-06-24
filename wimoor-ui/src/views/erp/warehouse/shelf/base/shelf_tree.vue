<template>
 <el-input v-model="fliterTreetext" placeholder="搜索库位"  >
	<template #suffix>
	<el-icon style="font-size:16px;" class="el-input__icon"><search/></el-icon>
	 </template> 
</el-input>
 <div class='title'>仓库列表</div>	
  <el-tree 
  :data="data.list"
  :props="defaultProps"
   node-key="id"
   highlight-current="true"
   ref="stree"
  :load="loadDataTree"
  @node-click="handleNodeClick"
  :filter-node-method="filterNode"
></el-tree>
</template>

<script>
  import {reactive,onBeforeMount,ref,watch,onUpdated } from 'vue'
  import {Search} from '@element-plus/icons-vue'
  import shelfApi from '@/api/erp/warehouse/shelf.js';
  export default {
	 name:'shelfTree',
     components: {Search},
	 setup(props,{attrs,slots,emit}){
		const fliterTreetext = ref('')
		var data=reactive({list:[]});
		var key=ref("");
		var parentnode=reactive({list:[]});
		var keys=reactive({list:[]});
		const stree=ref();
		var defaultProps= {
		   children: 'children',
		   label: (data,node)=>{return data.treepath?data.number+'-'+data.name:data.name},
		 };
		 var reloadTree=function(defaultid){
			 shelfApi.getWarehouseShelf().then(function(response){
				 if(response){
				 	data.list=response.data;
					if(defaultid){
						key.value=defaultid;
					}else if(response.data.length>0){
				 	   findDefault(response.data[0]);
				 	}
					
				 }
			 });
			 				
		 }
		 //获取树名字节点函数
		function getAllTreeName(data) {
			 var node=stree.value.getNode(data.id);
			 //点击时获取到树节点所有的层级name
			  let arr =[] 
			  let pt = node.parent
			  let obj1 = {}
		      obj1.name =data.number+'-'+data.name
			   obj1.id= data.id
			  //当前点击的节点放入数组
			  arr.push(obj1)
			  //循环获取每个父节点的名字
			  for(var i= 1;i<node.parent.level;i++){
			 				var obj={}
			 				obj.name =pt.data.number+'-'+pt.data.name 
			 				obj.id = pt.data.id
			 				arr.push(obj) 
			 				pt = pt.parent
			  }
			  //颠倒数组
			  arr.reverse()
			  return arr
		 }
		 var handleNodeClick=function(data) {
			 var node=stree.value.getNode(data.id);
			  if(data.parentid){
		        emit("node-click",data);
			  }
			  
		 }
		 var loadDataTree=function(){
		 }
		 var getCurrentNode=function(){
			 return stree.value.getCurrentNode();
		 }
		 function expandParents(node){
			 node.extended=true;
			 if(node.parent){
				 expandParents(node.parent);
			 }
		 }
	   var selectNode=function(data){
		   if(data['id']){
			   key.value=data.id;  
		   }else{
			   key.value=data;  
		   }   

		   if(stree.value){
			   var node=stree.value.getNode(key.value);
			   node.id=key.value;
			   if(node.data.parentid){
				   node.isCurrent=true;
			   	   stree.value.setCurrentNode(node);
			   	   expandParents(node);
			   	   emit("node-click",node.data);
			   }
		   }
		  
	   }
	     onBeforeMount(async () => {
		       const response = await shelfApi.getWarehouseShelf(); 
				if(response){
					data.list=response.data;
					if(response.data.length>0){
					   findDefault(response.data[0]);
					}
				}
		     });
			 onUpdated(()=>{
							  var node=stree.value.getNode(key.value);
							  if(node){
								  node.id=key.value;
								  stree.value.setCurrentNode(node);
								  emit("node-click",node.data);
							  }
							 
			 })
		   function findDefault(value){
		 	 if(value&&key.value==""){
		 		// if(value.children&&value.children.length>0){
		 		// 	key.value=value.children[0].id;
		 		// }else {
					key.value=value.id;
		 		// }
		 	}
		 }
		 watch(fliterTreetext,(val)=>{
			 stree.value.filter(val)
		 })
		 function filterNode(value,data){
			  if (!value) return true
              return data.name.indexOf(value) !== -1
		 }
		 return {
			 data,defaultProps,selectNode,handleNodeClick,loadDataTree,stree,getCurrentNode,filterNode,reloadTree,
			 fliterTreetext,getAllTreeName
		 }
		 }
  }
</script>
<style>
	.el-tree{
		background: none;
	}
	.gird-body{
		padding:16px;
	}
	.gird-body .title{
		font-size:15px;
		margin-top:16px;
		margin-bottom:8px;
		font-weight:600;
	}
	.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content{
		background-color: var(--el-color-primary-light-9);
		color:var(--el-color-primary);
	}
</style>