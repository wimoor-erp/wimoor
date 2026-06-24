<template>
	<span v-if="hasShelf">
	<el-popover v-if="opt=='0'" placement="left"  title="下架" :width="340"  
	:ref="`popovershelf-${formid}`" 
	   @show="getOptionsData(warehouseid)"  >
	    <template #reference>
	       <el-button  type="primary"  link >下架 </el-button> 
	    </template>
		   <el-form>
			  <el-form-item label="库位选择"> 
			      <el-radio-group v-model="shelfdownid" v-if="invlist&&invlist.length>0" @change="handleRadioChange">
			        <el-radio 		style="margin-bottom:5px;" v-for="item in invlist"  :value="item.shelfid" :label="item.shelfid"  border> {{item.shelfname}}----{{item.amount}}</el-radio>
			      </el-radio-group>
			     <div v-else>无上架产品</div>
			  </el-form-item>
			  
			 <el-form-item label="下架数量">
				  <el-input type="number" v-model="shelfNum"></el-input>
			  </el-form-item>
			</el-form>
		 <el-button class="m-t-8" @click="submitShelfDown('popovershelf-'+formid,proxy)" type="primary">提交</el-button>
	  </el-popover>
	  <el-popover v-else  placement="left"  title="上架" :width="340"
	  :ref="`popovershelf-${formid}`" 
	     @show="getOptionsData(warehouseid)"  >
	      <template #reference>
	         <el-button  type="primary"  link >上架 </el-button> 
	      </template>
	  	   <el-form>
	  		  <el-form-item label="库位选择"> 
	  		    <el-cascader  :teleported = "false"  
	  			:options="optionsShelf" 
	  			:props="props1" 
	  			v-model="shelf" 
	  			filterable 
	  			clearable 
	  			placeholder="可搜索" />
	  		  </el-form-item>
	  		 <el-form-item label="上架数量">
	  			  <el-input type="number" v-model="shelfNum"></el-input>
	  		  </el-form-item>
	  		</el-form>
	  	 <el-button class="m-t-8" @click="submitShelfUp('popovershelf-'+formid,proxy)" type="primary">提交</el-button>
	    </el-popover>
	  <el-popover  placement="left"  title="上架" :width="340" trigger="click"  @show="loadShefRecord()" >
	      <template #reference>
	         <el-button  type="primary"    link   v-if="recordlist&&recordlist.length>0"  >  查看记录 </el-button> 
			 <el-button type="info" link v-else >  暂无记录 </el-button> 
        </template>
	 <el-table :data="recordlist" size="small" border>
	   	<el-table-column label="库位" >
	   		<template #default="scope">
	   		      {{scope.row.shelfname}}
	   		</template>
	   	</el-table-column>
	   	<el-table-column label="操作" prop="quantity">
	   		<template #default="scope">
	   			<span v-if="scope.row.opt==0">下架：</span>
	   			<span v-else>上架：</span>
	   			    {{scope.row.quantity}}
	   			<p class="font-extraSmall">
	   			    {{scope.row.opttime}}
	   			</p>
	   		</template>
	   	</el-table-column>
	   </el-table>
	 </el-popover>
	 </span>
    </template>
<script setup>
	import { reactive,ref,watch,onMounted,toRefs,getCurrentInstance} from "vue";
	import shelfproductApi from '@/api/erp/warehouse/shelfproductApi.js';
	import shelfApi from '@/api/erp/warehouse/shelf.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	const   proxy  = getCurrentInstance();
	const props1 = {
	  checkStrictly: true,
	  value:'id',
	  label:'fullname'
	}
	const state = reactive({
		recordlist:[],
		optionsShelf:[],
		invlist:[],
		shelfdownid:"",
		hasShelf:true,
		shelfNum:"",
		shelf:[],
	})
	const {
		recordlist,showshelf,optionsShelf,shelf,shelfNum,hasShelf,invlist,shelfdownid
	}=toRefs(state)
	let props = defineProps({
	 	                       formid:"",
							   warehouseid:"",
							   materialid:"",
							   formtype:"",
							   amount:"",
							   opt:"",
	                       });
	const { formid,formtype,warehouseid,materialid,amount,opt} = toRefs(props);
    onMounted(()=>{
		var timer=setTimeout(function(){
			loadShefRecord();
			getOptionsData(props.warehouseid);
			clearTimeout(timer);
		},500);
	})
	watch(props,function(){
		getOptionsData(props.warehouseid);
	})
	function handleRadioChange(value){
		state.shelfdownid=value;
	}
	var defaultshelf="";
	function getOptionsData(warehouseid){
		state.shelfNum=props.amount;
		if(state.recordlist&&state.recordlist.length>0){
			 shelfname(state.optionsShelf);
			 state.shelf=state.shelf.reverse();
			 return ;
		}
		if(props.opt=="0"){
			shelfproductApi.getShelfInventoryList({"warehouseid":warehouseid,"materialid":props.materialid}).then(res=>{
				state.invlist=res.data.records;
				if(state.invlist&&state.invlist.length>0){
				   state.shelfdownid=state.invlist[0].shelfid;
				}
			})
		}else{
			shelfApi.getWarehouseShelf(warehouseid).then(function(res){
							 if(res){
								 if(res.data&&res.data.length==1){
									 state.optionsShelf=res.data[0].children;
									 shelfname(state.optionsShelf);
									 state.shelf=state.shelf.reverse();
								 }else{
									 state.optionsShelf=[];
									 state.hasShelf=false;
								 }
				            }else{
								state.hasShelf=false;
							}
			}).catch(e=>{
				state.hasShelf=false;
			});
		}
		
	}
	
	function shelfname(value){
		if(value&&value.length>0){
			value.forEach(item=>{
					item.fullname=item.number+"-"+item.name;
					if(item.children){
						item.children.forEach(child=>{
							if(defaultshelf&&defaultshelf==child.id){
								state.shelf.push(child.id);
								defaultshelf=item.id;
							}
						})
						shelfname(item.children);
					} 
					if(defaultshelf&&defaultshelf==item.id){
						state.shelf.push(item.id);
					}
					
			})
		}
	}
	
	function loadShefRecord(){
		var data={};
		data.materialid=props.materialid;
		data.formid=props.formid;
		data.formtype=props.formtype;
		shelfproductApi.shelfInventoryOptProList(data).then(res=>{
			state.recordlist=res.data;
			if(res.data&&res.data.length){
				defaultshelf=state.recordlist[0].shelfid;
			}
		});
	}
	
	function submitShelfDown(key,proxy){
	var data=[];
		var item={};
			if(state.shelfdownid){
				 item.shelfid=state.shelfdownid;
			}else{
				ElMessage.error('必须选择库位');
				return;
			}
 
		item.materialid=props.materialid;
		item.formid=props.formid;
		item.formtype=props.formtype;
		item.quantity=state.shelfNum;
		item.opt="0";
		item.warehouseid=props.warehouseid;
		data.push(item);
		if(state.recordlist&&state.recordlist.length>0){
			var hasqty=0;
			state.recordlist.forEach(shelfrecord=>{
				hasqty=hasqty+parseInt(shelfrecord.quantity);
			});
		}
		if(hasqty+parseInt(state.shelfNum)>parseInt(props.amount)){
			ElMessageBox.confirm(
			   '您的下架数量将大于收货数量，请确认是否继续?',
			   'Warning',
			   {
			     confirmButtonText: '确认',
			     cancelButtonText: '取消',
			     type: 'warning',
			   }
			 )
			   .then(() => {
				   shelfproductApi.subShelfInventory(data).then(res=>{
				   	loadShefRecord();
				   	ElMessage.success('下架成功');
					var pop=proxy.refs[key];
					if(pop instanceof Array){
						pop[0].hide();
					}else{
						pop.hide();
					}
				   });
			   })
		}else{
			shelfproductApi.subShelfInventory(data).then(res=>{
				loadShefRecord();
				ElMessage.success('下架成功');
				var pop=proxy.refs[key];
				if(pop instanceof Array){
					pop[0].hide();
				}else{
					pop.hide();
				}
			});
		}
	
	}
	function submitShelfUp(key,proxy){
		var data=[];
		var item={};
		if(state.shelf&&state.shelf.length>0){
	 		item.shelfid=state.shelf[state.shelf.length-1];
		}else{
			ElMessage.error( '必须选择库位');
			return;
		}
		item.materialid=props.materialid;
		item.formid=props.formid;
		item.formtype=props.formtype;
		item.quantity=state.shelfNum;
		item.warehouseid=props.warehouseid;
		item.opt="1";
		data.push(item);
		if(state.recordlist&&state.recordlist.length>0){
			var hasqty=0;
			state.recordlist.forEach(shelfrecord=>{
				hasqty=hasqty+parseInt(shelfrecord.quantity);
			});
		}
		if(hasqty+parseInt(state.shelfNum)>parseInt(props.amount)){
			ElMessageBox.confirm(
			   '您的上架数量将大于收货数量，请确认是否继续?',
			   'Warning',
			   {
			     confirmButtonText: '确认',
			     cancelButtonText: '取消',
			     type: 'warning',
			   }
			 )
			   .then(() => {
				   shelfproductApi.addShelfInventory(data).then(res=>{
				   	loadShefRecord();
				   	ElMessage.success('上架成功');
					var pop=proxy.refs[key];
					if(pop instanceof Array){
						pop[0].hide();
					}else{
						pop.hide();
					}
				   });
			   })
		}else{
			shelfproductApi.addShelfInventory(data).then(res=>{
				loadShefRecord();
				ElMessage.success('上架成功');
				var pop=proxy.refs[key];
				if(pop instanceof Array){
					pop[0].hide();
				}else{
					pop.hide();
				}
			});
		}
	
	}
</script>

<style>
</style>