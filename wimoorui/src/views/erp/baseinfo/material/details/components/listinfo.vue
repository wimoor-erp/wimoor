<template>
	<div class="right-align-list">
	<h3 id="base"  class="tab-scroll">基础信息</h3>
	<el-descriptions  :column="1"  size="default" >
		<el-descriptions-item label="产品图片" v-if="type!='package'" :span="2">
			<el-image
			      v-if="baseInfo.image && baseInfo.image!=undefined" 
				  style="width: 148px; height: 148px" 
				  :src="baseInfo.image"
			      :zoom-rate="1.2"
			      :preview-src-list="baseInfo.images"
			      :initial-index="4"
			      fit="cover"
			    />
			<el-image v-else style="width: 148px; height: 148px;"  :src="$require('empty/noimage40.png')" alt="暂无图片" ></el-image>
			<el-button link type="info"  v-if="baseInfo.location && baseInfo.location!=undefined"  style="position: relative;top: -135px;margin-left: 25px;">扩展图片</el-button>
			<el-image
			      v-if="baseInfo.location && baseInfo.location!=undefined" 
				  style="width: 148px; height: 148px;margin-left: 35px;" 
				  :src="baseInfo.location"
			      :zoom-rate="1.2"
			      :preview-src-list="baseInfo.location"
			      :initial-index="4"
			      fit="cover"
			    />
			 
		</el-descriptions-item>
	    <el-descriptions-item label="产品名称"  :span="2">{{baseInfo.name}}</el-descriptions-item>
	    <el-descriptions-item  label="SKU" :span="2">{{baseInfo.sku}}</el-descriptions-item>
		<el-descriptions-item  v-if="type=='product'" label="产品类型" :span="2">
			 <el-tag v-if="baseInfo.issfg=='0'" type="success" class="ml-2"  effect="plain">单独产品</el-tag>
			 <el-tag v-if="baseInfo.issfg=='1'" type="warning" class="ml-2" effect="plain">组合产品</el-tag>
			 <el-tag v-if="baseInfo.issfg=='2'" type="danger" class="ml-2" effect="plain">待组装产品</el-tag>
		</el-descriptions-item>
	    <el-descriptions-item   v-if="type=='product'" label="品牌" :span="2">
			{{baseInfo.brand}}
		</el-descriptions-item>
		<el-descriptions-item  label="规格" :span="2">
			{{baseInfo.specification}}
		</el-descriptions-item>
		<el-descriptions-item  label="条形码" :span="2">
			{{baseInfo.upc}}
		</el-descriptions-item>
		<el-descriptions-item label="品类" :span="2">{{baseInfo.category}}</el-descriptions-item>
	    <el-descriptions-item label="产品标签" v-if="type=='product'">
			<el-space>
	      <span class='tag-mr' v-if='tagNameList'  v-for='(s,index) in tagNameList' :key='index'>
	          <el-tag  effect="plain" :title="s.name" :type="s.color">  {{s.name}}</el-tag>
	      </span>
	      <span v-else>-</span>
		  </el-space>
	    </el-descriptions-item>
		
		<el-descriptions-item label="负责人" v-if="type=='product'" :span="2">{{baseInfo.ownername}}</el-descriptions-item>
		<el-descriptions-item label="生效日期" v-if="type=='product'" :span="2">{{dateFormat(baseInfo.effectivedate)}}</el-descriptions-item>
		<el-descriptions-item label="创建日期" :span="2">{{dateFormat(baseInfo.createdate)}}</el-descriptions-item>
		<el-descriptions-item label="创建人" :span="2">{{baseInfo.creator}}</el-descriptions-item>
		<el-descriptions-item label="修改日期" :span="2">{{baseInfo.opttime}}</el-descriptions-item>
		<el-descriptions-item label="修改人" :span="2">{{baseInfo.operator}}</el-descriptions-item>
	    <el-descriptions-item label="备注">
	     <div v-html="baseInfo.remarkHtml"></div>   
	    </el-descriptions-item>
	  </el-descriptions>
	 <el-divider />
	 <div v-if="baseInfo.issfg=='2'&&parentList&&type=='product'">
	 <h3 id="parent" class="tab-scroll">父SKU信息</h3>
		<el-descriptions  :column="1"  size="default" >
			 <el-descriptions-item label="父SKU列表" :span="2" class-name="flex-one">
			 <el-table border :data="parentList">
			 	<el-table-column width="50" type="index">
			 	</el-table-column>
			 	<el-table-column width="62" label="图片">
			 		 <template #default="scope">
			 			<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
			 			<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
			 		 </template>
			 	</el-table-column>
			 	<el-table-column label="名称/父SKU">
			 		<template #default="scope">
			 			<div class='name'>{{scope.row.name}}</div>
			 			<div class='sku'>{{scope.row.sku}} </div>
			 		</template>
			 	</el-table-column>
			 	<el-table-column label="单位数量" prop="MOQ" width="100" ></el-table-column>
				<el-table-column label="可用库存" prop="fulfillable" width="100" ></el-table-column>
				<el-table-column label="待入库" prop="inbound" width="100" ></el-table-column>
				<el-table-column label="待出库" prop="outbound" width="100" ></el-table-column>
			 </el-table>
			 </el-descriptions-item>
		</el-descriptions>
	 <el-divider />
	 </div>
	 <div v-if="baseInfo.issfg=='1'&&assemblyList&&type=='product'" >
	 <h3 id="children" class="tab-scroll">子SKU信息</h3>
	 <el-descriptions  :column="1"  size="default" >
		 <el-descriptions-item label="子SKU列表" :span="2" class-name="flex-one">
		 <el-table border :data="assemblyList">
		 	<el-table-column width="50" type="index">
		 	</el-table-column>
		 	<el-table-column width="62" label="图片">
					 <template #default="scope">
						<el-image  @click.stop="gotoDetails(scope.row.submid)" v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
						<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
					 </template>
		 	</el-table-column>
		 	<el-table-column label="名称/子SKU">
		 		<template #default="scope">
		 			<div class='name'>{{scope.row.mname}}</div>
		 			<div class='sku' @click.stop="gotoDetails(scope.row.submid)" >{{scope.row.sku}} </div>
		 		</template>
		 	</el-table-column>
		 	<el-table-column label="成本单价" width="300" >
		 		<template #default="scope">
		 			<el-popover
		 			    placement="top-start"
		 			    title="历史价格"
		 			    :width="200"
		 			    trigger="hover"
		 			  >
		 			  <template #reference>
		 			   <span class="pointer" v-html="scope.row.subprice"></span> 
		 			  </template>
		 			  <span  v-html="scope.row.historyprice"> </span>
		 			  </el-popover>
		 		</template>
		 	</el-table-column>
		 	<el-table-column label="单位数量" prop="subnumber" width="120" >
		 	</el-table-column>
		 	<el-table-column label="可用库存" prop="fulfillable"  width="150" />
		 </el-table>
		 </el-descriptions-item>
	 </el-descriptions>
	 <el-divider />
	 </div>
	<h3 id="purchase" class="tab-scroll">采购信息</h3>
	<el-descriptions  :column="1"  size="default" >
		<!-- <el-descriptions-item label="采购员" :span="2">李四</el-descriptions-item> -->
		<el-descriptions-item label="采购成本" :span="2">
			<span v-if="baseInfo.price">{{baseInfo.price}}</span>
			<span v-else>0.00</span>
		元</el-descriptions-item>
		<el-descriptions-item label="其它成本" :span="2">
			<span v-if="baseInfo.otherCost">{{baseInfo.otherCost}}</span>
			<span v-else>0.00</span>
		元
		</el-descriptions-item>
		<el-descriptions-item label="退税率" :span="2">{{baseInfo.vatrate}}</el-descriptions-item>
		<el-descriptions-item label="采购加权成本" :span="2">
			<span v-if="baseInfo.priceWavg">{{baseInfo.priceWavg}}</span>
			<span v-else>0.00</span>
				元
		</el-descriptions-item>
		<el-descriptions-item label="采购加权运费" :span="2">
			<span v-if="baseInfo.priceShipWavg">{{baseInfo.priceShipWavg}}</span>
			<span v-else>0.00</span>
		元
		</el-descriptions-item>
		<el-descriptions-item label="供货周期(天)" :span="2">{{baseInfo.deliveryCycle}}</el-descriptions-item>
		<el-descriptions-item label="供应商" :span="2" class-name="flex-one">
			<el-table :data="supplierList" border >
				<el-table-column type="index"></el-table-column>
				<el-table-column label="供应商名称" prop="name">
					<template #default="scope">
						<div>{{scope.row.name}}
						<el-tag type="success" v-if="scope.row.isdefault"> 默认</el-tag>
						<el-tag type="info" v-else> 备选</el-tag>
						</div>
						<div><span class="font-extraSmall">供货周期:</span>{{scope.row.deliverycycle}}天</div>
						<div><span class="font-extraSmall">采购链接:</span>{{scope.row.purchaseUrl}} 
						     <el-link target="_blank" :href="scope.row.purchaseUrl" type="primary">
							     <el-icon><Link /></el-icon>打开链接
						     </el-link>
					    </div>
					</template>
				</el-table-column>
				<el-table-column label="采购阶梯价" width="270" >
					<template #default="scope">
						<el-table :data="scope.row.stepList" class="small-tab" border>
							<el-table-column label="采购量">
								<template #default="scope">
									<span>{{scope.row.amount}}</span>
								</template>
							</el-table-column>
							<el-table-column label="币种" prop="currency" >
								<template #default="scope">
									RMB
								</template>
							</el-table-column>
							<el-table-column label="采购单价" prop="price"/>
						</el-table>
					</template>
				</el-table-column>
				<el-table-column label="其他信息" prop="productCode" width="310">
					<template #default="scope">
					<div><span class="font-extraSmall">不良率(%):</span>{{scope.row.badrate}}</div>
					<div><span class="font-extraSmall">1688代码:</span>{{scope.row.productCode}}</div>
                    <div><span class="font-extraSmall">下单备注:</span>{{scope.row.remark}}</div>					
					</template>
				</el-table-column>
			</el-table>
		</el-descriptions-item>
	 </el-descriptions>
	 <el-divider />
	 <h3 id="specs" class="tab-scroll">规格信息</h3>
	 <el-descriptions  :column="1"  size="default" >
	 	<el-descriptions-item label="净品规格" :span="2" v-if="type=='product'">
			<el-space :size="32">
			 <span v-if="itemDim">
				 <span>{{itemDim.length}}x{{itemDim.width}}x{{itemDim.height}} cm</span>
				 <span>{{'   '}}{{itemDim.weight}}kg</span>
			 </span>
			 <span v-else>
			 		--		 
			 </span>
			</el-space>
		</el-descriptions-item>
		<el-descriptions-item label="带包装规格" :span="2">
			<el-space :size="32">
			<span v-if="pkgDim">
				 <span>{{pkgDim.length}}x{{pkgDim.width}}x{{pkgDim.height}} cm</span>
				 <span>{{'   '}}{{pkgDim.weight}}kg</span>
			</span>
			<span v-else>
					--		 
			</span>
			</el-space>
		</el-descriptions-item>
		<el-descriptions-item label="单箱规格" :span="2" v-if="type=='product'">
			<el-space :size="32">
			<span v-if="boxDim">
				 <span>{{boxDim.length}}x{{boxDim.width}}x{{boxDim.height}} cm</span>
				 <span>{{'   '}}{{boxDim.weight}}kg</span>
			</span>
			<span v-else>
					--		 
			</span>
			</el-space>
		</el-descriptions-item>
		<el-descriptions-item label="单箱数量" :span="2" v-if="type=='product'">
			{{baseInfo.boxnum}}
		</el-descriptions-item>
	  </el-descriptions>
	  <el-divider  v-if="consPList&&consPList.length>0&&type=='consumable'" />
	  <h3 id="consumablePs" v-if="consPList&&consPList.length>0&&type=='consumable'" class="tab-scroll">辅料关联</h3>
	  <el-descriptions  v-if="consPList&&consPList.length>0&&type=='consumable'"  :column="1"  size="default" >
	  		  <el-descriptions-item label="主产品" :span="2" class-name="flex-one">
	  		  	<el-table border :data="consPList">
	  		  			 <el-table-column width="50"  type="index"> 
	  		  			 			 <template #header>
	  									 序号
	  		  			 			 </template>
	  		  			 </el-table-column>
	  		  			 <el-table-column width="80" label="图片">
	  		  				<template #default="scope">
	  		  					<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
	  		  					<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
	  		  				</template>
	  		  			 </el-table-column>
	  		  			 <el-table-column label="辅料名称/辅料SKU" width="400">
	  							 <template #default="scope">
	  								<div class='name'>{{scope.row.name}}</div>
	  								<div class='sku'>{{scope.row.sku}} </div>
	  							 </template>
	  		  			 </el-table-column>
	  		  			 <el-table-column label="单价" prop="price">
	  		  			 </el-table-column>
	  		  			 <el-table-column label="关联数量" prop="amount">
	  		  			 </el-table-column>
						 <el-table-column label="库存" prop="fulfillable">
						 </el-table-column>
						 <el-table-column label="待入库" prop="inbound">
						 </el-table-column>
						 <el-table-column label="需求量" prop="needPlan">
						 </el-table-column>
	  		  	</el-table>
	  		  </el-descriptions-item>
	  </el-descriptions>
	  	  <el-divider  v-if="consList&&consList.length>0&&type=='product'" />
	  <h3 id="consumables" v-if="consList&&consList.length>0&&type=='product'" class="tab-scroll">辅料关联</h3>
	  <el-descriptions  v-if="consList&&consList.length>0&&type=='product'"  :column="1"  size="default" >
		  <el-descriptions-item label="辅料" :span="2" class-name="flex-one">
		  	<el-table border :data="consList">
		  			 <el-table-column width="50"  type="index"> 
		  			 			 <template #header>
									 序号
		  			 			 </template>
		  			 </el-table-column>
		  			 <el-table-column width="80" label="图片">
		  				<template #default="scope">
		  					<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
		  					<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
		  				</template>
		  			 </el-table-column>
		  			 <el-table-column label="辅料名称/辅料SKU" width="400">
							 <template #default="scope">
								<div class='name'>{{scope.row.name}}</div>
								<div class='sku'>{{scope.row.sku}} </div>
							 </template>
		  			 </el-table-column>
		  			 <el-table-column label="单价" prop="price">
		  			 </el-table-column>
		  			 <el-table-column label="关联数量" prop="amount">
		  			 </el-table-column>
		  	</el-table>
		  </el-descriptions-item>
	  </el-descriptions>
	  <el-divider v-if="(logistics||baseInfo.addfee)&&type=='product'" />
	  <div  v-if="(logistics&&logistics.length>0&&type=='product')||(baseInfo.addfee&&type=='product')">
			<h3 id="logistics"  class="tab-scroll">物流信息</h3>
			  <el-descriptions  v-if="baseInfo.addfee" :column="1"  size="default" >
					<el-descriptions-item label="附加费" :span="2" >
						 {{baseInfo.addfee}}
					</el-descriptions-item>
			   </el-descriptions>
			  <el-table v-if="logistics&&logistics.length>0&&type=='product'"  border :data="logistics">
			  		<el-table-column label="国家" prop="country">
			  		</el-table-column>
			  		<el-table-column label="海关编码(必填)" prop="code">
			  		</el-table-column>
			  		<el-table-column label="中文名" prop="cname">
			  		</el-table-column>
			  		<el-table-column label="英文名" prop="ename" >
			  		</el-table-column>
			  		<el-table-column label="报价" prop="price">
			  		</el-table-column>
			  		<el-table-column label="材质(英文)"  prop="material">
			  		</el-table-column>
					<el-table-column label="材质(中文)"  prop="materialcn">
					</el-table-column>
			  		<el-table-column label="用途" prop="application" >
			  		</el-table-column>
			  		<el-table-column label="税率">
			  			  <template #default="scope"> {{CheckInputFloat(scope.row.rate)}}  %  </template> 
			  		</el-table-column>
			  </el-table>
	  <el-divider />
 </div>
	  <h3 id="inv" class="tab-scroll">库存信息  </h3>
	   <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
	      <el-tab-pane label="本地仓" name="11">
			  <el-select style="margin-bottom: 5px;" v-model="localtype" @change="changeLocalWarehouse">
				  <el-option key="self_usable" value="self_usable" label="正品仓">正品仓</el-option>
				  <el-option key="self_test" value="self_test" label="测试仓">测试仓</el-option>
				  <el-option key="self_unusable" value="self_unusable" label="废品仓">废品仓</el-option>
			  </el-select>
			  <el-button style="margin-bottom: 5px;float: right;" type="primary" size='small' @click.stop="submitInv">提交本地信息</el-button>
			  <el-table border :data="inventoryList">
				  <el-table-column label="仓库" prop="warehouse"></el-table-column>
				  <el-table-column label="安全库存周期" prop="stockingCycle" width="120">
					  <template #default="scope">
						  <el-input v-model="scope.row.stockingCycle" @input="scope.row.stockingCycle=CheckInputInt(scope.row.stockingCycle)" ></el-input>
					  </template>
				  </el-table-column>
				 <!-- <el-table-column label="最小补货周期" prop="mincycle" width="120">
					  <template #default="scope">
					  		 <el-input v-model="scope.row.mincycle"  @input="scope.row.mincycle=CheckInputInt(scope.row.mincycle)"></el-input>
					  </template>
				  </el-table-column> -->
				  <el-table-column label="待入库" prop="inbound"></el-table-column>
				  <el-table-column label="可用" prop="fulfillable"></el-table-column>
				  <el-table-column label="待出库" prop="outbound"></el-table-column>
				 <!-- <el-table-column label="操作" prop="id">
					  <template #default="scope">
						  <el-button v-if="scope.row.fulfillable && scope.row.fulfillable>0" link type="primary" @click.stop="dispatchInv(scope.row)" >调库</el-button>
						  <el-button v-else link type="primary"  disabled >调库</el-button>
					  </template>
				  </el-table-column> -->
			  </el-table>
		  </el-tab-pane>
	      <el-tab-pane v-if="type=='product'" label="FBA仓库" name="12">
			   <el-button style="margin-bottom: 5px;float: right;" type="primary" size='small' @click.stop="submitInvFBA">提交FBA信息</el-button>
			  <el-table border :data="fbainventoryList">
					  <el-table-column label="仓库" prop="name"></el-table-column>
					  <el-table-column label="店铺" prop="gname"></el-table-column>
					  <el-table-column label="SKU" prop="sku"></el-table-column>
					  <el-table-column label="安全库存周期(天)" prop="stockingCycle" width="120">
						  <template #default="scope">
							   <el-input  v-loading="scope.row.loading" v-model="scope.row.stockingCycle" @input="scope.row.stockingCycle=CheckInputInt(scope.row.stockingCycle)" ></el-input>
						  </template>
					  </el-table-column>
					  <el-table-column label="发货频率(天)" prop="mincycle" width="120">
						  <template #default="scope">
								<el-input  v-loading="scope.row.loading" v-model="scope.row.mincycle" @input="scope.row.mincycle=CheckInputInt(scope.row.mincycle)" ></el-input>
						  </template>
					  </el-table-column>
					 <el-table-column label="头程运费费用" prop="firstlegcharges" width="120">
							  <template #default="scope">
								  	<el-input v-loading="scope.row.loading" v-model="scope.row.firstlegcharges" @input="scope.row.firstlegcharges=CheckInputFloat(scope.row.firstlegcharges)" ></el-input>
							  </template>
					  </el-table-column> 
					  <el-table-column label="可售" prop="afnFulfillableQuantity"></el-table-column>
					  <el-table-column label="正在发货" prop="afnInboundWorkingQuantity"></el-table-column>
					  <el-table-column label="待接收" prop="afnInboundShippedQuantity"></el-table-column>
					  <el-table-column label="正在接收" prop="afnInboundReceivingQuantity"></el-table-column>
					  <el-table-column label="不可售" prop="afnUnsellableQuantity"></el-table-column>
			  </el-table>
		  </el-tab-pane>
	    </el-tabs>
	 </div>
</template>

<script setup>
import { ref,reactive,toRefs,} from 'vue'
import {Edit,Link} from '@element-plus/icons-vue';
import {useRouter } from 'vue-router'
import {ElMessage } from 'element-plus';
import {dateFormat} from '@/utils/index.js';
import {CheckInputFloat,CheckInputInt,decodeText} from '@/utils/index.js';
import inventoryApi from '@/api/erp/inventory/inventoryApi.js';
import inventoryRptApi from '@/api/amazon/inventory/inventoryRptApi.js';
import fbaCycleApi from '@/api/amazon/inbound/fbacycleApi.js';
import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';

		let router = useRouter();
		const type=router.currentRoute.value.query.type?router.currentRoute.value.query.type:"product";
		
        let state = reactive({
			activeName:'11',
			baseInfo:{},
			tagNameList:[],
			assemblyList:[],
			supplierList:[],
			itemDim:{},
			pkgDim:{},
			boxDim:{},
			consList:[],
			consPList:[],
			logistics:{},
			logisItemList:[],
			parentList:[],
			inventoryList:[],
			fbainventoryList:[],
			localtype:'self_usable',
			nowid:'',
		})
		
		let{
			activeName,baseInfo,tagNameList,assemblyList,supplierList,
			itemDim,pkgDim,boxDim,consList,consPList,logistics,logisItemList,parentList,inventoryList,fbainventoryList,localtype,nowid
		}=toRefs(state)
		function loadData(datas){
			state.baseInfo=datas.material;
			state.baseInfo.remarkHtml=decodeText(state.baseInfo.remark);
			state.baseInfo.images=[];
			state.baseInfo.images.push(state.baseInfo.image);
			state.tagNameList=datas.tagNameList;
			state.assemblyList=datas.assemblyList;
			state.supplierList=datas.supplierList;
			state.itemDim=datas.itemDim;
			state.pkgDim=datas.pkgDim;
			state.boxDim=datas.boxDim;
			state.consList=datas.consumableList;
			state.consPList=datas.consumablePList;
			if(state.consPlist){
				state.consPlist.forEach(item=>{
					item.tadaye=32432;
				})
			}
			state.logistics=datas.customs;
			state.logisItemList=datas.customsItemList;
			state.parentList=datas.parentList;
			loadInventory(datas.material.id,datas.material.sku);
		}
		function gotoDetails(mids){
			router.push({
				path:'/material/details/children',
				query:{
				  title:"子产品详情",
				  path:'/material/details/children',
				  details:mids,
				  type:"product",
				}
			});
		}
		function changeLocalWarehouse(val){
			 inventoryApi.list({"id":state.nowid,"ftype":val}).then((res)=>{
			 	if(res.data && res.data.length>0){
			 		state.inventoryList=res.data;
			 	}
			 });
		}
		function loadInventory(id,sku){
			state.nowid=id;
			inventoryApi.list({"id":id,"ftype":"self_usable"}).then((res)=>{
				if(res.data && res.data.length>0){
					state.inventoryList=res.data;
				}
			});
			inventoryRptApi.list({"sku":sku}).then((res)=>{
				if(res.data && res.data.length>0){
					state.fbainventoryList=res.data;
				}
			});
		}
		function dispatchInv(rows){
			var skuid=rows.skuid;
			var sku=rows.sku;
			var warehouseid=rows.id;
			var warehouse=rows.warehouse;
			//跳转至调库
			router.push({
				path:'/material/details',
				query:{
				  title:"产品详情",
				  path:'/material/details',
				  warehouseid:warehouseid,
				  sku:sku,
				  warehouse:warehouse,
				  skuid:skuid
				}
			})
		}
	 function submitInvFBA(){
			var list=[];
			for(var i=0;i<state.fbainventoryList.length;i++){
				var item=state.fbainventoryList[i];
				item.loading=true;
				list.push({
						 "marketplaceid":item.marketplaceid,
						 "groupid":item.groupid,
						 "sku":item.sku,
						 "cycle":item.stockingCycle,
						 "mincycle":item.mincycle,
						 "fee":item.firstlegcharges})
			}
		  fbaCycleApi.updateStockByChange(list).then((res)=>{
		 	state.fbainventoryList.forEach(item=>{
				item.loading=false;
			});
			ElMessage.success('批量操作成功！');
		 }).catch(e=>{
		 	state.fbainventoryList.forEach(item=>{
		 		item.loading=false;
		 	})
		 })
		 
		}
		 
		function submitInv(){
			var isok=true;
			var list=[];
			state.inventoryList.forEach(function(item){
				if(item.stockingCycle && item.mincycle){
					list.push({"materialid":item.skuid,"warehouseid":item.id,"stockingcycle":item.stockingCycle, "mincycle":item.mincycle});
				}
			});
			warehouseApi.updateStockByChange(list).then((res)=>{
				if(res.data==false){
					isok=false;
				}
			});
			if(isok==true){
				ElMessage.success('批量操作成功！');
			}else{
				ElMessage.error('操作失败！');
			}
		}
		
		defineExpose({
		   loadData
		})
				
	
</script>

<style>
.flex-one{
	flex: 1;
}	
.right-align-list 	.el-descriptions__cell{
	display: flex;
}
.right-align-list .el-descriptions__label{
	width:100px;
	text-align: right;
	padding-right:16px;
	flex:none;
}
</style>
