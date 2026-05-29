<template >

  <div class="app-container"   >
    <div :style="widthStyle"  >
      <div class="action-bar" >
        <el-space>
          <el-button type="primary" @click="saveAndNew">保存并新增</el-button>
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="saveDraft">暂存</el-button>
          <el-dropdown @command="handleDropdownCommand">
            <el-button>模板</el-button>
            <el-dropdown-menu>
              <el-dropdown-item command="template1">模板1</el-dropdown-item>
              <el-dropdown-item command="template2">模板2</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <!--      <el-dropdown @command="handlePreferences">
                  <el-button>偏好设置</el-button>
                </el-dropdown>-->
        </el-space>
        <div class="action-right">
          <el-button type="text" @click="showKeyboardShortcuts">快捷键</el-button>
          <el-button type="text" @click="toggleFullScreen">大屏</el-button>
          <el-button type="text" @click="prevVoucher" :disabled="queryParams.pageNum+1>total&&queryParams.pageNum!=0">‹</el-button>
          <el-button type="text" @click="nextVoucher" :disabled="queryParams.pageNum<1">›</el-button>
        </div>
      </div>

      <el-form ref="vouchersRef" :model="form" :rules="rules" label-width="0" >
        <div :style="colorStyle">
          <div class="voucher-header" >
            <div class="voucher-main-info">
              <div class="voucher-type-info">
                <span class="voucher-type-label">凭证字</span>
                <el-select v-model="form.voucherType"
                           placeholder="记"
                           @change="handleNextVoucherNo"
                           style="width:70px;margin-right: 5px; border-radius: 0; height: 30px;">
                  <el-option label="记" value="记" key="记"></el-option>
                  <el-option label="收" value="收" key="收"></el-option>
                  <el-option label="付" value="付" key="付"></el-option>
                  <el-option label="转" value="转" key="转"></el-option>
                </el-select>
                <span class="voucher-no-label">号</span>
                <el-input v-model="form.voucherNo" placeholder="001" style="width: 50px; text-align: center; height: 30px; border-radius: 0; margin-right: 30px;" />
                <div class="voucher-date-info">
                  <span class="voucher-date-label">日期</span>
                  <el-date-picker
                      v-model="form.voucherDate"
                      type="date"
                      value-format="YYYY-MM-DD"
                      style="width: 130px; height: 30px; border-radius: 0;"
                  >
                  </el-date-picker>
                </div>
              </div>
              <div class="voucher-period-info">
                <h2 v-if="form.voucherType==='记'">记账凭证</h2>
                <h2 v-if="form.voucherType==='收'">收款凭证</h2>
                <h2 v-if="form.voucherType==='付'">付款凭证</h2>
                <h2 v-if="form.voucherType==='转'">转账凭证</h2>
                <div class="voucher-period">{{ new Date(form.voucherDate).getFullYear() }}年第{{ (new Date(form.voucherDate).getMonth() + 1)  }}期</div>
              </div>
              <div class="voucher-attachment-info">
                <el-button type="text" @click="searchVoucher" size="small">
                  <el-icon><Search /></el-icon>
                </el-button>
                <span>附件{{form.files?form.files.length:0}}张</span>
                <el-button type="primary" link @click="uploadAttachment" size="small">
                  <el-icon><Upload /></el-icon>上传附件
                </el-button>
              </div>
            </div>
          </div>

          <div class="wimoor-voucher-table-wrapper" >
            <table class="wimoor-voucher-table">
              <thead>
              <tr>
                <th class="row-number-col">序号</th>
                <th class="summary-col">摘要</th>
                <th class="account-col">科目</th>
                <th  >
                  <div class="amountHeaderWrapper--3TtBf"><div class="amountTitle--146pZ">借方金额</div><div class="amountUnit--XSR71"><div class="amountLineBgWrapper--1MZC6" style="font-size: 12px; font-weight: 400;"><div class="amountLine--2qaEF">亿</div><div class="amountLine--2qaEF">千</div><div class="amountLine--2qaEF">百</div><div class="amountLine--2qaEF">十</div><div class="amountLine--2qaEF">万</div><div class="amountLine--2qaEF">千</div><div class="amountLine--2qaEF">百</div><div class="amountLine--2qaEF">十</div><div class="amountLine--2qaEF">元</div><div class="amountLine--2qaEF">角</div><div class="amountLine--2qaEF">分</div></div></div></div>
                </th>
                <th >
                  <div class="amountHeaderWrapper--3TtBf"><div class="amountTitle--146pZ">贷方金额</div><div class="amountUnit--XSR71"><div class="amountLineBgWrapper--1MZC6" style="font-size: 12px; font-weight: 400;"><div class="amountLine--2qaEF">亿</div><div class="amountLine--2qaEF">千</div><div class="amountLine--2qaEF">百</div><div class="amountLine--2qaEF">十</div><div class="amountLine--2qaEF">万</div><div class="amountLine--2qaEF">千</div><div class="amountLine--2qaEF">百</div><div class="amountLine--2qaEF">十</div><div class="amountLine--2qaEF">元</div><div class="amountLine--2qaEF">角</div><div class="amountLine--2qaEF">分</div></div></div></div>
                </th>
              </tr>
              </thead>
              <tbody>
              <tr :id="index" v-for="(item, index) in form.entries" :key="index" @keydown="handleKeydown($event, index)" class="voucher-row">
                <td class="row-number">
                  <el-space class="row-actions">
                    <el-button  link size="small" @click="insertRow(index)" icon="Plus" title="插入行"></el-button>
                    <el-button link size="small" @click="copyRow(index)" icon="CopyDocument" title="复制行"></el-button>
                    <el-button link size="small" @click="removeItem(index)" icon="Delete" :disabled="form.entries.length <= 1" title="删除行"></el-button>
                  </el-space>

                  <div class="row-number-text">
                    {{ index + 1 }}
                  </div>

                </td>
                <td class="summary-col">
                  <el-row>
                    <el-col :span="21">
                      <el-input
                          v-model="item.summary"
                          @keyup.enter="focusNextCell($event, 'account', index)"
                          style="border: none; box-shadow: none; padding: 0; height: 60px;width:100%; font-size: 13px;"
                          class="wimoor-input">
                      </el-input>
                    </el-col>
                    <el-col :span="3">
                      <div style="padding-top:20px;">
                        <el-button
                            circle
                            size="small"
                            class="row-actions"
                            @click="showSummarySuggestions(index)"
                            title="摘要选择"
                        >
                          <el-icon   ><MoreFilled /></el-icon>
                        </el-button>
                      </div>
                    </el-col>

                  </el-row>
                </td>
                <td class="account-col">
                  <el-select
                      v-model="item.subjectId"
                      placeholder=""
                      filterable
                      remote
                      :remote-method="queryAccount"
                      @focus="loadAccountList();clearEdit()"
                      @keyup.enter="focusNextCell($event, 'debit', index)"
                      style="width: 100%;box-shadow: none; border: none; padding: 0; height: 60px; font-size: 13px;"
                  >
                    <el-option
                        v-for="account in accountList"
                        :key="account.subjectCode"
                        :label="`${account.subjectCode} ${account.subjectName}`"
                        :value="account.subjectId"
                    >
                      <div class="account-option">
                        <span>{{ account.subjectCode }}</span>
                        <span>{{ account.subjectName }}</span>
                        <span v-if="account.balance !== undefined" class="account-balance">余额: {{ formatCurrency(account.balance) }}</span>
                      </div>
                    </el-option>
                  </el-select>
                </td>
                <td class="debit-col"  @click.stop="e=>handleEditTrue(e,item,'debitEdit',index)">
                  <div class="amount-input-wrapper" v-if="item.debitEdit">
                    <el-input
                        clearable
                        v-model.number="item.debitAmount"
                        @blur="item.debitEdit=false"
                        @click.stop="item.debitEdit=true"
                        @input="calculateTotal(item)"
                        @clear="calculateTotal(item)"
                        @keyup.enter="focusNextCell($event, 'credit', index)"
                        @keyup.tab="focusNextCell($event, 'credit', index)"
                        style="border: none; box-shadow: none; padding: 0; text-align: right; height: 100%; font-size: 13px;"
                        class="wimoor-input"
                    />
                  </div>
                  <div v-else class="amountHeaderWrapper" >
                    <div class="amountUnit--XSR71">
                      <div v-if="item.debitAmountChinese&&item.debitAmountChinese.length>0" class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 400;">
                        <div class="amountLine--2qaEF" title="亿" >{{item.debitAmountChinese[0]}}</div>
                        <div class="amountLine--2qaEF" title="千" >{{item.debitAmountChinese[1]}}</div>
                        <div class="amountLine--2qaEF" title="百" >{{item.debitAmountChinese[2]}}</div>
                        <div class="amountLine--2qaEF" title="十" >{{item.debitAmountChinese[3]}}</div>
                        <div class="amountLine--2qaEF" title="万" >{{item.debitAmountChinese[4]}}</div>
                        <div class="amountLine--2qaEF" title="千" >{{item.debitAmountChinese[5]}}</div>
                        <div class="amountLine--2qaEF" title="百" >{{item.debitAmountChinese[6]}}</div>
                        <div class="amountLine--2qaEF" title="十" >{{item.debitAmountChinese[7]}}</div>
                        <div class="amountLine--2qaEF" title="元" >{{item.debitAmountChinese[8]}}</div>
                        <div class="amountLine--2qaEF" title="角" >{{item.debitAmountChinese[9]}}</div>
                        <div class="amountLine--2qaEF" title="分" >{{item.debitAmountChinese[10]}}</div>
                      </div>
                      <div v-else class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 400;">
                        <div class="amountLine--2qaEF" title="亿" ></div>
                        <div class="amountLine--2qaEF" title="千" ></div>
                        <div class="amountLine--2qaEF" title="百" ></div>
                        <div class="amountLine--2qaEF" title="十" ></div>
                        <div class="amountLine--2qaEF" title="万" ></div>
                        <div class="amountLine--2qaEF" title="千" ></div>
                        <div class="amountLine--2qaEF" title="百" ></div>
                        <div class="amountLine--2qaEF" title="十" ></div>
                        <div class="amountLine--2qaEF" title="元" ></div>
                        <div class="amountLine--2qaEF" title="角" ></div>
                        <div class="amountLine--2qaEF" title="分" ></div>
                      </div>
                    </div>
                  </div>
                </td>
                <td class="credit-col" @click.stop="e=>handleEditTrue(e,item,'creditEdit',index)">
                  <div class="amount-input-wrapper" v-if="item.creditEdit">
                    <el-input
                        v-model.number="item.creditAmount"
                        @blur="item.creditEdit=false"
                        clearable
                        @click.stop="item.creditEdit=true"
                        @clear="calculateTotal(item)"
                        @input="calculateTotal(item)"
                        @keyup.enter="focusNextRow(index)"
                        style="border: none; box-shadow: none; padding: 0; text-align: right; height: 100%; font-size: 13px;"
                        class="wimoor-input"
                    />
                  </div>
                  <div v-else class="amountHeaderWrapper" >
                    <div class="amountUnit--XSR71">
                      <div v-if="item.creditAmountChinese&&item.creditAmountChinese.length>10" class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 400;">
                        <div class="amountLine--2qaEF" title="亿" >{{item.creditAmountChinese[0]}}</div>
                        <div class="amountLine--2qaEF" title="千" >{{item.creditAmountChinese[1]}}</div>
                        <div class="amountLine--2qaEF" title="百" >{{item.creditAmountChinese[2]}}</div>
                        <div class="amountLine--2qaEF" title="十" >{{item.creditAmountChinese[3]}}</div>
                        <div class="amountLine--2qaEF" title="万" >{{item.creditAmountChinese[4]}}</div>
                        <div class="amountLine--2qaEF" title="千" >{{item.creditAmountChinese[5]}}</div>
                        <div class="amountLine--2qaEF" title="百" >{{item.creditAmountChinese[6]}}</div>
                        <div class="amountLine--2qaEF" title="十" >{{item.creditAmountChinese[7]}}</div>
                        <div class="amountLine--2qaEF" title="元" >{{item.creditAmountChinese[8]}}</div>
                        <div class="amountLine--2qaEF" title="角" >{{item.creditAmountChinese[9]}}</div>
                        <div class="amountLine--2qaEF" title="分" >{{item.creditAmountChinese[10]}}</div>
                      </div>
                      <div  v-else class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 400;">
                        <div class="amountLine--2qaEF" title="亿" ></div>
                        <div class="amountLine--2qaEF" title="千" ></div>
                        <div class="amountLine--2qaEF" title="百" ></div>
                        <div class="amountLine--2qaEF" title="十" ></div>
                        <div class="amountLine--2qaEF" title="万" ></div>
                        <div class="amountLine--2qaEF" title="千" ></div>
                        <div class="amountLine--2qaEF" title="百" ></div>
                        <div class="amountLine--2qaEF" title="十" ></div>
                        <div class="amountLine--2qaEF" title="元" ></div>
                        <div class="amountLine--2qaEF" title="角" ></div>
                        <div class="amountLine--2qaEF" title="分" ></div>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
              </tbody>
              <tfoot>
              <tr class="total-row">
                <td colspan="2">
                  <el-button
                      type="primary"
                      size="small"
                      @click="autoBalance"
                      v-if="!isBalance"
                      style="border-radius: 0; height: 28px; font-size: 12px; margin-left: 5px;"
                  >    <svg-icon icon-class="calculator-white"  style="color:#fff;width:14px;height:14px" />自动平衡</el-button>
                </td>
                <td class="total-label">合计</td>
                <td class="amountHeaderWrapper">
                  <div class="amountUnit--XSR71">
                    <div v-if="form.debitTotalChinese&&form.debitTotalChinese.length>10" class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 600;">
                      <div class="amountLine--2qaEF" title="亿" >{{form.debitTotalChinese[0]}}</div>
                      <div class="amountLine--2qaEF" title="千" >{{form.debitTotalChinese[1]}}</div>
                      <div class="amountLine--2qaEF" title="百" >{{form.debitTotalChinese[2]}}</div>
                      <div class="amountLine--2qaEF" title="十" >{{form.debitTotalChinese[3]}}</div>
                      <div class="amountLine--2qaEF" title="万" >{{form.debitTotalChinese[4]}}</div>
                      <div class="amountLine--2qaEF" title="千" >{{form.debitTotalChinese[5]}}</div>
                      <div class="amountLine--2qaEF" title="百" >{{form.debitTotalChinese[6]}}</div>
                      <div class="amountLine--2qaEF" title="十" >{{form.debitTotalChinese[7]}}</div>
                      <div class="amountLine--2qaEF" title="元" >{{form.debitTotalChinese[8]}}</div>
                      <div class="amountLine--2qaEF" title="角" >{{form.debitTotalChinese[9]}}</div>
                      <div class="amountLine--2qaEF" title="分" >{{form.debitTotalChinese[10]}}</div>
                    </div>
                    <div v-else class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 600;">
                      <div class="amountLine--2qaEF" title="亿" ></div>
                      <div class="amountLine--2qaEF" title="千" ></div>
                      <div class="amountLine--2qaEF" title="百" ></div>
                      <div class="amountLine--2qaEF" title="十" ></div>
                      <div class="amountLine--2qaEF" title="万" ></div>
                      <div class="amountLine--2qaEF" title="千" ></div>
                      <div class="amountLine--2qaEF" title="百" ></div>
                      <div class="amountLine--2qaEF" title="十" ></div>
                      <div class="amountLine--2qaEF" title="元" ></div>
                      <div class="amountLine--2qaEF" title="角" ></div>
                      <div class="amountLine--2qaEF" title="分" ></div>

                    </div>
                  </div>
                </td>
                <td class="amountHeaderWrapper">
                  <div class="amountUnit--XSR71">
                    <div  v-if="form.creditTotalChinese&&form.debitTotalChinese.length>10" class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 600;">
                      <div class="amountLine--2qaEF" title="亿">{{form.creditTotalChinese[0]}}</div>
                      <div class="amountLine--2qaEF" title="千">{{form.creditTotalChinese[1]}}</div>
                      <div class="amountLine--2qaEF" title="百">{{form.creditTotalChinese[2]}}</div>
                      <div class="amountLine--2qaEF" title="十">{{form.creditTotalChinese[3]}}</div>
                      <div class="amountLine--2qaEF" title="万">{{form.creditTotalChinese[4]}}</div>
                      <div class="amountLine--2qaEF" title="千">{{form.creditTotalChinese[5]}}</div>
                      <div class="amountLine--2qaEF" title="百">{{form.creditTotalChinese[6]}}</div>
                      <div class="amountLine--2qaEF" title="十">{{form.creditTotalChinese[7]}}</div>
                      <div class="amountLine--2qaEF" title="元">{{form.creditTotalChinese[8]}}</div>
                      <div class="amountLine--2qaEF" title="角">{{form.creditTotalChinese[9]}}</div>
                      <div class="amountLine--2qaEF" title="分">{{form.creditTotalChinese[10]}}</div>
                    </div>
                    <div  v-else class="amountLineBgWrapper--1MZC6" style="font-size: 12px;height:60px; font-weight: 600;">
                      <div class="amountLine--2qaEF" title="亿"></div>
                      <div class="amountLine--2qaEF" title="千"></div>
                      <div class="amountLine--2qaEF" title="百"></div>
                      <div class="amountLine--2qaEF" title="十"></div>
                      <div class="amountLine--2qaEF" title="万"></div>
                      <div class="amountLine--2qaEF" title="千"></div>
                      <div class="amountLine--2qaEF" title="百"></div>
                      <div class="amountLine--2qaEF" title="十"></div>
                      <div class="amountLine--2qaEF" title="元"></div>
                      <div class="amountLine--2qaEF" title="角"></div>
                      <div class="amountLine--2qaEF" title="分"></div>
                    </div>
                  </div>
                </td>
              </tr>
              </tfoot>
            </table>
          </div>

          <div class="wimoor-voucher-footer">
            <div class="voucher-signatures">
              <div class="signature-item">
                <span class="signature-label">制单人:</span>
               <!-- {{nickName}} -->
                <el-button type="text" size="small" title="选择用户">
                  <el-icon><User /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </el-form>


      <el-dialog
          v-model="showSummaryDialog"
          title="常用摘要"
          width="400px"
      >
        <div class="summary-list">
          <div
              v-for="summary in commonSummaries"
              :key="summary"
              class="summary-item"
              @click="selectSummary(currentSummaryIndex, summary)"
          >
            {{ summary }}
          </div>
        </div>
      </el-dialog>
      <UploadDialog ref="fileUpload" @change="handleFilesChange" type="finance" :file-type="['pdf','jpg','png']"></UploadDialog>
    </div>
  </div>

</template>

<script setup name="VouchersEdit">
import {
  listVouchers,
  getVouchers,
  addVouchers,
  updateVouchers,
  nextVoucherNo,
  updateVouchersFiles
} from "@/api/finance/vouchers"
import { ref, reactive, toRefs, onMounted,nextTick } from "vue"
import { Search, Upload, User, Delete, Download ,MoreFilled} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import {listSubjects} from "@/api/finance/subjects.js";
import finStore from "@/hooks/store/useFinanceStore.js"
import UploadDialog from "@/components/FileUpload/UploadDialog.vue";
import $ from 'jquery'; // Local import
const router = useRouter()
const route = useRoute()
// const { proxy } = getCurrentInstance()
// 使用标准API替代proxy
const showMessage = (message, type = 'info') => {
  ElMessage({ message, type })
}

const showSuccess = (message) => showMessage(message, 'success')
const showWarning = (message) => showMessage(message, 'warning')
const showError = (message) => showMessage(message, 'error')

const showAlert = (options) => {
  return ElMessageBox.alert(options.message, options.title || '提示', {
    confirmButtonText: '确定',
    callback: options.callback
  })
}

const showConfirm = (message, title = '确认') => {
  return ElMessageBox.confirm(message, title, {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
}

const fileUpload=ref();
const isBalance = ref(true)
const accountList = ref([]) // 会计科目列表
const showSummaryDialog = ref(false) // 摘要选择对话框
const currentSummaryIndex = ref(-1) // 当前摘要输入行索引
const commonSummaries = ref([ // 常用摘要
  '购买办公用品',
  '支付水电费',
  '发放工资',
  '销售收入',
  '报销差旅费',
  '银行存款利息',
  '支付租金',
  '支付税费'
])

const vouchersRef = ref(null) // 表单引用
const data = reactive({
  form: {
    fileds:0,
    voucherId: null,
    tenantId: null,
    voucherType: '记', // 凭证字
    voucherNo: null,
    voucherDate: new Date().toISOString().split('T')[0], // 默认当前日期
    attachmentCount: 0,
    totalAmount: 0,
    debitTotal:0,
    creditTotal:0,
    debitTotalChinese:{},
    creditTotalChinese:{},
    voucherStatus: 1, // 默认状态
    preparerId: null,
    auditorId: null,
    postUserId: null,
    postTime: null,
    createdTime: null,
    updatedTime: null,
    files:[],
    entries: [
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') },
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') },
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') },
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') }
    ]
  },
  queryParams: {
    pageNum: 0,
    pageSize: 1,
    voucherNo: null,
    voucherDate: null,
    attachmentCount: null,
    totalAmount: null,
    voucherStatus: null,
    preparerId: null,
    auditorId: null,
    postTime: null,
    createdTime: null,
    updatedTime: null
  },
  total:0,
  rules:{
    voucherNo: [
      { required: true, message: "凭证编号不能为空", trigger: "blur" }
    ],
    voucherDate: [
      { required: true, message: "凭证日期不能为空", trigger: "blur" }
    ],
    totalAmount: [
      { required: true, message: "凭证总金额不能为空", trigger: "blur" }
    ],
    preparerId: [
      { required: true, message: "制单人不能为空", trigger: "blur" }
    ],
  },
  "widthStyle":"width:60%",
  "colorStyle":"background-color:#fff;color:#000"
})

const { queryParams, total,form, rules,subjects,widthStyle,colorStyle } = toRefs(data)





// 页面加载时获取数据和初始化
onMounted(() => {
  // 设置当前用户为制单人
  form.preparerId = '当前用户'
  loadAccountList();
  // 如果有voucherId参数，则加载凭证数据
  const voucherId = route.query?.voucherId
  if (voucherId) {
    getVoucherDetail(voucherId)
  }else{
    handleNextVoucherNo();
  }
  nextTick(()=>{
    document.addEventListener('click', function(event) {
      clearEdit();
    });
  })
})
function handleEditTrue(e,item,type,index){
  const evt = e || window.e || window.event;
  e.stopPropagation();
  clearEdit();
  item[type] = true;
  nextTick(()=>{
    setTimeout(function(){
      if(type.indexOf("debit")>=0){
        $(".voucher-row").eq(index).find(".debit-col input").focus()
      }else{
        $(".voucher-row").eq(index).find(".credit-col input").focus()
      }
    },300)


  })
}
function handleFilesChange(files){
  if(data.form.voucherId){
    data.form.files = files;
    updateVouchersFiles(data.form).then(() => {
      showSuccess('上传成功');
      fileUpload.value.hide();
    })
  }else{
    data.form.files = files;
    fileUpload.value.hide();
  }

}
async function handleQuery(){
  data.queryParams.groupid=await finStore.getCurrentTenantId();
  if(data.queryParams.pageNum>0){
    listVouchers(data.queryParams).then(res=>{
      data.total=res.total;
      res.rows[0].entries.forEach(item=>{
        if(item.debitAmount&&!item.debitAmountChinese){
          item.debitAmountChinese=getChineseAmountDigits(item.debitAmount);
        }else{
          item.debitAmountChinese={};
        }
        if(item.creditAmount&&!item.creditAmountChinese){
          item.creditAmountChinese=getChineseAmountDigits(item.creditAmount);
        }else{
          item.creditAmountChinese={};
        }
      })
      Object.assign(data.form, res.rows[0])
      if(data.form.entries&&data.form.entries.length<4){
        for(let i=data.form.entries.length;i<4;i++){
          data.form.entries.push({ summary: '', account: '', debitAmount: '', creditAmount: '',creditAmountChinese:{},debitAmountChinese:{} })
        }
      }

      calculateTotal()
    })
  }else{
    reset()
  }

}
// 获取凭证详情
async function getVoucherDetail(id) {
  let groupid=await finStore.getCurrentTenantId();
    listVouchers({"groupid":groupid,"voucherId":id}).then(res=>{
      res.rows[0].entries.forEach(item=>{
        if(item.debitAmount&&!item.debitAmountChinese){
          item.debitAmountChinese=getChineseAmountDigits(item.debitAmount);
        }else{
          item.debitAmountChinese={};
        }
        if(item.creditAmount&&!item.creditAmountChinese){
          item.creditAmountChinese=getChineseAmountDigits(item.creditAmount);
        }else{
          item.creditAmountChinese={};
        }
      })
      Object.assign(data.form, res.rows[0])
      if(data.form.entries&&data.form.entries.length<4){
        for(let i=data.form.entries.length;i<4;i++){
          data.form.entries.push({ summary: '', account: '', debitAmount: '', creditAmount: '',creditAmountChinese:{},debitAmountChinese:{} })
        }
      }
      calculateTotal()
  })
}

function clearEdit(){
  data.form.entries.forEach(item => {
    item.debitEdit = false
    item.creditEdit = false
  })
}
// 表单提交
function submitForm() {
  vouchersRef.value.validate(async (valid) => {
    if (valid) {
      data.form.groupid=await finStore.getCurrentTenantId();
      if (data.form.voucherId != null) {
        updateVouchers(data.form).then(() => {
          showSuccess('修改成功')
        })
      } else {
        addVouchers(data.form).then(() => {
          showSuccess('新增成功')
        })
      }
    }
  })
}
async function handleNextVoucherNo(){
  nextVoucherNo({"voucherType":data.form.voucherType,
    "voucherDate":data.form.voucherDate,
    "groupid":await finStore.getCurrentTenantId()}).then(response => {
    data.form.voucherNo = response.msg;
  })
}
// 保存并新增
function saveAndNew() {
  vouchersRef.value.validate(async (valid) => {
    if (valid) {
      data.form.groupid=await finStore.getCurrentTenantId();
      addVouchers(data.form).then(() => {
        showSuccess('保存成功')
        reset()
      })
    }
  })
}

// 保存草稿
async function saveDraft() {
  data.form.voucherStatus = 0 // 草稿状态
  data.form.groupid=await finStore.getCurrentTenantId();

  if (data.form.voucherId != null) {
    updateVouchers(data.form).then(() => {
      showSuccess('草稿保存成功')
    })
  } else {
    addVouchers(data.form).then(() => {
      showSuccess('草稿保存成功')
    })
  }
}

// 表单重置
function reset() {
  Object.assign(data.form, {
    voucherId: null,
    tenantId: null,
    voucherType: '记',
    voucherNo: null,
    voucherDate: new Date().toISOString().split('T')[0],
    attachmentCount: 0,
    totalAmount: 0,
    voucherStatus: 1,
    preparerId: '当前用户',
    auditorId: null,
    postUserId: null,
    postTime: null,
    createdTime: null,
    updatedTime: null,
    // 添加总额中文数字的初始值
    debitTotalChinese: Array(11).fill(' '),
    creditTotalChinese: Array(11).fill(' '),
    files:[],
    entries: [
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') },
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') },
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') },
      { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') }
    ]
  })
  calculateTotal()
}

// 计算借贷总额
function getChineseAmountDigits(amount) {
  // 修复空值检查逻辑，显式检查undefined和null，允许0值通过
  if (amount === undefined || amount === null || isNaN(amount) || typeof amount !== 'number') {
    return null;
  }

  // 处理负数
  const isNegative = amount < 0;
  const absoluteAmount = Math.abs(amount);

  // 转换为数字并固定两位小数
  const fixedAmount = absoluteAmount.toFixed(2);

  // 分割整数和小数部分
  const [integerPart, decimalPart] = fixedAmount.split('.');

  // 确保整数部分不超过9位，取最后9位并补空格
  const paddedInteger = integerPart.padStart(9, ' ').slice(-9);
  // 组合所有数字位（9位整数 + 2位小数）
  const allDigits = paddedInteger.split('').concat(decimalPart.split(''));

  // 确保数组始终有11个元素
  while (allDigits.length < 11) {
    allDigits.unshift(' ');
  }
  allDigits.splice(11); // 截断过长的数组

  console.log(allDigits)
  return allDigits;
}
function calculateTotal(item) {
  try {
    if(item){
      if(item.debitAmount && !isNaN(parseFloat(item.debitAmount))){
        item.debitAmountChinese = getChineseAmountDigits(parseFloat(item.debitAmount)) || Array(11).fill(' ');
      }else{
        item.debitAmountChinese = Array(11).fill(' ');
      }
      if(item.creditAmount && !isNaN(parseFloat(item.creditAmount))){
        item.creditAmountChinese = getChineseAmountDigits(parseFloat(item.creditAmount)) || Array(11).fill(' ');
      }else{
        item.creditAmountChinese = Array(11).fill(' ');
      }
    }
    data.form.debitTotal = data.form.entries.reduce((total, item) => total + (parseFloat(item.debitAmount) || 0), 0)
    data.form.creditTotal = data.form.entries.reduce((total, item) => total + (parseFloat(item.creditAmount) || 0), 0)
    isBalance.value = Math.abs(data.form.debitTotal - data.form.creditTotal) < 0.01
    data.form.totalAmount = data.form.debitTotal;
    data.form.entries.forEach((item,index) => {
      item.entryNo=index+1;
    })
    data.form.debitTotalChinese = !isNaN(data.form.debitTotal) ? (getChineseAmountDigits(data.form.debitTotal) || Array(11).fill(' ')) : Array(11).fill(' ');
    data.form.creditTotalChinese = !isNaN(data.form.creditTotal) ? (getChineseAmountDigits(data.form.creditTotal) || Array(11).fill(' ')) : Array(11).fill(' ');
  } catch (error) {
    console.error('Error in calculateTotal:', error);
    // 确保在错误情况下仍有有效的默认值
    if(!data.form.debitTotalChinese) data.form.debitTotalChinese = Array(11).fill(' ');
    if(!data.form.creditTotalChinese) data.form.creditTotalChinese = Array(11).fill(' ');
    data.form.entries.forEach(item => {
      if(!item.debitAmountChinese) item.debitAmountChinese = Array(11).fill(' ');
      if(!item.creditAmountChinese) item.creditAmountChinese = Array(11).fill(' ');
    });
  }
}

// 格式化货币
function formatCurrency(value) {
  return Number(value || 0).toFixed(2)
}

// 添加行
// 添加行
function addItem() {
  data.form.entries.push({ summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') })
}

// 插入行
function insertRow(index) {
  data.form.entries.splice(index, 0, { summary: '', account: '', debitAmount: '', creditAmount: '', creditAmountChinese: Array(11).fill(' '), debitAmountChinese: Array(11).fill(' ') })
}

// 复制行
function copyRow(index) {
  const newRow = {...data.form.entries[index]};
  // 重置中文金额数组为11个空格元素
  newRow.debitAmountChinese = Array(11).fill(' ');
  newRow.creditAmountChinese = Array(11).fill(' ');
  // 清空金额值以避免复制原金额
  newRow.debitAmount = '';
  newRow.creditAmount = '';
  data.form.entries.splice(index + 1, 0, newRow);
  calculateTotal();
}

// 删除行
function removeItem(index) {
  if (data.form.entries.length > 1) {
    data.form.entries.splice(index, 1)
    calculateTotal()
  }
}

// 自动平衡
function autoBalance() {
  const diff = (data.form.debitTotal || 0) - (data.form.creditTotal || 0)
  if (Math.abs(diff) > 0.01) {
    // 如果最后一行只有一方有金额，则补充差额到另一方
    const lastItem = data.form.entries[data.form.entries.length - 1]
    if (lastItem.debit > 0 && lastItem.credit === 0) {
      lastItem.credit = diff
    } else if (lastItem.credit > 0 && lastItem.debit === 0) {
      lastItem.debit = -diff
    } else {
      // 否则创建新行来平衡
      data.form.entries.push({
        summary: '自动平衡',
        account: '',
        debitAmount: diff > 0 ? 0 : -diff,
        creditAmount: diff > 0 ? diff : 0 ,
        creditAmountChinese:{},
        debitAmountChinese:{}
      })
    }
    calculateTotal(data.form.entries[data.form.entries.length-1]);
  }
}

// 显示摘要建议
function showSummarySuggestions(index) {
  currentSummaryIndex.value = index
  showSummaryDialog.value = true
}

// 选择摘要
function selectSummary(index, summary) {
  if (index >= 0 && index < data.form.entries.length) {
    data.form.entries[index].summary = summary
  }
  showSummaryDialog.value = false
}

// 复制摘要到下一行
function copySummaryToNext(index) {
  // 可以根据需要实现复制摘要到下一行的功能
}

// 焦点移动到下一个单元格
function focusNextCell(event, cellType, index) {
  // 可以根据需要实现单元格间的焦点移动
}

// 焦点移动到下一行
function focusNextRow(index) {
  // 如果是最后一行，自动添加新行
  if (index === form.entries.length - 1) {
    addItem()
  }
  // 可以根据需要实现行之间的焦点移动
}

function handleKeydown(event, index) {
  // 可以根据需要实现键盘快捷键处理
}

async function loadAccountList() {
  // 模拟加载会计科目数据
  if (accountList.value.length === 0) {
    // 实际项目中应该从API获取
    listSubjects({"groupid":await finStore.getCurrentTenantId()}).then(res=>{
      if(res.data && res.data.length>0){
        accountList.value = res.data;
      }
    });
  }
}

function queryAccount(query) {
  // 可以根据需要实现远程搜索功能

}

function showAccountSelector(index) {
  // 可以根据需要实现账户选择器功能
}

function showCalculator(type, index) {
  // 可以根据需要实现计算器功能
}

function uploadAttachment() {
  // 可以根据需要实现附件上传功能
  fileUpload.value.show(data.form.files);
}

function searchVoucher() {
  // 可以根据需要实现凭证搜索功能
}

function handleDropdownCommand(command) {
  // 可以根据需要实现模板功能
}

function handlePreferences(command) {
  // 可以根据需要实现偏好设置功能
}

function showKeyboardShortcuts() {
  showAlert({
    message: 'F1: 帮助\nF2: 保存\nF3: 新增\nF5: 刷新',
    title: '快捷键说明'
  })
}

function toggleFullScreen() {
  // 可以根据需要实现全屏功能
  if(data.widthStyle==="width:100%"){
    data.widthStyle="width:60%";
  }else{
    data.widthStyle="width:100%";
  }
}



function prevVoucher() {
  // 可以根据需要实现切换到上一张凭证的功能
    data.queryParams.pageNum=data.queryParams.pageNum+1;
    handleQuery();
}

function nextVoucher() {
  // 可以根据需要实现切换到下一张凭证的功能
  data.queryParams.pageNum=data.queryParams.pageNum-1;
  handleQuery();
}

</script>
<style>
.wimoor-voucher-table .el-input__wrapper {
  box-shadow: none !important;
}
.wimoor-voucher-table .el-select__wrapper {
  box-shadow: none !important;
}
.wimoor-voucher-table .el-select__input{
  height:50px;
}
</style>
<style scoped>
.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  margin-bottom: 10px;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}

.action-right {
  display: flex;
  gap: 10px;
}

.voucher-header {
  border: 1px solid #d0d0d0;
  border-bottom: none;
  border-radius: 4px 4px 0 0;
  padding: 15px;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}

.voucher-main-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.voucher-type-info {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.voucher-type-label,
.voucher-no-label,
.voucher-date-label {
  margin-right: 5px;
  color: #606266;
  font-weight: 500;
}

.voucher-period-info {
  text-align: center;
}

.voucher-period-info h2 {
  margin: 0 0 5px 0;
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.voucher-period {
  font-size: 13px;
  color: #606266;
}

.voucher-attachment-info {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.wimoor-voucher-table-wrapper {
  border: 1px solid #000000;
}

.wimoor-voucher-table {
  width: 100%;
  border-collapse: collapse;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}

.wimoor-voucher-table th,
.wimoor-voucher-table td {
  border: 1px solid #cacaca;
}

.wimoor-voucher-table th {
  font-weight: 500;
  color: #303133;
  font-size: 13px;
}

.row-number-col {
  width: 80px;
  text-align: center;
}

.summary-col {
  width: 200px;
}

.account-col {
  width: 250px;
}

.debit-col,
.credit-col {
  width: 180px;
  text-align: center;
}

.amount-header {
  font-size: 13px;
  margin-bottom: 5px;
}

.amount-digits {
  display: flex;
  justify-content: space-around;
  font-size: 10px;
  color: #606266;
}

.voucher-row {
  height: 40px;
}
.voucher-row .row-actions {
  display: none;
  justify-content: center;
  gap: 5px;
  margin-bottom: 2px;
}
.voucher-row:hover .row-actions {
  display: flex;
  justify-content: center;
  gap: 5px;
  margin-bottom: 2px;
}
.voucher-row .row-number-text {
  text-align: center;
  font-size: 13px;
  display: block;
  color: #606266;
}
.voucher-row:hover .row-number-text {
  text-align: center;
  font-size: 13px;
  display: none;
  color: #606266;
}
.wimoor-input,
.wimoor-select {
  border-radius: 0 !important;
}

.wimoor-input:focus,
.wimoor-select:focus {
  box-shadow: none !important;
  border-color: #409eff !important;
}

.summary-suggest-btn,
.account-select-btn {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  padding: 0 5px;
}

.amount-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.amount-calc-btn {
  position: absolute;
  right: 0;
  padding: 0 5px;
}

.total-row {
  font-weight: 500;
}

.total-label {
  text-align: right;
  font-size: 14px;
  color: #303133;
  padding-right: 15px;
}

.total-amount-cell {
  text-align: right;
  font-size: 14px;
  color: #303133;
  padding-right: 15px;
  font-weight: bold;
}

.wimoor-voucher-footer {
  border: 1px solid #d0d0d0;
  border-top: none;
  border-radius: 0 0 4px 4px;
  padding: 12px 15px;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}

.voucher-signatures {
  display: flex;
  gap: 30px;
  align-items: center;
}

.signature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.signature-label {
  font-weight: 500;
  color: #303133;
  min-width: 70px;
  font-size: 13px;
}

/* 会计科目选项样式 */
.account-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 0;
}

.account-option span:nth-child(1) {
  color: #409eff;
  font-weight: 500;
  margin-right: 10px;
  min-width: 80px;
}

.account-option span:nth-child(2) {
  flex: 1;
}

.account-balance {
  color: #67c23a;
  font-size: 12px;
}

/* 摘要选择对话框样式 */
.summary-list {
  max-height: 300px;
  overflow-y: auto;
}

.summary-item {
  padding: 10px 15px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
}

.summary-item:hover {
}

.summary-item:last-child {
  border-bottom: none;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .wimoor-voucher-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .voucher-main-info {
    flex-wrap: wrap;
    gap: 20px;
    width: 100%;
  }

  .voucher-period-info {
    width: 100%;
    text-align: center;
  }

  .voucher-attachment-info {
    width: 100%;
    justify-content: flex-end;
  }
}

.amountLineBgWrapper--1MZC6 {
  position: relative;
  width: 100%;
  height: 100%;
  font-size: 16px;
  font-weight: 700
}

.amountLineBgWrapper--1MZC6.red--3rLFi {
  color: #fb2323;
}

.amountLineBgWrapper--1MZC6 .trillionAmount--1wIli {
  height: 100%;
  text-align: right;
  flex-direction: row;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 4px;
  letter-spacing: 2px
}

.amountLineBgWrapper--1MZC6 .amountValueWrapper--2cduc {
  position: absolute;
  left: 6px;
  top: 0;
  width: 100%;
  letter-spacing: 9px;
  height: 100%;
  z-index: 11;
  display: flex;
  justify-content: flex-end;
  align-items: center
}

.amountLineBgWrapper--1MZC6 .amountLine--2qaEF {
  float: left;
  height: 100%;
  width: 9.0909%;
  position: relative;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center
}

.amountLineBgWrapper--1MZC6 .amountLine--2qaEF.amountLineActive--1i4tk {
  background-color: #5582f3;
  color: #fff
}

.amountLineBgWrapper--1MZC6 .amountLine--2qaEF+.amountLine--2qaEF {
  border-left: 1px solid #e7e9eb
}

.amountLineBgWrapper--1MZC6 .amountLine--2qaEF:nth-of-type(10) {
  border-color: rgba(255,95,31,.3)
}

.amountLineBgWrapper--1MZC6 .amountLine--2qaEF:nth-of-type(10):after {
  content: "";
  position: absolute;
  bottom: -3px;
  left: -1px;
  height: 2px;
  width: 1px;
  background-color: rgba(255,95,31,.3)
}

.amountLineBgWrapper--1MZC6 .amountLine--2qaEF:nth-of-type(4),.amountLineBgWrapper--1MZC6 .amountLine--2qaEF:nth-of-type(7) {
  border-color: rgba(47,149,255,.3)
}

.amountLineBgWrapper--1MZC6 .amountLine--2qaEF:nth-of-type(4):after,.amountLineBgWrapper--1MZC6 .amountLine--2qaEF:nth-of-type(7):after {
  content: "";
  position: absolute;
  bottom: -3px;
  left: -1px;
  height: 2px;
  width: 1px;
  background-color: rgba(47,149,255,.3)
}

.amountHeaderWrapper--3TtBf {
  height: 50px;
  position: relative
}

.amountHeaderWrapper--3TtBf .amountTitle--146pZ {
  height: 50%;
  line-height: 27px;
  text-align: center;
  font-weight: 600
}

.amountHeaderWrapper--3TtBf .amountUnit--XSR71 {
  border-top: 1px solid #e7e9eb;
  height: 50%;
  line-height: 23px;
  text-align: center;
  padding-left: 1px
}

.amountHeaderWrapper {
  height: 50px;
  position: relative
}

.amountHeaderWrapper .amountTitle--146pZ {
  height: 50%;
  line-height: 27px;
  text-align: center;
  font-weight: 600
}

.amountHeaderWrapper .amountUnit--XSR71 {
  height: 50%;
  line-height: 23px;
  text-align: center;
  padding-left: 1px
}

.customHeaderWrapper--3Gf3Q {
  height: 100%;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center
}

.customHeaderWrapper--3Gf3Q .customTitle--3V-lJ {
  width: 100%;
  text-align: center;
  font-weight: 600
}

.customHeaderWrapper--3Gf3Q .customTitleOpt--2TI73 {
  position: absolute;
  top: 18px;
  right: 16px;
  margin-left: 12px
}

.customHeaderWrapper--3Gf3Q .customTitleOptBtn--3bFEA {
  color: #5582f3;
  font-size: 12px;
  font-weight: 400;
  cursor: pointer
}
.ql-bg-purple{
  background-color: #dedede;
  height:calc(100vh - 85px);
}
 
</style>