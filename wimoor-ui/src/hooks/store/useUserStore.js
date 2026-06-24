// hooks/store/useUserStore.js
import store from '@/store/index';
import { computed } from 'vue';
import userApi from '@/api/sys/admin/userApi.js';
import limitApi from '@/api/sys/admin/limitApi.js';
import summaryChartApi from '@/api/amazon/order/summaryChartApi.js';
import { ElMessageBox, ElMessage } from 'element-plus';
import router from '@/router';
import request from "@/utils/request.js";
const userInfo = computed(() => store.getters['user/getUserInfo']);
const image = computed(() => store.getters['user/getUserImage']);
const name = computed(() => store.getters['user/getUserName']);
const companyname = computed(() => store.getters['user/getCompanyName']);
const bindlist = computed(() => store.getters['user/getBindList']);
const isLoaded = computed(() => store.getters['user/isUserLoaded']);
const avatarName = computed(() => store.getters['user/getAvatarName']);

let loadingPromise = null;

async function loadUserInfo() {
    if (isLoaded.value) {
        return userInfo.value;
    }
    if (loadingPromise) {
        return loadingPromise;
    }
    loadingPromise = (async () => {
        try {
            const response = await userApi.getInfo();
            if (response && response.data) {
                store.commit('user/SET_USER_INFO', response.data);
                if (response.data.image) {
                    store.commit('user/SET_USER_IMAGE', response.data.image);
                }
                if (response.data.name) {
                    store.commit('user/SET_USER_NAME', response.data.name);
                }
                if (response.data.company) {
                    store.commit('user/SET_COMPANY_NAME', response.data.company);
                }
                store.commit('user/SET_IS_LOADED', true);
                try {
                    await store.dispatch('user/loadBindList', response.data);
                } catch (e) {
                    console.error('加载绑定列表失败:', e);
                }
                return response.data;
            }
        } catch (error) {
            console.error('加载用户信息失败:', error);
            throw error;
        } finally {
            loadingPromise = null;
        }
    })();
    return loadingPromise;
}

const loadBindList = (currentUser = null) => {
    return store.dispatch('user/loadBindList', currentUser);
};

const updateUserImage = (image) => {
    return store.dispatch('user/updateUserImage', image);
};

const updateUserName = (name) => {
    return store.dispatch('user/updateUserName', name);
};

const clearUserInfo = () => {
    return store.dispatch('user/clearUserInfo');
};

// ==================== 套餐限制检查 ====================

let isChecking = false;
let cachedOrderTotal = null;
let cachedEffectiveTime = null;

// 核心状态：套餐是否超限。一旦为 true，拦截器同步阻断所有请求，无法绕过。
let packageLimitExceeded = false;

/**
 * 同步检查套餐是否超限（拦截器使用，零开销）
 */
function isPackageLimitExceeded() {
    return packageLimitExceeded;
}

/**
 * 是否正在检查中（拦截器用来跳过内部 API 调用，避免死循环）
 */
function isCurrentlyCheckingPackageLimit() {
    return isChecking;
}

/**
 * 获取订单使用总量（带缓存，异常时默认返回0）
 */
async function getOrderTotal(effectiveTime) {
    if (cachedOrderTotal !== null && cachedEffectiveTime === effectiveTime) {
        return cachedOrderTotal;
    }
    try {
        const startDate = effectiveTime || (() => {
            const d = new Date();
            d.setFullYear(d.getFullYear() - 1);
            return d.getTime();
        })();
        const orderRes = await summaryChartApi.getMyOrderTotal({ startdate: startDate });
        const total = orderRes?.data?.ordersum ? parseInt(orderRes.data.ordersum) : 0;
        cachedOrderTotal = total;
        cachedEffectiveTime = effectiveTime;
        return total;
    } catch (e) {
        console.error('获取订单总数失败，默认为0:', e);
        cachedOrderTotal = 0;
        cachedEffectiveTime = effectiveTime;
        return 0;
    }
}

/**
 * 清除订单缓存（套餐变更/续费后调用）
 */
function clearOrderTotalCache() {
    cachedOrderTotal = null;
    cachedEffectiveTime = null;
    packageLimitExceeded = false;
    lastCheckTime = 0;
}

const logout = () => {
	const logintype = localStorage.getItem("logintype")
	console.log("logout");
	clearOrderTotalCache();
	request.get("/admin/api/v1/auth/apilogout").then(res => {
		if ("api" == logintype || "feishu" == logintype) {
			if (res.data == "success") {
				ElMessage({
					type: "success",
					message: "登出成功!"
				})
			} else {
				ElMessage({
					type: "error",
					message: "登出失败!"
				})
			}
		}
	}).catch(() => {
		ElMessage({
			type: "warning",
			message: "登出请求异常，已强制退出"
		})
	}).finally(() => {
		localStorage.removeItem("jsessionid")
		localStorage.removeItem("logintype")
		store.dispatch("routerStore/clearRouter")
		store.dispatch("permissionStore/clearPermission")
		store.dispatch("jessionStore/setJessionTime", null)
		clearOrderTotalCache();
		if (logintype == "api" || logintype == "feishu") {
			router.push({ path: "/login" })
		} else {
			const authserver = localStorage.getItem("authserver")
			if (authserver) {
				location = authserver + "/logout"
			} else {
				router.push({ path: "/ssologin" })
			}
		}
	})
}

/**
 * 显示不可关闭的额度用完弹窗（带 MutationObserver 防删除）
 */
function showProtectedDialog(messageText) {
    const boxOptions = {
        confirmButtonText: '去升级',
        showCancelButton: false,
        showClose: false,
        type: 'warning',
        closeOnClickModal: false,
        closeOnPressEscape: false,
        closeOnHashChange: false,
    };

    const showDialog = () => {
        let observer = null;
        const promise = ElMessageBox.confirm(messageText, '订单额度已用完', boxOptions);

        promise.then(() => {
            if (observer) observer.disconnect();
            packageLimitExceeded = false;
            logout();
        }).catch(() => {
            if (observer) observer.disconnect();
            setTimeout(() => showDialog(), 100);
        });

        setTimeout(() => {
            observer = new MutationObserver(() => {
                const msgBox = document.querySelector('.el-overlay');
                if (!msgBox) {
                    observer.disconnect();
                    observer = null;
                    setTimeout(() => showDialog(), 100);
                }
            });
            observer.observe(document.body, { childList: true, subtree: true });
        }, 200);
    };

    showDialog();
}

/**
 * 检查用户套餐限制，返回状态：'ok' | 'expired' | 'exceeded'
 * 拦截器会 await 这个方法，根据返回值决定是否阻断请求
 */
async function checkPackageLimit() {
    if (isChecking) return;
    isChecking = true;
    try {
        if (!store.getters['user/isUserLoaded']) {
            await loadUserInfo();
        }

        const limitRes = await limitApi.getMyManagerLimitmeal();
        const limitData = limitRes.data;
        if (!limitData) return 'ok';

        const now = Date.now();
        const { expirationTime, maxOrderCount, effectiveTime } = limitData;

        // 套餐到期：需要确认但不阻断，用户仍可继续使用
        if (expirationTime && now > expirationTime) {
            await ElMessageBox.alert(
                '您的套餐已到期，请及时升级以继续使用完整功能。',
                '套餐到期提醒',
                {
                    confirmButtonText: '我知道了',
                    type: 'warning',
                    closeOnClickModal: false,
                }
            ).catch(() => {});
        }

        // 订单使用量超限：阻断所有后续请求
        if (maxOrderCount && maxOrderCount < 9999999) {
            const orderTotal = await getOrderTotal(effectiveTime);
            if (orderTotal >= maxOrderCount) {
                packageLimitExceeded = true;
                const messageText = `您的订单额度已用完（已使用 ${orderTotal} / 总额度 ${maxOrderCount}），请升级套餐以继续使用。`;
                showProtectedDialog(messageText);
                return 'exceeded';
            }
        }

        return 'ok';
    } catch (e) {
        console.error('检查套餐限制失败:', e);
        return 'ok';
    } finally {
        isChecking = false;
    }
}

let lastCheckTime = 0;
const CHECK_INTERVAL = 30 * 60 * 1000;

/**
 * 带节流的套餐检查（返回状态，拦截器 await 使用）
 */
async function throttledCheckPackageLimit() {
    if (packageLimitExceeded) return 'exceeded';
    const now = Date.now();
    if (now - lastCheckTime > CHECK_INTERVAL) {
        lastCheckTime = now;
        return await checkPackageLimit();
    }
    return 'ok';
}

export default {
    userInfo,
    image,
    name,
    companyname,
    bindlist,
    isLoaded,
    avatarName,
    loadUserInfo,
    loadBindList,
    updateUserImage,
    updateUserName,
    clearUserInfo,
    checkPackageLimit,
    clearOrderTotalCache,
    throttledCheckPackageLimit,
    isPackageLimitExceeded,
    isCurrentlyCheckingPackageLimit,
    logout
};