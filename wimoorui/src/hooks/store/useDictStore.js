// composables/useDictStore.js
import store from '@/store/index';
import { computed } from 'vue'


// 通过计算属性获取整个字典（如果需要）
const dict = computed(() => store.state.dict.dict)

// Getters
const getDict = (key) => {
  return store.getters['dict/getDict'](key)
}

// Actions
const setDict = (key, value) => {
  store.dispatch('dict/setDict', { key, value })
}

const removeDict = (key) => {
  store.dispatch('dict/removeDict', key)
}

const cleanDict = () => {
  store.dispatch('dict/cleanDict')
}

const initDict = () => {
  store.dispatch('dict/initDict')
}

export default {
  dict,
  getDict,
  setDict,
  removeDict,
  cleanDict,
  initDict
}
