<template>
	<div class="shipment-info-wrap">
			<el-row class="ship-sty" :gutter="16">
				<el-col :span="8">
					<el-card shadow="never">
						<div class="ship-title ">
							<h4>单据信息</h4>
						</div>
						<el-space direction="vertical" size="large"  alignment="left"	>
							<div class='item-list-from'>
								<div class="label">订单编码</div>
								<div class="value">
									 {{plan.number}} 
								</div>
							</div>
						 <div class='item-list-from'>
							<div class="label">	货件名称</div>
							<div class="value">
							    {{shipmentInfo.name}} 
							</div>
						 </div>
						 <div class='item-list-from'>
							<div class="label">	货件编码</div>
							<div class="value">
								<span>{{shipment.shipmentConfirmationId}}</span>
								<span class="font-extraSmall"> (Referenceid:{{shipmentInfo.referenceid}})</span>
							</div>
						 </div>
						<div class='item-list-from'>
							<div class="label">货件状态</div>
							<div class="value">
								 <el-tag  >{{shipmentInfo.shipmentstatus}}</el-tag>
							</div>
						</div>
						<div class='item-list-from'>
							<div class="label">店铺</div>
							<div class="value">{{shipmentInfo.groupname}}</div>
						</div>
						<div class='item-list-from'>
							<div class="label">收货站点</div>
							<div class="value">{{shipmentInfo.country}}</div>
						</div>
						<div class='item-list-from'>
							<div class="label">发货仓库</div>
							<div class="value">{{shipmentInfo.warehouse}}</div>
						</div>
						<div class='item-list-from'>
							<div class="label">备注</div>
							<div class="value">{{shipmentInfo.remark}}</div>
						</div>
						</el-space>
					</el-card>
				</el-col>
				<el-col :span="8">
					<el-card shadow="never">
						<div class="ship-title ">
							<h4 >运输信息</h4>
						</div>
						<el-row >
							<el-col :span="8">
									<div class="local-title">总货值</div>
									<div class="local-num text-orange">{{totalprice}}</div>
							</el-col>		
							<el-col :span="8">
								<div class="local-title">物流总费用</div>
								<div class="local-num text-orange">{{totalfee}}</div>
							</el-col>
							<el-col :span="8">
								<div class="local-title">实际发货总数</div>
								<div class="local-num " >{{shipmentInfo.sumQuantity||0}}</div>
							</el-col>
						</el-row>
            <el-row>
              <el-col :span="12">
                <el-space direction="vertical" :size="12"  alignment="left"	>
                  <div class="ship-dea-data ship-dea-data-first">
                    <div class="ship-text">SKU个数</div>
                    <div class="ship-num-text">{{shipmentInfo.skuamount}}</div>
                  </div>
                  <div class="ship-dea-data">
                    <div class="ship-text">实际结算重量</div>
                    <div class="ship-num-text" v-if="weightvalue">{{weightvalue}}</div>
                    <div class="ship-num-text" v-else>无</div>
                  </div>
                  <div class="ship-dea-data">
                    <div class="ship-text">产品净重（kg)</div>
                    <div class="ship-num-text" v-if="readyweightvalue">{{readyweightvalue}}</div>
                    <div class="ship-num-text" v-else>无</div>
                  </div>
                  <div class="ship-dea-data">
                    <div class="ship-text">箱子重（kg)</div>
                    <div class="ship-num-text" v-if="boxweightvalue">{{boxweightvalue}}</div>
                    <div class="ship-num-text" v-else>无</div>
                  </div>
                  <div class="ship-dea-data">
                    <div class="ship-text">体积(立方)</div>
                    <div class="ship-num-text" v-if="volume">{{volume}}</div>
                    <div class="ship-num-text" v-else>无</div>
                  </div>

                </el-space>
              </el-col>
              <el-col :span="12">
                <el-space direction="vertical" :size="12"  alignment="left"	>
                  <div class="ship-dea-data">
                    <div class="ship-text">亚马逊运输方式</div>
                    <div class="ship-num-text" v-if="shiptype">{{shiptype}}</div>
                    <div class="ship-num-text" v-else>无</div>
                  </div>
                  <div class="ship-dea-data">
                    <div class="ship-text">装箱箱数</div>
                    <div class="ship-num-text" >{{boxnum||"-"}}</div>
                  </div>
                </el-space>
              </el-col>
            </el-row>

					</el-card>
				</el-col>
				<el-col :span="8">
					<el-card shadow="never">
						<div class="ship-title flex-between">
							<h4 >地址信息</h4>
							<el-button size="small" @click="handleShowDest" link type="info">添加箱标地址</el-button>
						</div>
						<el-row >
							<el-col :span="24">
								<el-timeline style="max-width: 600px">
								    <el-timeline-item timestamp="发货地址" placement="top">
								      <el-card   body-style="padding:12px;" style="border:none">
										    <h4>{{fromAddress.name}}</h4>
								            <div>{{fromAddress.addressline1}}<span v-if="fromAddress.addressline2">,{{fromAddress.addressline2}}</span></div> 
								            <div>{{fromAddress.city}}<span v-if="fromAddress.stateorprovincecode">,{{fromAddress.stateorprovincecode}}</span></div>
								            <div>{{fromAddress.postalcode}},{{fromAddress.countrycode}}</div>
								      </el-card>
								    </el-timeline-item>
								    <el-timeline-item timestamp="收货地址" placement="top">
								      <el-card   body-style="padding:12px" style="border:none">
								        <h4>{{toAddress.name}}</h4>
								        <p>{{toAddress.addressLine1}}<span v-if="toAddress.addressLine2">,{{toAddress.addressLine2}}</span></p>
										<p>{{toAddress.city}}<span v-if="toAddress.stateOrProvinceCode">,{{toAddress.stateOrProvinceCode}}</span></p>
									    <p>{{toAddress.postalCode}},{{toAddress.countryCode}}</p>
								      </el-card>
								    </el-timeline-item>
									<el-timeline-item type="primary" v-if="toAddressBox&&toAddressBox.code" timestamp="箱标收货地址" placement="top">
									  <el-card   body-style="padding:12px" style="border:none">
									    <div class="flex-between">
											<h4>{{toAddressBox.name}}</h4>
											<el-icon class="font-extraSmall pointer" @click="handleRemoveAddressBox()"><Close></Close></el-icon>
										</div>
									    <p>{{toAddressBox.addressLine1}}<span v-if="toAddressBox.addressLine2">,{{toAddressBox.addressLine2}}</span></p>
										<p>{{toAddressBox.city}}<span v-if="toAddressBox.stateOrProvinceCode">,{{toAddressBox.stateOrProvinceCode}}</span></p>
									    <p>{{toAddressBox.postalCode}},{{toAddressBox.countryCode}}</p>
									  </el-card>
									</el-timeline-item>
								  </el-timeline>
								  
								  
							</el-col>
						</el-row>
					</el-card>
				</el-col>
			</el-row>
	</div>
	<DestinationDailog ref="destinationDailogRef" @change="getBaseInfo(shipment.shipmentid)"></DestinationDailog>
</template>

<script setup>
	import {TakeOff,TransactionOrder,Local} from '@icon-park/vue-next';
	import { ref,reactive,onMounted, toRefs } from 'vue';
	import {List,Promotion,LocationFilled,Close} from '@element-plus/icons-vue'
	import {dateFormat,formatFloat} from '@/utils/index.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import {tranStatus,tranStatusType} from "@/hooks/erp/shipment/shipment_status.js"
	import DestinationDailog from "./destination.vue";
	import { ElMessage, ElMessageBox } from 'element-plus';
		let shipmentInfo=ref({})
		let readyweightvalue=ref("")
		let weightvalue=ref("")
    let boxweightvalue=ref("")
		let volume=ref("")
		let shiptype=ref("")
		let boxnum=ref("")
		let totalprice=ref("")
		let totalfee=ref("")
		let fromAddress=ref({})
		let toAddress=ref({})
		let toAddressBox=ref({})
		let shipment=ref({})
		let plan=ref({});
		const destinationDailogRef=ref();
		const emit = defineEmits(['change']);
        function handleShowDest(){
			destinationDailogRef.value.show(shipment.value.shipmentid,toAddress.value.countryCode);
		}
		function handleRemoveAddressBox(){
			shipmentPlacementApi.saveDestinationBox({"shipmentid":shipment.value.shipmentid,"destinationBox":"NA"}).then(res=>{
				 ElMessage.success("操作成功");
				 toAddressBox.value={};
				 getBaseInfo(shipment.value.shipmentid);
			})
			
		}
		function noDatatran(val){
			if(val==null||val==undefined){
				return "--"
			}else{
				return val
			}
		}
	 
		function getValue(value){
			if(value==null||value==""||typeof(value)=="undefined"||(typeof(value) === "number" && isNaN(value))){
				return "";
			}
			else {
			  return  value;
			}
		}
		
		function getAddressValue(value){
			if(value==null||value==""||typeof(value)=="undefined"||(typeof(value) === "number" && isNaN(value))){
				return "";
			}
			else {
			  return ","+value;
			}
		}
		
		function getBaseInfo(shipmentid){
			shipmentPlacementApi.getBaseInfo({
				"shipmentid":shipmentid
			}).then(res=>{
				if(res.data){
					var data=res.data;
					var obj={};
					var obj1={};
					shipmentInfo.value=data.shipmentAll;
					shipment.value=data.shipment;
					plan.value=data.plan;
					volume.value=data.totalBoxSize;
					shiptype.value=data.shipment.transtyle;
					if(data.listbox && data.listbox.length>0){
						boxnum.value=data.listbox.length;
					}else{
						boxnum.value=data.shipment.boxnum;
					}
					if(data.detail){
				    	totalprice.value="￥"+getValue(parseFloat(data.detail.itemprice));
					}
					if(data["fromAddress"]){
						fromAddress.value=data["fromAddress"];
					}
					if(data["toAddress"]){
						toAddress.value=data["toAddress"];
					}
					if(data["toAddressBox"]){
						toAddressBox.value=data["toAddressBox"];
					}
					if(data.transinfo==null || data.transinfo==undefined){
						totalfee.value="0";
					}else{
						if(!data.transinfo.otherfee)data.transinfo.otherfee=0;
						if(!data.transinfo.transweight)data.transinfo.transweight =0;
						if(!data.transinfo.singleprice)data.transinfo.singleprice=0;
						totalfee.value=( "￥"+  formatFloat(parseFloat(parseFloat(data.transinfo.transweight) *parseFloat(data.transinfo.singleprice))+parseFloat(data.transinfo.otherfee))); 
					}
          boxweightvalue.value=data.totalweight;
					if(data.transinfo && data.transinfo.transweight){
						if(data.transinfo.wunit){
							weightvalue.value=(data.transinfo.transweight+""+getValue(data.transinfo.wunit));
						}else{
							if(data.transchannel.priceunits=="weight"){
								weightvalue.value=(data.transinfo.transweight+"kg");
							}else{
								weightvalue.value=(data.transinfo.transweight+"cbm");
							}
						}
					}else{
					    if(data.detail){
						   readyweightvalue.value=(getValue(data.detail.readweight));
						}
					}
					emit("change",data);
				}
			})
		}
  defineExpose({ getBaseInfo })
			 
</script>

<style scoped>
	.local-title{
		margin-bottom: 8px;
	}
	.local-num{
		font-size: 18px;
		font-weight: 500;
	}
	.text-orange{
		color:#ff7315;
	}
	.shipment-info-wrap{
		margin:16px;
		padding-bottom: 16px;
	}
	.ship-sty .item-list-from,.ship-sty .el-timeline{
		font-size: 14px;
	}
	.ship-text{
		font-size:14px;
		color:#999;
		width:140px;
		white-space: nowrap;
	}

	.ship-num-text{
		font-size: 14px;
		font-weight: 500;
		font-family: "Helvetica Neue";
		width: 160px;
	}
	.ad-weight{
		font-weight: 600;
	}
	.text-gray{
		color:#999;
	}
	.ship-dea-data{
		margin-top: 12px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.ship-dea-data-first{
		margin-top:24px;
	}
	.ship-title{
		display: flex;
	 align-items: center;
	 margin-bottom:16px ;
	 font-size: 14px;
	}
	
</style>
