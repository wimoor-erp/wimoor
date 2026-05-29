<template>
	<el-dialog
	title="装箱"
	v-model="boxVisiable"
	width="80%"
	@close = "dialogClose"
	top="1vh"
	>
	<div class="con-body"  >
	 <el-scrollbar
	 @scroll="scroll"
	 ref="scrollbarRef"
	 >
	<table class="sd-table" 
	   ref="tableRef"
	   border="0" cellpadding="0" cellspacing="0"  style="min-height: 600px;" >
		<thead  >
			<tr  >
				<th style="width: 70px;" >图片</th>
				<th   style="width: 170px;" >名称/SKU</th>
				<th   style="width: 110px;">数量</th>
				<!-- 箱子增减 -->
				<th v-for="(item,index) in inputboxNum" width="110px">	  
				   箱号
				   <span v-if="boxDetail.listbox&&boxDetail.listbox[index]&&boxDetail.listbox[index].shipmentid">
				   						    {{boxDetail.listbox[index].boxnum}}
											     <el-button type="primary"   
												 @click="e=>handleShowAutoMark(e,index)"
												  @mouseenter="e=>handleShowAutoMark(e,index)"
												  size="small" link>批量</el-button>
				   						   <div>
				   						   {{boxDetail.listbox[index].shipmentid}}
				   						   </div>
				   </span>
				   <span v-else>{{index+1}}     
					<el-button type="primary"    
					        @click="e=>handleShowAutoMark(e,index)"
						    @mouseenter="e=>handleShowAutoMark(e,index)"
						    size="small" link>批量</el-button></span>
				
				</th>
				<th width="80px;" > 
		 
					<el-tag   type="default" @click="inputboxNum=inputboxNum+1;  checkPackNumBalance();refreshsize();" link>+</el-tag>
					<el-tag  style="margin-left:5px;"  type="primary" @click="inputboxNum>1?inputboxNum=inputboxNum-1:1;  checkPackNumBalance();refreshsize();" link>-</el-tag> 
			 
				</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for = "(item,index) in tableData.list" :key="index"   >
				<td><img :src="item.skuinfo.image"  style="width:40px;height:40px" /></td>
				<td>
					<div class='name  text-omit-1'>{{item.skuinfo.name}}</div>
					<div class='sku'>{{item.skuinfo.sku}}</div>
					<div class="font-extraSmall">ASIN：{{item.asin}} </div>
				</td>
				
				<td style="text-align: center;">{{item.quantity}}</td>
				<!-- 箱子增减 -->
				<td  style="padding: 5px;text-align: center;" v-for="(sub,i) in inputboxNum" :key="i">
					   
							 <el-input
							 :disabled="boxDisable"  
							 :controls="false"   
							  v-model="item['boxNum'+i]" 
							 :id="'c-'+parseInt(i)+'-'+parseInt(index)+''"
							 :title="'箱号 :'+(i+1)+' SKU:'+ item.msku+''"
							 :min="0" 
							 @focus.stop="e=>handleShowFocus(e,i,item.msku)"
							 @input="packNumchange(item,i)"
							 />
						 
					   
				</td>
				<td>
					<span v-if="item.sum!=item.quantity" class="text-danger">{{item.sum}}</span>
				    <span v-else class="text-success">{{item.sum}}</span>
					<!-- <span>{{item.sum}}</span> -->
				</td>
			</tr>
		</tbody>
		<!-- 合计 -->
		<tbody>
			<tr class="h-bg">
				<td colspan="2">
					<div class="text-right">合计</div>
				</td>	
				<td  class=" font-bold">{{planData.sumquantity}}</td>
				<!-- 每列合计 -->
				<td  v-for="(item,index) in tableData.list.colBoxsumNum"><div class=" font-bold">{{getSummmary(index)}}</div></td>
				<td  class=" font-bold">{{getSummmary()}}</td>
			</tr>
		</tbody>	
		<!-- 箱子加货物重量 -->
		<tbody>
			<tr>
				<td colspan="3">
				<div class="text-right">箱子+货物重量(kg)</div>
				</td>
				<td style="padding: 5px;text-align: center;" v-for="(sub,i) in inputboxNum">
					<el-tooltip
					   class="box-item"
					   effect="light"
					   placement="top"
					 >
					 <template #content>箱号: {{i+1}} </template>
					  <el-input
					  :disabled="boxDisable"
					  :controls="false"  
					   v-model="boxWeightData[i]" 
					  :min="0" 
					  @input="boxWeightData[i]=CheckInputFloat(boxWeightData[i])"
					  @change="boxweightChange"/>
					 </el-tooltip>
					 
				</td>
				<td >{{boxweightSum}}</td>
			</tr>
		</tbody>
		<!-- 箱子尺寸 -->
		<tbody>
			<tr v-for="(item,index)  in boxListData.list">
				<td colspan="3">
				<div class="text-right">
						<div class=" box-size">
					     <span v-if="index == 0">包装箱尺寸(cm)</span>
						 <el-link class="icon-c" @click="delectBox(item)" :underline="false" v-else><el-icon ><Close /></el-icon></el-link>
						<el-input :disabled="boxDisable"    v-model="item.boxlength"  style="width:80px;"
						 @input="item.boxlength=CheckInputFloat(item.boxlength)" placeholder="长"/>
						<span>*</span>
						<el-input :disabled="boxDisable"  
						 v-model="item.boxwidth" 
						 style="width:80px;"
						 @input="item.boxwidth=CheckInputFloat(item.boxwidth)"
						 placeholder="宽"/>
						<span>*</span>
						<el-input :disabled="boxDisable"    style="width:80px;"
						@input="item.boxheight=CheckInputFloat(item.boxheight)"
						v-model="item.boxheight"   placeholder="高"/>
					</div>
				</div>
				</td>
				<td  v-for="(sub,i) in inputboxNum">
					<div>
						<el-tooltip
						   class="box-item"
						   effect="light"
						   placement="bottom"
						 >
						 <template #content>箱号 : {{i+1}}  </template>
						    <el-checkbox :disabled="boxDisable"  @change='checkboxChange(i,index)' v-model="item.boxcheck[i]"></el-checkbox>
						 </el-tooltip>
						 
					
					</div>
				</td>
				<td >
				<div>{{rowboxNumAndsize(item,index).boxnum}}箱</div>
				<div>{{rowboxNumAndsize(item,index).size}}m³</div>
				</td>
			</tr>
		</tbody>
			<!-- 添加箱子尺寸 -->
			<tbody>
				<tr >
					<td colspan="3" >
					<div class="text-right">
						<el-link type="primary"  @click="addBoxSize" :underline="false">添加其他不同尺寸的箱子</el-link>
						</div>
					</td>
					<td   v-show="inputboxNum>0" :colspan="inputboxNum">使用以上复选框指定特定包装箱的尺寸</td>
					<td>
					<div>{{sumbsn.boxnum}}箱</div>
					<div>{{sumbsn.size}}m³</div>
					</td>
				</tr>
			</tbody>
	</table>
	</el-scrollbar>
	  <el-affix position="bottom"
	  :style="{'height':affixState?'64px':'0px'}"
	   @change="affixHide"
	   :offset="0">
	   <div v-show="affixState" class="slider-wrap">
	   <el-slider
	       v-model="sliderVal"
	       :max="sliderMax"
	       :format-tooltip="formatTooltip"
	       @input="inputSlider"
	     />
		 </div>
	  </el-affix>
	</div>
	<template #footer>
		<div style="float: left;">
			<el-tag size="large" effect="plain"
           round :type="notice.type" >{{notice.message}}</el-tag>
		</div>
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
	import { pointKeyChnage} from '@/utils/jquery/key/point-key';
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
	let{operatorInfo,boxDisable,tableData,btnloading,inputboxNum,boxWeightData,autoMarkIndex,automarknumber,titlesku,titleindex,
	boxListData,boxVisiable,boxDetail,planData,notice,boxtitleVis,sliderMax,affixState}=toRefs(state);
	
	
	let boxweightSum =   computed(()=>{
		let sumweight = 0;
		for(var i=0;i<state.boxWeightData.length;i++){
			if(state.boxWeightData[i]&&state.boxWeightData[i]!=""){
			   sumweight +=parseFloat(state.boxWeightData[i]);
			}
		}
		return formatFloat(sumweight);
	})  
	//计算属性
	let sumbsn = computed(()=>{
		let obj={}
		let boxnum = 0
		let size = 0
		state.boxListData.list.forEach((row)=>{
			let obj=rowboxNumAndsize(row);
			size=size+obj.size;
			boxnum=boxnum+obj.boxnum;
		})
		obj.boxnum = formatFloat(boxnum);
		obj.size =formatFloat(size) ;
		return obj
	}) 
	
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
	//总计合计
	function getSummmary(index){
		let sumitem=0;
		if(index!=undefined){
			state.tableData.list.forEach((item)=>{
					if(item['boxNum'+index]!=undefined&&item['boxNum'+index]!=""){
						sumitem+=parseInt(item['boxNum'+index]);
					}
				});
		}else{
			state.tableData.list.forEach((item)=>{
				for(var i=0;i<state.inputboxNum ;i++){
					if(item['boxNum'+i]!=undefined&&item['boxNum'+i]!=""){
						sumitem+=parseInt(item['boxNum'+i]);
					}
					}
				});
		}
		return sumitem; 
	}
	function checkPackNumBalance(){
		var isBalance=true;
		var boxContent={};
		var boxGroup={};
			state.tableData.list.forEach((item)=>{
			for(var i=0;i<state.inputboxNum ;i++){
				if(item['boxNum'+i]!=undefined&&item['boxNum'+i]!=""){
					if(!boxContent['boxNum'+i]){
						boxContent['boxNum'+i]=item.sku+":"+item['boxNum'+i];
					}else{
						boxContent['boxNum'+i]=boxContent['boxNum'+i]+","+item.sku+":"+item['boxNum'+i];
					}
				}
				}
			});
		for(var i=0;i<state.inputboxNum ;i++){
			 var content=boxContent['boxNum'+i];
			 if(!boxGroup['boxNum'+i]){
				 for(var j=0;j<state.inputboxNum ;j++){
				 	if(!boxGroup['boxNum'+j]&&content==boxContent['boxNum'+j]){
				 		boxGroup['boxNum'+j]=i;
				 	}
				 }
			 }
			 
		}	 
		var groupnumber=[];
		for(var i=0;i<state.inputboxNum ;i++){
			  groupnumber[i]=0;
		}	
		
		for(var i=0;i<state.inputboxNum ;i++){
			 var group=boxGroup['boxNum'+i];
			 groupnumber[group]++;
		}
			groupnumber.forEach(item=>{
				if(item!=0&&item<5){
					isBalance=false;
				}
			})
		if(isBalance){
			state.notice.message="装箱信息均匀";
			state.notice.type="success";
		}else{
			state.notice.message="当前装箱不均衡";
			state.notice.type="danger";
		}
		
	}
	
	
	function packNumchange(row,index){
		var boxindex='boxNum'+index;
		if(row[boxindex]&&row[boxindex]!="0"){
		   row[boxindex]=CheckInputIntLGZero(row[boxindex]);
		}
		packNumSum(row);
		checkPackNumBalance();
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
	function generatePlacementOptions(){
		shipmentPlacementApi.generatePlacementOptions({"formid":formid}).then(res=>{
			if(res.data.operationid){
				 operationRef.value.show(res.data.operationid,1);
			}
		})
	}
	function handleOperationChange(data){
		//跳转路由至第二步
		if(data && data.operationStatus=="SUCCESS"&&data.operation=="setPackingInformation"){
		   generatePlacementOptions();
		}else if(data && data.operationStatus=="SUCCESS"&&data.operation=="generatePlacementOptions"){
			ElMessage.success("箱子信息保存成功！");
			state.boxVisiable=false;
			state.btnloading=false;
			emit('change',state.operatorInfo,state.planData);
		}
		
	}
	 
	 
	function submitBox(type){
		var isok=true;
		 state.tableData.list.forEach(item=>{
				if(item.sum!=item.quantity){
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
		params.boxnum=state.inputboxNum;
		params.opttype=opttype;
		if(state.planData.invtype=="1"){
			params.packingGroupId=state.planData.id;
		}else{
		    params.packingGroupId=state.operatorInfo.packingGroupId;
		}
		var total=0;
		var itemtotal=0;
	
		
		for(let i=0;i<state.inputboxNum;i++){
			 var boxrow={};
			boxrow.weight=state.boxWeightData[i];
			boxrow.wunit='kg';
			boxrow.formid=state.planData.id;
			boxrow.boxnum=i+1;
			state.boxListData.list.forEach(box=>{
				 if(box.boxcheck[i]==true){
						boxrow.unit='cm';
						boxrow.length=box.boxlength;
						boxrow.height=box.boxheight;
						boxrow.width=box.boxwidth;
				}
			})
			boxrow.caseListDetail=[];
			state.tableData.list.forEach(item=>{
				if(item["boxNum"+i]){
					var caseDetail={"sku":item.msku,"quantity":item["boxNum"+i]};
					total=total+parseInt(caseDetail.quantity);
					boxrow.caseListDetail.push(caseDetail);
				}
			});
			 params.boxListDetail.push(boxrow);
		}
	 
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
function initOneBoxData(){
		 //渲染box数据
		  state.tableData.list.forEach( async function(item,index){
				 item["boxNum"+0]=item.quantity; 
			     packNumSum(item);
		  });
		  state.boxListData.list=[];
		 let obj = {boxlength:"",boxwidth:"",boxheight:"",boxcheck:[]};
		 for(let i=0;i<state.inputboxNum;i++){
		 	obj.boxcheck[i]=true;
		 }
		 obj.boxcheck[0]=true;
		 state.boxListData.list.push(obj)
		  state.tableData.list.colBoxsumNum = computed(()=>{
		  	const sumarr = [];
		  	state.tableData.list.forEach((item)=>{
		  		for(var i=0;i<state.inputboxNum ;i++){
		  			if(sumarr[i]){
		  				sumarr[i] += item['boxNum'+i]
		  			}else{
		  				sumarr[i] = item['boxNum'+i]
		  			}
		  		}
		  	})
		  	return sumarr
		  })
	 } 
	 
	 function initMoreBoxData(){
	 		 //渲染box数据
	 		  state.boxListData.list=[];
	 		 let obj = {boxlength:"",boxwidth:"",boxheight:"",boxcheck:[]};
	 		 for(let i=0;i<state.inputboxNum;i++){
	 		 	obj.boxcheck[i]=true;
	 		 }
	 		 obj.boxcheck[0]=true;
	 		 state.boxListData.list.push(obj)
	 		  state.tableData.list.colBoxsumNum = computed(()=>{
	 		  	const sumarr = [];
	 		  	state.tableData.list.forEach((item)=>{
	 		  		for(var i=0;i<state.inputboxNum ;i++){
	 		  			if(sumarr[i]){
	 		  				sumarr[i] += item['boxNum'+i]
	 		  			}else{
	 		  				sumarr[i] = item['boxNum'+i]
	 		  			}
	 		  		}
	 		  	})
	 		  	return sumarr
	 		  })
	 	 } 
	 
function loadBoxData(){
		 //渲染box数据 item是group
		  state.tableData.list.forEach( async function(item,index){
			  item.sku=item.msku;
			  for(var i=0;i<state.boxDetail.totalBoxNum ;i++){
				  state.boxDetail.listbox[i].caseListDetail.forEach(caseItem=>{
					  if(caseItem.sku==item.sku){
						   item["boxNum"+i]=caseItem.quantity; 
					  }
				  })
			  }
			packNumSum(item);
		  });
		  state.boxListData.list=[];
		  state.boxDetail.listbox.forEach((item)=>{
					 state.boxWeightData[item.boxnum-1]=item.weight;
					 let isold=false;
					if(state.boxListData.list&&state.boxListData.list.length>0){
						 state.boxListData.list.forEach((boxitem)=>{
							 if(boxitem.boxlength==item.length
								 &&boxitem.boxwidth==item.width
								 &&boxitem.boxheight==item.height){
									 boxitem.boxcheck[item.boxnum-1]=true;
									 isold=true;
									 return;
								 }
						 });
					  }
					if(isold==false){
						let obj = {boxlength:item.length,boxwidth:item.width,boxheight:item.height,boxcheck:[]};
						for(let i=0;i<state.inputboxNum;i++){
							obj.boxcheck[i]=false;
						}
						obj.boxcheck[(item.boxnum-1)]=true;
						state.boxListData.list.push(obj)
					} 
		  });
		  
		  state.tableData.list.colBoxsumNum = computed(()=>{
		  	const sumarr = [];
		  	state.tableData.list.forEach((item)=>{
		  		for(var i=0;i<state.inputboxNum ;i++){
		  			if(sumarr[i]){
		  				sumarr[i] += item['boxNum'+i]
		  			}else{
		  				sumarr[i] = item['boxNum'+i]
		  			}
		  		}
		  	})
		  	return sumarr
		  })
		  checkPackNumBalance();
	
	 }
	
	function inputSlider(value){
		scrollbarRef.value.setScrollLeft(value);
	}
	function scroll(e){
	 
		sliderVal.value = e.scrollLeft; 
	}
	const formatTooltip = (value)=>{
		return value + 'px';
	}
    function refreshsize(){
		var dwidth = window.innerWidth*0.8 - 60
		var mytime=setTimeout(function(){
				state.sliderMax =  tableRef.value?.clientWidth - dwidth;
		    clearTimeout(mytime);
		 },300);
	}
	function getBoxDetial(){
		var params={};
		params.formid=state.planData.id;
		params.packingGroupId=state.operatorInfo.packingGroupId;
		params.shipmentid=state.operatorInfo.shipmentid;
		shipmentBoxApi.getBoxDetail(params).then((res)=>{
		   state.boxDetail=res.data;
		   if(res.data && res.data.listbox && res.data.listbox.length>0){
				loadBoxData();
			}else if( parseInt(state.inputboxNum)<=1){
				initOneBoxData();
			}else{
				initMoreBoxData();
			}
			refreshsize(); 
		});
	}
	
	
	//竖向向装箱数求和
	state.tableData.list.colBoxsumNum = computed(()=>{
		const sumarr = [];
		state.tableData.list.forEach((item)=>{
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
		
		return sumarr;
	}) 
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
	
	//确认装箱数量
	function subimtBoxNum(){
		state.tableData.list.forEach((item)=>{
			for(var i=0;i<state.inputboxNum;i++){
				item['boxNum'+i] = undefined
				state.boxWeightData[i] = undefined;
			}
		})
	}
	
	//删除
	function delectBox(row){
		state.boxListData.list.splice(state.boxListData.list.indexOf(row), 1)
	}
	
	//箱子尺寸单选
	function checkboxChange(i,a){
		if(i==0&&a==0&&state.boxListData.list.length==1){
			state.boxListData.list[0].boxcheck=[];
			for(var ibox=0;ibox<state.inputboxNum ;ibox++){
				state.boxListData.list[0].boxcheck.push(true);
			}
		}else{
			state.boxListData.list.forEach((item,index)=>{
				if(a==index){
					item.boxcheck[i] = true
				}else{
					item.boxcheck[i] = false
				}
			})
		}
	}
	
	//添加箱子尺寸
	function addBoxSize(type){
		let obj = {boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]};
		if(type=="init"){
			state.boxListData.list=[];
			state.boxListData.list.push(obj);
		}else{
			state.boxListData.list.push(obj)
		}
		
	}
	
	//箱子重量变化
	function boxweightChange(){
		//console.log("重量数组",boxWeightData.value)
	}
	
	
	
	function show(data,plans){
		state.planData=plans;
		state.boxVisiable=true;
		state.operatorInfo=data;
		state.inputboxNum=state.operatorInfo.boxnum;
		state.tableData.list=state.operatorInfo.items;
		getBoxDetial();
		addBoxSize("init");
		pointKeyChnage(".sd-table");
		state.tableData.list.colBoxsumNum = computed(()=>{
			const sumarr = [];
			state.tableData.list.forEach((item)=>{
				for(var i=0;i<state.inputboxNum ;i++){
					if(sumarr[i]){
						sumarr[i] += item['boxNum'+i]
					}else{
						sumarr[i] = item['boxNum'+i]
					}
				}
			})
			return sumarr;
		})
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