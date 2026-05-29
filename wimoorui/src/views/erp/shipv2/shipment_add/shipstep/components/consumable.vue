<template>
	<el-dialog v-model="materialDialogVisible" width="50%" >
		  <template #header>
			  <span v-if="myshowtype!='isshow'">确认出库</span>
			  <span v-else>辅料与箱子出库详情</span>
		    </template>
		<el-card>
			 <template #header>
			      <div class="card-header">
			        <span>辅料列表</span>
					<Warehouse style="margin-left:10px;" size="small" defaultValue="only"
					 :defaultid="form.consWarehouseid"  @change="changeWarehouse"  />
					 <el-button type="primary" size="small" style="float: right;" @click.stop="handleAdd">添加辅料</el-button>
			      </div>
			</template>
		<el-table :data="materiallist" border >
			<el-table-column prop="image" label="图片" width="100" >
				<template  #default="scope">
					<el-image :src="scope.row.image"  style="width: 40px;height: 40px;"></el-image>
				</template>
			</el-table-column>
			<el-table-column prop="sku" label="SKU" width="100"  >
				<template  #default="scope">
					 {{scope.row.sku}}
				</template>
			</el-table-column>
			<el-table-column prop="mname" label="产品名称"     />
			<el-table-column prop="inventoryqty" label="产品库存" width="100"   />
			<el-table-column prop="need" label="扣减数量" width="100"  >
				<template  #default="scope">
				    <div v-if="scope.row.out">
						<span class="font-extraSmall">已出库：</span>
						<span class="text-success">{{scope.row.out}}</span>
					</div>
					<el-input v-else    v-model="scope.row.need" @input="scope.row.need=CheckInputInt(scope.row.need)" ></el-input>
				</template>
			</el-table-column>
			<el-table-column prop="residue" label="剩余待扣" width="100"  >
				<template  #default="scope">
					<el-input    v-model="scope.row.residue" @input="scope.row.residue=CheckInputFloat(scope.row.residue)" ></el-input>
				</template>
			</el-table-column>
			<el-table-column prop="residue" label="操作" width="100"  >
				<template  #default="scope">
				  <el-button type="link" @click.stop="deleteItem(scope.row,scope.$index)" >删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		</el-card>
		 <el-card v-if="!isassembly" style="margin-top:20px;">
			 <template #header>
			       <div class="card-header">
			         <span>箱子列表</span>
			         <Warehouse style="margin-left:10px;" size="small"  defaultValue="only"
					 :defaultid="form.boxWarehouseid"  @change="changeBoxWarehouse"  />
					  <el-button type="primary" size="small" style="float: right;" @click.stop="handleBoxAdd">添加箱子</el-button>
			       </div>
			 </template>
		<el-table :data="boxListData" border  >
			<el-table-column prop="length" label="长x宽x高(cm)"  >
			<template #default="scope">
				 {{scope.row.length}}x{{scope.row.width}}x{{scope.row.height}}
			</template>
			</el-table-column>
			<el-table-column prop="number" label="箱子数量"  >
			</el-table-column>
			<el-table-column prop="materialid" label="对应箱子SKU"  width="200" >
				<template  #default="scope">
					 <el-select v-model="scope.row.materialid" @change="handlePackageSkuChange(scope.row)">
						 <el-option v-for="item in packageSkuListData" :value="item.id" :key="item.id" :label="item.sku">{{item.sku}}</el-option>
					 </el-select>
				</template>
			</el-table-column>
			<el-table-column prop="number" label="库存"  >
				<template  #default="scope">
					 <div v-if="scope.row.packagesku">{{scope.row.packagesku.fulfillable}}</div>
				      <div v-else>0</div>
				</template>
				
			</el-table-column>
			<el-table-column prop="number" label="出库数量"   >
				<template  #default="scope">
					<div v-if="scope.row.packagesku&&scope.row.packagesku.out">
						<span class="font-extraSmall">已出库：</span>
						<span class="text-success">{{scope.row.packagesku.out}}</span>
					</div>
					<el-input v-else    v-model="scope.row.editnumber" @input="scope.row.editnumber=CheckInputInt(scope.row.editnumber)" ></el-input>
				</template>
			</el-table-column>
		</el-table>
		 	</el-card>
		<template #footer>
		  <span class="dialog-footer">
		    <el-button @click="materialDialogVisible = false">关闭</el-button>
			<el-button type="primary" v-if="myshowtype!='isshow'" :loading="submitloading" @click="submitMaterialCon()">提交出库</el-button>
		  </span>
		</template>
	</el-dialog>
	<!-- 辅料选择弹窗 -->
	 <MaterialDialog ref="materialDailogRef" :warehouseid="form.consWarehouseid" @getdata="getRows" type="consumable"  ></MaterialDialog>
	 <!-- 箱子选择弹窗 -->
	 <MaterialDialog ref="boxmaterialDailogRef" :warehouseid="form.boxWarehouseid" @getdata="getBoxRows" type="package"  ></MaterialDialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,nextTick,computed } from 'vue';
	import '@/assets/css/packbox_table.css'
	import { ElMessage,ElMessageBox } from 'element-plus';
	import { Search,ArrowDown,Close} from '@element-plus/icons-vue';
	import { useRoute,useRouter } from 'vue-router';
	import { pointKeyChnage} from '@/utils/jquery/key/point-key';
	import Warehouse from '@/components/header/warehouse.vue';
	import { formatFloat,CheckInputIntLGZero,CheckInputFloat,getValue,CheckInputInt} from '@/utils/index.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import materialApi from '@/api/erp/material/materialApi.js';
	import consumablesApi from '@/api/erp/material/consumablesApi.js';
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	const emit = defineEmits(['change']);
	let router = useRouter();
	let props = defineProps({isassembly:undefined });
	const {isassembly} = toRefs(props);
	const formid=router.currentRoute.value.query.id;
	const materialDailogRef=ref();
	const boxmaterialDailogRef=ref();
	let state =reactive({
		materialDialogVisible:false,
		materiallist:[],
		submitloading:false,
		myshowtype:"isshow",
		form:{consWarehouseid:""},
		boxListData:[],
		packageSkuListData:[],
		planData:null,
		recordList:[],
	});
	
	let{materialDialogVisible,materiallist,submitloading,myshowtype,form,boxListData,planData,packageSkuListData,
	recordList}=toRefs(state);
	function changeBoxWarehouse(val){
		state.form.boxWarehouseid=val;
	}
	
	function changeWarehouse(val){
		state.form.consWarehouseid=val;
	}
	function handlePackageSkuChange(row){
					 var packagesku="";
					  state.packageSkuListData.forEach(item=>{
						  if(item.id==row.materialid){
							  packagesku=item;
						  }
					  })
					 row.packagesku=packagesku;
					 setEditNumber(row);
	}
	
 
	
	async function openMaterialCon(showtype) {
		state.materialDialogVisible = true;
		 state.myshowtype=showtype;
		 state.form.consWarehouseid=state.planData.warehouseid;
		 state.form.boxWarehouseid=state.planData.warehouseid;
		//查询material的箱子列表
		if(!props.isassembly){
			await shipmentBoxApi.getAllBoxDim({"formid":state.planData.id}).then(res=>{
				state.boxListData=res.data;
			}) 
			
			await materialApi.packageListAll({'warehouseid':state.planData.warehouseid,'search':state.planData.number}).then((res)=>{
				 state.packageSkuListData=res.data;
				 state.boxListData.forEach(row=>{
					 state.packageSkuListData.forEach(item=>{
					 					if(row.length==item.length
					 					   &&row.width==item.width
					 					   &&row.height==item.height){	
											   row.materialid=item.id;
											   row.packagesku=item;
											   setEditNumber(row);
									      }
										  if(item.out){
											  state.myshowtype="isshow";
										  }
					 })
					 if(row.materialid==""){
						 state.packageSkuListData.forEach(item=>{
						 					if(row.length<=item.length+2&&row.length>=item.length+1
											   &&row.width<=item.width+2&&row.width>=item.width+1
											   &&row.height<=item.height+2&&row.height>=item.height+1
						 					   ){	
						 						   row.materialid=item.id;
												   row.packagesku=item;
												   setEditNumber(row);
						       }
						 })
					 }
				 })
				 
			});
		}
		
		
		var skulist=[];
		state.planData.itemlist.forEach(item=>{
			skulist.push({"sku":item.msku,"qty":item.confirmQuantity});
		});
		consumablesApi.getConsumableList({
			"warehouseid":state.planData.warehouseid,
			"number":state.planData.number,
			"skulist": skulist
		}).then(res => {
			if (res.data && res.data.length > 0) {
				if(res.data[0] && res.data[0].recordList){
					state.recordList=res.data[0].recordList;
				}
				state.materiallist = res.data;
				if(state.materiallist&&state.materiallist.length>0){
					state.materiallist.forEach(item=>{
						item.residue=item.need-parseInt(item.need);
						if(item.out){
						    item.need=parseInt(item.need)-parseInt(item.out);
							state.myshowtype="isshow";
						}else{
							item.need=parseInt(item.need);
						}
						if(item.need<0){
							item.need=0;
						}
					});
					
				}
			}else{
				state.materiallist = [];
			}
		})
	}
	
	function setEditNumber(row){
		 if(row.packagesku&&row.number>row.packagesku.fulfillable){
			  row.editnumber=parseInt(row.packagesku.fulfillable);
		 }else{
			   row.editnumber=parseInt(row.number);
		 }
		 if(row.packagesku&&(row.editnumber+row.packagesku.out)>row.number){
			 row.editnumber=parseInt(row.number-row.packagesku.out);
			 if( row.editnumber<0){
				 row.editnumber=0;
			 }
		 }
	}
		
	function feefunc(value1, value2) {
		return (parseFloat(value1)) * (parseFloat(value2));
	}
		
	function submitMaterialCon() {
		var conswarehouseid = state.form.consWarehouseid;
		var boxwarehouseid = state.form.boxWarehouseid;
		var skulist = [];
		var pkglist=[];
		var recordlist=[];
		var isok=true;
		var isout=false;
		state.materiallist.forEach(function(item) {
			var row = {}
			row.sku = item.sku;
			if(item.need){
				row.amount = parseInt(item.need);
			}else{
				row.amount =0;
			}
			if(item.out&&parseInt(item.out)>0){
				isout=true;
			}
			if(item.residue){
				row.residue = parseFloat(item.residue);
			}else{
				row.residue =0;
			}
			if(row.amount<0){
				isok=false;
			}
			if(item.inventoryqty<0){
				isok=false;
			}
			if(row.residue>=1){
				isok=false;
			}
			 
			if(!item.out&&parseInt(item.need)>item.inventoryqty){
				isok=false;
			}
			if(!item.out){
			    skulist.push(row);
			}
			state.recordList.forEach(rec=>{
				if(rec.sku==item.sku){
					recordlist.push(rec);
				}
			})
		});
		if(isok==false){
			ElMessage.error('剩余库存必须小于1或者辅料库存不足');
			return;
		}
		
		state.boxListData.forEach(function(items) {
			 if(items.editnumber&&items.materialid&&items.packagesku&&parseInt(items.editnumber)>0){
				 var row = {};
				 row.amount=items.editnumber;
				 row.warehouseid=boxwarehouseid;
				 row.materialid=items.materialid;
				 row.sku=items.packagesku.sku;
				if(!items.packagesku.out){
					     pkglist.push(row);
				  }
				
			 }
			
			
		});
		
		// if(withTransSubmit.value){
		// 	saveSelfTrans("submitbatch");
		// }
		if(isout){
			 ElMessageBox.confirm(
			    '已经完成辅料出库，确认将只执行货件出库操作?',
			    '注意',
			    {
			      confirmButtonText: '确认',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  )
			.then(() => {
			  //amazonDoneShipment();
			  ElMessage.success('操作成功！');
			})
		}else{
			shipmenthandlingApi.saveInventoryConsumable({
				"formid": formid,
				"warehouseid": conswarehouseid,
				"skulist":  skulist,
				"pkglist":pkglist,
				"consumablelist":recordlist,
				"number":state.planData.number
			}).then(res => {
					state.materialDialogVisible = false;
					ElMessage.success('操作成功！');
					emit('change');
			       // amazonDoneShipment();
			})
		}
		
	}
	function handleAdd(){
		 materialDailogRef.value.show();
	}
	
	function handleBoxAdd(){
		 boxmaterialDailogRef.value.show();
	}
	
	function deleteItem(row,index){
		if(state.materiallist.length==1){
			ElMessage.error( "请至少保留一行！")	;
			return;
		}
		 state.materiallist.splice(index,1);
	}
	function getBoxRows(selecteds){
		if(selecteds && selecteds.length>0){
			  selecteds.forEach(function(item){
				  var row={};
				  row.id=item.id;
				  row.materialid=item.id;
				  row.length=item.length;
				  row.width=item.width;
				  row.height=item.height;
				  row.sku=item.sku;
				  row.packagesku=item;
				  var ispush=true;
				  state.boxListData.forEach(function(items){
					  if(items.sku==row.sku){
						 ispush=false; 
					  }
				  });
				  if(ispush==true){
					   state.boxListData.push(row);
				  }
			  });
		}
	}
	
	function getRows(selecteds){
		 if(selecteds && selecteds.length>0){
			  selecteds.forEach(function(item){
				  var row={};
				  row.id=item.id;
				  row.sku=item.sku;
				  row.image=item.image;
				  row.mname=item.name;
				  row.inventoryqty=item.fulfillable;
				  row.amount=1;
				  var ispush=true;
				  state.materiallist.forEach(function(items){
					  if(items.sku==row.sku){
						 ispush=false; 
					  }
				  });
				  if(ispush==true){
					   state.materiallist.push(row);
				  }
			  });
		 }
		 
	}
	
	function show(showtype,data){
		state.planData=data;
		state.myshowtype=showtype;
		state.materialDialogVisible=true;
		openMaterialCon(state.myshowtype);
	}
	 defineExpose({ show })
</script>

<style>
</style>