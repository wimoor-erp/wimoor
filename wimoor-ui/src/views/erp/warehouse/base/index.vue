<template>
	  <div v-if="warehouseType=='oversea'" class="el-white-bg oversea">
	  	<el-tabs v-model="activeName" class="oversea-tabs">
	  		<el-tab-pane label="本地海外仓" name="local">
	  			<div class="flex el-white-bg">
	  				<div class="gird-line-left">
	  					 <parent-list @selectedItem="handleClick"  :ftype="warehouseType" />
	  				</div>
	  				<div class="gird-line-right">
	  				 <el-scrollbar class="localoversea">
	  				  <children-list ref="childrenListRef"  :ftype="warehouseType" />
	  				  </el-scrollbar>
	  				</div>
	  			</div>
	  		</el-tab-pane>
	  		<el-tab-pane label="第三方海外仓" name="other">
				<div class="flex el-white-bg">
					 <ThirdPartyOversea></ThirdPartyOversea>  
				</div>
	  		</el-tab-pane>
			
	  	</el-tabs>
	  </div>
	  <div v-else>
	  	<div class="flex el-white-bg">
	  		<div class="gird-line-left">
	  			 <parent-list @selectedItem="handleClick"  :ftype="warehouseType" />
	  		</div>
	  		<div class="gird-line-right">
	  		 <el-scrollbar class="local">
	  		  <children-list ref="childrenListRef"  :ftype="warehouseType" />
	  		  </el-scrollbar>
	  		</div>
	  	</div>
	  </div>
</template>
<script>
export default {
	name: "仓库列表"
}
</script>
<script setup>
import ChildrenList from './components/childrenlist.vue';
import ParentList from './components/parentlist.vue';
import ThirdPartyOversea from "./components/thirdparty_oversea.vue";
import { useRoute,useRouter } from 'vue-router'
import { reactive, toRefs,ref,onMounted } from 'vue'; 
const childrenListRef=ref(ChildrenList);
let router = useRouter();
let activeName=ref("local");
const warehouseType=router.currentRoute.value.query.path.indexOf("oversea")>0?"oversea":"self";
const handleClick = (id) => {
		childrenListRef.value.show(id); 
 };
 
</script>
<style>
	.oversea .el-tabs__nav{
		padding-left:20px;
		padding-top:10px;
	}
	.oversea-tabs > .el-tabs__header{
		padding-bottom:0px;
		margin-bottom:0px;
	}
</style>
<style scoped="scoped">
.flex{
	display: flex;
}
	.flex .gird-line-left{
		width: 310px;
	}
	.flex .gird-line-right{
		flex:1;
	}
	.local{
		height: calc(100vh - 66px);
	}
	
	.localoversea{
		height: calc(100vh - 120px);
	}
</style>
