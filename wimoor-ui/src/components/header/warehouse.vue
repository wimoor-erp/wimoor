<template>
	<el-space v-if="isAll">
		<el-cascader
		      v-model="warehouseCheck"
		      :options="warehouseData"
			    :value = "id"
			    :label="name"
		        :props="myprops"
		      @change="getWarehouse"
			     placeholder="全部仓库"
			     :clearable="defaultValue!='only'"
		    />
	</el-space>
	<el-space v-else-if="isform">
		<span v-if="warehouseType">
			<el-select v-model="warehouseid"   style="width: 200px"   placeholder="全部仓库" @change="warehouseChange">
			      <el-option  v-for="item in warehouseList"   :key="item.id"  :label="item.name" :value="item.id"   >
			      </el-option>
			</el-select>
		</span>
		<span v-else>
			<div class="select-group">
				<el-select v-model="ftype" style="width:100px" class="select-group-left"  placeholder="本地仓" @change="getWarehouseData">
					  <el-option  key="self" label="本地仓" value="self"> </el-option>
					  <el-option  key="oversea" label="海外仓" value="oversea"   > </el-option>
				</el-select>
				<el-select v-model="warehouseid" class="select-group-right"  style="width: 200px"    placeholder="全部仓库" @change="warehouseChange">
					  <el-option  v-for="item in warehouseList"   :key="item.id"  :label="item.name" :value="item.id"   >
					  </el-option>
				</el-select>
			</div>
		</span>
	</el-space>
	 <el-select v-else v-model="warehouseid"  style="width: 200px"    placeholder="全部仓库" @change="warehouseChange">
	       <el-option  v-for="item in warehouseList"   :key="item.id"  :label="item.name" :value="item.id"   >
	       </el-option>
	 </el-select>
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js'
	import { ref,reactive,onMounted,watch ,toRefs} from 'vue'
    const emit = defineEmits(["changeware",'change']);
	let props = defineProps({ defaultValue:"" ,warehouseCheck:"",isform:"" ,isAll:"",defaultText:"",defaultid:"",warehouseType:""})
	const { defaultValue ,isform,isAll ,defaultid} = toRefs(props);
	let warehouseList =ref([]);
	let state =reactive({ warehouseData:[], warehouseCheck:'', });
	let{ warehouseData,warehouseCheck}=toRefs(state);
	const myprops = {    value:'id',  label:'name', }
	let warehouseid =ref("");
	let ftype =ref("self");
			onMounted(()=>{
				getWarehouseData()
			})
			watch(props,()=>{
				getWarehouseData();
			})
			//获取仓库列表
			function getWarehouse(val,type){
				var warehouseid =''
				if(val&&val.length>1){
					state.warehouseData.forEach((item)=>{
						if(item.children && item.children.length>0){
							item.children.forEach((sub)=>{
								if(sub.id == val[1]){
									warehouseid =sub.id;
								}
							})
						}
					})
				}else{
					warehouseid =''
				}
				state.warehouseCheck=warehouseid;
				//每次改变仓库 变换list
				if(warehouseid!=''){
							if(warehouseid=="#"){
								warehouseid="";
								state.warehouseCheck=warehouseid;
							}
				}
				var warehouse={};
				state.warehouseData.forEach((item)=>{
					if(item.children && item.children.length>0){
						item.children.forEach((sub)=>{
							if(sub.id == state.warehouseCheck){
								warehouse =sub;
							}
						})
					}
				})
				emit("changeware",state.warehouseCheck,type,warehouse);
				emit("change",state.warehouseCheck,type,warehouse)
			}
			  function getWarehouseData(){
				   warehouseid.value="";
				   if(props.isAll===true){
					   warehouseApi.getWarehouseList().then(function(res){
						 if(props.defaultValue!="only"){ 
					   	    res.data.push({name:"全部仓库",id:"#",children:[{name:"全部仓位",id:"#"}]});
						}
					   	state.warehouseData = res.data;
					   	if(res.data && res.data.length>0){
							var warehouse=res.data[0].children[0];
					   		state.warehouseCheck=res.data[0].children[0].id;
					   		emit("changeware",state.warehouseCheck,"load",warehouse);
					   		emit("change",state.warehouseCheck,"load",warehouse)
					   	}
					   })
				   } else if(props.warehouseType){
					  warehouseApi.getWarehouse({"ftype":props.warehouseType}).then(res=>{
						  if(props.defaultValue!="only"){
							  if(props.defaultText){
								  res.data.push({"id":"","name":props.defaultText})
							  }else{
								 res.data.push({"id":"","name":"全部自有仓"}) 
							  }
						  }
						  warehouseList.value=res.data;
						  if(res.data&&res.data.length>0){
						      if(props.defaultValue=="only"){
						      	 warehouseid.value=res.data[0].id;
						      }
						  	if(props.defaultid!=undefined && props.defaultid!="" && props.defaultid!=null){
						  		 warehouseid.value=props.defaultid;
						  	}
						  	warehouseChange(warehouseid.value,"load");
						  }
					  });
				  }else{
					  if(ftype.value=="oversea"){
					  					  warehouseApi.getOversaWarehouseUseable().then((res)=>{
					  					  	if(props.defaultValue!="only"){
												if(props.defaultText){
												  res.data.push({"id":"","name":props.defaultText})
												}else{
												  res.data.push({"id":"","name":"全部自有仓"}) 
												}
					  					  	}
					  					  	warehouseList.value=res.data;
					  					  	if(res.data&&res.data.length>0){
					  					  	    if(props.defaultValue=="only"){
					  					  	    	 warehouseid.value=res.data[0].id;
					  					  	    }
					  							if(props.defaultid!=undefined && props.defaultid!="" && props.defaultid!=null){
					  								 warehouseid.value=props.defaultid;
					  							}
					  					  		warehouseChange(warehouseid.value,"load");
					  					  	}
					  					  })
					  }else{
					  					  warehouseApi.getWarehouseUseable().then((res)=>{
					  					  	if(props.defaultValue!="only"){
												if(props.defaultText){
													res.data.push({"id":"","name":props.defaultText})
												}else{
													res.data.push({"id":"","name":"全部自有仓"}) 
												}
					  					  	}
					  					  	warehouseList.value=res.data;
					  					  	if(res.data&&res.data.length>0){
					  					  	    if(props.defaultValue=="only"){
					  					  	    	 warehouseid.value=res.data[0].id;
					  					  	    }
					  							if(props.defaultid!=undefined && props.defaultid!="" && props.defaultid!=null){
					  								 warehouseid.value=props.defaultid;
					  							}
					  					  		warehouseChange(warehouseid.value,"load");
					  					  	}
					  					  })
					  }
					  				
				  }
				  
			}
			
			function warehouseChange(val,type){
				  var warehouse={};
				  warehouseList.value.forEach(row=>{
					  if(row.id==val){
						  warehouse=row;
					  }
				  })
				 emit("changeware",val,type,warehouse);
				 emit("change",val,type,warehouse)
			}
		 
			defineExpose({
				warehouseid, 
			})
		 
</script>

<style>
	 
	.select-group .select-group-left .el-select__wrapper{
      border-right: none;
			border-top-right-radius: 0px !important;
			border-bottom-right-radius: 0px !important;

	}
	.select-group .select-group-right .el-select__wrapper{
		border-top-left-radius:0px !important;
		border-bottom-left-radius:0px !important;
		border-left: none;

}
</style>
