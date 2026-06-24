import { listDictsByCode } from "@/api/sys/admin/dict.js";
import useDictStore from "@/hooks/store/useDictStore.js"
/**
 * 获取字典数据
 */
export function useDict(...args) {
  const res = ref({})
  return (() => {
    args.forEach((dictType, index) => {
      res.value[dictType] = []
      const dicts = useDictStore.getDict(dictType)
      if (dicts) {
        res.value[dictType] = dicts
      } else {
        listDictsByCode(dictType).then(resp => {
          res.value[dictType] = resp.data.map(p => ({ label: p.name, value: p.value, elTagType: p.listClass, elTagClass: p.cssClass }))
          useDictStore.setDict(dictType, res.value[dictType])
        })
      }
    })
    return toRefs(res.value)
  })()
}