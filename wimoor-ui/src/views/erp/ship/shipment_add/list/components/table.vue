<template>
	<el-row>
		<GlobalTable ref="globalTable" :tableData="tableData" :stripe="true"  @loadTable="loadtableData"  height="calc(100vh - 300px)" :defaultSort="{ prop: 'createdate2', order: 'descending' }"  
			style="width: 100%;margin-bottom:16px;">
			<template #field>
				<el-table-column prop="groupname" label="店铺" width="120" />
				<el-table-column prop="number" label="计划编码" width='180' />
				<el-table-column prop="name" label="计划名称" width='180' />
				<el-table-column prop="skunum" label="SKU个数" width="100" sortable="custom" />
				<el-table-column prop="sumquantity" label="发货数量" width="120" sortable="custom" />
				<el-table-column prop="warename" label="发货仓库" width="140" />
				<el-table-column prop="country" label="收货站点" width="120" />
				<el-table-column prop="createdate2" label="申请日期" width="120" sortable="custom"  />
				<el-table-column prop="remark" label="备注" />
				<el-table-column prop="remark" label="操作" width="120"  fixed="right" >
					<template #default="scope">
						<el-space>
							<el-button class='el-button--blue' @click="shipmentDetails(scope.row)">详情</el-button>
						</el-space>
					</template>
				</el-table-column>
			</template>
		</GlobalTable>
	</el-row>
</template>

<script>
	import {
		ref,
		reactive,
		onMounted
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import shipmentplanApi from '@/api/erp/ship/shipmentplanApi.js';
	import { dateFormat } from '@/utils/index.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	export default {
		name: 'Table',
		components: {
			GlobalTable
		},
		setup() {
			let globalTable = ref(GlobalTable);
			let router = useRouter()
			let parmashead = ref({})
			let tableData = reactive({
				records: [],
				total: 0
			})
			let auditstatus = ref("")

			function getshipplanData(params) {
				parmashead.value = params;
				globalTable.value.loadTable();
			}

			function shipmentDetails(row) {
				router.push({
					path: '/shipmentdetails',
					query: {
						id: row.id,
						title: "货件详情",
						path: '/shipmentdetails',
					}
				})
			}

			function loadtableData(params) {
				params.groupid = parmashead.value.groupid;
				params.marketplaceid = parmashead.value.marketplaceid;
				params.warehouseid = parmashead.value.warehouseid;
				if (parmashead.value.start !== undefined) {
					params.fromDate = (parmashead.value.start);
					params.toDate = (parmashead.value.end + " 23:59:59");
				} else {
					const end = new Date()
					const start = new Date()
					start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
					params.fromDate = dateFormat(start);
					params.toDate = dateFormat(end) + " 23:59:59";
				}
				params.auditstatus = parmashead.value.auditstatus;
				if (parmashead.value.seachtype !== undefined) {
					params.searchtype = parmashead.value.seachtype;
				} else {
					params.searchtype = "sku";
				}
				params.search = parmashead.value.searchwords;
				shipmentplanApi.getPlanList(params).then((res) => {
					tableData.records = res.data.records
					tableData.total = res.data.total
				})
			}

			return {
				tableData,
				shipmentDetails,
				getshipplanData,
				parmashead,
				globalTable,
				auditstatus,
				dateFormat,
				loadtableData
			}
		}
	}
</script>

<style>
</style>
