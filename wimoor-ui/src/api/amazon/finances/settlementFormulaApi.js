 import request from "@/utils/request.js";
 
 function loadformula(data){
 	 return request.get("/amazon/api/v1/fin/settlementFormula/loadformula",{params:data})
 } 
function formulaSave(data){
	 return request.get("/amazon/api/v1/fin/settlementFormula/formulaSave",{params:data})
}

function getformulaTitle(data){
	 return request.get("/amazon/api/v1/fin/settlementFormula/getformulaTitle",{params:data})
}


export default{
	loadformula,formulaSave,getformulaTitle,
}