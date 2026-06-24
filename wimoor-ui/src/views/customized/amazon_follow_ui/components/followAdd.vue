<template>
	<el-dialog
	title="添加跟卖"
	v-model="visable"
	width="80%"
	top="4vh"
	>
	<el-tabs v-model="activeName"  type="card" @tab-click="handleClick">
	   <el-tab-pane label="单个上架" name="1">
		   <el-form 
		   :rules="rules"
		   v-model="form"
		   label-width="80px"
		   >
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="Asin" prop="asin" >
				   <el-input v-model="form.asin" ></el-input>
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="SKU">
				   <el-input v-model="form.sku" placeholder="不填系统自动生成"></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="店铺/站点" prop="market">
				   <el-select  style="width:100%;" filterable @change="getShipGroup()"  v-model="form.groupid"  >
				   	<el-option v-for="item in grouplist" :key="item.value"  :label="item.label" :value="item.value"></el-option>
				   </el-select>
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="跟卖时间" prop="time">
				  <el-select v-model="form.timeid"  filterable  style="width:100%;">
				  	<el-option v-for="item in timelist" :key="item.id"  :label="item.fullname"  :value="item.id">
				  	</el-option>
				  </el-select>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="6">
			   <el-form-item label="库存数量" prop="inventory">
				   <el-input v-model="form.fulfillable" @input="form.fulfillable=CheckInputInt(form.fulfillable)" placeholder="默认上架库存数量"></el-input>
				   
			   </el-form-item>
			   </el-col>
			   <el-col :span="6">
			   <el-form-item label="库存水位"  >
			   		  <el-input v-model="form.lowestquantity" @input="form.lowestquantity=CheckInputFloat(form.lowestquantity)" placeholder="达到当前值会自动补库存"></el-input>
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="发货天数" prop="day">
				  <el-input v-model="form.cycle" @input="form.cycle=CheckInputInt(form.cycle)" placeholder="订单的处理时间,一般填5"></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="是否去重"  >
			   		 <el-radio-group v-model="form.isrepeat" class="ml-4">
			   		      <el-radio label="1" >去重</el-radio>
			   		      <el-radio label="0" >不去重</el-radio>
			   		    </el-radio-group>
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="限购数量" >
			   			 <el-input placeholder="限制下单数量,可不填" v-model="form.maxorderqty" @input="form.maxorderqty=CheckInputInt(form.maxorderqty)" ></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="6">
			   <el-form-item label="初始价格" prop="inventory">
			   				   <el-input placeholder="初始上架价格" v-model="form.startprice" @input="form.startprice=CheckInputFloat(form.startprice)"></el-input>
			   				   
			   </el-form-item>
			   </el-col>
			   <el-col :span="6">
			   <el-form-item label="承受价"  >
			   				  <el-input placeholder="为0不调价" v-model="form.overprice" @input="form.overprice=CheckInputFloat(form.overprice)"></el-input>
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="运费模板"  >
				   <el-select  style="width:80%;" filterable   v-model="form.templateid"  >
				   	<el-option v-for="item in shipgrouplist" :key="item.id"  :label="item.name" :value="item.id"></el-option>
				   </el-select>
				   <el-button  style="width:20%;"  @click="refreshShipGroupList()" :loading="Templateloading">刷新</el-button>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="24">
			   <el-form-item label="备注"  >
			   		<el-input type="textarea" v-model="form.remark" ></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
		   </el-form>
	   </el-tab-pane>
	 <!--  /////////////////////// -->
	   <el-tab-pane label="多变体添加" name="2">
		   <div class="flex-center-between">
		   <el-space>
			 <el-select  style="width:100%;" filterable  @change="getShipGroup()"  v-model="form.groupid"  >
			 	<el-option v-for="item in grouplist" :key="item.value"  :label="item.label" :value="item.value"></el-option>
			 </el-select>
		     <el-input   v-model="form.asin" placeholder="请输入要查询的ASIN"></el-input>
			 <el-button type="primary" @click="handleFindAsin">查询</el-button>
		   </el-space>
		   <el-button @click="handleClear()">清空</el-button>
		   </div>
		   <el-row :gutter="24" style="padding-top:20px;padding-bottom:20px;">
		   <el-col :span="6">
			   <el-card  :body-style="{ padding: '0px' }">
				<!-- 左表开始 -->
			   <el-table :data="LeftTableData" v-loading="asinloading">
				   <!-- 数据栏开始 -->
				   <el-table-column   >
					    <template #header>
							变体Asin <el-button link   type="primary" size="small" @click="handleAddAllChildren">全部添加</el-button>
							
						</template>
					   <template #default="scope">
						   <el-space>
						      <img style="width:40px;height:40px" :src="scope.row.link"/>
							<div>
								<div :class="{'text-orange':scope.row.self}">{{scope.row.asin}}
								<el-link v-if="scope.row.summaries" v-for="sitem in scope.row.summaries" target="_blank" type="" :href="sitem.websiteDisplayGroup" :underline="false">
								<el-icon class="font-medium" :title="sitem.itemName" >
									<Link /></el-icon>
								</el-link>
								</div>
								<p class="font-extraSmall">Size: <span>{{scope.row.size}} </span><span> Color:{{scope.row.color}}</span>
								</p>		
							</div>
							</el-space>
					   </template>
				   </el-table-column>
				    <!-- 数据栏结束 -->
					
				   <!--  操作栏开始 -->
				   <el-table-column width="60" label="操作">
					   <template #default="scope">
						   <el-button type="primary" @click="handleAdd(scope.row)" link>添加</el-button>
					   </template>
				   </el-table-column>
				    <!--  操作栏结束 -->
			   </el-table>
			   <!-- 左表结束 -->
			   
			   <el-button v-if="asininfoloading" :loading="asininfoloading" link>数据加载中……</el-button>
			   </el-card>
		   </el-col>
		   <el-col :span="17" style="margin-left: 10px;">
			   <el-card  :body-style="{ padding: '0px' }">
		   	   <el-table :data="RightTableData">
				   <el-table-column label="Asin" prop="asin" show-overflow-tooltip width="220">
					  <template #default="scope">
						  <el-space>
							  <img style="width:40px;height:40px"  :src="scope.row.link"/>
							  <div>
								  <div  >{{scope.row.asin}}</div>
								  <div class="font-extraSmall">{{scope.row.name}}</div>
							  </div>
						  </el-space>
					     
				   </template>
                  </el-table-column>
				   <el-table-column label="尺寸" width="180">
					   <template #default="scope">
					   	    <p class="font-extraSmall">Size:
					   		   <span>{{scope.row.size}} </span>
							   <span> Color:{{scope.row.color}}</span>
					   		 </p>			
					   </template>
				   </el-table-column>
				   <el-table-column label="SKU">
					   <template #default="scope">
					     <el-input v-model="scope.row.sku"  placeholder="不填系统自动生成"></el-input>
						 </template>
				   </el-table-column>
				   <el-table-column label="初始价"  >
					   <template #default="scope">
						   <el-space>
					     <el-input v-model="scope.row.startprice"  @input="scope.row.startprice=CheckInputFloat(scope.row.startprice)" placeholder="初始上架价格"></el-input>
						<el-icon @click="handleFill(scope.row.startprice,scope.$index,'startprice')" class="font-medium text-info pointer"><Bottom /></el-icon>
						</el-space>
					   </template>
				   </el-table-column>
				   <el-table-column label="承受价"   >
					   <template #default="scope">
						   <el-space>
					     <el-input v-model="scope.row.overprice" @input="scope.row.overprice=CheckInputFloat(scope.row.overprice)" placeholder="为0不调价"></el-input>
						 <el-icon @click="handleFill(scope.row.overprice,scope.$index,'overprice')" class="font-medium text-info pointer"><Bottom /></el-icon>
						 </el-space>
					   </template>
				   </el-table-column>
				   <el-table-column label="操作" width="70">
					   <template #default="scope">
					   		 <el-button type="primary" @click="handleDelete(scope.$index)" link>删除</el-button>
					   </template>
				   </el-table-column>
		   	   </el-table>
			   </el-card>
		   </el-col>
		   </el-row>
		  <el-form
		  :rules="rules"
		  v-model="form"
		  label-width="80px"
		  >
		  			   <el-row :gutter="24">
		  			   <el-col :span="12">
						<el-form-item label="跟卖时间" prop="time">
										  <el-select v-model="form.timeid"  filterable  style="width:100%;">
											<el-option v-for="item in timelist" :key="item.id"  :label="item.fullname"  :value="item.id">
											</el-option>
										  </el-select>
						</el-form-item>
		  			   </el-col>
					   
					   <el-col :span="6">
					   <el-form-item label="库存数量" prop="inventory">
					   		  <el-input v-model="form.fulfillable" @input="form.fulfillable=CheckInputInt(form.fulfillable)" placeholder="默认上架库存数量"></el-input>
					   </el-form-item>
					    </el-col>
					    <el-col :span="6">
					    <el-form-item label="库存水位"  >
					    		<el-input v-model="form.lowestquantity" @input="form.lowestquantity=CheckInputFloat(form.lowestquantity)" placeholder="达到当前值会自动补库存"></el-input>
					    </el-form-item>
					    </el-col>
		  			   </el-row>

					   
					   <el-row :gutter="24">
		  			   <el-col :span="6">
		  			   <el-form-item label="发货天数" prop="day">
		  			   		<el-input v-model="form.cycle" @input="form.cycle=CheckInputInt(form.cycle)" placeholder="订单的处理时间,一般填5"></el-input>
		  			   </el-form-item>
		  			   </el-col>
					   <el-col :span="6">
					   <el-form-item label="限购数量" prop="num">
					   		<el-input v-model="form.maxorderqty" @input="form.maxorderqty=CheckInputInt(form.maxorderqty)" placeholder="限制下单数量,可不填"></el-input>
					   </el-form-item>
					   </el-col>

		  			   <el-col :span="6">
		  			   <el-form-item label="运费模板"  >
		  			   		  <el-select  style="width:80%;" filterable   v-model="form.templateid"  >
		  			   		  	<el-option v-for="item in shipgrouplist" :key="item.id"  :label="item.name" :value="item.id"></el-option>
		  			   		  </el-select>
							  <el-button style="width:20%;" @click="refreshShipGroupList()" :loading="Templateloading">刷新</el-button>
		  			   </el-form-item>
		  			   </el-col>
					     <el-col :span="6">
		  			    <el-form-item label="是否去重"  >
		  			    		 <el-radio-group v-model="form.isrepeat" class="ml-4">
		  			    		      <el-radio label="1" >去重</el-radio>
		  			    		      <el-radio label="0" >不去重</el-radio>
		  			    		    </el-radio-group>
		  			    </el-form-item>
						 </el-col>
						 </el-row>
						 
		  			   <el-row :gutter="24">
		  			   <el-col :span="24">
		  			   <el-form-item label="备注" prop="day">
		  			   		<el-input type="textarea" v-model="form.remark" ></el-input>
		  			   </el-form-item>
		  			   </el-col>
		  			   </el-row>
		  </el-form> 
	   </el-tab-pane>
	 </el-tabs>
	<template #footer>
		<el-button @click="visable=false">取消</el-button>
		<el-button type="primary" @click="submitForm" :loading="submitloading">提交</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,}from"vue";
	import {Link,Bottom,} from '@element-plus/icons-vue';
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import {CheckInputFloat,CheckInputInt,formatFloat} from '@/utils/index';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	import followTimeApi from '@/views/customized/amazon_follow_ui/js/followTimeApi.js';
	import authApi from '@/api/amazon/auth/authApi.js';
	import mfnApi from '@/api/amazon/listing/mfnApi.js';
	import {findAsin,findAsinInfo} from '@/api/amazon/listing/listingApi.js';
	const emit = defineEmits(['change']);
	const state=reactive({
		timelist:[],
		grouplist:[],
		asinloading:false,
		LeftTableData:[],
		RightTableData:[],
		shipgrouplist:[],
		visable:false,
		submitloading:false,
		Templateloading:false,
		asininfoloading:false,
		activeName:'1',
		rules:{
		asin:[
			// { required: true, message: 'asiN不能为空！', trigger: 'blur' },
		],
		},
		form:{
			asin:'',
			sku:'',
			radio:'1',
			isrepeat:'1',
			fulfillable:20,
			lowestquantity:10,
			cycle:5,
			templateid:'',
		},
	})
	const {
		timelist,
		grouplist,
		LeftTableData,
		asinloading,
		RightTableData,
		asininfoloading,
		submitloading,
		Templateloading,
		shipgrouplist,
		form,
		rules,
		visable,
		activeName,
	}=toRefs(state)
	
	function show(row){
		state.visable = true;
    if(row){
      state.form.asin=row.asin
	  state.form.remark="选品库导入"
    }
		followTimeApi.list().then((res)=>{
			if(res.data){
				res.data.forEach(item=>{
					if(!item.endtime){
						item.fullname=item.name+" ("+item.starttime+"--"+")";
					}else{
						item.fullname=item.name+" ("+item.starttime+"-"+item.endtime+")";
					}
				});
				state.timelist=res.data;
				state.form.timeid=res.data[0].id;


			}
			
		});
		authApi.getAuthMaketplace().then((res)=>{
			if(res.data){
				state.grouplist=res.data;
				state.form.groupid=res.data[0].value;
				getShipGroup();
			}
		});
	}
	function refreshShipGroupList(){
		state.Templateloading=true;
		var data={};
		data.marketplaceids=[];
		data.marketplaceids.push(state.form.groupid.split("-")[0]);
		data.amazonauthid=state.form.groupid.split("-")[1];
		mfnApi.refreshShipGroup(data).then(res=>{
			if(res.data){
				res.data.push({id:"",name:"不设置"});
			}else{
				res.data={id:"",name:"不设置"};
			}
			state.shipgrouplist=res.data;
			state.Templateloading=false;
			ElMessage({
			  type: 'success',
			  message: '获取成功!',
			})
			
		});
	}
	
	function getShipGroup(){
		var data={};
		data.marketplaceids=[];
		data.marketplaceids.push(state.form.groupid.split("-")[0]);
		data.amazonauthid=state.form.groupid.split("-")[1];
		mfnApi.listShipGroup(data).then(res=>{
			if(res.data){
				res.data.push({id:"",name:"不设置"});
			}else{
				res.data={id:"",name:"不设置"};
			}
			state.shipgrouplist=res.data;
		});
	}
	async function submitForm(){
		if(!state.form.asin || state.form.asin=="" || state.form.asin==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写ASIN!',
			})
			return;
		}
		if(!state.form.cycle || state.form.cycle=="" || state.form.cycle==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写发货天数!',
			})
			return;
		}
		if(!state.form.fulfillable || state.form.fulfillable=="" || state.form.fulfillable==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写库存!',
			})
			return;
		}
		
		if(state.form.groupid.indexOf("-")>=0){
			state.form.marketplaceid=state.form.groupid.split("-")[0];
			state.form.amazonauthid=state.form.groupid.split("-")[1];
		}
		var data=[];
		if(state.activeName=="1"){
			if(!state.form.startprice || state.form.startprice=="" || state.form.startprice==undefined){
				ElMessage({
				  type: 'error',
				  message: '必须填写初始价格!',
				})
				return;
			}
			// if(state.form.overprice==null || state.form.overprice=="" || state.form.overprice==undefined){
			// 	ElMessage({
			// 	  type: 'error',
			// 	  message: '必须填写承受价格!',
			// 	})
			// 	return;
			// }
			data.push(state.form);
		}else{
			var haserror=false;
			state.submitloading=true;
			state.RightTableData.forEach(item=>{
				if(!item.startprice || item.startprice=="" || item.startprice==undefined){
					ElMessage({
					  type: 'error',
					  message:item.asin+ '必须填写初始价格!',
					});
					state.submitloading=false;
					haserror=true;
				}
				
				// if(item.overprice==null || item.overprice=="" || item.overprice==undefined){
				// 	ElMessage({
				// 	  type: 'error',
				// 	  message: item.asin+'必须填写承受价格!',
				// 	});
				// 	haserror=true;
				// }
				state.form.asin=item.asin;
				state.form.sku=item.sku;
				state.form.startprice=item.startprice;
				state.form.overprice=item.overprice;
				state.form.image=item.link;
				data.push(JSON.parse(JSON.stringify(state.form)));
			});
			if(haserror==true){
				return;
			}
		}
		for(var i=0;i<data.length;i++){
			var mydata=[data[i]];
			await followListApi.saveProfuctFollow(mydata).then((res)=>{});
			
		}
		
		ElMessage({
		  type: 'success',
		  message: '添加成功!',
		})
		state.visable = false;
		emit("change");
		state.submitloading=false;
		
	}
	
	
	
	
	
	defineExpose({
		show,
	})
	function handleFill(value,index,field){
		state.RightTableData.forEach((item,i)=>{
			if(i>index){
				item[field] = value;
			}
		})
	}
	function handleClear(){
		state.RightTableData=[];
		state.LeftTableData=[];
	}
	function handleDelete(index){
		state.RightTableData.splice(index,1);
	}
	function handleAddAllChildren(){
		state.LeftTableData.forEach(row=>{
			handleAdd(row);
		})
	}
	function handleAdd(row){
		var isrepeat=false;
		state.RightTableData.forEach(item=>{
			if(item.asin==row.asin){
				isrepeat=true;
			}
		})
		if(isrepeat){
			ElMessage({
			  type: 'warning',
			  message: '请勿重复添加!',
			})
			state.submitloading=false;
			return;
		}else{
		   state.RightTableData.push(row);
		   
		}
	}
	async function handleFindAsin(){
		if(!state.form.asin || state.form.asin=="" || state.form.asin==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写ASIN!',
			})
			return;
		}
		var data={};
		if(state.form.groupid.indexOf("-")>=0){
			state.form.marketplaceid=state.form.groupid.split("-")[0];
			state.form.amazonauthid=state.form.groupid.split("-")[1];
		}
		state.form.marketplaceids=[];
		state.form.asin=state.form.asin.trim();
		state.form.marketplaceids.push(state.form.marketplaceid);
		state.asinloading=true;
		
		await findAsin(state.form).then(async res=>{
			state.asinloading=false;
			 state.LeftTableData=[];
			var asins=[];
			if(res.data){
				for(var i=0;i<res.data.length;i++){
						asins.push(res.data[i]);
					if(asins.length>=20){
						state.form.asins=asins;
						state.asininfoloading=true;
						await findAsinInfo(state.form).then(subres=>{
							subres.data.forEach(item=>{
								if(item.asin==state.form.asin){
									item.self=true;
								}else{
									item.self=false;
								}
								handAsinRowImage(item);
								state.LeftTableData.push(item);
							})
							state.asininfoloading=false;
							asins=[];
						})
					}
				}
				if(asins.length>0){
					state.asininfoloading=true;
					state.form.asins=asins;
					await findAsinInfo(state.form).then(res=>{
						res.data.forEach(item=>{
							if(item.asin==state.form.asin){
								item.self=true;
							}else{
								item.self=false;
							}
							handAsinRowImage(item);
							state.LeftTableData.push(item);
						});
						state.asininfoloading=false;
						asins=[];
					})
				}
			}
			 
		}).catch(e=>{
			state.asinloading=false;
		});
	}
	function handAsinRowImage(row){
		row.asin=row.asin;
		row.size="";
		row.color="";
		if(row.summaries&&row.summaries.length>0){
			row.size=row.summaries[0].size;
			row.color=row.summaries[0].color;
			row.name=row.summaries[0].itemName;
		}
		if(row.images&&row.images.length>0){
				var imagelist=row.images[0].images;
				var main=null;
				var minmain=1000000;
				for(var i=0;i<imagelist.length;i++){
					var image=imagelist[i];
					if(image.variant=='MAIN'){
						if(image.height==75||image.width==75){
							row.link=image.link;break;
						}
						if(image.height<minmain){
							minmain=image.height;
							main=image.link;
						}
					}
				}
				if(!row.link){
					if(main){
						row.link=main;
					}else{
						row.link=imagelist[0].link;
					}
				}
		}
	}
</script>

<style scoped>
	 
</style>