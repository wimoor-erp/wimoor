<template>
	<div class="main-sty chelseaplan">
	    <Header
		 @completeOrder="completeOrder"
		 @pay = "pay"
		 @delete = "deleteOrder"
		 @change="loadData"
		  ></Header>
		<table 
		 class="sd-table m-b-16 " 
		 border="0" cellpadding="0" cellspacing="0">
		 <thead>
			  <tr>
				   <th class="left-border" width="10px">
					   <el-checkbox
					   v-model="allCheckStatus"
					   :indeterminate="isIndeterminate"
					   @change="allCheckChange"
					   ></el-checkbox>
				   </th>
				   <th width="60px">图片</th>
				   <th width="15%">名称/SKU</th>
				   <th width="100px">采购单价(￥)</th>
				   <th width="100px">预计交货日期</th>
				   <th width="80px">实际下单数</th>
				   <th width="80px">金额(￥)</th>
				   <th width="100px">预计可售日期</th>
				   <th width="80px">操作人</th>
				   <th width="60px">操作</th>
			  </tr>
			</thead>
			<tbody
			v-if="tableList && tableList.length>0"
			v-for="item in tableList"
			:key="item.id"
			>
			<tr>
				<td colspan="10" class="right-border-none" style="margin-top:12px ;"></td>
			</tr>
             <tr >
				 <td class="gary-bg left-border">
				 <el-checkbox 
				 @change="checkChange"
				 v-model="item.checked"></el-checkbox></td>
				 <td colspan="9" class="gary-bg">
				 <el-space :size="32">
					 <span>
						 <el-space :size="4">
						 <el-icon ><User /></el-icon>
						<span class="light-font"> 供应商:</span> </el-space>{{item.supplier}}</span>
					 <el-space><span><span class="light-font">订单号:</span> {{item.number}}</span>
					 <el-tag v-if="item.auditstatus===0" type="warning" effect="plain">待提交</el-tag>
					 <el-tag v-else-if="item.auditstatus===1" type="blue" effect="plain">待生产</el-tag>
					  <el-tag v-else-if="item.auditstatus===2" type="blue" effect="plain">待出货</el-tag>
					 <el-tag v-else-if="item.auditstatus===3" type="blue" effect="plain">已出货</el-tag>
					 <el-tag v-else-if="item.auditstatus===4" type="success" effect="plain">已结束</el-tag>
					 </el-space>
					 <span><span class="light-font">下单时间:</span> {{item.createtime}}</span> 
					 <el-space><span class="light-font">付款状态:</span> 
					 <span class=" warning-dot" v-if="item.paystatus===0">待结清</span>
					 <span class=" success-dot" v-else="item.paystatus===1">已结清</span>
					 </el-space>
					 <span><span class="light-font">应付金额:</span>￥{{outputmoney(item.needamount)}}</span>
					 <span><span class="light-font">实付金额:</span>￥{{getPayAmount(item)}}</span> 
				 </el-space>
				 </td>
			 </tr>
             <tr v-for="sub in item.entryList" :key="sub.id">
				  <td class="left-border" ></td>
				 <td>
					 <div class="flex-center">
				 <el-image 
				 class="product-img"
				 v-if="sub.image"
				 :src="sub.image"></el-image>
				 <el-image v-else 
				 :src="$require('empty/noimage40.png')"
				class="product-img"
				 ></el-image>
				 </div>
				 </td>
				 <td>
					 <div >{{sub.name}}</div>
					 <span class="font-extraSmall">{{sub.sku}}</span>
				 </td>
				 <td>
				 	{{sub.price}}
				 </td>
				 <td><div  ><el-date-picker
				        style="width:200px"
						v-model="sub.oktime"
						type="date"
						@change="handleOktimeChange(sub)"
						placeholder="预计交货日期"
					  /></div> </td>
				 <td>{{sub.quantity}}</td>
				 <td>{{outputmoney(sub.quantity*sub.price)}}</td>
				 <td><div v-if="sub.saletime">{{dateFormat(sub.saletime)}}</div></td>
				 <td>{{sub.operatorname}}</td>
				 <td>
					 <el-link
					  @click="handleDetails(item)"
					  type="primary" :underline="false">详情</el-link>
				 </td>
			 </tr>
			</tbody>
			<tbody v-else>
				<tr>
					<td colspan="9" class="right-border-none" style="margin-top:12px ;text-align: center;"><h4>暂无数据!</h4></td>
				</tr>
			</tbody>
		</table>
		<pagination
		    background 
		     v-if="total > 0"
		     :total="total"
		     v-model:page="queryParams.currentpage"
		     v-model:limit="queryParams.pagesize"
		     @pagination="handleQuery"
			  layout="total,  prev, pager, next, jumper"
			  :page-sizes="[10, 20, 50, 100]"
		   />
	</div>
	<DetailsDiglog ref="orderDetailsRef" @change="handleQuery" />
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,} from 'vue'
	import {User,} from '@element-plus/icons-vue'
    import Header from './components/header.vue';
    import DetailsDiglog from './components/details_dialog.vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {formatFloat,CheckInputInt,outputmoney,debounce,dateFormat} from '@/utils/index.js';
	import '@/assets/css/packbox_table.css'
	import productformApi from "@/views/customized/chelsea/api/productionform/productformApi.js";
	const orderDetailsRef =ref(null);
	const state = reactive({
		allCheckStatus:false,
		isIndeterminate:false,
		queryParams:{
			currentpage:1,
			pagesize:10,
			sort:"createtime",
			order:"desc"
		},
		total:0,
      tableList:[
		   
	  ],
	})
	const {
     tableList,
	 isIndeterminate,
	 queryParams,
	 allCheckStatus,
	 total,
	} = toRefs(state)
	function allCheckChange(){
		state.isIndeterminate = false
		state.tableList.forEach(item=>{
					if(state.allCheckStatus){
						item.checked = true;
					}else{
						item.checked = false;
					}
		})
	}
	function handleOktimeChange(sub){
		productformApi.saveOktime({"date":dateFormat(sub.oktime),"id":sub.id}).then(res=>{
			  ElMessage.success("修改成功！");
		})
	}
	function checkChange(){
		var num = 0;
		state.tableList.forEach(item=>{
			 if(item.checked){
						num++;
			 }
		})
	state.isIndeterminate =  num > 0 && num < state.tableList.length;
	if(num===0){
		state.allCheckStatus = false;
	}
	if(num === state.tableList.length){
		state.allCheckStatus = true;
	}
	}
	function loadData(params){
		state.queryParams.paramother=params;
		 handleQuery();
	}
	
	function handleQuery(){
		productformApi.listForm(state.queryParams).then((res)=>{
			state.tableList=res.data.records;
			state.total=res.data.total;
		});
	}
	function saveForm(lists,status){
		lists.forEach(orderData=>{
			orderData.auditstatus=status;
			orderData.needamount=orderData.totalfee-orderData.depositfee;
			orderData.createtime=new Date(orderData.createtime);
			orderData.auditime=new Date(orderData.auditime);
			orderData.opttime=new Date(orderData.opttime);
			orderData.preArrivalDate=new Date(orderData.preArrivalDate);
			orderData.auditime1=orderData.auditime1? new Date(orderData.auditime1):null;
			orderData.auditime2=orderData.auditime2? new Date(orderData.auditime2):null;
			orderData.auditime3=orderData.auditime3? new Date(orderData.auditime3):null;
			orderData.auditime4=orderData.auditime4? new Date(orderData.auditime4):null;
			orderData.paytime=orderData.paytime? new Date(orderData.paytime):null;
			orderData.entryList.forEach(item=>{
				item.createtime=new Date(item.createtime);
				item.opttime=new Date(item.opttime);
				item.oktime=new Date(item.oktime);
			});
		})
		productformApi.saveForm(lists).then((res)=>{
			ElMessage.success("操作成功!");
			handleQuery();
		});
	}
	
	function savePay(lists){
		lists.forEach(orderData=>{
			orderData.payamount=orderData.needamount;
			orderData.paystatus = 1;
			if(orderData.depositfee){
				orderData.needamount=orderData.totalfee-orderData.depositfee;
			}else{
				orderData.needamount=orderData.totalfee;
			}
			orderData.payamount=orderData.needamount;
			orderData.createtime=new Date(orderData.createtime);
			orderData.auditime=new Date(orderData.auditime);
			orderData.opttime=new Date(orderData.opttime);
			orderData.preArrivalDate=new Date(orderData.preArrivalDate);
			orderData.auditime1=orderData.auditime1? new Date(orderData.auditime1):null;
			orderData.auditime2=orderData.auditime2? new Date(orderData.auditime2):null;
			orderData.auditime3=orderData.auditime3? new Date(orderData.auditime3):null;
			orderData.auditime4=orderData.auditime4? new Date(orderData.auditime4):null;
			orderData.paytime=orderData.paytime? new Date(orderData.paytime):null;
			orderData.entryList.forEach(item=>{
				item.createtime=new Date(item.createtime);
				item.opttime=new Date(item.opttime);
				item.oktime=new Date(item.oktime);
			});
		})
		productformApi.saveForm(lists).then((res)=>{
			ElMessage.success("操作成功!");
			handleQuery();
		});
	}
	function completeOrder(){
	  var rows = getrows();
	  if(rows&&rows.length>0){
		saveForm(rows,4);
	  }
	}
	function pay(){
	  var rows = getrows();
	  if(rows&&rows.length>0){
	  	savePay(rows);
	  }
	}
	function deleteOrder(){
	   // var rows = getrows();
	   var ids=[];
	   state.tableList.forEach(item=>{
		   if(item.checked==true){
			   ids.push(item.id);
		   }
	   })
	   if(ids!=null  && ids.length>0){
		   productformApi.deleteForm(ids).then((res)=>{
		   		if(res.data){
					ElMessage.success("已删除单据！")
				}   
		   });
	   }else{
		    ElMessage.error("请选择数据行！")
	   }
	  
	}
	function getrows(){
		var filteredTableList =[];
		state.tableList.forEach(item=>{
			 if(item.checked){
				filteredTableList.push(item);
			 }
		})
		if(filteredTableList.length>0){
			return filteredTableList;
		}else{
			ElMessage.warning("请至少选择一条订单数据!");
		}
	}
	
	function handleDetails(row){
		orderDetailsRef.value.show(row.id);
	}
	
	function getPayAmount(row){
		var amount=0;
		if(row.depositfee){
			amount=amount+row.depositfee;
		}
		if(row.payamount){
			amount=amount+row.payamount;
		}
		return outputmoney(amount);
	}
</script>
<style>
	.chelseaplan   .sd-table {
	     border-left:none;
		 border-right:none;
	}
	.chelseaplan   .sd-table td {
	    border-right: 1px solid #ebeef5;
	}
	.chelseaplan .left-border{
	    border-left: 1px solid #ebeef5;	
	}	
	.chelseaplan .right-border{
	    border-right: 1px solid #ebeef5;	
	}
	.chelseaplan .right-border-none{
		    border-right: none !important;	
		}
	.chelseaplan  .sd-table tr  {
	    border-left:none;
	}
 
 
</style>
<style scoped>
 .gary-bg{
	 background-color: var(--el-bg-color);
 }
</style>