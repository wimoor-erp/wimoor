<template>
	<div>
		<el-dialog
		    v-model="dialogVisible"
		    :title="dialogTitle"
		    width="40%"
		    :before-close="handleClose"
		  >
		    <el-form
		       ref="ruleFormRef"
		       :model="ruleForm"
		       :rules="rules"
		       label-width="120px"
		       class="demo-ruleForm"
		       :size="formSize"
		       status-icon
		     >
			 <el-form-item label="店铺" prop="groupid" required>
				 <el-space>
			   <el-select ref="amzgroup" v-model="ruleForm.groupid"  @change="groupChange"  placeholder="店铺">
			    <el-option   v-for="item in groupList"   
				            :key="item.id"  
							:label="item.name" 
							:value="item.id"   >
			    </el-option>
			   </el-select>
			   <el-select ref="amzgroup" v-model="ruleForm.marketplaceid"    placeholder="站点">
			    <el-option   v-for="item in marketList"   
							:key="item.marketplaceid"  
							:label="item.name" 
							:value="item.marketplaceid"   >
			    </el-option>
			   </el-select>
			   </el-space>
			 </el-form-item>
		       <el-form-item label="名称" prop="name" placeholder="可填店铺名称" required>
		         <el-input v-model="ruleForm.name" />
		       </el-form-item>
		       
		       <el-form-item label="发货地" prop="countrycode" required>
		         <el-select-v2
		           v-model="ruleForm.countrycode"
		           placeholder="国家"
		           :options="options"
				   filterable
		         >
				 		
				 </el-select-v2>
				 </el-form-item>
				 <el-form-item label="省份" prop="stateorprovincecode"  >
				  <el-input v-model="ruleForm.stateorprovincecode"  placeholder="省份"   />
				</el-form-item>
				 <el-form-item label="城市" prop="city"  >
				  <el-input v-model="ruleForm.city"  placeholder="城市" />
				 </el-form-item>
			    <el-form-item label="地区" prop="districtorcounty"  >
				  <el-input v-model="ruleForm.districtorcounty"  placeholder="地区"   />
		       </el-form-item>
			   <el-form-item label="街道" prop="addressLine1"  >
			     <el-input v-model="ruleForm.addressline1"   />
			   </el-form-item>
		       <el-form-item label="详细地址" prop="addressLine2"  >
		         <el-input v-model="ruleForm.addressline2" type="textarea" />
		       </el-form-item>
			   <el-form-item label="邮政编码" prop="postalcode" placeholder="可填店铺名称" >
			     <el-input v-model="ruleForm.postalcode" />
			   </el-form-item>
			   
			   <el-divider   content-position="left" >联系人信息</el-divider>
			   <el-form-item label=""   >
			   </el-form-item>
			   <el-form-item label="联系人" prop="contact" placeholder="联系人姓名" >
			     <el-input v-model="ruleForm.contact" />
			   </el-form-item>
			   <el-form-item label="邮箱" prop="email" placeholder="联系人邮箱" >
			     <el-input v-model="ruleForm.email" />
			   </el-form-item>
			   <el-form-item label="电话" prop="phone" placeholder="联系人电话" >
			     <el-input v-model="ruleForm.phone" />
			   </el-form-item>
			   <el-form-item label="公司名" prop="companyName" placeholder="公司名称" >
			     <el-input v-model="ruleForm.companyName" />
			   </el-form-item>
			   <el-form-item label="默认地址" prop="isdefault" placeholder="店铺默认地址" >
				       <el-checkbox v-model="ruleForm.isdefault"></el-checkbox>
			   </el-form-item>
		     </el-form>
		    <template #footer>
		      <span class="dialog-footer">
		        <el-button @click="dialogVisible = false">取消</el-button>
		        <el-button type="primary" @click="submitAddress"
		          >提交</el-button >
		      </span>
		    </template>
		  </el-dialog>
	</div>
</template>

<script setup>
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,nextTick } from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import groupApi from '@/api/amazon/group/groupApi.js';
	import country from '@/model/sys/common/country.json';
	import addressApi from '@/api/amazon/inbound/addressApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js'
	import {ElMessage,ElLoading} from 'element-plus';
	 
	   
			let dialogVisible=ref(false)
			let formSize = ref('default')
			let groupList=ref([]);
			let marketList=ref([]);
			let options =ref();
			let dialogTitle=ref("");
			let amzgroup=ref();
			
			let ruleForm = reactive({
			  name: '',
			  groupid: '',
			  marketplaceid:'',
			  addressline1: '',
			  addressline2: '',
			  city: '',
			  districtorcounty: '',
			  stateorprovincecode: '',
			  countrycode: 'CN',
			  postalcode: '',
			  isfrom:true,
			  isdefault:false,
			  phone:'',
			  contact:"",
			  companyName:'',
			  email:''
			})
			function handleOpen(){
					 options.value = country;
			}
			
			
		    function submitAddress(){
			
				if(ruleForm["id"]){
					addressApi.updateAddress(ruleForm).then((res)=>{
						 dialogVisible.value = false;
						 ElMessage.success('操作成功！');
						 context.emit("addressChange") ;
					});
				}else{
					addressApi.saveAddress(ruleForm).then((res)=>{
						dialogVisible.value = false;
						ElMessage.success('操作成功！');
						context.emit("addressChange") ;
					});
				}
			}
			
			function showModel(param,groupid){
				dialogVisible.value = true;
				handleOpen();
				groupApi.getAmazonGroup().then((res)=>{
					groupList.value=res.data;
					if(res.data && res.data.length>0){
						groupChange(res.data[0].id,()=>{
							nextTick(()=>{
									if(param){
										ruleForm.id=param.id;
										ruleForm.name=param.name;
										ruleForm.groupid=param.groupid;
										if(param.marketplaceid){
											ruleForm.marketplaceid=param.marketplaceid;
										}else{
											ruleForm.marketplaceid="";
										}
										ruleForm.addressline1=param.addressline1;
										ruleForm.addressline2=param.addressline2;
										ruleForm.city=param.city;
										ruleForm.districtorcounty=param.districtorcounty;
										ruleForm.stateorprovincecode=param.stateorprovincecode;
										ruleForm.countrycode=param.countrycode;
										ruleForm.postalcode=param.postalcode;
										ruleForm.isfrom=param.isfrom;
										ruleForm.isdefault=param.isdefault;
										ruleForm.phone=param.phone;
										ruleForm.email=param.email;
										ruleForm.companyName=param.companyName;
										ruleForm.contact=param.contact;
										dialogTitle.value="修改发货地址";
									}else{
										ruleForm.name= '';
										ruleForm.groupid=groupid;
										ruleForm.addressline1= '';
										ruleForm.addressline2= '';
										ruleForm.city= '';
										ruleForm.districtorcounty= '';
										ruleForm.stateorprovincecode= '';
										ruleForm.countrycode= 'CN';
										ruleForm.postalcode= '';
										ruleForm.phone='';
										ruleForm.email='';
										ruleForm.companyName='';
										ruleForm.contact='';
										dialogTitle.value="添加发货地址";
									}
								})
								
							})
						}
					});
					
				
				
			}
			function groupChange(val,callback){
				ruleForm.groupid=val;
				marketApi.getMarketByGroup({'groupid':val}).then((res)=>{
					res.data.push({"marketplaceid":"","name":"全部"});
					marketList.value=res.data;
					if(res.data && res.data.length>0){
						ruleForm.marketplaceid=res.data[0].marketplaceid;
					}
					if(callback){
						callback();
					}
				})
			}
	        //方法
	        //数据接收
			
			 defineExpose({showModel});
	      
	
	 
</script>

<style>
</style>
