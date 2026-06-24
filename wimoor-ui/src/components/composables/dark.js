import { ref, computed, watchEffect } from 'vue'

const STORAGE_KEY = 'vueuse-color-scheme'

function getSystemDark() {
  return window.matchMedia('(prefers-color-scheme: dark)').matches
}

function applyClass(dark) {
  const el = document.documentElement
  if (dark) {
    el.classList.add('dark')
  } else {
    el.classList.remove('dark')
  }
}

const stored = localStorage.getItem(STORAGE_KEY)
const mode = ref(stored || 'auto') // 'auto' | 'dark' | 'light'

const isDark = computed({
  get() {
    if (mode.value === 'auto') return getSystemDark()
    return mode.value === 'dark'
  },
  set(val) {
    mode.value = val ? 'dark' : 'light'
  }
})

watchEffect(() => {
  applyClass(isDark.value)
})

// listen to system preference changes
window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', () => {
  if (mode.value === 'auto') {
    applyClass(isDark.value)
  }
})

function useToggle(refVal) {
  return () => {
    refVal.value = !refVal.value
    return refVal.value
  }
}

export { isDark }
export const toggleDark = useToggle(isDark)
export { useToggle }
