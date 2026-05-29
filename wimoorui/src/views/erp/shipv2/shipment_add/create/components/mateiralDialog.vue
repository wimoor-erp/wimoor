<!-- 添加产品时，共用的产品弹窗 -->

<template>
	<div>
		<el-dialog v-model="dialogVisible" title="添加产品" width="60%" :before-close="handleClose">
			<div class="con-header">
				<el-row>
					<el-input v-model="searchSku" @input="loadData" placeholder="搜索产品SKU">
						<template #suffix>
							<el-icon :size="16">
								<Search />
							</el-icon>
						</template>
					</el-input>
				</el-row>
			</div>
			<GlobalTable ref="globalTable" :inDialog="true" :tableData="tableData" @loadTable="loadTableData"
				@selectionChange='selectChange' border style="width: 100%;margin-bottom:16px;">
				<template #field>
					<el-table-column type="selection" :reserve-selection="false" width="38" />
					<el-table-column prop="imgage" label="图片" width="60">
						<template #default="scope">
							<el-image :src="scope.row.image" width="40" height="40"></el-image>
						</template>
					</el-table-column>
					<el-table-column prop="sku" label="名称/SKU" width="400" show-overflow-tooltip>
						<template #default="scope">
							<div class='name'>{{scope.row.name}}</div>
		 				<div class='sku'>{{scope.row.sku}} </div>
						</template>
					</el-table-column>
					<el-table-column prop="fulfillable" label="可用库存" width="100">
						<template #default="scope">
							<div class='name'>{{scope.row.fulfillable}}</div>
							<span v-if="scope.row.willfulfillable>0">
								可组装:{{scope.row.willfulfillable}} 
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="supplier" label="供应商">
					</el-table-column>
				</template>
		 </GlobalTable>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="dialogVisible = false">取消</el-button>
					<el-button type="primary" @click="submitFunc">提交</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script>
	import {
		MenuUnfold,
		Plus,
		SettingTwo,
		Help,
		Copy,
		MoreOne
	} from '@icon-park/vue-next';
	import {
		ref,
		reactive,
		onMounted
	} from 'vue'
	import {
		Search,
		ArrowDown,
	} from '@element-plus/icons-vue'
	import {
		ElDivider,
		ElMessageBox,
		ElMessage
	} from 'element-plus'
	import shipmentplanApi from '@/api/erp/ship/shipmentplanApi.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	export default {
		name: 'mateiralDialog',
		components: {
			Search,
			GlobalTable,
		},
		emits: ["getdata"],
		setup(props, context) {
			let dialogVisible = ref(false)
			let searchSku = ref('')
			let tableData = reactive({
				records: [],
				total: 0
			})
			let selectDatas = reactive({
				list: []
			})
			let ftype = ref('shipment')
			let params = ref({})
			let globalTable = ref();
			//方法
	 

			function loadData() {
					if (globalTable.value && globalTable.value["loadTable"]) {
						   globalTable.value.loadTable();
					} 

			}

			function loadTableData(data) {
				if ( params.value["groupid"]) {
					data.groupid = params.value.groupid;
					data.marketplaceid = params.value.marketplaceid;
					data.ftype = ftype.value;
					data.warehouseid = params.value.warehouseid;
					data.search = searchSku.value;
					shipmentplanApi.getProductInfoList(data).then((res) => {
							tableData.records = res.data.records;
							tableData.total = res.data.total;
					})
				} 
				 
			}

			function submitFunc() {
				if (selectDatas.list.length > 0) {
					dialogVisible.value = false;
					context.emit("getdata", selectDatas.list);
				} else {
					ElMessage.error('提交的行数不能小于1！')
					dialogVisible.value = true;
				}
			}

			function selectChange(selection) {
				selectDatas.list = selection;
			}
			//数据接收
			return {
				dialogVisible,
				tableData,
				submitFunc,
				ftype,
				params,
				loadData,
				selectDatas,
				selectChange,
				searchSku,
				loadTableData,
				globalTable

			}
		}
	}
</script>

<style>
	.el-input__suffix {
		display: flex;
		align-items: center;
		font-size: 16px;
		padding-right: 8px;
	}
</style>
