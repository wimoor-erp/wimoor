<template>
	<div class="quot-wrap-main" v-if="islogin">
	<div class="quot-wrap">
		<div class="quot-top-banner">
			<div class="banner-header">
				<div class="title">{{pageTitle}} - 货件询价系统 <div style="float:right; ">
							  <el-button-group class="ml-4">
							    <el-button type="primary" size="small" round 
								:plain="queryParam.displayType=='number'?true:false"   
								@click="queryParam.displayType='center';handleQuery()"  >地址</el-button>
							    <el-button type="primary" size="small" round 
								:plain="queryParam.displayType=='center'?true:false"   
								@click="queryParam.displayType='number';handleQuery()">批次</el-button>
							  </el-button-group>
						</div></div>
				<div class="subtext">
					 <el-dropdown >
					 
							   <!-- <el-button color="" type="primary" plain size="large"  round >
							        {{getTitle()}}<el-icon class="el-icon--right"><arrow-down /></el-icon>
							      </el-button> -->
								  <div class="num" style="margin-top:1px" >
									  <div style="padding-top:2px;width:170px;padding-bottom:2px">
									   {{getTitle()}}<el-icon class="el-icon--right"><arrow-down /></el-icon>
									   </div>
								  </div>
					       <!-- <div style="width:140px;text-align:center;padding-top:12px;padding-bottom:8px">
					      <el-icon class="el-icon--right">
					        <arrow-down />
					      </el-icon> 
					    </div>-->
				 
					    <template #dropdown>
					      <el-dropdown-menu>
					        <el-dropdown-item @click="selectType=2;handleQuery()">待报价</el-dropdown-item>
					        <el-dropdown-item @click="selectType=3;handleQuery()">已报价-未中标</el-dropdown-item>
					        <el-dropdown-item @click="selectType=4;handleQuery()">已报价-等待开标</el-dropdown-item>
					        <el-dropdown-item @click="selectType=5;handleQuery()">已报价-已中标</el-dropdown-item>
					      </el-dropdown-menu>
					    </template>
					  </el-dropdown>
					<!-- <el-select  style="width: 130px;margin-right: 8px;" placeholder="选择类型">
						<el-option   label="待报价" value="2"></el-option>
						<el-option   label="已报价-未中标" value="3"></el-option>
						<el-option   label="已报价-等待开标" value="4"></el-option>
						<el-option   label="已报价-已中标" value="5"></el-option>
					</el-select> -->
					<div class="num">
				{{tableData.total}}
				</div>个订单正在询价中</div>
				</div>
		</div>
		<div   >
		<div class="quot-shipment-box "  >
			<div v-for="order in tableData.records" :key="order.id">
			<div class="pag-radius-bor">
				<div class="order-header flex-center-between font-base">
					<el-space :size="24">
						<span><span class="light-font">订单号：</span>{{order.number}}</span>
						<span><span class="light-font">创建时间：</span>{{dateFormat(order.createtime)}}</span>
						<span><span class="light-font">预计结束时间：</span> 
						{{dateTimesFormat(order.closetime,"yyyy-MM-dd hh:mm")}}
						</span>
						<span><span class="light-font">到货时效(天)：</span>{{order.days}}</span>
						<span>
							<el-space>
							<span class="light-font">材积基数：</span>
							<el-input size="small" 
							          style="width:100px"  
							          @change="handleOrderBase(order)" 
									  v-model="order.base">
							  <template #append>	<el-button size="small" @click="handleOrderBase(order)">确定</el-button></template>
								
							</el-input>
							<div class="text-red" style="padding-left:20px" v-if="notice">{{notice}}！</div>
					</el-space></span>
					</el-space>
					<el-tag
					 v-if="!order.price.edit" @click="order.price.edit=true"
					 type="success" round   effect="dark" >已报价</el-tag>
					<el-tag
					v-else
					 type="warning" round   effect="dark" >待报价</el-tag>
				</div>
				<div class="remark-box font-base flex-between" style="padding-top:10px">
					<div>
						<span><span class="light-font">备注：</span>{{order.remark||'-'}}</span>
					</div>
					<div>
						<span><span class="light-font">渠道：</span>
						<span class="text-green" v-if="order.shipmentTranschannel">{{order.shipmentTranschannel.name}}</span>
						<span class="text-green" v-else>--</span>
						</span>
					</div>
				</div>
				<el-table :data="[]" height="35">
					<el-table-column label="货件/计划名称" width="290px"/>
					<el-table-column label="配送中心" width="120px"/>
					<el-table-column label="体积m³" width="120px" />
					<el-table-column label="发货数" width="100px"/>
					<el-table-column label="计算重量kg"  width="100px"/>
					<el-table-column label="报价"  />
					<el-table-column label="操作" width="80px">
					  
					</el-table-column>
				</el-table>
				<div v-for="address in order.addressList">
					<div  class="flex-center-between address-center">
					<el-space>
						<div><span class="font-extraSmall">编码 : </span>{{address.code}}</div>
							<div><span class="font-extraSmall">件数 : </span>{{address.shipmentList.length}}</div>
						<div><span class="font-extraSmall">重量(kg) : </span>{{address.weight}}</div>
						<div><span class="font-extraSmall">体积(m³) : </span>{{address.cbm}}</div>
						<div><span class="font-extraSmall">计算重量(kg) : </span>{{address.calweight}}</div>
							<div><span class="font-extraSmall"> 地址 : </span></div>
						<div>{{address.addressLine1}}</div>
						<div>{{address.city}},{{address.stateOrProvinceCode}}</div>
						<div>{{address.postalCode}},{{address.countryCode}}</div>
					</el-space>
					<div style="float:right">
						<el-space>
												 <div><span class="font-extraSmall">运输费用：</span>{{formatFloat(address.price.editshipfee)}}</div>
												 <span><span class="font-extraSmall">总金额(￥)：</span></span> 
												 <span class="font-bold">{{formatFloat(address.price.edittotalfee)||'-'}}</span>
						</el-space>
					</div>
					</div>
				<el-table :data="address.shipmentList" :show-header="false">
					 <el-table-column type="expand" label="#">
						  <template #default="props">
							  <el-row :gutter="48">
								  <el-col :span="12">
									 <el-row :gutter="24">
											 <el-col :span="12"
											  v-for=" sub in  props.row.itemList" :key="sub.id"
											 >
											 <div class="sku-item">
												 <div class="flex-center">
												 <el-image class="product-img " :src="sub.image" />
												 <div style="margin-left: 8px;">
												<p>{{sub.name}}</p>	 
												<p class="font-extraSmall ">{{sub.ename}}</p>	 
												<p class="font-extraSmall ">材质：{{sub.material||'-'}}</p>	 
												</div>
												 </div>
												 <div>
													 <div><span class="font-extraSmall">x </span>{{sub.quantity}}</div>
												 </div>
											 </div>
											 </el-col>
									 </el-row>
								  </el-col>
								  <el-col :span="12">
								  		<el-row :gutter="8">
											<el-col :span="12"  v-for=" (box,index) in  props.row.boxList" :key="box.id">
												<div class="box-style font-small">
													<el-space direction="vertical" alignment="normal">
														<span><el-tag type="info" round>箱子{{index+1}}</el-tag></span>
													<p><span>ID：</span>{{box.boxid}}</p>
														<p>
														<span>长*宽*高<span class="font-extraSmall">(cm)：</span></span>
														<span>{{box.length}}*{{box.width}}*{{box.height}}</span></p>
													</el-space>
										           <el-space >
													<p>重量<span class="font-extraSmall">(kg)：</span>{{box.weight}}</p>
													<p>材积重量<span class="font-extraSmall">(kg)：</span>{{box.dimweight}}</p>
													</el-space>
												</div>
											</el-col>
										</el-row>
								  </el-col>
							  </el-row>
						  </template>  
					 </el-table-column>	 
					<el-table-column label="货件/计划名称" width="240px">
						<template #default="scope">
							<div>{{scope.row.shipmentid}}</div>
							<div class="font-extraSmall">{{scope.row.name}}</div>
						</template>
					</el-table-column>
					<el-table-column label="配送中心" width="120px">
						<template #default="scope">
							<div>{{scope.row.destination}}({{scope.row.country}})</div>
							<div class="font-extraSmall"><span v-if="scope.row.area">{{scope.row.area}}</span></div>
						</template>
					</el-table-column>
					<el-table-column label="体积m³" prop="cbm" width="120px" >
					<template #default="scope">
							<div>{{scope.row.cbm}}</div>
							<div><span class="font-extraSmall">材积:</span>{{scope.row.dimweight}}</div>
						</template>
					</el-table-column>
					<el-table-column label="发货数" prop="num" width="100px">
					  <template #default="scope">
						  <div>{{scope.row.num}}</div>
							<div><span class="font-extraSmall">箱数:</span>{{scope.row.boxList.length}}</div>
						</template>
					</el-table-column>
					<el-table-column label="计算重量kg" prop="weight" width="100px">
					<template #default="scope">
							<div>{{scope.row.calweight}}</div>
							<div><span class="font-extraSmall">重量:</span>{{scope.row.weight}}</div>
						</template>
					</el-table-column>
					<el-table-column label="报价"  >
					  <template #default="scope">
						  <el-form label-width="auto" class="priceInnerform" :disabled="!order.price.edit" v-if="!scope.row.price.disabled" size="small">
							  <el-form-item label="渠道">
								    <el-select
								      v-model="scope.row.price.supplierChannel"
								      filterable
								      default-first-option
								      :reserve-keyword="false"
								      placeholder="贵司报价表的产品渠道名称"
								    >
									<template #prefix>
										<el-button   @click.stop="handleShowChannelEdit(order.shipmentTranschannel)" size="small"  type="info"  text  bg>
											<el-icon class="font-base font-bold"><Setting /></el-icon>
										</el-button>
									</template>
								      <el-option
									   v-if="order.shipmentTranschannel"
								        v-for="item in order.shipmentTranschannel.supplierTranschannelList"
								        :key="item.id"
								        :label="item.name"
								        :value="item.id"
								      />
								    </el-select> &nbsp;&nbsp;
								  <el-tooltip effect="light">
								  											 <template #content>
								  												 请严格对应贵司提供的报价表的产品渠道名称
								  											 </template>
																			
								  											  <el-icon class="text-info"><InfoFilled /></el-icon>
								  </el-tooltip>
							  </el-form-item>
							  <el-form-item label="价格">
								  <el-space>
								  							<div><el-input placeholder="基本单价" @input="handleChangePrice(scope.row,order)" v-model="scope.row.price.editunitprice" size="small"></el-input></div>
								  							<el-icon><Plus /></el-icon><div><el-input placeholder="附加单价" @input="handleChangePrice(scope.row,order)" v-model="scope.row.price.edittax" size="small"></el-input></div>
								  							<el-select v-model="scope.row.price.ftype" 
								  							           @change="handleChangePrice(scope.row,order)"
								  							           style="width:60px" 
								  									   size="small">
								  															  <el-option label="kg" value="kg"></el-option>
								  															  <el-option label="cbm" value="cbm"></el-option>
								  							</el-select>
															<el-tooltip effect="light">
																										 <template #content>
																											 <div   v-if="scope.row.minfee&&scope.row.minfee<10000000">最低价：{{formatFloat(scope.row.minfee)}}</div>
																											 <div   v-if="scope.row.minfeeSecond&&scope.row.minfeeSecond<10000000">次低价：{{formatFloat(scope.row.minfeeSecond)}}</div>
																										 </template>
																										  <el-icon class="text-red"><InfoFilled /></el-icon>
															</el-tooltip>
								  							</el-space> 
							  </el-form-item>
							  <el-form-item label="其他费用">
								  <div class="flex-between">
								  	    <el-input placeholder="其它费用" @input="handleChangePrice(scope.row,order)" v-model="scope.row.price.editotherfee" size="small"></el-input>
										
								  </div>
							  </el-form-item>
							  <el-form-item label="报价备注">
								  <el-input   v-if="scope.row.markVisable==true" v-model="scope.row.price.editremark" type="text"></el-input>
								  <div v-else   class="table-edit-flex" @click="editRemarks(scope.row)">
								  		<el-tooltip placement="left-start"   >
								  			<template #content><div style="max-width:400px;"  v-html="scope.row.price.editremark"></div></template>
								  			<span class="text-omit-3" v-html="scope.row.price.editremark"> </span>
								  		</el-tooltip>
								  		   <el-icon ><Edit/></el-icon>
								  </div>
								 
							  </el-form-item>
						  </el-form>
						  <div v-else class="text-orange">
							  已放弃报价
						  </div>
						</template>
					</el-table-column>
					<el-table-column label="操作" width="80px">
					  <template #default="scope">
							 <el-button size="small"  :disabled="!order.price.edit" v-if="!scope.row.price.disabled"  @click="scope.row.price.disabled=true;refreshTotalFee()" type="warning">放弃 </el-button>
							 <el-button size="small"  :disabled="!order.price.edit" v-else @click="scope.row.price.disabled=false;refreshTotalFee()" >恢复 </el-button>
						</template>
					</el-table-column>

				</el-table>
				</div>
				<div class="flex-between">
					<div class="quot-cal-wrap font-base">
						<div class="block-radio">
														 <div class="item">
															 <div class="box el-tag el-tag--success el-tag--light"    v-if="order.minfee&&order.minfee<10000000">
															   <span ><span class="font-extraSmall">最低价</span></span>
															   <div class="font-bold">{{order.minfee.toFixed(2)}}￥</div>
															 </div>
															 <div class="box el-tag el-tag--primary el-tag--light" v-if="order.minfeeSecond&&order.minfeeSecond<10000000">
															   <span ><span class="font-extraSmall">次低价</span></span>
															   <div class="font-bold">{{order.minfeeSecond.toFixed(2)}}￥</div>
														    </div>
														 </div>
						</div>
					</div>
					<div class="quot-cal-wrap font-base">
						<el-space direction="vertical" fill  style="width:300px;">
							<div class="font-extraSmall">您可以按重量或体积方式计算费用</div>
					         <div class="block-radio">
								 <div class="item">
									 <div class="box" >
									   <span >总重量<span class="font-extraSmall">(kg)</span></span>
									   <div class="font-bold">{{order.weight}}</div>
									 </div>
									 <div class="box">
									   <span >总体积<span class="font-extraSmall">(m³)</span></span>
									   <div class="font-bold">{{order.cbm.toFixed(2)}}</div>
								    </div>
								 </div>
							 </div>
							 <div v-show="false" class="flex-center-between">
								 <span>单价<span class="font-extraSmall">(￥)</span></span>
								 <el-input-number :disabled="true"
								 v-model="order.price.editunitprice"
								 @change="handlePrice(order.price)"
								 ></el-input-number>
							 </div>
							 <div v-show="false" class="flex-center-between">
								 <span>其他费用<span class="font-extraSmall">(￥)</span></span>
								 <el-input-number :disabled="true"
								 v-model="order.price.editotherfee"
								 @change="handlePrice(order.price)"
								 ></el-input-number>
							 </div>
							 <div class="flex-center-between">
								 <div><span class="font-extraSmall">运输费用：</span>{{formatFloat(order.price.editshipfee)}}</div>
								 <span><span class="font-extraSmall">总金额(￥)</span></span> 
								 <span class="font-bold">{{formatFloat(order.price.edittotalfee)||'-'}}</span>
							 </div>
							 <div v-if="selectType==2||selectType==4" class="flex-center-between" style="padding-top:20px">
								 <span></span> 
								 <el-button v-if="!order.price.edit"   plain  @click="order.price.edit=true">
									 <span>重新编辑</span>
								 </el-button>
								 <el-button v-else
								  @click="handleSubmitPrice(order)"
								  type="primary">
								  <span >提交报价</span>
								  </el-button>
							 </div>
						</el-space>
					</div>
				</div>
				
			</div>
			</div>
				<div  style="display:flex;justify-content: center;">
					<pagination
						background 
						 v-if="tableData.total >1"
						 :total="tableData.total"
						 v-model:page="queryParam.currentpage"
						 v-model:limit="queryParam.pagesize"
						 @pagination="handleQuery"
						  layout="total,  prev, pager, next, jumper"
						  :page-sizes="[1,10,20]"
					   />
			   </div>
			   </div>
		</div>
	
	</div>
</div>
<div class="quot-wrap-main" v-else>
	<div class="quot-wrap">
		<div class="quot-top-banner">
			<div class="banner-header">
			</div>
			<div class="quot-shipment-box "  >
				<div class="pag-radius-bor" style="width:600px">
					<div class="order-header flex-center-between font-base">
						<div></div>
						<div style="width:300px">
							<h1 style="padding:20px;text-align: center;">欢迎登录</h1>
							<el-form>
								<el-form-item label="登录账号">
									<el-input placeholder="对应手机号码"  v-model="loginForm.mobile"></el-input>
								</el-form-item>
								<el-form-item label="登录密码"  >
									<el-input type="password" show-password v-model="loginForm.password"></el-input>
								</el-form-item>
								<el-form-item label="">
									<el-checkbox @click="showProtocolDialog" v-model="isagree">我同意报价协议</el-checkbox>
								</el-form-item>
							</el-form>
								
							<div class="flex-between">
								<el-button link type="primary" @click="resetPasswordDialog" :disabled="!isagree">重置密码</el-button>
								<el-button type="primary" :disabled="!isagree" @click="handleLogin">登 录</el-button>
							</div>
							
						</div>
						<div></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
 <ChannelDialog ref="channelDialogRef" @change="handleChannelChange"></ChannelDialog>
 <ProtocolDialog ref="protocolDialogRef" @change="handleProtocolChange"></ProtocolDialog>
 <ResetPasswordDialog ref="resetPasswordDialogRef"></ResetPasswordDialog>
</template>

<script setup>
	import {Search,ArrowDown,Edit,InfoFilled,Plus,Setting} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick,computed  } from 'vue'
	import supplierApi from '@/api/quote/supplierApi.js';
	import {ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import { ElLoading } from 'element-plus';
	import { useRoute } from 'vue-router';
	import ChannelDialog  from "./components/channel_dialog.vue";
	import ProtocolDialog  from "./components/protocol_dialog.vue";
	import ResetPasswordDialog  from "./components/reset_password_dialog.vue";
	const globalTableRef=ref();
	const channelDialogRef=ref();
    const token=GetRequest();
	const protocolDialogRef=ref();
	const resetPasswordDialogRef=ref();
	const state = reactive({
		tableData:{records:[],total:0},
		queryParam:{
			 token:null,
			 currentpage:1,
			 pagesize:1,
			 sort:"opttime",
			 order:"desc",
			 displayType:'center',
		},
		isagree:false,
		islogin:true,
		isActive:'kg',
		selectType:"2",
		editChannel:{},
		loginForm:{},
		pageTitle:"供应商",
		notice:"",
		 localRow:{},
		 localOrder:null,
		 markVisable:false,
	})
	
	const{
		 queryParam,tableData,loading,selectType,editChannel,pageTitle,
		 isActive,localRow,markVisable,localOrder,notice,islogin,isagree,loginForm
	}=toRefs(state)
    const route = useRoute();
    state.pageTitle = route.query.title ||"供应商";
	function handleQuery(){
		state.queryParam.status= parseInt(state.selectType);
		loadTableData(state.queryParam)
	}
	function getTitle(){
		if(state.selectType==2)return "待报价";
		if(state.selectType==3)return "已报价-未中标";
		if(state.selectType==4)return "已报价-等待开标";
		if(state.selectType==5)return "已报价-已中标";
		return "选择类型";
	}
	function showProtocolDialog(){
		protocolDialogRef.value.show(route.query.title || '报价方');
	}
	function resetPasswordDialog(){
		resetPasswordDialogRef.value.show(state.loginForm.mobile,state.queryParam.token);
	}
	function showSetDialog(rows,orders){
		state.markVisable=true;
		rows.price.editremark=rows.price.remark;
		state.localRow=rows;
		state.localOrder=orders;
	}
	function handleProtocolChange(value){
		state.isagree=value;
	}
    function handleShowChannelEdit(shipmentTranschannel){
		state.editChannel=shipmentTranschannel;
		channelDialogRef.value.show(state.queryParam.token,shipmentTranschannel.id);
	}
	function handleChannelChange(list){
		state.editChannel.supplierTranschannelList=list;
	}
	function handleTypeChange(unit,item){
		if(unit==="kg"){
			item.quotationPriceList[0].weight =item.weight;
			item.quotationPriceList[0].ftype="kg";
			
		}else{
			item.quotationPriceList[0].weight =item.cbm;
			item.quotationPriceList[0].ftype="cbm";
		}
		handlePrice(item.quotationPriceList[0]);
	}
	function handleOrderBase(order){
		var data={"orderid":order.id,"base":order.base,"token":token};
		supplierApi.updateBase(data).then(res=>{
			ElMessage.success('修改成功！');
				handleQuery();
		})
	}
	function handleDisable(row){
		if(row&&row.price){
			row.price.disabled=true;
		}
	}
	function handleLogin(){
		state.loginForm.operate="login";
		supplierApi.submitlogin(state.queryParam.token,state.loginForm).then(res=>{
			state.islogin=true;
			localStorage.setItem("wimoor_supplier_login_time"+state.queryParam.token,new Date());
			localStorage.setItem("wimoor_supplier_login_name"+state.queryParam.token,res.data);
		})
	}
	function handleSubmitPrice(order){
		refreshTotalFee();
		var priceList=[];
		var iserrorWithoutChannel=false;
		order.addressList.forEach(address=>{
			address.shipmentList.forEach(shipment=>{
				var price=shipment.price;
				if(price.ftype==="kg"){
					price.weight =shipment.calweight;
				}else{
					price.weight =shipment.cbm;
				}
				price.unitprice=price.editunitprice;
				price.tax=formatFloat(parseFloat(price.edittax)*parseFloat(price.weight)); 
				price.remark=price.editremark;
				//price.tax=price.edittax;
				price.otherfee=price.editotherfee;
				price.totalfee=price.edittotalfee;
				price.shipfee=price.editshipfee;
				price.base=order.base;
				if(!price.supplierChannel&&!price.disabled){
					iserrorWithoutChannel=true;
				}
				if(price.unitprice && price.unitprice!=null && price.unitprice!="" &&   price.unitprice!=undefined && parseFloat(price.unitprice)>0){
				   priceList.push(price);
				}
			})
		})
		
		if(priceList&&priceList.length>0){
			var price=order.price;
			price.weight=price.weight;
			price.unitprice=price.editunitprice;
			price.otherfee=price.editotherfee;
			if(!price.editunitprice){
				price.tax=0;
				price.totalfee=0;
				price.shipfee=0;
				price.base=order.base;
				priceList.forEach(item=>{
					if(!item.disabled){
						if(item.tax){
							price.tax=price.tax+parseFloat(item.tax);
						}
						price.totalfee=price.totalfee+parseFloat(item.totalfee);
						if(item.otherfee){
						   price.otherfee=price.otherfee+parseFloat(item.otherfee);
						}
						price.shipfee=price.shipfee+parseFloat(item.shipfee);
					}
				});
				priceList.push(price);
			}
			if(iserrorWithoutChannel){
				ElMessage.error("渠道信息为必填信息");
				return;
			}
			supplierApi.submitPrice(token,priceList).then(rec=>{
				ElMessage.success('报价成功！');
				handleQuery();
			})
		}else{
			ElMessage.error('请填写单价！');
		}

	}
	function editRemarks(row){
		row.markVisable=true;
		row.price.editremark=row.price.remark;
	}
	
	function handleChangePrice(row,order,isitem){
		var weight=0;
		var unitprice=0;
		var otherfee=0;
		var tax=0;
		var price=row.price;
		var remark="";
		if(price.ftype==="kg"){
			price.weight =row.calweight;
		}else{
			price.weight =row.cbm;
		}
		if(price.weight){
			weight=price.weight;
		}
		if(price.editunitprice){
			unitprice=price.editunitprice;
		}
		if(price.editotherfee){
			otherfee=price.editotherfee;
		}
		if(price.editremark){
			remark=price.editremark;
		}
		if(price.edittax){
			tax=price.edittax;
		}
		row.price.editshipfee=formatFloat(parseFloat(unitprice)*parseFloat(weight));
		row.price.edittotalfee=formatFloat(parseFloat(unitprice)*parseFloat(weight)+parseFloat(otherfee)+parseFloat(weight)*parseFloat(tax));
		var key=token;
		if(price.orderid){
			key=key+price.orderid;
		}
		if(price.destination){
			key=key+price.destination;
		}
		if(price.shipmentid){
			key=key+price.shipmentid;
		}
		sessionStorage.setItem(key,JSON.stringify(price));
		if(!isitem){
			refreshTotalFee();
		}
		
	}
	function refreshTotalFee(res){
		state.tableData.records.forEach(order=>{
			var oshipfee=0;
			var ototalfee=0;
			order.addressList.forEach(address=>{
				var shipfee=0;
				var totalfee=0;
				var calweight=0;
				address.shipmentList.forEach(shipment=>{
					if(!shipment.price.disabled){
						shipment.price.base=order.base;
						if(shipment.price.editshipfee){
							shipfee=shipfee+parseFloat(shipment.price.editshipfee);
							oshipfee=oshipfee+parseFloat(shipment.price.editshipfee);
						}
						if(shipment.price.edittotalfee){
							totalfee=totalfee+parseFloat(shipment.price.edittotalfee);
							ototalfee=ototalfee+parseFloat(shipment.price.edittotalfee);
						}
						calweight=calweight+shipment.price.weight;
					}
				})
				address.price.base=order.base;
				address.price.editshipfee=shipfee;
				address.price.edittotalfee=totalfee;
			})
			order.price.base=order.base;
			order.price.editshipfee=oshipfee;
			order.price.edittotalfee=ototalfee;
		})
	}
	function  handlePrice(price){
		var weight=0;
		var unitprice=0;
		var otherfee=0;
		if(price.weight){
			weight=price.weight;
		}
		if(price.unitprice){
			unitprice=price.editunitprice;
		}
		if(price.otherfee){
			otherfee=price.editotherfee;
		}
		price.edittotalfee=formatFloat(parseFloat(unitprice)*parseFloat(weight)+parseFloat(otherfee));
	}
	function handleInitPrice(order,orderid,addresscode,shipmentid){
		order.cbm=formatFloat(parseFloat(order.volume)/1000000);
		if(order.quotationPriceList&&order.quotationPriceList.length>0){
			    order.price=order.quotationPriceList[0];
				order.price.weight= order.price.weight;
				order.price.editunitprice=order.price.unitprice;
				if(!order.price.otherfee&&order.price.totalfee!=order.price.shipfee){
					var subfee=parseFloat(order.price.totalfee);
					if(order.price.shipfee){
						subfee=subfee-parseFloat(order.price.shipfee);
					}
					if(order.price.otherfee){
						subfee=subfee-parseFloat(order.price.otherfee);
					}
					if(subfee){
						order.price.tax=subfee;
					}
				}
				if(order.price.tax){
					order.price.edittax=formatFloat(parseFloat(order.price.tax)/parseFloat(order.price.weight),1);
				}else{
					order.price.edittax=null;
				}
				
				order.price.editremark=order.price.reamrk;
				//order.price.edittax=order.price.tax;
				order.price.edittotalfee=order.price.totalfee;
				order.price.editotherfee=order.price.otherfee;
				order.price.editshipfee=order.price.shipfee;
				if(order.price.totalfee){
					order.price.edit=false;
				}else{
					order.price.edit=true;
				}
				if(!order.price.ftype){
					order.price.ftype="kg";
				}
		}else{
			var key=token;
			if(orderid){
				key=key+orderid;
			}
			if(addresscode){
				key=key+addresscode;
			}
			if(shipmentid){
				key=key+shipmentid;
			}
			order.price=sessionStorage.getItem(key);
			if(order.price==null){
				order.price={};
				order.price.orderid=orderid;
				order.price.destination=addresscode; 
				order.price.shipmentid=shipmentid; 
				order.price.weight=order.weight;
				order.price.editunitprice="";
				order.price.editotherfee="";
				order.price.editremark="";
				order.price.edittax="";
				order.price.edittotalfee="";
				if(order.price.totalfee){
					order.price.edit=false;
				}else{
					order.price.edit=true;
				}
				if(!order.price.ftype){
					order.price.ftype="kg";
				}
			}else{
				order.price=JSON.parse(order.price);
			}
		}
	}
	function loadTableData(param){
	var loading=ElLoading.service({
						lock: true,
						text: '加载中',
						background: 'rgba(0, 0, 0, 0.7)',
					  })
		supplierApi.listSupplierOrder(param).then(res=>{
		loading.close()
		var resdata= JSON.parse(JSON.stringify(res));
			if(res.data&&res.data.records&&res.data.records.length>0){
				state.tableData.records=res.data.records;
				state.tableData.records.forEach(order=>{
                   handleInitPrice(order,order.id);
				   order.addressList.forEach(address=>{
					     handleInitPrice(address,order.id,address.code);
						    address.shipmentList.forEach(shipment=>{
							 handleInitPrice(shipment,order.id,address.code,shipment.shipmentid);
							 handleChangePrice(shipment,order,true);
						 })
				   })
				})
			}else{
			    state.tableData.records=[];	
			}
			 state.tableData.total=res.data.total;
			 refreshTotalFee();
			 state.tableData.records.forEach(order=>{
			     resdata.data.records.forEach(item=>{
					 if(item.id==order.id&& item.quotationPriceList && item.quotationPriceList.length>0 && item.quotationPriceList[0].totalfee){
						 if(formatFloat(parseFloat(order.price.edittotalfee))!=formatFloat(parseFloat(item.quotationPriceList[0].totalfee))){
						 	state.notice="请重新提交";	
							order.price.edit=true;
						 }
					 }
					
				 })
			 })
		})
	}
	
	function GetRequest() {
	   var url = location.search; //获取url中"?"符后的字串
	   if (url.indexOf("?") != -1) {    //判断是否有参数
	      var str = url.substr(1); //从第一个字符开始 因为第0个是?号 获取所有除问号的所有符串
	      var strs = str.split("=");   //用等号进行分隔 （因为知道只有一个参数 所以直接用等号进分隔 如果有多个参数 要用&号分隔 再用等号进行分隔）
	      return strs[1].split("&")[0];          //直接弹出第一个参数 （如果有多个参数 还要进行循环的）
	   }
	   return null;
	}
	function scrollHeight(){
	}
	onMounted(()=>{
		state.queryParam.token=token;
		var wimoor_supplier_login_time=localStorage.getItem("wimoor_supplier_login_time"+state.queryParam.token);
		var wimoor_supplier_login_name=localStorage.getItem("wimoor_supplier_login_name"+state.queryParam.token);
		if(wimoor_supplier_login_time){
			var date = new Date()
			date.setTime(date.getTime() - 3600 * 1000 * 24 * (7))
			date = new Date(date);
			wimoor_supplier_login_time= new Date(wimoor_supplier_login_time);
			if(wimoor_supplier_login_time.getTime()>date.getTime()){
				state.islogin=true;
			}
		}
		if(wimoor_supplier_login_name&&state.pageTitle=='供应商'){
			state.pageTitle=wimoor_supplier_login_name;
		}
		handleQuery();
		scrollHeight();
	})
</script>

<style scoped>
	.priceInnerform .el-form-item--small{
		margin-bottom:3px;
	}
	.address-center{
		padding:3px;
		font-size:12px;
		width:100%;
		background-color:var(--el-color-info-lighter);
	}
	.dark .address-center{
		padding:3px;
		font-size:12px;
		width:100%;
		background-color:#3e3e3e;
	}
	.quot-wrap-main{
		background-color: var(--el-color-info-lighter);
	}
	.dark .quot-wrap-main{
		background-color: #3e3e3e;
	}
	.quot-wrap{
		position: relative;
	}
	.quot-top-banner{
		background-image: url("@/assets/image/quot-banner.png");
		background-repeat: no-repeat;
		background-position:center;
		height:259px;
		}
	.quot-shipment-box{
		width: 100%;
		position: absolute;
		top:180px;
		z-index: 1;
		 background-image: linear-gradient(180deg,transparent 80px,  var(--el-color-info-lighter) 80px);
	}	
	.dark .quot-shipment-box{
		width: 100%;
		position: absolute;
		top:180px;
		z-index: 1;
		 background-image: linear-gradient(180deg,transparent 80px,  #3e3e3e 80px);
	}	
	.quot-shipment-box .pag-radius-bor{
		width:1200px;
		margin: 0 auto;
		margin-bottom: 16px;
	}
	.banner-header{
		width: 1200px;
		margin: 0 auto;
	}	
	.banner-header .title{
	  font-size:30px;
	  color: #fff;
	  font-weight: 700;
	  padding-top:48px;
	  margin-bottom:12px;  
	}	
	.banner-header .subtext{
		color:#fff;
	}
	.banner-header .num{
		color: #ff7315;
		font-weight: 1000;
		padding:8px 24px;
		background-color: #fff;
		border-radius:40px;
		display: inline-block;
		margin-right: 8px;
	}
	.quot-cal-wrap{
		display: flex;
		justify-content: end;
		margin-top: 16px;
	}
	.block-radio .item{
		display: flex;
		flex-basis: 1;
		margin: 0 -8px;
	}
	.block-radio .box{
		border: 1px solid var(--el-border-color-light);
		padding:8px 24px;
		border-radius: 6px;
		margin: 0 8px;
		flex: 1;
		cursor: pointer;
	}
	 .block-radio .active{
		 color:var(--el-color-primary);
		 border: 1px solid var(--el-color-primary);
	 }
	 .remark-box{
		 margin-bottom:8px;
	 }
	 .sku-item{
		 margin-top: 4px;
		 margin-bottom: 4px;
		 display: flex;
		 justify-content: space-between;
		 align-items: center;
	 }
	 
	 .box-style{
		 padding: 8px;
		 border: 1px solid var(--el-border-color-light);
		 border-radius: 6px;
		 margin-top: 4px;
		 margin-bottom: 4px;
	 }
</style>