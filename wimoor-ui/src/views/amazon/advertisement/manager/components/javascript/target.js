import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
import adgroupsStatus from '@/model/amazon/advertisement/status.json';
import {ElMessage,ElDivider,ElMessageBox} from 'element-plus';
import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
import {handleAdvDaysData,formatter} from './common.js';
import productAnysApi from '@/api/amazon/product/productAnysApi.js';
import {nextTick} from 'vue';
function handleExtData(state,ress){
	ress.data.forEach(itemext=>{
		state.tableData.records.forEach(items=>{
			 if(items.id==itemext.targetid){
				 items.ext=itemext;
				 items.servingStatus= adgroupsStatus[itemext.servingStatus];
				if(items.servingStatus&&itemext.servingStatus&&items.status){
					if(items.status.toUpperCase()==itemext.servingStatus.toUpperCase()){
						items.servingStatus.ishow=false;
					}else{
						items.servingStatus.ishow=true;
					}
				}
			 }
		});
		
	})
}	 
function loadExt(state){ 
	      var data=[];
          var  profileid=state.tableData.records[0].profileid;
			state.tableData.records.forEach(item=>{
				item.servingStatus="loading";
				data.push(item.id);
			});
			if(state.queryParams.campaignType.toUpperCase()=='SD'){
				data.forEach(items=>{
					advTargetApi.getTargetExt(profileid,state.queryParams.campaignType,[items]).then((res)=>{
						handleExtData(state,res);
					});
				});
			}else{
				advTargetApi.getTargetExt(profileid,state.queryParams.campaignType,data).then((res)=>{
					handleExtData(state,res);
				});
			}
}
function loadRowSuggestBid(state){
	var profileid=null;
	var targets=[];
	var type=state.queryParams.campaignType;
	var profileid=state.tableData.records[0].profileid;
	

     state.tableData.records.forEach(row=>{
			row.suggestedBid="loading";
			var list=getSugguestBidExpressionParam(type,row.expression)
			targets.push(list); 
	});
	if(type.toLowerCase()=="sp"){
		if(state.queryParams.adGroupid){
			advTargetApi.getTargetBidRecommendations({"profileid":profileid,"expression":targets,
			"adtype":type,"adGroupid":state.queryParams.adGroupid,
			"campaignId":state.queryParams.campaignId}).then((res)=>{
				state.tableData.records.forEach(row=>{  row.suggestedBid={}; });
					 if(res.data && res.data.length>0){
						 res.data.forEach((item,index)=>{
							 var row=state.tableData.records[index];
							 row.suggestedBid=item.suggestedBid;
						 })
					 } 
			});
		}else{
			state.tableData.records.forEach(row=>{  row.suggestedBid={}; });
		}
	}			
				
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
		if(state.tableData.records && state.tableData.records.length>0){
			//加载各个ext信息
			loadExt(state)
			//加载各个建议竞价
			loadRowSuggestBid(state);
			
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
		submitUpdate(state,profileid,type,parmadata,data,()=>{
			state.selectionloding=false;
			row.statusloading=false;
			state.bidloading=false;
			state.batch.input=false;
			state.bidVisible=false;
			loadList(state);
		 })
}	
export function loadList(state){
	advTargetApi.getProductTargeList(state.queryParams).then((res)=>{
		 handleApiData(state,res);
		if(state.queryParams.currentpage==1){
			 if(state.tableData.total>0){
				 advTargetApi.getSumProductTarge(state.queryParams).then((ress)=>{
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
	addObj.targets=arrays;
	var addstr=JSON.stringify(addObj);
	advTargetApi.updateTargetList(state.queryParams.profileid,state.queryParams.campaignType,{"jsonstr":addstr}).then((res)=>{
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
		 // if(ftype=="status" && param=="DELETE"){
		 // 	ElMessageBox.confirm(
		 // 		'请确认是否归档？',
		 // 		{
		 // 		  confirmButtonText: '确认',
		 // 		  cancelButtonText: '取消',
		 // 		  type: 'warning',
		 // 		  callback:(action)=>{
		 // 			 if(action=="confirm"){
		 // 				  submitUpdate(state,profileid,type,campaignids,data,()=>{
		 // 				 	row.statusloading=false;
		 // 				 	row.bidloading=false;
		 // 				 	state.bidVisible=false;
		 // 				  })
		 // 			 }
		 // 		  }
		 // 		}
		 // 	  )
		 // }else{
		 	  submitUpdate(state,profileid,ftype,data,row,()=>{
				state.selectionloding=false;
				row.statusloading=false;
				state.bidloading=false;
				state.batch.input=false;
				state.bidVisible=false;
		 	 })
		 //}
		
	}

	async function getData(profileid,campaignType,data){
		var campaigns=[];
		await advTargetApi.getTarget(profileid,campaignType,data).then((res)=>{
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
 
function getSugguestBidExpressionParam(type,expression){
		 var list=[];
	     if(type.toLowerCase()=="sp"){
			 var expression=JSON.parse(expression);
			 expression.forEach(item=>{
				 var value=item.value;
				 var ftype=item.type;
				 if(ftype=="ASIN_SAME_AS"){
				 	ftype="asinSameAs";	
				 }else if(ftype=="ASIN_CATEGORY_SAME_AS"){
				 	 ftype="asinCategorySameAs";
				 }else if(ftype=="ASIN_BRAND_SAME_AS"){
				 	 ftype="asinBrandSameAs";
				 }else if(ftype=="ASIN_ACCESSORY_RELATED"){
				 	 ftype="asinAccessoryRelated";
				 }else if(ftype=="ASIN_SUBSTITUTE_RELATED"){
				 	 ftype="asinSubstituteRelated";
				 }else if(ftype=="QUERY_BROAD_REL_MATCHES"){
				 	 ftype="queryBroadMatches";
				 }else if(ftype=="QUERY_HIGH_REL_MATCHES"){
				 	 ftype="queryHighRelMatches";
				 }else if(ftype=="ASIN_EXPANDED_FROM"){
				 				 ftype="asinExpandedFrom";
				 }
				 list.push({"type":ftype,"value":value});
			 })
	    }
	   return list;
}
  //获取关键词建议竞价
 export function loadSuggestedTargets(row,type){
	 var targets=[getSugguestBidExpressionParam(type,row.expression)];
	 advTargetApi.getTargetBidRecommendations({"profileid":row.profileid,"expression":targets,
	 "adtype":type,"adGroupid":row.adGroupId,
	 "campaignId":row.campaignId}).then((res)=>{
		  row.suggestedBid={};
		 if(res.data && res.data.length>0){
			row.suggestedBid=res.data[0].suggestedBid;
		 }else{
			 row.suggestedBid={};
		 }
	 });
	 
 }

 