<template>
	<div class=" gary-bg" style="padding:16px 0">
		<el-row >
			<el-col :span="20"  :offset="2">
				<el-card class="fr-con">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <h3>物流信息</h3>
						 <el-form :model="form"  label-width="120px">
							 <el-form-item label="物流承运公司" required >
							 	<span>{{form.name}}</span>
							 </el-form-item>
						 </el-form>
						 <el-form :model="form"  label-width="120px">
							 <el-form-item label="公司简称"  >
								<span>{{form.simplename}}</span>
							 </el-form-item>
						 </el-form>
						 <el-form :model="form"  label-width="120px">
							 <el-form-item label="物流接口"  >
								 <el-space>
							 <span>{{form.api}} </span>
							 <el-icon @click="handleRouteToApi"><Setting/></el-icon>
							 </el-space>
							 </el-form-item>
						 </el-form>
						 <el-form :model="form"  label-width="120px">
						 	<el-form-item label="报价单" >
								 <el-space v-if="form.uploadpath" >
								<el-tag  type="success">已上传</el-tag>
								<el-link  @click="uploadVisiable=true"   type="primary" :underline="false">
									<el-icon> <Upload/> </el-icon>&nbsp;更新
								</el-link>
								<el-link  @click="infoVisiable=true" type="info" :underline="false">
									<el-icon> <View /> </el-icon>&nbsp;查看
								</el-link>
								
								<el-link  :href="form.uploadpath" type="info" :underline="false">
									<el-icon> <Download /> </el-icon>&nbsp;下载
								</el-link>
								 </el-space>
								 <el-space v-else>
									 <el-link @click="uploadVisiable=true"  type="primary" :underline="false">
									 	<el-icon> <Upload/> </el-icon>&nbsp;上传
									 </el-link>
								 </el-space>
						 	</el-form-item>
						 </el-form>
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >渠道列表 </h5>
						   </div>
						   <el-select v-model="tranTypechecked" @change="disableChange">
						   		<el-option v-for="item in tranTypes" :label="item.label" :value="item.value"></el-option>
						   </el-select>
						 </div>
						 <el-table border :data="tranTableData.list">
							 <el-table-column label="区域" prop="mname" width="80"/>
							 <el-table-column label="分区" prop="subarea" width="80" />
							 <el-table-column label="运输方式" prop="tname" width="120"  />
							 <el-table-column label="渠道类型"  prop="cname" width="120"/>
							 <el-table-column label="供应商渠道名称"  prop="channame">
							 </el-table-column>
							 <el-table-column label="渠道时效(天)" width="95" prop="pretime"/>
							 <el-table-column label="计价单位" width="100" prop="priceu" />
							 <el-table-column label="价格(元)" width="80" prop="price"/>
							 <el-table-column label="材积基数(cbcm)" width="120" prop="drate"/>
							 <el-table-column label="材积基数(cbm)" width="110" prop="cbmrate"/>
							 <el-table-column label="备注" prop="remark"/>
						 </el-table>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							<el-space>
								<el-button type="" @click="closeTab()">关闭</el-button>
								<el-button type="primary" @click="editTranInfo()">编辑</el-button>
							</el-space>
						</div>
						
				</el-card>
			</el-col>
		</el-row>
	</div>
	
	<el-dialog v-model="uploadVisiable"   title="导入">
			  <el-upload
			  		class="upload" 
			  		action   
			  		:http-request="uploadFile" 
			  		:limit="1000" 
			  		:show-file-list="true" 
			  		:headers="headers" 
			  		accept=".xls,.xlsx"
			  		>
			  		<!-- action="/api/file/fileUpload" -->
			  		<el-button class="btn"><i class="el-icon-paperclip"></i>上传文件</el-button>
			  		<template #tip>
						   
			  		</template>
			  	</el-upload>
		<template #footer>
		      <span class="dialog-footer">
		        <el-button @click="uploadVisiable = false">取消</el-button>
		        <el-button type="primary" @click="uploadPriceFile"  >上传</el-button >
		      </span>
		 </template>
	</el-dialog>
	
	<el-dialog class="excelinfo" v-model="infoVisiable" width="860px"  title="详情">
			  <iframe  :src="getFrameUrl(form.uploadpath)" width="100%" height='423px' style="padding:0;margin:0" frameborder='0'> </iframe>
		<template #footer>
		      <span class="dialog-footer">
		        <el-button type="primary" @click="infoVisiable=false"  >关闭</el-button >
		      </span>
		 </template>
	</el-dialog>
	
</template>
<script>
	import {ArrowDown,Edit,View,Upload,Download,Setting} from '@element-plus/icons-vue'
	import {Plus,Minus} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject } from 'vue'
    import { useRoute,useRouter } from 'vue-router'
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus'
	export default{
		components: {
			Plus,Edit,Minus,View,Upload,Download,Setting,
		},
		setup(){
		    let uploadVisiable=ref(false);
			let infoVisiable=ref(false);
			let tranTypechecked =ref(true)
			let tranTypes =reactive([
						{label:'启用',value:true},
						{label:'已删除',value:false}
					])
			let tranTableData =reactive({list:[]})
			let tranTable =[];
			let tranDeleteTable =[];
			let form = reactive({});
			let router = useRouter();
			let myfile=ref();
			let id="";
			function getFrameUrl(url){
				return "https://view.officeapps.live.com/op/embed.aspx?src="+url;
			}
			function editTranInfo(){
				if(id){
					router.push({
							path:'/transportation/edit',
							query:{
							  id: id,
							  replace:true,
							  title:"编辑物流",
							  path:'/transportation/edit',
							}
						})
				}
			 }
			 function showDeleteDetail(){
				 transportationApi.showDelDetail({'id':id}).then((res)=>{
					  tranDeleteTable=res.data;
					  tranTableData.list=tranDeleteTable;
				 });
			 }
			 function disableChange(){
				 if(tranTypechecked.value){
					 if(tranTable.length>0){
						  tranTableData.list=tranTable;
					 }else{
						 showCompanyDetail();
					 }
				 }else{
					if(tranDeleteTable.length>0){
						  tranTableData.list=tranDeleteTable;
					}else{
						 showDeleteDetail();
					} 
				 }
			 }
			function showCompanyDetail(){
				  id = router.currentRoute.value.query.id;
				if(id){
					transportationApi.showCompanyDetail({'id':id}).then((res)=>{
						if(res&&res.data){
						   form.name=res.data.name;
						   form.simplename=res.data.simplename;
						   if(res.data.api){
						      form.api=res.data.api;
						   }else{
							   form.api="无";
						   }
						   form.uploadpath=res.data.uploadpath;
						   transportationApi.loadApiCompany().then((res)=>{
							   res.data.forEach((item)=>{
								   if(item.id==form.api){
								   	    form.api=item.name;
								   }
							   })
							 
						   });
						   form.accessToken=res.data.accessToken;
						   form.id=res.data.id;
						   tranTableData.list=res.data.detaillist;
						   tranTable=res.data.detaillist;
						}
					});
				}
				
			}
		const emitter = inject("emitter"); // Inject `emitter`
		function closeTab(){
			emitter.emit("removeTab", 100);
		};
		function uploadFile(item){
			//上传文件的需要formdata类型;所以要转
			myfile.value=item.file;
		}
		function uploadPriceFile(){
			let FormDatas = new FormData()
			FormDatas.append('file',myfile.value);
			FormDatas.append('comid',id);
			transportationApi.uploadPriceFile(FormDatas).then(function(res){
				  ElMessage.success('上传成功');
				  uploadVisiable.value = false;
				  showCompanyDetail();
			})
		}
		function handleRouteToApi(){
			router.push({
				path:'/erp/thirdparty',
				query:{
				  title:"物流绑定",
				  path:'/erp/thirdparty',
				}
			})
		}
			onMounted(()=>{
			   showCompanyDetail();
			});	 
			return{
				form,
				tranTypes,
				tranTypechecked,
				tranTableData,
				closeTab,
				editTranInfo,
				disableChange,
				uploadVisiable,
				uploadFile,
				uploadPriceFile,
				infoVisiable,
				getFrameUrl,
				handleRouteToApi
			}
		}
		}
</script>

<style>
	.font-12{font-size: 12px;margin-right:4px;}
	.fr-con h3{
		margin-bottom:16px ;
	}
	.fr-con .el-card__body{
			height:calc(100vh - 68px);
			min-height:600px;
		}
	.he-scr-car{
		height:calc(100vh - 176px);
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
.excelinfo .el-dialog__body{
	padding:0px;
}
</style>
