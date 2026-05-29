<template>
	<div class="box-ship">
		<el-row>
			<el-col :span="16">
				<el-form :model="form"  label-position="left" label-width="200px">
					<el-form-item label="选择运输方式">
						<el-radio-group v-model="form.tranType"  @change="loadCarrier" :disabled="boxDisable">
							<el-radio  label="SP">
							<p>小包裹快递</p>
							<span class="font-extraSmall">我要用单独的箱子由快递配送</span>
							</el-radio>
							<el-radio   label="LTL" >
							<p>汽车零担</p>
							<span class="font-extraSmall">我要发送托拍</span>
							</el-radio>
						</el-radio-group>
					</el-form-item>
							<el-form-item label="配送商" prop="carrier">
								<el-space :size="16">
								<el-select v-model="form.carrier" placeholder="请选择"  :disabled="boxDisable">
									<el-option v-for="item in carrierData.list" :label="item" :value="item" />
								</el-select>
								<span class="font-extraSmall">注：暂不支持亚马逊合作承运商</span>
								</el-space>
							</el-form-item>
				</el-form>
			</el-col>
		</el-row>
		<el-divider/>
		<div class="con-header">
			<el-space :spacer="spacer">
				<h4>箱子信息</h4>
				<el-space v-if="!boxDetail.arecasesrequired"  class="font-small">
					<span>需要</span>
					 <el-input :controls="false" v-model="inputboxNum" @input="inputBoxNumChange" @focus="inputBoxNumChange" :disabled="boxed" />
					<span>个箱子装箱</span>
					<el-button type='primary' @click="subimtBoxNum" :disabled="boxed">确认</el-button>
				</el-space>
				
				<el-space v-if="form.tranType=='LTL'"  class="font-small">
					<span>托盘个数:</span>
					 <el-input-number :controls="false"  :max="30" v-model.number="panNum"   />
				</el-space>
				
			</el-space>
				<el-button @click="jumpBoxinfo">跳过箱子信息</el-button>
		</div>
		<div class="con-body"  >
			<div style="width:100%;overflow-x: auto;margin-bottom:10px;">
			<table class="sd-table"    border="0" cellpadding="0" cellspacing="0"  v-if="!boxDetail.arecasesrequired">
				<thead>
					<tr>
						<th style="width: 60px;" >图片</th>
						<th style="width: 280px;"  >名称/SKU</th>
						<th   style="width: 80px;">货件数量</th>
						<!-- 箱子增减 -->
						<th v-for="(item,index) in inputboxNum" width="100px">箱子{{index+1}}</th>
						<th width="80px;" >总计</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for = "(item,index) in tableData.list" :key="index" >
						<td><img :src="item.image"   style="width:40px;height:40px" /></td>
						<td>
							<div class='name  text-omit-1'>{{item.name}}</div>
							<div class='sku'>{{item.sellersku}}</div>
						</td>
						<td>{{item.quantityshipped}}</td>
						<!-- 箱子增减 -->
						<td  v-for="(sub,i) in inputboxNum" :key="i">
							   <el-popover
								  placement="top"
								  trigger="hover"
								  :visible="item['popover'+i]" 
								>
								 <template #default>
								    箱号 : {{i+1}} <br/>SKU:{{ item.sellersku}} 
								 </template>
								 <template #reference> 
								 <el-input
								  value-on-clear=""
								 :disabled="boxDisable"  
								 :controls="false"   
								  v-model="item['boxNum'+i]" 
								  @blur="item['popover'+i]=false"
								  @focus="item['popover'+i]=true"
								 :id="'c-'+parseInt(i)+'-'+parseInt(index)+''"
								 :title="'箱号 :'+(i+1)+' SKU:'+ item.sellersku+''"
								 :min="0" 
								 @input="packNumchange(item,i)"
								 />
								 </template>
							  </el-popover>
						</td>
						<td><span v-if="item.sum!=item.quantityshipped" class="text-danger">{{item.sum}}</span>
						    <span v-else class="text-success">{{item.sum}}</span>
						</td>
					</tr>
				</tbody>
				<!-- 合计 -->
				<tbody>
					<tr class="h-bg">
						<td colspan="2">
							<div class="text-right">合计</div>
						</td>	
						<td  class=" font-bold">{{boxDetail.sumquantityshiped}}</td>
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
						<td v-for="(sub,i) in inputboxNum">
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
						<tr>
							<td colspan="3">
							<div class="text-right">
								<el-link type="primary"  @click="addBoxSize" :underline="false">添加其他不同尺寸的箱子</el-link>
								</div>
							</td>
							<td v-show="inputboxNum>0" :colspan="inputboxNum">使用以上复选框指定特定包装箱的尺寸</td>
							<td >
							<div>{{sumbsn.boxnum}}箱</div>
							<div>{{sumbsn.size}}m³</div>
							</td>
						</tr>
					</tbody>
			</table>
			</div>
			<!-- 原厂发货table -->
			<!-- 原厂发货table -->
			<!-- 原厂发货table -->
			<el-table :data="tableData.list" v-if="boxDetail.arecasesrequired" border style="width: 100%;margin-bottom:10px;">
				<el-table-column width="70" label="图片" >
					<template #default="scope">
						<el-image :src="scope.row.image" style="width: 40px;height: 40px;"  ></el-image>
					</template>
				</el-table-column>
				<el-table-column label="名称/SKU"  >
					<template #default="scope">
						<div class='name'>{{scope.row.name}}</div>
						<div class='sku'>{{scope.row.SellerSKU}}</div>
					</template>
				</el-table-column>
				 <el-table-column prop="QuantityShipped" label="货件数量" width="80" />
				 <el-table-column label="每箱装箱个数" width="120">
				 	<template #default="scope">
				 		 <el-input-number :controls="false"  v-model="scope.row.num" :min="0" @change="changeTotalBoxNum()"/>
				 	</template>
				 </el-table-column>
				 <el-table-column label="箱子个数" width="120">
				 	<template #default="scope">
				 		 <el-input-number :controls="false"  v-model="scope.row.boxnum" :min="0" @change="changeTotalBoxNum()"/>
				 	</template>
				 </el-table-column>
				 <el-table-column label="箱规" width="320">
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
				 <el-table-column label="箱号设置" width="120">
				 	<template #default="scope">
				 		<el-link  @click="showEditBoxNum(scope.row)" type="primary" :underline="false" class="font-extraSmall">设置</el-link>
				 	</template>
				 </el-table-column>
				  <el-table-column prop="totalQuantityShipped" label="总计"  >
					  <template #default="scope">
					       <span v-if="scope.row.totalQuantityShipped!=scope.row.quantityshipped" class="text-danger">{{scope.row.totalQuantityShipped}}</span>
					      <span v-else class="text-success">{{scope.row.totalQuantityShipped}}</span>
					 </template>
					 </el-table-column> 
			</el-table>
			<!-- 原厂发货table -->
			<!-- 原厂发货table -->
			<!-- 原厂发货table -->
			<el-row class="mar-bot ">
			<el-space >
				  <el-button 
					  @click="downloadBoxInfo">下载装箱详情
				  </el-button>
				 <el-button-group>
				 <el-select v-model="boxmarksType" class="m-2 myshipdrop"  placeholder="选择打印箱子标签类型">
				    <el-option
				      v-for="item in boxMarks"
				      :key="item.value"
				      :label="item.label"
				      :value="item.value"
				   />
				   </el-select>
				   <el-dropdown trigger="click" >
				     <el-button >
				                打印箱子标签<el-icon class="el-icon--right"><arrow-down /></el-icon>
				     </el-button>
				     <template #dropdown>
				       <el-dropdown-menu >
						 <el-dropdown-item @click="downloadLabel('SELLER_LABEL')">下载箱子标签</el-dropdown-item>
						 <el-dropdown-item @click="downloadLabel('BARCODE_2D')">下载2D条形码</el-dropdown-item>
						 <el-dropdown-item @click="downloadLabel('PALLET')">下载托盘标签</el-dropdown-item>
				       </el-dropdown-menu>
				     </template>
				   </el-dropdown>
				 </el-button-group>
				 <div style="float:left" class="font-extraSmall">
				 	箱子信息：<FeedStatus ref="feedStatusExcel" :feedid="feedidexcel" filename="shipmentFeedFile.xlsx"></FeedStatus>
				 </div>
			</el-space>
			<div class="rt-btn-group">
				
				<el-button :disabled="boxDisable"   @click="submitBox('save')" >保存装箱信息</el-button>
				<el-button :disabled="boxDisable||(!boxDetail.arecasesrequired&&boxDetail.sumquantityshiped!=getSummmary())" :loading="submitloading" type="primary" @click="submitBox('submit')">提交</el-button>
			</div>
			</el-row>
			<el-row :gutter="16" class="mar-bot">
				<el-col :span="12">
				<FeedStatus ref="feedStatus" :feedid="feedid" filename="shipmentFeedFile.xml"></FeedStatus>
				</el-col>
				<el-col :span="12">
					<el-alert  :closable="false" title="打印箱子标签之后才能进行下一步!" type="info" />
				</el-col>
			</el-row>
			
			<el-row class="mar-bot">
				 <ShipmentOpt ref="optRef"  />
				<div class="rt-btn-group">
					<el-button type="primary" @click="stepclick(0)" plain>上一步</el-button>
					<el-button type="primary" @click="stepclick(2)" plain>下一步</el-button>
				</div>
			</el-row>
		</div>
	
	  <el-dialog v-model="centerDialogVisible" title="箱子箱号配置" width="50%" center >
		  <table class="sd-table" border="0" cellpadding="0" cellspacing="0" >
		  	<thead>
		  		<tr>
		  			<th width="60px">图片</th>
		  			<th>名称/SKU</th>
		  			<th >箱子个数</th>
					<th >箱号选择</th>
		  		</tr>
		  	</thead>
		  	<tbody>
		  		<tr v-for = "(item,index) in tableData.list" :key="index" >
		  			<td><el-image :src="item.image" width="40" height="40"></el-image></td>
		  			<td>
		  				<div class='name text-omit-1'>{{item.name}}</div>
		  				<div class='sku'>{{item.SellerSKU}}</div>
		  			</td>
					<td >{{item.boxnum}}</td>
		  			<td >
						<div style="display: inline;margin: 5px;" v-for="(sub,i) in totalBoxNum">
							<span>{{i+1}}:</span>
							<!-- v-model="item.numbercase" -->
							<el-checkbox  size="large"  v-model="item['boxselect'+i]" ></el-checkbox>
						</div>
		  			</td>
		  		</tr>
		  	</tbody>
		  </table>
	    
	    <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="centerDialogVisible = false">关闭</el-button>
	        <el-button type="primary" @click="setBoxNum()"
	          >确认</el-button
	        >
	      </span>
	    </template>
	  </el-dialog>
</div>
	  
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,computed,nextTick} from 'vue'
	import {Search,ArrowDown,Close} from '@element-plus/icons-vue'
	import { ElDivider,ElMessageBox ,ElMessage } from 'element-plus'
	import '@/assets/css/packbox_table.css'
	import materialApi from '@/api/erp/material/materialApi.js';
	import feedApi from '@/api/amazon/feed/feedApi.js';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import { useRoute,useRouter } from 'vue-router'
	import ShipmentOpt from"./shipment_operator.vue"
	import FeedStatus from "@/components/feedstatus/index.vue"
	import {formatFloat,CheckInputIntLGZero,CheckInputFloat} from '@/utils/index.js';
	import {pointKeyChnage} from '@/utils/jquery/key/point-key';
	import boxMarks from '@/model/erp/ship/boxMarks.json';
   
    const emit = defineEmits(['stepdata',"carrData","change"]);
	let spacer = h(ElDivider, { direction: 'vertical' })
	let radio1 = ref("3")
	let boxed =ref(false)
	let shipdata=ref({})
	let router = useRouter()
	let country=ref("")
	let boxDetail=ref({})
	let feedStatus=ref(FeedStatus);
	let feedStatusExcel=ref(FeedStatus);
	let totalBoxNum=ref(0)
	let feedid=ref("");
	let feedidexcel=ref("");
	let optRef=ref();
	let centerDialogVisible=ref(false)
	let submitloading=ref(false);
	let infoData=ref({});
	const shipmentid=router.currentRoute.value.query.shipmentid;
	let carrierData = reactive({list:[
		 
	]})
	let boxDisable=ref(false);
	let inputboxNum = ref("")
	let form = reactive({
		carrier: 'OTHER', 
		tranType:"SP",
	})
	let boxmarksType=ref("PackageLabel_Plain_Paper")
	let boxWeightData = ref([])
	let panNum=ref(1)
	let tableData = reactive({list:[
			
	]}) 
	let newtableData=reactive({list:[
		{
			name:'箱子+货物重量',
		},
		{
			name:'包装箱尺寸(cm)'
		}
	]}) 
	let boxListData = reactive({list:[
		{boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]},
	]})
			 function cellTitle(sku,index){
				 return sku+"    箱号:"+(index+1)+"";
			 }
			 function inputBoxNumChange(){
				 inputboxNum.value=CheckInputIntLGZero(inputboxNum.value);
				 if(inputboxNum.value){
				    inputboxNum.value=parseInt(inputboxNum.value)
				 }
				 addBoxSize("init");
				pointKeyChnage(".sd-table");
			 }
			//确认装箱数量
			function subimtBoxNum(){
				tableData.list.forEach((item)=>{
					for(var i=0;i<inputboxNum.value ;i++){
						item['boxNum'+i] = undefined
						boxWeightData.value[i] = undefined
					}
				})
				boxed.value =true
			}
			function radioChange(val){
				radio1.value= val
			}
			//箱子尺寸单选
			function checkboxChange(i,a){
				if(i==0&&a==0&&boxListData.list.length==1){
					boxListData.list[0].boxcheck=[];
					for(var ibox=0;ibox<inputboxNum.value ;ibox++){
						boxListData.list[0].boxcheck.push(true);
					}
				}else{
					boxListData.list.forEach((item,index)=>{
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
				for(var i=0;i<inputboxNum.value ;i++){
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
					tableData.list.forEach((item)=>{
							if(item['boxNum'+index]!=undefined&&item['boxNum'+index]!=""){
								sumitem+=parseInt(item['boxNum'+index]);
							}
						});
				}else{
					tableData.list.forEach((item)=>{
						for(var i=0;i<inputboxNum.value ;i++){
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
				boxListData.list.forEach((row)=>{
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
				for(var i=0;i<boxWeightData.value.length;i++){
					if(boxWeightData.value[i]&&boxWeightData.value[i]!=""){
					   sumweight +=parseFloat(boxWeightData.value[i]);
					}
				}
				return formatFloat(sumweight);
			})  
				//竖向向装箱数求和
			tableData.list.colBoxsumNum = computed(()=>{
				const sumarr = [];
				tableData.list.forEach((item)=>{
					for(var i=0;i<inputboxNum.value ;i++){
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
				for(var i=0;i<inputboxNum.value ;i++){
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
			//箱子重量变化
			function boxweightChange(){
				//console.log("重量数组",boxWeightData.value)
			}
			//添加箱子尺寸
			function addBoxSize(type){
				let obj = {boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]};
				if(type=="init"){
					boxListData.list=[];
					boxListData.list.push(obj);
				}else{
					boxListData.list.push(obj)
				}
				
			}
			//删除
			function delectBox(row){
				boxListData.list.splice(boxListData.list.indexOf(row), 1)
			}
			function loadCarrier(){
				  var tranType=form.tranType;
				  if(boxDetail.value.market!=null){
					  shipmenthandlingApi.getCarrier({
					  	"country":boxDetail.value.market,
					  	"transtyle":tranType
					  }).then(res=>{
					  	   carrierData.list=res.data;
					  })
				  }
			}
			function getBoxDetial(res){
					if(res.data){
						boxDetail.value=res.data;
						tableData.list=res.data.itemlist;
						loadCarrier();
						 inputboxNum.value=res.data.totalBoxNum;
						 if(res.data.shipment.carrier){
						    form.carrier=res.data.shipment.carrier;
						 }
						 tableData.list.forEach( async function(item,index){
						 	//混装发货 caselist
						 	if(boxDetail.value.arecasesrequired==false){
						 		for(let i=0;i<inputboxNum.value;i++){
									if(item.boxsku[i+1]&&item.boxsku[i+1]["quantity"]){
										item["boxNum"+i]=item.boxsku[i+1].quantity;
									}else{
										item["boxNum"+i]=0;
									}
						 		}
							   packNumSum(item);
						 	}else{
						 		//原厂包装发货 caselist
								if(item.boxnum&&parseInt(item.boxnum)>0){
								    item.num=parseInt(item.boxnum);
								}else{
									item.num=0;
								}
								if(item.num>0&&item.quantityshipped){
    								item.boxnum=item.quantityshipped/item.num;
								}else{
									item.boxnum=0;
								}
									
								if(item.boxsku&&Object.keys(item.boxsku).length>=1){
									item.boxnum=Object.keys(item.boxsku).length;
									item.totalQuantityShipped=item.boxnum*item.num;
									for (var key in item.boxsku) {
										item.num=item.boxsku[key].unitspercase;
										var boxnode=boxDetail.value&&boxDetail.value.listbox&&boxDetail.value.listbox.length>parseInt(key)-1?boxDetail.value.listbox[parseInt(key)-1]:null;
										if(boxnode){
											item.boxlength=boxnode.length;
											item.boxwidth =boxnode.width;
											item.boxheight=boxnode.height;  
											item.boxweight=boxnode.weight;  
										}
									 }
								}else{
									item.totalQuantityShipped=item.boxnum*item.num;
								}
						 	}
						 });
						 boxListData.list=[];
						 res.data.listbox.forEach((item)=>{
							 boxWeightData.value[item.boxnum-1]=item.weight;
							 let isold=false;
							 if(boxListData.list&&boxListData.list.length>0){
									 boxListData.list.forEach((boxitem)=>{
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
									for(let i=0;i<inputboxNum.value;i++){
									    obj.boxcheck[i]=false;
									}
									obj.boxcheck[(item.boxnum-1)]=true;
									boxListData.list.push(obj)
								} 
						 });
						 
						 tableData.list.colBoxsumNum = computed(()=>{
						 	const sumarr = [];
						 	tableData.list.forEach((item)=>{
						 		for(var i=0;i<inputboxNum.value ;i++){
						 			if(sumarr[i]){
						 				sumarr[i] += item['boxNum'+i]
						 			}else{
						 				sumarr[i] = item['boxNum'+i]
						 			}
						 		}
						 	})
						 	return sumarr
						 })
						if(res.data.shipment && res.data.shipment.submissionid){
							feedid.value=res.data.shipment.submissionid;
							feedidexcel.value=res.data.shipment.submissionidExcel;
						    feedStatus.value.submitfeedInfo(res.data.shipment.submissionid);
						    feedStatusExcel.value.submitfeedInfo(res.data.shipment.submissionidExcel);
						}
					}
				 
			}
			function submitBox(opttype){
				//console.log(tableData.list);
				var caselist=[];
				var boxlist=[];
				var summary=getSummmary();
				//混装发货 boxlist
				if(!boxDetail.value.arecasesrequired){
					if(boxDetail.value.sumquantityshiped!=summary){
						 ElMessage.error( '数量校验失败，请确所有产品都已装箱！');
						 return ;
					}
					boxListData.list.forEach(function(item,index){
						var boxnums="";							
						for(let i=0;i<inputboxNum.value;i++){
							 if(item.boxcheck[i]==true){
								 var boxrow={};
								 boxrow.shipmentid=shipmentid;
								 boxrow.boxnum=(i+1)+"";
								 boxrow.length=item.boxlength;
								 boxrow.width=item.boxwidth;
								 boxrow.height=item.boxheight;
								 boxrow.unit='cm';
								 boxrow.weight=boxWeightData.value[i];
								 boxrow.wunit='kg';
								 if(boxrow.length&&boxrow.weight){
									 boxlist.push(boxrow);
								 }
							 }
						}
					})
					if(boxlist.length!=inputboxNum.value){
						ElMessage.error('请填写完整的箱子信息！');
						 return ;
					}
				}else{
					changeTotalBoxNum();
				}
	
				tableData.list.forEach( async function(item,index){
					//混装发货 caselist
					if(boxDetail.value.arecasesrequired==true){
						//原厂包装发货 caselist
						inputboxNum.value=totalBoxNum.value;
						for(let i=0;i<totalBoxNum.value;i++){
							if(item['boxselect'+i]==true){
								var caserow={};
								caserow.shipmentid=item.ShipmentId;
								caserow.merchantsku=item.SellerSKU;
								caserow.unitspercase=item.num;
								caserow.numberofcase=(1+i);
								caserow.quantity=item.num;
								caselist.push(caserow);
								
								var boxrow={};
								boxrow.shipmentid=item.ShipmentId;
								boxrow.boxnum=(1+i);//箱号
								boxrow.length=item.boxlength;
								boxrow.width=item.boxwidth;
								boxrow.height=item.boxheight;
								boxrow.unit='cm';
								boxrow.weight=item.boxweight;
								boxrow.wunit='kg';
								boxlist.push(boxrow);
							} 
						}
					    
					}else{
						for(let i=0;i<inputboxNum.value;i++){
							var caserow={};
							caserow.shipmentid=item.ShipmentId;
							caserow.merchantsku=item.sellersku;
							caserow.numberofcase=(1+i);
							if(item["boxNum"+i] && item["boxNum"+i]!=undefined){
								caserow.quantity=item["boxNum"+i];
								caserow.unitspercase=1;
							}else{
								caserow.quantity=0;
								caserow.unitspercase=0;
							}
							caselist.push(caserow);
						}
					}
				});
				var data={
					"shipmentid":shipmentid,
					"boxnum":inputboxNum.value,
					"transtyle":form.tranType,
					"opttype":opttype,
					"carrier":form.carrier,
					"caseListDetail":caselist,
					"boxListDetail":boxlist
				}
				submitloading.value=true;
				shipmenthandlingApi.submitCart(data).then(res=>{
				submitloading.value=false;
					if(res.data){
						ElMessage.success( '提交成功！');
						 if(opttype=="submit"){
							 feedid.value=res.data.submissionid;
							 feedidexcel.value=res.data.submissionidExcel;
							 feedStatus.value.submitfeedInfo(feedid.value);
							 feedStatusExcel.value.submitfeedInfo(feedidexcel.value);
							 emit("change");
						 }
					}else{
						emit("change");
						ElMessage.success('保存成功！');
					}
				}).catch(e=>{
					submitloading.value=false;
				})
			}
			function showEditBoxNum(row){
				changeTotalBoxNum();
				centerDialogVisible.value=true;
			}
			function changeTotalBoxNum(){
				var index=0;
				totalBoxNum.value=0;
				tableData.list.forEach(function(item){
						if(item.boxnum && parseInt(item.boxnum)>0){
						  totalBoxNum.value=(totalBoxNum.value+parseInt(item.boxnum));
						}else{
							item.boxnum=0;
						}
						item.totalQuantityShipped=item.boxnum*item.num;
					});
				tableData.list.forEach(function(item){
					var number=item.boxnum;
					for(var i=0;i<totalBoxNum.value;i++){
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
			function setBoxNum(){
				//console.log(tableData.list);
				centerDialogVisible.value=false;
			}
			function downloadBoxInfo(){
				shipmenthandlingApi.downExcelBoxDetail({
					"shipmentid":shipmentid
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
			function downloadLabel(labeltype){
				var data={};
				data.shipmentid=shipmentid;
				data.pagetype=boxmarksType.value;
				data.labeltype=labeltype;
				data.pannum=panNum.value;
				shipmenthandlingApi.downLabel(data);
			}
			function loadOptData(datas){
				optRef.value.loadOptData(datas.shipmentAll);
				infoData.value=datas.shipmentAll;
				if(infoData.value.status=="2" || infoData.value.status=="3" || infoData.value.status=="4"){
					boxDisable.value=false;
					boxed.value=false;
				}else{
					boxDisable.value=true;
					boxed.value=true;
				}
				if(infoData.value.transtyle){
				   form.tranType=infoData.value.transtyle;
				}
				getBoxDetial({"data":datas});
			}
			function stepclick(step){
				emit("stepdata",step);
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