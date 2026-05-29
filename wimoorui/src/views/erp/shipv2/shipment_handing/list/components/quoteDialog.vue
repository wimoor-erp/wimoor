<template>
	<el-dialog
	title="发起/加入物流报价单"
	v-model="quoteVisiable"
	width="60%"
	
	>
	 <el-row>
	    <el-col :span="24">
			 <el-form-item label="已选货件信息:">
			 </el-form-item>
			 	    <el-table 
					:data="selectData"
					row-key="shipmentid"
					default-expand-all
			 	     border
					 height="550"
					 style="margin-bottom: 15px;"
			 	      >
					  <el-table-column type="expand">
					        <template #default="props">
								<div class="expend-bor" style="margin: 0;padding: 0;">
					            <el-table :data="props.row.listbox"  style="padding: 0;margin: 0;"  :row-class-name="tableRowClassName"
								 size="small">
					              <el-table-column label="ID" prop="id" />
					              <el-table-column label="重量" prop="weight" />
					              <el-table-column label="体积" prop="width" >
									  <template #default="scope">
									  		{{scope.row.length}}*{{scope.row.width}}*{{scope.row.height}}CM					 
									   </template>
								  </el-table-column>
					            </el-table>
								</div>
					        </template>
					      </el-table-column>
					 <el-table-column prop="sumqty" label="货件号">
						 <template #default="scope">
							 <span v-if="scope.row.shipment&& scope.row.shipment.shipmentConfirmationId">
								  {{scope.row.shipment.shipmentConfirmationId}}
							 </span>
						  </template>
					 </el-table-column>
					 
					 <el-table-column prop="sumqty" label="发货总数"   >
						 <template #default="scope">
						 		总发货量:{{scope.row.sumqty}}					 
						  </template>
					</el-table-column>
					  <el-table-column prop="sumweight" label="重量体积"   >
						<template #default="scope">
								<div>总重量:{{scope.row.sumweight}}kg</div>	
								总体积:{{scope.row.volume}}cm³		 
						</template>
					  </el-table-column>
					  <el-table-column prop="destination" label="目的地"   >
							<template #default="scope">
									目的地:{{scope.row.market}}	/ <span v-if="scope.row.shipment&& scope.row.shipment.destination">{{scope.row.shipment.destination}}</span>				 
							</template>
					  </el-table-column>
					  <el-table-column prop="destination" label="询价备注"   >
								<template #default="scope">
										<el-input v-model="scope.row.quotaremark"></el-input>
								</template>
					  </el-table-column>
			 	    </el-table>
			  
				<div>
				
				</div>
		</el-col>
	    <!-- <el-col :span="12">
			<el-form-item label="报价单列表:">
			  </el-form-item>
			 <el-table :data="tableData"
			  row-key="id"
			  border
			  default-expand-all >
			 </el-table>
		</el-col> -->
	  </el-row>
	
	
	<template #footer>
		<el-space>
			<el-button @click="submitQuoteOrder" v-if="quoteToken"  type="primary">确认</el-button>
			<div v-else class="text-red">无法获取绑定关系，请到【发货->物流报价->关联设置】 确认绑定关系</div>
		<el-button @click="quoteVisiable=false"  >关闭</el-button>
		</el-space>
	</template>
	</el-dialog>
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import orderApi from '@/api/quote/orderApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import {ElMessage } from 'element-plus'
	 const emit = defineEmits(["change"]);
	 const baseDateRef=ref();
	const state = reactive({
		quoteVisiable:false,
		qutoeType:"2",
		selectData:[],
		qutoeGroup:true,
		quoteToken:null,
		queryParam:{
			baseline:null,
		},
	})
	const{
		quoteVisiable,qutoeType,qutoeGroup,quoteToken,selectData,queryParam,
		}=toRefs(state)
		
	function submitQuoteOrder(){
		var datas={};
		datas.token=state.quoteToken;
		datas.status=1;
		datas.baseline=state.queryParam.baseline;
		if(state.selectData && state.selectData.length>0){
			var shipments=[];
			var shipmentids=[];
			state.selectData.forEach(item=>{
				var shipmentItem={};
				shipmentItem.groupname=item.shipmentAll.groupname;
				shipmentItem.warehousename=item.shipmentAll.warehouse;
				shipmentItem.shipmentid=item.shipment.shipmentConfirmationId;
				shipmentItem.destination=item.shipment.destination;
				shipmentItem.country=item.shipmentAll.countryCode;
				shipmentItem.weight=item.sumweight;
				shipmentItem.volume=item.volume;
				shipmentItem.num=item.sumqty;
				shipmentItem.remark=item.quotaremark;
				shipmentids.push(item.shipmentid);
				if(item.listbox && item.listbox.length>0){
					var boxlist=[];
					item.listbox.forEach(box=>{
						var boxItem={};
						boxItem.shipmentid=item.shipment.shipmentConfirmationId;
						boxItem.boxid=box.id;
						boxItem.unit=box.unit;
						boxItem.length=box.length;
						boxItem.width=box.width;
						boxItem.height=box.height;
						boxItem.weight=box.weight;
						boxItem.wunit=box.wunit;
						boxlist.push(boxItem);
					});
					shipmentItem.boxList=boxlist;
				}
				if(item.toAddress){
					var address={};
					address=item.toAddress;
					shipmentItem.address=address;
				}
				shipments.push(shipmentItem);
			});
			
			datas.shipments=shipments;
		}
		shipmentPlacementApi.toQuote(shipmentids,state.quoteToken).then(ress=>{
			ElMessage.success("保存成功");
			state.quoteVisiable=false;
			emit("change");
		})
		
		
	}
	async function show(rows){
		state.quoteVisiable=true;
		thirdpartyApi.getQuoteToken().then((res)=>{
			state.quoteToken=res.data.buyertoken;
		});
		await getBaseInfo(rows);
	}
	
	async function getBaseInfo(rows){
			var shipmentlist=[];
			if(rows&&rows.length>0){
				for(var i=0;i<rows.length;i++){
					var items=rows[i];
					await shipmentPlacementApi.getBaseInfo({"shipmentid":items+""}).then(res=>{
						console.log(res.data);
						if(res.data){
							if(res.data.itemlist && res.data.itemlist.length>0){
								res.data.children=res.data.itemlist;
							}
							res.data.quotaremark=res.data.plan.remark;
							shipmentlist.push(res.data);
						}
					});
				}
			}
			state.selectData=shipmentlist;
	}
	defineExpose({show});
</script>

 