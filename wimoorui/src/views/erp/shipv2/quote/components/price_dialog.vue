<template>
	 <el-dialog
	    v-model="dialog.visible"
	    width="80%"
		top="6vh"
	    :before-close="handleClose"
	  >
	    <el-table :data="data" height="calc(100vh - 250px)" :show-summary="true">
			<el-table-column label="订单编号" width="190"  prop="number">
				<template #default="scope">
									  <div>{{scope.row.number}}</div>
									 <div><span class="font-extraSmall">货件编号</span>{{scope.row.shipmentid}}</div>
				</template>
			</el-table-column>
			<el-table-column label="供应商" prop="supplier">
				  <template #default="scope">
					  <div>{{scope.row.supplier}}</div>
					  <el-tag v-if="scope.row.supplierStatus=='已中标'" type="success">{{scope.row.supplierStatus}}</el-tag>
					  <el-tag v-if="scope.row.supplierStatus=='已放弃'" type="danger">{{scope.row.supplierStatus}}</el-tag>
				  </template>
			</el-table-column>
			<el-table-column label="订单渠道" prop="orderchannel"></el-table-column>
			<el-table-column label="报价渠道" prop="pricechannel"></el-table-column>
			<el-table-column label="发货日期" width="100" prop="shipdate"></el-table-column>
			<el-table-column label="收货地址" width="80" prop="destination"></el-table-column>
			<el-table-column label="重量" width="90" prop="weight"></el-table-column>
			<el-table-column label="体积" width="90" prop="volume"></el-table-column>
			<el-table-column label="CBM" width="90" prop="cbm"></el-table-column>
			<el-table-column label="发货数量" width="100" prop="shipqty"></el-table-column>
			<el-table-column label="SKU数量" width="100" prop="shipnums"></el-table-column>
			<el-table-column label="报价重量" width="100" prop="reaweight"></el-table-column>
			<el-table-column label="价格" width="80" prop="price"></el-table-column>
			<el-table-column label="总费用(不含附加费)" width="80" prop="totalprice"></el-table-column>
			<el-table-column label="附加费" width="80" prop="tax"></el-table-column>
		</el-table>
	  </el-dialog>
	
</template>

<script setup>
		import {ref,reactive,toRefs,onMounted,computed} from "vue"
		import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
		import {formatFloat} from '@/utils/index.js';
		import {Plus,Edit,Delete} from '@element-plus/icons-vue'
	    import {ElMessage } from 'element-plus'
		import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
		import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
		import orderApi from '@/api/quote/orderApi.js';
		const emit = defineEmits(['change']);
		const state = reactive({
		  // 弹窗属性
		  dialog: { visible: false }  ,
		  supplier:{},
		  data:[],
		  token:"",
		  title:"",
		  submitloading:false,
		});
		const {
		  dialog,
		  supplier,
		  title,
		  token,
		  data,
		  submitloading,
		} = toRefs(state);
  
		function show(orderid,supplierid){
			state.dialog.visible=true;
			orderApi.showDetail({"orderid":orderid,"supplierid":supplierid}).then(res=>{
				state.data=res.data;
				if(state.data&&state.data.length>0){
					state.data.forEach(item=>{
						if(item.supplier.indexOf("(已放弃)")>0){
							item.supplierStatus="已放弃";
							item.supplier=item.supplier.replace("(已放弃)","");
							item.totalprice=0;
							item.price=0;
						}
						if(item.supplier.indexOf("(已中标)")>0){
							item.supplier=item.supplier.replace("(已中标)","");
							item.supplierStatus="已中标";
						}
					})
				}
			});
		}
		defineExpose({show});
 
	 
</script>

<style>
</style>