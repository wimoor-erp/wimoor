import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function downloadShipTemplateFile(data, callback, name) {
	return request({
		url: "/amazon/api/v2/ship/customs/downloadShipTemplateFile",
		responseType: "blob",
		params: data,
		method: 'get'
	}).then(res => {
		downloadhandler.downloadSuccess(res, name);
		if (callback) {
			callback(res);
		}
	}).catch(e => {
		downloadhandler.downloadFail(e);
		if (callback) {
			callback(e);
		}
	});
}

function deleteCustomsFile(data) {
	return request.get('/amazon/api/v2/ship/customs/deleteCustomsFile', { params: data });
}
function getShipmentCustomsRecord(data) {
	return request.get('/amazon/api/v2/ship/customs/getShipmentCustomsRecord', { params: data });
}

function uploadCustomsFile(FormData) {
	return request({
		'method': 'POST',
		'url': "/amazon/api/v2/ship/customs/uploadCustomsFile",
		'data': FormData,
		'headers': { 'Content-Type': "multipart/form-data" },

	});
}

function generateCustomsXml(data) {
	return request.post('/amazon/api/v2/ship/customsxml/generateCustomsXml', data);
}


function updateShipmentCustomsXml(data) {
	return request.post('/amazon/api/v2/ship/customsxml/updateShipmentCustomsXml', data);
}

function getCustomsXml(data) {
	return request.get('/amazon/api/v2/ship/customsxml/getCustomsXml', { params: data });
}

function disabledCustomsXml(data) {
	return request.get('/amazon/api/v2/ship/customsxml/disabledCustomsXml', { params: data });
}

function viewXmlData(data) {
	return request.post('/amazon/api/v2/ship/customsxml/viewXmlData', data);
}

function customsData(data) {
	return request.get('/amazon/api/v2/ship/customsxml/customsData', { params: data });
}
function generateFileName(data) {
	return request.post('/amazon/api/v2/ship/customsxml/generateFileName', data);
}
function listCustomsXml(data) {
	return request.post('/amazon/api/v2/ship/customsxml/listCustomsXml', data);
}

function listCustomsXmlItem(data) {
	return request.post('/amazon/api/v2/ship/customsxml/listCustomsXmlItem', data);
}

function summaryCustomsXml(data) {
	return request.post('/amazon/api/v2/ship/customsxml/summaryCustomsXml', data);
}



function downExcelCustomsXml(data) {
	return request({
		url: "/amazon/api/v2/ship/customsxml/downExcelCustomsXml",
		responseType: "blob",
		data: data,
		method: 'post'
	}).then(res => {
		downloadhandler.downloadSuccess(res, "customList.xlsx")
	}).catch(e => {
		downloadhandler.downloadFail(e);
	});
}

function downloadCustomsExcel(data) {
	return request({
		url: "/amazon/api/v2/ship/customsxml/downloadCustomsExcel",
		responseType: "blob",
		data: data,
		method: 'post'
	}).then(res => {
		downloadhandler.downloadSuccess(res, "customsReport.xlsx")
	}).catch(e => {
		downloadhandler.downloadFail(e);
	});
}

function importReturnStatus(data) {
	return request({
		'method': 'POST',
		'url': "/amazon/api/v2/ship/customsxml/importReturnStatus",
		'data': data,
		'headers': { 'Content-Type': "multipart/form-data" },
	});
}

export default {
	downloadShipTemplateFile, deleteCustomsFile, getShipmentCustomsRecord, uploadCustomsFile, generateCustomsXml,
	updateShipmentCustomsXml, disabledCustomsXml, viewXmlData, customsData, generateFileName, getCustomsXml,
	listCustomsXml, listCustomsXmlItem, summaryCustomsXml, downExcelCustomsXml, downloadCustomsExcel, importReturnStatus,
}