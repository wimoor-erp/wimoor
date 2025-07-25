 
 
document.getElementById("startsubmit").onclick=function(){
	  	  chrome.tabs.query({active:true, currentWindow:true}, function (tab) {//获取当前tab
	  				    chrome.tabs.sendMessage(tab[0].id,  {data:"自动任务"}, 
	  					function (response) {
	  							if(response){
	  								document.getElementById("smessage").innerHTML=response.message+"<br/>";
	  							}
	  					});
	          });
	   
	  }

//document.getElementById("downloadPageView").onclick=function(){
//	  chrome.tabs.query({active:true, currentWindow:true}, function (tab) {//获取当前tab
//         chrome.tabs.sendMessage(tab[0].id,  {type:"downloadPageView"}, 
//					function (response) {
//							if(response){
//								document.getElementById("smessage").innerHTML=response.message+"<br/>";
//							}
//					});
//        });
// }

const dataURLtoBlob = (dataUrl) => {
	  let arr = dataUrl.split(','),
	  mime = arr[0].match(/:(.*?);/)[1],
	  bstr = atob(arr[1]),
	  n = bstr.length,
	  u8arr = new Uint8Array(n);
	  while (n--) {
	    u8arr[n] = bstr.charCodeAt(n);
	  }
	  return new Blob([u8arr], {
	    type: mime
	  })
	}
	// Blob转换为File
	const blobToFile = (blob, fileName) => {
	  return new File([blob], fileName);
	}
 
//document.getElementById("capturedownload").onclick=function(){
//	  chrome.tabs.query({active:true, currentWindow:true}, function (tab) {//获取当前tab
//       chrome.tabs.sendMessage(tab[0].id,  {type:"downloadPageView"}, 
//					function (response) {
//							if(response){
//								$("#file-input").focus();
//								var WshShell=new ActiveXObject("WScript.Shell"); //会有安全提示
//								WshShell.sendKeys(response.filename); //向文本框里发送东东
//							}
//					});
//      });
//}

//var file = document.getElementById("file").files[0];
//var reader = new FileReader();
//reader.onload = function(e){
//  console.log(e.target.result);
//}
//reader.readAsText(file);