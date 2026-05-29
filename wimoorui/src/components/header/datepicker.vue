<template>
	<el-date-picker
	        v-if="datetype===''||datetype===null||datetype===undefined||datetype==='day'"
	        v-model="dateValue"
			@change = "dateChange"
	        type="daterange"
			:clearable="false"
	        range-separator="至"
	        start-placeholder="开始日期"
	        end-placeholder="结束日期"
	        :shortcuts="shortcuts"
			:editable="true"
	      />
		  <el-date-picker
		    v-else-if="datetype==='week'"
		    v-model="week"
		    type="week"
		    format="ww [周]"
		    :clearable="false"
			@change = "e=>dateChange(e,'week')"
		  />
</template>

<script>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
import { zIndexContextKey } from 'element-plus';
	import { ref,reactive,onMounted,watch ,defineExpose} from 'vue'
	import { useStore } from 'vuex';
	const store = useStore();
	export default{
		name:"datepick",
		components:{MenuUnfold,Search,ArrowDown,SettingTwo,Help,Copy,MoreOne,},
		emits:["changedate"],
		 props:{
			  datetype: {  
			       type: String,  
			       default: ''  
			     } ,
			days:{
				type: Number,
				default: ''  
			},	
			shortIndex:{
				type: String,
				default: ''  
			},	
			longtime:{
				type: String,
				default: ''  
			},	 
		 },
		setup(props,context){
			let beforedays=0;
			let week = ref("")
			if(props.days){
				 beforedays=props.days-1;
			}else{
				 beforedays=-1;
			}
			let shortcuts = [
			  {
			    text: '近7天',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * (7+beforedays))
			      return [start, end]
			    },
			  },
			  {
			    text: '近1个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
			      start.setTime(start.getTime() - 3600 * 1000 * 24 *(30+beforedays))
			      return [start, end]
			    },
			  },
			  {
			    text: '近2个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * (60+beforedays))
			      return [start, end]
			    },
			  },
			  {
			    text: '近3个月',
			    value: () => {
			      const end = new Date()
			      const start = new Date()
				  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
			      start.setTime(start.getTime() - 3600 * 1000 * 24 * (90+beforedays))
			      return [start, end]
			    },
			  },
			];
			if(props.longtime){
				shortcuts = [
				  {
				    text: '近1个月',
				    value: () => {
				      const end = new Date()
				      const start = new Date()
					  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
				      start.setTime(start.getTime() - 3600 * 1000 * 24 * (30+beforedays))
				      return [start, end]
				    },
				  },
				  {
				    text: '近3个月',
				    value: () => {
				      const end = new Date()
				      const start = new Date()
					  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
				      start.setTime(start.getTime() - 3600 * 1000 * 24 *(90+beforedays))
				      return [start, end]
				    },
				  },
				  {
				    text: '近6个月',
				    value: () => {
				      const end = new Date()
				      const start = new Date()
					  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
				      start.setTime(start.getTime() - 3600 * 1000 * 24 * (180+beforedays))
				      return [start, end]
				    },
				  },
				  {
				    text: '近1年',
				    value: () => {
				      const end = new Date()
				      const start = new Date()
					  end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1))
				      start.setTime(start.getTime() - 3600 * 1000 * 24 * (365+beforedays))
				      return [start, end]
				    },
				  },
				];
			}
			let datas={}
			let dateValue=ref()
			if(props.datetype==='week'){
				getSundayofCurrentWeek();
			}
			onMounted(()=>{
				if(props.shortIndex){
					 dateValue.value = shortcuts[props.shortIndex].value();
				}else{
				     dateValue.value = shortcuts[0].value();
				}
					dateChange(dateValue.value,"load");
			})
		 
			function dateChange(val,type){
				if(type==='week'){
					var value={start:val}
					value.end = new Date(new Date(value.start).getTime() + 6*24*60*60*1000)
				}else{
				var value={start:val[0],end:val[1]};
				if(val[0].$d){
					value.start=val[0].$d;
				}
				if(val[1].$d){
					value.end=val[1].$d;
				}	
				}
				datas.start=value.start.format("yyyy-MM-dd");
				datas.end=value.end.format("yyyy-MM-dd")+" 23:59:59";
				context.emit("changedate",datas,value,type);
			}
			function getValue(){
				var mydata={start:"",end:""};
				mydata.start=dateValue.value[0].format("yyyy-MM-dd");
				mydata.end=dateValue.value[1].format("yyyy-MM-dd")+" 23:59:59";
				return mydata;
			}
			function setValue(value){
				if(value){
					dateValue.value=value;
				}
				dateChange(dateValue.value);
			}
	        function reset(index){
				if(props.shortIndex){
					 dateValue.value = shortcuts[props.shortIndex].value();
				}else{
					if(index){
						dateValue.value = shortcuts[index].value();
					}else{
						 dateValue.value = shortcuts[0].value();
					}
				}
			}
			function setBlank(){
				 dateValue.value = "";
			}
			function getSundayofCurrentWeek(){  
			  const currentDate = new Date();  
			  const dayOfWeek = currentDate.getDay(); 
			  let  diff = 0;  
			  if (dayOfWeek !== 0) { 
				diff = dayOfWeek % 7;
			  }  
			  currentDate.setDate(currentDate.getDate() - diff);  
			  week.value = currentDate;
			  dateChange(currentDate,'week');
			} 
			defineExpose({
				reset,
			})
			return{
				  dateChange,dateValue,shortcuts,reset,setBlank,getValue,setValue,week,getSundayofCurrentWeek,
			}
		}
	}
</script>

<style>
</style>
