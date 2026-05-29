	import advProductsApi from '@/api/amazon/advertisement/report/advProductsApi.js';
	import adgroupsStatus from '@/model/amazon/advertisement/status.json';
	import {ElMessage,ElDivider} from 'element-plus';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import {handleAdvDaysData,formatter} from './common.js';
	import productAnysApi from '@/api/amazon/product/productAnysApi.js';
	import advSBAdsApi from '@/api/amazon/advertisement/report/advSBAdsApi.js';
	import {nextTick} from 'vue';
function handleExtData(state,ress){
	ress.data.forEach(itemext=>{
		state.tableData.records.forEach(items=>{
			 if(items.id==itemext.adid){
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
			if(state.queryParams.campaignType.toUpperCase()=='SD'){
				data.forEach(items=>{
					advProductsApi.getProductAdsExtList(profileid,state.queryParams.campaignType,[items]).then((res)=>{
					    handleExtData(state,res);
					});
				});
				
			}else if(state.queryParams.campaignType.toUpperCase()=='SP'){
				advProductsApi.getProductAdsExtList(profileid,state.queryParams.campaignType,data).then((res)=>{
					   handleExtData(state,res);
				});
			}else{
				var array1=[];
				var array2=[];
				state.tableData.records.forEach(item=>{
					item.servingStatus="loading";
					if(item.adGroupid==item.id){
						array1.push(item.id);
					}else{
						array2.push(item.id);
					}
				});
				var sbdata={};
				if(array1.length>0){
				   sbdata.adGroupIdFilter={include:array1};
				}
				if(array2.length>0){
				   sbdata.adIdFilter={include:array2};
				}
				advSBAdsApi.getAds(profileid,state.queryParams.campaignType,sbdata).then((res)=>{
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
		if(type=="remark"){
			data={remark:param};
		}
		submitUpdate(state,profileid,type,parmadata,data,()=>{
			state.selectionloding=false;
			state.batch.input=false;
			loadList(state);
		 })
}	
export function loadList(state){
	if(state.queryParams.campaignType.toUpperCase()=="SB"){
		advSBAdsApi.getAdsList(state.queryParams).then((res)=>{
			 handleApiData(state,res);
			if(state.queryParams.currentpage==1){
				 if(state.tableData.total>0){
					 advSBAdsApi.getSumAds(state.queryParams).then((ress)=>{
						 state.summary=ress.data;
					 });
				 }else{
					 state.summary ={};
				 }
				 
			}
		})
	}else{
		advProductsApi.getProductAdList(state.queryParams).then((res)=>{
			 handleApiData(state,res);
			if(state.queryParams.currentpage==1){
				 if(state.tableData.total>0){
					 advProductsApi.getSumProductAd(state.queryParams).then((ress)=>{
						 state.summary=ress.data;
					 });
				 }else{
					 state.summary ={};
				 }
				 
			}
		})
	}
}
 
export function update(state,arrays,callback){
	var addObj = {};
	addObj.productAds=arrays;
	var addstr=JSON.stringify(addObj);
	advProductsApi.updateProductAdList(state.queryParams.profileid,state.queryParams.campaignType,{"jsonstr":addstr}).then((res)=>{
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
		state.budgetVisible=false;
		state.visible=false;
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
	     submitUpdate(state,profileid,ftype,data,row,()=>{
			 row.statusloading=false;
		 })
		
	}

	async function getData(profileid,campaignType,data){
		var campaigns=[];
		await advProductsApi.getProductAds(profileid,campaignType,data).then((res)=>{
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
				var myparam=await getData(profileid,state.queryParams.campaignType,paramdata.ids);
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
				})
				update(state,param,()=>{
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
	  if(state.expandType=="sumproducts"){
			var data={};
			data.sku=row.sku;
			data.marketplaceid=row.marketplaceid;
			data.sellerid=row.sellerId;
			data.groupid=row.groupid;
			productAnysApi.productdetailByInfo(data).then((res)=>{
				if(res.data){
					var datas=[];
					res.data.status="none";
					res.data.statustype="product";
					datas.push(res.data);
					row.children=datas;
				}else{
					row.children=[{status:'none',name:'暂无数据！'}];
				}
			});
	} 
	if(state.expandType=="otherproducts"){
		var data={};
		data.campaignid=row.campaignId;
		data.adGroupid=row.adGroupId;
		data.profileid=row.profileid;
		data.asin=row.asin;
		data.fromDate=state.queryParams.fromDate;
		data.endDate=state.queryParams.endDate;
		advProductsApi.getProductAdotherAsin(data).then((res)=>{
			if(res.data && res.data.length>0){
				var datas=res.data;
				datas.forEach(item=>{
					item.attributedSales=item.attributedSalesOtherSKU;
					item.attributedUnitsOrdered=item.attributedUnitsOrderedOtherSKU;
					item.status="none";
				});
				row.children=datas;
			}else{
				row.children=[{status:'none',name:'暂无数据！'}];
			}
		});
	}
 	
 }
 