
<template>
  <div class="main-sty">
     <div class="demo-block">
      <div class="source">
		  <el-space>
		 <el-select @change="yearsChange" v-model="generateYear" placeholder="年份">
		 	<el-option v-for="item in years" :label="item" :value="item">{{item}}</el-option>
		 </el-select>
		  <el-button @click="generateYearData" v-loading="generateYearLoading">生成年度日历</el-button>
		  </el-space>
		  <el-row :gutter="24">
        <el-col :span="8" class="el-calendar"  v-for="(mothdata,index) in allMonthDays">
          <div class="el-calendar__header">
            <div class="el-calendar__title">{{queryParams.year}}年{{index+1}}月</div>
          </div>
          <div class="el-calendar__body">
            <table class="el-calendar-table" cellspacing="0" cellpadding="0" >
              <thead>
                <th >日</th>
                <th >一</th>
                <th>二</th>
                <th >三</th>
                <th >四</th>
                <th>五</th>
                <th >六</th>
              </thead>
                <tbody>
                <tr
                  v-for="item in mothdata"
                  :key="item.id"
                >
                    <td
                    class="current"
                    v-for="(col, colIdx) in item"
                    :key="colIdx"
                    >
                      <div @click="selectDay(col)"
					   :class="[col.curDate==selectId?'el-calendar-day-active':'el-calendar-day',
					   col.isGray==0?(col.type==1||col.type==2?'el-holiday':'el-work'):(col.type==1||col.type==2?'el-holiday-re':'el-work-re')
					   ]" 
					   >
                        <div class="font-base">{{col.day}}</div>
                        <span :title="col.showName" class="font-extraSmall text-omit-1">{{col.showName}}</span>
						<p >休息</p>
                      </div>
                    </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-col>
		</el-row>
      </div>
    </div>
  </div>
</template>
 <script setup>
	 import { onMounted, reactive, ref, toRefs, watch } from 'vue';
     import localholidayApi from "@/api/sys/tool/localholidayApi.js";
	 const state = reactive({
		  queryParams: {
			id: null,
			type: null,
			year: new Date().getFullYear(),
			month: new Date().getMonth()+1,
		  },
		  generateYearLoading:false,
		  changeDateTypeLoading:false,
		  generateYear: new Date().getFullYear(),
		  showSearch: true,
		  years:[],
		  selectId: initSearchDate(),
		  allMonthDays:[],
	 });
	 const { queryParams,generateYear,generateYearLoading,
	 changeDateTypeLoading,
	 allMonthDays,
	 showSearch,selectId ,years} = toRefs(state);
	 onMounted(()=>{
		 initDate();
	 })
	 
    function initSearchDate() {
      var nowDate = new Date();
      var date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        day: nowDate.getDate()
      }
      return date.year + '-' + (date.month >= 10 ? date.month : '0' + date.month) + '-' + (date.day >= 10 ? date.day : '0' + date.day);
    }
	function generateYearData(){
		state.generateYearLoading=true;
		   localholidayApi.generateYearData(state.queryParams).then(res => {
			   state.generateYearLoading=false;
			   initDate();
		   });
	}
 async function changeDateType(id,type,month){
		   state.changeDateTypeLoading=true;
		await localholidayApi.changeDateType({"id":id,"type":type}).then(res => {
			state.changeDateTypeLoading=false;
			state.queryParams.month = month;
		   });
		  state.allMonthDays[month-1] = await getHdayList(state.queryParams);
	}
async function initDate(){
	state.allMonthDays =[];
      state.holidayList= [];
	  var nowDate = new Date();
	  state.years=[];
	  for(var i=nowDate.getFullYear()-1;i<nowDate.getFullYear()+3;i++){
		  state.years.push(i);
	  }
	  for(let month = 0; month < 12; month++){
		  state.queryParams.month =  month + 1;
		  const holidayList = await getHdayList(state.queryParams);
		  state.allMonthDays.push(holidayList);
	  }
    }
 
 async function getHdayList(queryParams){
	  let holidayChunks = [];
	await localholidayApi.listHolid(queryParams).then(res => {
	   let list = res.data;
	   for (let row = 0; row < 6; row++) {
	     holidayChunks.push(list.splice(0, 7));
	   }
	 });
	 return holidayChunks;
 }
 
   function selectDay(col){
	  if(col.type===0){
		changeDateType(col.id,2,col.month); 
	  }else{
		changeDateType(col.id,0,col.month);
	  }
	
    }
 
  function yearsChange(e){
	state.queryParams.year = e
	initDate()
}


    
	
</script>
<style  scoped>
	.el-holiday{
		color:#fff;
		font-weight:600;
		background-color: #2864c5;
	}
	.el-holiday:hover{
		background-color: #245db8;
	}
	.el-holiday span{
		color:rgba(255,255,255,0.5);
		font-weight: 400;
	}
	.el-holiday p{
	font-size: 12px;
	border:1px solid rgba(255,255,255,0.5);
	font-weight: 400;
	padding:0px 2px;
	border-radius: 2px;
	color: rgba(255,255,255,0.5);
	display: inline-block;
	}
	.el-work{
		color:#333;
		font-weight:600;
	}
	.el-work span{
		color:#999;
		font-weight:400;
	}
	.el-work p{
		display: none;
	}
	.el-holiday-re{
		color:#ff7315;
		opacity:0;
		background-color: #f5f5f5;
		font-weight: 400;
	}
	.el-work-re{
		color:#333;
		opacity: 0;
		background-color: #f5f5f5;
		font-weight: 400;
	}
.app-main{
  background-color: #f6f6f6;
}
.dark .app-main{
   background-color: #090909;
 }
  
.demo-block {
  background-color: #fff;
}
.dark  .demo-block {
   background-color: #000;
 
 }
.el-calendar {
  background-color: #fff;
}
.dark  .el-calendar {
   background-color: #000;
 }
.el-calendar__header {
  display: flex;
  justify-content: space-between;
  padding-left:0;
}
 .el-calendar-table td{
	 border-bottom:1px solid #e9e9e9;
	 border-right:1px solid #e9e9e9;
	 
 }
 .el-calendar-table tr:first-child td{
	 border-top:1px solid #e9e9e9;
 }
 .el-calendar-table tr td:first-child{
	 border-left:1px solid #e9e9e9;
 }
.el-calendar__title {
  color: #333;
  align-self: center;
  font-weight: 700;
}
 
.el-button-group {
  display: inline-block;
  vertical-align: middle;
}
 
.el-calendar__body {
	padding:0;
}
 
table {
  display: table;
  border-collapse: separate;
  box-sizing: border-box;
  text-indent: initial;
  border-spacing: 1px;
  border-color: gray;
}
 
.el-calendar-table {
  table-layout: fixed;
  width: 100%;
  tr {
    display: table-row;
    vertical-align: inherit;
    border-color: inherit;
  }
}
 
.current{
  vertical-align: top;
  transition: background-color .2s ease;
}
 
.el-calendar-day{
  box-sizing: border-box;
  padding:8px;
  height: 72px;
}
 
.el-calendar-day-active{
  box-sizing: border-box;
  padding: 8px;
  background-color: #ff7315;
  color:#fff;
  height: 72px;
}

 .el-calendar-day-active span{
	color:rgba(255,255,255,0.7);
 }
.el-calendar-day-gray{
  box-sizing: border-box;
  padding:8px;

}
 .current{
	 cursor: pointer;
 }
tbody {
  display: table-row-group;
  vertical-align: middle;
  border-color: inherit;
}
</style>
 