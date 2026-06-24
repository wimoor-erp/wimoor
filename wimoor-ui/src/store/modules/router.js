const state = {
  router: []
}

const mutations = {
  SET_ROUTER: (state, data) => {
    state.router=data;
  },
  CLEAR_ROUTER: (state) => {
    state.router.splice(0)
  }
}

const actions = {
  setRouter({ commit }, data) {
    commit('SET_ROUTER', data)
  },
  clearRouter({ commit }) {
    commit('CLEAR_ROUTER')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}