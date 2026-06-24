import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";


function getMonth(data){
    return request.post('/amazon/api/v1/settlementSummarySkuMonth/getMonth',data);
}

function getQuarter(data){
    return request.post('/amazon/api/v1/settlementSummarySkuMonth/getQuarter',data);
}
function downloadMonth(data,callback){
    return request({url:"/amazon/api/v1/settlementSummarySkuMonth/downloadMonth",
        responseType:"blob",
        data:data,
        method:'post'}).then(res => {
        downloadhandler.downloadSuccess(res,"FinancesMonthExcel.xlsx")
        if(callback){
            callback();
        }
    }).catch(e=>{
        downloadhandler.downloadFail(e);
        if(callback){
            callback();

        }
    });
}

export default{
    getMonth,getQuarter,downloadMonth,
}