	import advAdgroupApi from '@/api/amazon/advertisement/report/advAdgroupApi.js';
	import adgroupsStatus from '@/model/amazon/advertisement/status.json';
	import {ElMessage,ElDivider,ElMessageBox} from 'element-plus';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import {handleAdvDaysData,formatter} from './common.js';
	import {nextTick} from 'vue';
	
function handleExtData(state,ress){
	ress.data.forEach(itemext=>{
		state.tableData.records.forEach(items=>{
			 if(items.id==itemext.adgroupid){
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
export function bidplusFormatter(row) {
		if(row.name == "汇总"){
			return "";
		}
		var value = row.bidding;
		var addhtml = "";
		if (value) {
			var data = JSON.parse(value);
			if (data["strategy"]) {
				value = data.strategy;
				if ("autoForSales" == value || value=="AUTO_FOR_SALES")
					value = "提高和降低";
				if ("legacyForSales" == value || value=='LEGACY_FOR_SALES')
					value = "只降低";
				if ("manual" == value || value=="MANUAL")
					value = "固定竞价";
				if (value=="RULE_BASED")
					 value = "固定竞价";
				var just = data.adjustments?data.adjustments:data.placementBidding;
				if (just && just.length > 0) {
					var tophtml = "";
					var prohtml = "";
					for (var i = 0; i < just.length; i++) {
						var predicate = just[i].predicate?just[i].predicate:just[i].placement;
						if (just.length == 2) {
							if (predicate == "placementTop" || predicate == "PLACEMENT_TOP") {
								tophtml += "<span class='light-font'>首页:</span>" + getValue(just[i].percentage) + "%";
							} else {
								prohtml += "<span class='light-font'>商品页面:</span>" + getValue(just[i].percentage) + "%";
							}
						}
						if (just.length == 1) {
							if (predicate == "placementTop" || predicate == "PLACEMENT_TOP") {
								tophtml += "<span class='light-font'>首页:</span>" + getValue(just[i].percentage) + "%";
								prohtml += "<span class='light-font'>商品页面:0%<span>";
							} else {
								prohtml += "<span class='light-font'>商品页面:</span>" + getValue(just[i].percentage) + "%";
								tophtml += "<span class='light-font'>首页:0%</span>";
							}
						}
					}
					addhtml = "<div class='text-sm'>" + tophtml + " | " + prohtml + "</div>";
				} else {
					addhtml = "<div  class='text-sm light-font'>首页:0%  |  商品页面:0%</div>";
				}
			}
		}
		var html = "<a  class='pointer text-blue' >" + getValue(value) + "</a>"
				+ addhtml;
		return html;
	}
function handleApiData(state,res){
		if(res&&res.data&&res.data.total>0){
			res.data.records.forEach(item=>{
				if(!item.status){
					item.status="";
				}
				formatter(item);
				if(item&&item.status&&item.status.toUpperCase()=="ENABLED"){
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
			if(state.queryParams.campaignType.toUpperCase()=='SD'){
				data.forEach(items=>{
					advAdgroupApi.getAdgroupExtList(profileid,state.queryParams.campaignType,[items]).then((res)=>{
						 handleExtData(state,res);
					});
				});
			}else{
				advAdgroupApi.getAdgroupExtList(profileid,state.queryParams.campaignType,data).then((res)=>{
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
			data={defaultbid:param};
		}
		if(type=="portfolioid"){
			data={portfolioid:param};
		}
		if(type=="remark"){
			data={remark:param};
		}
		if(type=="status" && param=="ARCHIVED"){
			ElMessageBox.confirm(
				'请确认是否归档？',
				{
				  confirmButtonText: '确认',
				  cancelButtonText: '取消',
				  type: 'warning',
				  callback:(action)=>{
					 if(action=="confirm"){
						  submitUpdate(state,profileid,type,parmadata,data,()=>{
						 	state.selectionloding=false;
						 	state.batch.input=false;
						  })
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
	advAdgroupApi.getAdGroupList(state.queryParams).then((res)=>{
		 handleApiData(state,res);
		if(state.queryParams.currentpage==1){
			 if(state.tableData.total>0){
				 advAdgroupApi.getSumAdGroup(state.queryParams).then((ress)=>{
					 state.summary=ress.data;
				 });
			 }else{
				 state.summary ={};
			 }
			 
		}
	})
}

export	function biddingEdit(biddingRef,row){
		var data=[];
		data.push(row.id);
		var form={dynamicBidding:{placementBidding:[]},campaignid:row.id,campaignType:state.queryParams.campaignType};
		biddingRef.show(form,false);
		advAdgroupApi.getAdgroups(row.profileid,row.campaignType,data).then((res)=>{
			if(res.data && res.data.length>0){
				form=res.data[0];
				form.dynamicBidding=JSON.parse(form.bidding);
				form.budget=JSON.parse(form.budget);
				var placementBidding=[
				  {"percentage":null,"placement":"PLACEMENT_TOP"},
				  {"percentage":null,"placement":"PLACEMENT_PRODUCT_PAGE"},
			     ];
				 if(form.dynamicBidding.placementBidding){
					 form.dynamicBidding.placementBidding.forEach(item=>{
						 if(item.placement=="PLACEMENT_TOP"){
							 placementBidding[0].percentage=item.percentage
						 }
						 if(item.placement=="PLACEMENT_PRODUCT_PAGE"){
						 	 placementBidding[1].percentage=item.percentage
						 }
					 })
				 }
				form.dynamicBidding.placementBidding=placementBidding;
				biddingRef.show(form,true);
			}
		});
	}
export function updateAdGroups(state,adGroupArray,callback){
	var addObj = {};
	addObj.adGroupArray=adGroupArray;
	var addstr=JSON.stringify(addObj);
	advAdgroupApi.updateAdGroups(state.queryParams.profileid,state.queryParams.campaignType,{"jsonstr":addstr}).then((res)=>{
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
		state.budgetVisible=false;
		state.visible=false;
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
		if(ftype=="name"){
		   row.nameloading=true;
		}
		if(ftype=="status"){
		   row.statusloading=true;
		   if(row.checkstatus){
				row.state="ENABLED";
		   }else{
			   row.state="PAUSED";
		   }
		}
	     submitUpdate(state,profileid,ftype,data,row,()=>{
			 state.nameVisible=false;
			 row.statusloading=false;
			 row.nameloading=false;
			 state.bidloading=false;
			 nextTick(()=>{
				 state.bidVisible=false;
			 });
			 
		 })
		
	}

	async function getAdgroupsData(profileid,campaignType,data){
		var campaigns=[];
		await advAdgroupApi.getAdgroups(profileid,campaignType,data).then((res)=>{
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
					if(ftype=="name"){
						item.name=modifydata.name;
					}
					if(ftype=="status"){
						 item.state=modifydata.state;
					}
					if(ftype=="bid"){
						 item.defaultbid=modifydata.defaultbid;
					}
				})
				if(!isok){
					ElMessage.error("请确定数据修改是否正确！");
					return;
				}
				updateAdGroups(state,param,()=>{
					if(ftype!="status"){
						loadList(state);
					}
					 if(callback){
						 callback();
					 }
				});
			}
			
		}
		
export function handleAdvPlacementData(datas){
		if(datas && datas.length>0){
			datas.forEach(item=>{
				item.ACOS=item.ACOSPlacement;
				item.CTR=item.CTRPlacement;
				item.ROAS=item.ROASPlacement;
				item.attributedConversions=item.attributedConversionsPlacement;
				item.attributedConversionsSameSKU=item.attributedConversionsSameSKUPlacement;
				item.attributedSales=item.attributedSalesPlacement;
				item.attributedSalesSameSKU=item.attributedSalesSameSKUPlacement;
				item.attributedUnitsOrdered=item.attributedUnitsOrderedPlacement;
				item.avgcost=item.avgcostPlacement;
				item.clicks=item.clicksPlacement;
				item.cost=item.costPlacement;
				item.impressions=item.impressionsPlacement;
				item.sumSales=item.sumSalesPlacement;
				item.sumUnits=item.sumUnitsPlacement;
				item.status='none';
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
  //加载广告组建议竞价
 export  function loadAgroupSuggestBid(state,type){
	 var item=state.editRow;
	 state.bidloading=true;
	 advAdgroupApi.getAdGroupsBid( item.profileid,type,item.id).then(res=>{
		 state.bidloading=false;
		 if(res.data){
			var data=JSON.parse(res.data);
			if(data && data.bidRecommendations && data.bidRecommendations.length>0){
				state.editRowBid=data.bidRecommendations[0];
			}
		 }
	 })
		// advAdgroupApi.getAdGroupSuggestBid({"profileid":item.profileid,"adgroupid":item.id,"campaignid":item.campaignId}).then((ress)=>{
		// 	console.log(ress.data);
		// 	if(ress.data && ress.data.bidRecommendations){
		// 		var bidArray=ress.data.bidRecommendations[0];
		// 		var expression=bidArray.bidRecommendationsForTargetingExpressions;
		// 	}
		// 	if (ress.data["suggestedBid"] && item) {
		// 		item.suggestedbid = ress.data.suggestedBid.suggested;
		// 		item.rangeEnd = ress.data.suggestedBid.rangeEnd;
		// 		item.rangeStart = ress.data.suggestedBid.rangeStart;
		// 	}
		// }); 
 }
 
 