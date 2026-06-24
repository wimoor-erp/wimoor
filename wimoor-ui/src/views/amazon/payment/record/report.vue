
<template>
  <div  class="flex-between">
    <el-space style="margin-bottom: 20px;">
      <Group @change="groupChange" />
      <div>
        <Datepicker ref="datepickers" :shortIndex="1" @changedate="changedate" />
      </div>
      <el-button type="primary" @click="handleSearch" :loading="searchLoading" size="default">查询</el-button>
      <el-button @click="resetForm" size="default">重置</el-button>

    </el-space>
    <el-space>
      <PrinterBtn printId="recordReportPrintArea" title="加工单详情" />
    </el-space>
  </div>
  <div class="report-container" id="recordReportPrintArea">
    <!-- 页面头部 -->
    <!-- 筛选条件 -->


    <el-dialog v-model="settingDialogVisible" title="店铺信息设置" width="500px">
      <el-form :model="settingForm" label-width="120px">
        <el-form-item label="Display name">
          <el-input v-model="settingForm.displayName" placeholder="请输入显示名称" />
        </el-form-item>
        <el-form-item label="Legal name">
          <el-input v-model="settingForm.legalName" placeholder="请输入公司名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="settingDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveGroupInfo" :loading="saveLoading">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <div class="header">
      <div class="logo-section">
        <img v-if="queryParams.marketcurrency === 'USD'||queryParams.marketcurrency === 'CAD' || queryParams.marketcurrency === 'MXN'" src="/src/assets/image/amzus.png" alt="Amazon Seller Central"  >
        <img v-else src="/src/assets/image/europe.png" alt="Amazon Seller Central"  >
      </div>
      <div class="account-info"  >
        <div class="info-row" style="margin-left: 400px;">
          
          <span class="label">Display name:</span>
          <span class="value">{{ settingForm.displayName }}</span>
           <el-button style="margin-left: 10px;" class="font-extraSmall" link @click="openSettingDialog" size="default" :disabled="!queryParams.groupid">
          <el-icon class="font-extraSmall" ><Setting /></el-icon>
           
        </el-button>
        </div>
        <div class="info-row" style="margin-left: 400px;">
          <span class="label">Legal name:</span>
          <span class="value">{{ settingForm.legalName }}</span>
        </div>
      </div>
      <div class="page-info">
        <span>Page 1 of 1</span>
      </div>

      <!-- 账户活动时间范围 -->

    </div>
    <el-row>
      <div class="time-range" style="display: flex; justify-content: space-between; width: 100%;">
        <p>Account activity from {{ formatDate(queryParams.fromDate) }} 00:00 {{formData.timezone}} to {{ formatDate(queryParams.endDate) }} 23:59 {{formData.timezone}}</p>
        <p>All amounts in {{ queryParams.marketcurrency  }}, unless specified</p>
      </div>
    </el-row>

    <!-- 摘要部分 -->
    <div class="summary-section">
     <el-row>
       <h3>Summaries</h3>
       <span style="margin-left: 100px;" >Can include Amazon Marketplace, Fulfilment by Amazon(FBA), and Amazon Webstore transactions</span>
     </el-row>
      <div class="summary-table">
        <table>
          <thead>
            <tr>
              <th> </th>
              <th> </th>
              <th>Totals</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!formData.summary || formData.summary.length === 0">
              <td>Income</td>
              <td><span class="font-extraSmall">Sales, credits, and refunds</span></td>
              <td class="positive">{{ outputmoney(incomeSubtotalDebits+incomeSubtotalCredits) }}</td>
            </tr>
            <tr v-if="!formData.summary || formData.summary.length === 0">
              <td>Expenses</td>
              <td><span class="font-extraSmall">Fees, including Amazon service fees, selling fees, FBA fees, and delivery</span></td>
              <td class="negative">{{ outputmoney(expensesSubtotalDebits+expensesSubtotalCredits) }}</td>
            </tr>
            <tr v-if="!formData.summary || formData.summary.length === 0">
              <td>Tax</td>
              <td><span class="font-extraSmall">Net taxes collected on product sales and services</span></td>
              <td class="positive">{{ outputmoney(taxSubtotalDebits+taxSubtotalCredits) }}</td>
            </tr>
            <tr v-if="!formData.summary || formData.summary.length === 0">
              <td>Transfers</td>
              <td><span class="font-extraSmall">Deposits and withdrawals</span></td>
              <td class="negative">{{ outputmoney(transfersSubtotalDebits+transfersSubtotalCredits) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 详细部分 -->
    <div class="details-section">
      <!-- Income和Expenses左右结构 -->
      <div class="detail-row">
        <!-- 收入部分 -->
        <div class="detail-block">
          <div class="detail-table">
            <table>
              <tbody>
              <tr>
                <td></td>
                <td><span class="font-bold">Debits</span></td>
                <td><span class="font-bold">Credits</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Seller fulfilled product sales<span v-if="formData['Seller fulfilled product sales_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Seller fulfilled product sales_rmb'])}})</span></td>
                <td><span v-if="formData['Seller fulfilled product sales']&&formData['Seller fulfilled product sales']<0" >{{outputmoney(formData['Seller fulfilled product sales'])}}</span></td>
                <td><span v-if="formData['Seller fulfilled product sales']&&formData['Seller fulfilled product sales']>=0" >{{outputmoney(formData['Seller fulfilled product sales'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Seller-fulfilled product sale refunds <span v-if="formData['Seller_fulfilled product sale refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Seller_fulfilled product sale refunds_rmb'])}})</span></td>
                <td><span v-if="formData['Seller_fulfilled product sale refunds']&&formData['Seller_fulfilled product sale refunds']<0" >{{outputmoney(formData['Seller_fulfilled product sale refunds'])}}</span></td>
                <td><span v-if="formData['Seller_fulfilled product sale refunds']&&formData['Seller_fulfilled product sale refunds']>=0" >{{outputmoney(formData['Seller_fulfilled product sale refunds'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>FBA product sales <span v-if="formData['FBA product sales_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA product sales_rmb'])}})</span></td>
                <td><span v-if="formData['FBA product sales']&&formData['FBA product sales']<0" >{{outputmoney(formData['FBA product sales'])}}</span></td>
                <td><span v-if="formData['FBA product sales']&&formData['FBA product sales']>=0" >{{outputmoney(formData['FBA product sales'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>FBA product sale refunds <span v-if="formData['FBA product sale refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA product sale refunds_rmb'])}})</span></td>
                <td><span v-if="formData['FBA product sale refunds']&&formData['FBA product sale refunds']<0" >{{outputmoney(formData['FBA product sale refunds'])}}</span></td>
                <td><span v-if="formData['FBA product sale refunds']&&formData['FBA product sale refunds']>=0" >{{outputmoney(formData['FBA product sale refunds'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>FBA inventory credit</td>
                <td><span v-if="formData['FBA inventory credit']&&formData['FBA inventory credit']<0" >{{outputmoney(formData['FBA inventory credit'])}}</span></td>
                <td><span v-if="formData['FBA inventory credit']&&formData['FBA inventory credit']>=0" >{{outputmoney(formData['FBA inventory credit'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>FBA liquidation proceeds <span v-if="formData['FBA liquidation proceeds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA liquidation proceeds_rmb'])}})</span></td>
                <td><span v-if="formData['FBA liquidation proceeds']&&formData['FBA liquidation proceeds']<0" >{{outputmoney(formData['FBA liquidation proceeds'])}}</span></td>
                <td><span v-if="formData['FBA liquidation proceeds']&&formData['FBA liquidation proceeds']>=0" >{{outputmoney(formData['FBA liquidation proceeds'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>FBA Liquidations proceeds adjustments <span v-if="formData['FBA Liquidations proceeds adjustments_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA Liquidations proceeds adjustments_rmb'])}})</span></td>
                <td><span v-if="formData['FBA Liquidations proceeds adjustments']&&formData['FBA Liquidations proceeds adjustments']<0" >{{outputmoney(formData['FBA Liquidations proceeds adjustments'])}}</span></td>
                <td><span v-if="formData['FBA Liquidations proceeds adjustments']&&formData['FBA Liquidations proceeds adjustments']>=0" >{{outputmoney(formData['FBA Liquidations proceeds adjustments'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Postage credits <span v-if="formData['Postage credits_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Postage credits_rmb'])}})</span></td>
                <td><span v-if="formData['Postage credits']&&formData['Postage credits']<0" >{{outputmoney(formData['Postage credits'])}}</span></td>
                <td><span v-if="formData['Postage credits']&&formData['Postage credits']>=0" >{{outputmoney(formData['Postage credits'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Delivery credit refunds <span v-if="formData['Delivery credit refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Delivery credit refunds_rmb'])}})</span></td>
                <td><span v-if="formData['Delivery credit refunds']&&formData['Delivery credit refunds']<0" >{{outputmoney(formData['Delivery credit refunds'])}}</span></td>
                <td><span v-if="formData['Delivery credit refunds']&&formData['Delivery credit refunds']>=0" >{{outputmoney(formData['Delivery credit refunds'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Gift wrap credits <span v-if="formData['Gift wrap credits_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Gift wrap credits_rmb'])}})</span></td>
                <td><span v-if="formData['Gift wrap credits']&&formData['Gift wrap credits']<0" >{{outputmoney(formData['Gift wrap credits'])}}</span></td>
                <td><span v-if="formData['Gift wrap credits']&&formData['Gift wrap credits']>=0" >{{outputmoney(formData['Gift wrap credits'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Gift wrap credits refunds <span v-if="formData['Gift wrap credits refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Gift wrap credits refunds_rmb'])}})</span></td>
                <td><span v-if="formData['Gift wrap credits refunds']&&formData['Gift wrap credits refunds']<0" >{{outputmoney(formData['Gift wrap credits refunds'])}}</span></td>
                <td><span v-if="formData['Gift wrap credits refunds']&&formData['Gift wrap credits refunds']>=0" >{{outputmoney(formData['Gift wrap credits refunds'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Promotional rebates <span v-if="formData['Promotional rebates_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Promotional rebates_rmb'])}})</span></td>
                <td><span v-if="formData['Promotional rebates']&&formData['Promotional rebates']<0" >{{outputmoney(formData['Promotional rebates'])}}</span></td>
                <td><span v-if="formData['Promotional rebates']&&formData['Promotional rebates']>=0" >{{outputmoney(formData['Promotional rebates'])}}</span></td>
              </tr>
              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Promotional rebate refunds <span v-if="formData['Promotional rebate refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Promotional rebate refunds_rmb'])}})</span></td>
                <td><span v-if="formData['Promotional rebate refunds']&&formData['Promotional rebate refunds']<0" >{{outputmoney(formData['Promotional rebate refunds'])}}</span></td>
                <td><span v-if="formData['Promotional rebate refunds']&&formData['Promotional rebate refunds']>=0" >{{outputmoney(formData['Promotional rebate refunds'])}}</span></td>
              </tr>
<!--            <tr v-if="!formData.income || formData.income.length === 0">
                <td>A-to-z Guarantee claims <span v-if="formData['A-to-z Guarantee claims_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['A-to-z Guarantee claims_rmb'])}})</span></td>
                <td><span v-if="formData['A-to-z Guarantee claims']&&formData['A-to-z Guarantee claims']<0" >{{outputmoney(formData['A-to-z Guarantee claims'])}}</span></td>
                <td><span v-if="formData['A-to-z Guarantee claims']&&formData['A-to-z Guarantee claims']>=0" >{{outputmoney(formData['A-to-z Guarantee claims'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Chargebacks <span v-if="formData['Chargebacks_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Chargebacks_rmb'])}})</span></td>
                <td><span v-if="formData['Chargebacks']&&formData['Chargebacks']<0" >{{outputmoney(formData['Chargebacks'])}}</span></td>
                <td><span v-if="formData['Chargebacks']&&formData['Chargebacks']>=0" >{{outputmoney(formData['Chargebacks'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Amazon Shipping Reimbursement <span v-if="formData['Amazon Shipping Reimbursement_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Amazon Shipping Reimbursement_rmb'])}})</span></td>
                <td><span v-if="formData['Amazon Shipping Reimbursement']&&formData['Amazon Shipping Reimbursement']<0" >{{outputmoney(formData['Amazon Shipping Reimbursement'])}}</span></td>
                <td><span v-if="formData['Amazon Shipping Reimbursement']&&formData['Amazon Shipping Reimbursement']>=0" >{{outputmoney(formData['Amazon Shipping Reimbursement'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                <td>SAFE-T Reimbursement <span v-if="formData['SAFE-T Reimbursement_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['SAFE-T Reimbursement_rmb'])}})</span></td>
                <td><span v-if="formData['SAFE-T Reimbursement']&&formData['SAFE-T Reimbursement']<0" >{{outputmoney(formData['SAFE-T Reimbursement'])}}</span></td>
                <td><span v-if="formData['SAFE-T Reimbursement']&&formData['SAFE-T Reimbursement']>=0" >{{outputmoney(formData['SAFE-T Reimbursement'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Receivables Reversals <span v-if="formData['Receivables Reversals_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Receivables Reversals_rmb'])}})</span></td>
                <td><span v-if="formData['Receivables Reversals']&&formData['Receivables Reversals']<0" >{{outputmoney(formData['Receivables Reversals'])}}</span></td>
                <td><span v-if="formData['Receivables Reversals']&&formData['Receivables Reversals']>=0" >{{outputmoney(formData['Receivables Reversals'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Commingling VAT <span v-if="formData['Commingling VAT_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Commingling VAT_rmb'])}})</span></td>
                <td><span v-if="formData['Commingling VAT']&&formData['Commingling VAT']<0" >{{outputmoney(formData['Commingling VAT'])}}</span></td>
                <td><span v-if="formData['Commingling VAT']&&formData['Commingling VAT']>=0" >{{outputmoney(formData['Commingling VAT'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                  <td>Product sales (non-FBA) <span v-if="formData['Product sales (non-FBA) _mb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Product sales (non-FBA) _mb'])}})</span></td>
                  <td><span v-if="formData['Product sales (non-FBA)']&&formData['Product sales (non-FBA)']>=0" >{{outputmoney(formData['Product sales (non-FBA)'])}}</span></td>
                  <td><span v-if="formData['Product sales (non-FBA)']&&formData['Product sales (non-FBA)']<0" >{{outputmoney(formData['Product sales (non-FBA)'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                  <td>Product sale refunds (non-FBA) <span v-if="formData['Product sale refunds (non-FBA) _mb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Product sale refunds (non-FBA) _mb'])}})</span></td>
                  <td><span v-if="formData['Product sale refunds (non-FBA)']&&formData['Product sale refunds (non-FBA)']>=0" >{{outputmoney(formData['Product sale refunds (non-FBA)'])}}</span></td>
                  <td><span v-if="formData['Product sale refunds (non-FBA)']&&formData['Product sale refunds (non-FBA)']<0" >{{outputmoney(formData['Product sale refunds (non-FBA)'])}}</span></td>
              </tr>-->
<!--              <tr v-if="!formData.income || formData.income.length === 0">
                <td>Amazon Shipping Reimbursement Adjustments <span v-if="formData['Amazon Shipping Reimbursement Adjustments_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Amazon Shipping Reimbursement Adjustments_rmb'])}})</span></td>
                <td><span v-if="formData['Amazon Shipping Reimbursement Adjustments']&&formData['Amazon Shipping Reimbursement Adjustments']>=0" >{{outputmoney(formData['Amazon Shipping Reimbursement Adjustments'])}}</span></td>
                <td><span v-if="formData['Amazon Shipping Reimbursement Adjustments']&&formData['Amazon Shipping Reimbursement Adjustments']<0" >{{outputmoney(formData['Amazon Shipping Reimbursement Adjustments'])}}</span></td>
              </tr>-->
              <tr v-if="!formData.income || formData.income.length === 0">
                  <td>Others <span v-if="formData['Others_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Others_rmb'])}})</span></td>
                  <td><span v-if="formData['Others']&&formData['Others']<0" >{{outputmoney(formData['Others'])}}</span></td>
                  <td><span v-if="formData['Others']&&formData['Others']>=0" >{{outputmoney(formData['Others'])}}</span></td>
              </tr>
              <tr class="subtotal">
                <td>subtotals</td>
                <td>{{ outputmoney(incomeSubtotalDebits) }}</td>
                <td>{{ outputmoney(incomeSubtotalCredits) }}</td>
              </tr>
              </tbody>
              <thead>
              <tr>
                <th>
                  <h3>Income</h3>
                </th>
                <th></th>
                <th>
                  <div class="total-amount">{{ outputmoney(incomeSubtotalDebits+incomeSubtotalCredits) }}</div>
                </th>
              </tr>
              </thead>
            </table>
          </div>
        </div>

        <!-- 支出部分 -->
        <div class="detail-block">
          <div class="detail-table">
            <table>
              <tbody>
              <tr>
                <td></td>
                <td><span class="font-bold">Debits</span></td>
                <td><span class="font-bold">Credits</span></td>
              </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Seller-fulfilled selling fees <span v-if="formData['Seller_fulfilled selling fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Seller_fulfilled selling fees_rmb'])}})</span></td>
                  <td><span v-if="formData['Seller_fulfilled selling fees']&&formData['Seller_fulfilled selling fees']<0" >{{outputmoney(formData['Seller_fulfilled selling fees'])}}</span></td>
                  <td><span v-if="formData['Seller_fulfilled selling fees']&&formData['Seller_fulfilled selling fees']>=0" >{{outputmoney(formData['Seller_fulfilled selling fees'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>FBA selling fees <span v-if="formData['FBA selling fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA selling fees_rmb'])}})</span></td>
                  <td><span v-if="formData['FBA selling fees']&&formData['FBA selling fees']<0" >{{outputmoney(formData['FBA selling fees'])}}</span></td>
                  <td><span v-if="formData['FBA selling fees']&&formData['FBA selling fees']>=0" >{{outputmoney(formData['FBA selling fees'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Selling fee refunds <span v-if="formData['Selling fee refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Selling fee refunds_rmb'])}})</span></td>
                  <td><span v-if="formData['Selling fee refunds']&&formData['Selling fee refunds']<0" >{{outputmoney(formData['Selling fee refunds'])}}</span></td>
                  <td><span v-if="formData['Selling fee refunds']&&formData['Selling fee refunds']>=0" >{{outputmoney(formData['Selling fee refunds'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>FBA transaction fees <span v-if="formData['FBA transaction fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA transaction fees_rmb'])}})</span></td>
                  <td><span v-if="formData['FBA transaction fees']&&formData['FBA transaction fees']<0" >{{outputmoney(formData['FBA transaction fees'])}}</span></td>
                  <td><span v-if="formData['FBA transaction fees']&&formData['FBA transaction fees']>=0" >{{outputmoney(formData['FBA transaction fees'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>FBA transaction fee refunds <span v-if="formData['FBA transaction fee refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA transaction fee refunds_rmb'])}})</span></td>
                  <td><span v-if="formData['FBA transaction fee refunds']&&formData['FBA transaction fee refunds']<0" >{{outputmoney(formData['FBA transaction fee refunds'])}}</span></td>
                  <td><span v-if="formData['FBA transaction fee refunds']&&formData['FBA transaction fee refunds']>=0" >{{outputmoney(formData['FBA transaction fee refunds'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Other transaction fees <span v-if="formData['Other transaction fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Other transaction fees_rmb'])}})</span></td>
                  <td><span v-if="formData['Other transaction fees']&&formData['Other transaction fees']<0" >{{outputmoney(formData['Other transaction fees'])}}</span></td>
                  <td><span v-if="formData['Other transaction fees']&&formData['Other transaction fees']>=0" >{{outputmoney(formData['Other transaction fees'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Other transaction fee refunds <span v-if="formData['Other transaction fee refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Other transaction fee refunds_rmb'])}})</span></td>
                  <td><span v-if="formData['Other transaction fee refunds']&&formData['Other transaction fee refunds']<0" >{{outputmoney(formData['Other transaction fee refunds'])}}</span></td>
                  <td><span v-if="formData['Other transaction fee refunds']&&formData['Other transaction fee refunds']>=0" >{{outputmoney(formData['Other transaction fee refunds'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>FBA inventory and inbound services fees <span v-if="formData['FBA inventory and inbound services fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['FBA inventory and inbound services fees_rmb'])}})</span></td>
                  <td><span v-if="formData['FBA inventory and inbound services fees']&&formData['FBA inventory and inbound services fees']<0" >{{outputmoney(formData['FBA inventory and inbound services fees'])}}</span></td>
                  <td><span v-if="formData['FBA inventory and inbound services fees']&&formData['FBA inventory and inbound services fees']>=0" >{{outputmoney(formData['FBA inventory and inbound services fees'])}}</span></td>
                </tr>
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Delivery label purchases <span v-if="formData['Delivery label purchases_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Delivery label purchases_rmb'])}})</span></td>
                  <td><span v-if="formData['Delivery label purchases']&&formData['Delivery label purchases']<0" >{{outputmoney(formData['Delivery label purchases'])}}</span></td>
                  <td><span v-if="formData['Delivery label purchases']&&formData['Delivery label purchases']>=0" >{{outputmoney(formData['Delivery label purchases'])}}</span></td>
                </tr>-->
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Delivery label refunds <span v-if="formData['Delivery label refunds_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Delivery label refunds_rmb'])}})</span></td>
                  <td><span v-if="formData['Delivery label refunds']&&formData['Delivery label refunds']<0" >{{outputmoney(formData['Delivery label refunds'])}}</span></td>
                  <td><span v-if="formData['Delivery label refunds']&&formData['Delivery label refunds']>=0" >{{outputmoney(formData['Delivery label refunds'])}}</span></td>
                </tr>-->
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Carrier delivery label adjustments <span v-if="formData['Carrier delivery label adjustments_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Carrier delivery label adjustments_rmb'])}})</span></td>
                  <td><span v-if="formData['Carrier delivery label adjustments']&&formData['Carrier delivery label adjustments']<0" >{{outputmoney(formData['Carrier delivery label adjustments'])}}</span></td>
                  <td><span v-if="formData['Carrier delivery label adjustments']&&formData['Carrier delivery label adjustments']>=0" >{{outputmoney(formData['Carrier delivery label adjustments'])}}</span></td>
                </tr>-->
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Service fees <span v-if="formData['Service fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Service fees_rmb'])}})</span></td>
                  <td><span v-if="formData['Service fees']&&formData['Service fees']<0" >{{outputmoney(formData['Service fees'])}}</span></td>
                  <td><span v-if="formData['Service fees']&&formData['Service fees']>=0" >{{outputmoney(formData['Service fees'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Refund administration fees <span v-if="formData['Refund administration fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Refund administration fees_rmb'])}})</span></td>
                  <td><span v-if="formData['Refund administration fees']&&formData['Refund administration fees']<0" >{{outputmoney(formData['Refund administration fees'])}}</span></td>
                  <td><span v-if="formData['Refund administration fees']&&formData['Refund administration fees']>=0" >{{outputmoney(formData['Refund administration fees'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Adjustments <span v-if="formData['Adjustments_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Adjustments_rmb'])}})</span></td>
                  <td><span v-if="formData['Adjustments']&&formData['Adjustments']<0" >{{outputmoney(formData['Adjustments'])}}</span></td>
                  <td><span v-if="formData['Adjustments']&&formData['Adjustments']>=0" >{{outputmoney(formData['Adjustments'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Cost of Advertising <span v-if="formData['Cost of Advertising_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Cost of Advertising_rmb'])}})</span></td>
                  <td><span v-if="formData['Cost of Advertising']&&formData['Cost of Advertising']<0" >{{outputmoney(formData['Cost of Advertising'])}}</span></td>
                  <td><span v-if="formData['Cost of Advertising']&&formData['Cost of Advertising']>=0" >{{outputmoney(formData['Cost of Advertising'])}}</span></td>
                </tr>
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Refund for Advertiser <span v-if="formData['Refund for Advertiser_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Refund for Advertiser_rmb'])}})</span></td>
                  <td><span v-if="formData['Refund for Advertiser']&&formData['Refund for Advertiser']<0" >{{outputmoney(formData['Refund for Advertiser'])}}</span></td>
                  <td><span v-if="formData['Refund for Advertiser']&&formData['Refund for Advertiser']>=0" >{{outputmoney(formData['Refund for Advertiser'])}}</span></td>
                </tr>
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Commingling VAT <span v-if="formData['Commingling VAT_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Commingling VAT_rmb'])}})</span></td>
                  <td><span v-if="formData['Commingling VAT']&&formData['Commingling VAT']<0" >{{outputmoney(formData['Commingling VAT'])}}</span></td>
                  <td><span v-if="formData['Commingling VAT']&&formData['Commingling VAT']>=0" >{{outputmoney(formData['Commingling VAT'])}}</span></td>
                </tr>-->
                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Liquidations fees <span v-if="formData['Liquidations fees_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Liquidations fees_rmb'])}})</span></td>
                  <td><span v-if="formData['Liquidations fees']&&formData['Liquidations fees']<0" >{{outputmoney(formData['Liquidations fees'])}}</span></td>
                  <td><span v-if="formData['Liquidations fees']&&formData['Liquidations fees']>=0" >{{outputmoney(formData['Liquidations fees'])}}</span></td>
                </tr>
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Amazon Shipping Charges <span v-if="formData['Amazon Shipping Charges_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Amazon Shipping Charges_rmb'])}})</span></td>
                  <td><span v-if="formData['Amazon Shipping Charges']&&formData['Amazon Shipping Charges']<0" >{{outputmoney(formData['Amazon Shipping Charges'])}}</span></td>
                  <td><span v-if="formData['Amazon Shipping Charges']&&formData['Amazon Shipping Charges']>=0" >{{outputmoney(formData['Amazon Shipping Charges'])}}</span></td>
                </tr>-->
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Receivables Deductions <span v-if="formData['Receivables Deductions_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Receivables Deductions_rmb'])}})</span></td>
                  <td><span v-if="formData['Receivables Deductions']&&formData['Receivables Deductions']<0" >{{outputmoney(formData['Receivables Deductions'])}}</span></td>
                  <td><span v-if="formData['Receivables Deductions']&&formData['Receivables Deductions']>=0" >{{outputmoney(formData['Receivables Deductions'])}}</span></td>
                </tr>-->
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Carrier shipping label adjustments <span v-if="formData['Carrier shipping label adjustments_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Carrier shipping label adjustments_rmb'])}})</span></td>
                  <td><span v-if="formData['Carrier shipping label adjustments']&&formData['Carrier shipping label adjustments']<0" >{{outputmoney(formData['Carrier shipping label adjustments'])}}</span></td>
                  <td><span v-if="formData['Carrier shipping label adjustments']&&formData['Carrier shipping label adjustments']>=0" >{{outputmoney(formData['Carrier shipping label adjustments'])}}</span></td>
                </tr>-->
<!--                <tr v-if="!formData.expenses || formData.expenses.length === 0">
                  <td>Amazon Shipping Charge Adjustments <span v-if="formData['Amazon Shipping Charge Adjustments_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Amazon Shipping Charge Adjustments_rmb'])}})</span></td>
                  <td><span v-if="formData['Amazon Shipping Charge Adjustments']&&formData['Amazon Shipping Charge Adjustments']<0" >{{outputmoney(formData['Amazon Shipping Charge Adjustments'])}}</span></td>
                  <td><span v-if="formData['Amazon Shipping Charge Adjustments']&&formData['Amazon Shipping Charge Adjustments']>=0" >{{outputmoney(formData['Amazon Shipping Charge Adjustments'])}}</span></td>
                </tr>-->
                <tr class="subtotal">
                  <td>subtotals</td>
                  <td>{{ outputmoney(expensesSubtotalDebits) }}</td>
                  <td>{{ outputmoney(expensesSubtotalCredits) }}</td>
                </tr>
              </tbody>
              <thead>
              <tr>
                <th>
                  <h3>Expenses</h3>
                </th>
                <th></th>
                <th>
                  <div class="total-amount negative">{{ outputmoney(expensesSubtotalDebits+expensesSubtotalCredits) }}</div>
                </th>
              </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>

      <!-- Transfers和Tax左右结构 -->
      <div class="detail-row">
        <!-- 转账部分 -->
        <div class="detail-block">
          <div class="detail-table">
            <table>
              <tbody>
              <tr>
                <td></td>
                <td><span class="font-bold">Debits</span></td>
                <td><span class="font-bold">Credits</span></td>
              </tr>
                <tr v-if="!formData.transfers || formData.transfers.length === 0">
                  <td>Transfers to bank account <span v-if="formData['Transfers to bank account_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Transfers to bank account_rmb'])}})</span></td>
                  <td><span v-if="formData['Transfers to bank account']&&formData['Transfers to bank account']<0" >{{outputmoney(formData['Transfers to bank account'])}}</span></td>
                  <td><span v-if="formData['Transfers to bank account']&&formData['Transfers to bank account']>=0" >{{outputmoney(formData['Transfers to bank account'])}}</span></td>
                </tr>
                <tr v-if="!formData.transfers || formData.transfers.length === 0">
                  <td>Failed transfers to bank account <span v-if="formData['Failed transfers to bank account_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Failed transfers to bank account_rmb'])}})</span></td>
                  <td><span v-if="formData['Failed transfers to bank account']&&formData['Failed transfers to bank account']<0" >{{outputmoney(formData['Failed transfers to bank account'])}}</span></td>
                  <td><span v-if="formData['Failed transfers to bank account']&&formData['Failed transfers to bank account']>=0" >{{outputmoney(formData['Failed transfers to bank account'])}}</span></td>
                </tr>
                <tr v-if="!formData.transfers || formData.transfers.length === 0">
                  <td>Charges to credit card and other debt recovery <span v-if="formData['Charges to credit card and other debt recovery_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Charges to credit card and other debt recovery_rmb'])}})</span></td>
                  <td><span v-if="formData['Charges to credit card and other debt recovery']&&formData['Charges to credit card and other debt recovery']<0" >{{outputmoney(formData['Charges to credit card and other debt recovery'])}}</span></td>
                  <td><span v-if="formData['Charges to credit card and other debt recovery']&&formData['Charges to credit card and other debt recovery']>=0" >{{outputmoney(formData['Charges to credit card and other debt recovery'])}}</span></td>
                </tr>
                <tr v-if="!formData.transfers || formData.transfers.length === 0">
                  <td>Disburse to Amazon Gift Card balance <span v-if="formData['Disburse to Amazon Gift Card balance_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Disburse to Amazon Gift Card balance_rmb'])}})</span></td>
                  <td><span v-if="formData['Disburse to Amazon Gift Card balance']&&formData['Disburse to Amazon Gift Card balance']<0" >{{outputmoney(formData['Disburse to Amazon Gift Card balance'])}}</span></td>
                  <td><span v-if="formData['Disburse to Amazon Gift Card balance']&&formData['Disburse to Amazon Gift Card balance']>=0" >{{outputmoney(formData['Disburse to Amazon Gift Card balance'])}}</span></td>
                </tr>
                <tr class="subtotal">
                  <td>subtotals</td>
                  <td>{{ outputmoney(transfersSubtotalDebits) }}</td>
                  <td>{{ outputmoney(transfersSubtotalCredits) }}</td>
                </tr>
              </tbody>
              <thead>
              <tr>
                <th>
                  <h3>Transfers</h3>
                </th>
                <th></th>
                <th>
                  <div class="total-amount negative">{{ outputmoney(transfersSubtotalDebits+transfersSubtotalCredits) }}</div>
                </th>
              </tr>
              </thead>
            </table>
          </div>
        </div>

        <!-- 税务部分 -->
        <div class="detail-block">
          <div class="detail-table">
            <table>
              <tbody>
              <tr>
                <td></td>
                <td><span class="font-bold">Debits</span></td>
                <td><span class="font-bold">Credits</span></td>
              </tr>
                <tr v-if="(!formData.tax || formData.tax.length === 0) && (queryParams.marketcurrency != 'USD'&& queryParams.marketcurrency != 'CAD' && queryParams.marketcurrency != 'MXN')" >
                  <td>Product, delivery and gift wrap taxes collected <span v-if="formData['Product, delivery and gift wrap taxes collected_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Product, delivery and gift wrap taxes collected_rmb'])}})</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes collected']&&formData['Product, delivery and gift wrap taxes collected']<0" >{{outputmoney(formData['Product, delivery and gift wrap taxes collected'])}}</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes collected']&&formData['Product, delivery and gift wrap taxes collected']>=0" >{{outputmoney(formData['Product, delivery and gift wrap taxes collected'])}}</span></td>
                </tr>
                <tr v-if="(!formData.tax || formData.tax.length === 0) && (queryParams.marketcurrency != 'USD'&& queryParams.marketcurrency != 'CAD' && queryParams.marketcurrency != 'MXN')" >
                  <td>Product, delivery and gift wrap taxes refunded <span v-if="formData['Product, delivery and gift wrap taxes refunded_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Product, delivery and gift wrap taxes refunded_rmb'])}})</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes refunded']&&formData['Product, delivery and gift wrap taxes refunded']<0" >{{outputmoney(formData['Product, delivery and gift wrap taxes refunded'])}}</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes refunded']&&formData['Product, delivery and gift wrap taxes refunded']>=0" >{{outputmoney(formData['Product, delivery and gift wrap taxes refunded'])}}</span></td>
                </tr>
                <tr v-if="(!formData.tax || formData.tax.length === 0) && (queryParams.marketcurrency != 'USD'&& queryParams.marketcurrency != 'CAD' && queryParams.marketcurrency != 'MXN')" >
                  <td>Amazon obligated tax withheld <span v-if="formData['Amazon obligated tax withheld_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Amazon obligated tax withheld_rmb'])}})</span></td>
                  <td><span v-if="formData['Amazon obligated tax withheld']&&formData['Amazon obligated tax withheld']<0" >{{outputmoney(formData['Amazon obligated tax withheld'])}}</span></td>
                  <td><span v-if="formData['Amazon obligated tax withheld']&&formData['Amazon obligated tax withheld']>=0" >{{outputmoney(formData['Amazon obligated tax withheld'])}}</span></td>
                </tr>
                <tr v-if="(!formData.tax || formData.tax.length === 0) && (queryParams.marketcurrency === 'USD'|| queryParams.marketcurrency === 'CAD' || queryParams.marketcurrency === 'MXN')" >
                  <td>Product, shipping, gift wrap taxes and regulatory fee collected <span v-if="formData['Product, delivery and gift wrap taxes collected_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Product, delivery and gift wrap taxes collected_rmb'])}})</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes collected']&&formData['Product, delivery and gift wrap taxes collected']<0" >{{outputmoney(formData['Product, delivery and gift wrap taxes collected'])}}</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes collected']&&formData['Product, delivery and gift wrap taxes collected']>=0" >{{outputmoney(formData['Product, delivery and gift wrap taxes collected'])}}</span></td>
                </tr>
                <tr v-if="(!formData.tax || formData.tax.length === 0) && (queryParams.marketcurrency === 'USD'|| queryParams.marketcurrency === 'CAD' || queryParams.marketcurrency === 'MXN')" >
                  <td>Product, shipping, gift wrap taxes and regulatory fee refunded <span v-if="formData['Product, delivery and gift wrap taxes refunded_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Product, delivery and gift wrap taxes refunded_rmb'])}})</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes refunded']&&formData['Product, delivery and gift wrap taxes refunded']<0" >{{outputmoney(formData['Product, delivery and gift wrap taxes refunded'])}}</span></td>
                  <td><span v-if="formData['Product, delivery and gift wrap taxes refunded']&&formData['Product, delivery and gift wrap taxes refunded']>=0" >{{outputmoney(formData['Product, delivery and gift wrap taxes refunded'])}}</span></td>
                </tr>
                <tr v-if="(!formData.tax || formData.tax.length === 0) && (queryParams.marketcurrency === 'USD'|| queryParams.marketcurrency === 'CAD' || queryParams.marketcurrency === 'MXN')" >
                  <td>Amazon Obligated Tax and Regulatory Fee Withheld <span v-if="formData['Amazon obligated tax withheld_rmb']" class="font-extraSmall">(RMB:{{outputmoney(formData['Amazon obligated tax withheld_rmb'])}})</span></td>
                  <td><span v-if="formData['Amazon obligated tax withheld']&&formData['Amazon obligated tax withheld']<0" >{{outputmoney(formData['Amazon obligated tax withheld'])}}</span></td>
                  <td><span v-if="formData['Amazon obligated tax withheld']&&formData['Amazon obligated tax withheld']>=0" >{{outputmoney(formData['Amazon obligated tax withheld'])}}</span></td>
                </tr>
                <tr class="subtotal">
                  <td>subtotals</td>
                  <td>{{ outputmoney(taxSubtotalDebits) }}</td>
                  <td>{{ outputmoney(taxSubtotalCredits) }}</td>
                </tr>
              </tbody>
              <thead>
              <tr>
                <th>
                  <h3>Tax</h3>
                </th>
                <th></th>
                <th>
                  <div class="total-amount">{{ outputmoney(taxSubtotalDebits+taxSubtotalCredits) }}</div>
                </th>
              </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部声明 -->
    <div class="disclaimer">
      <p v-if="queryParams.marketcurrency=='USD' || queryParams.marketcurrency=='CAD' || queryParams.marketcurrency=='MXN'">Information in this statement does not constitute accounting,tax, legal, or other professional advice.</p>
      <div v-else>
        <p>Information in this statement does not constitute accounting, legal, or professional advice.</p>
        <p>All fees listed include any applicable VAT.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,toRefs,computed,nextTick} from 'vue'
import Group from "@/components/header/group.vue";
import { ElMessage, ElIcon } from 'element-plus'
import { Setting } from '@element-plus/icons-vue'
import {outputmoney} from '@/utils/index.js';
import Datepicker from '@/components/header/datepicker.vue';
import settlementApi from '@/api/amazon/finances/settlementApi'
import groupApi from '@/api/amazon/group/groupApi'
import PrinterBtn from "@/components/header/printer.vue";

const state=reactive({
  formData:{},
  queryParams:{
    isload:false,
  },
  searchLoading: false,
  groupInfo: {},
  settingDialogVisible: false,
  saveLoading: false,
  settingForm: {
    groupid: '',
    displayName: '',
    legalName: ''
  }
});
const {
  formData,
  queryParams,
  searchLoading,
  groupInfo,
  settingDialogVisible,
  saveLoading,
  settingForm
} = toRefs(state);

// 计算Income部分的Debits和Credits汇总
const incomeSubtotalDebits = computed(() => {
  let total = 0;
  if (!formData.value.income || formData.value.income.length === 0) {
    const incomeFields = [
      'Seller fulfilled product sales',
      'Seller-fulfilled product sale refunds',
      'FBA product sales',
      'FBA product sale refunds',
      'FBA inventory credit',
      'FBA liquidation proceeds',
      'FBA Liquidations proceeds adjustments',
      'Postage credits',
      'Delivery credit refunds',
      'Gift wrap credits',
      'Gift wrap credit refunds',
      'Promotional rebates',
      'Promotional rebate refunds',
      'A-to-z Guarantee claims',
      'Chargebacks',
      'Amazon Shipping Reimbursement',
      'SAFE-T Reimbursement',
      'Receivables Reversals',
      'Commingling VAT',
      'Product sales (non-FBA)',
      'Product sale refunds (non-FBA)',
      'Shipping credits',
      'Shipping credit refunds',
      'Amazon Shipping Reimbursement Adjustments',
      'Others'
    ];
    incomeFields.forEach(field => {
      const value = formData.value[field];
      if (value && value < 0) {
        total +=value;
      }
    });
  } else {
    formData.value.income.forEach(item => {
      total += item.debits || 0;
    });
  }
  return total;
});

const incomeSubtotalCredits = computed(() => {
  let total = 0;
  if (!formData.value.income || formData.value.income.length === 0) {
    const incomeFields = [
      'Seller fulfilled product sales',
      'Seller-fulfilled product sale refunds',
      'FBA product sales',
      'FBA product sale refunds',
      'FBA inventory credit',
      'FBA liquidation proceeds',
      'FBA Liquidations proceeds adjustments',
      'Postage credits',
      'Delivery credit refunds',
      'Gift wrap credits',
      'Gift wrap credit refunds',
      'Promotional rebates',
      'Promotional rebate refunds',
      'A-to-z Guarantee claims',
      'Chargebacks',
      'Amazon Shipping Reimbursement',
      'SAFE-T Reimbursement',
      'Receivables Reversals',
      'Commingling VAT',
      'Product sales (non-FBA)',
      'Product sale refunds (non-FBA)',
      'Shipping credits',
      'Shipping credit refunds',
      'Amazon Shipping Reimbursement Adjustments',
      "Others"
    ];
    incomeFields.forEach(field => {
      const value = formData.value[field];
      if (value && value >= 0) {
        total += value;
      }
    });
  } else {
    formData.value.income.forEach(item => {
      total += item.credits || 0;
    });
  }
  return total;
});

// 计算Expenses部分的Debits和Credits汇总
const expensesSubtotalDebits = computed(() => {
  let total = formData.value.expensesSubtotalDebits || 0;
  if (!formData.value.expenses || formData.value.expenses.length === 0) {
    const expensesFields = [
      'Seller-fulfilled selling fees',
      'FBA selling fees',
      'Selling fee refunds',
      'FBA transaction fees',
      'FBA transaction fee refunds',
      'Other transaction fees',
      'Other transaction fee refunds',
      'FBA inventory and inbound services fees',
      'Delivery label purchases',
      'Delivery label refunds',
      'Carrier delivery label adjustments',
      'Service fees',
      'Refund administration fees',
      'Adjustments',
      'Cost of Advertising',
      'Refund for Advertiser',
      'Commingling VAT',
      'Liquidations fees',
      'Amazon Shipping Charges',
      'Receivables Deductions',
      'Shipping label purchases',
      'Shipping label refunds',
      'Carrier shipping label adjustments',
      'Amazon Shipping Charge Adjustments'
    ];
    expensesFields.forEach(field => {
      const value = formData.value[field];
      if (value && value < 0) {
        total += value;
      }
    });
  } else {
    formData.value.expenses.forEach(item => {
      total += item.debits || 0;
    });
  }
  return total;
});

const expensesSubtotalCredits = computed(() => {
  let total = formData.value.expensesSubtotalCredits || 0;
  if (!formData.value.expenses || formData.value.expenses.length === 0) {
    const expensesFields = [
      'Seller-fulfilled selling fees',
      'FBA selling fees',
      'Selling fee refunds',
      'FBA transaction fees',
      'FBA transaction fee refunds',
      'Other transaction fees',
      'Other transaction fee refunds',
      'FBA inventory and inbound services fees',
      'Delivery label purchases',
      'Delivery label refunds',
      'Carrier delivery label adjustments',
      'Service fees',
      'Refund administration fees',
      'Adjustments',
      'Cost of Advertising',
      'Refund for Advertiser',
      'Commingling VAT',
      'Liquidations fees',
      'Amazon Shipping Charges',
      'Receivables Deductions',
      'Shipping label purchases',
      'Shipping label refunds',
      'Carrier shipping label adjustments',
      'Amazon Shipping Charge Adjustments'
    ];
    expensesFields.forEach(field => {
      const value = formData.value[field];
      if (value && value >= 0) {
        total += value;
      }
    });
  } else {
    formData.value.expenses.forEach(item => {
      total += item.credits || 0;
    });
  }
  return total;
});

// 计算Transfers部分的Debits和Credits汇总
const transfersSubtotalDebits = computed(() => {
  let total = formData.value.transfersSubtotalDebits || 0;
  if (!formData.value.transfers || formData.value.transfers.length === 0) {
    const transfersFields = [
      'Transfers to bank account',
      'Failed transfers to bank account',
      'Charges to credit card and other debt recovery',
      'Disburse to Amazon Gift Card balance'
    ];
    transfersFields.forEach(field => {
      const value = formData.value[field];
      if (value && value < 0) {
        total += value;
      }
    });
  } else {
    formData.value.transfers.forEach(item => {
      total += item.debits || 0;
    });
  }
  return total;
});

const transfersSubtotalCredits = computed(() => {
  let total = formData.value.transfersSubtotalCredits || 0;
  if (!formData.value.transfers || formData.value.transfers.length === 0) {
    const transfersFields = [
      'Transfers to bank account',
      'Failed transfers to bank account',
      'Charges to credit card and other debt recovery',
      'Disburse to Amazon Gift Card balance'
    ];
    transfersFields.forEach(field => {
      const value = formData.value[field];
      if (value && value >= 0) {
        total += value;
      }
    });
  } else {
    formData.value.transfers.forEach(item => {
      total += item.credits || 0;
    });
  }
  return total;
});

// 计算Tax部分的Debits和Credits汇总
const taxSubtotalDebits = computed(() => {
  let total = formData.value.taxSubtotalDebits || 0;
  if (!formData.value.tax || formData.value.tax.length === 0) {
    const taxFields = [
      'Product, delivery and gift wrap taxes collected',
      'Product, delivery and gift wrap taxes refunded',
      'Amazon obligated tax withheld',
      'Product, shipping, gift wrap taxes and regulatory fee collected',
      'Product, shipping, gift wrap taxes and regulatory fee refunded',
      'Amazon Obligated Tax and Regulatory Fee Withheld'
    ];
    taxFields.forEach(field => {
      const value = formData.value[field];
      if (value && value < 0) {
        total += value;
      }
    });
  } else {
    formData.value.tax.forEach(item => {
      total += item.debits || 0;
    });
  }
  return total;
});

const taxSubtotalCredits = computed(() => {
  let total = formData.value.taxSubtotalCredits || 0;
  if (!formData.value.tax || formData.value.tax.length === 0) {
    const taxFields = [
      'Product, delivery and gift wrap taxes collected',
      'Product, delivery and gift wrap taxes refunded',
      'Amazon obligated tax withheld',
      'Product, shipping, gift wrap taxes and regulatory fee collected',
      'Product, shipping, gift wrap taxes and regulatory fee refunded',
      'Amazon Obligated Tax and Regulatory Fee Withheld'
    ];
    taxFields.forEach(field => {
      const value = formData.value[field];
      if (value && value >= 0) {
        total += value;
      }
    });
  } else {
    formData.value.tax.forEach(item => {
      total += item.credits || 0;
    });
  }
  return total;
});

// 格式化日期函数
function formatDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const month = months[date.getUTCMonth()];
  const day = date.getUTCDate();
  const year = date.getUTCFullYear();
  const hours = date.getUTCHours().toString().padStart(2, '0');
  const minutes = date.getUTCMinutes().toString().padStart(2, '0');
  return `${month} ${day}, ${year} `;
}

function resetForm() {
  state.queryParams.accountId = ''
  state.queryParams.site = ''
  state.queryParams.currency = 'market'
  state.queryParams.datetype = 'settlement'
}
function handleSearch(){
  queryParams.value.isload=true;
  loadData();
}

function loadData(){
  searchLoading.value = true;
  settlementApi.getMonthReport(queryParams.value).then(res => {
      formData.value = res.data;
  }).catch(err => {
    ElMessage.error('网络错误，请稍后重试');
  }).finally(() => {
    searchLoading.value = false;
  });
}

function groupChange(obj){
  state.queryParams.groupid=obj.groupid;
  state.queryParams.marketplaceid=obj.marketplaceid;
  state.queryParams.marketplace_name=obj.market.pointName;
  state.queryParams.marketcurrency=obj.market.currency;
  state.groupInfo = obj.group || {};
  loadGroupInfo(obj.groupid);
  handleSearch();
}

function loadGroupInfo(groupid) {
  if (!groupid) return;
  groupApi.getGroupInfo(groupid).then(res => {
    if (res.data) {
      formData.value.displayName = res.data.displayName;
      formData.value.legalName = res.data.legalName;
      state.settingForm.displayName = res.data.displayName;
      state.settingForm.legalName = res.data.legalName;
    }else{
      formData.value.displayName ="";
      formData.value.legalName ="";
      state.settingForm.displayName ="";
      state.settingForm.legalName ="";
    }
  }).catch(err => {
    console.error('加载店铺信息失败', err);
  });
}

function openSettingDialog() {
  if (!queryParams.value.groupid) {
    ElMessage.warning('请先选择店铺');
    return;
  }
  loadGroupInfo(queryParams.value.groupid);
  state.settingDialogVisible = true;
}

function saveGroupInfo() {
  if (!state.settingForm.groupid) {
    ElMessage.warning('店铺ID不能为空');
    return;
  }
  state.saveLoading = true;
  groupApi.saveGroupInfo(state.settingForm).then(res => {
    ElMessage.success('保存成功');
    state.settingDialogVisible = false;
    formData.value.displayName = state.settingForm.displayName;
    formData.value.legalName = state.settingForm.legalName;
  }).catch(err => {
    ElMessage.error('保存失败，请稍后重试');
  }).finally(() => {
    state.saveLoading = false;
  });
}
//日期改变
function changedate(dates){
  state.queryParams.fromDate=dates.start;
  state.queryParams.endDate=dates.end;
  if(state.queryParams.isload){
    handleSearch();
  }
}

onMounted(()=>{
  //loadData()
})

</script>

<style scoped>
.report-container {
  font-family: Arial, sans-serif;
  width: 100%;
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 10px;
}

.logo-section {
  display: flex;
  align-items: center;
}

.logo {
  width: 50px;
  height: 50px;
  margin-right: 15px;
}

.logo-section h1 {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  color: #000;
}

.logo-section h2 {
  font-size: 16px;
  font-weight: normal;
  margin: 0;
  color: #666;
}

.account-info {
  flex: 1;
  margin-left: 40px;
}

.info-row {
  display: flex;
  margin-bottom: 5px;
}

.label {
  font-weight: bold;
  margin-right: 10px;
  min-width: 100px;
}

.page-info {
  font-size: 14px;
  color: #666;
}

.time-range {
  margin-bottom: 30px;
  padding-bottom: 10px;
  font-size: 14px;
  border-bottom: 1px solid #ddd;
}

.summary-section {
  margin-bottom: 40px;
}

.summary-section h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.summary-section p {
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
}

.summary-table table {
  width: 100%;
  border-collapse: collapse;
}

.summary-table th,
.summary-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.summary-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.details-section {
  margin-bottom: 40px;
}

.detail-row {
  display: flex;
  gap: 20px;
  margin-bottom: 40px;
}

.detail-block {
  flex: 1;
  min-width: 0;
}

.detail-block h3 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #333;
}

.total-amount {
  font-size: 18px;
  font-weight: bold;
  text-align: right;
}

.positive {
  color: #006600;
}

.negative {
  color: #cc0000;
}

.detail-table table {
  width: 100%;
  border-collapse: collapse;
}

.detail-table th,
.detail-table td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #eee;
  font-size: 14px;
}

.detail-table th {
  background-color: #f9f9f9;
  font-weight: bold;
}

.subtotal {
  background-color: #f5f5f5;
  font-weight: bold;
}

.disclaimer {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #ddd;
  font-size: 12px;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .account-info {
    margin-left: 0;
    margin-top: 20px;
  }
  
  .page-info {
    margin-top: 10px;
  }
  
  .summary-table table,
  .detail-table table {
    font-size: 12px;
  }
  
  .summary-table th,
  .summary-table td,
  .detail-table th,
  .detail-table td {
    padding: 6px;
  }
}
</style>