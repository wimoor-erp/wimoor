<template>
    <div class="main-sty">
        <div class="con-header">
            <el-row>
                <el-space >
                    <el-button type="primary" class="im-but-one" @click="addAddress">
                        <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
                        <span>添加地址</span>
                    </el-button>
					<GroupWithoutMarket defaultValue="only" @change="changeData"></GroupWithoutMarket>
                </el-space>
				
                <div class='rt-btn-group'>
					<el-button v-if="showNonDelete" @click="changeSearchType(false)">已删地址</el-button>
					<el-button v-else @click="changeSearchType(true)">未删地址</el-button>
                    <el-button   class='ic-btn' title='帮助文档'>
                        <help theme="outline" size="16" :strokeWidth="3"/>
                    </el-button>
                </div>
            </el-row>
        </div>
        <!--表单-->
        <el-row>
           <Table ref="tablelist" @editAddress="editAddress"/>
        </el-row>
       <Editdialog ref="editRef" @addressChange="addressChange"/>
    </div>
</template>
<script>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import { ref,reactive,onMounted } from 'vue'
	import Table from "./components/table.vue"
	import Editdialog from "./components/editdialog.vue"
	import GroupWithoutMarket from "@/components/header/groupWithoutMarket.vue"
    import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { useRoute,useRouter } from 'vue-router'
    export default{
        name: '发货地址',
        components:{
            Help,Table,
            Search,GroupWithoutMarket,
            Plus,Editdialog,
            ArrowDown
        },
        setup(){
			let editRef = ref(Editdialog)
			let router = useRouter();
			let tablelist=ref();
			let showNonDelete=ref(true);
			let params={};
			function changeSearchType(value){
				showNonDelete.value=value;
				if(value==true){
					params.isdisable="";
				}else{
					params.isdisable="true";
				}
				if(tablelist&&tablelist.value){
				   tablelist.value.refreshTable(params);
				}
			}
			function addAddress(){
				editRef.value.showModel();
			}
			function editAddress(param){
				editRef.value.showModel(param);
			}
			function addressChange(){
				if(tablelist&&tablelist.value){
				   tablelist.value.refreshTable(params);
				}
			}
			function changeData(param){
				params.groupid=param.groupid;
				if(tablelist&&tablelist.value){
				   tablelist.value.refreshTable(params);
				}
			}
            //方法
            //数据接收
            return{
                addAddress,editRef,changeData,tablelist,showNonDelete,changeSearchType,editAddress,addressChange
            }
        }

    }
</script>
<style>
</style>