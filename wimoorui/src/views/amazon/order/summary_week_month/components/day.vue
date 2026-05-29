<template>
		<div class="m-b-24">
		<el-space class="m-b-8">
				<h3>即时订单</h3><span class="font-extraSmall"> 2022-02-03 08:08</span>
		</el-space>
		<el-card shadow="never">
			<el-row>
				<el-col :span="4" v-for="item in daysum">
					<div class="font-Small">{{item.name}}</div>
					<div class="pag-data-num">{{item.value}}</div>
					<div class="pag-small-Extra">
						<span>昨日 </span>
						<span>{{item.yesvalue}}</span>
					</div>
				</el-col>
			</el-row>
		</el-card>
		</div>
</template>

<script>
	import { ref ,watch,reactive,onMounted} from 'vue'
	export default{
		name:'Day',
		components:{
			
		},
		setup(){
			let daysum= reactive([
				{name:'订单数量',value:"5544",yesvalue:'5689'},
				{name:'销量',value:"5616",yesvalue:'5685'},
				{name:'订单金额',value:"$527.84",yesvalue:'$566.69'},
				{name:'退货量',value:"65",yesvalue:'87'},
				{name:'每单平均销量',value:"1.05",yesvalue:'1.08'},
				{name:'每单平均销售额',value:"$1.63",yesvalue:'$1.73'},
			])
			return{
				daysum
			}
		}
	}
</script>

<style scoped>
    .pag-data-num {
        font-size: 16px;
        font-weight: 600;
        font-family: "Helvetica Neue";
        margin-bottom: 16px;
    }
	.font-Small{
		font-size: 13px;
	}
	.m-b-8{
		margin-bottom:8px;
	}
	.m-b-24{
		margin-bottom:24px;
	}
	.flex-center{
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
</style>
