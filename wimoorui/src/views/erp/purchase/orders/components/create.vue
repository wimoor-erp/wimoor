<template>
	<div :class="isdialog?'':'el-white-bg'">
					 <el-scrollbar class="he-scr-car" :style="isdialog?'height:calc(100vh - 226px);':'height:calc(100vh - 126px);'" @scroll="scroll">
						 <div v-if="!isdialog" class="gird-line-head">
						 <h3>填写采购单</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							
						 <el-form :model="form"
						   label-width="120px">
							  <el-row>
							 <el-col :span="12">
							 <el-form-item v-if="form.ordertype=='normal'" label="单据编码"   >
								 <el-input class="in-wi-24" disabled  v-model="form.number" placeholder="请输入"> </el-input>
							</el-form-item>
							 <el-form-item v-else label="起始编码"   >
							 	<el-input class="in-wi-24" disabled  v-model="form.number" placeholder="请输入"> </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="订单类型" >
							 		 <el-radio-group v-model="form.ordertype"  @change="changOrderType" class="ml-4">
							 		      <el-radio label="normal">单个</el-radio>
							 		      <el-radio label="more">批量新增 (按供应商生成多个订单)</el-radio>
							 		 </el-radio-group>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
								 <el-form-item label="入库仓库" prop="warehouse">
								 	   <Warehouse v-if="planwarehouseid" :defaultid="planwarehouseid"  @blur.stop="e=>handleBlur(e,scope.row)" @changeware="getWarehouse" defaultValue="only" />
									   <Warehouse v-else  @changeware="getWarehouse" defaultValue="only" @blur.stop="e=>handleBlur(e,scope.row)" />
								 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="店铺" >
							 	 <GroupSelect  ref="groupRef" @change="changeGroup" :defaultValue="plangroupid"/>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="采购员" >
							 	<OwnerAll @owner="changeOwner" notAll="isNotAll"  ></OwnerAll>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12" v-if="form.ordertype=='normal'">
							 <el-form-item label="供应商" prop="supplier" >
										 <el-input  :readonly="true" class="in-wi-24" @click.stop="selectSupplier()" v-model="form.name" placeholder="请选择供应商">
											   <template #append>
													<el-button @click.stop="selectSupplier()" :icon="Operation" />
											   </template>
										 </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="24">
							 <el-form-item label="备注"  >
							 	<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="备注"> </el-input>
							 </el-form-item>
							 </el-col>
							  </el-row>
						 </el-form>
						
						 <el-divider></el-divider>
						 <div class="mark-re">
						   <h5 >商品列表({{summary.skunum}}) <span class="text-orange" style="padding-left:10px">￥{{outputmoney(summary.totalprice)}}</span> </h5>
						   <div>
						   	<el-space>
								<el-tooltip
										 effect="dark"
										 content="组装产品可生成加工单"
										 placement="top-start"
								>
								 	<el-checkbox
									 v-model="form.syncAssembly" label="同步生成加工单" />	
								   </el-tooltip>
							    <el-button @click="handleAdd" type="primary">添加产品</el-button>	   
						   <el-button @click="showConsModal" >辅料采购</el-button>
						   <el-button @click="showUploadFile" >导入产品</el-button>
						   </el-space>
						 </div>
						 </div>
					<div v-loading="tableLoading">
						 <table class="sd-table"  border="0" cellpadding="0" cellspacing="0">
							 <thead>
								 <tr>
									 <th width="65">图片</th>
									 <th >名称/SKU</th>
									 <th  >数量</th>
									 <th  >单价</th>
									 <th>总金额</th>
									 <th v-if="form.ordertype=='more'">供应商</th>
									 <th width="245">预计到货日期</th>
									 <th width="80">操作</th>
								 </tr>
							 </thead>
							 <tbody v-for="(item,index) in tableData">
								 <tr >
									 <td>
										 <el-image   v-if="item.image" :src="item.image"  class="product-img"   ></el-image>
										 <el-image    v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
									 </td>
									 <td >
										 <el-space>
											 <div>
											 <el-tooltip
												 effect="dark"
												 :content="item.name"
												 placement="top-start"
											 >
												      <div class='name overflow-text' >{{item.name}}</div>
												       </el-tooltip>
										 <div class='sku' ><span class='pointer' @click="handleToMaterial(item.id)" >{{item.sku}}</span>
										  <el-checkbox v-if="item.issfg=='1'" v-model="item.checkdsub" @change="changeCheckSub(item)" label="购买子SKU" size="small" />
										 </div>
										    </div>
										 <el-tag type="warning" v-if="item.issfg=='1'"   effect="plain">组合</el-tag>
										 </el-space>
									 </td>
									 <td >
										 <el-input v-if="item.issfg=='1' && item.checkdsub==true" v-model="item.num" @input="changesubtotal(item)">
										 </el-input>
										 <div v-else>
											 <el-input  v-model="item.num" @input="changeNum(item)">
											 </el-input>
											 <div class="font-extraSmall text-warning" v-html="item.warningText"></div>
										 </div>
									 </td>
									 <td >
										 <el-tooltip
										            class="box-item"
										            effect="dark"
										            placement="top"
													raw-content
													:content="item.pricestr"
										          >
										 		<el-input v-model="item.price" @input="changePrice(item)"  v-if="item.issfg!='1' ||  item.checkdsub==false">
													<template #prefix >
													  ￥
													</template>
										 		</el-input>
										 </el-tooltip>
									 </td>
									 <td >
										<el-input v-model="item.totalprice"  @input="changeTotalPrice(item)"   v-if="item.issfg!='1' || item.checkdsub==false">
										        <template #prefix >
										          ￥
										        </template>
										      </el-input>
									 </td>
									 <td v-if="form.ordertype=='more'">
										<el-input @click.stop="selectSubSupplier(item)" v-if="item.issfg!='1'||  item.checkdsub==false"  :readonly="true"    v-model="item.supplier" placeholder="请选择供应商">
										  <template #append>
												<el-button @click.stop="selectSubSupplier(item)" :icon="ArrowDownBold" />
										   </template>
										</el-input> 
									 </td>
									 <td >
										 <el-date-picker
										        v-if="item.issfg!='1' ||  item.checkdsub==false"
										         v-model="item.deliverycycledate"
										         type="date"
										         placeholder="请选择"
										       />
									 </td>
									 <td>
										  <el-button @click="handleDelete(index)" link type="primary" >删除</el-button>
									 </td>
									<!-- 子产品 -->
								 </tr>
								<tr v-if="item.issfg=='1' && item.checkdsub==true">
									 <td :colspan="colspan" class="subtable">
										 <el-table :data="item.subsku" size="small">
											 <el-table-column label="图片" width="65">
											   <template #default="scope">
												 <el-image v-if="scope.row.image" :src="scope.row.image"  class="product-img"   ></el-image>
												 <el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
											  </template> 
											 </el-table-column>
											 <el-table-column label="名称/子SKU" show-overflow-tooltip>
											   <template #default="scope">
												<div class='name'>{{scope.row.mname}}</div>
												<div class='sku'><span class="pointer" @click="handleToMaterial(scope.row.submid)">{{scope.row.sku}}</span></div>
											  </template> 
											 </el-table-column>
											  <el-table-column label="单位个数" width="100" prop="subnumber"/>
											 <el-table-column label="剩余库存" width="200" prop="inbound" >
													<template #default="scope">
														<div v-if="scope.row.submap &&scope.row.submap.newmoreqty">{{scope.row.submap.newmoreqty}}</div>
														<div v-else>0</div>
														<div class="font-extraSmall">
														<el-tooltip  v-if="scope.row.submap && scope.row.submap.amount" content="可用库存">
															 <span>{{scope.row.submap.amount}}</span>	
													   </el-tooltip> 
													   <el-tooltip  v-else content="可用库存">
													   		 <span>0</span>	
													   </el-tooltip> 
													   <el-tooltip v-if="scope.row.submap&&scope.row.submap.inbound" content="待入库">
															<span > + {{scope.row.submap.inbound}}</span>
													   </el-tooltip> 
													<el-tooltip v-if="scope.row.submap&&scope.row.submap.needprocess" content="组装需求">
															 <span v-if="scope.row.submap.needprocess"> - {{scope.row.submap.needprocess}}</span>
													</el-tooltip> 
														
														</div>
																
													</template> 
											 </el-table-column>
											 <el-table-column label="数量">
											   <template #default="scope">
											   <el-input v-model="scope.row.num" @input="changeSubNum(scope.row)">  </el-input>
											   <div class="font-extraSmall text-red" v-if="scope.row.reallynum&&scope.row.num!=scope.row.reallynum">计算采购：{{scope.row.reallynum}}</div>
											   <div class="font-extraSmall text-warning" v-html="scope.row.warningText"></div>
											  </template> 
											 </el-table-column>
											 <el-table-column label="单价">
											   <template #default="scope">
												   <el-tooltip
												           class="box-item"
												           effect="dark"
												           placement="top"
														   raw-content
														   :content="scope.row.historyprice"
												         >
														<el-input v-model="scope.row.subprice"  @input="changeSubPrice(scope.row)">
														  <template #prefix >
															￥
														  </template>
														</el-input>
												</el-tooltip>
											  </template> 
											 </el-table-column>
											 <el-table-column label="总金额">
											   <template #default="scope">
											    <el-input  
												v-model="scope.row.totalprice"  @input="changeSubTotalPrice(scope.row)" >
											        <template #prefix >
											          ￥
											        </template>
											      </el-input>
											  </template> 
											 </el-table-column>
											 <el-table-column label="供应商" v-if="form.ordertype=='more'">
												 <template #default="scope">
													<el-input @click.stop="selectSubSupplier(scope.row)"  :readonly="true"  v-model="scope.row.supplier" placeholder="请选择供应商">
													  <template #append>
															<el-button @click.stop="selectSubSupplier(scope.row)" :icon="ArrowDownBold" />
													   </template>
													</el-input> 
												  </template> 
											 </el-table-column>
											 <el-table-column label="预计到货日期" width="245">
											   <template #default="scope">
												<el-date-picker
												        v-model="scope.row.deliverycycledate"
												        type="date"
												        placeholder="请选择"
												      />
											  </template> 
											 </el-table-column>
											 <el-table-column label="操作" width="80">
												 <template #default="scope">
													   <el-button @click="handleSubDelete(item.subsku,scope.row,scope.$index)" link type="primary" >删除</el-button>
												 </template>
											 </el-table-column>
										 </el-table>
									 </td>
								 </tr>
							 </tbody>
						 </table>
						 </div>
						</div>
					</el-scrollbar>
					<!-- 提示表单信息 -->
					<el-row>
					 <div 
					 v-if="isShowAlert||sameSku.length>0"
					 @click="HandlePreview"
					 class="self-alert-blue ">
					   <span v-show="form.syncAssembly">
						 表单中将会生成 {{assembiySkus.length}} 个加工单! 
						</span>
						<span v-show="sameSku.length>0"> &nbsp;有 {{sameSku.length}} 个相同SKU将合并采购数量!</span>
						 点击查看
					 </div>
					 <el-col :span="6" :offset="isdialog?8:3">
				<div class='text-center mar-top-16' >
					 <div style="padding-bottom:0px;">
						<el-space>
							<el-button type="" @click="closeTab">关闭</el-button>
							<!-- <el-button type="" @click="submitForm('save')">保存</el-button> -->
							<el-button type="primary" :loading="submitloading" @click="submitForm('submit')">提交</el-button>
						</el-space>
					</div>
				</div>
				</el-col>
				</el-row>
		<el-dialog
		   v-model="uploadVisible"
		   title="导入产品"
		   width="400px"
		 >
		 <el-upload
			:drag="true"
			action
			:http-request="uploadFiles"
			:limit="1"
			:before-upload="beforeUpload" 
			:show-file-list="true" 
			:headers="headers" 
			accept=".xls,.xlsx"
			multiple
		   >
			 <el-icon class="font-large"><upload-filled /></el-icon>
			 <div class="el-upload__text">
			  拖拽文件到此处或 <em>点击上传</em>
			 </div>
		   </el-upload>
		 <template #footer>
		   <span class="dialog-footer">
			   <div class="flex-center-between">
			 <el-button type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
			 <div>
			 <el-button @click="uploadVisible = false">取消</el-button>
			 <el-button type="primary" @click.stop="uploadExcel">
			   上传文件
			 </el-button></div></div>
		   </span>
		 </template>
		  </el-dialog>
		<el-dialog  v-model="consVisible"
		   title="辅料清单"
		   width="800px">
			<el-table  :data="constableData" style="width: 100%" :border="false">
				<el-table-column type="expand">
				      <template #default="props">
						  {{ props.row.state }}
				          <el-table :data="props.row.mainlist" :border="true">
				            <el-table-column label="图片" prop="sku" >
								<template #default="scope">
									<el-image v-if="scope.row.material && scope.row.material.image" :src="scope.row.material.image"  class="product-img"   ></el-image>
									<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
								</template>
							</el-table-column>
				            <el-table-column label="SKU" prop="sku" />
				            <el-table-column label="主产品名称" prop="name" />
				            <el-table-column label="对应比例" prop="rate" >
								 <template #default="scope">
									1:{{scope.row.rate}}
								 </template>
							</el-table-column>
				            <el-table-column label="采购数量" prop="mainPurchaseQty" />
				          </el-table>
				      </template>
				</el-table-column>
				<el-table-column label="图片" prop="image" >
					 <template #default="scope">
						<el-image v-if="scope.row.image" :src="scope.row.image"  class="product-img"   ></el-image>
						<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
					 </template>
				</el-table-column>
				<el-table-column label="SKU" prop="sku" />
				<el-table-column label="产品名称" prop="name" />
				<el-table-column label="产品库存" prop="fulfillable" />
				<el-table-column label="采购数量" prop="needpurchase" >
					 <template #default="scope">
						 <el-input v-model="scope.row.needpurchase"  @input="scope.row.needpurchase=CheckInputInt(scope.row.needpurchase)"></el-input>
					 </template>	 
				</el-table-column>
			</el-table>
			<template #footer>
			  <span class="dialog-footer">
						   <div class="flex-center-between">
						 <div>
						 <el-button @click="consVisible = false">取消</el-button>
						 <el-button type="primary" @click.stop="addTabledata">
						   加入采购列表
						 </el-button></div></div>
			  </span>
			</template>
		</el-dialog>					  
	<MaterialDialog ref = "MaterialRef"  :warehouseid="form.warehouseid" @getdata="getRows" />
	<SupplierDialog ref="supplierRef" @getdata="getSupplierRows"  />
	<SupplierDialog ref="subsupplierRef" @getdata="getSubSupplierRows"  />
	</div>
	
	<!-- 采购预览弹窗 -->
	<el-dialog  v-model="formAlertVisible"
	   title="采购信息提示"
	   :width="alertWidth">
	   <div class="flex-star">
      <div class="ass-order-wrap " v-show="form.syncAssembly">
		  <h4 >生成了 {{assembiySkus.length}} 个加工单 </h4>
		  <div class="ass-item pointer" v-for="(item ,index) in assembiySkus" :key="index" @click="item.subshow=!item.subshow">
			  <div class="flex-between " style="margin-bottom:8px">
			  <el-space >
			  <el-image v-if="item.image" :src="item.image" class="product-img "></el-image>
			  <el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
			  <div>
				  <p>{{item.sku}}</p>
				   <el-tag type="warning" v-if="item.issfg=='1'"   effect="plain">组合</el-tag>
			  </div>
			  </el-space>
			  <div class="text-right">{{item.num||0}}
			  <p class="font-extraSmall">数量</p>
			  </div>
			  </div>
			  <div class="sub-item-box" v-if="item.subshow">
			  <el-space class="sub-item" v-for="(sub ,i) in item.subsku"  :key="i">
				  <el-space>
				  <el-image v-if="sub.image" :src="sub.image" class="product-img"></el-image>
				  <el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
				  <div>
					  <p>{{sub.sku}}</p>
					  <div class="font-extraSmall">x {{sub.num||0}}</div>
				  </div>
				  </el-space>
			  </el-space>
			  </div>
		  </div>
	  </div>
	  <div class="ass-order-wrap" style="max-width:300px;" v-show="sameSku.length>0">
		<h4 >有 {{sameSku.length}} 个相同的采购产品 </h4>
		  <div class="ass-item" v-for="(item ,index) in sameSku" :key="index">
		  			  <div class="flex-between ">
		  			  <el-space >
		  			  <el-image v-if="item.image" :src="item.image" class="product-img "></el-image>
		  			  <el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
					  <p>{{item.sku}}
					  <div class="font-extraSmall">
					   <div v-if="item.submap &&item.submap.newmoreqty" >剩余库存：{{item.submap.newmoreqty}}</div>
					  					  	<div v-else>剩余库存：0</div>
					  					  	<div class="font-extraSmall">=
					  					  	<span  v-if="item.submap && item.submap.amount" title="可用库存">
					  					  		 <span>{{item.submap.amount}}</span>	
					    </span> 
					    <span  v-else title="可用库存">
					    		 <span>0</span>	
					    </span> 
					    <span v-if="item.submap&&item.submap.inbound" title="待入库">
					  					  		<span > + {{item.submap.inbound}}</span>
					    </span> 
					  						  <span v-if="item.submap&&item.submap.needprocess" title="组装需求">
					  								 <span v-if="item.submap.needprocess"> - {{item.submap.needprocess}}</span>
					  						  </span> 
					  					  	</div>
					  					  	</div>
					  </p>
					  
		  			  </el-space>
		  			  <div class="text-right">{{item.num||0}}
		  			  <p class="font-extraSmall">数量</p>
					  <div v-if="item.reallynum!=item.num" class="font-extraSmall">
						  计划:{{item.reallynum}}
						  </div>
		  			  </div>
		  			  </div>
		  </div>
	  </div>
	   </div>
	   <template #footer>
	   	 <el-button @click="formAlertVisible = false">关闭</el-button>
	   	</template>
	   </el-dialog>	
</template>
<script setup>
	import {ArrowDown,Edit,Operation,ArrowDownBold } from '@element-plus/icons-vue'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import '@/assets/css/packbox_table.css'
	import { ref,reactive,onMounted,watch,inject,toRefs, computed } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus'
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	import SupplierDialog from "@/views/erp/baseinfo/supplier/supplier_dialog.vue"
	import { useRoute,useRouter } from 'vue-router';
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import {CheckInputFloat,CheckInputInt,formatFloat,outputmoney} from '@/utils/index.js';
	import Warehouse from '@/components/header/warehouse.vue';
	import GroupSelect from '@/components/header/group_select.vue';
	import OwnerAll from '@/components/header/ownerAll.vue';
	import {getSubForm } from '@/api/erp/assembly/assemblyApi.js';
	import purchaseEditApi from '@/api/erp/purchase/form/editApi.js';
	import {removeBatchItem,getBatchListItem} from '@/api/erp/purchase/plan/planApi.js';
	import materialApi from '@/api/erp/material/materialApi.js';
	import consumableApi from '@/api/erp/purchase/plan/consumableApi.js';
	const router = useRouter();
	const conplanwarehouseid=router.currentRoute.value.query.conplanwarehouseid;
	const planid = router.currentRoute.value.query.planid;
	const batchnumber = router.currentRoute.value.query.batchnumber;
	const planwarehouseid = router.currentRoute.value.query.warehouseid;
	const plangroupid = router.currentRoute.value.query.groupid? router.currentRoute.value.query.groupid:"all";
	const materialid = router.currentRoute.value.query.materialid;
	const materialList = router.currentRoute.value.query.materialList;
	const MaterialRef = ref();
	const supplierRef = ref();
	const subsupplierRef = ref();
	const groupRef=ref();
	const emitter = inject("emitter"); // Inject `emitter`
	const emit = defineEmits(['close','confirm']);
	
	let props = defineProps({isdialog:""});
	const {isdialog} = toRefs(props);
	const state = reactive({
		tableLoading:false,
		alertWidth:"600px",
		formAlertVisible:false,
		isShowAlert:false,
		tableData:[],
		assembiySkus:[],
		sameSku:[],
		nowRow:{},
		colspan:8,
		submitloading:false,
		form:{
			syncAssembly:true,
			name:null,
			ordertype:'more',
			number:'',
			remark:'',
			warehouseid:null,
			owner:null,
			supplierid:null,
			groupid:null,
		},
		orderitemlist:[],
		skulist:[],
		assemblylist:[],
		uploadVisible:false,
		consVisible:false,
		myfile:null,
		constableData:[],
	
	})
	const {
		form,
		isShowAlert,
		alertWidth,
		formAlertVisible,
		assembiySkus,
		submitloading,
		sameSku,
		tableData,
		nowRow,
		colspan,
		assemblylist,
		skulist,
		orderitemlist,
		uploadVisible,
		myfile,
		consVisible,
		constableData,
		tableLoading,
	}=toRefs(state)
		function closeTab(){
			if(props.isdialog){
				emit("close");
			}else{
					emitter.emit("removeTab", 100);
			}
		
		};	
    function handleAdd(){
		if(state.form.ordertype=='normal'){
			if(state.form.supplierid){
				MaterialRef.value.show({'supplier':state.form.supplierid});
			}else{
				ElMessage.error( "请先选择供应商！ ")
			}
		}else{
			MaterialRef.value.show();
		}
	}
	
	function handleBlur(e,row){
		// const evt = e || window.e || window.event;
		// var input=$(evt.currentTarget).find("input");
		// if(input){
		// 	input.val(row.amount);
		// }
	}
	function selectSupplier(){
		supplierRef.value.show();
	}
	function getSupplierRows(row){
		var name=row.name;
		state.form.name=name;
		state.form.supplierid=row.id;
	}
	function selectSubSupplier(row){
		state.nowRow=row;
		subsupplierRef.value.show(row.id);
	}
	function getSubSupplierRows(row){
		var name=row.name;
		var materialid="";
		state.nowRow.supplier=name;
		state.nowRow.supplierid=row.id;
		if(state.nowRow.submid){
			materialid=state.nowRow.submid;
		}else{
			materialid=state.nowRow.id;
		}
		loadStepPrice(row.id,materialid,state.nowRow.num,state.nowRow);
	}
	async function refreshSub(){
		if(state.tableData && state.tableData.length>0){
			await	state.tableData.forEach(async function(item){
						if(item.issfg=="1"){
							await getSubForm({"materialid":item.id,"warehouseid":state.form.warehouseid}).then((res)=>{
								if(res.data.sublist){
									 item.subsku=res.data.sublist;
								}else{
									ElMessage.warning( item.sku+"的子产品列表暂无匹配记录！");
								}
							});
						}
				});
		 var timer=setTimeout(function(){
			loadtotalPrice();
			clearTimeout(timer);
		 },500);
			 
		}
		 
	}
	async function getRows(rows){
		if(rows && rows.length>0){
			    var skus="";
				state.tableData.forEach(function(datas){
					 skus+=(datas.sku)+(",");
				});
			await	rows.forEach(async function(item){
					if(skus.indexOf((item.sku+","))<0){
						if(item.issfg=="1"){
							item.checkdsub=true;
							await getSubForm({"materialid":item.id,"warehouseid":state.form.warehouseid}).then((res)=>{
								if(res.data.sublist){
									 item.subsku=res.data.sublist;
								}else{
									ElMessage.warning( item.sku+"的子产品列表暂无匹配记录！");
								}
							});
						}
						state.tableData.push(item);
						checkFormAlert();
					}
				});
			await loadPrice();
			var timer=setTimeout(function(){
			   loadtotalPrice("refresh");
			   clearTimeout(timer);
			},500);
		}
		 
	}
	 async function loadPrice(){
		await state.tableData.forEach(async function(item){
			var supplierid="";
			if(state.form.ordertype=="normal"){
				supplierid=state.form.supplierid;
			}
			if(item.checkdsub==true && item.issfg=="1" && item.subsku && item.subsku.length>0){
				//组装产品且勾选了
				item.subsku.forEach(async function(sub){
					await purchaseEditApi.getPriceBySupplier({"supplierid":sub.supplierid,"materialid":item.submid,"amount":1}).then((res)=>{
						if(res.data){
							sub.subprice=res.data.itemprice;
						}
					});
				});
			} 
			await purchaseEditApi.getPriceBySupplier({"supplierid":supplierid,"materialid":item.id,"amount":1}).then((res)=>{
				if(res.data){
					item.price=res.data.itemprice;
				}
			});
		});
	}
	function checkFormAlert(){
		var arr = []
		state.assembiySkus = state.tableData.filter(item=>item.issfg==='1');
		if(state.form.syncAssembly){
			if(state.assembiySkus.length>0){
				state.isShowAlert = true;
			}
		}
		//收集相同的sku
		if(state.assembiySkus.length>0){
			var allsku = getsameSku(state.tableData);
			var result = [];
              result =Object.values(allsku.reduce((acc,item)=>{
				   if(!acc[item.id]){
						   acc[item.id]=[];
				   }
				   acc[item.id].push(item);
				   return acc;
			   },{})).filter(item=>item.length>1).flat(); 
			   state.sameSku = Object.values(result.reduce((acc,curr)=>{
				   if(acc[curr.id]){
					   acc[curr.id].num += curr.num;
					   acc[curr.id].reallynum += curr.reallynum;
				   }else{
					  acc[curr.id] = {...curr}; 
				   }
				   return acc
			   },{}));
		}
	}
     function getsameSku(arr){
		 var result=[];
		 arr.forEach(item=>{
			 result.push(item);
			 if(item.subsku&& Array.isArray(item.subsku)){
			 result = result.concat(getsameSku(item.subsku))
			 }
		 })
		return result
	 }
	
	function handleToMaterial(materialid){
		 router.push({
			path:'/material/details',
			query:{
			  title:"产品详情",
			  path:'/material/details',
			  details:materialid,
			}
		 })
	}
	async function loadStepPrice(supplierid,materialid,amount,row){
		row.warningText="";
		if(amount>0){
			if(row.boxnum&&amount%parseInt(row.boxnum)>0){
					row.warningText=row.warningText+"当前采购量不满足整箱个数："+row.boxnum;
			}
			await purchaseEditApi.getPriceBySupplier({"supplierid":supplierid,"materialid":materialid,"amount":amount}).then((res)=>{
				if(res.data){
					var needamount=res.data.needamount;
					if(amount<needamount){
						if(row.warningText){
							row.warningText=row.warningText+"<br/>当前供应商下最低起订量是："+needamount;
						}else{
							row.warningText="当前供应商下最低起订量是："+needamount;
						}
						
					} 
					if(row.subprice){
						if(res.data.itemprice && res.data.itemprice>0){
							row.subprice=res.data.itemprice;
						}else{
							row.subprice=res.data.price;
						}
						if(row.num && row.num>0 && row.subprice && row.subprice>0){
							row.totalprice=formatFloat(parseFloat(row.num)*parseFloat(row.subprice));
						}else{
							row.totalprice=0;
						}
					}else{
						if(res.data.itemprice && res.data.itemprice>0){
							row.price=res.data.itemprice;
						}else{
							row.price=res.data.price;
						}
						if(row.num && row.num>0 && row.price && row.price>0){
							row.totalprice=formatFloat(parseFloat(row.num)*parseFloat(row.price));
						}else{
							row.totalprice=0;
						}
					}
				}else{
					if(row.num&&row.price){
						row.totalprice=formatFloat(parseFloat(row.num)*parseFloat(row.price));
					}else if(row.num&&row.subprice){
						row.totalprice=formatFloat(parseFloat(row.num)*parseFloat(row.subprice));
					}else{
						row.totalprice=0;
					 }
					
					
				}
			});
		}else{
			row.totalprice=0;
		}
	}
	function getWarehouse(wid){
		state.form.warehouseid=wid;
		refreshSub();
		
	}
	function changeOwner(id){
		state.form.owner=id;
	}
	function changeSubNum(row){
		row.num=CheckInputInt(row.num);
		var supplierid="";
		if(state.form.ordertype=="normal"){
			supplierid=state.form.supplierid;
		}else{
			supplierid=row.supplierid;
		}
		//拿取新的采购价格
		loadStepPrice(supplierid,row.submid,row.num,row);
	}
	function changeSubPrice(row){
		row.subprice=CheckInputFloat(row.subprice);
		if(row.num && row.num>0 && row.subprice && row.subprice>0){
			row.totalprice=formatFloat(row.num*parseFloat(row.subprice));
		}else{
			row.totalprice=0;
		}
	}
	function changeSubTotalPrice(row){
		row.totalprice=CheckInputFloat(row.totalprice);
		if(row.num && row.num>0 && row.totalprice && row.totalprice>0){
			row.subprice=formatFloat(parseFloat(row.totalprice)/row.num);
		}else{
			row.subprice=0;
		}
	}
	function changeNum(row){
		row.num=CheckInputInt(row.num);
		var materialid="";
		var supplierid="";
		if(state.form.ordertype=="normal"){
			supplierid=state.form.supplierid;
		}else{
			supplierid=row.supplierid;
		}
		//拿取新的采购价格
		if(row.materialid){
			materialid=row.materialid;
		}else{
			materialid=row.id;
		}
		loadStepPrice(supplierid,materialid,row.num,row);
	}
	function changePrice(row){
		row.price=CheckInputFloat(row.price);
		if(row.num && row.num>0 && row.price && row.price>0){
			row.totalprice=formatFloat(row.num*parseFloat(row.price));
		}else{
			row.totalprice=0;
		}
	}
	function changeTotalPrice(row){
		row.totalprice=CheckInputFloat(row.totalprice);
		if(row.num && row.num>0 && row.totalprice && row.totalprice>0){
			row.price=formatFloat(parseFloat(row.totalprice)/row.num);
		}else{
			row.price=0;
		}
	}
	function changesubtotal(row){
		row.num=CheckInputInt(row.num);
		var supplierid="";
		if(state.form.ordertype=="normal"){
			supplierid=state.form.supplierid;
		}
		//级联修改sub的数值
		if(row.subsku && row.subsku.length>0 && row.num>0){
		 row.subsku.forEach(async function(item){
				if(item.submap ){
				    item.num=row.num*item.subnumber;
				}else{
					item.num=row.num*item.subnumber;
				}
				//拿取新的采购价格
				if(state.form.ordertype=="more"){
					supplierid=item.supplierid;
				}
				var newprice=await loadStepPrice(supplierid,item.submid,item.num,item);
			});
			var timer=setTimeout(function(){
			   loadtotalPrice("refresh");
			   clearTimeout(timer);
			},500);
		}
		checkFormAlert();
	}
	function changeCheckSub(item){
		if(item.checkdsub==false){
			ElMessageBox.confirm(
				'请确认您要直接购买主产品吗？',
				{
				  confirmButtonText: '确认',
				  cancelButtonText: '取消',
				  type: 'warning',
				  callback:(action)=>{
					 if(action=="confirm"){
						 item.checkdsub=false;
					 }else{
						 item.checkdsub=true;
					 }
				  }
				}
			 )
			
		}else{
			item.checkdsub=true;
			if(item.subsku==null || item.subsku==undefined || item.subsku=="" || item.subsku=="undefined"){
				getSubForm({"materialid":item.id,"warehouseid":state.form.warehouseid}).then((res)=>{
					if(res.data.sublist){
						 item.subsku=res.data.sublist;
					}else{
						ElMessage.warning( item.sku+"的子产品列表暂无匹配记录！");
					}
				});
			} 
		}
	}
	function changeGroup(val){
		state.form.groupid=val;
	}
	function changOrderType(){
		if(state.form.ordertype=="normal"){
			state.colspan=7;
			state.tableData=[];
		}else{
			state.colspan=8;
		}
		
	}
	function handleDelete(index){
		var item=state.tableData[index];
		state.tableData.splice(index,1);
		checkFormAlert();
	}
	function handleSubDelete(sublist,item,index){
		if(sublist && sublist.length>1){
			sublist.splice(index,1);
		}else{
			ElMessage.error("最后一个子SKU不支持删除！ ");
		}
	}
	function getTableData() {
		var flag = true;
		state.orderitemlist = [];
		state.skulist = [];
		state.assemblylist=[];
		state.tableData.forEach(function(item){
			if(item.checkdsub==true && item.subsku && item.subsku.length>0){
				//把主SKU加入到组装清单
				if(state.form.syncAssembly==true){
					state.assemblylist.push(JSON.stringify({"materialid":item.id,"quantity":item.num}));
				}
				//把子SKU加入到采购清单
				item.subsku.forEach(function(sub){
					var mitem = {};
					var supplierid="";
					if(state.form.ordertype=="normal"){
						supplierid=state.form.supplierid;
					}else{
						supplierid=sub.supplierid;
					}
					mitem.materialid =sub.submid;
					mitem.planitemid = null;
					mitem.supplier = supplierid;
					mitem.deliverydate = sub.deliverycycledate;
					mitem.amount = sub.num;
					mitem.itemprice = sub.subprice;
					mitem.orderprice = sub.totalprice;
					mitem.sku = sub.sku;
					if (typeof (mitem.materialid) != "undefined" && mitem.materialid != "" && mitem.amount && mitem.amount != "") {
						state.orderitemlist.push(JSON.stringify(mitem));
					}
				});
			}else{
				var mitem = {};
				var supplierid="";
				if(state.form.ordertype=="normal"){
					supplierid=state.form.supplierid;
				}else{
					supplierid=item.supplierid;
				}
				mitem.materialid =item.id;
				mitem.planitemid = null;
				mitem.supplier = supplierid;
				mitem.deliverydate = item.deliverycycledate;
				mitem.amount = item.num;
				mitem.itemprice = item.price;
				mitem.orderprice = item.totalprice;
				mitem.sku = item.sku;
				if (typeof (mitem.materialid) != "undefined" && mitem.amount != "" && state.skulist[mitem.materialid]) {
					ElMessage.error("不能重复添加SKU[" + mitem.sku + "]");
					flag = false;
				}
				if (typeof (mitem.materialid) != "undefined" && mitem.materialid != "" && mitem.amount && mitem.amount != "") {
					state.skulist[mitem.materialid] = mitem.amount;
					state.orderitemlist.push(JSON.stringify(mitem));
				}
			}
			
		});
		return flag;
	}
	function submitForm(ftype){
		state.submitloading=true;
		var isok=getTableData();
		if(isok==true){
			var data={};
			data.ftype="more";
			data.warehouseid=state.form.warehouseid;
			data.remark=state.form.remark;
			data.purchaser=state.form.owner;
			data.groupid=state.form.groupid;
			data.orderitemlist=state.orderitemlist.toString();
			data.assList=state.assemblylist.toString();
			purchaseEditApi.saveData(data).then((res)=>{
				state.submitloading=false;
				 if(res.data){
					 var message=res.data.msg;
					 if(planid && batchnumber){
						  removeBatchItem(batchnumber).then((res)=>{
							  ElMessage.success( message);
						  });
					 }else if(conplanwarehouseid){
						 consumableApi.clear({"warehouseid":conplanwarehouseid}).then((res)=>{
							ElMessage.success( message);				  
						 });
					 }else{
						 ElMessage.success( message);
					 }
					  if(props.isdialog){
					  	emit("confirm");
					  }else{
					  		router.push({
					  			path:"/erp/purchase/orders",
					  			query:{
					  				title:'采购单',
					  				path:"/erp/purchase/orders",
					  				replace:true,
					  				refresh:true,
					  			},
					  		})
					  }
					 
				 }else{
					 ElMessage.error( "保存失败");
					 state.submitloading=false;
				 }
			}).catch(e=>{
				state.submitloading=false;
			});
		}else{
			state.submitloading=false;
		}
	}
	function uploadExcel(){
		let FormDatas = new FormData();
		FormDatas.append('file',state.myfile);
		purchaseEditApi.uploadExcel(FormDatas).then(function(res){
			if(res.data.msg=="上传成功"){
				state.uploadVisible=false;
				if(res.data.result && res.data.result.length>0){
					state.tableData=res.data.result;
				}
				ElMessage.success('上传成功!');
			}else{
				ElMessage.error('上传失败！');
			}
		})
	}
	//文件上传之前
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 50000; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error( '上传文件大小不能超过 50MB!!');
				return false;
			}
			else {
			   return true;
			}
		}
	}
	function uploadFiles(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	function downloadTemp(){
		purchaseEditApi.downExcelTemp();
	}
	function showUploadFile(){
		state.uploadVisible = true; 
	}
	function showConsModal(){
		state.constableData=[];
		var isok=getTableData();
		if(isok==true && state.orderitemlist && state.orderitemlist.length>0){
			var consItemList=state.orderitemlist;
			 state.tableData.forEach(item=>{
				 if(item.subsku && item.subsku.length>0 && item.checkdsub==true){
					 consItemList.push(JSON.stringify({"materialid":item.id,"supplier":item.supplierid,"deliverydate":item.deliverycycledate,
					 "amount":parseInt(item.num),"sku":item.sku,"itemprice":item.price}));
				 }
			 });
			state.consVisible = true;
			purchaseEditApi.getConsumableByMainSKU({"skulist":consItemList.toString(),"warehouseid":state.form.warehouseid}).then((res)=>{
				 if(res.data){
					 state.constableData=res.data;
				 }
			});
		}else{
			ElMessage.error( '请确认采购列表数量是否填写正常!');
		}
		
	}
	//添加耗材至采购列表
	function addTabledata(){
		var addcount=0;
		var skus="";
		if(state.tableData && state.tableData.length>0){
			state.tableData.forEach(function(datas){
				 skus+=(datas.sku)+(",");
			});
        }
		state.constableData.forEach(function(item){
			if(item.needpurchase!="" && item.needpurchase!=undefined && item.needpurchase>0){
				var myrow=item.material;
				myrow.num=item.needpurchase;
				myrow.amount=item.needpurchase;
				myrow.image=item.image;
				if(item.supplier){
					myrow.supplierid=item.supplier.supplier;
					myrow.supplier=item.supplier.name;
				}
				if(myrow.price){
					myrow.totalprice=formatFloat(myrow.num*parseFloat(myrow.price));
				}
				myrow.effectivedate=null;
				if(skus.indexOf((item.sku+","))<0){
					 state.tableData.push(myrow);
					 addcount++;
				}
				
			}
		});
		if(addcount>0){
			state.consVisible=false;
		}
	}
	const summary=computed(()=>{
		     var data={"skunum":0,"totalprice":0};
			if(state.tableData && state.tableData.length>0){
		 	state.tableData.forEach(function(row){
					if(row.issfg=="1" && row.checkdsub==true){
						if(row.subsku && row.subsku.length>0){
							row.subsku.forEach(function(sub){
								if(sub.totalprice && sub.totalprice>0){
									data.totalprice=parseFloat(data.totalprice)+parseFloat(sub.totalprice);
								}
								data.skunum++;
							});
						}
					}else {
							data.skunum++;
							if(row.totalprice && row.totalprice>0){
								data.totalprice=parseFloat(data.totalprice)+parseFloat(row.totalprice);
							}
					}
			 
			});
		}
		return data;
	})
	function loadtotalPrice(type){
		if(state.tableData && state.tableData.length>0){
			var moreqty={};
			state.tableData.forEach(function(row){
				if(row.num && row.num>0){
					if(row.issfg=="1" && row.checkdsub==true){
						if(row.subsku && row.subsku.length>0){
							row.subsku.forEach(function(sub){
								if(sub.submap&&sub.submap.moreqty){
									if(moreqty[sub.sku]!=undefined){
										 sub.submap.newmoreqty=moreqty[sub.sku];
									}else{
										 moreqty[sub.sku]=sub.submap.moreqty
										 sub.submap.newmoreqty=sub.submap.moreqty;
									}
								    if(row.num*sub.subnumber<sub.submap.newmoreqty){
										sub.num=0;
										moreqty[sub.sku]=parseInt(sub.submap.newmoreqty)-parseInt(row.num)*parseInt(sub.subnumber);
									}else{
										sub.num=parseInt(row.num)*parseInt(sub.subnumber)-parseInt(sub.submap.newmoreqty);
										moreqty[sub.sku]=0;
									
									}
								}else{
									sub.num=parseInt(row.num)*sub.subnumber;
								}
								sub.reallynum=row.num*sub.subnumber;
								sub.totalprice=formatFloat(parseFloat(sub.subprice)*parseFloat(sub.num));
								if(type!="refresh"){
								loadStepPrice(sub.supplierid,sub.submid,sub.num,sub)
								}
							});
						}
					}else if(row.price && row.price>0){
					    row.totalprice=formatFloat(parseFloat(row.num)*parseFloat(row.price));
					}
				}
			});
		}
	}
	function loadMaterialData(){
		if(materialList){
			materialApi.getMaterialList({"materialList":materialList}).then((res) => {
					if(res.data){
						getRows(res.data.records); 
					}
			})
		}else if(materialid){
			materialApi.getMaterialList({"materialid":materialid}).then((res) => {
					if(res.data){
						getRows(res.data.records); 
					}
			})
		}
		state.form.warehouseid=planwarehouseid;
	}
	function loadPlanData(){
		state.tableLoading=true;
		getBatchListItem(planid,batchnumber).then((res)=>{
			state.tableLoading=false;
			if(res.data && res.data.length>0){
				state.form.warehouseid=planwarehouseid;
				state.form.groupid=plangroupid;
				state.tableData=res.data;
				if(state.tableData){
						state.tableData.forEach(item=>{
							item.id=item.mid;
							item.name=item.mname;
							loadStepPrice(item.supplierid,item.id,item.amount,item);
						})
				  }
				var timer=setTimeout(function(){
				   loadtotalPrice();
				   checkFormAlert();
				   clearTimeout(timer);
				},500);
			}else{
				ElMessage.error(  "暂无计划匹配的记录！" );
			}
		});
	}
	function loadConsPlanData(){
		consumableApi.list({"warehouseid":conplanwarehouseid}).then((res)=>{
			if(res.data && res.data.length>0){
				state.form.warehouseid=conplanwarehouseid;
				state.tableData=res.data;
				if(state.tableData){
					state.tableData.forEach(item=>{
						item.id=item.mid;
						item.name=item.mname;
					    loadStepPrice(item.supplierid,item.id,item.amount,item);
					})
				}
				setTimeout(function(){
				   loadtotalPrice();
				},500);
			}else{
				ElMessage.error( "暂无计划匹配的记录！");
			}
		});
	}
	function load(){
		serialnumberApi.getSerialNumber({"ftype":"PO","isfind":"true"}).then((res)=>{
			if(res.data){
				state.form.number=res.data;
			}
		});
		if(planid){
			loadPlanData();
		}
		if(materialid||materialList){
			loadMaterialData();	
		}
		if(conplanwarehouseid){
			loadConsPlanData();
		}
	}
	function initData(form){
			state.form.warehouseid=form.warehouseid;
			getRows(form.list);
	}
	onMounted(()=>{
		load();
	});
	defineExpose({ initData });
	function HandlePreview(){
		state.formAlertVisible = true
		if(state.sameSku.length>0){
			state.alertWidth = 800;
		}
	}

</script>

<style scoped="scoped">
  .flex-star{
	  display: flex;
  }
  .ass-order-wrap{
	  flex:1;
  }
	.ass-order-wrap .ass-item{
		margin-top: 8px;
		margin-right: 16px;
		padding:16px;
		border:1px solid #eee;
		border-radius: 4px;
		
	}
	.sub-item-box{
		background-color: #f8f8f8;
	}
	.ass-order-wrap .sub-item{
		padding:8px;
	}
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
		.mark-re{
			margin-top: 16px;
			margin-bottom:16px;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	.mark-re h5::before{
		content: "";
		display: inline-block;
		height: 13px;
		width: 4px;
		margin-right:8px;
		background-color: #FF6700;
	}
	.fill-from-body{
		margin:16px 24px;
	}
	.subtable {
		padding:0!important;
	}
	.subtable .el-table{
		background-color: var(--el-bg-color)!important;
	}
	.overflow-text{
		width: 200px;
		   overflow: hidden;
		    display: -webkit-box;
		    -webkit-box-orient: vertical;
		    -webkit-line-clamp:1;
	}
	.self-alert-blue{
		cursor:pointer;
		font-size: 14px;
		padding:8px;
		border-radius: 4px;
		background-color: #f0f8ff;
		color:#3c8bfa;
		margin-bottom:8px;
	}
</style>
<style>
	.subtable td{
		background-color: var(--el-bg-color)!important;
	}
</style>
