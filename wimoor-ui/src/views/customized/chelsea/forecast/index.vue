<template>
	<div class="main-sty forcaseTable">
		<div class="con-header week-range-sty">
			<el-row>
			<el-space>
			  <Group  ref="groups" @change="changeGroup"  defaultValue="onlyEU"/>
				<el-input  v-model="queryParams.search" placeholder="请输入SKU"
				 clearable @input="handleQuery" style="min-width:200px;" >
				   <template #append>
					 <el-button @click="handleQuery">
						 <el-icon style="font-size: 16px;align-itmes:center" >
						 <search />
					  </el-icon>
					 </el-button>
				   </template>
				 </el-input>
                <el-space>
				         <el-date-picker
				           v-model="weekStar"
				           type="week"
						   :clearable="false"
						   @change = "weekChange"
				           format="gggg [年] ww [周] "
				           placeholder="选择一周"
				         />
						 <spam>-</spam>
				         <el-date-picker
				           v-model="weekEnd"
						   :disabled-date="disabledDate"
				           type="week"
						   @change = "weekChange"
						   :clearable="false"
				           format="gggg [年] ww [周]"
				           placeholder="选择一周"
				         />
			 </el-space>
			 <el-button @click="showDialog">导入</el-button>
			 <div style="padding-left:60px">
			 <el-button-group>
			     <el-button  size="small" :disabled="queryparam.currentpage==1"   @click="prePage()" :icon="ArrowLeft">上一页(PageUp)</el-button>
			     <el-button  size="small"  :disabled="queryparam.currentpage>parseInt(tableData.total/queryparam.pagesize)" @click="nextPage()">
			       下一页(PageDown)<el-icon class="el-icon--right"><ArrowRight /></el-icon>
			     </el-button>
			   </el-button-group>
			   </div>
			 </el-space>
			 <div class='rt-btn-group'>
			 <el-button :loading="refreshloading" @click="handleRefresh">刷新上周数据</el-button>
			 </div>
		    </el-row>
	    </div>
		<div class="con-body"  >
			<GlobalTable  ref="globalTable" 
			:tableData="tableData"
			 :span-method="objectSpanMethod"
			 :row-class-name="rowClassName"
			  @loadTable="loadtableData"
			  height="calc(100vh - 170px)"
			  class="forcaseTable"
			   border >
			   <template #field>
			      <el-table-column  label="SKU/名称" width="160" prop="sku" sortable="custom" fixed>
					  <template #default="scope">
						  <div style="padding-left:4px;padding-right:4px">
						  <div class="text-center">
							  <el-image
							   class="product-img"
							   v-if="scope.row.image"
							   :src="scope.row.image"></el-image>
							   <el-image v-else 
							   :src="$require('empty/noimage40.png')"
							  class="product-img"
							   ></el-image>
						  </div>
						  <div style=" line-height:15px ">
							  <div >{{scope.row.name}}</div>
							  <span class="font-extraSmall">{{scope.row.sku}}</span>
						  </div>
						 </div>
					  </template>
				  </el-table-column>
			      <el-table-column prop="refname" label="ref" width="120" fixed>
				     <template #default="scope">
						 <span style="padding-left:4px">{{scope.row.refname}}</span>&nbsp;
						   <el-popover
						     placement="top-start"
						     title="默认到货周数设置"
						     :width="120"
						     trigger="click"
						   >
						   <template #default>
							   <el-input  v-model="scope.row.cycle"></el-input>
							   <el-button type="primary" class="m-t-8">确认</el-button>
							</template>   
						     <template #reference>
						      <el-tag
						      size="small"
						      class="pointer"
						      type="info"
						      v-show="scope.row.refname==='海外仓发货'"
						      >7</el-tag>
						     </template>
						   </el-popover>
					  </template>
				</el-table-column>
			      <el-table-column
				  width="70"
				   v-for="(item,index) in columnWeeks"
				   v-key="item.id"
				   class-name="column-style"
				   prop="ref" label="ref" >
				   <template #header>
					 <p :style="item.value===currentWeek&&'padding-bottom:4px'">{{item.label.day}}</p> 
					 <span :class="item.value===currentWeek&&'current-sty'">{{item.label.week}}周</span> 
				   </template>
				     <template #default="scope">
						  <el-tooltip
						placement="top"
						 :disabled="!scope.row[item.value]?.remark"
						:visible="scope.row[item.value]?.remarkVisible">
						<template #content>
							<spam>{{scope.row[item.value]?.remark}}</spam>
						</template>	
						 <div >
							 <div  v-if="(scope.row.refname==='agl 发货'||scope.row.refname==='近7日销量'||scope.row.refname==='海外仓发货' )  && compareTime(index)">
								 <el-space v-if="scope.row[item.value]?.editloading">
								 <input
								 style="width:40px;"
								 type="number"
								  class="focus-input"
								  :data-row="scope.$index" 
								  :data-col="index" 
								  size="small"
								 
								 v-model="scope.row[item.value].amount"
								 @change="handleSave(scope.row[item.value])"
								 ></input>
								 <el-button  link loading ></el-button>
								 </el-space>
								 <input
								 style="width:50px;"
								 type="number"
								 :data-row="scope.$index"
								 :data-col="index" 
								 class="focus-input"
								 v-else
								  size="small"
								 v-model="scope.row[item.value].amount"
								 @change="handleSave(scope.row[item.value])"
								 ></input>
							 </div>
						
						 <el-tooltip
						  v-else-if="scope.row.refname==='FBA库存'"
						  placement="top"
						  >
							 <template #content>
								<div>{{scope.row[item.value]?.remark}}</div>
							 </template>
							<span>{{scope.row[item.value]?.amount||0}}</span>
						 </el-tooltip>
						<span 
						:class="scope.row[item.value]?.amount<0&&'text-red'"
						v-else
						>{{scope.row[item.value]?.amount||''}} <el-icon v-if="scope.row[item.value]?.islock" class="text-warning" title="已锁定数量,锁定后无法被自动修改"><Lock /></el-icon></span>
						 <span 
						 v-if="scope.row.refname==='agl 发货'||scope.row.refname==='inbound'||scope.row.refname==='海外仓发货'||scope.row.refname==='海外仓库存'"
						 :class="scope.row[item.value]?.remark&&'isshow'"
						 class="icon-td-pos pointer ">
						<el-icon
						title="备注"
						@click="handleEdit(scope.row,item.value)"
						><EditPen /></el-icon>
						</span>
						</div> 
						</el-tooltip>  
						
				     </template>
				   </el-table-column>
				   </template>
			   </GlobalTable>
		</div>
	</div>
  <el-dialog 
  v-model="remarkVis"
  title="备注"
  width="400px"
  :modal="false"
  draggable
  >
  <el-form>
	  <el-form-item v-if="state.editRow.ftype==='alg_inbound'" label="inbound数量">
		<el-input v-model="state.editRow.amount"></el-input>  
	  </el-form-item>
	  <el-form-item v-if="state.editRow.ftype==='wh_inv'" label="海外仓库存">
	  		<el-input v-model="state.editRow.amount"></el-input>  
	  </el-form-item>
	  <el-form-item v-if="state.editRow.ftype==='wh_inv'||state.editRow.ftype==='alg_inbound'" label="锁定入库数量">
	  		  <el-checkbox v-model="state.editRow.editIslock" size="large" />
	  </el-form-item>
	  <el-form-item v-if="state.editRow.ftype==='wh_ship'||state.editRow.ftype==='alg_ship'" label="发货数量">
	  		<el-input v-model="state.editRow.amount"></el-input>  
	  </el-form-item>
	  <el-form-item v-if="state.editRow.ftype==='wh_ship'||state.editRow.ftype==='alg_ship'" label="预计到货周数">
	  		<el-input v-model="state.editRow.arrivalDateDays"></el-input>  
	  </el-form-item>
	  <el-form-item label="备注">
	  		<el-input type="textarea" v-model="state.editRow.editRemark"></el-input>
	  </el-form-item>
  </el-form>
      
	  
	  <template #footer>
		  <el-button type="primary" @click="saveRemark"  :loading="state.editRow.editloading">保存</el-button>
	  </template>
  </el-dialog>
  <el-dialog
     v-model="uploadVisible"
     title="导入"
     width="400px"
   >
   <el-upload
       :drag="true"
       action
  	 :http-request="uploadFiles"
  	 :limit="1"
  	 :before-upload="beforeUpload" 
  	 :show-file-list="true" 
  	 :headers="headers" 
  	 accept=".xls,.xlsx"
     >
       <el-icon class="font-large"><upload-filled /></el-icon>
       <div class="el-upload__text">
        拖拽文件到此处或 <em>点击上传</em>
       </div>
     </el-upload>
   <template #footer>
     <span class="dialog-footer">
  	   <div class="flex-center-between">
  	 <el-button type="success" @click="downExcelTemp"  plain>下载模板</el-button>
  	 <div>
       <el-button @click="uploadVisible = false">取消</el-button>
       <el-button type="primary" :loading="uploadloading" @click.stop="uploadExcel">
         上传文件
       </el-button></div></div>
     </span>
   </template>
    </el-dialog>
</template>
 
<script setup>
	import { ref,reactive,onMounted,toRefs, computed, nextTick, watch,} from 'vue';
	import Group from '@/components/header/group.vue';
	import {Search,EditPen,Lock} from '@element-plus/icons-vue'
	import {useRoute}from"vue-router";
	import {dateFormat} from '@/utils/index.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import productShipApi from "@/views/customized/chelsea/api/ship/productShipApi.js"
	const route = useRoute();
	const globalTable = ref();
	const weekStar = ref();
	const weekEnd = ref();
	const remarks = ref();
	const state = reactive({
		tableData:{
			records:[],
			total:1,
		},
	  uploadVisible:false,
	  editRow:null,
	  isChangeField:false,
      columnWeeks:[],
	  uploadloading:false,
	  refreshloading:false,
	  myfile:null,
	  currentWeek:"2025week7",
	  remarkVis:false,
	  queryparam:{},
	  queryParams:{}
	})
	const {
		tableData,
		columnWeeks,
		currentWeek,
		queryParams,
		uploadloading,
		refreshloading,
		myfile,
		remarkVis,
		queryparam,
		uploadVisible,
	}=toRefs(state)
	
	function getCurrentSunday(today) {
		if(!(today instanceof Date)){
			today=new Date(today);
		}
	  const dayOfWeek = today.getDay();
	  
	  // 如果今天是周日，则返回今天的日期
	  if (dayOfWeek === 0) {
	    return today;
	  }
	  
	  // 计算距离周日还有多少天
	  const daysUntilSunday = 7 - dayOfWeek;
	  
	  // 获取距离今天相应天数的日期
	  const nextSunday = new Date(today.getFullYear(), today.getMonth(), today.getDate() + daysUntilSunday);
	  
	  return nextSunday;
	}
	function uploadFiles(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	function uploadExcel(){
		let FormDatas = new FormData()
		state.uploadloading=true;
		FormDatas.append('file',state.myfile);
		FormDatas.append('groupid',state.queryParams.groupid);
		FormDatas.append('marketplaceid',state.queryParams.marketplaceid);
		productShipApi.uploadFile(FormDatas).then(function(res){
				 ElMessage.success('上传成功');
				 state.uploadloading=false;
				 state.uploadVisible = false;
			}).catch(()=>{
				state.uploadloading = false;
			})
	}
	function showDialog(){
		state.uploadVisible=true;
	}
	function downExcelTemp(){
		productShipApi.downExcelTemp({"ftype":'preSales'});
	}
    function handleSave(item){
		var param=JSON.parse(JSON.stringify(item));
		param.quantity=item.amount;
		param.param=state.queryParams;
		item.editloading=true;
		productShipApi.save(param).then(res=>{
			item.editloading=false;
			ElMessage.success('操作成功');
			item.quantity=param.amount;
			handleResult(res);
		}).catch(e=>{
			item.editloading=false;
		})
	}
	function handleResult(res){
		if(res&&res.data&&res.data.records&&res.data.records.length>0){
							 var item=res.data.records[0];
							 var index=0;
							 const {ref,...rest} = item;
							 for(var i=0;i<=state.tableData.records.length;i++){
							 	var row=state.tableData.records[i];
							 	if(row.sku==item.sku){
							 		index=i;break;
							 	}
							 }
							 for(var i =0;i<ref.length;i++){
							 	//v-model使用可选链（?.）要报错，处理为空的数据
							 	state.columnWeeks.forEach(column=>{
							 		if(!ref[i][column.value]){
							 			ref[i][column.value] = {};
							 		}
							 	})
								var itemrow={ ...rest, ...ref[i], };
							 	state.tableData.records[index+i]=itemrow;
							 }
		}
	}
	function saveRemark(){
		var param=JSON.parse(JSON.stringify(state.editRow));
		param.remark=param.editRemark;
		param.quantity=state.editRow.amount;
		if(param.arrivalDateDays){
			let date=new Date(param.weekDate);
			date.setDate(date.getDate()+parseInt(param.arrivalDateDays)*7)
			param.arrivalDate=date;
		}
		param.param=state.queryParams;
		state.editRow.editloading=true;
		param.islock=param.editIslock
		productShipApi.save(param).then(res=>{
			state.editRow.editloading=false;
			ElMessage.success('操作成功');
			 state.editRow.remark=state.editRow.editRemark;
			 state.editRow.islock=state.editRow.editIslock;
			 if(state.editRow.amount!=state.editRow.quantity||param.arrivalDateDays){
				state.editRow.quantity=state.editRow.amount;
				handleResult(res);
			 }
			state.remarkVis =false;
		}).catch(e=>{
			state.remarkVis =false;
			state.editRow.editloading=false;
		})
	}
	function setDefaultWeek(){
		 const star = new Date();
		 const end = new Date();
		 weekStar.value = getCurrentSunday(star.setDate(star.getDate() - 84));
		 weekEnd.value =  getCurrentSunday(end.setDate(end.getDate() + 84+49));
		 state.queryParams.weekStart=dateFormat(weekStar.value);
		 state.queryParams.weekEnd=dateFormat(weekEnd.value);
		 getColumnWeeks();
	}
	 function disabledDate(time) {
	        // 设置最大可选日期为2023-12-31
		   const end = new Date();
		   var endtime=  getCurrentSunday(end.setDate(end.getDate() + 84*2));
		   return time.getTime() > endtime.getTime();
	}
	function handleRefresh(){
		    state.refreshloading=true;
			productShipApi.refresh(state.queryParams).then(res=>{
			    state.refreshloading=false;
				ElMessage.success('操作成功');
				handleQuery();
			});
	}
	function weekChange(){
		if(weekStar.value>weekEnd.value){
			[weekStar.value,weekEnd.value] = [weekEnd.value,weekStar.value]
		}
		state.queryParams.weekStart=dateFormat(weekStar.value);
		state.queryParams.weekEnd=dateFormat(weekEnd.value);
		getColumnWeeks()
	}
	function getColumnWeeks(){
           state.isChangeField=true;
		   handleQuery();
	}
	function changeGroup(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		handleQuery();
	}
	function handleQuery(){
		if(state.queryParams.marketplaceid){
		   globalTable.value.loadTable({"paramother":state.queryParams});
		}
	}
    function rowClassName(data){
		if(data&&data.row&&data.row.refname=="近7日销量"){
			return "toprow";
		}else{
			return "";
		}
		
	}
	function loadtableData(param){
		state.queryparam=param;
		productShipApi.list(param).then(res=>{
			var arr = [];
			res.data.records.forEach(item=>{
						const {ref,...rest} = item;
						for(var i =0;i<ref.length;i++){
							//v-model使用可选链（?.）要报错，处理为空的数据
							state.columnWeeks.forEach(column=>{
								if(!ref[i][column.value]){
									ref[i][column.value] = {};
								}
							})
							arr.push({
								...rest,
								...ref[i],
							})
						}
				})
			state.tableData.records=arr;
			state.tableData.total=res.data.total;
			if(state.isChangeField){
				nextTick(()=>{
					productShipApi.columnWeeks(state.queryParams).then(res=>{
							 state.columnWeeks=res.data.columnWeeks;
							 state.currentWeek=res.data.nowWeeks;
							 globalTable.value.doLayout();
							 state.isChangeField=false;
							 getScrollLeft();
					})
				})
			}
			 setTimeout(()=>{
				 var nextInput = document.querySelector('.focus-input[data-row="' + 0 + '"][data-col="' + 11 + '"]');
				 if (nextInput) {
				   nextInput.focus();
				 }
			 },1000);
	    });
	}
	function compareTime(i){
		//历史数据不能编辑
		const num = state.columnWeeks.findIndex((item)=>item.value===state.currentWeek);
	    return i>num;
	}
 function getScrollLeft(){
	 const num = state.columnWeeks.findIndex((item)=>item.value===state.currentWeek);
	 const left = num*100;
	 setTimeout(()=>{
		 globalTable.value.setScrollLeft(left);
		 globalTable.value.doLayout();
	 },100)
 }

 watch(route,()=>{
	 getScrollLeft();
})
 const objectSpanMethod = ({
  row,
  column,
  rowIndex,
  columnIndex,
})=>{
	  if (columnIndex < 1) {
	    if (rowIndex % 7 === 0) {
	      return {
	        rowspan: 7,
	        colspan: 1,
	      }
	    } else {
	      return {
	        rowspan: 0,
	        colspan: 0,
	      }
	    }
	  }
}

  function getWeeksDifference(date1, date2) {
      // 将日期转换为毫秒
      const diffInMs = Math.abs(date2 - date1);
      // 将毫秒转换为天数
      const diffInDays = diffInMs / (24 * 60 * 60 * 1000);
      // 计算周数，向下取整
      const weeks = Math.floor(diffInDays / 7);
      return weeks;
  }
   

function handleEdit(row,colunm){
	state.remarkVis =true;
	state.editRow=row[colunm];
	state.editRow.arrivalDateDays=7;
	if(state.editRow.arrivalDate){
		const date1 = new Date(state.editRow.weekDate);
		const date2 = new Date(state.editRow.arrivalDate);
		state.editRow.arrivalDateDays=getWeeksDifference(date1, date2);
	}
	state.editRow.editRemark= row[colunm].remark;
	state.editRow.editIslock=state.editRow.islock;
}
function nextPage(){
	if(state.queryparam.currentpage<=parseInt(state.tableData.total/state.queryparam.pagesize)){
		globalTable.value.handleCurrentChange(state.queryparam.currentpage+1);
	}
}
function prePage(){
	if(state.queryparam.currentpage>=1){
		globalTable.value.handleCurrentChange(state.queryparam.currentpage-1);
	}
}
 
  onMounted(()=>{
	   setDefaultWeek();
	   globalTable.value.changeSize(3);
	   document.addEventListener('keydown', function(event) {
	     if (event.key === 'ArrowRight' || event.key === 'ArrowDown') {
	       event.preventDefault(); // 阻止默认行为，如页面滚动
	       var activeElement = document.activeElement;
	       if (activeElement.classList.contains('focus-input')) {
	         var row = parseInt(activeElement.getAttribute('data-row'));
	         var col = parseInt(activeElement.getAttribute('data-col'));
	         if (event.key === 'ArrowRight' && col < 1000) { // 如果在第一列且按右箭头键，则移动到下一列
	           col++;
	   		var nextInput = document.querySelector('.focus-input[data-row="' + row + '"][data-col="' + col + '"]');
	   		if (nextInput) {
	   		  nextInput.focus();
	   		}
	         } 
	   	   while(event.key === 'ArrowDown' && row < 1000) { // 如果在第一行且按下箭头键，则移动到下一行
	           row++;
	   		var nextInput = document.querySelector('.focus-input[data-row="' + row + '"][data-col="' + col + '"]');
	   		if (nextInput) {
	   		  nextInput.focus();
	   		  break;
	   		}
	         }
	         
	       }
	     } else if (event.key === 'ArrowLeft' || event.key === 'ArrowUp') {
	       event.preventDefault(); // 阻止默认行为，如页面滚动
	       var activeElement = document.activeElement;
	       if (activeElement.classList.contains('focus-input')) {
	         var row = parseInt(activeElement.getAttribute('data-row'));
	         var col = parseInt(activeElement.getAttribute('data-col'));
	         if (event.key === 'ArrowLeft' && col > 0) { // 如果在第二列且按左箭头键，则移动到前一列
	           col--;
	   		var nextInput = document.querySelector('.focus-input[data-row="' + row + '"][data-col="' + col + '"]');
	   		if (nextInput) {
	   		  nextInput.focus();
	   		}
	         } 
	   	  while(event.key === 'ArrowUp' && row > 0) { // 如果在第二行且按上箭头键，则移动到前一行
	           row--;
	   		var nextInput = document.querySelector('.focus-input[data-row="' + row + '"][data-col="' + col + '"]');
	   		if (nextInput) {
	   		  nextInput.focus();
	   		  break;
	   		}
	         }
	        
	       }
	     }else if(event.key === 'PageUp' || event.key === 'PageDown'){
			 if(event.key === 'PageUp'){
				 prePage();
			 }else{
				 nextPage();
			 }
		 }
	   });
  })
</script>


<style scoped>
	.con-body{
		margin-top: 10px;
	}
   .current-sty{
	   color: var(--el-color-white);
	   background-color: var(--el-color-primary);
	   padding:2px 4px;
	   border-radius: 2px;
   }
   .icon-td-pos{
	   position: absolute;
	   top:-4px;
	   right: -4px;
	   width: 24px;
	   height: 24px;
	   opacity: 0;
	   display: flex;
	   justify-content: center;
	   align-items: center;
   }
   .column-style {
	   position: relative;
	   
   }
   .column-style:hover  .icon-td-pos{
	   opacity: 1;
	   transition: opacity 0.5s;
   }
   .isshow{
	   opacity: 1;
	   color: var(--el-color-primary);
   }
</style>
<style>
	.toprow td{
		border-top:1px solid #dedede;
		font-size:11px;
	}
	.forcaseTable table {
		   font-size:11px;
	}
	.forcaseTable .el-pagination{
		padding-left:10px;
		padding-bottom:3px;
	}
	.forcaseTable .el-table th {
		   font-size:10px !important;
		   line-height:16px !important;
		   font-weight: 500;
		   font-color:#dedede;
	}
	.forcaseTable .product-img{
		width:64px;
		height:64px;
	}
	.forcaseTable .el-table--default .cell{
		   padding:0 1px !important;
		   line-height:11px  ;
	}
	.forcaseTable .el-table--default .cell input{
		   border:1px solid #cfcfcf;
		   padding:0px;
		   border-radius: 4px;
		   
	}
	.forcaseTable .el-table--default .cell input:focus{
		   border:1px solid #868686;
	}
	.forcaseTable{
		padding:4PX 0px !important;
		margin:10px !important;
	}
	.week-range-sty{
		padding-top:5px  !important;
		padding-left:10px;
		padding-right:10px;
	}
</style>