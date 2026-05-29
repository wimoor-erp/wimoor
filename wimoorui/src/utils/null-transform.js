/*空字符转换为"-" */
export default function(val){
	if(val==null||val==undefined||val==""){
		return "-"
	}else{
		return val
	}
}  