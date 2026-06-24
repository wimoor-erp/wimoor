// store/modules/user.js
import userApi from '@/api/sys/admin/userApi.js';

const state = {
    userInfo: null,
    image: '',
    name: '',
    companyname: '',
    bindlist: [],
    isLoaded: false
};

const getters = {
    getUserInfo: (state) => state.userInfo,
    getUserImage: (state) => state.image,
    getUserName: (state) => state.name,
    getCompanyName: (state) => state.companyname,
    getBindList: (state) => state.bindlist,
    isUserLoaded: (state) => state.isLoaded,
    getAvatarName: (state) => state.name ? state.name.substring(0, 1) : ''
};

const mutations = {
    SET_USER_INFO(state, userInfo) {
        state.userInfo = userInfo;
    },
    SET_USER_IMAGE(state, image) {
        state.image = image;
    },
    SET_USER_NAME(state, name) {
        state.name = name;
    },
    SET_COMPANY_NAME(state, companyname) {
        state.companyname = companyname;
    },
    SET_BIND_LIST(state, bindlist) {
        state.bindlist = bindlist;
    },
    SET_IS_LOADED(state, isLoaded) {
        state.isLoaded = isLoaded;
    },
    CLEAR_USER_INFO(state) {
        state.userInfo = null;
        state.image = '';
        state.name = '';
        state.companyname = '';
        state.bindlist = [];
        state.isLoaded = false;
    }
};

const actions = {
    async loadUserInfo({ commit, dispatch }) {
        const response = await userApi.getInfo();
        if (response && response.data) {
            commit('SET_USER_INFO', response.data);
            if (response.data.image) {
                commit('SET_USER_IMAGE', response.data.image);
            }
            if (response.data.name) {
                commit('SET_USER_NAME', response.data.name);
            }
            if (response.data.company) {
                commit('SET_COMPANY_NAME', response.data.company);
            }
            commit('SET_IS_LOADED', true);
            await dispatch('loadBindList', response.data);
            return response.data;
        }
    },

    async loadBindList({ commit }, currentUser = null) {
        try {
            const response = await userApi.findbindlist();
            if (response.data) {
                const bindlist = response.data.map(item => {
                    return {
                        ...item,
                        userid: item.id,
                        isactive: currentUser && currentUser.id === item.id,
                        companyname: item.userinfo?.companyname || '',
                        name: item.userinfo?.name || ''
                    };
                });
                commit('SET_BIND_LIST', bindlist);
                return bindlist;
            }
        } catch (error) {
            console.error('加载绑定列表失败:', error);
            throw error;
        }
    },

    updateUserImage({ commit }, image) {
        commit('SET_USER_IMAGE', image);
        if (this.state.user.userInfo) {
            commit('SET_USER_INFO', { ...this.state.user.userInfo, image });
        }
    },

    updateUserName({ commit }, name) {
        commit('SET_USER_NAME', name);
        if (this.state.user.userInfo) {
            commit('SET_USER_INFO', { ...this.state.user.userInfo, name });
        }
    },

    clearUserInfo({ commit }) {
        commit('CLEAR_USER_INFO');
    }
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
};