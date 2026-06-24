  import $ from 'jquery';
  import {nextTick} from 'vue';
 export function screenToTop(){
	  nextTick(() => {document.documentElement.scrollTop=0;});
 }
 export function checkVisiable(id){
	 return $("#"+id).is(":visible")
 }
 export function tableHeaderFloat(id){
	  nextTick(() => {
	  								function scrollHandler() {
										    var headers=$("#"+id).find(".el-table__header-wrapper");
											var bodys=$("#"+id).find(".el-table__body-wrapper");
											var scrollbar=$("#"+id).find(".el-scrollbar__bar.is-horizontal");
											if(headers.length==0||bodys.length==0)return;
	  								        var tablehead= $(headers[0]);
	  								        var tablebody=$(bodys[0]); 
											if(tablehead.length==0||tablebody.length==0)return;
	  								        var scroH = $(document).scrollTop();  //滚动高度
	  								        var viewH =  document.body.clientHeight;  //可见高度 
	  								        var contentH = document.body.scrollHeight;  //内容高度
	  										var headHeight=tablehead[0].clientHeight;
											var headWidth=tablehead[0].clientWidth;
											var rightfield=$("#"+id).find(".el-table-fixed-column--right");
											var leftfieldwidth=rightfield.clientWidth;
                                            var doc = document;   
	                                        var win = window;   
											  
	                                        var $ScrollBottom = $(doc).height() - $(win).height() - $(win).scrollTop();
	  								        var mytop=parseInt(tablebody.offset().top)-(headHeight+headHeight);
												if(mytop==NaN){ return ; }
												if(mytop<0){ mytop=0; }
	  								              if(scroH>mytop){
	  								            	  tablebody.css("margin-top",headHeight+ "px");
													  tablehead.css("width",headWidth +"px");
	  								            	  tablehead.addClass("floathead");
	  								              }else{
	  								            	   tablebody.css("margin-top",0) ;
													   tablehead.css("width", "inherit");
	  								            	   tablehead.removeClass("floathead");
	  								              }	
												  if($ScrollBottom>60){
													scrollbar.css("margin-left",(document.body.scrollWidth-bodys.width()-30)+"px")
													scrollbar.addClass("floatfooter");
												  }else{
													  scrollbar.css("margin-left",0+"px");
													  scrollbar.removeClass("floatfooter");
												  }	
															
	  									    }
	  					        	$(document).scroll(scrollHandler);
									scrollHandler();
	  				  });
  }
  