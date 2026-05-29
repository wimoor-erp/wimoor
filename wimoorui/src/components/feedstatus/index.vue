<template>
	<el-tag class="pointer" v-loading="loading" v-if="feed_processing_status=='_DONE_' || feed_processing_status=='DONE'" title="请求已处理"
		@click="downloadFeedFile" type="success" :closable="false" >请求已处理</el-tag>
	<el-tag class="pointer"  v-loading="loading" v-else-if="feed_processing_status" :title="feed_processing_name" @click="downloadFeedFile"
		type="warning" :closable="false" >{{feed_processing_name}}{{feed_processing_status}}</el-tag>
	<el-tag class="pointer"  v-loading="loading" v-else title="未提交" type="info" :closable="false" >
		未提交</el-tag>
</template>

<script>
	import {
		ref,
		watch,
		reactive,
		onMounted,
		onUnmounted
	} from 'vue'
	import {
		Search,
		ArrowDown,
		Close
	} from '@element-plus/icons-vue'
	import {
		ElDivider,
		ElMessageBox,
		ElMessage
	} from 'element-plus'
	import feedApi from '@/api/amazon/feed/feedApi.js';
	export default {
		name: 'FeedStatus',
		components: {},
		props: ["feedid", "filename"],
		setup(prop, context) {
			let feed_processing_status = ref("");
			let feed_processing_name = ref("");
			let loading=ref(false);
			let params = {};
			let isRun = true;
			if (prop.feedid) {
				params.queueid = prop.feedid;
			} else {
				params.queueid = "";
			}
			if (prop.filename) {
				params.filename = prop.filename;
			} else {
				params.filename = "blank.txt";
			}
			

			function downloadFeedFile() {
				feedApi.downloadFeedFile(params).then(res => {
					ElMessage.success('导出成功！')
					const blob = new Blob([res]);
					if (window.navigator["msSaveOrOpenBlob"] && window.navigator.msSaveOrOpenBlob()) {
						navigator.msSaveBlob(blob, prop.filename)
					} else {
						var link = document.createElement("a");
						link.href = window.URL.createObjectURL(blob);
						link.download = prop.filename;
						link.click();
						window.URL.revokeObjectURL(link.href);
					}
				}).catch(e => {
					ElMessage.error('导出失败！')
				})
			}
			let num = 0;

			function submitfeedInfo(feedid) {
				if (feedid) {
					params.queueid = feedid
				} else {
					params.queueid = prop.feedid
				}
				loading.value=true;
				feedApi.submitfeedInfo(params).then(res => {
					loading.value=false;
					if (res.data) {
						var data = res.data;
						feed_processing_status.value = data.status;
						feed_processing_name.value = data.name;
						num++;
						if (data && data.status != '_DONE_' && data.status != "DONE" && isRun) {
							if (num <= 10) {
								setTimeout(submitfeedInfo, 1000);
							} else if (num <= 20) {
								setTimeout(submitfeedInfo, 10000);
							} else {
								setTimeout(submitfeedInfo, 20000);
							}
						}
					}
				})
			}
			onMounted(() => {
				if (prop.feedid) {
					submitfeedInfo();
				}
				watch(prop.feedid, (val) => {
					submitfeedInfo();
				});
				isRun = true;
			});
			onUnmounted(() => {
				isRun = false;
			})
			return {
				submitfeedInfo,
				feed_processing_status,
				feed_processing_name,
				downloadFeedFile,
				loading
			}
		}
	}
</script>

<style>
	.el-alert {
		cursor: pointer
	}
</style>
