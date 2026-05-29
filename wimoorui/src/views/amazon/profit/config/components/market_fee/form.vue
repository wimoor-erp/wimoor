<template>
	<el-row v-if="country=='IN'"> 
	    <el-col :span="12">
		   <el-form-item label="亚马逊收费GST税率" >
		     <template #label>
				 <div class="text-right">
				  <span class="font-small">亚马逊收费GST税率</span>	 
				  <p>FBA GST Taxes</p>
				 </div>
			 </template>
			 <el-input @input="marketData.fbaTaxes=CheckInputFloat(marketData.fbaTaxes)" 
			 v-model="marketData.fbaTaxes" > <template #append>% </template></el-input>
		</el-form-item>  
		</el-col>
		</el-row>
		<el-row v-if="country=='CA'">
			<el-col :span="12">
		<el-form-item label="FBA GST/HST 税率" >
		     <template #label>
				 <div class="text-right">
				  <span class="font-small">FBA GST/HST 税率</span>	 
				  <p>FBA GST/HST Taxes</p>
				 </div>
			 </template>
			 <el-input @input="marketData.fbaTaxes=CheckInputFloat(marketData.fbaTaxes)" 
			 v-model="marketData.fbaTaxes"> <template #append>% </template></el-input>
		</el-form-item>  
		</el-col>
		</el-row>
	<el-form-item v-if="country=='EU'" label="亚马逊配送方案" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">亚马逊配送方案</span>	 
			  <p>Amazon distribution scheme</p>
			 </div>
		 </template>
		 <el-select v-model="marketData.fenpeiType">
			 <el-option  label="Cross-border fulfilment(EFN)" value="EFN"></el-option>
			 <el-option  label="Pan EU" value="PAN_EU"></el-option>
		 </el-select>
	</el-form-item>  
	<el-form-item v-if="country=='EU'"  label="亚马逊仓库站点" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">亚马逊仓库站点</span>	 
			  <p>Amazon warehouse site</p>
			 </div>
		 </template>
		 <el-select v-if="marketData.fenpeiType=='EFN'" v-model="marketData.warehousesite">
			<el-option  label="France" value="fr"></el-option>
			<el-option  label="Italy" value="it"></el-option>
			<el-option  label="Spain" value="es"></el-option>
			<el-option  label="Germany" value="de"></el-option>
			<el-option  label="England" value="uk"></el-option>
			<el-option  label="Netherland" value="nl"></el-option>
			<el-option  label="Sweden" value="se"></el-option>
			<el-option  label="Poland" value="pl"></el-option>
		 </el-select>
		 
			 <div v-else> 
				 <el-checkbox :checked="true"  label="France" value="fr" disabled="disabled"></el-checkbox>
				 <el-checkbox :checked="true"  label="Italy" value="it" disabled="disabled"></el-checkbox>
				 <el-checkbox :checked="true"  label="Spain" value="es" disabled="disabled"></el-checkbox>
				 <el-checkbox :checked="true"  label="Germany" value="de" disabled="disabled"></el-checkbox>
				 <el-checkbox :checked="true"  label="England" value="uk" disabled="disabled"></el-checkbox>
				 <el-checkbox :checked="true"  label="Netherland" value="nl" disabled="disabled"></el-checkbox>
				 <el-checkbox :checked="true"  label="Sweden" value="se" disabled="disabled"></el-checkbox>
				 <el-checkbox :checked="true"  label="Poland" value="pl" disabled="disabled"></el-checkbox>
			     <el-checkbox v-model="marketData.hasAddedSite" label="Poland, Czech Republic" size="large" />
			 </div>
	</el-form-item>  
	<el-form-item label="亚马逊仓储费" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">亚马逊仓储费</span>	 
			  <p>Inventory Storage Fee</p>
			 </div>
		 </template>
		 <div class="flex-center" v-if="country=='US'">
			 <el-space>
			  <span >库存时间段</span>
			  <el-select v-model="marketData.fbaMonth">
				  <el-option v-for="item in fbaMonthsOptions" :label="item.label" :value="item.value"></el-option>
			  </el-select>
			 </el-space>
			 <el-divider direction="vertical"></el-divider>
			 <el-space>
			  <span>库存周期</span>
			  <el-input  @input="marketData.amonth=CheckInputInt(marketData.amonth)" 
			  v-model="marketData.amonth"> <template #append>月</template></el-input>
			 </el-space>
		 </div>
		 <div class="flex-center" v-else-if="country=='EU'">
		 			 <el-space>
		 			  <span >DE/FR/IT/ES/BE 每月每立方米</span>
		 			  <el-select v-model="marketData.storagefee">
		 			  	  <el-option v-for="item in storageFee "  :label="item" :value="item"></el-option>
		 			  </el-select>
					  {{currencyicon}}
		 			 </el-space>
		 			 <el-divider direction="vertical"></el-divider>
		 			 <el-space>
		 			  <span>库存周期</span>
		 			  <el-input v-model="marketData.amonth"> <template #append>月</template></el-input>
		 			 </el-space>
		 </div>
		 <div class="flex-center" v-else>
		 			 <el-space>
		 			  <span >每月每立方英尺</span>
		 			  <el-select v-model="marketData.storagefee">
		 				  <el-option v-for="item in storageFee "  :label="item" :value="item"></el-option>
		 			  </el-select>
					    {{currencyicon}}
		 			 </el-space>
		 			 <el-divider direction="vertical"></el-divider>
		 			 <el-space>
		 			  <span>库存周期</span>
		 			  <el-input v-model="marketData.amonth"> <template #append>月</template></el-input>
		 			 </el-space>
		 </div>
	</el-form-item> 		
					
	<el-form-item label="是否计算库存配置费"  v-if="country=='US'||country=='JP'">
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">是否计算库存配置费</span>	 
			  <p>FBA Inventory Placement Service Fee</p>
			 </div>
		 </template>
		<el-select v-model="marketData.invplacefee">
			<el-option v-for="item in inplacefeeOptions "  :label="item.name" :value="item.id"></el-option>
		</el-select>
	</el-form-item>  
	
	<el-form-item v-if="country=='US'" label="亚马逊手动处理费" >
	     <template #label>  
			 <div class="text-right">
			  <span class="font-small">亚马逊手动处理费</span>	 
			  <p>FBA Manual Processing Fee</p>
			 </div>
		 </template>
		 <el-select v-model="marketData.manualProcessing">
		 			 <el-option v-for="item in manualProcessingFeeOptions" :label="item.manualprocessingfee" :value="item.manualprocessingfee"></el-option>
		 </el-select>
		美元
	</el-form-item>  
	
	<el-form-item label="是否计算亚马逊标签服务费" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">是否计算亚马逊标签服务费</span>	 
			  <p>FBA Label Service Fee</p>
			 </div>
		 </template>
		 <el-radio-group v-model="marketData.labelService">
		   <el-radio :label="true" >是</el-radio>
		   <el-radio :label="false" >否</el-radio>
		 </el-radio-group>
	</el-form-item> 	
	
	<el-form-item label="自有包装发货" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">自有包装发货</span>	 
			  <p>SIPP(Ships In Product Packaging)</p>
			 </div>
		 </template>
		 <el-radio-group v-model="marketData.sipp">
		   <el-radio :label="true" >是</el-radio>
		   <el-radio :label="false" >否</el-radio>
		 </el-radio-group>
	</el-form-item> 
						
	<el-row>
		<el-col :span="12">
	<el-form-item v-if="shipmentstyle=='dim_weight'||shipmentstyle=='volume'" label="材积重量计算常量" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">材积重量计算常量</span>	 
			  <p>Dimensional Weight</p>
			 </div>
		 </template>
		 <el-input 
		 @input="marketData.constantd=CheckInputFloat(marketData.constantd)"  
		 v-model="marketData.constantd"> <template #prepend>长(cm) * 宽(cm) * 高(cm) / </template></el-input>
	</el-form-item>		
					
	<el-form-item label="运费" v-if="shipmentstyle!='manually'" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">根据重量或材积重计算运费</span>	 
			  <p>Shipment by Weight or Dimensional Weight</p>
			 </div>
		 </template>
		 <el-input @input="marketData.constantw=CheckInputFloat(marketData.constantw)"  
		 v-model="marketData.constantw"> <template #append>元/千克 </template></el-input>
	</el-form-item>  
	<el-form-item v-if="country=='IN'" label="销售GST税率" >
		     <template #label>
				 <div class="text-right">
				  <span class="font-small">销售GST税率</span>	 
				  <p>Selling GST Rate</p>
				 </div>
			 </template>
			 <el-input  @input="marketData.sellingGSTRate=CheckInputFloat(marketData.sellingGSTRate)"  
			 v-model="marketData.sellingGSTRate"> <template #append>%</template></el-input>
		</el-form-item> 
		 
		 <el-form-item v-if="country=='IN'" label="企业所得税率" >
		      <template #label>
		 		 <div class="text-right">
		 		  <span class="font-small">企业所得税率</span>	 
		 		  <p>Corporate Income Tax Rate</p>
		 		 </div>
		 	 </template>
		 	 <el-input 
			 @input="marketData.corporateInRate=CheckInputFloat(marketData.corporateInRate)"  
			 v-model="marketData.corporateInRate"> <template #append>%</template></el-input>
		 </el-form-item> 
	<el-form-item label="进口关税费率" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">进口关税费率</span>	 
			  <p>Import Duty Rate</p>
			 </div>
		 </template>
		 <el-input @input="marketData.taxrate=CheckInputFloat(marketData.taxrate)"  
		 v-model="marketData.taxrate"> <template #append>%</template></el-input>
	</el-form-item>  
	
	<el-form-item label="汇率损耗比率" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">汇率损耗比率</span>	 
			  <p>Currency Transport Fee</p>
			 </div>
		 </template>
		 <el-input  @input="marketData.lostrate=CheckInputFloat(marketData.lostrate)"  
		  v-model="marketData.lostrate"> <template #append>%</template></el-input>
	</el-form-item>  
	
	<el-form-item label="市场营销费用占销售额比率" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">市场营销费用占销售额比率</span>	 
			  <p>Marketing</p>
			 </div>
		 </template>
		 <el-input   @input="marketData.sellerrate=CheckInputFloat(marketData.sellerrate)"  
		 v-model="marketData.sellerrate"> <template #append>%</template></el-input>
	</el-form-item>  
	
	<el-form-item label="其它每单销售固定成本比率" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">其它每单销售固定成本比率</span>	 
			  <p>Others Rate</p>
			 </div>
		 </template>
		 <el-input  @input="marketData.costrate=CheckInputFloat(marketData.costrate)"  
		 v-model="marketData.costrate"> <template #append>%</template></el-input>
	</el-form-item>  
	
	<el-form-item label="其它每单销售固定费用" >
	     <template #label>
			 <div class="text-right">
			  <span class="font-small">其它每单销售固定费用</span>	 
			  <p>Others</p>
			 </div>
		 </template>
		 <el-input @input="marketData.otherfee=CheckInputFloat(marketData.otherfee)" 
		  v-model="marketData.otherfee"> <template #append>{{currencyicon}}</template></el-input>
	</el-form-item>  
	<el-form-item v-if="country=='EU'" label="VAT增值税费率" >
	    <template #label>
			<div class="text-right">
			  <span class="font-small">VAT增值税费率:</span>	 
			  <p>VAT TAX Rate</p>
			</div>
		</template>
		<el-row>
		 <el-col :span="8" class="paddingleft10">
			     <span class="font-extraSmall">德国(DE)</span>
			     <el-input  @input="countryMapData.DE.vatRate=CheckInputFloat(countryMapData.DE.vatRate)" 
				 v-model="countryMapData.DE.vatRate"> <template #append>% </template></el-input>
		 </el-col>
		<el-col :span="8" class="paddingleft10">
		     <span class="font-extraSmall">法国(FR)</span>
		     <el-input @input="countryMapData.FR.vatRate=CheckInputFloat(countryMapData.FR.vatRate)" 
			 v-model="countryMapData.FR.vatRate"> <template #append>% </template></el-input>
		</el-col>
		<el-col :span="8" class="paddingleft10">
			<span class="font-extraSmall">意大利(IT)</span>
		    <el-input @input="countryMapData.IT.vatRate=CheckInputFloat(countryMapData.IT.vatRate)" 
			v-model="countryMapData.IT.vatRate"> <template #append>% </template></el-input>
		</el-col>
		<el-col :span="8" class="paddingleft10">
		     <span class="font-extraSmall">西班牙(ES) </span>
		    <el-input @input="countryMapData.ES.vatRate=CheckInputFloat(countryMapData.ES.vatRate)" 
			     v-model="countryMapData.ES.vatRate"> <template #append>% </template></el-input>
		</el-col>
		<el-col :span="8" class="paddingleft10">
		    <span class="font-extraSmall">荷兰(NL) </span>
		    <el-input @input="countryMapData.NL.vatRate=CheckInputFloat(countryMapData.NL.vatRate)" 
			v-model="countryMapData.NL.vatRate"> <template #append>% </template></el-input>
		 </el-col>
		<el-col :span="8" class="paddingleft10">
		    <span class="font-extraSmall">瑞典(SE)</span>
		    <el-input @input="countryMapData.SE.vatRate=CheckInputFloat(countryMapData.SE.vatRate)" 
			v-model="countryMapData.SE.vatRate"> <template #append>% </template></el-input>
		 </el-col>
		<el-col :span="8" class="paddingleft10">
		    <span class="font-extraSmall">波兰(PL) </span>
		    <el-input  @input="countryMapData.PL.vatRate=CheckInputFloat(countryMapData.PL.vatRate)" 
			v-model="countryMapData.PL.vatRate"> <template #append>% </template></el-input>
		</el-col>
		<el-col :span="8" class="paddingleft10">
		    <span class="font-extraSmall">比利时(BE) </span>
		    <el-input  @input="countryMapData.BE.vatRate=CheckInputFloat(countryMapData.BE.vatRate)" 
			v-model="countryMapData.BE.vatRate"> <template #append>% </template></el-input>
		</el-col>
			 </el-row>
	</el-form-item>  
	
	
	</el-col>
	</el-row>
</template>

<script setup>
	import configApi from "@/api/amazon/profit/config.js"
	import {ref,reactive,toRefs,onMounted}from"vue"
	import countryData from '@/model/amazon/profit/configData.json';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	let props = defineProps({
		marketData:{default:{},},
		countryMapData:{marketDate:{default:{}}},
		fbaMonthsOptions:[],
		manualProcessingFeeOptions:[],
		storageFee:[],
		country:'',
		currency:'',
		shipmentstyle:'',
		currencyicon:'',
		inplacefeeOptions:[],
	})
 
	let {marketData,fbaMonthsOptions,manualProcessingFeeOptions,countryMapData,shipmentstyle,
	     storageFee,country,currency,currencyicon,inplacefeeOptions} =toRefs(props);
</script>

<style>
	.paddingleft10{
		padding-left: 10px;
	}
</style>
