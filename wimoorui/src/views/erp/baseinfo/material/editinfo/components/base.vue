<template>
	<h3 id="base" class="tab-scroll">基础信息</h3>
	<el-form-item label="产品图片"   v-if="type!='package'">
		<el-upload 
		    action="#"
		    list-type="picture-card"
			:show-file-list="false"
			:limit="1"
			:multiple="false"
			accept=".png,.jpeg,.jpg,.bmp"
			:headers="headers" 
			:http-request="uploadFiles"
			:before-upload="beforeUpload" 
			:on-exceed="handleExceed"
			 ref="uploadRef"
			auto-upload="true"
		>
		<div v-if="dataForms.id" >
			<img  width="130" height="130" v-if="dataForms.image && dataForms.image!=undefined"   :src="dataForms.image" alt="产品图片" />
			<img width="130" height="130" v-else   :src="$require('empty/noimage40.png')" alt="暂无图片" />
		</div>
		<el-icon v-else>
			<img  width="130" height="130" v-if="dataForms.image && dataForms.image!=undefined"   :src="dataForms.image" alt="产品图片" />
			<Plus v-else />
		</el-icon>
		</el-upload>
		<el-button v-if="dataForms.location || showPkgImage" link type="info" style="position: relative;top: -60px;margin-left: 15px;margin-right: 10px;">扩展图片</el-button>
		<el-upload
		v-if="dataForms.location || showPkgImage"
		    action="#"
		    list-type="picture-card"
			:show-file-list="false"
			:limit="1"
			:multiple="false"
			accept=".png,.jpeg,.jpg,.bmp"
			:headers="headers" 
			:http-request="uploadFiles2"
			:before-upload="beforeUpload2" 
			:on-exceed="handleExceed2"
			 ref="pkguploadRef"
			auto-upload="true"
		>
		<div v-if="dataForms.id" >
			<img  width="130" height="130" v-if="dataForms.location && dataForms.location!=undefined"   :src="dataForms.location" alt="产品带包装图片" />
			<img width="130" height="130" v-else   :src="$require('empty/noimage40.png')" alt="暂无图片" />
		</div>
		<el-icon v-else>
			<img  width="130" height="130" v-if="dataForms.location && dataForms.location!=undefined"   :src="dataForms.location" alt="产品带包装图片" />
			<Plus v-else />
		</el-icon>
		</el-upload>
	</el-form-item>
	<el-form-item prop="baseforms.name" :rules="{ required: true,  message: '名称不能为空',  }" label="产品名称" >
		  <el-input v-model="dataForms.name"  placeholder="产品名称"> </el-input>
	</el-form-item>
	
	<el-row>
		<el-col :span="12">
			<el-form-item label="SKU" prop="baseforms.sku"
			:rules="{ required: true,  message: 'SKU不能为空',  }"
			>	
				<el-input v-if="!dataForms.id" class="in-wi-24" v-model="dataForms.sku"   placeholder="建议用字母加数字"> </el-input>
				<el-input v-else class="in-wi-24" v-model="dataForms.sku" disabled="true" placeholder="建议用字母加数字"> </el-input>
			</el-form-item>
			<el-form-item label="规格"  class="grid-row">
				 <el-input  class="in-wi-24" v-model="dataForms.specification"  placeholder="请不要输入长宽高信息"> </el-input>
			</el-form-item>
			<el-form-item label="条形码"  class="grid-row">
				 <el-input  class="in-wi-24" v-model="dataForms.upc"  placeholder="请不要输入长宽高信息"> </el-input>
			</el-form-item>
	<el-form-item label="品牌"  v-if="type=='product'" class="grid-row">
		<el-select class="in-wi-24" v-model="dataForms.brandid" placeholder="选择品牌">
			<el-option v-for="(item,index) in brandlist" :label="item.name" :value="item.id" />
		</el-select>
		<el-button type="primary"  link @click="jumpBrand">
			<el-icon class="ic-cen font-small">
				<Edit />
			</el-icon>
			编辑</el-button>
	</el-form-item>

	<el-form-item label="品类" class="grid-row">
		<el-select class="in-wi-24" v-model="dataForms.categoryid" placeholder="选择品类">
			<el-option v-for="(item,index) in catelist" :label="item.name" :value="item.id" />
		</el-select>
		<el-button type="primary"  link  @click="jumpCategory">
			<el-icon class="ic-cen font-small">
				<Edit />
			</el-icon>
			编辑</el-button>
	</el-form-item>
	
	<el-form-item  v-if="type=='product'"  label="负责人">
		<OwnerAll @owner="changeOwner"  class="in-wi-24" notAll="isNotAll" :isInForm="true" :defaultValue="dataForms.owner" ></OwnerAll>
	</el-form-item>
	<el-form-item v-if="type=='product'"  label="生效日期">
		<el-date-picker class="in-wi-24" style="width:400px" v-model="dataForms.effectivedate" 
		:default-value="dataForms.effectivedate" type="date" 
		 placeholder="选择日期" :size="size" />
	</el-form-item>
	</el-col>
	<el-col :span="12">
		
		<el-form-item  v-if="type=='product'" label="产品标签" class="grid-row">
			  <el-cascader-panel ref="tagsRef" v-model="tagsValue"   placeholder="请选择标签" :options="tagsList" 
			   @change="changeTags" :popper-append-to-body="false" :props="markprops" clearable />
			 
		</el-form-item>
		<div style="padding-left:100px;margin-top:-15px" v-if="type=='product'">
	    <el-button type="primary"   link  @click="Addtag">
			<el-icon class="ic-cen font-small">
				<Edit />
			</el-icon>
			编辑</el-button>
			 </div>
	</el-col>
	</el-row>
	<el-form-item label="备注">
		<el-input v-model="dataForms.remark" maxlength="2000" placeholder="产品备注" show-word-limit type="textarea" />
	</el-form-item>
	<el-form-item label="" v-if="!dataForms.location"> 
		<el-button   link type="info" @click.stop="showPkgImage=true" >添加扩展图片</el-button>
	</el-form-item>
 
	
</template>

<script  setup>
	import {ArrowDown,Edit,Delete,Download,ZoomIn} from '@element-plus/icons-vue'
	import {Plus} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch ,toRefs,computed} from 'vue'
	import {useRouter } from 'vue-router'
	import brandApi from '@/api/erp/material/brandApi.js';
	import categoryApi from '@/api/erp/material/categoryApi.js'
	import materialApi from '@/api/erp/material/materialApi.js';
	import { genFileId } from 'element-plus'
	import {getAllTags} from '@/api/sys/admin/tag.js';
	import OwnerAll from '@/components/header/ownerAll.vue';
	import { ElMessage } from 'element-plus';
	import {CheckInputFloat,CheckInputInt,spaceCharInput} from '@/utils/index.js';
	let headers=ref({"Content-Type": "multipart/form-data" });
	let tagsRef=ref();
	        onMounted(()=>{
	        	   setTimeout(function(){editTags();},300);
	        })
			let uploadRef=ref();
			let pkguploadRef=ref();
			let router = useRouter()
			const iscopy=router.currentRoute.value.query.iscopy;
			const type=router.currentRoute.value.query.type;
	        let props = defineProps({
	          dataForms:Object,
			  tagsValue:[],
	        })
			let state=reactive({
				brandlist:[],
				catelist:[],
				imgfile:{},
				pkgimgfile:{},
				markVisable:false,
				tagsList:[],
				 markprops:{ multiple: true },
				 checktags:'',
				 tagsNodes:[],
				 copypage:iscopy,
				 showPkgImage:false,
			});
			defineExpose({
			  loadBrandCateList
			})
			 let {dataForms,tagsValue} =toRefs(props);
			 let {
				tags,brandlist,catelist,
			    imgfile,tagsList,pkgimgfile,
			    markVisable,markprops,checktags,tagsNodes,copypage,showPkgImage
				} =toRefs(state);
			//选中标签
			function openPopover(){
				let arr =[]
				props.dataForms.tagOptions.forEach((item)=>{
					item.marks.forEach((sub)=>{
						arr.push({name:sub.name})
					})
				})
				tags.forEach((item)=>{
					item.marks.forEach((sub)=>{
						for(var i=0;i<arr.length;i++)
						if(sub.name==arr[i].name){
							sub.value=true
						}
					})
				})
			}
			//跳转到品牌管理
			function jumpBrand(){
				router.push({
					path:"/erp/baseinfo/brand",
					query:{
						title:'品牌管理',
						path:'/erp/baseinfo/brand',
					}
				})
			}
			function jumpCategory(){
				router.push({
				path:"/erp/baseinfo/category",
				query:{
					title:'品类管理',
					path:'/erp/baseinfo/category',
				},
				})
			}
			
			function jumptags(){
				router.push({
				path:"/sys/tags",
				query:{
					title:'产品标签',
					path:'/sys/tags',
				},
				})
			}
		//变为单选
		function tagChange(row,item){
			  row.forEach((i)=>{
				  if(i.name!==item.name){
					  i.value = false
				  }
			  })
		}
		function loadBrandCateList(){
			brandApi.getBrand().then((res)=>{
				state.brandlist=res.data;
			});
			categoryApi.getcategory().then((res)=>{
				state.catelist=res.data;
			});
		}
		//文件上传之前
		function beforeUpload(file){
			if (file.type != "" || file.type != null || file.type != undefined){
			    //截取文件的后缀，判断文件类型
				//const FileExt = file.name.replace(/.+\./, "").toLowerCase();
				//计算文件的大小
				const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
				//如果大于50M
				if (!isLt5M) {
					ElMessage.error('上传文件大小不能超过 5000KB!!');
					return false;
				}
				else {
				   props.dataForms.image=URL.createObjectURL(file);
				   return true;
				}
			}
		}
		//文件上传之前
		function beforeUpload2(file){
			if (file.type != "" || file.type != null || file.type != undefined){
			    //截取文件的后缀，判断文件类型
				//const FileExt = file.name.replace(/.+\./, "").toLowerCase();
				//计算文件的大小
				const isLt5Mpkg = file.size / 1024  < 5000; //这里做文件大小限制
				//如果大于50M
				if (!isLt5Mpkg) {
					ElMessage.error('上传文件大小不能超过 5000KB!!');
					return false;
				}
				else {
				   props.dataForms.location=URL.createObjectURL(file);
				   props.dataForms.pkgimage=URL.createObjectURL(file);
				   return true;
				}
			}
		}
		function uploadFiles(item){
			//上传文件的需要formdata类型;所以要转
			state.imgfile=item.file;
			props.dataForms.imgfile=item.file;
		}
		function uploadFiles2(item){
			//上传文件的需要formdata类型;所以要转
			state.pkgimgfile=item.file;
			props.dataForms.pkgimgfile=item.file;
		}
		function handleExceed(files){
			uploadRef.value.clearFiles();//清空图片list
			const file=files[0];
			file.uid = genFileId();
			uploadRef.value.handleStart(file);//手动选择图片
			props.dataForms.image=URL.createObjectURL(file);
			uploadRef.value.submit();//上传图片
		}
		function handleExceed2(files){
			pkguploadRef.value.clearFiles();//清空图片list
			const file2=files[0];
			file2.uid = genFileId();
			pkguploadRef.value.handleStart(file2);//手动选择图片
			props.dataForms.pkgimage=URL.createObjectURL(file2);
			props.dataForms.location=URL.createObjectURL(file2);
			pkguploadRef.value.submit();//上传图片
		}
		function Addtag(){
			router.push({
				path:'/sys/tags',
				query:{
				  title:"产品标签",
				  path:'/sys/tags',
				}
			})
		}
		function changeTags(tags){
			 var items="";
			 if(tags){
				  tags.forEach(function(item){
					  items+=(item[1]+",");
				  });
			 }else{
				 items="";
			 }
			 state.checktags=items;
			 submitTags();
		}
		function submitTags(){
			props.dataForms.taglist=state.checktags;
			state.tagsNodes=[];
			if(tagsRef.value.getCheckedNodes() && tagsRef.value.getCheckedNodes().length>0){
				 tagsRef.value.getCheckedNodes().forEach(function(item){
					 if(item.level==2){
						 state.tagsNodes.push({"name":item.label});
					 }
				 });
				 props.dataForms.tagNameList=state.tagsNodes;
			}
			state.markVisable=false;
		}
		function editTags(){
			getAllTags().then(res => {
				state.tagsList=res.data;
			});
			//state.markVisable =true;
		}
		function changeOwner(id){
			 props.dataForms.owner=id;
		}
		 
			
</script>

<style scoped>
	.scoped-form .el-form-item{
		margin-bottom:0px;
	}
	 .footer-right{
		text-align: right;
		margin-top:16px;
	}
	.right-align-list .el-descriptions__label{
		width:100px;
		text-align: right;
		padding-right:16px;
	}
	.in-wi-24{
		width: 400px;
	}
</style>
