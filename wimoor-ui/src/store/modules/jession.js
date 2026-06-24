const state = {
  jsessiontime: null
}

const mutations = {
  SET_SESSION: (state, data) => {
    state.jsessiontime=data;
  },
}

const actions = {
  setJessionTime({ commit }, data) {
    commit('SET_SESSION', data)
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}