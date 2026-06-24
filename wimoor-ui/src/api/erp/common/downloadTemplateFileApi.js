
import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function downExcelTemp(data,action){
	var myActionUrl="/erp/api/v1/common/excelDownload/downExcelTemp";
	if(action&&action.indexOf("/")>=0){
		 myActionUrl=action;
	} 
	return request({url:myActionUrl,
					responseType:"blob",
					params:data,
					method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,data.ftype+".xlsx");
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
				});
}

function downloadlist(data){
	return request.get("/erp/api/v1/common/excelDownload/downloadlist",{params:data}) ;
}

function deletedownload(data){
	return request.get("/erp/api/v1/common/excelDownload/deletedownload",{params:data}) ;
}

function downExcel(data){
	var myActionUrl="/erp/api/v1/common/excelDownload/downExcel";
	return request({url:myActionUrl,
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,data.ftype+".xlsx");
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
				});
}
export default{
	downExcelTemp,downloadlist,deletedownload,downExcel
}