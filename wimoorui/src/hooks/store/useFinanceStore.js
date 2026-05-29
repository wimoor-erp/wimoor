// composables/useFinanceStore.js
import store from '@/store/index';
import { computed } from 'vue'
    // Getters
    const currentTenantName = computed(() => store.getters['finance/getCurrentTenantName'])
    const currentTenantId = computed(() => store.getters['finance/getCurrentTenantId'])
    const tenantList = computed(() => store.getters['finance/getTenantList'])
    const isNotice = computed(() => store.getters['finance/getIsNotice'])
    
    // Actions
    const setCurrentTenantId = (tenantId) => {
        return store.dispatch('finance/setCurrentTenantId', tenantId)
    }
    
    const getCurrentTenantId = () => {
        return store.dispatch('finance/getCurrentTenantId')
    }
    
    const getTenantList = () => {
        return store.dispatch('finance/getTenantList')
    }
    
export default   {
        // States (通过 getters)
        currentTenantName,
        currentTenantId,
        tenantList,
        isNotice,
        
        // Actions
        setCurrentTenantId,
        getCurrentTenantId,
        getTenantList
    }
 