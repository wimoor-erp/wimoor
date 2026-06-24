	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	 import {formatter} from './common.js';
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
				var items={};
				items.campaignid=item.campaignId;
				items.matchtype=item.matchType;
				items.adGroupid=item.adGroupId;
				items.rowid=item.id;
				items.profileid=profileid;
				items.query=item.query;
				data.push(items);
			});
			//加载各个ext信息
			advKeywordsApi.getKeywordforQuery(data).then((ress)=>{
				 if(ress.data && ress.data.length>0){
					 ress.data.forEach(oitem=>{
						state.tableData.records.forEach(row=>{
							 if(row.id==oitem.rowid){
								 if(oitem.keywordFlag){
									 row.keywordFlag=oitem.keywordFlag;
								 }
								 if(oitem.keywordNegativaFlag ){
								 	 row.keywordNegativaFlag =oitem.keywordNegativaFlag;
								 }
							 }
						});
					 });
				 }
			});
		}
	}
	
 
 
export function loadList(state){
	advKeywordsApi.getKeywordQueryList(state.queryParams).then((res)=>{
		 handleApiData(state,res);
	})
}

 
 
 