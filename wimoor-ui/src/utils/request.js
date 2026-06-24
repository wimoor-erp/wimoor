import axios from 'axios'
import router from "@/router";
import store from '@/store/index';
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/wimoor'
import { saveAs } from 'file-saver'
import useUserStore from '@/hooks/store/useUserStore.js'

let downloadLoadingInstance
const CancelToken = axios.CancelToken;
let cancelMap = new Map();
const request = axios.create({
	timeout: 600000,
	headers: { 'Content-Type': 'application/json;charset=utf-8' }
})
request.defaults.maxConcurrentRequests = 100;
axios.defaults.timeout = 300000;
// 请求白名单，如果请求在白名单里面，将不会被拦截校验权限
const whiteUrls = [
	"/user/add",
	'/user/getUserList',
	'/admin/api/v1/auth/login',
	'/admin/api/v1/auth/ssologinisrun',
	"/user/update",
	"/user/deleteUser",
	"/admin/api/v1/sms/checkSmsCode",
	"/admin/api/v1/users/updatePassword",
	"/amazon/api/v1/amzauthority/authSeller",
	"/amazonadv/api/v1/advert/bindAdvAuthData",
	"/admin/api/v1/users/getSmsCode",
	"/admin/api/v1/users/getEmailCode",
	"/admin/api/v1/users/register",
	"/admin/api/v1/auth/mfacode",
	"/admin/api/v1/users/updatePasswordForget",
	"/admin/api/v1/auth/verifySmsCode",
	"/quote/api/v1/quote/supplier/submitPrice/**",
	"/quote/api/v1/quote/supplier/submitPrice",
	"/quote/api/v1/quote/supplier/listSupplierOrder",
]
const whiteMutiUrls = [
	"/api/finance/periods/groups",
	"/admin/api/feishu/table/typeList",
	"/amazon/api/v0/feed/submitfeedInfo",
	"/amazon/api/v1/shipInboundPlan/getShipRecordByMarket",
	"/amazon/api/v1/report/product/analysis/productdetailByInfo",
	"/amazon/api/v1/shipForm/guidance",
	"/amazon/api/v1/product/salesplan/getExpandCountryData",
	"/amazon/api/v1/amzauthority/getAmazonGroup",
	"/amazon/api/v1/amzauthority/getMarketByGroup",
	"/erp/api/v1/material/getMaterialInventoryInfo",
	"/erp/api/v1/purchase/plan/getLast",
	"/erp/api/v1/warehouse/getlist",
	"/erp/api/v1/assembly/getSubForm",
	"/erp/api/v1/purchase_form/getPriceBySupplier",
	"/erp/api/v1/warehouse/shelf/list",
	"/erp/api/v1/warehouse/shelfInventoryOptPro/list",
	"/erp/api/v1/shipTransCompany/getTranlist",
	"/erp/api/v1/shipTransCompany/getChannel",
	"/erp/api/v1/shipTransCompany/getCompanyTranstypeList",
	"/admin/api/v1/dict-items",
	"/admin/api/v1/sysprogress",
	"/amazonadv/api/v1/advert/loadProfile",
	"/amazonadv/api/v1/advCampaignManager/getCampaignPlacement",
	"/amazonadv/api/v1/advAdgroupManager/getAdGroupSuggestBid",
	"/amazonadv/api/v1/advKeywordManager/getKeywordSuggestBid",
	"/amazonadv/api/v1/advProductAdsManager/getProductAdotherAsin",
	"/amazonadv/api/v1/advProductTargeManager/getTargetBidRecommendations",
	"/amazonadv/api/v1/advProductTargeManager/getRecommendationsProductCount",
	"/amazonadv/api/v1/advProductTargeManager/getNagetiveTargetforQuery",
	 "/amazon/api/v2/ship/customsxml/customsData",
	"/erp/api/v1/purchase/plan/item/listHis",
	"/erp/api/v1/purchase_form/getReceiveReport",
	"/amazon/api/v2/shipInboundPlan/shipment/getBaseInfo",
	"/amazon/api/v2/shipInboundPlan/transportation/listTransportationOptions",
	"/erp/api/v1/purchase_form/recallEntry",
	"/admin/api/v1/users/info",
]

/**
 * 通配符路径匹配
 * 支持 /** 前缀匹配，匹配所有子路径
 * 支持 * 通配符，匹配任意字符（不包含路径分隔符/）
 */
const matchUrl = (url, pattern) => {
    // 精确匹配
    if (url === pattern) {
        return true;
    }
    
    // 前缀匹配：以 /** 结尾，匹配所有子路径
    if (pattern.endsWith('/**')) {
        const prefix = pattern.slice(0, -3);
        // 如果前缀为空或者url以prefix开头，匹配成功
        if (!prefix || url.startsWith(prefix)) {
            return true;
        }
        return false;
    }
    
    // 通配符匹配
    if (pattern.includes('*')) {
        // 将通配符模式转换为正则表达式
        let regexStr = pattern
            .replace(/[.*+?^${}()|[\]\\]/g, '\\$&')  // 转义特殊字符
            .replace(/\\*\\*/g, '.*')                 // ** 匹配任意路径
            .replace(/\\*/g, '[^/]*')                // * 匹配单层路径
        const regex = new RegExp(`^${regexStr}$`);
        return regex.test(url);
    }
    
    return false;
};

/**
 * 检查URL是否在白名单中，支持通配符匹配
 */
const isWhiteUrl = (url) => {
    for (const pattern of whiteUrls) {
        if (matchUrl(url, pattern)) {
            return true;
        }
    }
    return false;
};

/**
 * 检查URL是否在重复请求白名单中，支持通配符匹配
 */
const isWhiteMutiUrl = (url) => {
    for (const pattern of whiteMutiUrls) {
        if (matchUrl(url, pattern)) {
            return true;
        }
    }
    return false;
};

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密 

request.interceptors.request.use(async config => {
	// 取出sessionStorage里面缓存的用户信息
	let jsessionid = localStorage.getItem("jsessionid");
	if (!isWhiteUrl(config.url) ) {  // 校验请求白名单
		if (!jsessionid) {
			localStorage.getItem("jsessionid")
			if (!sessionStorage.getItem("old_url_before_login")) {
				sessionStorage.setItem("old_url_before_login", window.location.pathname + window.location.search);
			}
			router.push("/ssologin");
		} else {
			var storage_jsessiontime = localStorage.getItem("jsessiontime");
			if (storage_jsessiontime) {
				let jsessiontime = store.state.jessionStore.jsessiontime;
				if (jsessiontime && jsessiontime != "null" && jsessiontime != null && jsessiontime != undefined) {
					if (storage_jsessiontime != jsessiontime) {
						ElMessage.error("账户异常，请刷新浏览器");
						return null;
					}
				} else {
					store.dispatch("jessionStore/setJessionTime", storage_jsessiontime);
				}
			} else {
				localStorage.setItem("jsessiontime", new Date().getTime());
				storage_jsessiontime = localStorage.getItem("jsessiontime");
				store.dispatch("jessionStore/setJessionTime", storage_jsessiontime);
			}
			config.headers['jsessionid'] = jsessionid;  // 设置请求头

			// 登出等关键接口跳过套餐限制检查
			const skipLimitUrls = ['/admin/api/v1/auth/apilogout', '/admin/api/v1/users/findbindlist'];
			const shouldSkipLimit = skipLimitUrls.some(url => config.url.indexOf(url) >= 0);

			// 检查用户套餐是否到期、订单使用量是否超过限制
			// 同步快速检查：如果已确认超限，直接阻断请求
			if (!shouldSkipLimit && useUserStore.isPackageLimitExceeded()) {
				throw new axios.Cancel('套餐额度已用完，请求被阻断');
			}
			// 跳过检查过程中的内部 API 调用，避免死循环
			if (!shouldSkipLimit && !useUserStore.isCurrentlyCheckingPackageLimit()) {
				const status = await useUserStore.throttledCheckPackageLimit();
				if (status === 'exceeded') {
					throw new axios.Cancel('套餐额度已用完，请求被阻断');
				}
			}
		}
	}
	if (cancelMap.has(config.url)) {
		let isIgnoreReportUrl = config.url.indexOf("ignoreRepeat") > 0;
		if (!isWhiteMutiUrl(config.url) && isIgnoreReportUrl === false) {
			cancelMap.get(config.url)();// 取消请求
		}
		cancelMap.delete(config.url); // 仓库里删除链接
	}
	// 统一在请求前添加 cancelToken 方法
	config.cancelToken = new CancelToken(function executor(c) {
		// executor 函数接收一个 cancel 函数作为参数
		cancelMap.set(config.url, c)
	})


	return config
}, error => {
	return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
	response => {
		let res = response.data;
		// 如果是返回的文件
		if (cancelMap.has(response.config.url)) {
			cancelMap.delete(response.config.url);
		}

		if (response.config.responseType === 'blob') {
			return res
		}
		// 兼容服务端返回的字符串数据
		if (typeof res === 'string') {
			if (res == "noauth") {
				localStorage.removeItem("jsessionid");
				if (!sessionStorage.getItem("old_url_before_login")) {
					sessionStorage.setItem("old_url_before_login", window.location.pathname + window.location.search);
				}
				router.push("/ssologin");
			}
			res = res ? JSON.parse(res) : res
		}
		// 验证token
		if (res && res.data) {
			if (res.code === 401) {
				localStorage.removeItem("jsessionid");
				if (!sessionStorage.getItem("old_url_before_login")) {
					sessionStorage.setItem("old_url_before_login", window.location.pathname + window.location.search);
				}
				router.push("/ssologin");
			}
		}
		if (res.code == 200 || res.code == 0) {
			return res;
		} else {
			if (res && res.msg) {
				ElMessage.error(res.msg);
			}
			return Promise.reject(res);
		}
	},
	error => {
		if (error.config && cancelMap.has(error.config.url)) {
			cancelMap.delete(error.config.url);
		}
		if (error.response && error.response.data) {
			let code = error.response.data.code;
			if (code === 401) {
				localStorage.removeItem("jsessionid");
				if (!sessionStorage.getItem("old_url_before_login")) {
					sessionStorage.setItem("old_url_before_login", window.location.pathname + window.location.search);
				}
				router.push("/ssologin");
			} else if (error.response && error.response.data.msg) {
				if (error.response.data.msg.indexOf("Duplicate entry") >= 0) {
					ElMessage.error("数据重复冲突，请勿频繁保存，如功能无法使用请联系管理员");
				} else if (error.response.data.msg.indexOf("milliseconds ago") >= 0) {
					ElMessage.error("查询数据超时，请缩小查询范围或稍后重试");
				} else {
					ElMessage({
						dangerouslyUseHTMLString: true,
						message: error.response.data.msg,
						type: "error"
					})
					//ElMessage.error(error.response.data.msg);
				}
				return Promise.reject(error);
			} else {
				return Promise.reject(error);
			}
		} else {
		if (error.response && error.name == "AxiosError" && error.response.data instanceof Blob) {
			var reader = new FileReader();
			reader.readAsText(error.response.data, 'utf-8');
			reader.onload = function (e) {
				var result = JSON.parse(reader.result);
				ElMessage.error('导出失败！');
			}
		} else if (!error.response) {
			// 请求被取消时不显示错误提示（axios.isCancel 检查）
			if (!axios.isCancel(error)) {
				ElMessage.error('网络请求失败，请检查网络连接或服务是否启动');
			}
		}
		return Promise.reject(error);
	}
	}
)

// 通用下载方法
export function download(url, params, filename, config) {
	downloadLoadingInstance = ElLoading.service({ text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)", })
	return request.post(url, params, {
		transformRequest: [(params) => { return tansParams(params) }],
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		responseType: 'blob',
		...config
	}).then(async (data) => {
		const isBlob = blobValidate(data)
		if (isBlob) {
			const blob = new Blob([data])
			saveAs(blob, filename)
		} else {
			const resText = await data.text()
			const rspObj = JSON.parse(resText)
			const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
			ElMessage.error(errMsg)
		}
		downloadLoadingInstance.close()
	}).catch((r) => {
		console.error(r)
		ElMessage.error('下载文件出现错误，请联系管理员！')
		downloadLoadingInstance.close()
	})
}

export default request