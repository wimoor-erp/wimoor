<template>
	<h3 id="logistics" class="tab-scroll">物流信息</h3>
	
	<el-row>
		<el-col :span="8">
			<el-form-item label="附加费">
				<el-input v-model="dataForms.addfee" type="text" @input="dataForms.addfee=CheckInputFloat(dataForms.addfee)" placeholder="请输入采购成本"></el-input>
			</el-form-item>
		</el-col>
	 
	</el-row>
  <el-form-item label="报关(国内)">
    <el-form  label-width="auto" label-position="top" :inline="true" size="small">
      <el-row style="width:100%">
          <el-col :span="6">
              <el-form-item label="海关编码" prop="code">
                <el-input size="small" v-model="custom.code"> </el-input>
              </el-form-item>
          </el-col>
            <el-col :span="6">
              <el-form-item label="中文名" prop="cname">
                <el-input size="small" v-model="custom.cname"> </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="英文名" prop="ename">
                <el-input size="small" v-model="custom.ename"> </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-form-item label="报价" prop="price">
                  <el-input size="small" v-model="custom.price"> </el-input>
                </el-form-item>
            </el-col>
      </el-row>
      <el-row style="width:100%">
        <el-col :span="6">
          <el-form-item label="材质(英文)" prop="material">
            <el-input size="small" v-model="custom.material"> </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="材质(中文)" prop="materialcn">
            <el-input size="small" v-model="custom.materialcn"> </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="用途" prop="application">
            <el-input size="small" v-model="custom.application"> </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="税率" prop="rate">
            <el-input size="small" v-model="custom.rate" @input="custom.rate=CheckInputFloat(custom.rate)" >
              <template #append>
                %
              </template>
            </el-input>
           </el-form-item>
        </el-col>
      </el-row>
      <el-row style="width:100%">
        <el-col :span="6">
          <el-form-item label="单位"  prop="unit">
            <el-autocomplete
              v-model="custom.unit"
              :fetch-suggestions="querySearch"
              style="width: 150px;"
              clearable
              size="small"
              placeholder="请输入单位"
              @select="handleUnitSelect"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="子单位"  prop="unit2">
            <el-autocomplete
              v-model="custom.unit2"
              :fetch-suggestions="querySearch"
              style="width: 150px;"
              clearable
              size="small"
              placeholder="请输入子单位"
              @select="handleUnitSelect"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="子单位比例"  prop="unitrate">
            <el-input   size="small"    v-model="custom.unitrate"> </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row style="width:100%">
        <el-col :span="24">
          <el-form-item label="海关要素"  prop="elements">
            <el-input type="textarea" size="small"  :rows="4" v-model="custom.elements"> </el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-form-item>
	<el-form-item label="报关(海外)" >
       <el-space wrap>
         <el-button title="清空"  type="info"    style="width:60px;height:160px;"   :icon="Delete"  size="small" @click="clearForms()">清空</el-button>
         <el-card v-for="(row,index) in listForms" shadow="hover" :key="index" style="width: 160px;height:160px;">
           <div style="display:flex;justify-content: flex-end;margin-top:-15px;margin-right:-15px;">
             <el-button type="info" link  :icon="Close" size="small" @click="removeLogtis(index)"  ></el-button>
           </div>
           <el-form  label-width="auto" label-position="top"  size="small">
             <el-form-item label="国家" prop="country">
               <el-select size="small" v-model="row.country" style="width:100px">
                 <el-option v-for="item in marketList"   :key="item.market"  :label="item.name" :value="item.market" ></el-option>
               </el-select>
             </el-form-item>
             <el-form-item label="海关编码(必填)" prop="code">
               <el-input size="small" v-model="row.code"> </el-input>
             </el-form-item>
           </el-form>
         </el-card>

    <el-card  style="width: 160px;height:160px;align-content: center;text-align: center;align-items: center; " @click="addCustItem">
      <br/>
      <el-link :underline="false" type="primary" >
        <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
      </el-link>
    </el-card>

</el-space>

	</el-form-item>
</template>

<script setup>
	import {Plus,Minus} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
  import '@/assets/css/packbox_table.css'
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
  import {
    Check,
    Delete,
    Edit,
    Close,
    Search,
    Star,
  } from '@element-plus/icons-vue'

	const emit = defineEmits(['clear']);
	let props = defineProps({  listForms:Object,dataForms:Object,custom:Object })
	let {listForms,dataForms,custom} =toRefs(props);
	let state=reactive({
		marketList:[],
		units:[]
	});
	let { marketList, units  } =toRefs(state);
	
	function clearForms(){
		emit("clear");
	}
	function addCustItem(){
		var row={};
		row.country="US";
		row.price=0;
		row.code="";
		row.rate=0;
		props.listForms.push(row);
	}
	function loadAllMarket(){
		marketApi.getMarketAll().then((res)=>{
			state.marketList=res.data;
		});
	}
	function removeLogtis(index){
		props.listForms.splice(index,1)
	}
	onMounted(()=>{
		loadAllMarket();
		state.units = loadUnits();
	})

	const querySearch = (queryString, cb) => {
		const results = queryString
			? units.value.filter(createFilter(queryString))
			: units.value
		cb(results)
	}
	const createFilter = (queryString) => {
		return (unit) => {
			return (
				unit.toLowerCase().indexOf(queryString.toLowerCase()) === 0
			)
		}
	}
  const loadUnits = () => {
    return [
      { value: '台' },
      { value: '座' },
      { value: '辆' },
      { value: '艘' },
      { value: '架' },
      { value: '套' },
      { value: '个' },
      { value: '只' },
      { value: '头' },
      { value: '张' },
      { value: '件' },
      { value: '支' },
      { value: '枝' },
      { value: '根' },
      { value: '条' },
      { value: '把' },
      { value: '块' },
      { value: '卷' },
      { value: '副' },
      { value: '片' },
      { value: '组' },
      { value: '份' },
      { value: '幅' },
      { value: '双' },
      { value: '对' },
      { value: '棵' },
      { value: '株' },
      { value: '井' },
      { value: '米' },
      { value: '盘' },
      { value: '平方米' },
      { value: '立方米' },
      { value: '筒' },
      { value: '千克' },
      { value: '克' },
      { value: '盆' },
      { value: '万个' },
      { value: '具' },
      { value: '百副' },
      { value: '百支' },
      { value: '百把' },
      { value: '百个' },
      { value: '百片' },
      { value: '刀' },
      { value: '疋' },
      { value: '公担' },
      { value: '扇' },
      { value: '百枝' },
      { value: '千只' },
      { value: '千块' },
      { value: '千盒' },
      { value: '千枝' },
      { value: '千个' },
      { value: '亿支' },
      { value: '亿个' },
      { value: '万套' },
      { value: '千张' },
      { value: '万张' },
      { value: '千伏安' },
      { value: '千瓦' },
      { value: '千瓦时' },
      { value: '千升' },
      { value: '英尺' },
      { value: '吨' },
      { value: '长吨' },
      { value: '短吨' },
      { value: '司马担' },
      { value: '司马斤' },
      { value: '斤' },
      { value: '磅' },
      { value: '担' },
      { value: '英担' },
      { value: '短担' },
      { value: '两' },
      { value: '市担' },
      { value: '盎司' },
      { value: '克拉' },
      { value: '市尺' },
      { value: '码' },
      { value: '英寸' },
      { value: '寸' },
      { value: '升' },
      { value: '毫升' },
      { value: '英加仑' },
      { value: '美加仑' },
      { value: '立方英尺' },
      { value: '立方尺' },
      { value: '平方码' },
      { value: '平方英尺' },
      { value: '平方尺' },
      { value: '英制马力' },
      { value: '公制马力' },
      { value: '令' },
      { value: '箱' },
      { value: '批' },
      { value: '罐' },
      { value: '桶' },
      { value: '扎' },
      { value: '包' },
      { value: '箩' },
      { value: '打' },
      { value: '筐' },
      { value: '罗' },
      { value: '匹' },
      { value: '册' },
      { value: '本' },
      { value: '发' },
      { value: '枚' },
      { value: '捆' },
      { value: '袋' },
      { value: '粒' },
      { value: '盒' },
      { value: '合' },
      { value: '瓶' },
      { value: '千支' },
      { value: '万双' },
      { value: '万粒' },
      { value: '千粒' },
      { value: '千米' },
      { value: '千英尺' },
      { value: '百万贝可' },
      { value: '部' },
      { value: '亿株' },
      { value: '人份' },
    ]
  }
	const handleUnitSelect = (item) => {
		console.log(item)
	}
		 
		 
</script>

<style>
	.wi-100{
		width:100px;
	}
  .m-b-16{
    margin-bottom:16px;
  }
</style>
