<template>
  <el-row style="padding-bottom:20px">
    <el-col :span="24">
      <el-space>
        <Group ref="groups"
               @change="changeGroup"
               defaultValue="only"/>
        <el-input  v-model="queryParams.search"
                   clearable
                   @input="handleQuery"
                   @clear="handleQuery"
                   placeholder="请输入SKU"
                   class="input-with-select" >
          <template #append>
            <el-button >
              <el-icon style="font-size: 16px;align-itmes:center">
                <search @click="handleQuery" />
              </el-icon>
            </el-button>
          </template>
        </el-input>
        <el-date-picker
            v-model="queryParams.monthDate"
            type="month"
            placeholder="选择月份"
            @change="changedate"
        />
        <el-button @click="downloadData">导出</el-button>
        <div class="font-extraSmall">所有费用使用站点币种</div>
      </el-space>
    </el-col>
  </el-row>
  <GlobalTable  ref="globalTable"
                :tableData="tableData"
                @loadTable="loadtableData"
                :cellClassName="cellClassName"
                height="calc(100vh - 300px)"
  >
    <template #field>
      <el-table-column label="图片" width="65" fixed>
        <template #default="scope">
          <el-image
              class="product-img"
              v-if="scope.row.image"
              :src="scope.row.image"></el-image>
          <el-image v-else
                    :src="$require('empty/noimage40.png')"
                    class="product-img"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column  label="SKU/名称" min-width="210" show-overflow-tooltip fixed>
        <template #default="scope">
          <div >{{scope.row.pname}}</div>
          <span class="font-extraSmall">{{scope.row.sku}}</span>
        </template>
      </el-table-column>
      <el-table-column  label="销售额(含税)" width="120" prop="principal" sortable="custom"  >
      </el-table-column>
      <el-table-column  label="销售额" width="100" prop="principalWithoutTax" sortable="custom"  >
      </el-table-column>
      <el-table-column label="销量" prop="sales" sortable="custom" width="80"></el-table-column>
      <el-table-column label="订单量" prop="order_amount" sortable="custom" width="90"></el-table-column>
      <el-table-column label="退货数" prop="refundsales" sortable="custom" width="85" ></el-table-column>
      <el-table-column label="退货金额" prop="refund" sortable="custom" width="100"  ></el-table-column>
      <el-table-column label="FBA费" width="90" sortable="custom"   prop="fbafee">
        <template #default="scope">
          <p>{{scope.row.fbafee}}</p>
        </template>
      </el-table-column>
      <el-table-column label="数字服务费" prop="digital" sortable="custom" width="120" ></el-table-column>
      <el-table-column label="佣金" prop="commission" sortable="custom" width="80" ></el-table-column>

      <el-table-column label="赔偿金" prop="reimbursement" sortable="custom" width="95"  ></el-table-column>
      <el-table-column label="仓储费" prop="skuStorageFee" sortable="custom" width="95"  ></el-table-column>
      <el-table-column label="广告花费" width="100" sortable="custom"   prop="rpt_adv_spend_fee">
        <template #default="scope">
          <p>{{scope.row.rpt_adv_spend_fee}}</p>
          <span v-if="scope.row.oldRow" class="light-font">{{scope.row.oldRow.month}}月: {{formatFloat(scope.row.oldRow.rpt_adv_spend_fee)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="其他费用" prop="fin_sum_fee" sortable="custom" width="100"  ></el-table-column>
      <el-table-column label="采购成本" width="100" sortable="custom" align="right" prop="price">
        <template #default="scope">
          {{scope.row.price}}
        </template>
      </el-table-column>
      <el-table-column label="毛利润" prop="profit" sortable="custom" width="110" align="right">
        <template #header>
          <div style="float:left">
            <p class="l-h-none " >
              <el-space>毛利润
                <el-tooltip
                    effect="dark"
                    content=""
                    placement="top-start"
                >
                  <template #content>
                    <div>利润 = 销售额+refund+FBA费+数字服务费+佣金+赔偿金-(其它成本+采购成本+仓储费+广告费用)</div>
                  </template>
                  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
                </el-tooltip>
              </el-space>
            </p>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="毛利率" prop="profitRate" sortable="custom" width="100" fixed="right">
        <template #default="scope">
          <span v-if="scope.row.profitRate">{{formatFloat(scope.row.profitRate*100)}}%</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
    </template>
  </GlobalTable>
</template>
<script setup>
import { ref,reactive,onMounted,toRefs,} from 'vue'
import {Search,InfoFilled} from '@element-plus/icons-vue';
import Group from '@/components/header/group.vue';
import {Plus,Formula,Help,Copy,MoreOne} from '@icon-park/vue-next';
import {formatFloat,formatPercent,formatInteger,decodeText,getCurrencyMark,outputmoney} from '@/utils/index.js';
import Datepicker from '@/components/header/datepicker.vue';
import Currency from"@/components/header/currency.vue";
import settlementSkuRptApi from '@/api/amazon/finances/settlementSkuRptApi.js';
import { useRoute,useRouter } from 'vue-router'
import financesApi from "@/views/customized/chelsea/api/finances/financesApi.js";
const editPriceRef = ref();
const calcDialogRef=ref();
const refreshDataDialogRef=ref();
const otherFeeDiaRef = ref();
const globalTable=ref();
const poporef = ref();
const monthRef = ref();
const currencyRef=ref();
const router = useRouter();
const state = reactive({
  queryParams:{currency:'MARKET',charttype:"sku",fatype:"sku",
    sort:"principal","order":"desc",
    "monthDate":new Date(new Date().getFullYear(), new Date().getMonth() - 1, 1),"quarterValue":new Date()},
  orderState:[
    {label:'SKU',name:'0',},
    {label:'月度',name:'1',},
  ],
  quarterData:{},
  activeName:"0",
  tableData:{
    records:[],
    total:1,
  }
})
const {
  orderState,
  activeName,
  quarterData,
  tableData,
  queryParams,
} = toRefs(state)

function recalculation(){
  refreshDataDialogRef.value.show();
}

function handleChange(val){
  handleQuery();
}
function formulaConfig(){
  calcDialogRef.value.show();
}


function getFBARef(el){
  if(el){
    poporef.value.buttonRef = el;
  }
}
function changeCurrency(value){
  state.queryParams.currency=value;
  handleQuery();
}
function editPrice(row){
  state.skuprice = row.price;
}
function handleOtherFee(){
  otherFeeDiaRef.value.show();
}
function handleQuery(){
    globalTable.value.loadTable(state.queryParams);
}
function loadtableData(param){
  param.fromDate=state.queryParams.monthDate;
  param.fromDate.setTime(param.fromDate.getTime() + 3600 * 1000 * 24 * 1)
  param.fromDate=new Date(param.fromDate);
  param.groupid=state.queryParams.groupid;
  param.marketplaceid=state.queryParams.marketplaceid;
  param.currency=state.queryParams.currency;
  param.fatype=state.queryParams.fatype;
  param.charttype=state.queryParams.charttype;
  param.search=state.queryParams.search;
  settlementSkuRptApi.getList(param).then(res=>{
    state.tableData.records=res.data.records;
    state.tableData.total=res.data.total;
  })
}
function changeGroup(obj){
  state.queryParams.groupid=obj.groupid;
  state.queryParams.marketplaceid=obj.marketplaceid;
  handleQuery();
}
//日期改变
function changedate(dates){
  state.queryParams.monthDate=dates;
  handleQuery();
}
function changedaterange(dates){
  state.queryParams.monthfromDate=dates.start;
  state.queryParams.monthendDate=dates.end;
  getMonthData();
}
function exchangeRateSet(){
  router.push({
    path:"/finance/exchangeRate",
    query:{
      title:'汇率管理',
      path:'/finance/exchangeRate',

    }
  })
}
function cellClassName(data){
  if(data.column.property=="setincome"){
    return "cell-setincome";
  }
}
function downloadData(){
    var param=state.queryParams;
    param.fromDate=state.queryParams.monthDate;
    param.groupid=state.queryParams.groupid;
    param.marketplaceid=state.queryParams.marketplaceid;
    param.currency=state.queryParams.currency;
    param.fatype=state.queryParams.fatype;
    param.charttype=state.queryParams.charttype;
    param.search=state.queryParams.search;
    settlementSkuRptApi.downloadList(param);
}

</script>
<style scoped>

</style>