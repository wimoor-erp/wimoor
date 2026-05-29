import {nextTick} from 'vue';
import {formatPercent,formatFloat} from '@/utils/index.js';
import {ElMessage} from 'element-plus';
export function showColumnSet(columnSetRef,ftype){
	columnSetRef.show(ftype);
}	
export	function showAdChart(adchartRef,state){
		var ftype=state.activeName;
		if(state.queryParams.profileid){
			if(ftype=='adcams'){
				state.queryParams.campaignStatus=state.adstate;
				state.queryParams.targetingType=state.adType;
				state.queryParams.campaignName=state.queryParams.search;
			}else if(ftype=='adgroups'){
				state.queryParams.adGroupsName=state.queryParams.search;
				state.queryParams.adGroupsStatus=state.adstate;
			}
			nextTick(()=>{
				//eltabsRef.value.doLayout();
				adchartRef.show(ftype,state.queryParams);
			})
		}
	}
	

export function handleAdvDaysData(row,advData){
		advData.push({
			name:'1天内成交',
			attributedSalesSameSKU:row.attributedSalesSameSKU1d,
			sumUnits:row.sumUnits1d,
			sumSales:row.sumSales1d,
			attributedUnitsOrdered:row.attributedUnitsOrdered1d,
			ACOS:row.ACOS1d,
			ROAS:row.ROAS1d,
			CSRT:row.CSRT1d,
			attributedSalesSameSKU:row.attributedSalesSameSKU1d,
			attributedSales:row.attributedSales1d,
			attributedConversionsSameSKU:row.attributedConversionsSameSKU1d,
			attributedConversions:row.attributedConversions1d,
			status:'none',
		});
		advData.push({
			name:'7天内成交',
			attributedSalesSameSKU:row.attributedSalesSameSKU7d,
			sumUnits:row.sumUnits7d,
			sumSales:row.sumSales7d,
			attributedUnitsOrdered:row.attributedUnitsOrdered7d,
			ACOS:row.ACOS7d,
			ROAS:row.ROAS7d,
			CSRT:row.CSRT7d,
			attributedSalesSameSKU:row.attributedSalesSameSKU7d,
			attributedSales:row.attributedSales7d,
			attributedConversionsSameSKU:row.attributedConversionsSameSKU7d,
			attributedConversions:row.attributedConversions7d,
			status:'none',
		});
		advData.push({
			name:'14天内成交',
			attributedSalesSameSKU:row.attributedSalesSameSKU14d,
			sumUnits:row.sumUnits14d,
			sumSales:row.sumSales14d,
			attributedUnitsOrdered:row.attributedUnitsOrdered14d,
			ACOS:row.ACOS14d,
			ROAS:row.ROAS14d,
			CSRT:row.CSRT14d,
			attributedSalesSameSKU:row.attributedSalesSameSKU14d,
			attributedSales:row.attributedSales14d,
			attributedConversionsSameSKU:row.attributedConversionsSameSKU14d,
			attributedConversions:row.attributedConversions14d,
			status:'none',
		});
		advData.push({
			name:'30天内成交',
			attributedSalesSameSKU:row.attributedSalesSameSKU30d,
			sumUnits:row.sumUnits30d,
			sumSales:row.sumSales30d,
			attributedUnitsOrdered:row.attributedUnitsOrdered30d,
			ACOS:row.ACOS30d,
			ROAS:row.ROAS30d,
			CSRT:row.CSRT30d,
			attributedSalesSameSKU:row.attributedSalesSameSKU30d,
			attributedSales:row.attributedSales30d,
			attributedConversionsSameSKU:row.attributedConversionsSameSKU30d,
			attributedConversions:row.attributedConversions30d,
			status:'none',
		});
		advData.forEach(item=>{
			formatter(item);
		});
		return advData;
	}
	
export	function formatter(row){
		row.CSRT=row.CSRT?formatPercent(row.CSRT)+"%":"";
		row.ROAS=row.ROAS?formatFloat(row.ROAS):"";
		row.ACOS=row.ACOS?formatPercent(row.ACOS)+"%":"";	
		row.CTR=row.CTR?formatPercent(row.CTR)+"%":"";
	}
export	function splitStr(str){
		if(str){
			if (str.indexOf("@") >= 0 || str.indexOf("$") >= 0
									|| str.indexOf("#") >= 0 || str.indexOf("*") >= 0
									|| str.indexOf("/") >= 0 || str.indexOf("!") >= 0
									|| str.indexOf("！") >= 0) {
					 ElMessage.error("无法解析：不得包含特殊字符@丶#丶*丶$丶/等!");
					 return null;
			 }
			var zhengze = /([\n\r])+/g;
			str = str.replace(zhengze, '\n');
			var list = str.split('\n');
		    return list;
		}
	}
	export	async function changeTimes(datepickersRef,val){
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
			datepickersRef.dateValue=array;
			datepickersRef.dateChange(array);
		}
		
	