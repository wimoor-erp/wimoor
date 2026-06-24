/* 复制文本 */
import {ElMessage} from 'element-plus'
export default function(text){
	 var input = document.createElement("textarea");
	 document.body.appendChild(input)
	 input.value = text
	 input.select()
	 document.execCommand('copy')
	 document.body.removeChild(input);
	 ElMessage.success('复制成功！');
}  