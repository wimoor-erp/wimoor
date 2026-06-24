<template>
	<div class="main-sty">
		<Header/>
		<Day/>
		<Month/>
	</div>
</template>

<script>
	import { ref ,watch,reactive,onMounted} from 'vue'
	import Header from "./components/header.vue";
	import Day from "./components/day.vue";
	import Month from "./components/month.vue";
	export default{
		name:'Index',
		components:{
			Header,Day,Month
		},
		setup(){
			
			return{
			}
		}
	}
</script>

<style>
	.m-b-8{
		margin-bottom:8px;
	}
	.m-b-24{
		margin-bottom:24px;
	}
</style>