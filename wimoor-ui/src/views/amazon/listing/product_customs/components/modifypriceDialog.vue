<template>
	<el-dialog v-model="modifyPriceVisable"  title="产品调价" @opened="loadPriceHis" destroy-on-close='true' width="80%"  >
		<div class="flex-center dialog-pro">
			<el-image :src="localData.image"></el-image>
			<div>
				<div class="name">{{localData.name}}</div>
				<div class="sku">{{localData.sku}}</div>
				<div class="name">
					备注:
					<span v-if="localData.priceremark && localData.priceremark!=''">
						{{localData.priceremark}}
					</span>
					<span v-else>无</span>
				</div>
				<el-space>
				<div style="width:200px;"></div>
				<el-popover  v-model:visible="remarkvisible" placement="top" :width="250" trigger="click">
					 <el-space direction="vertical">
				       <el-input v-model="localData.priceremark" placeholder="请输入改价备注"></el-input>
				      <el-button size="small" type="primary" @click="submitRemark" >提交</el-button  >
				       </el-space>
				    
				    <template #reference>
				  <el-button  text bg>
				  	<el-icon class="font-base">
				  		 <edit></edit>								
				  	</el-icon>
				  	 调价公告
				  </el-button>
				    </template>
				  </el-popover>
				
				 <el-popover  v-model:visible="visible" placement="top" :width="250" trigger="click">
					 <el-space direction="vertical">
				<span style="font-size: 12px;">注：提交冻结调价后，此SKU在设定的时间内调价功能暂时无法使用。</span>
				 <div v-if="localData.haslocked==false">
				 <el-date-picker
					v-model="lockedDate"
					type="date"
					placeholder="选择日期"
					:size="size"
					:teleported="false"
				  />
				      <el-button size="small" type="primary" style="margin-top:10px" @click="submitTime" >提交</el-button  >
					  </div>
					  <div v-else>
						  <el-button size="small" type="primary" @click="cancelTime" >确认取消</el-button  >
					  </div>
				       </el-space>
				    
				    <template #reference>
				   <el-button  text bg>
				   	<el-icon class="font-base"><Lock /></el-icon>
				   	<span v-if="localData.haslocked==false">冻结调价</span> 
				   	<span v-else >取消冻结</span> 
				   </el-button>
				    </template>
				  </el-popover>
				</el-space>
			</div>
		</div>
		<el-tabs style="margin-top:-15px;" v-model="activeType" class="demo-tabs"  >
			 <el-tab-pane label="调价-立即处理" name="1">
				 <el-row>
				 <el-col :span="10">
				 <el-row>
					  <el-form-item label="调价类型:">
						  <el-radio-group v-model="pricetimetype" class="ml-4">
						        <el-radio label="1"  >标准价格</el-radio>
						        <el-radio label="2"  >限时价格</el-radio>
						  </el-radio-group>
						 </el-form-item>  
				 </el-row>
				 <el-row>
				 	<el-form :model="form2" label-width="120px"  label-position="top">
				 		 <el-form-item label="产品售价:">
				 		      <el-input   @input="localData.changeprice=CheckInputFloat(localData.changeprice)" v-model="localData.changeprice" >
				 				  <template #prepend>
				 					  {{localData.landedCurrency}}
				 				  </template>
				 			  </el-input>
				 			  <span v-if="localData.haslocked==true">
				 			 冻结期限至：{{dateFormat(localData.lockedtime)}}
				 			 </span>
				 		    </el-form-item>
				 	 </el-form>
				 </el-row>
				  <el-row v-if="pricetimetype=='2'">
					  <el-form :model="form2" label-width="120px"  label-position="top">
					 <el-form-item label="限时日期">
					      <el-date-picker
					             v-model="justdiscountDate"
					             type="daterange"
					             range-separator="-"
					             start-placeholder="开始日期"
					             end-placeholder="结束日期"
					             :size="size"
					           />
					    </el-form-item>
						 </el-form>
				  </el-row>
				  <div class="dialog-footer">
				  	<el-button @click="modifyPriceVisable = false">取消</el-button>
				  	<el-button @click="modifyPriceQueue" v-if="activeType=='2'">队列调价详情</el-button>
				  	<el-button type="primary" @click="submitJustPrice" :loading="priceloading" >提交</el-button>
					<div style="padding-top: 20px;" class="font-extraSmall">提交成功后，亚马逊将会在10分钟左右完成本次调价！</div>
				  </div>
				   </el-col>
				 
				 <el-col :span="14" style="padding-left:60px;">
				 		<el-table :data="justPriceList" style="width: 100%;" height="350">
				 		    <el-table-column prop="ftype" label="改价类型"  width="130">
								<template #default="scope">
									<span v-if="scope.row.startdate">
									 限时价格
									<div>{{dateFormat(scope.row.startdate)}} 
									   <span class="font-extraSmall">至</span> 
									</div> 
									 {{dateFormat(scope.row.enddate)}}
									</span>
									<span v-else>
										永久调价
									</span>
								</template>
							</el-table-column>
				 		    <el-table-column prop="price" label="调整后价格"   />
				 		    <el-table-column prop="oldprice" label="调整前价格" />
							<el-table-column prop="opttime" label="操作时间" width="160" >
								<template #default="scope">
										 {{dateTimesFormat(scope.row.opttime)}}
									</template>
							 </el-table-column>
							 <el-table-column prop="operator" label="操作人" />
							 <el-table-column prop="remark" label="备注" />
				 		  </el-table>	  
				 </el-col>
				  </el-row>
				 
			 </el-tab-pane>
			 <el-tab-pane label="调价-队列处理" name="2">
				 <el-row>
				 <el-col :span="10">
					 <el-form-item label="调价类型:">
					 						  <el-radio-group v-model="activeName" class="ml-4">
					 						        <el-radio label="isForever"  >售价</el-radio>
					 						        <el-radio label="isFortime"  >限时售价</el-radio>
													<el-radio label="isBusiness"  >企业售价</el-radio>
					 						  </el-radio-group>
											  <span style="padding-left:20px;">
												  <feed-status ref="feedStatus" :feedid="feedid" filename="priceFeedFile.xlsx"></feed-status>
											  </span>
											   
					 						 </el-form-item>  
				 <div   class="demo-tabs" >
				     <div v-if="activeName=='isForever'" label="售价" name="isForever">
				 		<el-row>
				 			<el-col :span="12">
				 		<el-form :model="form" label-width="120px"  label-position="top">
				 			 <el-form-item label="产品售价">
				 			      <el-input type="number"  v-model="localData.changeprice" >
				 					  <template #prepend>
				 						  {{localData.landedCurrency}}
				 					  </template>
				 				  </el-input>
				 				  <span v-if="localData.haslocked==true">
				 				 冻结期限至：{{dateFormat(localData.lockedtime)}}
				 				 </span>
				 			    </el-form-item>
				 		 </el-form>
				 		 </el-col>
				 		 </el-row>
				 	</div>
				     <div  v-if="activeName=='isFortime'" label="限时售价" name="isFortime">
				 		<el-row>
				 			<el-col :span="12">
				 		<el-form :model="form" label-width="120px"  label-position="top">
				 			 <el-form-item label="优惠价">
				 			      <el-input v-model="localData.changeprice" >
				 					  <template #prepend>
				 						   {{localData.landedCurrency}}
				 					  </template>
				 				  </el-input>
				 			    </el-form-item>
				 			 <el-form-item label="优惠日期">
				 			      <el-date-picker
				 			             v-model="discountDate"
				 			             type="daterange"
				 			             range-separator="-"
				 			             start-placeholder="开始日期"
				 			             end-placeholder="结束日期"
				 			             :size="size"
				 			           />
				 			    </el-form-item>
				 		 </el-form>
				 		 </el-col>
				 		 </el-row>
				 	</div>
				     <div  v-if="activeName=='isBusiness'" label="企业售价" name="isBusiness">
				 		<el-row>
				 			<el-col :span="24">
				 		<el-form :model="form" label-width="120px"  label-position="top">
				 			 <el-col :span="12">
				 			 <el-form-item label="企业售价">
				 			      <el-input v-model="localData.changeprice" >
				 					  <template #prepend>
				 						   {{localData.landedCurrency}}
				 					  </template>
				 				  </el-input>
				 			    </el-form-item>
				 				<el-form-item label="折扣类型">
				 					 <el-radio-group v-model="bussinesstype" class="ml-4">
				 					      <el-radio label="percent" size="large">比例</el-radio>
				 					      <el-radio label="fixed" size="large">固定价格</el-radio>
				 					    </el-radio-group>
				 				</el-form-item>
				 				 </el-col>
				 				 <el-col :span="24">
				 				<el-form-item label="折扣类型">
				 					<el-table :data="bussinessPrice">
				 						<el-table-column  label="销售数量"> 
				 						  <template #default="scope">
				 							   <el-input @input="changeBusPrice(scope.row)" v-model="scope.row.amount"></el-input>
				 						  </template>
				 						</el-table-column>
				 						<el-table-column :label="bussinesstype=='percent'?'优惠折扣':'售价'"> 
				 						<template #default="scope">
				 							<el-input @input="changeBusPrice(scope.row)" v-model="scope.row.discount">
				 								<template #append>
				 										<span v-if="bussinesstype=='percent'">%</span>
				 										<span v-else>{{localData.landedCurrency}}</span>
				 								</template>
				 							</el-input>
				 						</template>
				 						</el-table-column>
				 						<el-table-column v-if="bussinesstype=='percent'" label="售价">
				 							<template #default="scope">
				 								{{scope.row.price}}
				 							</template>
				 						</el-table-column>
				 						<el-table-column label="操作">
				 							<template #default="scope">
				 							  <el-button type="primary" @click="deleteBusPrice(scope.$index)" link>删除</el-button>
				 							  </template>
				 						</el-table-column>
				 					</el-table>
				 					 <el-button @click="addBussRow"  class="add-table-row">
				 						 <el-icon>
				 							 <plus></plus>
				 						 </el-icon>
				 						 添加</el-button>
				 				</el-form-item>
				 				 </el-col>
				 		 </el-form>
				 		  </el-col>
				 		 </el-row>
						
				 	</div>
					<div class="dialog-footer">
						<el-button @click="modifyPriceVisable = false">取消</el-button>
						<el-button @click="modifyPriceQueue" v-if="activeType=='2'">队列调价详情</el-button>
						<el-button type="primary" @click="submitPrice" :loading="priceloading" >提交</el-button>
					</div>
				   </div>
				   </el-col>
				   <el-col :span="14" style="padding-left:60px;">
				  
				 	    <PriceTable ref="priceTableRef" :isDailog="true"></PriceTable>
				 	 
				   </el-col>
				 </el-row>
			 </el-tab-pane>
		</el-tabs>
		
	  
	</el-dialog>
</template>

<script>
	import {ref,reactive,onMounted,watch} from "vue"
	import {Lock} from '@element-plus/icons-vue';
	import {Plus,Edit} from '@icon-park/vue-next';
	import {useRouter} from 'vue-router'
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import listingApi from '@/api/amazon/listing/listingApi.js';
	import {dateFormat,dateTimesFormat,formatFloat,CheckInputFloat} from '@/utils/index.js';
	import {ElMessage} from 'element-plus';
	import FeedStatus from "@/components/feedstatus/index.vue"
	import PriceTable from "@/views/amazon/listing/price_adjust/components/table.vue"
	export default{
		components:{
			Plus,Edit,Lock,FeedStatus,PriceTable
		},
		emits:["change"],
		setup(props,context){
			let radio1 =ref("1")
			let activeName = ref('isForever')
			let pricetimetype = ref('1')
			let activeType=ref("1")
			let router = useRouter()
            let form = reactive({
				price:18.00,
			})
			let priceTableRef =ref(PriceTable);
			let bussinessPrice = reactive([
				{
					amount:0,
					discount:0,
					price:0,
				},
			])
			let feedStatus=ref(FeedStatus);
			let feedid=ref("");
			let modifyPriceVisable =ref(false)
			let localData=ref({});
			let discountDate=ref();
			let justdiscountDate=ref();
			let lockedDate=ref();
			let hasLocked=ref(false);
			let bussinesstype=ref("percent");
			let pricetype=ref("isForever");
			let visible=ref(false);
			let priceloading=ref(false);
			let remarkvisible=ref(false);
			let justPriceList=ref([]);
			function modifyPriceQueue(){
				router.push({
					path:'/amazon/sale/priceadjust',
					query:{
					  title:"调价队列",
					  path:'/amazon/sale/priceadjust',
					  details:{},
					}
				}) 
			}
			function submitPrice(){
				if(localData.value.haslocked==false){
					var param={};
					param.pid=localData.value.id;
					param.currency=localData.value.listingCurrency;
					param.changeprice=localData.value.changeprice;
					if(activeName.value=="isForever"){
						param.oldprice=localData.value.changeprice;
					}else{
						param.oldprice=localData.value.landedAmount;
					}
					param.ftype=activeName.value;
					param.submittype='save';
					//企业调价
					param.businessprice=localData.value.changeprice;
					param.businesstype=bussinesstype.value;
					param.standprice=localData.value.landedAmount;
					var list=[];
					bussinessPrice.forEach(function(item){
						if(item.amount>0 && item.discount>0){
							var row={};
							row.quantity=item.amount;
							row.price=item.discount;
							list.push(JSON.stringify(row));
						}
					});
					if(list && list.length>0){
						param.businesslist=list.toString();
					}else{
						param.businesslist=null;
					}
					//限时调价
					if(discountDate.value && discountDate.value.length>0){
						param.startTime=dateFormat(new Date(discountDate.value[0]));
						param.endTime=dateFormat(new Date(discountDate.value[1]));
					}else{
						param.startTime=null;
						param.endTime=null;
					}
					priceloading.value=true;
					productinfoApi.changeProPrice(param).then((res)=>{
					   priceloading.value=false;
					   ElMessage.success(res.data.msg)
					   feedid.value=res.data.submissionid;
					   feedStatus.value.submitfeedInfo(res.data.submissionid);
					   modifyPriceVisable = false;
					   loadPriceHis();
					   context.emit("change");
					}).catch(e=>{
						priceloading.value=false;
					});
				}else{
					ElMessage.error( '当前存在调价冻结！')
				}
			}
			function submitTime(){
				if(new Date(lockedDate.value)>new Date()==true){
					var times=dateFormat(new Date(lockedDate.value));
					productinfoApi.ProductPriceLocked({"pid":localData.value.id,"price":localData.value.landedAmount,"days":times}).then((res)=>{
						if(res.data && res.data.isok=="true"){
							ElMessage.success('操作成功！')
							visible.value = false;
							localData.value.haslocked=true;
							localData.value.lockedtime=new Date(lockedDate.value);
						}else{
							ElMessage.error("操作失败！")
						}
					});
				}else{
					ElMessage.error( '请选择未来的日期！')
				}
			}
			function addBussRow(){
				var row={};
				row.amount=0;
				row.discount=0;
				row.price=0;
				bussinessPrice.push(row);
			}
			function changeBusPrice(row){
				if(bussinesstype.value=="fixed"){
					row.price=row.discount;
				}else{
					row.price=formatFloat(localData.value.changeprice-formatFloat((row.discount/100)*(localData.value.changeprice)));
				}
			}
			function submitRemark(){
				productinfoApi.updateRemark({"id":localData.value.id,"remark":localData.value.priceremark,"ftype":"price"}).then((res)=>{
					if(res.data && res.data.message=="success"){
						ElMessage.success('操作成功！')
						remarkvisible.value = false;
					}else{
						ElMessage.error( res.data.msg)
					}
				});
				
			}
			function cancelTime(){
				productinfoApi.cancelProductPriceLocked({"pid":localData.value.id}).then((res)=>{
					if(res.data && res.data.isok=="true"){
						ElMessage.success('操作成功！')
						visible.value = false;
						localData.value.haslocked=false;
					}else{
						ElMessage.error( res.data.msg)
					}
				});
				
			}
			function deleteBusPrice(index){
				if(bussinessPrice.length==1){
					ElMessage.error( "请至少保留一行！")	;
					return;
				}
				 bussinessPrice.splice(index,1);
			}
			function loadPriceHis(){
				                 activeType.value="1";
				               justPriceList.value=[];
								setTimeout(function(){
									loadPrice();
									loadJustPriceList();
								},100)
						}
			function loadPrice(){
                var queryParam={};
				queryParam.groupid=localData.value.groupid;
				queryParam.marketplaceid=localData.value.marketplaceid;
				queryParam.sku=localData.value.sku;
				const end = new Date()
				const start = new Date()
				start.setTime(start.getTime() - 3600 * 1000 * 24 *720)
				queryParam.startDate=start.format("yyyy-MM-dd");
				queryParam.endDate=end.format("yyyy-MM-dd")+" 23:59:59";
				 priceTableRef.value.loadData(queryParam);
			}
			 
			function submitJustPrice(){
				if(localData.value.haslocked==false){
					//立即调价
					var data={};
					if(pricetimetype.value=="2"){
						if(justdiscountDate.value && justdiscountDate.value.length>0){
							data.fromDate=dateFormat(new Date(justdiscountDate.value[0]));
							data.toDate=dateFormat(new Date(justdiscountDate.value[1]));
						}
					}else{
						data.fromDate=null; 
						data.toDate=null; 
					}
					data.pid=localData.value.id;
					data.price=localData.value.changeprice;
					data.ftype=pricetimetype.value;
					priceloading.value=true;
					listingApi.changePrice(data).then((res)=>{
						priceloading.value=false;
						if(res.data && res.data.status&& res.data.status=="ACCEPTED"){
							ElMessage.success('操作成功！')
							loadJustPriceList();
							context.emit("change",data);
						}
						else  {
							if(res.data.issues&&res.data.issues.length>0){
								ElMessage.error('操作失败！请尝试使用队列处理提交。'+res.data.issues[0].message)
							}else{
								ElMessage.error( '操作失败！')
							}
							
						}
						
					}).catch(e=>{
						priceloading.value=false;
					});
				}else{
					ElMessage.error( '当前存在调价冻结！')
				}
			}
			function loadJustPriceList(){
				listingApi.getPriceRecord({"pid":localData.value.id,"byday":""}).then((res)=>{
					justPriceList.value=res.data;
				});
			}
			
			return{
				modifyPriceVisable,activeName,form,radio1,bussinessPrice,visible,localData,discountDate,lockedDate,hasLocked,
				bussinesstype,pricetype,remarkvisible,feedid,activeType,pricetimetype,justdiscountDate,justPriceList,loadPriceHis,
				//value
				modifyPriceQueue,submitPrice,submitTime,dateFormat,addBussRow,changeBusPrice,submitRemark,cancelTime,
				deleteBusPrice,submitJustPrice,loadJustPriceList,dateTimesFormat,priceloading,CheckInputFloat,
				//function
				feedStatus,priceTableRef,
				//ref
				
			}
		}	
	}
</script>

<style>
	.dialog-footer{
	
		padding-top:20px;
	}
	.add-table-row{
		width: 100%;
		margin-top: 8px;
	}
	.dialog-pro{
		margin-bottom:16px;
		align-items: flex-start!important;
	}
	.dialog-pro .el-image{
		overflow: visible;
		margin-right:16px;
	}
	.dialog-pro .el-image img{
		width:60px;
		height:60px;
	}
	.dialog-pro .name{
		font-size: 12px;
		margin-bottom:8px;
	}
	.dialog-pro .sku{
		color: #409eff;
		font-size: 12px;
		margin-bottom:8px;
	}
	
</style>
