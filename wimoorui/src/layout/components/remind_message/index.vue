<template>
	  <el-badge :value="number"  :max="99"
	   :hidden="number===0?true:false"
	   class="item"  @click="handleMessage" >
	    <remind theme="outline"  class="pointer ic-cen" size="16" />
	  </el-badge>
	 
	<el-drawer  v-model="drawer" class="remindmessage"  @closed="handleCloseDrawer" size="640">
	   <template #header="{ close, titleId, titleClass }">
			 <el-tabs :id="titleId" :class="titleClass" v-model="activeName" class="mess-tab-title" @tab-change="handleTabChange">
			    <el-tab-pane label="系统消息" name="sys">
					<template #label>
					        <span class="custom-tabs-label">
					          <span>系统消息</span>
					        </span>
					      </template>
				</el-tab-pane>
			    <el-tab-pane label="我的提醒" name="nti">
					<template #label>
					        <span class="custom-tabs-label">
					          <span>我的提醒</span>
					        </span>
					      </template>
				</el-tab-pane>
			    <el-tab-pane label="我的私信" name="mes">
					<template #label>
					        <span class="custom-tabs-label">
					          <span>我的私信</span>
					        </span>
					      </template>
				</el-tab-pane>
				<el-tab-pane label="全部已读" name="dis"  :disabled="true">
					<template #label>
					        <span class="font-small text-blue pointer m-r-8" @click="handleRead">
					          <span>全部已读</span>
					        </span>
					      </template>
				</el-tab-pane>
			
			  </el-tabs>
	      </template>
		<div v-if="activeName=='mes'"  >
			<el-row :gutter="20">
			    <el-col :span="6">
					<div>
					   <el-table :data="userlist" 
					              highlight-current-row
								  style="width: 100%" 
								  height="calc(100vh - 125px)"
					             :cell-style="{padding:'5px',border:0,borderRadius:'8px'}" 
								 :row-style="{padding:0,border:0}" 
								  row-key="id" 
								  class="userAvatarTable"
								 :scrollbar-always-on="true"
								  ref="userlistAvatarTableRef"
								  @current-change="handleCurrentChange"
								 :show-header="false">
						<el-table-column prop="name" show-overflow-tooltip label="name"   >
							 <template #default="scope">
								  <el-avatar :style="'background-color:'+colors[parseInt(scope.row.id)%6]">  {{scope.row.name.charAt(0)}} </el-avatar> {{scope.row.name}}
							 </template>
						</el-table-column>		 
					   </el-table>
					</div>
				</el-col>
			    <el-col :span="18">
					<GlobalTable  :inDialog="true" height="calc(100vh - 256px)" ref="globalTableRef" :tableData="tableData"   @loadTable="loadTableData"
					 :defaultSort="{ prop: 'createdAt', order: 'descending' }"  
					 :showHeader="false"
					 :stripe="false"
					 rowKey="id"
					 >
					<template #field>
						<el-table-column label="title"  prop="title" >
								 <template #default="scope">
									 <div class="message-wrapper">
										 <div class="mWimageB" 
										 v-if="scope.row.icon">
											<el-icon color="#fff"><Comment /></el-icon>
										 </div>
										 <div class="mWimage" 
										 v-else>
											<el-icon color="#fff"><BellFilled /></el-icon>
										 </div>
										 <div class="mWtext">
											 <div class="mWcon">
												  <el-tooltip
												         effect="dark"
												        
												         placement="top-start"
												       >
													   <template #content><div v-html="scope.row.title"></div> </template>
												        <div class="font24 text-omit-1" 
														v-html="scope.row.title"
														></div>
												       </el-tooltip>
												  <div class="font-extraSmall ">
												  <span  v-if="scope.row.isRead">已读</span>
												  <span style="padding-left:16px;" v-else>未读</span>
												  </div>
											 </div>
											 <span class="font-extraSmall"  style="white-space: nowrap;">
												 {{scope.row.createdAt}}
											 </span>
										 </div>
									 </div>
									  <div class="message-body message-content" v-html="scope.row.content"></div>
							     </template>
						</el-table-column>
					</template>
					</GlobalTable>
					<div  style='padding-top:10px;' >
				<el-input type="textarea" v-model="message"></el-input>
				<el-button size="small" @click="submitMessageHandle">提交</el-button>
				</div></el-col>
			  </el-row>
			
		</div>
	    <GlobalTable v-else :inDialog="true" :height="activeName=='mes'?'calc(100vh - 256px)':'calc(100vh - 156px)'" ref="globalTableRef" :tableData="tableData"   @loadTable="loadTableData" 
	     :defaultSort="{ prop: 'createdAt', order: 'descending' }"  
		 :showHeader="false"
		 :stripe="false"
	     rowKey="id"
	     >
	    <template #field>
			<el-table-column label="title"  prop="title" >
					 <template #default="scope">
						 <div class="message-wrapper">
							 <div class="mWimageB" 
							 v-if="scope.row.icon">
								<el-icon color="#fff"><Comment /></el-icon>
							 </div>
							 <div class="mWimage" 
							 v-else>
								<el-icon color="#fff"><BellFilled /></el-icon>
							 </div>
							 <div class="mWtext">
								 <div class="mWcon">
									  <el-tooltip
									         effect="dark"
									         :content="scope.row.ntitle"
									         placement="top-start"
									       >
									        <div class="font24 text-omit-1" 
											v-html="scope.row.ntitle"
											></div>
									       </el-tooltip>
									  <div class="font-extraSmall ">
									  <span style="padding-right:16px;"
									   v-if="scope.row.systype">{{scope.row.systype}}</span> 
									  <span  v-if="scope.row.isRead">已读</span>
									  <span style="padding-left:16px;" v-else>未读</span>
									  </div>
								 </div>
								 <span class="font-extraSmall"  style="white-space: nowrap;">
									 {{scope.row.createdAt}}
								 </span>
							 </div>
						 </div>
						  <div class="message-body message-content" v-html="scope.row.content"></div>
				     </template>
			</el-table-column>
		</template>
		</GlobalTable>
	  </el-drawer>
	  
	 <el-dialog
	    v-model="visible"
	    title="提醒"
	    width="50%"
		top="6vh"
		class="skusearchdialog"
		:destroy-on-close='true' 
	  >
	   <el-table :show-header="false" :data="listNotify" height="calc(100vh - 450px)">
		   <el-table-column label="title"  prop="title" >
		   		 <template #default="scope">
		   			  <h3 v-html="scope.row.title"></h3>
		   			  <div class="font-extraSmall message-content">{{dateTimesFormat(scope.row.createdat)}}
					  <span style="padding-left:20px;" v-if="scope.row.isRead">已读</span>
					  <span style="padding-left:20px;" v-else>未读</span>
					  </div>
		   			  <div class="message-body message-content" v-html="scope.row.content"></div>
		   	     </template>
		   </el-table-column>
	   </el-table>
	    <template #footer>
	      <span class="dialog-footer">
	        <el-button type="primary" @click="visible = false">关闭</el-button>
	      </span>
	    </template>
	  </el-dialog> 
</template>

<script setup>
	import {Remind,MessageUnread,MessageSuccess} from '@icon-park/vue-next';
	import {ref,reactive,onMounted,watch,inject,toRefs,nextTick} from 'vue';
	import { BellFilled,Comment } from '@element-plus/icons-vue';
	import { ElMessage } from 'element-plus';
	import notify from "@/api/sys/admin/notify.js";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import userApi from '@/api/sys/admin/userApi.js';
	const globalTableRef=ref();
	const userlistAvatarTableRef=ref();
	const state = reactive({
		drawer:false,
		visible:false,
		activeName:"sys",
		typenoread:{sys:false,nti:false,mes:false},
		userinfo:{},
		queryParams:{},
		listNotify:[],
		number:0,
		message:"",
		userlist:[],
		currentRow:null,
		tableData:{records:[],total:0},
	})
	const colors=["#f56c6c","#909399","#409eff","#e6a23c","#67c23a","#c792ea"];
	const {
		drawer,activeName,tableData,queryParams,number,typenoread,visible,listNotify,userlist,message,currentRow
	}=toRefs(state);
	var invt=null;
	var num=0;
	function loadMessage(){
		notify.findNoReadByUserAll().then(res=>{
			if(res&&res.data&&res.data.length>0){
				var needshow=false;
				res.data.forEach(item=>{
					if(item.action=="timely"){
						needshow=true;
					}
				})
				if(needshow){
					state.visible=true;
					 nextTick(()=>{
					     state.listNotify=res.data;
					 })
				}
			}
		})
	}
	function handleCloseDrawer(){
		clearInterval(invt);
	}
	function show(userinfo){
		num=0;
		state.userinfo=userinfo;
		notify.pullMessage().then(res=>{
			loadMessage();
		});
		userApi.findOwnerAll().then((res)=>{
			state.userlist=res.data;
			state.userlist.unshift({name:"系统管理员",id:0,account:"admin@wimoor.com"});
			state.currentRow=null;
			});
        setInterval(function(){
			loadMessage();
		},100000);
		notify.findNitofyNums().then(res=>{
			var num=0;
			res.data.forEach(item=>{
				if(item.ftype==2){
					num=item.nums;
				}
			});
			state.number=parseInt(num);
		}) 
	}
	function handleRead(){
		notify.updateAllTargets().then(res=>{
			 ElMessage.success('全部标记为已读!');
		})
	}
	function submitMessageHandle(){
		num=0;
		if(state.currentRow!=null){
			 state.currentRow.content=state.message;
			  notify.submitMsg(state.currentRow).then(res=>{
				   ElMessage.success('提交成功');
				   handleQuery();
				   state.message="";
				   loadMessage();
			  })
		}else{
			 ElMessage.error('请选择对象');
		}
	}
	
	function handleTabChange(){
		handleQuery();
		num=0;
		if(state.activeName=="mes"&&num<100){
			invt=setInterval(function(){
				 num++;
				 notify.getMessage(state.queryParams).then(res=>{
					 if(res.data.records.length!=state.tableData.records.length){
						 handleQuery();
					 }
				 });
			},2000);
		}else{
			clearInterval(invt);
		}
	}
	function handleCurrentChange(row){
		num=0;
		state.currentRow=row;
	}
	function loadTableData(param){
		   if(state.activeName=="sys"){
			   notify.getAnnounce(param).then(res=>{
					res.data.records.forEach(item=>{
						item.ntitle =  item.title.slice(6)
						item.systype = item.title.slice(1,5)
						if(item.systype==='系统公告'){
							item.icon = 'sysgg'
						}
					})
					state.tableData.records=res.data.records;
					state.tableData.total=res.data.total;
					
			   })
		   }
		   else if(state.activeName=="nti"){
			   notify.getRemind(param).then(res=>{
				   res.data.records.forEach(item=>{
				   	item.ntitle =  item.title
				   })
					 state.tableData.records=res.data.records;
					 state.tableData.total=res.data.total;
			   })
		   }
		  else if(state.activeName=="mes"){
			 notify.getMessage(param).then(res=>{
				 res.data.records.forEach(item=>{
				 	item.ntitle =  item.title.slice(6)
				 	item.systype = item.title.slice(1,5)
				 })
					state.tableData.records=res.data.records;
					state.tableData.total=res.data.total;
			 }) 
		  }
	}
	function handleQuery(){
		globalTableRef.value.loadTable(state.queryParams);
	}
	function handleMessage(){
		state.drawer=true;
		var timer=setTimeout(function(){
					handleQuery();
		},300)
	}
	defineExpose({show});
</script>
<style>

	.message-wrapper{
		display: flex;
	}
	.mWtext{
		display: flex;
		justify-content: space-between;
		flex-grow:1;
		margin-bottom: 16px;
	}
   .mess-tab-title .el-tabs__nav{
	   width: 100%;
   }
   .message .el-badge__content{
    position: absolute;
    top: 0px !important;
    right: calc(10px + var(--el-badge-size)/ 2) !important;
    transform: translateY(-50%) translateX(100%);
}
	.skusearchdialog .el-dialog__body{
		padding:0 0 10px 0;
	}
	.remindmessage .is-disabled{
		flex:1;
		justify-content: end;
	}
	.remindmessage .el-tabs__header{
		margin:0;
	}
	.remindmessage	.el-drawer__header{
		margin-bottom: 0;
	}
	.remindmessage	.el-table__row .el-table__cell{
		padding-top:24px;
		padding-bottom:24px;
	}
	.userAvatarTable .el-table__body tr.current-row>td.el-table__cell {
	    background-color: #f7cfc3 !important;
	}
	.dark .userAvatarTable .el-table__body tr.current-row>td.el-table__cell {
	    background-color: #4802f7 !important;
	}
</style>
<style scoped>
  .font24{
	font-size:18px;
	font-weight:700;
	margin-bottom:4px;
	margin-right:24px;
}
	.skusearchdialog {
			 margin: var(--el-dialog-margin-top,15vh) auto 10px !important;
	}
.message-body{
	color:var(--el-text-color-regular);
}
.dark .message-body{
	color:var(--el-text-color-regular);
}
	.mWimage{
		background-color: var(--el-color-primary-light-1);
		width:32px;
		height:32px;
		border-radius:16px;
		display: flex;
		flex-shrink: 0;
		align-items: center;
		justify-content: center;
		margin-right:12px;
	}
	.mWimageB{
		background-color: var(--el-color-blue-light-1);
		width:32px;
		height:32px;
		border-radius:16px;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right:12px;
	}
	.message-content{
		margin-left:42px
	}
	

</style>