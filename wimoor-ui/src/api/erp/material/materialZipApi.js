import request from "@/utils/request.js";

function uploadFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/material/zip/uploadFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
export default{
	uploadFile
}