<template>
  <el-dialog v-model="xmlVisible"
             width="85%"
             top="1vh"
             class="declaration-dialog"
             title="海关申报单维护"
             style="padding: 15px;"
             @closed="handleDialogClose"
  >
    <template #header>
      <el-space>

     <h3 v-if="xmlform.xmlType=='DEC001'">海关申报单维护</h3>
        <h3 v-else>海关申报单草单维护</h3>

        <el-radio-group v-model="xmlform.xmlType" size="small" fill="#409eff">
          <el-radio-button label="报关单" value="DEC001" />
          <el-radio-button label="草单" value="DEC002" />
        </el-radio-group>
      </el-space>
    </template>
    <!-- 海关申报单内容 -->
    <div class="customs-declaration-container">
      <el-tabs v-model="activeTab" class="declaration-tabs">
        <!-- 报关单表头 -->
        <el-tab-pane label="报关单表头" name="header">
          <el-form ref="headerFormRef" :model="declaration.header" label-width="140px" class="header-form">
            <el-row :gutter="20">
              <!-- 左侧列 -->
              <el-col :span="12">
                <el-card shadow="never" class="section-card">
                  <div class="section-header">
                    <h3 class="section-title">基本信息</h3>
                  </div>

                  <el-form-item label="数据中心统一编号：" prop="seqNo">
                    <el-input v-model="declaration.header.seqNo" placeholder="自动生成" disabled />
                  </el-form-item>

                  <el-form-item label="进出口标志：" prop="ieFlag" required>
                    <el-radio-group v-model="declaration.header.ieFlag">
                      <el-radio label="I">进口</el-radio>
                      <el-radio label="E">出口</el-radio>
                    </el-radio-group>
                  </el-form-item>

                  <el-form-item label="申报地海关：" prop="customMaster" required>
                    <CustomsSelect v-model="declaration.header.customMaster" filterable
                                   :options="portCode" valueField="code" labelField="name" type="portCode" placeholder="申报地海关"   />
                  </el-form-item>

                  <el-form-item label="进出口岸：" prop="iePort" required>
                    <CustomsSelect v-model="declaration.header.iePort" filterable
                                   :options="portCode" valueField="code" labelField="name" type="portCode" placeholder="进出口岸"   />
                  </el-form-item>

                  <el-form-item label="贸易方式：" prop="tradeMode">
                    <el-select v-model="declaration.header.tradeMode" placeholder="请选择贸易方式">
                      <el-option label="9610-跨境电商B2C" value="9610" />
                      <el-option label="9710-跨境电商B2B" value="9710" />
                      <el-option label="1210-保税电商" value="1210" />
                      <el-option label="0110-一般贸易" value="0110" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="报关单类型：" prop="entryType">
                    <el-select v-model="declaration.header.entryType" placeholder="请选择报关单类型">
                      <el-option label="M-无纸化通关" value="M" />
                      <el-option label="P-普通报关" value="P" />
                    </el-select>
                  </el-form-item>
                </el-card>

                <el-card shadow="never" class="section-card" style="margin-top: 15px;">
                  <div class="section-header">
                    <h3 class="section-title">企业信息</h3>
                  </div>

                  <el-form-item label="申报单位代码：" prop="agentCode" required>
                    <el-input v-model="declaration.header.agentCode" placeholder="请输入申报单位代码" />
                  </el-form-item>

                  <el-form-item label="申报单位名称：" prop="agentName" required>
                    <el-input v-model="declaration.header.agentName" placeholder="请输入申报单位名称" />
                  </el-form-item>

                  <el-form-item label="收发货人代码：" prop="tradeCode" required>
                    <el-input v-model="declaration.header.tradeCode" placeholder="请输入境内收发货人编号" />
                  </el-form-item>

                  <el-form-item label="收发货人名称：" prop="tradeName" required>
                    <el-input v-model="declaration.header.tradeName" placeholder="请输入境内收发货人名称" />
                  </el-form-item>

                  <el-form-item label="生产销售单位代码：" prop="ownerCode">
                    <el-input v-model="declaration.header.ownerCode" placeholder="请输入消费使用/生产销售单位代码" />
                  </el-form-item>

                  <el-form-item label="生产销售单位名称：" prop="ownerName">
                    <el-input v-model="declaration.header.ownerName" placeholder="请输入消费使用/生产销售单位名称" />
                  </el-form-item>
                </el-card>
              </el-col>

              <!-- 右侧列 -->
              <el-col :span="12">
                <el-card shadow="never" class="section-card">
                  <div class="section-header">
                    <h3 class="section-title">运输信息</h3>
                  </div>

                  <el-form-item label="运输方式：" prop="trafMode">
                    <el-select v-model="declaration.header.trafMode" placeholder="请选择运输方式">
                      <el-option label="2-江海运输" value="2" />
                      <el-option label="3-铁路运输" value="3" />
                      <el-option label="4-汽车运输" value="4" />
                      <el-option label="5-航空运输" value="5" />
                      <el-option label="6-邮件运输" value="6" />
                      <el-option label="9-其他运输" value="9" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="运输工具：" prop="trafName">
                    <el-input v-model="declaration.header.trafName" placeholder="请输入运输工具代码及名称" />
                  </el-form-item>

                  <el-form-item label="提单号：" prop="billNo">
                    <el-input v-model="declaration.header.billNo" placeholder="请输入提单号" />
                  </el-form-item>

                  <el-form-item label="合同号：" prop="contrNo">
                    <el-input v-model="declaration.header.contrNo" placeholder="请输入合同号" />
                  </el-form-item>

                  <el-form-item label="启运国/运抵国：" prop="tradeCountry">
                    <CustomsSelect v-model="declaration.header.tradeCountry" filterable
                                   :options="country" valueField="code" labelField="name" type="country" placeholder="启运国/运抵国"   />
                  </el-form-item>

                  <el-form-item label="经停港/指运港：" prop="distinatePort">
                    <CustomsSelect v-model="declaration.header.distinatePort" filterable
                                   :options="pod" valueField="code" labelField="name" type="pod" placeholder="经停港/指运港"   />
                  </el-form-item>
                </el-card>

                <el-card shadow="never" class="section-card" style="margin-top: 15px;">
                  <div class="section-header">
                    <h3 class="section-title">费用信息</h3>
                  </div>

                  <el-form-item label="成交方式：" prop="transMode">
                    <el-select v-model="declaration.header.transMode" placeholder="请选择成交方式">
                      <el-option label="1-FOB" value="1" />
                      <el-option label="2-CFR" value="2" />
                      <el-option label="3-CIF" value="3" />
                      <el-option label="4-FCA" value="4" />
                      <el-option label="5-CPT" value="5" />
                      <el-option label="6-CIP" value="6" />
                    </el-select>
                  </el-form-item>

                  <el-row :gutter="10">
                    <el-col :span="8">
                      <el-form-item label="运费标记：" prop="feeMark">
                        <el-select v-model="declaration.header.feeMark" placeholder="标记">
                          <el-option label="1-率" value="1" />
                          <el-option label="2-单价" value="2" />
                          <el-option label="3-总价" value="3" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="运费/率：" prop="feeRate">
                        <el-input v-model="declaration.header.feeRate" :min="0" :precision="2"  style="width: 100%"  />
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="运费币制：" prop="feeCurr">
                        <CustomsSelect v-model="declaration.header.feeCurr" :options="currency" valueField="code" labelField="encode" type="currency" placeholder="币制"   />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row :gutter="10">
                    <el-col :span="8">
                      <el-form-item label="保险费标记：" prop="insurMark">
                        <el-select v-model="declaration.header.insurMark" placeholder="标记">
                          <el-option label="1-率" value="1" />
                          <el-option label="2-单价" value="2" />
                          <el-option label="3-总价" value="3" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="保险费/率：" prop="insurRate">
                        <el-input v-model="declaration.header.insurRate" :min="0" :precision="2" style="width: 100%"  />
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="保险费币制：" prop="insurCurr">
                        <CustomsSelect v-model="declaration.header.insurCurr" :options="currency" valueField="code" labelField="encode" type="currency" placeholder="币制"   />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="进出口日期：" prop="ieDate">
                    <el-date-picker
                        v-model="declaration.header.ieDate"
                        type="date"
                        placeholder="选择进出口日期"
                        format="YYYY-MM-DD"
                        value-format="YYYYMMDD"
                        style="width: 100%"
                    />
                  </el-form-item>
                </el-card>
              </el-col>
            </el-row>

            <!-- 货物信息 -->
            <el-card shadow="never" class="section-card" style="margin-top: 15px;">
              <div class="section-header">
                <h3 class="section-title">货物信息</h3>
              </div>

              <el-row :gutter="20">
                <el-col :span="6">
                  <el-form-item label="毛重(kg)：" prop="grossWet">
                    <el-input-number v-model="declaration.header.grossWet" :min="0" :precision="3" style="width: 100%" />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="净重(kg)：" prop="netWt">
                    <el-input-number v-model="declaration.header.netWt" :min="0" :precision="3" style="width: 100%" />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="件数：" prop="packNo">
                    <el-input-number v-model="declaration.header.packNo" :min="0" style="width: 100%" />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="包装种类：" prop="wrapType">
                    <CustomsSelect  :filterable="true"  v-model="declaration.header.wrapType" :options="wrapType" valueField="code" labelField="name" type="wrapType" placeholder="包装种类"   />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="备注：" prop="noteS">
                <el-input
                    v-model="declaration.header.noteS"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入备注信息"
                />
              </el-form-item>
            </el-card>
          </el-form>
        </el-tab-pane>

        <!-- 商品项 -->
        <el-tab-pane label="商品项" name="items">
          <div class="items-container">
            <div class="table-toolbar">
              <el-button type="primary" @click="addItem" icon="Plus">新增商品</el-button>
              <el-button @click="batchDeleteItems" icon="Delete" :disabled="selectedItems.length === 0">批量删除</el-button>
            </div>

            <el-table
                :data="declaration.items"
                border
                style="width: 100%"
                class="items-table"
                @selection-change="handleItemSelectionChange"
            >
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column prop="gNo" label="序号" width="100" align="center">
                <template #default="scope">
                  <el-input-number
                      style="width: 70px"
                      v-model="scope.row.gNo"
                      :min="1"
                      controls-position="right"
                      size="small"
                      @change="updateItemSequence"
                  />
                </template>
              </el-table-column>

              <el-table-column label="商品信息"  >
                <template #default="scope">
                  <div class="item-info">
                    <el-row style="padding-bottom:10px;">
                      <el-col :span="8">
                          <div class="item-row">
                            <span class="item-label">商品编号：</span>
                            <el-input v-model="scope.row.codeTS" placeholder="商品编号" size="small" />
                          </div>
                      </el-col>
                      <el-col :offset="4" :span="12">
                          <div class="item-row">
                            <span class="item-label">商品名称：</span>
                            <el-input v-model="scope.row.gName" placeholder="商品名称" size="small" />
                          </div>
                        </el-col>
                    </el-row>
                    <div class="item-row">
                      <span class="item-label">申报要素：</span>
                      <el-input type="textarea" rows="2" v-model="scope.row.gModel" placeholder="申报要素" size="small" />
                    </div>
                  </div>
                </template>
              </el-table-column>

              <el-table-column label="申报信息" width="250">
                <template #default="scope">
                  <div class="item-info">
                    <div class="item-row">
                      <span class="item-label">数量：</span>
                      <el-input-number
                          v-model="scope.row.gQty"
                          :min="0"
                          :precision="3"
                          size="small"
                          style="width: 120px"
                          @change="updateItemTotal(scope.row)"
                      />
                    </div>
                    <div class="item-row">
                      <span class="item-label">单位：</span>
                      <CustomsSelect v-model="scope.row.gUnit" :options="unit"  size="small"   valueField="code" labelField="name" style="width: 120px" type="unit" placeholder="单位"   />
                    </div>
                    <div class="item-row">
                      <span class="item-label">单价：</span>
                      <el-input-number
                          v-model="scope.row.declPrice"
                          :min="0"
                          :precision="2"
                          size="small"
                          style="width: 120px"
                          @change="updateItemTotal(scope.row)"
                      />
                    </div>
                  </div>
                </template>
              </el-table-column>

              <el-table-column label="总价与产地" width="200">
                <template #default="scope">
                  <div class="item-info">
                    <div class="item-row">
                      <span class="item-label">申报总价：</span>
                      <span class="item-value">{{ scope.row.declTotal || '0.00' }}</span>
                    </div>
                    <div class="item-row">
                      <span class="item-label">原产地：</span>
                      <CustomsSelect v-model="scope.row.originCountry" :options="country"  size="small"   valueField="code" labelField="name" style="width: 120px" type="country" placeholder="原产地"   />

                    </div>
                    <div class="item-row">
                      <span class="item-label">最终目的国：</span>
                      <CustomsSelect v-model="scope.row.destinationCountry" :options="country"  size="small"   valueField="code" labelField="name" style="width: 120px" type="country" placeholder="最终目的国"   />

                    </div>
                  </div>
                </template>
              </el-table-column>

              <el-table-column label="法定计量单位" width="350">
                <template #default="scope">
                  <div class="item-info">
                    <div class="item-row">
                      <span class="item-label">第一数量：</span>
                      <el-space>
                      <el-input-number
                          v-model="scope.row.firstQty"
                          :min="0"
                          :precision="3"
                          size="small"
                          style="width: 120px"
                      />
                      <CustomsSelect v-model="scope.row.firstUnit" :options="unit"  size="small"   valueField="code" labelField="name" style="width: 100px" type="unit" placeholder="单位"   />
                      </el-space>
                    </div>
                    <div class="item-row">
                      <span class="item-label">第二数量：</span>
                      <el-space>
                      <el-input-number
                          v-model="scope.row.secondQty"
                          :min="0"
                          :precision="3"
                          size="small"
                          style="width: 120px"
                      />
                      <CustomsSelect v-model="scope.row.secondUnit" :options="unit"  size="small"   valueField="code" labelField="name" style="width: 100px" type="unit" placeholder="单位"   />
                      </el-space>
                    </div>
                  </div>
                </template>
              </el-table-column>

              <el-table-column label="操作" width="100" align="center" fixed="right">
                <template #default="scope">
                  <el-button link type="primary" @click="editItem(scope.row)" icon="Edit" size="small">编辑</el-button>
                  <el-button link type="danger" @click="deleteItem(scope.$index)" icon="Delete" size="small">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 商品项统计 -->
            <div class="items-summary">
              <div class="summary-row">
                <span class="summary-label">商品项数：</span>
                <span class="summary-value">{{ declaration.items.length }}</span>
              </div>
              <div class="summary-row">
                <span class="summary-label">商品总数量：</span>
                <span class="summary-value">{{ totalItemsQty }}</span>
              </div>
              <div class="summary-row">
                <span class="summary-label">商品总金额：</span>
                <span class="summary-value total-amount">{{ totalItemsAmount }}</span>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 集装箱信息 -->
        <el-tab-pane label="集装箱" name="containers">
          <div class="containers-container">
            <div class="table-toolbar">
              <el-button type="primary" @click="addContainer" icon="Plus">新增集装箱</el-button>
            </div>

            <el-table :data="declaration.containers" border width="100" class="containers-table">
              <el-table-column prop="containerId" label="集装箱号" >
                <template #default="scope">
                  <el-input v-model="scope.row.containerId" placeholder="集装箱号" />
                </template>
              </el-table-column>

              <el-table-column prop="containerMd" label="集装箱规格" width="150">
                <template #default="scope">
                  <el-select v-model="scope.row.containerMd" style="width:120px" placeholder="选择规格">
                    <el-option label="20英尺" value="20" />
                    <el-option label="40英尺" value="40" />
                    <el-option label="45英尺" value="45" />
                  </el-select>
                </template>
              </el-table-column>

              <el-table-column prop="goodsNo" label="商品项号" width="120">
                <template #default="scope">
                  <el-input v-model="scope.row.goodsNo" placeholder="商品项号，逗号分隔" />
                </template>
              </el-table-column>

              <el-table-column prop="lclFlag" label="拼箱标识" width="130">
                <template #default="scope">
                  <el-radio-group v-model="scope.row.lclFlag">
                    <el-radio label="0">否</el-radio>
                    <el-radio label="1">是</el-radio>
                  </el-radio-group>
                </template>
              </el-table-column>

              <el-table-column prop="goodsContaWt" label="箱货重量(kg)" width="180">
                <template #default="scope">
                  <el-input-number v-model="scope.row.goodsContaWt" :min="0" :precision="3" style="width: 100%" />
                </template>
              </el-table-column>

              <el-table-column prop="containerWt" label="自重(kg)" width="180">
                <template #default="scope">
                  <el-input-number v-model="scope.row.containerWt" :min="0" :precision="3" style="width: 100%" />
                </template>
              </el-table-column>

              <el-table-column label="操作" width="100" align="center">
                <template #default="scope">
                  <el-button link type="danger" @click="deleteContainer(scope.$index)" icon="Delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 随附单证 -->
        <el-tab-pane label="随附单证" name="license">
          <div class="license-container">
            <div class="table-toolbar">
              <el-button type="primary" @click="addLicenseDocu" icon="Plus">新增单证</el-button>
            </div>

            <el-table :data="declaration.licenseDocus" border style="width: 100%" class="license-table">
              <el-table-column prop="docuCode" label="单证代码"  >
                <template #default="scope">
                  <el-input v-model="scope.row.docuCode" placeholder="单证代码" />
                </template>
              </el-table-column>

              <el-table-column prop="certCode" label="单证编号" >
                <template #default="scope">
                  <el-input v-model="scope.row.certCode" placeholder="单证编号" />
                </template>
              </el-table-column>

              <el-table-column label="操作" width="100" align="center">
                <template #default="scope">
                  <el-button link type="danger" @click="deleteLicenseDocu(scope.$index)" icon="Delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 其他信息         -->
        <el-tab-pane label="其他信息" name="other">
          <div class="other-info-container">
            <el-row :gutter="20">
              <!--
            <el-col :span="12">

              <el-card shadow="never" class="section-card">
                <div class="section-header">
                  <h3 class="section-title">签名信息</h3>
                </div>
                <el-form :model="declaration.signInfo" label-width="140px">
                  <el-form-item label="操作员IC卡号：" prop="icCode">
                    <el-input v-model="declaration.signInfo.icCode" placeholder="请输入操作员IC卡号" />
                  </el-form-item>

                  <el-form-item label="操作员姓名：" prop="operName">
                    <el-input v-model="declaration.signInfo.operName" placeholder="请输入操作员姓名" />
                  </el-form-item>

                  <el-form-item label="企业组织机构代码：" prop="copCode">
                    <el-input v-model="declaration.signInfo.copCode" placeholder="请输入企业组织机构代码" />
                  </el-form-item>

                  <el-form-item label="客户端报关单编号：" prop="clientSeqNo">
                    <el-input v-model="declaration.signInfo.clientSeqNo" placeholder="请输入客户端报关单编号" />
                  </el-form-item>

                  <el-form-item label="签名日期：" prop="signDate">
                    <el-input v-model="declaration.signInfo.signDate" placeholder="自动生成" disabled />
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
             -->
            <el-col :span="12">
              <el-card shadow="never" class="section-card">
                <div class="section-header">
                  <h3 class="section-title">自由文本信息</h3>
                </div>

                <el-form :model="declaration.freeText" label-width="140px">
                  <el-form-item label="关联报关单号：" prop="relId">
                    <el-input v-model="declaration.freeText.relId" placeholder="请输入关联报关单号" />
                  </el-form-item>

                  <el-form-item label="关联备案号：" prop="relManNo">
                    <el-input v-model="declaration.freeText.relManNo" placeholder="请输入关联备案号" />
                  </el-form-item>

                  <el-form-item label="航次号：" prop="voyNo">
                    <el-input v-model="declaration.freeText.voyNo" placeholder="请输入航次号" />
                  </el-form-item>

                  <el-form-item label="监管仓号：" prop="bonNo">
                    <el-input v-model="declaration.freeText.bonNo" placeholder="请输入监管仓号" />
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
          </el-row>

          <el-card shadow="never" class="section-card" style="margin-top: 15px;">
            <div class="section-header">
              <h3 class="section-title">其他信息</h3>
            </div>

            <el-form :model="declaration" label-width="140px">
              <el-form-item label="业务流水号：" prop="businessNo">
                <el-input v-model="declaration.businessNo" placeholder="请输入业务流水号" />
              </el-form-item>

              <el-form-item label="报文版本：" prop="version">
                <el-input v-model="declaration.version" placeholder="请输入报文版本" />
              </el-form-item>

              <el-form-item label="备注：" prop="note">
                <el-input
                    v-model="declaration.note"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入其他备注信息"
                />
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-tab-pane>

    </el-tabs>
  </div>

  <template #footer>
    <div class="flex-between">
      <el-space>
        当前文件:
        <div>

          <el-input
              v-model="xmlform.fileName"
              placeholder="文件名"
              style="width: 720px;"
              readonly
              class="input-with-select"
          >
            <template #prepend>
              文件名
            </template>
            <template #append>
              <el-select v-model="optType" placeholder="请选择XML生成名称" style="width:100px" @change="getFileName"  >
                  <el-option label="深圳跨综服" value="dxpId" >DXP-深圳跨综服-深圳跨境电商综合服务平台</el-option>
                  <el-option label="深关数" value="ediCode" >H2018-深关数-深圳海关大数据平台</el-option>
              </el-select>
            </template>
          </el-input>

        </div>

        <el-space v-if="xmlform.filePath">
          <el-link type="primary" :href="xmlform.filePath" :title="xmlform.fileName">点此下载XML文件</el-link>
          <el-icon class="pointer" @click.stop="disableCustomsXml" style="margin-left:20px"><Delete/></el-icon>
        </el-space>
        <span v-else>暂无XML文件记录</span>
      </el-space>
      <div class="dialog-footer">

        <el-button @click="xmlVisible = false">取消</el-button>
        <el-button type="info" @click="resetForm">重置</el-button>
        <el-button type="warning" @click="handleSave('save')" :loading="xmlLoading">保存草稿</el-button>
        <el-button type="primary" @click="handleSave('submit')" :loading="xmlLoading">提交申报</el-button>
      </div>
    </div>


  </template>
</el-dialog>
</template>

<script setup>
import {ref, reactive, computed, watch, nextTick, toRefs} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import CustomsSelect from '@/views/erp/shipv2/shipment_handing/shipstep/components/customs_data.vue'
import shipmentCustomsApi from '@/api/erp/shipv2/shipmentCustomsApi.js'
import { useCustomsDict } from '@/hooks/erp/shipment/custom_data.js'
import {Delete} from "@element-plus/icons-vue";
const { unit,currency,wrapType,trafMode,pod,country,portCode } = useCustomsDict('unit','currency','wrapType','trafMode','pod','country','portCode')
const emit = defineEmits(['refresh'])

// 状态管理
const activeTab = ref('header')
const headerFormRef = ref(null)
const selectedItems = ref([])

let state = reactive({
xmlVisible: false,
groupid: null,
shipmentid: null,
xmlform: {
  xmlType: "DEC001",
  appType: "1",
  appStatus: "2",
  number: null,
},
optType:'dxpId',
xmlLoading: false,
// FBA订单表单数据
orderData: { items: [] }
})

let { xmlVisible, xmlform, xmlLoading, orderData,optType } = toRefs(state)
let shipInfo = ref({})
// 报关单数据
const declaration = reactive({
header: {
  ieFlag: 'E',
  tradeMode: '9610',
  transMode: '1',
  feeMark: '3',
  feeRate: 0,
  feeCurr: '142',
  insurMark: '3',
  insurRate: 0,
  insurCurr: '142',
  otherMark: '3',
  otherRate: 0,
  otherCurr: '142',
  wrapType: '1',
  ieDate: '',
  pDate: '',
  seqNo: '',
  ediId: '1',
  entryType: 'M',
  tradeAreaCode: '142',
  noOtherPack: '0',
  domainId: '1'
},
items: [],
containers: [],
licenseDocus: [],
requestCerts: [],
otherPacks: [],
users: [],
copLimits: [],
freeText: {},
signInfo: {
  operType: 'C',
  domainId: '1'
},
edocRelation: {},
goodsLimits: [],
note: '',
businessNo: '',
version: 'V4.0',
})

// 计算属性
const totalItemsQty = computed(() => {
return declaration.items.reduce((sum, item) => sum + (Number(item.gQty) || 0), 0)
})

const totalItemsAmount = computed(() => {
return declaration.items.reduce((sum, item) => {
  const total = Number(item.declTotal) || 0
  return sum + total
}, 0).toFixed(2)
})

// 监听商品项变化，更新表头总重量
watch(() => declaration.items, (newItems) => {
 /*const totalGrossWeight = newItems.reduce((sum, item) => {
   // 这里可以根据实际情况计算商品重量
   return sum + (Number(item.gQty) || 0)
 }, 0)*/

// if (!declaration.header.grossWet || declaration.header.grossWet < totalGrossWeight) {
//   declaration.header.grossWet = totalGrossWeight
//   declaration.header.netWt = totalGrossWeight * 0.9 // 假设净重为毛重的90%
// }
}, { deep: true })

async function getFileName(){
let data=JSON.parse(JSON.stringify(state.xmlform));
data.optType=state.optType;
await shipmentCustomsApi.generateFileName(data).then((res) => {
  if(res.data){
    state.xmlform.fileName=res.data;
    state.xmlform.filePath=null;
  }
})
}
// 方法
const updateItemTotal = (item) => {
if (item.gQty && item.declPrice) {
  item.declTotal = Number(item.gQty) * Number(item.declPrice)
} else {
  item.declTotal = 0
}
}

const updateItemSequence = () => {
// 对商品项按gNo排序
declaration.items.sort((a, b) => a.gNo - b.gNo)
}

const addItem = () => {
const newItem = {
  gNo: declaration.items.length + 1,
  codeTS: '',
  gName: '',
  gModel: '',
  gQty: 0,
  gUnit: '',
  firstQty: 0,
  firstUnit: '',
  secondQty: 0,
  secondUnit: '',
  originCountry: '',
  tradeCurr: '142',
  declPrice: 0,
  declTotal: 0,
  useTo: '',
  dutyMode: '1',
  destinationCountry: '',
  goodsLimits: []
}
declaration.items.push(newItem)
}

const editItem = (item) => {
// 可以在这里打开一个详细的商品编辑对话框
ElMessageBox.prompt('编辑商品信息', '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  inputValue: JSON.stringify(item, null, 2),
  inputType: 'textarea',
  inputRows: 10
}).then(({ value }) => {
  try {
    const editedItem = JSON.parse(value)
    const index = declaration.items.findIndex(i => i.gNo === item.gNo)
    if (index > -1) {
      declaration.items[index] = { ...declaration.items[index], ...editedItem }
      updateItemTotal(declaration.items[index])
    }
  } catch (e) {
    ElMessage.error('JSON格式错误')
  }
})
}

const deleteItem = (index) => {
ElMessageBox.confirm('确定删除该商品项吗？', '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
  declaration.items.splice(index, 1)
  // 重新排序
  declaration.items.forEach((item, idx) => {
    item.gNo = idx + 1
  })
  ElMessage.success('删除成功')
})
}

const batchDeleteItems = () => {
if (selectedItems.value.length === 0) {
  ElMessage.warning('请选择要删除的商品项')
  return
}

ElMessageBox.confirm(`确定删除选中的${selectedItems.value.length}个商品项吗？`, '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
  const selectedGnos = selectedItems.value.map(item => item.gNo)
  declaration.items = declaration.items.filter(item => !selectedGnos.includes(item.gNo))

  // 重新排序
  declaration.items.forEach((item, idx) => {
    item.gNo = idx + 1
  })

  selectedItems.value = []
  ElMessage.success('批量删除成功')
})
}

const handleItemSelectionChange = (selection) => {
selectedItems.value = selection
}

const importItems = () => {
// 这里可以实现商品导入逻辑
ElMessage.info('商品导入功能待实现')
}

const addContainer = () => {
declaration.containers.push({
  containerId: '',
  containerMd: '20',
  goodsNo: '',
  lclFlag: '0',
  goodsContaWt: 0,
  containerWt: 0
})
}

const deleteContainer = (index) => {
declaration.containers.splice(index, 1)
}

const addLicenseDocu = () => {
declaration.licenseDocus.push({
  docuCode: '',
  certCode: ''
})
}

const deleteLicenseDocu = (index) => {
declaration.licenseDocus.splice(index, 1)
}

function disableCustomsXml() {
ElMessageBox.confirm(
    '您确认要删除吗?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
)
    .then(() => {
      let groupid = state.groupid
      let ftype = state.xmlform.xmlType
      let number = state.shipmentid
      shipmentCustomsApi.disabledCustomsXml({"groupid": groupid, "ftype": ftype, "number": number}).then((res) => {
        ElMessage.success('删除海关XML成功')
        loadXml()
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消操作',
      })
    })
}

const resetForm = () => {
ElMessageBox.confirm('确定要重置所有表单数据吗？', '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
  // 重置所有数据
  Object.assign(declaration, {
    header: {
      ieFlag: 'E',
      tradeMode: '9610',
      transMode: '1',
      feeMark: '3',
      feeRate: 0,
      feeCurr: '142',
      insurMark: '3',
      insurRate: 0,
      insurCurr: '142',
      otherMark: '3',
      otherRate: 0,
      otherCurr: '142',
      wrapType: '1',
      ieDate: '',
      pDate: '',
      seqNo: '',
      ediId: '1',
      entryType: 'M',
      tradeAreaCode: '142',
      noOtherPack: '0',
      domainId: '1'
    },
    items: [],
    containers: [],
    licenseDocus: [],
    requestCerts: [],
    otherPacks: [],
    users: [],
    copLimits: [],
    freeText: {},
    signInfo: {
      operType: 'C',
      domainId: '1'
    },
    edocRelation: {},
    goodsLimits: [],
    note: '',
    businessNo: '',
    version: 'V4.0'
  })

  ElMessage.success('重置成功')
})
}

const saveAsDraft = async () => {
try {
  state.xmlLoading = true

  // 验证必填字段
  if (!declaration.header.customMaster) {
    ElMessage.warning('请填写申报地海关')
    state.xmlLoading = false
    return
  }

  if (!declaration.header.tradeCode) {
    ElMessage.warning('请填写收发货人代码')
    state.xmlLoading = false
    return
  }

  if (declaration.items.length === 0) {
    ElMessage.warning('请至少添加一个商品项')
    state.xmlLoading = false
    return
  }

  // 调用API保存草稿
  const result = await shipmentCustomsApi.saveDeclarationDraft(declaration)

  if (result.success) {
    ElMessage.success('保存草稿成功')
    emit('refresh')
  } else {
    ElMessage.error(result.message || '保存失败')
  }
} catch (error) {
  ElMessage.error('保存草稿时发生错误：' + error.message)
} finally {
  loading.value = false
}
}

function loadXml(){
state.xmlVisible=true;
let groupid=state.groupid;
let ftype=state.xmlform.xmlType;
let number=state.shipmentid;
if(!state.guid){
  state.guid="";
}
shipmentCustomsApi.getCustomsXml({"guid": state.guid}).then(async (res)=>{
  if(res.data && res.data.filePath){
    state.xmlform = res.data;
    state.guid=res.data.guid;
  }else{
    state.xmlform.xmlType="DEC001";
    state.xmlform.appType="1";
    state.xmlform.appStatus="2";
    state.xmlform.number=state.shipmentid;
    await getFileName();
  }
  if( state.xmlform.content&& "undefined"!= state.xmlform.content){
    var data=JSON.parse(state.xmlform.content);
    state.xmlform.customsDeclaration=data;
    declaration.header = data.header;
    declaration.items = data.items;
    declaration.containers=data.containers;
    declaration.licenseDocus=data.licenseDocus;
    declaration.requestCerts=data.requestCerts;
    declaration.otherPacks=data.otherPacks;
    declaration.users=data.users;
    declaration.copLimits=data.copLimits;
    declaration.freeText=data.freeText;
    declaration.signInfo=data.signInfo;
    declaration.edocRelation=data.edocRelation;
    declaration.goodsLimits=data.goodsLimits;
    declaration.note=data.note;
    declaration.businessNo=data.businessNo;
  }else{
    loadOrderData();
  }
});
}

function handleSave(ftype){
state.xmlform.optType=ftype;
submitDeclaration();
}

const submitDeclaration = async () => {
  state.xmlLoading = true

  // 验证表单
  if (headerFormRef.value) {
    const valid = await headerFormRef.value.validate()
    if (!valid) {
      state.xmlLoading = false
      return
    }
  }

  // 验证商品项
  if (declaration.items.length === 0) {
    ElMessage.warning('请至少添加一个商品项')
    state.xmlLoading = false
    return
  }

  // 验证商品项数据
  for (const item of declaration.items) {
    if (!item.codeTS || !item.gName) {
      ElMessage.warning('商品项必须填写商品编号和商品名称')
      state.xmlLoading= false
      return
    }
  }

  // 生成业务流水号（如果不存在）
  if (!declaration.businessNo) {
    declaration.businessNo = 'DECL' + new Date().getTime()
  }

  // 生成签名日期
  if (!declaration.signInfo.signDate) {
    const now = new Date()
    declaration.signInfo.signDate =
        now.getFullYear() +
        String(now.getMonth() + 1).padStart(2, '0') +
        String(now.getDate()).padStart(2, '0') +
        String(now.getHours()).padStart(2, '0') +
        String(now.getMinutes()).padStart(2, '0') +
        String(now.getSeconds()).padStart(2, '0')
  }

  // 调用API提交申报

  state.xmlform.number = state.shipmentid;
  state.xmlform.groupid = state.groupid;
  state.xmlform.customsDeclaration=declaration;
  shipmentCustomsApi.generateCustomsXml(state.xmlform).then((res) => {
    ElMessage.success('生成海关XML成功');
    state.xmlform.guid = res.data;
    state.guid = res.data;
    loadXml()
    state.xmlLoading = false;
  }).catch((error) => {
    state.xmlLoading = false;
  })
}

const handleDialogClose = () => {
// 对话框关闭时的清理工作
//resetForm()
}

function loadOrderData(){
state.xmlform.number=state.shipmentid;
shipmentCustomsApi.viewXmlData(state.xmlform).then((res) => {
  if (res.data && res.data.customsDeclaration) {
    state.xmlform = res.data;
    state.guid=res.data.guid;
    var data=res.data.customsDeclaration;
    declaration.header = data.header;
    declaration.items = data.items;
    declaration.containers=data.containers;
    declaration.licenseDocus=data.licenseDocus;
    declaration.requestCerts=data.requestCerts;
    declaration.otherPacks=data.otherPacks;
    declaration.users=data.users;
    declaration.copLimits=data.copLimits;
    declaration.freeText=data.freeText;
    declaration.signInfo=data.signInfo;
    declaration.edocRelation=data.edocRelation;
    declaration.goodsLimits=data.goodsLimits;
    declaration.note=data.note;
    declaration.businessNo=data.businessNo;
  } else {
    state.xmlform.xmlType = "DEC001"
    state.xmlform.appType = "1"
    state.xmlform.appStatus = "2"
    state.xmlform.number = state.shipmentid
  }
})
}

// 对外暴露的方法
async function show(groupid, shipmentid,guid) {
state.guid=guid;
state.xmlVisible = true;
state.groupid = groupid
state.shipmentid = shipmentid
if (state.xmlform) {
  state.xmlform.number=shipmentid;
  state.xmlform.groupid=groupid;
  state.xmlform.xmlType="DEC001";
} else {
  state.xmlform = {number:shipmentid,groupid:groupid,xmlType:"DEC001"}
}

loadXml();
}


defineExpose({ show })
</script>
<style>
.declaration-dialog .el-dialog__header{
padding-bottom: 0px;
}

</style>

<style scoped>
.customs-declaration-container {
padding: 10px 0;
}

.declaration-tabs {
:deep(.el-tabs__header) {
  margin: 0 0 15px 0;
}

:deep(.el-tabs__content) {
  padding: 0 10px;
}
}

.section-card {
margin-bottom: 15px;
border: 1px solid #ebeef5;
border-radius: 6px;
}

.section-header {
padding: 12px 0;
margin-bottom: 15px;
border-bottom: 1px solid #ebeef5;
}

.section-title {
margin: 0;
color: #303133;
font-size: 16px;
font-weight: 600;
}

.header-form {
margin-top: 10px;
}

/* 表格工具栏 */
.table-toolbar {
margin-bottom: 15px;
display: flex;
gap: 10px;
}

/* 商品项表格样式 */
.items-table {
margin-bottom: 15px;

:deep(.el-table__header-wrapper) {
  background-color: #f5f7fa;
}
}

.item-info {
padding: 5px 0;
}

.item-row {
display: flex;
align-items: center;
margin-bottom: 8px;

&:last-child {
  margin-bottom: 0;
}
}

.item-label {
width: 70px;
color: #606266;
font-size: 12px;
white-space: nowrap;
}

.item-value {
color: #303133;
font-weight: 500;
}

/* 统计信息 */
.items-summary {
display: flex;
justify-content: flex-end;
gap: 30px;
padding: 12px 20px;
background-color: #f8f9fa;
border-radius: 4px;
margin-top: 15px;
}

.summary-row {
display: flex;
align-items: center;
}

.summary-label {
color: #606266;
font-size: 13px;
}

.summary-value {
margin-left: 6px;
color: #303133;
font-size: 14px;
font-weight: 600;
}

.total-amount {
color: #f56c6c;
font-size: 15px;
}

/* 容器表格样式 */
.containers-table,
.license-table {
margin-bottom: 15px;

:deep(.el-table__header-wrapper) {
  background-color: #f5f7fa;
}
}

/* 其他信息容器 */
.other-info-container {
padding: 10px 0;
}

/* 对话框样式优化 */
:deep(.el-dialog__body) {
max-height: 75vh;
overflow-y: auto;
padding-top: 10px !important;
}

.dialog-footer {
display: flex;
justify-content: flex-end;
gap: 10px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
:deep(.el-dialog) {
  width: 95% !important;
}

.items-summary {
  flex-direction: column;
  gap: 10px;
  align-items: flex-end;
}
}

@media (max-width: 768px) {
.items-summary {
  align-items: stretch;
}

.summary-row {
  justify-content: space-between;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}
}
</style>