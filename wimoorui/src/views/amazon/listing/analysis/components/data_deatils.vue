<template>
     <div class="gird-line-right">
		<el-card >
			<el-row gutter="16" >
				<el-col :span="16">
				<div class="p-b-h">
				<div> 
				<el-image v-if="infoMap.image" :src="infoMap.image"   class="img-size" ></el-image>
				<el-image v-else :src="$require('empty/noimage40.png')"  class="img-size" ></el-image>
				</div>
				<div>
					<div class="name">
						{{infoMap.name}}
					</div>
					<div class="sku">
						{{infoMap.sku}}
					</div>
					<el-space class="font-extraSmall m-t-8" >
						<span>ASIN:{{infoMap.asin}}</span>
						<el-divider direction="vertical"></el-divider>
						<span>首次上架日期:{{infoMap.opendate}}</span>
					</el-space>
					<div class="m-t-8" v-if="infoMap.anysisremark">
						<p>备注:{{infoMap.anysisremark}}</p>
					</div>
				</div>
				</div>
				</el-col>
				<el-col :span="8" class="text-right">
					<el-space :size="16">
					<el-link @click="editRemarks" title="编辑备注" class="flex-center" :underline="false" >
						 <el-icon class="font-medium"><Edit /> </el-icon>&nbsp;备注
					</el-link>
					<el-link title="跳转亚马逊" class="flex-center"  target="_blank" :href="infoMap.link" :underline="false" >
						 <el-icon class="font-medium"><Link/></el-icon>&nbsp;跳转
					</el-link>
					</el-space>
				</el-col>
				</el-row>
		</el-card>
		<el-tabs
		    v-model="activeName"
		    type="card"
		    class="card-top-tabs m-t-16"
		    @tab-change="loadChart"
		  >
		   
		    
			
			 <el-tab-pane   name="sales" label="销量" >
			  </el-tab-pane>
			  <el-tab-pane   name="hisrank" label="历史排名" >
			   </el-tab-pane>
			 <el-tab-pane v-for="item in queryList" :label="item.name" :name="item.id" :class="item.name=='none'?'nopadding':''" :disabled ="item.name=='none'?true:false">
				 <template #label>
				        <div @click.stop="showQueryDialog"  class="custom-tabs-label pointer font-black " style="padding-left: 10px; padding-right: 10px;margin-left:-10px;margin-right:-10px" v-if="item.name=='none'">
				          <el-icon><Plus /></el-icon>
				        </div>
						<div class="custom-tabs-label" v-else>
						 {{item.name}}
						</div>
				      </template>
			 </el-tab-pane>
			 
		  </el-tabs>
		   
		<el-card class="p-a-card">
			    <template #header>
			      <div class="flex-center-between ">
					  <el-space>
			       <el-radio-group v-model="times" @change="changeTimes">
			             <el-radio-button label="近7天" />
			             <el-radio-button label="近30天" />
			             <el-radio-button label="近90天" />
			           </el-radio-group>
					   <Datepicker ref="datepickersRef" :days="1"  @changedate="changedate" />
					  </el-space>
					  <!--   <div>
					      <el-checkbox v-model="checked3" label="同期同比" />
					      <el-checkbox v-model="checked4" label="同期环比" />
					    </div> -->
			      </div>
			    </template>
				<div class="p-a-body">
					 <!-- <div v-if="activeName=='datas'" class="p-a-left" >
						  <el-card shadow="hover" class="m-b-8" v-for="item in summaryData" :class="{'active':item.active}">
							  <div class="flex-center-between">
								  <div>
									  <div class="title">{{item.label}}</div>
									  <div class="data">{{item.num}}</div>
								  </div>
								  <div class="text-right">
									  <el-checkbox style="height:14px;margin-bottom:12px;"  @change="addLines(item)" v-model="item.active" />
									  <div class="font-extraSmall" >环比前{{item.day}}天</div>
									  <div class="font-12" :class="'text-'+color(item.rate)">{{rate(item.rate)}}</div>
								  </div>
							  </div>
						  </el-card>
					  </div> -->
					  <div   class="p-a-right">
						<!-- <el-radio-group v-model="radio2">
						      <el-radio-button label="复合图" />
						      <el-radio-button label="单独图" />
						 </el-radio-group> -->
						 <div id="anaysis-mycharts" class="my-chart">
						 </div>
					  </div>
				</div>
				
		</el-card>
		<el-card style="margin-top:10px;">
		
			<el-scrollbar style="width:calc(100vw - 350px);" always>
		<table class="sd-table">
			<tr>
				<td  width="80px;">项目名称</td>
				<td width="80px;">汇总</td>
				<td v-for="label in labels" width="60px;">{{label}}</td>
			</tr>
		 
			<tr v-for="(legend,index) in legends">
				<td  width="80px;">{{legend}}</td>
				<td   width="80px;" >
					{{summary[index]}}
				</td>
				<td width="60px;" v-if="series && series[index] && series[index].data" v-for="item in series[index].data">
					{{item}}
				</td>
			</tr>
		</table>
		</el-scrollbar>
	
		</el-card>
	 </div>
	 <el-dialog v-model="remarkVisable" title="备注">
		 <el-input v-model="infoMap.remark2" type="textarea"  :rows="3"></el-input>
		 <template #footer>
			 <el-button @click="remarkVisable=false">取消</el-button>
			 <el-button type="primary" @click.stop="updateAnyRemark">确认</el-button>
		 </template>
	 </el-dialog>
	 <Dialog ref="dialogRef" @change="loadQueryList"></Dialog>
</template>

<script setup>
	import {Edit,Link,Plus} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import '@/assets/css/packbox_table.css';
	import {ref,reactive,toRefs,onMounted}from"vue"
	import * as echarts from 'echarts';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import Dialog from './dialog.vue';
	import productAnysApi from '@/api/amazon/product/productAnysApi.js';
	import queryFieldApi from '@/api/sys/tool/queryFieldApi.js';
	import {dateMonthFormat,getCurrencyMark,outputmoney,formatFloat} from '@/utils/index.js';
	const datepickersRef=ref();
	const dialogRef=ref();
	let state=reactive({
		remarkVisable:false,
		activeName:"sales",
		times:"近7天",radio2:'复合图',
		summaryData:[
			{label:'销量',num:"$3668.54",active:true,day:7,rate:0.6,value:'uns'},
			{label:'销售订单',num:"-",active:false,day:7,rate:"",value:'ods'},
			{label:'商品总销售额',num:"-",active:false,day:7,rate:"",value:'pts'},
			{label:'商品排名',num:1222,active:false,day:7,rate:"",value:'rnk'},
			{label:'访问人数',num:4564,active:false,day:7,rate:12.0,value:'ses'},
			{label:'页面浏览数量',num:5000,active:false,day:7,rate:-25.0,value:'pgv'},
			{label:'购物车比例',num:10000,active:false,day:7,rate:0.2,value:'bbp'},
			{label:'销售转化率',num:0.37,active:false,day:7,rate:0.2,value:'osp'},
			{label:'广告点击量',num:"-",active:false,day:7,rate:"",value:'cks'},
			{label:'广告展示量',num:"-",active:false,day:7,rate:"",value:'imp'},
			{label:'广告点击率',num:"-",active:false,day:7,rate:"",value:'ctr'},
			{label:'广告花费',num:"-",active:false,day:7,rate:"",value:'spd'},
			{label:'广告点击花费',num:"-",active:false,day:7,rate:"",value:'cpc'},
			{label:'Acos',num:"-",active:false,day:7,rate:"",value:'acos'},
			{label:'AcoAs',num:"-",active:false,day:7,rate:"",value:'acoas'},
			{label:'广告转化率',num:"-",active:false,day:7,rate:"",value:'cr'},
			{label:'广告销售额',num:"-",active:false,day:7,rate:"",value:'tos'},
			{label:'广告销量',num:"-",active:false,day:7,rate:"",value:'aus'},
			{label:'广告销量占比',num:"-",active:false,day:7,rate:"",value:'aups'},
		],
		infoMap:{remark2:'',},
		isload:true,
		queryParams:{},
		labels:[],
		series:[],
		summary:[],
		legends:[],
		selectlabels:'uns,',
		queryList:[{name:"none"}],
	})
	let {activeName,times,radio2,summary,summaryData,remarkVisable,infoMap,isload,queryParams,
	labels,series,legends,selectlabels,queryList,} =toRefs(state);
	
	function showQueryDialog(){
		dialogRef.value.show();
	}
	function editRemarks(){
		state.remarkVisable = true;
		state.infoMap.remark2=state.infoMap.anysisremark;
	}
	function color(val){
		if(val>0){
			return "green"
		}else if(val<0){
			return "red"
		}else{
			return "info"
		}
	}
	function rate(val){
		if(val==''||val==undefined){
			return "-"
		}else{
			if(val>0){
				return "+"+val+'%'
			}else{
				return val+'%'
			}
		}
	}
	function addLines(row){
		if(row.active==true){
		    state.selectlabels=(state.selectlabels+row.value)+",";
		}else{
			var oldstr=row.value+",";
			state.selectlabels=state.selectlabels.replace(oldstr,"");
			state.selectlabels=state.selectlabels.replace(row.value,"");
		}
		loadChart();
	}
	var myChart =null;
	function lineChart(ftype) {
		if(myChart!=null){
			myChart.clear()
		}else{
			 myChart =echarts.init(document.getElementById('anaysis-mycharts'));
		}
		// 指定图表的配置项和数据
		var option = {
		tooltip : {
			trigger : 'axis',
			formatter : function(params) {
				var showHtm = "";
				for (var i = 0; i < params.length; i++) {
					var date = params[i].name;
					var name = params[i].seriesName;
					var value = params[i].value;
					if (name == '购物车比例' || name == '销售转化率' || name == '广告点击率'
							|| name == 'Acos' || name == "AcoAs" || name == "广告转化率"
								|| name == "广告销量占比") {
						showHtm += name + ": " + value + "%" + '<br>';
					} else {
						showHtm += name + ": " + value + '<br>';
					}
				}
				showHtm = date + '<br>' + showHtm;
				return showHtm;
			},
			axisPointer : { // 坐标轴指示器，坐标轴触发有效
				type : 'line', // 默认为直线，可选为：'line' | 'shadow'
				lineStyle : { // 直线指示器样式设置
					color : '#ccc',
					width : 1,
					type : 'solid'
				},
			}
		},
		legend : {
			data : state.legends,
			y : 'top',
			x : 'center',
		},
		color : [ '#ffa400', '#75D6AA', '#EB6A79', '#7AA5DA', '#d69bf2',
				'#59f3e3', '#8875ff', '#e0e0e5', '#ff8559', '#00FF7F', '#00FF7F' ],
		grid : {
			x : 50,
			x2 : 50,
			y : 50,
			y2 :50,
			borderWidth : 0,
		},
		calculable : false,
		xAxis : [ {
			axisLabel : {
				show : true,
				textStyle : {
					color : '#999'
				}
			},
			splitLine : {
				lineStyle : {
					color : '#f1f1f1',
					width : 1,// 这里是为了突出显示加上的
				}
			},
			axisTick : {
				show : false,
				lineStyle : {
					color : '#f1f1f1'
				}
			},
			axisLine : {
				lineStyle : {
					color : '#f1f1f1',
					width : 1,// 这里是为了突出显示加上的
				}
			},
			type : 'category',
			boundaryGap : true,
			data : state.labels
		} ],
		yAxis : [ {
			axisLabel : {
				show : true,
				textStyle : {
					color : '#999'
				},
			},
			splitLine : {
				lineStyle : {
					color : '#f1f1f1',
					width : 1,// 这里是为了突出显示加上的
				}
			},
			axisLine : {
				lineStyle : {
					color : '#f1f1f1',
					width : 1,// 这里是为了突出显示加上的
				}
			},
		}, {
			axisLabel : {
				show : true,
				textStyle : {
					color : '#999'
				},
			},
			splitLine : {
				show:false,
			},
			axisLine : {
				lineStyle : {
					color : '#f1f1f1',
					width : 1,// 这里是为了突出显示加上的
				}
			},
		}, ],
		series : state.series
	};
		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
		window.addEventListener('resize',()=>{
						   myChart.resize();
		})
 	}
	
	function updateAnyRemark(){
		productAnysApi.updateAnyRemark({"pid":state.infoMap.id,"remark":state.infoMap.remark2}).then((res)=>{
			if(res.data){
				ElMessage.success( "修改成功！");
				state.remarkVisable=false;
				state.infoMap.anysisremark=state.infoMap.remark2;
			}
		});
		
	}
	//日期改变
	function changedate(dates){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(state.isload==false){
			loadChart();
		}
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
		datepickersRef.value.dateValue=array;
		datepickersRef.value.dateChange(array);
	}
	function loadChart(){
		var ftype="";
		if(state.activeName=="hisrank"||state.activeName=="sales"){
			ftype=state.activeName;
		}else{
			state.queryList.forEach(item=>{
				if(state.activeName==item.id){
					ftype=item.queryfield;
				}
			})
		}
		if(ftype!="none"){
			setTimeout(function(){
				productAnysApi.getChartData({"sku":state.infoMap.sku,"marketplaceid":state.infoMap.marketplaceid,"groupid":state.infoMap.groupid,
				"ftype":ftype,"fromDate":state.queryParams.fromDate,"endDate":state.queryParams.endDate}).then((res)=>{
					if (res.data && res.data.length > 0) {
						var data=res.data;
						state.labels = data[0].labels;
						state.series = [];
						state.legends = [];
						var hasrightline=false;
						for (var i = 0; i < data.length; i++) {
							var name = data[i].name;
							if (name == '购物车比例' || name == '销售转化率' || name == '广告点击率' 
									|| name == 'Acos' || name == "AcoAs" || name == "广告转化率"
										|| name == "广告销量占比") {
								hasrightline=true;break;
							} 
						}
						
						for (var i = 0; i < data.length; i++) {
							state.legends.push(data[i].name);
							var datas = {};
							state.summary[i]=0;
							data[i].data.forEach(item=>{
								if(item){
								   state.summary[i]=state.summary[i]+parseFloat(item);
								}
							})
							datas.name = data[i].name;
							datas.type = "line";
							if (datas.name == '购物车比例' || datas.name == '销售转化率' || datas.name == '广告点击率' 
									|| datas.name == 'Acos' || datas.name == "AcoAs" || datas.name == "广告转化率"
										|| datas.name == "广告销量占比") {
								state.summary[i]=formatFloat(state.summary[i]/data[i].data.length)+" (avg)";		
								datas.yAxisIndex = 1;
							} else if(hasrightline==true){
								datas.type = "bar";
								datas.barGap = "0%";
								datas.boundaryGap = "0%";
								datas.barMaxWidth = 32, datas.itemStyle = {
									normal : {
										barBorderRadius : [ 4, 4, 0, 0 ]
									}
								}
							}
							if(hasrightline==false){
								datas.symbolSize = 0, datas.itemStyle = {
										normal : {
											lineStyle : {
												width : 2
											// 0.1的线条是非常细的了
											}
										}
									};
							}
							datas.smooth = 0.5; 
							datas.symbol = 'emptycircle';// 拐点样式
							datas.data = data[i].data;
							datas.label={
	 								show:true,
	 							};
	 						datas.showAllSymbol=false;
							state.series.push(datas);
						}
						lineChart();
					}
					if(state.isload==true){
						state.isload=false;
					}
				});
			},500);
			 
		}else{
			showQueryDialog();
		}
	}
	
	function show(row){
		if(row.id){
			productAnysApi.productdetail({"pid":row.id}).then((res)=>{
				state.infoMap=res.data;
				state.infoMap.link="https://"+row.point_name+"/dp/"+res.data.asin;
				loadChart();
			});
		}
		loadQueryList();
	}
	function loadQueryList(data){
		//加载指标分组记录
		if(data){
			state.queryList=JSON.parse(JSON.stringify(data));
			state.queryList.push({name:"none","queryfield":"none","id":"none"});
		}else{
			queryFieldApi.getMyVersionFieldByUser({"queryname":"analysistarget"}).then((res)=>{
				state.queryList=res.data;
				state.queryList.push({name:"none","queryfield":"none","id":"none"});
			});
		}
		
	}
	function handleQuery(){
		
	}
	defineExpose({show})
</script>

<style>
	.my-chart{
		 height:300px;
		 width: 100%;
		 margin-top:16px;
	}
	.m-b-8{margin-bottom:8px;}
	.card-top-tabs .el-tabs__header{
		margin:0;
		margin-bottom: 1px;
		border-bottom:0px;
	}
	.nopadding{
		padding:0px;
	}
	.card-top-tabs .el-tabs__header .el-tabs__item.is-active{
		border-bottom: 0px;
		background-color: var(--el-color-primary-light-9);
	}
	.card-top-tabs .el-tabs--card>.el-tabs__header{
		border-bottom:0px;
	}
	.card-top-tabs .el-tabs__item{
		background-color: #fff;
	}
	.dark .card-top-tabs .el-tabs__item{
		background-color: #272727;
	}
.p-b-h{
	display: flex;
}
.p-b-h .sku{
	margin-top:8px;
	color: var(--el-color-blue);
}
.p-a-left {
	margin-top:16px;
	margin-bottom:16px;
	margin-right:16px;
	width: 240px;
}
.p-a-left .title{
	color: var(--el-text-color-secondary);
}
.p-a-left .data{
	margin-top:16px;
	font-weight: 700;
	font-size: 18px;
}
.p-a-left .text-green{
	color:var(--el-color-success)
}
.p-a-left .text-red{
	color:var(--el-color-danger)
}
.p-a-left .active{
	border-color:var(--el-color-primary-light-3)
}
.p-b-h .img-size{
	height:80px;
	width: 80px;
	margin-right:16px;
}
.m-t-16{
	margin-top: 16px;
}
.m-t-24{
	margin-top:24px;
}
.p-a-card .el-card__body{
	padding-top: 0;
	padding-bottom: 0;
}
.p-a-left .el-card__body{
	padding-top:8px;
	padding-bottom: 8px;
}
.p-a-body{
	display: flex;
}
.p-a-right{
	/* border-left:1px solid var(--el-border-color-base); */
	padding:16px;
	padding-right:0px;
	flex:1;
}
</style>
