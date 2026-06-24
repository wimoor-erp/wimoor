<template>
	<div class="last-space">
	<el-space >
	<div class="date-picker-group" >
		<el-select style="width:80px;" v-model="dateType" @change="dateTypeChange">
			<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
		</el-select>
		<el-date-picker
		        v-if="dateType==='day'"
				class="date-picker-width"
		        v-model="dateValue"
				@change = "changedate"
		        type="daterange"
				:clearable="false"
		        range-separator="至"
		        start-placeholder="开始日期"
		        end-placeholder="结束日期"
		        :shortcuts="shortcuts"
		      />
			  <el-date-picker
			   v-else
			    v-model="week"
				class="date-picker-width"
			    type="week"
			    format="ww [周]"
			    :clearable="false"
			  	@change = "e=>changedate(e,'week')"
			  />
	</div>
	   <el-radio-group v-if="dateType==='day'"  class="el-radio-group-button" v-model="times" @change="changeTimes(times)">
		  <el-radio-button label="昨天" />
		  <el-radio-button label="近7天" />
		  <el-radio-button label="近30天" />
		</el-radio-group>
		</el-space>
		</div>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs,computed,} from 'vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import { useStore } from 'vuex';
	const emits = defineEmits(["setDate","dateTypeSwitch"])
	const store = useStore();
	const state = reactive({
		dateOptions:[
			{label:'按日',value:'day',},
			{label:'按周',value:'week',}
		],
		shortcuts:[
			  {
			    text: '近7天',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * 1)
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
			      return [start, end]
			    },
			  },
			  {
			    text: '近1个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * 1)
			      start.setTime(start.getTime() - 3600 * 1000 * 24 *30)
			      return [start, end]
			    },
			  },
			  {
			    text: '近2个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * 1)
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 60)
			      return [start, end]
			    },
			  },
			  {
			    text: '近3个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * 1)
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
			      return [start, end]
			    },
			  },
			],
	})
	const{
		dateOptions,
		shortcuts,
	}=toRefs(state)
	const dateType = computed(()=>{
		return store.state.dateStore.dateType;
	})
	const dateValue = computed({
		      get(){  
		       return store.state.dateStore.dateValue;
		      }, 
		      set(value) {  
		       store.commit("dateStore/getDate",value);
		      } 
	})
	const week = computed({
		get(){
		 return store.state.dateStore.week;
		}, 
		set(value) {  
		 store.commit("dateStore/getWeek",value);
		} 
	})
	const times = computed({
		get(){
		 return store.state.dateStore.times;
		}, 
		set(value) {  
		 store.commit("dateStore/getTimers",value);
		} 
	})
	
	function changedate(val,dateType,isload,tabName){
		    const dates={};
			if(dateType==='week'){
				dates.start = val.format("yyyy-MM-dd");
				dates.end = new Date(new Date(dates.start).getTime() + 6*24*60*60*1000).format("yyyy-MM-dd")+" 23:59:59";
			}else{
				dates.start = val[0].format("yyyy-MM-dd");
				dates.end = val[1].format("yyyy-MM-dd")+" 23:59:59";
			}
			emits('setDate',dates,dateType,isload,tabName)
	}
	function dateTypeChange(val){
		store.commit("dateStore/increment",val);
		if(val==='week'){
			changedate(week.value,'week')
		}else {
			changedate(dateValue.value)
		}
		emits("dateTypeSwitch");
	}
	
	function getSundayofCurrentWeek(){
	  const currentDate = new Date();  
	  const dayOfWeek = currentDate.getDay(); 
	  let  diff = 0;  
	  if (dayOfWeek !== 0) { 
		diff = dayOfWeek % 7;
	  }  
	  currentDate.setDate(currentDate.getDate() - diff);  
	  return currentDate;
	 
	} 
	function changeTimes(val){
		var end = new Date();
		var start = new Date();
		var beforedays=0;
		end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1));
		var array=[start, end];
		if(val=="近7天"){
			 start.setTime(start.getTime() - 3600 * 1000 * 24 * (7+beforedays))
		}
		if(val=="近30天"){
			 start.setTime(start.getTime() - 3600 * 1000 * 24 * (30+beforedays))
		}
		if(val=="近90天"){
			start.setTime(start.getTime() - 3600 * 1000 * 24 * (90+beforedays))
		}
		if(val=="昨天"){
			start.setTime(start.getTime() - 3600 * 1000 * 24 * (1+beforedays))
		}
		changedate(array);
		store.commit("dateStore/getDate",array);
	}
	watch(()=>store.state.dateStore.isActiveDate,()=>{
			if(dateType.value==='week'){
				changedate(week.value,"week",'',store.state.dateStore.tabName);
			}else{
				changedate(dateValue.value,"day",'',store.state.dateStore.tabName);
			}
	})
	onMounted(()=>{
		if(week.value===''){
			store.commit("dateStore/getWeek",getSundayofCurrentWeek());
		}
		if(dateValue.value.length===0){
			store.commit("dateStore/getDate",state.shortcuts[0].value());
		}
		if(times.value===''){
			store.commit("dateStore/getTimers","近7天");
		}
		if(dateType.value==='week'){
			changedate(week.value,"week","load");
		}else{
			changedate(dateValue.value,"day","load");
		}
	})
</script>

<style >
	.last-space .el-space__item:last-child{
		margin-right: 0!important;
	}
</style>