import materialApi from '@/api/erp/material/materialApi.js';
import shipmentplanApi from '@/api/erp/ship/shipmentplanApi.js';
import planApi from '@/api/erp/ship/planApi.js';
import PrepInstruction from '@/model/amazon/ship/PrepInstruction.json';
import {ElMessage } from 'element-plus';
import calculateApi from "@/api/amazon/profit/calculateApi";
import shipmentplanV2Api from '@/api/erp/shipv2/shipmentplanApi.js';
export function loadInventory(state,productlist,warehouseid){
	 productlist.forEach(item=>{
		 var msku="";
		 if(item.msku){
			 msku=item.msku;
		 }else if(item.sku){
			 msku=item.sku;
		 }
	    materialApi.getMaterialInventoryInfo({"sku":msku,"warehouseid":warehouseid}).then(res=>{
	     											if(res.data){
														var data=res.data;
														productlist.forEach(skuitem=>{
															if(skuitem.msku==data.material.sku){
																if(data.canAssembly){
																	skuitem.canAssembly=data.canAssembly;
																}
																skuitem.QuantityInCase=data.boxnum;
																if(data.fulfillable){
																	skuitem.fulfillable=data.fulfillable.quantity;
																}else{
																	skuitem.fulfillable=0;
																}
																
																if(data.pkgDim){
																	skuitem.length=data.pkgDim.length;
																	skuitem.width=data.pkgDim.width;
																	skuitem.height=data.pkgDim.height;
																	skuitem.weight=data.pkgDim.weight;
																}
															}
														})
	     												
	     											}
	    })
	});

}
export function validSkuList(state,skulist){
 				 if(skulist && skulist.indexOf(",")>=0){
					  state.productlist.forEach(function(item){
						  if(skulist.includes(item.sku)){
							  item.guidance="";
						  }
					  });
 					 shipmentplanApi.guidance({"groupid":state.formData.groupid,
					                           "marketplaceid":state.formData.marketplaceid,
											   "skulist":skulist}).then((res)=>{
 								 var data=res.data.guidance;
								 var prepInstructions=res.data.prepInstructions;
 								 state.productlist.forEach(function(item){
									 if(prepInstructions[item.sku]){
													 var prepInstructionList=prepInstructions[item.sku].prepInstructionList;
													 item.prep=[];
													 prepInstructionList.forEach(prepitem=>{
														 if(PrepInstruction[prepitem]){
															 item.prep.push(PrepInstruction[prepitem]) ;
														 }
													 })
									 }
									 if(data&&data.skUInboundGuidanceList){
										 data.skUInboundGuidanceList.forEach(function(items){
											 if(items.sellerSKU==item.sku){
												item.asin=items.asin;	
												 if(items.inboundGuidance=="INBOUNDOK"){
													 var msg="";
													 if(items.guidanceReasonList){
														 items.guidanceReasonList.forEach(reason=>{
															 if(reason =="NOAPPLICABLEGUIDANCE"){
																 msg=msg+"【无适用指南】";
															 }else{
																 msg=msg+"【库存足，销售慢】";
															 }
														 })
													 }
													 item.msg=msg;
													 item.guidance="success";									  
												 }else{
													 item.guidance="error";
												 }
												
											 }
										 })
									 }
 									 if(data&&data.invalidSKUList&&data.invalidSKUList&&data.invalidSKUList.length>0){
										 var msg="";
 											 data.invalidSKUList.forEach(function(items){
 												 if(items.sellerSKU==item.sku){
 													  item.guidance="error";
													  if(item.errorReason){
														msg= "【" +item.errorReason+"】";
													  }
 												 }
 											 });
											 if(item.msg){
												item.msg=item.msg+msg; 
											 }else{
												 item.msg=msg; 
											 }
											
 									 }else if(data == "" || data == null || data == undefined){
 										if(skulist.indexOf(item.sku+",")){
 											item.guidance="success";
 										}else{
											item.guidance="success";
										}					 
 									 }else{
 										if(skulist.indexOf(item.sku+",")>=0){
 											item.guidance="success";
 										}							 
 									 }
 								 }); 
 					 						 
 					 })
 				 }
 			 }
export 	function submitProductList(state){
 				 let FormDatas = new FormData();
 				 FormDatas.append('file',state.logofile);
 				 FormDatas.append('warehouseid',state.formData.warehouseid);
 				 FormDatas.append('groupid',state.formData.groupid);
 				 FormDatas.append('marketplaceid',state.formData.marketplaceid);
 				 shipmentplanApi.uploadExcel(FormDatas).then((res)=>{
 					 if(res.data && res.data.result && res.data.result.length>0){
 						 state.downloadVisible = false;
 						 state.productlist=[];
 						 ElMessage.success('上传成功！');
 						  res.data.result.forEach(function(item){
 							 if(item.msku==undefined || item.msku==null || item.msku==''){
 							 	 item.msku=item.sku;
 							 }
 						 });
 						 state.productlist=res.data.result;
 						 state.totalproducts=state.productlist.length;
 						 var skulist="";
						 loadInventory(state,state.productlist,state.formData.warehouseid);
 						 if(state.productlist.length>0){
 							 state.productlist.forEach(function(items){
 								 skulist+=(items.sku+",");
 							 }); 
 						 }
 						 if(skulist!=""){
 								validSkuList(state,skulist);
 						 }
 					 }else{
 						 ElMessage.error( '上传失败！');
 					 }
 				 })
 			 }
			 
export function handleLoadPlanData(state,callback){
	             if(state.queryData.batchnumber){
					 state.productlist=[];
					 state.totalproducts=0;
					 var data={};
					 state.formData.warehouseid=state.queryData.warehouseid;
					 if(state.queryData.marketplaceid=="EU"){
						  data.marketplaceid="EU";
						  state.formData.marketplaceid="A1PA6795UKMFR9";
					 }else{
						  state.formData.marketplaceid=state.queryData.marketplaceid;
						  data.marketplaceid=state.queryData.marketplaceid;
					 }
					 data.batchnumber=state.queryData.batchnumber;
					 data.groupid=state.queryData.groupid;
					 data.warehouseid=state.queryData.warehouseid;
					 state.formData.groupid=state.queryData.groupid;
					 callback(state.queryData);
					 state.formData.warehouseid=state.queryData.warehouseid;
					 planApi.batchList(data).then((res)=>{
					  							 if(res.data && res.data.length>0){
					  								 res.data[0].list.forEach(function(item){
					  									 item.quantity=item.amount;
					  									 item.id=item.id;
					  									 if(item.msku==undefined || item.msku==null || item.msku==''){
					  									 	 item.msku=item.msku;
					  									 }
					  									item.sku=item.sku;
					  								 });
					  								 state.productlist=res.data[0].list;
					  								 state.totalproducts= state.productlist.length;
					  							     loadInventory(state,state.productlist,state.formData.warehouseid);
					  								 var skulist="";
					  								 if(state.productlist.length>0){
					  										state.productlist.forEach(function(items){
					  											 skulist+=(items.sku+",");
					  										 }); 
					  								 }
					  			   if(skulist!=""){
					  						 validSkuList(state,skulist);
					  			    }
					       }
					 })
				 }
 				 else if(state.queryData.marketplaceid){
 					 //发货规划点过来的
 					 state.productlist=[];
 					 state.totalproducts=0;
 					 var data={};
 					 state.formData.warehouseid=state.queryData.warehouseid;
 					 if(state.queryData.marketplaceid=="EU"){
					    state.formData.marketplaceid="A1PA6795UKMFR9";
					 }else{
					    state.formData.marketplaceid=state.queryData.marketplaceid;
 					 }
 					 data.planid=state.queryData.planid;
 					 data.issplit=state.queryData.issplit;
 					 if(!data.issplit&&state.queryData.marketplaceid=="A1PA6795UKMFR9"){
 					 	  data.marketplaceid="EU";
 					 }else{
 						  data.marketplaceid=state.queryData.marketplaceid;
 					 }
					 if(!data.issplit){
						 data.issplit="false";
					 }
 					 data.groupid=state.queryData.groupid;
 					 state.formData.groupid=state.queryData.groupid;
					 state.formData.warehouseid=state.queryData.warehouseid;
					 callback(state.queryData);
 					 data.warehouseid=state.queryData.warehouseid;
 					 shipmentplanApi.findPlanSubDetail(data).then((res)=>{
 							 if(res.data && res.data.length>0){
 								 res.data.forEach(function(item){
 									 item.quantity=item.amount;
 									 item.id=item.materialid;
 									 if(item.msku==undefined || item.msku==null || item.msku==''){
 									 	 item.msku=item.sku;
 									 }
 									item.sku=item.psku;
 								 });
 								 state.productlist=res.data;
 								 state.totalproducts=state.productlist.length;
								 loadInventory(state,state.productlist,state.formData.warehouseid);
 								 var skulist="";
 								 if(state.productlist.length>0){
 										state.productlist.forEach(function(items){
 											 skulist+=(items.sku+",");
 										 }); 
 								 }
 								 if(skulist!=""){
 										validSkuList(state,skulist);
 								 }
 							 }
 					 })
 				 }
 				 else if(state.queryData.shipmentid){
 					 //货件审核的时候 复制按钮 点进来的
 					 state.productlist=[];
 					 state.totalproducts=0;
 					 shipmentplanApi.getItemlistByShipmentId({"shipmentid":state.queryData.shipmentid}).then((res)=>{
 						 if(res.data){
 							 var data=res.data;
 							 state.formData.groupid=data.groupid;
 							 state.formData.marketplaceid=data.marketplaceid;
 							 state.formData.warehouseid=data.warehouseid;
							 state.formData.arecase=data.arecase;
							 callback(data);
 							 if(data.itemlist && data.itemlist.length>0){
 								 data.itemlist.forEach(function(item){
 									 item.sku=item.SellerSKU;
 									 item.quantity=item.Quantity;
 									 item.fulfillable=item.invquantity;
 									 item.id=item.mid;
									 item.marketplaceid=data.marketplaceid;
 									 if(item.msku==undefined || item.msku==null || item.msku==''){
 										 item.msku=item.SellerSKU;
 									 }
 									 
 								 });
 								state.productlist=data.itemlist;
 								state.totalproducts=state.productlist.length;
								loadInventory(state,state.productlist,state.formData.warehouseid);
 								var skulist="";
 								if(state.productlist.length>0){
 									 state.productlist.forEach(function(items){
 										 skulist+=(items.sku+",");
 									 }); 
 								}
 								if(skulist!=""){
 									 validSkuList(state,skulist);
 								}
 							 }
 						 }
 					 });
 				 }else if(state.queryData.inboundplanid){
					 
					  					 //货件审核的时候 复制按钮 点进来的
					  					 state.productlist=[];
					  					 state.totalproducts=0;
					  					 shipmentplanApi.getItemlistByInboundPlanId({"inboundplanid":state.queryData.inboundplanid}).then((res)=>{
					  						 if(res.data){
					  							 var data=res.data;
					  							 state.formData.groupid=data.groupid;
					  							 state.formData.marketplaceid=data.marketplaceid;
					  							 state.formData.warehouseid=data.warehouseid;
												 state.formData.arecase=data.arecase;
					 							 callback(data);
					  							 if(data.itemlist && data.itemlist.length>0){
					  								 data.itemlist.forEach(function(item){
					  									 item.sku=item.SellerSKU;
					  									 item.quantity=item.Quantity;
					  									 item.fulfillable=item.invquantity;
					  									 item.id=item.mid;
					  									 if(item.msku==undefined || item.msku==null || item.msku==''){
					  										 item.msku=item.SellerSKU;
					  									 }
					  									 
					  								 });
					  								state.productlist=data.itemlist;
					  								state.totalproducts=state.productlist.length;
					 								loadInventory(state,state.productlist,state.formData.warehouseid);
					  								var skulist="";
					  								if(state.productlist.length>0){
					  									 state.productlist.forEach(function(items){
					  										 skulist+=(items.sku+",");
					  									 }); 
					  								}
					  								if(skulist!=""){
					  									 validSkuList(state,skulist);
					  								}
					  							 }
					  						 }
					  					 });
					 
				 }else if(state.queryData.formid){
					//货件审核的时候 复制按钮 点进来的
					state.productlist=[];
					state.totalproducts=0;
					shipmentplanV2Api.getItemlistByFormId({"formid":state.queryData.formid}).then((res)=>{
										  						 if(res.data){
										  							 var data=res.data;
										  							 state.formData.groupid=data.groupid;
										  							 state.formData.marketplaceid=data.marketplaceid;
										  							 state.formData.warehouseid=data.warehouseid;
										 							 callback(data);
										  							 if(data.itemlist && data.itemlist.length>0){
										  								 data.itemlist.forEach(function(item){
										  									 item.sku=item.sellersku;
										  									 item.quantity=item.confirmQuantity;
										  									 item.fulfillable=item.invquantity;
										  									 item.id=item.mid;
										  									 if(item.msku==undefined || item.msku==null || item.msku==''){
										  										 item.msku=item.SellerSKU;
										  									 }
										  									 
										  								 });
										  								state.productlist=data.itemlist;
										  								state.totalproducts=state.productlist.length;
										 								loadInventory(state,state.productlist,state.formData.warehouseid);
										  								var skulist="";
										  								if(state.productlist.length>0){
										  									 state.productlist.forEach(function(items){
										  										 skulist+=(items.sku+",");
										  									 }); 
										  								}
										  								if(skulist!=""){
										  									 validSkuList(state,skulist);
										  								}
										  							 }
										  						 }
					}); 
				 }
				 
 			 }