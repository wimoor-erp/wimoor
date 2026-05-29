// store/modules/dict.js
const state = {
  dict: []
}

const getters = {
  getDict: (state) => (_key) => {
    if (_key == null || _key == "") {
      return null
    }
    try {
      for (let i = 0; i < state.dict.length; i++) {
        if (state.dict[i].key == _key) {
          return state.dict[i].value
        }
      }
      return null
    } catch (e) {
      return null
    }
  }
}

const mutations = {
  SET_DICT(state, { key, value }) {
    if (key !== null && key !== "") {
      state.dict.push({
        key: key,
        value: value
      })
    }
  },
  REMOVE_DICT(state, key) {
    try {
      for (let i = 0; i < state.dict.length; i++) {
        if (state.dict[i].key == key) {
          state.dict.splice(i, 1)
          break
        }
      }
    } catch (e) {
      console.error('删除字典项失败:', e)
    }
  },
  CLEAN_DICT(state) {
    state.dict = []
  }
}

const actions = {
  // 设置字典
  setDict({ commit }, { key, value }) {
    commit('SET_DICT', { key, value })
  },

  // 删除字典
  removeDict({ commit }, key) {
    commit('REMOVE_DICT', key)
  },

  // 清空字典
  cleanDict({ commit }) {
    commit('CLEAN_DICT')
  },

  // 初始字典（空方法，保留接口）
  initDict() {
    // 初始化字典的逻辑可以在这里实现
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}