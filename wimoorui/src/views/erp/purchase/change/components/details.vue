<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>换货单详情</h3>
						 
						 <div>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 </div>
						 <div class="fill-from-body" >
							<el-descriptions  :column="2"   width="150px" border :style="blockMargin">
							    <el-descriptions-item label="单据编码"   label-align="left" align="left">{{changeform.number}}</el-descriptions-item>
								<el-descriptions-item label="采购单编码" label-align="left" align="left">{{pnumber}}
								<el-icon  @click="selectPurchase"><Setting/> </el-icon>
								</el-descriptions-item>
							    <el-descriptions-item label="操作仓库"  label-align="left" align="left">{{warehouse.name}}
								  <el-tag   effect="plain"  round v-if="changeform.withoutInv">不锁定库存</el-tag>
								  <el-tag   effect="plain" round v-else="changeform.withoutInv">锁定库存</el-tag>
								</el-descriptions-item>
							    <el-descriptions-item label="供应商"    label-align="left" align="left">
									<span v-if="supplier&&supplier.name" >{{supplier.name}}</span>
									<span v-else-if="suggestsupplier&&suggestsupplier.name" class="font-extraSmall">
									                  {{suggestsupplier.name}}(产品默认)
									</span>
									<el-icon v-if="changeform.auditstatus==1" @click="selectSupplier"><Setting/> </el-icon>
								</el-descriptions-item>
								<el-descriptions-item label="创建人"  label-align="left" align="left">{{changeform.creator}}</el-descriptions-item>
								<el-descriptions-item label="状态"    label-align="left" align="left">
									<el-tag v-if="changeform.auditstatus==1" type="warning" >采购待确认</el-tag>
									<el-tag v-if="changeform.auditstatus==2" type="danger" >仓库待收货</el-tag>
									<el-tag v-if="changeform.auditstatus==3" type="success" >已完成</el-tag>
									<el-tag v-if="changeform.auditstatus==0" type="info" >已取消</el-tag>
								</el-descriptions-item>
								<el-descriptions-item label="创建时间" label-align="left" align="left">{{dateTimesFormat(changeform.createtime)}}</el-descriptions-item>
								<el-descriptions-item label="物流追踪编号"  label-align="left" align="left">
								<el-space> 
								         <div v-if="changeform.editLogistics">
											   <el-space>
											   <el-input placeholder="物流编号" v-model="logistics"></el-input>
											   <el-button @click="changeForm" type="success" :icon="Select"></el-button>
											   <el-button @click="changeform.editLogistics=false" :icon="CloseBold">  </el-button>
											   </el-space>
										   </div>
								         <div v-else >
												<el-space>
											   <div class="font-extraSmall">{{changeform.logistics}}</div>
											   <el-icon v-if="changeform.auditstatus==1||changeform.auditstatus==2" 
											   @click="changeform.editLogistics=true"><Setting/> </el-icon>
											   </el-space>
										   </div>
								</el-space>
								</el-descriptions-item>
								<el-descriptions-item v-if="assform && assform.number" label="生成组装单编码"  label-align="left" align="left">
									<el-link type="primary" @click="handleDetail(assform)">{{assform.number}}</el-link> 
								</el-descriptions-item>
								<el-descriptions-item v-if="disform && disform.number" label="生成拆分单编码"  label-align="left" align="left">
									<el-link type="primary" @click="handleDetail(disform)">{{disform.number}}</el-link> 
								</el-descriptions-item>
								<el-descriptions-item v-if="mainMaterial && mainMaterial.sku" label="组装主SKU"  label-align="left" align="left">
									{{mainMaterial.sku}}
								</el-descriptions-item>
								<el-descriptions-item label="产品入库" label-align="left" align="left" v-if="changeform.auditstatus==2||changeform.auditstatus==3">
								 <el-card   shadow="never" style="border:0px;width:80%">
								 <el-row class="m-b-24">
								 <el-col :span="12" class="text-center">
								 	<div >
								 	<p class="font-bold">{{changeform.totalin}}</p>
								 	<span class="font-extraSmall">已入库</span>
								 	</div>
								 </el-col>
								 <el-col :span="12" class="text-center">
								 								  
								 	<el-popover
								 	   placement="bottom"
								 	   title="产品入库"
								 	   :width="200"
								 	   trigger="click"
								 	 >
								 	  <el-input-number v-model="stockAmount" :min="1" :step="1" :precision="0" placeholder="数量" />
								 		<el-button  @click="handleInstock"
								 		 class="m-t-8"
								 		 type="primary">确认</el-button>
								 	   <template #reference>
								 	  <el-button v-if="changeform.auditstatus==2" size="large" type="primary" class="m-t-8" >入库</el-button>
								 	   </template>
								 	 </el-popover>
								 		  <el-button v-if="changeform.auditstatus==1 || changeform.auditstatus==2" size="large" @click="confrimSave"   class="m-t-8" >终止</el-button>
								 </el-col>
								 </el-row> 
								    <el-progress
								 	 :text-inside="true"
								 	      :stroke-width="8"
								 	      :percentage="(changeform.totalin/changeform.amount)*100"
								 	     >
								        <span></span>
								     </el-progress>
								 	<div class="flex-center-between m-t-8">
								 		<span class="font-extraSmall">处理进度</span>
								 		<span class="font-extraSmall">{{changeform.totalin+"/"+changeform.amount}}</span>
								 	</div>
								  </el-card>
								</el-descriptions-item>
								<el-descriptions-item label="备注" label-align="left"  align="left">
								 <div v-if="changeform.editRemark">
									 <el-space>
									 <el-input    style="width: 500px"  v-model="changeform.remarkinput" :rows="3" type="textarea"></el-input>
									 <div>
										 <el-button  @click="changeRemark(changeform)" type="success" :icon="Select"></el-button><br/>
									     <el-button style="margin-top:5px"   @click="changeform.editRemark=false" :icon="CloseBold">  </el-button>
									 </div>
									 
									 </el-space>
								 </div>
								 <span v-else>{{changeform.remark}}
								 <el-icon style="margin-left:20px;margin-top:5px" 
								      v-if="changeform.auditstatus==1||changeform.auditstatus==2" 
								      @click="changeform.editRemark=true;changeform.remarkinput=changeform.remark">
								     <Edit/>
								 </el-icon>
								 </span>
								
								</el-descriptions-item>
								<el-descriptions-item label="附件" label-align="left"   align="left">
									 <el-upload
									    v-model:file-list="attachments"
										 action
										 :http-request="uploadFiles"
										 :limit="10"
										 :before-upload="beforeUpload" 
										  accept=".jpg,.png,.jpeg,.bmp,.gif"
									    list-type="picture-card"
									    :on-preview="handlePictureCardPreview"
									    :on-remove="handleRemove"
									  >
									    <el-icon><Plus /></el-icon>
									  </el-upload>
									  <el-dialog v-model="dialogVisible">
									    <img w-full :src="dialogImageUrl" alt="Preview Image" />
									  </el-dialog>
								</el-descriptions-item>
							  </el-descriptions>
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >换货商品 </h5>
						   </div>
						 </div>
						 <el-table border :data="materialList">
							 <el-table-column  prop="image" label="图片"  width="70" >
							    <template #default="scope" >
							     <el-image v-if="scope.row.image" :src="scope.row.image"   class="product-img"  ></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"   class="product-img"   ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column  prop="product" label="名称/SKU"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.name}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column prop="name" label="换货数量" >
								 <template #default="scope">
								 <span>{{changeform.amount}}</span>
								 </template>
							 </el-table-column> 
							 <el-table-column prop="name" label="可用库存">
							 	 <template #default="scope">
							 		 <span v-if="inventory && inventory.fulfillable">{{inventory.fulfillable}}</span>
									  <span v-else>0</span>
							 	 </template>
							 </el-table-column>
								 <el-table-column prop="name" label="待入库">
									<template #default="scope">
										<span v-if="inventory && inventory.inbound">{{inventory.inbound}}</span>
										<span v-else>0</span>
									 </template>
								</el-table-column>	
						 </el-table>
						 <div v-if="changeform.auditstatus==2||changeform.auditstatus==3">
						 <el-divider></el-divider>
						 <div class="mark-re">
						 	<div>
						       <h5 >入库记录 </h5>
						    </div>
						 </div>
						 <el-table border :data="recordList">
							 <el-table-column label="操作时间" prop="opttime" >
								   <template #default="scope">
									{{dateTimesFormat(scope.row.opttime)}}
									</template>
							 </el-table-column>
							 <el-table-column label="操作人"  prop="operator"/>
							 <el-table-column label="入库数量" prop="amount" />
							 <el-table-column label="备注" prop="remark" />
							 <el-table-column label="操作" >
								  <template #default="scope" >
									<el-button v-if="scope.row.disable==0"  
									 link type="primary"  @click.stop="cancelStock(scope.row)">撤销入库</el-button>
									<span v-else  class="font-extraSmall" >已撤销入库</span>
								  </template>
							 </el-table-column> 
							
							 </el-table> 
							 </div>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16' >
							 <el-button type="primary" v-if="changeform.auditstatus==0"  @click="resetForm">重启单据</el-button>
							 <el-button type="primary" v-if="changeform.auditstatus==1"  @click="purchaseapplyForm">采购确认</el-button>
							 <el-button v-if="changeform.auditstatus==1"  @click="confrimSave">驳回终止</el-button>
							 <el-button type="" @click="closePage">关闭</el-button>
						</div>
						</div>
				<SupplierDialog ref='supDiaRef' @getdata="getSupplierRows" />
				<OrderDialog ref='orderDiaRef' @change="getChangeRow" />
				
	 		
</template>

<script setup>
	import {ArrowDown,Edit,Setting,CloseBold,Select} from '@element-plus/icons-vue';
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs,computed } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus';
	import OrderDialog from "@/views/erp/purchase/orders/order_dialog.vue";
	import SupplierDialog from "@/views/erp/baseinfo/supplier/supplier_dialog.vue";
	import { useRoute,useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import changeApi from '@/api/erp/purchase/change/changeApi.js';
	const MaterialRef = ref()
	const router = useRouter();
	const supDiaRef =ref();
	const orderDiaRef=ref();
	const id = router.currentRoute.value.query.id;
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	let headers=ref({"Content-Type": "multipart/form-data" });
	const state = reactive({
		changeform:{logistics:""},
		attachments:[],
		recordList:[],
		supplier:{},
		warehouse:{},
		logistics:"",
        suggestsupplier:{},
		materialList:[],
		inventory:{},
		stockAmount:null,
		pnumber:'',
		dialogImageUrl:"",
		dialogVisible:false,
		uploadVisible:false,
		assform:{},
		disform:{},
		mainMaterial:{},
	})
	const {
		changeform,
		recordList,
		supplier,
		suggestsupplier,
		logistics,
		warehouse,
		attachments,
		dialogImageUrl,
		materialList,
		inventory,
		stockAmount,
		dialogVisible,
		pnumber,
		uploadVisible,
		assform,
		disform,
		mainMaterial
	}=toRefs(state)
	function selectPurchase(){
		orderDiaRef.value.show();
	}
	const size = ref('default')
 
	function handlePictureCardPreview(uploadFile){
	  state.dialogImageUrl =uploadFile&&uploadFile.url? uploadFile.url:"";
	  state.dialogVisible = true;
	}
	function handleRemove(uploadFile, uploadFiles){
	 changeApi.deleteAttachment(uploadFile.id).then(res=>{
		 load();
	 });
	}

	function handleInstock(){
		if(state.stockAmount>0){
			changeApi.saveRecord({"remark":"","changeid":id,"amount":state.stockAmount}).then((res)=>{
				if(res.data){
					ElMessage.success("入库成功!");
					load();
				}
			});
		}
	}
	function getSupplierRows(row){
		changeApi.changeform({"id":id,"supplierid":row.id}).then((res)=>{
			if(res.data){
				changeform.editLogistics=false;
				ElMessage.success("修改成功!");
				 load();
			}
		});
		
	}
	function changeRemark(changeform){
		var form={};
		form.id=id;
		form.remark=changeform.remarkinput;
		changeApi.changeform(form).then((res)=>{
			if(res.data){
				ElMessage.success("修改成功!");
				 load();
			}
		});
	}
	function changeForm(){
		var form={};
		form.id=id;
		form.logistics=state.logistics;
		changeApi.changeform(form).then((res)=>{
			if(res.data){
				ElMessage.success("修改成功!");
				 load();
			}
		});
	}
	function getChangeRow(row){
		var form={};
		form.id=id;
		form.supplierid=row.supplier;
		form.entryid=row.id;
		changeApi.changeform(form).then((res)=>{
			if(res.data){
				ElMessage.success("修改成功!");
				 load();
			}
		});
	}
	function selectSupplier(){
		supDiaRef.value.show();
	}
	function resetForm(){
		changeApi.resetForm({"id":id}).then((res)=>{
			if(res.data){
				ElMessage.success( "单据重启成功!");
				load();
			}
		});
	}
	function purchaseapplyForm(){
		changeApi.purchaseapply({"id":id}).then(res=>{
			ElMessage.success( "操作成功!");
			load();
		});
	}
	function cancelStock(row){
		if(state.changeform.auditstatus==1){
			changeApi.cancelInstock({"receiveid":row.id}).then((res)=>{
				if(res.data){
					ElMessage.success( "撤销成功!");
					load();
				}
			});
		}else{
				ElMessage.error( "单据已完成,不支持撤销!");
		}
	}
	
	function load(){
		changeApi.getData({"id":id}).then((res)=>{
			if(res.data){
				state.changeform=res.data.changeform;
				state.attachments=res.data.attachments;
				state.supplier=res.data.supplier;
				state.suggestsupplier=res.data.suggestsupplier;
				state.warehouse=res.data.warehouse;
				state.recordList=res.data.recordList;
				state.materialList=res.data.material;
				state.inventory=res.data.inventory;
				state.pnumber=res.data.pnumber;
				if(res.data.assform){
					state.assform=res.data.assform;
					state.disform=res.data.disform;
				}
				state.mainMaterial=res.data.mainMaterial;
				if(state.attachments&&state.attachments.length>0){
					state.attachments.forEach(item=>{
						item.url=item.image;
					})
				}
			}
		});
	}
	
	function handleDetail(row){
		router.push({
				path:"/e/p/p/d",
				query:{
						title:'加工单详情',
						path:"/e/p/p/d",
						id:row.id
				},
		})
	}
	 
	function closePage(){
			var refreshvalue="";
			if(router.currentRoute.value.query.replace){
				refreshvalue=true;
			}
			router.push({
				path:"/erp/purchase/change",
				query:{
					title:'换货单',
					path:"/erp/purchase/change",
					replace:true,
					refresh:refreshvalue,
				},
			})
	}
	function confrimSave(){
		var titleStr="您确定要直接完成换货单吗？";
		var title="完成换货单";
		ElMessageBox.confirm(
					titleStr,
					title,
					{
					  confirmButtonText: '确定',
					  cancelButtonText: '取消',
					  type: 'warning',
					}
		 )
					.then(() => {
							changeApi.examine({"ids":id}).then((res)=>{
								  if(res.data){
									  ElMessage.success(res.data);
									  load();
								  }else{
									  ElMessage.error("操作失败");
								  }
							  });
					})
					.catch(() => {
					  ElMessage.info('取消操作');
					})
	}
	
	//文件上传之前
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error('上传文件大小不能超过 5MB!!');
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
		uploadExcel();
	}
	function uploadExcel(){
		let FormDatas = new FormData();
		FormDatas.append('file',state.myfile);
		FormDatas.append("entryid",id);
		changeApi.uploadAttachment(FormDatas).then(function(res){
			state.uploadVisible=false;
			load();
		})
	}
	
	function showUploadDialog(){
		state.uploadVisible=true;
	}
	
	onMounted(()=>{
		load();
	});
</script>

<style scoped>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 150px);
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
	.m-b-t-24{
		margin-bottom:24px;
		margin-top:24px;
	}
	.m-b-24{
		margin-bottom: 24px;
	}
	.m-b-16{
		margin-bottom: 16px;
	}
</style>
