  import $ from 'jquery';
  import {nextTick} from 'vue';
  /**
   * 用于控制input框的按键聚焦动作
   * input框通过ID 的c-x-y 其中x 和y 代表坐标从0开始
   * @param {Object} container
   */
  function toPoint(x,y){
	  var $input=$("#c-"+x+"-"+y);
	  if($input&&$input.length>0){
		   $input.focus(); 
	  }
  }
 export function pointKeyChnage(container){
	  nextTick(() => {  
		  $(container).find("input").each(function (index,input){
		  						 var id=$(input).attr("id");
		  						 if(id.indexOf("c-")>=0){
		  							 $(input).keydown(function(e){
		  								 var point=$(this).attr("id").split("-");
		  								 var x=parseInt(point[1]);
		  								 var y=parseInt(point[2]);
		  								  if (e.keyCode ===  40) {
		  									  y=y+1;
		  									  toPoint(x,y);
		  								     }
		  								     if (e.keyCode ===  38) {
		  										   y=y-1;
		  										   if(y>=0){
		  											 toPoint(x,y);
		  										   }
		  								     }
		  								     if (e.keyCode === 37) {
		  								         x=x-1;
		  								         if(x>=0){
		  											  toPoint(x,y);
		  								         }
		  								     }
		  								     if (e.keyCode ===  39) {
		  								            x=x+1;
		  											toPoint(x,y);
		  								     }
		  							 })
		  						 }
		  					 }) 
	  });
	
 }
 