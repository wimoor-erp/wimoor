<template>
  <el-dialog v-model="visible" :title="dialogTitle" width="900px" destroy-on-close>
    <div v-loading="loading" element-loading-text="加载中..." style="min-height: 100px;">
      <template v-if="!loading && logData">
        <!-- 基本信息 -->
        <el-descriptions :column="2" border size="small" style="margin-bottom: 16px;">
          <el-descriptions-item label="模板名称">{{ logData.templateName }}</el-descriptions-item>
          <el-descriptions-item label="模板类型">{{ getFtypeLabel(logData.ftype) }}</el-descriptions-item>
          <el-descriptions-item label="凭证ID">{{ logData.vourchesId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="凭证日期">{{ formatDate(logData.voucherDate) }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ logData.createBy || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(logData.createdTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 日志内容 -->
        <div v-if="logData.datalog">
          <div style="font-weight: bold; margin-bottom: 8px;">凭证生成日志</div>
          
          <!-- 结转损益日志 -->
          <el-table v-if="logData.ftype === 'pnl' || logData.ftype === 'profitloss'" :data="parsedLogs" border size="small">
            <el-table-column prop="entryNo" label="序号" width="70" align="center" />
            <el-table-column prop="direction" label="方向" width="70" align="center">
              <template #default="{ row }">
                <el-tag :type="row.direction === '借' ? 'primary' : 'success'" size="small">
                  {{ row.direction }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="subjectName" label="科目" min-width="200" />
            <el-table-column prop="amount" label="金额" width="150" align="right">
              <template #default="{ row }">
                {{ formatAmount(row.amount) }}
              </template>
            </el-table-column>
          </el-table>

          <!-- 期末调汇日志 -->
          <el-table v-else-if="logData.ftype === 'fct' || logData.ftype === 'exchange'" :data="parsedLogs" border size="small">
            <el-table-column prop="entryNo" label="序号" width="70" align="center" />
            <el-table-column prop="subjectId" label="科目ID" width="100" />
            <el-table-column prop="amount" label="金额" width="150" align="right">
              <template #default="{ row }">
                {{ formatAmount(row.amount) }}
              </template>
            </el-table-column>
            <el-table-column   />
          </el-table>

          <!-- 飞书日志 -->
          <el-table v-else-if="logData.ftype === 'feishu'" :data="parsedLogs" border size="small" max-height="400">
            <el-table-column prop="entryNo" label="序号" width="70" align="center" />
            <el-table-column label="飞书记录" min-width="400">
              <template #default="{ row }">
                <el-popover trigger="hover" width="500" placement="left">
                  <template #reference>
                    <span style="cursor: pointer; color: #409eff;">{{ truncateText(row.record, 60) }}</span>
                  </template>
                  <pre style="max-height: 400px; overflow: auto; white-space: pre-wrap; word-break: break-all;">{{ formatJson(row.record) }}</pre>
                </el-popover>
              </template>
            </el-table-column>
          </el-table>

          <!-- 亚马逊付款日志 -->
          <div v-else-if="logData.ftype === 'amz_payment' || logData.ftype === 'amzpayment'">
            <el-table :data="parsedLogs" border size="small" max-height="500">
              <el-table-column prop="entryNo" label="#" width="50" align="center" />
              <el-table-column label="计算公式" min-width="300">
                <template #default="{ row }">
                  <div style="font-family: monospace; font-size: 13px;">
                    <span style="color: #909399;">{{ row.formulaPrefix }}</span>
                    <span style="color: #409eff; font-weight: bold;">{{ row.formulaExpression }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="变量明细" min-width="350">
                <template #default="{ row }">
                  <div style="display: flex; flex-wrap: wrap; gap: 4px;">
                    <el-tag 
                      v-for="(val, key) in row.variables" 
                      :key="key" 
                      size="small" 
                      :type="Number(val) >= 0 ? 'primary' : 'danger'"
                      effect="plain">
                      {{ key }}: {{ formatAmount(val) }}
                    </el-tag>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="计算结果" width="130" align="right">
                <template #default="{ row }">
                  <span :style="{ color: row.result >= 0 ? '#67c23a' : '#f56c6c', fontWeight: 'bold' }">
                    {{ formatAmount(row.result) }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 通用日志（未知类型） -->
          <div v-else style="padding: 12px; background: #f5f7fa; border-radius: 4px;">
            <pre style="white-space: pre-wrap; word-break: break-all; margin: 0;">{{ logData.datalog }}</pre>
          </div>
        </div>

        <div v-else style="text-align: center; padding: 20px; color: #909399;">
          暂无日志数据
        </div>
      </template>
    </div>
    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue';
import { getVoucherLog } from '@/api/finance/closing_template.js';

const visible = ref(false);
const loading = ref(false);
const logData = ref(null);

const dialogTitle = computed(() => {
  if (logData.value) {
    return `凭证生成日志 - ${logData.value.templateName}`;
  }
  return '凭证生成日志';
});

// 模板类型标签
const ftypeMap = {
  'pnl': '结转损益',
  'profitloss': '结转损益',
  'fct': '期末调汇',
  'exchange': '期末调汇',
  'feishu': '飞书数据',
  'amzpayment': '亚马逊付款',
  'amz_payment': '亚马逊付款'
};

function getFtypeLabel(ftype) {
  return ftypeMap[ftype] || ftype || '未知';
}

// 解析日志内容
const parsedLogs = computed(() => {
  if (!logData.value || !logData.value.datalog) return [];
  const datalog = logData.value.datalog;
  const ftype = logData.value.ftype;
  
  // 按分号分割每条记录
  const entries = datalog.split(';').filter(e => e.trim());
  
  if (ftype === 'pnl' || ftype === 'profitloss') {
    // 结转损益: 序号-借/贷-科目名-金额
    return entries.map(entry => {
      const parts = entry.split('-');
      if (parts.length >= 4) {
        return {
          entryNo: parts[0],
          direction: parts[1],
          subjectName: parts[2],
          amount: parts[3]
        };
      }
      return { entryNo: '', direction: '', subjectName: entry, amount: '' };
    });
  }
  
  if (ftype === 'fct' || ftype === 'exchange') {
    // 期末调汇: 序号-科目ID-金额
    return entries.map(entry => {
      const parts = entry.split('-');
      if (parts.length >= 3) {
        return {
          entryNo: parts[0],
          subjectId: parts[1],
          amount: parts[2]
        };
      }
      return { entryNo: '', subjectId: '', amount: entry };
    });
  }
  
  if (ftype === 'feishu') {
    // 飞书: 序号-记录JSON
    return entries.map(entry => {
      const dashIndex = entry.indexOf('-');
      if (dashIndex > 0) {
        return {
          entryNo: entry.substring(0, dashIndex),
          record: entry.substring(dashIndex + 1)
        };
      }
      return { entryNo: '', record: entry };
    });
  }
  
  if (ftype === 'amz_payment' || ftype === 'amzpayment') {
    // 亚马逊付款: 序号-[Expression:公式](变量=值)(变量=值)
    return entries.map(entry => {
      const entryNoMatch = entry.match(/^(\d+)-/);
      const entryNo = entryNoMatch ? entryNoMatch[1] : '';
      const expressionMatch = entry.match(/\[Expression:([^\]]+)\]/);
      const expression = expressionMatch ? expressionMatch[1] : '';
      const variables = {};
      const varRegex = /\(([^=]+)=([^)]+)\)/g;
      let varMatch;
      while ((varMatch = varRegex.exec(entry)) !== null) {
        variables[varMatch[1]] = parseFloat(varMatch[2]);
      }
      
      // 计算结果
      let result = 0;
      try {
        let evalExpr = expression;
        for (const [key, val] of Object.entries(variables)) {
          evalExpr = evalExpr.replace(new RegExp(key.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'g'), `(${val})`);
        }
        // 安全计算
        result = Function('"use strict"; return (' + evalExpr + ')')();
      } catch (e) {
        result = 0;
      }
      
      // 格式化表达式显示
      const formulaPrefix = `第${entryNo}行: `;
      const formulaExpression = expression;
      
      return { entryNo, formulaPrefix, formulaExpression, variables, result };
    });
  }
  
  return entries.map((entry, index) => ({ entryNo: index + 1, content: entry }));
});

// 格式化金额
function formatAmount(amount) {
  if (!amount) return '0.00';
  return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

// 格式化日期
function formatDate(date) {
  if (!date) return '-';
  return new Date(date).toLocaleDateString('zh-CN');
}

// 格式化日期时间
function formatDateTime(date) {
  if (!date) return '-';
  return new Date(date).toLocaleString('zh-CN');
}

// 截断文本
function truncateText(text, maxLen) {
  if (!text) return '';
  return text.length > maxLen ? text.substring(0, maxLen) + '...' : text;
}

// 格式化JSON
function formatJson(str) {
  try {
    return JSON.stringify(JSON.parse(str), null, 2);
  } catch (e) {
    return str;
  }
}

// 打开弹窗
function open(templateId, period) {
  visible.value = true;
  loading.value = true;
  logData.value = null;
  
  getVoucherLog(templateId, period).then(response => {
    if (response && response.code === 200) {
      logData.value = response.data;
    }
  }).finally(() => {
    loading.value = false;
  });
}

defineExpose({ open });
</script>
