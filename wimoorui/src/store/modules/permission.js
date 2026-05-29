const state = {
  permission: new Set()
}

const mutations = {
  SET_PERMISSION: (state, data) => {
    state.permission=data;
  },
  CLEAR_PERMISSION: (state) => {
    state.permission.clear()
  }
}

const actions = {
  setPermission({ commit }, data) {
    commit('SET_PERMISSION', data)
  },
  clearPermission({ commit }) {
    commit('CLEAR_PERMISSION')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}