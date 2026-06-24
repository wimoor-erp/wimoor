// composables/useFinanceStore.js
import store from '@/store/index';
import { computed } from 'vue'
import { getCurrentPeriod as apiGetCurrentPeriod } from '@/api/finance/periods.js'

// 请求去重变量（提前声明，避免时序问题）
let _pendingPeriodRequest = null;
let _periodFetchedInSession = false;

// Getters
const currentTenantName = computed(() => store.getters['finance/getCurrentTenantName'])
const currentTenantId = computed(() => store.getters['finance/getCurrentTenantId'])
const tenantList = computed(() => store.getters['finance/getTenantList'])
const isNotice = computed(() => store.getters['finance/getIsNotice'])
const currentPeriod = computed(() => store.getters['finance/getCurrentPeriod'])

// Actions
const setCurrentTenantId = (tenantId) => {
    _periodFetchedInSession = false; // 切换租户时重置，确保重新获取期间
    return store.dispatch('finance/setCurrentTenantId', tenantId)
}

const getCurrentTenantId = () => {
    return store.dispatch('finance/getCurrentTenantId')
}

// 设置当前会计期间
const setCurrentPeriod = (period) => {
    return store.dispatch('finance/setCurrentPeriod', period)
}

const getTenantList = () => {
    return store.dispatch('finance/getTenantList')
}

// 获取当前会计期间
const getCurrentPeriod = async () => {
    const tenantId = store.state.finance.currentTenantId;
    if (!tenantId) {
        return store.state.finance.currentPeriod;
    }
    // 本次会话已获取过且有缓存，直接返回
    if (_periodFetchedInSession) {
        return store.state.finance.currentPeriod;
    }
    // 如果已有相同的请求在进行中，复用该请求
    if (_pendingPeriodRequest) {
        return _pendingPeriodRequest;
    }
    _pendingPeriodRequest = (async () => {
        try {
            const response = await apiGetCurrentPeriod(tenantId);
            if (response.code === 200 && response.data) {
                store.dispatch('finance/setCurrentPeriod', response.data);
                _periodFetchedInSession = true;
                return response.data;
            }
        } catch (error) {
            console.error('获取当前会计期间失败:', error);
        }
        return store.state.finance.currentPeriod;
    })();
    try {
        return await _pendingPeriodRequest;
    } finally {
        _pendingPeriodRequest = null;
    }
}

// 刷新当前会计期间
const refreshCurrentPeriod = () => {
    return store.dispatch('finance/refreshCurrentPeriod')
}

export default {
    // States (通过 getters)
    currentTenantName,
    currentTenantId,
    tenantList,
    isNotice,
    currentPeriod,
    
    // Actions
    setCurrentTenantId,
    getCurrentTenantId,
    setCurrentPeriod,
    getTenantList,
    getCurrentPeriod,
    refreshCurrentPeriod
}