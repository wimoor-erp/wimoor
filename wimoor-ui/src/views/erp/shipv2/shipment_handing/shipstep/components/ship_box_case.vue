<template>
	<div class="box-ship">
		<div class="con-header" v-if="!isview">
			<el-space :spacer="spacer" >
				<h4>箱子信息</h4>
				<el-space v-if="form.tranType=='LTL'"  class="font-small">
					<span>托盘个数:</span>
					 <el-input-number :controls="false"  :max="30" v-model.number="panNum"   />
				</el-space>
			</el-space>
		</div>
		<div class="con-body"  >
			<div style="width:100%;overflow-x: auto;margin-bottom:10px;">
			<el-table :data="planData.itemlist"
			   ref="tableRef"
			   >
				<el-table-column width="70" label="图片" >
					<template #default="scope">
						<el-image :src="scope.row.image" style="width: 40px;height: 40px;"  ></el-image>
					</template>
				</el-table-column>
				<el-table-column label="名称/SKU"  >
					<template #default="scope">
						<div class='name'>{{scope.row.name}}</div>
						<div class='sku'>{{scope.row.sku}}</div>
					</template>
				</el-table-column>
				 <el-table-column prop="quantity" label="货件数量" width="80" />
				 <el-table-column label="每箱装箱个数" width="120">
				 	<template #default="scope">
				 		 <el-input-number style="width:100%" :controls="false"  v-model="scope.row.num" :min="0" @change="changeTotalBoxNum()"/>
				 	</template>
				 </el-table-column>
				 <el-table-column label="箱子个数" width="120">
				 	<template #default="scope">
				 		 <el-input-number  style="width:100%" :controls="false"  v-model="scope.row.boxnum" :min="0" @change="changeTotalBoxNum()"/>
				 	</template>
				 </el-table-column>
				 <el-table-column label="箱规" width="480">
				 	<template #default="scope">
						<el-space>
				 		 <el-input :controls="false"  v-model="scope.row.boxlength" :min="0" >
							   <template #prefix>长:</template>
						 </el-input>
						  <el-input :controls="false"  v-model="scope.row.boxwidth" :min="0"  >
						        <template #prefix>宽:</template>
						  </el-input>
						  <el-input :controls="false"  v-model="scope.row.boxheight" :min="0"  >
						        <template #prefix>高:</template>
						  </el-input>
						   <el-input :controls="false"  v-model="scope.row.boxweight" :min="0"  >
							    <template #prefix>重:</template>
						   </el-input>
					   </el-space>
				 	</template>
				 </el-table-column>
			</el-table>
			</div>
			<el-row> 
			<el-col :span="20"  >
				<div class="rt-btn-group">
					<el-button     @click="downloadBoxInfo()">下载箱子详情</el-button>
					<el-button     @click="downExcelBox()">下载箱子信息</el-button>
				</div>
			</el-col>
				<el-col :span="4"  v-if="!isview">
					<div class="rt-btn-group">
						<el-button  class="rt-btn-group" :disabled="boxDisable" :loading="submitloading"   @click="saveBox('save')">保存装箱信息</el-button>
					</div>
				</el-col>
			</el-row>
		</div>
	
	  
</div>
	  
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,computed,nextTick,toRefs} from 'vue'
	import {Search,ArrowDown,Close} from '@element-plus/icons-vue'
	import { ElDivider,ElMessageBox ,ElMessage } from 'element-plus'
	import '@/assets/css/packbox_table.css'
	import materialApi from '@/api/erp/material/materialApi.js';
	import feedApi from '@/api/amazon/feed/feedApi.js';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
	import ShipmentOpt from"./shipment_operator.vue"
	import Operation from "@/views/erp/shipv2/shipment_add/shipstep/components/operation.vue";
	import {formatFloat,CheckInputIntLGZero,CheckInputFloat} from '@/utils/index.js';
	import {pointKeyChnage} from '@/utils/jquery/key/point-key';
	import boxMarks from '@/model/erp/ship/boxMarks.json';
   
    const emit = defineEmits(['stepdata',"carrData","change"]);
	
	let state =reactive({
		planData:{shipment:{}},
		transportation:[],
		deliveryWindowOptions:[],
		infoData:{shipment:{}},
		boxDetail:{},
		totalBoxNum:0,
		submitloading:false,
		carrierData:[],
		boxDisable:false,
		inputboxNum:"",
		loadingDeliverWindow:false,
		loadingTransportation:false,
		boxWeightData:[],
		titles:['箱子信息','配送信息生成','预计到货日期生成','提交预计到货日期'],
		tableData:[],
		boxListData:[{boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]}],
		form:{
			selectedTransportationOptionId:null,
			deliveryWindowOptionId:null,
			tranType:"SP",
		},
		boxed:false,
		tableloading:false,
	})
	let{planData,
		transportation,
		titles,
		deliveryWindowOptions,
		loadingDeliverWindow,
		loadingTransportation,
		infoData,
		boxDetail,
		totalBoxNum,
		submitloading,
		carrierData,
		boxDisable,
		inputboxNum,
		boxWeightData,
		tableData,
		boxListData,
		form,
		boxed,
		tableloading,
		}=toRefs(state);
	let props = defineProps({isview:undefined });
	const {isview} = toRefs(props);
	let spacer = h(ElDivider, { direction: 'vertical' })
	const operationRef=ref();
	let boxmarksType=ref("PackageLabel_Plain_Paper");
	let panNum=ref(1);
	
	
			 function cellTitle(sku,index){
				 return sku+"    箱号:"+(index+1)+"";
			 }
			 function inputBoxNumChange(){
				 state.inputboxNum=CheckInputIntLGZero(state.inputboxNum);
				 if(state.inputboxNum){
				    state.inputboxNum=parseInt(state.inputboxNum)
				 }
				 addBoxSize("init");
				pointKeyChnage(".sd-table");
			 }
			//确认装箱数量
			function subimtBoxNum(){
				state.tableData.forEach((item)=>{
					for(var i=0;i<state.inputboxNum ;i++){
						item['boxNum'+i] = undefined
						state.boxWeightData[i] = undefined
					}
				})
				state.boxed =true
			}
			function radioChange(val){
				radio1.value= val
			}
			 
			//箱子尺寸单选
			function checkboxChange(i,a){
				if(i==0&&a==0&&state.boxListData.length==1){
					state.boxListData[0].boxcheck=[];
					for(var ibox=0;ibox<state.inputboxNum ;ibox++){
						state.boxListData[0].boxcheck.push(true);
					}
				}else{
					state.boxListData.forEach((item,index)=>{
						if(a==index){
							item.boxcheck[i] = true
						}else{
							item.boxcheck[i] = false
						}
					})
				}
			}
			function rowBoxsumNum(row){
				let sum = 0
				for(var i=0;i<state.inputboxNum ;i++){
					if(row['boxNum'+i]){
				       sum +=row['boxNum'+i]
					}
				}
				return sum
			}
			//总计合计
			function getSummmary(index){
				let sumitem=0;
				if(index!=undefined){
					state.tableData.forEach((item)=>{
							if(item['boxNum'+index]!=undefined&&item['boxNum'+index]!=""){
								sumitem+=parseInt(item['boxNum'+index]);
							}
						});
				}else{
					state.tableData.forEach((item)=>{
						for(var i=0;i<state.inputboxNum ;i++){
							if(item['boxNum'+i]!=undefined&&item['boxNum'+i]!=""){
								sumitem+=parseInt(item['boxNum'+i]);
							}
							}
						});
				}
				return sumitem; 
			}
			//箱数立方尺寸求和
			function rowboxNumAndsize(row,index){
				let obj={}
				let boxnum = 0;
				let size =0
				if(row){
					row.boxcheck.forEach((item)=>{
						if(item == true){
							boxnum++;
						}
					})
					obj.boxnum = boxnum
					size = row.boxlength*row.boxwidth*row.boxheight*boxnum/1000000;
					obj.size = formatFloat(size);
				}
				return obj;
			} 
			 
			//计算属性
			let sumbsn = computed(()=>{
				let obj={}
				let boxnum = 0
				let size = 0
				state.boxListData.forEach((row)=>{
					let obj=rowboxNumAndsize(row);
					size=size+obj.size;
					boxnum=boxnum+obj.boxnum;
				})
				obj.boxnum = formatFloat(boxnum);
				obj.size =formatFloat(size) ;
				return obj
			})  
			
			let boxweightSum =   computed(()=>{
				let sumweight = 0;
				for(var i=0;i<state.boxWeightData.length;i++){
					if(state.boxWeightData[i]&&state.boxWeightData[i]!=""){
					   sumweight +=parseFloat(state.boxWeightData[i]);
					}
				}
				return formatFloat(sumweight);
			})  
				//竖向向装箱数求和
			state.tableData.colBoxsumNum = computed(()=>{
				const sumarr = [];
				state.tableData.forEach((item)=>{
					for(var i=0;i<state.inputboxNum ;i++){
						let sumitem=0;
						if(item['boxNum'+i]!=undefined){
							sumitem=item['boxNum'+i];
						}
						if(sumarr[i]){
							sumarr[i] +=sumitem;
						}else{
							sumarr[i] = sumitem;
						}
					}
				})
				
				return sumarr
			}) 
			//箱子重量
			//跳过箱子信息
			function jumpBoxinfo(){
				 ElMessageBox.alert('亚马逊可能收取处理费用', {
				    confirmButtonText: '确认',
					cancelButtonText: '取消',
					type: 'warning',
				  })
				  //货件状态3变成4
			}
			//装箱数量校验
			function packNumSum(row){
				let sum = 0
				for(var i=0;i<state.inputboxNum ;i++){
				    if(row['boxNum'+i]){
					sum +=parseInt(row['boxNum'+i]);
					}
				}
				row.sum=sum;
			}
			function packNumchange(row,index){
				var boxindex='boxNum'+index;
				if(row[boxindex]&&row[boxindex]!="0"){
				   row[boxindex]=CheckInputIntLGZero(row[boxindex]);
				}
				packNumSum(row);
			}
	 
			//添加箱子尺寸
			function addBoxSize(type){
				let obj = {boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]};
				if(type=="init"){
					state.boxListData=[];
					state.boxListData.push(obj);
				}else{
					state.boxListData.push(obj)
				}
				
			}
			//删除
			function delectBox(row){
				state.boxListData.splice(state.boxListData.indexOf(row), 1)
			}
 
			function getBoxDetial(){
				var params={};
				params.formid=state.planData.plan.id;
				params.shipmentid=state.planData.shipment.shipmentid;
				params.arecase="true";
				   state.boxDetail.listbox.forEach(item=>{
					   state.planData.itemlist.forEach(i=>{
						   if(item.sku==i.msku){
							   i.num=item.quantity;
							   i.boxnum=item.num;
							   i.boxlength=item.length;
							   i.boxheight=item.height;
							   i.boxwidth=item.width;
							   i.boxweight=item.weight;
						   }
					   })
				   })
			}
			
			function saveBox(opttype){
				var params={boxListDetail:[]};
				var lists=[];
				params.formid=state.planData.plan.id;
				params.boxnum=state.inputboxNum;
				params.opttype=opttype;
				params.shipmentid=state.planData.shipment.shipmentid;
				var isok=true;
				 state.tableData.list.forEach(item=>{
						if(parseInt(item.quantity)!=parseInt(item.num)*parseInt(item.boxnum)){
							isok=false;
						}
				  })
				if(isok==false){  ElMessage.error("装箱信息错误");  return; }
				state.btnloading=true;
				var opttype='save';
				if(type=='submit'){
					opttype='submit';
				}else{
					opttype='save';
				}
				var params={boxListDetail:[]};
				var lists=[];
				params.formid=state.planData.id;
				params.boxnum=state.inputboxNum;
				params.opttype=opttype;
				if(state.planData.invtype=="1"){
					params.packingGroupId=state.planData.id;
				}else{
				    params.packingGroupId=state.operatorInfo.packingGroupId;
				}
				var total=0;
				var itemtotal=0;
				var boxnum=1;
				state.tableData.list.forEach(item=>{
					for(var i=1;i<=parseInt(item.boxnum);i++){
						 var boxrow={};
						 var caseDetail={"sku":item.msku,"quantity":item.num};
								boxrow.weight=item.boxweight;
								boxrow.wunit='kg';
								boxrow.formid=state.planData.id;
								boxrow.boxnum=boxnum++;
								boxrow.caseListDetail=[caseDetail];
								boxrow.unit='cm';
								boxrow.length=item.boxlength;
								boxrow.height=item.boxheight;
								boxrow.width=item.boxwidth;
								params.boxListDetail.push(boxrow);
					}
				});
				shipmentBoxApi.savePackingInformation(params).then((res)=>{
					if(opttype=='save'){
						ElMessage.success("箱子信息保存成功！");
						emit('change',state.operatorInfo,state.planData);
						state.btnloading=false;
					}else if(res.data&&res.data.operationid){
						 ElMessage.warning("箱子信息已提交...");
						 operationRef.value.show(res.data.operationid,0);
					}else{
						state.boxVisiable=false;
						ElMessage.success("箱子信息保存成功！");
						state.btnloading=false;
						emit('change',state.operatorInfo,state.planData);
					}
				}).catch(erro=>{
					state.btnloading=false;
				});
				 
			}
			 
			function showEditBoxNum(row){
				changeTotalBoxNum();
			}
			function changeTotalBoxNum(){
				var index=0;
				state.totalBoxNum=0;
				state.tableData.forEach(function(item){
						if(item.boxnum && parseInt(item.boxnum)>0){
						  state.totalBoxNum=(state.totalBoxNum+parseInt(item.boxnum));
						}else{
							item.boxnum=0;
						}
						item.totalQuantityShipped=item.boxnum*item.num;
					});
				state.tableData.forEach(function(item){
					var number=item.boxnum;
					for(var i=0;i<state.totalBoxNum;i++){
						if(i>=index&&number>0){
							item["boxselect"+i]=true;
							index++;
							number--;
						}else{
					    	item["boxselect"+i]=false;
						}
					}
				})
			}
			 
			function downloadBoxInfo(){
				shipmentPlacementApi.downExcelBoxDetail({
					"shipmentid":state.planData.shipment.shipmentid
				}).then(res => {
						ElMessage.success( '导出成功！');
					 const blob = new Blob([res]);
					 if(window.navigator["msSaveOrOpenBlob"]&&window.navigator.msSaveOrOpenBlob()){
						navigator.msSaveBlob(blob, "boxDetail.xlsx")
					 }else{
						 var link=document.createElement("a");
						 link.href=window.URL.createObjectURL(blob);
						 link.download="boxDetail.xlsx";
						 link.click();
						 window.URL.revokeObjectURL(link.href);
					 }
				
				}).catch(e=>{
						ElMessage.error('导出失败！');
				})
			}
            function downExcelBox(){
				shipmentPlacementApi.downExcelBox({
					"shipmentid":state.planData.shipment.shipmentid
				}).then(res => {
						ElMessage.success( '导出成功！');
					 const blob = new Blob([res]);
					 if(window.navigator["msSaveOrOpenBlob"]&&window.navigator.msSaveOrOpenBlob()){
						navigator.msSaveBlob(blob, "boxInfo.xlsx")
					 }else{
						 var link=document.createElement("a");
						 link.href=window.URL.createObjectURL(blob);
						 link.download="boxInfo.xlsx";
						 link.click();
						 window.URL.revokeObjectURL(link.href);
					 }
				
				}).catch(e=>{
						ElMessage.error('导出失败！');
				})
			}
			function loadOptData(datas){
				console.log(datas);
				state.planData=datas;
				state.infoData=datas.shipmentAll;
				state.boxDetail.listbox=datas.listbox;
				if(datas.plan.submitbox==false && datas.shipment.status==3){
					state.boxDisable=false;
					state.boxed=false;
				}else{
					state.boxDisable=true;
					state.boxed=true;
				}
				if(state.infoData.transtyle){
				   state.form.tranType=state.infoData.transtyle;
				}
				getBoxDetial();
			}
 
	defineExpose({loadOptData})
</script>

<style scoped="scoped">
	.box-ship .el-form-item--small .el-form-item__content{
		line-height: 20px;
	}
	.font-extraSmall{
		font-weight: 400;
	}
	.box-ship .con-header{
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top:16px;
		margin-bottom:16px;
	}
	.box-ship .el-input-number--small{
		width: 100%;
	}
	.sd-table{
		margin-bottom:16px;
	}
	.box-size{
		display: flex;
		align-items: center;
		justify-content: flex-end;
	}
	.box-ship .el-input-number{width:70px !important;}
	.box-size span{
		margin-left: 8px;
		margin-right:8px;
	}
	.icon-c{
		font-size: 16px;
		margin-right:8px;
		line-height: 0px;
	}
	.box-ship .el-radio-group{
		line-height:20px;
	}
	.sd-table td{
		background-color: var(--el-bg-color);
	}
</style>
<style>
	.myshipdrop .el-input__wrapper{
		border-top-right-radius:0px !important;
		border-bottom-right-radius:0px !important;
	}
	.el-input.is-disabled .el-input__inner{
		color:#333;
		-webkit-text-fill-color:#333;
	}
	.el-radio__input.is-disabled+span.el-radio__label{color:inherit;}
</style>