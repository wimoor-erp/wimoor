<template>
	<el-dialog v-model="dialog.visible" title="货件拆分" destroy-on-close='true' width="80%" @close='closeDialog'>
			<div class="sh-con-tw ">
				<div class="sh-ri">
					<el-table :data="splittableData" border>
						<el-table-column label="图片" width="68">
							<template #default="scope">
								<el-image :src="scope.row.image" class="product-img">
								</el-image>
							</template>
						</el-table-column>
						<el-table-column label="名称/SKU" :show-overflow-tooltip="true" prop="sellersku" sortable>
								<template #default="scope">
									<div >{{scope.row.name}}</div>
									<div class="sku">{{scope.row.sku}}</div>
								</template>
						</el-table-column>
						<el-table-column label="发货数量" width="100" prop="copshipnum" sortable>
							<template #default="scope">
							<div class="table-edit-flex  ">
							<span>{{scope.row.copshipnum}}</span>
							<el-popover v-if="!scope.row.issub"  :visible="scope.row.itemshow" placement="top" width="180px" trigger="click" title="拆分发货数量" >
							<template  #reference>	
							<el-icon @click.stop="showPopver(scope.row)"><Edit /></el-icon>
							</template>
							<el-input-number   v-model="scope.row.rowsplitnum" placeholder="发货数量"  />
							<el-button  class="m-t-8"  @click="scope.row.itemshow=false" >取消</el-button>
							<el-button  class="m-t-8" type="primary" @click="splitNumber(scope.row,scope.$index)" >确定</el-button>
							</el-popover>
							<el-icon v-else @click="deleteRow(scope.row,scope.$index)"><Delete/></el-icon>
							</div>
							</template>
						</el-table-column>
						<el-table-column label="材积重" width="90" prop="dimweightsum" sortable>
							<template #default="scope">
									<div >{{scope.row.dimweightsum}}
									<span class="font-extraSmall"> kg</span>
									<div >{{scope.row.dimweight}} <span class="font-extraSmall"> kg/1</span>
									</div>
								   </div>
							</template>
						</el-table-column>
						<el-table-column label="体积" width="110" prop="volumesum" sortable>
							<template #default="scope">
									<div >{{scope.row.volumesum}}<span class="font-extraSmall"> m³</span>
									<div > {{formatFloat(parseFloat(scope.row.skuvolume)*1000000)}} <span class="font-extraSmall"> cm³/1</span>
									</div>
									</div>
							</template>
						</el-table-column>
						<el-table-column label="重量" width="90" prop="weightsum" sortable>
							<template #default="scope">
									<div v-if="scope.row.weight">{{scope.row.weightsum}}
									<span class="font-extraSmall"> kg</span>
									<div > {{scope.row.weight}} <span class="font-extraSmall"> kg/1</span></div>
									</div>
									
									<div v-else>0kg</div>
							</template>
						</el-table-column>
						<el-table-column label="分组"   width="190">
							<template #default="scope">
							<el-radio-group v-model="scope.row.checkGroup" @change="groupSwitch(scope.row)" >
								 <el-radio 
								v-for="item in scope.row.groups"
								:key="item.value"
								:label="item.value"
								:value="item.value"
								 >{{item.label}}
								</el-radio>
							  </el-radio-group>
							</template>
						</el-table-column>
					</el-table>
				</div>
				<div class="sh-le">
					   <h4 class="ma-bo-16">拆分结果</h4>
					    <div  v-for="item in splitResult">
						<el-divider></el-divider>	
						<el-space>
							<h5>{{item.name}}</h5>
							<el-tag size="small" type="primary" v-if="item.group=='6'">无</el-tag>
							 <el-tag size="small" type="primary" v-else>
									组{{item.group}}</el-tag>
							<el-tag v-if="false" size="small" type="danger">重现提交</el-tag>
						</el-space>
						<div>
						 <el-space :size="64">
							 <div>
								 <span class="font-extraSmall">发货重量</span>
								 <p>{{formatFloat(item.weight)}} kg</p>
							 </div>
							 <div>
								 <span class="font-extraSmall">体积</span>
								 <p>{{formatFloat(item.volume)}} m³</p>
							 </div>
							 <div>
							 	  <span class="font-extraSmall">材积重</span>
							 	  <p>{{formatFloat(item.dimweight)}} kg</p>
							 </div>
						 </el-space>
						 	 </div>
							 <div>
						 <el-space :size="64">
							 <div>
							 	  <span class="font-extraSmall">SKU数量</span>
							 	  <p>{{item.skunum}}</p>
							 </div>
							 <div>
							 	 <span class="font-extraSmall">发货数量</span>
							 	  <p>{{item.shipnum}}</p>
							 </div>
							 <div>
								 <span class="font-extraSmall">状态</span>
								 <div>
									 <div v-if="item.group=='6'">
										  不提交 
									 </div>
									 <div v-else>
									 <el-tag v-if="item.splitstr==0" size="small" type="info">待提交</el-tag>
									 <el-tag v-if="item.splitstr==1" size="small" type="warning">提交中...</el-tag>
									 <el-tag v-if="item.splitstr==2" size="small" type="success">已提交</el-tag>
									 <el-tag v-if="item.splitstr==3" 
									       @click="submitPlan(item)" size="small" type="danger">(提交失败)点击重新提交</el-tag>
									  </div>
								</div>
							 </div>
							
						 </el-space>
						 <el-row v-if="item.group!='6'" style="padding-top:10px;">
							<el-col :span="4" class="font-extraSmall">备注:</el-col>
							<el-col :span="20" > 
							<el-input size="small" placeholder="请在确认分组后填写备注(调整分组备注将重置)" v-model="item.remark" clearable></el-input> 
							</el-col>
						 </el-row>
						 </div>
					</div>
				</div>
			</div>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="dialog.visible = false">取消</el-button>
				<el-button type="primary" @click="saveSplit"  :loading="submitloading">提交</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,computed} from "vue"
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import {formatFloat} from '@/utils/index.js';
	import {Plus,Edit,Delete} from '@element-plus/icons-vue'
    import {ElMessage } from 'element-plus'
	const state = reactive({
	  // 弹窗属性
	  dialog: { visible: false }  ,
	  splittableData:[],
	  nowshipment:"",
	  planData:{},
	  splitResult:[],
	  submitloading:false,
	});
	const {
	  splittableData,
	  dialog,
	  planData,
	  nowshipment,
	  splitResult,
	  submitloading,
	} = toRefs(state);
	function groupSwitch(row){
		computedCheckGroup();
	}
    function deleteRow(row,index){
		var hasback=false;
		state.splittableData.forEach(item=>{
			if(hasback==false){
				if(item.sku==row.sku&&!item.issub){
					item.copshipnum=item.copshipnum+row.copshipnum;
					hasback=true;
				}
			}
		})
		state.splittableData.splice(index,1);
		computedCheckGroup();
	}	 
	function showPopver(row){
		row.itemshow=true;
	}
	function splitNumber(row,index){
		if(row.rowsplitnum){
			var newrow = JSON.parse(JSON.stringify(row));
			newrow.copshipnum=row.rowsplitnum;
			if(row.copshipnum-row.rowsplitnum<=0){
				ElMessage.error('输入数量不能大于原有数量！');
				return;
			}
			newrow.issub=true;
			newrow.itemshow=false;
			row.copshipnum=row.copshipnum-row.rowsplitnum;
			row.itemshow=false;
			if(state.splittableData){
				state.splittableData.splice(index+1,0,newrow);
			}
			computedCheckGroup();
		}else{
		    row.itemshow=false;
			return ;
		}
		 
		
	}
	async function saveSplit(){
		for(var i=0;i<state.splitResult.length;i++){
			var item=state.splitResult[i];
			await submitPlan(item);
		}
	}
	function computedCheckGroup(){
			var nowDate=new Date();
			let arr=[]
			let checkGroups = state.splittableData.map((item)=>{
				return item.checkGroup
			})
			checkGroups = Array.from(new Set(checkGroups))
			checkGroups.forEach((checkGroup,index)=>{
				let sameGroupArr = state.splittableData.filter((item)=>item.checkGroup === checkGroup)
				 arr[index] = sameGroupArr
			})
			
			let newArr=[]
			arr.forEach((group,index)=>{
				let obj={}
				obj.shipnum = 0;
				obj.weight = 0;
				obj.dimweight = 0;
				obj.volume = 0;
				obj.skunum = 0;
				obj.splitstr=0;
				var tablist=[];
				group.forEach((item)=>{
					var tab={};
					if(item.checkGroup=="6"){
						obj.name="不提交";
					}else{
					    obj.name="PLN"+"("+(nowDate.getMonth()+1)+"/"+(nowDate.getDate())+"/"+nowDate.getFullYear()+" "+nowDate.getHours()+":"+nowDate.getMinutes()+")-"+item.checkGroup;
					}
					tab.sku=item.sku;
					tab.msku=item.msku;
					tab.quantity=item.copshipnum;
					tab.mid=item.materialid;
					obj.shipnum += item.copshipnum;
					obj.weight += item.weight*item.copshipnum;
					obj.volume += (item.skuvolume)*item.copshipnum;
					obj.dimweight += parseFloat(item.dimweight)*item.copshipnum;
					obj.skunum++
					obj.group = item.checkGroup;
					if(state.nowshipment.remark){
					   obj.remark=state.nowshipment.remark;
					}else{
					   obj.remark=state.planData.remark;
					}
					tablist.push(tab);
				})
				obj.skulist=tablist;
				newArr.push(obj)
			})
			 newArr.sort(function (m, n) {
			 if (m.group < n.group) return -1
			 else if (m.group > n.group) return 1
			 else return 0
			});
		 state.splitResult=newArr;
	}
	function show(data,planData,nowshipment){
		var mydata=  JSON.parse(JSON.stringify(data));
		mydata.forEach(row=>{
			row.itemshow=false;
			row.copshipnum=row.quantity;
			row.checkGroup='6';
			row.groups=[{label:'组1',value:'1'},
							{label:'组2',value:'2'},
							{label:'组3',value:'3'},
							{label:'组4',value:'4'},
							{label:'组5',value:'5'},
							{label:'无',value:'6'},
						];
			row.dimweightsum=formatFloat(row.dimweight*row.copshipnum);
			row.volumesum=formatFloat(row.skuvolume*row.copshipnum);
			row.weightsum=formatFloat(row.weight*row.copshipnum);
		})
		state.splittableData=  JSON.parse(JSON.stringify(mydata));
		state.planData=JSON.parse(JSON.stringify(planData));
		state.nowshipment=nowshipment;
		computedCheckGroup();
		state.dialog.visible=true;
	}
	defineExpose({show});
	async function submitPlan(item){
			var itemlist=[];
			var params={};
			params.groupid=state.planData.amazongroupid;
			params.amazongroupid=state.planData.amazongroupid;
			params.planmarketplaceid=state.planData.marketplaceid;
			params.planid=null;
			params.batchnumber=null;
			params.issplit=false;
			params.name=item.name;
			params.sourceAddress=state.planData.addressid;
			params.remark=item.remark;
			params.plansubid=null;
			params.warehouseid=state.planData.warehouseid;
			params.marketplaceid=state.planData.marketplaceid;
			item.skulist.forEach(function(item){
					 var row=item;
					 row.QuantityShipped=item.quantity;
					 row.materialid=item.mid;
					 row.sellersku=item.sku;
					 itemlist.push(row);
			});
			params.planitemlist=itemlist;
			submitloading.value=true;
		
			 
			if(item.group=='6'){
				return;
			}
			await shipmentplanApi.saveInboundPlan(params).then((res)=>{
					ElMessage.success('提交成功！');
					state.submitloading=false;
					item.splitstr=2;
			}).catch(e=>{
				    item.splitstr=3;
					state.submitloading=false;
				    ElMessage.success('提交失败！');
			})
	}
</script>

<style>
 
</style>