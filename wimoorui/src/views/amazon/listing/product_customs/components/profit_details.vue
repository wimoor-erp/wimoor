<template>
	<el-dialog v-model="profitVisable"   top="4vh" class="profitModel" destroy-on-close='true' width="800px"  >
     <template #header>
	   	<h4 >利润计算详情</h4>
    </template>
		<el-row :gutter="16" >
			<el-col :span="10">
				<el-card :class="getCardBg()" class="m-c-16">
					<div class="bg-color-text">
					<div class="flex-center-between">
					<div>
					<div class="font-small">利润率</div>
					<div class="font-large font-bold">{{profitData.profit}} </div>
					</div>
					<finance class="ic-cen" theme="outline" size="32" fill="#ffffff"/>
					</div>
					</div>
				</el-card>
				<h4 class="m-b-8 m-t-32">参与计算的参数</h4>
				<el-space direction="vertical" alignment="left"  class="w-100">
					<div class=" flex-center-between">
						<span v-if="ftype=='has'">&nbsp;当前售价：</span>
						<span v-else>&nbsp;七日平均售价：</span>
						<span>{{profitData?.costDetailMap?.currency}}{{profitData.avgsales}}</span>
					</div>
					<div class=" flex-center-between">
						<span>&nbsp;当前计算方案：</span>
						<span>{{profitData.profitcfg}}
						<el-popover v-model:visible="visible" placement="top" :width="200"  title="切换利润计算方案" trigger="click">
						   <el-form>
							   <el-form-item label="计算方案">
								   <el-select :teleported="false" v-model="profitCfg" >
									   <el-option v-for="item in profitData.profitCfgList"   :key="item.id"  :label="item.name" :value="item.id" >
										   
									   </el-option>
								   </el-select>
							   </el-form-item>
						   </el-form>
						    <div >
						      <el-button size="small" text @click="visible = false">取消</el-button>
						      <el-button size="small" type="primary" @click="switchProfit"
						        >确认</el-button
						      >
						    </div>
						    <template #reference>
						     <el-button  link title="切换利润计算方案" type="primary">
						     <el-icon class="font-base"><Switch /></el-icon>
						     </el-button>
						    </template>
						  </el-popover>
						</span>
					</div>
					<div class=" flex-center-between">
						<span>&nbsp;长x宽x高：</span>
						<span> {{profitData.dimensionsInfo}}</span>
					</div>
					<div class=" flex-center-between">
						<span>&nbsp;重量：</span>
						<span>{{profitData.dimensionsWeight}}</span>
					</div>
					<div class=" flex-center-between" v-if="profitData?.costDetailMap?.productTier">
						<span>&nbsp;尺寸分段：</span>
						<span>{{profitData?.costDetailMap?.productTier}}</span>
					</div>
					
				</el-space>
			</el-col>
			<el-col :span="14">
				<el-table :data="tableData" size="small" v-loading="loading"  border   show-summary>
						  <el-table-column label="序号"  width="50" type="index" />
						  <el-table-column label="费用项">
							  <template #default = "scope">
							    <div>{{scope.row.name}}</div>
								<div class="font-extraSmall" v-html="scope.row.enname"></div>
							  </template>
						  </el-table-column>
						  <el-table-column label="金额" width="80" prop="cost" >
						  <template #default = "scope">
								<div><span v-if="scope.row.cost">{{profitData?.costDetailMap?.currency}}</span>
								{{scope.row.cost}}</div>
						  </template>
						  </el-table-column>
						  <el-table-column label="实际成本金额" width="100" prop="realcost" >
							   <template #default = "scope">
							  <div v-if="scope.row.title">
								  <el-tooltip
								         class="box-item"
								         effect="dark"
								         :content="scope.row.title"
								         placement="top"
								  		 raw-content
								       >
									<span><span v-if="scope.row.realcost">{{profitData?.costDetailMap?.currency}}</span>{{scope.row.realcost}}</span>
									</el-tooltip>
							  </div>
							  <div v-else>
								  <span><span v-if="scope.row.realcost">{{profitData?.costDetailMap?.currency}}</span>{{scope.row.realcost}}</span>
							  </div>
							</template>
						  </el-table-column>
						  
				</el-table>
			</el-col>
		</el-row>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs} from "vue";
	import {ElMessage} from 'element-plus';
	import {Finance} from '@icon-park/vue-next';
	import {Switch,} from '@element-plus/icons-vue';
	import calculateApi from '@/api/amazon/profit/calculateApi.js';
	import productinoptApi from '@/api/amazon/product/productinoptApi.js';
 
			const state=reactive({
				profitVisable:false,
				visible:false,
				loading:false,
				profitData:{costDetailMap:{currency:""}},
				tableData:[],
				pid:"",
				profitCfg:"",
				prices:null,
				ftype:'',
			})
			 const {
			   profitVisable,
			   profitData,
			   tableData,
			   pid,
			   profitCfg,
			   ftype,
			   visible,
			   loading,
			 } = toRefs(state);
			 
			function getCardBg(){
				if(state.profitData.profit<=0){
					return "redCard"
				}else{
					return "greenCard"
				}
			}
			function switchProfit(){
				var cfgid=state.profitCfg;
				var pid=state.pid;
				//ajax提交
				productinoptApi.updateOptProfitId({"profitid":cfgid,"pid":pid}).then((res)=>{
					if(res.data.isok=="true"){
						ElMessage.success('操作成功！');
						loadData(state.pid,state.prices,state.ftype);
					}else{
						ElMessage.error('操作失败！');
					}
				});
			}
			function loadData(pid,price,type){
				var data={};
				state.profitVisable=true;
				state.pid=pid;
				state.prices=price;
				state.ftype=type;
				data.pid=pid;
				data.price=price;
				data.productpricetype=type;
				state.loading=true;
				calculateApi.showProfitDetial(data).then((res)=>{
					state.profitData=res.data;
					state.loading=false;
					state.profitCfg=res.data.profitcfgid;
					if(state.profitData.costDetailMap){
						var maps=state.profitData;
						var datas=state.profitData.costDetailMap;
						var arrs=[];
						var shiptitle="";
						if(maps.reshipmentfeeRMB){
							shiptitle="最近90天内货件计算的分摊均价,对应金额RMB:"+maps.reshipmentfeeRMB;	
						}
						var row1={"name":"采购成本","enname":"Purchase cost","cost":datas.purchase,"realcost":maps.reprice,"title":maps.repricetime};
						var row2={"name":"运费","enname":"Shipment","cost":datas.shipment,"realcost":maps.reshipmentfee,title:shiptitle};
						var row3={"name":"平台佣金","enname":"Amazon Referral Fee","cost":datas.referralFee,"realcost":maps.order_Commission};
						var row4={"name":"变动结算费","enname":"Variable Closing Fee","cost":datas.vcfee,"realcost":null};
						var fbatitle="";
						if(state.ftype=='has'){
							fbatitle="<span title='通过下载亚马逊平台FBA Fee Preview Report报表获取'>(fba estimated fees平台报表)</span>";
						}else{
							fbatitle="<span title='通过下载亚马逊平台FBA Fee Preview Report报表获取'>(fba estimated fees平台报表)</span>";
						}
						var row5={"name":"FBA处理费","enname":"FBA"+fbatitle,"cost":datas.fba,"realcost":maps.order_FBAPerUnitFulfillmentFee};
						if(maps.orderFinDetailList && maps.orderFinDetailList.length>0){
							var str="<table class='table' style='width:300px;'><tr>"+
							"<td>订单:</td><td> "+maps.orderid+"</td></tr>";
							maps.orderFinDetailList.forEach(function(item){
								var name="";
								if(item.name){
									name=item.name+":";
								}else{
									name=item.ftype+":";
								}
								str+="<tr><td>"+name+"</td><td>"+datas.currency+item.amount+"</td></tr>";
							});
							str+="</table>";
							row5.title=str;
						}
						var row5_1={};
						if(datas.country=='IN'){
							row5_1={"name":"固定结算费","enname":"Fixed Closing Fee","cost":datas.closingFee,"realcost":null};
						}
						var row5_2={};
						if(datas.country=='IN' || datas.country=='CA'){
							row5_2={"name":"FBA GST/HST 税率","enname":"FBA GST/HST Taxes","cost":datas.fbaTaxFee,"realcost":null};
						}
						var row6={"name":"仓储费","enname":"Inventory Storage Fee","cost":datas.storageFee,"realcost":null};
						if(maps.fbastorage){
							row6.title=maps.fbastorage.month+"月度仓储费";
							row6.realcost=maps.fbastorage.monthlyStorageFee;
						}
						var row7={};
						if(datas.country=='US' || datas.country=='JP'){
							row7={"name":"库存配置费","enname":"Inventory Placement Service Fee","cost":datas.inPlaceFee,"realcost":null};
						}
						var row8={"name":"标签费","enname":"Label Service Fee","cost":datas.labelServiceFee,"realcost":null};
						var row9={};
						if(datas.country=='US'){
							row9={"name":"人工处理费","enname":"Manual Processing Fee","cost":datas.manualProcessingFee,"realcost":null};
						}
						var row10={"name":"进口关税","enname":"Import Duty Tax","cost":datas.tax,"realcost":null};
						
						var row10_1={};
						if(datas.country=='IN'){
							row10_1={"name":"进口GST税率","enname":"Import GST Tax","cost":datas.import_GST,"realcost":null};
						}
						var row11={"name":"汇率损耗","enname":"Currency Transport Fee","cost":datas.currencyTransportFee,"realcost":null};
						var row12={"name":"市场营销费用","enname":"Marketing","cost":datas.marketing,"realcost":null};
						var row13={"name":"其它每单销售固定费用","enname":"Others","cost":datas.others,"realcost":null};
						var row14={"name":"其它每单销售固定成本","enname":"Other rate Fee","cost":datas.othersFee,"realcost":null};
						var row15={"name":"按件收费，适用个人版","enname":"Per-Item Fee","cost":datas.perItemFee,"realcost":null};
						var row15_1={};
						if(datas.country=='UK' || datas.country=='DE' || datas.country=='IT' || datas.country=='ES'|| datas.country=='FR'|| datas.country=='NL'){
							row15_1={"name":"欧洲增值税","enname":"Vat Tax Fee","cost":datas.vatFee,"realcost":maps.order_vat};
						}
						var row15_2={};
						var row15_3={};
						if(datas.country=='IN'){
							row15_2={"name":"销售GST税率","enname":"Selling GST Tax","cost":datas.selling_GST,"realcost":null};
							row15_3={"name":"企业所得税率","enname":"Corporate Income Tax","cost":datas.corporateInFee,"realcost":null};
						}
						arrs.push(row1);arrs.push(row2);arrs.push(row3);
						arrs.push(row4);
						arrs.push(row5);
						if(row5_1 && row5_1.name){
							arrs.push(row5_1);
						}
						if(row5_2 && row5_2.name){
							arrs.push(row5_2);
						}
						arrs.push(row6);
						if(row7 && row7.name){
							arrs.push(row7);
						}
						arrs.push(row8);
						if(row9 && row9.name){
							arrs.push(row9);
						}
						arrs.push(row10);
						if(row10_1 && row10_1.name){
							arrs.push(row10_1);
						}
						arrs.push(row11);arrs.push(row12);
						arrs.push(row13);arrs.push(row14);arrs.push(row15);
						if(row15_1 && row15_1.name){
							arrs.push(row15_1);
						}
						if(row15_2 && row15_2.name){
							arrs.push(row15_2);
						}
						if(row15_3 && row15_3.name){
							arrs.push(row15_3);
						}
						state.tableData=arrs;
					}
					
				});
			}
		 defineExpose({
		   loadData,
		 })
		 
	 
</script>

<style>
.bg-color-text{
	color: #fff;
}
.redCard{
	background-color:#f56c6c;
	box-shadow:0 2px 10px 0 rgb(255 30 30 / 20%)!important;
	border:none;
}
.greenCard{
	background-color:#67c23a;
	box-shadow:0 2px 10px 0 rgb(61 137 30 / 20%)!important;
	border:none;
}
.m-c-16 .el-card__body{
	padding:16px 24px ;
}
.m-t-32{margin-top:32px;}
.m-b-8{margin-bottom: 16px;}
.w-100{width:100%}
.profitModel .el-table .cell{
	line-height: 18px !important;
}
.profitModel  .el-dialog__body{
	padding-top:10px;
}
</style>

