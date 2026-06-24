	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import adgroupsStatus from '@/model/amazon/advertisement/status.json';
	import {ElMessage} from 'element-plus';
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
function handleApiData(state,res){
		if(res.data&&res.data.total>0){
			res.data.records.forEach(item=>{
				if(item.status.toUpperCase()=="ENABLED"){
					item.checkstatus=true;
				}else{
					item.checkstatus=false;
				}
			});
			state.tableData.records =  res.data.records;
			state.tableData.total = res.data.total;
		}else{
			state.tableData.records =[];
			state.tableData.total = 0;
		}
	
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
					advTargetApi.negativaTargetsExt(profileid,state.queryParams.campaignType,[items]).then((ress)=>{
					      handleExtData(state,ress)
					});
				})
				
			}else{
				advTargetApi.negativaTargetsExt(profileid,state.queryParams.campaignType,data).then((ress)=>{
					 handleExtData(state,ress)
				});
			}
			
		}
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
	     submitUpdate(state,profileid,ftype,data,row,()=>{
			 row.statusloading=false;
		 })
		
	}	
 
 
export function loadList(state){
	advTargetApi.getNegativaTargeList(state.queryParams).then((res)=>{
		 handleApiData(state,res);
	})
}
 
 async function getData(profileid,campaignType,data){
 		var campaigns=[];
 		await advTargetApi.negativaTargets(profileid,campaignType,data).then((res)=>{
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
 					 //loadList(state);
 					 if(callback){
 						 callback();
 					 }
 				});
 			}
 			
 		}
 export function update(state,arrays,callback){
 	var addObj = {};
 	addObj.ntargets=arrays;
 	var addstr=JSON.stringify(addObj);
 	advTargetApi.updateNegativaTargets(state.queryParams.profileid,state.queryParams.campaignType,{"jsonstr":addstr}).then((res)=>{
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
 