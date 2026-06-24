<template>
	<el-dialog
	title="装箱"
	v-model="boxVisiable"
	width="80%"
	@close = "dialogClose"
	top="1vh"
	>
	<div class="con-body"  >
	 <!-- 原厂发货table -->
	 <el-table :data="tableData.list" 
	 class="sd-table"
	    ref="tableRef"
	    border="0" cellpadding="0" cellspacing="0"  style="min-height: 600px;" >
	 	<el-table-column width="70" label="图片" >
	 		<template #default="scope">
	 			<el-image :src="scope.row.skuinfo.image" style="width: 40px;height: 40px;"  ></el-image>
	 		</template>
	 	</el-table-column>
	 	<el-table-column label="名称/SKU"  >
	 		<template #default="scope">
	 			<div class='name'>{{scope.row.skuinfo.name}}</div>
	 			<div class='sku'>{{scope.row.skuinfo.sku}}</div>
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
	<template #footer>
		<el-button @click="boxVisiable=false" >关闭</el-button>
		<el-button @click="submitBox('save')" :loading="btnloading">保存</el-button>
	</template>
	</el-dialog>
	 <el-popover
	 	  ref="popoverAutoMarkRef"
	   :virtual-ref="autoMarkRef"
	   virtual-triggering
	   trigger="click"  placement="top" width="240px" >
	   <div ><el-input-number v-model="automarknumber" style="width:100%"></el-input-number></div>
	   <div class="text-right" style="padding-top:10px">
		   <el-button @click="handleAutoMarkHide">取消</el-button>
			<el-button type="primary" @click="handleAutoMarkConfirm">确认</el-button></div>
	  </el-popover>
	  <el-popover
        :visible="boxtitleVis"
	    :virtual-ref="boxQtyRef"
	    virtual-triggering
	    trigger="click"  placement="top" width="240px" >
	    <div >  箱号 : {{titleindex+1}} <br/>SKU:{{titlesku}} </div>
	   </el-popover>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,nextTick,computed } from 'vue';
	import '@/assets/css/packbox_table.css'
	import { ElMessage,ElMessageBox } from 'element-plus';
	import { Search,ArrowDown,Close} from '@element-plus/icons-vue';
	import { useRoute,useRouter } from 'vue-router';
	import { formatFloat,CheckInputIntLGZero,CheckInputFloat,getValue} from '@/utils/index.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	const emit = defineEmits(['change']);
	const operationRef=ref();
	let router = useRouter();
	const formid=router.currentRoute.value.query.id;
	const wstepRef=ref();
	const autoMarkRef=ref();
	const scrollbarRef=ref();
	const sliderVal=ref(0);
	const popoverAutoMarkRef=ref();
	const tableRef=ref();
	const boxQtyRef=ref();
	let props = defineProps({isdiv:undefined });
	const {isdiv} = toRefs(props);
	let state =reactive({
		notice:{
			message:"当前装箱不均衡",
			type:'danger'
		},
		
		boxVisiable:false,
		operatorInfo:{}, 
		step:0,
		sliderMax:0,
		title:['箱子信息提交','生成配置方案'],
		boxDisable:false,
		tableData:{"list":[]},
		inputboxNum:1,
		affixState:true,
		boxtitleVis:false,
		automarknumber:5,
		titleindex:0,
		titlesku:"",
		btnloading:false,
		boxWeightData:[],
		autoMarkIndex:null,
		boxListData :{list:[ {boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]}, ]},
		boxDetail:{},
		planData:{},
	})
	let{operatorInfo,boxDisable,tableData,btnloading,inputboxNum,
		boxWeightData,autoMarkIndex,automarknumber,titlesku,titleindex,
		boxListData,boxVisiable,boxDetail,planData,notice,
		boxtitleVis,sliderMax,affixState
		}=toRefs(state);
	
	
 
	function affixHide(e){
		state.affixState = e;
	}
	
	function handleAutoMarkHide(){
		popoverAutoMarkRef.value.hide();
	}
	function handleAutoMarkConfirm(){
		state.tableData.list.forEach((item)=>{
			for(var i=state.autoMarkIndex+1;i<state.inputboxNum&&i<state.automarknumber+state.autoMarkIndex ;i++){
				item['boxNum'+i]=item['boxNum'+state.autoMarkIndex];
				}
				packNumSum(item);
				checkPackNumBalance();
		});
		state.boxListData.list.forEach((item)=>{
			for(var i=state.autoMarkIndex+1;i<state.inputboxNum&&i<state.automarknumber+state.autoMarkIndex ;i++){
				item.boxcheck[i]=item.boxcheck[state.autoMarkIndex];
				}
		});
		for(var i=state.autoMarkIndex+1;i<state.inputboxNum&&i<state.automarknumber+state.autoMarkIndex ;i++){
			state.boxWeightData[i]=state.boxWeightData[state.autoMarkIndex]
		 }
		popoverAutoMarkRef.value.hide();
	}
	function handleShowAutoMark(e,index){
		state.autoMarkIndex = index
		const evt = e || window.e || window.event;
		autoMarkRef.value = evt.currentTarget
	}

	function handleShowFocus(e,index,sku){
		state.titleindex = index;
		state.titlesku = sku;
		const evt = e || window.e || window.event;
		boxQtyRef.value = evt.currentTarget
		state.boxtitleVis = true;
	}
 
 
	function submitBox(type){
		var isok=true;
		 state.tableData.list.forEach(item=>{
				if(parseInt(item.quantity)!=parseInt(item.num)*parseInt(item.boxnum)){
					isok=false;
				}
		  })
		if(isok==false){
				 ElMessage.error("装箱信息错误");
				 return;
		}
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
		params.boxnum=boxnum-1;
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
 
  
	function getBoxDetial(){
		var params={};
		params.formid=state.planData.id;
		params.packingGroupId=state.operatorInfo.packingGroupId;
		params.shipmentid=state.operatorInfo.shipmentid;
		params.arecase="true";
		shipmentBoxApi.getBoxDetail(params).then((res)=>{
		   state.boxDetail=res.data;
		   state.boxDetail.listbox.forEach(item=>{
			   state.tableData.list.forEach(i=>{
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
		});
	}
 
	function show(data,plans){
		state.planData=plans;
		state.boxVisiable=true;
		state.operatorInfo=data;
		state.inputboxNum=state.operatorInfo.boxnum;
		state.tableData.list=state.operatorInfo.items;
		getBoxDetial();
	}
	function dialogClose(){
		state.boxtitleVis = false;
	}
	 defineExpose({ show })
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
	.slider-wrap{
		background-color: #fff;
		padding:16px;
	}
	.dark .slider-wrap{
		background-color: #000000;
		padding:16px;
	}
</style>