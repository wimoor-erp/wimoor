import {ElMessage} from 'element-plus';
function downloadSuccess(res,filename){
	ElMessage.success('导出成功！');
	const blob = new Blob([res]);
	if(window.navigator["msSaveOrOpenBlob"]&&window.navigator.msSaveOrOpenBlob()){
		 navigator.msSaveBlob(blob, filename)
	}else{
		 var link=document.createElement("a");
		 link.href=window.URL.createObjectURL(blob);
		 link.download=filename;
		 link.click();
		 window.URL.revokeObjectURL(link.href);
	}
}
 
function downloadFail(res,filename){
			
	if(res&&res.response.data){
		var reader = new FileReader();
		reader.readAsText(res.response.data, 'utf-8');
		reader.onload = function (e) {
			 var result=JSON.parse(reader.result);
	         if(result&&result.msg){
				 ElMessage.error('导出失败！'+result.msg);
			 }else{
				 ElMessage.error('导出失败！')
			 }
			 
		}
	}
	else if(res&&res.msg){
		 ElMessage.error('导出失败！'+res.msg);
	}else if(res&&res.message){
	     ElMessage.error('导出失败！'+res.message)
	}else{
	     ElMessage.error('导出失败！')
	}
	
}
export default{
	downloadSuccess,downloadFail
}