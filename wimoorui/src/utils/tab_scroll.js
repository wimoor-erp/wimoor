/* 滚动tab切换 */
import { ref,reactive,onMounted,onUnmounted} from 'vue'
export default function(obj,className){
	let active = ref("1")
	let arrDom = ref([])
	arrDom.value = document.getElementsByClassName(className)
		for(let i = 0;i<arrDom.value.length;i++){
			if(arrDom.value[arrDom.value.length-1].offsetTop-obj.scrollTop>80){
			if(arrDom.value[i].offsetTop-obj.scrollTop<=80&&arrDom.value[i+1].offsetTop-obj.scrollTop>80){
				active.value = i+1+""
			}
			}else{
				active.value = arrDom.value.length+''
			}
		}
	return active.value
}