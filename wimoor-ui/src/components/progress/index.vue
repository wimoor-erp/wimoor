<template>
<el-progress
      :text-inside="true"
      :stroke-width="16"
	  v-show="options.progress>=0"
      :percentage="options.progress"
      status="warning"
    />
 
</template>

<script setup>
		import { ref,reactive,onMounted,toRefs,watch} from 'vue';
		import progressApi from '@/api/sys/tool/progressApi.js';
		let props = defineProps({ options:{key:"",progress:0} });
		const { options} = toRefs(props);
	/* 	const state = reactive({ progress:0});
		const { progress } = toRefs(state); */
		const emit = defineEmits(['change']);
		function loadProgress(){
			if(props.options.key){
				progressApi.loadProgress(props.options.key).then(res=>{
					if(res.data){
						props.options.progress=parseInt(res.data);
					}  
					emit("change",props.options.progress);
					if(props.options.progress!=-1&&props.options.progress!=100){
						setTimeout(function(){ loadProgress() },2000)
					}
				})
			}
			
		}
	 
		function show(){
			setTimeout(function(){ loadProgress(); },1000)
		}
		defineExpose({show})
</script>

<style>
</style>