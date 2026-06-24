// store/modules/finance.js
import {listGroups} from '@/api/finance/periods.js';
import { ElMessageBox } from "element-plus";
import router from '@/router';
import { getCurrentPeriod } from '@/api/finance/periods.js';

//console.log('fin.js模块加载成功');
//console.log('getCurrentPeriod API:', getCurrentPeriod);

// 从本地存储加载当前会计期间
const loadFromStorage = () => {
    try {
        const savedPeriod = localStorage.getItem('finance_currentPeriod');
        if (savedPeriod) {
            return JSON.parse(savedPeriod);
        }
    } catch (error) {
        console.error('从本地存储加载会计期间失败:', error);
    }
    return null;
};

const state = {
    currentTenantId: null,
    currentTenantName: null,
    tenantList: [],
    isnotice: false,
    currentPeriod: loadFromStorage(),
};

const getters = {
    getCurrentTenantName: (state) => state.currentTenantName,
    getCurrentTenantId: (state) => state.currentTenantId,
    getTenantList: (state) => state.tenantList,
    getIsNotice: (state) => state.isnotice,
    getCurrentPeriod: (state) => state.currentPeriod,
};

const mutations = {
    SET_CURRENT_TENANT_ID(state, tenantId) {
        state.currentTenantId = tenantId;
    },
    SET_CURRENT_TENANT_NAME(state, tenantName) {
        state.currentTenantName = tenantName;
    },
    SET_TENANT_LIST(state, list) {
        state.tenantList = list;
    },
    SET_IS_NOTICE(state, status) {
        state.isnotice = status;
    },
    SET_CURRENT_PERIOD(state, period) {
        state.currentPeriod = period;
    },
};

const actions = {
    // 设置当前租户ID（不再自动获取期间，由调用方按需通过 useFinanceStore.getCurrentPeriod 获取）
    setCurrentTenantId({ commit, state }, tenantId) {
        commit('SET_CURRENT_TENANT_ID', tenantId);
        if (tenantId && state.tenantList && state.tenantList.length > 0) {
            const tenant = state.tenantList.find(item => item.id === tenantId);
            const tenantName = tenant ? (tenant.company?tenant.company:tenant.name) : tenantId;
            commit('SET_CURRENT_TENANT_NAME', tenantName);
        }
    },

    // 获取当前租户ID
    async getCurrentTenantId({ commit, state, dispatch }) {
        if (state.currentTenantId) {
            commit('SET_IS_NOTICE', false);
            return state.currentTenantId;
        } else {
            try {
                //console.log('开始获取当前租户ID');
                const res = await listGroups();
                //console.log('获取租户列表结果:', res);
                let list=[]
                res.data.forEach(item => {
                    if(item.isfinance===true){
                        list.push(item);
                    }
                });
                //console.log('过滤后的租户列表:', list);
                commit('SET_TENANT_LIST',list);
                
                if (list && list.length > 0) {
                    //console.log('设置第一个租户为当前租户:', list[0].id);
                    await dispatch('setCurrentTenantId', list[0].id);
                }
                
                if (!list || list.length === 0) {
                    if (state.isnotice === false) {
                        commit('SET_IS_NOTICE', true);
                        ElMessageBox.confirm(
                            '您还没有创建店铺账套，请先创建店铺账套！',
                            '安全提示',
                            {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }
                        ).then(() => {
                            commit('SET_IS_NOTICE', false);
                            router.push({
                                path: '/amazon/storeAuth',
                                query: {
                                    title: "店铺管理",
                                    path: '/amazon/storeAuth',
                                }
                            });
                        }).catch(() => {
                            commit('SET_IS_NOTICE', false);
                        });
                    } else {
                        commit('SET_IS_NOTICE', false);
                    }
                }
                //console.log('返回当前租户ID:', state.currentTenantId);
                return state.currentTenantId;
            } catch (error) {
                console.error('获取租户ID失败:', error);
                throw error;
            }
        }
    },

    // 获取租户列表
    async getTenantList({ commit, state, dispatch }) {
        try {
            const res = await listGroups();
            let list=[]
            res.data.forEach(item => {
                if(item.isfinance===true){
                    list.push(item);
                }
            });
            commit('SET_TENANT_LIST', list);
            commit('SET_IS_NOTICE', false);
            if (list && list.length > 0) {
                const currentTenant = list.find(item => item.id === state.currentTenantId);
                let tenantId;
                if (currentTenant) {
                    tenantId = currentTenant.id;
                } else {
                    tenantId = list[0].id;
                }
                
                //console.log('选择的租户ID:', tenantId);
                await dispatch('setCurrentTenantId', tenantId);
            } else {
                dispatch('setCurrentTenantId', null);
            }
            return {code: res.code, msg: res.msg, data: list};
        } catch (error) {
            console.error('获取租户列表失败:', error);
            throw error;
        }
    },
    
    // 设置当前会计期间
    setCurrentPeriod({ commit }, period) {
        commit('SET_CURRENT_PERIOD', period);
        // 保存到本地存储
        if (period) {
            localStorage.setItem('finance_currentPeriod', JSON.stringify(period));
        }
    },
    
    // 刷新当前会计期间
    async refreshCurrentPeriod({ commit, state }) {
        if (state.currentTenantId) {
            try {
                //console.log('开始刷新当前会计期间，租户ID:', state.currentTenantId);
                const periodRes = await getCurrentPeriod(state.currentTenantId);
                //console.log('刷新当前会计期间结果:', periodRes);
                if (periodRes.code === 200 && periodRes.data) {
                    //console.log('更新当前会计期间:', periodRes.data);
                    commit('SET_CURRENT_PERIOD', periodRes.data);
                    return periodRes.data;
                } else {
                    //console.log('刷新当前会计期间失败，返回结果:', periodRes);
                    return null;
                }
            } catch (error) {
                console.error('刷新当前会计期间失败:', error);
                console.error('错误详情:', error.response);
                return null;
            }
        } else {
            //console.log('租户ID为空，无法刷新当前会计期间');
            return null;
        }
    }
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
};