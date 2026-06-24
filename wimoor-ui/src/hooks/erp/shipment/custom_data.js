import { listDictsByCode } from "@/api/sys/admin/dict.js";
import shipmentCustomsApi from '@/api/erp/shipv2/shipmentCustomsApi.js'
/**
 * 获取字典数据
 */
export function useCustomsDict(...args) {
  const res = ref({})
  return (() => {
    args.forEach((ftype, index) => {
      res.value[ftype] = []
        shipmentCustomsApi.customsData({"type": ftype}).then(resp => {
          res.value[ftype] = resp.data
        })
    })
    return toRefs(res.value)
  })()
}