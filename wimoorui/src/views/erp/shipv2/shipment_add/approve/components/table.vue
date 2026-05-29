<template>
	<div class="text-right" v-if="planData && planData.auditstatus<=2&&(!planData.checkInv)&&parseInt(planData.invstatus)<2" style="margin-bottom:10px"> 
	<el-button type="warning" v-if="!isappend" @click="showAppendCard" plain>追加新增SKU</el-button>
	<el-card  v-if="isappend"   style="margin-top:10px"><Fromlist ref="formlistRef" :isappend="isappend" @cancel="isappend=false" @change="handleAppendSubmit"/></el-card> 
	 </div>
	<el-card shadow="hover" style="margin-bottom: 10px;">
		<div>
			<el-descriptions
			    class="margin-top"
			    :column="4"
			    border
			  >
			    <el-descriptions-item>
			      <template #label>
			        <div class="cell-item"> sku数量 </div>
			      </template>
			     {{plansummary.skucount}}
			    </el-descriptions-item>
				<el-descriptions-item>
				  <template #label>
				    <div class="cell-item">  预估发货重量(kg) </div>
				  </template>
				  <span v-if="plansummary.weight && plansummary.weight!=''"> {{formatFloat(plansummary.weight)}}</span>
				  <span v-else>--</span>
				</el-descriptions-item>
				<el-descriptions-item>
				  <template #label>
				    <div class="cell-item"> 预估箱子体积(m³)
					<el-tooltip content="当维护箱规后此处是箱子体积,没有维护则使用产品体积预估总体积">
						<el-icon>
							<Warning></Warning>
						</el-icon>
					</el-tooltip>
					</div>
				  </template>
				  <span v-if="plansummary.boxvolume && plansummary.boxvolume!='0E-10'"> {{formatFloat(plansummary.boxvolume)}}</span>
				  <span v-else>--</span>
				</el-descriptions-item>
				<el-descriptions-item>
				  <template #label>
				    <div class="cell-item">货值</div>
				  </template>
				  ￥{{plansummary.itemprice}}
				</el-descriptions-item>
				<el-descriptions-item>
				  <template #label>
				    <div class="cell-item"> 发货数量</div>
				  </template>
				  {{plansummary.toquantity}}
				</el-descriptions-item>
			    <el-descriptions-item>
			      <template #label>
			        <div class="cell-item"> 预估运输重量(kg)</div>
			      </template>
				  <span v-if="plansummary.readweight && plansummary.readweight!=''"> {{formatFloat(plansummary.readweight)}}</span>
				  <span v-else>--</span>
			    </el-descriptions-item>
			  </el-descriptions>
		</div>
		<el-table :data="itemlist" border style="width: 100%;margin-top:16px;" :height="planData && planData.auditstatus<=2&&(!planData.checkInv)&&parseInt(planData.invstatus)<2?'calc(100vh - 330px)':'calc(100vh - 290px)'" :scrollbar-always-on="true">
		    <el-table-column prop="image" label="图片" width="68" >
		     <template #default="scope">
		      <el-image :src="scope.row.image"  class="product-img"   ></el-image>
		    </template>
		  </el-table-column>
		  <el-table-column prop="name" label="名称/SKU"  show-overflow-tooltip>
		     <template #default="scope">
		        <div class='name'>{{scope.row.name}}</div>
		        <div class='sku'>{{scope.row.sku}}
		          <copy title='复制' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
				  <span v-if="scope.row.issfg=='1'"   @click.stop="handleEmtpy">
				  	<el-tag
				  	 title="组合产品"
				  	 @click.stop="e=>handleAssemblyShow(e,scope.row)"
				  	 @mouseenter="e=>handleAssemblyShow(e,scope.row)"
				  	 type="warning" class="pointer" v-if="scope.row.issfg=='1'"
				  	 size="small" 
				  	>组合</el-tag>
				  </span>
		        </div>
		    </template>
		  </el-table-column>
		  <el-table-column prop="msku"  label="本地SKU" width="150"  />
		  <el-table-column prop="typename" label="分类"  show-overflow-tooltip width="160" />
		  <el-table-column prop="invquantity" label="可用库存"  width="100" />
		    <el-table-column prop="avgsales" label="预估日均销量"  width="140" />
		  <el-table-column prop="quantity" label="发货数量"   width="100" >
			  <template #default="scope">
				  {{scope.row.quantity}}
				  <!-- <div class="font-extraSmall" :title="'预估配置费:￥'+scope.row.inplacefeeCny" v-if="scope.row.inplacefee">预估:${{scope.row.inplacefee}}</div> -->
			  </template>
			  </el-table-column>
		  <el-table-column prop="quantity" label="商品详情"  width="110"   sortable>
		      <template #default="scope">
		          <el-button class='el-button--blue' @click="showProductModal(scope.row.sku)">查看</el-button>
		      </template>
		  </el-table-column>
		</el-table>
	</el-card>
	 <MateiralDialog ref="mateiralDialogRef" @getdata="getdata"></MateiralDialog>
	
	 <GoodsDeatiles ref="goodsDeatilsRef"  />
	 <SplitDialog ref="splitDialogRef" />
	 <!-- 组装产品 -->
	 <AssemblyDialog :assbRef="assbRef" @change="getAssembliyList(editRow)" :loading="editRow.assloading"
	  :assemblyList="editRow.assemblyList"/>
</template>
<script setup>
	import { ElDivider } from 'element-plus';
	import {CopyDocument,Crop,Plus,Edit,Warning} from '@element-plus/icons-vue';
	import {h, ref ,watch,reactive,onMounted,computed,inject ,toRefs} from 'vue';
	import { useRoute,useRouter } from 'vue-router';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
    import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import GoodsDeatiles from "@/views/amazon/listing/common/goods_deatils.vue";
	import AssemblyDialog from "@/views/erp/components/assembly_dialog.vue";
	import SplitDialog from "./split_dialog.vue";
	import Fromlist from"@/views/erp/shipv2/shipment_add/create/components/form.vue"
	import {ElMessage,ElMessageBox } from 'element-plus';
	import {formatFloat} from '@/utils/index.js';
	import {sublit} from "@/api/erp/assembly/assemblyApi.js";
	import MateiralDialog from "@/views/erp/shipv2/shipment_add/create/components/mateiralDialog.vue";	
			const emit = defineEmits(['change']);
			let router = useRouter()
			let spacer = h(ElDivider, {direction: 'vertical'})
			let planData=ref();
			let itemlist=ref([]);
			let userchannelList=ref([]);
			let companylist=ref([]);
			let channellist=ref([]);
			let goodsDeatilsRef =ref()
			let splitDialogRef=ref();
			let productInfoData=ref({});
			let splitVisible=ref(false);
			let reApproveloading=ref(false);
			let expclicks=0;
			let localshipmentid="";
			let plansummary=ref({});
			let editRow=ref({});
			const assbRef =ref();
			const mateiralDialogRef=ref();
			const formlistRef=ref();
		    const emitter = inject("emitter"); // Inject `emitter`
			let state=reactive({ isappend:false, })
			let{isappend }=toRefs(state)
			function rollbackShipnum(row){
				  row.copshipnum = row.quantity
			}
		
			function addProduct(){
				mateiralDialogRef.value.dialogVisible=true;
				var data={};
				data.search='';
				data.warehouseid=planData.value.warehouseid;
				data.ftype='shipment';
				data.marketplaceid=planData.value.marketplaceid;
				data.groupid=planData.value.amazongroupid;
				mateiralDialogRef.value.ftype='shipment';
				mateiralDialogRef.value.params=data;
				mateiralDialogRef.value.loadData();
			}
			function submitShipmentName(id,name){
				//ajax修改名字
				updateShipmentRemark(id,'',name);
			}
			function submitShipmentRemark(id,remark){
				//ajax修改备注
				updateShipmentRemark(id,remark,'');
			}
			function showAppendCard(){
				var formData={};
				formData.warehouseid=planData.value.warehouseid;
				formData.marketplaceid=planData.value.marketplaceid;
				formData.groupid=planData.value.amazongroupid;
				state.isappend=true
				setTimeout(function(){formlistRef.value.init(formData);},300);
				
			}
			function handleAppendSubmit(formstate){
				var param=[];
				formstate.formData.planitemlist.forEach(item=>{
					var paramitem={"sku":item.sku,
					               "msku":item.msku,
					               "quantity":item.quantity,
					               "formid":planData.value.id,
					               "confirmQuantity":item.quantity ,
								   "prepOwner":item.prepOwner,
								   "labelOwner":item.labelOwner,
								   "expiration":item.expiration};
					param.push(paramitem);
				})
				shipmentplanApi.approveInboundPlanAppend(param).then(res=>{
					 
						formstate.submitloading=false;
						state.isappend=false;
					    emit("change");
				});
			}
			function handleItemRemark(item){
				item.editremark=item.remark;
				if(!item.editremark){
					item.editremark=planData.value.remark;
				}
			}
			function getdata(data){
			 				 if(data.length>0){
			 					 var skulist="";var oldskulist="";
			 					 if(itemlist.value.length>0){
			 						 itemlist.value.forEach(function(items){
			 						 	 oldskulist+=(items.sku+",");
			 						 }); 
			 					 }
			 					 data.forEach(function(item){
			 						 var haslength=oldskulist.indexOf(item.sku+",");
			 						 if(haslength<0||oldskulist==""){
			 							   skulist+=(item.sku+",");
			 							   if(item.msku==undefined || item.msku==null || item.msku==''){
			 							   	 item.msku=item.sku;
			 							   }
										   item.isEdit=true;
										   item.areCasesRequired="0";
										   item.prepOwner="SELLER";
										   item.labelOwner="SELLER";
			 							   itemlist.value.push(item);
			 						 }
			 						
			 					 });
								 
			 				 }
			 				  
			}
			function getchannelList(){
				transportationApi.channelList().then((res)=>{
					userchannelList=res.data;
				});
			}
			
			function toTraceShipment(shipmentid){
					router.push({
						path:'/shipment_handing/shipstep',
						query:{
						  'shipmentid':shipmentid,
						  title:"发货流程"+shipmentid,
						  path:'/shipment_handing/shipstep',
						}
					});
			}
			async function initData(shipitem){
				itemlist.value=shipitem.itemlist;
				plansummary.value=shipitem.plansummary;
			 }
		 
			
			function companyChange(index){
					 let shipment=itemlist.list[index];
					 if(shipment.companyid){
						transportationApi.getChannel({"company":shipment.companyid,"marketplaceid":planData.value.marketplaceid,"transtype":""}).then((res)=>{
							let channelid="";
							if(res.data&&res.data.length>0){
								 shipment.channellist=res.data;
								 shipment.chang=new Date();
								 itemlist.list[index]=shipment;
								 channelid=res.data[0].id;
							}else{
								shipment.channellist=[];
								shipment.chang=new Date();
								itemlist.list[index]=shipment;
							}
							shipment.channelid=channelid;
						})
					 }
					
			}
			function showProductModal(sku){
				//根据 groupid marketplaceid sku去查product
				var groupid=planData.value.amazongroupid;
				var marketplaceid=planData.value.marketplaceid;
				var data={};
				data.groupid=groupid;
				data.marketplace=marketplaceid;
				data.sku=sku;
			    goodsDeatilsRef.value.show(data);
			}
			function copyPlan(shipmentid){
				emitter.emit("removeCache", "添加货件");
				router.push({
					path:'/invoice/addshipment',
					query:{
						shipmentid:shipmentid,
						title:'添加货件',
						path:'/invoice/addshipment',
					}
				}) 
			}
 
			function updateShipmentRemark(id,remark,name){
				shipmentplanApi.updateShipmentRemark({"shipmentid":id,"remark":remark,"name":name}).then((res)=>{
					 if(res.data && res.data=="ok"){
						ElMessage.success( '修改成功！');
							itemlist.list.forEach(item=>{
								if(item.shipmentId==id){
									item.remark=remark;
									item.remarkVis=false;
								}
							}) 
					 }else{
						 ElMessage.error('修改失败！');
					 }
				});
			}
			
			
			function saveTrans(item){
				var companyid=item.companyid?item.companyid:"";
				var channelid=item.channelid?item.channelid:"";
				var transtyle=item.transtyle?item.transtyle:"";
				var shipmentid=item.shipmentId;
				if(channelid || transtyle){
					shipmentplanApi.saveTrans({
					"company":companyid,
					"channel":channelid,
					"transtyle":transtyle,
					"shipmentid":shipmentid}).then((res)=>{
						item.remarkVis=false;
						if(res.data && res.data=="success"){
							 ElMessage.success('修改成功！');
							 emit("change");
						}else{
							ElMessage.error('修改失败！');
						}
					});
				}
			}
			function percentformat(value){
				if(value && value!=0){
					return (value*100).toFixed(2)+'%';
				}else{
					return 0;
				}
			}
			function showExceptionDialog(shipid){
				localshipmentid=shipid;
				if(localshipmentid==shipid){
					expclicks++;
				}else{
					expclicks=1;
				}
				if(expclicks>=3){
					ElMessageBox.confirm(
					   '处理后产品待出库会增加，货件号:'+shipid+'，确定操作吗？',
					   '处理未扣待出库货件',
					   {
					     confirmButtonText: '确定',
					     cancelButtonText: '取消',
					     type: 'warning',
					   }
					 )
					   .then(() => {
					     shipmentplanApi.handlerExpShipment({"shipmentid":shipid}).then((res)=>{
									  if(res.data){
										  ElMessage.success("操作成功!");
										  expclicks=0;
									  }else{
										  ElMessage.error( "操作失败");
									  }
								  });
					   })
					   .catch(() => {
					     ElMessage.info( '取消删除');
					   })
				}
			}
			
			function getAssembliyList(row){
				if(!row["assemblyList"]){
					row.assloading=true;
					sublit({materialid:row.materialid,warehouseid:row.warehouseid}).then(res=>{
						row.assemblyList=res.data;
						row.assloading=false;
					});
				}
			}
			function handleAssemblyShow(e,row){
				 const evt = e || window.e || window.event;
				 assbRef.value = evt.currentTarget
				 editRow.value = row;
				 editRow.value.oldwarehouseid=editRow.value.warehouseid;
			}
			
			 defineExpose({initData,planData });
	 
</script>

<style>
.subtext{
	margin-top:8px;
}
.remark{margin-bottom:16px;}
.card-header{
	display: flex;
	justify-content: space-between;
}
.inp-box{
	margin-bottom:8px;
}
.sh-ri{
	flex:1;
}
.sh-le{
	padding-left:16px;
	width:400px;
	border-left: 1px solid var(--el-border-color-base);
	margin-left:16px;
}
.sh-le p{
	font-size: 12;
	font-weight: 600;
}
.sh-con-tw{
	display: flex;
}
.ma-bo-16{
	margin-bottom:16px;
}
.m-t-8{
	margin-top: 8px;
}
</style>
