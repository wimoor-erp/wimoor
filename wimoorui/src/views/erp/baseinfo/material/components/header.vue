<template>
	<el-row>
	    <el-space >
	   <Supplier  ref="supplierRef"   @change="handleChangeSupplier" />
	   <Owner @owner="changeOwner" v-if="type=='product'" ref="ownerRef" ></Owner>
	   <Category @change="changeCategory" ref="categoryRef" :type="type"></Category>
	   <Tags @change="changeTags"  v-if="type=='product'" ref="tagsRef"></Tags>
	   <Warehouse @changeware="getWarehouse"  v-if="type=='consumable'"  defaultValue="only"    />
	   <el-input  v-model="searchKeywords" clearable @input="changeData" placeholder="请输入" class="input-with-select" >
	      <template #prepend>
	        <el-select v-model="searchType" placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="产品名称" value="name"></el-option>
	          <el-option label="供应商" value="supplier"></el-option>
	           <el-option label="备注" value="remark"></el-option>
	        </el-select>
	      </template>
	      <template #append>
	        <el-button @click.stop="changeData">
	           <el-icon class="ic-cen font-medium">
	            <search />
	         </el-icon>
	        </el-button>
	      </template>
	    </el-input>
		<el-popover   :teleported="true"  :width="500" trigger="click">
			<template #reference>
			<el-button  title='高级筛选'  class='ic-btn'>
			  <filtericon></filtericon>
			</el-button>
			</template>
			 <el-form  label-width="auto"  label-position="left">
				 <el-form-item v-if="type=='product'" label="类型">
					 <el-radio-group v-model="productType">
					      <el-radio-button label="全部产品" />
					      <el-radio-button label="单独产品" />
						  <el-radio-button label="组合产品" />
						  <el-radio-button label="待组装产品" />
					    </el-radio-group>
					 </el-form-item>	
				<el-form-item label="产品状态">
				     <el-radio-group v-model="isdelete" >
				           <el-radio label="0" size="large" >启用</el-radio>
				           <el-radio label="1" size="large" >停用</el-radio>
				         </el-radio-group>
				   </el-form-item>
				   <el-form-item label="本地SKU">
				     <el-input
				         v-model="searchlist"
				         :rows="2"
				         type="textarea"
				         placeholder="请输入本地SKU,以英文的逗号分割"
				       />
				    </el-form-item>
					<el-form-item label="产品名称">
					  <el-input
					      v-model="productname"
					      :rows="2"
					      type="textarea"
					      placeholder="请输入产品名称"
					    />
					 </el-form-item>
				<el-form-item label="备注">
				  <el-input
				      v-model="remarks"
				      :rows="2"
				      type="textarea"
				      placeholder="请输入"
				    />
				 </el-form-item>
				 <el-form-item>
					 <el-button type="primary" @click.stop="submitQuery" plain>确认</el-button>
				  </el-form-item>
				 </el-form>
			  </el-popover>	 
	    <el-button @click.stop="clearSearch">重置</el-button>
	  </el-space>
	     <div class='rt-btn-group' v-if="type=='consumable'">
			 <el-button @click.stop="handlePurchase" type="primary"> 采购</el-button>
				    <el-button @click.stop="showSetDialog"><SettingTwo></SettingTwo>库存比例配置</el-button>
					<el-button @click.stop="clearPlan">清空计划</el-button>
		  </div>
	  </el-row>
	   <!--功能区域 -->
	  <el-row>
	   <el-space >
	    <el-button type="primary"  v-if="type=='product'" class="im-but-one" @click="addProduct">
	      <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
	      <span>添加产品</span>
	    </el-button>
		<el-button type="primary"  v-if="type=='consumable'" class="im-but-one" @click="addProduct">
		  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		  <span>添加辅料</span>
		</el-button>
		<el-button type="primary"  v-if="type=='package'" class="im-but-one" @click="addProduct">
		  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		  <span>添加箱子</span>
		</el-button>
	    <el-dropdown trigger="click">
	      <el-button>
	                 导入数据<el-icon class="el-icon--right"><arrow-down /></el-icon>
	      </el-button>
	      <template #dropdown>
	        <el-dropdown-menu >
	          <el-dropdown-item  v-if="type=='product'"     @click="importProduct('base')">导入产品</el-dropdown-item>
			  <el-dropdown-item  v-if="type=='consumable'"  @click="importProduct('base')">导入辅料</el-dropdown-item>
			  <el-dropdown-item  v-if="type=='package'"  @click="importProduct('base')">导入箱子</el-dropdown-item>
	          <el-dropdown-item  v-if="type!='package'" @click="importProduct('image')">导入图片</el-dropdown-item>
	          <el-dropdown-item @click="importProduct('supplier')">导入供应商</el-dropdown-item>
			  <el-dropdown-item @click="importProduct('moresupplier')">导入备选供应商</el-dropdown-item>
	          <el-dropdown-item  v-if="type=='product'" @click="importProduct('cons')">导入辅料</el-dropdown-item>
			  <el-dropdown-item  v-if="type=='product'" @click="importProduct('customs')">导入海关</el-dropdown-item>
			  <el-dropdown-item  v-if="type=='product'" @click="importProduct('assembly')">导入组装关系</el-dropdown-item>
	        </el-dropdown-menu>
	      </template>
	    </el-dropdown>
		<el-dropdown trigger="click" v-hasPerm="'erp:material:download'"  >
		  <el-button>
		             导出数据<el-icon class="el-icon--right"><arrow-down /></el-icon>
		  </el-button>
		  <template #dropdown>
		    <el-dropdown-menu >
		      <el-dropdown-item v-if="type=='product'" @click="downloadProduct('base')">导出产品</el-dropdown-item>
			  <el-dropdown-item v-if="type=='consumable'" @click="downloadProduct('base')">导出辅料</el-dropdown-item>
			   <el-dropdown-item v-if="type=='consumable'" @click="downloadProduct('plan')">导出计划</el-dropdown-item>
			  <el-dropdown-item v-if="type=='package'" @click="downloadProduct('base')">导出箱子</el-dropdown-item>
		      <el-dropdown-item @click="downloadProduct('supplier')" >导出供应商</el-dropdown-item>
		      <el-dropdown-item  v-if="type=='product'" @click="downloadProduct('cons')">导出辅料</el-dropdown-item>
			  <el-dropdown-item  v-if="type=='product'" @click="downloadProduct('customs')">导出海关</el-dropdown-item>
			  <el-dropdown-item  v-if="type=='product'" @click="downloadProduct('assembly')">导出组装关系</el-dropdown-item>
		    </el-dropdown-menu>
		  </template>
		</el-dropdown>
	    <el-button  v-if="type=='product'" @click.stop="showPictureDialog">
			同步图片/尺寸
		</el-button>
	    <el-dropdown trigger="click">
	      <el-button>
	                 批量操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
	      </el-button>
	      <template #dropdown>
	        <el-dropdown-menu >
	          <el-dropdown-item @click.stop="recoverRows">启用</el-dropdown-item>
	          <el-dropdown-item @click.stop="deleteRows">停用</el-dropdown-item>
	          <el-dropdown-item  v-if="type!='consumable'" @click.stop="purchaseRows">采购</el-dropdown-item>
			  <el-dropdown-item @click.stop="showInfoDialog">批量修改</el-dropdown-item>
			  <el-dropdown-item v-if="type=='product'" @click.stop="handlerSyncProductList">同步亚马逊SKU</el-dropdown-item>
	        </el-dropdown-menu>
	      </template>
	    </el-dropdown>
	   </el-space> 
	   <div class='rt-btn-group'>
		   <div v-if="type=='consumable'" class="flex-center font-small m-r-16" style="padding-right: 10px;">
			<el-checkbox v-model="queryParam.iswarning" label="仅显示低于安全库存"  @change="submitQuery"/>
			<el-checkbox v-model="queryParam.isplan" label="显示已选"  @change="submitQuery"/>
			<el-divider direction="vertical" />
			<span>已选 <span class="text-orange font-bold"> {{summary.skunum}} </span> 个SKU</span>
			<el-divider direction="vertical" />
			<span>采购总数 <span class="text-orange font-bold">
			<span v-if="summary.amount>0" >{{summary.amount}} </span>
			<span v-else>0 </span>
			</span></span>
		   </div>
	    <el-button   class='ic-btn' title='帮助文档'>
	     <help theme="outline" size="16" :strokeWidth="3"/>
	   </el-button>
	   </div>
	</el-row>
	<!-- 上传文件弹框 -->
	<el-dialog
	   v-model="uploadVisible"
	   :title="uploadTitle"
	   width="400px"
	 >
	 <el-upload
	     :drag="true"
	     action
		 ref="uploadRef"
		 :http-request="uploadFiles"
		 :limit="1"
		 :on-exceed="handleExceed"
		 :before-upload="beforeUpload" 
		 :show-file-list="true" 
		 :headers="headers" 
		 accept=".xls,.xlsx,.zip"
	   >
	     <el-icon class="font-large"><upload-filled /></el-icon>
	     <div class="el-upload__text">
	      拖拽文件到此处或 <em>点击上传</em>
	     </div>
	   </el-upload>
	 <template #footer>
	   <span class="dialog-footer">
		   <div class="flex-center-between">
		 <el-button v-if="state.uploadTitle!='导入产品图片'" type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
		 <span class="font-extraSmall" v-else>图片需对应SKU命名(sku.jpg/png),<br/>然后压缩文件为zip上传</span>
		 <div>
	     <el-button @click="uploadVisible = false">取消</el-button>
	     <el-button type="primary" v-loading="uploadloading" @click.stop="uploadExcel">
	       上传文件
	     </el-button></div></div>
	   </span>
	 </template>
	  </el-dialog>
	  <!-- 图片同步弹框 -->
	  <el-dialog v-model="pictureDialog" title="同步图片/尺寸" width="1000px">
	  	  <el-table :data="pictureTable" style="width: 100%">
			  <el-table-column prop="image" label="产品图片" width="80" >
				 <template #default="scope"> 
					 <el-image v-if="scope.row.image" :src="scope.row.image"   width="40" height="40"  ></el-image>
					 <el-image v-else  :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
				 </template> 
			  </el-table-column>
			   <el-table-column prop="name" label="本地产品名称" show-overflow-tooltip width="200" >
				   <template #default="scope"> 
					<div v-if="scope.row.pname" :src="scope.row.name"  >
							 本地SKU:{{scope.row.sku}}
							 <div>{{scope.row.name}}</div>
					</div>
				    </template> 
			   </el-table-column>
			  <el-table-column prop="infoimage" label="商品图片"  width="80" >
					<template #default="scope">
						 <el-image v-if="scope.row.infoimage" :src="scope.row.infoimage"   width="40" height="40"  ></el-image>
						 <span v-else>无</span>
					 </template> 
			  </el-table-column>  
			   <el-table-column prop="pname" label="在售商品名称" show-overflow-tooltip width="200" >
				   <template #default="scope">
				   	 <div v-if="scope.row.pname" :src="scope.row.pname"  >
						 平台SKU:{{scope.row.infoSKU}}
						 <div>{{scope.row.pname}}</div>
					 </div>
				   	 <span v-else>暂无在售商品！</span>
				    </template>
			   </el-table-column>  
			  <el-table-column prop="infosize" label="在售尺寸" >
				  <template #default="scope">
				  	 <span v-if="scope.row.infosize"    >{{scope.row.infosize}}</span>
				  	 <span v-else>暂无在售尺寸！</span>
				   </template>
			  </el-table-column>  
			  <el-table-column prop="ischeck" label="操作" width="120" >
				 <template #default="scope">
					 <el-checkbox v-if="scope.row.infosize || scope.row.infoimage"  v-model="scope.row.ischeck" ></el-checkbox>
					 <span v-else></span>
				 </template> 
			  </el-table-column>
		  </el-table>
	  	  <template #footer>
	  	  	<span class="dialog-footer">
				<el-button type="primary" @click.stop="submitSize">一键同步尺寸</el-button>
				<el-button type="primary" @click.stop="submitPicture">一键同步图片</el-button>
	  	  		<el-button  @click="pictureDialog = false">取消</el-button>
	  	  	</span>
	  	  </template>
	  </el-dialog>
	  
	  <el-dialog v-model="setVisable"  title="编辑库存比例设置" width="400px">
	  	   <el-form>
			   
	  		 <el-form-item label="库存比例">
				 <el-input v-model="invRate" @input="invRate=CheckInputInt(invRate)"  placeholder="请输入整数">
					   <template #append>%</template>
				 </el-input>
			 </el-form-item>
	  	 </el-form>
	  	  <template #footer>
	  	  	<span class="dialog-footer">
	  	  		<el-button @click="setVisable = false">关闭</el-button>
	  	  		<el-button type="primary" @click.stop="submitInvRate">提交</el-button>
	  	  	</span>
	  	  </template>
	  </el-dialog>
	  <el-dialog v-model="infoVisable"  title="批量修改以下产品" width="800px" >
	  	  <el-table :data="selectRows" height="calc(40vh)">
			  <el-table-column prop="sku" label="SKU" width="150"></el-table-column>
			  <el-table-column prop="sku" label="图片" width="90">
				  <template #default="scope">
				  <el-image v-if="scope.row.image" :src="scope.row.image"  style="width:60px;height:60px;padding:0px;"  ></el-image>
				 <el-image v-else :src="$require('empty/noimage40.png')"  style="width:60px;height:60px;padding:0px;"   ></el-image>
				</template>
			  </el-table-column>
			  <el-table-column prop="name" label="名称"></el-table-column>
		  </el-table>
		  <el-row style="margin-top:10px;">批量修改内容:</el-row>
		  <el-row>
		   <el-radio-group v-model="updateType" @change="typeChangeHandle">
		      <el-radio :label="1">阶梯价</el-radio>
		      <el-radio   v-if="type=='product'" :label="2">生效日期 </el-radio>
		      <el-radio :label="3">供货周期</el-radio>
			  <el-radio   v-if="type=='product'" :label="4">负责人</el-radio>
			  <el-radio :label="5">供应商</el-radio>
			  <el-radio :label="6">备注</el-radio>
			  <el-radio :label="7">采购成本</el-radio>
			  <el-radio :label="8">标签</el-radio>
			  <el-radio :label="9">辅料对应关系</el-radio>
			  <el-radio :label="10">产品尺寸</el-radio>
		    </el-radio-group>
		  </el-row>
		  <el-row>
			  <el-col :span="24" v-if="updateType==1">
				  <span>默认供应商:</span><Supplier   @change="changePriceSupplier"  defaultValue="only"/>
				  <el-table style="margin-top:5px;" border :data="stepList" >
				  	<el-table-column label="采购量">
				  		<template #default="scope">
				  			<el-space>
				  				<el-input v-model="scope.row.amount" @input="scope.row.amount=CheckInputInt(scope.row.amount)" placeholder="起订量" ></el-input>
				  			</el-space>
				  		</template>	
				  	</el-table-column>
				  	<el-table-column label="采购单价">
				  		<template #default="scopesub">
				  			<el-input v-if="0==scopesub.$index"  
				  			v-model="scopesub.row.price" 
				  			@input="scopesub.row.price=CheckInputFloat(scopesub.row.price)"
				  			@change="checkPriceInputFloat(scopesub.row,scopesub.row.price)"></el-input>
				  			<el-input v-else v-model="scopesub.row.price" @input="scopesub.row.price=CheckInputFloat(scopesub.row.price)"></el-input>
				  		</template>	
				  	</el-table-column>
				  	<el-table-column label="操作">
				  		<template #default="scopesub">
				  			<el-space>
				  				<el-link v-if="(stepList.length-1)==scopesub.$index" title="添加" :underline="false" type="warning" @click="addladder">
				  				  <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
				  				 </el-link>
				  				<el-link title="删除" v-if="scopesub.$index>0 && (stepList.length-1)==scopesub.$index" type="warning" :underline="false" @click="removePrice(stepList)">
				  					<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
				  				</el-link>
				  			</el-space>
				  		</template>	
				  	</el-table-column>
				  </el-table>
			  </el-col>
			   <el-col :span="24" v-if="updateType==2">
				    <el-date-picker
				          v-model="infoParams.effDate"
				          type="date"
				          placeholder="选择日期"
				        />
				</el-col>
				<el-col :span="24" v-if="updateType==3">
					<el-input v-model="infoParams.cycle"  @input="infoParams.cycle=CheckInputInt(infoParams.cycle)" placeholder="单位:天"></el-input>
				</el-col>
				<el-col :span="24" v-if="updateType==4">
					<OwnerAll @owner="changeAllOwner"  class="in-wi-24" notAll="isNotAll" :isInForm="true"   ></OwnerAll>
				</el-col>
				<el-col :span="24" v-if="updateType==5">
					 <Supplier   @change="changeSupplier"  defaultValue="only"/>
				</el-col>
				<el-col :span="24" v-if="updateType==6">
					<el-input v-model="infoParams.remark" placeholder="请输入备注" ></el-input>
				</el-col>
				<el-col :span="24" v-if="updateType==7">
					<el-input v-model="infoParams.cost" @input="infoParams.cost=CheckInputFloat(infoParams.cost)" placeholder="请输入采购成本" ></el-input>
				</el-col>
				<el-col :span="24" v-if="updateType==8">
					<el-cascader v-model="optTagsValue" placeholder="请选择标签" :options="optTagsList"  :popper-append-to-body="false" :props="markprops" clearable />
				</el-col>
				<el-col :span="24" v-if="updateType==9">
					请选择辅料
					<el-table style="margin-top:5px;" border :data="consList" >
						<el-table-column label="辅料SKU">
							<template #default="scope">
								<el-space>
									<el-input v-model="scope.row.sku"  placeholder="输入辅料SKU" ></el-input>
								</el-space>
							</template>	
						</el-table-column>
						<el-table-column label="对应数量">
							<template #default="scope">
								<el-input v-model="scope.row.amount" @input="scope.row.amount=CheckInputFloat(scope.row.amount)" placeholder="对应量" ></el-input>
							</template>	
						</el-table-column>
						<el-table-column label="操作">
							<template #default="scopesub">
								<el-space>
									<el-link v-if="(consList.length-1)==scopesub.$index" title="添加" :underline="false" type="warning" @click="addladdercons">
									  <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
									 </el-link>
									<el-link title="删除"   type="warning" :underline="false" @click="removeCons(scopesub.$index)">
										<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
									</el-link>
								</el-space>
							</template>	
						</el-table-column>
					</el-table>
				</el-col>
				<el-col :span="24" v-if="updateType==10">
					<el-space>
					<el-input v-model="infoParams.pkgdimension.length"  @input="infoParams.pkgdimension.length=CheckInputFloat(infoParams.pkgdimension.length)" placeholder="长(cm)"></el-input>
					<el-input v-model="infoParams.pkgdimension.width"  @input="infoParams.pkgdimension.width=CheckInputFloat(infoParams.pkgdimension.width)" placeholder="宽(cm)"></el-input>
					<el-input v-model="infoParams.pkgdimension.height"  @input="infoParams.pkgdimension.height=CheckInputFloat(infoParams.pkgdimension.height)" placeholder="高(cm)"></el-input>
					<el-input v-model="infoParams.pkgdimension.weight"  @input="infoParams.pkgdimension.weight=CheckInputFloat(infoParams.pkgdimension.weight)" placeholder="重(kg)"></el-input>
					</el-space>
				</el-col>
			</el-row>
	  	  <template #footer>
	  	  	<span class="dialog-footer">
	  	  		<el-button @click="infoVisable = false">关闭</el-button>
				<el-button v-if="updateType==9" type="primary" @click.stop="submitConsData">清除辅料对应关系</el-button>
	  	  		<el-button type="primary" @click.stop="submitInfoData">提交</el-button>
	  	  	</span>
	  	  </template>
	  </el-dialog>
</template>

<script setup>
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,Minus} from '@icon-park/vue-next';
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue';
	import { useRoute,useRouter } from 'vue-router'
	import { ElMessage, ElMessageBox,genFileId } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import { ref,reactive,onMounted,watch,toRefs,inject } from 'vue'
	import Owner from '@/components/header/owner.vue';
	import OwnerAll from '@/components/header/ownerAll.vue';
	import Supplier from '@/components/header/supplier.vue';
	import Tags from '@/components/header/tags.vue';
	import Category from '@/components/header/category.vue';
	import materialApi from '@/api/erp/material/materialApi.js';
	import materialZipApi from '@/api/erp/material/materialZipApi.js';
	import {getAllTags} from '@/api/sys/admin/tag.js';
	import downloadTemplateFileApi from '@/api/erp/common/downloadTemplateFileApi.js';
	import uploadhandler from "@/utils/upload-handler";
	import filtericon from "@/components/icon/filtericon.vue";
	import consumableApi from '@/api/erp/purchase/plan/consumableApi.js';
	onMounted(()=>{
		changeData();
	})
	const supplierRef=ref();
	const uploadRef=ref();
	const emitter = inject("emitter");
	const emit = defineEmits(['getdata']);
	let router = useRouter();
	let ownerRef=ref();
	let categoryRef=ref();
	let tagsRef=ref();
	let state =reactive({
		invRate:"",
		setVisable:false,
		uploadVisible:false,
		uploadloading:false,
		uploadTitle:'导入产品基础',
		uploadtype:'base',
		searchType:'sku',
		searchKeywords:'',
		isdelete:'0',
		remarks:'',
		productType:'全部产品',
		tagsList:[],
		tagsValue:'',
		optTagsList:[],
		optTagsValue:'',
		queryParam:{selected:false},
		markprops:{ multiple: true },
		selectRows:[],
		myfile:null,
		pictureDialog:false,
		pictureTable:[],
		summary:{amount:0,skunum:0},
		productname:null,
		searchlist:null,
		infoVisable:false,
		updateType:1,
		stepList:[{amount:1,price:""}],
		consList:[{"sku":'',"amount":1}],
		infoParams:{
			pkgdimension:{
				length:null,
				width:null,
				height:null,
				weight:null,
			}
		}
	});
	let{searchType,uploadloading,searchKeywords,tagsList,tagsValue,optTagsList,optTagsValue,PersonData,Person,categoryList,category,
	   productType,isdelete,remarks,uploadTitle,uploadVisible,queryParam,markprops,selectRows,myfile,uploadtype,pictureDialog,summary,
	   pictureTable,setVisable,invRate,productname,searchlist,infoVisable,updateType,stepList,infoParams,consList,
	//buyerData,buyer
	}=toRefs(state);
	let props = defineProps({  type:""});
	let{type }=toRefs(props);	
	function removePrice(list){
		list.pop();
	}
	function removeCons(index){
		state.consList.splice(index,1)
	}
	function changeAllOwner(id){
		 state.infoParams.owner=id;
	}
	function changeSupplier(id){
		 state.infoParams.supplierid=id;
	}
	function handleChangeSupplier(id,load){
		state.queryParam.supplier=id;
		if(load!="load"){
		changeData();
		}
	}
	function changePriceSupplier(id){
		 state.infoParams.priceSupplierid=id;
	}
	function handlerSyncProductList(){
		ElMessageBox.confirm('您确认要从亚马逊同步SKU到产品列表！', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
		})
				.then(() => {
				 materialApi.syncProductList().then(res=>{
				 	ElMessage.success('操作成功！');
				 	changeData();
				 })
				})
		.catch(() => ElMessage.info('已取消操作'));
		
	}
	function typeChangeHandle(){
		if(state.updateType){
			editTags();
		}
	}
	function editTags(){
		getAllTags().then(res => { state.optTagsList=res.data; });
	}
	function checkPriceInputFloat(row,price){
			row.price=price;
	}
	function addladdercons(){
		state.consList.push({
			amount:'',
			sku:'',
		})
	}
	
			function addladder(){
				state.stepList.push({
					amount:'',
					currency:'0',
					price:'',
				})
				//putval(row,index)
			}
			function submitConsData(){
				ElMessageBox.confirm('确认移除选择SKU的辅料关系吗！', '警告', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning',
				})
						.then(() => {
						  //判断辅料列表 是否输入正确
						  var skulist=[];
						  state.selectRows.forEach(items=>{
						  	var id=items.id+'';
						  	skulist.push(id);
						  }); 
						  if(skulist.length>0){
						  	materialApi.batchClearCons(skulist).then((res)=>{
						  		ElMessage.success('操作成功！');
						  		changeData();
						  		state.infoVisable=false;
						  	});
						  }else{
						  	ElMessage.error('至少选择一行数据！')
						  }
						})
				.catch(() => ElMessage.info('已取消操作'));
				
				
				
			}
			
		    function submitInfoData(){
				//批量保存
				var tagitems="";
				if(state.selectRows&&state.selectRows.length>0){
					if(state.updateType==1){
						//判断阶梯价 是否输入正确
						var isok=true;
						let amounts = state.stepList.map(item => parseInt(item["amount"]));
						let amountSet = new Set(amounts);//去重
						 if (amountSet.size == amounts.length) {
						   isok=true;
						} else {
						   isok=false;
						}
						if(!isok){
							ElMessage.error( '阶梯价不能出现重复的采购量！' )
							return;
						}
						//先拿到默认供应商
						var supplierid=state.infoParams.priceSupplierid;
						//修改阶梯价
						var list=[];
						state.selectRows.forEach(item=>{
							state.stepList.forEach(stp=>{
								var row={};
								row.materialid=item.id;
								row.amount=stp.amount;
								row.supplierid=supplierid;
								row.price=stp.price;
								list.push(row);
							});
						});
						materialApi.batchUpdatePrice(list).then((res)=>{
							ElMessage.success('操作成功！');
							changeData();
							state.infoVisable=false;
						});
					}else if(state.updateType==9){
						//判断辅料列表 是否输入正确
						var conlists=[];
						var skulist=[];
						state.selectRows.forEach(items=>{
							var row={};
							row.materialid=items.id
							skulist.push(row);
						}); 
						state.consList.forEach(item=>{
							if(item.sku!='' && item.amount!=''){
								var row={};
								row.consku=item.sku;
								row.amount=item.amount;
								conlists.push(row);
							}
						}); 
						if(conlists.length>0){
							var datas={};
							datas.conlist=conlists;
							datas.skulist=skulist;
							materialApi.batchUpdateCons(datas).then((res)=>{
								ElMessage.success('操作成功！');
								changeData();
								state.infoVisable=false;
							});
						}else{
							ElMessage.error('辅料列表数据填入有误！')
						}
						
					}else{
						var ftype="date";
						if(state.updateType==2){
							ftype="date";
						}else if(state.updateType==3){
							ftype="cycle";
						}else if(state.updateType==4){
							ftype="owner";
						}else if(state.updateType==5){
							ftype="supplier";
						}else if(state.updateType==6){
							ftype="remark";
						}else if(state.updateType==7){
							ftype="cost";
						}else if(state.updateType==8){
							ftype="tags";
							state.optTagsValue.forEach(function(item){
								 tagitems+=(item[1]+",");
							});
						}else if(state.updateType==10){
							ftype="dim";
						}
						var data=[];
						state.selectRows.forEach(item=>{
							var row={};
							row.id=item.id;
							row.effectivedate=state.infoParams.effDate;
							row.deliveryCycle=state.infoParams.cycle;
							row.owner=state.infoParams.owner;
							row.supplier=state.infoParams.supplierid;
							row.remark=state.infoParams.remark;
							row.taglist=tagitems;
							if(state.infoParams.cost){
								row.price=parseFloat(state.infoParams.cost);
							}
							row.pkgdimension=state.infoParams.pkgdimension;
							data.push(row);
						});
						materialApi.updateMaterial(ftype,data).then((res)=>{
							ElMessage.success('操作成功！');
							changeData();
							state.infoVisable=false;
						})
					}
				}else{
					ElMessage.error('至少选择一个数据行！')
				}
				
			}
			 function showInfoDialog(){
				 state.infoParams.pkgdimension={
				 	length:null,
				 	width:null,
				 	height:null,
				 	weight:null,
				 };
				 state.infoVisable=true;
			 }
			function addProduct(){
				var title="添加产品";
				if(props.type=='consumable'){
					title="添加辅料";
				}
				if(props.type=='package'){
					title="添加包材";
				}
				emitter.emit("removeCache", title);
				router.push({
					path:'/localproduct/addinfo',
					query:{
					  title:title,
					  path:'/localproduct/addinfo',
					  details:'',
					  refresh:true,
					  type:props.type
					},
				}) 
				 
			}
		function importProduct(ftype){
			state.uploadVisible = true;
			state.uploadloading = false;
			state.uploadtype=ftype;
			if(ftype=="base"){
				state.uploadTitle="导入产品基础";
			}
			if(ftype=="image"){
				state.uploadTitle="导入产品图片";
			}
			if(ftype=="supplier"){
				state.uploadTitle="导入产品默认供应商";
			}
			if(ftype=="moresupplier"){
				state.uploadTitle="导入产品备选供应商";
			}
			if(ftype=="cons"){
				state.uploadTitle="导入产品辅料";
			}
			if(ftype=="customs"){
				state.uploadTitle="导入产品海关";
			}
			if(ftype=="assembly"){
				state.uploadTitle="导入产品组装关系";
			}
		}
		
		function downloadProduct(ftype){
			state.uploadVisible = false;
			 var data={};
			 data=state.queryParam;
			 if(ftype=="assembly"){
				 ftype="MaterialAssembly";
				 data.issfg="1";
			 }
			 if(ftype=="base"){
				 if(props.type=="product"){
			 	    ftype='MaterialBaseInfo';
				 }else if(props.type=="consumable"){
					ftype="MaterialBaseInfoConsumable";
				 }else if(props.type=="package"){
					ftype="MaterialBaseInfoPackage"; 
				 }
			 }
			 if(ftype=="plan"){
				ftype='MaterialConsumablePlan';
			 }
			 if(ftype=="supplier"){
			 	ftype='MaterialSupplier';
			 }
			 if(ftype=="cons"){
			 	ftype='MaterialConsumable';
			 }
			 if(ftype=="customs"){
			 	ftype='MaterialCustoms';
			 }
			 data.downtype=ftype;
			 data.mtype=type;
			 materialApi.downExcelRecords(data);
		}
		function changeOwner(id){
			state.queryParam.owner=id;
			changeData();
		}
		function getWarehouse(id){
			state.queryParam.warehouseid=id;
			changeData();
		}
		function getSetWarehouse(id){
			state.queryParam.setwarehouseid=id;
		}
		function changeTags(tags){
			 state.queryParam.taglist=tags;
			 changeData();
		}
		function handleExceed(files){
			  uploadRef.value.clearFiles();
			  const myfile = files[0]  ;
			  myfile.uid = genFileId();
			  uploadRef.value.handleStart(myfile);
		}
		function changeCategory(value){
			state.queryParam.categoryid=value;
			changeData();
		}
		function submitQuery(){
			var issfg="";
			if(state.productType=="全部产品"){
				issfg="";
			}else if(state.productType=="单独产品"){
				issfg="0";
			}else if(state.productType=="组合产品"){
				issfg="1";
			}else if(state.productType=="待组装产品"){
				issfg="2";
			}
			if(state.productname && state.productname!=""){
				state.queryParam.productname=state.productname;
			}else{
				state.queryParam.productname=null;
			}
			if(state.searchlist&& state.searchlist!=""){
				state.queryParam.searchlist=state.searchlist;
			}else{
				state.queryParam.searchlist=null;
			}
			state.queryParam.issfg=issfg;
			state.queryParam.isDelete=state.isdelete;
			state.queryParam.remark=state.remarks;
			changeData();
		}
		function changeData(){
			 state.queryParam.searchtype=state.searchType;
			 state.queryParam.search=state.searchKeywords;
			 emit('getdata',state.queryParam);
			 if(props.type=="consumable"){
				getSummary();
			 }
		}
		function deleteRows(){
			if(state.selectRows && state.selectRows.length>0){
				var ids="";
				state.selectRows.forEach(function(item){
					ids+=(item.id+",");
				});
				materialApi.deleteData({"ids":ids}).then((res)=>{
					if(res.data){
						ElMessage.success('批量删除成功！');
						changeData();
					}
				})
			}
		}
		function recoverRows(){
			if(state.selectRows && state.selectRows.length>0){
				state.selectRows.forEach(function(item){
					if(item.isDelete==true || item.isDelete==1){
						materialApi.recoverData({"id":item.id,"sku":item.sku}).then((res)=>{
							if(res.data){
								ElMessage.success(res.data);
								changeData();
							}
						})
					}
				});
				
			}
		}
		function handlePurchase(){
			    emitter.emit("removeCache", "添加采购单");
				router.push({
					path:"/e/p/o",
					query:{
						title:'添加采购单',
						path:"/e/p/o",
						conplanwarehouseid:state.queryParam.warehouseid
					},
				})
		}
		function purchaseRows(){
			//跳转至采购的路由url
			var materialList=[];
			state.selectRows.forEach(item=>{
				materialList.push(item.id);
			})
			emitter.emit("removeCache", "添加采购单");
				router.push({
					path:"/e/p/o",
					query:{
						title:'添加采购单',
						path:"/e/p/o",
						materialList:materialList
					},
				})
			 
		}
		function downloadTemp(){
			if(state.uploadtype=="base"){
				if(props.type=="product"){
				    downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialBaseInfo'});
				}else if(props.type=="consumable"){
					downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialBaseInfoConsumable'});
				}else if(props.type=="package"){
					downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialBaseInfoPackage'});
				}
			}
			if(state.uploadtype=="image"){
				downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialImage'});
			}
			if(state.uploadtype=="supplier"){
				downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialSupplier'});
			}
			if(state.uploadtype=="moresupplier"){
				downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialMoreSupplier'});
			}
			if(state.uploadtype=="cons"){
				downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialConsumable'});
			}
			if(state.uploadtype=="customs"){
				downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialCustoms'});
			}
			if(state.uploadtype=="assembly"){
				downloadTemplateFileApi.downExcelTemp({"ftype":'MaterialAssembly'});
			}
		}
		//文件上传之前
		function beforeUpload(file){
			if (file.type != "" || file.type != null || file.type != undefined){
			    //截取文件的后缀，判断文件类型
				const FileExt = file.name.replace(/.+\./, "").toLowerCase();
				//计算文件的大小
				const isLt5M = file.size / 1024  < 50000; //这里做文件大小限制
				//如果大于50M
				if (!isLt5M) {
					ElMessage.error('上传文件大小不能超过 50MB!!');
					return false;
				}
				else {
				   return true;
				}
			}
		}
		function uploadFiles(item){
			//上传文件的需要formdata类型;所以要转
			state.myfile=item.file;
		}
		function uploadExcel(){
			let FormDatas = new FormData()
			state.uploadloading=true;
			FormDatas.append('file',state.myfile);
			if(state.uploadtype=="base"){
				FormDatas.append('mtype',props.type);
				materialApi.uploadBaseInfoFile(FormDatas).then(function(res){
					 ElMessage.success('上传成功');
					   state.uploadloading=false;
					  state.uploadVisible = false;
					  changeData();
				}).catch(()=>{
					state.uploadloading = false;
				})
			}
			if(state.uploadtype=="image"){
				materialZipApi.uploadFile(FormDatas).then(function(res){
					  ElMessage.success('上传成功');
					  state.uploadloading=false;
					  state.uploadVisible = false;
					  changeData();
				}).catch(()=>{
					state.uploadloading = false;
				})
			}
			if(state.uploadtype=="supplier"){
				materialApi.uploadSupplierFile(FormDatas).then(function(res){
					  ElMessage.success('上传成功');
					  state.uploadloading=false;
					  state.uploadVisible = false;
					  changeData();
				}).catch(()=>{
					state.uploadloading = false;
				})
			}
			if(state.uploadtype=="moresupplier"){
				materialApi.uploadMoreSupplierFile(FormDatas).then(function(res){
					  ElMessage.success('上传成功');
					  state.uploadloading=false;
					  state.uploadVisible = false;
					  changeData();
				}).catch(()=>{
					state.uploadloading = false;
				})
			}
			if(state.uploadtype=="cons"){
				materialApi.uploadConsumableFile(FormDatas).then(function(res){
					  uploadhandler.uploadResult(res,()=>{
						   state.uploadloading=false;
						   state.uploadVisible = false;
						    changeData();
					  });
					 
				}).catch(e=>{
					  state.uploadloading = false;
					  uploadhandler.uploadResult(e);
				})
			}
			if(state.uploadtype=="customs"){
				materialApi.uploadCustomsFile(FormDatas).then(function(res){
					   ElMessage.success('上传成功');
					   state.uploadloading=false;
					   state.uploadVisible = false;
					   changeData();
				}).catch(()=>{
					state.uploadloading = false;
				})
			}
			if(state.uploadtype=="assembly"){
				materialApi.uploadAssemblyFile(FormDatas).then(function(res){
					  ElMessage.success('上传成功');
					  state.uploadloading=false;
					  state.uploadVisible = false;
					  changeData();
				}).catch(()=>{
					state.uploadloading = false;
				})
			}
			
			
		}
		function showPictureDialog(){
			if(state.selectRows && state.selectRows.length>0){
				state.pictureDialog=true;
				var skulist=[];
				var picturelist=[];
				state.selectRows.forEach(function(item){
					skulist.push(item.sku);
					item.ischeck=false;
					picturelist.push(item);
				});
				state.pictureTable=picturelist;
				materialApi.findSKUImageForProduct(skulist).then((res)=>{
					if(res.data){
						var data=res.data;
							state.pictureTable.forEach(function(mitem){
								var items=data[mitem.sku];
								if(items&&mitem.sku==items.materialSKU){
									mitem.info=items;
									mitem.infoimage=items.image;
									mitem.pname=items.infoName;
									mitem.infoSKU=items.infoSKU;
									mitem.infosize="长:"+items.length+items.lenunit+",宽:"+items.width+items.wunit+
									",高:"+items.height+items.hunit+",重:"+items.weight+items.weunit;
									mitem.dimid=items.dimid;
								}
							});
					}
					
				});
				
			}else{
				ElMessage.error('请至少选择一行数据！');
			}
		}
		function submitPicture(){
			var materialids="";
			var skus="";
			var images="";
			state.pictureTable.forEach(function(item){
				if(item.ischeck==true){
					if(item.infoimage){
						materialids+=(item.id+"%,#");
						skus+=(item.sku+"%,#");
						images+=(item.infoimage+"%,#");
					}
				}
			});
			if(materialids!="" && skus!="" && images!=""){
				materialApi.copyImageForProduct({"sku":skus,"materialid":materialids,"image":images}).then((res)=>{
					    var data=res.data;
						if(data && data.length > 0){
							var msg = "";
							for(var i = 0; i < data.length; i++){
								msg += data[i].sku + ";";
							}
							ElMessage.error(msg + ":同步失败!");
						}else{
							ElMessage.success( "同步成功!");
							state.pictureDialog=false;
							changeData();
						}
				});
			}else{
				ElMessage.error("请选择同步产品!");
			}
			
		}
		function submitSize(){
				var list=[];
				state.pictureTable.forEach(function(item){
					if(item.ischeck==true){
					   list.push(item.info);
					}
				});
				if(list.length>0){
					materialApi.copyDimsForProduct(list).then((res)=>{
						    var data=res.data;
							if(data && data.length > 0){
								var msg = "";
								for(var i = 0; i < data.length; i++){
									msg += data[i].sku + ";";
								}
								ElMessage.error(msg + ":同步失败!");
							}else{
								ElMessage.success( "同步成功!");
								state.pictureDialog=false;
								changeData();
							}
					});
				}else{
					ElMessage.error("请选择同步产品!");
				}
		}
		function clearSearch(){
			if(ownerRef.value){
				ownerRef.value.ownerid="";
				state.queryParam.owner="";
			}
			if(categoryRef.value){
				categoryRef.value.state.category=""
				state.queryParam.categoryid="";
			}
			state.searchType="sku";
			state.searchKeywords="";
			state.productType=="全部产品";
			state.queryParam.issfg="";
			state.queryParam.isDelete="0";
			state.isdelete="0";
			state.remarks="";
			state.queryParam.supplier="";
			state.queryParam.remark="";
			if(supplierRef.value){
				supplierRef.value.reset();
			}
			if(tagsRef.value){
				tagsRef.value.state.tagsValue="";
				state.queryParam.taglist=[];
			}
			
			changeData();
		}
		function clearPlan(){
			ElMessageBox.confirm('确认清空计划吗,会移除所有已加入的项！', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning',
			})
					.then(() => {
					 consumableApi.clear({"warehouseid":state.queryParam.warehouseid}).then((res)=>{
					 	ElMessage.success("计划清除成功!");
						submitQuery();
					 });
					})
			.catch(() => ElMessage.info('已取消操作'));
			
		}
		function getSummary(){
			consumableApi.getSummary({"warehouseid":state.queryParam.warehouseid}).then((res)=>{
				state.summary=res.data;
			});
		}
		function loadInvRate(){
			materialApi.consumableSafetyStockShow().then(res=>{
				state.invRate=res.data.amount;
			})
		}
		function submitInvRate(){
			
			if(state.invRate>0&&state.invRate<100){
				materialApi.consumableSafetyStockSave({amount:state.invRate}).then(res=>{
					ElMessage.success( "修改成功!");
					state.setVisable=false;
					loadInvRate();
					submitQuery();
				})
			}else{
				ElMessage.error("必须在0-100之间!");
			}
			
		}
		function showSetDialog(){
			state.setVisable=true;
			loadInvRate();
		}
		defineExpose({
		  selectRows,submitQuery,getSummary,
		})	
</script>

<style scoped>
	.font-large{
		font-size: 48px;
		color: #999;
	}
</style>
