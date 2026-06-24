import request from "@/utils/request.js";
import store from '@/store/index';
import {sortByKey} from '@/utils/index.js';
export const menuApi={
	    getRoute:function(){
	     		return request.get("/api/admin/api/v1/menus/route");
	    },
		cleanUserCache:function(){
			    return request.get("/api/admin/api/v1/menus/cleanUserCache");
		},
		getMenuALL:function(callback){
			 	                 let response={};
			 	                 let menuDataModel=[];
			 	                 let submenulistModel=[];
								 let menulist=store.state.routerStore.router;
								  if(menulist&&menulist.length>0){
										  menulist=sortByKey(menulist,"sort");
								  }
			 	                 for (let i=0; i<menulist.length; i++){
			 						  let menu=store.state.routerStore.router[i];
			 						  let menuitem={};
			 						  menuitem.name=menu.meta.title;
			 						  menuitem.iconName=menu.meta.icon;
			 						  menuitem.id=menu.name;
									  menuitem.path=menu.path;
			 						  menuDataModel.push(menuitem);
			 						  ///////////////////生成sub menu
			 					 
			 						  let sublist=[];
			 						  if(menu.children){
										  if(menu.children.length>0){
										  	   menu.children=sortByKey(menu.children,"sort");
										  }
			 								for (let j=0; j<menu.children.length; j++){
			 								let child=menu.children[j];
			 								if(child.children){
			 							       let namegrouplist=[];
											   if(child.children.length>0){
											   	   child.children=sortByKey(child.children,"sort");
											   }
			 								   for(let k=0;k<child.children.length;k++){
			 									  let item=child.children[k];
												  if(item.path){
													  namegrouplist.push({"id":item.name,
														                  "name":item.meta.title,
													                      "path":item.path,
																		  "isout":false});
												  }else{
													  if(item["redirect"]&&(item.redirect.indexOf(".do")>0||item.redirect.indexOf(".jsp")>0)){
													  		if(item["sort"]>=711 &&item["sort"]<=717 ){
																namegrouplist.push({"name":item.meta.title,
																                     "id":item.name,
																					 "isout":true,
																                     "path":"https://weilan.wimoor.com/page.do?location="+item.oldid });  
															}else{
																namegrouplist.push({"name":item.meta.title,
																                     "id":item.name,
																					 "isout":true,
																                     "path":"https://app.wimoor.com/page.do?location="+item.oldid });  
															}
															
													  }
													  
													
												  }
			 									 
			 								     }
			 						 
			 							     sublist.push({"namegroup":namegrouplist,"isShow":false,name:child.meta.title}) ;
			 								}
			 						     }
			 						  }
			 					    if(sublist.length>0){
			 						    submenulistModel.push({"id":menu.name,"menugroup":sublist});
			 					    } 
			 					}
			 					response.menuData=menuDataModel;
			 					response.submenus=submenulistModel;
			 					callback(response);
		  }
  };
  
