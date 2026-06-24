<template>
	<el-tabs type="border-card" class="p-l" v-model="activeKeywordTabName" @tab-change="handleKeywordTabChange" >
		<el-tab-pane  name="suggestkeywordtab" label="建议关键词"  >
			<BiddingType  @change="handleListBid" :bidType="bidType"  ></BiddingType>
			<el-table  v-loading="keywordLoading"  :data="tableData"   height="400px">
				<el-table-column label="关键词" prop="value" >
					
					<template #default="scope">
						  <div>{{scope.row.value}}</div>
						 <!-- <div class="font-extraSmall">{{scope.row.translation}}</div> -->
						</template>
				</el-table-column>
				<el-table-column label="匹配" width="60" prop="matchType" >
					<template #default="scope">
							 <div v-if="scope.row.matchType=='BROAD'">广泛</div>
							 <div v-if="scope.row.matchType=='PHRASE'">词组</div>
							 <div v-if="scope.row.matchType=='EXACT'">精准</div>
						</template>
					</el-table-column>
				<el-table-column label="建议竞价" width="120">
					<template #default="scope">
						<div v-if="scope.row?.suggestedBid"  >
						    <div>{{scope.row?.suggestedBid?.recommended}}<span class="font-extraSmall">( {{scope.row?.suggestedBid?.rangeStart}}-{{scope.row?.suggestedBid?.rangeEnd}})</span></div>
						</div>
						<div v-else>--</div>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="80">
					<template #header>
						<el-button type="primary" class="font-small" @click="addKeywordsAll" link>全部添加</el-button>
					</template>
					<template #default="scope">  
						    <div>	<el-button link type="primary"
							   @click="addKeywords(scope.row)">添加</el-button>
							</div>
					</template>
				</el-table-column>
			</el-table>
		</el-tab-pane>
		<el-tab-pane  name="mykeywordtab" label="填写关键词"  >
			<el-space style="margin-bottom:10px;">
				<span>匹配类型</span>
				 <el-checkbox-group  v-model="matchType" size="small">
					<el-checkbox label="EXACT">精准匹配</el-checkbox>
					<el-checkbox label="BROAD">广泛匹配</el-checkbox>
					<el-checkbox label="PHRASE">词组匹配</el-checkbox>
				</el-checkbox-group>
			</el-space>
			<BiddingType  @change="handleListBid" :bidType="bidType" ></BiddingType>
			<el-input v-model="mykeywords" type="textarea" :autosize="{ minRows:8, maxRows:8}"
				placeholder="输入关键词,用逗号隔开" />
			<el-space class="m-t-8">
				<el-button @click="addMykeywords">添加商品</el-button>
				<span class="font-extraSmall ">最多只能添加1000个关键词!</span>
			</el-space>
		</el-tab-pane>
	</el-tabs>
	<div class="p-r">
						<span class="font-extraSmall m-l-8">已添加 <span> {{addList.length}} </span> 个关键词</span>
						<el-table :data="addList" height="466px" v-loading="rightloading">
							<el-table-column label="关键词" prop="keywords">
								<template #default="scope">
									  <div>{{scope.row.keywords}}</div>
									</template>
							</el-table-column>
							<el-table-column label="匹配类型" width="90" prop="matchType" >
								<template #default="scope">
									 <div v-if="scope.row.matchType=='BROAD'">广泛</div>
									 <div v-if="scope.row.matchType=='PHRASE'">词组</div>
									 <div v-if="scope.row.matchType=='EXACT'">精准</div>
								</template>
							</el-table-column>
							<el-table-column label="建议竞价" width="110">
								<template #default="scope">
									<el-space v-if="scope.row.suggestedBid">${{scope.row.suggestedBid.recommended}}
										<arrow-circle-right title="应用" @click="scope.row.bids=scope.row.suggestedBid.recommended"
											class="pointer " theme="filled" size="16" fill="#67c23a" />
									</el-space>
									<p v-if="scope.row.suggestedBid" class="font-extraSmall">{{scope.row.suggestedBid?.rangeStart}}-{{scope.row.suggestedBid?.rangeEnd}}</p>
								</template>
							</el-table-column>
							<el-table-column label="竞价" width="110">
								<template #default="scope">
									<el-input @input="emitChange" v-model="scope.row.bids" clearable size="small">
										<template #prefix>$</template>
									</el-input>
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
</template>

<script setup>
	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import BiddingType from './bidding_type.vue';
	import {
		ref,
		reactive,
		onMounted,
		toRefs,
		watch
	} from 'vue'
	import {
		Search,
	} from '@element-plus/icons-vue'
	import {
		ArrowCircleRight,
	} from '@icon-park/vue-next';
	//展示用
	const star = ref([2, 4])
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
		matchType:["BROAD"] ,
		targetType: "keyword",
		queryParams: {},
		suggestProduct: [{
			asin: '485WESS6',
		}],
		classtree: [],
		tableData: [ ],
		activeKeywordTabName:"suggestkeywordtab",
		suggestCategoryList:[],
		bidType:{type:"suggest",bid:""} ,
		activeName: '1',
		defaultProps:{},
		activeProduct: "1",
		addList: [],
		visible: false,
		keywordLoading:false,
		rightloading:false,
		cateloading:false,
		mykeywords:'',
		targetingType:"MANUAL",
	})
	const {
		matchType,
		targetType,
		suggestProduct,
		defaultProps,
		classtree,
		queryParams,
		tableData,
		addList,
		activeName,
		activeKeywordTabName,
		suggestCategoryList,
		activeProduct,
		visible,
		keywordLoading,
		mykeywords,
		rightloading,
		cateloading,
		targetingType,
		bidType,
	} = toRefs(state)
     state.defaultProps.label="na";
	 state.defaultProps.children="ch";
     function handleKeywordTabChange(){
		 if(state.activeKeywordTabName!="mykeywordtab"&&state.tableData.length==0){
			 loadSuggestKeyword(null,(data)=>{
			 	if(data){
			 		state.tableData=data;
			 	}
			 	state.keywordLoading=false;
			 });
		 }
	 }
    function loadData(){
			state.keywordLoading=true;
			if(state.queryParams.querylist&&state.queryParams.querylist.length>0){
				state.activeKeywordTabName="mykeywordtab";
				var targetlist=[];
				state.tableData=[];
				if(state.queryParams.querylist instanceof Array){
					state.queryParams.querylist.forEach(item=>{
						var itemjson=JSON.parse(item);
						itemjson.userSelectedKeyword=true;
						itemjson.keyword=itemjson.keywords;
						targetlist.push(itemjson);
					})
				}else{
					var itemjson=JSON.parse(state.queryParams.querylist);
					itemjson.userSelectedKeyword=true;
					itemjson.keyword=itemjson.keywords;
					targetlist.push(itemjson);
				}
				handleAddMyKeywords(targetlist);
			}else{
				loadSuggestKeyword(null,(data)=>{
					if(data){
						state.tableData=data;
					}
					state.queryParams.querylist=[];
					state.keywordLoading=false;
				});
			}
	}
	function show(params) {
		state.queryParams = params;
		loadData();
	}
	defineExpose({
		show,
	})
	function deleteRow(row,index){
	     state.addList.splice(index,1);
		 emitChange();
	}
	function deleteRowAll(){
		state.addList=[];
		emitChange();
	}
	
	function loadKeywordBid(arrays,ftype){
		// ,"campaignId":state.queryParams.campaign.campaignid,"goal":state.queryParams.campaign.goal,"costType":state.queryParams.campaign.costType
		advKeywordsApi.getAmzKeywordBid(state.queryParams.profileid,state.queryParams.campaignType,{"keywords":arrays}).then((res)=>{
			var data=JSON.parse(res.data);
			var result=data.keywordsBidsRecommendationSuccessResults;
			if(result && result.length>0){
				if(ftype=="myKeyword"){
					result.forEach(item=>{
						  var rows={"suggestedBid":{"rangeStart":null,"rangeEnd":null}};
						  rows.keywords=item.keyword.keywordText;
						  rows.keyword=rows.keywords;
						  rows.matchType=item.keyword.matchType.toUpperCase();
						  rows.suggestedBid=item.recommendedBid;
						 handleBid(rows);
						 state.addList.push(rows);
					});
					state.rightloading=false;
				}else{
					state.tableData.forEach(function(row,index){
						result.forEach(item=>{
							 if(index==item.keywordIndex){
								 row.suggestedBid=item.recommendedBid;
							 }
						});
					});
				}
				
			}
		});
		 
	}
	
	
	function loadSuggestKeyword(target,callback){
		if(state.queryParams.adgroupid){
			var ads=state.queryParams.ads;
			var creative=JSON.parse(ads.creative);
			var asins=[];
			var param= {"goal":state.queryParams.campaign.goal, 
						"creativeType":creative.type,
						"maxNumSuggestions":"100",
						"locale": "zh_CN",
				         };
			 if(target){
				 var arrays=[];
				 target.forEach(item=>{
					 arrays.push({"matchType":item.matchType.toLowerCase(),"keywordText":item.keywords});
				 });
				 loadKeywordBid(arrays,"myKeyword");
			 }else{
				 if(state.queryParams.productList && state.queryParams.productList.length>0){
				 	state.queryParams.productList.forEach(item=>{
				 		asins.push(item.asin);
				 	});
				 	param.asins=asins;
				 }else{
				 	var landingPage=JSON.parse(ads.landingPage);
				 	param.url=landingPage.url;
				 }
				 advKeywordsApi.getSuggestedkeywords(state.queryParams.profileid,state.queryParams.campaignType,param).then((res)=>{
				 	var data={};
				 	if(res&&res.data){
				 		
				 	    data=JSON.parse(res.data);	
				 		if(data&&data.length>0){
				 			var arrays=[];
				 			data.forEach(item=>{
				 				arrays.push({"matchType":item.matchType.toLowerCase(),"keywordText":item.value});
				 			});
				 			loadKeywordBid(arrays);
				 		}
				 	}
				 	if(callback){
				 		callback(data);
				 	}
				 }).catch(()=>{
						if(callback){
							callback();
						}
				  });
			 }
			
		}
		else  if(state.queryParams.productList && state.queryParams.productList.length>0){
					var asins=[];
					state.queryParams.productList.forEach(item=>{
						asins.push(item.asin);
					});
					var param={"recommendationType":"KEYWORDS_FOR_ASINS", "asins":asins, "locale":"zh_CN"};
					 if(target){
						 param.targets=target;
						 param.maxRecommendations= 0;
					 }
					advKeywordsApi.getSuggestedkeywords(state.queryParams.profileid,state.queryParams.campaignType,param).then((res)=>{
						var data={};
						if(res&&res.data){
						    data=JSON.parse(res.data);	
							if(data&&data.keywordTargetList){
								data.keywordTargetList.forEach(item=>{
									if(item.bidInfo && item.bidInfo.length>0){
										item.bidInfo.forEach(info=>{
											if(info.bid){
												info.bid=parseFloat(info.bid)/100.0;
											}
											if(info.suggestedBid && info.suggestedBid.rangeStart){
												info.suggestedBid.rangeStart=parseFloat(info.suggestedBid.rangeStart)/100.0;
											}
											if(info.suggestedBid && info.suggestedBid.rangeEnd){
												info.suggestedBid.rangeEnd=parseFloat(info.suggestedBid.rangeEnd)/100.0;
											}
										});
									} 
								})
							}
						}
						if(callback){
							callback(data);
						}
					}).catch(error=>{
							if(callback){
								callback();
							}
					});
		} else{
			if(callback){
				callback();
			}
		} 
	}
	
	function pushKeywordAll(){
		state.tableData.forEach(item=>{
			if(item.suggestedBid){
					var rows={"suggestedBid":{"rangeStart":null,"rangeEnd":null}};
					rows.keywords=item.keyword;
					rows.matchType=item.matchType;
					rows.bid=item.suggestedBid.recommended;
					rows.bids=item.suggestedBid.recommended;
					if(item.suggestedBid && item.suggestedBid.rangeStart){
						rows.suggestedBid.rangeStart=item.suggestedBid.rangeStart;
					}
					if(item.suggestedBid && item.suggestedBid.rangeEnd){
						rows.suggestedBid.rangeEnd=item.suggestedBid.rangeEnd;
					}
					handleBid(rows);
					state.addList.push(rows);
			}else{
				state.addList.push({"keywords":item.keyword,"matchType":item.matchType});
			}
		});
	}
	
	function addKeywordsAll(){
		if(state.queryParams.querylist){
			pushKeywordAll();
		}else{
			state.addList=[];
			pushKeywordAll();
		}
		
	}
	function handleBid(row){
		if(state.bidType.type=="suggest"){
			if(row.bid){
				row.bids=row.bid;
			}
			if(row.suggestedBid && row.suggestedBid.recommended){
				row.bids=row.suggestedBid.recommended;
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
	
 
	function addKeywords(row) {
		if(state.addList && state.addList.length>0){
			var isok=true;
			state.addList.forEach(item=>{
				if(item.keywords==row.value && row.matchType==item.matchType){
					isok=false;
				}
			});
			if(isok){
				row.keywords=row.value;
				var myrow=JSON.parse(JSON.stringify(row));
				handleBid(myrow);
				state.addList.push(myrow);
				emitChange();
			}
		}else{
			row.keywords=row.value;
			var myrow=JSON.parse(JSON.stringify(row));
			handleBid(myrow);
			state.addList.push(myrow);
			emitChange();
		}
	}

	function handleRefine() {
		state.visible = true
	}

	function radioChange(val) {
		state.targetType=val;
		loadData();
		emit('changetype', val);
	}
	function emitChange(){
		emit('change', state.addList);
	}
	function handleAddMyKeywords(mylist){
		state.rightloading=true;
		loadSuggestKeyword(mylist,(data)=>{
			state.rightloading=false;
			if(data){
				data.keywordTargetList.forEach(item=>{
					if(item.bidInfo && item.bidInfo.length>0){
						item.bidInfo.forEach(info=>{
							var rows={"suggestedBid":{"rangeStart":null,"rangeEnd":null}};
							rows.keywords=item.keyword;
							rows.translation=item.translation;
							rows.matchType=info.matchType;
							if(info.bid){
								rows.bid=info.bid;
								rows.bids=info.bid;
							}
							if(info.suggestedBid && info.suggestedBid.rangeStart){
								rows.suggestedBid.rangeStart=info.suggestedBid.rangeStart;
							}
							if(info.suggestedBid && info.suggestedBid.rangeEnd){
								rows.suggestedBid.rangeEnd=info.suggestedBid.rangeEnd;
							}
							var isok=true;
							state.addList.forEach(mitem=>{
								if(mitem.keywords==rows.keywords && rows.matchType==mitem.matchType){
									isok=false;
								}
							});
							if(isok){
								handleBid(rows);
						     	state.addList.push(rows);
							}
						});
					}else{
						var matchType="";
						mylist.forEach(myitem=>{
							if(myitem.keyword==item.keyword){
								matchType=myitem.matchType;
							}
						});
						item.keywords=item.keyword;
						item.bids=item.bid;
						item.matchType=matchType;
						var isok=true;
						state.addList.forEach(mitem=>{
							if(mitem.keywords==item.keywords && item.matchType==mitem.matchType){
								isok=false;
							}
						});
						if(isok){
								handleBid(item);
						 		state.addList.push(item);
						}
					
					}
				})
				emitChange();
			}
			
		})
	}
	function addMykeywords(){
		var matchType=state.matchType;
		var mylist=[];
		var zhengze = /([\n\r])+/g;
		state.mykeywords = state.mykeywords.replace(zhengze, '\n');
		var liststr = state.mykeywords.split('\n');
		liststr.forEach(item=>{
			matchType.forEach(typeitem=>{
				if(item && item.trim().length>=1){
					mylist.push({'keywordText':item,'matchType':typeitem.toLowerCase()});
				}
			})
		});
		addMyAddList(mylist);
		//handleAddMyKeywords(mylist);
	}
	function addMyAddList(mylist){
		advKeywordsApi.getAmzKeywordBid(state.queryParams.profileid,state.queryParams.campaignType,{"keywords":mylist}).then((res)=>{
			var data=JSON.parse(res.data);
			var result=data.keywordsBidsRecommendationSuccessResults;
			if(result && result.length>0){
				result.forEach(item=>{
					var row={"keywords":item.keyword.keywordText,"matchType":item.keyword.matchType.toUpperCase(),"suggestedBid":item.recommendedBid};
					handleBid(row);
					state.addList.push(row);
				});
			}
		});
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
		height: 500px;
	}

	.p-r {
		width: 50%;
		height: 500px;
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
