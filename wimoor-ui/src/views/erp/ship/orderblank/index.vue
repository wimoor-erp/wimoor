<template>
	<div class="card">
	<el-card class="box-card">
	   <div class="card-header">
		    <h3>配货单</h3>
			 <el-divider border-style="dashed" />
			<div class="head-list">
			<el-row  :gutter="16">
				 <el-col :span="8"> 
				  <div class="grid-content" >
				    <label>货件ID:</label><span>{{shipFormData.list.shipmentid}}</span>
				  </div> 
				  <div class="grid-content" >
				    <label>创建日期:</label><span>{{dateFormat(shipFormData.list.createdate)}}</span> 
				  </div>
				  <div class="grid-content" >
					   <label>SKU数量:</label><span>{{shipFormData.list.skuamount}}</span> 
				  </div>
				 </el-col>
                 <el-col :span="8">
				  <div class="grid-content" >
					   <label>发货数量:</label><span>{{shipFormData.list.sumQuantity}}</span>
				 </div>
				 <div class="grid-content" >
				 		 <label>目的仓库:</label><span>{{shipFormData.list.groupname}}-{{shipFormData.list.country}}({{shipFormData.list.center}})</span>			 
				 </div>
				 <div class="grid-content" >
				 		<label>发货仓库:</label><span>{{shipFormData.list.warehouse}}</span> 
				 </div>
				 </el-col>
				 <el-col :span="8">
				  <div class="grid-content" >
				 	<label>备注:</label><span>{{shipFormData.list.remark}}</span> 
				 </div>
				 <div class="grid-content" >
				 	<label>审核时间:</label><span>{{dateFormat(shipFormData.list.auditime)}}</span> 
				 </div>
				 </el-col>
			</el-row>
			<el-image class="qrcode" :src="shipQRcodeUrl"></el-image>
			</div>
			 <el-divider style="border:2px solid #222;" />
	   </div>
	   <div class="card-body">
	      <el-table
	           :data="skuData.list"
			   :size="small"
	           style="width: 100%; margin-bottom: 16px"
	           row-key="id"
	           border
	           default-expand-all
	         >
	          <el-table-column prop="image" label="图片" width="60" >
	             <template #default="scope">
	              <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
	            </template>
	          </el-table-column>
	          <el-table-column prop="name" label="名称/SKU" >
	             <template #default="scope">
	                <div class='name'>{{scope.row.name}}</div>
	                <div class='sku'>{{scope.row.sku}}
					<span v-show="scope.row.boxnum>0">箱规:单箱{{scope.row.boxnum}}个,</span>
					<span v-show="scope.row.boxweight">重{{scope.row.boxweight}}kg,</span>
					<span v-show="scope.row.boxlength"> {{scope.row.boxlength}}*{{scope.row.boxwidth}}*{{scope.row.boxheight}}cm</span>
	                </div>
	            </template>
	          </el-table-column>
			   <el-table-column prop="sellersku" label="平台SKU" width="100" />
			   <el-table-column prop="invquantity" label="可用库存" width="70" />
				 <el-table-column prop="quantityShipped" label="拟发货数" width="70" />
				  <el-table-column prop="" label="配送数量" width="70" />
	         </el-table>
	   </div>
	</el-card>
	  </div>
</template>

<script>
import { ref,reactive,onMounted } from 'vue'	
import {dateFormat} from '@/utils/index.js';
import { useRoute } from "vue-router"
import orderblankApi from '@/api/erp/ship/orderblankApi.js';
export default {
	name: 'OrderBlank',
	components: {},
	setup(){
		onMounted(()=>{
			getshipFormData()
			showQRCode()
		})
		let shipQRcodeUrl=ref()
		let skuData = reactive({list:[]})
		let shipFormData = reactive({list:[]})
		const route = useRoute()
		const shipmentid=route.query.shipmentid;
	  
		let getshipFormData = function(){
			orderblankApi.getQuotainfo(shipmentid).then((res)=>{
				shipFormData.list = res.data
				skuData.list = res.data.itemList
			})
		}
		let showQRCode=()=>{
					  orderblankApi.getQRCode(shipmentid).then(res => {
									 const blob = new Blob([res]);
								     let url = window.URL.createObjectURL(blob);
									 shipQRcodeUrl.value=url;
		   });
		}
		return{
			getshipFormData,
			shipFormData,
			dateFormat,
			showQRCode,
			shipQRcodeUrl,
			skuData,
		}
	}
}	
</script>

<style>
	.card{
		display: flex;
		justify-content: center;
		background-color:var(--el-bg-color);
		min-height:100%;
	}
	.box-card{
		width:800px;
	}
	.card-header h3{text-align: center;}
	.grid-content{font-size:13px;margin-bottom:8px;}
	.head-list{display: flex;}
	.head-list .el-row{flex:1}
	.el-divider--horizontal{margin:8px 0 !important}
	.card-body .name{line-height: 1;}
	.card-body .el-table {
		--el-table-border-color: #2c2c2c;
	}
</style>
