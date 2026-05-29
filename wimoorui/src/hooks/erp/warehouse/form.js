import {reactive,onBeforeMount,ref,watch } from 'vue'
import shelfApi from '@/api/erp/warehouse/shelf.js';
import { ElMessage } from 'element-plus';
 export default function myform(){
	const ruleForm = ref(); 
	const itemForm = ref();
    const formdata=reactive(
	{   parentname:'暂无父容器',
		parentid:'',
		parentspecifications:'',
		name:'',
		length:'',
		width:'',
		height:'',
		number:'',
		subqty:0,
		treepath:'',
		desc:''});
		
    const listitem=reactive({list:[]});
     let rules= {
	          name: [
	            { required: true, message: '请输入库位名称', trigger: 'blur' },
	            { min: 1, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur', },
	          ],      
			  number: [
	            { required: true, message: '请输入库位编码', trigger: 'blur' },
	            { min: 1,max: 5,  message: '长度在 3 到 5 个字符', trigger: 'blur',},
				{ pattern: /[a-zA-Z0-9]+/, message: '编号只能包含字母或者数字', trigger: 'blur' },
	          ],
			  length:[ { pattern:/^(-?\d+)(\.\d+)?$/, message: '规格只能输入正小数', trigger: 'blur' }, ],
			  width:[ { pattern:/^(-?\d+)(\.\d+)?$/, message: '规格只能输入正小数', trigger: 'blur' }, ],
			  height:[ { pattern:/^(-?\d+)(\.\d+)?$/, message: '规格只能输入正小数', trigger: 'blur' }, ],
	        }; 
			
function pad(num,n){//数字位数不够补0操作
					var len=num.toString().length;
					while(len<n){
						num="0"+num;
						len++;
					}
					return num;
				}
				
function charAdd(indexchar,num){//将字符按数字逻辑增长，与到进位则开始字符变成indexchar(a或者A)
					var numarr=num.split('');
					var position=num.length-1;
					var index=indexchar.charCodeAt(0);
					while(position>=0){ //n:200 
						if(num[position].charCodeAt(0)+1<index+26){
							var option=String.fromCharCode(num[position].charCodeAt(0)+1);
							numarr[position]=option;
							break;
						}
						numarr[position]=indexchar;
						position--;
					}
					num=numarr.join('');
					if(position<0){
						num=indexchar+num;
					}
					return num;
				}
	 
watch(()=>[formdata.quantity,
		   formdata.name,
		   formdata.number,
		   formdata.length,
		   formdata.width,
		   formdata.height],
		 ([newquantity,newname,newnumber,newlength,newwidth,newheight],
		  [oldquantity,oldname,oldnumber,oldlength,oldwidth,oldheight])=>{
					if(parseInt(formdata.quantity)>0){
						var myitemlist=[];
						var number=formdata.number;//编码
						var reg = /(\d+)$/g
						var result = reg.exec(number);
						var matchNum ="";
						var prefix="";
						var matchCharL ="";
						var matchCharU ="";
					    if(result){//如果没有匹配到，则result为null
							 matchNum = result[1];//结果数组中角标为1的值就是我们捕获的正则小分组中的串
							 prefix=number.replace(matchNum,"");
					    }else{
							reg = /([a-z]+)$/g
							var result = reg.exec(number);
							if(result){
								 matchCharL = result[1];//结果数组中角标为1的值就是我们捕获的正则小分组中的串
								 prefix=number.replace(matchCharL,"");
							}else{
								reg = /([A-Z]+)$/g
								result = reg.exec(number);
								if(result){
									 matchCharU = result[1];//结果数组中角标为1的值就是我们捕获的正则小分组中的串
								     prefix=number.replace(matchCharU,"");
								}
							}
						}
						var matchNumlength=0;
						
						if(matchNum){//当用户的其实编码是1位数字如1 ，但是库位是12个起始编码应该是01
							if(matchNum.length<formdata.quantity.length){
								matchNumlength=formdata.quantity.length;
							}else{//当用户的其实编码是1位数字如00011 ，但是库位是12个起始编码应该是00011
								matchNumlength=matchNum.length;
							}
						} 
						for(var i=0;i<parseInt(formdata.quantity);i++){ 
							var item={};
							item.warehouseid=formdata.warehouseid;//仓库ID
							item.name=formdata.name;//货柜名称
							 if(matchNum){//如果没有匹配到，则result为null
								   item.number=prefix+pad(parseInt(matchNum)+i,matchNum.length);
							   }else if(matchCharL){
								    if(i>0){
								       matchCharL=charAdd('a',matchCharL);
								    }
								    item.number=prefix+matchCharL;
							   }else if(matchCharU){
								   if(i>0){
									   matchCharU=charAdd('A',matchCharU);
								    }
								    item.number=prefix+matchCharU;
							   }else{
								   item.number=number;
							   }
								 
							    
							//item.capacity="";//容量 提交时需要计算
							item.length=formdata.length;//长度
							item.width=formdata.width;//宽度		
							item.height=formdata.height;//高度
							item.parentid=formdata.parentid;//父货柜ID
							item.sort=i;//排序即（柜子所在位置）
							//item.treepath="";//所有付货柜编码如：A01!033!F01 提交时需要计算
							item.iswarn=false;//是否报警
							item.isdelete=false;//是否逻辑删除
							item.isfrozen=false;//是否冻结
							myitemlist.push(item);
						}
						listitem.list=myitemlist;
						 
					}
				});
 
const submitForm=async () => {
				    var mylist=listitem.list;
				    var hash = {};
					var isRepeat=false;
				  for(let i =0;i<mylist.length;i++){
					  var item=mylist[i];
					  if(hash[item.number]) {
					    isRepeat= true;break;
					  }
					  if(!item.name){
						  ElMessage.error('仓位名称不能为空.')
						  return false;
					  }
					  if(!item.number){
						  ElMessage.error('编码名称不能为空.');
						  return false;
					  }
					  item.parentid=formdata.parentid;
					  hash[item.number] = true;
					  item.treepath=(formdata.treepath?formdata.treepath:"root")+"!"+item.number;
					  if(item.length&&item.height&&item.width){
						  item.capacity=parseFloat(item.length)*parseFloat(item.height)*parseFloat(item.width);
					  }
					  item.addressid=formdata.addressid;  
				  }
				  if(isRepeat){
				  		ElMessage.error('编码不能重复.');
				  		return false;
				  }
				var data=await shelfApi.saveWarehouseShelf(mylist);
				if(data&&data.code=="201"){
					ElMessage.success( '保存成功');
				}
				
			};
function resetForm(formName) {
			 ruleForm.value.resetFields()
			};
			
   return {ruleForm,rules,submitForm,resetForm,formdata,listitem};
 }