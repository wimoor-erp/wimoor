<template>
	<el-dialog
	    v-model="visible"
	    width="60%"
		top="1vh"
		:show-close="false"
	    :before-close="handleClose"
	  >
	    <h1 class="text-center" style="padding-bottom:20px">
	    	科方达承运商询价系统使用协议
	    </h1>
		<div >
			<h4>
				甲方（使用方）：深圳市科方达电商有限公司
			</h4>
			<h4>
				乙方（承运商）：{{company}}
			</h4>
		</div>
	    
	    <div style="padding-top:20px">
	    	鉴于甲方通过科方达系统发布货运需求，乙方作为承运商参与报价，双方就系统使用达成如下协议：
	    </div><br/>
	    <div>
	    <h3>第一条 定义与解释</h3> 
	    <div>系统：指甲方运营的“科方达承运商询价系统” 。</div>
	    <div>用户：指注册并使用系统的乙方及其授权人员。</div>
	    <div>询价单：甲方通过系统发布的货运需求信息。</div>
	    </div><br/>
	    <div>
	    <h3>第二条 用户资格与注册</h3> 
	    <div>乙方需具备合法运输资质，提交真实有效的营业执照、道路运输许可证等文件。</div>
	    <div>乙方负责账户安全管理，不得转借他人使用，否则甲方有权终止服务。</div>
	    </div><br/>
	    <div>
	    <h3>第三条 询价与报价规则</h3> 
	    <div>甲方通过系统发布货物信息（如起止地、重量、体积、时效等），乙方应在指定时间内响应报价。</div>
	    <div>报价应包含运输费用、保险及其他附加费用，且须为最终含税价。</div>
	    <div>乙方报价一经甲方确认即视为合同要约，不得无故撤销。</div>
	    </div><br/>
	    <div>
	    <h3>第四条 履约与违约责任</h3> 
	    <div>乙方中标后须严格按约定完成运输，若出现延误、货损等，需按《合同法》及甲方标准赔偿。</div>
	    <div>甲方有权根据乙方履约情况（如报价准确性、服务质量）调整其信用评级或暂停合作。</div>
	    </div><br/>
	    <div>
	    <h3>第五条 费用与结算</h3> 
	    <div>结算周期：按月结算，乙方需开具合规发票。</div>
	    <div>争议处理：费用异议需在结算单签收后5个工作日内提出。</div>
	    </div><br/>
	    <div>
	    <h3>第六条 数据与保密</h3> 
	    <div>乙方不得泄露甲方货物信息、客户资料等商业秘密，保密义务持续至协议终止后2年。</div>
	    <div>系统数据（如报价记录、运输轨迹）归甲方所有，乙方不得擅自导出或商用。</div>
	    </div><br/>
	    <div>
	    <h3>第七条 系统使用规范</h3> 
	    <div>乙方不得利用系统从事刷单、虚假报价等行为，否则甲方有权追责并终止合作。</div>
	    <div>因乙方操作失误导致的损失由其自行承担。</div>
	    </div><br/>
	    <div>
	    <h3>第八条 其他条款</h3> 
	    <div>不可抗力：因自然灾害、政策调整等导致无法履约的，双方协商解决。</div>
	    <div>争议解决：本协议适用中华人民共和国法律，争议提交甲方所在地法院诉讼解决。</div>
	    </div><br/>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="handleAgree(false)">不同意</el-button>
	        <el-button type="primary" :disabled="timeout" @click="handleAgree(true)">
	          同意{{title}}
	        </el-button>
	      </div>
	    </template>
	  </el-dialog>
	
</template>


<script setup>
import {Search,ArrowDown,Link} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import CopyText from"@/utils/copy_text.js";
	import orderApi from '@/api/quote/orderApi.js';
	import supplierApi from '@/api/quote/supplierApi.js';
	import transchannelApi from '@/api/quote/transchannelApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import {ElMessage,ElMessageBox } from 'element-plus';
	const emit = defineEmits(['change']);
	const globalTableRef=ref();
	const state = reactive({ visible:false,timeout:true,title:"3",isAgree:false ,company:""})
	const{ visible,timeout,title,isAgree,company }=toRefs(state);
	function handleAgree(value){
		state.visible = false;
		state.isAgree=value;
		emit("change",state.isAgree);
	}
	function handleClose(){
		
	}
	function timeOut(t){
		state.timeout=t;
		state.title=t+"秒";
		if(t>=1){
			setTimeout(function(){
				timeOut(t-1);
			},1000);	
		}else{
			state.timeout=false;
			state.title="";
		}
	}
	function show(data){
			timeOut(3);
			state.company=data;
			state.visible=true;
	}
 defineExpose({show});
</script>

<style>
</style>