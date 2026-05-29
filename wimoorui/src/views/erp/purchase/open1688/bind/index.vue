<template>
    <div class="main-sty">
        <div class="con-header">
            <el-row>
                <el-space >
                    <el-button type="primary" class="im-but-one" @click="showDialog">
                        <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
                        <span>绑定账号</span>
                    </el-button>
                </el-space>
                <div class='rt-btn-group'>
                    <el-button   class='ic-btn' title='帮助文档'>
                        <help theme="outline" size="16" :strokeWidth="3"/>
                    </el-button>
                </div>
            </el-row>
        </div>
        <!--表单-->
        <el-row>
            <el-table :data="tableData.list" :stripe="true"  style="width: 100%;margin-bottom:16px;">
                <el-table-column type="selection" width="38" />
                <el-table-column prop="name"  label="采购账号" sortable >
				<template #default="scope">
					<el-space>
				    <span class='name'>{{scope.row.name}}</span>
				    <el-icon class="icon"  @click="showDialog(scope.row)"><Edit /></el-icon>
					</el-space>
				</template>
				</el-table-column>
                <el-table-column   label="采购平台"  sortable >
				 <template #default="scope">
					 <el-image class="plant-img" :src="$require('1688.png')"></el-image>
				 </template>  
				</el-table-column>
				<el-table-column   label="平台账户名称"  sortable >
				<template #default="scope">
				   <span >{{scope.row.resourceOwner}}</span>
				</template>
				</el-table-column>
				<el-table-column prop="createtime"  label="到期时间" sortable >
				<template #default="scope">
				   <span >{{dateFormat(scope.row.refreshTokenTimeout)}}</span>
				</template>
				</el-table-column>
				
                <el-table-column   label="状态"  sortable >
				 <template #default="scope">
				 	 <el-tag type="success" v-if="scope.row.refreshToken">已绑定</el-tag>
					 <el-tag type="info"  v-else>未绑定</el-tag>
				  </template>  
				</el-table-column>
                <el-table-column prop="operate"  label="操作" width="140" sortable >
                    <template #default="scope">
                        <el-button  v-if="!scope.row.refreshToken"  class='el-button--blue' @click="goUrl(scope.row.id)">绑定</el-button>
						<el-button class='el-button--blue' v-if="scope.row.refreshToken"  @click="goUrl(scope.row.id)">延期授权</el-button>
						<el-button class='el-button--blue' @click="removeBind(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
		 <el-alert title="授权绑定您的阿里巴巴1688平台账号后,即可到采购单中查看物流信息!" type="info" :closable="false" />
    </div>
	<!-- 弹窗添加账号绑定 -->
	<el-dialog v-model="bindVisible" title="采购绑定" destroy-on-close='true' width="560px" @close='closeDialog'>
		 <div class="from-body">
			<el-row>
				<el-col class="text-ve-center"  :span="6">平台</el-col>
				<el-col :span="16">
					<el-image class="plant-img" :src="$require('1688.png')"></el-image>
				</el-col>
			</el-row>
			<el-row style="display:none">
				<el-col class="text-ve-center"  :span="6">ID</el-col>
				<el-col :span="16">
				<el-input v-model="purchId" placeholder="ID" />
				</el-col>
			</el-row>

			<el-row>
				<el-col class="text-ve-center"  :span="6"><span class="ness-input">*</span>名称</el-col>
				<el-col :span="16">
				<el-input v-model="purchName" placeholder="采购账号名称" />
				</el-col>
			</el-row>
			<el-row >
				<el-col class="text-ve-center"  :span="6"><span class="ness-input">*</span>授权类型</el-col>
				<el-col :span="16" >
					<el-switch
					    v-model="isdeveloper"
					    class="ml-2"
					    :width="60"
						 style="--el-switch-on-color: #ff4949; --el-switch-off-color:  #13ce66"
					    inline-prompt
					    active-text="高级"
					    inactive-text="常规"
					  />
				</el-col>
			</el-row>
			<el-row v-if="isdeveloper">
				<el-col class="text-ve-center"  :span="6">AppKey</el-col>
				<el-col :span="16">
				<el-input v-model="purchAppkey"  type="password" placeholder="Open1688 开发者ID" />
				</el-col>
			</el-row>
			<el-row  v-if="isdeveloper">
				<el-col class="text-ve-center"  :span="6">AppSecret</el-col>
				<el-col :span="16">
				<el-input v-model="purchAppSecret"  type="password" placeholder="Open1688 开发者密码" />
				</el-col>
			</el-row>
			
		</div>	
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="bindVisible = false">取消</el-button>
				<el-button type="primary" @click="bindAuth"  v-if="isauth">授权</el-button>
				<el-button type="primary" @click="updateName"  v-else>保存</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import { ref,reactive,onMounted } from 'vue'
    import {Search,ArrowDown,Edit} from '@element-plus/icons-vue'
	import {dateFormat} from '@/utils/index.js';
	import {ElMessage} from 'element-plus';
	import purchasealibabaApi from '@/api/erp/purchase/open1688/purchasealibabaApi.js';
    import router from "@/router";
    export default{
        name: '1688绑定',
        components:{
            Help,
            Search,Edit,
            Plus,
            ArrowDown
        },
        setup(){
			let purchName = ref("");
			let purchid= ref("");
			let purchAppkey = ref("");
			let purchAppSecret = ref("");
            let bindVisible =ref(false)
			let isauth=ref(true);
			let isdeveloper=ref(false);
            let tableData=reactive({list:[]})
            //方法
			onMounted(()=>{
				getauthList()
			//授权成功后解析地址
				GetRequest()
			})
			//获取授权列表
			function getauthList(){
				purchasealibabaApi.getAuthData().then((res)=>{
					tableData.list = res.data
					tableData.list.forEach((item)=>{
						item.visible = false
					})
				})
			}
			//绑定账号
			function showDialog(row){
			 if(row.id){
				 purchid.value=row.id;
				 purchName.value=row.name;
				 purchAppkey.value=row.appkey;
				 purchAppSecret.value=row.appsecret;
				 isauth.value=false;
			 }else{
				 isauth.value=true;
				 purchName.value='';
				 purchAppkey.value='';
				 purchAppSecret.value='';
			  }
			   bindVisible.value = true	
			}
			//获取id
			function getcode(){
			  purchasealibabaApi.submitname({
				  "name":purchName.value,
				  "appkey":purchAppkey.value,
				  "appsecret":purchAppSecret.value
			  }).then((res)=>{ 
				 goUrl(res.data.id);
			  })
			}
			//解绑
			function removeBind(id){
				purchasealibabaApi.logicDelete({"id":id}).then((res)=>{
					if(res.code == "201"){
						ElMessage.success('解绑成功！');
						getauthList();
					}
				})
			}
			//
			function GetRequest() {  
			    var url = location.href;
			    var object = {};
			    if(url.indexOf("?") != -1)//url中存在问号，也就说有参数。  
			    {   
			      var str = url.split("?")[1];  //得到?后面的字符串
			      var strs = str.split("&");  //将得到的参数分隔成数组[id="123456",Name="bicycle"];
			      for(var i = 0; i < strs.length; i ++)  
			        {   
			　　　　　　　　object[strs[i].split("=")[0]]=strs[i].split("=")[1]
			　　　　}
			　　}
			    
			   if(object.code){
					   if(!object.state){ 
						   object.state="0";
					   } 
					   purchasealibabaApi.bindAuthData({
					   				  "code":object.code,
					   				  "state":object.state,
					   }).then((res)=>{
								ElMessage.success('绑定成功！');
								router.push(window.location.pathname);
					   })
			   } 
			}  
			function refreshAuthData(id){
				purchasealibabaApi.refreshAuthData({"id":id}).then((res)=>{
					if(res.code == "201"){
						ElMessage.success( '刷新成功！');
						getauthList();
					}
				})
			}
			function updateName(){
				if(purchName.value){
					purchasealibabaApi.submitname({
						              "id":purchid.value,
									  "name":purchName.value,
									  "appkey":purchAppkey.value,
									  "appsecret":purchAppSecret.value
					}).then((res)=>{  
									ElMessage.success( '修改成功！');
									bindVisible.value =false;
									getauthList();
					});
				}else{
					ElMessage.error('名称不能为空！');
				}
			}
 
			//获取1688链接
			function goUrl(countid){
				purchasealibabaApi.get1688Url(countid) ;
			}
			//授权
			function bindAuth(){
				if(purchName.value){
					   getcode()
				}else{
					ElMessage.error('名称不能为空！');
				}
			}
			function edit(row){
				row.names = row.name  
			}
            //数据接收
            return{
                bindVisible,showDialog,removeBind,edit,
				dateFormat,purchid,purchName,purchAppkey,isauth,
				purchAppSecret,updateName,
                tableData,GetRequest,
				bindAuth,
				getcode,
				getauthList,
				goUrl,
				refreshAuthData,
				isdeveloper,
            }
        }

    }
</script>
<style>
	.text-ve-center{
		align-items: center;
		display: flex;
	}
	.el-alert{margin-bottom:16px}
	.icon{font-size: 14px;display:flex;cursor: pointer;}
	.from-body .el-row{margin-bottom:32px;}
    .con-header .el-row{margin-bottom:16px;}
	.plant-img{width:100px;display: flex;}
</style>