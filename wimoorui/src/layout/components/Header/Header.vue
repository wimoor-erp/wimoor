<template>
    <div style="height:56px;line-height:56px;border-bottom: 1px solid #eee;display: flex;">
        <div style="width: 200px;padding-left: 30px;font-weight: bold;color: dodgerblue;"><h3>后台管理</h3></div>
        <div style="flex: 1;"></div>
        <div style="width: 100px;">
            <el-dropdown class="pointer">
                <span class="el-dropdown-link">
                  下拉菜单<el-icon class="el-icon--right">
                    <arrow-down />
                  </el-icon>
                </span>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item>个人信息</el-dropdown-item>
                        <el-dropdown-item @click="loginout">退出系统</el-dropdown-item>
                        <!--<el-dropdown-item disabled>Action 4</el-dropdown-item>
                        <el-dropdown-item divided>Action 5</el-dropdown-item>-->
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
    </div>
</template>



<script>
    import { ArrowDown } from '@element-plus/icons-vue';
    export default {
        name: "Header",
        components:{
            ArrowDown
        },
        methods:{
            loginout(){
                 this.$router.push("/login");//路由跳转
            }
        }
    };
</script>

<style scoped>

</style>