<template>
	 <el-dropdown>
		   <span class="flex-center">
			  <el-space :size="4" v-for="(planware,planindex) in plan.warehouseList" >
								   <el-icon v-if="planindex>0"><Plus /></el-icon>
			  						  <el-tag              round
			  									          :type="types[planindex]"
			  									          effect="plain"
			  									      >
			  									       {{ planware.name }}
			  						  </el-tag>
			  					 
			  </el-space>
	            <el-icon class="el-icon--right">
	              <arrow-down />
	            </el-icon>
	          </span>
	      <template #dropdown>
	        <el-dropdown-menu>
	          <el-dropdown-item v-for="item in planList" @click="change(item)">
				  <el-space :size="4" v-for="(ware,index) in item.warehouseList" >
									  <el-icon class="m-r-0" v-if="index>0"><Plus /></el-icon>
									  <el-tag  round
									          :type="types[index]"
									          effect="plain"
									      >
									       {{ ware.name }}
									      </el-tag>
				  </el-space>
			  </el-dropdown-item>
	        </el-dropdown-menu>
	      </template>
	    </el-dropdown>
		<el-icon title="编辑计划" @click="showDailog" :size="20" class="purchasePlanIcon font-extraSmall  pointer ">
		    <Edit />
		  </el-icon>
		  
<el-dialog v-model="dialog.visible" title="补货规划" width="70%">
	<el-space :size="16" class="m-b-16">
				<el-button @click="handleAdd" >
						<el-icon >
							 <Plus />
						</el-icon>
					 &nbsp;添加补货规划 
				</el-button>
			<div class="font-extraSmall">
			可选择一个或多个仓库作为一个补货规划
			</div>
	</el-space>
	 
	  <el-row :gutter="12" v-if="isedit">
		  <el-col :span="24">
		  	 <div v-if="planwarehouseids" >
		  	 <span  v-for="(id,index) in planwarehouseids"  >
								  <el-icon v-if="index>0"><Plus /></el-icon>
		  	 										  <el-tag  round
		  	 												  :type="types[index]"
		  	 												  effect="plain"
		  	 											  >
		  	 											   {{planwarehouse[id].name }}
		  	 											  </el-tag>
		  	 </span>
		  	 </div>
		  	 <div v-else  >新的计划</div>
		  </el-col>
	     <el-col :span="24" v-if="selfUsable&&selfUsable.length>0">
		 <div class="flex-star">
			 <div class="w-120">
		           <p >本地仓库:</p>
				   <span class="font-extraSmall">正品仓</span>
				   </div>
			<div>
		     <el-checkbox v-for="warehouse in selfUsable" 
			             @change="handleCheckedChange(selfUsable)" 
						 v-model="warehouse.checked"
						 :key="warehouse.id" 
						 :label="warehouse.name">
			   {{  warehouse.name }}
			 </el-checkbox>
			 </div>
			 </div>
	     </el-col>
		 
	     <el-col :span="24" v-if="selfTest&&selfTest.length>0">
		 <div class="flex-star">
			      <div class="w-120">
			         <p>本地仓库:</p>
					 <span class="font-extraSmall">测试仓</span>
			       </div>
				   <div>
			     <el-checkbox v-for="warehouse in selfTest" 
				   @change="handleCheckedChange(selfTest)"
				   v-model="warehouse.checked"
				  :key="warehouse.id" 
				  :label="warehouse.name">
				  {{ warehouse.name  }}
				 </el-checkbox>
				 </div>
				 </div>
	     </el-col>
		 
	     <el-col :span="24" v-if="overseaUsable&&overseaUsable.length>0">
		    <div class="flex-star">
			     <div class="w-120">
			        <p>海外仓库:</p>
					<span class="font-extraSmall">正品仓</span>
			      </div>
			     <el-checkbox v-for="warehouse in overseaUsable" 
				 @change="handleCheckedChange(overseaUsable)"
				 v-model="warehouse.checked"
				 :key="warehouse.id" 
				 :label="warehouse.name">
				  {{ warehouse.name }}
				 </el-checkbox>
				  </div>
	     </el-col>
		 
		 <el-col :span="24" v-if="overseaTest&&overseaTest.length>0" >
		 <div class="flex-star">
			     <div class="w-120">
					<p>海外仓库:</p>
					<span class="font-extraSmall">测试仓</span>
			      </div>
				  <div>
			   <el-checkbox v-for="warehouse in overseaTest" 
			   @change="handleCheckedChange(overseaTest)"
			    v-model="warehouse.checked"
			   :key="warehouse.id" 
			   :label="warehouse.name">
			    {{ warehouse.name }}
			   </el-checkbox>
			   </div>
			   </div>
		 </el-col>
		 <el-col :span="24"  >
		 	 <el-button class="pull-right"  type="primary" @click="submitForm">保存 </el-button >     
			 <el-button class="pull-right"   @click="handleCancel">取消 </el-button >   
		 </el-col>
	   </el-row>
 
	<el-table :data="planList" style="width: 100%">
	    <el-table-column   label="计划列表" >
			  <template #default="scope">
			       <span style="float: left;padding-bottom:5px;" v-for="(ware,index) in scope.row.warehouseList" :id="ware.id">
					  <el-icon v-if="index>0"><Plus /></el-icon>
										  <el-tag  round
												  :type="types[index]"
												  effect="plain"
											  >
											   {{ ware.name }}
											  </el-tag>
			       					 
			       </span>
			  </template>
		</el-table-column>
	   <el-table-column   label="操作" width="180" >
	   	  <template #default="scope">
			  <el-button type="primary" link @click="handleEdit(scope.row)">编辑 </el-button >
			  <el-popover :visible="scope.row.visible"  
			     trigger="click" placement="top" :width="260">
			      <p>删除计划将导致仓库下加入计划的SKU丢失！</p>
			      <div style="text-align: right; margin: 0">
			        <el-button size="small" text @click="scope.row.visible = false">取消</el-button>
			        <el-button size="small" type="primary" @click="handleDelete(scope.row)">删除</el-button>
			      </div>
			      <template #reference>
					   <el-button type="primary" @click="scope.row.visible = true" link >删除 </el-button >
			      </template>
			    </el-popover>
	   	  </template>
	   </el-table-column>
	  </el-table>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCLose">关闭</el-button>
      </span>
    </template>
  </el-dialog>	  
</template>
<script setup>
    import { ref ,reactive,onMounted,toRefs} from 'vue';
	import {getPlanList,savePlan,updatePlan,deletePlan} from '@/api/erp/purchase/plan/planApi.js';
	import { ArrowDown,Plus,Edit } from '@element-plus/icons-vue';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import { ElMessage ,ElMessageBox} from 'element-plus'
	const state = reactive({
				     planList:[],
					 dialog:{visible:false},
					 plan:{},
					 editplan:{},
					 isedit:false,
					 types:['success','info','danger','warning',
					        'success','info','danger','warning'],
					 selfUsable:[],
					 checkedSelfUsable:[],
					 selfTest:[],
					 overseaUsable:[],
					 overseaTest:[],
					 planwarehouseids:[],
					 planwarehouse:{},
			 
	         });
	const {
	  planList,plan,editplan,types,isedit,dialog,selfUsable,selfTest,
	  overseaUsable,overseaTest,planwarehouse,planwarehouseids
	} = toRefs(state);
	const emit = defineEmits(['change']);
	function change(item){
		state.plan=item;
		emit("change",item.id,item);
	}
	function handleCheckedChange(warehouselist){
		warehouselist.forEach(warehouse=>{
			if(warehouse.checked){
				if(!state.planwarehouseids.includes(warehouse.id)){
					state.planwarehouseids.push(warehouse.id);
					state.planwarehouse[warehouse.id]=warehouse;
				}
			}else{
				var index = state.planwarehouseids.indexOf(warehouse.id);
					if (index > -1) {
						state.planwarehouseids.splice(index, 1);
						state.planwarehouse[warehouse.id]=null;
					}
			}
		});
	} 
	function warehouseInit(warehouseList){
		if(warehouseList&&warehouseList.length>0){
			warehouseList.forEach(warehouse=>{
				if(state.planwarehouseids.includes(warehouse.id)){
					warehouse.checked=true;
				}else{
					warehouse.checked=false;
				}
			})
		}
	}
	function handleCLose(){
		state.dialog.visible = false
		state.planList.forEach((item)=>{
			item.visible=false
		})
	}
	function handleWarehouseInit(){
		warehouseInit(state.selfUsable)
		warehouseInit(state.selfTest);
		warehouseInit(state.overseaUsable);
		warehouseInit(state.overseaTest);
	}
	function handleEdit(row){
		state.isedit=true;
		state.editplan=row;
		state.planwarehouseids=[];
		state.planwarehouse={};
		row.warehouseList.forEach(warehouse=>{
			state.planwarehouseids.push(warehouse.warehouseid);
			state.planwarehouse[warehouse.warehouseid]=warehouse;
		});
		if(state.selfUsable&&state.selfUsable.length>0){
			state.selfUsable.forEach(warehouse=>{
				if(state.planwarehouseids.includes(warehouse.id)){
					warehouse.checked=true;
				}else{
					warehouse.checked=false;
				}
			})
		}
		handleWarehouseInit();
	}
	function handleDelete(row){
		deletePlan({"id":row.id}).then(res=>{
			 row.visible = false;
			 loadData();
			 ElMessage.success("删除成功");
		});
	}
	function handleCancel(row){
		state.isedit=false;
		state.planwarehouseids=[];
		state.planwarehouse={};
	}
	function handleAdd(){
		state.isedit=true;
		state.editplan={};
		state.planwarehouseids=[];
		state.planwarehouse={};
		handleWarehouseInit();
	}
	function submitForm(){
		state.editplan.warehouseList=[];
		state.planwarehouseids.forEach(id=>{
			state.planwarehouse[id].warehouseid=id;
			state.editplan.warehouseList.push(state.planwarehouse[id]);
		});
		if(state.editplan.id){
			updatePlan(state.editplan).then(res=>{
				 ElMessage.success("更新成功");
				 loadData();
			});
		}else{
			state.editplan.number="123456";
			savePlan(state.editplan).then(res=>{
				  ElMessage.success( "保存成功");
				  loadData();
			});
		}
	}
	async function showDailog(){
		await warehouseApi.getWarehouse({ftype:"self_usable"}).then(res=>{state.selfUsable=res.data});
		await warehouseApi.getWarehouse({ftype:"self_test"}).then(res=>{state.selfTest=res.data});
		await warehouseApi.getWarehouse({ftype:"oversea_usable"}).then(res=>{state.overseaUsable=res.data});
		await warehouseApi.getWarehouse({ftype:"oversea_test"}).then(res=>{state.overseaTest=res.data});
		state.dialog.visible=true;
	}
	function loadData(){
		getPlanList().then(res=>{
			if(res.data&&res.data.length>0){
				state.planList=res.data;
				state.plan=res.data[0];
				emit("change",state.plan.id,state.plan);
			}else{
				state.planList=[];
				state.plan={};
				emit("change","",state.plan);
			}
		});
	}
	onMounted(()=>{
		loadData();
	});
</script>

<style>
.purchasePlanIcon{
	margin-left:16px;
	padding-top:3px;
} 
.flex-star{
display: flex;
align-items: flex-start;

}
.pull-right{
	float:right;
	margin-top:10px;
	margin-bottom:10px;
	margin-right:10px;
}
.row-list-item{
	padding-top:20px;
}
.m-r-0{
	margin-right: 0px!important;
}
.m-b-16{
	margin-bottom:16px;
}
.w-120{
	width: 120px;
	margin-top: 5px;
	flex:none;
}
</style>