	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import adgroupsStatus from '@/model/amazon/advertisement/status.json';
	import {ElMessage,ElDivider,ElMessageBox} from 'element-plus';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import {handleAdvDaysData,formatter} from './common.js';
	import productAnysApi from '@/api/amazon/product/productAnysApi.js';
	import {nextTick} from 'vue';
function handleExtData(state,ress){
	ress.data.forEach(itemext=>{
		state.tableData.records.forEach(items=>{
			 if(items.id==itemext.keywordid){
				 items.ext=itemext;
				 if(itemext.servingStatus){
					 items.servingStatus= adgroupsStatus[itemext.servingStatus];
					 if(items.servingStatus&&itemext.servingStatus&&items.status){
					 	if(items.status.toUpperCase()==itemext.servingStatus.toUpperCase()){
					 		items.servingStatus.ishow=false;
					 	}else{
					 		items.servingStatus.ishow=true;
					 	}
					 }
				 }else{
					 items.servingStatus={};
				 }
			 }
		});
		
	})
}		 
function handleApiData(state,res){
		if(res.data.total>0){
			res.data.records.forEach(item=>{
				formatter(item);
				if(item.status.toUpperCase()=="ENABLED"){
					item.checkstatus=true;
				}else{
					item.checkstatus=false;
				}
			});
		}
		state.tableData.records =  res.data.records;
		state.tableData.total = res.data.total;
		var data=[];
		if(state.tableData.records && state.tableData.records.length>0){
			var profileid=null;
			profileid=state.tableData.records[0].profileid;
			state.tableData.records.forEach(item=>{
				item.servingStatus="loading";
				data.push(item.id);
			});
			//加载各个ext信息
			if(state.queryParams.campaignType.toUpperCase()=="SB"){
				data.forEach(item=>{
					advKeywordsApi.getkeywordsExt(profileid,state.queryParams.campaignType,[item]).then((res)=>{
						handleExtData(state,res);
					});
				});
			}else{
				advKeywordsApi.getkeywordsExt(profileid,state.queryParams.campaignType,data).then((res)=>{
					handleExtData(state,res);
				});
			}
			
		}
	}
	
 
export async function handleUpdate(tableRef,state,type,param){
		var rows=tableRef.getSelectionRows();
		var parmadata={ids:[],exts:[]};
		var profileid="";
		if(rows && rows.length>0){
			state.selectionloding=true;
			rows.forEach(item=>{
				if(item.ext){
					parmadata.exts.push(item.ext);
				}else{
					parmadata.ids.push(item.id);
				}
				profileid=item.profileid;
			})
		}else{
			ElMessage.success("请选择至少1行数据");
			return;
		}
		var data=null;
		if(type=="status"){
			data={state:param};
		}
		if(type=="bid"){
			data={bid:param};
		}
		if(type=="status" && param=="DELETE"){
			ElMessageBox.confirm(
				'请确认是否删除？',
				{
				  confirmButtonText: '确认',
				  cancelButtonText: '取消',
				  type: 'warning',
				  callback:(action)=>{
					 if(action=="confirm"){
						  // submitUpdate(state,profileid,type,campaignids,data,()=>{
						 	// state.selectionloding=false;
						 	// state.batch.input=false;
						  // })
						  //删除代码
					 }
				  }
				}
			  )
		}else{
			submitUpdate(state,profileid,type,parmadata,data,()=>{
				state.selectionloding=false;
				state.batch.input=false;
				loadList(state);
			 })
		}
}	
export function loadList(state){
	advKeywordsApi.getKeywordList(state.queryParams).then((res)=>{
		 handleApiData(state,res);
		if(state.queryParams.currentpage==1){
			 if(state.tableData.total>0){
				 advKeywordsApi.getSumAdvKeywords(state.queryParams).then((ress)=>{
					 state.summary=ress.data;
				 });
			 }else{
				 state.summary ={};
			 }
			 
		}
	})
}
 
export function update(state,arrays,callback){
	var addObj = {};
	addObj.keywords=arrays;
	var addstr=JSON.stringify(addObj);
	advKeywordsApi.updateKeywordList(state.queryParams.profileid,state.queryParams.campaignType,{"jsonstr":addstr}).then((res)=>{
		ElMessage.success("操作成功");
		if(callback){
			callback(res);
		}
	}).catch(e=>{
		if(callback){
			callback(e);
		}
	});
}
//更新动作
export function handleSubmitUpdate(state,ftype,row){
		//单独行更新 先去拿最新的数据
		if(row==null){
			return;
		}
		state.bidloading=true;
		var data={ids:[],exts:[]};
		var profileid=null;
		var campaignType=null;
		if(row){
			profileid=row.profileid;
			campaignType=state.queryParams.campaignType;
			if(row.ext){
				data.exts.push(row.ext);
			}else{
				data.ids.push(row.id);
			}
		}else{
			profileid=state.editRow.profileid;
			campaignType=state.queryParams.campaignType;
			if(state.editRow.ext){
				data.exts.push(state.editRow.ext);
			}else{
				data.ids.push(state.editRow.id);
			}
		}
		if(ftype=="status"){
		   row.statusloading=true;
		   if(row.checkstatus){
				row.state="ENABLED";
		   }else{
			   row.state="PAUSED";
		   }
		}
		if(ftype=="bid"){
		    row.bidloading=true;
		}
	     submitUpdate(state,profileid,ftype,data,row,()=>{
			 row.statusloading=false;
			 row.bidloading=false;
			 state.bidVisible=false;
		 })
		
	}

	async function getData(profileid,campaignType,data){
		var campaigns=[];
		await advKeywordsApi.getKeywords(profileid,campaignType,data).then((res)=>{
			campaigns=res.data;
		 });
		 return campaigns;
	}
	//更新动作
	export async function submitUpdate(state,profileid,ftype,paramdata,modifydata,callback){
			//单独行更新 先去拿最新的数据
			var param=[];
			if(paramdata.exts&&paramdata.exts.length>0){
				var myparam=paramdata.exts;
				if(myparam&&myparam.length>0){
				   param=param.concat(myparam);
				}
			}
			if(paramdata.ids&&paramdata.ids.length>0){
				var myparam=await getAdgroupsData(profileid,state.queryParams.campaignType,paramdata);
				if(myparam&&myparam.length>0){
				    param=param.concat(myparam);
				}
			}
			if(param && param.length>0){
				var isok=true;
				param.forEach(item=>{
					if(ftype=="status"){
						 item.state=modifydata.state;
					}
					if(ftype=="bid"){
						 item.bid=modifydata.bid;
					}
				})
				update(state,param,()=>{
					if(ftype!="status"){
						loadList(state);
					}
					 if(callback){
						 callback();
					 }
				});
			}
			
		}
		
 
export function handleExpandChange(state,row,treeNode,resolve){
 	if(state.expandType==""){
 		ElMessage.error("请在右上角先选择数据展开类型！");
 		return;
 	}
 	row.children=[{status:'none',name:'暂无数据！'}];
 	if(state.expandType=="days"){
 		//处理广告周期数据
 		var advData=[];
 		advData=handleAdvDaysData(row,advData);
 		row.children=advData;
 	}
 	
 }
 
 //获取关键词建议竞价
 export function loadSuggestedkeywords(row){
	 row.suggestText="建议竞价加载中......";
	 var profileid=row.profileid;
	 var campaignType=row.campaignType;
	 var param={};
	 if(campaignType=="sp"){
		 var target=[];
		 target.push({"keyword":row.keywordText,"matchType":row.matchType.toUpperCase(),"userselectedKeyword":true});
		  param= {"recommendationType":"KEYWORDS_FOR_ADGROUP",
		              "campaignId":row.campaignId, 
		 			 "adGroupId":row.adGroupId,
		 	         "locale":"zh_CN"};
		 param.targets=target;
		 param.maxRecommendations= 0;
		 advKeywordsApi.getSuggestedkeywords(profileid,campaignType,param).then((res)=>{
		 	 var data={};
		 	 if(res&&res.data){
		 	     data=JSON.parse(res.data);	
		 			 row.suggestedBid={"bid":null,"rangeStart":null,"rangeEnd":null};
		 	 	if(data&&data.keywordTargetList){
		 	 		data.keywordTargetList.forEach(item=>{
		 	 			if(item.bidInfo && item.bidInfo.length>0){
		 	 				item.bidInfo.forEach(info=>{
		 	 					if(info.bid){
		 	 						row.suggestedBid.bid=parseFloat(info.bid)/100.0;
		 	 					}
		 	 					if(info.suggestedBid && info.suggestedBid.rangeStart){
		 	 						row.suggestedBid.rangeStart=parseFloat(info.suggestedBid.rangeStart)/100.0;
		 	 					}
		 	 					if(info.suggestedBid && info.suggestedBid.rangeEnd){
		 	 						row.suggestedBid.rangeEnd=parseFloat(info.suggestedBid.rangeEnd)/100.0;
		 	 					}
		 	 				});
		 	 			} 
		 	 		})
		 	 	}
		 	 }
		 	 
		 });
	 }else if(campaignType=="hsa"){
		 var list=[];
		 list.push({'keywordText':row.keywordText,'matchType':row.matchType.toLowerCase()});
		advKeywordsApi.getAmzKeywordBid(profileid,campaignType,{"keywords":list}).then((res)=>{
			var data=JSON.parse(res.data);
			 row.suggestedBid={"bid":null,"rangeStart":null,"rangeEnd":null};
			var result=data.keywordsBidsRecommendationSuccessResults;
			if(result&& result.length>0){
				row.suggestedBid.bid=result[0].recommendedBid.recommended;
				row.suggestedBid.rangeStart=result[0].recommendedBid.rangeStart;
				row.suggestedBid.rangeEnd=result[0].recommendedBid.rangeEnd;
			}
			 
		});
	 }
	 
 }

 