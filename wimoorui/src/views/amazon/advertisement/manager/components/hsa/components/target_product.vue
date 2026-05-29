<template>
  <el-tabs type="border-card" v-model="activeTab" class="p-l"  @tab-change="handleTargetTypeChange">
					<el-tab-pane label="分类" name="category">
						<el-tabs v-model="activeName" @tab-change="handleCateChange">
							<el-tab-pane label="建议分类" name="suggestCate" v-loading="cateloading" >
								<BiddingType  @change="handleListBid" :bidType="bidType"  ></BiddingType>
								<el-table height="350" :data="suggestCategoryList">
									<el-table-column  >
										<template #header>
										 <div class="class-header font-small">
										 	<span>商品分类({{suggestCategoryList.length}} 个建议分类)</span>
										 </div>
										 </template>
										<template #default="scope">
											<div class="font-extraSmall">{{scope.row.path}}({{scope.row.translatedCategoryPath}})</div>
											<div>分类:{{scope.row.name}}({{scope.row.translatedName}})</div>
											<div class="font-extraSmall">商品:{{scope.row.AsinCountsMin}}-{{scope.row.AsinCountsMax}}件</div>
										</template>
									</el-table-column>
									<el-table-column label="建议竞价"  width="100">
										<template #default="scope">
											<el-button link :loading="true" v-if="scope.row.bidloading">
											</el-button>
												
											<div v-else>
												<div  class="font-extraSmall">
													 {{scope.row.suggestedBid?.suggested}}
												</div>
												<div class="font-extraSmall"> {{scope.row.suggestedBid?.rangeStart}}-{{scope.row.suggestedBid?.rangeEnd}}
												</div>
											</div>
												
											 
										</template>
									</el-table-column>
									<el-table-column  width="120" >
										<template #header>
											<el-button type="primary" size="small" link>添加全部</el-button>
										</template>
										<template #default="scope">
											<el-button type="primary" @click="addCateTarget(scope.row)" link>添加</el-button>
											<el-button @click="handleRefine(scope.row)" type="primary" link size='small'>细化</el-button>
										</template>
									</el-table-column>
									 
								</el-table>
								
							</el-tab-pane>
							<el-tab-pane label="搜索" name="searchCate">
								<el-space>
								<el-input v-model="filterCate" size="small" placeholder="按分类搜索..."  @change="changeFilter" >
									<template #suffix>
										<el-icon>
											<Search @click="changeFilter"  />
										</el-icon>
									</template>
								</el-input>
								<BiddingType  @change="handleListBid" :bidType="bidType" ></BiddingType>
								</el-space>
								<el-scrollbar height="350px">
									<!-- load="loadCateById" -->
								<el-tree ref="treeRef" :filter-node-method="handleFilter"
								 class="m-t-8" :data="classtree" height=""  v-loading="treeloading" 
								 :render-after-expand="true"
								
								  :props="defaultProps" node-key="categoryRefinementId"
									@node-click="loadCateById">
									<template #default="scope">
										<div class="item-node flex-center-between">
											<el-space>
											<div><span>{{scope.node.label}}</span>	</div>
											  <div class="font-extraSmall">ASIN个数:{{scope.node.data.asinCountRange.min}}-{{scope.node.data.asinCountRange.max}}	</div>   
											</el-space>
											<div style="padding-right:20px">
												<el-button size="small" link type="primary" @click.stop="addCateTreeTarget(scope.node)">添加</el-button>
												<el-button size="small" link type="primary" @click.stop="handleTreeRefine(scope.node)">细化</el-button>
											</div>
										</div>
									</template>
								</el-tree>
								</el-scrollbar>
								
							</el-tab-pane>
						</el-tabs>
					</el-tab-pane>
					<el-tab-pane label="商品" name="asin">
						<el-tabs v-model="activeProduct" @tab-change="handleAsinChange">
							<el-tab-pane label="建议商品" name="suggestAsin">
								<el-space>
									<el-checkbox-group disabled size="small" v-model="asinTargetType" @change="changeTargetType">
										<el-checkbox label="ASIN_SAME_AS">精准</el-checkbox>
										<!-- <el-checkbox label="ASIN_EXPANDED_FROM">扩展</el-checkbox> -->
									</el-checkbox-group>
									<BiddingType  @change="handleListBid" :bidType="bidType"  ></BiddingType>
								</el-space>
								<el-table :data="suggestProductList" height="368px" v-loading="suggestasinLoading">
									<el-table-column label="产品信息" show-overflow-tooltip>
										<template #header>
											<div>
												产品信息
											</div>
										</template>
										<template #default="scope">
											<el-space v-loading="scope.row.infoloading">
											<el-image v-if="scope.row.image" :src="scope.row.image"
												class="product-img"></el-image>
											<el-image v-else :src="$require('empty/noimage40.png')"
												class="product-img"></el-image>
												<div>
													<div>{{scope.row.name}}</div>
													<div class="font-extraSmall">品牌:{{scope.row.brand}}</div>
												</div>
												</el-space>
										</template>
									</el-table-column>
									<el-table-column label="ASIN" prop="recommendedTargetAsin"  show-overflow-tooltip>
										<template #default="scope">
											<div>{{scope.row.recommendedTargetAsin}}</div>
											<!-- <div class="font-extraSmall">{{scope.row.themes}}</div> -->
										 </template>
									</el-table-column>
									<el-table-column label="竞价"  width="100">
										<template #default="scope">
											<el-button link :loading="true" v-if="scope.row.bidloading">
											</el-button>
												
											<div v-else>
												<div  class="font-extraSmall">
													 {{scope.row.suggestedBid?.suggested}}
												</div>
												<div class="font-extraSmall"> {{scope.row.suggestedBid?.rangeStart}}-{{scope.row.suggestedBid?.rangeEnd}}
												</div>
											</div>
										</template>
									</el-table-column>
									<el-table-column label="操作" width="110">
										<template #header>
											<el-button type="primary" size="small" link>添加全部</el-button>
										</template>
										<template #default="scope">
											<el-button type="primary" @click.stop="addAsinTarget(scope.row)" link>添加</el-button>
										</template>
									</el-table-column>
								</el-table>
							</el-tab-pane>
							<el-tab-pane label="搜索" name="searchAsin">
								<el-space>
								<el-input size="small" placeholder="搜索ASIN或商品名称..." style="width:200px;" v-model="searchAsinKey" @change="loadSearchAsin">
									<template #suffix>
										<el-icon>
											<Search @click.stop="loadSearchAsin"/>
										</el-icon>
									</template>
								</el-input>
								<el-checkbox-group disabled v-model="asinTargetType" @change="changeTargetType" size="small">
									<el-checkbox label="ASIN_SAME_AS">精准</el-checkbox>
									<!-- <el-checkbox label="ASIN_EXPANDED_FROM">扩展</el-checkbox> -->
								</el-checkbox-group>
								<BiddingType  @change="handleListBid" :bidType="bidType" ></BiddingType>
								</el-space>
								
								<el-table class="m-t-8" :data="suggestAsinProductList" v-loading="searchAsinloading" height="340px">
									<el-table-column label="产品信息" show-overflow-tooltip>
										<template #header>
											<el-space >
											<div>产品信息</div>
											<div class="font-extraSmall"  >总计：{{asinParam.numberOfResults}} 条记录</div>
											 <div class='rt-btn-group'  >
											             <el-button-group>
																<el-button link type="primary" 
																 @click.stop="handleAsinPage(asinParam.previousToken)" 
																:icon="ArrowLeft" 
																:disabled="!asinParam.previousToken"> 
																</el-button>
																<el-button link type="primary" 
																 @click.stop="handleAsinPage(asinParam.nextToken)" 
																 :icon="ArrowRight"
																 :disabled="!asinParam.nextToken">
																</el-button>
											              </el-button-group>
											   </div>
										    </el-space>		
										</template>
										<template #default="scope">
											<el-space v-loading="scope.row.infoloading">
											<el-image v-if="scope.row.image" :src="scope.row.image"
												class="product-img"></el-image>
											<el-image v-else :src="$require('empty/noimage40.png')"
												class="product-img"></el-image>
												<div>
													<div>{{scope.row.name}}</div>
													<div class="font-extraSmall">品牌:{{scope.row.brand}}</div>
												</div>
												</el-space>
										</template>
									</el-table-column>
									<el-table-column label="ASIN" prop="asin"  width="130"/>
									<el-table-column label="竞价"  width="100">
										<template #default="scope">
											<el-button link :loading="true" v-if="scope.row.bidloading">
											</el-button>
												
											<div v-else>
												<div  class="font-extraSmall">
													 {{scope.row.suggestedBid?.suggested}}
												</div>
												<div class="font-extraSmall"> {{scope.row.suggestedBid?.rangeStart}}-{{scope.row.suggestedBid?.rangeEnd}}
												</div>
											</div>
												
											 
										</template>
									</el-table-column>
									<el-table-column label="操作" width="110">
										<template #header>
											<el-button type="primary" size="small" link>添加全部</el-button>
										</template>
										<template #default="scope">
											<el-button type="primary" @click="addAsinTarget(scope.row)" link>添加</el-button>
										</template>
									</el-table-column>
								</el-table>
							</el-tab-pane>
							<el-tab-pane label="填写ASIN" name="writeAsin">
								<el-space>
								<el-checkbox-group disabled size="small" v-model="asinTargetType" @change="changeTargetType">
									<el-checkbox label="ASIN_SAME_AS">精准</el-checkbox>
									<!-- <el-checkbox label="ASIN_EXPANDED_FROM">扩展</el-checkbox> -->
								</el-checkbox-group>
								<BiddingType  @change="handleListBid" :bidType="bidType"  ></BiddingType>
								</el-space>
								<el-input v-model="writeAsin" type="textarea" :autosize="{ minRows:8, maxRows:8}"
									placeholder="输入ASIN" />
								<el-button class="m-t-8" @click="addWriteAsin">添加</el-button>
							</el-tab-pane>
						</el-tabs>
					</el-tab-pane>
				</el-tabs>
				<div class="p-r">
					<span class="font-extraSmall m-l-8">已定位 <span> {{addList.length}}</span> 个分类&商品</span>
					<el-table :data="addList" height="448px">
						<el-table-column label="分类&商品" prop="name" show-overflow-tooltip>
							<template #default="scope">
								<el-space v-if="scope.row.asin" v-loading="scope.row.infoloading">
									<el-image v-if="scope.row.image" :src="scope.row.image"
										class="product-img"></el-image>
									<el-image v-else :src="$require('empty/noimage40.png')"
										class="product-img"></el-image>
										<div>
											<div  >{{scope.row.asin}}</div>
											<div class="font-extraSmall">{{scope.row.name}}</div>
									</div>
								</el-space>
								<div v-else v-loading="scope.row.infoloading">
									<div class="font-extraSmall">{{scope.row.path}}</div>
									<div>分类:{{scope.row.name}}</div>
									<div v-if="scope.row.brandname">品牌:{{scope.row.brandname}}</div>
									<div class="font-extraSmall">商品:{{scope.row.AsinCountsMin}}-{{scope.row.AsinCountsMax}}件</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column label="建议竞价" width="110">
							<template #default="scope">
								<el-space v-if="scope.row.suggestedBid?.suggested">${{scope.row.suggestedBid.suggested}}
									<arrow-circle-right title="应用" @click="scope.row.bids=scope.row.suggestedBid.suggested"
										class="pointer " theme="filled" size="16" fill="#67c23a" />
								</el-space>
								<p v-if="scope.row.suggestedBid?.suggested" class="font-extraSmall">{{scope.row.suggestedBid?.rangeStart}}-{{scope.row.suggestedBid?.rangeEnd}}</p>
							</template>
						</el-table-column>
						<el-table-column label="竞价" width="110">
							<template #default="scope">
								<el-input @input="emitChange" v-model="scope.row.bids" clearable size="small">
									<template #prefix>$</template>
								</el-input>
								<div v-if="scope.row.isExpend">类型:已扩展</div>
							</template>
						</el-table-column>
						<el-table-column label="操作" width="60">
							<template #header>
								<el-button type="primary" class="font-small" link @click="deleteRowAll">清空</el-button>
							</template>
							<template #default="scope">
								<el-button link type="primary" @click="deleteRow(scope.row,scope.$index)">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
 
	<el-dialog v-model="visible" :title="'细化分类:'+refineForm.row.name" width="480px">
		<el-form :model="refineForm" label-position="top" v-loading="refineloading">
			<el-form-item label="品牌">
				<el-select  
				    @change="changeRefine"
					multiple
					collapse-tags
					collapse-tags-tooltip
					v-model="refineForm.brand" 
					placeholder="请选择">
					<el-option v-for="item in refine.brands"  :key="item.id" :label="item.name" :value="item.id"    ></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="商品价格范围">
				<el-input v-model="refineForm.priceRange.min" @change="changeRefine" placeholder="最低" style="width:140px">
					<template #prepend>$</template>
				</el-input>
				-
				<el-input v-model="refineForm.priceRange.max" @change="changeRefine" placeholder="最高" style="width:140px">
					<template #prepend>$</template>
				</el-input>
			</el-form-item>
			<el-form-item label="查看星级评定(0-5星)">
				<el-row class="star-box">
					<el-slider v-model="refineForm.ratingArray" @change="changeRefine" range show-stops :max="5" :marks="marks" />
				</el-row>
			</el-form-item>
			<el-form-item label="配送" >
				<el-radio-group v-model="refineForm.isPrimeShipping" @change="changeRefine">
					<el-radio label="">所有</el-radio>
					<el-radio label="1">具备prime资格</el-radio>
					<el-radio label="0">不具备prime资格</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="竞价" >
				<el-input v-model="refineForm.bids"></el-input>
			</el-form-item>
		</el-form>
			<el-button link class="font-extraSmall" :loading="refineForm.countloading">商品:{{refineForm.AsinCountsMin}}-{{refineForm.AsinCountsMax}}件</el-button>
		<template #footer>
			<el-button @click="visible=false">取消</el-button>
			<el-button type="primary" @click.stop="addRefine">确认</el-button>
		</template>
	</el-dialog>
</template>

<script setup>
	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import listingApi from '@/api/amazon/listing/listingApi.js';
	import BiddingType from './bidding_type.vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import {
		ref,
		reactive,
		onMounted,
		toRefs,
		watch
	} from 'vue'
	import {
	Search,ArrowLeft,ArrowRight
	} from '@element-plus/icons-vue'
	import {
		ArrowCircleRight,
	} from '@icon-park/vue-next';
	//展示用
	const star = ref([2, 4]);
	const treeRef=ref();
	const marks = reactive({
		0: '0',
		2: '2',
		5: '5',
		1: '1',
		3: '3',
		4: '4',
	})
	const emit = defineEmits(['change','changetype']);
	const state = reactive({
		matchType:["EXACT"] ,
		asinTargetType:["ASIN_SAME_AS"],
		bidType:{type:"suggest",bid:""} ,
		targetType: "keyword",
		queryParams: {},
		suggestProductList: [],
		asinParam:{},
		suggestAsinProductList:[],
		classtree: [],
		tableData: [ ],
		refineForm:{"brand":[],"priceRange":{"max":null,"min":null},"ratingRange":{"min":0,"max":5},"isPrimeShipping":"","row":{},"ratingArray":[0,5]},
		refine:{},
		suggestCategoryList:[],
		activeTab:"category",
		activeName: 'suggestCate',
		activeProduct: "suggestAsin",
		defaultProps:{},
		addList: [],
		visible: false,
		keywordLoading:false,
		treeloading:false,
		rightloading:false,
		refineloading:false,
		suggestasinLoading:false,
		searchAsinloading:false,
		cateloading:false,
		searchAsinKey:"",
		mykeywords:'',
		filterCate:null,
		targetingType:"MANUAL",
		writeAsin:null,
	})
	const {
		filterCate,
		matchType,
		targetType,
		suggestProductList,
		suggestAsinProductList,
		activeTab,
		defaultProps,
		asinParam,
		classtree,
		queryParams,
		tableData,
		addList,
		refineForm,
		refine,
		activeName,
		suggestCategoryList,
		activeProduct,
		visible,
		keywordLoading,
		treeloading,
		mykeywords,
		rightloading,
		refineloading,
		suggestasinLoading,
		cateloading,
		targetingType,
		searchAsinloading,
		searchAsinKey,
		asinTargetType,
		bidType,
		writeAsin,
	} = toRefs(state)
     state.defaultProps.label="name";
	 state.defaultProps.children="ch";
    function loadData(){
			loadSuggestTargetCategories();
	}
	function show(params) {
		state.queryParams = params;
		loadData();
	}
	 
	defineExpose({
		show,
	})
	function changeTargetType(val){
		if(state.asinTargetType.length==0){
			ElMessage.error("至少保留一个选择！");
			state.asinTargetType=["ASIN_SAME_AS"];
		}
	}
	
	function handleTargetTypeChange(val){
		if(state.activeTab=="asin"){
			state.activeProduct= "suggestAsin";
			handleAsinChange(state.activeProduct);
		}else{
			state.activeName='suggestCate';
			handleCateChange(state.activeName);
		}
	}
	function handleAsinChange(){
		if(state.activeProduct=="suggestAsin"){
			loadSuggestTargetAsin();
		}
		if(state.activeProduct=="searchAsin"){
			loadSearchAsin();
		}
	}
	function handleAsinPage(token){
				if(token){
				   state.asinParam.pageToken=token;
				}
		loadSearchAsin();
	}
	
	function loadAsinSearchBid(){
		state.suggestAsinProductList.forEach(item=>{
			item.bidloading=true;
			var targets=[];
			var list=[];
			var param={"type":"asinSameAs","value": item.asin   };
			list.push(param);
			targets.push(list);
			advTargetApi.getTargetBidRecommendations({"profileid":state.queryParams.profileid,"expression":targets,
			"adtype":state.queryParams.campaignType,"adGroupid":state.queryParams.adgroupid,
			"campaignId":state.queryParams.campaignid}).then((res)=>{
				item.bidloading=false;
				if(res.data && res.data.length>0){
					var datas=res.data;
					if(datas && datas.length>0){
						//item.suggestedBid=datas[0].suggestedBid;
						item.suggestedBid=datas[0][0].recommendedBid;
						item.suggestedBid.suggested=item.suggestedBid.recommended;
					}
				}
			}).catch(()=>{
				item.bidloading=false;
			});
		});
	}
	
	function loadSearchAsin(){
		if(state.searchAsinKey){
			state.asinParam.marketplaceIds=[state.queryParams.marketplaceid];
			state.asinParam.sellerId=state.queryParams.sellerid;
			state.asinParam.keywords=[];
			state.asinParam.keywords.push(state.searchAsinKey);
			state.searchAsinloading=true;
			listingApi.searchCatalogProducts(state.asinParam).then(res=>{
						 state.searchAsinloading=false;
						 state.suggestAsinProductList=[];
						 var img="";
						 var height=1000000;
						 state.asinParam.nextToken=null;
						 state.asinParam.previousToken=null;
						 if(res&&res.data&&res.data.pagination&&res.data.pagination.nextToken){
							 state.asinParam.nextToken=res.data.pagination.nextToken;
						 }
						 state.asinParam.numberOfResults=res.data.numberOfResults;
						 if(res&&res.data&&res.data.pagination&&res.data.pagination.previousToken){
							 state.asinParam.previousToken=res.data.pagination.previousToken;
						 }
							 res.data.items.forEach(info=>{
								  var sug={};
								 info.images[0].images.forEach(image=>{
									if(image.variant =="MAIN"&&height>image.height){
										img=image.link;
									}
								 })
								 sug.name=info.summaries[0].itemName;
								 sug.brand=info.summaries[0].brand;
								 sug.asin=info.asin;
								 sug.image=img;
								 state.suggestAsinProductList.push(sug);
							 })	
							loadAsinSearchBid();
						 });
		}
		
	}
	function deleteRow(row,index){
	     state.addList.splice(index,1);
		 emitChange();
	}
	function deleteRowAll(){
		state.addList=[];
		emitChange();
	}
	function changeRefine(){
		var param={"category":state.refineForm.row.id};
	        param.brands=[]
			state.refine.brands.forEach(brand=>{
				if(state.refineForm.brand.includes(brand.id)){
					param.brands.push(brand);
				}
			});
			if(state.refineForm.isPrimeShipping=="1"){
				param.isPrimeShipping=true;
			}
			if(state.refineForm.isPrimeShipping=="0"){
				param.isPrimeShipping=false;
			}
			param.priceRange=state.refineForm.priceRange;
			param.ratingRange=state.refineForm.ratingRange;
			state.refineForm.countloading=true;
		  loadCategoryProductCount(param,datas=>{
			  state.refineForm.countloading=false;
			  if(datas){
				  state.refineForm.AsinCountsMin=datas.AsinCounts[0].min;
				  state.refineForm.AsinCountsMax=datas.AsinCounts[0].max;
			  }else{
				  state.refineForm.AsinCountsMin="";
				  state.refineForm.AsinCountsMax="";
			  }
			
		})
	}
	function addRefine(){
		if(state.refineForm.ratingArray){
			state.refineForm.ratingRange.min=state.refineForm.ratingArray[0];
			state.refineForm.ratingRange.max=state.refineForm.ratingArray[1];
		}
		state.refineForm.brand.forEach(brandid=>{
			var item=JSON.parse(JSON.stringify(state.refineForm.row));
			var mybrand=null;
			state.refine.brands.forEach(brand=>{
				if(brand.id==brandid){
					mybrand=brand;
				}
			});
			item.brandname=mybrand.name;
			item.bids=state.refineForm.bids;
			var expression=[];
			expression.push({"type":"asinCategorySameAs","value":state.refineForm.row.id+""});
			expression.push({"type":"ASIN_BRAND_SAME_AS","value":brandid});
			if(state.refineForm.ratingRange.min!=0&&state.refineForm.ratingRange.min!=5){
				expression.push({"type":"ASIN_REVIEW_RATING_BETWEEN","value":state.refineForm.ratingRange.min+"-"+state.refineForm.ratingRange.max});
			}
			else if(state.refineForm.ratingRange.min!=0){
				expression.push({"type":"ASIN_REVIEW_RATING_GREATER_THAN","value":state.refineForm.ratingRange.min});
			}
			else if(state.refineForm.ratingRange.max!=5){
				expression.push({"type":"ASIN_REVIEW_RATING_LESS_THAN","value":state.refineForm.ratingRange.max});
			}
			if(state.refineForm.priceRange.max && state.refineForm.priceRange.min){
				expression.push({"type":"ASIN_PRICE_BETWEEN","value":state.refineForm.priceRange.min+"-"+state.refineForm.priceRange.max});
			}
			else if(state.refineForm.priceRange.min){
			 	expression.push({"type":"ASIN_PRICE_GREATER_THAN","value":state.refineForm.priceRange.min});
			  }
			else if(state.refineForm.priceRange.max){
			  	expression.push({"type":"ASIN_PRICE_LESS_THAN","value":state.refineForm.priceRange.max});
			  }
			
			if(state.refineForm.isPrimeShipping=="1"){
				expression.push({"type":"ASIN_IS_PRIME_SHIPPING_ELIGIBLE","value":"true"});
			}
			if(state.refineForm.isPrimeShipping=="0"){
				expression.push({"type":"ASIN_IS_PRIME_SHIPPING_ELIGIBLE","value":"false"});
			}
			item.expression=expression;
			state.addList.push(item);
			state.visible = false;
			emitChange();
		})
		
		
	}
	
	function changeFilter(){
		treeRef.value.filter(state.filterCate);
	}
	function handleFilter(val,data){
		if(!val)return true;
		return data.name.includes(val);
	}
	
	function handleCateChange(val){
		if(state.activeName=='suggestCate'){
			loadSuggestTargetCategories()
		}else{
			loadCateById();
		}
	}
	//查询分类
	function  loadCateById(node){
		state.treeloading=true;
		var param={};
		var source="AMAZON";
		if(state.queryParams.campaign?.adFormat=="video"){
			source="STREAMING_VIDEO";
		}
		if(node){
			param.parentCategoryRefinementId=node.categoryRefinementId;
		}else{
			param.includeOnlyRootCategories=true;
		}
		
		param.supplySource=source;
		advTargetApi.getTargetsCategoriesAllRecommendations(state.queryParams.profileid,state.queryParams.campaignType,param).then((res)=>{
			state.treeloading=false;
			if(res.data){
				var data=JSON.parse(res.data);
				if(node){
					node.ch=data.categoryTree;
					node.children=data.categoryTree;
				}else{
					state.classtree= data.categoryTree ;
				}
			}
		});
	}  
	
	function addCateTarget(row){
		var expression=[];
		expression.push({"type":"asinCategorySameAs","value":row.id});
		row.expression=expression;
		handleBid(row);
		state.addList.push(row);
		emitChange();
	}
	
	function addWriteAsin(){
		if(state.writeAsin){
			var zhengze = /([\n\r])+/g;
			state.writeAsin = state.writeAsin.replace(zhengze, '\n');
			var asinlist = state.writeAsin.split('\n');
			asinlist.forEach(item=>{
				if(item.length==10){
					var row={};
					row.asin=item;
					handleBid(row);
					addAsinTarget(row);
				}
			});
			loadAsinInfo(state.addList);
		}
	}
	
	function addAsinTarget(row){
		 state.asinTargetType.forEach(item=>{
			 var expression=[];
			var rows=JSON.parse(JSON.stringify(row));
			 if(item=="ASIN_EXPANDED_FROM"){
				rows.isExpend=true;
			 }else{
				 rows.isExpend=false;
			 }
			 if(rows.recommendedTargetAsin){
				rows.asin=rows.recommendedTargetAsin; 
			 }
			 var value="";
			 if(item=="ASIN_SAME_AS"){
				 value="asinSameAs";
			 }
			 expression.push({"type":value,"value":rows.asin});
			 rows.expression=expression;
			 handleBid(rows);
			 state.addList.push(rows);
		 });
		emitChange();
	}
	
	function addCateTreeTarget(item){
		var row=item.data;
		var expression=[];
		expression.push({"type":"asinCategorySameAs","value":row.categoryRefinementId});
		row.expression=expression;
		row.name=row.name;
		handleBid(row);
		state.addList.push(row);
		emitChange();
	}
	
	function loadAllCategoryProductCount(){
		state.suggestCategoryList.forEach(item=>{
			var param={"category":item.id+""};
			loadCategoryProductCount(param,datas=>{
				if(datas){
					item.AsinCountsMin=datas.asinCounts.min;
					item.AsinCountsMax=datas.asinCounts.max;
				}
			})
		});
	}
	
	function loadCategoryProductCount(param,callback){
			advTargetApi.getRecommendationsProductCount(state.queryParams.profileid,state.queryParams.campaignType,param).then((res)=>{
				if(callback){
					if(res.data){
						var datas=JSON.parse(res.data);
						callback(datas);
					}else{
						callback(null);
					}
				}
			}).catch(()=>{
				if(callback){
						callback(null);
				}
			});
	}
	
	function loadSuggestTargetBid(){
		state.suggestCategoryList.forEach(item=>{
			item.bidloading=true;
			var targets=[];
			var list=[];
			var param={"type":"asinCategorySameAs","value":item.id+""   };
			list.push(param);
			targets.push(list);
			advTargetApi.getTargetBidRecommendations({"profileid":state.queryParams.profileid,"expression":targets,
			"adtype":state.queryParams.campaignType,"adGroupid":state.queryParams.adgroupid,
			"campaignId":state.queryParams.campaignid}).then((res)=>{
				item.bidloading=false;
				if(res.data && res.data.length>0){
					var datas=res.data;
					if(datas && datas.length>0){
						item.suggestedBid=datas[0][0].recommendedBid;
						item.suggestedBid.suggested=item.suggestedBid.recommended;
					}
				}
			}).catch(()=>{
				item.bidloading=false;
			});
		});
		
	}
	
	
	function loadAsinSuggestBid(){
		state.suggestProductList.forEach(item=>{
			item.bidloading=true;
			var targets=[];
			var list=[];
			var param={"type":"asinSameAs","value": item.recommendedTargetAsin   };
			list.push(param);
			targets.push(list);
			advTargetApi.getTargetBidRecommendations({"profileid":state.queryParams.profileid,"expression":targets,
			"adtype":state.queryParams.campaignType,"adGroupid":state.queryParams.adgroupid,
			"campaignId":state.queryParams.campaignid}).then((res)=>{
				item.bidloading=false;
				if(res.data && res.data.length>0){
					var datas=res.data;
					if(datas && datas.length>0){
						item.suggestedBid=datas[0][0].recommendedBid;
						item.suggestedBid.suggested=item.suggestedBid.recommended;
						//item.suggestedBid=datas[0].suggestedBid;
					}
				}
			}).catch(()=>{
				item.bidloading=false;
			});
		});
	}
	
	function loadAsinInfo(asinlist){
		 var list=[];
		 asinlist.forEach(item=>{
		 	if(item.asin){
		 		list.push(item.asin);
		 	}else{
		 		list.push(item.recommendedTargetAsin);
		 	}
			 item.infoloading=true;
		 });
		 listingApi.findAsinInfo({"asins":list,"marketplaceids":[state.queryParams.marketplaceid],"sellerid":state.queryParams.sellerid}).then((ress)=>{
		 	if(ress.data){
		 		var infoData={};
		 		ress.data.forEach(item=>{
		 			infoData[item.asin]=item;
		 		});
		 		asinlist.forEach(sug=>{
		 				sug.infoloading=false;
		 			var info=infoData[sug.asin?sug.asin:sug.recommendedTargetAsin];
		 			if(info){
		 				var img="";
		 				var height=1000000;
		 				info.images[0].images.forEach(image=>{
		 					if(image.variant =="MAIN"&&height>image.height){
		 						img=image.link;
		 					}
		 				})
		 				sug.name=info.summaries[0].itemName;
		 				sug.brand=info.summaries[0].brand;
		 				sug.image=img;
		 			}
		 			 
		 		});
		 	}
		 });
	}
 
	function loadSuggestTargetAsin(){
		if(state.activeProduct!="suggestAsin"){
			return;
		}
		state.suggestasinLoading=true;
		var asinlist=[];
		if(state.queryParams.productList && state.queryParams.productList.length>0){
			state.queryParams.productList.forEach(item=>{
				asinlist.push(item.asin);
			});
		}else{
			return;
		}
		var params={
			"filters": [{"filterType":"ASINS","values":asinlist}],
			"maxResults": 100,
		};
		advTargetApi.getTargetsProductsRecommendations(state.queryParams.profileid,state.queryParams.campaignType,params).then((res)=>{
			state.suggestasinLoading=false;
			var datas=JSON.parse(res.data);
			state.suggestProductList= datas.recommendedProducts;
			loadAsinInfo(state.suggestProductList);
			loadAsinSuggestBid();
		});
	}
	
	

	function loadSuggestTargetCategories(){
		if(state.activeName!="suggestCate"){
			return;
		}
		state.cateloading=true;
		var asins=[];
		state.queryParams.productList.forEach(item=>{
			asins.push(item.asin);
		});
		var source="AMAZON";
		if(state.queryParams.campaign?.adFormat=="video"){
			source="STREAMING_VIDEO";
		}
		var param={"supplySource":source, "asins":asins};
		advTargetApi.getTargetsCategoriesRecommendations(state.queryParams.profileid,state.queryParams.campaignType,param).then((res)=>{
		    state.cateloading=false;
			if(res.data){
					var data=JSON.parse(res.data);
					state.suggestCategoryList=data.categoryRecommendationResults;
					loadAllCategoryProductCount();
					loadSuggestTargetBid();
			}
			
		});
	}
	    
	function handleRefine(row) {
		state.refineForm.brand=null;
		state.refineForm.row=row;
		if(row.suggestedBid && row.suggestedBid.suggested){
			state.refineForm.bids=row.suggestedBid.suggested;
		}
		state.refineForm.AsinCountsMin=row.AsinCountsMin;
		state.refineForm.AsinCountsMax=row.AsinCountsMax;
		var param={"categoryId":row.id};
		state.refineloading=true;
		 advTargetApi.getTargetsRefineRecommendations(state.queryParams.profileid,state.queryParams.campaignType,param).then((res)=>{
			 state.refineloading=false;
			 if(res.data){
				 var data=JSON.parse(res.data);
				 state.refine.brands=data.brands;
			 }
		 });
		state.visible = true;
	}
	
	function handleTreeRefine(row){
		state.refineForm.brand=null;
		state.refineForm.name=row.data.name;
		state.refineForm.row=row.data;
		state.refineForm.row.name=	row.data.na;
		var param={"categoryId":row.data.id};
		state.refineloading=true;
		 advTargetApi.getTargetsRefineRecommendations(state.queryParams.profileid,state.queryParams.campaignType,param).then((res)=>{
			 state.refineloading=false;
			 if(res.data){
				 var data=JSON.parse(res.data);
				 state.refine.brands=data.brands;
			 }
		 });
		state.visible = true;
	}
	
	function addSearchTarget(){
		
	}

	function handleBid(row){
		if(state.bidType.type=="suggest"){
			if(row.bid){
				row.bids=row.bid;
			}
			if(row.suggestedBid && row.suggestedBid.suggested){
				row.bids=row.suggestedBid.suggested;
			}
		}else if(state.bidType.type=="my"){
			if(state.bidType.bid && state.bidType.bid!=""){
				row.bids=parseFloat(state.bidType.bid);
			}
		}else{
			if(state.queryParams.adgroup.defaultbid){
				row.bids=parseFloat(state.queryParams.adgroup.defaultbid);
			}
		}
	}
	function handleListBid(bids){
		if(state.addList && state.addList.length>0){
			state.addList.forEach(row=>{
				handleBid(row);
			});
		}
	}
	function emitChange(){
		emit('change', state.addList);
	}
	
	 
</script>

<style scoped>
	.star-box {
		width: 100%;
		margin: 0 16px;
		margin-bottom: 16px;
	}

	.class-header {
		display: flex;
		justify-content: space-between;
		color: #666;
	}

	.class-list {
		display: flex;
		justify-content: space-between;
		color: #333;
		border-bottom: 1px solid #eee;
	}

	.have-ass-text-radio .el-radio {
		height: inherit !important;
		line-height: 24px;
		white-space: inherit;
		margin-bottom: 16px;
		align-items: start;
	}

	.have-ass-text-radio p {
		font-size: 12px;
		color: #acb0b9;
	}

	.p-l {
		width: 50%;
		height: 600px;
	}

	.p-r {
		width: 50%;
		height: 600px;
		background-color: var(--el-color-info-light-7);
	}

	.m-l-8 {
		margin-left: 8px;
	}
</style>
<style>
	.have-ass-text-radio .el-radio__input {
		padding-top: 6px;
	}

	.el-tree-node__label {
		flex-grow: 1;
	}
</style>
