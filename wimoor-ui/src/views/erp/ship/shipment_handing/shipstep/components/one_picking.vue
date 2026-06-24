<template>
	<div>
		<el-table :data="productData.list" border style="width: 100%;margin-bottom:16px;">
			<el-table-column prop="image" label="图片" width="70">
				<template #default="scope">
					<img :src="scope.row.image" @click.stop="handleToMaterial(scope.row)" style="width:40px;height:40px" />
				</template>
			</el-table-column>
			<el-table-column label="名称/SKU"  show-overflow-tooltip>
				<template #default="scope"> 
					<div class='name text-omit-1'>{{scope.row.name}}</div>
					<div class='sku pointer'   @click.stop="showPrintModal(scope.row)">{{scope.row.sellersku}} 
					<el-tag v-if="scope.row.issfg=='1'" type="success">组装</el-tag>
					<el-tag v-if="scope.row.boxnum&&shipInfo.arecasesrequired">原装单箱：{{scope.row.boxnum}}</el-tag>
					<span v-if="scope.row.prep&&scope.row.prep.prepInstructionList&&scope.row.prep.prepInstructionList.length>0">
						<el-tag style="margin-left:3px;" v-for="prepitem in scope.row.prep.prepInstructionList">
							 <span v-if="PrepInstruction[prepitem]" >{{PrepInstruction[prepitem]}}</span>
							 [{{prepitem}}]
						</el-tag>
					</span>
					</div>
				</template>
			</el-table-column>
						<el-table-column prop="quantity" label="拟发货数量" width="100"   />
						<el-table-column prop="invquantity" label="库存" width="100"   />
						<el-table-column prop="invquantity_outbound" label="待出库" width="100"   />
			<el-table-column  label="实际配货数量" width="200">
				<template #default="scope">
					<el-input :disabled="true" v-model.number="scope.row.quantityshipped"></el-input>
				</template>
			</el-table-column>
		</el-table>
		<el-row :gutter="16" class="mar-bot">
			<el-col :span="6">
				<el-card  shadow="never" class="text-center">
					<span class="el-dropdown">
					<div class="icon-prin">
					<shopping-cart-del theme="outline" size="24" fill="#FF6700" :strokeWidth="3"/>
					<span @click="handleShelf"  class="el-dropdown-link">下架配货产品</span>
					</div>
					</span>
				</el-card>	
			</el-col>	
			<el-col :span="6">
				<el-card  shadow="never" class="text-center">
					<el-dropdown>
						<div class="icon-prin">
						<printer theme="outline" size="24" fill="#FF6700" :strokeWidth="3"/>
					    <span class="el-dropdown-link">
					     打印配货单
					      <el-icon class="el-icon--right">
					        <arrow-down />
					      </el-icon>
					    </span>
						</div>
					    <template #dropdown>
					      <el-dropdown-menu>
							  <el-dropdown-item @click="openShipmentInfo('simple')">简洁版配货单</el-dropdown-item>
							  <el-dropdown-item @click="openShipmentInfo('detail')">详细版配货单</el-dropdown-item>
					          <el-dropdown-item @click="openShipmentInfo('mobile')">移动版配货单</el-dropdown-item>
					      </el-dropdown-menu>
					    </template>
					  </el-dropdown>
				</el-card>
			</el-col>
			<el-col :span="6">
				<el-card  shadow="never" class="text-center">
					<el-dropdown>
						<div class="icon-prin">
						<add-print theme="outline" size="23" fill="#FF6700" :strokeWidth="3"/>
					    <span class="el-dropdown-link">
					     打印产品标签
					      <el-icon class="el-icon--right">
					        <arrow-down />
					      </el-icon>
					    </span>
						</div>
					    <template #dropdown>
					      <el-dropdown-menu>
					        <el-dropdown-item @click="downloadLabel('pdf')">PDF格式</el-dropdown-item>
					        <el-dropdown-item @click="downloadLabel('excel')">Excel格式</el-dropdown-item>
					      </el-dropdown-menu>
					    </template>
					  </el-dropdown>
				</el-card>
			</el-col>
			<el-col :span="6">
				<el-card  shadow="never" class="text-center">
					<el-dropdown>
						<div class="icon-prin">
						<inbox-out theme="outline" size="24" fill="#FF6700" :strokeWidth="3"/>
						<span class="el-dropdown-link">
						    {{boxconName}}
						  <el-icon class="el-icon--right">
						    <arrow-down />
						  </el-icon>
						</span>
						</div>
					    <template #dropdown>
					      <el-dropdown-menu>
					        <el-dropdown-item @click="changeboxcontents('FEED')">系统提交装箱</el-dropdown-item>
					        <el-dropdown-item @click="changeboxcontents('NONE')">亚马逊后台提交装箱</el-dropdown-item>
					      </el-dropdown-menu>
					    </template>
					  </el-dropdown>
				</el-card>
			</el-col>
		</el-row>
		  <el-alert   type="info " show-icon class="mar-bot">
			  <template #default>
				  <el-space direction="vertical" alignment="left">
				 <p>1．点击发货前请确保发货实物的SKU和数量完全与创建的货件一致，不然会影响您的卖家绩效和商品销售，可点击上方“打印配货单” 打印纸质清单与实物核对。</p>
				<p>2．若不一致，需要在点击发货前请修改已有商品发货数量； （修改的数量浮动只能在 5% 或 6 个SKU）</p>	
			</el-space>
			  </template>  
		  </el-alert>
		   <el-row >
		  <el-space>
			    <ShipmentOpt ref="optRef"  />
				 <el-button @click="validateShipment" :loading="validateLoading" >验证货件是否正确</el-button>	 
				 <el-button @click="loadShipmentPrep" >查看并修复预备信息</el-button>
		  </el-space>
		  <div class="rt-btn-group">
			  <el-button type="primary"  @click="stepclick(2)" plain>下一步</el-button>
		  </div>
		  </el-row>
		  <el-row v-if="hasAssembly" class="m-t-16">
			  <el-col :span="24">
				  <el-alert
				    @click.stop="handleToAssemblyForm" 
				     type="warning"
				     description="系统已经入库可组装库存，请在该货件发货前，完成相应主SKU的组装任务，"
				     show-icon
				  >
				  <template #title>
					  该货件生成了1个组装任务,点击查看组装单. <span class="text-red">（注意：取消货件时，实际发货数量小于计划发货数量，自动组装将不会撤销）</span>
				  </template>
				   </el-alert>
			   </el-col>
		  </el-row>
		   
	</div>
	<PrintDialog ref="printRef"></PrintDialog>
</template>

<script setup>
	import {Search,ArrowDown,WarningFilled} from '@element-plus/icons-vue'
	import {Printer,AddPrint,InboxOut,ShoppingCartDel} from '@icon-park/vue-next';
	import { ref,reactive,onMounted } from 'vue'
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import { useRoute,useRouter } from 'vue-router'
	import ShipmentOpt from"./shipment_operator.vue";
	import PrintDialog from"./print_dialog.vue";
	import {downloadShipmentLabel} from '@/hooks/amazon/listing/label.js';
	import {assemblyShipment} from '@/api/erp/assembly/assemblyApi.js';
	import PrepInstruction from '@/model/amazon/ship/PrepInstruction.json';
		const emit = defineEmits(['stepdata']);
		const printRef=ref();
		let router = useRouter()
		let productData=reactive({list:[]})
		let shipInfo=ref({})
		let validateLoading=ref(false);
		let hasAssembly=ref(false);
		let boxcontents=ref("FEED")
		let optRef=ref();
		let piceDisable=ref(false);
		let confirmloading=ref(false);
		let boxconName=ref("系统提交装箱");
		
			function openShipmentInfo(ftype){
					if(ftype=="mobile"){
						var path={path:"/shipment_handing/pehuo","query":{"shipmentid":shipInfo.value.shipmentid, title:"配货单",path:"/shipment_handing/pehuo"}};
						router.push(path);
					}else{
						//下载pdf
						shipmenthandlingApi.downPDFShipForm({
							"shipmentid":shipInfo.value.shipmentid,
							"ftype":ftype
						}).then(res => {
								ElMessage.success('导出成功！');
							 const blob = new Blob([res]);
							 if(window.navigator["msSaveOrOpenBlob"]&&window.navigator.msSaveOrOpenBlob()){
								navigator.msSaveBlob(blob, "shipmentDdetail.pdf")
							 }else{
								 var link=document.createElement("a");
								 link.href=window.URL.createObjectURL(blob);
								 link.download="shipmentDdetail.pdf";
								 link.click();
								 window.URL.revokeObjectURL(link.href);
							 }
						
						}).catch(e=>{
								ElMessage.error('导出失败！');
						})
					}
				
			}
			function downloadLabel(ftype){ 
				downloadShipmentLabel(shipInfo.value.shipmentid,ftype,null);
			}
			function changeboxcontents(value){
				boxcontents.value=value;
				if(value=="FEED"){
					boxconName.value="系统提交装箱";
				}else{
					boxconName.value="亚马逊后台提交装箱";
				}
			}
			function qtyconfirm(){
				var lists=[];
				confirmloading.value=true;
				productData.list.forEach(function(item,index){
					var map={};
					map.sellersku=item.sellersku;
					map.quantityshipped=item.quantityshipped;
					map.shipmentid=shipInfo.value.shipmentid;
					lists.push(map);
				})
				shipmenthandlingApi.updateShipment({
					"shipmentid":shipInfo.value.shipmentid,
					"intendedBoxContentsSource":boxcontents.value,
					"itemList":lists
				}).then(res=>{
					confirmloading.value=false;
					emit("stepdata",1);
				}).catch(error=>{
					confirmloading.value=false;
				})
			}
			
			function loadShipmentPrep(){
				shipmenthandlingApi.loadShipmentPrep({
					"shipmentid":shipInfo.value.shipmentid
				}).then(res=>{
					if(res&&res.data){
						productData.list.forEach(item=>{
							item.prep=res.data[item.sellersku];
						});
					}
					
					 
				}) ;
			}
			
			function validateShipment(){
				validateLoading.value=true;
				shipmenthandlingApi.validateShipment({
					"shipmentid":shipInfo.value.shipmentid
				}).then(res=>{
					validateLoading.value=false;
					 ElMessage.success('验证成功,该货件未发现问题！');
				}).catch(error=>{
					validateLoading.value=false;
				})
			}
			function loadOptData(data){
				optRef.value.loadOptData(data.shipmentAll);
				data.shipmentid=data.shipment.shipmentid;
				shipInfo.value=data;
				productData.list=data.itemlist;
				if(shipInfo.value.shipmentAll.status=="2" || shipInfo.value.shipmentAll.status=="3" || shipInfo.value.shipmentAll.status=="4"){
					piceDisable.value=false;
				}else{
					piceDisable.value=true;
				}
				if(shipInfo.value.intendedBoxContentsSource=="NONE"){
					boxconName.value="亚马逊后台提交装箱";
				}else{
					boxconName.value="系统提交装箱";
				}
				assemblyShipment({"shipmentid":data.shipmentid}).then(res=>{
					if(res.data&&parseInt(res.data)>0){
						hasAssembly.value=true;
					}
				})
			}
			function stepclick(step){
				emit("stepdata",step);
			}
			function handleToAssemblyForm(){
				router.push({
					path:'/erp/purchase/process',
					query:{
					  shipmentid:shipInfo.value.shipmentid,
					  title:"加工单",
					  path:'/erp/purchase/process',
					}
				})
			}
			function handleShelf(){
				router.push({
					path:'/erp/ship/quota',
					query:{
					  shipmentid:shipInfo.value.shipmentid,
					  title:"配货单下架",
					  path:'/erp/ship/quota',
					}
				})
			}
			function handleToMaterial(row){
				 router.push({
					path:'/material/details',
					query:{
					  title:"产品详情",
					  path:'/material/details',
					  details:row.mid,
					}
				 })
			}
			function showPrintModal(row){
				printRef.value.show(row,shipInfo.value.shipment);
			}
			  
		  defineExpose({loadOptData})
</script>

<style scoped>
	.icon-prin{
		display: flex;
		align-items: center;
	}
	.icon-prin .el-dropdown-link{
		margin-left: 8px;
		color: var(--el-color-primary);
	}
	.m-t-16{
		margin-top:16px;
	}
</style>
