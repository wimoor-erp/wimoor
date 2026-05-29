<template>
	<el-dialog v-model="dialogTransVisible" class="transdailog"   width="80%" top="1vh" center>
		<div v-loading="loading"  >
		<div v-if="systemType=='ZH'">
		  	   	<el-col :span="24">
			    <el-card   style="margin-bottom:10px;margin-top:-30px;">
					<el-descriptions :column="3" border>
					  <el-descriptions-item>
					      <template #label>
					        <div class="cell-item">
					          服务类型
					        </div>
					      </template>
					      {{shipment.service_name}}
					    </el-descriptions-item>
					    <el-descriptions-item>
					      <template #label>
					        <div class="cell-item">
					          客户订单号
					        </div>
					      </template>
					      {{shipment.shipment_id}} / {{shipment.client_reference}}
					    </el-descriptions-item>
					    <el-descriptions-item>
					      <template #label>
					        <div class="cell-item">
					          运费
					        </div>
					      </template>
					      ￥ {{shipment.charge_amount}}
					    </el-descriptions-item>
					    <el-descriptions-item>
					      <template #label>
					        <div class="cell-item">
					          状态
					        </div>
					      </template>
					      <el-tag size="small"> {{shipment.status}}</el-tag>
					    </el-descriptions-item>
					    <el-descriptions-item>
					      <template #label>
					        <div class="cell-item">
					         箱数
					        </div>
					      </template>
					      {{shipment.parcel_count}}
					    </el-descriptions-item>
						<el-descriptions-item>
						  <template #label>
						    <div class="cell-item">
						     申报数量
						    </div>
						  </template>
						  {{shipment.sumqty}}
						</el-descriptions-item>
						<el-descriptions-item>
						  <template #label>
						    <div class="cell-item">
						     申报价格
						    </div>
						  </template>
						  {{shipment.sumprice}}
						</el-descriptions-item>
					  </el-descriptions>
			   	    </el-card>
			</el-col>
			
		  <el-row :gutter="20" style="margin-bottom:10px">
				 <el-col :span="12">
					 <el-card >
						  <el-scrollbar height="240px">
						     <el-descriptions
						         class="margin-top"
						         :column="1"
								  border
						       >
						         <el-descriptions-item label="收件人">
						     								<div v-if="shipment.to_address" style="min-height: 100px;">
						     								<div v-if="shipment.to_address.name">{{shipment.to_address.name}}</div>
						     								<div v-if="shipment.to_address.address_1">{{shipment.to_address.address_1}}</div>
						     								<div v-if="shipment.to_address.city_1">{{shipment.to_address.city_1}}</div>
						     								{{shipment.to_address.postcode}} {{shipment.to_address.country}}
						     								</div>
						     								<div v-else style="min-height: 100px;">
						     									暂无发件人信息
						     								</div>
						     							</el-descriptions-item>
						         <el-descriptions-item label="发件人">
						     								 <div v-if="shipment.from_address" style="min-height: 100px;">
						     								 	<div v-if="shipment.from_address.name">{{shipment.from_address.name}}</div>
						     								 	<div v-if="shipment.from_address.address_1">{{shipment.from_address.address_1}}</div>
						     								 	<div v-if="shipment.from_address.city_1">{{shipment.from_address.city_1}}</div>
						     								 	{{shipment.from_address.postcode}} {{shipment.from_address.country}}
						     								 </div>
						     								 <div v-else style="min-height: 100px;">
						     								 	暂无发件人信息
						     								 </div>
						     							 </el-descriptions-item>
						       </el-descriptions>
						   </el-scrollbar>
					 </el-card>
				</el-col>
				<el-col :span="12">
					 <el-card >
					  <el-scrollbar height="240px">
						<div class="box-title">路由信息 </div>
						 <el-timeline style="max-width: 600px;margin-top:20px">
						    <el-timeline-item
						      v-for="traces,index in shipment.traces"
						      :key="index"
						      :timestamp="dateFormat(traces.time*1000)"
						    >
						    <span>{{shipment.carrier_code}}</span>
						    							#<span>{{shipment.tracking_number}}</span>
														{{ traces.info }}
						    </el-timeline-item>
						  </el-timeline>
						 </el-scrollbar>
						</el-card>
				</el-col>
			 </el-row> 
			 
		 <el-row :gutter="20" >
						<el-table class="table" scrollbar-always-on  height="300px" :data="shipment.parcels">
							<el-table-column type="expand">
							<template #default="props">
							<el-table class="table" :data="props.row.declarations">
								<el-table-column prop="sku" label="商品SKU"  >
									<template #default="scope">
										     {{scope.row.sku}}
									</template>
								</el-table-column> 	 
								<el-table-column prop="name_zh" label="中文品名" >
									<template #default="scope">
										     {{scope.row.name_zh}}
									</template>
								</el-table-column> 	
								 <el-table-column prop="name_en" label="英文品名" >
								 	<template #default="scope">
								 		     {{scope.row.name_en}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="unit_value" label="申报单价" >
								 	<template #default="scope">
								 		     {{scope.row.unit_value}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="qty" label="数量" >
								 	<template #default="scope">
								 		     {{scope.row.qty}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="material" label="材质" >
								 	<template #default="scope">
								 		     {{scope.row.material}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="usage" label="用途"  >
								 	<template #default="scope">
								 		     {{scope.row.usage}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="brand" label="品牌" >
								 	<template #default="scope">
								 		     {{scope.row.brand}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="size" label="型号" >
								 	<template #default="scope">
								 		     {{scope.row.size}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="sale_url" label="销售链接" show-overflow-tooltip >
								 	<template #default="scope">
								 		     {{scope.row.sale_url}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="weight" label="产品重量"  >
								 	<template #default="scope">
								 		     {{scope.row.weight}}
								 	</template>
								 </el-table-column>
								 <el-table-column prop="photo_url" label="图片链接" show-overflow-tooltip>
								 	<template #default="scope">
								 		     <span v-for="photo,index in photo_url">
								 		     		{{photo}}
								 		     	</span>
								 	</template>
								 </el-table-column>
								 <el-table-column prop="hscode" label="海关编码" >
								 	<template #default="scope">
										 {{scope.row.hscode}}
								 	</template>
								 </el-table-column>
								</el-table>	
							</template>
							</el-table-column>
							 <el-table-column prop="number" label="货箱号" >
							  <template #default="scope">
							 	    {{scope.row.number}} <br/>{{scope.row.ext_number}} 
							    </template>
							 </el-table-column>
							 <el-table-column prop="client_weight" label="客户数据" >
								 <template #default="scope">
								 	    {{scope.row.client_weight}}<br/>
								 	    	{{scope.row.client_length}} * {{scope.row.client_weight}} *
								 	    	{{scope.row.client_height}}
								    </template>
								 </el-table-column>
								 <el-table-column prop="client_weight" label="承运快递" >
								 								 <template #default="scope">
								 								 	     <div>{{scope.row.carrier_code}}</div>
																		 <div>{{scope.row.tracking_number}}</div>
								 								    </template>
								  </el-table-column>
							 <el-table-column prop="chargeable_weight" label="拣货数据(实重/材重)"  >
								 <template #default="scope">
								 	     {{scope.row.chargeable_weight}}/{{scope.row.actual_weight}}(kg) <br/>
								 	     	{{scope.row.chargeable_length}} * {{scope.row.chargeable_weight}} *
								 	     	{{scope.row.chargeable_height}} (cm)
								    </template>
								 </el-table-column>
							 <el-table-column prop="picking_time" label="打包时间"  >
								 <template #default="scope">
								     {{dateFormat(scope.row.picking_time*1000)}}
							   </template>
						    </el-table-column>
						</el-table>
				 </el-row>
 </div>
	 


		<div class="row" v-if="systemType=='ZM'">
			<div class="col-md-12">
				<div class="box box-widget" style="height:500px;">
					<div class="box-title"><img :src="$require('@/assets/pages/shipment/transinfo/inventory_detail_icon03.png')"> 路由信息</div>
					<div id="transship2" style="margin-left:15px;margin-top:10px;">


						<p>运单号码:{{zmData.jobno}}</p>
						<div class="left-line"></div>
						<div style="padding-left:15px;max-height:400px;width:100%;overflow:auto;">
							<div v-for="traces,index in zmData.podInfoDTOList">
								<span class="sys-point"></span><span
									class="text-sm light-font">{{traces.scanTime }}</span>
								<p style="margin-left:8px;">{{traces.remark}}</p>
							</div>
						</div>


					</div>
				</div>
			</div>
		</div>
		</div>
		<template #footer  >
			<span class="dialog-footer">
				<el-button @click="dialogTransVisible=false">关闭</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script>
   import transportationApi from '@/api/erp/ship/transportationApi.js';
   import { ref } from 'vue'
   	import {dateFormat} from '@/utils/index.js';
	export default {
		name: "TransInfoDailog",
		components: {},
		setup(props, context) {
			let dialogTransVisible=ref(false);
			let zmData=ref({});
			let shipment=ref({});
			let loading=ref(false);
			let zhTrans=ref({});
			let systemType=ref("ZH");
			function loadZmApiDetail(data){
					if(data&&data.message&&data.message=="请求成功"){
						zmData.value=data;
					} 
			   }
		    function loadZhApiDetail(data,companyid,shipmentid){
						if(data&&data.status&&data.data.shipment){
							var sumqty=0;
							var sumprice=0.0;
							if(data
									 &&data.data
									 &&data.data.shipment
									 &&data.data.shipment.parcels){
										for(var i=0;i<data.data.shipment.parcels.length;i++){
											var parcels=data.data.shipment.parcels[i];
											for(var j=0;j<parcels.declarations.length;j++){
												var declarations=parcels.declarations[j];
												var qty=declarations.qty;
												var unit_value=declarations.unit_value;
												sumqty=parseInt(sumqty)+parseInt(qty);
												sumprice=parseFloat(sumprice)+(parseFloat(qty)*parseFloat(unit_value));
											}
										}
									 data.data.shipment.sumqty=sumqty;
									 data.data.shipment.sumprice=sumprice;
									 shipment.value=data.data.shipment;
									 shipment.value.sumprice=sumprice;
									}
									if(data&&data.detail){
									shipment.value=Object.assign(shipment.value, data.detail.data.shipment);	
									}
								 loading.value=false;
				   }
		      }
		function loadTransDetialInfo(companyid,shipmentid,ordernum){
								var html="";
								loading.value=true;
								shipment.value="";
								zmData.value="";
								transportationApi.shipTransDetial({"companyid": companyid,"shipmentid":shipmentid,"ordernum":ordernum}).then(res=>{
									loading.value=false;
									if(res && res.data.ftype=="ZH"){
										systemType.value=res.data.ftype;
										loadZhApiDetail(res.data,companyid,shipmentid);
									}
									if(res && res.data.ftype=="ZM"){
										systemType.value=res.data.ftype;
										loadZmApiDetail(res.data);
									}
								})
							    dialogTransVisible.value=true;
		}
	  return {dialogTransVisible,zmData,loading,
	         shipment,zhTrans,loadTransDetialInfo,systemType,dateFormat
			}
	    }
	}
</script>

<style>
	.transdailog .el-dialog__body{
		padding-bottom:5px;
	}
</style>
