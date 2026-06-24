/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string')) {
      if ((/^[0-9]+$/.test(time))) {
        // support "1548221490638"
        time = parseInt(time)
      } else {
        // support safari
        // https://stackoverflow.com/questions/4310953/invalid-date-in-safari
        time = time.replace(new RegExp(/-/gm), '/')
      }
    }

    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{([ymdhisa])+}/g, (result, key) => {
    const value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value ] }
    return value.toString().padStart(2, '0')
  })
  return time_str
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function getQueryObject(url) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * @param {string} input value
 * @returns {number} output value
 */
export function byteLength(str) {
  // returns the byte length of an utf8 string
  let s = str.length
  for (var i = str.length - 1; i >= 0; i--) {
    const code = str.charCodeAt(i)
    if (code > 0x7f && code <= 0x7ff) s++
    else if (code > 0x7ff && code <= 0xffff) s += 2
    if (code >= 0xDC00 && code <= 0xDFFF) i--
  }
  return s
}

/**
 * @param {Array} actual
 * @returns {Array}
 */
export function cleanArray(actual) {
  const newArray = []
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i])
    }
  }
  return newArray
}

/**
 * @param {Object} json
 * @returns {Array}
 */
export function param(json) {
  if (!json) return ''
  return cleanArray(
    Object.keys(json).map(key => {
      if (json[key] === undefined) return ''
      return encodeURIComponent(key) + '=' + encodeURIComponent(json[key])
    })
  ).join('&')
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url) {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj = {}
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}

/**
 * @param {string} val
 * @returns {string}
 */
export function html2Text(val) {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

/**
 * Merges two objects, giving the last one precedence
 * @param {Object} target
 * @param {(Object|Array)} source
 * @returns {Object}
 */
export function objectMerge(target, source) {
  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach(property => {
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

/**
 * @param {HTMLElement} element
 * @param {string} className
 */
export function toggleClass(element, className) {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString =
      classString.substr(0, nameIndex) +
      classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

/**
 * @param {string} type
 * @returns {Date}
 */
export function getTime(type) {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90
  } else {
    return new Date(new Date().toDateString())
  }
}
 
/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param {Object} source
 * @returns {Object}
 */
export function deepClone(source) {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments', 'deepClone')
  }
  const targetObj = source.constructor === Array ? [] : {}
  Object.keys(source).forEach(keys => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

/**
 * @param {Array} arr
 * @returns {Array}
 */
export function uniqueArr(arr) {
  return Array.from(new Set(arr))
}

/**
 * @returns {string}
 */
export function createUniqueString() {
  const timestamp = +new Date() + ''
  const randomNum = parseInt((1 + Math.random()) * 65536) + ''
  return (+(randomNum + timestamp)).toString(32)
}

/**
 * Check if an element has a class
 * @param {HTMLElement} elm
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele, cls) {
  return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}

/**
 * Add class to element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function addClass(ele, cls) {
  if (!hasClass(ele, cls)) ele.className += ' ' + cls
}

/**
 * Remove class from element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function removeClass(ele, cls) {
  if (hasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
    ele.className = ele.className.replace(reg, ' ')
  }
}

export function formatFloat(src,pos){  
	//四舍五入,保留2位小数 
   if(src===""||src===undefined){
   	return "-";
   }else if(src===0){
   	   return 0;
   }else {
		if(pos==null){
			pos=2;
		}
		return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);      
   }
} 

export function formatPercent(src){   //四舍五入,保留2位小数 再算出百分比  
	if(src && src!=0){
		src=src*100;
		return Math.round(src*Math.pow(10, 2))/Math.pow(10, 2);  
	}else{
		return 0;
	}
} 

export function formatInteger(src){
	if(src==""||src==undefined){
		return "--"
	}else{
		 return parseInt(src);    
	}//取整数  
}


export function number(value){
	return value.replace(/[^\d.]/g,'');
}

export function sortByKey(array,key){
	return array.sort(function(a,b){
		let x=a[key];
		let y=b[key];
		return ((x<y)?-1:(x>y)?1:0);
	})
}
Date.prototype.clone=function(){
		  return new Date(this.valueOf());
 }
Date.prototype.format = function(fmt) { 
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
     for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
     }
    return fmt; 
	//yyyy-MM-dd hh:mm:ss日期格式
}     
export function guid() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
	return v.toString(16);
	});
}
export function dateFormat(time){
	if(!time)return "";
	if(('' + time).indexOf("-")>0||('' + time).indexOf("/")>0){
	  return time.substring(0,10);
	}else if (('' + time).length === 10) {
	  time = parseInt(time) * 1000;
	}
	const d = new Date(time);
	return d.format("yyyy-MM-dd");
}
export function dateFormatMMdd(time){
	if(!time)return "";
	if (('' + time).length === 10) {
	  time = parseInt(time) * 1000;
	} else if(('' + time).indexOf("-")>0||('' + time).indexOf("/")>0){
	  return time.substring(0,10);
	}
	const d = new Date(time);
	return d.format("MM-dd");
}
export function dateTimesFormat(time,format){
	if(!time)return "-";
	if (('' + time).length === 10) {
	  time = parseInt(time) * 1000;
	} else if(('' + time).indexOf("-")>0||('' + time).indexOf("/")>0){
	  return time;
	}
	const d = new Date(time);
	if(format){
		return d.format(format);
	}else{
		return d.format("yyyy-MM-dd hh:mm:ss");
	}
}
export function dateTimesWithoutYearFormat(time){
	if(!time)return "-";
	if (('' + time).length === 10) {
	  time = parseInt(time) * 1000;
	} else if(('' + time).indexOf("-")>0||('' + time).indexOf("/")>0){
		if(time.length>=19){
			return time.substring(5,time.length);
		}
	  return time;
	}
	const d = new Date(time);
	return d.format("MM-dd hh:mm:ss");
}
 export function dateMonthFormat(time){
 	if(!time)return "-";
 	if (('' + time).length === 10) {
 	  time = parseInt(time) * 1000;
 	} else if(('' + time).indexOf("-")>0||('' + time).indexOf("/")>0){
 	  return time;
 	}
 	const d = new Date(time);
 	return d.format("yyyy-MM");
 }
 export function dateYearFormat(time){
 	if(!time)return "-";
 	if (('' + time).length === 10) {
 	  time = parseInt(time) * 1000;
 	} else if(('' + time).indexOf("-")>0||('' + time).indexOf("/")>0){
 	  return time;
 	}
 	const d = new Date(time);
 	return d.format("yyyy");
 }
export  function deepCopy(obj){
     let target  = null
     if(typeof obj === 'object'){
         if(Array.isArray(obj)){ //数组
             target = [];
             obj.forEach(item => {
                 target.push(deepCopy(item));
             })
         }else if(obj){
             target = {}
             let objKeys = Object.keys(obj);
             objKeys.forEach(key => {
                 target[key] = deepCopy(obj[key]);
             })
         }else{
             target = obj
         }
     }else{
         target = obj;
     }
     return target
 }
 
export  function CheckALLFloat(value) {
 	value=value+"";
 	if(value&& '-' != value&&'' != value.replace(/-{0,1}\d{1,}\.{0,1}\d{0,}/,'')) { 
 		value = value.match(/-{0,1}\d{1,}\.{0,1}\d{0,}/) == null ? '' :value.match(/-{0,1}\d{1,}\.{0,1}\d{0,}/); 
 	} 
 	return value;
  }
export  function CheckInputFloat(value) { 
	value=value+"";
 	if(value&& '' != value.replace(/\d{1,}\.{0,1}\d{0,}/,'')) { 
 		value = value.match(/\d{1,}\.{0,1}\d{0,}/) == null ? '' :value.match(/\d{1,}\.{0,1}\d{0,}/); 
 	} 
	return value;
 }
 
export function CheckInputInt(value) {
	value=value+"";
 	if(value&&'' != value.replace(/\d{1,}/,'')) { 
 		value = value.match(/\d{1,}/) == null ? '' :value.match(/\d{1,}/); 
 	} 
	return value;
 }
export function CheckInputUpper(value) {
	value=value+"";
 	if(value){
 		value=value.toUpperCase();
 	}
	 return value;
 }
export function CheckInputIntLGZero(value) {
	value=value+"";
 	if(value&&'' != value.replace(/^[1-9]\d{0,}$/,'')) { 
 		value = value.match(/^[1-9]\d{0,}$/) == null ? '' :value.match(/^[1-9]\d{0,}$/); 
 	} 
	return value;
 }

export function isInt(value){
	value=value+"";
 	var re = /^\d+$/;// /^[1-9]+[0-9]*]*$/;  
     if (re.test(value)) {  
 		return true;
 	} else {
 		return false;
 	}
 }
export function isFloat(value) {
	 value=value+"";
     var partten =/(^[1-9][0-9]*[\.]{0,1}[0-9]*[0-9]$)|(^[0].[0-9]*[0-9]$)|(^[0-9]$)/;
     if (partten.test(value)) {  
 		return true;
 	} else {
 		return false;
 	}
  }

 export function getValue(value,prefix,suffix){
 	if(value==null||value==""||typeof(value)=="undefined"||(typeof(value) === "number" && isNaN(value))){
 		return "-";
 	}else {
 		if(prefix){
 			value=prefix+value;
 		}
 	    if(suffix){
 	    	value=value+suffix;
 		} 
 	  return value;
 	}
 }
 export function getCountryCode(value){
 	 if(value=="ATVPDKIKX0DER"){
		 value="US";
	 }
	 if(value=="A13V1IB3VIYZZH"){
	 		 value="FR";
	 }
	 if(value=="A17E79C6D8DWNP"){
	 		 value="SA";
	 }
	 if(value=="A1805IZSGTT6HS"){
	 		 value="NL";
	 }
	 if(value=="A19VAU5U5O7RUS"){
	 		 value="SG";
	 }
	 if(value=="A1AM78C64UM0Y8"){
	 		 value="MX";
	 }
	 if(value=="A1C3SOZRARQ6R3"){
	 		 value="PL";
	 }
	 if(value=="ATVPDKIKX0DER"){
	 		 value="US";
	 }
	 if(value=="A1F83G8C2ARO7P"){
	 		 value="UK";
	 }
	 if(value=="A1PA6795UKMFR9"){
	 		 value="DE";
	 }
	 if(value=="A1RKKUPIHCS9HS"){
	 		 value="ES";
	 }
	 if(value=="A1VC38T7YXB528"){
	 		 value="JP";
	 }
	 if(value=="A21TJRUUN4KGV"){
	 		 value="IN";
	 }
	 if(value=="A2EUQ1WTGCTBG2"){
	 		 value="CA";
	 }
	 if(value=="A2NODRKZP88ZB9"){
	 		 value="SE";
	 }
	 if(value=="A2Q3Y263D00KWC"){
	 		 value="BR";
	 }
	 if(value=="A2VIGQ35RCS4UG"){
	 		 value="AE";
	 }
	 if(value=="A33AVAJ2PDY3EV"){
	 		 value="TR";
	 }
	 if(value=="A39IBJ37TRP1C6"){
	 		 value="AU";
	 }
	 if(value=="APJ6JRA9NG5V4"){
	 		 value="IT";
	 }
	 if(value=="ARBP9OOSHTCHU"){
	 		 value="EG";
	 }
	 return value;
 }
 export function getMarketplaceId(value){
 	 if(value=="US"){
		 value="ATVPDKIKX0DER";
	 }
	 if(value=="FR"){
	 		 value="A13V1IB3VIYZZH";
	 }
	 if(value=="SA"){
	 		 value="A17E79C6D8DWNP";
	 }
	 if(value=="NL"){
	 		 value="A1805IZSGTT6HS";
	 }
	 if(value=="SG"){
	 		 value="A19VAU5U5O7RUS";
	 }
	 if(value=="MX"){
	 		 value="A1AM78C64UM0Y8";
	 }
	 if(value=="PL"){
	 		 value="A1C3SOZRARQ6R3";
	 }
	 if(value=="US"){
	 		 value="ATVPDKIKX0DER";
	 }
	 if(value=="UK"){
	 		 value="A1F83G8C2ARO7P";
	 }
	 if(value=="DE"){
	 		 value="A1PA6795UKMFR9";
	 }
	 if(value=="ES"){
	 		 value="A1RKKUPIHCS9HS";
	 }
	 if(value=="JP"){
	 		 value="A1VC38T7YXB528";
	 }
	 if(value=="IN"){
	 		 value="A21TJRUUN4KGV";
	 }
	 if(value=="CA"){
	 		 value="A2EUQ1WTGCTBG2";
	 }
	 if(value=="SE"){
	 		 value="A2NODRKZP88ZB9";
	 }
	 if(value=="BR"){
	 		 value="A2Q3Y263D00KWC";
	 }
	 if(value=="AE"){
	 		 value="A2VIGQ35RCS4UG";
	 }
	 if(value=="TR"){
	 		 value="A33AVAJ2PDY3EV";
	 }
	 if(value=="AU"){
	 		 value="A39IBJ37TRP1C6";
	 }
	 if(value=="IT"){
	 		 value="APJ6JRA9NG5V4";
	 }
	 if(value=="EG"){
	 		 value="ARBP9OOSHTCHU";
	 }
	 return value;
 }

export function getCurrencyMark(currency){
	    var fcuurency="";
		if (currency != null) {
			if (currency=="USD") {
				fcuurency = "$";
			}
			if (currency=="GBP") {
				fcuurency = "£";
			}
			if (currency=="EUR") {
				fcuurency = "€";
			}
			if (currency=="JPY") {
				fcuurency = "¥";
			}
			if (currency=="CNY") {
				fcuurency = "¥";
			}
			if (currency=="RMB") {
				fcuurency = "¥";
			}
			if (currency=="CAD") {
				fcuurency = "C$";
			}
			if (currency=="INR") {
				fcuurency = "₹";
			}
			if (currency=="AUD") {
				fcuurency = "A$";
			}
			if (currency=="MXN") {
				fcuurency = "Mex$";
			}
			if (currency=="SEK") {
				fcuurency = "Kr";
			}
			if (currency=="PLN") {
				fcuurency = "zł";
			}
			return fcuurency;
		}else{
			return "";
		}
}
 export function getDateValue(value){
					if(value){
						if (('' + value).length === 10) {
						   value = parseInt(value) * 1000;
						} else if(('' +value).indexOf("-")>0||('' +value).indexOf("/")>0){
						   value= new Date(value);;
						}
					}
					return value;
				}
export function spaceCharInput(value){
	value=value+"";
	if(value){
		value = value.match(/^[\u4E00-\u9FA5A-Za-z0-9_-]+$/); 
	}
	return value;
}
export function percentInput(value){
	value=value+"";
	if(value){
		value = value.match(/^(0|[1-9][0-9]*)$/); 
	}
	return value;
}
export function checkPassword(value){
	value=value+"";
	if(value){
		value = value.match(/^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,}$/); 
	}
	return value;
}
 export function checkEmail(value){
 	value=value+"";
 	if(value){
 		value = value.match(/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/); 
 	}
 	return value;
 } 

export function outputmoney(number) {  
	if(!number)return "0";
   if(number==0)return "0";
   if (isNaN(number) || number == "") return "";  
   number = Math.round(number * 100) / 100;  
   if (number < 0)  
       return '-' + outputdollars(Math.floor(Math.abs(number) - 0) + '') + outputcents(Math.abs(number) - 0);  
   else  
       return outputdollars(Math.floor(number - 0) + '') + outputcents(number - 0);  
}  
export function outputcents(amount) {  
   amount = Math.round(((amount) - Math.floor(amount)) * 100);  
   return (amount < 10 ? '.0' + amount : '.' + amount);  
}  

//格式化金额   
export function outputdollars(number) {  
   if (number==undefined||isNaN(number) || number == "") return "";  
   if ( number == 0) return "0";  
   number=number+"";
   if (number.length <= 3)  
       return (number == '' ? '0' : number);  
   else {  
       var mod = number.length % 3;  
       var output = (mod == 0 ? '' : (number.substring(0, mod)));  
       for (var i = 0; i < Math.floor(number.length / 3); i++) {  
           if ((mod == 0) && (i == 0))  
               output += number.substring(mod + 3 * i, mod + 3 * i + 3);  
           else  
               output += ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);  
       }  
       return (output);  
   }  
}  			

export function  decodeText(str) {
	if(str){
		var reg = new RegExp("\n", "g");
		 str = str.replace(reg, "<br>");
		return str;
	}else{
		return "";
	}
}
 
//<br/> 转 textArea换行符
export function encodeText(str) {
 if(!str)return;
  var reg = new RegExp("<br>", "g");
  str = str.replace(reg, "\n");
 return str;
}

export function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}