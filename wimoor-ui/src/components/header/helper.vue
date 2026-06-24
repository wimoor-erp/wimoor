<template>
	<el-button class='ic-btn' :title="'帮助文档: ' + name" @click="openHelp">
		<help theme="outline" size="16" :strokeWidth="3"/>
	</el-button>
</template>

<script setup>
import { Help } from '@icon-park/vue-next';
import { getDictItemByCodeAndName } from '@/api/sys/admin/dict.js';
import { ElMessage } from 'element-plus';

const props = defineProps({
	name: {
		type: String,
		required: true
	}
});

async function openHelp() {
	try {
		const res = await getDictItemByCodeAndName('helppage', props.name);
		if (res.data) {
			const item = res.data.find(d => d.name === props.name);
			if (item && item.value) {
				window.open(item.value, '_blank');
			} else {
				ElMessage.warning('未找到对应的帮助文档');
			}
		}
	} catch (e) {
		ElMessage.error('获取帮助文档失败');
	}
}
</script>