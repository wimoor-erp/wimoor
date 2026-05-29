// store/modules/finance.js
import groupApi from '@/api/amazon/group/groupApi.js';
import { ElMessageBox } from "element-plus";
import router from '@/router';

const state = {
    currentTenantId: null,
    currentTenantName: null,
    tenantList: [],
    isnotice: false,
};

const getters = {
    getCurrentTenantName: (state) => state.currentTenantName,
    getCurrentTenantId: (state) => state.currentTenantId,
    getTenantList: (state) => state.tenantList,
    getIsNotice: (state) => state.isnotice,
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
};

const actions = {
    // 设置当前租户ID
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
                const res = await groupApi.getAmazonGroup();
                let list=[]
                res.data.forEach(item => {
                    if(item.isfinance===true){
                        list.push(item);
                    }
                });
                commit('SET_TENANT_LIST',list);
                
                if (list && list.length > 0) {
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
                return state.currentTenantId;
            } catch (error) {
                console.error('获取租户ID失败:', error);
                throw error;
            }
        }
    },

    // 获取租户列表
    getTenantList({ commit, state, dispatch }) {
        return new Promise((resolve, reject) => {
            groupApi.getAmazonGroup().then((res) => {
                let list=[]
                res.data.forEach(item => {
                    if(item.isfinance===true){
                        list.push(item);
                    }
                });
                commit('SET_TENANT_LIST', list);
                commit('SET_IS_NOTICE', false);
                console.log(list);
                if (list && list.length > 0) {
                    const currentTenant = state.tenantList.find(item => item.id === state.currentTenantId);
                    const currentTenantName = currentTenant ? currentTenant.name : state.currentTenantId;
                    
                    if (state.currentTenantId && currentTenantName && state.currentTenantName === currentTenantName) {
                        dispatch('setCurrentTenantId', state.currentTenantId);
                    } else {
                        dispatch('setCurrentTenantId', list[0].id);
                    }
                } else {
                    dispatch('setCurrentTenantId', null);
                }
                resolve({data:list});
            }).catch(error => {
                reject(error);
            });
        });
    },
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
};